package edu.netcracker.backend.service;

import edu.netcracker.backend.model.history.HistoryTicket;

import java.util.List;

public interface PurchaseHistoryService {
    List<HistoryTicket> getPurchaseHistory(String username);
}
