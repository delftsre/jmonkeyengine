package com.jme3.renderer.geometryrenderers;

import com.jme3.light.*;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.scene.Geometry;
import com.jme3.shader.Shader;
import com.jme3.shader.Uniform;
import com.jme3.shader.VarType;
import com.jme3.util.TempVars;

import java.util.logging.Logger;

public class SinglePassGeometryRenderer extends GeometryRenderer {

    private static final Logger logger = Logger.getLogger(SinglePassGeometryRenderer.class.getName());

    public SinglePassGeometryRenderer(Geometry g, RenderManager rm) {
        super(g, rm);
    }

    @Override
    public void renderForLighting() {

        int nbRenderedLights = 0;

        Shader shader = this.geometry.getMaterial().getActiveTechnique().getShader();
        Renderer renderer = this.renderManager.getRenderer();

        LightList ll = this.renderManager.getFilteredLightList();

        resetUniformsNotSetByCurrent(shader);
        if (ll.size() == 0) {
            updateLightListUniforms(this.renderManager.getSinglePassLightBatchSize(), 0);
            renderer.setShader(shader);
            renderMeshFromGeometry();
        } else {
            while (nbRenderedLights < ll.size()) {
                nbRenderedLights = updateLightListUniforms(this.renderManager.getSinglePassLightBatchSize(), nbRenderedLights);
                renderer.setShader(shader);
                renderMeshFromGeometry();
            }
        }

        resetUniformsNotSetByCurrent(shader);
        renderer.setShader(shader);

        renderMeshFromGeometry();

    }

    /**
     * Uploads the lights in the light list as two uniform arrays.<br/><br/> *
     * <p>
     * <code>uniform vec4 g_LightColor[numLights];</code><br/> //
     * g_LightColor.rgb is the diffuse/specular color of the light.<br/> //
     * g_Lightcolor.a is the type of light, 0 = Directional, 1 = Point, <br/> //
     * 2 = Spot. <br/> <br/>
     * <code>uniform vec4 g_LightPosition[numLights];</code><br/> //
     * g_LightPosition.xyz is the position of the light (for point lights)<br/>
     * // or the direction of the light (for directional lights).<br/> //
     * g_LightPosition.w is the inverse radius (1/r) of the light (for
     * attenuation) <br/> </p>
     */
    /* Test */ public int updateLightListUniforms(int numLights, int startIndex) {

        Shader shader = this.geometry.getMaterial().getActiveTechnique().getShader();
        LightList lightList = this.renderManager.getFilteredLightList();

        if (numLights == 0) { // this shader does not do lighting, ignore.
            return 0;
        }

        Uniform lightData = shader.getUniform("g_LightData");
        lightData.setVector4Length(numLights * 3);//8 lights * max 3
        Uniform ambientColor = shader.getUniform("g_AmbientLightColor");


        if (startIndex != 0) {
            // apply additive blending for 2nd and future passes
            RenderState additiveLight = new RenderState();
            additiveLight.setBlendMode(RenderState.BlendMode.AlphaAdditive);
            additiveLight.setDepthWrite(false);

            this.renderManager.getRenderer().applyRenderState(additiveLight);
            ambientColor.setValue(VarType.Vector4, ColorRGBA.Black);
        }else{
            int oldAmountOfLights = lightList.size();
            ambientColor.setValue(VarType.Vector4, this.getAmbientColor(true));
        }

        int lightDataIndex = 0;
        TempVars vars = TempVars.get();
        Vector4f tmpVec = vars.vect4f1;
        int curIndex;
        int endIndex = numLights + startIndex;
        for (curIndex = startIndex; curIndex < endIndex && curIndex < lightList.size(); curIndex++) {
            Light l = lightList.get(curIndex);
            if(l.getType() == Light.Type.Ambient){
                endIndex++;
                continue;
            }
            ColorRGBA color = l.getColor();
            //Color
            lightData.setVector4InArray(color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    l.getType().getId(),
                    lightDataIndex);
            lightDataIndex++;

            switch (l.getType()) {
                case Directional:
                    DirectionalLight dl = (DirectionalLight) l;
                    Vector3f dir = dl.getDirection();
                    //Data directly sent in view space to avoid a matrix mult for each pixel
                    tmpVec.set(dir.getX(), dir.getY(), dir.getZ(), 0.0f);
                    this.renderManager.getCurrentCamera().getViewMatrix().mult(tmpVec, tmpVec);
//                        tmpVec.divideLocal(tmpVec.w);
//                        tmpVec.normalizeLocal();
                    lightData.setVector4InArray(tmpVec.getX(), tmpVec.getY(), tmpVec.getZ(), -1, lightDataIndex);
                    lightDataIndex++;
                    //PADDING
                    lightData.setVector4InArray(0,0,0,0, lightDataIndex);
                    lightDataIndex++;
                    break;
                case Point:
                    PointLight pl = (PointLight) l;
                    Vector3f pos = pl.getPosition();
                    float invRadius = pl.getInvRadius();
                    tmpVec.set(pos.getX(), pos.getY(), pos.getZ(), 1.0f);
                    this.renderManager.getCurrentCamera().getViewMatrix().mult(tmpVec, tmpVec);
                    //tmpVec.divideLocal(tmpVec.w);
                    lightData.setVector4InArray(tmpVec.getX(), tmpVec.getY(), tmpVec.getZ(), invRadius, lightDataIndex);
                    lightDataIndex++;
                    //PADDING
                    lightData.setVector4InArray(0,0,0,0, lightDataIndex);
                    lightDataIndex++;
                    break;
                case Spot:
                    SpotLight sl = (SpotLight) l;
                    Vector3f pos2 = sl.getPosition();
                    Vector3f dir2 = sl.getDirection();
                    float invRange = sl.getInvSpotRange();
                    float spotAngleCos = sl.getPackedAngleCos();
                    tmpVec.set(pos2.getX(), pos2.getY(), pos2.getZ(),  1.0f);
                    this.renderManager.getCurrentCamera().getViewMatrix().mult(tmpVec, tmpVec);
                    // tmpVec.divideLocal(tmpVec.w);
                    lightData.setVector4InArray(tmpVec.getX(), tmpVec.getY(), tmpVec.getZ(), invRange, lightDataIndex);
                    lightDataIndex++;

                    //We transform the spot direction in view space here to save 5 varying later in the lighting shader
                    //one vec4 less and a vec4 that becomes a vec3
                    //the downside is that spotAngleCos decoding happens now in the frag shader.
                    tmpVec.set(dir2.getX(), dir2.getY(), dir2.getZ(),  0.0f);
                    this.renderManager.getCurrentCamera().getViewMatrix().mult(tmpVec, tmpVec);
                    tmpVec.normalizeLocal();
                    lightData.setVector4InArray(tmpVec.getX(), tmpVec.getY(), tmpVec.getZ(), spotAngleCos, lightDataIndex);
                    lightDataIndex++;
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown type of light: " + l.getType());
            }
        }
        vars.release();
        //Padding of unsued buffer space
        while(lightDataIndex < numLights * 3) {
            lightData.setVector4InArray(0f, 0f, 0f, 0f, lightDataIndex);
            lightDataIndex++;
        }
        return curIndex;
    }

}
