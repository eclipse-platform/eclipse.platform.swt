/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/

#include "swt.h"
#include "xpcom_structs.h"
#include "xpcom_stats.h"

extern "C" {

#define XPCOM_NATIVE(func) Java_org_eclipse_swt_internal_mozilla_XPCOM_##func

#ifndef NO_strlen_1PRUnichar
JNIEXPORT jint JNICALL XPCOM_NATIVE(strlen_1PRUnichar)
	(JNIEnv *env, jclass that, SWT_PTR arg0)
{
	jint rc;
	XPCOM_NATIVE_ENTER(env, that, strlen_1PRUnichar_FUNC);
	{
	const PRUnichar* lparg0 = NULL;
	if (arg0) lparg0 = (const PRUnichar *)arg0;
	PRUint32 len = 0;
	if (lparg0 != NULL)	while (*lparg0++ != 0) len++;
	rc = (jint)len;
	}
	XPCOM_NATIVE_EXIT(env, that, strlen_1PRUnichar_FUNC);
	return rc;
}
#endif

#ifndef NO__1JS_1EvaluateUCScriptForPrincipals
JNIEXPORT jint JNICALL XPCOM_NATIVE(_1JS_1EvaluateUCScriptForPrincipals)
	(JNIEnv *env, jclass that, jbyteArray mozillaPath, SWT_PTR arg0, SWT_PTR arg1, SWT_PTR arg2, jcharArray arg3, jint arg4, jbyteArray arg5, jint arg6, SWT_PTRArray arg7)
{
	jbyte *lpmozillaPath=NULL;
	jchar *lparg3=NULL;
	jbyte *lparg5=NULL;
	SWT_PTR *lparg7=NULL;
	jint rc = 0;
	XPCOM_NATIVE_ENTER(env, that, _1JS_1EvaluateUCScriptForPrincipals_FUNC);
	if (mozillaPath) if ((lpmozillaPath = env->GetByteArrayElements(mozillaPath, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = env->GetCharArrayElements(arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = env->GetByteArrayElements(arg5, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = env->GetSWT_PTRArrayElements(arg7, NULL)) == NULL) goto fail;
/*
	rc = (jint)JS_EvaluateUCScriptForPrincipals(arg0, arg1, arg2, lparg3, arg4, lparg5, arg6, lparg7);
*/
	{
	
#ifdef _WIN32
		LOAD_FUNCTION(fp, JS_EvaluateUCScriptForPrincipals)
		if (fp) {
			rc = (jint)((jint (*)(SWT_PTR, SWT_PTR, SWT_PTR, jchar *, jint, jbyte *, jint, SWT_PTR *))fp)(arg0, arg1, arg2, lparg3, arg4, lparg5, arg6, lparg7);
		}
#else
#define CALLING_CONVENTION
		static int initialized = 0;
		static void *fp = NULL;
		if (!initialized) {
			void* handle = dlopen((const char *)lpmozillaPath, RTLD_LAZY);
			if (handle) {
				fp = dlsym(handle, "JS_EvaluateUCScriptForPrincipals");
			}
			initialized = 1;
		}
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(SWT_PTR, SWT_PTR, SWT_PTR, jchar *, jint, jbyte *, jint, SWT_PTR *))fp)(arg0, arg1, arg2, lparg3, arg4, lparg5, arg6, lparg7);
		}
#endif /* _WIN32 */
	}
fail:
	if (arg7 && lparg7) env->ReleaseSWT_PTRArrayElements(arg7, lparg7, 0);
	if (arg5 && lparg5) env->ReleaseByteArrayElements(arg5, lparg5, 0);
	if (arg3 && lparg3) env->ReleaseCharArrayElements(arg3, lparg3, 0);
	if (mozillaPath && lpmozillaPath) env->ReleaseByteArrayElements(mozillaPath, lpmozillaPath, 0);
	XPCOM_NATIVE_EXIT(env, that, _1JS_1EvaluateUCScriptForPrincipals_FUNC);
	return rc;
}
#endif

}
