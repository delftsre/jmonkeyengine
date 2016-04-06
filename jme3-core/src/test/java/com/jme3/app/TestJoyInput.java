package com.jme3.app;


import com.jme3.input.InputManager;
import com.jme3.input.JoyInput;
import com.jme3.input.Joystick;
import com.jme3.input.RawInputListener;

public class TestJoyInput implements JoyInput{
    @Override
    public void setJoyRumble(int joyId, float amount) {

    }

    @Override
    public Joystick[] loadJoysticks(InputManager inputManager) {
        return new Joystick[0];
    }

    @Override
    public void initialize() {

    }

    @Override
    public void update() {

    }

    @Override
    public void destroy() {

    }

    @Override
    public boolean isInitialized() {
        return false;
    }

    @Override
    public void setInputListener(RawInputListener listener) {

    }

    @Override
    public long getInputTimeNanos() {
        return 0;
    }
}
