package spring_project.project.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;


@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_EMAIL(CONFLICT, "이미 존재하는 이메일 입니다."),
    DUPLICATE_PHONE_NUM(CONFLICT, "이미 존재하는 전화번호 입니다."),
    DUPLICATE_USER(CONFLICT, "이미 존재 회원입니다."),
    EMPTY_USER(BAD_REQUEST, "존재하지 않는 회원입니다."),
    EMPTY_DELETE_USER(BAD_REQUEST, "삭제할 회원이 없습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
