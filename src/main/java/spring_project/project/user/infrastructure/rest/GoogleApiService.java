//package spring_project.project.user.infrastructure.rest;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.http.*;
//import org.springframework.stereotype.Service;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//import spring_project.project.user.controller.dto.GoogleUser;
//import spring_project.project.user.controller.dto.OauthToken;
//
//import static spring_project.project.common.enums.UserUrl.*;
//
//@Service
//public class GoogleApiService {
//
//    private final ObjectMapper objectMapper;
//    RestTemplate restTemplate = new RestTemplate();
//
//
//    public GoogleApiService() {
//        this.objectMapper = new ObjectMapper();
//    }
//
//    /**
//     * API Server로부터 받은 code를 활용하여 사용자 인증 정보 요청 ( access-Token)
//     *
//     * @param code API Server 에서 받아온 code
//     * @return API 서버로 부터 응답받은 Json 형태의 결과를 string으로 반환환
//    */
//    public ResponseEntity<OauthToken> requestAccessToken(String Url,MultiValueMap<String, String> params,HttpHeaders headers) throws Exception {
//
////        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
////        params.add("code", code);
////        params.add("client_id", GOOGLE_SNS_CLIENT_ID);
////        params.add("client_secret", GOOGLE_SNS_CLIENT_SECRET);
////        params.add("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
////        params.add("grant_type", "authorization_code");
////
////        HttpHeaders headers = new HttpHeaders();
////        headers.add("Content-Type", "application/x-www-form-urlencoded");
//
//        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
//
//        return restTemplate.exchange(GOOGLE_SNS_TOKEN_BASE_URL, HttpMethod.POST, httpEntity, OauthToken.class);
//
//    }
///*
//
//    public OauthToken getAccessToken(ResponseEntity<String> response) {
//        OauthToken oauthToken ;
//        try {
//            oauthToken = objectMapper.readValue(response.getBody(), OauthToken.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return oauthToken;
//    }
//
//    public ResponseEntity<String> createGetRequest(OauthToken oauthToken) {
//        String url = "https://www.googleapis.com/oauth2/v1/userinfo";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + oauthToken.getAccessToken());
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
//
//        return restTemplate.exchange(url, HttpMethod.GET, request, String.class);
//    }
//
//    public GoogleUser getUserInfo(ResponseEntity<String> userInfoResponse) {
//        GoogleUser googleUser = null;
//
//        try {
//            googleUser = objectMapper.readValue(userInfoResponse.getBody(), GoogleUser.class);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return googleUser;
//    }
//*/
//
//}
