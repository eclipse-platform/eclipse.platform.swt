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

#ifndef NO_GdkColor
GdkColor *getGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct);
void setGdkColorFields(JNIEnv *env, jobject lpObject, GdkColor *lpStruct);
#define GdkColor_sizeof() sizeof(GdkColor)
#else
#define getGdkColorFields(a,b,c) NULL
#define setGdkColorFields(a,b,c)
#define GdkColor_sizeof() 0
#endif

#ifndef NO_GdkDragContext
GdkDragContext *getGdkDragContextFields(JNIEnv *env, jobject lpObject, GdkDragContext *lpStruct);
void setGdkDragContextFields(JNIEnv *env, jobject lpObject, GdkDragContext *lpStruct);
#define GdkDragContext_sizeof() sizeof(GdkDragContext)
#else
#define getGdkDragContextFields(a,b,c) NULL
#define setGdkDragContextFields(a,b,c)
#define GdkDragContext_sizeof() 0
#endif

#ifndef NO_GdkEvent
GdkEvent *getGdkEventFields(JNIEnv *env, jobject lpObject, GdkEvent *lpStruct);
void setGdkEventFields(JNIEnv *env, jobject lpObject, GdkEvent *lpStruct);
#define GdkEvent_sizeof() sizeof(GdkEvent)
#else
#define getGdkEventFields(a,b,c) NULL
#define setGdkEventFields(a,b,c)
#define GdkEvent_sizeof() 0
#endif

#ifndef NO_GdkEventButton
GdkEventButton *getGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct);
void setGdkEventButtonFields(JNIEnv *env, jobject lpObject, GdkEventButton *lpStruct);
#define GdkEventButton_sizeof() sizeof(GdkEventButton)
#else
#define getGdkEventButtonFields(a,b,c) NULL
#define setGdkEventButtonFields(a,b,c)
#define GdkEventButton_sizeof() 0
#endif

#ifndef NO_GdkEventCrossing
GdkEventCrossing *getGdkEventCrossingFields(JNIEnv *env, jobject lpObject, GdkEventCrossing *lpStruct);
void setGdkEventCrossingFields(JNIEnv *env, jobject lpObject, GdkEventCrossing *lpStruct);
#define GdkEventCrossing_sizeof() sizeof(GdkEventCrossing)
#else
#define getGdkEventCrossingFields(a,b,c) NULL
#define setGdkEventCrossingFields(a,b,c)
#define GdkEventCrossing_sizeof() 0
#endif

#ifndef NO_GdkEventExpose
GdkEventExpose *getGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct);
void setGdkEventExposeFields(JNIEnv *env, jobject lpObject, GdkEventExpose *lpStruct);
#define GdkEventExpose_sizeof() sizeof(GdkEventExpose)
#else
#define getGdkEventExposeFields(a,b,c) NULL
#define setGdkEventExposeFields(a,b,c)
#define GdkEventExpose_sizeof() 0
#endif

#ifndef NO_GdkEventFocus
GdkEventFocus *getGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct);
void setGdkEventFocusFields(JNIEnv *env, jobject lpObject, GdkEventFocus *lpStruct);
#define GdkEventFocus_sizeof() sizeof(GdkEventFocus)
#else
#define getGdkEventFocusFields(a,b,c) NULL
#define setGdkEventFocusFields(a,b,c)
#define GdkEventFocus_sizeof() 0
#endif

#ifndef NO_GdkEventKey
GdkEventKey *getGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct);
void setGdkEventKeyFields(JNIEnv *env, jobject lpObject, GdkEventKey *lpStruct);
#define GdkEventKey_sizeof() sizeof(GdkEventKey)
#else
#define getGdkEventKeyFields(a,b,c) NULL
#define setGdkEventKeyFields(a,b,c)
#define GdkEventKey_sizeof() 0
#endif

#ifndef NO_GdkEventWindowState
GdkEventWindowState *getGdkEventWindowStateFields(JNIEnv *env, jobject lpObject, GdkEventWindowState *lpStruct);
void setGdkEventWindowStateFields(JNIEnv *env, jobject lpObject, GdkEventWindowState *lpStruct);
#define GdkEventWindowState_sizeof() sizeof(GdkEventWindowState)
#else
#define getGdkEventWindowStateFields(a,b,c) NULL
#define setGdkEventWindowStateFields(a,b,c)
#define GdkEventWindowState_sizeof() 0
#endif

#ifndef NO_GdkGCValues
GdkGCValues *getGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpStruct);
void setGdkGCValuesFields(JNIEnv *env, jobject lpObject, GdkGCValues *lpStruct);
#define GdkGCValues_sizeof() sizeof(GdkGCValues)
#else
#define getGdkGCValuesFields(a,b,c) NULL
#define setGdkGCValuesFields(a,b,c)
#define GdkGCValues_sizeof() 0
#endif

