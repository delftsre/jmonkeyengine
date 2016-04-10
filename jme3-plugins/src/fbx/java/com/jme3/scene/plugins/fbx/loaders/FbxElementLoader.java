package com.jme3.scene.plugins.fbx.loaders;

import java.util.Map;

import com.jme3.scene.plugins.fbx.file.FbxElement;

public interface FbxElementLoader {
    /**
     * Loads an fbxelement in the local datamap
     */
    public void load(FbxElement element);
        
    /**
     * Returns all the created instances of this loader
     */
    public Map<Long, ?> getObjectMap();
    
    /**
     * Clears all data that was saved in the local map
     */
    public void release();
}
