/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2003, 2004 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */


/* Defines the entry point for the DLL application. */

#include "nsCRT.h"
#include "nsEmbedAPI.h"	
#include "prmem.h"
#include "prenv.h"
#include "nsString.h"
#include "nsEnumeratorUtils.h"
#include "nsIInputStream.h"
#include "nsRect.h"
#include "jni.h"

extern "C" {

#define XPCOM_NATIVE(func) Java_org_eclipse_swt_internal_mozilla_XPCOM_##func

/* used to cast Vtabl entries */
 
typedef NS_CALLBACK_(jint, P_OLE_FN_16S)(jint,jint, jint, jint, jint, jint,jint,jint, jint, jint, jint, jint, jint, jint, jshort, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_11)(jint, jint, jint, jint, jint, jint, jint, jint, jint, jint, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_10)(jint, jint, jint, jint, jint, jint, jint, jint, jint, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_9S)(jint, jint, jint, jint, jint, jint, jint, jint, jshort);
typedef NS_CALLBACK_(jint, P_OLE_FN_9) (jint, jint, jint, jint, jint, jint, jint, jint, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_8) (jint, jint, jint, jint, jint, jint, jint, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_7) (jint, jint, jint, jint, jint, jint, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_6SSS) (jint, jint, jshort, jshort, jshort, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_6) (jint, jint, jint, jint, jint, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_5) (jint, jint, jint, jint, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_4) (jint, jint, jint, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_3) (jint, jint, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_3S)(jint, jshort,jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_3SF) (jint, jshort, jfloat);
typedef NS_CALLBACK_(void, P_OLE_FNNORET_3)(jint, jint, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_2L)(jint, jlong);
typedef NS_CALLBACK_(jint, P_OLE_FN_2F)(jint, jfloat);
typedef NS_CALLBACK_(jint, P_OLE_FN_2D)(jint, jdouble);
typedef NS_CALLBACK_(jint, P_OLE_FN_2) (jint, jint);
typedef NS_CALLBACK_(void, P_OLE_FNNORET_2)(jint, jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_1) (jint);
typedef NS_CALLBACK_(jint, P_OLE_FN_0) (void);

typedef struct nsID_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID m0, m1, m2, m3;
} nsID_FID_CACHE;

nsID_FID_CACHE nsIDFc;

void cachensIDFields(JNIEnv *env, jobject lpObject)
{
	if (nsIDFc.cached) return;
	nsIDFc.clazz = env->GetObjectClass(lpObject);
	nsIDFc.m0 = env->GetFieldID(nsIDFc.clazz, "m0", "I");
	nsIDFc.m1 = env->GetFieldID(nsIDFc.clazz, "m1", "S");
	nsIDFc.m2 = env->GetFieldID(nsIDFc.clazz, "m2", "S");
	nsIDFc.m3 = env->GetFieldID(nsIDFc.clazz, "m3", "[B");
	nsIDFc.cached = 1;
}

nsID *getnsIDFields(JNIEnv *env, jobject lpObject, nsID *lpStruct)
{
	if (!nsIDFc.cached) cachensIDFields(env, lpObject);
	lpStruct->m0 = env->GetIntField(lpObject, nsIDFc.m0);
	lpStruct->m1 = env->GetShortField(lpObject, nsIDFc.m1);
	lpStruct->m2 = env->GetShortField(lpObject, nsIDFc.m2);
	{
	jbyteArray lpObject1 = (jbyteArray)env->GetObjectField(lpObject, nsIDFc.m3);
	env->GetByteArrayRegion(lpObject1, 0, sizeof(lpStruct->m3), (jbyte *)lpStruct->m3);
	}
	return lpStruct;
}

