package com.jme3.math;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.FloatBuffer;

public class MatrixTest4x4 
{
	float[] data4x4 = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};

	int r = 4, c = 4;
	float[] data;
	
	Matrix matrixIdentity_1;
	Matrix matrixIdentity_2;
	Matrix matrix_1;
	Matrix matrix_2;
	Matrix matrix_copy;
	
	Matrix4f matrixfIdentity_1;
	Matrix4f matrixfIdentity_2;
	Matrix4f matrixf_1;
	Matrix4f matrixf_2;
	Matrix4f matrixf_copy;
	
	Matrix matrix4Identity_1 = new Matrix(4);
	Matrix matrix4Identity_2 = new Matrix(4);
	Matrix matrix4_1 = new Matrix(data4x4);
	Matrix matrix4_2 = new Matrix(data4x4);
	
	Matrix4f matrix4fIdentity1 = new Matrix4f();
	Matrix4f matrix4fIdentity2 = new Matrix4f();
	Matrix4f matrix4f1 = new Matrix4f(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);
	Matrix4f matrix4f2 = new Matrix4f(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);
	Matrix4f matrix4fcopy = new Matrix4f(matrixf_1);
	
	boolean test4x4 = true;
	
	@Before
	public void setUp() throws Exception 
	{
			matrix_1 = matrix4_1;
			matrix_2 = matrix4_1;
			
			matrixIdentity_1 = matrix4Identity_1;
			matrixIdentity_2 = matrix4Identity_2;
			
			matrixf_1 = matrix4f1;
			matrixf_2 = matrix4f1;
			
			matrixfIdentity_1 = matrix4fIdentity1;
			matrixfIdentity_2 = matrix4fIdentity2;
	
			matrixf_copy = matrix4fcopy;
			
			data = data4x4;
	}
	
	@Test
	public void testMatrix() 
	{
		if(!matrixfIdentity_1.toString().equals(matrixIdentity_1.toString()))
		{
			fail("4x4 matrix not equal");
		}
	}
	
	@Test
	public void testMatrixMatrix() 
	{
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("matrix not equal");
		}
	}

	@Test
	public void testMatrixMatrixMatrix() 
	{
		
		matrix_copy = new Matrix(matrix_1);
		matrix4fcopy = new Matrix4f(matrixf_1);
		if(!matrix4fcopy.toString().equals(matrix_copy.toString()))
		{
			fail("matrix not equal copy failed");
		}
	}

	@Test
	public void testget() throws IOException 
	{
		float[] matrixArray1 = new float[16];
		float[] matrixArray2 = new float[16];

		matrix_1.get(matrixArray1);
		matrixf_1.get(matrixArray2);		

		for (int i = 0; i < matrixArray2.length; i++)
		{
			if(matrixArray1[i] != matrixArray2[i])
			{
				fail("get failed");
			}
		}
	}

	@Test
	public void testgetgettrue() throws IOException 
	{
		float[] matrixArray1 = new float[16];
		float[] matrixArray2 = new float[16];

		matrix_1.get(matrixArray1, true);
		matrixf_1.get(matrixArray2, true);		

		for (int i = 0; i < matrixArray2.length; i++)
		{
			if(matrixArray1[i] != matrixArray2[i])
			{
				fail("get failed");
			}
		}
	}

	@Test
	public void testgetgetfalse() throws IOException 
	{
		float[] matrixArray1 = new float[16];
		float[] matrixArray2 = new float[16];

		matrix_1.get(matrixArray1, false);
		matrixf_1.get(matrixArray2, false);			

		for (int i = 0; i < matrixArray2.length; i++)
		{
			if(matrixArray1[i] != matrixArray2[i])
			{
				fail("get failed");
			}
		}
	}

	@Test
	public void testgetgetget() 
	{
		if(matrix_1.get(2, 2) != matrixf_1.get(2, 2))
		{
			fail("getgetget fail");
		}
	}

	@Test
	public void testgetColumn() 
	{
		float[] store1 = matrix_1.getColumn(2);
		float[] store2 = matrixf_1.getColumn(2);
		for (int i = 0; i < store1.length; i++)
		{
			if(store1[i] != store2[i])
			{
				fail("fail");
			}
		}
	}
	
	@Test
	public void testgetColumngetColumn() 
	{
		float[] store1 = new float[data.length];
		matrix_1.getColumn(3,store1);
		
		float[] store2 = new float[data.length];
		matrixf_1.getColumn(3,store2);
		
		for (int i = 0; i < store1.length; i++)
		{
			if(store1[i] != store2[i])
			{
				fail("fail");
			}
		}
	}
	
	@Test
	public void testsetColumn() 
	{
		float[] store1 = {3,4,7,2};
		matrix_1.setColumn(3,store1);
		
		float[] store2 = {3,4,7,2};
		matrixf_1.setColumn(3,store2);
		
		float[] store1Get = new float[data.length];
		matrix_1.getColumn(3,store1);
		
		float[] store2Get = new float[data.length];
		matrixf_1.getColumn(3,store2);
		
		for (int i = 0; i < store1Get.length; i++)
		{
			if(store1Get[i] != store2Get[i])
			{
				fail("fail");
			}
		}
	}
	
	@Test
	public void testset1() 
	{
		int r = 1, c = 0;
		matrix_1.set(r, c, 7);
		matrixf_1.set(r, c, 7);
		
		if(matrix_1.get(r, c) != matrixf_1.get(r, c))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testset2() 
	{
		matrix_1.set(matrixIdentity_1.matrix);
		matrixf_1.set(matrixIdentity_1.matrix);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testset3() 
	{
		matrix_1.set(matrixIdentity_1);
		matrixf_1.set(matrixfIdentity_1);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testset4() 
	{
		float[] values = {5,5,5,5,5,5,5,8,4,4,15,2,1,7,2,3};
		matrixIdentity_1.set(values);
		matrixfIdentity_1.set(values);
		
		if(!matrixIdentity_1.toString().equals(matrixfIdentity_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testset6true() 
	{
		float[] values = {5,5,5,5,5,5,5,8,4,4,15,2,1,7,2,3};
		matrixIdentity_1.set(values, true);
		matrixfIdentity_1.set(values, true);
		
		if(!matrixIdentity_1.toString().equals(matrixfIdentity_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testset6false() 
	{
		float[] values = {5,5,5,5,5,5,5,8,4,4,15,2,1,7,2,3};
		matrixIdentity_1.set(values, false);
		matrixfIdentity_1.set(values, false);
		
		if(!matrixIdentity_1.toString().equals(matrixfIdentity_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtranspose() 
	{
		Matrix test1 = matrixIdentity_1.transpose(r, c);
		Matrix4f test2 = matrixfIdentity_1.transpose();
		
		if(!test1.toString().equals(test2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtransposeLocal() 
	{
		Matrix test1 = matrix_1.transposeLocal();
		Matrix4f test2 = matrixf_1.transposeLocal();
		
		if(!test1.toString().equals(test2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtoFloatBuffer1() 
	{
		FloatBuffer fb1 = matrix_1.toFloatBuffer();
		FloatBuffer fb2 = matrixf_1.toFloatBuffer();

		if(fb1.compareTo(fb2) != 0)
		{
			fail("fail");
		}
		
	}
	
	@Test
	public void testtoFloatBuffer2true() 
	{
		FloatBuffer fb1 = matrix_1.toFloatBuffer(true);
		FloatBuffer fb2 = matrixf_1.toFloatBuffer(true);

		if(fb1.compareTo(fb2) != 0)
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtoFloatBuffer2false() 
	{
		FloatBuffer fb1 = matrix_1.toFloatBuffer(false);
		FloatBuffer fb2 = matrixf_1.toFloatBuffer(false);
		
		if(fb1.compareTo(fb2) != 0)
		{
			fail("fail");
		}
	}
	
	@Test
	public void testfillFloatBuffer1() 
	{
		FloatBuffer fb1 = FloatBuffer.allocate(99999999);
		fb1 = matrix_1.fillFloatBuffer(fb1);
		
		FloatBuffer fb2 = FloatBuffer.allocate(99999999);
		fb2 = matrixf_1.fillFloatBuffer(fb2);
		
		if(fb1.compareTo(fb2) != 0)
		{
			fail("fail");
		}
	}
	
	@Test
	public void testfillFloatBuffer2true() 
	{
		float[] array1 = new float[data.length];
		FloatBuffer fb1 = FloatBuffer.allocate(99999999);
		fb1 = matrix_1.fillFloatBuffer(fb1, true);
		
		float[] array2 = new float[data.length];
		FloatBuffer fb2 = FloatBuffer.allocate(99999999);
		fb2 = matrixf_1.fillFloatBuffer(fb2, true);
		
		array1= fb1.array();
		array2= fb2.array();
		
		
		for(int i = 0; i < data.length; i++)
		{
			if( array1[i] != array2[i] )
			{
				fail("fail");
			}
		}
	}
	@Test
	public void testfillFloatBuffer2false() 
	{
		float[] array1 = new float[data.length];
		FloatBuffer fb1 = FloatBuffer.allocate(99999999);
		fb1 = matrix_1.fillFloatBuffer(fb1, false);
		
		float[] array2 = new float[data.length];
		FloatBuffer fb2 = FloatBuffer.allocate(99999999);
		fb2 = matrixf_1.fillFloatBuffer(fb2, false);
		
		array1= fb1.array();
		array2= fb2.array();
		
		
		for(int i = 0; i < data.length; i++)
		{
			if( array1[i] != array2[i] )
			{
				fail("fail");
			}
		}
	}
	
	@Test
	public void testfillFloatArraytrue() 
	{
		float[] array1 = new float[data.length];
		float[] array2 = new float[data.length];
		
		matrix_1.fillFloatArray(array1, true);
		matrixf_1.fillFloatArray(array2, true);
		
		for(int i = 0; i < data.length; i++)
		{
			if(array1[i] != array2[i])
			{
				fail("fail");
			}
		}
	}
	
	@Test
	public void testfillFloatArrayfalse() 
	{
		float[] array1 = new float[data.length];
		float[] array2 = new float[data.length];
		
		matrix_1.fillFloatArray(array1, false);
		matrixf_1.fillFloatArray(array2, false);
		
		for(int i = 0; i < data.length; i++)
		{
			if(array1[i] != array2[i])
			{
				fail("fail");
			}
		}
	}
	
	@Test
	public void testreadFloatBuffer1() 
	{
		FloatBuffer fb1 = FloatBuffer.allocate(99999999);
		fb1 = matrix_1.fillFloatBuffer(fb1, false);
		
		FloatBuffer fb2 = FloatBuffer.allocate(99999999);
		fb2 = matrixf_1.fillFloatBuffer(fb2, false);
		
		matrixIdentity_1.readFloatBuffer(fb1);
		matrixfIdentity_1.readFloatBuffer(fb2);
		
		//TODO works but there matrix4f doesn't work
		
		if(!matrixIdentity_1.toString().equals(matrixfIdentity_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testreadFloatBuffer2true() 
	{
		FloatBuffer fb1 = FloatBuffer.allocate(99999999);
		fb1 = matrix_1.fillFloatBuffer(fb1, true);
		
		FloatBuffer fb2 = FloatBuffer.allocate(99999999);
		fb2 = matrixf_1.fillFloatBuffer(fb2, true);
		
		matrixIdentity_1.readFloatBuffer(fb1, true);
		matrixfIdentity_1.readFloatBuffer(fb2, true);
		
		//TODO works but there matrix4f doesn't work
		
		if(!matrixIdentity_1.toString().equals(matrixfIdentity_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testreadFloatBuffer2false() 
	{
		FloatBuffer fb1 = FloatBuffer.allocate(99999999);
		fb1 = matrix_1.fillFloatBuffer(fb1, true);
		
		FloatBuffer fb2 = FloatBuffer.allocate(99999999);
		fb2 = matrixf_1.fillFloatBuffer(fb2, true);
		
		matrixIdentity_1.readFloatBuffer(fb1, false);
		matrixfIdentity_1.readFloatBuffer(fb2, false);
		
		//TODO works but there matrix4f doesn't work
		
		if(!matrixIdentity_1.toString().equals(matrixfIdentity_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testloadIdentity() 
	{
		Matrix matrix3 = new Matrix(3);
		Matrix matrix4 = new Matrix(4);
		Matrix4f matrix4f = new Matrix4f();
		Matrix3f matrix3f = new Matrix3f();

		if(!matrix3.toString().equals(matrix3f.toString()))
		{
			fail("3x3 matrix not equal");
		}

		if(!matrix4.toString().equals(matrix4f.toString()))
		{
			fail("4x4 matrix not equal");
		}

	}
	
	@Test
	public void testfromFrustumtrue() 
	{
		matrix_1.fromFrustum(3, 4, 5, 4, 3, 7, true);
		matrixf_1.fromFrustum(3, 4, 5, 4, 3, 7, true);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testfromFrustumfalse() 
	{
		matrix_1.fromFrustum(3, 4, 5, 4, 3, 7, false);
		matrixf_1.fromFrustum(3, 4, 5, 4, 3, 7, false);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testfromAngleAxis() 
	{
		Vector3f vector3f = new Vector3f(3,5,2);
		matrix_1.fromAngleAxis(3, vector3f);
		matrixf_1.fromAngleAxis(3, vector3f);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testfromAngleNormalAxis() 
	{
		Vector3f vector3f = new Vector3f(3,5,2);
		matrix_1.fromAngleNormalAxis(3, vector3f);
		matrixf_1.fromAngleNormalAxis(3, vector3f);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmultLocal() 
	{
		matrix_1.multLocal(34);
		matrixf_1.multLocal(34);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmultLocal2() 
	{
		matrix_1.multLocal(matrix_1);
		matrixf_1.multLocal(matrixf_1);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmultLocal3() throws IOException 
	{
		Quaternion rotation = new Quaternion(200,9,4,6);
		matrix_1.multLocal(rotation);
		matrixf_1.multLocal(rotation);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmult1() 
	{
		Matrix out1 = matrix_1.mult(4);
		Matrix4f out2 = matrixf_1.mult(4);
		
		if(!out1.toString().equals(out2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmult2() 
	{
		Matrix out1 = new Matrix(4);
		Matrix4f out2 = new Matrix4f();
		
		matrix_1.mult((float) 7.0, out1);
		matrixf_1.mult((float)7.0, out2);
		
		if(!out1.toString().equals(out2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmult3() 
	{
		Matrix out1 = new Matrix(4);
		Matrix4f out2 = new Matrix4f();
		
		out1 = matrix_1.mult(matrix_1);
		out2 = matrixf_1.mult(matrixf_1);
		
		if(!out1.toString().equals(out2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmult4() 
	{
		Matrix store1 = new Matrix(4);
		Matrix4f store2 = new Matrix4f();
		store1.zero();
		store2.zero();
		matrix_1.mult(matrix_1,store1);
		matrixf_1.mult(matrixf_1, store2);
		
		if(!store1.toString().equals(store2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmult5() 
	{
		Vector3f vector3f = new Vector3f(3,5,2);
		Vector3f vector3f1 = matrix_1.mult(vector3f);
		Vector3f vector3f2 = matrixf_1.mult(vector3f);
		
		if(!vector3f1.equals(vector3f2))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmult6() 
	{
		Vector3f vector3f = new Vector3f(3,5,2);
		Vector3f vector3f1 = matrix_1.mult(vector3f);
		Vector3f vector3f2 = matrixf_1.mult(vector3f);
		
		if(!vector3f1.equals(vector3f2))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmult7() 
	{
		Vector4f vector4f = new Vector4f(3,5,2,7);
		Vector4f vector4f1 = matrix_1.mult(vector4f);
		Vector4f vector4f2 = matrixf_1.mult(vector4f);
		
		if(!vector4f1.equals(vector4f2))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmult8() 
	{
		Vector4f vector4f = new Vector4f(3,5,2,7);
		Vector4f vector4f1 = matrix_1.mult(vector4f);
		Vector4f vector4f2 = matrixf_1.mult(vector4f);
		
		if(!vector4f1.equals(vector4f2))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmult9() 
	{
		Quaternion quaternion = new Quaternion(4,5,6,8);
		Quaternion store1 = new Quaternion();
		Quaternion store2 = new Quaternion();
		
		matrix_1.mult(quaternion, store1);
		matrixf_1.mult(quaternion, store2);
		
		if(!store1.equals(store2))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmult10() 
	{		
		float[] vec4f1 = {5,4,9,6};
		float[] vec4f2 = {5,4,9,6};
		
		matrix_1.mult(vec4f1);
		matrixf_1.mult(vec4f2);
		
		
        for(int i = 0; i < vec4f1.length;i++)
        {
			if(vec4f1[i] != vec4f2[i])
			{
				fail("fail");
			}
        }
	}
	
	@Test
	public void testmultAcross1() 
	{
		Vector4f vector4f = new Vector4f(3,5,2,7);
		
		Vector4f store1 = new Vector4f();
		Vector4f store2 = new Vector4f();
		
		matrix_1.multAcross(vector4f, store1);
		matrixf_1.multAcross(vector4f, store2);

		
		if(!store1.equals(store2))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmultAcross2() 
	{
		Vector4f vector4f = new Vector4f(3,5,2,7);
		Vector4f vector4f1 = matrix_1.multAcross(vector4f);
		Vector4f vector4f2 = matrixf_1.multAcross(vector4f);
		
		if(!vector4f1.equals(vector4f2))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmultAcross3() 
	{
		Vector3f vector3f = new Vector3f(3,5,2);
		Vector3f store1 = new Vector3f();
		Vector3f store2 = new Vector3f();
		
		Vector3f vector3f1 = matrix_1.multAcross(vector3f, store1);
		Vector3f vector3f2 = matrixf_1.multAcross(vector3f, store2);
		
		if(!vector3f1.equals(vector3f2))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmultAcross4() 
	{
		float[] vec4f1 = {5,4,9,6};
		float[] vec4f2 = {5,4,9,6};
		
		matrix_1.multAcross(vec4f1);
		matrixf_1.multAcross(vec4f2);
		
		
        for(int i = 0; i < vec4f1.length;i++)
        {
			if(vec4f1[i] != vec4f2[i])
			{
				fail("fail");
			}
        }
	}
	
	@Test
	public void testmultNormal() 
	{
		Vector3f vector3f = new Vector3f(3,5,7);
		
		Vector3f store1 = new Vector3f();
		Vector3f store2 = new Vector3f();
		
		matrix_1.multNormal(vector3f, store1);
		matrixf_1.multNormal(vector3f, store2);
		
		if(!store1.equals(store2))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmultNormalAcross() 
	{
		Vector3f vector3f = new Vector3f(3,5,7);
		
		Vector3f store1 = new Vector3f();
		Vector3f store2 = new Vector3f();
		
		matrix_1.multNormalAcross(vector3f, store1);
		matrixf_1.multNormalAcross(vector3f, store2);
		
		if(!store1.equals(store2))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testmultProj() 
	{
		Vector3f vector3f = new Vector3f(3,5,7);
		
		Vector3f store1 = new Vector3f();
		Vector3f store2 = new Vector3f();
		
		float value1 = matrix_1.multProj(vector3f, store1);
		float value2 = matrixf_1.multProj(vector3f, store2);
		
		if(!store1.equals(store2))
		{
			fail("fail");
		}
		
		if(value1 != value2)
		{
			fail("fail");
		}
	}
	
	@Test
	public void testinvert1() 
	{
		Matrix store1 = new Matrix(4);
		Matrix4f store2 = new Matrix4f();
		store1 = matrix_1.invert();
		store2 = matrixfIdentity_1.invert();
		
		//TODO matrix4f incorrect
		
		if(!store1.toString().equals(store2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testinvert2() 
	{
		float[] iverse = {4,0,0,0,0,0,2,0,0,1,2,0,1,0,0,1};
		Matrix iverseM = new Matrix(iverse);
		Matrix4f iverseMf4 = new Matrix4f(4,0,0,0,0,0,2,0,0,1,2,0,1,0,0,1);
		
		Matrix store1 = new Matrix(4);
		Matrix4f store2 = new Matrix4f();
		
		iverseM.invert(store1);
		iverseMf4.invert(store2);
		
		//TODO matrix4f incorrect
		
		if(!store1.toString().equals(store2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testinvertLocal() 
	{
		float[] iverse = {4,0,0,0,0,0,2,0,0,1,2,0,1,0,0,1};
		Matrix iverseM = new Matrix(iverse);
		Matrix4f iverseMf4 = new Matrix4f(4,0,0,0,0,0,2,0,0,1,2,0,1,0,0,1);
		
		iverseM.invertLocal();
		iverseMf4.invertLocal();
		
		//TODO matrix4f incorrect
		
		if(!iverseM.toString().equals(iverseMf4.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testadjoint1() 
	{
		float[] ajointdata = {5,-2,2,7,1,0,0,3,-3,1,5,0,3,-1,-9,4};
		Matrix adjoint1 = new Matrix(ajointdata);
		Matrix4f adjoint2 = new Matrix4f(5,-2,2,7,1,0,0,3,-3,1,5,0,3,-1,-9,4);
		
		Matrix matrix = adjoint1.adjoint();
		Matrix4f matrix4f = adjoint2.adjoint();
		
		if(!matrix.toString().equals(matrix4f.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testadjoint2() 
	{
		Matrix store1 = new Matrix(4);
		Matrix4f store2 = new Matrix4f();
		
		float[] ajointdata = {5,-2,2,7,1,0,0,3,-3,1,5,0,3,-1,-9,4};
		
		Matrix adjoint1 = new Matrix(ajointdata);
		Matrix4f adjoint2 = new Matrix4f(5,-2,2,7,1,0,0,3,-3,1,5,0,3,-1,-9,4);
		
		adjoint1.adjoint(store1);
		adjoint2.adjoint(store2);
		
		if(!store1.toString().equals(store2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testsetTransform() 
	{
		Vector3f position = new Vector3f(3,6,9);
		Vector3f scale = new Vector3f(8,4,2);
		
		float[] rotMatData = {8,3,6,2,4,6,8,4,3};
		Matrix rotMat1 = new Matrix(rotMatData);
		Matrix3f rotMat2 = new Matrix3f(8,3,6,2,4,6,8,4,3);
		
		matrix_1.setTransform(position, scale, rotMat1);
		matrixf_1.setTransform(position, scale, rotMat2);
		
		
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testdeterminant() 
	{
		float[] determinantData = {2,5,3,5,4,6,6,3,11,3,2,-2,4,-7,9,3};
		Matrix determinant1 = new Matrix(determinantData);
		Matrix4f determinant2 = new Matrix4f(2,5,3,5,4,6,6,3,11,3,2,-2,4,-7,9,3);
		
		float value1 = determinant1.determinant();
		float value2 = determinant2.determinant();
		
		if(value1 != value2)
		{
			fail("fail");
		}
	}
	
	@Test
	public void testzero() 
	{
		if(!matrix_1.zero().toString().equals(matrixf_1.zero().toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testadd() 
	{
		float[] determinantData = {2,5,3,5,4,6,6,3,11,3,2,-2,4,-7,9,3};
		Matrix determinant1 = new Matrix(determinantData);
		Matrix4f determinant2 = new Matrix4f(2,5,3,5,4,6,6,3,11,3,2,-2,4,-7,9,3);
		
		Matrix add1 = matrix_1.add(determinant1);
		Matrix4f add2 = matrixf_1.add(determinant2);
		
		if(!add1.toString().equals(add2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testaddLocal() 
	{
		float[] determinantData = {2,5,3,5,4,6,6,3,11,3,2,-2,4,-7,9,3};
		Matrix determinant1 = new Matrix(determinantData);
		Matrix4f determinant2 = new Matrix4f(2,5,3,5,4,6,6,3,11,3,2,-2,4,-7,9,3);
		
		matrix_1.addLocal(determinant1);
		matrixf_1.addLocal(determinant2);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtoTranslationVector1() 
	{
		Vector3f vector1 = matrix_1.toTranslationVector();
		Vector3f vector2 = matrixf_1.toTranslationVector();
		
		if(!vector1.toString().equals(vector2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtoTranslationVector2() 
	{
		Vector3f vector1 = new Vector3f(3,6,9);
		Vector3f vector2 = new Vector3f(3,6,9);
		matrix_1.toTranslationVector(vector1);
		matrixf_1.toTranslationVector(vector2);
		
		if(!vector1.toString().equals(vector2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtoRotationQuat1() 
	{
		Quaternion quat1 = matrix_1.toRotationQuat();
		Quaternion quat2 = matrixf_1.toRotationQuat();
		
		if(!quat1.toString().equals(quat2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtoRotationQuat2() 
	{
		Quaternion quat1 = new Quaternion();
		Quaternion quat2 = new Quaternion();
		matrix_1.toRotationQuat(quat1);
		matrixf_1.toRotationQuat(quat2);
		
		if(!quat1.toString().equals(quat2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtoRotationMatrix1() 
	{
		Matrix matrix1 = matrix_1.toRotationMatrix();
		Matrix3f matrix2 = matrixf_1.toRotationMatrix();
		
		if(!matrix1.toString().equals(matrix2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtoRotationMatrix2() 
	{
		Matrix matrix1 = new Matrix(4);
		matrix_1.toRotationMatrix(matrix1);
		Matrix3f matrix2 = new Matrix3f();
		matrixf_1.toRotationMatrix(matrix2);
		
		if(!matrix1.toString().equals(matrix2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtoScaleVector1() 
	{
		Vector3f vector1 = matrix_1.toScaleVector();
		Vector3f vector2 = matrixf_1.toScaleVector();
		
		if(!vector1.toString().equals(vector2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtoScaleVector2() 
	{
		Vector3f vector1 = new Vector3f();
		Vector3f vector2 = new Vector3f();
		matrix_1.toScaleVector(vector1);
		matrixf_1.toScaleVector(vector2);
		
		if(!vector1.toString().equals(vector2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testsetScale1() 
	{
		matrix_1.setScale(3,6,9);
		matrixf_1.setScale(3,6,9);
	
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testsetScale2() 
	{
		Vector3f vector = new Vector3f(3,7,1);
		matrix_1.setScale(vector);
		matrixf_1.setScale(vector);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testsetTranslation1() 
	{
		float[] values = {1,5,9};
		matrix_1.setTranslation(values);
		matrixf_1.setTranslation(values);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testsetTranslation2() 
	{
		float[] values = {1,5,9};
		matrix_1.setTranslation(values);
		matrixf_1.setTranslation(values);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testsetTranslation3() 
	{
		Vector3f values = new Vector3f(4,8,2);
		matrix_1.setTranslation(values);
		matrixf_1.setTranslation(values);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testsetInverseTranslation() 
	{
		float[] values = {1,5,9};
		matrix_1.setInverseTranslation(values);
		matrixf_1.setInverseTranslation(values);
		
		//TODO matrix4f doesnt work
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testangleRotation() 
	{
		Vector3f values = new Vector3f(4,8,2);
		matrix_1.angleRotation(values);
		matrixf_1.angleRotation(values);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testsetRotationQuaternion() 
	{
		Quaternion quat = new Quaternion(3,5,8,3);
		matrix_1.setRotationQuaternion(quat);
		matrixf_1.setRotationQuaternion(quat);;
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testsetInverseRotationRadians() 
	{
		float[] values = {20,90,45};
		matrix_1.setInverseRotationRadians(values);
		matrixf_1.setInverseRotationRadians(values);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testsetInverseRotationDegrees() 
	{
		float[] values = {20,90,45};
		matrix_1.setInverseRotationDegrees(values);
		matrixf_1.setInverseRotationDegrees(values);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testinverseTranslateVect1() 
	{
		float[] values1 = {20,90,45};
		float[] values2 = {20,90,45};
		matrix_1.inverseTranslateVect(values1);
		matrixf_1.inverseTranslateVect(values2);
    	for (int i = 0; i < 3; i++)
    	{
    		if(values1[i] != values2[i])
    		{
    			fail("fail");
    		}
    	}
	}
	
	@Test
	public void testinverseTranslateVect2() 
	{
		Vector3f vector1 = new Vector3f(3,7,2);
		Vector3f vector2 = new Vector3f(3,7,2);
		matrix_1.inverseTranslateVect(vector1);
		matrixf_1.inverseTranslateVect(vector2);
		
		if(!vector1.toString().equals(vector1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtranslateVect() 
	{
		Vector3f vector1 = new Vector3f(3,7,2);
		Vector3f vector2 = new Vector3f(3,7,2);
		matrix_1.translateVect(vector1);
		matrixf_1.translateVect(vector2);
		
		if(!vector1.toString().equals(vector1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testinverseRotateVect() 
	{
		Vector3f vector1 = new Vector3f(3,7,2);
		Vector3f vector2 = new Vector3f(3,7,2);
		matrix_1.inverseRotateVect(vector1);
		matrixf_1.inverseRotateVect(vector2);
		
		if(!vector1.toString().equals(vector1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testrotateVect() 
	{
		Vector3f vector1 = new Vector3f(3,7,2);
		Vector3f vector2 = new Vector3f(3,7,2);
		matrix_1.rotateVect(vector1);
		matrixf_1.rotateVect(vector2);
		
		if(!vector1.toString().equals(vector1.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testisIdentity() 
	{

		if(matrix_1.isIdentity() != matrixf_1.isIdentity())
		{
			fail("isIdentity() fail");
		}

		if(matrixIdentity_1.isIdentity() != matrixfIdentity_1.isIdentity())
		{
			fail("isIdentity() fail");
		}
	}

	@Test
	public void testhashCode() 
	{
		float[] data = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		Matrix matrix4 = new Matrix(data);
		Matrix4f matrix4f = new Matrix4f(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);

		if(matrix4.hashCode() != matrix4f.hashCode())
		{
			fail("hashCode fail");
		}
	}	

	@Test
	public void testequals() 
	{
		float[] data = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		Matrix matrix41 = new Matrix(data);
		Matrix matrix42 = new Matrix(data.length);
		Matrix matrix43 = new Matrix(data);
		Matrix matrix44 = new Matrix(data);
		Matrix4f matrixf_1 = new Matrix4f(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);
		Matrix4f matrix4f2 = new Matrix4f();
		Matrix4f matrix4f3 = new Matrix4f();
		Matrix4f matrix4f4 = new Matrix4f();

		if(matrix41.equals(matrix42) != matrixf_1.equals(matrix4f2))
		{
			fail("equals fail");
		}

		if(matrix43.equals(matrix44) != matrix4f3.equals(matrix4f4))
		{
			fail("equals fail");
		}
	}

	@Test
	public void testwrite() throws IOException 
	{
		float[] data = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		Matrix matrix4 = new Matrix(data);
		Matrix4f matrix4f = new Matrix4f(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);

		//TODO fix test
		
		fail("hard to test");
	}

	@Test
	public void testread() throws IOException 
	{
		float[] data = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		Matrix matrix4 = new Matrix(data);
		Matrix4f matrix4f = new Matrix4f(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);
		
		//TODO fix test
		
		fail("hard to test");
	}

	@Test
	public void testscale() throws IOException 
	{
		float[] data1 = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		Matrix matrix4 = new Matrix(data1);
		Matrix4f matrix4f = new Matrix4f(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);

		float[] data2 = {1,2,3,4,5,6,7,8,9};
		Matrix matrix3 = new Matrix(data2);
		Matrix3f matrix3f = new Matrix3f(1,2,3,4,5,6,7,8,9);

		Vector3f vector3f = new Vector3f(3,5,2);

		matrix4.scale(vector3f);
		matrix4f.scale(vector3f);

		matrix3.scale(vector3f);
		matrix3f.scale(vector3f);

		if(!matrix4.toString().equals(matrix4f.toString()))
		{
			fail("4x4 scale fail");
		}

		if(!matrix3.toString().equals(matrix3f.toString()))
		{
			fail("3x3 scale fail");
		}
	}

	@Test
	public void testequalIdentity() throws IOException 
	{
		float[] data = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		Matrix matrix4 = new Matrix(data);
		Matrix4f matrix4f = new Matrix4f(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);

		Matrix matrix4Identity = new Matrix(4);
		Matrix4f matrix4fIdentity = new Matrix4f();

		if(!Matrix.equalIdentity(matrix4) == Matrix4f.equalIdentity(matrix4f))
		{
			fail("equalIdentity fail");
		}

		if(!Matrix.equalIdentity(matrix4Identity) == Matrix4f.equalIdentity(matrix4fIdentity))
		{
			fail("equalIdentity fail");
		}
	}

	@Test
	public void testclone() throws IOException 
	{
		Matrix matrix3 = new Matrix(3);
		Matrix matrix4 = new Matrix(4);

		matrix3.clone();
		matrix4.clone();
	}

	@Test
	public void testfromFrame() throws IOException 
	{
		float[] data = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
		Matrix matrix4 = new Matrix(data);
		Matrix4f matrix4f = new Matrix4f(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);

		Vector3f location = new Vector3f(1,5,9);
		Vector3f direction = new Vector3f(2,6,10);
		Vector3f up = new Vector3f(3,7,11);
		Vector3f left = new Vector3f(4,8,12);

		matrix4.fromFrame(location, direction, up, left);
		matrix4f.fromFrame(location, direction, up, left);

		if(!matrix4.toString().equals(matrix4f.toString()))
		{
			fail("fromFrame fail");
		}
	}
}
