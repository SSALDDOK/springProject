package spring_project.project.user.domain.model.valueobjects;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseTime {
    @CreatedDate
    @Column(name = "create_at",updatable = false)
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name="update_at")
    private LocalDateTime updateAt;
}
