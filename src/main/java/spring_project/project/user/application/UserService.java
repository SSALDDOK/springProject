package spring_project.project.user.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.commands.UserCommand;
import spring_project.project.user.infrastructure.repository.UserJpaRepository;

import static spring_project.project.common.enums.ErrorCode.DUPLICATE_EMAIL;
import static spring_project.project.common.enums.ErrorCode.DUPLICATE_PHONE_NUM;


@Slf4j
@Service
public class UserService {

    /**
     * 회원가입 save
     * 회원수정 modify
     * 회원탈퇴 delete
     * 회원조회 list
     */

    private final UserJpaRepository userRepository;

    public UserService(UserJpaRepository userRepository) {

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
                .createAt(command.getCreateAt())
                .build();


        validateDuplicateUser(user);
        userRepository.save(user);
        return user;
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

  /*
    *//**
     * 회원 수정
     *
     * @Param UserUpdateDto
     *//*
    public User modify(UserModifyReqDTO dto) {


        Optional<User> findOne = userRepository.findByUserEmail(dto.getUserEmail());
        User user = User.builder()
                .userEmail(dto.getUserEmail())
                .userName(dto.getUserName())
                .gender(dto.getGender())
                .password(dto.getPassword())
                .userBasicInfo(dto.getUserBasicInfo())
                .createAt(dto.getCreateAt())
                .updateAt(LocalDateTime.now())
                .build();

        if (findOne.isEmpty()) {
            throw new IllegalStateException("없는 회원입니다");
        } else {
            userRepository.save(user);
        }

        return user;

    }


    *//**
     * 회원 탈퇴
     *
     * @Param UserDeleteDto
     *//*

    public void delete(UserDeleteReqDTO dto) {

        User user = User.builder()
                .userEmail(dto.getUserEmail())
                .build();

        userRepository.deleteById(user.getUserEmail());
    }

    *//**
     * 회원 목록 조회
     *
     * @Param page, pageCount
     *//*
    public Page<User> list(int page, int pageCount) {

        return userRepository.findAll(PageRequest.of(page, pageCount));
    }
*/
}
