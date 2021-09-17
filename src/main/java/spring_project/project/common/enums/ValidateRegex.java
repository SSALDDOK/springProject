package spring_project.project.common.enums;

public class ValidateRegex {
    //정규식
    public static final String phoneRegex ="^\\d{3}-\\d{3,4}-\\d{4}$";
    public static final String emailRegex ="^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$";
    public static final String passwordRegex="^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$";
    public static final String birthRegex ="^(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$";
    public static final String nameRegex = "^[가-힣]+$";


    //이름
    public static final String phoneName = "핸드폰번호";
    public static final String passwordName= "비밀번호";
    public static final String birthName= "생년월일";
}
