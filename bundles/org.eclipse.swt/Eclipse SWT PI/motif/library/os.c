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

#include "swt.h"
#include "os_structs.h"
#include "os_stats.h"

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_motif_OS_##func

#ifndef NO_CODESET
JNIEXPORT jint JNICALL OS_NATIVE(CODESET)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, CODESET_FUNC);
	rc = (jint)CODESET;
	OS_NATIVE_EXIT(env, that, CODESET_FUNC);
	return rc;
}
#endif

#ifndef NO_Call
JNIEXPORT jint JNICALL OS_NATIVE(Call)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, Call_FUNC);
	rc = (jint)((jint (*)())arg0)(arg1, arg2);
	OS_NATIVE_EXIT(env, that, Call_FUNC);
	return rc;
}
#endif

#ifndef NO_ConnectionNumber
JNIEXPORT jint JNICALL OS_NATIVE(ConnectionNumber)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, ConnectionNumber_FUNC);
	rc = (jint)ConnectionNumber(arg0);
	OS_NATIVE_EXIT(env, that, ConnectionNumber_FUNC);
	return rc;
}
#endif

#ifndef NO_FD_1ISSET
JNIEXPORT jboolean JNICALL OS_NATIVE(FD_1ISSET)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, FD_1ISSET_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jboolean)FD_ISSET(arg0, (fd_set *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, FD_1ISSET_FUNC);
	return rc;
}
#endif

#ifndef NO_FD_1SET
JNIEXPORT void JNICALL OS_NATIVE(FD_1SET)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, FD_1SET_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	FD_SET(arg0, (fd_set *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, FD_1SET_FUNC);
}
#endif

#ifndef NO_FD_1ZERO
JNIEXPORT void JNICALL OS_NATIVE(FD_1ZERO)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, FD_1ZERO_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	FD_ZERO((fd_set *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, FD_1ZERO_FUNC);
}
#endif

#ifndef NO_LC_1CTYPE
JNIEXPORT jint JNICALL OS_NATIVE(LC_1CTYPE)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, LC_1CTYPE_FUNC);
	rc = (jint)LC_CTYPE;
	OS_NATIVE_EXIT(env, that, LC_1CTYPE_FUNC);
	return rc;
}
#endif

#ifndef NO_MB_1CUR_1MAX
JNIEXPORT jint JNICALL OS_NATIVE(MB_1CUR_1MAX)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, MB_1CUR_1MAX_FUNC);
	rc = (jint)MB_CUR_MAX;
	OS_NATIVE_EXIT(env, that, MB_1CUR_1MAX_FUNC);
	return rc;
}
#endif

#ifndef NO_XAllocColor
JNIEXPORT jint JNICALL OS_NATIVE(XAllocColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	XColor _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XAllocColor_FUNC);
	if (arg2) if ((lparg2 = getXColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)XAllocColor((Display *)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setXColorFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, XAllocColor_FUNC);
	return rc;
}
#endif

#ifndef NO_XBell
JNIEXPORT void JNICALL OS_NATIVE(XBell)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XBell_FUNC);
	XBell((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XBell_FUNC);
}
#endif

#ifndef NO_XBlackPixel
JNIEXPORT jint JNICALL OS_NATIVE(XBlackPixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XBlackPixel_FUNC);
	rc = (jint)XBlackPixel((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XBlackPixel_FUNC);
	return rc;
}
#endif

#ifndef NO_XChangeActivePointerGrab
JNIEXPORT jint JNICALL OS_NATIVE(XChangeActivePointerGrab)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XChangeActivePointerGrab_FUNC);
	rc = (jint)XChangeActivePointerGrab((Display *)arg0, arg1, (Cursor)arg2, (Time)arg3);
	OS_NATIVE_EXIT(env, that, XChangeActivePointerGrab_FUNC);
	return rc;
}
#endif

#ifndef NO_XChangeProperty
JNIEXPORT void JNICALL OS_NATIVE(XChangeProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jint arg7)
{
	jint *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, XChangeProperty_FUNC);
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	XChangeProperty((Display *)arg0, (Window)arg1, (Atom)arg2, (Atom)arg3, arg4, arg5, (unsigned char *)lparg6, arg7);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, XChangeProperty_FUNC);
}
#endif

#ifndef NO_XChangeWindowAttributes
JNIEXPORT void JNICALL OS_NATIVE(XChangeWindowAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	XSetWindowAttributes _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, XChangeWindowAttributes_FUNC);
	if (arg3) if ((lparg3 = getXSetWindowAttributesFields(env, arg3, &_arg3)) == NULL) goto fail;
	XChangeWindowAttributes((Display *)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setXSetWindowAttributesFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, XChangeWindowAttributes_FUNC);
}
#endif

#ifndef NO_XCheckIfEvent
JNIEXPORT jint JNICALL OS_NATIVE(XCheckIfEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XCheckIfEvent_FUNC);
	rc = (jint)XCheckIfEvent((Display *)arg0, (XEvent *)arg1, (Bool (*)())arg2, (XPointer)arg3);
	OS_NATIVE_EXIT(env, that, XCheckIfEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_XCheckMaskEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(XCheckMaskEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XCheckMaskEvent_FUNC);
	rc = (jboolean)XCheckMaskEvent((Display *)arg0, arg1, (XEvent *)arg2);
	OS_NATIVE_EXIT(env, that, XCheckMaskEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_XCheckWindowEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(XCheckWindowEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XCheckWindowEvent_FUNC);
	rc = (jboolean)XCheckWindowEvent((Display *)arg0, (Window)arg1, arg2, (XEvent *)arg3);
	OS_NATIVE_EXIT(env, that, XCheckWindowEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_XClearArea
JNIEXPORT void JNICALL OS_NATIVE(XClearArea)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jboolean arg6)
{
	OS_NATIVE_ENTER(env, that, XClearArea_FUNC);
	XClearArea((Display *)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, XClearArea_FUNC);
}
#endif

#ifndef NO_XClipBox
JNIEXPORT void JNICALL OS_NATIVE(XClipBox)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	XRectangle _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, XClipBox_FUNC);
	if (arg1) if ((lparg1 = getXRectangleFields(env, arg1, &_arg1)) == NULL) goto fail;
	XClipBox((Region)arg0, (XRectangle *)lparg1);
fail:
	if (arg1 && lparg1) setXRectangleFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, XClipBox_FUNC);
}
#endif

#ifndef NO_XCloseDisplay
JNIEXPORT void JNICALL OS_NATIVE(XCloseDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XCloseDisplay_FUNC);
	XCloseDisplay((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XCloseDisplay_FUNC);
}
#endif

#ifndef NO_XCopyArea
JNIEXPORT void JNICALL OS_NATIVE(XCopyArea)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	OS_NATIVE_ENTER(env, that, XCopyArea_FUNC);
	XCopyArea((Display *)arg0, arg1, arg2, (GC)arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	OS_NATIVE_EXIT(env, that, XCopyArea_FUNC);
}
#endif

#ifndef NO_XCopyPlane
JNIEXPORT void JNICALL OS_NATIVE(XCopyPlane)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	OS_NATIVE_ENTER(env, that, XCopyPlane_FUNC);
	XCopyPlane((Display *)arg0, arg1, arg2, (GC)arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	OS_NATIVE_EXIT(env, that, XCopyPlane_FUNC);
}
#endif

#ifndef NO_XCreateBitmapFromData
JNIEXPORT jint JNICALL OS_NATIVE(XCreateBitmapFromData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XCreateBitmapFromData_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XCreateBitmapFromData((Display *)arg0, arg1, (char *)lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XCreateBitmapFromData_FUNC);
	return rc;
}
#endif

#ifndef NO_XCreateFontCursor
JNIEXPORT jint JNICALL OS_NATIVE(XCreateFontCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XCreateFontCursor_FUNC);
	rc = (jint)XCreateFontCursor((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XCreateFontCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_XCreateGC
JNIEXPORT jint JNICALL OS_NATIVE(XCreateGC)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	XGCValues _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XCreateGC_FUNC);
	if (arg3) if ((lparg3 = getXGCValuesFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)XCreateGC((Display *)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setXGCValuesFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, XCreateGC_FUNC);
	return rc;
}
#endif

#ifndef NO_XCreateImage
JNIEXPORT jint JNICALL OS_NATIVE(XCreateImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XCreateImage_FUNC);
	rc = (jint)XCreateImage((Display *)arg0, (Visual *)arg1, arg2, arg3, arg4, (char *)arg5, arg6, arg7, arg8, arg9);
	OS_NATIVE_EXIT(env, that, XCreateImage_FUNC);
	return rc;
}
#endif

#ifndef NO_XCreatePixmap
JNIEXPORT jint JNICALL OS_NATIVE(XCreatePixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XCreatePixmap_FUNC);
	rc = (jint)XCreatePixmap((Display *)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, XCreatePixmap_FUNC);
	return rc;
}
#endif

#ifndef NO_XCreatePixmapCursor
JNIEXPORT jint JNICALL OS_NATIVE(XCreatePixmapCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jint arg5, jint arg6)
{
	XColor _arg3, *lparg3=NULL;
	XColor _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XCreatePixmapCursor_FUNC);
	if (arg3) if ((lparg3 = getXColorFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getXColorFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)XCreatePixmapCursor((Display *)arg0, (Pixmap)arg1, (Pixmap)arg2, lparg3, lparg4, arg5, arg6);
fail:
	if (arg4 && lparg4) setXColorFields(env, arg4, lparg4);
	if (arg3 && lparg3) setXColorFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, XCreatePixmapCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_XCreateRegion
JNIEXPORT jint JNICALL OS_NATIVE(XCreateRegion)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XCreateRegion_FUNC);
	rc = (jint)XCreateRegion();
	OS_NATIVE_EXIT(env, that, XCreateRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_XCreateWindow
JNIEXPORT jint JNICALL OS_NATIVE(XCreateWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jlong arg10, jobject arg11)
{
	XSetWindowAttributes _arg11, *lparg11=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XCreateWindow_FUNC);
	if (arg11) if ((lparg11 = getXSetWindowAttributesFields(env, arg11, &_arg11)) == NULL) goto fail;
	rc = (jint)XCreateWindow((Display *)arg0, (Window)arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, (Visual *)arg9, arg10, (XSetWindowAttributes *)lparg11);
fail:
	if (arg11 && lparg11) setXSetWindowAttributesFields(env, arg11, lparg11);
	OS_NATIVE_EXIT(env, that, XCreateWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_XDefaultColormap
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultColormap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDefaultColormap_FUNC);
	rc = (jint)XDefaultColormap((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XDefaultColormap_FUNC);
	return rc;
}
#endif

#ifndef NO_XDefaultColormapOfScreen
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultColormapOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDefaultColormapOfScreen_FUNC);
	rc = (jint)XDefaultColormapOfScreen((Screen *)arg0);
	OS_NATIVE_EXIT(env, that, XDefaultColormapOfScreen_FUNC);
	return rc;
}
#endif

#ifndef NO_XDefaultDepthOfScreen
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultDepthOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDefaultDepthOfScreen_FUNC);
	rc = (jint)XDefaultDepthOfScreen((Screen *)arg0);
	OS_NATIVE_EXIT(env, that, XDefaultDepthOfScreen_FUNC);
	return rc;
}
#endif

#ifndef NO_XDefaultGCOfScreen
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultGCOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDefaultGCOfScreen_FUNC);
	rc = (jint)XDefaultGCOfScreen((Screen *)arg0);
	OS_NATIVE_EXIT(env, that, XDefaultGCOfScreen_FUNC);
	return rc;
}
#endif

#ifndef NO_XDefaultRootWindow
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultRootWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDefaultRootWindow_FUNC);
	rc = (jint)XDefaultRootWindow((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XDefaultRootWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_XDefaultScreen
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDefaultScreen_FUNC);
	rc = (jint)XDefaultScreen((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XDefaultScreen_FUNC);
	return rc;
}
#endif

#ifndef NO_XDefaultScreenOfDisplay
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultScreenOfDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDefaultScreenOfDisplay_FUNC);
	rc = (jint)XDefaultScreenOfDisplay((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XDefaultScreenOfDisplay_FUNC);
	return rc;
}
#endif

#ifndef NO_XDefaultVisual
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultVisual)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDefaultVisual_FUNC);
	rc = (jint)XDefaultVisual((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XDefaultVisual_FUNC);
	return rc;
}
#endif

#ifndef NO_XDefineCursor
JNIEXPORT void JNICALL OS_NATIVE(XDefineCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XDefineCursor_FUNC);
	XDefineCursor((Display *)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, XDefineCursor_FUNC);
}
#endif

#ifndef NO_XDestroyImage
JNIEXPORT jint JNICALL OS_NATIVE(XDestroyImage)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDestroyImage_FUNC);
	rc = (jint)XDestroyImage((XImage *)arg0);
	OS_NATIVE_EXIT(env, that, XDestroyImage_FUNC);
	return rc;
}
#endif

#ifndef NO_XDestroyRegion
JNIEXPORT void JNICALL OS_NATIVE(XDestroyRegion)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XDestroyRegion_FUNC);
	XDestroyRegion((Region)arg0);
	OS_NATIVE_EXIT(env, that, XDestroyRegion_FUNC);
}
#endif

#ifndef NO_XDestroyWindow
JNIEXPORT void JNICALL OS_NATIVE(XDestroyWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XDestroyWindow_FUNC);
	XDestroyWindow((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, XDestroyWindow_FUNC);
}
#endif

#ifndef NO_XDisplayHeight
JNIEXPORT jint JNICALL OS_NATIVE(XDisplayHeight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDisplayHeight_FUNC);
	rc = (jint)XDisplayHeight((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XDisplayHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_XDisplayHeightMM
JNIEXPORT jint JNICALL OS_NATIVE(XDisplayHeightMM)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDisplayHeightMM_FUNC);
	rc = (jint)XDisplayHeightMM((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XDisplayHeightMM_FUNC);
	return rc;
}
#endif

#ifndef NO_XDisplayWidth
JNIEXPORT jint JNICALL OS_NATIVE(XDisplayWidth)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDisplayWidth_FUNC);
	rc = (jint)XDisplayWidth((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XDisplayWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_XDisplayWidthMM
JNIEXPORT jint JNICALL OS_NATIVE(XDisplayWidthMM)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XDisplayWidthMM_FUNC);
	rc = (jint)XDisplayWidthMM((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XDisplayWidthMM_FUNC);
	return rc;
}
#endif

#ifndef NO_XDrawArc
JNIEXPORT void JNICALL OS_NATIVE(XDrawArc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	OS_NATIVE_ENTER(env, that, XDrawArc_FUNC);
	XDrawArc((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, XDrawArc_FUNC);
}
#endif

#ifndef NO_XDrawLine
JNIEXPORT void JNICALL OS_NATIVE(XDrawLine)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	OS_NATIVE_ENTER(env, that, XDrawLine_FUNC);
	XDrawLine((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, XDrawLine_FUNC);
}
#endif

#ifndef NO_XDrawLines
JNIEXPORT void JNICALL OS_NATIVE(XDrawLines)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3, jint arg4, jint arg5)
{
	jshort *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, XDrawLines_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	XDrawLines((Display *)arg0, (Drawable)arg1, (GC)arg2, (XPoint *)lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, XDrawLines_FUNC);
}
#endif

#ifndef NO_XDrawPoint
JNIEXPORT void JNICALL OS_NATIVE(XDrawPoint)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, XDrawPoint_FUNC);
	XDrawPoint((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, XDrawPoint_FUNC);
}
#endif

