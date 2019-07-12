package com.sunrun.setbacks.xgeom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.vecmath.Vector2f;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Lightmile extention of diva.util.java2d.Polygon2D.Float
 * @author jhovell
 *
 */
@SuppressWarnings({
		"PMD.GodClass",
		"PMD.UseVarargs",
		"PMD.UnnecessaryLocalBeforeReturn",
		"PMD.CyclomaticComplexity",
		"PMD.NPathComplexity",
		"PMD.NullAssignment"
})
public class Polygon2D extends diva.util.java2d.Polygon2D.Float {

    private static final Logger LOGGER = LoggerFactory.getLogger(Polygon2D.class.getName());

    public Polygon2D() {
        super();
    }

    public Polygon2D(final int size) {
        super(size);
    }

    public Polygon2D(final float[] coords) {
        super(coords);
    }

    /** Create a new polygon that's a copy
     */
    Polygon2D createCopy() {
        final Polygon2D clone = new Polygon2D(this.getVertexCount());
        clone._coordCount = this._coordCount;
        for (int i = 0; i < this.getVertexCount(); i++) {
            clone.setX(i, this.getX(i));
            clone.setY(i, this.getY(i));
        }
        clone._closed = this._closed;

        return clone;
    }

    /** Return double the polygon area - note:  will be negative if ordered clockwise
     */
	private double area2() {
		double area2 = 0.;
        for (int i = 0; i < getVertexCount(); i++) {
			final int next = i + 1 == getVertexCount() ? 0 : i + 1;
            area2 += getX(i) * getY(next) - getX(next) * getY(i);
        }
		return area2;
	}

	public boolean isCounterClockwise() {
		return area2() > 0.0;
	}

	/** Swap the order of the vertices to change winding from clockwise to counterclockwise
	 *		or visa-versa
	 */
	public void reverse() {
		int i = getVertexCount() - 1;
		for (int j = 0; j < i; j++, i--) {
		  final double tmpX = getX(j);
		  final double tmpY = getY(j);
		  setX(j, getX(i));
		  setY(j, getY(i));
		  setX(i, tmpX);
		  setY(i, tmpY);
		}
	}

	/** Copy a range of vertices from this Polygon2D to the end of another.
	 *		Destination should not be closed.  Do nothing if start > end.
	 */
	public void copyVertexRange( final int srcStart, final int srcEnd, final Polygon2D dest ) {
		if (srcStart > srcEnd) {
            return;
        }
		for (int i = srcStart; i <= srcEnd; i++) {
            dest.lineTo( getX(i), getY(i) );
        }
	}

	/** Offset each edge of the polygon by offset distance.
	 *		Negative offset distance means outer offset, positive means inset.
	 *		Polygon must be wound CCW - if not, it's reversed.
	 *		Polygon must be closed - if not, close it.
	 *
	 */
	@SuppressWarnings("PMD.AvoidReassigningLoopVariables")
	public Deque<Polygon2D> offset(final float[] offsetDistances ) {
		int vertexCount = getVertexCount();
		if (offsetDistances.length != 1 && offsetDistances.length != vertexCount) {
			// TODO: Had to replace null with new LinkedList() due to error on the maven-processor.
			// return null;
            return new LinkedList<>();
        }

		closePath();
		vertexCount = getVertexCount();	// Reset vertex count after closing Path.

		final float[] offsets = new float[vertexCount];
		if (offsetDistances.length == 1) {
			// if only one offset distance, apply it to all edges
			for (int i = 0; i < vertexCount; i++) {
                offsets[i] = offsetDistances[0];
            }
		}
		else {
			float signum = Math.signum( offsetDistances[0] );
            int i = 0;
            do {
                signum = Math.signum( offsetDistances[i] );
            } while (++i < offsetDistances.length && signum == 0.f);
            if (signum == 0.f) {
                // all offsets = 0.  just return a copy of original polygon
                final Deque<Polygon2D> polyList = new ArrayDeque<>();
                polyList.add(this.createCopy());
                return polyList;
            }
			for (i = 0; i < vertexCount; i++) {
                offsets[i] = signum * Math.abs(offsetDistances[i]);
            }
		}

		if (!isCounterClockwise()) {
            reverse();
        }

		Polygon2D offPoly;

		offPoly = new Polygon2D( vertexCount );
		for (int i = 0; i < vertexCount; i++) {
			final int prev = i == 0 ? vertexCount - 1 : i - 1;
			while (i < vertexCount && !edgeEdgeOffset( offPoly, prev, i, (i + 1) % vertexCount, offsets[prev], offsets[i] )) {
                i++; // assume colinear, therefore skip colinear vertices
            }
		}
		// Check for self intersection in offset poly.
		//		May generate multiple polygons.
		//		Discards any polygons that have clockwise order (meaning vertex disappeared).
		final Deque<Polygon2D> offPolyList = offPoly.findLoops(true, offsets);

		//System.out.println("Original polygon2D = " + this);
		//System.out.println("\tOffsets = " + Arrays.toString(offsets));
		//System.out.println("\tOffset Polys = " + offPolyList);
		return offPolyList;
	}

