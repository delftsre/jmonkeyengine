package com.jme3.scene;

import org.junit.Test;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.VertexBuffer.Format;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.VertexBuffer.Usage;
import com.jme3.scene.mesh.*;
import com.jme3.util.BufferUtils;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Random;

/**
 * Verifies that the class Mesh is working correctly
 * 
 * @author Enrique Correa
 */
public class MeshTest {
  public Mesh generateMesh(int numVertices) {
    Random randomGenerator = new Random();
    Mesh mesh = new Mesh();
    Vector3f [] vertices = new Vector3f[numVertices];
    vertices[0] = new Vector3f(0,0,0);
    for (int i = 1; i < numVertices ; i++) {
      vertices[i] = new Vector3f(
        randomGenerator.nextInt(100),
        randomGenerator.nextInt(100),
        randomGenerator.nextInt(100)
      );
    }
    Vector2f[] texCoord = new Vector2f[4];
    texCoord[0] = new Vector2f(0,0);
    texCoord[1] = new Vector2f(1,0);
    texCoord[2] = new Vector2f(0,1);
    texCoord[3] = new Vector2f(1,1);
    int [] indexes = { 2, 0, 1, 1, 3, 2 };
    mesh.setBuffer(Type.Position, 3, BufferUtils.createFloatBuffer(vertices));
    mesh.setBuffer(Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoord));
    mesh.setBuffer(Type.Index,    3, BufferUtils.createIntBuffer(indexes));
    mesh.updateBound();
    return mesh;
  }

  @Test
  public void testVertexCount() {
    Mesh mesh = generateMesh(3);
    assert mesh.getVertexCount() == 3;
    mesh = generateMesh(4);
    assert mesh.getVertexCount() == 4;
  }
  @Test
  public void testComputeNumElements() {
    Mesh mesh = generateMesh(3);
    assert mesh.computeNumElements() == 1;
    mesh = generateMesh(4);
    mesh.setMode(Mesh.Mode.Lines);
    assert mesh.computeNumElements() == 2;
    mesh.setMode(Mesh.Mode.LineStrip);
    assert mesh.computeNumElements() == 3;
    mesh.setMode(Mesh.Mode.TriangleFan);
    assert mesh.computeNumElements() == 2;
    mesh.setMode(Mesh.Mode.Patch);
    mesh.setPatchVertexCount(1);
    assert mesh.computeNumElements() == 4;
    mesh.setPatchVertexCount(2);
    assert mesh.computeNumElements() == 2;
    mesh.setPatchVertexCount(3);
    assert mesh.computeNumElements() == 1;
  }
}
