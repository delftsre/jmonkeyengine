package com.jme3.terrain.geomipmap;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.CollisionResults;
import com.jme3.math.Ray;
import com.jme3.math.Triangle;
import com.jme3.math.Vector3f;

public class TerrainPatchCollision {

    protected static int collideWithRay(Ray ray, CollisionResults results) {
        // This should be handled in the root terrain quad
        return 0;
    }

    protected static int collideWithBoundingVolume(BoundingVolume boundingVolume, CollisionResults results, TerrainPatch tp) {
        if (boundingVolume instanceof BoundingBox)
            return collideWithBoundingBox((BoundingBox)boundingVolume, results, tp);
        else if(boundingVolume instanceof BoundingSphere) {
            BoundingSphere sphere = (BoundingSphere) boundingVolume;
            BoundingBox bbox = new BoundingBox(boundingVolume.getCenter().clone(), sphere.getRadius(),
                                                           sphere.getRadius(),
                                                           sphere.getRadius());
            return collideWithBoundingBox(bbox, results, tp);
        }
        return 0;
    }

    protected static Vector3f worldCoordinateToLocal(Vector3f loc, TerrainPatch tp) {
        Vector3f translated = new Vector3f();
        translated.x = loc.x/tp.getWorldScale().x - tp.getWorldTranslation().x;
        translated.y = loc.y/tp.getWorldScale().y - tp.getWorldTranslation().y;
        translated.z = loc.z/tp.getWorldScale().z - tp.getWorldTranslation().z;
        return translated;
    }

    /**
     * This most definitely is not optimized.
     */
    private static int collideWithBoundingBox(BoundingBox bbox, CollisionResults results, TerrainPatch tp) {
        
        // test the four corners, for cases where the bbox dimensions are less than the terrain grid size, which is probably most of the time
        Vector3f topLeft = worldCoordinateToLocal(new Vector3f(bbox.getCenter().x-bbox.getXExtent(), 0, bbox.getCenter().z-bbox.getZExtent()), tp);
        Vector3f topRight = worldCoordinateToLocal(new Vector3f(bbox.getCenter().x+bbox.getXExtent(), 0, bbox.getCenter().z-bbox.getZExtent()), tp);
        Vector3f bottomLeft = worldCoordinateToLocal(new Vector3f(bbox.getCenter().x-bbox.getXExtent(), 0, bbox.getCenter().z+bbox.getZExtent()), tp);
        Vector3f bottomRight = worldCoordinateToLocal(new Vector3f(bbox.getCenter().x+bbox.getXExtent(), 0, bbox.getCenter().z+bbox.getZExtent()), tp);

        Triangle t = tp.getTriangle(topLeft.x, topLeft.z);
        if (t != null && bbox.collideWith(t, results) > 0)
            return 1;
        t = tp.getTriangle(topRight.x, topRight.z);
        if (t != null && bbox.collideWith(t, results) > 0)
            return 1;
        t = tp.getTriangle(bottomLeft.x, bottomLeft.z);
        if (t != null && bbox.collideWith(t, results) > 0)
            return 1;
        t = tp.getTriangle(bottomRight.x, bottomRight.z);
        if (t != null && bbox.collideWith(t, results) > 0)
            return 1;
        
        // box is larger than the points on the terrain, so test against the points
        for (float z=topLeft.z; z<bottomLeft.z; z+=1) {
            for (float x=topLeft.x; x<topRight.x; x+=1) {
                
                if (x < 0 || z < 0 || x >= tp.getSize()|| z >= tp.getSize())
                    continue;
                t = tp.getTriangle(x,z);
                if (t != null && bbox.collideWith(t, results) > 0)
                    return 1;
            }
        }

        return 0;
    }
}