#ifndef NO_XDrawRectangle
JNIEXPORT void JNICALL OS_NATIVE(XDrawRectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	OS_NATIVE_ENTER(env, that, XDrawRectangle_FUNC);
	XDrawRectangle((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, XDrawRectangle_FUNC);
}
#endif

#ifndef NO_XEmptyRegion
JNIEXPORT jboolean JNICALL OS_NATIVE(XEmptyRegion)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XEmptyRegion_FUNC);
	rc = (jboolean)XEmptyRegion((Region)arg0);
	OS_NATIVE_EXIT(env, that, XEmptyRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_XEventsQueued
JNIEXPORT jint JNICALL OS_NATIVE(XEventsQueued)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XEventsQueued_FUNC);
	rc = (jint)XEventsQueued((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XEventsQueued_FUNC);
	return rc;
}
#endif

#ifndef NO_XFillArc
JNIEXPORT void JNICALL OS_NATIVE(XFillArc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	OS_NATIVE_ENTER(env, that, XFillArc_FUNC);
	XFillArc((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, XFillArc_FUNC);
}
#endif

#ifndef NO_XFillPolygon
JNIEXPORT jint JNICALL OS_NATIVE(XFillPolygon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3, jint arg4, jint arg5, jint arg6)
{
	jshort *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XFillPolygon_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XFillPolygon((Display *)arg0, (Drawable)arg1, (GC)arg2, (XPoint *)lparg3, arg4, arg5, arg6);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, XFillPolygon_FUNC);
	return rc;
}
#endif

#ifndef NO_XFillRectangle
JNIEXPORT void JNICALL OS_NATIVE(XFillRectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	OS_NATIVE_ENTER(env, that, XFillRectangle_FUNC);
	XFillRectangle((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, XFillRectangle_FUNC);
}
#endif

#ifndef NO_XFilterEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(XFilterEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XFilterEvent_FUNC);
	rc = (jboolean)XFilterEvent((XEvent *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, XFilterEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_XFlush
JNIEXPORT void JNICALL OS_NATIVE(XFlush)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XFlush_FUNC);
	XFlush((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XFlush_FUNC);
}
#endif

#ifndef NO_XFontsOfFontSet
JNIEXPORT jint JNICALL OS_NATIVE(XFontsOfFontSet)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XFontsOfFontSet_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XFontsOfFontSet((XFontSet)arg0, (XFontStruct ***)lparg1, (char ***)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XFontsOfFontSet_FUNC);
	return rc;
}
#endif

#ifndef NO_XFree
JNIEXPORT jint JNICALL OS_NATIVE(XFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XFree_FUNC);
	rc = (jint)XFree((char *)arg0);
	OS_NATIVE_EXIT(env, that, XFree_FUNC);
	return rc;
}
#endif

#ifndef NO_XFreeColors
JNIEXPORT jint JNICALL OS_NATIVE(XFreeColors)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3, jint arg4)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XFreeColors_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XFreeColors((Display *)arg0, arg1, (unsigned long *)lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XFreeColors_FUNC);
	return rc;
}
#endif

#ifndef NO_XFreeCursor
JNIEXPORT void JNICALL OS_NATIVE(XFreeCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XFreeCursor_FUNC);
	XFreeCursor((Display *)arg0, (Cursor)arg1);
	OS_NATIVE_EXIT(env, that, XFreeCursor_FUNC);
}
#endif

#ifndef NO_XFreeFont
JNIEXPORT void JNICALL OS_NATIVE(XFreeFont)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XFreeFont_FUNC);
	XFreeFont((Display *)arg0, (XFontStruct *)arg1);
	OS_NATIVE_EXIT(env, that, XFreeFont_FUNC);
}
#endif

#ifndef NO_XFreeFontNames
JNIEXPORT void JNICALL OS_NATIVE(XFreeFontNames)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XFreeFontNames_FUNC);
	XFreeFontNames((char **)arg0);
	OS_NATIVE_EXIT(env, that, XFreeFontNames_FUNC);
}
#endif

#ifndef NO_XFreeGC
JNIEXPORT void JNICALL OS_NATIVE(XFreeGC)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XFreeGC_FUNC);
	XFreeGC((Display *)arg0, (GC)arg1);
	OS_NATIVE_EXIT(env, that, XFreeGC_FUNC);
}
#endif

#ifndef NO_XFreePixmap
JNIEXPORT void JNICALL OS_NATIVE(XFreePixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XFreePixmap_FUNC);
	XFreePixmap((Display *)arg0, (Pixmap)arg1);
	OS_NATIVE_EXIT(env, that, XFreePixmap_FUNC);
}
#endif

#ifndef NO_XFreeStringList
JNIEXPORT void JNICALL OS_NATIVE(XFreeStringList)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XFreeStringList_FUNC);
	XFreeStringList((char **)arg0);
	OS_NATIVE_EXIT(env, that, XFreeStringList_FUNC);
}
#endif

#ifndef NO_XGetGCValues
JNIEXPORT jint JNICALL OS_NATIVE(XGetGCValues)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	XGCValues _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XGetGCValues_FUNC);
	if (arg3) if ((lparg3 = getXGCValuesFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)XGetGCValues((Display *)arg0, (GC)arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setXGCValuesFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, XGetGCValues_FUNC);
	return rc;
}
#endif

#ifndef NO_XGetGeometry
JNIEXPORT jint JNICALL OS_NATIVE(XGetGeometry)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6, jintArray arg7, jintArray arg8)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XGetGeometry_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)XGetGeometry((Display *)arg0, (Drawable)arg1, (Window *)lparg2, (int *)lparg3, (int *)lparg4, (unsigned int *)lparg5, (unsigned int *)lparg6, (unsigned int *)lparg7, (unsigned int *)lparg8);
fail:
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XGetGeometry_FUNC);
	return rc;
}
#endif

#ifndef NO_XGetIconSizes
JNIEXPORT jint JNICALL OS_NATIVE(XGetIconSizes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XGetIconSizes_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XGetIconSizes((Display *)arg0, (Window)arg1, (XIconSize **)lparg2, (int *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XGetIconSizes_FUNC);
	return rc;
}
#endif

#ifndef NO_XGetImage
JNIEXPORT jint JNICALL OS_NATIVE(XGetImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XGetImage_FUNC);
	rc = (jint)XGetImage((Display *)arg0, (Drawable)arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	OS_NATIVE_EXIT(env, that, XGetImage_FUNC);
	return rc;
}
#endif

#ifndef NO_XGetInputFocus
JNIEXPORT jint JNICALL OS_NATIVE(XGetInputFocus)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XGetInputFocus_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XGetInputFocus((Display *)arg0, (Window *)lparg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XGetInputFocus_FUNC);
	return rc;
}
#endif

#ifndef NO_XGetModifierMapping
JNIEXPORT jint JNICALL OS_NATIVE(XGetModifierMapping)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XGetModifierMapping_FUNC);
	rc = (jint)XGetModifierMapping((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XGetModifierMapping_FUNC);
	return rc;
}
#endif

#ifndef NO_XGetWindowAttributes
JNIEXPORT jboolean JNICALL OS_NATIVE(XGetWindowAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	XWindowAttributes _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XGetWindowAttributes_FUNC);
	if (arg2) if ((lparg2 = getXWindowAttributesFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)XGetWindowAttributes((Display *)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setXWindowAttributesFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, XGetWindowAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_XGetWindowProperty
JNIEXPORT jint JNICALL OS_NATIVE(XGetWindowProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jboolean arg5, jint arg6, jintArray arg7, jintArray arg8, jintArray arg9, jintArray arg10, jintArray arg11)
{
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint *lparg9=NULL;
	jint *lparg10=NULL;
	jint *lparg11=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XGetWindowProperty_FUNC);
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	if (arg9) if ((lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL)) == NULL) goto fail;
	if (arg10) if ((lparg10 = (*env)->GetIntArrayElements(env, arg10, NULL)) == NULL) goto fail;
	if (arg11) if ((lparg11 = (*env)->GetIntArrayElements(env, arg11, NULL)) == NULL) goto fail;
	rc = (jint)XGetWindowProperty((Display *)arg0, (Window)arg1, (Atom)arg2, arg3, arg4, (Bool)arg5, (Atom)arg6, (Atom *)lparg7, (int *)lparg8, (unsigned long *)lparg9, (unsigned long *)lparg10, (unsigned char **)lparg11);
fail:
	if (arg11 && lparg11) (*env)->ReleaseIntArrayElements(env, arg11, lparg11, 0);
	if (arg10 && lparg10) (*env)->ReleaseIntArrayElements(env, arg10, lparg10, 0);
	if (arg9 && lparg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	OS_NATIVE_EXIT(env, that, XGetWindowProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_XGrabKeyboard
JNIEXPORT jint JNICALL OS_NATIVE(XGrabKeyboard)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XGrabKeyboard_FUNC);
	rc = (jint)XGrabKeyboard((Display *)arg0, arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, XGrabKeyboard_FUNC);
	return rc;
}
#endif

#ifndef NO_XGrabPointer
JNIEXPORT jint JNICALL OS_NATIVE(XGrabPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XGrabPointer_FUNC);
	rc = (jint)XGrabPointer((Display *)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, XGrabPointer_FUNC);
	return rc;
}
#endif

#ifndef NO_XInitThreads
JNIEXPORT jint JNICALL OS_NATIVE(XInitThreads)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XInitThreads_FUNC);
	rc = (jint)XInitThreads();
	OS_NATIVE_EXIT(env, that, XInitThreads_FUNC);
	return rc;
}
#endif

#ifndef NO_XInternAtom
JNIEXPORT jint JNICALL OS_NATIVE(XInternAtom)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jboolean arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XInternAtom_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XInternAtom((Display *)arg0, (char *)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XInternAtom_FUNC);
	return rc;
}
#endif

#ifndef NO_XIntersectRegion
JNIEXPORT void JNICALL OS_NATIVE(XIntersectRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XIntersectRegion_FUNC);
	XIntersectRegion((Region)arg0, (Region)arg1, (Region)arg2);
	OS_NATIVE_EXIT(env, that, XIntersectRegion_FUNC);
}
#endif

#ifndef NO_XKeysymToKeycode
JNIEXPORT jint JNICALL OS_NATIVE(XKeysymToKeycode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XKeysymToKeycode_FUNC);
	rc = (jint)XKeysymToKeycode((Display *)arg0, (KeySym)arg1);
	OS_NATIVE_EXIT(env, that, XKeysymToKeycode_FUNC);
	return rc;
}
#endif

#ifndef NO_XKeysymToString
JNIEXPORT jint JNICALL OS_NATIVE(XKeysymToString)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XKeysymToString_FUNC);
	rc = (jint)XKeysymToString(arg0);
	OS_NATIVE_EXIT(env, that, XKeysymToString_FUNC);
	return rc;
}
#endif

#ifndef NO_XListFonts
JNIEXPORT jint JNICALL OS_NATIVE(XListFonts)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XListFonts_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XListFonts((Display *)arg0, (char *)lparg1, arg2, (int *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XListFonts_FUNC);
	return rc;
}
#endif

#ifndef NO_XListProperties
JNIEXPORT jint JNICALL OS_NATIVE(XListProperties)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XListProperties_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XListProperties((Display *)arg0, (Window)arg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XListProperties_FUNC);
	return rc;
}
#endif

#ifndef NO_XLocaleOfFontSet
JNIEXPORT jint JNICALL OS_NATIVE(XLocaleOfFontSet)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XLocaleOfFontSet_FUNC);
	rc = (jint)XLocaleOfFontSet((XFontSet)arg0);
	OS_NATIVE_EXIT(env, that, XLocaleOfFontSet_FUNC);
	return rc;
}
#endif

#ifndef NO_XLookupString
JNIEXPORT jint JNICALL OS_NATIVE(XLookupString)
	(JNIEnv *env, jclass that, jobject arg0, jbyteArray arg1, jint arg2, jintArray arg3, jintArray arg4)
{
	XKeyEvent _arg0, *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XLookupString_FUNC);
	if (arg0) if ((lparg0 = getXKeyEventFields(env, arg0, &_arg0)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XLookupString((XKeyEvent *)lparg0, (char *)lparg1, arg2, (KeySym *)lparg3, (XComposeStatus *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) setXKeyEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, XLookupString_FUNC);
	return rc;
}
#endif

#ifndef NO_XLowerWindow
JNIEXPORT jint JNICALL OS_NATIVE(XLowerWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XLowerWindow_FUNC);
	rc = (jint)XLowerWindow((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, XLowerWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_XMapWindow
JNIEXPORT void JNICALL OS_NATIVE(XMapWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XMapWindow_FUNC);
	XMapWindow((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, XMapWindow_FUNC);
}
#endif

#ifndef NO_XMoveResizeWindow
JNIEXPORT void JNICALL OS_NATIVE(XMoveResizeWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, XMoveResizeWindow_FUNC);
	XMoveResizeWindow((Display *)arg0, (Window)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, XMoveResizeWindow_FUNC);
}
#endif

#ifndef NO_XOffsetRegion
JNIEXPORT jint JNICALL OS_NATIVE(XOffsetRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XOffsetRegion_FUNC);
	rc = (jint)XOffsetRegion((Region)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, XOffsetRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_XOpenDisplay
JNIEXPORT jint JNICALL OS_NATIVE(XOpenDisplay)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XOpenDisplay_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)XOpenDisplay((char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, XOpenDisplay_FUNC);
	return rc;
}
#endif

#ifndef NO_XPointInRegion
JNIEXPORT jboolean JNICALL OS_NATIVE(XPointInRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XPointInRegion_FUNC);
	rc = (jboolean)XPointInRegion((Region)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, XPointInRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_XPolygonRegion
JNIEXPORT jint JNICALL OS_NATIVE(XPolygonRegion)
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1, jint arg2)
{
	jshort *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XPolygonRegion_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)XPolygonRegion((XPoint *)lparg0, arg1, arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, XPolygonRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_XPutImage
JNIEXPORT jint JNICALL OS_NATIVE(XPutImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XPutImage_FUNC);
	rc = (jint)XPutImage((Display *)arg0, (Drawable)arg1, (GC)arg2, (XImage *)arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	OS_NATIVE_EXIT(env, that, XPutImage_FUNC);
	return rc;
}
#endif

#ifndef NO_XQueryBestCursor
JNIEXPORT jint JNICALL OS_NATIVE(XQueryBestCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XQueryBestCursor_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)XQueryBestCursor((Display *)arg0, arg1, arg2, arg3, (unsigned int *)lparg4, (unsigned int *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, XQueryBestCursor_FUNC);
	return rc;
}
#endif

#ifndef NO_XQueryColor
JNIEXPORT jint JNICALL OS_NATIVE(XQueryColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	XColor _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XQueryColor_FUNC);
	if (arg2) if ((lparg2 = getXColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)XQueryColor((Display *)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setXColorFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, XQueryColor_FUNC);
	return rc;
}
#endif

#ifndef NO_XQueryPointer
JNIEXPORT jint JNICALL OS_NATIVE(XQueryPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5, jintArray arg6, jintArray arg7, jintArray arg8)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XQueryPointer_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	if (arg8) if ((lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL)) == NULL) goto fail;
	rc = (jint)XQueryPointer((Display *)arg0, (Window)arg1, (Window *)lparg2, (Window *)lparg3, (int *)lparg4, (int *)lparg5, (int *)lparg6, (int *)lparg7, (unsigned int *)lparg8);
fail:
	if (arg8 && lparg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XQueryPointer_FUNC);
	return rc;
}
#endif

#ifndef NO_XQueryTree
JNIEXPORT jint JNICALL OS_NATIVE(XQueryTree)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XQueryTree_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)XQueryTree((Display *)arg0, (Window)arg1, (Window *)lparg2, (Window *)lparg3, (Window **)lparg4, (unsigned int *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XQueryTree_FUNC);
	return rc;
}
#endif

