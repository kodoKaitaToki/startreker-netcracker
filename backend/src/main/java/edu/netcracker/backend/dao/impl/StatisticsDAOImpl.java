package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.StatisticsDAO;
import edu.netcracker.backend.dao.mapper.StatMapper;
import edu.netcracker.backend.message.response.CarrierStatisticsResponse;
import edu.netcracker.backend.message.response.ServiceDistributionElement;
import edu.netcracker.backend.message.response.TripDistributionElement;
import edu.netcracker.backend.utils.ReportStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@PropertySource("classpath:sql/statisticsdao.properties")
public class StatisticsDAOImpl implements StatisticsDAO {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    public StatisticsDAOImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
    }

    @Value("${selectRoutesDistribution.sql}")
    private String selectRoutesDistribution;

    @Value("${selectServicesDistribution.sql}")
    private String selectServicesDistribution;

    @Value("${selectAllTroubleTicketsCounts.sql}")
    private String selectAllTroubleTicketsCounts;

    @Value("${selectAllTroubleTicketsByApprover.sql}")
    private String selectAllTroubleTicketsByApprover;

    @Value("${selectTicketsSoldAndRevenue.sql}")
    private String selectTicketsSoldAndRevenue;

    @Value("${selectTicketsSoldAndRevenueWithTimeLimits.sql}")
    private String selectTicketsSoldAndRevenueWithTimeLimits;

    @Value("${selectServicesTotalSoldAndRevenue.sql}")
    private String selectServicesTotalSoldAndRevenue;

    @Value("${selectServicesSoldAndRevenueWithTimeLimits.sql}")
    private String selectServicesSoldAndRevenueWithTimeLimits;

    public List<TripDistributionElement> getTripsStatistics(){
        return jdbcTemplate.query(selectRoutesDistribution, (rs, rowNum) -> {
            TripDistributionElement rstat = new TripDistributionElement();
            rstat.setArrivalId(rs.getLong("arrival_id"));
            rstat.setDepartureId(rs.getLong("departure_id"));
            rstat.setArrivalPlanetId(rs.getLong("arrival_planet_id"));
            rstat.setDeparturePlanetId(rs.getLong("departure_planet_id"));
            rstat.setArrivalSpaceportName(rs.getString("arrival_spaceport_name"));
            rstat.setDepartureSpaceportName(rs.getString("departure_spaceport_name"));
            rstat.setArrivalPlanetName(rs.getString("arrival_planet_name"));
            rstat.setDeparturePlanetName(rs.getString("departure_planet_name"));
            rstat.setOccurrenceCount(rs.getLong("occurrence_count"));
            rstat.setPercentage(rs.getDouble("percentage"));
            return rstat;
        });
    }

    public CarrierStatisticsResponse getTripsSalesStatistics(long carrierId, LocalDate from, LocalDate to){
        return jdbcTemplate.queryForObject(
                selectTicketsSoldAndRevenueWithTimeLimits,
                new Object[]{carrierId, from, to},
                new StatMapper());
    }

    public CarrierStatisticsResponse getTripsSalesStatistics(long carrierId){
        return jdbcTemplate.queryForObject(
                selectTicketsSoldAndRevenue,
                new Object[]{carrierId},
                new StatMapper());
    }

    public CarrierStatisticsResponse getServiceSalesStatistics(long carrierId, LocalDate from, LocalDate to){
        return jdbcTemplate.queryForObject(
                selectServicesSoldAndRevenueWithTimeLimits,
                new Object[]{carrierId, from, to},
                new StatMapper());
    }

    public CarrierStatisticsResponse getServiceSalesStatistics(long carrierId){
        return jdbcTemplate.queryForObject(
                selectServicesTotalSoldAndRevenue,
                new Object[]{carrierId},
                new StatMapper());
    }

    public List<ServiceDistributionElement> getServicesDistribution(){
        return jdbcTemplate.query(selectServicesDistribution, (rs, rowNum) -> {
            ServiceDistributionElement rstat = new ServiceDistributionElement();
            rstat.setServiceId(rs.getLong("service_id"));
            rstat.setServiceName(rs.getString("service_name"));
            rstat.setOccurrenceCount(rs.getLong("occurrence_count"));
            rstat.setPercentage(rs.getDouble("percentage"));
            return rstat;
        });
    }

    public Map<String, Double> getTroubleTicketStatistics(){
        SqlRowSet data = namedJdbcTemplate.queryForRowSet(selectAllTroubleTicketsCounts,
                getStatisticsParameters());
        return toMap(data);
    }

    public Map<String, Double> getTroubleTicketStatisticsByApprover(Long approverId){
        Map<String, Object> parameters = getStatisticsParameters();
        parameters.put("approver", approverId);
        SqlRowSet data = namedJdbcTemplate.queryForRowSet(selectAllTroubleTicketsByApprover, parameters);
        return toMap(data);
    }

    private Map<String, Object> getStatisticsParameters(){
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("opened", ReportStatus.OPEN.getDatabaseValue());
        parameters.put("in_progress", ReportStatus.IN_PROGRESS.getDatabaseValue());
        parameters.put("answered", ReportStatus.ANSWERED.getDatabaseValue());
        parameters.put("rated", ReportStatus.RATED.getDatabaseValue());
        parameters.put("reopened", ReportStatus.REOPENED.getDatabaseValue());
        return parameters;
    }

    private Map<String, Double> toMap(SqlRowSet data){
        data.next();
        Map<String, Double> result = new HashMap<>();
        int colCount = data.getMetaData().getColumnCount();
        for (int i = 1; i <= colCount; i++){
            result.put(data.getMetaData().getColumnLabel(i).toLowerCase(), data.getDouble(i));
        }
        return result;
    }
}
