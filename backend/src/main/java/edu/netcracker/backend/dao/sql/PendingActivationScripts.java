package edu.netcracker.backend.dao.sql;

public class PendingActivationScripts {

    public static final String GET_TRIPS_PENDING_ACTIVATION = "SELECT\n" +
            "  T.TRIP_ID,\n" +
            "  T.TRIP_STATUS,\n" +
            "  T.ARRIVAL_DATE,\n" +
            "  T.DEPARTURE_DATE,\n" +
            "  T.CREATION_DATE,\n" +
            "  APPROVER.USER_NAME                 AS APPROVER_NAME,\n" +
            "  APPROVER.USER_EMAIL                AS APPROVER_EMAIL,\n" +
            "  APPROVER.USER_TELEPHONE            AS APPROVER_TEL,\n" +
            "  CARRIER.USER_NAME                  AS CARRIER_NAME,\n" +
            "  CARRIER.USER_EMAIL                 AS CARRIER_EMAIL,\n" +
            "  CARRIER.USER_TELEPHONE             AS CARRIER__TEL,\n" +
            "  SPACEPORT_DEPARTURE.SPACEPORT_NAME AS SPACEPORT_DEPARTURE,\n" +
            "  SPECEPORT_ARRIVAL.SPACEPORT_NAME   AS SPACEPORT_ARRIVAL,\n" +
            "  PLANET_DEPARTURE.PLANET_NAME       AS PLANET_DEPARTURE,\n" +
            "  PLANET_ARRIVAL.PLANET_NAME         AS PLANET_ARRIVAL\n" +
            "FROM TRIP T\n" +
            "  INNER JOIN USER_A CARRIER ON T.CARRIER_ID = CARRIER.USER_ID\n" +
            "  LEFT JOIN USER_A APPROVER ON T.APPROVER_ID = APPROVER.USER_ID\n" +
            "  INNER JOIN SPACEPORT SPACEPORT_DEPARTURE ON T.DEPARTURE_ID = SPACEPORT_DEPARTURE.SPACEPORT_ID\n" +
            "  INNER JOIN SPACEPORT SPECEPORT_ARRIVAL ON T.ARRIVAL_ID = SPECEPORT_ARRIVAL.SPACEPORT_ID\n" +
            "  INNER JOIN PLANET PLANET_DEPARTURE ON SPACEPORT_DEPARTURE.PLANET_ID = PLANET_DEPARTURE.PLANET_ID\n" +
            "  INNER JOIN PLANET PLANET_ARRIVAL ON SPECEPORT_ARRIVAL.PLANET_ID = PLANET_ARRIVAL.PLANET_ID\n" +
            "WHERE TRIP_STATUS < 4\n" +
            "order by t.trip_status asc";

    public static final String GET_SERVICES_PENDING_ACTIVATION = "select\n" +
            "  srvc.service_id          as srvc_id,\n" +
            "  srvc.service_name        as srvc_name,\n" +
            "  srvc.service_description as srvc_descr,\n" +
            "  srvc.service_status      as srvc_status,\n" +
            "  srvc.creation_date       as srvc_creation_date,\n" +
            "  approver.user_name       as approver_name,\n" +
            "  approver.user_email      as approver_email,\n" +
            "  approver.user_telephone  as approver_tel,\n" +
            "  carrier.user_name        as carrier_name,\n" +
            "  carrier.user_email       as carrier_email,\n" +
            "  carrier.user_telephone   as carrier_tel\n" +
            "from service srvc\n" +
            "  left join user_a approver on srvc.approver_id = approver.user_id\n" +
            "  inner join user_a carrier on srvc.carrier_id = carrier.user_id\n" +
            "where srvc.service_status < 4\n" +
            "order by srvc.service_status asc";

    public static final String LIMIT_AND_OFFSET = " limit ? offset ?";
}
