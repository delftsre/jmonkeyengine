package com.jme3.math;

import static org.junit.Assert.*;

import java.nio.FloatBuffer;

import org.junit.Test;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bounding.BoundingVolume.Type;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.collision.UnsupportedCollisionException;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix4f;
import com.jme3.math.Plane;
import com.jme3.math.Plane.Side;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Transform;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.GeometryList;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.util.BufferUtils;
import com.jme3.util.TempVars;
import com.sun.accessibility.internal.resources.accessibility;
import com.sun.javafx.geom.Vec3f;

import javafx.beans.property.FloatProperty;
import sun.net.www.content.audio.x_aiff;

public class Matrix4fTest {
	float[] numbers = {-1.0f,0.0f,-0.0f,0.0f,0.0f,1.0f,-0.0f,0.0f,0.0f,-0.0f,-1.0f,0.0f,-0.0f,-0.0f,0.0f,1.0f};
	Matrix4f matrix = new Matrix4f(numbers);
	
	@Test
	public void testCopy()
	{
		float[] numbers2 = {3.0f,0.0f,-0.0f,0.0f,0.0f,1.0f,-0.0f,0.0f,0.0f,-0.0f,-1.0f,0.0f,-0.0f,-0.0f,0.0f,1.0f};
		Matrix4f newMatrix = new Matrix4f(numbers2);
		newMatrix.copy(matrix);
		assertTrue(matrix.equals(newMatrix));
	}
	
	@Test
	public void testFromFrame()
	{
		Vector3f location = new Vector3f(1.0f, 2.0f, 3.0f);
		Vector3f direction = new Vector3f(2.0f, 3.0f, 4.0f);
		Vector3f up = new Vector3f(1.0f, 2.0f, 4.0f);
		Vector3f left = new Vector3f(5.0f, 0.0f, 1.0f);
		matrix.fromFrame(location, direction, up, left);
		TempVars vars = TempVars.get();
		assertTrue(vars.vect1.equals(new Vector3f(2.0f,3.0f,4.0f)));
		assertTrue(vars.vect2.equals(new Vector3f(4.0f,-4.0f,1.0f)));
		assertTrue(vars.vect3.equals(new Vector3f(-19.0f,-14.0f,20.0f)));
		//System.out.println(vars.vect3);
	}
	
	@Test
	public void testGetMatrix()
	{
		float[] matrixFloat = {-10.0f,0.0f,-0.0f,0.0f,0.0f,1.0f,-0.0f,0.0f,0.0f,-0.0f,-1.0f,0.0f,-0.0f,-0.0f,0.0f,1.0f};
		matrix.get(matrixFloat);
		for (int i=0; i<matrixFloat.length ; i++)
		{
			assertTrue(matrixFloat[i] == numbers[i]);
		}
		matrix.get(matrixFloat,false);
		assertTrue(matrixFloat[0] == numbers[0]);
		assertTrue(matrixFloat[4] == numbers[1]);
		assertTrue(matrixFloat[8] == numbers[2]);
		assertTrue(matrixFloat[13] == numbers[7]);
		assertTrue(matrixFloat[6] == numbers[9]);
		assertTrue(matrixFloat[3] == numbers[12]);
		assertTrue(matrixFloat[15] == numbers[15]);
        
		//System.out.println(matrixFloat[0]);
	}
	
	
	@Test
	public void testGet()
	{
		for (int i=0; i<=3 ;i++)
			for (int j=0 ; j<=3; j++)
			{
				assertTrue(numbers[i*4+j]==matrix.get(j, i));
			}
	}
	
	@Test
	public void testGetColumn()
	{
		//System.out.println(matrix);
		assertTrue(matrix.getColumn(0)[0] == -1.0f);
		assertTrue(matrix.getColumn(0)[1] == 0.0f);
		assertTrue(matrix.getColumn(0)[2] == -0.0f);
		assertTrue(matrix.getColumn(0)[3] == 0.0f);
		assertTrue(matrix.getColumn(1)[0] == 0.0f);
		assertTrue(matrix.getColumn(1)[1] == 1.0f);
		assertTrue(matrix.getColumn(1)[2] == -0.0f);
		assertTrue(matrix.getColumn(2)[1] == -0.0f);
		assertTrue(matrix.getColumn(2)[2] == -1.0f);
		assertTrue(matrix.getColumn(3)[3] == 1.0f);
	}
	
	@Test
	public void testSetColumn()
	{
		float[] column = {1.0f,2.0f,3.0f,4.0f};
		for (int i=0; i<=3; i++)
		{
			matrix.setColumn(i, column);
			for (int j=0; j<column.length; j++)
			{
				assertTrue(matrix.get(j, i)==column[j]);
			}
		}
	}
	
