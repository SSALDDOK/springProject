package spring_project.project.user.domain.service;
import spring_project.project.user.domain.model.aggregates.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByUserEmail(String userEmail);
    Optional<User> findByUserBasicInfoPhoneNumber(String phoneNumber);
}
