package com.jme3.math.interpolations.impl;

import com.jme3.math.interpolations.api.Interpolation;

public class LinearFloatInterpolation implements Interpolation<Float> {

    private final float start;
    private final float end;

    /**
     * Creates a Interpolation instance that applies linear interpolation between
     * start and end
     *
     * @param start The minimum value of this interpolation
     * @param end The maximum  value of this interpolation
     */
    public LinearFloatInterpolation(float start, float end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns an interpolated value on range of this interpolation.
     * Returns start if step <= 0.0 and end if step >= 1.0
     * The result is always within range of start, end
     */
    @Override
    public Float interpolate(float step) {
        if (this.start == this.end) {
            return this.start;
        }
        if (step <= 0f) {
            return this.start;
        }
        if (step >= 1f) {
            return this.end;
        }
        return ((1f - step) * this.start) + (step * this.end);

    }
}
