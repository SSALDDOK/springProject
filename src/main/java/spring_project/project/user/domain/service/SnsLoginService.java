package spring_project.project.user.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import spring_project.project.common.enums.SnsType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public class SnsLoginService {

    private final StrategyFactory strategyFactory;
    private final HttpServletResponse response;

    @Autowired
    public SnsLoginService(StrategyFactory strategyFactory,HttpServletResponse response) {
        this.strategyFactory = strategyFactory;
        this.response = response;
    }


    public void findSnsRedirectUrl(SnsType snsType) throws IOException {

         String redirectUrl = strategyFactory.findStrategy(snsType).sendUrlQuery();
         response.sendRedirect(redirectUrl);

    }

    public String createPostToken(String snsType, String code) throws Exception {

        SnsType snsTypeName = SnsType.valueOf(snsType.toUpperCase());

        Strategy strategy = strategyFactory.findStrategy(snsTypeName);

        return strategy.sendCallbackUrlCode(code);

    }

    public String createGetRequest(String snsType, String oauthAccessToken) throws Exception {

        SnsType snsTypeName = SnsType.valueOf(snsType.toUpperCase());

        Strategy strategy = strategyFactory.findStrategy(snsTypeName);

        return strategy.createGetRequest(oauthAccessToken);

    }


}
