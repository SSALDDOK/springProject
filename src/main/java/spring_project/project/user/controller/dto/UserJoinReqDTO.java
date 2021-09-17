package spring_project.project.user.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring_project.project.user.controller.validation.ValidationGenderField;
import spring_project.project.user.controller.validation.ValidationNumberField;

import javax.validation.constraints.NotBlank;

import static spring_project.project.common.enums.Gender.GENDER_FEMALE;
import static spring_project.project.common.enums.Gender.GENDER_MALE;
import static spring_project.project.common.enums.ValidateRegex.*;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserJoinReqDTO {

    /**
     * 아이디(이메일)
     * 이름
     * 패스워드
     * 주소
     * 전화번호
     * 성별
     * 생년월일
     */

    private String userEmail; //아이디

    private String userName; //이름

    @NotBlank
    private String address; //주소

    @ValidationNumberField(name = passwordName, length = 20, regex = passwordRegex)
    private String password; //비밀번호

    @ValidationNumberField(name = phoneName, length = 15, regex = phoneRegex)
    private String phoneNumber; //전화번호

    @ValidationGenderField(genderMaleType = GENDER_MALE, genderFemaleType = GENDER_FEMALE)
    private String gender; //성별

    @ValidationNumberField(name = birthName, length = 9, regex = birthRegex)
    private String birth; //생년월일

}
