package com.jme3.math;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;

public class Vector implements Savable, Cloneable, java.io.Serializable {

	static final long serialVersionUID = 1;
	
	private static final Logger logger = Logger.getLogger(Vector4f.class.getName());
	
	protected float[] values;
	
	/**
	 * Create a new Vector with a given dimension.
	 *   Values will be preset to zero.
	 * 
	 * @param size dimension
	 */
	public Vector(int size) {
		this.values = new float[size];
		Arrays.fill(values, 0f);
	}
	
	/**
	 * Create a new Vector with preset values. The dimension of the Vector
	 *   will equal the length of the array with preset values.
	 *   
	 * @param v preset values
	 */
	public Vector(float... v) {
		this.values = new float[v.length];
		for (int x = 0; x < v.length; x++) { this.values[x] = v[x]; }
	}
	
	/**
	 * Create a new Vector identically to the provided Vector
	 *   
	 * @param v Provided Vector
	 */
	public Vector(Vector v) {
		this(v.getValues());
	}
	
	/**
	 * Preset with ZERO
	 */
	public void makeZERO() { Arrays.fill(values, 0f); }
	public final static Vector ZERO(int size) { Vector v = new Vector(size); v.makeZERO(); return v; }
	
	/**
	 * Preset with NAN
	 */
	public void makeNAN() { Arrays.fill(values, Float.NaN); }
	public final static Vector NAN(int size) { Vector v = new Vector(size); v.makeNAN(); return v; }
	
	/**
	 * Preset with ONES
	 */
	public void makeONES() { Arrays.fill(values, 1f); }
	public final static Vector ONES(int size) { Vector v = new Vector(size); v.makeONES(); return v; }
	
	/**
	 * Preset with UNIT at X
	 */
	public void makeUNIT(int x) { Arrays.fill(values, 0); values[x] = 1; }
	public final static Vector UNIT(int size, int u) { Vector v = new Vector(size); v.makeUNIT(u); return v; }

	/**
	 * Preset with POSITIVE_INF
	 */
	public void makePOSITIVE_INF() { Arrays.fill(values, Float.POSITIVE_INFINITY); }
	public final static Vector POSITIVE_INF(int size) { Vector v = new Vector(size); v.makePOSITIVE_INF(); return v; }
	
	/**
	 * Preset with NEGATIVE_INF
	 */
	public void makeNEGATIVE_INF() { Arrays.fill(values, Float.NEGATIVE_INFINITY); }
	public final static Vector NEGATIVE_INF(int size) { Vector v = new Vector(size); v.makeNEGATIVE_INF(); return v; }
	
	/**
	 * Replace the values of the current Vector with the values of the supplied array.
	 * @param newValues the values that should be inserted into this Vector.
	 * @return this Vector after updating
	 */
	public Vector set(float... newValues) {
		for (int x = 0; x < this.values.length; x++) {
			this.values[x] = newValues[x];
		}
		return this;
	}
	
	/**
	 * Replace the values of the current Vector with the values of the supplied Vector.
	 * @param newValues the Vector with values that should be inserted into this Vector.
	 * @return this Vector after updating
	 */
	public Vector set(Vector v) {
		return this.set(v.getValues());
	}
	
	/**
	 * Add the values of the Array to the internal values of the Vector
	 * @param v values to add
	 * @return new vector with the result
	 */
	public Vector add(float[] v) {
		Vector res = new Vector(this.getSize()).set(this);
		float[] newValues = new float[this.getSize()];
		for (int x = 0; x < res.getValues().length; x++) {
			newValues[x] = v[x] + this.values[x];
		}
		return res.set(newValues);		
	}
	
	/**
	 * Add the values of the supplied Vector to this Vector
	 * @param v supplied Vector
	 * @return new vector with result
	 */
	public Vector add(Vector v) {
		if (v == null) { logger.warning("Provided Vector is null, null returned."); return null; }
		return this.add(v.getValues());
	}
	
