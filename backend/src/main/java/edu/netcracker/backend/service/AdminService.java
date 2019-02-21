package edu.netcracker.backend.service;

import java.time.LocalDate;
import java.util.Map;

public interface AdminService {
    Map<Integer, Integer> getCostsPerPeriodPerCarrier(Number id, LocalDate from, LocalDate to);
    Map<Integer, Integer> getCostsPerPeriod(LocalDate from, LocalDate to);

}
