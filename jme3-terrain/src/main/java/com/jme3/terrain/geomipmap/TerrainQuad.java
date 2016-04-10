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
package com.jme3.terrain.geomipmap;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.WireBox;
import com.jme3.terrain.ProgressMonitor;
import com.jme3.terrain.Terrain;
import com.jme3.terrain.geomipmap.lodcalc.LodCalculator;
import com.jme3.terrain.geomipmap.picking.BresenhamTerrainPicker;
import com.jme3.terrain.geomipmap.picking.TerrainPickData;
import com.jme3.terrain.geomipmap.picking.TerrainPicker;
import com.jme3.util.SafeArrayList;
import com.jme3.util.TangentBinormalGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>
 * TerrainQuad is a heightfield-based terrain system. Heightfield terrain is fast and can
 * render large areas, and allows for easy Level of Detail control. However it does not
 * permit caves easily.
 * TerrainQuad is a quad tree, meaning that the root quad has four children, and each of
 * those children have four children. All the way down until you reach the bottom, the actual
 * geometry, the TerrainPatches.
 * If you look at a TerrainQuad in wireframe mode with the TerrainLODControl attached, you will
 * see blocks that change their LOD level together; these are the TerrainPatches. The TerrainQuad
 * is just an organizational structure for the TerrainPatches so patches that are not in the
 * view frustum get culled quickly.
 * TerrainQuads size are a power of 2, plus 1. So 513x513, or 1025x1025 etc.
 * Each point in the terrain is one unit apart from its neighbour. So a 513x513 terrain
 * will be 513 units wide and 513 units long.
 * Patch size can be specified on the terrain. This sets how large each geometry (TerrainPatch)
 * is. It also must be a power of 2 plus 1 so the terrain can be subdivided equally.
 * </p>
 * <p>
 * The height of the terrain can be modified at runtime using setHeight()
 * </p>
 * <p>
 * A terrain quad is a node in the quad tree of the terrain system.
 * The root terrain quad will be the only one that receives the update() call every frame
 * and it will determine if there has been any LOD change.
 * </p><p>
 * The leaves of the terrain quad tree are Terrain Patches. These have the real geometry mesh.
 * </p><p>
 * Heightmap coordinates start from the bottom left of the world and work towards the
 * top right.
 * </p><pre>
 *  +x
 *  ^
 *  | ......N = length of heightmap
 *  | :     :
 *  | :     :
 *  | 0.....:
 *  +---------&gt; +z
 * (world coordinates)
 * </pre>
 * @author Brent Owens
 */
public class TerrainQuad extends Node implements Terrain {
    protected Vector2f offset;

    protected int totalSize; // the size of this entire terrain tree (on one side)

    protected int size; // size of this quad, can be between totalSize and patchSize

    protected int patchSize; // size of the individual patches

    protected Vector3f stepScale;

    protected float offsetAmount;

    protected int quadrant = 0; // 1=upper left, 2=lower left, 3=upper right, 4=lower right
    private int maxLod = -1;
    private BoundingBox affectedAreaBBox; // only set in the root quad

    private TerrainPicker picker;
    private Vector3f lastScale = Vector3f.UNIT_XYZ;

    protected NeighbourFinder neighbourFinder;
    
    public TerrainQuad() {
        super("Terrain");
    }

    /**
     * Creates a terrain with:
     * <ul>
     * <li>the total, real-world, size of the terrain</li>
     * <li>the patchSize, or the size of each geometry tile of the terrain</li>
     * <li>the heightmap that defines the height of the terrain</li>
     * </ul>
     * <p>
     * A TerrainQuad of totalSize 513x513 will be 513 units wide and 513 units long.
     * PatchSize is just used to subdivide the terrain into tiles that can be culled.
     * </p>
     * @param name the name of the scene element. This is required for
     * identification and comparison purposes.
     * @param patchSize size of the individual patches (geometry). Power of 2 plus 1, 
     * must be smaller than totalSize. (eg. 33, 65...)
     * @param totalSize the size of this entire terrain (on one side). Power of 2 plus 1 
     * (eg. 513, 1025, 2049...)
     * @param heightMap The height map to generate the terrain from (a flat
     * height map will be generated if this is null). The size of one side of the heightmap 
     * must match the totalSize. So a 513x513 heightmap is needed for a terrain with totalSize of 513.
     */
    public TerrainQuad(String name, int patchSize, int totalSize, float[] heightMap) {
        this(name, patchSize, totalSize, Vector3f.UNIT_XYZ, heightMap);
                
        affectedAreaBBox = new BoundingBox(new Vector3f(0,0,0), size*2, Float.MAX_VALUE, size*2);
        fixNormalEdges(affectedAreaBBox);
        addControl(new NormalRecalcControl(this));
    }
    
