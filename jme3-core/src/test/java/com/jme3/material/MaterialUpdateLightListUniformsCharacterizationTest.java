package com.jme3.material;

import com.jme3.light.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix4f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.geometryrenderers.MultiPassGeometryRenderer;
import com.jme3.renderer.geometryrenderers.SinglePassGeometryRenderer;
import com.jme3.renderer.opengl.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.shader.Shader;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class MaterialUpdateLightListUniformsCharacterizationTest {

    @Test
    public void testUpdateLightListUniforms() {
        // We found this method in the detection of SOLID violations, what we want to do is refactor the switch/case

        // We want to know how this method works, therefore we will try to list calls on mock objects and see how data
        // flows between the various parameters feeded to this material

        Material mat = new Material();

        Shader shader = new Shader(); // this cannot be mocked as it is final
        Geometry geom = mock(Geometry.class, withSettings().verboseLogging());

        RenderManager rm = mock(RenderManager.class, withSettings().verboseLogging());


        SinglePassGeometryRenderer geometryRenderer = new SinglePassGeometryRenderer(geom, rm);

        // We found that if you give a RenderManager with no Renderer, you'll get a NullPointer, so we have to mock that method
        Renderer r = mock(Renderer.class, withSettings().verboseLogging());
        when(rm.getRenderer()).thenReturn(r);
        // We later found out that there is a deep dependency on RenderManager's CurrentCamera's ViewMatrix
        Camera currentCamera = mock(Camera.class, withSettings().verboseLogging());
        when(rm.getCurrentCamera()).thenReturn(currentCamera);
        when(currentCamera.getViewMatrix()).thenReturn(new Matrix4f());


        LightList ll = new LightList(mock(Spatial.class, withSettings().verboseLogging()));// this cannot be mocked as it is final

        // We apparently need a mapping between LightType and a concrete instance
        // This is problematic to us, as this means there is a relationship between a magic type enum and a class
        // That enum is inflexible

        // The different types of lights are distinct enough to not be the same class, to test them all we will
        // mock them individually
        AmbientLight al = mock(AmbientLight.class, withSettings().verboseLogging());
        when(al.getColor()).thenReturn(new ColorRGBA(0,0,0,1.0f)); // A light has to return its color
        when(al.getType()).thenReturn(Light.Type.Ambient); // A light has to return its type, this is part of the problem
        ll.add(al);

        DirectionalLight dl = mock(DirectionalLight.class, withSettings().verboseLogging());
        when(dl.getColor()).thenReturn(new ColorRGBA(0,0,0,1.0f));
        when(dl.getType()).thenReturn(Light.Type.Directional);
        when(dl.getDirection()).thenReturn(new Vector3f(0.0f, 0.0f, 0.0f)); // A directional light needs a direction
        ll.add(dl);

        PointLight pl = mock(PointLight.class, withSettings().verboseLogging());
        when(pl.getColor()).thenReturn(new ColorRGBA(0,0,0,1.0f));
        when(pl.getType()).thenReturn(Light.Type.Point);
        when(pl.getPosition()).thenReturn(new Vector3f(0.0f, 0.0f, 0.0f)); // A point light needs a position
        ll.add(pl);

        SpotLight sl = mock(SpotLight.class, withSettings().verboseLogging());
        when(sl.getColor()).thenReturn(new ColorRGBA(0,0,0,1.0f));
        when(sl.getType()).thenReturn(Light.Type.Spot);
        when(sl.getPosition()).thenReturn(new Vector3f(0.0f, 0.0f, 0.0f)); // A spot light needs a position
        when(sl.getDirection()).thenReturn(new Vector3f(0.0f, 0.0f, 0.0f)); // A spot light also needs a direction
        ll.add(sl);

        // With the other created characterization test we found out that shaders initialize is to be called
        shader.initialize();

        // This method changes its shader parameter
        System.out.println(shader.getUniformMap());


        System.out.println(shader.getUniformMap());

        //geometryRenderer.updateLightListUniforms(6, 0);
    }
}
