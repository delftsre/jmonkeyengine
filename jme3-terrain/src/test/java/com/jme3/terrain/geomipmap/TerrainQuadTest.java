package com.jme3.terrain.geomipmap;

import com.jme3.asset.AssetManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;
import com.jme3.terrain.geomipmap.lodcalc.LodCalculator;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.HillHeightMap;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.junit.Assert.*;

public class TerrainQuadTest {

    final int DIR_RIGHT = 0, DIR_DOWN = 1, DIR_LEFT = 2, DIR_TOP = 3;

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private AssetManager assetManager;
    private FakeTerrainQuad parentTerrainQuad;
    private TerrainQuad terrainQuad = new TerrainQuad();
    private FakeTerrainQuad[] children = new FakeTerrainQuad[4];
    private TerrainPatch[] tpChildren = new TerrainPatch[4];
    private LodCalculator lodCalculator = new DistanceLodCalculator();
    private LodCalculator fakeLodCalculator = new FakeDistanceLodCalculator();
    private List<Vector3f> location = new ArrayList<Vector3f>();
    private HashMap<String, UpdatedTerrainPatch> updates = new HashMap<String, UpdatedTerrainPatch>();
    private Vector3f v3f = new Vector3f();
    private Vector2f v2f = new Vector2f();
    private BoundingBox boundingBox = new BoundingBox();
    private float[] testHeightmap;


    @Before
    public void init() {
        for (int i = 0; i < 4; i++) {
            children[i] = new FakeTerrainQuad();
            tpChildren[i] = new TerrainPatch();
        }

        // HEIGHTMAP image (for the terrain heightmap)
        //Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/mountains512.png");
        // CREATE HEIGHTMAP
        AbstractHeightMap heightmap = null;
        try {
            heightmap = new HillHeightMap(5, 1000, 50, 100, (byte) 3);

            //heightmap = new ImageBasedHeightMap(heightMapImage.getImage(), 1f);
            heightmap.load();

        } catch (Exception e) {
            e.printStackTrace();
        }


        terrainQuad = new TerrainQuad("terrain_1", 3, 5, heightmap.getHeightMap());
        testHeightmap = heightmap.getHeightMap();

        parentTerrainQuad = new FakeTerrainQuad();
        fakeCreateQuad(parentTerrainQuad, children);
    }

    private void fakeCreateQuad(FakeTerrainQuad parent, FakeTerrainQuad[] children) {
        for (int i = 0; i < children.length; i++) {
            children[i].quadrant = i + 1; // Quadrant starts counting from 1
            parent.attachChild(children[i]);
        }
    }

    /**
     * Used to recursively create a nested structure of {@link Spatial}s.
     * If nesting level is > 1, root element will be a {@link TerrainQuad}.
     * Leafs (nesting level 0) are {@link TerrainPatch}es.
     *
     * @param nestLevel Nest level to be created.
     * @return Nested structure of {@link Spatial}s
     */
    private Spatial createNestedQuad(int nestLevel, String index) {
        if (nestLevel == 0) {
            TerrainPatch tp = new TerrainPatch();
            tp.setName(index);
            return tp;
        }

        FakeTerrainQuad parent = new FakeTerrainQuad();
        parent.setName(index);
        for (int i = 0; i < 4; i++) {
            Spatial child = createNestedQuad(nestLevel - 1, index + (i + 1));

            if (child instanceof TerrainPatch) {
                TerrainPatch patchChild = (TerrainPatch) child;
                patchChild.quadrant = (short) (i + 1);
                parent.attachChild(patchChild);
            } else if (child instanceof TerrainQuad) {
                FakeTerrainQuad quadChild = (FakeTerrainQuad) child;
                quadChild.quadrant = i + 1;
                parent.attachChild(quadChild);
            }
        }

        return parent;
    }

    @Test
    public void testFakeTerrainQuad() {
        FakeTerrainQuad fake = new FakeTerrainQuad();
        assertEquals(fake, fake.getQuad(0));
    }

