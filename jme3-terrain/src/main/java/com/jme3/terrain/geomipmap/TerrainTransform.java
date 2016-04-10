package com.jme3.terrain.geomipmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.terrain.ProgressMonitor;
import com.jme3.terrain.geomipmap.lodcalc.LodCalculator;
import com.jme3.terrain.geomipmap.picking.BresenhamTerrainPicker;
import com.jme3.terrain.geomipmap.picking.TerrainPickData;
import com.jme3.util.TangentBinormalGenerator;

public class TerrainTransform {
	
    /**
     * Caches the transforms (except rotation) so the LOD calculator,
     * which runs on a separate thread, can access them safely.
     */
	
    public static int collideWithRay(Ray ray, CollisionResults results, TerrainQuad terrainQuad) {
        if (terrainQuad.getPicker() == null)
            terrainQuad.setPicker(new BresenhamTerrainPicker(terrainQuad));

        Vector3f intersection = terrainQuad.getPicker().getTerrainIntersection(ray, results);
        if (intersection != null) {
            if (ray.getLimit() < Float.POSITIVE_INFINITY) {
                if (results.getClosestCollision().getDistance() <= ray.getLimit())
                    return 1; // in range
                else
                    return 0; // out of range
            } else
                return 1;
        } else
            return 0;
    }

    /**
     * Caches the transforms (except rotation) so the LOD calculator,
     * which runs on a separate thread, can access them safely.
     */
	
    public static void cacheTerrainTransforms(TerrainQuad tq) {
        for (int i = tq.getChildren().size(); --i >= 0;) {
            Spatial child = tq.getChildren().get(i);
            if (child instanceof TerrainQuad) {
                cacheTerrainTransforms((TerrainQuad) child);
            } else if (child instanceof TerrainPatch) {
                ((TerrainPatch) child).cacheTerrainTransforms();
            }
        }
    }
    
    /**
     * Generate the entropy values for the terrain for the "perspective" LOD
     * calculator. This routine can take a long time to run!
     * @param progressMonitor optional
     */
    
    public static void generateEntropy(ProgressMonitor progressMonitor, TerrainQuad terrainQuad) {
        // only check this on the root quad
        if (terrainQuad.isRootQuad())
            if (progressMonitor != null) {
                int numCalc = (terrainQuad.getTotalSize()-1)/(terrainQuad.getPatchSize()-1); // make it an even number
                progressMonitor.setMonitorMax(numCalc*numCalc);
            }

        if (terrainQuad.getChildren() != null) {
            for (int i = terrainQuad.getChildren().size(); --i >= 0;) {
                Spatial child = terrainQuad.getChildren().get(i);
                if (child instanceof TerrainQuad) {
                        generateEntropy(progressMonitor, (TerrainQuad) child);
                } else if (child instanceof TerrainPatch) {
                    ((TerrainPatch) child).generateLodEntropies();
                    if (progressMonitor != null)
                        progressMonitor.incrementProgress(1);
                }
            }
        }

        // only do this on the root quad
        if (terrainQuad.isRootQuad())
            if (progressMonitor != null)
                progressMonitor.progressComplete();
    }
    
    
    protected static boolean calculateLod(List<Vector3f> location, HashMap<String,UpdatedTerrainPatch> updates, LodCalculator lodCalculator, TerrainQuad terrainQuad) {

        boolean lodChanged = false;

        if (terrainQuad.getChildren() != null) {
            for (int i = terrainQuad.getChildren().size(); --i >= 0;) {
                Spatial child = terrainQuad.getChildren().get(i);
                if (child instanceof TerrainQuad) {
                    boolean b = calculateLod(location, updates, lodCalculator, ((TerrainQuad) child));
                    if (b)
                        lodChanged = true;
                } else if (child instanceof TerrainPatch) {
                    boolean b = lodCalculator.calculateLod((TerrainPatch) child, location, updates);
                    if (b)
                        lodChanged = true;
                }
            }
        }

        return lodChanged;
    }
    
    /**
     * Reset the cached references of neighbours.
     * TerrainQuad caches neighbours for faster LOD checks.
     * Sometimes you might want to reset this cache (for instance in TerrainGrid)
     */
    public static void resetCachedNeighbours(TerrainQuad terrainQuad) {
        if (terrainQuad.getChildren() != null) {
            for (int x = terrainQuad.getChildren().size(); --x >= 0;) {
                Spatial child = terrainQuad.getChildren().get(x);
                if (child instanceof TerrainQuad) {
                    resetCachedNeighbours(((TerrainQuad) child));
                } else if (child instanceof TerrainPatch) {
                    TerrainPatch patch = (TerrainPatch) child;
                    patch.searchedForNeighboursAlready = false;
                }
            }
        }
    }
    
