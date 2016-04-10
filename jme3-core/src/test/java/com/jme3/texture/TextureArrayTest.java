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
import com.jme3.texture.image.ColorSpace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Verifies that {@link TextureArray} works correctly.
 *
 * @author Mart Oude Weernink
 */
@RunWith(MockitoJUnitRunner.class)
public class TextureArrayTest extends Texture2DTest {

    @Before
    public void initiate(){
        texture = new TextureArray();
        List<Image> list = new ArrayList<Image>();
        list.add(new Image(Image.Format.ABGR8, 20, 5, null, ColorSpace.Linear));
        list.add(new Image(Image.Format.ABGR8, 20, 5, null, ColorSpace.Linear));
        texture_extended = new TextureArray(list);
    }

    @Test
    public void testConstructorTexture(){
        Image image = Mockito.mock(Image.class);
        when(image.getData(0)).thenReturn(null);
        when(image.getHeight()).thenReturn(20);
        when(image.getWidth()).thenReturn(10);
        when(image.getFormat()).thenReturn(Image.Format.Depth16);
        when(image.getColorSpace()).thenReturn(ColorSpace.Linear);
        when(image.getMipMapSizes()).thenReturn(new int[]{1,4,6});

        List<Image> images = new ArrayList<Image>();
        images.add(image);
        images.add(image);

        // Test Constructor
        texture = new TextureArray(images);
        verify(image, times(3)).getHeight();
        verify(image, times(3)).getWidth();
        verify(image, times(3)).getFormat();
        verify(image).getColorSpace();
        verify(image, times(3)).getMipMapSizes();
    }

    @Test
    public void testType(){
        assert texture.getType().equals(Type.TwoDimensionalArray);
    }

    @Test
    public void readwriteTest() {
        TextureArray loaded_texture = new TextureArray();
        loaded_texture = (TextureArray) this.writeAndRead(loaded_texture);
        assert texture_extended.getImage().getWidth() == loaded_texture.getImage().getWidth();
        assert texture_extended.getImage().getHeight() == loaded_texture.getImage().getHeight();
        assert texture_extended.getImage().getFormat().equals(loaded_texture.getImage().getFormat());
        assert texture_extended.getImage().equals(loaded_texture.getImage());
    }
}
