package com.sunrun.setbacks.model;

import org.jgrapht.Graph;

public class ConnectedRoofPlanes {

    private final Graph<RoofPlane, SharedRoofEdge> graph;

    public ConnectedRoofPlanes(Graph<RoofPlane, SharedRoofEdge> graph) {
        this.graph = graph;
    }

    public Graph<RoofPlane, SharedRoofEdge> getGraph() {
        return this.graph;
    }
}
