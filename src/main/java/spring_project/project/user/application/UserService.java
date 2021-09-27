package spring_project.project.user.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.commands.UserCommand;
import spring_project.project.user.domain.service.UserRepository;

import java.util.List;
import java.util.Optional;

import static spring_project.project.common.enums.ErrorCode.*;


@Slf4j
@Service
public class UserService {

    /**
     * 회원가입 save
     * 회원수정 modify
     * 회원탈퇴 delete
     * 회원조회 list
     */

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    /**
     * 회원가입
     *
     * @Param UserJoinDto
     * return responseEntity<>
     */
    public User join(UserCommand command) {

        User user = User.builder()
                .userEmail(command.getUserEmail())
                .userName(command.getUserName())
                .password(command.getPassword())
                .birth(command.getBirth())
                .gender(command.getGender())
                .userBasicInfo(command.getUserBasicInfo())
                .build();


        validateDuplicateUser(user);
        return userRepository.save(user);
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByUserEmail(user.getUserEmail())
                .ifPresent(m ->
                {
                    throw new CustomException(DUPLICATE_EMAIL);
                });

        userRepository.findByUserBasicInfoPhoneNumber(user.getUserBasicInfo().getPhoneNumber())
                .ifPresent(m ->
                {
                    throw new CustomException(DUPLICATE_PHONE_NUM);
                });
    }


/**
 *회원 수정
 *@Param UserUpdateDto
 */
  public User modify(UserCommand command) {

      Optional<User> findOne = userRepository.findByUserEmail(command.getUserEmail());

      if (findOne.isEmpty()) {
          throw new CustomException(EMPTY_USER);
      }

      boolean findOnePhoneNum = findOne.get().getUserBasicInfo().getPhoneNumber()
              .equals(command.getUserBasicInfo().getPhoneNumber());

      if (findOnePhoneNum) {
          throw new CustomException(DUPLICATE_PHONE_NUM);
      }

      User user = User.builder()
              .userEmail(command.getUserEmail())
              .userName(command.getUserName())
              .gender(command.getGender())
              .birth(command.getBirth())
              .password(command.getPassword())
              .userBasicInfo(command.getUserBasicInfo())
              .build();

      userRepository.save(user);
      return user;

  }


    /**
     *회원 탈퇴
     *@Param UserDeleteDto
     */

    public List<User> delete(UserCommand command) {

        Optional<User> findOne = userRepository.findByUserEmail(command.getUserEmail());

        if(findOne.isEmpty()){
            throw new CustomException(EMPTY_USER);
        }

        User user = User.builder()
                .userEmail(command.getUserEmail())
                .build();

        userRepository.deleteById(user.getUserEmail());

        return userRepository.findAll();

    }

    /**
     * 회원 목록 조회
     *
     * @Param page, pageCount
     *//*
    public Page<User> list(int page, int pageCount) {

        return userRepository.findAll(PageRequest.of(page, pageCount));
    }
*/
}
