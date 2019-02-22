package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.CrudDAO;
import edu.netcracker.backend.dao.TicketClassDAO;
import edu.netcracker.backend.model.TicketClass;
import org.springframework.stereotype.Repository;

@Repository
public class TicketClassDAOImpl extends CrudDAOImpl<TicketClass> implements TicketClassDAO {
}
