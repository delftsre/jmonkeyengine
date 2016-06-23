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
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;
import com.jme3.util.TempVars;

/**
 * Interface for representing a light source.
 */
public interface ILight extends Savable, Cloneable {
    /**
     * Describes the light type.
     */
    public enum Type {

        /**
         * Directional light
         * 
         * @see DirectionalLight
         */
        Directional(0),
        
        /**
         * Point light
         * 
         * @see PointLight
         */
        Point(1),
        
        /**
         * Spot light.
         * 
         * @see SpotLight
         */
        Spot(2),
        
        /**
         * Ambient light
         * 
         * @see AmbientLight
         */
        Ambient(3);

        private int typeId;

        Type(int type){
            this.typeId = type;
        }

        /**
         * Returns an index for the light type
         * @return an index for the light type
         */
        public int getId(){
            return typeId;
        }
  }
  public float getLastDistance();
  public void setLastDistance(float lastDistance);
  /**
   * This method sets the light name.
   * 
   * @param name the light name
   */
  void setName(String name);
   /**
   * Return the light name.
   * 
   * @return the light name
   */
  String getName();
  /**
   * Sets the light color.
   * 
   * @param color the light color.
   */
  void setColor(ColorRGBA color);
  /**
   * Returns the color of the light.
   * 
   * @return The color of the light.
   */
  ColorRGBA getColor();
  /**
   * Returns true if this light is enabled.
   * @return true if enabled, otherwise false.
   */
  boolean isEnabled();
  /**
   * Set to false in order to disable a light and have it filtered out from being included in rendering.
   *
   * @param enabled true to enable and false to disable the light.
   */
  void setEnabled(boolean enabled);
  /**
   * Returns the light type
   * 
   * @return the light type
   * 
   * @see Type
   */
  Type getType();
  /**
   * Determines if the light intersects with the given bounding box.
   * <p>
   * For non-local lights, such as {@link DirectionalLight directional lights},
   * {@link AmbientLight ambient lights}, or {@link PointLight point lights}
   * without influence radius, this method should always return true.
   * 
   * @param box The box to check intersection against.
   * @param vars TempVars in case it is needed.
   * 
   * @return True if the light intersects the box, false otherwise.
   */
  boolean intersectsBox(BoundingBox box, TempVars vars);
  /**
   * Determines if the light intersects with the given bounding sphere.
   * <p>
   * For non-local lights, such as {@link DirectionalLight directional lights},
   * {@link AmbientLight ambient lights}, or {@link PointLight point lights}
   * without influence radius, this method should always return true.
   * 
   * @param sphere The sphere to check intersection against.
   * @param vars TempVars in case it is needed.
   * 
   * @return True if the light intersects the sphere, false otherwise.
   */
  boolean intersectsSphere(BoundingSphere sphere, TempVars vars);
  /**
   * Determines if the light intersects with the given camera frustum.
   * 
   * For non-local lights, such as {@link DirectionalLight directional lights},
   * {@link AmbientLight ambient lights}, or {@link PointLight point lights}
   * without influence radius, this method should always return true.
   * 
   * @param camera The camera frustum to check intersection against.
   * @param vars TempVars in case it is needed.
   * @return True if the light intersects the frustum, false otherwise.
   */
  boolean intersectsFrustum(Camera camera, TempVars vars);
  /**
   * Used internally to compute the last distance value.
   */
  void computeLastDistance(Spatial owner);
  void setIntersectsFrustum(boolean intersectsFrustum);
  boolean isIntersectsFrustum();
  boolean isFrustumCheckNeeded();
  void setFrustumCheckNeeded(boolean frustumCheckNeeded);
  Light clone();
  void write(JmeExporter ex) throws IOException;
  void read(JmeImporter im) throws IOException;
}