package com.jme3.input;

import java.util.List;

import com.jme3.input.queue.IEventQueue;
import com.jme3.input.util.ISetInputSettings;
import com.jme3.input.util.InputTimer;

public class InputUpdater implements IInputUpdater {
	
    private InputTimer timer;
    private ActionInvoker invoker;
    private IEventQueue processor;
    private List<Input> inputDevices;
    private ISetInputSettings settings;
	
    public InputUpdater(InputTimer inputTimer, ActionInvoker actionInvoker, IEventQueue queueProcessor, List<Input> inputDevices, ISetInputSettings inputSettings) {
    	timer = inputTimer;
    	invoker = actionInvoker;
    	processor = queueProcessor;
    	this.inputDevices = inputDevices;
    	settings = inputSettings;
    }
	
    /**
     * Updates the whole InputSystem.
     * This will query current input devices and send
     * appropriate events to registered listeners.
     *
     * @param tpf Time per frame value.
     */
    public void update(float tpf) {
        settings.setFrameTPF(tpf);
        
        // Activate safemode if the TPF value is so small
        // that rounding errors are inevitable
        settings.setSafeMode(tpf < 0.015f);

        long currentTime = java.lang.System.currentTimeMillis();
        timer.setFrameDelta(currentTime - timer.getLastUpdateTime());
        
        for(Input inp : inputDevices)
        {
        	inp.update();
        }        

        processor.processQueue();
        invoker.invokeUpdateActions();

        timer.setLastLastUpdateTime(timer.getLastUpdateTime());
        timer.setLastUpdateTime(currentTime);
    }
}
