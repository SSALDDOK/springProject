package spring_project.project.user.domain.model.valueobjects;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Getter
public class BaseTime {

    @CreatedDate
    @Column(name = "createat" ,updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "updateat")
    private LocalDateTime updateAt;



}
