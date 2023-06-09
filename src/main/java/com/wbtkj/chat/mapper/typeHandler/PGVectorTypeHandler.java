package com.wbtkj.chat.mapper.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PGVectorTypeHandler extends BaseTypeHandler<List<BigDecimal>> {
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<BigDecimal> parameter, JdbcType jdbcType) throws SQLException {
        PGobject pgObject = new PGobject();
        pgObject.setType("vector");
        pgObject.setValue(toPGString(parameter));
        ps.setObject(i, pgObject);
    }


    @Override
    public List<BigDecimal> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String pgString = rs.getString(columnName);
        return fromPGString(pgString);
    }

    @Override
    public List<BigDecimal> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String pgString = rs.getString(columnIndex);
        return fromPGString(pgString);
    }

    @Override
    public List<BigDecimal> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String pgString = cs.getString(columnIndex);
        return fromPGString(pgString);
    }

    private String toPGString(List<BigDecimal> vector) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (BigDecimal d : vector) {
            sb.append(d.toString()).append(",");
        }
        sb.setLength(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }

    private List<BigDecimal> fromPGString(String pgString) {
        if (pgString == null || pgString.isEmpty()) {
            return null;
        }
        String[] parts = pgString.substring(1, pgString.length() - 1).split(",");
        List<BigDecimal> result = new ArrayList<>();
        for (String part : parts) {
            result.add(new BigDecimal(part));
        }
        return result;
    }
}