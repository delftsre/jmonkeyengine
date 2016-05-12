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
package com.jme3.material;

import com.jme3.asset.TextureKey;
import com.jme3.export.*;
import com.jme3.math.*;
import com.jme3.renderer.Renderer;
import com.jme3.shader.VarType;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.WrapMode;
import java.io.IOException;

/**
 * Describes a material parameter. This is used for both defining a name and type
 * as well as a material parameter value.
 *
 * @author Kirill Vainer
 */
public class MatParam implements Savable, Cloneable {

    protected VarType type;
    protected String name;
    protected String prefixedName;
    protected Object value;

    /**
     * Create a new material parameter. For internal use only.
     */
    public MatParam(VarType type, String name, Object value) {
        this.type = type;
        this.name = name;
        this.prefixedName = "m_" + name;
        this.value = value;
    }

    /**
     * Serialization only. Do not use.
     */
    public MatParam() {
    }

    /**
     * Returns the material parameter type.
     *
     * @return the material parameter type.
     */
    public VarType getVarType() {
        return type;
    }

    /**
     * Returns the name of the material parameter.
     * @return the name of the material parameter.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name with "m_" prefixed to it.
     *
     * @return the name with "m_" prefixed to it
     */
    public String getPrefixedName() {
        return prefixedName;
    }

    /**
     * Used internally
     * @param name
     */
    void setName(String name) {
        this.name = name;
        this.prefixedName = "m_" + name;
    }

    /**
     * Returns the value of this material parameter.
     * <p>
     * Material parameters that are used for material definitions
     * will not have a value, unless there's a default value declared
     * in the definition.
     *
     * @return the value of this material parameter.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value of this material parameter.
     * <p>
     * It is assumed the value is of the same {@link MatParam#getVarType() type}
     * as this material parameter.
     *
     * @param value the value of this material parameter.
     */
    public void setValue(Object value) {
        this.value = value;
    }

    void apply(Renderer r, Technique technique) {
        technique.updateUniformParam(getPrefixedName(), getVarType(), getValue());
    }

    /**
     * Returns the material parameter value as it would appear in a J3M
     * file. E.g.<br/>
     * <code>
     * MaterialParameters {<br/>
     *     ABC : 1 2 3 4<br/>
     * }<br/>
     * </code>
     * Assuming "ABC" is a Vector4 parameter, then the value
     * "1 2 3 4" would be returned by this method.
     * <br/><br/>
     * @return material parameter value as it would appear in a J3M file.
     */
    public String getValueAsString() {
        return getVarType().getValueAsString(value);
    }

    @Override
    public MatParam clone() {
        try {
            MatParam param = (MatParam) super.clone();
            return param;
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError();
        }
    }

    public void write(JmeExporter ex) throws IOException {
        OutputCapsule oc = ex.getCapsule(this);
        oc.write(type, "varType", null);
        oc.write(name, "name", null);
        if (value instanceof Savable) {
            Savable s = (Savable) value;
            oc.write(s, "value_savable", null);
        } else if (value instanceof Float) {
            Float f = (Float) value;
            oc.write(f.floatValue(), "value_float", 0f);
        } else if (value instanceof Integer) {
            Integer i = (Integer) value;
            oc.write(i.intValue(), "value_int", 0);
        } else if (value instanceof Boolean) {
            Boolean b = (Boolean) value;
            oc.write(b.booleanValue(), "value_bool", false);
        }
    }

    public void read(JmeImporter im) throws IOException {
        InputCapsule ic = im.getCapsule(this);
        type = ic.readEnum("varType", VarType.class, null);
        name = ic.readString("name", null);
        prefixedName = "m_" + name;
        switch (getVarType()) {
            case Boolean:
                value = ic.readBoolean("value_bool", false);
                break;
            case Float:
                value = ic.readFloat("value_float", 0f);
                break;
            case Int:
                value = ic.readInt("value_int", 0);
                break;
            default:
                value = ic.readSavable("value_savable", null);
                break;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MatParam other = (MatParam) obj;
        if (this.type != other.type) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if (this.value != other.value && (this.value == null || !this.value.equals(other.value))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 59 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 59 * hash + (this.value != null ? this.value.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        if (value != null) {
            return type.name() + " " + name + " : " + getValueAsString();
        } else {
            return type.name() + " " + name;
        }
    }
}
