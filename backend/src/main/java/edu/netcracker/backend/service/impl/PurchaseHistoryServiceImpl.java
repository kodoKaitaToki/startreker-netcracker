package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.model.history.HistoryTicket;
import edu.netcracker.backend.service.PurchaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService {

    private TicketDAO ticketDAO;

    @Autowired
    public PurchaseHistoryServiceImpl(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    @Override
    public List<HistoryTicket> getPurchaseHistory(String username) {
        return ticketDAO.findAllPurchasedByUser(username);
    }
}
