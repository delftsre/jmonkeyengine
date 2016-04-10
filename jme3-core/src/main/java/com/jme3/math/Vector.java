package com.jme3.math;

public class Vector implements Vectorable {

    private float[] vector;

    public Vector(float[] f) {
        vector = f;
    }

    public Vectorable add(Vectorable v) {
        checkEqualVectorLength(v);
        float[] result = new float[vector.length];
        for (int i = 0; i < vector.length; i++)
            result[i] = get(i) + v.get(i);
        return new Vector(result);
    }

    public Vectorable addLocal(Vectorable v) {
        checkEqualVectorLength(v);
        for (int i = 0; i < vector.length; i++)
            vector[i] = get(i) + v.get(i);
        return this;
    }

    public float angleBetween(Vectorable v) {
        checkEqualVectorLength(v);
        float dotProduct = dot(v);
        float angle = FastMath.acos(dotProduct);
        return angle;
    }

    private void checkEqualVectorLength(Vectorable v) {
        if (!isValidVector(v))
            throw new IllegalArgumentException("Vector length is not equal.");
    }

    public Vectorable cross(Vectorable v) {
        // TODO: To be implemented.
        return null;
    }

    public float determinant(Vectorable v) {
        // TODO: To be implemented.
        return 0;
    }

    public float distance(Vectorable v) {
        checkEqualVectorLength(v);
        return FastMath.sqrt(distanceSquared(v));
    }

    public float distanceSquared(Vectorable v) {
        checkEqualVectorLength(v);
        float result = 0;
        for (int i = 0; i < vector.length; i++)
            result += (get(i) - v.get(i)) * (get(i) - v.get(i));
        return result;
    }

    public Vectorable divide(float d) {
        float[] result = new float[vector.length];
        for (int i = 0; i < vector.length; i++)
            result[i] = get(i) / d;
        return new Vector(result);
    }

    public Vectorable divideLocal(float d) {
        for (int i = 0; i < vector.length; i++)
            vector[i] = get(i) / d;
        return this;
    }

    public float dot(Vectorable v) {
        checkEqualVectorLength(v);
        if (v == null) return 0;
        float result = 0;
        for (int i = 0; i < vector.length; i++)
            result += get(i) * v.get(i);
        return result;
    }

    public float get(int i) {
        if (i < 0 || i >= vector.length)
            throw new IllegalArgumentException();
        return vector[i];
    }

    public float[] getVector() {
        return vector;
    }

    public Vectorable interpolateLocal(Vectorable v, float changeAmount) {
        checkEqualVectorLength(v);
        for (int i = 0; i < vector.length; i++)
            vector[i] = (1 - changeAmount) * get(i) + changeAmount * v.get(i);
        return this;
    }

    public boolean isValidVector(Vectorable v) {
        if (v == null) return false;
        if (vector.length != v.getVector().length) return false;
        for (float f : vector)
            if (Float.isNaN(f) || Float.isInfinite(f))
                return false;
        return true;
    }

    public float length() {
        return FastMath.sqrt(lengthSquared());
    }

    public float lengthSquared() {
        float result = 0;
        for (float f : vector)
            result += f * f;
        return result;
    }

    public Vectorable mult(float m) {
        float[] result = new float[vector.length];
        for (int i = 0; i < vector.length; i++)
            result[i] = m * get(i);
        return new Vector(result);
    }

    public Vectorable multLocal(float m) {
        for (int i = 0; i < vector.length; i++)
            vector[i] = m * get(i);
        return this;
    }

    public Vectorable negate() {
        float[] result = new float[vector.length];
        for (int i = 0; i < vector.length; i++)
            result[i] = -get(i);
        return new Vector(result);
    }

    public Vectorable negateLocal() {
        for (int i = 0; i < vector.length; i++)
            vector[i] = -get(i);
        return this;
    }

    public Vectorable normalize() {
        float length = length();
        if (length != 0) {
            return divide(length);
        }

        return divide(1);
    }

    public Vectorable normalizeLocal() {
        float length = length();
        if (length != 0) {
            return divideLocal(length);
        }

        return divideLocal(1);
    }

    public Vectorable set(Vectorable v) {
        checkEqualVectorLength(v);
        for (int i = 0; i < vector.length; i++)
            vector[i] = v.get(i);
        return this;
    }

    public Vectorable substract(Vectorable v) {
        checkEqualVectorLength(v);
        float[] result = new float[vector.length];
        for (int i = 0; i < vector.length; i++)
            result[i] = get(i) - v.get(i);
        return new Vector(result);
    }

    public Vectorable substractLocal(Vectorable v) {
        checkEqualVectorLength(v);
        for (int i = 0; i < vector.length; i++)
            vector[i] = get(i) - v.get(i);
        return this;
    }

    public Vectorable zero() {
        for (int i = 0; i < vector.length; i++)
            vector[i] = 0;
        return this;
    }

    public boolean equals(Object o) {
        Vectorable that;
        if (o instanceof Vectorable)
            that = (Vectorable) o;
        else return false;
        for (int i = 0; i < vector.length; i++)
            if (get(i) != that.get(i))
                return false;

        return true;
    }

}
