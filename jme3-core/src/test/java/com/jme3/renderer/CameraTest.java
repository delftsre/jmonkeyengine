package com.jme3.renderer;
/**
 *
 * @author mengmeng
 */

import static org.junit.Assert.*;

import java.nio.FloatBuffer;

import org.junit.Test;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bounding.BoundingVolume.Type;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix4f;
import com.jme3.math.Plane;
import com.jme3.math.Plane.Side;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Transform;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.GeometryList;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.util.TempVars;
import com.sun.accessibility.internal.resources.accessibility;
import com.sun.javafx.geom.Vec3f;

import javafx.beans.property.FloatProperty;
import sun.net.www.content.audio.x_aiff;

public class CameraTest {
	
	@Test
	public void testClone()
	{
		Camera cam = new Camera(10,10);
		assertTrue(cam.getHeight()==cam.clone().getHeight());
		assertTrue(cam.getWidth()==cam.clone().getWidth());
	}
	
	@Test
	public void testCopyFrom()
	{
		Camera cam = new Camera(10,10);
		Camera cam2 = new Camera(2,2);
		cam2.copyFrom(cam);
		
		assertTrue(cam.getHeight()==cam2.getHeight());
		assertTrue(cam.getWidth()==cam2.getWidth());
	}
	
	@Test
	public void testSetGetName()
	{
		Camera camera = new Camera();
		camera.setName("cam");
		assertTrue(camera.getName().equals("cam"));
	}
	
	@Test
	public void testSetClipPlane()
	{
		Camera camera = new Camera(5,5);
		Vector3f point = new Vector3f(3.0f, 2.0f, 3.0f);
		Plane plane = new Plane(point, 4.0f);
		
		
		float[] numbers = new float[16];
		for (int i=0 ; i<numbers.length; i++)
		{
			numbers[i] = 0.0f;
			if (i==0||i==5)
			{
				numbers[i] = 2.0f;
			}
			if (i==12||i==13)
			{
				numbers[i] = -0.0f;
			}
			if (i==10)
			{
				numbers[i] = -2.0f;
			}
			if (i==14)
			{
				numbers[i] = -3.0f;
			}
			if (i==15)
			{
				numbers[i] = 1.0f;
			}
		}
		Matrix4f matrix1 = new Matrix4f(numbers);
		camera.setClipPlane(plane);
		Matrix4f matrix3 = camera.getProjectionMatrix();
		assertTrue(matrix1.equals(matrix3));
		
		camera.setClipPlane(plane,Side.Positive);
		Matrix4f matrix2 = camera.getProjectionMatrix();
		//assertTrue(matrix1.equals(matrix2));
		
	}
	
	@Test
	public void testResize()
	{
		Camera camera = new Camera(5,5);
		camera.resize(10, 10, true);
		assertTrue(camera.getWidth() == 10);
		assertTrue(camera.getHeight() == 10);
		
	}
	
