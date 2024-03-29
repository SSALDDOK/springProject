package spring_project.project.common.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponse {

    private final int status;
    private final String error;
    private final String code;
    private final String message;

}
