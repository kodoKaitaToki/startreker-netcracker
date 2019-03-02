package edu.netcracker.backend.validation.validator;

import edu.netcracker.backend.validation.annotation.DateValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeStringValidator implements ConstraintValidator<DateValidation, String> {

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            System.out.println();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
