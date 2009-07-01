/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cde;

import org.eclipse.swt.internal.*;

public class CDE extends Platform {

	/** Constants */
	public static final int DtACTION_FILE = 1;
	public static final String DtDTS_DA_ACTION_LIST = "ACTIONS";
	public static final String DtDTS_DA_ICON = "ICON";
	public static final String DtDTS_DA_MIME_TYPE = "MIME_TYPE";
	public static final String DtDTS_DA_NAME_TEMPLATE = "NAME_TEMPLATE";
	
	/** 64 bit */
	public static final native int DtActionArg_sizeof();

/** Natives */

/**
 * @param appContext cast=(XtAppContext)
 * @param display cast=(Display *)
 * @param topWiget cast=(Widget)
 * @param appName cast=(char *)
 * @param appClass cast=(char *)
 */
public static final native boolean _DtAppInitialize(int /*long*/ appContext, int /*long*/ display, int /*long*/ topWiget, byte[] appName, byte[] appClass);
public static final boolean DtAppInitialize(int /*long*/ appContext, int /*long*/ display, int /*long*/ topWiget, byte[] appName, byte[] appClass) {
	lock.lock();
	try {
		return _DtAppInitialize(appContext, display, topWiget, appName, appClass);
	} finally {
		lock.unlock();
	}
}
public static final native void _DtDbLoad();
public static final void DtDbLoad() {
	lock.lock();
	try {
		_DtDbLoad();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _DtDtsDataTypeNames();
public static final int /*long*/ DtDtsDataTypeNames() {
	lock.lock();
	try {
		return _DtDtsDataTypeNames();
	} finally {
		lock.unlock();
	}
}
/** @param fileName cast=(char *) */
public static final native int /*long*/ _DtDtsFileToDataType(byte[] fileName);
public static final int /*long*/ DtDtsFileToDataType(byte[] fileName) {
	lock.lock();
	try {
		return _DtDtsFileToDataType(fileName);
	} finally {
		lock.unlock();
	}
}
/** @param dataType cast=(char *) */
public static final native boolean _DtDtsDataTypeIsAction(byte[] dataType);
public static final boolean DtDtsDataTypeIsAction(byte[] dataType) {
	lock.lock();
	try {
		return _DtDtsDataTypeIsAction(dataType);
	} finally {
		lock.unlock();
	}
}
/**
 * @param dataType cast=(char *)
 * @param attrName cast=(char *)
 * @param optName cast=(char *)
 */
public static final native int /*long*/ _DtDtsDataTypeToAttributeValue(byte[] dataType, byte[] attrName, byte[] optName);
public static final int /*long*/ DtDtsDataTypeToAttributeValue(byte[] dataType, byte[] attrName, byte[] optName) {
	lock.lock();
	try {
		return _DtDtsDataTypeToAttributeValue(dataType, attrName, optName);
	} finally {
		lock.unlock();
	}
}
/** @param dataType cast=(char *) */
public static final native void _DtDtsFreeDataType(int /*long*/ dataType);
public static final void DtDtsFreeDataType(int /*long*/ dataType) {
	lock.lock();
	try {
		_DtDtsFreeDataType(dataType);
	} finally {
		lock.unlock();
	}
}
/** @param dataTypeList cast=(char **) */
public static final native void _DtDtsFreeDataTypeNames(int /*long*/ dataTypeList);
public static final void DtDtsFreeDataTypeNames(int /*long*/ dataTypeList) {
	lock.lock();
	try {
		_DtDtsFreeDataTypeNames(dataTypeList);
	} finally {
		lock.unlock();
	}
}
/** @param attrValue cast=(char *) */
public static final native void _DtDtsFreeAttributeValue(int /*long*/ attrValue);
public static final void DtDtsFreeAttributeValue(int /*long*/ attrValue) {
	lock.lock();
	try {
		_DtDtsFreeAttributeValue(attrValue);
	} finally {
		lock.unlock();
	}
}
/**
 * @param topWidget cast=(Widget)
 * @param action cast=(char *)
 * @param termOpts cast=(char *)
 * @param execHost cast=(char *)
 * @param contextDir cast=(char *)
 * @param callback cast=(DtActionCallbackProc)
 * @param clientData cast=(XtPointer)
 */
public static final native long _DtActionInvoke(int /*long*/ topWidget, byte[] action, DtActionArg args, int argCount, byte[] termOpts, byte[] execHost, byte[] contextDir, int useIndicator, int /*long*/ callback, int /*long*/ clientData);
public static final long DtActionInvoke(int /*long*/ topWidget, byte[] action, DtActionArg args, int argCount, byte[] termOpts, byte[] execHost, byte[] contextDir, int useIndicator, int /*long*/ callback, int /*long*/ clientData) {
	lock.lock();
	try {
		return _DtActionInvoke(topWidget, action, args, argCount, termOpts, execHost, contextDir, useIndicator, callback, clientData);
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native int /*long*/ _topLevelShellWidgetClass();
public static final int /*long*/ topLevelShellWidgetClass() {
	lock.lock();
	try {
		return _topLevelShellWidgetClass();
	} finally {
		lock.unlock();
	}
}
/**
 * @param appName cast=(String)
 * @param appClass cast=(String)
 * @param widgetClass cast=(WidgetClass)
 * @param display cast=(Display *)
 * @param argList cast=(ArgList)
 */
public static final native int /*long*/ _XtAppCreateShell(byte[] appName, byte[] appClass, int /*long*/ widgetClass, int /*long*/ display, int /*long*/ [] argList, int argCount);
public static final int /*long*/ XtAppCreateShell(byte[] appName, byte[] appClass, int /*long*/ widgetClass, int /*long*/ display, int /*long*/ [] argList, int argCount) {
	lock.lock();
	try {
		return _XtAppCreateShell(appName, appClass, widgetClass, display, argList, argCount);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _XtCreateApplicationContext();
public static final int /*long*/ XtCreateApplicationContext() {
	lock.lock();
	try {
		return _XtCreateApplicationContext();
	} finally {
		lock.unlock();
	}
}
/**
 * @param app_context cast=(XtAppContext)
 * @param display cast=(Display *)
 * @param appName cast=(String)
 * @param appClass cast=(String)
 * @param options cast=(XrmOptionDescRec *)
 * @param num_options cast=(Cardinal)
 * @param argc cast=(int *)
 * @param argv cast=(String *)
 */
public static final native void _XtDisplayInitialize(int /*long*/ app_context, int /*long*/ display, byte[] appName, byte[] appClass, int /*long*/ options, int num_options, int /*long*/ [] argc, int argv);
public static final void XtDisplayInitialize(int /*long*/ appContext, int /*long*/ display, byte[] appName, byte[] appClass, int /*long*/ options, int num_options, int /*long*/ [] argc, int argv) {
	lock.lock();
	try {
		_XtDisplayInitialize(appContext, display, appName, appClass, options, num_options, argc, argv);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtRealizeWidget(int /*long*/ widget);
public static final void XtRealizeWidget(int /*long*/ widget) {
	lock.lock();
	try {
		_XtRealizeWidget(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtResizeWidget(int /*long*/ widget, int width, int height, int borderWidth);
public static final void XtResizeWidget(int /*long*/ widget, int width, int height, int borderWidth) {
	lock.lock();
	try {
		_XtResizeWidget(widget, width, height, borderWidth);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtSetMappedWhenManaged(int /*long*/ widget, boolean flag);
public static final void XtSetMappedWhenManaged(int /*long*/ widget, boolean flag) {
	lock.lock();
	try {
		_XtSetMappedWhenManaged(widget, flag);
	} finally {
		lock.unlock();
	}
}
public static final native void _XtToolkitInitialize();
public static final void XtToolkitInitialize() {
	lock.lock();
	try {
		_XtToolkitInitialize();
	} finally {
		lock.unlock();
	}
}
}
