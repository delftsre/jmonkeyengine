package com.jme3.math;

public class Frustum {

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
    protected float frustumNear;
    /**
     * Distance from camera to far frustum plane.
     */
    protected float frustumFar;
    /**
     * Distance from camera to left frustum plane.
     */
    protected float frustumLeft;
    /**
     * Distance from camera to right frustum plane.
     */
    protected float frustumRight;
    /**
     * Distance from camera to top frustum plane.
     */
    protected float frustumTop;
    /**
     * Distance from camera to bottom frustum plane.
     */
    protected float frustumBottom;
    
    //TODO this looks like a red flag
    //Temporary values computed in onFrustumChange that are needed if a
    //call is made to onFrameChange.
    protected float[] coeffLeft;
    protected float[] coeffRight;
    protected float[] coeffBottom;
    protected float[] coeffTop;
    
    public float[][] getCoeffFirstValue(){
    	float[][] result = {coeffLeft, coeffRight, coeffBottom, coeffTop };
    	return result;
    }
    
   
    
    public Frustum( float frustumNear,float frustumFar,float frustumLeft,float frustumRight,float frustumTop,float frustumBottom){
    	this.frustumNear = frustumNear;
    	this.frustumFar=frustumFar;
    	this.frustumLeft=frustumLeft;
    	this.frustumRight=frustumRight;
    	this.frustumTop=frustumTop;
    	this.frustumBottom=frustumBottom;
    	
    	coeffLeft = new float[2];
        coeffRight = new float[2];
        coeffBottom = new float[2];
        coeffTop = new float[2];
    }
    
    
    public void resetTemporaryVariables(){
    	coeffLeft = new float[2];
        coeffRight = new float[2];
        coeffBottom = new float[2];
        coeffTop = new float[2];
    }
    
    public void setTemporaryVariables(float[] left, float[] right, float[] bottom, float[] top){
    	coeffLeft = left;
        coeffRight = right;
        coeffBottom = bottom;
        coeffTop = top;
    }
    
    public void copySettingFrom( Frustum modelFrustum){
    	 frustumNear = modelFrustum.frustumNear;
         frustumFar = modelFrustum.frustumFar;
         frustumLeft = modelFrustum.frustumLeft;
         frustumRight = modelFrustum.frustumRight;
         frustumTop = modelFrustum.frustumTop;
         frustumBottom = modelFrustum.frustumBottom;
         
         coeffLeft[0] = modelFrustum.coeffLeft[0];
         coeffLeft[1] = modelFrustum.coeffLeft[1];
         coeffRight[0] = modelFrustum.coeffRight[0];
         coeffRight[1] = modelFrustum.coeffRight[1];
         coeffBottom[0] = modelFrustum.coeffBottom[0];
         coeffBottom[1] = modelFrustum.coeffBottom[1];
         coeffTop[0] = modelFrustum.coeffTop[0];
         coeffTop[1] = modelFrustum.coeffTop[1];
    }
    
    public void fixAspect( int width,int height){
    	frustumRight = frustumTop * ((float) width / height);
        frustumLeft = -frustumRight;
        
    }
	
    
    public void setFrustum(float top, float bottom, float left, float right){
    	this.frustumTop = top;
    	this.frustumBottom = bottom;
    	this.frustumLeft = left;
    	this.frustumRight = right;
    }
    
    public void setFrustum(float top, float bottom, float left, float right,float near, float far){
    	setFrustum(top, bottom, left,right);
    	this.frustumNear = near;
    	this.frustumFar = far;
    }
    
    
    //TODO try to remove these
    public float getBottom(){
    	return frustumBottom;
    }
    
    public float getTop(){
    	return frustumTop;
    }
    
    public float getFar(){
    	return frustumFar;
    }
    
    public float getLeft(){
    	return frustumLeft;
    }
    
    public float getRight(){
    	return frustumRight;
    }
    
    public float getNear(){
    	return frustumNear;
    }
    
   
    
    public void setFar(float far){
    	frustumFar = far;
    }
    
    public void setNear(float near){
    	frustumFar = near;
    }
    
    
    /**
     * <code>onFrustumChange</code> updates the frustum to reflect any changes
     * made to the planes. The new frustum values are kept in a temporary
     * location for use when calculating the new frame. The projection
     * matrix is updated to reflect the current values of the frustum.
     */
    public void onFrustumChange(boolean parallelProjection) {
        if (!parallelProjection) {
            float nearSquared = frustumNear * frustumNear;
            float leftSquared = frustumLeft * frustumLeft;
            float rightSquared = frustumRight * frustumRight;
            float bottomSquared = frustumBottom * frustumBottom;
            float topSquared = frustumTop * frustumTop;

            float inverseLength = FastMath.invSqrt(nearSquared + leftSquared);
            coeffLeft[0] = -frustumNear * inverseLength;
            coeffLeft[1] = -frustumLeft * inverseLength;

            inverseLength = FastMath.invSqrt(nearSquared + rightSquared);
            coeffRight[0] = frustumNear * inverseLength;
            coeffRight[1] = frustumRight * inverseLength;

            inverseLength = FastMath.invSqrt(nearSquared + bottomSquared);
            coeffBottom[0] = frustumNear * inverseLength;
            coeffBottom[1] = -frustumBottom * inverseLength;

            inverseLength = FastMath.invSqrt(nearSquared + topSquared);
            coeffTop[0] = -frustumNear * inverseLength;
            coeffTop[1] = frustumTop * inverseLength;
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
    }
	
}
