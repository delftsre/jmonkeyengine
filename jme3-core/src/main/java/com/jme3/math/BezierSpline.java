package com.jme3.math;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;

public class BezierSpline extends Spline{
	
	public BezierSpline(List<Vector3f> controlPoints, boolean cycle) {
		super(controlPoints, cycle);
	}
	
	public BezierSpline(Vector3f[] controlPoints, boolean cycle) {
		super(Arrays.asList(controlPoints), cycle);
	}

	@Override
	public Vector3f interpolate(float value, int currentControlPoint, Vector3f store) {
		if (store == null) {
            store = new Vector3f();
        }
		FastMath.interpolateBezier(value, controlPoints.get(currentControlPoint), controlPoints.get(currentControlPoint + 1), controlPoints.get(currentControlPoint + 2), controlPoints.get(currentControlPoint + 3), store);
		return store;
	}

	@Override
	public void computeTotalLentgh() {
		prepareTotalLengthComputation();
		computeBezierLength();
	}
	
	/**
     * This method calculates the Bezier curve length.
     */
    private void computeBezierLength() {
    	float l = 0;
        if (controlPoints.size() > 1) {
            for (int i = 0; i < controlPoints.size() - 1; i+=3) {
                l = FastMath.getBezierP1toP2Length(controlPoints.get(i),
                		controlPoints.get(i + 1), controlPoints.get(i + 2), controlPoints.get(i + 3));
                segmentsLength.add(l);
                totalLength += l;
            }
        }
    }

	@Override
	public float[] getPositionArrayForMesh(int nbSubSegments) {
		Vector3f temp = new Vector3f();
		if (nbSubSegments == 0) {
            nbSubSegments = 1;
        }
        int centerPointsAmount = (this.getControlPoints().size() + 2) / 3;

        //calculating vertices
        float[] array = new float[((centerPointsAmount - 1) * nbSubSegments + 1) * 3];
        int currentControlPoint = 0;
        List<Vector3f> controlPoints = this.getControlPoints();
        int lineIndex = 0;
        for (int i = 0; i < centerPointsAmount - 1; ++i) {
            Vector3f vector3f = controlPoints.get(currentControlPoint);
            array[lineIndex++] = vector3f.x;
            array[lineIndex++] = vector3f.y;
            array[lineIndex++] = vector3f.z;
            for (int j = 1; j < nbSubSegments; ++j) {
            	this.interpolate((float) j / nbSubSegments, currentControlPoint, temp);
                array[lineIndex++] = temp.getX();
                array[lineIndex++] = temp.getY();
                array[lineIndex++] = temp.getZ();
            }
            currentControlPoint += 3;
        }
        Vector3f vector3f = controlPoints.get(currentControlPoint);
        array[lineIndex++] = vector3f.x;
        array[lineIndex++] = vector3f.y;
        array[lineIndex++] = vector3f.z;

		return array;
	}

	@Override
	public short[] getIndicesArrayForMesh(int nbSubSegments) {
		if (nbSubSegments == 0) {
            nbSubSegments = 1;
        }
        int centerPointsAmount = (this.getControlPoints().size() + 2) / 3;
        int i = 0, k;
        short[] indices = new short[(centerPointsAmount - 1) * nbSubSegments << 1];
        for (int j = 0; j < (centerPointsAmount - 1) * nbSubSegments; ++j) {
            k = j;
            indices[i++] = (short) k;
            ++k;
            indices[i++] = (short) k;
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
