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

#include "swt.h"
#include "os_structs.h"

#ifndef NO_Visual
typedef struct Visual_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID ext_data, visualid, c_class, red_mask, green_mask, blue_mask, bits_per_rgb, map_entries;
} Visual_FID_CACHE;

Visual_FID_CACHE VisualFc;

void cacheVisualFields(JNIEnv *env, jobject lpObject)
{
	if (VisualFc.cached) return;
	VisualFc.clazz = (*env)->GetObjectClass(env, lpObject);
	VisualFc.ext_data = (*env)->GetFieldID(env, VisualFc.clazz, "ext_data", "I");
	VisualFc.visualid = (*env)->GetFieldID(env, VisualFc.clazz, "visualid", "I");
	VisualFc.c_class = (*env)->GetFieldID(env, VisualFc.clazz, "c_class", "I");
	VisualFc.red_mask = (*env)->GetFieldID(env, VisualFc.clazz, "red_mask", "I");
	VisualFc.green_mask = (*env)->GetFieldID(env, VisualFc.clazz, "green_mask", "I");
	VisualFc.blue_mask = (*env)->GetFieldID(env, VisualFc.clazz, "blue_mask", "I");
	VisualFc.bits_per_rgb = (*env)->GetFieldID(env, VisualFc.clazz, "bits_per_rgb", "I");
	VisualFc.map_entries = (*env)->GetFieldID(env, VisualFc.clazz, "map_entries", "I");
	VisualFc.cached = 1;
}

Visual *getVisualFields(JNIEnv *env, jobject lpObject, Visual *lpStruct)
{
	if (!VisualFc.cached) cacheVisualFields(env, lpObject);
	lpStruct->ext_data = (XExtData *)(*env)->GetIntField(env, lpObject, VisualFc.ext_data);
	lpStruct->visualid = (*env)->GetIntField(env, lpObject, VisualFc.visualid);
	lpStruct->class = (*env)->GetIntField(env, lpObject, VisualFc.c_class);
	lpStruct->red_mask = (*env)->GetIntField(env, lpObject, VisualFc.red_mask);
	lpStruct->green_mask = (*env)->GetIntField(env, lpObject, VisualFc.green_mask);
	lpStruct->blue_mask = (*env)->GetIntField(env, lpObject, VisualFc.blue_mask);
	lpStruct->bits_per_rgb = (*env)->GetIntField(env, lpObject, VisualFc.bits_per_rgb);
	lpStruct->map_entries = (*env)->GetIntField(env, lpObject, VisualFc.map_entries);
	return lpStruct;
}

void setVisualFields(JNIEnv *env, jobject lpObject, Visual *lpStruct)
{
	if (!VisualFc.cached) cacheVisualFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, VisualFc.ext_data, (jint)lpStruct->ext_data);
	(*env)->SetIntField(env, lpObject, VisualFc.visualid, (jint)lpStruct->visualid);
	(*env)->SetIntField(env, lpObject, VisualFc.c_class, (jint)lpStruct->class);
	(*env)->SetIntField(env, lpObject, VisualFc.red_mask, (jint)lpStruct->red_mask);
	(*env)->SetIntField(env, lpObject, VisualFc.green_mask, (jint)lpStruct->green_mask);
	(*env)->SetIntField(env, lpObject, VisualFc.blue_mask, (jint)lpStruct->blue_mask);
	(*env)->SetIntField(env, lpObject, VisualFc.bits_per_rgb, (jint)lpStruct->bits_per_rgb);
	(*env)->SetIntField(env, lpObject, VisualFc.map_entries, (jint)lpStruct->map_entries);
}
#endif

#ifndef NO_XAnyEvent
typedef struct XAnyEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID serial, send_event, display, window;
} XAnyEvent_FID_CACHE;

XAnyEvent_FID_CACHE XAnyEventFc;

void cacheXAnyEventFields(JNIEnv *env, jobject lpObject)
{
	if (XAnyEventFc.cached) return;
	cacheXEventFields(env, lpObject);
	XAnyEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XAnyEventFc.serial = (*env)->GetFieldID(env, XAnyEventFc.clazz, "serial", "I");
	XAnyEventFc.send_event = (*env)->GetFieldID(env, XAnyEventFc.clazz, "send_event", "I");
	XAnyEventFc.display = (*env)->GetFieldID(env, XAnyEventFc.clazz, "display", "I");
	XAnyEventFc.window = (*env)->GetFieldID(env, XAnyEventFc.clazz, "window", "I");
	XAnyEventFc.cached = 1;
}

XAnyEvent *getXAnyEventFields(JNIEnv *env, jobject lpObject, XAnyEvent *lpStruct)
{
	if (!XAnyEventFc.cached) cacheXAnyEventFields(env, lpObject);
	getXEventFields(env, lpObject, (XEvent *)lpStruct);
	lpStruct->serial = (*env)->GetIntField(env, lpObject, XAnyEventFc.serial);
	lpStruct->send_event = (*env)->GetIntField(env, lpObject, XAnyEventFc.send_event);
	lpStruct->display = (Display *)(*env)->GetIntField(env, lpObject, XAnyEventFc.display);
	lpStruct->window = (*env)->GetIntField(env, lpObject, XAnyEventFc.window);
	return lpStruct;
}

void setXAnyEventFields(JNIEnv *env, jobject lpObject, XAnyEvent *lpStruct)
{
	if (!XAnyEventFc.cached) cacheXAnyEventFields(env, lpObject);
	setXEventFields(env, lpObject, (XEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XAnyEventFc.serial, (jint)lpStruct->serial);
	(*env)->SetIntField(env, lpObject, XAnyEventFc.send_event, (jint)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, XAnyEventFc.display, (jint)lpStruct->display);
	(*env)->SetIntField(env, lpObject, XAnyEventFc.window, (jint)lpStruct->window);
}
#endif

#ifndef NO_XButtonEvent
typedef struct XButtonEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID root, subwindow, time, x, y, x_root, y_root, state, button, same_screen;
} XButtonEvent_FID_CACHE;

XButtonEvent_FID_CACHE XButtonEventFc;

void cacheXButtonEventFields(JNIEnv *env, jobject lpObject)
{
	if (XButtonEventFc.cached) return;
	cacheXAnyEventFields(env, lpObject);
	XButtonEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XButtonEventFc.root = (*env)->GetFieldID(env, XButtonEventFc.clazz, "root", "I");
	XButtonEventFc.subwindow = (*env)->GetFieldID(env, XButtonEventFc.clazz, "subwindow", "I");
	XButtonEventFc.time = (*env)->GetFieldID(env, XButtonEventFc.clazz, "time", "I");
	XButtonEventFc.x = (*env)->GetFieldID(env, XButtonEventFc.clazz, "x", "I");
	XButtonEventFc.y = (*env)->GetFieldID(env, XButtonEventFc.clazz, "y", "I");
	XButtonEventFc.x_root = (*env)->GetFieldID(env, XButtonEventFc.clazz, "x_root", "I");
	XButtonEventFc.y_root = (*env)->GetFieldID(env, XButtonEventFc.clazz, "y_root", "I");
	XButtonEventFc.state = (*env)->GetFieldID(env, XButtonEventFc.clazz, "state", "I");
	XButtonEventFc.button = (*env)->GetFieldID(env, XButtonEventFc.clazz, "button", "I");
	XButtonEventFc.same_screen = (*env)->GetFieldID(env, XButtonEventFc.clazz, "same_screen", "I");
	XButtonEventFc.cached = 1;
}

XButtonEvent *getXButtonEventFields(JNIEnv *env, jobject lpObject, XButtonEvent *lpStruct)
{
	if (!XButtonEventFc.cached) cacheXButtonEventFields(env, lpObject);
	getXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	lpStruct->root = (*env)->GetIntField(env, lpObject, XButtonEventFc.root);
	lpStruct->subwindow = (*env)->GetIntField(env, lpObject, XButtonEventFc.subwindow);
	lpStruct->time = (*env)->GetIntField(env, lpObject, XButtonEventFc.time);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XButtonEventFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XButtonEventFc.y);
	lpStruct->x_root = (*env)->GetIntField(env, lpObject, XButtonEventFc.x_root);
	lpStruct->y_root = (*env)->GetIntField(env, lpObject, XButtonEventFc.y_root);
	lpStruct->state = (*env)->GetIntField(env, lpObject, XButtonEventFc.state);
	lpStruct->button = (*env)->GetIntField(env, lpObject, XButtonEventFc.button);
	lpStruct->same_screen = (*env)->GetIntField(env, lpObject, XButtonEventFc.same_screen);
	return lpStruct;
}

void setXButtonEventFields(JNIEnv *env, jobject lpObject, XButtonEvent *lpStruct)
{
	if (!XButtonEventFc.cached) cacheXButtonEventFields(env, lpObject);
	setXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XButtonEventFc.root, (jint)lpStruct->root);
	(*env)->SetIntField(env, lpObject, XButtonEventFc.subwindow, (jint)lpStruct->subwindow);
	(*env)->SetIntField(env, lpObject, XButtonEventFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, XButtonEventFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XButtonEventFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XButtonEventFc.x_root, (jint)lpStruct->x_root);
	(*env)->SetIntField(env, lpObject, XButtonEventFc.y_root, (jint)lpStruct->y_root);
	(*env)->SetIntField(env, lpObject, XButtonEventFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, XButtonEventFc.button, (jint)lpStruct->button);
	(*env)->SetIntField(env, lpObject, XButtonEventFc.same_screen, (jint)lpStruct->same_screen);
}
#endif

#ifndef NO_XCharStruct
typedef struct XCharStruct_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lbearing, rbearing, width, ascent, descent, attributes;
} XCharStruct_FID_CACHE;

XCharStruct_FID_CACHE XCharStructFc;

void cacheXCharStructFields(JNIEnv *env, jobject lpObject)
{
	if (XCharStructFc.cached) return;
	XCharStructFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XCharStructFc.lbearing = (*env)->GetFieldID(env, XCharStructFc.clazz, "lbearing", "S");
	XCharStructFc.rbearing = (*env)->GetFieldID(env, XCharStructFc.clazz, "rbearing", "S");
	XCharStructFc.width = (*env)->GetFieldID(env, XCharStructFc.clazz, "width", "S");
	XCharStructFc.ascent = (*env)->GetFieldID(env, XCharStructFc.clazz, "ascent", "S");
	XCharStructFc.descent = (*env)->GetFieldID(env, XCharStructFc.clazz, "descent", "S");
	XCharStructFc.attributes = (*env)->GetFieldID(env, XCharStructFc.clazz, "attributes", "S");
	XCharStructFc.cached = 1;
}

XCharStruct *getXCharStructFields(JNIEnv *env, jobject lpObject, XCharStruct *lpStruct)
{
	if (!XCharStructFc.cached) cacheXCharStructFields(env, lpObject);
	lpStruct->lbearing = (*env)->GetShortField(env, lpObject, XCharStructFc.lbearing);
	lpStruct->rbearing = (*env)->GetShortField(env, lpObject, XCharStructFc.rbearing);
	lpStruct->width = (*env)->GetShortField(env, lpObject, XCharStructFc.width);
	lpStruct->ascent = (*env)->GetShortField(env, lpObject, XCharStructFc.ascent);
	lpStruct->descent = (*env)->GetShortField(env, lpObject, XCharStructFc.descent);
	lpStruct->attributes = (*env)->GetShortField(env, lpObject, XCharStructFc.attributes);
	return lpStruct;
}

void setXCharStructFields(JNIEnv *env, jobject lpObject, XCharStruct *lpStruct)
{
	if (!XCharStructFc.cached) cacheXCharStructFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, XCharStructFc.lbearing, (jshort)lpStruct->lbearing);
	(*env)->SetShortField(env, lpObject, XCharStructFc.rbearing, (jshort)lpStruct->rbearing);
	(*env)->SetShortField(env, lpObject, XCharStructFc.width, (jshort)lpStruct->width);
	(*env)->SetShortField(env, lpObject, XCharStructFc.ascent, (jshort)lpStruct->ascent);
	(*env)->SetShortField(env, lpObject, XCharStructFc.descent, (jshort)lpStruct->descent);
	(*env)->SetShortField(env, lpObject, XCharStructFc.attributes, (jshort)lpStruct->attributes);
}
#endif

#ifndef NO_XClientMessageEvent
typedef struct XClientMessageEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID message_type, format, data;
} XClientMessageEvent_FID_CACHE;

XClientMessageEvent_FID_CACHE XClientMessageEventFc;

void cacheXClientMessageEventFields(JNIEnv *env, jobject lpObject)
{
	if (XClientMessageEventFc.cached) return;
	cacheXAnyEventFields(env, lpObject);
	XClientMessageEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XClientMessageEventFc.message_type = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "message_type", "I");
	XClientMessageEventFc.format = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "format", "I");
	XClientMessageEventFc.data = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "data", "[I");
	XClientMessageEventFc.cached = 1;
}

XClientMessageEvent *getXClientMessageEventFields(JNIEnv *env, jobject lpObject, XClientMessageEvent *lpStruct)
{
	if (!XClientMessageEventFc.cached) cacheXClientMessageEventFields(env, lpObject);
	getXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	lpStruct->message_type = (Atom)(*env)->GetIntField(env, lpObject, XClientMessageEventFc.message_type);
	lpStruct->format = (*env)->GetIntField(env, lpObject, XClientMessageEventFc.format);
	{
	jintArray lpObject1 = (jintArray)(*env)->GetObjectField(env, lpObject, XClientMessageEventFc.data);
	(*env)->GetIntArrayRegion(env, lpObject1, 0, sizeof(lpStruct->data.l) / sizeof(jint), (jint *)lpStruct->data.l);
	}
	return lpStruct;
}

void setXClientMessageEventFields(JNIEnv *env, jobject lpObject, XClientMessageEvent *lpStruct)
{
	if (!XClientMessageEventFc.cached) cacheXClientMessageEventFields(env, lpObject);
	setXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XClientMessageEventFc.message_type, (jint)lpStruct->message_type);
	(*env)->SetIntField(env, lpObject, XClientMessageEventFc.format, (jint)lpStruct->format);
	{
	jintArray lpObject1 = (jintArray)(*env)->GetObjectField(env, lpObject, XClientMessageEventFc.data);
	(*env)->SetIntArrayRegion(env, lpObject1, 0, sizeof(lpStruct->data.l) / sizeof(jint), (jint *)lpStruct->data.l);
	}
}
#endif

#ifndef NO_XColor
typedef struct XColor_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pixel, red, green, blue, flags, pad;
} XColor_FID_CACHE;

XColor_FID_CACHE XColorFc;

void cacheXColorFields(JNIEnv *env, jobject lpObject)
{
	if (XColorFc.cached) return;
	XColorFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XColorFc.pixel = (*env)->GetFieldID(env, XColorFc.clazz, "pixel", "I");
	XColorFc.red = (*env)->GetFieldID(env, XColorFc.clazz, "red", "S");
	XColorFc.green = (*env)->GetFieldID(env, XColorFc.clazz, "green", "S");
	XColorFc.blue = (*env)->GetFieldID(env, XColorFc.clazz, "blue", "S");
	XColorFc.flags = (*env)->GetFieldID(env, XColorFc.clazz, "flags", "B");
	XColorFc.pad = (*env)->GetFieldID(env, XColorFc.clazz, "pad", "B");
	XColorFc.cached = 1;
}

XColor *getXColorFields(JNIEnv *env, jobject lpObject, XColor *lpStruct)
{
	if (!XColorFc.cached) cacheXColorFields(env, lpObject);
	lpStruct->pixel = (*env)->GetIntField(env, lpObject, XColorFc.pixel);
	lpStruct->red = (*env)->GetShortField(env, lpObject, XColorFc.red);
	lpStruct->green = (*env)->GetShortField(env, lpObject, XColorFc.green);
	lpStruct->blue = (*env)->GetShortField(env, lpObject, XColorFc.blue);
	lpStruct->flags = (*env)->GetByteField(env, lpObject, XColorFc.flags);
	lpStruct->pad = (*env)->GetByteField(env, lpObject, XColorFc.pad);
	return lpStruct;
}

void setXColorFields(JNIEnv *env, jobject lpObject, XColor *lpStruct)
{
	if (!XColorFc.cached) cacheXColorFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XColorFc.pixel, (jint)lpStruct->pixel);
	(*env)->SetShortField(env, lpObject, XColorFc.red, (jshort)lpStruct->red);
	(*env)->SetShortField(env, lpObject, XColorFc.green, (jshort)lpStruct->green);
	(*env)->SetShortField(env, lpObject, XColorFc.blue, (jshort)lpStruct->blue);
	(*env)->SetByteField(env, lpObject, XColorFc.flags, (jbyte)lpStruct->flags);
	(*env)->SetByteField(env, lpObject, XColorFc.pad, (jbyte)lpStruct->pad);
}
#endif

#ifndef NO_XConfigureEvent
typedef struct XConfigureEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID serial, send_event, display, event, window, x, y, width, height, border_width, above, override_redirect;
} XConfigureEvent_FID_CACHE;

XConfigureEvent_FID_CACHE XConfigureEventFc;