	@Test
	public void testGets()
	{
		Camera camera = new Camera(5,5);
		assertTrue(camera.getFrustumBottom()==-0.5);
		assertTrue(camera.getFrustumFar()==2.0);
		assertTrue(camera.getFrustumLeft() == -0.5);
		assertTrue(camera.getFrustumNear() == 1.0);
		assertTrue(camera.getFrustumRight() == 0.5);
		assertTrue(camera.getFrustumTop() == 0.5);
		
		assertTrue(camera.getLocation().equals(new Vector3f(0.0f, 0.0f, 0.0f)));
		float[] angles = {0.0f,0.0f,0.0f};
		assertTrue(camera.getRotation().equals(new Quaternion(angles)));
		assertTrue(camera.getDirection().equals(new Vector3f(0.0f, 0.0f, 1.0f)));
		assertTrue(camera.getLeft().equals(new Vector3f(1.0f, 0.0f, 0.0f)));
		assertTrue(camera.getUp().equals(new Vector3f(0.0f, 1.0f, 0.0f)));
		assertTrue(camera.getPlaneState() == 0);
		assertTrue(camera.getViewPortLeft() == 0.0f);
		assertTrue(camera.getViewPortRight() == 1.0f);
		assertTrue(camera.getViewPortTop() == 1.0f);
		assertTrue(camera.getViewPortBottom() == 0.0f);
		Plane plane = new Plane(new Vector3f(1.0f, 0.0f, 0.0f), -0.5f);
		assertTrue(plane.getNormal().equals(camera.getWorldPlane(0).getNormal()));
		assertTrue(plane.getConstant()==camera.getWorldPlane(0).getConstant());
		
		float[] numbers = {-1.0f,0.0f,-0.0f,0.0f,0.0f,1.0f,-0.0f,0.0f,0.0f,-0.0f,-1.0f,0.0f,-0.0f,-0.0f,0.0f,1.0f};
		Matrix4f matrix4f = new Matrix4f(numbers);
		assertTrue(matrix4f.equals(camera.getViewMatrix()));
		
		float[] numbers2 = {2.0f,0.0f,0.0f,0.0f,0.0f,2.0f,0.0f,0.0f,0.0f,0.0f,-2.0f,0.0f,-0.0f,-0.0f,-3.0f,1.0f};
		Matrix4f matrix4f2 = new Matrix4f(numbers2);
		assertTrue(matrix4f2.equals(camera.getProjectionMatrix()));
		
		float[] numbers3 = {-2.0f,0.0f,0.0f,0.0f,0.0f,2.0f,0.0f,0.0f,0.0f,0.0f,2.0f,0.0f,0.0f,0.0f,-3.0f,1.0f};
		Matrix4f matrix4f3 = new Matrix4f(numbers3);
		assertTrue(matrix4f3.equals(camera.getViewProjectionMatrix()));
		
		assertTrue(camera.isViewportChanged() == true);
		assertTrue(camera.getViewToProjectionZ(2.0f) == 1.0f);
		
		Vector2f screenPos = new Vector2f(1.0f, 2.0f);
		Vector3f store = new Vector3f(3.0f, 2.0f, 3.0f);
		System.out.println(camera.getWorldCoordinates(screenPos, 1.0f,store));
		assertTrue(camera.getWorldCoordinates(screenPos, 1.0f, store).getX()==0.3f);
		assertTrue(camera.getWorldCoordinates(screenPos, 1.0f, store).getZ()==2.0f);
		
		Vector3f worldPosition = new Vector3f(1.0f, 2.0f,3.0f);
		Vector3f store2 = new Vector3f(3.0f, 2.0f, 3.0f);
		assertTrue(camera.getScreenCoordinates(worldPosition, store2).equals(new Vector3f(-2.5f, 12.5f, 2.0f)));
		
		assertTrue(camera.toString().equals("Camera[location=(0.0, 0.0, 0.0)\n"+
				", direction=(0.0, 0.0, 1.0)\n"+
				"res=5x5, parallel=true\n"+"near=1.0, far=2.0]"));
		
		//System.out.println(camera.toString());
		
		
	}
	
