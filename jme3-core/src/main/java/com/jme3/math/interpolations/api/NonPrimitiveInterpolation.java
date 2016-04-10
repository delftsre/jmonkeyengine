package com.jme3.math.interpolations.api;

public interface NonPrimitiveInterpolation<T> {
    /** Interpolates a non-primitive type by applying {@param step} to some interpolation function
     *
     * Unlike {@link Interpolation<T>}, this function is in place. Because of type erasure during Java compilation
     * it is impossible to construct a class of generic type. To circumvent this, the consumer of this api is required
     * to provide an implementation of the generic type of this class, which ironically will be correctly type checked
     * by the Java compiler again. E.g. it is not possible to have a NonPrimitiveInterpolation<Vector2f> on which you
     * call interpolate(step, new Matrix4f());
     *
     * @param step The step used by the interpolation function
     * @param res The value the resulting interpolated value should be stored in
     */
    void interpolate(float step, T res);
}
