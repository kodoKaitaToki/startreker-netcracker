package edu.netcracker.backend.controller;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.message.request.MandatoryTimeInterval;
import edu.netcracker.backend.message.request.OptionalTimeInterval;
import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.CarrierRevenueResponse;
import edu.netcracker.backend.message.response.CarrierViewsResponse;
import edu.netcracker.backend.message.response.ServiceCRUDDTO;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.StatisticsService;
import edu.netcracker.backend.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ServiceController {

    private final StatisticsService statisticsService;
    private final SecurityContext securityContext;

    private final ServiceService serviceService;

    @Autowired
    public ServiceController(StatisticsService statisticsService,
                             SecurityContext securityContext,
                             ServiceService serviceService) {
        this.statisticsService = statisticsService;
        this.securityContext = securityContext;
        this.serviceService = serviceService;
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

    @GetMapping("api/v1/carrier/service")
    //@PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<ServiceCRUDDTO> getAllServices(){
        return serviceService.getServicesOfCarrier();
    }

    @GetMapping("api/v1/carrier/service/pagin")
    //@PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<ServiceCRUDDTO> getPaginServices(@RequestParam("from") Integer from,
                                             @RequestParam("number") Integer number){
        return serviceService.getPaginServicesOfCarrier(from, number);
    }

    @GetMapping("api/v1/carrier/service/by-status")
    //@PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public List<ServiceCRUDDTO> getByStatus(@RequestParam("status") Integer status){
        return serviceService.findByStatus(status);
    }

    @DeleteMapping("api/v1/carrier/service/{servId}")
    //@PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public ServiceCRUDDTO deleteService(@PathVariable Long servId){
        return serviceService.deleteService(servId);
    }

    @PutMapping("api/v1/carrier/service")
    //@PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public ServiceCRUDDTO updateService(@Valid @RequestBody ServiceCRUDDTO serviceCRUDDTO){
        return serviceService.updateService(serviceCRUDDTO);
    }

    @PostMapping("api/v1/carrier/service")
    //@PreAuthorize("hasAuthority('ROLE_CARRIER')")
    public ServiceCRUDDTO addService(@Valid @RequestBody ServiceCreateForm serviceCreateForm){
        return serviceService.addService(serviceCreateForm);
    }

    @GetMapping("api/v1/approver/service")
    //@PreAuthorize("hasAuthority('ROLE_APPROVER')")
    public List<ServiceCRUDDTO> getServicesForApprover(@RequestParam("from") int from,
                                             @RequestParam("number") int number,
                                             @RequestParam("status") int status){

        if (status != 2 && status != 3)
            throw new RequestException("Approver may only read open or assigned services", HttpStatus.BAD_REQUEST);

        int approverId = securityContext.getUser().getUserId();
        return serviceService.getServicesForApprover(from, number, status, approverId);
    }

    @PutMapping("api/v1/approver/service")
    //@PreAuthorize("hasAuthority('ROLE_APPROVER')")
    public ServiceCRUDDTO updateServiceReview(@Valid @RequestBody ServiceCRUDDTO serviceDTO){
        boolean reviewOnAssigned = (
                serviceDTO.getServiceStatus() != 5
                        && serviceDTO.getReplyText() != null
                        && serviceDTO.getReplyText().length() > 0);
        if (reviewOnAssigned)
            throw new RequestException("Reviews can only be on under clarification services", HttpStatus.BAD_REQUEST);

        int state = serviceDTO.getServiceStatus();
        boolean illegalState = (state == 1 || state == 2 || state == 6);
        if (illegalState)
            throw new RequestException("Approver may only assign, publish or review services", HttpStatus.BAD_REQUEST);

        int approverId = securityContext.getUser().getUserId();
        return serviceService.reviewService(serviceDTO, approverId);
    }

}
