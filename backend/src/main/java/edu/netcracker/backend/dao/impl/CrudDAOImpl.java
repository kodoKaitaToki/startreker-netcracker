package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.mapper.GenericMapper;
import edu.netcracker.backend.dao.sql.EntityProcessor;
import edu.netcracker.backend.dao.sql.EntityProcessorImpl;
import edu.netcracker.backend.dao.sql.PostgresSqlBuilder;
import edu.netcracker.backend.dao.sql.SQLBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Slf4j(topic = "log")
public abstract class CrudDAOImpl<T> implements CrudDAO<T> {

    private JdbcTemplate jdbcTemplate;

    private final SQLBuilder sql;

    private final GenericMapper<T> genericMapper;
    private final EntityProcessor<T> entityProcessor;

    @Autowired
    public CrudDAOImpl() {
        // Hack to get generic type class
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        Class<T> entityClass = (Class<T>) pt.getActualTypeArguments()[0];

        this.entityProcessor = new EntityProcessorImpl<>(entityClass);

        this.sql = new PostgresSqlBuilder(entityClass,
                                          entityProcessor.getPrimaryKey(),
                                          entityProcessor.getFieldAttributeMap());

        genericMapper = new GenericMapper<>(entityClass,
                                            entityProcessor.getPrimaryKeyField(),
                                            entityProcessor.getFieldAttributeMap());
    }

    public Optional<T> find(Number id) {
        try {
            T entity = jdbcTemplate.queryForObject(sql.getSelectSql(), new Object[]{id}, genericMapper);
            return Optional.ofNullable(entity);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<T> findIn(List<?> args) {
        if (args.size() == 0) {
            return new ArrayList<>();
        }
        return jdbcTemplate.query(sql.assembleVariableSelectInSql(args.size()), args.toArray(), genericMapper);
    }

    public void save(T entity) {
        if (isAlreadyExists(entity)) {
            update(entity);
            return;
        }

        PreparedStatementCreator psc = connection -> {
            PreparedStatement ps = connection.prepareStatement(sql.getInsertSql(), Statement.RETURN_GENERATED_KEYS);
            int i = 1; // 1 - first parameter in jdbc
            for (Object obj : entityProcessor.resolveCreateParameters(entity)) {
                ps.setObject(i++, obj);
            }
            return ps;
        };

        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(psc, holder);

        try {
            entityProcessor.putGeneratedPrimaryKey(holder, entity);
        } catch (IllegalAccessException e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void delete(T entity) {
        jdbcTemplate.update(sql.getDeleteSql(), entityProcessor.resolvePrimaryKeyParameters(entity));
    }

    protected void update(T entity) {
        jdbcTemplate.update(sql.getUpdateSql(), entityProcessor.resolveUpdateParameters(entity));
    }

    private boolean isAlreadyExists(T entity) {
        Long count = jdbcTemplate.queryForObject(sql.getExistsSql(),
                                                 Long.class,
                                                 entityProcessor.resolvePrimaryKeyParameters(entity));
        if (count == null) {
            throw new RuntimeException();
        }
        return count > 0L;
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