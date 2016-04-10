package com.jme3.test.util;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class TestLogHandler extends Handler
{
    Level lastLevel = Level.FINEST;
    int loggedMessages = 0;
    
    public Level  checkLevel() {
        return lastLevel;
    }    

    public void publish(LogRecord record) {
        lastLevel = record.getLevel();
        loggedMessages++;
    }
    
    public int getNrOfLoggedMessages()
    {
    	return loggedMessages;
    }

    public void close(){}
    public void flush(){}
}