#ifndef NO_XRaiseWindow
JNIEXPORT jint JNICALL OS_NATIVE(XRaiseWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XRaiseWindow_FUNC);
	rc = (jint)XRaiseWindow((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, XRaiseWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_XReconfigureWMWindow
JNIEXPORT jint JNICALL OS_NATIVE(XReconfigureWMWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	XWindowChanges _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XReconfigureWMWindow_FUNC);
	if (arg4) if ((lparg4 = getXWindowChangesFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)XReconfigureWMWindow((Display *)arg0, (Window)arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) setXWindowChangesFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, XReconfigureWMWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_XRectInRegion
JNIEXPORT jint JNICALL OS_NATIVE(XRectInRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XRectInRegion_FUNC);
	rc = (jint)XRectInRegion((Region)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, XRectInRegion_FUNC);
	return rc;
}
#endif

#ifndef NO_XReparentWindow
JNIEXPORT jint JNICALL OS_NATIVE(XReparentWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XReparentWindow_FUNC);
	rc = (jint)XReparentWindow((Display *)arg0, (Window)arg1, (Window)arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, XReparentWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_XResizeWindow
JNIEXPORT void JNICALL OS_NATIVE(XResizeWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, XResizeWindow_FUNC);
	XResizeWindow((Display *)arg0, (Window)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, XResizeWindow_FUNC);
}
#endif

#ifndef NO_XRootWindowOfScreen
JNIEXPORT jint JNICALL OS_NATIVE(XRootWindowOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XRootWindowOfScreen_FUNC);
	rc = (jint)XRootWindowOfScreen((Screen *)arg0);
	OS_NATIVE_EXIT(env, that, XRootWindowOfScreen_FUNC);
	return rc;
}
#endif

#ifndef NO_XSelectInput
JNIEXPORT void JNICALL OS_NATIVE(XSelectInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XSelectInput_FUNC);
	XSelectInput((Display *)arg0, (Window)arg1, arg2);
	OS_NATIVE_EXIT(env, that, XSelectInput_FUNC);
}
#endif

#ifndef NO_XSendEvent
JNIEXPORT jint JNICALL OS_NATIVE(XSendEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XSendEvent_FUNC);
	rc = (jint)XSendEvent((Display *)arg0, (Window)arg1, (Bool)arg2, (long)arg3, (XEvent *)arg4);
	OS_NATIVE_EXIT(env, that, XSendEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_XSetBackground
JNIEXPORT void JNICALL OS_NATIVE(XSetBackground)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XSetBackground_FUNC);
	XSetBackground((Display *)arg0, (GC)arg1, arg2);
	OS_NATIVE_EXIT(env, that, XSetBackground_FUNC);
}
#endif

#ifndef NO_XSetClipMask
JNIEXPORT void JNICALL OS_NATIVE(XSetClipMask)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XSetClipMask_FUNC);
	XSetClipMask((Display *)arg0, (GC)arg1, (Pixmap)arg2);
	OS_NATIVE_EXIT(env, that, XSetClipMask_FUNC);
}
#endif

#ifndef NO_XSetClipRectangles
JNIEXPORT void JNICALL OS_NATIVE(XSetClipRectangles)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jint arg6)
{
	XRectangle _arg4, *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, XSetClipRectangles_FUNC);
	if (arg4) if ((lparg4 = getXRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	XSetClipRectangles((Display *)arg0, (GC)arg1, arg2, arg3, (XRectangle *)lparg4, arg5, arg6);
fail:
	if (arg4 && lparg4) setXRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, XSetClipRectangles_FUNC);
}
#endif

#ifndef NO_XSetDashes
JNIEXPORT jint JNICALL OS_NATIVE(XSetDashes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XSetDashes_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XSetDashes((Display *)arg0, (GC)arg1, arg2, (char *)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, XSetDashes_FUNC);
	return rc;
}
#endif

#ifndef NO_XSetErrorHandler
JNIEXPORT jint JNICALL OS_NATIVE(XSetErrorHandler)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XSetErrorHandler_FUNC);
	rc = (jint)XSetErrorHandler((XErrorHandler)arg0);
	OS_NATIVE_EXIT(env, that, XSetErrorHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_XSetFillStyle
JNIEXPORT void JNICALL OS_NATIVE(XSetFillStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XSetFillStyle_FUNC);
	XSetFillStyle((Display *)arg0, (GC)arg1, arg2);
	OS_NATIVE_EXIT(env, that, XSetFillStyle_FUNC);
}
#endif

#ifndef NO_XSetForeground
JNIEXPORT void JNICALL OS_NATIVE(XSetForeground)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XSetForeground_FUNC);
	XSetForeground((Display *)arg0, (GC)arg1, arg2);
	OS_NATIVE_EXIT(env, that, XSetForeground_FUNC);
}
#endif

#ifndef NO_XSetFunction
JNIEXPORT void JNICALL OS_NATIVE(XSetFunction)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XSetFunction_FUNC);
	XSetFunction((Display *)arg0, (GC)arg1, arg2);
	OS_NATIVE_EXIT(env, that, XSetFunction_FUNC);
}
#endif

#ifndef NO_XSetGraphicsExposures
JNIEXPORT void JNICALL OS_NATIVE(XSetGraphicsExposures)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, XSetGraphicsExposures_FUNC);
	XSetGraphicsExposures((Display *)arg0, (GC)arg1, (Bool)arg2);
	OS_NATIVE_EXIT(env, that, XSetGraphicsExposures_FUNC);
}
#endif

#ifndef NO_XSetIOErrorHandler
JNIEXPORT jint JNICALL OS_NATIVE(XSetIOErrorHandler)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XSetIOErrorHandler_FUNC);
	rc = (jint)XSetIOErrorHandler((XIOErrorHandler)arg0);
	OS_NATIVE_EXIT(env, that, XSetIOErrorHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_XSetInputFocus
JNIEXPORT jint JNICALL OS_NATIVE(XSetInputFocus)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XSetInputFocus_FUNC);
	rc = (jint)XSetInputFocus((Display *)arg0, (Window)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, XSetInputFocus_FUNC);
	return rc;
}
#endif

#ifndef NO_XSetLineAttributes
JNIEXPORT jint JNICALL OS_NATIVE(XSetLineAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XSetLineAttributes_FUNC);
	rc = (jint)XSetLineAttributes((Display *)arg0, (GC)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, XSetLineAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO_XSetRegion
JNIEXPORT void JNICALL OS_NATIVE(XSetRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XSetRegion_FUNC);
	XSetRegion((Display *)arg0, (GC)arg1, (Region)arg2);
	OS_NATIVE_EXIT(env, that, XSetRegion_FUNC);
}
#endif

#ifndef NO_XSetStipple
JNIEXPORT void JNICALL OS_NATIVE(XSetStipple)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XSetStipple_FUNC);
	XSetStipple((Display *)arg0, (GC)arg1, (Pixmap)arg2);
	OS_NATIVE_EXIT(env, that, XSetStipple_FUNC);
}
#endif

#ifndef NO_XSetSubwindowMode
JNIEXPORT void JNICALL OS_NATIVE(XSetSubwindowMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XSetSubwindowMode_FUNC);
	XSetSubwindowMode((Display *)arg0, (GC)arg1, arg2);
	OS_NATIVE_EXIT(env, that, XSetSubwindowMode_FUNC);
}
#endif

#ifndef NO_XSetWMNormalHints
JNIEXPORT void JNICALL OS_NATIVE(XSetWMNormalHints)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	XSizeHints _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, XSetWMNormalHints_FUNC);
	if (arg2) if ((lparg2 = getXSizeHintsFields(env, arg2, &_arg2)) == NULL) goto fail;
	XSetWMNormalHints((Display *)arg0, (Window)arg1, lparg2);
fail:
	if (arg2 && lparg2) setXSizeHintsFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, XSetWMNormalHints_FUNC);
}
#endif

#ifndef NO_XShapeCombineMask
JNIEXPORT void JNICALL OS_NATIVE(XShapeCombineMask)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	OS_NATIVE_ENTER(env, that, XShapeCombineMask_FUNC);
	XShapeCombineMask((Display *)arg0, (Window)arg1, arg2, arg3, arg4, (Pixmap)arg5, arg6);
	OS_NATIVE_EXIT(env, that, XShapeCombineMask_FUNC);
}
#endif

#ifndef NO_XShapeCombineRegion
JNIEXPORT void JNICALL OS_NATIVE(XShapeCombineRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	OS_NATIVE_ENTER(env, that, XShapeCombineRegion_FUNC);
	XShapeCombineRegion((Display *)arg0, (Window)arg1, arg2, arg3, arg4, (Region)arg5, arg6);
	OS_NATIVE_EXIT(env, that, XShapeCombineRegion_FUNC);
}
#endif

#ifndef NO_XSubtractRegion
JNIEXPORT void JNICALL OS_NATIVE(XSubtractRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XSubtractRegion_FUNC);
	XSubtractRegion((Region)arg0, (Region)arg1, (Region)arg2);
	OS_NATIVE_EXIT(env, that, XSubtractRegion_FUNC);
}
#endif

#ifndef NO_XSync
JNIEXPORT void JNICALL OS_NATIVE(XSync)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, XSync_FUNC);
	XSync((Display *)arg0, (Bool)arg1);
	OS_NATIVE_EXIT(env, that, XSync_FUNC);
}
#endif

#ifndef NO_XSynchronize
JNIEXPORT jint JNICALL OS_NATIVE(XSynchronize)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XSynchronize_FUNC);
	rc = (jint)XSynchronize((Display *)arg0, (Bool)arg1);
	OS_NATIVE_EXIT(env, that, XSynchronize_FUNC);
	return rc;
}
#endif

#ifndef NO_XTestFakeButtonEvent
JNIEXPORT void JNICALL OS_NATIVE(XTestFakeButtonEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, XTestFakeButtonEvent_FUNC);
	XTestFakeButtonEvent((Display *)arg0, arg1, (Bool)arg2, (unsigned long)arg3);
	OS_NATIVE_EXIT(env, that, XTestFakeButtonEvent_FUNC);
}
#endif

#ifndef NO_XTestFakeKeyEvent
JNIEXPORT void JNICALL OS_NATIVE(XTestFakeKeyEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, XTestFakeKeyEvent_FUNC);
	XTestFakeKeyEvent((Display *)arg0, arg1, (Bool)arg2, (unsigned long)arg3);
	OS_NATIVE_EXIT(env, that, XTestFakeKeyEvent_FUNC);
}
#endif

#ifndef NO_XTestFakeMotionEvent
JNIEXPORT void JNICALL OS_NATIVE(XTestFakeMotionEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, XTestFakeMotionEvent_FUNC);
	XTestFakeMotionEvent((Display *)arg0, arg1, arg2, arg3, (unsigned long)arg4);
	OS_NATIVE_EXIT(env, that, XTestFakeMotionEvent_FUNC);
}
#endif

#ifndef NO_XTranslateCoordinates
JNIEXPORT jboolean JNICALL OS_NATIVE(XTranslateCoordinates)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6, jintArray arg7)
{
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XTranslateCoordinates_FUNC);
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jboolean)XTranslateCoordinates((Display *)arg0, (Window)arg1, (Window)arg2, arg3, arg4, lparg5, lparg6, (Window *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	OS_NATIVE_EXIT(env, that, XTranslateCoordinates_FUNC);
	return rc;
}
#endif

#ifndef NO_XUndefineCursor
JNIEXPORT void JNICALL OS_NATIVE(XUndefineCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XUndefineCursor_FUNC);
	XUndefineCursor((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, XUndefineCursor_FUNC);
}
#endif

#ifndef NO_XUngrabKeyboard
JNIEXPORT jint JNICALL OS_NATIVE(XUngrabKeyboard)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XUngrabKeyboard_FUNC);
	rc = (jint)XUngrabKeyboard((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XUngrabKeyboard_FUNC);
	return rc;
}
#endif

#ifndef NO_XUngrabPointer
JNIEXPORT jint JNICALL OS_NATIVE(XUngrabPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XUngrabPointer_FUNC);
	rc = (jint)XUngrabPointer((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XUngrabPointer_FUNC);
	return rc;
}
#endif

#ifndef NO_XUnionRectWithRegion
JNIEXPORT void JNICALL OS_NATIVE(XUnionRectWithRegion)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XRectangle _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, XUnionRectWithRegion_FUNC);
	if (arg0) if ((lparg0 = getXRectangleFields(env, arg0, &_arg0)) == NULL) goto fail;
	XUnionRectWithRegion((XRectangle *)lparg0, (Region)arg1, (Region)arg2);
fail:
	if (arg0 && lparg0) setXRectangleFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, XUnionRectWithRegion_FUNC);
}
#endif

#ifndef NO_XUnionRegion
JNIEXPORT void JNICALL OS_NATIVE(XUnionRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XUnionRegion_FUNC);
	XUnionRegion((Region)arg0, (Region)arg1, (Region)arg2);
	OS_NATIVE_EXIT(env, that, XUnionRegion_FUNC);
}
#endif

#ifndef NO_XUnmapWindow
JNIEXPORT void JNICALL OS_NATIVE(XUnmapWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XUnmapWindow_FUNC);
	XUnmapWindow((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, XUnmapWindow_FUNC);
}
#endif

#ifndef NO_XWarpPointer
JNIEXPORT jint JNICALL OS_NATIVE(XWarpPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XWarpPointer_FUNC);
	rc = (jint)XWarpPointer((Display *)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, XWarpPointer_FUNC);
	return rc;
}
#endif

#ifndef NO_XWhitePixel
JNIEXPORT jint JNICALL OS_NATIVE(XWhitePixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XWhitePixel_FUNC);
	rc = (jint)XWhitePixel((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XWhitePixel_FUNC);
	return rc;
}
#endif

#ifndef NO_XWithdrawWindow
JNIEXPORT void JNICALL OS_NATIVE(XWithdrawWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XWithdrawWindow_FUNC);
	XWithdrawWindow((Display *)arg0, (Window)arg1, arg2);
	OS_NATIVE_EXIT(env, that, XWithdrawWindow_FUNC);
}
#endif

#ifndef NO_XineramaIsActive
JNIEXPORT jboolean JNICALL OS_NATIVE(XineramaIsActive)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XineramaIsActive_FUNC);
	rc = (jboolean)XineramaIsActive((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XineramaIsActive_FUNC);
	return rc;
}
#endif

#ifndef NO_XineramaQueryScreens
JNIEXPORT jint JNICALL OS_NATIVE(XineramaQueryScreens)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XineramaQueryScreens_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XineramaQueryScreens((Display *)arg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XineramaQueryScreens_FUNC);
	return rc;
}
#endif

#ifndef NO_XmAddWMProtocolCallback
JNIEXPORT void JNICALL OS_NATIVE(XmAddWMProtocolCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, XmAddWMProtocolCallback_FUNC);
	XmAddWMProtocolCallback((Widget)arg0, (Atom)arg1, (XtCallbackProc)arg2, (XtPointer)arg3);
	OS_NATIVE_EXIT(env, that, XmAddWMProtocolCallback_FUNC);
}
#endif

#ifndef NO_XmChangeColor
JNIEXPORT void JNICALL OS_NATIVE(XmChangeColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XmChangeColor_FUNC);
	XmChangeColor((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmChangeColor_FUNC);
}
#endif

