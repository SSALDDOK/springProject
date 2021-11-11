package spring_project.project.user.infrastructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import spring_project.project.user.controller.dto.GoogleUser;
import spring_project.project.user.controller.dto.OauthToken;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("GOOGLE SNS 테스트")
@MockBean(JpaMetamodelMappingContext.class)
class GoogleTest {

    @InjectMocks
    Google google;

    public static MockWebServer mockWebServer;

    private static ObjectMapper objectMapper;

    String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        objectMapper = new ObjectMapper();
        mockWebServer.start();
    }

    @AfterAll
    static void finish() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @DisplayName("google 인가코드 받기 -성공")
    void sendUrlQuery() {
        //given
        ReflectionTestUtils.setField(google, "GOOGLE_SNS_CLIENT_ID", "GOOGLE_SNS_CLIENT_ID");
        ReflectionTestUtils.setField(google, "GOOGLE_SNS_CALLBACK_URL", "GOOGLE_SNS_CALLBACK_URL");
        ReflectionTestUtils.setField(google,"GOOGLE_SNS_BASE_URL",baseUrl);

        String params =baseUrl+"?scope=email%20profile&response_type=code&access_type=offline&client_id=GOOGLE_SNS_CLIENT_ID&redirect_uri=GOOGLE_SNS_CALLBACK_URL";

        //when
        String result = google.sendUrlQuery();

        //then
        assertThat(result).isEqualTo(params);
    }

    @Test
    @DisplayName("google로그인 토큰발행 _ 성공")
    void sendCallbackUrlCodeSuccess() throws Exception {
        //given
        ReflectionTestUtils.setField(google, "GOOGLE_SNS_CLIENT_ID", "GOOGLE_SNS_CLIENT_ID");
        ReflectionTestUtils.setField(google, "GOOGLE_SNS_CLIENT_SECRET", "GOOGLE_SNS_CLIENT_SECRET");
        ReflectionTestUtils.setField(google, "GOOGLE_SNS_CALLBACK_URL", "GOOGLE_SNS_CALLBACK_URL");
        ReflectionTestUtils.setField(google, "GOOGLE_SNS_TOKEN_BASE_URL", baseUrl);

        String code = "code";
        String token = "token";

        OauthToken responseDto = OauthToken.builder()
                .access_token(token)
                .token_type("bearer")
                .expires_in("12345")
                .refresh_token("refresh_token")
                .id_token("id_token")
                .scope("email")
                .build();

        String params ="code=code&client_id=GOOGLE_SNS_CLIENT_ID&client_secret=GOOGLE_SNS_CLIENT_SECRET&redirect_uri=GOOGLE_SNS_CALLBACK_URL&grant_type=authorization_code";

        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setHeader("Content-Type", MediaType.APPLICATION_JSON)
                .setBody(objectMapper.writeValueAsString(responseDto)));

        //when
        String result = google.sendCallbackUrlCode(code);
        RecordedRequest request = mockWebServer.takeRequest();

        //then

        assertThat(request.getMethod()).isEqualTo(HttpMethod.POST.toString());
        assertThat(request.getRequestUrl().encodedQuery()).isEqualTo(params);
        assertThat(result).isEqualTo(responseDto.getAccess_token());
    }


    @Test
    @DisplayName("google 토큰사용해서 유저정보 가져오기 _ 성공")
    void createGetRequestSuccess() throws Exception {
        ReflectionTestUtils.setField(google, "GOOGLE_SNS_USERINFO_URL", baseUrl);

        String token = "token";

        GoogleUser googleUserDto = GoogleUser.builder()
                .id("12345")
                .email("lizzy@plgrim.com")
                .verified_email(true)
                .name("leejy")
                .given_name("jy")
                .family_name("lee")
                .picture("picture")
                .locale("locale")
                .build();



        mockWebServer.enqueue(new MockResponse().setResponseCode(200)
                .setHeader("Content-Type", MediaType.APPLICATION_JSON)
                .setBody(objectMapper.writeValueAsString(googleUserDto)));

        String result = google.createGetRequest(token);

        RecordedRequest request = mockWebServer.takeRequest();

        System.out.println("request = " + request.getBody().toString());

        assertThat(request.getMethod()).isEqualTo(HttpMethod.GET.toString());
        assertThat(result).isEqualTo(googleUserDto.getEmail());
    }
}
