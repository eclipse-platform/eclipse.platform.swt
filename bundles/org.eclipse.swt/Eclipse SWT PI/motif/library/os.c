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

#ifndef NO_XRenderPictureAttributes_1sizeof
JNIEXPORT jint JNICALL OS_NATIVE(XRenderPictureAttributes_1sizeof)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, XRenderPictureAttributes_1sizeof_FUNC);
	rc = (jint)XRenderPictureAttributes_sizeof();
	OS_NATIVE_EXIT(env, that, XRenderPictureAttributes_1sizeof_FUNC);
	return rc;
}
#endif

#ifndef NO__1Call
JNIEXPORT jint JNICALL OS_NATIVE(_1Call)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1Call_FUNC);
	rc = (jint)((jint (*)())arg0)(arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1Call_FUNC);
	return rc;
}
#endif

#ifndef NO__1ConnectionNumber
JNIEXPORT jint JNICALL OS_NATIVE(_1ConnectionNumber)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1ConnectionNumber_FUNC);
	rc = (jint)ConnectionNumber(arg0);
	OS_NATIVE_EXIT(env, that, _1ConnectionNumber_FUNC);
	return rc;
}
#endif

#ifndef NO__1XAllocColor
JNIEXPORT jint JNICALL OS_NATIVE(_1XAllocColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	XColor _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XAllocColor_FUNC);
	if (arg2) if ((lparg2 = getXColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)XAllocColor((Display *)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setXColorFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1XAllocColor_FUNC);
	return rc;
}
#endif

#ifndef NO__1XBell
JNIEXPORT void JNICALL OS_NATIVE(_1XBell)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XBell_FUNC);
	XBell((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XBell_FUNC);
}
#endif

#ifndef NO__1XBlackPixel
JNIEXPORT jint JNICALL OS_NATIVE(_1XBlackPixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XBlackPixel_FUNC);
	rc = (jint)XBlackPixel((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XBlackPixel_FUNC);
	return rc;
}
#endif

#ifndef NO__1XChangeActivePointerGrab
JNIEXPORT jint JNICALL OS_NATIVE(_1XChangeActivePointerGrab)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XChangeActivePointerGrab_FUNC);
	rc = (jint)XChangeActivePointerGrab((Display *)arg0, arg1, (Cursor)arg2, (Time)arg3);
	OS_NATIVE_EXIT(env, that, _1XChangeActivePointerGrab_FUNC);
	return rc;
}
#endif

#ifndef NO__1XChangeProperty
JNIEXPORT void JNICALL OS_NATIVE(_1XChangeProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jint arg7)
{
	jint *lparg6=NULL;
	OS_NATIVE_ENTER(env, that, _1XChangeProperty_FUNC);
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	XChangeProperty((Display *)arg0, (Window)arg1, (Atom)arg2, (Atom)arg3, arg4, arg5, (unsigned char *)lparg6, arg7);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, _1XChangeProperty_FUNC);
}
#endif

#ifndef NO__1XChangeWindowAttributes
JNIEXPORT void JNICALL OS_NATIVE(_1XChangeWindowAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	XSetWindowAttributes _arg3, *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1XChangeWindowAttributes_FUNC);
	if (arg3) if ((lparg3 = getXSetWindowAttributesFields(env, arg3, &_arg3)) == NULL) goto fail;
	XChangeWindowAttributes((Display *)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setXSetWindowAttributesFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, _1XChangeWindowAttributes_FUNC);
}
#endif

#ifndef NO__1XCheckIfEvent
JNIEXPORT jint JNICALL OS_NATIVE(_1XCheckIfEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCheckIfEvent_FUNC);
	rc = (jint)XCheckIfEvent((Display *)arg0, (XEvent *)arg1, (Bool (*)())arg2, (XPointer)arg3);
	OS_NATIVE_EXIT(env, that, _1XCheckIfEvent_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCheckMaskEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XCheckMaskEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCheckMaskEvent_FUNC);
	rc = (jboolean)XCheckMaskEvent((Display *)arg0, arg1, (XEvent *)arg2);
	OS_NATIVE_EXIT(env, that, _1XCheckMaskEvent_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCheckWindowEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XCheckWindowEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCheckWindowEvent_FUNC);
	rc = (jboolean)XCheckWindowEvent((Display *)arg0, (Window)arg1, arg2, (XEvent *)arg3);
	OS_NATIVE_EXIT(env, that, _1XCheckWindowEvent_FUNC);
	return rc;
}
#endif

#ifndef NO__1XClearArea
JNIEXPORT void JNICALL OS_NATIVE(_1XClearArea)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jboolean arg6)
{
	OS_NATIVE_ENTER(env, that, _1XClearArea_FUNC);
	XClearArea((Display *)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, _1XClearArea_FUNC);
}
#endif

#ifndef NO__1XClipBox
JNIEXPORT void JNICALL OS_NATIVE(_1XClipBox)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	XRectangle _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1XClipBox_FUNC);
	if (arg1) if ((lparg1 = getXRectangleFields(env, arg1, &_arg1)) == NULL) goto fail;
	XClipBox((Region)arg0, (XRectangle *)lparg1);
fail:
	if (arg1 && lparg1) setXRectangleFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1XClipBox_FUNC);
}
#endif

#ifndef NO__1XCloseDisplay
JNIEXPORT void JNICALL OS_NATIVE(_1XCloseDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XCloseDisplay_FUNC);
	XCloseDisplay((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XCloseDisplay_FUNC);
}
#endif

#ifndef NO__1XCopyArea
JNIEXPORT void JNICALL OS_NATIVE(_1XCopyArea)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	OS_NATIVE_ENTER(env, that, _1XCopyArea_FUNC);
	XCopyArea((Display *)arg0, arg1, arg2, (GC)arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	OS_NATIVE_EXIT(env, that, _1XCopyArea_FUNC);
}
#endif

#ifndef NO__1XCopyPlane
JNIEXPORT void JNICALL OS_NATIVE(_1XCopyPlane)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	OS_NATIVE_ENTER(env, that, _1XCopyPlane_FUNC);
	XCopyPlane((Display *)arg0, arg1, arg2, (GC)arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	OS_NATIVE_EXIT(env, that, _1XCopyPlane_FUNC);
}
#endif

#ifndef NO__1XCreateBitmapFromData
JNIEXPORT jint JNICALL OS_NATIVE(_1XCreateBitmapFromData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCreateBitmapFromData_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XCreateBitmapFromData((Display *)arg0, arg1, (char *)lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XCreateBitmapFromData_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCreateColormap
JNIEXPORT jint JNICALL OS_NATIVE(_1XCreateColormap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCreateColormap_FUNC);
	rc = (jint)XCreateColormap((Display *)arg0, (Window)arg1, (Visual *)arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1XCreateColormap_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCreateFontCursor
JNIEXPORT jint JNICALL OS_NATIVE(_1XCreateFontCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCreateFontCursor_FUNC);
	rc = (jint)XCreateFontCursor((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XCreateFontCursor_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCreateGC
JNIEXPORT jint JNICALL OS_NATIVE(_1XCreateGC)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	XGCValues _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCreateGC_FUNC);
	if (arg3) if ((lparg3 = getXGCValuesFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)XCreateGC((Display *)arg0, arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setXGCValuesFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, _1XCreateGC_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCreateImage
JNIEXPORT jint JNICALL OS_NATIVE(_1XCreateImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCreateImage_FUNC);
	rc = (jint)XCreateImage((Display *)arg0, (Visual *)arg1, arg2, arg3, arg4, (char *)arg5, arg6, arg7, arg8, arg9);
	OS_NATIVE_EXIT(env, that, _1XCreateImage_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCreatePixmap
JNIEXPORT jint JNICALL OS_NATIVE(_1XCreatePixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCreatePixmap_FUNC);
	rc = (jint)XCreatePixmap((Display *)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, _1XCreatePixmap_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCreatePixmapCursor
JNIEXPORT jint JNICALL OS_NATIVE(_1XCreatePixmapCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jint arg5, jint arg6)
{
	XColor _arg3, *lparg3=NULL;
	XColor _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCreatePixmapCursor_FUNC);
	if (arg3) if ((lparg3 = getXColorFields(env, arg3, &_arg3)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getXColorFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)XCreatePixmapCursor((Display *)arg0, (Pixmap)arg1, (Pixmap)arg2, lparg3, lparg4, arg5, arg6);
fail:
	if (arg4 && lparg4) setXColorFields(env, arg4, lparg4);
	if (arg3 && lparg3) setXColorFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, _1XCreatePixmapCursor_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCreateRegion
JNIEXPORT jint JNICALL OS_NATIVE(_1XCreateRegion)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCreateRegion_FUNC);
	rc = (jint)XCreateRegion();
	OS_NATIVE_EXIT(env, that, _1XCreateRegion_FUNC);
	return rc;
}
#endif

#ifndef NO__1XCreateWindow
JNIEXPORT jint JNICALL OS_NATIVE(_1XCreateWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jlong arg10, jobject arg11)
{
	XSetWindowAttributes _arg11, *lparg11=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XCreateWindow_FUNC);
	if (arg11) if ((lparg11 = getXSetWindowAttributesFields(env, arg11, &_arg11)) == NULL) goto fail;
	rc = (jint)XCreateWindow((Display *)arg0, (Window)arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, (Visual *)arg9, arg10, (XSetWindowAttributes *)lparg11);
fail:
	if (arg11 && lparg11) setXSetWindowAttributesFields(env, arg11, lparg11);
	OS_NATIVE_EXIT(env, that, _1XCreateWindow_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDefaultColormap
JNIEXPORT jint JNICALL OS_NATIVE(_1XDefaultColormap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDefaultColormap_FUNC);
	rc = (jint)XDefaultColormap((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XDefaultColormap_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDefaultColormapOfScreen
JNIEXPORT jint JNICALL OS_NATIVE(_1XDefaultColormapOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDefaultColormapOfScreen_FUNC);
	rc = (jint)XDefaultColormapOfScreen((Screen *)arg0);
	OS_NATIVE_EXIT(env, that, _1XDefaultColormapOfScreen_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDefaultDepthOfScreen
JNIEXPORT jint JNICALL OS_NATIVE(_1XDefaultDepthOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDefaultDepthOfScreen_FUNC);
	rc = (jint)XDefaultDepthOfScreen((Screen *)arg0);
	OS_NATIVE_EXIT(env, that, _1XDefaultDepthOfScreen_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDefaultGCOfScreen
JNIEXPORT jint JNICALL OS_NATIVE(_1XDefaultGCOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDefaultGCOfScreen_FUNC);
	rc = (jint)XDefaultGCOfScreen((Screen *)arg0);
	OS_NATIVE_EXIT(env, that, _1XDefaultGCOfScreen_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDefaultRootWindow
JNIEXPORT jint JNICALL OS_NATIVE(_1XDefaultRootWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDefaultRootWindow_FUNC);
	rc = (jint)XDefaultRootWindow((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XDefaultRootWindow_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDefaultScreen
JNIEXPORT jint JNICALL OS_NATIVE(_1XDefaultScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDefaultScreen_FUNC);
	rc = (jint)XDefaultScreen((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XDefaultScreen_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDefaultScreenOfDisplay
JNIEXPORT jint JNICALL OS_NATIVE(_1XDefaultScreenOfDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDefaultScreenOfDisplay_FUNC);
	rc = (jint)XDefaultScreenOfDisplay((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XDefaultScreenOfDisplay_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDefaultVisual
JNIEXPORT jint JNICALL OS_NATIVE(_1XDefaultVisual)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDefaultVisual_FUNC);
	rc = (jint)XDefaultVisual((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XDefaultVisual_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDefineCursor
JNIEXPORT void JNICALL OS_NATIVE(_1XDefineCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XDefineCursor_FUNC);
	XDefineCursor((Display *)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XDefineCursor_FUNC);
}
#endif

#ifndef NO__1XDestroyImage
JNIEXPORT jint JNICALL OS_NATIVE(_1XDestroyImage)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDestroyImage_FUNC);
	rc = (jint)XDestroyImage((XImage *)arg0);
	OS_NATIVE_EXIT(env, that, _1XDestroyImage_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDestroyRegion
JNIEXPORT void JNICALL OS_NATIVE(_1XDestroyRegion)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XDestroyRegion_FUNC);
	XDestroyRegion((Region)arg0);
	OS_NATIVE_EXIT(env, that, _1XDestroyRegion_FUNC);
}
#endif

#ifndef NO__1XDestroyWindow
JNIEXPORT void JNICALL OS_NATIVE(_1XDestroyWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XDestroyWindow_FUNC);
	XDestroyWindow((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, _1XDestroyWindow_FUNC);
}
#endif

#ifndef NO__1XDisplayHeight
JNIEXPORT jint JNICALL OS_NATIVE(_1XDisplayHeight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDisplayHeight_FUNC);
	rc = (jint)XDisplayHeight((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XDisplayHeight_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDisplayHeightMM
JNIEXPORT jint JNICALL OS_NATIVE(_1XDisplayHeightMM)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDisplayHeightMM_FUNC);
	rc = (jint)XDisplayHeightMM((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XDisplayHeightMM_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDisplayWidth
JNIEXPORT jint JNICALL OS_NATIVE(_1XDisplayWidth)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDisplayWidth_FUNC);
	rc = (jint)XDisplayWidth((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XDisplayWidth_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDisplayWidthMM
JNIEXPORT jint JNICALL OS_NATIVE(_1XDisplayWidthMM)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XDisplayWidthMM_FUNC);
	rc = (jint)XDisplayWidthMM((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XDisplayWidthMM_FUNC);
	return rc;
}
#endif

#ifndef NO__1XDrawArc
JNIEXPORT void JNICALL OS_NATIVE(_1XDrawArc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	OS_NATIVE_ENTER(env, that, _1XDrawArc_FUNC);
	XDrawArc((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, _1XDrawArc_FUNC);
}
#endif

#ifndef NO__1XDrawLine
JNIEXPORT void JNICALL OS_NATIVE(_1XDrawLine)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	OS_NATIVE_ENTER(env, that, _1XDrawLine_FUNC);
	XDrawLine((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, _1XDrawLine_FUNC);
}
#endif

#ifndef NO__1XDrawLines
JNIEXPORT void JNICALL OS_NATIVE(_1XDrawLines)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3, jint arg4, jint arg5)
{
	jshort *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1XDrawLines_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	XDrawLines((Display *)arg0, (Drawable)arg1, (GC)arg2, (XPoint *)lparg3, arg4, arg5);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1XDrawLines_FUNC);
}
#endif

#ifndef NO__1XDrawPoint
JNIEXPORT void JNICALL OS_NATIVE(_1XDrawPoint)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, _1XDrawPoint_FUNC);
	XDrawPoint((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, _1XDrawPoint_FUNC);
}
#endif

#ifndef NO__1XDrawRectangle
JNIEXPORT void JNICALL OS_NATIVE(_1XDrawRectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	OS_NATIVE_ENTER(env, that, _1XDrawRectangle_FUNC);
	XDrawRectangle((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, _1XDrawRectangle_FUNC);
}
#endif

#ifndef NO__1XEmptyRegion
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XEmptyRegion)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XEmptyRegion_FUNC);
	rc = (jboolean)XEmptyRegion((Region)arg0);
	OS_NATIVE_EXIT(env, that, _1XEmptyRegion_FUNC);
	return rc;
}
#endif

#ifndef NO__1XEventsQueued
JNIEXPORT jint JNICALL OS_NATIVE(_1XEventsQueued)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XEventsQueued_FUNC);
	rc = (jint)XEventsQueued((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XEventsQueued_FUNC);
	return rc;
}
#endif

#ifndef NO__1XFillArc
JNIEXPORT void JNICALL OS_NATIVE(_1XFillArc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	OS_NATIVE_ENTER(env, that, _1XFillArc_FUNC);
	XFillArc((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, _1XFillArc_FUNC);
}
#endif

#ifndef NO__1XFillPolygon
JNIEXPORT jint JNICALL OS_NATIVE(_1XFillPolygon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3, jint arg4, jint arg5, jint arg6)
{
	jshort *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XFillPolygon_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XFillPolygon((Display *)arg0, (Drawable)arg1, (GC)arg2, (XPoint *)lparg3, arg4, arg5, arg6);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1XFillPolygon_FUNC);
	return rc;
}
#endif

#ifndef NO__1XFillRectangle
JNIEXPORT void JNICALL OS_NATIVE(_1XFillRectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	OS_NATIVE_ENTER(env, that, _1XFillRectangle_FUNC);
	XFillRectangle((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6);
	OS_NATIVE_EXIT(env, that, _1XFillRectangle_FUNC);
}
#endif

#ifndef NO__1XFilterEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XFilterEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XFilterEvent_FUNC);
	rc = (jboolean)XFilterEvent((XEvent *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, _1XFilterEvent_FUNC);
	return rc;
}
#endif

#ifndef NO__1XFlush
JNIEXPORT void JNICALL OS_NATIVE(_1XFlush)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XFlush_FUNC);
	XFlush((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XFlush_FUNC);
}
#endif

#ifndef NO__1XFontsOfFontSet
JNIEXPORT jint JNICALL OS_NATIVE(_1XFontsOfFontSet)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XFontsOfFontSet_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XFontsOfFontSet((XFontSet)arg0, (XFontStruct ***)lparg1, (char ***)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XFontsOfFontSet_FUNC);
	return rc;
}
#endif

#ifndef NO__1XFree
JNIEXPORT jint JNICALL OS_NATIVE(_1XFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XFree_FUNC);
	rc = (jint)XFree((char *)arg0);
	OS_NATIVE_EXIT(env, that, _1XFree_FUNC);
	return rc;
}
#endif

#ifndef NO__1XFreeColormap
JNIEXPORT jint JNICALL OS_NATIVE(_1XFreeColormap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XFreeColormap_FUNC);
	rc = (jint)XFreeColormap((Display *)arg0, (Colormap)arg1);
	OS_NATIVE_EXIT(env, that, _1XFreeColormap_FUNC);
	return rc;
}
#endif

#ifndef NO__1XFreeColors
JNIEXPORT jint JNICALL OS_NATIVE(_1XFreeColors)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3, jint arg4)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XFreeColors_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XFreeColors((Display *)arg0, arg1, (unsigned long *)lparg2, arg3, arg4);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XFreeColors_FUNC);
	return rc;
}
#endif

#ifndef NO__1XFreeCursor
JNIEXPORT void JNICALL OS_NATIVE(_1XFreeCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XFreeCursor_FUNC);
	XFreeCursor((Display *)arg0, (Cursor)arg1);
	OS_NATIVE_EXIT(env, that, _1XFreeCursor_FUNC);
}
#endif

#ifndef NO__1XFreeFont
JNIEXPORT void JNICALL OS_NATIVE(_1XFreeFont)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XFreeFont_FUNC);
	XFreeFont((Display *)arg0, (XFontStruct *)arg1);
	OS_NATIVE_EXIT(env, that, _1XFreeFont_FUNC);
}
#endif

#ifndef NO__1XFreeFontNames
JNIEXPORT void JNICALL OS_NATIVE(_1XFreeFontNames)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XFreeFontNames_FUNC);
	XFreeFontNames((char **)arg0);
	OS_NATIVE_EXIT(env, that, _1XFreeFontNames_FUNC);
}
#endif

#ifndef NO__1XFreeFontPath
JNIEXPORT jint JNICALL OS_NATIVE(_1XFreeFontPath)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XFreeFontPath_FUNC);
	rc = (jint)XFreeFontPath((char **)arg0);
	OS_NATIVE_EXIT(env, that, _1XFreeFontPath_FUNC);
	return rc;
}
#endif

#ifndef NO__1XFreeGC
JNIEXPORT void JNICALL OS_NATIVE(_1XFreeGC)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XFreeGC_FUNC);
	XFreeGC((Display *)arg0, (GC)arg1);
	OS_NATIVE_EXIT(env, that, _1XFreeGC_FUNC);
}
#endif

