/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
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

#ifndef NO_GdkColor
typedef struct GdkColor_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID pixel, red, green, blue;
} GdkColor_FID_CACHE;

GdkColor_FID_CACHE GdkColorFc;

void cacheGdkColorFields(JNIEnv *env, jobject lpObject)
{
	if (GdkColorFc.cached) return;
	GdkColorFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkColorFc.pixel = (*env)->GetFieldID(env, GdkColorFc.clazz, "pixel", "I");
	GdkColorFc.red = (*env)->GetFieldID(env, GdkColorFc.clazz, "red", "S");
	GdkColorFc.green = (*env)->GetFieldID(env, GdkColorFc.clazz, "green", "S");
	GdkColorFc.blue = (*env)->GetFieldID(env, GdkColorFc.clazz, "blue", "S");
	GdkColorFc.cached = 1;
}

GdkColor *getGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct)
{
	if (!GdkColorFc.cached) cacheGdkColorFields(env, lpObject);
	lpStruct->pixel = (guint32)(*env)->GetIntField(env, lpObject, GdkColorFc.pixel);
	lpStruct->red = (guint16)(*env)->GetShortField(env, lpObject, GdkColorFc.red);
	lpStruct->green = (guint16)(*env)->GetShortField(env, lpObject, GdkColorFc.green);
	lpStruct->blue = (guint16)(*env)->GetShortField(env, lpObject, GdkColorFc.blue);
	return lpStruct;
}

void setGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct)
{
	if (!GdkColorFc.cached) cacheGdkColorFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkColorFc.pixel, (jint)lpStruct->pixel);
	(*env)->SetShortField(env, lpObject, GdkColorFc.red, (jshort)lpStruct->red);
	(*env)->SetShortField(env, lpObject, GdkColorFc.green, (jshort)lpStruct->green);
	(*env)->SetShortField(env, lpObject, GdkColorFc.blue, (jshort)lpStruct->blue);
}
#endif

#ifndef NO_GdkDragContext
typedef struct GdkDragContext_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID protocol, is_source, source_window, dest_window, targets, actions, suggested_action, action, start_time;
} GdkDragContext_FID_CACHE;

GdkDragContext_FID_CACHE GdkDragContextFc;

void cacheGdkDragContextFields(JNIEnv *env, jobject lpObject)
{
	if (GdkDragContextFc.cached) return;
	GdkDragContextFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkDragContextFc.protocol = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "protocol", "I");
	GdkDragContextFc.is_source = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "is_source", "Z");
	GdkDragContextFc.source_window = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "source_window", "I");
	GdkDragContextFc.dest_window = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "dest_window", "I");
	GdkDragContextFc.targets = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "targets", "I");
	GdkDragContextFc.actions = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "actions", "I");
	GdkDragContextFc.suggested_action = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "suggested_action", "I");
	GdkDragContextFc.action = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "action", "I");
	GdkDragContextFc.start_time = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "start_time", "I");
	GdkDragContextFc.cached = 1;
}

GdkDragContext *getGdkDragContextFields(JNIEnv *env, jobject lpObject, GdkDragContext *lpStruct)
{
	if (!GdkDragContextFc.cached) cacheGdkDragContextFields(env, lpObject);
	lpStruct->protocol = (GdkDragProtocol)(*env)->GetIntField(env, lpObject, GdkDragContextFc.protocol);
	lpStruct->is_source = (gboolean)(*env)->GetBooleanField(env, lpObject, GdkDragContextFc.is_source);
	lpStruct->source_window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkDragContextFc.source_window);
	lpStruct->dest_window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkDragContextFc.dest_window);
	lpStruct->targets = (GList *)(*env)->GetIntField(env, lpObject, GdkDragContextFc.targets);
	lpStruct->actions = (GdkDragAction)(*env)->GetIntField(env, lpObject, GdkDragContextFc.actions);
	lpStruct->suggested_action = (GdkDragAction)(*env)->GetIntField(env, lpObject, GdkDragContextFc.suggested_action);
	lpStruct->action = (GdkDragAction)(*env)->GetIntField(env, lpObject, GdkDragContextFc.action);
	lpStruct->start_time = (guint32)(*env)->GetIntField(env, lpObject, GdkDragContextFc.start_time);
	return lpStruct;
}

void setGdkDragContextFields(JNIEnv *env, jobject lpObject, GdkDragContext *lpStruct)
{
	if (!GdkDragContextFc.cached) cacheGdkDragContextFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.protocol, (jint)lpStruct->protocol);
	(*env)->SetBooleanField(env, lpObject, GdkDragContextFc.is_source, (jboolean)lpStruct->is_source);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.source_window, (jint)lpStruct->source_window);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.dest_window, (jint)lpStruct->dest_window);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.targets, (jint)lpStruct->targets);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.actions, (jint)lpStruct->actions);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.suggested_action, (jint)lpStruct->suggested_action);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.action, (jint)lpStruct->action);
	(*env)->SetIntField(env, lpObject, GdkDragContextFc.start_time, (jint)lpStruct->start_time);
}
#endif

#ifndef NO_GdkEvent
typedef struct GdkEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type;
} GdkEvent_FID_CACHE;

GdkEvent_FID_CACHE GdkEventFc;

void cacheGdkEventFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventFc.cached) return;
	GdkEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventFc.type = (*env)->GetFieldID(env, GdkEventFc.clazz, "type", "I");
	GdkEventFc.cached = 1;
}

GdkEvent *getGdkEventFields(JNIEnv *env, jobject lpObject, GdkEvent *lpStruct)
{
	if (!GdkEventFc.cached) cacheGdkEventFields(env, lpObject);
	lpStruct->type = (GdkEventType)(*env)->GetIntField(env, lpObject, GdkEventFc.type);
	return lpStruct;
}

void setGdkEventFields(JNIEnv *env, jobject lpObject, GdkEvent *lpStruct)
{
	if (!GdkEventFc.cached) cacheGdkEventFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkEventFc.type, (jint)lpStruct->type);
}
#endif

#ifndef NO_GdkEventButton
typedef struct GdkEventButton_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, time, x, y, axes, state, button, device, x_root, y_root;
} GdkEventButton_FID_CACHE;

GdkEventButton_FID_CACHE GdkEventButtonFc;

void cacheGdkEventButtonFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventButtonFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventButtonFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventButtonFc.window = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "window", "I");
	GdkEventButtonFc.send_event = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "send_event", "B");
	GdkEventButtonFc.time = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "time", "I");
	GdkEventButtonFc.x = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "x", "D");
	GdkEventButtonFc.y = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "y", "D");
	GdkEventButtonFc.axes = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "axes", "I");
	GdkEventButtonFc.state = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "state", "I");
	GdkEventButtonFc.button = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "button", "I");
	GdkEventButtonFc.device = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "device", "I");
	GdkEventButtonFc.x_root = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "x_root", "D");
	GdkEventButtonFc.y_root = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "y_root", "D");
	GdkEventButtonFc.cached = 1;
}

GdkEventButton *getGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct)
{
	if (!GdkEventButtonFc.cached) cacheGdkEventButtonFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventButtonFc.send_event);
	lpStruct->time = (guint32)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.time);
	lpStruct->x = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventButtonFc.x);
	lpStruct->y = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventButtonFc.y);
	lpStruct->axes = (gdouble *)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.axes);
	lpStruct->state = (guint)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.state);
	lpStruct->button = (guint)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.button);
	lpStruct->device = (GdkDevice *)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.device);
	lpStruct->x_root = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventButtonFc.x_root);
	lpStruct->y_root = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventButtonFc.y_root);
	return lpStruct;
}

void setGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct)
{
	if (!GdkEventButtonFc.cached) cacheGdkEventButtonFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventButtonFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.time, (jint)lpStruct->time);
	(*env)->SetDoubleField(env, lpObject, GdkEventButtonFc.x, (jdouble)lpStruct->x);
	(*env)->SetDoubleField(env, lpObject, GdkEventButtonFc.y, (jdouble)lpStruct->y);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.axes, (jint)lpStruct->axes);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.button, (jint)lpStruct->button);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.device, (jint)lpStruct->device);
	(*env)->SetDoubleField(env, lpObject, GdkEventButtonFc.x_root, (jdouble)lpStruct->x_root);
	(*env)->SetDoubleField(env, lpObject, GdkEventButtonFc.y_root, (jdouble)lpStruct->y_root);
}
#endif

#ifndef NO_GdkEventCrossing
typedef struct GdkEventCrossing_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, subwindow, time, x, y, x_root, y_root, mode, detail, focus, state;
} GdkEventCrossing_FID_CACHE;

GdkEventCrossing_FID_CACHE GdkEventCrossingFc;

void cacheGdkEventCrossingFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventCrossingFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventCrossingFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventCrossingFc.window = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "window", "I");
	GdkEventCrossingFc.send_event = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "send_event", "B");
	GdkEventCrossingFc.subwindow = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "subwindow", "I");
	GdkEventCrossingFc.time = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "time", "I");
	GdkEventCrossingFc.x = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "x", "D");
	GdkEventCrossingFc.y = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "y", "D");
	GdkEventCrossingFc.x_root = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "x_root", "D");
	GdkEventCrossingFc.y_root = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "y_root", "D");
	GdkEventCrossingFc.mode = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "mode", "I");
	GdkEventCrossingFc.detail = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "detail", "I");
	GdkEventCrossingFc.focus = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "focus", "Z");
	GdkEventCrossingFc.state = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "state", "I");
	GdkEventCrossingFc.cached = 1;
}

GdkEventCrossing *getGdkEventCrossingFields(JNIEnv *env, jobject lpObject, GdkEventCrossing *lpStruct)
{
	if (!GdkEventCrossingFc.cached) cacheGdkEventCrossingFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventCrossingFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventCrossingFc.send_event);
	lpStruct->subwindow = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventCrossingFc.subwindow);
	lpStruct->time = (*env)->GetIntField(env, lpObject, GdkEventCrossingFc.time);
	lpStruct->x = (*env)->GetDoubleField(env, lpObject, GdkEventCrossingFc.x);
	lpStruct->y = (*env)->GetDoubleField(env, lpObject, GdkEventCrossingFc.y);
	lpStruct->x_root = (*env)->GetDoubleField(env, lpObject, GdkEventCrossingFc.x_root);
	lpStruct->y_root = (*env)->GetDoubleField(env, lpObject, GdkEventCrossingFc.y_root);
	lpStruct->mode = (GdkCrossingMode)(*env)->GetIntField(env, lpObject, GdkEventCrossingFc.mode);
	lpStruct->detail = (GdkNotifyType)(*env)->GetIntField(env, lpObject, GdkEventCrossingFc.detail);
	lpStruct->focus = (gboolean)(*env)->GetBooleanField(env, lpObject, GdkEventCrossingFc.focus);
	lpStruct->state = (*env)->GetIntField(env, lpObject, GdkEventCrossingFc.state);
	return lpStruct;
}

