/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "cde_structs.h"
#include "cde_stats.h"

#ifndef CDE_NATIVE
#define CDE_NATIVE(func) Java_org_eclipse_swt_internal_cde_CDE_##func
#endif

#ifndef NO_DtActionArg_1sizeof
JNIEXPORT jint JNICALL CDE_NATIVE(DtActionArg_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	CDE_NATIVE_ENTER(env, that, DtActionArg_1sizeof_FUNC);
	rc = (jint)DtActionArg_sizeof();
	CDE_NATIVE_EXIT(env, that, DtActionArg_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO__1DtActionInvoke
JNIEXPORT jlong JNICALL CDE_NATIVE(_1DtActionInvoke)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1, jobject arg2, jint arg3, jbyteArray arg4, jbyteArray arg5, jbyteArray arg6, jint arg7, jintLong arg8, jintLong arg9)
{
	jbyte *lparg1=NULL;
	DtActionArg _arg2, *lparg2=NULL;
	jbyte *lparg4=NULL;
	jbyte *lparg5=NULL;
	jbyte *lparg6=NULL;
	jlong rc = 0;
	CDE_NATIVE_ENTER(env, that, _1DtActionInvoke_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getDtActionArgFields(env, arg2, &_arg2)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetByteArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetByteArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jlong)DtActionInvoke((Widget)arg0, (char *)lparg1, lparg2, arg3, (char *)lparg4, (char *)lparg5, (char *)lparg6, arg7, (DtActionCallbackProc)arg8, (XtPointer)arg9);
fail:
	if (arg6 && lparg6) (*env)->ReleaseByteArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseByteArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) setDtActionArgFields(env, arg2, lparg2);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	CDE_NATIVE_EXIT(env, that, _1DtActionInvoke_FUNC);
	return rc;
}
#endif

#ifndef NO__1DtAppInitialize
JNIEXPORT jboolean JNICALL CDE_NATIVE(_1DtAppInitialize)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jbyteArray arg3, jbyteArray arg4)
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jboolean rc = 0;
	CDE_NATIVE_ENTER(env, that, _1DtAppInitialize_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jboolean)DtAppInitialize((XtAppContext)arg0, (Display *)arg1, (Widget)arg2, (char *)lparg3, (char *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	CDE_NATIVE_EXIT(env, that, _1DtAppInitialize_FUNC);
	return rc;
}
#endif

#ifndef NO__1DtDbLoad
JNIEXPORT void JNICALL CDE_NATIVE(_1DtDbLoad)
	(JNIEnv *env, jclass that)
{
	CDE_NATIVE_ENTER(env, that, _1DtDbLoad_FUNC);
	DtDbLoad();
	CDE_NATIVE_EXIT(env, that, _1DtDbLoad_FUNC);
}
#endif

#ifndef NO__1DtDtsDataTypeIsAction
JNIEXPORT jboolean JNICALL CDE_NATIVE(_1DtDtsDataTypeIsAction)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jboolean rc = 0;
	CDE_NATIVE_ENTER(env, that, _1DtDtsDataTypeIsAction_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jboolean)DtDtsDataTypeIsAction((char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	CDE_NATIVE_EXIT(env, that, _1DtDtsDataTypeIsAction_FUNC);
	return rc;
}
#endif

#ifndef NO__1DtDtsDataTypeNames
JNIEXPORT jintLong JNICALL CDE_NATIVE(_1DtDtsDataTypeNames)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	CDE_NATIVE_ENTER(env, that, _1DtDtsDataTypeNames_FUNC);
	rc = (jintLong)DtDtsDataTypeNames();
	CDE_NATIVE_EXIT(env, that, _1DtDtsDataTypeNames_FUNC);
	return rc;
}
#endif

#ifndef NO__1DtDtsDataTypeToAttributeValue
JNIEXPORT jintLong JNICALL CDE_NATIVE(_1DtDtsDataTypeToAttributeValue)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jbyteArray arg2)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jintLong rc = 0;
	CDE_NATIVE_ENTER(env, that, _1DtDtsDataTypeToAttributeValue_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jintLong)DtDtsDataTypeToAttributeValue((char *)lparg0, (char *)lparg1, (char *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	CDE_NATIVE_EXIT(env, that, _1DtDtsDataTypeToAttributeValue_FUNC);
	return rc;
}
#endif

#ifndef NO__1DtDtsFileToDataType
JNIEXPORT jintLong JNICALL CDE_NATIVE(_1DtDtsFileToDataType)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	CDE_NATIVE_ENTER(env, that, _1DtDtsFileToDataType_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)DtDtsFileToDataType((char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	CDE_NATIVE_EXIT(env, that, _1DtDtsFileToDataType_FUNC);
	return rc;
}
#endif

#ifndef NO__1DtDtsFreeAttributeValue
JNIEXPORT void JNICALL CDE_NATIVE(_1DtDtsFreeAttributeValue)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	CDE_NATIVE_ENTER(env, that, _1DtDtsFreeAttributeValue_FUNC);
	DtDtsFreeAttributeValue((char *)arg0);
	CDE_NATIVE_EXIT(env, that, _1DtDtsFreeAttributeValue_FUNC);
}
#endif

#ifndef NO__1DtDtsFreeDataType
JNIEXPORT void JNICALL CDE_NATIVE(_1DtDtsFreeDataType)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	CDE_NATIVE_ENTER(env, that, _1DtDtsFreeDataType_FUNC);
	DtDtsFreeDataType((char *)arg0);
	CDE_NATIVE_EXIT(env, that, _1DtDtsFreeDataType_FUNC);
}
#endif

