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

#include "os.h"

#ifndef NO_Visual
void cacheVisualFields(JNIEnv *env, jobject lpObject);
Visual *getVisualFields(JNIEnv *env, jobject lpObject, Visual *lpStruct);
void setVisualFields(JNIEnv *env, jobject lpObject, Visual *lpStruct);
#define Visual_sizeof() sizeof(Visual)
#else
#define cacheVisualFields(a,b)
#define getVisualFields(a,b,c) NULL
#define setVisualFields(a,b,c)
#define Visual_sizeof() 0
#endif

#ifndef NO_XAnyEvent
void cacheXAnyEventFields(JNIEnv *env, jobject lpObject);
XAnyEvent *getXAnyEventFields(JNIEnv *env, jobject lpObject, XAnyEvent *lpStruct);
void setXAnyEventFields(JNIEnv *env, jobject lpObject, XAnyEvent *lpStruct);
#define XAnyEvent_sizeof() sizeof(XAnyEvent)
#else
#define cacheXAnyEventFields(a,b)
#define getXAnyEventFields(a,b,c) NULL
#define setXAnyEventFields(a,b,c)
#define XAnyEvent_sizeof() 0
#endif

#ifndef NO_XButtonEvent
void cacheXButtonEventFields(JNIEnv *env, jobject lpObject);
XButtonEvent *getXButtonEventFields(JNIEnv *env, jobject lpObject, XButtonEvent *lpStruct);
void setXButtonEventFields(JNIEnv *env, jobject lpObject, XButtonEvent *lpStruct);
#define XButtonEvent_sizeof() sizeof(XButtonEvent)
#else
#define cacheXButtonEventFields(a,b)
#define getXButtonEventFields(a,b,c) NULL
#define setXButtonEventFields(a,b,c)
#define XButtonEvent_sizeof() 0
#endif

#ifndef NO_XCharStruct
void cacheXCharStructFields(JNIEnv *env, jobject lpObject);
XCharStruct *getXCharStructFields(JNIEnv *env, jobject lpObject, XCharStruct *lpStruct);
void setXCharStructFields(JNIEnv *env, jobject lpObject, XCharStruct *lpStruct);
#define XCharStruct_sizeof() sizeof(XCharStruct)
#else
#define cacheXCharStructFields(a,b)
#define getXCharStructFields(a,b,c) NULL
#define setXCharStructFields(a,b,c)
#define XCharStruct_sizeof() 0
#endif

#ifndef NO_XClientMessageEvent
void cacheXClientMessageEventFields(JNIEnv *env, jobject lpObject);
XClientMessageEvent *getXClientMessageEventFields(JNIEnv *env, jobject lpObject, XClientMessageEvent *lpStruct);
void setXClientMessageEventFields(JNIEnv *env, jobject lpObject, XClientMessageEvent *lpStruct);
#define XClientMessageEvent_sizeof() sizeof(XClientMessageEvent)
#else
#define cacheXClientMessageEventFields(a,b)
#define getXClientMessageEventFields(a,b,c) NULL
#define setXClientMessageEventFields(a,b,c)
#define XClientMessageEvent_sizeof() 0
#endif

#ifndef NO_XColor
void cacheXColorFields(JNIEnv *env, jobject lpObject);
XColor *getXColorFields(JNIEnv *env, jobject lpObject, XColor *lpStruct);
void setXColorFields(JNIEnv *env, jobject lpObject, XColor *lpStruct);
#define XColor_sizeof() sizeof(XColor)
#else
#define cacheXColorFields(a,b)
#define getXColorFields(a,b,c) NULL
#define setXColorFields(a,b,c)
#define XColor_sizeof() 0
#endif

#ifndef NO_XConfigureEvent
void cacheXConfigureEventFields(JNIEnv *env, jobject lpObject);
XConfigureEvent *getXConfigureEventFields(JNIEnv *env, jobject lpObject, XConfigureEvent *lpStruct);
void setXConfigureEventFields(JNIEnv *env, jobject lpObject, XConfigureEvent *lpStruct);
#define XConfigureEvent_sizeof() sizeof(XConfigureEvent)
#else
#define cacheXConfigureEventFields(a,b)
#define getXConfigureEventFields(a,b,c) NULL
#define setXConfigureEventFields(a,b,c)
#define XConfigureEvent_sizeof() 0
#endif

