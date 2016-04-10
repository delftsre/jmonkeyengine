package com.jme3.math;

import static com.jme3.math.DataGeneratorForSplineTest.initControlPoints;
import static com.jme3.math.DataGeneratorForSplineTest.initNurbKnots;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CurveAndSurfaceMathTest {

//	@Test
//	public void testInterpolateNurbsFloatNurbSplineVector3f() {
//		Spline nurbSpline = new Spline(initControlPoints(), initNurbKnots());
//		Vector3f store = new Vector3f();
//		CurveAndSurfaceMath.interpolateNurbs(0.3f, nurbSpline, store);
//		assertFalse(new Vector3f().equals(store));
//	}
	
	@Test
	public void testInterpolateNurbsFloatNurbSplineVector3f() {
		NurbSpline nurbSpline = new NurbSpline(initControlPoints(), initNurbKnots());
		Vector3f store = new Vector3f();
		CurveAndSurfaceMath.interpolateNurbs(0.3f, nurbSpline, store);
		assertFalse(new Vector3f().equals(store));
	}

}
