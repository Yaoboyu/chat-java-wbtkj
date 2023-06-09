package com.wbtkj.chat.mapper.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class StringListTypeHandler extends BaseTypeHandler<List<String>> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType) throws SQLException {
        String[] array = parameter.toArray(new String[parameter.size()]);
        Array sqlArray = ps.getConnection().createArrayOf("varchar", array);
        ps.setArray(i, sqlArray);
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return convertArray(rs.getArray(columnName));
    }

    @Override
    public List<String> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return convertArray(rs.getArray(columnIndex));
    }

    @Override
    public List<String> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return convertArray(cs.getArray(columnIndex));
    }

    private List<String> convertArray(Array array) throws SQLException {
        if (array == null) {
            return null;
        }

        String[] arrayData = (String[]) array.getArray();
        return Arrays.asList(arrayData);
    }
}