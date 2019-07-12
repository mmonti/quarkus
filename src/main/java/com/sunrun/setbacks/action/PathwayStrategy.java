package com.sunrun.setbacks.action;

import com.sunrun.setbacks.model.EdgeType;
import com.sunrun.setbacks.model.Offset;
import com.sunrun.setbacks.model.RoofEdge;
import com.sunrun.setbacks.model.RoofPlane;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PathwayStrategy implements ActionStrategy<RoofPlane> {

    private final double offsetDistance;

    public PathwayStrategy(double offsetDistance) {
        this.offsetDistance = offsetDistance;
    }

    @Override
    public Map<RoofEdge, Offset> apply(final RoofPlane roofPlane, final String rule) {
        final List<RoofEdge> roofEdges = roofPlane.getRoofEdges();

        if (CollectionUtils.isEmpty(roofEdges)
                || doesNotContainEdgeType(roofEdges, EdgeType.RIDGE)
                || doesNotContainEdgeType(roofEdges, EdgeType.EAVE)) {
            return Collections.emptyMap();
        }

        final int i = findLastEdgeTypeInConsecutiveListOfEdges(EdgeType.RIDGE, roofEdges);

        final List<RoofEdge> roofEdgesInBetweenRidgeAndEave = new ArrayList<>();
        int k = i + 1;
        while (roofEdges.get(getWrappedIndex(k, roofEdges.size())).getEdgeType() != EdgeType.EAVE
                && k < i + roofEdges.size()) {
            roofEdgesInBetweenRidgeAndEave.add(roofEdges.get(getWrappedIndex(k, roofEdges.size())));
            k++;
        }

        return roofEdgesInBetweenRidgeAndEave.stream().collect(Collectors.toMap(
                Function.identity(),
                roofEdge -> Offset.builder().offsetDistance(offsetDistance).rule(rule).build()));
    }

    private static int findLastEdgeTypeInConsecutiveListOfEdges(final EdgeType edgeType,
                                                                final List<RoofEdge> roofEdges) {
        int i = 0;
        for (; i < roofEdges.size() - 1; i++) {
            if (i == roofEdges.size() - 1) {
                if (roofEdges.get(roofEdges.size() - 1).getEdgeType() == edgeType
                        && roofEdges.get(0).getEdgeType() != edgeType) {
                    break;
                }
            }
            if (roofEdges.get(i).getEdgeType() == edgeType
                    && roofEdges.get(i + 1).getEdgeType() != edgeType) {
                break;
            }
        }
        return i;
    }

    private static int getWrappedIndex(final int idx, final int sizeOfCollection) {
        return idx % sizeOfCollection;
    }

    private static boolean doesNotContainEdgeType(final List<RoofEdge> roofEdges, final EdgeType edgeType) {
        return roofEdges.stream().noneMatch(roofEdge -> roofEdge.getEdgeType() == edgeType);
    }

}