#ifndef NO_GdkImage
GdkImage *getGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct);
void setGdkImageFields(JNIEnv *env, jobject lpObject, GdkImage *lpStruct);
#define GdkImage_sizeof() sizeof(GdkImage)
#else
#define getGdkImageFields(a,b,c) NULL
#define setGdkImageFields(a,b,c)
#define GdkImage_sizeof() 0
#endif

#ifndef NO_GdkRectangle
GdkRectangle *getGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpStruct);
void setGdkRectangleFields(JNIEnv *env, jobject lpObject, GdkRectangle *lpStruct);
#define GdkRectangle_sizeof() sizeof(GdkRectangle)
#else
#define getGdkRectangleFields(a,b,c) NULL
#define setGdkRectangleFields(a,b,c)
#define GdkRectangle_sizeof() 0
#endif

#ifndef NO_GdkVisual
GdkVisual *getGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpStruct);
void setGdkVisualFields(JNIEnv *env, jobject lpObject, GdkVisual *lpStruct);
#define GdkVisual_sizeof() sizeof(GdkVisual)
#else
#define getGdkVisualFields(a,b,c) NULL
#define setGdkVisualFields(a,b,c)
#define GdkVisual_sizeof() 0
#endif

#ifndef NO_GtkAdjustment
GtkAdjustment *getGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpStruct);
void setGtkAdjustmentFields(JNIEnv *env, jobject lpObject, GtkAdjustment *lpStruct);
#define GtkAdjustment_sizeof() sizeof(GtkAdjustment)
#else
#define getGtkAdjustmentFields(a,b,c) NULL
#define setGtkAdjustmentFields(a,b,c)
#define GtkAdjustment_sizeof() 0
#endif

#ifndef NO_GtkAllocation
GtkAllocation *getGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpStruct);
void setGtkAllocationFields(JNIEnv *env, jobject lpObject, GtkAllocation *lpStruct);
#define GtkAllocation_sizeof() sizeof(GtkAllocation)
#else
#define getGtkAllocationFields(a,b,c) NULL
#define setGtkAllocationFields(a,b,c)
#define GtkAllocation_sizeof() 0
#endif

#ifndef NO_GtkColorSelectionDialog
GtkColorSelectionDialog *getGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct);
void setGtkColorSelectionDialogFields(JNIEnv *env, jobject lpObject, GtkColorSelectionDialog *lpStruct);
#define GtkColorSelectionDialog_sizeof() sizeof(GtkColorSelectionDialog)
#else
#define getGtkColorSelectionDialogFields(a,b,c) NULL
#define setGtkColorSelectionDialogFields(a,b,c)
#define GtkColorSelectionDialog_sizeof() 0
#endif

#ifndef NO_GtkCombo
GtkCombo *getGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct);
void setGtkComboFields(JNIEnv *env, jobject lpObject, GtkCombo *lpStruct);
#define GtkCombo_sizeof() sizeof(GtkCombo)
#else
#define getGtkComboFields(a,b,c) NULL
#define setGtkComboFields(a,b,c)
#define GtkCombo_sizeof() 0
#endif

#ifndef NO_GtkFileSelection
GtkFileSelection *getGtkFileSelectionFields(JNIEnv *env, jobject lpObject, GtkFileSelection *lpStruct);
void setGtkFileSelectionFields(JNIEnv *env, jobject lpObject, GtkFileSelection *lpStruct);
#define GtkFileSelection_sizeof() sizeof(GtkFileSelection)
#else
#define getGtkFileSelectionFields(a,b,c) NULL
#define setGtkFileSelectionFields(a,b,c)
#define GtkFileSelection_sizeof() 0
#endif

#ifndef NO_GtkFixed
GtkFixed *getGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct);
void setGtkFixedFields(JNIEnv *env, jobject lpObject, GtkFixed *lpStruct);
#define GtkFixed_sizeof() sizeof(GtkFixed)
#else
#define getGtkFixedFields(a,b,c) NULL
#define setGtkFixedFields(a,b,c)
#define GtkFixed_sizeof() 0
#endif

#ifndef NO_GtkRequisition
GtkRequisition *getGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpStruct);
void setGtkRequisitionFields(JNIEnv *env, jobject lpObject, GtkRequisition *lpStruct);
#define GtkRequisition_sizeof() sizeof(GtkRequisition)
#else
#define getGtkRequisitionFields(a,b,c) NULL
#define setGtkRequisitionFields(a,b,c)
#define GtkRequisition_sizeof() 0
#endif

