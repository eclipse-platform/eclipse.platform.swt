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

/**
 * SWT OS natives implementation: OS.memmove(...)
 */ 

#include "swt.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>

/* Read */
/* GDK */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkColor_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGdkColorFids(env, dest, &PGLOB(GdkColorFc));
		setGdkColorFields(env, dest, (GdkColor *)src, &PGLOB(GdkColorFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkEventExpose_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGdkEventExposeFids(env, dest, &PGLOB(GdkEventExposeFc));
		setGdkEventExposeFields(env, dest, (GdkEvent *)src, &PGLOB(GdkEventExposeFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkVisual_2I
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGdkVisualFids(env, dest, &PGLOB(GdkVisualFc));
		setGdkVisualFields(env, dest, (GdkVisual *)src, &PGLOB(GdkVisualFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GdkImage_2I
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGdkImageFids(env, dest, &PGLOB(GdkImageFc));
		setGdkImageFields(env, dest, (GdkImage *)src, &PGLOB(GdkImageFc));
	}
}

/* GTK */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkStyleClass_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGtkStyleClassFids(env, dest, &PGLOB(GtkStyleClassFc));
		setGtkStyleClassFields(env, dest, (GtkStyleClass *)src, &PGLOB(GtkStyleClassFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkAllocation_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGtkAllocationFids(env, dest, &PGLOB(GtkAllocationFc));
		setGtkAllocationFields(env, dest, (GtkAllocation *)src, &PGLOB(GtkAllocationFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCombo_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGtkComboFids(env, dest, &PGLOB(GtkComboFc));
		setGtkComboFields(env, dest, (GtkCombo *)src, &PGLOB(GtkComboFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCList_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGtkCListFids(env, dest, &PGLOB(GtkCListFc));
		setGtkCListFields(env, dest, (GtkCList *)src, &PGLOB(GtkCListFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCTree_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGtkCTreeFids(env, dest, &PGLOB(GtkCTreeFc));
		setGtkCTreeFields(env, dest, (GtkCTree *)src, &PGLOB(GtkCTreeFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkRequisition_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGtkRequisitionFids(env, dest, &PGLOB(GtkRequisitionFc));
		setGtkRequisitionFields(env, dest, (GtkRequisition *)src, &PGLOB(GtkRequisitionFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkStyle_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGtkStyleFids(env, dest, &PGLOB(GtkStyleFc));
		setGtkStyleFields(env, dest, (GtkStyle *)src, &PGLOB(GtkStyleFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkAdjustment_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGtkAdjustmentFids(env, dest, &PGLOB(GtkAdjustmentFc));
		setGtkAdjustmentFields(env, dest, (GtkAdjustment *)src, &PGLOB(GtkAdjustmentFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCListRow_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGtkCListRowFids(env, dest, &PGLOB(GtkCListRowFc));
		setGtkCListRowFields(env, dest, (GtkCListRow *)src, &PGLOB(GtkCListRowFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCListColumn_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGtkCListColumnFids(env, dest, &PGLOB(GtkCListColumnFc));
		setGtkCListColumnFields(env, dest, (GtkCListColumn *)src, &PGLOB(GtkCListColumnFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkCTreeRow_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGtkCTreeRowFids(env, dest, &PGLOB(GtkCTreeRowFc));
		setGtkCTreeRowFields(env, dest, (GtkCTreeRow *)src, &PGLOB(GtkCTreeRowFc));
	}
}

/* Write */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkAdjustment_2
  (JNIEnv *env, jclass that, jint dest, jobject src)
{
	DECL_GLOB(pGlob)
	if (src) {
		cacheGtkAdjustmentFids(env, src, &PGLOB(GtkAdjustmentFc));
		getGtkAdjustmentFields(env, src, (GtkAdjustment *)dest, &PGLOB(GtkAdjustmentFc));
	}
}


JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkCListColumn_2
  (JNIEnv *env, jclass that, jint dest, jobject src)
{
	DECL_GLOB(pGlob)
	if (src) {
		cacheGtkCListColumnFids(env, src, &PGLOB(GtkCListColumnFc));
		getGtkCListColumnFields(env, src, (GtkCListColumn *)dest, &PGLOB(GtkCListColumnFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkStyle_2
  (JNIEnv *env, jclass that, jint dest, jobject src)
{
	DECL_GLOB(pGlob)
	if (src) {
		cacheGtkStyleFids(env, src, &PGLOB(GtkStyleFc));
		getGtkStyleFields(env, src, (GtkStyle *)dest, &PGLOB(GtkStyleFc));
	}
}


/* Primitive memmoves */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__I_3BI
  (JNIEnv *env, jclass that, jint dest, jbyteArray src, jint count)
{
	jbyte *src1;
	if (src) {
		src1 = (*env)->GetByteArrayElements(env, src, NULL);
		memmove((void*)dest, (void*)src1, count);
		(*env)->ReleaseByteArrayElements(env, src, src1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__I_3II
  (JNIEnv *env, jclass that, jint dest, jintArray src, jint count)
{
	jint *src1;
	if (src) {
		src1 = (*env)->GetIntArrayElements(env, src, NULL);
		memmove((void*)dest, (void*)src1, count);
		(*env)->ReleaseIntArrayElements(env, src, src1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove___3III
  (JNIEnv *env, jclass that, jintArray dest, jint src, jint count)
{
	jint *dest1;
	if (dest) {
		dest1 = (*env)->GetIntArrayElements(env, dest, NULL);
		memmove((void*)dest1, (void*)src, count);
		(*env)->ReleaseIntArrayElements(env, dest, dest1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove___3BII
  (JNIEnv *env, jclass that, jbyteArray dest, jint src, jint count)
{
	jbyte *dest1;
	if (dest) {
		dest1 = (*env)->GetByteArrayElements(env, dest, NULL);
		memmove((void*)dest1, (void*)src, count);
		(*env)->ReleaseByteArrayElements(env, dest, dest1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove___3I_3BI
  (JNIEnv *env, jclass that, jintArray dest, jbyteArray src, jint count)
{
	jint *dest1;
	jbyte *src1;
	if (src && dest) {
		dest1 = (*env)->GetIntArrayElements(env, dest, NULL);
		src1 = (*env)->GetByteArrayElements(env, dest, NULL);
		memmove((void*)dest1, (void*)src1, count);
		(*env)->ReleaseIntArrayElements(env, dest, dest1, 0);
		(*env)->ReleaseByteArrayElements(env, src, src1, 0);
	}
}

/* Clipboard */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__Lorg_eclipse_swt_internal_gtk_GtkSelectionData_2I
  (JNIEnv *env, jclass that, jobject dest, jint src)
{
	DECL_GLOB(pGlob)
	if (dest) {
		cacheGtkSelectionDataFids(env, dest, &PGLOB(GtkSelectionDataFc));
		setGtkSelectionDataFields(env, dest, (GtkSelectionData *)src, &PGLOB(GtkSelectionDataFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_memmove__ILorg_eclipse_swt_internal_gtk_GtkTargetEntry_2
  (JNIEnv *env, jclass that, jint dest, jobject src)
{
	DECL_GLOB(pGlob)
	if (src) {
		cacheGtkTargetEntryFids(env, src, &PGLOB(GtkTargetEntryFc));
		getGtkTargetEntryFields(env, src, (GtkTargetEntry *)dest, &PGLOB(GtkTargetEntryFc));
	}
}
