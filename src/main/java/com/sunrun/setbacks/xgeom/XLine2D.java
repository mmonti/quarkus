package com.sunrun.setbacks.xgeom; // extended geometry package

import javax.vecmath.Vector2f;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 *	Additional intersection routines for Line2D
 */
@SuppressWarnings({"PMD.SimplifiedTernary", "PMD.UnnecessaryFinalModifier"})
class XLine2D extends Line2D.Float {
    
    private static final long serialVersionUID = 1L;
    
	private static final float EPSILON=0.0005F;  // the minimum meaningful difference btw two values (should be a parameter)

	public enum IntersectionType { INTERSECTS, NOINTERSECT, COLLINEAR }

	public XLine2D() { super(); }

	public XLine2D(final float x1, final float y1, final float x2, final float y2) { super(x1, y1, x2, y2); }

	public XLine2D(final Point2D.Float p1, final Vector2f v ) {
		// = TODO: Had to change this due to mave-processor complaining about uninitialization.
		super(p1.x, p1.y, p1.x + v.x, p1.y + v.y);
//		this.setLine(p1.x, p1.y, p1.x + v.x, p1.y + v.y );
	}

	private static boolean equals(final float a, final float b){
        return a == b ? true : Math.abs(a - b) < EPSILON;
	}
	
	private static boolean equals(final double a, final double b){
        return a == b ? true : Math.abs(a - b) < EPSILON;
	}

	private static int signum(final float f){
        return f < EPSILON ? -1 : f > EPSILON ? 1 : 0;
	}

	/**
	 *  Find intersection point between two Line2D segments
	 */
	public IntersectionType intersect(final Line2D l0, final Point2D pt) {
		float x1, y1, x2, y2, x3, y3, x4, y4;	/* Coordinates of lines */
		float a1, b1, c1;						/* Coefficients of line eqns. */
		float r3, r4;        	                /* 'Sign' values */

		x1 = (float )this.getX1();
		y1 = (float )this.getY1();
		x2 = (float )this.getX2();
		y2 = (float )this.getY2();
		x3 = (float )l0.getX1();
		y3 = (float )l0.getY1();
		x4 = (float )l0.getX2();
		y4 = (float )l0.getY2();

		/* Compute a1, b1, c1, where line joining points 1 and 2
		 * is "a1 x  +  b1 y  +  c1  =  0".
		 */
		a1 = y2 - y1;
		b1 = x1 - x2;
		c1 = x2 * y1 - x1 * y2;

		/* Compute r3 and r4.
		 */
		r3 = a1 * x3 + b1 * y3 + c1;
		r4 = a1 * x4 + b1 * y4 + c1;

		/* Check signs of r3 and r4.  If both point 3 and point 4 lie on
		 * same side of line 1, the line segments do not intersect.
		 */
		if ( !equals(r3, 0.F) && !equals(r4, 0.F) && signum(r3) == signum(r4) ) {
			return IntersectionType.NOINTERSECT;
		}

		float a2, b2, c2;	/* Coefficients of line eqns. */

		/* Compute a2, b2, c2 */
		a2 = y4 - y3;
		b2 = x3 - x4;
		c2 = x4 * y3 - x3 * y4;

		/* Compute r1 and r2 */

		final float r1 = a2 * x1 + b2 * y1 + c2;
		final float r2 = a2 * x2 + b2 * y2 + c2;

		/* Check signs of r1 and r2.  If both point 1 and point 2 lie
		 * on same side of second line segment, the line segments do
		 * not intersect.
		 */
		if ( !equals(r1, 0.F) && !equals(r2, 0.F) && signum(r1) == signum(r2) ) {
			return IntersectionType.NOINTERSECT;
		}

		/* Line segments intersect: compute intersection point. 
		 */
		final float denom = a1 * b2 - a2 * b1;
		if ( equals(denom, 0.F) ) {
            return IntersectionType.COLLINEAR;
        }

		pt.setLocation((b1 * c2 - b2 * c1) / denom, (a2 * c1 - a1 * c2) / denom);

		return IntersectionType.INTERSECTS;
	}

	/**
	 *  Find intersection point between Line2D and an infinite horizontal line y
	 */
	public IntersectionType horizontalIntersect(final float y, final Point2D pt) {
		float y1, y2;

		y2 = (float )this.getY2();
		y1 = (float )this.getY1();

		if (equals(y2, y1)) {
			if (equals(y2, y)) {
                return IntersectionType.COLLINEAR;
            }
			return IntersectionType.NOINTERSECT;
		}
		pt.setLocation( (y - y1) * (this.getX2() - this.getX1()) / (y2 - y1) + this.getX1(), y );
		return IntersectionType.INTERSECTS;
	}

	/**
	 *  Find intersection point between Line2D and an infinite vertical line x
	 */
	public IntersectionType verticalIntersect(final float x, final Point2D pt) {
		float x1, x2;

		x2 = (float )this.getX2();
		x1 = (float )this.getX1();

		if (equals(x2, x1)) {
			if (equals(x2, x)) {
                return IntersectionType.COLLINEAR;
            }
			return IntersectionType.NOINTERSECT;
		}
		pt.setLocation( x, (x - x1) * (this.getY2() - this.getY1()) / (x2 - x1) + this.getY1() );
		return IntersectionType.INTERSECTS;
	}

	/**
	 *  Find apparent intersection point between two infinite 2D lines
	 */
	public boolean intersectApparent(final Line2D l0, final Point2D pt) {
		double x1, y1, x2, y2, x3, y3, x4, y4;	/* Coordinates of lines */
		double a1, a2, b1, b2, c1, c2;			/* Coefficients of line eqns. */
		double denom;    						/* Intermediate values */

		x1 = this.getX1();
		y1 = this.getY1();
		x2 = this.getX2();
		y2 = this.getY2();
		x3 = l0.getX1();
		y3 = l0.getY1();
		x4 = l0.getX2();
		y4 = l0.getY2();

		/* Compute a1, b1, c1, where line joining points 1 and 2
		 * is "a1 x  +  b1 y  +  c1  =  0".
		 */
		a1 = y2 - y1;
		b1 = x1 - x2;
		c1 = x2 * y1 - x1 * y2;

		/* Compute a2, b2, c2 */

		a2 = y4 - y3;
		b2 = x3 - x4;
		c2 = x4 * y3 - x3 * y4;

		/* Compute intersection point. 
		 */
		denom = a1 * b2 - a2 * b1;
		if ( equals(denom, 0.F) ) {
            return false;
        }

		pt.setLocation((b1 * c2 - b2 * c1) / denom, (a2 * c1 - a1 * c2) / denom);

		return true;
	}
}
