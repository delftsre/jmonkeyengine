package com.jme3.math;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;

public class NurbSpline extends Spline{
	
    private List<Float> knots;				//knots of NURBS spline
    private float[] weights;				//weights of NURBS spline
    private int basisFunctionDegree;		//degree of NURBS spline basis function (computed automatically)
    
    public NurbSpline(List<Vector4f> controlPoints, List<Float> nurbKnots) {
    	//input data control
    	for(int i=0;i<nurbKnots.size()-1;++i) {
    		if(nurbKnots.get(i)>nurbKnots.get(i+1)) {
    			throw new IllegalArgumentException("The knots values cannot decrease!");
    		}
    	}
    	//storing the data
        this.weights = new float[controlPoints.size()];
        this.knots = nurbKnots;
        this.basisFunctionDegree = nurbKnots.size() - weights.length;
        for(int i=0;i<controlPoints.size();++i) {
        	Vector4f controlPoint = controlPoints.get(i);
        	this.controlPoints.add(new Vector3f(controlPoint.x, controlPoint.y, controlPoint.z));
        	this.weights[i] = controlPoint.w;
        }
        CurveAndSurfaceMath.prepareNurbsKnots(knots, basisFunctionDegree);
        this.computeTotalLentgh();
    }

	@Override
	public Vector3f interpolate(float value, int currentControlPoint, Vector3f store) {
		if (store == null) {
            store = new Vector3f();
        }
		CurveAndSurfaceMath.interpolateNurbs(value, this, store);
		return store;
	}

	@Override
	public void computeTotalLentgh() {
		prepareTotalLengthComputation();
		//TODO: implement
	}
	
	@Override
	public void setCycle(boolean cycle) { }
	
	/**
	 * This method returns the minimum nurb curve knot value. Check the nurb type before calling this method. It the curve is not of a Nurb
	 * type - NPE will be thrown.
	 * @return the minimum nurb curve knot value
	 */
    public float getMinNurbKnot() {
    	return knots.get(basisFunctionDegree - 1);
    }
    
    /**
	 * This method returns the maximum nurb curve knot value. Check the nurb type before calling this method. It the curve is not of a Nurb
	 * type - NPE will be thrown.
	 * @return the maximum nurb curve knot value
	 */
    public float getMaxNurbKnot() {
    	return knots.get(weights.length);
    }

	public List<Float> getKnots() {
		return knots;
	}

	public void setKnots(List<Float> knots) {
		this.knots = knots;
	}

	public float[] getWeights() {
		return weights;
	}

	public void setWeights(float[] weights) {
		this.weights = weights;
	}

	public int getBasisFunctionDegree() {
		return basisFunctionDegree;
	}

	public void setBasisFunctionDegree(int basisFunctionDegree) {
		this.basisFunctionDegree = basisFunctionDegree;
	}

	@Override
	public float[] getPositionArrayForMesh(int nbSubSegments) {
		if(nbSubSegments == 0) {
    		nbSubSegments = this.getControlPoints().size() + 1;
    	} else {
    		nbSubSegments = this.getControlPoints().size() * nbSubSegments + 1;
    	}
        float minKnot = this.getMinNurbKnot();
        float maxKnot = this.getMaxNurbKnot();
        float deltaU = (maxKnot - minKnot) / nbSubSegments;

        float[] array = new float[(nbSubSegments + 1) * 3];

        float u = minKnot;
        Vector3f interpolationResult = new Vector3f();
        for (int i = 0; i < array.length; i += 3) {
            this.interpolate(u, 0, interpolationResult);
            array[i] = interpolationResult.x;
            array[i + 1] = interpolationResult.y;
            array[i + 2] = interpolationResult.z;
            u += deltaU;
        }
		return array;
	}

	@Override
	public short[] getIndicesArrayForMesh(int nbSubSegments) {
		int i = 0;
        short[] indices = new short[nbSubSegments << 1];
        for (int j = 0; j < nbSubSegments; ++j) {
            indices[i++] = (short) j;
            indices[i++] = (short) (j + 1);
        }
		return indices;
	}

	@Override
	public void write(JmeExporter ex) throws IOException {
		OutputCapsule oc = ex.getCapsule(this);
		SplineSavable.write(oc, this);
        oc.writeSavableArrayList((ArrayList<Float>)knots, "knots", null);
        oc.write(weights, "weights", null);
        oc.write(basisFunctionDegree, "basisFunctionDegree", 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(JmeImporter im) throws IOException {
		InputCapsule in = im.getCapsule(this);
		SplineSavable.read(in, this);
        knots = in.readSavableArrayList("knots", null);
        weights = in.readFloatArray("weights", null);
        basisFunctionDegree = in.readInt("basisFunctionDegree", 0);
		
	}

}
