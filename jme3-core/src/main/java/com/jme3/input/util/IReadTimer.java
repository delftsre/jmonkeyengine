package com.jme3.input.util;

public interface IReadTimer {

	long getLastLastUpdateTime();

	long getLastUpdateTime();

	long getFrameDelta();

}