    /**
     * 
     * @param name the name of the scene element. This is required for
     * identification and comparison purposes.
     * @param patchSize size of the individual patches
     * @param quadSize
     * @param totalSize the size of this entire terrain tree (on one side)
     * @param heightMap The height map to generate the terrain from (a flat
     * height map will be generated if this is null)
     */
    @Deprecated
    public TerrainQuad(String name, int patchSize, int quadSize, int totalSize, float[] heightMap) {
        this(name, patchSize, totalSize, quadSize, Vector3f.UNIT_XYZ, heightMap);
    }

    /**
     * 
     * @param name the name of the scene element. This is required for
     * identification and comparison purposes.
     * @param patchSize size of the individual patches
     * @param size size of this quad, can be between totalSize and patchSize
     * @param scale
     * @param heightMap The height map to generate the terrain from (a flat
     * height map will be generated if this is null)
     */
    @Deprecated
    public TerrainQuad(String name, int patchSize, int size, Vector3f scale, float[] heightMap) {
        this(name, patchSize, size, scale, heightMap, size, new Vector2f(), 0);
        //affectedAreaBBox = new BoundingBox(new Vector3f(0,0,0), size*2, Float.MAX_VALUE, size*2);
        //fixNormalEdges(affectedAreaBBox);
        //addControl(new NormalRecalcControl(this));
    }
    
    /**
     * 
     * @param name the name of the scene element. This is required for
     * identification and comparison purposes.
     * @param patchSize size of the individual patches
     * @param totalSize the size of this entire terrain tree (on one side)
     * @param quadSize
     * @param scale
     * @param heightMap The height map to generate the terrain from (a flat
     * height map will be generated if this is null)
     */
    @Deprecated
    public TerrainQuad(String name, int patchSize, int totalSize, int quadSize, Vector3f scale, float[] heightMap) {
        this(name, patchSize, quadSize, scale, heightMap, totalSize, new Vector2f(), 0);
        //affectedAreaBBox = new BoundingBox(new Vector3f(0,0,0), totalSize*2, Float.MAX_VALUE, totalSize*2);
        //fixNormalEdges(affectedAreaBBox);
        //addControl(new NormalRecalcControl(this));
    }
    

    public TerrainQuad(String name, int patchSize, int quadSize,
                            Vector3f scale, float[] heightMap, int totalSize,
                            Vector2f offset, float offsetAmount)
    {
    	
        super(name);        
        
        if (heightMap == null)
            heightMap = Utils.generateDefaultHeightMap(quadSize);
        
        if (!FastMath.isPowerOfTwo(quadSize - 1)) {
            throw new RuntimeException("size given: " + quadSize + "  Terrain quad sizes may only be (2^N + 1)");
        }
        if (FastMath.sqrt(heightMap.length) > quadSize) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Heightmap size is larger than the terrain size. Make sure your heightmap image is the same size as the terrain!");
        }
        
