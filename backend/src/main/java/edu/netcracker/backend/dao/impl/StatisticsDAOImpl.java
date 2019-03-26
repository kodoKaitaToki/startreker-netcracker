package edu.netcracker.backend.dao.impl;

import edu.netcracker.backend.dao.StatisticsDAO;
import edu.netcracker.backend.dao.mapper.CarrierRevenueMapper;
import edu.netcracker.backend.dao.mapper.CarrierViewsMapper;
import edu.netcracker.backend.message.response.*;
import edu.netcracker.backend.utils.ReportStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j(topic = "log")
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

    @Value("${FIND_COSTS}")
    private String FIND_COSTS_BY_PERIOD;

    @Value("${FIND_COSTS_BY_CARRIER}")
    private String FIND_COSTS_BY_CARRIER;

    @Value("${GET_USERS_INCREASING_PER_PERIOD_BY_ROLE}")
    private String GET_USERS_INCREASING_PER_PERIOD_BY_ROLE;

    @Value("${GET_LOCATIONS_INCREASING_PER_PERIOD}")
    private String GET_LOCATIONS_INCREASING_PER_PERIOD;

    @Value("${GET_USERS_INCREASING_PER_PERIOD}")
    private String GET_USERS_INCREASING_PER_PERIOD;

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

    @Override
    public Map<Float, Long> getCosts(LocalDateTime from, LocalDateTime to) {
        log.debug("Getting all costs by period from {}, to {}", from, to);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(FIND_COSTS_BY_PERIOD, from, to);

        return getCostMap(rows);
    }

    @Override
    public Map<Float, Long> getCostsByCarrier(Number carrierId, LocalDateTime from, LocalDateTime to) {
        log.debug("Getting costs by carrier (id = {}) by period from {}, to {}", carrierId, from, to);
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(FIND_COSTS_BY_CARRIER, carrierId, from, to);

        return getCostMap(rows);
    }

    private Map<Float, Long> getCostMap(List<Map<String, Object>> rows) {
        Map<Float, Long> map = new HashMap<>();

        for (Map<String, Object> row : rows) {
            map.put((Float) row.get("end_price"), (Long) row.get("end_price_count"));
        }

        return map;
    }

    @Override
    public Map<LocalDateTime, Long> getUsersIncreasingByRoleIdPerPeriod(Number id,
                                                                        LocalDateTime from,
                                                                        LocalDateTime to) {
        Map<LocalDateTime, Long> increasing = new HashMap<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_USERS_INCREASING_PER_PERIOD_BY_ROLE,
                                                                   id,
                                                                   from,
                                                                   to);

        for (Map<String, Object> row : rows) {
            increasing.put(((Timestamp) row.get("user_created")).toLocalDateTime(),
                           (Long) row.get("user_created_count"));
        }

        return increasing;
    }

    @Override
    public Map<LocalDateTime, Long> getUsersIncreasingPerPeriod(LocalDateTime from, LocalDateTime to) {
        Map<LocalDateTime, Long> increasing = new HashMap<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_USERS_INCREASING_PER_PERIOD, from, to);

        for (Map<String, Object> row : rows) {
            increasing.put(((Timestamp) row.get("user_created")).toLocalDateTime(),
                           (Long) row.get("user_created_count"));
        }

        return increasing;
    }

    @Override
    public Map<LocalDateTime, Long> getLocationsIncreasingPerPeriod(LocalDateTime from, LocalDateTime to) {
        Map<LocalDateTime, Long> increasing = new HashMap<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(GET_LOCATIONS_INCREASING_PER_PERIOD, from, to);

        for (Map<String, Object> row : rows) {
            increasing.put(((Timestamp) row.get("creation_date")).toLocalDateTime(),
                           (Long) row.get("creation_date_count"));
        }

        return increasing;
    }


    public List<TripDistributionElement> getTripsStatistics() {
        return jdbcTemplate.query(SELECT_ROUTES_DISTRIBUTION, (rs, rowNum) -> {
            TripDistributionElement rstat = new TripDistributionElement();
            rstat.setArrivalPlanetId(rs.getLong("arrival_planet_id"));
            rstat.setDeparturePlanetId(rs.getLong("departure_planet_id"));
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

    public ReportStatisticsResponse getTroubleTicketStatistics() {
        SqlRowSet data = namedJdbcTemplate.queryForRowSet(SELECT_TROUBLE_TICKETS_AMOUNT_IN_STATUS,
                                                          getStatisticsParameters());
        return toResponse(data);
    }

    public ReportStatisticsResponse getTroubleTicketStatisticsByApprover(Long approverId) {
        Map<String, Object> parameters = getStatisticsParameters();
        parameters.put("approver", approverId);
        SqlRowSet data = namedJdbcTemplate.queryForRowSet(SELECT_TROUBLE_TICKETS_BY_APPROVER, parameters);
        return toResponse(data);
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

    private ReportStatisticsResponse toResponse(SqlRowSet data) {

        ReportStatisticsResponse response = new ReportStatisticsResponse();

        while (data.next()) {
            String entry = data.getString("status");
            double count = data.getDouble("count");

            switch (entry) {
                case "total": {
                    response.setTotal(count);
                    break;
                }
                case "average": {
                    response.setAverageMark(count);
                    break;
                }
                case "resolved": {
                    response.setTotalResolved(count);
                    break;
                }
                default: {
                    if (Integer.toString(ReportStatus.OPEN.getDatabaseValue())
                               .equals(entry)) {
                        response.setTotalOpen(count);
                    } else if (Integer.toString(ReportStatus.IN_PROGRESS.getDatabaseValue())
                                      .equals(entry)) {
                        response.setTotalInProgress(count);
                    } else if (Integer.toString(ReportStatus.ANSWERED.getDatabaseValue())
                                      .equals(entry)) {
                        response.setTotalAnswered(count);
                    } else if (Integer.toString(ReportStatus.REOPENED.getDatabaseValue())
                                      .equals(entry)) {
                        response.setTotalReOpened(count);
                    } else if (Integer.toString(ReportStatus.RATED.getDatabaseValue())
                                      .equals(entry)) {
                        response.setTotalRated(count);
                    } else if (Integer.toString(ReportStatus.REMOVED.getDatabaseValue())
                                      .equals(entry)) {
                        response.setTotalRemoved(count);
                    }
                }
            }
        }

        return response;
    }
}
