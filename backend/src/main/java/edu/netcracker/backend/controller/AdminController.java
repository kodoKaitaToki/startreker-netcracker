package edu.netcracker.backend.controller;

import com.google.gson.Gson;
import edu.netcracker.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/costs/{carrierId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getCostsPerCarrier(@PathVariable Long carrierId,
                                     @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                     @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        return new Gson().toJson(adminService.getCostsPerPeriodPerCarrier(carrierId,
                                                                          convertToLocalDateTimeViaInstant(from),
                                                                          convertToLocalDateTimeViaInstant(to)));
    }

    @GetMapping("/costs")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getCostsPerPeriodPerCarrier(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                              @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        return new Gson().toJson(adminService.getCostsPerPeriod(convertToLocalDateTimeViaInstant(from),
                                                                convertToLocalDateTimeViaInstant(to)));
    }

    @GetMapping("/increasing/users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getUsersIncreasingPerPeriod(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
                                              @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {

        Map<LocalDateTime, Long> map
                = adminService.getUsersIncreasingPerPeriod(convertToLocalDateTimeViaInstant(from),
                                                           convertToLocalDateTimeViaInstant(to));

        return new Gson().toJson(map);
    }

    @GetMapping("/increasing/carriers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getCarriersIncreasingPerPeriod(
            @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {

        Map<LocalDateTime, Long> map = adminService.getCarriersIncreasingPerPeriod(convertToLocalDateTimeViaInstant(
                from), convertToLocalDateTimeViaInstant(to));

        return new Gson().toJson(map);
    }

    @GetMapping("/increasing/locations")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String getLocationsIncreasingPerPeriod(
            @RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from,
            @RequestParam("to") @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {

        Map<LocalDateTime, Long> map = adminService.getLocationsIncreasingPerPeriod(convertToLocalDateTimeViaInstant(
                from), convertToLocalDateTimeViaInstant(to));

        return new Gson().toJson(map);
    }

    private LocalDateTime convertToLocalDateTimeViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime();
    }

}
