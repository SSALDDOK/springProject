package spring_project.project.user.controller.validation;

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
@Target({FIELD,TYPE,METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidationStringField.StringValidator.class)
public @interface ValidationStringField {

    String message() default "join Validation(StringField) error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int length() default 0;

    String regex() default "";

    String name();

    class StringValidator implements ConstraintValidator<ValidationStringField, String> {
        private String regex;
        private int length;
        private String name;

        @Override
        public void initialize(ValidationStringField constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
            regex = constraintAnnotation.regex();
            length = constraintAnnotation.length();
            name = constraintAnnotation.name();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return checkValidate(value, context);
        }

        private boolean checkValidate(String value, ConstraintValidatorContext context) {

            if (matchRegexMethod(context, StringUtils.isNotBlank(value), "%s을 입력해 주세요."))
                return false;

            if (!(length == 0)) {

                if (!(value.length() < length)) {
                    addMsgMethod(context, String.format("%s은 %d 이하로 입력해 주세요", name, length));
                    return false;
                }

                if (matchRegexMethod(context, value.matches(regex), "%s 형식에 맞게 입력해주세요."))
                    return false;
            }

            if (matchRegexMethod(context, value.matches(regex), "%s 형식에 맞게 입력해주세요.")) return false;

            return true;
        }

        private boolean matchRegexMethod(ConstraintValidatorContext context, boolean matches, String msg) {
            if (!(matches)) {
                addMsgMethod(context, String.format(msg, name));
                return true;
            }
            return false;
        }

        public void addMsgMethod(ConstraintValidatorContext context, String msg) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(msg)
                    .addConstraintViolation();
        }
    }
}