void cacheXConfigureEventFields(JNIEnv *env, jobject lpObject)
{
	if (XConfigureEventFc.cached) return;
	cacheXEventFields(env, lpObject);
	XConfigureEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XConfigureEventFc.serial = (*env)->GetFieldID(env, XConfigureEventFc.clazz, "serial", "I");
	XConfigureEventFc.send_event = (*env)->GetFieldID(env, XConfigureEventFc.clazz, "send_event", "I");
	XConfigureEventFc.display = (*env)->GetFieldID(env, XConfigureEventFc.clazz, "display", "I");
	XConfigureEventFc.event = (*env)->GetFieldID(env, XConfigureEventFc.clazz, "event", "I");
	XConfigureEventFc.window = (*env)->GetFieldID(env, XConfigureEventFc.clazz, "window", "I");
	XConfigureEventFc.x = (*env)->GetFieldID(env, XConfigureEventFc.clazz, "x", "I");
	XConfigureEventFc.y = (*env)->GetFieldID(env, XConfigureEventFc.clazz, "y", "I");
	XConfigureEventFc.width = (*env)->GetFieldID(env, XConfigureEventFc.clazz, "width", "I");
	XConfigureEventFc.height = (*env)->GetFieldID(env, XConfigureEventFc.clazz, "height", "I");
	XConfigureEventFc.border_width = (*env)->GetFieldID(env, XConfigureEventFc.clazz, "border_width", "I");
	XConfigureEventFc.above = (*env)->GetFieldID(env, XConfigureEventFc.clazz, "above", "I");
	XConfigureEventFc.override_redirect = (*env)->GetFieldID(env, XConfigureEventFc.clazz, "override_redirect", "I");
	XConfigureEventFc.cached = 1;
}

XConfigureEvent *getXConfigureEventFields(JNIEnv *env, jobject lpObject, XConfigureEvent *lpStruct)
{
	if (!XConfigureEventFc.cached) cacheXConfigureEventFields(env, lpObject);
	getXEventFields(env, lpObject, (XEvent *)lpStruct);
	lpStruct->serial = (*env)->GetIntField(env, lpObject, XConfigureEventFc.serial);
	lpStruct->send_event = (*env)->GetIntField(env, lpObject, XConfigureEventFc.send_event);
	lpStruct->display = (Display *)(*env)->GetIntField(env, lpObject, XConfigureEventFc.display);
	lpStruct->event = (Window)(*env)->GetIntField(env, lpObject, XConfigureEventFc.event);
	lpStruct->window = (Window)(*env)->GetIntField(env, lpObject, XConfigureEventFc.window);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XConfigureEventFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XConfigureEventFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, XConfigureEventFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, XConfigureEventFc.height);
	lpStruct->border_width = (*env)->GetIntField(env, lpObject, XConfigureEventFc.border_width);
	lpStruct->above = (Window)(*env)->GetIntField(env, lpObject, XConfigureEventFc.above);
	lpStruct->override_redirect = (*env)->GetIntField(env, lpObject, XConfigureEventFc.override_redirect);
	return lpStruct;
}

void setXConfigureEventFields(JNIEnv *env, jobject lpObject, XConfigureEvent *lpStruct)
{
	if (!XConfigureEventFc.cached) cacheXConfigureEventFields(env, lpObject);
	setXEventFields(env, lpObject, (XEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XConfigureEventFc.serial, (jint)lpStruct->serial);
	(*env)->SetIntField(env, lpObject, XConfigureEventFc.send_event, (jint)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, XConfigureEventFc.display, (jint)lpStruct->display);
	(*env)->SetIntField(env, lpObject, XConfigureEventFc.event, (jint)lpStruct->event);
	(*env)->SetIntField(env, lpObject, XConfigureEventFc.window, (jint)lpStruct->window);
	(*env)->SetIntField(env, lpObject, XConfigureEventFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XConfigureEventFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XConfigureEventFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, XConfigureEventFc.height, (jint)lpStruct->height);
	(*env)->SetIntField(env, lpObject, XConfigureEventFc.border_width, (jint)lpStruct->border_width);
	(*env)->SetIntField(env, lpObject, XConfigureEventFc.above, (jint)lpStruct->above);
	(*env)->SetIntField(env, lpObject, XConfigureEventFc.override_redirect, (jint)lpStruct->override_redirect);
}
#endif

#ifndef NO_XCreateWindowEvent
typedef struct XCreateWindowEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID serial, send_event, display, parent, window, x, y, width, height, border_width, override_redirect;
} XCreateWindowEvent_FID_CACHE;

XCreateWindowEvent_FID_CACHE XCreateWindowEventFc;

void cacheXCreateWindowEventFields(JNIEnv *env, jobject lpObject)
{
	if (XCreateWindowEventFc.cached) return;
	cacheXEventFields(env, lpObject);
	XCreateWindowEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XCreateWindowEventFc.serial = (*env)->GetFieldID(env, XCreateWindowEventFc.clazz, "serial", "I");
	XCreateWindowEventFc.send_event = (*env)->GetFieldID(env, XCreateWindowEventFc.clazz, "send_event", "I");
	XCreateWindowEventFc.display = (*env)->GetFieldID(env, XCreateWindowEventFc.clazz, "display", "I");
	XCreateWindowEventFc.parent = (*env)->GetFieldID(env, XCreateWindowEventFc.clazz, "parent", "I");
	XCreateWindowEventFc.window = (*env)->GetFieldID(env, XCreateWindowEventFc.clazz, "window", "I");
	XCreateWindowEventFc.x = (*env)->GetFieldID(env, XCreateWindowEventFc.clazz, "x", "I");
	XCreateWindowEventFc.y = (*env)->GetFieldID(env, XCreateWindowEventFc.clazz, "y", "I");
	XCreateWindowEventFc.width = (*env)->GetFieldID(env, XCreateWindowEventFc.clazz, "width", "I");
	XCreateWindowEventFc.height = (*env)->GetFieldID(env, XCreateWindowEventFc.clazz, "height", "I");
	XCreateWindowEventFc.border_width = (*env)->GetFieldID(env, XCreateWindowEventFc.clazz, "border_width", "I");
	XCreateWindowEventFc.override_redirect = (*env)->GetFieldID(env, XCreateWindowEventFc.clazz, "override_redirect", "I");
	XCreateWindowEventFc.cached = 1;
}

XCreateWindowEvent *getXCreateWindowEventFields(JNIEnv *env, jobject lpObject, XCreateWindowEvent *lpStruct)
{
	if (!XCreateWindowEventFc.cached) cacheXCreateWindowEventFields(env, lpObject);
	getXEventFields(env, lpObject, (XEvent *)lpStruct);
	lpStruct->serial = (*env)->GetIntField(env, lpObject, XCreateWindowEventFc.serial);
	lpStruct->send_event = (*env)->GetIntField(env, lpObject, XCreateWindowEventFc.send_event);
	lpStruct->display = (Display *)(*env)->GetIntField(env, lpObject, XCreateWindowEventFc.display);
	lpStruct->parent = (Window)(*env)->GetIntField(env, lpObject, XCreateWindowEventFc.parent);
	lpStruct->window = (Window)(*env)->GetIntField(env, lpObject, XCreateWindowEventFc.window);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XCreateWindowEventFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XCreateWindowEventFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, XCreateWindowEventFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, XCreateWindowEventFc.height);
	lpStruct->border_width = (*env)->GetIntField(env, lpObject, XCreateWindowEventFc.border_width);
	lpStruct->override_redirect = (*env)->GetIntField(env, lpObject, XCreateWindowEventFc.override_redirect);
	return lpStruct;
}

void setXCreateWindowEventFields(JNIEnv *env, jobject lpObject, XCreateWindowEvent *lpStruct)
{
	if (!XCreateWindowEventFc.cached) cacheXCreateWindowEventFields(env, lpObject);
	setXEventFields(env, lpObject, (XEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XCreateWindowEventFc.serial, (jint)lpStruct->serial);
	(*env)->SetIntField(env, lpObject, XCreateWindowEventFc.send_event, (jint)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, XCreateWindowEventFc.display, (jint)lpStruct->display);
	(*env)->SetIntField(env, lpObject, XCreateWindowEventFc.parent, (jint)lpStruct->parent);
	(*env)->SetIntField(env, lpObject, XCreateWindowEventFc.window, (jint)lpStruct->window);
	(*env)->SetIntField(env, lpObject, XCreateWindowEventFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XCreateWindowEventFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XCreateWindowEventFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, XCreateWindowEventFc.height, (jint)lpStruct->height);
	(*env)->SetIntField(env, lpObject, XCreateWindowEventFc.border_width, (jint)lpStruct->border_width);
	(*env)->SetIntField(env, lpObject, XCreateWindowEventFc.override_redirect, (jint)lpStruct->override_redirect);
}
#endif

#ifndef NO_XCrossingEvent
typedef struct XCrossingEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID root, subwindow, time, x, y, x_root, y_root, mode, detail, same_screen, focus, state;
} XCrossingEvent_FID_CACHE;

XCrossingEvent_FID_CACHE XCrossingEventFc;

void cacheXCrossingEventFields(JNIEnv *env, jobject lpObject)
{
	if (XCrossingEventFc.cached) return;
	cacheXAnyEventFields(env, lpObject);
	XCrossingEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XCrossingEventFc.root = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "root", "I");
	XCrossingEventFc.subwindow = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "subwindow", "I");
	XCrossingEventFc.time = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "time", "I");
	XCrossingEventFc.x = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "x", "I");
	XCrossingEventFc.y = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "y", "I");
	XCrossingEventFc.x_root = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "x_root", "I");
	XCrossingEventFc.y_root = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "y_root", "I");
	XCrossingEventFc.mode = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "mode", "I");
	XCrossingEventFc.detail = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "detail", "I");
	XCrossingEventFc.same_screen = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "same_screen", "I");
	XCrossingEventFc.focus = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "focus", "I");
	XCrossingEventFc.state = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "state", "I");
	XCrossingEventFc.cached = 1;
}

XCrossingEvent *getXCrossingEventFields(JNIEnv *env, jobject lpObject, XCrossingEvent *lpStruct)
{
	if (!XCrossingEventFc.cached) cacheXCrossingEventFields(env, lpObject);
	getXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	lpStruct->root = (*env)->GetIntField(env, lpObject, XCrossingEventFc.root);
	lpStruct->subwindow = (*env)->GetIntField(env, lpObject, XCrossingEventFc.subwindow);
	lpStruct->time = (*env)->GetIntField(env, lpObject, XCrossingEventFc.time);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XCrossingEventFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XCrossingEventFc.y);
	lpStruct->x_root = (*env)->GetIntField(env, lpObject, XCrossingEventFc.x_root);
	lpStruct->y_root = (*env)->GetIntField(env, lpObject, XCrossingEventFc.y_root);
	lpStruct->mode = (*env)->GetIntField(env, lpObject, XCrossingEventFc.mode);
	lpStruct->detail = (*env)->GetIntField(env, lpObject, XCrossingEventFc.detail);
	lpStruct->same_screen = (*env)->GetIntField(env, lpObject, XCrossingEventFc.same_screen);
	lpStruct->focus = (*env)->GetIntField(env, lpObject, XCrossingEventFc.focus);
	lpStruct->state = (*env)->GetIntField(env, lpObject, XCrossingEventFc.state);
	return lpStruct;
}

void setXCrossingEventFields(JNIEnv *env, jobject lpObject, XCrossingEvent *lpStruct)
{
	if (!XCrossingEventFc.cached) cacheXCrossingEventFields(env, lpObject);
	setXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.root, (jint)lpStruct->root);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.subwindow, (jint)lpStruct->subwindow);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.x_root, (jint)lpStruct->x_root);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.y_root, (jint)lpStruct->y_root);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.mode, (jint)lpStruct->mode);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.detail, (jint)lpStruct->detail);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.same_screen, (jint)lpStruct->same_screen);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.focus, (jint)lpStruct->focus);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.state, (jint)lpStruct->state);
}
#endif

#ifndef NO_XDestroyWindowEvent
typedef struct XDestroyWindowEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID serial, send_event, display, event, window;
} XDestroyWindowEvent_FID_CACHE;

XDestroyWindowEvent_FID_CACHE XDestroyWindowEventFc;

void cacheXDestroyWindowEventFields(JNIEnv *env, jobject lpObject)
{
	if (XDestroyWindowEventFc.cached) return;
	cacheXEventFields(env, lpObject);
	XDestroyWindowEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XDestroyWindowEventFc.serial = (*env)->GetFieldID(env, XDestroyWindowEventFc.clazz, "serial", "I");
	XDestroyWindowEventFc.send_event = (*env)->GetFieldID(env, XDestroyWindowEventFc.clazz, "send_event", "I");
	XDestroyWindowEventFc.display = (*env)->GetFieldID(env, XDestroyWindowEventFc.clazz, "display", "I");
	XDestroyWindowEventFc.event = (*env)->GetFieldID(env, XDestroyWindowEventFc.clazz, "event", "I");
	XDestroyWindowEventFc.window = (*env)->GetFieldID(env, XDestroyWindowEventFc.clazz, "window", "I");
	XDestroyWindowEventFc.cached = 1;
}

XDestroyWindowEvent *getXDestroyWindowEventFields(JNIEnv *env, jobject lpObject, XDestroyWindowEvent *lpStruct)
{
	if (!XDestroyWindowEventFc.cached) cacheXDestroyWindowEventFields(env, lpObject);
	getXEventFields(env, lpObject, (XEvent *)lpStruct);
	lpStruct->serial = (*env)->GetIntField(env, lpObject, XDestroyWindowEventFc.serial);
	lpStruct->send_event = (*env)->GetIntField(env, lpObject, XDestroyWindowEventFc.send_event);
	lpStruct->display = (Display *)(*env)->GetIntField(env, lpObject, XDestroyWindowEventFc.display);
	lpStruct->event = (Window)(*env)->GetIntField(env, lpObject, XDestroyWindowEventFc.event);
	lpStruct->window = (Window)(*env)->GetIntField(env, lpObject, XDestroyWindowEventFc.window);
	return lpStruct;
}

void setXDestroyWindowEventFields(JNIEnv *env, jobject lpObject, XDestroyWindowEvent *lpStruct)
{
	if (!XDestroyWindowEventFc.cached) cacheXDestroyWindowEventFields(env, lpObject);
	setXEventFields(env, lpObject, (XEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XDestroyWindowEventFc.serial, (jint)lpStruct->serial);
	(*env)->SetIntField(env, lpObject, XDestroyWindowEventFc.send_event, (jint)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, XDestroyWindowEventFc.display, (jint)lpStruct->display);
	(*env)->SetIntField(env, lpObject, XDestroyWindowEventFc.event, (jint)lpStruct->event);
	(*env)->SetIntField(env, lpObject, XDestroyWindowEventFc.window, (jint)lpStruct->window);
}
#endif

#ifndef NO_XEvent
typedef struct XEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type;
} XEvent_FID_CACHE;

XEvent_FID_CACHE XEventFc;

void cacheXEventFields(JNIEnv *env, jobject lpObject)
{
	if (XEventFc.cached) return;
	XEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XEventFc.type = (*env)->GetFieldID(env, XEventFc.clazz, "type", "I");
	XEventFc.cached = 1;
}

XEvent *getXEventFields(JNIEnv *env, jobject lpObject, XEvent *lpStruct)
{
	if (!XEventFc.cached) cacheXEventFields(env, lpObject);
	lpStruct->type = (*env)->GetIntField(env, lpObject, XEventFc.type);
	return lpStruct;
}

void setXEventFields(JNIEnv *env, jobject lpObject, XEvent *lpStruct)
{
	if (!XEventFc.cached) cacheXEventFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XEventFc.type, (jint)lpStruct->type);
}
#endif

#ifndef NO_XExposeEvent
typedef struct XExposeEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height, count;
} XExposeEvent_FID_CACHE;

XExposeEvent_FID_CACHE XExposeEventFc;

void cacheXExposeEventFields(JNIEnv *env, jobject lpObject)
{
	if (XExposeEventFc.cached) return;
	cacheXAnyEventFields(env, lpObject);
	XExposeEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XExposeEventFc.x = (*env)->GetFieldID(env, XExposeEventFc.clazz, "x", "I");
	XExposeEventFc.y = (*env)->GetFieldID(env, XExposeEventFc.clazz, "y", "I");
	XExposeEventFc.width = (*env)->GetFieldID(env, XExposeEventFc.clazz, "width", "I");
	XExposeEventFc.height = (*env)->GetFieldID(env, XExposeEventFc.clazz, "height", "I");
	XExposeEventFc.count = (*env)->GetFieldID(env, XExposeEventFc.clazz, "count", "I");
	XExposeEventFc.cached = 1;
}

XExposeEvent *getXExposeEventFields(JNIEnv *env, jobject lpObject, XExposeEvent *lpStruct)
{
	if (!XExposeEventFc.cached) cacheXExposeEventFields(env, lpObject);
	getXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XExposeEventFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XExposeEventFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, XExposeEventFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, XExposeEventFc.height);
	lpStruct->count = (*env)->GetIntField(env, lpObject, XExposeEventFc.count);
	return lpStruct;
}

void setXExposeEventFields(JNIEnv *env, jobject lpObject, XExposeEvent *lpStruct)
{
	if (!XExposeEventFc.cached) cacheXExposeEventFields(env, lpObject);
	setXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XExposeEventFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XExposeEventFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XExposeEventFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, XExposeEventFc.height, (jint)lpStruct->height);
	(*env)->SetIntField(env, lpObject, XExposeEventFc.count, (jint)lpStruct->count);
}
#endif

