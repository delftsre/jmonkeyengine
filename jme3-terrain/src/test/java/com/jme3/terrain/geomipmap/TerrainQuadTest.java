package com.jme3.terrain.geomipmap;


import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;


import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.terrain.ProgressMonitor;

import junit.framework.Assert;

public class TerrainQuadTest {
	
	TerrainQuad tquad;
	Vector3f zero3f; 
	Vector2f zero2f;

	
	
	//String name, int patchSize, int quadSize,
    //Vector3f scale, float[] heightMap, int totalSize,
    //Vector2f offset, float offsetAmount
	
	@Before
	public void setTerrainQuad() {
		float heightMap[] = new float[16641];
		zero3f = new Vector3f(0.0f, 0.0f, 0.0f);
		zero2f = new Vector2f(0.0f, 0.0f);
		tquad = new TerrainQuad("QuadTest", 65, 129,
                            zero3f, heightMap, 513,
                            zero2f, 192.0f);
		
	}
	
	
	// Should return either 1 or 0.
	
	@Test 
	public void testCollideWithRay() {
		Ray ray = new Ray();
		ray.setDirection(zero3f);
		CollisionResults collResults = new CollisionResults();
		int ret = TerrainTransform.collideWithRay(ray, collResults, tquad);
		Assert.assertTrue(ret == 1 || ret == 0);
	}
	
	@Test
	public void testClone() {
		TerrainQuad clone = tquad.clone(true);
		Assert.assertNotSame(clone, tquad);
		Assert.assertEquals(clone.getName(), tquad.getName());
		Assert.assertEquals(clone.getSize(), tquad.getSize());
		Assert.assertEquals(clone.getTotalSize(), tquad.getTotalSize());
		Assert.assertEquals(clone.getOffsetAmount(), tquad.getOffsetAmount());
		Assert.assertEquals(clone.getQuadrant(), tquad.getQuadrant());
	}
	
	@Test
	public void testGetMaterial() {
		Material answer = tquad.getMaterial(zero3f);
		Assert.assertNull(answer);
	}	
	
	@Test
	public void testSetNeedToRecalculateNormals() {
		Assert.assertNull(tquad.getAffectedAreaBBox());
	}

}
