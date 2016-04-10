package com.jme3.input.test.util;

import com.jme3.input.controls.TouchListener;
import com.jme3.input.event.TouchEvent;

public class TouchListenerTester implements TouchListener {

	public boolean onTouchCalled = false;
	public int nrsCalled = 0;

	@Override
	public void onTouch(String name, TouchEvent event, float tpf) {
		nrsCalled++;
		onTouchCalled = true;		
	}

}
