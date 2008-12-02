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
extern int COM_nativeFunctionCount;
extern int COM_nativeFunctionCallCount[];
extern char* COM_nativeFunctionNames[];
#define COM_NATIVE_ENTER(env, that, func) COM_nativeFunctionCallCount[func]++;
#define COM_NATIVE_EXIT(env, that, func) 
#else
#ifndef COM_NATIVE_ENTER
#define COM_NATIVE_ENTER(env, that, func) 
#endif
#ifndef COM_NATIVE_EXIT
#define COM_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	AccessibleObjectFromWindow_FUNC,
	CAUUID_1sizeof_FUNC,
	CLSIDFromProgID_FUNC,
	CLSIDFromString_FUNC,
	CONTROLINFO_1sizeof_FUNC,
	COSERVERINFO_1sizeof_FUNC,
	CoCreateInstance_FUNC,
	CoFreeUnusedLibraries_FUNC,
	CoGetClassObject_FUNC,
	CoLockObjectExternal_FUNC,
	CoTaskMemAlloc_FUNC,
	CoTaskMemFree_FUNC,
	CreateStdAccessibleObject_FUNC,
	DISPPARAMS_1sizeof_FUNC,
	DVTARGETDEVICE_1sizeof_FUNC,
	DoDragDrop_FUNC,
	ELEMDESC_1sizeof_FUNC,
	EXCEPINFO_1sizeof_FUNC,
	FORMATETC_1sizeof_FUNC,
	FUNCDESC_1sizeof_FUNC,
	GUID_1sizeof_FUNC,
	GetClassFile_FUNC,
	IIDFromString_FUNC,
	IsEqualGUID_FUNC,
	LICINFO_1sizeof_FUNC,
	LresultFromObject_FUNC,
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_ole_win32_FORMATETC_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_ole_win32_GUID_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_ole_win32_STATSTG_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I_FUNC,
#else
	MoveMemory__JLorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2JI_FUNC,
#endif
#ifndef JNI64
	MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II_FUNC,
#else
	MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2JI_FUNC,
#endif
	OLECMD_1sizeof_FUNC,
	OLEINPLACEFRAMEINFO_1sizeof_FUNC,
	OleCreate_FUNC,
	OleCreateFromFile_FUNC,
	OleCreatePropertyFrame_FUNC,
	OleDraw_FUNC,
	OleFlushClipboard_FUNC,
	OleGetClipboard_FUNC,
	OleIsCurrentClipboard_FUNC,
	OleIsRunning_FUNC,
	OleLoad_FUNC,
	OleRun_FUNC,
	OleSave_FUNC,
	OleSetClipboard_FUNC,
	OleSetContainedObject_FUNC,
	OleSetMenuDescriptor_FUNC,
	OleTranslateColor_FUNC,
	ProgIDFromCLSID_FUNC,
	RegisterDragDrop_FUNC,
	ReleaseStgMedium_FUNC,
	RevokeDragDrop_FUNC,
	SHDoDragDrop_FUNC,
	STATSTG_1sizeof_FUNC,
	STGMEDIUM_1sizeof_FUNC,
	StgCreateDocfile_FUNC,
	StgIsStorageFile_FUNC,
	StgOpenStorage_FUNC,
	StringFromCLSID_FUNC,
	SysAllocString_FUNC,
	SysFreeString_FUNC,
	SysStringByteLen_FUNC,
	TYPEATTR_1sizeof_FUNC,
	TYPEDESC_1sizeof_FUNC,
	VARDESC_1sizeof_FUNC,
	VARIANT_1sizeof_FUNC,
	VariantChangeType_FUNC,
	VariantClear_FUNC,
	VariantInit_FUNC,
#ifndef JNI64
	VtblCall__IIII_FUNC,
#else
	VtblCall__IJII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIII_FUNC,
#else
	VtblCall__IJIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIII_FUNC,
#else
	VtblCall__IJIIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIIII_FUNC,
#else
	VtblCall__IJIIIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIIIII_FUNC,
#else
	VtblCall__IJIIIIIJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIIIIIII_FUNC,
#else
	VtblCall__IJJJJJIIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I_FUNC,
#else
	VtblCall__IJIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2J_FUNC,
#else
	VtblCall__IJIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIIJ_FUNC,
#else
	VtblCall__IJIIJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2_FUNC,
#else
	VtblCall__IJIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I_FUNC,
#else
	VtblCall__IJIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIILorg_eclipse_swt_internal_win32_POINT_2I_FUNC,
#else
	VtblCall__IJIILorg_eclipse_swt_internal_win32_POINT_2I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIII_3I_FUNC,
#else
	VtblCall__IJII_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIII_3J_FUNC,
#else
	VtblCall__IJII_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIJ_FUNC,
#else
	VtblCall__IJIJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIJ_3I_FUNC,
#else
	VtblCall__IJIJ_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2JJ_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2JJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2JIJLorg_eclipse_swt_internal_win32_RECT_2_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_win32_MSG_2JIJLorg_eclipse_swt_internal_win32_RECT_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_win32_POINT_2I_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_win32_POINT_2I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC,
