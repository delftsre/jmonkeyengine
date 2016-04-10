package com.jme3.terrain.geomipmap;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.terrain.GeoMap;
import com.jme3.terrain.geomipmap.TerrainPatchLod;

public class TerrainPatchLODTest {
	
	private TerrainPatchLod tpl;
	private int random = 42;

	@Before
	public void setUp() throws Exception {
		tpl = new TerrainPatchLod(0, 0, new float[0], Vector3f.ZERO, Vector2f.ZERO, 0f);
		random = (int)(Math.random() * 100); 
	}

	@Test
	public void getMaxLod_BelowZero_Test() {
		tpl.maxLod = -1;
		
		int max = tpl.getMaxLod();
		assert(max != -1);
		assert(tpl.maxLod != -1);
//		assert(max == 4);
//		assert(tpl.maxLod == 4);
	}
	
	@Test
	public void getMaxLoad_Test() {
		tpl.maxLod = random;
		assert(tpl.getMaxLod() == random);
	}
	
	@Test
	public void setMaxLod_Test() {
		if (tpl.maxLod == random) {
			random = random * 2; // Nasty much.
		}
		tpl.setMaxLod(random);
		assert(tpl.maxLod == random);
	}
	
	@Test
	public void getLod_Test() {
		tpl.lod = random;
		assert(tpl.getLod() == random);
	}
	
	
	@Test
	public void setLod_Test() {
		if (tpl.lod == random) {
			random = random * 2; // Nasty much.
		}
		tpl.setLod(random);
		assert(tpl.lod == random);
	}
	
	@Test
	public void getPreviousLod_Test() {
		tpl.previousLod = random;
		assert(tpl.getPreviousLod() == random);
	}
	
	
	@Test
	public void setPreviousLod_Test() {
		if (tpl.previousLod == random) {
			random = random * 2; // Nasty much.
		}
		tpl.setPreviousLod(random);
		assert(tpl.previousLod == random);
	}	
	
	@Test
	public void setLodAround_Test() {
		tpl.setLodLeft(random);
		tpl.setLodRight(random);
		tpl.setLodBottom(random);
		tpl.setLodTop(random);
		
		assert(tpl.lodLeft == random);
		assert(tpl.lodRight == random);
		assert(tpl.lodTop == random);
		assert(tpl.lodBottom == random);
	}
	
	@Test
	public void getLodAround_Test() {
		tpl.lodLeft = random;
		tpl.lodRight = random;
		tpl.lodTop = random;
		tpl.lodBottom = random;
		
		assert(tpl.getLodLeft() == random);
		assert(tpl.getLodRight() == random);
		assert(tpl.getLodTop() == random);
		assert(tpl.getLodBottom() == random);
	}
	
	@Test
	public void getMesh_Test() {
		assert(tpl.mesh.equals(tpl.getMesh()));
	}
	
	@Test
	public void getLODGeomap_Test() {
		assert(tpl.geomap.equals(tpl.getLODGeomap()));
	}
	
	@Test
	public void setLodEntropies_Test() {
		float[] x = new float[random];
		tpl.setLodEntropies(x);
		assert(tpl.lodEntropy.length == random);
	}
	
	@Test
	public void setLODGeomap_Test() {
		LODGeomap g = mock(LODGeomap.class);
		tpl.setLODGeomap(g);
		assert(tpl.geomap.equals(g));
	}
}
