package com.jme3.math;

import static com.jme3.math.DataGeneratorForSplineTest.initControlPoints;
import static com.jme3.math.DataGeneratorForSplineTest.initNurbKnots;
import static com.jme3.math.DataGeneratorForSplineTest.initSampleVectorArray;
import static com.jme3.math.DataGeneratorForSplineTest.initSampleVectorList;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;

public class SplineTest {
	
	Spline spline;
	float tension = 0.3f;
	boolean cycle = false;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
//		spline = new Spline();
		spline = new LinearSpline(initSampleVectorList(), cycle);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testConstructors() {
		List<Vector3f> sampleList = initSampleVectorList();
		spline = new LinearSpline(sampleList, cycle);
		splineProperties(tension, cycle);
		spline = null;
		spline = new CatmullRomSpline(sampleList, tension, cycle);		
		splineProperties(tension, cycle);
		assertNotNull(((CatmullRomSpline)spline).getCRcontrolPoints());
		spline = null;
		spline = new BezierSpline(sampleList, cycle);
		splineProperties(tension, cycle);
		spline = null;
		spline = new BezierSpline(initSampleVectorArray(), cycle);
		splineProperties(tension, cycle);
		Spline linearSpline = new LinearSpline(spline);
		assertTrue(linearSpline.isCycle() == spline.isCycle());
		assertTrue(linearSpline.getControlPoints().equals(spline.getControlPoints()));
		Spline catmullRomSpline = new CatmullRomSpline(linearSpline);
		assertTrue(catmullRomSpline.isCycle() == linearSpline.isCycle());
		assertTrue(catmullRomSpline.getControlPoints().equals(linearSpline.getControlPoints()));
		catmullRomSpline = null;
		catmullRomSpline = new CatmullRomSpline(initSampleVectorArray(), cycle);
		assertNotNull(catmullRomSpline);
	}
	
	@Test(expected=AssertionError.class)
	public void testNurbConstructor() {
		spline = new NurbSpline(initControlPoints(), initNurbKnots());
		splineProperties(0.0f, spline.isCycle());
		assertNotNull(((NurbSpline)spline).getKnots());
		assertNotNull(((NurbSpline)spline).getWeights());
		assertTrue(((NurbSpline)spline).getKnots().size()-((NurbSpline)spline).getWeights().length == ((NurbSpline)spline).getBasisFunctionDegree());
	}
	
	private void splineProperties(float tension, boolean cycle) {
		assertNotNull(spline);
		assertTrue(spline.getControlPoints().size() > 0);
		assertNotNull(spline.getSegmentsLength());
		assertTrue(spline.getSegmentsLength().size() > 0);
		assertTrue(spline.getTotalLength() > 0);
		assertTrue(spline.isCycle() == cycle);
	}
	
	@Test
	public void testSetters() {
		spline = new LinearSpline(initSampleVectorArray(), false);
		spline.setCycle(true);
		spline.setControlPoints(null);
		spline.setSegmentsLength(null);
		spline.setTotalLength(0);
		assertNull(spline.getControlPoints());
		assertNull(spline.getSegmentsLength());
		assertTrue(spline.isCycle());
		assertTrue(spline.getTotalLength()==0);
		NurbSpline spline = new NurbSpline(initControlPoints(), initNurbKnots());
		spline.setBasisFunctionDegree(0);
		spline.setKnots(null);
		spline.setWeights(null);
		assertNull(spline.getKnots());
		assertNull(spline.getWeights());
		assertTrue(spline.getBasisFunctionDegree()==0);
		CatmullRomSpline catmullRomSpline = new CatmullRomSpline(initSampleVectorList(), tension, cycle);
		catmullRomSpline.setCRcontrolPoints(null);
		assertNull(catmullRomSpline.getCRcontrolPoints());
	}
	
	/**
	 * Test method for {@link com.jme3.math.Spline#Spline(com.jme3.math.Spline.SplineType, com.jme3.math.Vector3f[], float, boolean)}.
	 */
	@Test
	public void splineSplineTypeVector3fArrayFloatBoolean() {
		spline = new LinearSpline(initSampleVectorArray(), false);
		assertNotNull(spline);
		assertTrue(spline instanceof LinearSpline);
		assertTrue(spline.isCycle() == false);
	}

