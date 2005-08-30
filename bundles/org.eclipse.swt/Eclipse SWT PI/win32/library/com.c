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
#include "com_structs.h"
#include "com_stats.h"

#define COM_NATIVE(func) Java_org_eclipse_swt_internal_ole_win32_COM_##func

#ifndef NO_CLSIDFromProgID
JNIEXPORT jint JNICALL COM_NATIVE(CLSIDFromProgID)
	(JNIEnv *env, jclass that, jcharArray arg0, jobject arg1)
{
	jchar *lparg0=NULL;
	GUID _arg1, *lparg1=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CLSIDFromProgID_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getGUIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)CLSIDFromProgID((LPCOLESTR)lparg0, lparg1);
fail:
	if (arg1 && lparg1) setGUIDFields(env, arg1, lparg1);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	COM_NATIVE_EXIT(env, that, CLSIDFromProgID_FUNC);
	return rc;
}
#endif

#ifndef NO_CLSIDFromString
JNIEXPORT jint JNICALL COM_NATIVE(CLSIDFromString)
	(JNIEnv *env, jclass that, jcharArray arg0, jobject arg1)
{
	jchar *lparg0=NULL;
	GUID _arg1, *lparg1=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CLSIDFromString_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getGUIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)CLSIDFromString((LPOLESTR)lparg0, lparg1);
fail:
	if (arg1 && lparg1) setGUIDFields(env, arg1, lparg1);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	COM_NATIVE_EXIT(env, that, CLSIDFromString_FUNC);
	return rc;
}
#endif

#ifndef NO_CoCreateInstance
JNIEXPORT jint JNICALL COM_NATIVE(CoCreateInstance)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jintArray arg4)
{
	GUID _arg0, *lparg0=NULL;
	GUID _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CoCreateInstance_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)CoCreateInstance(lparg0, (LPUNKNOWN)arg1, arg2, lparg3, (LPVOID *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, CoCreateInstance_FUNC);
	return rc;
}
#endif

#ifndef NO_CoFreeUnusedLibraries
JNIEXPORT void JNICALL COM_NATIVE(CoFreeUnusedLibraries)
	(JNIEnv *env, jclass that)
{
	COM_NATIVE_ENTER(env, that, CoFreeUnusedLibraries_FUNC);
	CoFreeUnusedLibraries();
	COM_NATIVE_EXIT(env, that, CoFreeUnusedLibraries_FUNC);
}
#endif

#ifndef NO_CoGetClassObject
JNIEXPORT jint JNICALL COM_NATIVE(CoGetClassObject)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2, jobject arg3, jintArray arg4)
{
	GUID _arg0, *lparg0=NULL;
	GUID _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CoGetClassObject_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)CoGetClassObject(lparg0, arg1, (COSERVERINFO *)arg2, lparg3, (LPVOID *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, CoGetClassObject_FUNC);
	return rc;
}
#endif

#ifndef NO_CoLockObjectExternal
JNIEXPORT jint JNICALL COM_NATIVE(CoLockObjectExternal)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1, jboolean arg2)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CoLockObjectExternal_FUNC);
	rc = (jint)CoLockObjectExternal((IUnknown *)arg0, (BOOL)arg1, (BOOL)arg2);
	COM_NATIVE_EXIT(env, that, CoLockObjectExternal_FUNC);
	return rc;
}
#endif

#ifndef NO_CoTaskMemAlloc
JNIEXPORT jint JNICALL COM_NATIVE(CoTaskMemAlloc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CoTaskMemAlloc_FUNC);
	rc = (jint)CoTaskMemAlloc((ULONG)arg0);
	COM_NATIVE_EXIT(env, that, CoTaskMemAlloc_FUNC);
	return rc;
}
#endif

#ifndef NO_CoTaskMemFree
JNIEXPORT void JNICALL COM_NATIVE(CoTaskMemFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	COM_NATIVE_ENTER(env, that, CoTaskMemFree_FUNC);
	CoTaskMemFree((LPVOID)arg0);
	COM_NATIVE_EXIT(env, that, CoTaskMemFree_FUNC);
}
#endif

