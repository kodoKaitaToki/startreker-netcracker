package edu.netcracker.backend.validation.validator;

import edu.netcracker.backend.validation.annotation.DateValidation;
import edu.netcracker.backend.validation.annotation.FieldMatch;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeStringValidator implements ConstraintValidator<DateValidation, String> {

    private String pattern;

    @Override
    public void initialize(DateValidation constraintAnnotation) {
        pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
            System.out.println();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