	/**
	 * Test method for {@link com.jme3.math.Spline#Spline(com.jme3.math.Spline.SplineType, java.util.List, float, boolean)}.
	 */
	@Test
	public void splineSplineTypeListOfVector3fFloatBoolean() {
		List<Vector3f> sampleList = initSampleVectorList();
		spline = new LinearSpline(sampleList, false);
		assertNotNull(spline);
		assertTrue(spline instanceof LinearSpline);
		assertTrue(spline.isCycle() == false);
	}

	/**
	 * Test method for {@link com.jme3.math.Spline#Spline(java.util.List, java.util.List)}.
	 */
	@Test(expected = IllegalArgumentException.class)
	public void splineListOfVector4fListOfFloat() {
		List<Vector4f> controlPoints = initControlPoints();
		List<Float> nurbKnots = initNurbKnots();
		spline = new NurbSpline(controlPoints, nurbKnots);
		assertNotNull(spline);
		assertTrue(spline instanceof NurbSpline);
		nurbKnots.add(2.0f);
		spline = new NurbSpline(controlPoints, nurbKnots);
	}

	/**
	 * Test method for {@link com.jme3.math.Spline#addControlPoint(com.jme3.math.Vector3f)}.
	 */
	@Test
	public void testAddControlPoint() {
		List<Vector3f> sampleList = initSampleVectorList();
		spline = new LinearSpline(sampleList, true);
		spline.addControlPoint(new Vector3f());
		assertTrue(spline.getControlPoints().contains(new Vector3f()));
		spline.clearControlPoints();
		spline.addControlPoint(new Vector3f());
		assertTrue(spline.getControlPoints().size()==1);
		spline.addControlPoint(new Vector3f(1,1,1));
		assertTrue(spline.getTotalLength() > 0);
	}

	/**
	 * Test method for {@link com.jme3.math.Spline#removeControlPoint(com.jme3.math.Vector3f)}.
	 */
	@Test
	public void testOperationsOverControlPoint() {
		spline = new NurbSpline(initControlPoints(), initNurbKnots());
		spline.addControlPoint(new Vector3f());
		assertTrue(spline.getControlPoints().contains(new Vector3f()));
		spline.removeControlPoint(new Vector3f());
		assertFalse(spline.getControlPoints().contains(new Vector3f()));
		spline.clearControlPoints();
		assertTrue(spline.getControlPoints().isEmpty());
	}

	/**
	 * Test method for {@link com.jme3.math.Spline#interpolate(float, int, com.jme3.math.Vector3f)}.
	 */
	@Test
	public void testLinearInterpolate() {
		spline = new LinearSpline(initSampleVectorList(), false);
		Vector3f result = spline.interpolate(0.3f, 0, null);
		assertTrue(result.equals(new Vector3f(1.3f, 1.3f, 1.3f)));
	}
	
	@Test
	public void testCatmullRomInterpolate() {
		spline = new CatmullRomSpline(initSampleVectorList(), 1.0f, false);
		Vector3f result = spline.interpolate(0.3f, 0, null);
		assertTrue(result.equals(new Vector3f(1.3840001f, 1.3840001f, 1.3840001f)));
	}
	
	@Test
	public void testBezierInterpolate() {
		spline = new BezierSpline(initSampleVectorList(), false);
		Vector3f result = spline.interpolate(0.3f, 0, null);
		assertTrue(result.equals(new Vector3f(1.9f, 1.9f, 1.9f)));
	}
	
	@Test
	public void testNurbInterpolate() {
		spline = new NurbSpline(initControlPoints(), initNurbKnots());
		Vector3f result = spline.interpolate(0.3f, 0, null);
		assertTrue(result.equals(new Vector3f(1.0f, 1.0f, 0.0f)));
	}

