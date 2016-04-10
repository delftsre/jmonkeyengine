package com.jme3.effect;

import com.jme3.material.Material;
import com.jme3.math.Matrix;
import com.jme3.renderer.Camera;
import org.junit.Test;

import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ParticlePointMeshTest extends ParticleMeshTest {

    @Test
    public void testSetQuadraticFloat() {
        // Cannot mock Matrix because it is final.
        Matrix matrix = new Matrix(4);
        Camera mockedCamera = mock(Camera.class);
        Material mockedMaterial = mock(Material.class);

        when(mockedCamera.getProjectionMatrix()).thenReturn(matrix);

        ParticlePointMesh particlePointMesh = new ParticlePointMesh();
        particlePointMesh.setQuadraticFloat(mockedCamera, mockedMaterial);

        verify(mockedMaterial).setFloat(eq("Quadratic"), anyFloat());
    }

}
