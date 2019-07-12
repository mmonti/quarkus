package com.sunrun.setbacks;

import com.google.common.collect.ImmutableMap;
import com.sunrun.setbacks.model.Offset;
import com.sunrun.setbacks.model.RoofEdge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This consolidator chooses the maximum offset for a given roof edge if there are multiple offsets from which to
 * choose. For example, let's say that several rules crossover each other and apply offsets to the same edge. If there
 * are 3 offsets for a given ridge of one roof plane: {0f, 1.5f, 2f}, this class will choose 2f. This is the most
 * conservative approach and this gives us the ability to process any {@link com.sunrun.setbacks.rule.SetbackRule}
 * independently. This throws away a lot of complexity because we don't care about the order or priority of rules and
 * can easily resolve RoofEdges that have more than one offset.
 */
public class MaximumOffsetConsolidator implements OffsetConsolidator {

    @Override
    @SuppressWarnings("unchecked")
    public Map<RoofEdge, Offset> consolidate(final List<Map<RoofEdge, Offset>> offsetMappings) {
        final Map<RoofEdge, Offset> result = new HashMap<>();
        for (final Map<RoofEdge, Offset> offsetMap : offsetMappings) {
            for (final Map.Entry<RoofEdge, Offset> entry : offsetMap.entrySet()) {
                final RoofEdge roofEdgeKey = entry.getKey();
                if (result.containsKey(roofEdgeKey)) {
                    if (entry.getValue().getOffsetDistance() > result.get(roofEdgeKey).getOffsetDistance()) {
                        result.put(roofEdgeKey, entry.getValue());
                    }
                } else {
                    result.put(roofEdgeKey, entry.getValue());
                }
            }
        }
        return ImmutableMap.copyOf(result);
    }
}
