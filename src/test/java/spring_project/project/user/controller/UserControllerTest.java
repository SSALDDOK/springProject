package spring_project.project.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import spring_project.project.user.application.UserService;
import spring_project.project.user.controller.dto.UserJoinReqDTO;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;
import spring_project.project.user.domain.service.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@DisplayName("회원가입_컨트롤러테스트")
@MockBean(JpaMetamodelMappingContext.class)
class UserControllerTest {

    @Autowired
    private MockMvc mvc ;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private UserController userController;

    @MockBean
    private UserRepository userRepository;

    final UserJoinReqDTO dto = UserJoinReqDTO.builder()
            .userEmail("lizzy@plgrim.com")
            .userName("이지연")
            .password("password11")
            .gender("F")
            .address("incheon")
            .phoneNumber("010-8710-1086")
            .birth("19970717")
            .build();

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


//    final  UserCommand userCommand =requestMapper.toCommand(dto);

    @Test
    @DisplayName("회원가입")
    void joinControllerSuccessUnitTest() throws Exception{
        String mapper = objectMapper.writeValueAsString(dto);

        mvc.perform(post("/user/join")
        .content(mapper)
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

}