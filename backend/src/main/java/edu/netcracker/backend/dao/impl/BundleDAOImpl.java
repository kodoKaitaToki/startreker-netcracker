package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.BundleDAO;
import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.model.Bundle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BundleDAOImpl extends CrudDAO<Bundle> implements BundleDAO {

    private final String ORDER_BY = "ORDER BY bundle_id ";

    private final String SELECT_ALL_BUNDLES = "SELECT bundle_id, " +
            "start_date, " +
            "finish_date, " +
            "bundle_price, " +
            "bundle_description, " +
            "bundle_photo " +
            "FROM bundle" +
            ORDER_BY;

    private final String PAGING_SELECT_BUNDLES = SELECT_ALL_BUNDLES + "LIMIT ? OFFSET ?;";

    private final String SELECT_BY_ID = SELECT_ALL_BUNDLES + "WHERE bundle_id = ?" + ORDER_BY;

    private final String INSERT_BUNDLE = "INSERT INTO bundle ( " +
            "start_date, " +
            "finish_date, " +
            "bundle_price, " +
            "bundle_description, " +
            "bundle_photo " +
            " ) VALUES ( ?, ?, ?, ?, ?);";

    private final String UPDATE_BUNDLE = "UPDATE bundle " +
            "SET start_date   = ?, " +
            "    finish_date  = ?, " +
            "    bundle_price = ?, " +
            "    bundle_description = ?, " +
            "    bundle_photo = ? " +
            "WHERE bundle_id = ?;";

    private final String DELETE_BUNDLE = "";

    private final String COUNT_BUNDLES = "";

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

    @Override
    public Long count() {
        return null;
    }
}
