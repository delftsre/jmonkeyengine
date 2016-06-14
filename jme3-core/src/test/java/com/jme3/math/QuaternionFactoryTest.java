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

/**
 * Verifies that algorithms in {@link Quaternion} are working correctly.
 * 
 * @author Enrique Correa
 */
public class QuaternionFactoryTest {
  @Test
  public void testFromRotationMatrix() {
    Quaternion q;
    q = QuaternionFactory.createFromRotationMatrix(10f, 10f, 10f, 10f, 10f, 10f, 10f, 10f, 10f);
    assert q.getX() == 0f;
    assert q.getY() == 0f;
    assert q.getZ() == 0f;
    q = QuaternionFactory.createFromRotationMatrix(100f, 5f, 3f, 2f, 7f, 8f, 1f, 100f, 7f);
    assert q.getX() == 0.082727775f;
    assert q.getY() == 0.07955756f;
    assert q.getZ() == -0.009068803f;
    q = QuaternionFactory.createFromRotationMatrix(18f, 35f, 53f, 52f, -6f, -8f, -16f, -100f, 27f);
    assert q.getX() == -0.30958095f;
    assert q.getY() == 0.4447462f;
    assert q.getZ() == 0.22101063f;
  }
  @Test
  public void testFromAngle() {
    Quaternion q;
    q = QuaternionFactory.createFromAngles(0.5f * FastMath.PI, 0.1f * FastMath.PI, 1.5f * FastMath.PI);
    assert q.getX() == -0.41562694f;
    assert q.getY() == 0.41562694f;
    assert q.getZ() == 0.5720614f;
    q = QuaternionFactory.createFromAngles(0.35f * FastMath.PI, 0.15f * FastMath.PI, -1.5f * FastMath.PI);
    assert q.getX() == -0.5f;
    assert q.getY() == -0.5f;
    assert q.getZ() == -0.49999997f;
    q = QuaternionFactory.createFromAngles(5f * FastMath.PI, -0.1f * FastMath.PI, 0.33f * FastMath.PI);
    assert q.getX() == 0.8579372f;
    assert q.getY() == 0.48935884f;
    assert q.getZ() == 0.13588375f;
  }
}