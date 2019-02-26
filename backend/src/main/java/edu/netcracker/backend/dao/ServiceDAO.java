package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Service;

import java.util.List;
import java.util.Optional;

public interface ServiceDAO {
    void save(Service service);

    Optional<Service> find(Number id);

    void delete(Service service);

    List<Service> findAll();

    List<Service> findAllWithTicketClassId(Number id);

    List<Service> findSuggestionsWithTicketClassId(Number id);

    void saveSuggestion(Number tripId, Number serviceId, Number discount_rate);

    void deleteSuggestion(Number tripId, Number serviceId);

    Optional<Service> findSuggestion(Number tripId, Number serviceId);
}
