package com.sunrun.setbacks.model;

import org.slf4j.Logger;

import javax.vecmath.Point3f;

public class RoofEdge {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RoofEdge.class);
    // rake, ridge, eave, hip, valley
    private final EdgeType edgeType;
    // potentially users might label some edges as obstructed (if there is a tree in the way, or furniture)
    // some rules won't allow us to have fire pathways on obstructed edges
    private final boolean obstructed;
    private final Point3f start;
    private final Point3f end;

    private RoofPlane roofPlane;

    public RoofEdge(EdgeType edgeType, boolean obstructed, Point3f start, Point3f end, RoofPlane roofPlane) {
        this.edgeType = edgeType;
        this.obstructed = obstructed;
        this.start = start;
        this.end = end;
        this.roofPlane = roofPlane;
    }

    public static RoofEdgeBuilder builder() {
        return new RoofEdgeBuilder();
    }

    public boolean hasAdjacentRoofPlaneWithLayout() {
        final RoofPlane adjacentRoofPlane = roofPlane.getAdjacentRoofPlane(this);
        return adjacentRoofPlane != null && adjacentRoofPlane.hasLayout();
    }

    public EdgeType getEdgeType() {
        return this.edgeType;
    }

    public boolean isObstructed() {
        return this.obstructed;
    }

    public Point3f getStart() {
        return this.start;
    }

    public Point3f getEnd() {
        return this.end;
    }

    public RoofPlane getRoofPlane() {
        return this.roofPlane;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RoofEdge)) return false;
        final RoofEdge other = (RoofEdge) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$edgeType = this.getEdgeType();
        final Object other$edgeType = other.getEdgeType();
        if (this$edgeType == null ? other$edgeType != null : !this$edgeType.equals(other$edgeType)) return false;
        if (this.isObstructed() != other.isObstructed()) return false;
        final Object this$start = this.getStart();
        final Object other$start = other.getStart();
        if (this$start == null ? other$start != null : !this$start.equals(other$start)) return false;
        final Object this$end = this.getEnd();
        final Object other$end = other.getEnd();
        if (this$end == null ? other$end != null : !this$end.equals(other$end)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RoofEdge;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $edgeType = this.getEdgeType();
        result = result * PRIME + ($edgeType == null ? 43 : $edgeType.hashCode());
        result = result * PRIME + (this.isObstructed() ? 79 : 97);
        final Object $start = this.getStart();
        result = result * PRIME + ($start == null ? 43 : $start.hashCode());
        final Object $end = this.getEnd();
        result = result * PRIME + ($end == null ? 43 : $end.hashCode());
        return result;
    }

    public String toString() {
        return "RoofEdge(edgeType=" + this.getEdgeType() + ", obstructed=" + this.isObstructed() + ", start=" + this.getStart() + ", end=" + this.getEnd() + ", roofPlane=" + this.getRoofPlane() + ")";
    }

    public void setRoofPlane(RoofPlane roofPlane) {
        this.roofPlane = roofPlane;
    }

    public static class RoofEdgeBuilder {
        private EdgeType edgeType;
        private boolean obstructed;
        private Point3f start;
        private Point3f end;
        private RoofPlane roofPlane;

        RoofEdgeBuilder() {
        }

        public RoofEdge.RoofEdgeBuilder edgeType(EdgeType edgeType) {
            this.edgeType = edgeType;
            return this;
        }

        public RoofEdge.RoofEdgeBuilder obstructed(boolean obstructed) {
            this.obstructed = obstructed;
            return this;
        }

        public RoofEdge.RoofEdgeBuilder start(Point3f start) {
            this.start = start;
            return this;
        }

        public RoofEdge.RoofEdgeBuilder end(Point3f end) {
            this.end = end;
            return this;
        }

        public RoofEdge.RoofEdgeBuilder roofPlane(RoofPlane roofPlane) {
            this.roofPlane = roofPlane;
            return this;
        }

        public RoofEdge build() {
            return new RoofEdge(edgeType, obstructed, start, end, roofPlane);
        }

        public String toString() {
            return "RoofEdge.RoofEdgeBuilder(edgeType=" + this.edgeType + ", obstructed=" + this.obstructed + ", start=" + this.start + ", end=" + this.end + ", roofPlane=" + this.roofPlane + ")";
        }
    }
}
