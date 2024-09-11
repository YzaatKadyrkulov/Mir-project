package kg.mir.Mirproject.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kg.mir.Mirproject.validation.PhoneNumberValidation;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidation, String> {
    private static final String PHONE_REGEX = "^((\\+996\\d{9})|(\\+7\\d{10}))$";

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null) {
            return false;
        }
        return phoneNumber.matches(PHONE_REGEX);
    }
}
