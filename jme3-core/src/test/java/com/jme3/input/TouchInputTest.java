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

import com.jme3.input.controls.TouchTrigger;
import com.jme3.input.dummy.*;
import com.jme3.input.event.*;
import com.jme3.input.event.TouchEvent.Type;
import com.jme3.input.test.util.ActionListenerTester;
import com.jme3.input.test.util.AnalogListenerTester;
import com.jme3.input.test.util.TouchListenerTester;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests functionality of input behavior of the mouse and events 
 * 
 * @author Willem Vaandrager
 */

public class TouchInputTest{
    
	public MouseInput mi;
	public KeyInput ki;
	public TestTouchInput ti;
	public InputManager im;
	
    @Before
    public void setUp() {
    	mi = new DummyMouseInput();
        mi.initialize();
        
    	ki = new DummyKeyInput();    	
        ki.initialize();
        
        ti = new TestTouchInput();
        ti.initialize();
        
    	im = new InputManager(mi, ki, null, ti);
    }    

    @Test(expected=UnsupportedOperationException.class)
    public void testTouchEventNotEventsPermitted() {
        im.onTouchEvent(new TouchEvent());
    }
    
    @Test
    public void testOnTouchEvent() {
    	ti.addEvent(new TouchEvent(Type.DOWN,5,10,5,10));
    	assertEquals(false,ti.eventQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ti.eventQueue.get(0).isConsumed());
    	
    	assertEquals(5, im.getCursorPosition().getX(),0.001);
        assertEquals(10, im.getCursorPosition().getY(),0.001);
    }
    
    @Test
    public void testOnTouchEventBinding() {
   	 	//test binding
    	im.addMapping("3", new TouchTrigger(TouchInput.KEYCODE_HOME));
    	
    	TouchEvent te = new TouchEvent(Type.DOWN,5,10,5,10);
    	te.setKeyCode(3);
    	
    	ti.addEvent(te);
    	
    	ActionListenerTester acl = new ActionListenerTester();
    	TouchListenerTester tcl = new TouchListenerTester();
    	
        im.addListener(acl, "3");
        im.addListener(tcl, "3");
        
    	im.update(1);
    	
    	assertEquals(false, acl.onActionCalled);
    	assertEquals(0, acl.nrsCalled);
    	assertEquals(1, tcl.nrsCalled);

    	assertEquals(true, tcl.onTouchCalled);
    	
    	assertEquals(5, im.getCursorPosition().getX(),0.001);
        assertEquals(10, im.getCursorPosition().getY(),0.001);
    }
    
    @SuppressWarnings("deprecation")
	@Test
    public void testSimulateMouse(){
    	assertEquals(false, im.isSimulateMouse());
    	
    	//deprecated method
    	assertEquals(false, im.getSimulateMouse());
    	
    	im.setSimulateMouse(true);
    	assertEquals(true, im.isSimulateMouse());
    	
    	im.setSimulateMouse(false);
    	assertEquals(false, im.isSimulateMouse());
    	
    }
    
    @Test
    public void testSimulateKeyboard(){
    	assertEquals(false, im.isSimulateKeyboard());
    	
    	im.setSimulateKeyboard(true);
    	assertEquals(true, im.isSimulateKeyboard());
    	
    	im.setSimulateKeyboard(false);
    	assertEquals(false, im.isSimulateKeyboard());
    }
    
    @SuppressWarnings("deprecation")
	@Test
    public void testSimulateMouseNoTouchDevice(){
    	im = new InputManager(mi, ki, null, null);
    	
    	im.setSimulateMouse(true);
    	assertEquals(false,im.isSimulateMouse());
    	
    	//deprecated method
    	assertEquals(false,im.getSimulateMouse());
    }
    
    @Test
    public void testSimulateKeyboardNoTouchDevice(){
    	im = new InputManager(mi, ki, null, null);
    	
    	im.setSimulateKeyboard(true);
    	assertEquals(false,im.isSimulateKeyboard());
    }   
}
