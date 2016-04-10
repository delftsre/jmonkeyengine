package com.jme3.input.inputListener;

import com.jme3.input.RawInputListener;
import com.jme3.input.event.InputEvent;
import com.jme3.input.queue.IEventQueue;

public abstract class BaseInputListener implements RawInputListener{

	public IEventQueue inputQueue;
	
	public BaseInputListener(IEventQueue queue)
	{
		this.inputQueue = queue;
	}	
    
    public abstract void onEvent(InputEvent event);

	@Override
	public void beginInput() {
	}

	@Override
	public void endInput() {
	}
}