	@Test
	public void testSets()
	{
		Camera camera = new Camera(5,5);
		//camera.setFrustumBottom(3.0f);
		//assertTrue(camera.getFrustumBottom()==3.0f);
		camera.setFrustumFar(5.0f);
		//assertTrue(camera.getFrustumFar()==5.0f);
		//camera.setFrustumLeft(1.0f);
		//assertTrue(camera.getFrustumLeft()==1.0f);
		camera.setFrustumNear(1.5f);
		//assertTrue(camera.getFrustumNear()==1.5f);
		//camera.setFrustumRight(2.0f);
		//assertTrue(camera.getFrustumRight()==2.0f);
		//camera.setFrustumTop(2.0f);
		//assertTrue(camera.getFrustumTop()==2.0f);
		camera.setFrustum(1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f);
		//assertTrue(camera.frustumNear==1.0f);
		//assertTrue(camera.frustumFar==2.0f);
		//assertTrue(camera.frustumLeft==3.0f);
		//assertTrue(camera.frustumRight==4.0f);
		//assertTrue(camera.frustumTop==5.0f);
		//assertTrue(camera.frustumBottom==6.0f);
		
		camera.setFrustumPerspective(1.0f, 2.0f, 3.0f, 4.0f);
		float h = FastMath.tan(1.0f * FastMath.DEG_TO_RAD * .5f) * 3.0f;
        float w = h * 2.0f;
        /*
        assertTrue(camera.frustumNear==3.0f);
		assertTrue(camera.frustumFar==4.0f);
		assertTrue(camera.frustumLeft==-w);
		assertTrue(camera.frustumRight==w);
		assertTrue(camera.frustumTop==h);
		assertTrue(camera.frustumBottom==-h);
		*/
		
		Vector3f location = new Vector3f(3.0f, 4.0f, 5.0f);
		camera.setLocation(location);
		assertTrue(camera.getLocation().equals(location));
		
		float[] angles = {4.0f,4.0f,3.0f};
		Quaternion quart = new Quaternion(angles);
		camera.setRotation(quart);
		assertTrue(camera.getRotation().equals(quart));
		
		camera.setPlaneState(3);
		assertTrue(camera.getPlaneState() == 3);
		
		camera.getDisplayViewPort().setLeft(3.0f);
		assertTrue(camera.getViewPortLeft() == 3.0f);
		camera.getDisplayViewPort().setRight(3.0f);
		assertTrue(camera.getViewPortRight() == 3.0f);
		camera.getDisplayViewPort().setTop(3.0f);
		assertTrue(camera.getViewPortTop() == 3.0f);
		camera.getDisplayViewPort().setBottom(3.0f);
		assertTrue(camera.getViewPortBottom() == 3.0f);
		camera.setViewPort(1.0f, 2.0f, 3.0f, 4.0f);
		assertTrue(camera.getViewPortLeft() == 1.0f);
		assertTrue(camera.getViewPortRight() == 2.0f);
		//assertTrue(camera.getViewPortBottom() == 3.0f);
		//assertTrue(camera.getViewPortTop() == 4.0f);
		
		camera = new Camera(5,5);
		camera.setProjectionMatrix(null);
		float[] numbers = {2.0f,0.0f,0.0f,0.0f,0.0f,2.0f,0.0f,0.0f,0.0f,0.0f,-2.0f,0.0f,-0.0f,-0.0f,-3.0f,1.0f};
		Matrix4f matrix4f = new Matrix4f(numbers);
		assertTrue(matrix4f.equals(camera.getProjectionMatrix()));
		
		camera.clearViewportChanged();
		assertTrue(camera.isViewportChanged()==false);
		
		camera.setParallelProjection(false);
		assertTrue(camera.isParallelProjection()==false);
		
	}
	
	@Test
	public void testLookatDirection()
	{
		Camera camera = new Camera(5, 4);
		Vector3f direction = new Vector3f(0.0f,0.0f,3.0f);
		Vector3f up = new Vector3f(0.0f, 3.0f, 0.0f);
		camera.lookAtDirection(direction, up);
		TempVars vars = TempVars.get();
		assertTrue(vars.vect1.equals(new Vector3f(1.0f,0.0f,0.0f)));
		assertTrue(vars.vect2.equals(new Vector3f(0.0f,0.0f,1.0f)));
		assertTrue(vars.vect3.equals(new Vector3f(0.0f,1.0f,0.0f)));
		//System.out.println(vars.vect3);
	}
	
