package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.TicketDAO;
import edu.netcracker.backend.message.response.HistoryDTO.*;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.PurchaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService {

    private TicketDAO ticketDAO;
    private ServiceDAO serviceDAO;
    private final SecurityContext securityContext;

    @Autowired
    public PurchaseHistoryServiceImpl(TicketDAO ticketDAO, ServiceDAO serviceDAO, SecurityContext securityContext) {
        this.ticketDAO = ticketDAO;
        this.serviceDAO = serviceDAO;
        this.securityContext = securityContext;
    }


    @Override
    public List<HistoryTicketDTO> getPurchaseHistory(Number limit, Number offset, String startDate, String endDate) {
        if (limit == null) {
            throw new RequestException("Limit was null", HttpStatus.BAD_REQUEST);
        }
        if (offset == null) {
            throw new RequestException("Offset was null", HttpStatus.BAD_REQUEST);
        }
        Number user_id = securityContext.getUser()
                                        .getUserId();
        LocalDate startLocal = (startDate == null) ? null : LocalDate.parse(startDate);
        LocalDate endLocal = (endDate == null) ? null : LocalDate.parse(endDate);
        return ticketDAO.findAllPurchasedByUser(user_id, limit, offset, startLocal, endLocal)
                        .stream()
                        .map(HistoryTicketDTO::from)
                        .collect(Collectors.toList());
    }

    @Override
    public List<HistoryServiceDTO> getServiceNamesByTicket(Number id) {
        if (id == null) {
            throw new RequestException("Ticket id was null", HttpStatus.BAD_REQUEST);
        }
        return serviceDAO.getServiceNamesByTicket(id)
                         .stream()
                         .map(HistoryServiceDTO::from)
                         .collect(Collectors.toList());
    }
}
