package edu.netcracker.backend.service;

import edu.netcracker.backend.message.request.ServiceCreateForm;
import edu.netcracker.backend.message.response.ServiceDTO;
import edu.netcracker.backend.model.ServiceDescr;

import java.util.List;

public interface ServiceService {

    List<ServiceDescr> getServicesOfCarrier();

    List<ServiceDescr> getPaginServicesOfCarrier(Integer from, Integer amount);

    ServiceDTO addService(ServiceCreateForm serviceCreateForm);

    ServiceDescr updateService(ServiceDescr serviceDescr);

    ServiceDescr deleteService(Long serviceId);
}
