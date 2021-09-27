package spring_project.project.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring_project.project.user.controller.dto.UserDeleteReqDTO;
import spring_project.project.user.controller.dto.UserJoinReqDTO;
import spring_project.project.user.controller.dto.UserModifyReqDTO;
import spring_project.project.user.controller.dto.mapper.RequestMapper;
import spring_project.project.user.domain.model.aggregates.User;
import spring_project.project.user.application.UserService;
import spring_project.project.user.domain.model.commands.UserCommand;

import java.util.List;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    /**
     * 로그인 POST Login("/login")
     * 회원가입 POST Join("/join")
     * 회원수정 PUT("/modify")
     * 회원탈퇴 DELETE("/delete")
     * 회원목록조회 GET ("/list")
     */

    private final UserService userService;
    private final RequestMapper requestMapper;

    @Autowired
    public UserController(UserService userService , RequestMapper requestMapper) {

        this.userService = userService;
        this.requestMapper = requestMapper;
    }

    /**
     * 회원 가입
     */
    @PostMapping("/join")
    public ResponseEntity<User> joinReq(@RequestBody @Validated UserJoinReqDTO dto) {

        //1. dto Mapper 객체매핑 ->builder 시켜서 entity형식으로 교체
        UserCommand userCommand = requestMapper.toCommand(dto);

        //2.Mapper -> Service 교체된 엔티티 형식을 서비스 단으로 보내줌
        User user = userService.join(userCommand);

        //3.Service에서 받아온 User객체 반환받기
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

   /**
    *회원 수정
    */
@PutMapping("/modify")
public ResponseEntity<User> modify(@RequestBody UserModifyReqDTO dto) {
    //1. dto Mapper 객체매핑 ->builder 시켜서 entity형식으로 교체
    UserCommand userCommand = requestMapper.toCommand(dto);

    //2.Mapper -> Service 교체된 엔티티 형식을 서비스 단으로 보내줌
    User user = userService.modify(userCommand);

    //3.Service 성공/실패 확인
    return new ResponseEntity<>(user, HttpStatus.OK);
}

    /**
     *회원 탈퇴
     */
    @DeleteMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody UserDeleteReqDTO dto) {
        //1. dto Mapper 객체매핑 ->builder 시켜서 entity형식으로 교체
        UserCommand userCommand = requestMapper.toCommand(dto);

        //2.Mapper -> Service 교체된 엔티티 형식을 서비스 단으로 보내줌
        List<User> users = userService.delete(userCommand);

        //3.Service 성공/실패 확인
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    /**
     * 회원 목록 조회
     *//*
    @GetMapping("/users/{page}/{pageCount}")
    public ResponseEntity<Object> list(@PathVariable int page, @PathVariable int pageCount) {

        Page<User> list = userService.list(page, pageCount);
        List<User> users = list.getContent();
        log.info("list ={}", list);
        return new ResponseEntity<>(users, HttpStatus.OK);

    }
    */
}
