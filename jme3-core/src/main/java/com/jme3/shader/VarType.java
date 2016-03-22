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
package com.jme3.shader;

import com.jme3.asset.TextureKey;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;

public enum VarType {

    Float("float") {
        @Override
        public String getValueAsString(Object value) {
            return value.toString();
        }
    },
    Vector2("vec2") {
        @Override
        public String getValueAsString(Object value) {
            Vector2f v2 = (Vector2f) value;
            return v2.getX() + " " + v2.getY();
        }
    },
    Vector3("vec3") {
        @Override
        public String getValueAsString(Object value) {
            Vector3f v3 = (Vector3f) value;
            return v3.getX() + " " + v3.getY() + " " + v3.getZ();
        }
    },
    Vector4("vec4") {
        @Override
        public String getValueAsString(Object value) {
            // can be either ColorRGBA, Vector4f or Quaternion
            if (value instanceof Vector4f) {
                Vector4f v4 = (Vector4f) value;
                return v4.getX() + " " + v4.getY() + " " + v4.getZ() + " " + v4.getW();
            } else if (value instanceof ColorRGBA) {
                ColorRGBA color = (ColorRGBA) value;
                return color.getRed() + " " + color.getGreen() + " " + color.getBlue() + " " + color.getAlpha();
            } else if (value instanceof Quaternion) {
                Quaternion quat = (Quaternion) value;
                return quat.getX() + " " + quat.getY() + " " + quat.getZ() + " " + quat.getW();
            } else {
                throw new UnsupportedOperationException("Unexpected Vector4 type: " + value);
            }
        }
    },

    IntArray(true, false, "int"),
    FloatArray(true, false, "float"),
    Vector2Array(true, false, "vec2") {
        @Override
        public String getValueAsString(Object value) {
/* 
        This may get used at a later point of time
        When arrays can be inserted in J3M files

            Vector2f[] v2Arr = (Vector2f[]) value;
            String v2str = "";
            for (int i = 0; i < v2Arr.length ; i++) {
                v2str += v2Arr[i].getX() + " " + v2Arr[i].getY() + "\n";
            }
            return v2str;
*/
            return null;
        }
    },
    Vector3Array(true, false, "vec3") {
        @Override
        public String getValueAsString(Object value) {
            return null;
            /* 
        Vector3f[] v3Arr = (Vector3f[]) value;
        String v3str = "";
        for (int i = 0; i < v3Arr.length ; i++) {
            v3str += v3Arr[i].getX() + " "
                    + v3Arr[i].getY() + " "
                    + v3Arr[i].getZ() + "\n";
        }
        return v3str;
*/ 
        }
    },
    Vector4Array(true, false, "vec4") {
        @Override
        public String getValueAsString(Object value) {
            return null;
            /* 
                // can be either ColorRGBA, Vector4f or Quaternion
                if (value instanceof Vector4f) {
                    Vector4f[] v4arr = (Vector4f[]) value;
                    String v4str = "";
                    for (int i = 0; i < v4arr.length ; i++) {
                        v4str += v4arr[i].getX() + " "
                                + v4arr[i].getY() + " "
                                + v4arr[i].getZ() + " "
                                + v4arr[i].getW() + "\n";
                    }
                    return v4str;
                } else if (value instanceof ColorRGBA) {
                    ColorRGBA[] colorArr = (ColorRGBA[]) value;
                    String colStr = "";
                    for (int i = 0; i < colorArr.length ; i++) {
                        colStr += colorArr[i].getRed() + " "
                                + colorArr[i].getGreen() + " "
                                + colorArr[i].getBlue() + " "
                                + colorArr[i].getAlpha() + "\n";
                    }
                    return colStr;
                } else if (value instanceof Quaternion) {
                    Quaternion[] quatArr = (Quaternion[]) value;
                    String quatStr = "";
                    for (int i = 0; i < quatArr.length ; i++) {
                        quatStr += quatArr[i].getX() + " "
                                + quatArr[i].getY() + " "
                                + quatArr[i].getZ() + " "
                                + quatArr[i].getW() + "\n";
                    }
                    return quatStr;
                } else {
                    throw new UnsupportedOperationException("Unexpected Vector4Array type: " + value);
                }
*/
        }
    },