#ifndef NO__1XFreeModifiermap
JNIEXPORT void JNICALL OS_NATIVE(_1XFreeModifiermap)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XFreeModifiermap_FUNC);
	XFreeModifiermap((XModifierKeymap *)arg0);
	OS_NATIVE_EXIT(env, that, _1XFreeModifiermap_FUNC);
}
#endif

#ifndef NO__1XFreePixmap
JNIEXPORT void JNICALL OS_NATIVE(_1XFreePixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XFreePixmap_FUNC);
	XFreePixmap((Display *)arg0, (Pixmap)arg1);
	OS_NATIVE_EXIT(env, that, _1XFreePixmap_FUNC);
}
#endif

#ifndef NO__1XFreeStringList
JNIEXPORT void JNICALL OS_NATIVE(_1XFreeStringList)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XFreeStringList_FUNC);
	XFreeStringList((char **)arg0);
	OS_NATIVE_EXIT(env, that, _1XFreeStringList_FUNC);
}
#endif

#ifndef NO__1XGetFontPath
JNIEXPORT jint JNICALL OS_NATIVE(_1XGetFontPath)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XGetFontPath_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XGetFontPath((Display *)arg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XGetFontPath_FUNC);
	return rc;
}
#endif

#ifndef NO__1XGetGCValues
JNIEXPORT jint JNICALL OS_NATIVE(_1XGetGCValues)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	XGCValues _arg3, *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XGetGCValues_FUNC);
	if (arg3) if ((lparg3 = getXGCValuesFields(env, arg3, &_arg3)) == NULL) goto fail;
	rc = (jint)XGetGCValues((Display *)arg0, (GC)arg1, arg2, lparg3);
fail:
	if (arg3 && lparg3) setXGCValuesFields(env, arg3, lparg3);
	OS_NATIVE_EXIT(env, that, _1XGetGCValues_FUNC);
	return rc;
}
#endif

#ifndef NO__1XGetGeometry
JNIEXPORT jint JNICALL OS_NATIVE(_1XGetGeometry)
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
	OS_NATIVE_ENTER(env, that, _1XGetGeometry_FUNC);
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
	OS_NATIVE_EXIT(env, that, _1XGetGeometry_FUNC);
	return rc;
}
#endif

#ifndef NO__1XGetIconSizes
JNIEXPORT jint JNICALL OS_NATIVE(_1XGetIconSizes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XGetIconSizes_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XGetIconSizes((Display *)arg0, (Window)arg1, (XIconSize **)lparg2, (int *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XGetIconSizes_FUNC);
	return rc;
}
#endif

#ifndef NO__1XGetImage
JNIEXPORT jint JNICALL OS_NATIVE(_1XGetImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XGetImage_FUNC);
	rc = (jint)XGetImage((Display *)arg0, (Drawable)arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	OS_NATIVE_EXIT(env, that, _1XGetImage_FUNC);
	return rc;
}
#endif

#ifndef NO__1XGetInputFocus
JNIEXPORT jint JNICALL OS_NATIVE(_1XGetInputFocus)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XGetInputFocus_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XGetInputFocus((Display *)arg0, (Window *)lparg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XGetInputFocus_FUNC);
	return rc;
}
#endif

#ifndef NO__1XGetModifierMapping
JNIEXPORT jint JNICALL OS_NATIVE(_1XGetModifierMapping)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XGetModifierMapping_FUNC);
	rc = (jint)XGetModifierMapping((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XGetModifierMapping_FUNC);
	return rc;
}
#endif

#ifndef NO__1XGetWindowAttributes
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XGetWindowAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	XWindowAttributes _arg2, *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XGetWindowAttributes_FUNC);
	if (arg2) if ((lparg2 = getXWindowAttributesFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jboolean)XGetWindowAttributes((Display *)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setXWindowAttributesFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1XGetWindowAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO__1XGetWindowProperty
JNIEXPORT jint JNICALL OS_NATIVE(_1XGetWindowProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jboolean arg5, jint arg6, jintArray arg7, jintArray arg8, jintArray arg9, jintArray arg10, jintArray arg11)
{
	jint *lparg7=NULL;
	jint *lparg8=NULL;
	jint *lparg9=NULL;
	jint *lparg10=NULL;
	jint *lparg11=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XGetWindowProperty_FUNC);
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
	OS_NATIVE_EXIT(env, that, _1XGetWindowProperty_FUNC);
	return rc;
}
#endif

#ifndef NO__1XGrabKeyboard
JNIEXPORT jint JNICALL OS_NATIVE(_1XGrabKeyboard)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XGrabKeyboard_FUNC);
	rc = (jint)XGrabKeyboard((Display *)arg0, arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, _1XGrabKeyboard_FUNC);
	return rc;
}
#endif

#ifndef NO__1XGrabPointer
JNIEXPORT jint JNICALL OS_NATIVE(_1XGrabPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XGrabPointer_FUNC);
	rc = (jint)XGrabPointer((Display *)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, _1XGrabPointer_FUNC);
	return rc;
}
#endif

#ifndef NO__1XInitThreads
JNIEXPORT jint JNICALL OS_NATIVE(_1XInitThreads)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XInitThreads_FUNC);
	rc = (jint)XInitThreads();
	OS_NATIVE_EXIT(env, that, _1XInitThreads_FUNC);
	return rc;
}
#endif

#ifndef NO__1XInternAtom
JNIEXPORT jint JNICALL OS_NATIVE(_1XInternAtom)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jboolean arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XInternAtom_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XInternAtom((Display *)arg0, (char *)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XInternAtom_FUNC);
	return rc;
}
#endif

#ifndef NO__1XIntersectRegion
JNIEXPORT void JNICALL OS_NATIVE(_1XIntersectRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XIntersectRegion_FUNC);
	XIntersectRegion((Region)arg0, (Region)arg1, (Region)arg2);
	OS_NATIVE_EXIT(env, that, _1XIntersectRegion_FUNC);
}
#endif

#ifndef NO__1XKeysymToKeycode
JNIEXPORT jint JNICALL OS_NATIVE(_1XKeysymToKeycode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XKeysymToKeycode_FUNC);
	rc = (jint)XKeysymToKeycode((Display *)arg0, (KeySym)arg1);
	OS_NATIVE_EXIT(env, that, _1XKeysymToKeycode_FUNC);
	return rc;
}
#endif

#ifndef NO__1XKeysymToString
JNIEXPORT jint JNICALL OS_NATIVE(_1XKeysymToString)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XKeysymToString_FUNC);
	rc = (jint)XKeysymToString(arg0);
	OS_NATIVE_EXIT(env, that, _1XKeysymToString_FUNC);
	return rc;
}
#endif

#ifndef NO__1XListFonts
JNIEXPORT jint JNICALL OS_NATIVE(_1XListFonts)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XListFonts_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XListFonts((Display *)arg0, (char *)lparg1, arg2, (int *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XListFonts_FUNC);
	return rc;
}
#endif

#ifndef NO__1XListProperties
JNIEXPORT jint JNICALL OS_NATIVE(_1XListProperties)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XListProperties_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XListProperties((Display *)arg0, (Window)arg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XListProperties_FUNC);
	return rc;
}
#endif

#ifndef NO__1XLocaleOfFontSet
JNIEXPORT jint JNICALL OS_NATIVE(_1XLocaleOfFontSet)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XLocaleOfFontSet_FUNC);
	rc = (jint)XLocaleOfFontSet((XFontSet)arg0);
	OS_NATIVE_EXIT(env, that, _1XLocaleOfFontSet_FUNC);
	return rc;
}
#endif

#ifndef NO__1XLookupString
JNIEXPORT jint JNICALL OS_NATIVE(_1XLookupString)
	(JNIEnv *env, jclass that, jobject arg0, jbyteArray arg1, jint arg2, jintArray arg3, jintArray arg4)
{
	XKeyEvent _arg0, *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XLookupString_FUNC);
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
	OS_NATIVE_EXIT(env, that, _1XLookupString_FUNC);
	return rc;
}
#endif

#ifndef NO__1XLowerWindow
JNIEXPORT jint JNICALL OS_NATIVE(_1XLowerWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XLowerWindow_FUNC);
	rc = (jint)XLowerWindow((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, _1XLowerWindow_FUNC);
	return rc;
}
#endif

#ifndef NO__1XMapWindow
JNIEXPORT void JNICALL OS_NATIVE(_1XMapWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XMapWindow_FUNC);
	XMapWindow((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, _1XMapWindow_FUNC);
}
#endif

#ifndef NO__1XMoveResizeWindow
JNIEXPORT void JNICALL OS_NATIVE(_1XMoveResizeWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, _1XMoveResizeWindow_FUNC);
	XMoveResizeWindow((Display *)arg0, (Window)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, _1XMoveResizeWindow_FUNC);
}
#endif

#ifndef NO__1XOffsetRegion
JNIEXPORT jint JNICALL OS_NATIVE(_1XOffsetRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XOffsetRegion_FUNC);
	rc = (jint)XOffsetRegion((Region)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XOffsetRegion_FUNC);
	return rc;
}
#endif

#ifndef NO__1XOpenDisplay
JNIEXPORT jint JNICALL OS_NATIVE(_1XOpenDisplay)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XOpenDisplay_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)XOpenDisplay((char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1XOpenDisplay_FUNC);
	return rc;
}
#endif

#ifndef NO__1XPointInRegion
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XPointInRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XPointInRegion_FUNC);
	rc = (jboolean)XPointInRegion((Region)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XPointInRegion_FUNC);
	return rc;
}
#endif

#ifndef NO__1XPolygonRegion
JNIEXPORT jint JNICALL OS_NATIVE(_1XPolygonRegion)
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1, jint arg2)
{
	jshort *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XPolygonRegion_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)XPolygonRegion((XPoint *)lparg0, arg1, arg2);
fail:
	if (arg0 && lparg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1XPolygonRegion_FUNC);
	return rc;
}
#endif

#ifndef NO__1XPutImage
JNIEXPORT jint JNICALL OS_NATIVE(_1XPutImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XPutImage_FUNC);
	rc = (jint)XPutImage((Display *)arg0, (Drawable)arg1, (GC)arg2, (XImage *)arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	OS_NATIVE_EXIT(env, that, _1XPutImage_FUNC);
	return rc;
}
#endif

#ifndef NO__1XQueryBestCursor
JNIEXPORT jint JNICALL OS_NATIVE(_1XQueryBestCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XQueryBestCursor_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)XQueryBestCursor((Display *)arg0, arg1, arg2, arg3, (unsigned int *)lparg4, (unsigned int *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1XQueryBestCursor_FUNC);
	return rc;
}
#endif

#ifndef NO__1XQueryColor
JNIEXPORT jint JNICALL OS_NATIVE(_1XQueryColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	XColor _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XQueryColor_FUNC);
	if (arg2) if ((lparg2 = getXColorFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)XQueryColor((Display *)arg0, arg1, lparg2);
fail:
	if (arg2 && lparg2) setXColorFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1XQueryColor_FUNC);
	return rc;
}
#endif

#ifndef NO__1XQueryPointer
JNIEXPORT jint JNICALL OS_NATIVE(_1XQueryPointer)
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
	OS_NATIVE_ENTER(env, that, _1XQueryPointer_FUNC);
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
	OS_NATIVE_EXIT(env, that, _1XQueryPointer_FUNC);
	return rc;
}
#endif

