package edu.netcracker.backend.controller;

import com.google.gson.Gson;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.model.Service;
import edu.netcracker.backend.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

@RestController
public class AdminController {

    private AdminService adminService;

    @Autowired
    private ServiceDAO serviceDAO;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/api/v1/admin/costs/{carrierId}")
    public String getCostsPerCarrier(@PathVariable Long carrierId,
                                     @RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                     @RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {
        return new Gson().toJson(adminService.getCostsPerPeriodPerCarrier(carrierId,
                convertToLocalDateViaInstant(from),
                convertToLocalDateViaInstant(to)));
    }

    @GetMapping("/api/v1/admin/costs")
    public String getCostsPerPeriodPerCarrier(@RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                              @RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {

        return new Gson().toJson(adminService.getCostsPerPeriod(convertToLocalDateViaInstant(from),
                convertToLocalDateViaInstant(to)));
    }

    @GetMapping("/api/v1/admin/increasing/users")
    public String getUsersIncreasingPerPeriod(@RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                              @RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {

        Map<LocalDate, Integer> map = adminService.getUsersIncreasingPerPeriod(convertToLocalDateViaInstant(from)
                , convertToLocalDateViaInstant(to));

        return new Gson().toJson(map);
    }

    @GetMapping("/api/v1/admin/increasing/carriers")
    public String getCarriersIncreasingPerPeriod(@RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                                 @RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {

        Map<LocalDate, Integer> map = adminService.getCarriersIncreasingPerPeriod(convertToLocalDateViaInstant(from)
                , convertToLocalDateViaInstant(to));

        return new Gson().toJson(map);
    }

    @GetMapping("/api/v1/admin/increasing/locations")
    public String getLocationsIncreasingPerPeriod(@RequestParam("from") @DateTimeFormat(pattern="yyyy-MM-dd") Date from,
                                                  @RequestParam("to") @DateTimeFormat(pattern="yyyy-MM-dd") Date to) {

        Map<LocalDate, Integer> map = adminService.getLocationsIncreasingPerPeriod(convertToLocalDateViaInstant(from)
                , convertToLocalDateViaInstant(to));

        return new Gson().toJson(map);
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

}
