package spring_project.project.user.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import java.util.Optional;
import java.util.function.BooleanSupplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User user;
    User user1;
    User user2;

    Pageable pageble;

    @BeforeEach
    void setUp() {
        pageble = PageRequest.of(0, 1);
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
    @DisplayName("가입성공")
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
        User findOneByEmail = userRepository.findByUserEmail(user.getUserEmail()).get();

        //then
        assertThat(findOneByEmail).usingRecursiveComparison()
                .ignoringFields("createAt", "updateAt")
                .isEqualTo(user);
    }

    @Test
    @DisplayName("회원 전화번호 조회")
    void findByUserBasicInfoPhoneNumber() {
        //given
        userRepository.save(user);

        //when
        User findOneByPhoneNum = userRepository.findByUserBasicInfoPhoneNumber(user.getUserBasicInfo().getPhoneNumber()).get();

        //then
        assertThat(findOneByPhoneNum).usingRecursiveComparison()
                .ignoringFields("createAt", "updateAt")
                .isEqualTo(user);
    }

    @Test
    @DisplayName("회원목록조회")
    void PageFindAll() {
        //given
        userRepository.save(user);
        userRepository.save(user1);
        userRepository.save(user2);
        Pageable pageble =  PageRequest.of(0, 1);

        //when
        userRepository.findAll(pageble);

        //
    }

    @Test
    @DisplayName("삭제 후 회원목록 조회")
    void deleteById() {
    }
}