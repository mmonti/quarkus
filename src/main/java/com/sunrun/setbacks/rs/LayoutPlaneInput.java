package com.sunrun.setbacks.rs;

import javax.vecmath.Point2f;
import java.util.List;

public class LayoutPlaneInput {

    private LayoutPlane layoutPlane;
    private List<Point2f> polygon2D;

    public LayoutPlaneInput(LayoutPlane layoutPlane, List<Point2f> polygon2D) {
        this.layoutPlane = layoutPlane;
        this.polygon2D = polygon2D;
    }

    public LayoutPlaneInput() {
    }

    public LayoutPlane getLayoutPlane() {
        return this.layoutPlane;
    }

    public List<Point2f> getPolygon2D() {
        return this.polygon2D;
    }

    public void setLayoutPlane(LayoutPlane layoutPlane) {
        this.layoutPlane = layoutPlane;
    }

    public void setPolygon2D(List<Point2f> polygon2D) {
        this.polygon2D = polygon2D;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LayoutPlaneInput)) return false;
        final LayoutPlaneInput other = (LayoutPlaneInput) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$layoutPlane = this.getLayoutPlane();
        final Object other$layoutPlane = other.getLayoutPlane();
        if (this$layoutPlane == null ? other$layoutPlane != null : !this$layoutPlane.equals(other$layoutPlane))
            return false;
        final Object this$polygon2D = this.getPolygon2D();
        final Object other$polygon2D = other.getPolygon2D();
        if (this$polygon2D == null ? other$polygon2D != null : !this$polygon2D.equals(other$polygon2D)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LayoutPlaneInput;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $layoutPlane = this.getLayoutPlane();
        result = result * PRIME + ($layoutPlane == null ? 43 : $layoutPlane.hashCode());
        final Object $polygon2D = this.getPolygon2D();
        result = result * PRIME + ($polygon2D == null ? 43 : $polygon2D.hashCode());
        return result;
    }

    public String toString() {
        return "LayoutPlaneInput(layoutPlane=" + this.getLayoutPlane() + ", polygon2D=" + this.getPolygon2D() + ")";
    }
}