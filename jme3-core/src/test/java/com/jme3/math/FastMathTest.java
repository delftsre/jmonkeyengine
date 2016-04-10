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

import org.hamcrest.CoreMatchers;
import org.junit.Test;

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
    public void testLinearInterpolate() {
        // Some good weather situations
        assertEquals(60.0f, FastMath.interpolateLinear(0.1f, 50.0f, 150.0f),  0.001f);
        assertEquals(100.0f, FastMath.interpolateLinear(0.5f, 50.0f, 150.0f),  0.001f);
        assertEquals(70.0f, FastMath.interpolateLinear(0.2f, 50.0f, 150.0f),  0.001f);

        // Some bad weather situations
        // On the border of the range
        assertEquals(50.0f, FastMath.interpolateLinear(0.0f, 50.0f, 150.0f),  0.001f);
        // Other border
        assertEquals(150.0f, FastMath.interpolateLinear(1.0f, 50.0f, 150.0f),  0.001f);
        // Way below the lower bound
        assertEquals(50.0f, FastMath.interpolateLinear(-100.0f, 50.0f, 150.0f),  0.001f);
        // Way above the higher bound
        assertEquals(150.0f, FastMath.interpolateLinear(200.0f, 50.0f, 150.0f), 0.001f);
    }

    @Test
    public void testVectorLinearInterpolate() {
        //Vectors to be tested
        Vector3f v1s = new Vector3f(10.0f, 30.0f, 25.0f);
        Vector3f v1e = new Vector3f(15.0f, 42.0f, 27.5f);
        Vector3f v1eneg = new Vector3f(-15.0f, -42.0f, -27.5f);

        //Resultvectors
        Vector3f v1r1 = new Vector3f(10.5f, 31.2f, 25.25f);
        Vector3f v1r2 = new Vector3f(12.5f, 36.0f, 26.25f);
        Vector3f v1r3 = new Vector3f(15.0f, 42.0f, 27.5f);
        Vector3f v1rneg = new Vector3f(-2.5f, -6.0f, -1.25f);

        //Test different scales with correct vectors
        assertEquals(v1r1, FastMath.interpolateLinear(0.1f, v1s, v1e));
        assertEquals(v1r2, FastMath.interpolateLinear(0.5f, v1s, v1e));
        assertEquals(v1r3, FastMath.interpolateLinear(1.0f, v1s, v1e));

        //Test negative end vector which is also smaller than the start vector
        assertEquals(v1rneg, FastMath.interpolateLinear(0.5f, v1s, v1eneg));

        //Test out of bounds scales
        assertEquals(v1s, FastMath.interpolateLinear(-0.5f, v1s, v1e));
        assertEquals(v1e, FastMath.interpolateLinear(1.5f, v1s, v1e));
    }

    @Test
    public void interpolateCatmullRom() {
        float[] expected = new float[]{5.147461f, 5.3921876f, 5.7001953f, 6.0375f, 6.370117f, 6.6640625f, 6.8853517f};

        for (int i = 0; i < 7; i++) {
            float value = ((float) i + 1) / 8;

            assertEquals(expected[i], FastMath.interpolateCatmullRom(value, 0.1f, 0f, 5f, 7f, 9f), 0.001f);
        }

        //Test boundaries
        assertEquals(5.0f, FastMath.interpolateCatmullRom(0.0f, 0.1f, 0f, 5f, 7f, 9f), 0.001f);
        assertEquals(7.0f, FastMath.interpolateCatmullRom(1.0f, 0.1f, 0f, 5f, 7f, 9f), 0.001f);
    }

    @Test
    public void interpolateVectorCatmullRom() {
        Vector3f[] expected = new Vector3f[]{
                new Vector3f(1.1719726f, 3.875293f , 0.31552735f),
                new Vector3f(1.5320313f, 6.899219f , -0.19140625f),
                new Vector3f(2.0151367f, 11.006349f, -0.91357434f),
                new Vector3f(2.5562499f, 15.631251f, -1.7437499f),
                new Vector3f(3.0903318f, 20.208498f, -2.574707f),
                new Vector3f(3.5523434f, 24.172657f, -3.2992187f),
                new Vector3f(3.8772454f, 26.958302f, -3.810058f)
        };

        Vector3f p0 = new Vector3f(-0.5f, -1.0f, -5.0f);
        Vector3f p1 = new Vector3f(1.0f, 2.5f, 0.5f);
        Vector3f p2 = new Vector3f(4.0f, 28.0f, -4.0f);
        Vector3f p3 = new Vector3f(1.0f, 1.0f, 1.0f);

        for (int i = 0; i < 7; i++) {
            float value = ((float) i + 1) / 8;

            Vector3f currentResult = FastMath.interpolateCatmullRom(value, 0.1f, p0, p1, p2, p3);

            assertEquals(expected[i].getX(), currentResult.getX(), 0.001f);
            assertEquals(expected[i].getY(), currentResult.getY(), 0.001f);
            assertEquals(expected[i].getZ(), currentResult.getZ(), 0.001f);
        }

        //Test boundaries
        Vector3f lowerBoundaryResult = FastMath.interpolateCatmullRom(0.0f, 0.1f, p0, p1, p2, p3);
        assertEquals(p1.getX(), lowerBoundaryResult.getX(), 0.001f);
        assertEquals(p1.getY(), lowerBoundaryResult.getY(), 0.001f);
        assertEquals(p1.getZ(), lowerBoundaryResult.getZ(), 0.001f);

        Vector3f higherBoundaryResult = FastMath.interpolateCatmullRom(1.0f, 0.1f, p0, p1, p2, p3);
        assertEquals(p2.getX(), higherBoundaryResult.getX(), 0.001f);
        assertEquals(p2.getY(), higherBoundaryResult.getY(), 0.001f);
        assertEquals(p2.getZ(), higherBoundaryResult.getZ(), 0.001f);
    }

    @Test
    public void interpolateBezier() {
        float[] expected = new float[]{1.7402344f, 3.234375f, 4.517578f, 5.625f, 6.591797f, 7.453125f, 8.244141f};

        for (int i = 0; i < 7; i++) {
            float value = ((float) i + 1) / 8;

            assertEquals(expected[i], FastMath.interpolateBezier(value, 0f, 5f, 7f, 9f), 0.001f);
        }

        assertEquals(0.0f, FastMath.interpolateBezier(0.0f, 0f, 5f, 7f, 9f), 0.001f);
        assertEquals(9.0f, FastMath.interpolateBezier(1.0f, 0f, 5f, 7f, 9f), 0.001f);
    }

    @Test
    public void testGetBezierP1toP2Length() {
        Vector3f p0 = new Vector3f(10f, 10f, 10f);
        Vector3f p1 = new Vector3f(12f, 14f, 11f);
        Vector3f p2 = new Vector3f(15f, 15f, 17f);
        Vector3f p3 = new Vector3f(17f, 20f, 19f);

        assertEquals(FastMath.getBezierP1toP2Length(p0, p1, p2, p3), 15.394257f, 0.001f);

        p0 = new Vector3f(12f, 10f, 15f);
        p1 = new Vector3f(15f, 13f, 17f);
        p2 = new Vector3f(16f, 17f, 19f);
        p3 = new Vector3f(16f, 20f, 19f);

        assertEquals(FastMath.getBezierP1toP2Length(p0, p1, p2, p3), 11.833087f, 0.001f);
    }

    @Test
    public void interpolateVectorBezier() {
        Vector3f[] expected = new Vector3f[]{
            new Vector3f(0.11816406f, 1.1982422f    , -3.368164f),
            new Vector3f(0.7890625f , 4.5859375f    , -2.4453125f),
            new Vector3f(1.4248047f , 8.290039f     , -2.0029297f),
            new Vector3f(1.9375f    , 11.4375f      , -1.8125f),
            new Vector3f(2.2392578f , 13.155273f    , -1.6455078f),
            new Vector3f(2.2421875f , 12.5703125f   , -1.2734375f),
            new Vector3f(1.8583984f , 8.80957f      , -0.46777344f)
        };

        Vector3f p0 = new Vector3f(-0.5f, -1.0f, -5.0f);
        Vector3f p1 = new Vector3f(1.0f, 2.5f, 0.5f);
        Vector3f p2 = new Vector3f(4.0f, 28.0f, -4.0f);
        Vector3f p3 = new Vector3f(1.0f, 1.0f, 1.0f);

        for (int i = 0; i < 7; i++) {
            float value = ((float) i + 1) / 8;

            Vector3f currentResult = FastMath.interpolateBezier(value, p0, p1, p2, p3);

            assertEquals(expected[i].getX(), currentResult.getX(), 0.001f);
            assertEquals(expected[i].getY(), currentResult.getY(), 0.001f);
            assertEquals(expected[i].getZ(), currentResult.getZ(), 0.001f);
        }

        //Test boundaries
        Vector3f lowerBoundaryResult = FastMath.interpolateBezier(0.0f, p0, p1, p2, p3);
        assertEquals(p0.getX(), lowerBoundaryResult.getX(), 0.001f);
        assertEquals(p0.getY(), lowerBoundaryResult.getY(), 0.001f);
        assertEquals(p0.getZ(), lowerBoundaryResult.getZ(), 0.001f);

        Vector3f higherBoundaryResult = FastMath.interpolateBezier(1.0f, p0, p1, p2, p3);
        assertEquals(p3.getX(), higherBoundaryResult.getX(), 0.001f);
        assertEquals(p3.getY(), higherBoundaryResult.getY(), 0.001f);
        assertEquals(p3.getZ(), higherBoundaryResult.getZ(), 0.001f);
    }
}
