/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 */

#include "swt.h"
#include "structs.h"

typedef struct GdkColor_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pixel, red, green, blue;
} GdkColor_FID_CACHE;
typedef GdkColor_FID_CACHE *PGdkColor_FID_CACHE;

GdkColor_FID_CACHE GdkColorFc;

void cacheGdkColorFids(JNIEnv *env, jobject lpObject, PGdkColor_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->pixel = (*env)->GetFieldID(env, lpCache->clazz, "pixel", "I");
	lpCache->red = (*env)->GetFieldID(env, lpCache->clazz, "red", "S");
	lpCache->green = (*env)->GetFieldID(env, lpCache->clazz, "green", "S");
	lpCache->blue = (*env)->GetFieldID(env, lpCache->clazz, "blue", "S");
	lpCache->cached = 1;
}

GdkColor *getGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct)
{
	PGdkColor_FID_CACHE lpCache = &GdkColorFc;
	if (!lpCache->cached) cacheGdkColorFids(env, lpObject, lpCache);
	lpStruct->pixel = (guint32)(*env)->GetIntField(env, lpObject, lpCache->pixel);
	lpStruct->red = (guint16)(*env)->GetShortField(env, lpObject, lpCache->red);
	lpStruct->green = (guint16)(*env)->GetShortField(env, lpObject, lpCache->green);
	lpStruct->blue = (guint16)(*env)->GetShortField(env, lpObject, lpCache->blue);
	return lpStruct;
}

void setGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct)
{
	PGdkColor_FID_CACHE lpCache = &GdkColorFc;
	if (!lpCache->cached) cacheGdkColorFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->pixel, (jint)lpStruct->pixel);
	(*env)->SetShortField(env, lpObject, lpCache->red, (jshort)lpStruct->red);
	(*env)->SetShortField(env, lpObject, lpCache->green, (jshort)lpStruct->green);
	(*env)->SetShortField(env, lpObject, lpCache->blue, (jshort)lpStruct->blue);
}

typedef struct GtkColorSelectionDialog_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID colorsel, ok_button, cancel_button, help_button;
} GtkColorSelectionDialog_FID_CACHE;
typedef GtkColorSelectionDialog_FID_CACHE *PGtkColorSelectionDialog_FID_CACHE;

GtkColorSelectionDialog_FID_CACHE GtkColorSelectionDialogFc;

void cacheGtkColorSelectionDialogFids(JNIEnv *env, jobject lpObject, PGtkColorSelectionDialog_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->colorsel = (*env)->GetFieldID(env, lpCache->clazz, "colorsel", "I");
	lpCache->ok_button = (*env)->GetFieldID(env, lpCache->clazz, "ok_button", "I");
	lpCache->cancel_button = (*env)->GetFieldID(env, lpCache->clazz, "cancel_button", "I");
	lpCache->help_button = (*env)->GetFieldID(env, lpCache->clazz, "help_button", "I");
	lpCache->cached = 1;
}

GtkColorSelectionDialog *getGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct)
{
	PGtkColorSelectionDialog_FID_CACHE lpCache = &GtkColorSelectionDialogFc;
	if (!lpCache->cached) cacheGtkColorSelectionDialogFids(env, lpObject, lpCache);
	lpStruct->colorsel = (GtkWidget *)(*env)->GetIntField(env, lpObject, lpCache->colorsel);
	lpStruct->ok_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, lpCache->ok_button);
	lpStruct->cancel_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, lpCache->cancel_button);
	lpStruct->help_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, lpCache->help_button);
	return lpStruct;
}

void setGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct)
{
	PGtkColorSelectionDialog_FID_CACHE lpCache = &GtkColorSelectionDialogFc;
	if (!lpCache->cached) cacheGtkColorSelectionDialogFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->colorsel, (jint)lpStruct->colorsel);
	(*env)->SetIntField(env, lpObject, lpCache->ok_button, (jint)lpStruct->ok_button);
	(*env)->SetIntField(env, lpObject, lpCache->cancel_button, (jint)lpStruct->cancel_button);
	(*env)->SetIntField(env, lpObject, lpCache->help_button, (jint)lpStruct->help_button);
}

typedef struct GdkDragContext_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID start_time, action, suggested_action, actions, targets, dest_window, source_window, is_source, protocol;
} GdkDragContext_FID_CACHE;
typedef GdkDragContext_FID_CACHE *PGdkDragContext_FID_CACHE;

GdkDragContext_FID_CACHE GdkDragContextFc;

void cacheGdkDragContextFids(JNIEnv *env, jobject lpObject, PGdkDragContext_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->start_time = (*env)->GetFieldID(env, lpCache->clazz, "start_time", "I");
	lpCache->action = (*env)->GetFieldID(env, lpCache->clazz, "action", "I");
	lpCache->suggested_action = (*env)->GetFieldID(env, lpCache->clazz, "suggested_action", "I");
	lpCache->actions = (*env)->GetFieldID(env, lpCache->clazz, "actions", "I");
	lpCache->targets = (*env)->GetFieldID(env, lpCache->clazz, "targets", "I");
	lpCache->dest_window = (*env)->GetFieldID(env, lpCache->clazz, "dest_window", "I");
	lpCache->source_window = (*env)->GetFieldID(env, lpCache->clazz, "source_window", "I");
	lpCache->is_source = (*env)->GetFieldID(env, lpCache->clazz, "is_source", "Z");
	lpCache->protocol = (*env)->GetFieldID(env, lpCache->clazz, "protocol", "I");
	lpCache->cached = 1;
}

GdkDragContext *getGdkDragContextFields(JNIEnv *env, jobject lpObject, GdkDragContext *lpStruct)
{
	PGdkDragContext_FID_CACHE lpCache = &GdkDragContextFc;
	if (!lpCache->cached) cacheGdkDragContextFids(env, lpObject, lpCache);
	lpStruct->start_time = (*env)->GetIntField(env, lpObject, lpCache->start_time);
	lpStruct->action = (GdkDragAction)(*env)->GetIntField(env, lpObject, lpCache->action);
	lpStruct->suggested_action = (GdkDragAction)(*env)->GetIntField(env, lpObject, lpCache->suggested_action);
	lpStruct->actions = (GdkDragAction)(*env)->GetIntField(env, lpObject, lpCache->actions);
	lpStruct->targets = (GList *)(*env)->GetIntField(env, lpObject, lpCache->targets);
	lpStruct->dest_window = (GdkWindow *)(*env)->GetIntField(env, lpObject, lpCache->dest_window);
	lpStruct->source_window = (GdkWindow *)(*env)->GetIntField(env, lpObject, lpCache->source_window);
	lpStruct->is_source = (*env)->GetBooleanField(env, lpObject, lpCache->is_source);
	lpStruct->protocol = (GdkDragProtocol)(*env)->GetIntField(env, lpObject, lpCache->protocol);
	return lpStruct;
}

void setGdkDragContextFields(JNIEnv *env, jobject lpObject, GdkDragContext *lpStruct)
{
	PGdkDragContext_FID_CACHE lpCache = &GdkDragContextFc;
	if (!lpCache->cached) cacheGdkDragContextFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->start_time, (jint)lpStruct->start_time);
	(*env)->SetIntField(env, lpObject, lpCache->action, (jint)lpStruct->action);
	(*env)->SetIntField(env, lpObject, lpCache->suggested_action, (jint)lpStruct->suggested_action);
	(*env)->SetIntField(env, lpObject, lpCache->actions, (jint)lpStruct->actions);
	(*env)->SetIntField(env, lpObject, lpCache->targets, (jint)lpStruct->targets);
	(*env)->SetIntField(env, lpObject, lpCache->dest_window, (jint)lpStruct->dest_window);
	(*env)->SetIntField(env, lpObject, lpCache->source_window, (jint)lpStruct->source_window);
	(*env)->SetBooleanField(env, lpObject, lpCache->is_source, (jboolean)lpStruct->is_source);
	(*env)->SetIntField(env, lpObject, lpCache->protocol, (jint)lpStruct->protocol);
}

typedef struct GdkEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type;
} GdkEvent_FID_CACHE;
typedef GdkEvent_FID_CACHE *PGdkEvent_FID_CACHE;

GdkEvent_FID_CACHE GdkEventFc;

void cacheGdkEventFids(JNIEnv *env, jobject lpObject, PGdkEvent_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->type = (*env)->GetFieldID(env, lpCache->clazz, "type", "I");
	lpCache->cached = 1;
}

GdkEvent *getGdkEventFields(JNIEnv *env, jobject lpObject, GdkEvent *lpStruct)
{
	PGdkEvent_FID_CACHE lpCache = &GdkEventFc;
	if (!lpCache->cached) cacheGdkEventFids(env, lpObject, lpCache);
	lpStruct->type = (GdkEventType)(*env)->GetIntField(env, lpObject, lpCache->type);
	return lpStruct;
}

void setGdkEventFields(JNIEnv *env, jobject lpObject, GdkEvent *lpStruct)
{
	PGdkEvent_FID_CACHE lpCache = &GdkEventFc;
	if (!lpCache->cached) cacheGdkEventFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->type, (jint)lpStruct->type);
}

typedef struct GdkEventButton_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, time, x, y, axes, state, button, device, x_root, y_root;
} GdkEventButton_FID_CACHE;
typedef GdkEventButton_FID_CACHE *PGdkEventButton_FID_CACHE;

GdkEventButton_FID_CACHE GdkEventButtonFc;

void cacheGdkEventButtonFids(JNIEnv *env, jobject lpObject, PGdkEventButton_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	cacheGdkEventFids(env, lpObject, &GdkEventFc);
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->window = (*env)->GetFieldID(env, lpCache->clazz, "window", "I");
	lpCache->send_event = (*env)->GetFieldID(env, lpCache->clazz, "send_event", "B");
	lpCache->time = (*env)->GetFieldID(env, lpCache->clazz, "time", "I");
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "D");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "D");
	lpCache->axes = (*env)->GetFieldID(env, lpCache->clazz, "axes", "I");
	lpCache->state = (*env)->GetFieldID(env, lpCache->clazz, "state", "I");
	lpCache->button = (*env)->GetFieldID(env, lpCache->clazz, "button", "I");
	lpCache->device = (*env)->GetFieldID(env, lpCache->clazz, "device", "I");
	lpCache->x_root = (*env)->GetFieldID(env, lpCache->clazz, "x_root", "D");
	lpCache->y_root = (*env)->GetFieldID(env, lpCache->clazz, "y_root", "D");
	lpCache->cached = 1;
}

GdkEventButton *getGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct)
{
	PGdkEventButton_FID_CACHE lpCache = &GdkEventButtonFc;
	if (!lpCache->cached) cacheGdkEventButtonFids(env, lpObject, lpCache);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, lpCache->window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, lpCache->send_event);
	lpStruct->time = (guint32)(*env)->GetIntField(env, lpObject, lpCache->time);
	lpStruct->x = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->x);
	lpStruct->y = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->y);
	lpStruct->axes = (gdouble *)(*env)->GetIntField(env, lpObject, lpCache->axes);
	lpStruct->state = (guint)(*env)->GetIntField(env, lpObject, lpCache->state);
	lpStruct->button = (guint)(*env)->GetIntField(env, lpObject, lpCache->button);
	lpStruct->device = (GdkDevice *)(*env)->GetIntField(env, lpObject, lpCache->device);
	lpStruct->x_root = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->x_root);
	lpStruct->y_root = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->y_root);
	return lpStruct;
}

void setGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct)
{
	PGdkEventButton_FID_CACHE lpCache = &GdkEventButtonFc;
	if (!lpCache->cached) cacheGdkEventButtonFids(env, lpObject, lpCache);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, lpCache->window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, lpCache->send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, lpCache->time, (jint)lpStruct->time);
	(*env)->SetDoubleField(env, lpObject, lpCache->x, (jdouble)lpStruct->x);
	(*env)->SetDoubleField(env, lpObject, lpCache->y, (jdouble)lpStruct->y);
	(*env)->SetIntField(env, lpObject, lpCache->axes, (jint)lpStruct->axes);
	(*env)->SetIntField(env, lpObject, lpCache->state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, lpCache->button, (jint)lpStruct->button);
	(*env)->SetIntField(env, lpObject, lpCache->device, (jint)lpStruct->device);
	(*env)->SetDoubleField(env, lpObject, lpCache->x_root, (jdouble)lpStruct->x_root);
	(*env)->SetDoubleField(env, lpObject, lpCache->y_root, (jdouble)lpStruct->y_root);
}

typedef struct GdkEventExpose_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, area_x, area_y, area_width, area_height, region, count;
} GdkEventExpose_FID_CACHE;
typedef GdkEventExpose_FID_CACHE *PGdkEventExpose_FID_CACHE;

GdkEventExpose_FID_CACHE GdkEventExposeFc;

void cacheGdkEventExposeFids(JNIEnv *env, jobject lpObject, PGdkEventExpose_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	cacheGdkEventFids(env, lpObject, &GdkEventFc);
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->window = (*env)->GetFieldID(env, lpCache->clazz, "window", "I");
	lpCache->send_event = (*env)->GetFieldID(env, lpCache->clazz, "send_event", "B");
	lpCache->area_x = (*env)->GetFieldID(env, lpCache->clazz, "area_x", "I");
	lpCache->area_y = (*env)->GetFieldID(env, lpCache->clazz, "area_y", "I");
	lpCache->area_width = (*env)->GetFieldID(env, lpCache->clazz, "area_width", "I");
	lpCache->area_height = (*env)->GetFieldID(env, lpCache->clazz, "area_height", "I");
	lpCache->region = (*env)->GetFieldID(env, lpCache->clazz, "region", "I");
	lpCache->count = (*env)->GetFieldID(env, lpCache->clazz, "count", "I");
	lpCache->cached = 1;
}

GdkEventExpose *getGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct)
{
	PGdkEventExpose_FID_CACHE lpCache = &GdkEventExposeFc;
	if (!lpCache->cached) cacheGdkEventExposeFids(env, lpObject, lpCache);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, lpCache->window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, lpCache->send_event);
	lpStruct->area.x = (*env)->GetIntField(env, lpObject, lpCache->area_x);
	lpStruct->area.y = (*env)->GetIntField(env, lpObject, lpCache->area_y);
	lpStruct->area.width = (*env)->GetIntField(env, lpObject, lpCache->area_width);
	lpStruct->area.height = (*env)->GetIntField(env, lpObject, lpCache->area_height);
	lpStruct->region = (GdkRegion *)(*env)->GetIntField(env, lpObject, lpCache->region);
	lpStruct->count = (gint)(*env)->GetIntField(env, lpObject, lpCache->count);
	return lpStruct;
}

void setGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct)
{
	PGdkEventExpose_FID_CACHE lpCache = &GdkEventExposeFc;
	if (!lpCache->cached) cacheGdkEventExposeFids(env, lpObject, lpCache);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, lpCache->window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, lpCache->send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, lpCache->area_x, (jint)lpStruct->area.x);
	(*env)->SetIntField(env, lpObject, lpCache->area_y, (jint)lpStruct->area.y);
	(*env)->SetIntField(env, lpObject, lpCache->area_width, (jint)lpStruct->area.width);
	(*env)->SetIntField(env, lpObject, lpCache->area_height, (jint)lpStruct->area.height);
	(*env)->SetIntField(env, lpObject, lpCache->region, (jint)lpStruct->region);
	(*env)->SetIntField(env, lpObject, lpCache->count, (jint)lpStruct->count);
}

typedef struct GdkEventFocus_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, in;
} GdkEventFocus_FID_CACHE;
typedef GdkEventFocus_FID_CACHE *PGdkEventFocus_FID_CACHE;

