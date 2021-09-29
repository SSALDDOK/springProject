package spring_project.project.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring_project.project.user.controller.validation.ValidationGenderField;
import spring_project.project.user.controller.validation.ValidationBasicField;

import javax.validation.constraints.NotBlank;

import static spring_project.project.common.enums.Gender.GENDER_FEMALE;
import static spring_project.project.common.enums.Gender.GENDER_MALE;
import static spring_project.project.common.enums.ValidateRegex.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyReqDTO {

    @ValidationBasicField(name = emailName, regex = emailRegex)
    private String userEmail; //아이디

    @ValidationBasicField(name = name, length = 5, regex = nameRegex)
    private String userName; //이름

    @NotBlank
    private String address; //주소

    @ValidationBasicField(name = passwordName, length = 20, regex = passwordRegex)
    private String password; //비밀번호

    @ValidationBasicField(name = phoneName, length = 15, regex = phoneRegex)
    private String phoneNumber; //전화번호

    @ValidationGenderField(genderMaleType = GENDER_MALE, genderFemaleType = GENDER_FEMALE)
    private String gender; //성별

    @ValidationBasicField(name = birthName, length = 9, regex = birthRegex)
    private String birth; //생년월일

}