#ifndef NO_CreateStdAccessibleObject
JNIEXPORT jint JNICALL COM_NATIVE(CreateStdAccessibleObject)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jintArray arg3)
{
	GUID _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CreateStdAccessibleObject_FUNC);
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
/*
	rc = (jint)CreateStdAccessibleObject((HWND)arg0, arg1, lparg2, (LPVOID *)lparg3);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(CreateStdAccessibleObject_LIB);
			if (hm) fp = GetProcAddress(hm, "CreateStdAccessibleObject");
			initialized = 1;
		}
		if (fp) {
			rc = (jint)fp((HWND)arg0, arg1, lparg2, (LPVOID *)lparg3);
		}
	}
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, CreateStdAccessibleObject_FUNC);
	return rc;
}
#endif

#ifndef NO_DoDragDrop
JNIEXPORT jint JNICALL COM_NATIVE(DoDragDrop)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, DoDragDrop_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)DoDragDrop((IDataObject *)arg0, (IDropSource *)arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	COM_NATIVE_EXIT(env, that, DoDragDrop_FUNC);
	return rc;
}
#endif

#ifndef NO_GetClassFile
JNIEXPORT jint JNICALL COM_NATIVE(GetClassFile)
	(JNIEnv *env, jclass that, jcharArray arg0, jobject arg1)
{
	jchar *lparg0=NULL;
	GUID _arg1, *lparg1=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, GetClassFile_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getGUIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)GetClassFile((LPCWSTR)lparg0, lparg1);
fail:
	if (arg1 && lparg1) setGUIDFields(env, arg1, lparg1);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	COM_NATIVE_EXIT(env, that, GetClassFile_FUNC);
	return rc;
}
#endif

#ifndef NO_IIDFromString
JNIEXPORT jint JNICALL COM_NATIVE(IIDFromString)
	(JNIEnv *env, jclass that, jcharArray arg0, jobject arg1)
{
	jchar *lparg0=NULL;
	GUID _arg1, *lparg1=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, IIDFromString_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getGUIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)IIDFromString((LPOLESTR)lparg0, lparg1);
fail:
	if (arg1 && lparg1) setGUIDFields(env, arg1, lparg1);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	COM_NATIVE_EXIT(env, that, IIDFromString_FUNC);
	return rc;
}
#endif

#ifndef NO_IsEqualGUID
JNIEXPORT jboolean JNICALL COM_NATIVE(IsEqualGUID)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1)
{
	GUID _arg0, *lparg0=NULL;
	GUID _arg1, *lparg1=NULL;
	jboolean rc = 0;
	COM_NATIVE_ENTER(env, that, IsEqualGUID_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getGUIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)IsEqualGUID(lparg0, lparg1);
fail:
	if (arg1 && lparg1) setGUIDFields(env, arg1, lparg1);
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, IsEqualGUID_FUNC);
	return rc;
}
#endif

#ifndef NO_LresultFromObject
JNIEXPORT jint JNICALL COM_NATIVE(LresultFromObject)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GUID _arg0, *lparg0=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, LresultFromObject_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
/*
	rc = (jint)LresultFromObject(lparg0, arg1, (LPUNKNOWN)arg2);
*/
	{
		static int initialized = 0;
		static HMODULE hm = NULL;
		static FARPROC fp = NULL;
		rc = 0;
		if (!initialized) {
			if (!hm) hm = LoadLibrary(LresultFromObject_LIB);
			if (hm) fp = GetProcAddress(hm, "LresultFromObject");
			initialized = 1;
		}
		if (fp) {
			rc = (jint)fp(lparg0, arg1, (LPUNKNOWN)arg2);
		}
	}
