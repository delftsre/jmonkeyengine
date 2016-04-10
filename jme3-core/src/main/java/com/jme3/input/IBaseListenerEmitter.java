package com.jme3.input;

import java.util.List;

import com.jme3.input.event.InputEvent;

public interface IBaseListenerEmitter {

	void emit(List<InputEvent> inputQueue);

}