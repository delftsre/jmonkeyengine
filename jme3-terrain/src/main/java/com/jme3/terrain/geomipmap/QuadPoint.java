package com.jme3.terrain.geomipmap;

import com.jme3.math.Vector;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.util.SafeArrayList;

public class QuadPoint {
	
	private int quad, split, col, row;
	private SafeArrayList<Spatial> children;
	
	public QuadPoint(int x, int z, SafeArrayList<Spatial> child, int size) {

		this.children = child;
		quad = Utils.findQuadrant(x, z, size);
		split = (size + 1) >> 1;
	}
	    
    
    private boolean calculateColRow(int i, int x, int z, Spatial spat ) {
        int col = x;
        int row = z;
        boolean match = false;

        // get the childs quadrant
        int childQuadrant = 0;
        if (spat instanceof TerrainQuad) {
            childQuadrant = ((TerrainQuad) spat).getQuadrant();
        } else if (spat instanceof TerrainPatch) {
            childQuadrant = ((TerrainPatch) spat).getQuadrant();
        }

        if (childQuadrant == 1 && (quad & 1) != 0) {
            match = true;
        } else if (childQuadrant == 2 && (quad & 2) != 0) {
            row = z - split + 1;
            match = true;
        } else if (childQuadrant == 3 && (quad & 4) != 0) {
            col = x - split + 1;
            match = true;
        } else if (childQuadrant == 4 && (quad & 8) != 0) {
            col = x - split + 1;
            row = z - split + 1;
            match = true;
        }
        return match;
    }

    
    public QuadrantChild calculateQuadrant(int x, int z) {
        if (children != null) {
            for (int i = children.size(); --i >= 0;) {
                Spatial spat = children.get(i);
            	boolean match = calculateColRow(i, x, z, spat);
            	
                if (match)
                    return new QuadrantChild(col, row, spat);
            }
        }
        return null;
         
    }
    
    public float calculateHeightMap(int x, int z) {
        if (children != null) {
            for (int i = children.size(); --i >= 0;) {
            	Spatial spat = children.get(i);
            	boolean match = calculateColRow(i, x, z, spat);
            	
                if (match) {
                    if (spat instanceof TerrainQuad) {
                        return ((TerrainQuad) spat).getHeightmapHeight(col, row);
                    } else if (spat instanceof TerrainPatch) {
                        return ((TerrainPatch) spat).getHeightmapHeight(col, row);
                    }
                }

            }
        }
        return Float.NaN;
    }
    
    public Vector calculateMeshNormal(int x, int z) {
        if (children != null) {
            for (int i = children.size(); --i >= 0;) {
            	Spatial spat = children.get(i);
            	boolean match = calculateColRow(i, x, z, spat);

                if (match) {
                    if (spat instanceof TerrainQuad) {
                        return ((TerrainQuad) spat).getMeshNormal(col, row);
                    } else if (spat instanceof TerrainPatch) {
                        return ((TerrainPatch) spat).getMeshNormal(col, row);
                    }
                }

            }
        }
        return null;
    }
}
