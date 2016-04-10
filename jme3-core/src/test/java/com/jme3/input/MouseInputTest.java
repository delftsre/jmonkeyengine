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
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.dummy.*;
import com.jme3.input.event.*;
import com.jme3.input.test.util.ActionListenerTester;
import com.jme3.input.test.util.AnalogListenerTester;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests functionality of input behavior of the mouse and events 
 * 
 * @author Willem Vaandrager
 */

public class MouseInputTest{
    
	public TestMouseInput mi;
	public KeyInput ki;
	public TouchInput ti;
	public JoyInput ji;
	public InputManager im;
	
    @Before
    public void setUp() {
    	ki = new DummyKeyInput();
        ki.initialize();
        
    	mi = new TestMouseInput();    	
        mi.initialize();
        
        
    	im = new InputManager(mi, ki, null, null);
    	im.addMapping("MouseL", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    	
    }    

    @Test(expected=UnsupportedOperationException.class)
    public void testOnMouseButtonEventNotEventsPermitted() {
        im.onMouseButtonEvent(new MouseButtonEvent(0, true, 5, 10));
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testOnMouseMotionEventNotEventsPermitted() {
        im.onMouseMotionEvent(new MouseMotionEvent(0, 0, 0, 0, 0, 0));
    }    
    
    @Test
    public void testOnLeftMouseButtonEvent() {
    	mi.addEvent(new MouseButtonEvent(0, true, 5, 10));
    	
    	ActionListenerTester acl = new ActionListenerTester();
    	AnalogListenerTester anl = new AnalogListenerTester();
    	
        im.addListener(acl, "MouseL");
        im.addListener(anl, "MouseL");
        
    	im.update(1);
    	
    	assertEquals(true, acl.onActionCalled);
    	assertEquals(1, acl.nrsCalled);

    	assertEquals(false, anl.onAnalogCalled);
    	
        assertEquals(5, im.getCursorPosition().getX(),0.001);
        assertEquals(10, im.getCursorPosition().getY(),0.001);
    }
    
    @Test
    public void testOnRightMouseButtonEvent() {
    	
    	mi.addEvent(new MouseButtonEvent(1, true, 10, 5));
    	
    	im.update(1);
    	
        assertEquals(10, im.getCursorPosition().getX(),0.001);
        assertEquals(5, im.getCursorPosition().getY(),0.001);
    }
    
    @Test
    public void testMouseMotion() {
        assertEquals(0, im.getCursorPosition().getX(),0.001);
        assertEquals(0, im.getCursorPosition().getY(),0.001);
        
    	mi.addEvent(new MouseMotionEvent(5,10,10,10,2,2));
    	
    	im.update(1);
    	
        assertEquals(5, im.getCursorPosition().getX(),0.001);
        assertEquals(10, im.getCursorPosition().getY(),0.001);
    }
    
    @Test
    public void testCursor(){    	
    	assertEquals(true,im.isCursorVisible());
    	
    	im.setMouseCursor(new JmeCursor());
    	assertEquals(true,im.isCursorVisible());
    	
    	im.setCursorVisible(false);    	
    	assertEquals(false,im.isCursorVisible());
    }
}
