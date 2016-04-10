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


import com.jme3.input.InputManager;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.dummy.DummyKeyInput;
import com.jme3.input.dummy.DummyMouseInput;
import com.jme3.test.util.TestLogHandler;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Tests functionality of input behaviour as: http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=264
 * 
 * @author 
 */
public class InputManagerMappingTest {
	
	public InputManager inputManager;
	public TestLogHandler tlh;
    
    @Before
    public void setUp() {
    	inputManager = new InputManager(new DummyMouseInput(), new DummyKeyInput(), null, null);
    	
    	Logger logger = Logger.getLogger(InputManager.class.getName());
    	tlh = new TestLogHandler();
    	tlh.setLevel(Level.ALL);
    	logger.setUseParentHandlers(false);
    	logger.addHandler(tlh);
    	logger.setLevel(Level.ALL);
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalid1()
    {
    	inputManager = new InputManager(null, new DummyKeyInput(), null, null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorInvalid2()
    {
    	inputManager = new InputManager(new DummyMouseInput(), null, null, null);
    }
    
    @Test
    public void testAddNewMapping()
    {
    	inputManager.addMapping("testMapping", new KeyTrigger(0));
    	assertTrue(inputManager.hasMapping("testMapping"));
    }
    
    @Test
    public void testAddExistingMapping()
    {
    	inputManager.addMapping("testMapping", new KeyTrigger(0));
    	inputManager.addMapping("testMapping", new KeyTrigger(1));
    	assertTrue(inputManager.hasMapping("testMapping"));
    }
    
    @Test
    public void testAddTriggerTwice()
    {
    	inputManager.addMapping("testMapping", new KeyTrigger(0));
    	inputManager.addMapping("testMapping", new KeyTrigger(0));
    	assertTrue(inputManager.hasMapping("testMapping"));
    	assertEquals(1, tlh.getNrOfLoggedMessages());
    }
    
    @Test
    public void testRemoveMappingExisting()
    {
    	inputManager.addMapping("testMapping", new KeyTrigger(5));
    	inputManager.deleteMapping("testMapping");
    	assertFalse(inputManager.hasMapping("testMapping"));
    }
    
    @Test
    public void testRemoveMappingNonExistent()
    {
    	inputManager.addMapping("testMapping", new KeyTrigger(1));
    	inputManager.deleteMapping("test");
    	assertEquals(1, tlh.getNrOfLoggedMessages());
    }
    
    @Test
    public void testClearMapping()
    {
    	inputManager.addMapping("testMapping1", new KeyTrigger(8));
    	inputManager.addMapping("testMapping2", new KeyTrigger(9));
    	inputManager.clearMappings();
    	assertFalse(inputManager.hasMapping("testMapping1"));
    	assertFalse(inputManager.hasMapping("testMapping2"));
    }
}
