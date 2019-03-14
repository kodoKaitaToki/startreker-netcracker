package edu.netcracker.backend.model.state.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TripStateRegistry {

    private Map<Integer, TripState> registry;

    @Autowired
    public TripStateRegistry(List<TripState> states) {
        this.registry = createMap(states);
    }

    private Map<Integer, TripState> createMap(List<TripState> states) {
        Map<Integer, TripState> idStateMap = new HashMap<>();
        states.forEach((state) -> idStateMap.put(state.getDatabaseValue(), state));
        return idStateMap;
    }

    public TripState getState(int value) {
        TripState tripState = registry.get(value);
        if (tripState == null) {
            throw new IllegalArgumentException();
        }
        return tripState;
    }
}