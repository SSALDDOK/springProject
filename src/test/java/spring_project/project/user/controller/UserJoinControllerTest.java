package spring_project.project.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import spring_project.project.user.controller.dto.UserJoinReqDTO;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static spring_project.project.common.enums.ErrorCode.DUPLICATE_EMAIL;
import static spring_project.project.common.enums.ErrorCode.DUPLICATE_PHONE_NUM;

@SpringBootTest
@DisplayName("회원가입_컨트롤러테스트")
@Transactional
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
class UserJoinControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext ctx;

    final UserJoinReqDTO dto = UserJoinReqDTO.builder()
            .userEmail("lizzy@plgrim.com")
            .userName("이지연")
            .password("password11")
            .gender("F")
            .address("incheon")
            .phoneNumber("010-8710-1086")
            .birth("19970717")
            .build();

    @BeforeEach
    public void setup() {

        this.mvc = webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();

    }

    @Test
    @DisplayName("회원가입_컨트롤러_성공")
    void joinControllerSuccessUnitTest() throws Exception {

        //given
        final User user = User.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("incheon")
                        .phoneNumber("010-8710-1086")
                        .build())
                .birth("19970717")
                .build();

        String mapper = objectMapper.writeValueAsString(dto);

        //response로 받은 값과 비교할 user 객체
        String userOne = objectMapper.writeValueAsString(user);

        //when
        MvcResult result = mvc.perform(post("/user/join")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

//        String actual = result.getResponse().;
//        objectMapper.writeValueAsString(actual);
        //then
//        assertThat(actual).usingDefaultComparator();
    }


    @Test
    @DisplayName("회원가입_컨트롤러_실패_이메일중복")
    void joinControllerFailByEamilDuplicationUnitTest() throws Exception {
        //given
        final UserJoinReqDTO failDto = UserJoinReqDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("리리지")
                .password("passwor11")
                .gender("M")
                .address("mikuk")
                .phoneNumber("010-710-1086")
                .birth("19970727")
                .build();

        String mapper = objectMapper.writeValueAsString(dto);
        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        mvc.perform(post("/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper));

        MvcResult result = mvc.perform(post("/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andReturn();

        int actual = result.getResponse().getStatus();

        //then
        assertThat(actual).isEqualTo(DUPLICATE_EMAIL.getHttpStatus().value());

    }

    @Test
    @DisplayName("회원가입_컨트롤러_실패_전화번호 중복")
    void joinControllerFailByPhoneNumberDuplicationUnitTest() throws Exception {
        //given
        final UserJoinReqDTO failDto = UserJoinReqDTO.builder()
                .userEmail("lizy@plgrim.com")
                .userName("리리지")
                .password("passwor11")
                .gender("M")
                .address("mikuk")
                .phoneNumber("010-8710-1086")
                .birth("19970727")
                .build();

        String mapper = objectMapper.writeValueAsString(dto);
        String testMapper = objectMapper.writeValueAsString(failDto);

        //when
        mvc.perform(post("/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper));

        MvcResult result = mvc.perform(post("/user/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(testMapper))
                .andDo(print())
                .andReturn();

        int actual = result.getResponse().getStatus();

        //then
        assertThat(actual).isEqualTo(DUPLICATE_PHONE_NUM.getHttpStatus().value());

    }

}