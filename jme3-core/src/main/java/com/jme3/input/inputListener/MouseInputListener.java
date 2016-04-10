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

public class MouseInputListener extends BaseInputListener {

	private ICursorPos cursorPos;
	
	public MouseInputListener(IEventQueue queue, ICursorPos cp)
	{
		super(queue);
		this.cursorPos = cp;
	}
	
	@Override
	public void onEvent(InputEvent event) {
//        if (!eventsPermitted) {
//            throw new UnsupportedOperationException("MouseInput has raised an event at an illegal time.");
//        }
		
		if(event instanceof MouseMotionEvent){
			MouseMotionEvent evt = (MouseMotionEvent) event;
			cursorPos.setCursorPosition(evt.getX(), evt.getY());
		}else if(event instanceof MouseButtonEvent){
			MouseButtonEvent evt = (MouseButtonEvent) event;
			cursorPos.setCursorPosition(evt.getX(), evt.getY());
		}else {
			return;
		}
		
		
		inputQueue.add(event);
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
