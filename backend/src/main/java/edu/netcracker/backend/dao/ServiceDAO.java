package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.ServiceDescr;

import javax.xml.ws.Service;
import java.util.List;
import java.util.Optional;

public interface ServiceDAO {

    void save(ServiceDescr service);

    void delete(Long id);

    void update(ServiceDescr service);

    Optional<ServiceDescr> find(Number id);

    Optional<Long> findIdByName(String name, Number id);

    List<ServiceDescr> findAllByCarrierId(Number id);

    List<ServiceDescr> findPaginByCarrierId(Number id, Integer from, Integer amount);

}