void setGdkEventCrossingFields(JNIEnv *env, jobject lpObject, GdkEventCrossing *lpStruct)
{
	if (!GdkEventCrossingFc.cached) cacheGdkEventCrossingFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventCrossingFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventCrossingFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventCrossingFc.subwindow, (jint)lpStruct->subwindow);
	(*env)->SetIntField(env, lpObject, GdkEventCrossingFc.time, (jint)lpStruct->time);
	(*env)->SetDoubleField(env, lpObject, GdkEventCrossingFc.x, (jdouble)lpStruct->x);
	(*env)->SetDoubleField(env, lpObject, GdkEventCrossingFc.y, (jdouble)lpStruct->y);
	(*env)->SetDoubleField(env, lpObject, GdkEventCrossingFc.x_root, (jdouble)lpStruct->x_root);
	(*env)->SetDoubleField(env, lpObject, GdkEventCrossingFc.y_root, (jdouble)lpStruct->y_root);
	(*env)->SetIntField(env, lpObject, GdkEventCrossingFc.mode, (jint)lpStruct->mode);
	(*env)->SetIntField(env, lpObject, GdkEventCrossingFc.detail, (jint)lpStruct->detail);
	(*env)->SetBooleanField(env, lpObject, GdkEventCrossingFc.focus, (jboolean)lpStruct->focus);
	(*env)->SetIntField(env, lpObject, GdkEventCrossingFc.state, (jint)lpStruct->state);
}
#endif

#ifndef NO_GdkEventExpose
typedef struct GdkEventExpose_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, area_x, area_y, area_width, area_height, region, count;
} GdkEventExpose_FID_CACHE;

GdkEventExpose_FID_CACHE GdkEventExposeFc;

void cacheGdkEventExposeFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventExposeFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventExposeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventExposeFc.window = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "window", "I");
	GdkEventExposeFc.send_event = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "send_event", "B");
	GdkEventExposeFc.area_x = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "area_x", "I");
	GdkEventExposeFc.area_y = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "area_y", "I");
	GdkEventExposeFc.area_width = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "area_width", "I");
	GdkEventExposeFc.area_height = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "area_height", "I");
	GdkEventExposeFc.region = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "region", "I");
	GdkEventExposeFc.count = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "count", "I");
	GdkEventExposeFc.cached = 1;
}

GdkEventExpose *getGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct)
{
	if (!GdkEventExposeFc.cached) cacheGdkEventExposeFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventExposeFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventExposeFc.send_event);
	lpStruct->area.x = (*env)->GetIntField(env, lpObject, GdkEventExposeFc.area_x);
	lpStruct->area.y = (*env)->GetIntField(env, lpObject, GdkEventExposeFc.area_y);
	lpStruct->area.width = (*env)->GetIntField(env, lpObject, GdkEventExposeFc.area_width);
	lpStruct->area.height = (*env)->GetIntField(env, lpObject, GdkEventExposeFc.area_height);
	lpStruct->region = (GdkRegion *)(*env)->GetIntField(env, lpObject, GdkEventExposeFc.region);
	lpStruct->count = (gint)(*env)->GetIntField(env, lpObject, GdkEventExposeFc.count);
	return lpStruct;
}

void setGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct)
{
	if (!GdkEventExposeFc.cached) cacheGdkEventExposeFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventExposeFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.area_x, (jint)lpStruct->area.x);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.area_y, (jint)lpStruct->area.y);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.area_width, (jint)lpStruct->area.width);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.area_height, (jint)lpStruct->area.height);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.region, (jint)lpStruct->region);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.count, (jint)lpStruct->count);
}
#endif

#ifndef NO_GdkEventFocus
typedef struct GdkEventFocus_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, in;
} GdkEventFocus_FID_CACHE;

GdkEventFocus_FID_CACHE GdkEventFocusFc;

void cacheGdkEventFocusFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventFocusFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventFocusFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventFocusFc.window = (*env)->GetFieldID(env, GdkEventFocusFc.clazz, "window", "I");
	GdkEventFocusFc.send_event = (*env)->GetFieldID(env, GdkEventFocusFc.clazz, "send_event", "B");
	GdkEventFocusFc.in = (*env)->GetFieldID(env, GdkEventFocusFc.clazz, "in", "S");
	GdkEventFocusFc.cached = 1;
}

GdkEventFocus *getGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct)
{
	if (!GdkEventFocusFc.cached) cacheGdkEventFocusFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventFocusFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventFocusFc.send_event);
	lpStruct->in = (gint16)(*env)->GetShortField(env, lpObject, GdkEventFocusFc.in);
	return lpStruct;
}

void setGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct)
{
	if (!GdkEventFocusFc.cached) cacheGdkEventFocusFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventFocusFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventFocusFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetShortField(env, lpObject, GdkEventFocusFc.in, (jshort)lpStruct->in);
}
#endif

#ifndef NO_GdkEventKey
typedef struct GdkEventKey_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, time, state, keyval, length, string, hardware_keycode, group;
} GdkEventKey_FID_CACHE;

GdkEventKey_FID_CACHE GdkEventKeyFc;

void cacheGdkEventKeyFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventKeyFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventKeyFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventKeyFc.window = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "window", "I");
	GdkEventKeyFc.send_event = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "send_event", "B");
	GdkEventKeyFc.time = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "time", "I");
	GdkEventKeyFc.state = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "state", "I");
	GdkEventKeyFc.keyval = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "keyval", "I");
	GdkEventKeyFc.length = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "length", "I");
	GdkEventKeyFc.string = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "string", "I");
	GdkEventKeyFc.hardware_keycode = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "hardware_keycode", "S");
	GdkEventKeyFc.group = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "group", "B");
	GdkEventKeyFc.cached = 1;
}

GdkEventKey *getGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct)
{
	if (!GdkEventKeyFc.cached) cacheGdkEventKeyFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventKeyFc.send_event);
	lpStruct->time = (guint32)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.time);
	lpStruct->state = (guint)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.state);
	lpStruct->keyval = (guint)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.keyval);
	lpStruct->length = (gint)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.length);
	lpStruct->string = (gchar *)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.string);
	lpStruct->hardware_keycode = (guint16)(*env)->GetShortField(env, lpObject, GdkEventKeyFc.hardware_keycode);
	lpStruct->group = (guint8)(*env)->GetByteField(env, lpObject, GdkEventKeyFc.group);
	return lpStruct;
}

void setGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct)
{
	if (!GdkEventKeyFc.cached) cacheGdkEventKeyFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventKeyFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.keyval, (jint)lpStruct->keyval);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.length, (jint)lpStruct->length);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.string, (jint)lpStruct->string);
	(*env)->SetShortField(env, lpObject, GdkEventKeyFc.hardware_keycode, (jshort)lpStruct->hardware_keycode);
	(*env)->SetByteField(env, lpObject, GdkEventKeyFc.group, (jbyte)lpStruct->group);
}
#endif

#ifndef NO_GdkEventVisibility
typedef struct GdkEventVisibility_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, state;
} GdkEventVisibility_FID_CACHE;

GdkEventVisibility_FID_CACHE GdkEventVisibilityFc;

void cacheGdkEventVisibilityFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventVisibilityFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventVisibilityFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventVisibilityFc.window = (*env)->GetFieldID(env, GdkEventVisibilityFc.clazz, "window", "I");
	GdkEventVisibilityFc.send_event = (*env)->GetFieldID(env, GdkEventVisibilityFc.clazz, "send_event", "B");
	GdkEventVisibilityFc.state = (*env)->GetFieldID(env, GdkEventVisibilityFc.clazz, "state", "I");
	GdkEventVisibilityFc.cached = 1;
}

GdkEventVisibility *getGdkEventVisibilityFields(JNIEnv *env, jobject lpObject, GdkEventVisibility *lpStruct)
{
	if (!GdkEventVisibilityFc.cached) cacheGdkEventVisibilityFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventVisibilityFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventVisibilityFc.send_event);
	lpStruct->state = (GdkVisibilityState)(*env)->GetIntField(env, lpObject, GdkEventVisibilityFc.state);
	return lpStruct;
}

void setGdkEventVisibilityFields(JNIEnv *env, jobject lpObject, GdkEventVisibility *lpStruct)
{
	if (!GdkEventVisibilityFc.cached) cacheGdkEventVisibilityFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventVisibilityFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventVisibilityFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventVisibilityFc.state, (jint)lpStruct->state);
}
#endif

#ifndef NO_GdkEventWindowState
typedef struct GdkEventWindowState_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, changed_mask, new_window_state;
} GdkEventWindowState_FID_CACHE;

GdkEventWindowState_FID_CACHE GdkEventWindowStateFc;

void cacheGdkEventWindowStateFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventWindowStateFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventWindowStateFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventWindowStateFc.window = (*env)->GetFieldID(env, GdkEventWindowStateFc.clazz, "window", "I");
	GdkEventWindowStateFc.send_event = (*env)->GetFieldID(env, GdkEventWindowStateFc.clazz, "send_event", "B");
	GdkEventWindowStateFc.changed_mask = (*env)->GetFieldID(env, GdkEventWindowStateFc.clazz, "changed_mask", "I");
	GdkEventWindowStateFc.new_window_state = (*env)->GetFieldID(env, GdkEventWindowStateFc.clazz, "new_window_state", "I");
	GdkEventWindowStateFc.cached = 1;
}

GdkEventWindowState *getGdkEventWindowStateFields(JNIEnv *env, jobject lpObject, GdkEventWindowState *lpStruct)
{
	if (!GdkEventWindowStateFc.cached) cacheGdkEventWindowStateFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntField(env, lpObject, GdkEventWindowStateFc.window);
	lpStruct->send_event = (*env)->GetByteField(env, lpObject, GdkEventWindowStateFc.send_event);
	lpStruct->changed_mask = (*env)->GetIntField(env, lpObject, GdkEventWindowStateFc.changed_mask);
	lpStruct->new_window_state = (*env)->GetIntField(env, lpObject, GdkEventWindowStateFc.new_window_state);
	return lpStruct;
}

