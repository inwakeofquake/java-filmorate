package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateConstraint, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate minReleaseDate = LocalDate.of(1895, 12, 28);
        if (value == null) {
            return true; // null values are handled by @NotNull constraint
        }
        return value.isEqual(minReleaseDate) || value.isAfter(minReleaseDate);
    }
}

