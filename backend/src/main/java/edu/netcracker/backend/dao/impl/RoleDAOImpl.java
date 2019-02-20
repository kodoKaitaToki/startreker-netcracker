package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.RoleDAO;
import edu.netcracker.backend.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public class RoleDAOImpl extends CrudDAO<Role> implements RoleDAO {

}

