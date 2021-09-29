package spring_project.project.user.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.commands.UserCommand;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DisplayName("회원가입서비스테스트")
@Transactional
@SpringBootTest
public class UserServiceModifyTest {

    @Autowired
    UserService userService;

    UserCommand command = UserCommand.builder()
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
    @DisplayName("회원수정 성공")
    void modifySuccess() {
        //given
        UserBasicInfo testUserBasicInfo = UserBasicInfo.builder()
                .address("incheon")
                .phoneNumber("010-871-1086")
                .build();

        UserCommand testCommand = UserCommand.builder()
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

        String existPhoneNumber = command.getUserBasicInfo().getPhoneNumber();
        System.out.println(existPhoneNumber);

        //when
        userService.join(command);
        assertFalse(command.toString().isEmpty());
        assertNotEquals(existPhoneNumber,
                testCommand.getUserBasicInfo().getPhoneNumber());

        User result = userService.modify(testCommand);

        //then
        assertThat(result.getUserName()).isEqualTo("liy");
    }

    @Test
    @DisplayName("회원수정 실패_수정할 회원 없음")
    void modifyFailByIsNotExistUsers() {

    }

    @Test
    @DisplayName("회원수정 실패_기존 전화번호 존재")
    void modifyFailByExistPhoneNumber() {

    }
}
