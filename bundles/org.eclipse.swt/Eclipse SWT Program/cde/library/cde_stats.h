/*******************************************************************************
* Copyright (c) 2000, 2004 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#ifdef NATIVE_STATS
extern int CDE_nativeFunctionCount;
extern int CDE_nativeFunctionCallCount[];
extern char* CDE_nativeFunctionNames[];
#define CDE_NATIVE_ENTER(env, that, func) CDE_nativeFunctionCallCount[func]++;
#define CDE_NATIVE_EXIT(env, that, func) 
#else
#define CDE_NATIVE_ENTER(env, that, func) 
#define CDE_NATIVE_EXIT(env, that, func) 
#endif

typedef enum {
	DtActionInvoke_FUNC,
	DtAppInitialize_FUNC,
	DtDbLoad_FUNC,
	DtDtsDataTypeIsAction_FUNC,
	DtDtsDataTypeNames_FUNC,
	DtDtsDataTypeToAttributeValue_FUNC,
	DtDtsFileToDataType_FUNC,
	DtDtsFreeAttributeValue_FUNC,
	DtDtsFreeDataType_FUNC,
	DtDtsFreeDataTypeNames_FUNC,
} CDE_FUNCS;
