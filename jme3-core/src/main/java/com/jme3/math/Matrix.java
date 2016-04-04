package com.jme3.math;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.logging.Logger;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;
import com.jme3.util.BufferUtils;
import com.jme3.util.TempVars;

public final class Matrix  implements Savable, Cloneable, java.io.Serializable 
{
    private static final Logger logger = Logger.getLogger(Matrix.class.getName());
    private int M = 0;             // number of rows & columns
    public float[][] matrix = null;   // M-by-N array
	
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
    public Matrix(Matrix mat) 
    {
    	this.M = mat.M;
    	this.matrix = new float[M][M];
        copy(mat);
    }
    
    /**
     * <code>copy</code> transfers the contents of a given matrix to this
     * matrix. If a null matrix is supplied, this matrix is set to the identity
     * matrix.
     * 
     * @param matrix
     *            the matrix to copy.
     */
    public void copy(Matrix matrix) {
        if (null == matrix) 
        {
            loadIdentity();
        } 
        else 
        {
        	this.matrix = matrix.matrix;
        }
    }
    
    /**
     * Takes the absolute value of all matrix fields locally.
     */
    public void absoluteLocal() 
    {
		for (int i = 0; i < M; i++)
		{
			for (int j = 0; j < M; j++)
			{
				matrix[i][j] = FastMath.abs(matrix[i][j]);
			}
		}
    }
    
    /**
     * Normalize this matrix
     * @return this matrix once normalized.
     */
    public Matrix normalizeLocal() {
        return normalize(this);
    }
    
    /**
     * Normalize this matrix and store the result in the store parameter that is
     * returned.
     * 
     * Note that the original matrix is not altered.
     *
     * @param store the matrix to store the result of the normalization. If this
     * parameter is null a new one is created
     * @return the normalized matrix
     */
    public Matrix normalize(Matrix store) {
        if (store == null) {
            store = new Matrix(M);
        }

		for (int i = 0; i < M; i++)
		{
			float mag = 1.0f / FastMath.sqrt(matrix[i][0] * matrix[i][0]+ matrix[i][1] * matrix[i][1] + matrix[i][2] * matrix[i][2]);
			for (int j = 0; j < M; j++)
			{
				store.matrix[i][j] = matrix[i][j] * mag;				
			}
			store.matrix[2][0] = store.matrix[0][1] * store.matrix[1][2] - store.matrix[1][1] * store.matrix[0][2];
			store.matrix[2][1] = store.matrix[1][0] * store.matrix[0][2] - store.matrix[0][0] * store.matrix[1][2];
			store.matrix[2][2] = store.matrix[0][0] * store.matrix[1][1] - store.matrix[1][0] * store.matrix[0][1];
		}
        return store;
    }
    
    public void fromFrame(Vector3f location, Vector3f direction, Vector3f up, Vector3f left) {
        TempVars vars = TempVars.get();
        try 
        {
        	loadIdentity();
        	
            Vector3f fwdVector = vars.vect1.set(direction);
            float[] fwdVectorValues = {-fwdVector.x, -fwdVector.y, -fwdVector.z, fwdVector.dot(location)};
            
            Vector3f leftVector = vars.vect2.set(fwdVector).crossLocal(up);
            float[] leftVectorValues = {leftVector.x, leftVector.y, leftVector.z, -leftVector.dot(location)};
            
            Vector3f upVector = vars.vect3.set(leftVector).crossLocal(fwdVector);
            float[] upVectorValues = {upVector.x, upVector.y, upVector.z, -upVector.dot(location)};
            
        	for (int c = 0; c < 4; c++)
            {
                	matrix[c][0] = leftVectorValues[c];
            }

        	for (int c = 0; c < 4; c++)
            {
                	matrix[c][1] = upVectorValues[c];
            }

        	for (int c = 0; c < 4; c++)
            {
                	matrix[c][2] = fwdVectorValues[c];
            }
        } 
        finally 
        {
            vars.release();
        }
    }
    
    /**
     * <code>get</code> retrieves the values of this object into
     * a float array in row-major order.
     * 
     * @param matrix
     *            the matrix to set the values into.
     */
    public void get(float[] matrix) {
        get(matrix, true);
    }
    
    /**
     * <code>set</code> retrieves the values of this object into
     * a float array.
     * 
     * @param matrix
     *            the matrix to set the values into.
     * @param rowMajor
     *            whether the outgoing data is in row or column major order.
     */
    public void get(float[] matrix, boolean rowMajor) {
        if (matrix.length != M*M) 
        {
            throw new IllegalArgumentException("Array is incorrect size.");
        }
        int count = 0;
        if (rowMajor) 
        {
            for (int c = 0; c < M; c++)
            {
                for (int r = 0; r < M; r++)
                {
                	matrix[count] = this.matrix[r][c];
                 	count++;
                }
            }
        } 
        else 
        {
            for (int r = 0; r < M; r++)
            {
                for (int c = 0; c < M; c++)
                {
                	matrix[count] = this.matrix[r][c];
                 	count++;
                }
            }
        }
    }
    
    /**
     * <code>get</code> retrieves a value from the matrix at the given
     * position. If the position is invalid a <code>JmeException</code> is
     * thrown.
     * 
     * @param i
     *            the row index.
     * @param j
     *            the colum index.
     * @return the value at (i, j).
     */
    @SuppressWarnings("fallthrough")
    public float get(int i, int j) 
    {
        for (int c = 0; c < M; c++)
        {
            for (int r = 0; r < M; r++)
            {
            	if(r == i && c == j)
            	{
            		return matrix[r][c];
            	}
            }
        }
        logger.warning("Invalid matrix index.");
        throw new IllegalArgumentException("Invalid indices into matrix.");
    }
    
    /**
     * <code>getColumn</code> returns one of three columns specified by the
     * parameter. This column is returned as a float array of length 4.
     * 
     * @param i
     *            the column to retrieve. Must be between 0 and 3.
     * @return the column specified by the index.
     */
    public float[] getColumn(int i) 
    {
    	float[] nullValues = null;
        return getColumn(i, nullValues);
    }
    
    /**
     * <code>getColumn</code> returns one of three columns specified by the
     * parameter. This column is returned as a float[M].
     * 
     * @param i
     *            the column to retrieve. Must be between 0 and M.
     * @param store
     *            the float array to store the result in. if null, a new one
     *            is created.
     * @return the column specified by the index.
     */
    public float[] getColumn(int i, float[] store) 
    {
        if (store == null) 
        {
            store = new float[M];
        }
        if (i > M) 
        {
        	logger.warning("Invalid column index.");
            throw new IllegalArgumentException("Invalid column index. " + i);
        }
        for (int r = 0; r < M; r++)
        {
        	store[r] = matrix[i][r];
        }
        return store;
    }
    