    @Test
    public void testNestStructure() {
        Spatial leaf = createNestedQuad(0, "");
        assertTrue(leaf instanceof TerrainPatch);

        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(1, "");
        assertEquals(root.getChildren().size(), 4);
        for (int i = 0; i < 4; i++) {
            assertTrue(root.getChild(i) instanceof TerrainPatch); // Ensure children of root are leafs
        }

        root = (FakeTerrainQuad) createNestedQuad(2, "");
        assertEquals(root.getChildren().size(), 4);
        for (int i = 0; i < 4; i++) {
            assertTrue(root.getChild(i) instanceof TerrainQuad); // Ensure children of root are not leafs
        }
    }

    @Test
    public void testGetQuad() {
        assertEquals(parentTerrainQuad.getQuad(0), parentTerrainQuad);
        assertEquals(parentTerrainQuad.getQuad(1), children[0]);
        assertEquals(parentTerrainQuad.getQuad(2), children[1]);
        assertEquals(parentTerrainQuad.getQuad(3), children[2]);
        assertEquals(parentTerrainQuad.getQuad(4), children[3]);
        assertEquals(parentTerrainQuad.getQuad(5), null);
    }

    @Test
    public void testFindQuadNeighbourFinder() {
        FakeTerrainQuad[] roots = new FakeTerrainQuad[4];
        roots[0] = (FakeTerrainQuad) createNestedQuad(2, "");
        roots[1] = (FakeTerrainQuad) createNestedQuad(2, "");
        roots[2] = (FakeTerrainQuad) createNestedQuad(2, "");
        roots[3] = (FakeTerrainQuad) createNestedQuad(2, "");

        NeighbourFinder nf = new TestNeighbourFinder(roots[0], roots[1], roots[2], roots[3]);
        for (FakeTerrainQuad root : roots) {
            root.setNeighbourFinder(nf);
            // Legacy code
            assertEquals(root.findRightQuad(), nf.getRightQuad(root));
            assertEquals(root.findDownQuad(), nf.getDownQuad(root));
            assertEquals(root.findLeftQuad(), nf.getLeftQuad(root));
            assertEquals(root.findTopQuad(), nf.getTopQuad(root));

            // Refactored code
            assertEquals(root.findQuad(DIR_RIGHT), nf.getRightQuad(root));
            assertEquals(root.findQuad(DIR_DOWN), nf.getDownQuad(root));
            assertEquals(root.findQuad(DIR_LEFT), nf.getLeftQuad(root));
            assertEquals(root.findQuad(DIR_TOP), nf.getTopQuad(root));
        }
    }

    @Test
    public void testFindRightQuad() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(3, "");
        FakeTerrainQuad topLeftChild = (FakeTerrainQuad) root.getQuad(1);
        FakeTerrainQuad topRight = (FakeTerrainQuad) root.getQuad(3);

        assertEquals(root.findRightQuad(), null);
        assertEquals(topLeftChild.findRightQuad(), topRight); // Confirm position of two parent quads

        // Check quad children of parent
        assertEquals(topLeftChild.getQuad(1).findRightQuad(), topLeftChild.getQuad(3));
        assertEquals(topLeftChild.getQuad(2).findRightQuad(), topLeftChild.getQuad(4));
        assertEquals(topLeftChild.getQuad(3).findRightQuad(), topRight.getQuad(1));
        assertEquals(topLeftChild.getQuad(4).findRightQuad(), topRight.getQuad(2));

