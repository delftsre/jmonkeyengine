package com.jme3.renderer;

public class CameraViewport {

    private Camera camera;

    //view port coordinates
    /**
     * Percent value on display where horizontal viewing starts for this camera.
     * Default is 0.
     */
    private float left;
    /**
     * Percent value on display where horizontal viewing ends for this camera.
     * Default is 1.
     */
    private float right;
    /**
     * Percent value on display where vertical viewing ends for this camera.
     * Default is 1.
     */
    private float top;
    /**
     * Percent value on display where vertical viewing begins for this camera.
     * Default is 0.
     */
    private float bottom;

    public CameraViewport(Camera camera, float left, float right, float top, float bottom) {
        this.camera = camera;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public void set(float left, float right, float top, float bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        camera.onViewPortChange();
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
        camera.onViewPortChange();
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
        camera.onViewPortChange();
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
        camera.onViewPortChange();
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
        camera.onViewPortChange();
    }

    public void copyFrom(CameraViewport other) {
        this.left = other.left;
        this.right = other.right;
        this.top = other.top;
        this.bottom = other.bottom;
        camera.onViewPortChange();
    }


}
