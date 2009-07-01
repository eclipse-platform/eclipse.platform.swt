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

#include "swt.h"
#include "com_structs.h"
#include "com_stats.h"

#define COM_NATIVE(func) Java_org_eclipse_swt_internal_ole_win32_COM_##func

#ifndef NO_AccessibleObjectFromWindow
JNIEXPORT jint JNICALL COM_NATIVE(AccessibleObjectFromWindow)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2, jintLongArray arg3)
{
	GUID _arg2, *lparg2=NULL;
	jintLong *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, AccessibleObjectFromWindow_FUNC);
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
/*
	rc = (jint)AccessibleObjectFromWindow((HWND)arg0, (DWORD)arg1, lparg2, (LPVOID *)lparg3);
*/
	{
		LOAD_FUNCTION(fp, AccessibleObjectFromWindow)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(HWND, DWORD, GUID *, LPVOID *))fp)((HWND)arg0, (DWORD)arg1, lparg2, (LPVOID *)lparg3);
		}
	}
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, AccessibleObjectFromWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_CAUUID_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(CAUUID_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CAUUID_1sizeof_FUNC);
	rc = (jint)CAUUID_sizeof();
	COM_NATIVE_EXIT(env, that, CAUUID_1sizeof_FUNC);
	return rc;
}
#endif

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

#ifndef NO_CONTROLINFO_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(CONTROLINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CONTROLINFO_1sizeof_FUNC);
	rc = (jint)CONTROLINFO_sizeof();
	COM_NATIVE_EXIT(env, that, CONTROLINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_COSERVERINFO_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(COSERVERINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, COSERVERINFO_1sizeof_FUNC);
	rc = (jint)COSERVERINFO_sizeof();
	COM_NATIVE_EXIT(env, that, COSERVERINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_CoCreateInstance
JNIEXPORT jint JNICALL COM_NATIVE(CoCreateInstance)
	(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2, jobject arg3, jintLongArray arg4)
{
	GUID _arg0, *lparg0=NULL;
	GUID _arg3, *lparg3=NULL;
	jintLong *lparg4=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CoCreateInstance_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)CoCreateInstance(lparg0, (LPUNKNOWN)arg1, arg2, lparg3, (LPVOID *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
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
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jintLong arg2, jobject arg3, jintLongArray arg4)
{
	GUID _arg0, *lparg0=NULL;
	GUID _arg3, *lparg3=NULL;
	jintLong *lparg4=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CoGetClassObject_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)CoGetClassObject(lparg0, arg1, (COSERVERINFO *)arg2, lparg3, (LPVOID *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, CoGetClassObject_FUNC);
	return rc;
}
#endif

#ifndef NO_CoLockObjectExternal
JNIEXPORT jint JNICALL COM_NATIVE(CoLockObjectExternal)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1, jboolean arg2)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CoLockObjectExternal_FUNC);
	rc = (jint)CoLockObjectExternal((IUnknown *)arg0, (BOOL)arg1, (BOOL)arg2);
	COM_NATIVE_EXIT(env, that, CoLockObjectExternal_FUNC);
	return rc;
}
#endif

#ifndef NO_CoTaskMemAlloc
JNIEXPORT jintLong JNICALL COM_NATIVE(CoTaskMemAlloc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jintLong rc = 0;
	COM_NATIVE_ENTER(env, that, CoTaskMemAlloc_FUNC);
	rc = (jintLong)CoTaskMemAlloc((ULONG)arg0);
	COM_NATIVE_EXIT(env, that, CoTaskMemAlloc_FUNC);
	return rc;
}
#endif

#ifndef NO_CoTaskMemFree
JNIEXPORT void JNICALL COM_NATIVE(CoTaskMemFree)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	COM_NATIVE_ENTER(env, that, CoTaskMemFree_FUNC);
	CoTaskMemFree((LPVOID)arg0);
	COM_NATIVE_EXIT(env, that, CoTaskMemFree_FUNC);
}
#endif

#ifndef NO_CreateStdAccessibleObject
JNIEXPORT jint JNICALL COM_NATIVE(CreateStdAccessibleObject)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jobject arg2, jintLongArray arg3)
{
	GUID _arg2, *lparg2=NULL;
	jintLong *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, CreateStdAccessibleObject_FUNC);
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
/*
	rc = (jint)CreateStdAccessibleObject((HWND)arg0, arg1, lparg2, (LPVOID *)lparg3);
*/
	{
		LOAD_FUNCTION(fp, CreateStdAccessibleObject)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(HWND, jint, GUID *, LPVOID *))fp)((HWND)arg0, arg1, lparg2, (LPVOID *)lparg3);
		}
	}
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
	COM_NATIVE_EXIT(env, that, CreateStdAccessibleObject_FUNC);
	return rc;
}
#endif

