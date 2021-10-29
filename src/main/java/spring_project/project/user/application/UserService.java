package spring_project.project.user.application;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import spring_project.project.common.auth.provider.JwtTokenProvider;
import spring_project.project.common.auth.provider.SnsTokenProvider;
import spring_project.project.common.enums.SnsType;
import spring_project.project.common.exception.CustomException;
import spring_project.project.user.controller.dto.OauthToken;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.commands.UserCommand;
import spring_project.project.user.domain.service.Strategy;
import spring_project.project.user.domain.service.StrategyFactory;
import spring_project.project.user.infrastructure.repository.UserJpaRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static spring_project.project.common.enums.ErrorCode.*;


@Slf4j
@Service
public class UserService {

    /**
     * 회원가입 join
     * 회원수정 modify
     * 회원탈퇴 delete
     * 회원조회 list
     */

    //JPARepository로 DB에 저장
    private final UserJpaRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final SnsTokenProvider snsTokenProvider;
    private final PasswordEncoder encoder;
    private final StrategyFactory strategyFactory;


    public UserService(UserJpaRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder encoder
            , SnsTokenProvider snsTokenProvider, StrategyFactory strategyFactory) {

        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.encoder = encoder;
        this.snsTokenProvider = snsTokenProvider;
        this.strategyFactory = strategyFactory;
    }


    /**
     * 로그인 확인 서비스
     *
     * @param command
     * @return
     */

    public String localLogin(UserCommand command) {
        User user = User.builder()
                .userEmail(command.getUserEmail())
                .password(command.getPassword())
                .build();

        User findOne = userRepository.findByUserEmail(user.getUserEmail())
                .orElseThrow(() -> new CustomException(EMPTY_USER_EMAIL));

        if (!encoder.matches(user.getPassword(), findOne.getPassword())) {
            throw new CustomException(NOT_MATCHES_PASSWORD);
        }

        return jwtTokenProvider.createToken(findOne.getUsername(), findOne.getRoles());

    }

    public Strategy snsLogin(String snsType) {
        SnsType snsTypeName = SnsType.valueOf(snsType.toUpperCase());

        return strategyFactory.findStrategy(snsTypeName);

    }

    public ResponseEntity<OauthToken> oauthLogin(String snsType, String code) throws Exception {
        //토큰만들기
//        ResponseEntity<OauthToken> accessTokenResponse = oauthService.requestAccessToken(code);
//        snsTokenProvider.createToken(code);

        //만들어진 토큰 가져오기
//        OauthToken oauthToken = oauthService.getAccessToken(accessTokenResponse);

        /*//만들어진 토큰을 이용해서 유저 조회하기
        ResponseEntity<String> userInfoResponse = oauthService.createGetRequest(oauthToken);

        GoogleUser googleUser = oauthService.getUserInfo(userInfoResponse);

        User findOne = userRepository.findByUserEmail(googleUser.getUserEmail())
                .orElseThrow(() -> new CustomException(EMPTY_USER_EMAIL));*/

        return snsTokenProvider.createToken(snsType, code);
    }

    /**
     * 회원가입
     *
     * @Param UserCommand
     */
    public User join(UserCommand command) {

        //매핑된 command를 엔티티로 매핑
        User user = User.builder()
                .userEmail(command.getUserEmail())
                .userName(command.getUserName())
                .password(encoder.encode(command.getPassword()))
                .birth(command.getBirth())
                .gender(command.getGender())
                .userBasicInfo(command.getUserBasicInfo())
                .roles(Collections.singletonList("ROLE_USER"))
                .build();


        //기존 유저 이메일, 전화번호 있는지 확인하는 유효성 검사 메서드
        validateDuplicatedJoinUser(user);

        //중복체크 후 repository에 저장
        return userRepository.save(user);

    }

    /**
     * 회원가입 중복체크
     */
    private void validateDuplicatedJoinUser(User user) throws CustomException {

        //기존 DB에 해당 이메일,전화번호가 있는지 중복체크
        List<User> validateUser = userRepository.findByUserEmailOrUserBasicInfoPhoneNumber(user.getUserEmail(), user.getUserBasicInfo().getPhoneNumber());

        log.info("validateUser ={}", validateUser);

        /**
         * 숙제!!! - filter 적용
         * */
        //filter로 할수 있는 방법 찾기
//        validateUser.stream().filter(u->u.getUserEmail().equals(user.getUserEmail())).findFirst()
//                .ifPresent(p-> {throw new CustomException(DUPLICATE_EMAIL);});
//
//        validateUser.stream().filter(u->u.getUserBasicInfo().getPhoneNumber().equals(user.getUserBasicInfo().getPhoneNumber())).findFirst()
//                .ifPresent(p-> {throw new CustomException(DUPLICATE_PHONE_NUM);});


        validateUser.forEach(u -> {
            if (u.getUserEmail().equals(user.getUserEmail()))
                throw new CustomException(DUPLICATE_EMAIL);

            else if (u.getUserBasicInfo().getPhoneNumber().equals(user.getUserBasicInfo().getPhoneNumber()))
                throw new CustomException(DUPLICATE_PHONE_NUM);
        });
    }

    /**
     * 회원 수정
     *
     * @Param UserUpdateDto
     */
    public User modify(UserCommand command) throws CustomException {

        User user = User.builder()
                .id(command.getId())
                .userEmail(command.getUserEmail())
                .userName(command.getUserName())
                .gender(command.getGender())
                .birth(command.getBirth())
                .password(encoder.encode(command.getPassword()))
                .userBasicInfo(command.getUserBasicInfo())
                .roles(command.getRoles())
                .build();


        log.info("user = {}", user);

        //DB에 해당 유저가 존재하는 지 확인
        Optional<User> findOne = userRepository.findById(user.getId());

        //수정할 회원이 없을 경우
        if (findOne.isEmpty()) {
            throw new CustomException(EMPTY_USER);
        }

        //기존 유저 이메일, 전화번호 있는지 확인하는 유효성 검사 메서드
        validateDuplicatedModifyUser(user);

        //유효성 검사 통과 시 저장
        return userRepository.save(user);
    }

    private void validateDuplicatedModifyUser(User user) {
        //조회한 값들 List 로 받아오기
        List<User> validateUser = userRepository.findByUserEmailOrUserBasicInfoPhoneNumber(user.getUserEmail(), user.getUserBasicInfo().getPhoneNumber());

        //받아온 list값만큼 반복실행
        validateUser.forEach(u -> {
            if (!u.getId().equals(user.getId()))

                if (u.getUserEmail().equals(user.getUserEmail())) {
                    throw new CustomException(DUPLICATE_EMAIL);

                } else if (u.getUserBasicInfo().getPhoneNumber().equals(user.getUserBasicInfo().getPhoneNumber())) {
                    throw new CustomException(DUPLICATE_PHONE_NUM);
                }
        });

    }

    /**
     * 회원 탈퇴
     *
     * @Param Long
     */

    public void delete(Long id) {
        User userId = User.builder()
                .id(id)
                .build();

        //삭제할 회원 조회
        Optional<User> findOne = userRepository.findById(userId.getId());

        //삭제할 회원이 없을때
        if (findOne.isEmpty()) {
            throw new CustomException(EMPTY_DELETE_USER);
        }

        //삭제로직
        userRepository.deleteById(userId.getId());
    }


    /**
     * 회원 목록 조회
     *
     * @Param int page ,int pageCount
     */
    public Page<User> list(int page, int pageCount) {

        //페이징 처리에 맞게 반환
        return userRepository.findAll(PageRequest.of(page, pageCount));
    }

}
