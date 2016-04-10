package com.jme3.terrain.geomipmap;

import com.jme3.scene.VertexBuffer.Type;

public final class Utils {
	
    public static int findQuadrant(int x, int y, int size) {
        int split = (size + 1) >> 1;
        int quads = 0;
        if (x < split && y < split)
            quads |= 1;
        if (x < split && y >= split - 1)
            quads |= 2;
        if (x >= split - 1 && y < split)
            quads |= 4;
        if (x >= split - 1 && y >= split - 1)
            quads |= 8;
        return quads;
    }
    
    /**
     * Create just a flat heightmap
     */
    public static float[] generateDefaultHeightMap(int size) {
        float[] heightMap = new float[size*size];
        return heightMap;
    }
    
    /**
     * is the 2d point inside the terrain?
     * @param x local coordinate
     * @param z local coordinate
     * @return 
     */
    public static boolean isInside(int x, int z, int totalSize) {
        if (x < 0 || z < 0 || x > totalSize || z > totalSize)
            return false;
        return true;
    }
    
    protected boolean isPointOnTerrain(int x, int z, int totalSize) {
        return (x >= 0 && x <= totalSize && z >= 0 && z <= totalSize);
    }
    
    public float getHeightmapHeight(float x, float z, TerrainPatch tp) {
        if (x < 0 || z < 0 || x >= tp.getSize()|| z >= tp.getSize())
            return 0;
        int idx = (int) (z * tp.getSize()+ x);
        return tp.getMesh().getFloatBuffer(Type.Position).get(idx*3+1); // 3 floats per entry (x,y,z), the +1 is to get the Y
    }
    
    


}
