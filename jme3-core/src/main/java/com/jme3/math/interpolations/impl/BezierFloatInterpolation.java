package com.jme3.math.interpolations.impl;

import com.jme3.math.interpolations.api.Interpolation;

public class BezierFloatInterpolation implements Interpolation<Float> {
    private final float p0, p1, p2, p3;

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
    public BezierFloatInterpolation(float p0, float p1, float p2, float p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    /**Calculates the bezier interpolation of step using the state stored in this interpolator
     */
    @Override
    public Float interpolate(float step) {
        float oneMinusValue = 1.0f - step;
        float oneMinusValue2 = oneMinusValue * oneMinusValue;
        float value2 = step * step;
        return p0 * oneMinusValue2 * oneMinusValue
                + 3.0f * p1 * step * oneMinusValue2
                + 3.0f * p2 * value2 * oneMinusValue
                + p3 * value2 * step;
    }
}
