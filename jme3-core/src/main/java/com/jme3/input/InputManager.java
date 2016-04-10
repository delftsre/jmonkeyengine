/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.input;

import com.jme3.app.Application;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.controls.*;
import com.jme3.input.event.*;
import com.jme3.input.eventprocessing.EventProcessorHandler;
import com.jme3.input.eventprocessing.JoyEventProcessor;
import com.jme3.input.eventprocessing.KeyEventProcessor;
import com.jme3.input.eventprocessing.MouseEventProcessor;
import com.jme3.input.eventprocessing.TouchEventProcessor;
import com.jme3.input.inputListener.*;
import com.jme3.input.queue.EventQueue;
import com.jme3.input.util.InputSettings;
import com.jme3.input.util.InputTimer;
import com.jme3.math.Vector2f;
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>InputManager</code> is responsible for converting input events
 * received from the Key, Mouse and Joy Input implementations into an
 * abstract, input device independent representation that user code can use.
 * <p>
 * By default an <code>InputManager</code> is included with every Application instance for use
 * in user code to query input, unless the Application is created as headless
 * or with input explicitly disabled.
 * <p>
 * The input manager has two concepts, a {@link Trigger} and a mapping.
 * A trigger represents a specific input trigger, such as a key button,
 * or a mouse axis. A mapping represents a link onto one or several triggers,
 * when the appropriate trigger is activated (e.g. a key is pressed), the
 * mapping will be invoked. Any listeners registered to receive an event
 * from the mapping will have an event raised.
 * <p>
 * There are two types of events that {@link InputListener input listeners}
 * can receive, one is {@link ActionListener#onAction(java.lang.String, boolean, float) action}
 * events and another is {@link AnalogListener#onAnalog(java.lang.String, float, float) analog}
 * events.
 * <p>
 * <code>onAction</code> events are raised when the specific input
 * activates or deactivates. For a digital input such as key press, the <code>onAction()</code>
 * event will be raised with the <code>isPressed</code> argument equal to true,
 * when the key is released, <code>onAction</code> is called again but this time
 * with the <code>isPressed</code> argument set to false.
 * For analog inputs, the <code>onAction</code> method will be called any time
 * the input is non-zero, however an exception to this is for joystick axis inputs,
 * which are only called when the input is above the {@link InputManager#setAxisDeadZone(float) dead zone}.
 * <p>
 * <code>onAnalog</code> events are raised every frame while the input is activated.
 * For digital inputs, every frame that the input is active will cause the
 * <code>onAnalog</code> method to be called, the argument <code>value</code>
 * argument will equal to the frame's time per frame (TPF) value but only
 * for digital inputs. For analog inputs however, the <code>value</code> argument
 * will equal the actual analog value.
 */
//@Deprecated
public class InputManager implements RawInputListener {

	CursorManager cursorManager;
	JoystickManager joystickManager;

	InputUpdater updater;
    EventQueue inputQueue;
	EventProcessorHandler processor;
    BaseListenerHandler listener;  
    
    InputSettings settings;
    Mapper mapper;
    InputTimer timer;       
    ActionInvoker invoker;

    /**
     * Initializes the InputManager.
     *
     * <p>This should only be called internally in {@link Application}.
     *
     * @param mouse
     * @param keys
     * @param joystick
     * @param touch
     * @throws IllegalArgumentException If either mouseInput or keyInput are null.
     */
    public InputManager(Mapper m, CursorManager cm) {
    	this.mapper = m;
    	this.cursorManager = cm;
    }
    
    /**
    
     * Initializes the InputManager.
     *
     * <p>This should only be called internally in {@link Application}.
     *
     * @throws IllegalArgumentException If either mouseInput or keyInput are null.
     **/
    public InputManager(MouseInput mouse, KeyInput keys, JoyInput joystick, TouchInput touch) {
        if (keys == null || mouse == null) {
            throw new IllegalArgumentException("Mouse or keyboard cannot be null");
        }
        
        processor = new EventProcessorHandler();
        
        listener = new BaseListenerHandler();
        List<Input> inputDevices = new ArrayList<Input>();
        
        settings = new InputSettings();
        mapper = new Mapper();
        timer = new InputTimer();        
        
        invoker = new ActionInvoker(settings, mapper, timer);
        
    	cursorManager = new CursorManager(mouse,touch);
    	joystickManager = new JoystickManager(this,joystick);
    	
        inputQueue = new EventQueue(listener, processor);
        
        if(mouse != null){
        	processor.add(new MouseEventProcessor(invoker));
        	mouse.setInputListener(new MouseInputListener(inputQueue, cursorManager));
        	inputDevices.add(mouse);
        }
        if(keys != null){
        	processor.add(new KeyEventProcessor(invoker));
        	keys.setInputListener(new KeyInputListener(inputQueue));
        	inputDevices.add(keys);
        }
        if(joystick != null){
        	processor.add(new JoyEventProcessor(invoker));
        	joystick.setInputListener(new JoyInputListener(inputQueue));
        	inputDevices.add(joystick);
        }
        if(touch != null){
        	processor.add(new TouchEventProcessor(invoker));
        	touch.setInputListener(new TouchInputListener(inputQueue, cursorManager));
        	inputDevices.add(touch);
        }      
        
        updater = new InputUpdater(timer, invoker, inputQueue, inputDevices, settings);
    }

    public void addListener(InputListener listener, String... mappingNames) {
        mapper.addListener(listener, mappingNames);;
    }
    
    public void removeListener(InputListener listener) {
	    mapper.removeListener(listener);
    }

    /**
     * Adds a {@link RawInputListener} to receive raw input events.
     *
     * <p>
     * Any raw input listeners registered to this <code>InputManager</code>
     * will receive raw input events first, before they get handled
     * by the <code>InputManager</code> itself. The listeners are
     * each processed in the order they were added, e.g. FIFO.
     * <p>
     * If a raw input listener has handled the event and does not wish
     * other listeners down the list to process the event, it may set the
     * {@link InputEvent#setConsumed() consumed flag} to indicate the
     * event was consumed and shouldn't be processed any further.
     * The listener may do this either at each of the event callbacks
     * or at the {@link RawInputListener#endInput() } method.
     *
     * @param listener A listener to receive raw input events.
     *
     * @see RawInputListener
     */
    public void addRawInputListener(RawInputListener listener) {
        this.listener.addRawInputListener(listener);
    }

    /**
     * Removes a {@link RawInputListener} so that it no longer
     * receives raw input events.
     *
     * @param listener The listener to cease receiving raw input events.
     *
     * @see InputManager#addRawInputListener(com.jme3.input.RawInputListener)
     */
    public void removeRawInputListener(RawInputListener listener) {
        this.listener.removeRawInputListener(listener);
    }

    /**
     * Clears all {@link RawInputListener}s.
     *
     * @see InputManager#addRawInputListener(com.jme3.input.RawInputListener)
     */
    public void clearRawInputListeners() {
    	listener.clearRawInputListeners();
    }


    public void addMapping(String mappingName, Trigger... triggers) {
    	mapper.addMapping(mappingName, triggers);
    }

    /**
     * Returns true if this InputManager has a mapping registered
     * for the given mappingName.
     *
     * @param mappingName The mapping name to check.
     *
     * @see InputManager#addMapping(java.lang.String, com.jme3.input.controls.Trigger[])
     * @see InputManager#deleteMapping(java.lang.String)
     */
    public boolean hasMapping(String mappingName) {
        return mapper.hasMapping(mappingName);
    }

    public void deleteMapping(String mappingName) {
    	mapper.deleteMapping(mappingName);
    }
    

    /**
     * Deletes a specific trigger registered to a mapping.
     *
     * <p>
     * The given mapping will no longer receive events raised by the
     * trigger.
     *
     * @param mappingName The mapping name to cease receiving events from the
     * trigger.
     * @param trigger The trigger to no longer invoke events on the mapping.
     */
    public void deleteTrigger(String mappingName, Trigger trigger) {
        mapper.deleteTrigger(mappingName, trigger);
    }

    /**
     * Clears all the input mappings from this InputManager.
     * Consequently, also clears all of the
     * InputListeners as well.
     */
    public void clearMappings() {
        mapper.clearMappings();
    }
    /**
     * Set the deadzone for joystick axes.
     *
     * <p>{@link ActionListener#onAction(java.lang.String, boolean, float) }
     * events will only be raised if the joystick axis value is greater than
     * the <code>deadZone</code>.
     *
     * @param deadZone the deadzone for joystick axes.
     */
    public void setAxisDeadZone(float deadZone) {
        settings.setGlobalAxisDeadZone(deadZone);
    }

    /**
     * Returns the deadzone for joystick axes.
     *
     * @return the deadzone for joystick axes.
     */
    public float getAxisDeadZone() {
        return settings.getGlobalAxisDeadZone();
    }
    
    /**
     * Do not use.
     * Called to reset pressed keys or buttons when focus is restored.
     */
    public void reset() {
    	invoker.reset();
    }
    
    public void setMouseCursor(JmeCursor jmeCursor) {
		cursorManager.setMouseCursor(jmeCursor);
	}
    
    /**
     * Returns whether the mouse cursor is visible or not.
     *
     * <p>By default the cursor is visible.
     *
     * @return whether the mouse cursor is visible or not.
     *
     * @see InputManager#setCursorVisible(boolean)
     */
    public boolean isCursorVisible() {
        return cursorManager.isCursorVisible();
    }

    /**
     * Set whether the mouse cursor should be visible or not.
     *
     * @param visible whether the mouse cursor should be visible or not.
     */
    public void setCursorVisible(boolean visible) {
       cursorManager.setCursorVisible(visible);
    }

    /**
     * Returns the current cursor position. The position is relative to the
     * bottom-left of the screen and is in pixels.
     *
     * @return the current cursor position
     */
    public Vector2f getCursorPosition() {
        return cursorManager.getCursorPosition();
    }
    
    /**
     * Returns an array of all joysticks installed on the system.
     *
     * @return an array of all joysticks installed on the system.
     */
    public Joystick[] getJoysticks() {
        return joystickManager.getJoysticks();
    }

    /**
     * Enable simulation of mouse events. Used for touchscreen input only.
     *
     * @param value True to enable simulation of mouse events
     */
    public void setSimulateMouse(boolean value) {
        cursorManager.setSimulateMouse(value);
    }
    /**
     * @deprecated Use isSimulateMouse
     * Returns state of simulation of mouse events. Used for touchscreen input only.
     *
     */
    public boolean getSimulateMouse() {
        return cursorManager.isSimulateMouse();
    }

    /**
     * Returns state of simulation of mouse events. Used for touchscreen input only.
     *
     */
    public boolean isSimulateMouse() {
        return cursorManager.isSimulateMouse();
    }

    /**
     * Enable simulation of keyboard events. Used for touchscreen input only.
     *
     * @param value True to enable simulation of keyboard events
     */
    public void setSimulateKeyboard(boolean value) {
        cursorManager.setSimulateKeyboard(value);
    }

    /**
     * Returns state of simulation of key events. Used for touchscreen input only.
     *
     */
    public boolean isSimulateKeyboard() {
        return cursorManager.isSimulateKeyboard();
    }
    
    /**
     * Updates the <code>InputManager</code>.
     * This will query current input devices and send
     * appropriate events to registered listeners.
     *
     * @param tpf Time per frame value.
     */
    public void update(float tpf) {
        updater.update(tpf);
    }
    
	@Override
	public void beginInput() {
	}

	@Override
	public void endInput() {
	}

	public void onJoyAxisEvent(JoyAxisEvent evt) {
        throw new UnsupportedOperationException("JoyInput has raised an event at an illegal time.");
	}

	public void onJoyButtonEvent(JoyButtonEvent evt) {
        throw new UnsupportedOperationException("JoyInput has raised an event at an illegal time.");
	}

	public void onMouseMotionEvent(MouseMotionEvent evt) {
        throw new UnsupportedOperationException("MouseInput has raised an event at an illegal time.");
	}

	public void onMouseButtonEvent(MouseButtonEvent evt) {
        throw new UnsupportedOperationException("MouseInput has raised an event at an illegal time.");
	}

	public void onKeyEvent(KeyInputEvent evt) {
        throw new UnsupportedOperationException("KeyInput has raised an event at an illegal time.");		
	}

	public void onTouchEvent(TouchEvent evt) {
        throw new UnsupportedOperationException("TouchInput has raised an event at an illegal time.");
	}

}
