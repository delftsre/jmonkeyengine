/*
 * Copyright (c) 2009-2016 jMonkeyEngine
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
package com.jme3.scene.plugins.fbx.misc;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jme3.scene.plugins.fbx.obj.FbxObject;
import com.jme3.scene.plugins.fbx.obj.FbxUnknownObject;

/**
 * Singleton class for dispatching FbxElements to Fbx-* classes.
 * Uses a configuration file in src/fbx/resources for constructing the dispatch table
 */
public class FbxClassTypeDispatcher {
    private static final Logger logger = Logger.getLogger(FbxClassTypeDispatcher.class.getName());

    private static FbxClassTypeDispatcher instance = null;
    private final Path configFilePath = Paths.get(System.getProperty("user.dir"), 
            "src", "fbx", "resources", "FbxImportRules.json");
    
    private Map<String, Map<String, String>> dispatchTable;
    
    /**
     * @return an existing instance (or a new one if no instance exists)
     */
    public static FbxClassTypeDispatcher getInstance() {
        if(instance == null) {
            instance = new FbxClassTypeDispatcher();
        }
        return instance;
    }
    
    /**
     * Private constructor. Reads the configuration file as is set in configFilePath and
     * converts it to a Map structure.
     */
    private FbxClassTypeDispatcher() {
        String typesJson = null;
        try {
            typesJson = new String(Files.readAllBytes(configFilePath));
        } catch (IOException e) {
            typesJson = "";
            logger.log(Level.WARNING, "Cannot load configuration for FbxObject parsing. Location: {0}", configFilePath.toString());
        }
        Type type = new TypeToken<Map<String, Map<String, String>>>(){}.getType();
        dispatchTable = new Gson().fromJson(typesJson, type);
    }
    
    /**
     * this method determines which type belongs to the given Strings.
     * @param elementName name of the top-level element
     * @param subclassName name of the sub-level element
     * @return the class corresponding to the given arguments as found in the dispatchTable. 
     *      If no type can be cound, it returns FbxUnknownObject.class
     */
    public Class<? extends FbxObject> dispatchType(String elementName, String subclassName) {
        Map<String, String> mainType = dispatchTable.get(elementName);
        Class<? extends FbxObject> res = FbxUnknownObject.class;
        
        if(mainType == null){
            mainType = dispatchTable.get("*");
        }
        
        try {
            if(mainType.containsKey(subclassName)) {
                res = (Class<? extends FbxObject>) Class.forName(mainType.get(subclassName));
            } else if (mainType.containsKey("*")) {
                res = (Class<? extends FbxObject>) Class.forName(mainType.get("*"));   
            }
        } catch (ClassNotFoundException cnfe) {
            logger.log(Level.WARNING, "Configuration file contains class that cannot be found.", cnfe);
        }
        return res;
    }
}