	/**
	 *	Offset two edges by the specified distances and find the new intersection point
	 *		between the offset edges
	 */
	private boolean edgeEdgeOffset(final Polygon2D offPoly, final int v0, final int v, final int v1, final float d0, final float d1 ) {
		final double v0X = getX(v0);
		final double v0Y = getY(v0);
		final double vX = getX(v);
		final double vY = getY(v);
		final double v1X = getX(v1);
		final double v1Y = getY(v1);
		// make vectors from edges
		final Vector2f e0 = new Vector2f( (float)(v0X - vX), (float)(v0Y - vY) );
		final Vector2f e1 = new Vector2f( (float)(v1X - vX), (float)(v1Y - vY) );

		// get offset vectors perpendicular to their edge and scaled to their offset distance
		final Vector2f e0off = new Vector2f( (float)(vY - v0Y), (float)(v0X - vX) );
		e0off.normalize();
		e0off.scale(d0);
		final Vector2f e1off = new Vector2f( (float)(vY - v1Y), (float)(v1X - vX) );
		e1off.normalize();
		e1off.scale(d1);

		// make sure we offset toward the inside of the polygon or
		// outside of the polygon based upon the sign of the offset
		// distance. It is assumed that both the distances have the
		// same sign.
		if (d0 > 0 || d0 == 0 && d1 > 0) {
			if (e0off.dot(e1) < 0.) {
                e0off.negate();
            }
			if (e1off.dot(e0) < 0.) {
                e1off.negate();
            }
		}
		else {
			if (e0off.dot(e1) > 0.) {
                e0off.negate();
            }
			if (e1off.dot(e0) > 0.) {
                e1off.negate();
            }
		}

		// if it's a reflex angle, offset in the opposite direction
		if (XGeom.reflex( v0X, v0Y, vX, vY, v1X, v1Y )) {
			e0off.negate();
			e1off.negate();
		}

		// create new offset edges and find intersection point
		final Point2D.Float pne0 = new Point2D.Float( (float)v0X + e0off.x, (float)v0Y + e0off.y );
		final Point2D.Float pne1 = new Point2D.Float( (float)v1X + e1off.x, (float)v1Y + e1off.y );
        final Point2D.Float pne = new Point2D.Float();
		final XLine2D neSeg0 = new XLine2D( pne0, e0 );
		final XLine2D neSeg1 = new XLine2D( pne1, e1 );

		if (neSeg1.intersectApparent( neSeg0, pne )) {
            // sanity check - make sure new edges are in same direction
            // relative to one another as the original edges
            final Vector2f ne0 = new Vector2f( (float)(pne0.x - pne.x), (float)(pne0.y - pne.y) );
            final Vector2f ne1 = new Vector2f( (float)(pne1.x - pne.x), (float)(pne1.y - pne.y) );
            if (Math.abs(e0.dot(e1)) > 0.1 && (ne0.dot(e0) < 0 || ne1.dot(e1) < 0)) {
                // ideally, should create an additional vertex in this case, but it only occurs
                // with nearly straight edges and unequal offsets, making it relatively rare
                LOGGER.debug("Reversed edge direction: e0=" + e0 + " e1=" + e1 + " ne0=" + ne0 + " ne1=" + ne1);
            }
            else {
                offPoly.lineTo( pne.x, pne.y );
                return true;
            }
		}
        return false;
	}

