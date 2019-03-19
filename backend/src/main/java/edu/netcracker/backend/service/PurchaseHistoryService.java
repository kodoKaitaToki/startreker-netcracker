package edu.netcracker.backend.service;

import edu.netcracker.backend.message.response.HistoryDTO.HistoryServiceDTO;
import edu.netcracker.backend.message.response.HistoryDTO.HistoryTicketDTO;

import java.util.List;

public interface PurchaseHistoryService {

    List<HistoryTicketDTO> getPurchaseHistory(Number limit, Number offset, String startDate, String endDate);

    List<HistoryServiceDTO> getServiceNamesByTicket(Number id);
}
