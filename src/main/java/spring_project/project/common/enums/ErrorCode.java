package spring_project.project.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    //409
    DUPLICATE_EMAIL(CONFLICT, "이미 존재하는 이메일 입니다."),
    DUPLICATE_PHONE_NUM(CONFLICT, "이미 존재하는 전화번호 입니다."),
    DUPLICATE_USER(CONFLICT, "이미 존재 회원입니다."),

    // 500에러로 고치기
    EMPTY_USER(INTERNAL_SERVER_ERROR, "존재하지 않는 회원입니다."),
    EMPTY_DELETE_USER(INTERNAL_SERVER_ERROR, "삭제할 회원이 없습니다."),
    NOT_MATCHES_PASSWORD(INTERNAL_SERVER_ERROR, "잘못된 비밀번호 입니다."),
    EMPTY_USER_EMAIL(INTERNAL_SERVER_ERROR, "존재하지 않는 이메일 입니다."),

    //401
    NOT_UNAUTHORIZED(UNAUTHORIZED, "인증되지 않은 유저입니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
