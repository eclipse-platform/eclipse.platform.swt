/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others.
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

#define OS_NATIVE(func) Java_org_eclipse_swt_internal_motif_OS_##func

#ifndef NO_CODESET
JNIEXPORT jint JNICALL OS_NATIVE(CODESET)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "CODESET\n")
	rc = (jint)CODESET;
	NATIVE_EXIT(env, that, "CODESET\n")
	return rc;
}
#endif

#ifndef NO_ConnectionNumber
JNIEXPORT jint JNICALL OS_NATIVE(ConnectionNumber)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "ConnectionNumber\n")
	rc = (jint)ConnectionNumber(arg0);
	NATIVE_EXIT(env, that, "ConnectionNumber\n")
	return rc;
}
#endif

#ifndef NO_FD_1ISSET
JNIEXPORT jboolean JNICALL OS_NATIVE(FD_1ISSET)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "FD_1ISSET\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jboolean)FD_ISSET(arg0, (fd_set *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "FD_1ISSET\n")
	return rc;
}
#endif

#ifndef NO_FD_1SET
JNIEXPORT void JNICALL OS_NATIVE(FD_1SET)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	NATIVE_ENTER(env, that, "FD_1SET\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	FD_SET(arg0, (fd_set *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "FD_1SET\n")
}
#endif

#ifndef NO_FD_1ZERO
JNIEXPORT void JNICALL OS_NATIVE(FD_1ZERO)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	NATIVE_ENTER(env, that, "FD_1ZERO\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	FD_ZERO((fd_set *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "FD_1ZERO\n")
}
#endif

#ifndef NO_LC_1CTYPE
JNIEXPORT jint JNICALL OS_NATIVE(LC_1CTYPE)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "LC_1CTYPE\n")
	rc = (jint)LC_CTYPE;
	NATIVE_EXIT(env, that, "LC_1CTYPE\n")
	return rc;
}
#endif

#ifndef NO_MB_1CUR_1MAX
JNIEXPORT jint JNICALL OS_NATIVE(MB_1CUR_1MAX)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "MB_1CUR_1MAX\n")
	rc = (jint)MB_CUR_MAX;
	NATIVE_EXIT(env, that, "MB_1CUR_1MAX\n")
	return rc;
}
#endif

#ifndef NO_XAllocColor
JNIEXPORT jint JNICALL OS_NATIVE(XAllocColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	XColor _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XAllocColor\n")
	if (arg2) lparg2 = getXColorFields(env, arg2, &_arg2);
	rc = (jint)XAllocColor((Display *)arg0, arg1, lparg2);
	if (arg2) setXColorFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "XAllocColor\n")
	return rc;
}
#endif

#ifndef NO_XBell
JNIEXPORT void JNICALL OS_NATIVE(XBell)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XBell\n")
	XBell((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XBell\n")
}
#endif

#ifndef NO_XBlackPixel
JNIEXPORT jint JNICALL OS_NATIVE(XBlackPixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XBlackPixel\n")
	rc = (jint)XBlackPixel((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XBlackPixel\n")
	return rc;
}
#endif

#ifndef NO_XChangeActivePointerGrab
JNIEXPORT jint JNICALL OS_NATIVE(XChangeActivePointerGrab)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "XChangeActivePointerGrab\n")
	rc = (jint)XChangeActivePointerGrab((Display *)arg0, arg1, (Cursor)arg2, (Time)arg3);
	NATIVE_EXIT(env, that, "XChangeActivePointerGrab\n")
	return rc;
}
#endif

#ifndef NO_XChangeProperty
JNIEXPORT void JNICALL OS_NATIVE(XChangeProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6, jint arg7)
{
	jint *lparg6=NULL;
	NATIVE_ENTER(env, that, "XChangeProperty\n")
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	XChangeProperty((Display *)arg0, (Window)arg1, (Atom)arg2, (Atom)arg3, arg4, arg5, (unsigned char *)lparg6, arg7);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	NATIVE_EXIT(env, that, "XChangeProperty\n")
}
#endif

#ifndef NO_XChangeWindowAttributes
JNIEXPORT void JNICALL OS_NATIVE(XChangeWindowAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	XSetWindowAttributes _arg3, *lparg3=NULL;
	NATIVE_ENTER(env, that, "XChangeWindowAttributes\n")
	if (arg3) lparg3 = getXSetWindowAttributesFields(env, arg3, &_arg3);
	XChangeWindowAttributes((Display *)arg0, arg1, arg2, lparg3);
	if (arg3) setXSetWindowAttributesFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "XChangeWindowAttributes\n")
}
#endif

#ifndef NO_XCheckIfEvent
JNIEXPORT jint JNICALL OS_NATIVE(XCheckIfEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "XCheckIfEvent\n")
	rc = (jint)XCheckIfEvent((Display *)arg0, (XEvent *)arg1, (Bool (*)())arg2, (XPointer)arg3);
	NATIVE_EXIT(env, that, "XCheckIfEvent\n")
	return rc;
}
#endif

#ifndef NO_XCheckMaskEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(XCheckMaskEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XCheckMaskEvent\n")
	rc = (jboolean)XCheckMaskEvent((Display *)arg0, arg1, (XEvent *)arg2);
	NATIVE_EXIT(env, that, "XCheckMaskEvent\n")
	return rc;
}
#endif

#ifndef NO_XCheckWindowEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(XCheckWindowEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XCheckWindowEvent\n")
	rc = (jboolean)XCheckWindowEvent((Display *)arg0, (Window)arg1, arg2, (XEvent *)arg3);
	NATIVE_EXIT(env, that, "XCheckWindowEvent\n")
	return rc;
}
#endif

#ifndef NO_XClearArea
JNIEXPORT void JNICALL OS_NATIVE(XClearArea)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jboolean arg6)
{
	NATIVE_ENTER(env, that, "XClearArea\n")
	XClearArea((Display *)arg0, arg1, arg2, arg3, arg4, arg5, arg6);
	NATIVE_EXIT(env, that, "XClearArea\n")
}
#endif

#ifndef NO_XClipBox
JNIEXPORT void JNICALL OS_NATIVE(XClipBox)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	XRectangle _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "XClipBox\n")
	if (arg1) lparg1 = getXRectangleFields(env, arg1, &_arg1);
	XClipBox((Region)arg0, (XRectangle *)lparg1);
	if (arg1) setXRectangleFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "XClipBox\n")
}
#endif

#ifndef NO_XCloseDisplay
JNIEXPORT void JNICALL OS_NATIVE(XCloseDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XCloseDisplay\n")
	XCloseDisplay((Display *)arg0);
	NATIVE_EXIT(env, that, "XCloseDisplay\n")
}
#endif

#ifndef NO_XCopyArea
JNIEXPORT void JNICALL OS_NATIVE(XCopyArea)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	NATIVE_ENTER(env, that, "XCopyArea\n")
	XCopyArea((Display *)arg0, arg1, arg2, (GC)arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	NATIVE_EXIT(env, that, "XCopyArea\n")
}
#endif

#ifndef NO_XCopyPlane
JNIEXPORT void JNICALL OS_NATIVE(XCopyPlane)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jint arg10)
{
	NATIVE_ENTER(env, that, "XCopyPlane\n")
	XCopyPlane((Display *)arg0, arg1, arg2, (GC)arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10);
	NATIVE_EXIT(env, that, "XCopyPlane\n")
}
#endif

#ifndef NO_XCreateBitmapFromData
JNIEXPORT jint JNICALL OS_NATIVE(XCreateBitmapFromData)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jint arg3, jint arg4)
{
	jbyte *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XCreateBitmapFromData\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)XCreateBitmapFromData((Display *)arg0, arg1, (char *)lparg2, arg3, arg4);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XCreateBitmapFromData\n")
	return rc;
}
#endif

#ifndef NO_XCreateFontCursor
JNIEXPORT jint JNICALL OS_NATIVE(XCreateFontCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XCreateFontCursor\n")
	rc = (jint)XCreateFontCursor((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XCreateFontCursor\n")
	return rc;
}
#endif

#ifndef NO_XCreateGC
JNIEXPORT jint JNICALL OS_NATIVE(XCreateGC)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	XGCValues _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XCreateGC\n")
	if (arg3) lparg3 = getXGCValuesFields(env, arg3, &_arg3);
	rc = (jint)XCreateGC((Display *)arg0, arg1, arg2, lparg3);
	if (arg3) setXGCValuesFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "XCreateGC\n")
	return rc;
}
#endif

#ifndef NO_XCreateImage
JNIEXPORT jint JNICALL OS_NATIVE(XCreateImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jint rc;
	NATIVE_ENTER(env, that, "XCreateImage\n")
	rc = (jint)XCreateImage((Display *)arg0, (Visual *)arg1, arg2, arg3, arg4, (char *)arg5, arg6, arg7, arg8, arg9);
	NATIVE_EXIT(env, that, "XCreateImage\n")
	return rc;
}
#endif

#ifndef NO_XCreatePixmap
JNIEXPORT jint JNICALL OS_NATIVE(XCreatePixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "XCreatePixmap\n")
	rc = (jint)XCreatePixmap((Display *)arg0, arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "XCreatePixmap\n")
	return rc;
}
#endif

#ifndef NO_XCreatePixmapCursor
JNIEXPORT jint JNICALL OS_NATIVE(XCreatePixmapCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3, jobject arg4, jint arg5, jint arg6)
{
	XColor _arg3, *lparg3=NULL;
	XColor _arg4, *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XCreatePixmapCursor\n")
	if (arg3) lparg3 = getXColorFields(env, arg3, &_arg3);
	if (arg4) lparg4 = getXColorFields(env, arg4, &_arg4);
	rc = (jint)XCreatePixmapCursor((Display *)arg0, (Pixmap)arg1, (Pixmap)arg2, lparg3, lparg4, arg5, arg6);
	if (arg4) setXColorFields(env, arg4, lparg4);
	if (arg3) setXColorFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "XCreatePixmapCursor\n")
	return rc;
}
#endif

#ifndef NO_XCreateRegion
JNIEXPORT jint JNICALL OS_NATIVE(XCreateRegion)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "XCreateRegion\n")
	rc = (jint)XCreateRegion();
	NATIVE_EXIT(env, that, "XCreateRegion\n")
	return rc;
}
#endif

#ifndef NO_XCreateWindow
JNIEXPORT jint JNICALL OS_NATIVE(XCreateWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jlong arg10, jint arg11)
{
	jint rc;
	NATIVE_ENTER(env, that, "XCreateWindow\n")
	rc = (jint)XCreateWindow((Display *)arg0, (Window)arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, (Visual *)arg9, arg10, (XSetWindowAttributes *)arg11);
	NATIVE_EXIT(env, that, "XCreateWindow\n")
	return rc;
}
#endif

#ifndef NO_XDefaultColormap
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultColormap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDefaultColormap\n")
	rc = (jint)XDefaultColormap((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XDefaultColormap\n")
	return rc;
}
#endif

#ifndef NO_XDefaultColormapOfScreen
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultColormapOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDefaultColormapOfScreen\n")
	rc = (jint)XDefaultColormapOfScreen((Screen *)arg0);
	NATIVE_EXIT(env, that, "XDefaultColormapOfScreen\n")
	return rc;
}
#endif

#ifndef NO_XDefaultDepthOfScreen
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultDepthOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDefaultDepthOfScreen\n")
	rc = (jint)XDefaultDepthOfScreen((Screen *)arg0);
	NATIVE_EXIT(env, that, "XDefaultDepthOfScreen\n")
	return rc;
}
#endif

#ifndef NO_XDefaultGCOfScreen
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultGCOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDefaultGCOfScreen\n")
	rc = (jint)XDefaultGCOfScreen((Screen *)arg0);
	NATIVE_EXIT(env, that, "XDefaultGCOfScreen\n")
	return rc;
}
#endif

#ifndef NO_XDefaultRootWindow
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultRootWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDefaultRootWindow\n")
	rc = (jint)XDefaultRootWindow((Display *)arg0);
	NATIVE_EXIT(env, that, "XDefaultRootWindow\n")
	return rc;
}
#endif

#ifndef NO_XDefaultScreen
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDefaultScreen\n")
	rc = (jint)XDefaultScreen((Display *)arg0);
	NATIVE_EXIT(env, that, "XDefaultScreen\n")
	return rc;
}
#endif

#ifndef NO_XDefaultScreenOfDisplay
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultScreenOfDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDefaultScreenOfDisplay\n")
	rc = (jint)XDefaultScreenOfDisplay((Display *)arg0);
	NATIVE_EXIT(env, that, "XDefaultScreenOfDisplay\n")
	return rc;
}
#endif

#ifndef NO_XDefaultVisual
JNIEXPORT jint JNICALL OS_NATIVE(XDefaultVisual)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDefaultVisual\n")
	rc = (jint)XDefaultVisual((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XDefaultVisual\n")
	return rc;
}
#endif

#ifndef NO_XDefineCursor
JNIEXPORT void JNICALL OS_NATIVE(XDefineCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XDefineCursor\n")
	XDefineCursor((Display *)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "XDefineCursor\n")
}
#endif

#ifndef NO_XDestroyImage
JNIEXPORT jint JNICALL OS_NATIVE(XDestroyImage)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDestroyImage\n")
	rc = (jint)XDestroyImage((XImage *)arg0);
	NATIVE_EXIT(env, that, "XDestroyImage\n")
	return rc;
}
#endif

#ifndef NO_XDestroyRegion
JNIEXPORT void JNICALL OS_NATIVE(XDestroyRegion)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XDestroyRegion\n")
	XDestroyRegion((Region)arg0);
	NATIVE_EXIT(env, that, "XDestroyRegion\n")
}
#endif

#ifndef NO_XDestroyWindow
JNIEXPORT void JNICALL OS_NATIVE(XDestroyWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XDestroyWindow\n")
	XDestroyWindow((Display *)arg0, (Window)arg1);
	NATIVE_EXIT(env, that, "XDestroyWindow\n")
}
#endif

