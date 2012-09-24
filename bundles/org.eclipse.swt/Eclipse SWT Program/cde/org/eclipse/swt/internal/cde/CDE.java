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
public static final native boolean _DtAppInitialize(long /*int*/ appContext, long /*int*/ display, long /*int*/ topWiget, byte[] appName, byte[] appClass);
public static final boolean DtAppInitialize(long /*int*/ appContext, long /*int*/ display, long /*int*/ topWiget, byte[] appName, byte[] appClass) {
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
public static final native long /*int*/ _DtDtsDataTypeNames();
public static final long /*int*/ DtDtsDataTypeNames() {
	lock.lock();
	try {
		return _DtDtsDataTypeNames();
	} finally {
		lock.unlock();
	}
}
/** @param fileName cast=(char *) */
public static final native long /*int*/ _DtDtsFileToDataType(byte[] fileName);
public static final long /*int*/ DtDtsFileToDataType(byte[] fileName) {
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
public static final native long /*int*/ _DtDtsDataTypeToAttributeValue(byte[] dataType, byte[] attrName, byte[] optName);
public static final long /*int*/ DtDtsDataTypeToAttributeValue(byte[] dataType, byte[] attrName, byte[] optName) {
	lock.lock();
	try {
		return _DtDtsDataTypeToAttributeValue(dataType, attrName, optName);
	} finally {
		lock.unlock();
	}
}
/** @param dataType cast=(char *) */
public static final native void _DtDtsFreeDataType(long /*int*/ dataType);
public static final void DtDtsFreeDataType(long /*int*/ dataType) {
	lock.lock();
	try {
		_DtDtsFreeDataType(dataType);
	} finally {
		lock.unlock();
	}
}
/** @param dataTypeList cast=(char **) */
public static final native void _DtDtsFreeDataTypeNames(long /*int*/ dataTypeList);
public static final void DtDtsFreeDataTypeNames(long /*int*/ dataTypeList) {
	lock.lock();
	try {
		_DtDtsFreeDataTypeNames(dataTypeList);
	} finally {
		lock.unlock();
	}
}
/** @param attrValue cast=(char *) */
public static final native void _DtDtsFreeAttributeValue(long /*int*/ attrValue);
public static final void DtDtsFreeAttributeValue(long /*int*/ attrValue) {
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
public static final native long _DtActionInvoke(long /*int*/ topWidget, byte[] action, DtActionArg args, int argCount, byte[] termOpts, byte[] execHost, byte[] contextDir, int useIndicator, long /*int*/ callback, long /*int*/ clientData);
public static final long DtActionInvoke(long /*int*/ topWidget, byte[] action, DtActionArg args, int argCount, byte[] termOpts, byte[] execHost, byte[] contextDir, int useIndicator, long /*int*/ callback, long /*int*/ clientData) {
	lock.lock();
	try {
		return _DtActionInvoke(topWidget, action, args, argCount, termOpts, execHost, contextDir, useIndicator, callback, clientData);
	} finally {
		lock.unlock();
	}
}
/** @method flags=const */
public static final native long /*int*/ _topLevelShellWidgetClass();
public static final long /*int*/ topLevelShellWidgetClass() {
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
public static final native long /*int*/ _XtAppCreateShell(byte[] appName, byte[] appClass, long /*int*/ widgetClass, long /*int*/ display, long /*int*/ [] argList, int argCount);
public static final long /*int*/ XtAppCreateShell(byte[] appName, byte[] appClass, long /*int*/ widgetClass, long /*int*/ display, long /*int*/ [] argList, int argCount) {
	lock.lock();
	try {
		return _XtAppCreateShell(appName, appClass, widgetClass, display, argList, argCount);
	} finally {
		lock.unlock();
	}
}
public static final native long /*int*/ _XtCreateApplicationContext();
public static final long /*int*/ XtCreateApplicationContext() {
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
public static final native void _XtDisplayInitialize(long /*int*/ app_context, long /*int*/ display, byte[] appName, byte[] appClass, long /*int*/ options, int num_options, long /*int*/ [] argc, int argv);
public static final void XtDisplayInitialize(long /*int*/ appContext, long /*int*/ display, byte[] appName, byte[] appClass, long /*int*/ options, int num_options, long /*int*/ [] argc, int argv) {
	lock.lock();
	try {
		_XtDisplayInitialize(appContext, display, appName, appClass, options, num_options, argc, argv);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtRealizeWidget(long /*int*/ widget);
public static final void XtRealizeWidget(long /*int*/ widget) {
	lock.lock();
	try {
		_XtRealizeWidget(widget);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtResizeWidget(long /*int*/ widget, int width, int height, int borderWidth);
public static final void XtResizeWidget(long /*int*/ widget, int width, int height, int borderWidth) {
	lock.lock();
	try {
		_XtResizeWidget(widget, width, height, borderWidth);
	} finally {
		lock.unlock();
	}
}
/** @param widget cast=(Widget) */
public static final native void _XtSetMappedWhenManaged(long /*int*/ widget, boolean flag);
public static final void XtSetMappedWhenManaged(long /*int*/ widget, boolean flag) {
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
