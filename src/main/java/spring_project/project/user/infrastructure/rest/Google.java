package spring_project.project.user.infrastructure.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import spring_project.project.common.enums.SnsType;
import spring_project.project.user.controller.dto.GoogleUser;
import spring_project.project.user.controller.dto.OauthToken;
import spring_project.project.user.domain.service.Strategy;

import java.util.Objects;


@Component
@Slf4j
public class Google implements Strategy {

    @Value("${sns.google.client.id}")
    public String GOOGLE_SNS_CLIENT_ID;

    @Value("${sns.google.callback.url}")
    public String GOOGLE_SNS_CALLBACK_URL;

    @Value("${sns.google.url}")
    public String GOOGLE_SNS_BASE_URL;

    @Value("${sns.google.client.secret}")
    public String GOOGLE_SNS_CLIENT_SECRET;

    @Value("${sns.google.token.base.url}")
    public String GOOGLE_SNS_TOKEN_BASE_URL;

    @Value("${sns.google.userinfo.url}")
    public String GOOGLE_SNS_USERINFO_URL;

    @Override
    public String sendUrlQuery() {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>() {{
            add("scope", "email profile");
            add("response_type", "code");
            add("access_type", "offline");
            add("client_id", GOOGLE_SNS_CLIENT_ID);
            add("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        }};

        return UriComponentsBuilder.fromHttpUrl(GOOGLE_SNS_BASE_URL).queryParams(query).toUriString();

    }

    @Override
    public String sendCallbackUrlCode(String code) throws Exception {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", GOOGLE_SNS_CLIENT_ID);
        params.add("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.add("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.add("grant_type", "authorization_code");

        OauthToken response = WebClient.create(GOOGLE_SNS_TOKEN_BASE_URL)
                .post()
                .uri(uriBuilder -> uriBuilder.queryParams(params).build())
                .retrieve()
                .bodyToFlux(OauthToken.class)
                .blockFirst();

        //requireNonNull은 해당 참조가 null일 경우 즉시 개발자에게 알리는 것
       return Objects.requireNonNull(response).getAccess_token();

    }


    public String createGetRequest(String oauthAccessToken) {
        GoogleUser response = WebClient.create(GOOGLE_SNS_USERINFO_URL)
                .get()
                .headers(httpHeaders -> httpHeaders.add("Authorization", "Bearer " + oauthAccessToken))
                .retrieve()
                .bodyToFlux(GoogleUser.class)
                .blockFirst();

        return Objects.requireNonNull(response).getEmail();
    }

    @Override
    public SnsType getSnsType() {

        return SnsType.GOOGLE;

    }
}
