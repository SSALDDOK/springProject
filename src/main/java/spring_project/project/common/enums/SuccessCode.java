package spring_project.project.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.OK;


@Getter
@AllArgsConstructor
public enum SuccessCode {
    //로그인 성공시
    LOGIN_SUCCESS(OK, "로그인 성공");

    private final HttpStatus httpStatus;
    private final String detail;
}