	/**
	 * Add the values of provided Vector to the current Vectors values and store
	 *   the results in the specified vector.
	 *   
	 * @param v Vector to add to the current Vector
	 * @param res Destination Vector
	 * @return the destination Vector after addition
	 */
	public Vector add(Vector v, Vector res) {
		float[] newValues = new float[this.getSize()];
		for (int x = 0; x < this.values.length; x++) {
			newValues[x] = this.values[x] + v.getValues()[x];
		}
		return res.set(newValues);
	}
	
	/**
	 * Add a Vector to the current Vector and store the result locally.
	 * @param v Vector to add
	 * @return this Vector after addition
	 */
	public Vector addLocal(Vector v) {
		return this.add(v, this);
	}
	
	/**
	 * Add a Vector to the current Vector and store the result locally.
	 * @param v Vector to add
	 * @return this Vector after addition
	 */
	public Vector addLocal(float[] v) {
		float[] newValues = new float[this.getSize()];
		for (int x = 0; x < this.values.length; x++) {
			newValues[x] = v[x] + this.values[x];
		}
		return this.set(newValues);			
	}
	
	/**
	 * Multiplies the current Vector with a scalar and adds the given Vector
	 *   Stores the result in the current Vector.
	 *   
	 * @param scalar Scalar
	 * @param a Vector
	 * @return the current Vector after the changes have been applied
	 */
	public Vector scaleAdd(float scalar, Vector a) {
		return scaleAdd(scalar, this, a);
	}
	
	/**
	 * Multiplies the given Vector with a scalar and adds the given Vector
	 *   Stores the result in the current Vector.
	 * 
	 * @param scalar Scalar
	 * @param v Vector to operate on
	 * @param a Vector
	 * @return the current Vector after the changes have been applied
	 */
	public Vector scaleAdd(float scalar, Vector v, Vector a) {
		return this.set(v.mult(scalar).add(a));
	}
	
	/**
     * <code>cross</code> calculates the cross product of this vector with a
     * parameter vector v.
     *
     * @param v
     *            the vector to take the cross product of with this.
     * @return the cross product vector.
     */
	public Vector cross(Vector v) {
		return this.cross(v, null);
	}
	
	/**
     * <code>cross</code> calculates the cross product of this vector with a
     * parameter vector v.  The result is stored in <code>result</code>
     *
     * @param v
     *            the vector to take the cross product of with this.
     * @param result
     *            the vector to store the cross product result.
     * @return result, after recieving the cross product vector.
     */
	public Vector cross(Vector v,Vector result) {
		assert(v.values.length >= 3 && result.values.length >= 3);
        return this.cross(v.getAtIndex(0), v.getAtIndex(1), v.getAtIndex(2), result);
    }
	
	/**
     * <code>cross</code> calculates the cross product of this vector with a
     * parameter vector v.  The result is stored in <code>result</code>
     *
     * @param otherX
     *            x component of the vector to take the cross product of with this.
     * @param otherY
     *            y component of the vector to take the cross product of with this.
     * @param otherZ
     *            z component of the vector to take the cross product of with this.
     * @param result
     *            the vector to store the cross product result.
     * @return result, after recieving the cross product vector.
     */
	public Vector cross(float otherX, float otherY, float otherZ, Vector res) {
		assert(this.values.length >= 3 && res.values.length >= 3);
		if (res == null) res = new Vector(this.values.length);
		res.setAtIndex(0, ((res.getAtIndex(1) * otherZ) - (this.getAtIndex(2) * otherY)));
		res.setAtIndex(1, ((res.getAtIndex(2) * otherX) - (this.getAtIndex(0) * otherZ)));
		res.setAtIndex(2, ((res.getAtIndex(0) * otherY) - (this.getAtIndex(1) * otherX)));
		return res;
	}
	
	/**
     * <code>crossLocal</code> calculates the cross product of this vector
     * with a parameter vector v.
     *
     * @param v
     *            the vector to take the cross product of with this.
     * @return this.
     */
    public Vector crossLocal(Vector v) {
        return this.crossLocal(v.getAtIndex(0), v.getAtIndex(1), v.getAtIndex(2));
    }
	
