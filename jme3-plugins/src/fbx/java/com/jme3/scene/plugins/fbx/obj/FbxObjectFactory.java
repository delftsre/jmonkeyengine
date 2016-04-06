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
import com.jme3.scene.plugins.fbx.file.FbxElement;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Responsible for producing FBX objects given an FBXElement.
 */
public final class FbxObjectFactory {
    
    /**
     * Automatically create an FBXObject by inspecting its class / subclass
     * properties.
     * 
     * @param element The element from which to create an object.
     * @param assetManager AssetManager to load dependent resources
     * @param sceneFolderName Folder relative to which resources shall be loaded
     * @return The object, or null if not supported (?)
     */
    public static FbxObject createObject(FbxElement element, AssetManager assetManager, String sceneFolderName) {
        String elementName = element.id;
        String subclassName = element.getSubclassName();
        
        Class<? extends FbxObject> javaFbxClass = element.resolveFbxClass();
        
        if (javaFbxClass != null) {
            try {
                // This object is supported by FBX importer, create new instance.
                // Import the data into the object from the element, then return it.
                Constructor<? extends FbxObject> ctor = javaFbxClass.getConstructor(AssetManager.class, String.class);
                FbxObject obj = ctor.newInstance(assetManager, sceneFolderName);
                obj.fromElement(element);
                
                String subClassName = elementName + ", " + subclassName;
                if (obj.assetManager == null) {
                    throw new IllegalStateException("FBXObject subclass (" + subClassName + 
                                                    ") forgot to call super() in their constructor");
                } else if (obj.className == null) {
                    throw new IllegalStateException("FBXObject subclass (" + subClassName + 
                                                    ") forgot to call super.fromElement() in their fromElement() implementation");
                }
                return obj;
            } catch (InvocationTargetException ex) {
                // Programmer error.
                throw new IllegalStateException(ex);
            } catch (NoSuchMethodException ex) {
                // Programmer error.
                throw new IllegalStateException(ex);
            } catch (InstantiationException ex) {
                // Programmer error.
                throw new IllegalStateException(ex);
            } catch (IllegalAccessException ex) {
                // Programmer error.
                throw new IllegalStateException(ex);
            }
        }
        
        // Not supported object.
        return null;
    }
}
