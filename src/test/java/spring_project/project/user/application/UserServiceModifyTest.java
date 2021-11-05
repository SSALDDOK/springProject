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
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static spring_project.project.common.enums.ErrorCode.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원수정_서비스테스트")
public class UserServiceModifyTest {

    @Mock
    private static PasswordEncoder encoder;

    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private UserService userService;

    static String modifiedEmail = "lizzy@plgrim.com";

    static UserCommand command;

    @BeforeAll
    static void setUp() {
        //회원수정 command
        command = UserCommand.builder()
                .id(1L)
                .userEmail(modifiedEmail)
                .userName("lizzy")
                .password("jqijfe123")
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

    }

    @Test
    @DisplayName("회원수정_성공")
    void modifySuccessUnitTest() {
        //given
        User expected = User.builder()
                .id(1L)
                .userEmail(modifiedEmail)
                .password(encoder.encode("jqijfe123"))
                .build();

        given(userRepository.findById(anyLong())).willReturn(Optional.of(new User()));

        willReturn(new ArrayList<>())
                .given(userRepository)
                .findByUserEmailOrUserBasicInfoPhoneNumber(anyString(), anyString());

        given(userRepository.save(any())).willReturn(expected);

        //when
        User result = userService.modify(command);

        //then
        assertThat(result.getId()).isEqualTo(expected.getId());
        assertThat(result.getPassword()).isEqualTo(expected.getPassword());
        assertThat(result.getUserEmail()).isEqualTo(modifiedEmail);
    }

    @Test
    @DisplayName("회원수정_실패_회원없음")
    void modifyFailByNoExistUsersUnitTest() throws CustomException {
        //given
        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class, () -> userService.modify(command));

        //then
        assertThat(exception.getErrorCode()).isEqualTo(EMPTY_USER);
    }


    @ParameterizedTest(name = "{index} {arguments} {displayName}")
    @MethodSource("emailAndPhoneNum")
    @DisplayName("회원수정_실패_다른회원 정보중복(이메일or전화번호)")
    void modifyFailByExistedEmailOrPhoneNumberUnitTest(String email, String phoneNum) throws CustomException{
        //given
        User expected = User.builder()
                .id(2L)
                .userEmail(email)
                .userBasicInfo(UserBasicInfo.builder()
                        .phoneNumber(phoneNum)
                        .build())
                .build();


        List<User> validateUser = new ArrayList<>();
        validateUser.add(expected);

        given(userRepository.findById(anyLong())).willReturn(Optional.of(new User()));

        willReturn(validateUser)
                .given(userRepository)
                .findByUserEmailOrUserBasicInfoPhoneNumber(anyString(), anyString());

        //when
        CustomException exception = assertThrows(CustomException.class, () -> userService.modify(command));

        //then // 명확하게 만들기
        assertThat(exception.getErrorCode()).isBetween(DUPLICATE_EMAIL, DUPLICATE_PHONE_NUM);

    }

    static Stream<Arguments> emailAndPhoneNum() {
        return Stream.of(
                arguments("lizzy@plgrim.com", "010-870-1086"),
                arguments("lizy@plgrim.com", "010-8710-1086")
        );
    }
}
