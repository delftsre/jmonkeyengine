package com.jme3.math;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;

public class CatmullRomSpline extends Spline{
	
	private List<Vector3f> CRcontrolPoints;
	private float curveTension = 0.5f;
	
	public CatmullRomSpline() {}
	
	public CatmullRomSpline(Spline spline) { 
		super(spline);
	}
	
	public CatmullRomSpline(List<Vector3f> controlPoints, float curveTension, boolean cycle) {
		super(controlPoints, cycle);
		this.curveTension = curveTension;
	}
	
	public CatmullRomSpline(Vector3f[] controlPoints, float curveTension, boolean cycle) {
		super(Arrays.asList(controlPoints), cycle);
		this.curveTension = curveTension;
	}
	
	public CatmullRomSpline(List<Vector3f> controlPoints, boolean cycle) {
		super(controlPoints, cycle);
	}
	
	public CatmullRomSpline(Vector3f[] controlPoints, boolean cycle) {
		super(Arrays.asList(controlPoints), cycle);
	}

	@Override
	public Vector3f interpolate(float value, int currentControlPoint, Vector3f store) {
		if (store == null) {
            store = new Vector3f();
        }
		FastMath.interpolateCatmullRom(value, curveTension, CRcontrolPoints.get(currentControlPoint), CRcontrolPoints.get(currentControlPoint + 1), CRcontrolPoints.get(currentControlPoint + 2), CRcontrolPoints.get(currentControlPoint + 3), store);
		return store;
	}
	
	@Override
	public void computeTotalLentgh() {
		prepareTotalLengthComputation();
		initCatmullRomWayPoints(controlPoints);
        computeCatmullRomLength();
	}
	
    public float getCurveTension() {
        return curveTension;
    }

    /**
     * sets the curve tension
     *
     * @param curveTension the tension
     */
    public void setCurveTension(float curveTension) {
        this.curveTension = curveTension;
        if(!controlPoints.isEmpty()) {            
        	computeTotalLentgh();
        }
    }
	
	private void initCatmullRomWayPoints(List<Vector3f> list) {
        if (CRcontrolPoints == null) {
            CRcontrolPoints = new ArrayList<Vector3f>();
        } else {
            CRcontrolPoints.clear();
        }
        int nb = list.size() - 1;

        if (cycle) {
            CRcontrolPoints.add(list.get(list.size() - 2));
        } else {
            CRcontrolPoints.add(list.get(0).subtract(list.get(1).subtract(list.get(0))));
        }

        for (Iterator<Vector3f> it = list.iterator(); it.hasNext();) {
            Vector3f vector3f = it.next();
            CRcontrolPoints.add(vector3f);
        }
        if (cycle) {
            CRcontrolPoints.add(list.get(1));
        } else {
            CRcontrolPoints.add(list.get(nb).add(list.get(nb).subtract(list.get(nb - 1))));
        }

    }
	
	private void computeCatmullRomLength() {
        float l = 0;
        if (controlPoints.size() > 1) {
            for (int i = 0; i < controlPoints.size() - 1; i++) {
                l = FastMath.getCatmullRomP1toP2Length(CRcontrolPoints.get(i),
                        CRcontrolPoints.get(i + 1), CRcontrolPoints.get(i + 2), CRcontrolPoints.get(i + 3), 0, 1, curveTension);
                segmentsLength.add(l);
                totalLength += l;
            }
        }
    }

	public List<Vector3f> getCRcontrolPoints() {
		return CRcontrolPoints;
	}

	public void setCRcontrolPoints(List<Vector3f> cRcontrolPoints) {
		CRcontrolPoints = cRcontrolPoints;
	}

	@Override
	public float[] getPositionArrayForMesh(int nbSubSegments) {
		Vector3f temp = new Vector3f();
        float[] array = new float[((this.getControlPoints().size() - 1) * nbSubSegments + 1) * 3];
        int i = 0;
        int cptCP = 0;
        for (Iterator<Vector3f> it = this.getControlPoints().iterator(); it.hasNext();) {
            Vector3f vector3f = it.next();
            array[i] = vector3f.x;
            i++;
            array[i] = vector3f.y;
            i++;
            array[i] = vector3f.z;
            i++;
            if (it.hasNext()) {
                for (int j = 1; j < nbSubSegments; j++) {
                	this.interpolate((float) j / nbSubSegments, cptCP, temp);
                    array[i] = temp.getX();
                    i++;
                    array[i] = temp.getY();
                    i++;
                    array[i] = temp.getZ();
                    i++;
                }
            }
            cptCP++;
        }
		return array;
	}

	@Override
	public short[] getIndicesArrayForMesh(int nbSubSegments) {
		short[] indices = new short[(this.getControlPoints().size() - 1) * nbSubSegments * 2];
        int i = 0;
        int k;
        for (int j = 0; j < (this.getControlPoints().size() - 1) * nbSubSegments; j++) {
            k = j;
            indices[i] = (short) k;
            i++;
            k++;
            indices[i] = (short) k;
            i++;
        }
		return indices;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void write(JmeExporter ex) throws IOException {
		OutputCapsule oc = ex.getCapsule(this);
		SplineSavable.write(oc, this);
        oc.writeSavableArrayList((ArrayList) CRcontrolPoints, "CRControlPoints", null);
        oc.write(curveTension, "curveTension", 0.5f);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void read(JmeImporter im) throws IOException {
        InputCapsule in = im.getCapsule(this);
		SplineSavable.read(in, this);
        CRcontrolPoints = (ArrayList<Vector3f>) in.readSavableArrayList("CRControlPoints", null);
        curveTension = in.readFloat("curveTension", 0.5f);
	}

}