    /**
     * <code>getColumn</code> returns one of three columns specified by the
     * parameter. This column is returned as a <code>Vector3f</code> object.
     * 
     * @param i
     *            the column to retrieve. Must be between 0 and 2.
     * @param store
     *            the vector object to store the result in. if null, a new one
     *            is created.
     * @return the column specified by the index.
     */
    public Vector3f getColumn(int i, Vector3f store) {
    	if (store == null) {
            store = new Vector3f();
        }
    	float[] values = new float[M];
        for (int r = 0; r < 3; r++)
        {
        	values[r] = matrix[i][r];
        }
        store.x = values[0];
        store.y = values[1];
        store.z = values[2];
        return store;
    }
    
    /**
     * <code>getColumn</code> returns one of three rows as specified by the
     * parameter. This row is returned as a <code>Vector3f</code> object.
     * 
     * @param i
     *            the row to retrieve. Must be between 0 and 2.
     * @return the row specified by the index.
     */
    public Vector3f getRow(int i) {
        return getRow(i, null);
    }
    
    /**
     * <code>getRow</code> returns one of three rows as specified by the
     * parameter. This row is returned as a <code>Vector3f</code> object.
     * 
     * @param i
     *            the row to retrieve. Must be between 0 and 2.
     * @param store
     *            the vector object to store the result in. if null, a new one
     *            is created.
     * @return the row specified by the index.
     */
    public Vector3f getRow(int i, Vector3f store) {
    	if (store == null) {
            store = new Vector3f();
        }
    	if(i > M )
    	{
    		logger.warning("Invalid row index.");
            throw new IllegalArgumentException("Invalid row index. " + i);
    	}
    	float[] values = new float[M];
        for (int r = 0; r < 3; r++)
        {
        	values[r] = matrix[r][i];
        }
        store.x = values[0];
        store.y = values[1];
        store.z = values[2];

        return store;
    }
    
    /**
     * 
     * <code>setColumn</code> sets a particular column of this matrix to that
     * represented by the provided vector.
     * 
     * @param i
     *            the column to set.
     * @param column
     *            the data to set.
     */
    public void setColumn(int i, float[] column) {
        if (column == null) 
        {
            logger.warning("Column is null. Ignoring.");
            return;
        }
        if (i > M) 
        {
        	logger.warning("Invalid column index.");
            throw new IllegalArgumentException("Invalid column index. " + i);
        }
        for (int r = 0; r < M; r++)
        {
        	matrix[i][r] = column[r];
        }
    }
    
    /**
     * 
     * <code>setColumn</code> sets a particular column of this matrix to that
     * represented by the provided vector.
     * 
     * @param i
     *            the column to set.
     * @param column
     *            the data to set.
     * @return this
     */
    public Matrix setColumn(int i, Vector3f column) {
        if (column == null) 
        {
            logger.warning("Column is null. Ignoring.");
            return this;
        }
        if (i > M) 
        {
        	logger.warning("Invalid column index.");
            throw new IllegalArgumentException("Invalid column index. " + i);
        }
        float[] values = {column.x,column.y,column.z};
        for (int r = 0; r < values.length; r++)
        {
        	matrix[i][r] = values[r];
        }
        return this;
    }
    
    /**
     * 
     * <code>setRow</code> sets a particular row of this matrix to that
     * represented by the provided vector.
     * 
     * @param i
     *            the row to set.
     * @param row
     *            the data to set.
     * @return this
     */
    public Matrix setRow(int i, Vector3f row) {
        if (row == null) 
        {
            logger.warning("Column is null. Ignoring.");
            return this;
        }
        if (i > M) 
        {
        	logger.warning("Invalid column index.");
            throw new IllegalArgumentException("Invalid column index. " + i);
        }
        float[] values = {row.x,row.y,row.z};
        for (int r = 0; r < values.length; r++)
        {
        	matrix[r][i] = values[r];
        }
        return this;
    }
    
    /**
     * <code>set</code> places a given value into the matrix at the given
     * position. If the position is invalid a <code>JmeException</code> is
     * thrown.
     * 
     * @param i
     *            the row index.
     * @param j
     *            the colum index.
     * @param value
     *            the value for (i, j).
     */
    @SuppressWarnings("fallthrough")
    public void set(int i, int j, float value) 
    {
        if (i > M || j > M ) 
        {
            logger.warning("Invalid matrix index.");
            throw new IllegalArgumentException("Invalid indices into matrix.");
        }
    	matrix[i][j] = value;
    }
    
    /**
     * <code>set</code> sets the values of this matrix from an array of
     * values.
     * 
     * @param matrix
     *            the matrix to set the value to.
     * @throws JmeException
     *             if the array is not of size 16.
     */
    public void set(float[][] matrix) 
    {
        if (matrix.length != M || matrix[0].length != M) 
        {
            throw new IllegalArgumentException("Array must be of size of M.");
        }
        this.matrix = matrix;
    }
    
    /**
     * <code>set</code> sets the values of this matrix from another matrix.
     *
     * @param matrix
     *            the matrix to read the value from.
     */
    public Matrix set(Matrix matrix) 
    {
        if (null == matrix) 
        {
            loadIdentity();
        }
    	this.matrix = matrix.matrix;
    	this.M = matrix.M;
        return this;
    }
    
    /**
     * <code>set</code> sets the values of this matrix from an array of
     * values assuming that the data is rowMajor order;
     * 
     * @param matrix
     *            the matrix to set the value to.
     */
    public void set(float[] matrix) 
    {
        set(matrix, true);
    }
    
    /**
     * <code>set</code> sets the values of this matrix from an array of
     * values;
     * 
     * @param matrix
     *            the matrix to set the value to.
     * @param rowMajor
     *            whether the incoming data is in row or column major order.
     */
    public void set(float[] matrix, boolean rowMajor) {
        if (matrix.length != M*M) 
        {
            throw new IllegalArgumentException("Array must be of size "+(M*M));
        }
        if (rowMajor) 
        {
        	int count = 0;
            for (int c = 0; c < M; c++)
            {
                for (int r = 0; r < M; r++)
                {
                	this.matrix[r][c] = matrix[count];
                	count++;
                }
            }
        } 
        else 
        {
        	int count = 0;
            for (int c = 0; c < M; c++)
            {
                for (int r = 0; r < M; r++)
                {
                	this.matrix[c][r] = matrix[count];
                	count++;
                }
            }
        }
    }
    
    /**
     * 
     * <code>set</code> defines the values of the matrix based on a supplied
     * <code>Quaternion</code>. It should be noted that all previous values
     * will be overridden.
     * 
     * @param quaternion
     *            the quaternion to create a rotational matrix from.
     * @return this
     */
    public Matrix set(Quaternion quaternion) {
        return quaternion.toRotationMatrix(this);
    }
    
    public Matrix transpose(int row, int column) 
    {
        float[] tmp = new float[row*column];
        get(tmp, true);
        Matrix mat = new Matrix(tmp);
        return mat;
    }
    