#ifndef NO_XCreateWindowEvent
void cacheXCreateWindowEventFields(JNIEnv *env, jobject lpObject);
XCreateWindowEvent *getXCreateWindowEventFields(JNIEnv *env, jobject lpObject, XCreateWindowEvent *lpStruct);
void setXCreateWindowEventFields(JNIEnv *env, jobject lpObject, XCreateWindowEvent *lpStruct);
#define XCreateWindowEvent_sizeof() sizeof(XCreateWindowEvent)
#else
#define cacheXCreateWindowEventFields(a,b)
#define getXCreateWindowEventFields(a,b,c) NULL
#define setXCreateWindowEventFields(a,b,c)
#define XCreateWindowEvent_sizeof() 0
#endif

#ifndef NO_XCrossingEvent
void cacheXCrossingEventFields(JNIEnv *env, jobject lpObject);
XCrossingEvent *getXCrossingEventFields(JNIEnv *env, jobject lpObject, XCrossingEvent *lpStruct);
void setXCrossingEventFields(JNIEnv *env, jobject lpObject, XCrossingEvent *lpStruct);
#define XCrossingEvent_sizeof() sizeof(XCrossingEvent)
#else
#define cacheXCrossingEventFields(a,b)
#define getXCrossingEventFields(a,b,c) NULL
#define setXCrossingEventFields(a,b,c)
#define XCrossingEvent_sizeof() 0
#endif

#ifndef NO_XDestroyWindowEvent
void cacheXDestroyWindowEventFields(JNIEnv *env, jobject lpObject);
XDestroyWindowEvent *getXDestroyWindowEventFields(JNIEnv *env, jobject lpObject, XDestroyWindowEvent *lpStruct);
void setXDestroyWindowEventFields(JNIEnv *env, jobject lpObject, XDestroyWindowEvent *lpStruct);
#define XDestroyWindowEvent_sizeof() sizeof(XDestroyWindowEvent)
#else
#define cacheXDestroyWindowEventFields(a,b)
#define getXDestroyWindowEventFields(a,b,c) NULL
#define setXDestroyWindowEventFields(a,b,c)
#define XDestroyWindowEvent_sizeof() 0
#endif

#ifndef NO_XEvent
void cacheXEventFields(JNIEnv *env, jobject lpObject);
XEvent *getXEventFields(JNIEnv *env, jobject lpObject, XEvent *lpStruct);
void setXEventFields(JNIEnv *env, jobject lpObject, XEvent *lpStruct);
#define XEvent_sizeof() sizeof(XEvent)
#else
#define cacheXEventFields(a,b)
#define getXEventFields(a,b,c) NULL
#define setXEventFields(a,b,c)
#define XEvent_sizeof() 0
#endif

#ifndef NO_XExposeEvent
void cacheXExposeEventFields(JNIEnv *env, jobject lpObject);
XExposeEvent *getXExposeEventFields(JNIEnv *env, jobject lpObject, XExposeEvent *lpStruct);
void setXExposeEventFields(JNIEnv *env, jobject lpObject, XExposeEvent *lpStruct);
#define XExposeEvent_sizeof() sizeof(XExposeEvent)
#else
#define cacheXExposeEventFields(a,b)
#define getXExposeEventFields(a,b,c) NULL
#define setXExposeEventFields(a,b,c)
#define XExposeEvent_sizeof() 0
#endif

#ifndef NO_XFocusChangeEvent
void cacheXFocusChangeEventFields(JNIEnv *env, jobject lpObject);
XFocusChangeEvent *getXFocusChangeEventFields(JNIEnv *env, jobject lpObject, XFocusChangeEvent *lpStruct);
void setXFocusChangeEventFields(JNIEnv *env, jobject lpObject, XFocusChangeEvent *lpStruct);
#define XFocusChangeEvent_sizeof() sizeof(XFocusChangeEvent)
#else
#define cacheXFocusChangeEventFields(a,b)
#define getXFocusChangeEventFields(a,b,c) NULL
#define setXFocusChangeEventFields(a,b,c)
#define XFocusChangeEvent_sizeof() 0
#endif

