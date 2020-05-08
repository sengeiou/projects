package com.normal.core.mybatis;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.hibernate.validator.cfg.context.Cascadable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class }),
        @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
                RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class }), })
public class PageInterceptor implements Interceptor {

    private static final Logger logger = LoggerFactory.getLogger(PageInterceptor.class);

    private SqlSession sqlSession;
    public PageInterceptor() {
	}
    public PageInterceptor(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
	}

	@Override
    public Object intercept(final Invocation invocation) throws Throwable {

        final Object[] args = invocation.getArgs();
        final MappedStatement ms = (MappedStatement) args[0];
        final Object parameter = args[1];
        final RowBounds rowBounds = (RowBounds) args[2];
        final ResultHandler resultHandler = (ResultHandler) args[3];
        final Executor executor = (Executor) invocation.getTarget();
        if (!(parameter instanceof PageParam && Page.class.equals(invocation.getMethod().getReturnType()))) {
            return invocation.proceed();
        }
        PageParam pageParam = (PageParam) parameter;
        CacheKey cacheKey;
        BoundSql boundSql;

        if (args.length == 4) {
            boundSql = ms.getBoundSql(parameter);
            cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
        } else {
            boundSql = (BoundSql) args[5];
            cacheKey = (CacheKey) args[4];
        }

        List<Object> pageList = executor.query(ms, pageParam, rowBounds, resultHandler, cacheKey, boundSql);

        Page<Object> page = new Page<Object>();
        page.setPageNo(pageParam.getPageNo());
        page.setResults(pageList);

        String countMsId = ms.getId() + "_count";
        Configuration configuration = ms.getConfiguration();
        MappedStatement countMs = configuration.getMappedStatement(countMsId);
        if (countMs == null) {
            // countMs = newCountMappedStatement(ms, countMsId);
            
            configuration.addMappedStatement(countMs);
        }

        
        return null;
    }

     
}