#ifndef NO__1XQueryTree
JNIEXPORT jint JNICALL OS_NATIVE(_1XQueryTree)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XQueryTree_FUNC);
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
	OS_NATIVE_EXIT(env, that, _1XQueryTree_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRaiseWindow
JNIEXPORT jint JNICALL OS_NATIVE(_1XRaiseWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRaiseWindow_FUNC);
	rc = (jint)XRaiseWindow((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, _1XRaiseWindow_FUNC);
	return rc;
}
#endif

#ifndef NO__1XReconfigureWMWindow
JNIEXPORT jint JNICALL OS_NATIVE(_1XReconfigureWMWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	XWindowChanges _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XReconfigureWMWindow_FUNC);
	if (arg4) if ((lparg4 = getXWindowChangesFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)XReconfigureWMWindow((Display *)arg0, (Window)arg1, arg2, arg3, lparg4);
fail:
	if (arg4 && lparg4) setXWindowChangesFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1XReconfigureWMWindow_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRectInRegion
JNIEXPORT jint JNICALL OS_NATIVE(_1XRectInRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRectInRegion_FUNC);
	rc = (jint)XRectInRegion((Region)arg0, arg1, arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, _1XRectInRegion_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRenderComposite
JNIEXPORT void JNICALL OS_NATIVE(_1XRenderComposite)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1, jintLong arg2, jintLong arg3, jintLong arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10, jint arg11, jint arg12)
{
	OS_NATIVE_ENTER(env, that, _1XRenderComposite_FUNC);
/*
	XRenderComposite(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
*/
	{
		LOAD_FUNCTION(fp, XRenderComposite)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jint, jintLong, jintLong, jintLong, jint, jint, jint, jint, jint, jint, jint, jint))fp)(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12);
		}
	}
	OS_NATIVE_EXIT(env, that, _1XRenderComposite_FUNC);
}
#endif

#ifndef NO__1XRenderCreatePicture
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XRenderCreatePicture)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2, jintLong arg3, jobject arg4)
{
	XRenderPictureAttributes _arg4, *lparg4=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRenderCreatePicture_FUNC);
	if (arg4) if ((lparg4 = getXRenderPictureAttributesFields(env, arg4, &_arg4)) == NULL) goto fail;
/*
	rc = (jintLong)XRenderCreatePicture(arg0, arg1, arg2, arg3, lparg4);
*/
	{
		LOAD_FUNCTION(fp, XRenderCreatePicture)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jintLong, jintLong, jintLong, XRenderPictureAttributes *))fp)(arg0, arg1, arg2, arg3, lparg4);
		}
	}
fail:
	if (arg4 && lparg4) setXRenderPictureAttributesFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1XRenderCreatePicture_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRenderFindStandardFormat
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XRenderFindStandardFormat)
	(JNIEnv *env, jclass that, jintLong arg0, jint arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRenderFindStandardFormat_FUNC);
/*
	rc = (jintLong)XRenderFindStandardFormat(arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, XRenderFindStandardFormat)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jint))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1XRenderFindStandardFormat_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRenderFindVisualFormat
JNIEXPORT jintLong JNICALL OS_NATIVE(_1XRenderFindVisualFormat)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRenderFindVisualFormat_FUNC);
/*
	rc = (jintLong)XRenderFindVisualFormat(arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, XRenderFindVisualFormat)
		if (fp) {
			rc = (jintLong)((jintLong (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1XRenderFindVisualFormat_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRenderFreePicture
JNIEXPORT void JNICALL OS_NATIVE(_1XRenderFreePicture)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1)
{
	OS_NATIVE_ENTER(env, that, _1XRenderFreePicture_FUNC);
/*
	XRenderFreePicture(arg0, arg1);
*/
	{
		LOAD_FUNCTION(fp, XRenderFreePicture)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong))fp)(arg0, arg1);
		}
	}
	OS_NATIVE_EXIT(env, that, _1XRenderFreePicture_FUNC);
}
#endif

#ifndef NO__1XRenderQueryExtension
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XRenderQueryExtension)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRenderQueryExtension_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	rc = (jboolean)XRenderQueryExtension(arg0, lparg1, lparg2);
*/
	{
		LOAD_FUNCTION(fp, XRenderQueryExtension)
		if (fp) {
			rc = (jboolean)((jboolean (CALLING_CONVENTION*)(jintLong, jint *, jint *))fp)(arg0, lparg1, lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XRenderQueryExtension_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRenderQueryVersion
JNIEXPORT jint JNICALL OS_NATIVE(_1XRenderQueryVersion)
	(JNIEnv *env, jclass that, jintLong arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRenderQueryVersion_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	rc = (jint)XRenderQueryVersion(arg0, lparg1, lparg2);
*/
	{
		LOAD_FUNCTION(fp, XRenderQueryVersion)
		if (fp) {
			rc = (jint)((jint (CALLING_CONVENTION*)(jintLong, jint *, jint *))fp)(arg0, lparg1, lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XRenderQueryVersion_FUNC);
	return rc;
}
#endif

#ifndef NO__1XRenderSetPictureClipRectangles
JNIEXPORT void JNICALL OS_NATIVE(_1XRenderSetPictureClipRectangles)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jint arg2, jint arg3, jshortArray arg4, jint arg5)
{
	jshort *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, _1XRenderSetPictureClipRectangles_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL)) == NULL) goto fail;
/*
	XRenderSetPictureClipRectangles(arg0, arg1, arg2, arg3, lparg4, arg5);
*/
	{
		LOAD_FUNCTION(fp, XRenderSetPictureClipRectangles)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong, jint, jint, jshort *, jint))fp)(arg0, arg1, arg2, arg3, lparg4, arg5);
		}
	}
fail:
	if (arg4 && lparg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1XRenderSetPictureClipRectangles_FUNC);
}
#endif

#ifndef NO__1XRenderSetPictureClipRegion
JNIEXPORT void JNICALL OS_NATIVE(_1XRenderSetPictureClipRegion)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintLong arg2)
{
	OS_NATIVE_ENTER(env, that, _1XRenderSetPictureClipRegion_FUNC);
/*
	XRenderSetPictureClipRegion(arg0, arg1, arg2);
*/
	{
		LOAD_FUNCTION(fp, XRenderSetPictureClipRegion)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong, jintLong))fp)(arg0, arg1, arg2);
		}
	}
	OS_NATIVE_EXIT(env, that, _1XRenderSetPictureClipRegion_FUNC);
}
#endif

#ifndef NO__1XRenderSetPictureTransform
JNIEXPORT void JNICALL OS_NATIVE(_1XRenderSetPictureTransform)
	(JNIEnv *env, jclass that, jintLong arg0, jintLong arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1XRenderSetPictureTransform_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
/*
	XRenderSetPictureTransform(arg0, arg1, lparg2);
*/
	{
		LOAD_FUNCTION(fp, XRenderSetPictureTransform)
		if (fp) {
			((void (CALLING_CONVENTION*)(jintLong, jintLong, jint *))fp)(arg0, arg1, lparg2);
		}
	}
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XRenderSetPictureTransform_FUNC);
}
#endif

#ifndef NO__1XReparentWindow
JNIEXPORT jint JNICALL OS_NATIVE(_1XReparentWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XReparentWindow_FUNC);
	rc = (jint)XReparentWindow((Display *)arg0, (Window)arg1, (Window)arg2, arg3, arg4);
	OS_NATIVE_EXIT(env, that, _1XReparentWindow_FUNC);
	return rc;
}
#endif

#ifndef NO__1XResizeWindow
JNIEXPORT void JNICALL OS_NATIVE(_1XResizeWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1XResizeWindow_FUNC);
	XResizeWindow((Display *)arg0, (Window)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1XResizeWindow_FUNC);
}
#endif

#ifndef NO__1XRootWindowOfScreen
JNIEXPORT jint JNICALL OS_NATIVE(_1XRootWindowOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XRootWindowOfScreen_FUNC);
	rc = (jint)XRootWindowOfScreen((Screen *)arg0);
	OS_NATIVE_EXIT(env, that, _1XRootWindowOfScreen_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSelectInput
JNIEXPORT void JNICALL OS_NATIVE(_1XSelectInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSelectInput_FUNC);
	XSelectInput((Display *)arg0, (Window)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XSelectInput_FUNC);
}
#endif

#ifndef NO__1XSendEvent
JNIEXPORT jint JNICALL OS_NATIVE(_1XSendEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSendEvent_FUNC);
	rc = (jint)XSendEvent((Display *)arg0, (Window)arg1, (Bool)arg2, (long)arg3, (XEvent *)arg4);
	OS_NATIVE_EXIT(env, that, _1XSendEvent_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetBackground
JNIEXPORT void JNICALL OS_NATIVE(_1XSetBackground)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSetBackground_FUNC);
	XSetBackground((Display *)arg0, (GC)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XSetBackground_FUNC);
}
#endif

#ifndef NO__1XSetClipMask
JNIEXPORT void JNICALL OS_NATIVE(_1XSetClipMask)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSetClipMask_FUNC);
	XSetClipMask((Display *)arg0, (GC)arg1, (Pixmap)arg2);
	OS_NATIVE_EXIT(env, that, _1XSetClipMask_FUNC);
}
#endif

#ifndef NO__1XSetClipRectangles
JNIEXPORT void JNICALL OS_NATIVE(_1XSetClipRectangles)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jint arg6)
{
	XRectangle _arg4, *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, _1XSetClipRectangles_FUNC);
	if (arg4) if ((lparg4 = getXRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	XSetClipRectangles((Display *)arg0, (GC)arg1, arg2, arg3, (XRectangle *)lparg4, arg5, arg6);
fail:
	if (arg4 && lparg4) setXRectangleFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1XSetClipRectangles_FUNC);
}
#endif

#ifndef NO__1XSetDashes
JNIEXPORT jint JNICALL OS_NATIVE(_1XSetDashes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSetDashes_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XSetDashes((Display *)arg0, (GC)arg1, arg2, (char *)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1XSetDashes_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetErrorHandler
JNIEXPORT jint JNICALL OS_NATIVE(_1XSetErrorHandler)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSetErrorHandler_FUNC);
	rc = (jint)XSetErrorHandler((XErrorHandler)arg0);
	OS_NATIVE_EXIT(env, that, _1XSetErrorHandler_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetFillRule
JNIEXPORT void JNICALL OS_NATIVE(_1XSetFillRule)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSetFillRule_FUNC);
	XSetFillRule((Display *)arg0, (GC)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XSetFillRule_FUNC);
}
#endif

#ifndef NO__1XSetFillStyle
JNIEXPORT void JNICALL OS_NATIVE(_1XSetFillStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSetFillStyle_FUNC);
	XSetFillStyle((Display *)arg0, (GC)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XSetFillStyle_FUNC);
}
#endif

#ifndef NO__1XSetFontPath
JNIEXPORT jint JNICALL OS_NATIVE(_1XSetFontPath)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSetFontPath_FUNC);
	rc = (jint)XSetFontPath((Display *)arg0, (char **)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XSetFontPath_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetForeground
JNIEXPORT void JNICALL OS_NATIVE(_1XSetForeground)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSetForeground_FUNC);
	XSetForeground((Display *)arg0, (GC)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XSetForeground_FUNC);
}
#endif

#ifndef NO__1XSetFunction
JNIEXPORT void JNICALL OS_NATIVE(_1XSetFunction)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSetFunction_FUNC);
	XSetFunction((Display *)arg0, (GC)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XSetFunction_FUNC);
}
#endif

#ifndef NO__1XSetGraphicsExposures
JNIEXPORT void JNICALL OS_NATIVE(_1XSetGraphicsExposures)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSetGraphicsExposures_FUNC);
	XSetGraphicsExposures((Display *)arg0, (GC)arg1, (Bool)arg2);
	OS_NATIVE_EXIT(env, that, _1XSetGraphicsExposures_FUNC);
}
#endif

#ifndef NO__1XSetIOErrorHandler
JNIEXPORT jint JNICALL OS_NATIVE(_1XSetIOErrorHandler)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSetIOErrorHandler_FUNC);
	rc = (jint)XSetIOErrorHandler((XIOErrorHandler)arg0);
	OS_NATIVE_EXIT(env, that, _1XSetIOErrorHandler_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetInputFocus
JNIEXPORT jint JNICALL OS_NATIVE(_1XSetInputFocus)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSetInputFocus_FUNC);
	rc = (jint)XSetInputFocus((Display *)arg0, (Window)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1XSetInputFocus_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetLineAttributes
JNIEXPORT jint JNICALL OS_NATIVE(_1XSetLineAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSetLineAttributes_FUNC);
	rc = (jint)XSetLineAttributes((Display *)arg0, (GC)arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, _1XSetLineAttributes_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetRegion
JNIEXPORT void JNICALL OS_NATIVE(_1XSetRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSetRegion_FUNC);
	XSetRegion((Display *)arg0, (GC)arg1, (Region)arg2);
	OS_NATIVE_EXIT(env, that, _1XSetRegion_FUNC);
}
#endif

#ifndef NO__1XSetStipple
JNIEXPORT void JNICALL OS_NATIVE(_1XSetStipple)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSetStipple_FUNC);
	XSetStipple((Display *)arg0, (GC)arg1, (Pixmap)arg2);
	OS_NATIVE_EXIT(env, that, _1XSetStipple_FUNC);
}
#endif

#ifndef NO__1XSetSubwindowMode
JNIEXPORT void JNICALL OS_NATIVE(_1XSetSubwindowMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSetSubwindowMode_FUNC);
	XSetSubwindowMode((Display *)arg0, (GC)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XSetSubwindowMode_FUNC);
}
#endif

#ifndef NO__1XSetTSOrigin
JNIEXPORT void JNICALL OS_NATIVE(_1XSetTSOrigin)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1XSetTSOrigin_FUNC);
	XSetTSOrigin((Display *)arg0, (GC)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1XSetTSOrigin_FUNC);
}
#endif

#ifndef NO__1XSetTile
JNIEXPORT void JNICALL OS_NATIVE(_1XSetTile)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSetTile_FUNC);
	XSetTile((Display *)arg0, (GC)arg1, (Pixmap)arg2);
	OS_NATIVE_EXIT(env, that, _1XSetTile_FUNC);
}
#endif

#ifndef NO__1XSetTransientForHint
JNIEXPORT jint JNICALL OS_NATIVE(_1XSetTransientForHint)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSetTransientForHint_FUNC);
	rc = (jint)XSetTransientForHint((Display *)arg0, (Window)arg1, (Window)arg2);
	OS_NATIVE_EXIT(env, that, _1XSetTransientForHint_FUNC);
	return rc;
}
#endif

#ifndef NO__1XSetWMNormalHints
JNIEXPORT void JNICALL OS_NATIVE(_1XSetWMNormalHints)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	XSizeHints _arg2, *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1XSetWMNormalHints_FUNC);
	if (arg2) if ((lparg2 = getXSizeHintsFields(env, arg2, &_arg2)) == NULL) goto fail;
	XSetWMNormalHints((Display *)arg0, (Window)arg1, lparg2);
fail:
	if (arg2 && lparg2) setXSizeHintsFields(env, arg2, lparg2);
	OS_NATIVE_EXIT(env, that, _1XSetWMNormalHints_FUNC);
}
#endif

#ifndef NO__1XSetWindowBackgroundPixmap
JNIEXPORT void JNICALL OS_NATIVE(_1XSetWindowBackgroundPixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSetWindowBackgroundPixmap_FUNC);
	XSetWindowBackgroundPixmap((Display *)arg0, (Window)arg1, (Pixmap)arg2);
	OS_NATIVE_EXIT(env, that, _1XSetWindowBackgroundPixmap_FUNC);
}
#endif

#ifndef NO__1XShapeCombineMask
JNIEXPORT void JNICALL OS_NATIVE(_1XShapeCombineMask)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	OS_NATIVE_ENTER(env, that, _1XShapeCombineMask_FUNC);
	XShapeCombineMask((Display *)arg0, (Window)arg1, arg2, arg3, arg4, (Pixmap)arg5, arg6);
	OS_NATIVE_EXIT(env, that, _1XShapeCombineMask_FUNC);
}
#endif

#ifndef NO__1XShapeCombineRegion
JNIEXPORT void JNICALL OS_NATIVE(_1XShapeCombineRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	OS_NATIVE_ENTER(env, that, _1XShapeCombineRegion_FUNC);
	XShapeCombineRegion((Display *)arg0, (Window)arg1, arg2, arg3, arg4, (Region)arg5, arg6);
	OS_NATIVE_EXIT(env, that, _1XShapeCombineRegion_FUNC);
}
#endif

