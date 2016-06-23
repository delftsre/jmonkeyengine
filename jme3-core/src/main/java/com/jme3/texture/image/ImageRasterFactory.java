package com.jme3.texture.image;

import com.jme3.texture.Image;

public class ImageRasterFactory {
    /**
     * Create new image reader / writer.
     *
     * @param image The image to read / write to.
     * @param slice Which slice to use. Only applies to 3D images, 2D image
     * arrays or cubemaps.
     * @param mipMapLevel The mipmap level to read / write to. To access levels 
     * other than 0, the image must have 
     * {@link Image#setMipMapSizes(int[]) mipmap sizes} set.
     * @param convertToLinear If true, the application expects read or written
     * colors to be in linear color space (<code>ImageRaster</code> will
     * automatically perform a conversion as needed). If false, the application expects
     * colors to be in the image's native {@link Image#getColorSpace() color space}.
     * @return An ImageRaster to read / write to the image.
     */
    public static ImageRaster create(Image image, int slice, int mipMapLevel, boolean convertToLinear) {
        return new DefaultImageRaster(image, slice, mipMapLevel, convertToLinear);
    }
    
    /**
     * Create new image reader / writer.
     *
     * @param image The image to read / write to.
     * @param slice Which slice to use. Only applies to 3D images, 2D image
     * arrays or cubemaps.
     * @return An ImageRaster to read / write to the image.
     */
    public static ImageRaster create(Image image, int slice) {
        return create(image, slice, 0, false);
    }
    
    /**
     * Create new image reader / writer for 2D images.
     * 
     * @param image The image to read / write to.
     * @return An ImageRaster to read / write to the image.
     */
    public static ImageRaster create(Image image) {
        if (image.getData().size() > 1) {
            throw new IllegalStateException("Use constructor that takes slices argument to read from multislice image");
        }
        return create(image, 0, 0, false);
    }
}
