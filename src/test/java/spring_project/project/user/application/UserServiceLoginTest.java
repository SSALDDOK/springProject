package spring_project.project.user.application;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring_project.project.common.auth.provider.JwtTokenProvider;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.commands.UserCommand;
import spring_project.project.user.domain.service.SnsLoginService;
import spring_project.project.user.infrastructure.repository.UserJpaRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static spring_project.project.common.enums.ErrorCode.EMPTY_USER_EMAIL;
import static spring_project.project.common.enums.ErrorCode.NOT_MATCHES_PASSWORD;

@ExtendWith(MockitoExtension.class)
@DisplayName("로그인_서비스테스트")
public class UserServiceLoginTest {

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private SnsLoginService snsLoginService;

    @InjectMocks
    private UserService userService;

    private static UserCommand command;

    @BeforeAll
    static void setUp() {

        command = UserCommand.builder()
                .id(1L)
                .userEmail("lizzy@plgrim.com")
                .password("jqijfe123")
                .build();
    }


    @Test
    @DisplayName("로컬로그인 성공")
    void localLoginSuccessUnitTest() throws CustomException {
        //given
        User user = User.builder()
                .userEmail(command.getUserEmail())
                .password(command.getPassword())
                .build();

        given(userRepository.findByUserEmail(any())).willReturn(Optional.of(user));

        given(encoder.matches(any(), any())).willReturn(true);

        given(jwtTokenProvider.createToken(any(), any())).willReturn("token");

        //when
        String result = userService.localLogin(command);

        //then
        assertThat(result).isEqualTo("token");
    }

    @Test
    @DisplayName("로컬로그인 실패_이메일 없음")
    void localLoginFailByEmptyEmailUnitTest() {
        //given
        given(userRepository.findByUserEmail(any())).willReturn(Optional.empty());

        //when
        CustomException customException = assertThrows(CustomException.class, () -> userService.localLogin(command));

        //then
        assertThat(customException.getErrorCode()).isEqualTo(EMPTY_USER_EMAIL);
    }

    @Test
    @DisplayName("로컬로그인 실패_비밀번호 불일치")
    void localLoginFailByNotMatchesPasswordUnitTest() {
        //given
        given(userRepository.findByUserEmail(any())).willReturn(Optional.of(new User()));

        given(encoder.matches(any(), any())).willReturn(false);

        //when
        CustomException customException = assertThrows(CustomException.class, () -> userService.localLogin(command));

        //then
        assertThat(customException.getErrorCode()).isEqualTo(NOT_MATCHES_PASSWORD);
    }

    @Test
    @DisplayName("SNS 로그인 페이지 성공")
    void snsLoginPageSuccessUnitTest() throws Exception {
        //given
        String google = "google";
        String googleUrl ="googleUrl";

        given(snsLoginService.findSnsRedirectUrl(any())).willReturn(googleUrl);

        //when
        String result = userService.snsLogin(google);

        //then
        assertThat(result).isEqualTo(googleUrl);
    }

    @Test
    @DisplayName("SNS 로그인 성공 _ 토큰 발행")
    void snsLoginSuccessUnitTest() throws Exception {
        //given
        String snsTestEmail = "lizzy@plgrim.com";
        String snsType = "google";
        String code = "code";
        String token = "token";

        User user = User.builder()
                .userEmail(snsTestEmail)
                .build();

        //테스트다시
        given(snsLoginService.createPostToken(any(), any())).willReturn(token);
        given(snsLoginService.createGetRequest(any(), any())).willReturn(snsTestEmail);
        given(userRepository.findByUserEmail(any())).willReturn(Optional.of(user));
        given(jwtTokenProvider.createToken(any(), any())).willReturn(token);

        //when
        String result = userService.snsOauthLogin(snsType, code);

        //when
        assertThat(result).isEqualTo(token);
    }

    @Test
    @DisplayName("SNS 로그인 실패 _ 회원 이메일 없음")
    void snsLoginFailByNotExistUserUnitTest() throws Exception {
        //given
        String snsType = "google";
        String code = "code";

        given(userRepository.findByUserEmail(any())).willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class, () -> userService.snsOauthLogin(snsType, code));

        //then
        assertThat(exception.getErrorCode()).isEqualTo(EMPTY_USER_EMAIL);
    }

}
