package edu.netcracker.backend.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum ServiceStatus {

    DRAFT(1),
    OPEN(2),
    ASSIGNED(3),
    PUBLISHED(4),
    ARCHIVED(5),
    UNDER_CLARIFICATION(6),
    REMOVED(7);

    private final int value;

    ServiceStatus(int value){
        this.value = value;
    }
    public int getStatusNumber() {
        return this.value;
    }

    private static final Map<Integer, ServiceStatus> lookup = new HashMap<>();
    static
    {
        for(ServiceStatus status : ServiceStatus.values())
        {
            lookup.put(status.getStatusNumber(), status);
        }
    }

    public static ServiceStatus get(int number)
    {
        return lookup.get(number);
    }
}
