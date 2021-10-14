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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import spring_project.project.user.application.UserService;
import spring_project.project.user.controller.dto.UserJoinReqDTO;
import spring_project.project.user.controller.dto.mapper.RequestMapper;
import spring_project.project.user.domain.model.aggregates.User;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@DisplayName("회원탈퇴_컨트롤러테스트")
@MockBean(JpaMetamodelMappingContext.class)
public class UserDeleteControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    UserService userService;

    ObjectMapper objectMapper;

    RequestMapper requestMapper;


    @BeforeEach
    void setUp() {

        this.requestMapper = new RequestMapper();
        this.objectMapper = new ObjectMapper();



        this.mvc = webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .build();

    }

    @Test
    @DisplayName("회원탈퇴_성공")
    void deleteControllerSuccessUnitTest() throws Exception {
        //given
        Long id =1L;

        String deleteId = objectMapper.writeValueAsString(id);
        willDoNothing().given(userService).delete(id);

        //when
        //then
        mvc.perform(delete("/users/{userId}",deleteId)
        .contentType(MediaType.APPLICATION_JSON)) //content 타입 컨트롤러 반환값
                .andDo(print())
                .andReturn();

    }


}
