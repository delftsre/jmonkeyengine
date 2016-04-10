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
package com.jme3.scene;

import com.jme3.asset.AssetNotFoundException;
import com.jme3.bounding.BoundingVolume;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.export.InputCapsule;
import com.jme3.export.JmeExporter;
import com.jme3.export.JmeImporter;
import com.jme3.export.OutputCapsule;
import com.jme3.light.LightList;
import com.jme3.material.*;
import com.jme3.math.Matrix4f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.Caps;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.geometryrenderers.GeometryRenderer;
import com.jme3.renderer.Renderer;
import com.jme3.renderer.geometryrenderers.MultiPassGeometryRenderer;
import com.jme3.renderer.geometryrenderers.NoLightGeometryRenderer;
import com.jme3.renderer.geometryrenderers.SinglePassGeometryRenderer;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.util.TempVars;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <code>Geometry</code> defines a leaf node of the scene graph. The leaf node
 * contains the geometric data for rendering objects. It manages all rendering
 * information such as a {@link Material} object to define how the surface
 * should be shaded and the {@link Mesh} data to contain the actual geometry.
 * 
 * @author Kirill Vainer
 */
public class Geometry extends Spatial {

    // Version #1: removed shared meshes. 
    // models loaded with shared mesh will be automatically fixed.
    public static final int SAVABLE_VERSION = 1;
    private static final Logger logger = Logger.getLogger(Geometry.class.getName());
    protected Mesh mesh;
    protected transient int lodLevel = 0;
    protected Material material;
    /**
     * When true, the geometry's transform will not be applied.
     */
    protected boolean ignoreTransform = false;
    protected transient Matrix4f cachedWorldMat = new Matrix4f();
    
    /**
     * Specifies which {@link GeometryGroupNode} this <code>Geometry</code>
     * is managed by.
     */
    protected GeometryGroupNode groupNode;
    
    /**
     * The start index of this <code>Geometry's</code> inside 
     * the {@link GeometryGroupNode}.
     */
    protected int startIndex = -1;

    /**
     * The renderer that will render this geometry object
     */
    private GeometryRenderer renderer;

    private Material restorableMaterial;
    private String restorableTechniqueName;
    private RenderState restorableRenderState;

    /**
     * Serialization only. Do not use.
     */
    public Geometry() {
        this(null);
    }

    /**
     * Create a geometry node without any mesh data.
     * Both the mesh and the material are null, the geometry
     * cannot be rendered until those are set.
     * 
     * @param name The name of this geometry
     */
    public Geometry(String name) {
        super(name);
        
        // For backwards compatibility, only clear the "requires
        // update" flag if we are not a subclass of Geometry.
        // This prevents subclass from silently failing to receive
        // updates when they upgrade.
        setRequiresUpdates(Geometry.class != getClass()); 
    }

    /**
     * Create a geometry node with mesh data.
     * The material of the geometry is null, it cannot
     * be rendered until it is set.
     * 
     * @param name The name of this geometry
     * @param mesh The mesh data for this geometry
     */
    public Geometry(String name, Mesh mesh) {
        this(name);
        
        if (mesh == null) {
            throw new IllegalArgumentException("mesh cannot be null");
        }

        this.mesh = mesh;
    }
    
    @Override
    public boolean checkCulling(Camera cam) {
        if (isGrouped()) {
            setLastFrustumIntersection(Camera.FrustumIntersect.Outside);
            return false;
        }
        return super.checkCulling(cam);
    }

    /**
     * @return If ignoreTransform mode is set.
     * 
     * @see Geometry#setIgnoreTransform(boolean) 
     */
    public boolean isIgnoreTransform() {
        return ignoreTransform;
    }

    /**
     * @param ignoreTransform If true, the geometry's transform will not be applied.
     */
    public void setIgnoreTransform(boolean ignoreTransform) {
        this.ignoreTransform = ignoreTransform;
    }

