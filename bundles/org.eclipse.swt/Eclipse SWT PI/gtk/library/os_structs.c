/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others. All rights reserved.
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

#ifndef NO_GInterfaceInfo
typedef struct GInterfaceInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID interface_init, interface_finalize, interface_data;
} GInterfaceInfo_FID_CACHE;

GInterfaceInfo_FID_CACHE GInterfaceInfoFc;

void cacheGInterfaceInfoFields(JNIEnv *env, jobject lpObject)
{
	if (GInterfaceInfoFc.cached) return;
	GInterfaceInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GInterfaceInfoFc.interface_init = (*env)->GetFieldID(env, GInterfaceInfoFc.clazz, "interface_init", I_J);
	GInterfaceInfoFc.interface_finalize = (*env)->GetFieldID(env, GInterfaceInfoFc.clazz, "interface_finalize", I_J);
	GInterfaceInfoFc.interface_data = (*env)->GetFieldID(env, GInterfaceInfoFc.clazz, "interface_data", I_J);
	GInterfaceInfoFc.cached = 1;
}

GInterfaceInfo *getGInterfaceInfoFields(JNIEnv *env, jobject lpObject, GInterfaceInfo *lpStruct)
{
	if (!GInterfaceInfoFc.cached) cacheGInterfaceInfoFields(env, lpObject);
	lpStruct->interface_init = (GInterfaceInitFunc)(*env)->GetIntLongField(env, lpObject, GInterfaceInfoFc.interface_init);
	lpStruct->interface_finalize = (GInterfaceFinalizeFunc)(*env)->GetIntLongField(env, lpObject, GInterfaceInfoFc.interface_finalize);
	lpStruct->interface_data = (gpointer)(*env)->GetIntLongField(env, lpObject, GInterfaceInfoFc.interface_data);
	return lpStruct;
}

void setGInterfaceInfoFields(JNIEnv *env, jobject lpObject, GInterfaceInfo *lpStruct)
{
	if (!GInterfaceInfoFc.cached) cacheGInterfaceInfoFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, GInterfaceInfoFc.interface_init, (jintLong)lpStruct->interface_init);
	(*env)->SetIntLongField(env, lpObject, GInterfaceInfoFc.interface_finalize, (jintLong)lpStruct->interface_finalize);
	(*env)->SetIntLongField(env, lpObject, GInterfaceInfoFc.interface_data, (jintLong)lpStruct->interface_data);
}
#endif

#ifndef NO_GObjectClass
typedef struct GObjectClass_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID constructor, set_property, get_property, dispose, finalize, dispatch_properties_changed, notify;
} GObjectClass_FID_CACHE;

GObjectClass_FID_CACHE GObjectClassFc;

void cacheGObjectClassFields(JNIEnv *env, jobject lpObject)
{
	if (GObjectClassFc.cached) return;
	GObjectClassFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GObjectClassFc.constructor = (*env)->GetFieldID(env, GObjectClassFc.clazz, "constructor", I_J);
	GObjectClassFc.set_property = (*env)->GetFieldID(env, GObjectClassFc.clazz, "set_property", I_J);
	GObjectClassFc.get_property = (*env)->GetFieldID(env, GObjectClassFc.clazz, "get_property", I_J);
	GObjectClassFc.dispose = (*env)->GetFieldID(env, GObjectClassFc.clazz, "dispose", I_J);
	GObjectClassFc.finalize = (*env)->GetFieldID(env, GObjectClassFc.clazz, "finalize", I_J);
	GObjectClassFc.dispatch_properties_changed = (*env)->GetFieldID(env, GObjectClassFc.clazz, "dispatch_properties_changed", I_J);
	GObjectClassFc.notify = (*env)->GetFieldID(env, GObjectClassFc.clazz, "notify", I_J);
	GObjectClassFc.cached = 1;
}

GObjectClass *getGObjectClassFields(JNIEnv *env, jobject lpObject, GObjectClass *lpStruct)
{
	if (!GObjectClassFc.cached) cacheGObjectClassFields(env, lpObject);
	lpStruct->constructor = (GObject *(*)())(*env)->GetIntLongField(env, lpObject, GObjectClassFc.constructor);
	lpStruct->set_property = (void (*)())(*env)->GetIntLongField(env, lpObject, GObjectClassFc.set_property);
	lpStruct->get_property = (void (*)())(*env)->GetIntLongField(env, lpObject, GObjectClassFc.get_property);
	lpStruct->dispose = (void (*)())(*env)->GetIntLongField(env, lpObject, GObjectClassFc.dispose);
	lpStruct->finalize = (void (*)())(*env)->GetIntLongField(env, lpObject, GObjectClassFc.finalize);
	lpStruct->dispatch_properties_changed = (void (*)())(*env)->GetIntLongField(env, lpObject, GObjectClassFc.dispatch_properties_changed);
	lpStruct->notify = (void (*)())(*env)->GetIntLongField(env, lpObject, GObjectClassFc.notify);
	return lpStruct;
}

void setGObjectClassFields(JNIEnv *env, jobject lpObject, GObjectClass *lpStruct)
{
	if (!GObjectClassFc.cached) cacheGObjectClassFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, GObjectClassFc.constructor, (jintLong)lpStruct->constructor);
	(*env)->SetIntLongField(env, lpObject, GObjectClassFc.set_property, (jintLong)lpStruct->set_property);
	(*env)->SetIntLongField(env, lpObject, GObjectClassFc.get_property, (jintLong)lpStruct->get_property);
	(*env)->SetIntLongField(env, lpObject, GObjectClassFc.dispose, (jintLong)lpStruct->dispose);
	(*env)->SetIntLongField(env, lpObject, GObjectClassFc.finalize, (jintLong)lpStruct->finalize);
	(*env)->SetIntLongField(env, lpObject, GObjectClassFc.dispatch_properties_changed, (jintLong)lpStruct->dispatch_properties_changed);
	(*env)->SetIntLongField(env, lpObject, GObjectClassFc.notify, (jintLong)lpStruct->notify);
}
#endif

#ifndef NO_GTypeInfo
typedef struct GTypeInfo_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID class_size, base_init, base_finalize, class_init, class_finalize, class_data, instance_size, n_preallocs, instance_init, value_table;
} GTypeInfo_FID_CACHE;

GTypeInfo_FID_CACHE GTypeInfoFc;

void cacheGTypeInfoFields(JNIEnv *env, jobject lpObject)
{
	if (GTypeInfoFc.cached) return;
	GTypeInfoFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GTypeInfoFc.class_size = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "class_size", "S");
	GTypeInfoFc.base_init = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "base_init", I_J);
	GTypeInfoFc.base_finalize = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "base_finalize", I_J);
	GTypeInfoFc.class_init = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "class_init", I_J);
	GTypeInfoFc.class_finalize = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "class_finalize", I_J);
	GTypeInfoFc.class_data = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "class_data", I_J);
	GTypeInfoFc.instance_size = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "instance_size", "S");
	GTypeInfoFc.n_preallocs = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "n_preallocs", "S");
	GTypeInfoFc.instance_init = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "instance_init", I_J);
	GTypeInfoFc.value_table = (*env)->GetFieldID(env, GTypeInfoFc.clazz, "value_table", I_J);
	GTypeInfoFc.cached = 1;
}

GTypeInfo *getGTypeInfoFields(JNIEnv *env, jobject lpObject, GTypeInfo *lpStruct)
{
	if (!GTypeInfoFc.cached) cacheGTypeInfoFields(env, lpObject);
	lpStruct->class_size = (guint16)(*env)->GetShortField(env, lpObject, GTypeInfoFc.class_size);
	lpStruct->base_init = (GBaseInitFunc)(*env)->GetIntLongField(env, lpObject, GTypeInfoFc.base_init);
	lpStruct->base_finalize = (GBaseFinalizeFunc)(*env)->GetIntLongField(env, lpObject, GTypeInfoFc.base_finalize);
	lpStruct->class_init = (GClassInitFunc)(*env)->GetIntLongField(env, lpObject, GTypeInfoFc.class_init);
	lpStruct->class_finalize = (GClassFinalizeFunc)(*env)->GetIntLongField(env, lpObject, GTypeInfoFc.class_finalize);
	lpStruct->class_data = (gconstpointer)(*env)->GetIntLongField(env, lpObject, GTypeInfoFc.class_data);
	lpStruct->instance_size = (guint16)(*env)->GetShortField(env, lpObject, GTypeInfoFc.instance_size);
	lpStruct->n_preallocs = (guint16)(*env)->GetShortField(env, lpObject, GTypeInfoFc.n_preallocs);
	lpStruct->instance_init = (GInstanceInitFunc)(*env)->GetIntLongField(env, lpObject, GTypeInfoFc.instance_init);
	lpStruct->value_table = (GTypeValueTable *)(*env)->GetIntLongField(env, lpObject, GTypeInfoFc.value_table);
	return lpStruct;
}

void setGTypeInfoFields(JNIEnv *env, jobject lpObject, GTypeInfo *lpStruct)
{
	if (!GTypeInfoFc.cached) cacheGTypeInfoFields(env, lpObject);
	(*env)->SetShortField(env, lpObject, GTypeInfoFc.class_size, (jshort)lpStruct->class_size);
	(*env)->SetIntLongField(env, lpObject, GTypeInfoFc.base_init, (jintLong)lpStruct->base_init);
	(*env)->SetIntLongField(env, lpObject, GTypeInfoFc.base_finalize, (jintLong)lpStruct->base_finalize);
	(*env)->SetIntLongField(env, lpObject, GTypeInfoFc.class_init, (jintLong)lpStruct->class_init);
	(*env)->SetIntLongField(env, lpObject, GTypeInfoFc.class_finalize, (jintLong)lpStruct->class_finalize);
	(*env)->SetIntLongField(env, lpObject, GTypeInfoFc.class_data, (jintLong)lpStruct->class_data);
	(*env)->SetShortField(env, lpObject, GTypeInfoFc.instance_size, (jshort)lpStruct->instance_size);
	(*env)->SetShortField(env, lpObject, GTypeInfoFc.n_preallocs, (jshort)lpStruct->n_preallocs);
	(*env)->SetIntLongField(env, lpObject, GTypeInfoFc.instance_init, (jintLong)lpStruct->instance_init);
	(*env)->SetIntLongField(env, lpObject, GTypeInfoFc.value_table, (jintLong)lpStruct->value_table);
}
#endif

#ifndef NO_GTypeQuery
typedef struct GTypeQuery_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID type, type_name, class_size, instance_size;
} GTypeQuery_FID_CACHE;

GTypeQuery_FID_CACHE GTypeQueryFc;

void cacheGTypeQueryFields(JNIEnv *env, jobject lpObject)
{
	if (GTypeQueryFc.cached) return;
	GTypeQueryFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GTypeQueryFc.type = (*env)->GetFieldID(env, GTypeQueryFc.clazz, "type", "I");
	GTypeQueryFc.type_name = (*env)->GetFieldID(env, GTypeQueryFc.clazz, "type_name", I_J);
	GTypeQueryFc.class_size = (*env)->GetFieldID(env, GTypeQueryFc.clazz, "class_size", "I");
	GTypeQueryFc.instance_size = (*env)->GetFieldID(env, GTypeQueryFc.clazz, "instance_size", "I");
	GTypeQueryFc.cached = 1;
}

GTypeQuery *getGTypeQueryFields(JNIEnv *env, jobject lpObject, GTypeQuery *lpStruct)
{
	if (!GTypeQueryFc.cached) cacheGTypeQueryFields(env, lpObject);
	lpStruct->type = (GType)(*env)->GetIntField(env, lpObject, GTypeQueryFc.type);
	lpStruct->type_name = (const gchar *)(*env)->GetIntLongField(env, lpObject, GTypeQueryFc.type_name);
	lpStruct->class_size = (guint)(*env)->GetIntField(env, lpObject, GTypeQueryFc.class_size);
	lpStruct->instance_size = (guint)(*env)->GetIntField(env, lpObject, GTypeQueryFc.instance_size);
	return lpStruct;
}

void setGTypeQueryFields(JNIEnv *env, jobject lpObject, GTypeQuery *lpStruct)
{
	if (!GTypeQueryFc.cached) cacheGTypeQueryFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GTypeQueryFc.type, (jint)lpStruct->type);
	(*env)->SetIntLongField(env, lpObject, GTypeQueryFc.type_name, (jintLong)lpStruct->type_name);
	(*env)->SetIntField(env, lpObject, GTypeQueryFc.class_size, (jint)lpStruct->class_size);
	(*env)->SetIntField(env, lpObject, GTypeQueryFc.instance_size, (jint)lpStruct->instance_size);
}
#endif

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
	GdkDragContextFc.source_window = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "source_window", I_J);
	GdkDragContextFc.dest_window = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "dest_window", I_J);
	GdkDragContextFc.targets = (*env)->GetFieldID(env, GdkDragContextFc.clazz, "targets", I_J);
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
	lpStruct->source_window = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkDragContextFc.source_window);
	lpStruct->dest_window = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkDragContextFc.dest_window);
	lpStruct->targets = (GList *)(*env)->GetIntLongField(env, lpObject, GdkDragContextFc.targets);
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
	(*env)->SetIntLongField(env, lpObject, GdkDragContextFc.source_window, (jintLong)lpStruct->source_window);
	(*env)->SetIntLongField(env, lpObject, GdkDragContextFc.dest_window, (jintLong)lpStruct->dest_window);
	(*env)->SetIntLongField(env, lpObject, GdkDragContextFc.targets, (jintLong)lpStruct->targets);
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

