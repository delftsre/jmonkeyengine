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

import com.jme3.input.controls.*;
import com.jme3.input.dummy.*;
import com.jme3.input.event.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests functionality of input behavior of the mouse and events 
 * 
 * @author Willem Vaandrager
 */

public class KeysInputTest{
    
	public MouseInput mi;
	public TestKeyInput ki;
	public InputManager im;
	
    @Before
    public void setUp() {
    	mi = new DummyMouseInput();
        mi.initialize();
        
    	ki = new TestKeyInput();    	
        ki.initialize();
        
    	im = new InputManager(mi, ki, null, null);
    	
        //test binding
        im.addMapping("1", new KeyTrigger(1));
    }    

    @Test(expected=UnsupportedOperationException.class)
    public void testKeyInputEventNotEventsPermitted() {
        im.onKeyEvent(new KeyInputEvent(1, '1', true, false));
    }
    
    @Test
    public void testOnKeyInputEventNoBinding() {
    	ki.addEvent(new KeyInputEvent(2, '2', true, false));
    	assertEquals(false,ki.eventQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ki.eventQueue.get(0).isConsumed());
    }
    
    @Test
    public void testOnKeyInputEventBinding() {
    	ki.addEvent(new KeyInputEvent(1, '1', true, false));
    	assertEquals(false,ki.eventQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ki.eventQueue.get(0).isConsumed());
    }
    
    @Test
    public void testOnKeyInputEventNotPressed() {
    	ki.addEvent(new KeyInputEvent(1, '1', false, false));
    	assertEquals(false,ki.eventQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ki.eventQueue.get(0).isConsumed());
    }
    
    @Test
    public void testOnKeyInputEventPressRelease() {    	
    	ki.addEvent(new KeyInputEvent(1, '1', true, false));
    	assertEquals(false,ki.eventQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ki.eventQueue.get(0).isConsumed());
    	
    	ki.addEvent(new KeyInputEvent(1, '1', false, false));
    	assertEquals(false,ki.eventQueue.get(1).isConsumed());
    	im.update(1);
    	assertEquals(true,ki.eventQueue.get(1).isConsumed());
    	
    	ki.addEvent(new KeyInputEvent(1, '1', true, false));
    	assertEquals(false,ki.eventQueue.get(2).isConsumed());
    	im.update(1);
    	assertEquals(true,ki.eventQueue.get(2).isConsumed());  
    	
    	ki.addEvent(new KeyInputEvent(1, '1', false, false));
    	assertEquals(false,ki.eventQueue.get(3).isConsumed());
    	im.update(1);
    	assertEquals(true,ki.eventQueue.get(3).isConsumed());
    }
    
    @Test
    public void testOnKeyInputEventRepeating() {
    	ki.addEvent(new KeyInputEvent(1, '1', true, true));
    	assertEquals(false,ki.eventQueue.get(0).isConsumed());
    	im.update(1);
    	assertEquals(true,ki.eventQueue.get(0).isConsumed());
    }
}
