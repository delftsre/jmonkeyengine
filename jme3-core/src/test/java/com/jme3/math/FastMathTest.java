/*
 * Copyright (c) 2009-2015 jMonkeyEngine
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

import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

/**
 * Verifies that algorithms in {@link FastMath} are working correctly.
 * 
 * @author Kirill Vainer
 */
public class FastMathTest {
    
    private int nearestPowerOfTwoSlow(int number) {
        return (int) Math.pow(2, Math.ceil(Math.log(number) / Math.log(2)));
    }
    
    @Test
    public void testNearestPowerOfTwo() {
        for (int i = -100; i < 1; i++) {
            assert FastMath.nearestPowerOfTwo(i) == 1;
        }
        for (int i = 1; i < 10000; i++) {
            int nextPowerOf2 = FastMath.nearestPowerOfTwo(i);
            assert i <= nextPowerOf2;
            assert FastMath.isPowerOfTwo(nextPowerOf2);
            assert nextPowerOf2 == nearestPowerOfTwoSlow(i);
        }
    }

    @Test
    public void testIsPowerOfTwo() {
        assertFalse(FastMath.isPowerOfTwo(-16));
        assertFalse(FastMath.isPowerOfTwo(-10));
        assertFalse(FastMath.isPowerOfTwo(-8));
        assertFalse(FastMath.isPowerOfTwo(-7));
        assertFalse(FastMath.isPowerOfTwo(-4));
        assertFalse(FastMath.isPowerOfTwo(-1));
        assertFalse(FastMath.isPowerOfTwo(0));
        assertTrue(FastMath.isPowerOfTwo(1));
        assertTrue(FastMath.isPowerOfTwo(2));
        assertFalse(FastMath.isPowerOfTwo(3));
        assertTrue(FastMath.isPowerOfTwo(4));
        assertFalse(FastMath.isPowerOfTwo(5));
        assertFalse(FastMath.isPowerOfTwo(6));
        assertFalse(FastMath.isPowerOfTwo(7));
        assertTrue(FastMath.isPowerOfTwo(8));
    }

    @Test
    public void interpolateLinear() {
        for (float x = -3f; x < 3f; x++) {
            for (float y = -3f; y < 3f; y++) {
                assert x == FastMath.interpolateLinear(0f, x, y);
                assert x == FastMath.interpolateLinear(-1.5f, x, y);
                assert y == FastMath.interpolateLinear(1f, x, y);
                assert y == FastMath.interpolateLinear(2.5f, x, y);
                for (float scale = 0f; scale < 1f; scale += 0.2f) {
                    float interpolation = (1-scale) * x + scale * y;
                    assert interpolation == FastMath.interpolateLinear(scale, x, y); 
                }
            }
        }
    }

    @Test
    public void testExtrapolateLinear() {
        Vector3f start1 = new Vector3f(76.3f, 53.5f, 75.15f);
        Vector3f end1 = new Vector3f(1.3f, 16.5f, 56.5f);

        Vector3f sol1 = new Vector3f(75.55f, 53.13f, 74.9635f); 
        Vector3f sol2 = new Vector3f(38.800003f, 35f, 65.825f);
        Vector3f sol3 = new Vector3f(113.8f, 72.0f, 84.475006f);
        Vector3f sol4 = new Vector3f(-36.2f, -2f, 47.175f);

        // Normal
        assertEquals(sol1, FastMath.extrapolateLinear(0.01f, start1, end1));
        assertEquals(sol2, FastMath.extrapolateLinear(0.5f, start1, end1));
        // Negative
        assertEquals(sol3, FastMath.extrapolateLinear(-0.5f, start1, end1));
        // Over one
        assertEquals(sol4, FastMath.extrapolateLinear(1.5f, start1, end1));
    }

    @Test
    public void testInterpolateCatmullRom() {
        assert 3.0424f == FastMath.interpolateCatmullRom(0.1f, 0.1f, 2f, 3f, 4f, 5f);
        assert 3.1805296f == FastMath.interpolateCatmullRom(0.255f, 0.1f, 2f, 3f, 4f, 5f);
        assert 3.9503422f == FastMath.interpolateCatmullRom(0.8f / 0.9f, 0.1f, 2f, 3f, 4f, 5f);
        assert 3.9977605f == FastMath.interpolateCatmullRom(0.99f, 0.1f, 2f, 3f, 4f, 5f);
        assert 3.0056f == FastMath.interpolateCatmullRom(-0.1f, 0.1f, 2f, 3f, 4f, 5f);

        // Specials
        assert 3.9943984f == FastMath.interpolateCatmullRom(1.1f, 0.1f, 2f, 3f, 4f, 5f);
    }

