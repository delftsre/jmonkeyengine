/*
 * Copyright (c) 2009-2015 jMonkeyEngine
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
package com.jme3.texture;

import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.texture.Texture.Type;
import com.jme3.texture.Texture.WrapAxis;
import com.jme3.texture.Texture.WrapMode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.when;


/**
 * Verifies that {@link Texture3D} works correctly.
 *
 * @author Mart Oude Weernink
 */
@RunWith(MockitoJUnitRunner.class)
public class Texture3DTest extends Texture2DTest{

    //private Texture3D texture;
    //protected Texture3D texture_extended;


    @Before
    public void initiate(){
        texture = new Texture3D();
        texture_extended = new Texture3D(20,5,10, Image.Format.Depth24);
    }

    @Test
    public void testConstructorTexture(){
        //Image image = new Image();
        //image.setFormat(Image.Format.Depth);
        final Image image = Mockito.mock(Image.class);
        when(image.getFormat()).thenReturn(Image.Format.Depth);
        when(image.getData(0)).thenReturn(null);

        // Test Constructor with Image
        texture = new Texture3D(image);
        assert texture.getImage().equals(image);

        // Test Constructor with width, height and Format
        texture = new Texture3D(8, 2, 10, Image.Format.Depth24);
        assert texture.getImage().getWidth() == 8;
        assert texture.getImage().getHeight() == 2;
        assert texture.getImage().getDepth() == 10;
        assert texture.getImage().getFormat().equals(Image.Format.Depth24);

        // Test Constructor with width, height numSamples and Format
        //int width, int height, int depth, int numSamples, Image.Format format
        texture = new Texture3D(4, 10, 5, 15, Image.Format.Depth16);
        assert texture.getImage().getWidth() == 4;
        assert texture.getImage().getHeight() == 10;
        assert texture.getImage().getDepth() == 5;
        assert texture.getImage().getMultiSamples() == 15;
        assert texture.getImage().getFormat().equals(Image.Format.Depth16);
    }


    @Test
    public void testSetWrap(){
        // Invalid setWrap
        Exception exception = new Exception();
        try{
            texture.setWrap(null);
        } catch (Exception e) {
            exception = e;
        }
        assert exception instanceof IllegalArgumentException;

        // Invalid setWrap - WrapAxis null
        exception = new Exception();
        try{
            texture.setWrap(null, WrapMode.Repeat);
        } catch (Exception e){
            exception = e;
        }
        assert exception instanceof IllegalArgumentException;

        // Invalid setwrap - WrapMode null
        exception = new Exception();
        try{
            texture.setWrap(WrapAxis.T, null);
        } catch (Exception e){
            exception = e;
        }
        assert exception instanceof  IllegalArgumentException;

        texture.setWrap(WrapAxis.R, WrapMode.MirroredRepeat);
        assert texture.getWrap(WrapAxis.R).equals(WrapMode.MirroredRepeat);

        super.testSetWrap();
        assert texture.getWrap(WrapAxis.R).equals(WrapMode.EdgeClamp);

    }

    @Test
    public void testHash(){
        int old_hash = texture.hashCode();
        texture.setWrap(WrapAxis.R, WrapMode.Repeat);
        assert texture.hashCode() != old_hash;
        texture.setWrap(WrapAxis.R, WrapMode.EdgeClamp);
        assert texture.hashCode() == old_hash;
        super.testHash();
    }

    @Test
    public void testEquals(){
        super.testEquals();
        Texture clone = texture.clone();
        clone.setWrap(WrapMode.Repeat);
        assert clone.equals(texture) == false;

        clone = texture.clone();
        clone.setWrap(WrapAxis.R, WrapMode.MirroredRepeat);

        assert clone.equals(texture) == false;
    }

    @Test
    public void testType(){
        assert texture.getType().equals(Type.ThreeDimensional);
    }

    @Test
    public void readwriteTest() {
        Texture3D loaded_texture = new Texture3D();
        loaded_texture = (Texture3D) this.writeAndRead(loaded_texture);
        assert texture_extended.getImage().getWidth() == loaded_texture.getImage().getWidth();
        assert texture_extended.getImage().getHeight() == loaded_texture.getImage().getHeight();
        assert texture_extended.getImage().getFormat().equals(loaded_texture.getImage().getFormat());
        assert texture_extended.getImage().equals(loaded_texture.getImage());
        assert loaded_texture.getImage().getDepth() == texture_extended.getImage().getDepth();
    }
}