	/**
     * <code>crossLocal</code> calculates the cross product of this vector
     * with a parameter vector v.
     *
     * @param otherX
     *            x component of the vector to take the cross product of with this.
     * @param otherY
     *            y component of the vector to take the cross product of with this.
     * @param otherZ
     *            z component of the vector to take the cross product of with this.
     * @return this.
     */
	public Vector crossLocal(float otherX, float otherY, float otherZ) {
		assert(this.values.length >= 3);
		float tempx = (this.getAtIndex(1) * otherZ) - (this.getAtIndex(2) * otherY);
		float tempy = (this.getAtIndex(2) * otherX) - (this.getAtIndex(0) * otherZ);
		this.values[2] = (this.getAtIndex(1) * otherY) - (this.values[1] * otherX);
		this.values[1] = tempx;
		this.values[0] = tempy;
		return this;
	}
	
	/**
	 * Calculates the dot product with the given and current Vector.
	 * @param v Provided Vector
	 * @return value of the dot product.
	 */
	public float dot(Vector v) {
		float res = 0f;
		for (int x = 0; x < this.values.length; x ++) {
			res += this.values[x] * v.getValues()[x];
		}
		return res;
	}
	
	/**
	 * Return a new Vector containing the projection of this Vector onto another Vector
	 * @param other Vector to project upon
	 * @return Vector with projection
	 */
	public Vector project(Vector other) {
		float n = this.dot(other);
		float d = other.lengthSquared();
//		System.out.println("This Dot: " + n + "  -- Other Length squared: " + d + " n/d: " + n/d);
//		System.out.println(other.normalize());
//		
//		System.out.println();
//		
		return new Vector(other).normalizeLocal().multLocal(n/d);
	}
	
	/**
     * Projects this vector onto another vector, stores the result in this
     * vector
     *
     * @param other The vector to project this vector onto
     * @return This Vector3f, set to the projection result
     */
	public Vector projectLocal(Vector other) {
		return this.set(this.project(other));
	}
	
	/**
	 * Indicates whether this Vector is a Unit Vector (length == 1) or not.
	 * @return indication of Unit Vector-ness
	 */
	public boolean isUnitVector() {
		float len = length();
		return 0.99f < len && len < 1.01f;
	}
	
	/**
	 * Returns the length of the current Vector
	 * @return length of current Vector
	 */
	public float length() {
		return FastMath.sqrt(lengthSquared());
	}
	
	/**
	 * Squares all elements and adds them up.
	 * @return sum of all squared elements.
	 */
	public float lengthSquared() {
		float res = 0f;
		for (int x = 0; x < this.values.length; x++) {
			res += this.values[x] * this.values[x];
		}
		return res;
	}
	
	/**
	 * Computes the difference between elements in two Vectors, squares the results and sums these.
	 * @param v Other vector
	 * @return squared distance
	 */
	public float distanceSquared(Vector v) {
		float res = 0f;
		for (int x = 0; x < this.values.length; x++) {
			res += (this.values[x] - v.getValues()[x]) * (this.values[x] - v.getValues()[x]); 
		}
		return res;
	}
	
	/**
	 * Computes the distance between this and supplied Vector
	 * @param v supplied Vector
	 * @return distance between vectors
	 */
	public float distance(Vector v) {
		return FastMath.sqrt(distanceSquared(v));
	}
	
	/**
	 * Multiply the current Vector with a scalar and return the result as a new Vector
	 * @param scalar
	 * @return new Vector
	 */
	public Vector mult(float scalar) {
		float[] res = new float[this.getSize()];
		for (int x = 0; x < this.values.length; x++) {
			res[x] = this.values[x] * scalar;
		}
		return new Vector(res);
	}
	
	/**
	 * Multiply the current Vector with a scalar and store the result in the specified Vector
	 * @param scalar
	 * @param res Vector to store the result in
	 * @return the destination Vector after storing the results
	 */
	public Vector mult(float scalar, Vector res) {
		return res.set(this.mult(scalar));
	}
	
	/**
	 * Multiply the current Vector by a scalar and store the result within the current Vector
	 * @param scalar
	 * @return current Vector after multiplication
	 */
	public Vector multLocal(float scalar) {
		return this.mult(scalar, this);
	}
	
