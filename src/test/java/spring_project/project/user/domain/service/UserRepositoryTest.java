package spring_project.project.user.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;
import spring_project.project.user.infrastructure.repository.UserJpaRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserJpaRepository userRepository;

    User user;
    User user1;
    User user2;

    @BeforeEach
    public void setUp() {
        //User 정보
        UserBasicInfo userBasicInfo = UserBasicInfo.builder()
                .address("incheon")
                .phoneNumber("010-8710-1086")
                .build();

        user = User.builder()
                .userEmail("lizzy@plgrim.com")
                .userName("lizzy")
                .password("jqijfe123")
                .gender("F")
                .userBasicInfo(userBasicInfo)
                .birth("19970717")
                .build();

        //User1 정보
        UserBasicInfo userBasicInfo1 = UserBasicInfo.builder()
                .address("bucheon")
                .phoneNumber("010-870-1086")
                .build();

        user1 = User.builder()
                .userEmail("ezz@plgrim.com")
                .userName("lizy")
                .password("jqife123")
                .gender("F")
                .userBasicInfo(userBasicInfo1)
                .birth("19970717")
                .build();

        //User2 정보
        UserBasicInfo userBasicInfo2 = UserBasicInfo.builder()
                .address("LA")
                .phoneNumber("010-8710-1036")
                .build();

        user2 = User.builder()
                .userEmail("ezz2zze@plgrim.com")
                .userName("izzy")
                .password("qijfe123")
                .gender("F")
                .userBasicInfo(userBasicInfo2)
                .birth("19970717")
                .build();
    }

    @Test
    @DisplayName("회원가입")
    void save() {
        //given
        //when
        User result = userRepository.save(user);

        //then
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("createAt", "updateAt")
                .isEqualTo(user);
    }

    @Test
    @DisplayName("회원 이메일 조회")
    void findByUserEmail() {
        //given
        userRepository.save(user);

        //when
        Optional<User> result = userRepository.findByUserEmail(user.getUserEmail());

        //then
        assertFalse(result.isEmpty());
        assertThat(result.get()).usingRecursiveComparison()
                .ignoringFields("createAt", "updateAt")
                .isEqualTo(user);
    }

    @Test
    @DisplayName("회원 전화번호 조회")
    void findByUserBasicInfoPhoneNumber() {
        //given
        userRepository.save(user);

        //when
        Optional<User> findOneByPhoneNum = userRepository.findByUserBasicInfoPhoneNumber(user.getUserBasicInfo().getPhoneNumber());

        //then
        assertFalse(findOneByPhoneNum.isEmpty());
        assertThat(findOneByPhoneNum.get()).usingRecursiveComparison()
                .ignoringFields("createAt", "updateAt")
                .isEqualTo(user);
    }

    @Test
    @DisplayName("회원목록 조회")
    void PageFindAll() {
        //given
        Pageable pageable;
        pageable = PageRequest.of(0, 4);

        userRepository.save(user);
        userRepository.save(user1);
        userRepository.save(user2);

        //when
        Page<User> list = userRepository.findAll(pageable);
        List<User> result = list.getContent();

        System.out.println("result = " + result);
        //then
        assertTrue(result.size() <= pageable.getPageSize());
    }

    @Test
    @DisplayName("회원목록 삭제")
    void deleteById() {
        //given
        userRepository.save(user);

        //when
        userRepository.deleteById(user.getUserEmail());
        Optional<User> result = userRepository.findByUserEmail(user.getUserEmail());

        //then
        assertTrue(result.isEmpty());
    }
}