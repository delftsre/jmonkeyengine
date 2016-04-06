package com.jme3.texture;

import com.jme3.export.JmeExporter;
import com.jme3.export.OutputCapsule;
import com.jme3.export.Savable;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.Mockito.spy;

public class TestExporter implements JmeExporter {

    @Override
    public void save(Savable object, OutputStream f) throws IOException {

    }

    @Override
    public void save(Savable object, File f) throws IOException {

    }

    @Override
    public OutputCapsule getCapsule(Savable object) {
        return spy(new TestOutputCapsule());
    }
}
