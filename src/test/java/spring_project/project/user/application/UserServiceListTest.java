package spring_project.project.user.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;
import spring_project.project.user.infrastructure.repository.UserJpaRepository;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원목록조회_단위테스트")
public class UserServiceListTest {

    @Mock
    UserJpaRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    @DisplayName("회원목록조회_성공")
    void listSuccessUnitTest() {
        //given
        User user = User.builder()
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

        User user1 = User.builder()
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

        Pageable pageable = PageRequest.of(0, 2);
        List<User> userList = Arrays.asList(user, user1);

        given(userRepository.findAll(pageable)).willReturn(new PageImpl<>(userList));

        //when
        Page<User> pageList = userService.list(0, 2);

        //then
        assertThat(pageList.getContent()).isEqualTo(userList);
    }
}
