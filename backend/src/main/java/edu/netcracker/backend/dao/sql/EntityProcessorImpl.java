package edu.netcracker.backend.dao.sql;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.impl.CrudDAOImpl;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;

@Log4j2
@Getter
public class EntityProcessorImpl<T> implements EntityProcessor<T> {

    private final Class<?> entityClass;

    private Field primaryKeyField;
    private PrimaryKey primaryKey;

    private Map<Field, Attribute> fieldAttributeMap = new HashMap<>();

    public EntityProcessorImpl(Class<?> entityClass) {
        this.entityClass = entityClass;
        resolveFields();
    }

    public void putGeneratedPrimaryKey(KeyHolder holder, T entity) throws IllegalAccessException {
        Number result = (Number) Objects.requireNonNull(holder.getKeys())
                                        .get(primaryKey.value());
        Class<?> fieldType = primaryKeyField.getType();
        if (fieldType.equals(Long.class)) {
            primaryKeyField.set(entity, result.longValue());
        } else if (fieldType.equals(Integer.class)) {
            primaryKeyField.set(entity, toIntExact(result.longValue()));
        }
    }

    public Object[] resolveCreateParameters(T entity) {
        List<Object> objects = new ArrayList<>();
        addParams(objects, entity, fieldAttributeMap);
        return objects.toArray();
    }

    public Object[] resolveUpdateParameters(T entity) {
        List<Object> objects = new ArrayList<>();
        addParams(objects, entity, fieldAttributeMap);
        addParams(objects, entity, primaryKeyField);
        return objects.toArray();
    }

    public Object[] resolvePrimaryKeyParameters(T entity) {
        List<Object> objects = new ArrayList<>();
        addParams(objects, entity, primaryKeyField);
        return objects.toArray();
    }

    private void addParams(List<Object> objects, T entity, Map<Field, ?> currMapper) {
        objects.addAll(currMapper.keySet()
                                 .stream()
                                 .map((var) -> {
                                     try {
                                         return var.get(entity);
                                     } catch (IllegalAccessException e) {
                                         log.error(e);
                                         return null;
                                     }
                                 })
                                 .collect(Collectors.toList()));
    }

    private void addParams(List<Object> objects, T entity, Field field) {
        try {
            objects.add(field.get(entity));
        } catch (IllegalAccessException e) {
            log.error(e);
            throw new RuntimeException(e);
        }
    }

    private void resolveFields() {
        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            Attribute attribute = field.getAnnotation(Attribute.class);
            if (attribute != null) {
                fieldAttributeMap.put(field, attribute);
            }

            PrimaryKey primaryKeyOpt = field.getAnnotation(PrimaryKey.class);
            if (this.primaryKeyField == null && primaryKeyOpt != null) {
                this.primaryKeyField = field;
                this.primaryKey = primaryKeyOpt;
            } else if (this.primaryKeyField != null && primaryKeyOpt != null) {
                throw new BeanInstantiationException(CrudDAOImpl.class, "Duplicate primary keys found in entity");
            }
        }

        if (this.primaryKeyField == null) {
            throw new BeanInstantiationException(CrudDAOImpl.class, "Primary key not found in entity");
        }
    }
}
