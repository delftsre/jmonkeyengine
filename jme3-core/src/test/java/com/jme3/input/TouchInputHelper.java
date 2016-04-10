package com.jme3.input;

public class TouchInputHelper implements TouchInput {

	private RawInputListener inputListener;
	
	@Override
	public void initialize() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isInitialized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInputListener(RawInputListener listener) {
		this.inputListener = listener;

	}

	public RawInputListener getRawInputListener()
	{
		return this.inputListener;
	}

	@Override
	public long getInputTimeNanos() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setSimulateMouse(boolean simulate) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSimulateMouse() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSimulateKeyboard(boolean simulate) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSimulateKeyboard() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setOmitHistoricEvents(boolean dontSendHistory) {
		// TODO Auto-generated method stub

	}

}
