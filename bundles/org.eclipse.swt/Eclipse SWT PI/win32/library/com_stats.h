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
int COM_nativeFunctionCallCount[];
char* COM_nativeFunctionNames[];
#define COM_NATIVE_ENTER(env, that, func) COM_nativeFunctionCallCount[func]++;
#define COM_NATIVE_EXIT(env, that, func) 
#else
#define COM_NATIVE_ENTER(env, that, func) 
#define COM_NATIVE_EXIT(env, that, func) 
#endif

#define CLSIDFromProgID_FUNC 0
#define CLSIDFromString_FUNC 1
#define CoCreateInstance_FUNC 2
#define CoFreeUnusedLibraries_FUNC 3
#define CoGetClassObject_FUNC 4
#define CoLockObjectExternal_FUNC 5
#define CoTaskMemAlloc_FUNC 6
#define CoTaskMemFree_FUNC 7
#define CreateStdAccessibleObject_FUNC 8
#define DoDragDrop_FUNC 9
#define GetClassFile_FUNC 10
#define IIDFromString___3CLorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC 11
#define IsEqualGUID_FUNC 12
#define LresultFromObject_FUNC 13
#define MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I_FUNC 14
#define MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I_FUNC 15
#define MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I_FUNC 16
#define MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I_FUNC 17
#define MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I_FUNC 18
#define MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II_FUNC 19
#define MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II_FUNC 20
#define MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2II_FUNC 21
#define MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC 22
#define MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II_FUNC 23
#define MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II_FUNC 24
#define MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II_FUNC 25
#define MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2II_FUNC 26
#define MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II_FUNC 27
#define OleCreate_FUNC 28
#define OleCreateFromFile_FUNC 29
#define OleCreatePropertyFrame_FUNC 30
#define OleDraw_FUNC 31
#define OleFlushClipboard_FUNC 32
#define OleGetClipboard_FUNC 33
#define OleIsCurrentClipboard_FUNC 34
#define OleIsRunning_FUNC 35
#define OleLoad_FUNC 36
#define OleRun_FUNC 37
#define OleSave_FUNC 38
#define OleSetClipboard_FUNC 39
#define OleSetContainedObject_FUNC 40
#define OleSetMenuDescriptor_FUNC 41
#define OleTranslateColor_FUNC 42
#define ProgIDFromCLSID_FUNC 43
#define RegisterDragDrop_FUNC 44
#define ReleaseStgMedium_FUNC 45
#define RevokeDragDrop_FUNC 46
#define StgCreateDocfile_FUNC 47
#define StgIsStorageFile_FUNC 48
#define StgOpenStorage_FUNC 49
#define StringFromCLSID_FUNC 50
#define SysAllocString_FUNC 51
#define SysFreeString_FUNC 52
#define SysStringByteLen_FUNC 53
#define VariantChangeType_FUNC 54
#define VariantClear_FUNC 55
#define VariantInit_FUNC 56
#define VtblCall__IIII_FUNC 57
#define VtblCall__IIIII_FUNC 58
#define VtblCall__IIIIII_FUNC 59
#define VtblCall__IIIIIII_FUNC 60
#define VtblCall__IIIIIIII_FUNC 61
#define VtblCall__IIIIIIIIII_FUNC 62
#define VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2_FUNC 63
#define VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I_FUNC 64
#define VtblCall__IIII_3I_FUNC 65
#define VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I_FUNC 66
#define VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC 67
#define VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC 68
#define VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC 69
#define VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I_FUNC 70
#define VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC 71
#define VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC 72
#define VtblCall__IIIZ_FUNC 73
#define VtblCall__III_3I_FUNC 74
#define VtblCall__III_3II_3I_FUNC 75
#define VtblCall__III_3I_3I_3I_3I_FUNC 76
#define VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2_FUNC 77
#define VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2_FUNC 78
#define VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_FUNC 79
#define VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2_FUNC 80
#define VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z_FUNC 81
#define VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC 82
#define VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII_FUNC 83
#define VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I_FUNC 84
#define VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2_FUNC 85
#define VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC 86
#define VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2_FUNC 87
#define VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2_FUNC 88
#define VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2_FUNC 89
#define VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ_FUNC 90
#define VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2_FUNC 91
#define VtblCall__II_3C_FUNC 92
#define VtblCall__II_3CI_FUNC 93
#define VtblCall__II_3CIIII_3I_FUNC 94
#define VtblCall__II_3CIII_3I_FUNC 95
#define VtblCall__II_3C_3C_FUNC 96
#define VtblCall__II_3I_FUNC 97
#define WriteClassStg_FUNC 98