#ifndef NO__1XSubtractRegion
JNIEXPORT void JNICALL OS_NATIVE(_1XSubtractRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XSubtractRegion_FUNC);
	XSubtractRegion((Region)arg0, (Region)arg1, (Region)arg2);
	OS_NATIVE_EXIT(env, that, _1XSubtractRegion_FUNC);
}
#endif

#ifndef NO__1XSync
JNIEXPORT void JNICALL OS_NATIVE(_1XSync)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1XSync_FUNC);
	XSync((Display *)arg0, (Bool)arg1);
	OS_NATIVE_EXIT(env, that, _1XSync_FUNC);
}
#endif

#ifndef NO__1XSynchronize
JNIEXPORT jint JNICALL OS_NATIVE(_1XSynchronize)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XSynchronize_FUNC);
	rc = (jint)XSynchronize((Display *)arg0, (Bool)arg1);
	OS_NATIVE_EXIT(env, that, _1XSynchronize_FUNC);
	return rc;
}
#endif

#ifndef NO__1XTestFakeButtonEvent
JNIEXPORT void JNICALL OS_NATIVE(_1XTestFakeButtonEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1XTestFakeButtonEvent_FUNC);
	XTestFakeButtonEvent((Display *)arg0, arg1, (Bool)arg2, (unsigned long)arg3);
	OS_NATIVE_EXIT(env, that, _1XTestFakeButtonEvent_FUNC);
}
#endif

#ifndef NO__1XTestFakeKeyEvent
JNIEXPORT void JNICALL OS_NATIVE(_1XTestFakeKeyEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1XTestFakeKeyEvent_FUNC);
	XTestFakeKeyEvent((Display *)arg0, arg1, (Bool)arg2, (unsigned long)arg3);
	OS_NATIVE_EXIT(env, that, _1XTestFakeKeyEvent_FUNC);
}
#endif

#ifndef NO__1XTestFakeMotionEvent
JNIEXPORT void JNICALL OS_NATIVE(_1XTestFakeMotionEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, _1XTestFakeMotionEvent_FUNC);
	XTestFakeMotionEvent((Display *)arg0, arg1, arg2, arg3, (unsigned long)arg4);
	OS_NATIVE_EXIT(env, that, _1XTestFakeMotionEvent_FUNC);
}
#endif

#ifndef NO__1XTranslateCoordinates
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XTranslateCoordinates)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jintArray arg5, jintArray arg6, jintArray arg7)
{
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint *lparg7=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XTranslateCoordinates_FUNC);
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jboolean)XTranslateCoordinates((Display *)arg0, (Window)arg1, (Window)arg2, arg3, arg4, lparg5, lparg6, (Window *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	OS_NATIVE_EXIT(env, that, _1XTranslateCoordinates_FUNC);
	return rc;
}
#endif

#ifndef NO__1XUndefineCursor
JNIEXPORT void JNICALL OS_NATIVE(_1XUndefineCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XUndefineCursor_FUNC);
	XUndefineCursor((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, _1XUndefineCursor_FUNC);
}
#endif

#ifndef NO__1XUngrabKeyboard
JNIEXPORT jint JNICALL OS_NATIVE(_1XUngrabKeyboard)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XUngrabKeyboard_FUNC);
	rc = (jint)XUngrabKeyboard((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XUngrabKeyboard_FUNC);
	return rc;
}
#endif

#ifndef NO__1XUngrabPointer
JNIEXPORT jint JNICALL OS_NATIVE(_1XUngrabPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XUngrabPointer_FUNC);
	rc = (jint)XUngrabPointer((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XUngrabPointer_FUNC);
	return rc;
}
#endif

#ifndef NO__1XUnionRectWithRegion
JNIEXPORT void JNICALL OS_NATIVE(_1XUnionRectWithRegion)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XRectangle _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, _1XUnionRectWithRegion_FUNC);
	if (arg0) if ((lparg0 = getXRectangleFields(env, arg0, &_arg0)) == NULL) goto fail;
	XUnionRectWithRegion((XRectangle *)lparg0, (Region)arg1, (Region)arg2);
fail:
	if (arg0 && lparg0) setXRectangleFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, _1XUnionRectWithRegion_FUNC);
}
#endif

#ifndef NO__1XUnionRegion
JNIEXPORT void JNICALL OS_NATIVE(_1XUnionRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XUnionRegion_FUNC);
	XUnionRegion((Region)arg0, (Region)arg1, (Region)arg2);
	OS_NATIVE_EXIT(env, that, _1XUnionRegion_FUNC);
}
#endif

#ifndef NO__1XUnmapWindow
JNIEXPORT void JNICALL OS_NATIVE(_1XUnmapWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XUnmapWindow_FUNC);
	XUnmapWindow((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, _1XUnmapWindow_FUNC);
}
#endif

#ifndef NO__1XWarpPointer
JNIEXPORT jint JNICALL OS_NATIVE(_1XWarpPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XWarpPointer_FUNC);
	rc = (jint)XWarpPointer((Display *)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	OS_NATIVE_EXIT(env, that, _1XWarpPointer_FUNC);
	return rc;
}
#endif

#ifndef NO__1XWhitePixel
JNIEXPORT jint JNICALL OS_NATIVE(_1XWhitePixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XWhitePixel_FUNC);
	rc = (jint)XWhitePixel((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XWhitePixel_FUNC);
	return rc;
}
#endif

#ifndef NO__1XWithdrawWindow
JNIEXPORT void JNICALL OS_NATIVE(_1XWithdrawWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XWithdrawWindow_FUNC);
	XWithdrawWindow((Display *)arg0, (Window)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XWithdrawWindow_FUNC);
}
#endif

#ifndef NO__1XineramaIsActive
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XineramaIsActive)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XineramaIsActive_FUNC);
	rc = (jboolean)XineramaIsActive((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XineramaIsActive_FUNC);
	return rc;
}
#endif

#ifndef NO__1XineramaQueryScreens
JNIEXPORT jint JNICALL OS_NATIVE(_1XineramaQueryScreens)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XineramaQueryScreens_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XineramaQueryScreens((Display *)arg0, lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XineramaQueryScreens_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmAddWMProtocolCallback
JNIEXPORT void JNICALL OS_NATIVE(_1XmAddWMProtocolCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1XmAddWMProtocolCallback_FUNC);
	XmAddWMProtocolCallback((Widget)arg0, (Atom)arg1, (XtCallbackProc)arg2, (XtPointer)arg3);
	OS_NATIVE_EXIT(env, that, _1XmAddWMProtocolCallback_FUNC);
}
#endif

#ifndef NO__1XmChangeColor
JNIEXPORT void JNICALL OS_NATIVE(_1XmChangeColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmChangeColor_FUNC);
	XmChangeColor((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmChangeColor_FUNC);
}
#endif

#ifndef NO__1XmClipboardCopy
JNIEXPORT jint JNICALL OS_NATIVE(_1XmClipboardCopy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jintArray arg7)
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint *lparg7=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmClipboardCopy_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	if (arg7) if ((lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL)) == NULL) goto fail;
	rc = (jint)XmClipboardCopy((Display *)arg0, (Window)arg1, arg2, (char *)lparg3, (char *)lparg4, arg5, arg6, (void *)lparg7);
