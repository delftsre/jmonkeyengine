/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.renderer;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.export.*;
import com.jme3.math.*;
import com.jme3.util.TempVars;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>Camera</code> is a standalone, purely mathematical class for doing
 * camera-related computations.
 *
 * <p>
 * Given input data such as location, orientation (direction, left, up),
 * and viewport settings, it can compute data necessary to render objects
 * with the graphics library. Two matrices are generated, the view matrix
 * transforms objects from world space into eye space, while the projection
 * matrix transforms objects from eye space into clip space.
 * </p>
 * <p>Another purpose of the camera class is to do frustum culling operations,
 * defined by six planes which define a 3D frustum shape, it is possible to
 * test if an object bounded by a mathematically defined volume is inside
 * the camera frustum, and thus to avoid rendering objects that are outside
 * the frustum
 * </p>
 *
 * @author Mark Powell
 * @author Joshua Slack
 */
public class Camera implements Savable, Cloneable {

    private static final Logger logger = Logger.getLogger(Camera.class.getName());

    /**
     * Camera's location
     */
    protected Vector3f location;
    /**
     * The orientation of the camera.
     */
    protected Quaternion rotation;

    public CameraFrustum frustum;

    //Temporary values computed in onFrustumChange that are needed if a
    //call is made to onFrameChange.
    protected float[] coeffLeft;
    protected float[] coeffRight;
    protected float[] coeffBottom;
    protected float[] coeffTop;

    public CameraViewport viewPort;

    /**
     * Array holding the planes that this camera will check for culling.
     */
    protected Plane[] worldPlane;
    /**
     * A mask value set during contains() that allows fast culling of a Node's
     * children.
     */
    private int planeState;
    protected int width;
    protected int height;
    protected boolean viewportChanged = true;
    /**
     * store the value for field parallelProjection
     */
    private boolean parallelProjection = true;
    protected Matrix4f projectionMatrixOverride = new Matrix4f();
    private boolean overrideProjection;
    protected Matrix4f viewMatrix = new Matrix4f();
    protected Matrix4f projectionMatrix = new Matrix4f();
    protected Matrix4f viewProjectionMatrix = new Matrix4f();
    private BoundingBox guiBounding = new BoundingBox();
    /** The camera's name. */
    protected String name;

    /**
     * Serialization only. Do not use.
     */
    public Camera() {
        worldPlane = new Plane[CameraFrustum.MAX_WORLD_PLANES];
        for (int i = 0; i < CameraFrustum.MAX_WORLD_PLANES; i++) {
            worldPlane[i] = new Plane();
        }
    }

    /**
     * Constructor instantiates a new <code>Camera</code> object. All
     * values of the camera are set to default.
     */
    public Camera(int width, int height) {
        this();
        location = new Vector3f();
        rotation = new Quaternion();

        frustum = new CameraFrustum(this, 1.0f, 2.0f, -0.5f, 0.5f, 0.5f, -0.5f);

        coeffLeft = new float[2];
        coeffRight = new float[2];
        coeffBottom = new float[2];
        coeffTop = new float[2];

        viewPort = new CameraViewport(this, 0.0f, 1.0f, 1.0f, 0.0f);

        this.width = width;
        this.height = height;

        onFrustumChange();
        onViewPortChange();
        onFrameChange();

        logger.log(Level.FINE, "Camera created (W: {0}, H: {1})", new Object[]{width, height});
    }

