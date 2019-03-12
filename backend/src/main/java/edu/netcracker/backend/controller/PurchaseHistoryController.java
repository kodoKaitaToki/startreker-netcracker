package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.HistoryDTO.HistoryServiceDTO;
import edu.netcracker.backend.message.response.HistoryDTO.HistoryTicketDTO;
import edu.netcracker.backend.service.PurchaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PurchaseHistoryController {

    private PurchaseHistoryService phs;

    @Autowired
    PurchaseHistoryController(PurchaseHistoryService phs) {
        this.phs = phs;
    }

    @GetMapping("api/v1/ticket/history")
    public List<HistoryTicketDTO> getPurchaseHistory(@RequestParam("user_id") Number user_id,
                                                     @RequestParam("limit") Number limit,
                                                     @RequestParam("offset") Number offset){
        return phs.getPurchaseHistory(user_id, limit, offset)
                  .stream()
                  .map(HistoryTicketDTO::from)
                  .collect(Collectors.toList());
    }

    @GetMapping("api/v1/service/history")
    public List<HistoryServiceDTO> getServiceNamesByTicket(@RequestParam("ticket_id") Number id){
        return phs.getServiceNamesByTicket(id)
                  .stream()
                  .map(HistoryServiceDTO::from)
                  .collect(Collectors.toList());
    }
}
