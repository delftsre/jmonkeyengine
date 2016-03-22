/*
 * Copyright (c) 2009-2012, 2015-2016 jMonkeyEngine
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
package com.jme3.light;

import java.io.IOException;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;

/**
 * Abstract class for representing a light source.
 * <p>
 * All light source types have a color.
 */
public abstract class Light implements ILight {

    protected ColorRGBA color = new ColorRGBA(ColorRGBA.White);
    
    /**
     * Used in LightList for caching the distance 
     * to the owner spatial. Should be reset after the sorting.
     */
    protected transient float lastDistance = -1;

    protected boolean enabled = true;

    /** 
     * The light name. 
     */
    protected String name;
    
    protected boolean frustumCheckNeeded = true;
    protected boolean intersectsFrustum  = false;

    protected Light() {
    }

    protected Light(ColorRGBA color) {
        setColor(color);
    }

    @Override
    public ColorRGBA getColor() {
        return color;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setIntersectsFrustum(boolean intersectsFrustum) {
        this.intersectsFrustum = intersectsFrustum;
    }

    @Override
    public boolean isIntersectsFrustum() {
        return intersectsFrustum;
    }

    @Override
    public void setFrustumCheckNeeded(boolean frustumCheckNeeded) {
        this.frustumCheckNeeded = frustumCheckNeeded;
    }

    @Override
    public boolean isFrustumCheckNeeded() {
        return frustumCheckNeeded;
    }

    /*
    public void setLastDistance(float lastDistance){
        this.lastDistance = lastDistance;
    }

    public float getLastDistance(){
        return lastDistance;
    }
    */

    @Override
    public void setColor(ColorRGBA color){
        this.color.set(color);
    }


    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public float getLastDistance() {
        return lastDistance;
    }

    @Override
    public void setLastDistance(float lastDistance) {
        this.lastDistance = lastDistance;
    }

    @Override
    public abstract boolean intersectsBox(BoundingBox box, TempVars vars);
    
    @Override
    public abstract boolean intersectsSphere(BoundingSphere sphere, TempVars vars);

    @Override
    public abstract boolean intersectsFrustum(Camera camera, TempVars vars);

    @Override
    public abstract void computeLastDistance(Spatial owner);
    
    @Override
    public Light clone() {
        try {
            Light l = (Light) super.clone();
            l.color = color.clone();
            return l;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(color, "color", null);
        oc.write(enabled, "enabled", true);
        oc.write(name, "name", null);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        color = (ColorRGBA) ic.readSavable("color", null);
        enabled = ic.readBoolean("enabled", true);
        name = ic.readString("name", null);
    }    
    
    @Override
    public abstract Type getType();

}
