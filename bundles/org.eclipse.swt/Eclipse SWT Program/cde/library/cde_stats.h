/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifdef NATIVE_STATS
extern int CDE_nativeFunctionCount;
extern int CDE_nativeFunctionCallCount[];
extern char* CDE_nativeFunctionNames[];
#define CDE_NATIVE_ENTER(env, that, func) CDE_nativeFunctionCallCount[func]++;
#define CDE_NATIVE_EXIT(env, that, func) 
#else
#ifndef CDE_NATIVE_ENTER
#define CDE_NATIVE_ENTER(env, that, func) 
#endif
#ifndef CDE_NATIVE_EXIT
#define CDE_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	DtActionArg_1sizeof_FUNC,
	_1DtActionInvoke_FUNC,
	_1DtAppInitialize_FUNC,
	_1DtDbLoad_FUNC,
	_1DtDtsDataTypeIsAction_FUNC,
	_1DtDtsDataTypeNames_FUNC,
	_1DtDtsDataTypeToAttributeValue_FUNC,
	_1DtDtsFileToDataType_FUNC,
	_1DtDtsFreeAttributeValue_FUNC,
	_1DtDtsFreeDataType_FUNC,
	_1DtDtsFreeDataTypeNames_FUNC,
	_1XtAppCreateShell_FUNC,
	_1XtCreateApplicationContext_FUNC,
	_1XtDisplayInitialize_FUNC,
	_1XtRealizeWidget_FUNC,
	_1XtResizeWidget_FUNC,
	_1XtSetMappedWhenManaged_FUNC,
	_1XtToolkitInitialize_FUNC,
	_1topLevelShellWidgetClass_FUNC,
} CDE_FUNCS;
