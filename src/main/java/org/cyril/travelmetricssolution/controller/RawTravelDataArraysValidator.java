package org.cyril.travelmetricssolution.controller;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RawTravelDataArraysValidator implements ConstraintValidator<RawTravelDataArraysConstraint,
        RawTravelDataDTO.RawTravelDataArrays> {
    @Override
    public void initialize(RawTravelDataArraysConstraint rawTravelDataArraysConstraint) {
    }

    @Override
    public boolean isValid(RawTravelDataDTO.RawTravelDataArrays arrays, ConstraintValidatorContext context) {
        return arrays.isValid();
    }

}
