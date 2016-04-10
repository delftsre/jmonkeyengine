package com.jme3.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.Trigger;
import com.jme3.util.IntMap;

public class Mapper implements IMapper, IListenerManager, IReadBindings {

	private static final Logger logger = Logger.getLogger(InputManager.class.getName());
	private final HashMap<String, Mapping> mappings = new HashMap<String, Mapping>();
	private final IntMap<ArrayList<Mapping>> bindings = new IntMap<ArrayList<Mapping>>();
    
    /* (non-Javadoc)
	 * @see com.jme3.input.IMapper#addMapping(java.lang.String, com.jme3.input.controls.Trigger)
	 */
    @Override
	public void addMapping(String mappingName, Trigger... triggers) {
        Mapping mapping = mappings.get(mappingName);
        if (mapping == null) {
            mapping = new Mapping(mappingName);
            mappings.put(mappingName, mapping);
        }

        for (Trigger trigger : triggers) {
            int hash = trigger.triggerHashCode();
            ArrayList<Mapping> names = bindings.get(hash);
            if (names == null) {
                names = new ArrayList<Mapping>();
                bindings.put(hash, names);
            }
            if (!names.contains(mapping)) {
                names.add(mapping);
                mapping.triggers.add(hash);
            } else {
                logger.log(Level.WARNING, "Attempted to add mapping \"{0}\" twice to trigger.", mappingName);
            }
        }
    }
    
    /* (non-Javadoc)
	 * @see com.jme3.input.IMapper#deleteMapping(java.lang.String)
	 */
    @Override
	public void deleteMapping(String mappingName) {
        Mapping mapping = mappings.remove(mappingName);
        if (mapping == null) {
            //throw new IllegalArgumentException("Cannot find mapping: " + mappingName);
            logger.log(Level.WARNING, "Cannot find mapping to be removed, skipping: {0}", mappingName);
            return;
        }

        ArrayList<Integer> triggers = mapping.triggers;
        for (int i = triggers.size() - 1; i >= 0; i--) {
            int hash = triggers.get(i);
            ArrayList<Mapping> maps = bindings.get(hash);
            maps.remove(mapping);
        }
    }
    
    /* (non-Javadoc)
	 * @see com.jme3.input.IMapper#hasMapping(java.lang.String)
	 */
    @Override
	public boolean hasMapping(String mappingName) {
        return mappings.containsKey(mappingName);
    }
    
    /* (non-Javadoc)
	 * @see com.jme3.input.IMapper#deleteTrigger(java.lang.String, com.jme3.input.controls.Trigger)
	 */
    @Override
	public void deleteTrigger(String mappingName, Trigger trigger) {
        Mapping mapping = mappings.get(mappingName);
        if (mapping == null) {
            throw new IllegalArgumentException("Cannot find mapping: " + mappingName);
        }

        ArrayList<Mapping> maps = bindings.get(trigger.triggerHashCode());
        maps.remove(mapping);

    }
    
    /* (non-Javadoc)
	 * @see com.jme3.input.IListenerManager#addListener(com.jme3.input.controls.InputListener, java.lang.String)
	 */
    @Override
	public void addListener(InputListener listener, String... mappingNames) {
        for (String mappingName : mappingNames) {
            Mapping mapping = mappings.get(mappingName);
            if (mapping == null) {
                mapping = new Mapping(mappingName);
                mappings.put(mappingName, mapping);
            }
            if (!mapping.listeners.contains(listener)) {
                mapping.listeners.add(listener);
            }
        }
    }

    /* (non-Javadoc)
	 * @see com.jme3.input.IListenerManager#removeListener(com.jme3.input.controls.InputListener)
	 */
    @Override
	public void removeListener(InputListener listener) {
        for (Mapping mapping : mappings.values()) {
            mapping.listeners.remove(listener);
        }
    }
    
    public List<Mapping> getMappings(int hash)
    {
    	return bindings.get(hash);
    }
    
    public boolean contains(int hash)
    {
    	return bindings.containsKey(hash);
    }
    
    
    /* (non-Javadoc)
	 * @see com.jme3.input.IMapper#clearMappings()
	 */
    @Override
	public void clearMappings() {
        mappings.clear();
        bindings.clear();
    }
    

    
    
}
