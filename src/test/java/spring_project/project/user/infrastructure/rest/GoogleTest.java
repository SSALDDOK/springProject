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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import spring_project.project.common.enums.SnsType;
import spring_project.project.user.controller.dto.OauthToken;
import spring_project.project.user.domain.service.Strategy;
import spring_project.project.user.domain.service.StrategyFactory;

import java.io.IOException;
import java.net.http.HttpHeaders;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static spring_project.project.common.enums.SnsType.GOOGLE;

@ExtendWith(MockitoExtension.class)
@DisplayName("GOOGLE SNS 테스트")
class GoogleTest {

//    @InjectMock
//    Google google = new Google();

    @Mock
    Strategy strategy;

    @InjectMocks
    Google google;

    private MockWebServer mockWebServer;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
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
//        String googleUrl = "googleUrl";
//
//        given(strategy.getSnsType()).willReturn(GOOGLE);
//        given(google.sendUrlQuery()).willReturn(googleUrl);
//        String result = strategy.sendUrlQuery();
////        given(google.sendUrlQuery()).willReturn(googleUrl);
//        System.out.println(result);
//
//        String result = strategy.sendUrlQuery();

//        assertThat(result).isEqualTo(googleUrl);


    }

    @Test
    @DisplayName("google로그인 페이지 토큰받기 _ 성공")
    void sendCallbackUrlCodeSuccess() throws Exception{

    }
}
