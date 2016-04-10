package com.jme3.util;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import com.jme3.scene.VertexBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.FrameBuffer;
import com.jme3.shader.Shader;
import com.jme3.shader.Shader.ShaderSource;
import com.jme3.audio.AudioBuffer;
import com.jme3.audio.AudioStream;
import com.jme3.audio.LowPassFilter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

public class NativeObjectTest {

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
	public void test() {
		VertexBuffer vertexBuffer = new VertexBuffer();
		Image image = new Image();
		FrameBuffer frameBuffer = new FrameBuffer(1,1,1);
		Shader shader = new Shader();
		ShaderSource shaderSource = new ShaderSource();
		AudioBuffer audioBuffer = new AudioBuffer();
		AudioStream audioStream = new AudioStream();
		audioStream.setIds(new int[]{2});
		Float p4 = Mockito.anyFloat();
		Float p5 = Mockito.anyFloat();
		LowPassFilter lowPassFilter = new LowPassFilter(p4, p5);
		
		VertexBuffer vertexBuffer2 = new VertexBuffer();
		Image image2 = new Image();
		FrameBuffer frameBuffer2 = new FrameBuffer(1,1,1);
		Shader shader2 = new Shader();
		ShaderSource shaderSource2 = new ShaderSource();
		AudioBuffer audioBuffer2 = new AudioBuffer();
		AudioStream audioStream2 = new AudioStream();
		audioStream.setIds(new int[]{2});
		LowPassFilter lowPassFilter2 = new LowPassFilter(p4, p5);
		
		vertexBuffer.setId(1);
		image.setId(1);
		frameBuffer.setId(1);
		shader.setId(1);
		shaderSource.setId(1);
		audioBuffer.setId(1);
		audioStream.setIds(new int[]{1});
		lowPassFilter.setId(1);
		
		vertexBuffer2.setId(2);
		image2.setId(2);
		frameBuffer2.setId(2);
		shader2.setId(2);
		shaderSource2.setId(2);
		audioBuffer2.setId(2);
		audioStream2.setIds(new int[]{2});
		lowPassFilter2.setId(2);
		
		String id1 = vertexBuffer.getUniqueId();
		String id2 = image.getUniqueId();
		String id3 = frameBuffer.getUniqueId();
		String id4 = shader.getUniqueId();
		String id5 = shaderSource.getUniqueId();
		String id6 = audioBuffer.getUniqueId();
		String id7 = audioStream.getUniqueId();
		String id8 = lowPassFilter.getUniqueId();
		
		String id9 = vertexBuffer2.getUniqueId();
		String id10 = image2.getUniqueId();
		String id11 = frameBuffer2.getUniqueId();
		String id12 = shader2.getUniqueId();
		String id13 = shaderSource2.getUniqueId();
		String id14 = audioBuffer2.getUniqueId();
		String id15 = audioStream2.getUniqueId();
		String id16 = lowPassFilter2.getUniqueId();
		
		Set<String> hashSet = new HashSet<String>();
		hashSet.add(id1);
		hashSet.add(id2);
		hashSet.add(id3);
		hashSet.add(id4);
		hashSet.add(id5);
		hashSet.add(id6);
		hashSet.add(id7);
		hashSet.add(id8);
		hashSet.add(id9);
		hashSet.add(id10);
		hashSet.add(id11);
		hashSet.add(id12);
		hashSet.add(id13);
		hashSet.add(id14);
		hashSet.add(id15);
		hashSet.add(id16);
		long size = hashSet.size();
		assertTrue(16 == size);
	}

}
