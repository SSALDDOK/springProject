package spring_project.project.user.controller.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Target({FIELD, TYPE,METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidationStringField.StringValidator.class)
public @interface ValidationStringField {

    String messege() default "join Validation(StringField) error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class StringValidator implements ConstraintValidator<ValidationStringField,String>{
        @Override
        public void initialize(ValidationStringField constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return false;
        }
    }
}
