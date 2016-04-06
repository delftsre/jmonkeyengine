package com.jme3.texture.image;

import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;
import com.jme3.util.BufferUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

import static org.mockito.Mockito.*;

/**
 * Created by kristinfjola on 21/03/16.
 */
public class DefaultImageRasterTest {

    DefaultImageRaster ir;
    Image image;
    int x = 0;
    int y = 0;
    ColorRGBA color;

    @Before
    public void setUp() {
        image = createImage();
        createInputs();

        ir = spy(new DefaultImageRaster(image, 0 ,0, false));
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
        return image;
    }

    @Test
    public void testSetPixel() {
        ir.setPixel(x, y, color);

        verify(ir).rangeCheck(x, y);
        verify(ir).getSRGB(color);
        verify(ir).grayscaleCheck(color, ir.codec);
        verify(ir).setComponents(color, ir.codec, ir.components);
        verify(ir).writeComponents(x, y);
        verify(ir).setImageUpdateNeeded(ir.image);
    }

    @Test
    public void testGetPixel() {
        ColorRGBA pixelColor = ir.getPixel(x, y, color);

        verify(ir).rangeCheck(x, y);
        verify(ir).readComponents(x, y);
        verify(ir).setStoreComponents(color, ir.codec, ir.components);
        verify(ir).setStoreRGBA(color, ir.codec);
        verify(ir).setSRGB(color);
    }

    /**
     * Tests that the create methods creates an actual instance with the correct arguments
     * @throws Exception
     */
    @Test
    public void testCreate() throws Exception {
        DefaultImageRaster expectedRaster1 = DefaultImageRaster.create(image);
        DefaultImageRaster expectedRaster2 = DefaultImageRaster.create(image, 0, 0, true);
        DefaultImageRaster actualRaster = new DefaultImageRaster(image, 0, 0, true);
        DefaultImageRaster wrongRaster = new DefaultImageRaster(image, 1, 0, true);

        Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedRaster1, actualRaster));
        Assert.assertFalse(EqualsBuilder.reflectionEquals(expectedRaster1, wrongRaster));

        Assert.assertTrue(EqualsBuilder.reflectionEquals(expectedRaster2, actualRaster));
        Assert.assertFalse(EqualsBuilder.reflectionEquals(expectedRaster2, wrongRaster));
    }

    /**
     * Test that the create method throws an error when a slice should be specified
     */
    @Test(expected = IllegalStateException.class)
    public void testInvalidCreate() {
        ArrayList<ByteBuffer> byteBuffers = new ArrayList<ByteBuffer>(2);
        byteBuffers.add(BufferUtils.createByteBuffer(1));
        byteBuffers.add(BufferUtils.createByteBuffer(1));
        image.setData(byteBuffers);

        assert(image.getData().size() == 2);
        DefaultImageRaster actualRaster = DefaultImageRaster.create(image);
    }
}
