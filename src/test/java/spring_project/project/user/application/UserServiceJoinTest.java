package spring_project.project.user.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.commands.UserCommand;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;
import spring_project.project.user.domain.service.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static spring_project.project.common.enums.ErrorCode.DUPLICATE_EMAIL;
import static spring_project.project.common.enums.ErrorCode.DUPLICATE_PHONE_NUM;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원가입_단위테스트")
public class UserServiceJoinTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    final UserCommand command = UserCommand.builder()
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

    @Test
    @DisplayName("회원가입_성공")
    void joinSuccessUnitTest() {
        //given
        final User user = User.builder()
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

        given(userRepository.save(any())).willReturn(user);

        //when
        User result = userService.join(command);

        //then
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("createAt", "updateAt")
                .isEqualTo(user);

    }

    @Test
    @DisplayName("회원가입_실패_이메일 중복")
    void joinFailByEmailUnitTest() throws CustomException {
        //given
        final User user = User.builder()
                .userEmail("lizzy@plgrim.com")
                .build();

        given(userRepository.findByUserEmail("lizzy@plgrim.com")).willReturn(Optional.of(user))
                .willThrow(new CustomException(DUPLICATE_EMAIL));
        //when
        //then
        assertThrows(CustomException.class, () -> userService.join(command));
    }

    @Test
    @DisplayName("회원가입_성공_전화번호 중복")
    void joinFailByPhoneNumberUnitTest() throws CustomException {
        //given
        final User user = User.builder()
                .userBasicInfo(UserBasicInfo.builder().phoneNumber("010-8710-1086").build())
                .build();

        given(userRepository.findByUserBasicInfoPhoneNumber("010-8710-1086")).willReturn(Optional.of(user))
                .willThrow(new CustomException(DUPLICATE_PHONE_NUM));
        //when
        //then
        assertThrows(CustomException.class, () -> userService.join(command));
    }

}
