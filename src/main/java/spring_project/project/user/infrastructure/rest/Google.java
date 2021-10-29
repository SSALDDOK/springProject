package spring_project.project.user.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import spring_project.project.common.enums.SnsType;
import spring_project.project.user.controller.dto.OauthToken;
import spring_project.project.user.domain.service.Strategy;


@Component
@RequiredArgsConstructor
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

    @Override
    public UriComponentsBuilder sendUrlQuery() {
        MultiValueMap<String, String> query = new LinkedMultiValueMap<>() {{
            add("scope", "profile");
            add("response_type", "code");
            add("access_type", "offline");
            add("client_id", GOOGLE_SNS_CLIENT_ID);
            add("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        }};

        return UriComponentsBuilder.fromHttpUrl(GOOGLE_SNS_BASE_URL).queryParams(query);

    }

    @Override
    public ResponseEntity<OauthToken> sendCallbackUrlCode(String code) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", GOOGLE_SNS_CLIENT_ID);
        params.add("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.add("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        return restTemplate.exchange(GOOGLE_SNS_TOKEN_BASE_URL, HttpMethod.POST, httpEntity, OauthToken.class);

    }

    @Override
    public SnsType getSnsType() {
        return SnsType.GOOGLE;
    }
}