#ifndef NO_XmClipboardCopy
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardCopy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jintArray arg7)
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmClipboardCopy_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)XmClipboardCopy((Display *)arg0, (Window)arg1, arg2, (char *)lparg3, (char *)lparg4, arg5, arg6, (void *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, XmClipboardCopy_FUNC);
	return rc;
}
#endif

#ifndef NO_XmClipboardEndCopy
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardEndCopy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmClipboardEndCopy_FUNC);
	rc = (jint)XmClipboardEndCopy((Display *)arg0, (Window)arg1, arg2);
	OS_NATIVE_EXIT(env, that, XmClipboardEndCopy_FUNC);
	return rc;
}
#endif

#ifndef NO_XmClipboardEndRetrieve
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardEndRetrieve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmClipboardEndRetrieve_FUNC);
	rc = (jint)XmClipboardEndRetrieve((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, XmClipboardEndRetrieve_FUNC);
	return rc;
}
#endif

#ifndef NO_XmClipboardInquireCount
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardInquireCount)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmClipboardInquireCount_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XmClipboardInquireCount((Display *)arg0, (Window)arg1, (int *)lparg2, (unsigned long *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XmClipboardInquireCount_FUNC);
	return rc;
}
#endif

#ifndef NO_XmClipboardInquireFormat
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardInquireFormat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jintArray arg5)
{
	jbyte *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmClipboardInquireFormat_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)XmClipboardInquireFormat((Display *)arg0, (Window)arg1, arg2, (char *)lparg3, arg4, (unsigned long *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, XmClipboardInquireFormat_FUNC);
	return rc;
}
#endif

#ifndef NO_XmClipboardInquireLength
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardInquireLength)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3)
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmClipboardInquireLength_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XmClipboardInquireLength((Display *)arg0, (Window)arg1, (char *)lparg2, (unsigned long *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XmClipboardInquireLength_FUNC);
	return rc;
}
#endif

#ifndef NO_XmClipboardRetrieve
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardRetrieve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jint arg4, jintArray arg5, jintArray arg6)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmClipboardRetrieve_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)XmClipboardRetrieve((Display *)arg0, (Window)arg1, (char *)lparg2, (char *)lparg3, arg4, (unsigned long *)lparg5, (long *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XmClipboardRetrieve_FUNC);
	return rc;
}
#endif

#ifndef NO_XmClipboardStartCopy
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardStartCopy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmClipboardStartCopy_FUNC);
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)XmClipboardStartCopy((Display *)arg0, (Window)arg1, (XmString)arg2, arg3, (Widget)arg4, (XmCutPasteProc)arg5, (long *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, XmClipboardStartCopy_FUNC);
	return rc;
}
#endif

#ifndef NO_XmClipboardStartRetrieve
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardStartRetrieve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmClipboardStartRetrieve_FUNC);
	rc = (jint)XmClipboardStartRetrieve((Display *)arg0, (Window)arg1, arg2);
	OS_NATIVE_EXIT(env, that, XmClipboardStartRetrieve_FUNC);
	return rc;
}
#endif

#ifndef NO_XmComboBoxAddItem
JNIEXPORT void JNICALL OS_NATIVE(XmComboBoxAddItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	OS_NATIVE_ENTER(env, that, XmComboBoxAddItem_FUNC);
	XmComboBoxAddItem((Widget)arg0, (XmString)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, XmComboBoxAddItem_FUNC);
}
#endif

#ifndef NO_XmComboBoxDeletePos
JNIEXPORT void JNICALL OS_NATIVE(XmComboBoxDeletePos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XmComboBoxDeletePos_FUNC);
	XmComboBoxDeletePos((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmComboBoxDeletePos_FUNC);
}
#endif

#ifndef NO_XmComboBoxSelectItem
JNIEXPORT void JNICALL OS_NATIVE(XmComboBoxSelectItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XmComboBoxSelectItem_FUNC);
	XmComboBoxSelectItem((Widget)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, XmComboBoxSelectItem_FUNC);
}
#endif

#ifndef NO_XmCreateArrowButton
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateArrowButton)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateArrowButton_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateArrowButton((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateArrowButton_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateCascadeButtonGadget
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateCascadeButtonGadget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateCascadeButtonGadget_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateCascadeButtonGadget((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateCascadeButtonGadget_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateComboBox
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateComboBox)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateComboBox_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateComboBox((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateComboBox_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateDialogShell
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateDialogShell)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateDialogShell_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateDialogShell((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateDialogShell_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateDrawingArea
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateDrawingArea)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateDrawingArea_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateDrawingArea((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateDrawingArea_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateDrawnButton
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateDrawnButton)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateDrawnButton_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateDrawnButton((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateDrawnButton_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateErrorDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateErrorDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateErrorDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateErrorDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateErrorDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateFileSelectionDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateFileSelectionDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateFileSelectionDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateFileSelectionDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateFileSelectionDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateForm
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateForm)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateForm_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateForm((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateForm_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateFrame
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateFrame)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateFrame_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateFrame((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateFrame_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateInformationDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateInformationDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateInformationDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateInformationDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateInformationDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateLabel
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateLabel)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateLabel_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateLabel((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateLabel_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateList
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateList)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateList_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateList((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateList_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateMainWindow
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateMainWindow)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateMainWindow_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateMainWindow((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateMainWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateMenuBar
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateMenuBar)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateMenuBar_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateMenuBar((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateMenuBar_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateMessageDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateMessageDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateMessageDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateMessageDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateMessageDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreatePopupMenu
JNIEXPORT jint JNICALL OS_NATIVE(XmCreatePopupMenu)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreatePopupMenu_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreatePopupMenu((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreatePopupMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreatePulldownMenu
JNIEXPORT jint JNICALL OS_NATIVE(XmCreatePulldownMenu)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreatePulldownMenu_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreatePulldownMenu((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreatePulldownMenu_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreatePushButton
JNIEXPORT jint JNICALL OS_NATIVE(XmCreatePushButton)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreatePushButton_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreatePushButton((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreatePushButton_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreatePushButtonGadget
JNIEXPORT jint JNICALL OS_NATIVE(XmCreatePushButtonGadget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreatePushButtonGadget_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreatePushButtonGadget((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreatePushButtonGadget_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateQuestionDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateQuestionDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateQuestionDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateQuestionDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateQuestionDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateScale
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateScale)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateScale_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateScale((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateScale_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateScrollBar
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateScrollBar)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateScrollBar_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateScrollBar((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateScrollBar_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateScrolledList
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateScrolledList)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateScrolledList_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateScrolledList((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateScrolledList_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateScrolledText
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateScrolledText)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateScrolledText_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateScrolledText((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateScrolledText_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateSeparator
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateSeparator)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateSeparator_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateSeparator((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateSeparator_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateSeparatorGadget
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateSeparatorGadget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateSeparatorGadget_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateSeparatorGadget((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateSeparatorGadget_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateSimpleSpinBox
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateSimpleSpinBox)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateSimpleSpinBox_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateSimpleSpinBox((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateSimpleSpinBox_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateTextField
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateTextField)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateTextField_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateTextField((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateTextField_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateToggleButton
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateToggleButton)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateToggleButton_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateToggleButton((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateToggleButton_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateToggleButtonGadget
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateToggleButtonGadget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateToggleButtonGadget_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateToggleButtonGadget((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateToggleButtonGadget_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateWarningDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateWarningDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateWarningDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateWarningDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateWarningDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_XmCreateWorkingDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateWorkingDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmCreateWorkingDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateWorkingDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmCreateWorkingDialog_FUNC);
	return rc;
}
#endif

#ifndef NO_XmDestroyPixmap
JNIEXPORT jboolean JNICALL OS_NATIVE(XmDestroyPixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmDestroyPixmap_FUNC);
	rc = (jboolean)XmDestroyPixmap((Screen *)arg0, (Pixmap)arg1);
	OS_NATIVE_EXIT(env, that, XmDestroyPixmap_FUNC);
	return rc;
}
#endif

#ifndef NO_XmDragCancel
JNIEXPORT void JNICALL OS_NATIVE(XmDragCancel)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmDragCancel_FUNC);
	XmDragCancel((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmDragCancel_FUNC);
}
#endif

#ifndef NO_XmDragStart
JNIEXPORT jint JNICALL OS_NATIVE(XmDragStart)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmDragStart_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmDragStart((Widget)arg0, (XEvent *)arg1, (ArgList)lparg2, (Cardinal)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XmDragStart_FUNC);
	return rc;
}
#endif

#ifndef NO_XmDropSiteRegister
JNIEXPORT void JNICALL OS_NATIVE(XmDropSiteRegister)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, XmDropSiteRegister_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmDropSiteRegister((Widget)arg0, (ArgList)lparg1, (Cardinal)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmDropSiteRegister_FUNC);
}
#endif

#ifndef NO_XmDropSiteUnregister
JNIEXPORT void JNICALL OS_NATIVE(XmDropSiteUnregister)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmDropSiteUnregister_FUNC);
	XmDropSiteUnregister((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmDropSiteUnregister_FUNC);
}
#endif

#ifndef NO_XmDropSiteUpdate
JNIEXPORT void JNICALL OS_NATIVE(XmDropSiteUpdate)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, XmDropSiteUpdate_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmDropSiteUpdate((Widget)arg0, (ArgList)lparg1, (Cardinal)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmDropSiteUpdate_FUNC);
}
#endif

#ifndef NO_XmDropTransferAdd
JNIEXPORT void JNICALL OS_NATIVE(XmDropTransferAdd)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, XmDropTransferAdd_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmDropTransferAdd((Widget)arg0, (XmDropTransferEntryRec *)lparg1, (Cardinal)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmDropTransferAdd_FUNC);
}
#endif

#ifndef NO_XmDropTransferStart
JNIEXPORT jint JNICALL OS_NATIVE(XmDropTransferStart)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmDropTransferStart_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmDropTransferStart((Widget)arg0, (ArgList)lparg1, (Cardinal)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmDropTransferStart_FUNC);
	return rc;
}
#endif

#ifndef NO_XmFileSelectionBoxGetChild
JNIEXPORT jint JNICALL OS_NATIVE(XmFileSelectionBoxGetChild)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmFileSelectionBoxGetChild_FUNC);
	rc = (jint)XmFileSelectionBoxGetChild((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmFileSelectionBoxGetChild_FUNC);
	return rc;
}
#endif

#ifndef NO_XmFontListAppendEntry
JNIEXPORT jint JNICALL OS_NATIVE(XmFontListAppendEntry)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmFontListAppendEntry_FUNC);
	rc = (jint)XmFontListAppendEntry((XmFontList)arg0, (XmFontListEntry)arg1);
	OS_NATIVE_EXIT(env, that, XmFontListAppendEntry_FUNC);
	return rc;
}
#endif

#ifndef NO_XmFontListCopy
JNIEXPORT jint JNICALL OS_NATIVE(XmFontListCopy)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmFontListCopy_FUNC);
	rc = (jint)XmFontListCopy((XmFontList)arg0);
	OS_NATIVE_EXIT(env, that, XmFontListCopy_FUNC);
	return rc;
}
#endif

#ifndef NO_XmFontListEntryFree
JNIEXPORT void JNICALL OS_NATIVE(XmFontListEntryFree)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, XmFontListEntryFree_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	XmFontListEntryFree((XmFontListEntry *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, XmFontListEntryFree_FUNC);
}
#endif

#ifndef NO_XmFontListEntryGetFont
JNIEXPORT jint JNICALL OS_NATIVE(XmFontListEntryGetFont)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmFontListEntryGetFont_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmFontListEntryGetFont((XmFontListEntry)arg0, (XmFontType *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmFontListEntryGetFont_FUNC);
	return rc;
}
#endif

#ifndef NO_XmFontListEntryLoad
JNIEXPORT jint JNICALL OS_NATIVE(XmFontListEntryLoad)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg1=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmFontListEntryLoad_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XmFontListEntryLoad((Display *)arg0, (char *)lparg1, arg2, (char *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmFontListEntryLoad_FUNC);
	return rc;
}
#endif

#ifndef NO_XmFontListFree
JNIEXPORT void JNICALL OS_NATIVE(XmFontListFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmFontListFree_FUNC);
	XmFontListFree((XmFontList)arg0);
	OS_NATIVE_EXIT(env, that, XmFontListFree_FUNC);
}
#endif

#ifndef NO_XmFontListFreeFontContext
JNIEXPORT void JNICALL OS_NATIVE(XmFontListFreeFontContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmFontListFreeFontContext_FUNC);
	XmFontListFreeFontContext((XmFontContext)arg0);
	OS_NATIVE_EXIT(env, that, XmFontListFreeFontContext_FUNC);
}
#endif

#ifndef NO_XmFontListInitFontContext
JNIEXPORT jboolean JNICALL OS_NATIVE(XmFontListInitFontContext)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1)
{
	jint *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmFontListInitFontContext_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jboolean)XmFontListInitFontContext((XmFontContext *)lparg0, (XmFontList)arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, XmFontListInitFontContext_FUNC);
	return rc;
}
#endif

#ifndef NO_XmFontListNextEntry
JNIEXPORT jint JNICALL OS_NATIVE(XmFontListNextEntry)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmFontListNextEntry_FUNC);
	rc = (jint)XmFontListNextEntry((XmFontContext)arg0);
	OS_NATIVE_EXIT(env, that, XmFontListNextEntry_FUNC);
	return rc;
}
#endif

#ifndef NO_XmGetAtomName
JNIEXPORT jint JNICALL OS_NATIVE(XmGetAtomName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmGetAtomName_FUNC);
	rc = (jint)XmGetAtomName((Display *)arg0, (Atom)arg1);
	OS_NATIVE_EXIT(env, that, XmGetAtomName_FUNC);
	return rc;
}
#endif

#ifndef NO_XmGetDragContext
JNIEXPORT jint JNICALL OS_NATIVE(XmGetDragContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmGetDragContext_FUNC);
	rc = (jint)XmGetDragContext((Widget)arg0, (Time)arg1);
	OS_NATIVE_EXIT(env, that, XmGetDragContext_FUNC);
	return rc;
}
#endif

#ifndef NO_XmGetFocusWidget
JNIEXPORT jint JNICALL OS_NATIVE(XmGetFocusWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmGetFocusWidget_FUNC);
	rc = (jint)XmGetFocusWidget((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmGetFocusWidget_FUNC);
	return rc;
}
#endif

#ifndef NO_XmGetPixmap
JNIEXPORT jint JNICALL OS_NATIVE(XmGetPixmap)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmGetPixmap_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmGetPixmap((Screen *)arg0, (char *)lparg1, (Pixel)arg2, (Pixel)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmGetPixmap_FUNC);
	return rc;
}
#endif

#ifndef NO_XmGetPixmapByDepth
JNIEXPORT jint JNICALL OS_NATIVE(XmGetPixmapByDepth)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jint arg4)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmGetPixmapByDepth_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmGetPixmapByDepth((Screen *)arg0, (char *)lparg1, arg2, arg3, arg4);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmGetPixmapByDepth_FUNC);
	return rc;
}
#endif

#ifndef NO_XmGetXmDisplay
JNIEXPORT jint JNICALL OS_NATIVE(XmGetXmDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmGetXmDisplay_FUNC);
	rc = (jint)XmGetXmDisplay((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XmGetXmDisplay_FUNC);
	return rc;
}
#endif

#ifndef NO_XmImMbLookupString
JNIEXPORT jint JNICALL OS_NATIVE(XmImMbLookupString)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jbyteArray arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	XKeyEvent _arg1, *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmImMbLookupString_FUNC);
	if (arg1) if ((lparg1 = getXKeyEventFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)XmImMbLookupString((Widget)arg0, (XKeyPressedEvent *)lparg1, (char *)lparg2, arg3, (KeySym *)lparg4, (int *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setXKeyEventFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, XmImMbLookupString_FUNC);
	return rc;
}
#endif

#ifndef NO_XmImRegister
JNIEXPORT void JNICALL OS_NATIVE(XmImRegister)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XmImRegister_FUNC);
	XmImRegister((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmImRegister_FUNC);
}
#endif

#ifndef NO_XmImSetFocusValues
JNIEXPORT void JNICALL OS_NATIVE(XmImSetFocusValues)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, XmImSetFocusValues_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmImSetFocusValues((Widget)arg0, (ArgList)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmImSetFocusValues_FUNC);
}
#endif

#ifndef NO_XmImSetValues
JNIEXPORT void JNICALL OS_NATIVE(XmImSetValues)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, XmImSetValues_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmImSetValues((Widget)arg0, (ArgList)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmImSetValues_FUNC);
}
#endif

#ifndef NO_XmImUnregister
JNIEXPORT void JNICALL OS_NATIVE(XmImUnregister)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmImUnregister_FUNC);
	XmImUnregister((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmImUnregister_FUNC);
}
#endif

#ifndef NO_XmImUnsetFocus
JNIEXPORT void JNICALL OS_NATIVE(XmImUnsetFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmImUnsetFocus_FUNC);
	XmImUnsetFocus((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmImUnsetFocus_FUNC);
}
#endif

#ifndef NO_XmInternAtom
JNIEXPORT jint JNICALL OS_NATIVE(XmInternAtom)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jboolean arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmInternAtom_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmInternAtom((Display *)arg0, (String)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmInternAtom_FUNC);
	return rc;
}
#endif

#ifndef NO_XmListAddItemUnselected
JNIEXPORT void JNICALL OS_NATIVE(XmListAddItemUnselected)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XmListAddItemUnselected_FUNC);
	XmListAddItemUnselected((Widget)arg0, (XmString)arg1, arg2);
	OS_NATIVE_EXIT(env, that, XmListAddItemUnselected_FUNC);
}
#endif

#ifndef NO_XmListDeleteAllItems
JNIEXPORT void JNICALL OS_NATIVE(XmListDeleteAllItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmListDeleteAllItems_FUNC);
	XmListDeleteAllItems((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmListDeleteAllItems_FUNC);
}
#endif

#ifndef NO_XmListDeleteItemsPos
JNIEXPORT void JNICALL OS_NATIVE(XmListDeleteItemsPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XmListDeleteItemsPos_FUNC);
	XmListDeleteItemsPos((Widget)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, XmListDeleteItemsPos_FUNC);
}
#endif

