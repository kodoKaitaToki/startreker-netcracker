package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.StatisticsDAO;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.message.response.TripDistributionElement;
import edu.netcracker.backend.util.ReportStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StatisticsDAOImpl implements StatisticsDAO {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public StatisticsDAOImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    private final String selectRoutesDistribution = "" +
            "SELECT departure_id, arrival_id, departure_planet_id, arrival_planet_id, departure_spaceport_name," +
            " arrival_spaceport_name, departure_planet_name, arrival_planet_name," +
            " count(departure_id) AS occurrence_count, (count(departure_id) / CAST((SELECT COUNT(trip.trip_id) FROM trip) as float) * 100) as percentage\n" +
            "FROM (\n" +
            "\tSELECT\n" +
            "\tdeparture_id,\n" +
            "\tarrival_id,\n" +
            "\t(SELECT spaceport_name FROM spaceport WHERE spaceport_id = departure_id) AS departure_spaceport_name,\n" +
            "\t(SELECT spaceport_name FROM spaceport WHERE spaceport_id = arrival_id) AS arrival_spaceport_name,\n" +
            "\t(SELECT planet_id FROM spaceport WHERE spaceport_id = departure_id) AS departure_planet_id,\n" +
            "\t(SELECT planet_id FROM spaceport WHERE spaceport_id = arrival_id) AS arrival_planet_id,\n" +
            "\t(SELECT planet_name FROM planet WHERE planet_id = (SELECT planet_id FROM spaceport WHERE spaceport_id = departure_id)) AS departure_planet_name,\n" +
            "\t(SELECT planet_name FROM planet WHERE planet_id = (SELECT planet_id FROM spaceport WHERE spaceport_id = arrival_id))  arrival_planet_name\n" +
            "\tFROM trip\n" +
            ") fromclause \n" +
            "GROUP BY departure_id, arrival_id, departure_spaceport_name, arrival_spaceport_name, departure_planet_id, arrival_planet_id, departure_planet_name, arrival_planet_name\n" +
            "ORDER BY occurrence_count DESC";

    private final String selectServicesDistribution = "" +
            "SELECT service.service_id, service.service_name," +
            " COUNT(p_service_id) occurrence_count," +
            " (COUNT(p_service_id) / CAST((SELECT COUNT(p_service_id) FROM possible_service) as float) * 100) as percentage\n" +
            "FROM possible_service \n" +
            "INNER JOIN service ON possible_service.service_id = service.service_id \n" +
            "GROUP BY service.service_id \n" +
            "ORDER BY occurrence_count DESC";

    private final String selectAllTroubleTicketsCounts = "" +
            "SELECT " +
            "(SELECT COUNT(report_id) FROM report WHERE status = :opened) total_opened," +
            "(SELECT COUNT(report_id) FROM report WHERE status = :in_progress) total_in_progress," +
            "(SELECT COUNT(report_id) FROM report WHERE status = :answered) total_answered," +
            "(SELECT COUNT(report_id) FROM report WHERE status = :rated) total_rated," +
            "(SELECT COUNT(report_id) FROM report WHERE status = :reopened) total_reopened," +
            "(SELECT COUNT(report_id) FROM report) total," +
            "(SELECT AVG(report_rate) FROM report) avg_rate," +
            "(SELECT COUNT(report_id) FROM report WHERE status IN (:answered, :rated)) total_resolved";

    private final String selectAllTroubleTicketsByApprover = "" +
            "SELECT " +
            "(SELECT COUNT(report_id) FROM report WHERE status = :opened AND approver_id = :approver) total_opened," +
            "(SELECT COUNT(report_id) FROM report WHERE status = :in_progress AND approver_id = :approver) total_in_progress," +
            "(SELECT COUNT(report_id) FROM report WHERE status = :answered AND approver_id = :approver) total_answered," +
            "(SELECT COUNT(report_id) FROM report WHERE status = :rated AND approver_id = :approver) total_rated," +
            "(SELECT COUNT(report_id) FROM report WHERE status = :reopened AND approver_id = :approver) total_reopened," +
            "(SELECT COUNT(report_id) FROM report WHERE approver_id = :approver) total," +
            "(SELECT AVG(report_rate) FROM report WHERE approver_id = :approver) avg_rate," +
            "(SELECT COUNT(report_id) FROM report WHERE status IN (:answered, :rated) AND approver_id = :approver) total_resolved";

    @Override
    public List<TripDistributionElement> getTripsStatistics() {
        return jdbcTemplate.query(selectRoutesDistribution, (rs, rowNum) -> {
            TripDistributionElement rstat = new TripDistributionElement();
            rstat.setArrival_id(rs.getLong("arrival_id"));
            rstat.setDeparture_id(rs.getLong("departure_id"));
            rstat.setArrival_planet_id(rs.getLong("arrival_planet_id"));
            rstat.setDeparture_planet_id(rs.getLong("departure_planet_id"));
            rstat.setArrival_spaceport_name(rs.getString("arrival_spaceport_name"));
            rstat.setDeparture_spaceport_name(rs.getString("departure_spaceport_name"));
            rstat.setArrival_planet_name(rs.getString("arrival_planet_name"));
            rstat.setDeparture_planet_name(rs.getString("departure_planet_name"));
            rstat.setOccurrence_count(rs.getLong("occurrence_count"));
            rstat.setPercentage(rs.getDouble("percentage"));
            return rstat;
        });
    }

    @Override
    public List<ServiceDistributionElement> getServicesDistribution() {
        return jdbcTemplate.query(selectServicesDistribution, (rs, rowNum) -> {
            ServiceDistributionElement rstat = new ServiceDistributionElement();
            rstat.setService_id(rs.getLong("service_id"));
            rstat.setService_name(rs.getString("service_name"));
            rstat.setOccurrence_count(rs.getLong("occurrence_count"));
            rstat.setPercentage(rs.getDouble("percentage"));
            return rstat;
        });
    }

    @Override
    public Map<String, Double> getTroubleTicketStatistics() {
        SqlRowSet data = namedJdbcTemplate.queryForRowSet(selectAllTroubleTicketsCounts,
                getStatisticsParameters());
        return toMap(data);
    }

    @Override
    public Map<String, Double> getTroubleTicketStatisticsByApprover(Long approverId) {
        Map<String, Object> parameters = getStatisticsParameters();
        parameters.put("approver", approverId);
        SqlRowSet data = namedJdbcTemplate.queryForRowSet(selectAllTroubleTicketsByApprover, parameters);
        return toMap(data);
    }

    private Map<String, Object> getStatisticsParameters() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("opened", ReportStatus.OPEN.getDatabaseValue());
        parameters.put("in_progress", ReportStatus.IN_PROGRESS.getDatabaseValue());
        parameters.put("answered", ReportStatus.ANSWERED.getDatabaseValue());
        parameters.put("rated", ReportStatus.RATED.getDatabaseValue());
        parameters.put("reopened", ReportStatus.REOPENED.getDatabaseValue());
        return parameters;
    }

    private Map<String, Double> toMap(SqlRowSet data) {
        data.next();
        Map<String, Double> result = new HashMap<>();
        int colCount = data.getMetaData().getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            result.put(data.getMetaData().getColumnLabel(i).toLowerCase(), data.getDouble(i));
        }
        return result;
    }
}
