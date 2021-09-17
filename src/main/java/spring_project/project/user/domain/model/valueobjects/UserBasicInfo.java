package spring_project.project.user.domain.model.valueobjects;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserBasicInfo {

    @Column(name = "phone_number")
    private String phoneNumber; //전화번호

    @Column(name ="address" )
    private String address; //주소


}
