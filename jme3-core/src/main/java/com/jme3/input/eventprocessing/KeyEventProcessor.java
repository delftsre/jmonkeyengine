package com.jme3.input.eventprocessing;

import com.jme3.input.ActionInvoker;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.InputEvent;
import com.jme3.input.event.KeyInputEvent;

public class KeyEventProcessor implements EventProcessor {

	private ActionInvoker invoker;
	
	public KeyEventProcessor(ActionInvoker actionInvoker)
	{
		invoker = actionInvoker;
	}
	
	@Override
	public void processEvent(InputEvent event) {
		if(event instanceof KeyInputEvent){
			onKeyEventQueued((KeyInputEvent) event);
		}
	}
	
    private void onKeyEventQueued(KeyInputEvent evt) {
        if (evt.isRepeating()) {
            return; // repeat events not used for bindings
        }

        int hash = KeyTrigger.keyHash(evt.getKeyCode());
        invoker.invokeActions(hash, evt.isPressed());
        invoker.invokeTimedActions(hash, evt.getTime(), evt.isPressed());
    }

	@Override
	public boolean accepts(InputEvent event) {
		return (event instanceof KeyInputEvent);
	}

}
