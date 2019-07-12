package com.sunrun.setbacks;

import com.sunrun.setbacks.model.GlobalOffsetMapping;
import com.sunrun.setbacks.model.RuleSet;

public class FireSetbacksEngineBuilder {

    private RuleSet ruleSet;
    private OffsetConsolidator offsetConsolidator;
    private RuleExecutor ruleExecutor;
    private GlobalOffsetMapping globalOffsetMapping;

    public FireSetbacksEngineBuilder withMaximumOffsetConsolidator() {
        offsetConsolidator = new MaximumOffsetConsolidator();
        return this;
    }

    public FireSetbacksEngineBuilder withRuleSet(final RuleSet ruleSet) {
        if (ruleSet == null) {
            return this;
        }

        if (this.ruleSet == null) {
            this.ruleSet = new RuleSet();
            this.ruleSet.getRuleIds().addAll(ruleSet.getRuleIds());
        }

        return this;
    }

    public FireSetbacksEngineBuilder withRuleExecutor(final RuleExecutor ruleExecutor) {
        this.ruleExecutor = ruleExecutor;
        return this;
    }

    public FireSetbacksEngineBuilder withGlobalOffsetMapping(final GlobalOffsetMapping globalOffsetMapping) {
        this.globalOffsetMapping = globalOffsetMapping;
        return this;
    }

    public FireSetbacksEngine buildEngine() {
        if (offsetConsolidator == null) {
            throw new IllegalArgumentException("OffsetConsolidator null");
        }

        if (ruleExecutor == null) {
            throw new IllegalArgumentException("RuleExecutor is null");
        }

        final OffsetMappingEngine offsetMappingEngine = OffsetMappingEngine.builder()
                .offsetConsolidator(offsetConsolidator)
                .ruleExecutor(ruleExecutor)
                .build();

        return new FireSetbacksEngineImpl(
                offsetMappingEngine,
                new SetbackPolygonGeneratorImpl(),
                ruleSet,
                globalOffsetMapping
        );
    }

    public static FireSetbacksEngineBuilder builder() {
        return new FireSetbacksEngineBuilder();
    }

}