GdkEventFocus_FID_CACHE GdkEventFocusFc;

void cacheGdkEventFocusFids(JNIEnv *env, jobject lpObject, PGdkEventFocus_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	cacheGdkEventFids(env, lpObject, &GdkEventFc);
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->window = (*env)->GetFieldID(env, lpCache->clazz, "window", "I");
	lpCache->send_event = (*env)->GetFieldID(env, lpCache->clazz, "send_event", "B");
	lpCache->in = (*env)->GetFieldID(env, lpCache->clazz, "in", "S");
	lpCache->cached = 1;
}

GdkEventFocus *getGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct)
{
	PGdkEventFocus_FID_CACHE lpCache = &GdkEventFocusFc;
	if (!lpCache->cached) cacheGdkEventFocusFids(env, lpObject, lpCache);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, lpCache->window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, lpCache->send_event);
	lpStruct->in = (gint16)(*env)->GetShortField(env, lpObject, lpCache->in);
	return lpStruct;
}

void setGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct)
{
	PGdkEventFocus_FID_CACHE lpCache = &GdkEventFocusFc;
	if (!lpCache->cached) cacheGdkEventFocusFids(env, lpObject, lpCache);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, lpCache->window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, lpCache->send_event, (jbyte)lpStruct->send_event);
	(*env)->SetShortField(env, lpObject, lpCache->in, (jshort)lpStruct->in);
}

typedef struct GdkEventKey_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, time, state, keyval, length, string, hardware_keycode, group;
} GdkEventKey_FID_CACHE;
typedef GdkEventKey_FID_CACHE *PGdkEventKey_FID_CACHE;

GdkEventKey_FID_CACHE GdkEventKeyFc;

void cacheGdkEventKeyFids(JNIEnv *env, jobject lpObject, PGdkEventKey_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	cacheGdkEventFids(env, lpObject, &GdkEventFc);
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->window = (*env)->GetFieldID(env, lpCache->clazz, "window", "I");
	lpCache->send_event = (*env)->GetFieldID(env, lpCache->clazz, "send_event", "B");
	lpCache->time = (*env)->GetFieldID(env, lpCache->clazz, "time", "I");
	lpCache->state = (*env)->GetFieldID(env, lpCache->clazz, "state", "I");
	lpCache->keyval = (*env)->GetFieldID(env, lpCache->clazz, "keyval", "I");
	lpCache->length = (*env)->GetFieldID(env, lpCache->clazz, "length", "I");
	lpCache->string = (*env)->GetFieldID(env, lpCache->clazz, "string", "I");
	lpCache->hardware_keycode = (*env)->GetFieldID(env, lpCache->clazz, "hardware_keycode", "S");
	lpCache->group = (*env)->GetFieldID(env, lpCache->clazz, "group", "B");
	lpCache->cached = 1;
}

GdkEventKey *getGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct)
{
	PGdkEventKey_FID_CACHE lpCache = &GdkEventKeyFc;
	if (!lpCache->cached) cacheGdkEventKeyFids(env, lpObject, lpCache);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, lpCache->window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, lpCache->send_event);
	lpStruct->time = (guint32)(*env)->GetIntField(env, lpObject, lpCache->time);
	lpStruct->state = (guint)(*env)->GetIntField(env, lpObject, lpCache->state);
	lpStruct->keyval = (guint)(*env)->GetIntField(env, lpObject, lpCache->keyval);
	lpStruct->length = (gint)(*env)->GetIntField(env, lpObject, lpCache->length);
	lpStruct->string = (gchar *)(*env)->GetIntField(env, lpObject, lpCache->string);
	lpStruct->hardware_keycode = (guint16)(*env)->GetShortField(env, lpObject, lpCache->hardware_keycode);
	lpStruct->group = (guint8)(*env)->GetByteField(env, lpObject, lpCache->group);
	return lpStruct;
}

void setGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct)
{
	PGdkEventKey_FID_CACHE lpCache = &GdkEventKeyFc;
	if (!lpCache->cached) cacheGdkEventKeyFids(env, lpObject, lpCache);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, lpCache->window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, lpCache->send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, lpCache->time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, lpCache->state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, lpCache->keyval, (jint)lpStruct->keyval);
	(*env)->SetIntField(env, lpObject, lpCache->length, (jint)lpStruct->length);
	(*env)->SetIntField(env, lpObject, lpCache->string, (jint)lpStruct->string);
	(*env)->SetShortField(env, lpObject, lpCache->hardware_keycode, (jshort)lpStruct->hardware_keycode);
	(*env)->SetByteField(env, lpObject, lpCache->group, (jbyte)lpStruct->group);
}

typedef struct GdkGCValues_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID foreground_pixel, foreground_red, foreground_green, foreground_blue, background_pixel, background_red, background_green, background_blue, font, function, fill, tile, stipple, clip_mask, subwindow_mode, ts_x_origin, ts_y_origin, clip_x_origin, clip_y_origin, graphics_exposures, line_width, line_style, cap_style, join_style;
} GdkGCValues_FID_CACHE;
typedef GdkGCValues_FID_CACHE *PGdkGCValues_FID_CACHE;

GdkGCValues_FID_CACHE GdkGCValuesFc;

void cacheGdkGCValuesFids(JNIEnv *env, jobject lpObject, PGdkGCValues_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->foreground_pixel = (*env)->GetFieldID(env, lpCache->clazz, "foreground_pixel", "I");
	lpCache->foreground_red = (*env)->GetFieldID(env, lpCache->clazz, "foreground_red", "S");
	lpCache->foreground_green = (*env)->GetFieldID(env, lpCache->clazz, "foreground_green", "S");
	lpCache->foreground_blue = (*env)->GetFieldID(env, lpCache->clazz, "foreground_blue", "S");
	lpCache->background_pixel = (*env)->GetFieldID(env, lpCache->clazz, "background_pixel", "I");
	lpCache->background_red = (*env)->GetFieldID(env, lpCache->clazz, "background_red", "S");
	lpCache->background_green = (*env)->GetFieldID(env, lpCache->clazz, "background_green", "S");
	lpCache->background_blue = (*env)->GetFieldID(env, lpCache->clazz, "background_blue", "S");
	lpCache->font = (*env)->GetFieldID(env, lpCache->clazz, "font", "I");
	lpCache->function = (*env)->GetFieldID(env, lpCache->clazz, "function", "I");
	lpCache->fill = (*env)->GetFieldID(env, lpCache->clazz, "fill", "I");
	lpCache->tile = (*env)->GetFieldID(env, lpCache->clazz, "tile", "I");
	lpCache->stipple = (*env)->GetFieldID(env, lpCache->clazz, "stipple", "I");
	lpCache->clip_mask = (*env)->GetFieldID(env, lpCache->clazz, "clip_mask", "I");
	lpCache->subwindow_mode = (*env)->GetFieldID(env, lpCache->clazz, "subwindow_mode", "I");
	lpCache->ts_x_origin = (*env)->GetFieldID(env, lpCache->clazz, "ts_x_origin", "I");
	lpCache->ts_y_origin = (*env)->GetFieldID(env, lpCache->clazz, "ts_y_origin", "I");
	lpCache->clip_x_origin = (*env)->GetFieldID(env, lpCache->clazz, "clip_x_origin", "I");
	lpCache->clip_y_origin = (*env)->GetFieldID(env, lpCache->clazz, "clip_y_origin", "I");
	lpCache->graphics_exposures = (*env)->GetFieldID(env, lpCache->clazz, "graphics_exposures", "I");
	lpCache->line_width = (*env)->GetFieldID(env, lpCache->clazz, "line_width", "I");
	lpCache->line_style = (*env)->GetFieldID(env, lpCache->clazz, "line_style", "I");
	lpCache->cap_style = (*env)->GetFieldID(env, lpCache->clazz, "cap_style", "I");
	lpCache->join_style = (*env)->GetFieldID(env, lpCache->clazz, "join_style", "I");
	lpCache->cached = 1;
}

GdkGCValues *getGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpStruct)
{
	PGdkGCValues_FID_CACHE lpCache = &GdkGCValuesFc;
	if (!lpCache->cached) cacheGdkGCValuesFids(env, lpObject, lpCache);
	lpStruct->foreground.pixel = (guint32)(*env)->GetIntField(env, lpObject, lpCache->foreground_pixel);
	lpStruct->foreground.red = (guint16)(*env)->GetShortField(env, lpObject, lpCache->foreground_red);
	lpStruct->foreground.green = (guint16)(*env)->GetShortField(env, lpObject, lpCache->foreground_green);
	lpStruct->foreground.blue = (guint16)(*env)->GetShortField(env, lpObject, lpCache->foreground_blue);
	lpStruct->background.pixel = (guint32)(*env)->GetIntField(env, lpObject, lpCache->background_pixel);
	lpStruct->background.red = (guint16)(*env)->GetShortField(env, lpObject, lpCache->background_red);
	lpStruct->background.green = (guint16)(*env)->GetShortField(env, lpObject, lpCache->background_green);
	lpStruct->background.blue = (guint16)(*env)->GetShortField(env, lpObject, lpCache->background_blue);
	lpStruct->font = (GdkFont *)(*env)->GetIntField(env, lpObject, lpCache->font);
	lpStruct->function = (GdkFunction)(*env)->GetIntField(env, lpObject, lpCache->function);
	lpStruct->fill = (GdkFill)(*env)->GetIntField(env, lpObject, lpCache->fill);
	lpStruct->tile = (GdkPixmap *)(*env)->GetIntField(env, lpObject, lpCache->tile);
	lpStruct->stipple = (GdkPixmap *)(*env)->GetIntField(env, lpObject, lpCache->stipple);
	lpStruct->clip_mask = (GdkPixmap *)(*env)->GetIntField(env, lpObject, lpCache->clip_mask);
	lpStruct->subwindow_mode = (GdkSubwindowMode)(*env)->GetIntField(env, lpObject, lpCache->subwindow_mode);
	lpStruct->ts_x_origin = (gint)(*env)->GetIntField(env, lpObject, lpCache->ts_x_origin);
	lpStruct->ts_y_origin = (gint)(*env)->GetIntField(env, lpObject, lpCache->ts_y_origin);
	lpStruct->clip_x_origin = (gint)(*env)->GetIntField(env, lpObject, lpCache->clip_x_origin);
	lpStruct->clip_y_origin = (gint)(*env)->GetIntField(env, lpObject, lpCache->clip_y_origin);
	lpStruct->graphics_exposures = (gint)(*env)->GetIntField(env, lpObject, lpCache->graphics_exposures);
	lpStruct->line_width = (gint)(*env)->GetIntField(env, lpObject, lpCache->line_width);
	lpStruct->line_style = (GdkLineStyle)(*env)->GetIntField(env, lpObject, lpCache->line_style);
	lpStruct->cap_style = (GdkCapStyle)(*env)->GetIntField(env, lpObject, lpCache->cap_style);
	lpStruct->join_style = (GdkJoinStyle)(*env)->GetIntField(env, lpObject, lpCache->join_style);
	return lpStruct;
}

void setGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpStruct)
{
	PGdkGCValues_FID_CACHE lpCache = &GdkGCValuesFc;
	if (!lpCache->cached) cacheGdkGCValuesFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->foreground_pixel, (jint)lpStruct->foreground.pixel);
	(*env)->SetShortField(env, lpObject, lpCache->foreground_red, (jshort)lpStruct->foreground.red);
	(*env)->SetShortField(env, lpObject, lpCache->foreground_green, (jshort)lpStruct->foreground.green);
	(*env)->SetShortField(env, lpObject, lpCache->foreground_blue, (jshort)lpStruct->foreground.blue);
	(*env)->SetIntField(env, lpObject, lpCache->background_pixel, (jint)lpStruct->background.pixel);
	(*env)->SetShortField(env, lpObject, lpCache->background_red, (jshort)lpStruct->background.red);
	(*env)->SetShortField(env, lpObject, lpCache->background_green, (jshort)lpStruct->background.green);
	(*env)->SetShortField(env, lpObject, lpCache->background_blue, (jshort)lpStruct->background.blue);
	(*env)->SetIntField(env, lpObject, lpCache->font, (jint)lpStruct->font);
	(*env)->SetIntField(env, lpObject, lpCache->function, (jint)lpStruct->function);
	(*env)->SetIntField(env, lpObject, lpCache->fill, (jint)lpStruct->fill);
	(*env)->SetIntField(env, lpObject, lpCache->tile, (jint)lpStruct->tile);
	(*env)->SetIntField(env, lpObject, lpCache->stipple, (jint)lpStruct->stipple);
	(*env)->SetIntField(env, lpObject, lpCache->clip_mask, (jint)lpStruct->clip_mask);
	(*env)->SetIntField(env, lpObject, lpCache->subwindow_mode, (jint)lpStruct->subwindow_mode);
	(*env)->SetIntField(env, lpObject, lpCache->ts_x_origin, (jint)lpStruct->ts_x_origin);
	(*env)->SetIntField(env, lpObject, lpCache->ts_y_origin, (jint)lpStruct->ts_y_origin);
	(*env)->SetIntField(env, lpObject, lpCache->clip_x_origin, (jint)lpStruct->clip_x_origin);
	(*env)->SetIntField(env, lpObject, lpCache->clip_y_origin, (jint)lpStruct->clip_y_origin);
	(*env)->SetIntField(env, lpObject, lpCache->graphics_exposures, (jint)lpStruct->graphics_exposures);
	(*env)->SetIntField(env, lpObject, lpCache->line_width, (jint)lpStruct->line_width);
	(*env)->SetIntField(env, lpObject, lpCache->line_style, (jint)lpStruct->line_style);
	(*env)->SetIntField(env, lpObject, lpCache->cap_style, (jint)lpStruct->cap_style);
	(*env)->SetIntField(env, lpObject, lpCache->join_style, (jint)lpStruct->join_style);
}

typedef struct GdkImage_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, visual, byte_order, width, height, depth, bpp, bpl, bits_per_pixel, mem, colormap, windowing_data;
} GdkImage_FID_CACHE;
typedef GdkImage_FID_CACHE *PGdkImage_FID_CACHE;

GdkImage_FID_CACHE GdkImageFc;

void cacheGdkImageFids(JNIEnv *env, jobject lpObject, PGdkImage_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->type = (*env)->GetFieldID(env, lpCache->clazz, "type", "I");
	lpCache->visual = (*env)->GetFieldID(env, lpCache->clazz, "visual", "I");
	lpCache->byte_order = (*env)->GetFieldID(env, lpCache->clazz, "byte_order", "I");
	lpCache->width = (*env)->GetFieldID(env, lpCache->clazz, "width", "I");
	lpCache->height = (*env)->GetFieldID(env, lpCache->clazz, "height", "I");
	lpCache->depth = (*env)->GetFieldID(env, lpCache->clazz, "depth", "S");
	lpCache->bpp = (*env)->GetFieldID(env, lpCache->clazz, "bpp", "S");
	lpCache->bpl = (*env)->GetFieldID(env, lpCache->clazz, "bpl", "S");
	lpCache->bits_per_pixel = (*env)->GetFieldID(env, lpCache->clazz, "bits_per_pixel", "S");
	lpCache->mem = (*env)->GetFieldID(env, lpCache->clazz, "mem", "I");
	lpCache->colormap = (*env)->GetFieldID(env, lpCache->clazz, "colormap", "I");
	lpCache->windowing_data = (*env)->GetFieldID(env, lpCache->clazz, "windowing_data", "I");
	lpCache->cached = 1;
}