void setGdkEventWindowStateFields(JNIEnv *env, jobject lpObject, GdkEventWindowState *lpStruct)
{
	if (!GdkEventWindowStateFc.cached) cacheGdkEventWindowStateFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, GdkEventWindowStateFc.window, (jint)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventWindowStateFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventWindowStateFc.changed_mask, (jint)lpStruct->changed_mask);
	(*env)->SetIntField(env, lpObject, GdkEventWindowStateFc.new_window_state, (jint)lpStruct->new_window_state);
}
#endif

#ifndef NO_GdkGCValues
typedef struct GdkGCValues_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID foreground_pixel, foreground_red, foreground_green, foreground_blue, background_pixel, background_red, background_green, background_blue, font, function, fill, tile, stipple, clip_mask, subwindow_mode, ts_x_origin, ts_y_origin, clip_x_origin, clip_y_origin, graphics_exposures, line_width, line_style, cap_style, join_style;
} GdkGCValues_FID_CACHE;

GdkGCValues_FID_CACHE GdkGCValuesFc;

void cacheGdkGCValuesFields(JNIEnv *env, jobject lpObject)
{
	if (GdkGCValuesFc.cached) return;
	GdkGCValuesFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkGCValuesFc.foreground_pixel = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "foreground_pixel", "I");
	GdkGCValuesFc.foreground_red = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "foreground_red", "S");
	GdkGCValuesFc.foreground_green = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "foreground_green", "S");
	GdkGCValuesFc.foreground_blue = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "foreground_blue", "S");
	GdkGCValuesFc.background_pixel = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "background_pixel", "I");
	GdkGCValuesFc.background_red = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "background_red", "S");
	GdkGCValuesFc.background_green = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "background_green", "S");
	GdkGCValuesFc.background_blue = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "background_blue", "S");
	GdkGCValuesFc.font = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "font", "I");
	GdkGCValuesFc.function = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "function", "I");
	GdkGCValuesFc.fill = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "fill", "I");
	GdkGCValuesFc.tile = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "tile", "I");
	GdkGCValuesFc.stipple = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "stipple", "I");
	GdkGCValuesFc.clip_mask = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "clip_mask", "I");
	GdkGCValuesFc.subwindow_mode = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "subwindow_mode", "I");
	GdkGCValuesFc.ts_x_origin = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "ts_x_origin", "I");
	GdkGCValuesFc.ts_y_origin = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "ts_y_origin", "I");
	GdkGCValuesFc.clip_x_origin = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "clip_x_origin", "I");
	GdkGCValuesFc.clip_y_origin = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "clip_y_origin", "I");
	GdkGCValuesFc.graphics_exposures = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "graphics_exposures", "I");
	GdkGCValuesFc.line_width = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "line_width", "I");
	GdkGCValuesFc.line_style = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "line_style", "I");
	GdkGCValuesFc.cap_style = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "cap_style", "I");
	GdkGCValuesFc.join_style = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "join_style", "I");
	GdkGCValuesFc.cached = 1;
}

GdkGCValues *getGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpStruct)
{
	if (!GdkGCValuesFc.cached) cacheGdkGCValuesFields(env, lpObject);
	lpStruct->foreground.pixel = (guint32)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.foreground_pixel);
	lpStruct->foreground.red = (guint16)(*env)->GetShortField(env, lpObject, GdkGCValuesFc.foreground_red);
	lpStruct->foreground.green = (guint16)(*env)->GetShortField(env, lpObject, GdkGCValuesFc.foreground_green);
	lpStruct->foreground.blue = (guint16)(*env)->GetShortField(env, lpObject, GdkGCValuesFc.foreground_blue);
	lpStruct->background.pixel = (guint32)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.background_pixel);
	lpStruct->background.red = (guint16)(*env)->GetShortField(env, lpObject, GdkGCValuesFc.background_red);
	lpStruct->background.green = (guint16)(*env)->GetShortField(env, lpObject, GdkGCValuesFc.background_green);
	lpStruct->background.blue = (guint16)(*env)->GetShortField(env, lpObject, GdkGCValuesFc.background_blue);
	lpStruct->font = (GdkFont *)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.font);
	lpStruct->function = (GdkFunction)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.function);
	lpStruct->fill = (GdkFill)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.fill);
	lpStruct->tile = (GdkPixmap *)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.tile);
	lpStruct->stipple = (GdkPixmap *)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.stipple);
	lpStruct->clip_mask = (GdkPixmap *)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.clip_mask);
	lpStruct->subwindow_mode = (GdkSubwindowMode)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.subwindow_mode);
	lpStruct->ts_x_origin = (gint)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.ts_x_origin);
	lpStruct->ts_y_origin = (gint)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.ts_y_origin);
	lpStruct->clip_x_origin = (gint)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.clip_x_origin);
	lpStruct->clip_y_origin = (gint)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.clip_y_origin);
	lpStruct->graphics_exposures = (gint)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.graphics_exposures);
	lpStruct->line_width = (gint)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.line_width);
	lpStruct->line_style = (GdkLineStyle)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.line_style);
	lpStruct->cap_style = (GdkCapStyle)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.cap_style);
	lpStruct->join_style = (GdkJoinStyle)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.join_style);
	return lpStruct;
}

void setGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpStruct)
{
	if (!GdkGCValuesFc.cached) cacheGdkGCValuesFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.foreground_pixel, (jint)lpStruct->foreground.pixel);
	(*env)->SetShortField(env, lpObject, GdkGCValuesFc.foreground_red, (jshort)lpStruct->foreground.red);
	(*env)->SetShortField(env, lpObject, GdkGCValuesFc.foreground_green, (jshort)lpStruct->foreground.green);
	(*env)->SetShortField(env, lpObject, GdkGCValuesFc.foreground_blue, (jshort)lpStruct->foreground.blue);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.background_pixel, (jint)lpStruct->background.pixel);
	(*env)->SetShortField(env, lpObject, GdkGCValuesFc.background_red, (jshort)lpStruct->background.red);
	(*env)->SetShortField(env, lpObject, GdkGCValuesFc.background_green, (jshort)lpStruct->background.green);
	(*env)->SetShortField(env, lpObject, GdkGCValuesFc.background_blue, (jshort)lpStruct->background.blue);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.font, (jint)lpStruct->font);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.function, (jint)lpStruct->function);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.fill, (jint)lpStruct->fill);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.tile, (jint)lpStruct->tile);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.stipple, (jint)lpStruct->stipple);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.clip_mask, (jint)lpStruct->clip_mask);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.subwindow_mode, (jint)lpStruct->subwindow_mode);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.ts_x_origin, (jint)lpStruct->ts_x_origin);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.ts_y_origin, (jint)lpStruct->ts_y_origin);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.clip_x_origin, (jint)lpStruct->clip_x_origin);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.clip_y_origin, (jint)lpStruct->clip_y_origin);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.graphics_exposures, (jint)lpStruct->graphics_exposures);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.line_width, (jint)lpStruct->line_width);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.line_style, (jint)lpStruct->line_style);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.cap_style, (jint)lpStruct->cap_style);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.join_style, (jint)lpStruct->join_style);
}
#endif

#ifndef NO_GdkImage
typedef struct GdkImage_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, visual, byte_order, width, height, depth, bpp, bpl, bits_per_pixel, mem, colormap, windowing_data;
} GdkImage_FID_CACHE;

GdkImage_FID_CACHE GdkImageFc;

void cacheGdkImageFields(JNIEnv *env, jobject lpObject)
{
	if (GdkImageFc.cached) return;
	GdkImageFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkImageFc.type = (*env)->GetFieldID(env, GdkImageFc.clazz, "type", "I");
	GdkImageFc.visual = (*env)->GetFieldID(env, GdkImageFc.clazz, "visual", "I");
	GdkImageFc.byte_order = (*env)->GetFieldID(env, GdkImageFc.clazz, "byte_order", "I");
	GdkImageFc.width = (*env)->GetFieldID(env, GdkImageFc.clazz, "width", "I");
	GdkImageFc.height = (*env)->GetFieldID(env, GdkImageFc.clazz, "height", "I");
	GdkImageFc.depth = (*env)->GetFieldID(env, GdkImageFc.clazz, "depth", "S");
	GdkImageFc.bpp = (*env)->GetFieldID(env, GdkImageFc.clazz, "bpp", "S");
	GdkImageFc.bpl = (*env)->GetFieldID(env, GdkImageFc.clazz, "bpl", "S");
	GdkImageFc.bits_per_pixel = (*env)->GetFieldID(env, GdkImageFc.clazz, "bits_per_pixel", "S");
	GdkImageFc.mem = (*env)->GetFieldID(env, GdkImageFc.clazz, "mem", "I");
	GdkImageFc.colormap = (*env)->GetFieldID(env, GdkImageFc.clazz, "colormap", "I");
	GdkImageFc.windowing_data = (*env)->GetFieldID(env, GdkImageFc.clazz, "windowing_data", "I");
	GdkImageFc.cached = 1;
}

GdkImage *getGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct)
{
	if (!GdkImageFc.cached) cacheGdkImageFields(env, lpObject);
	lpStruct->type = (GdkImageType)(*env)->GetIntField(env, lpObject, GdkImageFc.type);
	lpStruct->visual = (GdkVisual *)(*env)->GetIntField(env, lpObject, GdkImageFc.visual);
	lpStruct->byte_order = (GdkByteOrder)(*env)->GetIntField(env, lpObject, GdkImageFc.byte_order);
	lpStruct->width = (gint)(*env)->GetIntField(env, lpObject, GdkImageFc.width);
	lpStruct->height = (gint)(*env)->GetIntField(env, lpObject, GdkImageFc.height);
	lpStruct->depth = (guint16)(*env)->GetShortField(env, lpObject, GdkImageFc.depth);
	lpStruct->bpp = (guint16)(*env)->GetShortField(env, lpObject, GdkImageFc.bpp);
	lpStruct->bpl = (guint16)(*env)->GetShortField(env, lpObject, GdkImageFc.bpl);
	lpStruct->bits_per_pixel = (guint16)(*env)->GetShortField(env, lpObject, GdkImageFc.bits_per_pixel);
	lpStruct->mem = (gpointer)(*env)->GetIntField(env, lpObject, GdkImageFc.mem);
	lpStruct->colormap = (GdkColormap *)(*env)->GetIntField(env, lpObject, GdkImageFc.colormap);
	lpStruct->windowing_data = (gpointer)(*env)->GetIntField(env, lpObject, GdkImageFc.windowing_data);
	return lpStruct;
}

void setGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct)
{
	if (!GdkImageFc.cached) cacheGdkImageFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkImageFc.type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, GdkImageFc.visual, (jint)lpStruct->visual);
	(*env)->SetIntField(env, lpObject, GdkImageFc.byte_order, (jint)lpStruct->byte_order);
	(*env)->SetIntField(env, lpObject, GdkImageFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, GdkImageFc.height, (jint)lpStruct->height);
	(*env)->SetShortField(env, lpObject, GdkImageFc.depth, (jshort)lpStruct->depth);
	(*env)->SetShortField(env, lpObject, GdkImageFc.bpp, (jshort)lpStruct->bpp);
	(*env)->SetShortField(env, lpObject, GdkImageFc.bpl, (jshort)lpStruct->bpl);
	(*env)->SetShortField(env, lpObject, GdkImageFc.bits_per_pixel, (jshort)lpStruct->bits_per_pixel);
	(*env)->SetIntField(env, lpObject, GdkImageFc.mem, (jint)lpStruct->mem);
	(*env)->SetIntField(env, lpObject, GdkImageFc.colormap, (jint)lpStruct->colormap);
	(*env)->SetIntField(env, lpObject, GdkImageFc.windowing_data, (jint)lpStruct->windowing_data);
}
#endif

#ifndef NO_GdkRectangle
typedef struct GdkRectangle_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height;
} GdkRectangle_FID_CACHE;

GdkRectangle_FID_CACHE GdkRectangleFc;

void cacheGdkRectangleFields(JNIEnv *env, jobject lpObject)
{
	if (GdkRectangleFc.cached) return;
	GdkRectangleFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkRectangleFc.x = (*env)->GetFieldID(env, GdkRectangleFc.clazz, "x", "I");
	GdkRectangleFc.y = (*env)->GetFieldID(env, GdkRectangleFc.clazz, "y", "I");
	GdkRectangleFc.width = (*env)->GetFieldID(env, GdkRectangleFc.clazz, "width", "I");
	GdkRectangleFc.height = (*env)->GetFieldID(env, GdkRectangleFc.clazz, "height", "I");
	GdkRectangleFc.cached = 1;
}

GdkRectangle *getGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpStruct)
{
	if (!GdkRectangleFc.cached) cacheGdkRectangleFields(env, lpObject);
	lpStruct->x = (gint)(*env)->GetIntField(env, lpObject, GdkRectangleFc.x);
	lpStruct->y = (gint)(*env)->GetIntField(env, lpObject, GdkRectangleFc.y);
	lpStruct->width = (gint)(*env)->GetIntField(env, lpObject, GdkRectangleFc.width);
	lpStruct->height = (gint)(*env)->GetIntField(env, lpObject, GdkRectangleFc.height);
	return lpStruct;
}

void setGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpStruct)
{
	if (!GdkRectangleFc.cached) cacheGdkRectangleFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkRectangleFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, GdkRectangleFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, GdkRectangleFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, GdkRectangleFc.height, (jint)lpStruct->height);
}
#endif

#ifndef NO_GdkVisual
typedef struct GdkVisual_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, depth, byte_order, colormap_size, bits_per_rgb, red_mask, red_shift, red_prec, green_mask, green_shift, green_prec, blue_mask, blue_shift, blue_prec;
} GdkVisual_FID_CACHE;

GdkVisual_FID_CACHE GdkVisualFc;

void cacheGdkVisualFields(JNIEnv *env, jobject lpObject)
{
	if (GdkVisualFc.cached) return;
	GdkVisualFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkVisualFc.type = (*env)->GetFieldID(env, GdkVisualFc.clazz, "type", "I");
	GdkVisualFc.depth = (*env)->GetFieldID(env, GdkVisualFc.clazz, "depth", "I");
	GdkVisualFc.byte_order = (*env)->GetFieldID(env, GdkVisualFc.clazz, "byte_order", "I");
	GdkVisualFc.colormap_size = (*env)->GetFieldID(env, GdkVisualFc.clazz, "colormap_size", "I");
	GdkVisualFc.bits_per_rgb = (*env)->GetFieldID(env, GdkVisualFc.clazz, "bits_per_rgb", "I");
	GdkVisualFc.red_mask = (*env)->GetFieldID(env, GdkVisualFc.clazz, "red_mask", "I");
	GdkVisualFc.red_shift = (*env)->GetFieldID(env, GdkVisualFc.clazz, "red_shift", "I");
	GdkVisualFc.red_prec = (*env)->GetFieldID(env, GdkVisualFc.clazz, "red_prec", "I");
	GdkVisualFc.green_mask = (*env)->GetFieldID(env, GdkVisualFc.clazz, "green_mask", "I");
	GdkVisualFc.green_shift = (*env)->GetFieldID(env, GdkVisualFc.clazz, "green_shift", "I");
	GdkVisualFc.green_prec = (*env)->GetFieldID(env, GdkVisualFc.clazz, "green_prec", "I");
	GdkVisualFc.blue_mask = (*env)->GetFieldID(env, GdkVisualFc.clazz, "blue_mask", "I");
	GdkVisualFc.blue_shift = (*env)->GetFieldID(env, GdkVisualFc.clazz, "blue_shift", "I");
	GdkVisualFc.blue_prec = (*env)->GetFieldID(env, GdkVisualFc.clazz, "blue_prec", "I");
	GdkVisualFc.cached = 1;
}

GdkVisual *getGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpStruct)
{
	if (!GdkVisualFc.cached) cacheGdkVisualFields(env, lpObject);
	lpStruct->type = (GdkVisualType)(*env)->GetIntField(env, lpObject, GdkVisualFc.type);
	lpStruct->depth = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.depth);
	lpStruct->byte_order = (GdkByteOrder)(*env)->GetIntField(env, lpObject, GdkVisualFc.byte_order);
	lpStruct->colormap_size = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.colormap_size);
	lpStruct->bits_per_rgb = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.bits_per_rgb);
	lpStruct->red_mask = (guint32)(*env)->GetIntField(env, lpObject, GdkVisualFc.red_mask);
	lpStruct->red_shift = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.red_shift);
	lpStruct->red_prec = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.red_prec);
	lpStruct->green_mask = (guint32)(*env)->GetIntField(env, lpObject, GdkVisualFc.green_mask);
	lpStruct->green_shift = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.green_shift);
	lpStruct->green_prec = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.green_prec);
	lpStruct->blue_mask = (guint32)(*env)->GetIntField(env, lpObject, GdkVisualFc.blue_mask);
	lpStruct->blue_shift = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.blue_shift);
	lpStruct->blue_prec = (gint)(*env)->GetIntField(env, lpObject, GdkVisualFc.blue_prec);
	return lpStruct;
}

void setGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpStruct)
{
	if (!GdkVisualFc.cached) cacheGdkVisualFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.depth, (jint)lpStruct->depth);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.byte_order, (jint)lpStruct->byte_order);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.colormap_size, (jint)lpStruct->colormap_size);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.bits_per_rgb, (jint)lpStruct->bits_per_rgb);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.red_mask, (jint)lpStruct->red_mask);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.red_shift, (jint)lpStruct->red_shift);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.red_prec, (jint)lpStruct->red_prec);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.green_mask, (jint)lpStruct->green_mask);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.green_shift, (jint)lpStruct->green_shift);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.green_prec, (jint)lpStruct->green_prec);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.blue_mask, (jint)lpStruct->blue_mask);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.blue_shift, (jint)lpStruct->blue_shift);
	(*env)->SetIntField(env, lpObject, GdkVisualFc.blue_prec, (jint)lpStruct->blue_prec);
}
#endif

#ifndef NO_GdkWindowAttr
typedef struct GdkWindowAttr_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID title, event_mask, x, y, width, height, wclass, visual, colormap, window_type, cursor, wmclass_name, wmclass_class, override_redirect;
} GdkWindowAttr_FID_CACHE;

GdkWindowAttr_FID_CACHE GdkWindowAttrFc;

void cacheGdkWindowAttrFields(JNIEnv *env, jobject lpObject)
{
	if (GdkWindowAttrFc.cached) return;
	GdkWindowAttrFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkWindowAttrFc.title = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "title", "I");
	GdkWindowAttrFc.event_mask = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "event_mask", "I");
	GdkWindowAttrFc.x = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "x", "I");
	GdkWindowAttrFc.y = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "y", "I");
	GdkWindowAttrFc.width = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "width", "I");
	GdkWindowAttrFc.height = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "height", "I");
	GdkWindowAttrFc.wclass = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "wclass", "I");
	GdkWindowAttrFc.visual = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "visual", "I");
	GdkWindowAttrFc.colormap = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "colormap", "I");
	GdkWindowAttrFc.window_type = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "window_type", "I");
	GdkWindowAttrFc.cursor = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "cursor", "I");
	GdkWindowAttrFc.wmclass_name = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "wmclass_name", "I");
	GdkWindowAttrFc.wmclass_class = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "wmclass_class", "I");
	GdkWindowAttrFc.override_redirect = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "override_redirect", "Z");
	GdkWindowAttrFc.cached = 1;
}

GdkWindowAttr *getGdkWindowAttrFields(JNIEnv *env, jobject lpObject, GdkWindowAttr *lpStruct)
{
	if (!GdkWindowAttrFc.cached) cacheGdkWindowAttrFields(env, lpObject);
	lpStruct->title = (gchar *)(*env)->GetIntField(env, lpObject, GdkWindowAttrFc.title);
	lpStruct->event_mask = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.event_mask);
	lpStruct->x = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.height);
	lpStruct->wclass = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.wclass);
	lpStruct->visual = (GdkVisual *)(*env)->GetIntField(env, lpObject, GdkWindowAttrFc.visual);
	lpStruct->colormap = (GdkColormap *)(*env)->GetIntField(env, lpObject, GdkWindowAttrFc.colormap);
	lpStruct->window_type = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.window_type);
	lpStruct->cursor = (GdkCursor *)(*env)->GetIntField(env, lpObject, GdkWindowAttrFc.cursor);
	lpStruct->wmclass_name = (gchar *)(*env)->GetIntField(env, lpObject, GdkWindowAttrFc.wmclass_name);
	lpStruct->wmclass_class = (gchar *)(*env)->GetIntField(env, lpObject, GdkWindowAttrFc.wmclass_class);
	lpStruct->override_redirect = (*env)->GetBooleanField(env, lpObject, GdkWindowAttrFc.override_redirect);
	return lpStruct;
}

