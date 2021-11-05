package spring_project.project.user.domain.service;

import spring_project.project.common.enums.SnsType;

public interface Strategy {
    String sendUrlQuery();
    String sendCallbackUrlCode(String code) throws Exception;
    String createGetRequest(String oauthAccessToken);
    SnsType getSnsType();
}
