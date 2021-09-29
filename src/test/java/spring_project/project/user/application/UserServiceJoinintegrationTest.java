package spring_project.project.user.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.commands.UserCommand;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;
import spring_project.project.user.domain.service.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("회원가입서비스테스트")
@Transactional
@SpringBootTest
class UserServiceJoinintegrationTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    UserBasicInfo userBasicInfo = UserBasicInfo.builder()
            .address("incheon")
            .phoneNumber("010-8710-1086")
            .build();

    UserCommand command = UserCommand.builder()
            .userEmail("lizzy@plgrim.com")
            .userName("lizzy")
            .password("jqijfe123")
            .gender("F")
            .userBasicInfo(userBasicInfo)
            .birth("19970717")
            .build();

    @Test
    @DisplayName("회원가입성공")
    void joinSuccess() {
        //given
        UserBasicInfo testUserBasciInfo = UserBasicInfo.builder()
                .address("incheon")
                .phoneNumber("010-8710-1086")
                .build();

        UserCommand testCommand = UserCommand.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("lizzy")
                .password("jqijfe123")
                .gender("F")
                .userBasicInfo(testUserBasciInfo)
                .birth("19970717")
                .build();

        //when
        User result = userService.join(command);

        //then
        assertThat(result).usingRecursiveComparison().ignoringFields("createAt", "updateAt").isEqualTo(testCommand);
    }

    @Test
    @DisplayName("회원가입실패_이메일 중복")
    void joinFailByEmail() {
        //given
        UserCommand testEmail = UserCommand.builder().userEmail("lizzy@plgrim.com").build();

        //when
        userService.join(command);

        //then
        assertThrows(CustomException.class, () -> userService.join(testEmail));

    }

    @Test
    @DisplayName("회원가입성공_전화번호 중복")
    void joinFailByPhoneNumber() {
        //given
        UserBasicInfo phoneNumber = UserBasicInfo.builder().phoneNumber("010-8710-1086").build();
        UserCommand testPhoneNumber = UserCommand.builder().userBasicInfo(phoneNumber).build();

        //when
        userService.join(command);

        //then
        assertThrows(CustomException.class, () -> userService.join(testPhoneNumber));
    }
}