package com.jme3.terrain.geomipmap;

import java.nio.FloatBuffer;

import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.BufferUtils;

public class TerrainPatchNormals {
	
    protected static void updateNormals(TerrainPatch tp) {
        FloatBuffer newNormalBuffer = tp.getTpLod().getLODGeomap().writeNormalArray(null, tp.getWorldScale());
        tp.getMesh().getBuffer(Type.Normal).updateData(newNormalBuffer);
        FloatBuffer newTangentBuffer = null;
        FloatBuffer newBinormalBuffer = null;
        FloatBuffer[] tb = tp.getTpLod().getLODGeomap().writeTangentArray(newNormalBuffer, newTangentBuffer, newBinormalBuffer, (FloatBuffer)tp.getMesh().getBuffer(Type.TexCoord).getData(), tp.getWorldScale());
        newTangentBuffer = tb[0];
        newBinormalBuffer = tb[1];
        tp.getMesh().getBuffer(Type.Tangent).updateData(newTangentBuffer);
        tp.getMesh().getBuffer(Type.Binormal).updateData(newBinormalBuffer);
    }

    private static void setInBuffer(Mesh mesh, int index, Vector3f normal, Vector3f tangent, Vector3f binormal) {
        VertexBuffer NB = mesh.getBuffer(Type.Normal);
        VertexBuffer TB = mesh.getBuffer(Type.Tangent);
        VertexBuffer BB = mesh.getBuffer(Type.Binormal);
        BufferUtils.setInBuffer(normal, (FloatBuffer)NB.getData(), index);
        BufferUtils.setInBuffer(tangent, (FloatBuffer)TB.getData(), index);
        BufferUtils.setInBuffer(binormal, (FloatBuffer)BB.getData(), index);
        NB.setUpdateNeeded();
        TB.setUpdateNeeded();
        BB.setUpdateNeeded();
    }
    
    /**
     * Matches the normals along the edge of the patch with the neighbours.
     * Computes the normals for the right, bottom, left, and top edges of the
     * patch, and saves those normals in the neighbour's edges too.
     *
     * Takes 4 points (if has neighbour on that side) for each
     * point on the edge of the patch:
     *              *
     *              |
     *          *---x---*
     *              |
     *              *
     * It works across the right side of the patch, from the top down to 
     * the bottom. Then it works on the bottom side of the patch, from the
     * left to the right.
     */
    protected static void fixNormalEdges(TerrainPatch right,
                                TerrainPatch bottom,
                                TerrainPatch top,
                                TerrainPatch left,
                                TerrainPatch bottomRight,
                                TerrainPatch bottomLeft,
                                TerrainPatch topRight,
                                TerrainPatch topLeft, 
                                TerrainPatch tp)
    {
        Vector3f rootPoint 	 = new Vector3f();
        Vector3f rightPoint  = new Vector3f();
        Vector3f leftPoint 	 = new Vector3f();
        Vector3f topPoint    = new Vector3f();
        Vector3f bottomPoint = new Vector3f();

        Vector3f tangent 	 = new Vector3f();
        Vector3f binormal 	 = new Vector3f();
        Vector3f normal 	 = new Vector3f();
        
        int s = tp.getSize()-1;
        
        for (int x = 0; x < 3; x++) {
        	for (int i=0; i<s+1; i++) {
        		// Left or Right
            	if (x <= 1 && ((x == 0 && right != null) || (x == 1 && left != null))) {
            		rootPoint.set(0, getHeightmapHeight((x == 0 ? s : 0),i, tp), 0);
                    leftPoint.set(-1, getHeightmapHeight(s-1,i, (x == 0 ? tp : left)), 0);
                    rightPoint.set(1, getHeightmapHeight(1,i, (x == 1 ? tp : right)), 0);
                    
                    if (i == 0) { // top point
                    	bottomPoint.set(0, getHeightmapHeight((x == 0 ? s : 0),i+1, tp), 1);
                    	
                        if (top != null) {
                            topPoint.set(0, getHeightmapHeight((x == 0 ? s : 0),s-1, top), -1);
                            setInBuffer(top.getMesh(), (x == 0 ? (s+1)*(s+1)-1 : (s+1)*s), normal, tangent, binormal);
                        } 
                        
                        averageNormalsTangents((top == null ? null : topPoint), rootPoint, leftPoint, bottomPoint, rightPoint,normal, tangent, binormal, tp);
                        setInBuffer(tp.getMesh(), (x == 0 ? s : 0), normal, tangent, binormal);
                        setInBuffer((x == 0 ? right : left).getMesh(), (x == 0 ? 0 : s), normal, tangent, binormal);
                    } else if (i == s) { // bottom point
                        topPoint.set(0, getHeightmapHeight((x == 0 ? s : 0), (x == 0 ? s-1 : i - 1), tp), -1);
                        
                        if (bottom != null) {
                            bottomPoint.set(0, getHeightmapHeight((x == 0 ? s : 0),1, bottom), 1);
                            setInBuffer(bottom.getMesh(), (x == 0 ? s : 0), normal, tangent, binormal);                            
                        }
                        
                        averageNormalsTangents(topPoint, rootPoint, leftPoint, (bottom == null ? null : bottomPoint), rightPoint, normal, tangent, binormal, tp);
                        setInBuffer(tp.getMesh(), (x == 0 ? (s+1)*(s+1)-1 : (s+1)*s), normal, tangent, binormal);
                        setInBuffer((x == 0 ? right : left).getMesh(), (x == 0 ? (s+1)*s : (s+1)*(s+1)-1), normal, tangent, binormal);
                        
                    } else { // all in the middle
                        topPoint.set(0, getHeightmapHeight((x == 0 ? s : 0),i-1, tp), -1);
                        bottomPoint.set(0, getHeightmapHeight((x == 0 ? s : 0),i+1, tp), 1);
                        averageNormalsTangents(topPoint, rootPoint, leftPoint, bottomPoint, rightPoint, normal, tangent, binormal, tp);
                        setInBuffer(tp.getMesh(), (x == 0 ? (s+1)*(i+1)-1 : (s+1)*i), normal, tangent, binormal);
                        setInBuffer((x == 0 ? right : left).getMesh(), (x == 0 ? (s+1)*i : (s+1)*(i+1)-1), normal, tangent, binormal);
                    }
            	} 
            	// Top or Bottom
            	else if (x <= 3 && ((x == 2 && top != null) || (x == 3 && bottom != null))) {
        			rootPoint.set(0, getHeightmapHeight(i,(x == 2 ? 0 : s), tp), 0);
                    topPoint.set(0, getHeightmapHeight(i,s-1, (x == 2 ? top : tp)), -1);
                    bottomPoint.set(0, getHeightmapHeight(i,1, (x == 3 ? bottom : tp)), 1);
                    
                    if (i != 0 && i != s) { // Other cases handled by this patch elsewhere
                    	leftPoint.set(-1, getHeightmapHeight(i-1,(x == 2 ? 0 : s), tp), 0);
                        rightPoint.set(1, getHeightmapHeight(i+1,(x == 2 ? 0 : s), tp), 0);
                        averageNormalsTangents(topPoint, rootPoint, leftPoint, bottomPoint, rightPoint, normal, tangent, binormal, tp);
                        setInBuffer(tp.getMesh(), (x == 2 ? i : (s+1)*(s)+i), normal, tangent, binormal);
                        setInBuffer((x == 2 ? top : bottom).getMesh(), (x == 2 ? (s+1)*(s)+i : i), normal, tangent, binormal);
                    }
            	}
        	}
        }
    }