#ifndef NO_XFontStruct
void cacheXFontStructFields(JNIEnv *env, jobject lpObject);
XFontStruct *getXFontStructFields(JNIEnv *env, jobject lpObject, XFontStruct *lpStruct);
void setXFontStructFields(JNIEnv *env, jobject lpObject, XFontStruct *lpStruct);
#define XFontStruct_sizeof() sizeof(XFontStruct)
#else
#define cacheXFontStructFields(a,b)
#define getXFontStructFields(a,b,c) NULL
#define setXFontStructFields(a,b,c)
#define XFontStruct_sizeof() 0
#endif

#ifndef NO_XGCValues
void cacheXGCValuesFields(JNIEnv *env, jobject lpObject);
XGCValues *getXGCValuesFields(JNIEnv *env, jobject lpObject, XGCValues *lpStruct);
void setXGCValuesFields(JNIEnv *env, jobject lpObject, XGCValues *lpStruct);
#define XGCValues_sizeof() sizeof(XGCValues)
#else
#define cacheXGCValuesFields(a,b)
#define getXGCValuesFields(a,b,c) NULL
#define setXGCValuesFields(a,b,c)
#define XGCValues_sizeof() 0
#endif

#ifndef NO_XIconSize
void cacheXIconSizeFields(JNIEnv *env, jobject lpObject);
XIconSize *getXIconSizeFields(JNIEnv *env, jobject lpObject, XIconSize *lpStruct);
void setXIconSizeFields(JNIEnv *env, jobject lpObject, XIconSize *lpStruct);
#define XIconSize_sizeof() sizeof(XIconSize)
#else
#define cacheXIconSizeFields(a,b)
#define getXIconSizeFields(a,b,c) NULL
#define setXIconSizeFields(a,b,c)
#define XIconSize_sizeof() 0
#endif

#ifndef NO_XImage
void cacheXImageFields(JNIEnv *env, jobject lpObject);
XImage *getXImageFields(JNIEnv *env, jobject lpObject, XImage *lpStruct);
void setXImageFields(JNIEnv *env, jobject lpObject, XImage *lpStruct);
#define XImage_sizeof() sizeof(XImage)
#else
#define cacheXImageFields(a,b)
#define getXImageFields(a,b,c) NULL
#define setXImageFields(a,b,c)
#define XImage_sizeof() 0
#endif

#ifndef NO_XKeyEvent
void cacheXKeyEventFields(JNIEnv *env, jobject lpObject);
XKeyEvent *getXKeyEventFields(JNIEnv *env, jobject lpObject, XKeyEvent *lpStruct);
void setXKeyEventFields(JNIEnv *env, jobject lpObject, XKeyEvent *lpStruct);
#define XKeyEvent_sizeof() sizeof(XKeyEvent)
#else
#define cacheXKeyEventFields(a,b)
#define getXKeyEventFields(a,b,c) NULL
#define setXKeyEventFields(a,b,c)
#define XKeyEvent_sizeof() 0
#endif

#ifndef NO_XModifierKeymap
void cacheXModifierKeymapFields(JNIEnv *env, jobject lpObject);
XModifierKeymap *getXModifierKeymapFields(JNIEnv *env, jobject lpObject, XModifierKeymap *lpStruct);
void setXModifierKeymapFields(JNIEnv *env, jobject lpObject, XModifierKeymap *lpStruct);
#define XModifierKeymap_sizeof() sizeof(XModifierKeymap)
#else
#define cacheXModifierKeymapFields(a,b)
#define getXModifierKeymapFields(a,b,c) NULL
#define setXModifierKeymapFields(a,b,c)
#define XModifierKeymap_sizeof() 0
#endif

#ifndef NO_XMotionEvent
void cacheXMotionEventFields(JNIEnv *env, jobject lpObject);
XMotionEvent *getXMotionEventFields(JNIEnv *env, jobject lpObject, XMotionEvent *lpStruct);
void setXMotionEventFields(JNIEnv *env, jobject lpObject, XMotionEvent *lpStruct);
#define XMotionEvent_sizeof() sizeof(XMotionEvent)
#else
#define cacheXMotionEventFields(a,b)
#define getXMotionEventFields(a,b,c) NULL
#define setXMotionEventFields(a,b,c)
#define XMotionEvent_sizeof() 0
#endif

