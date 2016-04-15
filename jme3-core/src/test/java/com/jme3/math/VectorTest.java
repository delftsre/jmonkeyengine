package com.jme3.math;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VectorTest {
	
	private boolean checkEachValue(Vector v, float f) {
		for (int x = 0; x < v.values.length; x++) {
			if (v.values[x] != f) return false;
		}
		return true;
	}
	
	private boolean checkAllValues(Vector v, float[] f) {
		for (int x = 0; x < v.values.length; x++) {
			if (v.values[x] != f[x]) return false;
		}
		return true;
	}
    
//    @Before
//    public void setUp() {
//        
//    }

    @Test
    public void constructorSizeTest() {
    	Vector v = new Vector(42);
    	assert(v.values.length == 42);
    	for (int x = 0; x < v.values.length; x++) {
    		assert(v.values[x] == 0);
    	}
    }
    
    @Test(expected=Exception.class)
    public void constructorNegativeSizeTest() {
    	new Vector(-1);
    }
    
    @Test
    public void constructorSingleFloatTest() {
    	Vector v = new Vector(42f);
    	assert(v.values.length == 1);
    	assert(v.values[0] == 42);
    }
    
    @Test
    public void constructorArrayTest() {
    	float[] f = new float[10];
    	for (int x = 0; x < f.length; x++) {
    		f[x] = (float)(Math.random()*10+1);
    	}
    	Vector v = new Vector(f);
    	
    	assert(v.values.length == 10);
    	assert(checkAllValues(v, f));
    	
    	// Test for constructor that copies from an other vector
    	Vector v2 = new Vector(v);
    	
    	assert(v2.values.length == 10);
    	assert(checkAllValues(v2, f)); 
    }
    
    @Test
    public void ZEROTest() {
    	assert(checkEachValue(Vector.ZERO(5), 0));
    }
    
    @Test
    public void makeZEROTest() {
    	Vector v = new Vector(3, 4, 5, 6);
    	v.makeZERO();
    	assert(checkEachValue(v, 0));
    }
    
    @Test
    public void NANTest() {
    	Vector v = Vector.NAN(5);
    	for (int x = 0; x < v.values.length; x++) {
    		assert(Float.isNaN(v.values[x]));
    	}
    }
    
    @Test
    public void makeNANTest() {
    	Vector v = new Vector(3, 4, 5, 6);
    	v.makeNAN();
    	for (int x = 0; x < v.values.length; x++) {
    		assert(Float.isNaN(v.values[x]));
    	}
    }
    
    @Test
    public void ONESTest() {
    	Vector v = Vector.ONES(5);
    	assert(checkEachValue(v, 1));
    }
    
    @Test
    public void makeONESTest() {
    	Vector v = new Vector(3, 4, 5, 6);
    	v.makeONES();
    	assert(checkEachValue(v, 1));
    }
    
    @Test
    public void makeUNITTest() {
    	Vector v = new Vector(3, 4, 5, 6);
    	v.makeUNIT(2);
    	for (int x = 0; x < v.values.length; x++) {
    		if (x == 2) assert(v.values[x] == 1);
    		else assert(v.values[x] == 0);
    	}
    }
    
    @Test
    public void UNITTEst() {
    	Vector v = Vector.UNIT(5, 2);
    	for (int x = 0; x < v.values.length; x++) {
    		if (x == 2) assert(v.values[x] == 1);
    		else assert(v.values[x] == 0);
    	}
    }
    
    @Test
    public void POSITIVE_INFTest() {
    	Vector v = Vector.POSITIVE_INF(5);
    	for (int x = 0; x < v.values.length; x++) {
    		assert(Float.isInfinite(v.values[x]));
    	}
    }
    
    @Test
    public void makePOSITIVE_INFTest() {
    	Vector v = new Vector(3, 4, 5, 6);
    	v.makePOSITIVE_INF();
    	for (int x = 0; x < v.values.length; x++) {
    		assert(Float.isInfinite(v.values[x]));
    	}
    }
    
    @Test
    public void NEGATIVE_INFTest() {
    	Vector v = Vector.NEGATIVE_INF(5);
    	for (int x = 0; x < v.values.length; x++) {
    		assert(Float.isInfinite(v.values[x]));
    	}
    }
    
    @Test
    public void makeNEGATIVE_INFTest() {
    	Vector v = new Vector(3, 4, 5, 6);
    	v.makeNEGATIVE_INF();
    	for (int x = 0; x < v.values.length; x++) {
    		assert(Float.isInfinite(v.values[x]));
    	}
    }
    
    @Test
    public void setTest() {
    	Vector v = Vector.ONES(5);
    	float[] f = new float[]{1, 2, 3, 4, 5};
    	v.set(f);
    	assert(checkAllValues(v, f));
    	
    	Vector v2 = new Vector(f);
    	v.set(v2);
    	assert(checkAllValues(v2, f));
    }
    
    @Test
    public void addTest() {
    	float[] vf = new float[]{1, 2, 3, 4, 5};
    	Vector v = new Vector(vf);
    	float[] af = new float[]{4, 3, 2, 1, 0};
    	Vector a = new Vector(af);
    	Vector r = Vector.NAN(5);
    	
    	assert(checkEachValue(v.add(af), 5f));
    	assert(checkEachValue(v.add(a), 5f));
    	v.add(a, r);
    	assert(checkEachValue(r, 5));
    	
    	// addLocal
    	v.addLocal(af);
    	assert(checkEachValue(v, 5f));
    	v.set(vf);
    	v.addLocal(a);
    	assert(checkEachValue(v, 5f));
    }
    
    @Test
    public void scaleAddTest() {
    	float scalar = 2;
    	Vector r = Vector.NAN(5);
    	Vector v = new Vector(1,2,3,4,5);
    	Vector a = Vector.ONES(5);
    	
    	r.scaleAdd(scalar, v, a);
    	assert(checkAllValues(r, new float[]{3, 5, 7, 9, 11}));
    	
    	r.set(Vector.ONES(5).scaleAdd(3, v));
    	assert(checkAllValues(r, new float[]{4, 5, 6, 7, 8}));
    }
    
    @Test
    public void dotTest() {
    	Vector v  = Vector.ONES(5);
    	Vector v2 = new Vector(1,2,3,4,5);
    	Vector v3 = new Vector(1,2,3,4,5);
    	
    	assert(v.dot(v2) == 15);
    	assert(v2.dot(v3) == (1 + 4 + 9 + 16 + 25));
    }
    
//    @Test
//    public void projectTest() {
//    	Vector a = new Vector(1,2,2);
//    	Vector b = new Vector(3,2,1);
//    	Vector c = b.project(a);
//    	System.out.println(c);
//    	System.out.println("(" + 27/14 + ", " + 9/7 + ", " + 9/14 + ")");
//    	assert(checkAllValues(c, new float[]{27/14, 9/7, 9/14}));
//    }
    
    @Test
    public void isUnitVectorTest() {
    	assertTrue (Vector.ONES(1).isUnitVector());
    	assertFalse(Vector.ONES(5).isUnitVector());
    	assertFalse(Vector.ZERO(5).isUnitVector());
    	
    	assertTrue (new Vector(1,0,0).isUnitVector());
    	assertTrue (new Vector(0,1,0).isUnitVector());
    	assertTrue (new Vector(0,0,1).isUnitVector());
    	assertFalse(new Vector(1,1,0).isUnitVector());
    	assertFalse(new Vector(0,1,1).isUnitVector());
    	assertFalse(new Vector(1,0,1).isUnitVector());
    	assertFalse(new Vector(1,1,1).isUnitVector());
    }
    
    @Test
    public void lengthSquaredTest() {
    	Vector v = new Vector(1,2,3,4,5);
    	assert(v.lengthSquared() == (1 + 4 + 9 + 16 + 25));
    }
    
    @Test
    public void lengthTest() {
    	Vector v = new Vector(1,2,3,4,5);
    	assert(v.length() == FastMath.sqrt(55));
    }
    
    @Test 
    public void distanceSquaredTest() {
    	Vector a = new Vector(1,2,3,4,5);
    	Vector b = new Vector(2,2,2,2,2);
    	Vector c = Vector.ONES(5);
    	
    	assert(a.distanceSquared(b) == 15);
    	assert(b.distanceSquared(a) == 15);
    	assert(a.distanceSquared(c) == 30);
    }
    
    @Test
    public void distanceTest() {
    	Vector a = new Vector(1,2,3,4,5);
    	Vector b = new Vector(2,2,2,2,2);
    	Vector c = Vector.ONES(5);
    	
    	assert(a.distance(b) == FastMath.sqrt(15));
    	assert(b.distance(a) == FastMath.sqrt(15));
    	assert(a.distance(c) == FastMath.sqrt(30));
    }
    
    @Test
    public void multTest() {
    	Vector a = Vector.ONES(5);
    	Vector b = new Vector(1,2,3,4,5);
    	Vector c = new Vector(5);
    	
    	assert(checkEachValue(a.mult(5), 5));
    	assert(checkAllValues(b.mult(3), new float[]{3, 6, 9, 12, 15}));
    	
    	b.mult(3, c);
    	assert(checkAllValues(c, new float[]{3, 6, 9, 12, 15}));
    	b.mult(a, c);
    	assert(checkAllValues(c, b.values));
    	
    	a.multLocal(b);
    	assert(checkAllValues(a, new float[]{1, 2, 3, 4, 5}));
    	a.makeONES();
    	a.multLocal(5);
    	assert(checkEachValue(a, 5));
    }
    
    @Test
    public void divideTest() {
    	Vector a = new Vector(12, 12, 12);
    	Vector b = new Vector(4, 3, 2);
    	
    	assert(checkAllValues(a.divide(2), new float[]{6, 6, 6}));
    	assert(checkAllValues(a.divide(b), new float[]{3, 4, 6}));
    	
    	Vector c = new Vector(a);
    	c.divideLocal(2);
    	assert(checkAllValues(c, new float[]{6, 6, 6}));
    	c = new Vector(a);
    	c.divideLocal(b);
    	assert(checkAllValues(c, new float[]{3, 4, 6}));
    }
    
    @Test
    public void negateTest() {
    	Vector a = Vector.ONES(3);
    	Vector b = new Vector(-1, -1, -1);
    	Vector c = Vector.ZERO(3);
    	Vector d = new Vector(-3, 3, 0);
    	
    	assert(checkEachValue(a.negate(), -1));
    	assert(checkEachValue(b.negate(), 1));
    	assert(checkEachValue(c.negate(), 0));
    	assert(checkAllValues(d.negate(), new float[]{3, -3, 0}));
    	
    	a.negateLocal(); b.negateLocal(); c.negateLocal(); d.negateLocal();
    	assert(checkEachValue(a, -1));
    	assert(checkEachValue(b, 1));
    	assert(checkEachValue(c, 0));
    	assert(checkAllValues(d, new float[]{3, -3, 0}));
    }
    
    @Test
    public void subtractTest() {
    	Vector a = Vector.ONES(3);
    	Vector b = Vector.ZERO(3);
    	Vector c = new Vector(5, 6, 7, 8);
    	Vector d = new Vector(12, 10, 8, 4);
    	
    	assert(checkEachValue(a.subtract(b), 1));
    	assert(checkEachValue(b.subtract(a), -1));
    	assert(checkEachValue(b.subtract(b), 0));
    	assert(checkAllValues(d.subtract(c), new float[]{7, 4, 1, -4}));
    }
    
    @Test
    public void normalizeTest() {
    	Vector a = Vector.ONES(3);
    	Vector b = new Vector(1,0,0);
    	
    	assert(checkAllValues(b.normalize(), new float[]{1,0,0}));
    	assert(checkAllValues(a.normalize(), new float[]{1/FastMath.sqrt(3),1/FastMath.sqrt(3),1/FastMath.sqrt(3)}));
    	a.normalizeLocal();
    	assert(checkAllValues(a, new float[]{1/FastMath.sqrt(3),1/FastMath.sqrt(3),1/FastMath.sqrt(3)}));
    }
    
    @Test
    public void maxTest() {
    	Vector a = new Vector(1,5,8,10);
    	Vector b = new Vector(5,3,8,11);
    	assert(checkAllValues(a.max(b), new float[]{5, 5, 8, 11}));
    	a.maxLocal(b);
    	assert(checkAllValues(a, new float[]{5, 5, 8, 11}));
    }
    
    @Test
    public void minTest() {
    	Vector a = new Vector(1,5,8,10);
    	Vector b = new Vector(5,3,8,11);
    	assert(checkAllValues(a.min(b), new float[]{1, 3, 8, 10}));
    	a.minLocal(b);
    	assert(checkAllValues(a, new float[]{1, 3, 8, 10}));
    }
    
    @Test
    public void angleBetweenTest() {
    	Vector a = new Vector(1,0,0);
    	Vector b = new Vector(0,1,0);
    	
    	assert(a.angleBetween(a) == 0);
    	assert(a.angleBetween(b) == 0.5 * FastMath.PI);
    }
    
    @Test
    public void interpolateTest() {
    	Vector a = Vector.ZERO(4);
    	Vector b = Vector.ONES(4);
    	
    	a.interpolateLocal(b, 0.5f);
    	assert(checkEachValue(a, 0.5f));
    	a.makeZERO();
    	b.set(new float[]{20, 23, 6, 7});
    	Vector c = new Vector(2, 3, 4, 5);
    	a.interpolateLocal(b, c, 0.2f);
    	assert(checkAllValues(a, new float[]{16.4f, 19f, 5.6000004f, 6.6f}));
    }
    
    @Test
    public void isValidVectorTest() {
    	Vector a = Vector.ONES(4);
    	Vector b = Vector.NAN(4);
    	Vector c = Vector.POSITIVE_INF(4);
    	Vector d = new Vector(4, 5, 6, Float.NaN);
    	Vector e = new Vector(4, 5, 6, Float.NEGATIVE_INFINITY);
    	
    	assert(a.isValidVector());
    	assertFalse(b.isValidVector());
    	assertFalse(c.isValidVector());
    	assertFalse(d.isValidVector());
    	assertFalse(e.isValidVector());
    }
    
    @Test
    public void toArrayTest() {
    	float[] f = new float[]{0,0,0,0};
    	float[] f2 = new float[]{5,6,7,8};
    	Vector v = new Vector(f2);
    	v.toArray(f);
    	for (int x = 0; x < f.length; x++) {
    		assert(f[x] == f2[x]);
    	}
    }
    
    @Test
    public void getAndSetTest() {
    	Vector a = new Vector(1,2,3,4,5,6);
    	assert(a.getAtIndex(0) == 1);
    	assert(a.getAtIndex(1) == 2);
    	
    	a.setAtIndex(0, 10);
    	assert(a.getAtIndex(0) == 10);
    }
}
