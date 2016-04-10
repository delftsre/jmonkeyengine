package com.jme3.terrain.geomipmap;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UpdatedTerrainPatchTest {
    private final int DIR_RIGHT = 0, DIR_DOWN = 1, DIR_LEFT = 2, DIR_TOP = 3;

    @Test
    public void testSetLeftLod() {
        TerrainPatch tp = new TerrainPatch();
        UpdatedTerrainPatch utp = new UpdatedTerrainPatch(tp);
        utp.setLeftLod(1);
        assertEquals(utp.getLeftLod(), 1);
    }

    @Test
    public void testSetTopLod() {
        TerrainPatch tp = new TerrainPatch();
        UpdatedTerrainPatch utp = new UpdatedTerrainPatch(tp);
        utp.setTopLod(1);
        assertEquals(utp.getTopLod(), 1);
    }

    @Test
    public void testSetRightLod() {
        TerrainPatch tp = new TerrainPatch();
        UpdatedTerrainPatch utp = new UpdatedTerrainPatch(tp);
        utp.setRightLod(1);
        assertEquals(utp.getRightLod(), 1);
    }

    @Test
    public void testSetBottomLod() {
        TerrainPatch tp = new TerrainPatch();
        UpdatedTerrainPatch utp = new UpdatedTerrainPatch(tp);
        utp.setBottomLod(1);
        assertEquals(utp.getBottomLod(), 1);
    }

    @Test
    public void testSetLod() {
        TerrainPatch tp = new TerrainPatch();
        UpdatedTerrainPatch utp = new UpdatedTerrainPatch(tp);

        utp.setLod(1, DIR_DOWN);
        assertEquals(utp.getBottomLod(), 1);

        utp.setLod(2, DIR_LEFT);
        assertEquals(utp.getLeftLod(), 2);

        utp.setLod(3, DIR_RIGHT);
        assertEquals(utp.getRightLod(), 3);

        utp.setLod(4, DIR_TOP);
        assertEquals(utp.getTopLod(), 4);
    }
}
