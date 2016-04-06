package com.jme3.texture;

public abstract class Texture2D extends Texture{

    public Texture2D() {
        super();
    }

    @Override
    public Texture createSimpleClone(Texture rVal) {
        rVal.setWrap(Texture.WrapAxis.S, wrapS);
        rVal.setWrap(Texture.WrapAxis.T, wrapT);
        return super.createSimpleClone(rVal);
    }

    /**
     * <code>setWrap</code> sets the wrap mode of this texture for a
     * particular axis.
     *
     * @param axis
     *            the texture axis to define a wrapmode on.
     * @param mode
     *            the wrap mode for the given axis of the texture.
     * @throws IllegalArgumentException
     *             if axis or mode are null
     */
    public void setWrap(WrapAxis axis, WrapMode mode) {
        super.setWrap(axis, mode);
        if (axis != WrapAxis.S && axis != WrapAxis.T) {
            throw new IllegalArgumentException("Not applicable for 2D textures");
        }
    }

    /**
     * <code>setWrap</code> sets the wrap mode of this texture for all axis.
     *
     * @param mode
     *            the wrap mode for the given axis of the texture.
     * @throws IllegalArgumentException
     *             if mode is null
     */
    public void setWrap(WrapMode mode) {
        super.setWrap(mode);
    }

    /**
     * <code>getWrap</code> returns the wrap mode for a given coordinate axis
     * on this texture.
     *
     * @param axis
     *            the axis to return for
     * @return the wrap mode of the texture.
     * @throws IllegalArgumentException
     *             if axis is null or invalid for this type
     */
    public WrapMode getWrap(WrapAxis axis) {
        if (axis == WrapAxis.R) {
            throw new IllegalArgumentException("invalid WrapAxis: " + axis);
        }
        return super.getWrap(axis);
    }
}
