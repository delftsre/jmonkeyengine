package com.jme3.input;

import java.util.List;

import com.jme3.input.event.*;
import com.jme3.util.SafeArrayList;
import com.jme3.input.inputListener.BaseInputListener;

public class BaseListenerHandler implements IBaseListenerRegistration, IBaseListenerEmitter {
	
	
	private final SafeArrayList<BaseInputListener> baseInputListeners = new SafeArrayList<BaseInputListener>(BaseInputListener.class);
    
	//contains rawinputlistener for not breaking code
	private final SafeArrayList<RawInputListener> rawInputListeners = new SafeArrayList<RawInputListener>(RawInputListener.class);

    /* (non-Javadoc)
	 * @see com.jme3.input.IBaseListenerHandler#addRawInputListener(com.jme3.input.inputListener.BaseInputListener)
	 */
    @Override
	public void addRawInputListener(RawInputListener listener) {
    	rawInputListeners.add(listener);
    }

    /* (non-Javadoc)
	 * @see com.jme3.input.IBaseListenerHandler#removeRawInputListener(com.jme3.input.RawInputListener)
	 */
    @Override
	public void removeRawInputListener(RawInputListener listener) {
    	rawInputListeners.remove(listener);
    }
    
	public void addRawInputListener(BaseInputListener listener) {
    	baseInputListeners.add(listener);
    }

	public void removeRawInputListener(BaseInputListener listener) {
    	baseInputListeners.remove(listener);
    }

    /* (non-Javadoc)
	 * @see com.jme3.input.IBaseListenerHandler#clearRawInputListeners()
	 */
    @Override
	public void clearRawInputListeners() {
    	rawInputListeners.clear();
    	baseInputListeners.clear();
    }
    
    
    /* (non-Javadoc)
	 * @see com.jme3.input.IBaseListenerEmitter#emit(java.util.List)
	 */
    @Override
	public void emit(List<InputEvent> inputQueue)
    {
    	int queueSize = inputQueue.size();
    	
        for (BaseInputListener listener : baseInputListeners) {
            listener.beginInput();

            for (int j = 0; j < queueSize; j++) {
                InputEvent event = inputQueue.get(j);
                if (event.isConsumed()) {
                    continue;
                }
                
                listener.onEvent(event);
            }

            listener.endInput();
        }
        
        RawInputListener[] array = rawInputListeners.getArray(); 

        for (RawInputListener listener : array) {
            listener.beginInput();

            for (int j = 0; j < queueSize; j++) {
                InputEvent event = inputQueue.get(j);
                if (event.isConsumed()) {
                    continue;
                }

                if (event instanceof MouseMotionEvent) {
                    listener.onMouseMotionEvent((MouseMotionEvent) event);
                } else if (event instanceof KeyInputEvent) {
                    listener.onKeyEvent((KeyInputEvent) event);
                } else if (event instanceof MouseButtonEvent) {
                    listener.onMouseButtonEvent((MouseButtonEvent) event);
                } else if (event instanceof JoyAxisEvent) {
                    listener.onJoyAxisEvent((JoyAxisEvent) event);
                } else if (event instanceof JoyButtonEvent) {
                    listener.onJoyButtonEvent((JoyButtonEvent) event);
                } else if (event instanceof TouchEvent) {
                    listener.onTouchEvent((TouchEvent) event);
                } else {
                    assert false;
                }
            }

            listener.endInput();
        }
        
        
    }
}
