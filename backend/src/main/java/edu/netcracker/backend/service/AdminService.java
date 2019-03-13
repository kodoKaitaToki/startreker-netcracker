package edu.netcracker.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

public interface AdminService {
    Map<Integer, Integer> getCostsPerPeriodPerCarrier(Number id, LocalDateTime from, LocalDateTime to);
    Map<Integer, Integer> getCostsPerPeriod(LocalDateTime from, LocalDateTime to);

    Map<LocalDateTime, Integer> getUsersIncreasingPerPeriod(LocalDateTime from, LocalDateTime to);
    Map<LocalDateTime, Integer> getCarriersIncreasingPerPeriod(LocalDateTime from, LocalDateTime to);
    Map<LocalDateTime, Integer> getLocationsIncreasingPerPeriod(LocalDateTime from, LocalDateTime to);
}