    /**
     * Recreate Matrix using the provided axis.
     * 
     * @param uAxis
     *            Vector3f
     * @param vAxis
     *            Vector3f
     * @param wAxis
     *            Vector3f
     */
    public void fromAxes(Vector3f uAxis, Vector3f vAxis, Vector3f wAxis) {
       
    	setColumn(0,uAxis);
    	setColumn(1,vAxis);
    	setColumn(2,wAxis);
    }
    
    /**
     * <code>transpose</code> locally transposes this Matrix.
     * 
     * @return this object for chaining.
     */
    public Matrix transposeLocal() 
    {
        float[][] matrixT = new float[M][M];
        for (int c = 0; c < M; c++)
        {
            for (int r = 0; r < M; r++)
            {
            	matrixT[r][c] = this.matrix[c][r];
            }
        }
        this.matrix = matrixT;
        return this;
    }
    
    /**
     * <code>transposeNew</code> returns a transposed version of this matrix.
     *
     * @return The new Matrix3f object.
     */
    public Matrix transposeNew() {
        Matrix ret = new Matrix(M);
        ret.set(this);
        ret.transposeLocal();
        return ret;
    }
    
    /**
     * <code>toFloatBuffer</code> returns a FloatBuffer object that contains
     * the matrix data.
     * 
     * @return matrix data as a FloatBuffer.
     */
    public FloatBuffer toFloatBuffer() 
    {
        return toFloatBuffer(false);
    }
    
    /**
     * <code>toFloatBuffer</code> returns a FloatBuffer object that contains the
     * matrix data.
     * 
     * @param columnMajor
     *            if true, this buffer should be filled with column major data,
     *            otherwise it will be filled row major.
     * @return matrix data as a FloatBuffer. The position is set to 0 for
     *         convenience.
     */
    public FloatBuffer toFloatBuffer(boolean columnMajor) 
    {
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        fillFloatBuffer(fb, columnMajor);
        fb.rewind();
        return fb;
    }
    
    /**
     * <code>fillFloatBuffer</code> fills a FloatBuffer object with
     * the matrix data.
     * @param fb the buffer to fill, must be correct size
     * @return matrix data as a FloatBuffer.
     */
    public FloatBuffer fillFloatBuffer(FloatBuffer fb) 
    {
        return fillFloatBuffer(fb, false);
    }
    
    /**
     * <code>fillFloatBuffer</code> fills a FloatBuffer object with the matrix
     * data.
     * 
     * @param fb
     *            the buffer to fill, starting at current position. Must have
     *            room for 16 more floats.
     * @param columnMajor
     *            if true, this buffer should be filled with column major data,
     *            otherwise it will be filled row major.
     * @return matrix data as a FloatBuffer. (position is advanced by 16 and any
     *         limit set is not changed).
     */
    public FloatBuffer fillFloatBuffer(FloatBuffer fb, boolean columnMajor) {

        TempVars vars = TempVars.get();

        fillFloatArray(vars.matrixWrite, columnMajor);

        fb.put(vars.matrixWrite, 0, 16);
        vars.release();

        return fb;
    }
    
    public void fillFloatArray(float[] f, boolean columnMajor) {
    	int count = 0;
    	if (columnMajor) 
        {
            for (int r = 0; r < M; r++)
            {
                for (int c = 0; c < M; c++)
                {
                	f[count] = this.matrix[r][c];
                 	count++;
                }
            }
        } 
        else 
        {
            for (int c = 0; c < M; c++)
            {
                for (int r = 0; r < M; r++)
                {
                	f[count] = this.matrix[r][c];
                 	count++;
                }
            }
        }
    }
    
    /**
     * <code>readFloatBuffer</code> reads value for this matrix from a FloatBuffer.
     * @param fb the buffer to read from, must be correct size
     * @return this data as a FloatBuffer.
     */
    public Matrix readFloatBuffer(FloatBuffer fb) 
    {
        return readFloatBuffer(fb, false);
    }
    
    /**
     * <code>readFloatBuffer</code> reads value for this matrix from a FloatBuffer.
     * @param fb the buffer to read from, must be correct size
     * @param columnMajor if true, this buffer should be filled with column
     * 		major data, otherwise it will be filled row major.
     * @return this data as a FloatBuffer.
     */
    public Matrix readFloatBuffer(FloatBuffer fb, boolean columnMajor) {
    	float[] bfArray = new float[M*M];
    	float[] buffArray = new float[M*M];
    	bfArray = fb.array();
		for(int i = 0; i < M*M; i++)
		{
			buffArray[i]=bfArray[i];
		}
		
    	this.set(buffArray, columnMajor);
    	
        return this;
    }
    
    /**
     * <code>loadIdentity</code> sets this matrix to the identity matrix,
     * namely all zeros with ones along the diagonal.
     *  
     */
    public void loadIdentity() 
    {
        for (int c = 0; c < M; c++) 
        {
            for (int r = 0; r < M; r++) 
            	this.matrix[r][c] = 0;
        }
        for (int i = 0; i < M; i++)
        {
            this.matrix[i][i] = 1;
        }
    }
    
