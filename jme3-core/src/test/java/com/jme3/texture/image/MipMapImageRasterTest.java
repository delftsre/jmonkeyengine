package com.jme3.texture.image;

import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

/**
 * Created by kristinfjola on 21/03/16.
 */
public class MipMapImageRasterTest {

    MipMapImageRaster ir;
    Image image;
    int x = 0;
    int y = 0;
    ColorRGBA color;

    @Before
    public void setUp() {
        image = createImage();
        createInputs();
        ir = spy(new MipMapImageRaster(image, 0));
        doNothing().when(ir).writeComponents(any(Integer.class), any(Integer.class));
        doNothing().when(ir).readComponents(any(Integer.class), any(Integer.class));
    }

    public void createInputs() {
        x = 0;
        y = 0;
        color = ColorRGBA.Black;
    }

    public Image createImage() {
        Image image = spy(new Image());
        when(image.getFormat()).thenReturn(Image.Format.BGR8);
        when(image.getWidth()).thenReturn(1);
        when(image.getHeight()).thenReturn(1);
        when(image.hasMipmaps()).thenReturn(true);
        when(image.getMipMapSizes()).thenReturn(new int[5]);
        return image;
    }

    @Test
    public void testSetPixel() {
        ir.setPixel(x, y, color);

        verify(ir).rangeCheck(x, y);
        verify(ir).grayscaleCheck(color,ir.codec);
        verify(ir).setComponents(color, ir.codec, ir.components);
        verify(ir).writeComponents(x, y);
        verify(ir).setImageUpdateNeeded(ir.image);
    }

    @Test
    public void testGetPixel() {
        ColorRGBA pixelColor = ir.getPixel(x, y, color);

        verify(ir).rangeCheck(x, y);
        verify(ir).readComponents(x, y);
        verify(ir).setStoreComponents(color, ir.codec ,ir.components);
        verify(ir).setStoreRGBA(color, ir.codec);
    }
}
