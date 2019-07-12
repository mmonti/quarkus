package com.sunrun.setbacks.model;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;

/**
 * A labeled edge of the graph (edge within the context of a graph, which is composed of vertices and edges). Roofplanes
 * are connected together via an edge in the graph if they share the same roof edge. So for example, in an A frame house,
 * the ridge is shared between the two roof planes. That ridge would be represented as a {@link SharedRoofEdge}.
 */
public class SharedRoofEdge {

    private final BiMap<RoofPlane, RoofEdge> roofEdgeRelativeToRoofPlane;

    public SharedRoofEdge(final RoofPlane roofPlaneA, final RoofEdge roofEdgeA,
                          final RoofPlane roofPlaneB, final RoofEdge roofEdgeB) {
        roofEdgeRelativeToRoofPlane = HashBiMap.create(ImmutableMap.of(roofPlaneA, roofEdgeA, roofPlaneB, roofEdgeB));
    }

    public RoofPlane getAdjacentRoofPlane(final RoofEdge roofEdge) {
        if (!roofEdgeRelativeToRoofPlane.containsValue(roofEdge)) {
            throw new IllegalArgumentException("Roof edge is not in this shared edge object.");
        }

        // essentially get the other roof plane that is not associated with this roof edge. better way TODO this?
        return roofEdgeRelativeToRoofPlane.entrySet()
                .stream()
                .filter(entry -> !entry.getValue().equals(roofEdge))
                .findFirst()
                .get().getKey();

    }

    public BiMap<RoofPlane, RoofEdge> getRoofEdgeRelativeToRoofPlane() {
        return this.roofEdgeRelativeToRoofPlane;
    }
}
