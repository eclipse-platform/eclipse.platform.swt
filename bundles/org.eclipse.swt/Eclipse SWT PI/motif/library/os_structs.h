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

#include "os.h"

#ifndef NO_Visual
Visual *getVisualFields(JNIEnv *env, jobject lpObject, Visual *lpStruct);
void setVisualFields(JNIEnv *env, jobject lpObject, Visual *lpStruct);
#else
#define getVisualFields(a,b,c) NULL
#define setVisualFields(a,b,c)
#endif

#ifndef NO_XEvent
XEvent *getXEventFields(JNIEnv *env, jobject lpObject, XEvent *lpStruct);
void setXEventFields(JNIEnv *env, jobject lpObject, XEvent *lpStruct);
#else
#define getXEventFields(a,b,c) NULL
#define setXEventFields(a,b,c)
#endif

#ifndef NO_XAnyEvent
XAnyEvent *getXAnyEventFields(JNIEnv *env, jobject lpObject, XAnyEvent *lpStruct);
void setXAnyEventFields(JNIEnv *env, jobject lpObject, XAnyEvent *lpStruct);
#else
#define getXAnyEventFields(a,b,c) NULL
#define setXAnyEventFields(a,b,c)
#endif

#ifndef NO_XButtonEvent
XButtonEvent *getXButtonEventFields(JNIEnv *env, jobject lpObject, XButtonEvent *lpStruct);
void setXButtonEventFields(JNIEnv *env, jobject lpObject, XButtonEvent *lpStruct);
#else
#define getXButtonEventFields(a,b,c) NULL
#define setXButtonEventFields(a,b,c)
#endif

#ifndef NO_XCharStruct
XCharStruct *getXCharStructFields(JNIEnv *env, jobject lpObject, XCharStruct *lpStruct);
void setXCharStructFields(JNIEnv *env, jobject lpObject, XCharStruct *lpStruct);
#else
#define getXCharStructFields(a,b,c) NULL
#define setXCharStructFields(a,b,c)
#endif

#ifndef NO_XColor
XColor *getXColorFields(JNIEnv *env, jobject lpObject, XColor *lpStruct);
void setXColorFields(JNIEnv *env, jobject lpObject, XColor *lpStruct);
#else
#define getXColorFields(a,b,c) NULL
#define setXColorFields(a,b,c)
#endif

#ifndef NO_XConfigureEvent
XConfigureEvent *getXConfigureEventFields(JNIEnv *env, jobject lpObject, XConfigureEvent *lpStruct);
void setXConfigureEventFields(JNIEnv *env, jobject lpObject, XConfigureEvent *lpStruct);
#else
#define getXConfigureEventFields(a,b,c) NULL
#define setXConfigureEventFields(a,b,c)
#endif

#ifndef NO_XCrossingEvent
XCrossingEvent *getXCrossingEventFields(JNIEnv *env, jobject lpObject, XCrossingEvent *lpStruct);
void setXCrossingEventFields(JNIEnv *env, jobject lpObject, XCrossingEvent *lpStruct);
#else
#define getXCrossingEventFields(a,b,c) NULL
#define setXCrossingEventFields(a,b,c)
#endif

#ifndef NO_XExposeEvent
XExposeEvent *getXExposeEventFields(JNIEnv *env, jobject lpObject, XExposeEvent *lpStruct);
void setXExposeEventFields(JNIEnv *env, jobject lpObject, XExposeEvent *lpStruct);
#else
#define getXExposeEventFields(a,b,c) NULL
#define setXExposeEventFields(a,b,c)
#endif

#ifndef NO_XFocusChangeEvent
XFocusChangeEvent *getXFocusChangeEventFields(JNIEnv *env, jobject lpObject, XFocusChangeEvent *lpStruct);
void setXFocusChangeEventFields(JNIEnv *env, jobject lpObject, XFocusChangeEvent *lpStruct);
#else
#define getXFocusChangeEventFields(a,b,c) NULL
#define setXFocusChangeEventFields(a,b,c)
#endif

#ifndef NO_XFontStruct
XFontStruct *getXFontStructFields(JNIEnv *env, jobject lpObject, XFontStruct *lpStruct);
void setXFontStructFields(JNIEnv *env, jobject lpObject, XFontStruct *lpStruct);
#else
#define getXFontStructFields(a,b,c) NULL
#define setXFontStructFields(a,b,c)
#endif

#ifndef NO_XGCValues
XGCValues *getXGCValuesFields(JNIEnv *env, jobject lpObject, XGCValues *lpStruct);
void setXGCValuesFields(JNIEnv *env, jobject lpObject, XGCValues *lpStruct);
#else
#define getXGCValuesFields(a,b,c) NULL
#define setXGCValuesFields(a,b,c)
#endif

#ifndef NO_XImage
XImage *getXImageFields(JNIEnv *env, jobject lpObject, XImage *lpStruct);
void setXImageFields(JNIEnv *env, jobject lpObject, XImage *lpStruct);
#else
#define getXImageFields(a,b,c) NULL
#define setXImageFields(a,b,c)
#endif

#ifndef NO_XKeyEvent
XKeyEvent *getXKeyEventFields(JNIEnv *env, jobject lpObject, XKeyEvent *lpStruct);
void setXKeyEventFields(JNIEnv *env, jobject lpObject, XKeyEvent *lpStruct);
#else
#define getXKeyEventFields(a,b,c) NULL
#define setXKeyEventFields(a,b,c)
#endif

#ifndef NO_XMotionEvent
XMotionEvent *getXMotionEventFields(JNIEnv *env, jobject lpObject, XMotionEvent *lpStruct);
void setXMotionEventFields(JNIEnv *env, jobject lpObject, XMotionEvent *lpStruct);
#else
#define getXMotionEventFields(a,b,c) NULL
#define setXMotionEventFields(a,b,c)
#endif

