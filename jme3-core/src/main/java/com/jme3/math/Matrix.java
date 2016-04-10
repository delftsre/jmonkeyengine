package com.jme3.math;

import java.io.IOException;
import java.io.Serializable;
import java.nio.FloatBuffer;
import java.util.logging.Logger;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.util.BufferUtils;
import com.jme3.util.TempVars;

public final class Matrix implements Matrixable {
	private static final Logger logger = Logger.getLogger(Matrix.class.getName());
	private int M = 0; // number of rows & columns
	public float[][] matrix = null; // M-by-N array

    /**
     * Constructor instantiates a new <code>Matrix</code> that is set to the
     * identity matrix.
     * @return 
     * 
     * @param row
     *            the row size to make m x m matrix
     */
    public Matrix(int row) 
    {
    	this.M = row;
    	this.matrix = new float[M][M];
        loadIdentity();
    }
    
    /**
     * constructs a matrix with the given values.
     */
    public Matrix(float[] data) 
    {
    	int count = 0;
        M = (int) Math.sqrt(data.length);
        this.matrix = new float[M][M];
        for (int c = 0; c < M; c++)
        {
            for (int r = 0; r < M; r++)
            {
            	this.matrix[r][c] = data[count];
             	count++;
            }
        }
    }
    
    /**
     * Constructor instantiates a new <code>Matrix</code> that is set to the
     * provided matrix. This constructor copies a given Matrix. If the provided
     * matrix is null, the constructor sets the matrix to the identity.
     * 
     * @param mat
     *            the matrix to copy.
     */
    public Matrix(Matrixable mat) 
    {
    	this.M = mat.getM();
    	this.matrix = new float[M][M];
        copy(mat);
    }