#ifndef NO_XFocusChangeEvent
typedef struct XFocusChangeEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID mode, detail;
} XFocusChangeEvent_FID_CACHE;

XFocusChangeEvent_FID_CACHE XFocusChangeEventFc;

void cacheXFocusChangeEventFields(JNIEnv *env, jobject lpObject)
{
	if (XFocusChangeEventFc.cached) return;
	cacheXAnyEventFields(env, lpObject);
	XFocusChangeEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XFocusChangeEventFc.mode = (*env)->GetFieldID(env, XFocusChangeEventFc.clazz, "mode", "I");
	XFocusChangeEventFc.detail = (*env)->GetFieldID(env, XFocusChangeEventFc.clazz, "detail", "I");
	XFocusChangeEventFc.cached = 1;
}

XFocusChangeEvent *getXFocusChangeEventFields(JNIEnv *env, jobject lpObject, XFocusChangeEvent *lpStruct)
{
	if (!XFocusChangeEventFc.cached) cacheXFocusChangeEventFields(env, lpObject);
	getXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	lpStruct->mode = (*env)->GetIntField(env, lpObject, XFocusChangeEventFc.mode);
	lpStruct->detail = (*env)->GetIntField(env, lpObject, XFocusChangeEventFc.detail);
	return lpStruct;
}

void setXFocusChangeEventFields(JNIEnv *env, jobject lpObject, XFocusChangeEvent *lpStruct)
{
	if (!XFocusChangeEventFc.cached) cacheXFocusChangeEventFields(env, lpObject);
	setXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XFocusChangeEventFc.mode, (jint)lpStruct->mode);
	(*env)->SetIntField(env, lpObject, XFocusChangeEventFc.detail, (jint)lpStruct->detail);
}
#endif

#ifndef NO_XFontStruct
typedef struct XFontStruct_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID ext_data, fid, direction, min_char_or_byte2, max_char_or_byte2, min_byte1, max_byte1, all_chars_exist, default_char, n_properties, properties, min_bounds_lbearing, min_bounds_rbearing, min_bounds_width, min_bounds_ascent, min_bounds_descent, min_bounds_attributes, max_bounds_lbearing, max_bounds_rbearing, max_bounds_width, max_bounds_ascent, max_bounds_descent, max_bounds_attributes, per_char, ascent, descent;
} XFontStruct_FID_CACHE;

XFontStruct_FID_CACHE XFontStructFc;

void cacheXFontStructFields(JNIEnv *env, jobject lpObject)
{
	if (XFontStructFc.cached) return;
	XFontStructFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XFontStructFc.ext_data = (*env)->GetFieldID(env, XFontStructFc.clazz, "ext_data", "I");
	XFontStructFc.fid = (*env)->GetFieldID(env, XFontStructFc.clazz, "fid", "I");
	XFontStructFc.direction = (*env)->GetFieldID(env, XFontStructFc.clazz, "direction", "I");
	XFontStructFc.min_char_or_byte2 = (*env)->GetFieldID(env, XFontStructFc.clazz, "min_char_or_byte2", "I");
	XFontStructFc.max_char_or_byte2 = (*env)->GetFieldID(env, XFontStructFc.clazz, "max_char_or_byte2", "I");
	XFontStructFc.min_byte1 = (*env)->GetFieldID(env, XFontStructFc.clazz, "min_byte1", "I");
	XFontStructFc.max_byte1 = (*env)->GetFieldID(env, XFontStructFc.clazz, "max_byte1", "I");
	XFontStructFc.all_chars_exist = (*env)->GetFieldID(env, XFontStructFc.clazz, "all_chars_exist", "I");
	XFontStructFc.default_char = (*env)->GetFieldID(env, XFontStructFc.clazz, "default_char", "I");
	XFontStructFc.n_properties = (*env)->GetFieldID(env, XFontStructFc.clazz, "n_properties", "I");
	XFontStructFc.properties = (*env)->GetFieldID(env, XFontStructFc.clazz, "properties", "I");
	XFontStructFc.min_bounds_lbearing = (*env)->GetFieldID(env, XFontStructFc.clazz, "min_bounds_lbearing", "S");
	XFontStructFc.min_bounds_rbearing = (*env)->GetFieldID(env, XFontStructFc.clazz, "min_bounds_rbearing", "S");
	XFontStructFc.min_bounds_width = (*env)->GetFieldID(env, XFontStructFc.clazz, "min_bounds_width", "S");
	XFontStructFc.min_bounds_ascent = (*env)->GetFieldID(env, XFontStructFc.clazz, "min_bounds_ascent", "S");
	XFontStructFc.min_bounds_descent = (*env)->GetFieldID(env, XFontStructFc.clazz, "min_bounds_descent", "S");
	XFontStructFc.min_bounds_attributes = (*env)->GetFieldID(env, XFontStructFc.clazz, "min_bounds_attributes", "S");
	XFontStructFc.max_bounds_lbearing = (*env)->GetFieldID(env, XFontStructFc.clazz, "max_bounds_lbearing", "S");
	XFontStructFc.max_bounds_rbearing = (*env)->GetFieldID(env, XFontStructFc.clazz, "max_bounds_rbearing", "S");
	XFontStructFc.max_bounds_width = (*env)->GetFieldID(env, XFontStructFc.clazz, "max_bounds_width", "S");
	XFontStructFc.max_bounds_ascent = (*env)->GetFieldID(env, XFontStructFc.clazz, "max_bounds_ascent", "S");
	XFontStructFc.max_bounds_descent = (*env)->GetFieldID(env, XFontStructFc.clazz, "max_bounds_descent", "S");
	XFontStructFc.max_bounds_attributes = (*env)->GetFieldID(env, XFontStructFc.clazz, "max_bounds_attributes", "S");
	XFontStructFc.per_char = (*env)->GetFieldID(env, XFontStructFc.clazz, "per_char", "I");
	XFontStructFc.ascent = (*env)->GetFieldID(env, XFontStructFc.clazz, "ascent", "I");
	XFontStructFc.descent = (*env)->GetFieldID(env, XFontStructFc.clazz, "descent", "I");
	XFontStructFc.cached = 1;
}

XFontStruct *getXFontStructFields(JNIEnv *env, jobject lpObject, XFontStruct *lpStruct)
{
	if (!XFontStructFc.cached) cacheXFontStructFields(env, lpObject);
	lpStruct->ext_data = (XExtData *)(*env)->GetIntField(env, lpObject, XFontStructFc.ext_data);
	lpStruct->fid = (*env)->GetIntField(env, lpObject, XFontStructFc.fid);
	lpStruct->direction = (*env)->GetIntField(env, lpObject, XFontStructFc.direction);
	lpStruct->min_char_or_byte2 = (*env)->GetIntField(env, lpObject, XFontStructFc.min_char_or_byte2);
	lpStruct->max_char_or_byte2 = (*env)->GetIntField(env, lpObject, XFontStructFc.max_char_or_byte2);
	lpStruct->min_byte1 = (*env)->GetIntField(env, lpObject, XFontStructFc.min_byte1);
	lpStruct->max_byte1 = (*env)->GetIntField(env, lpObject, XFontStructFc.max_byte1);
	lpStruct->all_chars_exist = (*env)->GetIntField(env, lpObject, XFontStructFc.all_chars_exist);
	lpStruct->default_char = (*env)->GetIntField(env, lpObject, XFontStructFc.default_char);
	lpStruct->n_properties = (*env)->GetIntField(env, lpObject, XFontStructFc.n_properties);
	lpStruct->properties = (XFontProp *)(*env)->GetIntField(env, lpObject, XFontStructFc.properties);
	lpStruct->min_bounds.lbearing = (*env)->GetShortField(env, lpObject, XFontStructFc.min_bounds_lbearing);
	lpStruct->min_bounds.rbearing = (*env)->GetShortField(env, lpObject, XFontStructFc.min_bounds_rbearing);
	lpStruct->min_bounds.width = (*env)->GetShortField(env, lpObject, XFontStructFc.min_bounds_width);
	lpStruct->min_bounds.ascent = (*env)->GetShortField(env, lpObject, XFontStructFc.min_bounds_ascent);
	lpStruct->min_bounds.descent = (*env)->GetShortField(env, lpObject, XFontStructFc.min_bounds_descent);
	lpStruct->min_bounds.attributes = (*env)->GetShortField(env, lpObject, XFontStructFc.min_bounds_attributes);
	lpStruct->max_bounds.lbearing = (*env)->GetShortField(env, lpObject, XFontStructFc.max_bounds_lbearing);
	lpStruct->max_bounds.rbearing = (*env)->GetShortField(env, lpObject, XFontStructFc.max_bounds_rbearing);
	lpStruct->max_bounds.width = (*env)->GetShortField(env, lpObject, XFontStructFc.max_bounds_width);
	lpStruct->max_bounds.ascent = (*env)->GetShortField(env, lpObject, XFontStructFc.max_bounds_ascent);
	lpStruct->max_bounds.descent = (*env)->GetShortField(env, lpObject, XFontStructFc.max_bounds_descent);
	lpStruct->max_bounds.attributes = (*env)->GetShortField(env, lpObject, XFontStructFc.max_bounds_attributes);
	lpStruct->per_char = (XCharStruct *)(*env)->GetIntField(env, lpObject, XFontStructFc.per_char);
	lpStruct->ascent = (*env)->GetIntField(env, lpObject, XFontStructFc.ascent);
	lpStruct->descent = (*env)->GetIntField(env, lpObject, XFontStructFc.descent);
	return lpStruct;
}

void setXFontStructFields(JNIEnv *env, jobject lpObject, XFontStruct *lpStruct)
{
	if (!XFontStructFc.cached) cacheXFontStructFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XFontStructFc.ext_data, (jint)lpStruct->ext_data);
	(*env)->SetIntField(env, lpObject, XFontStructFc.fid, (jint)lpStruct->fid);
	(*env)->SetIntField(env, lpObject, XFontStructFc.direction, (jint)lpStruct->direction);
	(*env)->SetIntField(env, lpObject, XFontStructFc.min_char_or_byte2, (jint)lpStruct->min_char_or_byte2);
	(*env)->SetIntField(env, lpObject, XFontStructFc.max_char_or_byte2, (jint)lpStruct->max_char_or_byte2);
	(*env)->SetIntField(env, lpObject, XFontStructFc.min_byte1, (jint)lpStruct->min_byte1);
	(*env)->SetIntField(env, lpObject, XFontStructFc.max_byte1, (jint)lpStruct->max_byte1);
	(*env)->SetIntField(env, lpObject, XFontStructFc.all_chars_exist, (jint)lpStruct->all_chars_exist);
	(*env)->SetIntField(env, lpObject, XFontStructFc.default_char, (jint)lpStruct->default_char);
	(*env)->SetIntField(env, lpObject, XFontStructFc.n_properties, (jint)lpStruct->n_properties);
	(*env)->SetIntField(env, lpObject, XFontStructFc.properties, (jint)lpStruct->properties);
	(*env)->SetShortField(env, lpObject, XFontStructFc.min_bounds_lbearing, (jshort)lpStruct->min_bounds.lbearing);
	(*env)->SetShortField(env, lpObject, XFontStructFc.min_bounds_rbearing, (jshort)lpStruct->min_bounds.rbearing);
	(*env)->SetShortField(env, lpObject, XFontStructFc.min_bounds_width, (jshort)lpStruct->min_bounds.width);
	(*env)->SetShortField(env, lpObject, XFontStructFc.min_bounds_ascent, (jshort)lpStruct->min_bounds.ascent);
	(*env)->SetShortField(env, lpObject, XFontStructFc.min_bounds_descent, (jshort)lpStruct->min_bounds.descent);
	(*env)->SetShortField(env, lpObject, XFontStructFc.min_bounds_attributes, (jshort)lpStruct->min_bounds.attributes);
	(*env)->SetShortField(env, lpObject, XFontStructFc.max_bounds_lbearing, (jshort)lpStruct->max_bounds.lbearing);
	(*env)->SetShortField(env, lpObject, XFontStructFc.max_bounds_rbearing, (jshort)lpStruct->max_bounds.rbearing);
	(*env)->SetShortField(env, lpObject, XFontStructFc.max_bounds_width, (jshort)lpStruct->max_bounds.width);
	(*env)->SetShortField(env, lpObject, XFontStructFc.max_bounds_ascent, (jshort)lpStruct->max_bounds.ascent);
	(*env)->SetShortField(env, lpObject, XFontStructFc.max_bounds_descent, (jshort)lpStruct->max_bounds.descent);
	(*env)->SetShortField(env, lpObject, XFontStructFc.max_bounds_attributes, (jshort)lpStruct->max_bounds.attributes);
	(*env)->SetIntField(env, lpObject, XFontStructFc.per_char, (jint)lpStruct->per_char);
	(*env)->SetIntField(env, lpObject, XFontStructFc.ascent, (jint)lpStruct->ascent);
	(*env)->SetIntField(env, lpObject, XFontStructFc.descent, (jint)lpStruct->descent);
}
#endif

#ifndef NO_XGCValues
typedef struct XGCValues_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID function, plane_mask, foreground, background, line_width, line_style, cap_style, join_style, fill_style, fill_rule, arc_mode, tile, stipple, ts_x_origin, ts_y_origin, font, subwindow_mode, graphics_exposures, clip_x_origin, clip_y_origin, clip_mask, dash_offset, dashes;
} XGCValues_FID_CACHE;

XGCValues_FID_CACHE XGCValuesFc;

void cacheXGCValuesFields(JNIEnv *env, jobject lpObject)
{
	if (XGCValuesFc.cached) return;
	XGCValuesFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XGCValuesFc.function = (*env)->GetFieldID(env, XGCValuesFc.clazz, "function", "I");
	XGCValuesFc.plane_mask = (*env)->GetFieldID(env, XGCValuesFc.clazz, "plane_mask", "I");
	XGCValuesFc.foreground = (*env)->GetFieldID(env, XGCValuesFc.clazz, "foreground", "I");
	XGCValuesFc.background = (*env)->GetFieldID(env, XGCValuesFc.clazz, "background", "I");
	XGCValuesFc.line_width = (*env)->GetFieldID(env, XGCValuesFc.clazz, "line_width", "I");
	XGCValuesFc.line_style = (*env)->GetFieldID(env, XGCValuesFc.clazz, "line_style", "I");
	XGCValuesFc.cap_style = (*env)->GetFieldID(env, XGCValuesFc.clazz, "cap_style", "I");
	XGCValuesFc.join_style = (*env)->GetFieldID(env, XGCValuesFc.clazz, "join_style", "I");
	XGCValuesFc.fill_style = (*env)->GetFieldID(env, XGCValuesFc.clazz, "fill_style", "I");
	XGCValuesFc.fill_rule = (*env)->GetFieldID(env, XGCValuesFc.clazz, "fill_rule", "I");
	XGCValuesFc.arc_mode = (*env)->GetFieldID(env, XGCValuesFc.clazz, "arc_mode", "I");
	XGCValuesFc.tile = (*env)->GetFieldID(env, XGCValuesFc.clazz, "tile", "I");
	XGCValuesFc.stipple = (*env)->GetFieldID(env, XGCValuesFc.clazz, "stipple", "I");
	XGCValuesFc.ts_x_origin = (*env)->GetFieldID(env, XGCValuesFc.clazz, "ts_x_origin", "I");
	XGCValuesFc.ts_y_origin = (*env)->GetFieldID(env, XGCValuesFc.clazz, "ts_y_origin", "I");
	XGCValuesFc.font = (*env)->GetFieldID(env, XGCValuesFc.clazz, "font", "I");
	XGCValuesFc.subwindow_mode = (*env)->GetFieldID(env, XGCValuesFc.clazz, "subwindow_mode", "I");
	XGCValuesFc.graphics_exposures = (*env)->GetFieldID(env, XGCValuesFc.clazz, "graphics_exposures", "I");
	XGCValuesFc.clip_x_origin = (*env)->GetFieldID(env, XGCValuesFc.clazz, "clip_x_origin", "I");
	XGCValuesFc.clip_y_origin = (*env)->GetFieldID(env, XGCValuesFc.clazz, "clip_y_origin", "I");
	XGCValuesFc.clip_mask = (*env)->GetFieldID(env, XGCValuesFc.clazz, "clip_mask", "I");
	XGCValuesFc.dash_offset = (*env)->GetFieldID(env, XGCValuesFc.clazz, "dash_offset", "I");
	XGCValuesFc.dashes = (*env)->GetFieldID(env, XGCValuesFc.clazz, "dashes", "B");
	XGCValuesFc.cached = 1;
}

