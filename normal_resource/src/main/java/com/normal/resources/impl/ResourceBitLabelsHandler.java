package com.normal.resources.impl;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResourceBitLabelsHandler extends BaseTypeHandler<ResourceBitLabels> {

    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, ResourceBitLabels resourceBitLabels, JdbcType jdbcType) throws SQLException {
        preparedStatement.setInt(i, resourceBitLabels.toInt());
    }

    @Override
    public ResourceBitLabels getNullableResult(ResultSet resultSet, String s) throws SQLException {
        int anInt = resultSet.getInt(s);
        return new ResourceBitLabels(anInt);
    }

    @Override
    public ResourceBitLabels getNullableResult(ResultSet resultSet, int i) throws SQLException {
        int anInt = resultSet.getInt(i);
        return new ResourceBitLabels(anInt);
    }

    @Override
    public ResourceBitLabels getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        int anInt = callableStatement.getInt(i);
        return new ResourceBitLabels(anInt);
    }
}