package com.jme3.texture;

import org.junit.Before;
import org.junit.Test;


public class TextureArrayTest {

    private TextureArray tex;

    @Before
    public void setUp(){
        tex = new TextureArray();
    }

    /**
     Test setWrap(WrapAxis, WrapMode)
     **/

    @Test(expected = IllegalArgumentException.class)
    public void testSetWrapAxisR() {
        Texture.WrapMode wrapMode = Texture.WrapMode.Repeat;
        Texture.WrapAxis wrapAxis = Texture.WrapAxis.R;
        tex.setWrap(wrapAxis, wrapMode);
    }

    /**
     Test getWrap(WrapAxis)
     **/

    @Test(expected = IllegalArgumentException.class)
    public void testGetWrapSetAllAxisR() {
        Texture.WrapMode wrapMode = Texture.WrapMode.MirroredRepeat;
        tex.setWrap(wrapMode);
        tex.getWrap(Texture.WrapAxis.R);
    }
}
