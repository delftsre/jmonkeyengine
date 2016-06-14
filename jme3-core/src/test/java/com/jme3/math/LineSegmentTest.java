package com.jme3.math;

import org.junit.Test;

/**
 * Verifies that algorithms in {@link LineSegment} are working correctly.
 * 
 * @author Enrique Correa
 */
public class LineSegmentTest {
  @Test
  public void testDistanceSquaredPoint() {
    LineSegment test1 = LineSegmentFactory.create(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0));
    assert test1.distanceSquared(new Vector3f(0, 0, 0)) == 0f;
    assert test1.distanceSquared(new Vector3f(1, 0, 0)) == 0f;
    assert FastMath.approximateEquals(test1.distanceSquared(new Vector3f(2, 0, 0)), 1f);
    assert FastMath.approximateEquals(test1.distanceSquared(new Vector3f(1, 1, 1)), 2f);
  }
  @Test
  public void testDistanceSquaredLineSegment() {
    LineSegment test1 = LineSegmentFactory.create(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0));
    LineSegment test2 = LineSegmentFactory.create(new Vector3f(0, 0, 0), new Vector3f(-1, 0, 0));
    LineSegment test3 = LineSegmentFactory.create(new Vector3f(1, 1, 1), new Vector3f(-1, -1, -1));
    LineSegment test4 = LineSegmentFactory.create(new Vector3f(-1, -1, -1), new Vector3f(-5, -5, -5));
    LineSegment test5 = LineSegmentFactory.create(new Vector3f(1, -5, -5), new Vector3f(1, -1, -1));
    // Parallels, different
    assert test1.distanceSquared(test2) == 0.0f;
    assert test1.distanceSquared(test3) == 0.0f;
    assert FastMath.approximateEquals(test1.distanceSquared(test4), 3f);
    assert FastMath.approximateEquals(test1.distanceSquared(test4), 3f);
  }
}
