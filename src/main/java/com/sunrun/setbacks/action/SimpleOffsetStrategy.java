package com.sunrun.setbacks.action;

import com.google.common.collect.ImmutableMap;
import com.sunrun.setbacks.model.Offset;
import com.sunrun.setbacks.model.RoofEdge;

import java.util.Map;

public class SimpleOffsetStrategy implements ActionStrategy<RoofEdge> {

    private final double offsetDistance;

    public SimpleOffsetStrategy(double offsetDistance) {
        this.offsetDistance = offsetDistance;
    }

    @Override
    public Map<RoofEdge, Offset> apply(final RoofEdge roofEdge, final String rule) {
        return ImmutableMap.<RoofEdge, Offset>of(roofEdge, Offset.builder()
                .rule(rule)
                .offsetDistance(offsetDistance)
                .build());
    }
}