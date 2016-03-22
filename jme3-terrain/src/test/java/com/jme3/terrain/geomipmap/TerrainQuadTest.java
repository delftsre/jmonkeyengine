package com.jme3.terrain.geomipmap;

import com.jme3.asset.DesktopAssetManager;
import com.jme3.material.*;
import com.jme3.scene.Node;
import com.jme3.terrain.*;
import org.junit.Test;

public class TerrainQuadTest {
    @Test
    public void testQuadCreate(){
        TerrainQuad tq = new TerrainQuad("foo", 4 + 1, 16 + 1, null);
    }

    @Test
    public void testGenerateEntropyNotRoot(){
        TerrainQuad tq = new TerrainQuad("foo", 4 + 1, 16 + 1, null);
        EntropyProgressMonitor epm = new EntropyProgressMonitor();
        tq.generateEntropy(epm);
        assert epm.progress == 16;
        assert Float.isNaN(epm.max);
    }

    @Test
    public void testGenerateEntropyWithRoot(){
        Node rootNode = new Node("root");
        TerrainQuad tq = new TerrainQuad("foo", 4 + 1, 16 + 1, null);
        rootNode.attachChild(tq);
        EntropyProgressMonitor epm = new EntropyProgressMonitor();
        tq.generateEntropy(epm);
        assert epm.progress == 16;
        assert epm.max == 16;
    }

    @Test
    public void testSetGetMaterial(){
        Material mat = new Material(new MaterialDef(new DesktopAssetManager(), "foo"));
        TerrainQuad tq = new TerrainQuad("foo", 4 + 1, 16 + 1, null);
        tq.setMaterial(mat);
        assert tq.getMaterial() == mat;
    }
}

class EntropyProgressMonitor implements ProgressMonitor {
    public float progress;
    public float max;

    public EntropyProgressMonitor(){
        this.progress = 0;
        this.max = Float.NaN;
    }

    public void incrementProgress(float increment){
        this.progress += increment;
    }

    public void setMonitorMax(float max){
        this.max = max;
    }

    public float getMonitorMax(){
        return this.max;
    }

    public void progressComplete(){
        this.progress = this.max;
    }
}
