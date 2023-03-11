package ru.yandex.practicum.filmorate.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PositiveNotNullDurationValidator.class)
public @interface PositiveNotNullDuration {
    String message() default "Duration must be positive";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
