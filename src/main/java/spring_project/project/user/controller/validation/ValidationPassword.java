package spring_project.project.user.controller.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;

@Documented
@Target({FIELD, METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidationPassword.PasswordValidator.class)
public @interface ValidationPassword {

    String message() default "Validation(PasswordField) error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String name();

    int min() default 5;

    int max() default 15;

    String regex();


    @Slf4j
    class PasswordValidator implements ConstraintValidator<ValidationPassword, String> {
        private String name;
        private int MIN;
        private int MAX;
        private String regex;


        @Override
        public void initialize(ValidationPassword constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
            name = constraintAnnotation.name();
            MIN = constraintAnnotation.min();
            MAX = constraintAnnotation.max();
            regex = constraintAnnotation.regex();
        }

        public boolean isValid(String value, ConstraintValidatorContext context) {

            return checkRegexValidate(value, context) ;
        }

        private boolean checkRegexValidate(String value, ConstraintValidatorContext context) {
            //공백이거나 null값일 때
            if (StringUtils.isBlank(value)) {
                addMsgMethod(context, String.format("%s값을 입력해 주세요.", name));
                return false;
            }

            if (!value.matches(regex)) {
                addMsgMethod(context, String.format("%s 형식에 맞게 입력해 주세요.(영문, 특수문자 , "+MIN+"이상 "+MAX+"이하)", name));
                return false;
            }
            return true;
        }
        private void addMsgMethod(ConstraintValidatorContext context, String msg) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(msg).addConstraintViolation();
        }
    }
}
