package com.jme3.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VectorTest {

    @Test
    public void testAdd() {
        float[] varray = {1.0f, 2.0f, 3.0f};
        float[] uarray = {1.0f, 2.0f, 3.0f};
        Vectorable v = new Vector(varray);
        Vectorable u = new Vector(uarray);

        float[] expectedarray = {2.0f, 4.0f, 6.0f};
        Vectorable expected = new Vector(expectedarray);

        assertEquals(expected, v.add(u));
    }

    @Test
    public void testAddLocal() {
        float[] varray = {1.0f, 2.0f, 3.0f};
        float[] uarray = {1.0f, 2.0f, 3.0f};
        Vectorable v = new Vector(varray);
        Vectorable u = new Vector(uarray);

        float[] expectedarray = {2.0f, 4.0f, 6.0f};
        Vectorable expected = new Vector(expectedarray);

        v.addLocal(u);

        assertEquals(expected, v);
    }

    @Test
    public void testDistance() {
        float[] va = {1.0f, 2.0f};
        float[] ua = {2.0f, 3.0f};
        Vectorable v = new Vector(va);
        Vectorable u = new Vector(ua);

        float expected = (float) Math.sqrt(2);

        assertEquals(v.distance(u), expected, 1e-5);
    }

    @Test
    public void testDivide() {
        float[] va = {1.0f, 2.0f};
        Vectorable v = new Vector(va);

        float[] ea = {0.5f, 1.0f};
        Vectorable expected = new Vector(ea);

        assertEquals(expected, v.divide(2));
    }

    @Test
    public void testDot() {
        float[] a = {1.0f, 2.0f};
        Vectorable v = new Vector(a);
        Vectorable u = new Vector(a);

        float expected = 5.0f;

        assertEquals(expected, v.dot(u), 1e-5);
    }

    @Test
    public void testGet() {
        float[] va = {1.0f, 2.0f};
        Vectorable v = new Vector(va);

        assertEquals(1.0f, v.get(0), 1e-5);
        assertEquals(2.0f, v.get(1), 1e-5);
    }

    @Test
    public void testLength() {
        float[] va = {1.0f, 2.0f};
        Vectorable v = new Vector(va);

        float expected = (float) Math.sqrt(5.0);

        assertEquals(expected, v.length(), 1e-5);
    }

    @Test
    public void testMult() {
        float[] va = {1.0f, 2.0f, 3.0f};
        Vectorable v = new Vector(va);

        float[] ea = {2.0f, 4.0f, 6.0f};
        Vectorable expected = new Vector(ea);

        assertEquals(expected, v.mult(2));
    }

    @Test
    public void testNegate() {
        float[] va = {1.0f, 2.0f, 3.0f};
        Vectorable v = new Vector(va);

        float[] ea = {-1.0f, -2.0f, -3.0f};
        Vectorable expected = new Vector(ea);

        assertEquals(expected, v.negate());
    }

    @Test
    public void testNormalize() {
        float[] va = {1.0f, 2.0f};
        Vectorable v = new Vector(va);

        float[] ea = {1.0f / (float) Math.sqrt(5), 2.0f / (float) Math.sqrt(5)};
        Vectorable expected = new Vector(ea);

        assertEquals(expected, v.normalize());
    }

    @Test
    public void testSubstract() {
        float[] va = {1.0f, 2.0f};
        float[] ua = {2.0f, 1.0f};
        Vectorable v = new Vector(va);
        Vectorable u = new Vector(ua);

        float[] ea = {-1.0f, 1.0f};
        Vectorable expected = new Vector(ea);

        assertEquals(expected, v.substract(u));
    }

    @Test
    public void testZero() {
        float[] va = {1.0f, 2.0f};
        Vectorable v = new Vector(va);

        float[] ea = {0, 0};
        Vectorable expected = new Vector(ea);

        assertEquals(expected, v.zero());
    }
}
