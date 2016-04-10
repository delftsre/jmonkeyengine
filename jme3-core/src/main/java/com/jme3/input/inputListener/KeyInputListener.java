package com.jme3.input.inputListener;

import com.jme3.input.event.InputEvent;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.input.queue.IEventQueue;

public class KeyInputListener extends BaseInputListener {

	public KeyInputListener(IEventQueue queue) {
		super(queue);
	}

	@Override
	public void onEvent(InputEvent event) {
		if(event instanceof KeyInputEvent)
		{
//	        if (!eventsPermitted) {
//	            throw new UnsupportedOperationException("KeyInput has raised an event at an illegal time.");
//	        }

	        inputQueue.add(event);
		}
	}

	@Override
	public void onJoyAxisEvent(JoyAxisEvent evt) {
		onEvent(evt);
		
	}

	@Override
	public void onJoyButtonEvent(JoyButtonEvent evt) {
		onEvent(evt);
		
	}

	@Override
	public void onMouseMotionEvent(MouseMotionEvent evt) {
		onEvent(evt);
		
	}

	@Override
	public void onMouseButtonEvent(MouseButtonEvent evt) {
		onEvent(evt);
		
	}

	@Override
	public void onKeyEvent(KeyInputEvent evt) {
		onEvent(evt);
		
	}

	@Override
	public void onTouchEvent(TouchEvent evt) {
		onEvent(evt);
		
	}

	
}
