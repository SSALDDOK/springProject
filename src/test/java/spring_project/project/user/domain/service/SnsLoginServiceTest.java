package spring_project.project.user.domain.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import spring_project.project.common.enums.SnsType;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static spring_project.project.common.enums.SnsType.GOOGLE;

@ExtendWith(MockitoExtension.class)
@DisplayName("sns도메인 서비스 테스트")
class SnsLoginServiceTest {

    @Mock
    private StrategyFactory strategyFactory;

    @Mock
    private Strategy strategy;

    @InjectMocks
    private SnsLoginService snsLoginService;

    @Test
    @DisplayName("SNS 로그인 페이지 Url _ 성공")
    void findSnsRedirectUrlSuccessTest() throws IOException {
        //given
        String googleUrl = "googleUrl";
        SnsType google = GOOGLE;

        given(strategyFactory.findStrategy(google)).willReturn(strategy);
        given(strategy.sendUrlQuery()).willReturn(googleUrl);

        //when
       String result = snsLoginService.findSnsRedirectUrl(google);

        //then
        assertThat(result).isEqualTo(googleUrl);
    }

    @Test
    @DisplayName("SNS 로그인 페이지 토큰 발행 _ 성공")
    void createPostTokenSuccessTest() throws Exception{
        //given
        String code = "code";
        String token = "token";
        String google = "google";

        //테스트다시
        given(strategyFactory.findStrategy(any())).willReturn(strategy);
        given(strategy.sendCallbackUrlCode(any())).willReturn(token);

        //when
        String result = snsLoginService.createPostToken(google,code);

        //then
        assertThat(result).isEqualTo(token);
    }

    @Test
    @DisplayName("SNS 로그인 유저정보 받기_성공")
    void createGetRequestUserInfo() throws Exception {
        //given
        String google = "google";
        String token = "token";
        String testEmail = "lizzy@plgrim.com";

        given(strategyFactory.findStrategy(any())).willReturn(strategy);
        given(strategy.createGetRequest(any())).willReturn(testEmail);

        //when
        String result = snsLoginService.createGetRequest(google,token);

        //then
        assertThat(result).isEqualTo(testEmail);
    }

}