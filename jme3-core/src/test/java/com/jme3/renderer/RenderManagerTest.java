package com.jme3.renderer;

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
	private Camera camera;

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

	/**
	 * Run the void render(float, boolean) method test
	 */
	public void testRender() {
		fail("Newly generated method - fix or disable");
		// add test code here
		float tpf = 0.0F;
		boolean mainFrameBufferActive = false;
		// This class does not have a public, no argument constructor,
		// so the render() method can not be tested
		assertTrue(false);
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