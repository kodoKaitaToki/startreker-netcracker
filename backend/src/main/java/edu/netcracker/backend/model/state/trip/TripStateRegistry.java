package edu.netcracker.backend.model.state.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TripStateRegistry {

    private Map<Integer, TripState> idStateRegistry;
    private Map<String, TripState> nameStateRegistry;

    @Autowired
    public TripStateRegistry(List<TripState> states) {
        this.idStateRegistry = createIdStateMap(states);
        this.nameStateRegistry = createNameStateMap(states);
    }

    private Map<Integer, TripState> createIdStateMap(List<TripState> states) {
        Map<Integer, TripState> idStateMap = new HashMap<>();
        states.forEach((state) -> idStateMap.put(state.getDatabaseValue(), state));
        return idStateMap;
    }

    private Map<String, TripState> createNameStateMap(List<TripState> states) {
        Map<String, TripState> nameStateMap = new HashMap<>();
        states.forEach((state) -> nameStateMap.put(state.getName(), state));
        return nameStateMap;
    }

    public TripState getState(int value) {
        TripState tripState = idStateRegistry.get(value);
        if (tripState == null) {
            throw new IllegalArgumentException();
        }
        return tripState;
    }

    public TripState getState(String value) {
        TripState tripState = nameStateRegistry.get(value.toUpperCase());
        if (tripState == null) {
            throw new IllegalArgumentException();
        }
        return tripState;
    }
}