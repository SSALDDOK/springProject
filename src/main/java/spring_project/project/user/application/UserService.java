package spring_project.project.user.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    //JPARepository로 DB에 저장
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    /**
     * 회원가입
     * @Param UserCommand
     */
    public User join(UserCommand command) {

        //매핑된 command를 엔티티로 매핑
        User user = User.builder()
                .userEmail(command.getUserEmail())
                .userName(command.getUserName())
                .password(command.getPassword())
                .birth(command.getBirth())
                .gender(command.getGender())
                .userBasicInfo(command.getUserBasicInfo())
                .build();


        //기존 유저 이메일, 전화번호 있는지 확인하는 메서드
        validateDuplicateUser(user);

        //중복체크 후 repository에 저장
        return userRepository.save(user);
    }

    /** 중복체크 */
    private void validateDuplicateUser(User user) {
        //기존 DB에 해당 이메일이 있는지 중복체크
        userRepository.findByUserEmail(user.getUserEmail())
                .ifPresent(m ->
                {
                    //있다면 CustomException으로 예외처리
                    throw new CustomException(DUPLICATE_EMAIL);
                });

        //기존 DB에 해당 전화번호가 있는지 중복체크
        userRepository.findByUserBasicInfoPhoneNumber(user.getUserBasicInfo().getPhoneNumber())
                .ifPresent(m ->
                {
                    //있다면 CustomException으로 예외처리
                    throw new CustomException(DUPLICATE_PHONE_NUM);
                });
    }


/**
 *회원 수정
 *@Param UserUpdateDto
 */
  public User modify(UserCommand command) {

      //DB에 해당 유저가 존재하는 지 확인
      Optional<User> findOne = userRepository.findByUserEmail(command.getUserEmail());

      //수정할 회원이 없을 경우
      if (findOne.isEmpty()) {
          throw new CustomException(EMPTY_USER);
      }

      //찾은 회원이 등록한 번호와 수정할 번호가 중복될 경우
      boolean findOnePhoneNum = findOne.get().getUserBasicInfo().getPhoneNumber()
              .equals(command.getUserBasicInfo().getPhoneNumber());


      if (findOnePhoneNum) {
          throw new CustomException(DUPLICATE_PHONE_NUM);
      }

      //유효성 검사 통과 시 ㅇㅇ
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
     */
    public Page<User> list(int page, int pageCount) {

        return userRepository.findAll(PageRequest.of(page, pageCount));
    }
}