#ifndef NO_GdkEventAny
typedef struct GdkEventAny_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event;
} GdkEventAny_FID_CACHE;

GdkEventAny_FID_CACHE GdkEventAnyFc;

void cacheGdkEventAnyFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventAnyFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventAnyFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventAnyFc.window = (*env)->GetFieldID(env, GdkEventAnyFc.clazz, "window", I_J);
	GdkEventAnyFc.send_event = (*env)->GetFieldID(env, GdkEventAnyFc.clazz, "send_event", "B");
	GdkEventAnyFc.cached = 1;
}

GdkEventAny *getGdkEventAnyFields(JNIEnv *env, jobject lpObject, GdkEventAny *lpStruct)
{
	if (!GdkEventAnyFc.cached) cacheGdkEventAnyFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkEventAnyFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventAnyFc.send_event);
	return lpStruct;
}

void setGdkEventAnyFields(JNIEnv *env, jobject lpObject, GdkEventAny *lpStruct)
{
	if (!GdkEventAnyFc.cached) cacheGdkEventAnyFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntLongField(env, lpObject, GdkEventAnyFc.window, (jintLong)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventAnyFc.send_event, (jbyte)lpStruct->send_event);
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
	GdkEventButtonFc.window = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "window", I_J);
	GdkEventButtonFc.send_event = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "send_event", "B");
	GdkEventButtonFc.time = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "time", "I");
	GdkEventButtonFc.x = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "x", "D");
	GdkEventButtonFc.y = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "y", "D");
	GdkEventButtonFc.axes = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "axes", I_J);
	GdkEventButtonFc.state = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "state", "I");
	GdkEventButtonFc.button = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "button", "I");
	GdkEventButtonFc.device = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "device", I_J);
	GdkEventButtonFc.x_root = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "x_root", "D");
	GdkEventButtonFc.y_root = (*env)->GetFieldID(env, GdkEventButtonFc.clazz, "y_root", "D");
	GdkEventButtonFc.cached = 1;
}

GdkEventButton *getGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct)
{
	if (!GdkEventButtonFc.cached) cacheGdkEventButtonFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkEventButtonFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventButtonFc.send_event);
	lpStruct->time = (guint32)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.time);
	lpStruct->x = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventButtonFc.x);
	lpStruct->y = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventButtonFc.y);
	lpStruct->axes = (gdouble *)(*env)->GetIntLongField(env, lpObject, GdkEventButtonFc.axes);
	lpStruct->state = (guint)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.state);
	lpStruct->button = (guint)(*env)->GetIntField(env, lpObject, GdkEventButtonFc.button);
	lpStruct->device = (GdkDevice *)(*env)->GetIntLongField(env, lpObject, GdkEventButtonFc.device);
	lpStruct->x_root = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventButtonFc.x_root);
	lpStruct->y_root = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventButtonFc.y_root);
	return lpStruct;
}

void setGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct)
{
	if (!GdkEventButtonFc.cached) cacheGdkEventButtonFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntLongField(env, lpObject, GdkEventButtonFc.window, (jintLong)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventButtonFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.time, (jint)lpStruct->time);
	(*env)->SetDoubleField(env, lpObject, GdkEventButtonFc.x, (jdouble)lpStruct->x);
	(*env)->SetDoubleField(env, lpObject, GdkEventButtonFc.y, (jdouble)lpStruct->y);
	(*env)->SetIntLongField(env, lpObject, GdkEventButtonFc.axes, (jintLong)lpStruct->axes);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, GdkEventButtonFc.button, (jint)lpStruct->button);
	(*env)->SetIntLongField(env, lpObject, GdkEventButtonFc.device, (jintLong)lpStruct->device);
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
	GdkEventCrossingFc.window = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "window", I_J);
	GdkEventCrossingFc.send_event = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "send_event", "B");
	GdkEventCrossingFc.subwindow = (*env)->GetFieldID(env, GdkEventCrossingFc.clazz, "subwindow", I_J);
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
	lpStruct->window = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkEventCrossingFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventCrossingFc.send_event);
	lpStruct->subwindow = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkEventCrossingFc.subwindow);
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
	(*env)->SetIntLongField(env, lpObject, GdkEventCrossingFc.window, (jintLong)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventCrossingFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntLongField(env, lpObject, GdkEventCrossingFc.subwindow, (jintLong)lpStruct->subwindow);
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
	GdkEventExposeFc.window = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "window", I_J);
	GdkEventExposeFc.send_event = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "send_event", "B");
	GdkEventExposeFc.area_x = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "area_x", "I");
	GdkEventExposeFc.area_y = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "area_y", "I");
	GdkEventExposeFc.area_width = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "area_width", "I");
	GdkEventExposeFc.area_height = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "area_height", "I");
	GdkEventExposeFc.region = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "region", I_J);
	GdkEventExposeFc.count = (*env)->GetFieldID(env, GdkEventExposeFc.clazz, "count", "I");
	GdkEventExposeFc.cached = 1;
}

GdkEventExpose *getGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct)
{
	if (!GdkEventExposeFc.cached) cacheGdkEventExposeFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkEventExposeFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventExposeFc.send_event);
	lpStruct->area.x = (*env)->GetIntField(env, lpObject, GdkEventExposeFc.area_x);
	lpStruct->area.y = (*env)->GetIntField(env, lpObject, GdkEventExposeFc.area_y);
	lpStruct->area.width = (*env)->GetIntField(env, lpObject, GdkEventExposeFc.area_width);
	lpStruct->area.height = (*env)->GetIntField(env, lpObject, GdkEventExposeFc.area_height);
	lpStruct->region = (GdkRegion *)(*env)->GetIntLongField(env, lpObject, GdkEventExposeFc.region);
	lpStruct->count = (gint)(*env)->GetIntField(env, lpObject, GdkEventExposeFc.count);
	return lpStruct;
}

void setGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct)
{
	if (!GdkEventExposeFc.cached) cacheGdkEventExposeFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntLongField(env, lpObject, GdkEventExposeFc.window, (jintLong)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventExposeFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.area_x, (jint)lpStruct->area.x);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.area_y, (jint)lpStruct->area.y);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.area_width, (jint)lpStruct->area.width);
	(*env)->SetIntField(env, lpObject, GdkEventExposeFc.area_height, (jint)lpStruct->area.height);
	(*env)->SetIntLongField(env, lpObject, GdkEventExposeFc.region, (jintLong)lpStruct->region);
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
	GdkEventFocusFc.window = (*env)->GetFieldID(env, GdkEventFocusFc.clazz, "window", I_J);
	GdkEventFocusFc.send_event = (*env)->GetFieldID(env, GdkEventFocusFc.clazz, "send_event", "B");
	GdkEventFocusFc.in = (*env)->GetFieldID(env, GdkEventFocusFc.clazz, "in", "S");
	GdkEventFocusFc.cached = 1;
}

GdkEventFocus *getGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct)
{
	if (!GdkEventFocusFc.cached) cacheGdkEventFocusFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkEventFocusFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventFocusFc.send_event);
	lpStruct->in = (gint16)(*env)->GetShortField(env, lpObject, GdkEventFocusFc.in);
	return lpStruct;
}

void setGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct)
{
	if (!GdkEventFocusFc.cached) cacheGdkEventFocusFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntLongField(env, lpObject, GdkEventFocusFc.window, (jintLong)lpStruct->window);
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
	GdkEventKeyFc.window = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "window", I_J);
	GdkEventKeyFc.send_event = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "send_event", "B");
	GdkEventKeyFc.time = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "time", "I");
	GdkEventKeyFc.state = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "state", "I");
	GdkEventKeyFc.keyval = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "keyval", "I");
	GdkEventKeyFc.length = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "length", "I");
	GdkEventKeyFc.string = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "string", I_J);
	GdkEventKeyFc.hardware_keycode = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "hardware_keycode", "S");
	GdkEventKeyFc.group = (*env)->GetFieldID(env, GdkEventKeyFc.clazz, "group", "B");
	GdkEventKeyFc.cached = 1;
}

GdkEventKey *getGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct)
{
	if (!GdkEventKeyFc.cached) cacheGdkEventKeyFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkEventKeyFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventKeyFc.send_event);
	lpStruct->time = (guint32)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.time);
	lpStruct->state = (guint)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.state);
	lpStruct->keyval = (guint)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.keyval);
	lpStruct->length = (gint)(*env)->GetIntField(env, lpObject, GdkEventKeyFc.length);
	lpStruct->string = (gchar *)(*env)->GetIntLongField(env, lpObject, GdkEventKeyFc.string);
	lpStruct->hardware_keycode = (guint16)(*env)->GetShortField(env, lpObject, GdkEventKeyFc.hardware_keycode);
	lpStruct->group = (guint8)(*env)->GetByteField(env, lpObject, GdkEventKeyFc.group);
	return lpStruct;
}

void setGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct)
{
	if (!GdkEventKeyFc.cached) cacheGdkEventKeyFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntLongField(env, lpObject, GdkEventKeyFc.window, (jintLong)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventKeyFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.keyval, (jint)lpStruct->keyval);
	(*env)->SetIntField(env, lpObject, GdkEventKeyFc.length, (jint)lpStruct->length);
	(*env)->SetIntLongField(env, lpObject, GdkEventKeyFc.string, (jintLong)lpStruct->string);
	(*env)->SetShortField(env, lpObject, GdkEventKeyFc.hardware_keycode, (jshort)lpStruct->hardware_keycode);
	(*env)->SetByteField(env, lpObject, GdkEventKeyFc.group, (jbyte)lpStruct->group);
}
#endif

#ifndef NO_GdkEventMotion
typedef struct GdkEventMotion_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, time, x, y, axes, state, is_hint, device, x_root, y_root;
} GdkEventMotion_FID_CACHE;

GdkEventMotion_FID_CACHE GdkEventMotionFc;

void cacheGdkEventMotionFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventMotionFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventMotionFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventMotionFc.window = (*env)->GetFieldID(env, GdkEventMotionFc.clazz, "window", I_J);
	GdkEventMotionFc.send_event = (*env)->GetFieldID(env, GdkEventMotionFc.clazz, "send_event", "B");
	GdkEventMotionFc.time = (*env)->GetFieldID(env, GdkEventMotionFc.clazz, "time", "I");
	GdkEventMotionFc.x = (*env)->GetFieldID(env, GdkEventMotionFc.clazz, "x", "D");
	GdkEventMotionFc.y = (*env)->GetFieldID(env, GdkEventMotionFc.clazz, "y", "D");
	GdkEventMotionFc.axes = (*env)->GetFieldID(env, GdkEventMotionFc.clazz, "axes", I_J);
	GdkEventMotionFc.state = (*env)->GetFieldID(env, GdkEventMotionFc.clazz, "state", "I");
	GdkEventMotionFc.is_hint = (*env)->GetFieldID(env, GdkEventMotionFc.clazz, "is_hint", "S");
	GdkEventMotionFc.device = (*env)->GetFieldID(env, GdkEventMotionFc.clazz, "device", I_J);
	GdkEventMotionFc.x_root = (*env)->GetFieldID(env, GdkEventMotionFc.clazz, "x_root", "D");
	GdkEventMotionFc.y_root = (*env)->GetFieldID(env, GdkEventMotionFc.clazz, "y_root", "D");
	GdkEventMotionFc.cached = 1;
}

GdkEventMotion *getGdkEventMotionFields(JNIEnv *env, jobject lpObject, GdkEventMotion *lpStruct)
{
	if (!GdkEventMotionFc.cached) cacheGdkEventMotionFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkEventMotionFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventMotionFc.send_event);
	lpStruct->time = (guint32)(*env)->GetIntField(env, lpObject, GdkEventMotionFc.time);
	lpStruct->x = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventMotionFc.x);
	lpStruct->y = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventMotionFc.y);
	lpStruct->axes = (gdouble *)(*env)->GetIntLongField(env, lpObject, GdkEventMotionFc.axes);
	lpStruct->state = (guint)(*env)->GetIntField(env, lpObject, GdkEventMotionFc.state);
	lpStruct->is_hint = (gint16)(*env)->GetShortField(env, lpObject, GdkEventMotionFc.is_hint);
	lpStruct->device = (GdkDevice *)(*env)->GetIntLongField(env, lpObject, GdkEventMotionFc.device);
	lpStruct->x_root = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventMotionFc.x_root);
	lpStruct->y_root = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventMotionFc.y_root);
	return lpStruct;
}