    @Test
    public void testInterpolateBezier() {
        assert 2.3f == FastMath.interpolateBezier(0.1f, 2f, 3f, 4f, 5f);
        assert 2.7649999f == FastMath.interpolateBezier(0.255f, 2f, 3f, 4f, 5f);
        assert 4.666667f == FastMath.interpolateBezier(0.8f / 0.9f, 2f, 3f, 4f, 5f);
        assert 4.9700003f == FastMath.interpolateBezier(0.99f, 2f, 3f, 4f, 5f);
        assert 1.7f == FastMath.interpolateBezier(-0.1f, 2f, 3f, 4f, 5f);

        // Specials
        assert 5.2999997f == FastMath.interpolateBezier(1.1f, 2f, 3f, 4f, 5f);
    }

    @Test
    public void testApproximateEquals() {
        assertTrue(FastMath.approximateEquals(100000, 100001));
        assertFalse(FastMath.approximateEquals(10000, 10001));
    }

    public float slowInvSqrt(float fValue) {
        return (float) (1.0f / Math.sqrt(fValue));
    }

    @Test
    public void testFastInvSqrt() {
        for (float i = 0.1f; i < 5f; i += 0.1) {
            // Is not very accurate
            assertTrue(FastMath.abs(slowInvSqrt(i) - FastMath.fastInvSqrt(i)) < 0.1f);
        }
    }

    @Test
    public void testCounterClockwise() {
        Vector2f center = new Vector2f(0f, 0f);
        Vector2f v1 = new Vector2f(1f, 1f);
        Vector2f v2 = new Vector2f(1f, 0f);
        assert FastMath.counterClockwise(center, v1, v2) == -1;
        assert FastMath.counterClockwise(center, v2, v1) == 1;
        // Case middle
        assert FastMath.counterClockwise(center, center, center) == 0;
        assert FastMath.counterClockwise(v1, v1, v1) == 0;
        Vector2f middle_v1 = new Vector2f(0.5f, 0.5f);
        assert FastMath.counterClockwise(center, v1, middle_v1) == 0;
    }

    @Test
    public void testPointInsideTriangle() {
        Vector2f v0 = new Vector2f(0f, 0f);
        Vector2f v1 = new Vector2f(1f, 1f);
        Vector2f v2 = new Vector2f(2f, 0f);
        Vector2f t1 = new Vector2f(0.5f, 0.5f);
        Vector2f t2 = new Vector2f(0f, 0f);
        Vector2f t3 = new Vector2f(2f, 2f);
        Vector2f t4 = new Vector2f(-1f, -1f);
        Vector2f t5 = new Vector2f(-0.01f, -0.01f);
        assert FastMath.pointInsideTriangle(v0, v1, v2, t1) == 1;
        assert FastMath.pointInsideTriangle(v2, v1, v0, t1) == 1;
        assert FastMath.pointInsideTriangle(v0, v2, v1, t1) == 1;
        assert FastMath.pointInsideTriangle(v1, v0, v2, t1) == 1;
        assert FastMath.pointInsideTriangle(v0, v1, v2, t2) == 1;
        assert FastMath.pointInsideTriangle(v0, v1, v2, t3) == 0;
        assert FastMath.pointInsideTriangle(v0, v1, v2, t4) == 0;
        assert FastMath.pointInsideTriangle(v0, v1, v2, t5) == 0;
    }

    @Test
    public void testComputeNormal() {
        Vector3f v0 = new Vector3f(0f, 0f, 0f);
        Vector3f v1 = new Vector3f(1f, 1f, 1f);
        Vector3f v2 = new Vector3f(2f, 0f, 0f);

        Vector3f s1 = new Vector3f(0.0f, 0.70710677f, -0.70710677f);
        Vector3f s2 = new Vector3f(0.0f, -0.70710677f, 0.70710677f);
        assertEquals(s1, FastMath.computeNormal(v0, v1, v2));
        assertEquals(s2, FastMath.computeNormal(v2, v1, v0));
    }

    @Test
    public void testDeterminant() {
        // Random Matrix by WolframAlpha
        assert FastMath.determinant(
            0.0653751, 0.19441, 0.284384, 0.937996,
            0.489997, 0.199627, 0.187348, 0.0407093,
            0.202885, 0.0241573, 0.107163, 0.658972,
            0.483023, 0.220096, 0.733425, 0.671541) == 0.035817923199160336824686115f;
        assert FastMath.determinant(
            0.1, 0.2, 0.1, 0.0,
            0.2, 0.4, 0.2, 0.0,
            0.3, 0.6, 0.3, 0.0,
            0.4, 0.8, 0.4, 0.0) == 0;
        assert FastMath.determinant(
            0.1, 0.1, 0.1, 0.1,
            0.0, 0.2, 0.0, 0.2,
            0.3, 0.0, 0.3, 0.0,
            0.4, 0.8, 0.4, 0.8) == 0;
    }

    @Test
    public void testClamp() {
        assert FastMath.clamp(0f, 0f, 0f) == 0f;
        assert FastMath.clamp(5f, -1f, 1f) == 1f;
        assert FastMath.clamp(-5f, -1f, 1f) == -1f;
        assert FastMath.clamp(0.5f, 0f, 1f) == 0.5f;
    }

}