fail:
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, LresultFromObject_FUNC);
	return rc;
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	FORMATETC _arg1, *lparg1=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I_FUNC);
	if (arg1) if ((lparg1 = getFORMATETCFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	COM_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	GUID _arg1, *lparg1=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I_FUNC);
	if (arg1) if ((lparg1 = getGUIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	COM_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	OLEINPLACEFRAMEINFO _arg1, *lparg1=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I_FUNC);
	if (arg1) if ((lparg1 = getOLEINPLACEFRAMEINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	COM_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	STATSTG _arg1, *lparg1=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I_FUNC);
	if (arg1) if ((lparg1 = getSTATSTGFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	COM_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	STGMEDIUM _arg1, *lparg1=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I_FUNC);
	if (arg1) if ((lparg1 = getSTGMEDIUMFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
	COM_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	DISPPARAMS _arg0, *lparg0=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setDISPPARAMSFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	FORMATETC _arg0, *lparg0=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setFORMATETCFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2II
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	FUNCDESC _arg0, *lparg0=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setFUNCDESCFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	GUID _arg0, *lparg0=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	STATSTG _arg0, *lparg0=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setSTATSTGFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	STGMEDIUM _arg0, *lparg0=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setSTGMEDIUMFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	TYPEATTR _arg0, *lparg0=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setTYPEATTRFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2II
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	VARDESC _arg0, *lparg0=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setVARDESCFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2II_FUNC);
}
#endif

#ifndef NO_MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	RECT _arg0, *lparg0=NULL;
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setRECTFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II_FUNC);
}
#endif

#ifndef NO_OleCreate
JNIEXPORT jint JNICALL COM_NATIVE(OleCreate)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jintArray arg6)
{
	GUID _arg0, *lparg0=NULL;
	GUID _arg1, *lparg1=NULL;
	FORMATETC _arg3, *lparg3=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleCreate_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getGUIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getFORMATETCFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)OleCreate(lparg0, lparg1, arg2, lparg3, (IOleClientSite *)arg4, (IStorage *)arg5, (void **)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg3 && lparg3) setFORMATETCFields(env, arg3, lparg3);
	if (arg1 && lparg1) setGUIDFields(env, arg1, lparg1);
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, OleCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_OleCreateFromFile
JNIEXPORT jint JNICALL COM_NATIVE(OleCreateFromFile)
	(JNIEnv *env, jclass that, jobject arg0, jcharArray arg1, jobject arg2, jint arg3, jobject arg4, jint arg5, jint arg6, jintArray arg7)
{
	GUID _arg0, *lparg0=NULL;
	jchar *lparg1=NULL;
	GUID _arg2, *lparg2=NULL;
	FORMATETC _arg4, *lparg4=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleCreateFromFile_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getFORMATETCFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)OleCreateFromFile(lparg0, (LPCOLESTR)lparg1, lparg2, arg3, lparg4, (LPOLECLIENTSITE)arg5, (LPSTORAGE)arg6, (LPVOID *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg4 && lparg4) setFORMATETCFields(env, arg4, lparg4);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, OleCreateFromFile_FUNC);
	return rc;
}
#endif

#ifndef NO_OleCreatePropertyFrame
JNIEXPORT jint JNICALL COM_NATIVE(OleCreatePropertyFrame)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jcharArray arg3, jint arg4, jintArray arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	jchar *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleCreatePropertyFrame_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)OleCreatePropertyFrame((HWND)arg0, arg1, arg2, (LPCOLESTR)lparg3, arg4, (LPUNKNOWN FAR*)lparg5, arg6, (LPCLSID)arg7, (LCID)arg8, arg9, (LPVOID)arg10);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	COM_NATIVE_EXIT(env, that, OleCreatePropertyFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_OleDraw
JNIEXPORT jint JNICALL COM_NATIVE(OleDraw)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleDraw_FUNC);
	rc = (jint)OleDraw((LPUNKNOWN)arg0, (DWORD)arg1, (HDC)arg2, (LPRECT)arg3);
	COM_NATIVE_EXIT(env, that, OleDraw_FUNC);
	return rc;
}
#endif

#ifndef NO_OleFlushClipboard
JNIEXPORT jint JNICALL COM_NATIVE(OleFlushClipboard)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleFlushClipboard_FUNC);
	rc = (jint)OleFlushClipboard();
	COM_NATIVE_EXIT(env, that, OleFlushClipboard_FUNC);
	return rc;
}
#endif

#ifndef NO_OleGetClipboard
JNIEXPORT jint JNICALL COM_NATIVE(OleGetClipboard)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleGetClipboard_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)OleGetClipboard((IDataObject **)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	COM_NATIVE_EXIT(env, that, OleGetClipboard_FUNC);
	return rc;
}
#endif

#ifndef NO_OleIsCurrentClipboard
JNIEXPORT jint JNICALL COM_NATIVE(OleIsCurrentClipboard)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleIsCurrentClipboard_FUNC);
	rc = (jint)OleIsCurrentClipboard((IDataObject *)arg0);
	COM_NATIVE_EXIT(env, that, OleIsCurrentClipboard_FUNC);
	return rc;
}
#endif

