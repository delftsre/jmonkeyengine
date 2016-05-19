package com.jme3.input.event;

import org.junit.Test;

/**
 * Check if the {@link KeyInputEvent} class is working correctly.
 * 
 * @author Enrique Correa
 */
public class KeyInputEventTest {
  @Test
  public void testToString() {
    KeyInputEvent kie1 = new KeyInputEvent(50, 'a', true, true);
    KeyInputEvent kie2 = new KeyInputEvent(25, 'b', false, true);
    KeyInputEvent kie3 = new KeyInputEvent(30, 'c', false, false);
    assert kie1.toString().equals("Key(CODE=50, CHAR=a, REPEATING)");
    assert kie2.toString().equals("Key(CODE=25, CHAR=b, REPEATING)");
    assert kie3.toString().equals("Key(CODE=30, CHAR=c, RELEASED)");
  }
}