void setGdkWindowAttrFields(JNIEnv *env, jobject lpObject, GdkWindowAttr *lpStruct)
{
	if (!GdkWindowAttrFc.cached) cacheGdkWindowAttrFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.title, (jint)lpStruct->title);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.event_mask, (jint)lpStruct->event_mask);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.height, (jint)lpStruct->height);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.wclass, (jint)lpStruct->wclass);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.visual, (jint)lpStruct->visual);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.colormap, (jint)lpStruct->colormap);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.window_type, (jint)lpStruct->window_type);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.cursor, (jint)lpStruct->cursor);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.wmclass_name, (jint)lpStruct->wmclass_name);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.wmclass_class, (jint)lpStruct->wmclass_class);
	(*env)->SetBooleanField(env, lpObject, GdkWindowAttrFc.override_redirect, (jboolean)lpStruct->override_redirect);
}
#endif

#ifndef NO_GtkAdjustment
typedef struct GtkAdjustment_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID lower, upper, value, step_increment, page_increment, page_size;
} GtkAdjustment_FID_CACHE;

GtkAdjustment_FID_CACHE GtkAdjustmentFc;

void cacheGtkAdjustmentFields(JNIEnv *env, jobject lpObject)
{
	if (GtkAdjustmentFc.cached) return;
	GtkAdjustmentFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkAdjustmentFc.lower = (*env)->GetFieldID(env, GtkAdjustmentFc.clazz, "lower", "D");
	GtkAdjustmentFc.upper = (*env)->GetFieldID(env, GtkAdjustmentFc.clazz, "upper", "D");
	GtkAdjustmentFc.value = (*env)->GetFieldID(env, GtkAdjustmentFc.clazz, "value", "D");
	GtkAdjustmentFc.step_increment = (*env)->GetFieldID(env, GtkAdjustmentFc.clazz, "step_increment", "D");
	GtkAdjustmentFc.page_increment = (*env)->GetFieldID(env, GtkAdjustmentFc.clazz, "page_increment", "D");
	GtkAdjustmentFc.page_size = (*env)->GetFieldID(env, GtkAdjustmentFc.clazz, "page_size", "D");
	GtkAdjustmentFc.cached = 1;
}

GtkAdjustment *getGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpStruct)
{
	if (!GtkAdjustmentFc.cached) cacheGtkAdjustmentFields(env, lpObject);
	lpStruct->lower = (gdouble)(*env)->GetDoubleField(env, lpObject, GtkAdjustmentFc.lower);
	lpStruct->upper = (gdouble)(*env)->GetDoubleField(env, lpObject, GtkAdjustmentFc.upper);
	lpStruct->value = (gdouble)(*env)->GetDoubleField(env, lpObject, GtkAdjustmentFc.value);
	lpStruct->step_increment = (gdouble)(*env)->GetDoubleField(env, lpObject, GtkAdjustmentFc.step_increment);
	lpStruct->page_increment = (gdouble)(*env)->GetDoubleField(env, lpObject, GtkAdjustmentFc.page_increment);
	lpStruct->page_size = (gdouble)(*env)->GetDoubleField(env, lpObject, GtkAdjustmentFc.page_size);
	return lpStruct;
}

void setGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpStruct)
{
	if (!GtkAdjustmentFc.cached) cacheGtkAdjustmentFields(env, lpObject);
	(*env)->SetDoubleField(env, lpObject, GtkAdjustmentFc.lower, (jdouble)lpStruct->lower);
	(*env)->SetDoubleField(env, lpObject, GtkAdjustmentFc.upper, (jdouble)lpStruct->upper);
	(*env)->SetDoubleField(env, lpObject, GtkAdjustmentFc.value, (jdouble)lpStruct->value);
	(*env)->SetDoubleField(env, lpObject, GtkAdjustmentFc.step_increment, (jdouble)lpStruct->step_increment);
	(*env)->SetDoubleField(env, lpObject, GtkAdjustmentFc.page_increment, (jdouble)lpStruct->page_increment);
	(*env)->SetDoubleField(env, lpObject, GtkAdjustmentFc.page_size, (jdouble)lpStruct->page_size);
}
#endif

#ifndef NO_GtkAllocation
typedef struct GtkAllocation_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height;
} GtkAllocation_FID_CACHE;

GtkAllocation_FID_CACHE GtkAllocationFc;

void cacheGtkAllocationFields(JNIEnv *env, jobject lpObject)
{
	if (GtkAllocationFc.cached) return;
	GtkAllocationFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkAllocationFc.x = (*env)->GetFieldID(env, GtkAllocationFc.clazz, "x", "I");
	GtkAllocationFc.y = (*env)->GetFieldID(env, GtkAllocationFc.clazz, "y", "I");
	GtkAllocationFc.width = (*env)->GetFieldID(env, GtkAllocationFc.clazz, "width", "I");
	GtkAllocationFc.height = (*env)->GetFieldID(env, GtkAllocationFc.clazz, "height", "I");
	GtkAllocationFc.cached = 1;
}

GtkAllocation *getGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpStruct)
{
	if (!GtkAllocationFc.cached) cacheGtkAllocationFields(env, lpObject);
	lpStruct->x = (*env)->GetIntField(env, lpObject, GtkAllocationFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, GtkAllocationFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, GtkAllocationFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, GtkAllocationFc.height);
	return lpStruct;
}

void setGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpStruct)
{
	if (!GtkAllocationFc.cached) cacheGtkAllocationFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkAllocationFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, GtkAllocationFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, GtkAllocationFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, GtkAllocationFc.height, (jint)lpStruct->height);
}
#endif

#ifndef NO_GtkColorSelectionDialog
typedef struct GtkColorSelectionDialog_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID colorsel, ok_button, cancel_button, help_button;
} GtkColorSelectionDialog_FID_CACHE;

GtkColorSelectionDialog_FID_CACHE GtkColorSelectionDialogFc;

void cacheGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject)
{
	if (GtkColorSelectionDialogFc.cached) return;
	GtkColorSelectionDialogFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkColorSelectionDialogFc.colorsel = (*env)->GetFieldID(env, GtkColorSelectionDialogFc.clazz, "colorsel", "I");
	GtkColorSelectionDialogFc.ok_button = (*env)->GetFieldID(env, GtkColorSelectionDialogFc.clazz, "ok_button", "I");
	GtkColorSelectionDialogFc.cancel_button = (*env)->GetFieldID(env, GtkColorSelectionDialogFc.clazz, "cancel_button", "I");
	GtkColorSelectionDialogFc.help_button = (*env)->GetFieldID(env, GtkColorSelectionDialogFc.clazz, "help_button", "I");
	GtkColorSelectionDialogFc.cached = 1;
}

GtkColorSelectionDialog *getGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct)
{
	if (!GtkColorSelectionDialogFc.cached) cacheGtkColorSelectionDialogFields(env, lpObject);
	lpStruct->colorsel = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkColorSelectionDialogFc.colorsel);
	lpStruct->ok_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkColorSelectionDialogFc.ok_button);
	lpStruct->cancel_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkColorSelectionDialogFc.cancel_button);
	lpStruct->help_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkColorSelectionDialogFc.help_button);
	return lpStruct;
}

void setGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct)
{
	if (!GtkColorSelectionDialogFc.cached) cacheGtkColorSelectionDialogFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkColorSelectionDialogFc.colorsel, (jint)lpStruct->colorsel);
	(*env)->SetIntField(env, lpObject, GtkColorSelectionDialogFc.ok_button, (jint)lpStruct->ok_button);
	(*env)->SetIntField(env, lpObject, GtkColorSelectionDialogFc.cancel_button, (jint)lpStruct->cancel_button);
	(*env)->SetIntField(env, lpObject, GtkColorSelectionDialogFc.help_button, (jint)lpStruct->help_button);
}
#endif

#ifndef NO_GtkCombo
typedef struct GtkCombo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID entry, list;
} GtkCombo_FID_CACHE;

GtkCombo_FID_CACHE GtkComboFc;

void cacheGtkComboFields(JNIEnv *env, jobject lpObject)
{
	if (GtkComboFc.cached) return;
	GtkComboFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkComboFc.entry = (*env)->GetFieldID(env, GtkComboFc.clazz, "entry", "I");
	GtkComboFc.list = (*env)->GetFieldID(env, GtkComboFc.clazz, "list", "I");
	GtkComboFc.cached = 1;
}

GtkCombo *getGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct)
{
	if (!GtkComboFc.cached) cacheGtkComboFields(env, lpObject);
	lpStruct->entry = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkComboFc.entry);
	lpStruct->list = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkComboFc.list);
	return lpStruct;
}

void setGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct)
{
	if (!GtkComboFc.cached) cacheGtkComboFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkComboFc.entry, (jint)lpStruct->entry);
	(*env)->SetIntField(env, lpObject, GtkComboFc.list, (jint)lpStruct->list);
}
#endif

#ifndef NO_GtkFileSelection
typedef struct GtkFileSelection_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID dir_list, file_list, selection_entry, selection_text, main_vbox, ok_button, cancel_button, help_button, history_pulldown, history_menu, history_list, fileop_dialog, fileop_entry, fileop_file, cmpl_state, fileop_c_dir, fileop_del_file, fileop_ren_file, button_area, action_area;
} GtkFileSelection_FID_CACHE;

GtkFileSelection_FID_CACHE GtkFileSelectionFc;

void cacheGtkFileSelectionFields(JNIEnv *env, jobject lpObject)
{
	if (GtkFileSelectionFc.cached) return;
	GtkFileSelectionFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkFileSelectionFc.dir_list = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "dir_list", "I");
	GtkFileSelectionFc.file_list = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "file_list", "I");
	GtkFileSelectionFc.selection_entry = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "selection_entry", "I");
	GtkFileSelectionFc.selection_text = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "selection_text", "I");
	GtkFileSelectionFc.main_vbox = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "main_vbox", "I");
	GtkFileSelectionFc.ok_button = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "ok_button", "I");
	GtkFileSelectionFc.cancel_button = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "cancel_button", "I");
	GtkFileSelectionFc.help_button = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "help_button", "I");
	GtkFileSelectionFc.history_pulldown = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "history_pulldown", "I");
	GtkFileSelectionFc.history_menu = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "history_menu", "I");
	GtkFileSelectionFc.history_list = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "history_list", "I");
	GtkFileSelectionFc.fileop_dialog = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_dialog", "I");
	GtkFileSelectionFc.fileop_entry = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_entry", "I");
	GtkFileSelectionFc.fileop_file = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_file", "I");
	GtkFileSelectionFc.cmpl_state = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "cmpl_state", "I");
	GtkFileSelectionFc.fileop_c_dir = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_c_dir", "I");
	GtkFileSelectionFc.fileop_del_file = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_del_file", "I");
	GtkFileSelectionFc.fileop_ren_file = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_ren_file", "I");
	GtkFileSelectionFc.button_area = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "button_area", "I");
	GtkFileSelectionFc.action_area = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "action_area", "I");
	GtkFileSelectionFc.cached = 1;
}

