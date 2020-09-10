package com.normal.base.mybatis;

import com.normal.model.NormalEnum;
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
        ps.setObject(i, parameter.key());
    }

    @Override
    public NormalEnum getResult(ResultSet rs, String columnName) throws SQLException {
        return getNormalEnum(rs.getString(columnName));
    }

    @Override
    public NormalEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
        return getNormalEnum(rs.getString(columnIndex));
    }

    private NormalEnum getNormalEnum(String key) {
        for (NormalEnum item : this.clazz.getEnumConstants()) {
            if (item.key().equals(key)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public NormalEnum getResult(CallableStatement cs, int columnIndex) throws SQLException {
        for (NormalEnum item : this.clazz.getEnumConstants()) {
            if (item.key().equals(cs.getString(columnIndex))) {
                return item;
            }
        }
        return null;
    }
}
