package spring_project.project.common.auth.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import spring_project.project.common.auth.provider.JwtTokenProvider;
import spring_project.project.user.domain.model.aggregates.User;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("jwt Filter 테스트")
class JwtAuthenticationFilterTest {

    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    UserDetails userDetails;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Test
    @DisplayName("jwt local Filter 검증")
    void jwtAuthenticationFilterSuccessUnitTest() throws IOException, ServletException {
        //given
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-AUTH-TOKEN","token");
        request.addHeader("SYS-TYPE","local");

        given(jwtTokenProvider.resolveType(request)).willReturn("local");
        given(jwtTokenProvider.resolveToken(request)).willReturn("token");
        given(jwtTokenProvider.validateToken("token")).willReturn(true);

        UserDetails user = User.builder()
                .id(1L)
                .userEmail("monty@plgrim.com")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

        given(jwtTokenProvider.getAuthentication(any()))
                .willReturn(new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities()));

        MockHttpServletResponse response = Mockito.mock(MockHttpServletResponse.class);
        MockFilterChain chain = Mockito.mock(MockFilterChain.class);

        assertDoesNotThrow(() -> jwtAuthenticationFilter.doFilter(request,response,chain));

      // assertThrows(RuntimeException.class,()->jwtAuthenticationFilter.doFilter(request,response,chain));

    }

//    @Test
//    @DisplayName("jwt Filter 검증 실패")
//    void jwtAuthenticationFilterFailUnitTest() throws IOException,ServletException {
//        //given
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        System.out.println(request.getHeader("X-AUTH-TOKEN"));
//
//
//        MockHttpServletResponse response = Mockito.mock(MockHttpServletResponse.class);
//        MockFilterChain chain = Mockito.mock(MockFilterChain.class);
////        willReturn(false).given(jwtTokenProvider).validateToken(any());
//        willThrow(new IOException(), new ServletException()).given(chain).doFilter(request, response);
//        //
//        assertThrows(RuntimeException.class, () -> jwtAuthenticationFilter.doFilter(request, response, chain));
////        assertDoesNotThrow(()->jwtAuthenticationFilter.doFilter(request,response,chain));
//    }
}
