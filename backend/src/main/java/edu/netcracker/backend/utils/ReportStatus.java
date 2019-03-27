package edu.netcracker.backend.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum ReportStatus {

    OPEN(1),
    IN_PROGRESS(2),
    ANSWERED(3),
    REOPENED(4),
    RATED(5),
    REMOVED(6);

    private final int databaseValue;

    ReportStatus(int databaseValue) {
        this.databaseValue = databaseValue;
    }

    private static final Map<Integer, ReportStatus> idEnum = new HashMap<>();

    static {
        for (ReportStatus status : ReportStatus.values()) {
            idEnum.put(status.getDatabaseValue(), status);
        }
    }

    public static ReportStatus getStatus(int number) {
        return idEnum.get(number);
    }
}
