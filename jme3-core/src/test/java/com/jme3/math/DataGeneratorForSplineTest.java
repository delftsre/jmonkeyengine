package com.jme3.math;

import java.util.ArrayList;
import java.util.List;

public class DataGeneratorForSplineTest {

	public static List<Vector3f> initSampleVectorList() {
		List<Vector3f> sampleList = new ArrayList<>();
		sampleList.add(new Vector3f(1,1,1));
		sampleList.add(new Vector3f(2,2,2));
		sampleList.add(new Vector3f(3,3,3));
		sampleList.add(new Vector3f(4,4,4));
		return sampleList;
	}
	
	public static Vector3f[] initSampleVectorArray() {
		return new Vector3f[] {new Vector3f(1,1,1), new Vector3f(2,2,2), new Vector3f(3,3,3), new Vector3f(4,4,4)};
	}
	
	public static List<Vector4f> initControlPoints() {
		List<Vector4f> controlPoints = new ArrayList<>();
		controlPoints.add(new Vector4f(1,1,0,0.1f));
		controlPoints.add(new Vector4f(2,6,0,0.8f));
		controlPoints.add(new Vector4f(3,1,0,0.1f));
		return controlPoints;
	}
	
	public static List<Float> initNurbKnots() {
		List<Float> nurbKnots = new ArrayList<>();
		nurbKnots.add(0.0f);
		nurbKnots.add(1.0f);
		nurbKnots.add(2.0f);
		nurbKnots.add(3.0f);
		return nurbKnots;
	}
	
}
