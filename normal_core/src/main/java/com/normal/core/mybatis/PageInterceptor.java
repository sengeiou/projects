package com.normal.core.mybatis;

import com.normal.core.utils.Refracts;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.jdbc.ConnectionLogger;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Intercepts({@Signature(method = "query", type = Executor.class, args = {MappedStatement.class, Object.class,
        RowBounds.class, ResultHandler.class})})
public class PageInterceptor implements Interceptor {


    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Executor executor = (Executor) invocation.getTarget();
        final Object[] args = invocation.getArgs();
        final MappedStatement ms = (MappedStatement) args[0];
        final Object parameter = args[1];
        final RowBounds rowBounds = (RowBounds) args[2];
        final ResultHandler resultHandler = (ResultHandler) args[3];

        boolean isPageMethod = parameter instanceof PageParam;
        if (!isPageMethod) {
            return invocation.proceed();
        }
        PageParam pageParam = (PageParam) parameter;

        Page<Object> page = new Page<>();
        page.setPageNo(pageParam.getPageNo());

        BoundSql rawBoundSql = ms.getBoundSql(parameter);

        String sql = rawBoundSql.getSql();

        String countSql = new StringBuffer().append("select count(1) from (").append(sql).append(") a").toString();
        Refracts.setField(rawBoundSql, "sql", countSql);
        Long count = getCount(executor, ms, rawBoundSql);

        page.setTotalRecord(count.intValue());

        List<ParameterMapping> parameterMappings = rawBoundSql.getParameterMappings();

        List<ParameterMapping> pageParameterMappings = new ArrayList<>();
        pageParameterMappings.addAll(parameterMappings);
        pageParameterMappings.add(
                new ParameterMapping.Builder(ms.getConfiguration(), "offset", Integer.class)
                        .expression("#{offset}")
                        .build());

        pageParameterMappings.add(
                new ParameterMapping.Builder(ms.getConfiguration(), "pageSize", Integer.class)
                        .expression("#{pageSize}")
                        .build());

        BoundSql listBoundSql = new BoundSql(ms.getConfiguration(),
                new StringBuffer(sql).append(" limit ?, ?").toString(),
                pageParameterMappings, pageParam);

        CacheKey dataKey = executor.createCacheKey(ms, pageParam, RowBounds.DEFAULT, listBoundSql);

        List<Object> listData = executor.query(ms, pageParam, rowBounds, resultHandler, dataKey, listBoundSql);
        page.setResults(listData);
        Pages.setPageLocal(page);
        return listData;
    }

    private Long getCount(Executor executor, MappedStatement ms, BoundSql countSql) throws SQLException {
        Log log = ms.getStatementLog();
        Connection conn = executor.getTransaction().getConnection();
        if (log.isDebugEnabled()) {
            conn = ConnectionLogger.newInstance(conn, log, 1);
        }
        PreparedStatement countStmt = conn.prepareStatement(countSql.getSql());
        DefaultParameterHandler handler = new DefaultParameterHandler(ms, countSql.getParameterObject(), countSql);
        handler.setParameters(countStmt);

        ResultSet rs = null;
        try {
            rs = countStmt.executeQuery();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ignore) {
                }
            }
            if (countStmt != null) {
                try {
                    countStmt.close();
                } catch (SQLException ignore) {
                }
            }
        }
        return 0L;
    }

    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}