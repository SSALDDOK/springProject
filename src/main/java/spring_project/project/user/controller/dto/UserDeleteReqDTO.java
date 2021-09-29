package spring_project.project.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import spring_project.project.user.controller.validation.ValidationBasicField;

import static spring_project.project.common.enums.ValidateRegex.emailName;
import static spring_project.project.common.enums.ValidateRegex.emailRegex;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDeleteReqDTO {

    @ValidationBasicField(name = emailName, regex = emailRegex)
    private String userEmail; //아이디

}