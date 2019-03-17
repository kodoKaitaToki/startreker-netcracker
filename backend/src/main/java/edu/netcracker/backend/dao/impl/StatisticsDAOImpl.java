package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.StatisticsDAO;
import edu.netcracker.backend.dao.mapper.CarrierRevenueMapper;
import edu.netcracker.backend.dao.mapper.CarrierViewsMapper;
import edu.netcracker.backend.message.response.CarrierRevenueResponse;
import edu.netcracker.backend.message.response.CarrierViewsResponse;
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
    private final CarrierRevenueMapper carrierRevenueMapper;
    private final CarrierViewsMapper carrierViewsMapper;
    @Value("${SELECT_TRIP_VIEWS_TOTAL_BY_CARRIER_BY_WEEK}")
    private String SELECT_TRIP_VIEWS_TOTAL_BY_CARRIER_BY_WEEK;

    @Value("${SELECT_ROUTES_DISTRIBUTION}")
    private String SELECT_ROUTES_DISTRIBUTION;

    @Value("${SELECT_SERVICES_DISTRIBUTION}")
    private String SELECT_SERVICES_DISTRIBUTION;

    @Value("${SELECT_TROUBLE_TICKETS_AMOUNT_IN_STATUS}")
    private String SELECT_TROUBLE_TICKETS_AMOUNT_IN_STATUS;

    @Value("${SELECT_TROUBLE_TICKETS_BY_APPROVER}")
    private String SELECT_TROUBLE_TICKETS_BY_APPROVER;

    @Value("${SELECT_TICKETS_TOTAL_SOLD_REVENUE}")
    private String SELECT_TICKETS_TOTAL_SOLD_REVENUE;

    @Value("${SELECT_TICKETS_TOTAL_SOLD_REVENUE_TIME_BOUNDED}")
    private String SELECT_TICKETS_TOTAL_SOLD_REVENUE_TIME_BOUNDED;

    @Value("${SELECT_SERVICES_TOTAL_SOLD_REVENUE}")
    private String SELECT_SERVICES_TOTAL_SOLD_REVENUE;

    @Value("${SELECT_SERVICES_TOTAL_SOLD_REVENUE_TIME_BOUNDED}")
    private String SELECT_SERVICES_TOTAL_SOLD_REVENUE_TIME_BOUNDED;

    @Value("${SELECT_TICKETS_TOTAL_SOLD_REVENUE_BY_WEEK}")
    private String SELECT_TICKETS_TOTAL_SOLD_REVENUE_BY_WEEK;

    @Value("${SELECT_TICKETS_TOTAL_SOLD_REVENUE_BY_MONTH}")
    private String SELECT_TICKETS_TOTAL_SOLD_REVENUE_BY_MONTH;

    @Value("${SELECT_SERVICES_TOTAL_SOLD_REVENUE_BY_WEEK}")
    private String SELECT_SERVICES_TOTAL_SOLD_REVENUE_BY_WEEK;

    @Value("${SELECT_SERVICES_TOTAL_SOLD_REVENUE_BY_MONTH}")
    private String SELECT_SERVICES_TOTAL_SOLD_REVENUE_BY_MONTH;
    @Value("${SELECT_TRIP_VIEWS_TOTAL_BY_CARRIER_BY_MONTH}")
    private String SELECT_TRIP_VIEWS_TOTAL_BY_CARRIER_BY_MONTH;
    @Value("${SELECT_TRIP_VIEWS_TOTAL_BY_TRIP_BY_WEEK}")
    private String SELECT_TRIP_VIEWS_TOTAL_BY_TRIP_BY_WEEK;
    @Value("${SELECT_TRIP_VIEWS_TOTAL_BY_TRIP_BY_MONTH}")
    private String SELECT_TRIP_VIEWS_TOTAL_BY_TRIP_BY_MONTH;
    @Value("${SELECT_SERVICE_VIEWS_TOTAL_BY_CARRIER_BY_WEEK}")
    private String SELECT_SERVICE_VIEWS_TOTAL_BY_CARRIER_BY_WEEK;
    @Value("${SELECT_SERVICE_VIEWS_TOTAL_BY_CARRIER_BY_MONTH}")
    private String SELECT_SERVICE_VIEWS_TOTAL_BY_CARRIER_BY_MONTH;
    @Value("${SELECT_SERVICE_VIEWS_TOTAL_BY_SERVICE_BY_WEEK}")
    private String SELECT_SERVICE_VIEWS_TOTAL_BY_SERVICE_BY_WEEK;
    @Value("${SELECT_SERVICE_VIEWS_TOTAL_BY_SERVICE_BY_MONTH}")
    private String SELECT_SERVICE_VIEWS_TOTAL_BY_SERVICE_BY_MONTH;

    @Autowired
    public StatisticsDAOImpl(JdbcTemplate jdbcTemplate,
                             NamedParameterJdbcTemplate namedJdbcTemplate,
                             CarrierRevenueMapper carrierRevenueMapper,
                             CarrierViewsMapper carrierViewsMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.carrierRevenueMapper = carrierRevenueMapper;
        this.carrierViewsMapper = carrierViewsMapper;
    }

    public List<TripDistributionElement> getTripsStatistics() {
        return jdbcTemplate.query(SELECT_ROUTES_DISTRIBUTION, (rs, rowNum) -> {
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

    public List<CarrierViewsResponse> getTripsViewsStatisticsByWeek(long carrierId, LocalDate from, LocalDate to) {
        return jdbcTemplate.query(SELECT_TRIP_VIEWS_TOTAL_BY_CARRIER_BY_WEEK,
                                  new Object[]{carrierId, from, to},
                                  carrierViewsMapper);
    }

    public List<CarrierViewsResponse> getTripsViewsStatisticsByMonth(long carrierId, LocalDate from, LocalDate to) {
        return jdbcTemplate.query(SELECT_TRIP_VIEWS_TOTAL_BY_CARRIER_BY_MONTH,
                                  new Object[]{carrierId, from, to},
                                  carrierViewsMapper);
    }

    public List<CarrierViewsResponse> getTripsViewsStatisticsByTripByWeek(long tripId, LocalDate from, LocalDate to) {
        return jdbcTemplate.query(SELECT_TRIP_VIEWS_TOTAL_BY_TRIP_BY_WEEK,
                                  new Object[]{tripId, from, to},
                                  carrierViewsMapper);
    }

    public List<CarrierViewsResponse> getTripsViewsStatisticsByTripByMonth(long tripId, LocalDate from, LocalDate to) {
        return jdbcTemplate.query(SELECT_TRIP_VIEWS_TOTAL_BY_TRIP_BY_MONTH,
                                  new Object[]{tripId, from, to},
                                  carrierViewsMapper);
    }

    public List<CarrierViewsResponse> getServiceViewsStatisticsByWeek(long carrierId, LocalDate from, LocalDate to) {
        return jdbcTemplate.query(SELECT_SERVICE_VIEWS_TOTAL_BY_CARRIER_BY_WEEK,
                                  new Object[]{carrierId, from, to},
                                  carrierViewsMapper);
    }

    public List<CarrierViewsResponse> getServiceViewsStatisticsByMonth(long carrierId, LocalDate from, LocalDate to) {
        return jdbcTemplate.query(SELECT_SERVICE_VIEWS_TOTAL_BY_CARRIER_BY_MONTH,
                                  new Object[]{carrierId, from, to},
                                  carrierViewsMapper);
    }

    public List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByWeek(long serviceId,
                                                                               LocalDate from,
                                                                               LocalDate to) {
        return jdbcTemplate.query(SELECT_SERVICE_VIEWS_TOTAL_BY_SERVICE_BY_WEEK,
                                  new Object[]{serviceId, from, to},
                                  carrierViewsMapper);
    }

    public List<CarrierViewsResponse> getServiceViewsStatisticsByServiceByMonth(long serviceId,
                                                                                LocalDate from,
                                                                                LocalDate to) {
        return jdbcTemplate.query(SELECT_SERVICE_VIEWS_TOTAL_BY_SERVICE_BY_MONTH,
                                  new Object[]{serviceId, from, to},
                                  carrierViewsMapper);
    }

    public CarrierRevenueResponse getTripsSalesStatistics(long carrierId, LocalDate from, LocalDate to) {
        return jdbcTemplate.queryForObject(SELECT_TICKETS_TOTAL_SOLD_REVENUE_TIME_BOUNDED,
                                           new Object[]{carrierId, from, to},
                                           carrierRevenueMapper);
    }

    public CarrierRevenueResponse getTripsSalesStatistics(long carrierId) {
        return jdbcTemplate.queryForObject(SELECT_TICKETS_TOTAL_SOLD_REVENUE,
                                           new Object[]{carrierId},
                                           carrierRevenueMapper);
    }

    public List<CarrierRevenueResponse> getTripsSalesStatisticsByWeek(long carrierId, LocalDate from, LocalDate to) {
        return jdbcTemplate.query(SELECT_TICKETS_TOTAL_SOLD_REVENUE_BY_WEEK,
                                  new Object[]{carrierId, from, to},
                                  carrierRevenueMapper);
    }

    public List<CarrierRevenueResponse> getServicesSalesStatisticsByWeek(long carrierId, LocalDate from, LocalDate to) {
        return jdbcTemplate.query(SELECT_SERVICES_TOTAL_SOLD_REVENUE_BY_WEEK,
                                  new Object[]{carrierId, from, to},
                                  carrierRevenueMapper);
    }

    public List<CarrierRevenueResponse> getServicesSalesStatisticsByMonth(long carrierId,
                                                                          LocalDate from,
                                                                          LocalDate to) {
        return jdbcTemplate.query(SELECT_SERVICES_TOTAL_SOLD_REVENUE_BY_MONTH,
                                  new Object[]{carrierId, from, to},
                                  carrierRevenueMapper);
    }

    public List<CarrierRevenueResponse> getTripsSalesStatisticsByMonth(long carrierId, LocalDate from, LocalDate to) {
        return jdbcTemplate.query(SELECT_TICKETS_TOTAL_SOLD_REVENUE_BY_MONTH,
                                  new Object[]{carrierId, from, to},
                                  carrierRevenueMapper);
    }

    public CarrierRevenueResponse getServiceSalesStatistics(long carrierId, LocalDate from, LocalDate to) {
        return jdbcTemplate.queryForObject(SELECT_SERVICES_TOTAL_SOLD_REVENUE_TIME_BOUNDED,
                                           new Object[]{carrierId, from, to},
                                           carrierRevenueMapper);
    }

    public CarrierRevenueResponse getServiceSalesStatistics(long carrierId) {
        return jdbcTemplate.queryForObject(SELECT_SERVICES_TOTAL_SOLD_REVENUE,
                                           new Object[]{carrierId},
                                           carrierRevenueMapper);
    }

    public List<ServiceDistributionElement> getServicesDistribution() {
        return jdbcTemplate.query(SELECT_SERVICES_DISTRIBUTION, (rs, rowNum) -> {
            ServiceDistributionElement rstat = new ServiceDistributionElement();
            rstat.setServiceId(rs.getLong("service_id"));
            rstat.setServiceName(rs.getString("service_name"));
            rstat.setOccurrenceCount(rs.getLong("occurrence_count"));
            rstat.setPercentage(rs.getDouble("percentage"));
            return rstat;
        });
    }

    public Map<String, Double> getTroubleTicketStatistics() {
        SqlRowSet data = namedJdbcTemplate.queryForRowSet(SELECT_TROUBLE_TICKETS_AMOUNT_IN_STATUS,
                                                          getStatisticsParameters());
        return toMap(data);
    }

    public Map<String, Double> getTroubleTicketStatisticsByApprover(Long approverId) {
        Map<String, Object> parameters = getStatisticsParameters();
        parameters.put("approver", approverId);
        SqlRowSet data = namedJdbcTemplate.queryForRowSet(SELECT_TROUBLE_TICKETS_BY_APPROVER, parameters);
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
        int colCount = data.getMetaData()
                           .getColumnCount();
        for (int i = 1; i <= colCount; i++) {
            result.put(data.getMetaData()
                           .getColumnLabel(i)
                           .toLowerCase(), data.getDouble(i));
        }
        return result;
    }
}
