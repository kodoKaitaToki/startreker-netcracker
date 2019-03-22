package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.ServiceReplyDAO;
import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.ServiceCRUDDTO;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.model.ServiceReply;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.ServiceService;
import edu.netcracker.backend.utils.ServiceStatus;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j(topic = "log")
public class ServiceServiceImpl implements ServiceService {

    private ServiceDAO serviceDAO;

    private ServiceReplyDAO serviceReplyDAO;

    private final SecurityContext securityContext;

    @Autowired
    public ServiceServiceImpl(ServiceDAO serviceDAO,
                              ServiceReplyDAO serviceReplyDAO,
                              SecurityContext securityContext){
        this.serviceDAO = serviceDAO;
        this.serviceReplyDAO = serviceReplyDAO;
        this.securityContext = securityContext;
    }

    @Override
    public List<ServiceCRUDDTO> getServicesOfCarrier(Integer from, Integer amount, String status){
        log.debug("ServiceService.getServicesOfCarrier(Integer from, Integer amount, String status) was invoked " +
                "with parameters from={}, amount={}, status={}", from, amount, status);
        return serviceDAO.findByCarrierId(setCurUser(), from, amount, getStatusValue(status)).stream()
                .map((ServiceCRUDDTO service) -> {
                    service.setReplyText(serviceReplyDAO.getLastReply(service.getId()).orElse(""));
                    return service; })
                .collect(Collectors.toList());
    }

    @Override
    public ServiceCRUDDTO addService(ServiceCreateForm serviceCreateForm){
        log.debug("ServiceService.addService(ServiceCreateForm serviceCreateForm) was invoked " +
                "to add a new service with name={}, status={}",
                serviceCreateForm.getServiceName(), serviceCreateForm.getServiceStatus());
        String serviceName = serviceCreateForm.getServiceName();
        if(ifServiceExists(serviceName, setCurUser())){

            log.debug("ServiceService.addService(ServiceCreateForm serviceCreateForm)." +
                    "Can't add new service: The carrier with id={} already has service with name={}", setCurUser(), serviceName);

            throw new RequestException("The service with this name already exists", HttpStatus.CONFLICT);
        }

        String status = serviceCreateForm.getServiceStatus();
        if ((!status.equals(ServiceStatus.DRAFT.toString())) && (!status.equals(ServiceStatus.OPEN.toString()))) {

            log.debug("ServiceService.addService(ServiceCreateForm serviceCreateForm)." +
                    "Can't add new service: The new service has illegal status={}", status);

            throw new RequestException("Status of new service must be draft or open", HttpStatus.BAD_REQUEST);
        }

        ServiceDescr serviceDescr = new ServiceDescr();
        serviceDescr.setCarrierId(setCurUser());
        serviceDescr.setServiceName(serviceCreateForm.getServiceName());
        serviceDescr.setServiceDescription(serviceCreateForm.getServiceDescription());
        serviceDescr.setServiceStatus(getStatusValue(status));
        serviceDescr.setCreationDate(LocalDateTime.now());

        serviceDAO.save(serviceDescr);

        ServiceDescr result = serviceDAO.findByName(serviceDescr.getServiceName(), setCurUser()).orElse(null);

        return ServiceCRUDDTO.form(result, "");
    }

