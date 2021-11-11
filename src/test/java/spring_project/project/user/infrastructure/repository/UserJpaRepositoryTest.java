package spring_project.project.user.infrastructure.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.entities.UserRole;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserJpaRepositoryTest {

    @Autowired
    UserJpaRepository userRepository;

    User user;
    User user1;
    User user2;

    @BeforeEach
    void setUp() {
        UserRole userRole = UserRole.builder()
                .authority("ROLE_USER")
                .build();

        //User 정보
        user = User.builder()
                .userEmail("ezy@plgrim.com")
                .userName("lizzy")
                .password("jqijfe123")
                .gender("F")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("incheon")
                        .phoneNumber("010-8710-1086")
                        .build())
                .roles(Collections.singletonList(userRole))
                .birth("19970717")
                .build();

        //User1 정보

        user1 = User.builder()
                .userEmail("ezz@plgrim.com")
                .userName("lizy")
                .password("jqife123")
                .gender("F")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("bucheon")
                        .phoneNumber("010-870-1086")
                        .build())
                .birth("19970717")
                .build();

        //User2 정보
        user2 = User.builder()
                .userEmail("ezz2zze@plgrim.com")
                .userName("izzy")
                .password("qijfe123")
                .gender("F")
                .userBasicInfo(UserBasicInfo.builder()
                        .address("LA")
                        .phoneNumber("010-8710-1036")
                        .build())
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
                .isEqualTo(user);
    }

    @Test
    @DisplayName("회원 이메일, 전화번호 조회")
    void findByUserEmailAndUserPhoneNumber() {
        //given
//        userRepository.save(user);
        List<User> testUSer = new ArrayList<>();

        //when
        List<User> result = userRepository.findByUserEmailOrUserBasicInfoPhoneNumber(user.getUserEmail(), user.getUserBasicInfo().getPhoneNumber());

        //then
        assertThat(result).isEqualTo(testUSer);

    }

    @Test
    @DisplayName("회원정보 조회")
    void findById() {
        //given
        userRepository.save(user);

        //when
        Optional<User> findOne = userRepository.findById(user.getId());

        //then
        assertFalse(findOne.isEmpty());
        assertThat(findOne.get()).usingRecursiveComparison()
                .isEqualTo(user);

    }

    @Test
    @DisplayName("회원 이메일 조회")
    void findByUserEmail() {
        //given
        userRepository.save(user);

        //when
        Optional<User> findOneByUserEmail = userRepository.findByUserEmail(user.getUserEmail());

        //then
        assertFalse(findOneByUserEmail.isEmpty());
        assertThat(findOneByUserEmail.get()).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    @DisplayName("회원목록 삭제")
    void deleteById() {
        //given
        userRepository.save(user);

        //when
        userRepository.deleteById(user.getId());
        Optional<User> result = userRepository.findById(user.getId());

        //then
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("회원목록 조회")
    void pageFindAll() {
        //given
        Pageable pageable;
        pageable = PageRequest.of(0, 4);

        userRepository.save(user);
        userRepository.save(user1);
        userRepository.save(user2);

        //when
        Page<User> list = userRepository.findAll(pageable);
        List<User> result = list.getContent();

        //then
        assertTrue(result.size() <= pageable.getPageSize());
    }

    @Test
    @DisplayName("회원권한 조회")
    void findRolesById() {
        //given
        userRepository.save(user);

        //when
        Optional<User> result = userRepository.findRolesById(user.getId());

        //then
        assertFalse(result.isEmpty());
        assertThat(result.get()).isEqualTo(user);
    }

}