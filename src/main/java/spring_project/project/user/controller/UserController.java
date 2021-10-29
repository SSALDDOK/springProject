package spring_project.project.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring_project.project.user.application.UserService;
import spring_project.project.user.controller.dto.UserJoinReqDTO;
import spring_project.project.user.controller.dto.UserModifyReqDTO;
import spring_project.project.user.controller.dto.mapper.RequestMapper;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.domain.model.commands.UserCommand;

import java.util.List;

import static spring_project.project.common.enums.UserUrl.*;

@RestController
@RequestMapping(USER_ROOT_PATH)
@Slf4j
public class UserController {

    /**
     * 로그인 POST Login("/login")
     * 회원가입 POST Join("/users/user")
     * 회원수정 PUT("/users/user")
     * 회원탈퇴 DELETE("/users/{userId}")
     * 회원목록조회 GET ("/list")
     *
     * 숙제!!!! url이름 변경
     */

    //받아온 값을 기능적으로 구현할수 있는 서비스 단으로 넘기기 위해서 사용
    private final UserService userService;
    //받아온 dto의 값을 하나의 객체로 매핑 시켜주는 역할
    private final RequestMapper requestMapper;

    @Autowired
    public UserController(UserService userService) {

        this.userService = userService;
        this.requestMapper = new RequestMapper();
    }
/*

    */
/**
     * 로그인 구현
      * @param dto
     * @return
     *//*


    @GetMapping("/login")
    public ResponseEntity<MyUserDetail> login(@RequestBody UserLoginDTO dto) {
        //Authentication 객체를 통해 유저 정보를 가져올 수 있다.
        MyUserDetail user = userService.loadUserByUsername(dto.getUserEmail());

        log.info("userDetails ="+   user.getAuthorities());


          //userDetail 객체를 가져옴
        return new ResponseEntity<>(user,HttpStatus.OK);
    }
*/

    /**
     * 회원 가입
     *
     * @Param UserJoinReqDTO
     */
    @PostMapping(USER_NEW)
    public ResponseEntity<User> join(@RequestBody @Validated UserJoinReqDTO dto) {

        //1. dto Mapper 객체매핑 ->builder 시켜서 entity형식으로 교체
        UserCommand userCommand = requestMapper.toCommand(dto);

        //2.Mapper -> Service 교체된 엔티티 형식을 서비스 단으로 보내줌
        User user = userService.join(userCommand);

        //3.Service에서 받아온 User객체 반환받기
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * 회원 수정
     *
     * @Param UserModifyReqDTO
     */
    @PutMapping
    public ResponseEntity<User> modify(@RequestBody @Validated UserModifyReqDTO dto) {
        //1. dto Mapper 객체매핑 ->builder 시켜서 entity형식으로 교체
        UserCommand userCommand = requestMapper.toCommand(dto);

        //2.Mapper -> Service 교체된 엔티티 형식을 서비스 단으로 보내줌
        User user = userService.modify(userCommand);

        //3.Service 성공/실패 확인
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * 회원 탈퇴
     * 숙제!!!! uri 고치기
     *
     * @Param PathVariable userId
     */

    @DeleteMapping(USER_ID)
    public ResponseEntity<Object> delete(@PathVariable Long userId) {

        log.info("userId = {}", userId);
        //1.파라미터로 받은 id를 서비스로 바로 넘겨줌
        userService.delete(userId);

        //2.Service 성공/실패 확인
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 회원 목록 조회
     * //0으로 들어온다는 가정.
     * @Param page, pageCount
     */

    @GetMapping
    public ResponseEntity<Object> list(@RequestParam int page, @RequestParam int pageCount) {
        log.info("request param ={}", page);

        //1. 경로변수로 받은 페이지,페이지 수 -> service
        Page<User> list = userService.list(page, pageCount);

        //2. 페이징 처리되어 가져온 정보에 맞춰 users 조회
        List<User> users = list.getContent();

        //service 성공/실패 확인
        return new ResponseEntity<>(users, HttpStatus.OK);

    }
}
