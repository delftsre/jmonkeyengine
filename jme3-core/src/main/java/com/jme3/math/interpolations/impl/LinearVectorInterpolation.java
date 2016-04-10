package com.jme3.math.interpolations.impl;

import com.jme3.math.VectorNf;
import com.jme3.math.interpolations.api.NonPrimitiveInterpolation;

public class LinearVectorInterpolation<V extends VectorNf> implements NonPrimitiveInterpolation<V> {
    private final LinearFloatInterpolation[] inters;
    private final V lowerBound;
    private final V upperBound;

    /**
     * Creates a Interpolation instance that applies linear interpolation between
     * start and end
     *
     * @param start The minimum value of this interpolation
     * @param end The maximum  value of this interpolation
     */
    public LinearVectorInterpolation(V start, V end) {

        this.lowerBound = start;
        this.upperBound = end;

        inters = new LinearFloatInterpolation[start.size()];

        for (int i = 0; i < start.size(); i++) {
            inters[i] = new LinearFloatInterpolation(start.get(i), end.get(i));
        }
    }

    /**
     * Returns an interpolated value on range of this interpolation.
     * Returns start if step <= 0.0 and end if step >= 1.0
     * The result is always within range of start, end
     */
    @Override
    public void interpolate(float value, V resVector) {
        for (int i = 0; i < resVector.size(); i++) {
            resVector.setIndex(i, inters[i].interpolate(value));
        }
    }
}
