package com.jme3.renderer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.asset.AssetManager;
import static org.mockito.Mockito.*;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.PlaceholderAssets;
import com.jme3.scene.VertexBuffer.Type;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

public class RenderManagerSpatialTest{
	
	private RenderManager renderManager;
	private Camera camera;

	@Before
	public void setUp() throws Exception {
		Renderer renderer = null;
		camera = new Camera();
		renderManager = new RenderManager(renderer);
	}

	/**
	 * Perform post-test clean up
	 *
	 * @throws Exception
	 *
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		renderManager = null;
		camera = null;
	}
	
	//Test the preLoadScene method
	
	@Test(expected=IllegalStateException.class)
	public void testPreLoadSceneGeometryNull() {
		Geometry geo = new Geometry();
		Assert.assertNotNull(geo);
		Assert.assertNull(geo.getMaterial());
		renderManager.preloadScene(geo);	
	}
	
	@Test
	public void testPreLoadSceneSpatial() {
		Spatial spatial = Mockito.mock(Spatial.class);
		renderManager.preloadScene(spatial);	
		Assert.assertNotNull(spatial);
	}
	
	@Test
	public void testPreLoadSceneGeometryMeshNull() {
		Geometry geo = new Geometry();
		Material material = Mockito.mock(Material.class);
		geo.setMaterial(material);
		renderManager.preloadScene(geo);		
		Assert.assertNull(geo.getMesh());
	}
	
	
	@Test
	public void testPreLoadSceneNode() {
		Node node = new Node();	
		Geometry geo = new Geometry();
		Material material = Mockito.mock(Material.class);
		geo.setMaterial(material);
		node.attachChild(geo);
		Renderer r = Mockito.mock(Renderer.class);
		RenderManager m = Mockito.spy(new RenderManager(r));
		m.preloadScene(node);
		verify(m).preloadScene(node.getChild(0));
	}
	
	//test the renderScene method
	
	@Test
	public void testRenderScene(){
		Spatial spatial = Mockito.mock(Spatial.class);
		ViewPort port = new ViewPort("ViewPort", camera);
		Assert.assertEquals(port.getCamera().getPlaneState(),0);
		port.getCamera().setPlaneState(5);
		Assert.assertEquals(port.getCamera().getPlaneState(),5);
		renderManager.renderScene(spatial, port);
		Assert.assertEquals(port.getCamera().getPlaneState(),0);
		
	}
	
	
	

}
