/*
 * Copyright (c) 2009-2012 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors
 *   may be used to endorse or promote products derived from this software
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jme3.animation;

import com.jme3.export.*;
import com.jme3.material.MatParam;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.RendererException;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.*;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import com.jme3.shader.VarType;
import com.jme3.util.SafeArrayList;
import com.jme3.util.TempVars;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Skeleton control deforms a model according to a skeleton, It handles the
 * computation of the deformation matrices and performs the transformations on
 * the mesh
 *
 * @author Rémy Bouquet Based on AnimControl by Kirill Vainer
 */
public class SkeletonControl extends AbstractControl implements Cloneable {

    /**
     * The skeleton of the model.
     */
    private Skeleton skeleton;
    /**
     * List of targets which this controller effects.
     */
    private SafeArrayList<Mesh> targets = new SafeArrayList<Mesh>(Mesh.class);
    /**
     * Used to track when a mesh was updated. Meshes are only updated if they
     * are visible in at least one camera.
     */
    private boolean wasMeshUpdated = false;
    
    /**
     * User wishes to use hardware skinning if available.
     */
    private transient boolean hwSkinningDesired = true;
    
    /**
     * Hardware skinning is currently being used.
     */
    private transient boolean hwSkinningEnabled = false;
    
    /**
     * Hardware skinning was tested on this GPU, results
     * are stored in {@link #hwSkinningSupported} variable.
     */
    private transient boolean hwSkinningTested = false;
    
    /**
     * If hardware skinning was {@link #hwSkinningTested tested}, then
     * this variable will be set to true if supported, and false if otherwise.
     */
    private transient boolean hwSkinningSupported = false;
    
    /**
     * Bone offset matrices, recreated each frame
     */
    private transient Matrix[] offsetMatrices;
    /**
     * Material references used for hardware skinning
     */
    private Set<Material> materials = new HashSet<Material>();

    /**
     * Serialization only. Do not use.
     */
    public SkeletonControl() {
    }

    private void switchToHardware() {
        // Next full 10 bones (e.g. 30 on 24 bones)
        int numBones = ((skeleton.getBoneCount() / 10) + 1) * 10;
        for (Material m : materials) {
            m.setInt("NumberOfBones", numBones);
        }
        for (Mesh mesh : targets) {
            if (mesh.isAnimated()) {
                mesh.prepareForAnim(false);
            }
        }
    }

    private void switchToSoftware() {
        for (Material m : materials) {
            if (m.getParam("NumberOfBones") != null) {
                m.clearParam("NumberOfBones");
            }
        }
        for (Mesh mesh : targets) {
            if (mesh.isAnimated()) {
                mesh.prepareForAnim(true);
            }
        }
    }

    private boolean testHardwareSupported(RenderManager rm) {
        for (Material m : materials) {
            // Some of the animated mesh(es) do not support hardware skinning,
            // so it is not supported by the model.
            if (m.getMaterialDef().getMaterialParam("NumberOfBones") == null) {
                Logger.getLogger(SkeletonControl.class.getName()).log(Level.WARNING, 
                        "Not using hardware skinning for {0}, " + 
                        "because material {1} doesn''t support it.", 
                        new Object[]{spatial, m.getMaterialDef().getName()});
                
                return false;
            }
        }

        switchToHardware();
        
        try {
            rm.preloadScene(spatial);
            return true;
        } catch (RendererException e) {
            Logger.getLogger(SkeletonControl.class.getName()).log(Level.WARNING, "Could not enable HW skinning due to shader compile error:", e);
            return false;
        }
    }

    /**
     * Specifies if hardware skinning is preferred. If it is preferred and
     * supported by GPU, it shall be enabled, if its not preferred, or not
     * supported by GPU, then it shall be disabled.
     * 
     * @see #isHardwareSkinningUsed() 
     */
    public void setHardwareSkinningPreferred(boolean preferred) {
        hwSkinningDesired = preferred;
    }
    
