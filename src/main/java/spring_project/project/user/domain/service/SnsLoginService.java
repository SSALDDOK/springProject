package spring_project.project.user.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring_project.project.common.enums.SnsType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class SnsLoginService {

    private final StrategyFactory strategyFactory;

    @Autowired
    public SnsLoginService(StrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    /**
     * 인가코드 받는 코드
     *
     * @param snsType
     * @throws IOException
     */
    public String findSnsRedirectUrl(SnsType snsType) throws IOException {

        //컨트롤러단(화면전환)
        return strategyFactory.findStrategy(snsType).sendUrlQuery();

    }

    /**
     * 인가코드를 이용해서 토큰 발행
     *
     * @param snsType
     * @param code
     * @return
     * @throws Exception
     */
    public String createPostToken(String snsType, String code) throws Exception {

        SnsType snsTypeName = SnsType.valueOf(snsType.toUpperCase());

        Strategy strategy = strategyFactory.findStrategy(snsTypeName);

        return strategy.sendCallbackUrlCode(code);

    }

    /**
     * sns api를 통해서 발행된 토큰으로 유저정보 가져오기
     *
     * @param snsType
     * @param oauthAccessToken
     * @return
     * @throws Exception
     */
    public String createGetRequest(String snsType, String oauthAccessToken) throws Exception {

        SnsType snsTypeName = SnsType.valueOf(snsType.toUpperCase());

        Strategy strategy = strategyFactory.findStrategy(snsTypeName);

        return strategy.createGetRequest(oauthAccessToken);

    }
}
