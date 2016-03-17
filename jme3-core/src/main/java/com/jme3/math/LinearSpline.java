package com.jme3.math;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;

public class LinearSpline extends Spline{
	
	public LinearSpline(Spline spline) { 
		super(spline);
	}
	
	public LinearSpline(List<Vector3f> controlPoints, boolean cycle) {
		super(controlPoints, cycle);
	}
	
	public LinearSpline(Vector3f[] controlPoints, boolean cycle) {
		super(Arrays.asList(controlPoints), cycle);
	}

	@Override
	public Vector3f interpolate(float value, int currentControlPoint, Vector3f store) {
		if (store == null) {
            store = new Vector3f();
        }
		FastMath.interpolateLinear(value, controlPoints.get(currentControlPoint), controlPoints.get(currentControlPoint + 1), store);
		return store;
	}

	@Override
	public void computeTotalLentgh() {
		prepareTotalLengthComputation();
		float l = 0;
		if (controlPoints.size() > 1) {
            for (int i = 0; i < controlPoints.size() - 1; i++) {
                l = controlPoints.get(i + 1).subtract(controlPoints.get(i)).length();
                segmentsLength.add(l);
                totalLength += l;
            }
        }
	}

	@Override
	public float[] getPositionArrayForMesh(int nbSubSegments) {
        float[] array = new float[this.getControlPoints().size() * 3];
        int i = 0;
        for (Iterator<Vector3f> it = this.getControlPoints().iterator(); it.hasNext();) {
            Vector3f vector3f = it.next();
            array[i] = vector3f.getX();
            i++;
            array[i] = vector3f.getY();
            i++;
            array[i] = vector3f.getZ();
            i++;
        }
		return array;
	}

	@Override
	public short[] getIndicesArrayForMesh(int nbSubSegments) {
        short[] indices = new short[(this.getControlPoints().size() - 1) * 2];
        int cpt = 0;
        int k;
        int j = 0;
        for (Iterator<Vector3f> it = this.getControlPoints().iterator(); it.hasNext();) {
            it.next();
            if (it.hasNext()) {
                k = j;
                indices[cpt] = (short) k;
                cpt++;
                k++;
                indices[cpt] = (short) k;
                cpt++;
                j++;
            }
        }
		return indices;
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
		OutputCapsule oc = ex.getCapsule(this);
		SplineSavable.write(oc, this);
	}

	@Override
	public void read(JmeImporter im) throws IOException {
		InputCapsule in = im.getCapsule(this);
		SplineSavable.read(in, this);
	}

}
