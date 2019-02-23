package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.BundleDAO;
import edu.netcracker.backend.model.Bundle;
import edu.netcracker.backend.service.BundleCrudService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BundleCrudServiceImpl implements BundleCrudService {

    private BundleDAO bundleDAO;

    @Override
    public List<Bundle> getAll() {
        return null;
    }

    @Override
    public List<Bundle> getAll(Number limit, Number offset) {
        return null;
    }

    @Override
    public Bundle getById(Number id) {
        return null;
    }

    @Override
    public void add(Bundle bundle) {

    }

    @Override
    public void update(Bundle bundle) {

    }

    @Override
    public void delete(Bundle bundle) {

    }
}
