package com.normal.base.query;

import org.apache.ibatis.builder.annotation.ProviderMethodResolver;

/**
 * @author: fei.he
 */
public class QuerySqlProvider implements ProviderMethodResolver {


    public QuerySqlProvider() {
    }

    public String sql(QuerySql querySql) {
        return querySql.toString();
    }


}
