package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.HistoryDTO.HistoryServiceDTO;
import edu.netcracker.backend.message.response.HistoryDTO.HistoryTicketDTO;
import edu.netcracker.backend.model.history.HistoryTicket;

import java.util.List;

public interface PurchaseHistoryService {

    List<HistoryTicketDTO> getPurchaseHistory(Number user_id, Number limit, Number offset);

    List<HistoryServiceDTO> getServiceNamesByTicket(Number id);
}