XGCValues *getXGCValuesFields(JNIEnv *env, jobject lpObject, XGCValues *lpStruct)
{
	if (!XGCValuesFc.cached) cacheXGCValuesFields(env, lpObject);
	lpStruct->function = (*env)->GetIntField(env, lpObject, XGCValuesFc.function);
	lpStruct->plane_mask = (*env)->GetIntField(env, lpObject, XGCValuesFc.plane_mask);
	lpStruct->foreground = (*env)->GetIntField(env, lpObject, XGCValuesFc.foreground);
	lpStruct->background = (*env)->GetIntField(env, lpObject, XGCValuesFc.background);
	lpStruct->line_width = (*env)->GetIntField(env, lpObject, XGCValuesFc.line_width);
	lpStruct->line_style = (*env)->GetIntField(env, lpObject, XGCValuesFc.line_style);
	lpStruct->cap_style = (*env)->GetIntField(env, lpObject, XGCValuesFc.cap_style);
	lpStruct->join_style = (*env)->GetIntField(env, lpObject, XGCValuesFc.join_style);
	lpStruct->fill_style = (*env)->GetIntField(env, lpObject, XGCValuesFc.fill_style);
	lpStruct->fill_rule = (*env)->GetIntField(env, lpObject, XGCValuesFc.fill_rule);
	lpStruct->arc_mode = (*env)->GetIntField(env, lpObject, XGCValuesFc.arc_mode);
	lpStruct->tile = (*env)->GetIntField(env, lpObject, XGCValuesFc.tile);
	lpStruct->stipple = (*env)->GetIntField(env, lpObject, XGCValuesFc.stipple);
	lpStruct->ts_x_origin = (*env)->GetIntField(env, lpObject, XGCValuesFc.ts_x_origin);
	lpStruct->ts_y_origin = (*env)->GetIntField(env, lpObject, XGCValuesFc.ts_y_origin);
	lpStruct->font = (*env)->GetIntField(env, lpObject, XGCValuesFc.font);
	lpStruct->subwindow_mode = (*env)->GetIntField(env, lpObject, XGCValuesFc.subwindow_mode);
	lpStruct->graphics_exposures = (*env)->GetIntField(env, lpObject, XGCValuesFc.graphics_exposures);
	lpStruct->clip_x_origin = (*env)->GetIntField(env, lpObject, XGCValuesFc.clip_x_origin);
	lpStruct->clip_y_origin = (*env)->GetIntField(env, lpObject, XGCValuesFc.clip_y_origin);
	lpStruct->clip_mask = (*env)->GetIntField(env, lpObject, XGCValuesFc.clip_mask);
	lpStruct->dash_offset = (*env)->GetIntField(env, lpObject, XGCValuesFc.dash_offset);
	lpStruct->dashes = (*env)->GetByteField(env, lpObject, XGCValuesFc.dashes);
	return lpStruct;
}

void setXGCValuesFields(JNIEnv *env, jobject lpObject, XGCValues *lpStruct)
{
	if (!XGCValuesFc.cached) cacheXGCValuesFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.function, (jint)lpStruct->function);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.plane_mask, (jint)lpStruct->plane_mask);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.foreground, (jint)lpStruct->foreground);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.background, (jint)lpStruct->background);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.line_width, (jint)lpStruct->line_width);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.line_style, (jint)lpStruct->line_style);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.cap_style, (jint)lpStruct->cap_style);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.join_style, (jint)lpStruct->join_style);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.fill_style, (jint)lpStruct->fill_style);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.fill_rule, (jint)lpStruct->fill_rule);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.arc_mode, (jint)lpStruct->arc_mode);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.tile, (jint)lpStruct->tile);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.stipple, (jint)lpStruct->stipple);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.ts_x_origin, (jint)lpStruct->ts_x_origin);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.ts_y_origin, (jint)lpStruct->ts_y_origin);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.font, (jint)lpStruct->font);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.subwindow_mode, (jint)lpStruct->subwindow_mode);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.graphics_exposures, (jint)lpStruct->graphics_exposures);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.clip_x_origin, (jint)lpStruct->clip_x_origin);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.clip_y_origin, (jint)lpStruct->clip_y_origin);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.clip_mask, (jint)lpStruct->clip_mask);
	(*env)->SetIntField(env, lpObject, XGCValuesFc.dash_offset, (jint)lpStruct->dash_offset);
	(*env)->SetByteField(env, lpObject, XGCValuesFc.dashes, (jbyte)lpStruct->dashes);
}
#endif

#ifndef NO_XIconSize
typedef struct XIconSize_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID min_width, min_height, max_width, max_height, width_inc, height_inc;
} XIconSize_FID_CACHE;

XIconSize_FID_CACHE XIconSizeFc;

void cacheXIconSizeFields(JNIEnv *env, jobject lpObject)
{
	if (XIconSizeFc.cached) return;
	XIconSizeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XIconSizeFc.min_width = (*env)->GetFieldID(env, XIconSizeFc.clazz, "min_width", "I");
	XIconSizeFc.min_height = (*env)->GetFieldID(env, XIconSizeFc.clazz, "min_height", "I");
	XIconSizeFc.max_width = (*env)->GetFieldID(env, XIconSizeFc.clazz, "max_width", "I");
	XIconSizeFc.max_height = (*env)->GetFieldID(env, XIconSizeFc.clazz, "max_height", "I");
	XIconSizeFc.width_inc = (*env)->GetFieldID(env, XIconSizeFc.clazz, "width_inc", "I");
	XIconSizeFc.height_inc = (*env)->GetFieldID(env, XIconSizeFc.clazz, "height_inc", "I");
	XIconSizeFc.cached = 1;
}

XIconSize *getXIconSizeFields(JNIEnv *env, jobject lpObject, XIconSize *lpStruct)
{
	if (!XIconSizeFc.cached) cacheXIconSizeFields(env, lpObject);
	lpStruct->min_width = (*env)->GetIntField(env, lpObject, XIconSizeFc.min_width);
	lpStruct->min_height = (*env)->GetIntField(env, lpObject, XIconSizeFc.min_height);
	lpStruct->max_width = (*env)->GetIntField(env, lpObject, XIconSizeFc.max_width);
	lpStruct->max_height = (*env)->GetIntField(env, lpObject, XIconSizeFc.max_height);
	lpStruct->width_inc = (*env)->GetIntField(env, lpObject, XIconSizeFc.width_inc);
	lpStruct->height_inc = (*env)->GetIntField(env, lpObject, XIconSizeFc.height_inc);
	return lpStruct;
}

void setXIconSizeFields(JNIEnv *env, jobject lpObject, XIconSize *lpStruct)
{
	if (!XIconSizeFc.cached) cacheXIconSizeFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XIconSizeFc.min_width, (jint)lpStruct->min_width);
	(*env)->SetIntField(env, lpObject, XIconSizeFc.min_height, (jint)lpStruct->min_height);
	(*env)->SetIntField(env, lpObject, XIconSizeFc.max_width, (jint)lpStruct->max_width);
	(*env)->SetIntField(env, lpObject, XIconSizeFc.max_height, (jint)lpStruct->max_height);
	(*env)->SetIntField(env, lpObject, XIconSizeFc.width_inc, (jint)lpStruct->width_inc);
	(*env)->SetIntField(env, lpObject, XIconSizeFc.height_inc, (jint)lpStruct->height_inc);
}
#endif

#ifndef NO_XImage
typedef struct XImage_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID width, height, xoffset, format, data, byte_order, bitmap_unit, bitmap_bit_order, bitmap_pad, depth, bytes_per_line, bits_per_pixel, red_mask, green_mask, blue_mask, obdata, create_image, destroy_image, get_pixel, put_pixel, sub_image, add_pixel;
} XImage_FID_CACHE;

XImage_FID_CACHE XImageFc;

void cacheXImageFields(JNIEnv *env, jobject lpObject)
{
	if (XImageFc.cached) return;
	XImageFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XImageFc.width = (*env)->GetFieldID(env, XImageFc.clazz, "width", "I");
	XImageFc.height = (*env)->GetFieldID(env, XImageFc.clazz, "height", "I");
	XImageFc.xoffset = (*env)->GetFieldID(env, XImageFc.clazz, "xoffset", "I");
	XImageFc.format = (*env)->GetFieldID(env, XImageFc.clazz, "format", "I");
	XImageFc.data = (*env)->GetFieldID(env, XImageFc.clazz, "data", "I");
	XImageFc.byte_order = (*env)->GetFieldID(env, XImageFc.clazz, "byte_order", "I");
	XImageFc.bitmap_unit = (*env)->GetFieldID(env, XImageFc.clazz, "bitmap_unit", "I");
	XImageFc.bitmap_bit_order = (*env)->GetFieldID(env, XImageFc.clazz, "bitmap_bit_order", "I");
	XImageFc.bitmap_pad = (*env)->GetFieldID(env, XImageFc.clazz, "bitmap_pad", "I");
	XImageFc.depth = (*env)->GetFieldID(env, XImageFc.clazz, "depth", "I");
	XImageFc.bytes_per_line = (*env)->GetFieldID(env, XImageFc.clazz, "bytes_per_line", "I");
	XImageFc.bits_per_pixel = (*env)->GetFieldID(env, XImageFc.clazz, "bits_per_pixel", "I");
	XImageFc.red_mask = (*env)->GetFieldID(env, XImageFc.clazz, "red_mask", "I");
	XImageFc.green_mask = (*env)->GetFieldID(env, XImageFc.clazz, "green_mask", "I");
	XImageFc.blue_mask = (*env)->GetFieldID(env, XImageFc.clazz, "blue_mask", "I");
	XImageFc.obdata = (*env)->GetFieldID(env, XImageFc.clazz, "obdata", "I");
	XImageFc.create_image = (*env)->GetFieldID(env, XImageFc.clazz, "create_image", "I");
	XImageFc.destroy_image = (*env)->GetFieldID(env, XImageFc.clazz, "destroy_image", "I");
	XImageFc.get_pixel = (*env)->GetFieldID(env, XImageFc.clazz, "get_pixel", "I");
	XImageFc.put_pixel = (*env)->GetFieldID(env, XImageFc.clazz, "put_pixel", "I");
	XImageFc.sub_image = (*env)->GetFieldID(env, XImageFc.clazz, "sub_image", "I");
	XImageFc.add_pixel = (*env)->GetFieldID(env, XImageFc.clazz, "add_pixel", "I");
	XImageFc.cached = 1;
}

XImage *getXImageFields(JNIEnv *env, jobject lpObject, XImage *lpStruct)
{
	if (!XImageFc.cached) cacheXImageFields(env, lpObject);
	lpStruct->width = (*env)->GetIntField(env, lpObject, XImageFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, XImageFc.height);
	lpStruct->xoffset = (*env)->GetIntField(env, lpObject, XImageFc.xoffset);
	lpStruct->format = (*env)->GetIntField(env, lpObject, XImageFc.format);
	lpStruct->data = (char *)(*env)->GetIntField(env, lpObject, XImageFc.data);
	lpStruct->byte_order = (*env)->GetIntField(env, lpObject, XImageFc.byte_order);
	lpStruct->bitmap_unit = (*env)->GetIntField(env, lpObject, XImageFc.bitmap_unit);
	lpStruct->bitmap_bit_order = (*env)->GetIntField(env, lpObject, XImageFc.bitmap_bit_order);
	lpStruct->bitmap_pad = (*env)->GetIntField(env, lpObject, XImageFc.bitmap_pad);
	lpStruct->depth = (*env)->GetIntField(env, lpObject, XImageFc.depth);
	lpStruct->bytes_per_line = (*env)->GetIntField(env, lpObject, XImageFc.bytes_per_line);
	lpStruct->bits_per_pixel = (*env)->GetIntField(env, lpObject, XImageFc.bits_per_pixel);
	lpStruct->red_mask = (*env)->GetIntField(env, lpObject, XImageFc.red_mask);
	lpStruct->green_mask = (*env)->GetIntField(env, lpObject, XImageFc.green_mask);
	lpStruct->blue_mask = (*env)->GetIntField(env, lpObject, XImageFc.blue_mask);
	lpStruct->obdata = (XPointer)(*env)->GetIntField(env, lpObject, XImageFc.obdata);
	lpStruct->f.create_image = (XImage *(*)())(*env)->GetIntField(env, lpObject, XImageFc.create_image);
	lpStruct->f.destroy_image = (int(*)())(*env)->GetIntField(env, lpObject, XImageFc.destroy_image);
	lpStruct->f.get_pixel = (unsigned long(*)())(*env)->GetIntField(env, lpObject, XImageFc.get_pixel);
	lpStruct->f.put_pixel = (int(*)())(*env)->GetIntField(env, lpObject, XImageFc.put_pixel);
	lpStruct->f.sub_image = (XImage *(*)())(*env)->GetIntField(env, lpObject, XImageFc.sub_image);
	lpStruct->f.add_pixel = (int(*)())(*env)->GetIntField(env, lpObject, XImageFc.add_pixel);
	return lpStruct;
}

void setXImageFields(JNIEnv *env, jobject lpObject, XImage *lpStruct)
{
	if (!XImageFc.cached) cacheXImageFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XImageFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, XImageFc.height, (jint)lpStruct->height);
	(*env)->SetIntField(env, lpObject, XImageFc.xoffset, (jint)lpStruct->xoffset);
	(*env)->SetIntField(env, lpObject, XImageFc.format, (jint)lpStruct->format);
	(*env)->SetIntField(env, lpObject, XImageFc.data, (jint)lpStruct->data);
	(*env)->SetIntField(env, lpObject, XImageFc.byte_order, (jint)lpStruct->byte_order);
	(*env)->SetIntField(env, lpObject, XImageFc.bitmap_unit, (jint)lpStruct->bitmap_unit);
	(*env)->SetIntField(env, lpObject, XImageFc.bitmap_bit_order, (jint)lpStruct->bitmap_bit_order);
	(*env)->SetIntField(env, lpObject, XImageFc.bitmap_pad, (jint)lpStruct->bitmap_pad);
	(*env)->SetIntField(env, lpObject, XImageFc.depth, (jint)lpStruct->depth);
	(*env)->SetIntField(env, lpObject, XImageFc.bytes_per_line, (jint)lpStruct->bytes_per_line);
	(*env)->SetIntField(env, lpObject, XImageFc.bits_per_pixel, (jint)lpStruct->bits_per_pixel);
	(*env)->SetIntField(env, lpObject, XImageFc.red_mask, (jint)lpStruct->red_mask);
	(*env)->SetIntField(env, lpObject, XImageFc.green_mask, (jint)lpStruct->green_mask);
	(*env)->SetIntField(env, lpObject, XImageFc.blue_mask, (jint)lpStruct->blue_mask);
	(*env)->SetIntField(env, lpObject, XImageFc.obdata, (jint)lpStruct->obdata);
	(*env)->SetIntField(env, lpObject, XImageFc.create_image, (jint)lpStruct->f.create_image);
	(*env)->SetIntField(env, lpObject, XImageFc.destroy_image, (jint)lpStruct->f.destroy_image);
	(*env)->SetIntField(env, lpObject, XImageFc.get_pixel, (jint)lpStruct->f.get_pixel);
	(*env)->SetIntField(env, lpObject, XImageFc.put_pixel, (jint)lpStruct->f.put_pixel);
	(*env)->SetIntField(env, lpObject, XImageFc.sub_image, (jint)lpStruct->f.sub_image);
	(*env)->SetIntField(env, lpObject, XImageFc.add_pixel, (jint)lpStruct->f.add_pixel);
}
#endif

#ifndef NO_XKeyEvent
typedef struct XKeyEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID root, subwindow, time, x, y, x_root, y_root, state, keycode, same_screen;
} XKeyEvent_FID_CACHE;

XKeyEvent_FID_CACHE XKeyEventFc;

void cacheXKeyEventFields(JNIEnv *env, jobject lpObject)
{
	if (XKeyEventFc.cached) return;
	cacheXAnyEventFields(env, lpObject);
	XKeyEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XKeyEventFc.root = (*env)->GetFieldID(env, XKeyEventFc.clazz, "root", "I");
	XKeyEventFc.subwindow = (*env)->GetFieldID(env, XKeyEventFc.clazz, "subwindow", "I");
	XKeyEventFc.time = (*env)->GetFieldID(env, XKeyEventFc.clazz, "time", "I");
	XKeyEventFc.x = (*env)->GetFieldID(env, XKeyEventFc.clazz, "x", "I");
	XKeyEventFc.y = (*env)->GetFieldID(env, XKeyEventFc.clazz, "y", "I");
	XKeyEventFc.x_root = (*env)->GetFieldID(env, XKeyEventFc.clazz, "x_root", "I");
	XKeyEventFc.y_root = (*env)->GetFieldID(env, XKeyEventFc.clazz, "y_root", "I");
	XKeyEventFc.state = (*env)->GetFieldID(env, XKeyEventFc.clazz, "state", "I");
	XKeyEventFc.keycode = (*env)->GetFieldID(env, XKeyEventFc.clazz, "keycode", "I");
	XKeyEventFc.same_screen = (*env)->GetFieldID(env, XKeyEventFc.clazz, "same_screen", "I");
	XKeyEventFc.cached = 1;
}

XKeyEvent *getXKeyEventFields(JNIEnv *env, jobject lpObject, XKeyEvent *lpStruct)
{
	if (!XKeyEventFc.cached) cacheXKeyEventFields(env, lpObject);
	getXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	lpStruct->root = (*env)->GetIntField(env, lpObject, XKeyEventFc.root);
	lpStruct->subwindow = (*env)->GetIntField(env, lpObject, XKeyEventFc.subwindow);
	lpStruct->time = (*env)->GetIntField(env, lpObject, XKeyEventFc.time);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XKeyEventFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XKeyEventFc.y);
	lpStruct->x_root = (*env)->GetIntField(env, lpObject, XKeyEventFc.x_root);
	lpStruct->y_root = (*env)->GetIntField(env, lpObject, XKeyEventFc.y_root);
	lpStruct->state = (*env)->GetIntField(env, lpObject, XKeyEventFc.state);
	lpStruct->keycode = (*env)->GetIntField(env, lpObject, XKeyEventFc.keycode);
	lpStruct->same_screen = (*env)->GetIntField(env, lpObject, XKeyEventFc.same_screen);
	return lpStruct;
}

