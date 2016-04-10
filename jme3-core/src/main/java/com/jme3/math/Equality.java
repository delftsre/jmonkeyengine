package com.jme3.math;

public class Equality {

    public static boolean equals(float x, float y, float delta) {
        return Math.abs(x-y) < delta;
    }

}
