package com.jme3.input.test.util;

import com.jme3.input.controls.AnalogListener;

public class AnalogListenerTester implements AnalogListener {

	public boolean onAnalogCalled = false;
	public int nrsCalled = 0;
	
	@Override
	public void onAnalog(String name, float value, float tpf) {
		nrsCalled++;
		onAnalogCalled = true;
	}

}
