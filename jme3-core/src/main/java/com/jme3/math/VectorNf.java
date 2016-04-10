package com.jme3.math;


import java.util.Iterator;

/**
 * Common ancenstor of all Vector classes
 *
 * Specifies a generic way to access vectors of different sizes
 */
public interface VectorNf {
    /*
    * Returns the size of the Vector currenlty being evaluated
    */
    public int size();

    /*
    * Returns the component on a certain index.
    *
    * Should throw an exception if the index is outside of the range of this VectorNF. Always returns a float if the
    * index is between zero and VectorNf#size();
     */
    public float get(int index);

    /*
    * Sets the component on a certain index.
    *
    * Should throw an exception if the index is outside of the range of this VectorNF. Always sets a float if the
    * index is between zero and VectorNf#size();
    */
    public void setIndex(int index, float value);
}
