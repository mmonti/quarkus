package com.sunrun.setbacks;

import com.sunrun.setbacks.model.*;

import javax.vecmath.Point2f;
import java.util.List;
import java.util.Map;

public class FireSetbacksEngineImpl implements FireSetbacksEngine {

    private final OffsetMappingEngine offsetMappingEngine;
    private final SetbackPolygonGenerator setbackPolygonGenerator;
    private final RuleSet ruleSet;
    private final GlobalOffsetMapping globalOffsetMapping;

    public FireSetbacksEngineImpl(OffsetMappingEngine offsetMappingEngine, SetbackPolygonGenerator setbackPolygonGenerator, RuleSet ruleSet, GlobalOffsetMapping globalOffsetMapping) {
        this.offsetMappingEngine = offsetMappingEngine;
        this.setbackPolygonGenerator = setbackPolygonGenerator;
        this.ruleSet = ruleSet;
        this.globalOffsetMapping = globalOffsetMapping;
    }

    @Override
    public Map<String, List<List<Point2f>>> generateSetbacks(final Site site) {
        final Map<RoofEdge, Offset> offsetMapping = offsetMappingEngine.generateOffsets(site, globalOffsetMapping, ruleSet);
        return setbackPolygonGenerator.buildSetbackPolygons(site, offsetMapping);
    }

    public OffsetMappingEngine getOffsetMappingEngine() {
        return this.offsetMappingEngine;
    }

    public SetbackPolygonGenerator getSetbackPolygonGenerator() {
        return this.setbackPolygonGenerator;
    }

    public RuleSet getRuleSet() {
        return this.ruleSet;
    }

    public GlobalOffsetMapping getGlobalOffsetMapping() {
        return this.globalOffsetMapping;
    }
}
