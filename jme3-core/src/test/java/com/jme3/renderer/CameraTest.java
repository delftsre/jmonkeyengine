package com.jme3.renderer;
import static org.junit.Assert.*;

import com.jme3.math.*;
import com.jme3.math.Plane.Side;
import com.jme3.renderer.Camera.FrustumIntersect;

import org.junit.Test;

public class CameraTest {
    
	Camera c;
	
	/**
     * LEFT_PLANE represents the left plane of the camera frustum.
     */
    private static final int LEFT_PLANE = 0;
    /**
     * RIGHT_PLANE represents the right plane of the camera frustum.
     */
    private static final int RIGHT_PLANE = 1;
    /**
     * BOTTOM_PLANE represents the bottom plane of the camera frustum.
     */
    private static final int BOTTOM_PLANE = 2;
    /**
     * TOP_PLANE represents the top plane of the camera frustum.
     */
    private static final int TOP_PLANE = 3;
	
    @Test
    public void testConstructor() {
        Camera c = new Camera(20,20);
        assertNotNull(c.getFrustumFar());
    }
    
    public void testInit(){
    	c = new Camera(20,20);
    }
    
    private boolean worldPlaneEquals(Plane[] pa1, Plane[] pa2){
    	boolean eq = true;
    	int l = pa1.length;
    	if(l != pa2.length)
    		return false;
    	
    	for(int i = 0;i < l;i++){
    		Plane p1 = pa1[i];
    		Plane p2 = pa2[i];
    		eq = p1.getConstant() == p2.getConstant();
    		eq = eq && p1.getNormal().equals(p2.getNormal());
    		if(eq == false)
    			break;
    	}
    	return eq;
    }
    
    @Test
    public void testWPlaneEquals(){
    	Plane[] pa1 = new Plane[3];
    	Plane[] pa2 = new Plane[3];
    	Plane[] pa3 = new Plane[2];
    	Plane[] pa4 = new Plane[3];
    	Plane[] pa5 = new Plane[3];
    	
    	pa1[0] = new Plane(new Vector3f(1,2,3),4);
    	pa1[1] = new Plane(new Vector3f(5,6,7),8);
    	pa1[2] = new Plane();
    	

    	pa2[0] = new Plane(new Vector3f(1,2,3),4);
    	pa2[1] = new Plane(new Vector3f(5,6,7),8);
    	pa2[2] = new Plane();
    	

    	pa3[0] = new Plane(new Vector3f(1,2,3),4);
    	pa3[1] = new Plane(new Vector3f(5,6,7),8);
    	
    	pa4[0] = new Plane(new Vector3f(1,2,3),4);
    	pa4[1] = new Plane(new Vector3f(5,1,7),8);
    	pa4[2] = new Plane();
    	
    	pa5[0] = new Plane(new Vector3f(1,2,3),4);
    	pa5[1] = new Plane(new Vector3f(5,6,7),1);
    	pa5[2] = new Plane();
    	
    	assertTrue(worldPlaneEquals(pa1,pa2));
    	assertFalse(worldPlaneEquals(pa1,pa3));
    	assertFalse(worldPlaneEquals(pa1,pa4));
    	assertFalse(worldPlaneEquals(pa1,pa5));
    }
    
    /**
     * Tests the Clone method
     */
    @Test
    public void testClone(){
    	testInit();
    	Camera c2 = c.clone();
    	assertEquals(c.getPlaneState(),c2.getPlaneState());
    	assertTrue(worldPlaneEquals(c.worldPlane, c2.worldPlane));
    	assertEquals(c.getRotation(),c2.getRotation());
    	assertEquals(c.getLocation(),c2.getLocation());
    	assertEquals(c.projectionMatrixOverride,c2.projectionMatrixOverride);
    	assertEquals(c.viewMatrix,c2.viewMatrix);
    	
    	c.projectionMatrixOverride = null;
    	Camera c3 = c.clone();
    	assertEquals(c.getPlaneState(),c3.getPlaneState());
    	assertTrue(worldPlaneEquals(c.worldPlane, c3.worldPlane));
    	assertEquals(c.getRotation(),c3.getRotation());
    	assertEquals(c.getLocation(),c3.getLocation());
    	assertEquals(c.projectionMatrixOverride,c3.projectionMatrixOverride);
    	assertEquals(c.viewMatrix,c3.viewMatrix);
    }
    
