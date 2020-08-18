package com.normal.bizassistant.autosend;

import com.normal.core.utils.ApplicationContextHolder;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: fei.he
 */
public class ImagePathTypeHandler implements TypeHandler<List<String>> {

    Environment env;

    @Override
    public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        String imagePaths = parameter.stream().collect(Collectors.joining(","));
        ps.setObject(i, imagePaths);
    }

    @Override
    public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
        return doGetResults(rs, columnName);
    }

    @Override
    public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
        return doGetResults(rs, columnIndex);
    }

    private List<String> doGetResults(ResultSet rs, Object param) throws SQLException {

        String images = null;
        if (param instanceof String) {
            images = rs.getString((String) param);
        } else {
            images = rs.getString((Integer) param);
        }
        return collect(images);
    }

    private List<String> collect(String images) {
        if (this.env == null) {
            env = ApplicationContextHolder.getContext().getEnvironment();
        }
        return Stream.of(images.split("."))
                .map((item) -> env.getProperty("autosend.images.path") + item)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String images = cs.getString(columnIndex);
        return collect(images);
    }

}