	@Override
	public void copy(Matrixable matrix) {
		if (null == matrix) {
			loadIdentity();
		} else {
			this.matrix = matrix.getMatrix();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#absoluteLocal()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#absoluteLocal()
	 */
	@Override
	public void absoluteLocal() {
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < M; j++) {
				matrix[i][j] = FastMath.abs(matrix[i][j]);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#normalizeLocal()
	 */

	@Override
	public Matrixable normalizeLocal() {
		return normalize(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#normalize(com.jme3.math.Matrix)
	 */

	@Override
	public Matrixable normalize(Matrixable store) {
		if (store == null) {
			store = new Matrix(M);
		}
		float[][] matrixstore = new float[store.getM()][store.getM()];
		for (int i = 0; i < M; i++) {
			float mag = 1.0f / FastMath
					.sqrt(matrix[i][0] * matrix[i][0] + matrix[i][1] * matrix[i][1] + matrix[i][2] * matrix[i][2]);
			for (int j = 0; j < M; j++) {
				matrixstore[i][j] = matrix[i][j] * mag;
			}
			matrixstore[2][0] = matrixstore[0][1] * matrixstore[1][2] - matrixstore[1][1] * matrixstore[0][2];
			matrixstore[2][1] = matrixstore[1][0] * matrixstore[0][2] - matrixstore[0][0] * matrixstore[1][2];
			matrixstore[2][2] = matrixstore[0][0] * matrixstore[1][1] - matrixstore[1][0] * matrixstore[0][1];
		}
		store.set(matrixstore);
		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fromFrame(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f, com.jme3.math.Vector3f, com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fromFrame(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f, com.jme3.math.Vector3f, com.jme3.math.Vector3f)
	 */
	@Override
	public void fromFrame(Vector3f location, Vector3f direction, Vector3f up, Vector3f left) {
		TempVars vars = TempVars.get();
		try {
			loadIdentity();

			Vector3f fwdVector = vars.vect1.set(direction);
			float[] fwdVectorValues = { -fwdVector.x, -fwdVector.y, -fwdVector.z, fwdVector.dot(location) };

			Vector3f leftVector = vars.vect2.set(fwdVector).crossLocal(up);
			float[] leftVectorValues = { leftVector.x, leftVector.y, leftVector.z, -leftVector.dot(location) };

			Vector3f upVector = vars.vect3.set(leftVector).crossLocal(fwdVector);
			float[] upVectorValues = { upVector.x, upVector.y, upVector.z, -upVector.dot(location) };

			for (int c = 0; c < 4; c++) {
				matrix[c][0] = leftVectorValues[c];
			}

			for (int c = 0; c < 4; c++) {
				matrix[c][1] = upVectorValues[c];
			}

			for (int c = 0; c < 4; c++) {
				matrix[c][2] = fwdVectorValues[c];
			}
		} finally {
			vars.release();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#get(float[])
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#get(float[])
	 */
	@Override
	public void get(float[] matrix) {
		get(matrix, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#get(float[], boolean)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#get(float[], boolean)
	 */
	@Override
	public void get(float[] matrix, boolean rowMajor) {
		if (matrix.length != M * M) {
			throw new IllegalArgumentException("Array is incorrect size.");
		}
		int count = 0;
		if (rowMajor) {
			for (int c = 0; c < M; c++) {
				for (int r = 0; r < M; r++) {
					matrix[count] = this.matrix[r][c];
					count++;
				}
			}
		} else {
			for (int r = 0; r < M; r++) {
				for (int c = 0; c < M; c++) {
					matrix[count] = this.matrix[r][c];
					count++;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#get(int, int)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#get(int, int)
	 */
	@Override
	@SuppressWarnings("fallthrough")
	public float get(int i, int j) {
		for (int c = 0; c < M; c++) {
			for (int r = 0; r < M; r++) {
				if (r == i && c == j) {
					return matrix[c][r];
				}
			}
		}
		logger.warning("Invalid matrix index.");
		throw new IllegalArgumentException("Invalid indices into matrix.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getColumn(int)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getColumn(int)
	 */
	@Override
	public float[] getColumn(int i) {
		float[] nullValues = null;
		return getColumn(i, nullValues);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getColumn(int, float[])
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getColumn(int, float[])
	 */
	@Override
	public float[] getColumn(int i, float[] store) {
		if (store == null) {
			store = new float[M];
		}
		if (i > M) {
			logger.warning("Invalid column index.");
			throw new IllegalArgumentException("Invalid column index. " + i);
		}
		for (int r = 0; r < M; r++) {
			store[r] = matrix[i][r];
		}
		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getColumn(int, com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getColumn(int, com.jme3.math.Vector3f)
	 */
	@Override
	public Vector3f getColumn(int i, Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		float[] values = new float[M];
		for (int r = 0; r < 3; r++) {
			values[r] = matrix[i][r];
		}
		store.x = values[0];
		store.y = values[1];
		store.z = values[2];
		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getRow(int)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getRow(int)
	 */
	@Override
	public Vector3f getRow(int i) {
		return getRow(i, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getRow(int, com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getRow(int, com.jme3.math.Vector3f)
	 */
	@Override
	public Vector3f getRow(int i, Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		if (i > M) {
			logger.warning("Invalid row index.");
			throw new IllegalArgumentException("Invalid row index. " + i);
		}
		float[] values = new float[M];
		for (int r = 0; r < 3; r++) {
			values[r] = matrix[r][i];
		}
		store.x = values[0];
		store.y = values[1];
		store.z = values[2];

		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setColumn(int, float[])
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setColumn(int, float[])
	 */
	@Override
	public void setColumn(int i, float[] column) {
		if (column == null) {
			logger.warning("Column is null. Ignoring.");
			return;
		}
		if (i > M) {
			logger.warning("Invalid column index.");
			throw new IllegalArgumentException("Invalid column index. " + i);
		}
		for (int r = 0; r < M; r++) {
			matrix[i][r] = column[r];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setColumn(int, com.jme3.math.Vector3f)
	 */

	@Override
	public Matrixable setColumn(int i, Vector3f column) {
		if (column == null) {
			logger.warning("Column is null. Ignoring.");
			return this;
		}
		if (i > M) {
			logger.warning("Invalid column index.");
			throw new IllegalArgumentException("Invalid column index. " + i);
		}
		float[] values = { column.x, column.y, column.z };
		for (int r = 0; r < values.length; r++) {
			matrix[i][r] = values[r];
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setRow(int, com.jme3.math.Vector3f)
	 */

	@Override
	public Matrixable setRow(int i, Vector3f row) {
		if (row == null) {
			logger.warning("Column is null. Ignoring.");
			return this;
		}
		if (i > M) {
			logger.warning("Invalid column index.");
			throw new IllegalArgumentException("Invalid column index. " + i);
		}
		float[] values = { row.x, row.y, row.z };
		for (int r = 0; r < values.length; r++) {
			matrix[r][i] = values[r];
		}
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#set(int, int, float)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#set(int, int, float)
	 */
	@Override
	@SuppressWarnings("fallthrough")
	public void set(int i, int j, float value) {
		if (i > M || j > M) {
			logger.warning("Invalid matrix index.");
			throw new IllegalArgumentException("Invalid indices into matrix.");
		}
		matrix[j][i] = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#set(float[][])
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#set(float[][])
	 */
	@Override
	public void set(float[][] matrix) {
		if (matrix.length != M || matrix[0].length != M) {
			throw new IllegalArgumentException("Array must be of size of M.");
		}
		this.matrix = matrix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#set(com.jme3.math.Matrix)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#set(com.jme3.math.Matrix)
	 */
	@Override
	public Matrix set(Matrixable matrix) {
		if (null == matrix) {
			loadIdentity();
		}
		this.matrix = matrix.getMatrix();
		this.M = matrix.getM();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#set(float[])
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#set(float[])
	 */
	@Override
	public void set(float[] matrix) {
		set(matrix, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#set(float[], boolean)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#set(float[], boolean)
	 */
	@Override
	public void set(float[] matrix, boolean rowMajor) {
		if (matrix.length != M * M) {
			throw new IllegalArgumentException("Array must be of size " + (M * M));
		}
		if (rowMajor) {
			int count = 0;
			for (int c = 0; c < M; c++) {
				for (int r = 0; r < M; r++) {
					this.matrix[r][c] = matrix[count];
					count++;
				}
			}
		} else {
			int count = 0;
			for (int c = 0; c < M; c++) {
				for (int r = 0; r < M; r++) {
					this.matrix[c][r] = matrix[count];
					count++;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#set(com.jme3.math.Quaternion)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#set(com.jme3.math.Quaternion)
	 */
	@Override
	public Matrix set(Quaternion quaternion) {
		return quaternion.toRotationMatrix(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#transpose()
	 */

	@Override
	public Matrixable transpose() {
		float[] tmp = new float[M * M];
		get(tmp, true);
		Matrixable mat = new Matrix(tmp);
		return mat;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fromAxes(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f, com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fromAxes(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f, com.jme3.math.Vector3f)
	 */
	@Override
	public void fromAxes(Vector3f uAxis, Vector3f vAxis, Vector3f wAxis) {

		setColumn(0, uAxis);
		setColumn(1, vAxis);
		setColumn(2, wAxis);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#transposeLocal()
	 */

	@Override
	public Matrixable transposeLocal() {
		float[][] matrixT = new float[M][M];
		for (int c = 0; c < M; c++) {
			for (int r = 0; r < M; r++) {
				matrixT[r][c] = this.matrix[c][r];
			}
		}
		this.matrix = matrixT;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#transposeNew()
	 */

	@Override
	public Matrixable transposeNew() {
		Matrixable ret = new Matrix(M);
		ret.set(this);
		ret.transposeLocal();
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toFloatBuffer()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toFloatBuffer()
	 */
	@Override
	public FloatBuffer toFloatBuffer() {
		return toFloatBuffer(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toFloatBuffer(boolean)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toFloatBuffer(boolean)
	 */
	@Override
	public FloatBuffer toFloatBuffer(boolean columnMajor) {
		FloatBuffer fb = BufferUtils.createFloatBuffer(16);
		fillFloatBuffer(fb, columnMajor);
		fb.rewind();
		return fb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fillFloatBuffer(java.nio.FloatBuffer)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fillFloatBuffer(java.nio.FloatBuffer)
	 */
	@Override
	public FloatBuffer fillFloatBuffer(FloatBuffer fb) {
		return fillFloatBuffer(fb, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fillFloatBuffer(java.nio.FloatBuffer,
	 * boolean)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fillFloatBuffer(java.nio.FloatBuffer,
	 * boolean)
	 */
	@Override
	public FloatBuffer fillFloatBuffer(FloatBuffer fb, boolean columnMajor) {

		TempVars vars = TempVars.get();

		fillFloatArray(vars.matrixWrite, columnMajor);

		fb.put(vars.matrixWrite, 0, 16);
		vars.release();

		return fb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fillFloatArray(float[], boolean)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fillFloatArray(float[], boolean)
	 */
	@Override
	public void fillFloatArray(float[] f, boolean columnMajor) {
		int count = 0;
		if (columnMajor) {
			for (int r = 0; r < M; r++) {
				for (int c = 0; c < M; c++) {
					f[count] = this.matrix[r][c];
					count++;
				}
			}
		} else {
			for (int c = 0; c < M; c++) {
				for (int r = 0; r < M; r++) {
					f[count] = this.matrix[r][c];
					count++;
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#readFloatBuffer(java.nio.FloatBuffer)
	 */

	@Override
	public Matrixable readFloatBuffer(FloatBuffer fb) {
		return readFloatBuffer(fb, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#readFloatBuffer(java.nio.FloatBuffer,
	 * boolean)
	 */

	@Override
	public Matrixable readFloatBuffer(FloatBuffer fb, boolean columnMajor) {
		float[] bfArray = new float[M * M];
		float[] buffArray = new float[M * M];
		bfArray = fb.array();
		for (int i = 0; i < M * M; i++) {
			buffArray[i] = bfArray[i];
		}

		this.set(buffArray, columnMajor);

		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#loadIdentity()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#loadIdentity()
	 */
	@Override
	public void loadIdentity() {
		for (int c = 0; c < M; c++) {
			for (int r = 0; r < M; r++)
				this.matrix[r][c] = 0;
		}
		for (int i = 0; i < M; i++) {
			this.matrix[i][i] = 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fromFrustum(float, float, float, float,
	 * float, float, boolean)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fromFrustum(float, float, float, float,
	 * float, float, boolean)
	 */
	@Override
	public void fromFrustum(float near, float far, float left, float right, float top, float bottom, boolean parallel) {
		loadIdentity();
		if (parallel) {
			// scale
			matrix[0][0] = 2.0f / (right - left);
			matrix[1][1] = 2.0f / (top - bottom);
			matrix[2][2] = -2.0f / (far - near);
			matrix[3][3] = 1f;

			// translation
			matrix[3][0] = -(right + left) / (right - left);
			matrix[3][1] = -(top + bottom) / (top - bottom);
			matrix[3][2] = -(far + near) / (far - near);
		} else {
			matrix[0][0] = (2.0f * near) / (right - left);
			matrix[1][1] = (2.0f * near) / (top - bottom);
			matrix[2][3] = -1.0f;
			matrix[3][3] = -0.0f;

			// A
			matrix[2][0] = (right + left) / (right - left);

			// B
			matrix[2][1] = (top + bottom) / (top - bottom);

			// C
			matrix[2][2] = -(far + near) / (far - near);

			// D
			matrix[3][2] = -(2.0f * far * near) / (far - near);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fromAngleAxis(float,
	 * com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fromAngleAxis(float,
	 * com.jme3.math.Vector3f)
	 */
	@Override
	public void fromAngleAxis(float angle, Vector3f axis) {
		Vector3f normAxis = axis.normalize();
		fromAngleNormalAxis(angle, normAxis);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fromAngleNormalAxis(float,
	 * com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fromAngleNormalAxis(float,
	 * com.jme3.math.Vector3f)
	 */
	@Override
	public void fromAngleNormalAxis(float angle, Vector3f axis) {
		zero();
		float fCos = FastMath.cos(angle);
		float fSin = FastMath.sin(angle);
		float fOneMinusCos = ((float) 1.0) - fCos;
		float fX2 = axis.x * axis.x;
		float fY2 = axis.y * axis.y;
		float fZ2 = axis.z * axis.z;
		float fXYM = axis.x * axis.y * fOneMinusCos;
		float fXZM = axis.x * axis.z * fOneMinusCos;
		float fYZM = axis.y * axis.z * fOneMinusCos;
		float fXSin = axis.x * fSin;
		float fYSin = axis.y * fSin;
		float fZSin = axis.z * fSin;

		matrix[0][0] = fX2 * fOneMinusCos + fCos;
		matrix[1][0] = fXYM - fZSin;
		matrix[2][0] = fXZM + fYSin;

		matrix[0][1] = fXYM + fZSin;
		matrix[1][1] = fY2 * fOneMinusCos + fCos;
		matrix[2][1] = fYZM - fXSin;

		matrix[0][2] = fXZM - fYSin;
		matrix[1][2] = fYZM + fXSin;
		matrix[2][2] = fZ2 * fOneMinusCos + fCos;
		if (M == 4) {
			matrix[3][3] = 1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multLocal(float)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multLocal(float)
	 */
	@Override
	public void multLocal(float scalar) {
		for (int c = 0; c < M; c++) {
			for (int r = 0; r < M; r++) {
				matrix[r][c] *= scalar;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multLocal(com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multLocal(com.jme3.math.Vector3f)
	 */
	@Override
	public Vector3f multLocal(Vector3f vec) {
		if (vec == null) {
			return null;
		}
		mult(vec, vec);
		return vec;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(float)
	 */

	@Override
	public Matrixable mult(float scalar) {
		Matrixable out = new Matrix(M);
		out.set(this);
		out.multLocal(scalar);
		return out;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(float, com.jme3.math.Matrixable)
	 */

	@Override
	public Matrixable mult(float scalar, Matrixable store) {
		store.set(this);
		store.multLocal(scalar);
		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Matrix)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Matrix)
	 */
	@Override
	public Matrixable mult(Matrixable in2) {
		return mult(in2, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Matrix,
	 * com.jme3.math.Matrix)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Matrix,
	 * com.jme3.math.Matrix)
	 */
	@Override
	public Matrixable mult(Matrixable in2, Matrixable store) {
		if (store == null) {
			store = new Matrix(M);
		}
		store.zero();
		float[][] storeMatrix = new float[M][M];
		for (int r = 0; r < M; r++) {
			for (int c = 0; c < M; c++) {
				storeMatrix[r][c] += matrix[r][c] * in2.getMatrix()[r][c];

			}
		}
		store.set(storeMatrix);
		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multLocal(com.jme3.math.Matrix)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multLocal(com.jme3.math.Matrix)
	 */
	@Override
	public Matrixable multLocal(Matrixable in2) {
		Matrixable store = mult(in2, null);
		matrix = store.getMatrix();
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Vector3f)
	 */
	@Override
	public Vector3f mult(Vector3f vec) {
		return mult(vec, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f)
	 */
	@Override
	public Vector3f mult(Vector3f vec, Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		float[] storeArray = new float[3];
		float vx = vec.x, vy = vec.y, vz = vec.z;

		for (int r = 0; r < 3; r++) {
			if (M < 4) {
				storeArray[r] = (matrix[0][r] * vx) + (matrix[1][r] * vy) + (matrix[2][r] * vz);
			} else {
				storeArray[r] = (matrix[0][r] * vx) + (matrix[1][r] * vy) + (matrix[2][r] * vz) + matrix[3][r];
			}

		}
		store.x = storeArray[0];
		store.y = storeArray[1];
		store.z = storeArray[2];
		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Vector4f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Vector4f)
	 */
	@Override
	public Vector4f mult(Vector4f vec) {
		return mult(vec, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Vector4f,
	 * com.jme3.math.Vector4f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Vector4f,
	 * com.jme3.math.Vector4f)
	 */
	@Override
	public Vector4f mult(Vector4f vec, Vector4f store) {
		if (null == vec) {
			logger.warning("Source vector is null, null result returned.");
			return null;
		}
		if (store == null) {
			store = new Vector4f();
		}

		float[] storeArray = new float[4];
		float vx = vec.x, vy = vec.y, vz = vec.z, vw = vec.w;

		for (int r = 0; r < 4; r++) {
			storeArray[r] = (matrix[0][r] * vx) + (matrix[1][r] * vy) + (matrix[2][r] * vz) + (matrix[3][r] * vw);
		}
		store.x = storeArray[0];
		store.y = storeArray[1];
		store.z = storeArray[2];
		store.w = storeArray[3];
		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multAcross(com.jme3.math.Vector4f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multAcross(com.jme3.math.Vector4f)
	 */
	@Override
	public Vector4f multAcross(Vector4f vec) {
		return multAcross(vec, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multAcross(com.jme3.math.Vector4f,
	 * com.jme3.math.Vector4f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multAcross(com.jme3.math.Vector4f,
	 * com.jme3.math.Vector4f)
	 */
	@Override
	public Vector4f multAcross(Vector4f vec, Vector4f store) {
		if (null == vec) {
			logger.warning("Source vector is null, null result returned.");
			return null;
		}
		if (store == null) {
			store = new Vector4f();
		}

		float[] storeArray = new float[4];
		float vx = vec.x, vy = vec.y, vz = vec.z, vw = vec.w;

		for (int r = 0; r < 4; r++) {
			storeArray[r] = (matrix[r][0] * vx) + (matrix[r][1] * vy) + (matrix[r][2] * vz) + (matrix[r][3] * vw);
		}
		store.x = storeArray[0];
		store.y = storeArray[1];
		store.z = storeArray[2];
		store.w = storeArray[3];
		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multNormal(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multNormal(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f)
	 */
	@Override
	public Vector3f multNormal(Vector3f vec, Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		float[] storeArray = new float[3];
		float vx = vec.x, vy = vec.y, vz = vec.z;

		for (int r = 0; r < 3; r++) {
			storeArray[r] = (matrix[0][r] * vx) + (matrix[1][r] * vy) + (matrix[2][r] * vz);
		}
		store.x = storeArray[0];
		store.y = storeArray[1];
		store.z = storeArray[2];

		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multNormalAcross(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multNormalAcross(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f)
	 */
	@Override
	public Vector3f multNormalAcross(Vector3f vec, Vector3f store) {
		if (store == null) {
			store = new Vector3f();
		}
		float[] storeArray = new float[3];
		float vx = vec.x, vy = vec.y, vz = vec.z;

		for (int r = 0; r < 3; r++) {
			storeArray[r] = (matrix[r][0] * vx) + (matrix[r][1] * vy) + (matrix[r][2] * vz);
		}
		store.x = storeArray[0];
		store.y = storeArray[1];
		store.z = storeArray[2];

		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multProj(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multProj(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f)
	 */
	@Override
	public float multProj(Vector3f vec, Vector3f store) {
		float vx = vec.x, vy = vec.y, vz = vec.z;
		mult(vec, store);
		return matrix[0][3] * vx + matrix[1][3] * vy + matrix[2][3] * vz + matrix[3][3];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multAcross(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multAcross(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f)
	 */
	@Override
	public Vector3f multAcross(Vector3f vec, Vector3f store) {
		if (null == vec) {
			logger.warning("Source vector is null, null result returned.");
			return null;
		}
		if (store == null) {
			store = new Vector3f();
		}
		float[] storeArray = new float[3];
		float vx = vec.x, vy = vec.y, vz = vec.z;

		for (int r = 0; r < 3; r++) {
			storeArray[r] = (matrix[r][0] * vx) + (matrix[r][1] * vy) + (matrix[r][2] * vz) + matrix[r][3];
		}
		store.x = storeArray[0];
		store.y = storeArray[1];
		store.z = storeArray[2];
		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Quaternion,
	 * com.jme3.math.Quaternion)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(com.jme3.math.Quaternion,
	 * com.jme3.math.Quaternion)
	 */
	@Override
	public Quaternion mult(Quaternion vec, Quaternion store) {
		if (null == vec) {
			logger.warning("Source vector is null, null result returned.");
			return null;
		}
		if (store == null) {
			store = new Quaternion();
		}

		float[] storeArray = new float[4];
		for (int r = 0; r < 4; r++) {
			storeArray[r] = (matrix[r][0] * vec.x) + (matrix[r][1] * vec.y) + (matrix[r][2] * vec.z)
					+ (matrix[r][3] * vec.w);
		}
		store.x = storeArray[0];
		store.y = storeArray[1];
		store.z = storeArray[2];
		store.w = storeArray[3];
		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(float[])
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#mult(float[])
	 */
	@Override
	public float[] mult(float[] vec4f) {
		if (null == vec4f || vec4f.length != 4) {
			logger.warning("invalid array given, must be nonnull and length 4");
			return null;
		}
		float vx = vec4f[0], vy = vec4f[1], vz = vec4f[2], vw = vec4f[3];
		for (int r = 0; r < 4; r++) {
			vec4f[r] = (matrix[0][r] * vx) + (matrix[1][r] * vy) + (matrix[2][r] * vz) + (matrix[3][r] * vw);
		}
		return vec4f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multAcross(float[])
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multAcross(float[])
	 */
	@Override
	public float[] multAcross(float[] vec4f) {
		if (null == vec4f || vec4f.length != 4) {
			logger.warning("invalid array given, must be nonnull and length 4");
			return null;
		}
		float x = vec4f[0], y = vec4f[1], z = vec4f[2], w = vec4f[3];
		for (int r = 0; r < 4; r++) {
			vec4f[r] = (matrix[r][0] * x) + (matrix[r][1] * y) + (matrix[r][2] * z) + (matrix[r][3] * w);
		}
		return vec4f;
	}

	@Override
	public Matrixable invert() {
		return invert(null);
	}

	@Override
	public Matrixable invert(Matrixable store) {
		if (store == null) {
			store = new Matrix(M);
		}
		float det = determinant();
		if (FastMath.abs(det) <= FastMath.FLT_EPSILON) {
			return store.zero();
		}
		float x[][] = new float[M][M];
		float b[][] = new float[M][M];
		int index[] = new int[M];
		for (int i = 0; i < M; ++i)
			b[i][i] = 1;

		gaussian(matrix, index);

		for (int i = 0; i < M - 1; ++i) {
			for (int j = i + 1; j < M; ++j) {
				for (int k = 0; k < M; ++k) {
					b[index[j]][k] -= matrix[index[j]][i] * b[index[i]][k];
				}
			}
		}

		for (int i = 0; i < M; ++i) {
			x[M - 1][i] = b[index[M - 1]][i] / matrix[index[M - 1]][M - 1];
			for (int j = M - 2; j >= 0; --j) {
				x[j][i] = b[index[j]][i];
				for (int k = j + 1; k < M; ++k) {
					x[j][i] -= matrix[index[j]][k] * x[k][i];
				}
				x[j][i] /= matrix[index[j]][j];
			}
		}
		store.set(x);
		return store;
	}

	public static void gaussian(float a[][], int index[]) {
		int n = index.length;
		float c[] = new float[n];

		for (int i = 0; i < n; ++i)
			index[i] = i;

		for (int i = 0; i < n; ++i) {
			float c1 = 0;
			for (int j = 0; j < n; ++j) {
				float c0 = Math.abs(a[i][j]);
				if (c0 > c1)
					c1 = c0;
			}
			c[i] = c1;
		}

		int k = 0;
		for (int j = 0; j < n - 1; ++j) {
			float pi1 = 0;
			for (int i = j; i < n; ++i) {
				float pi0 = Math.abs(a[index[i]][j]);
				pi0 /= c[index[i]];
				if (pi0 > pi1) {
					pi1 = pi0;
					k = i;
				}
			}

			int itmp = index[j];
			index[j] = index[k];
			index[k] = itmp;
			for (int i = j + 1; i < n; ++i) {
				float pj = a[index[i]][j] / a[index[j]][j];

				a[index[i]][j] = pj;

				for (int l = j + 1; l < n; ++l)
					a[index[i]][l] -= pj * a[index[j]][l];
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#invertLocal()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#invertLocal()
	 */
	@Override
	public Matrixable invertLocal() {

		return invert(this);
	}

	@Override
	public Matrixable adjoint() {
		return adjoint(null);
	}

	@Override
	public Matrixable adjoint(Matrixable store) {
		if (store == null) {
			store = new Matrix(M);
		}
		cofactor(store);
		return store;
	}

	@Override
	public Matrixable cofactor(Matrixable store) {
		float[][] storeMatrix = new float[store.getM()][store.getM()];

		for (int c = 0; c < M; c++) {
			for (int r = 0; r < M; r++) {
				Matrixable tmp = removeRowCol(c, r);
				storeMatrix[r][c] = (int) (Math.pow(-1, c + r) * tmp.determinant());
			}
		}
		store.set(storeMatrix);
		return store;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#removeRowCol(int, int)
	 */

	@Override
	public Matrixable removeRowCol(int row, int col) {
		Matrix result = new Matrix(M - 1);

		int k = 0, l = 0;
		for (int i = 0; i < M; i++) {
			if (i == row)
				continue;
			for (int j = 0; j < M; j++) {
				if (j == col)
					continue;
				result.matrix[l][k] = matrix[i][j];

				k = (k + 1) % (M - 1);
				if (k == 0)
					l++;
			}
		}

		return result;
	}

	@Override
	public void setTransform(Vector3f position, Vector3f scale, Matrixable rotMat) {
		// Ordering:
		// 1. Scale
		// 2. Rotate
		// 3. Translate

		// Set up final matrix with scale, rotation and translation

		float[] scaleArray = { scale.x, scale.y, scale.z };
		float[] positionArray = { position.x, position.y, position.z };
		for (int c = 0; c < M; c++) {
			for (int r = 0; r < M; r++) {
				if (c == 3) {
					matrix[r][c] = 0;
					if (r == 3) {
						matrix[3][3] = 1;
					}
				} else {
					try {
						matrix[c][r] = scaleArray[c] * rotMat.getMatrix()[c][r];
					} catch (Exception e) {
						matrix[r][c] = positionArray[c];
					}
				}
			}
		}
	}

	@Override
	public float determinant() {
		float det = 0, sign = 1;
		int p = 0, q = 0;
		int n = M;
		if (n == 1) {
			det = matrix[0][0];
		} else {
			Matrix b = new Matrix(n - 1);
			for (int x = 0; x < n; x++) {
				p = 0;
				q = 0;
				for (int i = 1; i < n; i++) {
					for (int j = 0; j < n; j++) {
						if (j != x) {
							b.matrix[p][q++] = matrix[i][j];
							if (q % (n - 1) == 0) {
								p++;
								q = 0;
							}
						}
					}
				}
				det = det + matrix[0][x] * b.determinant() * sign;
				sign = -sign;
			}
		}
		return det;
	}

	@Override
	public Matrix zero() {
		for (int c = 0; c < M; c++) {
			for (int r = 0; r < M; r++) {
				matrix[r][c] = 0;
			}
		}
		return this;
	}

	@Override
	public Matrixable add(Matrixable mat) {
		Matrix result = new Matrix(M);
		for (int i = 0; i < M; i++)
			for (int j = 0; j < M; j++)
				result.matrix[i][j] = matrix[i][j] + mat.getMatrix()[i][j];
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#addLocal(com.jme3.math.Matrix)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#addLocal(com.jme3.math.Matrix)
	 */
	@Override
	public void addLocal(Matrixable mat) {
		for (int i = 0; i < M; i++)
			for (int j = 0; j < M; j++)
				matrix[i][j] += mat.getMatrix()[i][j];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toTranslationVector()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toTranslationVector()
	 */
	@Override
	public Vector3f toTranslationVector() {
		float[] vectorArray = new float[3];
		for (int j = 0; j < 3; j++) {
			vectorArray[j] = matrix[3][j];
		}
		Vector3f vector = new Vector3f(vectorArray[0], vectorArray[1], vectorArray[2]);
		return vector;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toTranslationVector(com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toTranslationVector(com.jme3.math.Vector3f)
	 */
	@Override
	public void toTranslationVector(Vector3f vector) {
		float[] vectorArray = new float[3];
		for (int j = 0; j < 3; j++) {
			vectorArray[j] = matrix[3][j];
		}
		vector.set(vectorArray[0], vectorArray[1], vectorArray[2]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toRotationQuat()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toRotationQuat()
	 */
	@Override
	public Quaternion toRotationQuat() {
		Quaternion quat = new Quaternion();
		quat.fromRotationMatrix(toRotationMatrix());
		return quat;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toRotationQuat(com.jme3.math.Quaternion)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toRotationQuat(com.jme3.math.Quaternion)
	 */
	@Override
	public void toRotationQuat(Quaternion q) {
		q.fromRotationMatrix(toRotationMatrix());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toRotationMatrix()
	 */

	@Override
	public Matrixable toRotationMatrix() {
		float[] data = new float[(M - 1) * (M - 1)];
		int count = 0;
		for (int i = 0; i < M - 1; i++) {
			for (int j = 0; j < M - 1; j++) {
				data[count] = matrix[j][i];
				count++;
			}
		}
		return new Matrix(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toRotationMatrix(com.jme3.math.Matrixable)
	 */

	@Override
	public void toRotationMatrix(Matrixable mat) {
		float[] data = new float[(M - 1) * (M - 1)];
		int count = 0;
		for (int i = 0; i < M - 1; i++) {
			for (int j = 0; j < M - 1; j++) {
				data[count] = matrix[j][i];
				count++;
			}
		}
		Matrix newMatrix = new Matrix(data);
		mat.set(newMatrix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toScaleVector()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toScaleVector()
	 */
	@Override
	public Vector3f toScaleVector() {
		Vector3f result = new Vector3f();
		this.toScaleVector(result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toScaleVector(com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toScaleVector(com.jme3.math.Vector3f)
	 */
	@Override
	public void toScaleVector(Vector3f vector) {
		float[] vectorArray = new float[3];

		for (int i = 0; i < 3; i++) {
			vectorArray[i] = (float) Math
					.sqrt(matrix[i][0] * matrix[i][0] + matrix[i][1] * matrix[i][1] + matrix[i][2] * matrix[i][2]);
		}
		vector.set(vectorArray[0], vectorArray[1], vectorArray[2]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setScale(float, float, float)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setScale(float, float, float)
	 */
	@Override
	public void setScale(float x, float y, float z) {
		TempVars vars = TempVars.get();

		float[] values = { x, y, z };

		for (int i = 0; i < 3; i++) {
			vars.vect1.set(matrix[i][0], matrix[i][1], matrix[i][2]);
			vars.vect1.normalizeLocal().multLocal(values[i]);
			float[] versValues = { vars.vect1.x, vars.vect1.y, vars.vect1.z };
			for (int j = 0; j < 3; j++) {
				matrix[i][j] = versValues[j];
			}
		}
		vars.release();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setScale(com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setScale(com.jme3.math.Vector3f)
	 */
	@Override
	public void setScale(Vector3f scale) {
		this.setScale(scale.x, scale.y, scale.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setTranslation(float[])
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setTranslation(float[])
	 */
	@Override
	public void setTranslation(float[] translation) {
		if (translation.length != 3) {
			throw new IllegalArgumentException("Translation size must be 3.");
		}
		for (int i = 0; i < translation.length; i++) {
			matrix[3][i] = translation[i];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setTranslation(float, float, float)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setTranslation(float, float, float)
	 */
	@Override
	public void setTranslation(float x, float y, float z) {
		float[] values = { x, y, z };
		for (int i = 0; i < values.length; i++) {
			matrix[3][i] = values[i];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setTranslation(com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setTranslation(com.jme3.math.Vector3f)
	 */
	@Override
	public void setTranslation(Vector3f translation) {
		float[] values = { translation.x, translation.y, translation.z };
		for (int i = 0; i < values.length; i++) {
			matrix[3][i] = values[i];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setInverseTranslation(float[])
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setInverseTranslation(float[])
	 */
	@Override
	public void setInverseTranslation(float[] translation) {
		for (int i = 0; i < translation.length; i++) {
			translation[i] = -translation[i];
		}
		setTranslation(translation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#angleRotation(com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#angleRotation(com.jme3.math.Vector3f)
	 */
	@Override
	public void angleRotation(Vector3f angles) {
		float angle;
		float sr, sp, sy, cr, cp, cy;

		angle = (angles.z * FastMath.DEG_TO_RAD);
		sy = FastMath.sin(angle);
		cy = FastMath.cos(angle);
		angle = (angles.y * FastMath.DEG_TO_RAD);
		sp = FastMath.sin(angle);
		cp = FastMath.cos(angle);
		angle = (angles.x * FastMath.DEG_TO_RAD);
		sr = FastMath.sin(angle);
		cr = FastMath.cos(angle);
		float[] values = { cp * cy, cp * sy, -sp, sr * sp * cy + cr * -sy, sr * sp * sy + cr * cy, sr * cp,
				(cr * sp * cy + -sr * -sy), (cr * sp * sy + -sr * cy), cr * cp, 0.0f, 0.0f, 0.0f };
		int count = 0;
		for (int i = 0; i < M; i++) {
			for (int j = 0; j < 3; j++) {
				matrix[i][j] = values[count];
				count++;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jme3.math.Matrixable#setRotationQuaternion(com.jme3.math.Quaternion)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jme3.math.Matrixable#setRotationQuaternion(com.jme3.math.Quaternion)
	 */
	@Override
	public void setRotationQuaternion(Quaternion quat) {
		quat.toRotationMatrix(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setInverseRotationRadians(float[])
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setInverseRotationRadians(float[])
	 */
	@Override
	public void setInverseRotationRadians(float[] angles) {
		if (angles.length != 3) {
			throw new IllegalArgumentException("Angles must be of size 3.");
		}
		double cr = FastMath.cos(angles[0]);
		double sr = FastMath.sin(angles[0]);
		double cp = FastMath.cos(angles[1]);
		double sp = FastMath.sin(angles[1]);
		double cy = FastMath.cos(angles[2]);
		double sy = FastMath.sin(angles[2]);

		double[] values = { (cp * cy), (cp * sy), (-sp), ((sr * sp) * cy - cr * sy), ((sr * sp) * sy + cr * cy),
				(sr * cp), ((cr * sp) * cy + sr * sy), ((cr * sp) * sy - sr * cy), (cr * cp) };
		int count = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				matrix[i][j] = (float) values[count];
				count++;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setInverseRotationDegrees(float[])
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setInverseRotationDegrees(float[])
	 */
	@Override
	public void setInverseRotationDegrees(float[] angles) {
		if (angles.length != 3) {
			throw new IllegalArgumentException("Angles must be of size 3.");
		}
		float vec[] = new float[3];
		for (int i = 0; i < 3; i++) {
			vec[i] = (angles[i] * FastMath.RAD_TO_DEG);
		}
		setInverseRotationRadians(vec);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#inverseTranslateVect(float[])
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#inverseTranslateVect(float[])
	 */
	@Override
	public void inverseTranslateVect(float[] vec) {
		if (vec.length != 3) {
			throw new IllegalArgumentException("vec must be of size 3.");
		}
		for (int i = 0; i < vec.length; i++) {
			vec[i] = vec[i] - matrix[3][i];
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jme3.math.Matrixable#inverseTranslateVect(com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jme3.math.Matrixable#inverseTranslateVect(com.jme3.math.Vector3f)
	 */
	@Override
	public void inverseTranslateVect(Vector3f data) {
		data.x -= matrix[3][0];
		data.y -= matrix[3][1];
		data.z -= matrix[3][2];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#translateVect(com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#translateVect(com.jme3.math.Vector3f)
	 */
	@Override
	public void translateVect(Vector3f data) {
		data.x += matrix[3][0];
		data.y += matrix[3][1];
		data.z += matrix[3][2];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#inverseRotateVect(com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#inverseRotateVect(com.jme3.math.Vector3f)
	 */
	@Override
	public void inverseRotateVect(Vector3f vec) {
		float vx = vec.x, vy = vec.y, vz = vec.z;
		float[] values = new float[3];
		for (int i = 0; i < 3; i++) {
			values[i] = vx * matrix[i][0] + vy * matrix[i][1] + vz * matrix[i][2];
		}
		vec.x = values[0];
		vec.y = values[1];
		vec.z = values[2];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#rotateVect(com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#rotateVect(com.jme3.math.Vector3f)
	 */
	@Override
	public void rotateVect(Vector3f vec) {
		float vx = vec.x, vy = vec.y, vz = vec.z;
		float[] values = new float[3];
		for (int i = 0; i < 3; i++) {
			values[i] = vx * matrix[0][i] + vy * matrix[1][i] + vz * matrix[2][i];
		}
		vec.x = values[0];
		vec.y = values[1];
		vec.z = values[2];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toString()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#toString()
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Matrix" + M + "f\n[\n");
		for (int c = 0; c < M; c++) {
			result.append(" ");
			for (int r = 0; r < M; r++) {
				result.append(matrix[r][c]);
				result.append("  ");
			}
			result.deleteCharAt(result.length() - 1);
			result.append("\n");
		}
		result.append("]");
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#hashCode()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 37;

		for (int c = 0; c < M; c++) {
			for (int r = 0; r < M; r++) {
				hash = 37 * hash + Float.floatToIntBits(matrix[r][c]);
			}
		}
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#equals(java.lang.Object)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Matrix) || o == null) {
			return false;
		}
		if (this.matrix == ((Matrix) o).matrix) {
			return true;
		}

		Matrix comp = (Matrix) o;

		if (comp.M != M) {
			return false;
		}

		for (int c = 0; c < M; c++)
			for (int r = 0; r < M; r++)
				if (matrix[r][c] != comp.matrix[r][c])
					return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#write(com.jme3.export.JmeExporter)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#write(com.jme3.export.JmeExporter)
	 */
	@Override
	public void write(JmeExporter e) throws IOException {
		OutputCapsule cap = e.getCapsule(this);
		Matrix tmpIdentity = new Matrix(M);
		for (int c = 0; c < M; c++) {
			for (int r = 0; r < M; r++) {
				cap.write(matrix[r][c], String.valueOf(r) + String.valueOf(c), (int) tmpIdentity.matrix[r][c]);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#read(com.jme3.export.JmeImporter)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#read(com.jme3.export.JmeImporter)
	 */
	@Override
	public void read(JmeImporter e) throws IOException {
		InputCapsule cap = e.getCapsule(this);
		Matrix tmpIdentity = new Matrix(M);
		for (int c = 0; c < M; c++) {
			for (int r = 0; r < M; r++) {
				matrix[r][c] = cap.readFloat(String.valueOf(r) + String.valueOf(c), (int) tmpIdentity.matrix[r][c]);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#isIdentity()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#isIdentity()
	 */
	@Override
	public boolean isIdentity() {
		Matrix tmpIdentity = new Matrix(M);
		if (tmpIdentity.M != M) {
			throw new RuntimeException("Illegal matrix dimensions.");
		}

		for (int c = 0; c < M; c++) {
			for (int r = 0; r < M; r++) {
				if (matrix[r][c] != tmpIdentity.matrix[r][c]) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#scale(com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#scale(com.jme3.math.Vector3f)
	 */
	@Override
	public void scale(Vector3f scale) {
		float[] scaleValues = { scale.getX(), scale.getY(), scale.getZ() };
		for (int c = 0; c < 3; c++) {
			for (int r = 0; r < M; r++) {
				matrix[c][r] *= scaleValues[c];
			}
		}
	}

	static boolean equalIdentity(Matrixable mat) {
		return mat.isIdentity();
	}

	// XXX: This tests more solid than converting the q to a matrix and
	// multiplying... why?
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multLocal(com.jme3.math.Quaternion)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#multLocal(com.jme3.math.Quaternion)
	 */
	@Override
	public void multLocal(Quaternion rotation) {
		Vector3f axis = new Vector3f();
		float angle = rotation.toAngleAxis(axis);
		Matrix matrix = new Matrix(M);
		matrix.fromAngleAxis(angle, axis);
		multLocal(matrix);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#clone()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#clone()
	 */
	@Override
	public Matrix clone() {
		try {
			return (Matrix) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(); // can not happen
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fromStartEndVectors(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#fromStartEndVectors(com.jme3.math.Vector3f,
	 * com.jme3.math.Vector3f)
	 */
	@Override
	public void fromStartEndVectors(Vector3f start, Vector3f end) {
		Vector3f v = new Vector3f();
		float e, h, f;

		start.cross(end, v);
		e = start.dot(end);
		f = (e < 0) ? -e : e;

		// if "from" and "to" vectors are nearly parallel
		if (f > 1.0f - FastMath.ZERO_TOLERANCE) {
			Vector3f u = new Vector3f();
			Vector3f x = new Vector3f();
			float c1, c2, c3; /* coefficients for later use */
			int i, j;

			x.x = (start.x > 0.0) ? start.x : -start.x;
			x.y = (start.y > 0.0) ? start.y : -start.y;
			x.z = (start.z > 0.0) ? start.z : -start.z;

			if (x.x < x.y) {
				if (x.x < x.z) {
					x.x = 1.0f;
					x.y = x.z = 0.0f;
				} else {
					x.z = 1.0f;
					x.x = x.y = 0.0f;
				}
			} else {
				if (x.y < x.z) {
					x.y = 1.0f;
					x.x = x.z = 0.0f;
				} else {
					x.z = 1.0f;
					x.x = x.y = 0.0f;
				}
			}

			u.x = x.x - start.x;
			u.y = x.y - start.y;
			u.z = x.z - start.z;
			v.x = x.x - end.x;
			v.y = x.y - end.y;
			v.z = x.z - end.z;

			c1 = 2.0f / u.dot(u);
			c2 = 2.0f / v.dot(v);
			c3 = c1 * c2 * u.dot(v);

			for (i = 0; i < 3; i++) {
				for (j = 0; j < 3; j++) {
					float val = -c1 * u.get(i) * u.get(j) - c2 * v.get(i) * v.get(j) + c3 * v.get(i) * u.get(j);
					set(i, j, val);
				}
				float val = get(i, i);
				set(i, i, val + 1.0f);
			}
		} else {
			// the most common case, unless "start"="end", or "start"=-"end"
			float hvx, hvz, hvxy, hvxz, hvyz;
			h = 1.0f / (1.0f + e);
			hvx = h * v.x;
			hvz = h * v.z;
			hvxy = hvx * v.y;
			hvxz = hvx * v.z;
			hvyz = hvz * v.y;
			set(0, 0, e + hvx * v.x);
			set(0, 1, hvxy - v.z);
			set(0, 2, hvxz + v.y);

			set(1, 0, hvxy + v.z);
			set(1, 1, e + h * v.y * v.y);
			set(1, 2, hvyz - v.x);

			set(2, 0, hvxz - v.y);
			set(2, 1, hvyz + v.x);
			set(2, 2, e + hvz * v.z);

		}
		transposeLocal();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getM()
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getM()
	 */
	@Override
	public int getM() {
		return M;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setM(int)
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setM(int)
	 */
	@Override
	public void setM(int m) {
		M = m;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getMatrix()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#getMatrix()
	 */
	@Override
	public float[][] getMatrix() {
		return matrix;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setMatrix(float[][])
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.math.Matrixable#setMatrix(float[][])
	 */
	@Override
	public void setMatrix(float[][] matrix) {
		this.matrix = matrix;
	}
}