#ifndef NO_OleIsRunning
JNIEXPORT jboolean JNICALL COM_NATIVE(OleIsRunning)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	COM_NATIVE_ENTER(env, that, OleIsRunning_FUNC);
	rc = (jboolean)OleIsRunning((LPOLEOBJECT)arg0);
	COM_NATIVE_EXIT(env, that, OleIsRunning_FUNC);
	return rc;
}
#endif

#ifndef NO_OleLoad
JNIEXPORT jint JNICALL COM_NATIVE(OleLoad)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2, jintArray arg3)
{
	GUID _arg1, *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleLoad_FUNC);
	if (arg1) if ((lparg1 = getGUIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)OleLoad((IStorage *)arg0, lparg1, (IOleClientSite *)arg2, (LPVOID *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) setGUIDFields(env, arg1, lparg1);
	COM_NATIVE_EXIT(env, that, OleLoad_FUNC);
	return rc;
}
#endif

#ifndef NO_OleRun
JNIEXPORT jint JNICALL COM_NATIVE(OleRun)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleRun_FUNC);
	rc = (jint)OleRun((LPUNKNOWN)arg0);
	COM_NATIVE_EXIT(env, that, OleRun_FUNC);
	return rc;
}
#endif

#ifndef NO_OleSave
JNIEXPORT jint JNICALL COM_NATIVE(OleSave)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleSave_FUNC);
	rc = (jint)OleSave((IPersistStorage *)arg0, (IStorage *)arg1, arg2);
	COM_NATIVE_EXIT(env, that, OleSave_FUNC);
	return rc;
}
#endif

#ifndef NO_OleSetClipboard
JNIEXPORT jint JNICALL COM_NATIVE(OleSetClipboard)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleSetClipboard_FUNC);
	rc = (jint)OleSetClipboard((IDataObject *)arg0);
	COM_NATIVE_EXIT(env, that, OleSetClipboard_FUNC);
	return rc;
}
#endif

#ifndef NO_OleSetContainedObject
JNIEXPORT jint JNICALL COM_NATIVE(OleSetContainedObject)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleSetContainedObject_FUNC);
	rc = (jint)OleSetContainedObject((LPUNKNOWN)arg0, arg1);
	COM_NATIVE_EXIT(env, that, OleSetContainedObject_FUNC);
	return rc;
}
#endif

#ifndef NO_OleSetMenuDescriptor
JNIEXPORT jint JNICALL COM_NATIVE(OleSetMenuDescriptor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleSetMenuDescriptor_FUNC);
	rc = (jint)OleSetMenuDescriptor((HOLEMENU)arg0, (HWND)arg1, (HWND)arg2, (LPOLEINPLACEFRAME)arg3, (LPOLEINPLACEACTIVEOBJECT)arg4);
	COM_NATIVE_EXIT(env, that, OleSetMenuDescriptor_FUNC);
	return rc;
}
#endif

#ifndef NO_OleTranslateColor
JNIEXPORT jint JNICALL COM_NATIVE(OleTranslateColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleTranslateColor_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)OleTranslateColor((OLE_COLOR)arg0, (HPALETTE)arg1, (COLORREF *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	COM_NATIVE_EXIT(env, that, OleTranslateColor_FUNC);
	return rc;
}
#endif

#ifndef NO_ProgIDFromCLSID
JNIEXPORT jint JNICALL COM_NATIVE(ProgIDFromCLSID)
	(JNIEnv *env, jclass that, jobject arg0, jintArray arg1)
{
	GUID _arg0, *lparg0=NULL;
	jint *lparg1=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, ProgIDFromCLSID_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)ProgIDFromCLSID(lparg0, (LPOLESTR *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, ProgIDFromCLSID_FUNC);
	return rc;
}
#endif

#ifndef NO_RegisterDragDrop
JNIEXPORT jint JNICALL COM_NATIVE(RegisterDragDrop)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, RegisterDragDrop_FUNC);
	rc = (jint)RegisterDragDrop((HWND)arg0, (IDropTarget *)arg1);
	COM_NATIVE_EXIT(env, that, RegisterDragDrop_FUNC);
	return rc;
}
#endif

#ifndef NO_ReleaseStgMedium
JNIEXPORT void JNICALL COM_NATIVE(ReleaseStgMedium)
	(JNIEnv *env, jclass that, jint arg0)
{
	COM_NATIVE_ENTER(env, that, ReleaseStgMedium_FUNC);
	ReleaseStgMedium((STGMEDIUM *)arg0);
	COM_NATIVE_EXIT(env, that, ReleaseStgMedium_FUNC);
}
#endif

