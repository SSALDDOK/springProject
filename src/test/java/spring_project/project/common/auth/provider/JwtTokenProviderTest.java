/*
package spring_project.project.common.auth.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import spring_project.project.common.auth.provider.JwtTokenProvider;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import java.util.Base64;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@DisplayName("jwt Provider 테스트")
class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserDetailsService userDetailsService;

    private UserDetails userDetails;

    private String SECRET_KEY;

    private User user;

    @BeforeEach
    void setUp() {
        SECRET_KEY = Base64.getEncoder().encodeToString("LIZZY".getBytes());
        user = User.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("lizzy")
                .gender("F")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("incheon")
                        .phoneNumber("010-8710-1086")
                        .build())
                .birth("19970717")
                .roles(Collections.singletonList("ROLE_USER"))
                .build();

     userDetails = user;

    }


    @Test
    @DisplayName("jwt 토큰 생성 성공")
    void jwtCreateTokenSuccessTest() {
        Claims claims = Jwts.claims().setSubject("lizzy@plgrim.com");
        claims.put("roles", Collections.singletonList("ROLE_USER"));
        Date now = new Date();
        // 1시간만 토큰 유효
        long tokenValid = 60 * 60 * 1000L;

        String jwt = Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValid)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 암호화 알고리즘, secret값 세팅
                .compact();

        String result = jwtTokenProvider.createToken(user.getUserEmail(), user.getRoles());

        assertThat(result).isEqualTo(jwt);
    }

    @Test
    @DisplayName("jwt 토큰에서 회원 정보 추출")
    void jwtGetUserPkTest() {
        //given
        Claims claims = Jwts.claims().setSubject("lizzy@plgrim.com");
        claims.put("roles", Collections.singletonList("ROLE_USER"));
        Date now = new Date();
        // 1시간만 토큰 유효
        long tokenValid = 60 * 60 * 1000L;

        String jwt = Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValid)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 암호화 알고리즘, secret값 세팅
                .compact();

        //토큰에서 회원 정보 추출
        //when
        String result = jwtTokenProvider.getUserPk(jwt);

        //then
        assertThat(result).isEqualTo("lizzy@plgrim.com");
    }

    @Test
    @DisplayName("jwt 토큰에서 추출한 회원 인증 정보 조회")
    void jwtGetAuthenticationTest() {
        //given
        Claims claims = Jwts.claims().setSubject("lizzy@plgrim.com");
        claims.put("roles", Collections.singletonList("ROLE_USER"));
        Date now = new Date();
        // 1시간만 토큰 유효
        long tokenValid = 60 * 60 * 1000L;

        String jwt = Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(new Date(now.getTime() + tokenValid)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 암호화 알고리즘, secret값 세팅
                .compact();

        UserDetails userDetails = userDetailsService.loadUserByUsername("lizzy@plgrim.com");

        Authentication test = new UsernamePasswordAuthenticationToken(userDetails,"",userDetails.getAuthorities());

        //when
        Authentication result = jwtTokenProvider.getAuthentication(jwt);

        //then
        assertThat(test.getPrincipal()).usingRecursiveComparison()
                .ignoringFields("createAt","updateAt")
                .isEqualTo(result.getPrincipal());
    }

    @Test
    @DisplayName("jwt 토큰 헤더 파싱")
    void jwtResolveTokenTest() {

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-AUTH-TOKEN","token");

        String result = jwtTokenProvider.resolveToken(request);

        assertThat(result).isEqualTo("token");
    }

    @Test
    @DisplayName("jwt 토큰 유효성, 만료일자 확인")
    void jwtValidateTokenTest() {

    }



}*/