GdkImage *getGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct)
{
	PGdkImage_FID_CACHE lpCache = &GdkImageFc;
	if (!lpCache->cached) cacheGdkImageFids(env, lpObject, lpCache);
	lpStruct->type = (GdkImageType)(*env)->GetIntField(env, lpObject, lpCache->type);
	lpStruct->visual = (GdkVisual *)(*env)->GetIntField(env, lpObject, lpCache->visual);
	lpStruct->byte_order = (GdkByteOrder)(*env)->GetIntField(env, lpObject, lpCache->byte_order);
	lpStruct->width = (gint)(*env)->GetIntField(env, lpObject, lpCache->width);
	lpStruct->height = (gint)(*env)->GetIntField(env, lpObject, lpCache->height);
	lpStruct->depth = (guint16)(*env)->GetShortField(env, lpObject, lpCache->depth);
	lpStruct->bpp = (guint16)(*env)->GetShortField(env, lpObject, lpCache->bpp);
	lpStruct->bpl = (guint16)(*env)->GetShortField(env, lpObject, lpCache->bpl);
	lpStruct->bits_per_pixel = (guint16)(*env)->GetShortField(env, lpObject, lpCache->bits_per_pixel);
	lpStruct->mem = (gpointer)(*env)->GetIntField(env, lpObject, lpCache->mem);
	lpStruct->colormap = (GdkColormap *)(*env)->GetIntField(env, lpObject, lpCache->colormap);
	lpStruct->windowing_data = (gpointer)(*env)->GetIntField(env, lpObject, lpCache->windowing_data);
	return lpStruct;
}

void setGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct)
{
	PGdkImage_FID_CACHE lpCache = &GdkImageFc;
	if (!lpCache->cached) cacheGdkImageFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, lpCache->visual, (jint)lpStruct->visual);
	(*env)->SetIntField(env, lpObject, lpCache->byte_order, (jint)lpStruct->byte_order);
	(*env)->SetIntField(env, lpObject, lpCache->width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, lpCache->height, (jint)lpStruct->height);
	(*env)->SetShortField(env, lpObject, lpCache->depth, (jshort)lpStruct->depth);
	(*env)->SetShortField(env, lpObject, lpCache->bpp, (jshort)lpStruct->bpp);
	(*env)->SetShortField(env, lpObject, lpCache->bpl, (jshort)lpStruct->bpl);
	(*env)->SetShortField(env, lpObject, lpCache->bits_per_pixel, (jshort)lpStruct->bits_per_pixel);
	(*env)->SetIntField(env, lpObject, lpCache->mem, (jint)lpStruct->mem);
	(*env)->SetIntField(env, lpObject, lpCache->colormap, (jint)lpStruct->colormap);
	(*env)->SetIntField(env, lpObject, lpCache->windowing_data, (jint)lpStruct->windowing_data);
}

typedef struct GdkRectangle_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height;
} GdkRectangle_FID_CACHE;
typedef GdkRectangle_FID_CACHE *PGdkRectangle_FID_CACHE;

GdkRectangle_FID_CACHE GdkRectangleFc;

void cacheGdkRectangleFids(JNIEnv *env, jobject lpObject, PGdkRectangle_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->width = (*env)->GetFieldID(env, lpCache->clazz, "width", "I");
	lpCache->height = (*env)->GetFieldID(env, lpCache->clazz, "height", "I");
	lpCache->cached = 1;
}

GdkRectangle *getGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpStruct)
{
	PGdkRectangle_FID_CACHE lpCache = &GdkRectangleFc;
	if (!lpCache->cached) cacheGdkRectangleFids(env, lpObject, lpCache);
	lpStruct->x = (gint)(*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->y = (gint)(*env)->GetIntField(env, lpObject, lpCache->y);
	lpStruct->width = (gint)(*env)->GetIntField(env, lpObject, lpCache->width);
	lpStruct->height = (gint)(*env)->GetIntField(env, lpObject, lpCache->height);
	return lpStruct;
}

void setGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpStruct)
{
	PGdkRectangle_FID_CACHE lpCache = &GdkRectangleFc;
	if (!lpCache->cached) cacheGdkRectangleFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, lpCache->y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, lpCache->width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, lpCache->height, (jint)lpStruct->height);
}

typedef struct GdkVisual_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, depth, byte_order, colormap_size, bits_per_rgb, red_mask, red_shift, red_prec, green_mask, green_shift, green_prec, blue_mask, blue_shift, blue_prec;
} GdkVisual_FID_CACHE;
typedef GdkVisual_FID_CACHE *PGdkVisual_FID_CACHE;

GdkVisual_FID_CACHE GdkVisualFc;

void cacheGdkVisualFids(JNIEnv *env, jobject lpObject, PGdkVisual_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->type = (*env)->GetFieldID(env, lpCache->clazz, "type", "I");
	lpCache->depth = (*env)->GetFieldID(env, lpCache->clazz, "depth", "I");
	lpCache->byte_order = (*env)->GetFieldID(env, lpCache->clazz, "byte_order", "I");
	lpCache->colormap_size = (*env)->GetFieldID(env, lpCache->clazz, "colormap_size", "I");
	lpCache->bits_per_rgb = (*env)->GetFieldID(env, lpCache->clazz, "bits_per_rgb", "I");
	lpCache->red_mask = (*env)->GetFieldID(env, lpCache->clazz, "red_mask", "I");
	lpCache->red_shift = (*env)->GetFieldID(env, lpCache->clazz, "red_shift", "I");
	lpCache->red_prec = (*env)->GetFieldID(env, lpCache->clazz, "red_prec", "I");
	lpCache->green_mask = (*env)->GetFieldID(env, lpCache->clazz, "green_mask", "I");
	lpCache->green_shift = (*env)->GetFieldID(env, lpCache->clazz, "green_shift", "I");
	lpCache->green_prec = (*env)->GetFieldID(env, lpCache->clazz, "green_prec", "I");
	lpCache->blue_mask = (*env)->GetFieldID(env, lpCache->clazz, "blue_mask", "I");
	lpCache->blue_shift = (*env)->GetFieldID(env, lpCache->clazz, "blue_shift", "I");
	lpCache->blue_prec = (*env)->GetFieldID(env, lpCache->clazz, "blue_prec", "I");
	lpCache->cached = 1;
}

GdkVisual *getGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpStruct)
{
	PGdkVisual_FID_CACHE lpCache = &GdkVisualFc;
	if (!lpCache->cached) cacheGdkVisualFids(env, lpObject, lpCache);
	lpStruct->type = (GdkVisualType)(*env)->GetIntField(env, lpObject, lpCache->type);
	lpStruct->depth = (gint)(*env)->GetIntField(env, lpObject, lpCache->depth);
	lpStruct->byte_order = (GdkByteOrder)(*env)->GetIntField(env, lpObject, lpCache->byte_order);
	lpStruct->colormap_size = (gint)(*env)->GetIntField(env, lpObject, lpCache->colormap_size);
	lpStruct->bits_per_rgb = (gint)(*env)->GetIntField(env, lpObject, lpCache->bits_per_rgb);
	lpStruct->red_mask = (guint32)(*env)->GetIntField(env, lpObject, lpCache->red_mask);
	lpStruct->red_shift = (gint)(*env)->GetIntField(env, lpObject, lpCache->red_shift);
	lpStruct->red_prec = (gint)(*env)->GetIntField(env, lpObject, lpCache->red_prec);
	lpStruct->green_mask = (guint32)(*env)->GetIntField(env, lpObject, lpCache->green_mask);
	lpStruct->green_shift = (gint)(*env)->GetIntField(env, lpObject, lpCache->green_shift);
	lpStruct->green_prec = (gint)(*env)->GetIntField(env, lpObject, lpCache->green_prec);
	lpStruct->blue_mask = (guint32)(*env)->GetIntField(env, lpObject, lpCache->blue_mask);
	lpStruct->blue_shift = (gint)(*env)->GetIntField(env, lpObject, lpCache->blue_shift);
	lpStruct->blue_prec = (gint)(*env)->GetIntField(env, lpObject, lpCache->blue_prec);
	return lpStruct;
}

void setGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpStruct)
{
	PGdkVisual_FID_CACHE lpCache = &GdkVisualFc;
	if (!lpCache->cached) cacheGdkVisualFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, lpCache->depth, (jint)lpStruct->depth);
	(*env)->SetIntField(env, lpObject, lpCache->byte_order, (jint)lpStruct->byte_order);
	(*env)->SetIntField(env, lpObject, lpCache->colormap_size, (jint)lpStruct->colormap_size);
	(*env)->SetIntField(env, lpObject, lpCache->bits_per_rgb, (jint)lpStruct->bits_per_rgb);
	(*env)->SetIntField(env, lpObject, lpCache->red_mask, (jint)lpStruct->red_mask);
	(*env)->SetIntField(env, lpObject, lpCache->red_shift, (jint)lpStruct->red_shift);
	(*env)->SetIntField(env, lpObject, lpCache->red_prec, (jint)lpStruct->red_prec);
	(*env)->SetIntField(env, lpObject, lpCache->green_mask, (jint)lpStruct->green_mask);
	(*env)->SetIntField(env, lpObject, lpCache->green_shift, (jint)lpStruct->green_shift);
	(*env)->SetIntField(env, lpObject, lpCache->green_prec, (jint)lpStruct->green_prec);
	(*env)->SetIntField(env, lpObject, lpCache->blue_mask, (jint)lpStruct->blue_mask);
	(*env)->SetIntField(env, lpObject, lpCache->blue_shift, (jint)lpStruct->blue_shift);
	(*env)->SetIntField(env, lpObject, lpCache->blue_prec, (jint)lpStruct->blue_prec);
}

typedef struct GtkAdjustment_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lower, upper, value, step_increment, page_increment, page_size;
} GtkAdjustment_FID_CACHE;
typedef GtkAdjustment_FID_CACHE *PGtkAdjustment_FID_CACHE;

GtkAdjustment_FID_CACHE GtkAdjustmentFc;

void cacheGtkAdjustmentFids(JNIEnv *env, jobject lpObject, PGtkAdjustment_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->lower = (*env)->GetFieldID(env, lpCache->clazz, "lower", "D");
	lpCache->upper = (*env)->GetFieldID(env, lpCache->clazz, "upper", "D");
	lpCache->value = (*env)->GetFieldID(env, lpCache->clazz, "value", "D");
	lpCache->step_increment = (*env)->GetFieldID(env, lpCache->clazz, "step_increment", "D");
	lpCache->page_increment = (*env)->GetFieldID(env, lpCache->clazz, "page_increment", "D");
	lpCache->page_size = (*env)->GetFieldID(env, lpCache->clazz, "page_size", "D");
	lpCache->cached = 1;
}

GtkAdjustment *getGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpStruct)
{
	PGtkAdjustment_FID_CACHE lpCache = &GtkAdjustmentFc;
	if (!lpCache->cached) cacheGtkAdjustmentFids(env, lpObject, lpCache);
	lpStruct->lower = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->lower);
	lpStruct->upper = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->upper);
	lpStruct->value = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->value);
	lpStruct->step_increment = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->step_increment);
	lpStruct->page_increment = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->page_increment);
	lpStruct->page_size = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->page_size);
	return lpStruct;
}

void setGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpStruct)
{
	PGtkAdjustment_FID_CACHE lpCache = &GtkAdjustmentFc;
	if (!lpCache->cached) cacheGtkAdjustmentFids(env, lpObject, lpCache);
	(*env)->SetDoubleField(env, lpObject, lpCache->lower, (jdouble)lpStruct->lower);
	(*env)->SetDoubleField(env, lpObject, lpCache->upper, (jdouble)lpStruct->upper);
	(*env)->SetDoubleField(env, lpObject, lpCache->value, (jdouble)lpStruct->value);
	(*env)->SetDoubleField(env, lpObject, lpCache->step_increment, (jdouble)lpStruct->step_increment);
	(*env)->SetDoubleField(env, lpObject, lpCache->page_increment, (jdouble)lpStruct->page_increment);
	(*env)->SetDoubleField(env, lpObject, lpCache->page_size, (jdouble)lpStruct->page_size);
}

typedef struct GtkAllocation_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height;
} GtkAllocation_FID_CACHE;
typedef GtkAllocation_FID_CACHE *PGtkAllocation_FID_CACHE;

GtkAllocation_FID_CACHE GtkAllocationFc;

void cacheGtkAllocationFids(JNIEnv *env, jobject lpObject, PGtkAllocation_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "I");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "I");
	lpCache->width = (*env)->GetFieldID(env, lpCache->clazz, "width", "I");
	lpCache->height = (*env)->GetFieldID(env, lpCache->clazz, "height", "I");
	lpCache->cached = 1;
}

GtkAllocation *getGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpStruct)
{
	PGtkAllocation_FID_CACHE lpCache = &GtkAllocationFc;
	if (!lpCache->cached) cacheGtkAllocationFids(env, lpObject, lpCache);
	lpStruct->x = (*env)->GetIntField(env, lpObject, lpCache->x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, lpCache->y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, lpCache->width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, lpCache->height);
	return lpStruct;
}

void setGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpStruct)
{
	PGtkAllocation_FID_CACHE lpCache = &GtkAllocationFc;
	if (!lpCache->cached) cacheGtkAllocationFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, lpCache->y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, lpCache->width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, lpCache->height, (jint)lpStruct->height);
}

typedef struct GtkCombo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID entry, list;
} GtkCombo_FID_CACHE;
typedef GtkCombo_FID_CACHE *PGtkCombo_FID_CACHE;

GtkCombo_FID_CACHE GtkComboFc;

void cacheGtkComboFids(JNIEnv *env, jobject lpObject, PGtkCombo_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->entry = (*env)->GetFieldID(env, lpCache->clazz, "entry", "I");
	lpCache->list = (*env)->GetFieldID(env, lpCache->clazz, "list", "I");
	lpCache->cached = 1;
}

GtkCombo *getGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct)
{
	PGtkCombo_FID_CACHE lpCache = &GtkComboFc;
	if (!lpCache->cached) cacheGtkComboFids(env, lpObject, lpCache);
	lpStruct->entry = (GtkWidget *)(*env)->GetIntField(env, lpObject, lpCache->entry);
	lpStruct->list = (GtkWidget *)(*env)->GetIntField(env, lpObject, lpCache->list);
	return lpStruct;
}

void setGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct)
{
	PGtkCombo_FID_CACHE lpCache = &GtkComboFc;
	if (!lpCache->cached) cacheGtkComboFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->entry, (jint)lpStruct->entry);
	(*env)->SetIntField(env, lpObject, lpCache->list, (jint)lpStruct->list);
}

typedef struct GtkRequisition_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID width, height;
} GtkRequisition_FID_CACHE;
typedef GtkRequisition_FID_CACHE *PGtkRequisition_FID_CACHE;

GtkRequisition_FID_CACHE GtkRequisitionFc;

void cacheGtkRequisitionFids(JNIEnv *env, jobject lpObject, PGtkRequisition_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->width = (*env)->GetFieldID(env, lpCache->clazz, "width", "I");
	lpCache->height = (*env)->GetFieldID(env, lpCache->clazz, "height", "I");
	lpCache->cached = 1;
}

GtkRequisition *getGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpStruct)
{
	PGtkRequisition_FID_CACHE lpCache = &GtkRequisitionFc;
	if (!lpCache->cached) cacheGtkRequisitionFids(env, lpObject, lpCache);
	lpStruct->width = (*env)->GetIntField(env, lpObject, lpCache->width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, lpCache->height);
	return lpStruct;
}

void setGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpStruct)
{
	PGtkRequisition_FID_CACHE lpCache = &GtkRequisitionFc;
	if (!lpCache->cached) cacheGtkRequisitionFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, lpCache->height, (jint)lpStruct->height);
}

typedef struct GtkSelectionData_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID selection, target, type, format, data, length;
} GtkSelectionData_FID_CACHE;
typedef GtkSelectionData_FID_CACHE *PGtkSelectionData_FID_CACHE;

GtkSelectionData_FID_CACHE GtkSelectionDataFc;

void cacheGtkSelectionDataFids(JNIEnv *env, jobject lpObject, PGtkSelectionData_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->selection = (*env)->GetFieldID(env, lpCache->clazz, "selection", "I");
	lpCache->target = (*env)->GetFieldID(env, lpCache->clazz, "target", "I");
	lpCache->type = (*env)->GetFieldID(env, lpCache->clazz, "type", "I");
	lpCache->format = (*env)->GetFieldID(env, lpCache->clazz, "format", "I");
	lpCache->data = (*env)->GetFieldID(env, lpCache->clazz, "data", "I");
	lpCache->length = (*env)->GetFieldID(env, lpCache->clazz, "length", "I");
	lpCache->cached = 1;
}

GtkSelectionData *getGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct)
{
	PGtkSelectionData_FID_CACHE lpCache = &GtkSelectionDataFc;
	if (!lpCache->cached) cacheGtkSelectionDataFids(env, lpObject, lpCache);
	lpStruct->selection = (GdkAtom)(*env)->GetIntField(env, lpObject, lpCache->selection);
	lpStruct->target = (GdkAtom)(*env)->GetIntField(env, lpObject, lpCache->target);
	lpStruct->type = (GdkAtom)(*env)->GetIntField(env, lpObject, lpCache->type);
	lpStruct->format = (gint)(*env)->GetIntField(env, lpObject, lpCache->format);
	lpStruct->data = (guchar *)(*env)->GetIntField(env, lpObject, lpCache->data);
	lpStruct->length = (gint)(*env)->GetIntField(env, lpObject, lpCache->length);
	return lpStruct;
}

void setGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct)
{
	PGtkSelectionData_FID_CACHE lpCache = &GtkSelectionDataFc;
	if (!lpCache->cached) cacheGtkSelectionDataFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->selection, (jint)lpStruct->selection);
	(*env)->SetIntField(env, lpObject, lpCache->target, (jint)lpStruct->target);
	(*env)->SetIntField(env, lpObject, lpCache->type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, lpCache->format, (jint)lpStruct->format);
	(*env)->SetIntField(env, lpObject, lpCache->data, (jint)lpStruct->data);
	(*env)->SetIntField(env, lpObject, lpCache->length, (jint)lpStruct->length);
}

typedef struct GtkStyle_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID fg0_pixel, fg0_red, fg0_green, fg0_blue, fg1_pixel, fg1_red, fg1_green, fg1_blue, fg2_pixel, fg2_red, fg2_green, fg2_blue, fg3_pixel, fg3_red, fg3_green, fg3_blue, fg4_pixel, fg4_red, fg4_green, fg4_blue, bg0_pixel, bg0_red, bg0_green, bg0_blue, bg1_pixel, bg1_red, bg1_green, bg1_blue, bg2_pixel, bg2_red, bg2_green, bg2_blue, bg3_pixel, bg3_red, bg3_green, bg3_blue, bg4_pixel, bg4_red, bg4_green, bg4_blue, light0_pixel, light0_red, light0_green, light0_blue, light1_pixel, light1_red, light1_green, light1_blue, light2_pixel, light2_red, light2_green, light2_blue, light3_pixel, light3_red, light3_green, light3_blue, light4_pixel, light4_red, light4_green, light4_blue, dark0_pixel, dark0_red, dark0_green, dark0_blue, dark1_pixel, dark1_red, dark1_green, dark1_blue, dark2_pixel, dark2_red, dark2_green, dark2_blue, dark3_pixel, dark3_red, dark3_green, dark3_blue, dark4_pixel, dark4_red, dark4_green, dark4_blue, mid0_pixel, mid0_red, mid0_green, mid0_blue, mid1_pixel, mid1_red, mid1_green, mid1_blue, mid2_pixel, mid2_red, mid2_green, mid2_blue, mid3_pixel, mid3_red, mid3_green, mid3_blue, mid4_pixel, mid4_red, mid4_green, mid4_blue, text0_pixel, text0_red, text0_green, text0_blue, text1_pixel, text1_red, text1_green, text1_blue, text2_pixel, text2_red, text2_green, text2_blue, text3_pixel, text3_red, text3_green, text3_blue, text4_pixel, text4_red, text4_green, text4_blue, base0_pixel, base0_red, base0_green, base0_blue, base1_pixel, base1_red, base1_green, base1_blue, base2_pixel, base2_red, base2_green, base2_blue, base3_pixel, base3_red, base3_green, base3_blue, base4_pixel, base4_red, base4_green, base4_blue, black_pixel, black_red, black_green, black_blue, white_pixel, white_red, white_green, white_blue, font_desc, xthickness, ythickness, fg_gc0, fg_gc1, fg_gc2, fg_gc3, fg_gc4, bg_gc0, bg_gc1, bg_gc2, bg_gc3, bg_gc4, light_gc0, light_gc1, light_gc2, light_gc3, light_gc4, dark_gc0, dark_gc1, dark_gc2, dark_gc3, dark_gc4, mid_gc0, mid_gc1, mid_gc2, mid_gc3, mid_gc4, text_gc0, text_gc1, text_gc2, text_gc3, text_gc4, base_gc0, base_gc1, base_gc2, base_gc3, base_gc4, black_gc, white_gc, bg_pixmap0, bg_pixmap1, bg_pixmap2, bg_pixmap3, bg_pixmap4, bg_pixmap5;
} GtkStyle_FID_CACHE;
typedef GtkStyle_FID_CACHE *PGtkStyle_FID_CACHE;

GtkStyle_FID_CACHE GtkStyleFc;

