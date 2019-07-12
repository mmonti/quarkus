package com.sunrun.setbacks.model;

import com.sunrun.util.Sqft;
import org.jgrapht.Graph;
import org.slf4j.Logger;

import java.util.Set;

/**
 * This object represents the entire site, all roof planes, as a whole. There is an underlying graph that represents
 * how roofplanes are connected to one another: roofplanes being the vertices of the graph, and roof edges that are
 * shared amongst roofplanes are the edges of the graph.
 * <br><br>
 * example: an A-frame house has two roofplanes that have a shared roofedge: the ridge in between. So the graph would
 * have two vertices and one edge between the vertices.
 *
 */
@SuppressWarnings("PMD.ShortClassName")
public class Site {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Site.class);
    // = Multigraph allows for parallel edges
    private final Graph<RoofPlane, SharedRoofEdge> graph;
    private final boolean hasFireSprinklers;

    public Site(Graph<RoofPlane, SharedRoofEdge> graph, boolean hasFireSprinklers) {
        this.graph = graph;
        this.hasFireSprinklers = hasFireSprinklers;
    }

    public Set<SharedRoofEdge> getSharedRoofEdges() {
        return graph.edgeSet();
    }

    public Set<RoofPlane> getRoofPlanes() {
        return graph.vertexSet();
    }

    public boolean hasSingleRidge() {
        return getRoofPlanes().stream()
                .filter(roofPlane -> roofPlane.getRoofEdges().stream()
                        .filter(roofEdge -> roofEdge.getEdgeType() == EdgeType.RIDGE).count() == 1)
                .count() == 1;
    }

    public boolean isHipRoofSite() {
        return getRoofPlanes().stream().allMatch(roofPlane ->
                roofPlane.getRoofEdges().stream()
                        .allMatch(roofEdge -> roofEdge.getEdgeType() == EdgeType.HIP
                                || roofEdge.getEdgeType() == EdgeType.RIDGE
                                || roofEdge.getEdgeType() == EdgeType.EAVE));
    }

    @Sqft
    public double getAreaInSqFt() {
        return getRoofPlanes().stream().mapToDouble(RoofPlane::getAreaInSqFt).sum();
    }

    @Sqft
    public double getModuleFillInSqFt() {
        return getRoofPlanes().stream().mapToDouble(RoofPlane::getModuleFillInSqFt).sum();
    }

    public double getModuleFillPct() {
        return (getModuleFillInSqFt() / getAreaInSqFt()) * 100.0;
    }

    public Graph<RoofPlane, SharedRoofEdge> getGraph() {
        return this.graph;
    }

    public boolean isHasFireSprinklers() {
        return this.hasFireSprinklers;
    }

    public String toString() {
        return "Site(graph=" + this.getGraph() + ", hasFireSprinklers=" + this.isHasFireSprinklers() + ")";
    }
}
