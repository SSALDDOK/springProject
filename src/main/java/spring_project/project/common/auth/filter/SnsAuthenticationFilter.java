package spring_project.project.common.auth.filter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import spring_project.project.common.auth.provider.SnsTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class SnsAuthenticationFilter extends GenericFilterBean {

    private final SnsTokenProvider snsTokenProvider;

    public SnsAuthenticationFilter(SnsTokenProvider snsTokenProvider) {
        this.snsTokenProvider = snsTokenProvider;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // 헤더에서 JWT 를 받아옵니다.
        String snsType = snsTokenProvider.resolveType((HttpServletRequest) request);

        if (snsType != null && snsType.equals("google")) {

            String token = snsTokenProvider.resolveToken((HttpServletRequest) request);

            // 유효한 토큰인지 확인합니다.
            if (token != null && snsTokenProvider.validateToken(token)) {
                // 토큰이 유효하면 토큰으로부터 유저 정보를 받아옵니다.
                Authentication authentication = snsTokenProvider.getAuthentication(token);
                // SecurityContext 에 Authentication 객체를 저장합니다.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);

    }
}