    Boolean("bool") {
        @Override
        public String getValueAsString(Object value) {
            return value.toString();
        }
    },

    Matrix3(true, false, "mat3"),
    Matrix4(true, false, "mat4"),

    Matrix3Array(true, false, "mat3"),
    Matrix4Array(true, false, "mat4"),
    
    TextureBuffer(false, true, "sampler1D|sampler1DShadow") {
        @Override
        public String getValueAsString(Object value) {
            return getValueAsStringFromTexture((Texture) value);
        }
    },
    Texture2D(false, true, "sampler2D|sampler2DShadow") {
        @Override
        public String getValueAsString(Object value) {
            return getValueAsStringFromTexture((Texture) value);
        }
    },
    Texture3D(false, true, "sampler3D") {
        @Override
        public String getValueAsString(Object value) {
            return getValueAsStringFromTexture((Texture) value);
        }
    },
    TextureArray(false, true, "sampler2DArray") {
        @Override
        public String getValueAsString(Object value) {
            return getValueAsStringFromTexture((Texture) value);
        }
    },
    TextureCubeMap(false, true, "samplerCube") {
        @Override
        public String getValueAsString(Object value) {
            return getValueAsStringFromTexture((Texture) value);
        }
    },
    Int("int") {
        @Override
        public String getValueAsString(Object value) {
            return value.toString();
        }
    };

    private boolean usesMultiData = false;
    private boolean textureType = false;
    private String glslType;

    
    VarType(String glslType) {
        this.glslType = glslType;
    }

    VarType(boolean multiData, boolean textureType, String glslType) {
        usesMultiData = multiData;
        this.textureType = textureType;
        this.glslType = glslType;
    }

    private static String getWrapMode(Texture texVal, Texture.WrapAxis axis) {
        WrapMode mode = WrapMode.EdgeClamp;
        try {
            mode = texVal.getWrap(axis);
        } catch (IllegalArgumentException e) {
            //this axis doesn't exist on the texture
            return "";
        }
        if (mode != WrapMode.EdgeClamp) {
            return "Wrap" + mode.name() + "_" + axis.name() + " ";
        }
        return "";
    }

    public static String getValueAsStringFromTexture(Texture texVal) {
        TextureKey texKey = (TextureKey) texVal.getKey();
        if (texKey == null) {
            throw new UnsupportedOperationException("The specified MatParam cannot be represented in J3M");
        }

        StringBuilder ret = new StringBuilder();
        if (texKey.isFlipY()) {
            ret.append("Flip ");
        }
        // Wrap mode
        ret.append(getWrapMode(texVal, Texture.WrapAxis.S));
        ret.append(getWrapMode(texVal, Texture.WrapAxis.T));
        ret.append(getWrapMode(texVal, Texture.WrapAxis.R));

        //Min and Mag filter
        Texture.MinFilter def =  Texture.MinFilter.BilinearNoMipMaps;
        if(texVal.getImage().hasMipmaps() || texKey.isGenerateMips()) {
            def = Texture.MinFilter.Trilinear;
        }
        if(texVal.getMinFilter() != def) {
            ret.append("Min").append(texVal.getMinFilter().name()).append(" ");
        }

        if(texVal.getMagFilter() != Texture.MagFilter.Bilinear) {
            ret.append("Min").append(texVal.getMagFilter().name()).append(" ");
        }
        ret.append("\"").append(texKey.getName()).append("\"");
        return ret.toString();
    }

    public String getValueAsString(Object value) {
        return null;
    }

    public boolean isTextureType() {
        return textureType;
    }

    public boolean usesMultiData() {
        return usesMultiData;
    }

    public String getGlslType() {
        return glslType;
    }    

}