void setGdkEventMotionFields(JNIEnv *env, jobject lpObject, GdkEventMotion *lpStruct)
{
	if (!GdkEventMotionFc.cached) cacheGdkEventMotionFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntLongField(env, lpObject, GdkEventMotionFc.window, (jintLong)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventMotionFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventMotionFc.time, (jint)lpStruct->time);
	(*env)->SetDoubleField(env, lpObject, GdkEventMotionFc.x, (jdouble)lpStruct->x);
	(*env)->SetDoubleField(env, lpObject, GdkEventMotionFc.y, (jdouble)lpStruct->y);
	(*env)->SetIntLongField(env, lpObject, GdkEventMotionFc.axes, (jintLong)lpStruct->axes);
	(*env)->SetIntField(env, lpObject, GdkEventMotionFc.state, (jint)lpStruct->state);
	(*env)->SetShortField(env, lpObject, GdkEventMotionFc.is_hint, (jshort)lpStruct->is_hint);
	(*env)->SetIntLongField(env, lpObject, GdkEventMotionFc.device, (jintLong)lpStruct->device);
	(*env)->SetDoubleField(env, lpObject, GdkEventMotionFc.x_root, (jdouble)lpStruct->x_root);
	(*env)->SetDoubleField(env, lpObject, GdkEventMotionFc.y_root, (jdouble)lpStruct->y_root);
}
#endif

#ifndef NO_GdkEventScroll
typedef struct GdkEventScroll_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID window, send_event, time, x, y, state, direction, device, x_root, y_root;
} GdkEventScroll_FID_CACHE;

GdkEventScroll_FID_CACHE GdkEventScrollFc;

void cacheGdkEventScrollFields(JNIEnv *env, jobject lpObject)
{
	if (GdkEventScrollFc.cached) return;
	cacheGdkEventFields(env, lpObject);
	GdkEventScrollFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkEventScrollFc.window = (*env)->GetFieldID(env, GdkEventScrollFc.clazz, "window", I_J);
	GdkEventScrollFc.send_event = (*env)->GetFieldID(env, GdkEventScrollFc.clazz, "send_event", "B");
	GdkEventScrollFc.time = (*env)->GetFieldID(env, GdkEventScrollFc.clazz, "time", "I");
	GdkEventScrollFc.x = (*env)->GetFieldID(env, GdkEventScrollFc.clazz, "x", "D");
	GdkEventScrollFc.y = (*env)->GetFieldID(env, GdkEventScrollFc.clazz, "y", "D");
	GdkEventScrollFc.state = (*env)->GetFieldID(env, GdkEventScrollFc.clazz, "state", "I");
	GdkEventScrollFc.direction = (*env)->GetFieldID(env, GdkEventScrollFc.clazz, "direction", "I");
	GdkEventScrollFc.device = (*env)->GetFieldID(env, GdkEventScrollFc.clazz, "device", I_J);
	GdkEventScrollFc.x_root = (*env)->GetFieldID(env, GdkEventScrollFc.clazz, "x_root", "D");
	GdkEventScrollFc.y_root = (*env)->GetFieldID(env, GdkEventScrollFc.clazz, "y_root", "D");
	GdkEventScrollFc.cached = 1;
}

GdkEventScroll *getGdkEventScrollFields(JNIEnv *env, jobject lpObject, GdkEventScroll *lpStruct)
{
	if (!GdkEventScrollFc.cached) cacheGdkEventScrollFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkEventScrollFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventScrollFc.send_event);
	lpStruct->time = (guint32)(*env)->GetIntField(env, lpObject, GdkEventScrollFc.time);
	lpStruct->x = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventScrollFc.x);
	lpStruct->y = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventScrollFc.y);
	lpStruct->state = (guint)(*env)->GetIntField(env, lpObject, GdkEventScrollFc.state);
	lpStruct->direction = (GdkScrollDirection)(*env)->GetIntField(env, lpObject, GdkEventScrollFc.direction);
	lpStruct->device = (GdkDevice *)(*env)->GetIntLongField(env, lpObject, GdkEventScrollFc.device);
	lpStruct->x_root = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventScrollFc.x_root);
	lpStruct->y_root = (gdouble)(*env)->GetDoubleField(env, lpObject, GdkEventScrollFc.y_root);
	return lpStruct;
}

void setGdkEventScrollFields(JNIEnv *env, jobject lpObject, GdkEventScroll *lpStruct)
{
	if (!GdkEventScrollFc.cached) cacheGdkEventScrollFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntLongField(env, lpObject, GdkEventScrollFc.window, (jintLong)lpStruct->window);
	(*env)->SetByteField(env, lpObject, GdkEventScrollFc.send_event, (jbyte)lpStruct->send_event);
	(*env)->SetIntField(env, lpObject, GdkEventScrollFc.time, (jint)lpStruct->time);
	(*env)->SetDoubleField(env, lpObject, GdkEventScrollFc.x, (jdouble)lpStruct->x);
	(*env)->SetDoubleField(env, lpObject, GdkEventScrollFc.y, (jdouble)lpStruct->y);
	(*env)->SetIntField(env, lpObject, GdkEventScrollFc.state, (jint)lpStruct->state);
	(*env)->SetIntField(env, lpObject, GdkEventScrollFc.direction, (jint)lpStruct->direction);
	(*env)->SetIntLongField(env, lpObject, GdkEventScrollFc.device, (jintLong)lpStruct->device);
	(*env)->SetDoubleField(env, lpObject, GdkEventScrollFc.x_root, (jdouble)lpStruct->x_root);
	(*env)->SetDoubleField(env, lpObject, GdkEventScrollFc.y_root, (jdouble)lpStruct->y_root);
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
	GdkEventVisibilityFc.window = (*env)->GetFieldID(env, GdkEventVisibilityFc.clazz, "window", I_J);
	GdkEventVisibilityFc.send_event = (*env)->GetFieldID(env, GdkEventVisibilityFc.clazz, "send_event", "B");
	GdkEventVisibilityFc.state = (*env)->GetFieldID(env, GdkEventVisibilityFc.clazz, "state", "I");
	GdkEventVisibilityFc.cached = 1;
}

GdkEventVisibility *getGdkEventVisibilityFields(JNIEnv *env, jobject lpObject, GdkEventVisibility *lpStruct)
{
	if (!GdkEventVisibilityFc.cached) cacheGdkEventVisibilityFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkEventVisibilityFc.window);
	lpStruct->send_event = (gint8)(*env)->GetByteField(env, lpObject, GdkEventVisibilityFc.send_event);
	lpStruct->state = (GdkVisibilityState)(*env)->GetIntField(env, lpObject, GdkEventVisibilityFc.state);
	return lpStruct;
}

void setGdkEventVisibilityFields(JNIEnv *env, jobject lpObject, GdkEventVisibility *lpStruct)
{
	if (!GdkEventVisibilityFc.cached) cacheGdkEventVisibilityFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntLongField(env, lpObject, GdkEventVisibilityFc.window, (jintLong)lpStruct->window);
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
	GdkEventWindowStateFc.window = (*env)->GetFieldID(env, GdkEventWindowStateFc.clazz, "window", I_J);
	GdkEventWindowStateFc.send_event = (*env)->GetFieldID(env, GdkEventWindowStateFc.clazz, "send_event", "B");
	GdkEventWindowStateFc.changed_mask = (*env)->GetFieldID(env, GdkEventWindowStateFc.clazz, "changed_mask", "I");
	GdkEventWindowStateFc.new_window_state = (*env)->GetFieldID(env, GdkEventWindowStateFc.clazz, "new_window_state", "I");
	GdkEventWindowStateFc.cached = 1;
}

GdkEventWindowState *getGdkEventWindowStateFields(JNIEnv *env, jobject lpObject, GdkEventWindowState *lpStruct)
{
	if (!GdkEventWindowStateFc.cached) cacheGdkEventWindowStateFields(env, lpObject);
	getGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	lpStruct->window = (GdkWindow *)(*env)->GetIntLongField(env, lpObject, GdkEventWindowStateFc.window);
	lpStruct->send_event = (*env)->GetByteField(env, lpObject, GdkEventWindowStateFc.send_event);
	lpStruct->changed_mask = (*env)->GetIntField(env, lpObject, GdkEventWindowStateFc.changed_mask);
	lpStruct->new_window_state = (*env)->GetIntField(env, lpObject, GdkEventWindowStateFc.new_window_state);
	return lpStruct;
}

void setGdkEventWindowStateFields(JNIEnv *env, jobject lpObject, GdkEventWindowState *lpStruct)
{
	if (!GdkEventWindowStateFc.cached) cacheGdkEventWindowStateFields(env, lpObject);
	setGdkEventFields(env, lpObject, (GdkEvent *)lpStruct);
	(*env)->SetIntLongField(env, lpObject, GdkEventWindowStateFc.window, (jintLong)lpStruct->window);
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
	GdkGCValuesFc.font = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "font", I_J);
	GdkGCValuesFc.function = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "function", I_J);
	GdkGCValuesFc.fill = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "fill", "I");
	GdkGCValuesFc.tile = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "tile", I_J);
	GdkGCValuesFc.stipple = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "stipple", I_J);
	GdkGCValuesFc.clip_mask = (*env)->GetFieldID(env, GdkGCValuesFc.clazz, "clip_mask", I_J);
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
	lpStruct->font = (GdkFont *)(*env)->GetIntLongField(env, lpObject, GdkGCValuesFc.font);
	lpStruct->function = (GdkFunction)(*env)->GetIntLongField(env, lpObject, GdkGCValuesFc.function);
	lpStruct->fill = (GdkFill)(*env)->GetIntField(env, lpObject, GdkGCValuesFc.fill);
	lpStruct->tile = (GdkPixmap *)(*env)->GetIntLongField(env, lpObject, GdkGCValuesFc.tile);
	lpStruct->stipple = (GdkPixmap *)(*env)->GetIntLongField(env, lpObject, GdkGCValuesFc.stipple);
	lpStruct->clip_mask = (GdkPixmap *)(*env)->GetIntLongField(env, lpObject, GdkGCValuesFc.clip_mask);
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
	(*env)->SetIntLongField(env, lpObject, GdkGCValuesFc.font, (jintLong)lpStruct->font);
	(*env)->SetIntLongField(env, lpObject, GdkGCValuesFc.function, (jintLong)lpStruct->function);
	(*env)->SetIntField(env, lpObject, GdkGCValuesFc.fill, (jint)lpStruct->fill);
	(*env)->SetIntLongField(env, lpObject, GdkGCValuesFc.tile, (jintLong)lpStruct->tile);
	(*env)->SetIntLongField(env, lpObject, GdkGCValuesFc.stipple, (jintLong)lpStruct->stipple);
	(*env)->SetIntLongField(env, lpObject, GdkGCValuesFc.clip_mask, (jintLong)lpStruct->clip_mask);
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

#ifndef NO_GdkGeometry
typedef struct GdkGeometry_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID min_width, min_height, max_width, max_height, base_width, base_height, width_inc, height_inc, min_aspect, max_aspect, win_gravity;
} GdkGeometry_FID_CACHE;

GdkGeometry_FID_CACHE GdkGeometryFc;

void cacheGdkGeometryFields(JNIEnv *env, jobject lpObject)
{
	if (GdkGeometryFc.cached) return;
	GdkGeometryFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GdkGeometryFc.min_width = (*env)->GetFieldID(env, GdkGeometryFc.clazz, "min_width", "I");
	GdkGeometryFc.min_height = (*env)->GetFieldID(env, GdkGeometryFc.clazz, "min_height", "I");
	GdkGeometryFc.max_width = (*env)->GetFieldID(env, GdkGeometryFc.clazz, "max_width", "I");
	GdkGeometryFc.max_height = (*env)->GetFieldID(env, GdkGeometryFc.clazz, "max_height", "I");
	GdkGeometryFc.base_width = (*env)->GetFieldID(env, GdkGeometryFc.clazz, "base_width", "I");
	GdkGeometryFc.base_height = (*env)->GetFieldID(env, GdkGeometryFc.clazz, "base_height", "I");
	GdkGeometryFc.width_inc = (*env)->GetFieldID(env, GdkGeometryFc.clazz, "width_inc", "I");
	GdkGeometryFc.height_inc = (*env)->GetFieldID(env, GdkGeometryFc.clazz, "height_inc", "I");
	GdkGeometryFc.min_aspect = (*env)->GetFieldID(env, GdkGeometryFc.clazz, "min_aspect", "D");
	GdkGeometryFc.max_aspect = (*env)->GetFieldID(env, GdkGeometryFc.clazz, "max_aspect", "D");
	GdkGeometryFc.win_gravity = (*env)->GetFieldID(env, GdkGeometryFc.clazz, "win_gravity", "I");
	GdkGeometryFc.cached = 1;
}