#ifndef NO_XmListDeletePos
JNIEXPORT void JNICALL OS_NATIVE(XmListDeletePos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XmListDeletePos_FUNC);
	XmListDeletePos((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmListDeletePos_FUNC);
}
#endif

#ifndef NO_XmListDeletePositions
JNIEXPORT void JNICALL OS_NATIVE(XmListDeletePositions)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, XmListDeletePositions_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmListDeletePositions((Widget)arg0, (int *)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmListDeletePositions_FUNC);
}
#endif

#ifndef NO_XmListDeselectAllItems
JNIEXPORT void JNICALL OS_NATIVE(XmListDeselectAllItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmListDeselectAllItems_FUNC);
	XmListDeselectAllItems((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmListDeselectAllItems_FUNC);
}
#endif

#ifndef NO_XmListDeselectPos
JNIEXPORT void JNICALL OS_NATIVE(XmListDeselectPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XmListDeselectPos_FUNC);
	XmListDeselectPos((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmListDeselectPos_FUNC);
}
#endif

#ifndef NO_XmListGetKbdItemPos
JNIEXPORT jint JNICALL OS_NATIVE(XmListGetKbdItemPos)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmListGetKbdItemPos_FUNC);
	rc = (jint)XmListGetKbdItemPos((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmListGetKbdItemPos_FUNC);
	return rc;
}
#endif

#ifndef NO_XmListGetSelectedPos
JNIEXPORT jboolean JNICALL OS_NATIVE(XmListGetSelectedPos)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmListGetSelectedPos_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)XmListGetSelectedPos((Widget)arg0, (int **)lparg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmListGetSelectedPos_FUNC);
	return rc;
}
#endif

#ifndef NO_XmListItemPos
JNIEXPORT jint JNICALL OS_NATIVE(XmListItemPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmListItemPos_FUNC);
	rc = (jint)XmListItemPos((Widget)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, XmListItemPos_FUNC);
	return rc;
}
#endif

#ifndef NO_XmListPosSelected
JNIEXPORT jboolean JNICALL OS_NATIVE(XmListPosSelected)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmListPosSelected_FUNC);
	rc = (jboolean)XmListPosSelected((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmListPosSelected_FUNC);
	return rc;
}
#endif

#ifndef NO_XmListReplaceItemsPosUnselected
JNIEXPORT void JNICALL OS_NATIVE(XmListReplaceItemsPosUnselected)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, XmListReplaceItemsPosUnselected_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmListReplaceItemsPosUnselected((Widget)arg0, (XmString *)lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmListReplaceItemsPosUnselected_FUNC);
}
#endif

#ifndef NO_XmListSelectPos
JNIEXPORT void JNICALL OS_NATIVE(XmListSelectPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, XmListSelectPos_FUNC);
	XmListSelectPos((Widget)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, XmListSelectPos_FUNC);
}
#endif

#ifndef NO_XmListSetKbdItemPos
JNIEXPORT jboolean JNICALL OS_NATIVE(XmListSetKbdItemPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmListSetKbdItemPos_FUNC);
	rc = (jboolean)XmListSetKbdItemPos((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmListSetKbdItemPos_FUNC);
	return rc;
}
#endif

#ifndef NO_XmListSetPos
JNIEXPORT void JNICALL OS_NATIVE(XmListSetPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XmListSetPos_FUNC);
	XmListSetPos((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmListSetPos_FUNC);
}
#endif

#ifndef NO_XmListUpdateSelectedList
JNIEXPORT void JNICALL OS_NATIVE(XmListUpdateSelectedList)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmListUpdateSelectedList_FUNC);
	XmListUpdateSelectedList((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmListUpdateSelectedList_FUNC);
}
#endif

#ifndef NO_XmMainWindowSetAreas
JNIEXPORT void JNICALL OS_NATIVE(XmMainWindowSetAreas)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, XmMainWindowSetAreas_FUNC);
	XmMainWindowSetAreas((Widget)arg0, (Widget)arg1, (Widget)arg2, (Widget)arg3, (Widget)arg4, (Widget)arg5);
	OS_NATIVE_EXIT(env, that, XmMainWindowSetAreas_FUNC);
}
#endif

#ifndef NO_XmMessageBoxGetChild
JNIEXPORT jint JNICALL OS_NATIVE(XmMessageBoxGetChild)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmMessageBoxGetChild_FUNC);
	rc = (jint)XmMessageBoxGetChild((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmMessageBoxGetChild_FUNC);
	return rc;
}
#endif

#ifndef NO_XmParseMappingCreate
JNIEXPORT jint JNICALL OS_NATIVE(XmParseMappingCreate)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmParseMappingCreate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)XmParseMappingCreate((ArgList)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, XmParseMappingCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_XmParseMappingFree
JNIEXPORT void JNICALL OS_NATIVE(XmParseMappingFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmParseMappingFree_FUNC);
	XmParseMappingFree((XmParseMapping)arg0);
	OS_NATIVE_EXIT(env, that, XmParseMappingFree_FUNC);
}
#endif

#ifndef NO_XmProcessTraversal
JNIEXPORT jboolean JNICALL OS_NATIVE(XmProcessTraversal)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmProcessTraversal_FUNC);
	rc = (jboolean)XmProcessTraversal((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmProcessTraversal_FUNC);
	return rc;
}
#endif

#ifndef NO_XmRenderTableAddRenditions
JNIEXPORT jint JNICALL OS_NATIVE(XmRenderTableAddRenditions)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmRenderTableAddRenditions_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmRenderTableAddRenditions((XmRenderTable)arg0, (XmRendition *)lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmRenderTableAddRenditions_FUNC);
	return rc;
}
#endif

#ifndef NO_XmRenderTableFree
JNIEXPORT void JNICALL OS_NATIVE(XmRenderTableFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmRenderTableFree_FUNC);
	XmRenderTableFree((XmRenderTable)arg0);
	OS_NATIVE_EXIT(env, that, XmRenderTableFree_FUNC);
}
#endif

#ifndef NO_XmRenditionCreate
JNIEXPORT jint JNICALL OS_NATIVE(XmRenditionCreate)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmRenditionCreate_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmRenditionCreate((Widget)arg0, (XmStringTag)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmRenditionCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_XmRenditionFree
JNIEXPORT void JNICALL OS_NATIVE(XmRenditionFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmRenditionFree_FUNC);
	XmRenditionFree((XmRendition)arg0);
	OS_NATIVE_EXIT(env, that, XmRenditionFree_FUNC);
}
#endif

#ifndef NO_XmStringBaseline
JNIEXPORT jint JNICALL OS_NATIVE(XmStringBaseline)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmStringBaseline_FUNC);
	rc = (jint)XmStringBaseline((XmRenderTable)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, XmStringBaseline_FUNC);
	return rc;
}
#endif

#ifndef NO_XmStringCompare
JNIEXPORT jboolean JNICALL OS_NATIVE(XmStringCompare)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmStringCompare_FUNC);
	rc = (jboolean)XmStringCompare((XmString)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, XmStringCompare_FUNC);
	return rc;
}
#endif

#ifndef NO_XmStringComponentCreate
JNIEXPORT jint JNICALL OS_NATIVE(XmStringComponentCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmStringComponentCreate_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmStringComponentCreate(arg0, arg1, (XtPointer)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XmStringComponentCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_XmStringConcat
JNIEXPORT jint JNICALL OS_NATIVE(XmStringConcat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmStringConcat_FUNC);
	rc = (jint)XmStringConcat((XmString)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, XmStringConcat_FUNC);
	return rc;
}
#endif

#ifndef NO_XmStringCreate
JNIEXPORT jint JNICALL OS_NATIVE(XmStringCreate)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmStringCreate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmStringCreate((char *)lparg0, (char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, XmStringCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_XmStringCreateLocalized
JNIEXPORT jint JNICALL OS_NATIVE(XmStringCreateLocalized)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmStringCreateLocalized_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)XmStringCreateLocalized((char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, XmStringCreateLocalized_FUNC);
	return rc;
}
#endif

#ifndef NO_XmStringDraw
JNIEXPORT void JNICALL OS_NATIVE(XmStringDraw)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jobject arg10)
{
	XRectangle _arg10, *lparg10=NULL;
	OS_NATIVE_ENTER(env, that, XmStringDraw_FUNC);
	if (arg10) if ((lparg10 = getXRectangleFields(env, arg10, &_arg10)) == NULL) goto fail;
	XmStringDraw((Display *)arg0, (Window)arg1, (XmFontList)arg2, (XmString)arg3, (GC)arg4, arg5, arg6, arg7, arg8, arg9, lparg10);
fail:
	if (arg10 && lparg10) setXRectangleFields(env, arg10, lparg10);
	OS_NATIVE_EXIT(env, that, XmStringDraw_FUNC);
}
#endif

#ifndef NO_XmStringDrawImage
JNIEXPORT void JNICALL OS_NATIVE(XmStringDrawImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jobject arg10)
{
	XRectangle _arg10, *lparg10=NULL;
	OS_NATIVE_ENTER(env, that, XmStringDrawImage_FUNC);
	if (arg10) if ((lparg10 = getXRectangleFields(env, arg10, &_arg10)) == NULL) goto fail;
	XmStringDrawImage((Display *)arg0, (Window)arg1, (XmFontList)arg2, (XmString)arg3, (GC)arg4, arg5, arg6, arg7, arg8, arg9, lparg10);
fail:
	if (arg10 && lparg10) setXRectangleFields(env, arg10, lparg10);
	OS_NATIVE_EXIT(env, that, XmStringDrawImage_FUNC);
}
#endif

#ifndef NO_XmStringDrawUnderline
JNIEXPORT void JNICALL OS_NATIVE(XmStringDrawUnderline)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jobject arg10, jint arg11)
{
	XRectangle _arg10, *lparg10=NULL;
	OS_NATIVE_ENTER(env, that, XmStringDrawUnderline_FUNC);
	if (arg10) if ((lparg10 = getXRectangleFields(env, arg10, &_arg10)) == NULL) goto fail;
	XmStringDrawUnderline((Display *)arg0, (Window)arg1, (XmFontList)arg2, (XmString)arg3, (GC)arg4, arg5, arg6, arg7, arg8, arg9, lparg10, (XmString)arg11);
fail:
	if (arg10 && lparg10) setXRectangleFields(env, arg10, lparg10);
	OS_NATIVE_EXIT(env, that, XmStringDrawUnderline_FUNC);
}
#endif

#ifndef NO_XmStringEmpty
JNIEXPORT jboolean JNICALL OS_NATIVE(XmStringEmpty)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmStringEmpty_FUNC);
	rc = (jboolean)XmStringEmpty((XmString)arg0);
	OS_NATIVE_EXIT(env, that, XmStringEmpty_FUNC);
	return rc;
}
#endif

#ifndef NO_XmStringExtent
JNIEXPORT void JNICALL OS_NATIVE(XmStringExtent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jshortArray arg3)
{
	jshort *lparg2=NULL;
	jshort *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, XmStringExtent_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	XmStringExtent((XmRenderTable)arg0, (XmString)arg1, (Dimension *)lparg2, (Dimension *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XmStringExtent_FUNC);
}
#endif

#ifndef NO_XmStringFree
JNIEXPORT void JNICALL OS_NATIVE(XmStringFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmStringFree_FUNC);
	XmStringFree((XmString)arg0);
	OS_NATIVE_EXIT(env, that, XmStringFree_FUNC);
}
#endif

#ifndef NO_XmStringGenerate
JNIEXPORT jint JNICALL OS_NATIVE(XmStringGenerate)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmStringGenerate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XmStringGenerate((XtPointer)lparg0, (XmStringTag)lparg1, arg2, (XmStringTag)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, XmStringGenerate_FUNC);
	return rc;
}
#endif

#ifndef NO_XmStringHeight
JNIEXPORT jint JNICALL OS_NATIVE(XmStringHeight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmStringHeight_FUNC);
	rc = (jint)XmStringHeight((XmFontList)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, XmStringHeight_FUNC);
	return rc;
}
#endif

#ifndef NO_XmStringParseText
JNIEXPORT jint JNICALL OS_NATIVE(XmStringParseText)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jbyteArray arg2, jint arg3, jintArray arg4, jint arg5, jint arg6)
{
	jbyte *lparg0=NULL;
	jbyte *lparg2=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmStringParseText_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XmStringParseText((XtPointer)lparg0, (XtPointer *)arg1, (XmStringTag)lparg2, arg3, (XmParseTable)lparg4, arg5, (XtPointer)arg6);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, XmStringParseText_FUNC);
	return rc;
}
#endif

