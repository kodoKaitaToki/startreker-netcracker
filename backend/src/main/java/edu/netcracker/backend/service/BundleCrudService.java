package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.BundleDTO;
import edu.netcracker.backend.model.Bundle;

import java.util.List;

public interface BundleCrudService {

    List<Bundle> getAll(Number limit, Number offset);

    List<BundleDTO> getAll();

    Bundle getById(Number id);

    void add(Bundle bundle);

    void update(Bundle bundle);

    void delete(Number id);

    Long count();
}
