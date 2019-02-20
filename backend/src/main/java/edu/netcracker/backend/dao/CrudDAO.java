package edu.netcracker.backend.dao;

import edu.netcracker.backend.dao.annotations.Attribute;
import edu.netcracker.backend.dao.annotations.PrimaryKey;
import edu.netcracker.backend.dao.mapper.GenericMapper;
import edu.netcracker.backend.dao.sql.PostgresSqlBuilder;
import edu.netcracker.backend.dao.sql.SQLBuilder;
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

import static java.lang.Math.toIntExact;

public abstract class CrudDAO<T> {

    private JdbcTemplate jdbcTemplate;
    private Class<T> clazz;
    private String selectSql;
    private String createSql;
    private String updateSql;
    private String deleteSql;
    private String existsSql;
    private GenericMapper<T> genericMapper;
    private Map<Field, PrimaryKey> fieldPrimaryKeyMap = new HashMap<>();
    private Map<Field, Attribute> fieldAttributeMap = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(CrudDAO.class);

    @Autowired
    public CrudDAO() {
        // Hack to get generic type class
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        this.clazz = (Class<T>) pt.getActualTypeArguments()[0];
        resolveFields();
        SQLBuilder builder = new PostgresSqlBuilder(clazz, fieldPrimaryKeyMap, fieldAttributeMap);
        selectSql = builder.assembleSelectSql();
        createSql = builder.assembleInsertSql();
        updateSql = builder.assembleUpdateSql();
        deleteSql = builder.assembleDeleteSql();
        existsSql = builder.assembleExistsSql();
        genericMapper = new GenericMapper<>();
        genericMapper.setClazz(clazz);
        genericMapper.setFieldAttributeMap(fieldAttributeMap);
        genericMapper.setFieldPrimaryKeyMap(fieldPrimaryKeyMap);
    }

    public Optional<T> find(Number id) {
        try {
            T entity = jdbcTemplate.queryForObject(
                    selectSql,
                    new Object[]{id},
                    genericMapper);
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void save(T entity) {
        if (!isAlreadyExists(entity)) {
            PreparedStatementCreator psc = connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        createSql,
                        Statement.RETURN_GENERATED_KEYS);
                int i = 1;
                for (Object obj : resolveCreateParameters(entity)) {
                    ps.setObject(i++, obj);
                }
                return ps;
            };
            KeyHolder holder = new GeneratedKeyHolder();
            jdbcTemplate.update(psc, holder);
            for (Field field : fieldPrimaryKeyMap.keySet()) {
                try {
                    Number result = (Number) holder.getKeys().get(field.getAnnotation(PrimaryKey.class).value());
                    if (field.getType().equals(Long.class)) {
                        field.set(entity, result.longValue());
                    } else if (field.getType().equals(Integer.class)) {
                        field.set(entity, toIntExact(result.longValue()));
                    }
                } catch (IllegalAccessException e) {
                    logger.warn(e.toString());
                }
            }
        } else update(entity);
    }

    public void delete(T entity) {
        jdbcTemplate.update(deleteSql, resolvePrimaryKeyParameters(entity));
    }

    protected void update(T entity) {
        jdbcTemplate.update(updateSql, resolveUpdateParameters(entity));
    }

    private boolean isAlreadyExists(T entity) {
        Long count = jdbcTemplate.queryForObject(
                existsSql,
                Long.class,
                resolvePrimaryKeyParameters(entity));
        return count > 0L;
    }

    private Object[] resolveCreateParameters(T entity) {
        List<Object> objects = new ArrayList<>();
        addMapperParams(objects, entity, fieldAttributeMap);
        return objects.toArray();
    }

    private Object[] resolveUpdateParameters(T entity) {
        List<Object> objects = new ArrayList<>();
        addMapperParams(objects, entity, fieldAttributeMap);
        addMapperParams(objects, entity, fieldPrimaryKeyMap);
        return objects.toArray();
    }

    private Object[] resolvePrimaryKeyParameters(T entity) {
        List<Object> objects = new ArrayList<>();
        addMapperParams(objects, entity, fieldPrimaryKeyMap);
        return objects.toArray();
    }

    private void addMapperParams(List<Object> objects, T entity, Map<Field, ?> currMapper) {
        for (Field field : currMapper.keySet()) {
            try {
                objects.add(field.get(entity));
            } catch (IllegalAccessException e) {
                logger.warn(e.toString());
                throw new RuntimeException(e);
            }
        }
    }

    private void resolveFields() {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Attribute attribute = field.getAnnotation(Attribute.class);
            if (attribute != null) fieldAttributeMap.put(field, attribute);
            PrimaryKey primaryKey = field.getAnnotation(PrimaryKey.class);
            if (primaryKey != null) fieldPrimaryKeyMap.put(field, primaryKey);
        }
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    protected GenericMapper<T> getGenericMapper() {
        return genericMapper;
    }

    @Autowired
    private void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}