#else
	VtblCall__IJILorg_eclipse_swt_internal_win32_SIZE_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIIZ_FUNC,
#else
	VtblCall__IJIZ_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3I_FUNC,
#else
	VtblCall__IJI_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3II_3I_FUNC,
#else
	VtblCall__IJI_3II_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3I_3I_3I_3I_FUNC,
#else
	VtblCall__IJI_3I_3I_3I_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3J_FUNC,
#else
	VtblCall__IJI_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3JI_3I_FUNC,
#else
	VtblCall__IJI_3JI_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__III_3J_3J_3I_3J_FUNC,
#else
	VtblCall__IJI_3J_3J_3I_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJI_FUNC,
#else
	VtblCall__IJJI_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJI_3I_FUNC,
#else
	VtblCall__IJJI_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJI_3J_FUNC,
#else
	VtblCall__IJJI_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJ_FUNC,
#else
	VtblCall__IJJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJLorg_eclipse_swt_internal_ole_win32_GUID_2J_3J_FUNC,
#else
	VtblCall__IJJJLorg_eclipse_swt_internal_ole_win32_GUID_2J_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJJLorg_eclipse_swt_internal_win32_POINT_2I_FUNC,
#else
	VtblCall__IJJJLorg_eclipse_swt_internal_win32_POINT_2I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3J_FUNC,
#else
	VtblCall__IJJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJLorg_eclipse_swt_internal_ole_win32_STATSTG_2_3J_FUNC,
#else
	VtblCall__IJJLorg_eclipse_swt_internal_ole_win32_STATSTG_2_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJLorg_eclipse_swt_internal_win32_POINT_2J_FUNC,
#else
	VtblCall__IJJLorg_eclipse_swt_internal_win32_POINT_2J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJZ_FUNC,
#else
	VtblCall__IJJZ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3I_FUNC,
#else
	VtblCall__IJJ_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIJ_3J_FUNC,
#else
	VtblCall__IJJ_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_CAUUID_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2IIII_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2III_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIJJ_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2IIJJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2JII_3I_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2JII_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3J_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3J_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_ole_win32_LICINFO_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_win32_MSG_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_win32_POINT_2I_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_win32_POINT_2I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2IZ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2JZ_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2JZ_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I_FUNC,
#endif
#ifndef JNI64
	VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2J_FUNC,
#else
	VtblCall__IJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2J_FUNC,
#endif
#ifndef JNI64
	VtblCall__IIZ_FUNC,
#else
	VtblCall__IJZ_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3C_FUNC,
#else
	VtblCall__IJ_3C_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CI_FUNC,
#else
	VtblCall__IJ_3CI_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CIIII_3I_FUNC,
#else
	VtblCall__IJ_3CIIII_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CIII_3I_FUNC,
#else
	VtblCall__IJ_3CIII_3I_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CIII_3J_FUNC,
#else
	VtblCall__IJ_3CIII_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CJ_FUNC,
#else
	VtblCall__IJ_3CJ_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CJIII_3J_FUNC,
#else
	VtblCall__IJ_3CJIII_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3CJII_3J_FUNC,
#else
	VtblCall__IJ_3CJII_3J_FUNC,
#endif
#ifndef JNI64
	VtblCall__II_3C_3C_FUNC,
#else
	VtblCall__IJ_3C_3C_FUNC,
#endif
	VtblCall__IJIIIIJ_FUNC,
#ifndef JNI64
	VtblCall__IJJIIIII_FUNC,
#else
	VtblCall__IJJIIIIJ_FUNC,
#endif
	VtblCall_1IVARIANT_FUNC,
	VtblCall_1IVARIANTP_FUNC,
	VtblCall_1PPPPVARIANT_FUNC,
	VtblCall_1PVARIANTP_FUNC,
	VtblCall_1VARIANT_FUNC,
	VtblCall_1VARIANTP_FUNC,
	WriteClassStg_FUNC,
	accDoDefaultAction_1CALLBACK_FUNC,
	accLocation_1CALLBACK_FUNC,
	accNavigate_1CALLBACK_FUNC,
	accSelect_1CALLBACK_FUNC,
	get_1accChild_1CALLBACK_FUNC,
	get_1accDefaultAction_1CALLBACK_FUNC,
	get_1accDescription_1CALLBACK_FUNC,
	get_1accHelpTopic_1CALLBACK_FUNC,
	get_1accHelp_1CALLBACK_FUNC,
	get_1accKeyboardShortcut_1CALLBACK_FUNC,
	get_1accName_1CALLBACK_FUNC,
	get_1accRole_1CALLBACK_FUNC,
	get_1accState_1CALLBACK_FUNC,
	get_1accValue_1CALLBACK_FUNC,
	put_1accName_1CALLBACK_FUNC,
	put_1accValue_1CALLBACK_FUNC,
} COM_FUNCS;
