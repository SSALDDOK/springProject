package spring_project.project.user.domain.model.commands;

import lombok.*;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import java.util.List;


@Getter
@Builder
@AllArgsConstructor
//dto 변환
public class UserCommand {

    final private Long id;

    final private String userEmail; //아이디

    final private String userName; //이름

    final private String password; //비밀번호

    final private UserBasicInfo userBasicInfo; //주소,

    final private String gender; //성별

    final private String birth; //생년월일

    final private List<String> roles; //권한

}