    @Override
    public Camera clone() {
        try {
            Camera cam = (Camera) super.clone();
            cam.viewportChanged = true;
            cam.planeState = 0;

            cam.worldPlane = new Plane[CameraFrustum.MAX_WORLD_PLANES];
            for (int i = 0; i < worldPlane.length; i++) {
                cam.worldPlane[i] = worldPlane[i].clone();
            }

            cam.coeffLeft = new float[2];
            cam.coeffRight = new float[2];
            cam.coeffBottom = new float[2];
            cam.coeffTop = new float[2];

            cam.location = location.clone();
            cam.rotation = rotation.clone();

            if (projectionMatrixOverride != null) {
                cam.projectionMatrixOverride = projectionMatrixOverride.clone();
            }

            cam.viewMatrix = viewMatrix.clone();
            cam.projectionMatrix = projectionMatrix.clone();
            cam.viewProjectionMatrix = viewProjectionMatrix.clone();
            cam.guiBounding = (BoundingBox) guiBounding.clone();

            cam.update();

            return cam;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }
    
	/**
         * This method copies the settings of the given camera.
    	 * 
	 * @param cam
	 *            the camera we copy the settings from
	 */
    public void copyFrom(Camera cam) {
    	location.set(cam.location);
        rotation.set(cam.rotation);

        frustum.copyFrom(cam.frustum);

        coeffLeft[0] = cam.coeffLeft[0];
        coeffLeft[1] = cam.coeffLeft[1];
        coeffRight[0] = cam.coeffRight[0];
        coeffRight[1] = cam.coeffRight[1];
        coeffBottom[0] = cam.coeffBottom[0];
        coeffBottom[1] = cam.coeffBottom[1];
        coeffTop[0] = cam.coeffTop[0];
        coeffTop[1] = cam.coeffTop[1];

        viewPort.copyFrom(cam.viewPort);

        this.width = cam.width;
        this.height = cam.height;
        
        this.planeState = 0;
        this.viewportChanged = true;
        for (int i = 0; i < CameraFrustum.MAX_WORLD_PLANES; ++i) {
            worldPlane[i].setNormal(cam.worldPlane[i].getNormal());
            worldPlane[i].setConstant(cam.worldPlane[i].getConstant());
        }
        
        this.parallelProjection = cam.parallelProjection;
        this.overrideProjection = cam.overrideProjection;
        this.projectionMatrixOverride.set(cam.projectionMatrixOverride);
        
        this.viewMatrix.set(cam.viewMatrix);
        this.projectionMatrix.set(cam.projectionMatrix);
        this.viewProjectionMatrix.set(cam.viewProjectionMatrix);

        this.guiBounding.setXExtent(cam.guiBounding.getXExtent());
        this.guiBounding.setYExtent(cam.guiBounding.getYExtent());
        this.guiBounding.setZExtent(cam.guiBounding.getZExtent());
        this.guiBounding.setCenter(cam.guiBounding.getCenter());
        this.guiBounding.setCheckPlane(cam.guiBounding.getCheckPlane());

        this.name = cam.name;
    }

    /**
     * This method sets the cameras name.
     * @param name the cameras name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method returns the cameras name.
     * @return the cameras name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a clipPlane for this camera.
     * The clipPlane is used to recompute the
     * projectionMatrix using the plane as the near plane     
     * This technique is known as the oblique near-plane clipping method introduced by Eric Lengyel
     * more info here
     * <ul>
     * <li><a href="http://www.terathon.com/code/oblique.html">http://www.terathon.com/code/oblique.html</a>
     * <li><a href="http://aras-p.info/texts/obliqueortho.html">http://aras-p.info/texts/obliqueortho.html</a>
     * <li><a href="http://hacksoflife.blogspot.com/2008/12/every-now-and-then-i-come-across.html">http://hacksoflife.blogspot.com/2008/12/every-now-and-then-i-come-across.html</a>
     * </ul>
     *
     * Note that this will work properly only if it's called on each update, and be aware that it won't work properly with the sky bucket.
     * if you want to handle the sky bucket, look at how it's done in SimpleWaterProcessor.java
     * @param clipPlane the plane
     * @param side the side the camera stands from the plane
     */
    public void setClipPlane(Plane clipPlane, Plane.Side side) {     
        float sideFactor = 1;
        if (side == Plane.Side.Negative) {
            sideFactor = -1;
        }
        //we are on the other side of the plane no need to clip anymore.
        if (clipPlane.whichSide(location) == side) {
            return;
        }
        
        TempVars vars = TempVars.get();
        try {        
            Matrix4f p = projectionMatrixOverride.set(projectionMatrix);

            Matrix4f ivm = viewMatrix;

            Vector3f point = clipPlane.getNormal().mult(clipPlane.getConstant(), vars.vect1);
            Vector3f pp = ivm.mult(point, vars.vect2);
            Vector3f pn = ivm.multNormal(clipPlane.getNormal(), vars.vect3);
            Vector4f clipPlaneV = vars.vect4f1.set(pn.x * sideFactor, pn.y * sideFactor, pn.z * sideFactor, -(pp.dot(pn)) * sideFactor);
    
            Vector4f v = vars.vect4f2.set(0, 0, 0, 0);
    
            v.x = (Math.signum(clipPlaneV.x) + p.m02) / p.m00;
            v.y = (Math.signum(clipPlaneV.y) + p.m12) / p.m11;
            v.z = -1.0f;
            v.w = (1.0f + p.m22) / p.m23;
    
            float dot = clipPlaneV.dot(v);//clipPlaneV.x * v.x + clipPlaneV.y * v.y + clipPlaneV.z * v.z + clipPlaneV.w * v.w;
            Vector4f c = clipPlaneV.multLocal(2.0f / dot);
    
            p.m20 = c.x - p.m30;
            p.m21 = c.y - p.m31;
            p.m22 = c.z - p.m32;
            p.m23 = c.w - p.m33;
            setProjectionMatrix(p);
        } finally {
            vars.release();
        }            
    }

    /**
     * Sets a clipPlane for this camera.
     * The cliPlane is used to recompute the projectionMatrix using the plane as the near plane
     * This technique is known as the oblique near-plane clipping method introduced by Eric Lengyel
     * more info here
     * <ul>
     * <li><a href="http://www.terathon.com/code/oblique.html">http://www.terathon.com/code/oblique.html</a></li>
     * <li><a href="http://aras-p.info/texts/obliqueortho.html">http://aras-p.info/texts/obliqueortho.html</a></li>
     * <li><a href="http://hacksoflife.blogspot.com/2008/12/every-now-and-then-i-come-across.html">
     * http://hacksoflife.blogspot.com/2008/12/every-now-and-then-i-come-across.html</a></li>
     * </ul>
     *
     * Note that this will work properly only if it's called on each update, and be aware that it won't work properly with the sky bucket.
     * if you want to handle the sky bucket, look at how it's done in SimpleWaterProcessor.java
     * @param clipPlane the plane
     */
    public void setClipPlane(Plane clipPlane) {
        setClipPlane(clipPlane, clipPlane.whichSide(location));
    }

    /**
     * Resizes this camera's view with the given width and height. This is
     * similar to constructing a new camera, but reusing the same Object. This
     * method is called by an associated {@link RenderManager} to notify the camera of
     * changes in the display dimensions.
     *
     * @param width the view width
     * @param height the view height
     * @param fixAspect If true, the camera's aspect ratio will be recomputed.
     * Recomputing the aspect ratio requires changing the frustum values.
     */
    public void resize(int width, int height, boolean fixAspect) {
        this.width = width;
        this.height = height;
        onViewPortChange();

        if (fixAspect /*&& !parallelProjection*/) {
            frustum.setRight(frustum.getTop() * ((float) width / height));
            frustum.setLeft(-frustum.getRight());
        }
    }

    /**
     * <code>getLocation</code> retrieves the location vector of the camera.
     *
     * @return the position of the camera.
     * @see Camera#getLocation()
     */
    public Vector3f getLocation() {
        return location;
    }

    /**
     * <code>getRotation</code> retrieves the rotation quaternion of the camera.
     *
     * @return the rotation of the camera.
     */
    public Quaternion getRotation() {
        return rotation;
    }

    /**
     * <code>getDirection</code> retrieves the direction vector the camera is
     * facing.
     *
     * @return the direction the camera is facing.
     * @see Camera#getDirection()
     */
    public Vector3f getDirection() {
        return rotation.getRotationColumn(2);
    }

    /**
     * <code>getLeft</code> retrieves the left axis of the camera.
     *
     * @return the left axis of the camera.
     * @see Camera#getLeft()
     */
    public Vector3f getLeft() {
        return rotation.getRotationColumn(0);
    }

    /**
     * <code>getUp</code> retrieves the up axis of the camera.
     *
     * @return the up axis of the camera.
     * @see Camera#getUp()
     */
    public Vector3f getUp() {
        return rotation.getRotationColumn(1);
    }

    /**
     * <code>getDirection</code> retrieves the direction vector the camera is
     * facing.
     *
     * @return the direction the camera is facing.
     * @see Camera#getDirection()
     */
    public Vector3f getDirection(Vector3f store) {
        return rotation.getRotationColumn(2, store);
    }

    /**
     * <code>getLeft</code> retrieves the left axis of the camera.
     *
     * @return the left axis of the camera.
     * @see Camera#getLeft()
     */
    public Vector3f getLeft(Vector3f store) {
        return rotation.getRotationColumn(0, store);
    }

    /**
     * <code>getUp</code> retrieves the up axis of the camera.
     *
     * @return the up axis of the camera.
     * @see Camera#getUp()
     */
    public Vector3f getUp(Vector3f store) {
        return rotation.getRotationColumn(1, store);
    }

    /**
     * <code>setLocation</code> sets the position of the camera.
     *
     * @param location the position of the camera.
     */
    public void setLocation(Vector3f location) {
        this.location.set(location);
        onFrameChange();
    }

    /**
     * <code>setRotation</code> sets the orientation of this camera. This will
     * be equivalent to setting each of the axes:
     * <code><br>
     * cam.setLeft(rotation.getRotationColumn(0));<br>
     * cam.setUp(rotation.getRotationColumn(1));<br>
     * cam.setDirection(rotation.getRotationColumn(2));<br>
     * </code>
     *
     * @param rotation the rotation of this camera
     */
    public void setRotation(Quaternion rotation) {
        this.rotation.set(rotation);
        onFrameChange();
    }

    /**
     * <code>lookAtDirection</code> sets the direction the camera is facing
     * given a direction and an up vector.
     *
     * @param direction the direction this camera is facing.
     */
    public void lookAtDirection(Vector3f direction, Vector3f up) {
        this.rotation.lookAt(direction, up);
        onFrameChange();
    }

    /**
     * <code>setAxes</code> sets the axes (left, up and direction) for this
     * camera.
     *
     * @param left      the left axis of the camera.
     * @param up        the up axis of the camera.
     * @param direction the direction the camera is facing.
     * 
     * @see Camera#setAxes(com.jme3.math.Quaternion) 
     */
    public void setAxes(Vector3f left, Vector3f up, Vector3f direction) {
        this.rotation.fromAxes(left, up, direction);
        onFrameChange();
    }

    /**
     * <code>setAxes</code> uses a rotational matrix to set the axes of the
     * camera.
     *
     * @param axes the matrix that defines the orientation of the camera.
     */
    public void setAxes(Quaternion axes) {
        this.rotation.set(axes);
        onFrameChange();
    }

    /**
     * normalize normalizes the camera vectors.
     */
    public void normalize() {
        this.rotation.normalizeLocal();
        onFrameChange();
    }

    /**
     * <code>setFrame</code> sets the orientation and location of the camera.
     *
     * @param location  the point position of the camera.
     * @param left      the left axis of the camera.
     * @param up        the up axis of the camera.
     * @param direction the facing of the camera.
     * @see Camera#setFrame(com.jme3.math.Vector3f,
     *      com.jme3.math.Vector3f, com.jme3.math.Vector3f, com.jme3.math.Vector3f)
     */
    public void setFrame(Vector3f location, Vector3f left, Vector3f up,
            Vector3f direction) {

        this.location = location;
        this.rotation.fromAxes(left, up, direction);
        onFrameChange();
    }

    /**
     * <code>lookAt</code> is a convenience method for auto-setting the frame
     * based on a world position the user desires the camera to look at. It
     * repoints the camera towards the given position using the difference
     * between the position and the current camera location as a direction
     * vector and the worldUpVector to compute up and left camera vectors.
     *
     * @param pos           where to look at in terms of world coordinates
     * @param worldUpVector a normalized vector indicating the up direction of the world.
     *                      (typically {0, 1, 0} in jME.)
     */
    public void lookAt(Vector3f pos, Vector3f worldUpVector) {
        TempVars vars = TempVars.get();
        Vector3f newDirection = vars.vect1;
        Vector3f newUp = vars.vect2;
        Vector3f newLeft = vars.vect3;

        newDirection.set(pos).subtractLocal(location).normalizeLocal();

        newUp.set(worldUpVector).normalizeLocal();
        if (newUp.equals(Vector3f.ZERO)) {
            newUp.set(Vector3f.UNIT_Y);
        }

        newLeft.set(newUp).crossLocal(newDirection).normalizeLocal();
        if (newLeft.equals(Vector3f.ZERO)) {
            if (newDirection.x != 0) {
                newLeft.set(newDirection.y, -newDirection.x, 0f);
            } else {
                newLeft.set(0f, newDirection.z, -newDirection.y);
            }
        }

        newUp.set(newDirection).crossLocal(newLeft).normalizeLocal();

        this.rotation.fromAxes(newLeft, newUp, newDirection);
        this.rotation.normalizeLocal();
        vars.release();

        onFrameChange();
    }

    /**
     * <code>setFrame</code> sets the orientation and location of the camera.
     * 
     * @param location
     *            the point position of the camera.
     * @param axes
     *            the orientation of the camera.
     */
    public void setFrame(Vector3f location, Quaternion axes) {
        this.location = location;
        this.rotation.set(axes);
        onFrameChange();
    }

    /**
     * <code>update</code> updates the camera parameters by calling
     * <code>onFrustumChange</code>,<code>onViewPortChange</code> and
     * <code>onFrameChange</code>.
     *
     * @see Camera#update()
     */
    public void update() {
        onFrustumChange();
        onViewPortChange();
        //...this is always called by onFrustumChange()
        //onFrameChange();
    }

    /**
     * <code>getPlaneState</code> returns the state of the frustum planes. So
     * checks can be made as to which frustum plane has been examined for
     * culling thus far.
     *
     * @return the current plane state int.
     */
    public int getPlaneState() {
        return planeState;
    }

    /**
     * <code>setPlaneState</code> sets the state to keep track of tested
     * planes for culling.
     *
     * @param planeState the updated state.
     */
    public void setPlaneState(int planeState) {
        this.planeState = planeState;
    }

    /**
     * Returns the pseudo distance from the given position to the near
     * plane of the camera. This is used for render queue sorting.
     * @param pos The position to compute a distance to.
     * @return Distance from the far plane to the point.
     */
    public float distanceToNearPlane(Vector3f pos) {
        return worldPlane[CameraFrustum.NEAR_PLANE].pseudoDistance(pos);
    }
    
    public Plane getWorldPlane(int planeId) {
        return worldPlane[planeId];
    }
    
    /**
     * <code>containsGui</code> tests a bounding volume against the ortho
     * bounding box of the camera. A bounding box spanning from
     * 0, 0 to Width, Height. Constrained by the viewport settings on the
     * camera.
     *
     * @param bound the bound to check for culling
     * @return True if the camera contains the gui element bounding volume.
     */
    public boolean containsGui(BoundingVolume bound) {
        if (bound == null) {
            return true;
        }
        return guiBounding.intersects(bound);
    }

    /**
     * @return the view matrix of the camera.
     * The view matrix transforms world space into eye space.
     * This matrix is usually defined by the position and
     * orientation of the camera.
     */
    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    /**
     * Overrides the projection matrix used by the camera. Will
     * use the matrix for computing the view projection matrix as well.
     * Use null argument to return to normal functionality.
     *
     * @param projMatrix
     */
    public void setProjectionMatrix(Matrix4f projMatrix) {
        if (projMatrix == null) {
            overrideProjection = false;
            projectionMatrixOverride.loadIdentity();   
        } else {
            overrideProjection = true;            
            projectionMatrixOverride.set(projMatrix);
        }            
        updateViewProjection();
    }

    /**
     * @return the projection matrix of the camera.
     * The view projection matrix  transforms eye space into clip space.
     * This matrix is usually defined by the viewport and perspective settings
     * of the camera.
     */
    public Matrix4f getProjectionMatrix() {
        if (overrideProjection) {
            return projectionMatrixOverride;
        }

        return projectionMatrix;
    }

    /**
     * Updates the view projection matrix.
     */
    public void updateViewProjection() {
        if (overrideProjection) {
            viewProjectionMatrix.set(projectionMatrixOverride).multLocal(viewMatrix);
        } else {
            //viewProjectionMatrix.set(viewMatrix).multLocal(projectionMatrix);
            viewProjectionMatrix.set(projectionMatrix).multLocal(viewMatrix);
        }
    }

    /**
     * @return The result of multiplying the projection matrix by the view
     * matrix. This matrix is required for rendering an object. It is
     * precomputed so as to not compute it every time an object is rendered.
     */
    public Matrix4f getViewProjectionMatrix() {
        return viewProjectionMatrix;
    }

    /**
     * @return True if the viewport (width, height, left, right, bottom, up)
     * has been changed. This is needed in the renderer so that the proper
     * viewport can be set-up.
     */
    public boolean isViewportChanged() {
        return viewportChanged;
    }

    /**
     * Clears the viewport changed flag once it has been updated inside
     * the renderer.
     */
    public void clearViewportChanged() {
        viewportChanged = false;
    }

    /**
     * Called when the viewport has been changed.
     */
    public void onViewPortChange() {
        viewportChanged = true;
        setGuiBounding();
    }

    private void setGuiBounding() {
        float sx = width * viewPort.getLeft();
        float ex = width * viewPort.getRight();
        float sy = height * viewPort.getBottom();
        float ey = height * viewPort.getTop();
        float xExtent = Math.max(0f, (ex - sx) / 2f);
        float yExtent = Math.max(0f, (ey - sy) / 2f);
        guiBounding.setCenter(sx + xExtent, sy + yExtent, 0);
        guiBounding.setXExtent(xExtent);
        guiBounding.setYExtent(yExtent);
        guiBounding.setZExtent(Float.MAX_VALUE);
    }

    /**
     * <code>onFrustumChange</code> updates the frustum to reflect any changes
     * made to the planes. The new frustum values are kept in a temporary
     * location for use when calculating the new frame. The projection
     * matrix is updated to reflect the current values of the frustum.
     */
    public void onFrustumChange() {
        if (!isParallelProjection()) {
            float nearSquared = frustum.getNear() * frustum.getNear();
            float leftSquared = frustum.getLeft() * frustum.getLeft();
            float rightSquared = frustum.getRight() * frustum.getRight();
            float bottomSquared = frustum.getBottom() * frustum.getBottom();
            float topSquared = frustum.getTop() * frustum.getTop();

            float inverseLength = FastMath.invSqrt(nearSquared + leftSquared);
            coeffLeft[0] = -frustum.getNear() * inverseLength;
            coeffLeft[1] = -frustum.getLeft() * inverseLength;

            inverseLength = FastMath.invSqrt(nearSquared + rightSquared);
            coeffRight[0] = frustum.getNear() * inverseLength;
            coeffRight[1] = frustum.getRight() * inverseLength;

            inverseLength = FastMath.invSqrt(nearSquared + bottomSquared);
            coeffBottom[0] = frustum.getNear() * inverseLength;
            coeffBottom[1] = -frustum.getBottom() * inverseLength;

            inverseLength = FastMath.invSqrt(nearSquared + topSquared);
            coeffTop[0] = -frustum.getNear() * inverseLength;
            coeffTop[1] = frustum.getTop() * inverseLength;
        } else {
            coeffLeft[0] = 1;
            coeffLeft[1] = 0;

            coeffRight[0] = -1;
            coeffRight[1] = 0;

            coeffBottom[0] = 1;
            coeffBottom[1] = 0;

            coeffTop[0] = -1;
            coeffTop[1] = 0;
        }

        projectionMatrix.fromFrustum(frustum.getNear(), frustum.getFar(), frustum.getLeft(), frustum.getRight(), frustum.getTop(), frustum.getBottom(), parallelProjection);
//        projectionMatrix.transposeLocal();

        // The frame is effected by the frustum values
        // update it as well
        onFrameChange();
    }

    /**
     * <code>onFrameChange</code> updates the view frame of the camera.
     */
    public void onFrameChange() {
        TempVars vars = TempVars.get();
        
        Vector3f left = getLeft(vars.vect1);
        Vector3f direction = getDirection(vars.vect2);
        Vector3f up = getUp(vars.vect3);

        float dirDotLocation = direction.dot(location);

        // left plane
        Vector3f leftPlaneNormal = worldPlane[CameraFrustum.LEFT_PLANE].getNormal();
        leftPlaneNormal.x = left.x * coeffLeft[0];
        leftPlaneNormal.y = left.y * coeffLeft[0];
        leftPlaneNormal.z = left.z * coeffLeft[0];
        leftPlaneNormal.addLocal(direction.x * coeffLeft[1], direction.y
                * coeffLeft[1], direction.z * coeffLeft[1]);
        worldPlane[CameraFrustum.LEFT_PLANE].setConstant(location.dot(leftPlaneNormal));

        // right plane
        Vector3f rightPlaneNormal = worldPlane[CameraFrustum.RIGHT_PLANE].getNormal();
        rightPlaneNormal.x = left.x * coeffRight[0];
        rightPlaneNormal.y = left.y * coeffRight[0];
        rightPlaneNormal.z = left.z * coeffRight[0];
        rightPlaneNormal.addLocal(direction.x * coeffRight[1], direction.y
                * coeffRight[1], direction.z * coeffRight[1]);
        worldPlane[CameraFrustum.RIGHT_PLANE].setConstant(location.dot(rightPlaneNormal));

        // bottom plane
        Vector3f bottomPlaneNormal = worldPlane[CameraFrustum.BOTTOM_PLANE].getNormal();
        bottomPlaneNormal.x = up.x * coeffBottom[0];
        bottomPlaneNormal.y = up.y * coeffBottom[0];
        bottomPlaneNormal.z = up.z * coeffBottom[0];
        bottomPlaneNormal.addLocal(direction.x * coeffBottom[1], direction.y
                * coeffBottom[1], direction.z * coeffBottom[1]);
        worldPlane[CameraFrustum.BOTTOM_PLANE].setConstant(location.dot(bottomPlaneNormal));

        // top plane
        Vector3f topPlaneNormal = worldPlane[CameraFrustum.TOP_PLANE].getNormal();
        topPlaneNormal.x = up.x * coeffTop[0];
        topPlaneNormal.y = up.y * coeffTop[0];
        topPlaneNormal.z = up.z * coeffTop[0];
        topPlaneNormal.addLocal(direction.x * coeffTop[1], direction.y
                * coeffTop[1], direction.z * coeffTop[1]);
        worldPlane[CameraFrustum.TOP_PLANE].setConstant(location.dot(topPlaneNormal));

        if (isParallelProjection()) {
            worldPlane[CameraFrustum.LEFT_PLANE].setConstant(worldPlane[CameraFrustum.LEFT_PLANE].getConstant() + frustum.getLeft());
            worldPlane[CameraFrustum.RIGHT_PLANE].setConstant(worldPlane[CameraFrustum.RIGHT_PLANE].getConstant() - frustum.getRight());
            worldPlane[CameraFrustum.TOP_PLANE].setConstant(worldPlane[CameraFrustum.TOP_PLANE].getConstant() - frustum.getTop());
            worldPlane[CameraFrustum.BOTTOM_PLANE].setConstant(worldPlane[CameraFrustum.BOTTOM_PLANE].getConstant() + frustum.getBottom());
        }

        // far plane
        worldPlane[CameraFrustum.FAR_PLANE].setNormal(left);
        worldPlane[CameraFrustum.FAR_PLANE].setNormal(-direction.x, -direction.y, -direction.z);
        worldPlane[CameraFrustum.FAR_PLANE].setConstant(-(dirDotLocation + frustum.getFar()));

        // near plane
        worldPlane[CameraFrustum.NEAR_PLANE].setNormal(direction.x, direction.y, direction.z);
        worldPlane[CameraFrustum.NEAR_PLANE].setConstant(dirDotLocation + frustum.getNear());

        viewMatrix.fromFrame(location, direction, up, left);
        
        vars.release();
        
//        viewMatrix.transposeLocal();
        updateViewProjection();
    }

    /**
     * @return true if parallel projection is enable, false if in normal perspective mode
     * @see #setParallelProjection(boolean)
     */
    public boolean isParallelProjection() {
        return this.parallelProjection;
    }

    /**
     * Enable/disable parallel projection.
     *
     * @param value true to set up this camera for parallel projection is enable, false to enter normal perspective mode
     */
    public void setParallelProjection(final boolean value) {
        this.parallelProjection = value;
        onFrustumChange();
    }

    /**
     * Computes the z value in projection space from the z value in view space 
     * Note that the returned value is going non linearly from 0 to 1.
     * for more explanations on non linear z buffer see
     * http://www.sjbaker.org/steve/omniv/love_your_z_buffer.html
     * @param viewZPos the z value in view space.
     * @return the z value in projection space.
     */
    public float getViewToProjectionZ(float viewZPos) {
        float far = frustum.getFar();
        float near = frustum.getNear();
        float a = far / (far - near);
        float b = far * near / (near - far);
        return a + b / viewZPos;
    }

    /**
     * Computes a position in World space given a screen position in screen space (0,0 to width, height)
     * and a z position in projection space ( 0 to 1 non linear).
     * This former value is also known as the Z buffer value or non linear depth buffer.
     * for more explanations on non linear z buffer see
     * http://www.sjbaker.org/steve/omniv/love_your_z_buffer.html
     * 
     * To compute the projection space z from the view space z (distance from cam to object) @see Camera#getViewToProjectionZ
     * 
     * @param screenPos 2d coordinate in screen space
     * @param projectionZPos non linear z value in projection space
     * @return the position in world space.
     */
    public Vector3f getWorldCoordinates(Vector2f screenPos, float projectionZPos) {
        return getWorldCoordinates(screenPos, projectionZPos, null);
    }

    /**
     * @see Camera#getWorldCoordinates
     */
    public Vector3f getWorldCoordinates(Vector2f screenPosition,
            float projectionZPos, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
 
        Matrix4f inverseMat = new Matrix4f(viewProjectionMatrix);
        inverseMat.invertLocal();

        store.set(
                (screenPosition.x / getWidth() - viewPort.getLeft()) / (viewPort.getRight() - viewPort.getLeft()) * 2 - 1,
                (screenPosition.y / getHeight() - viewPort.getBottom()) / (viewPort.getTop() - viewPort.getBottom()) * 2 - 1,
                projectionZPos * 2 - 1);

        float w = inverseMat.multProj(store, store);      
        store.multLocal(1f / w);

        return store;
    }

    /**
     * Converts the given position from world space to screen space.
     * 
     * @see Camera#getScreenCoordinates
     */
    public Vector3f getScreenCoordinates(Vector3f worldPos) {
        return getScreenCoordinates(worldPos, null);
    }

    /**
     * Converts the given position from world space to screen space.
     *
     * @see Camera#getScreenCoordinates(Vector3f, Vector3f)
     */
    public Vector3f getScreenCoordinates(Vector3f worldPosition, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }

//        TempVars vars = vars.lock();
//        Quaternion tmp_quat = vars.quat1;
//        tmp_quat.set( worldPosition.x, worldPosition.y, worldPosition.z, 1 );
//        viewProjectionMatrix.mult(tmp_quat, tmp_quat);
//        tmp_quat.multLocal( 1.0f / tmp_quat.getW() );
//        store.x = ( ( tmp_quat.getX() + 1 ) * ( viewPortRight - viewPortLeft ) / 2 + viewPortLeft ) * getWidth();
//        store.y = ( ( tmp_quat.getY() + 1 ) * ( viewPortTop - viewPortBottom ) / 2 + viewPortBottom ) * getHeight();
//        store.z = ( tmp_quat.getZ() + 1 ) / 2;
//        vars.release();

        float w = viewProjectionMatrix.multProj(worldPosition, store);
        store.divideLocal(w);

        store.x = ((store.x + 1f) * (viewPort.getRight() - viewPort.getLeft()) / 2f + viewPort.getLeft()) * getWidth();
        store.y = ((store.y + 1f) * (viewPort.getTop() - viewPort.getBottom()) / 2f + viewPort.getBottom()) * getHeight();
        store.z = (store.z + 1f) / 2f;

        return store;
    }

