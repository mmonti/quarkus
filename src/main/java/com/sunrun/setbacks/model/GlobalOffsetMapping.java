package com.sunrun.setbacks.model;

import java.util.HashMap;
import java.util.Map;

public class GlobalOffsetMapping {

    private static final double DEFAULT_VALUE = 0.0;

    private final Map<EdgeType, Double> offsetMapping;

    public GlobalOffsetMapping() {
        this.offsetMapping = new HashMap<>();
    }

    public GlobalOffsetMapping(final Map<EdgeType, Double> offsetMapping) {
        if (offsetMapping == null) {
            this.offsetMapping = new HashMap<>();
        } else {
            this.offsetMapping = offsetMapping;
        }
    }

    public final Double getOffset(final EdgeType edgeType) {
        return offsetMapping.getOrDefault(edgeType, DEFAULT_VALUE);
    }

}