	/**
	 * Multiply the current Vector with another Vector and store the result within the current Vector
	 * @param v
	 * @return the current Vector after multiplication
	 */
	public Vector multLocal(Vector v) {
		return this.set(this.mult(v));
	}
	
	/**
	 * Multiply the current Vector by a scalar and store the result in the specified Vector.
	 * @param v
	 * @param dest Vector to store result in
	 * @return destination Vector after storing results
	 */
	public Vector mult(float[] v, Vector dest) {
		if (dest == null) dest = new Vector(this.values.length);
		float[] res = new float[this.values.length];
		for (int x = 0; x < this.values.length; x++) {
			res[x] = this.values[x] * v[x];
		}
		return dest.set(res);
	}
	
	/**
	 * Multiply the current Vector by another Vector and store the result in a new Vector.
	 * @param v Vector to multiply with
	 * @return new Vector containing the results
	 */
	public Vector mult(Vector v) {
		return mult(v.getValues(), null);
	}
	
	/**
	 * Multiply the current vector with another Vector and store the result in the given Vector.
	 * @param v Vector to multiply with
	 * @param dest Vector for result
	 * @return destination Vector after storing the result
	 */
	public Vector mult(Vector v, Vector dest) {
		return mult(v.getValues(), dest);
	}
	
	/**
	 * Multiply the current Vector by the given values and store it in the current Vector
	 * @param f values to multiply with
	 * @return current Vector after multiplication
	 */
	public Vector multLocal(float[] f) {
		return this.mult(f, this);
	}
	
	/**
	 * Divide the current vector by a scalar and return the result in a new Vector
	 * @param scalar
	 * @return Vector containing result
	 */
	public Vector divide(float scalar) {
		scalar = 1f / scalar;
		return this.mult(scalar);
	}
	
	/**
	 * Divide the current vector by a scalar and store the result in the current Vector
	 * @param scalar
	 * @return the current Vector after division.
	 */
	public Vector divideLocal(float scalar) {
		scalar = 1f / scalar;
		return this.multLocal(scalar);
	}
	
	/**
	 * Divide the current vector by another Vector and return a new Vector with the result.
	 * @param v Vector to divide by.
	 * @return new Vector with the result of the division
	 */
	public Vector divide(Vector v) {
		float[] res = new float[this.values.length];
		for (int x = 0; x < this.values.length; x++) {
			res[x] = this.values[x] / v.getValues()[x];
		}
		return new Vector(res);
	}
	
	/**
	 * Divide the current Vector by another Vector and store the result in the current Vector
	 * @param v Vector the divide by.
	 * @return the current Vector after division.
	 */
	public Vector divideLocal(Vector v) {
		return this.set(this.divide(v));
	}
	
	/**
	 * Return a new Vector containing the negated values of the current Vector
	 * @return new negated Vector
	 */
	public Vector negate() {
		return this.mult(-1f);
	}
	
	/**
	 * Negate the current Vector and store the results in the current Vector.
	 * @return the current Vector after negating.
	 */
	public Vector negateLocal() {
		return this.multLocal(-1f);
	}
	
	/**
	 * Subtract the given Vector from the current Vector and return the result in a new Vector.
	 * @param v Vector to subtract from current Vector
	 * @return result in a new Vector
	 */
	public Vector subtract(Vector v) {
		return this.subtract(v.getValues());
	}
	
	/**
	 * Subtract the specified values from the current Vector. Return the results in a new Vector.
	 * @param v values to subtract
	 * @return new Vector containing computed results
	 */
	public Vector subtract(float[] v) {
		float[] res = new float[this.values.length];
		for (int x = 0; x < this.values.length; x++) {
			res[x] = this.values[x] - v[x];
		}
		return new Vector(res);
	}
	
	/**
	 * Subtract the given values from the current Vector and store the result in the current Vector
	 * @param v values to subtract
	 * @return the current Vector after subtraction.
	 */
	public Vector subtractLocal(float[] v) {
		return this.set(this.subtract(v));
	}
	
