package com.jme3.effect;

import com.jme3.math.Vector3f;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

public class ParticleEmitterTest {
    private ParticleEmitter pe;
    private Particle p;

    @Before
    public void init () {
        pe = new ParticleEmitter();
        p = new Particle();

        p.startlife = 10.0f;
        p.life = 10.0f;
    }

    @Test
    public void testUpdateParticle() {
        // we are only interested in the size parameter of our particle
        // the rest we will not touch

        pe.updateParticle(p, 0.0f, new Vector3f(), new Vector3f());
        // at the very start of the anim, the particle should be its start size
        assertThat(p.size, is(0.2f));
        // when some time has passed the particle should have a different size
        p.life -= 2.0f;
        pe.updateParticle(p, 2.0f, new Vector3f(), new Vector3f());
        assertThat(p.size, is(0.56f));
        // updating start-size should change the value
        pe.setStartSize(0.7f);
        // please note that there is no change in life here!
        pe.updateParticle(p, 0.0f, new Vector3f(), new Vector3f());
        assertThat(p.size, is(0.96000004f));
        // same goes for endSize
        pe.setEndSize(3.0f);
        pe.updateParticle(p, 0.0f, new Vector3f(), new Vector3f());
        assertThat(p.size, is(1.1600001f));
    }
}
