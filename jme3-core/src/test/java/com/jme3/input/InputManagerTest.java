package com.jme3.input;

import com.jme3.app.TestJoyInput;
import com.jme3.app.TestTouchInput;
import com.jme3.input.dummy.DummyKeyInput;
import com.jme3.input.dummy.DummyMouseInput;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class InputManagerTest {

    private MouseInput mouseSpy;
    private KeyInput keySpy;
    private TouchInput touchSpy;
    private JoyInput joySpy;
    private ArrayList<Input> inputs;

    @Before
    public void setupInputs(){
        mouseSpy = spy(new DummyMouseInput());
        keySpy = spy(new DummyKeyInput());
        touchSpy = spy(new TestTouchInput());
        joySpy = spy(new TestJoyInput());

        inputs = new ArrayList<>();
        inputs.add(mouseSpy);
        inputs.add(keySpy);
        inputs.add(touchSpy);
        inputs.add(joySpy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializationNoKey() {
        inputs.remove(keySpy);

        new InputManager(inputs);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitializationNoMouse() {
        inputs.remove(mouseSpy);

        new InputManager(inputs);
    }

    @Test
    public void testInitializationListenersSet() {
        InputManager inputManager = new InputManager(inputs);

        verify(mouseSpy, times(1)).setInputListener(inputManager);
        verify(keySpy, times(1)).setInputListener(inputManager);
        verify(touchSpy, times(1)).setInputListener(inputManager);
        verify(joySpy, times(1)).setInputListener(inputManager);
    }

    @Test
    public void testInitializationInputInitialization() {
        new InputManager(inputs);

        verify(mouseSpy, times(1)).initialize();
        verify(keySpy, times(1)).initialize();
        verify(touchSpy, times(1)).initialize();
        verify(joySpy, times(1)).initialize();
    }

    @Test
    public void testInitializationInputAssignments() {
        InputManager inputManager = new InputManager(inputs);

        assertNotNull(inputManager.getJoystick());
        assertNotNull(inputManager.getMouse());
        assertNotNull(inputManager.getKeys());
        assertNotNull(inputManager.getTouch());
    }

    @Test
    public void testDestroyInput() throws Exception {
        InputManager inputManager = new InputManager(inputs);

        inputManager.destroyInput();

        for(Input input : inputs) {
            verify(input, times(1)).destroy();
        }

    }
}