    public void fromFrustum(float near, float far, float left, float right, float top, float bottom, boolean parallel) {
        loadIdentity();
        if (parallel) {
            // scale
            matrix[0][0] = 2.0f / (right -  left);
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
    
    /**
     * <code>fromAngleAxis</code> sets this matrix4f to the values specified
     * by an angle and an axis of rotation.  This method creates an object, so
     * use fromAngleNormalAxis if your axis is already normalized.
     * 
     * @param angle
     *            the angle to rotate (in radians).
     * @param axis
     *            the axis of rotation.
     */
    public void fromAngleAxis(float angle, Vector3f axis) 
    {
        Vector3f normAxis = axis.normalize();
        fromAngleNormalAxis(angle, normAxis);
    }
    
    /**
     * <code>fromAngleNormalAxis</code> sets this matrix4f to the values
     * specified by an angle and a normalized axis of rotation.
     * 
     * @param angle
     *            the angle to rotate (in radians).
     * @param axis
     *            the axis of rotation (already normalized).
     */
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
        if(M == 4)
        {
        	matrix[3][3] = 1;
        }
    }
    
    /**
     * <code>mult</code> multiplies this matrix by a scalar.
     * 
     * @param scalar
     *            the scalar to multiply this matrix by.
     */
    public void multLocal(float scalar) 
    {
        for (int c = 0; c < M; c++)
        {
            for (int r = 0; r < M; r++)
            {
            	matrix[r][c] *= scalar;
            }
        }
    }
    
    /**
     * <code>multLocal</code> multiplies this matrix by a given
     * <code>Vector3f</code> object. The result vector is stored inside the
     * passed vector, then returned . If the given vector is null, null will be
     * returned.
     * 
     * @param vec
     *            the vector to multiply this matrix by.
     * @return The passed vector after multiplication
     */
    public Vector3f multLocal(Vector3f vec) {
        if (vec == null) {
            return null;
        }
        mult(vec, vec);
        return vec;
    }
    
    public Matrix mult(float scalar) 
    {
        Matrix out = new Matrix(M);
        out.set(this);
        out.multLocal(scalar);
        return out;
    }
    
    public Matrix mult(float scalar, Matrix store) 
    {
        store.set(this);
        store.multLocal(scalar);
        return store;
    }

    /**
     * <code>mult</code> multiplies this matrix with another matrix. The
     * result matrix will then be returned. This matrix will be on the left hand
     * side, while the parameter matrix will be on the right.
     * 
     * @param in2
     *            the matrix to multiply this matrix by.
     * @return the resultant matrix
     */
    public Matrix mult(Matrix in2) 
    {
        return mult(in2, null);
    }
    
    /**
     * <code>mult</code> multiplies this matrix with another matrix. The
     * result matrix will then be returned. This matrix will be on the left hand
     * side, while the parameter matrix will be on the right.
     * 
     * @param in2
     *            the matrix to multiply this matrix by.
     * @param store
     *            where to store the result. It is safe for in2 and store to be
     *            the same object.
     * @return the resultant matrix
     */
    public Matrix mult(Matrix in2, Matrix store) {
        if (store == null) 
        {
            store = new Matrix(M);
        }
        store.zero();
        for (int r = 0; r < M; r++)
        {
            for (int c = 0; c < M; c++)
            {
		        for (int i = 0; i < M; i++)
		        {
		        	store.matrix[r][c] += matrix[r][i] *in2.matrix[i][c];
		        }
            }
        }
        return store;
    }
    
    /**
     * <code>mult</code> multiplies this matrix with another matrix. The
     * results are stored internally and a handle to this matrix will 
     * then be returned. This matrix will be on the left hand
     * side, while the parameter matrix will be on the right.
     * 
     * @param in2
     *            the matrix to multiply this matrix by.
     * @return the resultant matrix
     */
    public Matrix multLocal(Matrix in2) 
    {
    	Matrix store = mult(in2, null);
        matrix = store.matrix;
        return this;
    }
    
    /**
     * <code>mult</code> multiplies a vector about a rotation matrix. The
     * resulting vector is returned as a new Vector3f.
     * 
     * @param vec
     *            vec to multiply against.
     * @return the rotated vector.
     */
    public Vector3f mult(Vector3f vec) {
        return mult(vec, null);
    }
    
    /**
     * <code>mult</code> multiplies a vector about a rotation matrix and adds
     * translation. The resulting vector is returned.
     * 
     * @param vec
     *            vec to multiply against.
     * @param store
     *            a vector to store the result in. Created if null is passed.
     * @return the rotated vector.
     */
    public Vector3f mult(Vector3f vec, Vector3f store) {
        if (store == null) {
            store = new Vector3f();
        }
        float[] storeArray = new float[3];
        float vx = vec.x, vy = vec.y, vz = vec.z;
        
        for(int r = 0; r < 3;r++)
        {
        	if(M < 4)
        	{
        		storeArray[r] = (matrix[0][r] * vx) + (matrix[1][r] * vy) + (matrix[2][r] * vz);
        	}
        	else
        	{
        		storeArray[r] = (matrix[0][r] * vx) + (matrix[1][r] * vy) + (matrix[2][r] * vz) + matrix[3][r];
        	}
            	
        }
        store.x = storeArray[0];
        store.y = storeArray[1];
        store.z = storeArray[2];
        return store;
    }
    
    /**
     * <code>mult</code> multiplies a <code>Vector4f</code> about a rotation
     * matrix. The resulting vector is returned as a new <code>Vector4f</code>.
     *
     * @param vec
     *            vec to multiply against.
     * @return the rotated vector.
     */
    public Vector4f mult(Vector4f vec) {
        return mult(vec, null);
    }
    
    /**
     * <code>mult</code> multiplies a <code>Vector4f</code> about a rotation
     * matrix. The resulting vector is returned.
     *
     * @param vec
     *            vec to multiply against.
     * @param store
     *            a vector to store the result in. Created if null is passed.
     * @return the rotated vector.
     */
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
        
        for(int r = 0; r < 4;r++)
        {
            	storeArray[r] = (matrix[0][r] * vx) + (matrix[1][r] * vy) + (matrix[2][r] * vz) + (matrix[3][r]*vw);
        }
        store.x = storeArray[0];
        store.y = storeArray[1];
        store.z = storeArray[2];
        store.w = storeArray[3];
        return store;
    }
    
    /**
     * <code>mult</code> multiplies a vector about a rotation matrix. The
     * resulting vector is returned.
     *
     * @param vec
     *            vec to multiply against.
     * 
     * @return the rotated vector.
     */
    public Vector4f multAcross(Vector4f vec) {
        return multAcross(vec, null);
    }
    
    /**
     * <code>mult</code> multiplies a vector about a rotation matrix. The
     * resulting vector is returned.
     *
     * @param vec
     *            vec to multiply against.
     * @param store
     *            a vector to store the result in.  created if null is passed.
     * @return the rotated vector.
     */
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
        
