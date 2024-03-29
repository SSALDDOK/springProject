package spring_project.project.user.controller.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import spring_project.project.common.enums.Gender;

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
@Target({FIELD, TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidationGenderField.GenderValidator.class)
public @interface ValidationGenderField {

    String message() default "join Validation(GenderField) error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    //"F"로 들어올 때
    Gender genderFemaleType();

    //"M"로 들어올 때
    Gender genderMaleType();

    @Slf4j
    class GenderValidator implements ConstraintValidator<ValidationGenderField, String> {
        //"F"나 "M"을 받을 변수 선언
        private String genderFemaleType;
        private String genderMaleType;

        @Override
        public void initialize(ValidationGenderField constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
            //초기값 지정
            genderFemaleType = constraintAnnotation.genderFemaleType().getGenderType();
            genderMaleType = constraintAnnotation.genderMaleType().getGenderType();
        }

        @Override
        public boolean isValid(String value, ConstraintValidatorContext context) {
            //만약 해당 값이 null이거나 공백이지 않고, enum객체에 있는 값을 포함 할 때 통과
            if (StringUtils.isNotBlank(value) && (value.equals(genderFemaleType) || value.equals(genderMaleType)))
                return true;

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("성별[F or M]를 기입해 주세요.")
                    .addConstraintViolation();

            return false;
        }
    }
}
