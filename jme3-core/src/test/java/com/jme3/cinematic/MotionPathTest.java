package com.jme3.cinematic;

import static com.jme3.math.DataGeneratorForSplineTest.initSampleVectorList;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.material.MatParam;
import com.jme3.material.MaterialDef;
import com.jme3.math.BezierSpline;
import com.jme3.math.LinearSpline;
import com.jme3.math.Spline;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

public class MotionPathTest {

//	@Test
//	public void test() {
//		MotionPath motionPath = getPreparedMotionPathForTests();
//		motionPath.setPathSplineType(SplineType.Linear);
//		assertTrue(motionPath.getSpline().getType().equals(SplineType.Linear));
//		motionPath.setPathSplineType(SplineType.Bezier);
//		assertTrue(motionPath.getSpline().getType().equals(SplineType.Bezier));
//		motionPath.setPathSplineType(SplineType.CatmullRom);
//		assertTrue(motionPath.getSpline().getType().equals(SplineType.CatmullRom));
//	}
	
	@Test
	public void testAfterRefactoring() {
		MotionPath motionPath = getPreparedMotionPathForTests();
		Spline testSpline = new LinearSpline(initSampleVectorList(), false);
		motionPath.setSpline(testSpline);
		assertTrue(motionPath.getSpline() instanceof LinearSpline);
		testSpline = new BezierSpline(initSampleVectorList(), false);
		motionPath.setSpline(testSpline);
		assertTrue(motionPath.getSpline() instanceof BezierSpline);
	}
	
	private MotionPath getPreparedMotionPathForTests() {
		MotionPath motionPath = new MotionPath();
		motionPath.getSpline().addControlPoint(new Vector3f(0,0,0));
		motionPath.getSpline().addControlPoint(new Vector3f(1,1,1));
		motionPath.getSpline().addControlPoint(new Vector3f(2,2,2));
		motionPath.getSpline().addControlPoint(new Vector3f(3,3,3));
		AssetManager assetManager = Mockito.mock(AssetManager.class);
		MaterialDef materialDef = Mockito.mock(MaterialDef.class);
		Mockito.when(materialDef.getMaterialParam("Color")).thenReturn(new MatParam());
		Mockito.when(assetManager.loadAsset(new AssetKey<>("Common/MatDefs/Misc/Unshaded.j3md"))).thenReturn(materialDef);
		motionPath.enableDebugShape(assetManager, new Node());
		return motionPath;
	}

}
