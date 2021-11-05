package spring_project.project.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OauthToken {

    private String access_token;
    private String expires_in;
    private String id_token;
    private String refresh_token;
    private String scope;
    private String token_type;

}