    /**
     * Tests the copyFrom method
     */
    @Test
    public void testCopy(){
    	testInit();
    	c.setClipPlane(new Plane(new Vector3f(10,20,30),55));
    	c.setPlaneState(5);
    	c.setFrustumLeft(20);
    	c.worldPlane[2].setNormal(3, 4, 5);
    	Camera c2 = new Camera(30,30);
    	c2.copyFrom(c);
    	assertNotEquals(c.getPlaneState(),c2.getPlaneState());
    	assertTrue(worldPlaneEquals(c.worldPlane, c2.worldPlane));
    	assertEquals(c.getRotation(),c2.getRotation());
    	assertEquals(c.getLocation(),c2.getLocation());
    	assertEquals(c.projectionMatrixOverride,c2.projectionMatrixOverride);
    	assertEquals(c.viewMatrix,c2.viewMatrix);
    }
    
    /**
     * Test for simple get and set operations
     */
    @Test
    public void testSimpleGettersAndSetters(){
    	testInit();
    	//Name
    	assertNull(c.getName());
    	c.setName("cam");
    	assertEquals("cam",c.name);
    	assertEquals(c.name, c.getName());
    	
    	//Frustums
    	assertEquals(c.frustumBottom,c.getFrustumBottom(),0);
    	c.setFrustumBottom(20);
    	assertEquals(20,c.frustumBottom,0);
    	assertEquals(c.frustumBottom,c.getFrustumBottom(),0);

    	assertEquals(c.frustumFar,c.getFrustumFar(),0);
    	c.setFrustumFar(20);
    	assertEquals(20,c.frustumFar,0);
    	assertEquals(c.frustumFar,c.getFrustumFar(),0);

    	assertEquals(c.frustumNear,c.getFrustumNear(),0);
    	c.setFrustumNear(20);
    	assertEquals(20,c.frustumNear,0);
    	assertEquals(c.frustumNear,c.getFrustumNear(),0);

    	assertEquals(c.frustumTop,c.getFrustumTop(),0);
    	c.setFrustumTop(20);
    	assertEquals(20,c.frustumTop,0);
    	assertEquals(c.frustumTop,c.getFrustumTop(),0);

    	assertEquals(c.frustumLeft,c.getFrustumLeft(),0);
    	c.setFrustumLeft(20);
    	assertEquals(20,c.frustumLeft,0);
    	assertEquals(c.frustumLeft,c.getFrustumLeft(),0);

    	assertEquals(c.frustumRight,c.getFrustumRight(),0);
    	c.setFrustumRight(20);
    	assertEquals(20,c.frustumRight,0);
    	assertEquals(c.frustumRight,c.getFrustumRight(),0);
    	
    	c.setFrustum(1,2,3,4,5,6);
    	assertEquals(c.frustumNear,1,0);
    	assertEquals(c.frustumFar,2,0);
    	assertEquals(c.frustumLeft,3,0);
    	assertEquals(c.frustumRight,4,0);
    	assertEquals(c.frustumTop,5,0);
    	assertEquals(c.frustumBottom,6,0);
    	
    	//Direction, left, up
    	assertEquals(c.rotation.getRotationColumn(2), c.getDirection());
    	assertEquals(c.rotation.getRotationColumn(0), c.getLeft());
    	assertEquals(c.rotation.getRotationColumn(1), c.getUp());
    	
    	//Location
    	c.setLocation(new Vector3f(20,25,20));
    	assertEquals(new Vector3f(20,25,20), c.location);
    	assertEquals(c.location,c.getLocation());
    	
    	//Rotation
    	c.setRotation(new Quaternion(2,3,4,5));
    	assertEquals(new Quaternion(2,3,4,5),c.rotation);
    	c.lookAtDirection(new Vector3f(3,4,5),new Vector3f(4,5,6));
    	assertEquals(new Quaternion(-0.38264582f, 0.0053614737f, -0.5439183f, 0.7467974f), c.rotation);
    	c.setAxes(new Quaternion(1,3,4,5));
    	assertEquals(new Quaternion(1,3,4,5),c.rotation);
    	c.setAxes(new Vector3f(1,2,3),new Vector3f(10,20,30),new Vector3f(20,30,40));
    	assertEquals(new Quaternion(0.0766996f, -0.1349051f, 0.083772056f, 0.79758465f),c.rotation);
    	
    	//Frame
    	c.setFrame(new Vector3f(1,2,3), new Vector3f(4,5,6), new Vector3f(7,8,9), new Vector3f(10,11,12));
    	assertEquals(new Vector3f(1,2,3),c.location);
    	assertEquals(new Quaternion(0.02158811f, -0.04917081f, 0.020617872f, 0.8152174f),c.rotation);
    	c.setFrame(new Vector3f(4,5,6), new Quaternion(4,5,6,7));
    	assertEquals(new Vector3f(4,5,6),c.location);
    	assertEquals(new Quaternion(4,5,6,7),c.rotation);
    	
    	//Viewports
    	assertEquals(c.viewPortLeft,c.getViewPortLeft(),0);
    	assertNotEquals(3,c.viewPortLeft);
    	c.setViewPortLeft(3);
    	assertEquals(3,c.viewPortLeft,0);

    	assertEquals(c.viewPortRight,c.getViewPortRight(),0);
    	assertNotEquals(4,c.viewPortRight);
    	c.setViewPortRight(4);
    	assertEquals(4,c.viewPortRight,0);
    	

    	assertEquals(c.viewPortTop,c.getViewPortTop(),0);
    	assertNotEquals(5,c.viewPortTop);
    	c.setViewPortTop(5);
    	assertEquals(5,c.viewPortTop,0);
    	

    	assertEquals(c.viewPortBottom,c.getViewPortBottom(),0);
    	assertNotEquals(6,c.viewPortBottom);
    	c.setViewPortBottom(6);
    	assertEquals(6,c.viewPortBottom,0);
    	
    	c.setViewPort(10, 20, 30, 40);
    	assertEquals(10,c.viewPortLeft,0);
    	assertEquals(20,c.viewPortRight,0);
    	assertEquals(30,c.viewPortBottom,0);
    	assertEquals(40,c.viewPortTop,0);
    	
    	//WorldPlane
    	Plane wp = c.getWorldPlane(0);
    	assertTrue(worldPlaneEquals(new Plane[]{wp},new Plane[]{wp}));
    	
    	//ViewMatrices
    	assertEquals(c.viewMatrix, c.getViewMatrix());
    	assertEquals(c.viewProjectionMatrix,c.getViewProjectionMatrix());
    	
    	//Viewportchanged
    	c.viewportChanged = true;
    	assertTrue(c.isViewportChanged());
    	c.clearViewportChanged();
    	assertFalse(c.isViewportChanged());
    	
    	//Parallel projection
    	c.setParallelProjection(false);
    	assertFalse(c.isParallelProjection());
    	c.setParallelProjection(true);
    	assertTrue(c.isParallelProjection());
    	
    	//Width & height
    	assertEquals(c.height, c.getHeight());
    	assertEquals(c.width, c.getWidth());


    	
    	

    }
    