#ifndef NO__1DtDtsFreeDataTypeNames
JNIEXPORT void JNICALL CDE_NATIVE(_1DtDtsFreeDataTypeNames)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	CDE_NATIVE_ENTER(env, that, _1DtDtsFreeDataTypeNames_FUNC);
	DtDtsFreeDataTypeNames((char **)arg0);
	CDE_NATIVE_EXIT(env, that, _1DtDtsFreeDataTypeNames_FUNC);
}
#endif

#ifndef NO__1XtAppCreateShell
JNIEXPORT jintLong JNICALL CDE_NATIVE(_1XtAppCreateShell)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jintLong arg2, jintLong arg3, jintLongArray arg4, jint arg5)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jintLong *lparg4=NULL;
	jintLong rc = 0;
	CDE_NATIVE_ENTER(env, that, _1XtAppCreateShell_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntLongArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jintLong)XtAppCreateShell((String)lparg0, (String)lparg1, (WidgetClass)arg2, (Display *)arg3, (ArgList)lparg4, arg5);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntLongArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	CDE_NATIVE_EXIT(env, that, _1XtAppCreateShell_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtCreateApplicationContext
JNIEXPORT jintLong JNICALL CDE_NATIVE(_1XtCreateApplicationContext)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	CDE_NATIVE_ENTER(env, that, _1XtCreateApplicationContext_FUNC);
	rc = (jintLong)XtCreateApplicationContext();
	CDE_NATIVE_EXIT(env, that, _1XtCreateApplicationContext_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtDisplayInitialize
JNIEXPORT void JNICALL CDE_NATIVE(_1XtDisplayInitialize)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jbyteArray arg2, jbyteArray arg3, jintLong arg4, jint arg5, jintLongArray arg6, jint arg7)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jintLong *lparg6=NULL;
	CDE_NATIVE_ENTER(env, that, _1XtDisplayInitialize_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntLongArrayElements(env, arg6, NULL)) == NULL) goto fail;
	XtDisplayInitialize((XtAppContext)arg0, (Display *)arg1, (String)lparg2, (String)lparg3, (XrmOptionDescRec *)arg4, (Cardinal)arg5, (int *)lparg6, (String *)arg7);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntLongArrayElements(env, arg6, lparg6, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	CDE_NATIVE_EXIT(env, that, _1XtDisplayInitialize_FUNC);
}
#endif

#ifndef NO__1XtRealizeWidget
JNIEXPORT void JNICALL CDE_NATIVE(_1XtRealizeWidget)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	CDE_NATIVE_ENTER(env, that, _1XtRealizeWidget_FUNC);
	XtRealizeWidget((Widget)arg0);
	CDE_NATIVE_EXIT(env, that, _1XtRealizeWidget_FUNC);
}
#endif

#ifndef NO__1XtResizeWidget
JNIEXPORT void JNICALL CDE_NATIVE(_1XtResizeWidget)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jint arg2, jint arg3)
{
	CDE_NATIVE_ENTER(env, that, _1XtResizeWidget_FUNC);
	XtResizeWidget((Widget)arg0, arg1, arg2, arg3);
	CDE_NATIVE_EXIT(env, that, _1XtResizeWidget_FUNC);
}
#endif

#ifndef NO__1XtSetMappedWhenManaged
JNIEXPORT void JNICALL CDE_NATIVE(_1XtSetMappedWhenManaged)
	(JNIEnv *env, jclass that, jintLong arg0, jboolean arg1)
{
	CDE_NATIVE_ENTER(env, that, _1XtSetMappedWhenManaged_FUNC);
	XtSetMappedWhenManaged((Widget)arg0, arg1);
	CDE_NATIVE_EXIT(env, that, _1XtSetMappedWhenManaged_FUNC);
}
#endif

#ifndef NO__1XtToolkitInitialize
JNIEXPORT void JNICALL CDE_NATIVE(_1XtToolkitInitialize)
	(JNIEnv *env, jclass that)
{
	CDE_NATIVE_ENTER(env, that, _1XtToolkitInitialize_FUNC);
	XtToolkitInitialize();
	CDE_NATIVE_EXIT(env, that, _1XtToolkitInitialize_FUNC);
}
#endif

#ifndef NO__1topLevelShellWidgetClass
JNIEXPORT jintLong JNICALL CDE_NATIVE(_1topLevelShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jintLong rc = 0;
	CDE_NATIVE_ENTER(env, that, _1topLevelShellWidgetClass_FUNC);
	rc = (jintLong)topLevelShellWidgetClass;
	CDE_NATIVE_EXIT(env, that, _1topLevelShellWidgetClass_FUNC);
	return rc;
}
#endif

