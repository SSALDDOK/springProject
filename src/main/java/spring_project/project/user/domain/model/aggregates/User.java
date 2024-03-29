package spring_project.project.user.domain.model.aggregates;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import spring_project.project.user.domain.model.entities.UserRole;
import spring_project.project.user.domain.model.valueobjects.BaseTime;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTime implements UserDetails{

    //id는 회원 번호로 변경
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "useremail")
    private String userEmail; //아이디

    @Column(name = "username")
    private String userName; //이름

    @Column(name = "password")
    private String password; //비밀번호

    @Embedded
    private UserBasicInfo userBasicInfo;

    @Column(name = "gender")
    private String gender; //성별

    @Column(name = "birth")
    private String birth; //생년월일

    //데이터 업데이트 => 연관관계있는 것  => cascade type(DBA 질문)
//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="uid")
    private List<UserRole> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
       return this.roles.stream()
                .map((UserRole role) -> new SimpleGrantedAuthority(role.getAuthority()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return userEmail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