    @Test
    public void testSetFrustumPerspective(){
    	testInit();
    	c.setFrustumPerspective(2, 3, 4, 5);
    	float h = 0.06982026f;
    	float w = h * 3;
    	assertEquals(-w, c.frustumLeft,0);
    	assertEquals(w,  c.frustumRight,0);
    	assertEquals(-h, c.frustumBottom,0);
    	assertEquals(h,  c.frustumTop,0);
    	assertEquals(4,  c.frustumNear,0);
    	assertEquals(5,  c.frustumFar,0);
    	assertFalse(c.isParallelProjection());
    	
    	c.setFrustumPerspective(2f, Float.NaN, 4f, 5f);
    	c.setFrustumPerspective(2f, Float.NEGATIVE_INFINITY, 4f, 5f);
    	//expect nothing
    }
    
    @Test(expected = NullPointerException.class)
    public void testNullSetLocation(){c.setLocation(null);}

    @Test(expected = NullPointerException.class)
    public void testNullsetRotation(){c.setRotation(null);}

    @Test(expected = NullPointerException.class)
    public void testNullsetAxes(){c.setAxes(null,null,null);}

    @Test(expected = NullPointerException.class)
    public void testNullsetAxes2(){c.setAxes(null);}
    
    /**
     * Should test clip plane calculations
     * Not implemented since the calculations can not be 
     * verified by us.
     */
    @Test
    public void testClipPlane(){
    	//testInit();
    	//c.setClipPlane(new Plane(new Vector3f(5,5,5),0), Side.Positive);
    }
    