GdkGeometry *getGdkGeometryFields(JNIEnv *env, jobject lpObject, GdkGeometry *lpStruct)
{
	if (!GdkGeometryFc.cached) cacheGdkGeometryFields(env, lpObject);
	lpStruct->min_width = (*env)->GetIntField(env, lpObject, GdkGeometryFc.min_width);
	lpStruct->min_height = (*env)->GetIntField(env, lpObject, GdkGeometryFc.min_height);
	lpStruct->max_width = (*env)->GetIntField(env, lpObject, GdkGeometryFc.max_width);
	lpStruct->max_height = (*env)->GetIntField(env, lpObject, GdkGeometryFc.max_height);
	lpStruct->base_width = (*env)->GetIntField(env, lpObject, GdkGeometryFc.base_width);
	lpStruct->base_height = (*env)->GetIntField(env, lpObject, GdkGeometryFc.base_height);
	lpStruct->width_inc = (*env)->GetIntField(env, lpObject, GdkGeometryFc.width_inc);
	lpStruct->height_inc = (*env)->GetIntField(env, lpObject, GdkGeometryFc.height_inc);
	lpStruct->min_aspect = (*env)->GetDoubleField(env, lpObject, GdkGeometryFc.min_aspect);
	lpStruct->max_aspect = (*env)->GetDoubleField(env, lpObject, GdkGeometryFc.max_aspect);
	lpStruct->win_gravity = (*env)->GetIntField(env, lpObject, GdkGeometryFc.win_gravity);
	return lpStruct;
}

void setGdkGeometryFields(JNIEnv *env, jobject lpObject, GdkGeometry *lpStruct)
{
	if (!GdkGeometryFc.cached) cacheGdkGeometryFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkGeometryFc.min_width, (jint)lpStruct->min_width);
	(*env)->SetIntField(env, lpObject, GdkGeometryFc.min_height, (jint)lpStruct->min_height);
	(*env)->SetIntField(env, lpObject, GdkGeometryFc.max_width, (jint)lpStruct->max_width);
	(*env)->SetIntField(env, lpObject, GdkGeometryFc.max_height, (jint)lpStruct->max_height);
	(*env)->SetIntField(env, lpObject, GdkGeometryFc.base_width, (jint)lpStruct->base_width);
	(*env)->SetIntField(env, lpObject, GdkGeometryFc.base_height, (jint)lpStruct->base_height);
	(*env)->SetIntField(env, lpObject, GdkGeometryFc.width_inc, (jint)lpStruct->width_inc);
	(*env)->SetIntField(env, lpObject, GdkGeometryFc.height_inc, (jint)lpStruct->height_inc);
	(*env)->SetDoubleField(env, lpObject, GdkGeometryFc.min_aspect, (jdouble)lpStruct->min_aspect);
	(*env)->SetDoubleField(env, lpObject, GdkGeometryFc.max_aspect, (jdouble)lpStruct->max_aspect);
	(*env)->SetIntField(env, lpObject, GdkGeometryFc.win_gravity, (jint)lpStruct->win_gravity);
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
	GdkImageFc.visual = (*env)->GetFieldID(env, GdkImageFc.clazz, "visual", I_J);
	GdkImageFc.byte_order = (*env)->GetFieldID(env, GdkImageFc.clazz, "byte_order", "I");
	GdkImageFc.width = (*env)->GetFieldID(env, GdkImageFc.clazz, "width", "I");
	GdkImageFc.height = (*env)->GetFieldID(env, GdkImageFc.clazz, "height", "I");
	GdkImageFc.depth = (*env)->GetFieldID(env, GdkImageFc.clazz, "depth", "S");
	GdkImageFc.bpp = (*env)->GetFieldID(env, GdkImageFc.clazz, "bpp", "S");
	GdkImageFc.bpl = (*env)->GetFieldID(env, GdkImageFc.clazz, "bpl", "S");
	GdkImageFc.bits_per_pixel = (*env)->GetFieldID(env, GdkImageFc.clazz, "bits_per_pixel", "S");
	GdkImageFc.mem = (*env)->GetFieldID(env, GdkImageFc.clazz, "mem", I_J);
	GdkImageFc.colormap = (*env)->GetFieldID(env, GdkImageFc.clazz, "colormap", I_J);
	GdkImageFc.windowing_data = (*env)->GetFieldID(env, GdkImageFc.clazz, "windowing_data", I_J);
	GdkImageFc.cached = 1;
}

GdkImage *getGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct)
{
	if (!GdkImageFc.cached) cacheGdkImageFields(env, lpObject);
	lpStruct->type = (GdkImageType)(*env)->GetIntField(env, lpObject, GdkImageFc.type);
	lpStruct->visual = (GdkVisual *)(*env)->GetIntLongField(env, lpObject, GdkImageFc.visual);
	lpStruct->byte_order = (GdkByteOrder)(*env)->GetIntField(env, lpObject, GdkImageFc.byte_order);
	lpStruct->width = (gint)(*env)->GetIntField(env, lpObject, GdkImageFc.width);
	lpStruct->height = (gint)(*env)->GetIntField(env, lpObject, GdkImageFc.height);
	lpStruct->depth = (guint16)(*env)->GetShortField(env, lpObject, GdkImageFc.depth);
	lpStruct->bpp = (guint16)(*env)->GetShortField(env, lpObject, GdkImageFc.bpp);
	lpStruct->bpl = (guint16)(*env)->GetShortField(env, lpObject, GdkImageFc.bpl);
	lpStruct->bits_per_pixel = (guint16)(*env)->GetShortField(env, lpObject, GdkImageFc.bits_per_pixel);
	lpStruct->mem = (gpointer)(*env)->GetIntLongField(env, lpObject, GdkImageFc.mem);
	lpStruct->colormap = (GdkColormap *)(*env)->GetIntLongField(env, lpObject, GdkImageFc.colormap);
	lpStruct->windowing_data = (gpointer)(*env)->GetIntLongField(env, lpObject, GdkImageFc.windowing_data);
	return lpStruct;
}

void setGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct)
{
	if (!GdkImageFc.cached) cacheGdkImageFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GdkImageFc.type, (jint)lpStruct->type);
	(*env)->SetIntLongField(env, lpObject, GdkImageFc.visual, (jintLong)lpStruct->visual);
	(*env)->SetIntField(env, lpObject, GdkImageFc.byte_order, (jint)lpStruct->byte_order);
	(*env)->SetIntField(env, lpObject, GdkImageFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, GdkImageFc.height, (jint)lpStruct->height);
	(*env)->SetShortField(env, lpObject, GdkImageFc.depth, (jshort)lpStruct->depth);
	(*env)->SetShortField(env, lpObject, GdkImageFc.bpp, (jshort)lpStruct->bpp);
	(*env)->SetShortField(env, lpObject, GdkImageFc.bpl, (jshort)lpStruct->bpl);
	(*env)->SetShortField(env, lpObject, GdkImageFc.bits_per_pixel, (jshort)lpStruct->bits_per_pixel);
	(*env)->SetIntLongField(env, lpObject, GdkImageFc.mem, (jintLong)lpStruct->mem);
	(*env)->SetIntLongField(env, lpObject, GdkImageFc.colormap, (jintLong)lpStruct->colormap);
	(*env)->SetIntLongField(env, lpObject, GdkImageFc.windowing_data, (jintLong)lpStruct->windowing_data);
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
	GdkWindowAttrFc.title = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "title", I_J);
	GdkWindowAttrFc.event_mask = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "event_mask", "I");
	GdkWindowAttrFc.x = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "x", "I");
	GdkWindowAttrFc.y = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "y", "I");
	GdkWindowAttrFc.width = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "width", "I");
	GdkWindowAttrFc.height = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "height", "I");
	GdkWindowAttrFc.wclass = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "wclass", "I");
	GdkWindowAttrFc.visual = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "visual", I_J);
	GdkWindowAttrFc.colormap = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "colormap", I_J);
	GdkWindowAttrFc.window_type = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "window_type", "I");
	GdkWindowAttrFc.cursor = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "cursor", I_J);
	GdkWindowAttrFc.wmclass_name = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "wmclass_name", I_J);
	GdkWindowAttrFc.wmclass_class = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "wmclass_class", I_J);
	GdkWindowAttrFc.override_redirect = (*env)->GetFieldID(env, GdkWindowAttrFc.clazz, "override_redirect", "Z");
	GdkWindowAttrFc.cached = 1;
}

GdkWindowAttr *getGdkWindowAttrFields(JNIEnv *env, jobject lpObject, GdkWindowAttr *lpStruct)
{
	if (!GdkWindowAttrFc.cached) cacheGdkWindowAttrFields(env, lpObject);
	lpStruct->title = (gchar *)(*env)->GetIntLongField(env, lpObject, GdkWindowAttrFc.title);
	lpStruct->event_mask = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.event_mask);
	lpStruct->x = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.y);
	lpStruct->width = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.width);
	lpStruct->height = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.height);
	lpStruct->wclass = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.wclass);
	lpStruct->visual = (GdkVisual *)(*env)->GetIntLongField(env, lpObject, GdkWindowAttrFc.visual);
	lpStruct->colormap = (GdkColormap *)(*env)->GetIntLongField(env, lpObject, GdkWindowAttrFc.colormap);
	lpStruct->window_type = (*env)->GetIntField(env, lpObject, GdkWindowAttrFc.window_type);
	lpStruct->cursor = (GdkCursor *)(*env)->GetIntLongField(env, lpObject, GdkWindowAttrFc.cursor);
	lpStruct->wmclass_name = (gchar *)(*env)->GetIntLongField(env, lpObject, GdkWindowAttrFc.wmclass_name);
	lpStruct->wmclass_class = (gchar *)(*env)->GetIntLongField(env, lpObject, GdkWindowAttrFc.wmclass_class);
	lpStruct->override_redirect = (*env)->GetBooleanField(env, lpObject, GdkWindowAttrFc.override_redirect);
	return lpStruct;
}

void setGdkWindowAttrFields(JNIEnv *env, jobject lpObject, GdkWindowAttr *lpStruct)
{
	if (!GdkWindowAttrFc.cached) cacheGdkWindowAttrFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, GdkWindowAttrFc.title, (jintLong)lpStruct->title);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.event_mask, (jint)lpStruct->event_mask);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.width, (jint)lpStruct->width);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.height, (jint)lpStruct->height);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.wclass, (jint)lpStruct->wclass);
	(*env)->SetIntLongField(env, lpObject, GdkWindowAttrFc.visual, (jintLong)lpStruct->visual);
	(*env)->SetIntLongField(env, lpObject, GdkWindowAttrFc.colormap, (jintLong)lpStruct->colormap);
	(*env)->SetIntField(env, lpObject, GdkWindowAttrFc.window_type, (jint)lpStruct->window_type);
	(*env)->SetIntLongField(env, lpObject, GdkWindowAttrFc.cursor, (jintLong)lpStruct->cursor);
	(*env)->SetIntLongField(env, lpObject, GdkWindowAttrFc.wmclass_name, (jintLong)lpStruct->wmclass_name);
	(*env)->SetIntLongField(env, lpObject, GdkWindowAttrFc.wmclass_class, (jintLong)lpStruct->wmclass_class);
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

#ifndef NO_GtkBorder
typedef struct GtkBorder_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID left, right, top, bottom;
} GtkBorder_FID_CACHE;

GtkBorder_FID_CACHE GtkBorderFc;

void cacheGtkBorderFields(JNIEnv *env, jobject lpObject)
{
	if (GtkBorderFc.cached) return;
	GtkBorderFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkBorderFc.left = (*env)->GetFieldID(env, GtkBorderFc.clazz, "left", "I");
	GtkBorderFc.right = (*env)->GetFieldID(env, GtkBorderFc.clazz, "right", "I");
	GtkBorderFc.top = (*env)->GetFieldID(env, GtkBorderFc.clazz, "top", "I");
	GtkBorderFc.bottom = (*env)->GetFieldID(env, GtkBorderFc.clazz, "bottom", "I");
	GtkBorderFc.cached = 1;
}

GtkBorder *getGtkBorderFields(JNIEnv *env, jobject lpObject, GtkBorder *lpStruct)
{
	if (!GtkBorderFc.cached) cacheGtkBorderFields(env, lpObject);
	lpStruct->left = (*env)->GetIntField(env, lpObject, GtkBorderFc.left);
	lpStruct->right = (*env)->GetIntField(env, lpObject, GtkBorderFc.right);
	lpStruct->top = (*env)->GetIntField(env, lpObject, GtkBorderFc.top);
	lpStruct->bottom = (*env)->GetIntField(env, lpObject, GtkBorderFc.bottom);
	return lpStruct;
}

void setGtkBorderFields(JNIEnv *env, jobject lpObject, GtkBorder *lpStruct)
{
	if (!GtkBorderFc.cached) cacheGtkBorderFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, GtkBorderFc.left, (jint)lpStruct->left);
	(*env)->SetIntField(env, lpObject, GtkBorderFc.right, (jint)lpStruct->right);
	(*env)->SetIntField(env, lpObject, GtkBorderFc.top, (jint)lpStruct->top);
	(*env)->SetIntField(env, lpObject, GtkBorderFc.bottom, (jint)lpStruct->bottom);
}
#endif

