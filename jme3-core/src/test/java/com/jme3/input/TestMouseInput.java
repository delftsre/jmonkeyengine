/*
 * Copyright (c) 2009-2012 jMonkeyEngine
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

import java.util.ArrayList;

import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;

/**
 * TestMouseInput as an implementation of <code>MouseInput</code> to simulate mouse for tests.
 *
 * @author Willem Vaandrager.
 */
public class TestMouseInput implements MouseInput {

    protected boolean inited = false;
    
    public ArrayList<MouseButtonEvent> eventQueue = new ArrayList<MouseButtonEvent>();
    public ArrayList<MouseMotionEvent> motionQueue = new ArrayList<MouseMotionEvent>();
	
    private RawInputListener listener;

    public void initialize() {
        if (inited)
            throw new IllegalStateException("Input already initialized.");

        inited = true;
    }
    
    public void setCursorVisible(boolean visible) {
        if (!inited)
            throw new IllegalStateException("Input not initialized.");
    }

    public int getButtonCount() {
        return 3;
    }
    
    public void addEvent(MouseButtonEvent evt){
    	eventQueue.add(evt);
    }
    
    public void addEvent(MouseMotionEvent evt){
    	motionQueue.add(evt);
    }
    
    public void setInputListener(RawInputListener listener) {
        this.listener = listener;
    }
    
    public RawInputListener getInputListener() {
        return listener;
    }

    public void update() {
        if (!inited)
            throw new IllegalStateException("Input not initialized.");
        
        for (MouseButtonEvent evt : eventQueue) {
            listener.onMouseButtonEvent(evt);
            
		}
        for (MouseMotionEvent evt : motionQueue) {
            listener.onMouseMotionEvent(evt);
		}
    }
    
    public void setNativeCursor(JmeCursor cursor) {
    }

    public void destroy() {
        if (!inited)
            throw new IllegalStateException("Input not initialized.");

        inited = false;
    }

    public boolean isInitialized() {
        return inited;
    }

    public long getInputTimeNanos() {
        return System.currentTimeMillis() * 1000000;
    }
}

