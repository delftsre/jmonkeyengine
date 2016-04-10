package com.jme3.effect;

import com.jme3.material.Material;
import com.jme3.math.Matrixable;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import org.junit.Before;
import org.junit.Test;

import com.jme3.scene.Mesh;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class ParticleEmitterTest {

    private ParticleEmitter particleEmitter;
    private ParticleMesh mockedParticlePointMesh;

    @Before
    public void setUp() {
        mockedParticlePointMesh = mock(ParticlePointMesh.class);
        particleEmitter = new ParticleEmitter("test", mockedParticlePointMesh, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMeshNull() {
        particleEmitter.setMesh(null);
    }

    @Test
    public void testSetMesh() {
        Mesh mockedMesh = mock(ParticleTriMesh.class);
        particleEmitter.setMesh(mockedMesh);
        assertEquals(mockedMesh, particleEmitter.getMesh());
    }

    @Test
    public void testClone() {
        ParticleEmitter clone = particleEmitter.clone();
        assertEquals(mockedParticlePointMesh, clone.getMesh());
    }

    @Test
    public void testSetMaterial() {
        Material mockedMaterial = mock(Material.class);
        particleEmitter.setMaterial(mockedMaterial);
        assertEquals(mockedMaterial, particleEmitter.getMaterial());
    }

    @Test
    public void testRenderFromControl() {
        RenderManager mockedRenderManager = mock(RenderManager.class);
        ViewPort mockedViewPort = mock(ViewPort.class);
        Camera mockedCamera = mock(Camera.class);
        Material mockedMaterial = mock(Material.class);

        when(mockedViewPort.getCamera()).thenReturn(mockedCamera);

        particleEmitter.setMaterial(mockedMaterial);

        ParticleEmitter.ParticleEmitterControl particleEmitterControl = new ParticleEmitter.ParticleEmitterControl(particleEmitter);
        particleEmitterControl.render(mockedRenderManager, mockedViewPort);

        verify(mockedParticlePointMesh).setQuadraticFloat(mockedCamera, mockedMaterial);
    }

}