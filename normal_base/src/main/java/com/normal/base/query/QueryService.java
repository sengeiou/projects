package com.normal.base.query;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: fei.he
 */
public class QueryService {

    @Autowired
    private SqlSession sqlSession;

    public <T> T query(QuerySql sql, Class<T> clazz) {
           sqlSession.select(sql.toString(), new ResultHandler() {
            @Override
            public void handleResult(ResultContext context) {
             }

        });

    }
}
