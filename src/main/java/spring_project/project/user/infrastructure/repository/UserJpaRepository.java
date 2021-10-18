package spring_project.project.user.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring_project.project.user.domain.model.aggregates.User;

import java.util.List;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    List<User> findByUserEmailOrUserBasicInfoPhoneNumber(String userEmail, String phoneNumber);

}
