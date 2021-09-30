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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static spring_project.project.common.enums.ErrorCode.DUPLICATE_PHONE_NUM;
import static spring_project.project.common.enums.ErrorCode.EMPTY_USER;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원수정_서비스테스트")
public class UserServiceModifyTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

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
    @DisplayName("회원수정_성공")
    void modifySuccessUnitTest() {
        //given
        final User user = User.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("liy")
                .password("dd1wdw213")
                .gender("M")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("incheon")
                        .phoneNumber("010-871-1086")
                        .build())
                .birth("19970717")
                .build();

        given(userRepository.save(any())).willReturn(user);
        given(userRepository.findByUserEmail("lizzy@plgrim.com")).willReturn(Optional.of(user));
        given(userRepository.findByUserBasicInfoPhoneNumber("010-8710-1086")).willReturn(Optional.empty());

        //when
        User result = userService.modify(command);

        //then
        assertThat(result.getUserName()).isEqualTo("lizzy");

    }

    @Test
    @DisplayName("회원수정_실패_회원없음")
    void modifyFailByIsNotExistUsersUnitTest() throws CustomException {
        //given
        final User user = User.builder()
                .userEmail("lizzy@plgrim.com")
                .build();

        given(userRepository.findByUserEmail("lizzy@plgrim.com")).willReturn(Optional.empty())
                .willThrow(new CustomException(EMPTY_USER));

        //when
        //then
        assertThrows(CustomException.class,()->userService.modify(command));

    }

    @Test
    @DisplayName("회원수정_실패_기존전화번호존재")
    void modifyFailByExistPhoneNumberUnitTest() throws CustomException{
        //given
        final User user = User.builder()
                .userBasicInfo(UserBasicInfo.builder().phoneNumber("010-8710-1086").build())
                .build();

//        given(userRepository.findByUserEmail("lizzy@"))
        given(userRepository.findByUserBasicInfoPhoneNumber("lizzy@plgrim.com")).willReturn(Optional.of(user))
                .willThrow(new CustomException(DUPLICATE_PHONE_NUM));

        //when
        //then
        assertThrows(CustomException.class,()->userService.modify(command));
    }
}
