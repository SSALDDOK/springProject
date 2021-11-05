package spring_project.project.user.controller.dto;

import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GoogleUser {

        public String id;
        public String email;
        public Boolean verified_email;
        public String name;
        public String given_name;
        public String family_name;
        public String picture;
        public String locale;
}
