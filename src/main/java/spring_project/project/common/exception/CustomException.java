package spring_project.project.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import spring_project.project.common.enums.ErrorCode;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    private final ErrorCode errorCode;

}
