package spring_project.project.common.enums;

public class UserUrl {
    //user api URL
    public static final String USER_ROOT_PATH = "/users";
    public static final String USER_NEW = "/new";
    public static final String USER_ID = "/{userId}";

    //LOCAL 로그인 URL
    public static final String LOGIN_ROOT_PATH = "/login";

    //SNS 로그인 URL
    public static final String LOGIN_SNS_PATH = "/{snsType}";
    public static final String LOGIN_SNS_CALLBACK_PATH = "/{snsType}/callback";

}