	@Test
	public void testSet()
	{
		for (int i=0; i<=3; i++)
		{
			for (int j=0; j<=3; j++)
			{
				matrix.set(i, j, i*4+j+2);
				assertTrue(matrix.get(i, j) == (i*4+j+2));
			}
		}
	}
	
	@Test
	public void testSet2()
	{
		float[][] testMatrix = new float[4][4];
		for (int i=0; i<=3; i++)
		{
			for (int j=0; j<=3; j++)
			{
				testMatrix[i][j] = i*4+j+2;
			}
		}
		
		matrix.set(testMatrix);
		for (int i=0; i<=3; i++)
		{
			for (int j=0; j<=3; j++)
			{
				assertTrue(matrix.get(i,j) == testMatrix[i][j]);
			}
		}
	}
	
	@Test
	public void testSet3()
	{

		
		matrix.set(3.0f,4.0f,5.0f,6.0f,7.0f,8.0f,9.0f,10.0f,11.0f,12.0f,13.0f,14.0f,15.0f,16.0f,17.0f,18.0f);
		for (int i=0; i<=3; i++)
		{
			for (int j=0; j<=3; j++)
			{
				assertTrue(matrix.get(i,j) == i*4+j+3);
			}
		}
		
	}
	
	@Test
	public void testSet4()
	{
		float[] floatArray = {3.0f,4.0f,5.0f,6.0f,7.0f,8.0f,9.0f,10.0f,11.0f,12.0f,13.0f,14.0f,15.0f,16.0f,17.0f,18.0f};
		Matrix4f newMatrix = new Matrix4f(floatArray);
		matrix.set(newMatrix);
		assertTrue(matrix.equals(newMatrix));
	}
	
	@Test
	public void testSet5()
	{
		float[] floatArray = {3.0f,4.0f,5.0f,6.0f,7.0f,8.0f,9.0f,10.0f,11.0f,12.0f,13.0f,14.0f,15.0f,16.0f,17.0f,18.0f};
		matrix.set(floatArray,true);
		for (int i=0; i<=3; i++)
		{
			for (int j=0; j<=3; j++)
			{
				assertTrue(matrix.get(i,j) == i*4+j+3);
			}
		}
	}
	
	@Test
	public void testTranspose()
	{
		Matrix4f trans = matrix.transpose();
		for (int i=0; i<=3; i++)
		{
			for (int j=0; j<=3; j++)
			{
				assertTrue(matrix.get(i,j) == trans.get(j, i));
			}
		}
	}
	

	@Test
	public void testTransposeLocal()
	{
		Matrix4f trans = matrix.transposeLocal();
		for (int i=0; i<=3; i++)
		{
			for (int j=0; j<=3; j++)
			{
				assertTrue(matrix.get(i,j) == trans.get(j, i));
			}
		}
	}
	
	@Test
	public void testToFloatBuffer()
	{
		assertTrue(matrix.toFloatBuffer().toString().equals("java.nio.DirectFloatBufferU[pos=0 lim=16 cap=16]"));
	
	}

	@Test
	public void testFillFloatArray()
	{
		float[] floatArray = {3.0f,4.0f,5.0f,6.0f,7.0f,8.0f,9.0f,10.0f,11.0f,12.0f,13.0f,14.0f,15.0f,16.0f,17.0f,18.0f};
		//System.out.println(matrix);
		matrix.fillFloatArray(floatArray, true);
		for (int i=0; i<=3; i++)
		{
			for (int j=0; j<=3; j++)
			{
				assertTrue(matrix.get(j,i) == floatArray[i*4+j]);
			}
		}
	}
	
	@Test
	public void testReadFloatBuffer()
	{
		float[] floatArray = {3.0f,4.0f,5.0f,6.0f,7.0f,8.0f,9.0f,10.0f,11.0f,12.0f,13.0f,14.0f,15.0f,16.0f,17.0f,18.0f};
		FloatBuffer floatBuffer  = FloatBuffer.allocate(16);
		//floatBuffer.put(1.0f);
		//System.out.println(floatBuffer.get());
		matrix.readFloatBuffer(floatBuffer,true);
		//System.out.println(matrix);
		
	}
	
	@Test
	public void testFromFrustum()
	{
		float near = 1.0f;
		float far = 2.0f;
		float left = 3.0f;
		float right = 3.0f;
		float top = 4.0f;
		float bottom = 4.0f;
		matrix.fromFrustum(near, far, left, right, top, bottom, true);
		assertTrue(matrix.get(0,0) == 2.0f / (right - left)); 
		assertTrue(matrix.get(1,1) == 2.0f / (top - bottom)); 
		assertTrue(matrix.get(2,2) == -2.0f / (far - near)); 
		assertTrue(matrix.get(3,3) == 1f); 
		assertTrue(matrix.get(0,3) == -(right + left) / (right - left)); 
		assertTrue(matrix.get(1,3) == -(top + bottom) / (top - bottom)); 
		assertTrue(matrix.get(2,3) ==  -(far + near) / (far - near)); 
		
		matrix.fromFrustum(near, far, left, right, top, bottom, false);
		assertTrue(matrix.get(0,0) == (2.0f * near) / (right - left)); 
		assertTrue(matrix.get(1,1) == (2.0f * near) / (top - bottom));
		assertTrue(matrix.get(3,2) == -1.0f); 
		assertTrue(matrix.get(3,3) == 0.0f); 
		assertTrue(matrix.get(0,2) == (right + left) / (right - left)); 
		assertTrue(matrix.get(1,2) == (top + bottom) / (top - bottom)); 
		assertTrue(matrix.get(2,2) ==  -(far + near) / (far - near)); 
		assertTrue(matrix.get(2,3) ==  -(2.0f * far * near) / (far - near)); 



	}
	