fail:
	if (arg7 && lparg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1XmClipboardCopy_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmClipboardEndCopy
JNIEXPORT jint JNICALL OS_NATIVE(_1XmClipboardEndCopy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmClipboardEndCopy_FUNC);
	rc = (jint)XmClipboardEndCopy((Display *)arg0, (Window)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XmClipboardEndCopy_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmClipboardEndRetrieve
JNIEXPORT jint JNICALL OS_NATIVE(_1XmClipboardEndRetrieve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmClipboardEndRetrieve_FUNC);
	rc = (jint)XmClipboardEndRetrieve((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, _1XmClipboardEndRetrieve_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmClipboardInquireCount
JNIEXPORT jint JNICALL OS_NATIVE(_1XmClipboardInquireCount)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmClipboardInquireCount_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XmClipboardInquireCount((Display *)arg0, (Window)arg1, (int *)lparg2, (unsigned long *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XmClipboardInquireCount_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmClipboardInquireFormat
JNIEXPORT jint JNICALL OS_NATIVE(_1XmClipboardInquireFormat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jintArray arg5)
{
	jbyte *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmClipboardInquireFormat_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg5) if ((lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL)) == NULL) goto fail;
	rc = (jint)XmClipboardInquireFormat((Display *)arg0, (Window)arg1, arg2, (char *)lparg3, arg4, (unsigned long *)lparg5);
fail:
	if (arg5 && lparg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1XmClipboardInquireFormat_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmClipboardInquireLength
JNIEXPORT jint JNICALL OS_NATIVE(_1XmClipboardInquireLength)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3)
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmClipboardInquireLength_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XmClipboardInquireLength((Display *)arg0, (Window)arg1, (char *)lparg2, (unsigned long *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XmClipboardInquireLength_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmClipboardRetrieve
JNIEXPORT jint JNICALL OS_NATIVE(_1XmClipboardRetrieve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jbyteArray arg3, jint arg4, jintArray arg5, jintArray arg6)
{
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint *lparg5=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmClipboardRetrieve_FUNC);
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
	OS_NATIVE_EXIT(env, that, _1XmClipboardRetrieve_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmClipboardStartCopy
JNIEXPORT jint JNICALL OS_NATIVE(_1XmClipboardStartCopy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmClipboardStartCopy_FUNC);
	if (arg6) if ((lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL)) == NULL) goto fail;
	rc = (jint)XmClipboardStartCopy((Display *)arg0, (Window)arg1, (XmString)arg2, arg3, (Widget)arg4, (XmCutPasteProc)arg5, (long *)lparg6);
fail:
	if (arg6 && lparg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	OS_NATIVE_EXIT(env, that, _1XmClipboardStartCopy_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmClipboardStartRetrieve
JNIEXPORT jint JNICALL OS_NATIVE(_1XmClipboardStartRetrieve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmClipboardStartRetrieve_FUNC);
	rc = (jint)XmClipboardStartRetrieve((Display *)arg0, (Window)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XmClipboardStartRetrieve_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmComboBoxAddItem
JNIEXPORT void JNICALL OS_NATIVE(_1XmComboBoxAddItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	OS_NATIVE_ENTER(env, that, _1XmComboBoxAddItem_FUNC);
	XmComboBoxAddItem((Widget)arg0, (XmString)arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1XmComboBoxAddItem_FUNC);
}
#endif

#ifndef NO__1XmComboBoxDeletePos
JNIEXPORT void JNICALL OS_NATIVE(_1XmComboBoxDeletePos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmComboBoxDeletePos_FUNC);
	XmComboBoxDeletePos((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmComboBoxDeletePos_FUNC);
}
#endif

#ifndef NO__1XmComboBoxSelectItem
JNIEXPORT void JNICALL OS_NATIVE(_1XmComboBoxSelectItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmComboBoxSelectItem_FUNC);
	XmComboBoxSelectItem((Widget)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, _1XmComboBoxSelectItem_FUNC);
}
#endif

#ifndef NO__1XmCreateArrowButton
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateArrowButton)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateArrowButton_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateArrowButton((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateArrowButton_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateCascadeButtonGadget
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateCascadeButtonGadget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateCascadeButtonGadget_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateCascadeButtonGadget((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateCascadeButtonGadget_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateComboBox
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateComboBox)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateComboBox_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateComboBox((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateComboBox_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateDialogShell
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateDialogShell)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateDialogShell_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateDialogShell((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateDialogShell_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateDrawingArea
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateDrawingArea)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateDrawingArea_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateDrawingArea((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateDrawingArea_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateDrawnButton
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateDrawnButton)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateDrawnButton_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateDrawnButton((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateDrawnButton_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateErrorDialog
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateErrorDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateErrorDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateErrorDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateErrorDialog_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateFileSelectionDialog
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateFileSelectionDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateFileSelectionDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateFileSelectionDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateFileSelectionDialog_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateForm
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateForm)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateForm_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateForm((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateForm_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateFrame
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateFrame)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateFrame_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateFrame((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateFrame_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateInformationDialog
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateInformationDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateInformationDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateInformationDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateInformationDialog_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateLabel
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateLabel)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateLabel_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateLabel((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateLabel_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateList
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateList)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateList_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateList((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateList_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateMainWindow
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateMainWindow)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateMainWindow_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateMainWindow((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateMainWindow_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateMenuBar
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateMenuBar)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateMenuBar_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateMenuBar((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateMenuBar_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateMessageDialog
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateMessageDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateMessageDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateMessageDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateMessageDialog_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreatePopupMenu
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreatePopupMenu)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreatePopupMenu_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreatePopupMenu((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreatePopupMenu_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreatePulldownMenu
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreatePulldownMenu)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreatePulldownMenu_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreatePulldownMenu((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreatePulldownMenu_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreatePushButton
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreatePushButton)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreatePushButton_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreatePushButton((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreatePushButton_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreatePushButtonGadget
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreatePushButtonGadget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreatePushButtonGadget_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreatePushButtonGadget((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreatePushButtonGadget_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateQuestionDialog
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateQuestionDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateQuestionDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateQuestionDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateQuestionDialog_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateScale
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateScale)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateScale_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateScale((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateScale_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateScrollBar
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateScrollBar)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateScrollBar_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateScrollBar((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateScrollBar_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateScrolledList
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateScrolledList)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateScrolledList_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateScrolledList((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateScrolledList_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateScrolledText
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateScrolledText)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateScrolledText_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateScrolledText((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateScrolledText_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateSeparator
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateSeparator)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateSeparator_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateSeparator((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateSeparator_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateSeparatorGadget
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateSeparatorGadget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateSeparatorGadget_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateSeparatorGadget((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateSeparatorGadget_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateSimpleSpinBox
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateSimpleSpinBox)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateSimpleSpinBox_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateSimpleSpinBox((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateSimpleSpinBox_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateTextField
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateTextField)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateTextField_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateTextField((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateTextField_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateToggleButton
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateToggleButton)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateToggleButton_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateToggleButton((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateToggleButton_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateToggleButtonGadget
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateToggleButtonGadget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateToggleButtonGadget_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateToggleButtonGadget((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateToggleButtonGadget_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateWarningDialog
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateWarningDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateWarningDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateWarningDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateWarningDialog_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmCreateWorkingDialog
JNIEXPORT jint JNICALL OS_NATIVE(_1XmCreateWorkingDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmCreateWorkingDialog_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmCreateWorkingDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmCreateWorkingDialog_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmDestroyPixmap
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmDestroyPixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmDestroyPixmap_FUNC);
	rc = (jboolean)XmDestroyPixmap((Screen *)arg0, (Pixmap)arg1);
	OS_NATIVE_EXIT(env, that, _1XmDestroyPixmap_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmDragCancel
JNIEXPORT void JNICALL OS_NATIVE(_1XmDragCancel)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmDragCancel_FUNC);
	XmDragCancel((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmDragCancel_FUNC);
}
#endif

#ifndef NO__1XmDragStart
JNIEXPORT jint JNICALL OS_NATIVE(_1XmDragStart)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmDragStart_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmDragStart((Widget)arg0, (XEvent *)arg1, (ArgList)lparg2, (Cardinal)arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XmDragStart_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmDropSiteRegister
JNIEXPORT void JNICALL OS_NATIVE(_1XmDropSiteRegister)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1XmDropSiteRegister_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmDropSiteRegister((Widget)arg0, (ArgList)lparg1, (Cardinal)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmDropSiteRegister_FUNC);
}
#endif

#ifndef NO__1XmDropSiteUnregister
JNIEXPORT void JNICALL OS_NATIVE(_1XmDropSiteUnregister)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmDropSiteUnregister_FUNC);
	XmDropSiteUnregister((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmDropSiteUnregister_FUNC);
}
#endif

#ifndef NO__1XmDropSiteUpdate
JNIEXPORT void JNICALL OS_NATIVE(_1XmDropSiteUpdate)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1XmDropSiteUpdate_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmDropSiteUpdate((Widget)arg0, (ArgList)lparg1, (Cardinal)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmDropSiteUpdate_FUNC);
}
#endif

#ifndef NO__1XmDropTransferAdd
JNIEXPORT void JNICALL OS_NATIVE(_1XmDropTransferAdd)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1XmDropTransferAdd_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmDropTransferAdd((Widget)arg0, (XmDropTransferEntryRec *)lparg1, (Cardinal)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmDropTransferAdd_FUNC);
}
#endif

#ifndef NO__1XmDropTransferStart
JNIEXPORT jint JNICALL OS_NATIVE(_1XmDropTransferStart)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmDropTransferStart_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmDropTransferStart((Widget)arg0, (ArgList)lparg1, (Cardinal)arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmDropTransferStart_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmFileSelectionBoxGetChild
JNIEXPORT jint JNICALL OS_NATIVE(_1XmFileSelectionBoxGetChild)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmFileSelectionBoxGetChild_FUNC);
	rc = (jint)XmFileSelectionBoxGetChild((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmFileSelectionBoxGetChild_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmFontListAppendEntry
JNIEXPORT jint JNICALL OS_NATIVE(_1XmFontListAppendEntry)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmFontListAppendEntry_FUNC);
	rc = (jint)XmFontListAppendEntry((XmFontList)arg0, (XmFontListEntry)arg1);
	OS_NATIVE_EXIT(env, that, _1XmFontListAppendEntry_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmFontListCopy
JNIEXPORT jint JNICALL OS_NATIVE(_1XmFontListCopy)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmFontListCopy_FUNC);
	rc = (jint)XmFontListCopy((XmFontList)arg0);
	OS_NATIVE_EXIT(env, that, _1XmFontListCopy_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmFontListEntryFree
JNIEXPORT void JNICALL OS_NATIVE(_1XmFontListEntryFree)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, _1XmFontListEntryFree_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	XmFontListEntryFree((XmFontListEntry *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1XmFontListEntryFree_FUNC);
}
#endif

#ifndef NO__1XmFontListEntryGetFont
JNIEXPORT jint JNICALL OS_NATIVE(_1XmFontListEntryGetFont)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmFontListEntryGetFont_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmFontListEntryGetFont((XmFontListEntry)arg0, (XmFontType *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmFontListEntryGetFont_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmFontListEntryLoad
JNIEXPORT jint JNICALL OS_NATIVE(_1XmFontListEntryLoad)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg1=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmFontListEntryLoad_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XmFontListEntryLoad((Display *)arg0, (char *)lparg1, arg2, (char *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmFontListEntryLoad_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmFontListFree
JNIEXPORT void JNICALL OS_NATIVE(_1XmFontListFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmFontListFree_FUNC);
	XmFontListFree((XmFontList)arg0);
	OS_NATIVE_EXIT(env, that, _1XmFontListFree_FUNC);
}
#endif

#ifndef NO__1XmFontListFreeFontContext
JNIEXPORT void JNICALL OS_NATIVE(_1XmFontListFreeFontContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmFontListFreeFontContext_FUNC);
	XmFontListFreeFontContext((XmFontContext)arg0);
	OS_NATIVE_EXIT(env, that, _1XmFontListFreeFontContext_FUNC);
}
#endif

#ifndef NO__1XmFontListInitFontContext
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmFontListInitFontContext)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1)
{
	jint *lparg0=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmFontListInitFontContext_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jboolean)XmFontListInitFontContext((XmFontContext *)lparg0, (XmFontList)arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1XmFontListInitFontContext_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmFontListNextEntry
JNIEXPORT jint JNICALL OS_NATIVE(_1XmFontListNextEntry)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmFontListNextEntry_FUNC);
	rc = (jint)XmFontListNextEntry((XmFontContext)arg0);
	OS_NATIVE_EXIT(env, that, _1XmFontListNextEntry_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmGetAtomName
JNIEXPORT jint JNICALL OS_NATIVE(_1XmGetAtomName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmGetAtomName_FUNC);
	rc = (jint)XmGetAtomName((Display *)arg0, (Atom)arg1);
	OS_NATIVE_EXIT(env, that, _1XmGetAtomName_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmGetDragContext
JNIEXPORT jint JNICALL OS_NATIVE(_1XmGetDragContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmGetDragContext_FUNC);
	rc = (jint)XmGetDragContext((Widget)arg0, (Time)arg1);
	OS_NATIVE_EXIT(env, that, _1XmGetDragContext_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmGetFocusWidget
JNIEXPORT jint JNICALL OS_NATIVE(_1XmGetFocusWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmGetFocusWidget_FUNC);
	rc = (jint)XmGetFocusWidget((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmGetFocusWidget_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmGetPixmap
JNIEXPORT jint JNICALL OS_NATIVE(_1XmGetPixmap)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmGetPixmap_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmGetPixmap((Screen *)arg0, (char *)lparg1, (Pixel)arg2, (Pixel)arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmGetPixmap_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmGetPixmapByDepth
JNIEXPORT jint JNICALL OS_NATIVE(_1XmGetPixmapByDepth)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jint arg4)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmGetPixmapByDepth_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmGetPixmapByDepth((Screen *)arg0, (char *)lparg1, arg2, arg3, arg4);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmGetPixmapByDepth_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmGetXmDisplay
JNIEXPORT jint JNICALL OS_NATIVE(_1XmGetXmDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmGetXmDisplay_FUNC);
	rc = (jint)XmGetXmDisplay((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XmGetXmDisplay_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmImMbLookupString
JNIEXPORT jint JNICALL OS_NATIVE(_1XmImMbLookupString)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jbyteArray arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	XKeyEvent _arg1, *lparg1=NULL;
	jbyte *lparg2=NULL;
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmImMbLookupString_FUNC);
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
	OS_NATIVE_EXIT(env, that, _1XmImMbLookupString_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmImRegister
JNIEXPORT void JNICALL OS_NATIVE(_1XmImRegister)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmImRegister_FUNC);
	XmImRegister((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmImRegister_FUNC);
}
#endif

#ifndef NO__1XmImSetFocusValues
JNIEXPORT void JNICALL OS_NATIVE(_1XmImSetFocusValues)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1XmImSetFocusValues_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmImSetFocusValues((Widget)arg0, (ArgList)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmImSetFocusValues_FUNC);
}
#endif

#ifndef NO__1XmImSetValues
JNIEXPORT void JNICALL OS_NATIVE(_1XmImSetValues)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1XmImSetValues_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmImSetValues((Widget)arg0, (ArgList)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmImSetValues_FUNC);
}
#endif

#ifndef NO__1XmImUnregister
JNIEXPORT void JNICALL OS_NATIVE(_1XmImUnregister)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmImUnregister_FUNC);
	XmImUnregister((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmImUnregister_FUNC);
}
#endif

#ifndef NO__1XmImUnsetFocus
JNIEXPORT void JNICALL OS_NATIVE(_1XmImUnsetFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmImUnsetFocus_FUNC);
	XmImUnsetFocus((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmImUnsetFocus_FUNC);
}
#endif

#ifndef NO__1XmInternAtom
JNIEXPORT jint JNICALL OS_NATIVE(_1XmInternAtom)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jboolean arg2)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmInternAtom_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmInternAtom((Display *)arg0, (String)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmInternAtom_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmListAddItemUnselected
JNIEXPORT void JNICALL OS_NATIVE(_1XmListAddItemUnselected)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XmListAddItemUnselected_FUNC);
	XmListAddItemUnselected((Widget)arg0, (XmString)arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XmListAddItemUnselected_FUNC);
}
#endif

#ifndef NO__1XmListDeleteAllItems
JNIEXPORT void JNICALL OS_NATIVE(_1XmListDeleteAllItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmListDeleteAllItems_FUNC);
	XmListDeleteAllItems((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmListDeleteAllItems_FUNC);
}
#endif

#ifndef NO__1XmListDeleteItemsPos
JNIEXPORT void JNICALL OS_NATIVE(_1XmListDeleteItemsPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XmListDeleteItemsPos_FUNC);
	XmListDeleteItemsPos((Widget)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XmListDeleteItemsPos_FUNC);
}
#endif

#ifndef NO__1XmListDeletePos
JNIEXPORT void JNICALL OS_NATIVE(_1XmListDeletePos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmListDeletePos_FUNC);
	XmListDeletePos((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmListDeletePos_FUNC);
}
#endif

#ifndef NO__1XmListDeletePositions
JNIEXPORT void JNICALL OS_NATIVE(_1XmListDeletePositions)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1XmListDeletePositions_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmListDeletePositions((Widget)arg0, (int *)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmListDeletePositions_FUNC);
}
#endif

#ifndef NO__1XmListDeselectAllItems
JNIEXPORT void JNICALL OS_NATIVE(_1XmListDeselectAllItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmListDeselectAllItems_FUNC);
	XmListDeselectAllItems((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmListDeselectAllItems_FUNC);
}
#endif

#ifndef NO__1XmListDeselectPos
JNIEXPORT void JNICALL OS_NATIVE(_1XmListDeselectPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmListDeselectPos_FUNC);
	XmListDeselectPos((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmListDeselectPos_FUNC);
}
#endif

#ifndef NO__1XmListGetKbdItemPos
JNIEXPORT jint JNICALL OS_NATIVE(_1XmListGetKbdItemPos)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmListGetKbdItemPos_FUNC);
	rc = (jint)XmListGetKbdItemPos((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmListGetKbdItemPos_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmListGetSelectedPos
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmListGetSelectedPos)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmListGetSelectedPos_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)XmListGetSelectedPos((Widget)arg0, (int **)lparg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmListGetSelectedPos_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmListItemPos
JNIEXPORT jint JNICALL OS_NATIVE(_1XmListItemPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmListItemPos_FUNC);
	rc = (jint)XmListItemPos((Widget)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, _1XmListItemPos_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmListPosSelected
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmListPosSelected)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmListPosSelected_FUNC);
	rc = (jboolean)XmListPosSelected((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmListPosSelected_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmListReplaceItemsPosUnselected
JNIEXPORT void JNICALL OS_NATIVE(_1XmListReplaceItemsPosUnselected)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1XmListReplaceItemsPosUnselected_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmListReplaceItemsPosUnselected((Widget)arg0, (XmString *)lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmListReplaceItemsPosUnselected_FUNC);
}
#endif

#ifndef NO__1XmListSelectPos
JNIEXPORT void JNICALL OS_NATIVE(_1XmListSelectPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	OS_NATIVE_ENTER(env, that, _1XmListSelectPos_FUNC);
	XmListSelectPos((Widget)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XmListSelectPos_FUNC);
}
#endif

#ifndef NO__1XmListSetKbdItemPos
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmListSetKbdItemPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmListSetKbdItemPos_FUNC);
	rc = (jboolean)XmListSetKbdItemPos((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmListSetKbdItemPos_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmListSetPos
JNIEXPORT void JNICALL OS_NATIVE(_1XmListSetPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmListSetPos_FUNC);
	XmListSetPos((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmListSetPos_FUNC);
}
#endif

#ifndef NO__1XmListUpdateSelectedList
JNIEXPORT void JNICALL OS_NATIVE(_1XmListUpdateSelectedList)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmListUpdateSelectedList_FUNC);
	XmListUpdateSelectedList((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmListUpdateSelectedList_FUNC);
}
#endif

#ifndef NO__1XmMainWindowSetAreas
JNIEXPORT void JNICALL OS_NATIVE(_1XmMainWindowSetAreas)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, _1XmMainWindowSetAreas_FUNC);
	XmMainWindowSetAreas((Widget)arg0, (Widget)arg1, (Widget)arg2, (Widget)arg3, (Widget)arg4, (Widget)arg5);
	OS_NATIVE_EXIT(env, that, _1XmMainWindowSetAreas_FUNC);
}
#endif

#ifndef NO__1XmMessageBoxGetChild
JNIEXPORT jint JNICALL OS_NATIVE(_1XmMessageBoxGetChild)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmMessageBoxGetChild_FUNC);
	rc = (jint)XmMessageBoxGetChild((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmMessageBoxGetChild_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmParseMappingCreate
JNIEXPORT jint JNICALL OS_NATIVE(_1XmParseMappingCreate)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1)
{
	jint *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmParseMappingCreate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)XmParseMappingCreate((ArgList)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1XmParseMappingCreate_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmParseMappingFree
JNIEXPORT void JNICALL OS_NATIVE(_1XmParseMappingFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmParseMappingFree_FUNC);
	XmParseMappingFree((XmParseMapping)arg0);
	OS_NATIVE_EXIT(env, that, _1XmParseMappingFree_FUNC);
}
#endif

#ifndef NO__1XmProcessTraversal
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmProcessTraversal)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmProcessTraversal_FUNC);
	rc = (jboolean)XmProcessTraversal((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmProcessTraversal_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmRenderTableAddRenditions
JNIEXPORT jint JNICALL OS_NATIVE(_1XmRenderTableAddRenditions)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmRenderTableAddRenditions_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmRenderTableAddRenditions((XmRenderTable)arg0, (XmRendition *)lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmRenderTableAddRenditions_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmRenderTableFree
JNIEXPORT void JNICALL OS_NATIVE(_1XmRenderTableFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmRenderTableFree_FUNC);
	XmRenderTableFree((XmRenderTable)arg0);
	OS_NATIVE_EXIT(env, that, _1XmRenderTableFree_FUNC);
}
#endif

#ifndef NO__1XmRenditionCreate
JNIEXPORT jint JNICALL OS_NATIVE(_1XmRenditionCreate)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmRenditionCreate_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmRenditionCreate((Widget)arg0, (XmStringTag)lparg1, (ArgList)lparg2, arg3);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmRenditionCreate_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmRenditionFree
JNIEXPORT void JNICALL OS_NATIVE(_1XmRenditionFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmRenditionFree_FUNC);
	XmRenditionFree((XmRendition)arg0);
	OS_NATIVE_EXIT(env, that, _1XmRenditionFree_FUNC);
}
#endif

#ifndef NO__1XmStringBaseline
JNIEXPORT jint JNICALL OS_NATIVE(_1XmStringBaseline)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmStringBaseline_FUNC);
	rc = (jint)XmStringBaseline((XmRenderTable)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, _1XmStringBaseline_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmStringCompare
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmStringCompare)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmStringCompare_FUNC);
	rc = (jboolean)XmStringCompare((XmString)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, _1XmStringCompare_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmStringComponentCreate
JNIEXPORT jint JNICALL OS_NATIVE(_1XmStringComponentCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmStringComponentCreate_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XmStringComponentCreate(arg0, arg1, (XtPointer)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XmStringComponentCreate_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmStringConcat
JNIEXPORT jint JNICALL OS_NATIVE(_1XmStringConcat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmStringConcat_FUNC);
	rc = (jint)XmStringConcat((XmString)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, _1XmStringConcat_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmStringCreate
JNIEXPORT jint JNICALL OS_NATIVE(_1XmStringCreate)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmStringCreate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmStringCreate((char *)lparg0, (char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1XmStringCreate_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmStringCreateLocalized
JNIEXPORT jint JNICALL OS_NATIVE(_1XmStringCreateLocalized)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmStringCreateLocalized_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)XmStringCreateLocalized((char *)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1XmStringCreateLocalized_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmStringDraw
JNIEXPORT void JNICALL OS_NATIVE(_1XmStringDraw)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jobject arg10)
{
	XRectangle _arg10, *lparg10=NULL;
	OS_NATIVE_ENTER(env, that, _1XmStringDraw_FUNC);
	if (arg10) if ((lparg10 = getXRectangleFields(env, arg10, &_arg10)) == NULL) goto fail;
	XmStringDraw((Display *)arg0, (Window)arg1, (XmFontList)arg2, (XmString)arg3, (GC)arg4, arg5, arg6, arg7, arg8, arg9, lparg10);
fail:
	if (arg10 && lparg10) setXRectangleFields(env, arg10, lparg10);
	OS_NATIVE_EXIT(env, that, _1XmStringDraw_FUNC);
}
#endif

#ifndef NO__1XmStringDrawImage
JNIEXPORT void JNICALL OS_NATIVE(_1XmStringDrawImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jobject arg10)
{
	XRectangle _arg10, *lparg10=NULL;
	OS_NATIVE_ENTER(env, that, _1XmStringDrawImage_FUNC);
	if (arg10) if ((lparg10 = getXRectangleFields(env, arg10, &_arg10)) == NULL) goto fail;
	XmStringDrawImage((Display *)arg0, (Window)arg1, (XmFontList)arg2, (XmString)arg3, (GC)arg4, arg5, arg6, arg7, arg8, arg9, lparg10);
fail:
	if (arg10 && lparg10) setXRectangleFields(env, arg10, lparg10);
	OS_NATIVE_EXIT(env, that, _1XmStringDrawImage_FUNC);
}
#endif

#ifndef NO__1XmStringDrawUnderline
JNIEXPORT void JNICALL OS_NATIVE(_1XmStringDrawUnderline)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jobject arg10, jint arg11)
{
	XRectangle _arg10, *lparg10=NULL;
	OS_NATIVE_ENTER(env, that, _1XmStringDrawUnderline_FUNC);
	if (arg10) if ((lparg10 = getXRectangleFields(env, arg10, &_arg10)) == NULL) goto fail;
	XmStringDrawUnderline((Display *)arg0, (Window)arg1, (XmFontList)arg2, (XmString)arg3, (GC)arg4, arg5, arg6, arg7, arg8, arg9, lparg10, (XmString)arg11);
fail:
	if (arg10 && lparg10) setXRectangleFields(env, arg10, lparg10);
	OS_NATIVE_EXIT(env, that, _1XmStringDrawUnderline_FUNC);
}
#endif

#ifndef NO__1XmStringEmpty
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmStringEmpty)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmStringEmpty_FUNC);
	rc = (jboolean)XmStringEmpty((XmString)arg0);
	OS_NATIVE_EXIT(env, that, _1XmStringEmpty_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmStringExtent
JNIEXPORT void JNICALL OS_NATIVE(_1XmStringExtent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jshortArray arg3)
{
	jshort *lparg2=NULL;
	jshort *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1XmStringExtent_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	XmStringExtent((XmRenderTable)arg0, (XmString)arg1, (Dimension *)lparg2, (Dimension *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XmStringExtent_FUNC);
}
#endif

#ifndef NO__1XmStringFree
JNIEXPORT void JNICALL OS_NATIVE(_1XmStringFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmStringFree_FUNC);
	XmStringFree((XmString)arg0);
	OS_NATIVE_EXIT(env, that, _1XmStringFree_FUNC);
}
#endif

#ifndef NO__1XmStringGenerate
JNIEXPORT jint JNICALL OS_NATIVE(_1XmStringGenerate)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmStringGenerate_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XmStringGenerate((XtPointer)lparg0, (XmStringTag)lparg1, arg2, (XmStringTag)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1XmStringGenerate_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmStringHeight
JNIEXPORT jint JNICALL OS_NATIVE(_1XmStringHeight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmStringHeight_FUNC);
	rc = (jint)XmStringHeight((XmFontList)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, _1XmStringHeight_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmStringParseText
JNIEXPORT jint JNICALL OS_NATIVE(_1XmStringParseText)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jbyteArray arg2, jint arg3, jintArray arg4, jint arg5, jint arg6)
{
	jbyte *lparg0=NULL;
	jbyte *lparg2=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmStringParseText_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XmStringParseText((XtPointer)lparg0, (XtPointer *)arg1, (XmStringTag)lparg2, arg3, (XmParseTable)lparg4, arg5, (XtPointer)arg6);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1XmStringParseText_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmStringUnparse
JNIEXPORT jint JNICALL OS_NATIVE(_1XmStringUnparse)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jintArray arg4, jint arg5, jint arg6)
{
	jbyte *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmStringUnparse_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XmStringUnparse((XmString)arg0, (XmStringTag)lparg1, arg2, arg3, (XmParseTable)lparg4, arg5, arg6);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmStringUnparse_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmStringWidth
JNIEXPORT jint JNICALL OS_NATIVE(_1XmStringWidth)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmStringWidth_FUNC);
	rc = (jint)XmStringWidth((XmFontList)arg0, (XmString)arg1);
	OS_NATIVE_EXIT(env, that, _1XmStringWidth_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTabCreate
JNIEXPORT jint JNICALL OS_NATIVE(_1XmTabCreate)
	(JNIEnv *env, jclass that, jint arg0, jbyte arg1, jbyte arg2, jbyte arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTabCreate_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XmTabCreate(arg0, arg1, arg2, arg3, (char *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1XmTabCreate_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTabFree
JNIEXPORT void JNICALL OS_NATIVE(_1XmTabFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmTabFree_FUNC);
	XmTabFree((XmTab)arg0);
	OS_NATIVE_EXIT(env, that, _1XmTabFree_FUNC);
}
#endif

#ifndef NO__1XmTabListFree
JNIEXPORT void JNICALL OS_NATIVE(_1XmTabListFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmTabListFree_FUNC);
	XmTabListFree((XmTabList)arg0);
	OS_NATIVE_EXIT(env, that, _1XmTabListFree_FUNC);
}
#endif

#ifndef NO__1XmTabListInsertTabs
JNIEXPORT jint JNICALL OS_NATIVE(_1XmTabListInsertTabs)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTabListInsertTabs_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XmTabListInsertTabs((XmTabList)arg0, (XmTab *)lparg1, arg2, arg3);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmTabListInsertTabs_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextClearSelection
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextClearSelection)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmTextClearSelection_FUNC);
	XmTextClearSelection((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmTextClearSelection_FUNC);
}
#endif

#ifndef NO__1XmTextCopy
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmTextCopy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextCopy_FUNC);
	rc = (jboolean)XmTextCopy((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmTextCopy_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextCut
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmTextCut)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextCut_FUNC);
	rc = (jboolean)XmTextCut((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmTextCut_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextDisableRedisplay
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextDisableRedisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmTextDisableRedisplay_FUNC);
	XmTextDisableRedisplay((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmTextDisableRedisplay_FUNC);
}
#endif

#ifndef NO__1XmTextEnableRedisplay
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextEnableRedisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmTextEnableRedisplay_FUNC);
	XmTextEnableRedisplay((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmTextEnableRedisplay_FUNC);
}
#endif

#ifndef NO__1XmTextFieldPaste
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmTextFieldPaste)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextFieldPaste_FUNC);
	rc = (jboolean)XmTextFieldPaste((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmTextFieldPaste_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextGetInsertionPosition
JNIEXPORT jint JNICALL OS_NATIVE(_1XmTextGetInsertionPosition)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextGetInsertionPosition_FUNC);
	rc = (jint)XmTextGetInsertionPosition((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmTextGetInsertionPosition_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextGetLastPosition
JNIEXPORT jint JNICALL OS_NATIVE(_1XmTextGetLastPosition)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextGetLastPosition_FUNC);
	rc = (jint)XmTextGetLastPosition((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmTextGetLastPosition_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextGetMaxLength
JNIEXPORT jint JNICALL OS_NATIVE(_1XmTextGetMaxLength)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextGetMaxLength_FUNC);
	rc = (jint)XmTextGetMaxLength((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmTextGetMaxLength_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextGetSelection
JNIEXPORT jint JNICALL OS_NATIVE(_1XmTextGetSelection)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextGetSelection_FUNC);
	rc = (jint)XmTextGetSelection((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmTextGetSelection_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextGetSelectionPosition
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmTextGetSelectionPosition)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextGetSelectionPosition_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jboolean)XmTextGetSelectionPosition((Widget)arg0, (XmTextPosition *)lparg1, (XmTextPosition *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmTextGetSelectionPosition_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextGetString
JNIEXPORT jint JNICALL OS_NATIVE(_1XmTextGetString)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextGetString_FUNC);
	rc = (jint)XmTextGetString((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmTextGetString_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextGetSubstring
JNIEXPORT jint JNICALL OS_NATIVE(_1XmTextGetSubstring)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextGetSubstring_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XmTextGetSubstring((Widget)arg0, arg1, arg2, arg3, (char *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1XmTextGetSubstring_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextGetSubstringWcs
JNIEXPORT jint JNICALL OS_NATIVE(_1XmTextGetSubstringWcs)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextGetSubstringWcs_FUNC);
	if (arg4) if ((lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XmTextGetSubstringWcs((Widget)arg0, (XmTextPosition)arg1, arg2, arg3, (wchar_t *)lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	OS_NATIVE_EXIT(env, that, _1XmTextGetSubstringWcs_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextInsert
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextInsert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1XmTextInsert_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL)) == NULL) goto fail;
	XmTextInsert((Widget)arg0, arg1, (char *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XmTextInsert_FUNC);
}
#endif

#ifndef NO__1XmTextPaste
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmTextPaste)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextPaste_FUNC);
	rc = (jboolean)XmTextPaste((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmTextPaste_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextPosToXY
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmTextPosToXY)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jshortArray arg3)
{
	jshort *lparg2=NULL;
	jshort *lparg3=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextPosToXY_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jboolean)XmTextPosToXY((Widget)arg0, (XmTextPosition)arg1, (Position *)lparg2, (Position *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XmTextPosToXY_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmTextReplace
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextReplace)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1XmTextReplace_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	XmTextReplace((Widget)arg0, arg1, arg2, (char *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1XmTextReplace_FUNC);
}
#endif

#ifndef NO__1XmTextScroll
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextScroll)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmTextScroll_FUNC);
	XmTextScroll((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmTextScroll_FUNC);
}
#endif

#ifndef NO__1XmTextSetEditable
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextSetEditable)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmTextSetEditable_FUNC);
	XmTextSetEditable((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmTextSetEditable_FUNC);
}
#endif

#ifndef NO__1XmTextSetHighlight
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextSetHighlight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1XmTextSetHighlight_FUNC);
	XmTextSetHighlight((Widget)arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1XmTextSetHighlight_FUNC);
}
#endif

#ifndef NO__1XmTextSetInsertionPosition
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextSetInsertionPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmTextSetInsertionPosition_FUNC);
	XmTextSetInsertionPosition((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmTextSetInsertionPosition_FUNC);
}
#endif

#ifndef NO__1XmTextSetMaxLength
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextSetMaxLength)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmTextSetMaxLength_FUNC);
	XmTextSetMaxLength((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmTextSetMaxLength_FUNC);
}
#endif

#ifndef NO__1XmTextSetSelection
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextSetSelection)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1XmTextSetSelection_FUNC);
	XmTextSetSelection((Widget)arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1XmTextSetSelection_FUNC);
}
#endif

#ifndef NO__1XmTextSetString
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextSetString)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1XmTextSetString_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XmTextSetString((Widget)arg0, (char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XmTextSetString_FUNC);
}
#endif

#ifndef NO__1XmTextShowPosition
JNIEXPORT void JNICALL OS_NATIVE(_1XmTextShowPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XmTextShowPosition_FUNC);
	XmTextShowPosition((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XmTextShowPosition_FUNC);
}
#endif

#ifndef NO__1XmTextXYToPos
JNIEXPORT jint JNICALL OS_NATIVE(_1XmTextXYToPos)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmTextXYToPos_FUNC);
	rc = (jint)XmTextXYToPos((Widget)arg0, (Position)arg1, (Position)arg2);
	OS_NATIVE_EXIT(env, that, _1XmTextXYToPos_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmUpdateDisplay
JNIEXPORT void JNICALL OS_NATIVE(_1XmUpdateDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XmUpdateDisplay_FUNC);
	XmUpdateDisplay((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XmUpdateDisplay_FUNC);
}
#endif

#ifndef NO__1XmWidgetGetDisplayRect
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XmWidgetGetDisplayRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	XRectangle _arg1, *lparg1=NULL;
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmWidgetGetDisplayRect_FUNC);
	if (arg1) if ((lparg1 = getXRectangleFields(env, arg1, &_arg1)) == NULL) goto fail;
	rc = (jboolean)XmWidgetGetDisplayRect((Widget)arg0, (XRectangle *)lparg1);
fail:
	if (arg1 && lparg1) setXRectangleFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1XmWidgetGetDisplayRect_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmbTextListToTextProperty
JNIEXPORT jint JNICALL OS_NATIVE(_1XmbTextListToTextProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	XTextProperty _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmbTextListToTextProperty_FUNC);
	if (arg4) if ((lparg4 = getXTextPropertyFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)XmbTextListToTextProperty((Display *)arg0, (char **)arg1, arg2, (XICCEncodingStyle)arg3, lparg4);
fail:
	if (arg4 && lparg4) setXTextPropertyFields(env, arg4, lparg4);
	OS_NATIVE_EXIT(env, that, _1XmbTextListToTextProperty_FUNC);
	return rc;
}
#endif

#ifndef NO__1XmbTextPropertyToTextList
JNIEXPORT jint JNICALL OS_NATIVE(_1XmbTextPropertyToTextList)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jintArray arg2, jintArray arg3)
{
	XTextProperty _arg1, *lparg1=NULL;
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XmbTextPropertyToTextList_FUNC);
	if (arg1) if ((lparg1 = getXTextPropertyFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XmbTextPropertyToTextList((Display *)arg0, lparg1, (char ***)lparg2, (int *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) setXTextPropertyFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1XmbTextPropertyToTextList_FUNC);
	return rc;
}
#endif

#ifndef NO__1XpCancelJob
JNIEXPORT void JNICALL OS_NATIVE(_1XpCancelJob)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1XpCancelJob_FUNC);
	XpCancelJob((Display *)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XpCancelJob_FUNC);
}
#endif

#ifndef NO__1XpCreateContext
JNIEXPORT jint JNICALL OS_NATIVE(_1XpCreateContext)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XpCreateContext_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XpCreateContext((Display *)arg0, (char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XpCreateContext_FUNC);
	return rc;
}
#endif

#ifndef NO__1XpDestroyContext
JNIEXPORT void JNICALL OS_NATIVE(_1XpDestroyContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XpDestroyContext_FUNC);
	XpDestroyContext((Display *)arg0, (XPContext)arg1);
	OS_NATIVE_EXIT(env, that, _1XpDestroyContext_FUNC);
}
#endif

#ifndef NO__1XpEndJob
JNIEXPORT void JNICALL OS_NATIVE(_1XpEndJob)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XpEndJob_FUNC);
	XpEndJob((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XpEndJob_FUNC);
}
#endif

#ifndef NO__1XpEndPage
JNIEXPORT void JNICALL OS_NATIVE(_1XpEndPage)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XpEndPage_FUNC);
	XpEndPage((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XpEndPage_FUNC);
}
#endif

#ifndef NO__1XpFreePrinterList
JNIEXPORT void JNICALL OS_NATIVE(_1XpFreePrinterList)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XpFreePrinterList_FUNC);
	XpFreePrinterList((XPPrinterList)arg0);
	OS_NATIVE_EXIT(env, that, _1XpFreePrinterList_FUNC);
}
#endif

#ifndef NO__1XpGetOneAttribute
JNIEXPORT jint JNICALL OS_NATIVE(_1XpGetOneAttribute)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyte arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XpGetOneAttribute_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XpGetOneAttribute((Display *)arg0, (XPContext)arg1, (XPAttributes)arg2, (char *)lparg3);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1XpGetOneAttribute_FUNC);
	return rc;
}
#endif

#ifndef NO__1XpGetPageDimensions
JNIEXPORT jint JNICALL OS_NATIVE(_1XpGetPageDimensions)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jshortArray arg3, jobject arg4)
{
	jshort *lparg2=NULL;
	jshort *lparg3=NULL;
	XRectangle _arg4, *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XpGetPageDimensions_FUNC);
	if (arg2) if ((lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = getXRectangleFields(env, arg4, &_arg4)) == NULL) goto fail;
	rc = (jint)XpGetPageDimensions((Display *)arg0, (XPContext)arg1, (unsigned short *)lparg2, (unsigned short *)lparg3, (XRectangle *)lparg4);
fail:
	if (arg4 && lparg4) setXRectangleFields(env, arg4, lparg4);
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2 && lparg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	OS_NATIVE_EXIT(env, that, _1XpGetPageDimensions_FUNC);
	return rc;
}
#endif

#ifndef NO__1XpGetPrinterList
JNIEXPORT jint JNICALL OS_NATIVE(_1XpGetPrinterList)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XpGetPrinterList_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	rc = (jint)XpGetPrinterList((Display *)arg0, (char *)lparg1, (int *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XpGetPrinterList_FUNC);
	return rc;
}
#endif

#ifndef NO__1XpGetScreenOfContext
JNIEXPORT jint JNICALL OS_NATIVE(_1XpGetScreenOfContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XpGetScreenOfContext_FUNC);
	rc = (jint)XpGetScreenOfContext((Display *)arg0, (XPContext)arg1);
	OS_NATIVE_EXIT(env, that, _1XpGetScreenOfContext_FUNC);
	return rc;
}
#endif

#ifndef NO__1XpSetAttributes
JNIEXPORT void JNICALL OS_NATIVE(_1XpSetAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyte arg2, jbyteArray arg3, jbyte arg4)
{
	jbyte *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1XpSetAttributes_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL)) == NULL) goto fail;
	XpSetAttributes((Display *)arg0, (XPContext)arg1, (XPAttributes)arg2, (char *)lparg3, (XPAttrReplacement)arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1XpSetAttributes_FUNC);
}
#endif

#ifndef NO__1XpSetContext
JNIEXPORT void JNICALL OS_NATIVE(_1XpSetContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XpSetContext_FUNC);
	XpSetContext((Display *)arg0, (XPContext)arg1);
	OS_NATIVE_EXIT(env, that, _1XpSetContext_FUNC);
}
#endif

#ifndef NO__1XpStartJob
JNIEXPORT void JNICALL OS_NATIVE(_1XpStartJob)
	(JNIEnv *env, jclass that, jint arg0, jbyte arg1)
{
	OS_NATIVE_ENTER(env, that, _1XpStartJob_FUNC);
	XpStartJob((Display *)arg0, (XPSaveData)arg1);
	OS_NATIVE_EXIT(env, that, _1XpStartJob_FUNC);
}
#endif

#ifndef NO__1XpStartPage
JNIEXPORT void JNICALL OS_NATIVE(_1XpStartPage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XpStartPage_FUNC);
	XpStartPage((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, _1XpStartPage_FUNC);
}
#endif

#ifndef NO__1XtAddCallback
JNIEXPORT void JNICALL OS_NATIVE(_1XtAddCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1XtAddCallback_FUNC);
	XtAddCallback((Widget)arg0, (String)arg1, (XtCallbackProc)arg2, (XtPointer)arg3);
	OS_NATIVE_EXIT(env, that, _1XtAddCallback_FUNC);
}
#endif

#ifndef NO__1XtAddEventHandler
JNIEXPORT void JNICALL OS_NATIVE(_1XtAddEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, _1XtAddEventHandler_FUNC);
	XtAddEventHandler((Widget)arg0, arg1, arg2, (XtEventHandler)arg3, (XtPointer)arg4);
	OS_NATIVE_EXIT(env, that, _1XtAddEventHandler_FUNC);
}
#endif

#ifndef NO__1XtAddExposureToRegion
JNIEXPORT void JNICALL OS_NATIVE(_1XtAddExposureToRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XtAddExposureToRegion_FUNC);
	XtAddExposureToRegion((XEvent *)arg0, (Region)arg1);
	OS_NATIVE_EXIT(env, that, _1XtAddExposureToRegion_FUNC);
}
#endif

#ifndef NO__1XtAppAddInput
JNIEXPORT jint JNICALL OS_NATIVE(_1XtAppAddInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtAppAddInput_FUNC);
	rc = (jint)XtAppAddInput((XtAppContext)arg0, arg1, (XtPointer)arg2, (XtInputCallbackProc)arg3, (XtPointer)arg4);
	OS_NATIVE_EXIT(env, that, _1XtAppAddInput_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtAppAddTimeOut
JNIEXPORT jint JNICALL OS_NATIVE(_1XtAppAddTimeOut)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtAppAddTimeOut_FUNC);
	rc = (jint)XtAppAddTimeOut((XtAppContext)arg0, arg1, (XtTimerCallbackProc)arg2, (XtPointer)arg3);
	OS_NATIVE_EXIT(env, that, _1XtAppAddTimeOut_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtAppCreateShell
JNIEXPORT jint JNICALL OS_NATIVE(_1XtAppCreateShell)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jint arg2, jint arg3, jintArray arg4, jint arg5)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtAppCreateShell_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL)) == NULL) goto fail;
	rc = (jint)XtAppCreateShell((String)lparg0, (String)lparg1, (WidgetClass)arg2, (Display *)arg3, (ArgList)lparg4, arg5);
fail:
	if (arg4 && lparg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1XtAppCreateShell_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtAppGetSelectionTimeout
JNIEXPORT jint JNICALL OS_NATIVE(_1XtAppGetSelectionTimeout)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtAppGetSelectionTimeout_FUNC);
	rc = (jint)XtAppGetSelectionTimeout((XtAppContext)arg0);
	OS_NATIVE_EXIT(env, that, _1XtAppGetSelectionTimeout_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtAppNextEvent
JNIEXPORT void JNICALL OS_NATIVE(_1XtAppNextEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XtAppNextEvent_FUNC);
	XtAppNextEvent((XtAppContext)arg0, (XEvent *)arg1);
	OS_NATIVE_EXIT(env, that, _1XtAppNextEvent_FUNC);
}
#endif

#ifndef NO__1XtAppPeekEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XtAppPeekEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtAppPeekEvent_FUNC);
	rc = (jboolean)XtAppPeekEvent((XtAppContext)arg0, (XEvent *)arg1);
	OS_NATIVE_EXIT(env, that, _1XtAppPeekEvent_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtAppPending
JNIEXPORT jint JNICALL OS_NATIVE(_1XtAppPending)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtAppPending_FUNC);
	rc = (jint)XtAppPending((XtAppContext)arg0);
	OS_NATIVE_EXIT(env, that, _1XtAppPending_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtAppProcessEvent
JNIEXPORT void JNICALL OS_NATIVE(_1XtAppProcessEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XtAppProcessEvent_FUNC);
	XtAppProcessEvent((XtAppContext)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XtAppProcessEvent_FUNC);
}
#endif

#ifndef NO__1XtAppSetErrorHandler
JNIEXPORT jint JNICALL OS_NATIVE(_1XtAppSetErrorHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtAppSetErrorHandler_FUNC);
	rc = (jint)XtAppSetErrorHandler((XtAppContext)arg0, (XtErrorHandler)arg1);
	OS_NATIVE_EXIT(env, that, _1XtAppSetErrorHandler_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtAppSetFallbackResources
JNIEXPORT void JNICALL OS_NATIVE(_1XtAppSetFallbackResources)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XtAppSetFallbackResources_FUNC);
	XtAppSetFallbackResources((XtAppContext)arg0, (String *)arg1);
	OS_NATIVE_EXIT(env, that, _1XtAppSetFallbackResources_FUNC);
}
#endif

#ifndef NO__1XtAppSetSelectionTimeout
JNIEXPORT void JNICALL OS_NATIVE(_1XtAppSetSelectionTimeout)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XtAppSetSelectionTimeout_FUNC);
	XtAppSetSelectionTimeout((XtAppContext)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XtAppSetSelectionTimeout_FUNC);
}
#endif

#ifndef NO__1XtAppSetWarningHandler
JNIEXPORT jint JNICALL OS_NATIVE(_1XtAppSetWarningHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtAppSetWarningHandler_FUNC);
	rc = (jint)XtAppSetWarningHandler((XtAppContext)arg0, (XtErrorHandler)arg1);
	OS_NATIVE_EXIT(env, that, _1XtAppSetWarningHandler_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtBuildEventMask
JNIEXPORT jint JNICALL OS_NATIVE(_1XtBuildEventMask)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtBuildEventMask_FUNC);
	rc = (jint)XtBuildEventMask((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtBuildEventMask_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtCallActionProc
JNIEXPORT void JNICALL OS_NATIVE(_1XtCallActionProc)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3, jint arg4)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	OS_NATIVE_ENTER(env, that, _1XtCallActionProc_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	XtCallActionProc((Widget)arg0, (String)lparg1, (XEvent *)arg2, (String *)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XtCallActionProc_FUNC);
}
#endif

#ifndef NO__1XtClass
JNIEXPORT jint JNICALL OS_NATIVE(_1XtClass)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtClass_FUNC);
	rc = (jint)XtClass((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtClass_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtConfigureWidget
JNIEXPORT void JNICALL OS_NATIVE(_1XtConfigureWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, _1XtConfigureWidget_FUNC);
	XtConfigureWidget((Widget)arg0, arg1, arg2, arg3, arg4, arg5);
	OS_NATIVE_EXIT(env, that, _1XtConfigureWidget_FUNC);
}
#endif

#ifndef NO__1XtCreateApplicationContext
JNIEXPORT jint JNICALL OS_NATIVE(_1XtCreateApplicationContext)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtCreateApplicationContext_FUNC);
	rc = (jint)XtCreateApplicationContext();
	OS_NATIVE_EXIT(env, that, _1XtCreateApplicationContext_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtCreatePopupShell
JNIEXPORT jint JNICALL OS_NATIVE(_1XtCreatePopupShell)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2, jintArray arg3, jint arg4)
{
	jbyte *lparg0=NULL;
	jint *lparg3=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtCreatePopupShell_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	if (arg3) if ((lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL)) == NULL) goto fail;
	rc = (jint)XtCreatePopupShell((String)lparg0, (WidgetClass)arg1, (Widget)arg2, (ArgList)lparg3, arg4);
fail:
	if (arg3 && lparg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1XtCreatePopupShell_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtDestroyApplicationContext
JNIEXPORT void JNICALL OS_NATIVE(_1XtDestroyApplicationContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XtDestroyApplicationContext_FUNC);
	XtDestroyApplicationContext((XtAppContext)arg0);
	OS_NATIVE_EXIT(env, that, _1XtDestroyApplicationContext_FUNC);
}
#endif

#ifndef NO__1XtDestroyWidget
JNIEXPORT void JNICALL OS_NATIVE(_1XtDestroyWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XtDestroyWidget_FUNC);
	XtDestroyWidget((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtDestroyWidget_FUNC);
}
#endif

#ifndef NO__1XtDisownSelection
JNIEXPORT void JNICALL OS_NATIVE(_1XtDisownSelection)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XtDisownSelection_FUNC);
	XtDisownSelection((Widget)arg0, (Atom)arg1, (Time)arg2);
	OS_NATIVE_EXIT(env, that, _1XtDisownSelection_FUNC);
}
#endif

