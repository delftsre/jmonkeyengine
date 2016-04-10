/*
 * Copyright (c) 2009-2015 jMonkeyEngine
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

import static org.junit.Assert.*;

import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.dummy.*;
import com.jme3.input.event.*;
import com.jme3.input.test.util.ActionListenerTester;
import com.jme3.input.test.util.AnalogListenerTester;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests functionality of input behavior of the mouse and events 
 * 
 * @author Willem Vaandrager
 */

public class JoyInputTest{
    
	public MouseInput mi;
	public KeyInput ki;
	public TestJoyInput ji;
	public InputManager im;
	
	public Joystick joystick;
	public JoystickButton button;
	public JoystickAxis axis;
	
    @Before
    public void setUp() {
    	mi = new DummyMouseInput();
        mi.initialize();
        
    	ki = new DummyKeyInput();    	
        ki.initialize();
        
        ji = new TestJoyInput();
        ji.initialize();
        
    	im = new InputManager(mi, ki, ji, null);
    	
    	//Test joystick
    	joystick = new TestJoystick(im, ji, 0, "test");
    	button = new DefaultJoystickButton(im, joystick, 0, "button0", "0");
    	axis = new DefaultJoystickAxis(im, joystick, 0, "x", "x", false, false, 0);
    	ji.setJoystick(joystick);
    }    

