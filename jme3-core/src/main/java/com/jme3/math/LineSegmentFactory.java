package com.jme3.math;

public class LineSegmentFactory {
    /**
     * <p>Creates a new LineSegment with a given origin and end. This constructor will calculate the
     * center, the direction and the extent.</p>
     */
    public static LineSegment create(Vector3f start, Vector3f end) {
        Vector3f origin = new Vector3f(0.5f * (start.x + end.x), 0.5f * (start.y + end.y), 0.5f * (start.z + end.z));
        Vector3f direction = end.subtract(start);
        float extent = direction.length() * 0.5f;
        direction.normalizeLocal();

        return new LineSegment(origin, direction, extent);
    }
}
