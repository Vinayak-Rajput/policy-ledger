package com.hdfclife.ledger.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PolicyTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PolicyType {

    String message() default "Invalid policy type. Allowed values are TERM, ULIP, ENDOWMENT";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}