#ifndef NO_XmStringUnparse
JNIEXPORT jint JNICALL OS_NATIVE(XmStringUnparse)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jintArray arg4, jint arg5, jint arg6)
{
	jbyte *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmStringUnparse_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XmStringUnparse((XmString)arg0, (XmStringTag)lparg1, arg2, arg3, (XmParseTable)lparg4, arg5, arg6);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmStringUnparse_FUNC);
	return rc;
}
#endif

#ifndef NO_XmStringWidth
JNIEXPORT jint JNICALL OS_NATIVE(XmStringWidth)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmStringWidth_FUNC);
	rc = (jint)XmStringWidth((XmFontList)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, XmStringWidth_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTabCreate
JNIEXPORT jint JNICALL OS_NATIVE(XmTabCreate)
	(JNIEnv *env, jclass that, jint arg0, jbyte arg1, jbyte arg2, jbyte arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmTabCreate_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XmTabCreate(arg0, arg1, arg2, arg3, (char *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, XmTabCreate_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTabFree
JNIEXPORT void JNICALL OS_NATIVE(XmTabFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmTabFree_FUNC);
	XmTabFree((XmTab)arg0);
	OS_NATIVE_EXIT(env, that, XmTabFree_FUNC);
}
#endif

#ifndef NO_XmTabListFree
JNIEXPORT void JNICALL OS_NATIVE(XmTabListFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmTabListFree_FUNC);
	XmTabListFree((XmTabList)arg0);
	OS_NATIVE_EXIT(env, that, XmTabListFree_FUNC);
}
#endif

#ifndef NO_XmTabListInsertTabs
JNIEXPORT jint JNICALL OS_NATIVE(XmTabListInsertTabs)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmTabListInsertTabs_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmTabListInsertTabs((XmTabList)arg0, (XmTab *)lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmTabListInsertTabs_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextClearSelection
JNIEXPORT void JNICALL OS_NATIVE(XmTextClearSelection)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XmTextClearSelection_FUNC);
	XmTextClearSelection((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmTextClearSelection_FUNC);
}
#endif

#ifndef NO_XmTextCopy
JNIEXPORT jboolean JNICALL OS_NATIVE(XmTextCopy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextCopy_FUNC);
	rc = (jboolean)XmTextCopy((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmTextCopy_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextCut
JNIEXPORT jboolean JNICALL OS_NATIVE(XmTextCut)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextCut_FUNC);
	rc = (jboolean)XmTextCut((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmTextCut_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextDisableRedisplay
JNIEXPORT void JNICALL OS_NATIVE(XmTextDisableRedisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmTextDisableRedisplay_FUNC);
	XmTextDisableRedisplay((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmTextDisableRedisplay_FUNC);
}
#endif

#ifndef NO_XmTextEnableRedisplay
JNIEXPORT void JNICALL OS_NATIVE(XmTextEnableRedisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmTextEnableRedisplay_FUNC);
	XmTextEnableRedisplay((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmTextEnableRedisplay_FUNC);
}
#endif

#ifndef NO_XmTextFieldPaste
JNIEXPORT jboolean JNICALL OS_NATIVE(XmTextFieldPaste)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextFieldPaste_FUNC);
	rc = (jboolean)XmTextFieldPaste((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmTextFieldPaste_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextGetInsertionPosition
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetInsertionPosition)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextGetInsertionPosition_FUNC);
	rc = (jint)XmTextGetInsertionPosition((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmTextGetInsertionPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextGetLastPosition
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetLastPosition)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextGetLastPosition_FUNC);
	rc = (jint)XmTextGetLastPosition((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmTextGetLastPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextGetMaxLength
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetMaxLength)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextGetMaxLength_FUNC);
	rc = (jint)XmTextGetMaxLength((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmTextGetMaxLength_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextGetSelection
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetSelection)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextGetSelection_FUNC);
	rc = (jint)XmTextGetSelection((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmTextGetSelection_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextGetSelectionPosition
JNIEXPORT jboolean JNICALL OS_NATIVE(XmTextGetSelectionPosition)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextGetSelectionPosition_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)XmTextGetSelectionPosition((Widget)arg0, (XmTextPosition *)lparg1, (XmTextPosition *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmTextGetSelectionPosition_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextGetString
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetString)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextGetString_FUNC);
	rc = (jint)XmTextGetString((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmTextGetString_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextGetSubstring
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetSubstring)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextGetSubstring_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XmTextGetSubstring((Widget)arg0, arg1, arg2, arg3, (char *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, XmTextGetSubstring_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextGetSubstringWcs
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetSubstringWcs)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextGetSubstringWcs_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XmTextGetSubstringWcs((Widget)arg0, (XmTextPosition)arg1, arg2, arg3, (wchar_t *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, XmTextGetSubstringWcs_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextInsert
JNIEXPORT void JNICALL OS_NATIVE(XmTextInsert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, XmTextInsert_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	XmTextInsert((Widget)arg0, arg1, (char *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XmTextInsert_FUNC);
}
#endif

#ifndef NO_XmTextPaste
JNIEXPORT jboolean JNICALL OS_NATIVE(XmTextPaste)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextPaste_FUNC);
	rc = (jboolean)XmTextPaste((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmTextPaste_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextPosToXY
JNIEXPORT jboolean JNICALL OS_NATIVE(XmTextPosToXY)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jshortArray arg3)
{
	jshort *lparg2=NULL;
	jshort *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmTextPosToXY_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jboolean)XmTextPosToXY((Widget)arg0, (XmTextPosition)arg1, (Position *)lparg2, (Position *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XmTextPosToXY_FUNC);
	return rc;
}
#endif

#ifndef NO_XmTextReplace
JNIEXPORT void JNICALL OS_NATIVE(XmTextReplace)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, XmTextReplace_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	XmTextReplace((Widget)arg0, arg1, arg2, (char *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, XmTextReplace_FUNC);
}
#endif

#ifndef NO_XmTextScroll
JNIEXPORT void JNICALL OS_NATIVE(XmTextScroll)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XmTextScroll_FUNC);
	XmTextScroll((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmTextScroll_FUNC);
}
#endif

#ifndef NO_XmTextSetEditable
JNIEXPORT void JNICALL OS_NATIVE(XmTextSetEditable)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, XmTextSetEditable_FUNC);
	XmTextSetEditable((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmTextSetEditable_FUNC);
}
#endif

#ifndef NO_XmTextSetHighlight
JNIEXPORT void JNICALL OS_NATIVE(XmTextSetHighlight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, XmTextSetHighlight_FUNC);
	XmTextSetHighlight((Widget)arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, XmTextSetHighlight_FUNC);
}
#endif

#ifndef NO_XmTextSetInsertionPosition
JNIEXPORT void JNICALL OS_NATIVE(XmTextSetInsertionPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XmTextSetInsertionPosition_FUNC);
	XmTextSetInsertionPosition((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmTextSetInsertionPosition_FUNC);
}
#endif

#ifndef NO_XmTextSetMaxLength
JNIEXPORT void JNICALL OS_NATIVE(XmTextSetMaxLength)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XmTextSetMaxLength_FUNC);
	XmTextSetMaxLength((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmTextSetMaxLength_FUNC);
}
#endif

#ifndef NO_XmTextSetSelection
JNIEXPORT void JNICALL OS_NATIVE(XmTextSetSelection)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, XmTextSetSelection_FUNC);
	XmTextSetSelection((Widget)arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, XmTextSetSelection_FUNC);
}
#endif

#ifndef NO_XmTextSetString
JNIEXPORT void JNICALL OS_NATIVE(XmTextSetString)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, XmTextSetString_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmTextSetString((Widget)arg0, (char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XmTextSetString_FUNC);
}
#endif

#ifndef NO_XmTextShowPosition
JNIEXPORT void JNICALL OS_NATIVE(XmTextShowPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XmTextShowPosition_FUNC);
	XmTextShowPosition((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XmTextShowPosition_FUNC);
}
#endif

#ifndef NO_XmUpdateDisplay
JNIEXPORT void JNICALL OS_NATIVE(XmUpdateDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XmUpdateDisplay_FUNC);
	XmUpdateDisplay((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XmUpdateDisplay_FUNC);
}
#endif

#ifndef NO_XmWidgetGetDisplayRect
JNIEXPORT jboolean JNICALL OS_NATIVE(XmWidgetGetDisplayRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	XRectangle _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XmWidgetGetDisplayRect_FUNC);
	if (arg1) if ((lparg1 = getXRectangleFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)XmWidgetGetDisplayRect((Widget)arg0, (XRectangle *)lparg1);
fail:
	if (arg1 && lparg1) setXRectangleFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, XmWidgetGetDisplayRect_FUNC);
	return rc;
}
#endif

#ifndef NO_XmbTextListToTextProperty
JNIEXPORT jint JNICALL OS_NATIVE(XmbTextListToTextProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	XTextProperty _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmbTextListToTextProperty_FUNC);
	if (arg4) if ((lparg4 = getXTextPropertyFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)XmbTextListToTextProperty((Display *)arg0, (char **)arg1, arg2, (XICCEncodingStyle)arg3, lparg4);
fail:
	if (arg4 && lparg4) setXTextPropertyFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, XmbTextListToTextProperty_FUNC);
	return rc;
}
#endif

#ifndef NO_XmbTextPropertyToTextList
JNIEXPORT jint JNICALL OS_NATIVE(XmbTextPropertyToTextList)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jintArray arg2, jintArray arg3)
{
	XTextProperty _arg1, *lparg1=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XmbTextPropertyToTextList_FUNC);
	if (arg1) if ((lparg1 = getXTextPropertyFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XmbTextPropertyToTextList((Display *)arg0, lparg1, (char ***)lparg2, (int *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setXTextPropertyFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, XmbTextPropertyToTextList_FUNC);
	return rc;
}
#endif

#ifndef NO_XpCancelJob
JNIEXPORT void JNICALL OS_NATIVE(XpCancelJob)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, XpCancelJob_FUNC);
	XpCancelJob((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XpCancelJob_FUNC);
}
#endif

#ifndef NO_XpCreateContext
JNIEXPORT jint JNICALL OS_NATIVE(XpCreateContext)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XpCreateContext_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XpCreateContext((Display *)arg0, (char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XpCreateContext_FUNC);
	return rc;
}
#endif

#ifndef NO_XpDestroyContext
JNIEXPORT void JNICALL OS_NATIVE(XpDestroyContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XpDestroyContext_FUNC);
	XpDestroyContext((Display *)arg0, (XPContext)arg1);
	OS_NATIVE_EXIT(env, that, XpDestroyContext_FUNC);
}
#endif

#ifndef NO_XpEndJob
JNIEXPORT void JNICALL OS_NATIVE(XpEndJob)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XpEndJob_FUNC);
	XpEndJob((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XpEndJob_FUNC);
}
#endif

#ifndef NO_XpEndPage
JNIEXPORT void JNICALL OS_NATIVE(XpEndPage)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XpEndPage_FUNC);
	XpEndPage((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XpEndPage_FUNC);
}
#endif

#ifndef NO_XpFreePrinterList
JNIEXPORT void JNICALL OS_NATIVE(XpFreePrinterList)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XpFreePrinterList_FUNC);
	XpFreePrinterList((XPPrinterList)arg0);
	OS_NATIVE_EXIT(env, that, XpFreePrinterList_FUNC);
}
#endif

#ifndef NO_XpGetOneAttribute
JNIEXPORT jint JNICALL OS_NATIVE(XpGetOneAttribute)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyte arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XpGetOneAttribute_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XpGetOneAttribute((Display *)arg0, (XPContext)arg1, (XPAttributes)arg2, (char *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, XpGetOneAttribute_FUNC);
	return rc;
}
#endif

#ifndef NO_XpGetPageDimensions
JNIEXPORT jint JNICALL OS_NATIVE(XpGetPageDimensions)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jshortArray arg3, jobject arg4)
{
	jshort *lparg2=NULL;
	jshort *lparg3=NULL;
	XRectangle _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XpGetPageDimensions_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getXRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)XpGetPageDimensions((Display *)arg0, (XPContext)arg1, (unsigned short *)lparg2, (unsigned short *)lparg3, (XRectangle *)lparg4);
fail:
	if (arg4 && lparg4) setXRectangleFields(env, arg4, lparg4);
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, XpGetPageDimensions_FUNC);
	return rc;
}
#endif

#ifndef NO_XpGetPrinterList
JNIEXPORT jint JNICALL OS_NATIVE(XpGetPrinterList)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XpGetPrinterList_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XpGetPrinterList((Display *)arg0, (char *)lparg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XpGetPrinterList_FUNC);
	return rc;
}
#endif

#ifndef NO_XpGetScreenOfContext
JNIEXPORT jint JNICALL OS_NATIVE(XpGetScreenOfContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XpGetScreenOfContext_FUNC);
	rc = (jint)XpGetScreenOfContext((Display *)arg0, (XPContext)arg1);
	OS_NATIVE_EXIT(env, that, XpGetScreenOfContext_FUNC);
	return rc;
}
#endif

#ifndef NO_XpSetAttributes
JNIEXPORT void JNICALL OS_NATIVE(XpSetAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyte arg2, jbyteArray arg3, jbyte arg4)
{
	jbyte *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, XpSetAttributes_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	XpSetAttributes((Display *)arg0, (XPContext)arg1, (XPAttributes)arg2, (char *)lparg3, (XPAttrReplacement)arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, XpSetAttributes_FUNC);
}
#endif

#ifndef NO_XpSetContext
JNIEXPORT void JNICALL OS_NATIVE(XpSetContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XpSetContext_FUNC);
	XpSetContext((Display *)arg0, (XPContext)arg1);
	OS_NATIVE_EXIT(env, that, XpSetContext_FUNC);
}
#endif

#ifndef NO_XpStartJob
JNIEXPORT void JNICALL OS_NATIVE(XpStartJob)
	(JNIEnv *env, jclass that, jint arg0, jbyte arg1)
{
	OS_NATIVE_ENTER(env, that, XpStartJob_FUNC);
	XpStartJob((Display *)arg0, (XPSaveData)arg1);
	OS_NATIVE_EXIT(env, that, XpStartJob_FUNC);
}
#endif

#ifndef NO_XpStartPage
JNIEXPORT void JNICALL OS_NATIVE(XpStartPage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XpStartPage_FUNC);
	XpStartPage((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, XpStartPage_FUNC);
}
#endif

#ifndef NO_XtAddCallback
JNIEXPORT void JNICALL OS_NATIVE(XtAddCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, XtAddCallback_FUNC);
	XtAddCallback((Widget)arg0, (String)arg1, (XtCallbackProc)arg2, (XtPointer)arg3);
	OS_NATIVE_EXIT(env, that, XtAddCallback_FUNC);
}
#endif

#ifndef NO_XtAddEventHandler
JNIEXPORT void JNICALL OS_NATIVE(XtAddEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, XtAddEventHandler_FUNC);
	XtAddEventHandler((Widget)arg0, arg1, arg2, (XtEventHandler)arg3, (XtPointer)arg4);
	OS_NATIVE_EXIT(env, that, XtAddEventHandler_FUNC);
}
#endif

#ifndef NO_XtAddExposureToRegion
JNIEXPORT void JNICALL OS_NATIVE(XtAddExposureToRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XtAddExposureToRegion_FUNC);
	XtAddExposureToRegion((XEvent *)arg0, (Region)arg1);
	OS_NATIVE_EXIT(env, that, XtAddExposureToRegion_FUNC);
}
#endif

