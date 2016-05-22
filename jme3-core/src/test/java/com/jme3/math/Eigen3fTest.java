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
 * Verifies that algorithms in {@link Eigen3d} are working correctly.
 * 
 * @author Enrique Correa
 */
public class Eigen3fTest {
  @Test
  public void testCalculateEigen() {
    Matrix3f mat = new Matrix3f(2, 1, 1, 1, 2, 1, 1, 1, 2);
    Eigen3f eigenSystem = new Eigen3f(mat);

    assert eigenSystem.getEigenValue(0) == 1f;
    assert eigenSystem.getEigenValue(1) == 1f;
    assert eigenSystem.getEigenValue(2) == 4f;

    assert eigenSystem.getEigenVector(0).equals(new Vector3f(-0.81649655f, 0.40824828f, 0.40824828f));
    assert eigenSystem.getEigenVector(1).equals(new Vector3f(4.4703484E-8f, 0.7071067f, -0.7071066f));
    assert eigenSystem.getEigenVector(2).equals(new Vector3f(-0.5773502f, -0.57735014f, -0.57735026f));

    // Verify results by definition
    assert mat.mult(eigenSystem.getEigenVector(0)).distanceSquared(eigenSystem.getEigenVector(0).mult(eigenSystem.getEigenValue(0))) < 0.1f;
    assert mat.mult(eigenSystem.getEigenVector(1)).distanceSquared(eigenSystem.getEigenVector(1).mult(eigenSystem.getEigenValue(1))) < 0.1f;
    assert mat.mult(eigenSystem.getEigenVector(2)).distanceSquared(eigenSystem.getEigenVector(2).mult(eigenSystem.getEigenValue(2))) < 0.1f;

  }
}