#ifndef NO_XDisplayHeight
JNIEXPORT jint JNICALL OS_NATIVE(XDisplayHeight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDisplayHeight\n")
	rc = (jint)XDisplayHeight((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XDisplayHeight\n")
	return rc;
}
#endif

#ifndef NO_XDisplayHeightMM
JNIEXPORT jint JNICALL OS_NATIVE(XDisplayHeightMM)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDisplayHeightMM\n")
	rc = (jint)XDisplayHeightMM((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XDisplayHeightMM\n")
	return rc;
}
#endif

#ifndef NO_XDisplayWidth
JNIEXPORT jint JNICALL OS_NATIVE(XDisplayWidth)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDisplayWidth\n")
	rc = (jint)XDisplayWidth((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XDisplayWidth\n")
	return rc;
}
#endif

#ifndef NO_XDisplayWidthMM
JNIEXPORT jint JNICALL OS_NATIVE(XDisplayWidthMM)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XDisplayWidthMM\n")
	rc = (jint)XDisplayWidthMM((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XDisplayWidthMM\n")
	return rc;
}
#endif

#ifndef NO_XDrawArc
JNIEXPORT void JNICALL OS_NATIVE(XDrawArc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	NATIVE_ENTER(env, that, "XDrawArc\n")
	XDrawArc((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	NATIVE_EXIT(env, that, "XDrawArc\n")
}
#endif

#ifndef NO_XDrawLine
JNIEXPORT void JNICALL OS_NATIVE(XDrawLine)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	NATIVE_ENTER(env, that, "XDrawLine\n")
	XDrawLine((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6);
	NATIVE_EXIT(env, that, "XDrawLine\n")
}
#endif

#ifndef NO_XDrawLines
JNIEXPORT void JNICALL OS_NATIVE(XDrawLines)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3, jint arg4, jint arg5)
{
	jshort *lparg3=NULL;
	NATIVE_ENTER(env, that, "XDrawLines\n")
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	XDrawLines((Display *)arg0, (Drawable)arg1, (GC)arg2, (XPoint *)lparg3, arg4, arg5);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "XDrawLines\n")
}
#endif

#ifndef NO_XDrawPoint
JNIEXPORT void JNICALL OS_NATIVE(XDrawPoint)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	NATIVE_ENTER(env, that, "XDrawPoint\n")
	XDrawPoint((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "XDrawPoint\n")
}
#endif

#ifndef NO_XDrawRectangle
JNIEXPORT void JNICALL OS_NATIVE(XDrawRectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	NATIVE_ENTER(env, that, "XDrawRectangle\n")
	XDrawRectangle((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6);
	NATIVE_EXIT(env, that, "XDrawRectangle\n")
}
#endif

#ifndef NO_XEmptyRegion
JNIEXPORT jboolean JNICALL OS_NATIVE(XEmptyRegion)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XEmptyRegion\n")
	rc = (jboolean)XEmptyRegion((Region)arg0);
	NATIVE_EXIT(env, that, "XEmptyRegion\n")
	return rc;
}
#endif

#ifndef NO_XEventsQueued
JNIEXPORT jint JNICALL OS_NATIVE(XEventsQueued)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XEventsQueued\n")
	rc = (jint)XEventsQueued((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XEventsQueued\n")
	return rc;
}
#endif

#ifndef NO_XFillArc
JNIEXPORT void JNICALL OS_NATIVE(XFillArc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	NATIVE_ENTER(env, that, "XFillArc\n")
	XFillArc((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	NATIVE_EXIT(env, that, "XFillArc\n")
}
#endif

#ifndef NO_XFillPolygon
JNIEXPORT jint JNICALL OS_NATIVE(XFillPolygon)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jshortArray arg3, jint arg4, jint arg5, jint arg6)
{
	jshort *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XFillPolygon\n")
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jint)XFillPolygon((Display *)arg0, (Drawable)arg1, (GC)arg2, (XPoint *)lparg3, arg4, arg5, arg6);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "XFillPolygon\n")
	return rc;
}
#endif

#ifndef NO_XFillRectangle
JNIEXPORT void JNICALL OS_NATIVE(XFillRectangle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	NATIVE_ENTER(env, that, "XFillRectangle\n")
	XFillRectangle((Display *)arg0, (Drawable)arg1, (GC)arg2, arg3, arg4, arg5, arg6);
	NATIVE_EXIT(env, that, "XFillRectangle\n")
}
#endif

#ifndef NO_XFilterEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(XFilterEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XFilterEvent\n")
	rc = (jboolean)XFilterEvent((XEvent *)arg0, (Window)arg1);
	NATIVE_EXIT(env, that, "XFilterEvent\n")
	return rc;
}
#endif

#ifndef NO_XFlush
JNIEXPORT void JNICALL OS_NATIVE(XFlush)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XFlush\n")
	XFlush((Display *)arg0);
	NATIVE_EXIT(env, that, "XFlush\n")
}
#endif

#ifndef NO_XFontsOfFontSet
JNIEXPORT jint JNICALL OS_NATIVE(XFontsOfFontSet)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XFontsOfFontSet\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XFontsOfFontSet((XFontSet)arg0, (XFontStruct ***)lparg1, (char ***)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XFontsOfFontSet\n")
	return rc;
}
#endif

#ifndef NO_XFree
JNIEXPORT jint JNICALL OS_NATIVE(XFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XFree\n")
	rc = (jint)XFree((char *)arg0);
	NATIVE_EXIT(env, that, "XFree\n")
	return rc;
}
#endif

#ifndef NO_XFreeColors
JNIEXPORT jint JNICALL OS_NATIVE(XFreeColors)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3, jint arg4)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XFreeColors\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XFreeColors((Display *)arg0, arg1, (unsigned long *)lparg2, arg3, arg4);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XFreeColors\n")
	return rc;
}
#endif

#ifndef NO_XFreeCursor
JNIEXPORT void JNICALL OS_NATIVE(XFreeCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XFreeCursor\n")
	XFreeCursor((Display *)arg0, (Cursor)arg1);
	NATIVE_EXIT(env, that, "XFreeCursor\n")
}
#endif

#ifndef NO_XFreeFont
JNIEXPORT void JNICALL OS_NATIVE(XFreeFont)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XFreeFont\n")
	XFreeFont((Display *)arg0, (XFontStruct *)arg1);
	NATIVE_EXIT(env, that, "XFreeFont\n")
}
#endif

#ifndef NO_XFreeFontNames
JNIEXPORT void JNICALL OS_NATIVE(XFreeFontNames)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XFreeFontNames\n")
	XFreeFontNames((char **)arg0);
	NATIVE_EXIT(env, that, "XFreeFontNames\n")
}
#endif

#ifndef NO_XFreeGC
JNIEXPORT void JNICALL OS_NATIVE(XFreeGC)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XFreeGC\n")
	XFreeGC((Display *)arg0, (GC)arg1);
	NATIVE_EXIT(env, that, "XFreeGC\n")
}
#endif

#ifndef NO_XFreePixmap
JNIEXPORT void JNICALL OS_NATIVE(XFreePixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XFreePixmap\n")
	XFreePixmap((Display *)arg0, (Pixmap)arg1);
	NATIVE_EXIT(env, that, "XFreePixmap\n")
}
#endif

#ifndef NO_XFreeStringList
JNIEXPORT void JNICALL OS_NATIVE(XFreeStringList)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XFreeStringList\n")
	XFreeStringList((char **)arg0);
	NATIVE_EXIT(env, that, "XFreeStringList\n")
}
#endif

#ifndef NO_XGetGCValues
JNIEXPORT jint JNICALL OS_NATIVE(XGetGCValues)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jobject arg3)
{
	XGCValues _arg3, *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XGetGCValues\n")
	if (arg3) lparg3 = getXGCValuesFields(env, arg3, &_arg3);
	rc = (jint)XGetGCValues((Display *)arg0, (GC)arg1, arg2, lparg3);
	if (arg3) setXGCValuesFields(env, arg3, lparg3);
	NATIVE_EXIT(env, that, "XGetGCValues\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "XGetGeometry\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)XGetGeometry((Display *)arg0, (Drawable)arg1, (Window *)lparg2, (int *)lparg3, (int *)lparg4, (unsigned int *)lparg5, (unsigned int *)lparg6, (unsigned int *)lparg7, (unsigned int *)lparg8);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XGetGeometry\n")
	return rc;
}
#endif

#ifndef NO_XGetImage
JNIEXPORT jint JNICALL OS_NATIVE(XGetImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7)
{
	jint rc;
	NATIVE_ENTER(env, that, "XGetImage\n")
	rc = (jint)XGetImage((Display *)arg0, (Drawable)arg1, arg2, arg3, arg4, arg5, arg6, arg7);
	NATIVE_EXIT(env, that, "XGetImage\n")
	return rc;
}
#endif

#ifndef NO_XGetInputFocus
JNIEXPORT jint JNICALL OS_NATIVE(XGetInputFocus)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XGetInputFocus\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XGetInputFocus((Display *)arg0, (Window *)lparg1, (int *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XGetInputFocus\n")
	return rc;
}
#endif

#ifndef NO_XGetModifierMapping
JNIEXPORT jint JNICALL OS_NATIVE(XGetModifierMapping)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XGetModifierMapping\n")
	rc = (jint)XGetModifierMapping((Display *)arg0);
	NATIVE_EXIT(env, that, "XGetModifierMapping\n")
	return rc;
}
#endif

#ifndef NO_XGetWindowAttributes
JNIEXPORT jboolean JNICALL OS_NATIVE(XGetWindowAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	XWindowAttributes _arg2, *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "XGetWindowAttributes\n")
	if (arg2) lparg2 = getXWindowAttributesFields(env, arg2, &_arg2);
	rc = (jboolean)XGetWindowAttributes((Display *)arg0, arg1, lparg2);
	if (arg2) setXWindowAttributesFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "XGetWindowAttributes\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "XGetWindowProperty\n")
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	if (arg9) lparg9 = (*env)->GetIntArrayElements(env, arg9, NULL);
	if (arg10) lparg10 = (*env)->GetIntArrayElements(env, arg10, NULL);
	if (arg11) lparg11 = (*env)->GetIntArrayElements(env, arg11, NULL);
	rc = (jint)XGetWindowProperty((Display *)arg0, (Window)arg1, (Atom)arg2, arg3, arg4, (Bool)arg5, (Atom)arg6, (Atom *)lparg7, (int *)lparg8, (unsigned long *)lparg9, (unsigned long *)lparg10, (unsigned char **)lparg11);
	if (arg11) (*env)->ReleaseIntArrayElements(env, arg11, lparg11, 0);
	if (arg10) (*env)->ReleaseIntArrayElements(env, arg10, lparg10, 0);
	if (arg9) (*env)->ReleaseIntArrayElements(env, arg9, lparg9, 0);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	NATIVE_EXIT(env, that, "XGetWindowProperty\n")
	return rc;
}
#endif

#ifndef NO_XGrabKeyboard
JNIEXPORT jint JNICALL OS_NATIVE(XGrabKeyboard)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc;
	NATIVE_ENTER(env, that, "XGrabKeyboard\n")
	rc = (jint)XGrabKeyboard((Display *)arg0, arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "XGrabKeyboard\n")
	return rc;
}
#endif

#ifndef NO_XGrabPointer
JNIEXPORT jint JNICALL OS_NATIVE(XGrabPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jint rc;
	NATIVE_ENTER(env, that, "XGrabPointer\n")
	rc = (jint)XGrabPointer((Display *)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	NATIVE_EXIT(env, that, "XGrabPointer\n")
	return rc;
}
#endif

#ifndef NO_XInitThreads
JNIEXPORT jint JNICALL OS_NATIVE(XInitThreads)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "XInitThreads\n")
	rc = (jint)XInitThreads();
	NATIVE_EXIT(env, that, "XInitThreads\n")
	return rc;
}
#endif

#ifndef NO_XInternAtom
JNIEXPORT jint JNICALL OS_NATIVE(XInternAtom)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jboolean arg2)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XInternAtom\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)XInternAtom((Display *)arg0, (char *)lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XInternAtom\n")
	return rc;
}
#endif

#ifndef NO_XIntersectRegion
JNIEXPORT void JNICALL OS_NATIVE(XIntersectRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XIntersectRegion\n")
	XIntersectRegion((Region)arg0, (Region)arg1, (Region)arg2);
	NATIVE_EXIT(env, that, "XIntersectRegion\n")
}
#endif

#ifndef NO_XKeysymToKeycode
JNIEXPORT jint JNICALL OS_NATIVE(XKeysymToKeycode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XKeysymToKeycode\n")
	rc = (jint)XKeysymToKeycode((Display *)arg0, (KeySym)arg1);
	NATIVE_EXIT(env, that, "XKeysymToKeycode\n")
	return rc;
}
#endif

#ifndef NO_XKeysymToString
JNIEXPORT jint JNICALL OS_NATIVE(XKeysymToString)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XKeysymToString\n")
	rc = (jint)XKeysymToString(arg0);
	NATIVE_EXIT(env, that, "XKeysymToString\n")
	return rc;
}
#endif

#ifndef NO_XListFonts
JNIEXPORT jint JNICALL OS_NATIVE(XListFonts)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XListFonts\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)XListFonts((Display *)arg0, (char *)lparg1, arg2, (int *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XListFonts\n")
	return rc;
}
#endif

#ifndef NO_XListProperties
JNIEXPORT jint JNICALL OS_NATIVE(XListProperties)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XListProperties\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XListProperties((Display *)arg0, (Window)arg1, (int *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XListProperties\n")
	return rc;
}
#endif

#ifndef NO_XLocaleOfFontSet
JNIEXPORT jint JNICALL OS_NATIVE(XLocaleOfFontSet)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XLocaleOfFontSet\n")
	rc = (jint)XLocaleOfFontSet((XFontSet)arg0);
	NATIVE_EXIT(env, that, "XLocaleOfFontSet\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "XLookupString\n")
	if (arg0) lparg0 = getXKeyEventFields(env, arg0, &_arg0);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)XLookupString((XKeyEvent *)lparg0, (char *)lparg1, arg2, (KeySym *)lparg3, (XComposeStatus *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0) setXKeyEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "XLookupString\n")
	return rc;
}
#endif

#ifndef NO_XLowerWindow
JNIEXPORT jint JNICALL OS_NATIVE(XLowerWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XLowerWindow\n")
	rc = (jint)XLowerWindow((Display *)arg0, (Window)arg1);
	NATIVE_EXIT(env, that, "XLowerWindow\n")
	return rc;
}
#endif

#ifndef NO_XMapWindow
JNIEXPORT void JNICALL OS_NATIVE(XMapWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XMapWindow\n")
	XMapWindow((Display *)arg0, (Window)arg1);
	NATIVE_EXIT(env, that, "XMapWindow\n")
}
#endif

#ifndef NO_XMoveResizeWindow
JNIEXPORT void JNICALL OS_NATIVE(XMoveResizeWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	NATIVE_ENTER(env, that, "XMoveResizeWindow\n")
	XMoveResizeWindow((Display *)arg0, (Window)arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "XMoveResizeWindow\n")
}
#endif

#ifndef NO_XOpenDisplay
JNIEXPORT jint JNICALL OS_NATIVE(XOpenDisplay)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XOpenDisplay\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)XOpenDisplay((char *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "XOpenDisplay\n")
	return rc;
}
#endif

#ifndef NO_XPointInRegion
JNIEXPORT jboolean JNICALL OS_NATIVE(XPointInRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XPointInRegion\n")
	rc = (jboolean)XPointInRegion((Region)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "XPointInRegion\n")
	return rc;
}
#endif

#ifndef NO_XPolygonRegion
JNIEXPORT jint JNICALL OS_NATIVE(XPolygonRegion)
	(JNIEnv *env, jclass that, jshortArray arg0, jint arg1, jint arg2)
{
	jshort *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XPolygonRegion\n")
	if (arg0) lparg0 = (*env)->GetShortArrayElements(env, arg0, NULL);
	rc = (jint)XPolygonRegion((XPoint *)lparg0, arg1, arg2);
	if (arg0) (*env)->ReleaseShortArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "XPolygonRegion\n")
	return rc;
}
#endif

#ifndef NO_XPutImage
JNIEXPORT jint JNICALL OS_NATIVE(XPutImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9)
{
	jint rc;
	NATIVE_ENTER(env, that, "XPutImage\n")
	rc = (jint)XPutImage((Display *)arg0, (Drawable)arg1, (GC)arg2, (XImage *)arg3, arg4, arg5, arg6, arg7, arg8, arg9);
	NATIVE_EXIT(env, that, "XPutImage\n")
	return rc;
}
#endif

#ifndef NO_XQueryBestCursor
JNIEXPORT jint JNICALL OS_NATIVE(XQueryBestCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jintArray arg4, jintArray arg5)
{
	jint *lparg4=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XQueryBestCursor\n")
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)XQueryBestCursor((Display *)arg0, arg1, arg2, arg3, (int *)lparg4, (int *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "XQueryBestCursor\n")
	return rc;
}
#endif

#ifndef NO_XQueryColor
JNIEXPORT jint JNICALL OS_NATIVE(XQueryColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jobject arg2)
{
	XColor _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XQueryColor\n")
	if (arg2) lparg2 = getXColorFields(env, arg2, &_arg2);
	rc = (jint)XQueryColor((Display *)arg0, arg1, lparg2);
	if (arg2) setXColorFields(env, arg2, lparg2);
	NATIVE_EXIT(env, that, "XQueryColor\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "XQueryPointer\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	if (arg8) lparg8 = (*env)->GetIntArrayElements(env, arg8, NULL);
	rc = (jint)XQueryPointer((Display *)arg0, (Window)arg1, (Window *)lparg2, (Window *)lparg3, (int *)lparg4, (int *)lparg5, (int *)lparg6, (int *)lparg7, (unsigned int *)lparg8);
	if (arg8) (*env)->ReleaseIntArrayElements(env, arg8, lparg8, 0);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XQueryPointer\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "XQueryTree\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)XQueryTree((Display *)arg0, (Window)arg1, (Window *)lparg2, (Window *)lparg3, (Window **)lparg4, (unsigned int *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XQueryTree\n")
	return rc;
}
#endif

#ifndef NO_XRaiseWindow
JNIEXPORT jint JNICALL OS_NATIVE(XRaiseWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XRaiseWindow\n")
	rc = (jint)XRaiseWindow((Display *)arg0, (Window)arg1);
	NATIVE_EXIT(env, that, "XRaiseWindow\n")
	return rc;
}
#endif

#ifndef NO_XReconfigureWMWindow
JNIEXPORT jint JNICALL OS_NATIVE(XReconfigureWMWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	XWindowChanges _arg4, *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XReconfigureWMWindow\n")
	if (arg4) lparg4 = getXWindowChangesFields(env, arg4, &_arg4);
	rc = (jint)XReconfigureWMWindow((Display *)arg0, (Window)arg1, arg2, arg3, lparg4);
	if (arg4) setXWindowChangesFields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "XReconfigureWMWindow\n")
	return rc;
}
#endif

#ifndef NO_XRectInRegion
JNIEXPORT jint JNICALL OS_NATIVE(XRectInRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "XRectInRegion\n")
	rc = (jint)XRectInRegion((Region)arg0, arg1, arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "XRectInRegion\n")
	return rc;
}
#endif

#ifndef NO_XReparentWindow
JNIEXPORT jint JNICALL OS_NATIVE(XReparentWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "XReparentWindow\n")
	rc = (jint)XReparentWindow((Display *)arg0, (Window)arg1, (Window)arg2, arg3, arg4);
	NATIVE_EXIT(env, that, "XReparentWindow\n")
	return rc;
}
#endif