	/**
	 * Test method for {@link com.jme3.math.Spline#setCurveTension(float)}.
	 */
	@Test
	public void testSetCurveTension() {
		spline = new CatmullRomSpline(initSampleVectorList(), 1.0f, false);
		((CatmullRomSpline)spline).setCurveTension(3.5f);
		assertTrue(((CatmullRomSpline)spline).getCurveTension() == 3.5f);
	}

	/**
	 * Test method for {@link com.jme3.math.Spline#setCycle(boolean)}.
	 */
	@Test
	public void testSetCycle() {
		spline = new NurbSpline(initControlPoints(), initNurbKnots());
		spline.setCycle(true);
		assertFalse(spline.isCycle());
		spline = new CatmullRomSpline(initSampleVectorList(), 1.0f, false);
		spline.setCycle(true);
		assertTrue(spline.isCycle());
		spline = new CatmullRomSpline(initSampleVectorList(), 1.0f, true);
		spline.setCycle(false);
		assertFalse(spline.isCycle());
		spline = new CatmullRomSpline(initSampleVectorList(), 1.0f, true);
		spline.setCycle(true);
		assertTrue(spline.isCycle());
		spline = new CatmullRomSpline(initSampleVectorList(), 1.0f, false);
		spline.setCycle(false);
		assertFalse(spline.isCycle());
		spline.clearControlPoints();
		spline.setCycle(true);
		assertTrue(spline.isCycle());
	}

	/**
	 * Test method for {@link com.jme3.math.Spline#getSegmentsLength()}.
	 */
	@Test
	public void testGetSegmentsLength() {
		spline = new LinearSpline(initSampleVectorList(), false);
		assertTrue(spline.getSegmentsLength().size() != 0);
	}
	
	/**
	 * Test method for {@link com.jme3.math.Spline#getMinNurbKnot()}.
	 */
	@Test
	public void testGetMinNurbKnot() {
		NurbSpline spline = new NurbSpline(initControlPoints(), initNurbKnots());
		assertTrue(spline.getMinNurbKnot() == spline.getKnots().get(spline.getBasisFunctionDegree() - 1));
	}

	/**
	 * Test method for {@link com.jme3.math.Spline#getMaxNurbKnot()}.
	 */
	@Test
	public void testGetMaxNurbKnot() {
		NurbSpline spline = new NurbSpline(initControlPoints(), initNurbKnots());
		assertTrue(spline.getMaxNurbKnot() == spline.getKnots().get(spline.getWeights().length));
	}

