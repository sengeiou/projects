package com.normal.base.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class QuerySql {
    private List<String> columns = new ArrayList<>(2);
    private String table;
    private Map<String, String> eqConds = new HashMap<>(8);

    private QuerySql() {
    }

    public static QuerySql newInstance() {
        return new QuerySql();
    }

    public QuerySql column(String column) {
        this.columns.add(column);
        return this;
    }

    public QuerySql fromTable(String table) {
        this.table = table;
        return this;
    }

    public QuerySql withEqCond(String condColumn, String condValue) {
        eqConds.put(condColumn, condValue);
        return this;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("select ")
                .append(columns.stream().collect(Collectors.joining(",")))
                .append(" from ")
                .append(table);

        if (!eqConds.entrySet().isEmpty()) {
            sb.append(" where ")
                    .append(eqConds.entrySet().stream().map((entry) -> entry.getKey() + " = '" + entry.getValue() + "'").collect(Collectors.joining(" and")));
        }
        return sb.toString();
    }
}
