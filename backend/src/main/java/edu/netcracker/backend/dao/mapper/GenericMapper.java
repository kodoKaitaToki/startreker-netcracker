package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Math.toIntExact;

@Setter
public class GenericMapper<T> implements RowMapper<T> {

    private Class<T> clazz;
    private Map<Field, PrimaryKey> fieldPrimaryKeyMap = new HashMap<>();
    private Map<Field, Attribute> fieldAttributeMap = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(GenericMapper.class);

    @Override
    public T mapRow(ResultSet rs, int rowNum) {
        try {
            T entity = clazz.getConstructor().newInstance();
            for (Map.Entry<Field, PrimaryKey> entry : fieldPrimaryKeyMap.entrySet()) {
                Object attr;
                String dbColumn = entry.getValue().value();
                attr = castTypes(
                        rs.getObject(dbColumn),
                        entry.getKey().getType());
                entry.getKey().set(entity, attr);
            }
            for (Map.Entry<Field, Attribute> entry : fieldAttributeMap.entrySet()) {
                String dbColumn = entry.getValue().value();
                Object attr = castTypes(
                        rs.getObject(dbColumn),
                        entry.getKey().getType());
                entry.getKey().set(entity, attr);
            }
            return entity;
        }catch (Exception e){
            logger.error(e.toString());
            return null;
        }
    }

    private Object castTypes(Object attr, Class<?> fieldType) {
        if(attr == null) return null;
        if (attr instanceof BigDecimal) {
            BigDecimal bd = (BigDecimal) attr;
            if (fieldType.equals(Integer.class)) {
                attr = bd.intValueExact();
            } else if (fieldType.equals(Long.class)) {
                attr = bd.longValueExact();
            }
        }
        else if(attr instanceof Number){
            Number bd = (Number) attr;
            if (fieldType.equals(Integer.class)) {
                attr = toIntExact(bd.longValue());
            } else if (fieldType.equals(Long.class)) {
                attr = bd.longValue();
            }
        } else if(attr instanceof Date && fieldType.equals(LocalDate.class)){
            Date date = (Date) attr;
            attr = date.toLocalDate();
        } else if(attr instanceof Timestamp && fieldType.equals(LocalDateTime.class)){
            Timestamp timestamp = (Timestamp) attr;
            attr = timestamp.toLocalDateTime();
        }
        return attr;
    }
}
