package edu.netcracker.backend.dao.mapper;

import edu.netcracker.backend.model.Suggestion;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class SuggestionWithDiscountMapper implements RowMapper<Suggestion> {

    private DiscountMapper discountRowMapper;

    public SuggestionWithDiscountMapper(DiscountMapper discountRowMapper) {this.discountRowMapper = discountRowMapper;}

    @Override
    public Suggestion mapRow(ResultSet resultSet, int i) throws SQLException {
        Suggestion suggestion = new Suggestion();

        suggestion.setSuggestionId(resultSet.getLong("suggestion_id"));
        suggestion.setDiscountId(resultSet.getLong("discount_id"));
        suggestion.setClassId(resultSet.getLong("class_id"));

        suggestion.setDiscount(discountRowMapper.mapRow(resultSet, i));

        return suggestion;
    }
}
