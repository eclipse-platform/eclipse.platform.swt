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
#include "com_stats.h"

#ifdef NATIVE_STATS

int COM_nativeFunctionCount = 99;
int COM_nativeFunctionCallCount[99];
char * COM_nativeFunctionNames[] = {
	"CLSIDFromProgID",
	"CLSIDFromString",
	"CoCreateInstance",
	"CoFreeUnusedLibraries",
	"CoGetClassObject",
	"CoLockObjectExternal",
	"CoTaskMemAlloc",
	"CoTaskMemFree",
	"CreateStdAccessibleObject",
	"DoDragDrop",
	"GetClassFile",
	"IIDFromString",
	"IsEqualGUID",
	"LresultFromObject",
	"MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I",
	"MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I",
	"MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I",
	"MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I",
	"MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I",
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II",
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II",
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2II",
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II",
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II",
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II",
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II",
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2II",
	"MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II",
	"OleCreate",
	"OleCreateFromFile",
	"OleCreatePropertyFrame",
	"OleDraw",
	"OleFlushClipboard",
	"OleGetClipboard",
	"OleIsCurrentClipboard",
	"OleIsRunning",
	"OleLoad",
	"OleRun",
	"OleSave",
	"OleSetClipboard",
	"OleSetContainedObject",
	"OleSetMenuDescriptor",
	"OleTranslateColor",
	"ProgIDFromCLSID",
	"RegisterDragDrop",
	"ReleaseStgMedium",
	"RevokeDragDrop",
	"StgCreateDocfile",
	"StgIsStorageFile",
	"StgOpenStorage",
	"StringFromCLSID",
	"SysAllocString",
	"SysFreeString",
	"SysStringByteLen",
	"VariantChangeType",
	"VariantClear",
	"VariantInit",
	"VtblCall__IIII",
	"VtblCall__IIIII",
	"VtblCall__IIIIII",
	"VtblCall__IIIIIII",
	"VtblCall__IIIIIIII",
	"VtblCall__IIIIIIIIII",
	"VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2",
	"VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I",
	"VtblCall__IIII_3I",
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I",
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2",
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II",
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I",
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I",
	"VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2",
	"VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2",
	"VtblCall__IIIZ",
	"VtblCall__III_3I",
	"VtblCall__III_3II_3I",
	"VtblCall__III_3I_3I_3I_3I",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2",
	"VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2",
	"VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2",
	"VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ",
	"VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2",
	"VtblCall__II_3C",
	"VtblCall__II_3CI",
	"VtblCall__II_3CIIII_3I",
	"VtblCall__II_3CIII_3I",
	"VtblCall__II_3C_3C",
	"VtblCall__II_3I",
	"WriteClassStg",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(COM_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return COM_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(COM_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, COM_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(COM_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return COM_nativeFunctionCallCount[index];
}

#endif
