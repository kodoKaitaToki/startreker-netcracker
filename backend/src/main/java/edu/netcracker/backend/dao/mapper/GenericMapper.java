package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@Setter
public class GenericMapper<T> implements RowMapper<T> {

    private Class<T> clazz;
    private Map<Field, PrimaryKey> primaryMapper = new HashMap<>();
    private Map<Field, Attribute> mapper = new HashMap<>();

    @Override
    public T mapRow(ResultSet rs, int rowNum) {
        try {
            T entity = clazz.getConstructor().newInstance();
            for (Map.Entry<Field, PrimaryKey> entry : primaryMapper.entrySet()) {
                Object attr;
                attr = castTypes(
                        rs.getObject(entry.getValue().value()),
                        entry.getKey().getGenericType().getTypeName());
                entry.getKey().set(entity, attr);
            }
            for (Map.Entry<Field, Attribute> entry : mapper.entrySet()) {
                Object attr = castTypes(
                        rs.getObject(entry.getValue().value()),
                        entry.getKey().getGenericType().getTypeName());
                entry.getKey().set(entity, attr);
            }
            return entity;
        }catch (Exception e){
            return null;
        }
    }

    private Object castTypes(Object attr, String fieldType) {
        if (attr instanceof BigDecimal) {
            BigDecimal bd = (BigDecimal) attr;
            if (fieldType.equals("java.lang.Integer")) {
                attr = bd.intValueExact();
            }
            if (fieldType.equals("java.lang.Long")) {
                attr = bd.longValueExact();
            }
        }
        return attr;
    }
}
