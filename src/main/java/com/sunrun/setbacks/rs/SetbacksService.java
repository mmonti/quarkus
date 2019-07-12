package com.sunrun.setbacks.rs;

import com.sunrun.setbacks.DroolsRuleExecutorImpl;
import com.sunrun.setbacks.FireSetbacksEngine;
import com.sunrun.setbacks.FireSetbacksEngineBuilder;
import com.sunrun.setbacks.action.ActionExecutor;
import com.sunrun.setbacks.model.EdgeType;
import com.sunrun.setbacks.model.GlobalOffsetMapping;
import com.sunrun.setbacks.model.RuleSet;
import com.sunrun.setbacks.model.Site;

import javax.enterprise.context.ApplicationScoped;
import javax.vecmath.Point2f;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class SetbacksService {

    private FireSetbacksEngine engine;

    public Map<String, List<List<Point2f>>> getSetbacks(final Site site, final Set<String> rules,
                                                        final Map<EdgeType, Double> globalOffsetMapping) {
        if (engine == null) {
            engine = FireSetbacksEngineBuilder.builder()
                    .withMaximumOffsetConsolidator()
                    .withRuleExecutor(new DroolsRuleExecutorImpl(new ActionExecutor()))
                    .withRuleSet(new RuleSet(rules))
                    .withGlobalOffsetMapping(new GlobalOffsetMapping(globalOffsetMapping))
                    .buildEngine();
        }
        return engine.generateSetbacks(site);
    }

}