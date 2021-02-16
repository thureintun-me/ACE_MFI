package com.mfi.customvalidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements 
ConstraintValidator<Phone, String> {

  @Override
  public void initialize(Phone contactNumber) {
  }

  @Override
  public boolean isValid(String contactField,
    ConstraintValidatorContext cxt) {
      return contactField != null && contactField.matches("[0-9]+")
        && (contactField.length() >= 6) && (contactField.length() <= 14);
  }

}