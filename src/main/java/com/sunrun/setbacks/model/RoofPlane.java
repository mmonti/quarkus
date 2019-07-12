package com.sunrun.setbacks.model;

import com.sunrun.util.Sqft;
import org.apache.commons.collections.CollectionUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.slf4j.Logger;

import javax.vecmath.Point2f;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RoofPlane {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(RoofPlane.class);
    private final String roofPlaneId;
    private final List<Point2f> polygon2D;
    private final boolean useForPv;
    private final float slope;
    private final boolean frontFacing;
    private final boolean habitable;
    private final boolean detached;

    private final Layout layout;

    // = More immutable data that we might need...
    private final List<List<Point2f>> fireEscapes; // ??

    // = Number of edges matches number of points.
    private final List<RoofEdge> roofEdges;

    private Site site;

    public RoofPlane(String roofPlaneId, List<Point2f> polygon2D, boolean useForPv, float slope, boolean frontFacing, boolean habitable, boolean detached, Layout layout, List<List<Point2f>> fireEscapes, List<RoofEdge> roofEdges, Site site) {
        this.roofPlaneId = roofPlaneId;
        this.polygon2D = polygon2D;
        this.useForPv = useForPv;
        this.slope = slope;
        this.frontFacing = frontFacing;
        this.habitable = habitable;
        this.detached = detached;
        this.layout = layout;
        this.fireEscapes = fireEscapes;
        this.roofEdges = roofEdges;
        this.site = site;
    }

    public static RoofPlaneBuilder builder() {
        return new RoofPlaneBuilder();
    }

    public void validate() {
        if (polygon2D.size() < 3) {
            throw new IllegalArgumentException("polygon list must be have at least 3 points");
        }
        if (roofEdges.isEmpty()) {
            throw new IllegalArgumentException("roofEdges cannot be empty");
        }
    }

    public boolean hasRidge() {
        return roofEdges.stream().anyMatch(roofEdge -> roofEdge.getEdgeType() == EdgeType.RIDGE);
    }

    public boolean hasFireEscapes() {
        return CollectionUtils.isNotEmpty(fireEscapes);
    }

    public boolean hasGroundAccess() {
        return roofEdges.stream().anyMatch(roofEdge ->
                roofEdge.getEdgeType() == EdgeType.EAVE && !roofEdge.isObstructed());
    }

    @Sqft
    public double getAreaInSqFt() {
        // TODO : fill this out
        return 0.0;
    }

    @Sqft
    public double getModuleFillInSqFt() {
        return layout == null ? 0.0 : layout.getNumModules() * layout.getModuleSpec().getAreaInFtSq();
    }

    public boolean hasLayout() {
        return layout != null && layout.getNumModules() > 0;
    }

    public Set<RoofPlane> getNeighbors() {
        return getNeighboringRoofPlanes(1);
    }

    public ConnectedRoofPlanes getConnectedRoofPlanes() {
        return new ConnectedRoofPlanes(new AsSubgraph<>(site.getGraph(), getNeighboringRoofPlanes(null)));
    }

    private Set<RoofPlane> getNeighboringRoofPlanes(@Nullable final Integer depth) {
        if (depth != null && depth < 1) {
            throw new IllegalArgumentException("Bad input");
        }
        final BreadthFirstIterator<RoofPlane, SharedRoofEdge> bfIterator =
                new BreadthFirstIterator<>(site.getGraph(), this);

        final Set<RoofPlane> roofPlanes = new HashSet<>();
        while (bfIterator.hasNext()) {
            final RoofPlane current = bfIterator.next();
            if (depth == null || bfIterator.getDepth(current) == depth) {
                roofPlanes.add(current);
            }
        }

        return roofPlanes;
    }

    @Nullable
    public RoofPlane getAdjacentRoofPlane(final RoofEdge roofEdge) {
        if (!roofEdges.contains(roofEdge)) {
            throw new IllegalArgumentException("Inputted roof edge must belong to this roof plane.");
        }

        final Set<SharedRoofEdge> sharedRoofEdges = site.getGraph().edgesOf(this);

        final Optional<SharedRoofEdge> sharedEdge = sharedRoofEdges.stream()
                .filter(sharedRoofEdge -> sharedRoofEdge.getRoofEdgeRelativeToRoofPlane().containsValue(roofEdge))
                .findFirst();

        if (!sharedEdge.isPresent()) {
            return null;
        }

        return sharedEdge.get().getAdjacentRoofPlane(roofEdge);
    }

    public String getRoofPlaneId() {
        return this.roofPlaneId;
    }

    public List<Point2f> getPolygon2D() {
        return this.polygon2D;
    }

    public boolean isUseForPv() {
        return this.useForPv;
    }

    public float getSlope() {
        return this.slope;
    }

    public boolean isFrontFacing() {
        return this.frontFacing;
    }

    public boolean isHabitable() {
        return this.habitable;
    }

    public boolean isDetached() {
        return this.detached;
    }

    public Layout getLayout() {
        return this.layout;
    }

    public List<List<Point2f>> getFireEscapes() {
        return this.fireEscapes;
    }

    public List<RoofEdge> getRoofEdges() {
        return this.roofEdges;
    }

    public Site getSite() {
        return this.site;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof RoofPlane)) return false;
        final RoofPlane other = (RoofPlane) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$roofPlaneId = this.getRoofPlaneId();
        final Object other$roofPlaneId = other.getRoofPlaneId();
        if (this$roofPlaneId == null ? other$roofPlaneId != null : !this$roofPlaneId.equals(other$roofPlaneId))
            return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof RoofPlane;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $roofPlaneId = this.getRoofPlaneId();
        result = result * PRIME + ($roofPlaneId == null ? 43 : $roofPlaneId.hashCode());
        return result;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public static class RoofPlaneBuilder {
        private String roofPlaneId;
        private List<Point2f> polygon2D;
        private boolean useForPv;
        private float slope;
        private boolean frontFacing;
        private boolean habitable;
        private boolean detached;
        private Layout layout;
        private List<List<Point2f>> fireEscapes;
        private List<RoofEdge> roofEdges;
        private Site site;

        RoofPlaneBuilder() {
        }

        public RoofPlane build() {
            return build(true);
        }

        public RoofPlane build(final boolean validate) {
            final RoofPlane roofPlane = this.buildInternal();
            if (validate) {
                roofPlane.validate();
            }
            return roofPlane;
        }

        public RoofPlaneBuilder roofPlaneId(String roofPlaneId) {
            this.roofPlaneId = roofPlaneId;
            return this;
        }

        public RoofPlaneBuilder polygon2D(List<Point2f> polygon2D) {
            this.polygon2D = polygon2D;
            return this;
        }

        public RoofPlaneBuilder useForPv(boolean useForPv) {
            this.useForPv = useForPv;
            return this;
        }

        public RoofPlaneBuilder slope(float slope) {
            this.slope = slope;
            return this;
        }

        public RoofPlaneBuilder frontFacing(boolean frontFacing) {
            this.frontFacing = frontFacing;
            return this;
        }

        public RoofPlaneBuilder habitable(boolean habitable) {
            this.habitable = habitable;
            return this;
        }

        public RoofPlaneBuilder detached(boolean detached) {
            this.detached = detached;
            return this;
        }

        public RoofPlaneBuilder layout(Layout layout) {
            this.layout = layout;
            return this;
        }

        public RoofPlaneBuilder fireEscapes(List<List<Point2f>> fireEscapes) {
            this.fireEscapes = fireEscapes;
            return this;
        }

        public RoofPlaneBuilder roofEdges(List<RoofEdge> roofEdges) {
            this.roofEdges = roofEdges;
            return this;
        }

        public RoofPlaneBuilder site(Site site) {
            this.site = site;
            return this;
        }

        public RoofPlane buildInternal() {
            return new RoofPlane(roofPlaneId, polygon2D, useForPv, slope, frontFacing, habitable, detached, layout, fireEscapes, roofEdges, site);
        }

        public String toString() {
            return "RoofPlane.RoofPlaneBuilder(roofPlaneId=" + this.roofPlaneId + ", polygon2D=" + this.polygon2D + ", useForPv=" + this.useForPv + ", slope=" + this.slope + ", frontFacing=" + this.frontFacing + ", habitable=" + this.habitable + ", detached=" + this.detached + ", layout=" + this.layout + ", fireEscapes=" + this.fireEscapes + ", roofEdges=" + this.roofEdges + ", site=" + this.site + ")";
        }
    }

}