package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Bundle;

import java.util.List;
import java.util.Optional;

public interface BundleDAO {

    List<Bundle> findAll();

    List<Bundle> findAll(Number limit, Number offset);

    Optional<Bundle> find(Number id);

    void save(Bundle bundle);

    void update(Bundle bundle);

    void delete(Number id);

    Long count();
}
