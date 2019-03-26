package edu.netcracker.backend.dao.sql;

public enum PendingActivationScripts {

    GET_TRIPS_PENDING_ACTIVATION("SELECT\n" +
            "  t.trip_id,\n" +
            "  t.trip_status,\n" +
            "  t.arrival_date,\n" +
            "  t.departure_date,\n" +
            "  t.creation_date,\n" +
            "  approver.user_name                 AS approver_name,\n" +
            "  approver.user_email                AS approver_email,\n" +
            "  approver.user_telephone            AS approver_tel,\n" +
            "  carrier.user_name                  AS carrier_name,\n" +
            "  carrier.user_email                 AS carrier_email,\n" +
            "  carrier.user_telephone             AS carrier_tel,\n" +
            "  spaceport_departure.spaceport_name AS spaceport_departure,\n" +
            "  speceport_arrival.spaceport_name   AS spaceport_arrival,\n" +
            "  planet_departure.planet_name       AS planet_departure,\n" +
            "  planet_arrival.planet_name         AS planet_arrival\n" +
            "FROM trip t\n" +
            "  INNER JOIN user_a carrier ON t.carrier_id = carrier.user_id\n" +
            "  LEFT JOIN user_a approver ON t.approver_id = approver.user_id\n" +
            "  INNER JOIN spaceport spaceport_departure ON t.departure_id = spaceport_departure.spaceport_id\n" +
            "  INNER JOIN spaceport speceport_arrival ON t.arrival_id = speceport_arrival.spaceport_id\n" +
            "  INNER JOIN planet planet_departure ON spaceport_departure.planet_id = planet_departure.planet_id\n" +
            "  INNER JOIN planet planet_arrival ON speceport_arrival.planet_id = planet_arrival.planet_id\n" +
            "WHERE trip_status < 4\n" +
            "ORDER BY t.trip_status ASC"),

    GET_SERVICES_PENDING_ACTIVATION("\n" +
            "SELECT\n" +
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
            "  carrier.user_telephone   AS carrier_tel\n" +
            "FROM service srvc\n" +
            "  LEFT JOIN user_a approver ON srvc.approver_id = approver.user_id\n" +
            "  INNER JOIN user_a carrier ON srvc.carrier_id = carrier.user_id\n" +
            "WHERE srvc.service_status < 4\n" +
            "ORDER BY srvc.service_status ASC"),

    LIMIT_AND_OFFSET(" limit ? offset ?");

    public final String script;

    PendingActivationScripts(String script) {
        this.script = script;
    }
}
