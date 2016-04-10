package com.jme3.math;

import static org.junit.Assert.*;

import java.util.Vector;
import org.junit.Test;

public class MatrixTest {

	@Test
	public void testMatrixInt() {
		float[][] expected = {{1,0,0,0},{0,1,0,0},{0,0,1,0},{0,0,0,1}};
		Matrixable matrix4_test = new Matrix(4);

		assertArrayEquals(matrix4_test.getMatrix(), expected);
	}

	@Test
	public void testMatrixFloatArray() {
		float[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrixable matrix4_test = new Matrix(expected);
		
		int count =0;
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 4; j++)
			{
				assertEquals(matrix4_test.getMatrix()[j][i], expected[count],0.0001);
				count++;
			}
		}
	}

	@Test
	public void testMatrixMatrix() {
		float[] data = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrixable expected = new Matrix(data);
		
		Matrixable matrix_test = new Matrix(expected);

		assertArrayEquals(expected.getMatrix(), matrix_test.getMatrix());
	}

	@Test
	public void testCopy() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 8, 2, 8, 4, 5, 8, 7, 8, 9, 8, 11, 12, 8, 14, 15, 16 };

		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		matrix_test.copy(expected);

		assertArrayEquals(expected.getMatrix(), matrix_test.getMatrix());
	}

	@Test
	public void testAbsoluteLocal() {
		float[] data1 = { 1, 2, -3, 4, 5, 6, 7, 8, 9, -10, 11, 12, -13, 14, 15, 16 };
		float[] data2 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		matrix_test.absoluteLocal();

		assertArrayEquals(expected.getMatrix(), matrix_test.getMatrix());
	}

	@Test
	public void testNormalizeLocal() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9};
		float[] data2 = { (float) 0.12309149 , (float) 0.20739034 , (float) -0.03829199 ,
				 (float) 0.49236596 , (float) 0.51847583,  (float) 0.07658396 ,
				 (float) 0.86164045 , (float) 0.82956135 , (float) -0.038291983 };

		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);
		
		matrix_test.normalizeLocal();

		assertArrayEquals(expected.getMatrix(), matrix_test.getMatrix());
	}

	@Test
	public void testNormalize() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9};
		float[] data2 = { (float) 0.12309149 , (float) 0.20739034 , (float) -0.03829199 ,
				 (float) 0.49236596 , (float) 0.51847583,  (float) 0.07658396 ,
				 (float) 0.86164045 , (float) 0.82956135 , (float) -0.038291983 };

		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);
		
		Matrixable test = new Matrix(3);
		
		matrix_test.normalize(test);

		assertArrayEquals(expected.getMatrix(), test.getMatrix());
	}

	@Test
	public void testGetFloatArray() {
		float[] expected = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] test1 = new float[16];

		Matrixable matrix_test = new Matrix(expected);

		matrix_test.get(test1);

		for (int i = 0; i < 4; i++) {
			assertEquals(test1[i], expected[i], 0.0001);
		}
	}

	@Test
	public void testGetFloatArrayBoolean() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15, 4, 8, 12, 16 };
		float[] test1 = new float[16];

		Matrixable matrix_test = new Matrix(data1);

		matrix_test.get(test1, true);

		for (int i = 0; i < 16; i++) {
			assertEquals(test1[i], data1[i], 0.0001);
		}

		matrix_test.get(test1, false);

		for (int i = 0; i < 16; i++) {
			assertEquals(test1[i], data2[i], 0.0001);
		}
	}

	@Test
	public void testGetIntInt() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrixable matrix_test = new Matrix(data1);
		float test1 = 3;
		float test2 = matrix_test.get(0, 2);

		assertEquals(test1, test2, 0.0001);

	}

	@Test
	public void testGetColumnInt() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrixable matrix_test = new Matrix(data1);
		float[] array1 = { 1, 5, 9, 13 };
		float[] array2 = matrix_test.getColumn(0);

		for (int i = 0; i < 4; i++) {
			assertEquals(array1[i], array2[i], 0.0001);
		}
	}

	@Test
	public void testGetColumnIntFloatArray() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrixable matrix_test = new Matrix(data1);
		float[] array1 = { 1, 5, 9, 13 };
		float[] array2 = new float[4];

		matrix_test.getColumn(0, array2);
		for (int i = 0; i < 4; i++) {
			assertEquals(array1[i], array2[i], 0.0001);
		}
	}

	@Test
	public void testGetColumnIntVector3f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrixable matrix_test = new Matrix(data1);
		Vector3f vector1 = new Vector3f(1, 5, 9);
		Vector3f vector2 = new Vector3f();

		matrix_test.getColumn(0, vector2);

		assertEquals(vector1, vector2);
	}

	@Test
	public void testGetRowInt() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrixable matrix_test = new Matrix(data1);
		Vector3f vector1 = new Vector3f(1, 2, 3);

		Vector3f vector2 = matrix_test.getRow(0);

		assertEquals(vector1, vector2);
	}

	@Test
	public void testGetRowIntVector3f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrixable matrix_test = new Matrix(data1);
		Vector3f vector1 = new Vector3f(1, 2, 3);

		Vector3f vector2 = new Vector3f();
		matrix_test.getRow(0, vector2);

		assertEquals(vector1, vector2);
	}

	@Test
	public void testSetColumnIntFloatArray() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] test = { 45, 45, 45, 45 };
		Matrixable matrix_test = new Matrix(data1);

		matrix_test.setColumn(0, test);

		for (int i = 0; i < 4; i++) {
			assertEquals(test[i], matrix_test.getColumn(0)[i], 0.0001);
		}
	}

	@Test
	public void testSetColumnIntVector3f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Vector3f test1 = new Vector3f(45, 45, 45);
		Vector3f test2 = new Vector3f();
		Matrixable matrix_test = new Matrix(data1);

		matrix_test.setColumn(0, test1);
		matrix_test.getColumn(0, test2);

		assertEquals(test1, test2);
	}

	@Test
	public void testSetRow() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 999, 999, 999, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		Vector3f vector = new Vector3f(999, 999, 999);

		matrix_test.setRow(0, vector);

		assertArrayEquals(expected.getMatrix(), matrix_test.getMatrix());
	}

	@Test
	public void testSetIntIntFloat() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 1, 2, 3, 4, 5, 6, 999, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		int r = 1, c = 2;

		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);
		
		matrix_test.set(r, c, 999);

		assertArrayEquals(expected.getMatrix(), matrix_test.getMatrix());
	}

	@Test
	public void testSetFloatArrayArray() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[][] data2 = { { 7, 7, 7, 7 }, { 7, 7, 7, 7 }, { 7, 7, 7, 7 }, { 7, 7, 7, 7 } };

		Matrixable matrix_test = new Matrix(data1);
		matrix_test.set(data2);

		assertArrayEquals(data2, matrix_test.getMatrix());
	}

	@Test
	public void testSetMatrix() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };

		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		matrix_test.set(expected);

		assertArrayEquals(expected.getMatrix(), matrix_test.getMatrix());
	}

	@Test
	public void testSetFloatArray() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7 };
		float[][] expected = { { 7, 7, 7, 7 }, { 7, 7, 7, 7 }, { 7, 7, 7, 7 }, { 7, 7, 7, 7 } };

		Matrixable matrix_test = new Matrix(data1);

		matrix_test.set(data2);

		assertArrayEquals(expected, matrix_test.getMatrix());
	}

	@Test
	public void testSetFloatArrayBoolean() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[][] expected1 = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 16 } };
		float[][] expected2 = { { 1, 5, 9, 13 }, { 2, 6, 10, 14 }, { 3, 7, 11, 15 }, { 4, 8, 12, 16 } };

		Matrixable matrix_test = new Matrix(4);
		matrix_test.set(data1, false);

		assertArrayEquals(expected1, matrix_test.getMatrix());
		matrix_test.set(data1, true);

		assertArrayEquals(expected2, matrix_test.getMatrix());
	}

	@Test
	public void testSetQuaternion() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Quaternion data2 = new Quaternion(1, 2, 3, 4);
		float[][] expected = {{0.13333333f,-0.66666675f,0.73333335f ,13.0f},{0.9333334f,0.3333333f , 0.13333336f,14.0f },{ -0.33333334f, 0.66666675f , 0.6666666f,15.0f },{4.0f,8.0f,12.0f ,16.0f }};

		Matrixable matrix_test = new Matrix(data1);

		matrix_test.set(data2);
		assertArrayEquals(expected, matrix_test.getMatrix());
	}

	@Test
	public void testTransposeLocal() {
		float[] data1 = { 1, 1, 1, 1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 1, 5, 9, 13, 1, 6, 10, 14, 1, 7, 11, 15, 1, 8, 12, 16 };

		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		matrix_test.transposeLocal();

		assertArrayEquals(matrix_test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testTransposeNew() {
		float[] data1 = { 1, 1, 1, 1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 1, 5, 9, 13, 1, 6, 10, 14, 1, 7, 11, 15, 1, 8, 12, 16 };

		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		Matrixable matrix_test1 = matrix_test.transposeNew();

		assertArrayEquals(matrix_test1.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testLoadIdentity() {
		float[] data4 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test = new Matrix(data4);
		float[][] expected = { { 1, 0, 0, 0 }, { 0, 1, 0, 0 }, { 0, 0, 1, 0 }, { 0, 0, 0, 1 } };

		matrix_test.loadIdentity();
		assertArrayEquals(expected, matrix_test.getMatrix());
	}

	@Test
	public void testMultLocalFloat() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 14, 28, 42, 56, 70, 84, 98, 112, 126, 140, 154, 168, 182, 196, 210, 224 };

		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		matrix_test.multLocal(14);

		assertArrayEquals(expected.getMatrix(), matrix_test.getMatrix());
	}

	@Test
	public void testMultLocalVector3f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f test1 = new Vector3f(2, 3, 4);
		Vector3f expected = new Vector3f(24, 64, 104);

		Matrixable matrix_test = new Matrix(data1);

		matrix_test.multLocal(test1);

		assertEquals(test1, expected);
	}

	@Test
	public void testMultFloat() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32 };

		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		matrix_test.mult(2);

		assertArrayEquals(matrix_test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testMultFloatMatrix() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32 };

		Matrixable matrix = new Matrix(data1);
		Matrixable expected = new Matrix(data2);
		Matrixable matrix_test = new Matrix(4);

		matrix.mult(2, matrix_test);

		assertArrayEquals(matrix_test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testMultMatrix() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
		float[] data3 = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32 };

		Matrixable matrix = new Matrix(data1);
		Matrixable matrixMult = new Matrix(data2);
		Matrixable expected = new Matrix(data3);

		Matrixable matrix_test = matrix.mult(matrixMult);

		assertArrayEquals(matrix_test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testMultMatrixMatrix() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
		float[] data3 = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32 };

		Matrixable matrix = new Matrix(data1);
		Matrixable matrixMult = new Matrix(data2);
		Matrixable expected = new Matrix(data3);

		Matrixable matrix_test = matrix.mult(matrixMult);	

		assertArrayEquals(expected.getMatrix(), matrix_test.getMatrix());
	}

	@Test
	public void testMultLocalMatrix() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
		float[] data3 = { 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32 };

		Matrixable matrix_test = new Matrix(data1);
		Matrixable matrix4_2 = new Matrix(data2);
		Matrixable expected = new Matrix(data3);

		matrix_test.multLocal(matrix4_2);

		assertArrayEquals(matrix_test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testMultVector3f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f vector = new Vector3f(1, 2, 3);
		Vector3f expected = new Vector3f(18, 46, 74);

		Matrixable matrix_test = new Matrix(data1);

		Vector3f test = matrix_test.mult(vector);

		assertEquals(expected, test);
	}

	@Test
	public void testMultVector3fVector3f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f vector = new Vector3f(1, 2, 3);
		Vector3f expected = new Vector3f(18, 46, 74);

		Matrixable matrix_test = new Matrix(data1);

		Vector3f test = new Vector3f();
		matrix_test.mult(vector, test);

		assertEquals(expected, test);
	}

	@Test
	public void testMultVector4f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector4f vector = new Vector4f(1, 2, 3, 4);
		Vector4f expected = new Vector4f(30, 70, 110, 150);

		Matrixable matrix_test = new Matrix(data1);

		Vector4f test = matrix_test.mult(vector);

		assertEquals(expected, test);
	}

	@Test
	public void testMultVector4fVector4f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector4f vector = new Vector4f(1, 2, 3, 4);
		Vector4f expected = new Vector4f(30, 70, 110, 150);

		Matrixable matrix_test = new Matrix(data1);

		Vector4f test = new Vector4f();
		matrix_test.mult(vector, test);

		assertEquals(expected, test);
	}

	@Test
	public void testMultAcrossVector4f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector4f vector = new Vector4f(1, 2, 3, 4);
		Vector4f expected = new Vector4f(90, 100, 110, 120);

		Matrixable matrix_test = new Matrix(data1);

		Vector4f test = matrix_test.multAcross(vector);
		assertEquals(expected, test);
	}

	@Test
	public void testMultAcrossVector4fVector4f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector4f vector = new Vector4f(1, 2, 3, 4);
		Vector4f expected = new Vector4f(90, 100, 110, 120);

		Matrixable matrix_test = new Matrix(data1);

		Vector4f test = new Vector4f();
		matrix_test.multAcross(vector, test);

		assertEquals(expected, test);
	}

	@Test
	public void testMultNormal() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f vector = new Vector3f(1, 2, 3);
		Vector3f expected = new Vector3f(14, 38, 62);

		Matrixable matrix_test = new Matrix(data1);

		Vector3f test = new Vector3f();

		matrix_test.multNormal(vector, test);

		assertEquals(expected, test);
	}

	@Test
	public void testMultNormalAcross() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f vector = new Vector3f(1, 2, 3);
		Vector3f expected = new Vector3f(38, 44, 50);

		Matrixable matrix_test = new Matrix(data1);

		Vector3f test = new Vector3f();

		matrix_test.multNormalAcross(vector, test);

		assertEquals(expected, test);
	}

	@Test
	public void testMultProj() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f vector = new Vector3f(1, 2, 3);
		Vector3f expected = new Vector3f(18, 46, 74);

		Matrixable matrix_test = new Matrix(data1);

		Vector3f test = new Vector3f();

		matrix_test.multProj(vector, test);

		assertEquals(expected, test);
	}

	@Test
	public void testMultAcrossVector3fVector3f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Vector3f vector = new Vector3f(1, 2, 3);
		Vector3f expected = new Vector3f(51, 58, 65);

		Matrixable matrix_test = new Matrix(data1);

		Vector3f test = new Vector3f();

		matrix_test.multAcross(vector, test);

		assertEquals(expected, test);
	}

	@Test
	public void testMultQuaternionQuaternion() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		Quaternion vector = new Quaternion(1, 2, 3, 4);
		Quaternion expected = new Quaternion(90, 100, 110, 120);

		Matrixable matrix_test = new Matrix(data1);

		Quaternion test = new Quaternion();

		matrix_test.mult(vector, test);

		assertEquals(expected, test);
	}

	@Test
	public void testMultFloatArray() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		float[] array = { 1, 2, 3, 4 };
		float[] expected = { 30, 70, 110, 150 };

		Matrixable matrix_test = new Matrix(data1);

		float[] test = matrix_test.mult(array);
		for (int r = 0; r < 4; r++) {
			assertEquals(expected[r], test[r], 0.0001);
		}
	}

	@Test
	public void testMultAcrossFloatArray() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };

		float[] array = { 1, 2, 3, 4 };
		float[] expected = { 90, 100, 110, 120 };

		Matrixable matrix_test = new Matrix(data1);

		float[] test = matrix_test.multAcross(array);

		for (int r = 0; r < 4; r++) {
			assertEquals(expected[r], test[r], 0.0001);
		}
	}

	@Test
	public void testInvert() {
		float[] data1 = { 4, 0, 0, 0, 0, 0, 2, 0, 0, 1, 2, 0, 1, 0, 0, 1 };
		float[] data2 = { (float) 0.25, 0, 0, 0, 0, -1, 1, 0, 0, (float) 0.5, 0, 0, (float) -0.25, 0, 0, 1 };
		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		Matrixable test = matrix_test.invert();

		assertArrayEquals(test.getMatrix(), expected.getMatrix());

	}

	@Test
	public void testInvertMatrix() {
		float[] data1 = { 4, 0, 0, 0, 0, 0, 2, 0, 0, 1, 2, 0, 1, 0, 0, 1 };
		float[] data2 = { (float) 0.25, 0, 0, 0, 0, -1, 1, 0, 0, (float) 0.5, 0, 0, (float) -0.25, 0, 0, 1 };
		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		Matrixable test = new Matrix(4);

		matrix_test.invert(test);

		assertArrayEquals(test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testInvertLocal() {
		float[] data1 = { 4, 0, 0, 0, 0, 0, 2, 0, 0, 1, 2, 0, 1, 0, 0, 1 };
		float[] data2 = { (float) 0.25, 0, 0, 0, 0, -1, 1, 0, 0, (float) 0.5, 0, 0, (float) -0.25, 0, 0, 1 };
		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		matrix_test.invertLocal();

		assertArrayEquals(matrix_test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testAdjoint() {
		float[] data1 = { 5, -2, 2, 7, 1, 0, 0, 3, -3, 1, 5, 0, 3, -1, -9, 4 };
		float[] data2 = { -12, 76, -60, -36, -56, 208, -82, -58, 4, 4, -2, -10, 4, 4, 20, 12 };
		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		Matrixable test = matrix_test.adjoint();

		assertArrayEquals(test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testAdjointMatrix() {
		float[] data1 = { 5, -2, 2, 7, 1, 0, 0, 3, -3, 1, 5, 0, 3, -1, -9, 4 };
		float[] data2 = { -12, 76, -60, -36, -56, 208, -82, -58, 4, 4, -2, -10, 4, 4, 20, 12 };
		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		Matrixable test = new Matrix(4);
		matrix_test.adjoint(test);

		assertArrayEquals(test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testCofactor() {
		float[] data1 = { 5, -2, 2, 7, 1, 0, 0, 3, -3, 1, 5, 0, 3, -1, -9, 4 };
		float[] data2 = { -12, 76, -60, -36, -56, 208, -82, -58, 4, 4, -2, -10, 4, 4, 20, 12 };
		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		Matrixable test = new Matrix(4);
		matrix_test.cofactor(test);

		assertArrayEquals(test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testRemoveRowCol() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 1,3,4,9,11,12,13,15,16};
		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);
		
		Matrixable test = matrix_test.removeRowCol(1, 1);

		assertArrayEquals(test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testDeterminant() {
		float[] data1 = {2,5,3,5,4,6,6,3,11,3,2,-2,4,-7,9,3};
		Matrixable matrix_test = new Matrix(data1);
		float expected =2960;
		
		float test = matrix_test.determinant();
		
		assertEquals(test, expected,0.002);
	}

	@Test
	public void testZero() {
		float[] data1 = {2,5,3,5,4,6,6,3,11,3,2,-2,4,-7,9,3};
		float[] data2 = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
		Matrixable matrix_test = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		matrix_test.zero();
		
		assertArrayEquals(matrix_test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testAdd() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32};
		Matrixable matrix_test = new Matrix(data1);
		Matrixable addMatrix = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		Matrixable test = matrix_test.add(addMatrix);
		
		assertArrayEquals(test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testAddLocal() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = { 2,4,6,8,10,12,14,16,18,20,22,24,26,28,30,32};
		Matrixable matrix_test = new Matrix(data1);
		Matrixable addMatrix = new Matrix(data1);
		Matrixable expected = new Matrix(data2);

		matrix_test.addLocal(addMatrix);
		
		assertArrayEquals(matrix_test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testToTranslationVector() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test = new Matrix(data1);
		
		Vector3f expected = new Vector3f(4,8,12);
		Vector3f test = matrix_test.toTranslationVector();
		
		assertEquals(test, expected);
	}

	@Test
	public void testToTranslationVectorVector3f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test = new Matrix(data1);
		
		Vector3f expected = new Vector3f(4,8,12);
		Vector3f test = new Vector3f(); 
		matrix_test.toTranslationVector(test);
		
		assertEquals(test, expected);
	}

	@Test
	public void testToRotationQuat() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test = new Matrix(data1);
		
		Quaternion expected = new Quaternion((float)0.029251702,(float) -0.09027569,(float) 0.018377202, (float)0.78618026);
		Quaternion test = matrix_test.toRotationQuat();
		
		assertEquals(test, expected);
	}

	@Test
	public void testToRotationQuatQuaternion() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test = new Matrix(data1);
		
		Quaternion expected = new Quaternion(0.029251702f, -0.09027569f, 0.018377202f, 0.78618026f);
		Quaternion test = new Quaternion();
		matrix_test.toRotationQuat(test);
		
		System.out.println(test);
		assertEquals(test, expected);
	}

	@Test
	public void testToRotationMatrix() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = {1,2,3,5,6,7,9,10,11};
		Matrixable matrix_test = new Matrix(data1);
		
		Matrixable expected = new Matrix(data2);
		Matrixable test = matrix_test.toRotationMatrix();
		
		assertArrayEquals(test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testToRotationMatrixMatrix() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		float[] data2 = {1,2,3,5,6,7,9,10,11};
		Matrixable matrix_test = new Matrix(data1);
		
		Matrixable expected = new Matrix(data2);
		Matrixable test = new Matrix(3);
		matrix_test.toRotationMatrix(test);
		
		assertArrayEquals(test.getMatrix(), expected.getMatrix());
	}

	@Test
	public void testToScaleVector() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test = new Matrix(data1);
		
		Vector3f expected = new Vector3f((float)10.34408,(float)11.83216, (float)13.379088);
		
		Vector3f test = matrix_test.toScaleVector();
		
		assertEquals(test, expected);
	}

	@Test
	public void testToScaleVectorVector3f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test = new Matrix(data1);
		
		Vector3f expected = new Vector3f((float)10.34408,(float)11.83216, (float)13.379088);
		
		Vector3f test = new Vector3f();
		matrix_test.toScaleVector(test);
		
		assertEquals(test, expected);
	}

	@Test
	public void testSetScaleFloatFloatFloat() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test = new Matrix(data1);
		
		float[] data2 = { (float) 0.09667365, (float) 0.3380617, (float) 0.6726916, 4, (float) 0.48336828, (float) 1.0141851, (float) 1.5696137, 8, (float) 0.8700629, (float) 1.6903085, (float) 2.4665358, 12, 13, 14, 15, 16 };
		Matrixable expected = new Matrix(data2);
		
		matrix_test.setScale(1,2,3);
		
		assertArrayEquals(expected.getMatrix(), matrix_test.getMatrix());
	}

	@Test
	public void testSetScaleVector3f() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test = new Matrix(data1);
		
		float[] data2 = { (float) 0.09667365, (float) 0.3380617, (float) 0.6726916, 4, (float) 0.48336828, (float) 1.0141851, (float) 1.5696137, 8, (float) 0.8700629, (float) 1.6903085, (float) 2.4665358, 12, 13, 14, 15, 16 };
		Matrixable expected = new Matrix(data2);
		
		Vector3f vector = new Vector3f(1,2,3);
		
		matrix_test.setScale(vector);
		
		assertArrayEquals(expected.getMatrix(), matrix_test.getMatrix());
	}

	@Test
	public void testEqualsObject() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test = new Matrix(data1);
		Matrixable equal = new Matrix(data1);
		
		float[] data3 = { 7, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable notEqual = new Matrix(data3);
		
		assertEquals(matrix_test.equals(equal), true);
		
		assertEquals(matrix_test.equals(notEqual), false);
	}

	@Test
	public void testIsIdentity() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test1 = new Matrix(data1);
		
		float[] data2 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
		Matrixable matrix_test2 = new Matrix(data2);
		
		assertEquals(matrix_test1.isIdentity(), false);
		
		assertEquals(matrix_test2.isIdentity(), true);
	}

	@Test
	public void testScale() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test = new Matrix(data1);
		
		float[] data2 = {1,4,9,4,5,12,21,8,9,20,33,12,13,28,45,16};
		Matrixable expected = new Matrix(data2);
		
		Vector3f vector = new Vector3f(1,2,3);
		
		matrix_test.scale(vector);
		
		assertArrayEquals(expected.getMatrix(), matrix_test.getMatrix());
	}

	@Test
	public void testEqualIdentity() {
		float[] data1 = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 };
		Matrixable matrix_test1 = new Matrix(data1);
		
		float[] data2 = {1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1};
		Matrixable matrix_test2 = new Matrix(data2);
		
		assertEquals(Matrix.equalIdentity(matrix_test1), false);
		
		assertEquals(Matrix.equalIdentity(matrix_test2), true);
	}

}
