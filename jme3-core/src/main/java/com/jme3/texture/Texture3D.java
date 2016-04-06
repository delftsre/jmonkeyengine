package com.jme3.texture;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;

import java.io.IOException;

public abstract class Texture3D extends Texture{

    protected WrapMode wrapR = WrapMode.EdgeClamp;

    public Texture3D() {
        super();
    }

    @Override
    public Texture createSimpleClone(Texture rVal) {
        rVal.setWrap(WrapAxis.S, wrapS);
        rVal.setWrap(WrapAxis.T, wrapT);
        rVal.setWrap(WrapAxis.R, wrapR);
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
        if(axis == WrapAxis.R) {
            this.wrapR = mode;
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
        this.wrapR = mode;
    }

    /**
     * <code>getWrap</code> returns the wrap mode for a given coordinate axis
     * on this texture.
     *
     * @param axis
     *            the axis to return for
     * @return the wrap mode of the texture.
     * @throws IllegalArgumentException
     *             if axis is null
     */
    public WrapMode getWrap(WrapAxis axis) {
        if (axis == WrapAxis.R) {
            return wrapR;
        }
        return super.getWrap(axis);
    }

    @Override
    public void write(JmeExporter e) throws IOException {
        super.write(e);
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(wrapR, "wrapR", WrapMode.EdgeClamp);
    }

    @Override
    public void read(JmeImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
        wrapS = capsule.readEnum("wrapS", WrapMode.class, WrapMode.EdgeClamp);
        wrapT = capsule.readEnum("wrapT", WrapMode.class, WrapMode.EdgeClamp);
        wrapR = capsule.readEnum("wrapR", WrapMode.class, WrapMode.EdgeClamp);
    }
}
