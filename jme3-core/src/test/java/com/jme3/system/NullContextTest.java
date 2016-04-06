package com.jme3.system;

import com.jme3.input.Input;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class NullContextTest {

    @Test
    public void testGetInputNotNull() throws Exception {
        JmeContext spy = spy(new NullContext());

        List<Input> inputs = spy.getInput();

        assertNotNull(inputs);
    }

    @Test
    public void testGetInputWithoutJoystick() throws Exception {
        AppSettings settings = new AppSettings(true);
        settings.setUseJoysticks(false);
        JmeContext spy = spy(new NullContext());
        spy.setSettings(settings);

        spy.getInput();

        verify(spy, times(1)).getKeyInput();
        verify(spy, times(1)).getMouseInput();
        verify(spy, times(0)).getJoyInput();
        verify(spy, times(1)).getTouchInput();
    }

    @Test
    public void testGetInputNoNullWithoutJoystick() throws Exception {
        AppSettings settings = new AppSettings(true);
        settings.setUseJoysticks(false);
        JmeContext spy = spy(new NullContext());
        spy.setSettings(settings);

        List<Input> inputs = spy.getInput();

        assertFalse(inputs.contains(null));
    }

    @Test
    public void testGetInputNoNullWithJoystick() throws Exception {
        AppSettings settings = new AppSettings(true);
        settings.setUseJoysticks(true);
        JmeContext spy = spy(new NullContext());
        spy.setSettings(settings);

        List<Input> inputs = spy.getInput();

        assertFalse(inputs.contains(null));
    }

}