/*******************************************************************************
* Copyright (c) 2000, 2004 IBM Corporation and others. All rights reserved.
* The contents of this file are made available under the terms
* of the GNU Lesser General Public License (LGPL) Version 2.1 that
* accompanies this distribution (lgpl-v21.txt).  The LGPL is also
* available at http://www.gnu.org/licenses/lgpl.html.  If the version
* of the LGPL at http://www.gnu.org is different to the version of
* the LGPL accompanying this distribution and there is any conflict
* between the two license versions, the terms of the LGPL accompanying
* this distribution shall govern.
* 
* Contributors:
*     IBM Corporation - initial API and implementation
*******************************************************************************/

#include "swt.h"
#include "os_structs.h"
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_gtk_OS_##func

#ifndef NO_GDK_1WINDOWING_1X11
JNIEXPORT jboolean JNICALL OS_NATIVE(GDK_1WINDOWING_1X11)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	OS_NATIVE_ENTER(env, that, GDK_1WINDOWING_1X11_FUNC)
#ifdef GDK_WINDOWING_X11
	rc = (jboolean)1;
#else
	rc = (jboolean)0;
#endif	
	OS_NATIVE_EXIT(env, that, GDK_1WINDOWING_1X11_FUNC)
	return rc;
}
#endif

#ifndef NO__1gtk_1file_1chooser_1dialog_1new
JNIEXPORT SWT_PTR JNICALL OS_NATIVE(_1gtk_1file_1chooser_1dialog_1new)
	(JNIEnv *env, jclass that, jbyteArray arg0, SWT_PTR arg1, jint arg2, SWT_PTR arg3, jint arg4, SWT_PTR arg5, jint arg6, SWT_PTR arg7)
{
	jbyte *lparg0=NULL;
	SWT_PTR rc = 0;
	OS_NATIVE_ENTER(env, that, _1gtk_1file_1chooser_1dialog_1new_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
/*
	rc = (SWT_PTR)gtk_file_chooser_dialog_new(lparg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
*/
	{
		static int initialized = 0;
		static void *handle = NULL;
		/*
		* On AMD64, it is critical that functions which have a variable number of
		* arguments, indicated by '...', include the '...' in their prototype.  This
		* changes the calling convention, and leaving it out will cause crashes.
		*
		* For some reason, we must also explicitly declare all of the arguments we
		* are passing in, otherwise it crashes.
		*/
/*		typedef SWT_PTR (*FPTR)(jbyte *, SWT_PTR, jint, SWT_PTR, ...); */
		typedef SWT_PTR (*FPTR)(jbyte *, SWT_PTR, jint, SWT_PTR, jint, SWT_PTR, jint, SWT_PTR, ...);
		static FPTR fptr;
		rc = 0;
		if (!initialized) {
			if (!handle) handle = dlopen(gtk_file_chooser_dialog_new_LIB, RTLD_LAZY);
			if (handle) fptr = (FPTR)dlsym(handle, "gtk_file_chooser_dialog_new");
			initialized = 1;
		}
		if (fptr) {
			rc = (SWT_PTR)(*fptr)(lparg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
		}
	}
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1gtk_1file_1chooser_1dialog_1new_FUNC);
	return rc;
}
#endif
