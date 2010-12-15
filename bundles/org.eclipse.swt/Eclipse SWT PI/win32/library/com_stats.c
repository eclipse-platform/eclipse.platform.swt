/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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

int COM_nativeFunctionCount = 184;
int COM_nativeFunctionCallCount[184];
char * COM_nativeFunctionNames[] = {
	"AccessibleChildren",
	"AccessibleObjectFromWindow",
	"CALLBACK_1setCurrentValue",
	"CAUUID_1sizeof",
	"CLSIDFromProgID",
	"CLSIDFromString",
	"CONTROLINFO_1sizeof",
	"COSERVERINFO_1sizeof",
	"CoCreateInstance",
	"CoFreeUnusedLibraries",
	"CoGetClassObject",
	"CoLockObjectExternal",
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
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_ole_win32_FORMATETC_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_ole_win32_GUID_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_ole_win32_STATSTG_2I",
#endif
#ifndef JNI64
	"MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I",
#else
	"MoveMemory__JLorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2JI",
#endif
#ifndef JNI64
	"MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II",
#else
	"MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2JI",
#endif
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
	"SysStringLen",
	"TYPEATTR_1sizeof",
	"TYPEDESC_1sizeof",
	"VARDESC_1sizeof",
	"VARIANT_1sizeof",
	"VariantChangeType",
	"VariantClear",
	"VariantInit",
#ifndef JNI64
	"VtblCall__IIIIII",
#else
	"VtblCall__IJIIII",
#endif
#ifndef JNI64
	"VtblCall__IIIIIII",
#else
	"VtblCall__IJIIIII",
#endif
#ifndef JNI64
	"VtblCall__IIIIIIII",
#else
	"VtblCall__IJIIIIIJ",
#endif
#ifndef JNI64
	"VtblCall__IIIIIIIIII",
#else
	"VtblCall__IJJJJJIIII",
#endif
#ifndef JNI64
	"VtblCall__IIIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I",
#else
	"VtblCall__IJIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I",
#endif
#ifndef JNI64
	"VtblCall__IIIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2J",
#else
	"VtblCall__IJIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2J",
#endif
#ifndef JNI64
	"VtblCall__IIIIJ",
#else
	"VtblCall__IJIIJ",
#endif
#ifndef JNI64
	"VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2",
#else
	"VtblCall__IJIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2",
#endif
#ifndef JNI64
	"VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I",
#else
	"VtblCall__IJIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I",
#endif
#ifndef JNI64
	"VtblCall__IIIILorg_eclipse_swt_internal_win32_POINT_2I",
#else
	"VtblCall__IJIILorg_eclipse_swt_internal_win32_POINT_2I",
#endif
#ifndef JNI64
	"VtblCall__IIIIZ",
#else
	"VtblCall__IJJJZ",
#endif
#ifndef JNI64
	"VtblCall__IIII_3I",
#else
	"VtblCall__IJII_3I",
#endif
#ifndef JNI64
	"VtblCall__IIII_3J",
#else
	"VtblCall__IJII_3J",
#endif
#ifndef JNI64
	"VtblCall__IIIJ",
#else
	"VtblCall__IJIJ",
#endif
#ifndef JNI64
	"VtblCall__IIIJ_3I",
#else
	"VtblCall__IJIJ_3I",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2II",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2JJ",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2JJ",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2JIJLorg_eclipse_swt_internal_win32_RECT_2",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_win32_MSG_2JIJLorg_eclipse_swt_internal_win32_RECT_2",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_win32_POINT_2I",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_win32_POINT_2I",
#endif
#ifndef JNI64
	"VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2",
#else
	"VtblCall__IJILorg_eclipse_swt_internal_win32_SIZE_2",
#endif
#ifndef JNI64
	"VtblCall__IIIZ",
#else
	"VtblCall__IJIZ",
#endif
#ifndef JNI64
	"VtblCall__III_3II_3I",
#else
	"VtblCall__IJI_3II_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3I_3I_3I_3I",
#else
	"VtblCall__IJI_3I_3I_3I_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3JI_3I",
#else
	"VtblCall__IJI_3JI_3I",
#endif
#ifndef JNI64
	"VtblCall__III_3J_3J_3I_3J",
#else
	"VtblCall__IJI_3J_3J_3I_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJI_3I",
#else
	"VtblCall__IJJI_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJI_3J",
#else
	"VtblCall__IJJI_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJJ",
#else
	"VtblCall__IJJJ",
#endif
#ifndef JNI64
	"VtblCall__IIJJLorg_eclipse_swt_internal_ole_win32_GUID_2J_3J",
#else
	"VtblCall__IJJJLorg_eclipse_swt_internal_ole_win32_GUID_2J_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJJLorg_eclipse_swt_internal_win32_POINT_2I",
#else
	"VtblCall__IJJJLorg_eclipse_swt_internal_win32_POINT_2I",
#endif
#ifndef JNI64
	"VtblCall__IIJJ_3J",
#else
	"VtblCall__IJJJ_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3J",
#else
	"VtblCall__IJJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJLorg_eclipse_swt_internal_ole_win32_STATSTG_2_3J",
#else
	"VtblCall__IJJLorg_eclipse_swt_internal_ole_win32_STATSTG_2_3J",
#endif
#ifndef JNI64
	"VtblCall__IIJLorg_eclipse_swt_internal_win32_POINT_2J",
#else
	"VtblCall__IJJLorg_eclipse_swt_internal_win32_POINT_2J",
#endif
#ifndef JNI64
	"VtblCall__IIJZ",
#else
	"VtblCall__IJJZ",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3I",
#else
	"VtblCall__IJJ_3I",
#endif
#ifndef JNI64
	"VtblCall__IIJ_3J",
#else
	"VtblCall__IJJ_3J",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_CAUUID_2",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2IIII",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2III_3I",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIJJ",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2IIJJ",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2JII_3I",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2JII_3I",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3J",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3J",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_3I",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3J",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_3J",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_ole_win32_LICINFO_2",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_MSG_2",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_POINT_2I",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_POINT_2I",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2II",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2II",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2IZ",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2JJ",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2JJ",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2JZ",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2JZ",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I",
#endif
#ifndef JNI64
	"VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2J",
#else
	"VtblCall__IJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2J",
#endif
#ifndef JNI64
	"VtblCall__IIZ",
#else
	"VtblCall__IJZ",
#endif
#ifndef JNI64
	"VtblCall__IIZI",
#else
	"VtblCall__IJZJ",
#endif
#ifndef JNI64
	"VtblCall__II_3CIIII_3I",
#else
	"VtblCall__IJ_3CIIII_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3CIII_3I",
#else
	"VtblCall__IJ_3CIII_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3CIII_3J",
#else
	"VtblCall__IJ_3CIII_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3CI_3I",
#else
	"VtblCall__IJ_3CI_3I",
#endif
#ifndef JNI64
	"VtblCall__II_3CJIII_3J",
#else
	"VtblCall__IJ_3CJIII_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3CJII_3J",
#else
	"VtblCall__IJ_3CJII_3J",
#endif
#ifndef JNI64
	"VtblCall__II_3C_3C",
#else
	"VtblCall__IJ_3C_3C",
#endif
#ifndef JNI64
	"VtblCall__II_3C_3I_3I",
#else
	"VtblCall__IJ_3C_3I_3I",
#endif
	"VtblCall__IJIIIIJ",
#ifndef JNI64
	"VtblCall__IJJIIIII",
#else
	"VtblCall__IJJIIIIJ",
#endif
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