    /**
     * Sets the LOD level to use when rendering the mesh of this geometry.
     * Level 0 indicates that the default index buffer should be used,
     * levels [1, LodLevels + 1] represent the levels set on the mesh
     * with {@link Mesh#setLodLevels(com.jme3.scene.VertexBuffer[]) }.
     * 
     * @param lod The lod level to set
     */
    @Override
    public void setLodLevel(int lod) {
        if (mesh.getNumLodLevels() == 0) {
            throw new IllegalStateException("LOD levels are not set on this mesh");
        }

        if (lod < 0 || lod >= mesh.getNumLodLevels()) {
            throw new IllegalArgumentException("LOD level is out of range: " + lod);
        }

        lodLevel = lod;
        
        if (isGrouped()) {
            groupNode.onMeshChange(this);
        }
    }

    /**
     * Returns the LOD level set with {@link #setLodLevel(int) }.
     * 
     * @return the LOD level set
     */
    public int getLodLevel() {
        return lodLevel;
    }

    /**
     * Returns this geometry's mesh vertex count.
     * 
     * @return this geometry's mesh vertex count.
     * 
     * @see Mesh#getVertexCount() 
     */
    public int getVertexCount() {
        return mesh.getVertexCount();
    }

    /**
     * Returns this geometry's mesh triangle count.
     * 
     * @return this geometry's mesh triangle count.
     * 
     * @see Mesh#getTriangleCount() 
     */
    public int getTriangleCount() {
        return mesh.getTriangleCount();
    }

    /**
     * Sets the mesh to use for this geometry when rendering.
     * 
     * @param mesh the mesh to use for this geometry
     * 
     * @throws IllegalArgumentException If mesh is null
     */
    public void setMesh(Mesh mesh) {
        if (mesh == null) {
            throw new IllegalArgumentException();
        }

        this.mesh = mesh;
        setBoundRefresh();
        
        if (isGrouped()) {
            groupNode.onMeshChange(this);
        }
    }

    /**
     * Returns the mesh to use for this geometry
     * 
     * @return the mesh to use for this geometry
     * 
     * @see #setMesh(com.jme3.scene.Mesh) 
     */
    public Mesh getMesh() {
        return mesh;
    }

    /**
     * Sets the material to use for this geometry.
     * 
     * @param material the material to use for this geometry
     */
    @Override
    public void setMaterial(Material material) {
        this.material = material;
        
        if (isGrouped()) {
            groupNode.onMaterialChange(this);
        }
    }

    /**
     * Returns the material that is used for this geometry.
     * 
     * @return the material that is used for this geometry
     * 
     * @see #setMaterial(com.jme3.material.Material) 
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * @return The bounding volume of the mesh, in model space.
     */
    public BoundingVolume getModelBound() {
        return mesh.getBound();
    }

    /**
     * Updates the bounding volume of the mesh. Should be called when the
     * mesh has been modified.
     */
    public void updateModelBound() {
        mesh.updateBound();
        setBoundRefresh();
    }

    /**
     * <code>updateWorldBound</code> updates the bounding volume that contains
     * this geometry. The location of the geometry is based on the location of
     * all this node's parents.
     *
     * @see Spatial#updateWorldBound()
     */
    @Override
    protected void updateWorldBound() {
        super.updateWorldBound();
        if (mesh == null) {
            throw new NullPointerException("Geometry: " + getName() + " has null mesh");
        }

        if (mesh.getBound() != null) {
            if (ignoreTransform) {
                // we do not transform the model bound by the world transform,
                // just use the model bound as-is
                worldBound = mesh.getBound().clone(worldBound);
            } else {
                worldBound = mesh.getBound().transform(worldTransform, worldBound);
            }
        }
    }

    @Override
    protected void updateWorldTransforms() {
        super.updateWorldTransforms();
        computeWorldMatrix();

        if (isGrouped()) {
            groupNode.onTransformChange(this);   
        }
        
        // geometry requires lights to be sorted
        worldLights.sort(true);
    }

