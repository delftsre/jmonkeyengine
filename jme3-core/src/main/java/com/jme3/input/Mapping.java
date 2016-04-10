package com.jme3.input;

import java.util.ArrayList;

import com.jme3.input.controls.InputListener;

public class Mapping {

    public final String name;
    public final ArrayList<Integer> triggers = new ArrayList<Integer>();
    public final ArrayList<InputListener> listeners = new ArrayList<InputListener>();

    public Mapping(String name) {
        this.name = name;
    }
}
