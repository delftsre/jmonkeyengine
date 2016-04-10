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

import com.jme3.export.JmeExporter;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.export.xml.XMLExporter;
import com.jme3.texture.image.*;
import org.junit.*;
import com.jme3.texture.Image.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

/**
 * Verifies that class {@link Image} in package {@package texture} is working correctly.
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
    public void addSetGetDataTest(){
        assertEquals(true,myImage.getData().isEmpty());
        ByteBuffer bb = ByteBuffer.allocate(10000).put ((byte)0xff );
        myImage.setData(bb);
        assertEquals(myImage.getData(0),bb);
        assertEquals(myImage.getData(1),null);
        myImage.addData(bb);
        ArrayList<ByteBuffer> expected = new ArrayList<ByteBuffer>(2);
        expected.add(bb);
        expected.add(bb);
        assertEquals(myImage.getData(),expected);

    }

    @Test
    public void depthTest(){
        assertEquals(myImage.getDepth(),0);
        myImage.setDepth(2);
        assertEquals(myImage.getDepth(),2);
    }

    @Test
    public void formatTest(){
        assertEquals(myImage.getFormat(),Format.RGB8);
        myImage.setFormat(Format.RGBA32F);
        assertEquals(myImage.getFormat(),Format.RGBA32F);
    }

    @Test(expected=NullPointerException.class)
    public void formatExceptionTest() {
        myImage.setFormat(null);
    }

    @Test
    public void mipMapTest(){
        assertEquals(myImage.hasMipmaps(),false);
        assertEquals(myImage.getMipMapSizes(),null);
        int[] mipmapsizes = new int[]{1,4,6};
        myImage.setMipMapSizes(mipmapsizes);
        assertEquals(myImage.getMipMapSizes(),mipmapsizes);
        assertEquals(myImage.isMipmapsGenerated(),false);
        myImage.setMipmapsGenerated(true);
        assertEquals(myImage.isMipmapsGenerated(),true);

        assertEquals(myImage.isGeneratedMipmapsRequired(),false);
        myImage.setNeedGeneratedMipmaps();
        assertEquals(myImage.isGeneratedMipmapsRequired(),true);
    }

    @Test
    public void colorSpaceTest(){
        assertEquals(myImage.getColorSpace(),ColorSpace.Linear);
        myImage.setColorSpace(ColorSpace.sRGB);
        assertEquals(myImage.getColorSpace(),ColorSpace.sRGB);
    }

    @Test
    public void toStringTest(){
        assertEquals("Image[size=300x300, format=RGB8]", myImage.toString());
        int[] mipmapsizes = new int[]{1,4,6};
        myImage.setMipMapSizes(mipmapsizes);
        assertEquals("Image[size=300x300, format=RGB8, mips]", myImage.toString());
        myImage.setId(44);
        assertEquals("Image[size=300x300, format=RGB8, mips, id=44]", myImage.toString());
    }

    @Test
    public void imageIDTest(){
        Image imageID = new Image(32);
        assertEquals(32,imageID.getId());
        assertEquals(8589934624L, imageID.getUniqueId());
    }

    @Test
    public void equalsTest(){
        assertEquals(false, myImage.equals("Test"));
        assertEquals(true, myImage.equals(myImage));
        Image myImagecopy = myImage.clone();
        assertEquals(true, myImage.equals(myImagecopy));
        myImagecopy.setWidth(333);
        assertEquals(false, myImage.equals(myImagecopy));
        myImagecopy.setWidth(myImage.getWidth());
        assertEquals(true, myImage.equals(myImagecopy));
    }

    @Test
    public void hashCodeTest(){
        int startHash = myImage.hashCode();
        assertEquals(startHash, myImage.hashCode());
        Image myImagecopy = myImage.clone();
        assertEquals(startHash, myImagecopy.hashCode());
        myImagecopy.setWidth(333);
        assertNotEquals(startHash, myImagecopy.hashCode());
        myImagecopy.setWidth(myImage.getWidth());
        assertEquals(startHash, myImagecopy.hashCode());
    }

    @Test(expected=IllegalArgumentException.class)
    public void multiSamplesTest1(){
        myImage.setMultiSamples(0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void multiSamplesTest2(){
        ByteBuffer bb = ByteBuffer.allocate(10000).put ((byte)0xff );
        myImage.setData(0, bb);
        myImage.setMultiSamples(2);
    }

    @Test(expected=IllegalArgumentException.class)
    public void multiSamplesTest3(){
        myImage.setMipMapSizes(new int[]{1,6,8});
        myImage.setMultiSamples(3);
    }

    @Test
    public void multiSamplesTest4(){
        myImage.setMultiSamples(4);
    }

    @Test
    public void readwriteTest(){
        boolean exception = false;
        XMLExporter be = new XMLExporter();
        try {
            myImage.write(be);
        } catch (IOException e) {
            exception = true;
        }
        assertFalse("Export",exception);

        BinaryImporter bi = new BinaryImporter();
        Image newImage = new Image();
        try {
            newImage.read(bi);
        } catch (IOException e) {
            exception = true;
        }
        assertFalse("Import",exception);
        assertEquals(myImage, newImage);
    }

    @Test
    public void imageFormatEnumTest() {
        Format testformat = Format.Alpha8;
        assertEquals(testformat.getBitsPerPixel(), 8);
        assertEquals(testformat.isDepthFormat(), false);
        assertEquals(testformat.isDepthStencilFormat(), false);
        assertEquals(testformat.isFloatingPont(), false);
        assertEquals(testformat.isCompressed(), false);
        testformat = Format.Depth24Stencil8;
        assertEquals(testformat.getBitsPerPixel(), 32);
        assertEquals(testformat.isDepthFormat(), true);
        assertEquals(testformat.isDepthStencilFormat(), true);
        assertEquals(testformat.isFloatingPont(), false);
        assertEquals(testformat.isCompressed(), false);
        testformat = Format.RGBA32F;
        assertEquals(testformat.getBitsPerPixel(), 128);
        assertEquals(testformat.isDepthFormat(), false);
        assertEquals(testformat.isDepthStencilFormat(), false);
        assertEquals(testformat.isFloatingPont(), true);
        assertEquals(testformat.isCompressed(), false);
        testformat = Format.DXT1;
        assertEquals(testformat.getBitsPerPixel(), 4);
        assertEquals(testformat.isDepthFormat(), false);
        assertEquals(testformat.isDepthStencilFormat(), false);
        assertEquals(testformat.isFloatingPont(), false);
        assertEquals(testformat.isCompressed(), true);

    }

}
