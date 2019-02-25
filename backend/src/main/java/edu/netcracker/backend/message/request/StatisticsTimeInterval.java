package edu.netcracker.backend.message.request;

import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Setter
public class StatisticsTimeInterval {

    private String from;
    private String to;

    public boolean isProvided(){
        return from != null && to != null;
    }

    public LocalDate getFrom() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(this.from, dateFormatter);
    }

    public LocalDate getTo() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(this.to, dateFormatter);
    }
}