#ifndef NO_XPropertyEvent
void cacheXPropertyEventFields(JNIEnv *env, jobject lpObject);
XPropertyEvent *getXPropertyEventFields(JNIEnv *env, jobject lpObject, XPropertyEvent *lpStruct);
void setXPropertyEventFields(JNIEnv *env, jobject lpObject, XPropertyEvent *lpStruct);
#define XPropertyEvent_sizeof() sizeof(XPropertyEvent)
#else
#define cacheXPropertyEventFields(a,b)
#define getXPropertyEventFields(a,b,c) NULL
#define setXPropertyEventFields(a,b,c)
#define XPropertyEvent_sizeof() 0
#endif

#ifndef NO_XRectangle
void cacheXRectangleFields(JNIEnv *env, jobject lpObject);
XRectangle *getXRectangleFields(JNIEnv *env, jobject lpObject, XRectangle *lpStruct);
void setXRectangleFields(JNIEnv *env, jobject lpObject, XRectangle *lpStruct);
#define XRectangle_sizeof() sizeof(XRectangle)
#else
#define cacheXRectangleFields(a,b)
#define getXRectangleFields(a,b,c) NULL
#define setXRectangleFields(a,b,c)
#define XRectangle_sizeof() 0
#endif

#ifndef NO_XRenderPictureAttributes
void cacheXRenderPictureAttributesFields(JNIEnv *env, jobject lpObject);
XRenderPictureAttributes *getXRenderPictureAttributesFields(JNIEnv *env, jobject lpObject, XRenderPictureAttributes *lpStruct);
void setXRenderPictureAttributesFields(JNIEnv *env, jobject lpObject, XRenderPictureAttributes *lpStruct);
#define XRenderPictureAttributes_sizeof() sizeof(XRenderPictureAttributes)
#else
#define cacheXRenderPictureAttributesFields(a,b)
#define getXRenderPictureAttributesFields(a,b,c) NULL
#define setXRenderPictureAttributesFields(a,b,c)
#define XRenderPictureAttributes_sizeof() 0
#endif

#ifndef NO_XReparentEvent
void cacheXReparentEventFields(JNIEnv *env, jobject lpObject);
XReparentEvent *getXReparentEventFields(JNIEnv *env, jobject lpObject, XReparentEvent *lpStruct);
void setXReparentEventFields(JNIEnv *env, jobject lpObject, XReparentEvent *lpStruct);
#define XReparentEvent_sizeof() sizeof(XReparentEvent)
#else
#define cacheXReparentEventFields(a,b)
#define getXReparentEventFields(a,b,c) NULL
#define setXReparentEventFields(a,b,c)
#define XReparentEvent_sizeof() 0
#endif

#ifndef NO_XSetWindowAttributes
void cacheXSetWindowAttributesFields(JNIEnv *env, jobject lpObject);
XSetWindowAttributes *getXSetWindowAttributesFields(JNIEnv *env, jobject lpObject, XSetWindowAttributes *lpStruct);
void setXSetWindowAttributesFields(JNIEnv *env, jobject lpObject, XSetWindowAttributes *lpStruct);
#define XSetWindowAttributes_sizeof() sizeof(XSetWindowAttributes)
#else
#define cacheXSetWindowAttributesFields(a,b)
#define getXSetWindowAttributesFields(a,b,c) NULL
#define setXSetWindowAttributesFields(a,b,c)
#define XSetWindowAttributes_sizeof() 0
#endif

#ifndef NO_XSizeHints
void cacheXSizeHintsFields(JNIEnv *env, jobject lpObject);
XSizeHints *getXSizeHintsFields(JNIEnv *env, jobject lpObject, XSizeHints *lpStruct);
void setXSizeHintsFields(JNIEnv *env, jobject lpObject, XSizeHints *lpStruct);
#define XSizeHints_sizeof() sizeof(XSizeHints)
#else
#define cacheXSizeHintsFields(a,b)
#define getXSizeHintsFields(a,b,c) NULL
#define setXSizeHintsFields(a,b,c)
#define XSizeHints_sizeof() 0
#endif

