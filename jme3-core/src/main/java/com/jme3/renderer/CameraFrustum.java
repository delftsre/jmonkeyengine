package com.jme3.renderer;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.FastMath;
import com.jme3.math.Plane;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CameraFrustum {

    private static final Logger logger = Logger.getLogger(CameraFrustum.class.getName());

    private Camera camera;

    /**
     * The <code>Intersect</code> enum is returned as a result
     * of a culling check operation,
     * see {@link #contains(com.jme3.bounding.BoundingVolume) }
     */
    public enum Intersect {

        /**
         * defines a constant assigned to spatials that are completely outside
         * of this camera's view frustum.
         */
        Outside,
        /**
         * defines a constant assigned to spatials that are completely inside
         * the camera's view frustum.
         */
        Inside,
        /**
         * defines a constant assigned to spatials that are intersecting one of
         * the six planes that define the view frustum.
         */
        Intersects;
    }
    /**
     * LEFT_PLANE represents the left plane of the camera frustum.
     */
    public static final int LEFT_PLANE = 0;
    /**
     * RIGHT_PLANE represents the right plane of the camera frustum.
     */
    public static final int RIGHT_PLANE = 1;
    /**
     * BOTTOM_PLANE represents the bottom plane of the camera frustum.
     */
    public static final int BOTTOM_PLANE = 2;
    /**
     * TOP_PLANE represents the top plane of the camera frustum.
     */
    public static final int TOP_PLANE = 3;
    /**
     * FAR_PLANE represents the far plane of the camera frustum.
     */
    public static final int FAR_PLANE = 4;
    /**
     * NEAR_PLANE represents the near plane of the camera frustum.
     */
    public static final int NEAR_PLANE = 5;
    /**
     * FRUSTUM_PLANES represents the number of planes of the camera frustum.
     */
    public static final int FRUSTUM_PLANES = 6;
    /**
     * MAX_WORLD_PLANES holds the maximum planes allowed by the system.
     */
    public static final int MAX_WORLD_PLANES = 6;


    /**
     * Distance from camera to near frustum plane.
     */
    private float near;
    /**
     * Distance from camera to far frustum plane.
     */
    private float far;
    /**
     * Distance from camera to left frustum plane.
     */
    private float left;
    /**
     * Distance from camera to right frustum plane.
     */
    private float right;
    /**
     * Distance from camera to top frustum plane.
     */
    private float top;
    /**
     * Distance from camera to bottom frustum plane.
     */
    private float bottom;

    public CameraFrustum(Camera camera, float near, float far, float left, float right, float top, float bottom) {
        this.camera = camera;
        this.near = near;
        this.far = far;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public void set(float near, float far, float left, float right, float top, float bottom) {
        this.near = near;
        this.far = far;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        camera.onFrustumChange();
    }

    public float getNear() {
        return near;
    }

    public void setNear(float near) {
        this.near = near;
        camera.onFrustumChange();
    }

    public float getFar() {
        return far;
    }

    public void setFar(float far) {
        this.far = far;
        camera.onFrustumChange();
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
        camera.onFrustumChange();
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
        camera.onFrustumChange();
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
        camera.onFrustumChange();
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
        camera.onFrustumChange();
    }

    public void copyFrom(CameraFrustum other) {
        this.near = other.near;
        this.far = other.far;
        this.left = other.left;
        this.right = other.right;
        this.top = other.top;
        this.bottom = other.bottom;
        camera.onFrustumChange();
    }

    /**
     * <code>contains</code> tests a bounding volume against the planes of the
     * camera's frustum. The frustum's planes are set such that the normals all
     * face in towards the viewable scene. Therefore, if the bounding volume is
     * on the negative side of the plane is can be culled out.
     *
     * NOTE: This method is used internally for culling, for public usage,
     * the plane state of the bounding volume must be saved and restored, e.g:
     * <code>BoundingVolume bv;<br/>
     * Camera c;<br/>
     * int planeState = bv.getPlaneState();<br/>
     * bv.setPlaneState(0);<br/>
     * c.contains(bv);<br/>
     * bv.setPlaneState(plateState);<br/>
     * </code>
     *
     * @param bound the bound to check for culling
     * @return See enums in <code>Intersect</code>
     */
    public Intersect contains(BoundingVolume bound) {
        if (bound == null) {
            return Intersect.Inside;
        }

        int mask;
        Intersect rVal = Intersect.Inside;

        for (int planeCounter = FRUSTUM_PLANES; planeCounter >= 0; planeCounter--) {
            if (planeCounter == bound.getCheckPlane()) {
                continue; // we have already checked this plane at first iteration
            }
            int planeId = (planeCounter == FRUSTUM_PLANES) ? bound.getCheckPlane() : planeCounter;
//            int planeId = planeCounter;

            mask = 1 << (planeId);
            if ((camera.getPlaneState() & mask) == 0) {
                Plane.Side side = bound.whichSide(camera.getWorldPlane(planeId));

                if (side == Plane.Side.Negative) {
                    //object is outside of frustum
                    bound.setCheckPlane(planeId);
                    return Intersect.Outside;
                } else if (side == Plane.Side.Positive) {
                    //object is visible on *this* plane, so mark this plane
                    //so that we don't check it for sub nodes.
                    camera.setPlaneState(camera.getPlaneState() | mask);
                } else {
                    rVal = Intersect.Intersects;
                }
            }
        }

        return rVal;
    }

    /**
     * <code>setFrustumPerspective</code> defines the frustum for the camera.  This
     * frustum is defined by a viewing angle, aspect ratio, and near/far planes
     *
     * @param fovY   Frame of view angle along the Y in degrees.
     * @param aspect Width:Height ratio
     * @param near   Near view plane distance
     * @param far    Far view plane distance
     */
    public void setPerspective(float fovY, float aspect, float near,
                                      float far) {
        if (Float.isNaN(aspect) || Float.isInfinite(aspect)) {
            // ignore.
            logger.log(Level.WARNING, "Invalid aspect given to setFrustumPerspective: {0}", aspect);
            return;
        }

        float h = FastMath.tan(fovY * FastMath.DEG_TO_RAD * .5f) * near;
        float w = h * aspect;
        this.left = -w;
        this.right = w;
        this.bottom = -h;
        this.top = h;
        this.near = near;
        this.far = far;

        // Camera is no longer parallel projection even if it was before
        camera.setParallelProjection(false);

        camera.onFrustumChange();
    }

}