GtkFileSelection *getGtkFileSelectionFields(JNIEnv *env, jobject lpObject, GtkFileSelection *lpStruct)
{
	if (!GtkFileSelectionFc.cached) cacheGtkFileSelectionFields(env, lpObject);
	lpStruct->dir_list = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.dir_list);
	lpStruct->file_list = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.file_list);
	lpStruct->selection_entry = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.selection_entry);
	lpStruct->selection_text = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.selection_text);
	lpStruct->main_vbox = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.main_vbox);
	lpStruct->ok_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.ok_button);
	lpStruct->cancel_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.cancel_button);
	lpStruct->help_button = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.help_button);
	lpStruct->history_pulldown = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.history_pulldown);
	lpStruct->history_menu = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.history_menu);
	lpStruct->history_list = (GList *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.history_list);
	lpStruct->fileop_dialog = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.fileop_dialog);
	lpStruct->fileop_entry = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.fileop_entry);
	lpStruct->fileop_file = (gchar *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.fileop_file);
	lpStruct->cmpl_state = (gpointer)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.cmpl_state);
	lpStruct->fileop_c_dir = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.fileop_c_dir);
	lpStruct->fileop_del_file = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.fileop_del_file);
	lpStruct->fileop_ren_file = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.fileop_ren_file);
	lpStruct->button_area = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.button_area);
	lpStruct->action_area = (GtkWidget *)(*env)->GetIntField(env, lpObject, GtkFileSelectionFc.action_area);
	return lpStruct;
}

void setGtkFileSelectionFields(JNIEnv *env, jobject lpObject, GtkFileSelection *lpStruct)
{
	if (!GtkFileSelectionFc.cached) cacheGtkFileSelectionFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.dir_list, (jint)lpStruct->dir_list);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.file_list, (jint)lpStruct->file_list);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.selection_entry, (jint)lpStruct->selection_entry);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.selection_text, (jint)lpStruct->selection_text);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.main_vbox, (jint)lpStruct->main_vbox);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.ok_button, (jint)lpStruct->ok_button);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.cancel_button, (jint)lpStruct->cancel_button);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.help_button, (jint)lpStruct->help_button);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.history_pulldown, (jint)lpStruct->history_pulldown);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.history_menu, (jint)lpStruct->history_menu);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.history_list, (jint)lpStruct->history_list);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.fileop_dialog, (jint)lpStruct->fileop_dialog);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.fileop_entry, (jint)lpStruct->fileop_entry);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.fileop_file, (jint)lpStruct->fileop_file);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.cmpl_state, (jint)lpStruct->cmpl_state);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.fileop_c_dir, (jint)lpStruct->fileop_c_dir);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.fileop_del_file, (jint)lpStruct->fileop_del_file);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.fileop_ren_file, (jint)lpStruct->fileop_ren_file);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.button_area, (jint)lpStruct->button_area);
	(*env)->SetIntField(env, lpObject, GtkFileSelectionFc.action_area, (jint)lpStruct->action_area);
}
#endif

#ifndef NO_GtkFixed
typedef struct GtkFixed_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID children;
} GtkFixed_FID_CACHE;

GtkFixed_FID_CACHE GtkFixedFc;

void cacheGtkFixedFields(JNIEnv *env, jobject lpObject)
{
	if (GtkFixedFc.cached) return;
	GtkFixedFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkFixedFc.children = (*env)->GetFieldID(env, GtkFixedFc.clazz, "children", "I");
	GtkFixedFc.cached = 1;
}

GtkFixed *getGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct)
{
	if (!GtkFixedFc.cached) cacheGtkFixedFields(env, lpObject);
	lpStruct->children = (GList *)(*env)->GetIntField(env, lpObject, GtkFixedFc.children);
	return lpStruct;
}

void setGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct)
{
	if (!GtkFixedFc.cached) cacheGtkFixedFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkFixedFc.children, (jint)lpStruct->children);
}
#endif

#ifndef NO_GtkRequisition
typedef struct GtkRequisition_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID width, height;
} GtkRequisition_FID_CACHE;

GtkRequisition_FID_CACHE GtkRequisitionFc;

void cacheGtkRequisitionFields(JNIEnv *env, jobject lpObject)
{
	if (GtkRequisitionFc.cached) return;
	GtkRequisitionFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkRequisitionFc.width = (*env)->GetFieldID(env, GtkRequisitionFc.clazz, "width", "I");
	GtkRequisitionFc.height = (*env)->GetFieldID(env, GtkRequisitionFc.clazz, "height", "I");
	GtkRequisitionFc.cached = 1;
}

GtkRequisition *getGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpStruct)
{
	if (!GtkRequisitionFc.cached) cacheGtkRequisitionFields(env, lpObject);
	lpStruct->width = (*env)->GetIntField(env, lpObject, GtkRequisitionFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, GtkRequisitionFc.height);
	return lpStruct;
}

void setGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpStruct)
{
	if (!GtkRequisitionFc.cached) cacheGtkRequisitionFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkRequisitionFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, GtkRequisitionFc.height, (jint)lpStruct->height);
}
#endif

#ifndef NO_GtkSelectionData
typedef struct GtkSelectionData_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID selection, target, type, format, data, length;
} GtkSelectionData_FID_CACHE;

GtkSelectionData_FID_CACHE GtkSelectionDataFc;

void cacheGtkSelectionDataFields(JNIEnv *env, jobject lpObject)
{
	if (GtkSelectionDataFc.cached) return;
	GtkSelectionDataFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkSelectionDataFc.selection = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "selection", "I");
	GtkSelectionDataFc.target = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "target", "I");
	GtkSelectionDataFc.type = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "type", "I");
	GtkSelectionDataFc.format = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "format", "I");
	GtkSelectionDataFc.data = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "data", "I");
	GtkSelectionDataFc.length = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "length", "I");
	GtkSelectionDataFc.cached = 1;
}

GtkSelectionData *getGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct)
{
	if (!GtkSelectionDataFc.cached) cacheGtkSelectionDataFields(env, lpObject);
	lpStruct->selection = (GdkAtom)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.selection);
	lpStruct->target = (GdkAtom)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.target);
	lpStruct->type = (GdkAtom)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.type);
	lpStruct->format = (gint)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.format);
	lpStruct->data = (guchar *)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.data);
	lpStruct->length = (gint)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.length);
	return lpStruct;
}

void setGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct)
{
	if (!GtkSelectionDataFc.cached) cacheGtkSelectionDataFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.selection, (jint)lpStruct->selection);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.target, (jint)lpStruct->target);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.format, (jint)lpStruct->format);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.data, (jint)lpStruct->data);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.length, (jint)lpStruct->length);
}
#endif

#ifndef NO_GtkTargetEntry
typedef struct GtkTargetEntry_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID target, flags, info;
} GtkTargetEntry_FID_CACHE;

GtkTargetEntry_FID_CACHE GtkTargetEntryFc;

void cacheGtkTargetEntryFields(JNIEnv *env, jobject lpObject)
{
	if (GtkTargetEntryFc.cached) return;
	GtkTargetEntryFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkTargetEntryFc.target = (*env)->GetFieldID(env, GtkTargetEntryFc.clazz, "target", "I");
	GtkTargetEntryFc.flags = (*env)->GetFieldID(env, GtkTargetEntryFc.clazz, "flags", "I");
	GtkTargetEntryFc.info = (*env)->GetFieldID(env, GtkTargetEntryFc.clazz, "info", "I");
	GtkTargetEntryFc.cached = 1;
}

GtkTargetEntry *getGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct)
{
	if (!GtkTargetEntryFc.cached) cacheGtkTargetEntryFields(env, lpObject);
	lpStruct->target = (gchar *)(*env)->GetIntField(env, lpObject, GtkTargetEntryFc.target);
	lpStruct->flags = (guint)(*env)->GetIntField(env, lpObject, GtkTargetEntryFc.flags);
	lpStruct->info = (guint)(*env)->GetIntField(env, lpObject, GtkTargetEntryFc.info);
	return lpStruct;
}

void setGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct)
{
	if (!GtkTargetEntryFc.cached) cacheGtkTargetEntryFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkTargetEntryFc.target, (jint)lpStruct->target);
	(*env)->SetIntField(env, lpObject, GtkTargetEntryFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, GtkTargetEntryFc.info, (jint)lpStruct->info);
}
#endif

#ifndef NO_GtkTargetPair
typedef struct GtkTargetPair_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID target, flags, info;
} GtkTargetPair_FID_CACHE;

GtkTargetPair_FID_CACHE GtkTargetPairFc;

void cacheGtkTargetPairFields(JNIEnv *env, jobject lpObject)
{
	if (GtkTargetPairFc.cached) return;
	GtkTargetPairFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkTargetPairFc.target = (*env)->GetFieldID(env, GtkTargetPairFc.clazz, "target", "I");
	GtkTargetPairFc.flags = (*env)->GetFieldID(env, GtkTargetPairFc.clazz, "flags", "I");
	GtkTargetPairFc.info = (*env)->GetFieldID(env, GtkTargetPairFc.clazz, "info", "I");
	GtkTargetPairFc.cached = 1;
}

GtkTargetPair *getGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct)
{
	if (!GtkTargetPairFc.cached) cacheGtkTargetPairFields(env, lpObject);
	lpStruct->target = (GdkAtom)(*env)->GetIntField(env, lpObject, GtkTargetPairFc.target);
	lpStruct->flags = (guint)(*env)->GetIntField(env, lpObject, GtkTargetPairFc.flags);
	lpStruct->info = (guint)(*env)->GetIntField(env, lpObject, GtkTargetPairFc.info);
	return lpStruct;
}

void setGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct)
{
	if (!GtkTargetPairFc.cached) cacheGtkTargetPairFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkTargetPairFc.target, (jint)lpStruct->target);
	(*env)->SetIntField(env, lpObject, GtkTargetPairFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, GtkTargetPairFc.info, (jint)lpStruct->info);
}
#endif