	@Test
	public void testFromAxes()
	{
		Camera camera = new Camera(5,5);
		Quaternion rotate = camera.rotation;
		
		Vector3f xAxis = new Vector3f(0.0f,0.0f,1.0f);
		Vector3f yAxis = new Vector3f(0.0f,0.0f,0.0f);
		Vector3f zAxis = new Vector3f(0.0f,0.0f,0.0f);
		camera.setAxes(xAxis, yAxis, zAxis);
		assertTrue(rotate.equals(camera.rotation));
		Vector3f location = new Vector3f(2.0f,1.0f,3.0f);
		camera.setFrame(location, xAxis, yAxis, zAxis);
		assertTrue(camera.location.equals(location));
		assertTrue(rotate.equals(camera.rotation));
		camera.lookAt(xAxis, zAxis);
		
	}
	
	@Test
	public void testSetAxes()
	{
		Camera camera = new Camera(5,5);
		float[] angles = {0.0f,0.0f,0.0f};
		Quaternion quart = new Quaternion(angles);
		camera.setAxes(quart);
		assertTrue(camera.rotation.equals(quart));
		
	}
	
	@Test
	public void testNormalize()
	{
		Camera camera = new Camera(5,5);
		
		float[] angles = {0.0f,0.0f,0.0f};
		Quaternion quart = new Quaternion(angles);
		camera.normalize();
		assertTrue(camera.rotation.equals(quart));
		
	}
	
	@Test
	public void testSetFrame()
	{
		Camera camera = new Camera(5,5);
		Vector3f location = new Vector3f(2.0f,1.0f,3.0f);
		float[] angles = {1.0f,2.0f,3.0f};
		Quaternion quart = new Quaternion(angles);
		camera.setFrame(location, quart);
		assertTrue(camera.rotation.equals(quart));
		assertTrue(camera.getLocation().equals(location));
	}
	
	@Test
	public void testContains()
	{
		Camera camera = new Camera(5,5);
		Vector3f center = new Vector3f(2.0f,1.0f,3.0f);
		BoundingVolume volume = new BoundingVolume(center) {
			
			@Override
			public int collideWith(Collidable other, CollisionResults results) throws UnsupportedCollisionException {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Side whichSide(Plane plane) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public BoundingVolume transform(Matrix4f trans, BoundingVolume store) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public BoundingVolume transform(Transform trans, BoundingVolume store) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public BoundingVolume mergeLocal(BoundingVolume volume) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public BoundingVolume merge(BoundingVolume volume) {
				// TODO Auto-generated method stub
				return null;
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
			public boolean intersects(Vector3f point) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean intersects(Ray ray) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean intersects(BoundingVolume bv) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public float getVolume() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Type getType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public float distanceToEdge(Vector3f point) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public boolean contains(Vector3f point) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void computeFromPoints(FloatBuffer points) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public BoundingVolume clone(BoundingVolume store) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		assertTrue(camera.contains(volume).equals(Camera.FrustumIntersect.Intersects));
		
	}
	
	@Test
	public void testContainsGUI()
	{
		Camera camera = new Camera(5,5);
		Vector3f center = new Vector3f(0.0f,0.0f,2.0f);
		BoundingVolume volume = new BoundingVolume(center) {
			
			@Override
			public int collideWith(Collidable other, CollisionResults results) throws UnsupportedCollisionException {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Side whichSide(Plane plane) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public BoundingVolume transform(Matrix4f trans, BoundingVolume store) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public BoundingVolume transform(Transform trans, BoundingVolume store) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public BoundingVolume mergeLocal(BoundingVolume volume) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public BoundingVolume merge(BoundingVolume volume) {
				// TODO Auto-generated method stub
				return null;
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
			public boolean intersects(Vector3f point) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean intersects(Ray ray) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean intersects(BoundingVolume bv) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public float getVolume() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Type getType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public float distanceToEdge(Vector3f point) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public boolean contains(Vector3f point) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void computeFromPoints(FloatBuffer points) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public BoundingVolume clone(BoundingVolume store) {
				// TODO Auto-generated method stub
				return null;
			}
		};
		assertTrue(camera.containsGui(volume)==false);
	}
	
	
	
	

}
