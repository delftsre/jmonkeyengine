/*
 * Copyright (c) 2009-2016 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.scene.plugins.fbx.loaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.plugins.fbx.loaders.PropertyLink;
import com.jme3.scene.plugins.fbx.file.FbxElement;
import com.jme3.texture.Texture;

public class FbxMaterialLoader implements FbxElementLoader {

    private AssetManager assetManager;
    private Map<Long, MaterialData> matDataMap = new HashMap<Long, MaterialData>();
    private Map<Long, Material> matMap = new HashMap<Long, Material>();

    public FbxMaterialLoader(AssetManager assetMgr) {
        assetManager = assetMgr;
    }
    
    public void load(FbxElement element) {
        long id = (Long) element.properties.get(0);
        String path = (String) element.properties.get(1);
        String type = (String) element.properties.get(2);
        if(type.equals("")) {
            MaterialData data = new MaterialData();
            data.name = path.substring(0, path.indexOf(0));
            for(FbxElement e : element.children) {
                if(e.id.equals("ShadingModel")) {
                    data.shadingModel = (String) e.properties.get(0);
                } else if(e.id.equals("Properties70")) {
                    for(FbxElement e2 : e.children) {
                        if(e2.id.equals("P")) {
                            String propName = (String) e2.properties.get(0);
                            if(propName.equals("AmbientColor")) {
                                double x = (Double) e2.properties.get(4);
                                double y = (Double) e2.properties.get(5);
                                double z = (Double) e2.properties.get(6);
                                data.ambientColor.set((float) x, (float) y, (float) z);
                            } else if(propName.equals("AmbientFactor")) {
                                double x = (Double) e2.properties.get(4);
                                data.ambientFactor = (float) x;
                            } else if(propName.equals("DiffuseColor")) {
                                double x = (Double) e2.properties.get(4);
                                double y = (Double) e2.properties.get(5);
                                double z = (Double) e2.properties.get(6);
                                data.diffuseColor.set((float) x, (float) y, (float) z);
                            } else if(propName.equals("DiffuseFactor")) {
                                double x = (Double) e2.properties.get(4);
                                data.diffuseFactor = (float) x;
                            } else if(propName.equals("SpecularColor")) {
                                double x = (Double) e2.properties.get(4);
                                double y = (Double) e2.properties.get(5);
                                double z = (Double) e2.properties.get(6);
                                data.specularColor.set((float) x, (float) y, (float) z);
                            } else if(propName.equals("Shininess") || propName.equals("ShininessExponent")) {
                                double x = (Double) e2.properties.get(4);
                                data.shininessExponent = (float) x;
                            }
                        }
                    }
                }
            }
            matDataMap.put(id, data);
        }
    }
    
    /**
     * Links textures to materials using the given propertymap
     * @param texMap
     * @param propMap
     */
    public void linkTexturesToMaterials(Map<Long, Texture> texMap, Map<Long, List<PropertyLink>> propMap) {
        // Build materials        
        createMaterials();
        
        // Link given textures to materials
        for(long texId : texMap.keySet()) {
            List<PropertyLink> props = propMap.get(texId);
            if(props == null)
                continue;
            Texture tex = texMap.get(texId);
            for(PropertyLink prop : props) {
                Material mat = matMap.get(prop.getRef());
                if(mat != null) {
                    if(prop.getPropName().equals("DiffuseColor")) {
                        mat.setTexture("DiffuseMap", tex);
                        mat.setColor("Diffuse", ColorRGBA.White);
                    } else if(prop.getPropName().equals("SpecularColor")) {
                        mat.setTexture("SpecularMap", tex);
                        mat.setColor("Specular", ColorRGBA.White);
                    } else if(prop.getPropName().equals("NormalMap"))
                        mat.setTexture("NormalMap", tex);
                }
            }
        }
    }
    
    /**
     * Creates actual material instances of the MaterialData
     */
    private void createMaterials() {
        for(long matId : matDataMap.keySet()) {
            MaterialData data = matDataMap.get(matId);
            Material material = createMaterial(data);
            if(material != null)
                matMap.put(matId, material);
        }
    }
    
    
    private Material createMaterial(MaterialData data) {
        Material m = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        m.setName(data.name);
        data.ambientColor.multLocal(data.ambientFactor);
        data.diffuseColor.multLocal(data.diffuseFactor);
        data.specularColor.multLocal(data.specularFactor);
        m.setColor("Ambient", new ColorRGBA(data.ambientColor.x, data.ambientColor.y, data.ambientColor.z, 1));
        m.setColor("Diffuse", new ColorRGBA(data.diffuseColor.x, data.diffuseColor.y, data.diffuseColor.z, 1));
        m.setColor("Specular", new ColorRGBA(data.specularColor.x, data.specularColor.y, data.specularColor.z, 1));
        m.setFloat("Shininess", data.shininessExponent);
        m.setBoolean("UseMaterialColors", true);
        m.getAdditionalRenderState().setAlphaTest(true);
        m.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        return m;
    }
    
    public Map<Long, Material> getObjectMap() {
        return matMap;
    }
    
    public void release() {
        matMap.clear();
        matDataMap.clear();
    }
    
    public class MaterialData {
        String name;
        String shadingModel = "phong";
        Vector3f ambientColor = new Vector3f(0.2f, 0.2f, 0.2f);
        float ambientFactor = 1.0f;
        Vector3f diffuseColor = new Vector3f(0.8f, 0.8f, 0.8f);
        float diffuseFactor = 1.0f;
        Vector3f specularColor = new Vector3f(0.2f, 0.2f, 0.2f);
        float specularFactor = 1.0f;
        float shininessExponent = 20.0f;
    }
}
