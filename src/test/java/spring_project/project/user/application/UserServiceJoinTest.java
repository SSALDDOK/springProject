package spring_project.project.user.application;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.commands.UserCommand;
import spring_project.project.user.domain.model.entities.UserRole;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;
import spring_project.project.user.infrastructure.repository.UserJpaRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static spring_project.project.common.enums.ErrorCode.DUPLICATE_EMAIL;
import static spring_project.project.common.enums.ErrorCode.DUPLICATE_PHONE_NUM;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원가입_단위테스트")
public class UserServiceJoinTest {

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserService userService;

    static UserCommand command;

    @BeforeAll
    static void setUp(){
        command = UserCommand.builder()
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
    }


    @Test
    @DisplayName("회원가입_성공")
    void joinSuccessUnitTest() {
        //given
        final User user = User.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("lizzy")
                .password(encoder.encode("jqijfe123"))
                .gender("F")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("incheon")
                        .phoneNumber("010-8710-1086")
                        .build())
                .birth("19970717")
                .roles(Collections.singletonList(UserRole.builder()
                        .authority("ROLE_USER")
                        .build()))
                .build();

        given(userRepository.save(any())).willReturn(user);

        //when
        User result = userService.join(command);
        System.out.println(result);
        System.out.println(user);

        //then
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(user);

    }

    @ParameterizedTest
    @MethodSource("emailAndPhoneNum")
    @DisplayName("회원가입_실패_이메일 or 전화번호 중복")
    void joinFailByEmailOrPhoneNumberUnitTest(String email, String phoneNum) throws CustomException {
        //given
        final User user = User.builder()
                .userEmail(email)
                .userBasicInfo(UserBasicInfo.builder().phoneNumber(phoneNum).build())
                .build();

        final User commandToUser = User.builder()
                .userEmail(command.getUserEmail())
                .userBasicInfo(UserBasicInfo.builder()
                        .phoneNumber(command.getUserBasicInfo().getPhoneNumber())
                        .build())
                .build();


        List<User> validateUser = new ArrayList<>();
        validateUser.add(user);

        //when - 이메일이 중복됬을 때, 전화번호가 중복됬을 때
        willReturn(validateUser)
                .given(userRepository)
                .findByUserEmailOrUserBasicInfoPhoneNumber(commandToUser.getUserEmail(), commandToUser.getUserBasicInfo().getPhoneNumber());
        //doReturn|when 으로 쓸 수도 있음

        CustomException exception = assertThrows(CustomException.class, () -> userService.join(command));

        //then
        assertThat(exception.getErrorCode()).isBetween(DUPLICATE_EMAIL,DUPLICATE_PHONE_NUM);
    }

    static Stream<Arguments> emailAndPhoneNum() {
        return Stream.of(
                arguments("lizzy@plgrim.com", "010-870-1086"),
                arguments("lizy@plgrim.com", "010-8710-1086")
        );
    }
}


