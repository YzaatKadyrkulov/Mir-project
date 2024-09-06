package kg.mir.Mirproject.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kg.mir.Mirproject.validation.validator.EmailValidator;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
public @interface EmailValidation {

    String message() default "Email must end with '@gmail.com'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
