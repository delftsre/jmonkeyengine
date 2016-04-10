package com.jme3.math;

public interface Vectorable {

    Vectorable add(Vectorable v);
    Vectorable addLocal(Vectorable v);
    float angleBetween(Vectorable v);
    Vectorable cross(Vectorable v);
    float determinant(Vectorable v);
    float distance(Vectorable v);
    float distanceSquared(Vectorable v);
    Vectorable divide(float d);
    Vectorable divideLocal(float d);
    float dot(Vectorable v);
    float get(int i);
    float[] getVector();
    Vectorable interpolateLocal(Vectorable v, float changeAmount);
    boolean isValidVector(Vectorable v);
    float length();
    float lengthSquared();
    Vectorable mult(float m);
    Vectorable multLocal(float m);
    Vectorable negate();
    Vectorable negateLocal();
    Vectorable normalize();
    Vectorable normalizeLocal();
    Vectorable set(Vectorable v);
    Vectorable substract(Vectorable v);
    Vectorable substractLocal(Vectorable v);
    Vectorable zero();

}
