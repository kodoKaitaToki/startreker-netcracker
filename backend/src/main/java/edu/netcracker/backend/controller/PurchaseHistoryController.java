package edu.netcracker.backend.controller;

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
    public List<HistoryTicketDTO> getPurchaseHistory(@RequestParam("username") String username){
        return phs.getPurchaseHistory(username).stream().map(HistoryTicketDTO::from).collect(Collectors.toList());
    }
}
