package com.sunrun.setbacks;

import com.sunrun.setbacks.model.*;

import java.util.Map;

public class OffsetMappingEngine {

    private OffsetConsolidator offsetConsolidator;
    private RuleExecutor ruleExecutor;

    OffsetMappingEngine(OffsetConsolidator offsetConsolidator, RuleExecutor ruleExecutor) {
        this.offsetConsolidator = offsetConsolidator;
        this.ruleExecutor = ruleExecutor;
    }

    public static com.sunrun.setbacks.OffsetMappingEngine.OffsetMappingEngineBuilder builder() {
        return new OffsetMappingEngineBuilder();
    }

    /**
     * Given the site model and a collection of rules, generate a mapping of roofedges to offsets. All the roof edges
     * contained in the map exist inside the site model. If an edge is not found in the mapping, assume that the offset
     * is 0 feet.
     *
     * @param site the site model
     * @param ruleSet a collection of rules partitioned by site, roof plane, and roof edge
     * @return a mapping between the edges of the roof planes within the site model and their associated offset
     */
    public Map<RoofEdge, Offset> generateOffsets(final Site site,
                                                 final GlobalOffsetMapping globalOffsetMapping,
                                                 final RuleSet ruleSet) {
        // = Execute the rules, and consolidate the results.
        return offsetConsolidator.consolidate(ruleExecutor.execute(site, globalOffsetMapping, ruleSet));
    }

    public OffsetConsolidator getOffsetConsolidator() {
        return this.offsetConsolidator;
    }

    public RuleExecutor getRuleExecutor() {
        return this.ruleExecutor;
    }

    public static class OffsetMappingEngineBuilder {
        private OffsetConsolidator offsetConsolidator;
        private RuleExecutor ruleExecutor;

        OffsetMappingEngineBuilder() {
        }

        public com.sunrun.setbacks.OffsetMappingEngine.OffsetMappingEngineBuilder offsetConsolidator(OffsetConsolidator offsetConsolidator) {
            this.offsetConsolidator = offsetConsolidator;
            return this;
        }

        public com.sunrun.setbacks.OffsetMappingEngine.OffsetMappingEngineBuilder ruleExecutor(RuleExecutor ruleExecutor) {
            this.ruleExecutor = ruleExecutor;
            return this;
        }

        public com.sunrun.setbacks.OffsetMappingEngine build() {
            return new OffsetMappingEngine(offsetConsolidator, ruleExecutor);
        }

        public String toString() {
            return "OffsetMappingEngine.OffsetMappingEngineBuilder(offsetConsolidator=" + this.offsetConsolidator + ", ruleExecutor=" + this.ruleExecutor + ")";
        }
    }
}
