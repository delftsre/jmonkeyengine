/**
 * 
 */
package com.jme3.post;

import static org.junit.Assert.*;

import java.util.EnumSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Caps;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.ViewPort;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Texture;

/**
 * @author raies
 *
 */
public class FilterPostProcessorTest {
	
	private AssetManager assetManager;
	private FilterPostProcessor filterPostProcessor;
	private EnumSet<Caps> caps;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		assetManager = Mockito.mock(AssetManager.class);
		filterPostProcessor = new FilterPostProcessor(assetManager);		
		caps = EnumSet.noneOf(Caps.class);		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	//@Test
	public void testPostFrameForCapsOpenGL32Condition() {		
		FrameBuffer out = Mockito.mock(FrameBuffer.class);
		filterPostProcessor.postFrame(out );
	}
	
	@Test
	public void testReshapeForCapsOpenGL32Condition() {	
		int numSamples = 2;
		filterPostProcessor.setNumSamples(numSamples );
		
		caps.add(Caps.OpenGL32);
		caps.add(Caps.FrameBufferMultisample);
		
		int cW = 9;
		int cH = 12;
		Camera camera = new Camera(cW, cH);
		FrameBuffer framebuffer = Mockito.mock(FrameBuffer.class);
		ViewPort viewportForReshape = Mockito.mock(ViewPort.class);
		ViewPort viewportForFPP = Mockito.mock(ViewPort.class);
		
		int w = 5;
		int h = 5;
		Mockito.when(viewportForReshape.getCamera()).thenReturn(camera);
		Mockito.when(viewportForFPP.getCamera()).thenReturn(camera);
		Mockito.when(viewportForFPP.getOutputFrameBuffer()).thenReturn(framebuffer);
		
		RenderManager rm = Mockito.mock(RenderManager.class);
		Renderer renderer = Mockito.mock(Renderer.class);
		Mockito.when(rm.getRenderer()).thenReturn(renderer);
		Mockito.when(renderer.getCaps()).thenReturn(caps);
		filterPostProcessor.initialize(rm , viewportForFPP);
		filterPostProcessor.reshape(viewportForReshape, w, h);
		
		Texture filterText = filterPostProcessor.getFilterTexture();
		Texture depthText = filterPostProcessor.getDepthTexture();
		assertEquals(w, filterText.getImage().getWidth());
		assertEquals(h, filterText.getImage().getHeight());
		assertEquals(w, depthText.getImage().getWidth());
		assertEquals(h, depthText.getImage().getHeight());
	}
	
	@Test
	public void testReshapeForSkippingCapsOpenGL32Condition() {	
		int numSamples = 2;
		filterPostProcessor.setNumSamples(numSamples );
		
		//Do not add Caps.OpenGL32
		caps.add(Caps.FrameBufferMultisample);
		
		int cW = 9;
		int cH = 12;
		Camera camera = new Camera(cW, cH);
		FrameBuffer framebuffer = Mockito.mock(FrameBuffer.class);
		ViewPort viewportForReshape = Mockito.mock(ViewPort.class);
		ViewPort viewportForFPP = Mockito.mock(ViewPort.class);
		
		int w = 5;
		int h = 5;
		Mockito.when(viewportForReshape.getCamera()).thenReturn(camera);
		Mockito.when(viewportForFPP.getCamera()).thenReturn(camera);
		Mockito.when(viewportForFPP.getOutputFrameBuffer()).thenReturn(framebuffer);
		
		RenderManager rm = Mockito.mock(RenderManager.class);
		Renderer renderer = Mockito.mock(Renderer.class);
		Mockito.when(rm.getRenderer()).thenReturn(renderer);
		Mockito.when(renderer.getCaps()).thenReturn(caps);
		filterPostProcessor.initialize(rm , viewportForFPP);
		filterPostProcessor.reshape(viewportForReshape, w, h);
		
		Texture filterText = filterPostProcessor.getFilterTexture();
		Texture depthText = filterPostProcessor.getDepthTexture();
		assertEquals(w, filterText.getImage().getWidth());
		assertEquals(h, filterText.getImage().getHeight());
		assertNull(depthText);
	}

}
