package com.jme3.texture.image;

import java.nio.ByteBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CommonImageRasterTest {
    protected Image getMockImage(){
        ByteBuffer BB0 = ByteBuffer.allocate(100);
        ByteBuffer BB1 = ByteBuffer.allocate(100);
        Image mockImage = Mockito.mock(Image.class);
        Format someFormat = Format.RGB8;
        when(mockImage.getData(0)).thenReturn(BB0);
        when(mockImage.getData(1)).thenReturn(BB1);
        when(mockImage.getFormat()).thenReturn(someFormat);
        when(mockImage.getWidth()).thenReturn(4);
        when(mockImage.getHeight()).thenReturn(4);

        return mockImage;
    }
    protected Image getMipMockImage(){
        Image mockImage = getMockImage();
        when(mockImage.hasMipmaps()).thenReturn(true);
        when(mockImage.getMipMapSizes()).thenReturn(new int[]{1,2,3,4});

        return mockImage;
    }
}
