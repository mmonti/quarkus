package com.sunrun.setbacks.action;

import com.sunrun.setbacks.model.Offset;
import com.sunrun.setbacks.model.RoofEdge;
import com.sunrun.setbacks.model.RoofPlane;

import java.util.Map;
import java.util.stream.Collectors;

public class GlobalOffsetStrategy implements ActionStrategy<RoofPlane> {

    private final double offsetDistance;

    public GlobalOffsetStrategy(double offsetDistance) {
        this.offsetDistance = offsetDistance;
    }

    @Override
    public Map<RoofEdge, Offset> apply(final RoofPlane roofPlane, final String rule) {
        return roofPlane.getRoofEdges().stream()
                .collect(Collectors.toMap(
                        roofEdge -> roofEdge,
                        roofEdge -> Offset.builder().offsetDistance(offsetDistance).rule(rule).build()));
    }
}