#ifndef NO_XRectangle
XRectangle *getXRectangleFields(JNIEnv *env, jobject lpObject, XRectangle *lpStruct);
void setXRectangleFields(JNIEnv *env, jobject lpObject, XRectangle *lpStruct);
#else
#define getXRectangleFields(a,b,c) NULL
#define setXRectangleFields(a,b,c)
#endif

#ifndef NO_XSetWindowAttributes
XSetWindowAttributes *getXSetWindowAttributesFields(JNIEnv *env, jobject lpObject, XSetWindowAttributes *lpStruct);
void setXSetWindowAttributesFields(JNIEnv *env, jobject lpObject, XSetWindowAttributes *lpStruct);
#else
#define getXSetWindowAttributesFields(a,b,c) NULL
#define setXSetWindowAttributesFields(a,b,c)
#endif

#ifndef NO_XTextProperty
XTextProperty *getXTextPropertyFields(JNIEnv *env, jobject lpObject, XTextProperty *lpStruct);
void setXTextPropertyFields(JNIEnv *env, jobject lpObject, XTextProperty *lpStruct);
#else
#define getXTextPropertyFields(a,b,c) NULL
#define setXTextPropertyFields(a,b,c)
#endif

#ifndef NO_XWindowAttributes
XWindowAttributes *getXWindowAttributesFields(JNIEnv *env, jobject lpObject, XWindowAttributes *lpStruct);
void setXWindowAttributesFields(JNIEnv *env, jobject lpObject, XWindowAttributes *lpStruct);
#else
#define getXWindowAttributesFields(a,b,c) NULL
#define setXWindowAttributesFields(a,b,c)
#endif

#ifndef NO_XWindowChanges
XWindowChanges *getXWindowChangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpStruct);
void setXWindowChangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpStruct);
#else
#define getXWindowChangesFields(a,b,c) NULL
#define setXWindowChangesFields(a,b,c)
#endif

#ifndef NO_XineramaScreenInfo
XineramaScreenInfo *getXineramaScreenInfoFields(JNIEnv *env, jobject lpObject, XineramaScreenInfo *lpStruct);
void setXineramaScreenInfoFields(JNIEnv *env, jobject lpObject, XineramaScreenInfo *lpStruct);
#else
#define getXineramaScreenInfoFields(a,b,c) NULL
#define setXineramaScreenInfoFields(a,b,c)
#endif

#ifndef NO_XmAnyCallbackStruct
XmAnyCallbackStruct *getXmAnyCallbackStructFields(JNIEnv *env, jobject lpObject, XmAnyCallbackStruct *lpStruct);
void setXmAnyCallbackStructFields(JNIEnv *env, jobject lpObject, XmAnyCallbackStruct *lpStruct);
#else
#define getXmAnyCallbackStructFields(a,b,c) NULL
#define setXmAnyCallbackStructFields(a,b,c)
#endif

#ifndef NO_XmDragProcCallbackStruct
XmDragProcCallbackStruct *getXmDragProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDragProcCallbackStruct *lpStruct);
void setXmDragProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDragProcCallbackStruct *lpStruct);
#else
#define getXmDragProcCallbackStructFields(a,b,c) NULL
#define setXmDragProcCallbackStructFields(a,b,c)
#endif

#ifndef NO_XmDropFinishCallbackStruct
XmDropFinishCallbackStruct *getXmDropFinishCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropFinishCallbackStruct *lpStruct);
void setXmDropFinishCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropFinishCallbackStruct *lpStruct);
#else
#define getXmDropFinishCallbackStructFields(a,b,c) NULL
#define setXmDropFinishCallbackStructFields(a,b,c)
#endif

#ifndef NO_XmDropProcCallbackStruct
XmDropProcCallbackStruct *getXmDropProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropProcCallbackStruct *lpStruct);
void setXmDropProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropProcCallbackStruct *lpStruct);
#else
#define getXmDropProcCallbackStructFields(a,b,c) NULL
#define setXmDropProcCallbackStructFields(a,b,c)
#endif

#ifndef NO_XmTextBlockRec
XmTextBlockRec *getXmTextBlockRecFields(JNIEnv *env, jobject lpObject, XmTextBlockRec *lpStruct);
void setXmTextBlockRecFields(JNIEnv *env, jobject lpObject, XmTextBlockRec *lpStruct);
#else
#define getXmTextBlockRecFields(a,b,c) NULL
#define setXmTextBlockRecFields(a,b,c)
#endif

#ifndef NO_XmTextVerifyCallbackStruct
XmTextVerifyCallbackStruct *getXmTextVerifyCallbackStructFields(JNIEnv *env, jobject lpObject, XmTextVerifyCallbackStruct *lpStruct);
void setXmTextVerifyCallbackStructFields(JNIEnv *env, jobject lpObject, XmTextVerifyCallbackStruct *lpStruct);
#else
#define getXmTextVerifyCallbackStructFields(a,b,c) NULL
#define setXmTextVerifyCallbackStructFields(a,b,c)
#endif

#ifndef NO_XtWidgetGeometry
XtWidgetGeometry *getXtWidgetGeometryFields(JNIEnv *env, jobject lpObject, XtWidgetGeometry *lpStruct);
void setXtWidgetGeometryFields(JNIEnv *env, jobject lpObject, XtWidgetGeometry *lpStruct);
#else
#define getXtWidgetGeometryFields(a,b,c) NULL
#define setXtWidgetGeometryFields(a,b,c)
#endif

