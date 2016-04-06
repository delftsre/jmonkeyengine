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
package com.jme3.texture;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.texture.image.ColorSpace;
import java.io.IOException;

/**
 * @author Maarten Steur
 */
public class TextureDefault3D extends Texture3D {

    /**
     * Creates a new two-dimensional texture with default attributes.
     */
    public TextureDefault3D() {
        super();
    }

    /**
     * Creates a new three-dimensional texture using the given image.
     * @param img The image to use.
     */
    public TextureDefault3D(Image img) {
        super();
        setImage(img);
        if (img.getFormat().isDepthFormat()) {
            setMagFilter(MagFilter.Nearest);
            setMinFilter(MinFilter.NearestNoMipMaps);
        }
    }

    /**
     * Creates a new three-dimensional texture for the purpose of offscreen
     * rendering.
     *
     * @see com.jme3.texture.FrameBuffer
     *
     * @param width
     * @param height
     * @param depth
     * @param format
     */
    public TextureDefault3D(int width, int height, int depth, Image.Format format) {
        this(new Image(format, width, height, depth, null, ColorSpace.Linear));
    }

    /**
     * Creates a new three-dimensional texture for the purpose of offscreen
     * rendering.
     *
     * @see com.jme3.texture.FrameBuffer
     *
     * @param width
     * @param height
     * @param format
     * @param numSamples
     */
    public TextureDefault3D(int width, int height, int depth, int numSamples, Image.Format format) {
        this(new Image(format, width, height, depth, null, ColorSpace.Linear));
        getImage().setMultiSamples(numSamples);
    }

    @Override
    public Texture createSimpleClone() {
        return createSimpleClone(new TextureDefault3D());
    }

    @Override
    public Type getType() {
        return Type.ThreeDimensional;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TextureDefault3D)) {
            return false;
        }
        TextureDefault3D that = (TextureDefault3D) other;
        if (this.getWrap(WrapAxis.R) != that.getWrap(WrapAxis.R)) {
            return false;
        }
        return super.equals(other);
    }


}