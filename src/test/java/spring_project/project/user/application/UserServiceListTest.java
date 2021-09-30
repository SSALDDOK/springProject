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
import spring_project.project.user.domain.service.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("회원목록조회_단위테스트")
public class UserServiceListTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

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

    final User user1 = User.builder()
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
    @DisplayName("회원목록조회_성공")
    void listSuccessUnitTest() {
        //given
        final Pageable pageable = PageRequest.of(0, 2);
        List<User> userList = Arrays.asList(user,user1);

        given(userRepository.findAll(pageable))
                .willReturn(new PageImpl<>(userList));

        //when
        Page<User> pageList = userService.list(0,2);

        //then
        assertTrue(pageList.getContent().size() <= pageable.getPageSize());
    }
}