#ifndef NO_GtkCellRendererClass
typedef struct GtkCellRendererClass_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID render, get_size;
} GtkCellRendererClass_FID_CACHE;

GtkCellRendererClass_FID_CACHE GtkCellRendererClassFc;

void cacheGtkCellRendererClassFields(JNIEnv *env, jobject lpObject)
{
	if (GtkCellRendererClassFc.cached) return;
	GtkCellRendererClassFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkCellRendererClassFc.render = (*env)->GetFieldID(env, GtkCellRendererClassFc.clazz, "render", I_J);
	GtkCellRendererClassFc.get_size = (*env)->GetFieldID(env, GtkCellRendererClassFc.clazz, "get_size", I_J);
	GtkCellRendererClassFc.cached = 1;
}

GtkCellRendererClass *getGtkCellRendererClassFields(JNIEnv *env, jobject lpObject, GtkCellRendererClass *lpStruct)
{
	if (!GtkCellRendererClassFc.cached) cacheGtkCellRendererClassFields(env, lpObject);
	lpStruct->render = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkCellRendererClassFc.render);
	lpStruct->get_size = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkCellRendererClassFc.get_size);
	return lpStruct;
}

void setGtkCellRendererClassFields(JNIEnv *env, jobject lpObject, GtkCellRendererClass *lpStruct)
{
	if (!GtkCellRendererClassFc.cached) cacheGtkCellRendererClassFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, GtkCellRendererClassFc.render, (jintLong)lpStruct->render);
	(*env)->SetIntLongField(env, lpObject, GtkCellRendererClassFc.get_size, (jintLong)lpStruct->get_size);
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
	GtkColorSelectionDialogFc.colorsel = (*env)->GetFieldID(env, GtkColorSelectionDialogFc.clazz, "colorsel", I_J);
	GtkColorSelectionDialogFc.ok_button = (*env)->GetFieldID(env, GtkColorSelectionDialogFc.clazz, "ok_button", I_J);
	GtkColorSelectionDialogFc.cancel_button = (*env)->GetFieldID(env, GtkColorSelectionDialogFc.clazz, "cancel_button", I_J);
	GtkColorSelectionDialogFc.help_button = (*env)->GetFieldID(env, GtkColorSelectionDialogFc.clazz, "help_button", I_J);
	GtkColorSelectionDialogFc.cached = 1;
}

GtkColorSelectionDialog *getGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct)
{
	if (!GtkColorSelectionDialogFc.cached) cacheGtkColorSelectionDialogFields(env, lpObject);
	lpStruct->colorsel = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkColorSelectionDialogFc.colorsel);
	lpStruct->ok_button = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkColorSelectionDialogFc.ok_button);
	lpStruct->cancel_button = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkColorSelectionDialogFc.cancel_button);
	lpStruct->help_button = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkColorSelectionDialogFc.help_button);
	return lpStruct;
}

void setGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct)
{
	if (!GtkColorSelectionDialogFc.cached) cacheGtkColorSelectionDialogFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, GtkColorSelectionDialogFc.colorsel, (jintLong)lpStruct->colorsel);
	(*env)->SetIntLongField(env, lpObject, GtkColorSelectionDialogFc.ok_button, (jintLong)lpStruct->ok_button);
	(*env)->SetIntLongField(env, lpObject, GtkColorSelectionDialogFc.cancel_button, (jintLong)lpStruct->cancel_button);
	(*env)->SetIntLongField(env, lpObject, GtkColorSelectionDialogFc.help_button, (jintLong)lpStruct->help_button);
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
	GtkComboFc.entry = (*env)->GetFieldID(env, GtkComboFc.clazz, "entry", I_J);
	GtkComboFc.list = (*env)->GetFieldID(env, GtkComboFc.clazz, "list", I_J);
	GtkComboFc.cached = 1;
}

GtkCombo *getGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct)
{
	if (!GtkComboFc.cached) cacheGtkComboFields(env, lpObject);
	lpStruct->entry = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkComboFc.entry);
	lpStruct->list = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkComboFc.list);
	return lpStruct;
}

void setGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct)
{
	if (!GtkComboFc.cached) cacheGtkComboFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, GtkComboFc.entry, (jintLong)lpStruct->entry);
	(*env)->SetIntLongField(env, lpObject, GtkComboFc.list, (jintLong)lpStruct->list);
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
	GtkFileSelectionFc.dir_list = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "dir_list", I_J);
	GtkFileSelectionFc.file_list = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "file_list", I_J);
	GtkFileSelectionFc.selection_entry = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "selection_entry", I_J);
	GtkFileSelectionFc.selection_text = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "selection_text", I_J);
	GtkFileSelectionFc.main_vbox = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "main_vbox", I_J);
	GtkFileSelectionFc.ok_button = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "ok_button", I_J);
	GtkFileSelectionFc.cancel_button = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "cancel_button", I_J);
	GtkFileSelectionFc.help_button = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "help_button", I_J);
	GtkFileSelectionFc.history_pulldown = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "history_pulldown", I_J);
	GtkFileSelectionFc.history_menu = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "history_menu", I_J);
	GtkFileSelectionFc.history_list = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "history_list", I_J);
	GtkFileSelectionFc.fileop_dialog = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_dialog", I_J);
	GtkFileSelectionFc.fileop_entry = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_entry", I_J);
	GtkFileSelectionFc.fileop_file = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_file", I_J);
	GtkFileSelectionFc.cmpl_state = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "cmpl_state", I_J);
	GtkFileSelectionFc.fileop_c_dir = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_c_dir", I_J);
	GtkFileSelectionFc.fileop_del_file = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_del_file", I_J);
	GtkFileSelectionFc.fileop_ren_file = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "fileop_ren_file", I_J);
	GtkFileSelectionFc.button_area = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "button_area", I_J);
	GtkFileSelectionFc.action_area = (*env)->GetFieldID(env, GtkFileSelectionFc.clazz, "action_area", I_J);
	GtkFileSelectionFc.cached = 1;
}

GtkFileSelection *getGtkFileSelectionFields(JNIEnv *env, jobject lpObject, GtkFileSelection *lpStruct)
{
	if (!GtkFileSelectionFc.cached) cacheGtkFileSelectionFields(env, lpObject);
	lpStruct->dir_list = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.dir_list);
	lpStruct->file_list = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.file_list);
	lpStruct->selection_entry = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.selection_entry);
	lpStruct->selection_text = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.selection_text);
	lpStruct->main_vbox = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.main_vbox);
	lpStruct->ok_button = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.ok_button);
	lpStruct->cancel_button = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.cancel_button);
	lpStruct->help_button = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.help_button);
	lpStruct->history_pulldown = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.history_pulldown);
	lpStruct->history_menu = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.history_menu);
	lpStruct->history_list = (GList *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.history_list);
	lpStruct->fileop_dialog = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.fileop_dialog);
	lpStruct->fileop_entry = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.fileop_entry);
	lpStruct->fileop_file = (gchar *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.fileop_file);
	lpStruct->cmpl_state = (gpointer)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.cmpl_state);
	lpStruct->fileop_c_dir = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.fileop_c_dir);
	lpStruct->fileop_del_file = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.fileop_del_file);
	lpStruct->fileop_ren_file = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.fileop_ren_file);
	lpStruct->button_area = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.button_area);
	lpStruct->action_area = (GtkWidget *)(*env)->GetIntLongField(env, lpObject, GtkFileSelectionFc.action_area);
	return lpStruct;
}

void setGtkFileSelectionFields(JNIEnv *env, jobject lpObject, GtkFileSelection *lpStruct)
{
	if (!GtkFileSelectionFc.cached) cacheGtkFileSelectionFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.dir_list, (jintLong)lpStruct->dir_list);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.file_list, (jintLong)lpStruct->file_list);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.selection_entry, (jintLong)lpStruct->selection_entry);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.selection_text, (jintLong)lpStruct->selection_text);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.main_vbox, (jintLong)lpStruct->main_vbox);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.ok_button, (jintLong)lpStruct->ok_button);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.cancel_button, (jintLong)lpStruct->cancel_button);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.help_button, (jintLong)lpStruct->help_button);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.history_pulldown, (jintLong)lpStruct->history_pulldown);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.history_menu, (jintLong)lpStruct->history_menu);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.history_list, (jintLong)lpStruct->history_list);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.fileop_dialog, (jintLong)lpStruct->fileop_dialog);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.fileop_entry, (jintLong)lpStruct->fileop_entry);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.fileop_file, (jintLong)lpStruct->fileop_file);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.cmpl_state, (jintLong)lpStruct->cmpl_state);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.fileop_c_dir, (jintLong)lpStruct->fileop_c_dir);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.fileop_del_file, (jintLong)lpStruct->fileop_del_file);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.fileop_ren_file, (jintLong)lpStruct->fileop_ren_file);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.button_area, (jintLong)lpStruct->button_area);
	(*env)->SetIntLongField(env, lpObject, GtkFileSelectionFc.action_area, (jintLong)lpStruct->action_area);
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
	GtkFixedFc.children = (*env)->GetFieldID(env, GtkFixedFc.clazz, "children", I_J);
	GtkFixedFc.cached = 1;
}

GtkFixed *getGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct)
{
	if (!GtkFixedFc.cached) cacheGtkFixedFields(env, lpObject);
	lpStruct->children = (GList *)(*env)->GetIntLongField(env, lpObject, GtkFixedFc.children);
	return lpStruct;
}

void setGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct)
{
	if (!GtkFixedFc.cached) cacheGtkFixedFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, GtkFixedFc.children, (jintLong)lpStruct->children);
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
	GtkSelectionDataFc.selection = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "selection", I_J);
	GtkSelectionDataFc.target = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "target", I_J);
	GtkSelectionDataFc.type = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "type", I_J);
	GtkSelectionDataFc.format = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "format", "I");
	GtkSelectionDataFc.data = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "data", I_J);
	GtkSelectionDataFc.length = (*env)->GetFieldID(env, GtkSelectionDataFc.clazz, "length", "I");
	GtkSelectionDataFc.cached = 1;
}

GtkSelectionData *getGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct)
{
	if (!GtkSelectionDataFc.cached) cacheGtkSelectionDataFields(env, lpObject);
	lpStruct->selection = (GdkAtom)(*env)->GetIntLongField(env, lpObject, GtkSelectionDataFc.selection);
	lpStruct->target = (GdkAtom)(*env)->GetIntLongField(env, lpObject, GtkSelectionDataFc.target);
	lpStruct->type = (GdkAtom)(*env)->GetIntLongField(env, lpObject, GtkSelectionDataFc.type);
	lpStruct->format = (gint)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.format);
	lpStruct->data = (guchar *)(*env)->GetIntLongField(env, lpObject, GtkSelectionDataFc.data);
	lpStruct->length = (gint)(*env)->GetIntField(env, lpObject, GtkSelectionDataFc.length);
	return lpStruct;
}

void setGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct)
{
	if (!GtkSelectionDataFc.cached) cacheGtkSelectionDataFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, GtkSelectionDataFc.selection, (jintLong)lpStruct->selection);
	(*env)->SetIntLongField(env, lpObject, GtkSelectionDataFc.target, (jintLong)lpStruct->target);
	(*env)->SetIntLongField(env, lpObject, GtkSelectionDataFc.type, (jintLong)lpStruct->type);
	(*env)->SetIntField(env, lpObject, GtkSelectionDataFc.format, (jint)lpStruct->format);
	(*env)->SetIntLongField(env, lpObject, GtkSelectionDataFc.data, (jintLong)lpStruct->data);
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
	GtkTargetEntryFc.target = (*env)->GetFieldID(env, GtkTargetEntryFc.clazz, "target", I_J);
	GtkTargetEntryFc.flags = (*env)->GetFieldID(env, GtkTargetEntryFc.clazz, "flags", "I");
	GtkTargetEntryFc.info = (*env)->GetFieldID(env, GtkTargetEntryFc.clazz, "info", "I");
	GtkTargetEntryFc.cached = 1;
}

GtkTargetEntry *getGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct)
{
	if (!GtkTargetEntryFc.cached) cacheGtkTargetEntryFields(env, lpObject);
	lpStruct->target = (gchar *)(*env)->GetIntLongField(env, lpObject, GtkTargetEntryFc.target);
	lpStruct->flags = (guint)(*env)->GetIntField(env, lpObject, GtkTargetEntryFc.flags);
	lpStruct->info = (guint)(*env)->GetIntField(env, lpObject, GtkTargetEntryFc.info);
	return lpStruct;
}

void setGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct)
{
	if (!GtkTargetEntryFc.cached) cacheGtkTargetEntryFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, GtkTargetEntryFc.target, (jintLong)lpStruct->target);
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
	GtkTargetPairFc.target = (*env)->GetFieldID(env, GtkTargetPairFc.clazz, "target", I_J);
	GtkTargetPairFc.flags = (*env)->GetFieldID(env, GtkTargetPairFc.clazz, "flags", "I");
	GtkTargetPairFc.info = (*env)->GetFieldID(env, GtkTargetPairFc.clazz, "info", "I");
	GtkTargetPairFc.cached = 1;
}

