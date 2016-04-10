package com.jme3.terrain.geomipmap;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.jme3.math.Vector2f;
import com.jme3.scene.Spatial;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.terrain.geomipmap.TerrainQuad.LocationHeight;
import com.jme3.util.SafeArrayList;

public  class TerrainHeight {
	
    /**
     * This will just get the heightmap value at the supplied point,
     * not an interpolated (actual) height value.
     */   
    
    protected static float getHeightmapHeight(int x, int z, TerrainQuad tq) {
    	QuadPoint quad = new QuadPoint(x, z, tq.getChildren(), tq.getSize());
    	return quad.calculateHeightMap(x,z);
    }

    public static void adjustHeight(Vector2f xz, float delta, TerrainQuad tq) {
        List<Vector2f> coord = new ArrayList<Vector2f>();
        coord.add(xz);
        List<Float> h = new ArrayList<Float>();
        h.add(delta);

        tq.adjustHeight(coord, h);
    }
    

    /**
     * Used for searching for a child and keeping
     * track of its quadrant
     */

    private static QuadrantChild findMatchingChild(int x, int z, SafeArrayList<Spatial> children, int size) {
    	QuadPoint quad = new QuadPoint(x, z, children, size);
    	return quad.calculateQuadrant(x,z);
    }
    
    
    
    /*
     * gets an interpolated value at the specified point
     */
    protected static float getHeight(int x, int z, float xm, float zm, TerrainQuad tq) {
        
        QuadrantChild match = findMatchingChild(x,z, tq.getChildren(), tq.getSize());
        if (match != null) {
            if (match.child instanceof TerrainQuad) {
                return getHeight(match.col, match.row, xm, zm, (TerrainQuad) match.child);
            } else if (match.child instanceof TerrainPatch) {
                return ((TerrainPatch) match.child).getHeight(match.col, match.row, xm, zm);
            }
        }
        return Float.NaN;
    }
    
    public static float getHeightmapHeight(Vector2f xz, TerrainQuad tq) {
        // offset
        int halfSize = tq.getTotalSize() / 2;
        int x = Math.round((xz.x / tq.getWorldScale().x) + halfSize);
        int z = Math.round((xz.y / tq.getWorldScale().z) + halfSize);

        if (!Utils.isInside(x, z, tq.getTotalSize()))
            return Float.NaN;
        return tq.getHeightmapHeight(x, z);
    }
    
    protected static void setHeight(List<Vector2f> xz, List<Float> height, boolean overrideHeight, TerrainQuad tq) {
        if (xz.size() != height.size())
            throw new IllegalArgumentException("Both lists must be the same length!");

        int halfSize = tq.getTotalSize() / 2;

        List<LocationHeight> locations = new ArrayList<LocationHeight>();

        // offset
        for (int i=0; i<xz.size(); i++) {
            int x = Math.round((xz.get(i).x / tq.getWorldScale().x) + halfSize);
            int z = Math.round((xz.get(i).y / tq.getWorldScale().z) + halfSize);
            if (!Utils.isInside(x, z, tq.getTotalSize()))
                continue;
            locations.add(new LocationHeight(x,z,height.get(i)));
        }

        tq.setHeight(locations, overrideHeight); // adjust height of the actual mesh

        // signal that the normals need updating
        for (int i=0; i<xz.size(); i++)
            TerrainNormals.setNormalRecalcNeeded(xz.get(i), tq);
    }  
   
