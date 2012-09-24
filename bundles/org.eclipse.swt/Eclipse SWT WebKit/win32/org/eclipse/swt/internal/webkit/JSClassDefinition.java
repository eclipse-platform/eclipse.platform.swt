/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
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
    public long /*int*/ className;
    /** @field cast=(JSClassRef) */
    public long /*int*/ parentClass;
    /** @field cast=(const JSStaticValue*) */
    public long /*int*/ staticValues;
    /** @field cast=(const JSStaticFunction*) */
    public long /*int*/ staticFunctions;
    /** @field cast=(JSObjectInitializeCallback) */
    public long /*int*/ initialize;
    /** @field cast=(JSObjectFinalizeCallback) */
    public long /*int*/ finalize;
    /** @field cast=(JSObjectHasPropertyCallback) */
    public long /*int*/ hasProperty;
    /** @field cast=(JSObjectGetPropertyCallback) */
    public long /*int*/ getProperty;
    /** @field cast=(JSObjectSetPropertyCallback) */
    public long /*int*/ setProperty;
    /** @field cast=(JSObjectDeletePropertyCallback) */
    public long /*int*/ deleteProperty;
    /** @field cast=(JSObjectGetPropertyNamesCallback) */
    public long /*int*/ getPropertyNames;
    /** @field cast=(JSObjectCallAsFunctionCallback) */
    public long /*int*/ callAsFunction;
    /** @field cast=(JSObjectCallAsConstructorCallback) */
    public long /*int*/ callAsConstructor;
    /** @field cast=(JSObjectHasInstanceCallback) */
    public long /*int*/ hasInstance;
    /** @field cast=(JSObjectConvertToTypeCallback) */
    public long /*int*/ convertToType;

    public static final int sizeof = WebKit_win32.JSClassDefinition_sizeof();
}
