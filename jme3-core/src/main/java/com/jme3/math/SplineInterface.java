package com.jme3.math;

public interface SplineInterface {
	public void addControlPoint(Vector3f controlPoint);
	public void removeControlPoint(Vector3f controlPoint);
	public void clearControlPoints();
	public Vector3f interpolate(float value, int currentControlPoint, Vector3f store);
	public void computeTotalLentgh();
	public float[] getPositionArrayForMesh(int nbSubSegments);
	public short[] getIndicesArrayForMesh(int nbSubSegments);
}