#ifndef NO__1XtDispatchEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XtDispatchEvent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtDispatchEvent_FUNC);
	rc = (jboolean)XtDispatchEvent((XEvent *)arg0);
	OS_NATIVE_EXIT(env, that, _1XtDispatchEvent_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtDisplay
JNIEXPORT jint JNICALL OS_NATIVE(_1XtDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtDisplay_FUNC);
	rc = (jint)XtDisplay((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtDisplay_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtDisplayToApplicationContext
JNIEXPORT jint JNICALL OS_NATIVE(_1XtDisplayToApplicationContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtDisplayToApplicationContext_FUNC);
	rc = (jint)XtDisplayToApplicationContext((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XtDisplayToApplicationContext_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtFree
JNIEXPORT void JNICALL OS_NATIVE(_1XtFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XtFree_FUNC);
	XtFree((char *)arg0);
	OS_NATIVE_EXIT(env, that, _1XtFree_FUNC);
}
#endif

#ifndef NO__1XtGetDisplays
JNIEXPORT void JNICALL OS_NATIVE(_1XtGetDisplays)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	OS_NATIVE_ENTER(env, that, _1XtGetDisplays_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	if (arg2) if ((lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL)) == NULL) goto fail;
	XtGetDisplays((XtAppContext)arg0, (Display ***)lparg1, (Cardinal *)lparg2);
fail:
	if (arg2 && lparg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XtGetDisplays_FUNC);
}
#endif

#ifndef NO__1XtGetMultiClickTime
JNIEXPORT jint JNICALL OS_NATIVE(_1XtGetMultiClickTime)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtGetMultiClickTime_FUNC);
	rc = (jint)XtGetMultiClickTime((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XtGetMultiClickTime_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtGetSelectionValue
JNIEXPORT void JNICALL OS_NATIVE(_1XtGetSelectionValue)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, _1XtGetSelectionValue_FUNC);
	XtGetSelectionValue((Widget)arg0, (Atom)arg1, (Atom)arg2, (XtSelectionCallbackProc)arg3, (XtPointer)arg4, (Time)arg5);
	OS_NATIVE_EXIT(env, that, _1XtGetSelectionValue_FUNC);
}
#endif

#ifndef NO__1XtInsertEventHandler
JNIEXPORT void JNICALL OS_NATIVE(_1XtInsertEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4, jint arg5)
{
	OS_NATIVE_ENTER(env, that, _1XtInsertEventHandler_FUNC);
	XtInsertEventHandler((Widget)arg0, (EventMask)arg1, (Boolean)arg2, (XtEventHandler)arg3, (XtPointer)arg4, (XtListPosition)arg5);
	OS_NATIVE_EXIT(env, that, _1XtInsertEventHandler_FUNC);
}
#endif

#ifndef NO__1XtIsManaged
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XtIsManaged)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtIsManaged_FUNC);
	rc = (jboolean)XtIsManaged((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtIsManaged_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtIsRealized
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XtIsRealized)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtIsRealized_FUNC);
	rc = (jboolean)XtIsRealized((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtIsRealized_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtIsSubclass
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XtIsSubclass)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtIsSubclass_FUNC);
	rc = (jboolean)XtIsSubclass((Widget)arg0, (WidgetClass)arg1);
	OS_NATIVE_EXIT(env, that, _1XtIsSubclass_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtIsTopLevelShell
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XtIsTopLevelShell)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtIsTopLevelShell_FUNC);
	rc = (jboolean)XtIsTopLevelShell((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtIsTopLevelShell_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtLastTimestampProcessed
JNIEXPORT jint JNICALL OS_NATIVE(_1XtLastTimestampProcessed)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtLastTimestampProcessed_FUNC);
	rc = (jint)XtLastTimestampProcessed((Display *)arg0);
	OS_NATIVE_EXIT(env, that, _1XtLastTimestampProcessed_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtMalloc
JNIEXPORT jint JNICALL OS_NATIVE(_1XtMalloc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtMalloc_FUNC);
	rc = (jint)XtMalloc(arg0);
	OS_NATIVE_EXIT(env, that, _1XtMalloc_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtManageChild
JNIEXPORT void JNICALL OS_NATIVE(_1XtManageChild)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XtManageChild_FUNC);
	XtManageChild((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtManageChild_FUNC);
}
#endif

#ifndef NO__1XtMapWidget
JNIEXPORT void JNICALL OS_NATIVE(_1XtMapWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XtMapWidget_FUNC);
	XtMapWidget((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtMapWidget_FUNC);
}
#endif

#ifndef NO__1XtMoveWidget
JNIEXPORT void JNICALL OS_NATIVE(_1XtMoveWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XtMoveWidget_FUNC);
	XtMoveWidget((Widget)arg0, arg1, arg2);
	OS_NATIVE_EXIT(env, that, _1XtMoveWidget_FUNC);
}
#endif

#ifndef NO__1XtNameToWidget
JNIEXPORT jint JNICALL OS_NATIVE(_1XtNameToWidget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtNameToWidget_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jint)XtNameToWidget((Widget)arg0, (String)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XtNameToWidget_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtOpenDisplay
JNIEXPORT jint JNICALL OS_NATIVE(_1XtOpenDisplay)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jbyteArray arg2, jbyteArray arg3, jint arg4, jint arg5, jintArray arg6, jint arg7)
{
	jbyte *lparg1=NULL;
	jbyte *lparg2=NULL;
	jbyte *lparg3=NULL;
	jint *lparg6=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtOpenDisplay_FUNC);
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
	OS_NATIVE_EXIT(env, that, _1XtOpenDisplay_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtOverrideTranslations
JNIEXPORT void JNICALL OS_NATIVE(_1XtOverrideTranslations)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XtOverrideTranslations_FUNC);
	XtOverrideTranslations((Widget)arg0, (XtTranslations)arg1);
	OS_NATIVE_EXIT(env, that, _1XtOverrideTranslations_FUNC);
}
#endif

