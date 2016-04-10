package com.jme3.renderer.geometryrenderers;

import com.jme3.material.MatParam;
import com.jme3.material.Technique;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.scene.Geometry;
import com.jme3.shader.Shader;
import com.jme3.util.ListMap;

public class NoLightGeometryRenderer extends GeometryRenderer {
    public NoLightGeometryRenderer(Geometry g, RenderManager rm) {
        super(g, rm);
    }

    @Override
    public void renderForLighting() {
        Technique technique = this.geometry.getMaterial().getActiveTechnique();
        Shader shader = technique.getShader();
        Renderer renderer = this.renderManager.getRenderer();

        resetUniformsNotSetByCurrent(shader);
        renderer.setShader(shader);

        renderMeshFromGeometry();
    }
}
