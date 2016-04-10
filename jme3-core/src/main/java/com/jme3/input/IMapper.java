package com.jme3.input;

import com.jme3.input.controls.Trigger;

public interface IMapper {

	/**
	 * Create a new mapping to the given triggers.
	 *
	 * <p>
	 * The given mapping will be assigned to the given triggers, when
	 * any of the triggers given raise an event, the listeners
	 * registered to the mappings will receive appropriate events.
	 *
	 * @param mappingName The mapping name to assign.
	 * @param triggers The triggers to which the mapping is to be registered.
	 *
	 */
	void addMapping(String mappingName, Trigger... triggers);

	/**
	 * Deletes a mapping from receiving trigger events.
	 *
	 * <p>
	 * The given mapping will no longer be assigned to receive trigger
	 * events.
	 *
	 * @param mappingName The mapping name to unregister.
	 *
	 */
	void deleteMapping(String mappingName);

	/**
	 * Returns true if this InputManager has a mapping registered
	 * for the given mappingName.
	 *
	 * @param mappingName The mapping name to check.
	 *
	 */
	boolean hasMapping(String mappingName);

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
	void deleteTrigger(String mappingName, Trigger trigger);

	/**
	 * Clears all the input mappings from this InputManager.
	 * Consequently, also clears all of the
	 * InputListeners as well.
	 */
	void clearMappings();

}