#ifndef NO_XtAppAddInput
JNIEXPORT jint JNICALL OS_NATIVE(XtAppAddInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtAppAddInput_FUNC);
	rc = (jint)XtAppAddInput((XtAppContext)arg0, arg1, (XtPointer)arg2, (XtInputCallbackProc)arg3, (XtPointer)arg4);
	OS_NATIVE_EXIT(env, that, XtAppAddInput_FUNC);
	return rc;
}
#endif

#ifndef NO_XtAppAddTimeOut
JNIEXPORT jint JNICALL OS_NATIVE(XtAppAddTimeOut)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtAppAddTimeOut_FUNC);
	rc = (jint)XtAppAddTimeOut((XtAppContext)arg0, arg1, (XtTimerCallbackProc)arg2, (XtPointer)arg3);
	OS_NATIVE_EXIT(env, that, XtAppAddTimeOut_FUNC);
	return rc;
}
#endif

#ifndef NO_XtAppCreateShell
JNIEXPORT jint JNICALL OS_NATIVE(XtAppCreateShell)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jint arg2, jint arg3, jintArray arg4, jint arg5)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtAppCreateShell_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XtAppCreateShell((String)lparg0, (String)lparg1, (WidgetClass)arg2, (Display *)arg3, (ArgList)lparg4, arg5);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, XtAppCreateShell_FUNC);
	return rc;
}
#endif

#ifndef NO_XtAppGetSelectionTimeout
JNIEXPORT jint JNICALL OS_NATIVE(XtAppGetSelectionTimeout)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtAppGetSelectionTimeout_FUNC);
	rc = (jint)XtAppGetSelectionTimeout((XtAppContext)arg0);
	OS_NATIVE_EXIT(env, that, XtAppGetSelectionTimeout_FUNC);
	return rc;
}
#endif

#ifndef NO_XtAppNextEvent
JNIEXPORT void JNICALL OS_NATIVE(XtAppNextEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XtAppNextEvent_FUNC);
	XtAppNextEvent((XtAppContext)arg0, (XEvent *)arg1);
	OS_NATIVE_EXIT(env, that, XtAppNextEvent_FUNC);
}
#endif

#ifndef NO_XtAppPeekEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(XtAppPeekEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XtAppPeekEvent_FUNC);
	rc = (jboolean)XtAppPeekEvent((XtAppContext)arg0, (XEvent *)arg1);
	OS_NATIVE_EXIT(env, that, XtAppPeekEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_XtAppPending
JNIEXPORT jint JNICALL OS_NATIVE(XtAppPending)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtAppPending_FUNC);
	rc = (jint)XtAppPending((XtAppContext)arg0);
	OS_NATIVE_EXIT(env, that, XtAppPending_FUNC);
	return rc;
}
#endif

#ifndef NO_XtAppProcessEvent
JNIEXPORT void JNICALL OS_NATIVE(XtAppProcessEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XtAppProcessEvent_FUNC);
	XtAppProcessEvent((XtAppContext)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XtAppProcessEvent_FUNC);
}
#endif

#ifndef NO_XtAppSetErrorHandler
JNIEXPORT jint JNICALL OS_NATIVE(XtAppSetErrorHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtAppSetErrorHandler_FUNC);
	rc = (jint)XtAppSetErrorHandler((XtAppContext)arg0, (XtErrorHandler)arg1);
	OS_NATIVE_EXIT(env, that, XtAppSetErrorHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_XtAppSetFallbackResources
JNIEXPORT void JNICALL OS_NATIVE(XtAppSetFallbackResources)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XtAppSetFallbackResources_FUNC);
	XtAppSetFallbackResources((XtAppContext)arg0, (String *)arg1);
	OS_NATIVE_EXIT(env, that, XtAppSetFallbackResources_FUNC);
}
#endif

#ifndef NO_XtAppSetSelectionTimeout
JNIEXPORT void JNICALL OS_NATIVE(XtAppSetSelectionTimeout)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XtAppSetSelectionTimeout_FUNC);
	XtAppSetSelectionTimeout((XtAppContext)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XtAppSetSelectionTimeout_FUNC);
}
#endif

#ifndef NO_XtAppSetWarningHandler
JNIEXPORT jint JNICALL OS_NATIVE(XtAppSetWarningHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtAppSetWarningHandler_FUNC);
	rc = (jint)XtAppSetWarningHandler((XtAppContext)arg0, (XtErrorHandler)arg1);
	OS_NATIVE_EXIT(env, that, XtAppSetWarningHandler_FUNC);
	return rc;
}
#endif

#ifndef NO_XtBuildEventMask
JNIEXPORT jint JNICALL OS_NATIVE(XtBuildEventMask)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtBuildEventMask_FUNC);
	rc = (jint)XtBuildEventMask((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtBuildEventMask_FUNC);
	return rc;
}
#endif

#ifndef NO_XtCallActionProc
JNIEXPORT void JNICALL OS_NATIVE(XtCallActionProc)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3, jint arg4)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, XtCallActionProc_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	XtCallActionProc((Widget)arg0, (String)lparg1, (XEvent *)arg2, (String *)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XtCallActionProc_FUNC);
}
#endif

#ifndef NO_XtClass
JNIEXPORT jint JNICALL OS_NATIVE(XtClass)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtClass_FUNC);
	rc = (jint)XtClass((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtClass_FUNC);
	return rc;
}
#endif

#ifndef NO_XtConfigureWidget
JNIEXPORT void JNICALL OS_NATIVE(XtConfigureWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, XtConfigureWidget_FUNC);
	XtConfigureWidget((Widget)arg0, arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, XtConfigureWidget_FUNC);
}
#endif

#ifndef NO_XtCreateApplicationContext
JNIEXPORT jint JNICALL OS_NATIVE(XtCreateApplicationContext)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtCreateApplicationContext_FUNC);
	rc = (jint)XtCreateApplicationContext();
	OS_NATIVE_EXIT(env, that, XtCreateApplicationContext_FUNC);
	return rc;
}
#endif

#ifndef NO_XtCreatePopupShell
JNIEXPORT jint JNICALL OS_NATIVE(XtCreatePopupShell)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2, jintArray arg3, jint arg4)
{
	jbyte *lparg0=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtCreatePopupShell_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XtCreatePopupShell((String)lparg0, (WidgetClass)arg1, (Widget)arg2, (ArgList)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, XtCreatePopupShell_FUNC);
	return rc;
}
#endif

#ifndef NO_XtDestroyApplicationContext
JNIEXPORT void JNICALL OS_NATIVE(XtDestroyApplicationContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XtDestroyApplicationContext_FUNC);
	XtDestroyApplicationContext((XtAppContext)arg0);
	OS_NATIVE_EXIT(env, that, XtDestroyApplicationContext_FUNC);
}
#endif

#ifndef NO_XtDestroyWidget
JNIEXPORT void JNICALL OS_NATIVE(XtDestroyWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XtDestroyWidget_FUNC);
	XtDestroyWidget((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtDestroyWidget_FUNC);
}
#endif

#ifndef NO_XtDispatchEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(XtDispatchEvent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XtDispatchEvent_FUNC);
	rc = (jboolean)XtDispatchEvent((XEvent *)arg0);
	OS_NATIVE_EXIT(env, that, XtDispatchEvent_FUNC);
	return rc;
}
#endif

#ifndef NO_XtDisplay
JNIEXPORT jint JNICALL OS_NATIVE(XtDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtDisplay_FUNC);
	rc = (jint)XtDisplay((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtDisplay_FUNC);
	return rc;
}
#endif

#ifndef NO_XtDisplayToApplicationContext
JNIEXPORT jint JNICALL OS_NATIVE(XtDisplayToApplicationContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtDisplayToApplicationContext_FUNC);
	rc = (jint)XtDisplayToApplicationContext((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XtDisplayToApplicationContext_FUNC);
	return rc;
}
#endif

#ifndef NO_XtFree
JNIEXPORT void JNICALL OS_NATIVE(XtFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XtFree_FUNC);
	XtFree((char *)arg0);
	OS_NATIVE_EXIT(env, that, XtFree_FUNC);
}
#endif

#ifndef NO_XtGetMultiClickTime
JNIEXPORT jint JNICALL OS_NATIVE(XtGetMultiClickTime)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtGetMultiClickTime_FUNC);
	rc = (jint)XtGetMultiClickTime((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XtGetMultiClickTime_FUNC);
	return rc;
}
#endif

#ifndef NO_XtInsertEventHandler
JNIEXPORT void JNICALL OS_NATIVE(XtInsertEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, XtInsertEventHandler_FUNC);
	XtInsertEventHandler((Widget)arg0, (EventMask)arg1, (Boolean)arg2, (XtEventHandler)arg3, (XtPointer)arg4, (XtListPosition)arg5);
	OS_NATIVE_EXIT(env, that, XtInsertEventHandler_FUNC);
}
#endif

#ifndef NO_XtIsManaged
JNIEXPORT jboolean JNICALL OS_NATIVE(XtIsManaged)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XtIsManaged_FUNC);
	rc = (jboolean)XtIsManaged((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtIsManaged_FUNC);
	return rc;
}
#endif

#ifndef NO_XtIsRealized
JNIEXPORT jboolean JNICALL OS_NATIVE(XtIsRealized)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XtIsRealized_FUNC);
	rc = (jboolean)XtIsRealized((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtIsRealized_FUNC);
	return rc;
}
#endif

#ifndef NO_XtIsSubclass
JNIEXPORT jboolean JNICALL OS_NATIVE(XtIsSubclass)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XtIsSubclass_FUNC);
	rc = (jboolean)XtIsSubclass((Widget)arg0, (WidgetClass)arg1);
	OS_NATIVE_EXIT(env, that, XtIsSubclass_FUNC);
	return rc;
}
#endif

#ifndef NO_XtIsTopLevelShell
JNIEXPORT jboolean JNICALL OS_NATIVE(XtIsTopLevelShell)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XtIsTopLevelShell_FUNC);
	rc = (jboolean)XtIsTopLevelShell((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtIsTopLevelShell_FUNC);
	return rc;
}
#endif

#ifndef NO_XtLastTimestampProcessed
JNIEXPORT jint JNICALL OS_NATIVE(XtLastTimestampProcessed)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtLastTimestampProcessed_FUNC);
	rc = (jint)XtLastTimestampProcessed((Display *)arg0);
	OS_NATIVE_EXIT(env, that, XtLastTimestampProcessed_FUNC);
	return rc;
}
#endif

#ifndef NO_XtMalloc
JNIEXPORT jint JNICALL OS_NATIVE(XtMalloc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtMalloc_FUNC);
	rc = (jint)XtMalloc(arg0);
	OS_NATIVE_EXIT(env, that, XtMalloc_FUNC);
	return rc;
}
#endif

#ifndef NO_XtManageChild
JNIEXPORT void JNICALL OS_NATIVE(XtManageChild)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XtManageChild_FUNC);
	XtManageChild((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtManageChild_FUNC);
}
#endif

#ifndef NO_XtMapWidget
JNIEXPORT void JNICALL OS_NATIVE(XtMapWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XtMapWidget_FUNC);
	XtMapWidget((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtMapWidget_FUNC);
}
#endif

#ifndef NO_XtMoveWidget
JNIEXPORT void JNICALL OS_NATIVE(XtMoveWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XtMoveWidget_FUNC);
	XtMoveWidget((Widget)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, XtMoveWidget_FUNC);
}
#endif

#ifndef NO_XtNameToWidget
JNIEXPORT jint JNICALL OS_NATIVE(XtNameToWidget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtNameToWidget_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XtNameToWidget((Widget)arg0, (String)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XtNameToWidget_FUNC);
	return rc;
}
#endif

#ifndef NO_XtOpenDisplay
JNIEXPORT jint JNICALL OS_NATIVE(XtOpenDisplay)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2, jbyteArray arg3, jint arg4, jint arg5, jintArray arg6, jint arg7)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtOpenDisplay_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)XtOpenDisplay((XtAppContext)arg0, (String)lparg1, (String)lparg2, (String)lparg3, (XrmOptionDescRec *)arg4, arg5, (int *)lparg6, (char **)arg7);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XtOpenDisplay_FUNC);
	return rc;
}
#endif

#ifndef NO_XtOverrideTranslations
JNIEXPORT void JNICALL OS_NATIVE(XtOverrideTranslations)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XtOverrideTranslations_FUNC);
	XtOverrideTranslations((Widget)arg0, (XtTranslations)arg1);
	OS_NATIVE_EXIT(env, that, XtOverrideTranslations_FUNC);
}
#endif

#ifndef NO_XtParent
JNIEXPORT jint JNICALL OS_NATIVE(XtParent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtParent_FUNC);
	rc = (jint)XtParent((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtParent_FUNC);
	return rc;
}
#endif

#ifndef NO_XtParseTranslationTable
JNIEXPORT jint JNICALL OS_NATIVE(XtParseTranslationTable)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtParseTranslationTable_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)XtParseTranslationTable((String)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, XtParseTranslationTable_FUNC);
	return rc;
}
#endif

#ifndef NO_XtPopdown
JNIEXPORT void JNICALL OS_NATIVE(XtPopdown)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XtPopdown_FUNC);
	XtPopdown((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtPopdown_FUNC);
}
#endif

#ifndef NO_XtPopup
JNIEXPORT void JNICALL OS_NATIVE(XtPopup)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XtPopup_FUNC);
	XtPopup((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XtPopup_FUNC);
}
#endif

#ifndef NO_XtQueryGeometry
JNIEXPORT jint JNICALL OS_NATIVE(XtQueryGeometry)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	XtWidgetGeometry _arg1, *lparg1=NULL;
	XtWidgetGeometry _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtQueryGeometry_FUNC);
	if (arg1) if ((lparg1 = getXtWidgetGeometryFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getXtWidgetGeometryFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)XtQueryGeometry((Widget)arg0, (XtWidgetGeometry *)lparg1, (XtWidgetGeometry *)lparg2);
fail:
	if (arg2 && lparg2) setXtWidgetGeometryFields(env, arg2, lparg2);
	if (arg1 && lparg1) setXtWidgetGeometryFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, XtQueryGeometry_FUNC);
	return rc;
}
#endif

#ifndef NO_XtRealizeWidget
JNIEXPORT void JNICALL OS_NATIVE(XtRealizeWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XtRealizeWidget_FUNC);
	XtRealizeWidget((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtRealizeWidget_FUNC);
}
#endif

#ifndef NO_XtRegisterDrawable
JNIEXPORT void JNICALL OS_NATIVE(XtRegisterDrawable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, XtRegisterDrawable_FUNC);
	XtRegisterDrawable((Display *)arg0, (Drawable)arg1, (Widget)arg2);
	OS_NATIVE_EXIT(env, that, XtRegisterDrawable_FUNC);
}
#endif

#ifndef NO_XtRemoveEventHandler
JNIEXPORT void JNICALL OS_NATIVE(XtRemoveEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, XtRemoveEventHandler_FUNC);
	XtRemoveEventHandler((Widget)arg0, arg1, arg2, (XtEventHandler)arg3, (XtPointer)arg4);
	OS_NATIVE_EXIT(env, that, XtRemoveEventHandler_FUNC);
}
#endif

#ifndef NO_XtRemoveInput
JNIEXPORT void JNICALL OS_NATIVE(XtRemoveInput)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XtRemoveInput_FUNC);
	XtRemoveInput((XtInputId)arg0);
	OS_NATIVE_EXIT(env, that, XtRemoveInput_FUNC);
}
#endif

