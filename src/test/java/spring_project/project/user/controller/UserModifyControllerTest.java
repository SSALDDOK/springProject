package spring_project.project.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import spring_project.project.common.auth.provider.JwtTokenProvider;
import spring_project.project.common.auth.provider.SnsTokenProvider;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.application.UserService;
import spring_project.project.user.controller.dto.UserModifyReqDTO;
import spring_project.project.user.controller.dto.mapper.RequestMapper;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static spring_project.project.common.enums.ErrorCode.*;
import static spring_project.project.common.enums.UserUrl.USER_ROOT_PATH;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@DisplayName("회원수정_컨트롤러테스트")
@MockBean(JpaMetamodelMappingContext.class)
public class UserModifyControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;

    private ObjectMapper objectMapper;

    RequestMapper requestMapper;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    SnsTokenProvider snsTokenProvider;

    @MockBean
    UserDetailsService userDetailsService;

    @BeforeEach
    public void setup() {

        this.objectMapper = new ObjectMapper();
        this.requestMapper = new RequestMapper();

    }

    @Test
    @WithMockUser(value = "USER")
    @DisplayName("회원수정_컨트롤러_성공")
    void modifyControllerSuccessUnitTest() throws Exception {

        //given
        UserModifyReqDTO modifyDto = UserModifyReqDTO.builder()
                .id(1L)
                .userEmail("lizy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber("010-871-1086")
                .birth("19970717")
                .build();

        User user = User.builder()
                .id(1L)
                .userEmail("lizy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("incheon")
                        .phoneNumber("010-871-1086")
                        .build())
                .birth("19970717")
                .build();

        String modifyMapper = objectMapper.writeValueAsString(modifyDto);

        String testUser = objectMapper.writeValueAsString(user);

        given(userService.modify(any())).willReturn(user);

        //when
        //회원수정 컨트롤러 거쳐서 저장
         mvc.perform(put(USER_ROOT_PATH)
                .content(modifyMapper)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(testUser))
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @WithMockUser(value = "USER")
    @NullSource
    //이메일 정규식 불일치
    @DisplayName("회원수정_아이디유효성검사_실패")
//    @CsvSource({"이메일 값을 입력해 주세요.", "이메일 값을 입력해 주세요.", "이메일 형식에 맞게 입력해 주세요."})
    void modifyControllerFailByIdValidationUnitTest(Long id) throws Exception {
        //given
        UserModifyReqDTO failDto = UserModifyReqDTO.builder()
                .id(id)
                .userEmail("lizy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber("010-871-1086")
                .birth("19970717")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(put(USER_ROOT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @WithMockUser(value = "USER")
    @NullAndEmptySource
    //이메일 정규식 불일치
    @ValueSource(strings = {"lizzyplgrim"})
    @DisplayName("회원수정_이메일유효성검사_실패")
//    @CsvSource({"이메일 값을 입력해 주세요.", "이메일 값을 입력해 주세요.", "이메일 형식에 맞게 입력해 주세요."})
    void modifyControllerFailByEmailValidationUnitTest(String email) throws Exception {
        //given
        UserModifyReqDTO failDto = UserModifyReqDTO.builder()
                .id(1L)
                .userEmail(email)
                .userName("이지연")
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber("010-871-1086")
                .birth("19970717")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(put(USER_ROOT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @WithMockUser(value = "USER")
    @NullAndEmptySource
    //이름 정규식 불일치,5자 이상
    @ValueSource(strings = {"lizzy", "리지지지지"})
    @DisplayName("회원수정_이름유효성검사_실패")
    void modifyControllerFailByNameValidationUnitTest(String name) throws Exception {
        //given
        UserModifyReqDTO failDto = UserModifyReqDTO.builder()
                .id(1L)
                .userEmail("lizy@plgrim.com")
                .userName(name)
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber("010-871-1086")
                .birth("19970717")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(put(USER_ROOT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @WithMockUser(value = "USER")
    @NullAndEmptySource
    //15자 이상일 때,패스워드 정규식 불일치
    @ValueSource(strings = {"asdfewdsfefe1112", "z!", "한글불가"})
    @DisplayName("회원수정_비밀번호 유효성검사_실패")
    void modifyControllerFailByPasswordValidationUnitTest(String password) throws Exception {
        //given
        UserModifyReqDTO failDto = UserModifyReqDTO.builder()
                .id(1L)
                .userEmail("lizy@plgrim.com")
                .userName("이지연")
                .password(password)
                .gender("F")
                .address("incheon")
                .phoneNumber("010-871-1086")
                .birth("19970717")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(put(USER_ROOT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @WithMockUser(value = "USER")
    @NullAndEmptySource
    //15자 이상일 때,전화번호 정규식 불일치
    @ValueSource(strings = {"010-87777-77777", "asdfew", "한글불가"})
    @DisplayName("회원수정_전화번호 유효성검사_실패")
    void modifyControllerFailByPhoneNumberValidationUnitTest(String phoneNum) throws Exception {
        //given
        UserModifyReqDTO failDto = UserModifyReqDTO.builder()
                .id(1L)
                .userEmail("lizy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber(phoneNum)
                .birth("19970717")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(put(USER_ROOT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }


    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @WithMockUser(value = "USER")
    @NullAndEmptySource
    //성별 "F,M"값이 아닐 때
    @ValueSource(strings = {"Female"})
    @DisplayName("회원수정_성별유효성검사_실패")
    void modifyControllerFailByGenderValidationUnitTest(String gender) throws Exception {
        //given
        UserModifyReqDTO failDto = UserModifyReqDTO.builder()
                .id(1L)
                .userEmail("lizy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender(gender)
                .address("incheon")
                .phoneNumber("010-871-1086")
                .birth("19970717")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(put(USER_ROOT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @WithMockUser(value = "USER")
    @NullAndEmptySource
    //9자 이상일 때,생년월일 정규식 불일치
    @ValueSource(strings = {"199707177", "970717"})
    @DisplayName("회원수정_생년월일 유효성검사_실패")
    void modifyControllerFailByBirthValidationUnitTest(String birth) throws Exception {
        //given
        UserModifyReqDTO failDto = UserModifyReqDTO.builder()
                .id(1L)
                .userEmail("lizy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber("010-871-1086")
                .birth(birth)
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(put(USER_ROOT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @Test
    @WithMockUser(value = "USER")
    @DisplayName("회원수정_실패_회원없음")
    void modifyControllerFailByNoExistUsersUnitTest() throws Exception {
        UserModifyReqDTO failDto = UserModifyReqDTO.builder()
                .id(1L)
                .userEmail("lizy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber("010-871-1086")
                .birth("19970717")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

//        given(userService.modify(any())).willThrow(new CustomException(EMPTY_USER));
        willThrow(new CustomException(EMPTY_USER)).given(userService).modify(any());

        mvc.perform(put(USER_ROOT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is(EMPTY_USER.getHttpStatus().value()))
                .andReturn();
    }

    @Test
    @WithMockUser(value = "USER")
    @DisplayName("회원수정_컨트롤러_실패_이메일중복")
    void joinControllerFailByEmailDuplicationUnitTest() throws Exception {
        //given
        UserModifyReqDTO failDto = UserModifyReqDTO.builder()
                .id(1L)
                .userEmail("lizy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber("010-871-1086")
                .birth("19970717")
                .build();

        //dto -> json
        String testMapper = objectMapper.writeValueAsString(failDto);

        willThrow(new CustomException(DUPLICATE_EMAIL)).given(userService).modify(any());

        //when
        mvc.perform(put(USER_ROOT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is(DUPLICATE_EMAIL.getHttpStatus().value()))
                .andReturn();

    }

    @Test
    @WithMockUser(value = "USER")
    @DisplayName("회원수정_컨트롤러_실패_전화번호 중복")
    void joinControllerFailByPhoneNumberDuplicationUnitTest() throws Exception {
        //given
        UserModifyReqDTO failDto = UserModifyReqDTO.builder()
                .id(1L)
                .userEmail("lizy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber("010-871-1086")
                .birth("19970717")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        willThrow(new CustomException(DUPLICATE_PHONE_NUM)).given(userService).modify(any());
        //when
        //then
        mvc.perform(put(USER_ROOT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is(DUPLICATE_PHONE_NUM.getHttpStatus().value()))
                .andReturn();

    }

    @Test
    @DisplayName("회원수정_컨트롤러_실패_권한없음")
    void joinControllerFailByUnauthorizedUnitTest() throws Exception {
        //given
        UserModifyReqDTO failDto = UserModifyReqDTO.builder()
                .id(1L)
                .userEmail("lizy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber("010-871-1086")
                .birth("19970717")
                .build();

        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        //then
        mvc.perform(put(USER_ROOT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is(NOT_UNAUTHORIZED.getHttpStatus().value()))
                .andReturn();

    }

}
