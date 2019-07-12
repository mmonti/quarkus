package com.sunrun.setbacks.xgeom;

import javax.vecmath.Tuple2d;
import javax.vecmath.Tuple2f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

@SuppressWarnings({"PMD.SimplifiedTernary", "PMD.ClassNamingConventions"})
public final class XMath {

    public static final float EPSILON = 1E-4f;
	
    private XMath() {
        // Prevent instantiation
    }
	/**
	 *	Compare two single precision numbers with reasonable, scale dependent accuracy
	 */
	public static boolean equals( final float a, final float b ) {
		return a == b ? true : Math.abs(a - b) < 100.f * Math.ulp(Math.max(a,b));
	}

	/**
	 *	Compare two double precision numbers with reasonable, scale dependent accuracy
	 */
	public static boolean equals( final double a, final double b ) {
		return a == b ? true : Math.abs(a - b) < 100. * Math.ulp(Math.max(a,b));
	}

	/**
	 *	Compare two single precision numbers with specified accuracy
	 */
	public static boolean equals( final float a, final float b, final float epsilon ) {
		if (epsilon == 0.0) {
            return equals(a, b);
        }
		return a == b ? true : Math.abs(a - b) < epsilon;
	}
	
	/**
	 *  Compare two Tuple2d
	 */
	public static boolean equals(final Tuple2d t1, final Tuple2d t2) {
		return t1.equals(t2) ? true : XMath.equals(t1.x, t2.x) && XMath.equals(t1.y, t2.y);
	}
	
	/**
	 *  Compare two Tuple2f
	 */
	public static boolean equals(final Tuple2f t1, final Tuple2f t2) {
		return t1.equals(t2) ? true : XMath.equals(t1.x, t2.x) && XMath.equals(t1.y, t2.y);
	}
	
	/**
	 * Compare two Tuple3d, optionally ignore Z comparison
	 */
	public static boolean equals(final Tuple3d t1, final Tuple3d t2, final boolean ignoreZ) {
		if (ignoreZ) {
            return t1.equals(t2) || XMath.equals(t1.x, t2.x) && XMath.equals(t1.y, t2.y);
        } else {
            return t1.equals(t2) || XMath.equals(t1.x, t2.x) && XMath.equals(t1.y, t2.y) && XMath.equals(t1.z, t2.z);
        }
	}
	
	/**
	 * Compare two Tuple3f, optionally ignore Z comparison
	 */
	public static boolean equals(final Tuple3f t1, final Tuple3f t2, final boolean ignoreZ) {
		if (ignoreZ) {
            return t1.equals(t2) || XMath.equals(t1.x, t2.x, 1E-4) && XMath.equals(t1.y, t2.y, 1E-4);
        } else {
            return t1.equals(t2) || XMath.equals(t1.x, t2.x, 1E-4) && XMath.equals(t1.y, t2.y, 1E-4) && XMath.equals(t1.z, t2.z, 1E-4);
        }
	}
	
	/**
	 *	Compare two double precision numbers with specified accuracy
	 */
	public static boolean equals( final double a, final double b, final double epsilon ) {
		if (epsilon == 0.0) {
            return equals(a, b);
        }
		return a == b ? true : Math.abs(a - b) < epsilon;
	}
	
	/**
	 *  GP terms are calculated using formula given below:
		 a * r ^ (n-1) = b
		 where a = first term     
		       r = ratio
		       n = number of terms 
		       b = last term 
	 * The ratio r is calculated below for given first and last term
	 * and then the in between terms are calculated using the r     
	 * @param firstTerm
	 * @param lastTerm
	 * @param numOfTerms
	 * @return
	 */
	public static float[] calculateGeometricProgressionTerms(final float firstTerm, final float lastTerm, final int numOfTerms) {
		final float[] retVal = new float[numOfTerms];
		
		final double x = lastTerm / firstTerm; 
		final double y = 1f / (numOfTerms - 1f);
		final double r = Math.pow(x, y);
		
		for (int i=0; i<retVal.length; i++) {
			retVal[i] = (float)(firstTerm * Math.pow(r, i));
		}
		
		return retVal;
	}
}
