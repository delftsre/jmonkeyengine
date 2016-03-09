package com.jme3.renderer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

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
	
	@Test(expected=IllegalStateException.class)
	public void testPreLoadSceneGeometryNull() {
		Geometry geo = new Geometry();
		Assert.assertNotNull(geo);
		geo.setMaterial(null);
		renderManager.preloadScene(geo);	
	}
	
	/*@Test
	 public void testPreLoadSceneGeometry() {
		Geometry geo = new Geometry();
		renderManager.preloadScene(geo);	
	}*/
	
	//TODO test werkt met en zonder deze expected, maar heeft alleen code coverage met
	@Test(expected=IllegalStateException.class)
	public void testPreLoadSceneNode() {
		Node node = new Node();	
		Geometry geo = new Geometry();
		geo.setMaterial(null);
		node.attachChild(geo);
		renderManager.preloadScene(node);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testRenderSubSceneGeometry(){
		Geometry geo = new Geometry();
		geo.setMaterial(null);
		ViewPort port = new ViewPort("ViewPort", camera);
		RenderManager m = Mockito.mock(RenderManager.class);
		Assert.assertTrue(geo.checkCulling(camera));
		m.renderScene(geo, port);
		
	}
	
	@Test(expected=IllegalStateException.class)
	public void testRenderSubSceneNode(){
		Node node = new Node();
		Geometry geo = new Geometry();
		geo.setMaterial(null);
		node.attachChild(geo);
		ViewPort port = new ViewPort("ViewPort", camera);
		RenderManager m = Mockito.mock(RenderManager.class);
		Assert.assertTrue(node.checkCulling(camera));
		m.renderScene(node, port);
		
	}
	
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