void setnsIDFields(JNIEnv *env, jobject lpObject, nsID *lpStruct)
{
	if (!nsIDFc.cached) cachensIDFields(env, lpObject);
	env->SetIntField(lpObject, nsIDFc.m0, (jint)lpStruct->m0);
	env->SetShortField(lpObject, nsIDFc.m1, (jshort)lpStruct->m1);
	env->SetShortField(lpObject, nsIDFc.m2, (jshort)lpStruct->m2);
	{
	jbyteArray lpObject1 = (jbyteArray)env->GetObjectField(lpObject, nsIDFc.m3);
	env->SetByteArrayRegion(lpObject1, 0, sizeof(lpStruct->m3), (jbyte *)lpStruct->m3);
	}
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(NS_1InitEmbedding)
	(JNIEnv *, jclass, jint arg0, jint arg1)
{
	nsresult rv = NS_InitEmbedding((nsILocalFile *)arg0, (nsIDirectoryServiceProvider *)arg1);
	return rv;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(NS_1TermEmbedding)
	(JNIEnv *, jclass)
{
	return NS_TermEmbedding();
}

JNIEXPORT void JNICALL XPCOM_NATIVE(nsID_1delete)
	(JNIEnv *, jclass, jint arg0)
{
	delete (nsID*)arg0;
}

JNIEXPORT jboolean JNICALL XPCOM_NATIVE(nsID_1Equals)
	(JNIEnv *env, jclass, jint arg0, jint arg1)
{
	nsID *lparg0 = NULL;
	nsID *lparg1 = NULL;
	if (arg0 != 0)
		lparg0 = (nsID*)arg0;
	if (arg1 != 0)
		lparg1 = (nsID*)arg1;
	return lparg0->Equals((nsID&)(*lparg1));	
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(nsID_1new)
	(JNIEnv *env, jclass)
{
	return (jint)new nsID();
}

JNIEXPORT jboolean JNICALL XPCOM_NATIVE(nsID_1Parse)
	(JNIEnv *env, jclass, jint arg0, jstring arg1)
{
	nsID *lparg0 = NULL;
	const char *lparg1 = NULL;
	if (arg0 != 0)
		lparg0 = (nsID*)arg0;
	if (arg1 != 0)
		lparg1 = (const char *)env->GetStringUTFChars(arg1, NULL);
	return lparg0->Parse(lparg1);	
}

JNIEXPORT void JNICALL XPCOM_NATIVE(nsCString_1delete)
	(JNIEnv *, jclass, jint arg0)
{
	delete (nsCString *)arg0;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(nsCString_1get)
	(JNIEnv *env, jclass, jint arg0)
{
	nsCString *lparg0 = NULL;
	if (arg0 != 0)
		lparg0 = (nsCString *)arg0;
	return (jint)lparg0->get();
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(nsCString_1Length)
	(JNIEnv *env, jclass, jint arg0)
{
	nsCString *lparg0 = NULL;
	if (arg0 != 0)
		lparg0 = (nsCString *)arg0;
	return (jint)lparg0->Length();
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(nsCString_1new__)
	(JNIEnv *env, jclass)
{
	return (jint)new nsCString();
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(nsCString_1new___3BI)
	(JNIEnv *env, jclass, jbyteArray arg0, jint length)
{
	jbyte *lparg0=NULL;
	jint rc;
	if (arg0) lparg0 = env->GetByteArrayElements(arg0, NULL);
	rc = (jint)new nsCString((const char *)lparg0, length);
	if (arg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	return rc;
}

JNIEXPORT void JNICALL XPCOM_NATIVE(nsRect_1delete)
	(JNIEnv *, jclass, jint arg0)
{
	delete (nsRect*)arg0;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(nsRect_1new)
	(JNIEnv *env, jclass, int arg0, int arg1, int arg2, int arg3)
{
	return (jint)new nsRect(arg0, arg1, arg2, arg3);
}

JNIEXPORT void JNICALL XPCOM_NATIVE(nsString_1delete)
	(JNIEnv *, jclass, jint arg0)
{
	delete (nsString *)arg0;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(nsString_1get)
	(JNIEnv *env, jclass, jint arg0)
{
	nsString *lparg0 = NULL;
	if (arg0 != 0)
		lparg0 = (nsString *)arg0;
	return (jint)lparg0->get();
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(nsString_1Length)
	(JNIEnv *env, jclass, jint arg0)
{
	nsString *lparg0 = NULL;
	if (arg0 != 0)
		lparg0 = (nsString *)arg0;
	return (jint)lparg0->Length();
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(nsString_1new__)
	(JNIEnv *env, jclass)
{
	return (jint)new nsString();
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(nsString_1new___3C)
	(JNIEnv *env, jclass, jcharArray arg0)
{
	jchar *lparg0=NULL;
	jint rc;
	if (arg0) lparg0 = env->GetCharArrayElements(arg0, NULL);
	rc = (jint)new nsString(lparg0);
	if (arg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
	return rc;
}

JNIEXPORT jboolean JNICALL XPCOM_NATIVE(nsString_1Equals)
	(JNIEnv *env, jclass, jint arg0, jint arg1)
{
	nsString *lparg0 = NULL;
	nsString *lparg1 = NULL;
	if (arg0 != 0)
		lparg0 = (nsString*)arg0;
	if (arg1 != 0)
		lparg1 = (nsString*)arg1;
	return lparg0->Equals(*lparg1);
}

JNIEXPORT void JNICALL XPCOM_NATIVE(nsString_1AssignWithConversion)
	(JNIEnv *env, jclass, jint arg0, jbyteArray arg1)
{
	nsString *lparg0 = NULL;
	jbyte *lparg1 = NULL;
	if (arg0) lparg0 = (nsString*)arg0;
	if (arg1) lparg1 = env->GetByteArrayElements(arg1, NULL);
	lparg0->AssignWithConversion((const char *)lparg1);
	if (arg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
}

JNIEXPORT jstring JNICALL XPCOM_NATIVE(PR_1GetEnv)
	(JNIEnv *env, jclass, jstring arg0)
{
	const char* name = (const char *)env->GetStringUTFChars(arg0,NULL);
	char* var = PR_GetEnv(name);
	env->ReleaseStringUTFChars(arg0, name);
	if (NULL == var)
		return NULL;

	return env->NewStringUTF(var);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(NS_1NewLocalFile)
 	(JNIEnv *env, jclass, jint arg0, jboolean arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	if (arg2) lparg2 = env->GetIntArrayElements(arg2, NULL);
    rc = NS_NewLocalFile((nsAString &)(*(nsAString *)arg0), arg1, (nsILocalFile**)lparg2);
	if (arg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(NS_1NewSingletonEnumerator)
  (JNIEnv *env, jclass, jint nsFile, jintArray arg1)
{
	nsISimpleEnumerator *pEnum = NULL;
	nsresult rc = NS_NewSingletonEnumerator(&pEnum,(nsISupports *)nsFile);
  
	if (NS_SUCCEEDED(rc)) {
		jint *arg11 = NULL; 
		if (arg1)
			arg11 = env->GetIntArrayElements(arg1, NULL);

		arg11[0] = (jint)pEnum; 

		if (arg1)
			env->ReleaseIntArrayElements(arg1, arg11, 0);
	}

	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(NS_1GetComponentManager)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	if (arg0) lparg0 = env->GetIntArrayElements(arg0, NULL);
	rc = (jint)NS_GetComponentManager((nsIComponentManager**)lparg0);
	if (arg0) env->ReleaseIntArrayElements(arg0, lparg0, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(NS_1GetServiceManager)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	if (arg0) lparg0 = env->GetIntArrayElements(arg0, NULL);
	rc = (jint)NS_GetServiceManager((nsIServiceManager**)lparg0);
	if (arg0) env->ReleaseIntArrayElements(arg0, lparg0, 0);
	return rc;
}

JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__ILorg_eclipse_swt_internal_mozilla_nsID_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	nsID _arg1, *lparg1=NULL;
	if (arg1) lparg1 = getnsIDFields(env, arg1, &_arg1);
	memmove((void*)arg0, lparg1, arg2);
	if (arg1) setnsIDFields(env, arg1, lparg1);
}

JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__I_3II)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	if (arg1) lparg1 = env->GetIntArrayElements(arg1, NULL);
	memmove((void*)arg0, lparg1, arg2);
	if (arg1) env->ReleaseIntArrayElements(arg1, lparg1, 0);
}

JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__Lorg_eclipse_swt_internal_mozilla_nsID_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	nsID _arg0, *lparg0=NULL;
	if (arg0) lparg0 = getnsIDFields(env, arg0, &_arg0);
	memmove(lparg0, (void*)arg1, arg2);
	if (arg0) setnsIDFields(env, arg0, lparg0);
}

JNIEXPORT void JNICALL XPCOM_NATIVE(memmove___3III)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	if (arg0) lparg0 = env->GetIntArrayElements(arg0, NULL);
	memmove(lparg0, (void*)arg1, arg2);
	if (arg0) env->ReleaseIntArrayElements(arg0, lparg0, 0);
}

JNIEXPORT void JNICALL XPCOM_NATIVE(memmove___3BII)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	if (arg0) lparg0 = env->GetByteArrayElements(arg0, NULL);
	memmove(lparg0, (void*)arg1, arg2);
	if (arg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
}

JNIEXPORT void JNICALL XPCOM_NATIVE(memmove___3CII)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;
	if (arg0) lparg0 = env->GetCharArrayElements(arg0, NULL);
	memmove(lparg0, (void*)arg1, arg2);
	if (arg0) env->ReleaseCharArrayElements(arg0, lparg0, 0);
}

JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__I_3BI)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	if (arg1) lparg1 = env->GetByteArrayElements(arg1, NULL);
	memmove((void*)arg0, lparg1, arg2);
	if (arg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
}

JNIEXPORT void JNICALL XPCOM_NATIVE(memmove__I_3CI)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	if (arg1) lparg1 = env->GetCharArrayElements(arg1, NULL);
	memmove((void*)arg0, lparg1, arg2);
	if (arg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
}

JNIEXPORT void JNICALL XPCOM_NATIVE(memmove___3B_3CI)
	(JNIEnv *env, jclass that, jbyteArray arg0, jcharArray arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	jchar *lparg1=NULL;
	if (arg0) lparg0 = env->GetByteArrayElements(arg0, NULL);
	if (arg1) lparg1 = env->GetCharArrayElements(arg1, NULL);
	memmove((void *)lparg0, lparg1, arg2);
	if (arg1) env->ReleaseCharArrayElements(arg1, lparg1, 0);
	if (arg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(PR_1Malloc)
	(JNIEnv *, jclass, jint Length)
{
	return (jint)PR_Malloc(Length);
}

JNIEXPORT void JNICALL XPCOM_NATIVE(PR_1Free)
	(JNIEnv *, jclass, jint ptr)
{
	PR_Free((void *)ptr);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(nsWriteSegmentFun)
	(JNIEnv *env, jclass that, jint ptr, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jint arg4, jintArray arg5)
{
	jbyte *lparg2=NULL;
	jint *lparg5=NULL;
	jint rc;
	if (arg2) lparg2 = env->GetByteArrayElements(arg2, NULL);
	if (arg5) lparg5 = env->GetIntArrayElements(arg5, NULL);

	/* custom */	
	nsWriteSegmentFun aWriter = (nsWriteSegmentFun)ptr;
	rc = aWriter((nsIInputStream *)arg0, (void *)arg1, (const char *)lparg2, arg3, arg4, (PRUint32 *)lparg5);
	
	if (arg5) env->ReleaseIntArrayElements(arg5, lparg5, 0);
	if (arg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jintArray arg1)
{
    jint *arg11=NULL;
    nsID nsid;
	nsID *arg01 = NULL;
       
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

    if (arg0) {
       getnsIDFields(env, arg0, &nsid);
	   arg01 = &nsid;
    }
    
    if (arg1)
        arg11 = env->GetIntArrayElements(arg1, NULL);

    jint rc = fn(ppVtbl, (jint)arg01, (jint)arg11);

    if (arg1)
        env->ReleaseIntArrayElements(arg1, arg11, 0);

    if (arg01) 
        setnsIDFields(env, arg0, arg01);
    
    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jobject arg1, jintArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	nsID nsid;
	nsID *arg11 = NULL;
    if (arg1) {
       getnsIDFields(env, arg1, &nsid);
	   arg11 = &nsid;
    }
    
	jint *arg21=NULL;
    if (arg2)
        arg21 = env->GetIntArrayElements(arg2, NULL);

    jint rc = fn(ppVtbl, arg0, (jint)arg11, (jint)arg21);

    if (arg2)
        env->ReleaseIntArrayElements(arg2, arg21, 0);

    if (arg11) 
        setnsIDFields(env, arg1, arg11);
    
    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2_3B_3BI)
    (JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jbyteArray arg1, jbyteArray arg2, jint arg3)
{
	P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

	nsID nsid;
	nsID *arg01 = NULL;
    if (arg0) {
       getnsIDFields(env, arg0, &nsid);
	   arg01 = &nsid;
    }

	jbyte *arg11 = NULL;
    if (arg1)
        arg11 = env->GetByteArrayElements(arg1, NULL);

	jbyte *arg21 = NULL;
    if (arg2)
        arg21 = env->GetByteArrayElements(arg2, NULL);

	jint rc = fn(ppVtbl, (jint) arg01, (jint)arg11, (jint)arg21, arg3);

	if (arg01) 
        setnsIDFields(env, arg0, arg01);

	if (arg11)
        env->ReleaseByteArrayElements(arg1, arg11, 0);

	if (arg21)
        env->ReleaseByteArrayElements(arg2, arg21, 0);
    
    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl)
{
    P_OLE_FN_1 fn = (P_OLE_FN_1)(*(int **)ppVtbl)[fnNumber];

    return fn(ppVtbl); 
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIII)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

    return fn(ppVtbl, arg0, arg1); 
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jintArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jint *arg11 = NULL;
    if (arg1)
        arg11 = env->GetIntArrayElements(arg1, NULL);

    jint rc = fn(ppVtbl, arg0, (jint) arg11); 

    if (arg11)
        env->ReleaseIntArrayElements(arg1, arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jcharArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

    jint rc = fn(ppVtbl, arg0, (jint) arg11); 

	if (arg1)
        env->ReleaseCharArrayElements(arg1, arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3B)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jbyteArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg11 = NULL;
    if (arg1)
        arg11 = env->GetByteArrayElements(arg1, NULL);

    jint rc = fn(ppVtbl, arg0, (jint) arg11); 

    if (arg11)
        env->ReleaseByteArrayElements(arg1, arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jintArray arg0)
{
    P_OLE_FN_2 fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

	jint *arg01 = NULL;
    if (arg0)
        arg01 = env->GetIntArrayElements(arg0, NULL);

    jint rc = fn(ppVtbl, (jint) arg01); 

    if (arg0)
        env->ReleaseIntArrayElements(arg0, arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0)
{
    P_OLE_FN_2 fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

    return fn(ppVtbl, arg0); 
}

JNIEXPORT void JNICALL XPCOM_NATIVE(VtblCallNoRet__III)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0)
{
    P_OLE_FNNORET_2 fn = (P_OLE_FNNORET_2)(*(int **)ppVtbl)[fnNumber];

    fn(ppVtbl, arg0); 
}

JNIEXPORT void JNICALL XPCOM_NATIVE(VtblCallNoRet__IIII)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jint arg1)
{
    P_OLE_FNNORET_3 fn = (P_OLE_FNNORET_3)(*(int **)ppVtbl)[fnNumber];

    fn(ppVtbl, arg0, arg1); 
}

JNIEXPORT void JNICALL XPCOM_NATIVE(VtblCallNoRet__II_3I_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jintArray arg0, jintArray arg1)
{
	P_OLE_FNNORET_3 fn = (P_OLE_FNNORET_3)(*(int **)ppVtbl)[fnNumber];
	jint *lparg0 = NULL, *lparg1 = NULL;
	if (arg0) lparg0 = env->GetIntArrayElements(arg0, NULL);
	if (arg1) lparg1 = env->GetIntArrayElements(arg1, NULL);
    fn(ppVtbl, (jint)lparg0, (jint)lparg1);
    if (arg0) env->ReleaseIntArrayElements(arg0, lparg0, 0);
    if (arg1) env->ReleaseIntArrayElements(arg1, lparg1, 0);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0)
{
    P_OLE_FN_2 fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0) 
		arg01 = env->GetCharArrayElements(arg0, NULL);
     
    jint rc = fn(ppVtbl, (jint) arg01); 

    if (arg0)
        env->ReleaseCharArrayElements(arg0, arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbyteArray arg0)
{
    P_OLE_FN_2 fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg01 = NULL;
    if (arg0)
        arg01 = env->GetByteArrayElements(arg0, NULL);

    jint rc = fn(ppVtbl, (jint) arg01); 

    if (arg01)
        env->ReleaseByteArrayElements(arg0, arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jobject arg1)
{
	P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	nsID nsid;
	nsID *arg11 = NULL;
     
    if (arg1) {
        getnsIDFields(env, arg1, &nsid);
		arg11 = &nsid;
    }

    jint rc = fn(ppVtbl, arg0, (jint) arg11); 

    if (arg11) 
        setnsIDFields(env, arg1, arg11);
    
	return rc;
}
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3CI)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jint arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0) 
		arg01 = env->GetCharArrayElements(arg0, NULL);
	
    jint rc = fn(ppVtbl, (jint) arg01,arg1); 

    if (arg0)
        env->ReleaseCharArrayElements(arg0, arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BI)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbyteArray arg0, jint arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg01 = NULL;
    if (arg0)
        arg01 = env->GetByteArrayElements(arg0, NULL);

    jint rc = fn(ppVtbl, (jint) arg01,arg1); 

    if (arg01)
        env->ReleaseByteArrayElements(arg0, arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIIIII)
	(JNIEnv *, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
    P_OLE_FN_7 fn = (P_OLE_FN_7)(*(int **)ppVtbl)[fnNumber];

    return fn(ppVtbl, arg0, arg1, arg2, arg3, arg4, arg5);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3I_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jintArray arg0, jintArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jint *arg01 = NULL;
    if (arg0)
        arg01 = env->GetIntArrayElements(arg0, NULL);

	jint *arg11 = NULL;
    if (arg1)
        arg11 = env->GetIntArrayElements(arg1, NULL);

    jint rc = fn(ppVtbl, (jint) arg01,(jint) arg11); 

    if (arg01)
        env->ReleaseIntArrayElements(arg0, arg01, 0);

    if (arg11)
        env->ReleaseIntArrayElements(arg1, arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIZ)
	(JNIEnv *, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jboolean arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

    return fn(ppVtbl, arg0, arg1, arg2);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIIIZ)
	(JNIEnv *, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jint arg3, jboolean arg4)
{
    P_OLE_FN_6 fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];

    return fn(ppVtbl, arg0, arg1, arg2, arg3, arg4);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3I_3I_3I_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jintArray arg0, jintArray arg1, jintArray arg2, jintArray arg3)
{
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

	jint *arg01 = NULL;
    if (arg0)
        arg01 = env->GetIntArrayElements(arg0, NULL);

	jint *arg11 = NULL;
    if (arg1)
        arg11 = env->GetIntArrayElements(arg1, NULL);

	jint *arg21 = NULL;
    if (arg2)
        arg21 = env->GetIntArrayElements(arg2, NULL);

	jint *arg31 = NULL;
    if (arg3)
        arg31 = env->GetIntArrayElements(arg3, NULL);

    jint rc = fn(ppVtbl, (jint) arg01,(jint) arg11, (jint) arg21, (jint) arg31); 

    if (arg01)
        env->ReleaseIntArrayElements(arg0, arg01, 0);

    if (arg11)
        env->ReleaseIntArrayElements(arg1, arg11, 0);

	if (arg21)
        env->ReleaseIntArrayElements(arg2, arg21, 0);

	if (arg31)
        env->ReleaseIntArrayElements(arg3, arg31, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIZ)
	(JNIEnv *, jclass, jint fnNumber, jint ppVtbl, jboolean arg0)
{
    P_OLE_FN_2 fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

    return fn(ppVtbl, arg0);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3Z)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbooleanArray arg0)
{
    P_OLE_FN_2 fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

	jboolean *arg01 = NULL;
    if (arg0)
        arg01 = env->GetBooleanArrayElements(arg0, NULL);

	jint rc = fn(ppVtbl, (jint) arg01); 

    if (arg01)
        env->ReleaseBooleanArrayElements(arg0, arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3Z)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jbooleanArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0) 
		arg01 = env->GetCharArrayElements(arg0, NULL);

	jboolean *arg11 = NULL;
    if (arg1)
        arg11 = env->GetBooleanArrayElements(arg1, NULL);

	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11); 

	if (arg0)
        env->ReleaseCharArrayElements(arg0, arg01, 0);

    if (arg11)
        env->ReleaseBooleanArrayElements(arg1, arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3Z)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbyteArray arg0, jbooleanArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg01 = NULL;
    if (arg0)
        arg01 = env->GetByteArrayElements(arg0, NULL);

	jboolean *arg11 = NULL;
    if (arg1)
        arg11 = env->GetBooleanArrayElements(arg1, NULL);

	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11); 

	if (arg01)
        env->ReleaseByteArrayElements(arg0, arg01, 0);

    if (arg11)
        env->ReleaseBooleanArrayElements(arg1, arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3CI_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jint arg1, jintArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0) 
		arg01 = env->GetCharArrayElements(arg0, NULL);
    
	jint *arg21 = NULL;
    if (arg2)
        arg21 = env->GetIntArrayElements(arg2, NULL);

	jint rc = fn(ppVtbl, (jint) arg01, arg1, (jint) arg21); 

	if (arg0)
        env->ReleaseCharArrayElements(arg0, arg01, 0);

    if (arg21)
        env->ReleaseIntArrayElements(arg2, arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3Z_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jbooleanArray arg1, jintArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0) 
		arg01 = env->GetCharArrayElements(arg0, NULL);

	jboolean *arg11 = NULL;
    if (arg1)
        arg11 = env->GetBooleanArrayElements(arg1, NULL);

	jint *arg21 = NULL;
    if (arg2)
        arg21 = env->GetIntArrayElements(arg2, NULL);

	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21); 

	if (arg0)
        env->ReleaseCharArrayElements(arg0, arg01, 0);

	if (arg11)
        env->ReleaseBooleanArrayElements(arg1, arg11, 0);

    if (arg21)
        env->ReleaseIntArrayElements(arg2, arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIII_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jintArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jint *arg21 = NULL;
    if (arg2)
        arg21 = env->GetIntArrayElements(arg2, NULL);

	jint rc = fn(ppVtbl, arg0, arg1, (jint) arg21); 

    if (arg21)
        env->ReleaseIntArrayElements(arg2, arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jintArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0) 
		arg01 = env->GetCharArrayElements(arg0, NULL);

	jint *arg11 = NULL;
    if (arg1)
        arg11 = env->GetIntArrayElements(arg1, NULL);

	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11); 

	if (arg0)
        env->ReleaseCharArrayElements(arg0, arg01, 0);

    if (arg11)
        env->ReleaseIntArrayElements(arg1, arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbyteArray arg0, jintArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg01 = NULL;
    if (arg0)
        arg01 = env->GetByteArrayElements(arg0, NULL);

	jint *arg11 = NULL;
    if (arg1)
        arg11 = env->GetIntArrayElements(arg1, NULL);

	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11); 

	if (arg0)
        env->ReleaseByteArrayElements(arg0,arg01, 0);

    if (arg11)
        env->ReleaseIntArrayElements(arg1, arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3CII)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jint arg1, jint arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
	if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jint rc = fn(ppVtbl, (jint) arg01, arg1, arg2); 

	if (arg0)
        env->ReleaseCharArrayElements(arg0, arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BII)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbyteArray arg0, jint arg1, jint arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg01 = NULL;
    if (arg0)
        arg01 = env->GetByteArrayElements(arg0, NULL);

	jint rc = fn(ppVtbl, (jint) arg01, arg1, arg2); 

	if (arg0)
        env->ReleaseByteArrayElements(arg0,arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3Z)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jbooleanArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jboolean *arg11 = NULL;
    if (arg1)
        arg11 = env->GetBooleanArrayElements(arg1, NULL);

	jint rc = fn(ppVtbl, arg0, (jint) arg11); 

	if (arg1)
        env->ReleaseBooleanArrayElements(arg1,arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZ_3Z)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jboolean arg1, jbooleanArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jboolean *arg21 = NULL;
    if (arg2)
        arg21 = env->GetBooleanArrayElements(arg2, NULL);

	jint rc = fn(ppVtbl, arg0, arg1, (jint) arg21); 

	if (arg2)
        env->ReleaseBooleanArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIJ)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jlong arg0)
{
    P_OLE_FN_2L fn = (P_OLE_FN_2L)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl, arg0); 
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIF)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jfloat arg0)
{
    P_OLE_FN_2F fn = (P_OLE_FN_2F)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl, arg0); 
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IID)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jdouble arg0)
{
    P_OLE_FN_2D fn = (P_OLE_FN_2D)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl, arg0); 
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3J)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jlongArray arg0)
{
    P_OLE_FN_2 fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

	jlong *arg01 = NULL;
    if (arg0)
        arg01 = env->GetLongArrayElements(arg0, NULL);

	jint rc = fn(ppVtbl, (jint) arg01); 

	if (arg0)
        env->ReleaseLongArrayElements(arg0,arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3D)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jdoubleArray arg0)
{
    P_OLE_FN_2 fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

	jdouble *arg01 = NULL;
    if (arg0)
        arg01 = env->GetDoubleArrayElements(arg0, NULL);

	jint rc = fn(ppVtbl, (jint) arg01); 

	if (arg0)
        env->ReleaseDoubleArrayElements(arg0,arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3S)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jshortArray arg0)
{
    P_OLE_FN_2 fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

	jshort *arg01 = NULL;
    if (arg0)
        arg01 = env->GetShortArrayElements(arg0, NULL);

	jint rc = fn(ppVtbl, (jint) arg01); 

	if (arg0)
        env->ReleaseShortArrayElements(arg0,arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3F)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jfloatArray arg0)
{
    P_OLE_FN_2 fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

	jfloat *arg01 = NULL;
    if (arg0)
        arg01 = env->GetFloatArrayElements(arg0, NULL);

	jint rc = fn(ppVtbl, (jint) arg01); 

	if (arg0)
        env->ReleaseFloatArrayElements(arg0,arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IISF)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jshort arg0, jfloat arg1)
{
    P_OLE_FN_3SF fn = (P_OLE_FN_3SF)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl, arg0,arg1); 
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIS_3F)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jshort arg0, jfloatArray arg1)
{
    P_OLE_FN_3S fn = (P_OLE_FN_3S)(*(int **)ppVtbl)[fnNumber];

	jfloat *arg11 = NULL;
    if (arg1)
        arg11 = env->GetFloatArrayElements(arg1, NULL);

	jint rc = fn(ppVtbl, arg0, (jint) arg11); 

	if (arg1)
        env->ReleaseFloatArrayElements(arg1,arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IISI)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jshort arg0, jint arg1)
{
    P_OLE_FN_3S fn = (P_OLE_FN_3S)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl, arg0, arg1); 
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZZIIIIIIZZZZSI)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jboolean arg1, jboolean arg2, jint arg3,
  jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jboolean arg9, jboolean arg10, jboolean arg11,
  jboolean arg12, jshort arg13, jint arg14)
{
	P_OLE_FN_16S fn = (P_OLE_FN_16S)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl, arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZZIIIIS)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jboolean arg1, jboolean arg2, jint arg3,
	jint arg4, jint arg5, jint arg6, jshort arg7)
{
	P_OLE_FN_9S fn = (P_OLE_FN_9S)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl, arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3S)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jshortArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jshort *arg11 = NULL;
    if (arg1)
        arg11 = env->GetShortArrayElements(arg1, NULL);

	jint rc = fn(ppVtbl, arg0, (jint) arg11); 

	if (arg1)
        env->ReleaseShortArrayElements(arg1,arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIII_3S)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jshortArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jshort *arg21 = NULL;
    if (arg2)
        arg21 = env->GetShortArrayElements(arg2, NULL);

	jint rc = fn(ppVtbl, arg0, arg1, (jint) arg21); 

	if (arg2)
        env->ReleaseShortArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3CLorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jobject arg1, jintArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
       	arg01 = env->GetCharArrayElements(arg0, NULL);

	nsID nsid;
	nsID *arg11 = NULL;
     
    if (arg1) {
        getnsIDFields(env, arg1, &nsid);
		arg11 = &nsid;
    }

	jint *arg21 = NULL;
    if (arg2)
        arg21 = env->GetIntArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg11) 
        setnsIDFields(env, arg1, arg11);

	if (arg2)
        env->ReleaseIntArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbyteArray arg0, jobject arg1, jintArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg01 = NULL;
    if (arg0)
        arg01 = env->GetByteArrayElements(arg0, NULL);

	nsID nsid;
	nsID *arg11 = NULL;
     
    if (arg1) {
        getnsIDFields(env, arg1, &nsid);
		arg11 = &nsid;
    }

	jint *arg21 = NULL;
    if (arg2)
        arg21 = env->GetIntArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21);
	
	if (arg0)
        env->ReleaseByteArrayElements(arg0,arg01, 0);

	if (arg11) 
        setnsIDFields(env, arg1, arg11);

	if (arg2)
        env->ReleaseIntArrayElements(arg2,arg21, 0);

    return rc;
}
JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3C)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
       	arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3B)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbyteArray arg0, jbyteArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg01 = NULL;
    if (arg0)
        arg01 = env->GetByteArrayElements(arg0, NULL);

	jbyte *arg11 = NULL;
    if (arg1)
        arg11 = env->GetByteArrayElements(arg1, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11);
	
	if (arg0)
        env->ReleaseByteArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseByteArrayElements(arg1,arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3C)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbyteArray arg0, jcharArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg01 = NULL;
    if (arg0)
        arg01 = env->GetByteArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11);
	
	if (arg0)
        env->ReleaseByteArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11,0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3CIZ)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jint arg1, jboolean arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
       arg01 = env->GetCharArrayElements(arg0, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, arg1, arg2);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01,0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BIZ)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbyteArray arg0, jint arg1, jboolean arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg01 = NULL;
    if (arg0)
        arg01 = env->GetByteArrayElements(arg0, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, arg1, arg2);
	
	if (arg0)
        env->ReleaseByteArrayElements(arg0,arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3CZ)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jboolean arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, arg1);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BZ)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbyteArray arg0, jboolean arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg01 = NULL;
    if (arg0)
        arg01 = env->GetByteArrayElements(arg0, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, arg1);
	
	if (arg0)
        env->ReleaseByteArrayElements(arg0,arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3CLorg_eclipse_swt_internal_mozilla_nsID_2I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jobject arg1, jint arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
		arg01 = env->GetCharArrayElements(arg0, NULL);

	nsID nsid;
	nsID *arg11 = NULL;
     
    if (arg1) {
        getnsIDFields(env, arg1, &nsid);
		arg11 = &nsid;
    }

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, arg2);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg11) 
        setnsIDFields(env, arg1, arg11);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbyteArray arg0, jobject arg1, jint arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg01 = NULL;
    if (arg0)
        arg01 = env->GetByteArrayElements(arg0, NULL);

	nsID nsid;
	nsID *arg11 = NULL;
     
    if (arg1) {
        getnsIDFields(env, arg1, &nsid);
		arg11 = &nsid;
    }

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, arg2);
	
	if (arg0)
        env->ReleaseByteArrayElements(arg0,arg01, 0);

	if (arg11) 
        setnsIDFields(env, arg1, arg11);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3CZ)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jcharArray arg1, jboolean arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg11 = NULL;
    if (arg1)
      	arg11 = env->GetCharArrayElements(arg1, NULL);

  	jint rc = fn(ppVtbl, arg0, (jint) arg11, arg2);
	
	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3BZ)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jbyteArray arg1, jboolean arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg11 = NULL;
    if (arg1)
        arg11 = env->GetByteArrayElements(arg1, NULL);

  	jint rc = fn(ppVtbl, arg0, (jint) arg11, arg2);
	
	if (arg1)
        env->ReleaseByteArrayElements(arg1,arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jcharArray arg1, jintArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jint *arg21 = NULL;
    if (arg2)
        arg21 = env->GetIntArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, arg0, (jint) arg11, (jint) arg21);
	
	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseIntArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3C)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jcharArray arg1, jcharArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, arg0, (jint) arg11, (jint) arg21);
	
	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3B_3C)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jbyteArray arg1, jcharArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jbyte *arg11 = NULL;
    if (arg1)
        arg11 = env->GetByteArrayElements(arg1, NULL);

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, arg0, (jint) arg11, (jint) arg21);
	
	if (arg1)
        env->ReleaseByteArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL JNICALL XPCOM_NATIVE(VtblCall__II_3C_3C_3CZ)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1, jcharArray arg2, jboolean arg3)
{
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21, arg3);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3II)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jintArray arg0, jint arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jint *arg01 = NULL;
    if (arg0)
        arg01 = env->GetIntArrayElements(arg0, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, arg1);
	
	if (arg01)
        env->ReleaseIntArrayElements(arg0,arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIII)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2)
{
	P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl, arg0, arg1, arg2);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZ)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jboolean arg1)
{
	P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl, arg0, arg1);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZ_3C)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jboolean arg1, jcharArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, arg0, arg1, (jint) arg21);
	
	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIZZZ)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jboolean arg0, jboolean arg1, jboolean arg2)
{
	P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl, arg0, arg1, arg2);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3Z_3Z_3Z)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jbooleanArray arg0, jbooleanArray arg1, jbooleanArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jboolean *arg01 = NULL;
    if (arg0)
        arg01 = env->GetBooleanArrayElements(arg0, NULL);

	jboolean *arg11 = NULL;
    if (arg1)
        arg11 = env->GetBooleanArrayElements(arg1, NULL);

	jboolean *arg21 = NULL;
    if (arg2)
        arg21 = env->GetBooleanArrayElements(arg2, NULL);

	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21); 

	if (arg0)
        env->ReleaseBooleanArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseBooleanArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseBooleanArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIII_3C)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jcharArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, arg0, arg1, (jint) arg21);
	
	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIZI)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jboolean arg2, jint arg3)
{
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

 	return fn(ppVtbl, arg0, arg1, arg2 , arg3);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3C_3C)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1, jcharArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3C_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1, jintArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jint *arg21 = NULL;
    if (arg2)
        arg21 = env->GetIntArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseIntArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZ_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jboolean arg1, jintArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jint *arg21 = NULL;
    if (arg2)
        arg21 = env->GetIntArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, arg0, arg1, (jint) arg21);
	
	if (arg2)
        env->ReleaseIntArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIIZ_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jboolean arg3, jintArray arg4)
{
    P_OLE_FN_6 fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];

	jint *arg41 = NULL;
    if (arg4)
        arg41 = env->GetIntArrayElements(arg4, NULL);

  	jint rc = fn(ppVtbl, arg0, arg1, arg2, arg3, (jint) arg41);
	
	if (arg4)
        env->ReleaseIntArrayElements(arg4,arg41, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3C_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jcharArray arg1, jcharArray arg2, jintArray arg3)
{
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

	jint *arg31 = NULL;
    if (arg3)
        arg31 = env->GetIntArrayElements(arg3, NULL);

  	jint rc = fn(ppVtbl, arg0, (jint) arg11, (jint) arg21, (jint) arg31);
	
	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

	if (arg3)
        env->ReleaseIntArrayElements(arg3,arg31, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3C_3Z)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1, jbooleanArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jboolean *arg21 = NULL;
    if (arg2)
        arg21 = env->GetBooleanArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseBooleanArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3C_3C_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1, jcharArray arg2, jintArray arg3)
{
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

	jint *arg31 = NULL;
    if (arg3)
        arg31 = env->GetIntArrayElements(arg3, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21, (jint) arg31);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

	if (arg3)
        env->ReleaseIntArrayElements(arg3,arg31, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIII_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jintArray arg3)
{
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

	jint *arg31 = NULL;
    if (arg3)
        arg31 = env->GetIntArrayElements(arg3, NULL);

  	jint rc = fn(ppVtbl, arg0, arg1, arg2, (jint) arg31);
	
	if (arg3)
        env->ReleaseIntArrayElements(arg3,arg31, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZZ)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jboolean arg1, jboolean arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl, arg0, arg1, arg2);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZZIZZZZII)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jboolean arg1, jboolean arg2, jint arg3,
	jboolean arg4, jboolean arg5, jboolean arg6, jboolean arg7, jint arg8, jint arg9)
{
    P_OLE_FN_11 fn = (P_OLE_FN_11)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl, arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIZ_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jboolean arg0, jintArray arg1)
{
    P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	jint *arg11 = NULL;
    if (arg1)
        arg11 = env->GetIntArrayElements(arg1, NULL);

  	jint rc = fn(ppVtbl, arg0, (jint) arg11);
	
	if (arg1)
        env->ReleaseIntArrayElements(arg1,arg11, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIII_3Z)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jbooleanArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jboolean *arg21 = NULL;
    if (arg2)
        arg21 = env->GetBooleanArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, arg0, arg1, (jint) arg21);
	
	if (arg2)
        env->ReleaseBooleanArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3CI_3C_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jcharArray arg1, jint arg2, jcharArray arg3, jintArray arg4)
{
    P_OLE_FN_6 fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jchar *arg31 = NULL;
    if (arg3)
        arg31 = env->GetCharArrayElements(arg3, NULL);

	jint *arg41 = NULL;
    if (arg4)
        arg41 = env->GetIntArrayElements(arg4, NULL);

  	jint rc = fn(ppVtbl, arg0, (jint) arg11, arg2, (jint) arg31, (jint) arg41);
	
	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg3)
        env->ReleaseCharArrayElements(arg3,arg31, 0);

	if (arg4)
        env->ReleaseIntArrayElements(arg4,arg41, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIII_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4)
{
    P_OLE_FN_6 fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];

	jint *arg41 = NULL;
    if (arg4)
        arg41 = env->GetIntArrayElements(arg4, NULL);

  	jint rc = fn(ppVtbl, arg0, arg1, arg2, arg3, (jint) arg41);
	
	if (arg4)
        env->ReleaseIntArrayElements(arg4,arg41, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZZII)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jboolean arg1, jboolean arg2, jint arg3, jint arg4)
{
    P_OLE_FN_6 fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];

  	return fn(ppVtbl, arg0, arg1, arg2, arg3, arg4);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZZZZZZ_3Z)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jboolean arg1, jboolean arg2, jboolean arg3,
	jboolean arg4, jboolean arg5, jboolean arg6, jbooleanArray arg7)
{
    P_OLE_FN_9 fn = (P_OLE_FN_9)(*(int **)ppVtbl)[fnNumber];

	jboolean *arg71 = NULL;
    if (arg7)
        arg71 = env->GetBooleanArrayElements(arg7, NULL);

  	jint rc = fn(ppVtbl, arg0, arg1, arg2, arg3, arg4, arg5, arg6, (jint) arg71);
	
	if (arg7)
        env->ReleaseBooleanArrayElements(arg7,arg71, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3C_3CI_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1, jcharArray arg2, jint arg3, jintArray arg4)
{
    P_OLE_FN_6 fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

	jint *arg41 = NULL;
    if (arg4)
        arg41 = env->GetIntArrayElements(arg4, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21, arg3, (jint) arg41);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

	if (arg4)
        env->ReleaseIntArrayElements(arg4,arg41, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIIII)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	  P_OLE_FN_6 fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];
	  return fn(ppVtbl,arg0, arg1, arg2, arg3, arg4);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3I_3I_3I_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jintArray arg1, jintArray arg2, jintArray arg3, jintArray arg4)
{
    P_OLE_FN_6 fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];

	jint *arg11 = NULL;
    if (arg1)
        arg11 = env->GetIntArrayElements(arg1, NULL);

	jint *arg21 = NULL;
    if (arg2)
        arg21 = env->GetIntArrayElements(arg2, NULL);

	jint *arg31 = NULL;
    if (arg3)
        arg31 = env->GetIntArrayElements(arg3, NULL);

	jint *arg41 = NULL;
    if (arg4)
        arg41 = env->GetIntArrayElements(arg4, NULL);

  	jint rc = fn(ppVtbl, arg0, (jint) arg11, (jint) arg21, (jint) arg31, (jint) arg41);
	
	if (arg1)
        env->ReleaseIntArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseIntArrayElements(arg2,arg21, 0);

	if (arg3)
        env->ReleaseIntArrayElements(arg3,arg31, 0);

	if (arg4)
        env->ReleaseIntArrayElements(arg4,arg41, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3CI_3C)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jint arg1, jcharArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, arg1, (jint) arg21);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3C_3Z)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jcharArray arg1, jbooleanArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jboolean *arg21 = NULL;
    if (arg2)
        arg21 = env->GetBooleanArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, arg0, (jint) arg11, (jint) arg21);
	
	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseBooleanArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIII)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jint arg3)
{
	P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];
	return fn(ppVtbl,arg0, arg1, arg2, arg3);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3C_3C_3C)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1, jcharArray arg2, jcharArray arg3)
{
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

	jchar *arg31 = NULL;
    if (arg3)
        arg31 = env->GetCharArrayElements(arg3, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21, (jint) arg31);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

	if (arg3)
        env->ReleaseCharArrayElements(arg3,arg31, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIIZ_3Z)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jboolean arg3, jbooleanArray arg4)
{
    P_OLE_FN_6 fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];

	jboolean *arg41 = NULL;
    if (arg4)
        arg41 = env->GetBooleanArrayElements(arg4, NULL);

  	jint rc = fn(ppVtbl, arg0, arg1, arg2, arg3, (jint) arg41);

	if (arg4)
        env->ReleaseBooleanArrayElements(arg4,arg41, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIIZ)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];
	return fn(ppVtbl,arg0, arg1, arg2, arg3);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIIZ_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jboolean arg2, jintArray arg3)
{
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

	jint *arg31 = NULL;
    if (arg3)
        arg31 = env->GetIntArrayElements(arg3, NULL);

  	jint rc = fn(ppVtbl, arg0, arg1, arg2, (jint) arg31);

	if (arg3)
        env->ReleaseIntArrayElements(arg3,arg31, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIII_3Z)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jbooleanArray arg3)
{
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

	jboolean *arg31 = NULL;
    if (arg3)
        arg31 = env->GetBooleanArrayElements(arg3, NULL);

  	jint rc = fn(ppVtbl, arg0, arg1, arg2, (jint) arg31);

	if (arg3)
        env->ReleaseBooleanArrayElements(arg3,arg31, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3CI_3C_3I_3Z)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1,
			jint arg2, jcharArray arg3, jintArray arg4, jbooleanArray arg5)
{
    P_OLE_FN_7 fn = (P_OLE_FN_7)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jchar *arg31 = NULL;
    if (arg3)
        arg31 = env->GetCharArrayElements(arg3, NULL);

	jint *arg41 = NULL;
    if (arg4)
        arg41 = env->GetIntArrayElements(arg4, NULL);

	jboolean *arg51 = NULL;
    if (arg5)
        arg51 = env->GetBooleanArrayElements(arg5, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, arg2, (jint) arg31, (jint) arg41, (jint) arg51);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg3)
        env->ReleaseCharArrayElements(arg3,arg31, 0);

	if (arg4)
        env->ReleaseIntArrayElements(arg4,arg41, 0);

	if (arg5)
        env->ReleaseBooleanArrayElements(arg5,arg51, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3C_3C_3Z)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1, jcharArray arg2, jbooleanArray arg3)
{
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

	jboolean *arg31 = NULL;
    if (arg3)
        arg31 = env->GetBooleanArrayElements(arg3, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21, (jint) arg31);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

	if (arg3)
        env->ReleaseBooleanArrayElements(arg3,arg31, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3CI_3C_3C_3C_3C_3Z_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1, jint arg2, jcharArray arg3,
				jcharArray arg4, jcharArray arg5, jcharArray arg6, jbooleanArray arg7, jintArray arg8)
{
    P_OLE_FN_10 fn = (P_OLE_FN_10)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jchar *arg31 = NULL;
    if (arg3)
        arg31 = env->GetCharArrayElements(arg3, NULL);

	jchar *arg41 = NULL;
	if (arg4)
        arg41 = env->GetCharArrayElements(arg4, NULL);

	jchar *arg51 = NULL;
    if (arg5)
        arg51 = env->GetCharArrayElements(arg5, NULL);

	jchar *arg61 = NULL;
    if (arg6)
        arg61 = env->GetCharArrayElements(arg6, NULL);

	jboolean *arg71 = NULL;
    if (arg7)
        arg71 = env->GetBooleanArrayElements(arg7, NULL);

	jint *arg81 = NULL;
    if (arg8)
        arg81 = env->GetIntArrayElements(arg8, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, arg2, (jint) arg31, (jint) arg41, (jint) arg51, (jint) arg61, (jint) arg71, (jint) arg81);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg3)
        env->ReleaseCharArrayElements(arg3,arg31, 0);

	if (arg4)
        env->ReleaseCharArrayElements(arg4,arg41, 0);

	if (arg5)
        env->ReleaseCharArrayElements(arg5,arg51, 0);

	if (arg6)
        env->ReleaseCharArrayElements(arg6,arg61, 0);

	if (arg7)
        env->ReleaseBooleanArrayElements(arg7,arg71, 0);

	if (arg8)
        env->ReleaseIntArrayElements(arg8,arg81, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3C_3C_3Z_3Z)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jcharArray arg1, jcharArray arg2, jbooleanArray arg3, jbooleanArray arg4)
{
    P_OLE_FN_6 fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jchar *arg11 = NULL;
    if (arg1)
        arg11 = env->GetCharArrayElements(arg1, NULL);

	jchar *arg21 = NULL;
    if (arg2)
        arg21 = env->GetCharArrayElements(arg2, NULL);

	jboolean *arg31 = NULL;
    if (arg3)
        arg31 = env->GetBooleanArrayElements(arg3, NULL);

	jboolean *arg41 = NULL;
    if (arg4)
        arg41 = env->GetBooleanArrayElements(arg4, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21, (jint) arg31, (jint) arg41);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseCharArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseCharArrayElements(arg2,arg21, 0);

	if (arg3)
        env->ReleaseBooleanArrayElements(arg3,arg31, 0);

	if (arg4)
        env->ReleaseBooleanArrayElements(arg4,arg41, 0);;

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3C_3I_3I)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jintArray arg1, jintArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

	jint *arg11 = NULL;
    if (arg1)
        arg11 = env->GetIntArrayElements(arg1, NULL);

	jint *arg21 = NULL;
    if (arg2)
        arg21 = env->GetIntArrayElements(arg2, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, (jint) arg11, (jint) arg21);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

	if (arg1)
        env->ReleaseIntArrayElements(arg1,arg11, 0);

	if (arg2)
        env->ReleaseIntArrayElements(arg2,arg21, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jobject arg0)
{
    P_OLE_FN_2 fn = (P_OLE_FN_2)(*(int **)ppVtbl)[fnNumber];

	nsID nsid;
	nsID *arg01 = NULL;
     
    if (arg0) {
        getnsIDFields(env, arg0, &nsid);
		arg01 = &nsid;
    }

  	jint rc = fn(ppVtbl, (jint) arg01);
	
	if (arg01) 
        setnsIDFields(env, arg0, arg01);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIILorg_eclipse_swt_internal_mozilla_nsID_2Z)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jobject arg1, jboolean arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	nsID nsid;
	nsID *arg11 = NULL;
     
    if (arg1) {
        getnsIDFields(env, arg1, &nsid);
		arg11 = &nsid;
    }

  	jint rc = fn(ppVtbl, arg0, (jint) arg11, arg2);
	
	if (arg11) 
        setnsIDFields(env, arg1, arg11);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIII_3C)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jcharArray arg3)
{
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

	jchar *arg31 = NULL;
    if (arg3)
        arg31 = env->GetCharArrayElements(arg3, NULL);

  	jint rc = fn(ppVtbl, arg0, arg1, arg2, (jint) arg31);
	
	if (arg3)
        env->ReleaseCharArrayElements(arg3,arg31, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3CIIII)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jcharArray arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
    P_OLE_FN_6 fn = (P_OLE_FN_6)(*(int **)ppVtbl)[fnNumber];

	jchar *arg01 = NULL;
    if (arg0)
        arg01 = env->GetCharArrayElements(arg0, NULL);

  	jint rc = fn(ppVtbl, (jint) arg01, arg1, arg2, arg3, arg4);
	
	if (arg0)
        env->ReleaseCharArrayElements(arg0,arg01, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIIZI_3Z)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jboolean arg1, jint arg2, jbooleanArray arg3)
{
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];

	jboolean *arg03 = NULL;
    if (arg3)
        arg03 = env->GetBooleanArrayElements(arg3, NULL);

	jint rc = fn(ppVtbl,arg0,arg1,arg2,(jint) arg03); 

    if (arg03)
        env->ReleaseBooleanArrayElements(arg3, arg03, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIZZ)
  (JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jboolean arg0, jboolean arg1)
{
	P_OLE_FN_3 fn = (P_OLE_FN_3)(*(int **)ppVtbl)[fnNumber];

	return fn(ppVtbl,arg0,arg1);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jobject arg1, jintArray arg2)
{
	nsID _arg0, *lparg0=NULL;
	nsID _arg1, *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];
    
	if (arg0) lparg0 = getnsIDFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getnsIDFields(env, arg1, &_arg1);
	if (arg2) lparg2 = env->GetIntArrayElements(arg2, NULL);
	rc = fn(ppVtbl, (int)lparg0, (int)lparg1, (int)lparg2);
	if (arg0) setnsIDFields(env, arg0, lparg0);
	if (arg1) setnsIDFields(env, arg1, lparg1);
	if (arg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2ILorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jint arg1, jobject arg2, jintArray arg3)
{
	nsID _arg0, *lparg0=NULL;
	nsID _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];
	if (arg0) lparg0 = getnsIDFields(env, arg0, &_arg0);
	if (arg2) lparg2 = getnsIDFields(env, arg2, &_arg2);
	if (arg3) lparg3 = env->GetIntArrayElements(arg3, NULL);
	rc = fn(ppVtbl, (int)lparg0, arg1, (int)lparg2, (int)lparg3);
	if (arg0) setnsIDFields(env, arg0, lparg0);
	if (arg2) setnsIDFields(env, arg2, lparg2);
	if (arg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BILorg_eclipse_swt_internal_mozilla_nsID_2_3I)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jbyteArray arg0, jint arg1, jobject arg2, jintArray arg3)
{
	jbyte *lparg0=NULL;
	nsID _arg2, *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
    P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];
	if (arg0) lparg0 = env->GetByteArrayElements(arg0, NULL);
	if (arg2) lparg2 = getnsIDFields(env, arg2, &_arg2);
	if (arg3) lparg3 = env->GetIntArrayElements(arg3, NULL);
	rc = fn(ppVtbl, (int)lparg0, arg1, (int)lparg2, (int)lparg3);
	if (arg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	if (arg2) setnsIDFields(env, arg2, lparg2);
	if (arg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IILorg_eclipse_swt_internal_mozilla_nsID_2Lorg_eclipse_swt_internal_mozilla_nsID_2_3Z)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jobject arg0, jobject arg1, jbooleanArray arg2)
{
	nsID _arg0, *lparg0=NULL;
	nsID _arg1, *lparg1=NULL;
	jboolean *lparg2=NULL;
	jint rc;
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];
    
	if (arg0) lparg0 = getnsIDFields(env, arg0, &_arg0);
	if (arg1) lparg1 = getnsIDFields(env, arg1, &_arg1);
	if (arg2) lparg2 = env->GetBooleanArrayElements(arg2, NULL);
	rc = fn(ppVtbl, (int)lparg0, (int)lparg1, (int)lparg2);
	if (arg0) setnsIDFields(env, arg0, lparg0);
	if (arg1) setnsIDFields(env, arg1, lparg1);
	if (arg2) env->ReleaseBooleanArrayElements(arg2, lparg2, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BLorg_eclipse_swt_internal_mozilla_nsID_2_3Z)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jbyteArray arg0, jobject arg1, jbooleanArray arg2)
{
	jbyte *lparg0=NULL;
	nsID _arg1, *lparg1=NULL;
	jboolean *lparg2=NULL;
	jint rc;
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];
	if (arg0) lparg0 = env->GetByteArrayElements(arg0, NULL);
	if (arg1) lparg1 = getnsIDFields(env, arg1, &_arg1);
	if (arg2) lparg2 = env->GetBooleanArrayElements(arg2, NULL);
	rc = fn(ppVtbl, (int)lparg0, (int)lparg1, (int)lparg2);
	if (arg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	if (arg1) setnsIDFields(env, arg1, lparg1);
	if (arg2) env->ReleaseBooleanArrayElements(arg2, lparg2, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3I_3I)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jintArray arg1, jintArray arg2)
{
	P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jint *lparg1=NULL, *lparg2=NULL;
	jint rc;
	if (arg1) lparg1 = env->GetIntArrayElements(arg1, NULL);
	if (arg2) lparg2 = env->GetIntArrayElements(arg2, NULL);
	rc = fn(ppVtbl, arg0, (jint)lparg1, (jint)lparg2); 
    if (arg1) env->ReleaseIntArrayElements(arg1, lparg1, 0);
	if (arg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
    return rc;
}

JNIEXPORT jint JNICALL JNICALL XPCOM_NATIVE(VtblCall__IIISSS_3Z)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jshort arg1, jshort arg2, jshort arg3, jbooleanArray arg4)
{
    P_OLE_FN_6SSS fn = (P_OLE_FN_6SSS)(*(int **)ppVtbl)[fnNumber];
	jboolean *lparg4 = NULL;
	jint rc;
	if (arg4) lparg4 = env->GetBooleanArrayElements(arg4, NULL);
	rc = fn(ppVtbl, arg0, arg1, arg2, arg3, (jint)lparg4);
	if (arg4) env->ReleaseBooleanArrayElements(arg4, lparg4, 0);
    return rc;
}