    /**
     * @return the width/resolution of the display.
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return the height/resolution of the display.
     */
    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Camera[location=" + location + "\n, direction=" + getDirection() + "\n"
                + "res=" + width + "x" + height + ", parallel=" + parallelProjection + "\n"
                + "near=" + frustum.getNear() + ", far=" + frustum.getFar() + "]";
    }

    public void write(JmeExporter e) throws IOException {
        OutputCapsule capsule = e.getCapsule(this);
        capsule.write(location, "location", Vector3f.ZERO);
        capsule.write(rotation, "rotation", Quaternion.DIRECTION_Z);
        capsule.write(frustum.getNear(), "frustumNear", 1);
        capsule.write(frustum.getFar(), "frustumFar", 2);
        capsule.write(frustum.getLeft(), "frustumLeft", -0.5f);
        capsule.write(frustum.getRight(), "frustumRight", 0.5f);
        capsule.write(frustum.getTop(), "frustumTop", 0.5f);
        capsule.write(frustum.getBottom(), "frustumBottom", -0.5f);
        capsule.write(coeffLeft, "coeffLeft", new float[2]);
        capsule.write(coeffRight, "coeffRight", new float[2]);
        capsule.write(coeffBottom, "coeffBottom", new float[2]);
        capsule.write(coeffTop, "coeffTop", new float[2]);
        capsule.write(viewPort.getLeft(), "viewPortLeft", 0);
        capsule.write(viewPort.getRight(), "viewPortRight", 1);
        capsule.write(viewPort.getTop(), "viewPortTop", 1);
        capsule.write(viewPort.getBottom(), "viewPortBottom", 0);
        capsule.write(width, "width", 0);
        capsule.write(height, "height", 0);
        capsule.write(name, "name", null);
    }

