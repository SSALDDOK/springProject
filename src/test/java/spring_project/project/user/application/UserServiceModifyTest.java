package spring_project.project.user.application;

import org.junit.jupiter.api.BeforeEach;
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
import spring_project.project.user.infrastructure.repository.UserJpaRepository;

import java.util.ArrayList;
import java.util.List;
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
    private UserJpaRepository userRepository;

    @InjectMocks
    private UserService userService;


    UserCommand command = UserCommand.builder()
            .id(1L)
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
        final User testUser = User.builder()
                .id(1L)
                .userEmail("lizy@plgrim.com")
                .userName("liy")
                .password("dd1wdw213")
                .gender("M")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("incheon")
                        .phoneNumber("010-871-1086")
                        .build())
                .birth("19970717")
                .build();


        given(userRepository.save(any())).willReturn(testUser);
        given(userRepository.findById(command.getId())).willReturn(Optional.of(testUser));

        //when
        User result = userService.modify(command);

        //then
        assertThat(result.getId()).isEqualTo(testUser.getId());

    }

    @Test
    @DisplayName("회원수정_실패_회원없음")
    void modifyFailByNoExistUsersUnitTest() throws CustomException {
        //given
        given(userRepository.findById(command.getId())).willReturn(Optional.empty())
                .willThrow(new CustomException(EMPTY_USER));

        //when
        //then
        assertThrows(CustomException.class, () -> userService.modify(command));

    }
/*
    @Test
    @DisplayName("회원수정_실패_중복(이메일or전화번호)")
    void modifyFailByExistedPhoneNumberUnitTest() throws CustomException {
        //given
        final User user = User.builder()
                .userBasicInfo(UserBasicInfo.builder().phoneNumber("010-8710-1086").build())
                .build();

        given(userRepository.findByUserBasicInfoPhoneNumber("010-8710-1086")).willReturn(Optional.of(user))
                .willThrow(new CustomException(DUPLICATE_PHONE_NUM));

        //when
        //then
        assertThrows(CustomException.class, () -> userService.modify(command));
    }
    */
}
