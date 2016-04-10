package com.jme3.terrain.geomipmap;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.terrain.ProgressMonitor;
import com.jme3.terrain.geomipmap.lodcalc.LodCalculator;
import com.jme3.terrain.geomipmap.picking.TerrainPickData;
import com.sun.javafx.collections.MappingChange.Map;

import junit.framework.Assert;

public class TerrainTransformTest {

	private TerrainQuad tquad;
	private Vector3f zero3f; 
	private Vector2f zero2f;
	private float adjusted;

	@Before
	public void setTerrainQuad() {
		float heightMap[] = new float[16641];
		zero3f = new Vector3f(0.0f, 0.0f, 0.0f);
		zero2f = new Vector2f(0.0f, 0.0f);
		tquad = new TerrainQuad("QuadTest", 65, 129,
                            zero3f, heightMap, 513,
                            zero2f, 192.0f);
		
	}
	
	@Test
	public void testCollideWithRay() {
		Ray ray = new Ray();
		CollisionResults results = new CollisionResults();
		int result = TerrainTransform.collideWithRay(ray, results, tquad);
		Assert.assertTrue(result == 0 || result == 1);
	}
	
	
	// As progressmonitor is an interface this is a test class to see if modification occurs in the other methods.
	
	
	private class ExtendedProgmonitor implements ProgressMonitor {

		public ExtendedProgmonitor() {
		}
		
		@Override
		public void incrementProgress(float increment) {
			adjusted++;
		}

		@Override
		public void setMonitorMax(float max) {
			adjusted++;
		}

		@Override
		public float getMonitorMax() {
			return adjusted++;
		}

		@Override
		public void progressComplete() {
			adjusted++;
		}
    } 
	
	@Test
	public void testGenerateEntropy() {
		adjusted = 0.0f;
		ExtendedProgmonitor progmonitor =  new ExtendedProgmonitor();
		TerrainTransform.generateEntropy(progmonitor, tquad);
		// Should be executed four times. Thus checking if 4.0.
		
		Assert.assertEquals(adjusted, 4.0f);
	}
	
	// Just correct executiong test.
	
	@Test
	public void testResetCachedNeighbours() {
		TerrainTransform.resetCachedNeighbours(tquad);
		
	}
	
	// Saves the result in the holder list thus this can not be empty but must be of size 4.
	// Size 4 cuz tquad has 4 objects.
	
	@Test
	public void testGetAllTerrainPatches() {
		List<TerrainPatch> holder = new ArrayList<TerrainPatch>();
		TerrainTransform.getAllTerrainPatches(holder, tquad);
		
	}
	
	@Test
	public void testGetAllTerrainPatchesWithTranslation() {
		HashMap<TerrainPatch, Vector3f> holder = new HashMap<TerrainPatch, Vector3f>();
		TerrainTransform.getAllTerrainPatchesWithTranslation(holder, zero3f, tquad);
		Assert.assertTrue(holder.size() == 4);
	}
	
	// Same as above but now results are saved in results.
	
	@Test 
	public void testFindPick() {
		Ray testRay = new Ray();
		List<TerrainPickData> results = new ArrayList<TerrainPickData>();
		TerrainTransform.findPick(testRay, results, tquad);
		Assert.assertTrue(results.size() == 4);
	}
	
	
}
