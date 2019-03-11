package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.ServiceReplyDAO;
import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.ServiceCRUDDTO;
import edu.netcracker.backend.model.ServiceDescr;
import edu.netcracker.backend.model.ServiceReply;
import edu.netcracker.backend.model.User;
import edu.netcracker.backend.service.ServiceService;
import edu.netcracker.backend.service.UserService;
import edu.netcracker.backend.utils.AuthorityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ServiceServiceImpl implements ServiceService {

    private ServiceDAO serviceDAO;

    private ServiceReplyDAO serviceReplyDAO;

    private UserService userService;

    private Integer carrierId = 7;

    @Autowired
    public ServiceServiceImpl(ServiceDAO serviceDAO, ServiceReplyDAO serviceReplyDAO, UserService userService){
        this.serviceDAO = serviceDAO;
        this.serviceReplyDAO = serviceReplyDAO;
        this.userService = userService;
    }

    @Override
    public List<ServiceCRUDDTO> getServicesOfCarrier(){ return serviceDAO.findAllByCarrierId(carrierId);}

    @Override
    public List<ServiceCRUDDTO> getPaginServicesOfCarrier(Integer from, Integer amount){
        return serviceDAO.findPaginByCarrierId(carrierId, from, amount);
    }

    @Override
    public List<ServiceCRUDDTO> findByStatus(Integer status){
        return serviceDAO.findByStatus(carrierId, status);
    }

    @Override
    public ServiceCRUDDTO addService(ServiceCreateForm serviceCreateForm){
        String serviceName = serviceCreateForm.getServiceName();

        if(ifServiceExists(serviceName, carrierId)){
            throw new RequestException("The service with this name already exists", HttpStatus.CONFLICT);
        }

        Integer status = serviceCreateForm.getServiceStatus();
        if((status != 1)&(status != 2)){
            throw new RequestException("Status of new service must be draft or open", HttpStatus.BAD_REQUEST);
        }

        ServiceDescr serviceDescr = new ServiceDescr();
        serviceDescr.setCarrierId(carrierId);
        serviceDescr.setServiceName(serviceCreateForm.getServiceName());
        serviceDescr.setServiceDescription(serviceCreateForm.getServiceDescription());
        serviceDescr.setServiceStatus(serviceCreateForm.getServiceStatus());
        serviceDescr.setCreationDate(LocalDateTime.now());

        serviceDAO.save(serviceDescr);

        ServiceDescr result = serviceDAO.findByName(serviceDescr.getServiceName(), carrierId).orElse(null);

        return ServiceCRUDDTO.form(result, "");
    }

    @Override
    public ServiceCRUDDTO updateService(ServiceCRUDDTO serviceCRUDDTO){
        ServiceDescr serviceDescr = serviceDAO.find(serviceCRUDDTO.getId()).orElse(null);

        if(serviceDescr == null){
            throw new RequestException("Service " + serviceCRUDDTO.getId() + " not found ", HttpStatus.NOT_FOUND);
        }

        if((ifServiceExists(serviceCRUDDTO.getServiceName(), carrierId))&&
                (!Objects.equals(serviceCRUDDTO.getServiceName(),serviceDescr.getServiceName()))){
            throw new RequestException("The service with this name already exists", HttpStatus.CONFLICT);
        }

        Integer status = serviceCRUDDTO.getServiceStatus();
        if(((status == 3) | (status == 4) | (status == 6)) &&
                (!Objects.equals(serviceCRUDDTO.getServiceStatus(),serviceDescr.getServiceStatus()))){
            throw new RequestException("Cannot set service_status = " + serviceCRUDDTO.getServiceStatus(),
                    HttpStatus.BAD_REQUEST);
        }

        serviceDescr.setServiceName(serviceCRUDDTO.getServiceName());
        serviceDescr.setServiceDescription(serviceCRUDDTO.getServiceDescription());
        serviceDescr.setServiceStatus(serviceCRUDDTO.getServiceStatus());

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
    public List<ServiceCRUDDTO> getServicesForApprover(Integer from, Integer number, Integer status, Integer approverId) {
        switch (status) {
            case 2:
                return serviceDAO.getServicesForApprover(from, number, status);
            case 3:
                return serviceDAO.getServicesForApprover(from, number, status, approverId);
            default:
                throw new IllegalArgumentException("Illegal service status");
        }
    }

    @Override
    public ServiceCRUDDTO reviewService(ServiceCRUDDTO serviceDTO, Integer approverId) {
        ServiceDescr serviceDescr = serviceDAO.find(serviceDTO.getId()).orElse(null);

        if(serviceDescr == null){
            throw new RequestException("Service " + serviceDTO.getId() + " not found ", HttpStatus.NOT_FOUND);
        }

        serviceDescr.setServiceStatus(serviceDTO.getServiceStatus());
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

    private boolean ifServiceExists(String name, Number id){
        return serviceDAO.findByName(name, id).isPresent();
    }

    private void setCurCarrier(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String carrierName = authentication.getName();
        User user = userService.findByUsernameWithRole(carrierName, AuthorityUtils.ROLE_CARRIER);
        this.carrierId = user.getUserId();
    }


}
