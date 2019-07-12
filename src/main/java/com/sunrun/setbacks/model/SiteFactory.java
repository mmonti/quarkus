package com.sunrun.setbacks.model;

import org.apache.commons.collections.CollectionUtils;
import org.jgrapht.Graph;
import org.jgrapht.graph.Multigraph;

import javax.vecmath.Point3f;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class SiteFactory {

    private static final SiteFactory INSTANCE = new SiteFactory();

    /**
     *
     */
    private SiteFactory() {
    }

    public static SiteFactory getInstance() {
        return INSTANCE;
    }

    public Site buildSiteFromRoofPlanes(final List<RoofPlane> roofPlanes) {
        if (CollectionUtils.isEmpty(roofPlanes)) {
            throw new IllegalArgumentException("Make sure roof plane list is not null or empty.");
        }

        final Graph<RoofPlane, SharedRoofEdge> graph = new Multigraph<>(SharedRoofEdge.class);

        // = build graph
        roofPlanes.stream().forEach(roofPlane -> graph.addVertex(roofPlane));

        // add shared edges
        for (int i = 0; i < roofPlanes.size(); i++) {
            for (int j = i + 1; j < roofPlanes.size(); j++) {
                final RoofPlane roofPlaneA = roofPlanes.get(i);
                final RoofPlane roofPlaneB = roofPlanes.get(j);
                final Set<SharedRoofEdge> sharedRoofEdges = findSharedRoofEdges(roofPlaneA, roofPlaneB);
                sharedRoofEdges.stream()
                        .forEach(sharedRoofEdge -> graph.addEdge(roofPlaneA, roofPlaneB, sharedRoofEdge));
            }
        }
        final Site site = new Site(graph, false);

        // ugly but necessary for our purposes
        site.getRoofPlanes().stream().forEach(roofPlane -> {
            roofPlane.setSite(site);
            roofPlane.getRoofEdges().stream().forEach(roofEdge -> roofEdge.setRoofPlane(roofPlane));
        });

        return site;
    }

    static Set<SharedRoofEdge> findSharedRoofEdges(final RoofPlane roofPlaneA, final RoofPlane roofPlaneB) {
        final Set<SharedRoofEdge> result = new HashSet<>();
        for (int i = 0; i < roofPlaneA.getRoofEdges().size(); i++) {
            for (int j = 0; j < roofPlaneB.getRoofEdges().size(); j++) {
                final RoofEdge roofEdgeA = roofPlaneA.getRoofEdges().get(i);
                final RoofEdge roofEdgeB = roofPlaneB.getRoofEdges().get(j);

                if (nearlyTheSame(roofEdgeA, roofEdgeB) && roofEdgeA.getEdgeType() == roofEdgeB.getEdgeType()) {
                    result.add(new SharedRoofEdge(roofPlaneA, roofEdgeA, roofPlaneB, roofEdgeB));
                }
            }
        }
        return result;
    }

    static boolean nearlyTheSame(final RoofEdge roofEdgeA, final RoofEdge roofEdgeB) {
        return nearlyTheSame(roofEdgeA.getStart(), roofEdgeB.getStart()) && nearlyTheSame(roofEdgeA.getEnd(), roofEdgeB.getEnd())
                || nearlyTheSame(roofEdgeA.getStart(), roofEdgeB.getEnd()) && nearlyTheSame(roofEdgeA.getEnd(), roofEdgeB.getStart());
    }

    static boolean nearlyTheSame(final Point3f point3fA, final Point3f point3fB) {
        return point3fA.distance(point3fB) < 0.01f;
    }

}
