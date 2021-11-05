package spring_project.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring_project.project.common.auth.CustomAuthenticationEntryPoint;
import spring_project.project.common.auth.filter.JwtAuthenticationFilter;
import spring_project.project.common.auth.provider.JwtTokenProvider;

import static spring_project.project.common.enums.UserUrl.*;

//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity(debug = true)//spring security를 적용
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
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
                        , UsernamePasswordAuthenticationFilter.class);
//                .anyRequest().permitAll();

    }


}
