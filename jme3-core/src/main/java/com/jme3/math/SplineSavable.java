package com.jme3.math;

import java.io.IOException;
import java.util.ArrayList;

import com.jme3.export.InputCapsule;
import com.jme3.export.OutputCapsule;

public final class SplineSavable {
	
    @SuppressWarnings("rawtypes")
	public static void write(OutputCapsule oc, Spline spline) throws IOException {
        oc.writeSavableArrayList((ArrayList) spline.controlPoints, "controlPoints", null);
        float list[] = new float[spline.segmentsLength.size()];
        for (int i = 0; i < spline.segmentsLength.size(); i++) {
            list[i] = spline.segmentsLength.get(i);
        }
        oc.write(list, "segmentsLength", null);
        oc.write(spline.totalLength, "totalLength", 0);
        oc.write(spline.cycle, "cycle", false);
    }

    @SuppressWarnings("unchecked")
	public static void read(InputCapsule in, Spline spline) throws IOException {
    	spline.controlPoints = (ArrayList<Vector3f>) in.readSavableArrayList("wayPoints", null);
        float list[] = in.readFloatArray("segmentsLength", null);
        if (list != null) {
        	spline.segmentsLength = new ArrayList<Float>();
            for (int i = 0; i < list.length; i++) {
            	spline.segmentsLength.add(new Float(list[i]));
            }
        }
        spline.totalLength = in.readFloat("totalLength", 0);
        spline.cycle = in.readBoolean("cycle", false);
    }
}
