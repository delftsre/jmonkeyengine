package com.jme3.math.interpolations.impl;

import com.jme3.math.VectorNf;
import com.jme3.math.interpolations.api.NonPrimitiveInterpolation;

public class CatmullRomVectorInterpolation<V extends VectorNf>  implements NonPrimitiveInterpolation<V> {
    private final CatmullRomFloatInterpolation[] inters;

    private final V lowerBound;
    private final V upperBound;

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
    public CatmullRomVectorInterpolation(float T, V p0, V p1, V p2, V p3) {

        this.lowerBound = p1;
        this.upperBound = p2;

        inters = new CatmullRomFloatInterpolation[p0.size()];

        for (int i = 0; i < p0.size(); i++) {
            inters[i] = new CatmullRomFloatInterpolation(T, p0.get(i), p1.get(i), p2.get(i), p3.get(i));
        }

    }

    /* Calculates the CatmullRomFloatInterpolation of step using the state stored in this interpolator
     * the result is a value between p1 and p2, value=0 for p1, value=1 for p2
     */
    @Override
    public void interpolate(float value, V resVector) {
        for (int i = 0; i < resVector.size(); i++) {
            resVector.setIndex(i, inters[i].interpolate(value));
        }
    }
}