#ifndef NO_XRootWindowOfScreen
JNIEXPORT jint JNICALL OS_NATIVE(XRootWindowOfScreen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XRootWindowOfScreen\n")
	rc = (jint)XRootWindowOfScreen((Screen *)arg0);
	NATIVE_EXIT(env, that, "XRootWindowOfScreen\n")
	return rc;
}
#endif

#ifndef NO_XSelectInput
JNIEXPORT void JNICALL OS_NATIVE(XSelectInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XSelectInput\n")
	XSelectInput((Display *)arg0, (Window)arg1, arg2);
	NATIVE_EXIT(env, that, "XSelectInput\n")
}
#endif

#ifndef NO_XSendEvent
JNIEXPORT jint JNICALL OS_NATIVE(XSendEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "XSendEvent\n")
	rc = (jint)XSendEvent((Display *)arg0, (Window)arg1, (Bool)arg2, (long)arg3, (XEvent *)arg4);
	NATIVE_EXIT(env, that, "XSendEvent\n")
	return rc;
}
#endif

#ifndef NO_XSetBackground
JNIEXPORT void JNICALL OS_NATIVE(XSetBackground)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XSetBackground\n")
	XSetBackground((Display *)arg0, (GC)arg1, arg2);
	NATIVE_EXIT(env, that, "XSetBackground\n")
}
#endif

#ifndef NO_XSetClipMask
JNIEXPORT void JNICALL OS_NATIVE(XSetClipMask)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XSetClipMask\n")
	XSetClipMask((Display *)arg0, (GC)arg1, (Pixmap)arg2);
	NATIVE_EXIT(env, that, "XSetClipMask\n")
}
#endif

#ifndef NO_XSetClipRectangles
JNIEXPORT void JNICALL OS_NATIVE(XSetClipRectangles)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4, jint arg5, jint arg6)
{
	XRectangle _arg4, *lparg4=NULL;
	NATIVE_ENTER(env, that, "XSetClipRectangles\n")
	if (arg4) lparg4 = getXRectangleFields(env, arg4, &_arg4);
	XSetClipRectangles((Display *)arg0, (GC)arg1, arg2, arg3, (XRectangle *)lparg4, arg5, arg6);
	if (arg4) setXRectangleFields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "XSetClipRectangles\n")
}
#endif

#ifndef NO_XSetDashes
JNIEXPORT jint JNICALL OS_NATIVE(XSetDashes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4)
{
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XSetDashes\n")
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)XSetDashes((Display *)arg0, (GC)arg1, arg2, (char *)lparg3, arg4);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "XSetDashes\n")
	return rc;
}
#endif

#ifndef NO_XSetErrorHandler
JNIEXPORT jint JNICALL OS_NATIVE(XSetErrorHandler)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XSetErrorHandler\n")
	rc = (jint)XSetErrorHandler((XErrorHandler)arg0);
	NATIVE_EXIT(env, that, "XSetErrorHandler\n")
	return rc;
}
#endif

#ifndef NO_XSetFillStyle
JNIEXPORT void JNICALL OS_NATIVE(XSetFillStyle)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XSetFillStyle\n")
	XSetFillStyle((Display *)arg0, (GC)arg1, arg2);
	NATIVE_EXIT(env, that, "XSetFillStyle\n")
}
#endif

#ifndef NO_XSetForeground
JNIEXPORT void JNICALL OS_NATIVE(XSetForeground)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XSetForeground\n")
	XSetForeground((Display *)arg0, (GC)arg1, arg2);
	NATIVE_EXIT(env, that, "XSetForeground\n")
}
#endif

#ifndef NO_XSetFunction
JNIEXPORT void JNICALL OS_NATIVE(XSetFunction)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XSetFunction\n")
	XSetFunction((Display *)arg0, (GC)arg1, arg2);
	NATIVE_EXIT(env, that, "XSetFunction\n")
}
#endif

#ifndef NO_XSetGraphicsExposures
JNIEXPORT void JNICALL OS_NATIVE(XSetGraphicsExposures)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	NATIVE_ENTER(env, that, "XSetGraphicsExposures\n")
	XSetGraphicsExposures((Display *)arg0, (GC)arg1, (Bool)arg2);
	NATIVE_EXIT(env, that, "XSetGraphicsExposures\n")
}
#endif

#ifndef NO_XSetIOErrorHandler
JNIEXPORT jint JNICALL OS_NATIVE(XSetIOErrorHandler)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XSetIOErrorHandler\n")
	rc = (jint)XSetIOErrorHandler((XIOErrorHandler)arg0);
	NATIVE_EXIT(env, that, "XSetIOErrorHandler\n")
	return rc;
}
#endif

#ifndef NO_XSetInputFocus
JNIEXPORT jint JNICALL OS_NATIVE(XSetInputFocus)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "XSetInputFocus\n")
	rc = (jint)XSetInputFocus((Display *)arg0, (Window)arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "XSetInputFocus\n")
	return rc;
}
#endif

#ifndef NO_XSetLineAttributes
JNIEXPORT jint JNICALL OS_NATIVE(XSetLineAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	jint rc;
	NATIVE_ENTER(env, that, "XSetLineAttributes\n")
	rc = (jint)XSetLineAttributes((Display *)arg0, (GC)arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "XSetLineAttributes\n")
	return rc;
}
#endif

#ifndef NO_XSetRegion
JNIEXPORT void JNICALL OS_NATIVE(XSetRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XSetRegion\n")
	XSetRegion((Display *)arg0, (GC)arg1, (Region)arg2);
	NATIVE_EXIT(env, that, "XSetRegion\n")
}
#endif

#ifndef NO_XSetStipple
JNIEXPORT void JNICALL OS_NATIVE(XSetStipple)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XSetStipple\n")
	XSetStipple((Display *)arg0, (GC)arg1, (Pixmap)arg2);
	NATIVE_EXIT(env, that, "XSetStipple\n")
}
#endif

#ifndef NO_XSetSubwindowMode
JNIEXPORT void JNICALL OS_NATIVE(XSetSubwindowMode)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XSetSubwindowMode\n")
	XSetSubwindowMode((Display *)arg0, (GC)arg1, arg2);
	NATIVE_EXIT(env, that, "XSetSubwindowMode\n")
}
#endif

#ifndef NO_XShapeCombineMask
JNIEXPORT void JNICALL OS_NATIVE(XShapeCombineMask)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	NATIVE_ENTER(env, that, "XShapeCombineMask\n")
	XShapeCombineMask((Display *)arg0, (Window)arg1, arg2, arg3, arg4, (Pixmap)arg5, arg6);
	NATIVE_EXIT(env, that, "XShapeCombineMask\n")
}
#endif

#ifndef NO_XShapeCombineRegion
JNIEXPORT void JNICALL OS_NATIVE(XShapeCombineRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6)
{
	NATIVE_ENTER(env, that, "XShapeCombineRegion\n")
	XShapeCombineRegion((Display *)arg0, (Window)arg1, arg2, arg3, arg4, (Region)arg5, arg6);
	NATIVE_EXIT(env, that, "XShapeCombineRegion\n")
}
#endif

#ifndef NO_XSubtractRegion
JNIEXPORT void JNICALL OS_NATIVE(XSubtractRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XSubtractRegion\n")
	XSubtractRegion((Region)arg0, (Region)arg1, (Region)arg2);
	NATIVE_EXIT(env, that, "XSubtractRegion\n")
}
#endif

#ifndef NO_XSync
JNIEXPORT void JNICALL OS_NATIVE(XSync)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	NATIVE_ENTER(env, that, "XSync\n")
	XSync((Display *)arg0, (Bool)arg1);
	NATIVE_EXIT(env, that, "XSync\n")
}
#endif

#ifndef NO_XSynchronize
JNIEXPORT jint JNICALL OS_NATIVE(XSynchronize)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XSynchronize\n")
	rc = (jint)XSynchronize((Display *)arg0, (Bool)arg1);
	NATIVE_EXIT(env, that, "XSynchronize\n")
	return rc;
}
#endif

#ifndef NO_XUndefineCursor
JNIEXPORT void JNICALL OS_NATIVE(XUndefineCursor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XUndefineCursor\n")
	XUndefineCursor((Display *)arg0, (Window)arg1);
	NATIVE_EXIT(env, that, "XUndefineCursor\n")
}
#endif

#ifndef NO_XUngrabKeyboard
JNIEXPORT jint JNICALL OS_NATIVE(XUngrabKeyboard)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XUngrabKeyboard\n")
	rc = (jint)XUngrabKeyboard((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XUngrabKeyboard\n")
	return rc;
}
#endif

#ifndef NO_XUngrabPointer
JNIEXPORT jint JNICALL OS_NATIVE(XUngrabPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XUngrabPointer\n")
	rc = (jint)XUngrabPointer((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XUngrabPointer\n")
	return rc;
}
#endif

#ifndef NO_XUnionRectWithRegion
JNIEXPORT void JNICALL OS_NATIVE(XUnionRectWithRegion)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XRectangle _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "XUnionRectWithRegion\n")
	if (arg0) lparg0 = getXRectangleFields(env, arg0, &_arg0);
	XUnionRectWithRegion((XRectangle *)lparg0, (Region)arg1, (Region)arg2);
	if (arg0) setXRectangleFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "XUnionRectWithRegion\n")
}
#endif

#ifndef NO_XUnionRegion
JNIEXPORT void JNICALL OS_NATIVE(XUnionRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XUnionRegion\n")
	XUnionRegion((Region)arg0, (Region)arg1, (Region)arg2);
	NATIVE_EXIT(env, that, "XUnionRegion\n")
}
#endif

#ifndef NO_XUnmapWindow
JNIEXPORT void JNICALL OS_NATIVE(XUnmapWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XUnmapWindow\n")
	XUnmapWindow((Display *)arg0, (Window)arg1);
	NATIVE_EXIT(env, that, "XUnmapWindow\n")
}
#endif

#ifndef NO_XWarpPointer
JNIEXPORT jint JNICALL OS_NATIVE(XWarpPointer)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8)
{
	jint rc;
	NATIVE_ENTER(env, that, "XWarpPointer\n")
	rc = (jint)XWarpPointer((Display *)arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8);
	NATIVE_EXIT(env, that, "XWarpPointer\n")
	return rc;
}
#endif

#ifndef NO_XWhitePixel
JNIEXPORT jint JNICALL OS_NATIVE(XWhitePixel)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XWhitePixel\n")
	rc = (jint)XWhitePixel((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XWhitePixel\n")
	return rc;
}
#endif

#ifndef NO_XWithdrawWindow
JNIEXPORT void JNICALL OS_NATIVE(XWithdrawWindow)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XWithdrawWindow\n")
	XWithdrawWindow((Display *)arg0, (Window)arg1, arg2);
	NATIVE_EXIT(env, that, "XWithdrawWindow\n")
}
#endif

#ifndef NO_XineramaIsActive
JNIEXPORT jboolean JNICALL OS_NATIVE(XineramaIsActive)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XineramaIsActive\n")
	rc = (jboolean)XineramaIsActive((Display *)arg0);
	NATIVE_EXIT(env, that, "XineramaIsActive\n")
	return rc;
}
#endif

#ifndef NO_XineramaQueryScreens
JNIEXPORT jint JNICALL OS_NATIVE(XineramaQueryScreens)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XineramaQueryScreens\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)XineramaQueryScreens((Display *)arg0, lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XineramaQueryScreens\n")
	return rc;
}
#endif

#ifndef NO_XmAddWMProtocolCallback
JNIEXPORT void JNICALL OS_NATIVE(XmAddWMProtocolCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "XmAddWMProtocolCallback\n")
	XmAddWMProtocolCallback((Widget)arg0, (Atom)arg1, (XtCallbackProc)arg2, (XtPointer)arg3);
	NATIVE_EXIT(env, that, "XmAddWMProtocolCallback\n")
}
#endif

#ifndef NO_XmChangeColor
JNIEXPORT void JNICALL OS_NATIVE(XmChangeColor)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XmChangeColor\n")
	XmChangeColor((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmChangeColor\n")
}
#endif

#ifndef NO_XmClipboardCopy
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardCopy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jbyteArray arg4, jint arg5, jint arg6, jintArray arg7)
{
	jbyte *lparg3=NULL;
	jbyte *lparg4=NULL;
	jint *lparg7=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmClipboardCopy\n")
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	if (arg7) lparg7 = (*env)->GetIntArrayElements(env, arg7, NULL);
	rc = (jint)XmClipboardCopy((Display *)arg0, (Window)arg1, arg2, (char *)lparg3, (char *)lparg4, arg5, arg6, (void *)lparg7);
	if (arg7) (*env)->ReleaseIntArrayElements(env, arg7, lparg7, 0);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "XmClipboardCopy\n")
	return rc;
}
#endif

#ifndef NO_XmClipboardEndCopy
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardEndCopy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmClipboardEndCopy\n")
	rc = (jint)XmClipboardEndCopy((Display *)arg0, (Window)arg1, arg2);
	NATIVE_EXIT(env, that, "XmClipboardEndCopy\n")
	return rc;
}
#endif

#ifndef NO_XmClipboardEndRetrieve
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardEndRetrieve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmClipboardEndRetrieve\n")
	rc = (jint)XmClipboardEndRetrieve((Display *)arg0, (Window)arg1);
	NATIVE_EXIT(env, that, "XmClipboardEndRetrieve\n")
	return rc;
}
#endif

#ifndef NO_XmClipboardInquireCount
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardInquireCount)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jintArray arg3)
{
	jint *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmClipboardInquireCount\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)XmClipboardInquireCount((Display *)arg0, (Window)arg1, (int *)lparg2, (unsigned long *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XmClipboardInquireCount\n")
	return rc;
}
#endif

#ifndef NO_XmClipboardInquireFormat
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardInquireFormat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3, jint arg4, jintArray arg5)
{
	jbyte *lparg3=NULL;
	jint *lparg5=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmClipboardInquireFormat\n")
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)XmClipboardInquireFormat((Display *)arg0, (Window)arg1, arg2, (char *)lparg3, arg4, (unsigned long *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "XmClipboardInquireFormat\n")
	return rc;
}
#endif

#ifndef NO_XmClipboardInquireLength
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardInquireLength)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2, jintArray arg3)
{
	jbyte *lparg2=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmClipboardInquireLength\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)XmClipboardInquireLength((Display *)arg0, (Window)arg1, (char *)lparg2, (unsigned long *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XmClipboardInquireLength\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "XmClipboardRetrieve\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	rc = (jint)XmClipboardRetrieve((Display *)arg0, (Window)arg1, (char *)lparg2, (char *)lparg3, arg4, (unsigned long *)lparg5, (long *)lparg6);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XmClipboardRetrieve\n")
	return rc;
}
#endif

#ifndef NO_XmClipboardStartCopy
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardStartCopy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jintArray arg6)
{
	jint *lparg6=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmClipboardStartCopy\n")
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	rc = (jint)XmClipboardStartCopy((Display *)arg0, (Window)arg1, (XmString)arg2, arg3, (Widget)arg4, (XmCutPasteProc)arg5, (long *)lparg6);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	NATIVE_EXIT(env, that, "XmClipboardStartCopy\n")
	return rc;
}
#endif

#ifndef NO_XmClipboardStartRetrieve
JNIEXPORT jint JNICALL OS_NATIVE(XmClipboardStartRetrieve)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmClipboardStartRetrieve\n")
	rc = (jint)XmClipboardStartRetrieve((Display *)arg0, (Window)arg1, arg2);
	NATIVE_EXIT(env, that, "XmClipboardStartRetrieve\n")
	return rc;
}
#endif

#ifndef NO_XmComboBoxAddItem
JNIEXPORT void JNICALL OS_NATIVE(XmComboBoxAddItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jboolean arg3)
{
	NATIVE_ENTER(env, that, "XmComboBoxAddItem\n")
	XmComboBoxAddItem((Widget)arg0, (XmString)arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "XmComboBoxAddItem\n")
}
#endif

