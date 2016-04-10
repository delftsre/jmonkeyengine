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
import com.jme3.texture.Texture2D;
import com.jme3.texture.Texture.*;
import org.junit.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifies that {@link Texture2D} works correctly.
 *
 * @author Kirill Vainer
 */
@RunWith(MockitoJUnitRunner.class)
public class Texture2DTest {

    private Texture2D texture;


    @Before
    public void initiate(){
        texture = new Texture2D();


    }

    @Test
    public void testInitialisationOfTextureFromImage(){
        final Image image = Mockito.mock(Image.class);
        when(image.getData(0)).thenReturn(null);
        texture = new Texture2D(image);
        assert texture.getImage().equals(image);
    }

    @Test
    public void testCreateClone(){
        Texture clone = texture.createSimpleClone();
        assert texture.equals(clone);
    }

    @Test(expected = Exception.class)
    public void testSetWrapNull(){
        texture.setWrap(null);
    }

    @Test
    public void testSetWrap(){
        texture.setWrap(WrapAxis.S, WrapMode.Repeat);
        assert texture.getWrap(WrapAxis.S).equals(WrapMode.Repeat);

        texture.setWrap(WrapAxis.T, WrapMode.MirroredRepeat);
        assert texture.getWrap(WrapAxis.T).equals(WrapMode.MirroredRepeat);

        texture.setWrap(WrapMode.EdgeClamp);
        assert texture.getWrap(WrapAxis.S).equals(WrapMode.EdgeClamp);
        assert texture.getWrap(WrapAxis.T).equals(WrapMode.EdgeClamp);
    }

    @Test(expected = Exception.class)
    public void testInvalidGetWrap(){
        texture.getWrap(null);
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
        assert texture.equals(new Texture3D()) == false;
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


}
