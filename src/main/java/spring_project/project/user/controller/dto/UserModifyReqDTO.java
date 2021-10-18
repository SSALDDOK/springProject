package spring_project.project.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring_project.project.user.controller.validation.ValidationGenderField;
import spring_project.project.user.controller.validation.ValidationBasicField;
import spring_project.project.user.controller.validation.ValidationPassword;

import javax.validation.constraints.NotNull;

import static spring_project.project.common.enums.Gender.GENDER_FEMALE;
import static spring_project.project.common.enums.Gender.GENDER_MALE;
import static spring_project.project.common.enums.ValidateRegex.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModifyReqDTO {

    /**
     * id 회원번호
     * 아이디(이메일)
     * 이름
     * 패스워드
     * 주소
     * 전화번호
     * 성별
     * 생년월일
     */
    @NotNull
    private Long id;

    @ValidationBasicField(name = emailName, regex = emailRegex)
    private String userEmail; //아이디

    @ValidationBasicField(name = name, regex = nameRegex)
    private String userName; //이름

    private String address; //주소

    @ValidationPassword(name = passwordName, regex = passwordRegex)
    private String password; //비밀번호

    @ValidationBasicField(name = phoneName, regex = phoneRegex)
    private String phoneNumber; //전화번호

    @ValidationGenderField(genderMaleType = GENDER_MALE, genderFemaleType = GENDER_FEMALE)
    private String gender; //성별

    @ValidationBasicField(name = birthName, regex = birthRegex)
    private String birth; //생년월일

}
