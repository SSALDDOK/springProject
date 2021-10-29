package spring_project.project.user.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import spring_project.project.common.enums.SnsType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class StrategyFactory {
    private Map<SnsType, Strategy> strategies;

    //Strategy인터페이스를 이용한 모든 구현체들을 가져옴  => Set<Strategy>
    @Autowired
    public StrategyFactory(Set<Strategy> strategySet) {
        createStrategy(strategySet);
    }

    public Strategy findStrategy(SnsType snsType) {
        return strategies.get(snsType);
    }

    public void createStrategy(Set<Strategy> strategySet) {
        strategies = new HashMap<>();

        strategySet.forEach(strategy ->
                strategies.put(strategy.getSnsType(), strategy));
    }

}