#ifndef NO_PangoAttribute
typedef struct PangoAttribute_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID klass, start_index, end_index;
} PangoAttribute_FID_CACHE;

PangoAttribute_FID_CACHE PangoAttributeFc;

void cachePangoAttributeFields(JNIEnv *env, jobject lpObject)
{
	if (PangoAttributeFc.cached) return;
	PangoAttributeFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PangoAttributeFc.klass = (*env)->GetFieldID(env, PangoAttributeFc.clazz, "klass", "I");
	PangoAttributeFc.start_index = (*env)->GetFieldID(env, PangoAttributeFc.clazz, "start_index", "I");
	PangoAttributeFc.end_index = (*env)->GetFieldID(env, PangoAttributeFc.clazz, "end_index", "I");
	PangoAttributeFc.cached = 1;
}

PangoAttribute *getPangoAttributeFields(JNIEnv *env, jobject lpObject, PangoAttribute *lpStruct)
{
	if (!PangoAttributeFc.cached) cachePangoAttributeFields(env, lpObject);
	lpStruct->klass = (const PangoAttrClass *)(*env)->GetIntField(env, lpObject, PangoAttributeFc.klass);
	lpStruct->start_index = (*env)->GetIntField(env, lpObject, PangoAttributeFc.start_index);
	lpStruct->end_index = (*env)->GetIntField(env, lpObject, PangoAttributeFc.end_index);
	return lpStruct;
}

void setPangoAttributeFields(JNIEnv *env, jobject lpObject, PangoAttribute *lpStruct)
{
	if (!PangoAttributeFc.cached) cachePangoAttributeFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PangoAttributeFc.klass, (jint)lpStruct->klass);
	(*env)->SetIntField(env, lpObject, PangoAttributeFc.start_index, (jint)lpStruct->start_index);
	(*env)->SetIntField(env, lpObject, PangoAttributeFc.end_index, (jint)lpStruct->end_index);
}
#endif

#ifndef NO_PangoItem
typedef struct PangoItem_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID offset, length, num_chars, analysis_shape_engine, analysis_lang_engine, analysis_font, analysis_level, analysis_language, analysis_extra_attrs;
} PangoItem_FID_CACHE;

PangoItem_FID_CACHE PangoItemFc;

void cachePangoItemFields(JNIEnv *env, jobject lpObject)
{
	if (PangoItemFc.cached) return;
	PangoItemFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PangoItemFc.offset = (*env)->GetFieldID(env, PangoItemFc.clazz, "offset", "I");
	PangoItemFc.length = (*env)->GetFieldID(env, PangoItemFc.clazz, "length", "I");
	PangoItemFc.num_chars = (*env)->GetFieldID(env, PangoItemFc.clazz, "num_chars", "I");
	PangoItemFc.analysis_shape_engine = (*env)->GetFieldID(env, PangoItemFc.clazz, "analysis_shape_engine", "I");
	PangoItemFc.analysis_lang_engine = (*env)->GetFieldID(env, PangoItemFc.clazz, "analysis_lang_engine", "I");
	PangoItemFc.analysis_font = (*env)->GetFieldID(env, PangoItemFc.clazz, "analysis_font", "I");
	PangoItemFc.analysis_level = (*env)->GetFieldID(env, PangoItemFc.clazz, "analysis_level", "B");
	PangoItemFc.analysis_language = (*env)->GetFieldID(env, PangoItemFc.clazz, "analysis_language", "I");
	PangoItemFc.analysis_extra_attrs = (*env)->GetFieldID(env, PangoItemFc.clazz, "analysis_extra_attrs", "I");
	PangoItemFc.cached = 1;
}

PangoItem *getPangoItemFields(JNIEnv *env, jobject lpObject, PangoItem *lpStruct)
{
	if (!PangoItemFc.cached) cachePangoItemFields(env, lpObject);
	lpStruct->offset = (*env)->GetIntField(env, lpObject, PangoItemFc.offset);
	lpStruct->length = (*env)->GetIntField(env, lpObject, PangoItemFc.length);
	lpStruct->num_chars = (*env)->GetIntField(env, lpObject, PangoItemFc.num_chars);
	lpStruct->analysis.shape_engine = (PangoEngineShape *)(*env)->GetIntField(env, lpObject, PangoItemFc.analysis_shape_engine);
	lpStruct->analysis.lang_engine = (PangoEngineLang *)(*env)->GetIntField(env, lpObject, PangoItemFc.analysis_lang_engine);
	lpStruct->analysis.font = (PangoFont *)(*env)->GetIntField(env, lpObject, PangoItemFc.analysis_font);
	lpStruct->analysis.level = (*env)->GetByteField(env, lpObject, PangoItemFc.analysis_level);
	lpStruct->analysis.language = (PangoLanguage *)(*env)->GetIntField(env, lpObject, PangoItemFc.analysis_language);
	lpStruct->analysis.extra_attrs = (GSList *)(*env)->GetIntField(env, lpObject, PangoItemFc.analysis_extra_attrs);
	return lpStruct;
}

void setPangoItemFields(JNIEnv *env, jobject lpObject, PangoItem *lpStruct)
{
	if (!PangoItemFc.cached) cachePangoItemFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PangoItemFc.offset, (jint)lpStruct->offset);
	(*env)->SetIntField(env, lpObject, PangoItemFc.length, (jint)lpStruct->length);
	(*env)->SetIntField(env, lpObject, PangoItemFc.num_chars, (jint)lpStruct->num_chars);
	(*env)->SetIntField(env, lpObject, PangoItemFc.analysis_shape_engine, (jint)lpStruct->analysis.shape_engine);
	(*env)->SetIntField(env, lpObject, PangoItemFc.analysis_lang_engine, (jint)lpStruct->analysis.lang_engine);
	(*env)->SetIntField(env, lpObject, PangoItemFc.analysis_font, (jint)lpStruct->analysis.font);
	(*env)->SetByteField(env, lpObject, PangoItemFc.analysis_level, (jbyte)lpStruct->analysis.level);
	(*env)->SetIntField(env, lpObject, PangoItemFc.analysis_language, (jint)lpStruct->analysis.language);
	(*env)->SetIntField(env, lpObject, PangoItemFc.analysis_extra_attrs, (jint)lpStruct->analysis.extra_attrs);
}
#endif

#ifndef NO_PangoLayoutLine
typedef struct PangoLayoutLine_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID layout, start_index, length, runs;
} PangoLayoutLine_FID_CACHE;

PangoLayoutLine_FID_CACHE PangoLayoutLineFc;

void cachePangoLayoutLineFields(JNIEnv *env, jobject lpObject)
{
	if (PangoLayoutLineFc.cached) return;
	PangoLayoutLineFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PangoLayoutLineFc.layout = (*env)->GetFieldID(env, PangoLayoutLineFc.clazz, "layout", "I");
	PangoLayoutLineFc.start_index = (*env)->GetFieldID(env, PangoLayoutLineFc.clazz, "start_index", "I");
	PangoLayoutLineFc.length = (*env)->GetFieldID(env, PangoLayoutLineFc.clazz, "length", "I");
	PangoLayoutLineFc.runs = (*env)->GetFieldID(env, PangoLayoutLineFc.clazz, "runs", "I");
	PangoLayoutLineFc.cached = 1;
}

PangoLayoutLine *getPangoLayoutLineFields(JNIEnv *env, jobject lpObject, PangoLayoutLine *lpStruct)
{
	if (!PangoLayoutLineFc.cached) cachePangoLayoutLineFields(env, lpObject);
	lpStruct->layout = (PangoLayout *)(*env)->GetIntField(env, lpObject, PangoLayoutLineFc.layout);
	lpStruct->start_index = (*env)->GetIntField(env, lpObject, PangoLayoutLineFc.start_index);
	lpStruct->length = (*env)->GetIntField(env, lpObject, PangoLayoutLineFc.length);
	lpStruct->runs = (GSList *)(*env)->GetIntField(env, lpObject, PangoLayoutLineFc.runs);
	return lpStruct;
}

void setPangoLayoutLineFields(JNIEnv *env, jobject lpObject, PangoLayoutLine *lpStruct)
{
	if (!PangoLayoutLineFc.cached) cachePangoLayoutLineFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PangoLayoutLineFc.layout, (jint)lpStruct->layout);
	(*env)->SetIntField(env, lpObject, PangoLayoutLineFc.start_index, (jint)lpStruct->start_index);
	(*env)->SetIntField(env, lpObject, PangoLayoutLineFc.length, (jint)lpStruct->length);
	(*env)->SetIntField(env, lpObject, PangoLayoutLineFc.runs, (jint)lpStruct->runs);
}
#endif

#ifndef NO_PangoLayoutRun
typedef struct PangoLayoutRun_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID item, glyphs;
} PangoLayoutRun_FID_CACHE;

PangoLayoutRun_FID_CACHE PangoLayoutRunFc;

void cachePangoLayoutRunFields(JNIEnv *env, jobject lpObject)
{
	if (PangoLayoutRunFc.cached) return;
	PangoLayoutRunFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PangoLayoutRunFc.item = (*env)->GetFieldID(env, PangoLayoutRunFc.clazz, "item", "I");
	PangoLayoutRunFc.glyphs = (*env)->GetFieldID(env, PangoLayoutRunFc.clazz, "glyphs", "I");
	PangoLayoutRunFc.cached = 1;
}

PangoLayoutRun *getPangoLayoutRunFields(JNIEnv *env, jobject lpObject, PangoLayoutRun *lpStruct)
{
	if (!PangoLayoutRunFc.cached) cachePangoLayoutRunFields(env, lpObject);
	lpStruct->item = (PangoItem *)(*env)->GetIntField(env, lpObject, PangoLayoutRunFc.item);
	lpStruct->glyphs = (PangoGlyphString *)(*env)->GetIntField(env, lpObject, PangoLayoutRunFc.glyphs);
	return lpStruct;
}

void setPangoLayoutRunFields(JNIEnv *env, jobject lpObject, PangoLayoutRun *lpStruct)
{
	if (!PangoLayoutRunFc.cached) cachePangoLayoutRunFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PangoLayoutRunFc.item, (jint)lpStruct->item);
	(*env)->SetIntField(env, lpObject, PangoLayoutRunFc.glyphs, (jint)lpStruct->glyphs);
}
#endif

