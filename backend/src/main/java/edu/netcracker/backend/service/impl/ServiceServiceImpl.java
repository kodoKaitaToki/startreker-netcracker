package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.ServiceReplyDAO;
import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.ServiceCRUDDTO;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.model.ServiceReply;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.security.SecurityContext;
import edu.netcracker.backend.service.ServiceService;
import edu.netcracker.backend.service.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import edu.netcracker.backend.utils.ServiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ServiceServiceImpl implements ServiceService {

    private ServiceDAO serviceDAO;

    private ServiceReplyDAO serviceReplyDAO;

    private UserService userService;

    private final SecurityContext securityContext;

    @Autowired
    public ServiceServiceImpl(ServiceDAO serviceDAO,
                              ServiceReplyDAO serviceReplyDAO,
                              UserService userService,
                              SecurityContext securityContext){
        this.serviceDAO = serviceDAO;
        this.serviceReplyDAO = serviceReplyDAO;
        this.userService = userService;
        this.securityContext = securityContext;
    }

    @Override
    public List<ServiceCRUDDTO> getServicesOfCarrier(){ return serviceDAO.findAllByCarrierId(setCurUser());}

    @Override
    public List<ServiceCRUDDTO> getPaginServicesOfCarrier(Integer from, Integer amount){
        return serviceDAO.findPaginByCarrierId(setCurUser(), from, amount);
    }

    @Override
    public List<ServiceCRUDDTO> findByStatus(String status) {
        return serviceDAO.findByStatus(setCurUser(), getStatusValue(status));
    }

    @Override
    public ServiceCRUDDTO addService(ServiceCreateForm serviceCreateForm){
        String serviceName = serviceCreateForm.getServiceName();
        if(ifServiceExists(serviceName, setCurUser())){
            throw new RequestException("The service with this name already exists", HttpStatus.CONFLICT);
        }

        String status = serviceCreateForm.getServiceStatus();
        if ((!status.equals(ServiceStatus.DRAFT.toString())) && (!status.equals(ServiceStatus.OPEN.toString()))) {
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
        Optional<ServiceDescr> serviceOpt = serviceDAO.find(serviceCRUDDTO.getId());

        if(!serviceOpt.isPresent()){
            throw new RequestException("Service " + serviceCRUDDTO.getId() + " not found ", HttpStatus.NOT_FOUND);
        }

        ServiceDescr serviceDescr = serviceOpt.get();

        if((ifServiceExists(serviceCRUDDTO.getServiceName(), setCurUser()))&&
                (!Objects.equals(serviceCRUDDTO.getServiceName(),serviceDescr.getServiceName()))){
            throw new RequestException("The service with this name already exists", HttpStatus.CONFLICT);
        }

        String status = serviceCRUDDTO.getServiceStatus();
        if (((status.equals(ServiceStatus.ASSIGNED.toString())
              || (status.equals(ServiceStatus.PUBLISHED.toString())
                  || (status.equals(ServiceStatus.UNDER_CLARIFICATION.toString())))
                 && (!Objects.equals(serviceCRUDDTO.getServiceStatus(), serviceDescr.getServiceStatus()))))) {
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
    public ServiceCRUDDTO deleteService(Long serviceId){
        ServiceDescr serviceDescr = serviceDAO.find(serviceId).orElse(null);
        User approver = userService.findByIdWithRole(serviceDescr.getApproverId(), AuthorityUtils.ROLE_APPROVER);
        ServiceCRUDDTO serviceCRUDDTO = ServiceCRUDDTO.form(serviceDescr, approver.getUsername());

        serviceDAO.delete(serviceId);

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
            return stat.ordinal() + 1;
        } catch (IllegalArgumentException e) {
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
