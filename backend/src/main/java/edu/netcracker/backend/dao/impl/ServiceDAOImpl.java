package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.model.Service;
import org.springframework.stereotype.Repository;

@Repository
public class ServiceDAOImpl extends CrudDAOImpl<Service> implements ServiceDAO {

}
