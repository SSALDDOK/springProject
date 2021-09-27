package spring_project.project.user.controller.dto.mapper;

import org.springframework.stereotype.Component;
import spring_project.project.user.controller.dto.UserDeleteReqDTO;
import spring_project.project.user.controller.dto.UserJoinReqDTO;
import spring_project.project.user.controller.dto.UserModifyReqDTO;
import spring_project.project.user.domain.model.commands.UserCommand;
import spring_project.project.user.domain.model.valueobjects.UserBasicInfo;


@Component
public class RequestMapper {

    public UserCommand toCommand(UserJoinReqDTO dto){

        UserBasicInfo userBasicInfo = new UserBasicInfo(dto.getPhoneNumber(),dto.getAddress());

        return UserCommand.builder()
                .userEmail(dto.getUserEmail())
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .userBasicInfo(userBasicInfo)
                .build();

    }
    public UserCommand toCommand(UserDeleteReqDTO dto){

        return UserCommand.builder()
                .userEmail(dto.getUserEmail())
                .build();

    }

    public UserCommand toCommand(UserModifyReqDTO dto){

        UserBasicInfo userBasicInfo = new UserBasicInfo(dto.getPhoneNumber(),dto.getAddress());

        return UserCommand.builder()
                .userEmail(dto.getUserEmail())
                .userEmail(dto.getUserEmail())
                .userName(dto.getUserName())
                .password(dto.getPassword())
                .gender(dto.getGender())
                .birth(dto.getBirth())
                .userBasicInfo(userBasicInfo)
                .build();
    }

}
