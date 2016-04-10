package com.jme3.math.interpolations.impl;

import com.jme3.math.Quaternion;
import com.jme3.math.interpolations.api.NonPrimitiveInterpolation;

public class LinearQuaternionInterpolation implements NonPrimitiveInterpolation<Quaternion> {
    private final Quaternion start;
    private final Quaternion end;

    /**
     * Creates a Interpolation instance that applies linear interpolation between
     * start and end
     *
     * @param start The minimum value of this interpolation
     * @param end The maximum  value of this interpolation
     */
    public LinearQuaternionInterpolation(Quaternion start, Quaternion end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns an interpolated value on range of this interpolation.
     * Returns start if step <= 0.0 and end if step >= 1.0
     * The result is always within range of start, end
     */
    @Override
    public void interpolate(float step, Quaternion res) {
        res.slerp(this.start, this.end, step);
    }
}
