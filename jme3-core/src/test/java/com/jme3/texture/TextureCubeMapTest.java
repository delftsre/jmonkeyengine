package com.jme3.texture;


import com.jme3.export.JmeExporter;
import com.jme3.export.OutputCapsule;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TextureCubeMapTest {

    private TextureCubeMap tex;

    @Before
    public void setUp(){
        tex = new TextureCubeMap();
    }

    /**
     Test setWrap(WrapAxis, WrapMode)
     **/

    @Test
    public void testSetWrapAxisR() {
        Texture.WrapMode wrapMode = Texture.WrapMode.Repeat;
        Texture.WrapAxis wrapAxis = Texture.WrapAxis.R;
        tex.setWrap(wrapAxis, wrapMode);
        assert tex.getWrap(wrapAxis) == wrapMode;
    }

    /**
     Test setWrap(WrapMode)
     **/

    @Test
    public void testSetWrapOneParam() {
        Texture.WrapMode wrapMode = Texture.WrapMode.MirroredRepeat;
        tex.setWrap(wrapMode);
        assert tex.getWrap(Texture.WrapAxis.R) == wrapMode;
    }

    /**
     Test getWrap(WrapAxis)
     **/

    @Test
    public void testGetWrapSetAllAxisR() {
        Texture.WrapMode wrapMode = Texture.WrapMode.MirroredRepeat;
        tex.setWrap(wrapMode);
        assert tex.getWrap(Texture.WrapAxis.R) == wrapMode;
    }

    @Test
    public void testGetWrapSetOneAxisR() {
        Texture.WrapMode wrapMode = Texture.WrapMode.MirroredRepeat;
        Texture.WrapAxis wrapAxis = Texture.WrapAxis.R;
        tex.setWrap(wrapAxis, wrapMode);
        assert tex.getWrap(wrapAxis) == wrapMode;
    }

    /**
     Test equals(Object)
     **/

    @Test
    public void testEqualsDiffAxises() {
        TextureCubeMap tex1 = new TextureCubeMap();
        TextureCubeMap tex2 = new TextureCubeMap();

        tex1.setWrap(Texture.WrapMode.EdgeClamp);
        tex2.setWrap(Texture.WrapMode.EdgeClamp);

        assert tex1.equals(tex2) == true;
        assert tex2.equals(tex1) == true;
    }

    @Test
    public void testEqualsSameAxises() {
        TextureCubeMap tex1 = new TextureCubeMap();
        TextureCubeMap tex2 = new TextureCubeMap();

        tex1.setWrap(Texture.WrapAxis.T, Texture.WrapMode.Repeat);
        tex2.setWrap(Texture.WrapAxis.T, Texture.WrapMode.EdgeClamp);

        assert tex1.equals(tex2) == false;
        assert tex2.equals(tex1) == false;
    }

    @Test
    public void testWrite() throws IOException {
        JmeExporter e = spy(new TestExporter());
        OutputCapsule o = spy(new TestOutputCapsule());
        when(e.getCapsule(any(Texture.class))).thenReturn(o);

        tex.write(e);
        verify(o, times(1)).write(eq(Texture.WrapMode.EdgeClamp), eq("wrapR"), eq(Texture.WrapMode.EdgeClamp));
    }
}