#ifndef NO__1XtOwnSelection
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XtOwnSelection)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtOwnSelection_FUNC);
	rc = (jboolean)XtOwnSelection((Widget)arg0, (Atom)arg1, (Time)arg2, (XtConvertSelectionProc)arg3, (XtLoseSelectionProc)arg4, (XtSelectionDoneProc)arg5);
	OS_NATIVE_EXIT(env, that, _1XtOwnSelection_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtParent
JNIEXPORT jint JNICALL OS_NATIVE(_1XtParent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtParent_FUNC);
	rc = (jint)XtParent((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtParent_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtParseTranslationTable
JNIEXPORT jint JNICALL OS_NATIVE(_1XtParseTranslationTable)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtParseTranslationTable_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)XtParseTranslationTable((String)lparg0);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1XtParseTranslationTable_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtPopdown
JNIEXPORT void JNICALL OS_NATIVE(_1XtPopdown)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XtPopdown_FUNC);
	XtPopdown((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtPopdown_FUNC);
}
#endif

#ifndef NO__1XtPopup
JNIEXPORT void JNICALL OS_NATIVE(_1XtPopup)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XtPopup_FUNC);
	XtPopup((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XtPopup_FUNC);
}
#endif

#ifndef NO__1XtQueryGeometry
JNIEXPORT jint JNICALL OS_NATIVE(_1XtQueryGeometry)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	XtWidgetGeometry _arg1, *lparg1=NULL;
	XtWidgetGeometry _arg2, *lparg2=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtQueryGeometry_FUNC);
	if (arg1) if ((lparg1 = getXtWidgetGeometryFields(env, arg1, &_arg1)) == NULL) goto fail;
	if (arg2) if ((lparg2 = getXtWidgetGeometryFields(env, arg2, &_arg2)) == NULL) goto fail;
	rc = (jint)XtQueryGeometry((Widget)arg0, (XtWidgetGeometry *)lparg1, (XtWidgetGeometry *)lparg2);
