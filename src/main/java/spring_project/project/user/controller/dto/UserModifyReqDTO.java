package spring_project.project.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyReqDTO {

    private String userEmail; //아이디

    private String password; //비밀번호

    private String userName; //이름

    private String address; //주소

    private String phoneNumber; //전화번호

    private String gender; //성별

    private String birth; //생년월일

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

}
