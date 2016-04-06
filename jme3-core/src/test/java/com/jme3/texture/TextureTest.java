package com.jme3.texture;

import com.jme3.asset.TextureKey;
import com.jme3.export.JmeExporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TextureTest {
    private Texture tex;

    @Before
    public void setUp(){
        tex = new TextureDefault2D();
    }

    /**
    Test setWrap(WrapAxis, WrapMode)
     **/

    @Test(expected = IllegalArgumentException.class)
    public void testSetWrapWithAxisNull(){
        tex.setWrap(null, Texture.WrapMode.EdgeClamp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWrapModeNull() {
        tex.setWrap(Texture.WrapAxis.R, null);
    }

    @Test
    public void testSetWrapAxisS() {
        Texture.WrapMode wrapMode = Texture.WrapMode.EdgeClamp;
        Texture.WrapAxis wrapAxis = Texture.WrapAxis.S;
        tex.setWrap(wrapAxis, wrapMode);
        assert tex.getWrap(wrapAxis) == wrapMode;
    }

    @Test
    public void testSetWrapAxisT() {
        Texture.WrapMode wrapMode = Texture.WrapMode.MirroredRepeat;
        Texture.WrapAxis wrapAxis = Texture.WrapAxis.T;
        tex.setWrap(wrapAxis, wrapMode);
        assert tex.getWrap(wrapAxis) == wrapMode;
    }

    /**
     Test setWrap(WrapMode)
     **/

    @Test(expected = IllegalArgumentException.class)
    public void testSetWrapOneParamModeNull() {
        tex.setWrap(null);
    }

    @Test
    public void testSetWrapOneParam() {
        Texture.WrapMode wrapMode = Texture.WrapMode.MirroredRepeat;
        tex.setWrap(wrapMode);
        assert tex.getWrap(Texture.WrapAxis.S) == wrapMode;
        assert tex.getWrap(Texture.WrapAxis.T) == wrapMode;
    }

    /**
     Test getWrap(WrapAxis)
     **/

    @Test(expected = IllegalArgumentException.class)
    public void testGetWrapSetAllIllegal() {
        Texture.WrapMode wrapMode = Texture.WrapMode.MirroredRepeat;
        tex.setWrap(wrapMode);
        tex.getWrap(null);
    }

    @Test
    public void testGetWrapSetAllAxisS() {
        Texture.WrapMode wrapMode = Texture.WrapMode.MirroredRepeat;
        tex.setWrap(wrapMode);
        assert tex.getWrap(Texture.WrapAxis.S) == wrapMode;
    }

    @Test
    public void testGetWrapSetOneAxisT() {
        Texture.WrapMode wrapMode = Texture.WrapMode.MirroredRepeat;
        Texture.WrapAxis wrapAxis = Texture.WrapAxis.T;
        tex.setWrap(wrapAxis, wrapMode);
        assert tex.getWrap(wrapAxis) == wrapMode;
    }
    @Test
    public void testWriteNoKey() throws IOException {
        JmeExporter e = spy(new TestExporter());
        OutputCapsule o = spy(new TestOutputCapsule());
        when(e.getCapsule(any(Texture.class))).thenReturn(o);

        tex.write(e);

        verify(o, times(1)).write(isNull(String.class), eq("name"), isNull(String.class));
        verify(o, times(1)).write(any(Savable.class), eq("image"), isNull(Savable.class));
        verify(o, times(1)).write(anyInt(), eq("anisotropicFilter"), eq(1));
        verify(o, times(1)).write(any(Enum.class), eq("minificationFilter"), eq(Texture.MinFilter.BilinearNoMipMaps));
        verify(o, times(1)).write(any(Enum.class), eq("magnificationFilter"), eq(Texture.MagFilter.Bilinear));
    }
    @Test
    public void testWriteWithKey() throws IOException {
        JmeExporter e = spy(new TestExporter());
        OutputCapsule o = spy(new TestOutputCapsule());
        when(e.getCapsule(any(Texture.class))).thenReturn(o);

        tex.setKey(new TextureKey());
        tex.write(e);

        verify(o, times(1)).write(isNull(String.class), eq("name"), isNull(String.class));
        verify(o, times(1)).write(any(Savable.class), eq("key"), isNull(Savable.class));
        verify(o, times(1)).write(anyInt(), eq("anisotropicFilter"), eq(1));
        verify(o, times(1)).write(any(Enum.class), eq("minificationFilter"), eq(Texture.MinFilter.BilinearNoMipMaps));
        verify(o, times(1)).write(any(Enum.class), eq("magnificationFilter"), eq(Texture.MagFilter.Bilinear));

        verify(o, times(1)).write(eq(Texture.WrapMode.EdgeClamp), eq("wrapS"), eq(Texture.WrapMode.EdgeClamp));
        verify(o, times(1)).write(eq(Texture.WrapMode.EdgeClamp), eq("wrapT"), eq(Texture.WrapMode.EdgeClamp));
    }
}


