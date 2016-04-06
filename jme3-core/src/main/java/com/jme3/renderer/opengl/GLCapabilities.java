package com.jme3.renderer.opengl;

import com.jme3.renderer.Caps;
import com.jme3.renderer.Limits;
import com.jme3.renderer.RenderContext;
import com.jme3.util.BufferUtils;

import java.nio.IntBuffer;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

public class GLCapabilities {

    private static final Logger logger = Logger.getLogger(GLCapabilities.class.getName());
    protected final EnumSet<Caps> caps = EnumSet.noneOf(Caps.class);
    private static final Pattern GLVERSION_PATTERN = Pattern.compile(".*?(\\d+)\\.(\\d+).*");
    protected final EnumMap<Limits, Integer> limits = new EnumMap<>(Limits.class);

    private final IntBuffer intBuf16 = BufferUtils.createIntBuffer(16);
    private GL gl;
    private GL2 gl2;
    private GL3 gl3;
    private HashSet<String> extensions;

    public void loadCapabilities(GL gl, GL2 gl2, GL3 gl3, RenderContext context) {
        this.gl = gl;
        this.gl2 = gl2;
        this.gl3 = gl3;
        if (gl2 != null) {
            loadCapabilitiesGL2(gl.glGetString(GL.GL_VERSION), context);
            loadCapabilitiesGLSL(gl.glGetString(GL.GL_SHADING_LANGUAGE_VERSION));
            loadCapabilitiesGLSL1Support();
        } else {
            loadCapabilitiesES();
        }
        loadCapabilitiesCommon();
    }

    /**
     * Load Embedded System Capabilities.
     * Important: Do not add OpenGL20 - that's the desktop capability!
     */
    protected void loadCapabilitiesES() {
        caps.add(Caps.GLSL100);
        caps.add(Caps.OpenGLES20);
    }

    protected void loadCapabilitiesGL2(String glVersion, RenderContext context) {
        int oglVer = extractVersion(glVersion);
        Map<Integer, List<Caps>> versionCapMap = getGLVersionCapabilityMapping();
        addCapabilitiesBasedOnVersion(versionCapMap, oglVer);


        // Fix issue in TestRenderToMemory when GL.GL_FRONT is the main
        // buffer being used.
        context.initialDrawBuf = getInteger(GL2.GL_DRAW_BUFFER);
        context.initialReadBuf = getInteger(GL2.GL_READ_BUFFER);
    }

    /**
     * Load GL Shading Language capabilities
     */
    protected void loadCapabilitiesGLSL(String glslVersion) {
        int glslVer = extractVersion(glslVersion);
        Map<Integer, List<Caps>> versionCapMap = getGSLVersionCapabilityMapping();
        addCapabilitiesBasedOnVersion(versionCapMap, glslVer);
    }

    /**
     * Workaround, always assume we support GLSL100 & GLSL110
     * Supporting OpenGL 2.0 means supporting GLSL 1.10.
     */
    protected void loadCapabilitiesGLSL1Support() {
        caps.add(Caps.GLSL110);
        caps.add(Caps.GLSL100);
    }

