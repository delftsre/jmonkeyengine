package com.jme3.renderer;

import java.util.Queue;

import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.scene.SceneGraphVisitor;
import com.jme3.scene.Spatial;

public class SpatialImpl extends Spatial{
	
	public SpatialImpl(){
		super();
	}
	

	@Override
	public int collideWith(Collidable other, CollisionResults results) throws UnsupportedCollisionException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateModelBound() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setModelBound(BoundingVolume modelBound) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getVertexCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTriangleCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Spatial deepClone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void depthFirstTraversal(SceneGraphVisitor visitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void breadthFirstTraversal(SceneGraphVisitor visitor, Queue<Spatial> queue) {
		// TODO Auto-generated method stub
		
	}

}
