package edu.netcracker.backend.dao;

import edu.netcracker.backend.message.response.ServiceDTO;
import edu.netcracker.backend.model.ServiceDescr;

import javax.xml.ws.Service;
import java.util.List;
import java.util.Optional;

public interface ServiceDAO {

    void save(ServiceDescr service);

    void delete(Long id);

    void update(ServiceDescr service);

    Optional<ServiceDescr> find(Number id);

    Optional<ServiceDescr> findByName(String name, Number id);

    List<ServiceDTO> findAllByCarrierId(Number id);

    List<ServiceDTO> findPaginByCarrierId(Number id, Integer from, Integer amount);

    List<ServiceDTO> findByStatus(Number id, Integer status, Integer from, Integer number);

}
