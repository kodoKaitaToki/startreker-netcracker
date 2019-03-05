package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.ServiceCRUDDTO;

import java.util.List;

public interface ServiceService {

    List<ServiceCRUDDTO> getServicesOfCarrier();

    List<ServiceCRUDDTO> getPaginServicesOfCarrier(Integer from, Integer amount);

    List<ServiceCRUDDTO> findByStatus(Integer status);

    ServiceCRUDDTO addService(ServiceCreateForm serviceCreateForm);

    ServiceCRUDDTO updateService(ServiceCRUDDTO serviceCRUDDTO);

    ServiceCRUDDTO deleteService(Long serviceId);
}
