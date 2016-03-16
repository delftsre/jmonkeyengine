package com.jme3.renderer;

import java.nio.Buffer;

import org.mockito.Mockito;

import com.jme3.app.BasicProfiler;
import com.jme3.asset.AssetManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.light.LightFilter;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.material.RenderState;
import com.jme3.material.TechniqueDef;
import com.jme3.post.SceneProcessor;
import com.jme3.profile.AppProfiler;
import com.jme3.renderer.queue.GeometryList;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.system.NullRenderer;
import com.jme3.system.Timer;
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
		renderManager.createPreView("aview", camera);
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
		renderManager.createMainView("aview", camera);
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

	public void testGetNonExistingPostViewPort() {
		String viewName = "postView";
		ViewPort viewport = renderManager.createPostView("aview", camera);
		assertNotNull(viewport);
		ViewPort retreivedPort = renderManager.getPostView(viewName);
		assertNull(retreivedPort);
	}

	public void testGetViews() {
		assertNotNull(renderManager.getPostViews());
		assertNotNull(renderManager.getPreViews());
		assertNotNull(renderManager.getMainViews());
	}

	public void testRemovePostView() {
		String viewName = "postView";
		renderManager.createPostView(viewName, camera);
		boolean removed = renderManager.removePostView(viewName);
		assertTrue(removed);
	}

	public void testRemoveNonExistingPostView() {
		String viewName = "postView";
		renderManager.createPostView("aview", camera);
		boolean removed = renderManager.removePostView(viewName);
		assertFalse(removed);
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
	 * 
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
		AppProfiler prof = Mockito.mock(BasicProfiler.class);
		boolean flush = false;
		renderManager.setAppProfiler(prof);

		Geometry g = setupGeometry("g1");
		viewport.getQueue().addToQueue(g, Bucket.Sky);

		renderManager.renderViewPortQueues(viewport, flush);
		float expectedStart = 0;
		float expectedEnd = 1;
		assertEquals(expectedStart, ((RendererImpl) renderer).getStart());
		assertEquals(expectedEnd, ((RendererImpl) renderer).getEnd());
	}

	public void testRenderViewPortQueuesForTransparant() {
		AppProfiler prof = Mockito.mock(BasicProfiler.class);
		boolean flush = false;
		renderManager.setAppProfiler(prof);
		boolean handleTranslucentBucket = false;
		renderManager.setHandleTranslucentBucket(handleTranslucentBucket);

		Geometry g = setupGeometry("g1");
		viewport.getQueue().addToQueue(g, Bucket.Transparent);

		renderManager.renderViewPortQueues(viewport, flush);
		float expectedStart = 0;
		float expectedEnd = 0;
		assertEquals(expectedStart, ((RendererImpl) renderer).getStart());
		assertEquals(expectedEnd, ((RendererImpl) renderer).getEnd());
	}
	
	public void testRenderTraslucentQueues() {
		boolean handleTranslucentBucket = true;
		renderManager.setHandleTranslucentBucket(handleTranslucentBucket);

		Geometry g = setupGeometry("g1");
		viewport.getQueue().addToQueue(g, Bucket.Translucent);

		renderManager.renderTranslucentQueue(viewport);
		boolean empty = viewport.getQueue().isQueueEmpty(Bucket.Translucent);
		assertTrue(empty);
	}
	
	public void testDoNotRenderTranslucentQueues() {
		boolean handleTranslucentBucket = false;
		renderManager.setHandleTranslucentBucket(handleTranslucentBucket);
		
		Geometry g = setupGeometry("g1");
		viewport.getQueue().addToQueue(g, Bucket.Translucent);
		
		renderManager.renderTranslucentQueue(viewport);
		boolean empty = viewport.getQueue().isQueueEmpty(Bucket.Translucent);
		assertFalse(empty);
	}

	public void testRenderViewPortQueuesForMutipleGeo() {
		boolean flush = false;

		Geometry g1 = setupGeometry("g1");
		viewport.getQueue().addToQueue(g1, Bucket.Transparent);
		Geometry g2 = setupGeometry("g2");
		viewport.getQueue().addToQueue(g2, Bucket.Gui);
		Geometry g3 = setupGeometry("g3");
		viewport.getQueue().addToQueue(g3, Bucket.Sky);

		renderManager.renderViewPortQueues(viewport, flush);
		float expectedStart = 0;
		float expectedEnd = 1;
		assertEquals(expectedStart, ((RendererImpl) renderer).getStart());
		assertEquals(expectedEnd, ((RendererImpl) renderer).getEnd());
	}
	
	public void testRender() {		
		 
		FrameBuffer out = setupFrameBuffer();
		renderManager.createMainView(viewName, camera).setOutputFrameBuffer(out);
		renderManager.createPostView(viewName, camera).setOutputFrameBuffer(out);
		renderManager.createPreView(viewName, camera).setOutputFrameBuffer(out);

		float tpf = 10;
		boolean mainFrameBufferActive = true;
		
		Timer timer = Mockito.mock(Timer.class);
		renderManager.setTimer(timer);
		
		renderManager.render(tpf, mainFrameBufferActive);
		assertEquals(1, renderManager.getPreViews().size());
		assertEquals(1, renderManager.getPostViews().size());
		assertEquals(1, renderManager.getMainViews().size());
	}
	
	public void testRenderWithoutOutputBufferAndMainFrameBufferActive() {
		Camera cam = Mockito.mock(Camera.class);
		renderManager.createMainView(viewName, cam);
		renderManager.createPostView(viewName, cam);
		renderManager.createPreView(viewName, cam);
		
		AppProfiler prof = Mockito.mock(BasicProfiler.class);
		renderManager.setAppProfiler(prof);

		float tpf = 10;
		boolean mainFrameBufferActive = false;
		
		Timer timer = Mockito.mock(Timer.class);
		renderManager.setTimer(timer);
		
		renderManager.render(tpf, mainFrameBufferActive);
		assertEquals(1, renderManager.getPreViews().size());
		assertEquals(1, renderManager.getPostViews().size());
		assertEquals(1, renderManager.getMainViews().size());
	}
	
	public void testRenderofNullRenderer() {
		renderer = new NullRenderer();
		renderManager = new RenderManager(renderer);

		float tpf = 10;
		boolean mainFrameBufferActive = false;		
		
		renderManager.render(tpf, mainFrameBufferActive);
		assertEquals(0, renderManager.getPreViews().size());
		assertEquals(0, renderManager.getPostViews().size());
		assertEquals(0, renderManager.getMainViews().size());
	}
	
	public void testRenderWithMainFrameBufferActive() {
		 
		renderManager.createMainView(viewName, camera);
		renderManager.createPostView(viewName, camera);
		renderManager.createPreView(viewName, camera);
		
		AppProfiler prof = Mockito.mock(BasicProfiler.class);
		renderManager.setAppProfiler(prof);

		float tpf = 10;
		boolean mainFrameBufferActive = true;
		
		Timer timer = Mockito.mock(Timer.class);
		renderManager.setTimer(timer);
		
		renderManager.render(tpf, mainFrameBufferActive);
		assertEquals(1, renderManager.getPreViews().size());
		assertEquals(1, renderManager.getPostViews().size());
		assertEquals(1, renderManager.getMainViews().size());
	}

	public void testRenderViewPortQueuesForTransparantWithoutProf() {
		boolean flush = false;
		String geoName = "geoName";
		Geometry g = setupGeometry(geoName);
		viewport.getQueue().addToQueue(g, Bucket.Transparent);

		renderManager.renderViewPortQueues(viewport, flush);
		float expectedStart = 0;
		float expectedEnd = 0;
		assertEquals(expectedStart, ((RendererImpl) renderer).getStart());
		assertEquals(expectedEnd, ((RendererImpl) renderer).getEnd());
	}

	public void testRenderViewPortQueuesForGUI() {
		AppProfiler prof = Mockito.mock(BasicProfiler.class);
		boolean flush = false;
		renderManager.setAppProfiler(prof);

		String geoName = "geoName";
		Geometry g = setupGeometry(geoName);
		viewport.getQueue().addToQueue(g, Bucket.Gui);

		renderManager.renderViewPortQueues(viewport, flush);
		float expectedStart = 0;
		float expectedEnd = 1;
		assertEquals(expectedStart, ((RendererImpl) renderer).getStart());
		assertEquals(expectedEnd, ((RendererImpl) renderer).getEnd());
	}

	private Geometry setupGeometry(String geoName) {
		Geometry g = new Geometry(geoName);
		/*
		 * String matName = "wood"; AssetManager assetManager = new
		 * DesktopAssetManager(); MaterialDef matdef = new
		 * MaterialDef(assetManager, matName); TechniqueDef def = new
		 * TechniqueDef("Default"); matdef.addTechniqueDef(def); Material
		 * material = new Material(matdef);
		 */

		Material material = Mockito.mock(Material.class);
		g.setMaterial(material);
		return g;
	}

	public void testRenderViewPortRawEmpty() {
		renderManager.renderViewPortRaw(viewport);
		assertTrue(viewport.queue.isQueueEmpty(Bucket.Gui));
		assertTrue(viewport.queue.isQueueEmpty(Bucket.Sky));
		assertTrue(viewport.queue.isQueueEmpty(Bucket.Translucent));
	}

	public void testRenderViewPortRaw() {
		Spatial scene = Mockito.mock(Spatial.class);
		viewport.attachScene(scene);
		renderManager.renderViewPortRaw(viewport);
		assertTrue(viewport.queue.isQueueEmpty(Bucket.Gui));
		assertTrue(viewport.queue.isQueueEmpty(Bucket.Sky));
		assertTrue(viewport.queue.isQueueEmpty(Bucket.Translucent));
	}

	public void testRenderViewPortWithAppProfiler() {
		float tpf = 60;
		AppProfiler prof = Mockito.mock(BasicProfiler.class);
		renderManager.setAppProfiler(prof);
		renderManager.renderViewPort(viewport, tpf);
		assertTrue(viewport.queue.isQueueEmpty(Bucket.Gui));
		assertTrue(viewport.queue.isQueueEmpty(Bucket.Sky));
		assertTrue(viewport.queue.isQueueEmpty(Bucket.Translucent));
	}

	public void testDisabledViewportRendererViewport() {
		float tpf = 60;
		viewport.setEnabled(false);
		Spatial scene = Mockito.mock(Spatial.class);
		viewport.attachScene(scene);
		renderManager.renderViewPort(viewport, tpf);
		int actualSceneSize = viewport.getScenes().size();
		int expectedSize = 1;
		assertEquals(expectedSize, actualSceneSize);
	}

	public void testEnabledViewportRendererViewport() {
		float tpf = 60;

		renderManager.renderViewPort(viewport, tpf);
		int actualSceneSize = viewport.getScenes().size();
		int expectedSize = 0;
		assertEquals(expectedSize, actualSceneSize);
	}

	public void testViewPortRendererWithSceneProcessors() {
		float tpf = 60;
		boolean clearDepth = true;
		boolean clearColor = true;
		AppProfiler prof = null;
		renderManager.setAppProfiler(prof);
		SceneProcessor processor = Mockito.mock(SceneProcessor.class);
		viewport.addProcessor(processor);
		viewport.setClearDepth(clearDepth);
		viewport.setClearColor(clearColor);

		renderManager.renderViewPort(viewport, tpf);
		int actualSceneSize = viewport.getScenes().size();
		int expectedSize = 0;
		assertEquals(expectedSize, actualSceneSize);
	}

	public void testViewPortRendererWithClearStencil() {
		float tpf = 60;
		boolean clearStencil = true;
		viewport.setClearStencil(clearStencil);

		renderManager.renderViewPort(viewport, tpf);
		int actualSceneSize = viewport.getScenes().size();
		int expectedSize = 0;
		assertEquals(expectedSize, actualSceneSize);
	}

	public void testViewPortRendererWithClearColor() {
		float tpf = 60;
		boolean clearColor = true;
		viewport.setClearColor(clearColor);

		renderManager.renderViewPort(viewport, tpf);
		int actualSceneSize = viewport.getScenes().size();
		int expectedSize = 0;
		assertEquals(expectedSize, actualSceneSize);
	}

	public void testRenderGeometry() {
		Geometry geo1 = setupGeometry("g1");
		Geometry geo2 = setupGeometry("g2");
		GeometryList list = new GeometryList(null);
		list.add(geo1);
		list.add(geo2);
		renderManager.renderGeometryList(list);
		assertNull(renderManager.getForcedTechnique());
	}

	public void testRenderGeometryWithForcedTechnique() {
		Geometry geo1 = setupGeometryWithMaterial("g2");
		renderManager.setForcedTechnique("SomeTech");
		renderManager.setForcedMaterial(geo1.getMaterial());
		String techName = "SomeTech";		
		geo1.getMaterial().selectTechnique(techName, renderManager);
		renderManager.renderGeometry(geo1);
		assertEquals(techName, renderManager.getForcedTechnique());
	}
	
	public void testRenderGeometryWithForcedTechniqueAndNoRenderState() {
		boolean renderstate = false;
		Geometry geo1 = setupGeometryWithMaterial("g2", renderstate);		
		renderManager.setForcedTechnique("SomeTech");
		renderManager.setForcedMaterial(geo1.getMaterial());
		String techName = "SomeTech";		
		renderManager.renderGeometry(geo1);
		assertEquals(techName, renderManager.getForcedTechnique());
	}
	
	public void testRenderGeometryWithoutForcedTechniqueButForcedMaterialSet() {
		Geometry geo1 = setupGeometryWithMaterial("g2");
		String name = "SomeTechWhichIsnotInRenderer";
		renderManager.setForcedTechnique(name);
		renderManager.renderGeometry(geo1);
		assertEquals(name, renderManager.getForcedTechnique());
	}
	
	public void testRenderGeometryWithForcedMaterial() {
		Geometry geo1 = setupGeometryWithMaterial("g2");
		boolean ignoreTransform = true;
		geo1.setIgnoreTransform(ignoreTransform);
		LightFilter light = null;
		renderManager.setLightFilter(light);
		renderManager.setForcedMaterial(geo1.getMaterial());
		renderManager.renderGeometry(geo1);
		assertNull(renderManager.getForcedTechnique());
	}
	
	public void testRenderGeometryWithoutForcedTechniqueButWithForcedMaterial() {
		Geometry geo1 = setupGeometryWithMaterial("g2");
		String name = "SomeTechWhichIsnotInRenderer";
		renderManager.setForcedTechnique(name);
		renderManager.setForcedMaterial(geo1.getMaterial());
		renderManager.renderGeometry(geo1);
		assertEquals(name, renderManager.getForcedTechnique());
	}

	private Geometry setupGeometryWithMaterial(String geoName) {
		return setupGeometryWithMaterial(geoName, true);
	}
	
	private Geometry setupGeometryWithMaterial(String geoName, boolean renderstate) {
		Geometry g = new Geometry(geoName);

		String matName = "wood";
		AssetManager assetManager = new DesktopAssetManager();
		MaterialDef matdef = new MaterialDef(assetManager, matName);
		TechniqueDef defaultTech = new TechniqueDef("Default");
		TechniqueDef def = new TechniqueDef("SomeTech");
		
		if (renderstate) {
			RenderState state = Mockito.mock(RenderState.class);
			def.setForcedRenderState(state);
		}
		
		matdef.addTechniqueDef(def);
		matdef.addTechniqueDef(defaultTech);
		Material material = new Material(matdef);
		g.setMaterial(material);
		return g;
	}
	
	public void testSetCameraWithLightFilter() {
		LightFilter filter = Mockito.mock(LightFilter.class);
		renderManager.setLightFilter(filter);
		 
		boolean ortho = true;
		renderManager.setCamera(camera, ortho);
		Camera actual = renderManager.getCurrentCamera();
		assertNotNull(actual);
		assertEquals(camera, actual);
	}
	
	public void testSinglePassLightBatchSizeOne() {
		int singlePassLightBatchSize = 0;
		renderManager.setSinglePassLightBatchSize(singlePassLightBatchSize);
		int expected = 1;
		int actual = renderManager.getSinglePassLightBatchSize();
		assertEquals(expected, actual);
	}
	
	public void testSinglePassLightBatchSizeRandom() {
		int singlePassLightBatchSize = 2;
		renderManager.setSinglePassLightBatchSize(singlePassLightBatchSize);
		int expected = 2;
		int actual = renderManager.getSinglePassLightBatchSize();
		assertEquals(expected, actual);
	}
	
	public void testHandleTranslucentBucket() {
		boolean handleTranslucentBucket = true;
		renderManager.setHandleTranslucentBucket(handleTranslucentBucket);
		assertTrue(renderManager.isHandleTranslucentBucket());
	}
	
	public void testPreloadSpatialNode() {
		renderer = Mockito.mock(Renderer.class);
		renderManager = new RenderManager(renderer);
		Node scene = new Node("NodeM");
		Node child = Mockito.mock(Node.class);
		scene.attachChild(child);
		Geometry geo = setupGeometry("Geo");
		
		Mesh mesh = new Mesh();
		VertexBuffer vb1 = Mockito.mock(VertexBuffer.class);
		Buffer buffValue = Mockito.mock(Buffer.class);
		Mockito.when(vb1.getBufferType()).thenReturn(Type.Color);
		Mockito.when(vb1.getUsage()).thenReturn(Usage.Stream);
		Mockito.when(vb1.getData()).thenReturn(buffValue);
				
		mesh.setBuffer(vb1);
		geo.setMesh(mesh);
		
		geo.setMesh(mesh);
		scene.attachChild(geo);
		renderManager.preloadScene(scene);
		Mockito.verify(renderer, Mockito.times(1)).updateBufferData(vb1);
	}

	public void testPreloadSpatialNodeCPUOnly() {
		renderer = Mockito.mock(Renderer.class);
		renderManager = new RenderManager(renderer);
		Node scene = new Node("NodeM");
		Node child = Mockito.mock(Node.class);
		scene.attachChild(child);
		Geometry geo = setupGeometry("Geo");
		
		Mesh mesh = new Mesh();
		VertexBuffer vb1 = Mockito.mock(VertexBuffer.class);
		Buffer buffValue = Mockito.mock(Buffer.class);
		Mockito.when(vb1.getBufferType()).thenReturn(Type.Color);
		Mockito.when(vb1.getUsage()).thenReturn(Usage.CpuOnly);
		Mockito.when(vb1.getData()).thenReturn(buffValue);
				
		mesh.setBuffer(vb1);
		geo.setMesh(mesh);
		
		geo.setMesh(mesh);
		scene.attachChild(geo);
		renderManager.preloadScene(scene);
		Mockito.verify(renderer, Mockito.times(0)).updateBufferData(vb1);
	}
	
	public void testPreloadSpatialNodeWithNullDataBufferAndCPUOnly() {
		renderer = Mockito.mock(Renderer.class);
		renderManager = new RenderManager(renderer);
		Node scene = new Node("NodeM");
		Node child = Mockito.mock(Node.class);
		scene.attachChild(child);
		Geometry geo = setupGeometry("Geo");
		
		Mesh mesh = new Mesh();
		VertexBuffer vb1 = Mockito.mock(VertexBuffer.class);
		Buffer buffValue = null;
		Mockito.when(vb1.getBufferType()).thenReturn(Type.Color);
		Mockito.when(vb1.getUsage()).thenReturn(Usage.CpuOnly);
		Mockito.when(vb1.getData()).thenReturn(buffValue);
				
		mesh.setBuffer(vb1);
		geo.setMesh(mesh);
		
		geo.setMesh(mesh);
		scene.attachChild(geo);
		renderManager.preloadScene(scene);
		Mockito.verify(renderer, Mockito.times(0)).updateBufferData(vb1);
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