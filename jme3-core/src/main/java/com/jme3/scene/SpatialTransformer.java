package com.jme3.scene;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.util.TempVars;

public class SpatialTransformer {

    /**
     * Translates the spatial by the given translation vector.
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial move(Spatial s, float x, float y, float z) {
        s.localTransform.getTranslation().addLocal(x, y, z);
        s.setTransformRefresh();

        return s;
    }

    /**
     * Translates the spatial by the given translation vector.
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial move(Spatial s, Vector3f offset) {
        s.localTransform.getTranslation().addLocal(offset);
        s.setTransformRefresh();

        return s;
    }

    /**
     * Scales the spatial by the given value
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial scale(Spatial s, float xyz) {
        return scale(s, xyz, xyz, xyz);
    }

    /**
     * Scales the spatial by the given scale vector.
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial scale(Spatial s, float x, float y, float z) {
        s.localTransform.getScale().multLocal(x, y, z);
        s.setTransformRefresh();

        return s;
    }

    /**
     * Rotates the spatial by the given rotation.
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial rotate(Spatial s, Quaternion rot) {
        s.localTransform.getRotation().multLocal(rot);
        s.setTransformRefresh();

        return s;
    }

    /**
     * Rotates the spatial by the xAngle, yAngle and zAngle angles (in radians),
     * (aka pitch, yaw, roll) in the local coordinate space.
     *
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial rotate(Spatial s, float xAngle, float yAngle, float zAngle) {
        TempVars vars = TempVars.get();
        Quaternion q = vars.quat1;
        q.fromAngles(xAngle, yAngle, zAngle);
        rotate(s, q);
        vars.release();

        return s;
    }

    /**
     * Centers the spatial in the origin of the world bound.
     * @return The spatial on which this method is called, e.g <code>this</code>.
     */
    public Spatial center(Spatial s) {
        Vector3f worldTrans = s.getWorldTranslation();
        Vector3f worldCenter = s.getWorldBound().getCenter();

        Vector3f absTrans = worldTrans.subtract(worldCenter);
        s.setLocalTranslation(absTrans);

        return s;
    }

}
