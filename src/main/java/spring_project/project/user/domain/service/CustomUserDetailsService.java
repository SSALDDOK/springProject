package spring_project.project.user.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import spring_project.project.user.infrastructure.repository.UserJpaRepository;



//Domain service (순환 유무) -> 빼기
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userRepository;
    //jwtTokenProvider에서 UserDetailsService를 주입받고 있기때문에 순환 되므로 나눠줘야한다.

    public CustomUserDetailsService(UserJpaRepository userRepository) {

        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    }
