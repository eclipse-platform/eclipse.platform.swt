/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

/**
 * SWT OS natives implementation.
 */ 

// #define PRINT_FAILED_RCODES
#define NDEBUG 

#include "globals.h"
#include "structs.h"

#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <malloc.h>


JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_photon_OS_getSharedLibraryMajorVersionNumber
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "getSharedLibraryMajorVersionNumber\n");
#endif
    return SWT_LIBRARY_MAJOR_VERSION;
}

JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_photon_OS_getSharedLibraryMinorVersionNumber
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "getSharedLibraryMinorVersionNumber\n");
#endif
    return SWT_LIBRARY_MINOR_VERSION;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWindow
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWindow
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWindow\n");
#endif
	
	return (jint)PtWindow;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtList
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtList
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtList\n");
#endif
	
	return (jint)PtList;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtLabel
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtLabel
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtLabel\n");
#endif
	
	return (jint)PtLabel;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtInit
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtInit
  (JNIEnv *env, jobject that, jbyteArray name)
{
    char *name1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtInit\n");
#endif

    if (name)
        name1 = (char *)(*env)->GetByteArrayElements(env, name, NULL);

    result = (jint)PtInit(name1);

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, (jbyte *)name1, 0);
	
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtCreateWidget
 * Signature: (III[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtCreateWidget
  (JNIEnv *env, jobject that, jint clazz, jint parent, jint n_args, jintArray args)
{
    PtArg_t *args1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtCreateWidget\n");
#endif

    if (args)
        args1 = (PtArg_t *)(*env)->GetIntArrayElements(env, args, NULL);

    result = (jint)PtCreateWidget((PtWidgetClassRef_t *)clazz, (PtWidget_t *)parent, n_args, args1);

    if (args)
        (*env)->ReleaseIntArrayElements(env, args, (jint *)args1, 0);
	
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtSetResources
 * Signature: (II[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtSetResources
  (JNIEnv *env, jobject that, jint widget, jint n_args, jintArray args)
{
    jint *args1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtSetResources\n");
#endif

    if (args)
        args1 = (*env)->GetIntArrayElements(env, args, NULL);

    result = (jint)PtSetResources((PtWidget_t *)widget, n_args, (PtArg_t *)args1);

    if (args)
        (*env)->ReleaseIntArrayElements(env, args, args1, 0);
	
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtRealizeWidget
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtRealizeWidget
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtRealizeWidget\n");
#endif
	
	return (jint)PtRealizeWidget((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAddEventHandler
 * Signature: (IIII)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAddEventHandler
  (JNIEnv *env, jobject that, jint widget, jint event_mask, jint callback, jint data)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAddEventHandler\n");
#endif
	
	PtAddEventHandler((PtWidget_t *)widget, (unsigned long)event_mask, (PtCallbackF_t *)callback, (void *)data);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetRid
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetRid
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetRid\n");
#endif
	
	return (jint)PtWidgetRid((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtMainLoop
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtMainLoop
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtMainLoop\n");
#endif
	
	PtMainLoop();
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgFlush
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgFlush
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgFlush\n");
#endif
	
	return (jint)PgFlush();
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgCreateGC
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgCreateGC
  (JNIEnv *env, jobject that, jint size)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgCreateGC\n");
#endif
	
	return (jint)PgCreateGC(size);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetGC
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetGC
  (JNIEnv *env, jobject that, jint GC)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetGC\n");
#endif
	
	return (jint)PgSetGC((PhGC_t *)GC);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetRegion
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetRegion
  (JNIEnv *env, jobject that, jint rid)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetRegion\n");
#endif
	
	PgSetRegion(rid);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetFillColor
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetFillColor
  (JNIEnv *env, jobject that, jint color)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetFillColor\n");
#endif
	
	return (jint)PgSetFillColor(color);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetStrokeColor
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetStrokeColor
  (JNIEnv *env, jobject that, jint color)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetStrokeColor\n");
#endif
	
	return (jint)PgSetStrokeColor(color);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetTextColor
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetTextColor
  (JNIEnv *env, jobject that, jint color)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetTextColor\n");
#endif
	
	return (jint)PgSetTextColor(color);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetFont
 * Signature: ([B)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetFont
  (JNIEnv *env, jobject that, jbyteArray ff)
{
    jbyte *ff1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetFont\n");
#endif

    if (ff)
        ff1 = (*env)->GetByteArrayElements(env, ff, NULL);

    PgSetFont(ff1);

    if (ff)
        (*env)->ReleaseByteArrayElements(env, ff, ff1, JNI_ABORT);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetStrokeDash
 * Signature: ([BII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetStrokeDash
  (JNIEnv *env, jobject that, jbyteArray DashList, jint ListLen, jint DashScale)
{
    jbyte *DashList1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetStrokeDash\n");
#endif

    if (DashList)
        DashList1 = (*env)->GetByteArrayElements(env, DashList, NULL);

    PgSetStrokeDash(DashList1, ListLen, DashScale);

    if (DashList)
        (*env)->ReleaseByteArrayElements(env, DashList, DashList1, JNI_ABORT);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetStrokeWidth
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetStrokeWidth
  (JNIEnv *env, jobject that, jint width)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetStrokeWidth\n");
#endif
	
	return (jint)PgSetStrokeWidth(width);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetDrawMode
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetDrawMode
  (JNIEnv *env, jobject that, jint mode)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetDrawMode\n");
#endif
	
	return (jint)PgSetDrawMode(mode);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetClipping
 * Signature: (SI)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetClipping
  (JNIEnv *env, jobject that, jshort n, jint rects)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetClipping\n");
#endif
	
	PgSetClipping(n, (PhRect_t *)rects);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetPalette
 * Signature: (IISSII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetPalette
  (JNIEnv *env, jobject that, jint palette, jint palette_id, jshort first_color, jshort num_colors, jint flags, jint tag)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetPalette\n");
#endif
	
	return (jint)PgSetPalette((PgColor_t *)palette, palette_id, first_color, num_colors, flags, tag);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawArc
 * Signature: (Lorg/eclipse/swt/photon/PhPoint_t;Lorg/eclipse/swt/photon/PhPoint_t;III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawArc
  (JNIEnv *env, jobject that, jobject center, jobject radii, jint start, jint end, jint flags)
{
	DECL_GLOB(pGlob)
    PhPoint_t center1, *lpCenter1=NULL, radii1, *lpRadii1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDrawArc\n");
#endif

    if (center) {
        lpCenter1 = &center1;
        cachePhPoint_tFids(env, center, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, center, lpCenter1, &PGLOB(PhPoint_tFc));
    }
    if (radii) {
        lpRadii1 = &radii1;
        getPhPoint_tFields(env, radii, lpRadii1, &PGLOB(PhPoint_tFc));
    }
    return (jint) PgDrawArc(lpCenter1, lpRadii1, start, end, flags);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawEllipse
 * Signature: (Lorg/eclipse/swt/photon/PhPoint_t;Lorg/eclipse/swt/photon/PhPoint_t;I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawEllipse
  (JNIEnv *env, jobject that, jobject center, jobject radii, jint flags)
{
	DECL_GLOB(pGlob)
    PhPoint_t center1, *lpCenter1=NULL, radii1, *lpRadii1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDrawEllipse\n");
#endif

    if (center) {
        lpCenter1 = &center1;
        cachePhPoint_tFids(env, center, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, center, lpCenter1, &PGLOB(PhPoint_tFc));
    }
    if (radii) {
        lpRadii1 = &radii1;
        getPhPoint_tFields(env, radii, lpRadii1, &PGLOB(PhPoint_tFc));
    }
    return (jint) PgDrawEllipse(lpCenter1, lpRadii1, flags);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawRoundRect
 * Signature: (Lorg/eclipse/swt/photon/PhRect_t;Lorg/eclipse/swt/photon/PhPoint_t;I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawRoundRect
  (JNIEnv *env, jobject that, jobject rect, jobject radii, jint flags)
{
	DECL_GLOB(pGlob)
	PhRect_t rect1, *lpRect1=NULL;
    PhPoint_t radii1, *lpRadii1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDrawRoundRect\n");
#endif

    if (rect) {
        lpRect1 = &rect1;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpRect1, &PGLOB(PhRect_tFc));
    }
    if (radii) {
        lpRadii1 = &radii1;
        cachePhPoint_tFids(env, radii, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, radii, lpRadii1, &PGLOB(PhPoint_tFc));
    }
    return (jint) PgDrawRoundRect(lpRect1, lpRadii1, flags);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawPolygon
 * Signature: ([SI;Lorg/eclipse/swt/photon/PhPoint_t;I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawPolygon
  (JNIEnv *env, jobject that, jshortArray ptr, jint num, jobject pos, jint flags)
{
	DECL_GLOB(pGlob)
    jshort *ptr1=NULL;
    PhPoint_t pos1, *lpPos1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDrawPolygon\n");
#endif

    if (ptr)
        ptr1 = (*env)->GetShortArrayElements(env, ptr, NULL);

    if (pos) {
        lpPos1 = &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }

    result = (jint) PgDrawPolygon((PhPoint_t *)ptr1, num, lpPos1, flags);

    if (ptr)
        (*env)->ReleaseShortArrayElements(env, ptr, ptr1, 0);

    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawILine
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawILine
  (JNIEnv *env, jobject that, jint x1, jint y1, jint x2, jint y2)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDrawILine\n");
#endif
	
	return (jint)PgDrawILine(x1, y1, x2, y2);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawIRect
 * Signature: (IIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawIRect
  (JNIEnv *env, jobject that, jint ulx, jint uly, jint lrx, jint lry, jint flags)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDrawIRect\n");
#endif
	
	return (jint)PgDrawIRect(ulx, uly, lrx, lry, flags);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetMultiClip
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetMultiClip
  (JNIEnv *env, jobject that, jint num, jint clip_list)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetMultiClip\n");
#endif
	
	return (jint)PgSetMultiClip(num, (PhRect_t *)clip_list);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetUserClip
 * Signature: (I)I
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetUserClip__I
  (JNIEnv *env, jobject that, jint ClipRect)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetUserClip\n");
#endif
	
	PgSetUserClip((PhRect_t *)ClipRect);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetUserClip
 * Signature: (Lorg/eclipse/swt/internal/photon/PhRect_t;)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetUserClip__Lorg_eclipse_swt_internal_photon_PhRect_1t_2
  (JNIEnv *env, jobject that, jobject ClipRect)
{
	DECL_GLOB(pGlob)
	PhRect_t ClipRect1, *lpClipRect1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetUserClip\n");
#endif
	
    if (ClipRect) {
        lpClipRect1 = &ClipRect1;
        cachePhRect_tFids(env, ClipRect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, ClipRect, lpClipRect1, &PGLOB(PhRect_tFc));
    }
	PgSetUserClip(lpClipRect1);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetDrawBufferSize
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetDrawBufferSize
  (JNIEnv *env, jobject that, jint cmd_buf_len)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetDrawBufferSize\n");
#endif
	
	return (jint)PgSetDrawBufferSize(cmd_buf_len);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDestroyGC
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDestroyGC
  (JNIEnv *env, jobject that, jint GC)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDestroyGC\n");
#endif
	
	PgDestroyGC((PhGC_t *)GC);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawImage
 * Signature: (IILorg/eclipse/swt/photon/PhPoint_t;Lorg/eclipse/swt/photon/PhDim_t;II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawImage
  (JNIEnv *env, jobject that, jint ptr, jint type, jobject pos, jobject size, jint bpl, jint tag)
{
	DECL_GLOB(pGlob)
    PhPoint_t pos1, *lpPos1=NULL;
    PhDim_t size1, *lpSize1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDrawImage\n");
#endif

    if (pos) {
        lpPos1 = &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (size) {
        lpSize1 = &size1;
        cachePhDim_tFids(env, size, &PGLOB(PhDim_tFc));
        getPhDim_tFields(env, size, lpSize1, &PGLOB(PhDim_tFc));
    }
    return (jint) PgDrawImage((void *)ptr, type, lpPos1, lpSize1, bpl, tag);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawTImage
 * Signature: (IILorg/eclipse/swt/photon/PhPoint_t;Lorg/eclipse/swt/photon/PhDim_t;IIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawTImage
  (JNIEnv *env, jobject that, jint ptr, jint type, jobject pos, jobject size, jint bpl, jint tag, jint TransPtr, jint TransBPL)
{
	DECL_GLOB(pGlob)
    PhPoint_t pos1, *lpPos1=NULL;
    PhDim_t size1, *lpSize1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDrawTImage\n");
#endif

    if (pos) {
        lpPos1 = &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (size) {
        lpSize1 = &size1;
        cachePhDim_tFids(env, size, &PGLOB(PhDim_tFc));
        getPhDim_tFields(env, size, lpSize1, &PGLOB(PhDim_tFc));
    }
    return (jint) PgDrawTImage((void *)ptr, type, lpPos1, lpSize1, bpl, tag, (void *)TransPtr, TransBPL);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PiCropImage
 * Signature: (ILorg/eclipse/swt/photon/PhRect_t;I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PiCropImage
  (JNIEnv *env, jobject that, jint image, jobject bounds, jint flags)
{
	DECL_GLOB(pGlob)
    PhRect_t bounds1, *lpBounds1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PiCropImage\n");
#endif

    if (bounds) {
        lpBounds1 = &bounds1;
        cachePhRect_tFids(env, bounds, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, bounds, lpBounds1, &PGLOB(PhRect_tFc));
    }
    return (jint) PiCropImage((PhImage_t *)image, lpBounds1, flags);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawBitmap
 * Signature: (IILorg/eclipse/swt/photon/PhPoint_t;Lorg/eclipse/swt/photon/PhDim_t;II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawBitmap
  (JNIEnv *env, jobject that, jint ptr, jint flags, jobject pos, jobject size, jint bpl, jint tag)
{
	DECL_GLOB(pGlob)
    PhPoint_t pos1, *lpPos1=NULL;
    PhDim_t size1, *lpSize1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDrawBitmap\n");
#endif

    if (pos) {
        lpPos1 = &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (size) {
        lpSize1 = &size1;
        cachePhDim_tFids(env, size, &PGLOB(PhDim_tFc));
        getPhDim_tFields(env, size, lpSize1, &PGLOB(PhDim_tFc));
    }
    return (jint) PgDrawBitmap((void *)ptr, flags, lpPos1, (PhPoint_t *)lpSize1, bpl, tag);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawPhImageRectmx
 * Signature: (Lorg/eclipse/swt/photon/PhPoint_t;ILorg/eclipse/swt/photon/PhRect_t;I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawPhImageRectmx
  (JNIEnv *env, jobject that, jobject pos, int image, jobject rect, jint flags)
{
	DECL_GLOB(pGlob)
    PhPoint_t pos1, *lpPos1=NULL;
    PhRect_t rect1, *lpRect1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDrawPhImageRectmx\n");
#endif

    if (pos) {
        lpPos1 = &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (rect) {
        lpRect1 = &rect1;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpRect1, &PGLOB(PhRect_tFc));
    }
    return (jint) PgDrawPhImageRectmx(lpPos1, (PhImage_t *)image, lpRect1, flags);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawPhImagemx
 * Signature: (Lorg/eclipse/swt/photon/PhPoint_t;ILorg/eclipse/swt/photon/PhRect_t;I)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawPhImagemx
  (JNIEnv *env, jobject that, jobject pos, int image, jint flags)
{
	DECL_GLOB(pGlob)
    PhPoint_t pos1, *lpPos1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDrawPhImagemx\n");
#endif

    if (pos) {
        lpPos1 = &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    return (jint) PgDrawPhImagemx(lpPos1, (PhImage_t *)image, flags);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhBlit
 * Signature: (ILorg/eclipse/swt/photon/PhRect_t;ILorg/eclipse/swt/photon/PhPoint_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhBlit
  (JNIEnv *env, jobject that, jint rid, jobject rect, jobject offset)
{
	DECL_GLOB(pGlob)
    PhRect_t rect1, *lpRect1=NULL;
    PhPoint_t offset1, *lpOffset1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhBlit\n");
#endif

    if (rect) {
        lpRect1 = &rect1;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpRect1, &PGLOB(PhRect_tFc));
    }
    if (offset) {
        lpOffset1 = &offset1;
        cachePhPoint_tFids(env, offset, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, offset, lpOffset1, &PGLOB(PhPoint_tFc));
    }
    return (jint) PhBlit((PhRid_t)rid, lpRect1, lpOffset1);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhMakeTransBitmap
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhMakeTransBitmap
  (JNIEnv *env, jobject that, jint image, jint trans_color)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhMakeTransBitmap\n");
#endif

    return (jint) PhMakeTransBitmap((PhImage_t *)image, (PgColor_t)trans_color);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtEnter
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtEnter
  (JNIEnv *env, jobject that, jint flags)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtEnter\n");
#endif
	
	return (jint)PtEnter(flags);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtLeave
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtLeave
  (JNIEnv *env, jobject that, jint flags)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtLeave\n");
#endif
	
	return (jint)PtLeave(flags);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtGetResources
 * Signature: (II[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtGetResources
  (JNIEnv *env, jobject that, jint widget, jint n_args, jintArray args)
{
    jint *args1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtGetResources\n");
#endif

    if (args)
        args1 = (*env)->GetIntArrayElements(env, args, NULL);

    result = (jint)PtGetResources((PtWidget_t *)widget, n_args, (PtArg_t *)args1);

    if (args)
        (*env)->ReleaseIntArrayElements(env, args, args1, 0);
	
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtDestroyWidget
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtDestroyWidget
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtDestroyWidget\n");
#endif
	
	return (jint)PtDestroyWidget((PtWidget_t *)widget);
}  

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhEventPeek
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhEventPeek
  (JNIEnv *env, jobject that, jint buffer, jint size)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhEventPeek\n");
#endif
	
	return (jint)PhEventPeek((void *)buffer, size);
} 

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhEventNext
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhEventNext
  (JNIEnv *env, jobject that, jint buffer, jint size)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhEventNext\n");
#endif
	
	return (jint)PhEventNext((void *)buffer, size);
} 

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhGetMsgSize
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhGetMsgSize
  (JNIEnv *env, jobject that, jint event_buf)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhGetMsgSize\n");
#endif
	
	return (jint)PhGetMsgSize((PhEvent_t *)event_buf);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhGetTile
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhGetTile
  (JNIEnv *env, jobject that)
{
	PhTile_t *tile;
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhGetTile\n");
#endif
	
	tile = PhGetTile();
	memset(tile, 0, sizeof(PhTile_t));
	return (jint)tile;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhTranslateTiles
 * Signature: (ILorg/eclipse/swt/internal/photon/PhPoint_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhTranslateTiles
  (JNIEnv *env, jobject that, jint tile, jobject point_add)
{

	DECL_GLOB(pGlob)
	
    PhPoint_t point_add1, *lpPoint_add1=NULL;
	jint result;
	
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhTranslateTiles\n");
#endif

	if (point_add) {
        lpPoint_add1= &point_add1;
        cachePhPoint_tFids(env, point_add, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, point_add, lpPoint_add1, &PGLOB(PhPoint_tFc));
    }
    result = (jint)PhTranslateTiles((PhTile_t *)tile, lpPoint_add1);

    if (point_add) {
        setPhPoint_tFields(env, point_add, lpPoint_add1, &PGLOB(PhPoint_tFc));
    }
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhDeTranslateTiles
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhDeTranslateTiles
  (JNIEnv *env, jobject that, jint tile, jint point_sutract)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhDeTranslateTiles\n");
#endif
	
	return (jint)PhDeTranslateTiles((PhTile_t *)tile, (PhPoint_t *)point_sutract);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhAddMergeTiles
 * Signature: (II[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhAddMergeTiles
  (JNIEnv *env, jobject that, jint tiles, jint add_tiles, jintArray added)
{
	jint *added1 = NULL;
	jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhAddMergeTiles\n");
#endif

    if (added)
        added1 = (*env)->GetIntArrayElements(env, added, NULL);

	result = (jint)PhAddMergeTiles((PhTile_t *)tiles, (PhTile_t *)add_tiles, added1);

    if (added)
        (*env)->ReleaseIntArrayElements(env, added, added1, 0);

	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhIntersectTilings
 * Signature: (II[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhIntersectTilings
  (JNIEnv *env, jobject that, jint tile1, jint tile2, jshortArray num_intersect_tiles)
{
	jshort *num_intersect_tiles1 = NULL;
	jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhIntersectTilings\n");
#endif

    if (num_intersect_tiles)
        num_intersect_tiles1 = (*env)->GetShortArrayElements(env, num_intersect_tiles, NULL);

	result = (jint)PhIntersectTilings((PhTile_t *)tile1, (PhTile_t *)tile2, num_intersect_tiles1);

    if (num_intersect_tiles)
        (*env)->ReleaseShortArrayElements(env, num_intersect_tiles, num_intersect_tiles1, 0);

	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhCoalesceTiles
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhCoalesceTiles
  (JNIEnv *env, jobject that, jint tiles)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhCoalesceTiles\n");
#endif
	
	return (jint)PhCoalesceTiles((PhTile_t *)tiles);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhMergeTiles
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhMergeTiles
  (JNIEnv *env, jobject that, jint tiles)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhMergeTiles\n");
#endif
	
	return (jint)PhMergeTiles((PhTile_t *)tiles);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhRectUnion
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhRectUnion__II
  (JNIEnv *env, jobject that, jint rect1, jint rect2)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhRectUnion__II\n");
#endif
	
	return (jint)PhRectUnion((PhRect_t *)rect1, (PhRect_t *)rect2);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhRectIntersect
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhRectIntersect
  (JNIEnv *env, jobject that, jint rect1, jint rect2)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhRectIntersect\n");
#endif
	
	return (jint)PhRectIntersect((PhRect_t *)rect1, (PhRect_t *)rect2);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhClipTilings
 * Signature: (II[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhClipTilings
  (JNIEnv *env, jobject that, jint tiles, jint clip_tiles, jintArray intersection)
{
    jint *intersection1 = NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhClipTilings\n");
#endif

    if (intersection)
        intersection1 = (*env)->GetIntArrayElements(env, intersection, NULL);

    result = (jint)PhClipTilings((PhTile_t *)tiles, (PhTile_t *)clip_tiles, (PhTile_t **)intersection1);

    if (intersection)
        (*env)->ReleaseIntArrayElements(env, intersection, intersection1, 0);
	
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhTilesToRects
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhTilesToRects
  (JNIEnv *env, jobject that, jint tiles, jintArray num_rects)
{
    jint *num_rects1 = NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhTilesToRects\n");
#endif

    if (num_rects)
        num_rects1 = (*env)->GetIntArrayElements(env, num_rects, NULL);

    result = (jint)PhTilesToRects((PhTile_t *)tiles, (int *)num_rects1);

    if (num_rects)
        (*env)->ReleaseIntArrayElements(env, num_rects, num_rects1, 0);
	
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhRectsToTiles
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhRectsToTiles
  (JNIEnv *env, jobject that, jint rects, jint num_rects)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhRectsToTiles\n");
#endif

    return (jint)PhRectsToTiles((PhRect_t *)rects, num_rects);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhFreeTiles
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PhFreeTiles
  (JNIEnv *env, jobject that, jint tiles)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhFreeTiles\n");
#endif
	
	PhFreeTiles((PhTile_t *)tiles);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhCopyTiles
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhCopyTiles
  (JNIEnv *env, jobject that, jint tile)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhCopyTiles\n");
#endif
	
	return (jint) PhCopyTiles((PhTile_t *)tile);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtEventHandler
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtEventHandler
  (JNIEnv *env, jobject that, jint event)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtEventHandler\n");
#endif
	
	return (jint)PtEventHandler((PhEvent_t *)event);
}  

/*
 * Class:	org_eclipse_swt_internal_photon_OS
 * Method:	malloc
 * Signature: (I)I 
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_malloc
  (JNIEnv *env, jclass that, jint size)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "malloc\n");
#endif

	return (jint)calloc(1, (size_t)size);
}

/*
 * Class:	org_eclipse_swt_internal_photon_OS
 * Method:	free
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_free
  (JNIEnv *env, jclass that, jint ptr)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "free\n");
#endif

	free((void *)ptr);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PhPoint_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PhPoint_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PhPoint_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PhPoint_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhPoint_tFids(env, src, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, src, src1, &PGLOB(PhPoint_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PhPoint_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PhPoint_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PhPoint_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhPoint_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePhPoint_tFids(env, dest, &PGLOB(PhPoint_tFc));
        setPhPoint_tFields(env, dest, dest1, &PGLOB(PhPoint_tFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhWindowQueryVisible
 * Signature: (IIILorg/eclipse/swt/internal/photon/PhRect_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhWindowQueryVisible
  (JNIEnv *env, jobject that, jint flag, jint rid, jint input_group, jobject rectangle)
{
	DECL_GLOB(pGlob)
	int result;
	PhRect_t rect, *lpRect=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PhWindowQueryVisible\n");
#endif

    if (rectangle) {
        lpRect = &rect;
        cachePhRect_tFids(env, rectangle, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rectangle, lpRect, &PGLOB(PhRect_tFc));
    }
    result =PhWindowQueryVisible(flag, rid, input_group, lpRect);
    if (rectangle) {
        setPhRect_tFields(env, rectangle, lpRect, &PGLOB(PhRect_tFc));
    }
    
    return (jint) result;
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (I[II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__I_3II
  (JNIEnv *env, jclass that, jint dest, jintArray src, jint count)
{
    jint *src1;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__I_3II\n");
#endif

    /* don't do anything if src pointer is NULL */
    if (src) {
        src1 = (*env)->GetIntArrayElements(env, src, NULL);
        memmove((void *)dest, (void *)src1, count);
        (*env)->ReleaseIntArrayElements(env, src, src1, 0);
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: ([III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove___3III
  (JNIEnv *env, jclass that, jintArray dest, jint src, jint count)
{
    jint *dest1;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove___3III\n");
#endif

    /* don't do anything if dest pointer is NULL */
    if (dest) {
        dest1 = (*env)->GetIntArrayElements(env, dest, NULL);
        memmove((void *)dest1, (void *)src, count);
        (*env)->ReleaseIntArrayElements(env, dest, dest1, 0);    
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawText
 * Signature: ([CISSI)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawText
  (JNIEnv *env, jclass that, jcharArray ptr, jint len, jshort x, jshort y, jint flags)
{
	jint result = 0;
    jchar * ptr1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PgDrawText\n");
#endif

    if (ptr) {
    	PhPoint_t pos;

    	pos.x = x;
    	pos.y = y;
    	ptr1 = (*env)->GetCharArrayElements(env, ptr, NULL);

		result = (jint)PgDrawText((char *)ptr1, len, &pos, (int)flags);
		
        (*env)->ReleaseCharArrayElements(env, ptr, ptr1, JNI_ABORT);    
    }
    
    return result;
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawText
 * Signature: ([BISSI)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawText
  (JNIEnv *env, jclass that, jbyteArray ptr, jint len, jshort x, jshort y, jint flags)
{
	jint result = 0;
    jbyte * ptr1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PgDrawText\n");
#endif

    /* don't do anything if ptr is NULL */
    if (ptr) {
    	PhPoint_t pos;

    	pos.x = x;
    	pos.y = y;
    	ptr1 = (*env)->GetByteArrayElements(env, ptr, NULL);

		result = (jint)PgDrawText((char *)ptr1, len, &pos, (int)flags);
		
        (*env)->ReleaseByteArrayElements(env, ptr, ptr1, JNI_ABORT);    
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawMultiTextArea
 * Signature: ([BILorg/eclipse/swt/photon/PhRect_t;III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawMultiTextArea
  (JNIEnv *env, jclass that, jbyteArray ptr, jint len, jobject canvas, jint text_flags, jint canvas_flags, int linespacing)
{
	DECL_GLOB(pGlob)
	jint result = 0;
    jbyte * ptr1 = NULL;
    PhRect_t canvas1, *lpCanvas1;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PgDrawMultiTextArea\n");
#endif

    /* don't do anything if ptr is NULL */
    if (!ptr) return 0;
 
	if (canvas) {
        lpCanvas1 = &canvas1;
        cachePhRect_tFids(env, canvas, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, canvas, lpCanvas1, &PGLOB(PhRect_tFc));
    }
    ptr1 = (*env)->GetByteArrayElements(env, ptr, NULL);

	result = (jint)PgDrawMultiTextArea((char *)ptr1, len, lpCanvas1, text_flags, canvas_flags, linespacing);
		
	(*env)->ReleaseByteArrayElements(env, ptr, ptr1, JNI_ABORT);    
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetExtent
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetExtent__II
  (JNIEnv *env, jobject that, jint widget, jint extent)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetExtent\n");
#endif
	
	return (jint)PtWidgetExtent((PtWidget_t *)widget, (PhRect_t *)extent);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetChildBack
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetChildBack
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetChildBack\n");
#endif
	
	return (jint)PtWidgetChildBack((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetIsRealized
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetIsRealized
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetIsRealized\n");
#endif
	
	return (jint)PtWidgetIsRealized((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetBrotherInFront
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetBrotherInFront
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetBrotherInFront\n");
#endif
	
	return (jint)PtWidgetBrotherInFront((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetParent
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetParent
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetParent\n");
#endif
	
	return (jint)PtWidgetParent((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PhTile_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PhTile_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PhTile_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PhTile_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhTile_tFids(env, src, &PGLOB(PhTile_tFc));
        getPhTile_tFields(env, src, src1, &PGLOB(PhTile_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PhTile_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PhTile_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PhTile_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhTile_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePhTile_tFids(env, dest, &PGLOB(PhTile_tFc));
        setPhTile_tFields(env, dest, dest1, &PGLOB(PhTile_tFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PtCallbackInfo_t;I)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PtCallbackInfo_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PtCallbackInfo_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PtCallbackInfo_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePtCallbackInfo_tFids(env, src, &PGLOB(PtCallbackInfo_tFc));
        getPtCallbackInfo_tFields(env, src, src1, &PGLOB(PtCallbackInfo_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PtCallbackInfo_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PtCallbackInfo_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PtCallbackInfo_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PtCallbackInfo_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePtCallbackInfo_tFids(env, dest, &PGLOB(PtCallbackInfo_tFc));
        setPtCallbackInfo_tFields(env, dest, dest1, &PGLOB(PtCallbackInfo_tFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PhWindowEvent_t;I)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PhWindowEvent_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PhWindowEvent_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PhWindowEvent_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhWindowEvent_tFids(env, src, &PGLOB(PhWindowEvent_tFc));
        getPhWindowEvent_tFields(env, src, src1, &PGLOB(PhWindowEvent_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PhWindowEvent_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PhWindowEvent_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PhWindowEvent_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhWindowEvent_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePhWindowEvent_tFids(env, dest, &PGLOB(PhWindowEvent_tFc));
        setPhWindowEvent_tFields(env, dest, dest1, &PGLOB(PhWindowEvent_tFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAddCallback
 * Signature: (IIII)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAddCallback
  (JNIEnv *env, jobject that, jint widget, jint callback_type, jint callback, jint data)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAddEventHandler\n");
#endif
	
	PtAddCallback((PtWidget_t *)widget, (unsigned long)callback_type, (PtCallbackF_t *)callback, (void *)data);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetChildFront
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetChildFront
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetChildFront\n");
#endif
	
	return (jint) PtWidgetChildFront((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetBrotherBehind
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetBrotherBehind
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetBrotherBehind\n");
#endif
	
	return (jint) PtWidgetBrotherBehind((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (I[BI)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__I_3BI
  (JNIEnv *env, jclass that, jint dest, jbyteArray src, jint count)
{
    jbyte *src1;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__I_3BI\n");
#endif

    /* don't do anything if src pointer is NULL */
    if (src) {
        src1 = (*env)->GetByteArrayElements(env, src, NULL);
        memmove((void *)dest, (void *)src1, count);
        (*env)->ReleaseByteArrayElements(env, src, src1, 0);
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: ([BII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove___3BII
  (JNIEnv *env, jclass that, jbyteArray dest, jint src, jint count)
{
    jbyte *dest1;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove___3BII\n");
#endif

    /* don't do anything if dest pointer is NULL */
    if (dest) {
        dest1 = (*env)->GetByteArrayElements(env, dest, NULL);
        memmove((void *)dest1, (void *)src, count);
        (*env)->ReleaseByteArrayElements(env, dest, dest1, 0);
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWindow
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtButton
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtButton\n");
#endif
	
	return (jint)PtButton;
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PhRect_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PhRect_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PhRect_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PhPoint_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhRect_tFids(env, src, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, src, src1, &PGLOB(PhRect_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PhRect_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PhRect_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PhRect_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhPoint_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePhRect_tFids(env, dest, &PGLOB(PhRect_tFc));
        setPhRect_tFields(env, dest, dest1, &PGLOB(PhRect_tFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtUnrealizeWidget
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtUnrealizeWidget
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtUnrealizeWidget\n");
#endif
	
	return (jint)PtUnrealizeWidget((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtSyncWidget
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtSyncWidget
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtSyncWidget\n");
#endif
	
	return (jint)PtSyncWidget((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtFlush
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtFlush
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtFlush\n");
#endif
	
	return (jint)PtFlush();
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtContainerGiveFocus
 * Signature: (ILorg/eclipse/swt/internal/photon/PhEvent_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtContainerGiveFocus
  (JNIEnv *env, jobject that, jint widget, jobject event)
{
	DECL_GLOB(pGlob)
	jint result;
	PhEvent_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtContainerGiveFocus\n");
#endif

    if (event) {
        lpObject = &object;
        cachePhEvent_tFids(env, event, &PGLOB(PhEvent_tFc));
        getPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    result = (jint) PtContainerGiveFocus((PtWidget_t *)widget, lpObject);
    if (event) {
        setPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtContainerFocusNext
 * Signature: (ILorg/eclipse/swt/internal/photon/PhEvent_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtContainerFocusNext
  (JNIEnv *env, jobject that, jint widget, jobject event)
{
	DECL_GLOB(pGlob)
	jint result;
	PhEvent_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtContainerFocusNext\n");
#endif

    if (event) {
        lpObject = &object;
        cachePhEvent_tFids(env, event, &PGLOB(PhEvent_tFc));
        getPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    result = (jint) PtContainerFocusNext((PtWidget_t *)widget, lpObject);
    if (event) {
        setPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtContainerFocusPrev
 * Signature: (ILorg/eclipse/swt/internal/photon/PhEvent_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtContainerFocusPrev
  (JNIEnv *env, jobject that, jint widget, jobject event)
{
	DECL_GLOB(pGlob)
	jint result;
	PhEvent_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtContainerFocusPrev\n");
#endif

    if (event) {
        lpObject = &object;
        cachePhEvent_tFids(env, event, &PGLOB(PhEvent_tFc));
        getPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    result = (jint) PtContainerFocusPrev((PtWidget_t *)widget, lpObject);
    if (event) {
        setPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtGlobalFocusNext
 * Signature: (ILorg/eclipse/swt/internal/photon/PhEvent_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtGlobalFocusNext
  (JNIEnv *env, jobject that, jint widget, jobject event)
{
	DECL_GLOB(pGlob)
	jint result;
	PhEvent_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtGlobalFocusNext\n");
#endif

    if (event) {
        lpObject = &object;
        cachePhEvent_tFids(env, event, &PGLOB(PhEvent_tFc));
        getPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    result = (jint) PtGlobalFocusNext((PtWidget_t *)widget, lpObject);
    if (event) {
        setPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtGlobalFocusPrev
 * Signature: (ILorg/eclipse/swt/internal/photon/PhEvent_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtGlobalFocusPrev
  (JNIEnv *env, jobject that, jint widget, jobject event)
{
	DECL_GLOB(pGlob)
	jint result;
	PhEvent_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtGlobalFocusPrev\n");
#endif

    if (event) {
        lpObject = &object;
        cachePhEvent_tFids(env, event, &PGLOB(PhEvent_tFc));
        getPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    result = (jint) PtGlobalFocusPrev((PtWidget_t *)widget, lpObject);
    if (event) {
        setPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtGlobalFocusNextContainer
 * Signature: (ILorg/eclipse/swt/internal/photon/PhEvent_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtGlobalFocusNextContainer
  (JNIEnv *env, jobject that, jint widget, jobject event)
{
	DECL_GLOB(pGlob)
	jint result;
	PhEvent_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtGlobalFocusNextContainer\n");
#endif

    if (event) {
        lpObject = &object;
        cachePhEvent_tFids(env, event, &PGLOB(PhEvent_tFc));
        getPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    result = (jint) PtGlobalFocusNextContainer((PtWidget_t *)widget, lpObject);
    if (event) {
        setPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtGlobalFocusPrevContainer
 * Signature: (ILorg/eclipse/swt/internal/photon/PhEvent_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtGlobalFocusPrevContainer
  (JNIEnv *env, jobject that, jint widget, jobject event)
{
	DECL_GLOB(pGlob)
	jint result;
	PhEvent_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtGlobalFocusPrevContainer\n");
#endif

    if (event) {
        lpObject = &object;
        cachePhEvent_tFids(env, event, &PGLOB(PhEvent_tFc));
        getPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    result = (jint) PtGlobalFocusPrevContainer((PtWidget_t *)widget, lpObject);
    if (event) {
        setPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetToFront
  * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetToFront
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetToFront\n");
#endif
	
	return (jint) PtWidgetToFront((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetToBack
  * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetToBack
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetToBack\n");
#endif
	
	return (jint) PtWidgetToBack((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetInsert
  * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetInsert
  (JNIEnv *env, jobject that, jint widget, jint new_sibling, jint behind)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetInsert\n");
#endif
	
	return (jint) PtWidgetInsert((PtWidget_t *)widget, (PtWidget_t *)new_sibling, behind);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtDamageExtent
 * Signature: (ILorg/eclipse/swt/internal/photon/PhRect_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtDamageExtent
  (JNIEnv *env, jobject that, jint widget, jobject extent)
{
	DECL_GLOB(pGlob)
	jint result;
	PhRect_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtContainerGiveFocus\n");
#endif

    if (extent) {
        lpObject = &object;
        cachePhRect_tFids(env, extent, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, extent, lpObject, &PGLOB(PhRect_tFc));
    }
    result = (jint) PtDamageExtent((PtWidget_t *)widget, lpObject);
    if (extent) {
        setPhRect_tFields(env, extent, lpObject, &PGLOB(PhRect_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__III
  (JNIEnv *env, jobject that, jint dest, jint src, jint size)
{
	return (jint)memmove((void *)dest, (void *)src, size);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PfQueryFontInfo
 * Signature: ([BLorg/eclipse/swt/internal/photon/FontQueryInfo;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PfQueryFontInfo
  (JNIEnv *env, jobject that, jbyteArray font, jobject info)
{
	DECL_GLOB(pGlob)
    jbyte *font1=NULL;
	FontQueryInfo info1, *lpInfo1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PfQueryFontInfo\n");
#endif

    if (font)
        font1 = (*env)->GetByteArrayElements(env, font, NULL);
    if (info) {
        lpInfo1 = &info1;
        cacheFontQueryInfoFids(env, info, &PGLOB(FontQueryInfoFc));
        getFontQueryInfoFields(env, info, lpInfo1, &PGLOB(FontQueryInfoFc));
    }

    result = (jint)PfQueryFontInfo(font1, lpInfo1);

    if (font)
        (*env)->ReleaseByteArrayElements(env, font, font1, JNI_ABORT);
    if (info) {
        setFontQueryInfoFields(env, info, lpInfo1, &PGLOB(FontQueryInfoFc));
    }
	
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PfQueryFonts
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PfQueryFonts
  (JNIEnv *env, jobject that, jint symbol, jint flags, jint list, jint n)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PfQueryFonts\n");
#endif

    return (jint)PfQueryFonts(symbol, flags, (FontDetails *)list, n);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PfExtentWideText
 * Signature: (Lorg/eclipse/swt/internal/photon/PhRect_t;Lorg/eclipse/swt/internal/photon/PhPoint_t;[B[CI)Lorg/eclipse/swt/internal/photon/PhRect_t;
 */
JNIEXPORT jobject JNICALL Java_org_eclipse_swt_internal_photon_OS_PfExtentWideText
  (JNIEnv *env, jobject that, jobject extent, jobject pos, jbyteArray font, jcharArray str, jint len)
{
	DECL_GLOB(pGlob)
	PhRect_t extent1, *lpExtent1=NULL;
	PhPoint_t pos1, *lpPos1=NULL;
    jbyte *font1=NULL;
    jchar *str1=NULL;
    PhRect_t * result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PfExtentWideText\n");
#endif

    if (extent) {
        lpExtent1 = &extent1;
        cachePhRect_tFids(env, extent, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, extent, lpExtent1, &PGLOB(PhRect_tFc));
    }
    if (pos) {
        lpPos1 = &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (font)
        font1 = (*env)->GetByteArrayElements(env, font, NULL);
    if (str)
        str1 = (*env)->GetCharArrayElements(env, str, NULL);

    result = PfExtentWideText(lpExtent1, lpPos1, font1, str1, len);

    if (extent) {
        setPhRect_tFields(env, extent, lpExtent1, &PGLOB(PhRect_tFc));
    }
    if (pos) {
        setPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (font)
        (*env)->ReleaseByteArrayElements(env, font, font1, JNI_ABORT);
    if (str)
        (*env)->ReleaseCharArrayElements(env, str, str1, JNI_ABORT);
	
	return result != NULL ? extent : NULL;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgExtentMultiText
 * Signature: (Lorg/eclipse/swt/internal/photon/PhRect_t;Lorg/eclipse/swt/internal/photon/PhPoint_t;[B[BII)Lorg/eclipse/swt/internal/photon/PhRect_t;
 */
JNIEXPORT jobject JNICALL Java_org_eclipse_swt_internal_photon_OS_PgExtentMultiText
  (JNIEnv *env, jobject that, jobject extent, jobject pos, jbyteArray font, jbyteArray str, jint n, int linespacing)
{
	DECL_GLOB(pGlob)
	PhRect_t extent1, *lpExtent1=NULL;
	PhPoint_t pos1, *lpPos1=NULL;
    jbyte *font1=NULL;
    jbyte *str1=NULL;
    PhRect_t * result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgExtentMultiText\n");
#endif

    if (extent) {
        lpExtent1 = &extent1;
        cachePhRect_tFids(env, extent, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, extent, lpExtent1, &PGLOB(PhRect_tFc));
    }
    if (pos) {
        lpPos1 = &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (font)
        font1 = (*env)->GetByteArrayElements(env, font, NULL);
    if (str)
        str1 = (*env)->GetByteArrayElements(env, str, NULL);

    result = PgExtentMultiText(lpExtent1, lpPos1, font1, str1, n, linespacing);

    if (extent) {
        setPhRect_tFields(env, extent, lpExtent1, &PGLOB(PhRect_tFc));
    }
    if (pos) {
        setPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (font)
        (*env)->ReleaseByteArrayElements(env, font, font1, JNI_ABORT);
    if (str)
        (*env)->ReleaseByteArrayElements(env, str, str1, JNI_ABORT);
	
	return result != NULL ? extent : NULL;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PfExtentText
 * Signature: (Lorg/eclipse/swt/internal/photon/PhRect_t;Lorg/eclipse/swt/internal/photon/PhPoint_t;[B[BI)Lorg/eclipse/swt/internal/photon/PhRect_t;
 */
JNIEXPORT jobject JNICALL Java_org_eclipse_swt_internal_photon_OS_PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2_3B_3BI
  (JNIEnv *env, jclass that, jobject extent, jobject pos, jbyteArray font, jbyteArray str, jint len)
{
	DECL_GLOB(pGlob)
	PhRect_t extent1, *lpExtent1=NULL;
	PhPoint_t pos1, *lpPos1=NULL;
    jbyte *str1=NULL;
    jbyte *font1=NULL;
    PhRect_t * result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PfExtentText\n");
#endif

    if (extent) {
        lpExtent1 = &extent1;
        cachePhRect_tFids(env, extent, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, extent, lpExtent1, &PGLOB(PhRect_tFc));
    }
    if (pos) {
        lpPos1 = &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (font)
        font1 = (*env)->GetByteArrayElements(env, font, NULL);
    if (str)
        str1 = (*env)->GetByteArrayElements(env, str, NULL);

    result = PfExtentText(lpExtent1, lpPos1, font1, str1, len);

    if (extent) {
        setPhRect_tFields(env, extent, lpExtent1, &PGLOB(PhRect_tFc));
    }
    if (pos) {
        setPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (font)
        (*env)->ReleaseByteArrayElements(env, font, font1, JNI_ABORT);
    if (str)
        (*env)->ReleaseByteArrayElements(env, str, str1, JNI_ABORT);
	
	return result != NULL ? extent : NULL;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PfExtentText
 * Signature: (Lorg/eclipse/swt/internal/photon/PhRect_t;Lorg/eclipse/swt/internal/photon/PhPoint_t;III)Lorg/eclipse/swt/internal/photon/PhRect_t;
 */
JNIEXPORT jobject JNICALL Java_org_eclipse_swt_internal_photon_OS_PfExtentText__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhPoint_1t_2III
  (JNIEnv *env, jclass that, jobject extent, jobject pos, jint font, jint str, jint len)
{
	DECL_GLOB(pGlob)
	PhRect_t extent1, *lpExtent1=NULL;
	PhPoint_t pos1, *lpPos1=NULL;
    PhRect_t * result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PfExtentText\n");
#endif

    if (extent) {
        lpExtent1 = &extent1;
        cachePhRect_tFids(env, extent, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, extent, lpExtent1, &PGLOB(PhRect_tFc));
    }
    if (pos) {
        lpPos1 = &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }

    result = PfExtentText(lpExtent1, lpPos1, (char *)font, (char *)str, len);

    if (extent) {
        setPhRect_tFields(env, extent, lpExtent1, &PGLOB(PhRect_tFc));
    }
    if (pos) {
        setPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
	
	return result != NULL ? extent : NULL;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PhDim_t;I)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PhDim_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PhDim_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PhDim_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhDim_tFids(env, src, &PGLOB(PhDim_tFc));
        getPhDim_tFields(env, src, src1, &PGLOB(PhDim_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PhDim_t;II)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PhDim_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PhDim_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhDim_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePhDim_tFids(env, dest, &PGLOB(PhDim_tFc));
        setPhDim_tFields(env, dest, dest1, &PGLOB(PhDim_tFc));
    }

}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PhImage_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PhImage_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PhImage_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PhImage_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhImage_tFids(env, src, &PGLOB(PhImage_tFc));
        getPhImage_tFields(env, src, src1, &PGLOB(PhImage_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PhImage_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PhImage_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PhImage_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhImage_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePhImage_tFids(env, dest, &PGLOB(PhImage_tFc));
        setPhImage_tFields(env, dest, dest1, &PGLOB(PhImage_tFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhCreateImage
 * Signature: (Lorg/eclipse/swt/photon/PhImage_t;SSI[III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhCreateImage__Lorg_eclipse_swt_internal_photon_PhImage_1t_2SSI_3III
  (JNIEnv *env, jobject that, jobject buffer, jshort width, jshort height, jint type, jintArray palette, jint ncolors, jint tag)
{
	DECL_GLOB(pGlob)
	jint result;
    jint *palette1 = NULL;
    PhImage_t buffer1, *lpBuffer1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhCreateImage\n");
#endif

    if (buffer) {
        lpBuffer1 = &buffer1;
        cachePhImage_tFids(env, buffer, &PGLOB(PhImage_tFc));
        getPhImage_tFields(env, buffer, lpBuffer1, &PGLOB(PhImage_tFc));
    }
	if (palette)
        palette1 = (*env)->GetIntArrayElements(env, palette, NULL);

    result = (jint)PhCreateImage(lpBuffer1, width, height, type, (PgColor_t *)palette1, ncolors, tag);

    if (buffer) {
        setPhImage_tFields(env, buffer, lpBuffer1, &PGLOB(PhImage_tFc));
    }
    if (palette)
        (*env)->ReleaseIntArrayElements(env, palette, palette1, JNI_ABORT);
        
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhCreateImage
 * Signature: (Lorg/eclipse/swt/photon/PhImage_t;SSIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhCreateImage__Lorg_eclipse_swt_internal_photon_PhImage_1t_2SSIIII
  (JNIEnv *env, jobject that, jobject buffer, jshort width, jshort height, jint type, jint palette, jint ncolors, jint tag)
{
	DECL_GLOB(pGlob)
	jint result;
    PhImage_t buffer1, *lpBuffer1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhCreateImage\n");
#endif

    if (buffer) {
        lpBuffer1 = &buffer1;
        cachePhImage_tFids(env, buffer, &PGLOB(PhImage_tFc));
        getPhImage_tFields(env, buffer, lpBuffer1, &PGLOB(PhImage_tFc));
    }

    result = (jint)PhCreateImage(lpBuffer1, width, height, type, (PgColor_t *)palette, ncolors, tag);
    
    if (buffer) {
        setPhImage_tFields(env, buffer, lpBuffer1, &PGLOB(PhImage_tFc));
    }

    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhReleaseImage
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PhReleaseImage
  (JNIEnv *env, jobject that, jint image)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhReleaseImage\n");
#endif
	
	PhReleaseImage((PhImage_t *)image);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PmMemCreateMC
 * Signature: (ILorg/eclipse/swt/photon/PhDim_t;Lorg/eclipse/swt/photon/PhPoint_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PmMemCreateMC
  (JNIEnv *env, jobject that, jint image, jobject dim, jobject translation)
{
	DECL_GLOB(pGlob)
    PhDim_t dim1, *lpDim1=NULL;
    PhPoint_t translation1, *lpTranslation1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PmMemCreateMC\n");
#endif

    if (dim) {
        lpDim1 = &dim1;
        cachePhDim_tFids(env, dim, &PGLOB(PhDim_tFc));
        getPhDim_tFields(env, dim, lpDim1, &PGLOB(PhDim_tFc));
    }
    if (translation) {
        lpTranslation1 = &translation1;
        cachePhPoint_tFids(env, translation, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, translation, lpTranslation1, &PGLOB(PhPoint_tFc));
    }
    return (jint) PmMemCreateMC((PhImage_t *)image, lpDim1, lpTranslation1);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PmMemReleaseMC
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PmMemReleaseMC
  (JNIEnv *env, jobject that, jint mc)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PmMemReleaseMC\n");
#endif
	
	PmMemReleaseMC((PmMemoryContext_t *)mc);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PmMemStart
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PmMemStart
  (JNIEnv *env, jobject that, jint mc)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PmMemStart\n");
#endif
	
	return (jint)PmMemStart((PmMemoryContext_t *)mc);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PmMemStop
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PmMemStop
  (JNIEnv *env, jobject that, jint mc)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PmMemStop\n");
#endif
	
	return (jint)PmMemStop((PmMemoryContext_t *)mc);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PmMemFlush
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PmMemFlush
  (JNIEnv *env, jobject that, jint mc, jint image)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PmMemFlush\n");
#endif
	
	return (jint)PmMemFlush((PmMemoryContext_t *)mc, (PhImage_t *)image);
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtCreateWidgetClass
 * Signature: (III[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtCreateWidgetClass
  (JNIEnv *env, jobject that, jint superclass_ref, jint size, jint n_args, jintArray args)
{
    jint *args1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtCreateWidgetClass\n");
#endif

    if (args)
        args1 = (*env)->GetIntArrayElements(env, args, NULL);

    result = (jint)PtCreateWidgetClass((PtWidgetClassRef_t *)superclass_ref, size, n_args, (PtArg_t *)args1);

    if (args)
        (*env)->ReleaseIntArrayElements(env, args, args1, 0);
	
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtSuperClassDraw
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtSuperClassDraw
  (JNIEnv *env, jobject that, jint wc_ref, jint widget, jint damage)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtSuperClassDraw\n");
#endif
	
	PtSuperClassDraw((PtWidgetClassRef_t *) wc_ref, (PtWidget_t *)widget, (PhTile_t *)damage);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtToggleButton
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtToggleButton
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtToggleButton\n");
#endif
	
	return (jint)PtToggleButton;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtOnOffButton
 * Signature: ()I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtOnOffButton
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtOnOffButton\n");
#endif
	
	return (jint)PtOnOffButton;
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtOnOffButton
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtComboBox
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtComboBox\n");
#endif
	
	return (jint)PtComboBox;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtText
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtText
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtText\n");
#endif
	
	return (jint)PtText;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtMultiText
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtMultiText
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtMultiText\n");
#endif
	
	return (jint)PtMultiText;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtMenu
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtMenu
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtMenu\n");
#endif
	
	return (jint)PtMenu;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtMenuBar
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtMenuBar
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtMenuBar\n");
#endif
	
	return (jint)PtMenuBar;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtMenuButton
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtMenuButton
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtMenuButton\n");
#endif
	
	return (jint)PtMenuButton;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtScrollbar
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtScrollbar
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtScrollbar\n");
#endif
	
	return (jint)PtScrollbar;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtSeparator
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtSeparator
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtSeparator\n");
#endif
	
	return (jint)PtSeparator;
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtListAddItems
 * Signature: (I[III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtListAddItems
  (JNIEnv *env, jobject that, jint widget, jintArray items, jint item_count, jint position)
{
    jint *items1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtListAddItems\n");
#endif

    if (items)
        items1 = (*env)->GetIntArrayElements(env, items, NULL);

    result = (jint)PtListAddItems((PtWidget_t *)widget, (void *)items1, item_count, position);

    if (items)
        (*env)->ReleaseIntArrayElements(env, items, items1, 0);
	
	return result;
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtListUnselectPos
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtListUnselectPos
  (JNIEnv *env, jobject that, jint widget, jint pos)
{

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtListUnselectPos\n");
#endif

	PtListUnselectPos((PtWidget_t *)widget, pos);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (I[SI)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__I_3SI
  (JNIEnv *env, jclass that, jint dest, jintArray src, jint count)
{
    jshort *src1;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__I_3SI\n");
#endif

    /* don't do anything if src pointer is NULL */
    if (src) {
        src1 = (*env)->GetShortArrayElements(env, src, NULL);
        memmove((void *)dest, (void *)src1, count);
        (*env)->ReleaseShortArrayElements(env, src, src1, 0);
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: ([SII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove___3SII
  (JNIEnv *env, jclass that, jintArray dest, jint src, jint count)
{
    jshort *dest1;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove___3SII\n");
#endif

    /* don't do anything if dest pointer is NULL */
    if (dest) {
        dest1 = (*env)->GetShortArrayElements(env, dest, NULL);
        memmove((void *)dest1, (void *)src, count);
        (*env)->ReleaseShortArrayElements(env, dest, dest1, 0);    
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    strlen
 * Signature: (II)V
 */
JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_photon_OS_strlen
  (JNIEnv *env, jobject that, jint string)
{

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "strlen\n");
#endif

	return (jint) strlen((char *) string);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtListDeleteItemPos
 * Signature: (III)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtListDeleteItemPos
  (JNIEnv *env, jobject that, jint widget, jint item_count, jint position)
{

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtListDeleteItemPos\n");
#endif

	PtListDeleteItemPos((PtWidget_t *)widget, item_count, position);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtListDeleteAllItems
 * Signature: (I)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtListDeleteAllItems
  (JNIEnv *env, jobject that, jint widget)
{

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtListDeleteAllItems\n");
#endif

	PtListDeleteAllItems((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtListSelectPos
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtListSelectPos
  (JNIEnv *env, jobject that, jint widget, jint pos)
{

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtListSelectPos\n");
#endif

	PtListSelectPos((PtWidget_t *)widget, pos);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtListReplaceItemPos
 * Signature: (I[III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtListReplaceItemPos
  (JNIEnv *env, jobject that, jint widget, jintArray items, jint item_count, jint position)
{
    jint *items1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtListReplaceItemPos\n");
#endif

    if (items)
        items1 = (*env)->GetIntArrayElements(env, items, NULL);

    result = (jint)PtListReplaceItemPos((PtWidget_t *)widget, (void *)items1, item_count, position);

    if (items)
        (*env)->ReleaseIntArrayElements(env, items, items1, 0);
	
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtListGotoPos
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtListGotoPos
  (JNIEnv *env, jobject that, jint widget, jint pos)
{

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtListGotoPos\n");
#endif

	PtListGotoPos((PtWidget_t *)widget, pos);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtListItemPos
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtListItemPos
  (JNIEnv *env, jobject that, jint widget, jbyteArray item)
{
    char *item1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtListItemPos\n");
#endif

    if (item)
        item1 = (char *)(*env)->GetByteArrayElements(env, item, NULL);

	result = PtListItemPos((PtWidget_t *)widget, item1);

    if (item)
        (*env)->ReleaseByteArrayElements(env, item, (jbyte *)item1, 0);

	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTextModifyText
 * Signature: (IIII[BI)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTextModifyText__IIII_3BI
  (JNIEnv *env, jobject that, jint widget, jint start, jint end, jint insert_pos, jbyteArray text, jint length)
{
    char *text1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTextModifyText\n");
#endif

    if (text)
        text1 = (char *)(*env)->GetByteArrayElements(env, text, NULL);

    result = (jint)PtTextModifyText((PtWidget_t *)widget, start, end, insert_pos, text1, length);

    if (text)
        (*env)->ReleaseByteArrayElements(env, text, (jbyte *)text1, 0);
	
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTextModifyText
 * Signature: (IIIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTextModifyText__IIIIII
  (JNIEnv *env, jobject that, jint widget, jint start, jint end, jint insert_pos, jint text, jint length)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTextModifyText\n");
#endif

	return (jint)PtTextModifyText((PtWidget_t *)widget, start, end, insert_pos, (char *)text, length);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTextGetSelection
 * Signature: (I[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTextGetSelection
  (JNIEnv *env, jobject that, jint widget, jintArray start, jintArray end)
{
    int *start1=NULL, *end1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTextGetSelection\n");
#endif

    if (start)
        start1 = (int *)(*env)->GetIntArrayElements(env, start, NULL);
    if (end)
        end1 = (int *)(*env)->GetIntArrayElements(env, end, NULL);

    result = (jint)PtTextGetSelection((PtWidget_t *)widget, start1, end1);

    if (start)
        (*env)->ReleaseIntArrayElements(env, start, (jint *)start1, 0);
    if (end)
        (*env)->ReleaseIntArrayElements(env, end, (jint *)end1, 0);
	
	return result;
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTextSetSelection
 * Signature: (I[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTextSetSelection
  (JNIEnv *env, jobject that, jint widget, jintArray start, jintArray end)
{
    int *start1=NULL, *end1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTextSetSelection\n");
#endif

    if (start)
        start1 = (int *)(*env)->GetIntArrayElements(env, start, NULL);
    if (end)
        end1 = (int *)(*env)->GetIntArrayElements(env, end, NULL);

    result = (jint)PtTextSetSelection((PtWidget_t *)widget, start1, end1);

    if (start)
        (*env)->ReleaseIntArrayElements(env, start, (jint *)start1, 0);
    if (end)
        (*env)->ReleaseIntArrayElements(env, end, (jint *)end1, 0);
	
	return result;
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtPositionMenu
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtPositionMenu
  (JNIEnv *env, jobject that, jint widget, jobject event)
{
	DECL_GLOB(pGlob)
	PhEvent_t object, *lpObject=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtPositionMenu\n");
#endif
    if (event) {
        lpObject = &object;
        cachePhEvent_tFids(env, event, &PGLOB(PhEvent_tFc));
        getPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
    PtPositionMenu((PtWidget_t *)widget, lpObject);
    if (event) {
        setPhEvent_tFields(env, event, lpObject, &PGLOB(PhEvent_tFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtReParentWidget
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtReParentWidget
  (JNIEnv *env, jobject that, jint widget, jint parent)
{

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtReParentWidget\n");
#endif

	return (jint) PtReParentWidget((PtWidget_t *)widget, (PtWidget_t *)parent);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtForwardWindowEvent
 * Signature: (Lorg/eclipse/swt/internal/photon/PhWindowEvent_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtForwardWindowEvent
  (JNIEnv *env, jobject that, jobject event)
{
	DECL_GLOB(pGlob)
	jint result;
	PhWindowEvent_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtForwardWindowEvent\n");
#endif

    if (event) {
        lpObject = &object;
        cachePhWindowEvent_tFids(env, event, &PGLOB(PhWindowEvent_tFc));
        getPhWindowEvent_tFields(env, event, lpObject, &PGLOB(PhWindowEvent_tFc));
    }
    result = (jint) PtForwardWindowEvent(lpObject);
    if (event) {
        setPhWindowEvent_tFields(env, event, lpObject, &PGLOB(PhWindowEvent_tFc));
    }
    
    return result;
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetOffset
 * Signature: (ILorg/eclipse/swt/internal/photon/PhPoint_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetOffset
  (JNIEnv *env, jobject that, jint widget, jobject offset)
{
	DECL_GLOB(pGlob)
	jint result;
	PhPoint_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtWidgetOffset\n");
#endif

    if (offset) {
        lpObject = &object;
        cachePhPoint_tFids(env, offset, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, offset, lpObject, &PGLOB(PhPoint_tFc));
    }
    result = (jint) PtWidgetOffset((PtWidget_t *)widget, lpObject);
    if (offset) {
        setPhPoint_tFields(env, offset, lpObject, &PGLOB(PhPoint_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtRemoveCallback
 * Signature: (IIII)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtRemoveCallback
  (JNIEnv *env, jobject that, jint widget, jint callback_type, jint callback, jint data)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtRemoveCallback\n");
#endif
	
	PtRemoveCallback((PtWidget_t *)widget, (unsigned long)callback_type, (PtCallbackF_t *)callback, (void *)data);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWindow
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtScrollContainer
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtScrollContainer\n");
#endif
	
	return (jint)PtScrollContainer;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetCanvas
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetCanvas__II
  (JNIEnv *env, jobject that, jint widget, jint canvas_rect)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtWidgetCanvas\n");
#endif

    return (jint) PtWidgetCanvas((PtWidget_t *)widget, (PhRect_t *)canvas_rect);
} 

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetCanvas
 * Signature: (ILorg/eclipse/swt/internal/photon/PhRect_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetCanvas__ILorg_eclipse_swt_internal_photon_PhRect_1t_2
  (JNIEnv *env, jobject that, jint widget, jobject canvas_rect)
{
	DECL_GLOB(pGlob)
	jint result;
	PhRect_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtWidgetCanvas\n");
#endif

    if (canvas_rect) {
        lpObject = &object;
        cachePhRect_tFids(env, canvas_rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, canvas_rect, lpObject, &PGLOB(PhRect_tFc));
    }
    result = (jint) PtWidgetCanvas((PtWidget_t *)widget, lpObject);
    if (canvas_rect) {
        setPhRect_tFields(env, canvas_rect, lpObject, &PGLOB(PhRect_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtLabelWidgetCanvas
 * Signature: (ILorg/eclipse/swt/internal/photon/PhRect_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtLabelWidgetCanvas__ILorg_eclipse_swt_internal_photon_PhRect_1t_2
  (JNIEnv *env, jobject that, jint widget, jobject canvas_rect)
{
	DECL_GLOB(pGlob)
	jint result;
	PhRect_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtLabelWidgetCanvas\n");
#endif

    if (canvas_rect) {
        lpObject = &object;
        cachePhRect_tFids(env, canvas_rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, canvas_rect, lpObject, &PGLOB(PhRect_tFc));
    }
    result = (jint) PtLabelWidgetCanvas((PtWidget_t *)widget, lpObject);
    if (canvas_rect) {
        setPhRect_tFields(env, canvas_rect, lpObject, &PGLOB(PhRect_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetClass
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetClass
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetClass\n");
#endif
	
	return (jint) PtWidgetClass ((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PhPointerEvent_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PhPointerEvent_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhPointerEvent_tFids(env, src, &PGLOB(PhPointerEvent_tFc));
        getPhPointerEvent_tFields(env, src, src1, &PGLOB(PhPointerEvent_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PhPointerEvent_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PhPointerEvent_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhPointerEvent_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePhPointerEvent_tFids(env, dest, &PGLOB(PhPointerEvent_tFc));
        setPhPointerEvent_tFields(env, dest, dest1, &PGLOB(PhPointerEvent_tFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhGetData
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhGetData
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhGetData\n");
#endif
	
	return (jint)PhGetData((PhEvent_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhGetRects
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhGetRects
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhGetRects\n");
#endif
	
	return (jint)PhGetRects((PhEvent_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PhEvent_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PhEvent_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PhEvent_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhEvent_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePhEvent_tFids(env, dest, &PGLOB(PhEvent_tFc));
        setPhEvent_tFields(env, dest, dest1, &PGLOB(PhEvent_tFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PhEvent_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PhEvent_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PhEvent_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PhEvent_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhEvent_tFids(env, src, &PGLOB(PhEvent_tFc));
        getPhEvent_tFields(env, src, src1, &PGLOB(PhEvent_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtExtentWidget
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtExtentWidget
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtExtentWidget\n");
#endif
	
	return (jint)PtExtentWidget((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtExtentWidgetFamily
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtExtentWidgetFamily
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtExtentWidgetFamily\n");
#endif
	
	return (jint)PtExtentWidgetFamily((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtGetAbsPosition
 * Signature: ([S[S)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtGetAbsPosition
  (JNIEnv *env, jobject that, jint widget, jshortArray x, jshortArray y)
{
    jshort *x1=NULL, *y1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtGetAbsPosition\n");
#endif

    if (x)
        x1 = (*env)->GetShortArrayElements(env, x, NULL);
    if (y)
        y1 = (*env)->GetShortArrayElements(env, y, NULL);

	PtGetAbsPosition((PtWidget_t *)widget, x1, y1);

    if (x)
        (*env)->ReleaseShortArrayElements(env, x, x1, 0);
    if (y)
        (*env)->ReleaseShortArrayElements(env, y, y1, 0);
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtSetAreaFromExtent
 * Signature: (ILorg/eclipse/swt/internal/photon/PhRect_t;Lorg/eclipse/swt/internal/photon/PhArea_t;)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtSetAreaFromExtent
  (JNIEnv *env, jobject that, jint widget, jobject extent, jobject area)
{
	DECL_GLOB(pGlob)
	jint result;
	PhRect_t object1, *lpObject1=NULL;
	PhArea_t object2, *lpObject2=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtSetAreaFromExtent\n");
#endif

    if (extent) {
        lpObject1 = &object1;
        cachePhRect_tFids(env, extent, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, extent, lpObject1, &PGLOB(PhRect_tFc));
    }
    if (area) {
        lpObject2 = &object2;
        cachePhArea_tFids(env, area, &PGLOB(PhArea_tFc));
        getPhArea_tFields(env, area, lpObject2, &PGLOB(PhArea_tFc));
    }
    result = (jint) PtSetAreaFromExtent((PtWidget_t *)widget, lpObject1, (PhArea_t *)lpObject2);
    if (extent) {
        setPhRect_tFields(env, extent, lpObject1, &PGLOB(PhRect_tFc));
    }
    if (area) {
        setPhArea_tFields(env, area, lpObject2, &PGLOB(PhArea_tFc));
    }
   
    return result;
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtSetAreaFromWidgetCanvas
 * Signature: (ILorg/eclipse/swt/internal/photon/PhRect_t;Lorg/eclipse/swt/internal/photon/PhArea_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtSetAreaFromWidgetCanvas
  (JNIEnv *env, jobject that, jint widget, jobject extent, jobject area)
{
	DECL_GLOB(pGlob)
	jint result;
	PhRect_t object1, *lpObject1=NULL;
	PhArea_t object2, *lpObject2=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtSetAreaFromWidgetCanvas\n");
#endif

    if (extent) {
        lpObject1 = &object1;
        cachePhRect_tFids(env, extent, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, extent, lpObject1, &PGLOB(PhRect_tFc));
    }
    if (area) {
        lpObject2 = &object2;
        cachePhArea_tFids(env, area, &PGLOB(PhArea_tFc));
        getPhArea_tFields(env, area, lpObject2, &PGLOB(PhArea_tFc));
    }
    result = (jint) PtSetAreaFromWidgetCanvas((PtWidget_t *)widget, lpObject1, lpObject2);
    if (extent) {
        setPhRect_tFields(env, extent, lpObject1, &PGLOB(PhRect_tFc));
    }
    if (area) {
        setPhArea_tFields(env, area, lpObject2, &PGLOB(PhArea_tFc));
    }
   
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PhKeyEvent_t;I)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PhKeyEvent_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PhKeyEvent_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PhKeyEvent_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhKeyEvent_tFids(env, src, &PGLOB(PhKeyEvent_tFc));
        getPhKeyEvent_tFields(env, src, src1, &PGLOB(PhKeyEvent_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PhKeyEvent_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PhKeyEvent_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PhKeyEvent_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhKeyEvent_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePhKeyEvent_tFids(env, dest, &PGLOB(PhKeyEvent_tFc));
        setPhKeyEvent_tFields(env, dest, dest1, &PGLOB(PhKeyEvent_tFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhTo8859_1
 * Signature: (I)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhTo8859_11
  (JNIEnv *env, jobject that, jint event)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhTo8859_1\n");
#endif
	
	return (jint)PhTo8859_1((PhKeyEvent_t *)event);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhKeyToMb_1
 * Signature: ([BI)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhKeyToMb
  (JNIEnv *env, jobject that, jbyteArray buffer, jint event)
{
    jbyte *buffer1=NULL;
 	jint result;
 	
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhKeyToMb\n");
#endif
	
	if (buffer)
        buffer1 = (*env)->GetByteArrayElements(env, buffer, NULL);
        
   	result = PhKeyToMb(buffer1, (PhKeyEvent_t *)event);

    if (buffer)
        (*env)->ReleaseByteArrayElements(env, buffer, buffer1, 0);
	
	return result;
}
*/


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PtScrollbarCallback_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PtScrollbarCallback_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PtScrollbarCallback_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PhPoint_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePtScrollbarCallback_tFids(env, src, &PGLOB(PtScrollbarCallback_tFc));
        getPtScrollbarCallback_tFields(env, src, src1, &PGLOB(PtScrollbarCallback_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PtScrollbarCallback_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PtScrollbarCallback_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PtScrollbarCallback_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhPoint_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePtScrollbarCallback_tFids(env, dest, &PGLOB(PtScrollbarCallback_tFc));
        setPtScrollbarCallback_tFields(env, dest, dest1, &PGLOB(PtScrollbarCallback_tFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtSetParentWidget
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtSetParentWidget
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtSetParentWidget\n");
#endif
	
	return (jint)PtSetParentWidget((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtFrameSize
 * Signature: (II[I[I[I[I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtFrameSize
  (JNIEnv *env, jobject that, jint render, jint border_size, jintArray left_border, jintArray top_border, jintArray right_border, jintArray bottom_border)
{
    int *left_border1=NULL;
    int *top_border1=NULL;
    int *right_border1=NULL;
    int *bottom_border1=NULL;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtFrameSize\n");
#endif

    if (left_border)
        left_border1 = (*env)->GetIntArrayElements(env, left_border, NULL);
    if (top_border)
        top_border1 = (*env)->GetIntArrayElements(env, top_border, NULL);
    if (right_border)
        right_border1 = (*env)->GetIntArrayElements(env, right_border, NULL);
    if (bottom_border)
        bottom_border1 = (*env)->GetIntArrayElements(env, bottom_border, NULL);

    PtFrameSize (render, border_size, left_border1, top_border1, right_border1, bottom_border1);

    if (left_border)
        (*env)->ReleaseIntArrayElements(env, left_border, left_border1, 0);
    if (top_border)
        (*env)->ReleaseIntArrayElements(env, top_border, top_border1, 0);	
    if (right_border)
        (*env)->ReleaseIntArrayElements(env, right_border, right_border1, 0);
    if (bottom_border)
        (*env)->ReleaseIntArrayElements(env, bottom_border, bottom_border1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtCreateAppContext
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtCreateAppContext
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtCreateAppContext\n");
#endif
	
	return (jint)PtCreateAppContext();
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAppProcessEvent ()
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAppProcessEvent
  (JNIEnv *env, jobject that, jint app_context)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAppProcessEvent \n");
#endif
	
	PtAppProcessEvent ((PtAppContext_t)app_context);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAppAddWorkProc
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAppAddWorkProc
  (JNIEnv *env, jobject that, jint app_context, jint work_func, jint data)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAppAddWorkProc\n");
#endif
	
	return (jint)PtAppAddWorkProc ((PtAppContext_t)app_context, (PtWorkProc_t) work_func, (void *)data);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAppRemoveWorkProc()
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAppRemoveWorkProc
  (JNIEnv *env, jobject that, jint app_context, jint WorkProc_id)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAppRemoveWorkProc\n");
#endif
	
	PtAppRemoveWorkProc((PtAppContext_t)app_context, (PtWorkProcId_t *) WorkProc_id);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetExtent
 * Signature: (ILorg/eclipse/swt/internal/photon/PhRect_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetExtent__ILorg_eclipse_swt_internal_photon_PhRect_1t_2
  (JNIEnv *env, jobject that, jint widget, jobject rect) 
{
	DECL_GLOB(pGlob)
	PhRect_t rect1, *lpRect=NULL;
	jint result;
	
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetExtent\n");
#endif
  
    if (rect) {
        lpRect = &rect1;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpRect, &PGLOB(PhRect_tFc));
    }
    result = (jint) PtWidgetExtent((PtWidget_t *) widget, lpRect);
    if (rect) {
        setPhRect_tFields(env, rect, lpRect, &PGLOB(PhRect_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetArea
 * Signature: (ILorg/eclipse/swt/internal/photon/PhArea_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetArea
  (JNIEnv *env, jobject that, jint widget, jobject area) 
{
	DECL_GLOB(pGlob)
	PhArea_t area1, *lpArea=NULL;
	jint result;
	
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetArea\n");
#endif
  
    if (area) {
        lpArea = &area1;
        cachePhArea_tFids(env, area, &PGLOB(PhArea_tFc));
        getPhArea_tFields(env, area, lpArea, &PGLOB(PhArea_tFc));
    }
    result = (jint) PtWidgetArea((PtWidget_t *) widget, lpArea);
    if (area) {
        setPhArea_tFields(env, area, lpArea, &PGLOB(PhArea_tFc));
    }
    
    return result;
}
 
/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhQueryCursor
 * Signature: (ILorg/eclipse/swt/internal/photon/PhCursorInfo_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhQueryCursor
  (JNIEnv *env, jobject that, jshort ig, jobject buf) 
{
	DECL_GLOB(pGlob)
	PhCursorInfo_t buf1, *lpBuf1=NULL;
	jint result;
	
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhQueryCursor\n");
#endif
  
    if (buf) {
        lpBuf1 = &buf1;
        cachePhCursorInfo_tFids(env, buf, &PGLOB(PhCursorInfo_tFc));
        getPhCursorInfo_tFields(env, buf, lpBuf1, &PGLOB(PhCursorInfo_tFc));
    }
    result = (jint) PhQueryCursor(ig, lpBuf1);
    if (buf) {
        setPhCursorInfo_tFields(env, buf, lpBuf1, &PGLOB(PhCursorInfo_tFc));
    }
    
    return result;
}
 
/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhInputGroup
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhInputGroup
  (JNIEnv *env, jobject that, jint event)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhInputGroup\n");
#endif
	
	return (jint) PhInputGroup ((PhEvent_t *) event);
}
 
/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtContainer
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtContainer
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtContainer\n");
#endif
	
	return (jint)PtContainer;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtScrollArea
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtScrollArea
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtScrollArea\n");
#endif
	
	return (jint)PtScrollArea;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAddClassStyle
 * Signature: (II)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAddClassStyle
  (JNIEnv *env, jobject that, jint wclass, jint style)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAddClassStyle\n");
#endif
	
	return (jint)PtAddClassStyle((PtWidgetClassRef_t *)wclass, (PtWidgetClassStyle_t *)style);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtClippedBlit
 * Signature: (IILorg/eclipse/swt/photon/PhPoint_t;I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtClippedBlit
  (JNIEnv *env, jobject that, jint widget, jint src, jobject delta, jint clip)
{
	DECL_GLOB(pGlob)
    PhPoint_t delta1, *lpDelta1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtClippedBlit\n");
#endif
	if (delta) {
        lpDelta1 = &delta1;
        cachePhPoint_tFids(env, delta, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, delta, lpDelta1, &PGLOB(PhPoint_tFc));
    }
	
	return (jint)PtClippedBlit((PtWidget_t *)widget, (PhTile_t *)src, lpDelta1, (PhTile_t *)clip);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/FontDetails;I)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_FontDetails_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    FontDetails object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_FontDetails_2I\n");
#endif

    if (src) {
        src1=&object;
        cacheFontDetailsFids(env, src, &PGLOB(FontDetailsFc));
        getFontDetailsFields(env, src, src1, &PGLOB(FontDetailsFc));
    }
    memmove((void *)dest, (void *)src1, count);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/FontDetails;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_FontDetails_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	FontDetails object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_FontDetails_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cacheFontDetailsFids(env, dest, &PGLOB(FontDetailsFc));
        setFontDetailsFields(env, dest, dest1, &PGLOB(FontDetailsFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PhArea_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PhArea_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PhArea_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PhArea_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhArea_tFids(env, src, &PGLOB(PhArea_tFc));
        getPhArea_tFields(env, src, src1, &PGLOB(PhArea_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PhArea_t;II)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PhArea_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	
	PhArea_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhArea_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePhArea_tFids(env, dest, &PGLOB(PhArea_tFc));
        setPhArea_tFields(env, dest, dest1, &PGLOB(PhArea_tFc));
    }

}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtFileSelection
 * Signature: (ILorg/eclipse/swt/photon/PhPoint_t;[B[B[B[B[BLorg/eclipse/swt/photon/PtFileSelectionInfo_t;I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtFileSelection
  (JNIEnv *env, jobject that, jint widget, jobject pos, jbyteArray title, jbyteArray root_dir, jbyteArray file_spec, jbyteArray btn1, jbyteArray btn2, jbyteArray format, jobject info, jint flags)
{
	DECL_GLOB(pGlob)

	PhPoint_t pos1, *lpPos1=NULL;
	PtFileSelectionInfo_t info1, *lpInfo1=NULL;
	
	char *title1=NULL;
    char *root_dir1=NULL;
    char *file_spec1=NULL;
    char *btn11=NULL;
    char *btn21=NULL;
    char *format1=NULL;
    
    jint result;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtFileSelection\n");
#endif

    if (pos) {
        lpPos1= &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (info) {
        memset (&info1, 0, sizeof (PtFileSelectionInfo_t));
        lpInfo1= &info1;
        cachePtFileSelectionInfo_tFids(env, info, &PGLOB(PtFileSelectionInfo_tFc));
        getPtFileSelectionInfo_tFields(env, info, lpInfo1, &PGLOB(PtFileSelectionInfo_tFc));
    }
    if (title) title1 = (*env)->GetByteArrayElements(env, title, NULL);
    if (root_dir) root_dir1 = (*env)->GetByteArrayElements(env, root_dir, NULL);
    if (file_spec) file_spec1 = (*env)->GetByteArrayElements(env, file_spec, NULL);
    if (btn1) btn11 = (*env)->GetByteArrayElements(env, btn1, NULL);
    if (btn2) btn21 = (*env)->GetByteArrayElements(env, btn2, NULL);
    if (format) format1 = (*env)->GetByteArrayElements(env, format, NULL);

    result = (jint) PtFileSelection ((PtWidget_t *)widget, lpPos1, title1, root_dir1, file_spec1, btn11, btn21, format1, lpInfo1, flags);

    if (pos) {
        setPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }    
    if (info) {
        setPtFileSelectionInfo_tFields(env, info, lpInfo1, &PGLOB(PtFileSelectionInfo_tFc));
    }    
    if (title) (*env)->ReleaseByteArrayElements(env, title, title1, 0);
    if (root_dir) (*env)->ReleaseByteArrayElements(env, root_dir, root_dir1, 0);	
    if (file_spec) (*env)->ReleaseByteArrayElements(env, file_spec, file_spec1, 0);
    if (btn1) (*env)->ReleaseByteArrayElements(env, btn1, btn11, 0);
    if (btn2) (*env)->ReleaseByteArrayElements(env, btn2, btn21, 0);
    if (format) (*env)->ReleaseByteArrayElements(env, format, format1, 0);
      
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetPreferredSize
 * Signature: (ILorg/eclipse/swt/photon/PhDim_t;)Lorg/eclipse/swt/photon/PhDim_t;
 */
JNIEXPORT jobject JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetPreferredSize
  (JNIEnv *env, jobject that, jint widget, jobject dim)
{
	DECL_GLOB(pGlob)
    PhDim_t dim1, *result, *lpDim1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetPreferredSize\n");
#endif

    if (dim) {
        lpDim1 = &dim1;
        cachePhDim_tFids(env, dim, &PGLOB(PhDim_tFc));
        getPhDim_tFields(env, dim, lpDim1, &PGLOB(PhDim_tFc));
    }
    result = PtWidgetPreferredSize((PtWidget_t *)widget, lpDim1);
    if (dim) {
        setPhDim_tFields(env, dim, lpDim1, &PGLOB(PhDim_tFc));
    }
    return result == NULL ? NULL : dim;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAppCreatePulse
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAppCreatePulse
  (JNIEnv *env, jobject that, jint app, jint priority)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAppCreatePulse\n");
#endif
	
	return (jint)PtAppCreatePulse((PtAppContext_t)app, priority);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAppAddInput
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAppAddInput
  (JNIEnv *env, jobject that, jint app_context, jint pid, jint input_func, jint data)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAppAddInput\n");
#endif
	
	return (jint)PtAppAddInput((PtAppContext_t)app_context, (pid_t)pid, (PtInputCallbackProc_t)input_func, (void *)data);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAppRemoveInput
 * Signature: (II)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAppRemoveInput
  (JNIEnv *env, jobject that, jint app_context, jint input_id)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAppRemoveInput\n");
#endif
	
	PtAppRemoveInput((PtAppContext_t)app_context, (PtInputId_t *)input_id);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAppDeletePulse
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAppDeletePulse
  (JNIEnv *env, jobject that, jint app, jint pulse_pid)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAppDeletePulse\n");
#endif
	
	return (jint)PtAppDeletePulse((PtAppContext_t)app, (pid_t)pulse_pid);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAppPulseTrigger
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAppPulseTrigger
  (JNIEnv *env, jobject that, jint app, jint pulse)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAppPulseTrigger\n");
#endif
	
	return (jint)PtAppPulseTrigger((PtAppContext_t)app, (pid_t)pulse);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtContainerFindFocus
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtContainerFindFocus
  (JNIEnv *env, jobject that, jint family_member)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtContainerFindFocus\n");
#endif
	
	return (jint)PtContainerFindFocus((PtWidget_t *)family_member);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtIsFocused
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtIsFocused
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtIsFocused\n");
#endif
	
	return (jint)PtIsFocused((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWindowFocus
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWindowFocus
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWindowFocus\n");
#endif
	
	return (jint)PtWindowFocus((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWindowToFront
  * Signature: (I)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWindowToFront
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWindowToFront\n");
#endif
	
	PtWindowToFront((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWindowToBack
  * Signature: (I)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWindowToBack
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWindowToBack\n");
#endif
	
	PtWindowToBack((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtFindDisjoint
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtFindDisjoint
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtFindDisjoint\n");
#endif
	
	return (jint) PtFindDisjoint((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhClipboardCopyString
 * Signature: (SI)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhClipboardCopyString__SI
  (JNIEnv *env, jobject that, jshort ig, jint string)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhClipboardCopyString\n");
#endif
	
	return (jint) PhClipboardCopyString(ig, (char *)string);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhClipboardCopyString
 * Signature: (S[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhClipboardCopyString__S_3B
  (JNIEnv *env, jclass that, jshort ig, jbyteArray string)
{
    jbyte *string1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhClipboardCopyString\n");
#endif

    if (string)
        string1 = (*env)->GetByteArrayElements(env, string, NULL);

    result = (jint)PhClipboardCopyString(ig, (char *)string1);

    if (string)
        (*env)->ReleaseByteArrayElements(env, string, string1, 0);
	
	return result;

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhClipboardPasteString
 * Signature: (S)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhClipboardPasteString
  (JNIEnv *env, jobject that, jshort ig)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhClipboardPasteString\n");
#endif
	
	return (jint) PhClipboardPasteString(ig);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtBlockAllWindows
 * Signature: (ISI)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtBlockAllWindows
  (JNIEnv *env, jobject that, jint skip, jshort cursor, jint cursor_color)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtBlockAllWindows\n");
#endif
	
	return (jint) PtBlockAllWindows((PtWidget_t *)skip, cursor, (PgColor_t)cursor_color);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtBlockWindow
 * Signature: (ISI)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtBlockWindow
  (JNIEnv *env, jobject that, jint window, jshort cursor, jint cursor_color)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtBlockWindow\n");
#endif
	
	return (jint) PtBlockWindow((PtWidget_t *)window, cursor, (PgColor_t)cursor_color);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtUnblockWindows
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtUnblockWindows
  (JNIEnv *env, jobject that, jint bl)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtUnblockWindows\n");
#endif
	
	PtUnblockWindows((PtBlockedList_t *)bl);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtNextTopLevelWidget
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtNextTopLevelWidget
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtNextTopLevelWidget\n");
#endif
	
	return (jint) PtNextTopLevelWidget((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWindowGetState
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWindowGetState
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWindowGetState\n");
#endif
	
	return (jint) PtWindowGetState((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAddHotkeyHandler
 * Signature: (IIISII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAddHotkeyHandler
  (JNIEnv *env, jobject that, jint widget, jint key_sym_cap, jint key_mods, jshort flags, jint data, jint callback)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAddHotkeyHandler\n");
#endif
	
	PtAddHotkeyHandler((PtWidget_t *)widget, key_sym_cap, key_mods, flags, (void *)data, (PtCallbackF_t *)callback);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtRemoveHotkeyHandler
 * Signature: (IIISII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtRemoveHotkeyHandler
  (JNIEnv *env, jobject that, jint widget, jint key_sym_cap, jint key_mods, jshort flags, jint data, jint callback)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtRemoveHotkeyHandler\n");
#endif
	
	PtRemoveHotkeyHandler((PtWidget_t *)widget, key_sym_cap, key_mods, flags, (void *)data, (PtCallbackF_t *)callback);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PgAlpha_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PgAlpha_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
	/* Some fields of the structure are not represented in Java.
	* Make the are set to zero.
	*/
    PgAlpha_t object = {0}, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PgAlpha_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePgAlpha_tFids(env, src, &PGLOB(PgAlpha_tFc));
        getPgAlpha_tFields(env, src, src1, &PGLOB(PgAlpha_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PgAlpha_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PgAlpha_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	/* Some fields of the structure are not represented in Java.
	* Make the are set to zero.
	*/
	PgAlpha_t object = {0}, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PgAlpha_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePgAlpha_tFids(env, dest, &PGLOB(PgAlpha_tFc));
        setPgAlpha_tFields(env, dest, dest1, &PGLOB(PgAlpha_tFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetAlpha
 * Signature: (IIIBB)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetAlpha
  (JNIEnv *env, jobject that, jint alpha_op, jobject src_alpha_map, jint src_alpha_gradient, jbyte src_global_alpha, jbyte dst_global_alpha)
{
	DECL_GLOB(pGlob)
	PgMap_t map1, *lpMap1=NULL;
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetAlpha\n");
#endif

    if (src_alpha_map) {
        lpMap1 = &map1;
        cachePgMap_tFids(env, src_alpha_map, &PGLOB(PgMap_tFc));
        getPgMap_tFields(env, src_alpha_map, lpMap1, &PGLOB(PgMap_tFc));
    }
	PgSetAlpha(alpha_op, lpMap1, (PgGradient_t *)src_alpha_gradient, (char unsigned)src_global_alpha, (char unsigned)dst_global_alpha);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetAlpha
 * Signature: (IIIBB)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetAlpha
  (JNIEnv *env, jobject that, jint alpha_op, jint src_alpha_map, jint src_alpha_gradient, jbyte src_global_alpha, jbyte dst_global_alpha)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetAlpha\n");
#endif
	
	PgSetAlpha(alpha_op, (PgMap_t *)src_alpha_map, (PgGradient_t *)src_alpha_gradient, (char unsigned)src_global_alpha, (char unsigned)dst_global_alpha);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgAlphaOn
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgAlphaOn
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgAlphaOn\n");
#endif
	
	PgAlphaOn();
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgAlphaOff
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgAlphaOff
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgAlphaOff\n");
#endif
	
	PgAlphaOff();
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PtTextCallback_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PtTextCallback_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PtTextCallback_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PtTextCallback_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePtTextCallback_tFids(env, src, &PGLOB(PtTextCallback_tFc));
        getPtTextCallback_tFields(env, src, src1, &PGLOB(PtTextCallback_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PtTextCallback_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PtTextCallback_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PtTextCallback_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PtTextCallback_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePtTextCallback_tFids(env, dest, &PGLOB(PtTextCallback_tFc));
        setPtTextCallback_tFields(env, dest, dest1, &PGLOB(PtTextCallback_tFc));
    }

}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhInitDrag
 * Signature: (IILorg/eclipse/swt/photon/PhRect_t;Lorg/eclipse/swt/photon/PhRect_t;ILorg/eclipse/swt/photon/PhDim_t;Lorg/eclipse/swt/photon/PhDim_t;Lorg/eclipse/swt/photon/PhDim_t;Lorg/eclipse/swt/photon/PhPoint_t;[I;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhInitDrag
  (JNIEnv *env, jobject that, jint rid, jint flags, jobject rect, jobject boundary, jint input_group, jobject min, jobject max, jobject step, jobject ptrpos, jshortArray cursor)
{
	DECL_GLOB(pGlob)
	PhRect_t rect1, rect2, *lpRect1=NULL, *lpRect2=NULL;
	PhDim_t dim1, dim2, dim3, *lpDim1=NULL, *lpDim2=NULL, *lpDim3=NULL;
    PhPoint_t point1, *lpPoint1=NULL;
    jshort *cursor1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhInitDrag\n");
#endif

    if (rect) {
        lpRect1 = &rect1;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpRect1, &PGLOB(PhRect_tFc));
    }
    if (boundary) {
        lpRect2 = &rect2;
        cachePhRect_tFids(env, boundary, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, boundary, lpRect2, &PGLOB(PhRect_tFc));
    }
    if (min) {
        lpDim1 = &dim1;
        cachePhDim_tFids(env, min, &PGLOB(PhDim_tFc));
        getPhDim_tFields(env, min, lpDim1, &PGLOB(PhDim_tFc));
    }
    if (max) {
        lpDim2 = &dim2;
        cachePhDim_tFids(env, max, &PGLOB(PhDim_tFc));
        getPhDim_tFields(env, max, lpDim2, &PGLOB(PhDim_tFc));
    }
    if (step) {
        lpDim3 = &dim3;
        cachePhDim_tFids(env, step, &PGLOB(PhDim_tFc));
        getPhDim_tFields(env, step, lpDim3, &PGLOB(PhDim_tFc));
    }    
    if (ptrpos) {
        lpPoint1 = &point1;
        cachePhPoint_tFids(env, ptrpos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, ptrpos, lpPoint1, &PGLOB(PhPoint_tFc));
    }
    if (cursor)
        cursor1 = (*env)->GetShortArrayElements(env, cursor, NULL);
    
    result = (jint) PhInitDrag(rid, flags, lpRect1, lpRect2, input_group, lpDim1, lpDim2, lpDim3, lpPoint1, (PhCursorDescription_t *)cursor1);
    
    if (cursor)
        (*env)->ReleaseShortArrayElements(env, cursor, cursor1, 0);
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtProgress
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtProgress
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtProgress\n");
#endif
	
	return (jint)PtProgress;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtPanelGroup
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtPanelGroup
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtPanelGroup\n");
#endif
	
	return (jint)PtPanelGroup;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhAreaToRect
 * Signature: (Lorg/eclipse/swt/internal/photon/PhRect_t;Lorg/eclipse/swt/internal/photon/PhArea_t;)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PhAreaToRect
  (JNIEnv *env, jobject that, jobject area, jobject rect)
{
	DECL_GLOB(pGlob)
	PhArea_t object1, *lpObject1=NULL;
	PhRect_t object2, *lpObject2=NULL;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PhAreaToRect\n");
#endif

    if (area) {
        lpObject1 = &object1;
        cachePhArea_tFids(env, area, &PGLOB(PhArea_tFc));
        getPhArea_tFields(env, area, lpObject1, &PGLOB(PhArea_tFc));
    }
    if (rect) {
        lpObject2 = &object2;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpObject2, &PGLOB(PhRect_tFc));
    }
	PhAreaToRect(lpObject1, lpObject2);
    if (area) {
        setPhArea_tFields(env, area, lpObject1, &PGLOB(PhArea_tFc));
    }
    if (rect) {
        setPhRect_tFields(env, rect, lpObject2, &PGLOB(PhRect_tFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtCalcCanvas
 * Signature: (ILorg/eclipse/swt/internal/photon/PhRect_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtCalcCanvas__ILorg_eclipse_swt_internal_photon_PhRect_1t_2
  (JNIEnv *env, jobject that, jint widget, jobject canvas_rect)
{
	DECL_GLOB(pGlob)
	jint result;
	PhRect_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtCalcCanvas\n");
#endif

    if (canvas_rect) {
        lpObject = &object;
        cachePhRect_tFids(env, canvas_rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, canvas_rect, lpObject, &PGLOB(PhRect_tFc));
    }
    result = (jint) PtCalcCanvas((PtWidget_t *)widget, lpObject);
    if (canvas_rect) {
        setPhRect_tFields(env, canvas_rect, lpObject, &PGLOB(PhRect_tFc));
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtValidParent
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtValidParent
  (JNIEnv *env, jobject that, jint widget, jint class_ref)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtValidParent\n");
#endif
	
	return (jint)PtValidParent((PtWidget_t *)widget, (PtWidgetClassRef_t *) class_ref);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtCalcBorder
 * Signature: (ILorg/eclipse/swt/internal/photon/PhRect_t;)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtCalcBorder__ILorg_eclipse_swt_internal_photon_PhRect_1t_2
  (JNIEnv *env, jobject that, jint widget, jobject rect)
{
	DECL_GLOB(pGlob)
	PhRect_t object, *lpObject=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtCalcBorder\n");
#endif

    if (rect) {
        lpObject = &object;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpObject, &PGLOB(PhRect_tFc));
    }
    PtCalcBorder((PtWidget_t *)widget, lpObject);
    if (rect) {
        setPhRect_tFields(env, rect, lpObject, &PGLOB(PhRect_tFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtPane
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtPane
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtPane\n");
#endif
	
	return (jint)PtPane;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTree
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTree
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTree\n");
#endif
	
	return (jint)PtTree;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTreeAllocItem
 * Signature: (I[BSS)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTreeAllocItem
  (JNIEnv *env, jobject that, jint widget, jbyteArray str, jshort set_img, jshort unset_img)
{
	PtTreeItem_t *result;
    jbyte *str1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTreeAllocItem\n");
#endif
	if (str)
        str1 = (*env)->GetByteArrayElements(env, str, NULL);
	
	result = PtTreeAllocItem((PtWidget_t *)widget, str1, set_img, unset_img);

    if (str)
        (*env)->ReleaseByteArrayElements(env, str, str1, JNI_ABORT);
        
    return (jint) result;
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTreeModifyItem
 * Signature: (II[BSS)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTreeModifyItem
  (JNIEnv *env, jobject that, jint widget, jint item, jbyteArray str, jshort set_img, jshort unset_img)
{
	PtTreeItem_t *result;
    jbyte *str1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTreeModifyItem\n");
#endif
	if (str)
        str1 = (*env)->GetByteArrayElements(env, str, NULL);
	
	result = PtTreeModifyItem((PtWidget_t *)widget, (PtTreeItem_t *) item, str1, set_img, unset_img);

    if (str)
        (*env)->ReleaseByteArrayElements(env, str, str1, JNI_ABORT);
        
    return (jint) result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTreeModifyItemString
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTreeModifyItemString
  (JNIEnv *env, jobject that, jint widget, jint item, jbyteArray str)
{
	PtTreeItem_t *result;
    jbyte *str1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTreeModifyItemString\n");
#endif
	if (str)
        str1 = (*env)->GetByteArrayElements(env, str, NULL);
	
	result = PtTreeModifyItemString((PtWidget_t *)widget, (PtTreeItem_t *) item, str1);

    if (str)
        (*env)->ReleaseByteArrayElements(env, str, str1, JNI_ABORT);
        
    return (jint) result;
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTreeAddFirst
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTreeAddFirst
  (JNIEnv *env, jobject that, jint widget, jint item, jint parent)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTreeAddFirst\n");
#endif
	
	return (jint)PtTreeAddFirst((PtWidget_t *)widget, (PtTreeItem_t *) item, (PtTreeItem_t *) parent);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTreeAddAfter
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTreeAddAfter
  (JNIEnv *env, jobject that, jint widget, jint item, jint brother)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTreeAddAfter\n");
#endif
	
	return (jint)PtTreeAddAfter((PtWidget_t *)widget, (PtTreeItem_t *) item, (PtTreeItem_t *) brother);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTreeRemoveItem
 * Signature: (II)
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTreeRemoveItem
  (JNIEnv *env, jobject that, jint widget, jint item)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTreeRemoveItem\n");
#endif
	
	PtTreeRemoveItem((PtWidget_t *)widget, (PtTreeItem_t *) item);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTreeRootItem
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTreeRootItem
  (JNIEnv *env, jobject that, jint tree)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTreeRootItem\n");
#endif
	
	return (jint) PtTreeRootItem((PtWidget_t *)tree);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PtTreeItem_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PtTreeItem_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PtTreeItem_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PtTreeItem_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePtTreeItem_tFids(env, src, &PGLOB(PtTreeItem_tFc));
        getPtTreeItem_tFields(env, src, src1, &PGLOB(PtTreeItem_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PtTreeItem_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PtTreeItem_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PtTreeItem_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PtTreeItem_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePtTreeItem_tFids(env, dest, &PGLOB(PtTreeItem_tFc));
        setPtTreeItem_tFields(env, dest, dest1, &PGLOB(PtTreeItem_tFc));
    }

}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTreeClearSelection
 * Signature: (I)
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTreeClearSelection
  (JNIEnv *env, jobject that, jint tree)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTreeClearSelection\n");
#endif
	
	PtTreeClearSelection((PtWidget_t *)tree);
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTreeSelect
 * Signature: (II)
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTreeSelect
  (JNIEnv *env, jobject that, jint widget, jint item)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTreeSelect\n");
#endif
	
	PtTreeSelect((PtWidget_t *)widget, (PtTreeItem_t *) item);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTreeExpand
 * Signature: (III)I
 */
JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTreeExpand
  (JNIEnv *env, jobject that, jint widget, jint item, jint event)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTreeExpand\n");
#endif
	
	return PtTreeExpand((PtWidget_t *)widget, (PtTreeItem_t *) item, (PhEvent_t *) event);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTreeCollapse
 * Signature: (III)I
 */
JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTreeCollapse
  (JNIEnv *env, jobject that, jint widget, jint item, jint event)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTreeCollapse\n");
#endif
	
	return PtTreeCollapse((PtWidget_t *)widget, (PtTreeItem_t *) item, (PhEvent_t *) event);
}


/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTreeFreeAllItems 
 * Signature: (I)
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTreeFreeAllItems 
  (JNIEnv *env, jobject that, jint tree)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTreeFreeAllItems \n");
#endif
	
	PtTreeFreeAllItems ((PtWidget_t *)tree);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PgMap_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PgMap_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PgMap_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PgMap_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePgMap_tFids(env, src, &PGLOB(PgMap_tFc));
        getPgMap_tFields(env, src, src1, &PGLOB(PgMap_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PgMap_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PgMap_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PgMap_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PgMap_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePgMap_tFids(env, dest, &PGLOB(PgMap_tFc));
        setPgMap_tFields(env, dest, dest1, &PGLOB(PgMap_tFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtDamageWidget
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtDamageWidget
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtDamageWidget\n");
#endif
	
	return (jint)PtDamageWidget((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtBlit
 * Signature: (ILorg/eclipse/swt/photon/PhRect_t;ILorg/eclipse/swt/photon/PhPoint_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtBlit
  (JNIEnv *env, jobject that, jint widget, jobject rect, jobject offset)
{
	DECL_GLOB(pGlob)
    PhRect_t rect1, *lpRect1=NULL;
    PhPoint_t offset1, *lpOffset1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtBlit\n");
#endif

    if (rect) {
        lpRect1 = &rect1;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpRect1, &PGLOB(PhRect_tFc));
    }
    if (offset) {
        lpOffset1 = &offset1;
        cachePhPoint_tFids(env, offset, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, offset, lpOffset1, &PGLOB(PhPoint_tFc));
    }
    return (jint) PtBlit((PtWidget_t *)widget, lpRect1, lpOffset1);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtContainerHold
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtContainerHold
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtContainerHold\n");
#endif
	
	return (jint)PtContainerHold((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtContainerRelease
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtContainerRelease
  (JNIEnv *env, jobject that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtContainerRelease\n");
#endif
	
	return (jint)PtContainerRelease((PtWidget_t *)widget);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtFontSelection
 * Signature: (ILorg/eclipse/swt/photon/PhPoint_t;[B[BII[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtFontSelection
  (JNIEnv *env, jobject that, jint parent, jobject pos, jbyteArray title, jbyteArray font, jint symbol, jint flags, jbyteArray sample)
{
	DECL_GLOB(pGlob)

	PhPoint_t pos1, *lpPos1=NULL;
	
	char *title1=NULL;
    char *font1=NULL;
    char *sample1=NULL;
    
    jint result;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtFontSelection\n");
#endif

    if (pos) {
        lpPos1= &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (title) title1 = (*env)->GetByteArrayElements(env, title, NULL);
    if (font) font1 = (*env)->GetByteArrayElements(env, font, NULL);
    if (sample) sample1 = (*env)->GetByteArrayElements(env, sample, NULL);

    result = (jint) PtFontSelection ((PtWidget_t *)parent, lpPos1, title1, font1, symbol, flags, sample1);

    if (pos) {
        setPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }    
    if (title) (*env)->ReleaseByteArrayElements(env, title, title1, 0);
    if (font) (*env)->ReleaseByteArrayElements(env, font, font1, 0);	
    if (sample) (*env)->ReleaseByteArrayElements(env, sample, sample1, 0);
      
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PfGenerateFontName
 * Signature: ([BII[B)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_eclipse_swt_internal_photon_OS_PfGenerateFontName
  (JNIEnv *env, jobject that, jbyteArray pkucDescription, jint kuiFlags, jint kuiSize, jbyteArray pucBuff)
{
	char *pkucDescription1=NULL;
    char *pucBuff1=NULL;
    
    uchar_t *result;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PfGenerateFontName\n");
#endif

    if (pkucDescription) pkucDescription1 = (*env)->GetByteArrayElements(env, pkucDescription, NULL);
    if (pucBuff) pucBuff1 = (*env)->GetByteArrayElements(env, pucBuff, NULL);

    result = PfGenerateFontName(pkucDescription1, kuiFlags, kuiSize, pucBuff1);

    if (pkucDescription) (*env)->ReleaseByteArrayElements(env, pkucDescription, pkucDescription1, 0);
    if (pucBuff) (*env)->ReleaseByteArrayElements(env, pucBuff, pucBuff1, 0);	
      
	return result == NULL ? NULL : pucBuff;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PfFindFont
 * Signature: ([BII)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PfFindFont
  (JNIEnv *env, jobject that, jbyteArray pkucDescription, jint kulFlags, jint kulSize)
{
	char *pkucDescription1=NULL;
    
    jint result;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PfFindFont\n");
#endif

    if (pkucDescription) pkucDescription1 = (*env)->GetByteArrayElements(env, pkucDescription, NULL);

    result = (jint)PfFindFont(pkucDescription1, kulFlags, kulSize);

    if (pkucDescription) (*env)->ReleaseByteArrayElements(env, pkucDescription, pkucDescription1, 0);
      
	return result;
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PfFreeFont
 * Signature: (I)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PfFreeFont
  (JNIEnv *env, jobject that, jint ptsID)
{
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PfFreeFont\n");
#endif

    return PfFreeFont((FontID *)ptsID);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PfFontDescription
 * Signature: (I)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PfFontDescription
  (JNIEnv *env, jobject that, jint ptsID)
{
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PfFontDescription\n");
#endif

    return (jint)PfFontDescription((FontID *)ptsID);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PfFontFlags
 * Signature: (I)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PfFontFlags
  (JNIEnv *env, jobject that, jint ptsID)
{
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PfFontFlags\n");
#endif

    return PfFontFlags((FontID *)ptsID);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PfFontSize
 * Signature: (I)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PfFontSize
  (JNIEnv *env, jobject that, jint ptsID)
{
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PfFontSize\n");
#endif

    return PfFontSize((FontID *)ptsID);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PfConvertFontID
 * Signature: (I)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PfConvertFontID
  (JNIEnv *env, jobject that, jint ptsID)
{
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PfConvertFontID\n");
#endif

    return (jint)PfConvertFontID((FontID *)ptsID);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtToolbar
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtToolbar
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtToolbar\n");
#endif
	
	return (jint)PtToolbar;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtColorSelGroup
 * Signature: ()I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtColorSelGroup
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtColorSelGroup\n");
#endif
	
	return (jint)PtColorSelGroup;
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtColorPatch
 * Signature: ()I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtColorPatch
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtColorPatch\n");
#endif
	
	return (jint)PtColorPatch;
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtColorPalette
 * Signature: ()I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtColorPalette
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtColorPalette\n");
#endif
	
	return (jint)PtColorPalette;
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtGroup
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtGroup
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtGroup\n");
#endif
	
	return (jint)PtGroup;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    Pg_CM_RGB
 * Signature: ()I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_Pg_1CM_1RGB
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "Pg_CM_RGB\n");
#endif
	
	return (jint)Pg_CM_RGB;
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    Pg_CM_HSB
 * Signature: ()I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_Pg_1CM_1HSB
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "Pg_CM_HSB\n");
#endif
	
	return (jint)Pg_CM_HSB;
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    Pg_CM_HLS
 * Signature: ()I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_Pg_1CM_1HLS
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "Pg_CM_HLS\n");
#endif
	
	return (jint)Pg_CM_HLS;
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhRectUnion
 * Signature: (Lorg/eclipse/swt/internal/photon/PhRect_t;Lorg/eclipse/swt/internal/photon/PhRect_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhRectUnion__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhRect_1t_2
  (JNIEnv * env, jobject that, jobject rect1, jobject rect2)
{
	DECL_GLOB(pGlob)
	PhRect_t rect11, *lpRect11=NULL;
	PhRect_t rect21, *lpRect21=NULL;
	jint result;
	
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhRectUnion__Lorg_eclipse_swt_internal_photon_PhRect_1t_2Lorg_eclipse_swt_internal_photon_PhRect_1t_2\n");
#endif

    if (rect1) {
        lpRect11 = &rect11;
        cachePhRect_tFids(env, rect1, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect1, lpRect11, &PGLOB(PhRect_tFc));
    }
    if (rect2) {
        lpRect21 = &rect21;
        cachePhRect_tFids(env, rect2, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect2, lpRect21, &PGLOB(PhRect_tFc));
    }
	result = PhRectUnion(lpRect11, lpRect21);
    if (rect1) {
        setPhRect_tFields(env, rect1, lpRect11, &PGLOB(PhRect_tFc));
    }
    if (rect2) {
        setPhRect_tFields(env, rect2, lpRect21, &PGLOB(PhRect_tFc));
    }
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtRegion
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtRegion
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtRegion\n");
#endif
	
	return (jint)PtRegion;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAddFilterCallback
 * Signature: (IIII)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAddFilterCallback
  (JNIEnv *env, jobject that, jint widget, jint callback_type, jint callback, jint data)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAddFilterCallback\n");
#endif
	
	PtAddFilterCallback((PtWidget_t *)widget, (unsigned long)callback_type, (PtCallbackF_t *)callback, (void *)data);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtTimer
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtTimer
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtTimer\n");
#endif
	
	return (jint)PtTimer;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtHit
 * Signature: (IILorg/eclipse/swt/internal/photon/PhRect_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtHit
  (JNIEnv * env, jobject that, jint container, jint n, jobject rect)
{
	DECL_GLOB(pGlob)
	PhRect_t rect1, *lpRect1=NULL;
	jint result;
	
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtHit\n");
#endif

    if (rect) {
        lpRect1 = &rect1;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpRect1, &PGLOB(PhRect_tFc));
    }
	result = (jint) PtHit ((PtWidget_t *) container, n, lpRect1);
    if (rect) {
        setPhRect_tFields(env, rect, lpRect1, &PGLOB(PhRect_tFc));
    }
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtColorSelect
 * Signature: (I[BLorg/eclipse/swt/internal/photon/PtColorSelectInfo_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtColorSelect
  (JNIEnv *env, jobject that, jint parent, jbyteArray title, jobject info)
{
	DECL_GLOB(pGlob)
	PtColorSelectInfo_t info1, *lpInfo1=NULL;
	jbyte *title1=NULL;
	jint result;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtColorSelect\n");
#endif

	if (title) title1 = (*env)->GetByteArrayElements(env, title, NULL);
    if (info) {
        lpInfo1 = &info1;
        cachePtColorSelectInfo_tFids(env, info, &PGLOB(PtColorSelectInfo_tFc));
        getPtColorSelectInfo_tFields(env, info, lpInfo1, &PGLOB(PtColorSelectInfo_tFc));
    }
    result = PtColorSelect((PtWidget_t *)parent, (char *)title1, lpInfo1);
    if (title) (*env)->ReleaseByteArrayElements(env, title, title1, 0);
    if (info) {
        setPtColorSelectInfo_tFields(env, info, lpInfo1, &PGLOB(PtColorSelectInfo_tFc));
    }
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgDrawArrow
 * Signature: (Lorg/eclipse/swt/photon/PhRect_t;SII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgDrawArrow
  (JNIEnv *env, jobject that, jobject rect, jshort unknown, jint color, jint flags)
{
	DECL_GLOB(pGlob)
    PhRect_t rect1, *lpRect1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgDrawArrow\n");
#endif

    if (rect) {
        lpRect1 = &rect1;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpRect1, &PGLOB(PhRect_tFc));
    }
    PgDrawArrow(lpRect1, unknown, (PgColor_t)color, flags);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtWidgetIsClassMember
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtWidgetIsClassMember
  (JNIEnv *env, jobject that, jint widget, jint clazz)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtWidgetIsClassMember\n");
#endif
	
	return (jint)PtWidgetIsClassMember((PtWidget_t *)widget, (PtWidgetClassRef_t *)clazz);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtBeep
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtBeep
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtBeep\n");
#endif
	
	return (jint)PtBeep();
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtAlert
 * Signature: (ILorg/eclipse/swt/photon/PhPoint_t;[BI[B[BII[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtAlert
  (JNIEnv *env, jobject that, jint parent, jobject pos, jbyteArray title, int image, jbyteArray message, 
  	jbyteArray msgFont, int btnCount, jintArray buttons, jintArray btnFonts,
  	int defBtn, int escBtn, int flags)
{
	DECL_GLOB(pGlob)

	PhPoint_t pos1, *lpPos1=NULL;
	
	char *title1=NULL;
    char *message1=NULL;
    char *msgFont1=NULL;
    jint *buttons1=NULL;
    jint *btnFonts1=NULL;
    
    jint result;
    
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtAlert\n");
#endif

    if (pos) {
        lpPos1= &pos1;
        cachePhPoint_tFids(env, pos, &PGLOB(PhPoint_tFc));
        getPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }
    if (title) title1 = (*env)->GetByteArrayElements(env, title, NULL);
    if (message) message1 = (*env)->GetByteArrayElements(env, message, NULL);
    if (msgFont) msgFont1 = (*env)->GetByteArrayElements(env, msgFont, NULL);
    if (buttons) buttons1 = (*env)->GetIntArrayElements(env, buttons, NULL);
    if (btnFonts) btnFonts1 = (*env)->GetIntArrayElements(env, btnFonts, NULL);

    result = (jint) PtAlert ((PtWidget_t *)parent, lpPos1, title1, (PhImage_t *)image, message1, msgFont1, btnCount, (char const**)buttons1, (char const**)btnFonts1, defBtn, escBtn, flags);

    if (pos) {
        setPhPoint_tFields(env, pos, lpPos1, &PGLOB(PhPoint_tFc));
    }    
    if (title) (*env)->ReleaseByteArrayElements(env, title, title1, 0);
    if (msgFont) (*env)->ReleaseByteArrayElements(env, msgFont, msgFont1, 0);	
    if (message) (*env)->ReleaseByteArrayElements(env, message, message1, 0);
    if (buttons) (*env)->ReleaseIntArrayElements(env, buttons, buttons1, 0);
    if (btnFonts) (*env)->ReleaseIntArrayElements(env, btnFonts, btnFonts1, 0);
      
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtSlider
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtSlider
  (JNIEnv *env, jobject that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtSlider\n");
#endif
	
	return (jint)PtSlider;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PiDuplicateImage
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PiDuplicateImage
  (JNIEnv *env, jobject that, jint image, jint flags)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PiDuplicateImage\n");
#endif
	
	return (jint)PiDuplicateImage ((PhImage_t *)image, flags);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhMakeGhostBitmap
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhMakeGhostBitmap
  (JNIEnv *env, jobject that, jint image)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhMakeGhostBitmap\n");
#endif

    return (jint) PhMakeGhostBitmap((PhImage_t *)image);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgReadScreen
 * Signature: (Lorg/eclipse/swt/photon/PhRect_t;I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgReadScreen
  (JNIEnv *env, jobject that, jobject rect, jint buffer)
{
	DECL_GLOB(pGlob)
    PhRect_t rect1, *lpRect1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgReadScreen\n");
#endif

    if (rect) {
        lpRect1 = &rect1;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpRect1, &PGLOB(PhRect_tFc));
    }
    return (jint) PgReadScreen(lpRect1, (void *) buffer);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgReadScreenSize
 * Signature: (Lorg/eclipse/swt/photon/PhRect_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgReadScreenSize
  (JNIEnv *env, jobject that, jobject rect)
{
	DECL_GLOB(pGlob)
    PhRect_t rect1, *lpRect1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgReadScreenSize\n");
#endif

    if (rect) {
        lpRect1 = &rect1;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpRect1, &PGLOB(PhRect_tFc));
    }
    return (jint) PgReadScreenSize(lpRect1);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgShmemDestroy
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgShmemDestroy
  (JNIEnv *env, jobject that, jint addr)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgShmemDestroy\n");
#endif

    return (jint) PgShmemDestroy((void *)addr);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgShmemDetach
 * Signature: (I)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgShmemDetach
  (JNIEnv *env, jobject that, jint addr)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgShmemDetach\n");
#endif

    return (jint) PgShmemDetach((void *)addr);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgShmemCreate
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgShmemCreate
  (JNIEnv *env, jobject that, jint size, jbyteArray name)
{
	jint result;
	char *name1 = NULL;
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgShmemCreate\n");
#endif

	if (name) name1 = (*env)->GetByteArrayElements(env, name, NULL);
    result = (jint) PgShmemCreate(size, name1);
    if (name) (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgShmemCleanup
 * Signature: ()V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgShmemCleanup
  (JNIEnv *env, jobject that, jint addr)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgShmemCleanup\n");
#endif

    PgShmemCleanup();
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhRegionQuery
 * Signature: (ILorg/eclipse/swt/photon/PhRegion_t;Lorg/eclipse/swt/photon/PhRect_t;II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhRegionQuery
  (JNIEnv *env, jobject that, jint rid, jobject region, jobject rect, jint data, jint data_len)
{
	DECL_GLOB(pGlob)
    PhRegion_t region1, *lpRegion1=NULL;
    PhRect_t rect1, *lpRect1=NULL;
    jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhRegionQuery\n");
#endif

    if (region) {
        lpRegion1 = &region1;
        cachePhRegion_tFids(env, region, &PGLOB(PhRegion_tFc));
        getPhRegion_tFields(env, region, lpRegion1, &PGLOB(PhRegion_tFc));
    }
    if (rect) {
        lpRect1 = &rect1;
        cachePhRect_tFids(env, rect, &PGLOB(PhRect_tFc));
        getPhRect_tFields(env, rect, lpRect1, &PGLOB(PhRect_tFc));
    }
    result = (jint) PhRegionQuery((PhRid_t)rid, lpRegion1, lpRect1, (void *) data, data_len);   
    if (region) {
        setPhRegion_tFields(env, region, lpRegion1, &PGLOB(PhRegion_tFc));
    }    
    if (rect) {
        setPhRect_tFields(env, rect, lpRect1, &PGLOB(PhRect_tFc));
    }      
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PiGetPixelRGB
 * Signature: (II)I
 */
/*
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PiGetPixelRGB
  (JNIEnv *env, jobject that, jint image, jint x, jint y, jintArray value)
{
	jint result;
	jint *value1 = NULL;
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PiGetPixelRGB\n");
#endif
	
    if (value) value1 = (*env)->GetIntArrayElements(env, value, NULL);
    
	result = (jint)PiGetPixelRGB ((PhImage_t *)image, x, y, (PgColor_t *)value1);
	
    if (value) (*env)->ReleaseIntArrayElements(env, value, value1, 0);

	return result;
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PtContainerCallback_t;I)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PtContainerCallback_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PtContainerCallback_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PtContainerCallback_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePtContainerCallback_tFids(env, src, &PGLOB(PtContainerCallback_tFc));
        getPtContainerCallback_tFields(env, src, src1, &PGLOB(PtContainerCallback_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PtContainerCallback_t;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PtContainerCallback_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PtContainerCallback_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PtContainerCallback_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePtContainerCallback_tFids(env, dest, &PGLOB(PtContainerCallback_tFc));
        setPtContainerCallback_tFields(env, dest, dest1, &PGLOB(PtContainerCallback_tFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtSendEventToWidget
 * Signature: (ILorg/eclipse/swt/internal/photon/PhEvent_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtSendEventToWidget
  (JNIEnv *env, jobject that, jint widget, jint event)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PtSendEventToWidget\n");
#endif

    return (jint) PtSendEventToWidget((PtWidget_t *)widget, (void *)event);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PhCursorDef_t;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PhCursorDef_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PhCursorDef_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PhCursorDef_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhCursorDef_tFids(env, src, &PGLOB(PhCursorDef_tFc));
        getPhCursorDef_tFields(env, src, src1, &PGLOB(PhCursorDef_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PhCursorDef_t;II)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PhCursorDef_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PhCursorDef_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhCursorDef_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePhCursorDef_tFids(env, dest, &PGLOB(PhCursorDef_tFc));
        setPhCursorDef_tFields(env, dest, dest1, &PGLOB(PhCursorDef_tFc));
    }
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgSetFillTransPat
 * Signature: ([B)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PgSetFillTransPat
  (JNIEnv *env, jobject that, jbyteArray pat)
{
    char *pat1=NULL;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgSetFillTransPat\n");
#endif

    if (pat)
        pat1 = (char *)(*env)->GetByteArrayElements(env, pat, NULL);

    PgSetFillTransPat(pat1);

    if (pat)
        (*env)->ReleaseByteArrayElements(env, pat, (jbyte *)pat1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PtInflateBalloon
 * Signature: (III[B[BII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PtInflateBalloon
  (JNIEnv *env, jobject that, jint win, jint me, jint position, jbyteArray str, jbyteArray font, int fill, int text_color)
{
    jbyte *font1=NULL;
    jbyte *str1=NULL;
    int result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PtInflateBalloon\n");
#endif

    if (font)
        font1 = (*env)->GetByteArrayElements(env, font, NULL);
    if (str)
        str1 = (*env)->GetByteArrayElements(env, str, NULL);

    result = (jint) PtInflateBalloon((PtWidget_t *)win, (PtWidget_t *)me, position, str1, font1, fill, text_color);

    if (font)
        (*env)->ReleaseByteArrayElements(env, font, font1, JNI_ABORT);
    if (str)
        (*env)->ReleaseByteArrayElements(env, str, str1, JNI_ABORT);
	
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PgDisplaySettings_t;I)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PgDisplaySettings_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PgDisplaySettings_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PgDisplaySettings_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePgDisplaySettings_tFids(env, src, &PGLOB(PgDisplaySettings_tFc));
        getPgDisplaySettings_tFields(env, src, src1, &PGLOB(PgDisplaySettings_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PgDisplaySettings_t;II)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PgDisplaySettings_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PgDisplaySettings_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PgDisplaySettings_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePgDisplaySettings_tFids(env, dest, &PGLOB(PgDisplaySettings_tFc));
        setPgDisplaySettings_tFields(env, dest, dest1, &PGLOB(PgDisplaySettings_tFc));
    }
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PgVideoModeInfo_t;I)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__ILorg_eclipse_swt_internal_photon_PgVideoModeInfo_1t_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	
    PgVideoModeInfo_t object, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_photon_PgVideoModeInfo_1t_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePgVideoModeInfo_tFids(env, src, &PGLOB(PgVideoModeInfo_tFc));
        getPgVideoModeInfo_tFields(env, src, src1, &PGLOB(PgVideoModeInfo_tFc));
    }
    memmove((void *)dest, (void *)src1, count);
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PgVideoModeInfo_t;II)V
 */
/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PgVideoModeInfo_1t_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PgVideoModeInfo_t object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PgVideoModeInfo_1t_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePgVideoModeInfo_tFids(env, dest, &PGLOB(PgVideoModeInfo_tFc));
        setPgVideoModeInfo_tFields(env, dest, dest1, &PGLOB(PgVideoModeInfo_tFc));
    }
}
*/

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgGetVideoMode
 * Signature: (Lorg/eclipse/swt/internal/photon/PgDisplaySettings_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgGetVideoMode
  (JNIEnv *env, jobject that, jobject settings)
{
	DECL_GLOB(pGlob)
    PgDisplaySettings_t settings1, *lpSettings1=NULL;
	jint result;
	
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgGetVideoMode\n");
#endif

	if (settings) {
        lpSettings1 = &settings1;
        cachePgDisplaySettings_tFids(env, settings, &PGLOB(PgDisplaySettings_tFc));
        getPgDisplaySettings_tFields(env, settings, lpSettings1, &PGLOB(PgDisplaySettings_tFc));
    }
    result = (jint)PgGetVideoMode(lpSettings1);

    if (settings) {
        setPgDisplaySettings_tFields(env, settings, lpSettings1, &PGLOB(PgDisplaySettings_tFc));
    }
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PgGetVideoModeInfo
 * Signature: (ILorg/eclipse/swt/internal/photon/PgVideoModeInfo_t;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PgGetVideoModeInfo
  (JNIEnv *env, jobject that, jint mode_number, jobject mode_info)
{
	DECL_GLOB(pGlob)	
    PgVideoModeInfo_t mode_info1, *lpmode_info1=NULL;
	jint result;
	
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PgGetVideoModeInfo\n");
#endif

	if (mode_info) {
        lpmode_info1 = &mode_info1;
        cachePgVideoModeInfo_tFids(env, mode_info, &PGLOB(PgVideoModeInfo_tFc));
        getPgVideoModeInfo_tFields(env, mode_info, lpmode_info1, &PGLOB(PgVideoModeInfo_tFc));
    }
    result = (jint)PgGetVideoModeInfo(mode_number, lpmode_info1);

    if (mode_info) {
        setPgVideoModeInfo_tFields(env, mode_info, lpmode_info1, &PGLOB(PgVideoModeInfo_tFc));
    }
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhClipboardCopy
 * Signature: (SI[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhClipboardCopy
  (JNIEnv *env, jclass that, jshort ig, jint n, jbyteArray clip)
{	
   jbyte *clip1;
   jint result;
   
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PhClipboardCopy\n");
#endif

    if (clip) {
        clip1 = (*env)->GetByteArrayElements(env, clip, NULL);
    }
    
    result = (jint)PhClipboardCopy(ig, n, (PhClipHeader const *)clip1);
    
    if (clip) {
        (*env)->ReleaseByteArrayElements(env, clip, clip1, 0);
    }
    
    return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhClipboardPasteStart
 * Signature: (S)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhClipboardPasteStart
  (JNIEnv *env, jobject that, jshort ig)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhClipboardPasteStart\n");
#endif
	
	return (jint) PhClipboardPasteStart(ig);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhClipboardPasteType
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhClipboardPasteType
  (JNIEnv *env, jobject that, jint cbdata, jbyteArray type)
{
	 char *type1=NULL;
	 jint result;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhClipboardPasteType\n");
#endif

 	if (type)
        type1 = (char *)(*env)->GetByteArrayElements(env, type, NULL);
    	
	result = (jint) PhClipboardPasteType((void *)cbdata, type1);
	
	if (type)
        (*env)->ReleaseByteArrayElements(env, type, (jbyte *)type1, 0);
        
	return result;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhClipboardPasteTypeN
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_photon_OS_PhClipboardPasteTypeN
  (JNIEnv *env, jobject that, jint cbdata, jint n)
{

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "PhClipboardPasteTypeN\n");
#endif
	return (jint) PhClipboardPasteTypeN((void *)cbdata, n);
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    PhClipboardPasteFinish
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_PhClipboardPasteFinish
  (JNIEnv *env, jobject that, jint cbdata)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "PhClipboardPasteFinish\n");
#endif
	
	PhClipboardPasteFinish((void *)cbdata);
	
	return;
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/photon/PhClipHeader;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove__Lorg_eclipse_swt_internal_photon_PhClipHeader_2II
  (JNIEnv *env, jobject that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	PhClipHeader object, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_photon_PhClipHeader_2II\n");
#endif

    memmove((void *)&object, (void *)src, count);
    if (dest) {
        dest1=&object;
        cachePhClipHeaderFids(env, dest, &PGLOB(PhClipHeaderFc));
        setPhClipHeaderFields(env, dest, dest1, &PGLOB(PhClipHeaderFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: ([BLorg/eclipse/swt/internal/photon/PhClipHeader;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove___3BLorg_eclipse_swt_internal_photon_PhClipHeader_2I
  (JNIEnv *env, jobject that, jbyteArray dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	jbyte *dest1=NULL;
    PhClipHeader object, *src1= NULL;
    
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove___3BLorg_eclipse_swt_internal_photon_PhClipHeader_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhClipHeaderFids(env, src, &PGLOB(PhClipHeaderFc));
        getPhClipHeaderFields(env, src, src1, &PGLOB(PhClipHeaderFc));
    }
    
    if (dest) {
    	dest1 = (*env)->GetByteArrayElements(env, dest, NULL);
    }
    
    memmove((void *)dest1, (void *)src1, count);
    
    if (dest) {
    	 (*env)->ReleaseByteArrayElements(env, dest, (jbyte *)dest1, 0);
    }
}

/*
 * Class:     org_eclipse_swt_internal_photon_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/photon/PhClipHeader;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_photon_OS_memmove___ILorg_eclipse_swt_internal_photon_PhClipHeader_2I
  (JNIEnv *env, jobject that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
    PhClipHeader object, *src1= NULL;
    
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove___3BLorg_eclipse_swt_internal_photon_PhClipHeader_2I\n");
#endif

    if (src) {
        src1=&object;
        cachePhClipHeaderFids(env, src, &PGLOB(PhClipHeaderFc));
        getPhClipHeaderFields(env, src, src1, &PGLOB(PhClipHeaderFc));
    }
    
    memmove((void *)dest, (void *)src1, count);
}