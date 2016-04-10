package com.jme3.input;

public interface IJoyStickSettings {
	
    /**
     * Set the deadzone for joystick axes.
     *
     * <p>{@link ActionListener#onAction(java.lang.String, boolean, float) }
     * events will only be raised if the joystick axis value is greater than
     * the <code>deadZone</code>.
     *
     * @param deadZone the deadzone for joystick axes.
     */
    public void setAxisDeadZone(float deadZone);

    /**
     * Returns the deadzone for joystick axes.
     *
     * @return the deadzone for joystick axes.
     */
    public float getAxisDeadZone();

    /**
     * Returns an array of all joysticks installed on the system.
     *
     * @return an array of all joysticks installed on the system.
     */
    public Joystick[] getJoysticks();

}
