/**
 * 
 */
package com.jme3.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *  ViewportManager for managing basic functionality of viewPorts.
 *
 */
public class ViewPortManager {

	private ArrayList<ViewPort> preViewPorts;
	private ArrayList<ViewPort> viewPorts;
	private ArrayList<ViewPort> postViewPorts;

	public ViewPortManager() {
		preViewPorts = new ArrayList<ViewPort>();
		viewPorts = new ArrayList<ViewPort>();
		postViewPorts = new ArrayList<ViewPort>();
	}

	public ArrayList<ViewPort> getPreViewPorts() {
		return preViewPorts;
	}

	public void setPreViewPorts(ArrayList<ViewPort> preViewPorts) {
		this.preViewPorts = preViewPorts;
	}

	public ArrayList<ViewPort> getViewPorts() {
		return viewPorts;
	}

	public void setViewPorts(ArrayList<ViewPort> viewPorts) {
		this.viewPorts = viewPorts;
	}

	public ArrayList<ViewPort> getPostViewPorts() {
		return postViewPorts;
	}

	public void setPostViewPorts(ArrayList<ViewPort> postViewPorts) {
		this.postViewPorts = postViewPorts;
	}

	/**
	 * Returns the pre ViewPort with the given name.
	 * 
	 * @param viewName
	 *            The name of the pre ViewPort to look up
	 * @return The ViewPort, or null if not found.
	 * 
	 * @see #createPreView(java.lang.String, com.jme3.renderer.Camera)
	 */
	public ViewPort getPreView(String viewName) {
		return getViewPort(viewName, preViewPorts);
	}

	/**
	 * Removes the pre ViewPort with the specified name.
	 *
	 * @param viewName
	 *            The name of the pre ViewPort to remove
	 * @return True if the ViewPort was removed successfully.
	 *
	 * @see #createPreView(java.lang.String, com.jme3.renderer.Camera)
	 */
	public boolean removePreView(String viewName) {
		return removeView(viewName, preViewPorts);
	}

	/**
	 * Removes the specified pre ViewPort.
	 * 
	 * @param view
	 *            The pre ViewPort to remove
	 * @return True if the ViewPort was removed successfully.
	 * 
	 * @see #createPreView(java.lang.String, com.jme3.renderer.Camera)
	 */
	public boolean removePreView(ViewPort view) {
		return preViewPorts.remove(view);
	}

	/**
	 * Returns the main ViewPort with the given name.
	 * 
	 * @param viewName
	 *            The name of the main ViewPort to look up
	 * @return The ViewPort, or null if not found.
	 * 
	 * @see #createMainView(java.lang.String, com.jme3.renderer.Camera)
	 */
	public ViewPort getMainView(String viewName) {
		return getViewPort(viewName, viewPorts);
	}

	/**
	 * Removes the main ViewPort with the specified name.
	 * 
	 * @param viewName
	 *            The main ViewPort name to remove
	 * @return True if the ViewPort was removed successfully.
	 * 
	 * @see #createMainView(java.lang.String, com.jme3.renderer.Camera)
	 */
	public boolean removeMainView(String viewName) {
		return removeView(viewName, viewPorts);
	}

	/**
	 * Removes the specified main ViewPort.
	 * 
	 * @param view
	 *            The main ViewPort to remove
	 * @return True if the ViewPort was removed successfully.
	 * 
	 * @see #createMainView(java.lang.String, com.jme3.renderer.Camera)
	 */
	public boolean removeMainView(ViewPort view) {
		return viewPorts.remove(view);
	}

	/**
	 * Returns the post ViewPort with the given name.
	 * 
	 * @param viewName
	 *            The name of the post ViewPort to look up
	 * @return The ViewPort, or null if not found.
	 * 
	 * @see #createPostView(java.lang.String, com.jme3.renderer.Camera)
	 */
	public ViewPort getPostView(String viewName) {
		return getViewPort(viewName, postViewPorts);
	}

	/**
	 * Removes the post ViewPort with the specified name.
	 * 
	 * @param viewName
	 *            The post ViewPort name to remove
	 * @return True if the ViewPort was removed successfully.
	 * 
	 * @see #createPostView(java.lang.String, com.jme3.renderer.Camera)
	 */
	public boolean removePostView(String viewName) {
		return removeView(viewName, postViewPorts);
	}

	/**
	 * Removes the specified post ViewPort.
	 * 
	 * @param view
	 *            The post ViewPort to remove
	 * @return True if the ViewPort was removed successfully.
	 * 
	 * @see #createPostView(java.lang.String, com.jme3.renderer.Camera)
	 */
	public boolean removePostView(ViewPort view) {
		return postViewPorts.remove(view);
	}

	/**
	 * Returns a read-only list of all pre ViewPorts
	 * 
	 * @return a read-only list of all pre ViewPorts
	 * @see #createPreView(java.lang.String, com.jme3.renderer.Camera)
	 */
	public List<ViewPort> getPreViews() {
		return Collections.unmodifiableList(preViewPorts);
	}

	/**
	 * Returns a read-only list of all main ViewPorts
	 * 
	 * @return a read-only list of all main ViewPorts
	 * @see #createMainView(java.lang.String, com.jme3.renderer.Camera)
	 */
	public List<ViewPort> getMainViews() {
		return Collections.unmodifiableList(viewPorts);
	}

	/**
	 * Returns a read-only list of all post ViewPorts
	 * 
	 * @return a read-only list of all post ViewPorts
	 * @see #createPostView(java.lang.String, com.jme3.renderer.Camera)
	 */
	public List<ViewPort> getPostViews() {
		return Collections.unmodifiableList(postViewPorts);
	}

	/**
	 * Creates a new pre ViewPort, to display the given camera's content.
	 * <p>
	 * The view will be processed before the main and post viewports.
	 */
	public ViewPort createPreView(String viewName, Camera cam) {
		ViewPort vp = new ViewPort(viewName, cam);
		preViewPorts.add(vp);
		return vp;
	}

	/**
	 * Creates a new main ViewPort, to display the given camera's content.
	 * <p>
	 * The view will be processed before the post viewports but after the pre
	 * viewports.
	 */
	public ViewPort createMainView(String viewName, Camera cam) {
		ViewPort vp = new ViewPort(viewName, cam);
		viewPorts.add(vp);
		return vp;
	}

	/**
	 * Creates a new post ViewPort, to display the given camera's content.
	 * <p>
	 * The view will be processed after the pre and main viewports.
	 */
	public ViewPort createPostView(String viewName, Camera cam) {
		ViewPort vp = new ViewPort(viewName, cam);
		postViewPorts.add(vp);
		return vp;
	}

	/**
	 * Remove a viewport from the provided viewport list
	 * 
	 * @param viewName
	 *            the view to remove
	 * @param viewPorts
	 *            the list in which the viewport is in
	 * @return true if viewPort was removed, otherwise false;
	 */
	private boolean removeView(String viewName, ArrayList<ViewPort> viewPorts) {
		for (int i = 0; i < viewPorts.size(); i++) {
			if (viewPorts.get(i).getName().equals(viewName)) {
				viewPorts.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieve a viewPort by name from the provided viewPort list.
	 * 
	 * @param viewName
	 *            the name of the view to get
	 * @param viewPorts
	 *            the list in which the viewPort should be
	 * @return
	 */
	private ViewPort getViewPort(String viewName, ArrayList<ViewPort> viewPorts) {
		for (int i = 0; i < viewPorts.size(); i++) {
			if (viewPorts.get(i).getName().equals(viewName)) {
				return viewPorts.get(i);
			}
		}
		return null;
	}
}
