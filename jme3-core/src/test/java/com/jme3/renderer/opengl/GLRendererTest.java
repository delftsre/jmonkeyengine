package com.jme3.renderer.opengl;

import com.jme3.renderer.Caps;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.EnumSet;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.*;

public class GLRendererTest {

    private GLRenderer r;
    @Mock
    private GL2 gl;
    @Mock
    private GLExt glExt;
    @Mock
    private GLFbo glFbo;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        r = spy(new GLRenderer(gl, glExt, glFbo));
    }

    @Test
    public void testCapabilitiesInitialized() {
        assertNotNull(r.getCaps());
    }

    @Test
    public void testInitialize() {
        doNothing().when(r).loadCapabilities();

        r.initialize();

        verify(r, times(1)).loadCapabilities();
    }

    @Test
    public void testGetCaps() {
        EnumSet<Caps> expected = r.glCaps.caps;
        EnumSet<Caps> actual = r.getCaps();

        assertThat(actual, is(expected));
    }

}