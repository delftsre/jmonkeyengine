package com.jme3.math;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class MatrixTest {

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void testMatrixInt() {
		float[][] test1 = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
		Matrix matrix4_1 = new Matrix(4);

		assertArrayEquals(matrix4_1.matrix, test1);
	}

	@Test
	public void testMatrixFloatArray() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrix matrix4_1 = new Matrix(data4_1);
		
		int count =0;
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				assertEquals(matrix4_1.matrix[j][i], data4_1[count],0.0001);
				count++;
			}
		}
	}

	@Test
	public void testMatrixMatrix() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrix matrix4_1 = new Matrix(data4_1);
		
		Matrix test1 = new Matrix(matrix4_1);

		assertArrayEquals(matrix4_1.matrix, test1.matrix);
	}

	@Test
	public void testCopy() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 8, 2, 8, 4, 5, 8, 7, 8, 9, 8, 11, 12, 8, 14, 15, 16 };

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);

		matrix4_1.copy(matrix4_2);

		assertArrayEquals(matrix4_2.matrix, matrix4_1.matrix);
	}

	@Test
	public void testAbsoluteLocal() {
		float[] data3_1 = { 1, 2, 3, -4, 5, -6, 7, -8, 9 };
		float[] data3_2 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };

		float[] data4_1 = { 1, 2, -3, 4, 5, 6, 7, 8, 9, -10, 11, 12, -13, 14, 15, 16 };
		float[] data4_2 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrix matrix3_1 = new Matrix(data3_1);
		Matrix matrix3_2 = new Matrix(data3_2);

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);

		matrix3_1.absoluteLocal();
		matrix4_1.absoluteLocal();

		assertArrayEquals(matrix4_2.matrix, matrix4_1.matrix);
		assertArrayEquals(matrix3_2.matrix, matrix3_1.matrix);
	}

	@Test
	public void testNormalizeLocal() {
		float[] data3_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9};
		float[] data4_2 = { (float) 0.12309149 , (float) 0.20739034 , (float) -0.03829199 ,
				 (float) 0.49236596 , (float) 0.51847583,  (float) 0.07658396 ,
				 (float) 0.86164045 , (float) 0.82956135 , (float) -0.038291983 };

		Matrix matrix3_1 = new Matrix(data3_1);
		Matrix matrix3_2 = new Matrix(data4_2);
		
		matrix3_1.normalizeLocal();

		assertArrayEquals(matrix3_2.matrix, matrix3_1.matrix);
	}

	@Test
	public void testNormalize() {
		float[] data3_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9};
		float[] data4_2 = { (float) 0.12309149 , (float) 0.20739034 , (float) -0.03829199 ,
				 (float) 0.49236596 , (float) 0.51847583,  (float) 0.07658396 ,
				 (float) 0.86164045 , (float) 0.82956135 , (float) -0.038291983 };

		Matrix matrix3_1 = new Matrix(data3_1);
		Matrix matrix3_2 = new Matrix(data4_2);
		
		Matrix test1 = new Matrix(3);
		
		matrix3_1.normalize(test1);

		assertArrayEquals(matrix3_2.matrix, test1.matrix);
	}

	@Test
	public void testGetFloatArray() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] test1 = new float[16];

		Matrix matrix4_1 = new Matrix(data4_1);

		matrix4_1.get(test1);

		for (int i = 0; i < 4; i++) {
			assertEquals(test1[i], data4_1[i], 0.0001);
		}
	}

	@Test
	public void testGetFloatArrayBoolean() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15, 4, 8, 12, 16 };
		float[] test1 = new float[16];

		Matrix matrix4_1 = new Matrix(data4_1);

		matrix4_1.get(test1, true);

		for (int i = 0; i < 16; i++) {
			assertEquals(test1[i], data4_1[i], 0.0001);
		}

		matrix4_1.get(test1, false);

		for (int i = 0; i < 16; i++) {
			assertEquals(test1[i], data4_2[i], 0.0001);
		}
	}

	@Test
	public void testGetIntInt() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrix matrix4_1 = new Matrix(data4_1);
		float test1 = 3;
		float test2 = matrix4_1.get(0, 2);

		assertEquals(test1, test2, 0.0001);

	}

	@Test
	public void testGetColumnInt() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrix matrix4_1 = new Matrix(data4_1);
		float[] array1 = { 1, 5, 9, 13 };
		float[] array2 = matrix4_1.getColumn(0);

		for (int i = 0; i < 4; i++) {
			assertEquals(array1[i], array2[i], 0.0001);
		}
	}

	@Test
	public void testGetColumnIntFloatArray() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrix matrix4_1 = new Matrix(data4_1);
		float[] array1 = { 1, 5, 9, 13 };
		float[] array2 = new float[4];

		matrix4_1.getColumn(0, array2);
		for (int i = 0; i < 4; i++) {
			assertEquals(array1[i], array2[i], 0.0001);
		}
	}

	@Test
	public void testGetColumnIntVector3f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrix matrix4_1 = new Matrix(data4_1);
		Vector3f vector1 = new Vector3f(1, 5, 9);
		Vector3f vector2 = new Vector3f();

		matrix4_1.getColumn(0, vector2);

		assertEquals(vector1, vector2);
	}

	@Test
	public void testGetRowInt() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrix matrix4_1 = new Matrix(data4_1);
		Vector3f vector1 = new Vector3f(1, 2, 3);

		Vector3f vector2 = matrix4_1.getRow(0);

		assertEquals(vector1, vector2);
	}

	@Test
	public void testGetRowIntVector3f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrix matrix4_1 = new Matrix(data4_1);
		Vector3f vector1 = new Vector3f(1, 2, 3);

		Vector3f vector2 = new Vector3f();
		matrix4_1.getRow(0, vector2);

		assertEquals(vector1, vector2);
	}

	@Test
	public void testSetColumnIntFloatArray() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] test = { 45, 45, 45, 45 };
		Matrix matrix4_1 = new Matrix(data4_1);

		matrix4_1.setColumn(0, test);

		for (int i = 0; i < 4; i++) {
			assertEquals(test[i], matrix4_1.getColumn(0)[i], 0.0001);
		}
	}

	@Test
	public void testSetColumnIntVector3f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Vector3f test1 = new Vector3f(45, 45, 45);
		Vector3f test2 = new Vector3f();
		Matrix matrix4_1 = new Matrix(data4_1);

		matrix4_1.setColumn(0, test1);
		matrix4_1.getColumn(0, test2);

		assertEquals(test1, test2);
	}

	@Test
	public void testSetRow() {
		float[] data3_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		float[] data3_2 = { 999, 999, 999, 4, 5, 6, 7, 8, 9 };

		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 999, 999, 999, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrix matrix3_1 = new Matrix(data3_1);
		Matrix matrix3_2 = new Matrix(data3_2);

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);

		Vector3f vector = new Vector3f(999, 999, 999);

		matrix3_1.setRow(0, vector);
		matrix4_1.setRow(0, vector);

		assertArrayEquals(matrix4_2.matrix, matrix4_1.matrix);
		assertArrayEquals(matrix3_2.matrix, matrix3_1.matrix);
	}

	@Test
	public void testSetIntIntFloat() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 1, 2, 3, 4, 5, 6, 999, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		int r = 1, c = 2;

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);
		
		matrix4_1.set(r, c, 999);

		assertArrayEquals(matrix4_2.matrix, matrix4_1.matrix);
	}

	@Test
	public void testSetFloatArrayArray() {
		float[] data3_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		float[][] data3_2 = { { 7, 7, 7 }, { 7, 7, 7 }, { 7, 7, 7 } };

		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[][] data4_2 = { { 7, 7, 7, 7 }, { 7, 7, 7, 7 }, { 7, 7, 7, 7 }, { 7, 7, 7, 7 } };

		Matrix matrix3_1 = new Matrix(data3_1);

		Matrix matrix4_1 = new Matrix(data4_1);

		matrix3_1.set(data3_2);
		matrix4_1.set(data4_2);

		assertArrayEquals(data4_2, matrix4_1.matrix);
		assertArrayEquals(data3_2, matrix3_1.matrix);
	}

	@Test
	public void testSetMatrix() {
		float[] data3_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		float[] data3_2 = { 7, 7, 7, 7, 7, 7, 7, 7, 7 };

		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };

		Matrix matrix3_1 = new Matrix(data3_1);
		Matrix matrix3_2 = new Matrix(data3_2);

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);

		matrix3_1.set(matrix3_2);
		matrix4_1.set(matrix4_2);

		assertArrayEquals(matrix4_2.matrix, matrix4_1.matrix);
		assertArrayEquals(matrix3_2.matrix, matrix3_1.matrix);
	}

	@Test
	public void testSetFloatArray() {
		float[] data3_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		float[] data3_2 = { 7, 7, 7, 7, 7, 7, 7, 7, 7 };
		float[][] data3_3 = { { 7, 7, 7 }, { 7, 7, 7 }, { 7, 7, 7 } };

		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
		float[][] data4_3 = { { 7, 7, 7, 7 }, { 7, 7, 7, 7 }, { 7, 7, 7, 7 }, { 7, 7, 7, 7 } };

		Matrix matrix3_1 = new Matrix(data3_1);

		Matrix matrix4_1 = new Matrix(data4_1);

		matrix3_1.set(data3_2);

		matrix4_1.set(data4_2);

		assertArrayEquals(data4_3, matrix4_1.matrix);
		assertArrayEquals(data3_3, matrix3_1.matrix);
	}

	@Test
	public void testSetFloatArrayBoolean() {
		float[] data3_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		float[][] data3_3 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
		float[][] data3_4 = { { 1, 4, 7 }, { 2, 5, 8 }, { 3, 6, 9 } };

		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[][] data4_3 = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 16 } };
		float[][] data4_4 = { { 1, 5, 9, 13 }, { 2, 6, 10, 14 }, { 3, 7, 11, 15 }, { 4, 8, 12, 16 } };

		Matrix matrix3_1 = new Matrix(3);

		Matrix matrix4_1 = new Matrix(4);

		matrix3_1.set(data3_1, false);
		matrix4_1.set(data4_1, false);

		assertArrayEquals(data4_3, matrix4_1.matrix);
		assertArrayEquals(data3_3, matrix3_1.matrix);

		matrix3_1.set(data3_1, true);
		matrix4_1.set(data4_1, true);

		assertArrayEquals(data4_4, matrix4_1.matrix);
		assertArrayEquals(data3_4, matrix3_1.matrix);
	}

	@Test
	public void testSetQuaternion() {
		float[] data3_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9 };
		Quaternion data3_3 = new Quaternion(0, 0, 0, 0);
		float[][] data3_4 = { { (float) 8.124039, 0, 0 }, { 0, (float) 9.643651, 0 }, { 0, 0, (float) 11.224972 } };

		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Quaternion data4_3 = new Quaternion(0, 0, 0, 0);
		float[][] data4_4 = { { (float) 10.34408, 0, 0, 13 }, { 0, (float) 11.83216, 0, 14 },
				{ 0, 0, (float) 13.379088, 15 }, { 4, 8, 12, 16 } };

		Matrix matrix3_1 = new Matrix(data3_1);

		Matrix matrix4_1 = new Matrix(data4_1);

		matrix3_1.set(data3_3);
		matrix4_1.set(data4_3);

		assertArrayEquals(data4_4, matrix4_1.matrix);
		assertArrayEquals(data3_4, matrix3_1.matrix);
	}

	@Test
	public void testTransposeLocal() {
		float[] data4_1 = { 1, 1, 1, 1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 1, 5, 9, 13, 1, 6, 10, 14, 1, 7, 11, 15, 1, 8, 12, 16 };

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);

		matrix4_1.transposeLocal();

		assertArrayEquals(matrix4_1.matrix, matrix4_2.matrix);
	}

	@Test
	public void testTransposeNew() {
		float[] data4_1 = { 1, 1, 1, 1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 1, 5, 9, 13, 1, 6, 10, 14, 1, 7, 11, 15, 1, 8, 12, 16 };

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);

		Matrix matrix4_3 = matrix4_1.transposeNew();

		assertArrayEquals(matrix4_3.matrix, matrix4_2.matrix);
	}

	@Test
	public void testLoadIdentity() {
		float[] data3 = { 1, 2, 3, -4, 5, -6, 7, -8, 9 };
		float[] data4 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix3 = new Matrix(data3);
		Matrix matrix4 = new Matrix(data4);
		float[][] identityMatrix3 = { { 1, 0, 0 }, { 0, 1, 0 }, { 0, 0, 1 } };
		float[][] identityMatrix4 = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };
		matrix3.loadIdentity();
		matrix4.loadIdentity();
		assertArrayEquals(identityMatrix4, matrix4.matrix);
		assertArrayEquals(identityMatrix3, matrix3.matrix);
	}

	@Test
	public void testMultLocalFloat() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 14, 28, 42, 56, 70, 84, 98, 112, 126, 140, 154, 168, 182, 196, 210, 224 };

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);

		matrix4_1.multLocal(14);

		assertArrayEquals(matrix4_2.matrix, matrix4_2.matrix);
	}

	@Test
	public void testMultLocalVector3f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f test1 = new Vector3f(2, 3, 4);
		Vector3f test2 = new Vector3f(24, 64, 104);

		Matrix matrix4_1 = new Matrix(data4_1);

		matrix4_1.multLocal(test1);

		assertEquals(test1, test2);
	}

	@Test
	public void testMultFloat() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32 };

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);

		matrix4_1.mult(2);

		assertArrayEquals(matrix4_1.matrix, matrix4_2.matrix);
	}

	@Test
	public void testMultFloatMatrix() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32 };

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);
		Matrix matrix4_3 = new Matrix(4);

		matrix4_1.mult(2, matrix4_3);

		assertArrayEquals(matrix4_3.matrix, matrix4_2.matrix);
	}

	@Test
	public void testMultMatrix() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
		float[] data4_3 = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32 };

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);
		Matrix matrix4_3 = new Matrix(data4_3);

		Matrix matrix4_4 = matrix4_1.mult(matrix4_2);

		assertArrayEquals(matrix4_4.matrix, matrix4_3.matrix);
	}

	@Test
	public void testMultMatrixMatrix() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
		float[] data4_3 = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32 };

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);
		Matrix matrix4_3 = new Matrix(data4_3);

		Matrix matrix4_4 = matrix4_1.mult(matrix4_2);	

		assertArrayEquals(matrix4_3.matrix, matrix4_4.matrix);
	}

	@Test
	public void testMultLocalMatrix() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
		float[] data4_3 = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32 };

		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);
		Matrix matrix4_3 = new Matrix(data4_3);

		Matrix matrix4_4 = new Matrix(4);

		matrix4_1.multLocal(matrix4_2);

		assertArrayEquals(matrix4_1.matrix, matrix4_3.matrix);
	}

	@Test
	public void testMultVector3f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f vector = new Vector3f(1, 2, 3);
		Vector3f test1 = new Vector3f(18, 46, 74);

		Matrix matrix4_1 = new Matrix(data4_1);

		Vector3f test2 = matrix4_1.mult(vector);

		assertEquals(test1, test2);
	}

	@Test
	public void testMultVector3fVector3f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f vector = new Vector3f(1, 2, 3);
		Vector3f test1 = new Vector3f(18, 46, 74);

		Matrix matrix4_1 = new Matrix(data4_1);

		Vector3f test2 = new Vector3f();
		matrix4_1.mult(vector, test2);

		assertEquals(test1, test2);
	}

	@Test
	public void testMultVector4f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector4f vector = new Vector4f(1, 2, 3, 4);
		Vector4f test1 = new Vector4f(30, 70, 110, 150);

		Matrix matrix4_1 = new Matrix(data4_1);

		Vector4f test2 = matrix4_1.mult(vector);

		assertEquals(test1, test2);
	}

	@Test
	public void testMultVector4fVector4f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector4f vector = new Vector4f(1, 2, 3, 4);
		Vector4f test1 = new Vector4f(30, 70, 110, 150);

		Matrix matrix4_1 = new Matrix(data4_1);

		Vector4f test2 = new Vector4f();
		matrix4_1.mult(vector, test2);

		assertEquals(test1, test2);
	}

	@Test
	public void testMultAcrossVector4f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector4f vector = new Vector4f(1, 2, 3, 4);
		Vector4f test1 = new Vector4f(90, 100, 110, 120);

		Matrix matrix4_1 = new Matrix(data4_1);

		Vector4f test2 = matrix4_1.multAcross(vector);
		assertEquals(test1, test2);
	}

	@Test
	public void testMultAcrossVector4fVector4f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector4f vector = new Vector4f(1, 2, 3, 4);
		Vector4f test1 = new Vector4f(90, 100, 110, 120);

		Matrix matrix4_1 = new Matrix(data4_1);

		Vector4f test2 = new Vector4f();
		matrix4_1.multAcross(vector, test2);

		assertEquals(test1, test2);
	}

	@Test
	public void testMultNormal() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f vector = new Vector3f(1, 2, 3);
		Vector3f test1 = new Vector3f(14, 38, 62);

		Matrix matrix4_1 = new Matrix(data4_1);

		Vector3f test2 = new Vector3f();

		matrix4_1.multNormal(vector, test2);

		assertEquals(test1, test2);
	}

	@Test
	public void testMultNormalAcross() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f vector = new Vector3f(1, 2, 3);
		Vector3f test1 = new Vector3f(38, 44, 50);

		Matrix matrix4_1 = new Matrix(data4_1);

		Vector3f test2 = new Vector3f();

		matrix4_1.multNormalAcross(vector, test2);

		assertEquals(test1, test2);
	}

	@Test
	public void testMultProj() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f vector = new Vector3f(1, 2, 3);
		Vector3f test1 = new Vector3f(18, 46, 74);

		Matrix matrix4_1 = new Matrix(data4_1);

		Vector3f test2 = new Vector3f();

		matrix4_1.multProj(vector, test2);

		assertEquals(test1, test2);
	}

	@Test
	public void testMultAcrossVector3fVector3f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f vector = new Vector3f(1, 2, 3);
		Vector3f test1 = new Vector3f(51, 58, 65);

		Matrix matrix4_1 = new Matrix(data4_1);

		Vector3f test2 = new Vector3f();

		matrix4_1.multAcross(vector, test2);

		assertEquals(test1, test2);
	}

	@Test
	public void testMultQuaternionQuaternion() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Quaternion vector = new Quaternion(1, 2, 3, 4);
		Quaternion test1 = new Quaternion(90, 100, 110, 120);

		Matrix matrix4_1 = new Matrix(data4_1);

		Quaternion test2 = new Quaternion();

		matrix4_1.mult(vector, test2);

		assertEquals(test1, test2);
	}

	@Test
	public void testMultFloatArray() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		float[] array = { 1, 2, 3, 4 };
		float[] test1 = { 30, 70, 110, 150 };

		Matrix matrix4_1 = new Matrix(data4_1);

		float[] test2 = matrix4_1.mult(array);
		for (int r = 0; r < 4; r++) {
			assertEquals(test1[r], test2[r], 0.0001);
		}
	}

	@Test
	public void testMultAcrossFloatArray() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		float[] array = { 1, 2, 3, 4 };
		float[] test1 = { 90, 100, 110, 120 };

		Matrix matrix4_1 = new Matrix(data4_1);

		float[] test2 = matrix4_1.multAcross(array);

		for (int r = 0; r < 4; r++) {
			assertEquals(test1[r], test2[r], 0.0001);
		}
	}

	@Test
	public void testInvert() {
		float[] data4_1 = { 4, 0, 0, 0, 0, 0, 2, 0, 0, 1, 2, 0, 1, 0, 0, 1 };
		float[] data4_2 = { (float) 0.25, 0, 0, 0, 0, -1, 1, 0, 0, (float) 0.5, 0, 0, (float) -0.25, 0, 0, 1 };
		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix test2 = new Matrix(data4_2);

		Matrix test1 = matrix4_1.invert();

		assertArrayEquals(test1.matrix, test2.matrix);

	}

	@Test
	public void testInvertMatrix() {
		float[] data4_1 = { 4, 0, 0, 0, 0, 0, 2, 0, 0, 1, 2, 0, 1, 0, 0, 1 };
		float[] data4_2 = { (float) 0.25, 0, 0, 0, 0, -1, 1, 0, 0, (float) 0.5, 0, 0, (float) -0.25, 0, 0, 1 };
		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix test2 = new Matrix(data4_2);

		Matrix test1 = new Matrix(4);

		matrix4_1.invert(test1);

		assertArrayEquals(test1.matrix, test2.matrix);
	}

	@Test
	public void testInvertLocal() {
		float[] data4_1 = { 4, 0, 0, 0, 0, 0, 2, 0, 0, 1, 2, 0, 1, 0, 0, 1 };
		float[] data4_2 = { (float) 0.25, 0, 0, 0, 0, -1, 1, 0, 0, (float) 0.5, 0, 0, (float) -0.25, 0, 0, 1 };
		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix test2 = new Matrix(data4_2);

		matrix4_1.invertLocal();

		assertArrayEquals(matrix4_1.matrix, test2.matrix);
	}

	@Test
	public void testAdjoint() {
		float[] data4_1 = { 5, -2, 2, 7, 1, 0, 0, 3, -3, 1, 5, 0, 3, -1, -9, 4 };
		float[] data4_2 = { -12, 76, -60, -36, -56, 208, -82, -58, 4, 4, -2, -10, 4, 4, 20, 12 };
		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix test2 = new Matrix(data4_2);

		Matrix test1 = matrix4_1.adjoint();

		assertArrayEquals(test1.matrix, test2.matrix);
	}

	@Test
	public void testAdjointMatrix() {
		float[] data4_1 = { 5, -2, 2, 7, 1, 0, 0, 3, -3, 1, 5, 0, 3, -1, -9, 4 };
		float[] data4_2 = { -12, 76, -60, -36, -56, 208, -82, -58, 4, 4, -2, -10, 4, 4, 20, 12 };
		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix test2 = new Matrix(data4_2);

		Matrix test1 = new Matrix(4);
		matrix4_1.adjoint(test1);

		assertArrayEquals(test1.matrix, test2.matrix);
	}

	@Test
	public void testCofactor() {
		float[] data4_1 = { 5, -2, 2, 7, 1, 0, 0, 3, -3, 1, 5, 0, 3, -1, -9, 4 };
		float[] data4_2 = { -12, 76, -60, -36, -56, 208, -82, -58, 4, 4, -2, -10, 4, 4, 20, 12 };
		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix test2 = new Matrix(data4_2);

		Matrix test1 = new Matrix(4);
		matrix4_1.cofactor(test1);

		assertArrayEquals(test1.matrix, test2.matrix);
	}

	@Test
	public void testRemoveRowCol() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 1,3,4,9,11,12,13,15,16};
		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix test2 = new Matrix(data4_2);
		
		Matrix test1 = matrix4_1.removeRowCol(1, 1);

		assertArrayEquals(test1.matrix, test2.matrix);
	}

	@Test
	public void testDeterminant() {
		float[] data4_1 = {2,5,3,5,4,6,6,3,11,3,2,-2,4,-7,9,3};
		Matrix matrix4_1 = new Matrix(data4_1);
		float test2 =2960;
		
		float test1 = matrix4_1.determinant();
		
		assertEquals(test1, test2,0.002);
	}

	@Test
	public void testZero() {
		float[] data4_1 = {2,5,3,5,4,6,6,3,11,3,2,-2,4,-7,9,3};
		float[] data4_2 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_2);

		matrix4_1.zero();
		
		assertArrayEquals(matrix4_1.matrix, matrix4_2.matrix);
	}

	@Test
	public void testAdd() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32};
		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_1);
		Matrix test2 = new Matrix(data4_2);

		Matrix test1 = matrix4_1.add(matrix4_2);
		
		assertArrayEquals(test1.matrix, test2.matrix);
	}

	@Test
	public void testAddLocal() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = { 2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32};
		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_1);
		Matrix test2 = new Matrix(data4_2);

		matrix4_1.addLocal(matrix4_2);
		
		assertArrayEquals(matrix4_1.matrix, test2.matrix);
	}

	@Test
	public void testToTranslationVector() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_1 = new Matrix(data4_1);
		
		Vector3f test2 = new Vector3f(4,8,12);
		Vector3f test1 = matrix4_1.toTranslationVector();
		
		assertEquals(test1, test2);
	}

	@Test
	public void testToTranslationVectorVector3f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_1 = new Matrix(data4_1);
		
		Vector3f test2 = new Vector3f(4,8,12);
		Vector3f test1 = new Vector3f(); 
		matrix4_1.toTranslationVector(test1);
		
		assertEquals(test1, test2);
	}

	@Test
	public void testToRotationQuat() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_1 = new Matrix(data4_1);
		
		Quaternion test2 = new Quaternion((float)0.030060045, (float)-0.1420475, (float)-0.06684814, (float)0.7650391);
		Quaternion test1 = matrix4_1.toRotationQuat();
		assertEquals(test1, test2);
	}

	@Test
	public void testToRotationQuatQuaternion() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_1 = new Matrix(data4_1);
		
		Quaternion test2 = new Quaternion((float)0.030060045, (float)-0.1420475, (float)-0.06684814, (float)0.7650391);
		Quaternion test1 = new Quaternion();
		matrix4_1.toRotationQuat(test1);
		assertEquals(test1, test2);
	}

	@Test
	public void testToRotationMatrix() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = {1,2,3,5,6,7,9,10,11};
		Matrix matrix4_1 = new Matrix(data4_1);
		
		Matrix test2 = new Matrix(data4_2);
		Matrix test1 = matrix4_1.toRotationMatrix();
		
		assertArrayEquals(test1.matrix, test2.matrix);
	}

	@Test
	public void testToRotationMatrixMatrix() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data4_2 = {1,2,3,5,6,7,9,10,11};
		Matrix matrix4_1 = new Matrix(data4_1);
		
		Matrix test2 = new Matrix(data4_2);
		Matrix test1 = new Matrix(3);
		matrix4_1.toRotationMatrix(test1);
		
		assertArrayEquals(test1.matrix, test2.matrix);
	}

	@Test
	public void testToScaleVector() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_1 = new Matrix(data4_1);
		
		Vector3f test2 = new Vector3f((float)10.34408,(float)11.83216, (float)13.379088);
		
		Vector3f test1 = matrix4_1.toScaleVector();
		
		assertEquals(test1, test2);
	}

	@Test
	public void testToScaleVectorVector3f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_1 = new Matrix(data4_1);
		
		Vector3f test2 = new Vector3f((float)10.34408,(float)11.83216, (float)13.379088);
		
		Vector3f test1 = new Vector3f();
		matrix4_1.toScaleVector(test1);
		
		assertEquals(test1, test2);
	}

	@Test
	public void testSetScaleFloatFloatFloat() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_1 = new Matrix(data4_1);
		
		float[] data4_2 = { (float) 0.09667365, (float) 0.3380617, (float) 0.6726916, 4, (float) 0.48336828, (float) 1.0141851, (float) 1.5696137, 8, (float) 0.8700629, (float) 1.6903085, (float) 2.4665358, 12, 13, 14, 15, 16 };
		Matrix matrix4_2 = new Matrix(data4_2);
		
		matrix4_1.setScale(1,2,3);
		
		assertArrayEquals(matrix4_2.matrix, matrix4_1.matrix);
	}

	@Test
	public void testSetScaleVector3f() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_1 = new Matrix(data4_1);
		
		float[] data4_2 = { (float) 0.09667365, (float) 0.3380617, (float) 0.6726916, 4, (float) 0.48336828, (float) 1.0141851, (float) 1.5696137, 8, (float) 0.8700629, (float) 1.6903085, (float) 2.4665358, 12, 13, 14, 15, 16 };
		Matrix matrix4_2 = new Matrix(data4_2);
		
		Vector3f vector = new Vector3f(1,2,3);
		
		matrix4_1.setScale(vector);
		
		assertArrayEquals(matrix4_2.matrix, matrix4_1.matrix);
	}

	@Test
	public void testEqualsObject() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_1 = new Matrix(data4_1);
		Matrix matrix4_2 = new Matrix(data4_1);
		
		float[] data4_3 = { 7, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_3 = new Matrix(data4_3);
		
		assertEquals(matrix4_1.equals(matrix4_2), true);
		
		assertEquals(matrix4_1.equals(matrix4_3), false);
	}

	@Test
	public void testIsIdentity() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_1 = new Matrix(data4_1);
		
		float[] data4_2 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
		Matrix matrix4_2 = new Matrix(data4_2);
		
		assertEquals(matrix4_1.isIdentity(), false);
		
		assertEquals(matrix4_2.isIdentity(), true);
	}

	@Test
	public void testScale() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_1 = new Matrix(data4_1);
		
		float[] data4_2 = {1,4,9,4,5,12,21,8,9,20,33,12,13,28,45,16};
		Matrix matrix4_2 = new Matrix(data4_2);
		
		Vector3f vector = new Vector3f(1,2,3);
		
		matrix4_1.scale(vector);
		
		assertArrayEquals(matrix4_2.matrix, matrix4_1.matrix);
	}

	@Test
	public void testEqualIdentity() {
		float[] data4_1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrix matrix4_1 = new Matrix(data4_1);
		
		float[] data4_2 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
		Matrix matrix4_2 = new Matrix(data4_2);
		
		assertEquals(Matrix.equalIdentity(matrix4_1), false);
		
		assertEquals(Matrix.equalIdentity(matrix4_2), true);
	}

}
