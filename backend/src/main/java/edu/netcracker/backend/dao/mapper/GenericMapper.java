package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
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

@Log4j2
@AllArgsConstructor
public class GenericMapper<T> implements RowMapper<T> {

    private Class<T> entityClass;
    private Map<Field, PrimaryKey> fieldPrimaryKeyMap = new HashMap<>();
    private Map<Field, Attribute> fieldAttributeMap = new HashMap<>();

    @Override
    public T mapRow(ResultSet rs, int rowNum) {
        try {
            return map(rs);
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    private T map(ResultSet rs) throws Exception {
        T entity = entityClass.getConstructor()
                        .newInstance();

        for (Map.Entry<Field, PrimaryKey> entry : fieldPrimaryKeyMap.entrySet()) {
            Field field = entry.getKey();
            PrimaryKey primaryKey = entry.getValue();

            String dbColumn = primaryKey.value();
            Object attr = castTypes(rs.getObject(dbColumn), field.getType());
            field.set(entity, attr);
        }

        for (Map.Entry<Field, Attribute> entry : fieldAttributeMap.entrySet()) {
            Field field = entry.getKey();
            Attribute attribute = entry.getValue();

            String dbColumn = attribute.value();
            Object attr = castTypes(rs.getObject(dbColumn), field.getType());
            field.set(entity, attr);
        }

        return entity;
    }

    private Object castTypes(Object attr, Class<?> fieldType) {
        if (attr == null) {
            return null;
        }

        if (attr instanceof BigDecimal) {
            BigDecimal bd = (BigDecimal) attr;
            if (fieldType.equals(Integer.class)) {
                attr = bd.intValueExact();
            } else if (fieldType.equals(Long.class)) {
                attr = bd.longValueExact();
            }

        } else if (attr instanceof Number) {
            Number bd = (Number) attr;
            if (fieldType.equals(Integer.class)) {
                attr = toIntExact(bd.longValue());
            } else if (fieldType.equals(Long.class)) {
                attr = bd.longValue();
            }

        } else if (attr instanceof Date && fieldType.equals(LocalDate.class)) {
            Date date = (Date) attr;
            attr = date.toLocalDate();

        } else if (attr instanceof Timestamp && fieldType.equals(LocalDateTime.class)) {
            Timestamp timestamp = (Timestamp) attr;
            attr = timestamp.toLocalDateTime();

        } else if (attr instanceof Timestamp && fieldType.equals(LocalDate.class)) {
            Timestamp timestamp = (Timestamp) attr;
            attr = timestamp.toLocalDateTime()
                            .toLocalDate();
        }

        return attr;
    }
}
