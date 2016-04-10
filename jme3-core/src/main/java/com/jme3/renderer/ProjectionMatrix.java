package com.jme3.renderer;

import com.jme3.math.Matrix4f;

public class ProjectionMatrix {

	 protected Matrix4f projectionMatrixOverride = new Matrix4f();
	 protected Matrix4f viewMatrix = new Matrix4f();
	 protected Matrix4f projectionMatrix = new Matrix4f();
	 protected Matrix4f viewProjectionMatrix = new Matrix4f();
	 
	 public Matrix4f getviewProjectionMatrix(){
		 return viewProjectionMatrix;
	 }
	 
	 public void cloneFrom(ProjectionMatrix original){
		 if (projectionMatrixOverride != null) {
             this.projectionMatrixOverride = (Matrix4f) original.projectionMatrixOverride.clone();
         }
		 this.viewMatrix = (Matrix4f) original.viewMatrix.clone();
		 this.projectionMatrix = (Matrix4f) original.projectionMatrix.clone();
		 this.viewProjectionMatrix = (Matrix4f) original.viewProjectionMatrix.clone();
		 
	 }


	public void copySettingsFrom(ProjectionMatrix theProjectionMatrix) {
		 this.projectionMatrixOverride.set(theProjectionMatrix.projectionMatrixOverride);
	        
	        this.viewMatrix.set(theProjectionMatrix.viewMatrix);
	        this.projectionMatrix.set(theProjectionMatrix.projectionMatrix);
	        this.viewProjectionMatrix.set(theProjectionMatrix.viewProjectionMatrix);
		
	}
	 
	public Matrix4f getViewMatrix(){
		return viewMatrix;
	}
	
	public Matrix4f getSetMatrix(){
		return (Matrix4f) projectionMatrixOverride.set(projectionMatrix);
	}
	
	public Matrix4f getProjectionMatrixOverride(){
		return projectionMatrixOverride;
	}
	
	public Matrix4f getProjectionMatrix(){
		return projectionMatrix;
	}
	
	public void updateViewProjection(boolean overrideProjection) {
		 if (overrideProjection) {
	            viewProjectionMatrix.set(projectionMatrixOverride).multLocal(viewMatrix);
	        } else {
	            //viewProjectionMatrix.set(viewMatrix).multLocal(projectionMatrix);
	            viewProjectionMatrix.set(projectionMatrix).multLocal(viewMatrix);
	        }
		
	}
	 
	 
}
