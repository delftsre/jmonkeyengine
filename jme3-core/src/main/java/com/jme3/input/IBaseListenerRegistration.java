package com.jme3.input;

import com.jme3.input.event.InputEvent;
import com.jme3.input.inputListener.BaseInputListener;

public interface IBaseListenerRegistration {

	/**
	 * Adds a {@link RawInputListener} to receive raw input events.
	 *
	 * <p>
	 * Any raw input listeners registered to this <code>BaseListenerResgistration</code>
	 * will receive raw input events first, before they get handled
	 * by the <code>EventProcessors</code> itself. The listeners are
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
	void addRawInputListener(RawInputListener listener);

	/**
	 * Removes a {@link RawInputListener} so that it no longer
	 * receives raw input events.
	 *
	 * @param listener The listener to cease receiving raw input events.

	 */
	void removeRawInputListener(RawInputListener listener);

	/**
	 * Clears all {@link RawInputListener}s.
	 *
	 */
	void clearRawInputListeners();

}