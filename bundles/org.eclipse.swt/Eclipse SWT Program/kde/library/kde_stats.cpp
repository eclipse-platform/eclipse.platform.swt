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
#include "kde_stats.h"

#ifdef NATIVE_STATS

int KDE_nativeFunctionCount = 41;
int KDE_nativeFunctionCallCount[41];
char * KDE_nativeFunctionNames[] = {
	"KApplication_1new",
	"KGlobal_1iconLoader",
	"KIconLoader_1iconPath",
	"KMimeTypeListIterator_1delete",
	"KMimeTypeListIterator_1dereference",
	"KMimeTypeListIterator_1equals",
	"KMimeTypeListIterator_1increment",
	"KMimeTypeList_1begin",
	"KMimeTypeList_1delete",
	"KMimeTypeList_1end",
	"KMimeType_1allMimeTypes",
	"KMimeType_1delete",
	"KMimeType_1icon",
	"KMimeType_1mimeType",
	"KMimeType_1name",
	"KMimeType_1offers",
	"KMimeType_1patterns",
	"KRun_1runURL",
	"KServiceList_1delete",
	"KURL_1delete",
	"KURL_1new",
	"QCString_1data",
	"QCString_1delete",
	"QCString_1new",
	"QStringListIterator_1delete",
	"QStringListIterator_1dereference",
	"QStringListIterator_1equals",
	"QStringListIterator_1increment",
	"QStringList_1begin",
	"QStringList_1delete",
	"QStringList_1end",
	"QString_1delete",
	"QString_1equals",
	"QString_1new",
	"QString_1utf8",
	"XFreePixmap",
	"XpmReadFileToPixmap",
	"free",
	"malloc",
	"sigaction",
	"sigaction_1sizeof",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(KDE_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return KDE_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(KDE_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return env->NewStringUTF(KDE_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(KDE_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return KDE_nativeFunctionCallCount[index];
}

#endif
