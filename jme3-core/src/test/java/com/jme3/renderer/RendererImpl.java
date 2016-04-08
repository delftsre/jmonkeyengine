package com.jme3.renderer;

import java.nio.ByteBuffer;
import java.util.EnumSet;

import com.jme3.light.LightList;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix4f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.shader.Shader;
import com.jme3.shader.Shader.ShaderSource;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;

public class RendererImpl implements Renderer {

	private static final EnumSet<Caps> caps = EnumSet.noneOf(Caps.class);
	private static final Statistics stats = new Statistics();
	
	private float start;
    private float end;

	public void initialize() {
	}

	public EnumSet<Caps> getCaps() {
		return caps;
	}

	public Statistics getStatistics() {
		return stats;
	}

	public void invalidateState() {
	}

	public void clearBuffers(boolean color, boolean depth, boolean stencil) {
	}
	
	public void setBackgroundColor(ColorRGBA color) {
	}

	public void applyRenderState(RenderState state) {
	}

	public void setDepthRange(float start, float end) {
		this.start = start;
		this.end = end;
	}
	
	public float getStart() {
		return start;
	}
	
	public float getEnd() {
		return end;
	}

	public void postFrame() {
	}

	public void setWorldMatrix(Matrix4f worldMatrix) {
	}

	public void setViewProjectionMatrices(Matrix4f viewMatrix, Matrix4f projMatrix) {
	}

	public void setViewPort(int x, int y, int width, int height) {
	}

	public void setClipRect(int x, int y, int width, int height) {
	}

	public void clearClipRect() {
	}

	public void setLighting(LightList lights) {
	}

	public void setShader(Shader shader) {
	}

	public void deleteShader(Shader shader) {
	}

	public void deleteShaderSource(ShaderSource source) {
	}

	public void copyFrameBuffer(FrameBuffer src, FrameBuffer dst) {
	}

	public void copyFrameBuffer(FrameBuffer src, FrameBuffer dst, boolean copyDepth) {
	}

	public void setMainFrameBufferOverride(FrameBuffer fb) {
	}

	public void setFrameBuffer(FrameBuffer fb) {
	}

	public void readFrameBuffer(FrameBuffer fb, ByteBuffer byteBuf) {
	}

	public void deleteFrameBuffer(FrameBuffer fb) {
	}

	public void setTexture(int unit, Texture tex) {
	}

	public void modifyTexture(Texture tex, Image pixels, int x, int y) {
	}

	public void updateBufferData(VertexBuffer vb) {
	}

	public void deleteBuffer(VertexBuffer vb) {
	}

	public void renderMesh(Mesh mesh, int lod, int count, VertexBuffer[] instanceData) {
	}

	public void resetGLObjects() {
	}

	public void cleanup() {
	}

	public void deleteImage(Image image) {
	}

	public void setAlphaToCoverage(boolean value) {
	}

	public void setMainFrameBufferSrgb(boolean srgb) {
	}

	public void setLinearizeSrgbImages(boolean linearize) {
	}

	public void readFrameBufferWithFormat(FrameBuffer fb, ByteBuffer byteBuf, Image.Format format) {
	}

}
