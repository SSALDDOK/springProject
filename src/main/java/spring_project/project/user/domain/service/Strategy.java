package spring_project.project.user.domain.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import spring_project.project.common.enums.SnsType;
import spring_project.project.user.controller.dto.OauthToken;

public interface Strategy {
    UriComponentsBuilder sendUrlQuery();
    ResponseEntity<OauthToken> sendCallbackUrlCode(String code) throws Exception;
    SnsType getSnsType();
}
