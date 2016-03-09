package com.jme3.renderer;

import com.jme3.app.BasicProfiler;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.material.TechniqueDef;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.texture.FrameBuffer;

import junit.framework.TestCase;

/**
 * The class <code>RenderManagerTest</code> contains tests for the class {@link
 * <code>RenderManager</code>}
 *
 * @pattern JUnit Test Case
 *
 * @generatedBy CodePro at 9-3-16 11:29
 *
 * @author raies
 *
 * @version $Revision$
 */
public class RenderManagerTest extends TestCase {
	private RenderManager renderManager;
	private Renderer renderer;
	private Camera camera;
	private int camHeight = 5;
	private int camWidth = 10;
	private ViewPort viewport;
	private String viewName;

	/**
	 * Construct new test instance
	 *
	 * @param name
	 *            the test name
	 */
	public RenderManagerTest(String name) {
		super(name);
	}

	/**
	 * Perform pre-test initialization
	 *
	 * @throws Exception
	 *
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		renderer = new RendererImpl();		
		camera = new Camera(camWidth, camHeight);
		renderManager = new RenderManager(renderer);
		viewName = "viewName";
		viewport = new ViewPort(viewName, camera);
	}

	/**
	 * Perform post-test clean up
	 *
	 * @throws Exception
	 *
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		renderManager = null;
		camera = null;
	}

	/**
	 * Run the ViewPort createPreView(String, Camera) method test
	 */
	public void testCreatePreView() {
		String viewName = "camView";
		ViewPort viewPortCreated = renderManager.createPreView(viewName, camera);
		assertNotNull(viewPortCreated);
		ViewPort retreivedViewport = renderManager.getPreView(viewName);
		assertEquals(viewPortCreated, retreivedViewport);
	}

	public void testEmptyPreViewPortList() {
		String viewName = "noView";
		ViewPort viewPort = renderManager.getPreView(viewName);
		assertNull(viewPort);
	}

	public void testNonExistingPreViewPortList() {
		String viewName = "camView";
		renderManager.createPreView(viewName, camera);
		String viewNameTwo = "anotherCam";
		assertNull(renderManager.getPreView(viewNameTwo));
	}

	public void testRemoveExisitingPreViewPort() {
		String viewName = "camView";
		renderManager.createPreView(viewName, camera);
		boolean removed = renderManager.removePreView(viewName);
		assertTrue(removed);
	}

	public void testRemoveNonExisitingPreViewPort() {
		String viewName = "camView";
		boolean removed = renderManager.removePreView(viewName);
		assertFalse(removed);
	}

	public void testRemoveExisitingPreViewPortFromObject() {
		String viewName = "camView";
		ViewPort viewport = renderManager.createPreView(viewName, camera);
		boolean removed = renderManager.removePreView(viewport);
		assertTrue(removed);
	}

	public void testRemoveNullPreViewPort() {
		String viewName = "camView";
		ViewPort port = new ViewPort(viewName, camera);
		boolean removed = renderManager.removePreView(port);
		assertFalse(removed);
	}

	public void testCreateMainView() {
		String viewName = "camView";
		ViewPort viewPortCreated = renderManager.createMainView(viewName, camera);
		assertNotNull(viewPortCreated);
		ViewPort retreivedViewport = renderManager.getMainView(viewName);
		assertEquals(viewPortCreated, retreivedViewport);
	}

	public void testEmptyViewPortList() {
		String viewName = "noView";
		ViewPort viewPort = renderManager.getMainView(viewName);
		assertNull(viewPort);
	}

	public void testNonExistingMainPortList() {
		String viewName = "camView";
		renderManager.createMainView(viewName, camera);
		String viewNameTwo = "anotherCam";
		ViewPort viewport = renderManager.getMainView(viewNameTwo);
		assertNull(viewport);
	}

	public void testRemoveExisitingMainPort() {
		String viewName = "camView";
		renderManager.createMainView(viewName, camera);
		boolean removed = renderManager.removeMainView(viewName);
		assertTrue(removed);
	}
	
	public void testRemoveMainPortObject() {
		ViewPort viewport = null;
		boolean removed = renderManager.removeMainView(viewport);
		assertFalse(removed);
	}

	public void testRemoveNonExisitingMainPort() {
		String viewName = "camView";
		boolean removed = renderManager.removeMainView(viewName);
		assertFalse(removed);
	}

