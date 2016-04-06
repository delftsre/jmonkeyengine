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
import com.jme3.export.JmeImporter;
import com.jme3.texture.image.ColorSpace;
import java.io.IOException;

/**
 * @author Joshua Slack
 */
public class TextureDefault2D extends Texture2D {

    /**
     * Creates a new two-dimensional texture with default attributes.
     */
    public TextureDefault2D(){
        super();
    }

    /**
     * Creates a new two-dimensional texture using the given image.
     * @param img The image to use.
     */
    public TextureDefault2D(Image img){
        super();
        setImage(img);
        if (img.getData(0) == null) {
            setMagFilter(MagFilter.Nearest);
            setMinFilter(MinFilter.NearestNoMipMaps);
        }
    }

    /**
     * Creates a new two-dimensional texture for the purpose of offscreen
     * rendering.
     *
     * @see com.jme3.texture.FrameBuffer
     *
     * @param width
     * @param height
     * @param format
     */
    public TextureDefault2D(int width, int height, Image.Format format){
        this(new Image(format, width, height, null, ColorSpace.Linear));
    }

    /**
     * Creates a new two-dimensional texture for the purpose of offscreen
     * rendering.
     *
     * @see com.jme3.texture.FrameBuffer
     *
     * @param width
     * @param height
     * @param format
     * @param numSamples
     */
    public TextureDefault2D(int width, int height, int numSamples, Image.Format format){
        this(new Image(format, width, height, null, ColorSpace.Linear));
        getImage().setMultiSamples(numSamples);
    }

    @Override
    public Texture createSimpleClone() {
        TextureDefault2D clone = new TextureDefault2D();
        createSimpleClone(clone);
        return clone;
    }

    @Override
    public Type getType() {
        return Type.TwoDimensional;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TextureDefault2D)) {
            return false;
        }
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 79 * hash + (this.wrapS != null ? this.wrapS.hashCode() : 0);
        hash = 79 * hash + (this.wrapT != null ? this.wrapT.hashCode() : 0);
        return hash;
    }

    @Override
    public void read(JmeImporter e) throws IOException {
        super.read(e);
        InputCapsule capsule = e.getCapsule(this);
        wrapS = capsule.readEnum("wrapS", WrapMode.class, WrapMode.EdgeClamp);
        wrapT = capsule.readEnum("wrapT", WrapMode.class, WrapMode.EdgeClamp);
    }

}
