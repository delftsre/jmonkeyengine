package com.jme3.renderer;

import com.jme3.bounding.BoundingBox;

public class DisplayViewPort {

	
	  //view port coordinates
    /**
     * Percent value on display where horizontal viewing starts for this camera.
     * Default is 0.
     */
    private float viewPortLeft;
    /**
     * Percent value on display where horizontal viewing ends for this camera.
     * Default is 1.
     */
    private float viewPortRight;
    /**
     * Percent value on display where vertical viewing ends for this camera.
     * Default is 1.
     */
    private float viewPortTop;
    /**
     * Percent value on display where vertical viewing begins for this camera.
     * Default is 0.
     */
    private float viewPortBottom;
    
    public float getLeft(){
    	return viewPortLeft;
    }
    
    public float getRight(){
    	return viewPortRight;
    }
    
    public float getTop(){
    	return viewPortTop;
    }
    
    public float getBottom(){
    	return viewPortBottom;
    }
    
    
    public void setLeft(float value){
    	 viewPortLeft=value;
    }
    
    public void setRight(float value){
    	 viewPortRight=value;
    }
    
    public void setTop(float value){
    	 viewPortTop =value;
    }
    
    public void setBottom(float value){
    	viewPortBottom=value;
    }
    
    public DisplayViewPort(float viewPortLeft,float  viewPortRight,float viewPortTop, float viewPortBottom){
    	 this.viewPortLeft = viewPortLeft;
    	 this.viewPortRight = viewPortRight;
         this.viewPortTop = viewPortTop;
         this.viewPortBottom = viewPortBottom;
    }
    
    public void setDisplayViewPort(float viewPortLeft,float  viewPortRight,float viewPortTop, float viewPortBottom){
   	 this.viewPortLeft = viewPortLeft;
   	 this.viewPortRight = viewPortRight;
        this.viewPortTop = viewPortTop;
        this.viewPortBottom = viewPortBottom;
   }
    
    public void copySettingFrom(DisplayViewPort cloneModel){
    	 this.viewPortLeft = cloneModel.viewPortLeft;
    	 this.viewPortRight = cloneModel.viewPortRight;
    	 this.viewPortTop = cloneModel.viewPortTop;
    	 this.viewPortBottom = cloneModel.viewPortBottom;
    }
    
    public void setGuiBounding(int width, int height, BoundingBox guiBounding){
    	 float sx = width * viewPortLeft;
         float ex = width * viewPortRight;
         float sy = height * viewPortBottom;
         float ey = height * viewPortTop;
         float xExtent = Math.max(0f, (ex - sx) / 2f);
         float yExtent = Math.max(0f, (ey - sy) / 2f);
         guiBounding.setCenter(sx + xExtent, sy + yExtent, 0);
         guiBounding.setXExtent(xExtent);
         guiBounding.setYExtent(yExtent);
         guiBounding.setZExtent(Float.MAX_VALUE);
    }
    
    public float storeXValue(){
    	return (viewPortRight - viewPortLeft) / 2f + viewPortLeft;
    }
    
    public float storeYValue(){
    	return (viewPortTop - viewPortBottom) / 2f + viewPortBottom;
    }
    
    public float[] getViewPorts(){
    	float[] result = {viewPortLeft,viewPortRight,viewPortTop,viewPortBottom};
    	return result;
    }

	
    
}
