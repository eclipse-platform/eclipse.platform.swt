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

#ifndef INC_structs_H
#define INC_structs_H
 
#include <gtk/gtk.h>
#include <gdk/gdk.h>
#include <pango/pango.h>

GdkColor *getGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct);
void setGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct);

GdkDragContext *getGdkDragContextFields(JNIEnv *env, jobject lpObject, GdkDragContext *lpStruct);
void setGdkDragContextFields(JNIEnv *env, jobject lpObject, GdkDragContext *lpStruct);

GdkEvent *getGdkEventFields(JNIEnv *env, jobject lpObject, GdkEvent *lpStruct);
void setGdkEventFields(JNIEnv *env, jobject lpObject, GdkEvent *lpStruct);

GdkEventButton *getGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct);
void setGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct);

GdkEventExpose *getGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct);
void setGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct);

GdkEventFocus *getGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct);
void setGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct);

GdkEventKey *getGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct);
void setGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct);

GdkGCValues *getGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpStruct);
void setGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpStruct);

GdkImage *getGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct);
void setGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct);

GdkRectangle *getGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpStruct);
void setGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpStruct);

GdkVisual *getGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpStruct);
void setGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpStruct);

GtkAdjustment *getGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpStruct);
void setGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpStruct);

GtkAllocation *getGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpStruct);
void setGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpStruct);

GtkColorSelectionDialog *getGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct);
void setGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct);

GtkCombo *getGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct);
void setGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct);

GtkRequisition *getGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpStruct);
void setGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpStruct);

GtkSelectionData *getGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct);
void setGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct);

GtkStyle *getGtkStyleFields(JNIEnv *env, jobject lpObject, GtkStyle *lpStruct);
void setGtkStyleFields(JNIEnv *env, jobject lpObject, GtkStyle *lpStruct);

GtkTargetEntry *getGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct);
void setGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct);

GtkTargetPair *getGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct);
void setGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct);

GtkFixed *getGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct);
void setGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct);

GdkEventCrossing *getGdkEventCrossingFields(JNIEnv *env, jobject lpObject, GdkEventCrossing *lpStruct);
void setGdkEventCrossingFields(JNIEnv *env, jobject lpObject, GdkEventCrossing *lpStruct);

#endif // INC_structs_H

