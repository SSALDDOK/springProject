package spring_project.project.user.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.infrastructure.repository.UserJpaRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("토큰인증객체유저_단위테스트")
class CustomUserDetailsServiceTest {

    @Mock
    private UserJpaRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("토큰인증객체_유저확인_성공")
    void loadUserByUsernameSuccessTest() {
        //given
        String testEmail = "lizzy@plgrim.com";

        User testUser = User.builder()
                .userEmail(testEmail)
                .build();

        given(userRepository.findByUserEmail(testEmail)).willReturn(Optional.of(testUser));

        //when
        UserDetails user = customUserDetailsService.loadUserByUsername(testEmail);

        //then
        assertThat(user.getUsername()).isEqualTo(testUser.getUserEmail());
    }

    @Test
    @DisplayName("토큰인증객체_유저확인_실패")
    void loadUserByUsernameFailTest() {

        //given
        String testEmail = "lizzy@plgrim.com";

        given(userRepository.findByUserEmail(testEmail)).willReturn(Optional.empty());

        //when
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,()->customUserDetailsService.loadUserByUsername(testEmail));

        //then
        assertThat(exception.getMessage()).isEqualTo(testEmail);
    }

}