package com.sunrun.setbacks;

import com.sunrun.setbacks.model.Site;

import javax.vecmath.Point2f;
import java.util.List;
import java.util.Map;

public interface FireSetbacksEngine {

    Map<String, List<List<Point2f>>> generateSetbacks(final Site site);

}