GtkTargetPair *getGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct)
{
	if (!GtkTargetPairFc.cached) cacheGtkTargetPairFields(env, lpObject);
	lpStruct->target = (GdkAtom)(*env)->GetIntLongField(env, lpObject, GtkTargetPairFc.target);
	lpStruct->flags = (guint)(*env)->GetIntField(env, lpObject, GtkTargetPairFc.flags);
	lpStruct->info = (guint)(*env)->GetIntField(env, lpObject, GtkTargetPairFc.info);
	return lpStruct;
}

void setGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct)
{
	if (!GtkTargetPairFc.cached) cacheGtkTargetPairFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, GtkTargetPairFc.target, (jintLong)lpStruct->target);
	(*env)->SetIntField(env, lpObject, GtkTargetPairFc.flags, (jint)lpStruct->flags);
	(*env)->SetIntField(env, lpObject, GtkTargetPairFc.info, (jint)lpStruct->info);
}
#endif

#ifndef NO_GtkWidgetClass
typedef struct GtkWidgetClass_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID activate_signal, set_scroll_adjustments_signal, dispatch_child_properties_changed, show, show_all, hide, hide_all, map, unmap, realize, unrealize, size_request, size_allocate, state_changed, parent_set, hierarchy_changed, style_set, direction_changed, grab_notify, child_notify, mnemonic_activate, grab_focus, focus, event, button_press_event, button_release_event, scroll_event, motion_notify_event, delete_event, destroy_event, expose_event, key_press_event, key_release_event, enter_notify_event, leave_notify_event, configure_event, focus_in_event, focus_out_event, map_event, unmap_event, property_notify_event, selection_clear_event, selection_request_event, selection_notify_event, proximity_in_event, proximity_out_event, visibility_notify_event, client_event, no_expose_event, window_state_event, selection_get, selection_received, drag_begin, drag_end, drag_data_get, drag_data_delete, drag_leave, drag_motion, drag_drop, drag_data_received, popup_menu, show_help, get_accessible, screen_changed;
} GtkWidgetClass_FID_CACHE;

GtkWidgetClass_FID_CACHE GtkWidgetClassFc;

void cacheGtkWidgetClassFields(JNIEnv *env, jobject lpObject)
{
	if (GtkWidgetClassFc.cached) return;
	cacheGObjectClassFields(env, lpObject);
	GtkWidgetClassFc.clazz = (*env)->GetObjectClass(env, lpObject);
	GtkWidgetClassFc.activate_signal = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "activate_signal", "I");
	GtkWidgetClassFc.set_scroll_adjustments_signal = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "set_scroll_adjustments_signal", "I");
	GtkWidgetClassFc.dispatch_child_properties_changed = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "dispatch_child_properties_changed", I_J);
	GtkWidgetClassFc.show = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "show", I_J);
	GtkWidgetClassFc.show_all = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "show_all", I_J);
	GtkWidgetClassFc.hide = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "hide", I_J);
	GtkWidgetClassFc.hide_all = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "hide_all", I_J);
	GtkWidgetClassFc.map = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "map", I_J);
	GtkWidgetClassFc.unmap = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "unmap", I_J);
	GtkWidgetClassFc.realize = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "realize", I_J);
	GtkWidgetClassFc.unrealize = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "unrealize", I_J);
	GtkWidgetClassFc.size_request = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "size_request", I_J);
	GtkWidgetClassFc.size_allocate = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "size_allocate", I_J);
	GtkWidgetClassFc.state_changed = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "state_changed", I_J);
	GtkWidgetClassFc.parent_set = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "parent_set", I_J);
	GtkWidgetClassFc.hierarchy_changed = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "hierarchy_changed", I_J);
	GtkWidgetClassFc.style_set = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "style_set", I_J);
	GtkWidgetClassFc.direction_changed = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "direction_changed", I_J);
	GtkWidgetClassFc.grab_notify = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "grab_notify", I_J);
	GtkWidgetClassFc.child_notify = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "child_notify", I_J);
	GtkWidgetClassFc.mnemonic_activate = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "mnemonic_activate", I_J);
	GtkWidgetClassFc.grab_focus = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "grab_focus", I_J);
	GtkWidgetClassFc.focus = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "focus", I_J);
	GtkWidgetClassFc.event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "event", I_J);
	GtkWidgetClassFc.button_press_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "button_press_event", I_J);
	GtkWidgetClassFc.button_release_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "button_release_event", I_J);
	GtkWidgetClassFc.scroll_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "scroll_event", I_J);
	GtkWidgetClassFc.motion_notify_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "motion_notify_event", I_J);
	GtkWidgetClassFc.delete_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "delete_event", I_J);
	GtkWidgetClassFc.destroy_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "destroy_event", I_J);
	GtkWidgetClassFc.expose_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "expose_event", I_J);
	GtkWidgetClassFc.key_press_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "key_press_event", I_J);
	GtkWidgetClassFc.key_release_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "key_release_event", I_J);
	GtkWidgetClassFc.enter_notify_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "enter_notify_event", I_J);
	GtkWidgetClassFc.leave_notify_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "leave_notify_event", I_J);
	GtkWidgetClassFc.configure_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "configure_event", I_J);
	GtkWidgetClassFc.focus_in_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "focus_in_event", I_J);
	GtkWidgetClassFc.focus_out_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "focus_out_event", I_J);
	GtkWidgetClassFc.map_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "map_event", I_J);
	GtkWidgetClassFc.unmap_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "unmap_event", I_J);
	GtkWidgetClassFc.property_notify_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "property_notify_event", I_J);
	GtkWidgetClassFc.selection_clear_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "selection_clear_event", I_J);
	GtkWidgetClassFc.selection_request_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "selection_request_event", I_J);
	GtkWidgetClassFc.selection_notify_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "selection_notify_event", I_J);
	GtkWidgetClassFc.proximity_in_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "proximity_in_event", I_J);
	GtkWidgetClassFc.proximity_out_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "proximity_out_event", I_J);
	GtkWidgetClassFc.visibility_notify_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "visibility_notify_event", I_J);
	GtkWidgetClassFc.client_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "client_event", I_J);
	GtkWidgetClassFc.no_expose_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "no_expose_event", I_J);
	GtkWidgetClassFc.window_state_event = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "window_state_event", I_J);
	GtkWidgetClassFc.selection_get = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "selection_get", I_J);
	GtkWidgetClassFc.selection_received = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "selection_received", I_J);
	GtkWidgetClassFc.drag_begin = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "drag_begin", I_J);
	GtkWidgetClassFc.drag_end = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "drag_end", I_J);
	GtkWidgetClassFc.drag_data_get = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "drag_data_get", I_J);
	GtkWidgetClassFc.drag_data_delete = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "drag_data_delete", I_J);
	GtkWidgetClassFc.drag_leave = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "drag_leave", I_J);
	GtkWidgetClassFc.drag_motion = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "drag_motion", I_J);
	GtkWidgetClassFc.drag_drop = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "drag_drop", I_J);
	GtkWidgetClassFc.drag_data_received = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "drag_data_received", I_J);
	GtkWidgetClassFc.popup_menu = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "popup_menu", I_J);
	GtkWidgetClassFc.show_help = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "show_help", I_J);
	GtkWidgetClassFc.get_accessible = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "get_accessible", I_J);
	GtkWidgetClassFc.screen_changed = (*env)->GetFieldID(env, GtkWidgetClassFc.clazz, "screen_changed", I_J);
	GtkWidgetClassFc.cached = 1;
}

GtkWidgetClass *getGtkWidgetClassFields(JNIEnv *env, jobject lpObject, GtkWidgetClass *lpStruct)
{
	if (!GtkWidgetClassFc.cached) cacheGtkWidgetClassFields(env, lpObject);
	getGObjectClassFields(env, lpObject, (GObjectClass *)lpStruct);
	lpStruct->activate_signal = (*env)->GetIntField(env, lpObject, GtkWidgetClassFc.activate_signal);
	lpStruct->set_scroll_adjustments_signal = (*env)->GetIntField(env, lpObject, GtkWidgetClassFc.set_scroll_adjustments_signal);
	lpStruct->dispatch_child_properties_changed = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.dispatch_child_properties_changed);
	lpStruct->show = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.show);
	lpStruct->show_all = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.show_all);
	lpStruct->hide = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.hide);
	lpStruct->hide_all = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.hide_all);
	lpStruct->map = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.map);
	lpStruct->unmap = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.unmap);
	lpStruct->realize = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.realize);
	lpStruct->unrealize = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.unrealize);
	lpStruct->size_request = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.size_request);
	lpStruct->size_allocate = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.size_allocate);
	lpStruct->state_changed = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.state_changed);
	lpStruct->parent_set = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.parent_set);
	lpStruct->hierarchy_changed = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.hierarchy_changed);
	lpStruct->style_set = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.style_set);
	lpStruct->direction_changed = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.direction_changed);
	lpStruct->grab_notify = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.grab_notify);
	lpStruct->child_notify = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.child_notify);
	lpStruct->mnemonic_activate = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.mnemonic_activate);
	lpStruct->grab_focus = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.grab_focus);
	lpStruct->focus = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.focus);
	lpStruct->event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.event);
	lpStruct->button_press_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.button_press_event);
	lpStruct->button_release_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.button_release_event);
	lpStruct->scroll_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.scroll_event);
	lpStruct->motion_notify_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.motion_notify_event);
	lpStruct->delete_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.delete_event);
	lpStruct->destroy_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.destroy_event);
	lpStruct->expose_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.expose_event);
	lpStruct->key_press_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.key_press_event);
	lpStruct->key_release_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.key_release_event);
	lpStruct->enter_notify_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.enter_notify_event);
	lpStruct->leave_notify_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.leave_notify_event);
	lpStruct->configure_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.configure_event);
	lpStruct->focus_in_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.focus_in_event);
	lpStruct->focus_out_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.focus_out_event);
	lpStruct->map_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.map_event);
	lpStruct->unmap_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.unmap_event);
	lpStruct->property_notify_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.property_notify_event);
	lpStruct->selection_clear_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.selection_clear_event);
	lpStruct->selection_request_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.selection_request_event);
	lpStruct->selection_notify_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.selection_notify_event);
	lpStruct->proximity_in_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.proximity_in_event);
	lpStruct->proximity_out_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.proximity_out_event);
	lpStruct->visibility_notify_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.visibility_notify_event);
	lpStruct->client_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.client_event);
	lpStruct->no_expose_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.no_expose_event);
	lpStruct->window_state_event = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.window_state_event);
	lpStruct->selection_get = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.selection_get);
	lpStruct->selection_received = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.selection_received);
	lpStruct->drag_begin = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.drag_begin);
	lpStruct->drag_end = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.drag_end);
	lpStruct->drag_data_get = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.drag_data_get);
	lpStruct->drag_data_delete = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.drag_data_delete);
	lpStruct->drag_leave = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.drag_leave);
	lpStruct->drag_motion = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.drag_motion);
	lpStruct->drag_drop = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.drag_drop);
	lpStruct->drag_data_received = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.drag_data_received);
	lpStruct->popup_menu = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.popup_menu);
	lpStruct->show_help = (gboolean(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.show_help);
	lpStruct->get_accessible = (AtkObject*(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.get_accessible);
	lpStruct->screen_changed = (void(*)())(*env)->GetIntLongField(env, lpObject, GtkWidgetClassFc.screen_changed);
	return lpStruct;
}

void setGtkWidgetClassFields(JNIEnv *env, jobject lpObject, GtkWidgetClass *lpStruct)
{
	if (!GtkWidgetClassFc.cached) cacheGtkWidgetClassFields(env, lpObject);
	setGObjectClassFields(env, lpObject, (GObjectClass *)lpStruct);
	(*env)->SetIntField(env, lpObject, GtkWidgetClassFc.activate_signal, (jint)lpStruct->activate_signal);
	(*env)->SetIntField(env, lpObject, GtkWidgetClassFc.set_scroll_adjustments_signal, (jint)lpStruct->set_scroll_adjustments_signal);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.dispatch_child_properties_changed, (jintLong)lpStruct->dispatch_child_properties_changed);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.show, (jintLong)lpStruct->show);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.show_all, (jintLong)lpStruct->show_all);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.hide, (jintLong)lpStruct->hide);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.hide_all, (jintLong)lpStruct->hide_all);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.map, (jintLong)lpStruct->map);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.unmap, (jintLong)lpStruct->unmap);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.realize, (jintLong)lpStruct->realize);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.unrealize, (jintLong)lpStruct->unrealize);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.size_request, (jintLong)lpStruct->size_request);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.size_allocate, (jintLong)lpStruct->size_allocate);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.state_changed, (jintLong)lpStruct->state_changed);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.parent_set, (jintLong)lpStruct->parent_set);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.hierarchy_changed, (jintLong)lpStruct->hierarchy_changed);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.style_set, (jintLong)lpStruct->style_set);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.direction_changed, (jintLong)lpStruct->direction_changed);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.grab_notify, (jintLong)lpStruct->grab_notify);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.child_notify, (jintLong)lpStruct->child_notify);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.mnemonic_activate, (jintLong)lpStruct->mnemonic_activate);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.grab_focus, (jintLong)lpStruct->grab_focus);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.focus, (jintLong)lpStruct->focus);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.event, (jintLong)lpStruct->event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.button_press_event, (jintLong)lpStruct->button_press_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.button_release_event, (jintLong)lpStruct->button_release_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.scroll_event, (jintLong)lpStruct->scroll_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.motion_notify_event, (jintLong)lpStruct->motion_notify_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.delete_event, (jintLong)lpStruct->delete_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.destroy_event, (jintLong)lpStruct->destroy_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.expose_event, (jintLong)lpStruct->expose_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.key_press_event, (jintLong)lpStruct->key_press_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.key_release_event, (jintLong)lpStruct->key_release_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.enter_notify_event, (jintLong)lpStruct->enter_notify_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.leave_notify_event, (jintLong)lpStruct->leave_notify_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.configure_event, (jintLong)lpStruct->configure_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.focus_in_event, (jintLong)lpStruct->focus_in_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.focus_out_event, (jintLong)lpStruct->focus_out_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.map_event, (jintLong)lpStruct->map_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.unmap_event, (jintLong)lpStruct->unmap_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.property_notify_event, (jintLong)lpStruct->property_notify_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.selection_clear_event, (jintLong)lpStruct->selection_clear_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.selection_request_event, (jintLong)lpStruct->selection_request_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.selection_notify_event, (jintLong)lpStruct->selection_notify_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.proximity_in_event, (jintLong)lpStruct->proximity_in_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.proximity_out_event, (jintLong)lpStruct->proximity_out_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.visibility_notify_event, (jintLong)lpStruct->visibility_notify_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.client_event, (jintLong)lpStruct->client_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.no_expose_event, (jintLong)lpStruct->no_expose_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.window_state_event, (jintLong)lpStruct->window_state_event);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.selection_get, (jintLong)lpStruct->selection_get);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.selection_received, (jintLong)lpStruct->selection_received);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.drag_begin, (jintLong)lpStruct->drag_begin);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.drag_end, (jintLong)lpStruct->drag_end);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.drag_data_get, (jintLong)lpStruct->drag_data_get);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.drag_data_delete, (jintLong)lpStruct->drag_data_delete);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.drag_leave, (jintLong)lpStruct->drag_leave);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.drag_motion, (jintLong)lpStruct->drag_motion);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.drag_drop, (jintLong)lpStruct->drag_drop);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.drag_data_received, (jintLong)lpStruct->drag_data_received);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.popup_menu, (jintLong)lpStruct->popup_menu);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.show_help, (jintLong)lpStruct->show_help);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.get_accessible, (jintLong)lpStruct->get_accessible);
	(*env)->SetIntLongField(env, lpObject, GtkWidgetClassFc.screen_changed, (jintLong)lpStruct->screen_changed);
}
#endif

