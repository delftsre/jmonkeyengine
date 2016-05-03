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
    public void testApproximateEquals() {
        assertTrue(FastMath.approximateEquals(100000, 100001));
        assertFalse(FastMath.approximateEquals(10000, 10001));
    }

    public float slowInvSqrt(float fValue) {
        return (float) (1.0f / Math.sqrt(fValue));
    }

    @Test
    public void testFastInvSqrt() {
        for (float i = 0.1f; i < 5f; i+=0.1) {
            // Is not very accurate
            assertTrue(FastMath.abs(slowInvSqrt(i) - FastMath.fastInvSqrt(i)) < 0.1f);
        }
    }
}
