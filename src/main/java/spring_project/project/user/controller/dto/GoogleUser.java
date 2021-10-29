package spring_project.project.user.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUser {

        public String id;
        public String userEmail;
        public Boolean verifiedEmail;
        public String name;
        public String givenName;
        public String familyName;
        public String picture;
        public String locale;
}
