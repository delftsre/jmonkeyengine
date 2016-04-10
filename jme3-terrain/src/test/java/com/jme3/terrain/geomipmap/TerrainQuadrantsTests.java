package com.jme3.terrain.geomipmap;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import junit.framework.Assert;

public class TerrainQuadrantsTests {

	TerrainQuad tquad;
	TerrainPatch tpatch;
	Vector3f zero3f; 
	Vector2f zero2f;
	
	@Before
	public void setTerrainQuad() {
		float heightMap[] = new float[16641];
		zero3f = new Vector3f(0.0f, 0.0f, 0.0f);
		zero2f = new Vector2f(0.0f, 0.0f);
		tquad = new TerrainQuad("QuadTest", 65, 129,
                            zero3f, heightMap, 513,
                            zero2f, 192.0f);
		
		tpatch =  new TerrainPatch("PatchTest", 65, zero3f,
                heightMap, zero3f);
		
	}
	
	@Test 
	public void testPatchNeighbours() {
		TerrainQuadrants.patchNeighbours(tpatch, tquad);
		
		Assert.assertNotNull(tpatch.rightNeighbour);
		Assert.assertNotNull(tpatch.bottomNeighbour);
		Assert.assertTrue(tpatch.searchedForNeighboursAlready);
	}
	
	// results are loaded into updated. In this scenario 4 results possible
	
	@Test
	public void testFindNeighboursLod() {
		HashMap<String,UpdatedTerrainPatch> updated = new HashMap<String,UpdatedTerrainPatch>();
		TerrainQuadrants.findNeighboursLod(updated, tquad);
		Assert.assertEquals(updated.size(), 4);
	}

	@Test
	public void testGetRightPatch() {
		TerrainPatch patch = TerrainQuadrants.getPatch(1, tquad.getChildren());
		Assert.assertEquals(patch.getName(), "QuadTestPatch1");
		patch = TerrainQuadrants.getPatch(2, tquad.getChildren());
		Assert.assertEquals(patch.getName(), "QuadTestPatch2");
		patch = TerrainQuadrants.getPatch(3, tquad.getChildren());
		Assert.assertEquals(patch.getName(), "QuadTestPatch3");
		patch = TerrainQuadrants.getPatch(4, tquad.getChildren());
		Assert.assertEquals(patch.getName(), "QuadTestPatch4");
	}
	
	@Test
	public void testFindPatches() {
		TerrainPatch result = TerrainQuadrants.findRightPatch(tpatch, tquad);
		Assert.assertEquals(result.getName(), "QuadTestPatch3");
		result = TerrainQuadrants.findDownPatch(tpatch, tquad);
		Assert.assertEquals(result.getName(), "QuadTestPatch2");

	}
}
