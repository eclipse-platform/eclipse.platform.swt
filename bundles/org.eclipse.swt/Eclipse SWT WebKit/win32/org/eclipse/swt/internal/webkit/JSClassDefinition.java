/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
	public long className;
	/** @field cast=(JSClassRef) */
	public long parentClass;
	/** @field cast=(const JSStaticValue*) */
	public long staticValues;
	/** @field cast=(const JSStaticFunction*) */
	public long staticFunctions;
	/** @field cast=(JSObjectInitializeCallback) */
	public long initialize;
	/** @field cast=(JSObjectFinalizeCallback) */
	public long finalize;
	/** @field cast=(JSObjectHasPropertyCallback) */
	public long hasProperty;
	/** @field cast=(JSObjectGetPropertyCallback) */
	public long getProperty;
	/** @field cast=(JSObjectSetPropertyCallback) */
	public long setProperty;
	/** @field cast=(JSObjectDeletePropertyCallback) */
	public long deleteProperty;
	/** @field cast=(JSObjectGetPropertyNamesCallback) */
	public long getPropertyNames;
	/** @field cast=(JSObjectCallAsFunctionCallback) */
	public long callAsFunction;
	/** @field cast=(JSObjectCallAsConstructorCallback) */
	public long callAsConstructor;
	/** @field cast=(JSObjectHasInstanceCallback) */
	public long hasInstance;
	/** @field cast=(JSObjectConvertToTypeCallback) */
	public long convertToType;

	public static final int sizeof = WebKit_win32.JSClassDefinition_sizeof();
}
