package com.jme3.terrain.geomipmap;

public class TestNeighbourFinder implements NeighbourFinder {

    TerrainQuad quad1, quad2, quad3, quad4;

    public TestNeighbourFinder(TerrainQuad quad1, TerrainQuad quad2, TerrainQuad quad3, TerrainQuad quad4) {
        this.quad1 = quad1;
        this.quad2 = quad2;
        this.quad3 = quad3;
        this.quad4 = quad4;
    }

    @Override
    public TerrainQuad getRightQuad(TerrainQuad center) {
        if (center == quad1) {
            return quad3;
        }
        if (center == quad2) {
            return quad4;
        }

        return null;
    }

    @Override
    public TerrainQuad getLeftQuad(TerrainQuad center) {
        if (center == quad3) {
            return quad1;
        }
        if (center == quad4) {
            return quad2;
        }

        return null;
    }

    @Override
    public TerrainQuad getTopQuad(TerrainQuad center) {
        if (center == quad2) {
            return quad1;
        }
        if (center == quad4) {
            return quad3;
        }

        return null;
    }

    @Override
    public TerrainQuad getDownQuad(TerrainQuad center) {
        if (center == quad1) {
            return quad2;
        }
        if (center == quad3) {
            return quad4;
        }

        return null;
    }
}
