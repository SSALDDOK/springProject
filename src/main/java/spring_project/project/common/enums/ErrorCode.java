package spring_project.project.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;


@Getter
@AllArgsConstructor
public enum ErrorCode {

    DUPLICATE_EMAIL(CONFLICT,"이미 존재하는 이메일 입니다." ),
    DUPLICATE_PHONE_NUM(CONFLICT,"이미 존재하는 전화번호 입니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
