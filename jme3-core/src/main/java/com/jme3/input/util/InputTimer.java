package com.jme3.input.util;

public class InputTimer implements IReadTimer {

	private long lastLastUpdateTime = 0;
	private long lastUpdateTime = 0;
	private long frameDelta = 0;
	
	/* (non-Javadoc)
	 * @see com.jme3.input.IReadTimer#getLastLastUpdateTime()
	 */
	@Override
	public long getLastLastUpdateTime() {
		return lastLastUpdateTime;
	}
	public void setLastLastUpdateTime(long lastLastUpdateTime) {
		this.lastLastUpdateTime = lastLastUpdateTime;
	}
	/* (non-Javadoc)
	 * @see com.jme3.input.IReadTimer#getLastUpdateTime()
	 */
	@Override
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	/* (non-Javadoc)
	 * @see com.jme3.input.IReadTimer#getFrameDelta()
	 */
	@Override
	public long getFrameDelta() {
		return frameDelta;
	}
	public void setFrameDelta(long frameDelta) {
		this.frameDelta = frameDelta;
	}
	
	
}
