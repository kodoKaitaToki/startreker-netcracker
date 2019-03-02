package edu.netcracker.backend.utils;

import lombok.Getter;

@Getter
public enum ReportStatus {

    OPEN(1),
    IN_PROGRESS(2),
    ANSWERED(3),
    REOPENED(4),
    RATED(5),
    REMOVED(6);

    private final int databaseValue;

    ReportStatus(int databaseValue){
        this.databaseValue = databaseValue;
    }
}
