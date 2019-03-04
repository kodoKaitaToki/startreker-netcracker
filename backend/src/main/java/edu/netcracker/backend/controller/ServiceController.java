package edu.netcracker.backend.controller;

import edu.netcracker.backend.message.request.MandatoryTimeInterval;
import edu.netcracker.backend.message.request.OptionalTimeInterval;
import edu.netcracker.backend.message.response.CarrierRevenueResponse;
import edu.netcracker.backend.message.response.CarrierViewsResponse;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ServiceController {

    private final StatisticsService statisticsService;
    private final SecurityContext securityContext;

    @Autowired
    public ServiceController(StatisticsService statisticsService, SecurityContext securityContext) {
        this.statisticsService = statisticsService;
        this.securityContext = securityContext;
    }

    @GetMapping("api/v1/service/distribution")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<ServiceDistributionElement> getServiceStatistics() {
        return statisticsService.getServiceStatistics();
    }

    @GetMapping(value = "api/v1/service/sales")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public CarrierRevenueResponse getServicesSalesStatistics(OptionalTimeInterval timeInterval) {
        return timeInterval != null && timeInterval.isProvided()
                ? statisticsService.getServicesSalesStatistics(
                securityContext.getUser().getUserId(),
                timeInterval.getFrom(),
                timeInterval.getTo())

                : statisticsService.getServicesSalesStatistics(securityContext.getUser().getUserId());
    }

    @GetMapping(value = "api/v1/service/sales/per_week")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierRevenueResponse> getServicesSalesStatisticsByWeek(
            @Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getServicesSalesStatisticsByWeek(
                securityContext.getUser().getUserId(),
                timeInterval.getFrom(),
                timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/service/sales/per_month")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierRevenueResponse> getServicesSalesStatisticsByMonth(
            @Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getServicesSalesStatisticsByMonth(
                securityContext.getUser().getUserId(),
                timeInterval.getFrom(),
                timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/service/views/per_week")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierViewsResponse> getServiceViewsStatisticsByWeek(
            @Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getServiceViewsStatisticsByWeek(
                securityContext.getUser().getUserId(),
                timeInterval.getFrom(),
                timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/service/views/per_month")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierViewsResponse> getServiceViewsStatisticsByMonth(
            @Valid MandatoryTimeInterval timeInterval) {
        return statisticsService.getServiceViewsStatisticsByMonth(
                securityContext.getUser().getUserId(),
                timeInterval.getFrom(),
                timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/service/{id}/views/per_week")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByWeek(
            @Valid MandatoryTimeInterval timeInterval, @PathVariable("id") Long serviceId) {
        return statisticsService.getServiceViewsStatisticsByServiceByWeek(
                securityContext.getUser(),
                serviceId,
                timeInterval.getFrom(),
                timeInterval.getTo());
    }

    @GetMapping(value = "api/v1/service/{id}/views/per_month")
    @PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByMonth(
            @Valid MandatoryTimeInterval timeInterval, @PathVariable("id") Long serviceId) {
        return statisticsService.getServiceViewsStatisticsByServiceByMonth(
                securityContext.getUser(),
                serviceId,
                timeInterval.getFrom(),
                timeInterval.getTo());
    }
}