void setXKeyEventFields(JNIEnv *env, jobject lpObject, XKeyEvent *lpStruct)
{
	if (!XKeyEventFc.cached) cacheXKeyEventFields(env, lpObject);
	setXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XKeyEventFc.root, (jint)lpStruct->root);
	(*env)->SetIntField(env, lpObject, XKeyEventFc.subwindow, (jint)lpStruct->subwindow);
	(*env)->SetIntField(env, lpObject, XKeyEventFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, XKeyEventFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XKeyEventFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XKeyEventFc.x_root, (jint)lpStruct->x_root);
	(*env)->SetIntField(env, lpObject, XKeyEventFc.y_root, (jint)lpStruct->y_root);
	(*env)->SetIntField(env, lpObject, XKeyEventFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, XKeyEventFc.keycode, (jint)lpStruct->keycode);
	(*env)->SetIntField(env, lpObject, XKeyEventFc.same_screen, (jint)lpStruct->same_screen);
}
#endif

#ifndef NO_XModifierKeymap
typedef struct XModifierKeymap_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID max_keypermod, modifiermap;
} XModifierKeymap_FID_CACHE;

XModifierKeymap_FID_CACHE XModifierKeymapFc;

void cacheXModifierKeymapFields(JNIEnv *env, jobject lpObject)
{
	if (XModifierKeymapFc.cached) return;
	XModifierKeymapFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XModifierKeymapFc.max_keypermod = (*env)->GetFieldID(env, XModifierKeymapFc.clazz, "max_keypermod", "I");
	XModifierKeymapFc.modifiermap = (*env)->GetFieldID(env, XModifierKeymapFc.clazz, "modifiermap", "I");
	XModifierKeymapFc.cached = 1;
}

XModifierKeymap *getXModifierKeymapFields(JNIEnv *env, jobject lpObject, XModifierKeymap *lpStruct)
{
	if (!XModifierKeymapFc.cached) cacheXModifierKeymapFields(env, lpObject);
	lpStruct->max_keypermod = (*env)->GetIntField(env, lpObject, XModifierKeymapFc.max_keypermod);
	lpStruct->modifiermap = (KeyCode *)(*env)->GetIntField(env, lpObject, XModifierKeymapFc.modifiermap);
	return lpStruct;
}

void setXModifierKeymapFields(JNIEnv *env, jobject lpObject, XModifierKeymap *lpStruct)
{
	if (!XModifierKeymapFc.cached) cacheXModifierKeymapFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XModifierKeymapFc.max_keypermod, (jint)lpStruct->max_keypermod);
	(*env)->SetIntField(env, lpObject, XModifierKeymapFc.modifiermap, (jint)lpStruct->modifiermap);
}
#endif

#ifndef NO_XMotionEvent
typedef struct XMotionEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID root, subwindow, time, x, y, x_root, y_root, state, is_hint, same_screen;
} XMotionEvent_FID_CACHE;

XMotionEvent_FID_CACHE XMotionEventFc;

void cacheXMotionEventFields(JNIEnv *env, jobject lpObject)
{
	if (XMotionEventFc.cached) return;
	cacheXAnyEventFields(env, lpObject);
	XMotionEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XMotionEventFc.root = (*env)->GetFieldID(env, XMotionEventFc.clazz, "root", "I");
	XMotionEventFc.subwindow = (*env)->GetFieldID(env, XMotionEventFc.clazz, "subwindow", "I");
	XMotionEventFc.time = (*env)->GetFieldID(env, XMotionEventFc.clazz, "time", "I");
	XMotionEventFc.x = (*env)->GetFieldID(env, XMotionEventFc.clazz, "x", "I");
	XMotionEventFc.y = (*env)->GetFieldID(env, XMotionEventFc.clazz, "y", "I");
	XMotionEventFc.x_root = (*env)->GetFieldID(env, XMotionEventFc.clazz, "x_root", "I");
	XMotionEventFc.y_root = (*env)->GetFieldID(env, XMotionEventFc.clazz, "y_root", "I");
	XMotionEventFc.state = (*env)->GetFieldID(env, XMotionEventFc.clazz, "state", "I");
	XMotionEventFc.is_hint = (*env)->GetFieldID(env, XMotionEventFc.clazz, "is_hint", "I");
	XMotionEventFc.same_screen = (*env)->GetFieldID(env, XMotionEventFc.clazz, "same_screen", "I");
	XMotionEventFc.cached = 1;
}

XMotionEvent *getXMotionEventFields(JNIEnv *env, jobject lpObject, XMotionEvent *lpStruct)
{
	if (!XMotionEventFc.cached) cacheXMotionEventFields(env, lpObject);
	getXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	lpStruct->root = (*env)->GetIntField(env, lpObject, XMotionEventFc.root);
	lpStruct->subwindow = (*env)->GetIntField(env, lpObject, XMotionEventFc.subwindow);
	lpStruct->time = (*env)->GetIntField(env, lpObject, XMotionEventFc.time);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XMotionEventFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XMotionEventFc.y);
	lpStruct->x_root = (*env)->GetIntField(env, lpObject, XMotionEventFc.x_root);
	lpStruct->y_root = (*env)->GetIntField(env, lpObject, XMotionEventFc.y_root);
	lpStruct->state = (*env)->GetIntField(env, lpObject, XMotionEventFc.state);
	lpStruct->is_hint = (*env)->GetIntField(env, lpObject, XMotionEventFc.is_hint);
	lpStruct->same_screen = (*env)->GetIntField(env, lpObject, XMotionEventFc.same_screen);
	return lpStruct;
}

void setXMotionEventFields(JNIEnv *env, jobject lpObject, XMotionEvent *lpStruct)
{
	if (!XMotionEventFc.cached) cacheXMotionEventFields(env, lpObject);
	setXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XMotionEventFc.root, (jint)lpStruct->root);
	(*env)->SetIntField(env, lpObject, XMotionEventFc.subwindow, (jint)lpStruct->subwindow);
	(*env)->SetIntField(env, lpObject, XMotionEventFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, XMotionEventFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XMotionEventFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XMotionEventFc.x_root, (jint)lpStruct->x_root);
	(*env)->SetIntField(env, lpObject, XMotionEventFc.y_root, (jint)lpStruct->y_root);
	(*env)->SetIntField(env, lpObject, XMotionEventFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, XMotionEventFc.is_hint, (jint)lpStruct->is_hint);
	(*env)->SetIntField(env, lpObject, XMotionEventFc.same_screen, (jint)lpStruct->same_screen);
}
#endif

#ifndef NO_XPropertyEvent
typedef struct XPropertyEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID atom, time, state;
} XPropertyEvent_FID_CACHE;

XPropertyEvent_FID_CACHE XPropertyEventFc;

void cacheXPropertyEventFields(JNIEnv *env, jobject lpObject)
{
	if (XPropertyEventFc.cached) return;
	cacheXAnyEventFields(env, lpObject);
	XPropertyEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XPropertyEventFc.atom = (*env)->GetFieldID(env, XPropertyEventFc.clazz, "atom", "I");
	XPropertyEventFc.time = (*env)->GetFieldID(env, XPropertyEventFc.clazz, "time", "I");
	XPropertyEventFc.state = (*env)->GetFieldID(env, XPropertyEventFc.clazz, "state", "I");
	XPropertyEventFc.cached = 1;
}

XPropertyEvent *getXPropertyEventFields(JNIEnv *env, jobject lpObject, XPropertyEvent *lpStruct)
{
	if (!XPropertyEventFc.cached) cacheXPropertyEventFields(env, lpObject);
	getXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	lpStruct->atom = (*env)->GetIntField(env, lpObject, XPropertyEventFc.atom);
	lpStruct->time = (*env)->GetIntField(env, lpObject, XPropertyEventFc.time);
	lpStruct->state = (*env)->GetIntField(env, lpObject, XPropertyEventFc.state);
	return lpStruct;
}

void setXPropertyEventFields(JNIEnv *env, jobject lpObject, XPropertyEvent *lpStruct)
{
	if (!XPropertyEventFc.cached) cacheXPropertyEventFields(env, lpObject);
	setXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XPropertyEventFc.atom, (jint)lpStruct->atom);
	(*env)->SetIntField(env, lpObject, XPropertyEventFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, XPropertyEventFc.state, (jint)lpStruct->state);
}
#endif

#ifndef NO_XRectangle
typedef struct XRectangle_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height;
} XRectangle_FID_CACHE;

XRectangle_FID_CACHE XRectangleFc;

void cacheXRectangleFields(JNIEnv *env, jobject lpObject)
{
	if (XRectangleFc.cached) return;
	XRectangleFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XRectangleFc.x = (*env)->GetFieldID(env, XRectangleFc.clazz, "x", "S");
	XRectangleFc.y = (*env)->GetFieldID(env, XRectangleFc.clazz, "y", "S");
	XRectangleFc.width = (*env)->GetFieldID(env, XRectangleFc.clazz, "width", "S");
	XRectangleFc.height = (*env)->GetFieldID(env, XRectangleFc.clazz, "height", "S");
	XRectangleFc.cached = 1;
}

XRectangle *getXRectangleFields(JNIEnv *env, jobject lpObject, XRectangle *lpStruct)
{
	if (!XRectangleFc.cached) cacheXRectangleFields(env, lpObject);
	lpStruct->x = (*env)->GetShortField(env, lpObject, XRectangleFc.x);
	lpStruct->y = (*env)->GetShortField(env, lpObject, XRectangleFc.y);
	lpStruct->width = (*env)->GetShortField(env, lpObject, XRectangleFc.width);
	lpStruct->height = (*env)->GetShortField(env, lpObject, XRectangleFc.height);
	return lpStruct;
}

void setXRectangleFields(JNIEnv *env, jobject lpObject, XRectangle *lpStruct)
{
	if (!XRectangleFc.cached) cacheXRectangleFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, XRectangleFc.x, (jshort)lpStruct->x);
	(*env)->SetShortField(env, lpObject, XRectangleFc.y, (jshort)lpStruct->y);
	(*env)->SetShortField(env, lpObject, XRectangleFc.width, (jshort)lpStruct->width);
	(*env)->SetShortField(env, lpObject, XRectangleFc.height, (jshort)lpStruct->height);
}
#endif

#ifndef NO_XRenderPictureAttributes
typedef struct XRenderPictureAttributes_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID repeat, alpha_map, alpha_x_origin, alpha_y_origin, clip_x_origin, clip_y_origin, clip_mask, graphics_exposures, subwindow_mode, poly_edge, poly_mode, dither, component_alpha;
} XRenderPictureAttributes_FID_CACHE;

XRenderPictureAttributes_FID_CACHE XRenderPictureAttributesFc;

void cacheXRenderPictureAttributesFields(JNIEnv *env, jobject lpObject)
{
	if (XRenderPictureAttributesFc.cached) return;
	XRenderPictureAttributesFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XRenderPictureAttributesFc.repeat = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "repeat", "Z");
	XRenderPictureAttributesFc.alpha_map = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "alpha_map", I_J);
	XRenderPictureAttributesFc.alpha_x_origin = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "alpha_x_origin", "I");
	XRenderPictureAttributesFc.alpha_y_origin = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "alpha_y_origin", "I");
	XRenderPictureAttributesFc.clip_x_origin = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "clip_x_origin", "I");
	XRenderPictureAttributesFc.clip_y_origin = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "clip_y_origin", "I");
	XRenderPictureAttributesFc.clip_mask = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "clip_mask", I_J);
	XRenderPictureAttributesFc.graphics_exposures = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "graphics_exposures", "Z");
	XRenderPictureAttributesFc.subwindow_mode = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "subwindow_mode", "I");
	XRenderPictureAttributesFc.poly_edge = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "poly_edge", "I");
	XRenderPictureAttributesFc.poly_mode = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "poly_mode", "I");
	XRenderPictureAttributesFc.dither = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "dither", I_J);
	XRenderPictureAttributesFc.component_alpha = (*env)->GetFieldID(env, XRenderPictureAttributesFc.clazz, "component_alpha", "Z");
	XRenderPictureAttributesFc.cached = 1;
}

XRenderPictureAttributes *getXRenderPictureAttributesFields(JNIEnv *env, jobject lpObject, XRenderPictureAttributes *lpStruct)
{
	if (!XRenderPictureAttributesFc.cached) cacheXRenderPictureAttributesFields(env, lpObject);
	lpStruct->repeat = (*env)->GetBooleanField(env, lpObject, XRenderPictureAttributesFc.repeat);
	lpStruct->alpha_map = (*env)->GetIntLongField(env, lpObject, XRenderPictureAttributesFc.alpha_map);
	lpStruct->alpha_x_origin = (*env)->GetIntField(env, lpObject, XRenderPictureAttributesFc.alpha_x_origin);
	lpStruct->alpha_y_origin = (*env)->GetIntField(env, lpObject, XRenderPictureAttributesFc.alpha_y_origin);
	lpStruct->clip_x_origin = (*env)->GetIntField(env, lpObject, XRenderPictureAttributesFc.clip_x_origin);
	lpStruct->clip_y_origin = (*env)->GetIntField(env, lpObject, XRenderPictureAttributesFc.clip_y_origin);
	lpStruct->clip_mask = (*env)->GetIntLongField(env, lpObject, XRenderPictureAttributesFc.clip_mask);
	lpStruct->graphics_exposures = (*env)->GetBooleanField(env, lpObject, XRenderPictureAttributesFc.graphics_exposures);
	lpStruct->subwindow_mode = (*env)->GetIntField(env, lpObject, XRenderPictureAttributesFc.subwindow_mode);
	lpStruct->poly_edge = (*env)->GetIntField(env, lpObject, XRenderPictureAttributesFc.poly_edge);
	lpStruct->poly_mode = (*env)->GetIntField(env, lpObject, XRenderPictureAttributesFc.poly_mode);
	lpStruct->dither = (*env)->GetIntLongField(env, lpObject, XRenderPictureAttributesFc.dither);
	lpStruct->component_alpha = (*env)->GetBooleanField(env, lpObject, XRenderPictureAttributesFc.component_alpha);
	return lpStruct;
}

void setXRenderPictureAttributesFields(JNIEnv *env, jobject lpObject, XRenderPictureAttributes *lpStruct)
{
	if (!XRenderPictureAttributesFc.cached) cacheXRenderPictureAttributesFields(env, lpObject);
	(*env)->SetBooleanField(env, lpObject, XRenderPictureAttributesFc.repeat, (jboolean)lpStruct->repeat);
	(*env)->SetIntLongField(env, lpObject, XRenderPictureAttributesFc.alpha_map, (jintLong)lpStruct->alpha_map);
	(*env)->SetIntField(env, lpObject, XRenderPictureAttributesFc.alpha_x_origin, (jint)lpStruct->alpha_x_origin);
	(*env)->SetIntField(env, lpObject, XRenderPictureAttributesFc.alpha_y_origin, (jint)lpStruct->alpha_y_origin);
	(*env)->SetIntField(env, lpObject, XRenderPictureAttributesFc.clip_x_origin, (jint)lpStruct->clip_x_origin);
	(*env)->SetIntField(env, lpObject, XRenderPictureAttributesFc.clip_y_origin, (jint)lpStruct->clip_y_origin);
	(*env)->SetIntLongField(env, lpObject, XRenderPictureAttributesFc.clip_mask, (jintLong)lpStruct->clip_mask);
	(*env)->SetBooleanField(env, lpObject, XRenderPictureAttributesFc.graphics_exposures, (jboolean)lpStruct->graphics_exposures);
	(*env)->SetIntField(env, lpObject, XRenderPictureAttributesFc.subwindow_mode, (jint)lpStruct->subwindow_mode);
	(*env)->SetIntField(env, lpObject, XRenderPictureAttributesFc.poly_edge, (jint)lpStruct->poly_edge);
	(*env)->SetIntField(env, lpObject, XRenderPictureAttributesFc.poly_mode, (jint)lpStruct->poly_mode);
	(*env)->SetIntLongField(env, lpObject, XRenderPictureAttributesFc.dither, (jintLong)lpStruct->dither);
	(*env)->SetBooleanField(env, lpObject, XRenderPictureAttributesFc.component_alpha, (jboolean)lpStruct->component_alpha);
}
#endif

#ifndef NO_XReparentEvent
typedef struct XReparentEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID serial, send_event, display, event, window, parent, x, y, override_redirect;
} XReparentEvent_FID_CACHE;

XReparentEvent_FID_CACHE XReparentEventFc;