	/**
	 * Subtract the given Vector from the current Vector and store the result in the current Vector
	 * @param v Vector to subtract
	 * @return the current Vector after subtraction.
	 */
	public Vector subtractLocal(Vector v) {
		return this.set(this.subtract(v));
	}
	
	/**
	 * Subtract the given Vector from the current Vector and store the result in the indicated Vector
	 * @param v Vector to subtract
	 * @return the indicated Vector after subtraction.
	 */
	public Vector subtract(Vector v, Vector dest) {
		return dest.set(this.subtract(v));
	}
	
	/**
	 * Normalize the current Vector and return the normalized result in a new Vector.
	 * @return the new (normalized) Vector
	 */
	public Vector normalize() {
		float length = lengthSquared();
		
		if (length != 1f && length != 0f) {
			length = 1.0f / this.length();
			return mult(length);
		}
		
		return clone();
	}
	
	/**
	 * Normalize the current Vector and stire the normalized result ni the current Vector.
	 * @return the normalized Vector
	 */
	public Vector normalizeLocal() {
		return this.set(this.normalize());
	}
	
	/**
	 * Takes the values from the current and supplied Vectors and creates a new Vector
	 *   containing the max value for each entry in both Vectors.
	 * @param v Vector to max with
	 * @return new Vector containing the max values of each related entry in both Vectors
	 */
	public Vector max(Vector v) {
		float[] res = new float[this.values.length];
		for (int x = 0; x < this.values.length; x++) {
			res[x] = (v.getValues()[x] > this.values[x] ? v.getValues()[x] : this.values[x]);
		}
		return new Vector(res);
	}
	
	/**
	 * Takes the values from the current and supplied Vectors and stores the result
	 *   containing the max value for each entry in the current Vector.
	 * @param v Vector to max with
	 * @return current Vector containing the max values of each related entry in both Vectors
	 */
	public Vector maxLocal(Vector v) {
		return this.set(this.max(v));
	}
	
	/**
	 * Takes the values from the current and supplied Vectors and creates a new Vector
	 *   containing the min value for each entry in both Vectors.
	 * @param v Vector to min with
	 * @return new Vector containing the min values of each related entry in both Vectors
	 */
	public Vector min(Vector v) {
		float[] res = new float[this.values.length];
		for (int x = 0; x < this.values.length; x++) {
			res[x] = (v.getValues()[x] < this.values[x] ? v.getValues()[x] : this.values[x]);
		}
		return new Vector(res);
	}
	
	/**
	 * Takes the values from the current and supplied Vectors and stores the result
	 *   containing the min value for each entry in the current Vector.
	 * @param v Vector to min with
	 * @return current Vector containing the min values of each related entry in both Vectors
	 */
	public Vector minLocal(Vector v) {
		return this.set(this.min(v));
	}	
	
	/**
     * <code>angleBetween</code> returns (in radians) the angle required to
     * rotate a ray represented by this vector to lie colinear to a ray
     * described by the given vector. It is assumed that both this vector and
     * the given vector are unit vectors (iow, normalized).
     * 
     * @param otherVector
     *            the "destination" unit vector
     * @return the angle in radians.
     */
	public float angleBetween(Vector v) {
		if (v.values.length == 2 && this.values.length == 2) {
			return FastMath.atan2(v.getAtIndex(1), v.getAtIndex(0)) - FastMath.atan2(this.getAtIndex(1), this.getAtIndex(0));
		} else {
			return this.smallestAngleBetween(v);
		}
	}
	
	/**
	 * Returns the angle (in Radians) between two Vectors.
	 * @param v other Vector
	 * @return angle in Radians
	 */
	public float smallestAngleBetween(Vector v) {
		float dotProduct = this.dot(v);
		float angle = FastMath.acos(dotProduct);
		return angle;
	}
	
	/**
	 * <code>getAngle</code> returns (in radians) the angle represented by
     * this Vector2f as expressed by a conversion from rectangular coordinates (<code>x</code>,&nbsp;<code>y</code>)
     * to polar coordinates (r,&nbsp;<i>theta</i>).
     * 
	 * TODO: Find a better solution then asserting
	 * @return the angle in radians. [-pi, pi)
	 */
	public float getAngle() {
		assert(this.values.length >= 2);
		return FastMath.atan2(this.getAtIndex(1), this.getAtIndex(0));
	}
	
