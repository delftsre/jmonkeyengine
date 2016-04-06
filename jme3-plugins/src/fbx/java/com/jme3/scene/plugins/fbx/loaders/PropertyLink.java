package com.jme3.scene.plugins.fbx.loaders;

public class PropertyLink {
    private long ref;
    private String propName;
    
    public PropertyLink(long id, String prop) {
        this.ref = id;
        this.propName = prop;
    }
    
    public long getRef() {
        return ref;
    }
    
    public String getPropName() {
        return propName;
    }
}