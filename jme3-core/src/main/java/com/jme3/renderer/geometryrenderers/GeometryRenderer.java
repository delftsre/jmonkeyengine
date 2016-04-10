package com.jme3.renderer.geometryrenderers;

import com.jme3.light.AmbientLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.material.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix4f;
import com.jme3.renderer.Caps;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.RendererException;
import com.jme3.renderer.opengl.GLRenderer;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.instancing.InstancedGeometry;
import com.jme3.shader.Shader;
import com.jme3.shader.Uniform;
import com.jme3.system.SystemListener;
import com.jme3.util.ListMap;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * A class that contains all information to render a Geometry
 */
public abstract class GeometryRenderer {
    protected final Geometry geometry;
    protected final RenderManager renderManager;

    protected RenderState mergedRenderState = new RenderState();

    public GeometryRenderer(Geometry g, RenderManager rm) {
        this.geometry = g;
        this.renderManager = rm;
    }

    /**
     * Called by {@link RenderManager} to render the geometry
     * <p>
     * The material is rendered as follows:
     * <ul>
     * <li>Determine which technique to use to render the material -
     * either what the user selected via
     * {@link Geometry#selectTechnique(String, RenderManager)}
     * or the first default technique that the renderer supports
     * (based on the technique's {@link TechniqueDef#getRequiredCaps() requested rendering capabilities})<ul>
     * <li>If the technique has been changed since the last frame, then it is notified via
     * {@link Technique#makeCurrent(com.jme3.asset.AssetManager, boolean, java.util.EnumSet)
     * Technique.makeCurrent()}.
     * If the technique wants to use a shader to render the model, it should load it at this part -
     * the shader should have all the proper defines as declared in the technique definition,
     * including those that are bound to material parameters.
     * The technique can re-use the shader from the last frame if
     * no changes to the defines occurred.</li></ul>
     * <li>Set the {@link RenderState} to use for rendering. The render states are
     * applied in this order (later RenderStates override earlier RenderStates):<ol>
     * <li>{@link TechniqueDef#getRenderState() Technique Definition's RenderState}
     * - i.e. specific renderstate that is required for the shader.</li>
     * <li>{@link Material#getAdditionalRenderState() Material Instance Additional RenderState}
     * - i.e. ad-hoc renderstate set per model</li>
     * <li>{@link RenderManager#getForcedRenderState() RenderManager's Forced RenderState}
     * - i.e. renderstate requested by a {@link com.jme3.post.SceneProcessor} or
     * post-processing filter.</li></ol>
     * <li>If the technique {@link TechniqueDef#isUsingShaders() uses a shader}, then the uniforms of the shader must be updated.<ul>
     * <li>Uniforms bound to material parameters are updated based on the current material parameter values.</li>
     * <li>Uniforms bound to world parameters are updated from the RenderManager.
     * Internally {@link UniformBindingManager} is used for this task.</li>
     * <li>Uniforms bound to textures will cause the texture to be uploaded as necessary.
     * The uniform is set to the texture unit where the texture is bound.</li></ul>
     * <li>If the technique uses a shader, the model is then rendered according
     * to the lighting mode specified on the technique definition.<ul>
     * <li>{@link TechniqueDef.LightMode#SinglePass single pass light mode} fills the shader's light uniform arrays
     * with the first 4 lights and renders the model once.</li>
     * <li>{@link TechniqueDef.LightMode#MultiPass multi pass light mode} light mode renders the model multiple times,
     * for the first light it is rendered opaque, on subsequent lights it is
     * rendered with {@link RenderState.BlendMode#AlphaAdditive alpha-additive} blending and depth writing disabled.</li>
     * </ul>
     * <li>For techniques that do not use shaders,
     * fixed function OpenGL is used to render the model (see {@link GL1Renderer} interface):<ul>
     * <li>OpenGL state ({@link FixedFuncBinding}) that is bound to material parameters is updated. </li>
     * <li>The texture set on the material is uploaded and bound.
     * Currently only 1 texture is supported for fixed function techniques.</li>
     * <li>If the technique uses lighting, then OpenGL lighting state is updated
     * based on the light list on the geometry, otherwise OpenGL lighting is disabled.</li>
     * <li>The mesh is uploaded and rendered.</li>
     * </ul>
     * </ul>
     */
    public void render() {
        Technique technique = this.geometry.getMaterial().getActiveTechnique();
        Renderer renderer = this.renderManager.getRenderer();
        RenderState rs = this.geometry.getMaterial().getAdditionalRenderState();
        Shader shader = technique.getShader();

        if (renderManager.getForcedRenderState() != null) {
            renderer.applyRenderState(renderManager.getForcedRenderState());
        } else {
            TechniqueDef techDef = technique.getDef();

            if (techDef.getRenderState() != null) {
                renderer.applyRenderState(techDef.getRenderState().copyMergedTo(rs, mergedRenderState));
            } else {
                renderer.applyRenderState(RenderState.DEFAULT.copyMergedTo(rs, mergedRenderState));
            }
        }


        clearUniformsSetByCurrent(shader);
        renderManager.updateUniformBindings(this.geometry.getMaterial().getActiveTechnique().getWorldBindUniforms());

        ListMap<String, MatParam> paramsMap = geometry.getMaterial().getParamsMap();

        for (int i = 0; i < paramsMap.size(); i++) {
            MatParam param = paramsMap.getValue(i);
            param.apply(renderer, technique);
        }

        this.renderForLighting();
    }

