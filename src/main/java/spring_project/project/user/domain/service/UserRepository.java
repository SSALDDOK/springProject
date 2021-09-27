package spring_project.project.user.domain.service;
import spring_project.project.user.domain.model.aggregates.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);
    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByUserBasicInfoPhoneNumber(String phoneNumber);
    List<User> findAll();
    void deleteById(String userEmail);

}