    @Override
    public ServiceCRUDDTO updateService(ServiceCRUDDTO serviceCRUDDTO){
        log.debug("ServiceService.updateService(ServiceCRUDDTO serviceCRUDDTO) was invoked " +
                        "to update a service with id={}", serviceCRUDDTO.getId());

        Optional<ServiceDescr> serviceOpt = serviceDAO.find(serviceCRUDDTO.getId());

        if(!serviceOpt.isPresent()){
            log.debug("ServiceService.updateService(ServiceCRUDDTO serviceCRUDDTO)." +
                    "Can't update a service: The carrier with id={} doesn't have a service with id={}",
                    setCurUser(), serviceCRUDDTO.getId());

            throw new RequestException("Service " + serviceCRUDDTO.getId() + " not found ", HttpStatus.NOT_FOUND);
        }

        ServiceDescr serviceDescr = serviceOpt.get();

        if((ifServiceExists(serviceCRUDDTO.getServiceName(), setCurUser()))&&
                (!Objects.equals(serviceCRUDDTO.getServiceName(),serviceDescr.getServiceName()))){

            log.debug("ServiceService.updateService(ServiceCRUDDTO serviceCRUDDTO)." +
                    "Can't update service with id={}: The carrier with id={} already has service with name={}",
                    serviceCRUDDTO.getId(), setCurUser(), serviceCRUDDTO.getServiceName());

            throw new RequestException("The service with this name already exists", HttpStatus.CONFLICT);
        }

        String status = serviceCRUDDTO.getServiceStatus();
        if (((status.equals(ServiceStatus.ASSIGNED.toString())
              || (status.equals(ServiceStatus.PUBLISHED.toString())
                  || (status.equals(ServiceStatus.UNDER_CLARIFICATION.toString())))
                 && (!Objects.equals(getStatusValue(status), serviceDescr.getServiceStatus()))))) {

            log.debug("ServiceService.updateService(ServiceCreateForm serviceCreateForm)." +
                    "Can't update service with id={}: A carrier can't set status={}", status);

            throw new RequestException("Cannot set service_status = " + serviceCRUDDTO.getServiceStatus(),
                    HttpStatus.BAD_REQUEST);
        }

        serviceDescr.setServiceName(serviceCRUDDTO.getServiceName());
        serviceDescr.setServiceDescription(serviceCRUDDTO.getServiceDescription());
        serviceDescr.setServiceStatus(getStatusValue(status));

        serviceDAO.update(serviceDescr);

        return serviceCRUDDTO;
    }

    @Override
    public List<ServiceCRUDDTO> getServicesForApprover(Integer from,
                                                       Integer number,
                                                       String status,
                                                       Integer approverId) {
        if (status.equals(ServiceStatus.OPEN.toString())) {
            return serviceDAO.getServicesForApprover(from, number, getStatusValue(status));
        } else if (status.equals(ServiceStatus.ASSIGNED.toString())) {
            return serviceDAO.getServicesForApprover(from, number, getStatusValue(status), approverId);
        } else {
            throw new IllegalArgumentException("Illegal service status");
        }
    }

    @Override
    public ServiceCRUDDTO reviewService(ServiceCRUDDTO serviceDTO, Integer approverId) {
        ServiceDescr serviceDescr = serviceDAO.find(serviceDTO.getId()).orElse(null);

        if(serviceDescr == null){
            throw new RequestException("Service " + serviceDTO.getId() + " not found ", HttpStatus.NOT_FOUND);
        }

        serviceDescr.setServiceStatus(getStatusValue(serviceDTO.getServiceStatus()));
        serviceDescr.setApproverId(approverId);
        serviceDAO.update(serviceDescr);

        if (serviceDTO.getReplyText() != null && serviceDTO.getReplyText().length() != 0)
        {
            ServiceReply reply = new ServiceReply();
            reply.setServiceId(serviceDescr.getServiceId().intValue());
            reply.setWriterId(approverId.longValue());
            reply.setReportText(serviceDTO.getReplyText());
            reply.setCreationDate(LocalDateTime.now());

            serviceReplyDAO.delete(reply);
            serviceReplyDAO.save(reply);
        }

        return serviceDTO;
    }

    private Integer getStatusValue(String status) {
        try {
            ServiceStatus stat = ServiceStatus.valueOf(status);
            return stat.getValue();
        } catch (IllegalArgumentException e) {

            log.debug("ServiceService.getStatusValue(String status)." +
                    "Illegal format of status: Status={} doesn't exist", status);

            throw new RequestException("The status " + status + " doesn't exist", HttpStatus.NOT_FOUND);
        }
    }

    private boolean ifServiceExists(String name, Number id){
        return serviceDAO.findByName(name, id).isPresent();
    }

    private Integer setCurUser(){
        return securityContext.getUser().getUserId();
    }
}
