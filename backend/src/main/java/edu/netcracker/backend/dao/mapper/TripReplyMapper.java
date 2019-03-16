package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.TripReply;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class TripReplyMapper implements RowMapper<TripReply> {

    private final MapperHelper mapperHelper;

    public TripReplyMapper(MapperHelper mapperHelper) {this.mapperHelper = mapperHelper;}

    @Override
    public TripReply mapRow(ResultSet rs, int rowNum) throws SQLException {
        TripReply tripReply = new TripReply();
        tripReply.setTripId(rs.getLong("reply_trip_id"));
        tripReply.setReportText(rs.getString("reply_text"));
        tripReply.setCreationDate(rs.getTimestamp("reply_creation_date")
                                    .toLocalDateTime());
        tripReply.setWriterId(rs.getInt("reply_writer_id"));
        tripReply.setWriter(mapperHelper.mapUser(rs, "writer"));
        return tripReply;
    }
}
