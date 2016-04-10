package com.jme3.terrain.geomipmap;

import com.jme3.scene.Spatial;

public class QuadrantChild {
    int col;
    int row;
    Spatial child;
    
    QuadrantChild(int col, int row, Spatial child) {
        this.col = col;
        this.row = row;
        this.child = child;
    }
}
