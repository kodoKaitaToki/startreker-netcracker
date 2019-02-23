package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.BundleDAO;
import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.model.Bundle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BundleDAOImpl extends CrudDAO<Bundle> implements BundleDAO {

    @Override
    public List<Bundle> findAll() {
        return null;
    }

    @Override
    public List<Bundle> findAll(Number limit, Number offset) {
        return null;
    }

    @Override
    public void update(Bundle bundle) {
        super.update(bundle);
    }
}
