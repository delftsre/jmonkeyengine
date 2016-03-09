package com.jme3.renderer;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.texture.FrameBuffer;

public class ViewPortTest {
	Camera cam;
	ViewPort viewport;
	
	 @Before
	    public void setUp() throws Exception {
			cam = new Camera();
			viewport = new ViewPort("ViewPort",cam);	       
	    }
	
	@Test
    public void testConstructor() {
		Assert.assertNotNull(viewport);        
		Assert.assertNotNull(cam); 
		Assert.assertEquals(viewport.getName(), "ViewPort");
    }
	
	@Test
    public void testGetCamera() {
		Assert.assertNotNull(viewport.getCamera());
    }
	
	@Test
    public void testGetQueue() {
		Assert.assertNotNull(viewport.getQueue());
    }
	
	
	//Test removing and adding processes
	@Test
    public void testSceneProcessor1() {
		Assert.assertTrue(viewport.getProcessors().isEmpty());
		SceneProcessorImpl sceneProcessor = new SceneProcessorImpl();
		viewport.addProcessor(sceneProcessor);	
		Assert.assertFalse(viewport.getProcessors().isEmpty());
		viewport.removeProcessor(sceneProcessor);
		Assert.assertTrue(viewport.getProcessors().isEmpty());
			
    }
	
	//Test clearing processes
		@Test
	    public void testSceneProcessor2() {
			Assert.assertTrue(viewport.getProcessors().isEmpty());
			SceneProcessorImpl sceneProcessor1 = new SceneProcessorImpl();
			SceneProcessorImpl sceneProcessor2 = new SceneProcessorImpl();
			viewport.addProcessor(sceneProcessor1);	
			viewport.addProcessor(sceneProcessor2);
			Assert.assertFalse(viewport.getProcessors().isEmpty());
			viewport.clearProcessors();
			Assert.assertTrue(viewport.getProcessors().isEmpty());
				
	    }
		
		@Test(expected=IllegalArgumentException.class)
	    public void testAddProcessorNull() {
			Assert.assertTrue(viewport.getProcessors().isEmpty());
			SceneProcessorImpl sceneProcessor = null;
			viewport.addProcessor(sceneProcessor);					
	    }
		
		@Test(expected=IllegalArgumentException.class)
	    public void testRemoveProcessorNull() {
			Assert.assertTrue(viewport.getProcessors().isEmpty());
			viewport.removeProcessor(null);				
	    }
		
		@Test
	    public void testClearDepth() {
			Assert.assertFalse(viewport.isClearDepth());
			viewport.setClearDepth(true);
			Assert.assertTrue(viewport.isClearDepth());
		}
		
		@Test
	    public void testClearColor() {
			Assert.assertFalse(viewport.isClearColor());
			viewport.setClearColor(true);
			Assert.assertTrue(viewport.isClearColor());
			
		}
		
		@Test
	    public void testClearStencil() {
			Assert.assertFalse(viewport.clearStencil);
			viewport.setClearStencil(true);
			Assert.assertTrue(viewport.isClearStencil());
		}
		
		@Test
	    public void testEnabled() {
			Assert.assertTrue(viewport.isEnabled());
			viewport.setEnabled(false);
			Assert.assertFalse(viewport.isEnabled());
			
		}
		
		@Test
	    public void testClearFlags() {
			Assert.assertFalse(viewport.isClearColor());
			Assert.assertFalse(viewport.isClearDepth());
			Assert.assertFalse(viewport.isClearStencil());
			viewport.setClearFlags(true, true, true);
			Assert.assertTrue(viewport.isClearColor());
			Assert.assertTrue(viewport.isClearDepth());
			Assert.assertTrue(viewport.isClearStencil());
			
		}
		
		@Test
	    public void testOutputFrameBuffer() {
			Assert.assertNull(viewport.getOutputFrameBuffer());
			FrameBuffer buffer = new FrameBuffer(10,10,10);
			viewport.setOutputFrameBuffer(buffer);
			Assert.assertNotNull(viewport.getOutputFrameBuffer());
			
		}
		
		@Test
	    public void TestSpatial() {
			Assert.assertTrue(viewport.getScenes().isEmpty());
			SpatialImpl spatial = new SpatialImpl();
			viewport.attachScene(spatial);
			Assert.assertFalse(viewport.getScenes().isEmpty());
			viewport.detachScene(spatial);
			Assert.assertTrue(viewport.getScenes().isEmpty());
		}
		
		@Test(expected=IllegalArgumentException.class)
	    public void TestSpatialAttachNull() {
			Assert.assertTrue(viewport.getScenes().isEmpty());
			SpatialImpl spatial = null;
			viewport.attachScene(spatial);
		}
		
		@Test(expected=IllegalArgumentException.class)
	    public void TestSpatialDetachNull() {
			Assert.assertTrue(viewport.getScenes().isEmpty());
			viewport.detachScene(null);
		}
		
		@Test
	    public void TestSpatialAttachGeometry() {
			Assert.assertTrue(viewport.getScenes().isEmpty());
			Geometry geo = new Geometry();
			viewport.attachScene(geo);
			Assert.assertFalse(viewport.getScenes().isEmpty());
			
			
		}
		
		@Test
	    public void TestSpatialDetachGeometry() {
			Assert.assertTrue(viewport.getScenes().isEmpty());
			Geometry geo = new Geometry();
			viewport.attachScene(geo);
			viewport.detachScene(geo);
			Assert.assertTrue(viewport.getScenes().isEmpty());	
		}
		
		@Test
	    public void testScene() {
			SpatialImpl spatial1 = new SpatialImpl();
			SpatialImpl spatial2 = new SpatialImpl();
			viewport.attachScene(spatial1);
			viewport.attachScene(spatial2);
			Assert.assertFalse(viewport.getScenes().isEmpty());
			viewport.clearScenes();
			Assert.assertTrue(viewport.getScenes().isEmpty());	
	    }
		
		@Test
	    public void testBackgroundColor() {
			Assert.assertEquals(viewport.getBackgroundColor(),new ColorRGBA(0,0,0,0));
			viewport.setBackgroundColor(new ColorRGBA(1,1,1,1));
			Assert.assertEquals(viewport.getBackgroundColor(),new ColorRGBA(1,1,1,1));
		}
}
