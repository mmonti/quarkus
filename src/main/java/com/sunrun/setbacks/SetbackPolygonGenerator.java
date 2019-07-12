package com.sunrun.setbacks;

import com.sunrun.setbacks.model.Offset;
import com.sunrun.setbacks.model.RoofEdge;
import com.sunrun.setbacks.model.Site;

import javax.vecmath.Point2f;
import java.util.List;
import java.util.Map;

public interface SetbackPolygonGenerator {

    /**
     * Translate the Site model and the offset mapping into 2D setback geometry.
     *
     * @param site the site model
     * @param offsetMapping the offset mapping
     * @return a mapping between roofplane id and a collection of 2D setback polygons
     */
    Map<String, List<List<Point2f>>> buildSetbackPolygons(final Site site, final Map<RoofEdge, Offset> offsetMapping);

}
