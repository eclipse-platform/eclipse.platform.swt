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

#include "os.h"

#ifndef NO_GInterfaceInfo
GInterfaceInfo *getGInterfaceInfoFields(JNIEnv *env, jobject lpObject, GInterfaceInfo *lpStruct);
void setGInterfaceInfoFields(JNIEnv *env, jobject lpObject, GInterfaceInfo *lpStruct);
#else
#define getGInterfaceInfoFields(a,b,c) NULL
#define setGInterfaceInfoFields(a,b,c)
#endif

#ifndef NO_GObjectClass
GObjectClass *getGObjectClassFields(JNIEnv *env, jobject lpObject, GObjectClass *lpStruct);
void setGObjectClassFields(JNIEnv *env, jobject lpObject, GObjectClass *lpStruct);
#else
#define getGObjectClassFields(a,b,c) NULL
#define setGObjectClassFields(a,b,c)
#endif

#ifndef NO_GTypeInfo
GTypeInfo *getGTypeInfoFields(JNIEnv *env, jobject lpObject, GTypeInfo *lpStruct);
void setGTypeInfoFields(JNIEnv *env, jobject lpObject, GTypeInfo *lpStruct);
#else
#define getGTypeInfoFields(a,b,c) NULL
#define setGTypeInfoFields(a,b,c)
#endif

#ifndef NO_GTypeQuery
GTypeQuery *getGTypeQueryFields(JNIEnv *env, jobject lpObject, GTypeQuery *lpStruct);
void setGTypeQueryFields(JNIEnv *env, jobject lpObject, GTypeQuery *lpStruct);
#else
#define getGTypeQueryFields(a,b,c) NULL
#define setGTypeQueryFields(a,b,c)
#endif

#ifndef NO_GdkColor
GdkColor *getGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct);
void setGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct);
#else
#define getGdkColorFields(a,b,c) NULL
#define setGdkColorFields(a,b,c)
#endif

#ifndef NO_GdkDragContext
GdkDragContext *getGdkDragContextFields(JNIEnv *env, jobject lpObject, GdkDragContext *lpStruct);
void setGdkDragContextFields(JNIEnv *env, jobject lpObject, GdkDragContext *lpStruct);
#else
#define getGdkDragContextFields(a,b,c) NULL
#define setGdkDragContextFields(a,b,c)
#endif

#ifndef NO_GdkEvent
GdkEvent *getGdkEventFields(JNIEnv *env, jobject lpObject, GdkEvent *lpStruct);
void setGdkEventFields(JNIEnv *env, jobject lpObject, GdkEvent *lpStruct);
#else
#define getGdkEventFields(a,b,c) NULL
#define setGdkEventFields(a,b,c)
#endif

#ifndef NO_GdkEventButton
GdkEventButton *getGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct);
void setGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct);
#else
#define getGdkEventButtonFields(a,b,c) NULL
#define setGdkEventButtonFields(a,b,c)
#endif

#ifndef NO_GdkEventCrossing
GdkEventCrossing *getGdkEventCrossingFields(JNIEnv *env, jobject lpObject, GdkEventCrossing *lpStruct);
void setGdkEventCrossingFields(JNIEnv *env, jobject lpObject, GdkEventCrossing *lpStruct);
#else
#define getGdkEventCrossingFields(a,b,c) NULL
#define setGdkEventCrossingFields(a,b,c)
#endif

#ifndef NO_GdkEventExpose
GdkEventExpose *getGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct);
void setGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct);
#else
#define getGdkEventExposeFields(a,b,c) NULL
#define setGdkEventExposeFields(a,b,c)
#endif

#ifndef NO_GdkEventFocus
GdkEventFocus *getGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct);
void setGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct);
#else
#define getGdkEventFocusFields(a,b,c) NULL
#define setGdkEventFocusFields(a,b,c)
#endif

#ifndef NO_GdkEventKey
GdkEventKey *getGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct);
void setGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct);
#else
#define getGdkEventKeyFields(a,b,c) NULL
#define setGdkEventKeyFields(a,b,c)
#endif

#ifndef NO_GdkEventWindowState
GdkEventWindowState *getGdkEventWindowStateFields(JNIEnv *env, jobject lpObject, GdkEventWindowState *lpStruct);
void setGdkEventWindowStateFields(JNIEnv *env, jobject lpObject, GdkEventWindowState *lpStruct);
#else
#define getGdkEventWindowStateFields(a,b,c) NULL
#define setGdkEventWindowStateFields(a,b,c)
#endif