        for(int r = 0; r < 4;r++)
        {
            	storeArray[r] = (matrix[r][0] * vx) + (matrix[r][1] * vy) + (matrix[r][2] * vz) + (matrix[r][3]*vw);
        }
        store.x = storeArray[0];
        store.y = storeArray[1];
        store.z = storeArray[2];
        store.w = storeArray[3];
        return store;
    }
    
    /**
     * <code>multNormal</code> multiplies a vector about a rotation matrix, but
     * does not add translation. The resulting vector is returned.
     *
     * @param vec
     *            vec to multiply against.
     * @param store
     *            a vector to store the result in. Created if null is passed.
     * @return the rotated vector.
     */
    public Vector3f multNormal(Vector3f vec, Vector3f store) {
        if (store == null) 
        {
            store = new Vector3f();
        }
        float[] storeArray = new float[3];
        float vx = vec.x, vy = vec.y, vz = vec.z;
        
        for(int r = 0; r < 3;r++)
        {
            	storeArray[r] = (matrix[0][r] * vx) + (matrix[1][r] * vy) + (matrix[2][r] * vz);
        }
        store.x = storeArray[0];
        store.y = storeArray[1];
        store.z = storeArray[2];
        
        return store;
    }
    
    /**
     * <code>multNormal</code> multiplies a vector about a rotation matrix, but
     * does not add translation. The resulting vector is returned.
     *
     * @param vec
     *            vec to multiply against.
     * @param store
     *            a vector to store the result in. Created if null is passed.
     * @return the rotated vector.
     */
    public Vector3f multNormalAcross(Vector3f vec, Vector3f store) {
        if (store == null) 
        {
            store = new Vector3f();
        }
        float[] storeArray = new float[3];
        float vx = vec.x, vy = vec.y, vz = vec.z;
        
        for(int r = 0; r < 3;r++)
        {
            	storeArray[r] = (matrix[r][0] * vx) + (matrix[r][1] * vy) + (matrix[r][2] * vz);
        }
        store.x = storeArray[0];
        store.y = storeArray[1];
        store.z = storeArray[2];
        
        return store;
    }

    /**
     * <code>mult</code> multiplies a vector about a rotation matrix and adds
     * translation. The w value is returned as a result of
     * multiplying the last column of the matrix by 1.0
     * 
     * @param vec
     *            vec to multiply against.
     * @param store
     *            a vector to store the result in. 
     * @return the W value
     */
    public float multProj(Vector3f vec, Vector3f store) {
        float vx = vec.x, vy = vec.y, vz = vec.z;
        mult(vec, store);
        return matrix[0][3] * vx + matrix[1][3] * vy + matrix[2][3] * vz + matrix[3][3];
    }
    
    /**
     * <code>mult</code> multiplies a vector about a rotation matrix. The
     * resulting vector is returned.
     * 
     * @param vec
     *            vec to multiply against.
     * @param store
     *            a vector to store the result in.  created if null is passed.
     * @return the rotated vector.
     */
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
        
        for(int r = 0; r < 3;r++)
        {
            	storeArray[r] = (matrix[r][0] * vx) + (matrix[r][1] * vy) + (matrix[r][2] * vz) + matrix[r][3];
        }
        store.x = storeArray[0];
        store.y = storeArray[1];
        store.z = storeArray[2];
        return store;
    }
    
    /**
     * <code>mult</code> multiplies a quaternion about a matrix. The
     * resulting vector is returned.
     *
     * @param vec
     *            vec to multiply against.
     * @param store
     *            a quaternion to store the result in.  created if null is passed.
     * @return store = this * vec
     */
    public Quaternion mult(Quaternion vec, Quaternion store) {
        if (null == vec) {
            logger.warning("Source vector is null, null result returned.");
            return null;
        }
        if (store == null) {
            store = new Quaternion();
        }

        float[] storeArray = new float[4];
        for(int r = 0; r < 4;r++)
        {
            	storeArray[r] = (matrix[r][0] * vec.x) + (matrix[r][1] * vec.y) + (matrix[r][2] * vec.z) + (matrix[r][3] * vec.w);
        }
        store.x = storeArray[0];
        store.y = storeArray[1];
        store.z = storeArray[2];
        store.w = storeArray[3];
        return store;
    }
    
    /**
     * <code>mult</code> multiplies an array of 4 floats against this rotation 
     * matrix. The results are stored directly in the array. (vec4f x mat4f)
     * 
     * @param vec4f
     *            float array (size 4) to multiply against the matrix.
     * @return the vec4f for chaining.
     */
    public float[] mult(float[] vec4f) 
    {
        if (null == vec4f || vec4f.length != 4) {
            logger.warning("invalid array given, must be nonnull and length 4");
            return null;
        }
        float vx = vec4f[0], vy = vec4f[1], vz = vec4f[2], vw = vec4f[3];
        for(int r = 0; r < 4;r++)
        {
        		vec4f[r] = (matrix[0][r] * vx) + (matrix[1][r] * vy) + (matrix[2][r] * vz) + (matrix[3][r] * vw);
        }
        return vec4f;
    }
    
    /**
     * <code>mult</code> multiplies an array of 4 floats against this rotation 
     * matrix. The results are stored directly in the array. (vec4f x mat4f)
     * 
     * @param vec4f
     *            float array (size 4) to multiply against the matrix.
     * @return the vec4f for chaining.
     */
    public float[] multAcross(float[] vec4f) {
        if (null == vec4f || vec4f.length != 4) {
            logger.warning("invalid array given, must be nonnull and length 4");
            return null;
        }
        float x = vec4f[0], y = vec4f[1], z = vec4f[2], w = vec4f[3];
        for(int r = 0; r < 4;r++)
        {
        		vec4f[r] = (matrix[r][0] * x) + (matrix[r][1] * y) + (matrix[r][2] * z) + (matrix[r][3] * w);
        }
        return vec4f;
    }
    
    /**
     * Inverts this matrix as a new Matrix.
     * 
     * @return The new inverse matrix
     */
    public Matrix invert() {
        return invert(null);
    }
    
    /**
     * Inverts this matrix and stores it in the given store.
     * 
     * @return The store
     */
    public Matrix invert(Matrix store) {
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
        for (int i=0; i<M; ++i) 
            b[i][i] = 1;
 
        gaussian(matrix, index);
 
        for (int i=0; i<M-1; ++i)
        {
            for (int j=i+1; j<M; ++j)
            {
                for (int k=0; k<M; ++k)
                {
                    b[index[j]][k]-= matrix[index[j]][i]*b[index[i]][k];
                }
            }
        }
 
        for (int i=0; i<M; ++i) 
        {	
            x[M-1][i] = b[index[M-1]][i]/matrix[index[M-1]][M-1];
            for (int j=M-2; j>=0; --j) 
            {
                x[j][i] = b[index[j]][i];
                for (int k=j+1; k<M; ++k) 
                {
                    x[j][i] -= matrix[index[j]][k]*x[k][i];
                }
                x[j][i] /= matrix[index[j]][j];
            }
        }
        store.matrix = x;
        return store;
    }
   
    public static void gaussian(float a[][], int index[]) 
    {
        int n = index.length;
        float c[] = new float[n];
 
        for (int i=0; i<n; ++i) 
            index[i] = i;
 
        for (int i=0; i<n; ++i) 
        {
        	float c1 = 0;
            for (int j=0; j<n; ++j) 
            {
            	float c0 = Math.abs(a[i][j]);
                if (c0 > c1) c1 = c0;
            }
            c[i] = c1;
        }
 
        int k = 0;
        for (int j=0; j<n-1; ++j) 
        {
        	float pi1 = 0;
            for (int i=j; i<n; ++i) 
            {
            	float pi0 = Math.abs(a[index[i]][j]);
                pi0 /= c[index[i]];
                if (pi0 > pi1) 
                {
                    pi1 = pi0;
                    k = i;
                }
            }
 
            int itmp = index[j];
            index[j] = index[k];
            index[k] = itmp;
            for (int i=j+1; i<n; ++i) 	
            {
            	float pj = a[index[i]][j]/a[index[j]][j];
 
                a[index[i]][j] = pj;
 
                for (int l=j+1; l<n; ++l)
                    a[index[i]][l] -= pj*a[index[j]][l];
            }
        }
    }
    
    /**
     * Inverts this matrix locally.
     * 
     * @return this
     */
    public Matrix invertLocal() {

        return invert(this);
    }
    
    /**
     * Returns a new matrix representing the adjoint of this matrix.
     * 
     * @return The adjoint matrix
     */
    public Matrix adjoint() {
        return adjoint(null);
    }
    
    /**
     * Places the adjoint of this matrix in store (creates store if null.)
     * 
     * @param store
     *            The matrix to store the result in.  If null, a new matrix is created.
     * @return store
     */
    public Matrix adjoint(Matrix store) {
        if (store == null) 
        {
            store = new Matrix(M);
        }
        cofactor(store);
        return store;
    }
    
    public Matrix cofactor(Matrix store) 
    {
        for (int c = 0; c < M; c++) 
        {
            for (int r = 0; r < M; r++) 
            {
            	Matrix tmp = removeRowCol(c, r);
            	store.matrix[r][c] = (int)(Math.pow(-1, c + r) * tmp.determinant());
            }
        }
        return store;
    }
    
    public Matrix removeRowCol(int row, int col) 
    {
        Matrix result = new Matrix(M - 1);

        int k = 0, l = 0;
        for (int i = 0; i < M; i++) 
        {
            if (i == row) continue;
            for (int j = 0; j < M; j++) 
            {
                if (j == col) continue;
                result.matrix[l][k] = matrix[i][j];

                k = (k + 1) % (M - 1);
                if (k == 0) l++;
            }
        }

        return result;
    }
    
    public void setTransform(Vector3f position, Vector3f scale, Matrix rotMat) {
        // Ordering:
        //    1. Scale
        //    2. Rotate
        //    3. Translate

        // Set up final matrix with scale, rotation and translation
    	
    	float[] scaleArray = {scale.x, scale.y, scale.z};
    	float[] positionArray = {position.x, position.y, position.z};
        for (int c = 0; c < M; c++) 
        {
        	for (int r = 0; r < M;r++) 
        	{
        		if(c == 3)
        		{
        			matrix[r][c] = 0;
        			if(r == 3)
            		{
        				matrix[3][3] = 1;
            		}
        		}
        		else
	        	{
        			try
		            {
	            		matrix[c][r] = scaleArray[c] * rotMat.matrix[c][r];
	            	}
	            	catch (Exception e)
	            	{
	            		matrix[r][c] = positionArray[c];
	            	}
	            }
            }
        }
    }
    
    /**
     * <code>determinant</code> generates the determinate of this matrix.
     * 
     * @return the determinate
     */    
    public float determinant() 
    {
    	float det = 0, sign = 1;
    	int p = 0, q = 0;
    	int n = M;
    	if(n==1)
    	{
    		det = matrix[0][0];
    	}
    	else
    	{
    		Matrix b = new Matrix(n-1);
    		for(int x = 0 ; x < n ; x++)
    		{
    			p=0;q=0;
    			for(int i = 1;i < n; i++)
    			{
    				for(int j = 0; j < n;j++)
    				{
    					if(j != x)
    					{
    						b.matrix[p][q++] = matrix[i][j];
    						if(q % (n-1) == 0){
    							p++;
    							q=0;
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
    
    /**
     * Sets all of the values in this matrix to zero.
     * 
     * @return this matrix
     */
    public Matrix zero() {
        for (int c = 0; c < M; c++)
        {
            for (int r = 0; r < M; r++)
            {
            	matrix[r][c] = 0;
            }
        }
        return this;
    }
    
    public Matrix add(Matrix mat) {
        Matrix result = new Matrix(M);
        for (int i = 0; i < M; i++)
            for (int j = 0; j < M; j++)
            	result.matrix[i][j] = matrix[i][j] + mat.matrix[i][j];
        return result;
    }
    
    /**
     * <code>add</code> adds the values of a parameter matrix to this matrix.
     * 
     * @param mat
     *            the matrix to add to this.
     */
    public void addLocal(Matrix mat) {
        for (int i = 0; i < M; i++)
            for (int j = 0; j < M; j++)
            	matrix[i][j] += mat.matrix[i][j];
    }
    
    public Vector3f toTranslationVector() 
    {
    	float[] vectorArray = new float[3];
    	for (int j = 0; j < 3; j++)
    	{
    		vectorArray[j] = matrix[3][j];
    	}
    	Vector3f vector = new Vector3f(vectorArray[0],vectorArray[1],vectorArray[2]);
        return vector;
    }
    
    public void toTranslationVector(Vector3f vector) {
    	float[] vectorArray = new float[3];
    	for (int j = 0; j < 3; j++)
    	{
    		vectorArray[j] = matrix[3][j];
    	}
    	vector.set(vectorArray[0],vectorArray[1],vectorArray[2]);
    }
    
    public Quaternion toRotationQuat() 
    {
        Quaternion quat = new Quaternion();
        quat.fromRotationMatrix(toRotationMatrix());
        return quat;
    }
    
    public void toRotationQuat(Quaternion q) 
    {
        q.fromRotationMatrix(toRotationMatrix());
    }
    
    public Matrix toRotationMatrix() {
    	float[] data = new float[(M-1)*(M-1)];
    	int count=0;
    	for (int i = 0; i < M-1; i++)
    	{
    		for (int j = 0; j < M-1; j++)	
    		{
    			data[count] = matrix[j][i];
    			count++;
    		}
    	}
        return new Matrix(data);
    }
    
    public void toRotationMatrix(Matrix mat) 
    {
    	float[] data = new float[(M-1)*(M-1)];
    	int count=0;
    	for (int i = 0; i < M-1; i++)
    	{
    		for (int j = 0; j < M-1; j++)	
    		{
    			data[count] = matrix[j][i];
    			count++;
    		}
    	}
    	Matrix newMatrix = new Matrix(data);
    	mat.set(newMatrix);
	}
    
	/**
	 * Retreives the scale vector from the matrix.
	 * 
	 * @return the scale vector
	 */
	public Vector3f toScaleVector() {
		Vector3f result = new Vector3f();
		this.toScaleVector(result);
		return result;
	}
	
	/**
	 * Retreives the scale vector from the matrix and stores it into a given
	 * vector.
	 * 
	 * @param the
	 *            vector where the scale will be stored
	 */
	public void toScaleVector(Vector3f vector) 
	{
		float[] vectorArray = new float[3];
				
    	for (int i = 0; i < 3; i++)
    	{
    		vectorArray[i] = (float) Math.sqrt(matrix[i][0] * matrix[i][0] + matrix[i][1] * matrix[i][1] + matrix[i][2] * matrix[i][2]);
    	}
		vector.set(vectorArray[0], vectorArray[1], vectorArray[2]);
    }
	
	  /**
     * Sets the scale.
     * 
     * @param x
     *            the X scale
     * @param y
     *            the Y scale
     * @param z
     *            the Z scale
     */
    public void setScale(float x, float y, float z) {
        TempVars vars = TempVars.get();
        
        float[] values = {x,y,z};
        
    	for (int i = 0; i < 3; i++)
    	{
            vars.vect1.set(matrix[i][0], matrix[i][1], matrix[i][2]);
            vars.vect1.normalizeLocal().multLocal(values[i]);
            float[] versValues = {vars.vect1.x,vars.vect1.y,vars.vect1.z};
    		for (int j = 0; j < 3; j++)	
    		{
    			matrix[i][j] = versValues[j];
    		}
    	}
        vars.release();
    }
    
    /**
     * Sets the scale.
     * 
     * @param scale
     *            the scale vector to set
     */
    public void setScale(Vector3f scale) {
        this.setScale(scale.x, scale.y, scale.z);
    }
    
    /**
     * <code>setTranslation</code> will set the matrix's translation values.
     * 
     * @param translation
     *            the new values for the translation.
     * @throws JmeException
     *             if translation is not size 3.
     */
    public void setTranslation(float[] translation) {
        if (translation.length != 3) {
            throw new IllegalArgumentException(
                    "Translation size must be 3.");
        }
    	for (int i = 0; i < translation.length; i++)
    	{
    		matrix[3][i] = translation[i];
    	}
    }
    
    /**
     * <code>setTranslation</code> will set the matrix's translation values.
     * 
     * @param x
     *            value of the translation on the x axis
     * @param y
     *            value of the translation on the y axis
     * @param z
     *            value of the translation on the z axis
     */
    public void setTranslation(float x, float y, float z) {
    	float[] values = {x,y,z};
    	for (int i = 0; i < values.length; i++)
    	{
    		matrix[3][i] = values[i];
    	}
    }
    
    /**
     * <code>setTranslation</code> will set the matrix's translation values.
     *
     * @param translation
     *            the new values for the translation.
     */
    public void setTranslation(Vector3f translation) {
       	float[] values = {translation.x,translation.y,translation.z};
    	for (int i = 0; i < values.length; i++)
    	{
    		matrix[3][i] = values[i];
    	}
    }
    
    /**
     * <code>setInverseTranslation</code> will set the matrix's inverse
     * translation values.
     * 
     * @param translation
     *            the new values for the inverse translation.
     * @throws JmeException
     *             if translation is not size 3.
     */
    public void setInverseTranslation(float[] translation) 
    {
    	for (int i = 0; i < translation.length; i++)
    	{
    		translation[i] = -translation[i];
    	}
    	setTranslation(translation);
    }
    
    /**
     * <code>angleRotation</code> sets this matrix to that of a rotation about
     * three axes (x, y, z). Where each axis has a specified rotation in
     * degrees. These rotations are expressed in a single <code>Vector3f</code>
     * object.
     * 
     * @param angles
     *            the angles to rotate.
     */
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
        float[] values= {cp * cy, cp * sy,-sp,
        		sr * sp * cy + cr * -sy, sr * sp * sy + cr * cy, sr * cp,
        		(cr * sp * cy + -sr * -sy), (cr * sp * sy + -sr * cy),cr * cp,
        		0.0f, 0.0f, 0.0f};
        int count = 0;
    	for (int i = 0; i < M; i++)
    	{
        	for (int j = 0; j < 3; j++)
        	{
        		matrix[i][j]= values[count];
        		count++;
        	}
    	}
    }

    /**
     * <code>setRotationQuaternion</code> builds a rotation from a
     * <code>Quaternion</code>.
     * 
     * @param quat
     *            the quaternion to build the rotation from.
     * @throws NullPointerException
     *             if quat is null.
     */
    public void setRotationQuaternion(Quaternion quat) {
        quat.toRotationMatrix(this);
    }
    
    /**
     * <code>setInverseRotationRadians</code> builds an inverted rotation from
     * Euler angles that are in radians.
     * 
     * @param angles
     *            the Euler angles in radians.
     * @throws JmeException
     *             if angles is not size 3.
     */
    public void setInverseRotationRadians(float[] angles) {
        if (angles.length != 3) {
            throw new IllegalArgumentException(
                    "Angles must be of size 3.");
        }
        double cr = FastMath.cos(angles[0]);
        double sr = FastMath.sin(angles[0]);
        double cp = FastMath.cos(angles[1]);
        double sp = FastMath.sin(angles[1]);
        double cy = FastMath.cos(angles[2]);
        double sy = FastMath.sin(angles[2]);
        
        double[] values= {(cp * cy), (cp * sy), (-sp),
        		((sr * sp) * cy - cr * sy), ((sr * sp) * sy + cr * cy), (sr * cp),
        		((cr * sp) * cy + sr * sy), ((cr * sp) * sy - sr * cy), (cr * cp)};
        int count = 0;
    	for (int i = 0; i < 3; i++)
    	{
        	for (int j = 0; j < 3; j++)
        	{
        		matrix[i][j]= (float)values[count];
        		count++;
        	}
    	}
    }
    
    /**
     * <code>setInverseRotationDegrees</code> builds an inverted rotation from
     * Euler angles that are in degrees.
     * 
     * @param angles
     *            the Euler angles in degrees.
     * @throws JmeException
     *             if angles is not size 3.
     */
    public void setInverseRotationDegrees(float[] angles) {
        if (angles.length != 3) {
            throw new IllegalArgumentException(
                    "Angles must be of size 3.");
        }
        float vec[] = new float[3];
    	for (int i = 0; i < 3; i++)
    	{
    		vec[i] = (angles[i] * FastMath.RAD_TO_DEG);
    	}
        setInverseRotationRadians(vec);
    }
    
    /**
     * 
     * <code>inverseTranslateVect</code> translates a given Vector3f by the
     * translation part of this matrix.
     * 
     * @param vec
     *            the Vector3f data to be translated.
     * @throws JmeException
     *             if the size of the Vector3f is not 3.
     */
    public void inverseTranslateVect(float[] vec) {
        if (vec.length != 3) {
            throw new IllegalArgumentException(
                    "vec must be of size 3.");
        }
    	for (int i = 0; i < vec.length; i++)
    	{
    		vec[i] = vec[i] - matrix[3][i];
    	}
    }
    
    /**
     * 
     * <code>inverseTranslateVect</code> translates a given Vector3f by the
     * translation part of this matrix.
     * 
     * @param data
     *            the Vector3f to be translated.
     * @throws JmeException
     *             if the size of the Vector3f is not 3.
     */
    public void inverseTranslateVect(Vector3f data) {
        data.x -= matrix[3][0];
        data.y -= matrix[3][1];
        data.z -= matrix[3][2];
    }
    
    /**
     * 
     * <code>inverseTranslateVect</code> translates a given Vector3f by the
     * translation part of this matrix.
     * 
     * @param data
     *            the Vector3f to be translated.
     * @throws JmeException
     *             if the size of the Vector3f is not 3.
     */
    public void translateVect(Vector3f data) {
        data.x += matrix[3][0];
        data.y += matrix[3][1];
        data.z += matrix[3][2];
    }
    
    /**
     * 
     * <code>inverseRotateVect</code> rotates a given Vector3f by the rotation
     * part of this matrix.
     * 
     * @param vec
     *            the Vector3f to be rotated.
     */
    public void inverseRotateVect(Vector3f vec) {
        float vx = vec.x, vy = vec.y, vz = vec.z;
        float[] values = new float[3];
    	for (int i = 0; i < 3; i++)
    	{
    		values[i] = vx * matrix[i][0] + vy * matrix[i][1] + vz * matrix[i][2];
    	}
        vec.x = values[0];
        vec.y = values[1];
        vec.z = values[2];
    }
    
    public void rotateVect(Vector3f vec) {
        float vx = vec.x, vy = vec.y, vz = vec.z;
        float[] values = new float[3];
    	for (int i = 0; i < 3; i++)
    	{
    		values[i] = vx * matrix[0][i] + vy * matrix[1][i] + vz * matrix[2][i];
    	}
        vec.x = values[0];
        vec.y = values[1];
        vec.z = values[2];
    }
    
    /**
     * <code>toString</code> returns the string representation of this object.
     * It is in a format of a 4x4 matrix. For example, an identity matrix would
     * be represented by the following string. com.jme.math.Matrix3f <br>[<br>
     * 1.0  0.0  0.0  0.0 <br>
     * 0.0  1.0  0.0  0.0 <br>
     * 0.0  0.0  1.0  0.0 <br>
     * 0.0  0.0  0.0  1.0 <br>]<br>
     * 
     * @return the string representation of this object.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Matrix"+M+"f\n[\n");
        for (int c = 0; c < M; c++) 
        {
        	result.append(" ");
            for (int r = 0; r < M; r++) 
            {
            	result.append(matrix[r][c]);
            	result.append("  ");
            }
            result.deleteCharAt(result.length()-1);
            result.append("\n");
        }
        result.append("]");
        return result.toString();
    }
    
    /**
     * 
     * <code>hashCode</code> returns the hash code value as an integer and is
     * supported for the benefit of hashing based collection classes such as
     * Hashtable, HashMap, HashSet etc.
     * 
     * @return the hashcode for this instance of Matrix4f.
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 37;
        
        for (int c = 0; c < M; c++)
        {
            for (int r = 0; r < M; r++)
            {
            	hash = 37 * hash + Float.floatToIntBits(matrix[r][c]);
            }
        }
        return hash;
    }

    
    /**
     * are these two matrices the same? they are is they both have the same mXX values.
     *
     * @param o
     *            the object to compare for equality
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object o) 
    {
        if (!(o instanceof Matrix) || o == null) 
        {
            return false;
        }
        if (this == o) 
        {
            return true;
        }
        
        Matrix comp = (Matrix) o;
        
        if (comp.M != M) 
        {
        	return false;
        }
        
        for (int c = 0; c < M; c++)
            for (int r = 0; r < M; r++)
                if (matrix[r][c] != comp.matrix[r][c]) return false;
        return true;
    }
    
    public void write(JmeExporter e) throws IOException 
    {
        OutputCapsule cap = e.getCapsule(this);
        Matrix tmpIdentity = new Matrix(M);
        for (int c = 0; c < M; c++)
        {
            for (int r = 0; r < M; r++)
            {
            	cap.write(matrix[r][c], String.valueOf(r)+String.valueOf(c), (int) tmpIdentity.matrix[r][c]);
            }
        }
    }
    
    public void read(JmeImporter e) throws IOException 
    {
    	InputCapsule cap = e.getCapsule(this);
        Matrix tmpIdentity = new Matrix(M);
        for (int c = 0; c < M; c++)
        {
            for (int r = 0; r < M; r++)
            {
            	matrix[r][c] = cap.readFloat(String.valueOf(r)+String.valueOf(c), (int) tmpIdentity.matrix[r][c]);
            }
        }
    }
    
    /**
     * @return true if this matrix is identity
     */
    public boolean isIdentity() 
    {
    	Matrix tmpIdentity = new Matrix(M);
        if (tmpIdentity.M != M ) 
        {
        	throw new RuntimeException("Illegal matrix dimensions.");
        }
        
        for (int c = 0; c < M; c++)
        {
            for (int r = 0; r < M; r++)
            {
                if (matrix[r][c] != tmpIdentity.matrix[r][c]) 
                {
                	return false;
                }
            }
        }
        return true;
    }
    
    /**
     * Apply a scale to this matrix.
     * 
     * @param scale
     *            the scale to apply
     */
    public void scale(Vector3f scale) 
    {
    	float[] scaleValues = {scale.getX(),scale.getY(),scale.getZ()};
    	for (int c = 0; c < 3; c++)
        {
            for (int r = 0; r < M; r++)
            {
            	matrix[c][r] *=scaleValues[c];
            }
        }
    }
    
    static boolean equalIdentity(Matrix mat) 
    {
    	Matrix identity = new Matrix(mat.M);
    	mat.equals(identity);
        return mat.equals(identity);
    }  
    
    // XXX: This tests more solid than converting the q to a matrix and multiplying... why?
    public void multLocal(Quaternion rotation) {
        Vector3f axis = new Vector3f();
        float angle = rotation.toAngleAxis(axis);
        Matrix matrix = new Matrix(M);
        matrix.fromAngleAxis(angle, axis);
        multLocal(matrix);
    }
    
    @Override
    public Matrix clone() 
    {
        try 
        {
            return (Matrix) super.clone();
        } 
        catch (CloneNotSupportedException e) 
        {
            throw new AssertionError(); // can not happen
        }
    }
    
    /**
     * A function for creating a rotation matrix that rotates a vector called
     * "start" into another vector called "end".
     * 
     * @param start
     *            normalized non-zero starting vector
     * @param end
     *            normalized non-zero ending vector
     * @see "Tomas M�ller, John Hughes \"Efficiently Building a Matrix to Rotate \
     *      One Vector to Another\" Journal of Graphics Tools, 4(4):1-4, 1999"
     */
    public void fromStartEndVectors(Vector3f start, Vector3f end) 
    {
        Vector3f v = new Vector3f();
        float e, h, f;

        start.cross(end, v);
        e = start.dot(end);
        f = (e < 0) ? -e : e;

        // if "from" and "to" vectors are nearly parallel
        if (f > 1.0f - FastMath.ZERO_TOLERANCE) 
        {
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
                    float val = -c1 * u.get(i) * u.get(j) - c2 * v.get(i)
                            * v.get(j) + c3 * v.get(i) * u.get(j);
                    set(i, j, val);
                }
                float val = get(i, i);
                set(i, i, val + 1.0f);
            }
        } 
        else 
        {
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
    
}
