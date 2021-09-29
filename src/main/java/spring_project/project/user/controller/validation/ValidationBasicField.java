package spring_project.project.user.controller.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
@Target({TYPE, METHOD, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidationBasicField.NumberValidator.class)
public @interface ValidationBasicField {

    String message() default "join Validation(NumberField) error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String name() default "";

    int length() default 0;

    String regex() default "";


    @Slf4j
    class NumberValidator implements ConstraintValidator<ValidationBasicField, String> {
        private String name;
        private int length;
        private String regex;

        @Override
        public void initialize(ValidationBasicField constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
            name = constraintAnnotation.name();
            length = constraintAnnotation.length();
            regex = constraintAnnotation.regex();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return checkValidate(value, context);

        }

        private boolean checkValidate(String value, ConstraintValidatorContext context) {

            if (!StringUtils.isNotBlank(value)) {
                addMsgMethod(context, String.format("%s 값을 입력해 주세요.", name));
                return false;
            }

            if (length != 0 && !(value.length() < length)) {
                addMsgMethod(context, String.format("%s은 %d 이하로 입력해주세요.", name, length));
                return false;
            }

            addMsgMethod(context, String.format("%s 형식에 맞게 입력해 주세요.", name));
            return value.matches(regex);

        }

        private void addMsgMethod(ConstraintValidatorContext context, String msg) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
        }
    }
}
