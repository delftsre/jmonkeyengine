package com.jme3.terrain.geomipmap;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

public class TerrainCreatePatch {
	
    /**
     * <code>split</code> divides the heightmap data for four children. The
     * children are either quads or patches. This is dependent on the size of the
     * children. If the child's size is less than or equal to the set block
     * size, then patches are created, otherwise, quads are created.
     *
     * @param blockSize
     *			the blocks size to test against.
     * @param heightMap
     *			the height data.
     */
    public static void split(int blockSize, float[] heightMap, TerrainQuad tq) {
        if ((tq.getSize() >> 1) + 1 <= blockSize) {
            createQuadPatch(heightMap, tq);
        } else {
            createQuad(blockSize, heightMap, tq);
        }
    }
    
    protected static Vector2f createBasicOffset(Vector2f offset) {
    	Vector2f tempOffset = new Vector2f();
        tempOffset.x = offset.x;
        tempOffset.y = offset.y;
        return tempOffset;
    }
    
    protected static void addPatch(String name, int quadrant, int split, float[] heightBlock, Vector3f origin, Vector2f tempOffset, TerrainQuad tq) {
    	TerrainPatch patch = new TerrainPatch(tq.getName() + name, split,
                tq.getStepScale(), heightBlock, origin, tq.getTotalSize(), tempOffset,
                tq.getOffsetAmount());
    	patch.setQuadrant((short) quadrant);
    	tq.attachChild(patch);
    	patch.setModelBound(new BoundingBox());
    	patch.updateModelBound();
        //patch.setLodCalculator(lodCalculator);
        //TangentBinormalGenerator.generate(patch);
    }
    
    public static float[] createHeightSubBlock(float[] heightMap, int x,
        int y, int side) {
		float[] rVal = new float[side * side];
		int bsize = (int) FastMath.sqrt(heightMap.length);
		int count = 0;
		for (int i = y; i < side + y; i++) {
		    for (int j = x; j < side + x; j++) {
		        if (j < bsize && i < bsize)
		            rVal[count] = heightMap[j + (i * bsize)];
		       count++;
			  }
		}
		return rVal;
    }
    
    
    
    protected static void createQuadPatch(float[] heightMap, TerrainQuad tq) {
        // create 4 terrain patches
        int quarterSize = tq.getSize() >> 2;
        int halfSize = tq.getSize()>> 1;
        int split = (tq.getSize() + 1) >> 1;

        //if (lodCalculator == null)
        //    lodCalculator = createDefaultLodCalculator(); // set a default one

        
        tq.setOffsetAmount(tq.getOffsetAmount() + quarterSize);

        // 1 lower left
        float[] heightBlock1 = createHeightSubBlock(heightMap, 0, 0, split);

        Vector3f origin1 = new Vector3f(-halfSize * tq.getStepScale().x, 0, -halfSize
                        * tq.getStepScale().z);

        Vector2f tempOffset1 = createBasicOffset(tq.getOffSet());
        tempOffset1.x += origin1.x / 2;
        tempOffset1.y += origin1.z / 2;

        addPatch("Patch1", 1, split, heightBlock1, origin1, tempOffset1, tq);

        // 2 upper left
        float[] heightBlock2 = createHeightSubBlock(heightMap, 0, split - 1,
                        split);

        Vector3f origin2 = new Vector3f(-halfSize * tq.getStepScale().x, 0, 0);

        Vector2f tempOffset2 = createBasicOffset(tq.getOffSet());
        tempOffset2.x += origin1.x / 2;
        tempOffset2.y += quarterSize * tq.getStepScale().z;

        addPatch("Patch2", 2, split, heightBlock2, origin2, tempOffset2, tq);

        // 3 lower right
        float[] heightBlock3 = createHeightSubBlock(heightMap, split - 1, 0,
                        split);

        Vector3f origin3 = new Vector3f(0, 0, -halfSize * tq.getStepScale().z);

        Vector2f tempOffset3 = createBasicOffset(tq.getOffSet());
        tempOffset3.x += quarterSize * tq.getStepScale().x;
        tempOffset3.y += origin3.z / 2;

        addPatch("Patch3", 3, split, heightBlock3, origin3, tempOffset3, tq);

        // 4 upper right
        float[] heightBlock4 = createHeightSubBlock(heightMap, split - 1,
                        split - 1, split);

        Vector3f origin4 = new Vector3f(0, 0, 0);

        Vector2f tempOffset4 = createBasicOffset(tq.getOffSet());
        tempOffset4.x += quarterSize * tq.getStepScale().x;
        tempOffset4.y += quarterSize * tq.getStepScale().z;

        addPatch("Patch4", 4, split, heightBlock4, origin4, tempOffset4, tq);
    }
    
