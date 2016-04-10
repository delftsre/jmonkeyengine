package com.jme3.input.test.util;

public class ActionListenerTester implements com.jme3.input.controls.ActionListener {

	public boolean onActionCalled = false;
	public int nrsCalled = 0;
	
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		nrsCalled++;
		onActionCalled = true;
	}

}