        this.offset = offset;
        this.offsetAmount = offsetAmount;
        this.totalSize = totalSize;
        this.size = quadSize;
        this.patchSize = patchSize;
        this.stepScale = scale;
        TerrainCreatePatch.split(patchSize, heightMap, this);
    }

    public void setNeighbourFinder(NeighbourFinder neighbourFinder) {
        this.neighbourFinder = neighbourFinder;
        TerrainTransform.resetCachedNeighbours(this);
    }

    /**
     * Forces the recalculation of all normals on the terrain.
     */
    public void recalculateAllNormals() {
        affectedAreaBBox = new BoundingBox(new Vector3f(0,0,0), totalSize*2, Float.MAX_VALUE, totalSize*2);
    }
    
    public SafeArrayList<Spatial> getChildren() {
    	return children;
    }
    
    public void setChildren(SafeArrayList<Spatial> child) {
    	this.children = child;
    }
    
    public void setQuadrant(int quad) {
    	this.quadrant = quad;
    }
    
    
    public Node getRemoteParent() {
    	return getParent();
    }
    
    public NeighbourFinder getNeighbourFinder() {
    	return this.neighbourFinder;
    }
    
    public void setPicker(TerrainPicker tp) {
    	this.picker = tp;
    }
    
    public TerrainPicker getPicker() {
    	return picker;
    }
    
    public BoundingBox getAffectedAreaBBox() {
    	return this.affectedAreaBBox;
    }
    
    public void setAffectedAreaBBox(BoundingBox bb) {
    	this.affectedAreaBBox = bb;
    }
    
    public Vector3f getLastScale() {
    	return this.lastScale;
    }
    
    public Vector3f getStepScale() {
    	return this.stepScale;
    }
    
    public void setLastScale(Vector3f ls) {
    	this.lastScale = ls;
    }
    
    public int getSize() {
    	return this.size;
    }
    
    public float getOffsetAmount() {
    	return this.offsetAmount;
    }
    
    public void setOffsetAmount(float os) {
    	this.offsetAmount = os;
    }
    
    public Vector2f getOffSet() {
    	return this.offset;
    }
    
    public int getTerrainSize() {
        return totalSize;
    }
    
    public int getMaxLod() {
        if (maxLod < 0)
            maxLod = Math.max(1, (int) (FastMath.log(size-1)/FastMath.log(2)) -1); // -1 forces our minimum of 4 triangles wide

        return maxLod;
    }

    public int getPatchSize() {
        return patchSize;
    }

    public int getTotalSize() {
        return totalSize;
    }
    
    public int getQuadrant() {
        return quadrant;
    }

    public void setQuadrant(short quadrant) {
        this.quadrant = quadrant;
    }
    
    /**
     * Generate the entropy values for the terrain for the "perspective" LOD
     * calculator. This routine can take a long time to run!
     * @param progressMonitor optional
     */
    public void generateEntropy(ProgressMonitor progressMonitor) {
    	TerrainTransform.generateEntropy(progressMonitor, this);
    }

    protected boolean isRootQuad() {
        return (getParent() != null && !(getParent() instanceof TerrainQuad) );
    }

    public Material getMaterial() {
        return getMaterial(null);
    }
    
    public Material getMaterial(Vector3f worldLocation) {
        // get the material from one of the children. They all share the same material
        if (children != null) {
            for (int i = children.size(); --i >= 0;) {
                Spatial child = children.get(i);
                if (child instanceof TerrainQuad) {
                    return ((TerrainQuad)child).getMaterial(worldLocation);
                } else if (child instanceof TerrainPatch) {
                    return ((TerrainPatch)child).getMaterial();
                }
            }
        }
        return null;
    }

    public int getNumMajorSubdivisions() {
        return 1;
    }
   

    /**
     * A handy method that will attach all bounding boxes of this terrain
     * to the node you supply.
     * Useful to visualize the bounding boxes when debugging.
     * @param parent that will get the bounding box shapes of the terrain attached to
     */
    public void attachBoundChildren(Node parent) {
        for (int i = 0; i < this.getQuantity(); i++) {
            if (this.getChild(i) instanceof TerrainQuad) {
                ((TerrainQuad) getChild(i)).attachBoundChildren(parent);
            } else if (this.getChild(i) instanceof TerrainPatch) {
                BoundingVolume bv = getChild(i).getWorldBound();
                if (bv instanceof BoundingBox) {
                    attachBoundingBox((BoundingBox)bv, parent);
                }
            }
        }
        BoundingVolume bv = getWorldBound();
        if (bv instanceof BoundingBox) {
            attachBoundingBox((BoundingBox)bv, parent);
        }
    }

    /**
     * used by attachBoundChildren()
     */
    private void attachBoundingBox(BoundingBox bb, Node parent) {
        WireBox wb = new WireBox(bb.getXExtent(), bb.getYExtent(), bb.getZExtent());
        Geometry g = new Geometry();
        g.setMesh(wb);
        g.setLocalTranslation(bb.getCenter());
        parent.attachChild(g);
    }
    
    /**
     * This will cause all normals for this terrain quad to be recalculated
     */
    protected void setNeedToRecalculateNormals() {
        affectedAreaBBox = new BoundingBox(getWorldTranslation(), Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE);
    }
    
    protected static class LocationHeight {
        int x;
        int z;
        float h;

        LocationHeight(){}

        LocationHeight(int x, int z, float h){
            this.x = x;
            this.z = z;
            this.h = h;
        }
    }
    
    public float getHeightmapHeight(Vector2f xz) {
    	return TerrainHeight.getHeightmapHeight(xz, this);
    }
    

    /**
     * This will just get the heightmap value at the supplied point,
     * not an interpolated (actual) height value.
     * Jorden: Keeper
     */   
    
    protected float getHeightmapHeight(int x, int z) {

    	QuadPoint quad = new QuadPoint(x, z, children, size);
    	return quad.calculateHeightMap(x,z);
    }
    
    /**
     * Get the interpolated height of the terrain at the specified point.
     * @param xz the location to get the height for
     * @return Float.NAN if the value does not exist, or the coordinates are outside of the terrain
     */
    
    public float getHeight(Vector2f xz) {
        // offset
        float x = (float)(((xz.x - getWorldTranslation().x) / getWorldScale().x) + (float)(totalSize-1) / 2f);
        float z = (float)(((xz.y - getWorldTranslation().z) / getWorldScale().z) + (float)(totalSize-1) / 2f);
        if (!Utils.isInside((int)x, (int)z, totalSize))
            return Float.NaN;
        float height = TerrainHeight.getHeight((int)x, (int)z, (x%1f), (z%1f), this);
        height *= getWorldScale().y;
        return height;
    }


    public Vector3f getNormal(Vector2f xz) {
        // offset
        float x = (float)(((xz.x - getWorldTranslation().x) / getWorldScale().x) + (float)(totalSize-1) / 2f);
        float z = (float)(((xz.y - getWorldTranslation().z) / getWorldScale().z) + (float)(totalSize-1) / 2f);
        Vector3f normal = TerrainNormals.getNormal(x, z, xz, this);
        
        return normal;
    }
    

    public void adjustHeight(Vector2f xz, float delta) {
        List<Vector2f> coord = new ArrayList<Vector2f>();
        coord.add(xz);
        List<Float> h = new ArrayList<Float>();
        h.add(delta);

        adjustHeight(coord, h);
    }
    
    
    public  void setHeight(Vector2f xz, float height) {
        List<Vector2f> coord = new ArrayList<Vector2f>();
        coord.add(xz);
        List<Float> h = new ArrayList<Float>();
        h.add(height);

        setHeight(coord, h);
    }
    
    
    public void adjustHeight(List<Vector2f> xz, List<Float> height) {
        TerrainHeight.setHeight(xz, height, false, this);
    }

    
    public void setHeight(List<Vector2f> xz, List<Float> height) {
        TerrainHeight.setHeight(xz, height, true, this);
    }
    

    
    protected void setHeight(List<LocationHeight> locations, boolean overrideHeight) {
    	TerrainHeight.setHeight(locations, overrideHeight, children, size);
    }


    /**
     * lock or unlock the meshes of this terrain.
     * Locked meshes are uneditable but have better performance.
     * @param locked or unlocked
     */
    public void setLocked(boolean locked) {
        for (int i = 0; i < this.getQuantity(); i++) {
            if (this.getChild(i) instanceof TerrainQuad) {
                ((TerrainQuad) getChild(i)).setLocked(locked);
            } else if (this.getChild(i) instanceof TerrainPatch) {
                if (locked)
                    ((TerrainPatch) getChild(i)).lockMesh();
                else
                    ((TerrainPatch) getChild(i)).unlockMesh();
            }
        }
    }


    /**
     * fix the normals on the edge of the terrain patches.
     */
    protected void fixNormalEdges(BoundingBox affectedArea) {
    	TerrainQuadrants.fixNormalEdges(affectedArea, this);
    }



    @Override
    public int collideWith(Collidable other, CollisionResults results){
        int total = 0;

        if (other instanceof Ray)
            return TerrainTransform.collideWithRay((Ray)other, results, this);

        // if it didn't collide with this bbox, return
        if (other instanceof BoundingVolume)
            if (!this.getWorldBound().intersects((BoundingVolume)other))
                return total;

        for (Spatial child : children){
            total += child.collideWith(other, results);
        }
        return total;
    }

    @Override
    public void read(JmeImporter e) throws IOException {
        super.read(e);
        InputCapsule c = e.getCapsule(this);
        size = c.readInt("size", 0);
        stepScale = (Vector3f) c.readSavable("stepScale", null);
        offset = (Vector2f) c.readSavable("offset", new Vector2f(0,0));
        offsetAmount = c.readFloat("offsetAmount", 0);
        quadrant = c.readInt("quadrant", 0);
        totalSize = c.readInt("totalSize", 0);
        //lodCalculator = (LodCalculator) c.readSavable("lodCalculator", createDefaultLodCalculator());
        //lodCalculatorFactory = (LodCalculatorFactory) c.readSavable("lodCalculatorFactory", null);
        
        if ( !(getParent() instanceof TerrainQuad) ) {
            BoundingBox all = new BoundingBox(getWorldTranslation(), totalSize, totalSize, totalSize);
            affectedAreaBBox = all;
            TerrainNormals.updateNormals(this);
        }
    }

    @Override
    public void write(JmeExporter e) throws IOException {
        super.write(e);
        OutputCapsule c = e.getCapsule(this);
        c.write(size, "size", 0);
        c.write(totalSize, "totalSize", 0);
        c.write(stepScale, "stepScale", null);
        c.write(offset, "offset", new Vector2f(0,0));
        c.write(offsetAmount, "offsetAmount", 0);
        c.write(quadrant, "quadrant", 0);
        //c.write(lodCalculatorFactory, "lodCalculatorFactory", null);
        //c.write(lodCalculator, "lodCalculator", null);
    }

    @Override
    public TerrainQuad clone() {
        return this.clone(true);
    }

	@Override
    public TerrainQuad clone(boolean cloneMaterials) {
        TerrainQuad quadClone = (TerrainQuad) super.clone(cloneMaterials);
        quadClone.name = name.toString();
        quadClone.size = size;
        quadClone.totalSize = totalSize;
        if (stepScale != null) {
            quadClone.stepScale = stepScale.clone();
        }
        if (offset != null) {
            quadClone.offset = offset.clone();
        }
        quadClone.offsetAmount = offsetAmount;
        quadClone.quadrant = quadrant;
        //quadClone.lodCalculatorFactory = lodCalculatorFactory.clone();
        //quadClone.lodCalculator = lodCalculator.clone();
        
        TerrainLodControl lodControlCloned = this.getControl(TerrainLodControl.class);
        TerrainLodControl lodControl = quadClone.getControl(TerrainLodControl.class);
        
        if (lodControlCloned != null && !(getParent() instanceof TerrainQuad)) {
            //lodControlCloned.setLodCalculator(lodControl.getLodCalculator().clone());
        }
        NormalRecalcControl normalControl = getControl(NormalRecalcControl.class);
        if (normalControl != null)
            normalControl.setTerrain(this);

        return quadClone;
    }
        
    @Override
    protected void setParent(Node parent) {
        super.setParent(parent);
        if (parent == null) {
            // if the terrain is being detached
            TerrainTransform.clearCaches(this);
        }
    }
    

    public float[] getHeightMap() {
    	return TerrainHeight.getHeightMap(this);
    }

}

