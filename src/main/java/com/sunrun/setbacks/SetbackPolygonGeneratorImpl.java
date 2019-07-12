package com.sunrun.setbacks;

import com.sunrun.setbacks.model.Offset;
import com.sunrun.setbacks.model.RoofEdge;
import com.sunrun.setbacks.model.RoofPlane;
import com.sunrun.setbacks.model.Site;
import com.sunrun.setbacks.xgeom.Polygon2D;

import javax.vecmath.Point2f;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SetbackPolygonGeneratorImpl implements SetbackPolygonGenerator {

    @Override
    public Map<String, List<List<Point2f>>> buildSetbackPolygons(final Site site,
                                                                 final Map<RoofEdge, Offset> offsetMapping) {
        return site.getRoofPlanes().stream().collect(Collectors.toMap(
                (roofPlane) -> roofPlane.getRoofPlaneId(),
                (roofPlane) -> generateSetbackPolygons(roofPlane, offsetMapping)));
    }

    static List<List<Point2f>> generateSetbackPolygons(final RoofPlane roofPlane,
                                                       final Map<RoofEdge, Offset> offsetMapping) {
        final float[] offsets = new float[roofPlane.getRoofEdges().size()];
        IntStream.range(0, roofPlane.getRoofEdges().size())
                .boxed()
                .forEach(i -> offsets[i] = getOffset(roofPlane.getRoofEdges().get(i), offsetMapping));

        final Polygon2D polygon2D = getPolygon2D(roofPlane.getPolygon2D());
        final Deque<Polygon2D> setbackPolygons = polygon2D.offset(offsets);
        return setbackPolygons.stream().map(SetbackPolygonGeneratorImpl::getPointsList).collect(Collectors.toList());
    }

    static List<Point2f> getPointsList(final Polygon2D polygon2D) {
        if (polygon2D == null) {
            return Collections.emptyList();
        }

        final List<Point2f> polygonPoints = new ArrayList<>();
        for(int i = 0; i < polygon2D.getVertexCount(); ++i) {
            polygonPoints.add(new Point2f((float) polygon2D.getX(i), (float) polygon2D.getY(i)));
        }
        return polygonPoints;
    }

    static Polygon2D getPolygon2D(final List<Point2f> points) {
        final List<Float> coordsList = new ArrayList<>();
        for (final Point2f point : points) {
            coordsList.add(point.x);
            coordsList.add(point.y);
        }
        final float[] coordinates = new float[coordsList.size()];
        for (int i = 0; i < coordsList.size(); i += 2) {
            coordinates[i] = coordsList.get(i);
            coordinates[i + 1] = coordsList.get(i + 1);
        }
        return new Polygon2D(coordinates);
    }

    /**
     * Default to 0 feet if the roof edge is not found in the mapping.
     *
     * @param roofEdge the roof edge
     * @param offsetMapping mapping containing the offsets
     * @return the offset in feet
     */
    static float getOffset(final RoofEdge roofEdge, final Map<RoofEdge, Offset> offsetMapping) {
        return offsetMapping.get(roofEdge) == null ? 0f : (float) offsetMapping.get(roofEdge).getOffsetDistance();
    }
}
