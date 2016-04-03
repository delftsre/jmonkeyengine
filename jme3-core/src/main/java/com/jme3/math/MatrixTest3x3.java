package com.jme3.math;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.FloatBuffer;

public class MatrixTest3x3 
{
	
	float[] data3x3 = {1,2,3,4,5,6,7,8,9};

	int r = 3, c = 3;
	int MM = 3;
	float[] data;
	
	Matrix matrixIdentity_1;
	Matrix matrixIdentity_2;
	Matrix matrix_1;
	Matrix matrix_2;
	Matrix matrix_copy;
	
	Matrix3f matrixfIdentity_1;
	Matrix3f matrixfIdentity_2;
	Matrix3f matrixf_1;
	Matrix3f matrixf_2;
	Matrix3f matrixf_copy;
	
	Matrix matrix3Identity_1 = new Matrix(MM);
	Matrix matrix3Identity_2 = new Matrix(MM);
	Matrix matrix3_1 = new Matrix(data3x3);
	Matrix matrix3_2 = new Matrix(data3x3);
	
	Matrix3f matrix3fIdentity1 = new Matrix3f();
	Matrix3f matrix3fIdentity2 = new Matrix3f();
	Matrix3f matrix3f1 = new Matrix3f(1,2,3,4,5,6,7,8,9);
	Matrix3f matrix3f2 = new Matrix3f(1,2,3,4,5,6,7,8,9);
	
	boolean test3x3 = false;
	
	@Before
	public void setUp() throws Exception 
	{
			matrix_1 = matrix3_1;
			matrix_2 = matrix3_1;
			
			matrixIdentity_1 = matrix3Identity_1;
			matrixIdentity_2 = matrix3Identity_2;
			
			matrixf_1 = matrix3f1;
			matrixf_2 = matrix3f1;
			
			matrixfIdentity_1 = matrix3fIdentity1;
			matrixfIdentity_2 = matrix3fIdentity2;
	
			data = data3x3;

	}
	
	@Test
	public void testMatrix() 
	{
		if(!matrixfIdentity_1.toString().equals(matrixIdentity_1.toString()))
		{
			fail("fail");
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
		matrixf_copy = new Matrix3f(matrixf_1);
		if(!matrixf_copy.toString().equals(matrix_copy.toString()))
		{
			fail("matrix not equal copy failed");
		}
	}

	@Test
	public void testgetgettrue() throws IOException 
	{
		float[] matrixArray1 = new float[MM*MM];
		float[] matrixArray2 = new float[MM*MM];

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
		float[] matrixArray1 = new float[MM*MM];
		float[] matrixArray2 = new float[MM*MM];

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
	public void testabsoluteLocal()
	{
		matrix_1.absoluteLocal();
		matrixf_1.absoluteLocal();

		if(!matrixf_1.toString().equals(matrix_1.toString()))
		{
			fail("get failed");
		}
	}
	
	@Test
	public void testnormalizeLocal()
	{
		matrix_1.normalizeLocal();
		matrixf_1.normalizeLocal();

		if(!matrixf_1.toString().equals(matrix_1.toString()))
		{
			fail("get failed");
		}
	}
	
	@Test
	public void testnormalize()
	{
		Matrix store1 = new Matrix(MM);
		Matrix3f store2 = new Matrix3f();
		matrix_1.normalize(store1);
		matrixf_1.normalize(store2);

		if(!store1.toString().equals(store2.toString()))
		{
			fail("get failed");
		}
	}
	
	@Test
	public void testgetColumn() 
	{
		Vector3f vector1 = new Vector3f();
		Vector3f vector2 = new Vector3f();
		matrix_1.getColumn(1,vector1);
		matrixf_1.getColumn(1,vector2);
		
		if(!vector1.toString().equals(vector2.toString()))
		{
			fail("get failed");
		}
	}
	
	@Test
	public void testsetColumn() 
	{
		Vector3f vector = new Vector3f(2,7,3);
		matrix_1.setColumn(1,vector);
		matrixf_1.setColumn(1,vector);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("get failed");
		}
	}
	

	
	@Test
	public void testfromAxes() 
	{
		Vector3f vector1 = new Vector3f(5,7,3);
		Vector3f vector2 = new Vector3f(2,1,3);
		Vector3f vector3 = new Vector3f(4,2,9);
		
		matrix_1.fromAxes(vector1,vector2,vector3);
		matrixf_1.fromAxes(vector1,vector2,vector3);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("get failed");
		}
	}
	
