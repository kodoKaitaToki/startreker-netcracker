package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.ServiceDTO;
import edu.netcracker.backend.model.ServiceDescr;

import java.util.List;

public interface ServiceService {

    List<ServiceDTO> getServicesOfCarrier();

    List<ServiceDTO> getPaginServicesOfCarrier(Integer from, Integer amount);

    List<ServiceDTO> findByStatus(Integer status);

    ServiceDTO addService(ServiceCreateForm serviceCreateForm);

    ServiceDTO updateService(ServiceDTO serviceDTO);

    ServiceDTO deleteService(Long serviceId);

    List<ServiceDTO> getServicesForApprover(Integer from, Integer number, Integer status, Integer approverId);

    public ServiceDTO reviewService(ServiceDTO serviceDTO, Integer approverId);
}