    protected void loadCapabilitiesCommon() {
        extensions = loadExtensions();

        limits.put(Limits.VertexTextureUnits, getInteger(GL.GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS));
        if (limits.get(Limits.VertexTextureUnits) > 0) {
            caps.add(Caps.VertexTextureFetch);
        }

        limits.put(Limits.FragmentTextureUnits, getInteger(GL.GL_MAX_TEXTURE_IMAGE_UNITS));

        if (caps.contains(Caps.OpenGLES20)) {
            limits.put(Limits.VertexUniformVectors, getInteger(GL.GL_MAX_VERTEX_UNIFORM_VECTORS));
        } else {
            limits.put(Limits.VertexUniformVectors, getInteger(GL.GL_MAX_VERTEX_UNIFORM_COMPONENTS) / 4);
        }
        limits.put(Limits.VertexAttributes, getInteger(GL.GL_MAX_VERTEX_ATTRIBS));
        limits.put(Limits.TextureSize, getInteger(GL.GL_MAX_TEXTURE_SIZE));
        limits.put(Limits.CubemapSize, getInteger(GL.GL_MAX_CUBE_MAP_TEXTURE_SIZE));

        if (hasExtension("GL_ARB_draw_instanced") &&
                hasExtension("GL_ARB_instanced_arrays")) {
            caps.add(Caps.MeshInstancing);
        }

        if (hasExtension("GL_OES_element_index_uint") || gl2 != null) {
            caps.add(Caps.IntegerIndexBuffer);
        }

        if (hasExtension("GL_ARB_texture_buffer_object")) {
            caps.add(Caps.TextureBuffer);
        }

        loadTextureFormatExtensions();

        if (hasExtension("GL_ARB_vertex_array_object") || caps.contains(Caps.OpenGL30)) {
            caps.add(Caps.VertexBufferArray);
        }

        if (hasExtension("GL_ARB_texture_non_power_of_two") ||
                hasExtension("GL_OES_texture_npot") ||
                caps.contains(Caps.OpenGL30)) {
            caps.add(Caps.NonPowerOfTwoTextures);
        } else {
            logger.log(Level.WARNING, "Your graphics card does not "
                    + "support non-power-of-2 textures. "
                    + "Some features might not work.");
        }

        if (caps.contains(Caps.OpenGLES20)) {
            // OpenGL ES 2 has some limited support for NPOT textures
            caps.add(Caps.PartialNonPowerOfTwoTextures);
        }

        if (hasExtension("GL_EXT_texture_array") || caps.contains(Caps.OpenGL30)) {
            caps.add(Caps.TextureArray);
        }

        if (hasExtension("GL_EXT_texture_filter_anisotropic")) {
            caps.add(Caps.TextureFilterAnisotropic);
            limits.put(Limits.TextureAnisotropy, getInteger(GLExt.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT));
        }

        if (hasExtension("GL_EXT_framebuffer_object")
                || caps.contains(Caps.OpenGL30)
                || caps.contains(Caps.OpenGLES20)) {
            loadGLExtFrameBufferObjectCapabilities();
        }

        if (hasExtension("GL_ARB_multisample")) {
            boolean available = getInteger(GLExt.GL_SAMPLE_BUFFERS_ARB) != 0;
            int samples = getInteger(GLExt.GL_SAMPLES_ARB);
            logger.log(Level.FINER, "Samples: {0}", samples);
            boolean enabled = gl.glIsEnabled(GLExt.GL_MULTISAMPLE_ARB);
            if (samples > 0 && available && !enabled) {
                // Doesn't seem to be neccessary .. OGL spec says its always
                // set by default?
                gl.glEnable(GLExt.GL_MULTISAMPLE_ARB);
            }
            caps.add(Caps.Multisample);
        }

        // Supports sRGB pipeline.
        if ( (hasExtension("GL_ARB_framebuffer_sRGB") && hasExtension("GL_EXT_texture_sRGB"))
                || caps.contains(Caps.OpenGL30) ) {
            caps.add(Caps.Srgb);
        }

        // Supports seamless cubemap
        if (hasExtension("GL_ARB_seamless_cube_map") || caps.contains(Caps.OpenGL32)) {
            caps.add(Caps.SeamlessCubemap);
        }

        if (caps.contains(Caps.OpenGL32) && !hasExtension("GL_ARB_compatibility")) {
            caps.add(Caps.CoreProfile);
        }

        if (hasExtension("GL_ARB_get_program_binary")) {
            int binaryFormats = getInteger(GLExt.GL_NUM_PROGRAM_BINARY_FORMATS);
            if (binaryFormats > 0) {
                caps.add(Caps.BinaryShader);
            }
        }

        // Print context information
        logger.log(Level.INFO, "OpenGL Renderer Information\n" +
                        " * Vendor: {0}\n" +
                        " * Renderer: {1}\n" +
                        " * OpenGL Version: {2}\n" +
                        " * GLSL Version: {3}\n" +
                        " * Profile: {4}",
                new Object[]{
                        gl.glGetString(GL.GL_VENDOR),
                        gl.glGetString(GL.GL_RENDERER),
                        gl.glGetString(GL.GL_VERSION),
                        gl.glGetString(GL.GL_SHADING_LANGUAGE_VERSION),
                        caps.contains(Caps.CoreProfile) ? "Core" : "Compatibility"
                });

        // Print capabilities (if fine logging is enabled)
        if (logger.isLoggable(Level.FINE)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Supported capabilities: \n");
            for (Caps cap : caps)
            {
                sb.append("\t").append(cap.toString()).append("\n");
            }
            logger.log(Level.FINE, sb.toString());
        }
    }

