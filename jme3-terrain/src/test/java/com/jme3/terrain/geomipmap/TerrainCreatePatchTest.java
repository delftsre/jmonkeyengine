package com.jme3.terrain.geomipmap;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import junit.framework.Assert;

public class TerrainCreatePatchTest {

	TerrainQuad tquad;
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
		
	}
	
	// Method that invokes nearly all other methods in this class. Is called in constructor of terrainquad.
	// Adds 4 children every time. Test two times.
	
	@Test
	public void testTerrainSplit() {
		float heightMap[] = new float[16641];
		TerrainCreatePatch.split(65, heightMap, tquad);
		Assert.assertEquals(tquad.getChildren().size(), 8);
		TerrainCreatePatch.split(65, heightMap, tquad);
		Assert.assertEquals(tquad.getChildren().size(), 12);
	}
	
	// Test if there are 4 children created, it starts off with 4 so should be 8.
	
	@Test
	public void testCreateQuad() {
		float heightMap[] = new float[66049];
		TerrainCreatePatch.createQuad(65, heightMap, tquad);
		Assert.assertEquals(tquad.getChildren().size(), 8);
		
		//Test the creation of new objects. WHen created sucesfully the name is concated by Quad x.
		
		Assert.assertEquals(tquad.getChild(4).getName(), "QuadTestQuad1");
		Assert.assertEquals(tquad.getChild(5).getName(), "QuadTestQuad2");
		Assert.assertEquals(tquad.getChild(6).getName(), "QuadTestQuad3");
		Assert.assertEquals(tquad.getChild(7).getName(), "QuadTestQuad4");
	}
	
	@Test
	public void testAddPatch() {
		float heightMap[] = new float[16641];
		TerrainCreatePatch.createQuadPatch(heightMap, tquad);
		Assert.assertEquals(tquad.getChildren().size(), 8);
		
		Assert.assertEquals(tquad.getChild(4).getName(), "QuadTestPatch1");
		Assert.assertEquals(tquad.getChild(5).getName(), "QuadTestPatch2");
		Assert.assertEquals(tquad.getChild(6).getName(), "QuadTestPatch3");
		Assert.assertEquals(tquad.getChild(7).getName(), "QuadTestPatch4");
	}	
	
	@Test
	public void testCreateHeightSubBlock() {
		float heightMap[] = {0.0f, 1.0f, 2.0f, 3.0f};
		float results[] = TerrainCreatePatch.createHeightSubBlock(heightMap, 1, 0, 2);
		Assert.assertEquals(results[0], 1.0f);
		Assert.assertEquals(results[1], 0.0f);
		Assert.assertEquals(results[2], 3.0f);
		Assert.assertEquals(results[3], 0.0f);
		
	}
	
	@Test
	public void testCreateBasicOffset() {
		Vector2f vec2f = new Vector2f(1.0f, 3.0f);
		Vector2f result = TerrainCreatePatch.createBasicOffset(vec2f);
		Assert.assertEquals(vec2f.x, result.x);
		Assert.assertEquals(vec2f.y, result.y);
	}
	
	@Test
	public void testSetDefaultOffset() {
		Vector2f vec2f = new Vector2f(1.0f, 3.0f);
		Vector3f vec3f = new Vector3f(10.0f, 10.0f, 10.f);
		Vector2f result = TerrainCreatePatch.setDefaultOffset(vec3f, vec2f);
		Assert.assertEquals(result.x, vec2f.x+vec3f.x);
		Assert.assertEquals(result.y, vec2f.y+vec3f.y);
	}

}
