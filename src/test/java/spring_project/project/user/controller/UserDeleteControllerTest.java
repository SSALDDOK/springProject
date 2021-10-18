package spring_project.project.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
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
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.application.UserService;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static spring_project.project.common.enums.ErrorCode.EMPTY_DELETE_USER;
import static spring_project.project.common.enums.UserUrl.USER_ID;
import static spring_project.project.common.enums.UserUrl.USER_ROOT_PATH;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UserController.class)
@DisplayName("회원탈퇴_컨트롤러테스트")
@MockBean(JpaMetamodelMappingContext.class)
public class UserDeleteControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    UserService userService;

    static ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {

        objectMapper = new ObjectMapper();

    }

    //리턴되는 값들에 대해 모든 테스트
    @Test
    @DisplayName("회원탈퇴_성공")
    void deleteControllerSuccessUnitTest() throws Exception {
        //given
        Long deleteId =1L;

        willDoNothing().given(userService).delete(deleteId);

        //when
        //then
        mvc.perform(delete(USER_ROOT_PATH+USER_ID,deleteId)
        .contentType(MediaType.APPLICATION_JSON)) //content 타입 컨트롤러 반환값
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    @DisplayName("회원탈퇴_실패(회원없음)")
    void deleteControllerFailByEmptyUser() throws Exception{
        //given
        Long deleteId = 1L;

        willThrow(new CustomException(EMPTY_DELETE_USER)).given(userService).delete(deleteId);

        mvc.perform(delete(USER_ROOT_PATH+USER_ID,deleteId)
        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is(EMPTY_DELETE_USER.getHttpStatus().value()))
                .andReturn();

    }

}
