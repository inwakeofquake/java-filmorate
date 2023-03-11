package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

public class PositiveNotNullDurationValidator implements ConstraintValidator<PositiveNotNullDuration, Duration> {
    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext context) {
        if (duration == null) {
            return false;
        }
        return duration.toNanos() >= 0;
    }
}