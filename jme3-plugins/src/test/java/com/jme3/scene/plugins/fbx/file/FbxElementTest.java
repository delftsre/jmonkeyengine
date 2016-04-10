package com.jme3.scene.plugins.fbx.file;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class FbxElementTest {

    @Test
    public void testGetSubclassNameTwoProperties() {
        FbxElement el = new FbxElement(2);
        el.properties = new ArrayList<Object>();
        el.properties.add("kaas");
        el.properties.add("fromage");
        el.properties.add("cheese");
        assertEquals("fromage", el.getSubclassName());
    }
    
    @Test
    public void testGetSubclassNameThreeProperties() {
        FbxElement el = new FbxElement(3);
        el.properties = new ArrayList<Object>();
        el.properties.add("kaas");
        el.properties.add("fromage");
        el.properties.add("cheese");
        assertEquals("cheese", el.getSubclassName());
    }
    
    @Test
    public void testGetSubclassNameInvalidAmount() {
        FbxElement el = new FbxElement(5);
        el.properties = new ArrayList<Object>();
        el.properties.add("kaas");
        el.properties.add("fromage");
        el.properties.add("cheese");
        assertEquals(null, el.getSubclassName());
    }
    
}