void cacheXReparentEventFields(JNIEnv *env, jobject lpObject)
{
	if (XReparentEventFc.cached) return;
	cacheXEventFields(env, lpObject);
	XReparentEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XReparentEventFc.serial = (*env)->GetFieldID(env, XReparentEventFc.clazz, "serial", "I");
	XReparentEventFc.send_event = (*env)->GetFieldID(env, XReparentEventFc.clazz, "send_event", "I");
	XReparentEventFc.display = (*env)->GetFieldID(env, XReparentEventFc.clazz, "display", "I");
	XReparentEventFc.event = (*env)->GetFieldID(env, XReparentEventFc.clazz, "event", "I");
	XReparentEventFc.window = (*env)->GetFieldID(env, XReparentEventFc.clazz, "window", "I");
	XReparentEventFc.parent = (*env)->GetFieldID(env, XReparentEventFc.clazz, "parent", "I");
	XReparentEventFc.x = (*env)->GetFieldID(env, XReparentEventFc.clazz, "x", "I");
	XReparentEventFc.y = (*env)->GetFieldID(env, XReparentEventFc.clazz, "y", "I");
	XReparentEventFc.override_redirect = (*env)->GetFieldID(env, XReparentEventFc.clazz, "override_redirect", "I");
	XReparentEventFc.cached = 1;
}

XReparentEvent *getXReparentEventFields(JNIEnv *env, jobject lpObject, XReparentEvent *lpStruct)
{
	if (!XReparentEventFc.cached) cacheXReparentEventFields(env, lpObject);
	getXEventFields(env, lpObject, (XEvent *)lpStruct);
	lpStruct->serial = (*env)->GetIntField(env, lpObject, XReparentEventFc.serial);
	lpStruct->send_event = (*env)->GetIntField(env, lpObject, XReparentEventFc.send_event);
	lpStruct->display = (Display *)(*env)->GetIntField(env, lpObject, XReparentEventFc.display);
	lpStruct->event = (Window)(*env)->GetIntField(env, lpObject, XReparentEventFc.event);
	lpStruct->window = (Window)(*env)->GetIntField(env, lpObject, XReparentEventFc.window);
	lpStruct->parent = (Window)(*env)->GetIntField(env, lpObject, XReparentEventFc.parent);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XReparentEventFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XReparentEventFc.y);
	lpStruct->override_redirect = (*env)->GetIntField(env, lpObject, XReparentEventFc.override_redirect);
	return lpStruct;
}

void setXReparentEventFields(JNIEnv *env, jobject lpObject, XReparentEvent *lpStruct)
{
	if (!XReparentEventFc.cached) cacheXReparentEventFields(env, lpObject);
	setXEventFields(env, lpObject, (XEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XReparentEventFc.serial, (jint)lpStruct->serial);
	(*env)->SetIntField(env, lpObject, XReparentEventFc.send_event, (jint)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, XReparentEventFc.display, (jint)lpStruct->display);
	(*env)->SetIntField(env, lpObject, XReparentEventFc.event, (jint)lpStruct->event);
	(*env)->SetIntField(env, lpObject, XReparentEventFc.window, (jint)lpStruct->window);
	(*env)->SetIntField(env, lpObject, XReparentEventFc.parent, (jint)lpStruct->parent);
	(*env)->SetIntField(env, lpObject, XReparentEventFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XReparentEventFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XReparentEventFc.override_redirect, (jint)lpStruct->override_redirect);
}
#endif

#ifndef NO_XSetWindowAttributes
typedef struct XSetWindowAttributes_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID background_pixmap, background_pixel, border_pixmap, border_pixel, bit_gravity, win_gravity, backing_store, backing_planes, backing_pixel, save_under, event_mask, do_not_propagate_mask, override_redirect, colormap, cursor;
} XSetWindowAttributes_FID_CACHE;

XSetWindowAttributes_FID_CACHE XSetWindowAttributesFc;

void cacheXSetWindowAttributesFields(JNIEnv *env, jobject lpObject)
{
	if (XSetWindowAttributesFc.cached) return;
	XSetWindowAttributesFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XSetWindowAttributesFc.background_pixmap = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "background_pixmap", "I");
	XSetWindowAttributesFc.background_pixel = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "background_pixel", "I");
	XSetWindowAttributesFc.border_pixmap = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "border_pixmap", "I");
	XSetWindowAttributesFc.border_pixel = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "border_pixel", "I");
	XSetWindowAttributesFc.bit_gravity = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "bit_gravity", "I");
	XSetWindowAttributesFc.win_gravity = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "win_gravity", "I");
	XSetWindowAttributesFc.backing_store = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "backing_store", "I");
	XSetWindowAttributesFc.backing_planes = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "backing_planes", "I");
	XSetWindowAttributesFc.backing_pixel = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "backing_pixel", "I");
	XSetWindowAttributesFc.save_under = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "save_under", "I");
	XSetWindowAttributesFc.event_mask = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "event_mask", "I");
	XSetWindowAttributesFc.do_not_propagate_mask = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "do_not_propagate_mask", "I");
	XSetWindowAttributesFc.override_redirect = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "override_redirect", "I");
	XSetWindowAttributesFc.colormap = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "colormap", "I");
	XSetWindowAttributesFc.cursor = (*env)->GetFieldID(env, XSetWindowAttributesFc.clazz, "cursor", "I");
	XSetWindowAttributesFc.cached = 1;
}

XSetWindowAttributes *getXSetWindowAttributesFields(JNIEnv *env, jobject lpObject, XSetWindowAttributes *lpStruct)
{
	if (!XSetWindowAttributesFc.cached) cacheXSetWindowAttributesFields(env, lpObject);
	lpStruct->background_pixmap = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.background_pixmap);
	lpStruct->background_pixel = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.background_pixel);
	lpStruct->border_pixmap = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.border_pixmap);
	lpStruct->border_pixel = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.border_pixel);
	lpStruct->bit_gravity = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.bit_gravity);
	lpStruct->win_gravity = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.win_gravity);
	lpStruct->backing_store = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.backing_store);
	lpStruct->backing_planes = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.backing_planes);
	lpStruct->backing_pixel = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.backing_pixel);
	lpStruct->save_under = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.save_under);
	lpStruct->event_mask = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.event_mask);
	lpStruct->do_not_propagate_mask = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.do_not_propagate_mask);
	lpStruct->override_redirect = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.override_redirect);
	lpStruct->colormap = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.colormap);
	lpStruct->cursor = (*env)->GetIntField(env, lpObject, XSetWindowAttributesFc.cursor);
	return lpStruct;
}

void setXSetWindowAttributesFields(JNIEnv *env, jobject lpObject, XSetWindowAttributes *lpStruct)
{
	if (!XSetWindowAttributesFc.cached) cacheXSetWindowAttributesFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.background_pixmap, (jint)lpStruct->background_pixmap);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.background_pixel, (jint)lpStruct->background_pixel);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.border_pixmap, (jint)lpStruct->border_pixmap);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.border_pixel, (jint)lpStruct->border_pixel);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.bit_gravity, (jint)lpStruct->bit_gravity);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.win_gravity, (jint)lpStruct->win_gravity);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.backing_store, (jint)lpStruct->backing_store);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.backing_planes, (jint)lpStruct->backing_planes);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.backing_pixel, (jint)lpStruct->backing_pixel);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.save_under, (jint)lpStruct->save_under);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.event_mask, (jint)lpStruct->event_mask);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.do_not_propagate_mask, (jint)lpStruct->do_not_propagate_mask);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.override_redirect, (jint)lpStruct->override_redirect);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.colormap, (jint)lpStruct->colormap);
	(*env)->SetIntField(env, lpObject, XSetWindowAttributesFc.cursor, (jint)lpStruct->cursor);
}
#endif

#ifndef NO_XSizeHints
typedef struct XSizeHints_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID flags, x, y, width, height, min_width, min_height, max_width, max_height, width_inc, height_inc, aspect_x, aspect_y, base_width, base_height, win_gravity;
} XSizeHints_FID_CACHE;

XSizeHints_FID_CACHE XSizeHintsFc;

void cacheXSizeHintsFields(JNIEnv *env, jobject lpObject)
{
	if (XSizeHintsFc.cached) return;
	XSizeHintsFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XSizeHintsFc.flags = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "flags", "I");
	XSizeHintsFc.x = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "x", "I");
	XSizeHintsFc.y = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "y", "I");
	XSizeHintsFc.width = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "width", "I");
	XSizeHintsFc.height = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "height", "I");
	XSizeHintsFc.min_width = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "min_width", "I");
	XSizeHintsFc.min_height = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "min_height", "I");
	XSizeHintsFc.max_width = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "max_width", "I");
	XSizeHintsFc.max_height = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "max_height", "I");
	XSizeHintsFc.width_inc = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "width_inc", "I");
	XSizeHintsFc.height_inc = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "height_inc", "I");
	XSizeHintsFc.aspect_x = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "aspect_x", "I");
	XSizeHintsFc.aspect_y = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "aspect_y", "I");
	XSizeHintsFc.base_width = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "base_width", "I");
	XSizeHintsFc.base_height = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "base_height", "I");
	XSizeHintsFc.win_gravity = (*env)->GetFieldID(env, XSizeHintsFc.clazz, "win_gravity", "I");
	XSizeHintsFc.cached = 1;
}

XSizeHints *getXSizeHintsFields(JNIEnv *env, jobject lpObject, XSizeHints *lpStruct)
{
	if (!XSizeHintsFc.cached) cacheXSizeHintsFields(env, lpObject);
	lpStruct->flags = (*env)->GetIntField(env, lpObject, XSizeHintsFc.flags);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XSizeHintsFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XSizeHintsFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, XSizeHintsFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, XSizeHintsFc.height);
	lpStruct->min_width = (*env)->GetIntField(env, lpObject, XSizeHintsFc.min_width);
	lpStruct->min_height = (*env)->GetIntField(env, lpObject, XSizeHintsFc.min_height);
	lpStruct->max_width = (*env)->GetIntField(env, lpObject, XSizeHintsFc.max_width);
	lpStruct->max_height = (*env)->GetIntField(env, lpObject, XSizeHintsFc.max_height);
	lpStruct->width_inc = (*env)->GetIntField(env, lpObject, XSizeHintsFc.width_inc);
	lpStruct->height_inc = (*env)->GetIntField(env, lpObject, XSizeHintsFc.height_inc);
	lpStruct->min_aspect.x = (*env)->GetIntField(env, lpObject, XSizeHintsFc.aspect_x);
	lpStruct->min_aspect.y = (*env)->GetIntField(env, lpObject, XSizeHintsFc.aspect_y);
	lpStruct->base_width = (*env)->GetIntField(env, lpObject, XSizeHintsFc.base_width);
	lpStruct->base_height = (*env)->GetIntField(env, lpObject, XSizeHintsFc.base_height);
	lpStruct->win_gravity = (*env)->GetIntField(env, lpObject, XSizeHintsFc.win_gravity);
	return lpStruct;
}

void setXSizeHintsFields(JNIEnv *env, jobject lpObject, XSizeHints *lpStruct)
{
	if (!XSizeHintsFc.cached) cacheXSizeHintsFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.height, (jint)lpStruct->height);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.min_width, (jint)lpStruct->min_width);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.min_height, (jint)lpStruct->min_height);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.max_width, (jint)lpStruct->max_width);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.max_height, (jint)lpStruct->max_height);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.width_inc, (jint)lpStruct->width_inc);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.height_inc, (jint)lpStruct->height_inc);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.aspect_x, (jint)lpStruct->min_aspect.x);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.aspect_y, (jint)lpStruct->min_aspect.y);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.base_width, (jint)lpStruct->base_width);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.base_height, (jint)lpStruct->base_height);
	(*env)->SetIntField(env, lpObject, XSizeHintsFc.win_gravity, (jint)lpStruct->win_gravity);
}
#endif

#ifndef NO_XTextProperty
typedef struct XTextProperty_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID value, encoding, format, nitems;
} XTextProperty_FID_CACHE;

XTextProperty_FID_CACHE XTextPropertyFc;

void cacheXTextPropertyFields(JNIEnv *env, jobject lpObject)
{
	if (XTextPropertyFc.cached) return;
	XTextPropertyFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XTextPropertyFc.value = (*env)->GetFieldID(env, XTextPropertyFc.clazz, "value", "I");
	XTextPropertyFc.encoding = (*env)->GetFieldID(env, XTextPropertyFc.clazz, "encoding", "I");
	XTextPropertyFc.format = (*env)->GetFieldID(env, XTextPropertyFc.clazz, "format", "I");
	XTextPropertyFc.nitems = (*env)->GetFieldID(env, XTextPropertyFc.clazz, "nitems", "I");
	XTextPropertyFc.cached = 1;
}

XTextProperty *getXTextPropertyFields(JNIEnv *env, jobject lpObject, XTextProperty *lpStruct)
{
	if (!XTextPropertyFc.cached) cacheXTextPropertyFields(env, lpObject);
	lpStruct->value = (unsigned char *)(*env)->GetIntField(env, lpObject, XTextPropertyFc.value);
	lpStruct->encoding = (*env)->GetIntField(env, lpObject, XTextPropertyFc.encoding);
	lpStruct->format = (*env)->GetIntField(env, lpObject, XTextPropertyFc.format);
	lpStruct->nitems = (*env)->GetIntField(env, lpObject, XTextPropertyFc.nitems);
	return lpStruct;
}

void setXTextPropertyFields(JNIEnv *env, jobject lpObject, XTextProperty *lpStruct)
{
	if (!XTextPropertyFc.cached) cacheXTextPropertyFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XTextPropertyFc.value, (jint)lpStruct->value);
	(*env)->SetIntField(env, lpObject, XTextPropertyFc.encoding, (jint)lpStruct->encoding);
	(*env)->SetIntField(env, lpObject, XTextPropertyFc.format, (jint)lpStruct->format);
	(*env)->SetIntField(env, lpObject, XTextPropertyFc.nitems, (jint)lpStruct->nitems);
}
#endif

#ifndef NO_XWindowAttributes
typedef struct XWindowAttributes_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height, border_width, depth, visual, root, c_class, bit_gravity, win_gravity, backing_store, backing_planes, backing_pixel, save_under, colormap, map_installed, map_state, all_event_masks, your_event_mask, do_not_propagate_mask, override_redirect, screen;
} XWindowAttributes_FID_CACHE;

XWindowAttributes_FID_CACHE XWindowAttributesFc;

void cacheXWindowAttributesFields(JNIEnv *env, jobject lpObject)
{
	if (XWindowAttributesFc.cached) return;
	XWindowAttributesFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XWindowAttributesFc.x = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "x", "I");
	XWindowAttributesFc.y = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "y", "I");
	XWindowAttributesFc.width = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "width", "I");
	XWindowAttributesFc.height = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "height", "I");
	XWindowAttributesFc.border_width = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "border_width", "I");
	XWindowAttributesFc.depth = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "depth", "I");
	XWindowAttributesFc.visual = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "visual", "I");
	XWindowAttributesFc.root = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "root", "I");
	XWindowAttributesFc.c_class = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "c_class", "I");
	XWindowAttributesFc.bit_gravity = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "bit_gravity", "I");
	XWindowAttributesFc.win_gravity = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "win_gravity", "I");
	XWindowAttributesFc.backing_store = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "backing_store", "I");
	XWindowAttributesFc.backing_planes = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "backing_planes", "I");
	XWindowAttributesFc.backing_pixel = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "backing_pixel", "I");
	XWindowAttributesFc.save_under = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "save_under", "I");
	XWindowAttributesFc.colormap = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "colormap", "I");
	XWindowAttributesFc.map_installed = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "map_installed", "I");
	XWindowAttributesFc.map_state = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "map_state", "I");
	XWindowAttributesFc.all_event_masks = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "all_event_masks", "I");
	XWindowAttributesFc.your_event_mask = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "your_event_mask", "I");
	XWindowAttributesFc.do_not_propagate_mask = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "do_not_propagate_mask", "I");
	XWindowAttributesFc.override_redirect = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "override_redirect", "I");
	XWindowAttributesFc.screen = (*env)->GetFieldID(env, XWindowAttributesFc.clazz, "screen", "I");
	XWindowAttributesFc.cached = 1;
}

XWindowAttributes *getXWindowAttributesFields(JNIEnv *env, jobject lpObject, XWindowAttributes *lpStruct)
{
	if (!XWindowAttributesFc.cached) cacheXWindowAttributesFields(env, lpObject);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.height);
	lpStruct->border_width = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.border_width);
	lpStruct->depth = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.depth);
	lpStruct->visual = (Visual *)(*env)->GetIntField(env, lpObject, XWindowAttributesFc.visual);
	lpStruct->root = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.root);
	lpStruct->class = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.c_class);
	lpStruct->bit_gravity = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.bit_gravity);
	lpStruct->win_gravity = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.win_gravity);
	lpStruct->backing_store = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.backing_store);
	lpStruct->backing_planes = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.backing_planes);
	lpStruct->backing_pixel = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.backing_pixel);
	lpStruct->save_under = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.save_under);
	lpStruct->colormap = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.colormap);
	lpStruct->map_installed = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.map_installed);
	lpStruct->map_state = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.map_state);
	lpStruct->all_event_masks = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.all_event_masks);
	lpStruct->your_event_mask = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.your_event_mask);
	lpStruct->do_not_propagate_mask = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.do_not_propagate_mask);
	lpStruct->override_redirect = (*env)->GetIntField(env, lpObject, XWindowAttributesFc.override_redirect);
	lpStruct->screen = (Screen *)(*env)->GetIntField(env, lpObject, XWindowAttributesFc.screen);
	return lpStruct;
}

