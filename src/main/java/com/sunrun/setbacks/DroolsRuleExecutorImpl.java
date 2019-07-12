package com.sunrun.setbacks;

import com.sunrun.setbacks.action.ActionExecutor;
import com.sunrun.setbacks.model.*;
import org.kie.api.KieServices;
import org.kie.api.event.rule.DebugAgendaEventListener;
import org.kie.api.event.rule.DebugRuleRuntimeEventListener;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;

public class DroolsRuleExecutorImpl implements RuleExecutor {

    private static final String ACTION_EXECUTOR = "executor";
    private static final String GLOBAL_OFFSET_MAPPING = "globalOffsetMapping";

    private static final KieServices SERVICES;
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(DroolsRuleExecutorImpl.class);
    private static KieContainer container;

    static {
        final long startServices = System.currentTimeMillis();
        SERVICES = KieServices.get();
        log.info("Services took = {}", System.currentTimeMillis() - startServices);

        if (SERVICES != null) {
            final long startContainer = System.currentTimeMillis();
            container = SERVICES.getKieClasspathContainer();
            log.info("Container took = {}", System.currentTimeMillis() - startContainer);
        }
    }

    private final ActionExecutor actionExecutor;

    public DroolsRuleExecutorImpl(final ActionExecutor actionExecutor) {
        this.actionExecutor = actionExecutor;
    }

    @Override
    public List<Map<RoofEdge, Offset>> execute(final Site site, final GlobalOffsetMapping globalOffsetMapping,
                                               final RuleSet ruleSet) {
        if (container == null) {
            log.error("KieContainer is null, and cannot create session");
            throw new IllegalStateException("KieContainer is null, and cannot create session");
        }

        final long startSession = System.currentTimeMillis();
        final KieSession session = container.newKieSession();
        log.info("Session took = {}ms", System.currentTimeMillis() - startSession);

        session.addEventListener(new DebugRuleRuntimeEventListener());
        session.addEventListener(new DebugAgendaEventListener());

        // = Set the executor.
        session.setGlobal(ACTION_EXECUTOR, actionExecutor);
        session.setGlobal(GLOBAL_OFFSET_MAPPING, globalOffsetMapping);

        // = Insert objects to evaluate
        session.insert(ruleSet);
        session.insert(site);

        site.getRoofPlanes().stream()
            .flatMap(roofPlane -> {
                session.insert(roofPlane);
                return roofPlane.getRoofEdges().stream();
            })
            .forEach(roofEdge -> session.insert(roofEdge));

        // = Fire all the rules.
        final long firingStart = System.currentTimeMillis();
        session.fireAllRules();

        log.info("Firing rules took = {}", System.currentTimeMillis() - firingStart);
        session.dispose();

        // = Collect the offsets from all the rules.
        return actionExecutor.getOffsets();
    }

    public ActionExecutor getActionExecutor() {
        return actionExecutor;
    }
}
