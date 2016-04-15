package com.jme3.terrain.geomipmap;

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

}
