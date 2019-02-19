package edu.netcracker.backend.controller;

import com.google.gson.Gson;
import edu.netcracker.backend.model.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@RestController
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/api/admin/costs")
    @PreAuthorize("hasRole('ADMIN')")
    public String getCostsPerCarrier(@Valid @RequestParam("carrier_id") Long carrierId) {
        return new Gson().toJson(adminService.getCostsPerCarrier(carrierId));
    }

    @GetMapping("/api/admin/costs/per-week")
    @PreAuthorize("hasRole('ADMIN')")
    public String getCostsPerWeekPerCarrier(@Valid @RequestParam("carrier_id") Long carrierId) {
        return new Gson().toJson(adminService.getCostsPerWeekPerCarrier(carrierId));
    }

    @GetMapping("/api/admin/costs/per-month")
    @PreAuthorize("hasRole('ADMIN')")
    public String getCostsPerMonthPerCarrier(@Valid @RequestParam("carrier_id") Long carrierId) {
        return new Gson().toJson(adminService.getCostsPerMonthPerCarrier(carrierId));
    }

    @GetMapping("/api/admin/costs/per-period")
    @PreAuthorize("hasRole('ADMIN')")
    public String getCostsPerPeriodPerCarrier(@Valid @RequestParam("carrier_id") Long carrierId,
                                              @RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                              @RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {

        return new Gson().toJson(adminService.getCostsPerPeriodPerCarrier(carrierId
                , convertToLocalDateViaInstant(from)
                , convertToLocalDateViaInstant(to)));
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

}
