package com.jme3.texture;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.verify;

public class TextureDefault2DTest {

    private TextureDefault2D tex;

    @Before
    public void setUp(){
        tex = new TextureDefault2D();
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

    /**
     Test equals(Object)
     **/

    @Test
    public void testEqualsSameAxises() {
        TextureDefault2D tex1 = new TextureDefault2D();
        TextureDefault2D tex2 = new TextureDefault2D();

        tex1.setWrap(Texture.WrapMode.EdgeClamp);
        tex2.setWrap(Texture.WrapMode.EdgeClamp);

        assert tex1.equals(tex2) == true;
        assert tex2.equals(tex1) == true;
    }

    @Test
    public void testEqualsDiffAxises() {
        TextureDefault2D tex1 = new TextureDefault2D();
        TextureDefault2D tex2 = new TextureDefault2D();

        tex1.setWrap(Texture.WrapAxis.S, Texture.WrapMode.EdgeClamp);
        tex2.setWrap(Texture.WrapAxis.S, Texture.WrapMode.MirroredRepeat);

        assert tex1.equals(tex2) == false;
        assert tex2.equals(tex1) == false;
    }
}