    /**
     * Tests resize method
     */
    @Test
    public void testResize(){
    	testInit();
    	int oldWidth = c.width;
    	int oldHeight = c.height;
    	float oldFRight = c.frustumRight;
    	float oldFLeft = c.frustumLeft;
    	c.resize(5, 10, false);
    	assertNotEquals(oldWidth,c.width);
    	assertNotEquals(oldHeight,c.height);
    	assertEquals(5,c.width);
    	assertEquals(10,c.height);
    	assertEquals(oldFRight,c.frustumRight,0);
    	assertEquals(oldFLeft,c.frustumLeft,0);
    	
    	c.resize(5, 10, true);
    	assertNotEquals(oldWidth,c.width);
    	assertNotEquals(oldHeight,c.height);
    	assertEquals(5,c.width);
    	assertEquals(10,c.height);
    	assertNotEquals(oldFRight,c.frustumRight,0);
    	assertNotEquals(oldFLeft,c.frustumLeft,0);
    }
    
    
    /**
     * Tests the lookAt function
     */
    @Test
    public void testLookAt(){
    	testInit();
    	c.lookAt(new Vector3f(10,20,30), new Vector3f(10,20,30));
    	assertEquals(new Quaternion(-0.30639285f, 0.072329536f, -0.21807073f, 0.92376244f),c.rotation);
    	
    	c.lookAt(new Vector3f(10,20,30), Vector3f.ZERO);
    	assertEquals(new Quaternion(-0.27465674f, 0.15385646f, 0.04457065f, 0.94810617f),c.rotation);

    	c.lookAt(Vector3f.ZERO, Vector3f.ZERO);
    	assertEquals(new Quaternion(0f,0f,0f,1f),c.rotation);
    }
    
    
    /**
     * Tests contains function
     * Makes use of the BoundMock class
     */
    @Test
    public void testContains(){
    	testInit();
    	FrustumIntersect fi;
    	
    	fi = c.contains(null);
    	assertEquals(FrustumIntersect.Inside,fi);
    	
    	BoundMock bound = new BoundMock();
    	bound.checkPlane = 0;
    	fi = c.contains(bound);
    	assertEquals(FrustumIntersect.Inside,fi);
    	
    	fi = c.contains(bound);
    	assertEquals(FrustumIntersect.Inside,fi);

    	bound.checkPlane = 6;
    	c.setPlaneState(0);
    	c.worldPlane[0].setNormal(new Vector3f(5,3,3));;
    	fi = c.contains(bound);
    	assertEquals(FrustumIntersect.Outside,fi);

    	bound.checkPlane = 6;
    	c.setPlaneState(0);
    	c.worldPlane[0].setNormal(new Vector3f(7,3,3));;
    	fi = c.contains(bound);
    	assertEquals(FrustumIntersect.Intersects,fi);
    }
    
    
    /**
     * Tests Set and Get ProjectionMatrix, mainly for null -> identity
     */
    @Test
    public void testProjectionMatr(){
    	testInit();
    	//Sets projectionMatrixOverride to identity
    	c.setProjectionMatrix(null);
    	assertEquals(new Matrix4f(),c.projectionMatrixOverride);
    	assertEquals(c.projectionMatrix,c.getProjectionMatrix());
    	c.setProjectionMatrix(new Matrix4f(10f,10f,10f,10f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
    	assertEquals(new Matrix4f(10f,10f,10f,10f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),c.projectionMatrixOverride);
    	assertEquals(c.projectionMatrixOverride,c.getProjectionMatrix());
    }
    
    @Test
    public void testLeftPlane() {
    	testInit();    	
    	float x = (float) 5.8;
    	float z = (float) 7.5;
    	c.coeffLeft[0] = (float) 5.8;
    	c.coeffLeft[1] = (float) 7.5;
    	c.onFrameChange();
    	float wx = c.getWorldPlane(LEFT_PLANE).getNormal().x;
    	float wy = c.getWorldPlane(LEFT_PLANE).getNormal().y;
    	float wz = c.getWorldPlane(LEFT_PLANE).getNormal().z;
    	assertEquals(x, wx, 0);
    	assertEquals(0, wy, 0);
    	assertEquals(z, wz, 0);
    	float actual = c.getWorldPlane(TOP_PLANE).getConstant();
    	float expected = (float) -0.5;
    	assertEquals(expected, actual, 0);
    }
    
    @Test
    public void testRightPlane() {
    	testInit();
    	float x = (float) 7.8;
    	float z = (float) 6.5;
    	c.coeffRight[0] = (float) 7.8;
    	c.coeffRight[1] = (float) 6.5;
    	c.onFrameChange();
    	float wx = c.getWorldPlane(RIGHT_PLANE).getNormal().x;
    	float wy = c.getWorldPlane(RIGHT_PLANE).getNormal().y;
    	float wz = c.getWorldPlane(RIGHT_PLANE).getNormal().z;
    	assertEquals(x, wx, 0);
    	assertEquals(0, wy, 0);
    	assertEquals(z, wz, 0);
    	float actual = c.getWorldPlane(TOP_PLANE).getConstant();
    	float expected = (float) -0.5;
    	assertEquals(expected, actual, 0);   
    }
    
    @Test
    public void testBottomPlane() {
    	testInit();
    	float x = (float) 3.8;
    	float z = (float) 4.5;
    	c.coeffBottom[0] = (float) 3.8;
    	c.coeffBottom[1] = (float) 4.5;
    	c.onFrameChange();
    	float wx = c.getWorldPlane(BOTTOM_PLANE).getNormal().x;
    	float wy = c.getWorldPlane(BOTTOM_PLANE).getNormal().y;
    	float wz = c.getWorldPlane(BOTTOM_PLANE).getNormal().z;
    	assertEquals(0, wx, 0);
    	assertEquals(x, wy, 0);
    	assertEquals(z, wz, 0);
    	float actual = c.getWorldPlane(TOP_PLANE).getConstant();
    	float expected = (float) -0.5;
    	assertEquals(expected, actual, 0);   
    }
    
    
    @Test
    public void testTopPlane() {
    	testInit();
    	float x = (float) 6.8;
    	float z = (float) 3.5;
    	c.coeffTop[0] = (float) 6.8;
    	c.coeffTop[1] = (float) 3.5;
    	c.onFrameChange();
    	float wx = c.getWorldPlane(TOP_PLANE).getNormal().x;
    	float wy = c.getWorldPlane(TOP_PLANE).getNormal().y;
    	float wz = c.getWorldPlane(TOP_PLANE).getNormal().z;
    	assertEquals(0, wx, 0);
    	assertEquals(x, wy, 0);
    	assertEquals(z, wz, 0); 
    	float actual = c.getWorldPlane(TOP_PLANE).getConstant();
    	float expected = (float) -0.5;
    	assertEquals(expected, actual, 0);    	
    }
    
}
