package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.BundleDAO;
import edu.netcracker.backend.model.Bundle;
import edu.netcracker.backend.service.BundleCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BundleCrudServiceImpl implements BundleCrudService {

    private BundleDAO bundleDAO;

    @Autowired
    public BundleCrudServiceImpl(BundleDAO bundleDAO) {
        this.bundleDAO = bundleDAO;
    }

    @Override
    public List<Bundle> getAll() {
        return bundleDAO.findAll();
    }

    @Override
    public List<Bundle> getAll(Number limit, Number offset) {
        return bundleDAO.findAll(limit, offset);
    }

    @Override
    public Bundle getById(Number id) {
        return bundleDAO.find(id).get();
    }

    @Override
    public void add(Bundle bundle) {
        bundleDAO.save(bundle);
    }

    @Override
    public void update(Bundle bundle) {
        bundleDAO.update(bundle);
    }

    @Override
    public void delete(Number id) {
        bundleDAO.delete(id);
    }

    @Override
    public Long count() {
        return bundleDAO.count();
    }
}