    protected static void averageNormalsTangents(
            Vector3f topPoint,
            Vector3f rootPoint,
            Vector3f leftPoint, 
            Vector3f bottomPoint, 
            Vector3f rightPoint,
            Vector3f normal,
            Vector3f tangent,
            Vector3f binormal,
            TerrainPatch tp)
    {
        Vector3f scale = tp.getWorldScale();
        
        Vector3f n1 = new Vector3f(0,0,0);
        if (topPoint != null && leftPoint != null) {
            n1.set(calculateNormal(topPoint.mult(scale), rootPoint.mult(scale), leftPoint.mult(scale)));
        }
        Vector3f n2 = new Vector3f(0,0,0);
        if (leftPoint != null && bottomPoint != null) {
            n2.set(calculateNormal(leftPoint.mult(scale), rootPoint.mult(scale), bottomPoint.mult(scale)));
        }
        Vector3f n3 = new Vector3f(0,0,0);
        if (rightPoint != null && bottomPoint != null) {
            n3.set(calculateNormal(bottomPoint.mult(scale), rootPoint.mult(scale), rightPoint.mult(scale)));
        }
        Vector3f n4 = new Vector3f(0,0,0);
        if (rightPoint != null && topPoint != null) {
            n4.set(calculateNormal(rightPoint.mult(scale), rootPoint.mult(scale), topPoint.mult(scale)));
        }
        
        //if (bottomPoint != null && rightPoint != null && rootTex != null && rightTex != null && bottomTex != null)
        //    LODGeomap.calculateTangent(new Vector3f[]{rootPoint.mult(scale),rightPoint.mult(scale),bottomPoint.mult(scale)}, new Vector2f[]{rootTex,rightTex,bottomTex}, tangent, binormal);

        normal.set(n1.add(n2).add(n3).add(n4).normalize());
        
        tangent.set(normal.cross(new Vector3f(0,0,1)).normalize());
        binormal.set(new Vector3f(1,0,0).cross(normal).normalize());
    }

    private static Vector3f calculateNormal(Vector3f firstPoint, Vector3f rootPoint, Vector3f secondPoint) {
        Vector3f normal = new Vector3f();
        normal.set(firstPoint).subtractLocal(rootPoint)
                  .crossLocal(secondPoint.subtract(rootPoint)).normalizeLocal();
        return normal;
    }
    
    protected static Vector3f getMeshNormal(int x, int z, TerrainPatch tp) {
        if (x >= tp.getSize() || z >= tp.getSize())
            return null; // out of range
        
        int index = (z*tp.getSize()+x)*3;
        FloatBuffer nb = (FloatBuffer)tp.getMesh().getBuffer(Type.Normal).getData();
        Vector3f normal = new Vector3f();
        normal.x = nb.get(index);
        normal.y = nb.get(index+1);
        normal.z = nb.get(index+2);
        return normal;
    }
    
    public static float getHeightmapHeight(float x, float z, TerrainPatch tp) {
        if (x < 0 || z < 0 || x >= tp.getSize()|| z >= tp.getSize())
            return 0;
        int idx = (int) (z * tp.getSize()+ x);
        return tp.getMesh().getFloatBuffer(Type.Position).get(idx*3+1); // 3 floats per entry (x,y,z), the +1 is to get the Y
    }
    
}
