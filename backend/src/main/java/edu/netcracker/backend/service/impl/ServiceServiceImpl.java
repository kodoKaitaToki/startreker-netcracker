package edu.netcracker.backend.service.impl;

import edu.netcracker.backend.controller.exception.RequestException;
import edu.netcracker.backend.dao.ServiceDAO;
import edu.netcracker.backend.dao.UserDAO;
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

import java.util.ArrayList;
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
    public List<ServiceDTO> getServicesOfCarrier(){
        List<ServiceDescr> serviceDescr = serviceDAO.findAllByCarrierId(carrierId);
        return returnDTO(serviceDescr);
    }

    @Override
    public List<ServiceDTO> getPaginServicesOfCarrier(Integer from, Integer amount){
        List<ServiceDescr> serviceDescr = serviceDAO.findPaginByCarrierId(carrierId, from, amount);
        return returnDTO(serviceDescr);
    }

    @Override
    public List<ServiceDTO> findByStatus(Integer status){
        List<ServiceDescr> serviceDescr = serviceDAO.findByStatus(carrierId, status);
        return returnDTO(serviceDescr);
    }

    @Override
    public ServiceDTO addService(ServiceCreateForm serviceCreateForm){
        String serviceName = serviceCreateForm.getServiceName();

        if(ifServiceExists(serviceName, carrierId)){
            throw new RequestException("The service with this name already exists", HttpStatus.CONFLICT);
        }

        ServiceDescr serviceDescr = new ServiceDescr();
        serviceDescr.setServiceName(serviceCreateForm.getServiceName());
        serviceDescr.setServiceDescription(serviceCreateForm.getServiceDescription());
        serviceDescr.setServiceStatus(serviceCreateForm.getServiceStatus());
        serviceDescr.setCarrierId(carrierId);

        serviceDAO.save(serviceDescr);

        ServiceDescr result = serviceDAO.findByName(serviceDescr.getServiceName(), carrierId).get();

        return ServiceDTO.form(result);
    }

    @Override
    public ServiceDTO updateService(ServiceDTO serviceDTO){

        if(ifServiceExists(serviceDTO.getServiceName(), carrierId)){
            throw new RequestException("The service with this name already exists", HttpStatus.CONFLICT);
        }

        ServiceDescr serviceDescr = new ServiceDescr();
        serviceDescr.setServiceId(serviceDTO.getId());
        serviceDescr.setServiceName(serviceDTO.getServiceName());
        serviceDescr.setServiceDescription(serviceDTO.getServiceDescription());
        serviceDescr.setServiceStatus(serviceDTO.getServiceStatus());
        serviceDescr.setCarrierId(carrierId);

        serviceDAO.update(serviceDescr);

        return serviceDTO;
    }

    @Override
    public ServiceDescr deleteService(Long serviceId){
        ServiceDescr serviceDescr = serviceDAO.find(serviceId).orElse(null);

        serviceDAO.delete(serviceId);

        return serviceDescr;
    }

    private List<ServiceDTO> returnDTO(List<ServiceDescr> serviceDescr){
        List<ServiceDTO> serviceDTO = new ArrayList<>();
        serviceDescr.stream().forEach(service -> {
            serviceDTO.add(ServiceDTO.form(service));
        });
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