/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.math;

import com.jme3.export.*;
import com.jme3.util.BufferUtils;
import com.jme3.util.TempVars;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.logging.Logger;

/**
 * <code>Matrix4f</code> defines and maintains a 4x4 matrix in row major order.
 * This matrix is intended for use in a translation and rotational capacity. 
 * It provides convenience methods for creating the matrix from a multitude 
 * of sources.
 * 
 * Matrices are stored assuming column vectors on the right, with the translation
 * in the rightmost column. Element numbering is row,column, so m03 is the zeroth
 * row, third column, which is the "x" translation part. This means that the implicit
 * storage order is column major. However, the get() and set() functions on float
 * arrays default to row major order!
 *
 * @author Mark Powell
 * @author Joshua Slack
 */
public abstract class Matrixf implements Savable, Cloneable, java.io.Serializable {

    static final long serialVersionUID = 1;

    protected static final Logger logger = Logger.getLogger(Matrix4f.class.getName());
    protected float[][] matrix;
    protected int matrixSize;
    
    
    public static final Matrix4f ZERO = new Matrix4f(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    public static final Matrix4f IDENTITY = new Matrix4f();

    /**
     * Constructor instantiates a new <code>Matrix</code> that is set to the
     * identity matrix.
     *  
     */
    public Matrixf() {
        loadIdentity();
    }
    
    public abstract void  loadIdentity();

    /**
     * constructs a matrix with the given values.
     */
    public Matrixf(float[] values, int n){
    	int c=0;
    	int line=0;
    	int i;
    	float[][] result = new float[n][n];
    	while(c<values.length){
    		for(i=0;i<n;i++){
    			result[line][i]=values[i];
    		}
    		line++;
    	}
    	matrix=result;
    	matrixSize=n;
    }
    
    public float[][] getMatrix(){
    	return matrix;
    }

    /**
     * <code>copy</code> transfers the contents of a given matrix to this
     * matrix. If a null matrix is supplied, this matrix is set to the identity
     * matrix.
     * 
     * @param matrix
     *            the matrix to copy.
     */
    public void copy(Matrixf matrix) {
        if (null == matrix) {
            loadIdentity();
        } else {
        	int i,j;
            for(i=0;i<matrixSize;i++){
            	for(j=0;j<matrixSize;j++){
            		this.matrix[i][j]=matrix.getMatrix()[i][j];
            	}
            }
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
        if (matrix.length != matrixSize*matrixSize) {
            throw new IllegalArgumentException(
                    "Array must be of size "+ matrixSize*matrixSize);
        }

        if (rowMajor) {
        	int i,j;
        	int current=0;
        	for(i=0;i<matrixSize;i++){
        		for(j=0;j<matrixSize;j++){
        			matrix[current] = this.matrix[i][j];
        		}
        	}
        	
        } else {
        	
        	int i,j;
        	int current=0;
        	for(i=0;i<matrixSize;i++){
        		for(j=0;j<matrixSize;j++){
        			matrix[current] = this.matrix[j][i];
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
    public float get(int i, int j) {
    	
    	if(i<matrixSize && j<matrixSize){
    		return matrix[i][j];
    	}else{
	        logger.warning("Invalid matrix index.");
	        throw new IllegalArgumentException("Invalid indices into matrix.");
    	}
    }

    

    /**
     * <code>getColumn</code> returns one of three columns specified by the
     * parameter. This column is returned as a float[4].
     * 
     * @param i
     *            the column to retrieve. Must be between 0 and 3.
     * @param store
     *            the float array to store the result in. if null, a new one
     *            is created.
     * @return the column specified by the index.
     */
    public float[] getColumn(int i, float[] store) {
        if (store == null) {
            store = new float[matrixSize];
        }
        
        if(i<matrixSize){
        	store = matrix[i];
        }else{
            logger.warning("Invalid column index.");
            throw new IllegalArgumentException("Invalid column index. " + i);
        }
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

        if (column == null) {
            logger.warning("Column is null. Ignoring.");
            return;
        }
        
        if(i<matrixSize){
        	matrix[i] = column;
        }else{
          logger.warning("Invalid column index.");
          throw new IllegalArgumentException("Invalid column index. " + i);
        }
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
    public void set(int i, int j, float value) {
       
    	if(i<matrixSize && j<matrixSize){
    		matrix[i][j] = value;
    	}else{
    		 logger.warning("Invalid matrix index.");
    	     throw new IllegalArgumentException("Invalid indices into matrix.");
    	}	
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
    public void set(float[][] matrix) {
        if (matrix.length != matrixSize || matrix[0].length != matrixSize) {
        	throw new IllegalArgumentException(
                    "Array must be of size "+ matrixSize*matrixSize);
        }
        this.matrix = matrix;
    }
    
    
    

    /**
     * <code>set</code> sets the values of this matrix from another matrix.
     *
     * @param matrix
     *            the matrix to read the value from.
     */
    public Matrixf set(Matrixf matrix) {
        this.matrix = matrix.getMatrix();
        return this;
    }

    /**
     * <code>set</code> sets the values of this matrix from an array of
     * values assuming that the data is rowMajor order;
     * 
     * @param matrix
     *            the matrix to set the value to.
     */
    public void set(float[] matrix) {
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
        if (matrix.length != matrixSize*matrixSize) {
            throw new IllegalArgumentException(
                    "Array must be of size "+matrixSize*matrixSize);
        }

        if (rowMajor) {
           set(matrix);
        } else {
        	int i,j;
        	for(i=0;i<matrixSize;i++){
        		for(j=0;j<matrixSize;j++){
        			this.matrix[i][j] = i+j*matrixSize;
        		}
        	}
           
        }
    }


    /**
     * <code>transpose</code> locally transposes this Matrix.
     * 
     * @return this object for chaining.
     */
    public Matrixf transposeLocal() {
        
    	int i,j;
    	float tmp;
    	for(i=0;i<matrixSize;i++){
    		for(j=i;j<matrixSize;j++){
    			tmp = matrix[i][j];
    			matrix[i][j]=matrix[j][i];
    			matrix[j][i]=tmp;
    		}
    	}
    	
        return this;
    }

    /**
     * <code>toFloatBuffer</code> returns a FloatBuffer object that contains
     * the matrix data.
     * 
     * @return matrix data as a FloatBuffer.
     */
    public FloatBuffer toFloatBuffer() {
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
    public FloatBuffer toFloatBuffer(boolean columnMajor) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(matrixSize*matrixSize);
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
    public FloatBuffer fillFloatBuffer(FloatBuffer fb) {
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
        fb.put(vars.matrixWrite, 0, matrixSize*matrixSize);

        vars.release();

        return fb;
    }

    public void fillFloatArray(float[] f, boolean columnMajor) {
        if (columnMajor) {
        	int i,j;
        	int counter=0;
        	for(i=0;i<matrixSize;i++){
        		for(j=0;j<matrixSize;j++){
        			f[counter]=matrix[j][i];
        			counter++;
        		}
        	}
        }else{
        	int i,j;
        	int counter=0;
        	for(i=0;i<matrixSize;i++){
        		for(j=0;j<matrixSize;j++){
        			f[counter]=matrix[i][j];
        			counter++;
        		}
        	}
        }
        
    }

    /**
     * <code>readFloatBuffer</code> reads value for this matrix from a FloatBuffer.
     * @param fb the buffer to read from, must be correct size
     * @return this data as a FloatBuffer.
     */
    public Matrixf readFloatBuffer(FloatBuffer fb) {
        return readFloatBuffer(fb, false);
    }

    /**
     * <code>readFloatBuffer</code> reads value for this matrix from a FloatBuffer.
     * @param fb the buffer to read from, must be correct size
     * @param columnMajor if true, this buffer should be filled with column
     * 		major data, otherwise it will be filled row major.
     * @return this data as a FloatBuffer.
     */
    public Matrixf readFloatBuffer(FloatBuffer fb, boolean columnMajor) {

    	int i,j;
    	
    	
        if (columnMajor) {
        	for(i=0;i<matrixSize;i++){
        		for(j=0;j<matrixSize;j++){
        			matrix[i][j]=fb.get();
        		}
        	}
        } else {
        	for(i=0;i<matrixSize;i++){
        		for(j=0;j<matrixSize;j++){
        			matrix[j][i]=fb.get();
        		}
        	}
        }
        return this;
    }

  

   

    /**
     * <code>mult</code> multiplies this matrix by a scalar.
     * 
     * @param scalar
     *            the scalar to multiply this matrix by.
     */
    public void multLocal(float scalar) {
    	int i,j;
    	for(i=0;i<matrixSize;i++){
    		for(j=0;j<matrixSize;j++){
    			matrix[i][j]*=scalar;
    		}
    	}
      
    }

    public Matrixf mult(float scalar, Matrixf store) {
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
     * @param store
     *            where to store the result. It is safe for in2 and store to be
     *            the same object.
     * @return the resultant matrix
     */
    public Matrixf mult(Matrixf in2, Matrixf store) {
        
        int i,j,k;
        for(i=0;i<store.matrixSize;i++){
    		for(j=0;j<store.matrixSize;j++){
    			for(k=0;k<store.matrixSize;k++){
    				store.matrix[i][j]+= this.matrix[i][k]*in2.matrix[k][i];
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
    public Matrixf multLocal(Matrixf in2) {
        return mult(in2, this);
    }

   

   
   
  

    /**
     * Inverts this matrix as a new Matrix4f.
     * 
     * @return The new inverse matrix
     */
    public Matrixf invert() {
        return invert(null);
    }

    public abstract Matrixf invert(Matrixf m);
  
    

    /**
     * Returns a new matrix representing the adjoint of this matrix.
     * 
     * @return The adjoint matrix
     */
    public Matrixf adjoint() {
        return adjoint(null);
    }


    /**
     * Places the adjoint of this matrix in store (creates store if null.)
     * 
     * @param store
     *            The matrix to store the result in.  If null, a new matrix is created.
     * @return store
     */
    public abstract Matrixf adjoint(Matrixf store);

   
    /**
     * Sets all of the values in this matrix to zero.
     * 
     * @return this matrix
     */
    public Matrixf zero() {
    	int i,j;
    	for(i=0;i<matrixSize;i++){
    		for(j=0;j<matrixSize;j++){
    			matrix[i][j]=0.0f;
    		}
    	}
        return this;
    }

    public Matrixf add(Matrixf mat, Matrixf result) {
        int i,j;
        for(i=0;i<matrixSize;i++){
        	for(j=0;j<matrixSize;j++){
        		result.matrix[i][j]=this.matrix[i][j]+mat.matrix[i][j];
        	}
        }
        
        return result;
    }

    /**
     * <code>add</code> adds the values of a parameter matrix to this matrix.
     * 
     * @param mat
     *            the matrix to add to this.
     */
    public void addLocal(Matrixf mat) {
    	 int i,j;
         for(i=0;i<matrixSize;i++){
         	for(j=0;j<matrixSize;j++){
         		this.matrix[i][j]+=mat.matrix[i][j];
         	}
         }
       
    }


  

   
    public void toRotationMatrix(Matrixf mat) {
    	int i,j;
        for(i=0;i<matrixSize;i++){
        	for(j=0;j<matrixSize;j++){
        		mat.matrix[i][j]+=this.matrix[i][j];
        	}
        }
	}

	
    /**
     * are these two matrices the same? they are is they both have the same mXX values.
     *
     * @param o
     *            the object to compare for equality
     * @return true if they are equal
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Matrixf) || o == null) {
            return false;
        }

        if (this == o) {
            return true;
        }

        Matrixf comp = (Matrixf) o;
        
        int i,j;
        for(i=0;i<matrixSize;i++){
        	for(j=0;j<matrixSize;j++){
        		
        		if (Float.compare(matrix[i][j], comp.matrix[i][j]) != 0) {
                    return false;
                }
        	}
        }
        return true;
    }

   

    @Override
    public Matrixf clone() {
        try {
            return (Matrixf) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // can not happen
        }
    }
}
