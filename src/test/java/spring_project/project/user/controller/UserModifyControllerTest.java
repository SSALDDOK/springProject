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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@SpringBootTest
@DisplayName("회원수정_컨트롤러테스트")
@Transactional
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
public class UserModifyControllerTest {

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
    public void setup() throws Exception {

        this.mvc = webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();

        //회원가입 컨트롤러 거쳐서 저장
        mvc.perform(post("/user/join")
                .content(objectMapper.writeValueAsString(dto))
                .contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    @DisplayName("회원수정_컨트롤러_성공")
    void modifyControllerSuccessUnitTest() throws Exception {

        //given
        final UserJoinReqDTO modifyDto = UserJoinReqDTO.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("이지연")
                .password("password11")
                .gender("F")
                .address("incheon")
                .phoneNumber("010-871-1086")
                .birth("19970717")
                .build();

        final User user = User.builder()
                .userEmail("lizzy@plgrim.com")
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

        String userOne = objectMapper.writeValueAsString(user);

        //when
        //회원수정 컨트롤러 거쳐서 저장
        MvcResult result = mvc.perform(put("/user/modify")
                .content(modifyMapper)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        String actual = result.getResponse().getContentAsString();

        //then
        assertThat(actual).isEqualTo(userOne);
    }
}
