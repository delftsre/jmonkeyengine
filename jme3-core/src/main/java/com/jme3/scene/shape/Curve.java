/*
 * Copyright (c) 2009-2012 jMonkeyEngine
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
package com.jme3.scene.shape;

import com.jme3.math.CatmullRomSpline;
import com.jme3.math.NurbSpline;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

/**
 * A
 * <code>Curve</code> is a visual, line-based representation of a {@link Spline}.
 * The underlying Spline will be sampled N times where N is the number of
 * segments as specified in the constructor. Each segment will represent one
 * line in the generated mesh.
 *
 * @author Nehon
 */
public class Curve extends Mesh {

    private Spline spline;

    /**
     * Serialization only. Do not use.
     */
    public Curve() {
    }

    /**
     * Create a curve mesh. Use a CatmullRom spline model that does not cycle.
     *
     * @param controlPoints the control points to use to create this curve
     * @param nbSubSegments the number of subsegments between the control points
     */
    public Curve(Vector3f[] controlPoints, int nbSubSegments) {
        this(new CatmullRomSpline(controlPoints, 10, false), nbSubSegments);
    }

    /**
     * Create a curve mesh from a Spline
     *
     * @param spline the spline to use
     * @param nbSubSegments the number of subsegments between the control points
     */
    public Curve(Spline spline, int nbSubSegments) {
        super();
        this.spline = spline;
        if(!(spline instanceof NurbSpline) || (spline.getControlPoints()!= null && spline.getControlPoints().size() > 0)) {
        	this.setMode(Mesh.Mode.Lines);
            this.setBuffer(VertexBuffer.Type.Position, 3, spline.getPositionArrayForMesh(nbSubSegments));
            this.setBuffer(VertexBuffer.Type.Index, 2, spline.getIndicesArrayForMesh(nbSubSegments));//(spline.getControlPoints().size() - 1) * nbSubSegments * 2
            this.updateBound();
            this.updateCounts();
        }
    }

    /**
     * This method returns the length of the curve.
     *
     * @return the length of the curve
     */
    public float getLength() {
        return spline.getTotalLength();
    }
    
}
