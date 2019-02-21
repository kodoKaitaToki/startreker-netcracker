package edu.netcracker.backend.dao;

import edu.netcracker.backend.model.Report;

import java.util.Optional;

public interface ReportDAO {
    void save(Report report);

    Optional<Report> find(Number id);

    void delete(Report report);
}