	public void testRemoveExisitingMainPortFromObject() {
		String viewName = "camView";
		ViewPort viewport = renderManager.createPreView(viewName, camera);
		boolean removed = renderManager.removePreView(viewport);
		assertTrue(removed);
	}

	public void testRemoveNullMainPort() {
		String viewName = "camView";
		ViewPort port = new ViewPort(viewName, camera);
		assertFalse(renderManager.removePreView(port));
	}

	public void testPostViewPort() {
		String viewName = "postView";
		ViewPort viewport = renderManager.createPostView(viewName, camera);
		assertNotNull(viewport);
		ViewPort retreivedPort = renderManager.getPostView(viewName);
		assertEquals(viewport, retreivedPort);
	}

	public void testRemovePostView() {
		String viewName = "postView";
		renderManager.createPostView(viewName, camera);
		boolean removed = renderManager.removePostView(viewName);
		assertTrue(removed);
	}

	public void testRemovePostViewByObject() {
		String viewName = "postView";
		ViewPort viewport = renderManager.createPostView(viewName, camera);
		boolean removed = renderManager.removePostView(viewport);
		assertTrue(removed);
	}

	public void testGetAndRemoveNullPostView() {
		String viewName = "Something";
		assertNull(renderManager.getPostView(viewName));
		assertFalse(renderManager.removePostView(viewName));
	}

	public void testRemoveNullPostView() {
		ViewPort viewport = null;
		assertFalse(renderManager.removePostView(viewport));
	}
	
	public void testNotifyReshapeWhileNoViewports() {
		int w = 0;
		int h = 0;
		renderManager.notifyReshape(w, h);
	}
	
	public void testReshapePreviewPorts() {
		String viewName = "preview";
		renderManager.createPreView(viewName, camera);
		int w = 15;
		int h = 20;
		renderManager.notifyReshape(w, h);
		assertEquals(w, camera.getWidth());
		assertEquals(h, camera.getHeight());
	}
	
	public void testReshapePostviewPorts() {
		String viewName = "preview";
		renderManager.createPostView(viewName, camera);
		int w = 25;
		int h = 30;
		renderManager.notifyReshape(w, h);
		assertEquals(w, camera.getWidth());
		assertEquals(h, camera.getHeight());
	}
	
	public void testReshapeViewPorts() {
		String viewName = "preview";
		renderManager.createMainView(viewName, camera);
		int w = 15;
		int h = 20;
		renderManager.notifyReshape(w, h);
		assertEquals(w, camera.getWidth());
		assertEquals(h, camera.getHeight());
	}
	
	public void testReshapeWitOuputFrameBufferMainView() {
		String viewName = "preview";
		ViewPort viewport = renderManager.createMainView(viewName, camera);
		FrameBuffer bufferOut = setupFrameBuffer();
		viewport.setOutputFrameBuffer(bufferOut);
		int w = 15;
		int h = 20;		
		renderManager.notifyReshape(w, h);
		assertEquals(camWidth, camera.getWidth());
		assertEquals(camHeight, camera.getHeight());
	}
	
	public void testReshapeWitOuputFrameBufferPreView() {
		String viewName = "preview";
		ViewPort viewport = renderManager.createPreView(viewName, camera);
		FrameBuffer bufferOut = setupFrameBuffer();
		viewport.setOutputFrameBuffer(bufferOut);
		int w = 15;
		int h = 20;		
		renderManager.notifyReshape(w, h);
		assertEquals(camWidth, camera.getWidth());
		assertEquals(camHeight, camera.getHeight());
	}
	
	public void testReshapeWitOuputFrameBufferPostView() {
		String viewName = "preview";
		ViewPort viewport = renderManager.createPostView(viewName, camera);
		FrameBuffer bufferOut = setupFrameBuffer();
		viewport.setOutputFrameBuffer(bufferOut);
		int w = 15;
		int h = 20;		
		renderManager.notifyReshape(w, h);
		assertEquals(camWidth, camera.getWidth());
		assertEquals(camHeight, camera.getHeight());
	}
	
	public void testReshapeWithSceneprocessors() {
		String viewName = "preview";
		ViewPort viewport = renderManager.createPreView(viewName, camera);
		SceneProcessorImpl processor = new SceneProcessorImpl();
		viewport.addProcessor(processor);
		int w = 15;
		int h = 20;		
		renderManager.notifyReshape(w, h);
		assertEquals(w, camera.getWidth());
		assertEquals(h, camera.getHeight());
	}
		
