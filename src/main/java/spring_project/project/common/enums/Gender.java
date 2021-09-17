package spring_project.project.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    GENDER_MALE("M"),
    GENDER_FEMALE("F");

    private final String genderType;
}
