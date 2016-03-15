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
package com.jme3.effect;

import com.jme3.effect.influencers.ParticleInfluencer;
import com.jme3.scene.ISpatial;

/**
 *
 * @author Jaroslav Ševčík
 */
public interface IParticleEmitter extends ISpatial {

    /**
     * Instantly emits all the particles possible to be emitted. Any particles
     * which are currently inactive will be spawned immediately.
     */
    void emitAllParticles();

    /**
     * Instantly emits available particles, up to num.
     */
    void emitParticles(int num);

    int getMaxNumParticles();

    /**
     * Returns the mesh type used by the particle emitter.
     *
     *
     * @return the mesh type used by the particle emitter.
     *
     * @see #setMeshType(com.jme3.effect.ParticleMesh.Type)
     * @see ParticleEmitter#ParticleEmitter(java.lang.String, com.jme3.effect.ParticleMesh.Type, int)
     */
    ParticleMesh.Type getMeshType();

    /**
     * Returns the number of visible particles (spawned but not dead).
     *
     * @return the number of visible particles
     */
    int getNumVisibleParticles();

    /**
     * Returns the {@link ParticleInfluencer} that influences this
     * particle emitter.
     *
     * @return the {@link ParticleInfluencer} that influences this
     * particle emitter.
     *
     * @see ParticleInfluencer
     */
    ParticleInfluencer getParticleInfluencer();

    /**
     * Returns a list of all particles (shouldn't be used in most cases).
     *
     * <p>
     * This includes both existing and non-existing particles.
     * The size of the array is set to the <code>numParticles</code> value
     * specified in the constructor or {@link ParticleEmitter#setNumParticles(int) }
     * method.
     *
     * @return a list of all particles.
     */
    Particle[] getParticles();

    /**
     * Get the number of particles to spawn per
     * second.
     *
     * @return the number of particles to spawn per
     * second.
     *
     * @see ParticleEmitter#setParticlesPerSec(float)
     */
    float getParticlesPerSec();

    /**
     * Check if a particle emitter is enabled for update.
     *
     * @return True if a particle emitter is enabled for update.
     *
     * @see ParticleEmitter#setEnabled(boolean)
     */
    boolean isEnabled();

    /**
     * Instantly kills all active particles, after this method is called, all
     * particles will be dead and no longer visible.
     */
    void killAllParticles();

    /**
     * Kills the particle at the given index.
     *
     * @param index The index of the particle to kill
     * @see #getParticles()
     */
    void killParticle(int index);

    /**
     * Set to enable or disable the particle emitter
     *
     * <p>When a particle is
     * disabled, it will be "frozen in time" and not update.
     *
     * @param enabled True to enable the particle emitter
     */
    void setEnabled(boolean enabled);

    /**
     * Set the maximum amount of particles that
     * can exist at the same time with this emitter.
     * Calling this method many times is not recommended.
     *
     * @param numParticles the maximum amount of particles that
     * can exist at the same time with this emitter.
     */
    void setNumParticles(int numParticles);

    /**
     * Set the {@link ParticleInfluencer} to influence this particle emitter.
     *
     * @param particleInfluencer the {@link ParticleInfluencer} to influence
     * this particle emitter.
     *
     * @see ParticleInfluencer
     */
    void setParticleInfluencer(ParticleInfluencer particleInfluencer);

    /**
     * Set the number of particles to spawn per
     * second.
     *
     * @param particlesPerSec the number of particles to spawn per
     * second.
     */
    void setParticlesPerSec(float particlesPerSec);
    
}