void cacheGtkStyleFids(JNIEnv *env, jobject lpObject, PGtkStyle_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->fg0_pixel = (*env)->GetFieldID(env, lpCache->clazz, "fg0_pixel", "I");
	lpCache->fg0_red = (*env)->GetFieldID(env, lpCache->clazz, "fg0_red", "S");
	lpCache->fg0_green = (*env)->GetFieldID(env, lpCache->clazz, "fg0_green", "S");
	lpCache->fg0_blue = (*env)->GetFieldID(env, lpCache->clazz, "fg0_blue", "S");
	lpCache->fg1_pixel = (*env)->GetFieldID(env, lpCache->clazz, "fg1_pixel", "I");
	lpCache->fg1_red = (*env)->GetFieldID(env, lpCache->clazz, "fg1_red", "S");
	lpCache->fg1_green = (*env)->GetFieldID(env, lpCache->clazz, "fg1_green", "S");
	lpCache->fg1_blue = (*env)->GetFieldID(env, lpCache->clazz, "fg1_blue", "S");
	lpCache->fg2_pixel = (*env)->GetFieldID(env, lpCache->clazz, "fg2_pixel", "I");
	lpCache->fg2_red = (*env)->GetFieldID(env, lpCache->clazz, "fg2_red", "S");
	lpCache->fg2_green = (*env)->GetFieldID(env, lpCache->clazz, "fg2_green", "S");
	lpCache->fg2_blue = (*env)->GetFieldID(env, lpCache->clazz, "fg2_blue", "S");
	lpCache->fg3_pixel = (*env)->GetFieldID(env, lpCache->clazz, "fg3_pixel", "I");
	lpCache->fg3_red = (*env)->GetFieldID(env, lpCache->clazz, "fg3_red", "S");
	lpCache->fg3_green = (*env)->GetFieldID(env, lpCache->clazz, "fg3_green", "S");
	lpCache->fg3_blue = (*env)->GetFieldID(env, lpCache->clazz, "fg3_blue", "S");
	lpCache->fg4_pixel = (*env)->GetFieldID(env, lpCache->clazz, "fg4_pixel", "I");
	lpCache->fg4_red = (*env)->GetFieldID(env, lpCache->clazz, "fg4_red", "S");
	lpCache->fg4_green = (*env)->GetFieldID(env, lpCache->clazz, "fg4_green", "S");
	lpCache->fg4_blue = (*env)->GetFieldID(env, lpCache->clazz, "fg4_blue", "S");
	lpCache->bg0_pixel = (*env)->GetFieldID(env, lpCache->clazz, "bg0_pixel", "I");
	lpCache->bg0_red = (*env)->GetFieldID(env, lpCache->clazz, "bg0_red", "S");
	lpCache->bg0_green = (*env)->GetFieldID(env, lpCache->clazz, "bg0_green", "S");
	lpCache->bg0_blue = (*env)->GetFieldID(env, lpCache->clazz, "bg0_blue", "S");
	lpCache->bg1_pixel = (*env)->GetFieldID(env, lpCache->clazz, "bg1_pixel", "I");
	lpCache->bg1_red = (*env)->GetFieldID(env, lpCache->clazz, "bg1_red", "S");
	lpCache->bg1_green = (*env)->GetFieldID(env, lpCache->clazz, "bg1_green", "S");
	lpCache->bg1_blue = (*env)->GetFieldID(env, lpCache->clazz, "bg1_blue", "S");
	lpCache->bg2_pixel = (*env)->GetFieldID(env, lpCache->clazz, "bg2_pixel", "I");
	lpCache->bg2_red = (*env)->GetFieldID(env, lpCache->clazz, "bg2_red", "S");
	lpCache->bg2_green = (*env)->GetFieldID(env, lpCache->clazz, "bg2_green", "S");
	lpCache->bg2_blue = (*env)->GetFieldID(env, lpCache->clazz, "bg2_blue", "S");
	lpCache->bg3_pixel = (*env)->GetFieldID(env, lpCache->clazz, "bg3_pixel", "I");
	lpCache->bg3_red = (*env)->GetFieldID(env, lpCache->clazz, "bg3_red", "S");
	lpCache->bg3_green = (*env)->GetFieldID(env, lpCache->clazz, "bg3_green", "S");
	lpCache->bg3_blue = (*env)->GetFieldID(env, lpCache->clazz, "bg3_blue", "S");
	lpCache->bg4_pixel = (*env)->GetFieldID(env, lpCache->clazz, "bg4_pixel", "I");
	lpCache->bg4_red = (*env)->GetFieldID(env, lpCache->clazz, "bg4_red", "S");
	lpCache->bg4_green = (*env)->GetFieldID(env, lpCache->clazz, "bg4_green", "S");
	lpCache->bg4_blue = (*env)->GetFieldID(env, lpCache->clazz, "bg4_blue", "S");
	lpCache->light0_pixel = (*env)->GetFieldID(env, lpCache->clazz, "light0_pixel", "I");
	lpCache->light0_red = (*env)->GetFieldID(env, lpCache->clazz, "light0_red", "S");
	lpCache->light0_green = (*env)->GetFieldID(env, lpCache->clazz, "light0_green", "S");
	lpCache->light0_blue = (*env)->GetFieldID(env, lpCache->clazz, "light0_blue", "S");
	lpCache->light1_pixel = (*env)->GetFieldID(env, lpCache->clazz, "light1_pixel", "I");
	lpCache->light1_red = (*env)->GetFieldID(env, lpCache->clazz, "light1_red", "S");
	lpCache->light1_green = (*env)->GetFieldID(env, lpCache->clazz, "light1_green", "S");
	lpCache->light1_blue = (*env)->GetFieldID(env, lpCache->clazz, "light1_blue", "S");
	lpCache->light2_pixel = (*env)->GetFieldID(env, lpCache->clazz, "light2_pixel", "I");
	lpCache->light2_red = (*env)->GetFieldID(env, lpCache->clazz, "light2_red", "S");
	lpCache->light2_green = (*env)->GetFieldID(env, lpCache->clazz, "light2_green", "S");
	lpCache->light2_blue = (*env)->GetFieldID(env, lpCache->clazz, "light2_blue", "S");
	lpCache->light3_pixel = (*env)->GetFieldID(env, lpCache->clazz, "light3_pixel", "I");
	lpCache->light3_red = (*env)->GetFieldID(env, lpCache->clazz, "light3_red", "S");
	lpCache->light3_green = (*env)->GetFieldID(env, lpCache->clazz, "light3_green", "S");
	lpCache->light3_blue = (*env)->GetFieldID(env, lpCache->clazz, "light3_blue", "S");
	lpCache->light4_pixel = (*env)->GetFieldID(env, lpCache->clazz, "light4_pixel", "I");
	lpCache->light4_red = (*env)->GetFieldID(env, lpCache->clazz, "light4_red", "S");
	lpCache->light4_green = (*env)->GetFieldID(env, lpCache->clazz, "light4_green", "S");
	lpCache->light4_blue = (*env)->GetFieldID(env, lpCache->clazz, "light4_blue", "S");
	lpCache->dark0_pixel = (*env)->GetFieldID(env, lpCache->clazz, "dark0_pixel", "I");
	lpCache->dark0_red = (*env)->GetFieldID(env, lpCache->clazz, "dark0_red", "S");
	lpCache->dark0_green = (*env)->GetFieldID(env, lpCache->clazz, "dark0_green", "S");
	lpCache->dark0_blue = (*env)->GetFieldID(env, lpCache->clazz, "dark0_blue", "S");
	lpCache->dark1_pixel = (*env)->GetFieldID(env, lpCache->clazz, "dark1_pixel", "I");
	lpCache->dark1_red = (*env)->GetFieldID(env, lpCache->clazz, "dark1_red", "S");
	lpCache->dark1_green = (*env)->GetFieldID(env, lpCache->clazz, "dark1_green", "S");
	lpCache->dark1_blue = (*env)->GetFieldID(env, lpCache->clazz, "dark1_blue", "S");
	lpCache->dark2_pixel = (*env)->GetFieldID(env, lpCache->clazz, "dark2_pixel", "I");
	lpCache->dark2_red = (*env)->GetFieldID(env, lpCache->clazz, "dark2_red", "S");
	lpCache->dark2_green = (*env)->GetFieldID(env, lpCache->clazz, "dark2_green", "S");
	lpCache->dark2_blue = (*env)->GetFieldID(env, lpCache->clazz, "dark2_blue", "S");
	lpCache->dark3_pixel = (*env)->GetFieldID(env, lpCache->clazz, "dark3_pixel", "I");
	lpCache->dark3_red = (*env)->GetFieldID(env, lpCache->clazz, "dark3_red", "S");
	lpCache->dark3_green = (*env)->GetFieldID(env, lpCache->clazz, "dark3_green", "S");
	lpCache->dark3_blue = (*env)->GetFieldID(env, lpCache->clazz, "dark3_blue", "S");
	lpCache->dark4_pixel = (*env)->GetFieldID(env, lpCache->clazz, "dark4_pixel", "I");
	lpCache->dark4_red = (*env)->GetFieldID(env, lpCache->clazz, "dark4_red", "S");
	lpCache->dark4_green = (*env)->GetFieldID(env, lpCache->clazz, "dark4_green", "S");
	lpCache->dark4_blue = (*env)->GetFieldID(env, lpCache->clazz, "dark4_blue", "S");
	lpCache->mid0_pixel = (*env)->GetFieldID(env, lpCache->clazz, "mid0_pixel", "I");
	lpCache->mid0_red = (*env)->GetFieldID(env, lpCache->clazz, "mid0_red", "S");
	lpCache->mid0_green = (*env)->GetFieldID(env, lpCache->clazz, "mid0_green", "S");
	lpCache->mid0_blue = (*env)->GetFieldID(env, lpCache->clazz, "mid0_blue", "S");
	lpCache->mid1_pixel = (*env)->GetFieldID(env, lpCache->clazz, "mid1_pixel", "I");
	lpCache->mid1_red = (*env)->GetFieldID(env, lpCache->clazz, "mid1_red", "S");
	lpCache->mid1_green = (*env)->GetFieldID(env, lpCache->clazz, "mid1_green", "S");
	lpCache->mid1_blue = (*env)->GetFieldID(env, lpCache->clazz, "mid1_blue", "S");
	lpCache->mid2_pixel = (*env)->GetFieldID(env, lpCache->clazz, "mid2_pixel", "I");
	lpCache->mid2_red = (*env)->GetFieldID(env, lpCache->clazz, "mid2_red", "S");
	lpCache->mid2_green = (*env)->GetFieldID(env, lpCache->clazz, "mid2_green", "S");
	lpCache->mid2_blue = (*env)->GetFieldID(env, lpCache->clazz, "mid2_blue", "S");
	lpCache->mid3_pixel = (*env)->GetFieldID(env, lpCache->clazz, "mid3_pixel", "I");
	lpCache->mid3_red = (*env)->GetFieldID(env, lpCache->clazz, "mid3_red", "S");
	lpCache->mid3_green = (*env)->GetFieldID(env, lpCache->clazz, "mid3_green", "S");
	lpCache->mid3_blue = (*env)->GetFieldID(env, lpCache->clazz, "mid3_blue", "S");
	lpCache->mid4_pixel = (*env)->GetFieldID(env, lpCache->clazz, "mid4_pixel", "I");
	lpCache->mid4_red = (*env)->GetFieldID(env, lpCache->clazz, "mid4_red", "S");
	lpCache->mid4_green = (*env)->GetFieldID(env, lpCache->clazz, "mid4_green", "S");
	lpCache->mid4_blue = (*env)->GetFieldID(env, lpCache->clazz, "mid4_blue", "S");
	lpCache->text0_pixel = (*env)->GetFieldID(env, lpCache->clazz, "text0_pixel", "I");
	lpCache->text0_red = (*env)->GetFieldID(env, lpCache->clazz, "text0_red", "S");
	lpCache->text0_green = (*env)->GetFieldID(env, lpCache->clazz, "text0_green", "S");
	lpCache->text0_blue = (*env)->GetFieldID(env, lpCache->clazz, "text0_blue", "S");
	lpCache->text1_pixel = (*env)->GetFieldID(env, lpCache->clazz, "text1_pixel", "I");
	lpCache->text1_red = (*env)->GetFieldID(env, lpCache->clazz, "text1_red", "S");
	lpCache->text1_green = (*env)->GetFieldID(env, lpCache->clazz, "text1_green", "S");
	lpCache->text1_blue = (*env)->GetFieldID(env, lpCache->clazz, "text1_blue", "S");
	lpCache->text2_pixel = (*env)->GetFieldID(env, lpCache->clazz, "text2_pixel", "I");
	lpCache->text2_red = (*env)->GetFieldID(env, lpCache->clazz, "text2_red", "S");
	lpCache->text2_green = (*env)->GetFieldID(env, lpCache->clazz, "text2_green", "S");
	lpCache->text2_blue = (*env)->GetFieldID(env, lpCache->clazz, "text2_blue", "S");
	lpCache->text3_pixel = (*env)->GetFieldID(env, lpCache->clazz, "text3_pixel", "I");
	lpCache->text3_red = (*env)->GetFieldID(env, lpCache->clazz, "text3_red", "S");
	lpCache->text3_green = (*env)->GetFieldID(env, lpCache->clazz, "text3_green", "S");
	lpCache->text3_blue = (*env)->GetFieldID(env, lpCache->clazz, "text3_blue", "S");
	lpCache->text4_pixel = (*env)->GetFieldID(env, lpCache->clazz, "text4_pixel", "I");
	lpCache->text4_red = (*env)->GetFieldID(env, lpCache->clazz, "text4_red", "S");
	lpCache->text4_green = (*env)->GetFieldID(env, lpCache->clazz, "text4_green", "S");
	lpCache->text4_blue = (*env)->GetFieldID(env, lpCache->clazz, "text4_blue", "S");
	lpCache->base0_pixel = (*env)->GetFieldID(env, lpCache->clazz, "base0_pixel", "I");
	lpCache->base0_red = (*env)->GetFieldID(env, lpCache->clazz, "base0_red", "S");
	lpCache->base0_green = (*env)->GetFieldID(env, lpCache->clazz, "base0_green", "S");
	lpCache->base0_blue = (*env)->GetFieldID(env, lpCache->clazz, "base0_blue", "S");
	lpCache->base1_pixel = (*env)->GetFieldID(env, lpCache->clazz, "base1_pixel", "I");
	lpCache->base1_red = (*env)->GetFieldID(env, lpCache->clazz, "base1_red", "S");
	lpCache->base1_green = (*env)->GetFieldID(env, lpCache->clazz, "base1_green", "S");
	lpCache->base1_blue = (*env)->GetFieldID(env, lpCache->clazz, "base1_blue", "S");
	lpCache->base2_pixel = (*env)->GetFieldID(env, lpCache->clazz, "base2_pixel", "I");
	lpCache->base2_red = (*env)->GetFieldID(env, lpCache->clazz, "base2_red", "S");
	lpCache->base2_green = (*env)->GetFieldID(env, lpCache->clazz, "base2_green", "S");
	lpCache->base2_blue = (*env)->GetFieldID(env, lpCache->clazz, "base2_blue", "S");
	lpCache->base3_pixel = (*env)->GetFieldID(env, lpCache->clazz, "base3_pixel", "I");
	lpCache->base3_red = (*env)->GetFieldID(env, lpCache->clazz, "base3_red", "S");
	lpCache->base3_green = (*env)->GetFieldID(env, lpCache->clazz, "base3_green", "S");
	lpCache->base3_blue = (*env)->GetFieldID(env, lpCache->clazz, "base3_blue", "S");
	lpCache->base4_pixel = (*env)->GetFieldID(env, lpCache->clazz, "base4_pixel", "I");
	lpCache->base4_red = (*env)->GetFieldID(env, lpCache->clazz, "base4_red", "S");
	lpCache->base4_green = (*env)->GetFieldID(env, lpCache->clazz, "base4_green", "S");
	lpCache->base4_blue = (*env)->GetFieldID(env, lpCache->clazz, "base4_blue", "S");
	lpCache->black_pixel = (*env)->GetFieldID(env, lpCache->clazz, "black_pixel", "I");
	lpCache->black_red = (*env)->GetFieldID(env, lpCache->clazz, "black_red", "S");
	lpCache->black_green = (*env)->GetFieldID(env, lpCache->clazz, "black_green", "S");
	lpCache->black_blue = (*env)->GetFieldID(env, lpCache->clazz, "black_blue", "S");
	lpCache->white_pixel = (*env)->GetFieldID(env, lpCache->clazz, "white_pixel", "I");
	lpCache->white_red = (*env)->GetFieldID(env, lpCache->clazz, "white_red", "S");
	lpCache->white_green = (*env)->GetFieldID(env, lpCache->clazz, "white_green", "S");
	lpCache->white_blue = (*env)->GetFieldID(env, lpCache->clazz, "white_blue", "S");
	lpCache->font_desc = (*env)->GetFieldID(env, lpCache->clazz, "font_desc", "I");
	lpCache->xthickness = (*env)->GetFieldID(env, lpCache->clazz, "xthickness", "I");
	lpCache->ythickness = (*env)->GetFieldID(env, lpCache->clazz, "ythickness", "I");
	lpCache->fg_gc0 = (*env)->GetFieldID(env, lpCache->clazz, "fg_gc0", "I");
	lpCache->fg_gc1 = (*env)->GetFieldID(env, lpCache->clazz, "fg_gc1", "I");
	lpCache->fg_gc2 = (*env)->GetFieldID(env, lpCache->clazz, "fg_gc2", "I");
	lpCache->fg_gc3 = (*env)->GetFieldID(env, lpCache->clazz, "fg_gc3", "I");
	lpCache->fg_gc4 = (*env)->GetFieldID(env, lpCache->clazz, "fg_gc4", "I");
	lpCache->bg_gc0 = (*env)->GetFieldID(env, lpCache->clazz, "bg_gc0", "I");
	lpCache->bg_gc1 = (*env)->GetFieldID(env, lpCache->clazz, "bg_gc1", "I");
	lpCache->bg_gc2 = (*env)->GetFieldID(env, lpCache->clazz, "bg_gc2", "I");
	lpCache->bg_gc3 = (*env)->GetFieldID(env, lpCache->clazz, "bg_gc3", "I");
	lpCache->bg_gc4 = (*env)->GetFieldID(env, lpCache->clazz, "bg_gc4", "I");
	lpCache->light_gc0 = (*env)->GetFieldID(env, lpCache->clazz, "light_gc0", "I");
	lpCache->light_gc1 = (*env)->GetFieldID(env, lpCache->clazz, "light_gc1", "I");
	lpCache->light_gc2 = (*env)->GetFieldID(env, lpCache->clazz, "light_gc2", "I");
	lpCache->light_gc3 = (*env)->GetFieldID(env, lpCache->clazz, "light_gc3", "I");
	lpCache->light_gc4 = (*env)->GetFieldID(env, lpCache->clazz, "light_gc4", "I");
	lpCache->dark_gc0 = (*env)->GetFieldID(env, lpCache->clazz, "dark_gc0", "I");
	lpCache->dark_gc1 = (*env)->GetFieldID(env, lpCache->clazz, "dark_gc1", "I");
	lpCache->dark_gc2 = (*env)->GetFieldID(env, lpCache->clazz, "dark_gc2", "I");
	lpCache->dark_gc3 = (*env)->GetFieldID(env, lpCache->clazz, "dark_gc3", "I");
	lpCache->dark_gc4 = (*env)->GetFieldID(env, lpCache->clazz, "dark_gc4", "I");
	lpCache->mid_gc0 = (*env)->GetFieldID(env, lpCache->clazz, "mid_gc0", "I");
	lpCache->mid_gc1 = (*env)->GetFieldID(env, lpCache->clazz, "mid_gc1", "I");
	lpCache->mid_gc2 = (*env)->GetFieldID(env, lpCache->clazz, "mid_gc2", "I");
	lpCache->mid_gc3 = (*env)->GetFieldID(env, lpCache->clazz, "mid_gc3", "I");
	lpCache->mid_gc4 = (*env)->GetFieldID(env, lpCache->clazz, "mid_gc4", "I");
	lpCache->text_gc0 = (*env)->GetFieldID(env, lpCache->clazz, "text_gc0", "I");
	lpCache->text_gc1 = (*env)->GetFieldID(env, lpCache->clazz, "text_gc1", "I");
	lpCache->text_gc2 = (*env)->GetFieldID(env, lpCache->clazz, "text_gc2", "I");
	lpCache->text_gc3 = (*env)->GetFieldID(env, lpCache->clazz, "text_gc3", "I");
	lpCache->text_gc4 = (*env)->GetFieldID(env, lpCache->clazz, "text_gc4", "I");
	lpCache->base_gc0 = (*env)->GetFieldID(env, lpCache->clazz, "base_gc0", "I");
	lpCache->base_gc1 = (*env)->GetFieldID(env, lpCache->clazz, "base_gc1", "I");
	lpCache->base_gc2 = (*env)->GetFieldID(env, lpCache->clazz, "base_gc2", "I");
	lpCache->base_gc3 = (*env)->GetFieldID(env, lpCache->clazz, "base_gc3", "I");
	lpCache->base_gc4 = (*env)->GetFieldID(env, lpCache->clazz, "base_gc4", "I");
	lpCache->black_gc = (*env)->GetFieldID(env, lpCache->clazz, "black_gc", "I");
	lpCache->white_gc = (*env)->GetFieldID(env, lpCache->clazz, "white_gc", "I");
	lpCache->bg_pixmap0 = (*env)->GetFieldID(env, lpCache->clazz, "bg_pixmap0", "I");
	lpCache->bg_pixmap1 = (*env)->GetFieldID(env, lpCache->clazz, "bg_pixmap1", "I");
	lpCache->bg_pixmap2 = (*env)->GetFieldID(env, lpCache->clazz, "bg_pixmap2", "I");
	lpCache->bg_pixmap3 = (*env)->GetFieldID(env, lpCache->clazz, "bg_pixmap3", "I");
	lpCache->bg_pixmap4 = (*env)->GetFieldID(env, lpCache->clazz, "bg_pixmap4", "I");
	lpCache->bg_pixmap5 = (*env)->GetFieldID(env, lpCache->clazz, "bg_pixmap5", "I");
	lpCache->cached = 1;
}

