package com.jme3.terrain.geomipmap;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.material.Material;
import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.lodcalc.LodCalculator;
import com.jme3.terrain.geomipmap.TerrainQuad.LocationHeight;
import com.jme3.terrain.ProgressMonitor;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

interface TerrainNode {
    public void cacheTerrainTransforms();
    public void generateEntropy(ProgressMonitor progressMonitor);
    public Material getMaterial(Vector3f worldLocation);
    public boolean calculateLod(List<Vector3f> location, HashMap<String,UpdatedTerrainPatch> updates, LodCalculator lodCalculator);
    public void resetCachedNeighbours();
    public void reIndexPages(HashMap<String,UpdatedTerrainPatch> updated, boolean usesVariableLod);
    public short getQuadrant();
    public float getHeightmapHeight(int x, int z);
    public Vector3f getMeshNormal(int x, int z);
    public float getHeight(int x, int z, float xm, float zm);
    public void setHeight(List<LocationHeight> locationHeights, boolean overrideHeight);
    public void fixNormals(BoundingBox affectedArea);
    public BoundingVolume getWorldBound();
    public void setLocked(boolean locked);
    public void getAllTerrainPatches(List<TerrainPatch> holder);
    public void clearCaches();
    public void getAllTerrainPatchesWithTranslation(Map<TerrainPatch,Vector3f> holder, Vector3f translation);
}