#ifndef NO_XTextProperty
void cacheXTextPropertyFields(JNIEnv *env, jobject lpObject);
XTextProperty *getXTextPropertyFields(JNIEnv *env, jobject lpObject, XTextProperty *lpStruct);
void setXTextPropertyFields(JNIEnv *env, jobject lpObject, XTextProperty *lpStruct);
#define XTextProperty_sizeof() sizeof(XTextProperty)
#else
#define cacheXTextPropertyFields(a,b)
#define getXTextPropertyFields(a,b,c) NULL
#define setXTextPropertyFields(a,b,c)
#define XTextProperty_sizeof() 0
#endif

#ifndef NO_XWindowAttributes
void cacheXWindowAttributesFields(JNIEnv *env, jobject lpObject);
XWindowAttributes *getXWindowAttributesFields(JNIEnv *env, jobject lpObject, XWindowAttributes *lpStruct);
void setXWindowAttributesFields(JNIEnv *env, jobject lpObject, XWindowAttributes *lpStruct);
#define XWindowAttributes_sizeof() sizeof(XWindowAttributes)
#else
#define cacheXWindowAttributesFields(a,b)
#define getXWindowAttributesFields(a,b,c) NULL
#define setXWindowAttributesFields(a,b,c)
#define XWindowAttributes_sizeof() 0
#endif

#ifndef NO_XWindowChanges
void cacheXWindowChangesFields(JNIEnv *env, jobject lpObject);
XWindowChanges *getXWindowChangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpStruct);
void setXWindowChangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpStruct);
#define XWindowChanges_sizeof() sizeof(XWindowChanges)
#else
#define cacheXWindowChangesFields(a,b)
#define getXWindowChangesFields(a,b,c) NULL
#define setXWindowChangesFields(a,b,c)
#define XWindowChanges_sizeof() 0
#endif

#ifndef NO_XineramaScreenInfo
void cacheXineramaScreenInfoFields(JNIEnv *env, jobject lpObject);
XineramaScreenInfo *getXineramaScreenInfoFields(JNIEnv *env, jobject lpObject, XineramaScreenInfo *lpStruct);
void setXineramaScreenInfoFields(JNIEnv *env, jobject lpObject, XineramaScreenInfo *lpStruct);
#define XineramaScreenInfo_sizeof() sizeof(XineramaScreenInfo)
#else
#define cacheXineramaScreenInfoFields(a,b)
#define getXineramaScreenInfoFields(a,b,c) NULL
#define setXineramaScreenInfoFields(a,b,c)
#define XineramaScreenInfo_sizeof() 0
#endif

#ifndef NO_XmAnyCallbackStruct
void cacheXmAnyCallbackStructFields(JNIEnv *env, jobject lpObject);
XmAnyCallbackStruct *getXmAnyCallbackStructFields(JNIEnv *env, jobject lpObject, XmAnyCallbackStruct *lpStruct);
void setXmAnyCallbackStructFields(JNIEnv *env, jobject lpObject, XmAnyCallbackStruct *lpStruct);
#define XmAnyCallbackStruct_sizeof() sizeof(XmAnyCallbackStruct)
#else
#define cacheXmAnyCallbackStructFields(a,b)
#define getXmAnyCallbackStructFields(a,b,c) NULL
#define setXmAnyCallbackStructFields(a,b,c)
#define XmAnyCallbackStruct_sizeof() 0
#endif

#ifndef NO_XmDragProcCallbackStruct
void cacheXmDragProcCallbackStructFields(JNIEnv *env, jobject lpObject);
XmDragProcCallbackStruct *getXmDragProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDragProcCallbackStruct *lpStruct);
void setXmDragProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDragProcCallbackStruct *lpStruct);
#define XmDragProcCallbackStruct_sizeof() sizeof(XmDragProcCallbackStruct)
#else
#define cacheXmDragProcCallbackStructFields(a,b)
#define getXmDragProcCallbackStructFields(a,b,c) NULL
#define setXmDragProcCallbackStructFields(a,b,c)
#define XmDragProcCallbackStruct_sizeof() 0
#endif

#ifndef NO_XmDropFinishCallbackStruct
void cacheXmDropFinishCallbackStructFields(JNIEnv *env, jobject lpObject);
XmDropFinishCallbackStruct *getXmDropFinishCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropFinishCallbackStruct *lpStruct);
void setXmDropFinishCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropFinishCallbackStruct *lpStruct);
#define XmDropFinishCallbackStruct_sizeof() sizeof(XmDropFinishCallbackStruct)
#else
#define cacheXmDropFinishCallbackStructFields(a,b)
#define getXmDropFinishCallbackStructFields(a,b,c) NULL
#define setXmDropFinishCallbackStructFields(a,b,c)
#define XmDropFinishCallbackStruct_sizeof() 0
#endif