#ifndef NO_DISPPARAMS_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(DISPPARAMS_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, DISPPARAMS_1sizeof_FUNC);
	rc = (jint)DISPPARAMS_sizeof();
	COM_NATIVE_EXIT(env, that, DISPPARAMS_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_DVTARGETDEVICE_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(DVTARGETDEVICE_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, DVTARGETDEVICE_1sizeof_FUNC);
	rc = (jint)DVTARGETDEVICE_sizeof();
	COM_NATIVE_EXIT(env, that, DVTARGETDEVICE_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_DoDragDrop
JNIEXPORT jint JNICALL COM_NATIVE(DoDragDrop)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jintArray arg3)
{
	jint *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, DoDragDrop_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)DoDragDrop((IDataObject *)arg0, (IDropSource *)arg1, arg2, (LPDWORD)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	COM_NATIVE_EXIT(env, that, DoDragDrop_FUNC);
	return rc;
}
#endif

#ifndef NO_ELEMDESC_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(ELEMDESC_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, ELEMDESC_1sizeof_FUNC);
	rc = (jint)ELEMDESC_sizeof();
	COM_NATIVE_EXIT(env, that, ELEMDESC_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_EXCEPINFO_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(EXCEPINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, EXCEPINFO_1sizeof_FUNC);
	rc = (jint)EXCEPINFO_sizeof();
	COM_NATIVE_EXIT(env, that, EXCEPINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_FORMATETC_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(FORMATETC_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, FORMATETC_1sizeof_FUNC);
	rc = (jint)FORMATETC_sizeof();
	COM_NATIVE_EXIT(env, that, FORMATETC_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_FUNCDESC_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(FUNCDESC_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, FUNCDESC_1sizeof_FUNC);
	rc = (jint)FUNCDESC_sizeof();
	COM_NATIVE_EXIT(env, that, FUNCDESC_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_GUID_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(GUID_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, GUID_1sizeof_FUNC);
	rc = (jint)GUID_sizeof();
	COM_NATIVE_EXIT(env, that, GUID_1sizeof_FUNC);
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

#ifndef NO_LICINFO_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(LICINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, LICINFO_1sizeof_FUNC);
	rc = (jint)LICINFO_sizeof();
	COM_NATIVE_EXIT(env, that, LICINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_LresultFromObject
JNIEXPORT jintLong JNICALL COM_NATIVE(LresultFromObject)
	(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jintLong arg2)
{
	GUID _arg0, *lparg0=NULL;
	jintLong rc = 0;
	COM_NATIVE_ENTER(env, that, LresultFromObject_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
/*
	rc = (jintLong)LresultFromObject(lparg0, arg1, (LPUNKNOWN)arg2);
*/
	{
		LOAD_FUNCTION(fp, LresultFromObject)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(GUID *, jintLong, LPUNKNOWN))fp)(lparg0, arg1, (LPUNKNOWN)arg2);
		}
	}
fail:
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, LresultFromObject_FUNC);
	return rc;
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_ole_win32_FORMATETC_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_ole_win32_FORMATETC_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	FORMATETC _arg1, *lparg1=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_ole_win32_FORMATETC_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getFORMATETCFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_FORMATETC_2I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_ole_win32_FORMATETC_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_ole_win32_GUID_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_ole_win32_GUID_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	GUID _arg1, *lparg1=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_ole_win32_GUID_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getGUIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_GUID_2I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_ole_win32_GUID_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	OLEINPLACEFRAMEINFO _arg1, *lparg1=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getOLEINPLACEFRAMEINFOFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_ole_win32_OLEINPLACEFRAMEINFO_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_ole_win32_STATSTG_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_ole_win32_STATSTG_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	STATSTG _arg1, *lparg1=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_ole_win32_STATSTG_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getSTATSTGFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STATSTG_2I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_ole_win32_STATSTG_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I) && !defined(JNI64)) || (!defined(NO_MoveMemory__JLorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__JLorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I)(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jint arg2)
#endif
{
	STGMEDIUM _arg1, *lparg1=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__JLorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I_FUNC);
#endif
	if (arg1) if ((lparg1 = getSTGMEDIUMFields(env, arg1, &_arg1)) == NULL) goto fail;
	MoveMemory((PVOID)arg0, (CONST VOID *)lparg1, arg2);
fail:
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__ILorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__JLorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2I_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	DISPPARAMS _arg0, *lparg0=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setDISPPARAMSFields(env, arg0, lparg0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2II_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	FORMATETC _arg0, *lparg0=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setFORMATETCFields(env, arg0, lparg0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2II_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FORMATETC_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	FUNCDESC _arg0, *lparg0=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2II_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setFUNCDESCFields(env, arg0, lparg0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2II_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_FUNCDESC_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	GUID _arg0, *lparg0=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_GUID_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	STATSTG _arg0, *lparg0=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setSTATSTGFields(env, arg0, lparg0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2II_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STATSTG_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	STGMEDIUM _arg0, *lparg0=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setSTGMEDIUMFields(env, arg0, lparg0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2II_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	TYPEATTR _arg0, *lparg0=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setTYPEATTRFields(env, arg0, lparg0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2II_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_TYPEATTR_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	VARDESC _arg0, *lparg0=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2II_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setVARDESCFields(env, arg0, lparg0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2II_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARDESC_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	VARIANT _arg0, *lparg0=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2II_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setVARIANTFields(env, arg0, lparg0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2II_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_ole_win32_VARIANT_2JI_FUNC);
#endif
}
#endif

#if (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II) && !defined(JNI64)) || (!defined(NO_MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2JI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#else
JNIEXPORT void JNICALL COM_NATIVE(MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2JI)(JNIEnv *env, jclass that, jobject arg0, jintLong arg1, jint arg2)
#endif
{
	RECT _arg0, *lparg0=NULL;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II_FUNC);
#else
	COM_NATIVE_ENTER(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2JI_FUNC);
#endif
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	MoveMemory((PVOID)lparg0, (CONST VOID *)arg1, arg2);
fail:
	if (arg0 && lparg0) setRECTFields(env, arg0, lparg0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2II_FUNC);
#else
	COM_NATIVE_EXIT(env, that, MoveMemory__Lorg_eclipse_swt_internal_win32_RECT_2JI_FUNC);
#endif
}
#endif

#ifndef NO_OLECMD_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(OLECMD_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OLECMD_1sizeof_FUNC);
	rc = (jint)OLECMD_sizeof();
	COM_NATIVE_EXIT(env, that, OLECMD_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_OLEINPLACEFRAMEINFO_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(OLEINPLACEFRAMEINFO_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OLEINPLACEFRAMEINFO_1sizeof_FUNC);
	rc = (jint)OLEINPLACEFRAMEINFO_sizeof();
	COM_NATIVE_EXIT(env, that, OLEINPLACEFRAMEINFO_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_OleCreate
JNIEXPORT jint JNICALL COM_NATIVE(OleCreate)
	(JNIEnv *env, jclass that, jobject arg0, jobject arg1, jint arg2, jobject arg3, jintLong arg4, jintLong arg5, jintLongArray arg6)
{
	GUID _arg0, *lparg0=NULL;
	GUID _arg1, *lparg1=NULL;
	FORMATETC _arg3, *lparg3=NULL;
	jintLong *lparg6=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleCreate_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = getGUIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getFORMATETCFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntLongArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)OleCreate(lparg0, lparg1, arg2, lparg3, (IOleClientSite *)arg4, (IStorage *)arg5, (void **)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntLongArrayElements(env, arg6, lparg6, 0);
	if (arg3 && lparg3) setFORMATETCFields(env, arg3, lparg3);
	if (arg1 && lparg1) setGUIDFields(env, arg1, lparg1);
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, OleCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_OleCreateFromFile
JNIEXPORT jint JNICALL COM_NATIVE(OleCreateFromFile)
	(JNIEnv *env, jclass that, jobject arg0, jcharArray arg1, jobject arg2, jint arg3, jobject arg4, jintLong arg5, jintLong arg6, jintLongArray arg7)
{
	GUID _arg0, *lparg0=NULL;
	jchar *lparg1=NULL;
	GUID _arg2, *lparg2=NULL;
	FORMATETC _arg4, *lparg4=NULL;
	jintLong *lparg7=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleCreateFromFile_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getFORMATETCFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntLongArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)OleCreateFromFile(lparg0, (LPCOLESTR)lparg1, lparg2, arg3, lparg4, (LPOLECLIENTSITE)arg5, (LPSTORAGE)arg6, (LPVOID *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntLongArrayElements(env, arg7, lparg7, 0);
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
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jcharArray arg3, jint arg4, jintLongArray arg5, jint arg6, jintLong arg7, jint arg8, jint arg9, jintLong arg10)
{
	jchar *lparg3=NULL;
	jintLong *lparg5=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleCreatePropertyFrame_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntLongArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)OleCreatePropertyFrame((HWND)arg0, arg1, arg2, (LPCOLESTR)lparg3, arg4, (LPUNKNOWN FAR*)lparg5, arg6, (LPCLSID)arg7, (LCID)arg8, arg9, (LPVOID)arg10);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntLongArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	COM_NATIVE_EXIT(env, that, OleCreatePropertyFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_OleDraw
JNIEXPORT jint JNICALL COM_NATIVE(OleDraw)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3)
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
	(JNIEnv *env, jclass that, jintLongArray arg0)
{
	jintLong *lparg0=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleGetClipboard_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntLongArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)OleGetClipboard((IDataObject **)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntLongArrayElements(env, arg0, lparg0, 0);
	COM_NATIVE_EXIT(env, that, OleGetClipboard_FUNC);
	return rc;
}
#endif

#ifndef NO_OleIsCurrentClipboard
JNIEXPORT jint JNICALL COM_NATIVE(OleIsCurrentClipboard)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1, jintLong arg2, jintLongArray arg3)
{
	GUID _arg1, *lparg1=NULL;
	jintLong *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, OleLoad_FUNC);
	if (arg1) if ((lparg1 = getGUIDFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)OleLoad((IStorage *)arg0, lparg1, (IOleClientSite *)arg2, (LPVOID *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) setGUIDFields(env, arg1, lparg1);
	COM_NATIVE_EXIT(env, that, OleLoad_FUNC);
	return rc;
}
#endif

#ifndef NO_OleRun
JNIEXPORT jint JNICALL COM_NATIVE(OleRun)
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jboolean arg2)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4)
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
	(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintArray arg2)
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
	(JNIEnv *env, jclass that, jobject arg0, jintLongArray arg1)
{
	GUID _arg0, *lparg0=NULL;
	jintLong *lparg1=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, ProgIDFromCLSID_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)ProgIDFromCLSID(lparg0, (LPOLESTR *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, ProgIDFromCLSID_FUNC);
	return rc;
}
#endif

#ifndef NO_RegisterDragDrop
JNIEXPORT jint JNICALL COM_NATIVE(RegisterDragDrop)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
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
	(JNIEnv *env, jclass that, jintLong arg0)
{
	COM_NATIVE_ENTER(env, that, ReleaseStgMedium_FUNC);
	ReleaseStgMedium((STGMEDIUM *)arg0);
	COM_NATIVE_EXIT(env, that, ReleaseStgMedium_FUNC);
}
#endif

#ifndef NO_RevokeDragDrop
JNIEXPORT jint JNICALL COM_NATIVE(RevokeDragDrop)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, RevokeDragDrop_FUNC);
	rc = (jint)RevokeDragDrop((HWND)arg0);
	COM_NATIVE_EXIT(env, that, RevokeDragDrop_FUNC);
	return rc;
}
#endif

#ifndef NO_SHDoDragDrop
JNIEXPORT jint JNICALL COM_NATIVE(SHDoDragDrop)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jint arg3, jintArray arg4)
{
	jint *lparg4=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, SHDoDragDrop_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)SHDoDragDrop((HWND)arg0, (IDataObject *)arg1, (IDropSource *)arg2, arg3, (DWORD *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	COM_NATIVE_EXIT(env, that, SHDoDragDrop_FUNC);
	return rc;
}
#endif

#ifndef NO_STATSTG_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(STATSTG_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, STATSTG_1sizeof_FUNC);
	rc = (jint)STATSTG_sizeof();
	COM_NATIVE_EXIT(env, that, STATSTG_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_STGMEDIUM_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(STGMEDIUM_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, STGMEDIUM_1sizeof_FUNC);
	rc = (jint)STGMEDIUM_sizeof();
	COM_NATIVE_EXIT(env, that, STGMEDIUM_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_StgCreateDocfile
JNIEXPORT jint JNICALL COM_NATIVE(StgCreateDocfile)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2, jintLongArray arg3)
{
	jchar *lparg0=NULL;
	jintLong *lparg3=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, StgCreateDocfile_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)StgCreateDocfile(lparg0, arg1, arg2, (IStorage **)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntLongArrayElements(env, arg3, lparg3, 0);
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
	(JNIEnv *env, jclass that, jcharArray arg0, jintLong arg1, jint arg2, jintLong arg3, jint arg4, jintLongArray arg5)
{
	jchar *lparg0=NULL;
	jintLong *lparg5=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, StgOpenStorage_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntLongArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)StgOpenStorage((const WCHAR *)lparg0, (IStorage *)arg1, arg2, (SNB)arg3, arg4, (IStorage **)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntLongArrayElements(env, arg5, lparg5, 0);
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	COM_NATIVE_EXIT(env, that, StgOpenStorage_FUNC);
	return rc;
}
#endif

#ifndef NO_StringFromCLSID
JNIEXPORT jint JNICALL COM_NATIVE(StringFromCLSID)
	(JNIEnv *env, jclass that, jobject arg0, jintLongArray arg1)
{
	GUID _arg0, *lparg0=NULL;
	jintLong *lparg1=NULL;
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, StringFromCLSID_FUNC);
	if (arg0) if ((lparg0 = getGUIDFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetIntLongArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)StringFromCLSID(lparg0, (LPOLESTR *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntLongArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) setGUIDFields(env, arg0, lparg0);
	COM_NATIVE_EXIT(env, that, StringFromCLSID_FUNC);
	return rc;
}
#endif

#ifndef NO_SysAllocString
JNIEXPORT jintLong JNICALL COM_NATIVE(SysAllocString)
	(JNIEnv *env, jclass that, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jintLong rc = 0;
	COM_NATIVE_ENTER(env, that, SysAllocString_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)SysAllocString((OLECHAR *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	COM_NATIVE_EXIT(env, that, SysAllocString_FUNC);
	return rc;
}
#endif

#ifndef NO_SysFreeString
JNIEXPORT void JNICALL COM_NATIVE(SysFreeString)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	COM_NATIVE_ENTER(env, that, SysFreeString_FUNC);
	SysFreeString((BSTR)arg0);
	COM_NATIVE_EXIT(env, that, SysFreeString_FUNC);
}
#endif

#ifndef NO_SysStringByteLen
JNIEXPORT jint JNICALL COM_NATIVE(SysStringByteLen)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, SysStringByteLen_FUNC);
	rc = (jint)SysStringByteLen((BSTR)arg0);
	COM_NATIVE_EXIT(env, that, SysStringByteLen_FUNC);
	return rc;
}
#endif

#ifndef NO_TYPEATTR_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(TYPEATTR_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, TYPEATTR_1sizeof_FUNC);
	rc = (jint)TYPEATTR_sizeof();
	COM_NATIVE_EXIT(env, that, TYPEATTR_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_TYPEDESC_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(TYPEDESC_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, TYPEDESC_1sizeof_FUNC);
	rc = (jint)TYPEDESC_sizeof();
	COM_NATIVE_EXIT(env, that, TYPEDESC_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_VARDESC_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(VARDESC_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VARDESC_1sizeof_FUNC);
	rc = (jint)VARDESC_sizeof();
	COM_NATIVE_EXIT(env, that, VARDESC_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_VARIANT_1sizeof
JNIEXPORT jint JNICALL COM_NATIVE(VARIANT_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VARIANT_1sizeof_FUNC);
	rc = (jint)VARIANT_sizeof();
	COM_NATIVE_EXIT(env, that, VARIANT_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_VariantChangeType
JNIEXPORT jint JNICALL COM_NATIVE(VariantChangeType)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jshort arg2, jshort arg3)
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
	(JNIEnv *env, jclass that, jintLong arg0)
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
	(JNIEnv *env, jclass that, jintLong arg0)
{
	COM_NATIVE_ENTER(env, that, VariantInit_FUNC);
	VariantInit((VARIANTARG FAR* )arg0);
	COM_NATIVE_EXIT(env, that, VariantInit_FUNC);
}
#endif

#if (!defined(NO_VtblCall__IIII) && !defined(JNI64)) || (!defined(NO_VtblCall__IJII) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIII_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIII_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIII) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIII) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIII_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIII_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIIII) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIIII) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIIII_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIIII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIIII_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIIIII) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIIIII) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIIIII_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIIIII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIIIII_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIIIIII) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIIIIIJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintLong arg7)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIIIIIJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintLong arg7)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIIIIII_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIIIIIJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, jint, jint, jintLong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIIIIII_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIIIIIJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIIIIIIII) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJJJJIIII) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIIIIIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jint arg6, jint arg7, jint arg8, jint arg9)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJJJJIIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jintLong arg2, jintLong arg3, jintLong arg4, jintLong arg5, jint arg6, jint arg7, jint arg8, jint arg9)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIIIIIIII_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJJJJIIII_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jintLong, jintLong, jintLong, jintLong, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIIIIIIII_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJJJJIIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jobject arg5, jint arg6, jobject arg7, jint arg8)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jobject arg5, jint arg6, jobject arg7, jint arg8)
#endif
{
	DISPPARAMS _arg5, *lparg5=NULL;
	EXCEPINFO _arg7, *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I_FUNC);
#endif
	if (arg5) if ((lparg5 = getDISPPARAMSFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getEXCEPINFOFields(env, arg7, &_arg7)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, DISPPARAMS *, jint, EXCEPINFO *, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5, arg6, lparg7, arg8);
fail:
	if (arg7 && lparg7) setEXCEPINFOFields(env, arg7, lparg7);
	if (arg5 && lparg5) setDISPPARAMSFields(env, arg5, lparg5);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jobject arg5, jlong arg6, jobject arg7, jlong arg8)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jint arg4, jobject arg5, jlong arg6, jobject arg7, jlong arg8)
#endif
{
	DISPPARAMS _arg5, *lparg5=NULL;
	EXCEPINFO _arg7, *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2J_FUNC);
#endif
	if (arg5) if ((lparg5 = getDISPPARAMSFields(env, arg5, &_arg5)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getEXCEPINFOFields(env, arg7, &_arg7)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint, DISPPARAMS *, jlong, EXCEPINFO *, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4, lparg5, arg6, lparg7, arg8);
fail:
	if (arg7 && lparg7) setEXCEPINFOFields(env, arg7, lparg7);
	if (arg5 && lparg5) setDISPPARAMSFields(env, arg5, lparg5);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIIILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIIJ) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIIJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIIJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlong arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIIJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlong arg4)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIIJ_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIIJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, arg4);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIIJ_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIIJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jobject arg5)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jobject arg5)
#endif
{
	DVTARGETDEVICE _arg4, *lparg4=NULL;
	SIZE _arg5, *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#endif
	if (arg4) if ((lparg4 = getDVTARGETDEVICEFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getSIZEFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, DVTARGETDEVICE *, SIZE *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) setSIZEFields(env, arg5, lparg5);
	if (arg4 && lparg4) setDVTARGETDEVICEFields(env, arg4, lparg4);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIILorg_eclipse_swt_internal_ole_win32_DVTARGETDEVICE_2Lorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jintArray arg6)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jintArray arg6)
#endif
{
	GUID _arg4, *lparg4=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I_FUNC);
#endif
	if (arg4) if ((lparg4 = getGUIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, GUID *, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGUIDFields(env, arg4, lparg4);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIILorg_eclipse_swt_internal_ole_win32_GUID_2I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIILorg_eclipse_swt_internal_win32_POINT_2I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIILorg_eclipse_swt_internal_win32_POINT_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIILorg_eclipse_swt_internal_win32_POINT_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIILorg_eclipse_swt_internal_win32_POINT_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jobject arg4, jint arg5)
#endif
{
	POINT _arg4, *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#endif
	if (arg4) if ((lparg4 = getPOINTFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, POINT *, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5);
fail:
	if (arg4 && lparg4) setPOINTFields(env, arg4, lparg4);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIII_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJII_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jintArray arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jintArray arg4)
#endif
{
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIII_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJII_3I_FUNC);
#endif
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIII_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJII_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIII_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJII_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlongArray arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jint arg3, jlongArray arg4)
#endif
{
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIII_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJII_3J_FUNC);
#endif
	if (arg4) if ((lparg4 = (*env)->GetLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseLongArrayElements(env, arg4, lparg4, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIII_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJII_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIJ) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlong arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlong arg3)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIJ_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIJ_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIJ_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIJ_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlong arg3, jintArray arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlong arg3, jintArray arg4)
#endif
{
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIJ_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIJ_3I_FUNC);
#endif
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jlong, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIJ_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIJ_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jintArray arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jintArray arg4)
#endif
{
	FORMATETC _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = getFORMATETCFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, FORMATETC *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setFORMATETCFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3)
#endif
{
	GUID _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, GUID *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II) && !defined(JNI64)) || (!defined(NO_VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2II) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jint arg4, jint arg5)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2II)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jint arg4, jint arg5)
#endif
{
	GUID _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC);
#endif
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, GUID *, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2II_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jobject arg6, jint arg7, jobject arg8, jintArray arg9)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jobject arg6, jint arg7, jobject arg8, jintArray arg9)
#endif
{
	GUID _arg3, *lparg3=NULL;
	DISPPARAMS _arg6, *lparg6=NULL;
	EXCEPINFO _arg8, *lparg8=NULL;
	jint *lparg9=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getDISPPARAMSFields(env, arg6, &_arg6)) == NULL) goto fail;
	if (arg8) if ((lparg8 = getEXCEPINFOFields(env, arg8, &_arg8)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, GUID *, jint, jint, DISPPARAMS *, jint, EXCEPINFO *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4, arg5, lparg6, arg7, lparg8, lparg9);
fail:
	if (arg9 && lparg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg8 && lparg8) setEXCEPINFOFields(env, arg8, lparg8);
	if (arg6 && lparg6) setDISPPARAMSFields(env, arg6, lparg6);
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2ILorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jobject arg6, jlong arg7, jobject arg8, jintArray arg9)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jobject arg6, jlong arg7, jobject arg8, jintArray arg9)
#endif
{
	GUID _arg3, *lparg3=NULL;
	DISPPARAMS _arg6, *lparg6=NULL;
	EXCEPINFO _arg8, *lparg8=NULL;
	jint *lparg9=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg6) if ((lparg6 = getDISPPARAMSFields(env, arg6, &_arg6)) == NULL) goto fail;
	if (arg8) if ((lparg8 = getEXCEPINFOFields(env, arg8, &_arg8)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, GUID *, jint, jint, DISPPARAMS *, jlong, EXCEPINFO *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4, arg5, lparg6, arg7, lparg8, lparg9);
fail:
	if (arg9 && lparg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg8 && lparg8) setEXCEPINFOFields(env, arg8, lparg8);
	if (arg6 && lparg6) setDISPPARAMSFields(env, arg6, lparg6);
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2IILorg_eclipse_swt_internal_ole_win32_DISPPARAMS_2JLorg_eclipse_swt_internal_ole_win32_EXCEPINFO_2_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2JJ) && !defined(JNI64)) || (!defined(NO_VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2JJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2JJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jlong arg4, jlong arg5)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2JJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jlong arg4, jlong arg5)
#endif
{
	GUID _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2JJ_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2JJ_FUNC);