	/**
	 * Helper function for testing notifyReshape
	 * @return framebuffer
	 */
	private FrameBuffer setupFrameBuffer() {
		int frameWidth = 10;
		int frameHeight = 15;
		int samples = 2;
		FrameBuffer bufferOut = new FrameBuffer(frameWidth, frameHeight, samples);
		return bufferOut;
	}
	
	public void testFlushQueue() {
		renderManager.flushQueue(viewport);
	}
	
	public void testRenderViewPortQueuesForSky() {
		AppProfiler prof = new BasicProfiler();
		boolean flush = false;
		renderManager.setAppProfiler(prof);
		
		Geometry g = setupGeometry();
		viewport.getQueue().addToQueue(g, Bucket.Sky);
		
		renderManager.renderViewPortQueues(viewport, flush);
		float expectedStart = 0;
		float expectedEnd = 1;
		assertEquals(expectedStart, ((RendererImpl) renderer).getStart());
		assertEquals(expectedEnd, ((RendererImpl) renderer).getEnd());
	}
	
	public void testRenderViewPortQueuesForTransparant() {
		AppProfiler prof = new BasicProfiler();
		boolean flush = false;
		renderManager.setAppProfiler(prof);
		
		Geometry g = setupGeometry();
		viewport.getQueue().addToQueue(g, Bucket.Transparent);
		
		renderManager.renderViewPortQueues(viewport, flush);
		float expectedStart = 0;
		float expectedEnd = 0;
		assertEquals(expectedStart, ((RendererImpl) renderer).getStart());
		assertEquals(expectedEnd, ((RendererImpl) renderer).getEnd());
	}
	
	public void testRenderViewPortQueuesForMutipleGeo() {
		boolean flush = false;
		
		Geometry g1 = setupGeometry();
		viewport.getQueue().addToQueue(g1, Bucket.Transparent);
		Geometry g2 = setupGeometry();
		viewport.getQueue().addToQueue(g2, Bucket.Gui);
		Geometry g3 = setupGeometry();
		viewport.getQueue().addToQueue(g3, Bucket.Sky);
		
		renderManager.renderViewPortQueues(viewport, flush);
		float expectedStart = 0;
		float expectedEnd = 1;
		assertEquals(expectedStart, ((RendererImpl) renderer).getStart());
		assertEquals(expectedEnd, ((RendererImpl) renderer).getEnd());
	}
	
	public void testRenderViewPortQueuesForTransparantWithoutProf() {
		boolean flush = false;
		Geometry g = setupGeometry();
		viewport.getQueue().addToQueue(g, Bucket.Transparent);
		
		renderManager.renderViewPortQueues(viewport, flush);
		float expectedStart = 0;
		float expectedEnd = 0;
		assertEquals(expectedStart, ((RendererImpl) renderer).getStart());
		assertEquals(expectedEnd, ((RendererImpl) renderer).getEnd());
	}
	
	public void testRenderViewPortQueuesForGUI() {
		AppProfiler prof = new BasicProfiler();
		boolean flush = false;
		renderManager.setAppProfiler(prof);
		
		Geometry g = setupGeometry();
		viewport.getQueue().addToQueue(g, Bucket.Gui);
		
		renderManager.renderViewPortQueues(viewport, flush);		
		float expectedStart = 0;
		float expectedEnd = 1;
		assertEquals(expectedStart, ((RendererImpl) renderer).getStart());
		assertEquals(expectedEnd, ((RendererImpl) renderer).getEnd());
	}
	
	private Geometry setupGeometry() {
		String geoName = "geoName";
		Geometry g = new Geometry(geoName);
		
		String matName = "wood";
		AssetManager assetManager = new DesktopAssetManager();
		MaterialDef matdef = new MaterialDef(assetManager, matName);
		
		TechniqueDef def = new TechniqueDef("Default");
		matdef.addTechniqueDef(def);
		Material material = new Material(matdef);
		g.setMaterial(material);
		return g;
	}

}

/*
 * $CPS$ This comment was generated by CodePro. Do not edit it. patternId =
 * com.instantiations.assist.eclipse.pattern.testCasePattern strategyId =
 * com.instantiations.assist.eclipse.pattern.testCasePattern.junitTestCase
 * additionalTestNames = assertTrue = false callTestMethod = true createMain =
 * false createSetUp = true createTearDown = true createTestFixture = false
 * createTestStubs = true methods = package = com.jme3.renderer
 * package.sourceFolder = jme3-core/src/test/java superclassType =
 * junit.framework.TestCase testCase = RenderManagerTest testClassType =
 * com.jme3.renderer.RenderManager
 */