	@Test
	public void testFromAngleNormalAxis()
	{
		Vector3f axis = new Vector3f(0.0f,0.0f,0.0f);
		matrix.fromAngleNormalAxis(0.0f, axis);
		assertTrue(matrix.get(0,0)==1.0f);
		assertTrue(matrix.get(1,1)==1.0f);
		assertTrue(matrix.get(2,2)==1.0f);
		assertTrue(matrix.get(3,3)==1.0f);
	}
	
	@Test
	public void testMultLocal()
	{
		Matrix4f oldMatrix = new Matrix4f(matrix);
		matrix.multLocal(2.0f);;
		for (int i=0; i<=3; i++)
		{
			for (int j=0; j<=3; j++)
			{
				assertTrue(matrix.get(i,j) == (oldMatrix.get(i, j) * 2.0f));
			}
		}
	}
	
	@Test
	public void testMult()
	{
		Matrix4f multMatrix = matrix.mult(4.0f);
		for (int i=0; i<=3; i++)
		{
			for (int j=0; j<=3; j++)
			{
				assertTrue(matrix.get(i,j)*4.0f == (multMatrix.get(i, j)));
			}
		}
		
		Matrix4f testMatrix = new Matrix4f(matrix);
		matrix.mult(4.0f,testMatrix);
		for (int i=0; i<=3; i++)
		{
			for (int j=0; j<=3; j++)
			{
				assertTrue(matrix.get(i,j)*4.0f == (testMatrix.get(i, j)));
			}
		}
		
		float[] testFloats = {4.0f,0.0f,0.0f,0.0f,0.0f,4.0f,0.0f,0.0f,-0.0f,0.0f,4.0f,0.0f,0.0f,-0.0f,0.0f,4.0f};
		Matrix4f resultM = new Matrix4f(testFloats);
		assertTrue(resultM.equals(matrix.mult(testMatrix,testMatrix)));
		
		Vector3f vector = new Vector3f(2.0f,3.0f,4.0f);
		Vector3f store = null;
		assertTrue(matrix.mult(vector, store).equals(new Vector3f(-2.0f, 3.0f, -4.0f)));
		
		Vector4f vector4f = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
		Vector4f store4f = null;
		assertTrue(matrix.mult(vector4f, store4f).equals(new Vector4f(-1.0f, 2.0f, -3.0f, 4.0f)));
	}
	
	@Test
	public void testMultAcross()
	{
		Vector4f vector4f = new Vector4f(1.0f, 2.0f, 3.0f, 4.0f);
		Vector4f store4f = null;
		assertTrue(matrix.multAcross(vector4f, store4f).equals(new Vector4f(-1.0f, 2.0f, -3.0f, 4.0f)));
	}
	
	@Test
	public void testMultNormal()
	{
		Vector3f vector3f = new Vector3f(1.0f, 2.0f, 3.0f);
		Vector3f store3f = null;
		assertTrue(matrix.multNormal(vector3f, store3f).equals(new Vector3f(-1.0f, 2.0f, -3.0f)));
	}
	
	@Test
	public void testMultNormalAcross()
	{
		Vector3f vector3f = new Vector3f(1.0f, 2.0f, 3.0f);
		Vector3f store3f = null;
		assertTrue(matrix.multNormalAcross(vector3f, store3f).equals(new Vector3f(-1.0f, 2.0f, -3.0f)));
	}
	
	@Test
	public void testMultProj()
	{
		Vector3f vector3f = new Vector3f(1.0f, 2.0f, 3.0f);
		Vector3f store3f = new Vector3f(1.0f, 2.0f, 3.0f);
		assertTrue(matrix.multProj(vector3f, store3f)==1.0f);
	}
	
	@Test
	public void testMultAcross2()
	{
		Vector3f vector3f = new Vector3f(1.0f, 2.0f, 3.0f);
		Vector3f store3f = new Vector3f(1.0f, 2.0f, 3.0f);
		assertTrue(matrix.multAcross(vector3f, store3f).equals(new Vector3f(-1.0f,2.0f,-3.0f)));
	}
	
	
	
	
	

}
