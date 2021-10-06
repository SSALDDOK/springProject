package spring_project.project.user.application;


import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.commands.UserCommand;
import spring_project.project.user.domain.service.UserRepository;
import spring_project.project.user.infrastructure.repository.UserJpaRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static spring_project.project.common.enums.ErrorCode.EMPTY_DELETE_USER;

@Disabled
@ExtendWith(MockitoExtension.class)
@DisplayName("회원삭제_단위테스트")
public class UserServiceDeletTest {

    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private UserService userService;

    final UserCommand command = UserCommand.builder()
            .userEmail("lizzy@plgrim.com")
            .build();

    final User user = User.builder()
            .userEmail("lizzy@plgrim.com")
            .build();
/*

    @Test
    @DisplayName("회원탈퇴_성공")
    void deleteSuccessUnitTest() {
        //given
        given(userRepository.findByUserEmail("lizzy@plgrim.com")).willReturn(Optional.of(user));

        //when
        userService.delete(command);

        //then
        //해당 메소드가 times만큼 실행 됬는 지 검증
        verify(userRepository, times(1)).deleteById(user.getUserEmail());
    }

    @Test
    @DisplayName("회원탈퇴_실패_회원없음")
    void deleteFailByNoExistUsersUnitTest() throws CustomException {
        //given
        given(userRepository.findByUserEmail("lizzy@plgrim.com")).willReturn(Optional.empty())
                .willThrow(new CustomException(EMPTY_DELETE_USER));

        //when
        //then
        assertThrows(CustomException.class,()-> userService.delete(command));
    }
*/

}
