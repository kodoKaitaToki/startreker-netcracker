package edu.netcracker.backend.dao.sql;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.Field;
import java.util.Map;

public interface EntityProcessor<T> {

    void putGeneratedPrimaryKey(KeyHolder holder, T entity) throws IllegalAccessException;

    Object[] resolveCreateParameters(T entity);

    Object[] resolveUpdateParameters(T entity);

    Object[] resolvePrimaryKeyParameters(T entity);

    PrimaryKey getPrimaryKey();

    Field getPrimaryKeyField();

    Map<Field, Attribute> getFieldAttributeMap();
}
