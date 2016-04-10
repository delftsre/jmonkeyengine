package com.jme3.terrain.geomipmap;

import com.jme3.math.Vector3f;
import com.jme3.terrain.geomipmap.lodcalc.DistanceLodCalculator;


import java.util.HashMap;
import java.util.List;

public class FakeDistanceLodCalculator extends DistanceLodCalculator {

    @Override
    public boolean calculateLod(TerrainPatch terrainPatch, List<Vector3f> locations, HashMap<String, UpdatedTerrainPatch> updates){return true;}

}
