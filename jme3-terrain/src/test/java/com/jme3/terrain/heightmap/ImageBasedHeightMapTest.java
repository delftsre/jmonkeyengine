package com.jme3.terrain.heightmap;

import com.jme3.texture.Image;
import com.jme3.texture.image.DefaultImageRaster;
import com.jme3.texture.image.ImageRaster;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class ImageBasedHeightMapTest {

    private ImageBasedHeightMap heightMap;
    private Image image;

    @Before
    public void setUp() {
        image = createImage();
        heightMap = new ImageBasedHeightMap(image);

    }

    public Image createImage() {
        Image image = spy(new Image());
        when(image.getFormat()).thenReturn(Image.Format.BGR8);
        return image;
    }

    @Test
    public void testGetImageRaster() {
        ImageRaster imageRaster = heightMap.getImageRaster();

        assertThat(imageRaster, instanceOf(DefaultImageRaster.class));
    }
}
