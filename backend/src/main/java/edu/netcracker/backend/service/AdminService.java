package edu.netcracker.backend.service;

import java.time.LocalDate;
import java.util.Map;

public interface AdminService {
    Map<Integer, Integer> getCostsPerCarrier(Number id);
    Map<Integer, Integer> getCostsPerPeriodPerCarrier(Number id, LocalDate from, LocalDate to);
    Map<Integer, Integer> getCostsPerWeekPerCarrier(Number id);
    Map<Integer, Integer> getCostsPerMonthPerCarrier(Number id);

    Map<LocalDate, Integer> getUsersIncreasingPerPeriod(LocalDate from, LocalDate to);
    Map<LocalDate, Integer> getCarriersIncreasingPerPeriod(LocalDate from, LocalDate to);
    Map<LocalDate, Integer> getLocationsIncreasingPerPeriod(LocalDate from, LocalDate to);
}
