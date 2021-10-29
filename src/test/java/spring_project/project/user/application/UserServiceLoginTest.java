package spring_project.project.user.application;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring_project.project.common.auth.provider.JwtTokenProvider;
import spring_project.project.common.auth.provider.SnsTokenProvider;
import spring_project.project.common.enums.Encoder;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.commands.UserCommand;
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
    private SnsTokenProvider snsTokenProvider;

    @Mock
    private Encoder encoder;

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

        given(jwtTokenProvider.createToken(any(), any())).willReturn("jwt");

        //when
         String result = userService.localLogin(command);

        //then
        assertThat(result).isEqualTo("jwt");
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

        given(encoder.matches(any(),any())).willReturn(false);

        //when
        CustomException customException = assertThrows(CustomException.class, () -> userService.localLogin(command));

        //then
        assertThat(customException.getErrorCode()).isEqualTo(NOT_MATCHES_PASSWORD);
    }

//    @Test
//    @DisplayName("SNS 로그인 성공")
//    void snsLoginSuccessUnitTest() throws Exception{
//        //given
//        String code = "test_code";
//        String token = "test_token";
//
//        OauthToken oauthToken = OauthToken.builder()
//                .access_token(token)
//                .build();
//
//        given(snsTokenProvider.createToken(any())).willReturn(ResponseEntity.ok(oauthToken));
//
//        //when
//        ResponseEntity<OauthToken> result = userService.oauthLogin(code);
//
//        //then
//        assertThat(result).isEqualTo(ResponseEntity.ok(oauthToken));
//    }

//    @Test
//    @DisplayName("SNS 로그인 실패_토큰이 없음")
//    void snsLoginFailByTokenUnitTest() throws Exception{
//        //given
//        String code = "test_code";
//        String token = "test_token";
//
//        OauthToken oauthToken = OauthToken.builder()
//                .access_token(token)
//                .build();
//
//        given(snsTokenProvider.createToken(any())).willReturn();
//
//        //when
//        ResponseEntity<OauthToken> result = userService.oauthLogin(code);
//
//        //then
//        assertThat(result).isEqualTo(ResponseEntity.ok(oauthToken));
//    }

}