#endif
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, GUID *, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_GUID_2JJ_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_GUID_2JJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jintArray arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jintArray arg4)
#endif
{
	STATSTG _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = getSTATSTGFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, STATSTG *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setSTATSTGFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJILorg_eclipse_swt_internal_ole_win32_STATSTG_2_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jint arg6, jobject arg7)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jint arg4, jint arg5, jint arg6, jobject arg7)
#endif
{
	MSG _arg3, *lparg3=NULL;
	RECT _arg7, *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getMSGFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getRECTFields(env, arg7, &_arg7)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, MSG *, jint, jint, jint, RECT *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) setRECTFields(env, arg7, lparg7);
	if (arg3 && lparg3) setMSGFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJILorg_eclipse_swt_internal_win32_MSG_2IIILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2JIJLorg_eclipse_swt_internal_win32_RECT_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJILorg_eclipse_swt_internal_win32_MSG_2JIJLorg_eclipse_swt_internal_win32_RECT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2JIJLorg_eclipse_swt_internal_win32_RECT_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jlong arg4, jint arg5, jlong arg6, jobject arg7)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJILorg_eclipse_swt_internal_win32_MSG_2JIJLorg_eclipse_swt_internal_win32_RECT_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jlong arg4, jint arg5, jlong arg6, jobject arg7)
