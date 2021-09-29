package spring_project.project.user.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring_project.project.user.domain.model.aggregates.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByUserEmail(String userEmail);

    Optional<User> findByUserBasicInfoPhoneNumber(String phoneNumber);

    Page<User> findAll(Pageable pageable);

    void deleteById(String userEmail);

}