	@Test
	public void testtransposeNew() 
	{
		Matrix m1 = matrix_1.transposeNew();
		Matrix3f m2 = matrixf_1.transposeNew();
		
		
		if(!m1.toString().equals(m2.toString()))
		{
			fail("get failed");
		}
	}
	
	@Test
	public void testsetRow() 
	{
		Vector3f vector = new Vector3f(2,7,3);
		matrix_1.setRow(1,vector);
		matrixf_1.setRow(1,vector);
		
		
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("get failed");
		}
	}
	
	@Test
	public void testgetRow() 
	{
		Vector3f vector1 = new Vector3f();
		Vector3f vector2 = new Vector3f();
		matrix_1.getColumn(0,vector1);
		matrixf_1.getColumn(0,vector2);
		
		if(!vector1.toString().equals(vector2.toString()))
		{
			fail("get failed");
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
		float[] values = {5,5,5,5,5,5,5,8,4};
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
		float[] values = {5,5,5,5,5,5,5,8,4};
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
		float[] values = {5,5,5,5,5,5,5,8,4};
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
		Matrix3f test2 = matrixfIdentity_1.transpose();
		
		if(!test1.toString().equals(test2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testtransposeLocal() 
	{
		Matrix test1 = matrix_1.transposeLocal();
		Matrix3f test2 = matrixf_1.transposeLocal();
		
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
	public void testloadIdentity() 
	{
		Matrix matrix3 = new Matrix(MM);
		Matrix3f matrix3f = new Matrix3f();

		if(!matrix3.toString().equals(matrix3f.toString()))
		{
			fail("3x3 matrix not equal");
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
	public void testmultLocal3() 
	{
		Vector3f vector1 = new Vector3f(3,5,7);
		Vector3f vector2 = new Vector3f(3,5,7);
		
		matrix_1.multLocal(vector1);
		matrixf_1.multLocal(vector2);
		
		if(!vector1.toString().equals(vector2.toString()))
		{
			fail("fail");
		}
	}
	
	@Test
	public void testfromStartEndVectors() 
	{
		Vector3f start = new Vector3f(3,5,7);
		Vector3f end = new Vector3f(1,4,6);
		
		matrix_1.fromStartEndVectors(start, end);
		matrixf_1.fromStartEndVectors(start, end);
		
		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("fail");
		}
	}
	
	
	@Test
	public void testmult3() 
	{
		Matrix out1 = new Matrix(MM);
		Matrix3f out2 = new Matrix3f();
		
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
		Matrix store1 = new Matrix(MM);
		Matrix3f store2 = new Matrix3f();
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
	public void testinvert1() 
	{
		Matrix store1 = new Matrix(MM);
		Matrix3f store2 = new Matrix3f();
		store1 = matrixIdentity_1.invert();
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
		float[] iverse = {2,1,0,2,0,0,2,0,1};
		Matrix iverseM = new Matrix(iverse);
		Matrix3f iverseMf = new Matrix3f(2,1,0,2,0,0,2,0,1);
		
		Matrix store1 = new Matrix(MM);
		Matrix3f store2 = new Matrix3f();
		
		iverseM.invert(store1);
		iverseMf.invert(store2);
		
		if(!store1.toString().equals(store2.toString()))
		{
			fail("fail");
		}
	}
	//TODO test
	@Test
	public void testinvertLocal() 
	{
		float[] iverse = {2,1,0,2,0,0,2,0,1};
		Matrix iverseM = new Matrix(iverse);
		Matrix3f iverseMf3 = new Matrix3f(2,1,0,2,0,0,2,0,1);
		
		iverseM.invertLocal();
		iverseMf3.invertLocal();
		
		//TODO matrix4f incorrect
		
		if(!iverseM.toString().equals(iverseMf3.toString()))
		{
			fail("fail");
		}
	}
	//TODO test
	@Test
	public void testadjoint1() 
	{
		float[] ajointdata = {1,2,3,0,4,5,1,0,6};
		Matrix adjoint1 = new Matrix(ajointdata);
		Matrix3f adjoint2 = new Matrix3f(1,2,3,0,4,5,1,0,6);
		
		Matrix matrix = adjoint1.adjoint();
		Matrix3f matrix3f = adjoint2.adjoint();
		
		if(!matrix.toString().equals(matrix3f.toString()))
		{
			fail("fail");
		}
	}
	//TODO test
	@Test
	public void testadjoint2() 
	{
		Matrix store1 = new Matrix(MM);
		Matrix3f store2 = new Matrix3f();
		
		float[] ajointdata = {1,2,3,0,4,5,1,0,6};
		
		Matrix adjoint1 = new Matrix(ajointdata);
		Matrix3f adjoint2 = new Matrix3f(1,2,3,0,4,5,1,0,6);
		
		adjoint1.adjoint(store1);
		adjoint2.adjoint(store2);
		
		if(!store1.toString().equals(store2.toString()))
		{
			fail("fail");
		}
	}
	//TODO test
	@Test
	public void testdeterminant() 
	{
		float[] determinantData = {1,2,3,0,-4,1,0,3,-1};
		Matrix determinant1 = new Matrix(determinantData);
		Matrix3f determinant2 = new Matrix3f(1,2,3,0,-4,1,0,3,-1);
		
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
		if(matrix_1.hashCode() != matrixf_1.hashCode())
		{
			fail("hashCode fail");
		}
	}	

	@Test
	public void testequals() 
	{
		Matrix matrix31 = new Matrix(data);
		Matrix matrix32 = new Matrix(data.length);
		Matrix matrix33 = new Matrix(data);
		Matrix matrix34 = new Matrix(data);
		Matrix3f matrix3f_1 = new Matrix3f(1,2,3,4,5,6,7,8,9);
		Matrix3f matrix3f2 = new Matrix3f();
		Matrix3f matrix3f3 = new Matrix3f();
		Matrix3f matrix3f4 = new Matrix3f();

		if(matrix31.equals(matrix32) != matrix3f_1.equals(matrix3f2))
		{
			fail("equals fail");
		}

		if(matrix33.equals(matrix34) != matrix3f3.equals(matrix3f4))
		{
			fail("equals fail");
		}
	}

	@Test
	public void testwrite() throws IOException 
	{
		//TODO fix test
		
		fail("hard to test");
	}

	@Test
	public void testread() throws IOException 
	{
		//TODO fix test
		
		fail("hard to test");
	}

	@Test
	public void testscale() throws IOException 
	{
		Vector3f vector3f = new Vector3f(3,5,2);

		matrix_1.scale(vector3f);
		matrixf_1.scale(vector3f);

		if(!matrix_1.toString().equals(matrixf_1.toString()))
		{
			fail("3x3 scale fail");
		}
	}

	@Test
	public void testequalIdentity() throws IOException 
	{

		if(!Matrix.equalIdentity(matrix_1) == Matrix3f.equalIdentity(matrixf_1))
		{
			fail("equalIdentity fail");
		}

		if(!Matrix.equalIdentity(matrixIdentity_1) == Matrix3f.equalIdentity(matrixfIdentity_1))
		{
			fail("equalIdentity fail");
		}
	}

	@Test
	public void testclone() throws IOException 
	{
		matrixf_1.clone();
		matrix_1.clone();
	}
}
