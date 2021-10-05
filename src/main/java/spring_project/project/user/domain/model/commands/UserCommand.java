package spring_project.project.user.domain.model.commands;

import lombok.*;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;



@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
//dto 변환
public class UserCommand {

    private Long id;

    private String userEmail; //아이디

    private String userName; //이름

    private String password; //비밀번호

    private UserBasicInfo userBasicInfo; //주소,

    private String gender; //성별

    private String birth; //생년월일

}