#ifndef NO_GdkGCValues
GdkGCValues *getGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpStruct);
void setGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpStruct);
#else
#define getGdkGCValuesFields(a,b,c) NULL
#define setGdkGCValuesFields(a,b,c)
#endif

#ifndef NO_GdkImage
GdkImage *getGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct);
void setGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct);
#else
#define getGdkImageFields(a,b,c) NULL
#define setGdkImageFields(a,b,c)
#endif

#ifndef NO_GdkRectangle
GdkRectangle *getGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpStruct);
void setGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpStruct);
#else
#define getGdkRectangleFields(a,b,c) NULL
#define setGdkRectangleFields(a,b,c)
#endif

#ifndef NO_GdkVisual
GdkVisual *getGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpStruct);
void setGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpStruct);
#else
#define getGdkVisualFields(a,b,c) NULL
#define setGdkVisualFields(a,b,c)
#endif

#ifndef NO_GtkAccessible
GtkAccessible *getGtkAccessibleFields(JNIEnv *env, jobject lpObject, GtkAccessible *lpStruct);
void setGtkAccessibleFields(JNIEnv *env, jobject lpObject, GtkAccessible *lpStruct);
#else
#define getGtkAccessibleFields(a,b,c) NULL
#define setGtkAccessibleFields(a,b,c)
#endif

#ifndef NO_GtkAdjustment
GtkAdjustment *getGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpStruct);
void setGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpStruct);
#else
#define getGtkAdjustmentFields(a,b,c) NULL
#define setGtkAdjustmentFields(a,b,c)
#endif

#ifndef NO_GtkAllocation
GtkAllocation *getGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpStruct);
void setGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpStruct);
#else
#define getGtkAllocationFields(a,b,c) NULL
#define setGtkAllocationFields(a,b,c)
#endif

#ifndef NO_GtkColorSelectionDialog
GtkColorSelectionDialog *getGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct);
void setGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct);
#else
#define getGtkColorSelectionDialogFields(a,b,c) NULL
#define setGtkColorSelectionDialogFields(a,b,c)
#endif

#ifndef NO_GtkCombo
GtkCombo *getGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct);
void setGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct);
#else
#define getGtkComboFields(a,b,c) NULL
#define setGtkComboFields(a,b,c)
#endif

#ifndef NO_GtkFileSelection
GtkFileSelection *getGtkFileSelectionFields(JNIEnv *env, jobject lpObject, GtkFileSelection *lpStruct);
void setGtkFileSelectionFields(JNIEnv *env, jobject lpObject, GtkFileSelection *lpStruct);
#else
#define getGtkFileSelectionFields(a,b,c) NULL
#define setGtkFileSelectionFields(a,b,c)
#endif

#ifndef NO_GtkFixed
GtkFixed *getGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct);
void setGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct);
#else
#define getGtkFixedFields(a,b,c) NULL
#define setGtkFixedFields(a,b,c)
#endif

#ifndef NO_GtkRequisition
GtkRequisition *getGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpStruct);
void setGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpStruct);
#else
#define getGtkRequisitionFields(a,b,c) NULL
#define setGtkRequisitionFields(a,b,c)
#endif

#ifndef NO_GtkSelectionData
GtkSelectionData *getGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct);
void setGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct);
#else
#define getGtkSelectionDataFields(a,b,c) NULL
#define setGtkSelectionDataFields(a,b,c)
#endif

#ifndef NO_GtkStyle
GtkStyle *getGtkStyleFields(JNIEnv *env, jobject lpObject, GtkStyle *lpStruct);
void setGtkStyleFields(JNIEnv *env, jobject lpObject, GtkStyle *lpStruct);
#else
#define getGtkStyleFields(a,b,c) NULL
#define setGtkStyleFields(a,b,c)
#endif

#ifndef NO_GtkTargetEntry
GtkTargetEntry *getGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct);
void setGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct);
#else
#define getGtkTargetEntryFields(a,b,c) NULL
#define setGtkTargetEntryFields(a,b,c)
#endif

#ifndef NO_GtkTargetPair
GtkTargetPair *getGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct);
void setGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct);
#else
#define getGtkTargetPairFields(a,b,c) NULL
#define setGtkTargetPairFields(a,b,c)
#endif

