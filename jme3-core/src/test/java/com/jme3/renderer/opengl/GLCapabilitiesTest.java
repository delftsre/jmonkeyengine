package com.jme3.renderer.opengl;


import com.jme3.renderer.Caps;
import com.jme3.renderer.Limits;
import com.jme3.renderer.RenderContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.HashSet;

import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class GLCapabilitiesTest {

    private GLCapabilities c_gl2;
    private GLCapabilities c;
    @Mock
    private GL gl;
    @Mock
    private GL2 gl2;
    @Mock
    private GL3 gl3;
    @Mock
    private RenderContext context;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        c_gl2 = spy(new GLCapabilities());
        c = spy(new GLCapabilities());

        doReturn(new HashSet<String>()).when(c_gl2).loadExtensions();
        doReturn(new HashSet<String>()).when(c).loadExtensions();
    }

    @Test
    public void loadCapabilitiesWithGL2() throws Exception {
        doReturn(310).when(c_gl2).extractVersion(null);

        c_gl2.loadCapabilities(gl, gl2, gl3, context);

        String str = null;
        verify(c_gl2).loadCapabilitiesGL2(eq(str), eq(context));
        verify(c_gl2).loadCapabilitiesGLSL(eq(str));
        verify(c_gl2).loadCapabilitiesGLSL1Support();
        verify(c_gl2).loadCapabilitiesCommon();
    }

    @Test
    public void loadCapabilitiesWithOutGL2() throws Exception {
        doReturn(310).when(c).extractVersion(null);

        c.loadCapabilities(gl, null, gl3, context);

        verify(c).loadCapabilitiesES();
        verify(c).loadCapabilitiesCommon();
    }

    @Test
    public void loadCapabilitiesGL2WithRealVersionNumber() throws Exception {
        doReturn(310).when(c_gl2).extractVersion(null);

        c_gl2.loadCapabilities(gl, gl2, gl3, context);

        assertTrue(c_gl2.caps.contains(Caps.OpenGL20));
        assertTrue(c_gl2.caps.contains(Caps.OpenGL21));
        assertTrue(c_gl2.caps.contains(Caps.OpenGL30));
        assertTrue(c_gl2.caps.contains(Caps.OpenGL31));
        assertFalse(c_gl2.caps.contains(Caps.OpenGL32));
        assertFalse(c_gl2.caps.contains(Caps.OpenGL33));
        assertFalse(c_gl2.caps.contains(Caps.OpenGL40));

        assertTrue(context.initialDrawBuf == c_gl2.getInteger(GL2.GL_DRAW_BUFFER));
        assertTrue(context.initialReadBuf == c_gl2.getInteger(GL2.GL_READ_BUFFER));
    }

    @Test
    public void loadCapabilitiesGL2NotRealVersion() throws Exception {
        doReturn(213).when(c_gl2).extractVersion(null);

        c_gl2.loadCapabilities(gl, gl2, gl3, context);

        assertTrue(c_gl2.caps.contains(Caps.OpenGL20));
        assertTrue(c_gl2.caps.contains(Caps.OpenGL21));
        assertFalse(c_gl2.caps.contains(Caps.OpenGL30));
        assertFalse(c_gl2.caps.contains(Caps.OpenGL31));
        assertFalse(c_gl2.caps.contains(Caps.OpenGL32));
        assertFalse(c_gl2.caps.contains(Caps.OpenGL33));
        assertFalse(c_gl2.caps.contains(Caps.OpenGL40));
    }

    @Test
    public void loadCapabilitiesGLSLWithRealVersionNumber() throws Exception {
        doReturn(150).when(c_gl2).extractVersion(null);

        c_gl2.loadCapabilities(gl, gl2, gl3, context);

        assertFalse(c_gl2.caps.contains(Caps.GLSL400));
        assertFalse(c_gl2.caps.contains(Caps.GLSL330));
        assertTrue(c_gl2.caps.contains(Caps.GLSL150));
        assertTrue(c_gl2.caps.contains(Caps.GLSL140));
        assertTrue(c_gl2.caps.contains(Caps.GLSL130));
        assertTrue(c_gl2.caps.contains(Caps.GLSL120));
        assertTrue(c_gl2.caps.contains(Caps.GLSL110));
        assertTrue(c_gl2.caps.contains(Caps.GLSL100));
    }

    @Test
    public void loadCapabilitiesGLSLNotRealVersion() throws Exception {
        doReturn(124).when(c_gl2).extractVersion(null);

        c_gl2.loadCapabilities(gl, gl2, gl3, context);

        assertFalse(c_gl2.caps.contains(Caps.GLSL400));
        assertFalse(c_gl2.caps.contains(Caps.GLSL330));
        assertFalse(c_gl2.caps.contains(Caps.GLSL150));
        assertFalse(c_gl2.caps.contains(Caps.GLSL140));
        assertFalse(c_gl2.caps.contains(Caps.GLSL130));
        assertTrue(c_gl2.caps.contains(Caps.GLSL120));
        assertTrue(c_gl2.caps.contains(Caps.GLSL110));
        assertTrue(c_gl2.caps.contains(Caps.GLSL100));
    }

    @Test
    public void loadCapabilitiesGLSL1Support() throws Exception {
        doReturn(310).when(c_gl2).extractVersion(null);

        c_gl2.loadCapabilities(gl, gl2, gl3, context);

        assertTrue(c_gl2.caps.contains(Caps.GLSL110));
        assertTrue(c_gl2.caps.contains(Caps.GLSL100));
    }

    @Test
    public void loadCapabilitiesES() throws Exception {
        doReturn(310).when(c).extractVersion(null);

        c.loadCapabilities(gl, null, gl3, context);

        assertTrue(c.caps.contains(Caps.GLSL100));
        assertTrue(c.caps.contains(Caps.OpenGLES20));
    }

    @Test
    public void loadCapabilitiesCommon() {
        doReturn(310).when(c_gl2).extractVersion(null);

        c_gl2.loadCapabilities(gl, gl2, gl3, context);

        verify(c_gl2).loadExtensions();
        assertTrue(c_gl2.limits.get(Limits.VertexTextureUnits) == c_gl2.getInteger(GL.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS));
        assertFalse(c_gl2.caps.contains(Caps.VertexTextureFetch));
        assertTrue(c_gl2.limits.get(Limits.FragmentTextureUnits) == c_gl2.getInteger(GL.GL_MAX_TEXTURE_IMAGE_UNITS));
        assertTrue(c_gl2.limits.get(Limits.VertexUniformVectors) == c_gl2.getInteger(GL.GL_MAX_VERTEX_UNIFORM_VECTORS));
        assertTrue(c_gl2.limits.get(Limits.VertexAttributes) == c_gl2.getInteger(GL.GL_MAX_VERTEX_ATTRIBS));
        assertTrue(c_gl2.limits.get(Limits.TextureSize) == c_gl2.getInteger(GL.GL_MAX_TEXTURE_SIZE));
        assertTrue(c_gl2.limits.get(Limits.CubemapSize) == c_gl2.getInteger(GL.GL_MAX_CUBE_MAP_TEXTURE_SIZE));
        assertFalse(c_gl2.caps.contains(Caps.MeshInstancing));
        assertTrue(c_gl2.caps.contains(Caps.IntegerIndexBuffer));
        assertFalse(c_gl2.caps.contains(Caps.TextureBuffer));

        // texture format
        assertTrue(c_gl2.caps.contains(Caps.OpenGL30));
        assertTrue(c_gl2.caps.contains(Caps.FloatTexture));
        assertTrue(c_gl2.caps.contains(Caps.DepthTexture));
        assertFalse(c_gl2.caps.contains(Caps.Rgba8));
        assertTrue(c_gl2.caps.contains(Caps.PackedDepthStencilBuffer));
        assertFalse(c_gl2.caps.contains(Caps.FloatColorBuffer));
        assertFalse(c_gl2.caps.contains(Caps.FloatDepthBuffer));
        assertTrue(c_gl2.caps.contains(Caps.PackedFloatColorBuffer));
        assertTrue(c_gl2.caps.contains(Caps.PackedFloatTexture));
        assertTrue(c_gl2.caps.contains(Caps.SharedExponentTexture));
        assertFalse(c_gl2.caps.contains(Caps.TextureCompressionS3TC));
        assertFalse(c_gl2.caps.contains(Caps.TextureCompressionETC2));
        assertFalse(c_gl2.caps.contains(Caps.TextureCompressionETC1));
        assertFalse(c_gl2.caps.contains(Caps.TextureCompressionETC1));
        // end texture format

        assertTrue(c_gl2.caps.contains(Caps.VertexBufferArray));
        assertTrue(c_gl2.caps.contains(Caps.NonPowerOfTwoTextures));
        assertFalse(c_gl2.caps.contains(Caps.PartialNonPowerOfTwoTextures));
        assertTrue(c_gl2.caps.contains(Caps.TextureArray));
        assertFalse(c_gl2.caps.contains(Caps.TextureFilterAnisotropic));
        assertFalse(c_gl2.limits.containsKey(Limits.TextureAnisotropy));

        // big if statement
        assertTrue(c_gl2.caps.contains(Caps.FrameBuffer));
        assertTrue(c_gl2.limits.get(Limits.RenderBufferSize) == c_gl2.getInteger(GLFbo.GL_MAX_RENDERBUFFER_SIZE_EXT));
        assertTrue(c_gl2.limits.get(Limits.FrameBufferAttachments) == c_gl2.getInteger(GLFbo.GL_MAX_COLOR_ATTACHMENTS_EXT));
        assertTrue(c_gl2.caps.contains(Caps.FrameBufferBlit));
        assertFalse(c_gl2.caps.contains(Caps.FrameBufferMultisample));
        assertFalse(c_gl2.limits.containsKey(Limits.FrameBufferSamples));
        assertFalse(c_gl2.caps.contains(Caps.TextureMultisample));
        assertFalse(c_gl2.limits.containsKey(Limits.ColorTextureSamples));
        assertFalse(c_gl2.limits.containsKey(Limits.DepthTextureSamples));
        assertTrue(c_gl2.limits.get(Limits.FrameBufferMrtAttachments) == c_gl2.getInteger(GLExt.GL_MAX_DRAW_BUFFERS_ARB));
        assertFalse(c_gl2.caps.contains(Caps.FrameBufferMRT));
        // end big if statement

        assertFalse(c_gl2.caps.contains(Caps.Multisample));
        assertTrue(c_gl2.caps.contains(Caps.Srgb));
        assertFalse(c_gl2.caps.contains(Caps.SeamlessCubemap));
        assertFalse(c_gl2.caps.contains(Caps.CoreProfile));
        assertFalse(c_gl2.caps.contains(Caps.BinaryShader));
    }

    @Test
    public void testHas() {
        Caps test = Caps.Multisample;
        assertFalse(c.has(test));

        c.caps.add(test);

        assertTrue(c.has(test));
    }

    @Test
    public void testHasNot() {
        Caps test = Caps.Multisample;
        assertFalse(c.has(test));
    }
}