    /**
     * Associate this <code>Geometry</code> with a {@link GeometryGroupNode}.
     * 
     * Should only be called by the parent {@link GeometryGroupNode}.
     * 
     * @param node Which {@link GeometryGroupNode} to associate with.
     * @param startIndex The starting index of this geometry in the group.
     */
    public void associateWithGroupNode(GeometryGroupNode node, int startIndex) {
        if (isGrouped()) {
            unassociateFromGroupNode();
        }
        
        this.groupNode = node;
        this.startIndex = startIndex;
    }

    /**
     * Removes the {@link GeometryGroupNode} association from this 
     * <code>Geometry</code>.
     * 
     * Should only be called by the parent {@link GeometryGroupNode}.
     */
    public void unassociateFromGroupNode() {
        if (groupNode != null) {
            // Once the geometry is removed 
            // from the parent, the group node needs to be updated.
            groupNode.onGeometryUnassociated(this);
            groupNode = null;
            
            // change the default to -1 to make error detection easier
            startIndex = -1; 
        }
    }

    @Override
    public boolean removeFromParent() {
        return super.removeFromParent();
    }

    @Override
    protected void setParent(Node parent) {
        super.setParent(parent);
        
        // If the geometry is managed by group node we need to unassociate.
        if (parent == null && isGrouped()) {
            unassociateFromGroupNode();
        }
    }


    /**
     * Indicate that the transform of this spatial has changed and that
     * a refresh is required.
     */
    // NOTE: Spatial has an identical implementation of this method,
    // thus it was commented out.
//    @Override
//    protected void setTransformRefresh() {
//        refreshFlags |= RF_TRANSFORM;
//        setBoundRefresh();
//    }
    /**
     * Recomputes the matrix returned by {@link Geometry#getWorldMatrix() }.
     * This will require a localized transform update for this geometry.
     */
    public void computeWorldMatrix() {
        // Force a local update of the geometry's transform
        checkDoTransformUpdate();

        // Compute the cached world matrix
        cachedWorldMat.loadIdentity();
        cachedWorldMat.setRotationQuaternion(worldTransform.getRotation());
        cachedWorldMat.setTranslation(worldTransform.getTranslation());

        TempVars vars = TempVars.get();
        Matrix4f scaleMat = vars.tempMat4;
        scaleMat.loadIdentity();
        scaleMat.scale(worldTransform.getScale());
        cachedWorldMat.multLocal(scaleMat);
        vars.release();
    }

    /**
     * A {@link Matrix4f matrix} that transforms the {@link Geometry#getMesh() mesh}
     * from model space to world space. This matrix is computed based on the
     * {@link Geometry#getWorldTransform() world transform} of this geometry.
     * In order to receive updated values, you must call {@link Geometry#computeWorldMatrix() }
     * before using this method.
     * 
     * @return Matrix to transform from local space to world space
     */
    public Matrix4f getWorldMatrix() {
        return cachedWorldMat;
    }

    /**
     * Sets the model bound to use for this geometry.
     * This alters the bound used on the mesh as well via
     * {@link Mesh#setBound(com.jme3.bounding.BoundingVolume) } and
     * forces the world bounding volume to be recomputed.
     * 
     * @param modelBound The model bound to set
     */
    @Override
    public void setModelBound(BoundingVolume modelBound) {
        this.worldBound = null;
        mesh.setBound(modelBound);
        setBoundRefresh();

        // NOTE: Calling updateModelBound() would cause the mesh
        // to recompute the bound based on the geometry thus making
        // this call useless!
        //updateModelBound();
    }

    public int collideWith(Collidable other, CollisionResults results) {
        // Force bound to update
        checkDoBoundUpdate();
        // Update transform, and compute cached world matrix
        computeWorldMatrix();

        assert (refreshFlags & (RF_BOUND | RF_TRANSFORM)) == 0;

        if (mesh != null) {
            // NOTE: BIHTree in mesh already checks collision with the
            // mesh's bound
            int prevSize = results.size();
            int added = mesh.collideWith(other, cachedWorldMat, worldBound, results);
            int newSize = results.size();
            for (int i = prevSize; i < newSize; i++) {
                results.getCollisionDirect(i).setGeometry(this);
            }
            return added;
        }
        return 0;
    }

