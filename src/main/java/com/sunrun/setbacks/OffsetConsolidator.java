package com.sunrun.setbacks;

import com.sunrun.setbacks.model.Offset;
import com.sunrun.setbacks.model.RoofEdge;

import java.util.List;
import java.util.Map;

public interface OffsetConsolidator {

    Map<RoofEdge, Offset> consolidate(final List<Map<RoofEdge, Offset>> offsetMappings);

}
