package com.normal.base.query;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class QuerySql {
    private List<String> columns = new ArrayList<>(2);
    private List<String> columnAlias = new ArrayList<>(2);
    private String table;
    private Map<String, String> eqConds = new HashMap<>(8);

    private QuerySql() {
    }

    public static QuerySql newInstance() {
        return new QuerySql();
    }

    public QuerySql column(String column) {
        this.columns.add(column);
        this.columnAlias.add(alias(column));
        return this;
    }

    private String alias(String column) {
        StringBuffer rst = new StringBuffer();
        boolean nextUpper = false;
        for (int i = 0; i < column.length(); i++) {
            char each = column.charAt(i);
            if (String.valueOf(each).equals("_")) {
                nextUpper = true;
                continue;
            }
            if (nextUpper) {
                rst.append(String.valueOf(each).toUpperCase());
                nextUpper = false;
                continue;
            }

            rst.append(each);
        }
        return rst.toString();
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
                .append(getSelectColumns())
                .append(" from ")
                .append(table);

        if (!eqConds.entrySet().isEmpty()) {
            sb.append(" where ")
                    .append(eqConds.entrySet().stream().map((entry) -> entry.getKey() + " = '" + entry.getValue() + "'").collect(Collectors.joining(" and")));
        }
        return sb.toString();
    }

    private String getSelectColumns() {
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < columns.size(); i++) {
            joiner.add(columns.get(i) + " as " + columnAlias.get(i));
        }
        return joiner.toString();
    }

    /**
     * 查询单个字段值使用
     *
     * @return
     */
    public String getSimpleColumnAlias() {
        if (this.columnAlias.size() == 1) {
            return columnAlias.get(0);
        }
        throw new IllegalArgumentException("参数错误.查询多个查询列. column alias size: " + columnAlias.size());
    }
}
