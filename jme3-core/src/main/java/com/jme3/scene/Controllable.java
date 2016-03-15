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
package com.jme3.scene;

import com.jme3.scene.control.Control;

/**
 *
 * @author Jaroslav Ševčík
 */
public interface Controllable {

    /**
     * Add a control to the list of controls.
     *
     * @param control The control to add.
     *
     * @see Spatial#removeControl(java.lang.Class)
     */
    void addControl(Control control);

    /**
     * Removes the first control that is an instance of the given class.
     *
     * @see Spatial#addControl(com.jme3.scene.control.Control)
     */
    void removeControl(Class<? extends Control> controlType);

    /**
     * Removes the given control from this spatial's controls.
     *
     * @param control The control to remove
     * @return True if the control was successfully removed. False if the
     * control is not assigned to this spatial.
     *
     * @see Spatial#addControl(com.jme3.scene.control.Control)
     */
    boolean removeControl(Control control);

    /**
     * <code>setBatchHint</code> alters how batching will treat this spatial.
     *
     * @param hint one of: <code>BatchHint.Never</code>,
     * <code>BatchHint.Always</code>, or <code>BatchHint.Inherit</code>
     * <p>
     * The effect of the default value (BatchHint.Inherit) may change if the
     * spatial gets re-parented.
     */
    void setBatchHint(Spatial.BatchHint hint);

    /**
     * <code>setCullHint</code> alters how view frustum culling will treat this
     * spatial.
     *
     * @param hint one of: <code>CullHint.Dynamic</code>,
     * <code>CullHint.Always</code>, <code>CullHint.Inherit</code>, or
     * <code>CullHint.Never</code>
     * <p>
     * The effect of the default value (CullHint.Inherit) may change if the
     * spatial gets re-parented.
     */
    void setCullHint(Spatial.CullHint hint);
}