#endif
{
	MSG _arg3, *lparg3=NULL;
	RECT _arg7, *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2JIJLorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJILorg_eclipse_swt_internal_win32_MSG_2JIJLorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getMSGFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg7) if ((lparg7 = getRECTFields(env, arg7, &_arg7)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, MSG *, jlong, jint, jlong, RECT *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) setRECTFields(env, arg7, lparg7);
	if (arg3 && lparg3) setMSGFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_win32_MSG_2JIJLorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJILorg_eclipse_swt_internal_win32_MSG_2JIJLorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIILorg_eclipse_swt_internal_win32_POINT_2I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJILorg_eclipse_swt_internal_win32_POINT_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_win32_POINT_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jint arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJILorg_eclipse_swt_internal_win32_POINT_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3, jint arg4)
#endif
{
	POINT _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#endif
	if (arg3) if ((lparg3 = getPOINTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, POINT *, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) setPOINTFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJILorg_eclipse_swt_internal_win32_SIZE_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJILorg_eclipse_swt_internal_win32_SIZE_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jobject arg3)
#endif
{
	SIZE _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#endif
	if (arg3) if ((lparg3 = getSIZEFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, SIZE *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setSIZEFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJILorg_eclipse_swt_internal_win32_SIZE_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIIZ) && !defined(JNI64)) || (!defined(NO_VtblCall__IJIZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIIZ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jboolean arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIZ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jboolean arg3)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIIZ_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJIZ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jboolean))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIIZ_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJIZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__III_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJI_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__III_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3)
