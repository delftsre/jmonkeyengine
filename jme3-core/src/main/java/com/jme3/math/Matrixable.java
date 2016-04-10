package com.jme3.math;

import java.io.IOException;
import java.io.Serializable;
import java.nio.FloatBuffer;

import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.Savable;

public interface Matrixable extends Savable, Cloneable, Serializable  {

	void copy(Matrixable expected);

	void absoluteLocal();

	Matrixable normalizeLocal();

	Matrixable normalize(Matrixable store);

	void fromFrame(Vector3f location, Vector3f direction, Vector3f up, Vector3f left);

	void get(float[] matrix);

	void get(float[] matrix, boolean rowMajor);

	float get(int i, int j);

	float[] getColumn(int i);

	float[] getColumn(int i, float[] store);

	Vector3f getColumn(int i, Vector3f store);

	Vector3f getRow(int i);

	Vector3f getRow(int i, Vector3f store);

	void setColumn(int i, float[] column);

	Matrixable setColumn(int i, Vector3f column);

	Matrixable setRow(int i, Vector3f row);

	void set(int i, int j, float value);

	void set(float[][] matrix);

	Matrixable set(Matrixable matrix);

	void set(float[] matrix);

	void set(float[] matrix, boolean rowMajor);

	Matrixable set(Quaternion quaternion);

	Matrixable transpose();

	void fromAxes(Vector3f uAxis, Vector3f vAxis, Vector3f wAxis);

	Matrixable transposeLocal();

	Matrixable transposeNew();

	FloatBuffer toFloatBuffer();

	FloatBuffer toFloatBuffer(boolean columnMajor);

	FloatBuffer fillFloatBuffer(FloatBuffer fb);

	FloatBuffer fillFloatBuffer(FloatBuffer fb, boolean columnMajor);

	void fillFloatArray(float[] f, boolean columnMajor);

	Matrixable readFloatBuffer(FloatBuffer fb);

	Matrixable readFloatBuffer(FloatBuffer fb, boolean columnMajor);

	void loadIdentity();

	void fromFrustum(float near, float far, float left, float right, float top, float bottom, boolean parallel);

	void fromAngleAxis(float angle, Vector3f axis);

	void fromAngleNormalAxis(float angle, Vector3f axis);

	void multLocal(float scalar);

	Vector3f multLocal(Vector3f vec);

	Matrixable mult(float scalar);

	Matrixable mult(float scalar, Matrixable store);

	Matrixable mult(Matrixable in2);

	Matrixable mult(Matrixable in2, Matrixable store);

	Matrixable multLocal(Matrixable in2);

	Vector3f mult(Vector3f vec);

	Vector3f mult(Vector3f vec, Vector3f store);

	Vector4f mult(Vector4f vec);

	Vector4f mult(Vector4f vec, Vector4f store);

	Vector4f multAcross(Vector4f vec);

	Vector4f multAcross(Vector4f vec, Vector4f store);

	Vector3f multNormal(Vector3f vec, Vector3f store);

	Vector3f multNormalAcross(Vector3f vec, Vector3f store);

	float multProj(Vector3f vec, Vector3f store);

	Vector3f multAcross(Vector3f vec, Vector3f store);

	Quaternion mult(Quaternion vec, Quaternion store);

	float[] mult(float[] vec4f);

	float[] multAcross(float[] vec4f);

	Matrixable invert();

	Matrixable invert(Matrixable store);

	Matrixable invertLocal();

	Matrixable adjoint();

	Matrixable adjoint(Matrixable store);

	Matrixable cofactor(Matrixable store);

	Matrixable removeRowCol(int row, int col);

	void setTransform(Vector3f position, Vector3f scale, Matrixable rotMat);

	float determinant();

	Matrixable zero();

	Matrixable add(Matrixable mat);

	void addLocal(Matrixable mat);

	Vector3f toTranslationVector();

	void toTranslationVector(Vector3f vector);

	Quaternion toRotationQuat();

	void toRotationQuat(Quaternion q);

	Matrixable toRotationMatrix();

	void toRotationMatrix(Matrixable mat);

	Vector3f toScaleVector();

	void toScaleVector(Vector3f vector);

	void setScale(float x, float y, float z);

	void setScale(Vector3f scale);

	void setTranslation(float[] translation);

	void setTranslation(float x, float y, float z);

	void setTranslation(Vector3f translation);

	void setInverseTranslation(float[] translation);

	void angleRotation(Vector3f angles);

	void setRotationQuaternion(Quaternion quat);

	void setInverseRotationRadians(float[] angles);

	void setInverseRotationDegrees(float[] angles);

	void inverseTranslateVect(float[] vec);

	void inverseTranslateVect(Vector3f data);

	void translateVect(Vector3f data);

	void inverseRotateVect(Vector3f vec);

	void rotateVect(Vector3f vec);

	String toString();

	int hashCode();

	boolean equals(Object o);

	void write(JmeExporter e) throws IOException;

	void read(JmeImporter e) throws IOException;

	boolean isIdentity();

	void scale(Vector3f scale);

	void multLocal(Quaternion rotation);

	Matrixable clone();

	void fromStartEndVectors(Vector3f start, Vector3f end);

	int getM();

	void setM(int m);

	/* (non-Javadoc)
	 * @see com.jme3.math.Matrixable#getMatrix()
	 */
	float[][] getMatrix();

	/* (non-Javadoc)
	 * @see com.jme3.math.Matrixable#setMatrix(float[][])
	 */
	void setMatrix(float[][] matrix);

}