    @Test(expected=UnsupportedOperationException.class)
    public void testJoyButtonEventNotEventsPermitted() {
        im.onJoyButtonEvent(new JoyButtonEvent(button,true));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testJoyAxisEventNotEventsPermitted() {
        im.onJoyAxisEvent(new JoyAxisEvent(axis, 5));
    }
    
    @Test(expected=NullPointerException.class)
    public void testOnJoyButtonEventNoButton() {
    	ji.addEvent(new JoyButtonEvent(null,true));
    	assertEquals(false,ji.buttonQueue.get(0).isConsumed());
    	im.update(1);
    }
    
    @Test
    public void testOnJoyButtonEventPressed() {
    	ji.addEvent(new JoyButtonEvent(button,true));
    	
    	//test binding
        im.addMapping("button0", new JoyButtonTrigger(0, 0));
    	
    	ActionListenerTester acl = new ActionListenerTester();
    	AnalogListenerTester anl = new AnalogListenerTester();
    	
        im.addListener(acl, "button0");
        im.addListener(anl, "button0");
        
    	im.update(1);
    	
    	assertEquals(true, acl.onActionCalled);
    	assertEquals(1, acl.nrsCalled);

    	assertEquals(false, anl.onAnalogCalled);
    }
    
    @Test
    public void testOnJoyButtonEventNotPressed() {
    	ji.addEvent(new JoyButtonEvent(button,false));
    	assertEquals(false,ji.buttonQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ji.buttonQueue.get(0).isConsumed());
    }
    
    @Test
    public void testOnJoyButtonEventPressReleaseBindingTiming() {
    	//test binding
        im.addMapping("button0", new JoyButtonTrigger(0, 0));
    	
    	ji.addEvent(new JoyButtonEvent(button,true));
    	assertEquals(false,ji.buttonQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ji.buttonQueue.get(0).isConsumed());
    	
    	JoyButtonEvent je = new JoyButtonEvent(button, false);
    	je.setTime(1);
    	
    	ji.addEvent(je);
    	assertEquals(false,ji.buttonQueue.get(1).isConsumed());
    	im.update(1);
    	assertEquals(true,ji.buttonQueue.get(1).isConsumed());
    }
    
    @Test
    public void testOnJoyButtonEventPressTwiceBindingTiming() {
    	
    	TestKeyInput key = new TestKeyInput();    	
    	key.initialize();
    	
    	im = new InputManager(mi, key, ji, null);
    	
    	//test binding
        im.addMapping("button0", new JoyButtonTrigger(0, 0));
    	
    	ActionListenerTester acl = new ActionListenerTester();
    	AnalogListenerTester anl = new AnalogListenerTester();
    	
        im.addListener(acl, "button0");
        im.addListener(anl, "button0");
        
        ji.addEvent(new JoyButtonEvent(button,true));
        
    	im.update(1);
    	
    	assertEquals(true, acl.onActionCalled);
    	assertEquals(1, acl.nrsCalled);

    	assertEquals(false, anl.onAnalogCalled);
    	
    	ji.addEvent(new JoyButtonEvent(button,true));
    	
    	im.update(1);   
    	
    	assertEquals(true, acl.onActionCalled);
    	assertEquals(2, acl.nrsCalled);
    	assertEquals(1, anl.nrsCalled);
    	assertEquals(true, anl.onAnalogCalled);
    }

    @Test(expected=NullPointerException.class)
    public void testOnJoyAxisEventNoAxis() {
    	ji.addEvent(new JoyAxisEvent(null, 0));
    	assertEquals(false,ji.axisQueue.get(0).isConsumed());
    	im.update(1);
    }
    
    @Test
    public void testOnJoyAxisEventInDeadzone() {
    	ji.addEvent(new JoyAxisEvent(axis, 0.03F));
    	assertEquals(false,ji.axisQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ji.axisQueue.get(0).isConsumed());
    }
    
    @Test
    public void testOnJoyAxisEventMultiple() {
    	ji.addEvent(new JoyAxisEvent(axis, 0.1F));
    	assertEquals(false,ji.axisQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ji.axisQueue.get(0).isConsumed());    	

    	axis = new DefaultJoystickAxis(im, joystick, 1, "y", "y", true, true, 0);
    	
    	ji.addEvent(new JoyAxisEvent(axis,-0.1F));
    	assertEquals(false,ji.axisQueue.get(1).isConsumed());
    	im.update(1);
    	assertEquals(true,ji.axisQueue.get(1).isConsumed());
    	ji.addEvent(new JoyAxisEvent(axis, 0.04F));
    	assertEquals(false,ji.axisQueue.get(2).isConsumed());
    	im.update(1);
    	assertEquals(true,ji.axisQueue.get(2).isConsumed());
    	ji.addEvent(new JoyAxisEvent(axis, 0.03F));
    	assertEquals(false,ji.axisQueue.get(3).isConsumed());
    	im.update(1);
    	assertEquals(true,ji.axisQueue.get(3).isConsumed());
    }
    
    @Test
    public void testOnJoyAxisEventOutsideDeadzone() {
    	ji.addEvent(new JoyAxisEvent(axis, 0.1F));
    	assertEquals(false,ji.axisQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ji.axisQueue.get(0).isConsumed());
    }
    
    @Test
    public void testOnJoyAxisEventOutsideDeadzoneNegative() {
    	ji.addEvent(new JoyAxisEvent(axis, -0.1F));
    	assertEquals(false,ji.axisQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ji.axisQueue.get(0).isConsumed());
    }
    
    @Test
    public void testOnJoyAxisEventInDeadzoneBinding() {    	
    	//test binding
        im.addMapping("x", new JoyAxisTrigger(0, 0, false));
    	
    	ji.addEvent(new JoyAxisEvent(axis, 0.03F));
    	assertEquals(false,ji.axisQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ji.axisQueue.get(0).isConsumed());
    }
    
    @Test
    public void testOnJoyAxisEventOutsideDeadzoneBinding() {
    	//test binding
        im.addMapping("x", new JoyAxisTrigger(0, 0, false));

    	ji.addEvent(new JoyAxisEvent(axis, 0.1F));
    	
    	ActionListenerTester acl = new ActionListenerTester();
    	AnalogListenerTester anl = new AnalogListenerTester();
    	
        im.addListener(acl, "x");
        im.addListener(anl, "x");
        
    	im.update(1);
    	
    	assertEquals(true, acl.onActionCalled);
    	assertEquals(1, acl.nrsCalled);
    	assertEquals(2, anl.nrsCalled);

    	assertEquals(true, anl.onAnalogCalled);
    }
    
    @Test
    public void testOnJoyAxisEventOutsideDeadzoneNegativeBinding() {
    	//test binding
        im.addMapping("x", new JoyAxisTrigger(0, 0, false));
    	
    	ji.addEvent(new JoyAxisEvent(axis, -0.1F));
    	assertEquals(false,ji.axisQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ji.axisQueue.get(0).isConsumed());
    }
    
    
    @Test
    public void testSetAxisDeadzone(){
    	assertEquals(0.05f, im.getAxisDeadZone(),0.001);
    	im.setAxisDeadZone(0.1f);
    	assertEquals(0.1f, im.getAxisDeadZone(),0.001);
    }
    
    @Test
    public void testgetAxisDeadzone(){
    	assertEquals(0.05f, im.getAxisDeadZone(),0.001);
    }
    
    @Test 
    public void testGetJoysticks(){
        assertEquals(joystick.getName(),im.getJoysticks()[0].getName());
        assertEquals(joystick.getJoyId(),im.getJoysticks()[0].getJoyId());
        assertEquals(joystick.toString(),im.getJoysticks()[0].toString());
    }
}