        // Check non-existing neighbour quads
        assertEquals(topRight.getQuad(3).findRightQuad(), null);
        assertEquals(topRight.getQuad(4).findRightQuad(), null);
    }

    @Test
    public void testFindDownQuad() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(3, "");
        FakeTerrainQuad topLeftChild = (FakeTerrainQuad) root.getQuad(1);
        FakeTerrainQuad downLeftChild = (FakeTerrainQuad) root.getQuad(2);

        assertEquals(root.findDownQuad(), null);
        assertEquals(topLeftChild.findDownQuad(), downLeftChild); // Confirm position of two parent quads

        // Check quad children of parent
        assertEquals(topLeftChild.getQuad(1).findDownQuad(), topLeftChild.getQuad(2));
        assertEquals(topLeftChild.getQuad(2).findDownQuad(), downLeftChild.getQuad(1));
        assertEquals(topLeftChild.getQuad(3).findDownQuad(), topLeftChild.getQuad(4));
        assertEquals(topLeftChild.getQuad(4).findDownQuad(), downLeftChild.getQuad(3));

        // Check non-existing neighbour quads
        assertEquals(downLeftChild.getQuad(2).findDownQuad(), null);
        assertEquals(downLeftChild.getQuad(4).findDownQuad(), null);
    }

    @Test
    public void testFindLeftQuad() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(3, "");
        FakeTerrainQuad topLeftChild = (FakeTerrainQuad) root.getQuad(1);
        FakeTerrainQuad topRightChild = (FakeTerrainQuad) root.getQuad(3);

        assertEquals(root.findLeftQuad(), null);
        assertEquals(topRightChild.findLeftQuad(), topLeftChild); // Confirm position of two parent quads

        // Check quad children of parent
        assertEquals(topRightChild.getQuad(1).findLeftQuad(), topLeftChild.getQuad(3));
        assertEquals(topRightChild.getQuad(2).findLeftQuad(), topLeftChild.getQuad(4));
        assertEquals(topRightChild.getQuad(3).findLeftQuad(), topRightChild.getQuad(1));
        assertEquals(topRightChild.getQuad(4).findLeftQuad(), topRightChild.getQuad(2));

        // Check non-existing neighbour quads
        assertEquals(topLeftChild.getQuad(1).findLeftQuad(), null);
        assertEquals(topLeftChild.getQuad(2).findLeftQuad(), null);
    }

    @Test
    public void testFindTopQuad() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(3, "");
        FakeTerrainQuad topLeftChild = (FakeTerrainQuad) root.getQuad(1);
        FakeTerrainQuad downLeftChild = (FakeTerrainQuad) root.getQuad(2);

        assertEquals(root.findTopQuad(), null);
        assertEquals(downLeftChild.findTopQuad(), topLeftChild); // Confirm position of two parent quads

        // Check quad children of parent
        assertEquals(downLeftChild.getQuad(1).findTopQuad(), topLeftChild.getQuad(2));
        assertEquals(downLeftChild.getQuad(2).findTopQuad(), downLeftChild.getQuad(1));
        assertEquals(downLeftChild.getQuad(3).findTopQuad(), topLeftChild.getQuad(4));
        assertEquals(downLeftChild.getQuad(4).findTopQuad(), downLeftChild.getQuad(3));

        // Check non-existing neighbour quads
        assertEquals(topLeftChild.getQuad(1).findTopQuad(), null);
        assertEquals(topLeftChild.getQuad(3).findTopQuad(), null);
    }

    @Test
    public void testGetPatch() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(1, "");
        assertNull(root.getPatch(0));
        for (int i = 1; i <= 4; i++) {
            TerrainPatch child = root.getPatch(i);
            assertNotNull(child);
            assertEquals(root.getChild(i - 1), child);
        }
        assertEquals(root.getPatch(5), null);
    }

    @Test
    public void testFindRightPatch() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(2, "");
        FakeTerrainQuad topLeftChild = (FakeTerrainQuad) root.getQuad(1);
        FakeTerrainQuad topRightChild = (FakeTerrainQuad) root.getQuad(3);

        try {
            root.findRightPatch(null);
        } catch (RuntimeException e) {
            assertEquals(e.getClass(), NullPointerException.class);
        }

        assertEquals(topLeftChild.findQuad(DIR_RIGHT), topRightChild); // Confirm position of two parent quads

        // Check quad children of parent
        TerrainPatch child1 = topLeftChild.findRightPatch(topLeftChild.getPatch(1));
        assertNotNull(child1);
        assertEquals(child1, topLeftChild.getPatch(3));

        TerrainPatch child2 = topLeftChild.findRightPatch(topLeftChild.getPatch(2));
        assertNotNull(child1);
        assertEquals(child2, topLeftChild.getPatch(4));


        TerrainPatch child3 = topLeftChild.findRightPatch(topLeftChild.getPatch(3));
        assertNotNull(child3);
        assertEquals(child3, topRightChild.getPatch(1));


        TerrainPatch child4 = topLeftChild.findRightPatch(topLeftChild.getPatch(4));
        assertNotNull(child4);
        assertEquals(child4, topRightChild.getPatch(2));

        // Check non-existing neighbour quads
        assertEquals(topRightChild.findRightPatch(topRightChild.getPatch(3)), null);
        assertEquals(topRightChild.findRightPatch(topRightChild.getPatch(4)), null);
    }

    @Test
    public void testFindDownPatch() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(2, "");
        FakeTerrainQuad topLeftChild = (FakeTerrainQuad) root.getQuad(1);
        FakeTerrainQuad bottomLeftChild = (FakeTerrainQuad) root.getQuad(2);

        try {
            root.findDownPatch(null);
        } catch (RuntimeException e) {
            assertEquals(e.getClass(), NullPointerException.class);
        }

        assertEquals(topLeftChild.findQuad(DIR_DOWN), bottomLeftChild); // Confirm position of two parent quads

        // Check quad children of parent
        TerrainPatch child1 = topLeftChild.findDownPatch(topLeftChild.getPatch(1));
        assertNotNull(child1);
        assertEquals(child1, topLeftChild.getPatch(2));

        TerrainPatch child2 = topLeftChild.findDownPatch(topLeftChild.getPatch(2));
        assertNotNull(child1);
        assertEquals(child2, bottomLeftChild.getPatch(1));


        TerrainPatch child3 = topLeftChild.findDownPatch(topLeftChild.getPatch(3));
        assertNotNull(child3);
        assertEquals(child3, topLeftChild.getPatch(4));


        TerrainPatch child4 = topLeftChild.findDownPatch(topLeftChild.getPatch(4));
        assertNotNull(child4);
        assertEquals(child4, bottomLeftChild.getPatch(3));

        // Check non-existing neighbour quads
        assertEquals(bottomLeftChild.findDownPatch(bottomLeftChild.getPatch(2)), null);
        assertEquals(bottomLeftChild.findDownPatch(bottomLeftChild.getPatch(4)), null);
    }

    @Test
    public void testFindLeftPatch() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(2, "");
        FakeTerrainQuad topLeftChild = (FakeTerrainQuad) root.getQuad(1);
        FakeTerrainQuad topRightChild = (FakeTerrainQuad) root.getQuad(3);

        try {
            root.findLeftPatch(null);
        } catch (RuntimeException e) {
            assertEquals(e.getClass(), NullPointerException.class);
        }

        assertEquals(topRightChild.findQuad(DIR_LEFT), topLeftChild); // Confirm position of two parent quads

        // Check quad children of parent
        TerrainPatch child1 = topRightChild.findLeftPatch(topRightChild.getPatch(1));
        assertNotNull(child1);
        assertEquals(child1, topLeftChild.getPatch(3));

        TerrainPatch child2 = topRightChild.findLeftPatch(topRightChild.getPatch(2));
        assertNotNull(child1);
        assertEquals(child2, topLeftChild.getPatch(4));


        TerrainPatch child3 = topRightChild.findLeftPatch(topRightChild.getPatch(3));
        assertNotNull(child3);
        assertEquals(child3, topRightChild.getPatch(1));


        TerrainPatch child4 = topRightChild.findLeftPatch(topRightChild.getPatch(4));
        assertNotNull(child4);
        assertEquals(child4, topRightChild.getPatch(2));

        // Check non-existing neighbour quads
        assertEquals(topLeftChild.findLeftPatch(topLeftChild.getPatch(1)), null);
        assertEquals(topLeftChild.findLeftPatch(topLeftChild.getPatch(2)), null);
    }

    @Test
    public void testFindTopPatch() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(2, "");
        FakeTerrainQuad topRightChild = (FakeTerrainQuad) root.getQuad(3);
        FakeTerrainQuad bottomRightChild = (FakeTerrainQuad) root.getQuad(4);

        try {
            root.findTopPatch(null);
        } catch (RuntimeException e) {
            assertEquals(e.getClass(), NullPointerException.class);
        }

        assertEquals(bottomRightChild.findQuad(DIR_TOP), topRightChild); // Confirm position of two parent quads

        // Check quad children of parent
        TerrainPatch child1 = bottomRightChild.findTopPatch(bottomRightChild.getPatch(1));
        assertNotNull(child1);
        assertEquals(child1, topRightChild.getPatch(2));

        TerrainPatch child2 = bottomRightChild.findTopPatch(bottomRightChild.getPatch(2));
        assertNotNull(child1);
        assertEquals(child2, bottomRightChild.getPatch(1));


        TerrainPatch child3 = bottomRightChild.findTopPatch(bottomRightChild.getPatch(3));
        assertNotNull(child3);
        assertEquals(child3, topRightChild.getPatch(4));


        TerrainPatch child4 = bottomRightChild.findTopPatch(bottomRightChild.getPatch(4));
        assertNotNull(child4);
        assertEquals(child4, bottomRightChild.getPatch(3));

        // Check non-existing neighbour quads
        assertEquals(topRightChild.findTopPatch(topRightChild.getPatch(1)), null);
        assertEquals(topRightChild.findTopPatch(topRightChild.getPatch(3)), null);
    }

    @Test
    public void testFindQuad() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(2, "");

        assertEquals(root.quadrant, 0);

        assertNull(root.findQuad(-1));
        assertNull(root.getQuad(1).findQuad(-1));

        assertEquals(root.findQuad(DIR_RIGHT), root.findRightQuad());
        assertEquals(root.findQuad(DIR_DOWN), root.findDownQuad());
        assertEquals(root.findQuad(DIR_LEFT), root.findLeftQuad());
        assertEquals(root.findQuad(DIR_TOP), root.findTopQuad());

        for (int i = 0; i < root.getChildren().size(); i++) {
            FakeTerrainQuad child = (FakeTerrainQuad) root.getQuad(i);
            assertEquals(child.findQuad(DIR_RIGHT), child.findRightQuad());
            assertEquals(child.findQuad(DIR_DOWN), child.findDownQuad());
            assertEquals(child.findQuad(DIR_LEFT), child.findLeftQuad());
            assertEquals(child.findQuad(DIR_TOP), child.findTopQuad());
        }
    }

    /**
     * Tests the calculateLod method, which name has been refactored to hasLodChanged.
     * We came to the conclusion that the method does belong to TerrainQuad, but should be renamed
     * as it does not calculate anything. Is only retrieves values from subclasses of the LodCalculator interface.
     * The actual lodCalculator is defined in the calculateLod method of these childs.
     */
    @Test
    public void testHasLodChanged() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(1, "");
        assertFalse(root.hasLodChanged(location, updates, lodCalculator));
        assertTrue(root.hasLodChanged(location, updates, fakeLodCalculator));

        FakeTerrainQuad leaf = (FakeTerrainQuad) createNestedQuad(1, "");
        leaf.attachChild(children[1]);
        assertTrue(leaf.hasLodChanged(location, updates, fakeLodCalculator));
    }


    /**
     * Tests the refactored createQuadPatch method, which name is refactored to setPatchChildren.
     * setPatchChildren makes use of two new methods createHeightBlock and createQuadPatch (part of TerrainPatch).
     * This is the first of 4 tests, as setPatchChildren couples 4 TerrainPatches to
     * a TerrainQuad. Each tests makes sure that the correct TerrainPatch child has been coupled.
     */
    @Test
    public void testSetPatchChildren1() {
        String patch1 = "terrain_1Patch1";

        boundingBox.setCenter(1.0f, 54.88082f, 1.0f);
        terrainQuad.setPatchChildren(testHeightmap);

        assertTrue(terrainQuad.getChild(patch1) instanceof TerrainPatch);
        TerrainPatch p1 = (TerrainPatch) terrainQuad.getChild(patch1);

        assertEquals(patch1, p1.getName());
        assertEquals(v2f.set(-1.0f, -1.0f), p1.getOffset());
        assertEquals(1.0f, p1.getOffsetAmount(), 0.0f);
        assertEquals(v3f.add(-2.0f, 0.0f, -2.0f), p1.getLocalTranslation());
        assertEquals(9, p1.getHeightMap().length);
        assertEquals(5, p1.getTotalSize());
        assertEquals(1, p1.getQuadrant());
        assertEquals(boundingBox.getCenter(), p1.getModelBound().getCenter());
    }

    @Test
    public void testSetPatchChildren2() {
        String patch2 = "terrain_1Patch2";

        boundingBox.setCenter(1.0f, 92.78813f, 1.0f);

        terrainQuad.setPatchChildren(testHeightmap);

        assertTrue(terrainQuad.getChild(patch2) instanceof TerrainPatch);
        TerrainPatch p1 = (TerrainPatch) terrainQuad.getChild(patch2);

        assertEquals(patch2, p1.getName());
        assertEquals(v2f.set(-1.0f, 1.0f), p1.getOffset());
        assertEquals(1.0f, p1.getOffsetAmount(), 0.0f);
        assertEquals(v3f.add(-2.0f, 0.0f, 0.0f), p1.getLocalTranslation());
        assertEquals(9, p1.getHeightMap().length);
        assertEquals(5, p1.getTotalSize());
        assertEquals(2, p1.getQuadrant());
        assertEquals(boundingBox.getCenter(), p1.getModelBound().getCenter());
    }

    @Test
    public void testSetPatchChildren3() {
        String patch3 = "terrain_1Patch3";

        boundingBox.setCenter(1.0f, 64.86637f, 1.0f);

        terrainQuad.setPatchChildren(testHeightmap);

        assertTrue(terrainQuad.getChild(patch3) instanceof TerrainPatch);
        TerrainPatch p1 = (TerrainPatch) terrainQuad.getChild(patch3);

        assertEquals(patch3, p1.getName());
        assertEquals(v2f.set(1.0f, -1.0f), p1.getOffset());
        assertEquals(1.0f, p1.getOffsetAmount(), 0.0f);
        assertEquals(v3f.add(0.0f, 0.0f, -2.0f), p1.getLocalTranslation());
        assertEquals(9, p1.getHeightMap().length);
        assertEquals(5, p1.getTotalSize());
        assertEquals(3, p1.getQuadrant());
        assertEquals(boundingBox.getCenter(), p1.getModelBound().getCenter());
    }

    @Test
    public void testSetPatchChildren4() {
        String patch4 = "terrain_1Patch4";

        boundingBox.setCenter(1.0f, 180.92175f, 1.0f);

        terrainQuad.setPatchChildren(testHeightmap);

        assertTrue(terrainQuad.getChild(patch4) instanceof TerrainPatch);
        TerrainPatch p1 = (TerrainPatch) terrainQuad.getChild(patch4);

        assertEquals(patch4, p1.getName());
        assertEquals(v2f.set(1.0f, 1.0f), p1.getOffset());
        assertEquals(1.0f, p1.getOffsetAmount(), 0.0f);
        assertEquals(v3f.add(0.0f, 0.0f, 0.0f), p1.getLocalTranslation());
        assertEquals(9, p1.getHeightMap().length);
        assertEquals(5, p1.getTotalSize());
        assertEquals(4, p1.getQuadrant());
        assertEquals(boundingBox.getCenter(), p1.getModelBound().getCenter());
    }

    /**
     * Tests the method getHeightmapHeight(int x, int z).
     * An extra internal class QuadrantFinder has been created to find the
     * corresponding quadrant for the given coordinates.
     */
    @Test
    public void testGetHeightmapHeight() {
        assertEquals(0.0f, terrainQuad.getHeightmapHeight(6, 6), 0.0f);
        assertEquals(testHeightmap[0], terrainQuad.getHeightmapHeight(0, 0), 0.0f);
        children = new FakeTerrainQuad[3];
        for (int i = 0; i < 3; i++) {
            children[i] = new FakeTerrainQuad();
        }

        parentTerrainQuad = new FakeTerrainQuad();
        parentTerrainQuad.size = 10;
        fakeCreateQuad(parentTerrainQuad, children);

        assertEquals(Float.NaN, parentTerrainQuad.getHeightmapHeight(5, 3), 0.0f);

        children = new FakeTerrainQuad[2];
        for (int i = 0; i < 2; i++) {
            children[i] = new FakeTerrainQuad();
        }
        parentTerrainQuad = new FakeTerrainQuad();
        parentTerrainQuad.size = 50;
        fakeCreateQuad(parentTerrainQuad, children);

        assertEquals(Float.NaN, parentTerrainQuad.getHeightmapHeight(5, 49), 0.0f);


    }

    /**
     * Tests the method getMeshNormal(int x, int z).
     * An extra internal class QuadrantFinder has been created to find the
     * corresponding quadrant for the given coordinates.
     */
    @Test
    public void testGetMeshNormal() {
        Vector3f v1 = new Vector3f(-0.7327255f, 0.043074645f, -0.67915976f);

        assertEquals(null, terrainQuad.getMeshNormal(10, 10));
        assertEquals(v1, terrainQuad.getMeshNormal(0, 0));
        children = new FakeTerrainQuad[3];
        for (int i = 0; i < 3; i++) {
            children[i] = new FakeTerrainQuad();
        }

        parentTerrainQuad = new FakeTerrainQuad();
        parentTerrainQuad.size = 10;
        fakeCreateQuad(parentTerrainQuad, children);

        assertEquals(null, parentTerrainQuad.getMeshNormal(5, 3));

        children = new FakeTerrainQuad[2];
        for (int i = 0; i < 2; i++) {
            children[i] = new FakeTerrainQuad();
        }
        parentTerrainQuad = new FakeTerrainQuad();
        parentTerrainQuad.size = 50;
        fakeCreateQuad(parentTerrainQuad, children);

        assertEquals(null, parentTerrainQuad.getMeshNormal(5, 49));
    }

    /**
     * Tests the method getHeight(int x, int z, float xm, float zm).
     * This method is being tested to make sure the private method
     * findMatchingChild(int x, int z) after its refactoring in which
     * an extra internal class QuadrantFinder has been created to find the
     * corresponding quadrant for the given coordinates.
     */
    @Test
    public void getHeight() {
        assertEquals(Float.NaN, terrainQuad.getHeight(10, 10, 10.0f, 10.0f), 0.0f);
        assertEquals(2.9181213f, terrainQuad.getHeight(0, 0, 0.0f, 0.0f), 0.0f);

        children = new FakeTerrainQuad[3];
        for (int i = 0; i < 3; i++) {
            children[i] = new FakeTerrainQuad();
        }

        parentTerrainQuad = new FakeTerrainQuad();
        parentTerrainQuad.size = 10;
        fakeCreateQuad(parentTerrainQuad, children);

        assertEquals(Float.NaN, parentTerrainQuad.getHeight(0, 0, 0.0f, 0.0f), 0.0f);
    }


    /**
     * Test for TerrainQuads fixEdges method.
     * Both situations of updated and non-updated LODs are covered.
     * A typical situation is covered with assertions.
     */
    @Test
    public void testFixEdges() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(2, "");
        HashMap<String,UpdatedTerrainPatch> updated = new HashMap<>();

        assertNotNull(root.getChildren());

        // Create UTPs and add it to the updated var
        for (int i = 1; i <= root.getQuad(1).getChildren().size(); i++) {
            UpdatedTerrainPatch utp = new UpdatedTerrainPatch(root.getQuad(1).getPatch(i));
            updated.put(root.getQuad(1).getPatch(i).getName(), utp);
        }

        // Copy keys
        Set<String> oldKeyset = new HashSet<>();
        for (String s : updated.keySet()) {
            oldKeyset.add(s);
        }

        // Without any changes in LOD, keyset should remain the same
        root.fixEdges(updated);
        assertTrue(updated.keySet().equals(oldKeyset));

        // Change LOD for all patches in quad 1.
        for (int i = root.getQuad(1).getChildren().size(); i > 0; i--) {
            UpdatedTerrainPatch utp = updated.get(root.getQuad(1).getPatch(i).getName());
            utp.setPreviousLod(1); // Dummy value
            utp.setNewLod(2); // Dummy value
        }

        // Copy keys
        oldKeyset.clear();
        for (String s : updated.keySet()) {
            oldKeyset.add(s);
        }

        root.fixEdges(updated);

        // Make sure new keyset is different
        assertFalse(updated.keySet().equals(oldKeyset));

        // Extract newly updated keys
        updated.keySet().removeAll(oldKeyset);

        // Assert new keys
        assertTrue(updated.keySet().contains("21"));
        assertTrue(updated.keySet().contains("23"));
        assertTrue(updated.keySet().contains("31"));
        assertTrue(updated.keySet().contains("32"));
    }

    /**
     * Test for TerrainQuads deprecated findNeighboursLod method.
     * It tests for both an empty UpdatedTerrainPatch set and a full one.
     * Assertions are made to verify that the LODs are set correctly.
     */
    @Test
    public void testFindNeighboursLod() {
        FakeTerrainQuad root = (FakeTerrainQuad) createNestedQuad(2, "");
        HashMap<String,UpdatedTerrainPatch> updated = new HashMap<>();

        assertNotNull(root.getChildren());

        // Call method with empty setof UTPs
        root.findNeighboursLod(updated);

        // Check if all patches are updated
        for (int i = 1; i <= root.getChildren().size(); i++) {
            for (int j = 1; j <= root.getQuad(i).getChildren().size(); j++) {
                TerrainPatch tp = root.getQuad(i).getPatch(j);
                assertTrue(updated.containsKey(tp.getName()));
            }
        }

        // Set random LOD to ensure proper assertions.
        for (int i = 1; i <= root.getChildren().size(); i++) {
            for (int j = 1; j <= root.getQuad(i).getChildren().size(); j++) {
                UpdatedTerrainPatch utp = updated.get(root.getQuad(i).getPatch(j).getName());
                utp.setPreviousLod((int)(Math.random() * 100)); // Dummy value
                utp.setNewLod((int)(Math.random() * 100)); // Dummy value
            }
        }

        root.findNeighboursLod(updated);

        // Check if all patches are updated.
        for (int i = 1; i <= root.getChildren().size(); i++) {
            for (int j = 1; j <= root.getQuad(i).getChildren().size(); j++) {
                TerrainPatch tp = root.getQuad(i).getPatch(j);
                UpdatedTerrainPatch utp = updated.get(tp.getName());

                TerrainPatch left = tp.findPatch(DIR_LEFT);
                if (left != null) {
                    UpdatedTerrainPatch leftUtp = updated.get(left.getName());
                    assertEquals(utp.getLeftLod(), leftUtp.getNewLod());
                    assertEquals(leftUtp.getRightLod(), utp.getNewLod());
                }

                TerrainPatch right = tp.findPatch(DIR_RIGHT);
                if (right != null) {
                    UpdatedTerrainPatch rightUtp = updated.get(right.getName());
                    assertEquals(utp.getRightLod(), rightUtp.getNewLod());
                    assertEquals(rightUtp.getLeftLod(), utp.getNewLod());
                }

                TerrainPatch top = tp.findPatch(DIR_TOP);
                if (top != null) {
                    UpdatedTerrainPatch topUtp = updated.get(top.getName());
                    assertEquals(utp.getTopLod(), topUtp.getNewLod());
                    assertEquals(topUtp.getBottomLod(), utp.getNewLod());
                }

                TerrainPatch down = tp.findPatch(DIR_DOWN);
                if (down != null) {
                    UpdatedTerrainPatch downUtp = updated.get(down.getName());
                    assertEquals(utp.getBottomLod(), downUtp.getNewLod());
                    assertEquals(downUtp.getTopLod(), utp.getNewLod());
                }
            }
        }
    }
}
