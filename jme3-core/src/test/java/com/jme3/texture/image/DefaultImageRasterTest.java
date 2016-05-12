package com.jme3.texture.image;

import java.nio.ByteBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.image.ImageRaster;
import com.jme3.texture.Image.Format;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
