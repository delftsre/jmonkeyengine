package com.jme3.math.interpolations.impl;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.math.VectorNf;
import com.jme3.math.interpolations.api.NonPrimitiveInterpolation;

public class BezierVectorInterpolation<V extends VectorNf>  implements NonPrimitiveInterpolation<V> {
    private final BezierFloatInterpolation[] inters;

    private final V p0;
    private final V p1;
    private final V p2;
    private final V p3;

    /**Creates an Interpolation on a spline between at least 4 control points following the Bezier equation.
     * here is the interpolation matrix
     * m = [ -1.0   3.0  -3.0    1.0 ]
     *     [  3.0  -6.0   3.0    0.0 ]
     *     [ -3.0   3.0   0.0    0.0 ]
     *     [  1.0   0.0   0.0    0.0 ]
     * where T is the curve tension
     * @param p0 control point 0
     * @param p1 control point 1
     * @param p2 control point 2
     * @param p3 control point 3
     */
    public BezierVectorInterpolation(V p0, V p1, V p2, V p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

        inters = new BezierFloatInterpolation[p0.size()];

        for (int i = 0; i < p0.size(); i++) {
            inters[i] = new BezierFloatInterpolation(p0.get(i), p1.get(i), p2.get(i), p3.get(i));
        }

    }

    /**Calculates the bezier interpolation of step using the state stored in this interpolator
     */
    @Override
    public void interpolate(float step, V resVector) {
        for (int i = 0; i < resVector.size(); i++) {
            resVector.setIndex(i, inters[i].interpolate(step));
        }
    }
}
