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
#define Visual_sizeof() sizeof(Visual)
#else
#define getVisualFields(a,b,c) NULL
#define setVisualFields(a,b,c)
#define Visual_sizeof() 0
#endif

#ifndef NO_XEvent
XEvent *getXEventFields(JNIEnv *env, jobject lpObject, XEvent *lpStruct);
void setXEventFields(JNIEnv *env, jobject lpObject, XEvent *lpStruct);
#define XEvent_sizeof() sizeof(XEvent)
#else
#define getXEventFields(a,b,c) NULL
#define setXEventFields(a,b,c)
#define XEvent_sizeof() 0
#endif

#ifndef NO_XAnyEvent
XAnyEvent *getXAnyEventFields(JNIEnv *env, jobject lpObject, XAnyEvent *lpStruct);
void setXAnyEventFields(JNIEnv *env, jobject lpObject, XAnyEvent *lpStruct);
#define XAnyEvent_sizeof() sizeof(XAnyEvent)
#else
#define getXAnyEventFields(a,b,c) NULL
#define setXAnyEventFields(a,b,c)
#define XAnyEvent_sizeof() 0
#endif

#ifndef NO_XButtonEvent
XButtonEvent *getXButtonEventFields(JNIEnv *env, jobject lpObject, XButtonEvent *lpStruct);
void setXButtonEventFields(JNIEnv *env, jobject lpObject, XButtonEvent *lpStruct);
#define XButtonEvent_sizeof() sizeof(XButtonEvent)
#else
#define getXButtonEventFields(a,b,c) NULL
#define setXButtonEventFields(a,b,c)
#define XButtonEvent_sizeof() 0
#endif

#ifndef NO_XCharStruct
XCharStruct *getXCharStructFields(JNIEnv *env, jobject lpObject, XCharStruct *lpStruct);
void setXCharStructFields(JNIEnv *env, jobject lpObject, XCharStruct *lpStruct);
#define XCharStruct_sizeof() sizeof(XCharStruct)
#else
#define getXCharStructFields(a,b,c) NULL
#define setXCharStructFields(a,b,c)
#define XCharStruct_sizeof() 0
#endif

#ifndef NO_XClientMessageEvent
XClientMessageEvent *getXClientMessageEventFields(JNIEnv *env, jobject lpObject, XClientMessageEvent *lpStruct);
void setXClientMessageEventFields(JNIEnv *env, jobject lpObject, XClientMessageEvent *lpStruct);
#define XClientMessageEvent_sizeof() sizeof(XClientMessageEvent)
#else
#define getXClientMessageEventFields(a,b,c) NULL
#define setXClientMessageEventFields(a,b,c)
#define XClientMessageEvent_sizeof() 0
#endif

#ifndef NO_XColor
XColor *getXColorFields(JNIEnv *env, jobject lpObject, XColor *lpStruct);
void setXColorFields(JNIEnv *env, jobject lpObject, XColor *lpStruct);
#define XColor_sizeof() sizeof(XColor)
#else
#define getXColorFields(a,b,c) NULL
#define setXColorFields(a,b,c)
#define XColor_sizeof() 0
#endif

#ifndef NO_XConfigureEvent
XConfigureEvent *getXConfigureEventFields(JNIEnv *env, jobject lpObject, XConfigureEvent *lpStruct);
void setXConfigureEventFields(JNIEnv *env, jobject lpObject, XConfigureEvent *lpStruct);
#define XConfigureEvent_sizeof() sizeof(XConfigureEvent)
#else
#define getXConfigureEventFields(a,b,c) NULL
#define setXConfigureEventFields(a,b,c)
#define XConfigureEvent_sizeof() 0
#endif

#ifndef NO_XCreateWindowEvent
XCreateWindowEvent *getXCreateWindowEventFields(JNIEnv *env, jobject lpObject, XCreateWindowEvent *lpStruct);
void setXCreateWindowEventFields(JNIEnv *env, jobject lpObject, XCreateWindowEvent *lpStruct);
#define XCreateWindowEvent_sizeof() sizeof(XCreateWindowEvent)
#else
#define getXCreateWindowEventFields(a,b,c) NULL
#define setXCreateWindowEventFields(a,b,c)
#define XCreateWindowEvent_sizeof() 0
#endif

#ifndef NO_XCrossingEvent
XCrossingEvent *getXCrossingEventFields(JNIEnv *env, jobject lpObject, XCrossingEvent *lpStruct);
void setXCrossingEventFields(JNIEnv *env, jobject lpObject, XCrossingEvent *lpStruct);
#define XCrossingEvent_sizeof() sizeof(XCrossingEvent)
#else
#define getXCrossingEventFields(a,b,c) NULL
#define setXCrossingEventFields(a,b,c)
#define XCrossingEvent_sizeof() 0
#endif