    public void read(JmeImporter e) throws IOException {
        InputCapsule capsule = e.getCapsule(this);
        location = (Vector3f) capsule.readSavable("location", Vector3f.ZERO.clone());
        rotation = (Quaternion) capsule.readSavable("rotation", Quaternion.DIRECTION_Z.clone());
        frustum.set(
            capsule.readFloat("frustumNear", 1),
            capsule.readFloat("frustumFar", 2),
            capsule.readFloat("frustumLeft", -0.5f),
            capsule.readFloat("frustumRight", 0.5f),
            capsule.readFloat("frustumTop", 0.5f),
            capsule.readFloat("frustumBottom", -0.5f)
        );
        coeffLeft = capsule.readFloatArray("coeffLeft", new float[2]);
        coeffRight = capsule.readFloatArray("coeffRight", new float[2]);
        coeffBottom = capsule.readFloatArray("coeffBottom", new float[2]);
        coeffTop = capsule.readFloatArray("coeffTop", new float[2]);
        viewPort.set(
            capsule.readFloat("viewPortLeft", 0),
            capsule.readFloat("viewPortRight", 1),
            capsule.readFloat("viewPortTop", 1),
            capsule.readFloat("viewPortBottom", 0)
        );
        width = capsule.readInt("width", 1);
        height = capsule.readInt("height", 1);
        name = capsule.readString("name", null);
        onFrustumChange();
        onViewPortChange();
        onFrameChange();
    }
}