#ifndef NO_PangoAttrColor
typedef struct PangoAttrColor_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID color_red, color_green, color_blue;
} PangoAttrColor_FID_CACHE;

PangoAttrColor_FID_CACHE PangoAttrColorFc;

void cachePangoAttrColorFields(JNIEnv *env, jobject lpObject)
{
	if (PangoAttrColorFc.cached) return;
	cachePangoAttributeFields(env, lpObject);
	PangoAttrColorFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PangoAttrColorFc.color_red = (*env)->GetFieldID(env, PangoAttrColorFc.clazz, "color_red", "S");
	PangoAttrColorFc.color_green = (*env)->GetFieldID(env, PangoAttrColorFc.clazz, "color_green", "S");
	PangoAttrColorFc.color_blue = (*env)->GetFieldID(env, PangoAttrColorFc.clazz, "color_blue", "S");
	PangoAttrColorFc.cached = 1;
}

PangoAttrColor *getPangoAttrColorFields(JNIEnv *env, jobject lpObject, PangoAttrColor *lpStruct)
{
	if (!PangoAttrColorFc.cached) cachePangoAttrColorFields(env, lpObject);
	getPangoAttributeFields(env, lpObject, (PangoAttribute *)lpStruct);
	lpStruct->color.red = (*env)->GetShortField(env, lpObject, PangoAttrColorFc.color_red);
	lpStruct->color.green = (*env)->GetShortField(env, lpObject, PangoAttrColorFc.color_green);
	lpStruct->color.blue = (*env)->GetShortField(env, lpObject, PangoAttrColorFc.color_blue);
	return lpStruct;
}

void setPangoAttrColorFields(JNIEnv *env, jobject lpObject, PangoAttrColor *lpStruct)
{
	if (!PangoAttrColorFc.cached) cachePangoAttrColorFields(env, lpObject);
	setPangoAttributeFields(env, lpObject, (PangoAttribute *)lpStruct);
	(*env)->SetShortField(env, lpObject, PangoAttrColorFc.color_red, (jshort)lpStruct->color.red);
	(*env)->SetShortField(env, lpObject, PangoAttrColorFc.color_green, (jshort)lpStruct->color.green);
	(*env)->SetShortField(env, lpObject, PangoAttrColorFc.color_blue, (jshort)lpStruct->color.blue);
}
#endif

#ifndef NO_PangoAttrInt
typedef struct PangoAttrInt_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID value;
} PangoAttrInt_FID_CACHE;

PangoAttrInt_FID_CACHE PangoAttrIntFc;

void cachePangoAttrIntFields(JNIEnv *env, jobject lpObject)
{
	if (PangoAttrIntFc.cached) return;
	cachePangoAttributeFields(env, lpObject);
	PangoAttrIntFc.clazz = (*env)->GetObjectClass(env, lpObject);
	PangoAttrIntFc.value = (*env)->GetFieldID(env, PangoAttrIntFc.clazz, "value", "I");
	PangoAttrIntFc.cached = 1;
}

PangoAttrInt *getPangoAttrIntFields(JNIEnv *env, jobject lpObject, PangoAttrInt *lpStruct)
{
	if (!PangoAttrIntFc.cached) cachePangoAttrIntFields(env, lpObject);
	getPangoAttributeFields(env, lpObject, (PangoAttribute *)lpStruct);
	lpStruct->value = (*env)->GetIntField(env, lpObject, PangoAttrIntFc.value);
	return lpStruct;
}

void setPangoAttrIntFields(JNIEnv *env, jobject lpObject, PangoAttrInt *lpStruct)
{
	if (!PangoAttrIntFc.cached) cachePangoAttrIntFields(env, lpObject);
	setPangoAttributeFields(env, lpObject, (PangoAttribute *)lpStruct);
	(*env)->SetIntField(env, lpObject, PangoAttrIntFc.value, (jint)lpStruct->value);
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
	PangoAttributeFc.klass = (*env)->GetFieldID(env, PangoAttributeFc.clazz, "klass", I_J);
	PangoAttributeFc.start_index = (*env)->GetFieldID(env, PangoAttributeFc.clazz, "start_index", "I");
	PangoAttributeFc.end_index = (*env)->GetFieldID(env, PangoAttributeFc.clazz, "end_index", "I");
	PangoAttributeFc.cached = 1;
}

PangoAttribute *getPangoAttributeFields(JNIEnv *env, jobject lpObject, PangoAttribute *lpStruct)
{
	if (!PangoAttributeFc.cached) cachePangoAttributeFields(env, lpObject);
	lpStruct->klass = (const PangoAttrClass *)(*env)->GetIntLongField(env, lpObject, PangoAttributeFc.klass);
	lpStruct->start_index = (*env)->GetIntField(env, lpObject, PangoAttributeFc.start_index);
	lpStruct->end_index = (*env)->GetIntField(env, lpObject, PangoAttributeFc.end_index);
	return lpStruct;
}

void setPangoAttributeFields(JNIEnv *env, jobject lpObject, PangoAttribute *lpStruct)
{
	if (!PangoAttributeFc.cached) cachePangoAttributeFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, PangoAttributeFc.klass, (jintLong)lpStruct->klass);
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
	PangoItemFc.analysis_shape_engine = (*env)->GetFieldID(env, PangoItemFc.clazz, "analysis_shape_engine", I_J);
	PangoItemFc.analysis_lang_engine = (*env)->GetFieldID(env, PangoItemFc.clazz, "analysis_lang_engine", I_J);
	PangoItemFc.analysis_font = (*env)->GetFieldID(env, PangoItemFc.clazz, "analysis_font", I_J);
	PangoItemFc.analysis_level = (*env)->GetFieldID(env, PangoItemFc.clazz, "analysis_level", "B");
	PangoItemFc.analysis_language = (*env)->GetFieldID(env, PangoItemFc.clazz, "analysis_language", I_J);
	PangoItemFc.analysis_extra_attrs = (*env)->GetFieldID(env, PangoItemFc.clazz, "analysis_extra_attrs", I_J);
	PangoItemFc.cached = 1;
}

PangoItem *getPangoItemFields(JNIEnv *env, jobject lpObject, PangoItem *lpStruct)
{
	if (!PangoItemFc.cached) cachePangoItemFields(env, lpObject);
	lpStruct->offset = (*env)->GetIntField(env, lpObject, PangoItemFc.offset);
	lpStruct->length = (*env)->GetIntField(env, lpObject, PangoItemFc.length);
	lpStruct->num_chars = (*env)->GetIntField(env, lpObject, PangoItemFc.num_chars);
	lpStruct->analysis.shape_engine = (PangoEngineShape *)(*env)->GetIntLongField(env, lpObject, PangoItemFc.analysis_shape_engine);
	lpStruct->analysis.lang_engine = (PangoEngineLang *)(*env)->GetIntLongField(env, lpObject, PangoItemFc.analysis_lang_engine);
	lpStruct->analysis.font = (PangoFont *)(*env)->GetIntLongField(env, lpObject, PangoItemFc.analysis_font);
	lpStruct->analysis.level = (*env)->GetByteField(env, lpObject, PangoItemFc.analysis_level);
	lpStruct->analysis.language = (PangoLanguage *)(*env)->GetIntLongField(env, lpObject, PangoItemFc.analysis_language);
	lpStruct->analysis.extra_attrs = (GSList *)(*env)->GetIntLongField(env, lpObject, PangoItemFc.analysis_extra_attrs);
	return lpStruct;
}

void setPangoItemFields(JNIEnv *env, jobject lpObject, PangoItem *lpStruct)
{
	if (!PangoItemFc.cached) cachePangoItemFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, PangoItemFc.offset, (jint)lpStruct->offset);
	(*env)->SetIntField(env, lpObject, PangoItemFc.length, (jint)lpStruct->length);
	(*env)->SetIntField(env, lpObject, PangoItemFc.num_chars, (jint)lpStruct->num_chars);
	(*env)->SetIntLongField(env, lpObject, PangoItemFc.analysis_shape_engine, (jintLong)lpStruct->analysis.shape_engine);
	(*env)->SetIntLongField(env, lpObject, PangoItemFc.analysis_lang_engine, (jintLong)lpStruct->analysis.lang_engine);
	(*env)->SetIntLongField(env, lpObject, PangoItemFc.analysis_font, (jintLong)lpStruct->analysis.font);
	(*env)->SetByteField(env, lpObject, PangoItemFc.analysis_level, (jbyte)lpStruct->analysis.level);
	(*env)->SetIntLongField(env, lpObject, PangoItemFc.analysis_language, (jintLong)lpStruct->analysis.language);
	(*env)->SetIntLongField(env, lpObject, PangoItemFc.analysis_extra_attrs, (jintLong)lpStruct->analysis.extra_attrs);
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
	PangoLayoutLineFc.layout = (*env)->GetFieldID(env, PangoLayoutLineFc.clazz, "layout", I_J);
	PangoLayoutLineFc.start_index = (*env)->GetFieldID(env, PangoLayoutLineFc.clazz, "start_index", "I");
	PangoLayoutLineFc.length = (*env)->GetFieldID(env, PangoLayoutLineFc.clazz, "length", "I");
	PangoLayoutLineFc.runs = (*env)->GetFieldID(env, PangoLayoutLineFc.clazz, "runs", I_J);
	PangoLayoutLineFc.cached = 1;
}

PangoLayoutLine *getPangoLayoutLineFields(JNIEnv *env, jobject lpObject, PangoLayoutLine *lpStruct)
{
	if (!PangoLayoutLineFc.cached) cachePangoLayoutLineFields(env, lpObject);
	lpStruct->layout = (PangoLayout *)(*env)->GetIntLongField(env, lpObject, PangoLayoutLineFc.layout);
	lpStruct->start_index = (*env)->GetIntField(env, lpObject, PangoLayoutLineFc.start_index);
	lpStruct->length = (*env)->GetIntField(env, lpObject, PangoLayoutLineFc.length);
	lpStruct->runs = (GSList *)(*env)->GetIntLongField(env, lpObject, PangoLayoutLineFc.runs);
	return lpStruct;
}

