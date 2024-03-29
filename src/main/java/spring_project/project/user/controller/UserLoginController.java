package spring_project.project.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import spring_project.project.user.application.UserService;
import spring_project.project.user.controller.dto.UserLoginDTO;
import spring_project.project.user.controller.dto.mapper.RequestMapper;
import spring_project.project.user.domain.model.commands.UserCommand;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static spring_project.project.common.enums.UserUrl.*;


@RestController
@RequestMapping(LOGIN_ROOT_PATH)
@Slf4j
public class UserLoginController {

    //받아온 값을 기능적으로 구현할수 있는 서비스 단으로 넘기기 위해서 사용
    private final UserService userService;
    private final RequestMapper mapper;

    @Autowired
    public UserLoginController(UserService userService) {

        this.userService = userService;
        this.mapper = new RequestMapper();
    }

    /**
     * 로컬 로그인 구현
     *
     * @param
     * @return
     */

    // 로컬 로그인시 아이디와 패스워드를 사용해서 command와 매핑시킨후 서비스로 보낸다.
    @PostMapping
    public String login(@RequestBody UserLoginDTO dto) {

        UserCommand userCommand = mapper.toCommand(dto);

        return userService.localLogin(userCommand);
    }

    /**
     * SNS 페이지 호출
     *
     * @param snsType
     * @throws IOException
     */
    @GetMapping(LOGIN_SNS_PATH)
    public void socialLogin(HttpServletResponse response, @PathVariable String snsType) throws IOException {

        //FM : 어->도 (용도에 따라서 나누기)
        String redirectUrl = userService.snsLogin(snsType);

        response.sendRedirect(redirectUrl);
    }

    /**
     * Sns Login API Server 요청에 의한 callback 을 처리
     *
     * @param code API Server 로부터 넘어노는 code
     * @return SNS Login 요청 결과로 받은 Json 형태의 String 문자열 (access_token, refresh_token 등)
     */
    @GetMapping(LOGIN_SNS_CALLBACK_PATH)
    public String socialLoginCallback(@PathVariable String snsType, @RequestParam String code) throws Exception {

        return userService.snsOauthLogin(snsType, code);
//        return new ResponseEntity<>(new OauthTokenResponseDTO(token, "bearer"), HttpStatus.OK);

    }
}
