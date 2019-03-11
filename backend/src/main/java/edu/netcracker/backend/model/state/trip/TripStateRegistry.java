package edu.netcracker.backend.model.state.trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@Component
public class TripStateRegistry {

    private final Draft draft;
    private final Open open;
    private final Assigned assigned;
    private final Published published;
    private final Archived archived;
    private final UnderClarification underClarification;
    private final Removed removed;

    private HashMap<Integer, TripState> registry;

    @Autowired
    public TripStateRegistry(Draft draft,
                             Open open,
                             Assigned assigned,
                             Published published,
                             Archived archived,
                             UnderClarification underClarification,
                             Removed removed) {
        this.draft = draft;
        this.open = open;
        this.assigned = assigned;
        this.published = published;
        this.archived = archived;
        this.underClarification = underClarification;
        this.removed = removed;
    }

    @PostConstruct
    private void init() {
        registry = new HashMap<>();
        registry.put(draft.getDatabaseValue(), draft);
        registry.put(open.getDatabaseValue(), open);
        registry.put(assigned.getDatabaseValue(), assigned);
        registry.put(published.getDatabaseValue(), published);
        registry.put(archived.getDatabaseValue(), archived);
        registry.put(underClarification.getDatabaseValue(), underClarification);
        registry.put(removed.getDatabaseValue(), removed);
    }

    public TripState getState(int value) {
        TripState tripState = registry.get(value);
        if (tripState == null) {
            throw new IllegalArgumentException();
        }
        return tripState;
    }
}
