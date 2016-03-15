package com.jme3.util;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
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

import com.jme3.audio.AudioBuffer;
import com.jme3.audio.openal.ALAudioRenderer;
import com.jme3.renderer.opengl.GLRenderer;
import com.jme3.texture.Image;
import com.jme3.util.NativeObjectManager.NativeObjectRef;

public class NativeObjectManagerTest {

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

	@Test(expected=IllegalArgumentException.class)
	public void regiseringObject() {
		NativeObjectManager nativeObjectManager = new NativeObjectManager();
		AudioBuffer audioBuffer = new AudioBuffer();
		nativeObjectManager.registerObject(audioBuffer);
	}
	
	@Test
	public void regiseringObject2() {
		NativeObjectManager nativeObjectManager = new NativeObjectManager();
		
		final Logger logger = Logger.getLogger(NativeObjectManager.class.getName());
		Handler handler = Mockito.mock(Handler.class);
		logger.addHandler(handler);
		logger.setLevel(Level.FINEST);
		
		HashMap<String, NativeObjectRef> refMap = nativeObjectManager.getRefMap();
		int size1 = refMap.size();
		
		AudioBuffer audioBuffer = new AudioBuffer();
		audioBuffer.setId(1);
		nativeObjectManager.registerObject(audioBuffer);
		int size2 = refMap.size();
		
		Mockito.verify(handler, Mockito.atLeastOnce()).publish(Mockito.any(LogRecord.class));
		logger.removeHandler(handler);
		
		assert size2 - size1 == 1;
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void deleteNativeObject() {
		NativeObjectManager nativeObjectManager = new NativeObjectManager();
		AudioBuffer audioBuffer = new AudioBuffer();
		audioBuffer.setId(1);
		nativeObjectManager.enqueueUnusedObject(audioBuffer);
		ALAudioRenderer alAudioRenderer = Mockito.mock(ALAudioRenderer.class);
		nativeObjectManager.deleteUnused(alAudioRenderer);
	}
	
	@Test
	public void deleteNativeObject2() {
		NativeObjectManager nativeObjectManager = new NativeObjectManager();
		
		final Logger logger = Logger.getLogger(NativeObjectManager.class.getName());
		Handler handler = Mockito.mock(Handler.class);
		logger.addHandler(handler);
		
		AudioBuffer audioBuffer = new AudioBuffer();
		audioBuffer.setId(-1);
		nativeObjectManager.enqueueUnusedObject(audioBuffer);
		ALAudioRenderer alAudioRenderer = Mockito.mock(ALAudioRenderer.class);
		nativeObjectManager.deleteUnused(alAudioRenderer);
        
		Mockito.verify(handler, Mockito.atLeast(1)).publish(Mockito.any(LogRecord.class));
		logger.removeHandler(handler);
	}
	
	@Test
	public void deleteNativeObject3() {
		NativeObjectManager nativeObjectManager = new NativeObjectManager();
		
		final Logger logger = Logger.getLogger(NativeObjectManager.class.getName());
		Handler handler = Mockito.mock(Handler.class);
		logger.addHandler(handler);
		logger.setLevel(Level.FINEST);
		
		AudioBuffer audioBuffer = new AudioBuffer();
		audioBuffer.setId(1);
		nativeObjectManager.registerObject(audioBuffer);
		nativeObjectManager.enqueueUnusedObject(audioBuffer);
		ALAudioRenderer alAudioRenderer = Mockito.mock(ALAudioRenderer.class);
		nativeObjectManager.deleteUnused(alAudioRenderer);
        
		Mockito.verify(handler, Mockito.atLeast(1)).publish(Mockito.any(LogRecord.class));
		logger.removeHandler(handler);
	}
	
	@Test
	public void deleteNativeObjectRefMap() {
		NativeObjectManager nativeObjectManager = new NativeObjectManager();
		
		AudioBuffer audioBuffer = new AudioBuffer();
		audioBuffer.setId(1);
		nativeObjectManager.registerObject(audioBuffer);
		HashMap<String, NativeObjectRef> refMap = nativeObjectManager.getRefMap();
		int size1 = refMap.size();
		
		nativeObjectManager.enqueueUnusedObject(audioBuffer);
		ALAudioRenderer alAudioRenderer = Mockito.mock(ALAudioRenderer.class);
		nativeObjectManager.deleteUnused(alAudioRenderer);
		int size2 = refMap.size();
		
		assert size1 - size2 == 1;
	}
}
