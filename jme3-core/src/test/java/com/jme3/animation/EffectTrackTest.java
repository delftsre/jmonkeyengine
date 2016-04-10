/*
 * Copyright (c) 2009-2016 jMonkeyEngine
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

package com.jme3.animation;

import com.jme3.effect.IParticleEmitter;
import com.jme3.effect.ParticleEmitter;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Jaroslav Ševčík
 */
public class EffectTrackTest extends EffectTrack {
    
    @Test
    public void testfindEmitter () {
        Spatial spatial = new Geometry();
        IParticleEmitter emitter = findEmitter(spatial);
        Assert.assertNull("Method findEmitter(Spatial) returned instance of IParticleEmitter instead of null when the Spatial parameter is not ParticleEmitter nor Node.", emitter);
        
        spatial = new ParticleEmitter();
        emitter = findEmitter(spatial);
        Assert.assertNull("Method findEmitter(Spatial) returned instance of IParticleEmitter instead of null when there is not TrackInfo.", emitter);
        
        TrackInfo t = new TrackInfo();
        spatial.setUserData("TrackInfo", t);
        emitter = findEmitter(spatial);
        Assert.assertNull("Method findEmitter(Spatial) returned instance of IParticleEmitter instead of null when TrackInfo does not contain this.", emitter);
        
        t = new TrackInfo();
        t.addTrack(this);
        spatial.setUserData("TrackInfo", t);
        emitter = findEmitter(spatial);
        Assert.assertNotNull("Method findEmitter(Spatial) returned null instead of IParticleEmitter.", emitter);
        Assert.assertSame("Method findEmitter(Spatial) returned different instance of IParticleEmitter.", spatial, emitter);
        
        Node node1 = new Node();
        Node node2 = new Node();
        node2.attachChild(spatial);
        node1.attachChild(node2);
        emitter = findEmitter(spatial);
        Assert.assertNotNull("Method findEmitter(Spatial) returned null instead of IParticleEmitter when nested.", emitter);
        Assert.assertSame("Method findEmitter(Spatial) returned different instance of IParticleEmitter when nested.", spatial, emitter);
    }
}
