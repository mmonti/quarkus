package com.sunrun.setbacks.rs;

import com.sunrun.setbacks.model.EdgeType;

import javax.vecmath.Point3f;
import java.util.List;

// TODO: Probably we should refactor and rename. Maybe unify with LPInput????
public class LayoutPlane {

    private String identifier;
    private Float slope;
    private boolean useForPv;
    private List<EdgeType> edgeTypes;
    private List<Point3f> polygon;


    public LayoutPlane(String identifier, Float slope, boolean useForPv, List<EdgeType> edgeTypes, List<Point3f> polygon) {
        this.identifier = identifier;
        this.slope = slope;
        this.useForPv = useForPv;
        this.edgeTypes = edgeTypes;
        this.polygon = polygon;
    }

    public LayoutPlane() {
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Float getSlope() {
        return this.slope;
    }

    public boolean isUseForPv() {
        return this.useForPv;
    }

    public List<EdgeType> getEdgeTypes() {
        return this.edgeTypes;
    }

    public List<Point3f> getPolygon() {
        return this.polygon;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setSlope(Float slope) {
        this.slope = slope;
    }

    public void setUseForPv(boolean useForPv) {
        this.useForPv = useForPv;
    }

    public void setEdgeTypes(List<EdgeType> edgeTypes) {
        this.edgeTypes = edgeTypes;
    }

    public void setPolygon(List<Point3f> polygon) {
        this.polygon = polygon;
    }
}