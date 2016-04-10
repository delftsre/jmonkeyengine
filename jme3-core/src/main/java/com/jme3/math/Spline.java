package com.jme3.math;

import java.util.ArrayList;
import java.util.List;

import com.jme3.export.Savable;

public abstract class Spline implements SplineInterface, Savable{
	
	protected List<Vector3f> controlPoints = new ArrayList<Vector3f>();	
	protected List<Float> segmentsLength;
	protected float totalLength;
	protected boolean cycle = false;
	
	protected Spline() { }
	
	protected Spline(List<Vector3f> controlPoints, boolean cycle) {
		this.controlPoints.addAll(controlPoints);
        this.cycle = cycle;
        this.computeTotalLentgh();
	}
	
	protected Spline(Spline spline) {
		this.controlPoints.addAll(spline.controlPoints);
        this.cycle = spline.cycle;
        this.computeTotalLentgh();
	}
	
	protected void prepareTotalLengthComputation() {
		totalLength = 0;
        if (segmentsLength == null) {
            segmentsLength = new ArrayList<Float>();
        } else {
            segmentsLength.clear();
        }
	}
	
    /**
     * Adds a controlPoint to the spline
     * @param controlPoint a position in world space
     */
    public void addControlPoint(Vector3f controlPoint) {
        if (controlPoints.size() > 2 && this.cycle) {
            controlPoints.remove(controlPoints.size() - 1);
        }
        controlPoints.add(controlPoint.clone());
        if (controlPoints.size() >= 2 && this.cycle) {
            controlPoints.add(controlPoints.get(0).clone());
        }
        if (controlPoints.size() > 1) {
            this.computeTotalLentgh();
        }
    }

    /**
     * remove the controlPoint from the spline
     * @param controlPoint the controlPoint to remove
     */
    public void removeControlPoint(Vector3f controlPoint) {
        controlPoints.remove(controlPoint);
        if (controlPoints.size() > 1) {
            this.computeTotalLentgh();
        }
    }
    
    public void clearControlPoints(){
        controlPoints.clear();
        totalLength = 0;
    }
    
    public List<Vector3f> getControlPoints() {
		return controlPoints;
	}

	public void setControlPoints(List<Vector3f> controlPoints) {
		this.controlPoints = controlPoints;
	}

	public List<Float> getSegmentsLength() {
		return segmentsLength;
	}

	public void setSegmentsLength(List<Float> segmentsLength) {
		this.segmentsLength = segmentsLength;
	}

	public float getTotalLength() {
		return totalLength;
	}

	public void setTotalLength(float totalLength) {
		this.totalLength = totalLength;
	}

	public boolean isCycle() {
		return cycle;
	}

	public void setCycle(boolean cycle) {
		if (controlPoints.size() >= 2) {
			if (this.cycle && !cycle) {
				controlPoints.remove(controlPoints.size() - 1);
			}
			if (!this.cycle && cycle) {
				controlPoints.add(controlPoints.get(0));
			}
			this.cycle = cycle;
			this.computeTotalLentgh();
		} else {
			this.cycle = cycle;
		}
	}
}
