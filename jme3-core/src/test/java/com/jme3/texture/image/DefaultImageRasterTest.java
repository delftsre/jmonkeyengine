package com.jme3.texture.image;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.jme3.math.ColorRGBA;
import com.jme3.texture.Image;

@RunWith(MockitoJUnitRunner.class)
public class DefaultImageRasterTest extends CommonImageRasterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testDefaultSetSlice(){
        Image someImage = getMockImage();
        DefaultImageRaster testIR = new DefaultImageRaster(someImage, 0, 0, false);
        verify(someImage, times(1)).getData(0);
        testIR.setSlice(1);
        verify(someImage, times(1)).getData(1);
    }
    @Test
    public void testDefaultRangeCheckSucc(){
        Image someImage = getMockImage();
        ImageRaster testIR = ImageRasterFactory.create(someImage);
        testIR.getPixel(3,3);
    }
    @Test
    public void testDefaultRangeCheckFail(){
        Image someImage = getMockImage();
        ImageRaster testIR = ImageRasterFactory.create(someImage);
        thrown.expect(IllegalArgumentException.class);
        testIR.getPixel(3,4);
    }
    @Test
    public void testDefaultRangeCheckMip0Succ(){
        Image someImage = getMipMockImage();
        ImageRaster testIR = ImageRasterFactory.create(someImage, 0, 0, false);
        testIR.getPixel(3,3);
    }
    @Test
    public void testDefaultRangeCheckMip0Fail(){
        Image someImage = getMipMockImage();
        ImageRaster testIR = ImageRasterFactory.create(someImage, 0, 0, false);
        thrown.expect(IllegalArgumentException.class);
        testIR.getPixel(3,4);
    }
    @Test
    public void testDefaultRangeCheckMip1Succ(){
        Image someImage = getMipMockImage();
        ImageRaster testIR = ImageRasterFactory.create(someImage, 0, 1, false);
        testIR.getPixel(1,1);
    }
    @Test
    public void testDefaultRangeCheckMip1Fail(){
        Image someImage = getMipMockImage();
        ImageRaster testIR = ImageRasterFactory.create(someImage, 0, 1, false);
        thrown.expect(IllegalArgumentException.class);
        testIR.getPixel(1,2);
    }
    @Test
    public void testDefaultConstructNoMipSet(){
        Image someImage = getMockImage();
        thrown.expect(IllegalStateException.class);
        ImageRasterFactory.create(someImage, 0, 1, false);
    }
    @Test
    public void testDefaultConstructMipBelowLimit(){
        Image someImage = getMipMockImage();
        ImageRasterFactory.create(someImage, 0, 3, false);
    }
    @Test
    public void testDefaultConstructMipTooHigh(){
        Image someImage = getMipMockImage();
        thrown.expect(IllegalStateException.class);
        ImageRasterFactory.create(someImage, 0, 4, false);
    }
    @Test
    public void testDefaultGetPixelNoConvert(){
        Image someImage = getMockImage();
        ImageRaster testIR = ImageRasterFactory.create(someImage, 0, 0, false);
        someImage.getData(0).put(0, (byte) -128);
        ColorRGBA color = testIR.getPixel(0, 0);
        assert Math.abs(color.r - 0.5f) < .05f;
    }
    @Test
    public void testDefaultGetPixelDoConvert(){
        Image someImage = getMockImage();
        ImageRaster testIR = ImageRasterFactory.create(someImage, 0, 0, true);
        someImage.getData(0).put(0, (byte) -128);
        ColorRGBA color = testIR.getPixel(0, 0);
        assert Math.abs(color.r - 0.5f) > .2f;
    }
    @Test
    public void testDefaultSetPixelNoConvert(){
        Image someImage = getMockImage();
        ImageRaster testIR = ImageRasterFactory.create(someImage, 0, 0, false);
        ColorRGBA color = ColorRGBA.Gray;
        testIR.setPixel(0, 0, color);
        assert someImage.getData(0).get(0) == -128;
    }
    @Test
    public void testDefaultSetPixelDoConvert(){
        Image someImage = getMockImage();
        ImageRaster testIR = ImageRasterFactory.create(someImage, 0, 0, true);
        ColorRGBA color = ColorRGBA.Gray;
        testIR.setPixel(0, 0, color);
        assert someImage.getData(0).get(0) != -128;
    }
}
