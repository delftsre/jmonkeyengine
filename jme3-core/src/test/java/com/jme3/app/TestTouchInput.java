package com.jme3.app;

import com.jme3.input.RawInputListener;
import com.jme3.input.TouchInput;

public class TestTouchInput implements TouchInput{
    @Override
    public void setSimulateMouse(boolean simulate) {

    }

    @Override
    public boolean isSimulateMouse() {
        return false;
    }

    @Override
    public void setSimulateKeyboard(boolean simulate) {

    }

    @Override
    public boolean isSimulateKeyboard() {
        return false;
    }

    @Override
    public void setOmitHistoricEvents(boolean dontSendHistory) {

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
