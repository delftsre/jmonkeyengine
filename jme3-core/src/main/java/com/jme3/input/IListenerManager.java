package com.jme3.input;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;

public interface IListenerManager {

	/**
	 * Adds a new listener to receive events on the given mappings.
	 *
	 * <p>The given InputListener will be registered to receive events
	 * on the specified mapping names. When a mapping raises an event, the
	 * listener will have its appropriate method invoked, either
	 * {@link ActionListener#onAction(java.lang.String, boolean, float) }
	 * or {@link AnalogListener#onAnalog(java.lang.String, float, float) }
	 * depending on which interface the <code>listener</code> implements.
	 * If the listener implements both interfaces, then it will receive the
	 * appropriate event for each method.
	 *
	 * @param listener The listener to register to receive input events.
	 * @param mappingNames The mapping names which the listener will receive
	 * events from.
	 *
	 * @see InputManager#removeListener(com.jme3.input.controls.InputListener)
	 */
	void addListener(InputListener listener, String... mappingNames);

	/**
	 * Removes a listener from receiving events.
	 *
	 * <p>This will unregister the listener from any mappings that it
	 * was previously registered.
	 *
	 * @param listener The listener to unregister.
	 *
	 * @see InputManager#addListener(com.jme3.input.controls.InputListener, java.lang.String[])
	 */
	void removeListener(InputListener listener);

}