void setPangoLayoutLineFields(JNIEnv *env, jobject lpObject, PangoLayoutLine *lpStruct)
{
	if (!PangoLayoutLineFc.cached) cachePangoLayoutLineFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, PangoLayoutLineFc.layout, (jintLong)lpStruct->layout);
	(*env)->SetIntField(env, lpObject, PangoLayoutLineFc.start_index, (jint)lpStruct->start_index);
	(*env)->SetIntField(env, lpObject, PangoLayoutLineFc.length, (jint)lpStruct->length);
	(*env)->SetIntLongField(env, lpObject, PangoLayoutLineFc.runs, (jintLong)lpStruct->runs);
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
	PangoLayoutRunFc.item = (*env)->GetFieldID(env, PangoLayoutRunFc.clazz, "item", I_J);
	PangoLayoutRunFc.glyphs = (*env)->GetFieldID(env, PangoLayoutRunFc.clazz, "glyphs", I_J);
	PangoLayoutRunFc.cached = 1;
}

PangoLayoutRun *getPangoLayoutRunFields(JNIEnv *env, jobject lpObject, PangoLayoutRun *lpStruct)
{
	if (!PangoLayoutRunFc.cached) cachePangoLayoutRunFields(env, lpObject);
	lpStruct->item = (PangoItem *)(*env)->GetIntLongField(env, lpObject, PangoLayoutRunFc.item);
	lpStruct->glyphs = (PangoGlyphString *)(*env)->GetIntLongField(env, lpObject, PangoLayoutRunFc.glyphs);
	return lpStruct;
}

void setPangoLayoutRunFields(JNIEnv *env, jobject lpObject, PangoLayoutRun *lpStruct)
{
	if (!PangoLayoutRunFc.cached) cachePangoLayoutRunFields(env, lpObject);
	(*env)->SetIntLongField(env, lpObject, PangoLayoutRunFc.item, (jintLong)lpStruct->item);
	(*env)->SetIntLongField(env, lpObject, PangoLayoutRunFc.glyphs, (jintLong)lpStruct->glyphs);
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
	XAnyEventFc.serial = (*env)->GetFieldID(env, XAnyEventFc.clazz, "serial", I_J);
	XAnyEventFc.send_event = (*env)->GetFieldID(env, XAnyEventFc.clazz, "send_event", "I");
	XAnyEventFc.display = (*env)->GetFieldID(env, XAnyEventFc.clazz, "display", I_J);
	XAnyEventFc.window = (*env)->GetFieldID(env, XAnyEventFc.clazz, "window", I_J);
	XAnyEventFc.cached = 1;
}

XAnyEvent *getXAnyEventFields(JNIEnv *env, jobject lpObject, XAnyEvent *lpStruct)
{
	if (!XAnyEventFc.cached) cacheXAnyEventFields(env, lpObject);
	getXEventFields(env, lpObject, (XEvent *)lpStruct);
	lpStruct->serial = (*env)->GetIntLongField(env, lpObject, XAnyEventFc.serial);
	lpStruct->send_event = (*env)->GetIntField(env, lpObject, XAnyEventFc.send_event);
	lpStruct->display = (Display *)(*env)->GetIntLongField(env, lpObject, XAnyEventFc.display);
	lpStruct->window = (Window)(*env)->GetIntLongField(env, lpObject, XAnyEventFc.window);
	return lpStruct;
}

void setXAnyEventFields(JNIEnv *env, jobject lpObject, XAnyEvent *lpStruct)
{
	if (!XAnyEventFc.cached) cacheXAnyEventFields(env, lpObject);
	setXEventFields(env, lpObject, (XEvent *)lpStruct);
	(*env)->SetIntLongField(env, lpObject, XAnyEventFc.serial, (jintLong)lpStruct->serial);
	(*env)->SetIntField(env, lpObject, XAnyEventFc.send_event, (jint)lpStruct->send_event);
	(*env)->SetIntLongField(env, lpObject, XAnyEventFc.display, (jintLong)lpStruct->display);
	(*env)->SetIntLongField(env, lpObject, XAnyEventFc.window, (jintLong)lpStruct->window);
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
	XClientMessageEventFc.serial = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "serial", I_J);
	XClientMessageEventFc.send_event = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "send_event", "Z");
	XClientMessageEventFc.display = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "display", I_J);
	XClientMessageEventFc.window = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "window", I_J);
	XClientMessageEventFc.message_type = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "message_type", I_J);
	XClientMessageEventFc.format = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "format", "I");
	XClientMessageEventFc.data = (*env)->GetFieldID(env, XClientMessageEventFc.clazz, "data", I_JArray);
	XClientMessageEventFc.cached = 1;
}

XClientMessageEvent *getXClientMessageEventFields(JNIEnv *env, jobject lpObject, XClientMessageEvent *lpStruct)
{
	if (!XClientMessageEventFc.cached) cacheXClientMessageEventFields(env, lpObject);
	lpStruct->type = (*env)->GetIntField(env, lpObject, XClientMessageEventFc.type);
	lpStruct->serial = (*env)->GetIntLongField(env, lpObject, XClientMessageEventFc.serial);
	lpStruct->send_event = (*env)->GetBooleanField(env, lpObject, XClientMessageEventFc.send_event);
	lpStruct->display = (Display *)(*env)->GetIntLongField(env, lpObject, XClientMessageEventFc.display);
	lpStruct->window = (Window)(*env)->GetIntLongField(env, lpObject, XClientMessageEventFc.window);
	lpStruct->message_type = (Atom)(*env)->GetIntLongField(env, lpObject, XClientMessageEventFc.message_type);
	lpStruct->format = (*env)->GetIntField(env, lpObject, XClientMessageEventFc.format);
	{
	jintLongArray lpObject1 = (jintLongArray)(*env)->GetObjectField(env, lpObject, XClientMessageEventFc.data);
	(*env)->GetIntLongArrayRegion(env, lpObject1, 0, sizeof(lpStruct->data.l) / sizeof(jintLong), (jintLong *)lpStruct->data.l);
	}
	return lpStruct;
}

void setXClientMessageEventFields(JNIEnv *env, jobject lpObject, XClientMessageEvent *lpStruct)
{
	if (!XClientMessageEventFc.cached) cacheXClientMessageEventFields(env, lpObject);
	(*env)->SetIntField(env, lpObject, XClientMessageEventFc.type, (jint)lpStruct->type);
	(*env)->SetIntLongField(env, lpObject, XClientMessageEventFc.serial, (jintLong)lpStruct->serial);
	(*env)->SetBooleanField(env, lpObject, XClientMessageEventFc.send_event, (jboolean)lpStruct->send_event);
	(*env)->SetIntLongField(env, lpObject, XClientMessageEventFc.display, (jintLong)lpStruct->display);
	(*env)->SetIntLongField(env, lpObject, XClientMessageEventFc.window, (jintLong)lpStruct->window);
	(*env)->SetIntLongField(env, lpObject, XClientMessageEventFc.message_type, (jintLong)lpStruct->message_type);
	(*env)->SetIntField(env, lpObject, XClientMessageEventFc.format, (jint)lpStruct->format);
	{
	jintLongArray lpObject1 = (jintLongArray)(*env)->GetObjectField(env, lpObject, XClientMessageEventFc.data);
	(*env)->SetIntLongArrayRegion(env, lpObject1, 0, sizeof(lpStruct->data.l) / sizeof(jintLong), (jintLong *)lpStruct->data.l);
	}
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
	XCrossingEventFc.root = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "root", I_J);
	XCrossingEventFc.subwindow = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "subwindow", I_J);
	XCrossingEventFc.time = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "time", "I");
	XCrossingEventFc.x = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "x", "I");
	XCrossingEventFc.y = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "y", "I");
	XCrossingEventFc.x_root = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "x_root", "I");
	XCrossingEventFc.y_root = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "y_root", "I");
	XCrossingEventFc.mode = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "mode", "I");
	XCrossingEventFc.detail = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "detail", "I");
	XCrossingEventFc.same_screen = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "same_screen", "Z");
	XCrossingEventFc.focus = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "focus", "Z");
	XCrossingEventFc.state = (*env)->GetFieldID(env, XCrossingEventFc.clazz, "state", "I");
	XCrossingEventFc.cached = 1;
}

XCrossingEvent *getXCrossingEventFields(JNIEnv *env, jobject lpObject, XCrossingEvent *lpStruct)
{
	if (!XCrossingEventFc.cached) cacheXCrossingEventFields(env, lpObject);
	getXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	lpStruct->root = (Window)(*env)->GetIntLongField(env, lpObject, XCrossingEventFc.root);
	lpStruct->subwindow = (Window)(*env)->GetIntLongField(env, lpObject, XCrossingEventFc.subwindow);
	lpStruct->time = (Time)(*env)->GetIntField(env, lpObject, XCrossingEventFc.time);
	lpStruct->x = (*env)->GetIntField(env, lpObject, XCrossingEventFc.x);
	lpStruct->y = (*env)->GetIntField(env, lpObject, XCrossingEventFc.y);
	lpStruct->x_root = (*env)->GetIntField(env, lpObject, XCrossingEventFc.x_root);
	lpStruct->y_root = (*env)->GetIntField(env, lpObject, XCrossingEventFc.y_root);
	lpStruct->mode = (*env)->GetIntField(env, lpObject, XCrossingEventFc.mode);
	lpStruct->detail = (*env)->GetIntField(env, lpObject, XCrossingEventFc.detail);
	lpStruct->same_screen = (Bool)(*env)->GetBooleanField(env, lpObject, XCrossingEventFc.same_screen);
	lpStruct->focus = (Bool)(*env)->GetBooleanField(env, lpObject, XCrossingEventFc.focus);
	lpStruct->state = (unsigned int)(*env)->GetIntField(env, lpObject, XCrossingEventFc.state);
	return lpStruct;
}

void setXCrossingEventFields(JNIEnv *env, jobject lpObject, XCrossingEvent *lpStruct)
{
	if (!XCrossingEventFc.cached) cacheXCrossingEventFields(env, lpObject);
	setXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	(*env)->SetIntLongField(env, lpObject, XCrossingEventFc.root, (jintLong)lpStruct->root);
	(*env)->SetIntLongField(env, lpObject, XCrossingEventFc.subwindow, (jintLong)lpStruct->subwindow);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.time, (jint)lpStruct->time);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.x, (jint)lpStruct->x);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.y, (jint)lpStruct->y);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.x_root, (jint)lpStruct->x_root);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.y_root, (jint)lpStruct->y_root);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.mode, (jint)lpStruct->mode);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.detail, (jint)lpStruct->detail);
	(*env)->SetBooleanField(env, lpObject, XCrossingEventFc.same_screen, (jboolean)lpStruct->same_screen);
	(*env)->SetBooleanField(env, lpObject, XCrossingEventFc.focus, (jboolean)lpStruct->focus);
	(*env)->SetIntField(env, lpObject, XCrossingEventFc.state, (jint)lpStruct->state);
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

#ifndef NO_XVisibilityEvent
typedef struct XVisibilityEvent_FID_CACHE {
	int cached;
	jclass clazz;
	jfieldID state;
} XVisibilityEvent_FID_CACHE;

XVisibilityEvent_FID_CACHE XVisibilityEventFc;

void cacheXVisibilityEventFields(JNIEnv *env, jobject lpObject)
{
	if (XVisibilityEventFc.cached) return;
	cacheXAnyEventFields(env, lpObject);
	XVisibilityEventFc.clazz = (*env)->GetObjectClass(env, lpObject);
	XVisibilityEventFc.state = (*env)->GetFieldID(env, XVisibilityEventFc.clazz, "state", "I");
	XVisibilityEventFc.cached = 1;
}

XVisibilityEvent *getXVisibilityEventFields(JNIEnv *env, jobject lpObject, XVisibilityEvent *lpStruct)
{
	if (!XVisibilityEventFc.cached) cacheXVisibilityEventFields(env, lpObject);
	getXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	lpStruct->state = (*env)->GetIntField(env, lpObject, XVisibilityEventFc.state);
	return lpStruct;
}

void setXVisibilityEventFields(JNIEnv *env, jobject lpObject, XVisibilityEvent *lpStruct)
{
	if (!XVisibilityEventFc.cached) cacheXVisibilityEventFields(env, lpObject);
	setXAnyEventFields(env, lpObject, (XAnyEvent *)lpStruct);
	(*env)->SetIntField(env, lpObject, XVisibilityEventFc.state, (jint)lpStruct->state);
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
	XWindowChangesFc.sibling = (*env)->GetFieldID(env, XWindowChangesFc.clazz, "sibling", I_J);
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
	lpStruct->sibling = (*env)->GetIntLongField(env, lpObject, XWindowChangesFc.sibling);
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
	(*env)->SetIntLongField(env, lpObject, XWindowChangesFc.sibling, (jintLong)lpStruct->sibling);
	(*env)->SetIntField(env, lpObject, XWindowChangesFc.stack_mode, (jint)lpStruct->stack_mode);
}
#endif

