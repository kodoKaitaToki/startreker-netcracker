package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.dao.impl.PossibleServiceDAOImpl;
import edu.netcracker.backend.message.response.HistoryDTO.HistoryServiceDTO;
import edu.netcracker.backend.message.response.HistoryDTO.HistoryTicketDTO;
import edu.netcracker.backend.model.history.HistoryTicket;
import edu.netcracker.backend.service.PurchaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<HistoryTicketDTO> getPurchaseHistory(Number user_id, Number limit, Number offset) {
        return ticketDAO.findAllPurchasedByUser(user_id, limit, offset)
                        .stream()
                        .map(HistoryTicketDTO::from)
                        .collect(Collectors.toList());
    }

    @Override
    public List<HistoryServiceDTO> getServiceNamesByTicket(Number id) {
        return serviceDAO.getServiceNamesByTicket(id)
                         .stream()
                         .map(HistoryServiceDTO::from)
                         .collect(Collectors.toList());
    }
}
