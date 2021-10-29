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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import spring_project.project.common.auth.provider.JwtTokenProvider;
import spring_project.project.common.auth.provider.SnsTokenProvider;
import spring_project.project.user.application.UserService;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import java.util.Arrays;
import java.util.List;

import static java.lang.String.valueOf;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static spring_project.project.common.enums.UserUrl.USER_ROOT_PATH;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@DisplayName("회원목록조회_컨트롤러테스트")
@MockBean(JpaMetamodelMappingContext.class)
public class UserListControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;

    ObjectMapper objectMapper;

    @MockBean
    JwtTokenProvider jwtTokenProvider;

    @MockBean
    SnsTokenProvider snsTokenProvider;

    @MockBean
    UserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {

        this.objectMapper = new ObjectMapper();

    }

    @Test
    @WithMockUser(value = "USER")
    @DisplayName("회원목록조회_컨트롤러_성공")
    void findUsersListControllerSuccessUnitTest() throws Exception {
        //given
        int page = 0;
        int pageCount = 2;

        User user = User.builder()
                .id(1L)
                .userEmail("lizzy@plgrim.com")
                .userName("lizzy")
                .password("jqijfe123")
                .gender("F")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("incheon")
                        .phoneNumber("010-8710-1086")
                        .build())
                .birth("19970717")
                .build();

        User user1 = User.builder()
                .id(2L)
                .userEmail("lizzy@plgrim.com")
                .userName("lizzy")
                .password("jqijfe123")
                .gender("F")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("incheon")
                        .phoneNumber("010-8710-1086")
                        .build())
                .birth("19970717")
                .build();

        List<User> userList = Arrays.asList(user, user1);
        String userListToString = objectMapper.writeValueAsString(userList);

        given(userService.list(page, pageCount)).willReturn(new PageImpl<>(userList));

        //when
        mvc.perform(get(USER_ROOT_PATH)
                .param("page", valueOf(page))
                .param("pageCount", valueOf(pageCount))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(userListToString))
                .andReturn();

    }

    @Test
    @DisplayName("회원목록조회_컨트롤러_실패_권한없음")
    void findUsersListControllerFailByUnauthorizedUnitTest() throws Exception {
        //given
        int page = 0;
        int pageCount = 2;

        //when
        //then
        mvc.perform(get(USER_ROOT_PATH)
                .param("page", valueOf(page))
                .param("pageCount", valueOf(pageCount))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print())
                .andReturn();

    }

}
