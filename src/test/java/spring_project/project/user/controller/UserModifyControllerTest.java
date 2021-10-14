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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import spring_project.project.common.enums.ErrorCode;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.application.UserService;
import spring_project.project.user.controller.dto.UserJoinReqDTO;
import spring_project.project.user.controller.dto.UserModifyReqDTO;
import spring_project.project.user.controller.dto.mapper.RequestMapper;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static spring_project.project.common.enums.ErrorCode.EMPTY_USER;


@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@DisplayName("회원수정_컨트롤러테스트")
@MockBean(JpaMetamodelMappingContext.class)
public class UserModifyControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    UserService userService;

    private ObjectMapper objectMapper;

    RequestMapper requestMapper;

    UserJoinReqDTO dto;

    @BeforeEach
    public void setup() throws Exception {

        this.objectMapper = new ObjectMapper();
        this.requestMapper = new RequestMapper();

        dto = UserJoinReqDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber("010-8710-1086")
                .birth("19970717")
                .build();

        this.mvc = webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();

        //회원가입 컨트롤러 거쳐서 저장
        mvc.perform(post("/users/user")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON));

    }

    @Test
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

        given(userService.modify(any())).willReturn(user);

        //when
        //회원수정 컨트롤러 거쳐서 저장
        MvcResult result = mvc.perform(put("/users/user")
                .content(modifyMapper)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String resultContent = result.getResponse().getContentAsString();

        User actual = objectMapper.readValue(resultContent, User.class);

        //then
        assertThat(actual).usingRecursiveComparison().ignoringFields("createAt", "updateAt").isEqualTo(user);
    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
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
        mvc.perform(put("/users/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
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
        mvc.perform(put("/users/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
    @NullAndEmptySource
    //이름 정규식 불일치,5자 이상
    @ValueSource(strings = {"lizzy", "리지지지지"})
    @DisplayName("회원수정_이름유효성검사_실패")
//    @CsvSource({"이메일 값을 입력해 주세요.", "이메일 값을 입력해 주세요.", "이메일 형식에 맞게 입력해 주세요."})
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
        mvc.perform(put("/users/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @ParameterizedTest(name = "{index} {arguments} {displayName} ")
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
        mvc.perform(put("/users/user")
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
        mvc.perform(put("/users/user")
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
        mvc.perform(put("/users/user")
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
        mvc.perform(put("/users/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andReturn();

    }

    @Test
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

        given(userService.modify(any())).willThrow(new CustomException(EMPTY_USER));

        mvc.perform(put("/users/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andExpect(status().is(EMPTY_USER.getHttpStatus().value()))
                .andReturn();
    }

    //중복 테스트 ??? 질문

}