#ifndef NO_XDestroyWindowEvent
XDestroyWindowEvent *getXDestroyWindowEventFields(JNIEnv *env, jobject lpObject, XDestroyWindowEvent *lpStruct);
void setXDestroyWindowEventFields(JNIEnv *env, jobject lpObject, XDestroyWindowEvent *lpStruct);
#define XDestroyWindowEvent_sizeof() sizeof(XDestroyWindowEvent)
#else
#define getXDestroyWindowEventFields(a,b,c) NULL
#define setXDestroyWindowEventFields(a,b,c)
#define XDestroyWindowEvent_sizeof() 0
#endif

#ifndef NO_XExposeEvent
XExposeEvent *getXExposeEventFields(JNIEnv *env, jobject lpObject, XExposeEvent *lpStruct);
void setXExposeEventFields(JNIEnv *env, jobject lpObject, XExposeEvent *lpStruct);
#define XExposeEvent_sizeof() sizeof(XExposeEvent)
#else
#define getXExposeEventFields(a,b,c) NULL
#define setXExposeEventFields(a,b,c)
#define XExposeEvent_sizeof() 0
#endif

#ifndef NO_XFocusChangeEvent
XFocusChangeEvent *getXFocusChangeEventFields(JNIEnv *env, jobject lpObject, XFocusChangeEvent *lpStruct);
void setXFocusChangeEventFields(JNIEnv *env, jobject lpObject, XFocusChangeEvent *lpStruct);
#define XFocusChangeEvent_sizeof() sizeof(XFocusChangeEvent)
#else
#define getXFocusChangeEventFields(a,b,c) NULL
#define setXFocusChangeEventFields(a,b,c)
#define XFocusChangeEvent_sizeof() 0
#endif

#ifndef NO_XFontStruct
XFontStruct *getXFontStructFields(JNIEnv *env, jobject lpObject, XFontStruct *lpStruct);
void setXFontStructFields(JNIEnv *env, jobject lpObject, XFontStruct *lpStruct);
#define XFontStruct_sizeof() sizeof(XFontStruct)
#else
#define getXFontStructFields(a,b,c) NULL
#define setXFontStructFields(a,b,c)
#define XFontStruct_sizeof() 0
#endif

#ifndef NO_XGCValues
XGCValues *getXGCValuesFields(JNIEnv *env, jobject lpObject, XGCValues *lpStruct);
void setXGCValuesFields(JNIEnv *env, jobject lpObject, XGCValues *lpStruct);
#define XGCValues_sizeof() sizeof(XGCValues)
#else
#define getXGCValuesFields(a,b,c) NULL
#define setXGCValuesFields(a,b,c)
#define XGCValues_sizeof() 0
#endif

#ifndef NO_XIconSize
XIconSize *getXIconSizeFields(JNIEnv *env, jobject lpObject, XIconSize *lpStruct);
void setXIconSizeFields(JNIEnv *env, jobject lpObject, XIconSize *lpStruct);
#define XIconSize_sizeof() sizeof(XIconSize)
#else
#define getXIconSizeFields(a,b,c) NULL
#define setXIconSizeFields(a,b,c)
#define XIconSize_sizeof() 0
#endif

#ifndef NO_XImage
XImage *getXImageFields(JNIEnv *env, jobject lpObject, XImage *lpStruct);
void setXImageFields(JNIEnv *env, jobject lpObject, XImage *lpStruct);
#define XImage_sizeof() sizeof(XImage)
#else
#define getXImageFields(a,b,c) NULL
#define setXImageFields(a,b,c)
#define XImage_sizeof() 0
#endif

#ifndef NO_XKeyEvent
XKeyEvent *getXKeyEventFields(JNIEnv *env, jobject lpObject, XKeyEvent *lpStruct);
void setXKeyEventFields(JNIEnv *env, jobject lpObject, XKeyEvent *lpStruct);
#define XKeyEvent_sizeof() sizeof(XKeyEvent)
#else
#define getXKeyEventFields(a,b,c) NULL
#define setXKeyEventFields(a,b,c)
#define XKeyEvent_sizeof() 0
#endif

#ifndef NO_XModifierKeymap
XModifierKeymap *getXModifierKeymapFields(JNIEnv *env, jobject lpObject, XModifierKeymap *lpStruct);
void setXModifierKeymapFields(JNIEnv *env, jobject lpObject, XModifierKeymap *lpStruct);
#define XModifierKeymap_sizeof() sizeof(XModifierKeymap)
#else
#define getXModifierKeymapFields(a,b,c) NULL
#define setXModifierKeymapFields(a,b,c)
#define XModifierKeymap_sizeof() 0
#endif