#ifndef NO_XmComboBoxDeletePos
JNIEXPORT void JNICALL OS_NATIVE(XmComboBoxDeletePos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XmComboBoxDeletePos\n")
	XmComboBoxDeletePos((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmComboBoxDeletePos\n")
}
#endif

#ifndef NO_XmComboBoxSelectItem
JNIEXPORT void JNICALL OS_NATIVE(XmComboBoxSelectItem)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XmComboBoxSelectItem\n")
	XmComboBoxSelectItem((Widget)arg0, (XmString)arg1);
	NATIVE_EXIT(env, that, "XmComboBoxSelectItem\n")
}
#endif

#ifndef NO_XmCreateArrowButton
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateArrowButton)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateArrowButton\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateArrowButton((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateArrowButton\n")
	return rc;
}
#endif

#ifndef NO_XmCreateCascadeButtonGadget
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateCascadeButtonGadget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateCascadeButtonGadget\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateCascadeButtonGadget((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateCascadeButtonGadget\n")
	return rc;
}
#endif

#ifndef NO_XmCreateComboBox
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateComboBox)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateComboBox\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateComboBox((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateComboBox\n")
	return rc;
}
#endif

#ifndef NO_XmCreateDialogShell
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateDialogShell)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateDialogShell\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateDialogShell((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateDialogShell\n")
	return rc;
}
#endif

#ifndef NO_XmCreateDrawingArea
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateDrawingArea)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateDrawingArea\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateDrawingArea((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateDrawingArea\n")
	return rc;
}
#endif

#ifndef NO_XmCreateDrawnButton
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateDrawnButton)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateDrawnButton\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateDrawnButton((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateDrawnButton\n")
	return rc;
}
#endif

#ifndef NO_XmCreateErrorDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateErrorDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateErrorDialog\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateErrorDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateErrorDialog\n")
	return rc;
}
#endif

#ifndef NO_XmCreateFileSelectionDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateFileSelectionDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateFileSelectionDialog\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateFileSelectionDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateFileSelectionDialog\n")
	return rc;
}
#endif

#ifndef NO_XmCreateForm
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateForm)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateForm\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateForm((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateForm\n")
	return rc;
}
#endif

#ifndef NO_XmCreateFrame
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateFrame)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateFrame\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateFrame((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateFrame\n")
	return rc;
}
#endif

#ifndef NO_XmCreateInformationDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateInformationDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateInformationDialog\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateInformationDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateInformationDialog\n")
	return rc;
}
#endif

#ifndef NO_XmCreateLabel
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateLabel)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateLabel\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateLabel((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateLabel\n")
	return rc;
}
#endif

#ifndef NO_XmCreateList
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateList)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateList\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateList((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateList\n")
	return rc;
}
#endif

#ifndef NO_XmCreateMainWindow
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateMainWindow)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateMainWindow\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateMainWindow((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateMainWindow\n")
	return rc;
}
#endif

#ifndef NO_XmCreateMenuBar
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateMenuBar)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateMenuBar\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateMenuBar((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateMenuBar\n")
	return rc;
}
#endif

#ifndef NO_XmCreateMessageDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateMessageDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateMessageDialog\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateMessageDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateMessageDialog\n")
	return rc;
}
#endif

#ifndef NO_XmCreatePopupMenu
JNIEXPORT jint JNICALL OS_NATIVE(XmCreatePopupMenu)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreatePopupMenu\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreatePopupMenu((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreatePopupMenu\n")
	return rc;
}
#endif

#ifndef NO_XmCreatePulldownMenu
JNIEXPORT jint JNICALL OS_NATIVE(XmCreatePulldownMenu)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreatePulldownMenu\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreatePulldownMenu((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreatePulldownMenu\n")
	return rc;
}
#endif

#ifndef NO_XmCreatePushButton
JNIEXPORT jint JNICALL OS_NATIVE(XmCreatePushButton)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreatePushButton\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreatePushButton((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreatePushButton\n")
	return rc;
}
#endif

#ifndef NO_XmCreatePushButtonGadget
JNIEXPORT jint JNICALL OS_NATIVE(XmCreatePushButtonGadget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreatePushButtonGadget\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreatePushButtonGadget((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreatePushButtonGadget\n")
	return rc;
}
#endif

#ifndef NO_XmCreateQuestionDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateQuestionDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateQuestionDialog\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateQuestionDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateQuestionDialog\n")
	return rc;
}
#endif

#ifndef NO_XmCreateScale
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateScale)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateScale\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateScale((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateScale\n")
	return rc;
}
#endif

#ifndef NO_XmCreateScrollBar
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateScrollBar)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateScrollBar\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateScrollBar((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateScrollBar\n")
	return rc;
}
#endif

#ifndef NO_XmCreateScrolledList
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateScrolledList)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateScrolledList\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateScrolledList((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateScrolledList\n")
	return rc;
}
#endif

#ifndef NO_XmCreateScrolledText
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateScrolledText)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateScrolledText\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateScrolledText((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateScrolledText\n")
	return rc;
}
#endif

#ifndef NO_XmCreateSeparator
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateSeparator)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateSeparator\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateSeparator((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateSeparator\n")
	return rc;
}
#endif

#ifndef NO_XmCreateSeparatorGadget
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateSeparatorGadget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateSeparatorGadget\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateSeparatorGadget((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateSeparatorGadget\n")
	return rc;
}
#endif

#ifndef NO_XmCreateTextField
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateTextField)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateTextField\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateTextField((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateTextField\n")
	return rc;
}
#endif

#ifndef NO_XmCreateToggleButton
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateToggleButton)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateToggleButton\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateToggleButton((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateToggleButton\n")
	return rc;
}
#endif

#ifndef NO_XmCreateToggleButtonGadget
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateToggleButtonGadget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateToggleButtonGadget\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateToggleButtonGadget((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateToggleButtonGadget\n")
	return rc;
}
#endif

#ifndef NO_XmCreateWarningDialog
JNIEXPORT jint JNICALL OS_NATIVE(XmCreateWarningDialog)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmCreateWarningDialog\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmCreateWarningDialog((Widget)arg0, (String)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmCreateWarningDialog\n")
	return rc;
}
#endif

#ifndef NO_XmDestroyPixmap
JNIEXPORT jboolean JNICALL OS_NATIVE(XmDestroyPixmap)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XmDestroyPixmap\n")
	rc = (jboolean)XmDestroyPixmap((Screen *)arg0, (Pixmap)arg1);
	NATIVE_EXIT(env, that, "XmDestroyPixmap\n")
	return rc;
}
#endif

#ifndef NO_XmDragCancel
JNIEXPORT void JNICALL OS_NATIVE(XmDragCancel)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmDragCancel\n")
	XmDragCancel((Widget)arg0);
	NATIVE_EXIT(env, that, "XmDragCancel\n")
}
#endif

#ifndef NO_XmDragStart
JNIEXPORT jint JNICALL OS_NATIVE(XmDragStart)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jintArray arg2, jint arg3)
{
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmDragStart\n")
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmDragStart((Widget)arg0, (XEvent *)arg1, (ArgList)lparg2, (Cardinal)arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XmDragStart\n")
	return rc;
}
#endif

#ifndef NO_XmDropSiteRegister
JNIEXPORT void JNICALL OS_NATIVE(XmDropSiteRegister)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "XmDropSiteRegister\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	XmDropSiteRegister((Widget)arg0, (ArgList)lparg1, (Cardinal)arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmDropSiteRegister\n")
}
#endif

#ifndef NO_XmDropSiteUnregister
JNIEXPORT void JNICALL OS_NATIVE(XmDropSiteUnregister)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmDropSiteUnregister\n")
	XmDropSiteUnregister((Widget)arg0);
	NATIVE_EXIT(env, that, "XmDropSiteUnregister\n")
}
#endif

#ifndef NO_XmDropSiteUpdate
JNIEXPORT void JNICALL OS_NATIVE(XmDropSiteUpdate)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "XmDropSiteUpdate\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	XmDropSiteUpdate((Widget)arg0, (ArgList)lparg1, (Cardinal)arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmDropSiteUpdate\n")
}
#endif

#ifndef NO_XmDropTransferAdd
JNIEXPORT void JNICALL OS_NATIVE(XmDropTransferAdd)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "XmDropTransferAdd\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	XmDropTransferAdd((Widget)arg0, (XmDropTransferEntryRec *)lparg1, (Cardinal)arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmDropTransferAdd\n")
}
#endif

#ifndef NO_XmDropTransferStart
JNIEXPORT jint JNICALL OS_NATIVE(XmDropTransferStart)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmDropTransferStart\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)XmDropTransferStart((Widget)arg0, (ArgList)lparg1, (Cardinal)arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmDropTransferStart\n")
	return rc;
}
#endif

#ifndef NO_XmFileSelectionBoxGetChild
JNIEXPORT jint JNICALL OS_NATIVE(XmFileSelectionBoxGetChild)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmFileSelectionBoxGetChild\n")
	rc = (jint)XmFileSelectionBoxGetChild((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmFileSelectionBoxGetChild\n")
	return rc;
}
#endif

#ifndef NO_XmFontListAppendEntry
JNIEXPORT jint JNICALL OS_NATIVE(XmFontListAppendEntry)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmFontListAppendEntry\n")
	rc = (jint)XmFontListAppendEntry((XmFontList)arg0, (XmFontListEntry)arg1);
	NATIVE_EXIT(env, that, "XmFontListAppendEntry\n")
	return rc;
}
#endif

#ifndef NO_XmFontListCopy
JNIEXPORT jint JNICALL OS_NATIVE(XmFontListCopy)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmFontListCopy\n")
	rc = (jint)XmFontListCopy((XmFontList)arg0);
	NATIVE_EXIT(env, that, "XmFontListCopy\n")
	return rc;
}
#endif

#ifndef NO_XmFontListEntryFree
JNIEXPORT void JNICALL OS_NATIVE(XmFontListEntryFree)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	NATIVE_ENTER(env, that, "XmFontListEntryFree\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	XmFontListEntryFree((XmFontListEntry *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "XmFontListEntryFree\n")
}
#endif

#ifndef NO_XmFontListEntryGetFont
JNIEXPORT jint JNICALL OS_NATIVE(XmFontListEntryGetFont)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmFontListEntryGetFont\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)XmFontListEntryGetFont((XmFontListEntry)arg0, (XmFontType *)lparg1);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmFontListEntryGetFont\n")
	return rc;
}
#endif

#ifndef NO_XmFontListEntryLoad
JNIEXPORT jint JNICALL OS_NATIVE(XmFontListEntryLoad)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg1=NULL;
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmFontListEntryLoad\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)XmFontListEntryLoad((Display *)arg0, (char *)lparg1, arg2, (char *)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmFontListEntryLoad\n")
	return rc;
}
#endif

#ifndef NO_XmFontListFree
JNIEXPORT void JNICALL OS_NATIVE(XmFontListFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmFontListFree\n")
	XmFontListFree((XmFontList)arg0);
	NATIVE_EXIT(env, that, "XmFontListFree\n")
}
#endif

#ifndef NO_XmFontListFreeFontContext
JNIEXPORT void JNICALL OS_NATIVE(XmFontListFreeFontContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmFontListFreeFontContext\n")
	XmFontListFreeFontContext((XmFontContext)arg0);
	NATIVE_EXIT(env, that, "XmFontListFreeFontContext\n")
}
#endif

#ifndef NO_XmFontListInitFontContext
JNIEXPORT jboolean JNICALL OS_NATIVE(XmFontListInitFontContext)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1)
{
	jint *lparg0=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "XmFontListInitFontContext\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jboolean)XmFontListInitFontContext((XmFontContext *)lparg0, (XmFontList)arg1);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "XmFontListInitFontContext\n")
	return rc;
}
#endif

#ifndef NO_XmFontListNextEntry
JNIEXPORT jint JNICALL OS_NATIVE(XmFontListNextEntry)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmFontListNextEntry\n")
	rc = (jint)XmFontListNextEntry((XmFontContext)arg0);
	NATIVE_EXIT(env, that, "XmFontListNextEntry\n")
	return rc;
}
#endif

#ifndef NO_XmGetAtomName
JNIEXPORT jint JNICALL OS_NATIVE(XmGetAtomName)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmGetAtomName\n")
	rc = (jint)XmGetAtomName((Display *)arg0, (Atom)arg1);
	NATIVE_EXIT(env, that, "XmGetAtomName\n")
	return rc;
}
#endif

#ifndef NO_XmGetDragContext
JNIEXPORT jint JNICALL OS_NATIVE(XmGetDragContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmGetDragContext\n")
	rc = (jint)XmGetDragContext((Widget)arg0, (Time)arg1);
	NATIVE_EXIT(env, that, "XmGetDragContext\n")
	return rc;
}
#endif

#ifndef NO_XmGetFocusWidget
JNIEXPORT jint JNICALL OS_NATIVE(XmGetFocusWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmGetFocusWidget\n")
	rc = (jint)XmGetFocusWidget((Widget)arg0);
	NATIVE_EXIT(env, that, "XmGetFocusWidget\n")
	return rc;
}
#endif

#ifndef NO_XmGetPixmap
JNIEXPORT jint JNICALL OS_NATIVE(XmGetPixmap)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmGetPixmap\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)XmGetPixmap((Screen *)arg0, (char *)lparg1, (Pixel)arg2, (Pixel)arg3);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmGetPixmap\n")
	return rc;
}
#endif

#ifndef NO_XmGetPixmapByDepth
JNIEXPORT jint JNICALL OS_NATIVE(XmGetPixmapByDepth)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jint arg4)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmGetPixmapByDepth\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)XmGetPixmapByDepth((Screen *)arg0, (char *)lparg1, arg2, arg3, arg4);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmGetPixmapByDepth\n")
	return rc;
}
#endif

#ifndef NO_XmGetXmDisplay
JNIEXPORT jint JNICALL OS_NATIVE(XmGetXmDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmGetXmDisplay\n")
	rc = (jint)XmGetXmDisplay((Display *)arg0);
	NATIVE_EXIT(env, that, "XmGetXmDisplay\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "XmImMbLookupString\n")
	if (arg1) lparg1 = getXKeyEventFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	if (arg5) lparg5 = (*env)->GetIntArrayElements(env, arg5, NULL);
	rc = (jint)XmImMbLookupString((Widget)arg0, (XKeyPressedEvent *)lparg1, (char *)lparg2, arg3, (KeySym *)lparg4, (int *)lparg5);
	if (arg5) (*env)->ReleaseIntArrayElements(env, arg5, lparg5, 0);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) setXKeyEventFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "XmImMbLookupString\n")
	return rc;
}
#endif

#ifndef NO_XmImRegister
JNIEXPORT void JNICALL OS_NATIVE(XmImRegister)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XmImRegister\n")
	XmImRegister((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmImRegister\n")
}
#endif

#ifndef NO_XmImSetFocusValues
JNIEXPORT void JNICALL OS_NATIVE(XmImSetFocusValues)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "XmImSetFocusValues\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	XmImSetFocusValues((Widget)arg0, (ArgList)lparg1, arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmImSetFocusValues\n")
}
#endif

#ifndef NO_XmImSetValues
JNIEXPORT void JNICALL OS_NATIVE(XmImSetValues)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "XmImSetValues\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	XmImSetValues((Widget)arg0, (ArgList)lparg1, arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmImSetValues\n")
}
#endif

#ifndef NO_XmImUnregister
JNIEXPORT void JNICALL OS_NATIVE(XmImUnregister)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmImUnregister\n")
	XmImUnregister((Widget)arg0);
	NATIVE_EXIT(env, that, "XmImUnregister\n")
}
#endif

#ifndef NO_XmImUnsetFocus
JNIEXPORT void JNICALL OS_NATIVE(XmImUnsetFocus)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmImUnsetFocus\n")
	XmImUnsetFocus((Widget)arg0);
	NATIVE_EXIT(env, that, "XmImUnsetFocus\n")
}
#endif

#ifndef NO_XmInternAtom
JNIEXPORT jint JNICALL OS_NATIVE(XmInternAtom)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jboolean arg2)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmInternAtom\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)XmInternAtom((Display *)arg0, (String)lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmInternAtom\n")
	return rc;
}
#endif

#ifndef NO_XmListAddItemUnselected
JNIEXPORT void JNICALL OS_NATIVE(XmListAddItemUnselected)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XmListAddItemUnselected\n")
	XmListAddItemUnselected((Widget)arg0, (XmString)arg1, arg2);
	NATIVE_EXIT(env, that, "XmListAddItemUnselected\n")
}
#endif

#ifndef NO_XmListDeleteAllItems
JNIEXPORT void JNICALL OS_NATIVE(XmListDeleteAllItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmListDeleteAllItems\n")
	XmListDeleteAllItems((Widget)arg0);
	NATIVE_EXIT(env, that, "XmListDeleteAllItems\n")
}
#endif

#ifndef NO_XmListDeleteItemsPos
JNIEXPORT void JNICALL OS_NATIVE(XmListDeleteItemsPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XmListDeleteItemsPos\n")
	XmListDeleteItemsPos((Widget)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "XmListDeleteItemsPos\n")
}
#endif

