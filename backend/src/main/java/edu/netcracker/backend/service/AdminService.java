package edu.netcracker.backend.service;

import java.time.LocalDate;
import java.util.Map;

public interface AdminService {
    Map<Integer, Integer> getCostsPerPeriodPerCarrier(Number id, LocalDate from, LocalDate to);
    Map<Integer, Integer> getCostsPerPeriod(LocalDate from, LocalDate to);

    Map<LocalDate, Integer> getUsersIncreasingPerPeriod(LocalDate from, LocalDate to);
    Map<LocalDate, Integer> getCarriersIncreasingPerPeriod(LocalDate from, LocalDate to);
    Map<LocalDate, Integer> getLocationsIncreasingPerPeriod(LocalDate from, LocalDate to);
}
