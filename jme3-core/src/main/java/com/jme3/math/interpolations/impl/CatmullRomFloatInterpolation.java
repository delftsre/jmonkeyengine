package com.jme3.math.interpolations.impl;

import com.jme3.math.interpolations.api.Interpolation;

public class CatmullRomFloatInterpolation implements Interpolation<Float> {
    private final float lowerBound;
    private final float upperBound;
    private float c1, c2, c3, c4;


    /**Creates an Interpolation on a spline between at least 4 control points following the Catmull-Rom equation.
     * here is the interpolation matrix
     * m = [ 0.0  1.0  0.0   0.0 ]
     *     [-T    0.0  T     0.0 ]
     *     [ 2T   T-3  3-2T  -T  ]
     *     [-T    2-T  T-2   T   ]
     * where T is the curve tension
     * @param T The tension of the curve
     * @param p0 control point 0
     * @param p1 control point 1
     * @param p2 control point 2
     * @param p3 control point 3
     */
    public CatmullRomFloatInterpolation(float T, float p0, float p1, float p2, float p3) {
        this.lowerBound = p1;
        this.upperBound = p2;

        c1 = p1;
        c2 = -1.0f * T * p0 + T * p2;
        c3 = 2 * T * p0 + (T - 3) * p1 + (3 - 2 * T) * p2 + -T * p3;
        c4 = -T * p0 + (2 - T) * p1 + (T - 2) * p2 + T * p3;
    }

    /* Calculates the CatmullRomFloatInterpolation of step using the state stored in this interpolator
     * the result is a value between p1 and p2, value=0 for p1, value=1 for p2
     */
    @Override
    public Float interpolate(float value) {
        return (float) (((c4 * value + c3) * value + c2) * value + c1);
    }
}