#ifndef NO_XmListDeletePos
JNIEXPORT void JNICALL OS_NATIVE(XmListDeletePos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XmListDeletePos\n")
	XmListDeletePos((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmListDeletePos\n")
}
#endif

#ifndef NO_XmListDeletePositions
JNIEXPORT void JNICALL OS_NATIVE(XmListDeletePositions)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "XmListDeletePositions\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	XmListDeletePositions((Widget)arg0, (int *)lparg1, arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmListDeletePositions\n")
}
#endif

#ifndef NO_XmListDeselectAllItems
JNIEXPORT void JNICALL OS_NATIVE(XmListDeselectAllItems)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmListDeselectAllItems\n")
	XmListDeselectAllItems((Widget)arg0);
	NATIVE_EXIT(env, that, "XmListDeselectAllItems\n")
}
#endif

#ifndef NO_XmListDeselectPos
JNIEXPORT void JNICALL OS_NATIVE(XmListDeselectPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XmListDeselectPos\n")
	XmListDeselectPos((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmListDeselectPos\n")
}
#endif

#ifndef NO_XmListGetKbdItemPos
JNIEXPORT jint JNICALL OS_NATIVE(XmListGetKbdItemPos)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmListGetKbdItemPos\n")
	rc = (jint)XmListGetKbdItemPos((Widget)arg0);
	NATIVE_EXIT(env, that, "XmListGetKbdItemPos\n")
	return rc;
}
#endif

#ifndef NO_XmListGetSelectedPos
JNIEXPORT jboolean JNICALL OS_NATIVE(XmListGetSelectedPos)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "XmListGetSelectedPos\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)XmListGetSelectedPos((Widget)arg0, (int **)lparg1, (int *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmListGetSelectedPos\n")
	return rc;
}
#endif

#ifndef NO_XmListItemPos
JNIEXPORT jint JNICALL OS_NATIVE(XmListItemPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmListItemPos\n")
	rc = (jint)XmListItemPos((Widget)arg0, (XmString)arg1);
	NATIVE_EXIT(env, that, "XmListItemPos\n")
	return rc;
}
#endif

#ifndef NO_XmListPosSelected
JNIEXPORT jboolean JNICALL OS_NATIVE(XmListPosSelected)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XmListPosSelected\n")
	rc = (jboolean)XmListPosSelected((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmListPosSelected\n")
	return rc;
}
#endif

#ifndef NO_XmListReplaceItemsPosUnselected
JNIEXPORT void JNICALL OS_NATIVE(XmListReplaceItemsPosUnselected)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "XmListReplaceItemsPosUnselected\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	XmListReplaceItemsPosUnselected((Widget)arg0, (XmString *)lparg1, arg2, arg3);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmListReplaceItemsPosUnselected\n")
}
#endif

#ifndef NO_XmListSelectPos
JNIEXPORT void JNICALL OS_NATIVE(XmListSelectPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2)
{
	NATIVE_ENTER(env, that, "XmListSelectPos\n")
	XmListSelectPos((Widget)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "XmListSelectPos\n")
}
#endif

#ifndef NO_XmListSetKbdItemPos
JNIEXPORT jboolean JNICALL OS_NATIVE(XmListSetKbdItemPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XmListSetKbdItemPos\n")
	rc = (jboolean)XmListSetKbdItemPos((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmListSetKbdItemPos\n")
	return rc;
}
#endif

#ifndef NO_XmListSetPos
JNIEXPORT void JNICALL OS_NATIVE(XmListSetPos)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XmListSetPos\n")
	XmListSetPos((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmListSetPos\n")
}
#endif

#ifndef NO_XmListUpdateSelectedList
JNIEXPORT void JNICALL OS_NATIVE(XmListUpdateSelectedList)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmListUpdateSelectedList\n")
	XmListUpdateSelectedList((Widget)arg0);
	NATIVE_EXIT(env, that, "XmListUpdateSelectedList\n")
}
#endif

#ifndef NO_XmMainWindowSetAreas
JNIEXPORT void JNICALL OS_NATIVE(XmMainWindowSetAreas)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	NATIVE_ENTER(env, that, "XmMainWindowSetAreas\n")
	XmMainWindowSetAreas((Widget)arg0, (Widget)arg1, (Widget)arg2, (Widget)arg3, (Widget)arg4, (Widget)arg5);
	NATIVE_EXIT(env, that, "XmMainWindowSetAreas\n")
}
#endif

#ifndef NO_XmMessageBoxGetChild
JNIEXPORT jint JNICALL OS_NATIVE(XmMessageBoxGetChild)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmMessageBoxGetChild\n")
	rc = (jint)XmMessageBoxGetChild((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmMessageBoxGetChild\n")
	return rc;
}
#endif

#ifndef NO_XmParseMappingCreate
JNIEXPORT jint JNICALL OS_NATIVE(XmParseMappingCreate)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmParseMappingCreate\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)XmParseMappingCreate((ArgList)lparg0, arg1);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "XmParseMappingCreate\n")
	return rc;
}
#endif

#ifndef NO_XmParseMappingFree
JNIEXPORT void JNICALL OS_NATIVE(XmParseMappingFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmParseMappingFree\n")
	XmParseMappingFree((XmParseMapping)arg0);
	NATIVE_EXIT(env, that, "XmParseMappingFree\n")
}
#endif

#ifndef NO_XmProcessTraversal
JNIEXPORT jboolean JNICALL OS_NATIVE(XmProcessTraversal)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XmProcessTraversal\n")
	rc = (jboolean)XmProcessTraversal((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmProcessTraversal\n")
	return rc;
}
#endif

#ifndef NO_XmRenderTableAddRenditions
JNIEXPORT jint JNICALL OS_NATIVE(XmRenderTableAddRenditions)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmRenderTableAddRenditions\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)XmRenderTableAddRenditions((XmRenderTable)arg0, (XmRendition *)lparg1, arg2, arg3);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmRenderTableAddRenditions\n")
	return rc;
}
#endif

#ifndef NO_XmRenderTableFree
JNIEXPORT void JNICALL OS_NATIVE(XmRenderTableFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmRenderTableFree\n")
	XmRenderTableFree((XmRenderTable)arg0);
	NATIVE_EXIT(env, that, "XmRenderTableFree\n")
}
#endif

#ifndef NO_XmRenditionCreate
JNIEXPORT jint JNICALL OS_NATIVE(XmRenditionCreate)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2, jint arg3)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmRenditionCreate\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XmRenditionCreate((Widget)arg0, (XmStringTag)lparg1, (ArgList)lparg2, arg3);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmRenditionCreate\n")
	return rc;
}
#endif

#ifndef NO_XmRenditionFree
JNIEXPORT void JNICALL OS_NATIVE(XmRenditionFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmRenditionFree\n")
	XmRenditionFree((XmRendition)arg0);
	NATIVE_EXIT(env, that, "XmRenditionFree\n")
}
#endif

#ifndef NO_XmStringCompare
JNIEXPORT jboolean JNICALL OS_NATIVE(XmStringCompare)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XmStringCompare\n")
	rc = (jboolean)XmStringCompare((XmString)arg0, (XmString)arg1);
	NATIVE_EXIT(env, that, "XmStringCompare\n")
	return rc;
}
#endif

#ifndef NO_XmStringComponentCreate
JNIEXPORT jint JNICALL OS_NATIVE(XmStringComponentCreate)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmStringComponentCreate\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	rc = (jint)XmStringComponentCreate(arg0, arg1, (XtPointer)lparg2);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XmStringComponentCreate\n")
	return rc;
}
#endif

#ifndef NO_XmStringConcat
JNIEXPORT jint JNICALL OS_NATIVE(XmStringConcat)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmStringConcat\n")
	rc = (jint)XmStringConcat((XmString)arg0, (XmString)arg1);
	NATIVE_EXIT(env, that, "XmStringConcat\n")
	return rc;
}
#endif

#ifndef NO_XmStringCreate
JNIEXPORT jint JNICALL OS_NATIVE(XmStringCreate)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmStringCreate\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)XmStringCreate((char *)lparg0, (char *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "XmStringCreate\n")
	return rc;
}
#endif

#ifndef NO_XmStringCreateLocalized
JNIEXPORT jint JNICALL OS_NATIVE(XmStringCreateLocalized)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmStringCreateLocalized\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)XmStringCreateLocalized((char *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "XmStringCreateLocalized\n")
	return rc;
}
#endif

#ifndef NO_XmStringDraw
JNIEXPORT void JNICALL OS_NATIVE(XmStringDraw)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jobject arg10)
{
	XRectangle _arg10, *lparg10=NULL;
	NATIVE_ENTER(env, that, "XmStringDraw\n")
	if (arg10) lparg10 = getXRectangleFields(env, arg10, &_arg10);
	XmStringDraw((Display *)arg0, (Window)arg1, (XmFontList)arg2, (XmString)arg3, (GC)arg4, arg5, arg6, arg7, arg8, arg9, lparg10);
	if (arg10) setXRectangleFields(env, arg10, lparg10);
	NATIVE_EXIT(env, that, "XmStringDraw\n")
}
#endif

#ifndef NO_XmStringDrawImage
JNIEXPORT void JNICALL OS_NATIVE(XmStringDrawImage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jobject arg10)
{
	XRectangle _arg10, *lparg10=NULL;
	NATIVE_ENTER(env, that, "XmStringDrawImage\n")
	if (arg10) lparg10 = getXRectangleFields(env, arg10, &_arg10);
	XmStringDrawImage((Display *)arg0, (Window)arg1, (XmFontList)arg2, (XmString)arg3, (GC)arg4, arg5, arg6, arg7, arg8, arg9, lparg10);
	if (arg10) setXRectangleFields(env, arg10, lparg10);
	NATIVE_EXIT(env, that, "XmStringDrawImage\n")
}
#endif

#ifndef NO_XmStringDrawUnderline
JNIEXPORT void JNICALL OS_NATIVE(XmStringDrawUnderline)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5, jint arg6, jint arg7, jint arg8, jint arg9, jobject arg10, jint arg11)
{
	XRectangle _arg10, *lparg10=NULL;
	NATIVE_ENTER(env, that, "XmStringDrawUnderline\n")
	if (arg10) lparg10 = getXRectangleFields(env, arg10, &_arg10);
	XmStringDrawUnderline((Display *)arg0, (Window)arg1, (XmFontList)arg2, (XmString)arg3, (GC)arg4, arg5, arg6, arg7, arg8, arg9, lparg10, (XmString)arg11);
	if (arg10) setXRectangleFields(env, arg10, lparg10);
	NATIVE_EXIT(env, that, "XmStringDrawUnderline\n")
}
#endif

#ifndef NO_XmStringEmpty
JNIEXPORT jboolean JNICALL OS_NATIVE(XmStringEmpty)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XmStringEmpty\n")
	rc = (jboolean)XmStringEmpty((XmString)arg0);
	NATIVE_EXIT(env, that, "XmStringEmpty\n")
	return rc;
}
#endif

#ifndef NO_XmStringFree
JNIEXPORT void JNICALL OS_NATIVE(XmStringFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmStringFree\n")
	XmStringFree((XmString)arg0);
	NATIVE_EXIT(env, that, "XmStringFree\n")
}
#endif

#ifndef NO_XmStringGenerate
JNIEXPORT jint JNICALL OS_NATIVE(XmStringGenerate)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmStringGenerate\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)XmStringGenerate((XtPointer)lparg0, (XmStringTag)lparg1, arg2, (XmStringTag)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "XmStringGenerate\n")
	return rc;
}
#endif

#ifndef NO_XmStringHeight
JNIEXPORT jint JNICALL OS_NATIVE(XmStringHeight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmStringHeight\n")
	rc = (jint)XmStringHeight((XmFontList)arg0, (XmString)arg1);
	NATIVE_EXIT(env, that, "XmStringHeight\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "XmStringParseText\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)XmStringParseText((XtPointer)lparg0, (XtPointer *)arg1, (XmStringTag)lparg2, arg3, (XmParseTable)lparg4, arg5, (XtPointer)arg6);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "XmStringParseText\n")
	return rc;
}
#endif

#ifndef NO_XmStringUnparse
JNIEXPORT jint JNICALL OS_NATIVE(XmStringUnparse)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jint arg3, jintArray arg4, jint arg5, jint arg6)
{
	jbyte *lparg1=NULL;
	jint *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmStringUnparse\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)XmStringUnparse((XmString)arg0, (XmStringTag)lparg1, arg2, arg3, (XmParseTable)lparg4, arg5, arg6);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmStringUnparse\n")
	return rc;
}
#endif

#ifndef NO_XmStringWidth
JNIEXPORT jint JNICALL OS_NATIVE(XmStringWidth)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmStringWidth\n")
	rc = (jint)XmStringWidth((XmFontList)arg0, (XmString)arg1);
	NATIVE_EXIT(env, that, "XmStringWidth\n")
	return rc;
}
#endif

#ifndef NO_XmTabCreate
JNIEXPORT jint JNICALL OS_NATIVE(XmTabCreate)
	(JNIEnv *env, jclass that, jint arg0, jbyte arg1, jbyte arg2, jbyte arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmTabCreate\n")
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jint)XmTabCreate(arg0, arg1, arg2, arg3, (char *)lparg4);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "XmTabCreate\n")
	return rc;
}
#endif

#ifndef NO_XmTabFree
JNIEXPORT void JNICALL OS_NATIVE(XmTabFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmTabFree\n")
	XmTabFree((XmTab)arg0);
	NATIVE_EXIT(env, that, "XmTabFree\n")
}
#endif

#ifndef NO_XmTabListFree
JNIEXPORT void JNICALL OS_NATIVE(XmTabListFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmTabListFree\n")
	XmTabListFree((XmTabList)arg0);
	NATIVE_EXIT(env, that, "XmTabListFree\n")
}
#endif

#ifndef NO_XmTabListInsertTabs
JNIEXPORT jint JNICALL OS_NATIVE(XmTabListInsertTabs)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2, jint arg3)
{
	jint *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmTabListInsertTabs\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	rc = (jint)XmTabListInsertTabs((XmTabList)arg0, (XmTab *)lparg1, arg2, arg3);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmTabListInsertTabs\n")
	return rc;
}
#endif

#ifndef NO_XmTextClearSelection
JNIEXPORT void JNICALL OS_NATIVE(XmTextClearSelection)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XmTextClearSelection\n")
	XmTextClearSelection((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmTextClearSelection\n")
}
#endif

#ifndef NO_XmTextCopy
JNIEXPORT jboolean JNICALL OS_NATIVE(XmTextCopy)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XmTextCopy\n")
	rc = (jboolean)XmTextCopy((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmTextCopy\n")
	return rc;
}
#endif

#ifndef NO_XmTextCut
JNIEXPORT jboolean JNICALL OS_NATIVE(XmTextCut)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XmTextCut\n")
	rc = (jboolean)XmTextCut((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmTextCut\n")
	return rc;
}
#endif

#ifndef NO_XmTextDisableRedisplay
JNIEXPORT void JNICALL OS_NATIVE(XmTextDisableRedisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmTextDisableRedisplay\n")
	XmTextDisableRedisplay((Widget)arg0);
	NATIVE_EXIT(env, that, "XmTextDisableRedisplay\n")
}
#endif

#ifndef NO_XmTextEnableRedisplay
JNIEXPORT void JNICALL OS_NATIVE(XmTextEnableRedisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmTextEnableRedisplay\n")
	XmTextEnableRedisplay((Widget)arg0);
	NATIVE_EXIT(env, that, "XmTextEnableRedisplay\n")
}
#endif

#ifndef NO_XmTextFieldPaste
JNIEXPORT jboolean JNICALL OS_NATIVE(XmTextFieldPaste)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XmTextFieldPaste\n")
	rc = (jboolean)XmTextFieldPaste((Widget)arg0);
	NATIVE_EXIT(env, that, "XmTextFieldPaste\n")
	return rc;
}
#endif

#ifndef NO_XmTextGetInsertionPosition
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetInsertionPosition)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmTextGetInsertionPosition\n")
	rc = (jint)XmTextGetInsertionPosition((Widget)arg0);
	NATIVE_EXIT(env, that, "XmTextGetInsertionPosition\n")
	return rc;
}
#endif

#ifndef NO_XmTextGetLastPosition
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetLastPosition)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmTextGetLastPosition\n")
	rc = (jint)XmTextGetLastPosition((Widget)arg0);
	NATIVE_EXIT(env, that, "XmTextGetLastPosition\n")
	return rc;
}
#endif

#ifndef NO_XmTextGetMaxLength
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetMaxLength)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmTextGetMaxLength\n")
	rc = (jint)XmTextGetMaxLength((Widget)arg0);
	NATIVE_EXIT(env, that, "XmTextGetMaxLength\n")
	return rc;
}
#endif

#ifndef NO_XmTextGetSelection
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetSelection)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmTextGetSelection\n")
	rc = (jint)XmTextGetSelection((Widget)arg0);
	NATIVE_EXIT(env, that, "XmTextGetSelection\n")
	return rc;
}
#endif