	/**
	 * Test method for {@link com.jme3.math.Spline#write(com.jme3.export.JmeExporter)}.
	 */
	@Test
	public void testWriteLinearSpline() throws IOException{
		JmeExporter jmeExporter = Mockito.mock(JmeExporter.class);
		OutputCapsule outputCapsule = Mockito.mock(OutputCapsule.class);
		Mockito.when(jmeExporter.getCapsule(Mockito.any())).thenReturn(outputCapsule);
		spline = new LinearSpline(initSampleVectorList(), false);
		spline.write(jmeExporter);
		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.anyFloat(), Mockito.anyString(), Mockito.anyFloat());
		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean());
		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.any(float[].class), Mockito.anyString(), Mockito.any(float[].class));
		Mockito.verify(outputCapsule, Mockito.times(1)).writeSavableArrayList(Mockito.any(ArrayList.class), Mockito.anyString(), Mockito.any(ArrayList.class));
	}

	@Test
	public void testWriteBezierSpline() throws IOException{
		JmeExporter jmeExporter = Mockito.mock(JmeExporter.class);
		OutputCapsule outputCapsule = Mockito.mock(OutputCapsule.class);
		Mockito.when(jmeExporter.getCapsule(Mockito.any())).thenReturn(outputCapsule);
		spline = new BezierSpline(initSampleVectorList(), false);
		spline.write(jmeExporter);
		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.anyFloat(), Mockito.anyString(), Mockito.anyFloat());
		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean());
		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.any(float[].class), Mockito.anyString(), Mockito.any(float[].class));
		Mockito.verify(outputCapsule, Mockito.times(1)).writeSavableArrayList(Mockito.any(ArrayList.class), Mockito.anyString(), Mockito.any(ArrayList.class));
	}
	
	@Test
	public void testWriteCatmullRomSpline() throws IOException{
		JmeExporter jmeExporter = Mockito.mock(JmeExporter.class);
		OutputCapsule outputCapsule = Mockito.mock(OutputCapsule.class);
		Mockito.when(jmeExporter.getCapsule(Mockito.any())).thenReturn(outputCapsule);
		spline = new CatmullRomSpline(initSampleVectorList(), false);
		spline.write(jmeExporter);
		Mockito.verify(outputCapsule, Mockito.times(2)).write(Mockito.anyFloat(), Mockito.anyString(), Mockito.anyFloat());
		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean());
		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.any(float[].class), Mockito.anyString(), Mockito.any(float[].class));
		Mockito.verify(outputCapsule, Mockito.times(2)).writeSavableArrayList(Mockito.any(ArrayList.class), Mockito.anyString(), Mockito.any(ArrayList.class));
	}
	
	@Test
	public void testWriteNurbSpline() throws IOException{
		JmeExporter jmeExporter = Mockito.mock(JmeExporter.class);
		OutputCapsule outputCapsule = Mockito.mock(OutputCapsule.class);
		Mockito.when(jmeExporter.getCapsule(Mockito.any())).thenReturn(outputCapsule);
		spline = new NurbSpline(initControlPoints(),initNurbKnots());
		spline.write(jmeExporter);
		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.anyFloat(), Mockito.anyString(), Mockito.anyFloat());
		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean());
		Mockito.verify(outputCapsule, Mockito.times(2)).write(Mockito.any(float[].class), Mockito.anyString(), Mockito.any(float[].class));
		Mockito.verify(outputCapsule, Mockito.times(2)).writeSavableArrayList(Mockito.any(ArrayList.class), Mockito.anyString(), Mockito.any(ArrayList.class));
	}

	/**
	 * Test method for {@link com.jme3.math.Spline#read(com.jme3.export.JmeImporter)}.
	 * @throws IOException 
	 */
	@Test
	public void testReadLinearSpline() throws IOException {
		JmeImporter jmeImporter = Mockito.mock(JmeImporter.class);
		InputCapsule inputCapsule = Mockito.mock(InputCapsule.class);
		Mockito.when(jmeImporter.getCapsule(Mockito.any())).thenReturn(inputCapsule);
		spline = new LinearSpline(initSampleVectorList(), false);
		Mockito.when(inputCapsule.readFloatArray("segmentsLength", null)).thenReturn(new float[]{1.2f,2.3f});
		Mockito.when(inputCapsule.readSavableArrayList(Mockito.anyString(), Mockito.any(ArrayList.class))).thenReturn(null);
		spline.read(jmeImporter);
		Mockito.verify(inputCapsule, Mockito.times(1)).readFloat(Mockito.anyString(), Mockito.anyFloat());
		Mockito.verify(inputCapsule, Mockito.times(1)).readBoolean(Mockito.anyString(), Mockito.anyBoolean());
//		Mockito.verify(inputCapsule, Mockito.times(1)).readInt(Mockito.anyString(), Mockito.anyInt());
		Mockito.verify(inputCapsule, Mockito.times(1)).readFloatArray(Mockito.anyString(), Mockito.any(float[].class));
		Mockito.verify(inputCapsule, Mockito.times(1)).readSavableArrayList(Mockito.anyString(), Mockito.any(ArrayList.class));
		assertNull(spline.getControlPoints());
		assertTrue(spline.getTotalLength()==0);
		assertTrue(spline.getSegmentsLength().size()==2);
		assertFalse(spline.isCycle());
	}
	
	@Test
	public void testReadBezierSpline() throws IOException {
		JmeImporter jmeImporter = Mockito.mock(JmeImporter.class);
		InputCapsule inputCapsule = Mockito.mock(InputCapsule.class);
		Mockito.when(jmeImporter.getCapsule(Mockito.any())).thenReturn(inputCapsule);
		spline = new BezierSpline(initSampleVectorList(), false);
		Mockito.when(inputCapsule.readFloatArray("segmentsLength", null)).thenReturn(new float[]{1.2f,2.3f});
		Mockito.when(inputCapsule.readSavableArrayList(Mockito.anyString(), Mockito.any(ArrayList.class))).thenReturn(null);
		spline.read(jmeImporter);
		Mockito.verify(inputCapsule, Mockito.times(1)).readFloat(Mockito.anyString(), Mockito.anyFloat());
		Mockito.verify(inputCapsule, Mockito.times(1)).readBoolean(Mockito.anyString(), Mockito.anyBoolean());
		Mockito.verify(inputCapsule, Mockito.times(1)).readFloatArray(Mockito.anyString(), Mockito.any(float[].class));
		Mockito.verify(inputCapsule, Mockito.times(1)).readSavableArrayList(Mockito.anyString(), Mockito.any(ArrayList.class));
		assertNull(spline.getControlPoints());
		assertTrue(spline.getTotalLength()==0);
		assertTrue(spline.getSegmentsLength().size()==2);
		assertFalse(spline.isCycle());
	}
	
	@Test
	public void testReadCatmullRomSpline() throws IOException {
		JmeImporter jmeImporter = Mockito.mock(JmeImporter.class);
		InputCapsule inputCapsule = Mockito.mock(InputCapsule.class);
		Mockito.when(jmeImporter.getCapsule(Mockito.any())).thenReturn(inputCapsule);
		CatmullRomSpline spline = new CatmullRomSpline(initSampleVectorList(), false);
		Mockito.when(inputCapsule.readSavableArrayList(Mockito.anyString(), Mockito.any(ArrayList.class))).thenReturn(null);
		Mockito.when(inputCapsule.readFloatArray("segmentsLength", null)).thenReturn(new float[]{1.2f,2.3f});
		Mockito.when(inputCapsule.readFloat("curveTension", 0.5f)).thenReturn(0.5f);
		spline.read(jmeImporter);
		Mockito.verify(inputCapsule, Mockito.times(2)).readFloat(Mockito.anyString(), Mockito.anyFloat());
		Mockito.verify(inputCapsule, Mockito.times(1)).readBoolean(Mockito.anyString(), Mockito.anyBoolean());
		Mockito.verify(inputCapsule, Mockito.times(1)).readFloatArray(Mockito.anyString(), Mockito.any(float[].class));
		Mockito.verify(inputCapsule, Mockito.times(2)).readSavableArrayList(Mockito.anyString(), Mockito.any(ArrayList.class));
		assertNull(spline.getControlPoints());
		assertTrue(spline.getTotalLength()==0);
		assertTrue(spline.getSegmentsLength().size()==2);
		assertFalse(spline.isCycle());
		assertTrue(spline.getCurveTension() == 0.5f);
		assertNull(spline.getCRcontrolPoints());
	}
	
	@Test
	public void testReadNurbSpline() throws IOException {
		JmeImporter jmeImporter = Mockito.mock(JmeImporter.class);
		InputCapsule inputCapsule = Mockito.mock(InputCapsule.class);
		Mockito.when(jmeImporter.getCapsule(Mockito.any())).thenReturn(inputCapsule);
		NurbSpline spline = new NurbSpline(initControlPoints(), initNurbKnots());
		Mockito.when(inputCapsule.readFloatArray("segmentsLength", null)).thenReturn(new float[]{1.2f,2.3f});
		Mockito.when(inputCapsule.readSavableArrayList(Mockito.anyString(), Mockito.any(ArrayList.class))).thenReturn(null);
		spline.read(jmeImporter);
		Mockito.verify(inputCapsule, Mockito.times(1)).readFloat(Mockito.anyString(), Mockito.anyFloat());
		Mockito.verify(inputCapsule, Mockito.times(1)).readBoolean(Mockito.anyString(), Mockito.anyBoolean());
		Mockito.verify(inputCapsule, Mockito.times(2)).readFloatArray(Mockito.anyString(), Mockito.any(float[].class));
		Mockito.verify(inputCapsule, Mockito.times(2)).readSavableArrayList(Mockito.anyString(), Mockito.any(ArrayList.class));
		Mockito.verify(inputCapsule, Mockito.times(1)).readInt(Mockito.anyString(), Mockito.anyInt());
		assertNull(spline.getControlPoints());
		assertTrue(spline.getTotalLength()==0);
		assertTrue(spline.getSegmentsLength().size()==2);
		assertFalse(spline.isCycle());
		assertNull(spline.getKnots());
		assertTrue(spline.getBasisFunctionDegree() == 0);
	}
	
	/*
	 * TESTS BEFORE REFACTORING
	 */
	
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#Spline()}.
//	 */
//	@Test
//	public void testSpline() {
//		assertNotNull(spline);
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#Spline(com.jme3.math.Spline.SplineType, com.jme3.math.Vector3f[], float, boolean)}.
//	 */
//	@Test(expected = IllegalArgumentException.class)
//	public void testSplineSplineTypeVector3fArrayFloatBoolean() {
//		Vector3f[] sampleList = new Vector3f[]{new Vector3f(1,1,1), new Vector3f(2,2,2)};
//		spline = new Spline(SplineType.Linear, sampleList, 1.0f, false);
//		assertNotNull(spline);
//		assertTrue(spline.getCurveTension() == 1.0f);
//		assertTrue(spline.getType().equals(SplineType.Linear));
//		assertTrue(spline.isCycle() == false);
//		spline = null;
//		spline = new Spline(SplineType.Nurb, sampleList, 1.0f, false);
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#Spline(com.jme3.math.Spline.SplineType, java.util.List, float, boolean)}.
//	 */
//	@Test(expected = IllegalArgumentException.class)
//	public void testSplineSplineTypeListOfVector3fFloatBoolean() {
//		List<Vector3f> sampleList = initSampleVectorList();
//		spline = new Spline(SplineType.Linear, sampleList, 1.0f, false);
//		assertNotNull(spline);
//		assertTrue(spline.getCurveTension() == 1.0f);
//		assertTrue(spline.getType().equals(SplineType.Linear));
//		assertTrue(spline.isCycle() == false);
//		spline = new Spline(SplineType.Nurb, sampleList, 1.0f, false);
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#Spline(java.util.List, java.util.List)}.
//	 */
//	@Test(expected = IllegalArgumentException.class)
//	public void testSplineListOfVector4fListOfFloat() {
//		List<Vector4f> controlPoints = initControlPoints();
//		List<Float> nurbKnots = initNurbKnots();
//		spline = new Spline(controlPoints, nurbKnots);
//		assertNotNull(spline);
//		assertTrue(spline.getType().equals(SplineType.Nurb));
//		nurbKnots.add(2.0f);
//		spline = new Spline(controlPoints, nurbKnots);
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#addControlPoint(com.jme3.math.Vector3f)}.
//	 */
//	@Test
//	public void testAddControlPoint() {
//		List<Vector3f> sampleList = initSampleVectorList();
//		spline = new Spline(SplineType.Linear, sampleList, 1.0f, true);
//		spline.addControlPoint(new Vector3f());
//		assertTrue(spline.getControlPoints().contains(new Vector3f()));
//		spline.clearControlPoints();
//		spline.addControlPoint(new Vector3f());
//		assertTrue(spline.getControlPoints().size()==1);
//		spline.addControlPoint(new Vector3f(1,1,1));
//		assertTrue(spline.getTotalLength() > 0);
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#removeControlPoint(com.jme3.math.Vector3f)}.
//	 */
//	@Test
//	public void testOperationsOverControlPoint() {
//		spline = new Spline(initControlPoints(), initNurbKnots());
//		spline.addControlPoint(new Vector3f());
//		assertTrue(spline.getControlPoints().contains(new Vector3f()));
//		spline.removeControlPoint(new Vector3f());
//		assertFalse(spline.getControlPoints().contains(new Vector3f()));
//		spline.clearControlPoints();
//		assertTrue(spline.getControlPoints().isEmpty());
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#interpolate(float, int, com.jme3.math.Vector3f)}.
//	 */
//	@Test
//	public void testLinearInterpolate() {
//		spline = new Spline(SplineType.Linear, initSampleVectorList(), 1.0f, false);
//		Vector3f result = spline.interpolate(0.3f, 0, null);
//		assertTrue(result.equals(new Vector3f(1.3f, 1.3f, 1.3f)));
//	}
//	
//	@Test
//	public void testCatmullRomInterpolate() {
//		spline = new Spline(SplineType.CatmullRom, initSampleVectorList(), 1.0f, false);
//		Vector3f result = spline.interpolate(0.3f, 0, null);
//		assertTrue(result.equals(new Vector3f(1.3840001f, 1.3840001f, 1.3840001f)));
//	}
//	
//	@Test
//	public void testBezierInterpolate() {
//		spline = new Spline(SplineType.Bezier, initSampleVectorList(), 1.0f, false);
//		Vector3f result = spline.interpolate(0.3f, 0, null);
//		assertTrue(result.equals(new Vector3f(1.9f, 1.9f, 1.9f)));
//	}
//	
//	@Test
//	public void testNurbInterpolate() {
//		spline = new Spline(initControlPoints(), initNurbKnots());
//		Vector3f result = spline.interpolate(0.3f, 0, null);
//		assertTrue(result.equals(new Vector3f(1.0f, 1.0f, 0.0f)));
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#setCurveTension(float)}.
//	 */
//	@Test
//	public void testSetCurveTension() {
//		spline = new Spline(SplineType.CatmullRom, initSampleVectorList(), 1.0f, false);
//		spline.setCurveTension(3.5f);
//		assertTrue(spline.getCurveTension() == 3.5f);
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#setCycle(boolean)}.
//	 */
//	@Test
//	public void testSetCycle() {
//		spline = new Spline(initControlPoints(), initNurbKnots());
//		spline.setCycle(true);
//		assertFalse(spline.isCycle());
//		spline = new Spline(SplineType.CatmullRom, initSampleVectorList(), 1.0f, false);
//		spline.setCycle(true);
//		assertTrue(spline.isCycle());
//		spline = new Spline(SplineType.CatmullRom, initSampleVectorList(), 1.0f, true);
//		spline.setCycle(false);
//		assertFalse(spline.isCycle());
//		spline = new Spline(SplineType.CatmullRom, initSampleVectorList(), 1.0f, true);
//		spline.setCycle(true);
//		assertTrue(spline.isCycle());
//		spline = new Spline(SplineType.CatmullRom, initSampleVectorList(), 1.0f, false);
//		spline.setCycle(false);
//		assertFalse(spline.isCycle());
//		spline.clearControlPoints();
//		spline.setCycle(true);
//		assertTrue(spline.isCycle());
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#setType(com.jme3.math.Spline.SplineType)}.
//	 */
//	@Test
//	public void testSetType() {
//		spline = new Spline(SplineType.Bezier, initSampleVectorList(), 1.0f, false);
//		float prevoiusLength = spline.getTotalLength();
//		spline.setType(SplineType.Linear);
//		assertFalse(spline.getTotalLength()==prevoiusLength);
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#getSegmentsLength()}.
//	 */
//	@Test
//	public void testGetSegmentsLength() {
//		assertNull(spline.getSegmentsLength());
//		spline = new Spline(SplineType.Linear, initSampleVectorList(), 1.0f, false);
//		assertTrue(spline.getSegmentsLength().size() != 0);
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#getMinNurbKnot()}.
//	 */
//	@Test
//	public void testGetMinNurbKnot() {
//		spline = new Spline(initControlPoints(), initNurbKnots());
//		assertTrue(spline.getMinNurbKnot() == spline.getKnots().get(spline.getBasisFunctionDegree() - 1));
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#getMaxNurbKnot()}.
//	 */
//	@Test
//	public void testGetMaxNurbKnot() {
//		spline = new Spline(initControlPoints(), initNurbKnots());
//		assertTrue(spline.getMaxNurbKnot() == spline.getKnots().get(spline.getWeights().length));
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#write(com.jme3.export.JmeExporter)}.
//	 */
//	@Test
//	public void testWrite() throws IOException{
//		JmeExporter jmeExporter = Mockito.mock(JmeExporter.class);
//		OutputCapsule outputCapsule = Mockito.mock(OutputCapsule.class);
//		Mockito.when(jmeExporter.getCapsule(Mockito.any())).thenReturn(outputCapsule);
//		spline = new Spline(SplineType.Linear, initSampleVectorList(), 1.0f, false);
//		spline.write(jmeExporter);
//		Mockito.verify(outputCapsule, Mockito.times(2)).write(Mockito.anyFloat(), Mockito.anyString(), Mockito.anyFloat());
//		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.anyBoolean(), Mockito.anyString(), Mockito.anyBoolean());
//		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.any(SplineType.class), Mockito.anyString(), Mockito.any(SplineType.class));
//		Mockito.verify(outputCapsule, Mockito.times(1)).write(Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt());
//		Mockito.verify(outputCapsule, Mockito.times(2)).write(Mockito.any(float[].class), Mockito.anyString(), Mockito.any(float[].class));
//		Mockito.verify(outputCapsule, Mockito.times(3)).writeSavableArrayList(Mockito.any(ArrayList.class), Mockito.anyString(), Mockito.any(ArrayList.class));
//	}
//
//	/**
//	 * Test method for {@link com.jme3.math.Spline#read(com.jme3.export.JmeImporter)}.
//	 * @throws IOException 
//	 */
//	@SuppressWarnings("unchecked")
//	@Test
//	public void testRead() throws IOException {
//		JmeImporter jmeImporter = Mockito.mock(JmeImporter.class);
//		InputCapsule inputCapsule = Mockito.mock(InputCapsule.class);
//		Mockito.when(jmeImporter.getCapsule(Mockito.any())).thenReturn(inputCapsule);
//		spline = new Spline(SplineType.Linear, initSampleVectorList(), 1.0f, false);
//		Mockito.when(inputCapsule.readFloatArray("segmentsLength", null)).thenReturn(new float[]{1.2f,2.3f});
//		Mockito.when(inputCapsule.readSavableArrayList(Mockito.anyString(), Mockito.any(ArrayList.class))).thenReturn(null);
//		Mockito.when(inputCapsule.readEnum(Mockito.anyString(), Mockito.any(Class.class), Mockito.any(SplineType.class))).thenReturn(SplineType.CatmullRom);
//		Mockito.when(inputCapsule.readFloat("curveTension", 0.5f)).thenReturn(0.5f);
//		spline.read(jmeImporter);
//		Mockito.verify(inputCapsule, Mockito.times(2)).readFloat(Mockito.anyString(), Mockito.anyFloat());
//		Mockito.verify(inputCapsule, Mockito.times(1)).readBoolean(Mockito.anyString(), Mockito.anyBoolean());
//		Mockito.verify(inputCapsule, Mockito.times(1)).readEnum(Mockito.anyString(), Mockito.any(Class.class), Mockito.any(SplineType.class));
//		Mockito.verify(inputCapsule, Mockito.times(1)).readInt(Mockito.anyString(), Mockito.anyInt());
//		Mockito.verify(inputCapsule, Mockito.times(2)).readFloatArray(Mockito.anyString(), Mockito.any(float[].class));
//		Mockito.verify(inputCapsule, Mockito.times(3)).readSavableArrayList(Mockito.anyString(), Mockito.any(ArrayList.class));
//		assertNull(spline.getControlPoints());
//		assertNull(spline.getKnots());
//		assertTrue(spline.getTotalLength()==0);
//		assertTrue(spline.getSegmentsLength().size()==2);
//		assertTrue(spline.getType().equals(SplineType.CatmullRom));
//		assertTrue(spline.getCurveTension() == 0.5f);
//		assertNull(spline.getWeights());
//		assertTrue(spline.getBasisFunctionDegree() == 0);
//		assertFalse(spline.isCycle());
//	}

}
