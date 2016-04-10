/**
 * 
 */
package com.jme3.scene.shape;

import static com.jme3.math.DataGeneratorForSplineTest.initControlPoints;
import static com.jme3.math.DataGeneratorForSplineTest.initNurbKnots;
import static com.jme3.math.DataGeneratorForSplineTest.initSampleVectorList;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jme3.math.BezierSpline;
import com.jme3.math.CatmullRomSpline;
import com.jme3.math.LinearSpline;
import com.jme3.math.NurbSpline;
import com.jme3.math.Vector3f;
import com.jme3.scene.VertexBuffer;
/**
 * @author Piotr
 *
 */
public class CurveTest {
	
	Curve testCurve;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		testCurve = new Curve();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.jme3.scene.shape.Curve#Curve()}.
	 */
	@Test
	public void testCurve() {
		assertNotNull(testCurve);
	}

	/**
	 * Test method for {@link com.jme3.scene.shape.Curve#Curve(com.jme3.math.Vector3f[], int)}.
	 */
	@Test
	public void testCurveVector3fArrayInt() {
		testCurve = new Curve(new Vector3f[] {new Vector3f(1,1,1), new Vector3f(2,2,2)}, 5);
		assertNotNull(testCurve);
	}
	
	private LinearSpline createLinearSpline() {
//		return new Spline(SplineType.Linear, initSampleVectorList(), 1.0f, false);
		return new LinearSpline(initSampleVectorList(), false);
	}
	
	private CatmullRomSpline createCatmullRomSpline() {
//		return new Spline(SplineType.CatmullRom, initSampleVectorList(), 1.0f, false);
		return new CatmullRomSpline(initSampleVectorList(), 1.0f, false);
	}
	
	private BezierSpline createBezierSpline() {
//		return new Spline(SplineType.Bezier, initSampleVectorList(), 1.0f, false);
		return new BezierSpline(initSampleVectorList(), false);
	}
	
	private NurbSpline createNurbSpline() {
//		return new Spline(initControlPoints(), initNurbKnots());
		return new NurbSpline(initControlPoints(), initNurbKnots());
	}

	/**
	 * Test method for {@link com.jme3.scene.shape.Curve#Curve(com.jme3.math.Spline, int)}.
	 */
	@Test
	public void testCurveSplineInt() {
		assertNull(testCurve.getBuffer(VertexBuffer.Type.Position));
		assertNull(testCurve.getBuffer(VertexBuffer.Type.Index));
		testCurve = new Curve(createLinearSpline(), 5);
		assertNotNull(testCurve.getBuffer(VertexBuffer.Type.Position));
		assertNotNull(testCurve.getBuffer(VertexBuffer.Type.Index));
		testCurve = new Curve(createBezierSpline(), 0);
		assertNotNull(testCurve.getBuffer(VertexBuffer.Type.Position));
		assertNotNull(testCurve.getBuffer(VertexBuffer.Type.Index));
		testCurve = new Curve(createBezierSpline(), 5);
		assertNotNull(testCurve.getBuffer(VertexBuffer.Type.Position));
		assertNotNull(testCurve.getBuffer(VertexBuffer.Type.Index));
		testCurve = new Curve(createCatmullRomSpline(), 5);
		assertNotNull(testCurve.getBuffer(VertexBuffer.Type.Position));
		assertNotNull(testCurve.getBuffer(VertexBuffer.Type.Index));
		testCurve = new Curve(createNurbSpline(), 5);
		assertNotNull(testCurve.getBuffer(VertexBuffer.Type.Position));
		assertNotNull(testCurve.getBuffer(VertexBuffer.Type.Index));
		testCurve = new Curve(createNurbSpline(), 0);
		assertNotNull(testCurve.getBuffer(VertexBuffer.Type.Position));
		assertNotNull(testCurve.getBuffer(VertexBuffer.Type.Index));
	}

	/**
	 * Test method for {@link com.jme3.scene.shape.Curve#getLength()}.
	 */
	@Test
	public void testGetLength() {
		testCurve = new Curve(createLinearSpline(), 5);
		assertTrue(testCurve.getLength()!=0.0f);
		testCurve = new Curve(createBezierSpline(), 5);
		assertTrue(testCurve.getLength()!=0.0f);
		testCurve = new Curve(createCatmullRomSpline(), 5);
		assertTrue(testCurve.getLength()!=0.0f);
		testCurve = new Curve(createNurbSpline(), 5);
		assertTrue(testCurve.getLength()==0.0f);// no function calculating total length of nurb spline
	}

}
