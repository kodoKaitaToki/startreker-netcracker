package edu.netcracker.backend.model.state.trip;

import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TripStateUtils {

    private static HashMap<Integer, TripState> registry;

    static {
        registry = new HashMap<>();
        registry.put(1, new Draft());
        registry.put(2, new Open());
        registry.put(3, new Assigned());
        registry.put(4, new Published());
        registry.put(5, new Archived());
        registry.put(6, new UnderClarification());
        registry.put(7, new Removed());
    }

    public static TripState getState(int value){
        TripState tripState = registry.get(value);
        if(tripState == null) throw new IllegalArgumentException();
        return tripState;
    }
}
