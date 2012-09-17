/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others. All rights reserved.
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

#include "swt.h"
#include "gtk_stats.h"

#ifdef NATIVE_STATS

char * GTK_nativeFunctionNames[] = {
	"_1GTK_1WIDGET_1HEIGHT",
	"_1GTK_1WIDGET_1WIDTH",
	"_1g_1signal_1connect",
	"_1gtk_1events_1pending",
	"_1gtk_1init_1check",
	"_1gtk_1main",
	"_1gtk_1main_1iteration",
	"_1gtk_1plug_1new",
	"_1gtk_1widget_1destroy",
	"_1gtk_1widget_1show",
	"_1gtk_1widget_1show_1now",
	"_1gtk_1window_1new",
};
#define NATIVE_FUNCTION_COUNT sizeof(GTK_nativeFunctionNames) / sizeof(char*)
int GTK_nativeFunctionCount = NATIVE_FUNCTION_COUNT;
int GTK_nativeFunctionCallCount[NATIVE_FUNCTION_COUNT];

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(GTK_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return GTK_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(GTK_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, GTK_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(GTK_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return GTK_nativeFunctionCallCount[index];
}

#endif
