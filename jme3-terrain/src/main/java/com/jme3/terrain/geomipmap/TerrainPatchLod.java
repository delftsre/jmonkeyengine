package com.jme3.terrain.geomipmap;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.terrain.geomipmap.lodcalc.util.EntropyComputeUtil;

public class TerrainPatchLod {
    protected LODGeomap geomap;
    protected int lod = 0; // this terrain patch's LOD
    private int maxLod = -1;
    protected int previousLod = -1;
    protected int lodLeft, lodTop, lodRight, lodBottom; // it's neighbour's LODs
    protected float[] lodEntropy;
    
    protected int size, totalSize;
    private Mesh mesh;
    protected float[] heightMap;
    
    public TerrainPatchLod(int size, int totalSize, float[] heightMap, Vector3f stepScale, Vector2f offset, float offsetAmount) {
    	this.size = size;
    	this.heightMap = heightMap;
    	this.geomap = new LODGeomap(size, heightMap);
    	this.mesh = geomap.createMesh(stepScale, new Vector2f(1,1), offset, offsetAmount, totalSize, false);
    }
    
    /**
     * This calculation is slow, so don't use it often.
     */
    public void generateLodEntropies() {
        float[] entropies = new float[getMaxLod()+1];
        for (int i = 0; i <= getMaxLod(); i++){
            int curLod = (int) Math.pow(2, i);
            IndexBuffer idxB = geomap.writeIndexArrayLodDiff(curLod, false, false, false, false, totalSize);
            Buffer ib;
            if (idxB.getBuffer() instanceof IntBuffer)
                ib = (IntBuffer)idxB.getBuffer();
            else
                ib = (ShortBuffer)idxB.getBuffer();
            entropies[i] = EntropyComputeUtil.computeLodEntropy(mesh, ib);
        }

        lodEntropy = entropies;
    }

    public float[] getLodEntropies(){
        if (lodEntropy == null){
            generateLodEntropies();
        }
        return lodEntropy;
    }
    
    /**
     * The maximum lod supported by this terrain patch.
     * If the patch size is 32 then the returned value would be log2(32)-2 = 3
     * You can then use that value, 3, to see how many times you can divide 32 by 2
     * before the terrain gets too un-detailed (can't stitch it any further).
     * @return the maximum LOD
     */
    public int getMaxLod() {
        if (maxLod < 0)
            maxLod = Math.max(1, (int) (FastMath.log(size-1)/FastMath.log(2)) -1); // -1 forces our minimum of 4 triangles wide

        return maxLod;
    }
    
    public void setMaxLod(int maxLod) {
    	this.maxLod = maxLod;
    }
    
    public int getLod() {
        return lod;
    }

    public void setLod(int lod) {
        this.lod = lod;
    }

    public int getPreviousLod() {
        return previousLod;
    }

    public void setPreviousLod(int previousLod) {
        this.previousLod = previousLod;
    }

    protected int getLodLeft() {
        return lodLeft;
    }

    protected void setLodLeft(int lodLeft) {
        this.lodLeft = lodLeft;
    }

    protected int getLodTop() {
        return lodTop;
    }

    protected void setLodTop(int lodTop) {
        this.lodTop = lodTop;
    }

    protected int getLodRight() {
        return lodRight;
    }

    protected void setLodRight(int lodRight) {
        this.lodRight = lodRight;
    }

    protected int getLodBottom() {
        return lodBottom;
    }

    protected void setLodBottom(int lodBottom) {
        this.lodBottom = lodBottom;
    }
    
    public Mesh getMesh() {
    	return this.mesh;
    }
    
    public LODGeomap getLODGeomap() {
    	return this.geomap;
    }
    
    public void setLodEntropies(float[] entropies) {
    	this.lodEntropy = entropies;
	}
    
    public void setLODGeomap(LODGeomap geomap) {
    	this.geomap = geomap;
    }
 }