#ifndef NO_XmTextGetSelectionPosition
JNIEXPORT jboolean JNICALL OS_NATIVE(XmTextGetSelectionPosition)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jintArray arg2)
{
	jint *lparg1=NULL;
	jint *lparg2=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "XmTextGetSelectionPosition\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jboolean)XmTextGetSelectionPosition((Widget)arg0, (XmTextPosition *)lparg1, (XmTextPosition *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmTextGetSelectionPosition\n")
	return rc;
}
#endif

#ifndef NO_XmTextGetString
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetString)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XmTextGetString\n")
	rc = (jint)XmTextGetString((Widget)arg0);
	NATIVE_EXIT(env, that, "XmTextGetString\n")
	return rc;
}
#endif

#ifndef NO_XmTextGetSubstring
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetSubstring)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jbyteArray arg4)
{
	jbyte *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmTextGetSubstring\n")
	if (arg4) lparg4 = (*env)->GetByteArrayElements(env, arg4, NULL);
	rc = (jint)XmTextGetSubstring((Widget)arg0, arg1, arg2, arg3, (char *)lparg4);
	if (arg4) (*env)->ReleaseByteArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "XmTextGetSubstring\n")
	return rc;
}
#endif

#ifndef NO_XmTextGetSubstringWcs
JNIEXPORT jint JNICALL OS_NATIVE(XmTextGetSubstringWcs)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jcharArray arg4)
{
	jchar *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmTextGetSubstringWcs\n")
	if (arg4) lparg4 = (*env)->GetCharArrayElements(env, arg4, NULL);
	rc = (jint)XmTextGetSubstringWcs((Widget)arg0, (XmTextPosition)arg1, arg2, arg3, (wchar_t *)lparg4);
	if (arg4) (*env)->ReleaseCharArrayElements(env, arg4, lparg4, 0);
	NATIVE_EXIT(env, that, "XmTextGetSubstringWcs\n")
	return rc;
}
#endif

#ifndef NO_XmTextInsert
JNIEXPORT void JNICALL OS_NATIVE(XmTextInsert)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyteArray arg2)
{
	jbyte *lparg2=NULL;
	NATIVE_ENTER(env, that, "XmTextInsert\n")
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	XmTextInsert((Widget)arg0, arg1, (char *)lparg2);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XmTextInsert\n")
}
#endif

#ifndef NO_XmTextPaste
JNIEXPORT jboolean JNICALL OS_NATIVE(XmTextPaste)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XmTextPaste\n")
	rc = (jboolean)XmTextPaste((Widget)arg0);
	NATIVE_EXIT(env, that, "XmTextPaste\n")
	return rc;
}
#endif

#ifndef NO_XmTextPosToXY
JNIEXPORT jboolean JNICALL OS_NATIVE(XmTextPosToXY)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jshortArray arg2, jshortArray arg3)
{
	jshort *lparg2=NULL;
	jshort *lparg3=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "XmTextPosToXY\n")
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	rc = (jboolean)XmTextPosToXY((Widget)arg0, (XmTextPosition)arg1, (Position *)lparg2, (Position *)lparg3);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XmTextPosToXY\n")
	return rc;
}
#endif

#ifndef NO_XmTextReplace
JNIEXPORT void JNICALL OS_NATIVE(XmTextReplace)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	NATIVE_ENTER(env, that, "XmTextReplace\n")
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	XmTextReplace((Widget)arg0, arg1, arg2, (char *)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "XmTextReplace\n")
}
#endif

#ifndef NO_XmTextScroll
JNIEXPORT void JNICALL OS_NATIVE(XmTextScroll)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XmTextScroll\n")
	XmTextScroll((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmTextScroll\n")
}
#endif

#ifndef NO_XmTextSetEditable
JNIEXPORT void JNICALL OS_NATIVE(XmTextSetEditable)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	NATIVE_ENTER(env, that, "XmTextSetEditable\n")
	XmTextSetEditable((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmTextSetEditable\n")
}
#endif

#ifndef NO_XmTextSetHighlight
JNIEXPORT void JNICALL OS_NATIVE(XmTextSetHighlight)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "XmTextSetHighlight\n")
	XmTextSetHighlight((Widget)arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "XmTextSetHighlight\n")
}
#endif

#ifndef NO_XmTextSetInsertionPosition
JNIEXPORT void JNICALL OS_NATIVE(XmTextSetInsertionPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XmTextSetInsertionPosition\n")
	XmTextSetInsertionPosition((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmTextSetInsertionPosition\n")
}
#endif

#ifndef NO_XmTextSetMaxLength
JNIEXPORT void JNICALL OS_NATIVE(XmTextSetMaxLength)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XmTextSetMaxLength\n")
	XmTextSetMaxLength((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmTextSetMaxLength\n")
}
#endif

#ifndef NO_XmTextSetSelection
JNIEXPORT void JNICALL OS_NATIVE(XmTextSetSelection)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "XmTextSetSelection\n")
	XmTextSetSelection((Widget)arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "XmTextSetSelection\n")
}
#endif

#ifndef NO_XmTextSetString
JNIEXPORT void JNICALL OS_NATIVE(XmTextSetString)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	NATIVE_ENTER(env, that, "XmTextSetString\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	XmTextSetString((Widget)arg0, (char *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XmTextSetString\n")
}
#endif

#ifndef NO_XmTextShowPosition
JNIEXPORT void JNICALL OS_NATIVE(XmTextShowPosition)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XmTextShowPosition\n")
	XmTextShowPosition((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XmTextShowPosition\n")
}
#endif

#ifndef NO_XmUpdateDisplay
JNIEXPORT void JNICALL OS_NATIVE(XmUpdateDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XmUpdateDisplay\n")
	XmUpdateDisplay((Widget)arg0);
	NATIVE_EXIT(env, that, "XmUpdateDisplay\n")
}
#endif

#ifndef NO_XmWidgetGetDisplayRect
JNIEXPORT jboolean JNICALL OS_NATIVE(XmWidgetGetDisplayRect)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1)
{
	XRectangle _arg1, *lparg1=NULL;
	jboolean rc;
	NATIVE_ENTER(env, that, "XmWidgetGetDisplayRect\n")
	if (arg1) lparg1 = getXRectangleFields(env, arg1, &_arg1);
	rc = (jboolean)XmWidgetGetDisplayRect((Widget)arg0, (XRectangle *)lparg1);
	if (arg1) setXRectangleFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "XmWidgetGetDisplayRect\n")
	return rc;
}
#endif

#ifndef NO_XmbTextListToTextProperty
JNIEXPORT jint JNICALL OS_NATIVE(XmbTextListToTextProperty)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jobject arg4)
{
	XTextProperty _arg4, *lparg4=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XmbTextListToTextProperty\n")
	if (arg4) lparg4 = getXTextPropertyFields(env, arg4, &_arg4);
	rc = (jint)XmbTextListToTextProperty((Display *)arg0, (char **)arg1, arg2, (XICCEncodingStyle)arg3, lparg4);
	if (arg4) setXTextPropertyFields(env, arg4, lparg4);
	NATIVE_EXIT(env, that, "XmbTextListToTextProperty\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "XmbTextPropertyToTextList\n")
	if (arg1) lparg1 = getXTextPropertyFields(env, arg1, &_arg1);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)XmbTextPropertyToTextList((Display *)arg0, lparg1, (char ***)lparg2, (int *)lparg3);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) setXTextPropertyFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "XmbTextPropertyToTextList\n")
	return rc;
}
#endif

#ifndef NO_XpCancelJob
JNIEXPORT void JNICALL OS_NATIVE(XpCancelJob)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	NATIVE_ENTER(env, that, "XpCancelJob\n")
	XpCancelJob((Display *)arg0, arg1);
	NATIVE_EXIT(env, that, "XpCancelJob\n")
}
#endif

#ifndef NO_XpCreateContext
JNIEXPORT jint JNICALL OS_NATIVE(XpCreateContext)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XpCreateContext\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)XpCreateContext((Display *)arg0, (char *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XpCreateContext\n")
	return rc;
}
#endif

#ifndef NO_XpDestroyContext
JNIEXPORT void JNICALL OS_NATIVE(XpDestroyContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XpDestroyContext\n")
	XpDestroyContext((Display *)arg0, (XPContext)arg1);
	NATIVE_EXIT(env, that, "XpDestroyContext\n")
}
#endif

#ifndef NO_XpEndJob
JNIEXPORT void JNICALL OS_NATIVE(XpEndJob)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XpEndJob\n")
	XpEndJob((Display *)arg0);
	NATIVE_EXIT(env, that, "XpEndJob\n")
}
#endif

#ifndef NO_XpEndPage
JNIEXPORT void JNICALL OS_NATIVE(XpEndPage)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XpEndPage\n")
	XpEndPage((Display *)arg0);
	NATIVE_EXIT(env, that, "XpEndPage\n")
}
#endif

#ifndef NO_XpFreePrinterList
JNIEXPORT void JNICALL OS_NATIVE(XpFreePrinterList)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XpFreePrinterList\n")
	XpFreePrinterList((XPPrinterList)arg0);
	NATIVE_EXIT(env, that, "XpFreePrinterList\n")
}
#endif

#ifndef NO_XpGetOneAttribute
JNIEXPORT jint JNICALL OS_NATIVE(XpGetOneAttribute)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyte arg2, jbyteArray arg3)
{
	jbyte *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XpGetOneAttribute\n")
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	rc = (jint)XpGetOneAttribute((Display *)arg0, (XPContext)arg1, (XPAttributes)arg2, (char *)lparg3);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "XpGetOneAttribute\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "XpGetPageDimensions\n")
	if (arg2) lparg2 = (*env)->GetShortArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = getXRectangleFields(env, arg4, &_arg4);
	rc = (jint)XpGetPageDimensions((Display *)arg0, (XPContext)arg1, (unsigned short *)lparg2, (unsigned short *)lparg3, (XRectangle *)lparg4);
	if (arg4) setXRectangleFields(env, arg4, lparg4);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseShortArrayElements(env, arg2, lparg2, 0);
	NATIVE_EXIT(env, that, "XpGetPageDimensions\n")
	return rc;
}
#endif

#ifndef NO_XpGetPrinterList
JNIEXPORT jint JNICALL OS_NATIVE(XpGetPrinterList)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jintArray arg2)
{
	jbyte *lparg1=NULL;
	jint *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XpGetPrinterList\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	rc = (jint)XpGetPrinterList((Display *)arg0, (char *)lparg1, (int *)lparg2);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XpGetPrinterList\n")
	return rc;
}
#endif

#ifndef NO_XpGetScreenOfContext
JNIEXPORT jint JNICALL OS_NATIVE(XpGetScreenOfContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XpGetScreenOfContext\n")
	rc = (jint)XpGetScreenOfContext((Display *)arg0, (XPContext)arg1);
	NATIVE_EXIT(env, that, "XpGetScreenOfContext\n")
	return rc;
}
#endif

#ifndef NO_XpSetAttributes
JNIEXPORT void JNICALL OS_NATIVE(XpSetAttributes)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jbyte arg2, jbyteArray arg3, jbyte arg4)
{
	jbyte *lparg3=NULL;
	NATIVE_ENTER(env, that, "XpSetAttributes\n")
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	XpSetAttributes((Display *)arg0, (XPContext)arg1, (XPAttributes)arg2, (char *)lparg3, (XPAttrReplacement)arg4);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "XpSetAttributes\n")
}
#endif

#ifndef NO_XpSetContext
JNIEXPORT void JNICALL OS_NATIVE(XpSetContext)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XpSetContext\n")
	XpSetContext((Display *)arg0, (XPContext)arg1);
	NATIVE_EXIT(env, that, "XpSetContext\n")
}
#endif

#ifndef NO_XpStartJob
JNIEXPORT void JNICALL OS_NATIVE(XpStartJob)
	(JNIEnv *env, jclass that, jint arg0, jbyte arg1)
{
	NATIVE_ENTER(env, that, "XpStartJob\n")
	XpStartJob((Display *)arg0, (XPSaveData)arg1);
	NATIVE_EXIT(env, that, "XpStartJob\n")
}
#endif

#ifndef NO_XpStartPage
JNIEXPORT void JNICALL OS_NATIVE(XpStartPage)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XpStartPage\n")
	XpStartPage((Display *)arg0, (Window)arg1);
	NATIVE_EXIT(env, that, "XpStartPage\n")
}
#endif

#ifndef NO_XtAddCallback
JNIEXPORT void JNICALL OS_NATIVE(XtAddCallback)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "XtAddCallback\n")
	XtAddCallback((Widget)arg0, (String)arg1, (XtCallbackProc)arg2, (XtPointer)arg3);
	NATIVE_EXIT(env, that, "XtAddCallback\n")
}
#endif

#ifndef NO_XtAddEventHandler
JNIEXPORT void JNICALL OS_NATIVE(XtAddEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	NATIVE_ENTER(env, that, "XtAddEventHandler\n")
	XtAddEventHandler((Widget)arg0, arg1, arg2, (XtEventHandler)arg3, (XtPointer)arg4);
	NATIVE_EXIT(env, that, "XtAddEventHandler\n")
}
#endif

#ifndef NO_XtAddExposureToRegion
JNIEXPORT void JNICALL OS_NATIVE(XtAddExposureToRegion)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XtAddExposureToRegion\n")
	XtAddExposureToRegion((XEvent *)arg0, (Region)arg1);
	NATIVE_EXIT(env, that, "XtAddExposureToRegion\n")
}
#endif

#ifndef NO_XtAppAddInput
JNIEXPORT jint JNICALL OS_NATIVE(XtAppAddInput)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtAppAddInput\n")
	rc = (jint)XtAppAddInput((XtAppContext)arg0, arg1, (XtPointer)arg2, (XtInputCallbackProc)arg3, (XtPointer)arg4);
	NATIVE_EXIT(env, that, "XtAppAddInput\n")
	return rc;
}
#endif

#ifndef NO_XtAppAddTimeOut
JNIEXPORT jint JNICALL OS_NATIVE(XtAppAddTimeOut)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtAppAddTimeOut\n")
	rc = (jint)XtAppAddTimeOut((XtAppContext)arg0, arg1, (XtTimerCallbackProc)arg2, (XtPointer)arg3);
	NATIVE_EXIT(env, that, "XtAppAddTimeOut\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "XtAppCreateShell\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)XtAppCreateShell((String)lparg0, (String)lparg1, (WidgetClass)arg2, (Display *)arg3, (ArgList)lparg4, arg5);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "XtAppCreateShell\n")
	return rc;
}
#endif

#ifndef NO_XtAppGetSelectionTimeout
JNIEXPORT jint JNICALL OS_NATIVE(XtAppGetSelectionTimeout)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtAppGetSelectionTimeout\n")
	rc = (jint)XtAppGetSelectionTimeout((XtAppContext)arg0);
	NATIVE_EXIT(env, that, "XtAppGetSelectionTimeout\n")
	return rc;
}
#endif

#ifndef NO_XtAppNextEvent
JNIEXPORT void JNICALL OS_NATIVE(XtAppNextEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XtAppNextEvent\n")
	XtAppNextEvent((XtAppContext)arg0, (XEvent *)arg1);
	NATIVE_EXIT(env, that, "XtAppNextEvent\n")
}
#endif

#ifndef NO_XtAppPeekEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(XtAppPeekEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XtAppPeekEvent\n")
	rc = (jboolean)XtAppPeekEvent((XtAppContext)arg0, (XEvent *)arg1);
	NATIVE_EXIT(env, that, "XtAppPeekEvent\n")
	return rc;
}
#endif

#ifndef NO_XtAppPending
JNIEXPORT jint JNICALL OS_NATIVE(XtAppPending)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtAppPending\n")
	rc = (jint)XtAppPending((XtAppContext)arg0);
	NATIVE_EXIT(env, that, "XtAppPending\n")
	return rc;
}
#endif

#ifndef NO_XtAppProcessEvent
JNIEXPORT void JNICALL OS_NATIVE(XtAppProcessEvent)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XtAppProcessEvent\n")
	XtAppProcessEvent((XtAppContext)arg0, arg1);
	NATIVE_EXIT(env, that, "XtAppProcessEvent\n")
}
#endif

#ifndef NO_XtAppSetErrorHandler
JNIEXPORT jint JNICALL OS_NATIVE(XtAppSetErrorHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtAppSetErrorHandler\n")
	rc = (jint)XtAppSetErrorHandler((XtAppContext)arg0, (XtErrorHandler)arg1);
	NATIVE_EXIT(env, that, "XtAppSetErrorHandler\n")
	return rc;
}
#endif

