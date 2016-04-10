/*
 * Copyright (c) 2009-2016 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.scene.plugins.blender.textures;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.plugins.blender.textures.UVCoordinatesGenerator.UVCoordinatesType;
import com.jme3.scene.plugins.blender.textures.UVProjectionGenerator.UVProjectionType;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class UVCoordinatesGeneratorTest {
    
	@Mock
    private Mesh mockedMesh; 
	@Mock
    private Geometry mockedGeometry;
	
    private BoundingBox bb;
    private UVCoordinatesType texco;
    private UVProjectionType projection;
    private int[] indices;
    
    @Before
    public void setup() {
        mockedMesh = mock(Mesh.class);
        projection = UVProjectionType.PROJECTION_CUBE;
        bb = new BoundingBox(new Vector3f(1f, 2f ,3f), 1f, 2f, 3f);
            
        mockedGeometry = mock(Geometry.class);
        doNothing().when(mockedGeometry).updateModelBound();
        when(mockedGeometry.getModelBound()).thenReturn(bb); 
        
        indices = new int[0];
    }
    
    @Test
    public void testUnsupportedTextureType2D() {    
        List<Vector2f> result = null;
        texco = UVCoordinatesType.TEXCO_LAVECTOR;       
            
        result = UVCoordinatesGenerator.generateUVCoordinatesFor2DTexture(mockedMesh, texco, projection, mockedGeometry);
        assertEquals(new ArrayList<Vector2f>(), result);
    }
    
    @Test
    public void testORCOTextureType2D() {
        List<Vector2f> result = null;
        UVCoordinatesType texco = UVCoordinatesType.TEXCO_ORCO;                                                                                                                                  
        
        initFloatBuffer();
        
        result = UVCoordinatesGenerator.generateUVCoordinatesFor2DTexture(mockedMesh, texco, projection, mockedGeometry);
        
        ArrayList<Vector2f> expected = new ArrayList<Vector2f>();
        expected.add(new Vector2f(0.5f, 0.5f));
        expected.add(new Vector2f(2.0f, 1.25f));
        expected.add(new Vector2f(3.5f, 2.0f));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testORCOTextureTypeFlatProjectionType2D() {
        List<Vector2f> result = null;
        UVCoordinatesType texco = UVCoordinatesType.TEXCO_ORCO; 
        projection = UVProjectionType.PROJECTION_FLAT;
        
        initFloatBuffer();     
        
        result = UVCoordinatesGenerator.generateUVCoordinatesFor2DTexture(mockedMesh, texco, projection, mockedGeometry);
        
        ArrayList<Vector2f> expected = new ArrayList<Vector2f>();
        expected.add(new Vector2f(0.5f, 0.5f));
        expected.add(new Vector2f(2.0f, 1.0f));
        expected.add(new Vector2f(3.5f, 1.5f));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testORCOTextureTypeTubeProjectionType2D() {  	
        List<Vector2f> result = null;
        UVCoordinatesType texco = UVCoordinatesType.TEXCO_ORCO; 
        projection = UVProjectionType.PROJECTION_TUBE;    
        initFloatBuffer();     
        
        result = UVCoordinatesGenerator.generateUVCoordinatesFor2DTexture(mockedMesh, texco, projection, mockedGeometry);
        
        ArrayList<Vector2f> expected = new ArrayList<Vector2f>();
        expected.add(new Vector2f(0.25f, 0.5f));
        expected.add(new Vector2f(0.375f, 1.0f));
        expected.add(new Vector2f(0.375f, 1.5f));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testORCOTextureTypeSphereProjectionType2D() {
        List<Vector2f> result = null;
        UVCoordinatesType texco = UVCoordinatesType.TEXCO_ORCO; 
        projection = UVProjectionType.PROJECTION_SPHERE;
        
        initFloatBuffer();     
        
        result = UVCoordinatesGenerator.generateUVCoordinatesFor2DTexture(mockedMesh, texco, projection, mockedGeometry);
        
        ArrayList<Vector2f> expected = new ArrayList<Vector2f>();
        expected.add(new Vector2f(0.25f, 0.5f));
        expected.add(new Vector2f(0.375f, 0.69591326f));
        expected.add(new Vector2f(0.375f, 0.69591326f));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testUVTextureType2D() {
        List<Vector2f> result = null;
        UVCoordinatesType texco = UVCoordinatesType.TEXCO_UV;
        
        when(mockedMesh.getVertexCount()).thenReturn(2);
    
        result = UVCoordinatesGenerator.generateUVCoordinatesFor2DTexture(mockedMesh, texco, projection, mockedGeometry);
        ArrayList<Vector2f> expected = new ArrayList<Vector2f>();
        expected.add(new Vector2f(0f, 1f));
        expected.add(new Vector2f(0f, 0f));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testNORMTextureType2D() {
        List<Vector2f> result = null;
        UVCoordinatesType texco = UVCoordinatesType.TEXCO_NORM;
        
        result = UVCoordinatesGenerator.generateUVCoordinatesFor2DTexture(mockedMesh, texco, projection, mockedGeometry);
        assertEquals(new ArrayList<Vector2f>(), result);
    }
    
    @Test
    public void testUnsupportedTextureType3D() {
        List<Vector3f> result = null;
        UVCoordinatesType texco = UVCoordinatesType.TEXCO_SPEED;
        
        result = UVCoordinatesGenerator.generateUVCoordinatesFor3DTexture(mockedMesh, texco, indices, mockedGeometry);
        assertEquals(new ArrayList<Vector2f>(), result);
    }
    
    @Test
    public void testORCOTextureType3D() {
        List<Vector3f> result = null;
        UVCoordinatesType texco = UVCoordinatesType.TEXCO_ORCO;    
        indices = new int[3];
        indices[0] = 1;
        indices[1] = 2;
        indices[2] = 0;
        
        FloatBuffer buffer = FloatBuffer.allocate(9);
        float[] floatsForBuffer = {1f,2f,3f,4f,5f,6f,7f,8f,9f};
        buffer.put(floatsForBuffer);
        when(mockedMesh.getFloatBuffer(any(VertexBuffer.Type.class))).thenReturn(buffer);
        
        result = UVCoordinatesGenerator.generateUVCoordinatesFor3DTexture(mockedMesh, texco, indices, mockedGeometry);
        
        ArrayList<Vector3f> expected = new ArrayList<Vector3f>();
        expected.add(new Vector3f(0.5f, 0.5f, 0f));
        expected.add(new Vector3f(2.0f, 1.25f, 0f));
        expected.add(new Vector3f(3.5f, 2.0f, 0f));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testUVTextureType3D() {
        List<Vector3f> result = null;
        UVCoordinatesType texco = UVCoordinatesType.TEXCO_UV;
        
        when(mockedMesh.getVertexCount()).thenReturn(2);
        
        result = UVCoordinatesGenerator.generateUVCoordinatesFor3DTexture(mockedMesh, texco, indices, mockedGeometry);
        
        ArrayList<Vector3f> expected = new ArrayList<Vector3f>();
        expected.add(new Vector3f(0f, 1f, 0f));
        expected.add(new Vector3f(0f, 0f, 0f));
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testNORMTextureType3D() {
        List<Vector3f> result = null;
        UVCoordinatesType texco = UVCoordinatesType.TEXCO_NORM;
        
        result = UVCoordinatesGenerator.generateUVCoordinatesFor3DTexture(mockedMesh, texco, indices, mockedGeometry);
        assertEquals(new ArrayList<Vector2f>(), result);
    }
    
    /**
     * Helper function to create and prepare a FloatBuffer for the tests
     */
    public void initFloatBuffer() {
    	FloatBuffer buffer = FloatBuffer.allocate(9);
        float[] floatsForBuffer = {1f,2f,3f,4f,5f,6f,7f,8f,9f};
        buffer.put(floatsForBuffer);
        when(mockedMesh.getFloatBuffer(any(VertexBuffer.Type.class))).thenReturn(buffer);
    }
}
