/*
 * Copyright (c) 2009-2016 jMonkeyEngine
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
import static org.junit.Assert.*;

public class ColorRGBATest {
	
	@Test
	public void testEmptyArray() {
		double[] data = new double[0];
		ColorRGBA[] colorArr = ColorRGBA.toColorRGBA(data);
		assertEquals(0, colorArr.length);
	}
	
	@Test(expected=AssertionError.class)
	public void testArrayWithWrongLengthElements() {
		double[] data = {1.0, 3.0, 1.5};
		ColorRGBA[] colorArr = ColorRGBA.toColorRGBA(data);
	}
	
	@Test
	public void testCorrectWorkingOneColor() {
		double[] data = {0.1, 0.2, 0.3, 0.4};
		ColorRGBA[] colorArr = ColorRGBA.toColorRGBA(data);
		assertEquals(1, colorArr.length);
		ColorRGBA res = colorArr[0];
		double delta = 0.000001;
		assertEquals(data[0], res.r, delta);
		assertEquals(data[1], res.g, delta);
		assertEquals(data[2], res.b, delta);
		assertEquals(data[3], res.a, delta);
	}
	
	@Test
	public void testCorrectWorkingMultipleColors() {
		double[] data = {
			0.1, 0.2, 0.3, 0.4,
			0.5, 0.6, 0.7, 0.8,
			0.9, 1.0, 1.1, 1.2,
		};
		ColorRGBA[] colorArr = ColorRGBA.toColorRGBA(data);
		assertEquals(3,colorArr.length);
		
		ColorRGBA expected1 = new ColorRGBA(0.1f, 0.2f, 0.3f, 0.4f);
		ColorRGBA expected2 = new ColorRGBA(0.5f, 0.6f, 0.7f, 0.8f);
		ColorRGBA expected3 = new ColorRGBA(0.9f, 1.0f, 1.1f, 1.2f);
		
		assertTrue(expected1.equals(colorArr[0]));
		assertTrue(expected2.equals(colorArr[1]));
		assertTrue(expected3.equals(colorArr[2]));
	}
}