#endif
{
	jint *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__III_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJI_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__III_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJI_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__III_3II_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJI_3II_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__III_3II_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJI_3II_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3, jint arg4, jintArray arg5)
#endif
{
	jint *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__III_3II_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJI_3II_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint *, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__III_3II_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJI_3II_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__III_3I_3I_3I_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJI_3I_3I_3I_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__III_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJI_3I_3I_3I_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6)
#endif
{
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__III_3I_3I_3I_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJI_3I_3I_3I_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jint *, jint *, jint *, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__III_3I_3I_3I_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJI_3I_3I_3I_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__III_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJI_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__III_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlongArray arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlongArray arg3)
#endif
{
	jlong *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__III_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJI_3J_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseLongArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__III_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJI_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__III_3JI_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJI_3JI_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__III_3JI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlongArray arg3, jint arg4, jintArray arg5)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJI_3JI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlongArray arg3, jint arg4, jintArray arg5)
#endif
{
	jlong *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__III_3JI_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJI_3JI_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jlong *, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4, lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseLongArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__III_3JI_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJI_3JI_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__III_3J_3J_3I_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJI_3J_3J_3I_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__III_3J_3J_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlongArray arg3, jlongArray arg4, jintArray arg5, jlongArray arg6)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJI_3J_3J_3I_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jint arg2, jlongArray arg3, jlongArray arg4, jintArray arg5, jlongArray arg6)
