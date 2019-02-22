package edu.netcracker.backend.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDAO<T> {

    Optional<T> find(Number id);
    List<T> findIn(List<?> args);
    void save(T entity);
    void delete(T entity);
}
