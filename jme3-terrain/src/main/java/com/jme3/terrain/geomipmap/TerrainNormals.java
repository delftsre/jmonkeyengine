package com.jme3.terrain.geomipmap;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class TerrainNormals {
	
    protected static void updateNormals(TerrainQuad tq) {

        if (needToRecalculateNormals(tq)) {
            //TODO background-thread this if it ends up being expensive
            fixNormals(tq.getAffectedAreaBBox(), tq); // the affected patches
            TerrainQuadrants.fixNormalEdges(tq.getAffectedAreaBBox(), tq); // the edges between the patches
            
            setNormalRecalcNeeded(null, tq); // set to false
        }
    }
    

    /**
     * Find what terrain patches need normal recalculations and update
     * their normals;
     */
    
    protected static void fixNormals(BoundingBox affectedArea, TerrainQuad tq) {
        if (tq.getChildren() == null)
            return;

        // go through the children and see if they collide with the affectedAreaBBox
        // if they do, then update their normals
        for (int x = tq.getChildren().size(); --x >= 0;) {
            Spatial child = tq.getChildren() .get(x);
            if (child instanceof TerrainQuad) {
                if (affectedArea != null && affectedArea.intersects(((TerrainQuad) child).getWorldBound()) )
                    fixNormals(affectedArea, (TerrainQuad) child);
            } else if (child instanceof TerrainPatch) {
                if (affectedArea != null && affectedArea.intersects(((TerrainPatch) child).getWorldBound()) )
                    TerrainPatchNormals.updateNormals((TerrainPatch) child); // recalculate the patch's normals
            }
        }
    }
    
    /**
     * Signal if the normal vectors for the terrain need to be recalculated.
     * Does this by looking at the affectedAreaBBox bounding box. If the bbox
     * exists already, then it will grow the box to fit the new changedPoint.
     * If the affectedAreaBBox is null, then it will create one of unit size.
     *
     * @param needToRecalculateNormals if null, will cause needToRecalculateNormals() to return false
     */
    
    protected static void setNormalRecalcNeeded(Vector2f changedPoint, TerrainQuad tq) {
        if (changedPoint == null) { // set needToRecalculateNormals() to false
            tq.setAffectedAreaBBox(null);
            return;
        }

        if (tq.getAffectedAreaBBox() == null) {
            tq.setAffectedAreaBBox(new BoundingBox(new Vector3f(changedPoint.x, 0, changedPoint.y), 1f, Float.MAX_VALUE, 1f)); // unit length
        } else {
            // adjust size of box to be larger
        	tq.getAffectedAreaBBox().mergeLocal(new BoundingBox(new Vector3f(changedPoint.x, 0, changedPoint.y), 1f, Float.MAX_VALUE, 1f));
        }
    }
    
    protected static boolean needToRecalculateNormals(TerrainQuad tq) {
        if (tq.getAffectedAreaBBox() != null)
            return true;
        if (!tq.getLastScale().equals(tq.getWorldScale())) {
            tq.setAffectedAreaBBox(new BoundingBox(tq.getWorldTranslation(), Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE));
            tq.setLastScale(tq.getWorldScale());
            return true;
        }
        return false;
    }
    
    // -----
    

    public static Vector3f getNormal(Vector2f xz, TerrainQuad tq) {
        // offset
        float x = (float)(((xz.x - tq.getWorldTranslation().x) / tq.getWorldScale().x) + (float)(tq.getTotalSize()-1) / 2f);
        float z = (float)(((xz.y - tq.getWorldTranslation().z) / tq.getWorldScale().z) + (float)(tq.getTotalSize()-1) / 2f);
        Vector3f normal = getNormal(x, z, xz, tq);
        
        return normal;
    }
    
    protected static Vector3f getNormal(float x, float z, Vector2f xz, TerrainQuad tq) {
        x-=0.5f;
        z-=0.5f;
        float col = FastMath.floor(x);
        float row = FastMath.floor(z);
        boolean onX = false;
        if(1 - (x - col)-(z - row) < 0) // what triangle to interpolate on
            onX = true;
        // v1--v2  ^
        // |  / |  |
        // | /  |  |
        // v3--v4  | Z
        //         |
        // <-------Y
        //     X 
        Vector3f n1 = getMeshNormal((int) FastMath.ceil(x), (int) FastMath.ceil(z), tq);
        Vector3f n2 = getMeshNormal((int) FastMath.floor(x), (int) FastMath.ceil(z), tq);
        Vector3f n3 = getMeshNormal((int) FastMath.ceil(x), (int) FastMath.floor(z), tq);
        Vector3f n4 = getMeshNormal((int) FastMath.floor(x), (int) FastMath.floor(z), tq);
        
        return n1.add(n2).add(n3).add(n4).normalize();
    }
    
    protected static Vector3f getMeshNormal(int x, int z, TerrainQuad tq) {
    	System.out.println("running 2");
    	QuadPoint quad = new QuadPoint(x, z, tq.getChildren(), tq.getSize());
    	return quad.calculateMeshNormal(x,z);
    }
    

}
