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
import com.jme3.asset.*;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioKey;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.export.binary.BinaryImporter;
import com.jme3.font.BitmapFont;
import com.jme3.material.Material;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Caps;
import com.jme3.scene.Spatial;
import com.jme3.shader.Shader;
import com.jme3.shader.ShaderGenerator;
import com.jme3.shader.ShaderKey;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Texture.*;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;


/**
 * Verifies that {@link Texture2D} works correctly.
 *
 * @author Mart Oude Weernink
 */
@RunWith(MockitoJUnitRunner.class)
public class Texture2DTest {

    protected Texture2D texture;
    protected Texture2D texture_extended;


    @Before
    public void initiate(){
        texture = new Texture2D();
        texture_extended = new Texture2D(20,5,Image.Format.Alpha8);
    }

    @Test
    public void testConstructorTexture(){
        final Image image = Mockito.mock(Image.class);
        when(image.getData(0)).thenReturn(null);

        // Test Constructor with Image
        texture = new Texture2D(image);
        assert texture.getImage().equals(image);

        // Test Constructor with width, height and Format
        texture = new Texture2D(8, 2, Image.Format.ABGR8);
        assert texture.getImage().getWidth() == 8;
        assert texture.getImage().getHeight() == 2;
        assert texture.getImage().getFormat().equals(Image.Format.ABGR8);

        // Test Constructor with width, height numSamples and Format
        texture = new Texture2D(4, 10, 5, Image.Format.Alpha8);
        assert texture.getImage().getWidth() == 4;
        assert texture.getImage().getHeight() == 10;
        assert texture.getImage().getMultiSamples() == 5;
        assert texture.getImage().getFormat().equals(Image.Format.Alpha8);
    }

    @Test
    public void testCreateClone(){
        Texture clone = texture.createSimpleClone();
        assert texture.equals(clone);
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

        // Invalid setWrap - Use a non existing WrapAxis
        try{
            texture.setWrap(WrapAxis.R, WrapMode.Repeat);
        } catch (Exception e){
            exception = e;
        }
        assert exception instanceof IllegalArgumentException;

        // Invalid setWrap - WrapAxis null
        try{
            texture.setWrap(null, WrapMode.Repeat);
        } catch (Exception e){
            exception = e;
        }
        assert exception instanceof IllegalArgumentException;

        // Invalid setwrap - WrapMode null
        try{
            texture.setWrap(WrapAxis.T, null);
        } catch (Exception e){
            exception = e;
        }
        assert exception instanceof  IllegalArgumentException;

        // Invalid getWrap - Use a non existing WrapAxis
        try{
            texture.getWrap(WrapAxis.R);
        } catch (Exception e){
            exception = e;
        }
        assert exception instanceof IllegalArgumentException;

        texture.setWrap(WrapAxis.S, WrapMode.Repeat);
        assert texture.getWrap(WrapAxis.S).equals(WrapMode.Repeat);

        texture.setWrap(WrapAxis.T, WrapMode.MirroredRepeat);
        assert texture.getWrap(WrapAxis.T).equals(WrapMode.MirroredRepeat);

        texture.setWrap(WrapMode.EdgeClamp);
        assert texture.getWrap(WrapAxis.S).equals(WrapMode.EdgeClamp);
        assert texture.getWrap(WrapAxis.T).equals(WrapMode.EdgeClamp);
    }

    @Test
    public void testHash(){
        int old_hash = texture.hashCode();
        texture.setWrap(WrapMode.Repeat);
        assert texture.hashCode() != old_hash;
        texture.setWrap(WrapMode.EdgeClamp);
        assert texture.hashCode() == old_hash;

        texture.setWrap(WrapAxis.S, WrapMode.Repeat);
        assert texture.hashCode() != old_hash;

        texture.setWrap(WrapMode.EdgeClamp);
        assert texture.hashCode() == old_hash;

        texture.setWrap(WrapAxis.T, WrapMode.MirroredRepeat);
        assert texture.hashCode() != old_hash;

    }

    @Test
    public void testEquals(){
        Texture clone = texture.clone();
        assert clone.equals(texture) == true;
        assert texture.equals(new String()) == false;
        clone.setWrap(WrapMode.Repeat);
        assert clone.equals(texture) == false;

        clone = texture.clone();
        clone.setWrap(WrapAxis.T, WrapMode.MirroredRepeat);

        assert clone.equals(texture) == false;
    }



    @Test
    public void testType(){
        assert texture.getType().equals(Texture.Type.TwoDimensional);
    }

    @Test
    public void readwriteTest() {
        Texture2D loaded_texture = new Texture2D();
        this.writeAndRead(loaded_texture);
    }

    public Texture2D writeAndRead(Texture2D loaded_texture){
        Boolean exception = false;
        String userHome = System.getProperty("user.home");
        BinaryExporter exporter = BinaryExporter.getInstance();
        File file = new File(userHome + "/Models/" + "MyModel.j3o");
        try {
            exporter.save(texture_extended, file);
        } catch (IOException e) {
            System.out.println(e);
            exception = true;
        }
        BinaryImporter importer = BinaryImporter.getInstance();
        try{
            loaded_texture = (Texture2D) importer.load(file);
        } catch(IOException e){
            System.out.println(e);
            exception = true;
        }
        assert texture_extended.getImage().getWidth() == loaded_texture.getImage().getWidth();
        assert texture_extended.getImage().getHeight() == loaded_texture.getImage().getHeight();
        assert texture_extended.getImage().getFormat().equals(loaded_texture.getImage().getFormat());
        assert texture_extended.getImage().equals(loaded_texture.getImage());
        return loaded_texture;
    }
}
