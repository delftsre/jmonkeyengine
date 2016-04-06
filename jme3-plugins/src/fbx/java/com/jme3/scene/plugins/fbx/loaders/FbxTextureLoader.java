package com.jme3.scene.plugins.fbx.loaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.scene.plugins.fbx.file.FbxElement;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

public class FbxTextureLoader implements FbxElementLoader {
    private Map<Long, TextureData> texDataMap = new HashMap<Long, TextureData>();
    private Map<Long, Texture> texMap = new HashMap<Long, Texture>();
        
    public void load(FbxElement element) {
        long id = (Long) element.properties.get(0);
        String path = (String) element.properties.get(1);
        String type = (String) element.properties.get(2);
        if(type.equals("")) {
            TextureData data = new TextureData();
            data.name = path.substring(0, path.indexOf(0));
            for(FbxElement e : element.children) {
                if(e.id.equals("Type"))
                    data.bindType = (String) e.properties.get(0);
                else if(e.id.equals("FileName"))
                    data.filename = (String) e.properties.get(0);
            }
            texDataMap.put(id, data);
        }
    }
    
    public void linkImagesToTextures(Map<Long, Image> imgMap, Map<Long, List<Long>> refMap) {
        createTextures();
        
        for(long imgId : imgMap.keySet()) {
            List<Long> refs = refMap.get(imgId);
            if(refs == null)
                continue;
            Image img = imgMap.get(imgId);
            for(long refId : refs) {
                Texture tex = texMap.get(refId);
                if(tex != null)
                    tex.setImage(img);
            }
        }
    }
    
    public Map<Long, Texture> getObjectMap() {
        return texMap;
    }
    
    public void release() {
        texDataMap.clear();
        texMap.clear();
    }
    
    private void createTextures() {
        for(long texId : texDataMap.keySet()) {
            TextureData data = texDataMap.get(texId);
            Texture tex = createTexture(data);
            if(tex != null)
                texMap.put(texId, tex);
        }
    }
    
    private Texture createTexture(TextureData data) {
        Texture tex = new Texture2D();
        tex.setName(data.name);
        return tex;
    }
    
    private class TextureData {
        String name;
        String bindType;
        String filename;
    }
}
