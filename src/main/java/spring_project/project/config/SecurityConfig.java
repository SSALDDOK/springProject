package spring_project.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring_project.project.common.auth.CustomAuthenticationEntryPoint;
import spring_project.project.common.auth.filter.JwtAuthenticationFilter;
import spring_project.project.common.auth.filter.SnsAuthenticationFilter;
import spring_project.project.common.auth.provider.JwtTokenProvider;
import spring_project.project.common.auth.provider.SnsTokenProvider;

import static spring_project.project.common.enums.UserUrl.*;

//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity(debug = true)//spring security를 적용
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //UserDetailsService를 상속받는 서비스 주입
//    private final UserLoginSevice userLoginSevice;
    private final JwtTokenProvider jwtTokenProvider;
    private final SnsTokenProvider snsTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider, SnsTokenProvider snsTokenProvider) {
//        this.userLoginSevice = userLoginSevice;
        this.jwtTokenProvider = jwtTokenProvider;
        this.snsTokenProvider = snsTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()  // 기본 설정 해제
                .csrf().disable()       //  csrf 보안토큰 처리 해제
                //  세션 사용하지 않음. (토큰 인증 기반)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers(LOGIN_ROOT_PATH, USER_ROOT_PATH + USER_NEW).permitAll()
//                .antMatchers("/users/**").hasRole("USER")
//                .anyRequest().authenticated()
                .anyRequest().hasRole("USER")
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider)
                        , UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new SnsAuthenticationFilter(snsTokenProvider)
                        , UsernamePasswordAuthenticationFilter.class);
//                .anyRequest().permitAll();

    }


//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        //인증받는 서비스 부분
//        auth.userDetailsService(userLoginSevice);
//    }
}