	/**
     * Sets this vector to the interpolation by changeAmnt from this to the finalVec
     * this=(1-changeAmnt)*this + changeAmnt * finalVec
     * @param finalVec The final vector to interpolate towards
     * @param changeAmnt An amount between 0.0 - 1.0 representing a precentage
     *  change from this towards finalVec
     */
	public Vector interpolateLocal(Vector finalVec, float changeAmnt) {
		return interpolateLocal(this, finalVec, changeAmnt);
	}
	
	/**
     * Sets this vector to the interpolation by changeAmnt from beginVec to finalVec
     * this=(1-changeAmnt)*beginVec + changeAmnt * finalVec
     * @param beginVec the beging vector (changeAmnt=0)
     * @param finalVec The final vector to interpolate towards
     * @param changeAmnt An amount between 0.0 - 1.0 representing a precentage
     *  change from beginVec towards finalVec
     */
	public Vector interpolateLocal(Vector beginVec, Vector finalVec, float changeAmnt) {
		for (int x = 0; x < this.values.length; x++) {
			this.values[x] = (1 - changeAmnt) * beginVec.getValues()[x] + changeAmnt * finalVec.getValues()[x];
		}
		return this;
	}
	
	/**
	 * Returns a boolean indicating whether the current Vector is valid or not.
	 * @return validness of Vector
	 */
	public boolean isValidVector() {
		return Vector.isValidVector(this);
	}
	
	/**
	 * Returns a boolean indicating whether the provided Vector is valid or not.
	 * @param v Vector to check
	 * @return validness of Vector
	 */
	public static boolean isValidVector(Vector v) {
		if (v == null) return false;
		for (int x = 0; x < v.getValues().length; x++) {
			if (Float.isNaN(v.getValues()[x]) || Float.isInfinite(v.getValues()[x])) 
				return false;
		}
		return true;
	}
	
	@Override
	public Vector clone() {
		try {
			return (Vector) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
	
	/**
	 * Puts all values of the current Vector in the desired array
	 * @param floats array to put values into.
	 * @return the array after filling
	 */
	public float[] toArray(float[] floats) {
		if (floats == null) // Also when invalid length?
			floats = new float[this.values.length];
		
		for (int x = 0; x < this.values.length; x++) {
			floats[x] = this.values[x];
		}
		
		return floats;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Vector)) { return false; }

        if (this == o) { return true; }
        
        Vector comp = (Vector) o;
        for (int x = 0; x < this.values.length; x++) {
        	if (Float.compare(this.values[x], comp.getValues()[x]) != 0) return false;
        }
        return true;
	}
	
	public int hashCode() {
		int hash = 37;
		for (int x = 0; x < this.values.length; x++) { 
			hash += 37 * hash + Float.floatToIntBits(this.values[x]); 
		}
		return hash;
	}
	
	public String toString() {
		String res = "(";
		for (int x = 0; x < this.values.length; x++) { 
			res += this.values[x] + (x < this.values.length - 1 ? ", " : ""); 
		}
		return res + ")";
	}
	
	public float[] getValues() {
		return this.values;
	}
	
	public int getSize() {
		return this.values.length;
	}
	
	@Override
	public void write(JmeExporter e) throws IOException {
		OutputCapsule capsule = e.getCapsule(this);
		for (int x = 0; x < this.values.length; x++) {
			capsule.write(this.values[x], "" + x + "", 0);
		}
	}

	@Override
	public void read(JmeImporter e) throws IOException {
		InputCapsule capsule = e.getCapsule(this);
		for (int x = 0; x < this.values.length; x++) {
			this.values[x] = capsule.readFloat("" + x + "", 0);
		}
	}

	/**
	 * Get a value at a specific index
	 * @param index
	 * @return value at specific index
	 */
	public float getAtIndex(int index) {
		if (index < this.values.length) {
			return this.values[index];
		}
		throw new IllegalArgumentException("index must be between 0 and " + (this.values.length - 1));
	}
	