#ifndef NO_XMotionEvent
XMotionEvent *getXMotionEventFields(JNIEnv *env, jobject lpObject, XMotionEvent *lpStruct);
void setXMotionEventFields(JNIEnv *env, jobject lpObject, XMotionEvent *lpStruct);
#define XMotionEvent_sizeof() sizeof(XMotionEvent)
#else
#define getXMotionEventFields(a,b,c) NULL
#define setXMotionEventFields(a,b,c)
#define XMotionEvent_sizeof() 0
#endif

#ifndef NO_XPropertyEvent
XPropertyEvent *getXPropertyEventFields(JNIEnv *env, jobject lpObject, XPropertyEvent *lpStruct);
void setXPropertyEventFields(JNIEnv *env, jobject lpObject, XPropertyEvent *lpStruct);
#define XPropertyEvent_sizeof() sizeof(XPropertyEvent)
#else
#define getXPropertyEventFields(a,b,c) NULL
#define setXPropertyEventFields(a,b,c)
#define XPropertyEvent_sizeof() 0
#endif

#ifndef NO_XRectangle
XRectangle *getXRectangleFields(JNIEnv *env, jobject lpObject, XRectangle *lpStruct);
void setXRectangleFields(JNIEnv *env, jobject lpObject, XRectangle *lpStruct);
#define XRectangle_sizeof() sizeof(XRectangle)
#else
#define getXRectangleFields(a,b,c) NULL
#define setXRectangleFields(a,b,c)
#define XRectangle_sizeof() 0
#endif

#ifndef NO_XReparentEvent
XReparentEvent *getXReparentEventFields(JNIEnv *env, jobject lpObject, XReparentEvent *lpStruct);
void setXReparentEventFields(JNIEnv *env, jobject lpObject, XReparentEvent *lpStruct);
#define XReparentEvent_sizeof() sizeof(XReparentEvent)
#else
#define getXReparentEventFields(a,b,c) NULL
#define setXReparentEventFields(a,b,c)
#define XReparentEvent_sizeof() 0
#endif

#ifndef NO_XSetWindowAttributes
XSetWindowAttributes *getXSetWindowAttributesFields(JNIEnv *env, jobject lpObject, XSetWindowAttributes *lpStruct);
void setXSetWindowAttributesFields(JNIEnv *env, jobject lpObject, XSetWindowAttributes *lpStruct);
#define XSetWindowAttributes_sizeof() sizeof(XSetWindowAttributes)
#else
#define getXSetWindowAttributesFields(a,b,c) NULL
#define setXSetWindowAttributesFields(a,b,c)
#define XSetWindowAttributes_sizeof() 0
#endif

#ifndef NO_XTextProperty
XTextProperty *getXTextPropertyFields(JNIEnv *env, jobject lpObject, XTextProperty *lpStruct);
void setXTextPropertyFields(JNIEnv *env, jobject lpObject, XTextProperty *lpStruct);
#define XTextProperty_sizeof() sizeof(XTextProperty)
#else
#define getXTextPropertyFields(a,b,c) NULL
#define setXTextPropertyFields(a,b,c)
#define XTextProperty_sizeof() 0
#endif

#ifndef NO_XWindowAttributes
XWindowAttributes *getXWindowAttributesFields(JNIEnv *env, jobject lpObject, XWindowAttributes *lpStruct);
void setXWindowAttributesFields(JNIEnv *env, jobject lpObject, XWindowAttributes *lpStruct);
#define XWindowAttributes_sizeof() sizeof(XWindowAttributes)
#else
#define getXWindowAttributesFields(a,b,c) NULL
#define setXWindowAttributesFields(a,b,c)
#define XWindowAttributes_sizeof() 0
#endif

#ifndef NO_XWindowChanges
XWindowChanges *getXWindowChangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpStruct);
void setXWindowChangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpStruct);
#define XWindowChanges_sizeof() sizeof(XWindowChanges)
#else
#define getXWindowChangesFields(a,b,c) NULL
#define setXWindowChangesFields(a,b,c)
#define XWindowChanges_sizeof() 0
#endif