#ifndef NO_RevokeDragDrop
JNIEXPORT jint JNICALL COM_NATIVE(RevokeDragDrop)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, RevokeDragDrop_FUNC);
	rc = (jint)RevokeDragDrop((HWND)arg0);
	COM_NATIVE_EXIT(env, that, RevokeDragDrop_FUNC);
	return rc;
}
#endif

#ifndef NO_StgCreateDocfile
JNIEXPORT jint JNICALL COM_NATIVE(StgCreateDocfile)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2, jintArray arg3)
{
	jchar *lparg0=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, StgCreateDocfile_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)StgCreateDocfile(lparg0, arg1, arg2, (IStorage **)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	COM_NATIVE_EXIT(env, that, StgCreateDocfile_FUNC);
	return rc;
}
#endif

#ifndef NO_StgIsStorageFile
JNIEXPORT jint JNICALL COM_NATIVE(StgIsStorageFile)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, StgIsStorageFile_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)StgIsStorageFile((const WCHAR *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	COM_NATIVE_EXIT(env, that, StgIsStorageFile_FUNC);
	return rc;
}
#endif

#ifndef NO_StgOpenStorage
JNIEXPORT jint JNICALL COM_NATIVE(StgOpenStorage)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5)
{
	jchar *lparg0=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, StgOpenStorage_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)StgOpenStorage((const WCHAR *)lparg0, (IStorage *)arg1, arg2, (SNB)arg3, arg4, (IStorage **)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	COM_NATIVE_EXIT(env, that, StgOpenStorage_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFromCLSID
JNIEXPORT jint JNICALL COM_NATIVE(StringFromCLSID)
	(JNIEnv *env, jclass that, jobject arg0, jintArray arg1)
{
	GUID _arg0, *lparg0=NULL;
	jint *lparg1=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, StringFromCLSID_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)StringFromCLSID(lparg0, (LPOLESTR *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, StringFromCLSID_FUNC);
	return rc;
}
#endif

#ifndef NO_SysAllocString
JNIEXPORT jint JNICALL COM_NATIVE(SysAllocString)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, SysAllocString_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)SysAllocString((OLECHAR *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	COM_NATIVE_EXIT(env, that, SysAllocString_FUNC);
	return rc;
}
#endif

#ifndef NO_SysFreeString
JNIEXPORT void JNICALL COM_NATIVE(SysFreeString)
	(JNIEnv *env, jclass that, jint arg0)
{
	COM_NATIVE_ENTER(env, that, SysFreeString_FUNC);
	SysFreeString((BSTR)arg0);
	COM_NATIVE_EXIT(env, that, SysFreeString_FUNC);
}
#endif

#ifndef NO_SysStringByteLen
JNIEXPORT jint JNICALL COM_NATIVE(SysStringByteLen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, SysStringByteLen_FUNC);
	rc = (jint)SysStringByteLen((BSTR)arg0);
	COM_NATIVE_EXIT(env, that, SysStringByteLen_FUNC);
	return rc;
}
#endif

#ifndef NO_VariantChangeType
JNIEXPORT jint JNICALL COM_NATIVE(VariantChangeType)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshort arg2, jshort arg3)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VariantChangeType_FUNC);
	rc = (jint)VariantChangeType((VARIANTARG FAR* )arg0, (VARIANTARG FAR* )arg1, arg2, (VARTYPE)arg3);
	COM_NATIVE_EXIT(env, that, VariantChangeType_FUNC);
	return rc;
}
#endif

#ifndef NO_VariantClear
JNIEXPORT jint JNICALL COM_NATIVE(VariantClear)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VariantClear_FUNC);
	rc = (jint)VariantClear((VARIANTARG FAR* )arg0);
	COM_NATIVE_EXIT(env, that, VariantClear_FUNC);
	return rc;
}
#endif

#ifndef NO_VariantInit
JNIEXPORT void JNICALL COM_NATIVE(VariantInit)
	(JNIEnv *env, jclass that, jint arg0)
{
	COM_NATIVE_ENTER(env, that, VariantInit_FUNC);
	VariantInit((VARIANTARG FAR* )arg0);
	COM_NATIVE_EXIT(env, that, VariantInit_FUNC);
}
#endif