	/**
	 * Set a value at a specific index
	 * @param index
	 * @param f new value
	 */
	public void setAtIndex(int index, float f) {
		if (index < this.values.length) {
			this.values[index] = f;
			return;
		}
		throw new IllegalArgumentException("index must be between 0 and " + (this.values.length - 1));
	}
	
	/**
	 * Rotate this Vector around the origin.
	 * Requires the Vector to have a length of 2! (X and Y)
	 * 
	 * @param angle
	 * @param cw
	 */
    public void rotateAroundOrigin(float angle, boolean cw) {
        if (cw)
            angle = -angle;
        float newX = FastMath.cos(angle) * this.getAtIndex(0) - FastMath.sin(angle) * this.getAtIndex(1);
        float newY = FastMath.sin(angle) * this.getAtIndex(0) + FastMath.cos(angle) * this.getAtIndex(1);
        this.setAtIndex(0, newX);
        this.setAtIndex(1, newY);
    }
    
    public static void generateOrthonormalBasis(Vector u, Vector v, Vector w) {
        w.normalizeLocal();
        generateComplementBasis(u, v, w);
    }

    public static void generateComplementBasis(Vector u, Vector v, Vector w) {
    	assert(u.values.length >= 3);
    	assert(v.values.length >= 3);
    	assert(w.values.length >= 3);
        float fInvLength;

        if (FastMath.abs(w.getAtIndex(0)) >= FastMath.abs(w.getAtIndex(1))) {
            // w.x or w.z is the largest magnitude component, swap them
            fInvLength = FastMath.invSqrt(w.getAtIndex(0) * w.getAtIndex(0) + w.getAtIndex(2) * w.getAtIndex(2));
            u.values[0] = -w.getAtIndex(2) * fInvLength;
            u.values[1] = 0.0f;
            u.values[2] = +w.getAtIndex(0) * fInvLength;
            v.values[0] = w.getAtIndex(1) * u.getAtIndex(2);
            v.values[1] = w.getAtIndex(2) * u.getAtIndex(0) - w.getAtIndex(0) * u.getAtIndex(2);
            v.values[2] = -w.getAtIndex(1) * u.getAtIndex(0);
        } else {
            // w.y or w.z is the largest magnitude component, swap them
            fInvLength = FastMath.invSqrt(w.getAtIndex(1) * w.getAtIndex(1) + w.getAtIndex(2) * w.getAtIndex(2));
            u.values[0] = 0.0f;
            u.values[1] = +w.getAtIndex(2) * fInvLength;
            u.values[2] = -w.getAtIndex(1) * fInvLength;
            v.values[0] = w.getAtIndex(1) * u.getAtIndex(2) - w.getAtIndex(2) * u.getAtIndex(1);
            v.values[1] = -w.getAtIndex(0) * u.getAtIndex(2);
            v.values[2] = w.getAtIndex(0) * u.getAtIndex(1);
        }
    }
    
    // Legacy stuff
    public float getX() { return this.values[0]; }
    public float getY() { return this.values[1]; }
    public float getZ() { return this.values[2]; }
    public float getW() { return this.values[3]; }
    public void setX(float f) { this.values[0] = f; }
    public void setY(float f) { this.values[1] = f; }
    public void setZ(float f) { this.values[2] = f; }
    public void setW(float f) { this.values[3] = f; }
    
    /**
     * Fallback.
     * Whenever we need a Vector in a Vector3f form
     * @return VectorXf
     */
    public Vector4f toVector4f() {
    	return new Vector4f(this.values[0], this.values[1], this.values[2], this.values[3]);
    }
    public Vector3f toVector3f() {
    	return new Vector3f(this.values[0], this.values[1], this.values[2]);
    }
    public Vector2f toVector2f() {
    	return new Vector2f(this.values[0], this.values[1]);
    }
    public static Vector toVector(Vector2f v) {
    	return new Vector(v.x, v.y);
    }
	public static Vector toVector(Vector3f v) {
	    return new Vector(v.x, v.y, v.z);
    }
	public static Vector toVector(Vector4f v) {
		return new Vector(v.x, v.y, v.z, v.w);
	}
}