void setXWindowAttributesFields(JNIEnv *env, jobject lpObject, XWindowAttributes *lpStruct)
{
	if (!XWindowAttributesFc.cached) cacheXWindowAttributesFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.height, (jint)lpStruct->height);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.border_width, (jint)lpStruct->border_width);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.depth, (jint)lpStruct->depth);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.visual, (jint)lpStruct->visual);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.root, (jint)lpStruct->root);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.c_class, (jint)lpStruct->class);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.bit_gravity, (jint)lpStruct->bit_gravity);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.win_gravity, (jint)lpStruct->win_gravity);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.backing_store, (jint)lpStruct->backing_store);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.backing_planes, (jint)lpStruct->backing_planes);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.backing_pixel, (jint)lpStruct->backing_pixel);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.save_under, (jint)lpStruct->save_under);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.colormap, (jint)lpStruct->colormap);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.map_installed, (jint)lpStruct->map_installed);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.map_state, (jint)lpStruct->map_state);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.all_event_masks, (jint)lpStruct->all_event_masks);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.your_event_mask, (jint)lpStruct->your_event_mask);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.do_not_propagate_mask, (jint)lpStruct->do_not_propagate_mask);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.override_redirect, (jint)lpStruct->override_redirect);
	(*env)->SetIntField(env, lpObject, XWindowAttributesFc.screen, (jint)lpStruct->screen);
}
#endif

#ifndef NO_XWindowChanges
typedef struct XWindowChanges_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height, border_width, sibling, stack_mode;
} XWindowChanges_FID_CACHE;

XWindowChanges_FID_CACHE XWindowChangesFc;

void cacheXWindowChangesFields(JNIEnv *env, jobject lpObject)
{
	if (XWindowChangesFc.cached) return;
	XWindowChangesFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XWindowChangesFc.x = (*env)->GetFieldID(env, XWindowChangesFc.clazz, "x", "I");
	XWindowChangesFc.y = (*env)->GetFieldID(env, XWindowChangesFc.clazz, "y", "I");
	XWindowChangesFc.width = (*env)->GetFieldID(env, XWindowChangesFc.clazz, "width", "I");
	XWindowChangesFc.height = (*env)->GetFieldID(env, XWindowChangesFc.clazz, "height", "I");
	XWindowChangesFc.border_width = (*env)->GetFieldID(env, XWindowChangesFc.clazz, "border_width", "I");
	XWindowChangesFc.sibling = (*env)->GetFieldID(env, XWindowChangesFc.clazz, "sibling", "I");
	XWindowChangesFc.stack_mode = (*env)->GetFieldID(env, XWindowChangesFc.clazz, "stack_mode", "I");
	XWindowChangesFc.cached = 1;
}

XWindowChanges *getXWindowChangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpStruct)
{
	if (!XWindowChangesFc.cached) cacheXWindowChangesFields(env, lpObject);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XWindowChangesFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XWindowChangesFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, XWindowChangesFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, XWindowChangesFc.height);
	lpStruct->border_width = (*env)->GetIntField(env, lpObject, XWindowChangesFc.border_width);
	lpStruct->sibling = (*env)->GetIntField(env, lpObject, XWindowChangesFc.sibling);
	lpStruct->stack_mode = (*env)->GetIntField(env, lpObject, XWindowChangesFc.stack_mode);
	return lpStruct;
}

void setXWindowChangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpStruct)
{
	if (!XWindowChangesFc.cached) cacheXWindowChangesFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XWindowChangesFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XWindowChangesFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XWindowChangesFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, XWindowChangesFc.height, (jint)lpStruct->height);
	(*env)->SetIntField(env, lpObject, XWindowChangesFc.border_width, (jint)lpStruct->border_width);
	(*env)->SetIntField(env, lpObject, XWindowChangesFc.sibling, (jint)lpStruct->sibling);
	(*env)->SetIntField(env, lpObject, XWindowChangesFc.stack_mode, (jint)lpStruct->stack_mode);
}
#endif

#ifndef NO_XineramaScreenInfo
typedef struct XineramaScreenInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID screen_number, x_org, y_org, width, height;
} XineramaScreenInfo_FID_CACHE;

XineramaScreenInfo_FID_CACHE XineramaScreenInfoFc;

void cacheXineramaScreenInfoFields(JNIEnv *env, jobject lpObject)
{
	if (XineramaScreenInfoFc.cached) return;
	XineramaScreenInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XineramaScreenInfoFc.screen_number = (*env)->GetFieldID(env, XineramaScreenInfoFc.clazz, "screen_number", "I");
	XineramaScreenInfoFc.x_org = (*env)->GetFieldID(env, XineramaScreenInfoFc.clazz, "x_org", "S");
	XineramaScreenInfoFc.y_org = (*env)->GetFieldID(env, XineramaScreenInfoFc.clazz, "y_org", "S");
	XineramaScreenInfoFc.width = (*env)->GetFieldID(env, XineramaScreenInfoFc.clazz, "width", "S");
	XineramaScreenInfoFc.height = (*env)->GetFieldID(env, XineramaScreenInfoFc.clazz, "height", "S");
	XineramaScreenInfoFc.cached = 1;
}

XineramaScreenInfo *getXineramaScreenInfoFields(JNIEnv *env, jobject lpObject, XineramaScreenInfo *lpStruct)
{
	if (!XineramaScreenInfoFc.cached) cacheXineramaScreenInfoFields(env, lpObject);
	lpStruct->screen_number = (*env)->GetIntField(env, lpObject, XineramaScreenInfoFc.screen_number);
	lpStruct->x_org = (*env)->GetShortField(env, lpObject, XineramaScreenInfoFc.x_org);
	lpStruct->y_org = (*env)->GetShortField(env, lpObject, XineramaScreenInfoFc.y_org);
	lpStruct->width = (*env)->GetShortField(env, lpObject, XineramaScreenInfoFc.width);
	lpStruct->height = (*env)->GetShortField(env, lpObject, XineramaScreenInfoFc.height);
	return lpStruct;
}

void setXineramaScreenInfoFields(JNIEnv *env, jobject lpObject, XineramaScreenInfo *lpStruct)
{
	if (!XineramaScreenInfoFc.cached) cacheXineramaScreenInfoFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XineramaScreenInfoFc.screen_number, (jint)lpStruct->screen_number);
	(*env)->SetShortField(env, lpObject, XineramaScreenInfoFc.x_org, (jshort)lpStruct->x_org);
	(*env)->SetShortField(env, lpObject, XineramaScreenInfoFc.y_org, (jshort)lpStruct->y_org);
	(*env)->SetShortField(env, lpObject, XineramaScreenInfoFc.width, (jshort)lpStruct->width);
	(*env)->SetShortField(env, lpObject, XineramaScreenInfoFc.height, (jshort)lpStruct->height);
}
#endif

#ifndef NO_XmAnyCallbackStruct
typedef struct XmAnyCallbackStruct_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID reason, event;
} XmAnyCallbackStruct_FID_CACHE;

XmAnyCallbackStruct_FID_CACHE XmAnyCallbackStructFc;

void cacheXmAnyCallbackStructFields(JNIEnv *env, jobject lpObject)
{
	if (XmAnyCallbackStructFc.cached) return;
	XmAnyCallbackStructFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XmAnyCallbackStructFc.reason = (*env)->GetFieldID(env, XmAnyCallbackStructFc.clazz, "reason", "I");
	XmAnyCallbackStructFc.event = (*env)->GetFieldID(env, XmAnyCallbackStructFc.clazz, "event", "I");
	XmAnyCallbackStructFc.cached = 1;
}

XmAnyCallbackStruct *getXmAnyCallbackStructFields(JNIEnv *env, jobject lpObject, XmAnyCallbackStruct *lpStruct)
{
	if (!XmAnyCallbackStructFc.cached) cacheXmAnyCallbackStructFields(env, lpObject);
	lpStruct->reason = (*env)->GetIntField(env, lpObject, XmAnyCallbackStructFc.reason);
	lpStruct->event = (XEvent *)(*env)->GetIntField(env, lpObject, XmAnyCallbackStructFc.event);
	return lpStruct;
}

void setXmAnyCallbackStructFields(JNIEnv *env, jobject lpObject, XmAnyCallbackStruct *lpStruct)
{
	if (!XmAnyCallbackStructFc.cached) cacheXmAnyCallbackStructFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XmAnyCallbackStructFc.reason, (jint)lpStruct->reason);
	(*env)->SetIntField(env, lpObject, XmAnyCallbackStructFc.event, (jint)lpStruct->event);
}
#endif

#ifndef NO_XmDragProcCallbackStruct
typedef struct XmDragProcCallbackStruct_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID timeStamp, dragContext, x, y, dropSiteStatus, operation, operations, animate;
} XmDragProcCallbackStruct_FID_CACHE;

XmDragProcCallbackStruct_FID_CACHE XmDragProcCallbackStructFc;

void cacheXmDragProcCallbackStructFields(JNIEnv *env, jobject lpObject)
{
	if (XmDragProcCallbackStructFc.cached) return;
	cacheXmAnyCallbackStructFields(env, lpObject);
	XmDragProcCallbackStructFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XmDragProcCallbackStructFc.timeStamp = (*env)->GetFieldID(env, XmDragProcCallbackStructFc.clazz, "timeStamp", "I");
	XmDragProcCallbackStructFc.dragContext = (*env)->GetFieldID(env, XmDragProcCallbackStructFc.clazz, "dragContext", "I");
	XmDragProcCallbackStructFc.x = (*env)->GetFieldID(env, XmDragProcCallbackStructFc.clazz, "x", "S");
	XmDragProcCallbackStructFc.y = (*env)->GetFieldID(env, XmDragProcCallbackStructFc.clazz, "y", "S");
	XmDragProcCallbackStructFc.dropSiteStatus = (*env)->GetFieldID(env, XmDragProcCallbackStructFc.clazz, "dropSiteStatus", "B");
	XmDragProcCallbackStructFc.operation = (*env)->GetFieldID(env, XmDragProcCallbackStructFc.clazz, "operation", "B");
	XmDragProcCallbackStructFc.operations = (*env)->GetFieldID(env, XmDragProcCallbackStructFc.clazz, "operations", "B");
	XmDragProcCallbackStructFc.animate = (*env)->GetFieldID(env, XmDragProcCallbackStructFc.clazz, "animate", "B");
	XmDragProcCallbackStructFc.cached = 1;
}

XmDragProcCallbackStruct *getXmDragProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDragProcCallbackStruct *lpStruct)
{
	if (!XmDragProcCallbackStructFc.cached) cacheXmDragProcCallbackStructFields(env, lpObject);
	getXmAnyCallbackStructFields(env, lpObject, (XmAnyCallbackStruct *)lpStruct);
	lpStruct->timeStamp = (*env)->GetIntField(env, lpObject, XmDragProcCallbackStructFc.timeStamp);
	lpStruct->dragContext = (Widget)(*env)->GetIntField(env, lpObject, XmDragProcCallbackStructFc.dragContext);
	lpStruct->x = (*env)->GetShortField(env, lpObject, XmDragProcCallbackStructFc.x);
	lpStruct->y = (*env)->GetShortField(env, lpObject, XmDragProcCallbackStructFc.y);
	lpStruct->dropSiteStatus = (*env)->GetByteField(env, lpObject, XmDragProcCallbackStructFc.dropSiteStatus);
	lpStruct->operation = (*env)->GetByteField(env, lpObject, XmDragProcCallbackStructFc.operation);
	lpStruct->operations = (*env)->GetByteField(env, lpObject, XmDragProcCallbackStructFc.operations);
	lpStruct->animate = (*env)->GetByteField(env, lpObject, XmDragProcCallbackStructFc.animate);
	return lpStruct;
}

void setXmDragProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDragProcCallbackStruct *lpStruct)
{
	if (!XmDragProcCallbackStructFc.cached) cacheXmDragProcCallbackStructFields(env, lpObject);
	setXmAnyCallbackStructFields(env, lpObject, (XmAnyCallbackStruct *)lpStruct);
	(*env)->SetIntField(env, lpObject, XmDragProcCallbackStructFc.timeStamp, (jint)lpStruct->timeStamp);
	(*env)->SetIntField(env, lpObject, XmDragProcCallbackStructFc.dragContext, (jint)lpStruct->dragContext);
	(*env)->SetShortField(env, lpObject, XmDragProcCallbackStructFc.x, (jshort)lpStruct->x);
	(*env)->SetShortField(env, lpObject, XmDragProcCallbackStructFc.y, (jshort)lpStruct->y);
	(*env)->SetByteField(env, lpObject, XmDragProcCallbackStructFc.dropSiteStatus, (jbyte)lpStruct->dropSiteStatus);
	(*env)->SetByteField(env, lpObject, XmDragProcCallbackStructFc.operation, (jbyte)lpStruct->operation);
	(*env)->SetByteField(env, lpObject, XmDragProcCallbackStructFc.operations, (jbyte)lpStruct->operations);
	(*env)->SetByteField(env, lpObject, XmDragProcCallbackStructFc.animate, (jbyte)lpStruct->animate);
}
#endif

#ifndef NO_XmDropFinishCallbackStruct
typedef struct XmDropFinishCallbackStruct_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID timeStamp, operation, operations, dropSiteStatus, dropAction, completionStatus;
} XmDropFinishCallbackStruct_FID_CACHE;

XmDropFinishCallbackStruct_FID_CACHE XmDropFinishCallbackStructFc;

void cacheXmDropFinishCallbackStructFields(JNIEnv *env, jobject lpObject)
{
	if (XmDropFinishCallbackStructFc.cached) return;
	cacheXmAnyCallbackStructFields(env, lpObject);
	XmDropFinishCallbackStructFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XmDropFinishCallbackStructFc.timeStamp = (*env)->GetFieldID(env, XmDropFinishCallbackStructFc.clazz, "timeStamp", "I");
	XmDropFinishCallbackStructFc.operation = (*env)->GetFieldID(env, XmDropFinishCallbackStructFc.clazz, "operation", "B");
	XmDropFinishCallbackStructFc.operations = (*env)->GetFieldID(env, XmDropFinishCallbackStructFc.clazz, "operations", "B");
	XmDropFinishCallbackStructFc.dropSiteStatus = (*env)->GetFieldID(env, XmDropFinishCallbackStructFc.clazz, "dropSiteStatus", "B");
	XmDropFinishCallbackStructFc.dropAction = (*env)->GetFieldID(env, XmDropFinishCallbackStructFc.clazz, "dropAction", "B");
	XmDropFinishCallbackStructFc.completionStatus = (*env)->GetFieldID(env, XmDropFinishCallbackStructFc.clazz, "completionStatus", "B");
	XmDropFinishCallbackStructFc.cached = 1;
}

XmDropFinishCallbackStruct *getXmDropFinishCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropFinishCallbackStruct *lpStruct)
{
	if (!XmDropFinishCallbackStructFc.cached) cacheXmDropFinishCallbackStructFields(env, lpObject);
	getXmAnyCallbackStructFields(env, lpObject, (XmAnyCallbackStruct *)lpStruct);
	lpStruct->timeStamp = (*env)->GetIntField(env, lpObject, XmDropFinishCallbackStructFc.timeStamp);
	lpStruct->operation = (*env)->GetByteField(env, lpObject, XmDropFinishCallbackStructFc.operation);
	lpStruct->operations = (*env)->GetByteField(env, lpObject, XmDropFinishCallbackStructFc.operations);
	lpStruct->dropSiteStatus = (*env)->GetByteField(env, lpObject, XmDropFinishCallbackStructFc.dropSiteStatus);
	lpStruct->dropAction = (*env)->GetByteField(env, lpObject, XmDropFinishCallbackStructFc.dropAction);
	lpStruct->completionStatus = (*env)->GetByteField(env, lpObject, XmDropFinishCallbackStructFc.completionStatus);
	return lpStruct;
}

void setXmDropFinishCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropFinishCallbackStruct *lpStruct)
{
	if (!XmDropFinishCallbackStructFc.cached) cacheXmDropFinishCallbackStructFields(env, lpObject);
	setXmAnyCallbackStructFields(env, lpObject, (XmAnyCallbackStruct *)lpStruct);
	(*env)->SetIntField(env, lpObject, XmDropFinishCallbackStructFc.timeStamp, (jint)lpStruct->timeStamp);
	(*env)->SetByteField(env, lpObject, XmDropFinishCallbackStructFc.operation, (jbyte)lpStruct->operation);
	(*env)->SetByteField(env, lpObject, XmDropFinishCallbackStructFc.operations, (jbyte)lpStruct->operations);
	(*env)->SetByteField(env, lpObject, XmDropFinishCallbackStructFc.dropSiteStatus, (jbyte)lpStruct->dropSiteStatus);
	(*env)->SetByteField(env, lpObject, XmDropFinishCallbackStructFc.dropAction, (jbyte)lpStruct->dropAction);
	(*env)->SetByteField(env, lpObject, XmDropFinishCallbackStructFc.completionStatus, (jbyte)lpStruct->completionStatus);
}
#endif

#ifndef NO_XmDropProcCallbackStruct
typedef struct XmDropProcCallbackStruct_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID timeStamp, dragContext, x, y, dropSiteStatus, operation, operations, dropAction;
} XmDropProcCallbackStruct_FID_CACHE;

XmDropProcCallbackStruct_FID_CACHE XmDropProcCallbackStructFc;