#ifndef NO_XtAppSetFallbackResources
JNIEXPORT void JNICALL OS_NATIVE(XtAppSetFallbackResources)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XtAppSetFallbackResources\n")
	XtAppSetFallbackResources((XtAppContext)arg0, (String *)arg1);
	NATIVE_EXIT(env, that, "XtAppSetFallbackResources\n")
}
#endif

#ifndef NO_XtAppSetSelectionTimeout
JNIEXPORT void JNICALL OS_NATIVE(XtAppSetSelectionTimeout)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XtAppSetSelectionTimeout\n")
	XtAppSetSelectionTimeout((XtAppContext)arg0, arg1);
	NATIVE_EXIT(env, that, "XtAppSetSelectionTimeout\n")
}
#endif

#ifndef NO_XtAppSetWarningHandler
JNIEXPORT jint JNICALL OS_NATIVE(XtAppSetWarningHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtAppSetWarningHandler\n")
	rc = (jint)XtAppSetWarningHandler((XtAppContext)arg0, (XtErrorHandler)arg1);
	NATIVE_EXIT(env, that, "XtAppSetWarningHandler\n")
	return rc;
}
#endif

#ifndef NO_XtBuildEventMask
JNIEXPORT jint JNICALL OS_NATIVE(XtBuildEventMask)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtBuildEventMask\n")
	rc = (jint)XtBuildEventMask((Widget)arg0);
	NATIVE_EXIT(env, that, "XtBuildEventMask\n")
	return rc;
}
#endif

#ifndef NO_XtCallActionProc
JNIEXPORT void JNICALL OS_NATIVE(XtCallActionProc)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2, jintArray arg3, jint arg4)
{
	jbyte *lparg1=NULL;
	jint *lparg3=NULL;
	NATIVE_ENTER(env, that, "XtCallActionProc\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	XtCallActionProc((Widget)arg0, (String)lparg1, (XEvent *)arg2, (String *)lparg3, arg4);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XtCallActionProc\n")
}
#endif

#ifndef NO_XtClass
JNIEXPORT jint JNICALL OS_NATIVE(XtClass)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtClass\n")
	rc = (jint)XtClass((Widget)arg0);
	NATIVE_EXIT(env, that, "XtClass\n")
	return rc;
}
#endif

#ifndef NO_XtConfigureWidget
JNIEXPORT void JNICALL OS_NATIVE(XtConfigureWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3, jint arg4, jint arg5)
{
	NATIVE_ENTER(env, that, "XtConfigureWidget\n")
	XtConfigureWidget((Widget)arg0, arg1, arg2, arg3, arg4, arg5);
	NATIVE_EXIT(env, that, "XtConfigureWidget\n")
}
#endif

#ifndef NO_XtCreateApplicationContext
JNIEXPORT jint JNICALL OS_NATIVE(XtCreateApplicationContext)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtCreateApplicationContext\n")
	rc = (jint)XtCreateApplicationContext();
	NATIVE_EXIT(env, that, "XtCreateApplicationContext\n")
	return rc;
}
#endif

#ifndef NO_XtCreatePopupShell
JNIEXPORT jint JNICALL OS_NATIVE(XtCreatePopupShell)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2, jintArray arg3, jint arg4)
{
	jbyte *lparg0=NULL;
	jint *lparg3=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XtCreatePopupShell\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	rc = (jint)XtCreatePopupShell((String)lparg0, (WidgetClass)arg1, (Widget)arg2, (ArgList)lparg3, arg4);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "XtCreatePopupShell\n")
	return rc;
}
#endif

#ifndef NO_XtDestroyApplicationContext
JNIEXPORT void JNICALL OS_NATIVE(XtDestroyApplicationContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XtDestroyApplicationContext\n")
	XtDestroyApplicationContext((XtAppContext)arg0);
	NATIVE_EXIT(env, that, "XtDestroyApplicationContext\n")
}
#endif

#ifndef NO_XtDestroyWidget
JNIEXPORT void JNICALL OS_NATIVE(XtDestroyWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XtDestroyWidget\n")
	XtDestroyWidget((Widget)arg0);
	NATIVE_EXIT(env, that, "XtDestroyWidget\n")
}
#endif

#ifndef NO_XtDispatchEvent
JNIEXPORT jboolean JNICALL OS_NATIVE(XtDispatchEvent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XtDispatchEvent\n")
	rc = (jboolean)XtDispatchEvent((XEvent *)arg0);
	NATIVE_EXIT(env, that, "XtDispatchEvent\n")
	return rc;
}
#endif

#ifndef NO_XtDisplay
JNIEXPORT jint JNICALL OS_NATIVE(XtDisplay)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtDisplay\n")
	rc = (jint)XtDisplay((Widget)arg0);
	NATIVE_EXIT(env, that, "XtDisplay\n")
	return rc;
}
#endif

#ifndef NO_XtDisplayToApplicationContext
JNIEXPORT jint JNICALL OS_NATIVE(XtDisplayToApplicationContext)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtDisplayToApplicationContext\n")
	rc = (jint)XtDisplayToApplicationContext((Display *)arg0);
	NATIVE_EXIT(env, that, "XtDisplayToApplicationContext\n")
	return rc;
}
#endif

#ifndef NO_XtFree
JNIEXPORT void JNICALL OS_NATIVE(XtFree)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XtFree\n")
	XtFree((char *)arg0);
	NATIVE_EXIT(env, that, "XtFree\n")
}
#endif

#ifndef NO_XtGetMultiClickTime
JNIEXPORT jint JNICALL OS_NATIVE(XtGetMultiClickTime)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtGetMultiClickTime\n")
	rc = (jint)XtGetMultiClickTime((Display *)arg0);
	NATIVE_EXIT(env, that, "XtGetMultiClickTime\n")
	return rc;
}
#endif

#ifndef NO_XtInsertEventHandler
JNIEXPORT void JNICALL OS_NATIVE(XtInsertEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4, jint arg5)
{
	NATIVE_ENTER(env, that, "XtInsertEventHandler\n")
	XtInsertEventHandler((Widget)arg0, (EventMask)arg1, (Boolean)arg2, (XtEventHandler)arg3, (XtPointer)arg4, (XtListPosition)arg5);
	NATIVE_EXIT(env, that, "XtInsertEventHandler\n")
}
#endif

#ifndef NO_XtIsManaged
JNIEXPORT jboolean JNICALL OS_NATIVE(XtIsManaged)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XtIsManaged\n")
	rc = (jboolean)XtIsManaged((Widget)arg0);
	NATIVE_EXIT(env, that, "XtIsManaged\n")
	return rc;
}
#endif

#ifndef NO_XtIsRealized
JNIEXPORT jboolean JNICALL OS_NATIVE(XtIsRealized)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XtIsRealized\n")
	rc = (jboolean)XtIsRealized((Widget)arg0);
	NATIVE_EXIT(env, that, "XtIsRealized\n")
	return rc;
}
#endif

#ifndef NO_XtIsSubclass
JNIEXPORT jboolean JNICALL OS_NATIVE(XtIsSubclass)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XtIsSubclass\n")
	rc = (jboolean)XtIsSubclass((Widget)arg0, (WidgetClass)arg1);
	NATIVE_EXIT(env, that, "XtIsSubclass\n")
	return rc;
}
#endif

#ifndef NO_XtIsTopLevelShell
JNIEXPORT jboolean JNICALL OS_NATIVE(XtIsTopLevelShell)
	(JNIEnv *env, jclass that, jint arg0)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XtIsTopLevelShell\n")
	rc = (jboolean)XtIsTopLevelShell((Widget)arg0);
	NATIVE_EXIT(env, that, "XtIsTopLevelShell\n")
	return rc;
}
#endif

#ifndef NO_XtLastTimestampProcessed
JNIEXPORT jint JNICALL OS_NATIVE(XtLastTimestampProcessed)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtLastTimestampProcessed\n")
	rc = (jint)XtLastTimestampProcessed((Display *)arg0);
	NATIVE_EXIT(env, that, "XtLastTimestampProcessed\n")
	return rc;
}
#endif

#ifndef NO_XtMalloc
JNIEXPORT jint JNICALL OS_NATIVE(XtMalloc)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtMalloc\n")
	rc = (jint)XtMalloc(arg0);
	NATIVE_EXIT(env, that, "XtMalloc\n")
	return rc;
}
#endif

#ifndef NO_XtManageChild
JNIEXPORT void JNICALL OS_NATIVE(XtManageChild)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XtManageChild\n")
	XtManageChild((Widget)arg0);
	NATIVE_EXIT(env, that, "XtManageChild\n")
}
#endif

#ifndef NO_XtMapWidget
JNIEXPORT void JNICALL OS_NATIVE(XtMapWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XtMapWidget\n")
	XtMapWidget((Widget)arg0);
	NATIVE_EXIT(env, that, "XtMapWidget\n")
}
#endif

#ifndef NO_XtMoveWidget
JNIEXPORT void JNICALL OS_NATIVE(XtMoveWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XtMoveWidget\n")
	XtMoveWidget((Widget)arg0, arg1, arg2);
	NATIVE_EXIT(env, that, "XtMoveWidget\n")
}
#endif

#ifndef NO_XtNameToWidget
JNIEXPORT jint JNICALL OS_NATIVE(XtNameToWidget)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XtNameToWidget\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)XtNameToWidget((Widget)arg0, (String)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XtNameToWidget\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "XtOpenDisplay\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg6) lparg6 = (*env)->GetIntArrayElements(env, arg6, NULL);
	rc = (jint)XtOpenDisplay((XtAppContext)arg0, (String)lparg1, (String)lparg2, (String)lparg3, (XrmOptionDescRec *)arg4, arg5, (int *)lparg6, (char **)arg7);
	if (arg6) (*env)->ReleaseIntArrayElements(env, arg6, lparg6, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XtOpenDisplay\n")
	return rc;
}
#endif

#ifndef NO_XtOverrideTranslations
JNIEXPORT void JNICALL OS_NATIVE(XtOverrideTranslations)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XtOverrideTranslations\n")
	XtOverrideTranslations((Widget)arg0, (XtTranslations)arg1);
	NATIVE_EXIT(env, that, "XtOverrideTranslations\n")
}
#endif

#ifndef NO_XtParent
JNIEXPORT jint JNICALL OS_NATIVE(XtParent)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtParent\n")
	rc = (jint)XtParent((Widget)arg0);
	NATIVE_EXIT(env, that, "XtParent\n")
	return rc;
}
#endif

#ifndef NO_XtParseTranslationTable
JNIEXPORT jint JNICALL OS_NATIVE(XtParseTranslationTable)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XtParseTranslationTable\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)XtParseTranslationTable((String)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "XtParseTranslationTable\n")
	return rc;
}
#endif

#ifndef NO_XtPopdown
JNIEXPORT void JNICALL OS_NATIVE(XtPopdown)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XtPopdown\n")
	XtPopdown((Widget)arg0);
	NATIVE_EXIT(env, that, "XtPopdown\n")
}
#endif

#ifndef NO_XtPopup
JNIEXPORT void JNICALL OS_NATIVE(XtPopup)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XtPopup\n")
	XtPopup((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XtPopup\n")
}
#endif

#ifndef NO_XtQueryGeometry
JNIEXPORT jint JNICALL OS_NATIVE(XtQueryGeometry)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jobject arg2)
{
	XtWidgetGeometry _arg1, *lparg1=NULL;
	XtWidgetGeometry _arg2, *lparg2=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "XtQueryGeometry\n")
	if (arg1) lparg1 = getXtWidgetGeometryFields(env, arg1, &_arg1);
	if (arg2) lparg2 = getXtWidgetGeometryFields(env, arg2, &_arg2);
	rc = (jint)XtQueryGeometry((Widget)arg0, (XtWidgetGeometry *)lparg1, (XtWidgetGeometry *)lparg2);
	if (arg2) setXtWidgetGeometryFields(env, arg2, lparg2);
	if (arg1) setXtWidgetGeometryFields(env, arg1, lparg1);
	NATIVE_EXIT(env, that, "XtQueryGeometry\n")
	return rc;
}
#endif

#ifndef NO_XtRealizeWidget
JNIEXPORT void JNICALL OS_NATIVE(XtRealizeWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XtRealizeWidget\n")
	XtRealizeWidget((Widget)arg0);
	NATIVE_EXIT(env, that, "XtRealizeWidget\n")
}
#endif

#ifndef NO_XtRegisterDrawable
JNIEXPORT void JNICALL OS_NATIVE(XtRegisterDrawable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	NATIVE_ENTER(env, that, "XtRegisterDrawable\n")
	XtRegisterDrawable((Display *)arg0, (Drawable)arg1, (Widget)arg2);
	NATIVE_EXIT(env, that, "XtRegisterDrawable\n")
}
#endif

#ifndef NO_XtRemoveEventHandler
JNIEXPORT void JNICALL OS_NATIVE(XtRemoveEventHandler)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jboolean arg2, jint arg3, jint arg4)
{
	NATIVE_ENTER(env, that, "XtRemoveEventHandler\n")
	XtRemoveEventHandler((Widget)arg0, arg1, arg2, (XtEventHandler)arg3, (XtPointer)arg4);
	NATIVE_EXIT(env, that, "XtRemoveEventHandler\n")
}
#endif

#ifndef NO_XtRemoveInput
JNIEXPORT void JNICALL OS_NATIVE(XtRemoveInput)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XtRemoveInput\n")
	XtRemoveInput((XtInputId)arg0);
	NATIVE_EXIT(env, that, "XtRemoveInput\n")
}
#endif

#ifndef NO_XtRemoveTimeOut
JNIEXPORT void JNICALL OS_NATIVE(XtRemoveTimeOut)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XtRemoveTimeOut\n")
	XtRemoveTimeOut(arg0);
	NATIVE_EXIT(env, that, "XtRemoveTimeOut\n")
}
#endif

#ifndef NO_XtResizeWidget
JNIEXPORT void JNICALL OS_NATIVE(XtResizeWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2, jint arg3)
{
	NATIVE_ENTER(env, that, "XtResizeWidget\n")
	XtResizeWidget((Widget)arg0, arg1, arg2, arg3);
	NATIVE_EXIT(env, that, "XtResizeWidget\n")
}
#endif

#ifndef NO_XtResizeWindow
JNIEXPORT void JNICALL OS_NATIVE(XtResizeWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XtResizeWindow\n")
	XtResizeWindow((Widget)arg0);
	NATIVE_EXIT(env, that, "XtResizeWindow\n")
}
#endif

#ifndef NO_XtSetLanguageProc
JNIEXPORT jint JNICALL OS_NATIVE(XtSetLanguageProc)
	(JNIEnv *env, jclass that, jint arg0, jint arg1, jint arg2)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtSetLanguageProc\n")
	rc = (jint)XtSetLanguageProc((XtAppContext)arg0, (XtLanguageProc)arg1, (XtPointer)arg2);
	NATIVE_EXIT(env, that, "XtSetLanguageProc\n")
	return rc;
}
#endif

#ifndef NO_XtSetMappedWhenManaged
JNIEXPORT void JNICALL OS_NATIVE(XtSetMappedWhenManaged)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	NATIVE_ENTER(env, that, "XtSetMappedWhenManaged\n")
	XtSetMappedWhenManaged((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "XtSetMappedWhenManaged\n")
}
#endif

#ifndef NO_XtSetValues
JNIEXPORT void JNICALL OS_NATIVE(XtSetValues)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "XtSetValues\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	XtSetValues((Widget)arg0, (ArgList)lparg1, arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "XtSetValues\n")
}
#endif

#ifndef NO_XtToolkitInitialize
JNIEXPORT void JNICALL OS_NATIVE(XtToolkitInitialize)
	(JNIEnv *env, jclass that)
{
	NATIVE_ENTER(env, that, "XtToolkitInitialize\n")
	XtToolkitInitialize();
	NATIVE_EXIT(env, that, "XtToolkitInitialize\n")
}
#endif

#ifndef NO_XtToolkitThreadInitialize
JNIEXPORT jboolean JNICALL OS_NATIVE(XtToolkitThreadInitialize)
	(JNIEnv *env, jclass that)
{
	jboolean rc;
	NATIVE_ENTER(env, that, "XtToolkitThreadInitialize\n")
	rc = (jboolean)XtToolkitThreadInitialize();
	NATIVE_EXIT(env, that, "XtToolkitThreadInitialize\n")
	return rc;
}
#endif

#ifndef NO_XtTranslateCoords
JNIEXPORT void JNICALL OS_NATIVE(XtTranslateCoords)
	(JNIEnv *env, jclass that, jint arg0, jshort arg1, jshort arg2, jshortArray arg3, jshortArray arg4)
{
	jshort *lparg3=NULL;
	jshort *lparg4=NULL;
	NATIVE_ENTER(env, that, "XtTranslateCoords\n")
	if (arg3) lparg3 = (*env)->GetShortArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetShortArrayElements(env, arg4, NULL);
	XtTranslateCoords((Widget)arg0, arg1, arg2, lparg3, lparg4);
	if (arg4) (*env)->ReleaseShortArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseShortArrayElements(env, arg3, lparg3, 0);
	NATIVE_EXIT(env, that, "XtTranslateCoords\n")
}
#endif