fail:
	if (arg2 && lparg2) setXtWidgetGeometryFields(env, arg2, lparg2);
	if (arg1 && lparg1) setXtWidgetGeometryFields(env, arg1, lparg1);
	OS_NATIVE_EXIT(env, that, _1XtQueryGeometry_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtRealizeWidget
JNIEXPORT void JNICALL OS_NATIVE(_1XtRealizeWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XtRealizeWidget_FUNC);
	XtRealizeWidget((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtRealizeWidget_FUNC);
}
#endif

#ifndef NO__1XtRegisterDrawable
JNIEXPORT void JNICALL OS_NATIVE(_1XtRegisterDrawable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	OS_NATIVE_ENTER(env, that, _1XtRegisterDrawable_FUNC);
	XtRegisterDrawable((Display *)arg0, (Drawable)arg1, (Widget)arg2);
	OS_NATIVE_EXIT(env, that, _1XtRegisterDrawable_FUNC);
}
#endif

#ifndef NO__1XtRemoveEventHandler
JNIEXPORT void JNICALL OS_NATIVE(_1XtRemoveEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	OS_NATIVE_ENTER(env, that, _1XtRemoveEventHandler_FUNC);
	XtRemoveEventHandler((Widget)arg0, arg1, arg2, (XtEventHandler)arg3, (XtPointer)arg4);
	OS_NATIVE_EXIT(env, that, _1XtRemoveEventHandler_FUNC);
}
#endif

#ifndef NO__1XtRemoveInput
JNIEXPORT void JNICALL OS_NATIVE(_1XtRemoveInput)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XtRemoveInput_FUNC);
	XtRemoveInput((XtInputId)arg0);
	OS_NATIVE_EXIT(env, that, _1XtRemoveInput_FUNC);
}
#endif

#ifndef NO__1XtRemoveTimeOut
JNIEXPORT void JNICALL OS_NATIVE(_1XtRemoveTimeOut)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XtRemoveTimeOut_FUNC);
	XtRemoveTimeOut(arg0);
	OS_NATIVE_EXIT(env, that, _1XtRemoveTimeOut_FUNC);
}
#endif

#ifndef NO__1XtResizeWidget
JNIEXPORT void JNICALL OS_NATIVE(_1XtResizeWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	OS_NATIVE_ENTER(env, that, _1XtResizeWidget_FUNC);
	XtResizeWidget((Widget)arg0, arg1, arg2, arg3);
	OS_NATIVE_EXIT(env, that, _1XtResizeWidget_FUNC);
}
#endif

#ifndef NO__1XtResizeWindow
JNIEXPORT void JNICALL OS_NATIVE(_1XtResizeWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XtResizeWindow_FUNC);
	XtResizeWindow((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtResizeWindow_FUNC);
}
#endif

#ifndef NO__1XtSetLanguageProc
JNIEXPORT jint JNICALL OS_NATIVE(_1XtSetLanguageProc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtSetLanguageProc_FUNC);
	rc = (jint)XtSetLanguageProc((XtAppContext)arg0, (XtLanguageProc)arg1, (XtPointer)arg2);
	OS_NATIVE_EXIT(env, that, _1XtSetLanguageProc_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtSetMappedWhenManaged
JNIEXPORT void JNICALL OS_NATIVE(_1XtSetMappedWhenManaged)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1XtSetMappedWhenManaged_FUNC);
	XtSetMappedWhenManaged((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1XtSetMappedWhenManaged_FUNC);
}
#endif

#ifndef NO__1XtSetValues
JNIEXPORT void JNICALL OS_NATIVE(_1XtSetValues)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, _1XtSetValues_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL)) == NULL) goto fail;
	XtSetValues((Widget)arg0, (ArgList)lparg1, arg2);
fail:
	if (arg1 && lparg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1XtSetValues_FUNC);
}
#endif

#ifndef NO__1XtToolkitInitialize
JNIEXPORT void JNICALL OS_NATIVE(_1XtToolkitInitialize)
	(JNIEnv *env, jclass that)
{
	OS_NATIVE_ENTER(env, that, _1XtToolkitInitialize_FUNC);
	XtToolkitInitialize();
	OS_NATIVE_EXIT(env, that, _1XtToolkitInitialize_FUNC);
}
#endif

#ifndef NO__1XtToolkitThreadInitialize
JNIEXPORT jboolean JNICALL OS_NATIVE(_1XtToolkitThreadInitialize)
	(JNIEnv *env, jclass that)
{
	jboolean rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtToolkitThreadInitialize_FUNC);
	rc = (jboolean)XtToolkitThreadInitialize();
	OS_NATIVE_EXIT(env, that, _1XtToolkitThreadInitialize_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtTranslateCoords
JNIEXPORT void JNICALL OS_NATIVE(_1XtTranslateCoords)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jshortArray arg3, jshortArray arg4)
{
	jshort *lparg3=NULL;
	jshort *lparg4=NULL;
	OS_NATIVE_ENTER(env, that, _1XtTranslateCoords_FUNC);
	if (arg3) if ((lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL)) == NULL) goto fail;
	if (arg4) if ((lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL)) == NULL) goto fail;
	XtTranslateCoords((Widget)arg0, arg1, arg2, lparg3, lparg4);
fail:
	if (arg4 && lparg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	if (arg3 && lparg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	OS_NATIVE_EXIT(env, that, _1XtTranslateCoords_FUNC);
}
#endif

#ifndef NO__1XtUnmanageChild
JNIEXPORT void JNICALL OS_NATIVE(_1XtUnmanageChild)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XtUnmanageChild_FUNC);
	XtUnmanageChild((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtUnmanageChild_FUNC);
}
#endif

#ifndef NO__1XtUnmapWidget
JNIEXPORT void JNICALL OS_NATIVE(_1XtUnmapWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	OS_NATIVE_ENTER(env, that, _1XtUnmapWidget_FUNC);
	XtUnmapWidget((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtUnmapWidget_FUNC);
}
#endif

#ifndef NO__1XtUnregisterDrawable
JNIEXPORT void JNICALL OS_NATIVE(_1XtUnregisterDrawable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	OS_NATIVE_ENTER(env, that, _1XtUnregisterDrawable_FUNC);
	XtUnregisterDrawable((Display *)arg0, (Drawable)arg1);
	OS_NATIVE_EXIT(env, that, _1XtUnregisterDrawable_FUNC);
}
#endif

#ifndef NO__1XtWindow
JNIEXPORT jint JNICALL OS_NATIVE(_1XtWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtWindow_FUNC);
	rc = (jint)XtWindow((Widget)arg0);
	OS_NATIVE_EXIT(env, that, _1XtWindow_FUNC);
	return rc;
}
#endif

#ifndef NO__1XtWindowToWidget
JNIEXPORT jint JNICALL OS_NATIVE(_1XtWindowToWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1XtWindowToWidget_FUNC);
	rc = (jint)XtWindowToWidget((Display *)arg0, (Window)arg1);
	OS_NATIVE_EXIT(env, that, _1XtWindowToWidget_FUNC);
	return rc;
}
#endif

#ifndef NO__1_1XmSetMenuTraversal
JNIEXPORT void JNICALL OS_NATIVE(_1_1XmSetMenuTraversal)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	OS_NATIVE_ENTER(env, that, _1_1XmSetMenuTraversal_FUNC);
	_XmSetMenuTraversal((Widget)arg0, arg1);
	OS_NATIVE_EXIT(env, that, _1_1XmSetMenuTraversal_FUNC);
}
#endif

#ifndef NO__1_1XtDefaultAppContext
JNIEXPORT jint JNICALL OS_NATIVE(_1_1XtDefaultAppContext)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1_1XtDefaultAppContext_FUNC);
	rc = (jint)_XtDefaultAppContext();
	OS_NATIVE_EXIT(env, that, _1_1XtDefaultAppContext_FUNC);
	return rc;
}
#endif

#ifndef NO__1access
JNIEXPORT jint JNICALL OS_NATIVE(_1access)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1access_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jint)access((const char*)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1access_FUNC);
	return rc;
}
#endif

#ifndef NO__1applicationShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(_1applicationShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1applicationShellWidgetClass_FUNC);
	rc = (jint)applicationShellWidgetClass;
	OS_NATIVE_EXIT(env, that, _1applicationShellWidgetClass_FUNC);
	return rc;
}
#endif

#ifndef NO__1dlclose
JNIEXPORT jint JNICALL OS_NATIVE(_1dlclose)
	(JNIEnv *env, jclass that, jintLong arg0)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1dlclose_FUNC);
	rc = (jint)dlclose((void *)arg0);
	OS_NATIVE_EXIT(env, that, _1dlclose_FUNC);
	return rc;
}
#endif

#ifndef NO__1dlopen
JNIEXPORT jintLong JNICALL OS_NATIVE(_1dlopen)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1)
{
	jbyte *lparg0=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1dlopen_FUNC);
	if (arg0) if ((lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL)) == NULL) goto fail;
	rc = (jintLong)dlopen((const char *)lparg0, arg1);
fail:
	if (arg0 && lparg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	OS_NATIVE_EXIT(env, that, _1dlopen_FUNC);
	return rc;
}
#endif

#ifndef NO__1dlsym
JNIEXPORT jintLong JNICALL OS_NATIVE(_1dlsym)
	(JNIEnv *env, jclass that, jintLong arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jintLong rc = 0;
	OS_NATIVE_ENTER(env, that, _1dlsym_FUNC);
	if (arg1) if ((lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL)) == NULL) goto fail;
	rc = (jintLong)dlsym((void *)arg0, (const char *)lparg1);
fail:
	if (arg1 && lparg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	OS_NATIVE_EXIT(env, that, _1dlsym_FUNC);
	return rc;
}
#endif

#ifndef NO__1overrideShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(_1overrideShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1overrideShellWidgetClass_FUNC);
	rc = (jint)overrideShellWidgetClass;
	OS_NATIVE_EXIT(env, that, _1overrideShellWidgetClass_FUNC);
	return rc;
}
#endif

#ifndef NO__1shellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(_1shellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1shellWidgetClass_FUNC);
	rc = (jint)shellWidgetClass;
	OS_NATIVE_EXIT(env, that, _1shellWidgetClass_FUNC);
	return rc;
}
#endif

#ifndef NO__1topLevelShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(_1topLevelShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1topLevelShellWidgetClass_FUNC);
	rc = (jint)topLevelShellWidgetClass;
	OS_NATIVE_EXIT(env, that, _1topLevelShellWidgetClass_FUNC);
	return rc;
}
#endif

#ifndef NO__1transientShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(_1transientShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1transientShellWidgetClass_FUNC);
	rc = (jint)transientShellWidgetClass;
	OS_NATIVE_EXIT(env, that, _1transientShellWidgetClass_FUNC);
	return rc;
}
#endif

#ifndef NO__1xmMenuShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(_1xmMenuShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, _1xmMenuShellWidgetClass_FUNC);
	rc = (jint)xmMenuShellWidgetClass;
	OS_NATIVE_EXIT(env, that, _1xmMenuShellWidgetClass_FUNC);
	return rc;
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

#ifndef NO_localeconv_1decimal_1point
JNIEXPORT jint JNICALL OS_NATIVE(localeconv_1decimal_1point)
	(JNIEnv *env, jclass that)
{
	jint rc = 0;
	OS_NATIVE_ENTER(env, that, localeconv_1decimal_1point_FUNC);
	rc = (jint)localeconv_decimal_point();
	OS_NATIVE_EXIT(env, that, localeconv_1decimal_1point_FUNC);
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

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XmSpinBoxCallbackStruct_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XmSpinBoxCallbackStruct_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XmSpinBoxCallbackStruct _arg1, *lparg1=NULL;
	OS_NATIVE_ENTER(env, that, memmove__ILorg_eclipse_swt_internal_motif_XmSpinBoxCallbackStruct_2I_FUNC);
	if (arg1) if ((lparg1 = getXmSpinBoxCallbackStructFields(env, arg1, &_arg1)) == NULL) goto fail;
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
fail:
	OS_NATIVE_EXIT(env, that, memmove__ILorg_eclipse_swt_internal_motif_XmSpinBoxCallbackStruct_2I_FUNC);
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

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmSpinBoxCallbackStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmSpinBoxCallbackStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmSpinBoxCallbackStruct _arg0, *lparg0=NULL;
	OS_NATIVE_ENTER(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmSpinBoxCallbackStruct_2II_FUNC);
	if (arg0) if ((lparg0 = &_arg0) == NULL) goto fail;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
fail:
	if (arg0 && lparg0) setXmSpinBoxCallbackStructFields(env, arg0, lparg0);
	OS_NATIVE_EXIT(env, that, memmove__Lorg_eclipse_swt_internal_motif_XmSpinBoxCallbackStruct_2II_FUNC);
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

