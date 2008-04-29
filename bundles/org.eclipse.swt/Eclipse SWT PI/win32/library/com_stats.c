/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "com_stats.h"

#ifdef NATIVE_STATS

int COM_nativeFunctionCount = 157;
int COM_nativeFunctionCallCount[157];
char * COM_nativeFunctionNames[] = {
	"AccessibleObjectFromWindow",
	"CAUUID_1sizeof",
	"CLSIDFromProgID",
	"CLSIDFromString",
	"CONTROLINFO_1sizeof",
	"COSERVERINFO_1sizeof",
	"CoCreateInstance",
	"CoFreeUnusedLibraries",
	"CoGetClassObject",
	"CoLockObjectExternal",
	"CoTaskMemAlloc",
	"CoTaskMemFree",
	"CreateStdAccessibleObject",
	"DISPPARAMS_1sizeof",
	"DVTARGETDEVICE_1sizeof",
	"DoDragDrop",
	"ELEMDESC_1sizeof",
	"EXCEPINFO_1sizeof",
	"FORMATETC_1sizeof",
	"FUNCDESC_1sizeof",
	"GUID_1sizeof",
	"GetClassFile",
	"IIDFromString",
	"IsEqualGUID",
	"LICINFO_1sizeof",
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
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2II",
	"MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II",
	"OLECMD_1sizeof",
	"OLEINPLACEFRAMEINFO_1sizeof",
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
	"SHDoDragDrop",
	"STATSTG_1sizeof",
	"STGMEDIUM_1sizeof",
	"StgCreateDocfile",
	"StgIsStorageFile",
	"StgOpenStorage",
	"StringFromCLSID",
	"SysAllocString",
	"SysFreeString",
	"SysStringByteLen",
	"TYPEATTR_1sizeof",
	"TYPEDESC_1sizeof",
	"VARDESC_1sizeof",
	"VARIANT_1sizeof",
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
	"VtblCall__IIIILorg_eclipse_swt_internal_win32_POINT_2I",
	"VtblCall__IIII_3I",
	"VtblCall__IIII_3J",
	"VtblCall__IIIJ_3I",
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I",
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2",
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II",
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I",
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I",
	"VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2",
	"VtblCall__IIILorg_eclipse_swt_internal_win32_POINT_2I",
	"VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2",
	"VtblCall__IIIZ",
	"VtblCall__III_3I",
	"VtblCall__III_3II_3I",
	"VtblCall__III_3I_3I_3I_3I",
	"VtblCall__III_3J",
	"VtblCall__IIJI_3I",
	"VtblCall__IIJ_3I",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I",
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2",
	"VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2",
	"VtblCall__IILorg_eclipse_swt_internal_win32_POINT_2I",
	"VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2",
	"VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ",
	"VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2",
	"VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I",
	"VtblCall__IIZ",
	"VtblCall__II_3C",
	"VtblCall__II_3CI",
	"VtblCall__II_3CIIII_3I",
	"VtblCall__II_3CIII_3I",
	"VtblCall__II_3CIII_3J",
	"VtblCall__II_3CJII_3J",
	"VtblCall__II_3C_3C",
	"VtblCall__IJIIIIJ",
	"VtblCall__IJJIIIII",
	"VtblCall_1IVARIANT",
	"VtblCall_1IVARIANTP",
	"VtblCall_1PPPPVARIANT",
	"VtblCall_1PVARIANTP",
	"VtblCall_1VARIANT",
	"VtblCall_1VARIANTP",
	"WriteClassStg",
	"accDoDefaultAction_1CALLBACK",
	"accLocation_1CALLBACK",
	"accNavigate_1CALLBACK",
	"accSelect_1CALLBACK",
	"get_1accChild_1CALLBACK",
	"get_1accDefaultAction_1CALLBACK",
	"get_1accDescription_1CALLBACK",
	"get_1accHelpTopic_1CALLBACK",
	"get_1accHelp_1CALLBACK",
	"get_1accKeyboardShortcut_1CALLBACK",
	"get_1accName_1CALLBACK",
	"get_1accRole_1CALLBACK",
	"get_1accState_1CALLBACK",
	"get_1accValue_1CALLBACK",
	"put_1accName_1CALLBACK",
	"put_1accValue_1CALLBACK",
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
