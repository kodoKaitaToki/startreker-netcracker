package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.dao.impl.PossibleServiceDAOImpl;
import edu.netcracker.backend.model.history.HistoryTicket;
import edu.netcracker.backend.service.PurchaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService {

    private TicketDAO ticketDAO;
    private ServiceDAO serviceDAO;

    @Autowired
    public PurchaseHistoryServiceImpl(TicketDAO ticketDAO, ServiceDAO serviceDAO) {
        this.ticketDAO = ticketDAO;
        this.serviceDAO = serviceDAO;
    }


    @Override
    public List<HistoryTicket> getPurchaseHistory(String username, Number limit, Number offset) {
        return ticketDAO.findAllPurchasedByUser(username, limit, offset);
    }

    @Override
    public List<String> getServiceNamesByTicket(Number id) {
        return serviceDAO.getServiceNamesByTicket(id);
    }
}
