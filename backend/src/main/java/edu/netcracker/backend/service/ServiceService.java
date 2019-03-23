package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.ServiceCRUDDTO;
import edu.netcracker.backend.model.ServiceDescr;

import java.util.List;

public interface ServiceService {

    ServiceCRUDDTO addService(ServiceCreateForm serviceCreateForm);

    ServiceCRUDDTO updateService(ServiceCRUDDTO serviceDTO);

    List<ServiceCRUDDTO> getServices(Integer from, Integer amount, String status);

    List<ServiceCRUDDTO> getServicesOfCarrier(Integer from, Integer amount, String status);

    List<ServiceCRUDDTO> getServicesForApprover(Integer from, Integer number, String status, Integer approverId);

    ServiceCRUDDTO reviewService(ServiceCRUDDTO serviceDTO, Integer approverId);
}