    /**
     * Quadrants, world coordinates, and heightmap coordinates (Y-up):
     * 
     *         -z
     *      -u | 
     *    -v  1|3 
     *  -x ----+---- x
     *        2|4 u
     *         | v
     *         z
     * <code>createQuad</code> generates four new quads from this quad.
     * The heightmap's top left (0,0) coordinate is at the bottom, -x,-z
     * coordinate of the terrain, so it grows in the positive x.z direction.
     */
    
    protected static Vector2f setDefaultOffset(Vector3f origin, Vector2f offset) {
    	Vector2f tempOffset = new Vector2f();
    	tempOffset.x = offset.x;
    	tempOffset.y = offset.y;
    	tempOffset.x += origin.x;
    	tempOffset.y += origin.z;
    	return tempOffset;
    }
    
    protected static  void attachQuad(float[] heightBlock, Vector3f origin, Vector2f tempOffset, int quaddrant, String name, int split, int blockSize, TerrainQuad tq) {	
    	TerrainQuad quad = new TerrainQuad(tq.getName() + name, blockSize,
                split, tq.getStepScale(), heightBlock, tq.getTotalSize(), tempOffset,
                tq.getOffsetAmount());
    	
    	quad.setLocalTranslation(origin);
        quad.quadrant = quaddrant;
        tq.attachChild(quad);
    }
    
    protected static void createQuad(int blockSize, float[] heightMap, TerrainQuad tq) {
        // create 4 terrain quads
        int quarterSize = tq.getSize() >> 2;

        int split = (tq.getSize()+ 1) >> 1;

        tq.setOffsetAmount(tq.getOffsetAmount() + quarterSize);

        //if (lodCalculator == null)
        //    lodCalculator = createDefaultLodCalculator(); // set a default one

        // 1 upper left of heightmap, upper left quad
        float[] heightBlock1 = createHeightSubBlock(heightMap, 0, 0, split);
    	Vector3f origin1 = new Vector3f(-quarterSize * tq.getStepScale().x, 0,
                -quarterSize * tq.getStepScale().z);
    	
        attachQuad(heightBlock1, origin1, setDefaultOffset(origin1, tq.getOffSet()), 1, "Quad1", split, blockSize, tq);

        // 2 lower left of heightmap, lower left quad
        float[] heightBlock2 = createHeightSubBlock(heightMap, 0, split - 1,
                        split);
        
    	Vector3f origin2 = new Vector3f(-quarterSize * tq.getStepScale().x, 0,
                quarterSize * tq.getStepScale().z);
    	
        
        attachQuad(heightBlock2, origin2, setDefaultOffset(origin2, tq.getOffSet()), 2, "Quad2", split, blockSize, tq);


        // 3 upper right of heightmap, upper right quad
        float[] heightBlock3 = createHeightSubBlock(heightMap, split - 1, 0,
                        split);

    	Vector3f origin3 = new Vector3f(quarterSize * tq.getStepScale().x, 0,
                -quarterSize * tq.getStepScale().z);
    	        
        attachQuad(heightBlock3, origin3, setDefaultOffset(origin3, tq.getOffSet()), 3, "Quad3", split, blockSize, tq);
        
        // 4 lower right of heightmap, lower right quad
        float[] heightBlock4 = createHeightSubBlock(heightMap, split - 1,
                        split - 1, split);

    	Vector3f origin4 = new Vector3f(quarterSize * tq.getStepScale().x, 0,
                quarterSize * tq.getStepScale().z);
        
        attachQuad(heightBlock4, origin4, setDefaultOffset(origin4, tq.getOffSet()), 4, "Quad4", split, blockSize, tq);

    }
    
    

}