GtkStyle *getGtkStyleFields(JNIEnv *env, jobject lpObject, GtkStyle *lpStruct)
{
	PGtkStyle_FID_CACHE lpCache = &GtkStyleFc;
	if (!lpCache->cached) cacheGtkStyleFids(env, lpObject, lpCache);
	lpStruct->fg[0].pixel = (*env)->GetIntField(env, lpObject, lpCache->fg0_pixel);
	lpStruct->fg[0].red = (*env)->GetShortField(env, lpObject, lpCache->fg0_red);
	lpStruct->fg[0].green = (*env)->GetShortField(env, lpObject, lpCache->fg0_green);
	lpStruct->fg[0].blue = (*env)->GetShortField(env, lpObject, lpCache->fg0_blue);
	lpStruct->fg[1].pixel = (*env)->GetIntField(env, lpObject, lpCache->fg1_pixel);
	lpStruct->fg[1].red = (*env)->GetShortField(env, lpObject, lpCache->fg1_red);
	lpStruct->fg[1].green = (*env)->GetShortField(env, lpObject, lpCache->fg1_green);
	lpStruct->fg[1].blue = (*env)->GetShortField(env, lpObject, lpCache->fg1_blue);
	lpStruct->fg[2].pixel = (*env)->GetIntField(env, lpObject, lpCache->fg2_pixel);
	lpStruct->fg[2].red = (*env)->GetShortField(env, lpObject, lpCache->fg2_red);
	lpStruct->fg[2].green = (*env)->GetShortField(env, lpObject, lpCache->fg2_green);
	lpStruct->fg[2].blue = (*env)->GetShortField(env, lpObject, lpCache->fg2_blue);
	lpStruct->fg[3].pixel = (*env)->GetIntField(env, lpObject, lpCache->fg3_pixel);
	lpStruct->fg[3].red = (*env)->GetShortField(env, lpObject, lpCache->fg3_red);
	lpStruct->fg[3].green = (*env)->GetShortField(env, lpObject, lpCache->fg3_green);
	lpStruct->fg[3].blue = (*env)->GetShortField(env, lpObject, lpCache->fg3_blue);
	lpStruct->fg[4].pixel = (*env)->GetIntField(env, lpObject, lpCache->fg4_pixel);
	lpStruct->fg[4].red = (*env)->GetShortField(env, lpObject, lpCache->fg4_red);
	lpStruct->fg[4].green = (*env)->GetShortField(env, lpObject, lpCache->fg4_green);
	lpStruct->fg[4].blue = (*env)->GetShortField(env, lpObject, lpCache->fg4_blue);
	lpStruct->bg[0].pixel = (*env)->GetIntField(env, lpObject, lpCache->bg0_pixel);
	lpStruct->bg[0].red = (*env)->GetShortField(env, lpObject, lpCache->bg0_red);
	lpStruct->bg[0].green = (*env)->GetShortField(env, lpObject, lpCache->bg0_green);
	lpStruct->bg[0].blue = (*env)->GetShortField(env, lpObject, lpCache->bg0_blue);
	lpStruct->bg[1].pixel = (*env)->GetIntField(env, lpObject, lpCache->bg1_pixel);
	lpStruct->bg[1].red = (*env)->GetShortField(env, lpObject, lpCache->bg1_red);
	lpStruct->bg[1].green = (*env)->GetShortField(env, lpObject, lpCache->bg1_green);
	lpStruct->bg[1].blue = (*env)->GetShortField(env, lpObject, lpCache->bg1_blue);
	lpStruct->bg[2].pixel = (*env)->GetIntField(env, lpObject, lpCache->bg2_pixel);
	lpStruct->bg[2].red = (*env)->GetShortField(env, lpObject, lpCache->bg2_red);
	lpStruct->bg[2].green = (*env)->GetShortField(env, lpObject, lpCache->bg2_green);
	lpStruct->bg[2].blue = (*env)->GetShortField(env, lpObject, lpCache->bg2_blue);
	lpStruct->bg[3].pixel = (*env)->GetIntField(env, lpObject, lpCache->bg3_pixel);
	lpStruct->bg[3].red = (*env)->GetShortField(env, lpObject, lpCache->bg3_red);
	lpStruct->bg[3].green = (*env)->GetShortField(env, lpObject, lpCache->bg3_green);
	lpStruct->bg[3].blue = (*env)->GetShortField(env, lpObject, lpCache->bg3_blue);
	lpStruct->bg[4].pixel = (*env)->GetIntField(env, lpObject, lpCache->bg4_pixel);
	lpStruct->bg[4].red = (*env)->GetShortField(env, lpObject, lpCache->bg4_red);
	lpStruct->bg[4].green = (*env)->GetShortField(env, lpObject, lpCache->bg4_green);
	lpStruct->bg[4].blue = (*env)->GetShortField(env, lpObject, lpCache->bg4_blue);
	lpStruct->light[0].pixel = (*env)->GetIntField(env, lpObject, lpCache->light0_pixel);
	lpStruct->light[0].red = (*env)->GetShortField(env, lpObject, lpCache->light0_red);
	lpStruct->light[0].green = (*env)->GetShortField(env, lpObject, lpCache->light0_green);
	lpStruct->light[0].blue = (*env)->GetShortField(env, lpObject, lpCache->light0_blue);
	lpStruct->light[1].pixel = (*env)->GetIntField(env, lpObject, lpCache->light1_pixel);
	lpStruct->light[1].red = (*env)->GetShortField(env, lpObject, lpCache->light1_red);
	lpStruct->light[1].green = (*env)->GetShortField(env, lpObject, lpCache->light1_green);
	lpStruct->light[1].blue = (*env)->GetShortField(env, lpObject, lpCache->light1_blue);
	lpStruct->light[2].pixel = (*env)->GetIntField(env, lpObject, lpCache->light2_pixel);
	lpStruct->light[2].red = (*env)->GetShortField(env, lpObject, lpCache->light2_red);
	lpStruct->light[2].green = (*env)->GetShortField(env, lpObject, lpCache->light2_green);
	lpStruct->light[2].blue = (*env)->GetShortField(env, lpObject, lpCache->light2_blue);
	lpStruct->light[3].pixel = (*env)->GetIntField(env, lpObject, lpCache->light3_pixel);
	lpStruct->light[3].red = (*env)->GetShortField(env, lpObject, lpCache->light3_red);
	lpStruct->light[3].green = (*env)->GetShortField(env, lpObject, lpCache->light3_green);
	lpStruct->light[3].blue = (*env)->GetShortField(env, lpObject, lpCache->light3_blue);
	lpStruct->light[4].pixel = (*env)->GetIntField(env, lpObject, lpCache->light4_pixel);
	lpStruct->light[4].red = (*env)->GetShortField(env, lpObject, lpCache->light4_red);
	lpStruct->light[4].green = (*env)->GetShortField(env, lpObject, lpCache->light4_green);
	lpStruct->light[4].blue = (*env)->GetShortField(env, lpObject, lpCache->light4_blue);
	lpStruct->dark[0].pixel = (*env)->GetIntField(env, lpObject, lpCache->dark0_pixel);
	lpStruct->dark[0].red = (*env)->GetShortField(env, lpObject, lpCache->dark0_red);
	lpStruct->dark[0].green = (*env)->GetShortField(env, lpObject, lpCache->dark0_green);
	lpStruct->dark[0].blue = (*env)->GetShortField(env, lpObject, lpCache->dark0_blue);
	lpStruct->dark[1].pixel = (*env)->GetIntField(env, lpObject, lpCache->dark1_pixel);
	lpStruct->dark[1].red = (*env)->GetShortField(env, lpObject, lpCache->dark1_red);
	lpStruct->dark[1].green = (*env)->GetShortField(env, lpObject, lpCache->dark1_green);
	lpStruct->dark[1].blue = (*env)->GetShortField(env, lpObject, lpCache->dark1_blue);
	lpStruct->dark[2].pixel = (*env)->GetIntField(env, lpObject, lpCache->dark2_pixel);
	lpStruct->dark[2].red = (*env)->GetShortField(env, lpObject, lpCache->dark2_red);
	lpStruct->dark[2].green = (*env)->GetShortField(env, lpObject, lpCache->dark2_green);
	lpStruct->dark[2].blue = (*env)->GetShortField(env, lpObject, lpCache->dark2_blue);
	lpStruct->dark[3].pixel = (*env)->GetIntField(env, lpObject, lpCache->dark3_pixel);
	lpStruct->dark[3].red = (*env)->GetShortField(env, lpObject, lpCache->dark3_red);
	lpStruct->dark[3].green = (*env)->GetShortField(env, lpObject, lpCache->dark3_green);
	lpStruct->dark[3].blue = (*env)->GetShortField(env, lpObject, lpCache->dark3_blue);
	lpStruct->dark[4].pixel = (*env)->GetIntField(env, lpObject, lpCache->dark4_pixel);
	lpStruct->dark[4].red = (*env)->GetShortField(env, lpObject, lpCache->dark4_red);
	lpStruct->dark[4].green = (*env)->GetShortField(env, lpObject, lpCache->dark4_green);
	lpStruct->dark[4].blue = (*env)->GetShortField(env, lpObject, lpCache->dark4_blue);
	lpStruct->mid[0].pixel = (*env)->GetIntField(env, lpObject, lpCache->mid0_pixel);
	lpStruct->mid[0].red = (*env)->GetShortField(env, lpObject, lpCache->mid0_red);
	lpStruct->mid[0].green = (*env)->GetShortField(env, lpObject, lpCache->mid0_green);
	lpStruct->mid[0].blue = (*env)->GetShortField(env, lpObject, lpCache->mid0_blue);
	lpStruct->mid[1].pixel = (*env)->GetIntField(env, lpObject, lpCache->mid1_pixel);
	lpStruct->mid[1].red = (*env)->GetShortField(env, lpObject, lpCache->mid1_red);
	lpStruct->mid[1].green = (*env)->GetShortField(env, lpObject, lpCache->mid1_green);
	lpStruct->mid[1].blue = (*env)->GetShortField(env, lpObject, lpCache->mid1_blue);
	lpStruct->mid[2].pixel = (*env)->GetIntField(env, lpObject, lpCache->mid2_pixel);
	lpStruct->mid[2].red = (*env)->GetShortField(env, lpObject, lpCache->mid2_red);
	lpStruct->mid[2].green = (*env)->GetShortField(env, lpObject, lpCache->mid2_green);
	lpStruct->mid[2].blue = (*env)->GetShortField(env, lpObject, lpCache->mid2_blue);
	lpStruct->mid[3].pixel = (*env)->GetIntField(env, lpObject, lpCache->mid3_pixel);
	lpStruct->mid[3].red = (*env)->GetShortField(env, lpObject, lpCache->mid3_red);
	lpStruct->mid[3].green = (*env)->GetShortField(env, lpObject, lpCache->mid3_green);
	lpStruct->mid[3].blue = (*env)->GetShortField(env, lpObject, lpCache->mid3_blue);
	lpStruct->mid[4].pixel = (*env)->GetIntField(env, lpObject, lpCache->mid4_pixel);
	lpStruct->mid[4].red = (*env)->GetShortField(env, lpObject, lpCache->mid4_red);
	lpStruct->mid[4].green = (*env)->GetShortField(env, lpObject, lpCache->mid4_green);
	lpStruct->mid[4].blue = (*env)->GetShortField(env, lpObject, lpCache->mid4_blue);
	lpStruct->text[0].pixel = (*env)->GetIntField(env, lpObject, lpCache->text0_pixel);
	lpStruct->text[0].red = (*env)->GetShortField(env, lpObject, lpCache->text0_red);
	lpStruct->text[0].green = (*env)->GetShortField(env, lpObject, lpCache->text0_green);
	lpStruct->text[0].blue = (*env)->GetShortField(env, lpObject, lpCache->text0_blue);
	lpStruct->text[1].pixel = (*env)->GetIntField(env, lpObject, lpCache->text1_pixel);
	lpStruct->text[1].red = (*env)->GetShortField(env, lpObject, lpCache->text1_red);
	lpStruct->text[1].green = (*env)->GetShortField(env, lpObject, lpCache->text1_green);
	lpStruct->text[1].blue = (*env)->GetShortField(env, lpObject, lpCache->text1_blue);
	lpStruct->text[2].pixel = (*env)->GetIntField(env, lpObject, lpCache->text2_pixel);
	lpStruct->text[2].red = (*env)->GetShortField(env, lpObject, lpCache->text2_red);
	lpStruct->text[2].green = (*env)->GetShortField(env, lpObject, lpCache->text2_green);
	lpStruct->text[2].blue = (*env)->GetShortField(env, lpObject, lpCache->text2_blue);
	lpStruct->text[3].pixel = (*env)->GetIntField(env, lpObject, lpCache->text3_pixel);
	lpStruct->text[3].red = (*env)->GetShortField(env, lpObject, lpCache->text3_red);
	lpStruct->text[3].green = (*env)->GetShortField(env, lpObject, lpCache->text3_green);
	lpStruct->text[3].blue = (*env)->GetShortField(env, lpObject, lpCache->text3_blue);
	lpStruct->text[4].pixel = (*env)->GetIntField(env, lpObject, lpCache->text4_pixel);
	lpStruct->text[4].red = (*env)->GetShortField(env, lpObject, lpCache->text4_red);
	lpStruct->text[4].green = (*env)->GetShortField(env, lpObject, lpCache->text4_green);
	lpStruct->text[4].blue = (*env)->GetShortField(env, lpObject, lpCache->text4_blue);
	lpStruct->base[0].pixel = (*env)->GetIntField(env, lpObject, lpCache->base0_pixel);
	lpStruct->base[0].red = (*env)->GetShortField(env, lpObject, lpCache->base0_red);
	lpStruct->base[0].green = (*env)->GetShortField(env, lpObject, lpCache->base0_green);
	lpStruct->base[0].blue = (*env)->GetShortField(env, lpObject, lpCache->base0_blue);
	lpStruct->base[1].pixel = (*env)->GetIntField(env, lpObject, lpCache->base1_pixel);
	lpStruct->base[1].red = (*env)->GetShortField(env, lpObject, lpCache->base1_red);
	lpStruct->base[1].green = (*env)->GetShortField(env, lpObject, lpCache->base1_green);
	lpStruct->base[1].blue = (*env)->GetShortField(env, lpObject, lpCache->base1_blue);
	lpStruct->base[2].pixel = (*env)->GetIntField(env, lpObject, lpCache->base2_pixel);
	lpStruct->base[2].red = (*env)->GetShortField(env, lpObject, lpCache->base2_red);
	lpStruct->base[2].green = (*env)->GetShortField(env, lpObject, lpCache->base2_green);
	lpStruct->base[2].blue = (*env)->GetShortField(env, lpObject, lpCache->base2_blue);
	lpStruct->base[3].pixel = (*env)->GetIntField(env, lpObject, lpCache->base3_pixel);
	lpStruct->base[3].red = (*env)->GetShortField(env, lpObject, lpCache->base3_red);
	lpStruct->base[3].green = (*env)->GetShortField(env, lpObject, lpCache->base3_green);
	lpStruct->base[3].blue = (*env)->GetShortField(env, lpObject, lpCache->base3_blue);
	lpStruct->base[4].pixel = (*env)->GetIntField(env, lpObject, lpCache->base4_pixel);
	lpStruct->base[4].red = (*env)->GetShortField(env, lpObject, lpCache->base4_red);
	lpStruct->base[4].green = (*env)->GetShortField(env, lpObject, lpCache->base4_green);
	lpStruct->base[4].blue = (*env)->GetShortField(env, lpObject, lpCache->base4_blue);
	lpStruct->black.pixel = (*env)->GetIntField(env, lpObject, lpCache->black_pixel);
	lpStruct->black.red = (*env)->GetShortField(env, lpObject, lpCache->black_red);
	lpStruct->black.green = (*env)->GetShortField(env, lpObject, lpCache->black_green);
	lpStruct->black.blue = (*env)->GetShortField(env, lpObject, lpCache->black_blue);
	lpStruct->white.pixel = (*env)->GetIntField(env, lpObject, lpCache->white_pixel);
	lpStruct->white.red = (*env)->GetShortField(env, lpObject, lpCache->white_red);
	lpStruct->white.green = (*env)->GetShortField(env, lpObject, lpCache->white_green);
	lpStruct->white.blue = (*env)->GetShortField(env, lpObject, lpCache->white_blue);
	lpStruct->font_desc = (PangoFontDescription *)(*env)->GetIntField(env, lpObject, lpCache->font_desc);
	lpStruct->xthickness = (*env)->GetIntField(env, lpObject, lpCache->xthickness);
	lpStruct->ythickness = (*env)->GetIntField(env, lpObject, lpCache->ythickness);
	lpStruct->fg_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->fg_gc0);
	lpStruct->fg_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->fg_gc1);
	lpStruct->fg_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->fg_gc2);
	lpStruct->fg_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->fg_gc3);
	lpStruct->fg_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->fg_gc4);
	lpStruct->bg_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->bg_gc0);
	lpStruct->bg_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->bg_gc1);
	lpStruct->bg_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->bg_gc2);
	lpStruct->bg_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->bg_gc3);
	lpStruct->bg_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->bg_gc4);
	lpStruct->light_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->light_gc0);
	lpStruct->light_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->light_gc1);
	lpStruct->light_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->light_gc2);
	lpStruct->light_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->light_gc3);
	lpStruct->light_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->light_gc4);
	lpStruct->dark_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->dark_gc0);
	lpStruct->dark_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->dark_gc1);
	lpStruct->dark_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->dark_gc2);
	lpStruct->dark_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->dark_gc3);
	lpStruct->dark_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->dark_gc4);
	lpStruct->mid_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->mid_gc0);
	lpStruct->mid_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->mid_gc1);
	lpStruct->mid_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->mid_gc2);
	lpStruct->mid_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->mid_gc3);
	lpStruct->mid_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->mid_gc4);
	lpStruct->text_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->text_gc0);
	lpStruct->text_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->text_gc1);
	lpStruct->text_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->text_gc2);
	lpStruct->text_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->text_gc3);
	lpStruct->text_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->text_gc4);
	lpStruct->base_gc[0] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->base_gc0);
	lpStruct->base_gc[1] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->base_gc1);
	lpStruct->base_gc[2] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->base_gc2);
	lpStruct->base_gc[3] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->base_gc3);
	lpStruct->base_gc[4] = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->base_gc4);
	lpStruct->black_gc = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->black_gc);
	lpStruct->white_gc = (GdkGC *)(*env)->GetIntField(env, lpObject, lpCache->white_gc);
	lpStruct->bg_pixmap[0] = (GdkPixmap *)(*env)->GetIntField(env, lpObject, lpCache->bg_pixmap0);
	lpStruct->bg_pixmap[1] = (GdkPixmap *)(*env)->GetIntField(env, lpObject, lpCache->bg_pixmap1);
	lpStruct->bg_pixmap[2] = (GdkPixmap *)(*env)->GetIntField(env, lpObject, lpCache->bg_pixmap2);
	lpStruct->bg_pixmap[3] = (GdkPixmap *)(*env)->GetIntField(env, lpObject, lpCache->bg_pixmap3);
	lpStruct->bg_pixmap[4] = (GdkPixmap *)(*env)->GetIntField(env, lpObject, lpCache->bg_pixmap4);
	lpStruct->bg_pixmap[5] = (GdkPixmap *)(*env)->GetIntField(env, lpObject, lpCache->bg_pixmap5);
	return lpStruct;
}

