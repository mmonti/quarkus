package com.sunrun.setbacks.action;

import com.sunrun.setbacks.model.Offset;
import com.sunrun.setbacks.model.RoofEdge;

import java.util.Map;

public interface ActionStrategy<T> {

    Map<RoofEdge, Offset> apply(T t, String rule);

}
