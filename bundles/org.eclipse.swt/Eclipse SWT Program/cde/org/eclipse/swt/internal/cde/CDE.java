/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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

/** Natives */
public static final native boolean _DtAppInitialize(int appContext, int display, int topWiget, byte[] appName, byte[] appClass);
public static final boolean DtAppInitialize(int appContext, int display, int topWiget, byte[] appName, byte[] appClass) {
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
public static final native int _DtDtsDataTypeNames();
public static final int DtDtsDataTypeNames() {
	lock.lock();
	try {
		return _DtDtsDataTypeNames();
	} finally {
		lock.unlock();
	}
}
public static final native int _DtDtsFileToDataType(byte[] fileName);
public static final int DtDtsFileToDataType(byte[] fileName) {
	lock.lock();
	try {
		return _DtDtsFileToDataType(fileName);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _DtDtsDataTypeIsAction(byte[] dataType);
public static final boolean DtDtsDataTypeIsAction(byte[] dataType) {
	lock.lock();
	try {
		return _DtDtsDataTypeIsAction(dataType);
	} finally {
		lock.unlock();
	}
}
public static final native int _DtDtsDataTypeToAttributeValue(byte[] dataType, byte[] attrName, byte[] optName);
public static final int DtDtsDataTypeToAttributeValue(byte[] dataType, byte[] attrName, byte[] optName) {
	lock.lock();
	try {
		return _DtDtsDataTypeToAttributeValue(dataType, attrName, optName);
	} finally {
		lock.unlock();
	}
}
public static final native void _DtDtsFreeDataType(int dataType);
public static final void DtDtsFreeDataType(int dataType) {
	lock.lock();
	try {
		_DtDtsFreeDataType(dataType);
	} finally {
		lock.unlock();
	}
}
public static final native void _DtDtsFreeDataTypeNames(int dataTypeList);
public static final void DtDtsFreeDataTypeNames(int dataTypeList) {
	lock.lock();
	try {
		_DtDtsFreeDataTypeNames(dataTypeList);
	} finally {
		lock.unlock();
	}
}
public static final native void _DtDtsFreeAttributeValue(int attrValue);
public static final void DtDtsFreeAttributeValue(int attrValue) {
	lock.lock();
	try {
		_DtDtsFreeAttributeValue(attrValue);
	} finally {
		lock.unlock();
	}
}
public static final native int _DtActionInvoke(int topWidget, byte[] action, DtActionArg args, int argCount, byte[] termOpts, byte[] execHost, byte[] contextDir, int useIndicator, int callback, int clientData);
public static final int DtActionInvoke(int topWidget, byte[] action, DtActionArg args, int argCount, byte[] termOpts, byte[] execHost, byte[] contextDir, int useIndicator, int callback, int clientData) {
	lock.lock();
	try {
		return _DtActionInvoke(topWidget, action, args, argCount, termOpts, execHost, contextDir, useIndicator, callback, clientData);
	} finally {
		lock.unlock();
	}
}
public static final native int _topLevelShellWidgetClass();
public static final int topLevelShellWidgetClass() {
	lock.lock();
	try {
		return _topLevelShellWidgetClass();
	} finally {
		lock.unlock();
	}
}
public static final native int _XtAppCreateShell(byte[] appName, byte[] appClass, int widgetClass, int display, int[] argList, int argCount);
public static final int XtAppCreateShell(byte[] appName, byte[] appClass, int widgetClass, int display, int[] argList, int argCount) {
	lock.lock();
	try {
		return _XtAppCreateShell(appName, appClass, widgetClass, display, argList, argCount);
	} finally {
		lock.unlock();
	}
}
public static final native int _XtCreateApplicationContext();
public static final int XtCreateApplicationContext() {
	lock.lock();
	try {
		return _XtCreateApplicationContext();
	} finally {
		lock.unlock();
	}
}
public static final native void _XtDisplayInitialize(int app_context, int display, byte[] appName, byte[] appClass, int options, int num_options, int[] argc, int argv);
public static final void XtDisplayInitialize(int appContext, int display, byte[] appName, byte[] appClass, int options, int num_options, int[] argc, int argv) {
	lock.lock();
	try {
		_XtDisplayInitialize(appContext, display, appName, appClass, options, num_options, argc, argv);
	} finally {
		lock.unlock();
	}
}
public static final native void _XtRealizeWidget(int widget);
public static final void XtRealizeWidget(int widget) {
	lock.lock();
	try {
		_XtRealizeWidget(widget);
	} finally {
		lock.unlock();
	}
}
public static final native void _XtResizeWidget(int widget, int width, int height, int borderWidth);
public static final void XtResizeWidget(int widget, int width, int height, int borderWidth) {
	lock.lock();
	try {
		_XtResizeWidget(widget, width, height, borderWidth);
	} finally {
		lock.unlock();
	}
}
public static final native void _XtSetMappedWhenManaged(int widget, boolean flag);
public static final void XtSetMappedWhenManaged(int widget, boolean flag) {
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
