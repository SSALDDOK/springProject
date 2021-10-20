package spring_project.project.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring_project.project.user.application.UserService;
import spring_project.project.user.controller.dto.UserLoginDTO;
import spring_project.project.user.controller.dto.mapper.RequestMapper;
import spring_project.project.user.domain.model.commands.UserCommand;

import java.util.Map;


@RestController
@RequestMapping("/login")
@Slf4j
public class UserLoginController {

    //받아온 값을 기능적으로 구현할수 있는 서비스 단으로 넘기기 위해서 사용
    private final UserService userSevice;
    private final RequestMapper mapper;

    @Autowired
    public UserLoginController(UserService userSevice) {

        this.userSevice = userSevice;
        this.mapper = new RequestMapper();
    }

    /**
     * 로그인 구현
     *
     * @param
     * @return
     */

// 로그인
    @PostMapping
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserLoginDTO dto) {

        //로그인시 아이디와 패스워드를 사용해서 command와 매핑시킨다
        UserCommand userCommand = mapper.toCommand(dto);

        //매핑시킨 userCommand를 로그인 서비스 로직 매개변수로 보낸다.
        Map<String, Object> result = userSevice.login(userCommand);

        //성공시 200과 서비스에서 반환 받은 값을 보여준다.
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