#endif
{
	jlong *lparg3=NULL;
	jlong *lparg4=NULL;
	jint *lparg5=NULL;
	jlong *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__III_3J_3J_3I_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJI_3J_3J_3I_3J_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetLongArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jint, jlong *, jlong *, jint *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4, lparg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseLongArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseLongArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__III_3J_3J_3I_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJI_3J_3J_3I_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJI) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIJI_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJI_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIJI_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJI_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJI_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIJI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jintArray arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJI_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jintArray arg4)
#endif
{
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIJI_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJI_3I_FUNC);
#endif
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIJI_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJI_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJI_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJI_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIJI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jlongArray arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJI_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jint arg3, jlongArray arg4)
#endif
{
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIJI_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJI_3J_FUNC);
#endif
	if (arg4) if ((lparg4 = (*env)->GetLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseLongArrayElements(env, arg4, lparg4, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIJI_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJI_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJJ) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIJJ_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIJJ_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJJLorg_eclipse_swt_internal_ole_win32_GUID_2J_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJJLorg_eclipse_swt_internal_ole_win32_GUID_2J_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIJJLorg_eclipse_swt_internal_ole_win32_GUID_2J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jobject arg4, jlong arg5, jlongArray arg6)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJJLorg_eclipse_swt_internal_ole_win32_GUID_2J_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jobject arg4, jlong arg5, jlongArray arg6)
#endif
{
	GUID _arg4, *lparg4=NULL;
	jlong *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIJJLorg_eclipse_swt_internal_ole_win32_GUID_2J_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJJLorg_eclipse_swt_internal_ole_win32_GUID_2J_3J_FUNC);
#endif
	if (arg4) if ((lparg4 = getGUIDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetLongArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, GUID *, jlong, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseLongArrayElements(env, arg6, lparg6, 0);
	if (arg4 && lparg4) setGUIDFields(env, arg4, lparg4);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIJJLorg_eclipse_swt_internal_ole_win32_GUID_2J_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJJLorg_eclipse_swt_internal_ole_win32_GUID_2J_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJJLorg_eclipse_swt_internal_win32_POINT_2I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJJLorg_eclipse_swt_internal_win32_POINT_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIJJLorg_eclipse_swt_internal_win32_POINT_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jobject arg4, jint arg5)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJJLorg_eclipse_swt_internal_win32_POINT_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlong arg3, jobject arg4, jint arg5)
#endif
{
	POINT _arg4, *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIJJLorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJJLorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#endif
	if (arg4) if ((lparg4 = getPOINTFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong, POINT *, jint))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3, lparg4, arg5);
fail:
	if (arg4 && lparg4) setPOINTFields(env, arg4, lparg4);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIJJLorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJJLorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3, jlongArray arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3, jlongArray arg4)
#endif
{
	FORMATETC _arg3, *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3J_FUNC);
#endif
	if (arg3) if ((lparg3 = getFORMATETCFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, FORMATETC *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setFORMATETCFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJLorg_eclipse_swt_internal_ole_win32_STATSTG_2_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJLorg_eclipse_swt_internal_ole_win32_STATSTG_2_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIJLorg_eclipse_swt_internal_ole_win32_STATSTG_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3, jlongArray arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJLorg_eclipse_swt_internal_ole_win32_STATSTG_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3, jlongArray arg4)
#endif
{
	STATSTG _arg3, *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIJLorg_eclipse_swt_internal_ole_win32_STATSTG_2_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJLorg_eclipse_swt_internal_ole_win32_STATSTG_2_3J_FUNC);
#endif
	if (arg3) if ((lparg3 = getSTATSTGFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, STATSTG *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setSTATSTGFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIJLorg_eclipse_swt_internal_ole_win32_STATSTG_2_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJLorg_eclipse_swt_internal_ole_win32_STATSTG_2_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJLorg_eclipse_swt_internal_win32_POINT_2J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJLorg_eclipse_swt_internal_win32_POINT_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIJLorg_eclipse_swt_internal_win32_POINT_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3, jlong arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJLorg_eclipse_swt_internal_win32_POINT_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jobject arg3, jlong arg4)
#endif
{
	POINT _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIJLorg_eclipse_swt_internal_win32_POINT_2J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJLorg_eclipse_swt_internal_win32_POINT_2J_FUNC);
#endif
	if (arg3) if ((lparg3 = getPOINTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, POINT *, jlong))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) setPOINTFields(env, arg3, lparg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIJLorg_eclipse_swt_internal_win32_POINT_2J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJLorg_eclipse_swt_internal_win32_POINT_2J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJZ) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIJZ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jboolean arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJZ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jboolean arg3)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIJZ_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJZ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jboolean))(*(jintLong **)arg1)[arg0])(arg1, arg2, arg3);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIJZ_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJ_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJ_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jintArray arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJ_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jintArray arg3)
#endif
{
	jint *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIJ_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJ_3I_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jint *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIJ_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJ_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIJ_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJ_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlongArray arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJ_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jlong arg2, jlongArray arg3)
