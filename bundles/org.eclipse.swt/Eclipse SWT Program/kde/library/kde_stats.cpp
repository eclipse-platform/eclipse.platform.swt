/*******************************************************************************
* Copyright (c) 2000, 2005 IBM Corporation and others.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
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
	"_1KApplication_1new",
	"_1KGlobal_1iconLoader",
	"_1KIconLoader_1iconPath",
	"_1KMimeTypeListIterator_1delete",
	"_1KMimeTypeListIterator_1dereference",
	"_1KMimeTypeListIterator_1equals",
	"_1KMimeTypeListIterator_1increment",
	"_1KMimeTypeList_1begin",
	"_1KMimeTypeList_1delete",
	"_1KMimeTypeList_1end",
	"_1KMimeType_1allMimeTypes",
	"_1KMimeType_1delete",
	"_1KMimeType_1icon",
	"_1KMimeType_1mimeType",
	"_1KMimeType_1name",
	"_1KMimeType_1offers",
	"_1KMimeType_1patterns",
	"_1KRun_1runURL",
	"_1KServiceList_1delete",
	"_1KURL_1delete",
	"_1KURL_1new",
	"_1QCString_1data",
	"_1QCString_1delete",
	"_1QCString_1new",
	"_1QStringListIterator_1delete",
	"_1QStringListIterator_1dereference",
	"_1QStringListIterator_1equals",
	"_1QStringListIterator_1increment",
	"_1QStringList_1begin",
	"_1QStringList_1delete",
	"_1QStringList_1end",
	"_1QString_1delete",
	"_1QString_1equals",
	"_1QString_1new",
	"_1QString_1utf8",
	"_1XFreePixmap",
	"_1XpmReadFileToPixmap",
	"_1free",
	"_1malloc",
	"_1sigaction",
	"_1sigaction_1sizeof",
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
