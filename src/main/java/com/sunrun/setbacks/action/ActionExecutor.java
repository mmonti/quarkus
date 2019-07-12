package com.sunrun.setbacks.action;

import com.sunrun.setbacks.model.Offset;
import com.sunrun.setbacks.model.RoofEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionExecutor {

    private final List<Map<RoofEdge, Offset>> offsets = new ArrayList<>();

    public <T> void execute(final ActionStrategy<T> strategy, final T obj, final String ruleName) {
        offsets.add(strategy.apply(obj, ruleName));
    }

    public List<Map<RoofEdge, Offset>> getOffsets() {
        return this.offsets;
    }
}
