package com.normal.core.mybatis;

import com.normal.bizmodel.NormalEnum;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EnumTypeHandler implements TypeHandler<NormalEnum> {

    private Class<NormalEnum> clazz;

    public EnumTypeHandler(Class<NormalEnum> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, NormalEnum parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, parameter.key());
    }

    @Override
    public NormalEnum getResult(ResultSet rs, String columnName) throws SQLException {
        return null;
    }

    @Override
    public NormalEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        return null;
    }

    @Override
    public NormalEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return null;
    }
}