    /**
     * @return True if hardware skinning is preferable to software skinning.
     * Set to false by default.
     * 
     * @see #setHardwareSkinningPreferred(boolean) 
     */
    public boolean isHardwareSkinningPreferred() {
        return hwSkinningDesired;
    }
    
    /**
     * @return True is hardware skinning is activated and is currently used, false otherwise.
     */
    public boolean isHardwareSkinningUsed() {
        return hwSkinningEnabled;
    }
    
    /**
     * Creates a skeleton control. The list of targets will be acquired
     * automatically when the control is attached to a node.
     *
     * @param skeleton the skeleton
     */
    public SkeletonControl(Skeleton skeleton) {
        this.skeleton = skeleton;
    }

    private void findTargets(Node node) {
        for (Spatial child : node.getChildren()) {
            if (child instanceof Geometry) {
                Geometry geom = (Geometry) child;
                Mesh mesh = geom.getMesh();
                if (mesh.isAnimated()) {
                    targets.add(mesh);
                    materials.add(geom.getMaterial());
                }
            } else if (child instanceof Node) {
                findTargets((Node) child);
            }
        }
    }

    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        updateTargetsAndMaterials(spatial);
    }

    private void controlRenderSoftware() {
        resetToBind(); // reset morph meshes to bind pose

        offsetMatrices = skeleton.computeSkinningMatrices();

        for (Mesh mesh : targets) {
            // NOTE: This assumes that code higher up
            // Already ensured those targets are animated
            // otherwise a crash will happen in skin update
            softwareSkinUpdate(mesh, offsetMatrices);
        }     
    }
    
    private void controlRenderHardware() {
        offsetMatrices = skeleton.computeSkinningMatrices();
        for (Material m : materials) {
            MatParam currentParam = m.getParam("BoneMatrices");

            if (currentParam != null) {
                if (currentParam.getValue() != offsetMatrices) {
                    // Check to see if other SkeletonControl
                    // is operating on this material, in that case, user
                    // is sharing materials between models which is NOT allowed
                    // when hardware skinning used.
                    throw new UnsupportedOperationException(
                            "Material instances cannot be shared when hardware skinning is used. " +
                            "Ensure all models use unique material instances."
                    );
                }
            }
            
            m.setParam("BoneMatrices", VarType.Matrix4Array, offsetMatrices);
        }
    }

    
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
        if (!wasMeshUpdated) {
            updateTargetsAndMaterials(spatial);
            
            // Prevent illegal cases. These should never happen.
            assert hwSkinningTested || (!hwSkinningTested && !hwSkinningSupported && !hwSkinningEnabled);
            assert !hwSkinningEnabled || (hwSkinningEnabled && hwSkinningTested && hwSkinningSupported);

            if (hwSkinningDesired && !hwSkinningTested) {
                hwSkinningTested = true;
                hwSkinningSupported = testHardwareSupported(rm);

                if (hwSkinningSupported) {
                    hwSkinningEnabled = true;
                    
                    Logger.getLogger(SkeletonControl.class.getName()).log(Level.INFO, "Hardware skinning engaged for " + spatial);
                } else {
                    switchToSoftware();
                }
            } else if (hwSkinningDesired && hwSkinningSupported && !hwSkinningEnabled) {
                switchToHardware();
                hwSkinningEnabled = true;
            } else if (!hwSkinningDesired && hwSkinningEnabled) {
                switchToSoftware();
                hwSkinningEnabled = false;
            }

            if (hwSkinningEnabled) {
                controlRenderHardware();
            } else {
                controlRenderSoftware();
            }

            wasMeshUpdated = true;
        }
    }

    @Override
    protected void controlUpdate(float tpf) {
        wasMeshUpdated = false;
     }

    //only do this for software updates
    void resetToBind() {
        for (Mesh mesh : targets) {
            if (mesh.isAnimated()) {
                Buffer bwBuff = mesh.getBuffer(Type.BoneWeight).getData();
                Buffer biBuff = mesh.getBuffer(Type.BoneIndex).getData();
                if (!biBuff.hasArray() || !bwBuff.hasArray()) {
                    mesh.prepareForAnim(true); // prepare for software animation
                }
                VertexBuffer bindPos = mesh.getBuffer(Type.BindPosePosition);
                VertexBuffer bindNorm = mesh.getBuffer(Type.BindPoseNormal);
                VertexBuffer pos = mesh.getBuffer(Type.Position);
                VertexBuffer norm = mesh.getBuffer(Type.Normal);
                FloatBuffer pb = (FloatBuffer) pos.getData();
                FloatBuffer nb = (FloatBuffer) norm.getData();
                FloatBuffer bpb = (FloatBuffer) bindPos.getData();
                FloatBuffer bnb = (FloatBuffer) bindNorm.getData();
                pb.clear();
                nb.clear();
                bpb.clear();
                bnb.clear();

                //reseting bind tangents if there is a bind tangent buffer
                VertexBuffer bindTangents = mesh.getBuffer(Type.BindPoseTangent);
                if (bindTangents != null) {
                    VertexBuffer tangents = mesh.getBuffer(Type.Tangent);
                    FloatBuffer tb = (FloatBuffer) tangents.getData();
                    FloatBuffer btb = (FloatBuffer) bindTangents.getData();
                    tb.clear();
                    btb.clear();
                    tb.put(btb).clear();
                }


                pb.put(bpb).clear();
                nb.put(bnb).clear();
            }
        }
    }

    public Control cloneForSpatial(Spatial spatial) {
        Node clonedNode = (Node) spatial;
        SkeletonControl clone = new SkeletonControl();

        AnimControl ctrl = spatial.getControl(AnimControl.class);
        if (ctrl != null) {
            // AnimControl is responsible for cloning the skeleton, not
            // SkeletonControl.
            clone.skeleton = ctrl.getSkeleton();
        } else {
            // If there's no AnimControl, create the clone ourselves.
            clone.skeleton = new Skeleton(skeleton);
        }
        clone.hwSkinningDesired = this.hwSkinningDesired;
        clone.hwSkinningEnabled = this.hwSkinningEnabled;
        clone.hwSkinningSupported = this.hwSkinningSupported;
        clone.hwSkinningTested = this.hwSkinningTested;
        
        clone.setSpatial(clonedNode);

        // Fix attachments for the cloned node
        for (int i = 0; i < clonedNode.getQuantity(); i++) {
            // go through attachment nodes, apply them to correct bone
            Spatial child = clonedNode.getChild(i);
            if (child instanceof Node) {
                Node clonedAttachNode = (Node) child;
                Bone originalBone = (Bone) clonedAttachNode.getUserData("AttachedBone");

                if (originalBone != null) {
                    Bone clonedBone = clone.skeleton.getBone(originalBone.getName());

                    clonedAttachNode.setUserData("AttachedBone", clonedBone);
                    clonedBone.setAttachmentsNode(clonedAttachNode);
                }
            }
        }

        return clone;
    }

    /**
     *
     * @param boneName the name of the bone
     * @return the node attached to this bone
     */
    public Node getAttachmentsNode(String boneName) {
        Bone b = skeleton.getBone(boneName);
        if (b == null) {
            throw new IllegalArgumentException("Given bone name does not exist "
                    + "in the skeleton.");
        }

        Node n = b.getAttachmentsNode();
        Node model = (Node) spatial;
        model.attachChild(n);
        return n;
    }

    /**
     * returns the skeleton of this control
     *
     * @return
     */
    public Skeleton getSkeleton() {
        return skeleton;
    }

    /**
     * returns a copy of array of the targets meshes of this control
     *
     * @return
     */
    public Mesh[] getTargets() {        
        return targets.toArray(new Mesh[targets.size()]);
    }

    /**
     * Update the mesh according to the given transformation matrices
     *
     * @param mesh then mesh
     * @param offsetMatrices the transformation matrices to apply
     */
    private void softwareSkinUpdate(Mesh mesh, Matrix[] offsetMatrices) {

        VertexBuffer tb = mesh.getBuffer(Type.Tangent);
        if (tb == null) {
            //if there are no tangents use the classic skinning
            applySkinning(mesh, offsetMatrices);
        } else {
            //if there are tangents use the skinning with tangents
            applySkinningTangents(mesh, offsetMatrices, tb);
        }


    }

    /**
     * Method to apply skinning transforms to a mesh's buffers
     *
     * @param mesh the mesh
     * @param offsetMatrices the offset matices to apply
     */
    private void applySkinning(Mesh mesh, Matrix[] offsetMatrices) {
        int maxWeightsPerVert = mesh.getMaxNumWeights();
        if (maxWeightsPerVert <= 0) {
            throw new IllegalStateException("Max weights per vert is incorrectly set!");
        }

        int fourMinusMaxWeights = 4 - maxWeightsPerVert;

        // NOTE: This code assumes the vertex buffer is in bind pose
        // resetToBind() has been called this frame
        VertexBuffer vb = mesh.getBuffer(Type.Position);
        FloatBuffer fvb = (FloatBuffer) vb.getData();
        fvb.rewind();

        VertexBuffer nb = mesh.getBuffer(Type.Normal);
        FloatBuffer fnb = (FloatBuffer) nb.getData();
        fnb.rewind();

        // get boneIndexes and weights for mesh
        ByteBuffer ib = (ByteBuffer) mesh.getBuffer(Type.BoneIndex).getData();
        FloatBuffer wb = (FloatBuffer) mesh.getBuffer(Type.BoneWeight).getData();

        ib.rewind();
        wb.rewind();

        float[] weights = wb.array();
        byte[] indices = ib.array();
        int idxWeights = 0;

        TempVars vars = TempVars.get();

        float[] posBuf = vars.skinPositions;
        float[] normBuf = vars.skinNormals;

        int iterations = (int) FastMath.ceil(fvb.limit() / ((float) posBuf.length));
        int bufLength = posBuf.length;
        for (int i = iterations - 1; i >= 0; i--) {
            // read next set of positions and normals from native buffer
            bufLength = Math.min(posBuf.length, fvb.remaining());
            fvb.get(posBuf, 0, bufLength);
            fnb.get(normBuf, 0, bufLength);
            int verts = bufLength / 3;
            int idxPositions = 0;

            // iterate vertices and apply skinning transform for each effecting bone
            for (int vert = verts - 1; vert >= 0; vert--) {
                // Skip this vertex if the first weight is zero.
                if (weights[idxWeights] == 0) {
                    idxPositions += 3;
                    idxWeights += 4;
                    continue;
                }

                float nmx = normBuf[idxPositions];
                float vtx = posBuf[idxPositions++];
                float nmy = normBuf[idxPositions];
                float vty = posBuf[idxPositions++];
                float nmz = normBuf[idxPositions];
                float vtz = posBuf[idxPositions++];

                float rx = 0, ry = 0, rz = 0, rnx = 0, rny = 0, rnz = 0;

                for (int w = maxWeightsPerVert - 1; w >= 0; w--) {
                    float weight = weights[idxWeights];
                    Matrix mat = offsetMatrices[indices[idxWeights++] & 0xff];

                    rx += (mat.matrix[0][0] * vtx + mat.matrix[0][1] * vty + mat.matrix[0][2] * vtz + mat.matrix[0][3]) * weight;
                    ry += (mat.matrix[1][0] * vtx + mat.matrix[1][1] * vty + mat.matrix[1][2] * vtz + mat.matrix[1][3]) * weight;
                    rz += (mat.matrix[2][0] * vtx + mat.matrix[2][1] * vty + mat.matrix[2][2] * vtz + mat.matrix[2][3]) * weight;

                    rnx += (nmx * mat.matrix[0][0] + nmy * mat.matrix[0][1] + nmz * mat.matrix[0][2]) * weight;
                    rny += (nmx * mat.matrix[1][0] + nmy * mat.matrix[1][1] + nmz * mat.matrix[1][2]) * weight;
                    rnz += (nmx * mat.matrix[2][0] + nmy * mat.matrix[2][1] + nmz * mat.matrix[2][2]) * weight;
                }

                idxWeights += fourMinusMaxWeights;

                idxPositions -= 3;
                normBuf[idxPositions] = rnx;
                posBuf[idxPositions++] = rx;
                normBuf[idxPositions] = rny;
                posBuf[idxPositions++] = ry;
                normBuf[idxPositions] = rnz;
                posBuf[idxPositions++] = rz;
            }

            fvb.position(fvb.position() - bufLength);
            fvb.put(posBuf, 0, bufLength);
            fnb.position(fnb.position() - bufLength);
            fnb.put(normBuf, 0, bufLength);
        }

        vars.release();

        vb.updateData(fvb);
        nb.updateData(fnb);

    }

    /**
     * Specific method for skinning with tangents to avoid cluttering the
     * classic skinning calculation with null checks that would slow down the
     * process even if tangents don't have to be computed. Also the iteration
     * has additional indexes since tangent has 4 components instead of 3 for
     * pos and norm
     *
     * @param maxWeightsPerVert maximum number of weights per vertex
     * @param mesh the mesh
     * @param offsetMatrices the offsetMaytrices to apply
     * @param tb the tangent vertexBuffer
     */
    private void applySkinningTangents(Mesh mesh, Matrix[] offsetMatrices, VertexBuffer tb) {
        int maxWeightsPerVert = mesh.getMaxNumWeights();

        if (maxWeightsPerVert <= 0) {
            throw new IllegalStateException("Max weights per vert is incorrectly set!");
        }

        int fourMinusMaxWeights = 4 - maxWeightsPerVert;

        // NOTE: This code assumes the vertex buffer is in bind pose
        // resetToBind() has been called this frame
        VertexBuffer vb = mesh.getBuffer(Type.Position);
        FloatBuffer fvb = (FloatBuffer) vb.getData();
        fvb.rewind();

        VertexBuffer nb = mesh.getBuffer(Type.Normal);

        FloatBuffer fnb = (FloatBuffer) nb.getData();
        fnb.rewind();


        FloatBuffer ftb = (FloatBuffer) tb.getData();
        ftb.rewind();


        // get boneIndexes and weights for mesh
        ByteBuffer ib = (ByteBuffer) mesh.getBuffer(Type.BoneIndex).getData();
        FloatBuffer wb = (FloatBuffer) mesh.getBuffer(Type.BoneWeight).getData();

        ib.rewind();
        wb.rewind();

        float[] weights = wb.array();
        byte[] indices = ib.array();
        int idxWeights = 0;

        TempVars vars = TempVars.get();


        float[] posBuf = vars.skinPositions;
        float[] normBuf = vars.skinNormals;
        float[] tanBuf = vars.skinTangents;

        int iterations = (int) FastMath.ceil(fvb.limit() / ((float) posBuf.length));
        int bufLength = 0;
        int tanLength = 0;
        for (int i = iterations - 1; i >= 0; i--) {
            // read next set of positions and normals from native buffer
            bufLength = Math.min(posBuf.length, fvb.remaining());
            tanLength = Math.min(tanBuf.length, ftb.remaining());
            fvb.get(posBuf, 0, bufLength);
            fnb.get(normBuf, 0, bufLength);
            ftb.get(tanBuf, 0, tanLength);
            int verts = bufLength / 3;
            int idxPositions = 0;
            //tangents has their own index because of the 4 components
            int idxTangents = 0;

            // iterate vertices and apply skinning transform for each effecting bone
            for (int vert = verts - 1; vert >= 0; vert--) {
                // Skip this vertex if the first weight is zero.
                if (weights[idxWeights] == 0) {
                    idxTangents += 4;
                    idxPositions += 3;
                    idxWeights += 4;
                    continue;
                }

                float nmx = normBuf[idxPositions];
                float vtx = posBuf[idxPositions++];
                float nmy = normBuf[idxPositions];
                float vty = posBuf[idxPositions++];
                float nmz = normBuf[idxPositions];
                float vtz = posBuf[idxPositions++];

                float tnx = tanBuf[idxTangents++];
                float tny = tanBuf[idxTangents++];
                float tnz = tanBuf[idxTangents++];

                // skipping the 4th component of the tangent since it doesn't have to be transformed
                idxTangents++;

                float rx = 0, ry = 0, rz = 0, rnx = 0, rny = 0, rnz = 0, rtx = 0, rty = 0, rtz = 0;

                for (int w = maxWeightsPerVert - 1; w >= 0; w--) {
                    float weight = weights[idxWeights];
                    Matrix mat = offsetMatrices[indices[idxWeights++] & 0xff];

                    rx += (mat.matrix[0][0] * vtx + mat.matrix[0][1] * vty + mat.matrix[0][2] * vtz + mat.matrix[0][3]) * weight;
                    ry += (mat.matrix[1][0] * vtx + mat.matrix[1][1] * vty + mat.matrix[1][2] * vtz + mat.matrix[1][3]) * weight;
                    rz += (mat.matrix[2][0] * vtx + mat.matrix[2][1] * vty + mat.matrix[2][2] * vtz + mat.matrix[2][3]) * weight;

                    rnx += (nmx * mat.matrix[0][0] + nmy * mat.matrix[0][1] + nmz * mat.matrix[0][2]) * weight;
                    rny += (nmx * mat.matrix[1][0] + nmy * mat.matrix[1][1] + nmz * mat.matrix[1][2]) * weight;
                    rnz += (nmx * mat.matrix[2][0] + nmy * mat.matrix[2][1] + nmz * mat.matrix[2][2]) * weight;

                    rtx += (tnx * mat.matrix[0][0] + tny * mat.matrix[0][1] + tnz * mat.matrix[0][2]) * weight;
                    rty += (tnx * mat.matrix[1][0] + tny * mat.matrix[1][1] + tnz * mat.matrix[1][2]) * weight;
                    rtz += (tnx * mat.matrix[2][0] + tny * mat.matrix[2][1] + tnz * mat.matrix[2][2]) * weight;
                }

                idxWeights += fourMinusMaxWeights;

                idxPositions -= 3;

                normBuf[idxPositions] = rnx;
                posBuf[idxPositions++] = rx;
                normBuf[idxPositions] = rny;
                posBuf[idxPositions++] = ry;
                normBuf[idxPositions] = rnz;
                posBuf[idxPositions++] = rz;

                idxTangents -= 4;

                tanBuf[idxTangents++] = rtx;
                tanBuf[idxTangents++] = rty;
                tanBuf[idxTangents++] = rtz;

                //once again skipping the 4th component of the tangent
                idxTangents++;
            }

            fvb.position(fvb.position() - bufLength);
            fvb.put(posBuf, 0, bufLength);
            fnb.position(fnb.position() - bufLength);
            fnb.put(normBuf, 0, bufLength);
            ftb.position(ftb.position() - tanLength);
            ftb.put(tanBuf, 0, tanLength);
        }

        vars.release();

        vb.updateData(fvb);
        nb.updateData(fnb);
        tb.updateData(ftb);


    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(skeleton, "skeleton", null);
        //Targets and materials don't need to be saved, they'll be gathered on each frame
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule in = im.getCapsule(this);
        skeleton = (Skeleton) in.readSavable("skeleton", null);
    }

    private void updateTargetsAndMaterials(Spatial spatial) {
        targets.clear();
        materials.clear();           
        if (spatial != null && spatial instanceof Node) {
            Node node = (Node) spatial;                        
            findTargets(node);
        }
    }
}
