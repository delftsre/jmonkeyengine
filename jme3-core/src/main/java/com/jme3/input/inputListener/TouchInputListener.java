package com.jme3.input.inputListener;

import com.jme3.input.ICursorPos;
import com.jme3.input.event.InputEvent;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.input.queue.IEventQueue;

public class TouchInputListener extends BaseInputListener{
	
	private ICursorPos cursorPos;
	
	public TouchInputListener(IEventQueue queue, ICursorPos cp)
	{
		super(queue);
		this.cursorPos = cp;
	}
	
	@Override
	public void onEvent(InputEvent event) {
		if(event instanceof TouchEvent)
		{
			TouchEvent evt = (TouchEvent) event;
//	        if (!eventsPermitted) {
//	            throw new UnsupportedOperationException("MouseInput has raised an event at an illegal time.");
//	        }

	        cursorPos.setCursorPosition(evt.getX(), evt.getY());
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

