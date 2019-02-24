package edu.netcracker.backend.utils;

import lombok.Getter;

@Getter
public enum TripStatus {

    DRAFT(1),
    OPEN(2),
    ASSIGNED(3),
    PUBLISHED(4),
    ARCHIVED(5),
    UNDER_CLARIFICATION(6),
    REMOVED(7);

    private final int value;

    TripStatus(int value){
        this.value = value;
    }
}
