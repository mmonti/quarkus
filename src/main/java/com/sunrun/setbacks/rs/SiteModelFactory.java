package com.sunrun.setbacks.rs;

import com.sunrun.setbacks.model.*;

import javax.vecmath.Point2f;
import javax.vecmath.Point3f;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class SiteModelFactory {

    private static final SiteModelFactory INSTANCE = new SiteModelFactory();

    /**
     *
     */
    private SiteModelFactory() {
    }

    public static SiteModelFactory getInstance() {
        return INSTANCE;
    }

    public static SiteModelFactoryBuilder builder() {
        return new SiteModelFactoryBuilder();
    }

    public Site buildSiteModel(final List<LayoutPlaneInput> layoutPlaneInputs) {
        final List<RoofPlane> roofPlanes = layoutPlaneInputs.stream()
                .map(layoutPlane -> buildRoofPlane(layoutPlane.getLayoutPlane(), layoutPlane.getPolygon2D()))
                .collect(Collectors.toList());

        return SiteFactory.getInstance().buildSiteFromRoofPlanes(roofPlanes);
    }

    public RoofPlane buildRoofPlane(final LayoutPlane layoutPlane,
                                    final List<Point2f> roofPolygon2D) {
        return RoofPlane.builder()
                .roofPlaneId(layoutPlane.getIdentifier())
                .slope(layoutPlane.getSlope())
                .useForPv(layoutPlane.isUseForPv())
                .polygon2D(roofPolygon2D)
                .roofEdges(buildRoofEdgeList(layoutPlane.getPolygon(), layoutPlane.getEdgeTypes()))
                .build();
    }

    public List<RoofEdge> buildRoofEdgeList(final List<Point3f> points,
                                            final List<EdgeType> edgeTypes) {
        if (points.size() < 3 || points.size() != edgeTypes.size()) {
            throw new IllegalArgumentException("Points and edge size don't match.");
        }
        final int size = points.size();
        final List<RoofEdge> roofEdges = new ArrayList<>();
        for (int i = 1; i < size; i++) {
            final EdgeType edgeType = edgeTypes.get(i - 1);
            roofEdges.add(buildRoofEdge(edgeType, points.get(i - 1), points.get(i)));
        }
        roofEdges.add(buildRoofEdge(edgeTypes.get(size - 1), points.get(size - 1), points.get(0)));
        return roofEdges;
    }

    public RoofEdge buildRoofEdge(final EdgeType edgeType, final Point3f start, final Point3f end) {
        return RoofEdge.builder()
                .edgeType(edgeType)
                .start(start)
                .end(end)
                .build();
    }

    public static class SiteModelFactoryBuilder {
        SiteModelFactoryBuilder() {
        }

        public SiteModelFactory build() {
            return new SiteModelFactory();
        }

        public String toString() {
            return "SiteModelFactory.SiteModelFactoryBuilder()";
        }
    }
}