#ifndef NO_XmDropProcCallbackStruct
void cacheXmDropProcCallbackStructFields(JNIEnv *env, jobject lpObject);
XmDropProcCallbackStruct *getXmDropProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropProcCallbackStruct *lpStruct);
void setXmDropProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropProcCallbackStruct *lpStruct);
#define XmDropProcCallbackStruct_sizeof() sizeof(XmDropProcCallbackStruct)
#else
#define cacheXmDropProcCallbackStructFields(a,b)
#define getXmDropProcCallbackStructFields(a,b,c) NULL
#define setXmDropProcCallbackStructFields(a,b,c)
#define XmDropProcCallbackStruct_sizeof() 0
#endif

#ifndef NO_XmSpinBoxCallbackStruct
void cacheXmSpinBoxCallbackStructFields(JNIEnv *env, jobject lpObject);
XmSpinBoxCallbackStruct *getXmSpinBoxCallbackStructFields(JNIEnv *env, jobject lpObject, XmSpinBoxCallbackStruct *lpStruct);
void setXmSpinBoxCallbackStructFields(JNIEnv *env, jobject lpObject, XmSpinBoxCallbackStruct *lpStruct);
#define XmSpinBoxCallbackStruct_sizeof() sizeof(XmSpinBoxCallbackStruct)
#else
#define cacheXmSpinBoxCallbackStructFields(a,b)
#define getXmSpinBoxCallbackStructFields(a,b,c) NULL
#define setXmSpinBoxCallbackStructFields(a,b,c)
#define XmSpinBoxCallbackStruct_sizeof() 0
#endif

#ifndef NO_XmTextBlockRec
void cacheXmTextBlockRecFields(JNIEnv *env, jobject lpObject);
XmTextBlockRec *getXmTextBlockRecFields(JNIEnv *env, jobject lpObject, XmTextBlockRec *lpStruct);
void setXmTextBlockRecFields(JNIEnv *env, jobject lpObject, XmTextBlockRec *lpStruct);
#define XmTextBlockRec_sizeof() sizeof(XmTextBlockRec)
#else
#define cacheXmTextBlockRecFields(a,b)
#define getXmTextBlockRecFields(a,b,c) NULL
#define setXmTextBlockRecFields(a,b,c)
#define XmTextBlockRec_sizeof() 0
#endif

#ifndef NO_XmTextVerifyCallbackStruct
void cacheXmTextVerifyCallbackStructFields(JNIEnv *env, jobject lpObject);
XmTextVerifyCallbackStruct *getXmTextVerifyCallbackStructFields(JNIEnv *env, jobject lpObject, XmTextVerifyCallbackStruct *lpStruct);
void setXmTextVerifyCallbackStructFields(JNIEnv *env, jobject lpObject, XmTextVerifyCallbackStruct *lpStruct);
#define XmTextVerifyCallbackStruct_sizeof() sizeof(XmTextVerifyCallbackStruct)
#else
#define cacheXmTextVerifyCallbackStructFields(a,b)
#define getXmTextVerifyCallbackStructFields(a,b,c) NULL
#define setXmTextVerifyCallbackStructFields(a,b,c)
#define XmTextVerifyCallbackStruct_sizeof() 0
#endif

#ifndef NO_XtWidgetGeometry
void cacheXtWidgetGeometryFields(JNIEnv *env, jobject lpObject);
XtWidgetGeometry *getXtWidgetGeometryFields(JNIEnv *env, jobject lpObject, XtWidgetGeometry *lpStruct);
void setXtWidgetGeometryFields(JNIEnv *env, jobject lpObject, XtWidgetGeometry *lpStruct);
#define XtWidgetGeometry_sizeof() sizeof(XtWidgetGeometry)
#else
#define cacheXtWidgetGeometryFields(a,b)
#define getXtWidgetGeometryFields(a,b,c) NULL
#define setXtWidgetGeometryFields(a,b,c)
#define XtWidgetGeometry_sizeof() 0
#endif

