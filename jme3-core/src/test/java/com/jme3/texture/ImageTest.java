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

import com.jme3.math.ColorRGBA;
import com.jme3.texture.image.*;
import org.junit.*;
import com.jme3.texture.Image.*;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Verifies that class {@link Image} in package {@link Texture} is working correctly.
 * 
 * @author Mart Oude Weernink
 * @author Remco Tjeerdsma
 */
public class ImageTest {
    Image myImage;

    @Before
    public void initiate() {
        myImage = new Image(Format.RGB8,300,300, null,ColorSpace.Linear);
    }

    @Test
    public void updateNeededTest() {
        assertEquals(myImage.isUpdateNeeded(), true);
        myImage.clearUpdateNeeded();
        assertEquals(myImage.isUpdateNeeded(), false);
        myImage.setUpdateNeeded();
        assertEquals(myImage.isUpdateNeeded(), true);


    }

    @Test
    public void widthHeightGetSetTest() {
        assertEquals(myImage.getWidth(), 300);
        myImage.setWidth(400);
        assertEquals(myImage.getWidth(), 400);
        myImage.setHeight(400);
        assertEquals(myImage.getHeight(), 400);
        assertEquals(myImage.isUpdateNeeded(), true);

    }

    @Test
    public void NPOTTest() {
        assertEquals(myImage.isNPOT(), true);
        myImage.setHeight(512);
        myImage.setWidth(512);
        assertEquals(myImage.isNPOT(), false);
    }

    @Test
    public void resetObjectTest(){
        myImage.getLastTextureState().rWrap = Texture.WrapMode.Repeat;
        assertEquals(myImage.getLastTextureState().rWrap,Texture.WrapMode.Repeat);
        myImage.resetObject();
        assertEquals(myImage.getLastTextureState().rWrap,null);
    }

    @Test
    public void setDataTest(){
        assertEquals(true,myImage.getData().isEmpty());
        ByteBuffer bb = ByteBuffer.allocate(10000).put ((byte)0xff );
        myImage.setData(bb);
        assertEquals(myImage.getData().get(0),bb);
    }
}
