package spring_project.project.user.controller.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import spring_project.project.common.enums.ValidateEnum;
import spring_project.project.user.controller.dto.UserJoinReqDTO;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;



@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = JoinValidationField.Validator.class)
public @interface JoinValidationField {

    String message() default "join Validation error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Slf4j
    class Validator implements ConstraintValidator<JoinValidationField, UserJoinReqDTO> {

        @Override
        public void initialize(JoinValidationField constraintAnnotation) {
            ConstraintValidator.super.initialize(constraintAnnotation);
        }

        @Override
        public boolean isValid(UserJoinReqDTO dto, ConstraintValidatorContext context) {
            //ID(Email)
            String userEmail = dto.getUserEmail();
            //Name
            String userName = dto.getUserName();
            //주소
            String address = dto.getAddress();
            //비밀번호
            String password = dto.getPassword();
            //핸드폰 번호
            String phoneNo = dto.getPhoneNumber();
            //성별
            String gender = dto.getGender();
            //생년월일
            String birth = dto.getBirth();

            return checkPhoneNum(phoneNo) && checkEmail(userEmail) && checkAddress(address) && checkName(userName) && checkBirth(birth) && checkGender(gender) && checkPassword(password);
        }



        public boolean checkPhoneNum(String phoneNum) {
            return StringUtils.isNotBlank(phoneNum) && phoneNum.matches("^\\d{3}-\\d{3,4}-\\d{4}$");
        }

        public boolean checkEmail(String email) {
            return StringUtils.isNotBlank(email) && email.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$");
        }

        public boolean checkName(String name) {
            return StringUtils.isNotBlank(name);
        }

        public boolean checkPassword(String password) {
            return StringUtils.isNotBlank(password) && password.matches("^.*(?=^.{8,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$");
        }

        public boolean checkGender(String gender) {
            return StringUtils.isNotBlank(gender) && gender.contains("M") || gender.contains("F");
        }

        public boolean checkBirth(String birth) {
            return StringUtils.isNotBlank(birth);
        }

        public boolean checkAddress(String address) {
            return StringUtils.isNotBlank(address);
        }

    }
}
