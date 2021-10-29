package spring_project.project.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import spring_project.project.common.auth.provider.JwtTokenProvider;
import spring_project.project.common.auth.provider.SnsTokenProvider;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.application.UserService;
import spring_project.project.user.controller.dto.UserJoinReqDTO;
import spring_project.project.user.controller.dto.mapper.RequestMapper;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static spring_project.project.common.enums.ErrorCode.DUPLICATE_EMAIL;
import static spring_project.project.common.enums.ErrorCode.DUPLICATE_PHONE_NUM;
import static spring_project.project.common.enums.UserUrl.USER_NEW;
import static spring_project.project.common.enums.UserUrl.USER_ROOT_PATH;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@DisplayName("회원가입_컨트롤러테스트")
@MockBean(JpaMetamodelMappingContext.class)
class UserJoinControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    SnsTokenProvider snsTokenProvider;

    @MockBean
    UserDetailsService userDetailsService;

    private ObjectMapper objectMapper;

    RequestMapper requestMapper;

    @BeforeEach
    void setup() {

        this.requestMapper = new RequestMapper();
        this.objectMapper = new ObjectMapper();

    }

    @Test
    @DisplayName("회원가입_컨트롤러_성공")
    void joinControllerSuccessUnitTest() throws Exception {
        //given
        UserJoinReqDTO dto = UserJoinReqDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber("010-8710-1086")
                .birth("19970717")
                .build();

        User user = User.builder()
                .id(1L)
                .userEmail("lizzy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("incheon")
                        .phoneNumber("010-8710-1086")
                        .build())
                .birth("19970717")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        //dto -> json
        String mapper = objectMapper.writeValueAsString(dto);

        String testUser = objectMapper.writeValueAsString(user);

        //서비스 부분 given
        given(userService.join(any())).willReturn(user);

        //when
        //then
        mvc.perform(post(USER_ROOT_PATH + USER_NEW)
                .content(mapper)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(testUser))
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @NullAndEmptySource
    //이메일 정규식 불일치
    @ValueSource(strings = {"lizzyplgrim"})
    @DisplayName("회원가입_이메일유효성검사_실패")
//    @CsvSource({"이메일 값을 입력해 주세요.", "이메일 값을 입력해 주세요.", "이메일 형식에 맞게 입력해 주세요."})
    void joinControllerFailByEmailValidationUnitTest(String email) throws Exception {
        //given
        UserJoinReqDTO failDto = UserJoinReqDTO.builder()
                .userEmail(email)
                .userName("리리지")
                .password("passwor11")
                .gender("M")
                .address("mikuk")
                .phoneNumber("010-710-1086")
                .birth("19970727")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(post(USER_ROOT_PATH + USER_NEW)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @NullAndEmptySource
    //이름 정규식 불일치, 5자 이상일 때
    @ValueSource(strings = {"lizzy", "리지지지지"})
    @DisplayName("회원가입_이름유효성검사_실패")
    void joinControllerFailByNameValidationUnitTest(String name) throws Exception {
        //given
        UserJoinReqDTO failDto = UserJoinReqDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .userName(name)
                .password("passwor11")
                .gender("M")
                .address("mikuk")
                .phoneNumber("010-710-1086")
                .birth("19970727")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(post(USER_ROOT_PATH + USER_NEW)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @NullAndEmptySource
    //5자 이하, 15자 이상일 때,패스워드 정규식 불일치
    @ValueSource(strings = {"asdfewdsfefe1112", "z1", "한글불가","aaaaaaaa","!!!!!!!"})
    @DisplayName("회원가입_비밀번호 유효성검사_실패")
    void joinControllerFailByPasswordValidationUnitTest(String password) throws Exception {
        //given
        UserJoinReqDTO failDto = UserJoinReqDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("리리지")
                .password(password)
                .gender("M")
                .address("mikuk")
                .phoneNumber("010-710-1086")
                .birth("19970727")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(post(USER_ROOT_PATH + USER_NEW)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @NullAndEmptySource
    //15자 이상일 때,전화번호 정규식 불일치
    @ValueSource(strings = {"010-87777-77777", "asdfew", "한글불가"})
    @DisplayName("회원가입_전화번호 유효성검사_실패")
    void joinControllerFailByPhoneNumberValidationUnitTest(String phoneNum) throws Exception {
        //given
        UserJoinReqDTO failDto = UserJoinReqDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("리리지")
                .password("passwor11")
                .gender("M")
                .address("mikuk")
                .phoneNumber(phoneNum)
                .birth("19970727")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(post(USER_ROOT_PATH + USER_NEW)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @NullAndEmptySource
    //성별 "F,M"값이 아닐 때
    @ValueSource(strings = {"Female"})
    @DisplayName("회원가입_성별유효성검사_실패")
    void joinControllerFailByGenderValidationUnitTest(String gender) throws Exception {
        //given
        UserJoinReqDTO failDto = UserJoinReqDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("리리지")
                .password("passwor11")
                .gender(gender)
                .address("mikuk")
                .phoneNumber("010-710-1086")
                .birth("19970727")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(post(USER_ROOT_PATH + USER_NEW)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @NullAndEmptySource
    //9자 이상일 때,생년월일 정규식 불일치
    @ValueSource(strings = {"199707177", "970717"})
    @DisplayName("회원가입_생년월일 유효성검사_실패")
    void joinControllerFailByBirthValidationUnitTest(String birth) throws Exception {
        //given
        UserJoinReqDTO failDto = UserJoinReqDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("리리지")
                .password("passwor11")
                .gender("M")
                .address("mikuk")
                .phoneNumber("010-710-1086")
                .birth(birth)
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(post(USER_ROOT_PATH + USER_NEW)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @Test
    @DisplayName("회원가입_컨트롤러_실패_이메일중복")
    void joinControllerFailByEmailDuplicationUnitTest() throws Exception {
        //given
        UserJoinReqDTO failDto = UserJoinReqDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("리리지")
                .password("passwor11")
                .gender("M")
                .address("mikuk")
                .phoneNumber("010-710-1086")
                .birth("19970727")
                .build();

        //dto -> json

        String testMapper = objectMapper.writeValueAsString(failDto);

        given(userService.join(any())).willThrow(new CustomException(DUPLICATE_EMAIL));

        //when
        //then
        mvc.perform(post(USER_ROOT_PATH + USER_NEW)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is(DUPLICATE_EMAIL.getHttpStatus().value()))
                .andReturn();

    }

    @Test
    @DisplayName("회원가입_컨트롤러_실패_전화번호 중복")
    void joinControllerFailByPhoneNumberDuplicationUnitTest() throws Exception {
        //given
        UserJoinReqDTO failDto = UserJoinReqDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("리리지")
                .password("passwor11")
                .gender("M")
                .address("mikuk")
                .phoneNumber("010-8710-1086")
                .birth("19970727")
                .build();


        String testMapper = objectMapper.writeValueAsString(failDto);

        given(userService.join(any())).willThrow(new CustomException(DUPLICATE_PHONE_NUM));

        //when
        //then _ uri 객체 만들기
        mvc.perform(post(USER_ROOT_PATH + USER_NEW)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is(DUPLICATE_PHONE_NUM.getHttpStatus().value()))
                .andReturn();

    }

}