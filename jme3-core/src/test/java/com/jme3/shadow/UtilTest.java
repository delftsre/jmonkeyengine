
/**
 *
 * @author mengmeng
 */
package com.jme3.shadow;
import static org.junit.Assert.*;
import org.junit.Test;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Matrix4f;
import com.jme3.math.Transform;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.GeometryList;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.sun.accessibility.internal.resources.accessibility;
import com.sun.javafx.geom.Vec3f;

import sun.net.www.content.audio.x_aiff;

public class UtilTest {
	ShadowUtil util = new ShadowUtil(); 
	
	@Test
	public void testUpdatePoints2() {
	   
		Vector3f[] points = new Vector3f[8];
		float x = 0;
		float y = 1;
		float z = 2;
		for (int i = 0; i < points.length; i++)
		{
		   Vector3f p = new Vector3f(x, y, z);
		   points[i] = p;
		   x +=1;
		   y +=1;
		   z +=1;
		 }
	   
	   //test points
	   /*
	   for (int i = 0; i< points.length; i++)
	   {
		   System.out.println("point: " + points[i].getX()+" "+points[i].getY()+" "+points[i].getZ());
	   }
	   */
	   
		
		Camera camera = new Camera(10, 10);
		util.updateFrustumPoints2(camera, points);
		for (int i = 0; i< points.length; i++)
		{
			if (i <= 3)
			{
				assertEquals(1, (int)points[i].getZ());
			}
			else
			{
				assertEquals(2, (int)points[i].getZ());
			}
			
		}
   }
	
	@Test
	public void testUpdatePoints() {
	   
		Vector3f[] points = new Vector3f[8];
		float x = 0;
		float y = 1;
		float z = 2;
		for (int i = 0; i < points.length; i++)
		{
		   Vector3f p = new Vector3f(x, y, z);
		   points[i] = p;
		   x +=1;
		   y +=1;
		   z +=1;
		 }

		Camera camera = new Camera(2, 2);
        float near = 2;
        float far = 5;
		
		util.updateFrustumPoints(camera, near,far,10, points);
		/*
		(5.0, -5.0, -11.5)
		(5.0, 5.0, -11.5)
		(-5.0, 5.0, -11.5)
		(-5.0, -5.0, -11.5)
		(5.0, -5.0, 18.5)
		(5.0, 5.0, 18.5)
		(-5.0, 5.0, 18.5)
		(-5.0, -5.0, 18.5)
		*/
		assertTrue(points[0].equals(new Vector3f(5.0f,-5.0f,-11.5f)));
		assertTrue(points[1].equals(new Vector3f(5.0f,5.0f,-11.5f)));
		assertTrue(points[2].equals(new Vector3f(-5.0f,5.0f,-11.5f)));
		assertTrue(points[3].equals(new Vector3f(-5.0f,-5.0f,-11.5f)));
		assertTrue(points[4].equals(new Vector3f(5.0f,-5.0f,18.5f)));
		assertTrue(points[5].equals(new Vector3f(5.0f,5.0f,18.5f)));
		assertTrue(points[6].equals(new Vector3f(-5.0f,5.0f,18.5f)));
		assertTrue(points[7].equals(new Vector3f(-5.0f,-5.0f,18.5f)));
   }
	
		@Test
		public void testComputeUnionBound(){
			Vector3f point = new Vector3f(1.0f,1.0f,1.0f);
			Transform transform = new Transform(point);
			Geometry geometry = new Geometry("testGeo1", new Mesh());
			Geometry geometry2 = new Geometry("testGeo2", new Mesh());
			Geometry geometry3 = new Geometry("testGeo3", new Mesh());
			GeometryList geometryList = new GeometryList(null);
			geometryList.add(geometry);geometryList.add(geometry2);geometryList.add(geometry3);
			BoundingBox box = new BoundingBox(new Vector3f(0.5f, 0.5f, 0.5f), 0.5f, 0.5f, 0.5f);
			BoundingBox box2 = util.computeUnionBound(geometryList, transform);

			System.out.println(box.getCenter().equals(box2.getCenter())&&box.getXExtent()==box2.getXExtent());
			assertTrue(box.getCenter().equals(box2.getCenter()));
			assertTrue(box.getXExtent()==box2.getXExtent());
			assertTrue(box.getYExtent()==box2.getYExtent());
			assertTrue(box.getZExtent()==box2.getZExtent());
		}
		
		
}
