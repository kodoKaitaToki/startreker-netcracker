package edu.netcracker.backend.dao.sql;

public enum BundleQueries {
    SELECT_ALL_BUNDLES("SELECT bundle_id, "
                       + "start_date, "
                       + "finish_date, "
                       + "bundle_price, "
                       + "bundle_description, "
                       + "bundle_photo "
                       + "FROM bundle "),

    GET_FRESH_BUNDLES("SELECT bundle_id, start_date, finish_date, bundle_price, bundle_description, bundle_photo "
                      + "FROM bundle "
                      + "WHERE finish_date > current_date "),

    PAGING_SELECT_BUNDLES(SELECT_ALL_BUNDLES + "ORDER BY bundle_id DESC " + "LIMIT ? OFFSET ?"),

    SELECT_BY_ID(SELECT_ALL_BUNDLES + " WHERE bundle_id = ?"),

    DELETE_BUNDLE("DELETE FROM bundle WHERE bundle_id = ?;"),

    COUNT_BUNDLES("SELECT count(*) FROM bundle"),

    SELECT_BUNDLE_TRIP("SELECT DISTINCT t.trip_id, "
                       + "                trip_status, "
                       + "                departure_date, "
                       + "                arrival_date, "
                       + "                trip_photo, "
                       + "                t.creation_date, "
                       + "                bc.item_number, "
                       + "                spd.spaceport_id   departure_spaceport_id, "
                       + "                spd.spaceport_name departure_spaceport_name, "
                       + "                pd.planet_id       departure_planet_id, "
                       + "                pd.planet_name     departure_planet_name, "
                       + "                spa.spaceport_id   arrival_spaceport_id, "
                       + "                spa.spaceport_name arrival_spaceport_name, "
                       + "                pa.planet_id       arrival_planet_id, "
                       + "                pa.planet_name     arrival_planet_name "
                       + "FROM trip t "
                       + "INNER JOIN ticket_class tc ON t.trip_id = tc.trip_id "
                       + "INNER JOIN bundle_class bc on tc.class_id = bc.class_id "
                       + "INNER JOIN bundle b on bc.bundle_id = b.bundle_id "
                       + "INNER JOIN spaceport spd on t.departure_id = spd.spaceport_id "
                       + "INNER JOIN spaceport spa on t.arrival_id = spa.spaceport_id "
                       + "INNER JOIN planet pd on spd.planet_id = pd.planet_id "
                       + "INNER JOIN planet pa on spa.planet_id = pa.planet_id "
                       + "WHERE b.bundle_id = ?;"),

    SELECT_BUNDLE_SERVICES("SELECT s.service_id, service_name, service_description, service_price, bs.item_number "
                           + "FROM service s "
                           + "       INNER JOIN possible_service ps on s.service_id = ps.service_id "
                           + "       INNER JOIN ticket_class tc on ps.class_id = tc.class_id "
                           + "       INNER JOIN bundle_service bs on ps.p_service_id = bs.p_service_id "
                           + "       INNER JOIN bundle b on bs.bundle_id = b.bundle_id "
                           + "WHERE b.bundle_id = ?  AND tc.class_id = ? "
                           + "ORDER BY s.service_id;"),

    INSERT_BUNDLE_CLASS("INSERT INTO bundle_class (bundle_id, class_id, item_number) " + "VALUES (?, ?, ?);"),

    INSERT_BUNDLE_SERVICE("INSERT INTO bundle_service (bundle_id, item_number, p_service_id) "
                          + "VALUES (?, ?, (SELECT p_service_id FROM possible_service ps WHERE class_id = ? "
                          + "                                                           AND service_id = ?));"),

    DELETE_BUNDLE_CLASSES_BY_ID("DELETE FROM bundle_class WHERE bundle_id = ?;"),

    DELETE_BUNDLE_SERVICES_BY_ID("DELETE FROM bundle_service WHERE bundle_id = ?;");

    private final String text;

    BundleQueries(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