	/**
	 *	If polygon is self-intersecting, find intersection point(s) and split into multiple polygons
	 *		Returns the full list of non-self-intersecting polygons.
	 *		Note:  optional pruning removes polygons that result from a disappearing vertex
	 */
	public Deque<Polygon2D> findLoops(final boolean prune, final float[] offsets) {
		final Deque<Polygon2D> polyList = new ArrayDeque<>();
		findLoopsRecurse( polyList, 0, prune, offsets );
		return polyList;
	}

	/**
	 *	Find self-intersections in polygon and split at intersection point if found.
	 *		Note:  O(n^2) relative to number of vertices so works best with
	 *		polygons with not too many sides.
	 */
	private void findLoopsRecurse(final Deque<Polygon2D> polyList, final int firstEdge, final boolean prune, final float[] offsets) {
		polyList.push(this);
		final int vertexCount = getVertexCount();
		final XLine2D e0 = new XLine2D();
		final XLine2D e1 = new XLine2D();
		final Point2D.Float intersectPt = new Point2D.Float();
		for (int i = firstEdge; i < vertexCount - 2; i++) {
			for (int j = i + 2; j < vertexCount && (j + 1)%vertexCount != i; j++) {
				//System.out.println( "Comparing edge " + i + " to " + j );
				e0.setLine( getX(i), getY(i), getX(i+1), getY(i+1) );
				e1.setLine( getX(j), getY(j), getX((j+1) % vertexCount), getY((j+1) % vertexCount) );
				if (e0.intersect( e1, intersectPt ) == XLine2D.IntersectionType.INTERSECTS) {
					// remove self from polygon stack - if self intersecting, we only want the children
					polyList.pop();
					// split into two children, A and B
					Polygon2D polyA, polyB;
					polyA = new Polygon2D();
					polyB = new Polygon2D();
					// copy points from beginning to first point of first intersected edge
					//	and from second point of last intersected edge to end to polyA
					copyVertexRange( 0, i, polyA );
					polyA.lineTo( intersectPt.x, intersectPt.y );
					copyVertexRange( j+1, vertexCount-1, polyA );
					polyA.closePath();
					// copy points from second point of first intersected edge
					//	to first point of last intersected edge to polyB
					polyB.lineTo( intersectPt.x, intersectPt.y );
					copyVertexRange( i+1, j, polyB );
					polyB.closePath();

					//System.out.println( "Offset created two polygons: " );
					//System.out.println( "A:  " + polyA );
					//System.out.println( "B:  " + polyB );
					// optionally prune if winding wrong
					if (prune) {
						if (!polyA.isCounterClockwise()) {
							//System.out.printf( "pruned A with area %5.5f\n", polyA.area() );
							polyA = null;
						}
						if (!polyB.isCounterClockwise()) {
							//System.out.printf( "pruned B with area %5.5f\n", polyB.area() );
							polyB = null;
						}
					}
					// continue looking for intersections in children
					if (polyA != null) {
                        polyA.findLoopsRecurse( polyList, i, prune, offsets );
                    }
					if (polyB != null) {
                        polyB.findLoopsRecurse( polyList, 0, prune, offsets );
                    }
					return;
				}
			}
		}
	}

    /** Return a string representation of the polygon.
     */
    @Override
    public String toString() {
        final StringBuffer out = new StringBuffer(getClass().getName()).append("[\n");

        for (int i = 0; i < getVertexCount(); i++) {
            out.append('\t').append(getX(i)).append(", ").append(getY(i)).append('\n');
        }
        return out.append(']').toString();
    }

}