    protected synchronized static void reIndexPages(HashMap<String,UpdatedTerrainPatch> updated, boolean usesVariableLod, TerrainQuad tq) {
        if (tq.getChildren() != null) {
            for (int i = tq.getChildren().size(); --i >= 0;) {
                Spatial child = tq.getChildren().get(i);
                if (child instanceof TerrainQuad) {
                    reIndexPages(updated, usesVariableLod, (TerrainQuad) child);
                } else if (child instanceof TerrainPatch) {
                    ((TerrainPatch) child).reIndexGeometry(updated, usesVariableLod);
                }
            }
        }
    }
    
    public void generateDebugTangents(Material mat, TerrainQuad tq) {
        for (int x = tq.getChildren().size(); --x >= 0;) {
            Spatial child = tq.getChildren().get(x);
            if (child instanceof TerrainQuad) {
                generateDebugTangents(mat, (TerrainQuad)child);
            } else if (child instanceof TerrainPatch) {
                Geometry debug = new Geometry( "Debug " + tq.getName(),
                    TangentBinormalGenerator.genTbnLines( ((TerrainPatch)child).getMesh(), 0.8f));
                tq.attachChild(debug);
                debug.setLocalTranslation(child.getLocalTranslation());
                debug.setCullHint(CullHint.Never);
                debug.setMaterial(mat);
            }
        }
    }
    
    /**
     * Retrieve all Terrain Patches from all children and store them
     * in the 'holder' list
     * @param holder must not be null, will be populated when returns
     */
    public static void getAllTerrainPatches(List<TerrainPatch> holder, TerrainQuad tq) {
        if (tq.getChildren() != null) {
            for (int i = tq.getChildren().size(); --i >= 0;) {
                Spatial child = tq.getChildren().get(i);
                if (child instanceof TerrainQuad) {
                    getAllTerrainPatches(holder, (TerrainQuad) child);
                } else if (child instanceof TerrainPatch) {
                    holder.add((TerrainPatch)child);
                }
            }
        }
    }

    public static void getAllTerrainPatchesWithTranslation(Map<TerrainPatch,Vector3f> holder, Vector3f translation, TerrainQuad tq) {
        if (tq.getChildren() != null) {
            for (int i = tq.getChildren().size(); --i >= 0;) {
                Spatial child = tq.getChildren().get(i);
                if (child instanceof TerrainQuad) {
                    getAllTerrainPatchesWithTranslation(holder, translation.clone().add(child.getLocalTranslation()), (TerrainQuad) child);
                } else if (child instanceof TerrainPatch) {
                    //if (holder.size() < 4)
                    holder.put((TerrainPatch)child, translation.clone().add(child.getLocalTranslation()));
                }
            }
        }
    }
    
    /**
     * Gather the terrain patches that intersect the given ray (toTest).
     * This only tests the bounding boxes
     * @param toTest
     * @param results
     */
    public static void findPick(Ray toTest, List<TerrainPickData> results, TerrainQuad tq) {

        if (tq.getWorldBound() != null) {
            if (tq.getWorldBound().intersects(toTest)) {
                // further checking needed.
                for (int i = 0; i < tq.getQuantity(); i++) {
                    if (tq.getChildren().get(i) instanceof TerrainPatch) {
                        TerrainPatch tp = (TerrainPatch) tq.getChildren().get(i);
                        tp.ensurePositiveVolumeBBox();
                        if (tp.getWorldBound().intersects(toTest)) {
                            CollisionResults cr = new CollisionResults();
                            toTest.collideWith(tp.getWorldBound(), cr);
                            if (cr != null && cr.getClosestCollision() != null) {
                                cr.getClosestCollision().getDistance();
                                results.add(new TerrainPickData(tp, cr.getClosestCollision()));
                            }
                        }
                    }
                    else if (tq.getChildren().get(i) instanceof TerrainQuad) {
                    	findPick(toTest, results, (TerrainQuad) tq.getChildren().get(i));
                    }
                }
            }
        }
    }

    /**
     * Removes any cached references this terrain is holding, in particular
     * the TerrainPatch's neighbour references.
     * This is called automatically when the root terrainQuad is detached from
     * its parent or if setParent(null) is called.
     */
    public static void clearCaches(TerrainQuad tq) {
        if (tq.getChildren() != null) {
            for (int i = tq.getChildren().size(); --i >= 0;) {
                Spatial child = tq.getChildren().get(i);
                if (child instanceof TerrainQuad) {
                    clearCaches((TerrainQuad) child);
                } else if (child instanceof TerrainPatch) {
                    ((TerrainPatch) child).clearCaches();
                }
            }
        }
    }
    

}
