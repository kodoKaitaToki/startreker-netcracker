package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.model.Role;
import edu.netcracker.backend.model.Service;
import edu.netcracker.backend.model.User;

import java.util.ArrayList;
import java.util.List;

public class ServiceDAOImpl extends CrudDAO<Service> implements ServiceDAO {

    private final String findAll = "SELECT * FROM service";

    @Override
    public List<Service> findAll(Role role) {
        List<Service> users = new ArrayList<>();

        users.addAll(getJdbcTemplate().query(
                findAll,
                getGenericMapper()));

        return users;
    }

    @Override
    public List<Service> findAllByTicketId(Number id) {
        return null;
    }

    @Override
    public List<Service> findAllByTicketClass(Number id) {
        return null;
    }
}