void setGtkStyleFields(JNIEnv *env, jobject lpObject, GtkStyle *lpStruct)
{
	PGtkStyle_FID_CACHE lpCache = &GtkStyleFc;
	if (!lpCache->cached) cacheGtkStyleFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->fg0_pixel, (jint)lpStruct->fg[0].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->fg0_red, (jshort)lpStruct->fg[0].red);
	(*env)->SetShortField(env, lpObject, lpCache->fg0_green, (jshort)lpStruct->fg[0].green);
	(*env)->SetShortField(env, lpObject, lpCache->fg0_blue, (jshort)lpStruct->fg[0].blue);
	(*env)->SetIntField(env, lpObject, lpCache->fg1_pixel, (jint)lpStruct->fg[1].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->fg1_red, (jshort)lpStruct->fg[1].red);
	(*env)->SetShortField(env, lpObject, lpCache->fg1_green, (jshort)lpStruct->fg[1].green);
	(*env)->SetShortField(env, lpObject, lpCache->fg1_blue, (jshort)lpStruct->fg[1].blue);
	(*env)->SetIntField(env, lpObject, lpCache->fg2_pixel, (jint)lpStruct->fg[2].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->fg2_red, (jshort)lpStruct->fg[2].red);
	(*env)->SetShortField(env, lpObject, lpCache->fg2_green, (jshort)lpStruct->fg[2].green);
	(*env)->SetShortField(env, lpObject, lpCache->fg2_blue, (jshort)lpStruct->fg[2].blue);
	(*env)->SetIntField(env, lpObject, lpCache->fg3_pixel, (jint)lpStruct->fg[3].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->fg3_red, (jshort)lpStruct->fg[3].red);
	(*env)->SetShortField(env, lpObject, lpCache->fg3_green, (jshort)lpStruct->fg[3].green);
	(*env)->SetShortField(env, lpObject, lpCache->fg3_blue, (jshort)lpStruct->fg[3].blue);
	(*env)->SetIntField(env, lpObject, lpCache->fg4_pixel, (jint)lpStruct->fg[4].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->fg4_red, (jshort)lpStruct->fg[4].red);
	(*env)->SetShortField(env, lpObject, lpCache->fg4_green, (jshort)lpStruct->fg[4].green);
	(*env)->SetShortField(env, lpObject, lpCache->fg4_blue, (jshort)lpStruct->fg[4].blue);
	(*env)->SetIntField(env, lpObject, lpCache->bg0_pixel, (jint)lpStruct->bg[0].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->bg0_red, (jshort)lpStruct->bg[0].red);
	(*env)->SetShortField(env, lpObject, lpCache->bg0_green, (jshort)lpStruct->bg[0].green);
	(*env)->SetShortField(env, lpObject, lpCache->bg0_blue, (jshort)lpStruct->bg[0].blue);
	(*env)->SetIntField(env, lpObject, lpCache->bg1_pixel, (jint)lpStruct->bg[1].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->bg1_red, (jshort)lpStruct->bg[1].red);
	(*env)->SetShortField(env, lpObject, lpCache->bg1_green, (jshort)lpStruct->bg[1].green);
	(*env)->SetShortField(env, lpObject, lpCache->bg1_blue, (jshort)lpStruct->bg[1].blue);
	(*env)->SetIntField(env, lpObject, lpCache->bg2_pixel, (jint)lpStruct->bg[2].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->bg2_red, (jshort)lpStruct->bg[2].red);
	(*env)->SetShortField(env, lpObject, lpCache->bg2_green, (jshort)lpStruct->bg[2].green);
	(*env)->SetShortField(env, lpObject, lpCache->bg2_blue, (jshort)lpStruct->bg[2].blue);
	(*env)->SetIntField(env, lpObject, lpCache->bg3_pixel, (jint)lpStruct->bg[3].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->bg3_red, (jshort)lpStruct->bg[3].red);
	(*env)->SetShortField(env, lpObject, lpCache->bg3_green, (jshort)lpStruct->bg[3].green);
	(*env)->SetShortField(env, lpObject, lpCache->bg3_blue, (jshort)lpStruct->bg[3].blue);
	(*env)->SetIntField(env, lpObject, lpCache->bg4_pixel, (jint)lpStruct->bg[4].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->bg4_red, (jshort)lpStruct->bg[4].red);
	(*env)->SetShortField(env, lpObject, lpCache->bg4_green, (jshort)lpStruct->bg[4].green);
	(*env)->SetShortField(env, lpObject, lpCache->bg4_blue, (jshort)lpStruct->bg[4].blue);
	(*env)->SetIntField(env, lpObject, lpCache->light0_pixel, (jint)lpStruct->light[0].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->light0_red, (jshort)lpStruct->light[0].red);
	(*env)->SetShortField(env, lpObject, lpCache->light0_green, (jshort)lpStruct->light[0].green);
	(*env)->SetShortField(env, lpObject, lpCache->light0_blue, (jshort)lpStruct->light[0].blue);
	(*env)->SetIntField(env, lpObject, lpCache->light1_pixel, (jint)lpStruct->light[1].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->light1_red, (jshort)lpStruct->light[1].red);
	(*env)->SetShortField(env, lpObject, lpCache->light1_green, (jshort)lpStruct->light[1].green);
	(*env)->SetShortField(env, lpObject, lpCache->light1_blue, (jshort)lpStruct->light[1].blue);
	(*env)->SetIntField(env, lpObject, lpCache->light2_pixel, (jint)lpStruct->light[2].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->light2_red, (jshort)lpStruct->light[2].red);
	(*env)->SetShortField(env, lpObject, lpCache->light2_green, (jshort)lpStruct->light[2].green);
	(*env)->SetShortField(env, lpObject, lpCache->light2_blue, (jshort)lpStruct->light[2].blue);
	(*env)->SetIntField(env, lpObject, lpCache->light3_pixel, (jint)lpStruct->light[3].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->light3_red, (jshort)lpStruct->light[3].red);
	(*env)->SetShortField(env, lpObject, lpCache->light3_green, (jshort)lpStruct->light[3].green);
	(*env)->SetShortField(env, lpObject, lpCache->light3_blue, (jshort)lpStruct->light[3].blue);
	(*env)->SetIntField(env, lpObject, lpCache->light4_pixel, (jint)lpStruct->light[4].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->light4_red, (jshort)lpStruct->light[4].red);
	(*env)->SetShortField(env, lpObject, lpCache->light4_green, (jshort)lpStruct->light[4].green);
	(*env)->SetShortField(env, lpObject, lpCache->light4_blue, (jshort)lpStruct->light[4].blue);
	(*env)->SetIntField(env, lpObject, lpCache->dark0_pixel, (jint)lpStruct->dark[0].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->dark0_red, (jshort)lpStruct->dark[0].red);
	(*env)->SetShortField(env, lpObject, lpCache->dark0_green, (jshort)lpStruct->dark[0].green);
	(*env)->SetShortField(env, lpObject, lpCache->dark0_blue, (jshort)lpStruct->dark[0].blue);
	(*env)->SetIntField(env, lpObject, lpCache->dark1_pixel, (jint)lpStruct->dark[1].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->dark1_red, (jshort)lpStruct->dark[1].red);
	(*env)->SetShortField(env, lpObject, lpCache->dark1_green, (jshort)lpStruct->dark[1].green);
	(*env)->SetShortField(env, lpObject, lpCache->dark1_blue, (jshort)lpStruct->dark[1].blue);
	(*env)->SetIntField(env, lpObject, lpCache->dark2_pixel, (jint)lpStruct->dark[2].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->dark2_red, (jshort)lpStruct->dark[2].red);
	(*env)->SetShortField(env, lpObject, lpCache->dark2_green, (jshort)lpStruct->dark[2].green);
	(*env)->SetShortField(env, lpObject, lpCache->dark2_blue, (jshort)lpStruct->dark[2].blue);
	(*env)->SetIntField(env, lpObject, lpCache->dark3_pixel, (jint)lpStruct->dark[3].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->dark3_red, (jshort)lpStruct->dark[3].red);
	(*env)->SetShortField(env, lpObject, lpCache->dark3_green, (jshort)lpStruct->dark[3].green);
	(*env)->SetShortField(env, lpObject, lpCache->dark3_blue, (jshort)lpStruct->dark[3].blue);
	(*env)->SetIntField(env, lpObject, lpCache->dark4_pixel, (jint)lpStruct->dark[4].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->dark4_red, (jshort)lpStruct->dark[4].red);
	(*env)->SetShortField(env, lpObject, lpCache->dark4_green, (jshort)lpStruct->dark[4].green);
	(*env)->SetShortField(env, lpObject, lpCache->dark4_blue, (jshort)lpStruct->dark[4].blue);
	(*env)->SetIntField(env, lpObject, lpCache->mid0_pixel, (jint)lpStruct->mid[0].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->mid0_red, (jshort)lpStruct->mid[0].red);
	(*env)->SetShortField(env, lpObject, lpCache->mid0_green, (jshort)lpStruct->mid[0].green);
	(*env)->SetShortField(env, lpObject, lpCache->mid0_blue, (jshort)lpStruct->mid[0].blue);
	(*env)->SetIntField(env, lpObject, lpCache->mid1_pixel, (jint)lpStruct->mid[1].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->mid1_red, (jshort)lpStruct->mid[1].red);
	(*env)->SetShortField(env, lpObject, lpCache->mid1_green, (jshort)lpStruct->mid[1].green);
	(*env)->SetShortField(env, lpObject, lpCache->mid1_blue, (jshort)lpStruct->mid[1].blue);
	(*env)->SetIntField(env, lpObject, lpCache->mid2_pixel, (jint)lpStruct->mid[2].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->mid2_red, (jshort)lpStruct->mid[2].red);
	(*env)->SetShortField(env, lpObject, lpCache->mid2_green, (jshort)lpStruct->mid[2].green);
	(*env)->SetShortField(env, lpObject, lpCache->mid2_blue, (jshort)lpStruct->mid[2].blue);
	(*env)->SetIntField(env, lpObject, lpCache->mid3_pixel, (jint)lpStruct->mid[3].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->mid3_red, (jshort)lpStruct->mid[3].red);
	(*env)->SetShortField(env, lpObject, lpCache->mid3_green, (jshort)lpStruct->mid[3].green);
	(*env)->SetShortField(env, lpObject, lpCache->mid3_blue, (jshort)lpStruct->mid[3].blue);
	(*env)->SetIntField(env, lpObject, lpCache->mid4_pixel, (jint)lpStruct->mid[4].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->mid4_red, (jshort)lpStruct->mid[4].red);
	(*env)->SetShortField(env, lpObject, lpCache->mid4_green, (jshort)lpStruct->mid[4].green);
	(*env)->SetShortField(env, lpObject, lpCache->mid4_blue, (jshort)lpStruct->mid[4].blue);
	(*env)->SetIntField(env, lpObject, lpCache->text0_pixel, (jint)lpStruct->text[0].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->text0_red, (jshort)lpStruct->text[0].red);
	(*env)->SetShortField(env, lpObject, lpCache->text0_green, (jshort)lpStruct->text[0].green);
	(*env)->SetShortField(env, lpObject, lpCache->text0_blue, (jshort)lpStruct->text[0].blue);
	(*env)->SetIntField(env, lpObject, lpCache->text1_pixel, (jint)lpStruct->text[1].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->text1_red, (jshort)lpStruct->text[1].red);
	(*env)->SetShortField(env, lpObject, lpCache->text1_green, (jshort)lpStruct->text[1].green);
	(*env)->SetShortField(env, lpObject, lpCache->text1_blue, (jshort)lpStruct->text[1].blue);
	(*env)->SetIntField(env, lpObject, lpCache->text2_pixel, (jint)lpStruct->text[2].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->text2_red, (jshort)lpStruct->text[2].red);
	(*env)->SetShortField(env, lpObject, lpCache->text2_green, (jshort)lpStruct->text[2].green);
	(*env)->SetShortField(env, lpObject, lpCache->text2_blue, (jshort)lpStruct->text[2].blue);
	(*env)->SetIntField(env, lpObject, lpCache->text3_pixel, (jint)lpStruct->text[3].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->text3_red, (jshort)lpStruct->text[3].red);
	(*env)->SetShortField(env, lpObject, lpCache->text3_green, (jshort)lpStruct->text[3].green);
	(*env)->SetShortField(env, lpObject, lpCache->text3_blue, (jshort)lpStruct->text[3].blue);
	(*env)->SetIntField(env, lpObject, lpCache->text4_pixel, (jint)lpStruct->text[4].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->text4_red, (jshort)lpStruct->text[4].red);
	(*env)->SetShortField(env, lpObject, lpCache->text4_green, (jshort)lpStruct->text[4].green);
	(*env)->SetShortField(env, lpObject, lpCache->text4_blue, (jshort)lpStruct->text[4].blue);
	(*env)->SetIntField(env, lpObject, lpCache->base0_pixel, (jint)lpStruct->base[0].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->base0_red, (jshort)lpStruct->base[0].red);
	(*env)->SetShortField(env, lpObject, lpCache->base0_green, (jshort)lpStruct->base[0].green);
	(*env)->SetShortField(env, lpObject, lpCache->base0_blue, (jshort)lpStruct->base[0].blue);
	(*env)->SetIntField(env, lpObject, lpCache->base1_pixel, (jint)lpStruct->base[1].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->base1_red, (jshort)lpStruct->base[1].red);
	(*env)->SetShortField(env, lpObject, lpCache->base1_green, (jshort)lpStruct->base[1].green);
	(*env)->SetShortField(env, lpObject, lpCache->base1_blue, (jshort)lpStruct->base[1].blue);
	(*env)->SetIntField(env, lpObject, lpCache->base2_pixel, (jint)lpStruct->base[2].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->base2_red, (jshort)lpStruct->base[2].red);
	(*env)->SetShortField(env, lpObject, lpCache->base2_green, (jshort)lpStruct->base[2].green);
	(*env)->SetShortField(env, lpObject, lpCache->base2_blue, (jshort)lpStruct->base[2].blue);
	(*env)->SetIntField(env, lpObject, lpCache->base3_pixel, (jint)lpStruct->base[3].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->base3_red, (jshort)lpStruct->base[3].red);
	(*env)->SetShortField(env, lpObject, lpCache->base3_green, (jshort)lpStruct->base[3].green);
	(*env)->SetShortField(env, lpObject, lpCache->base3_blue, (jshort)lpStruct->base[3].blue);
	(*env)->SetIntField(env, lpObject, lpCache->base4_pixel, (jint)lpStruct->base[4].pixel);
	(*env)->SetShortField(env, lpObject, lpCache->base4_red, (jshort)lpStruct->base[4].red);
	(*env)->SetShortField(env, lpObject, lpCache->base4_green, (jshort)lpStruct->base[4].green);
	(*env)->SetShortField(env, lpObject, lpCache->base4_blue, (jshort)lpStruct->base[4].blue);
	(*env)->SetIntField(env, lpObject, lpCache->black_pixel, (jint)lpStruct->black.pixel);
	(*env)->SetShortField(env, lpObject, lpCache->black_red, (jshort)lpStruct->black.red);
	(*env)->SetShortField(env, lpObject, lpCache->black_green, (jshort)lpStruct->black.green);
	(*env)->SetShortField(env, lpObject, lpCache->black_blue, (jshort)lpStruct->black.blue);
	(*env)->SetIntField(env, lpObject, lpCache->white_pixel, (jint)lpStruct->white.pixel);
	(*env)->SetShortField(env, lpObject, lpCache->white_red, (jshort)lpStruct->white.red);
	(*env)->SetShortField(env, lpObject, lpCache->white_green, (jshort)lpStruct->white.green);
	(*env)->SetShortField(env, lpObject, lpCache->white_blue, (jshort)lpStruct->white.blue);
	(*env)->SetIntField(env, lpObject, lpCache->font_desc, (jint)lpStruct->font_desc);
	(*env)->SetIntField(env, lpObject, lpCache->xthickness, (jint)lpStruct->xthickness);
	(*env)->SetIntField(env, lpObject, lpCache->ythickness, (jint)lpStruct->ythickness);
	(*env)->SetIntField(env, lpObject, lpCache->fg_gc0, (jint)lpStruct->fg_gc[0]);
	(*env)->SetIntField(env, lpObject, lpCache->fg_gc1, (jint)lpStruct->fg_gc[1]);
	(*env)->SetIntField(env, lpObject, lpCache->fg_gc2, (jint)lpStruct->fg_gc[2]);
	(*env)->SetIntField(env, lpObject, lpCache->fg_gc3, (jint)lpStruct->fg_gc[3]);
	(*env)->SetIntField(env, lpObject, lpCache->fg_gc4, (jint)lpStruct->fg_gc[4]);
	(*env)->SetIntField(env, lpObject, lpCache->bg_gc0, (jint)lpStruct->bg_gc[0]);
	(*env)->SetIntField(env, lpObject, lpCache->bg_gc1, (jint)lpStruct->bg_gc[1]);
	(*env)->SetIntField(env, lpObject, lpCache->bg_gc2, (jint)lpStruct->bg_gc[2]);
	(*env)->SetIntField(env, lpObject, lpCache->bg_gc3, (jint)lpStruct->bg_gc[3]);
	(*env)->SetIntField(env, lpObject, lpCache->bg_gc4, (jint)lpStruct->bg_gc[4]);
	(*env)->SetIntField(env, lpObject, lpCache->light_gc0, (jint)lpStruct->light_gc[0]);
	(*env)->SetIntField(env, lpObject, lpCache->light_gc1, (jint)lpStruct->light_gc[1]);
	(*env)->SetIntField(env, lpObject, lpCache->light_gc2, (jint)lpStruct->light_gc[2]);
	(*env)->SetIntField(env, lpObject, lpCache->light_gc3, (jint)lpStruct->light_gc[3]);
	(*env)->SetIntField(env, lpObject, lpCache->light_gc4, (jint)lpStruct->light_gc[4]);
	(*env)->SetIntField(env, lpObject, lpCache->dark_gc0, (jint)lpStruct->dark_gc[0]);
	(*env)->SetIntField(env, lpObject, lpCache->dark_gc1, (jint)lpStruct->dark_gc[1]);
	(*env)->SetIntField(env, lpObject, lpCache->dark_gc2, (jint)lpStruct->dark_gc[2]);
	(*env)->SetIntField(env, lpObject, lpCache->dark_gc3, (jint)lpStruct->dark_gc[3]);
	(*env)->SetIntField(env, lpObject, lpCache->dark_gc4, (jint)lpStruct->dark_gc[4]);
	(*env)->SetIntField(env, lpObject, lpCache->mid_gc0, (jint)lpStruct->mid_gc[0]);
	(*env)->SetIntField(env, lpObject, lpCache->mid_gc1, (jint)lpStruct->mid_gc[1]);
	(*env)->SetIntField(env, lpObject, lpCache->mid_gc2, (jint)lpStruct->mid_gc[2]);
	(*env)->SetIntField(env, lpObject, lpCache->mid_gc3, (jint)lpStruct->mid_gc[3]);
	(*env)->SetIntField(env, lpObject, lpCache->mid_gc4, (jint)lpStruct->mid_gc[4]);
	(*env)->SetIntField(env, lpObject, lpCache->text_gc0, (jint)lpStruct->text_gc[0]);
	(*env)->SetIntField(env, lpObject, lpCache->text_gc1, (jint)lpStruct->text_gc[1]);
	(*env)->SetIntField(env, lpObject, lpCache->text_gc2, (jint)lpStruct->text_gc[2]);
	(*env)->SetIntField(env, lpObject, lpCache->text_gc3, (jint)lpStruct->text_gc[3]);
	(*env)->SetIntField(env, lpObject, lpCache->text_gc4, (jint)lpStruct->text_gc[4]);
	(*env)->SetIntField(env, lpObject, lpCache->base_gc0, (jint)lpStruct->base_gc[0]);
	(*env)->SetIntField(env, lpObject, lpCache->base_gc1, (jint)lpStruct->base_gc[1]);
	(*env)->SetIntField(env, lpObject, lpCache->base_gc2, (jint)lpStruct->base_gc[2]);
	(*env)->SetIntField(env, lpObject, lpCache->base_gc3, (jint)lpStruct->base_gc[3]);
	(*env)->SetIntField(env, lpObject, lpCache->base_gc4, (jint)lpStruct->base_gc[4]);
	(*env)->SetIntField(env, lpObject, lpCache->black_gc, (jint)lpStruct->black_gc);
	(*env)->SetIntField(env, lpObject, lpCache->white_gc, (jint)lpStruct->white_gc);
	(*env)->SetIntField(env, lpObject, lpCache->bg_pixmap0, (jint)lpStruct->bg_pixmap[0]);
	(*env)->SetIntField(env, lpObject, lpCache->bg_pixmap1, (jint)lpStruct->bg_pixmap[1]);
	(*env)->SetIntField(env, lpObject, lpCache->bg_pixmap2, (jint)lpStruct->bg_pixmap[2]);
	(*env)->SetIntField(env, lpObject, lpCache->bg_pixmap3, (jint)lpStruct->bg_pixmap[3]);
	(*env)->SetIntField(env, lpObject, lpCache->bg_pixmap4, (jint)lpStruct->bg_pixmap[4]);
	(*env)->SetIntField(env, lpObject, lpCache->bg_pixmap5, (jint)lpStruct->bg_pixmap[5]);
}

