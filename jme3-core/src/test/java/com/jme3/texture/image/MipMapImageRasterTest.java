package com.jme3.texture.image;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.jme3.texture.Image;

@RunWith(MockitoJUnitRunner.class)
public class MipMapImageRasterTest extends CommonImageRasterTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testMipMapSetSlice(){
        Image someImage = getMipMockImage();
        MipMapImageRaster MIR = new MipMapImageRaster(someImage, 0);
        verify(someImage, times(1)).getData(0);
        MIR.setSlice(1);
        verify(someImage, times(1)).getData(1);
    }
    @Test
    public void testMipMapRangeCheckSucc(){
        Image someImage = getMipMockImage();
        MipMapImageRaster MIR = new MipMapImageRaster(someImage, 0);
        MIR.getPixel(3,3);
    }
    @Test
    public void testMipMapRangeCheckFail(){
        Image someImage = getMipMockImage();
        MipMapImageRaster MIR = new MipMapImageRaster(someImage, 0);
        thrown.expect(IllegalArgumentException.class);
        MIR.getPixel(3,4);
    }
    @Test
    public void testMipConstructNoMip(){
        Image someImage = getMockImage();
        thrown.expect(IllegalArgumentException.class);
        new MipMapImageRaster(someImage, 0);
    }
    @Test
    public void testMipSetMipBelowLimit(){
        Image someImage = getMipMockImage();
        MipMapImageRaster MIR = new MipMapImageRaster(someImage, 0);
        MIR.setMipLevel(3);
    }
    @Test
    public void testMipSetMipTooHigh(){
        Image someImage = getMipMockImage();
        MipMapImageRaster MIR = new MipMapImageRaster(someImage, 0);
        thrown.expect(IllegalArgumentException.class);
        MIR.setMipLevel(4);
    }
    @Test
    public void testDefaultRangeCheckMip0Succ(){
        Image someImage = getMipMockImage();
        MipMapImageRaster MIR = new MipMapImageRaster(someImage, 0);
        MIR.setMipLevel(0);
        MIR.getPixel(3,3);
    }
    @Test
    public void testDefaultRangeCheckMip0Fail(){
        Image someImage = getMipMockImage();
        MipMapImageRaster MIR = new MipMapImageRaster(someImage, 0);
        MIR.setMipLevel(0);
        thrown.expect(IllegalArgumentException.class);
        MIR.getPixel(3,4);
    }
    @Test
    public void testDefaultRangeCheckMip1Succ(){
        Image someImage = getMipMockImage();
        MipMapImageRaster MIR = new MipMapImageRaster(someImage, 0);
        MIR.setMipLevel(1);
        MIR.getPixel(1,1);
    }
    @Test
    public void testDefaultRangeCheckMip1Fail(){
        Image someImage = getMipMockImage();
        MipMapImageRaster MIR = new MipMapImageRaster(someImage, 0);
        MIR.setMipLevel(1);
        thrown.expect(IllegalArgumentException.class);
        MIR.getPixel(1,2);
    }
}
