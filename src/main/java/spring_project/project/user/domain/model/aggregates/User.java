package spring_project.project.user.domain.model.aggregates;

import lombok.*;
import spring_project.project.user.domain.model.valueobjects.BaseTime;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import javax.persistence.*;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User extends BaseTime {

    //id는 회원 번호로 변경
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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


}
