package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.mapper.GenericMapper;
import edu.netcracker.backend.dao.sql.PostgresSqlBuilder;
import edu.netcracker.backend.dao.sql.SQLBuilder;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;

@Log4j2
public abstract class CrudDAOImpl<T> implements CrudDAO<T> {

    private JdbcTemplate jdbcTemplate;
    private Class<T> entityClass;

    private String selectSql;
    private String createSql;
    private String updateSql;
    private String deleteSql;
    private String existsSql;
    private SQLBuilder sqlBuilder;

    private GenericMapper<T> genericMapper;
    private Map<Field, PrimaryKey> fieldPrimaryKeyMap = new HashMap<>();
    private Map<Field, Attribute> fieldAttributeMap = new HashMap<>();

    @Autowired
    public CrudDAOImpl() {
        // Hack to get generic type class
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        this.entityClass = (Class<T>) pt.getActualTypeArguments()[0];

        resolveFields();

        this.sqlBuilder = new PostgresSqlBuilder(entityClass, fieldPrimaryKeyMap, fieldAttributeMap);
        selectSql = sqlBuilder.assembleSelectSql();
        createSql = sqlBuilder.assembleInsertSql();
        updateSql = sqlBuilder.assembleUpdateSql();
        deleteSql = sqlBuilder.assembleDeleteSql();
        existsSql = sqlBuilder.assembleExistsSql();

        genericMapper = new GenericMapper<>(entityClass, fieldPrimaryKeyMap, fieldAttributeMap);
    }

    public Optional<T> find(Number id) {
        try {
            T entity = jdbcTemplate.queryForObject(selectSql, new Object[]{id}, genericMapper);
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<T> findIn(List<?> args) {
        if (args.size() == 0) {
            return new ArrayList<>();
        }
        return jdbcTemplate.query(sqlBuilder.assembleVariableSelectInSql(args.size()), args.toArray(), genericMapper);
    }

    public void save(T entity) {
        if (isAlreadyExists(entity)) {
            update(entity);
            return;
        }

        PreparedStatementCreator psc = connection -> {
            PreparedStatement ps = connection.prepareStatement(createSql, Statement.RETURN_GENERATED_KEYS);
            int i = 1; // 1 - first parameter in jdbc
            for (Object obj : resolveCreateParameters(entity)) {
                ps.setObject(i++, obj);
            }
            return ps;
        };

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, holder);

        try {
            putGeneratedPrimaryKey(holder, entity);
        } catch (IllegalAccessException e) {
            log.error(e);
        }
    }

    private void putGeneratedPrimaryKey(KeyHolder holder, T entity) throws IllegalAccessException {
        for (Field field : fieldPrimaryKeyMap.keySet()) {
            Number result = (Number) Objects.requireNonNull(holder.getKeys())
                                            .get(field.getAnnotation(PrimaryKey.class)
                                                     .value());
            Class<?> fieldType = field.getType();
            if (fieldType.equals(Long.class)) {
                field.set(entity, result.longValue());
            } else if (fieldType.equals(Integer.class)) {
                field.set(entity, toIntExact(result.longValue()));
            }
        }
    }

    public void delete(T entity) {
        jdbcTemplate.update(deleteSql, resolvePrimaryKeyParameters(entity));
    }

    protected void update(T entity) {
        jdbcTemplate.update(updateSql, resolveUpdateParameters(entity));
    }

    private boolean isAlreadyExists(T entity) {
        Long count = jdbcTemplate.queryForObject(existsSql, Long.class, resolvePrimaryKeyParameters(entity));
        return count > 0L;
    }

    private Object[] resolveCreateParameters(T entity) {
        List<Object> objects = new ArrayList<>();
        addParams(objects, entity, fieldAttributeMap);
        return objects.toArray();
    }

    private Object[] resolveUpdateParameters(T entity) {
        List<Object> objects = new ArrayList<>();
        addParams(objects, entity, fieldAttributeMap);
        addParams(objects, entity, fieldPrimaryKeyMap);
        return objects.toArray();
    }

    private Object[] resolvePrimaryKeyParameters(T entity) {
        List<Object> objects = new ArrayList<>();
        addParams(objects, entity, fieldPrimaryKeyMap);
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

    private void resolveFields() {
        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            Attribute attribute = field.getAnnotation(Attribute.class);
            if (attribute != null) {
                fieldAttributeMap.put(field, attribute);
            }

            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                fieldPrimaryKeyMap.put(field, primaryKey);
            }
        }
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public GenericMapper<T> getGenericMapper() {
        return genericMapper;
    }

    @Autowired
    private void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}