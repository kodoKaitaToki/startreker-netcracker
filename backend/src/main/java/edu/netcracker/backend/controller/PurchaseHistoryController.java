package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.response.HistoryDTO.HistoryServiceDTO;
import edu.netcracker.backend.message.response.HistoryDTO.HistoryTicketDTO;
import edu.netcracker.backend.service.PurchaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PurchaseHistoryController {

    private PurchaseHistoryService phs;

    @Autowired
    PurchaseHistoryController(PurchaseHistoryService phs) {
        this.phs = phs;
    }

    @GetMapping("api/v1/history/user/{id}/ticket")
    public List<HistoryTicketDTO> getPurchaseHistory(@PathVariable("id") Number user_id,
                                                     @RequestParam("limit") Number limit,
                                                     @RequestParam("offset") Number offset,
                                                     @RequestParam(name = "start-date", required = false)
                                                                 String startDate,
                                                     @RequestParam(name = "end-date", required = false)
                                                                 String endDate) {
        return phs.getPurchaseHistory(user_id, limit, offset, startDate, endDate);
    }

    @GetMapping("api/v1/history/ticket/{id}/service")
    public List<HistoryServiceDTO> getServiceNamesByTicket(@PathVariable("id") Number id) {
        return phs.getServiceNamesByTicket(id);
    }
}
