/*
package spring_project.project.user.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.jfr.ContentType;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import spring_project.project.user.controller.dto.OauthToken;
import spring_project.project.user.domain.service.Strategy;

import java.io.IOException;
import java.net.http.HttpHeaders;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GOOGLE SNS 테스트")
class GoogleTest {

//    @InjectMock
//    Google google = new Google();

//    @Mock
//    Strategy strategy;

    @InjectMocks
    Google google;



    private MockWebServer mockWebServer;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.url("http://localhost:8080/");
        this.objectMapper = new ObjectMapper();

        mockWebServer.start();
    }

    @AfterEach
    void finish() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("google로그인 인가코드 받기 _ 성공")
    void sendUrlQuerySuccess() throws  Exception{
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        HttpUrl url = mockWebServer.url("/test");
        String params = "scope=email%20profile&response_type=code&access_type=offline&client_id=138695774678-6oie9bs83ui7pjob0kk6cr16486370q2.apps.googleusercontent.com&redirect_uri=http://localhost:8080/login/google/callback";

        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getRequestUrl().encodedPath()).isEqualTo(url.encodedPath());
        assertThat(request.getRequestUrl().encodedQuery()).isEqualTo(params);

        System.out.println(request);

    }

    @Test
    @DisplayName("google로그인 페이지 토큰받기 _ 성공")
    void sendCallbackUrlCodeSuccess() throws Exception{

        OauthToken responseBody = OauthToken.builder()
                .access_token("token")
                .expires_in("12345")
                .refresh_token("refresh_token")
                .id_token("id_token")
                .token_type("bearer")
                .scope("email profile")
                .build();

        mockWebServer.enqueue(new MockResponse()
        .setBody(objectMapper.writeValueAsString(responseBody)));

        HttpUrl url = mockWebServer.url("/login/google");
        String code = "code";

        google.sendCallbackUrlCode(code);
        RecordedRequest request = mockWebServer.takeRequest();

        //when
        assertThat(request.getRequestUrl().encodedPath()).isEqualTo(url.encodedPath());
//        assertThat(request.get)
    }
}*/
