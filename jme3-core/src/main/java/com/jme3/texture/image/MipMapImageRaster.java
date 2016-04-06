/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.texture.image;

import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.texture.Image;

import java.nio.ByteBuffer;

public class MipMapImageRaster extends ImageRaster {

    protected final int[] components = new int[4];
    private ByteBuffer buffer;
    protected final Image image;
    protected final ImageCodec codec;
    private int width[];
    private int height[];
    private final byte[] temp;
    private int slice;
    private int mipLevel;
    private int[] offsets;

    protected void rangeCheck(int x, int y) {
        if (x < 0 || y < 0 || x >= width[mipLevel] || y >= height[mipLevel]) {
            throw new IllegalArgumentException("x and y must be inside the image dimensions");
        }
    }

    public MipMapImageRaster(Image image, int slice) {
        this.image = image;
        this.slice = slice;
        this.buffer = image.getData(slice);
        this.codec = ImageCodec.lookup(image.getFormat());
        if (image.hasMipmaps()) {
            int nbMipMap = image.getMipMapSizes().length;
            this.width = new int[nbMipMap];
            this.height = new int[nbMipMap];
            this.offsets = new int[nbMipMap];
            for (int i = 0; i < nbMipMap; i++) {
                width[i] = Math.max(1, image.getWidth() >> i);
                height[i] = Math.max(1, image.getHeight() >> i);
                if (i > 0) {
                    offsets[i] = image.getMipMapSizes()[i - 1] + offsets[i - 1];
                }
            }

        } else {
            throw new IllegalArgumentException("Image must have MipMapSizes initialized.");
        }

        if (codec instanceof ByteAlignedImageCodec || codec instanceof ByteOffsetImageCodec) {
            this.temp = new byte[codec.bpp];
        } else {
            this.temp = null;
        }
    }

    public void setSlice(int slice) {
        this.slice = slice;
        this.buffer = image.getData(slice);
    }

    public void setMipLevel(int mipLevel) {
        if (mipLevel >= image.getMipMapSizes().length || mipLevel < 0) {
            throw new IllegalArgumentException("Mip level must be between 0 and " + image.getMipMapSizes().length);
        }
        this.mipLevel = mipLevel;
    }

    @Override
    public void setPixel(int x, int y, ColorRGBA color) {
        rangeCheck(x, y);
        grayscaleCheck(color, codec);
        setComponents(color, codec, components);
        writeComponents(x, y);
        setImageUpdateNeeded(image);
    }

    protected void writeComponents(int x, int y) {
        codec.writeComponents(getBuffer(), x, y, width[mipLevel], offsets[mipLevel], components, temp);
    }

    protected void readComponents(int x, int y) {
        codec.readComponents(getBuffer(), x, y, width[mipLevel], offsets[mipLevel], components, temp);
    }

    private ByteBuffer getBuffer() {
        if (buffer == null) {
            this.buffer = image.getData(slice);
        }
        return buffer;
    }

    @Override
    public ColorRGBA getPixel(int x, int y, ColorRGBA store) {
        rangeCheck(x, y);
        readComponents(x, y);
        if (store == null) {
            store = new ColorRGBA();
        }
        setStoreComponents(store, codec, components);
        setStoreRGBA(store, codec);
        return store;
    }

    @Override
    public int getWidth() {
        return width[mipLevel];
    }

    @Override
    public int getHeight() {
        return height[mipLevel];
    }
}
