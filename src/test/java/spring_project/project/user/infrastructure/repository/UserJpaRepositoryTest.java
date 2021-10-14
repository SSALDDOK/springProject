package spring_project.project.user.infrastructure.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserJpaRepositoryTest {

    @Autowired
    UserJpaRepository userRepository;

    User user;
    User user1;
    User user2;

    @BeforeEach
    public void setUp() {
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
        System.out.println(result);
        assertThat(result).usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    @DisplayName("회원 이메일, 전화번호 조회")
    void findByUserEmailAndUserPhoneNumber() {
        //given
        userRepository.save(user);
        List<User> testUSer = new ArrayList<>();
        testUSer.add(user);

        //when
        List<User> result = userRepository.findOneByUserEmailOrUserBasicInfoPhoneNumber(user.getUserEmail(), user.getUserBasicInfo().getPhoneNumber());
        System.out.println("result= " + result);
        System.out.println("user= " + user);

        //then
        //이게 맞는건가..?
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

   /* @Test
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
    }*/

}