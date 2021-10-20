package spring_project.project.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDTO {
    /**
     * 이메일
     * 패스워드
     */

    private String userEmail;
    private String password;
}
