/**
 * 
 */
package com.jme3.renderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 
 *
 */
public class ViewportManager {

    private ArrayList<ViewPort> preViewPorts = new ArrayList<ViewPort>();
    private ArrayList<ViewPort> viewPorts = new ArrayList<ViewPort>();
    private ArrayList<ViewPort> postViewPorts = new ArrayList<ViewPort>();
    
    public ViewportManager(){
    	
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
     * @param viewName The name of the pre ViewPort to look up
     * @return The ViewPort, or null if not found.
     * 
     * @see #createPreView(java.lang.String, com.jme3.renderer.Camera) 
     */
    public ViewPort getPreView(String viewName) {
        for (int i = 0; i < preViewPorts.size(); i++) {
            if (preViewPorts.get(i).getName().equals(viewName)) {
                return preViewPorts.get(i);
            }
        }
        return null;
    }

    /**
     * Removes the pre ViewPort with the specified name.
     *
     * @param viewName The name of the pre ViewPort to remove
     * @return True if the ViewPort was removed successfully.
     *
     * @see #createPreView(java.lang.String, com.jme3.renderer.Camera)
     */
    public boolean removePreView(String viewName) {
        for (int i = 0; i < preViewPorts.size(); i++) {
            if (preViewPorts.get(i).getName().equals(viewName)) {
                preViewPorts.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the specified pre ViewPort.
     * 
     * @param view The pre ViewPort to remove
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
     * @param viewName The name of the main ViewPort to look up
     * @return The ViewPort, or null if not found.
     * 
     * @see #createMainView(java.lang.String, com.jme3.renderer.Camera) 
     */
    public ViewPort getMainView(String viewName) {
        for (int i = 0; i < viewPorts.size(); i++) {
            if (viewPorts.get(i).getName().equals(viewName)) {
                return viewPorts.get(i);
            }
        }
        return null;
    }

    /**
     * Removes the main ViewPort with the specified name.
     * 
     * @param viewName The main ViewPort name to remove
     * @return True if the ViewPort was removed successfully.
     * 
     * @see #createMainView(java.lang.String, com.jme3.renderer.Camera) 
     */
    public boolean removeMainView(String viewName) {
        for (int i = 0; i < viewPorts.size(); i++) {
            if (viewPorts.get(i).getName().equals(viewName)) {
                viewPorts.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Removes the specified main ViewPort.
     * 
     * @param view The main ViewPort to remove
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
     * @param viewName The name of the post ViewPort to look up
     * @return The ViewPort, or null if not found.
     * 
     * @see #createPostView(java.lang.String, com.jme3.renderer.Camera) 
     */
    public ViewPort getPostView(String viewName) {
        for (int i = 0; i < postViewPorts.size(); i++) {
            if (postViewPorts.get(i).getName().equals(viewName)) {
                return postViewPorts.get(i);
            }
        }
        return null;
    }

    /**
     * Removes the post ViewPort with the specified name.
     * 
     * @param viewName The post ViewPort name to remove
     * @return True if the ViewPort was removed successfully.
     * 
     * @see #createPostView(java.lang.String, com.jme3.renderer.Camera) 
     */
    public boolean removePostView(String viewName) {
        for (int i = 0; i < postViewPorts.size(); i++) {
            if (postViewPorts.get(i).getName().equals(viewName)) {
                postViewPorts.remove(i);

                return true;
            }
        }
        return false;
    }

    /**
     * Removes the specified post ViewPort.
     * 
     * @param view The post ViewPort to remove
     * @return True if the ViewPort was removed successfully.
     * 
     * @see #createPostView(java.lang.String, com.jme3.renderer.Camera) 
     */
    public boolean removePostView(ViewPort view) {
        return postViewPorts.remove(view);
    }

    /**
     * Returns a read-only list of all pre ViewPorts
     * @return a read-only list of all pre ViewPorts
     * @see #createPreView(java.lang.String, com.jme3.renderer.Camera) 
     */
    public List<ViewPort> getPreViews() {
        return Collections.unmodifiableList(preViewPorts);
    }

    /**
     * Returns a read-only list of all main ViewPorts
     * @return a read-only list of all main ViewPorts
     * @see #createMainView(java.lang.String, com.jme3.renderer.Camera) 
     */
    public List<ViewPort> getMainViews() {
        return Collections.unmodifiableList(viewPorts);
    }

    /**
     * Returns a read-only list of all post ViewPorts
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
     * The view will be processed before the post viewports but after
     * the pre viewports.
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
}
