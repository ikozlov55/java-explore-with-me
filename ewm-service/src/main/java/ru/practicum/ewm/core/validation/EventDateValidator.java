package ru.practicum.ewm.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<EventDate, LocalDateTime> {
    public EventDateValidator() {

    }

    @Override
    public void initialize(EventDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.isAfter(LocalDateTime.now().plusHours(2));
    }

}