    private void loadGLExtFrameBufferObjectCapabilities() {
        caps.add(Caps.FrameBuffer);

        limits.put(Limits.RenderBufferSize, getInteger(GLFbo.GL_MAX_RENDERBUFFER_SIZE_EXT));
        limits.put(Limits.FrameBufferAttachments, getInteger(GLFbo.GL_MAX_COLOR_ATTACHMENTS_EXT));

        if (hasExtension("GL_EXT_framebuffer_blit") || caps.contains(Caps.OpenGL30)) {
            caps.add(Caps.FrameBufferBlit);
        }

        if (hasExtension("GL_EXT_framebuffer_multisample")) {
            caps.add(Caps.FrameBufferMultisample);
            limits.put(Limits.FrameBufferSamples, getInteger(GLExt.GL_MAX_SAMPLES_EXT));
        }

        if (hasExtension("GL_ARB_texture_multisample")) {
            caps.add(Caps.TextureMultisample);
            limits.put(Limits.ColorTextureSamples, getInteger(GLExt.GL_MAX_COLOR_TEXTURE_SAMPLES));
            limits.put(Limits.DepthTextureSamples, getInteger(GLExt.GL_MAX_DEPTH_TEXTURE_SAMPLES));
            if (!limits.containsKey(Limits.FrameBufferSamples)) {
                // In case they want to query samples on main FB ...
                limits.put(Limits.FrameBufferSamples, limits.get(Limits.ColorTextureSamples));
            }
        }

        if (hasExtension("GL_ARB_draw_buffers") || caps.contains(Caps.OpenGL30)) {
            limits.put(Limits.FrameBufferMrtAttachments, getInteger(GLExt.GL_MAX_DRAW_BUFFERS_ARB));
            if (limits.get(Limits.FrameBufferMrtAttachments) > 1) {
                caps.add(Caps.FrameBufferMRT);
            }
        } else {
            limits.put(Limits.FrameBufferMrtAttachments, 1);
        }
    }

    private void loadTextureFormatExtensions() {
        boolean hasFloatTexture;

        hasFloatTexture = hasExtension("GL_OES_texture_half_float") &&
                hasExtension("GL_OES_texture_float");

        if (!hasFloatTexture) {
            hasFloatTexture = hasExtension("GL_ARB_texture_float") &&
                    hasExtension("GL_ARB_half_float_pixel");

            if (!hasFloatTexture) {
                hasFloatTexture = caps.contains(Caps.OpenGL30);
            }
        }

        if (hasFloatTexture) {
            caps.add(Caps.FloatTexture);
        }

        if (hasExtension("GL_OES_depth_texture") || gl2 != null) {
            caps.add(Caps.DepthTexture);

            // TODO: GL_OES_depth24
        }

        if (hasExtension("GL_OES_rgb8_rgba8") ||
                hasExtension("GL_ARM_rgba8") ||
                hasExtension("GL_EXT_texture_format_BGRA8888")) {
            caps.add(Caps.Rgba8);
        }

        if (caps.contains(Caps.OpenGL30) || hasExtension("GL_OES_packed_depth_stencil")) {
            caps.add(Caps.PackedDepthStencilBuffer);
        }

        if (hasExtension("GL_ARB_color_buffer_float") &&
                hasExtension("GL_ARB_half_float_pixel")) {
            // XXX: Require both 16 and 32 bit float support for FloatColorBuffer.
            caps.add(Caps.FloatColorBuffer);
        }

        if (hasExtension("GL_ARB_depth_buffer_float")) {
            caps.add(Caps.FloatDepthBuffer);
        }

        if ((hasExtension("GL_EXT_packed_float") && hasFloatTexture) ||
                caps.contains(Caps.OpenGL30)) {
            // Either OpenGL3 is available or both packed_float & half_float_pixel.
            caps.add(Caps.PackedFloatColorBuffer);
            caps.add(Caps.PackedFloatTexture);
        }

        if (hasExtension("GL_EXT_texture_shared_exponent") || caps.contains(Caps.OpenGL30)) {
            caps.add(Caps.SharedExponentTexture);
        }

        if (hasExtension("GL_EXT_texture_compression_s3tc")) {
            caps.add(Caps.TextureCompressionS3TC);
        }

        if (hasExtension("GL_ARB_ES3_compatibility")) {
            caps.add(Caps.TextureCompressionETC2);
            caps.add(Caps.TextureCompressionETC1);
        } else if (hasExtension("GL_OES_compressed_ETC1_RGB8_texture")) {
            caps.add(Caps.TextureCompressionETC1);
        }
    }

