package spring_project.project.common.auth.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import spring_project.project.common.enums.SnsType;
import spring_project.project.user.controller.dto.OauthToken;
import spring_project.project.user.domain.service.Strategy;
import spring_project.project.user.domain.service.StrategyFactory;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
public class SnsTokenProvider {

    private String SECRET_KEY;
    private final long tokenValid;

    private final UserDetailsService userDetailsService;
    private final StrategyFactory strategyFactory;

    public SnsTokenProvider(UserDetailsService userDetailsService, StrategyFactory strategyFactory) {
        this.SECRET_KEY = "LIZZY";
        this.tokenValid = 60 * 60 * 1000L;
        this.userDetailsService = userDetailsService;
        this.strategyFactory = strategyFactory;
    }

    @PostConstruct
    protected void init() {
        SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
    }

    // Jwt 토큰 생성

    public ResponseEntity<OauthToken> createToken(String snsType ,String code) throws Exception {

        SnsType snsTypeName = SnsType.valueOf(snsType.toUpperCase());

        Strategy strategy = strategyFactory.findStrategy(snsTypeName);

        return strategy.sendCallbackUrlCode(code);

    }

//    // 토큰에서 값 추출
//    public Long getSubject(String token) {
//        return Long.valueOf(Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody()
//                .getSubject());
//    }


    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    /**
     * Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt토큰"
     */
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

    public String resolveType(HttpServletRequest request) {
        return request.getHeader("SNS-TYPE");
    }

    /**
     * 토큰의 유효성 + 만료일자 확인
     */
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    // 유효한 토큰인지 확인
//    public boolean validateToken(String token) {
//        Jws<Claims> claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
//        return !claims.getBody().getExpiration().before(new Date());
//    }


}
