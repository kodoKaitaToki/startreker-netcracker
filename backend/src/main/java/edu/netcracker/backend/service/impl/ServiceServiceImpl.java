package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.ServiceDTO;
import edu.netcracker.backend.model.ServiceDescr;
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

@Service
public class ServiceServiceImpl implements ServiceService {

    @Autowired
    private ServiceDAO serviceDAO;

    @Autowired
    private UserService userService;

    private Integer carrierId = 8;

    public ServiceServiceImpl(){
        //setCurCarrier();
    }

    @Override
    public List<ServiceDTO> getServicesOfCarrier(){ return serviceDAO.findAllByCarrierId(carrierId);}

    @Override
    public List<ServiceDTO> getPaginServicesOfCarrier(Integer from, Integer amount){
        return serviceDAO.findPaginByCarrierId(carrierId, from, amount);
    }

    @Override
    public List<ServiceDTO> findByStatus(Integer status){
        return serviceDAO.findByStatus(carrierId, status);
    }

    @Override
    public ServiceDTO addService(ServiceCreateForm serviceCreateForm){
        String serviceName = serviceCreateForm.getServiceName();

        if(ifServiceExists(serviceName, carrierId)){
            throw new RequestException("The service with this name already exists", HttpStatus.CONFLICT);
        }

        ServiceDescr serviceDescr = new ServiceDescr();
        serviceDescr.setCarrierId(carrierId);
        serviceDescr.setServiceName(serviceCreateForm.getServiceName());
        serviceDescr.setServiceDescription(serviceCreateForm.getServiceDescription());
        serviceDescr.setServiceStatus(serviceCreateForm.getServiceStatus());
        serviceDescr.setCreationDate(LocalDateTime.now());

        serviceDAO.save(serviceDescr);

        ServiceDescr result = serviceDAO.findByName(serviceDescr.getServiceName(), carrierId).orElse(null);

        return ServiceDTO.form(result, "");
    }

    @Override
    public ServiceDTO updateService(ServiceDTO serviceDTO){
        ServiceDescr serviceDescr = serviceDAO.find(serviceDTO.getId()).orElse(null);

        if(serviceDescr == null){
            throw new RequestException("Service " + serviceDTO.getId() + " not found ", HttpStatus.NOT_FOUND);
        }

        if(ifServiceExists(serviceDTO.getServiceName(), carrierId)){
            throw new RequestException("The service with this name already exists", HttpStatus.CONFLICT);
        }

        serviceDescr.setServiceName(serviceDTO.getServiceName());
        serviceDescr.setServiceDescription(serviceDTO.getServiceDescription());
        serviceDescr.setServiceStatus(serviceDTO.getServiceStatus());

        serviceDAO.update(serviceDescr);

        return serviceDTO;
    }

    @Override
    public ServiceDTO deleteService(Long serviceId){
        ServiceDescr serviceDescr = serviceDAO.find(serviceId).orElse(null);
        User approver = userService.findByIdWithRole(serviceDescr.getApproverId(), AuthorityUtils.ROLE_APPROVER);
        ServiceDTO serviceDTO = ServiceDTO.form(serviceDescr, approver.getUsername());

        serviceDAO.delete(serviceId);

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