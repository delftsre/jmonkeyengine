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

import com.jme3.texture.Texture.Type;
import com.jme3.texture.image.ColorSpace;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;


/**
 * Verifies that {@link TextureArray} works correctly.
 *
 * @author Mart Oude Weernink
 */
@RunWith(MockitoJUnitRunner.class)
public class TextureCubeMapTest extends Texture3DTest {

    @Before
    public void initiate(){
        texture = new TextureCubeMap();
        texture_extended = new TextureCubeMap(new Image(Image.Format.ABGR8, 20, 5, null, ColorSpace.Linear));
    }

    @Test
    public void testConstructorTexture(){
        final Image image = Mockito.mock(Image.class);
        when(image.getFormat()).thenReturn(Image.Format.Depth);
        when(image.getData(0)).thenReturn(null);

        // Test Constructor with Image
        texture = new TextureCubeMap(image);
        assert texture.getImage().equals(image);

        // Test Constructor with widht, height and format
        texture = new TextureCubeMap(20, 5, Image.Format.Depth32);
        assert texture.getImage().getWidth() == 20;
        assert texture.getImage().getHeight() == 5;
        assert texture.getImage().getFormat().equals(Image.Format.Depth32);

    }

    @Test
    public void testType(){
        assert texture.getType().equals(Type.CubeMap);
    }

    @Test
    public void readwriteTest() {
        TextureCubeMap loaded_texture = new TextureCubeMap();
        loaded_texture = (TextureCubeMap) this.writeAndRead(loaded_texture);
        assert texture_extended.getImage().getWidth() == loaded_texture.getImage().getWidth();
        assert texture_extended.getImage().getHeight() == loaded_texture.getImage().getHeight();
        assert texture_extended.getImage().getFormat().equals(loaded_texture.getImage().getFormat());
        assert texture_extended.getImage().equals(loaded_texture.getImage());
        assert loaded_texture.getImage().getDepth() == texture_extended.getImage().getDepth();
    }
}
