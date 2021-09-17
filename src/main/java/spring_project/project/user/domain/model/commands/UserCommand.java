package spring_project.project.user.domain.model.commands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
//dto 변환
public class UserCommand {
    private String userEmail; //아이디

    private String userName; //이름

    private String password; //비밀번호

    private UserBasicInfo userBasicInfo; //주소,

    private String gender; //성별

    private String birth; //생년월일

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
