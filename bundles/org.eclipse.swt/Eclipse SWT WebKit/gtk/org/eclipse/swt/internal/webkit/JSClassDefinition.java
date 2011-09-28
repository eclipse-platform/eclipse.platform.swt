/*******************************************************************************
 * Copyright (c) 2010 IBM Corporation and others. All rights reserved.
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
    public int attributes;
    /** @field cast=(const char*) */
    public int /*long*/ className;
    /** @field cast=(void*) */
    public int /*long*/ parentClass;
    /** @field cast=(const void*) */
    public int /*long*/ staticValues;
    /** @field cast=(const void*) */
    public int /*long*/ staticFunctions;
    /** @field cast=(void*) */
    public int /*long*/ initialize;
    /** @field cast=(void*) */
    public int /*long*/ finalize;
    /** @field cast=(void*) */
    public int /*long*/ hasProperty;
    /** @field cast=(void*) */
    public int /*long*/ getProperty;
    /** @field cast=(void*) */
    public int /*long*/ setProperty;
    /** @field cast=(void*) */
    public int /*long*/ deleteProperty;
    /** @field cast=(void*) */
    public int /*long*/ getPropertyNames;
    /** @field cast=(void*) */
    public int /*long*/ callAsFunction;
    /** @field cast=(void*) */
    public int /*long*/ callAsConstructor;
    /** @field cast=(void*) */
    public int /*long*/ hasInstance;
    /** @field cast=(void*) */
    public int /*long*/ convertToType;
    
    public static final int sizeof = WebKitGTK.JSClassDefinition_sizeof();
}
