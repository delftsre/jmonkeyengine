package com.jme3.renderer;

import java.nio.FloatBuffer;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.math.Matrix4f;
import com.jme3.math.Plane;
import com.jme3.math.Plane.Side;
import com.jme3.math.Ray;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;

/**
 * Mocks a BoundingVolume class
 * @author wiebe
 *
 */
public class BoundMock extends BoundingVolume {
	/**
	 * Protected to allow direct interaction with the variable from test classes
	 */
	protected int checkPlane;
	
	/**
	 * Returns checkPlane
	 */
	public int getCheckPlane(){
		return checkPlane;
	}
	
	/**
	 * Returns a Side based on first parameter
	 * of the plane's Normal
	 */
	@Override
	public Side whichSide(Plane p){
		float c = p.getNormal().x;
		if(c <= 4){
			return Side.Positive;
		}
		else if(c == 5){
			return Side.Negative;
		}
		else{
			return Side.None;
		}
	}
	
	@Override
	public int collideWith(Collidable other, CollisionResults results) throws UnsupportedCollisionException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Type getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoundingVolume transform(Transform trans, BoundingVolume store) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoundingVolume transform(Matrix4f trans, BoundingVolume store) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void computeFromPoints(FloatBuffer points) {
		// TODO Auto-generated method stub

	}

	@Override
	public BoundingVolume merge(BoundingVolume volume) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoundingVolume mergeLocal(BoundingVolume volume) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoundingVolume clone(BoundingVolume store) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float distanceToEdge(Vector3f point) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean intersects(BoundingVolume bv) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Ray ray) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersectsSphere(BoundingSphere bs) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersectsBoundingBox(BoundingBox bb) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean contains(Vector3f point) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean intersects(Vector3f point) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public float getVolume() {
		// TODO Auto-generated method stub
		return 0;
	}

}