JNIEXPORT jint JNICALL JNICALL XPCOM_NATIVE(VtblCall__IIZSSS_3Z)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jboolean arg0, jshort arg1, jshort arg2, jshort arg3, jbooleanArray arg4)
{
    P_OLE_FN_6SSS fn = (P_OLE_FN_6SSS)(*(int **)ppVtbl)[fnNumber];
	jboolean *lparg4 = NULL;
	jint rc;
	if (arg4) lparg4 = env->GetBooleanArrayElements(arg4, NULL);
	rc = fn(ppVtbl, (jint)arg0, arg1, arg2, arg3, (jint)lparg4);
	if (arg4) env->ReleaseBooleanArrayElements(arg4, lparg4, 0);
    return rc;
}

JNIEXPORT jint JNICALL JNICALL XPCOM_NATIVE(VtblCall__IIIIIIIII)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
    P_OLE_FN_8 fn = (P_OLE_FN_8)(*(int **)ppVtbl)[fnNumber];

  	jint rc = fn(ppVtbl, arg0, arg1, arg2, arg3, arg4, arg5, arg6);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIZI_3Z)
	(JNIEnv *env, jclass, jint fnNumber, jint ppVtbl, jboolean arg0, jint arg1, jbooleanArray arg2)
{
    P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];

	jboolean *lparg2 = NULL;
    if (arg2) lparg2 = env->GetBooleanArrayElements(arg2, NULL);

	jint rc = fn(ppVtbl, (jint)arg0, arg1, (jint)lparg2); 

	if (arg2) env->ReleaseBooleanArrayElements(arg2, lparg2, 0);

    return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__IIII_3B_3I)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3)
{
	P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
	if (arg2) lparg2 = env->GetByteArrayElements(arg2, NULL);
	if (arg3) lparg3 = env->GetIntArrayElements(arg3, NULL);
	rc = fn(ppVtbl, arg0, arg1, (jint)lparg2, (jint)lparg3);
	if (arg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg2) env->ReleaseByteArrayElements(arg2, lparg2, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3BI)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jbyteArray arg1, jint arg2)
{
	P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];
	jbyte *lparg1=NULL;
	jint rc;
	if (arg1) lparg1 = env->GetByteArrayElements(arg1, NULL);
	rc = fn(ppVtbl, arg0, (jint)lparg1, arg2);
	if (arg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__III_3BI_3I)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3)
{
	P_OLE_FN_5 fn = (P_OLE_FN_5)(*(int **)ppVtbl)[fnNumber];
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;
	if (arg1) lparg1 = env->GetByteArrayElements(arg1, NULL);
	if (arg3) lparg3 = env->GetIntArrayElements(arg3, NULL);
	rc = fn(ppVtbl, arg0, (jint)lparg1, arg2, (jint)lparg3);
	if (arg3) env->ReleaseIntArrayElements(arg3, lparg3, 0);
	if (arg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3BII_3BII_3I_3I)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jbyteArray arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jint arg5, jintArray arg6, jintArray arg7)
{
	P_OLE_FN_9 fn = (P_OLE_FN_9)(*(int **)ppVtbl)[fnNumber];
	jbyte *lparg0=NULL;
	jbyte *lparg3=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint rc;
	if (arg0) lparg0 = env->GetByteArrayElements(arg0, NULL);
	if (arg3) lparg3 = env->GetByteArrayElements(arg3, NULL);
	if (arg6) lparg6 = env->GetIntArrayElements(arg6, NULL);
	if (arg7) lparg7 = env->GetIntArrayElements(arg7, NULL);
	rc = fn(ppVtbl, (jint)lparg0, arg1, arg2, (jint)lparg3, arg4, arg5, (jint)lparg6, (jint)lparg7);
	if (arg7) env->ReleaseIntArrayElements(arg7, lparg7, 0);
	if (arg6) env->ReleaseIntArrayElements(arg6, lparg6, 0);
	if (arg3) env->ReleaseByteArrayElements(arg3, lparg3, 0);
	if (arg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(VtblCall__II_3B_3B_3I)
	(JNIEnv *env, jclass that, jint fnNumber, jint ppVtbl, jbyteArray arg0, jbyteArray arg1, jintArray arg2)
{
	P_OLE_FN_4 fn = (P_OLE_FN_4)(*(int **)ppVtbl)[fnNumber];
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	if (arg0) lparg0 = env->GetByteArrayElements(arg0, NULL);
	if (arg1) lparg1 = env->GetByteArrayElements(arg1, NULL);
	if (arg2) lparg2 = env->GetIntArrayElements(arg2, NULL);
	rc = fn(ppVtbl, (jint)lparg0, (jint)lparg1, (jint)lparg2);
	if (arg2) env->ReleaseIntArrayElements(arg2, lparg2, 0);
	if (arg1) env->ReleaseByteArrayElements(arg1, lparg1, 0);
	if (arg0) env->ReleaseByteArrayElements(arg0, lparg0, 0);
	return rc;
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(nsCRT_1strlen_1PRUnichar)
	(JNIEnv *env, jclass, jint arg0)
{
	PRUnichar* lparg0 = NULL;
	if (arg0) lparg0 = (PRUnichar *)arg0;

	return nsCRT::strlen(lparg0);
}

JNIEXPORT jint JNICALL XPCOM_NATIVE(strlen)
	(JNIEnv *env, jclass, jint arg0)
{
	char* lparg0 = NULL;
	if (arg0) lparg0 = (char *)arg0;

	return strlen(lparg0);
}

} /* extern "C" */
