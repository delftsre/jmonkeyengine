package com.jme3.input;

import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.math.Vector2f;

public class CursorManager implements ICursorPos{

	private final Vector2f cursorPos = new Vector2f();
	private boolean mouseVisible = true;
	private final MouseInput mouse;
	private final TouchInput touch;
	
	
	public CursorManager(MouseInput mouse, TouchInput touch)
	{
		this.mouse = mouse;
		this.touch = touch;
	}
	
    /**
     * Sets the mouse cursor image or animation.
     * Set cursor to null to show default system cursor.
     * To hide the cursor completely, use {@link #setCursorVisible(boolean) }.
     *
     * @param jmeCursor The cursor to set, or null to reset to system cursor.
     *
     * @see JmeCursor
     */
    public void setMouseCursor(JmeCursor jmeCursor) {
        mouse.setNativeCursor(jmeCursor);
    }
	
    /**
     * Returns whether the mouse cursor is visible or not.
     *
     * <p>By default the cursor is visible.
     *
     * @return whether the mouse cursor is visible or not.
     *
     * @see InputManager#setCursorVisible(boolean)
     */
    public boolean isCursorVisible() {
        return mouseVisible;
    }
    
    
    /**
     * Set whether the mouse cursor should be visible or not.
     *
     * @param visible whether the mouse cursor should be visible or not.
     */
    public void setCursorVisible(boolean visible) {
        if (mouseVisible != visible) {
            mouseVisible = visible;
            mouse.setCursorVisible(mouseVisible);
        }
    }

    /**
     * Returns the current cursor position. The position is relative to the
     * bottom-left of the screen and is in pixels.
     *
     * @return the current cursor position
     */
    public Vector2f getCursorPosition() {
        return cursorPos;
    }
	
	
    /**
     * Enable simulation of mouse events. Used for touchscreen input only.
     *
     * @param value True to enable simulation of mouse events
     */
    public void setSimulateMouse(boolean value) {
        if (touch != null) {
            touch.setSimulateMouse(value);
        }
    }
    /**
     * @deprecated Use isSimulateMouse
     * Returns state of simulation of mouse events. Used for touchscreen input only.
     *
     */
    public boolean getSimulateMouse() {
        if (touch != null) {
            return touch.isSimulateMouse();
        } else {
            return false;
        }
    }

    /**
     * Returns state of simulation of mouse events. Used for touchscreen input only.
     *
     */
    public boolean isSimulateMouse() {
        if (touch != null) {
            return touch.isSimulateMouse();
        } else {
            return false;
        }
    }
    
    /**
     * Enable simulation of keyboard events. Used for touchscreen input only.
     *
     * @param value True to enable simulation of keyboard events
     */
    public void setSimulateKeyboard(boolean value) {
        if (touch != null) {
        	touch.setSimulateKeyboard(value);
        }
    }

    /**
     * Returns state of simulation of key events. Used for touchscreen input only.
     *
     */
    public boolean isSimulateKeyboard() {
        if (touch != null) {
            return touch.isSimulateKeyboard();
        } else {
            return false;
        }
    }

	@Override
	public void setCursorPosition(float x, float y) {
		cursorPos.set(x,y);
	}
}