typedef struct GtkTargetEntry_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID target, flags, info;
} GtkTargetEntry_FID_CACHE;
typedef GtkTargetEntry_FID_CACHE *PGtkTargetEntry_FID_CACHE;

GtkTargetEntry_FID_CACHE GtkTargetEntryFc;

void cacheGtkTargetEntryFids(JNIEnv *env, jobject lpObject, PGtkTargetEntry_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->target = (*env)->GetFieldID(env, lpCache->clazz, "target", "I");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "I");
	lpCache->info = (*env)->GetFieldID(env, lpCache->clazz, "info", "I");
	lpCache->cached = 1;
}

GtkTargetEntry *getGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct)
{
	PGtkTargetEntry_FID_CACHE lpCache = &GtkTargetEntryFc;
	if (!lpCache->cached) cacheGtkTargetEntryFids(env, lpObject, lpCache);
	lpStruct->target = (gchar *)(*env)->GetIntField(env, lpObject, lpCache->target);
	lpStruct->flags = (guint)(*env)->GetIntField(env, lpObject, lpCache->flags);
	lpStruct->info = (guint)(*env)->GetIntField(env, lpObject, lpCache->info);
	return lpStruct;
}

void setGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct)
{
	PGtkTargetEntry_FID_CACHE lpCache = &GtkTargetEntryFc;
	if (!lpCache->cached) cacheGtkTargetEntryFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->target, (jint)lpStruct->target);
	(*env)->SetIntField(env, lpObject, lpCache->flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, lpCache->info, (jint)lpStruct->info);
}

typedef struct GtkTargetPair_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID info, flags, target;
} GtkTargetPair_FID_CACHE;
typedef GtkTargetPair_FID_CACHE *PGtkTargetPair_FID_CACHE;

GtkTargetPair_FID_CACHE GtkTargetPairFc;

void cacheGtkTargetPairFids(JNIEnv *env, jobject lpObject, PGtkTargetPair_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->info = (*env)->GetFieldID(env, lpCache->clazz, "info", "I");
	lpCache->flags = (*env)->GetFieldID(env, lpCache->clazz, "flags", "I");
	lpCache->target = (*env)->GetFieldID(env, lpCache->clazz, "target", "I");
	lpCache->cached = 1;
}

GtkTargetPair *getGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct)
{
	PGtkTargetPair_FID_CACHE lpCache = &GtkTargetPairFc;
	if (!lpCache->cached) cacheGtkTargetPairFids(env, lpObject, lpCache);
	lpStruct->info = (guint)(*env)->GetIntField(env, lpObject, lpCache->info);
	lpStruct->flags = (guint)(*env)->GetIntField(env, lpObject, lpCache->flags);
	lpStruct->target = (GdkAtom)(*env)->GetIntField(env, lpObject, lpCache->target);
	return lpStruct;
}

void setGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct)
{
	PGtkTargetPair_FID_CACHE lpCache = &GtkTargetPairFc;
	if (!lpCache->cached) cacheGtkTargetPairFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->info, (jint)lpStruct->info);
	(*env)->SetIntField(env, lpObject, lpCache->flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, lpCache->target, (jint)lpStruct->target);
}

typedef struct GtkFixed_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID children;
} GtkFixed_FID_CACHE;
typedef GtkFixed_FID_CACHE *PGtkFixed_FID_CACHE;

GtkFixed_FID_CACHE GtkFixedFc;

void cacheGtkFixedFids(JNIEnv *env, jobject lpObject, PGtkFixed_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->children = (*env)->GetFieldID(env, lpCache->clazz, "children", "I");
	lpCache->cached = 1;
}

GtkFixed *getGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct)
{
	PGtkFixed_FID_CACHE lpCache = &GtkFixedFc;
	if (!lpCache->cached) cacheGtkFixedFids(env, lpObject, lpCache);
	lpStruct->children = (GList *)(*env)->GetIntField(env, lpObject, lpCache->children);
	return lpStruct;
}

void setGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct)
{
	PGtkFixed_FID_CACHE lpCache = &GtkFixedFc;
	if (!lpCache->cached) cacheGtkFixedFids(env, lpObject, lpCache);
	(*env)->SetIntField(env, lpObject, lpCache->children, (jint)lpStruct->children);
}

typedef struct GdkEventCrossing_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, subwindow, time, x, y, x_root, y_root, mode, detail, focus, state;
} GdkEventCrossing_FID_CACHE;
typedef GdkEventCrossing_FID_CACHE *PGdkEventCrossing_FID_CACHE;

GdkEventCrossing_FID_CACHE GdkEventCrossingFc;

void cacheGdkEventCrossingFids(JNIEnv *env, jobject lpObject, PGdkEventCrossing_FID_CACHE lpCache)
{
	if (lpCache->cached) return;
	cacheGdkEventFids(env, lpObject, &GdkEventFc);
	lpCache->clazz = (*env)->GetObjectClass(env, lpObject);
	lpCache->window = (*env)->GetFieldID(env, lpCache->clazz, "window", "I");
	lpCache->send_event = (*env)->GetFieldID(env, lpCache->clazz, "send_event", "B");
	lpCache->subwindow = (*env)->GetFieldID(env, lpCache->clazz, "subwindow", "I");
	lpCache->time = (*env)->GetFieldID(env, lpCache->clazz, "time", "I");
	lpCache->x = (*env)->GetFieldID(env, lpCache->clazz, "x", "D");
	lpCache->y = (*env)->GetFieldID(env, lpCache->clazz, "y", "D");
	lpCache->x_root = (*env)->GetFieldID(env, lpCache->clazz, "x_root", "D");
	lpCache->y_root = (*env)->GetFieldID(env, lpCache->clazz, "y_root", "D");
	lpCache->mode = (*env)->GetFieldID(env, lpCache->clazz, "mode", "I");
	lpCache->detail = (*env)->GetFieldID(env, lpCache->clazz, "detail", "I");
	lpCache->focus = (*env)->GetFieldID(env, lpCache->clazz, "focus", "Z");
	lpCache->state = (*env)->GetFieldID(env, lpCache->clazz, "state", "I");
	lpCache->cached = 1;
}

GdkEventCrossing *getGdkEventCrossingFields(JNIEnv *env, jobject lpObject, GdkEventCrossing *lpStruct)
{
	PGdkEventCrossing_FID_CACHE lpCache = &GdkEventCrossingFc;
	if (!lpCache->cached) cacheGdkEventCrossingFids(env, lpObject, lpCache);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, lpCache->window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, lpCache->send_event);
	lpStruct->subwindow = (GdkWindow *)(*env)->GetIntField(env, lpObject, lpCache->subwindow);
	lpStruct->time = (guint32)(*env)->GetIntField(env, lpObject, lpCache->time);
	lpStruct->x = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->x);
	lpStruct->y = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->y);
	lpStruct->x_root = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->x_root);
	lpStruct->y_root = (gdouble)(*env)->GetDoubleField(env, lpObject, lpCache->y_root);
	lpStruct->mode = (GdkCrossingMode)(*env)->GetIntField(env, lpObject, lpCache->mode);
	lpStruct->detail = (GdkNotifyType)(*env)->GetIntField(env, lpObject, lpCache->detail);
	lpStruct->focus = (gboolean)(*env)->GetBooleanField(env, lpObject, lpCache->focus);
	lpStruct->state = (guint)(*env)->GetIntField(env, lpObject, lpCache->state);
	return lpStruct;
}

void setGdkEventCrossingFields(JNIEnv *env, jobject lpObject, GdkEventCrossing *lpStruct)
{
	PGdkEventCrossing_FID_CACHE lpCache = &GdkEventCrossingFc;
	if (!lpCache->cached) cacheGdkEventCrossingFids(env, lpObject, lpCache);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, lpCache->window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, lpCache->send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, lpCache->subwindow, (jint)lpStruct->subwindow);
	(*env)->SetIntField(env, lpObject, lpCache->time, (jint)lpStruct->time);
	(*env)->SetDoubleField(env, lpObject, lpCache->x, (jdouble)lpStruct->x);
	(*env)->SetDoubleField(env, lpObject, lpCache->y, (jdouble)lpStruct->y);
	(*env)->SetDoubleField(env, lpObject, lpCache->x_root, (jdouble)lpStruct->x_root);
	(*env)->SetDoubleField(env, lpObject, lpCache->y_root, (jdouble)lpStruct->y_root);
	(*env)->SetIntField(env, lpObject, lpCache->mode, (jint)lpStruct->mode);
	(*env)->SetIntField(env, lpObject, lpCache->detail, (jint)lpStruct->detail);
	(*env)->SetBooleanField(env, lpObject, lpCache->focus, (jboolean)lpStruct->focus);
	(*env)->SetIntField(env, lpObject, lpCache->state, (jint)lpStruct->state);
}
