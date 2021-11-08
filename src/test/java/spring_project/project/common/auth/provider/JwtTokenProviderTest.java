package spring_project.project.common.auth.provider;

import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.entities.UserRole;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;
import spring_project.project.user.domain.service.CustomUserDetailsService;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("jwt Provider 테스트")
class JwtTokenProviderTest {

    @InjectMocks
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private CustomUserDetailsService userDetailsService;

    private User user;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {

        user = User.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("lizzy")
                .gender("F")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("incheon")
                        .phoneNumber("010-8710-1086")
                        .build())
                .birth("19970717")
                .roles(Collections.singletonList(UserRole.builder()
                        .authority("ROLE_USER")
                        .build()))
                .build();

        userDetails = user;

    }


    @Test
    @DisplayName("jwt 토큰 생성 성공")
    void jwtCreateTokenSuccessTest() {
        //given
        ReflectionTestUtils.setField(jwtTokenProvider, "SECRET_KEY", "LIZZY");

        //when
        String test = jwtTokenProvider.createToken(user.getUserEmail(), user.getRoles());

        String result = Jwts.parser().setSigningKey("LIZZY").parseClaimsJws(test).getBody().getSubject();

        //then
        assertThat(result).isEqualTo(user.getUserEmail());
    }

    @Test
    @DisplayName("jwt 토큰에서 회원 정보 추출")
    void jwtGetUserPkTest() {
        //given
        ReflectionTestUtils.setField(jwtTokenProvider, "SECRET_KEY", "LIZZY");

        String test = jwtTokenProvider.createToken(user.getUserEmail(), user.getRoles());

        //when
        String result = jwtTokenProvider.getUserPk(test);

        //then
        assertThat(result).isEqualTo(user.getUserEmail());
    }

    @Test
    @DisplayName("jwt 토큰에서 추출한 회원 인증 정보 조회")
    void jwtGetAuthenticationTest() {

        //given
        ReflectionTestUtils.setField(jwtTokenProvider, "SECRET_KEY", "LIZZY");

        String test = jwtTokenProvider.createToken(user.getUserEmail(), user.getRoles());

        given(userDetailsService.loadUserByUsername(any())).willReturn(userDetails);

        //when
        Authentication result = jwtTokenProvider.getAuthentication(test);

        //then
        assertThat(result.getPrincipal()).isEqualTo(userDetails);
    }

    @Test
    @DisplayName("jwt 토큰 헤더 파싱")
    void jwtResolveTokenTest() {

        //given
        ReflectionTestUtils.setField(jwtTokenProvider, "SECRET_KEY", "LIZZY");
        String test = jwtTokenProvider.createToken(user.getUserEmail(), user.getRoles());

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-AUTH-TOKEN", test);

        //when
        String result = jwtTokenProvider.resolveToken(request);

        //then
        assertThat(result).isEqualTo(test);
    }

    @Test
    @DisplayName("jwt 토큰 유효성, 만료일자 확인 성공")
    void jwtValidateTokenSuccessTest() {
        //given
        ReflectionTestUtils.setField(jwtTokenProvider, "SECRET_KEY", "LIZZY");
        String test = jwtTokenProvider.createToken(user.getUserEmail(), user.getRoles());

        //when
        boolean result = jwtTokenProvider.validateToken(test);

        //then
        assertTrue(result);
    }

    @Test
    @DisplayName("jwt 토큰 유효성, 만료일자 확인 실패")
    void jwtValidateTokenFailTest() {
        //given
        String test = "fail";

        //when
        boolean result = jwtTokenProvider.validateToken(test);

        //then
        assertFalse(result);
    }


}
