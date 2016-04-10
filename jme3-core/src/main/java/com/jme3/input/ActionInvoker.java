package com.jme3.input;

import java.util.List;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.TouchListener;
import com.jme3.input.controls.TouchTrigger;
import com.jme3.input.event.TouchEvent;
import com.jme3.input.util.IReadInputSettings;
import com.jme3.input.util.IReadTimer;
import com.jme3.math.FastMath;
import com.jme3.util.IntMap;
import com.jme3.util.IntMap.Entry;

public class ActionInvoker {

	private IReadInputSettings settings;
	private IReadBindings bindings;
	private IReadTimer timer; 
    private final IntMap<Long> pressedButtons = new IntMap<Long>();
    private IntMap<Float> axisValues = new IntMap<Float>();
	
	public ActionInvoker(IReadInputSettings settings, IReadBindings bindings, IReadTimer timer)
	{
		this.settings = settings;
		this.bindings = bindings;
		this.timer = timer;
	}
	
    public void invokeActions(int hash, boolean pressed) {
        List<Mapping> maps = bindings.getMappings(hash);
        if (maps == null) {
            return;
        }

        int size = maps.size();
        for (int i = size - 1; i >= 0; i--) {
            Mapping mapping = maps.get(i);
            List<InputListener> listeners = mapping.listeners;
            int listenerSize = listeners.size();
            for (int j = listenerSize - 1; j >= 0; j--) {
                InputListener listener = listeners.get(j);
                if (listener instanceof ActionListener) {
                    ((ActionListener) listener).onAction(mapping.name, pressed, settings.getFrameTPF());
                }
            }
        }
    }

    private float computeAnalogValue(long timeDelta) {
        if (settings.safeModeEnabled() || timer.getFrameDelta() == 0) {
            return 1f;
        } else {
            return FastMath.clamp((float) timeDelta / (float) timer.getFrameDelta(), 0, 1);
        }
    }

    public void invokeTimedActions(int hash, long time, boolean pressed) {
        if (!bindings.contains(hash)) {
            return;
        }

        if (pressed) {
            pressedButtons.put(hash, time);
        } else {
            Long pressTimeObj = pressedButtons.remove(hash);
            if (pressTimeObj == null) {
                return; // under certain circumstances it can be null, ignore
            }                        // the event then.

            long pressTime = pressTimeObj;
            long releaseTime = time;
            long timeDelta = releaseTime - Math.max(pressTime, timer.getLastUpdateTime());

            if (timeDelta > 0) {
                invokeAnalogs(hash, computeAnalogValue(timeDelta), false);
            }
        }
    }

    public void invokeUpdateActions() {
        for (Entry<Long> pressedButton : pressedButtons) {
            int hash = pressedButton.getKey();
            long pressTime = pressedButton.getValue();
            
            long timeDelta = timer.getLastUpdateTime() - Math.max(timer.getLastLastUpdateTime(), pressTime);

            if (timeDelta > 0) {
                invokeAnalogs(hash, computeAnalogValue(timeDelta), false);
            }
        }

        for (Entry<Float> axisValue : axisValues) {
            int hash = axisValue.getKey();
            float value = axisValue.getValue();
            invokeAnalogs(hash, value * settings.getFrameTPF(), true);
        }
    }

    public void invokeTouchActions(TouchEvent evt)
    {
        List<Mapping> maps = bindings.getMappings(TouchTrigger.touchHash(evt.getKeyCode()));
        if (maps == null) {
            return;
        }

        int size = maps.size();
        for (int i = size - 1; i >= 0; i--) {
            Mapping mapping = maps.get(i);
            List<InputListener> listeners = mapping.listeners;
            int listenerSize = listeners.size();
            for (int j = listenerSize - 1; j >= 0; j--) {
                InputListener listener = listeners.get(j);
                if (listener instanceof TouchListener) {
                    ((TouchListener) listener).onTouch(mapping.name, evt, settings.getFrameTPF());
                }
            }
        }
    }
    
    public void invokeAnalogs(int hash, float value, boolean isAxis) {
        List<Mapping> maps = bindings.getMappings(hash);
        if (maps == null) {
            return;
        }

        if (!isAxis) {
            value *= settings.getFrameTPF();
        }

        int size = maps.size();
        for (int i = size - 1; i >= 0; i--) {
            Mapping mapping = maps.get(i);
            List<InputListener> listeners = mapping.listeners;
            int listenerSize = listeners.size();
            for (int j = listenerSize - 1; j >= 0; j--) {
                InputListener listener = listeners.get(j);
                if (listener instanceof AnalogListener) {
                    // NOTE: multiply by TPF for any button bindings
                    ((AnalogListener) listener).onAnalog(mapping.name, value, settings.getFrameTPF());
                }
            }
        }
    }
    
    public void invokeAnalogsAndActions(int hash, float value, 
    		float effectiveDeadZone, boolean applyTpf) {
    	
    	if (value < effectiveDeadZone) {
            invokeAnalogs(hash, value, !applyTpf);
            return;
        }

        List<Mapping> maps = bindings.getMappings(hash);
        if (maps == null) {
            return;
        }

        boolean valueChanged = !axisValues.containsKey(hash);
        
        if (applyTpf) {
            value *= settings.getFrameTPF();
        }

        int size = maps.size();
        for (int i = size - 1; i >= 0; i--) {
            Mapping mapping = maps.get(i);
            List<InputListener> listeners = mapping.listeners;
            int listenerSize = listeners.size();
            for (int j = listenerSize - 1; j >= 0; j--) {
                InputListener listener = listeners.get(j);

                if (listener instanceof ActionListener && valueChanged) {
                    ((ActionListener) listener).onAction(mapping.name, true, settings.getFrameTPF());
                }

                if (listener instanceof AnalogListener) {
                    ((AnalogListener) listener).onAnalog(mapping.name, value, settings.getFrameTPF());
                }

            }
        }
    }

    public void invokeAnalogsAndActions(int hash, float value, 
    		float effectiveDeadZone, boolean applyTpf, IntMap<Float> newerAxisValues) {
    	
    	axisValues = newerAxisValues;    	
    	invokeAnalogsAndActions(hash, value, effectiveDeadZone, applyTpf);
    }

	public IReadInputSettings getSettings() {
		return settings;
	}	
	
	/**
     * Do not use.
     * Called to reset pressed keys or buttons when focus is restored.
     */
    public void reset() {
        pressedButtons.clear();
        axisValues.clear();
    }
	
}
