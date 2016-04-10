/*
 * Copyright (c) 2009-2015 jMonkeyEngine
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
package com.jme3.scene.plugins.fbx.obj;

import com.jme3.asset.AssetManager;
import com.jme3.scene.plugins.fbx.anim.FbxAnimCurve;
import com.jme3.scene.plugins.fbx.anim.FbxAnimCurveNode;
import com.jme3.scene.plugins.fbx.anim.FbxAnimLayer;
import com.jme3.scene.plugins.fbx.anim.FbxAnimStack;
import com.jme3.scene.plugins.fbx.anim.FbxBindPose;
import com.jme3.scene.plugins.fbx.anim.FbxCluster;
import com.jme3.scene.plugins.fbx.anim.FbxLimbNode;
import com.jme3.scene.plugins.fbx.anim.FbxSkinDeformer;
import com.jme3.scene.plugins.fbx.file.FbxElement;
import com.jme3.scene.plugins.fbx.material.FbxImage;
import com.jme3.scene.plugins.fbx.material.FbxMaterial;
import com.jme3.scene.plugins.fbx.material.FbxTexture;
import com.jme3.scene.plugins.fbx.mesh.FbxMesh;
import com.jme3.scene.plugins.fbx.node.FbxNode;
import com.jme3.scene.plugins.fbx.node.FbxNullAttribute;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.rmi.activation.UnknownObjectException;
import java.util.ArrayList;

@RunWith(MockitoJUnitRunner.class)
public class FbxObjectFactoryTest {

	@Mock
	private AssetManager assetManager;
	
	private String sceneFolderName = "this-is-a-scenefolder-path";
	
	@Test(expected=NullPointerException.class)
	public void testNull() {
		FbxObjectFactory.createObject(null, assetManager, sceneFolderName);
	}
	
	@Test
	public void testWithNodeAttributeClassTwoAttributes() {
		FbxElement mockedEl =  new FbxElement(2);
	    mockedEl.properties.add("123\u0000\u0001NodeAttribute");
        mockedEl.properties.add("Root");
        mockedEl.id = "NodeAttribute";
		
		FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
		assertTrue(res instanceof FbxNullAttribute);
	}
	
	@Test
    public void testWithNodeAttributeClassThreeAttributes() {
        FbxElement mockedEl =  new FbxElement(3);
        mockedEl.properties.add(12345L);
        mockedEl.properties.add("123\u0000\u0001NodeAttribute");
        mockedEl.properties.add("Root");
        mockedEl.id = "NodeAttribute";
        
        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxNullAttribute);
    }
	
	@Test(expected=UnsupportedOperationException.class)
    public void testWithNodeAttributeClassInvalidAmountOfAttributes() {
        FbxElement mockedEl =  new FbxElement(5);
        mockedEl.properties.add(12345L);
        mockedEl.properties.add("123\u0000\u0001NodeAttribute");
        mockedEl.properties.add("Root");
        mockedEl.properties.add("Cheese");
        mockedEl.properties.add("Fromage");
        mockedEl.id = "NodeAttribute";
        
        FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
    }
	
	@Test
    public void testWithClassTwoAttributes() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001NodeAttribute");
        mockedEl.properties.add("Root");
        mockedEl.id = "NodeAttribute";
        
        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxNullAttribute);
    }
	
	@Test
	public void testWithNonExistingIdAndSubclass() {
	    FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Cheese");
        mockedEl.properties.add("Fromage");
        mockedEl.id = "Cheese";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxUnknownObject);
	}
    
	@Test
	public void testWithNodeAttributeAndLimbNode() {
	    FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001NodeAttribute");
        mockedEl.properties.add("LimbNode");
        mockedEl.id = "NodeAttribute";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxNullAttribute);
	}
	
	@Test
    public void testWithNodeAttributeAndNull() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001NodeAttribute");
        mockedEl.properties.add("Null");
        mockedEl.id = "NodeAttribute";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxNullAttribute);
    }
	
	@Test
    public void testWithNodeAttributeAndIKEffector() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001NodeAttribute");
        mockedEl.properties.add("IKEffector");
        mockedEl.id = "NodeAttribute";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxNullAttribute);
    }

	@Test
    public void testWithNodeAttributeAndFKEffector() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001NodeAttribute");
        mockedEl.properties.add("FKEffector");
        mockedEl.id = "NodeAttribute";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxNullAttribute);
    }
	
	@Test
    public void testWithNodeAttributeAndRandom() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001NodeAttribute");
        mockedEl.properties.add("Fromage");
        mockedEl.id = "NodeAttribute";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxUnknownObject);
    }
	
	@Test
    public void testWithGeometryAndMesh() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Geometry");
        mockedEl.properties.add("Mesh");
        mockedEl.id = "Geometry";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxMesh);
    }
	
	@Test
    public void testWithGeometryAndRandom() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Geometry");
        mockedEl.properties.add("Random");
        mockedEl.id = "Geometry";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxUnknownObject);
    }
	
	@Test
    public void testWithRandomAndMesh() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Fromage");
        mockedEl.properties.add("Mesh");
        mockedEl.id = "Fromage";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxUnknownObject);
    }
    
	
	@Test
    public void testWithModelAndLimbNode() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Model");
        mockedEl.properties.add("LimbNode");
        mockedEl.id = "Model";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxLimbNode);
    }
	
	@Test
    public void testWithModelAndSomethingElse() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Model");
        mockedEl.properties.add("Cheese");
        mockedEl.id = "Model";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxNode);
    }
	
	@Test
    public void testWithPoseAndBindPose() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Pose");
        mockedEl.properties.add("BindPose");
        mockedEl.id = "Pose";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxBindPose);
    }
	
	@Test
    public void testWithPoseAndSomethingElse() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Pose");
        mockedEl.properties.add("Fromage");
        mockedEl.id = "Pose";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxUnknownObject);
    }
	
	@Test
    public void testWithMaterialAndRandom() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Material");
        mockedEl.properties.add("Fromage");
        mockedEl.id = "Material";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxMaterial);
    }
	
	@Test
    public void testWithDeformerAndSkin() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Deformer");
        mockedEl.properties.add("Skin");
        mockedEl.id = "Deformer";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxSkinDeformer);
    }
	
	@Test
    public void testWithDeformerAndCluster() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Deformer");
        mockedEl.properties.add("Cluster");
        mockedEl.id = "Deformer";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxCluster);
    }
	
	@Test
    public void testWithDeformerAndRandom() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Deformer");
        mockedEl.properties.add("Fromage");
        mockedEl.id = "Deformer";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxUnknownObject);
    }
	
	@Test
    public void testWithVideoAndClip() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Video");
        mockedEl.properties.add("Clip");
        mockedEl.id = "Video";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxImage);
    }
	
	@Test
    public void testWithVideoAndRandom() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Video");
        mockedEl.properties.add("Fromage");
        mockedEl.id = "Video";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxUnknownObject);
    }
	
	@Test
    public void testWithTexture() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001Texture");
        mockedEl.properties.add("Fromage");
        mockedEl.id = "Texture";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxTexture);
    }
	
	@Test
    public void testWithAnimationStack() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001AnimationStack");
        mockedEl.properties.add("Fromage");
        mockedEl.id = "AnimationStack";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxAnimStack);
    }
	
	@Test
    public void testWithAnimationLayer() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001AnimationLayer");
        mockedEl.properties.add("Fromage");
        mockedEl.id = "AnimationLayer";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxAnimLayer);
    }
	
	   @Test
	    public void testWithAnimationCurveNode() {
	        FbxElement mockedEl =  new FbxElement(2);
	        mockedEl.properties.add("123\u0000\u0001AnimationCurveNode");
	        mockedEl.properties.add("Fromage");
	        mockedEl.id = "AnimationCurveNode";
	        
	        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
	        assertTrue(res instanceof FbxAnimCurveNode);
	    }
	
	@Test
    public void testWithAnimationCurve() {
        FbxElement mockedEl =  new FbxElement(2);
        mockedEl.properties.add("123\u0000\u0001AnimationCurve");
        mockedEl.properties.add("Fromage");
        mockedEl.id = "AnimationCurve";
        
        FbxElement mockedChild = new FbxElement(3);
        long[] l = {1L,2L};
        mockedChild.properties.add(l);
        mockedChild.properties.add("123\u0000\u0001KeyTime");
        mockedChild.properties.add("Fromage");
        mockedChild.id = "KeyTime";
        
        mockedEl.children.add(mockedChild);
        
        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        System.out.println(res);
        assertTrue(res instanceof FbxAnimCurve);
    }
	
	@Test
    public void testWithSceneInfo() {
        FbxElement mockedEl =  new FbxElement(3);
        long[] l = {1L,2L};
        mockedEl.properties.add(l);
        mockedEl.properties.add("123\u0000\u0001SceneInfo");
        mockedEl.properties.add("Fromage");
        mockedEl.id = "SceneInfo";

        FbxObject res = FbxObjectFactory.createObject(mockedEl, assetManager, sceneFolderName);
        assertTrue(res instanceof FbxUnknownObject);
    }
	
}
