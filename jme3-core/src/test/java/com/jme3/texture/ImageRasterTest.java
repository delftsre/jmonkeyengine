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
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image.Format;
import com.jme3.texture.image.ColorSpace;
import com.jme3.texture.image.ImageRaster;
import com.jme3.texture.image.MipMapImageRaster;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Verifies that classes in package {@package texture.image} are working correctly.
 * 
 * @author Mart Oude Weernink
 * @author Remco Tjeerdsma
 */
public class ImageRasterTest {
    ImageRaster myImageRaster;

    @Before
    public void initiate(){
        ByteBuffer bb = ByteBuffer.allocate(10000).put ((byte)0xff );
        Image myImage = new Image(Format.RGB8,400,500, bb,ColorSpace.Linear);
        myImageRaster = ImageRaster.create(myImage);
    }

    @Test(expected=IllegalStateException.class)
    public void wrongCreationTest() {
        ByteBuffer bb = ByteBuffer.allocate(10000).put ((byte)0xff );
        bb.put((byte)0xee );
        bb.put((byte)0xaa );
        Image myImage = new Image(Format.RGB8,400,500, bb,ColorSpace.Linear);
        myImage.addData(bb);
        myImageRaster = ImageRaster.create(myImage);
    }
    @Test
    public void getterTest() {
        assertEquals(400,myImageRaster.getWidth());
        assertEquals(500,myImageRaster.getHeight());
    }

    @Test
    public void pixelTest() {
        assertEquals(new ColorRGBA(1,0,0,1),myImageRaster.getPixel(0,0));
        myImageRaster.setPixel(0,0, new ColorRGBA(1,1,1,0));
        assertEquals(new ColorRGBA(1,1,1,1),myImageRaster.getPixel(0,0));
        ColorRGBA result = new ColorRGBA();
        myImageRaster.getPixel(0,0,result);
        assertEquals(new ColorRGBA(1,1,1,1), result);
    }

    @Test(expected=IllegalArgumentException.class)
    public void mipmapRasterFailTest() {
        ByteBuffer bb = ByteBuffer.allocate(10000).put ((byte)0xff );
        Image myImage = new Image(Format.RGB8,400,500, bb,ColorSpace.Linear);
        myImageRaster = new MipMapImageRaster(myImage,0);
    }

    @Test
    public void mipmapRasterTest() {
        ByteBuffer bb = ByteBuffer.allocate(10000).put ((byte)0xff );
        Image myImage = new Image(Format.RGB8,400,500, bb,ColorSpace.Linear);
        myImage.setMipMapSizes(new int[]{1,5,7});
        myImageRaster = new MipMapImageRaster(myImage,0);
        ((MipMapImageRaster) myImageRaster).setSlice(1);
        ((MipMapImageRaster) myImageRaster).setMipLevel(1);
        boolean caughtException = false;
        try{
            myImageRaster.getPixel(0,0);
        } catch (NullPointerException e) {
            caughtException = true;
        }
        assertTrue(caughtException);
        ((MipMapImageRaster) myImageRaster).setSlice(0);
        ((MipMapImageRaster) myImageRaster).setMipLevel(0);
        pixelTest();
        getterTest();

    }


}
