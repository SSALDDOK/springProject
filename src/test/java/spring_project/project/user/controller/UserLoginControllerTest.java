package spring_project.project.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import spring_project.project.common.auth.provider.JwtTokenProvider;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.application.UserService;
import spring_project.project.user.controller.dto.UserLoginDTO;
import spring_project.project.user.controller.dto.mapper.RequestMapper;
import spring_project.project.user.domain.service.Strategy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static spring_project.project.common.enums.ErrorCode.EMPTY_USER_EMAIL;
import static spring_project.project.common.enums.ErrorCode.NOT_MATCHES_PASSWORD;
import static spring_project.project.common.enums.UserUrl.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserLoginController.class)
@DisplayName("로그인_컨트롤러테스트")
@MockBean(JpaMetamodelMappingContext.class)
//컨트롤러 테스트 클래스와 컨트롤러 클래스 이름을 다르게 만들자..
public class UserLoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    UserDetailsService userDetailsService;

    private ObjectMapper objectMapper;

    RequestMapper requestMapper;

    @MockBean
    Strategy Google;


    @BeforeEach
    public void setUp() {

        this.objectMapper = new ObjectMapper();
        this.requestMapper = new RequestMapper();

    }

    @Test
    @DisplayName("로컬 로그인_컨트롤러_성공")
    void localLoginControllerSuccessUnitTest() throws Exception {
        //given
        UserLoginDTO dto = UserLoginDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .password("password11")
                .build();

        String mapper = objectMapper.writeValueAsString(dto);

        given(userService.localLogin(any())).willReturn("token");

        //when
        //then
        mvc.perform(post(LOGIN_ROOT_PATH)
                .content(mapper)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string("token"))
                .andReturn();

    }

    @Test
    @DisplayName("로컬 로그인_컨트롤러_실패_이메일 없음")
    void localLoginControllerFailByEmptyEmailUnitTest() throws Exception {
        //given
        UserLoginDTO dto = UserLoginDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .password("password11")
                .build();

        String mapper = objectMapper.writeValueAsString(dto);

        given(userService.localLogin(any())).willThrow(new CustomException(EMPTY_USER_EMAIL));

        //when
        //then
        mvc.perform(post(LOGIN_ROOT_PATH)
                .content(mapper)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(EMPTY_USER_EMAIL.getHttpStatus().value()))
                .andDo(print())
                .andReturn();
    }


    @Test
    @DisplayName("로컬 로그인_컨트롤러_실패_비밀번호 불일치")
    void localLoginControllerFailByNotMatchesPasswordUnitTest() throws Exception {
        //given
        UserLoginDTO dto = UserLoginDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .password("password11")
                .build();

        String mapper = objectMapper.writeValueAsString(dto);

        given(userService.localLogin(any())).willThrow(new CustomException(NOT_MATCHES_PASSWORD));

        //when
        //then
        mvc.perform(post(LOGIN_ROOT_PATH)
                .content(mapper)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(NOT_MATCHES_PASSWORD.getHttpStatus().value()))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("SNS 로그인 페이지 불러오기_컨트롤러_성공")
    void snsLoginControllerSuccessUnitTest() throws Exception {
        //given
        String redirectUrl = "googleUrl";

        given(userService.snsLogin(any())).willReturn(redirectUrl);

        //when
        //then
        mvc.perform(get(LOGIN_ROOT_PATH + LOGIN_SNS_PATH,"google"))
                .andExpect(status().is3xxRedirection())
                .andDo(print())
                .andExpect(redirectedUrl(redirectUrl))
                .andReturn();
    }


    @Test
    @DisplayName("SNS 로그인_콜백_컨트롤러_성공")
    void snsLoginCallbackControllerSuccessUnitTest() throws Exception {
        // given
        String code = "test_code";
        String token = "test_token";

        MultiValueMap<String, String> query = new LinkedMultiValueMap<>() {{
            add("code", code);
        }};

        given(userService.snsOauthLogin(any(),any())).willReturn(token);

        //  when
        //  then
        mvc.perform(get(LOGIN_ROOT_PATH + LOGIN_SNS_CALLBACK_PATH,"google")
                .queryParams(query))
                .andExpect(status().isOk())
                .andExpect(content().string(token))
                .andDo(print())
                .andReturn();
    }

    @Test
    @DisplayName("SNS 로그인_콜백_컨트롤러_실패_회원이메일 없음")
    void snsLoginCallbackControllerFailByNotExistUserEmailUnitTest() throws Exception {
        // given
        String code = "test_code";

        MultiValueMap<String, String> query = new LinkedMultiValueMap<>() {{
            add("code", code);
        }};

        given(userService.snsOauthLogin(any(),any())).willThrow(new CustomException(EMPTY_USER_EMAIL));

        //  when
        //  then
        mvc.perform(get(LOGIN_ROOT_PATH + LOGIN_SNS_CALLBACK_PATH,"google")
                .queryParams(query))
                .andExpect(status().is(EMPTY_USER_EMAIL.getHttpStatus().value()))
                .andDo(print())
                .andReturn();
    }

}
