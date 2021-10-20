package spring_project.project.user.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring_project.project.auth.JwtTokenProvider;
import spring_project.project.common.enums.Encoder;
import spring_project.project.common.enums.SuccessCode;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.controller.dto.UserLoginDTO;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.commands.UserCommand;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;
import spring_project.project.user.infrastructure.repository.UserJpaRepository;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("로그인_서비스테스트")
public class UserServiceLoginTest {

    @Mock
    private  UserJpaRepository userRepository;

    @Mock
    private  JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    private static UserCommand command;

    @BeforeAll
    static void setUp(){

        command = UserCommand.builder()
                .id(1L)
                .userEmail("lizzy@plgrim.com")
                .password("jqijfe123")
                .build();
    }


    @Test
    @DisplayName("로그인 성공")
    void LoginSuccessUnitTest() throws CustomException {
        //given
        User user = User.builder()
                .userEmail(command.getUserEmail())
                .password(command.getPassword())
                .build();

        given(userRepository.findByUserEmail(any())).willReturn(Optional.of(user));

        given(encoder.matches(any(),any())).willReturn(true);
        //when
        Map<String,Object> result = userService.login(command);


        //then

    }
}
