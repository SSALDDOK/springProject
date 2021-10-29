package spring_project.project.user.application;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.infrastructure.repository.UserJpaRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static spring_project.project.common.enums.ErrorCode.EMPTY_DELETE_USER;


@ExtendWith(MockitoExtension.class)
@DisplayName("회원삭제_단위테스트")
public class UserServiceDeleteTest {

    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private UserService userService;

    static Long userId  = 1L;

    static User user;

    @BeforeAll
    static void setUp() {

        user = User.builder()
                .id(userId)
                .build();
    }

    @Test
    @DisplayName("회원탈퇴_성공")
    void deleteSuccessUnitTest() {
        //given
        given(userRepository.findById(any())).willReturn(Optional.of(user));

        //when
        userService.delete(userId);

        //then
        //해당 메소드가 times만큼 실행 됬는 지 검증
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    @DisplayName("회원탈퇴_실패_회원없음")
    void deleteFailByNoExistUsersUnitTest() throws CustomException {
        //given
        given(userRepository.findById(user.getId())).willReturn(Optional.empty());

        //when
        CustomException exception = assertThrows(CustomException.class, () -> userService.delete(userId));

        //then
        assertThat(exception.getErrorCode()).isEqualTo(EMPTY_DELETE_USER);
    }


}