#endif
{
	jlong *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIJ_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJ_3J_FUNC);
#endif
	if (arg3) if ((lparg3 = (*env)->GetLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jlong, jlong *))(*(jintLong **)arg1)[arg0])(arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseLongArrayElements(env, arg3, lparg3, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIJ_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJ_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_CAUUID_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_CAUUID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#endif
{
	CAUUID _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_CAUUID_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getCAUUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, CAUUID *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setCAUUIDFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_CAUUID_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_CAUUID_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#endif
{
	CONTROLINFO _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getCONTROLINFOFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, CONTROLINFO *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setCONTROLINFOFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_CONTROLINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#endif
{
	FORMATETC _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getFORMATETCFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, FORMATETC *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setFORMATETCFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3)
#endif
{
	FORMATETC _arg2, *lparg2=NULL;
	STGMEDIUM _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getFORMATETCFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getSTGMEDIUMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, FORMATETC *, STGMEDIUM *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) setSTGMEDIUMFields(env, arg3, lparg3);
	if (arg2 && lparg2) setFORMATETCFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jboolean arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jboolean arg4)
#endif
{
	FORMATETC _arg2, *lparg2=NULL;
	STGMEDIUM _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z_FUNC);
#endif
	if (arg2) if ((lparg2 = getFORMATETCFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getSTGMEDIUMFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, FORMATETC *, STGMEDIUM *, jboolean))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, arg4);
fail:
	if (arg3 && lparg3) setSTGMEDIUMFields(env, arg3, lparg3);
	if (arg2 && lparg2) setFORMATETCFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_FORMATETC_2Lorg_eclipse_swt_internal_ole_win32_STGMEDIUM_2Z_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#endif
{
	GUID _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, GUID *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2IIII) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2IIII)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jint arg6)
#endif
{
	GUID _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2IIII_FUNC);
#endif
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, GUID *, jint, jint, jint, jint))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIII_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2IIII_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2III_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2III_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
#endif
{
	GUID _arg2, *lparg2=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2III_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, GUID *, jint, jint, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2III_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2III_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIJJ) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2IIJJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jint arg4, jlong arg5, jlong arg6)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2IIJJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jint arg4, jlong arg5, jlong arg6)
#endif
{
	GUID _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIJJ_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2IIJJ_FUNC);
#endif
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, GUID *, jint, jint, jlong, jlong))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, arg6);
fail:
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2IIJJ_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2IIJJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jobject arg4, jobject arg5)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jobject arg4, jobject arg5)
#endif
{
	GUID _arg2, *lparg2=NULL;
	OLECMD _arg4, *lparg4=NULL;
	OLECMDTEXT _arg5, *lparg5=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getOLECMDFields(env, arg4, &_arg4)) == NULL) goto fail;
	if (arg5) if ((lparg5 = getOLECMDTEXTFields(env, arg5, &_arg5)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, GUID *, jint, OLECMD *, OLECMDTEXT *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, lparg4, lparg5);
fail:
	if (arg5 && lparg5) setOLECMDTEXTFields(env, arg5, lparg5);
	if (arg4 && lparg4) setOLECMDFields(env, arg4, lparg4);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2ILorg_eclipse_swt_internal_ole_win32_OLECMD_2Lorg_eclipse_swt_internal_ole_win32_OLECMDTEXT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2JII_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2JII_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2JII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3, jint arg4, jint arg5, jintArray arg6)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2JII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3, jint arg4, jint arg5, jintArray arg6)
#endif
{
	GUID _arg2, *lparg2=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2JII_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2JII_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, GUID *, jlong, jint, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2JII_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2JII_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jintArray arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jintArray arg4)
#endif
{
	GUID _arg2, *lparg2=NULL;
	GUID _arg3, *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, GUID *, GUID *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jlongArray arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3, jlongArray arg4)
#endif
{
	GUID _arg2, *lparg2=NULL;
	GUID _arg3, *lparg3=NULL;
	jlong *lparg4=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getGUIDFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, GUID *, GUID *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseLongArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) setGUIDFields(env, arg3, lparg3);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2Lorg_eclipse_swt_internal_ole_win32_GUID_2_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jintArray arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jintArray arg3)
#endif
{
	GUID _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, GUID *, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlongArray arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlongArray arg3)
#endif
{
	GUID _arg2, *lparg2=NULL;
	jlong *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = getGUIDFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetLongArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, GUID *, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseLongArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) setGUIDFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_GUID_2_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_GUID_2_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_ole_win32_LICINFO_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_ole_win32_LICINFO_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#endif
{
	LICINFO _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_LICINFO_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getLICINFOFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, LICINFO *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setLICINFOFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_ole_win32_LICINFO_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_ole_win32_LICINFO_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_win32_MSG_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_win32_MSG_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#endif
{
	MSG _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_MSG_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getMSGFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, MSG *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setMSGFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_MSG_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_MSG_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_win32_POINT_2I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_win32_POINT_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_POINT_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_win32_POINT_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3)
#endif
{
	POINT _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#endif
	if (arg2) if ((lparg2 = getPOINTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, POINT *, jint))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setPOINTFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_POINT_2I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2)
#endif
{
	RECT _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, RECT *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2IZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jboolean arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2IZ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3, jboolean arg4)
#endif
{
	RECT _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2IZ_FUNC);
#endif
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, RECT *, jint, jboolean))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2IZ_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2IZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2JZ) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2JZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2JZ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3, jboolean arg4)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2JZ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3, jboolean arg4)
#endif
{
	RECT _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2JZ_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2JZ_FUNC);
#endif
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, RECT *, jlong, jboolean))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2JZ_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2JZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jobject arg3)
#endif
{
	RECT _arg2, *lparg2=NULL;
	RECT _arg3, *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#endif
	if (arg2) if ((lparg2 = getRECTFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg3) if ((lparg3 = getRECTFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, RECT *, RECT *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) setRECTFields(env, arg3, lparg3);
	if (arg2 && lparg2) setRECTFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_RECT_2Lorg_eclipse_swt_internal_win32_RECT_2_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jint arg3)
#endif
{
	SHDRAGIMAGE _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I_FUNC);
#endif
	if (arg2) if ((lparg2 = getSHDRAGIMAGEFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, SHDRAGIMAGE *, jint))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setSHDRAGIMAGEFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jobject arg2, jlong arg3)
#endif
{
	SHDRAGIMAGE _arg2, *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2J_FUNC);
#endif
	if (arg2) if ((lparg2 = getSHDRAGIMAGEFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, SHDRAGIMAGE *, jlong))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) setSHDRAGIMAGEFields(env, arg2, lparg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IILorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJLorg_eclipse_swt_internal_win32_SHDRAGIMAGE_2J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IIZ) && !defined(JNI64)) || (!defined(NO_VtblCall__IJZ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IIZ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jboolean arg2)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJZ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jboolean arg2)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IIZ_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJZ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jboolean))(*(jintLong **)arg1)[arg0])(arg1, arg2);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IIZ_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJZ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3C) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3C) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJ_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2)
