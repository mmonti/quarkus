package com.sunrun.setbacks.xgeom;

/**
 * Extended geometry functions - not dependent on application - just pure math
 */
@SuppressWarnings({"PMD.GodClass", "PMD.UseVarargs", "PMD.UselessParentheses", "PMD.UnusedPrivateField",
        "PMD.ClassNamingConventions"})
final class XGeom {
    /**
     * Supress default constructor for noninstantiability
     */
    private XGeom() {
    }
    /**
     * Return true if the three points form an angle more than 180 degrees
     * assuming CCW winding (right hand system)
     */
    public static boolean reflex(final double x1, final double y1, final double x2, final double y2, final double x3, final double y3) {
        final double a1 = x3 - x2;
        final double a2 = y3 - y2;
        final double b1 = x1 - x2;
        final double b2 = y1 - y2;
        return a1 * b2 - a2 * b1 < 0.0f;
    }

}
