/*
 * Copyright (c) IBM Corp. 2000, 2002.  All rights reserved.
 *
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 */

/*
 * This file is logically part of swt.c, and will be eventually merged back.
 */

/**
 * SWT natives for GTK container widgets.
 */ 

#include "swt.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>


/* gtk_box */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1box_1pack_1start
  (JNIEnv *env, jclass that, jint box, jint child, jboolean expand, jboolean fill, jint padding)
{
	gtk_box_pack_start((GtkBox*)box, (GtkWidget*)child, (gboolean)expand, (gboolean)fill, padding);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1box_1pack_1end
  (JNIEnv *env, jclass that, jint box, jint child, jboolean expand, jboolean fill, jint padding)
{
	gtk_box_pack_end((GtkBox*)box, (GtkWidget*)child, (gboolean)expand, (gboolean)fill, padding);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1hbox_1new
  (JNIEnv *env, jclass that, jboolean homogeneous, jint spacing)
{
	return (jint)gtk_hbox_new((gboolean)homogeneous, (gint)spacing);
}

/* gtk_container */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1add
  (JNIEnv *env, jclass that, jint container, jint widget)
{
	gtk_container_add((GtkContainer*)container, (GtkWidget*)widget);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1remove
  (JNIEnv *env, jclass that, jint container, jint widget)
{
	gtk_container_remove((GtkContainer*)container, (GtkWidget*)widget);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1container_1children
  (JNIEnv *env, jclass that, jint container)
{
	return (jint)gtk_container_children((GtkContainer*)container);
}

/* gtk_event_box */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1event_1box_1new
  (JNIEnv *env, jclass that)
{
	return (jint)gtk_event_box_new();
}

/* gtk_frame */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1frame_1new
  (JNIEnv *env, jclass that, jbyteArray label)
{
	jint rc;
	jbyte *label1 = NULL;
	if (label) {
		label1 = (*env)->GetByteArrayElements(env, label, NULL);
	}
	rc = (jint)gtk_frame_new((gchar*)label1);
	if (label) {
		(*env)->ReleaseByteArrayElements(env, label, label1, 0);
	}
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1frame_1set_1label
  (JNIEnv *env, jclass that, jint frame, jbyteArray label)
{
	jbyte *label1 = NULL;
	if (label) {
		label1 = (*env)->GetByteArrayElements(env, label, NULL);
	}
	gtk_frame_set_label((GtkFrame*)frame, (gchar*)label1);
	if (label) {
		(*env)->ReleaseByteArrayElements(env, label, label1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1frame_1set_1shadow_1type
  (JNIEnv *env, jclass that, jint frame, jint type)
{
	gtk_frame_set_shadow_type((GtkFrame*)frame, (GtkShadowType)type);
}

/* temporary code */
JNIEXPORT void JNICALL
Java_org_eclipse_swt_internal_gtk_OS_swt_1frame_1get_1trim
  (JNIEnv *env, jclass that, jint handle, jintArray trims)
{
   int border, top_margin;
   GtkWidget *widget; GtkFrame *frame;
   GtkRequisition requisition;
   int *ctrims;
   if (trims==NULL) return;
   ctrims = (*env)->GetIntArrayElements(env, trims, NULL);
   if (ctrims==NULL) return;
   /* calculate the trims */   
   widget = GTK_WIDGET (handle);
   frame  = GTK_FRAME (handle);
   if (frame->label_widget) {
      /* careful: not child_requisition, because it may not be up to date */
      gtk_widget_size_request (frame->label_widget, &requisition);
      top_margin = MAX (requisition.height, widget->style->ythickness);
   } else top_margin = widget->style->ythickness;
   border = GTK_CONTAINER(frame)->border_width;

   ctrims[0] = border + top_margin;
   ctrims[1] = border + widget->style->xthickness;
   ctrims[2] = border + widget->style->xthickness;
   ctrims[3] = border + widget->style->ythickness;

   (*env)->ReleaseIntArrayElements(env, trims, ctrims, 0);
}
