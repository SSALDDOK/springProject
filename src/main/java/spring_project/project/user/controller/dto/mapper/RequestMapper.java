package spring_project.project.user.controller.dto.mapper;

import spring_project.project.user.controller.dto.UserJoinReqDTO;
import spring_project.project.user.controller.dto.UserLoginDTO;
import spring_project.project.user.controller.dto.UserModifyReqDTO;
import spring_project.project.user.domain.model.commands.UserCommand;
import spring_project.project.user.domain.model.entities.UserRole;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;

import java.util.Collections;

public class RequestMapper {

    public UserCommand toCommand(UserJoinReqDTO dto) {

        UserBasicInfo userBasicInfo = new UserBasicInfo(dto.getPhoneNumber(), dto.getAddress());

        return UserCommand.builder()
                .userEmail(dto.getUserEmail())
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .userBasicInfo(userBasicInfo)
                .build();

    }

    public UserCommand toCommand(UserModifyReqDTO dto) {

        UserBasicInfo userBasicInfo = new UserBasicInfo(dto.getPhoneNumber(), dto.getAddress());

        //400에러일경우 dto가 잘못됬을경우
        //유저롤로바꿔서 넣어주기
        UserRole userRole = UserRole.builder()
                .authority(dto.getRoles().toString())
                .build();


        return UserCommand.builder()
                .id(dto.getId())
                .userEmail(dto.getUserEmail())
                .userEmail(dto.getUserEmail())
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .userBasicInfo(userBasicInfo)
                .roles(Collections.singletonList(userRole))
                .build();
    }

    public UserCommand toCommand(UserLoginDTO dto) {
        return UserCommand.builder()
                .userEmail(dto.getUserEmail())
                .password(dto.getPassword())
                .build();
    }

}
