package com.jme3.renderer.opengl;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.util.EnumSet;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.renderer.Caps;

public class TextureUtilTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testInitalizeMethod() {
		GL gl = Mockito.mock(GL.class);
		GL2 gl2 = Mockito.mock(GL2.class);
		GLExt glext = Mockito.mock(GLExt.class);
		TextureUtil textureUtil = new TextureUtil(gl, gl2, glext);
		EnumSet<Caps> caps = (EnumSet<Caps>) Mockito.mock(EnumSet.class);
		
		final Logger logger = Logger.getLogger(TextureUtil.class.getName());
		Handler handler = Mockito.mock(Handler.class);
		logger.addHandler(handler);
		logger.setLevel(Level.FINE);
		textureUtil.initialize(caps);
		Mockito.verify(handler, Mockito.atLeastOnce()).publish(Mockito.any(LogRecord.class));
		logger.removeHandler(handler);
		
		handler = Mockito.mock(Handler.class);
		logger.addHandler(handler);
		logger.setLevel(Level.CONFIG);
		textureUtil.initialize(caps);
		Mockito.verify(handler, Mockito.never()).publish(Mockito.any(LogRecord.class));
	}

}