#endif
{
	jchar *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__II_3C_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJ_3C_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *))(*(jintLong **)arg1)[arg0])(arg1, lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__II_3C_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJ_3C_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3CI) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3CI) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3CI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJ_3CI)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3)
#endif
{
	jchar *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__II_3CI_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJ_3CI_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jint))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__II_3CI_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJ_3CI_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3CIIII_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3CIIII_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3CIIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintArray arg7)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJ_3CIIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintArray arg7)
#endif
{
	jchar *lparg2=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__II_3CIIII_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJ_3CIIII_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jint, jint, jint, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__II_3CIIII_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJ_3CIIII_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3CIII_3I) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3CIII_3I) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3CIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJ_3CIII_3I)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
#endif
{
	jchar *lparg2=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__II_3CIII_3I_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJ_3CIII_3I_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jint, jint, jint, jint *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__II_3CIII_3I_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJ_3CIII_3I_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3CIII_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3CIII_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3CIII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jlongArray arg6)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJ_3CIII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jint arg3, jint arg4, jint arg5, jlongArray arg6)
#endif
{
	jchar *lparg2=NULL;
	jlong *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__II_3CIII_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJ_3CIII_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetLongArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jint, jint, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseLongArrayElements(env, arg6, lparg6, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__II_3CIII_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJ_3CIII_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3CJ) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3CJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3CJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jlong arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJ_3CJ)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jlong arg3)
#endif
{
	jchar *lparg2=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__II_3CJ_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJ_3CJ_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jlong))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__II_3CJ_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJ_3CJ_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3CJIII_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3CJIII_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3CJIII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jlong arg3, jint arg4, jint arg5, jint arg6, jlongArray arg7)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJ_3CJIII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jlong arg3, jint arg4, jint arg5, jint arg6, jlongArray arg7)
#endif
{
	jchar *lparg2=NULL;
	jlong *lparg7=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__II_3CJIII_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJ_3CJIII_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetLongArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jlong, jint, jint, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, arg6, lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseLongArrayElements(env, arg7, lparg7, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__II_3CJIII_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJ_3CJIII_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3CJII_3J) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3CJII_3J) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3CJII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jlong arg3, jint arg4, jint arg5, jlongArray arg6)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJ_3CJII_3J)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jlong arg3, jint arg4, jint arg5, jlongArray arg6)
#endif
{
	jchar *lparg2=NULL;
	jlong *lparg6=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__II_3CJII_3J_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJ_3CJII_3J_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetLongArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jlong, jint, jint, jlong *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, arg3, arg4, arg5, lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseLongArrayElements(env, arg6, lparg6, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__II_3CJII_3J_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJ_3CJII_3J_FUNC);
#endif
	return rc;
}
#endif

#if (!defined(NO_VtblCall__II_3C_3C) && !defined(JNI64)) || (!defined(NO_VtblCall__IJ_3C_3C) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__II_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jcharArray arg3)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJ_3C_3C)(JNIEnv *env, jclass that, jint arg0, jintLong arg1, jcharArray arg2, jcharArray arg3)
#endif
{
	jchar *lparg2=NULL;
	jchar *lparg3=NULL;
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__II_3C_3C_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJ_3C_3C_FUNC);
#endif
	if (arg2) if ((lparg2 = (*env)->GetCharArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetCharArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jintLong, jchar *, jchar *))(*(jintLong **)arg1)[arg0])(arg1, lparg2, lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseCharArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseCharArrayElements(env, arg2, lparg2, 0);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__II_3C_3C_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJ_3C_3C_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_VtblCall__IJIIIIJ
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJIIIIJ)
	(JNIEnv *env, jclass that, jint arg0, jlong arg1, jint arg2, jint arg3, jint arg4, jint arg5, jlong arg6)
{
	jint rc = 0;
	COM_NATIVE_ENTER(env, that, VtblCall__IJIIIIJ_FUNC);
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jlong, jint, jint, jint, jint, jlong))(*(jlong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6);
	COM_NATIVE_EXIT(env, that, VtblCall__IJIIIIJ_FUNC);
	return rc;
}
#endif

#if (!defined(NO_VtblCall__IJJIIIII) && !defined(JNI64)) || (!defined(NO_VtblCall__IJJIIIIJ) && defined(JNI64))
#ifndef JNI64
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJIIIII)(JNIEnv *env, jclass that, jint arg0, jlong arg1, jlong arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintLong arg7)
#else
JNIEXPORT jint JNICALL COM_NATIVE(VtblCall__IJJIIIIJ)(JNIEnv *env, jclass that, jint arg0, jlong arg1, jlong arg2, jint arg3, jint arg4, jint arg5, jint arg6, jintLong arg7)
#endif
{
	jint rc = 0;
#ifndef JNI64
	COM_NATIVE_ENTER(env, that, VtblCall__IJJIIIII_FUNC);
#else
	COM_NATIVE_ENTER(env, that, VtblCall__IJJIIIIJ_FUNC);
#endif
	rc = (jint)((jint (STDMETHODCALLTYPE *)(jlong, jlong, jint, jint, jint, jint, jintLong))(*(jlong **)arg1)[arg0])(arg1, arg2, arg3, arg4, arg5, arg6, arg7);
#ifndef JNI64
	COM_NATIVE_EXIT(env, that, VtblCall__IJJIIIII_FUNC);
#else
	COM_NATIVE_EXIT(env, that, VtblCall__IJJIIIIJ_FUNC);
#endif
	return rc;
}
#endif

#ifndef NO_WriteClassStg
JNIEXPORT jint JNICALL COM_NATIVE(WriteClassStg)
	(JNIEnv *env, jclass that, jintLong arg0, jobject arg1)
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

