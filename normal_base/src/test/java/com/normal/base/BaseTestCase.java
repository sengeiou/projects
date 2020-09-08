package com.normal.base;

import com.normal.base.query.QueryMapper;
import com.normal.base.query.QueryService;
import com.normal.base.query.QuerySql;
import org.apache.ibatis.session.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author: fei.he
 */
public class BaseTestCase {
    public static final Logger logger = LoggerFactory.getLogger(BaseTestCase.class);

    private SqlSession sqlSession;
    private QueryMapper mapper;


    public void init() throws Exception {
        SqlSessionFactory build = new SqlSessionFactoryBuilder().build(new FileReader("F:\\projects\\normal_base\\src\\test\\resources\\db-config.xml"));
        this.sqlSession = build.openSession(true);
        this.mapper = this.sqlSession.getMapper(QueryMapper.class);
    }

    @Test
    public void testQuerySql() {
        QuerySql   querySql = QuerySql.newInstance();
        String rst = querySql.fromTable("table")
                .column("a_b_cd_efg")
                .column("ef")
                .withEqCond("cc", "123")
                .toString();
        logger.info(rst);
    }

    @Test
    public void testQueryMapper() throws Exception{
        init();
        QuerySql sql = QuerySql.newInstance()
                .fromTable("trd_shop_banner")
                .column("material_id")
                .column("image")
                .withEqCond("id", "1");

        List<Map<String, Object>> rst = mapper.query(sql);
        rst.stream().forEach((item) -> {
            for (Map.Entry<String, Object> entry : item.entrySet()) {
                logger.info("key: {}, value:{}", entry.getKey(), entry.getValue());
            }
        });
    }

    @Test
    public void testQueryService() throws Exception{
        init();
        QuerySql sql = QuerySql.newInstance()
                .fromTable("trd_shop_banner")
                .column("material_id")
                .column("banner_title")
                .withEqCond("id", "1");
        QueryService queryService = new QueryService();
        Field queryMapper = QueryService.class.getDeclaredField("queryMapper");
        queryMapper.setAccessible(true);
        queryMapper.set(queryService, this.mapper);

        Map materialId = queryService.query(sql, Map.class);
        logger.info("materialId: {}", materialId);

    }

    @Test
    public void testPrimitive() {
        logger.info(String.valueOf(int.class.isPrimitive()));
    }

    @AfterEach
    public void clear() {
        if (sqlSession != null) {
            sqlSession.close();
        }
    }
}
