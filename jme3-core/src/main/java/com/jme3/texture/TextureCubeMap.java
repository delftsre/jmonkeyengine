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
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Describes a cubemap texture.
 * The image specified by setImage must contain 6 data units,
 * each data contains a 2D image representing a cube's face.
 * The slices are specified in this order:<br/>
 * <br/>
 * 0 => Positive X (+x)<br/>
 * 1 => Negative X (-x)<br/>
 * 2 => Positive Y (+y)<br/>
 * 3 => Negative Y (-y)<br/>
 * 4 => Positive Z (+z)<br/>
 * 5 => Negative Z (-z)<br/>
 *
 * @author Joshua Slack
 */
public class TextureCubeMap extends Texture3D {

    /**
     * Face of the Cubemap as described by its directional offset from the
     * origin.
     */
    public enum Face {

        PositiveX, NegativeX, PositiveY, NegativeY, PositiveZ, NegativeZ;
    }

    public TextureCubeMap(){
        super();
    }

    public TextureCubeMap(Image img){
        super();
        setImage(img);
    }
    
    public TextureCubeMap(int width, int height, Image.Format format){
        this(createEmptyLayeredImage(width, height, 6, format));
    }

    private static Image createEmptyLayeredImage(int width, int height,
            int layerCount, Image.Format format) {
        ArrayList<ByteBuffer> layers = new ArrayList<ByteBuffer>();
        for(int i = 0; i < layerCount; i++) {
            layers.add(null);
        }
        Image image = new Image(format, width, height, 0, layers, ColorSpace.Linear);
        return image;
    }

    public Texture createSimpleClone() {
        return createSimpleClone(new TextureCubeMap());
    }

    @Override
    public Type getType() {
        return Type.CubeMap;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TextureCubeMap)) {
            return false;
        }
        TextureCubeMap that = (TextureCubeMap) other;
        if (this.getWrap(WrapAxis.R) != that.getWrap(WrapAxis.R))
            return false;
        return super.equals(other);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 53 * hash + (this.wrapS != null ? this.wrapS.hashCode() : 0);
        hash = 53 * hash + (this.wrapT != null ? this.wrapT.hashCode() : 0);
        hash = 53 * hash + (this.wrapR != null ? this.wrapR.hashCode() : 0);
        return hash;
    }
}