#ifndef NO_XtRemoveTimeOut
JNIEXPORT void JNICALL OS_NATIVE(XtRemoveTimeOut)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XtRemoveTimeOut_FUNC);
	XtRemoveTimeOut(arg0);
	OS_NATIVE_EXIT(env, that, XtRemoveTimeOut_FUNC);
}
#endif

#ifndef NO_XtResizeWidget
JNIEXPORT void JNICALL OS_NATIVE(XtResizeWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, XtResizeWidget_FUNC);
	XtResizeWidget((Widget)arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, XtResizeWidget_FUNC);
}
#endif

#ifndef NO_XtResizeWindow
JNIEXPORT void JNICALL OS_NATIVE(XtResizeWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XtResizeWindow_FUNC);
	XtResizeWindow((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtResizeWindow_FUNC);
}
#endif

#ifndef NO_XtSetLanguageProc
JNIEXPORT jint JNICALL OS_NATIVE(XtSetLanguageProc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtSetLanguageProc_FUNC);
	rc = (jint)XtSetLanguageProc((XtAppContext)arg0, (XtLanguageProc)arg1, (XtPointer)arg2);
	OS_NATIVE_EXIT(env, that, XtSetLanguageProc_FUNC);
	return rc;
}
#endif

#ifndef NO_XtSetMappedWhenManaged
JNIEXPORT void JNICALL OS_NATIVE(XtSetMappedWhenManaged)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, XtSetMappedWhenManaged_FUNC);
	XtSetMappedWhenManaged((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, XtSetMappedWhenManaged_FUNC);
}
#endif

#ifndef NO_XtSetValues
JNIEXPORT void JNICALL OS_NATIVE(XtSetValues)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, XtSetValues_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XtSetValues((Widget)arg0, (ArgList)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, XtSetValues_FUNC);
}
#endif

#ifndef NO_XtToolkitInitialize
JNIEXPORT void JNICALL OS_NATIVE(XtToolkitInitialize)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, XtToolkitInitialize_FUNC);
	XtToolkitInitialize();
	OS_NATIVE_EXIT(env, that, XtToolkitInitialize_FUNC);
}
#endif

#ifndef NO_XtToolkitThreadInitialize
JNIEXPORT jboolean JNICALL OS_NATIVE(XtToolkitThreadInitialize)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, XtToolkitThreadInitialize_FUNC);
	rc = (jboolean)XtToolkitThreadInitialize();
	OS_NATIVE_EXIT(env, that, XtToolkitThreadInitialize_FUNC);
	return rc;
}
#endif

#ifndef NO_XtTranslateCoords
JNIEXPORT void JNICALL OS_NATIVE(XtTranslateCoords)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jshortArray arg3, jshortArray arg4)
{
	jshort *lparg3=NULL;
	jshort *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, XtTranslateCoords_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL)) == NULL) goto fail;
	XtTranslateCoords((Widget)arg0, arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, XtTranslateCoords_FUNC);
}
#endif

#ifndef NO_XtUnmanageChild
JNIEXPORT void JNICALL OS_NATIVE(XtUnmanageChild)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XtUnmanageChild_FUNC);
	XtUnmanageChild((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtUnmanageChild_FUNC);
}
#endif

#ifndef NO_XtUnmapWidget
JNIEXPORT void JNICALL OS_NATIVE(XtUnmapWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, XtUnmapWidget_FUNC);
	XtUnmapWidget((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtUnmapWidget_FUNC);
}
#endif

#ifndef NO_XtUnregisterDrawable
JNIEXPORT void JNICALL OS_NATIVE(XtUnregisterDrawable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, XtUnregisterDrawable_FUNC);
	XtUnregisterDrawable((Display *)arg0, (Drawable)arg1);
	OS_NATIVE_EXIT(env, that, XtUnregisterDrawable_FUNC);
}
#endif

#ifndef NO_XtWindow
JNIEXPORT jint JNICALL OS_NATIVE(XtWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtWindow_FUNC);
	rc = (jint)XtWindow((Widget)arg0);
	OS_NATIVE_EXIT(env, that, XtWindow_FUNC);
	return rc;
}
#endif

#ifndef NO_XtWindowToWidget
JNIEXPORT jint JNICALL OS_NATIVE(XtWindowToWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XtWindowToWidget_FUNC);
	rc = (jint)XtWindowToWidget((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, XtWindowToWidget_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmSetMenuTraversal
JNIEXPORT void JNICALL OS_NATIVE(_1XmSetMenuTraversal)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmSetMenuTraversal_FUNC);
	_XmSetMenuTraversal((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmSetMenuTraversal_FUNC);
}
#endif

#ifndef NO_close
JNIEXPORT jint JNICALL OS_NATIVE(close)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, close_FUNC);
	rc = (jint)close(arg0);
	OS_NATIVE_EXIT(env, that, close_FUNC);
	return rc;
}
#endif

#ifndef NO_fd_1set_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(fd_1set_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, fd_1set_1sizeof_FUNC);
	rc = (jint)fd_set_sizeof();
	OS_NATIVE_EXIT(env, that, fd_1set_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO_getenv
JNIEXPORT jint JNICALL OS_NATIVE(getenv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, getenv_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)getenv((const char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, getenv_FUNC);
	return rc;
}
#endif

#ifndef NO_iconv
JNIEXPORT jint JNICALL OS_NATIVE(iconv)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2, jintArray arg3, jintArray arg4)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, iconv_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)iconv((iconv_t)arg0, (void *)lparg1, (size_t *)lparg2, (char **)lparg3, (size_t *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, iconv_FUNC);
	return rc;
}
#endif

#ifndef NO_iconv_1close
JNIEXPORT jint JNICALL OS_NATIVE(iconv_1close)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, iconv_1close_FUNC);
	rc = (jint)iconv_close((iconv_t)arg0);
	OS_NATIVE_EXIT(env, that, iconv_1close_FUNC);
	return rc;
}
#endif

#ifndef NO_iconv_1open
JNIEXPORT jint JNICALL OS_NATIVE(iconv_1open)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, iconv_1open_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)iconv_open((const char *)lparg0, (const char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, iconv_1open_FUNC);
	return rc;
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XButtonEvent_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XButtonEvent_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XButtonEvent _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_motif_XButtonEvent_2I_FUNC);
	if (arg1) if ((lparg1 = getXButtonEventFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_motif_XButtonEvent_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XClientMessageEvent_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XClientMessageEvent_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XClientMessageEvent _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_motif_XClientMessageEvent_2I_FUNC);
	if (arg1) if ((lparg1 = getXClientMessageEventFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_motif_XClientMessageEvent_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XConfigureEvent_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XConfigureEvent_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XConfigureEvent _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_motif_XConfigureEvent_2I_FUNC);
	if (arg1) if ((lparg1 = getXConfigureEventFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_motif_XConfigureEvent_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XExposeEvent_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XExposeEvent_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XExposeEvent _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_motif_XExposeEvent_2I_FUNC);
	if (arg1) if ((lparg1 = getXExposeEventFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_motif_XExposeEvent_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XImage_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XImage_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XImage _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_motif_XImage_2I_FUNC);
	if (arg1) if ((lparg1 = getXImageFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_motif_XImage_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XKeyEvent_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XKeyEvent_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XKeyEvent _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_motif_XKeyEvent_2I_FUNC);
	if (arg1) if ((lparg1 = getXKeyEventFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_motif_XKeyEvent_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XmDragProcCallbackStruct _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2I_FUNC);
	if (arg1) if ((lparg1 = getXmDragProcCallbackStructFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XmTextBlockRec_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XmTextBlockRec_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XmTextBlockRec _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_motif_XmTextBlockRec_2I_FUNC);
	if (arg1) if ((lparg1 = getXmTextBlockRecFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_motif_XmTextBlockRec_2I_FUNC);
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XmTextVerifyCallbackStruct _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2I_FUNC);
	if (arg1) if ((lparg1 = getXmTextVerifyCallbackStructFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2I_FUNC);
}
#endif

#ifndef NO_memmove__I_3BI
JNIEXPORT void JNICALL OS_NATIVE(memmove__I_3BI)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__I_3BI_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, memmove__I_3BI_FUNC);
}
#endif

#ifndef NO_memmove__I_3CI
JNIEXPORT void JNICALL OS_NATIVE(memmove__I_3CI)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__I_3CI_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, memmove__I_3CI_FUNC);
}
#endif

#ifndef NO_memmove__I_3II
JNIEXPORT void JNICALL OS_NATIVE(memmove__I_3II)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__I_3II_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, memmove__I_3II_FUNC);
}
#endif

#ifndef NO_memmove__I_3SI
JNIEXPORT void JNICALL OS_NATIVE(memmove__I_3SI)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jint arg2)
{
	jshort *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__I_3SI_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, JNI_ABORT);
	OS_NATIVE_EXIT(env, that, memmove__I_3SI_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_Visual_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_Visual_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	Visual _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_Visual_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setVisualFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_Visual_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XAnyEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XAnyEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XAnyEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XAnyEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXAnyEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XAnyEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XButtonEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XButtonEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XButtonEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XButtonEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXButtonEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XButtonEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XCharStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XCharStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XCharStruct _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XCharStruct_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXCharStructFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XCharStruct_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XClientMessageEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XClientMessageEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XClientMessageEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XClientMessageEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXClientMessageEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XClientMessageEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XConfigureEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XConfigureEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XConfigureEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XConfigureEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXConfigureEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XConfigureEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XCreateWindowEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XCreateWindowEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XCreateWindowEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XCreateWindowEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXCreateWindowEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XCreateWindowEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XCrossingEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XCrossingEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XCrossingEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XCrossingEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXCrossingEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XCrossingEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XDestroyWindowEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XDestroyWindowEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XDestroyWindowEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XDestroyWindowEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXDestroyWindowEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XDestroyWindowEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XEvent_2II_FUNC);
	if (arg0) if ((lparg0 = getXEventFields(env, arg0, &_arg0)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XExposeEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XExposeEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XExposeEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XExposeEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXExposeEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XExposeEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XFocusChangeEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XFocusChangeEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XFocusChangeEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XFocusChangeEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXFocusChangeEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XFocusChangeEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XFontStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XFontStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XFontStruct _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XFontStruct_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXFontStructFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XFontStruct_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XIconSize_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XIconSize_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XIconSize _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XIconSize_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXIconSizeFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XIconSize_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XImage_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XImage_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XImage _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XImage_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXImageFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XImage_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XKeyEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXKeyEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XModifierKeymap_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XModifierKeymap_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XModifierKeymap _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XModifierKeymap_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXModifierKeymapFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XModifierKeymap_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XMotionEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XMotionEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XMotionEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XMotionEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXMotionEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XMotionEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XPropertyEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XPropertyEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XPropertyEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XPropertyEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXPropertyEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XPropertyEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XReparentEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XReparentEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XReparentEvent _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XReparentEvent_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXReparentEventFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XReparentEvent_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XineramaScreenInfo_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XineramaScreenInfo_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XineramaScreenInfo _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XineramaScreenInfo_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXineramaScreenInfoFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XineramaScreenInfo_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmAnyCallbackStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmAnyCallbackStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmAnyCallbackStruct _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmAnyCallbackStruct_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXmAnyCallbackStructFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmAnyCallbackStruct_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmDragProcCallbackStruct _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXmDragProcCallbackStructFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmDropFinishCallbackStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmDropFinishCallbackStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmDropFinishCallbackStruct _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmDropFinishCallbackStruct_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXmDropFinishCallbackStructFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmDropFinishCallbackStruct_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmDropProcCallbackStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmDropProcCallbackStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmDropProcCallbackStruct _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmDropProcCallbackStruct_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXmDropProcCallbackStructFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmDropProcCallbackStruct_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmTextBlockRec_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmTextBlockRec_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmTextBlockRec _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmTextBlockRec_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXmTextBlockRecFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmTextBlockRec_2II_FUNC);
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmTextVerifyCallbackStruct _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXmTextVerifyCallbackStructFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2II_FUNC);
}
#endif

#ifndef NO_memmove___3BII
JNIEXPORT void JNICALL OS_NATIVE(memmove___3BII)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove___3BII_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, memmove___3BII_FUNC);
}
#endif

#ifndef NO_memmove___3CII
JNIEXPORT void JNICALL OS_NATIVE(memmove___3CII)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove___3CII_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, memmove___3CII_FUNC);
}
#endif

#ifndef NO_memmove___3III
JNIEXPORT void JNICALL OS_NATIVE(memmove___3III)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove___3III_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, memmove___3III_FUNC);
}
#endif

#ifndef NO_nl_1langinfo
JNIEXPORT jint JNICALL OS_NATIVE(nl_1langinfo)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, nl_1langinfo_FUNC);
	rc = (jint)nl_langinfo(arg0);
	OS_NATIVE_EXIT(env, that, nl_1langinfo_FUNC);
	return rc;
}
#endif

#ifndef NO_overrideShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(overrideShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, overrideShellWidgetClass_FUNC);
	rc = (jint)overrideShellWidgetClass;
	OS_NATIVE_EXIT(env, that, overrideShellWidgetClass_FUNC);
	return rc;
}
#endif

#ifndef NO_pipe
JNIEXPORT jint JNICALL OS_NATIVE(pipe)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, pipe_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)pipe((int *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, pipe_FUNC);
	return rc;
}
#endif

#ifndef NO_read
JNIEXPORT jint JNICALL OS_NATIVE(read)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, read_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)read(arg0, (char *)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, read_FUNC);
	return rc;
}
#endif

#ifndef NO_select
JNIEXPORT jint JNICALL OS_NATIVE(select)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2, jbyteArray arg3, jintArray arg4)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, select_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)select(arg0, (fd_set *)lparg1, (fd_set *)lparg2, (fd_set *)lparg3, (struct timeval *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, select_FUNC);
	return rc;
}
#endif

#ifndef NO_setlocale
JNIEXPORT jint JNICALL OS_NATIVE(setlocale)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, setlocale_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)setlocale(arg0, (char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, setlocale_FUNC);
	return rc;
}
#endif

#ifndef NO_shellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(shellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, shellWidgetClass_FUNC);
	rc = (jint)shellWidgetClass;
	OS_NATIVE_EXIT(env, that, shellWidgetClass_FUNC);
	return rc;
}
#endif

#ifndef NO_strlen
JNIEXPORT jint JNICALL OS_NATIVE(strlen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, strlen_FUNC);
	rc = (jint)strlen((char *)arg0);
	OS_NATIVE_EXIT(env, that, strlen_FUNC);
	return rc;
}
#endif

#ifndef NO_topLevelShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(topLevelShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, topLevelShellWidgetClass_FUNC);
	rc = (jint)topLevelShellWidgetClass;
	OS_NATIVE_EXIT(env, that, topLevelShellWidgetClass_FUNC);
	return rc;
}
#endif

#ifndef NO_transientShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(transientShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, transientShellWidgetClass_FUNC);
	rc = (jint)transientShellWidgetClass;
	OS_NATIVE_EXIT(env, that, transientShellWidgetClass_FUNC);
	return rc;
}
#endif

#ifndef NO_write
JNIEXPORT jint JNICALL OS_NATIVE(write)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, write_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)write(arg0, (char *)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, write_FUNC);
	return rc;
}
#endif

#ifndef NO_xmMenuShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(xmMenuShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, xmMenuShellWidgetClass_FUNC);
	rc = (jint)xmMenuShellWidgetClass;
	OS_NATIVE_EXIT(env, that, xmMenuShellWidgetClass_FUNC);
	return rc;
}
#endif