#ifndef NO_GtkSelectionData
GtkSelectionData *getGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct);
void setGtkSelectionDataFields(JNIEnv *env, jobject lpObject, GtkSelectionData *lpStruct);
#define GtkSelectionData_sizeof() sizeof(GtkSelectionData)
#else
#define getGtkSelectionDataFields(a,b,c) NULL
#define setGtkSelectionDataFields(a,b,c)
#define GtkSelectionData_sizeof() 0
#endif

#ifndef NO_GtkTargetEntry
GtkTargetEntry *getGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct);
void setGtkTargetEntryFields(JNIEnv *env, jobject lpObject, GtkTargetEntry *lpStruct);
#define GtkTargetEntry_sizeof() sizeof(GtkTargetEntry)
#else
#define getGtkTargetEntryFields(a,b,c) NULL
#define setGtkTargetEntryFields(a,b,c)
#define GtkTargetEntry_sizeof() 0
#endif

#ifndef NO_GtkTargetPair
GtkTargetPair *getGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct);
void setGtkTargetPairFields(JNIEnv *env, jobject lpObject, GtkTargetPair *lpStruct);
#define GtkTargetPair_sizeof() sizeof(GtkTargetPair)
#else
#define getGtkTargetPairFields(a,b,c) NULL
#define setGtkTargetPairFields(a,b,c)
#define GtkTargetPair_sizeof() 0
#endif

#ifndef NO_PangoAttribute
PangoAttribute *getPangoAttributeFields(JNIEnv *env, jobject lpObject, PangoAttribute *lpStruct);
void setPangoAttributeFields(JNIEnv *env, jobject lpObject, PangoAttribute *lpStruct);
#define PangoAttribute_sizeof() sizeof(PangoAttribute)
#else
#define getPangoAttributeFields(a,b,c) NULL
#define setPangoAttributeFields(a,b,c)
#define PangoAttribute_sizeof() 0
#endif

#ifndef NO_PangoItem
PangoItem *getPangoItemFields(JNIEnv *env, jobject lpObject, PangoItem *lpStruct);
void setPangoItemFields(JNIEnv *env, jobject lpObject, PangoItem *lpStruct);
#define PangoItem_sizeof() sizeof(PangoItem)
#else
#define getPangoItemFields(a,b,c) NULL
#define setPangoItemFields(a,b,c)
#define PangoItem_sizeof() 0
#endif

#ifndef NO_PangoLayoutLine
PangoLayoutLine *getPangoLayoutLineFields(JNIEnv *env, jobject lpObject, PangoLayoutLine *lpStruct);
void setPangoLayoutLineFields(JNIEnv *env, jobject lpObject, PangoLayoutLine *lpStruct);
#define PangoLayoutLine_sizeof() sizeof(PangoLayoutLine)
#else
#define getPangoLayoutLineFields(a,b,c) NULL
#define setPangoLayoutLineFields(a,b,c)
#define PangoLayoutLine_sizeof() 0
#endif

#ifndef NO_PangoLayoutRun
PangoLayoutRun *getPangoLayoutRunFields(JNIEnv *env, jobject lpObject, PangoLayoutRun *lpStruct);
void setPangoLayoutRunFields(JNIEnv *env, jobject lpObject, PangoLayoutRun *lpStruct);
#define PangoLayoutRun_sizeof() sizeof(PangoLayoutRun)
#else
#define getPangoLayoutRunFields(a,b,c) NULL
#define setPangoLayoutRunFields(a,b,c)
#define PangoLayoutRun_sizeof() 0
#endif

#ifndef NO_PangoLogAttr
PangoLogAttr *getPangoLogAttrFields(JNIEnv *env, jobject lpObject, PangoLogAttr *lpStruct);
void setPangoLogAttrFields(JNIEnv *env, jobject lpObject, PangoLogAttr *lpStruct);
#define PangoLogAttr_sizeof() sizeof(PangoLogAttr)
#else
#define getPangoLogAttrFields(a,b,c) NULL
#define setPangoLogAttrFields(a,b,c)
#define PangoLogAttr_sizeof() 0
#endif

#ifndef NO_PangoRectangle
PangoRectangle *getPangoRectangleFields(JNIEnv *env, jobject lpObject, PangoRectangle *lpStruct);
void setPangoRectangleFields(JNIEnv *env, jobject lpObject, PangoRectangle *lpStruct);
#define PangoRectangle_sizeof() sizeof(PangoRectangle)
#else
#define getPangoRectangleFields(a,b,c) NULL
#define setPangoRectangleFields(a,b,c)
#define PangoRectangle_sizeof() 0
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

#ifndef NO_XWindowChanges
XWindowChanges *getXWindowChangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpStruct);
void setXWindowChangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpStruct);
#define XWindowChanges_sizeof() sizeof(XWindowChanges)
#else
#define getXWindowChangesFields(a,b,c) NULL
#define setXWindowChangesFields(a,b,c)
#define XWindowChanges_sizeof() 0
#endif

