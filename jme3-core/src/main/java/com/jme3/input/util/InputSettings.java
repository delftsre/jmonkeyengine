package com.jme3.input.util;

public class InputSettings implements IReadInputSettings, ISetInputSettings {

    private float frameTPF;
    private float globalAxisDeadZone = 0.05f;
    private boolean safemode = false;
    
	/* (non-Javadoc)
	 * @see com.jme3.input.IReadInputSettings#getFrameTPF()
	 */
	@Override
	public float getFrameTPF() {
		return frameTPF;
	}
	/* (non-Javadoc)
	 * @see com.jme3.input.ISetInputSettings#setFrameTPF(float)
	 */
	@Override
	public void setFrameTPF(float frameTPF) {
		this.frameTPF = frameTPF;
	}
	/* (non-Javadoc)
	 * @see com.jme3.input.IReadInputSettings#getGlobalAxisDeadZone()
	 */
	@Override
	public float getGlobalAxisDeadZone() {
		return globalAxisDeadZone;
	}
	/* (non-Javadoc)
	 * @see com.jme3.input.ISetInputSettings#setGlobalAxisDeadZone(float)
	 */
	@Override
	public void setGlobalAxisDeadZone(float globalAxisDeadZone) {
		this.globalAxisDeadZone = globalAxisDeadZone;
	}
	
	/* (non-Javadoc)
	 * @see com.jme3.input.ISetInputSettings#setSafeMode(boolean)
	 */
	@Override
	public void setSafeMode(boolean s)
	{
		safemode = s;
	}
	
	public boolean safeModeEnabled()
	{
		return safemode;
	}
	
}