    public int extractVersion(String version) {
        Matcher m = GLVERSION_PATTERN.matcher(version);
        if (m.matches()) {
            int major = Integer.parseInt(m.group(1));
            int minor = Integer.parseInt(m.group(2));
            if (minor >= 10 && minor % 10 == 0) {
                // some versions can look like "1.30" instead of "1.3".
                // make sure to correct for this
                minor /= 10;
            }
            return major * 100 + minor * 10;
        } else {
            return -1;
        }
    }

    protected HashSet<String> loadExtensions() {
        HashSet<String> extensionSet = new HashSet<String>(64);
        if (getCaps().contains(Caps.OpenGL30)) {
            // If OpenGL3+ is available, use the non-deprecated way
            // of getting supported extensions.
            gl3.glGetInteger(GL3.GL_NUM_EXTENSIONS, intBuf16);
            int extensionCount = intBuf16.get(0);
            for (int i = 0; i < extensionCount; i++) {
                String extension = gl3.glGetString(GL.GL_EXTENSIONS, i);
                extensionSet.add(extension);
            }
        } else {
            extensionSet.addAll(asList(gl.glGetString(GL.GL_EXTENSIONS).split(" ")));
        }
        return extensionSet;
    }

    public Map<Integer,List<Caps>> getGLVersionCapabilityMapping() {

        Map<Integer,List<Caps>> map = new TreeMap<>();

        map.put(200, Collections.singletonList(Caps.OpenGL20));
        map.put(210, Collections.singletonList(Caps.OpenGL21));
        map.put(300, Collections.singletonList(Caps.OpenGL30));
        map.put(310, Collections.singletonList(Caps.OpenGL31));
        map.put(320, Collections.singletonList(Caps.OpenGL32));
        map.put(330, asList(Caps.OpenGL33, Caps.GeometryShader));
        map.put(400, asList(Caps.OpenGL40, Caps.TesselationShader));

        return map;
    }

    public Map<Integer,List<Caps>> getGSLVersionCapabilityMapping() {
        Map<Integer,List<Caps>> map = new TreeMap<>();

        map.put(100, Collections.singletonList(Caps.GLSL100));
        map.put(110, Collections.singletonList(Caps.GLSL110));
        map.put(120, Collections.singletonList(Caps.GLSL120));
        map.put(130, Collections.singletonList(Caps.GLSL130));
        map.put(140, Collections.singletonList(Caps.GLSL140));
        map.put(150, Collections.singletonList(Caps.GLSL150));
        map.put(330, Collections.singletonList(Caps.GLSL330));
        map.put(400, Collections.singletonList(Caps.GLSL400));

        return map;
    }

    /**
     * Add capabilities for all versions below the given version.
     * @param versionCapMap mapping of existing versions and there capabilities
     * @param version version to add capabilities for
     */
    private void addCapabilitiesBasedOnVersion(Map<Integer, List<Caps>> versionCapMap, int version) {
        for (Map.Entry<Integer, List<Caps>> entry : versionCapMap.entrySet()) {
            if (version >= entry.getKey()) {
                for (Caps cap : entry.getValue()) {
                    caps.add(cap);
                }
            }
        }
    }

    protected boolean has(Caps cap) {
        return caps.contains(cap);
    }

    private boolean hasExtension(String extensionName) {
        return extensions.contains(extensionName);
    }

    protected int getInteger(int en) {
        intBuf16.clear();
        gl.glGetInteger(en, intBuf16);
        return intBuf16.get(0);
    }

    public EnumSet<Caps> getCaps() {
        return caps;
    }

    public EnumMap<Limits, Integer> getLimits() {
        return limits;
    }
}
