/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.webkit;


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
    
    public static final int sizeof = WebKitGTK.JSClassDefinition_sizeof();
}
