package edu.netcracker.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public interface AdminService {
    Map<Float, Long> getCostsPerPeriodPerCarrier(Number id, LocalDateTime from, LocalDateTime to);
    Map<Float, Long> getCostsPerPeriod(LocalDateTime from, LocalDateTime to);

    Map<LocalDateTime, Integer> getUsersIncreasingPerPeriod(LocalDateTime from, LocalDateTime to);
    Map<LocalDateTime, Integer> getCarriersIncreasingPerPeriod(LocalDateTime from, LocalDateTime to);
    Map<LocalDateTime, Integer> getLocationsIncreasingPerPeriod(LocalDateTime from, LocalDateTime to);
}