    protected static void setHeight(List<LocationHeight> locations, boolean overrideHeight, SafeArrayList<Spatial> children, int size) {
        if (children == null)
            return;

        List<LocationHeight> quadLH1 = new ArrayList<LocationHeight>();
        List<LocationHeight> quadLH2 = new ArrayList<LocationHeight>();
        List<LocationHeight> quadLH3 = new ArrayList<LocationHeight>();
        List<LocationHeight> quadLH4 = new ArrayList<LocationHeight>();
        Spatial quad1 = null;
        Spatial quad2 = null;
        Spatial quad3 = null;
        Spatial quad4 = null;

        // get the child quadrants
        for (int i = children.size(); --i >= 0;) {
            Spatial spat = children.get(i);
            int childQuadrant = 0;
            if (spat instanceof TerrainQuad) {
                childQuadrant = ((TerrainQuad) spat).getQuadrant();
            } else if (spat instanceof TerrainPatch) {
                childQuadrant = ((TerrainPatch) spat).getQuadrant();
            }

            if (childQuadrant == 1)
                quad1 = spat;
            else if (childQuadrant == 2)
                quad2 = spat;
            else if (childQuadrant == 3)
                quad3 = spat;
            else if (childQuadrant == 4)
                quad4 = spat;
        }

        int split = (size + 1) >> 1;

        // distribute each locationHeight into the quadrant it intersects
        for (LocationHeight lh : locations) {
            int quad = Utils.findQuadrant(lh.x, lh.z, size);
            int col = lh.x;
            int row = lh.z;

            if ((quad & 1) != 0) {
                quadLH1.add(lh);
            }
            if ((quad & 2) != 0) {
                row = lh.z - split + 1;
                quadLH2.add(new LocationHeight(lh.x, row, lh.h));
            }
            if ((quad & 4) != 0) {
                col = lh.x - split + 1;
                quadLH3.add(new LocationHeight(col, lh.z, lh.h));
            }
            if ((quad & 8) != 0) {
                col = lh.x - split + 1;
                row = lh.z - split + 1;
                quadLH4.add(new LocationHeight(col, row, lh.h));
            }
        }

        // send the locations to the children
        if (!quadLH1.isEmpty()) {
            if (quad1 instanceof TerrainQuad)
                ((TerrainQuad)quad1).setHeight(quadLH1, overrideHeight);
            else if(quad1 instanceof TerrainPatch)
                setHeight(quadLH1, overrideHeight, (TerrainPatch)quad1);
        }

        if (!quadLH2.isEmpty()) {
            if (quad2 instanceof TerrainQuad)
                ((TerrainQuad)quad2).setHeight(quadLH2, overrideHeight);
            else if(quad2 instanceof TerrainPatch)
                setHeight(quadLH2, overrideHeight, (TerrainPatch)quad2);
        }

        if (!quadLH3.isEmpty()) {
            if (quad3 instanceof TerrainQuad)
                ((TerrainQuad)quad3).setHeight(quadLH3, overrideHeight);
            else if(quad3 instanceof TerrainPatch)
                setHeight(quadLH3, overrideHeight, (TerrainPatch)quad3);
        }

        if (!quadLH4.isEmpty()) {
            if (quad4 instanceof TerrainQuad)
                ((TerrainQuad)quad4).setHeight(quadLH4, overrideHeight);
            else if(quad4 instanceof TerrainPatch)
                setHeight(quadLH4, overrideHeight, (TerrainPatch)quad4);
        }
    }
    
    public static float[] getHeightMap(TerrainQuad tq) {

        float[] hm = null;
        int length = ((tq.getSize()-1)/2)+1;
        int area = tq.getSize()*tq.getSize();
        hm = new float[area];

        if (tq.getChildren() != null && !tq.getChildren().isEmpty()) {
            float[] ul=null, ur=null, bl=null, br=null;
            // get the child heightmaps
            if (tq.getChild(0) instanceof TerrainPatch) {
                for (Spatial s : tq.getChildren()) {
                    if ( ((TerrainPatch)s).getQuadrant() == 1)
                        ul = ((TerrainPatch)s).getHeightMap();
                    else if(((TerrainPatch) s).getQuadrant() == 2)
                        bl = ((TerrainPatch)s).getHeightMap();
                    else if(((TerrainPatch) s).getQuadrant() == 3)
                        ur = ((TerrainPatch)s).getHeightMap();
                    else if(((TerrainPatch) s).getQuadrant() == 4)
                        br = ((TerrainPatch)s).getHeightMap();
                }
            }
            else {
                ul = TerrainQuadrants.getQuad(1, tq).getHeightMap();
                bl = TerrainQuadrants.getQuad(2, tq).getHeightMap();
                ur = TerrainQuadrants.getQuad(3, tq).getHeightMap();
                br = TerrainQuadrants.getQuad(4, tq).getHeightMap();
            }

            // combine them into a single heightmap


            // first upper blocks
            for (int y=0; y<length; y++) { // rows
                for (int x1=0; x1<length; x1++) {
                    int row = y*tq.getSize();
                    hm[row+x1] = ul[y*length+x1];
                }
                for (int x2=1; x2<length; x2++) {
                    int row = y*tq.getSize() + length;
                    hm[row+x2-1] = ur[y*length + x2];
                }
            }
            // second lower blocks
            int rowOffset = tq.getSize()*length;
            for (int y=1; y<length; y++) { // rows
                for (int x1=0; x1<length; x1++) {
                    int row = (y-1)*tq.getSize();
                    hm[rowOffset+row+x1] = bl[y*length+x1];
                }
                for (int x2=1; x2<length; x2++) {
                    int row = (y-1)*tq.getSize() + length;
                    hm[rowOffset+row+x2-1] = br[y*length + x2];
                }
            }
        }

        return hm;
    }
    
    protected static void setHeight(List<LocationHeight> locationHeights, boolean overrideHeight, TerrainPatch tp) {
        
        for (LocationHeight lh : locationHeights) {
            if (lh.x < 0 || lh.z < 0 || lh.x >= tp.getSize()|| lh.z >= tp.getSize())
                continue;
            int idx = lh.z * tp.getSize()+ lh.x;
            if (overrideHeight) {
            	tp.getTpLod().getLODGeomap().getHeightArray()[idx] = lh.h;
            } else {
                float h = tp.getMesh().getFloatBuffer(Type.Position).get(idx*3+1);
                tp.getTpLod().getLODGeomap().getHeightArray()[idx] = h+lh.h;
            }
            
        }

        FloatBuffer newVertexBuffer = tp.getTpLod().getLODGeomap().writeVertexArray(null, tp.getStepScale(), false);
        tp.getMesh().clearBuffer(Type.Position);
        tp.getMesh().setBuffer(Type.Position, 3, newVertexBuffer);
    }
  
}
