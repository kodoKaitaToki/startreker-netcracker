package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.ServiceCRUDDTO;

import java.util.List;

public interface ServiceService {

    List<ServiceCRUDDTO> getServicesOfCarrier();

    List<ServiceCRUDDTO> getPaginServicesOfCarrier(Integer from, Integer amount);

    List<ServiceCRUDDTO> findByStatus(String status);

    ServiceCRUDDTO addService(ServiceCreateForm serviceCreateForm);

    ServiceCRUDDTO updateService(ServiceCRUDDTO serviceDTO);

    ServiceCRUDDTO deleteService(Long serviceId);

    List<ServiceCRUDDTO> getServicesForApprover(Integer from, Integer number, String status, Integer approverId);

    public ServiceCRUDDTO reviewService(ServiceCRUDDTO serviceDTO, Integer approverId);
}
