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

#include "swt.h"
#include "cde_stats.h"

#ifdef NATIVE_STATS

int CDE_nativeFunctionCount = 10;
int CDE_nativeFunctionCallCount[10];
char * CDE_nativeFunctionNames[] = {
	"DtActionInvoke", 
	"DtAppInitialize", 
	"DtDbLoad", 
	"DtDtsDataTypeIsAction", 
	"DtDtsDataTypeNames", 
	"DtDtsDataTypeToAttributeValue", 
	"DtDtsFileToDataType", 
	"DtDtsFreeAttributeValue", 
	"DtDtsFreeDataType", 
	"DtDtsFreeDataTypeNames", 
};

#endif