#ifndef NO_VtblCall__IIII
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3);
	COM_NATIVE_EXIT(env, that, VtblCall__IIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIII
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4);
	COM_NATIVE_EXIT(env, that, VtblCall__IIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIIII
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
	COM_NATIVE_EXIT(env, that, VtblCall__IIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIIIII
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIIIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
	COM_NATIVE_EXIT(env, that, VtblCall__IIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIIIIII
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIIIIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	COM_NATIVE_EXIT(env, that, VtblCall__IIIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIIIIIIII
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIIIIIIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIIIIIIIII_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint, jint, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	COM_NATIVE_EXIT(env, that, VtblCall__IIIIIIIIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jobject arg5)
{
	DVTARGETDEVICE _arg4, *lparg4=NULL;
	SIZE _arg5, *lparg5=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
	if (arg4) if ((lparg4 = getDVTARGETDEVICEFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getSIZEFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, DVTARGETDEVICE *, SIZE *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) setSIZEFields(env, arg5, lparg5);
	if (arg4 && lparg4) setDVTARGETDEVICEFields(env, arg4, lparg4);
	COM_NATIVE_EXIT(env, that, VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jintArray arg6)
{
	GUID _arg4, *lparg4=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I_FUNC);
	if (arg4) if ((lparg4 = getGUIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, GUID *, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGUIDFields(env, arg4, lparg4);
	COM_NATIVE_EXIT(env, that, VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIII_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIII_3I_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	COM_NATIVE_EXIT(env, that, VtblCall__IIII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jintArray arg4)
{
	FORMATETC _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I_FUNC);
	if (arg3) if ((lparg3 = getFORMATETCFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, FORMATETC *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setFORMATETCFields(env, arg3, lparg3);
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	GUID _arg3, *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC);
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, GUID *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jint arg5)
{
	GUID _arg3, *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC);
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, GUID *, jint, jint))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jobject arg6, jint arg7, jobject arg8, jintArray arg9)
{
	GUID _arg3, *lparg3=NULL;
	DISPPARAMS _arg6, *lparg6=NULL;
	EXCEPINFO _arg8, *lparg8=NULL;
	jint *lparg9=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC);
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getDISPPARAMSFields(env, arg6, &_arg6)) == NULL) goto fail;
	if (arg8) if ((lparg8 = getEXCEPINFOFields(env, arg8, &_arg8)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, GUID *, jint, jint, DISPPARAMS *, jint, EXCEPINFO *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4, arg5, lparg6, arg7, lparg8, lparg9);
fail:
	if (arg9 && lparg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg8 && lparg8) setEXCEPINFOFields(env, arg8, lparg8);
	if (arg6 && lparg6) setDISPPARAMSFields(env, arg6, lparg6);
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jintArray arg4)
{
	STATSTG _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I_FUNC);
	if (arg3) if ((lparg3 = getSTATSTGFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, STATSTG *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setSTATSTGFields(env, arg3, lparg3);
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jint arg6, jobject arg7)
{
	MSG _arg3, *lparg3=NULL;
	RECT _arg7, *lparg7=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
	if (arg3) if ((lparg3 = getMSGFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getRECTFields(env, arg7, &_arg7)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, MSG *, jint, jint, jint, RECT *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) setRECTFields(env, arg7, lparg7);
	if (arg3 && lparg3) setMSGFields(env, arg3, lparg3);
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	SIZE _arg3, *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
	if (arg3) if ((lparg3 = getSIZEFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, SIZE *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setSIZEFields(env, arg3, lparg3);
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IIIZ
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IIIZ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jboolean))(*(jint **)arg1)[arg0])(arg1, arg2, arg3);
	COM_NATIVE_EXIT(env, that, VtblCall__IIIZ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__III_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__III_3I_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	COM_NATIVE_EXIT(env, that, VtblCall__III_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3II_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__III_3II_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5)
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__III_3II_3I_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint *, jint, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	COM_NATIVE_EXIT(env, that, VtblCall__III_3II_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__III_3I_3I_3I_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__III_3I_3I_3I_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6)
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__III_3I_3I_3I_3I_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint, jint *, jint *, jint *, jint *))(*(jint **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	COM_NATIVE_EXIT(env, that, VtblCall__III_3I_3I_3I_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	CAUUID _arg2, *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2_FUNC);
	if (arg2) if ((lparg2 = getCAUUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, CAUUID *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setCAUUIDFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	CONTROLINFO _arg2, *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2_FUNC);
	if (arg2) if ((lparg2 = getCONTROLINFOFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, CONTROLINFO *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setCONTROLINFOFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	FORMATETC _arg2, *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_FUNC);
	if (arg2) if ((lparg2 = getFORMATETCFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, FORMATETC *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setFORMATETCFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3)
{
	FORMATETC _arg2, *lparg2=NULL;
	STGMEDIUM _arg3, *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2_FUNC);
	if (arg2) if ((lparg2 = getFORMATETCFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getSTGMEDIUMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, FORMATETC *, STGMEDIUM *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) setSTGMEDIUMFields(env, arg3, lparg3);
	if (arg2 && lparg2) setFORMATETCFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3, jboolean arg4)
{
	FORMATETC _arg2, *lparg2=NULL;
	STGMEDIUM _arg3, *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z_FUNC);
	if (arg2) if ((lparg2 = getFORMATETCFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getSTGMEDIUMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, FORMATETC *, STGMEDIUM *, jboolean))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) setSTGMEDIUMFields(env, arg3, lparg3);
	if (arg2 && lparg2) setFORMATETCFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	GUID _arg2, *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC);
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, GUID *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	GUID _arg2, *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII_FUNC);
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, GUID *, jint, jint, jint, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	GUID _arg2, *lparg2=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I_FUNC);
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, GUID *, jint, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jobject arg4, jobject arg5)
{
	GUID _arg2, *lparg2=NULL;
	OLECMD _arg4, *lparg4=NULL;
	OLECMDTEXT _arg5, *lparg5=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2_FUNC);
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getOLECMDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getOLECMDTEXTFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, GUID *, jint, OLECMD *, OLECMDTEXT *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) setOLECMDTEXTFields(env, arg5, lparg5);
	if (arg4 && lparg4) setOLECMDFields(env, arg4, lparg4);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jintArray arg3)
{
	GUID _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC);
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, GUID *, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	LICINFO _arg2, *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2_FUNC);
	if (arg2) if ((lparg2 = getLICINFOFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, LICINFO *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setLICINFOFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	MSG _arg2, *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2_FUNC);
	if (arg2) if ((lparg2 = getMSGFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, MSG *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setMSGFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	RECT _arg2, *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, RECT *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jint arg3, jboolean arg4)
{
	RECT _arg2, *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ_FUNC);
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, RECT *, jint, jboolean))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2, jobject arg3)
{
	RECT _arg2, *lparg2=NULL;
	RECT _arg3, *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2_FUNC);
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getRECTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, RECT *, RECT *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) setRECTFields(env, arg3, lparg3);
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3C
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__II_3C_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	COM_NATIVE_EXIT(env, that, VtblCall__II_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3CI
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3CI)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3)
{
	jchar *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__II_3CI_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jint))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	COM_NATIVE_EXIT(env, that, VtblCall__II_3CI_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3CIIII_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3CIIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintArray arg7)
{
	jchar *lparg2=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__II_3CIIII_3I_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jint, jint, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	COM_NATIVE_EXIT(env, that, VtblCall__II_3CIIII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3CIII_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3CIII_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	jchar *lparg2=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__II_3CIII_3I_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jint, jint, jint, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	COM_NATIVE_EXIT(env, that, VtblCall__II_3CIII_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3C_3C
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3C_3C)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jcharArray arg2, jcharArray arg3)
{
	jchar *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__II_3C_3C_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jchar *, jchar *))(*(jint **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
	COM_NATIVE_EXIT(env, that, VtblCall__II_3C_3C_FUNC);
	return rc;
}
#endif

#ifndef NO_VtblCall__II_3I
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3I)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__II_3I_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jint, jint *))(*(jint **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	COM_NATIVE_EXIT(env, that, VtblCall__II_3I_FUNC);
	return rc;
}
#endif

#ifndef NO_WriteClassStg
JNIEXPORT jint JNICALL COM_NATIVE(WriteClassStg)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	GUID _arg1, *lparg1=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, WriteClassStg_FUNC);
	if (arg1) if ((lparg1 = getGUIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jint)WriteClassStg((IStorage *)arg0, lparg1);
fail:
	if (arg1 && lparg1) setGUIDFields(env, arg1, lparg1);
	COM_NATIVE_EXIT(env, that, WriteClassStg_FUNC);
	return rc;
}
#endif

