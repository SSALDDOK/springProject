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

    String message() default "Validation(NumberField) error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String name() default "";

    String regex() default "";


    @Slf4j
    class NumberValidator implements ConstraintValidator<ValidationBasicField, String> {
        private String name;
        private String regex;

        @Override
        public void initialize(ValidationBasicField constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
            name = constraintAnnotation.name();
            regex = constraintAnnotation.regex();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            return checkRegexValidate(value, context);

        }

        private boolean checkRegexValidate(String value, ConstraintValidatorContext context) {
            //공백이거나 null값 일 때
            if (StringUtils.isBlank(value)) {
                addMsgMethod(context, String.format("%s 값을 입력해 주세요.", name));
                return false;
            }

            //result 변수따로 선언 _ 명시적으로 만들기
            if (!value.matches(regex)) {
                addMsgMethod(context, String.format("%s 형식에 맞게 입력해 주세요.", name));
                return false;
            }
            //지정한 형식에 맞는지 확인
            return true;

        }

        private void addMsgMethod(ConstraintValidatorContext context, String msg) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
        }
    }
}