#ifndef NO_XtUnmanageChild
JNIEXPORT void JNICALL OS_NATIVE(XtUnmanageChild)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XtUnmanageChild\n")
	XtUnmanageChild((Widget)arg0);
	NATIVE_EXIT(env, that, "XtUnmanageChild\n")
}
#endif

#ifndef NO_XtUnmapWidget
JNIEXPORT void JNICALL OS_NATIVE(XtUnmapWidget)
	(JNIEnv *env, jclass that, jint arg0)
{
	NATIVE_ENTER(env, that, "XtUnmapWidget\n")
	XtUnmapWidget((Widget)arg0);
	NATIVE_EXIT(env, that, "XtUnmapWidget\n")
}
#endif

#ifndef NO_XtUnregisterDrawable
JNIEXPORT void JNICALL OS_NATIVE(XtUnregisterDrawable)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	NATIVE_ENTER(env, that, "XtUnregisterDrawable\n")
	XtUnregisterDrawable((Display *)arg0, (Drawable)arg1);
	NATIVE_EXIT(env, that, "XtUnregisterDrawable\n")
}
#endif

#ifndef NO_XtWindow
JNIEXPORT jint JNICALL OS_NATIVE(XtWindow)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtWindow\n")
	rc = (jint)XtWindow((Widget)arg0);
	NATIVE_EXIT(env, that, "XtWindow\n")
	return rc;
}
#endif

#ifndef NO_XtWindowToWidget
JNIEXPORT jint JNICALL OS_NATIVE(XtWindowToWidget)
	(JNIEnv *env, jclass that, jint arg0, jint arg1)
{
	jint rc;
	NATIVE_ENTER(env, that, "XtWindowToWidget\n")
	rc = (jint)XtWindowToWidget((Display *)arg0, (Window)arg1);
	NATIVE_EXIT(env, that, "XtWindowToWidget\n")
	return rc;
}
#endif

#ifndef NO__1XmSetMenuTraversal
JNIEXPORT void JNICALL OS_NATIVE(_1XmSetMenuTraversal)
	(JNIEnv *env, jclass that, jint arg0, jboolean arg1)
{
	NATIVE_ENTER(env, that, "_1XmSetMenuTraversal\n")
	_XmSetMenuTraversal((Widget)arg0, arg1);
	NATIVE_EXIT(env, that, "_1XmSetMenuTraversal\n")
}
#endif

#ifndef NO_close
JNIEXPORT jint JNICALL OS_NATIVE(close)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "close\n")
	rc = (jint)close(arg0);
	NATIVE_EXIT(env, that, "close\n")
	return rc;
}
#endif

#ifndef NO_getenv
JNIEXPORT jint JNICALL OS_NATIVE(getenv)
	(JNIEnv *env, jclass that, jbyteArray arg0)
{
	jbyte *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "getenv\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	rc = (jint)getenv((const char *)lparg0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "getenv\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "iconv\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetIntArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetIntArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)iconv((iconv_t)arg0, (void *)lparg1, (size_t *)lparg2, (char **)lparg3, (size_t *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseIntArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseIntArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "iconv\n")
	return rc;
}
#endif

#ifndef NO_iconv_1close
JNIEXPORT jint JNICALL OS_NATIVE(iconv_1close)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "iconv_1close\n")
	rc = (jint)iconv_close((iconv_t)arg0);
	NATIVE_EXIT(env, that, "iconv_1close\n")
	return rc;
}
#endif

#ifndef NO_iconv_1open
JNIEXPORT jint JNICALL OS_NATIVE(iconv_1open)
	(JNIEnv *env, jclass that, jbyteArray arg0, jbyteArray arg1)
{
	jbyte *lparg0=NULL;
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "iconv_1open\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)iconv_open((const char *)lparg0, (const char *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "iconv_1open\n")
	return rc;
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XButtonEvent_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XButtonEvent_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XButtonEvent _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XButtonEvent_2I\n")
	if (arg1) lparg1 = getXButtonEventFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XButtonEvent_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XClientMessageEvent_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XClientMessageEvent_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XClientMessageEvent _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XClientMessageEvent_2I\n")
	if (arg1) lparg1 = getXClientMessageEventFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XClientMessageEvent_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XConfigureEvent_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XConfigureEvent_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XConfigureEvent _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XConfigureEvent_2I\n")
	if (arg1) lparg1 = getXConfigureEventFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XConfigureEvent_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XExposeEvent_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XExposeEvent_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XExposeEvent _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XExposeEvent_2I\n")
	if (arg1) lparg1 = getXExposeEventFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XExposeEvent_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XImage_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XImage_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XImage _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XImage_2I\n")
	if (arg1) lparg1 = getXImageFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XImage_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XKeyEvent_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XKeyEvent_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XKeyEvent _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XKeyEvent_2I\n")
	if (arg1) lparg1 = getXKeyEventFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XKeyEvent_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XmDragProcCallbackStruct _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2I\n")
	if (arg1) lparg1 = getXmDragProcCallbackStructFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XmTextBlockRec_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XmTextBlockRec_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XmTextBlockRec _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XmTextBlockRec_2I\n")
	if (arg1) lparg1 = getXmTextBlockRecFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XmTextBlockRec_2I\n")
}
#endif

#ifndef NO_memmove__ILorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2I
JNIEXPORT void JNICALL OS_NATIVE(memmove__ILorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2I)
	(JNIEnv *env, jclass that, jint arg0, jobject arg1, jint arg2)
{
	XmTextVerifyCallbackStruct _arg1, *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2I\n")
	if (arg1) lparg1 = getXmTextVerifyCallbackStructFields(env, arg1, &_arg1);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	NATIVE_EXIT(env, that, "memmove__ILorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2I\n")
}
#endif

#ifndef NO_memmove__I_3BI
JNIEXPORT void JNICALL OS_NATIVE(memmove__I_3BI)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__I_3BI\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "memmove__I_3BI\n")
}
#endif

#ifndef NO_memmove__I_3CI
JNIEXPORT void JNICALL OS_NATIVE(memmove__I_3CI)
	(JNIEnv *env, jclass that, jint arg0, jcharArray arg1, jint arg2)
{
	jchar *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__I_3CI\n")
	if (arg1) lparg1 = (*env)->GetCharArrayElements(env, arg1, NULL);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseCharArrayElements(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "memmove__I_3CI\n")
}
#endif

#ifndef NO_memmove__I_3II
JNIEXPORT void JNICALL OS_NATIVE(memmove__I_3II)
	(JNIEnv *env, jclass that, jint arg0, jintArray arg1, jint arg2)
{
	jint *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__I_3II\n")
	if (arg1) lparg1 = (*env)->GetIntArrayElements(env, arg1, NULL);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseIntArrayElements(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "memmove__I_3II\n")
}
#endif

#ifndef NO_memmove__I_3SI
JNIEXPORT void JNICALL OS_NATIVE(memmove__I_3SI)
	(JNIEnv *env, jclass that, jint arg0, jshortArray arg1, jint arg2)
{
	jshort *lparg1=NULL;
	NATIVE_ENTER(env, that, "memmove__I_3SI\n")
	if (arg1) lparg1 = (*env)->GetShortArrayElements(env, arg1, NULL);
	memmove((void *)arg0, (const void *)lparg1, (size_t)arg2);
	if (arg1) (*env)->ReleaseShortArrayElements(env, arg1, lparg1, JNI_ABORT);
	NATIVE_EXIT(env, that, "memmove__I_3SI\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_Visual_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_Visual_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	Visual _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_Visual_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setVisualFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_Visual_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XButtonEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XButtonEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XButtonEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XButtonEvent_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXButtonEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XButtonEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XCharStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XCharStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XCharStruct _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XCharStruct_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXCharStructFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XCharStruct_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XClientMessageEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XClientMessageEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XClientMessageEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XClientMessageEvent_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXClientMessageEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XClientMessageEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XConfigureEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XConfigureEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XConfigureEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XConfigureEvent_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXConfigureEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XConfigureEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XCreateWindowEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XCreateWindowEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XCreateWindowEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XCreateWindowEvent_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXCreateWindowEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XCreateWindowEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XCrossingEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XCrossingEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XCrossingEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XCrossingEvent_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXCrossingEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XCrossingEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XDestroyWindowEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XDestroyWindowEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XDestroyWindowEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XDestroyWindowEvent_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXDestroyWindowEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XDestroyWindowEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XEvent_2II\n")
	if (arg0) lparg0 = getXEventFields(env, arg0, &_arg0);
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XExposeEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XExposeEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XExposeEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XExposeEvent_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXExposeEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XExposeEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XFocusChangeEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XFocusChangeEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XFocusChangeEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XFocusChangeEvent_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXFocusChangeEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XFocusChangeEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XFontStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XFontStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XFontStruct _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XFontStruct_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXFontStructFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XFontStruct_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XImage_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XImage_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XImage _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XImage_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXImageFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XImage_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XKeyEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXKeyEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XModifierKeymap_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XModifierKeymap_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XModifierKeymap _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XModifierKeymap_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXModifierKeymapFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XModifierKeymap_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XMotionEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XMotionEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XMotionEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XMotionEvent_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXMotionEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XMotionEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XPropertyEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XPropertyEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XPropertyEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XPropertyEvent_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXPropertyEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XPropertyEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XReparentEvent_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XReparentEvent_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XReparentEvent _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XReparentEvent_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXReparentEventFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XReparentEvent_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XineramaScreenInfo_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XineramaScreenInfo_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XineramaScreenInfo _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XineramaScreenInfo_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXineramaScreenInfoFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XineramaScreenInfo_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmAnyCallbackStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmAnyCallbackStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmAnyCallbackStruct _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XmAnyCallbackStruct_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXmAnyCallbackStructFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XmAnyCallbackStruct_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmDragProcCallbackStruct _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXmDragProcCallbackStructFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XmDragProcCallbackStruct_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmDropFinishCallbackStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmDropFinishCallbackStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmDropFinishCallbackStruct _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XmDropFinishCallbackStruct_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXmDropFinishCallbackStructFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XmDropFinishCallbackStruct_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmDropProcCallbackStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmDropProcCallbackStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmDropProcCallbackStruct _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XmDropProcCallbackStruct_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXmDropProcCallbackStructFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XmDropProcCallbackStruct_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmTextBlockRec_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmTextBlockRec_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmTextBlockRec _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XmTextBlockRec_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXmTextBlockRecFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XmTextBlockRec_2II\n")
}
#endif

#ifndef NO_memmove__Lorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2II
JNIEXPORT void JNICALL OS_NATIVE(memmove__Lorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2II)
	(JNIEnv *env, jclass that, jobject arg0, jint arg1, jint arg2)
{
	XmTextVerifyCallbackStruct _arg0, *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2II\n")
	if (arg0) lparg0 = &_arg0;
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) setXmTextVerifyCallbackStructFields(env, arg0, lparg0);
	NATIVE_EXIT(env, that, "memmove__Lorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2II\n")
}
#endif

#ifndef NO_memmove___3BII
JNIEXPORT void JNICALL OS_NATIVE(memmove___3BII)
	(JNIEnv *env, jclass that, jbyteArray arg0, jint arg1, jint arg2)
{
	jbyte *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove___3BII\n")
	if (arg0) lparg0 = (*env)->GetByteArrayElements(env, arg0, NULL);
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleaseByteArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memmove___3BII\n")
}
#endif

#ifndef NO_memmove___3CII
JNIEXPORT void JNICALL OS_NATIVE(memmove___3CII)
	(JNIEnv *env, jclass that, jcharArray arg0, jint arg1, jint arg2)
{
	jchar *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove___3CII\n")
	if (arg0) lparg0 = (*env)->GetCharArrayElements(env, arg0, NULL);
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleaseCharArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memmove___3CII\n")
}
#endif

#ifndef NO_memmove___3III
JNIEXPORT void JNICALL OS_NATIVE(memmove___3III)
	(JNIEnv *env, jclass that, jintArray arg0, jint arg1, jint arg2)
{
	jint *lparg0=NULL;
	NATIVE_ENTER(env, that, "memmove___3III\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	memmove((void *)lparg0, (const void *)arg1, (size_t)arg2);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "memmove___3III\n")
}
#endif

#ifndef NO_nl_1langinfo
JNIEXPORT jint JNICALL OS_NATIVE(nl_1langinfo)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "nl_1langinfo\n")
	rc = (jint)nl_langinfo(arg0);
	NATIVE_EXIT(env, that, "nl_1langinfo\n")
	return rc;
}
#endif

#ifndef NO_overrideShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(overrideShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "overrideShellWidgetClass\n")
	rc = (jint)overrideShellWidgetClass;
	NATIVE_EXIT(env, that, "overrideShellWidgetClass\n")
	return rc;
}
#endif

#ifndef NO_pipe
JNIEXPORT jint JNICALL OS_NATIVE(pipe)
	(JNIEnv *env, jclass that, jintArray arg0)
{
	jint *lparg0=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "pipe\n")
	if (arg0) lparg0 = (*env)->GetIntArrayElements(env, arg0, NULL);
	rc = (jint)pipe((int *)lparg0);
	if (arg0) (*env)->ReleaseIntArrayElements(env, arg0, lparg0, 0);
	NATIVE_EXIT(env, that, "pipe\n")
	return rc;
}
#endif

#ifndef NO_read
JNIEXPORT jint JNICALL OS_NATIVE(read)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "read\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)read(arg0, (char *)lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "read\n")
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
	jint rc;
	NATIVE_ENTER(env, that, "select\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	if (arg2) lparg2 = (*env)->GetByteArrayElements(env, arg2, NULL);
	if (arg3) lparg3 = (*env)->GetByteArrayElements(env, arg3, NULL);
	if (arg4) lparg4 = (*env)->GetIntArrayElements(env, arg4, NULL);
	rc = (jint)select(arg0, (fd_set *)lparg1, (fd_set *)lparg2, (fd_set *)lparg3, (struct timeval *)lparg4);
	if (arg4) (*env)->ReleaseIntArrayElements(env, arg4, lparg4, 0);
	if (arg3) (*env)->ReleaseByteArrayElements(env, arg3, lparg3, 0);
	if (arg2) (*env)->ReleaseByteArrayElements(env, arg2, lparg2, 0);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "select\n")
	return rc;
}
#endif

#ifndef NO_setlocale
JNIEXPORT jint JNICALL OS_NATIVE(setlocale)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "setlocale\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)setlocale(arg0, (char *)lparg1);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "setlocale\n")
	return rc;
}
#endif

#ifndef NO_shellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(shellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "shellWidgetClass\n")
	rc = (jint)shellWidgetClass;
	NATIVE_EXIT(env, that, "shellWidgetClass\n")
	return rc;
}
#endif

#ifndef NO_strlen
JNIEXPORT jint JNICALL OS_NATIVE(strlen)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "strlen\n")
	rc = (jint)strlen((char *)arg0);
	NATIVE_EXIT(env, that, "strlen\n")
	return rc;
}
#endif

#ifndef NO_topLevelShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(topLevelShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "topLevelShellWidgetClass\n")
	rc = (jint)topLevelShellWidgetClass;
	NATIVE_EXIT(env, that, "topLevelShellWidgetClass\n")
	return rc;
}
#endif

#ifndef NO_transientShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(transientShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "transientShellWidgetClass\n")
	rc = (jint)transientShellWidgetClass;
	NATIVE_EXIT(env, that, "transientShellWidgetClass\n")
	return rc;
}
#endif

#ifndef NO_write
JNIEXPORT jint JNICALL OS_NATIVE(write)
	(JNIEnv *env, jclass that, jint arg0, jbyteArray arg1, jint arg2)
{
	jbyte *lparg1=NULL;
	jint rc;
	NATIVE_ENTER(env, that, "write\n")
	if (arg1) lparg1 = (*env)->GetByteArrayElements(env, arg1, NULL);
	rc = (jint)write(arg0, (char *)lparg1, arg2);
	if (arg1) (*env)->ReleaseByteArrayElements(env, arg1, lparg1, 0);
	NATIVE_EXIT(env, that, "write\n")
	return rc;
}
#endif

#ifndef NO_xmMenuShellWidgetClass
JNIEXPORT jint JNICALL OS_NATIVE(xmMenuShellWidgetClass)
	(JNIEnv *env, jclass that)
{
	jint rc;
	NATIVE_ENTER(env, that, "xmMenuShellWidgetClass\n")
	rc = (jint)xmMenuShellWidgetClass;
	NATIVE_EXIT(env, that, "xmMenuShellWidgetClass\n")
	return rc;
}
#endif