void cacheXmDropProcCallbackStructFields(JNIEnv *env, jobject lpObject)
{
	if (XmDropProcCallbackStructFc.cached) return;
	cacheXmAnyCallbackStructFields(env, lpObject);
	XmDropProcCallbackStructFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XmDropProcCallbackStructFc.timeStamp = (*env)->GetFieldID(env, XmDropProcCallbackStructFc.clazz, "timeStamp", "I");
	XmDropProcCallbackStructFc.dragContext = (*env)->GetFieldID(env, XmDropProcCallbackStructFc.clazz, "dragContext", "I");
	XmDropProcCallbackStructFc.x = (*env)->GetFieldID(env, XmDropProcCallbackStructFc.clazz, "x", "S");
	XmDropProcCallbackStructFc.y = (*env)->GetFieldID(env, XmDropProcCallbackStructFc.clazz, "y", "S");
	XmDropProcCallbackStructFc.dropSiteStatus = (*env)->GetFieldID(env, XmDropProcCallbackStructFc.clazz, "dropSiteStatus", "B");
	XmDropProcCallbackStructFc.operation = (*env)->GetFieldID(env, XmDropProcCallbackStructFc.clazz, "operation", "B");
	XmDropProcCallbackStructFc.operations = (*env)->GetFieldID(env, XmDropProcCallbackStructFc.clazz, "operations", "B");
	XmDropProcCallbackStructFc.dropAction = (*env)->GetFieldID(env, XmDropProcCallbackStructFc.clazz, "dropAction", "B");
	XmDropProcCallbackStructFc.cached = 1;
}

XmDropProcCallbackStruct *getXmDropProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropProcCallbackStruct *lpStruct)
{
	if (!XmDropProcCallbackStructFc.cached) cacheXmDropProcCallbackStructFields(env, lpObject);
	getXmAnyCallbackStructFields(env, lpObject, (XmAnyCallbackStruct *)lpStruct);
	lpStruct->timeStamp = (*env)->GetIntField(env, lpObject, XmDropProcCallbackStructFc.timeStamp);
	lpStruct->dragContext = (Widget)(*env)->GetIntField(env, lpObject, XmDropProcCallbackStructFc.dragContext);
	lpStruct->x = (*env)->GetShortField(env, lpObject, XmDropProcCallbackStructFc.x);
	lpStruct->y = (*env)->GetShortField(env, lpObject, XmDropProcCallbackStructFc.y);
	lpStruct->dropSiteStatus = (*env)->GetByteField(env, lpObject, XmDropProcCallbackStructFc.dropSiteStatus);
	lpStruct->operation = (*env)->GetByteField(env, lpObject, XmDropProcCallbackStructFc.operation);
	lpStruct->operations = (*env)->GetByteField(env, lpObject, XmDropProcCallbackStructFc.operations);
	lpStruct->dropAction = (*env)->GetByteField(env, lpObject, XmDropProcCallbackStructFc.dropAction);
	return lpStruct;
}

void setXmDropProcCallbackStructFields(JNIEnv *env, jobject lpObject, XmDropProcCallbackStruct *lpStruct)
{
	if (!XmDropProcCallbackStructFc.cached) cacheXmDropProcCallbackStructFields(env, lpObject);
	setXmAnyCallbackStructFields(env, lpObject, (XmAnyCallbackStruct *)lpStruct);
	(*env)->SetIntField(env, lpObject, XmDropProcCallbackStructFc.timeStamp, (jint)lpStruct->timeStamp);
	(*env)->SetIntField(env, lpObject, XmDropProcCallbackStructFc.dragContext, (jint)lpStruct->dragContext);
	(*env)->SetShortField(env, lpObject, XmDropProcCallbackStructFc.x, (jshort)lpStruct->x);
	(*env)->SetShortField(env, lpObject, XmDropProcCallbackStructFc.y, (jshort)lpStruct->y);
	(*env)->SetByteField(env, lpObject, XmDropProcCallbackStructFc.dropSiteStatus, (jbyte)lpStruct->dropSiteStatus);
	(*env)->SetByteField(env, lpObject, XmDropProcCallbackStructFc.operation, (jbyte)lpStruct->operation);
	(*env)->SetByteField(env, lpObject, XmDropProcCallbackStructFc.operations, (jbyte)lpStruct->operations);
	(*env)->SetByteField(env, lpObject, XmDropProcCallbackStructFc.dropAction, (jbyte)lpStruct->dropAction);
}
#endif

#ifndef NO_XmSpinBoxCallbackStruct
typedef struct XmSpinBoxCallbackStruct_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID widget, doit, position, value, crossed_boundary;
} XmSpinBoxCallbackStruct_FID_CACHE;

XmSpinBoxCallbackStruct_FID_CACHE XmSpinBoxCallbackStructFc;

void cacheXmSpinBoxCallbackStructFields(JNIEnv *env, jobject lpObject)
{
	if (XmSpinBoxCallbackStructFc.cached) return;
	cacheXmAnyCallbackStructFields(env, lpObject);
	XmSpinBoxCallbackStructFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XmSpinBoxCallbackStructFc.widget = (*env)->GetFieldID(env, XmSpinBoxCallbackStructFc.clazz, "widget", "I");
	XmSpinBoxCallbackStructFc.doit = (*env)->GetFieldID(env, XmSpinBoxCallbackStructFc.clazz, "doit", "B");
	XmSpinBoxCallbackStructFc.position = (*env)->GetFieldID(env, XmSpinBoxCallbackStructFc.clazz, "position", "I");
	XmSpinBoxCallbackStructFc.value = (*env)->GetFieldID(env, XmSpinBoxCallbackStructFc.clazz, "value", "I");
	XmSpinBoxCallbackStructFc.crossed_boundary = (*env)->GetFieldID(env, XmSpinBoxCallbackStructFc.clazz, "crossed_boundary", "B");
	XmSpinBoxCallbackStructFc.cached = 1;
}

XmSpinBoxCallbackStruct *getXmSpinBoxCallbackStructFields(JNIEnv *env, jobject lpObject, XmSpinBoxCallbackStruct *lpStruct)
{
	if (!XmSpinBoxCallbackStructFc.cached) cacheXmSpinBoxCallbackStructFields(env, lpObject);
	getXmAnyCallbackStructFields(env, lpObject, (XmAnyCallbackStruct *)lpStruct);
	lpStruct->widget = (Widget)(*env)->GetIntField(env, lpObject, XmSpinBoxCallbackStructFc.widget);
	lpStruct->doit = (Boolean)(*env)->GetByteField(env, lpObject, XmSpinBoxCallbackStructFc.doit);
	lpStruct->position = (*env)->GetIntField(env, lpObject, XmSpinBoxCallbackStructFc.position);
	lpStruct->value = (XmString)(*env)->GetIntField(env, lpObject, XmSpinBoxCallbackStructFc.value);
	lpStruct->crossed_boundary = (Boolean)(*env)->GetByteField(env, lpObject, XmSpinBoxCallbackStructFc.crossed_boundary);
	return lpStruct;
}

void setXmSpinBoxCallbackStructFields(JNIEnv *env, jobject lpObject, XmSpinBoxCallbackStruct *lpStruct)
{
	if (!XmSpinBoxCallbackStructFc.cached) cacheXmSpinBoxCallbackStructFields(env, lpObject);
	setXmAnyCallbackStructFields(env, lpObject, (XmAnyCallbackStruct *)lpStruct);
	(*env)->SetIntField(env, lpObject, XmSpinBoxCallbackStructFc.widget, (jint)lpStruct->widget);
	(*env)->SetByteField(env, lpObject, XmSpinBoxCallbackStructFc.doit, (jbyte)lpStruct->doit);
	(*env)->SetIntField(env, lpObject, XmSpinBoxCallbackStructFc.position, (jint)lpStruct->position);
	(*env)->SetIntField(env, lpObject, XmSpinBoxCallbackStructFc.value, (jint)lpStruct->value);
	(*env)->SetByteField(env, lpObject, XmSpinBoxCallbackStructFc.crossed_boundary, (jbyte)lpStruct->crossed_boundary);
}
#endif

#ifndef NO_XmTextBlockRec
typedef struct XmTextBlockRec_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID ptr, length, format;
} XmTextBlockRec_FID_CACHE;

XmTextBlockRec_FID_CACHE XmTextBlockRecFc;

void cacheXmTextBlockRecFields(JNIEnv *env, jobject lpObject)
{
	if (XmTextBlockRecFc.cached) return;
	XmTextBlockRecFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XmTextBlockRecFc.ptr = (*env)->GetFieldID(env, XmTextBlockRecFc.clazz, "ptr", "I");
	XmTextBlockRecFc.length = (*env)->GetFieldID(env, XmTextBlockRecFc.clazz, "length", "I");
	XmTextBlockRecFc.format = (*env)->GetFieldID(env, XmTextBlockRecFc.clazz, "format", "I");
	XmTextBlockRecFc.cached = 1;
}

XmTextBlockRec *getXmTextBlockRecFields(JNIEnv *env, jobject lpObject, XmTextBlockRec *lpStruct)
{
	if (!XmTextBlockRecFc.cached) cacheXmTextBlockRecFields(env, lpObject);
	lpStruct->ptr = (char *)(*env)->GetIntField(env, lpObject, XmTextBlockRecFc.ptr);
	lpStruct->length = (*env)->GetIntField(env, lpObject, XmTextBlockRecFc.length);
	lpStruct->format = (XmTextFormat)(*env)->GetIntField(env, lpObject, XmTextBlockRecFc.format);
	return lpStruct;
}

void setXmTextBlockRecFields(JNIEnv *env, jobject lpObject, XmTextBlockRec *lpStruct)
{
	if (!XmTextBlockRecFc.cached) cacheXmTextBlockRecFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XmTextBlockRecFc.ptr, (jint)lpStruct->ptr);
	(*env)->SetIntField(env, lpObject, XmTextBlockRecFc.length, (jint)lpStruct->length);
	(*env)->SetIntField(env, lpObject, XmTextBlockRecFc.format, (jint)lpStruct->format);
}
#endif

#ifndef NO_XmTextVerifyCallbackStruct
typedef struct XmTextVerifyCallbackStruct_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID doit, currInsert, newInsert, startPos, endPos, text;
} XmTextVerifyCallbackStruct_FID_CACHE;

XmTextVerifyCallbackStruct_FID_CACHE XmTextVerifyCallbackStructFc;

void cacheXmTextVerifyCallbackStructFields(JNIEnv *env, jobject lpObject)
{
	if (XmTextVerifyCallbackStructFc.cached) return;
	cacheXmAnyCallbackStructFields(env, lpObject);
	XmTextVerifyCallbackStructFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XmTextVerifyCallbackStructFc.doit = (*env)->GetFieldID(env, XmTextVerifyCallbackStructFc.clazz, "doit", "B");
	XmTextVerifyCallbackStructFc.currInsert = (*env)->GetFieldID(env, XmTextVerifyCallbackStructFc.clazz, "currInsert", "I");
	XmTextVerifyCallbackStructFc.newInsert = (*env)->GetFieldID(env, XmTextVerifyCallbackStructFc.clazz, "newInsert", "I");
	XmTextVerifyCallbackStructFc.startPos = (*env)->GetFieldID(env, XmTextVerifyCallbackStructFc.clazz, "startPos", "I");
	XmTextVerifyCallbackStructFc.endPos = (*env)->GetFieldID(env, XmTextVerifyCallbackStructFc.clazz, "endPos", "I");
	XmTextVerifyCallbackStructFc.text = (*env)->GetFieldID(env, XmTextVerifyCallbackStructFc.clazz, "text", "I");
	XmTextVerifyCallbackStructFc.cached = 1;
}

XmTextVerifyCallbackStruct *getXmTextVerifyCallbackStructFields(JNIEnv *env, jobject lpObject, XmTextVerifyCallbackStruct *lpStruct)
{
	if (!XmTextVerifyCallbackStructFc.cached) cacheXmTextVerifyCallbackStructFields(env, lpObject);
	getXmAnyCallbackStructFields(env, lpObject, (XmAnyCallbackStruct *)lpStruct);
	lpStruct->doit = (*env)->GetByteField(env, lpObject, XmTextVerifyCallbackStructFc.doit);
	lpStruct->currInsert = (*env)->GetIntField(env, lpObject, XmTextVerifyCallbackStructFc.currInsert);
	lpStruct->newInsert = (*env)->GetIntField(env, lpObject, XmTextVerifyCallbackStructFc.newInsert);
	lpStruct->startPos = (*env)->GetIntField(env, lpObject, XmTextVerifyCallbackStructFc.startPos);
	lpStruct->endPos = (*env)->GetIntField(env, lpObject, XmTextVerifyCallbackStructFc.endPos);
	lpStruct->text = (XmTextBlock)(*env)->GetIntField(env, lpObject, XmTextVerifyCallbackStructFc.text);
	return lpStruct;
}

void setXmTextVerifyCallbackStructFields(JNIEnv *env, jobject lpObject, XmTextVerifyCallbackStruct *lpStruct)
{
	if (!XmTextVerifyCallbackStructFc.cached) cacheXmTextVerifyCallbackStructFields(env, lpObject);
	setXmAnyCallbackStructFields(env, lpObject, (XmAnyCallbackStruct *)lpStruct);
	(*env)->SetByteField(env, lpObject, XmTextVerifyCallbackStructFc.doit, (jbyte)lpStruct->doit);
	(*env)->SetIntField(env, lpObject, XmTextVerifyCallbackStructFc.currInsert, (jint)lpStruct->currInsert);
	(*env)->SetIntField(env, lpObject, XmTextVerifyCallbackStructFc.newInsert, (jint)lpStruct->newInsert);
	(*env)->SetIntField(env, lpObject, XmTextVerifyCallbackStructFc.startPos, (jint)lpStruct->startPos);
	(*env)->SetIntField(env, lpObject, XmTextVerifyCallbackStructFc.endPos, (jint)lpStruct->endPos);
	(*env)->SetIntField(env, lpObject, XmTextVerifyCallbackStructFc.text, (jint)lpStruct->text);
}
#endif

#ifndef NO_XtWidgetGeometry
typedef struct XtWidgetGeometry_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID request_mode, x, y, width, height, border_width, sibling, stack_mode;
} XtWidgetGeometry_FID_CACHE;

XtWidgetGeometry_FID_CACHE XtWidgetGeometryFc;

void cacheXtWidgetGeometryFields(JNIEnv *env, jobject lpObject)
{
	if (XtWidgetGeometryFc.cached) return;
	XtWidgetGeometryFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XtWidgetGeometryFc.request_mode = (*env)->GetFieldID(env, XtWidgetGeometryFc.clazz, "request_mode", "I");
	XtWidgetGeometryFc.x = (*env)->GetFieldID(env, XtWidgetGeometryFc.clazz, "x", "I");
	XtWidgetGeometryFc.y = (*env)->GetFieldID(env, XtWidgetGeometryFc.clazz, "y", "I");
	XtWidgetGeometryFc.width = (*env)->GetFieldID(env, XtWidgetGeometryFc.clazz, "width", "I");
	XtWidgetGeometryFc.height = (*env)->GetFieldID(env, XtWidgetGeometryFc.clazz, "height", "I");
	XtWidgetGeometryFc.border_width = (*env)->GetFieldID(env, XtWidgetGeometryFc.clazz, "border_width", "I");
	XtWidgetGeometryFc.sibling = (*env)->GetFieldID(env, XtWidgetGeometryFc.clazz, "sibling", "I");
	XtWidgetGeometryFc.stack_mode = (*env)->GetFieldID(env, XtWidgetGeometryFc.clazz, "stack_mode", "I");
	XtWidgetGeometryFc.cached = 1;
}

XtWidgetGeometry *getXtWidgetGeometryFields(JNIEnv *env, jobject lpObject, XtWidgetGeometry *lpStruct)
{
	if (!XtWidgetGeometryFc.cached) cacheXtWidgetGeometryFields(env, lpObject);
	lpStruct->request_mode = (*env)->GetIntField(env, lpObject, XtWidgetGeometryFc.request_mode);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XtWidgetGeometryFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XtWidgetGeometryFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, XtWidgetGeometryFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, XtWidgetGeometryFc.height);
	lpStruct->border_width = (*env)->GetIntField(env, lpObject, XtWidgetGeometryFc.border_width);
	lpStruct->sibling = (Widget)(*env)->GetIntField(env, lpObject, XtWidgetGeometryFc.sibling);
	lpStruct->stack_mode = (*env)->GetIntField(env, lpObject, XtWidgetGeometryFc.stack_mode);
	return lpStruct;
}

void setXtWidgetGeometryFields(JNIEnv *env, jobject lpObject, XtWidgetGeometry *lpStruct)
{
	if (!XtWidgetGeometryFc.cached) cacheXtWidgetGeometryFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XtWidgetGeometryFc.request_mode, (jint)lpStruct->request_mode);
	(*env)->SetIntField(env, lpObject, XtWidgetGeometryFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XtWidgetGeometryFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XtWidgetGeometryFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, XtWidgetGeometryFc.height, (jint)lpStruct->height);
	(*env)->SetIntField(env, lpObject, XtWidgetGeometryFc.border_width, (jint)lpStruct->border_width);
	(*env)->SetIntField(env, lpObject, XtWidgetGeometryFc.sibling, (jint)lpStruct->sibling);
	(*env)->SetIntField(env, lpObject, XtWidgetGeometryFc.stack_mode, (jint)lpStruct->stack_mode);
}
#endif

