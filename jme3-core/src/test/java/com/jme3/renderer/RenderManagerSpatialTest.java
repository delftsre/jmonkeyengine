package com.jme3.renderer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.material.Material;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import junit.framework.TestCase;

public class RenderManagerSpatialTest{
	
	private RenderManager renderManager;
	private Camera camera;
	private ViewPort viewport;

	@Before
	public void setUp() throws Exception {
		Renderer renderer = null;
		int height = 5;
		int width = 8;
		camera = new Camera(width, height);
		renderManager = new RenderManager(renderer);
		viewport = new ViewPort("viewName", camera);
		
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
	
	@Test(expected=IllegalStateException.class)
	public void testRenderSubSCeneGeometryNull(){
		Geometry geo = Mockito.mock(Geometry.class);
		Mockito.when(geo.checkCulling(viewport.getCamera())).thenReturn(true);
		renderManager.renderScene(geo, viewport);
	}
	
	/*@Test
	public void testRenderSubSceneGeometry(){
		Geometry geo = Mockito.mock(Geometry.class);
		ViewPort viewport = Mockito.mock(ViewPort.class);
		Material mat = Mockito.mock(Material.class);
		Mockito.when(geo.checkCulling(viewport.getCamera())).thenReturn(true);
		Mockito.when(geo.getMaterial()).thenReturn(mat);
		Mockito.when(viewport.getCamera()).thenReturn(camera);
		renderManager.renderScene(geo, viewport);
		
		//verify(viewport).getQueue().addToQueue(geo, geo.getQueueBucket());
	}*/
	
	@Test
	public void testRenderSubSceneNode(){
		Node nodeParrent = Mockito.mock(Node.class);
		Node nodeChild1 = Mockito.mock(Node.class);
		Mockito.when(nodeParrent.checkCulling(viewport.getCamera())).thenReturn(true);
		Mockito.when(nodeChild1.checkCulling(viewport.getCamera())).thenReturn(true);
		List<Spatial> children = new ArrayList<Spatial>();
		children.add(nodeChild1);
		Mockito.when(nodeParrent.getChildren()).thenReturn(children);
		int planeState = 5;
		viewport.getCamera().setPlaneState(planeState );
		assertEquals(5, viewport.getCamera().getPlaneState());
		renderManager.renderScene(nodeParrent, viewport);		
		assertEquals(0, viewport.getCamera().getPlaneState());
	}
	
	@Test
	public void testRenderSubSceneGeometry(){
		Geometry geo = Mockito.mock(Geometry.class);
		Mockito.when(geo.checkCulling(viewport.getCamera())).thenReturn(true);
		
		Material mat = Mockito.mock(Material.class);
		Mockito.when(geo.getMaterial()).thenReturn(mat);
		
		assertTrue(viewport.getQueue().isQueueEmpty(Bucket.Sky));
		
		Bucket value = Bucket.Sky;
		Mockito.when(geo.getQueueBucket()).thenReturn(value);
		
		renderManager.renderScene(geo, viewport);	
		
		assertFalse(viewport.getQueue().isQueueEmpty(Bucket.Sky));
		assertTrue(viewport.getQueue().isQueueEmpty(Bucket.Opaque));
		assertTrue(viewport.getQueue().isQueueEmpty(Bucket.Translucent));
		assertTrue(viewport.getQueue().isQueueEmpty(Bucket.Transparent));
		assertTrue(viewport.getQueue().isQueueEmpty(Bucket.Gui));
	}
	
	@Test
	public void testRenderSubSceneNodeAndGeo(){
		Node nodeParrent = Mockito.mock(Node.class);
		Geometry geo = Mockito.mock(Geometry.class);
		Mockito.when(nodeParrent.checkCulling(viewport.getCamera())).thenReturn(true);
		Mockito.when(geo.checkCulling(viewport.getCamera())).thenReturn(true);
		
		List<Spatial> children = new ArrayList<Spatial>();
		children.add(geo);
		Mockito.when(nodeParrent.getChildren()).thenReturn(children);
		int planeState = 5;
		viewport.getCamera().setPlaneState(planeState );
		
		Material mat = Mockito.mock(Material.class);
		Mockito.when(geo.getMaterial()).thenReturn(mat);
		Bucket value = Bucket.Sky;
		Mockito.when(geo.getQueueBucket()).thenReturn(value);
		
		assertTrue(viewport.getQueue().isQueueEmpty(Bucket.Sky));
		
		assertEquals(5, viewport.getCamera().getPlaneState());
		renderManager.renderScene(nodeParrent, viewport);		
		assertEquals(0, viewport.getCamera().getPlaneState());
		assertFalse(viewport.getQueue().isQueueEmpty(Bucket.Sky));
		assertTrue(viewport.getQueue().isQueueEmpty(Bucket.Opaque));
		assertTrue(viewport.getQueue().isQueueEmpty(Bucket.Translucent));
		assertTrue(viewport.getQueue().isQueueEmpty(Bucket.Transparent));
		assertTrue(viewport.getQueue().isQueueEmpty(Bucket.Gui));
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