    @Override
    public void depthFirstTraversal(SceneGraphVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    protected void breadthFirstTraversal(SceneGraphVisitor visitor, Queue<Spatial> queue) {
    }

    /**
     * Determine whether this <code>Geometry</code> is managed by a 
     * {@link GeometryGroupNode} or not.
     * 
     * @return True if managed by a {@link GeometryGroupNode}.
     */
    public boolean isGrouped() {
        return groupNode != null;
    }
    
    /**
     * @deprecated Use {@link #isGrouped()} instead.
     */
    @Deprecated
    public boolean isBatched() {
        return isGrouped();
    }

    /**
     * This version of clone is a shallow clone, in other words, the
     * same mesh is referenced as the original geometry.
     * Exception: if the mesh is marked as being a software
     * animated mesh, (bind pose is set) then the positions
     * and normals are deep copied.
     */
    @Override
    public Geometry clone(boolean cloneMaterial) {
        Geometry geomClone = (Geometry) super.clone(cloneMaterial);
        
        // This geometry is managed,
        // but the cloned one is not attached to anything, hence not managed.
        if (geomClone.isGrouped()) {
            geomClone.groupNode = null;
            geomClone.startIndex = -1;
        }
        
        geomClone.cachedWorldMat = cachedWorldMat.clone();
        if (material != null) {
            if (cloneMaterial) {
                geomClone.material = material.clone();
            } else {
                geomClone.material = material;
            }
        }

        if (mesh != null && mesh.getBuffer(Type.BindPosePosition) != null) {
            geomClone.mesh = mesh.cloneForAnim();
        }

        return geomClone;
    }

    /**
     * This version of clone is a shallow clone, in other words, the
     * same mesh is referenced as the original geometry.
     * Exception: if the mesh is marked as being a software
     * animated mesh, (bind pose is set) then the positions
     * and normals are deep copied.
     */
    @Override
    public Geometry clone() {
        return clone(true);
    }

    /**
     * Create a deep clone of the geometry. This creates an identical copy of
     * the mesh with the vertex buffer data duplicated.
     */
    @Override
    public Spatial deepClone() {
        Geometry geomClone = clone(true);
        geomClone.mesh = mesh.deepClone();
        return geomClone;
    }

    @Override
    public void write(JmeExporter ex) throws IOException {
        super.write(ex);
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(mesh, "mesh", null);
        if (material != null) {
            oc.write(material.getAssetName(), "materialName", null);
        }
        oc.write(material, "material", null);
        oc.write(ignoreTransform, "ignoreTransform", false);
    }

    @Override
    public void read(JmeImporter im) throws IOException {
        super.read(im);
        InputCapsule ic = im.getCapsule(this);
        mesh = (Mesh) ic.readSavable("mesh", null);

        material = null;
        String matName = ic.readString("materialName", null);
        if (matName != null) {
            // Material name is set,
            // Attempt to load material via J3M
            try {
                material = im.getAssetManager().loadMaterial(matName);
            } catch (AssetNotFoundException ex) {
                // Cannot find J3M file.
                logger.log(Level.FINE, "Cannot locate {0} for geometry {1}", new Object[]{matName, key});
            }
        }
        // If material is NULL, try to load it from the geometry
        if (material == null) {
            material = (Material) ic.readSavable("material", null);
        }
        ignoreTransform = ic.readBoolean("ignoreTransform", false);

        if (ic.getSavableVersion(Geometry.class) == 0) {
            // Fix shared mesh (if set)
            Mesh sharedMesh = getUserData(UserData.JME_SHAREDMESH);
            if (sharedMesh != null) {
                getMesh().extractVertexData(sharedMesh);
                setUserData(UserData.JME_SHAREDMESH, null);
            }
        }
    }

    /**
     * Preloads this material for the given render manager.
     * <p>
     * Preloading the material can ensure that when the material is first
     * used for rendering, there won't be any delay since the material has
     * been already been setup for rendering.
     *
     * @param rm The render manager to preload for
     */
    public void preload(RenderManager rm) {


        this.autoSelectRenderer(rm);

        Renderer r = rm.getRenderer();

        Technique technique = material.getActiveTechnique();
        TechniqueDef techDef = technique.getDef();

        Collection<MatParam> params = material.getParamsMap().values();
        for (MatParam param : params) {
            param.apply(r, technique);
        }

        r.setShader(technique.getShader());
    }

    public void setGeometryRenderer(GeometryRenderer renderer) {
        this.renderer = renderer;
    }

    public GeometryRenderer getGeometryRenderer() {
        return renderer;
    }

    public void render(RenderManager rm) {
        // Firstly lets set the world transforms
        if (this.isIgnoreTransform()) {
            rm.setWorldMatrix(Matrix4f.IDENTITY);
        } else {
            rm.setWorldMatrix(this.getWorldMatrix());
        }

        if (rm.getLightFilter() != null) {
            rm.getFilteredLightList().clear();
            rm.getLightFilter().filterLights(this, rm.getFilteredLightList());
        }

        //if forcedTechnique we try to force it for render,
        //if it does not exists in the mat def, we check for forcedMaterial and render the geom if not null
        //else the geom is not rendered

        String tmpTech;

        //if forcedTechnique we try to force it for render,
        //if it does not exists in the mat def, we check for forcedMaterial and render the geom if not null
        //else the geom is not rendered
        if (rm.getForcedTechnique() != null) {
            if (this.getMaterial().getMaterialDef().getTechniqueDef(rm.getForcedTechnique()) != null) {
                tmpTech = this.getMaterial().getActiveTechnique() != null ? this.getMaterial().getActiveTechnique().getDef().getName() : "Default";
                this.selectTechnique(rm.getForcedTechnique(), rm);
                //saving forcedRenderState for future calls
                RenderState tmpRs = rm.getForcedRenderState();
                if (this.getMaterial().getActiveTechnique().getDef().getForcedRenderState() != null) {
                    //forcing forced technique renderState
                    rm.setForcedRenderState(this.getMaterial().getActiveTechnique().getDef().getForcedRenderState());
                }
                // use geometry's material
                this.getRendererForTechnique(rm).render();
                this.selectTechnique(tmpTech, rm);

                //restoring forcedRenderState
                rm.setForcedRenderState(tmpRs);

                //Reverted this part from revision 6197
                //If forcedTechnique does not exists, and forcedMaterial is not set, the geom MUST NOT be rendered
            } else if (rm.getForcedMaterial() != null) {
                // use forced material
                Material oldMaterial = this.getMaterial();

                this.setMaterial(rm.getForcedMaterial());
                this.getRendererForTechnique(rm).render();

                this.setMaterial(oldMaterial);
            }
        } else if (rm.getForcedMaterial() != null) {
            // use forced material
            Material oldMaterial = this.getMaterial();

            this.setMaterial(rm.getForcedMaterial());
            this.getRendererForTechnique(rm).render();

            this.setMaterial(oldMaterial);
        } else {
            this.autoSelectRenderer(rm);
            this.getRendererForTechnique(rm).render();
        }
    }

    public void autoSelectRenderer(RenderManager rm) {
        if (this.getMaterial().getActiveTechnique() == null) {
            selectTechnique("Default", rm);
        } else {
            this.material.getActiveTechnique().makeCurrent(this.material.getMaterialDef().getAssetManager(), false, rm.getRenderer().getCaps(), rm);
            this.setGeometryRenderer(this.getRendererForTechnique(rm));
        }
    }

    private GeometryRenderer getRendererForTechnique(RenderManager rm) {

        switch (material.getActiveTechnique().getDef().getLightMode()) {
            case Disable:
                return new NoLightGeometryRenderer(this, rm);
            case SinglePass:
                return new SinglePassGeometryRenderer(this, rm);
            case MultiPass:
                return new MultiPassGeometryRenderer(this, rm);
            default:
                throw new IllegalArgumentException("OpenGL1 is not supported");
        }
    }

    /**
     * Select the technique to use for rendering this Geometry.
     * <p>
     * If <code>name</code> is "Default", then one of the
     * {@link MaterialDef#getDefaultTechniques() default techniques}
     * on the material will be selected. Otherwise, the named technique
     * will be found in the material definition.
     * <p>
     * Any candidate technique for selection (either default or named)
     * must be verified to be compatible with the system, for that, the
     * <code>renderManager</code> is queried for capabilities.
     *
     * @param name The name of the technique to select, pass "Default" to
     * select one of the default techniques.
     * @param renderManager The {@link RenderManager render manager}
     * to query for capabilities.
     *
     * @throws IllegalArgumentException If "Default" is passed and no default
     * techniques are available on the material definition, or if a name
     * is passed but there's no technique by that name.
     * @throws UnsupportedOperationException If no candidate technique supports
     * the system capabilities.
     */
    public void selectTechnique(String name, RenderManager renderManager) {
        Map<String, Technique> techniques = this.material.getTechiques();
        Technique tech = techniques.get(name);
        MaterialDef def = this.material.getMaterialDef();

        Technique currentlyUsedTechnique = this.material.getActiveTechnique();

        // When choosing technique, we choose one that
        // supports all the caps.
        EnumSet<Caps> rendererCaps = renderManager.getRenderer().getCaps();
        if (tech == null) {

            if (name.equals("Default")) {
                List<TechniqueDef> techDefs = def.getDefaultTechniques();
                if (techDefs == null || techDefs.isEmpty()) {
                    throw new IllegalArgumentException("No default techniques are available on material '" + def.getName() + "'");
                }

                TechniqueDef lastTech = null;
                for (TechniqueDef techDef : techDefs) {
                    if (rendererCaps.containsAll(techDef.getRequiredCaps())) {
                        // use the first one that supports all the caps
                        tech = new Technique(this.material, techDef);
                        techniques.put(name, tech);
                        if(tech.getDef().getLightMode() == renderManager.getPreferredLightMode() ||
                                tech.getDef().getLightMode() == TechniqueDef.LightMode.Disable){
                            break;
                        }
                    }
                    lastTech = techDef;
                }
                if (tech == null) {
                    throw new UnsupportedOperationException("No default technique on material '" + def.getName() + "'\n"
                            + " is supported by the video hardware. The caps "
                            + lastTech.getRequiredCaps() + " are required.");
                }

            } else {
                // create "special" technique instance
                TechniqueDef techDef = def.getTechniqueDef(name);
                if (techDef == null) {
                    throw new IllegalArgumentException("For material " + def.getName() + ", technique not found: " + name);
                }

                if (!rendererCaps.containsAll(techDef.getRequiredCaps())) {
                    throw new UnsupportedOperationException("The explicitly chosen technique '" + name + "' on material '" + def.getName() + "'\n"
                            + "requires caps " + techDef.getRequiredCaps() + " which are not "
                            + "supported by the video renderer");
                }

                tech = new Technique(this.material, techDef);
                techniques.put(name, tech);
            }
        } else if (currentlyUsedTechnique == tech) {
            // attempting to switch to an already
            // active technique.
            if (this.getGeometryRenderer() == null) {
                this.setGeometryRenderer(this.getRendererForTechnique(renderManager));
            }

            return;
        }

        this.material.setActiveTechnique(tech);
        tech.makeCurrent(def.getAssetManager(), true, rendererCaps, renderManager);

        this.setGeometryRenderer(this.getRendererForTechnique(renderManager));

        // shader was changed
        // TODO: sortingId = -1;
    }

}