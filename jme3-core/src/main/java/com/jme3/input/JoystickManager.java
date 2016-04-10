package com.jme3.input;

public class JoystickManager implements IJoyStickSettings{

	private float deadZone = 0.05F;
	private InputManager inputManager;
    private JoyInput joystick;
    
	
	public JoystickManager(InputManager inputManager, JoyInput joystick)
	{
		this.inputManager = inputManager;
		this.joystick = joystick;
	}

	@Override
	public void setAxisDeadZone(float deadZone) {
		this.deadZone = deadZone;		
	}

	@Override
	public float getAxisDeadZone() {
		return deadZone;
	}

	@Override
	public Joystick[] getJoysticks() {
		if(joystick == null)
			return null;
		return joystick.loadJoysticks(inputManager);
	}
}