    /**
     * Method that renders this geometry specifically for the lighting chosen
     *
     * So, if a {@link Technique} demands MultiPassLighting, the {@link Geometry} will select a
     * {@link MultiPassGeometryRenderer} to render itself, all specific code for multi pass rendering is then in
     * this function, which gets called from {@link GeometryRenderer#render()}
     */
    public abstract void renderForLighting();

    /**
     * Convenienece function that renders a mesh using the currently set shader
     *
     * If ths geomery is instanced, it will render multiple instances of this mesh on the different locations set
     * by the instancing
     */

    protected void renderMeshFromGeometry() {
        Mesh mesh = this.geometry.getMesh();
        int lodLevel = this.geometry.getLodLevel();

        Renderer renderer = this.renderManager.getRenderer();


        if (this.geometry instanceof InstancedGeometry) {
            InstancedGeometry instGeom = (InstancedGeometry) this.geometry;
            int numInstances = instGeom.getActualNumInstances();
            if (numInstances == 0) {
                return;
            }
            if (this.renderManager.getRenderer().getCaps().contains(Caps.MeshInstancing)) {
                renderer.renderMesh(mesh, lodLevel, numInstances, instGeom.getAllInstanceData());
            } else {
                throw new RendererException("Mesh instancing is not supported by the video hardware");
            }
        } else {
            renderer.renderMesh(mesh, lodLevel, 1, null);
        }
    }

    protected void resetUniformsNotSetByCurrent(Shader shader) {
        ListMap<String, Uniform> uniforms = shader.getUniformMap();
        int size = uniforms.size();
        for (int i = 0; i < size; i++) {
            Uniform u = uniforms.getValue(i);
            if (!u.isSetByCurrentMaterial()) {
                if (u.getName().charAt(0) != 'g') {
                    // Don't reset world globals!
                    // The benefits gained from this are very minimal
                    // and cause lots of matrix -> FloatBuffer conversions.
                    u.clearValue();
                }
            }
        }
    }

    protected void clearUniformsSetByCurrent(Shader shader) {
        ListMap<String, Uniform> uniforms = shader.getUniformMap();
        int size = uniforms.size();
        for (int i = 0; i < size; i++) {
            Uniform u = uniforms.getValue(i);
            u.clearSetByCurrentMaterial();
        }
    }

    protected ColorRGBA getAmbientColor(boolean removeLights) {
        ColorRGBA ambientLightColor = new ColorRGBA(0f, 0f, 0f, 1f);
        LightList lightList = renderManager.getFilteredLightList();

        for (int j = 0; j < lightList.size(); j++) {
            Light l = lightList.get(j);
            if (l instanceof AmbientLight) {
                ambientLightColor.addLocal(l.getColor());
                if(removeLights){
                    lightList.remove(l);
                }
            }
        }
        ambientLightColor.a = 1.0f;

        return ambientLightColor;
    }


}