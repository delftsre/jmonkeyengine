package com.jme3.math;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QuaternionTest {

    private float delta = 0.001f; // Delta for float comparison

    @Test
    public void identity() {
        Quaternion q = new Quaternion();

        assertTrue("new quaternion equals identity", q.isIdentity());
        assertTrue("new quaternion equals static identity", q.equals(Quaternion.IDENTITY));

        q.set(1,2,3,4);

        assertFalse("changed quaternion not equals identity", q.isIdentity());

        q.loadIdentity();

        assertTrue("loaded identity", q.isIdentity());
    }

    @Test
    public void average() {
        Quaternion rotateQuarterZ = new Quaternion();
        Quaternion rotateHalfZ = new Quaternion();

        float quarterRotation = (float) (2 * Math.PI / 4); // Rotation of 90 degrees in radians
        float halfRotation = (float) (2 * Math.PI / 2); // Rotation of 180 degrees in radians

        rotateQuarterZ.fromAngles(0, 0, 0);
        rotateHalfZ.fromAngles(halfRotation, 0, 0);

        Quaternion average = new Quaternion();

        average.slerp(rotateHalfZ, rotateQuarterZ, 0.5f);

        float[] averageAngles = average.toAngles(null);

        assertEquals("angle x", averageAngles[0], -quarterRotation, delta);
        assertEquals("angle y", averageAngles[1], 0, delta);
        assertEquals("angle z", averageAngles[2], 0, delta);

        float[] givenAngles = {
            -quarterRotation, 0, 0
        };

        Quaternion expectationFromAngles = new Quaternion(givenAngles);

        float[] expectedAngles = expectationFromAngles.toAngles(null);

        assertEquals("expected angle x", averageAngles[0], expectedAngles[0], delta);
        assertEquals("expected angle y", averageAngles[1], expectedAngles[1], delta);
        assertEquals("expected angle z", averageAngles[2], expectedAngles[2], delta);
    }

    @Test
    public void slerpConstructor() {
        Quaternion rotateQuarterZ = new Quaternion();
        Quaternion rotateHalfZ = new Quaternion();

        rotateQuarterZ.fromAngles(0, 0, 0);
        rotateHalfZ.fromAngles(1, 0, 0);

        Quaternion slerped = new Quaternion();

        slerped.slerp(rotateHalfZ, rotateQuarterZ, 0.5f);

        Quaternion constructed = new Quaternion(rotateQuarterZ, rotateHalfZ, 0.5f);

        assertTrue("slerp should have same functionality as slerp constructor", slerped.equals(constructed));
    }

    @Test
    public void normalization() {
        int x = 10;

        Quaternion q = new Quaternion(x,x,x,x);

        float norm = 4 * (x*x);

        assertEquals("norm should be sum of square of elements", q.norm(), norm, delta);

        q.normalizeLocal();

        assertEquals("norm of normalized quaternion should be 1", q.norm(), 1, delta);
    }

    @Test
    public void rotationMatrices() {
        Vector3f yAxis = new Vector3f(0,1,0);

        float angle = (float) (2 * Math.PI / 4); // Rotation of 90 degrees in radians

        Quaternion q = new Quaternion();

        q.fromAngleAxis(angle, yAxis);

        Matrix3f rotationMatrix3d = new Matrix3f();
        Matrix4f rotationMatrix4d = new Matrix4f();

        q.toRotationMatrix(rotationMatrix3d);
        q.toRotationMatrix(rotationMatrix4d);

        Matrix3f expectedRotationMatrix3d = new Matrix3f();
        Matrix4f expectedRotationMatrix4d = new Matrix4f();

        expectedRotationMatrix3d.fromAngleAxis(angle, yAxis);
        expectedRotationMatrix4d.fromAngleAxis(angle, yAxis);

        assertTrue("90 degree y-axis rotation matrix 3d", rotationMatrix3d.equals(expectedRotationMatrix3d, delta));
    }
}
