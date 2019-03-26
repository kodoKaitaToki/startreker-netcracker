package edu.netcracker.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public interface AdminService {
    Map<Float, Long> getCostsPerPeriodPerCarrier(Number id, LocalDateTime from, LocalDateTime to);
    Map<Float, Long> getCostsPerPeriod(LocalDateTime from, LocalDateTime to);

    Map<LocalDateTime, Long> getUsersIncreasingPerPeriod(LocalDateTime from, LocalDateTime to);
    Map<LocalDateTime, Long> getCarriersIncreasingPerPeriod(LocalDateTime from, LocalDateTime to);
    Map<LocalDateTime, Long> getLocationsIncreasingPerPeriod(LocalDateTime from, LocalDateTime to);
}
