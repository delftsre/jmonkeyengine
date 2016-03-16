package com.jme3.renderer;

import junit.framework.TestCase;

public class ViewportManagerTest extends TestCase{
	
	ViewPortManager viewportManager;
	Camera camera;
	
	/**
	 * Perform pre-test initialization
	 *
	 * @throws Exception
	 *
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		camera = new Camera(1,1);
		viewportManager = new ViewPortManager();
	}
	
	/**
	 * Run the ViewPort createPreView(String, Camera) method test
	 */
	public void testCreatePreView() {
		String viewName = "camView";
		ViewPort viewPortCreated = viewportManager.createPreView(viewName, camera);
		assertNotNull(viewPortCreated);
		ViewPort retreivedViewport = viewportManager.getPreView(viewName);
		assertEquals(viewPortCreated, retreivedViewport);
	}

	public void testEmptyPreViewPortList() {
		String viewName = "noView";
		ViewPort viewPort = viewportManager.getPreView(viewName);
		assertNull(viewPort);
	}

	public void testNonExistingPreViewPortList() {
		String viewName = "camView";
		viewportManager.createPreView(viewName, camera);
		String viewNameTwo = "anotherCam";
		assertNull(viewportManager.getPreView(viewNameTwo));
	}

	public void testRemoveExisitingPreViewPort() {
		String viewName = "camView";
		viewportManager.createPreView(viewName, camera);
		boolean removed = viewportManager.removePreView(viewName);
		assertTrue(removed);
	}

	public void testRemoveNonExisitingPreViewPort() {
		String viewName = "camView";
		viewportManager.createPreView("aview", camera);
		boolean removed = viewportManager.removePreView(viewName);
		assertFalse(removed);
	}

	public void testRemoveExisitingPreViewPortFromObject() {
		String viewName = "camView";
		ViewPort viewport = viewportManager.createPreView(viewName, camera);
		boolean removed = viewportManager.removePreView(viewport);
		assertTrue(removed);
	}

	public void testRemoveNullPreViewPort() {
		String viewName = "camView";
		ViewPort port = new ViewPort(viewName, camera);
		boolean removed = viewportManager.removePreView(port);
		assertFalse(removed);
	}

	public void testCreateMainView() {
		String viewName = "camView";
		ViewPort viewPortCreated = viewportManager.createMainView(viewName, camera);
		assertNotNull(viewPortCreated);
		ViewPort retreivedViewport = viewportManager.getMainView(viewName);
		assertEquals(viewPortCreated, retreivedViewport);
	}

	public void testEmptyViewPortList() {
		String viewName = "noView";
		ViewPort viewPort = viewportManager.getMainView(viewName);
		assertNull(viewPort);
	}

	public void testNonExistingMainPortList() {
		String viewName = "camView";
		viewportManager.createMainView(viewName, camera);
		String viewNameTwo = "anotherCam";
		ViewPort viewport = viewportManager.getMainView(viewNameTwo);
		assertNull(viewport);
	}

	public void testRemoveExisitingMainPort() {
		String viewName = "camView";
		viewportManager.createMainView(viewName, camera);
		boolean removed = viewportManager.removeMainView(viewName);
		assertTrue(removed);
	}

	public void testRemoveMainPortObject() {
		ViewPort viewport = null;
		boolean removed = viewportManager.removeMainView(viewport);
		assertFalse(removed);
	}

	public void testRemoveNonExisitingMainPort() {
		String viewName = "camView";
		viewportManager.createMainView("aview", camera);
		boolean removed = viewportManager.removeMainView(viewName);
		assertFalse(removed);
	}

	public void testRemoveExisitingMainPortFromObject() {
		String viewName = "camView";
		ViewPort viewport = viewportManager.createPreView(viewName, camera);
		boolean removed = viewportManager.removePreView(viewport);
		assertTrue(removed);
	}

	public void testRemoveNullMainPort() {
		String viewName = "camView";
		ViewPort port = new ViewPort(viewName, camera);
		assertFalse(viewportManager.removePreView(port));
	}

	public void testPostViewPort() {
		String viewName = "postView";
		ViewPort viewport = viewportManager.createPostView(viewName, camera);
		assertNotNull(viewport);
		ViewPort retreivedPort = viewportManager.getPostView(viewName);
		assertEquals(viewport, retreivedPort);
	}

	public void testGetNonExistingPostViewPort() {
		String viewName = "postView";
		ViewPort viewport = viewportManager.createPostView("aview", camera);
		assertNotNull(viewport);
		ViewPort retreivedPort = viewportManager.getPostView(viewName);
		assertNull(retreivedPort);
	}

	public void testGetViews() {
		assertNotNull(viewportManager.getPostViews());
		assertNotNull(viewportManager.getPreViews());
		assertNotNull(viewportManager.getMainViews());
	}

	public void testRemovePostView() {
		String viewName = "postView";
		viewportManager.createPostView(viewName, camera);
		boolean removed = viewportManager.removePostView(viewName);
		assertTrue(removed);
	}

	public void testRemoveNonExistingPostView() {
		String viewName = "postView";
		viewportManager.createPostView("aview", camera);
		boolean removed = viewportManager.removePostView(viewName);
		assertFalse(removed);
	}

	public void testRemovePostViewByObject() {
		String viewName = "postView";
		ViewPort viewport = viewportManager.createPostView(viewName, camera);
		boolean removed = viewportManager.removePostView(viewport);
		assertTrue(removed);
	}

	public void testGetAndRemoveNullPostView() {
		String viewName = "Something";
		assertNull(viewportManager.getPostView(viewName));
		assertFalse(viewportManager.removePostView(viewName));
	}

	public void testRemoveNullPostView() {
		ViewPort viewport = null;
		assertFalse(viewportManager.removePostView(viewport));
	}



}
