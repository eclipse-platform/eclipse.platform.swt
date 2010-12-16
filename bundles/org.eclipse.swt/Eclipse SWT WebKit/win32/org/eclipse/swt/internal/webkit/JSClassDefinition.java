/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.webkit;

/** @jniclass flags=cpp */
public class JSClassDefinition {
    public int version;
    /** @field cast=(JSClassAttributes) */
    public int attributes;
    /** @field cast=(const char*) */
    public int /*long*/ className;
    /** @field cast=(JSClassRef) */
    public int /*long*/ parentClass;
    /** @field cast=(const JSStaticValue*) */
    public int /*long*/ staticValues;
    /** @field cast=(const JSStaticFunction*) */
    public int /*long*/ staticFunctions;
    /** @field cast=(JSObjectInitializeCallback) */
    public int /*long*/ initialize;
    /** @field cast=(JSObjectFinalizeCallback) */
    public int /*long*/ finalize;
    /** @field cast=(JSObjectHasPropertyCallback) */
    public int /*long*/ hasProperty;
    /** @field cast=(JSObjectGetPropertyCallback) */
    public int /*long*/ getProperty;
    /** @field cast=(JSObjectSetPropertyCallback) */
    public int /*long*/ setProperty;
    /** @field cast=(JSObjectDeletePropertyCallback) */
    public int /*long*/ deleteProperty;
    /** @field cast=(JSObjectGetPropertyNamesCallback) */
    public int /*long*/ getPropertyNames;
    /** @field cast=(JSObjectCallAsFunctionCallback) */
    public int /*long*/ callAsFunction;
    /** @field cast=(JSObjectCallAsConstructorCallback) */
    public int /*long*/ callAsConstructor;
    /** @field cast=(JSObjectHasInstanceCallback) */
    public int /*long*/ hasInstance;
    /** @field cast=(JSObjectConvertToTypeCallback) */
    public int /*long*/ convertToType;
    
    public static final int sizeof = WebKit_win32.JSClassDefinition_sizeof();
}