#ifndef NO_XineramaScreenInfo
XineramaScreenInfo *getXineramaScreenInfoFields(JNIEnv *env, jobject lpObject, XineramaScreenInfo *lpStruct);
void setXineramaScreenInfoFields(JNIEnv *env, jobject lpObject, XineramaScreenInfo *lpStruct);
#define XineramaScreenInfo_sizeof() sizeof(XineramaScreenInfo)
#else
#define getXineramaScreenInfoFields(a,b,c) NULL
#define setXineramaScreenInfoFields(a,b,c)
#define XineramaScreenInfo_sizeof() 0
#endif

#ifndef NO_XmAnyCallbackStruct
XmAnyCallbackStruct *getXmAnyCallbackStructFields(JNIEnv *env, jobject lpObject, XmAnyCallbackStruct *lpStruct);
void setXmAnyCallbackStructFields(JNIEnv *env, jobject lpObject, XmAnyCallbackStruct *lpStruct);
#define XmAnyCallbackStruct_sizeof() sizeof(XmAnyCallbackStruct)
#else
#define getXmAnyCallbackStructFields(a,b,c) NULL
#define setXmAnyCallbackStructFields(a,b,c)
#define XmAnyCallbackStruct_sizeof() 0
#endif

#ifndef NO_XmDragProcCallbackStruct
XmDragProcCallbackStruct *getXmDragProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDragProcCallbackStruct *lpStruct);
void setXmDragProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDragProcCallbackStruct *lpStruct);
#define XmDragProcCallbackStruct_sizeof() sizeof(XmDragProcCallbackStruct)
#else
#define getXmDragProcCallbackStructFields(a,b,c) NULL
#define setXmDragProcCallbackStructFields(a,b,c)
#define XmDragProcCallbackStruct_sizeof() 0
#endif

#ifndef NO_XmDropFinishCallbackStruct
XmDropFinishCallbackStruct *getXmDropFinishCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropFinishCallbackStruct *lpStruct);
void setXmDropFinishCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropFinishCallbackStruct *lpStruct);
#define XmDropFinishCallbackStruct_sizeof() sizeof(XmDropFinishCallbackStruct)
#else
#define getXmDropFinishCallbackStructFields(a,b,c) NULL
#define setXmDropFinishCallbackStructFields(a,b,c)
#define XmDropFinishCallbackStruct_sizeof() 0
#endif

#ifndef NO_XmDropProcCallbackStruct
XmDropProcCallbackStruct *getXmDropProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropProcCallbackStruct *lpStruct);
void setXmDropProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropProcCallbackStruct *lpStruct);
#define XmDropProcCallbackStruct_sizeof() sizeof(XmDropProcCallbackStruct)
#else
#define getXmDropProcCallbackStructFields(a,b,c) NULL
#define setXmDropProcCallbackStructFields(a,b,c)
#define XmDropProcCallbackStruct_sizeof() 0
#endif

#ifndef NO_XmTextBlockRec
XmTextBlockRec *getXmTextBlockRecFields(JNIEnv *env, jobject lpObject, XmTextBlockRec *lpStruct);
void setXmTextBlockRecFields(JNIEnv *env, jobject lpObject, XmTextBlockRec *lpStruct);
#define XmTextBlockRec_sizeof() sizeof(XmTextBlockRec)
#else
#define getXmTextBlockRecFields(a,b,c) NULL
#define setXmTextBlockRecFields(a,b,c)
#define XmTextBlockRec_sizeof() 0
#endif

#ifndef NO_XmTextVerifyCallbackStruct
XmTextVerifyCallbackStruct *getXmTextVerifyCallbackStructFields(JNIEnv *env, jobject lpObject, XmTextVerifyCallbackStruct *lpStruct);
void setXmTextVerifyCallbackStructFields(JNIEnv *env, jobject lpObject, XmTextVerifyCallbackStruct *lpStruct);
#define XmTextVerifyCallbackStruct_sizeof() sizeof(XmTextVerifyCallbackStruct)
#else
#define getXmTextVerifyCallbackStructFields(a,b,c) NULL
#define setXmTextVerifyCallbackStructFields(a,b,c)
#define XmTextVerifyCallbackStruct_sizeof() 0
#endif

#ifndef NO_XtWidgetGeometry
XtWidgetGeometry *getXtWidgetGeometryFields(JNIEnv *env, jobject lpObject, XtWidgetGeometry *lpStruct);
void setXtWidgetGeometryFields(JNIEnv *env, jobject lpObject, XtWidgetGeometry *lpStruct);
#define XtWidgetGeometry_sizeof() sizeof(XtWidgetGeometry)
#else
#define getXtWidgetGeometryFields(a,b,c) NULL
#define setXtWidgetGeometryFields(a,b,c)
#define XtWidgetGeometry_sizeof() 0
#endif

