package com.jme3.scene.control;

import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;

/**
 * Interface for Controls requiring render() methods
 * @author wiebe
 *
 */
public interface RenderControl extends Control {
	
	public void render(RenderManager rm, ViewPort vp);
	
}
