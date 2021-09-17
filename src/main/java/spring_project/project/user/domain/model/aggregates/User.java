package spring_project.project.user.domain.model.aggregates;

import lombok.*;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {

    @Id
    @Column(name = "user_email")
    private String userEmail; //아이디

    @Column(name = "user_name")
    private String userName; //이름

    @Column(name="password")
    private String password; //비밀번호

    @Embedded
    private UserBasicInfo userBasicInfo;

    @Column(name = "gender")
    private String gender; //성별

    @Column(name = "birth")
    private String birth; //생년월일

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name="update_at")
    private LocalDateTime updateAt;

}