#ifndef NO_PangoLogAttr
typedef struct PangoLogAttr_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID is_line_break, is_mandatory_break, is_char_break, is_white, is_cursor_position, is_word_start, is_word_end, is_sentence_boundary, is_sentence_start, is_sentence_end;
} PangoLogAttr_FID_CACHE;

PangoLogAttr_FID_CACHE PangoLogAttrFc;

void cachePangoLogAttrFields(JNIEnv *env, jobject lpObject)
{
	if (PangoLogAttrFc.cached) return;
	PangoLogAttrFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PangoLogAttrFc.is_line_break = (*env)->GetFieldID(env, PangoLogAttrFc.clazz, "is_line_break", "Z");
	PangoLogAttrFc.is_mandatory_break = (*env)->GetFieldID(env, PangoLogAttrFc.clazz, "is_mandatory_break", "Z");
	PangoLogAttrFc.is_char_break = (*env)->GetFieldID(env, PangoLogAttrFc.clazz, "is_char_break", "Z");
	PangoLogAttrFc.is_white = (*env)->GetFieldID(env, PangoLogAttrFc.clazz, "is_white", "Z");
	PangoLogAttrFc.is_cursor_position = (*env)->GetFieldID(env, PangoLogAttrFc.clazz, "is_cursor_position", "Z");
	PangoLogAttrFc.is_word_start = (*env)->GetFieldID(env, PangoLogAttrFc.clazz, "is_word_start", "Z");
	PangoLogAttrFc.is_word_end = (*env)->GetFieldID(env, PangoLogAttrFc.clazz, "is_word_end", "Z");
	PangoLogAttrFc.is_sentence_boundary = (*env)->GetFieldID(env, PangoLogAttrFc.clazz, "is_sentence_boundary", "Z");
	PangoLogAttrFc.is_sentence_start = (*env)->GetFieldID(env, PangoLogAttrFc.clazz, "is_sentence_start", "Z");
	PangoLogAttrFc.is_sentence_end = (*env)->GetFieldID(env, PangoLogAttrFc.clazz, "is_sentence_end", "Z");
	PangoLogAttrFc.cached = 1;
}

PangoLogAttr *getPangoLogAttrFields(JNIEnv *env, jobject lpObject, PangoLogAttr *lpStruct)
{
	if (!PangoLogAttrFc.cached) cachePangoLogAttrFields(env, lpObject);
	lpStruct->is_line_break = (*env)->GetBooleanField(env, lpObject, PangoLogAttrFc.is_line_break);
	lpStruct->is_mandatory_break = (*env)->GetBooleanField(env, lpObject, PangoLogAttrFc.is_mandatory_break);
	lpStruct->is_char_break = (*env)->GetBooleanField(env, lpObject, PangoLogAttrFc.is_char_break);
	lpStruct->is_white = (*env)->GetBooleanField(env, lpObject, PangoLogAttrFc.is_white);
	lpStruct->is_cursor_position = (*env)->GetBooleanField(env, lpObject, PangoLogAttrFc.is_cursor_position);
	lpStruct->is_word_start = (*env)->GetBooleanField(env, lpObject, PangoLogAttrFc.is_word_start);
	lpStruct->is_word_end = (*env)->GetBooleanField(env, lpObject, PangoLogAttrFc.is_word_end);
	lpStruct->is_sentence_boundary = (*env)->GetBooleanField(env, lpObject, PangoLogAttrFc.is_sentence_boundary);
	lpStruct->is_sentence_start = (*env)->GetBooleanField(env, lpObject, PangoLogAttrFc.is_sentence_start);
	lpStruct->is_sentence_end = (*env)->GetBooleanField(env, lpObject, PangoLogAttrFc.is_sentence_end);
	return lpStruct;
}

void setPangoLogAttrFields(JNIEnv *env, jobject lpObject, PangoLogAttr *lpStruct)
{
	if (!PangoLogAttrFc.cached) cachePangoLogAttrFields(env, lpObject);
	(*env)->SetBooleanField(env, lpObject, PangoLogAttrFc.is_line_break, (jboolean)lpStruct->is_line_break);
	(*env)->SetBooleanField(env, lpObject, PangoLogAttrFc.is_mandatory_break, (jboolean)lpStruct->is_mandatory_break);
	(*env)->SetBooleanField(env, lpObject, PangoLogAttrFc.is_char_break, (jboolean)lpStruct->is_char_break);
	(*env)->SetBooleanField(env, lpObject, PangoLogAttrFc.is_white, (jboolean)lpStruct->is_white);
	(*env)->SetBooleanField(env, lpObject, PangoLogAttrFc.is_cursor_position, (jboolean)lpStruct->is_cursor_position);
	(*env)->SetBooleanField(env, lpObject, PangoLogAttrFc.is_word_start, (jboolean)lpStruct->is_word_start);
	(*env)->SetBooleanField(env, lpObject, PangoLogAttrFc.is_word_end, (jboolean)lpStruct->is_word_end);
	(*env)->SetBooleanField(env, lpObject, PangoLogAttrFc.is_sentence_boundary, (jboolean)lpStruct->is_sentence_boundary);
	(*env)->SetBooleanField(env, lpObject, PangoLogAttrFc.is_sentence_start, (jboolean)lpStruct->is_sentence_start);
	(*env)->SetBooleanField(env, lpObject, PangoLogAttrFc.is_sentence_end, (jboolean)lpStruct->is_sentence_end);
}
#endif

#ifndef NO_PangoRectangle
typedef struct PangoRectangle_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID x, y, width, height;
} PangoRectangle_FID_CACHE;

PangoRectangle_FID_CACHE PangoRectangleFc;

void cachePangoRectangleFields(JNIEnv *env, jobject lpObject)
{
	if (PangoRectangleFc.cached) return;
	PangoRectangleFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PangoRectangleFc.x = (*env)->GetFieldID(env, PangoRectangleFc.clazz, "x", "I");
	PangoRectangleFc.y = (*env)->GetFieldID(env, PangoRectangleFc.clazz, "y", "I");
	PangoRectangleFc.width = (*env)->GetFieldID(env, PangoRectangleFc.clazz, "width", "I");
	PangoRectangleFc.height = (*env)->GetFieldID(env, PangoRectangleFc.clazz, "height", "I");
	PangoRectangleFc.cached = 1;
}

PangoRectangle *getPangoRectangleFields(JNIEnv *env, jobject lpObject, PangoRectangle *lpStruct)
{
	if (!PangoRectangleFc.cached) cachePangoRectangleFields(env, lpObject);
	lpStruct->x = (*env)->GetIntField(env, lpObject, PangoRectangleFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, PangoRectangleFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, PangoRectangleFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, PangoRectangleFc.height);
	return lpStruct;
}

void setPangoRectangleFields(JNIEnv *env, jobject lpObject, PangoRectangle *lpStruct)
{
	if (!PangoRectangleFc.cached) cachePangoRectangleFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PangoRectangleFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, PangoRectangleFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, PangoRectangleFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, PangoRectangleFc.height, (jint)lpStruct->height);
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
	lpStruct->window = (Window)(*env)->GetIntField(env, lpObject, XAnyEventFc.window);
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

#ifndef NO_XClientMessageEvent
typedef struct XClientMessageEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, serial, send_event, display, window, message_type, format, data;
} XClientMessageEvent_FID_CACHE;

XClientMessageEvent_FID_CACHE XClientMessageEventFc;

void cacheXClientMessageEventFields(JNIEnv *env, jobject lpObject)
{
	if (XClientMessageEventFc.cached) return;
	XClientMessageEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XClientMessageEventFc.type = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "type", "I");
	XClientMessageEventFc.serial = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "serial", "I");
	XClientMessageEventFc.send_event = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "send_event", "Z");
	XClientMessageEventFc.display = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "display", "I");
	XClientMessageEventFc.window = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "window", "I");
	XClientMessageEventFc.message_type = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "message_type", "I");
	XClientMessageEventFc.format = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "format", "I");
	XClientMessageEventFc.data = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "data", "[I");
	XClientMessageEventFc.cached = 1;
}

XClientMessageEvent *getXClientMessageEventFields(JNIEnv *env, jobject lpObject, XClientMessageEvent *lpStruct)
{
	if (!XClientMessageEventFc.cached) cacheXClientMessageEventFields(env, lpObject);
	lpStruct->type = (*env)->GetIntField(env, lpObject, XClientMessageEventFc.type);
	lpStruct->serial = (*env)->GetIntField(env, lpObject, XClientMessageEventFc.serial);
	lpStruct->send_event = (*env)->GetBooleanField(env, lpObject, XClientMessageEventFc.send_event);
	lpStruct->display = (Display *)(*env)->GetIntField(env, lpObject, XClientMessageEventFc.display);
	lpStruct->window = (Window)(*env)->GetIntField(env, lpObject, XClientMessageEventFc.window);
	lpStruct->message_type = (Atom)(*env)->GetIntField(env, lpObject, XClientMessageEventFc.message_type);
	lpStruct->format = (*env)->GetIntField(env, lpObject, XClientMessageEventFc.format);
	{
	jintArray lpObject1 = (*env)->GetObjectField(env, lpObject, XClientMessageEventFc.data);
	(*env)->GetIntArrayRegion(env, lpObject1, 0, sizeof(lpStruct->data.l) / 4, (void *)lpStruct->data.l);
	}
	return lpStruct;
}

void setXClientMessageEventFields(JNIEnv *env, jobject lpObject, XClientMessageEvent *lpStruct)
{
	if (!XClientMessageEventFc.cached) cacheXClientMessageEventFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XClientMessageEventFc.type, (jint)lpStruct->type);
	(*env)->SetIntField(env, lpObject, XClientMessageEventFc.serial, (jint)lpStruct->serial);
	(*env)->SetBooleanField(env, lpObject, XClientMessageEventFc.send_event, (jboolean)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, XClientMessageEventFc.display, (jint)lpStruct->display);
	(*env)->SetIntField(env, lpObject, XClientMessageEventFc.window, (jint)lpStruct->window);
	(*env)->SetIntField(env, lpObject, XClientMessageEventFc.message_type, (jint)lpStruct->message_type);
	(*env)->SetIntField(env, lpObject, XClientMessageEventFc.format, (jint)lpStruct->format);
	{
	jintArray lpObject1 = (*env)->GetObjectField(env, lpObject, XClientMessageEventFc.data);
	(*env)->SetIntArrayRegion(env, lpObject1, 0, sizeof(lpStruct->data.l) / 4, (void *)lpStruct->data.l);
	}
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

