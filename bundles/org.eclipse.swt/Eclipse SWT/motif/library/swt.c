/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

/**
 * SWT OS natives implementation.
 */ 

/* #define PRINT_FAILED_RCODES */
#define NDEBUG 

#include "globals.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>

JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_motif_OS_getSharedLibraryMajorVersionNumber
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "getSharedLibraryMajorVersionNumber\n");
#endif
    return SWT_LIBRARY_MAJOR_VERSION;
}

JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_motif_OS_getSharedLibraryMinorVersionNumber
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "getSharedLibraryMinorVersionNumber\n");
#endif
    return SWT_LIBRARY_MINOR_VERSION;
}


/*************************************************************************

   Name     :  sleep_ms( unsigned int nmsecs)

   Purpose  :  sleep for a given number of milli seconds
   Parms    :  the number of milli seconds to sleep
   Returns  :  

*************************************************************************/
/*
void sleep_ms( unsigned int nmsecs)
{
    struct timeval tval;
    tval.tv_sec = (nmsecs * 1000) / 1000000;
    tval.tv_usec = (nmsecs * 1000) % 1000000;
    select(0, NULL, NULL, NULL, &tval);
}
*/

/* ------------------------------------- */

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XInitThreads
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XInitThreads
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XInitThreads\n");
#endif
    
	return (jint) XInitThreads();
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    OverrideShellWidgetClass
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_OverrideShellWidgetClass
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "OverrideShellWidgetClass\n");
#endif
    return (jint) (overrideShellWidgetClass);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    ShellWidgetClass
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_ShellWidgetClass
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "ShellWidgetClass\n");
#endif
    return (jint) (shellWidgetClass);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_TopLevelShellWidgetClass
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "TopLevelShellWidgetClass\n");
#endif
    return (jint) (topLevelShellWidgetClass);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    TransientShellWidgetClass
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_TransientShellWidgetClass
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "TransientShellWidgetClass\n");
#endif
    
    return (jint) (transientShellWidgetClass);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/motif/XButtonEvent;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__ILorg_eclipse_swt_internal_motif_XButtonEvent_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
    XEvent xEvent, *src1=NULL;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_motif_XButtonEvent_2I\n");
#endif
    if (src) {
        src1=&xEvent;
        cacheXbuttoneventFids(env, src, &PGLOB(XbuttoneventFc));
        getXbuttoneventFields(env, src, src1, &PGLOB(XbuttoneventFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/motif/XImage;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__ILorg_eclipse_swt_internal_motif_XImage_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
    XImage image, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_motif_XImage_2I\n");
#endif

    if (src) {
        src1=&image;
        cacheXimageFids(env, src, &PGLOB(XimageFc));
        getXimageFields(env, src, src1, &PGLOB(XimageFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/motif/XmTextBlockRec;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__ILorg_eclipse_swt_internal_motif_XmTextBlockRec_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
    XmTextBlockRec xmtextblockrec, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_motif_XmTextBlockRec_2I\n");
#endif

    if (src) {
        src1=&xmtextblockrec;
        cacheXmtextblockrecFids(env, src, &PGLOB(XmtextblockrecFc));
        getXmtextblockrecFields(env, src, src1, &PGLOB(XmtextblockrecFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/motif/XmTextVerifyCallbackStruct;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__ILorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
    XmTextVerifyCallbackStruct xmtextverifycallbackstruct, *src1=NULL;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2I\n");
#endif

    if (src) {
        src1=&xmtextverifycallbackstruct;
        cacheXmtextverifycallbackstructFids(env, src, &PGLOB(XmtextverifycallbackstructFc));
        getXmtextverifycallbackstructFields(env, src, src1, &PGLOB(XmtextverifycallbackstructFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__I_3BI
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
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (I[II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__I_3II
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
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/Visual;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_Visual_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	Visual visual, *lpxVisual=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_Visual_2II\n");
#endif

    memmove((void *)&visual, (void *)src, count);
    if (dest) {
        lpxVisual=&visual;
        cacheVisualFids(env, dest, &PGLOB(VisualFc));
        setVisualFields(env, dest, lpxVisual, &PGLOB(VisualFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XButtonEvent;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XButtonEvent_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XButtonEvent_2II\n");
#endif

    memmove((void *)&xEvent, (void *)src, count);
    if (dest) {
        lpxEvent=&xEvent;
        cacheXbuttoneventFids(env, dest, &PGLOB(XbuttoneventFc));
        setXbuttoneventFields(env, dest, lpxEvent, &PGLOB(XbuttoneventFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XCharStruct;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XCharStruct_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	XCharStruct xCharstruct, *lpxCharstruct=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XCharStruct_2II\n");
#endif

    memmove((void *)&xCharstruct, (void *)src, count);
    if (dest) {
        lpxCharstruct=&xCharstruct;
        cacheXcharstructFids(env, dest, &PGLOB(XcharstructFc));
        setXcharstructFields(env, dest, lpxCharstruct, &PGLOB(XcharstructFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XConfigureEvent;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XConfigureEvent_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XConfigureEvent_2II\n");
#endif
 
    memmove((void *)&xEvent, (void *)src, count);
    if (dest) {
        lpxEvent=&xEvent;
        cacheXconfigureeventFids(env, dest, &PGLOB(XconfigureeventFc));
        setXconfigureeventFields(env, dest, lpxEvent, &PGLOB(XconfigureeventFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XCrossingEvent;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XCrossingEvent_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
        XEvent xEvent, *lpxEvent=NULL;
#ifdef DEBUG_CALL_PRINTS
        fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XCrossingEvent_2II\n");
#endif

    memmove((void *)&xEvent, (void *)src, count);
    if (dest) {
        lpxEvent=&xEvent;
        cacheXcrossingeventFids(env, dest, &PGLOB(XcrossingeventFc));
        setXcrossingeventFields(env, dest, lpxEvent, &PGLOB(XcrossingeventFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XExposeEvent;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XExposeEvent_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XExposeEvent_2II\n");
#endif
    
    memmove((void *)&xEvent, (void *)src, count);
    if (dest) {
        lpxEvent=&xEvent;
        cacheXexposeeventFids(env, dest, &PGLOB(XexposeeventFc));
        setXexposeeventFields(env, dest, lpxEvent, &PGLOB(XexposeeventFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XFocusChangeEvent;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XFocusChangeEvent_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
    XEvent xEvent, *lpxEvent=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XFocusChangeEvent_2II\n");
#endif

    assert(count <= sizeof(XEvent));
    memmove((void *)&xEvent, (void *)src, count);
    if (dest) {
        lpxEvent=&xEvent;
        cacheXfocuschangeeventFids(env, dest, &PGLOB(XfocuschangeeventFc));
        setXfocuschangeeventFields(env, dest, lpxEvent, &PGLOB(XfocuschangeeventFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XFontStruct;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XFontStruct_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
    XFontStruct fontStruct, *lpxFontStruct=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XFontStruct_2II\n");
#endif

    memmove((void *)&fontStruct, (void *)src, count);
    if (dest) {
        lpxFontStruct=&fontStruct;
        cacheXfontstructFids(env, dest, &PGLOB(XfontstructFc));
        setXfontstructFields(env, dest, lpxFontStruct, &PGLOB(XfontstructFc));
    }

}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XImage;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XImage_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
    XImage image, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XImage_2II\n");
#endif

    memmove((void *)&image, (void *)src, count);
    if (dest) {
        dest1=&image;
        cacheXimageFids(env, dest, &PGLOB(XimageFc));
        setXimageFields(env, dest, dest1, &PGLOB(XimageFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XKeyEvent;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2II\n");
#endif
    
    memmove((void *)&xEvent, (void *)src, count);
    if (dest) {
        lpxEvent=&xEvent;
        cacheXkeyeventFids(env, dest, &PGLOB(XkeyeventFc));
        setXkeyeventFields(env, dest, lpxEvent, &PGLOB(XkeyeventFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XKeyEvent;Lorg/eclipse/swt/internal/motif/XAnyEvent;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2Lorg_eclipse_swt_internal_motif_XAnyEvent_2I
  (JNIEnv *env, jclass that, jobject dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
	XEvent xEvent1, *src1=NULL, xEvent2, *dest1=NULL;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XKeyEvent_2Lorg_eclipse_swt_internal_motif_XAnyEvent_2I\n");
#endif
    if (src) {
        src1=&xEvent1;
        cacheXanyeventFids(env, src, &PGLOB(XanyeventFc));
        getXanyeventFields(env, src, src1, &PGLOB(XanyeventFc));
    }
  
    memmove((void *)&xEvent2, (void *)src1, count);
    if (dest) {
        dest1=&xEvent2;
        cacheXkeyeventFids(env, dest, &PGLOB(XkeyeventFc));
        setXkeyeventFields(env, dest, dest1, &PGLOB(XkeyeventFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XMotionEvent;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XMotionEvent_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XMotionEvent_2II\n");
#endif
    
    memmove((void *)&xEvent, (void *)src, count);
    if (dest) {
        lpxEvent=&xEvent;
        cacheXmotioneventFids(env, dest, &PGLOB(XmotioneventFc));
        setXmotioneventFields(env, dest, lpxEvent, &PGLOB(XmotioneventFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XmAnyCallbackStruct;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XmAnyCallbackStruct_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
        XmAnyCallbackStruct xmanycallbackstruct, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
        fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XmAnyCallbackStruct_2II\n");
#endif

    memmove((void *)&xmanycallbackstruct, (void *)src, count);
    if (dest) {
        dest1=&xmanycallbackstruct;
        cacheXmanycallbackstructFids(env, dest, &PGLOB(XmanycallbackstructFc));
        setXmanycallbackstructFields(env, dest, dest1, &PGLOB(XmanycallbackstructFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (ILorg/eclipse/swt/internal/motif/XmDragProcCallback;I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__ILorg_eclipse_swt_internal_motif_XmDragProcCallback_2I
  (JNIEnv *env, jclass that, jint dest, jobject src, jint count)
{
	DECL_GLOB(pGlob)
    XmDragProcCallbackStruct xmdragproccallback, *src1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__ILorg_eclipse_swt_internal_motif_XmDragProcCallback_2I\n");
#endif

    if (src) {
        src1=&xmdragproccallback;
        cacheXmdragproccallbackFids(env, src, &PGLOB(XmdragproccallbackFc));
        getXmdragproccallbackFields(env, src, src1, &PGLOB(XmdragproccallbackFc));
    }
    memmove((void *)dest, (void *)src1, count);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XmDragProcCallback;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XmDragProcCallback_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
    XmDragProcCallbackStruct xmdragproccallback, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XmDragProcCallback_2II\n");
#endif

    memmove((void *)&xmdragproccallback, (void *)src, count);
    if (dest) {
        dest1=&xmdragproccallback;
        cacheXmdragproccallbackFids(env, dest, &PGLOB(XmdragproccallbackFc));
        setXmdragproccallbackFields(env, dest, dest1, &PGLOB(XmdragproccallbackFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XmDropProcCallback;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XmDropProcCallback_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
    XmDropProcCallbackStruct xmdropproccallback, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XmDropProcCallback_2II\n");
#endif

    memmove((void *)&xmdropproccallback, (void *)src, count);
    if (dest) {
        dest1=&xmdropproccallback;
        cacheXmdropproccallbackFids(env, dest, &PGLOB(XmdropproccallbackFc));
        setXmdropproccallbackFields(env, dest, dest1, &PGLOB(XmdropproccallbackFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XmDropFinishCallback;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XmDropFinishCallback_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
    XmDropFinishCallbackStruct xmdropfinishcallback, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XmDropFinishCallback_2II\n");
#endif

    memmove((void *)&xmdropfinishcallback, (void *)src, count);
    if (dest) {
        dest1=&xmdropfinishcallback;
        cacheXmdropfinishcallbackFids(env, dest, &PGLOB(XmdropfinishcallbackFc));
        setXmdropfinishcallbackFields(env, dest, dest1, &PGLOB(XmdropfinishcallbackFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XmTextBlockRec;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XmTextBlockRec_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	XmTextBlockRec xmTextBlockRec, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XmTextBlockRec_2II\n");    
#endif

    memmove((void *)&xmTextBlockRec, (void *)src, count);
    if (dest) {
        dest1=&xmTextBlockRec;
        cacheXmtextblockrecFids(env, dest, &PGLOB(XmtextblockrecFc));
        setXmtextblockrecFields(env, dest, dest1, &PGLOB(XmtextblockrecFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: (Lorg/eclipse/swt/internal/motif/XmTextVerifyCallbackStruct;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove__Lorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2II
  (JNIEnv *env, jclass that, jobject dest, jint src, jint count)
{
	DECL_GLOB(pGlob)
	XmTextVerifyCallbackStruct xmtextverifycallbackstruct, *dest1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "memmove__Lorg_eclipse_swt_internal_motif_XmTextVerifyCallbackStruct_2II\n");
#endif

    memmove((void *)&xmtextverifycallbackstruct, (void *)src, count);
    if (dest) {
        dest1=&xmtextverifycallbackstruct;
        cacheXmtextverifycallbackstructFids(env, dest, &PGLOB(XmtextverifycallbackstructFc));
        setXmtextverifycallbackstructFields(env, dest, dest1, &PGLOB(XmtextverifycallbackstructFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: ([BII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove___3BII
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
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    memmove
 * Signature: ([III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_memmove___3III
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

/* end of custom built crap */

/* ------------------------------------------------------------------------- */

/* the following map directly to X calls */

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XAllocColor
 * Signature: (IILorg/eclipse/swt/internal/motif/XColor;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XAllocColor
  (JNIEnv *env, jclass that, jint display, jint colormap, jobject color)
{
	DECL_GLOB(pGlob)
	XColor xColor, *lpColor=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XAllocColor\n");
#endif

    assert (color != 0);
    if (color) {
        lpColor = &xColor;
        cacheXcolorFids(env, color, &PGLOB(XcolorFc));
        getXcolorFields(env, color, lpColor, &PGLOB(XcolorFc));
    }
    rc = (jint) XAllocColor ((Display *)display, colormap, lpColor);

#ifdef PRINT_FAILED_RCODES
    
    if (rc == 0)
        fprintf(stderr, "XAllocColor: call failed rc = %d\n", rc);
#endif

    if (color) {
        setXcolorFields(env, color, lpColor, &PGLOB(XcolorFc));
    }

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XBell
 * Signature: (II)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XBell
  (JNIEnv *env, jclass that, jint display, jint ms)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XBell\n");
#endif
    XBell ((Display *)display, ms);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XBitmapBitOrder
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XBitmapBitOrder
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XBitmapBitOrder\n");
#endif
    return (jint) XBitmapBitOrder((Display *) display);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XBlackPixel
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XBlackPixel
  (JNIEnv *env, jclass that, jint display, jint screen_number)
{
    
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XBlackPixel\n");
#endif
    return (jint) XBlackPixel((Display *)display, screen_number);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XChangeGC
 * Signature: (IIILorg/eclipse/swt/internal/motif/XGCValues;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XChangeGC
  (JNIEnv *env, jclass that, jint display, jint gc, jint valuemask, jobject values)
{
	DECL_GLOB(pGlob)
    XGCValues xgcValues, *lpxgcValues=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XChangeGC\n");
#endif

    if (values) {
        lpxgcValues = &xgcValues;
        cacheXgcvaluesFids(env, values, &PGLOB(XgcvaluesFc));
        getXgcvaluesFields(env, values, lpxgcValues, &PGLOB(XgcvaluesFc));
    }
    rc  = (jint) XChangeGC((Display *)display, (GC)gc, valuemask, lpxgcValues);

    if (values) {
        setXgcvaluesFields(env, values, lpxgcValues, &PGLOB(XgcvaluesFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XChangeWindowAttributes
 * Signature: (IIILorg/eclipse/swt/internal/motif/XSetWindowAttributes;)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XChangeWindowAttributes
  (JNIEnv *env, jclass that, jint display, jint window, jint mask, jobject attributes)
{
	DECL_GLOB(pGlob)
	XSetWindowAttributes xSetWindowAttributes, *lpxSetWindowAttributes;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XChangeWindowAttributes\n");
#endif

    if (attributes) {
        lpxSetWindowAttributes = &xSetWindowAttributes;
        cacheXsetwindowattributesFids(env, attributes, &PGLOB(XsetwindowattributesFc));
        getXsetwindowattributesFields(env, attributes, lpxSetWindowAttributes, &PGLOB(XsetwindowattributesFc));
    }

    XChangeWindowAttributes((Display *)display, window, mask, lpxSetWindowAttributes);
    if (attributes) {
        setXsetwindowattributesFields(env, attributes, lpxSetWindowAttributes, &PGLOB(XsetwindowattributesFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCheckMaskEvent
 * Signature: (IILorg/eclipse/swt/internal/motif/XAnyEvent;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XCheckMaskEvent
  (JNIEnv *env, jclass that, jint display, jint mask, jobject event)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
    jboolean rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCheckMaskEvent\n");
#endif

    if (event) {
        lpxEvent = &xEvent;
        cacheXanyeventFids(env, event, &PGLOB(XanyeventFc));
        getXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    }
    rc = (jboolean) XCheckMaskEvent((Display *)display, mask, lpxEvent);

#ifdef PRINT_FAILED_RCODES
    
    if (rc != True && rc != False)
        fprintf(stderr, "XCheckMaskEvent: call failed rc = %d\n", rc);
#endif

    if (event) {
        setXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCheckWindowEvent
 * Signature: (IIILorg/eclipse/swt/internal/motif/XAnyEvent;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XCheckWindowEvent
  (JNIEnv *env, jclass that, jint display, jint window, jint mask, jobject event)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
    jboolean rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCheckWindowEvent\n");
#endif

    if (event) {
        lpxEvent = &xEvent;
        cacheXanyeventFids(env, event, &PGLOB(XanyeventFc));
        getXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    }
    rc = (jboolean)XCheckWindowEvent((Display *)display, window, mask, lpxEvent);

#ifdef PRINT_FAILED_RCODES
    
    if (rc != True && rc != False)
        fprintf(stderr, "XCheckWindowEvent: call failed rc = %d\n", rc);
#endif

    if (event) {
        setXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XClearArea
 * Signature: (IIIIIIZ)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XClearArea
  (JNIEnv *env, jclass that, jint display, jint window, jint x, jint y, jint width, jint height, jboolean exposures)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XClearArea\n");
#endif
    XClearArea((Display *)display, window, x, y, width, height, exposures);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XClipBox
 * Signature: (ILorg/eclipse/swt/internal/motif/XRectangle;)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XClipBox
  (JNIEnv *env, jclass that, jint region, jobject rectangle)
{
	DECL_GLOB(pGlob)
	XRectangle xRect, *lpxRect=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XClipBox\n");
#endif

    if (rectangle) {
        lpxRect = &xRect;
        cacheXrectangleFids(env, rectangle, &PGLOB(XrectangleFc));
        getXrectangleFields(env, rectangle, lpxRect, &PGLOB(XrectangleFc));
    }
    XClipBox((Region)region, (XRectangle *)lpxRect);
    if (rectangle) {
        setXrectangleFields(env, rectangle, lpxRect, &PGLOB(XrectangleFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCopyArea
 * Signature: (IIIIIIIIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XCopyArea
  (JNIEnv *env, jclass that, jint display, jint src, jint dest, jint gc, jint src_x, jint src_y, jint width, jint height, jint dest_x, jint dest_y)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCopyArea\n");
#endif
	XCopyArea((Display *)display, src, dest, (GC)gc, src_x, src_y, width, height, dest_x, dest_y);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCopyPlane
 * Signature: (IIIIIIIIIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XCopyPlane
  (JNIEnv *env, jclass that, jint display, jint src, jint dest, jint gc, jint src_x, jint src_y, jint width, jint height, jint dest_x, jint dest_y, jint plane)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCopyPlane\n");
#endif
	XCopyPlane((Display *)display, src, dest, (GC)gc, src_x, src_y, width, height, dest_x, dest_y, plane);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCreateBitmapFromData
 * Signature: (II[BII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XCreateBitmapFromData
  (JNIEnv *env, jclass that, jint display, jint drawable, jbyteArray data, jint width, jint height)
{
    jbyte *data1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCreateBitmapFromData\n");
#endif

    if (data)
        data1 = (*env)->GetByteArrayElements(env, data, NULL);

    rc = (jint) XCreateBitmapFromData((Display *)display, drawable, (char *)data1, width, height);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XCreateBitmapFromData: call failed rc = %d\n", rc);
#endif

    if (data)
        (*env)->ReleaseByteArrayElements(env, data, data1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCreateFontCursor
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XCreateFontCursor
  (JNIEnv *env, jclass that, jint display, jint shape)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCreateFontCursor\n");
#endif
	return (jint) XCreateFontCursor((Display *)display, shape);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCreateGC
 * Signature: (IIILorg/eclipse/swt/internal/motif/XGCValues;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XCreateGC
  (JNIEnv *env, jclass that, jint display, jint window, jint mask, jobject values)
{
	DECL_GLOB(pGlob)
	XGCValues xgcValues, *lpxgcValues=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCreateGC\n");
#endif

    if (values) {
        lpxgcValues = &xgcValues;
        cacheXgcvaluesFids(env, values, &PGLOB(XgcvaluesFc));
        getXgcvaluesFields(env, values, lpxgcValues, &PGLOB(XgcvaluesFc));
    }
    rc  = (jint) XCreateGC((Display *)display, window, mask, lpxgcValues);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XCreateGC: call failed rc = %d\n", rc);
#endif

    if (values) {
        setXgcvaluesFields(env, values, lpxgcValues, &PGLOB(XgcvaluesFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCreateImage
 * Signature: (IIIIIIIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XCreateImage
  (JNIEnv *env, jclass that, jint display, jint visual, jint depth, jint format, jint offset, jint data, jint width, jint height, jint bitmap_pad, jint bytes_per_line)
{
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCreateImage\n");
#endif

    rc = (jint) XCreateImage((Display *)display, (Visual *)visual, depth, format, offset, (char *)data, width, height, bitmap_pad, bytes_per_line);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XCreateImage: call failed rc = %d\n", rc);
#endif

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCreatePixmap
 * Signature: (IIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XCreatePixmap
  (JNIEnv *env, jclass that, jint display, jint drawable, jint width, jint height, jint depth)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCreatePixmap\n");
#endif
	return (jint) XCreatePixmap((Display *)display, drawable, width, height, depth);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCreatePixmapCursor
 * Signature: (IIILorg/eclipse/swt/internal/motif/XColor;Lorg/eclipse/swt/internal/motif/XColor;II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XCreatePixmapCursor
  (JNIEnv *env, jclass that, jint display, jint source, jint mask, jobject foreground_color, jobject background_color, jint x, jint y)
{
	DECL_GLOB(pGlob)
    XColor background_color1, foreground_color1;
    XColor *lp_background_color = NULL, *lp_foreground_color = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCreatePixmapCursor\n");
#endif

    assert(foreground_color != 0);
    if (foreground_color) {
        lp_foreground_color = &foreground_color1;
        cacheXcolorFids(env, foreground_color, &PGLOB(XcolorFc));
        getXcolorFields(env, foreground_color, lp_foreground_color, &PGLOB(XcolorFc));
    }
    if (background_color) {
        lp_background_color = &background_color1;
        cacheXcolorFids(env, background_color, &PGLOB(XcolorFc));
        getXcolorFields(env, background_color, lp_background_color, &PGLOB(XcolorFc));
    }
        
    return (jint) XCreatePixmapCursor((Display *)display, (Pixmap)source, (Pixmap)mask, lp_foreground_color, lp_background_color, x, y);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCreateRegion
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XCreateRegion
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCreateRegion\n");
#endif
	return (jint) XCreateRegion();
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDefaultColormap
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDefaultColormap
  (JNIEnv *env, jclass that, jint display, jint screen_number)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDefaultColormap\n");
#endif
    return (jint) XDefaultColormap((Display *)display, screen_number);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDefaultDepthOfScreen
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDefaultDepthOfScreen
  (JNIEnv *env, jclass that, jint screen)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDefaultDepthOfScreen\n");
#endif
	return (jint) XDefaultDepthOfScreen((Screen *)screen);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDefaultRootWindow
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDefaultRootWindow
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDefaultRootWindow\n");
#endif
    return (jint) XDefaultRootWindow((Display *)display);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDefaultScreen
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDefaultScreen
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDefaultScreen\n");
#endif
    return (jint) XDefaultScreen((Display *)display);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDefaultScreenOfDisplay
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDefaultScreenOfDisplay
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDefaultScreenOfDisplay\n");
#endif
	return (jint) XDefaultScreenOfDisplay((Display *)display);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDefaultVisual
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDefaultVisual
  (JNIEnv *env, jclass that, jint display, jint screen_number)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDefaultVisual\n");
#endif
    return (jint) XDefaultVisual((Display *)display, screen_number);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDefineCursor
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XDefineCursor
  (JNIEnv *env, jclass that, jint display, jint window, jint cursor)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDefineCursor\n");
#endif
	XDefineCursor((Display *)display, window, cursor);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDestroyImage
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDestroyImage
  (JNIEnv *env, jclass that, jint ximage)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDestroyImage\n");
#endif
    return (jint) XDestroyImage((XImage *)ximage);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDestroyRegion
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XDestroyRegion
  (JNIEnv *env, jclass that, jint region)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDestroyRegion\n");
#endif
	XDestroyRegion((Region)region);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDisplayHeight
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDisplayHeight
  (JNIEnv *env, jclass that, jint display, jint screen)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDisplayHeight\n");
#endif
    return (jint) XDisplayHeight((Display *)display, screen);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDisplayHeightMM
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDisplayHeightMM
  (JNIEnv *env, jclass that, jint display, jint screen)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDisplayHeightMM\n");
#endif
    return (jint) XDisplayHeightMM((Display *)display, screen);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDisplayWidth
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDisplayWidth
  (JNIEnv *env, jclass that, jint display, jint screen)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDisplayWidth\n");
#endif
    return (jint) XDisplayWidth((Display *)display, screen);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDisplayWidthMM
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDisplayWidthMM
  (JNIEnv *env, jclass that, jint display, jint screen)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDisplayWidthMM\n");
#endif
    return (jint) XDisplayWidthMM((Display *)display, screen);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDrawArc
 * Signature: (IIIIIIIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XDrawArc
  (JNIEnv *env, jclass that, jint display, jint drawable, jint gc, jint x1, jint y1, jint x2, jint y2, jint a1, jint a2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDrawArc\n");
#endif
    XDrawArc((Display *)display, drawable, (GC)gc, x1, y1, x2, y2, a1, a2);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDrawLine
 * Signature: (IIIIIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XDrawLine
  (JNIEnv *env, jclass that, jint display, jint drawable, jint gc, jint x1, jint y1, jint x2, jint y2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDrawLine\n");
#endif
	XDrawLine((Display *)display, drawable, (GC)gc, x1, y1, x2, y2);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDrawLines
 * Signature: (III[SII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XDrawLines
  (JNIEnv *env, jclass that, jint display, jint drawable, jint gc, jshortArray xPoints, jint nPoints, jint mode)
{
    jshort *xPoints1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDrawLines\n");
#endif

    if (xPoints)
        xPoints1 = (*env)->GetShortArrayElements(env, xPoints, NULL);
    XDrawLines((Display *)display, drawable, (GC)gc, (XPoint *)xPoints1, nPoints, mode);
    if (xPoints)
        (*env)->ReleaseShortArrayElements(env, xPoints, xPoints1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDrawRectangle
 * Signature: (IIIIIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XDrawRectangle
  (JNIEnv *env, jclass that, jint display, jint drawable, jint gc, jint x, jint y, jint width, jint height)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "\n");
#endif
	XDrawRectangle((Display *)display, drawable, (GC)gc, x, y, width, height);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XEmptyRegion
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XEmptyRegion
  (JNIEnv *env, jclass that, jint region)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XEmptyRegion\n");
#endif
	return (jboolean) XEmptyRegion((Region)region);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XEqualRegion
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XEqualRegion
  (JNIEnv *env, jclass that, jint region1, jint region2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XEqualRegion\n");
#endif
	return (jboolean) XEqualRegion((Region)region1, (Region)region2);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFillArc
 * Signature: (IIIIIIIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XFillArc
  (JNIEnv *env, jclass that, jint display, jint drawable, jint gc, jint x1, jint y1, jint x2, jint y2, jint a1, jint a2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFillArc\n");
#endif
    XFillArc((Display *)display, drawable, (GC)gc, x1, y1, x2, y2, a1, a2);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFillPolygon
 * Signature: (III[SIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XFillPolygon
  (JNIEnv *env, jclass that, jint display, jint drawable, jint gc, jshortArray xPoints, jint nPoints, jint mode, jint style)
{
    jshort *xPoints1=NULL;
	jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFillPolygon\n");
#endif
    if (xPoints)
        xPoints1 = (*env)->GetShortArrayElements(env, xPoints, NULL);
    rc = XFillPolygon((Display *)display, drawable, (GC)gc, (XPoint *)xPoints1, nPoints, mode, style);
    if (xPoints)
        (*env)->ReleaseShortArrayElements(env, xPoints, xPoints1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFillRectangle
 * Signature: (IIIIIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XFillRectangle
  (JNIEnv *env, jclass that, jint display, jint drawable, jint gc, jint x, jint y, jint width, jint height)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFillRectangle\n");
#endif
    XFillRectangle((Display *)display, drawable, (GC)gc, x, y, width, height);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFilterEvent
 * Signature: (Lorg/eclipse/swt/internal/motif/XAnyEvent;I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XFilterEvent
  (JNIEnv *env, jclass that, jobject event, jint window)
{
	DECL_GLOB(pGlob)
	XEvent xanyevent, *event1;
    jboolean rc;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFilterEvent\n");
#endif
    if (event) {
        event1=&xanyevent;
        cacheXanyeventFids(env, event, &PGLOB(XanyeventFc));
        getXanyeventFields(env, event, event1, &PGLOB(XanyeventFc));
    }
    rc = (jboolean) XFilterEvent(event1, (Window)window);
    if (event) {
        setXanyeventFields(env, event, event1, &PGLOB(XanyeventFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFlush
 * Signature: (I)I
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XFlush
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFlush\n");
#endif
    XFlush((Display *)display);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFontsOfFontSet
 * Signature: (I[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XFontsOfFontSet
  (JNIEnv *env, jclass that, jint fontSet, jintArray fontStructs, jintArray fontNames)
{
    jint *fontStructs1=NULL,*fontNames1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFontsOfFontSet\n");
#endif
    if (fontStructs)
        fontStructs1 = (*env)->GetIntArrayElements(env, fontStructs, NULL);
    if (fontNames)
        fontNames1 = (*env)->GetIntArrayElements(env, fontNames, NULL);

    rc = (jint) XFontsOfFontSet((XFontSet)fontSet, (XFontStruct ***)fontStructs1, (char ***)fontNames1);

    if (fontStructs)
        (*env)->ReleaseIntArrayElements(env, fontStructs, fontStructs1, 0);
    if (fontNames)
        (*env)->ReleaseIntArrayElements(env, fontNames, fontNames1, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFree
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XFree
  (JNIEnv *env, jclass that, jint address)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFree\n");
#endif
    return (jint) XFree((char *)address);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFreeColors
 * Signature: (II[III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XFreeColors
  (JNIEnv *env, jclass that, jint display, jint colormap, jintArray pixels, jint npixels, jint planes)
{
    jint *pixels1=NULL;
    jint rc;
    jint intArr[2]={250, 250};
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFreeColors\n");
#endif
    
    assert(pixels != 0);
    assert(npixels > 0);
    assert(npixels < 20);

    if (pixels) {
	pixels1 = (jint *)&intArr;
        (*env)->GetIntArrayRegion(env, pixels, 0, npixels, pixels1);
    }

    assert(*pixels1 >= 0);
    assert(*pixels1 < 65536);
    assert(*(pixels1+1) >= 0);
    assert(*(pixels1+1) < 65536);
    
    rc = (jint) XFreeColors((Display *)display, colormap, (unsigned long *)pixels1, npixels, planes);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XCreateGC: call failed rc = %d\n", rc);
#endif

    assert(rc > 0);

    if (pixels) {
        (*env)->SetIntArrayRegion(env, pixels, 0, npixels, pixels1);
    }

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFreeCursor
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XFreeCursor
  (JNIEnv *env, jclass that, jint display, jint pixmap)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFreeCursor\n");
#endif
	XFreeCursor((Display *)display, pixmap);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFreeFont
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XFreeFont
  (JNIEnv *env, jclass that, jint display, jint font_struct)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFreeFont\n");
#endif
	XFreeFont((Display *)display, (XFontStruct *)font_struct);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFreeFontSet
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XFreeFontSet
  (JNIEnv *env, jclass that, jint display, jint font_set)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFreeFontSet\n");
#endif
	XFreeFontSet((Display *)display, (XFontSet)font_set);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFreeFontNames
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XFreeFontNames
  (JNIEnv *env, jclass that, jint list)
{
    XFreeFontNames((char **)list);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFreeGC
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XFreeGC
  (JNIEnv *env, jclass that, jint display, jint gc)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFreeGC\n");
#endif
	XFreeGC((Display *)display, (GC)gc);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XFreePixmap
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XFreePixmap
  (JNIEnv *env, jclass that, jint display, jint pixmap)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XFreePixmap\n");
#endif
	XFreePixmap((Display *)display, pixmap);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XGetGCValues
 * Signature: (IIILorg/eclipse/swt/internal/motif/XGCValues;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XGetGCValues
  (JNIEnv *env, jclass that, jint display, jint gc, jint valuemask, jobject values)
{
	DECL_GLOB(pGlob)
    XGCValues xgcValues, *lpxgcValues=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XGetGCValues\n");
#endif

    if (values) {
        lpxgcValues = &xgcValues;
        cacheXgcvaluesFids(env, values, &PGLOB(XgcvaluesFc));
        getXgcvaluesFields(env, values, lpxgcValues, &PGLOB(XgcvaluesFc));
    }
    rc  = (jint) XGetGCValues((Display *)display, (GC)gc, valuemask, lpxgcValues);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XGetGCValues: call failed rc = %d\n", rc);
#endif

    if (values) {
        setXgcvaluesFields(env, values, lpxgcValues, &PGLOB(XgcvaluesFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XGetGeometry
 * Signature: (II[I[I[I[I[I[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XGetGeometry
  (JNIEnv *env, jclass that, jint display, jint drawable, jintArray root_return, jintArray x_return, jintArray y_return, jintArray width_return, jintArray height_return, jintArray border_width_return, jintArray depth_return)
{
    jint *root_return1=NULL, *x_return1=NULL, *y_return1=NULL, *width_return1=NULL, *height_return1=NULL, *border_width_return1=NULL, *depth_return1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XGetGeometry\n");
#endif

    if (root_return)
        root_return1 = (*env)->GetIntArrayElements(env, root_return, NULL);    
    if (x_return)
        x_return1 = (*env)->GetIntArrayElements(env, x_return, NULL);    
    if (y_return)
        y_return1 = (*env)->GetIntArrayElements(env, y_return, NULL);    
    if (width_return)
        width_return1 = (*env)->GetIntArrayElements(env, width_return, NULL);    
    if (height_return)
        height_return1 = (*env)->GetIntArrayElements(env, height_return, NULL);    
    if (border_width_return)
        border_width_return1 = (*env)->GetIntArrayElements(env, border_width_return, NULL);    
    if (depth_return)
        depth_return1 = (*env)->GetIntArrayElements(env, depth_return, NULL);    

    rc = (jint) XGetGeometry((Display *)display, drawable, (Window *)root_return1, (int *)x_return1, (int *)y_return1, (unsigned int *)width_return1, (unsigned int *)height_return1, (unsigned int *)border_width_return1, (unsigned int *)depth_return1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XGetGeometry: call failed rc = %d\n", rc);
#endif

    if (root_return)
        (*env)->ReleaseIntArrayElements(env, root_return, root_return1, 0);
    if (x_return)
        (*env)->ReleaseIntArrayElements(env, x_return, x_return1, 0);
    if (y_return)
        (*env)->ReleaseIntArrayElements(env, y_return, y_return1, 0);
    if (width_return)
        (*env)->ReleaseIntArrayElements(env, width_return, width_return1, 0);
    if (height_return)
        (*env)->ReleaseIntArrayElements(env, height_return, height_return1, 0);
    if (border_width_return)
        (*env)->ReleaseIntArrayElements(env, border_width_return, border_width_return1, 0);
    if (depth_return)
        (*env)->ReleaseIntArrayElements(env, depth_return, depth_return1, 0);
        
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XGetImage
 * Signature: (IIIIIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XGetImage
  (JNIEnv *env, jclass that, jint display, jint drawable, jint x, jint y, jint width, jint height, jint plane_mask, jint format)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XGetImage\n");
#endif
    return (jint) XGetImage((Display *)display, (Drawable)drawable, x, y, width, height, plane_mask, format);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XGetInputFocus
 * Signature: (I[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XGetInputFocus
  (JNIEnv *env, jclass that, jint display, jintArray window, jintArray revert)
{
    jint *window1, *revert1;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XGetInputFocus\n");
#endif

    if (window)
        window1 = (*env)->GetIntArrayElements(env, window, NULL);    
    if (revert)
        revert1 = (*env)->GetIntArrayElements(env, revert, NULL);    
    rc = (jint) XGetInputFocus((Display *)display, (Window *)window1, (int *)revert1);
    
#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XGetInputFocus: call failed rc = %d\n", rc);
#endif

    if (window)
        (*env)->ReleaseIntArrayElements(env, window, window1, 0);
    if (revert)
        (*env)->ReleaseIntArrayElements(env, revert, revert1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XGetWindowAttributes
 * Signature: (IILorg/eclipse/swt/internal/motif/XWindowAttributes;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XGetWindowAttributes
  (JNIEnv *env, jclass that, jint display, jint window, jobject attributes)
{
	DECL_GLOB(pGlob)
	XWindowAttributes xWindowAttributes, *lpxWindowAttributes;
    jboolean rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XGetWindowAttributes\n");
#endif

    if (attributes) {
        lpxWindowAttributes = &xWindowAttributes;
        cacheXwindowattributesFids(env, attributes, &PGLOB(XwindowattributesFc));
        getXwindowattributesFields(env, attributes, lpxWindowAttributes, &PGLOB(XwindowattributesFc));
    }
    
    rc = (jboolean) XGetWindowAttributes((Display *)display, window, lpxWindowAttributes);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XGetWindowAttributes: call failed rc = %d\n", rc);
#endif

    if (attributes) {
        setXwindowattributesFields(env, attributes, lpxWindowAttributes, &PGLOB(XwindowattributesFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XGrabPointer
 * Signature: (IIIIIIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XGrabPointer
  (JNIEnv *env, jclass that, jint display, jint grabWindow, jint ownerEvents, jint eventMask,
        jint PointerMode, jint KeyboardMode, jint confineToWindow, jint cursor, jint time)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XGrabPointer\n");
#endif
    return (jint) XGrabPointer((Display *)display, grabWindow, ownerEvents, eventMask, PointerMode, 
                        KeyboardMode, confineToWindow, cursor, time);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XKeysymToString
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XKeysymToString
  (JNIEnv *env, jclass that, jint keysym)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XKeysymToString\n");
#endif
    return (jint) XKeysymToString(keysym);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XListFonts
 * Signature: (I[BI[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XListFonts
  (JNIEnv *env, jclass that, jint display, jbyteArray pattern, jint maxnames, jintArray actual_count_return)
{
    jbyte *pattern1 = NULL;
    jint *actual_count_return1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XListFonts\n");
#endif
    if (pattern)    
        pattern1 = (*env)->GetByteArrayElements(env, pattern, NULL);
    if (actual_count_return)    
        actual_count_return1 = (*env)->GetIntArrayElements(env, actual_count_return, NULL);
    
    rc = (jint) XListFonts((Display *)display, (char *)pattern1, maxnames, (int *)actual_count_return1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XListFonts: call failed rc = %d\n", rc);
#endif

    if (pattern)
        (*env)->ReleaseByteArrayElements(env, pattern, pattern1, 0);
    if (actual_count_return)
        (*env)->ReleaseIntArrayElements(env, actual_count_return, actual_count_return1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XLookupString
 * Signature: (Lorg/eclipse/swt/internal/motif/XKeyEvent;[BI[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XLookupString
  (JNIEnv *env, jclass that, jobject keyEvent, jbyteArray string, jint size, jintArray keysym, jintArray status)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
    jint *keysym1=NULL, *status1=NULL;
    jbyte *string1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XLookupString\n");
#endif

    if (keyEvent) {
        lpxEvent = &xEvent;
        cacheXkeyeventFids(env, keyEvent, &PGLOB(XkeyeventFc));
        getXkeyeventFields(env, keyEvent, lpxEvent, &PGLOB(XkeyeventFc));
    }

    if (string)
        string1 = (*env)->GetByteArrayElements(env, string, NULL);    
    if (keysym)
        keysym1 = (*env)->GetIntArrayElements(env, keysym, NULL);    
    if (status)
        status1 = (*env)->GetIntArrayElements(env, status, NULL);    
    rc  = (jint) XLookupString((XKeyEvent *)lpxEvent, (char *)string1, size, (KeySym *)keysym1, (XComposeStatus *)status1);

#ifdef PRINT_FAILED_RCODES
    if (rc < 0)
        fprintf(stderr, "XLookupString: call failed rc = %d\n", rc);
#endif

    if (string)
        (*env)->ReleaseByteArrayElements(env, string, string1, 0);
    if (keysym)
        (*env)->ReleaseIntArrayElements(env, keysym, keysym1, 0);
    if (status)
        (*env)->ReleaseIntArrayElements(env, status, status1, 0);

    if (keyEvent) {
        setXkeyeventFields(env, keyEvent, lpxEvent, &PGLOB(XkeyeventFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XLowerWindow
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XLowerWindow
  (JNIEnv *env, jclass that, jint display, jint window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XLowerWindow\n");
#endif
    return (jint) XLowerWindow((Display *)display, window);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XPointInRegion
 * Signature: (III)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XPointInRegion
  (JNIEnv *env, jclass that, jint region, jint x, jint y)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XPointInRegion\n");
#endif
	return (jboolean) XPointInRegion((Region)region, x, y);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XPutImage
 * Signature: (IIIIIIIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XPutImage
  (JNIEnv *env, jclass that, jint display, jint drawable, jint gc, jint image, jint srcX, jint srcY, jint destX, jint destY, jint width, jint height)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XPutImage\n");
#endif
    return (jint) XPutImage((Display *)display, drawable, (GC)gc, (XImage *)image, srcX, srcY, destX, destY, width, height);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XQueryColor
 * Signature: (IILorg/eclipse/swt/internal/motif/XColor;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XQueryColor
  (JNIEnv *env, jclass that, jint display, jint colormap, jobject color)
{
	DECL_GLOB(pGlob)
	XColor xColor, *lpColor=NULL;
	jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XQueryColor\n");
#endif

    if (color) {
        lpColor = &xColor;
        cacheXcolorFids(env, color, &PGLOB(XcolorFc));
        getXcolorFields(env, color, lpColor, &PGLOB(XcolorFc));
    }

    rc = (jint) XQueryColor((Display *)display, colormap, lpColor);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XQueryColor: call failed rc = %d\n", rc);
#endif

    if (color) {
        setXcolorFields(env, color, lpColor, &PGLOB(XcolorFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XQueryPointer
 * Signature: (II[I[I[I[I[I[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XQueryPointer
  (JNIEnv *env, jclass that, jint display, jint window, jintArray root, jintArray child,
             jintArray rootX, jintArray rootY, jintArray windowX, jintArray windowY, jintArray mask)
{
    jint *root1=NULL, *child1=NULL, *rootX1=NULL, *rootY1=NULL, *windowX1=NULL, *windowY1=NULL, *mask1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XQueryPointer\n");
#endif

    if (root)
        root1 = (*env)->GetIntArrayElements(env, root, NULL);    
    if (child)
        child1 = (*env)->GetIntArrayElements(env, child, NULL);    
    if (rootX)
        rootX1 = (*env)->GetIntArrayElements(env, rootX, NULL);    
    if (rootY)
        rootY1 = (*env)->GetIntArrayElements(env, rootY, NULL);    
    if (windowX)
        windowX1 = (*env)->GetIntArrayElements(env, windowX, NULL);    
    if (windowY)
        windowY1 = (*env)->GetIntArrayElements(env, windowY, NULL);    
    if (mask)
        mask1 = (*env)->GetIntArrayElements(env, mask, NULL);    

    rc = (jint) XQueryPointer((Display *)display, window, (Window *)root1, (Window *)child1, (int *)rootX1, (int *)rootY1, (int *)windowX1, (int *)windowY1, (unsigned int *)mask1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XQueryPointer: call failed rc = %d\n", rc);
#endif

    if (root)
        (*env)->ReleaseIntArrayElements(env, root, root1, 0);
    if (child)
        (*env)->ReleaseIntArrayElements(env, child, child1, 0);
    if (rootX)
        (*env)->ReleaseIntArrayElements(env, rootX, rootX1, 0);
    if (rootY)
        (*env)->ReleaseIntArrayElements(env, rootY, rootY1, 0);
    if (windowX)
        (*env)->ReleaseIntArrayElements(env, windowX, windowX1, 0);
    if (windowY)
        (*env)->ReleaseIntArrayElements(env, windowY, windowY1, 0);
    if (mask)
        (*env)->ReleaseIntArrayElements(env, mask, mask1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XQueryTree
 * Signature: (II[I[I[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XQueryTree
  (JNIEnv *env, jclass that, jint display, jint window, jintArray root_return, jintArray parent_return, jintArray children_return, jintArray nChildren_return)
{
    jint *root_return1=NULL, *parent_return1=NULL, *children_return1=NULL, *nChildren_return1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XQueryTree\n");
#endif

    if (root_return)
        root_return1 = (*env)->GetIntArrayElements(env, root_return, NULL);    
    if (parent_return)
        parent_return1 = (*env)->GetIntArrayElements(env, parent_return, NULL);    
    if (children_return)
        children_return1 = (*env)->GetIntArrayElements(env, children_return, NULL);    
    if (nChildren_return)
        nChildren_return1 = (*env)->GetIntArrayElements(env, nChildren_return, NULL);    

    rc = (jint) XQueryTree((Display *)display, window, (Window *)root_return1, (Window *)parent_return1, (Window **)children_return1, (unsigned int *)nChildren_return1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XQueryTree: call failed rc = %d\n", rc);
#endif

    if (root_return)
        (*env)->ReleaseIntArrayElements(env, root_return, root_return1, 0);
    if (parent_return)
        (*env)->ReleaseIntArrayElements(env, parent_return, parent_return1, 0);
    if (children_return)
        (*env)->ReleaseIntArrayElements(env, children_return, children_return1, 0);
    if (nChildren_return)
        (*env)->ReleaseIntArrayElements(env, nChildren_return, nChildren_return1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XRaiseWindow
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XRaiseWindow
  (JNIEnv *env, jclass that, jint display, jint window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XRaiseWindow\n");
#endif
    return (jint) XRaiseWindow((Display *)display, window);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XReconfigureWMWindow
 * Signature: (IIIILorg/eclipse/swt/internal/motif/XWindowChanges;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XReconfigureWMWindow
  (JNIEnv *env, jclass that, jint display, jint window, jint screen, jint valueMask, jobject values)
{
	DECL_GLOB(pGlob)
	XWindowChanges xWindowChanges, *lpWindowChanges1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XReconfigureWMWindow\n");
#endif

    if (values) {
        lpWindowChanges1 = &xWindowChanges;
        cacheXwindowchangesFids(env, values, &PGLOB(XwindowchangesFc));
        getXwindowchangesFields(env, values, lpWindowChanges1, &PGLOB(XwindowchangesFc));
    }
    rc = (jint) XReconfigureWMWindow((Display *)display, window, screen, valueMask, lpWindowChanges1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XReconfigureWMWindow: call failed rc = %d\n", rc);
#endif

    if (values) {
        setXwindowchangesFields(env, values, lpWindowChanges1, &PGLOB(XwindowchangesFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XRectInRegion
 * Signature: (IIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XRectInRegion
  (JNIEnv *env, jclass that, jint region, jint x, jint y, jint width, jint height)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XRectInRegion\n");
#endif
	return (jint) XRectInRegion((Region)region, x, y, width, height);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetBackground
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetBackground
  (JNIEnv *env, jclass that, jint display, jint gc, jint background)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetBackground\n");
#endif
	XSetBackground((Display *)display, (GC)gc, background);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetClipMask
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetClipMask
  (JNIEnv *env, jclass that, jint display, jint gc, jint pixmap)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetClipMask\n");
#endif
	XSetClipMask((Display *)display, (GC)gc, pixmap);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetClipRectangles
 * Signature: (IIIILorg/eclipse/swt/internal/motif/XRectangle;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetClipRectangles
  (JNIEnv *env, jclass that, jint display, jint gc, jint clip_x_origin, jint clip_y_origin, jobject rectangle, jint n, jint ordering)
{
	DECL_GLOB(pGlob)
	XRectangle xRect, *lpxRect=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetClipRectangles\n");
#endif

    if (rectangle) {
        lpxRect = &xRect;
        cacheXrectangleFids(env, rectangle, &PGLOB(XrectangleFc));
        getXrectangleFields(env, rectangle, lpxRect, &PGLOB(XrectangleFc));
    }
    XSetClipRectangles((Display *)display, (GC)gc, clip_x_origin, clip_y_origin, lpxRect, n, ordering);
    if (rectangle) {
        setXrectangleFields(env, rectangle, lpxRect, &PGLOB(XrectangleFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetDashes
 * Signature: (III[BI)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetDashes
  (JNIEnv *env, jclass that, jint display, jint gc, jint dash_offset, jbyteArray dash_list, jint n)
{
    jbyte *dash_list1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetDashes\n");
#endif

    if (dash_list)
        dash_list1 = (*env)->GetByteArrayElements(env, dash_list, NULL);
    rc = (jint) XSetDashes((Display *)display, (GC)gc, dash_offset, (char *)dash_list1, n);
#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XSetDashes: call failed rc = %d\n", rc);
#endif
    if (dash_list)
        (*env)->ReleaseByteArrayElements(env, dash_list, dash_list1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetErrorHandler
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetErrorHandler
  (JNIEnv *env, jclass that, jint handler)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetErrorHandler\n");
#endif
    return (jint) XSetErrorHandler((XErrorHandler)handler);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetFillStyle
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetFillStyle
  (JNIEnv *env, jclass that, jint display, jint gc, jint fill_style)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetFillStyle\n");
#endif
    XSetFillStyle((Display*)display, (GC)gc, fill_style);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetFont
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetFont
  (JNIEnv *env, jclass that, jint display, jint gc, jint font)
{
    return (jint) XSetFont((Display *)display, (GC)gc, (Font)font);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetForeground
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetForeground
  (JNIEnv *env, jclass that, jint display, jint gc, jint foreground)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetForeground\n");
#endif
	XSetForeground((Display *)display, (GC)gc, foreground);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetFunction
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetFunction
  (JNIEnv *env, jclass that, jint display, jint gc, jint function)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetFunction\n");
#endif
	XSetFunction((Display *)display, (GC)gc, function);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetGraphicsExposures
 * Signature: (IIZ)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetGraphicsExposures
  (JNIEnv *env, jclass that, jint display, jint gc, jboolean graphics_exposures)
{
    XSetGraphicsExposures((Display *)display, (GC)gc, (Bool) graphics_exposures);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetInputFocus
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetInputFocus
  (JNIEnv *env, jclass that, jint display, jint window, jint revert, jint time)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetInputFocus\n");
#endif
	
    return (jint) XSetInputFocus((Display *)display, window, revert, time);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetLineAttributes
 * Signature: (IIIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetLineAttributes
  (JNIEnv *env, jclass that, jint display, jint gc, jint lineWidth, jint lineStyle, jint capStyle, jint joinStyle)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetLineAttributes\n");
#endif
    return (jint) XSetLineAttributes((Display *)display, (GC)gc, lineWidth, lineStyle, capStyle, joinStyle);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetRegion
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetRegion
  (JNIEnv *env, jclass that, jint display, jint gc, jint region)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetRegion\n");
#endif
	XSetRegion((Display *)display, (GC)gc, (Region)region);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetStipple
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetStipple
  (JNIEnv *env, jclass that, jint display, jint gc, jint pixmap)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetStipple\n");
#endif
    XSetStipple((Display*)display, (GC)gc, pixmap);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetSubwindowMode
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetSubwindowMode
  (JNIEnv *env, jclass that, jint display, jint gc, jint subwindow_mode)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetSubwindowMode\n");
#endif
    XSetSubwindowMode((Display*)display, (GC)gc, subwindow_mode);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetWindowBorderWidth
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetWindowBorderWidth
  (JNIEnv *env, jclass that, jint display, jint window, jint width)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetWindowBorderWidth\n");
#endif
    XSetWindowBorderWidth((Display *)display, window, width);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSubtractRegion
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSubtractRegion
  (JNIEnv *env, jclass that, jint sra, jint srb, jint da_return)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSubtractRegion\n");
#endif
    XSubtractRegion((Region)sra, (Region)srb, (Region)da_return);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSync
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSync
  (JNIEnv *env, jclass that, jint display, jboolean discard)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSync\n");
#endif
	XSync((Display *)display, (Bool)discard);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSynchronize
 * Signature: (IZ)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XSynchronize
  (JNIEnv *env, jclass that, jint display, jboolean onoff)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSynchronize\n");
#endif
    return (jint) XSynchronize((Display *)display, (Bool)onoff);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XUndefineCursor
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XUndefineCursor
  (JNIEnv *env, jclass that, jint display, jint window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XUndefineCursor\n");
#endif
	XUndefineCursor((Display *)display, window);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XUngrabPointer
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XUngrabPointer
  (JNIEnv *env, jclass that, jint display, jint time)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XUngrabPointer\n");
#endif
    return (jint) XUngrabPointer((Display *)display, time);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XUnionRectWithRegion
 * Signature: (Lorg/eclipse/swt/internal/motif/XRectangle;II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XUnionRectWithRegion
  (JNIEnv *env, jclass that, jobject rectangle, jint src_region, jint dest_region)
{
	DECL_GLOB(pGlob)
	XRectangle xRect, *lpxRect=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XUnionRectWithRegion\n");
#endif

    if (rectangle) {
        lpxRect = &xRect;
        cacheXrectangleFids(env, rectangle, &PGLOB(XrectangleFc));
        getXrectangleFields(env, rectangle, lpxRect, &PGLOB(XrectangleFc));
    }
    XUnionRectWithRegion(lpxRect, (Region)src_region, (Region)dest_region);
    if (rectangle) {
        setXrectangleFields(env, rectangle, lpxRect, &PGLOB(XrectangleFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XUnionRegion
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XUnionRegion
  (JNIEnv *env, jclass that, jint sra, jint srb, jint dr_return)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XUnionRegion\n");
#endif
	XUnionRegion((Region)sra, (Region)srb, (Region)dr_return);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XWhitePixel
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XWhitePixel
  (JNIEnv *env, jclass that, jint display, jint screenNum)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XWhitePixel\n");
#endif
    return (jint) XWhitePixel((Display *)display, screenNum);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XWithdrawWindow
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XWithdrawWindow
  (JNIEnv *env, jclass that, jint display, jint window, jint screen)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XWithdrawWindow\n");
#endif
    XWithdrawWindow((Display *)display, (Window)window, screen);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmAddWMProtocolCallback
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmAddWMProtocolCallback
  (JNIEnv *env, jclass that, jint shell, jint protocol, jint callback, jint closure)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmAddWMProtocolCallback\n");
#endif
    XmAddWMProtocolCallback((Widget)shell, (Atom)protocol, (XtCallbackProc)callback, (XtPointer)closure);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmChangeColor
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmChangeColor
  (JNIEnv *env, jclass that, jint widget, jint pixel)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmChangeColor\n");
#endif
    XmChangeColor((Widget)widget, pixel);
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmComboBoxAddItem
 * Signature: (IIIZ)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmComboBoxAddItem
  (JNIEnv *env, jclass that, jint combo, jint xmString, jint position, jboolean unique)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmComboBoxAddItem\n");
#endif
	XmComboBoxAddItem((Widget)combo, (XmString) xmString, position, unique);
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmComboBoxDeletePos
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmComboBoxDeletePos
  (JNIEnv *env, jclass that, jint combo, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmComboBoxDeletePos\n");
#endif
	XmComboBoxDeletePos((Widget)combo, position);
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmComboBoxSelectItem
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmComboBoxSelectItem
  (JNIEnv *env, jclass that, jint combo, jint xmString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmComboBoxSelectItem\n");
#endif
	XmComboBoxSelectItem((Widget)combo, (XmString) xmString);
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateArrowButton
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateArrowButton
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jint *name1=NULL, *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateArrowButton\n");
#endif

    if (name)
        name1 = (*env)->GetIntArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreateArrowButton((Widget)parent, (char *)name1, (ArgList)argList1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateArrowButton: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseIntArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateCascadeButtonGadget
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateCascadeButtonGadget
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateCascadeButtonGadget\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreateCascadeButtonGadget((Widget)parent, (String)name1, (ArgList)argList1, argcount);
    
#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateCascadeButtonGadget: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateComboBox
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateComboBox
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateComboBox\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    rc = (jint)XmCreateComboBox((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateComboBox: call failed rc = %d\n", rc);
#endif


    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateDialogShell
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateDialogShell
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateDialogShell\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreateDialogShell((Widget)parent, (String)name1, (ArgList)argList1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateDialogShell: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateDrawingArea
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateDrawingArea
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateDrawingArea\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreateDrawingArea((Widget)parent, (String)name1, (ArgList)argList1, argcount);
    
#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateDrawingArea: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateErrorDialog
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateErrorDialog
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateErrorDialog\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    rc = (jint)XmCreateErrorDialog((struct _WidgetRec*)parent, (String)name1, (ArgList)arglist1, argcount);
    
#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateErrorDialog: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateFileSelectionDialog
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateFileSelectionDialog
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateFileSelectionDialog\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    rc = (jint)XmCreateFileSelectionDialog((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateFileSelectionDialog: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateForm
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateForm
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateForm\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    rc = (jint)XmCreateForm((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateForm: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateFrame
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateFrame
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateFrame\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    rc = (jint)XmCreateFrame((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateFrame: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateInformationDialog
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateInformationDialog
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateInformationDialog\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);

    rc = (jint)XmCreateInformationDialog((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateInformationDialog: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateLabel
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateLabel
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateLabel\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    rc = (jint)XmCreateLabel((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateLabel: call failed rc = %d\n", rc);
#endif


    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateList
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateList
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jbyte *name1=NULL;
    jint *arglist1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateList\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);    
    rc = (jint) XmCreateList((Widget)parent, (String)name1, (ArgList)arglist1, argcount);
   
#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateList: call failed rc = %d\n", rc);
#endif
    
    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateMainWindow
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateMainWindow
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateMainWindow\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreateMainWindow((Widget)parent, (String)name1, (ArgList)argList1, argcount);
   
#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateMainWindow: call failed rc = %d\n", rc);
#endif
    
    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateMenuBar
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateMenuBar
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateMenuBar\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreateMenuBar((Widget)parent, (String)name1, (ArgList)argList1, argcount);
    
#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateMenuBar: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateMessageDialog
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateMessageDialog
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateMessageDialog\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    rc = (jint)XmCreateMessageDialog((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateMessageDialog: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreatePopupMenu
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreatePopupMenu
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreatePopupMenu\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreatePopupMenu((Widget)parent, (String)name1, (ArgList)argList1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreatePopupMenu: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreatePulldownMenu
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreatePulldownMenu\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreatePulldownMenu((Widget)parent, (String)name1, (ArgList)argList1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreatePulldownMenu: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreatePushButton
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreatePushButton
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1 = NULL;
    jint *argList1 = NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreatePushButton\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);

    rc = (jint) XmCreatePushButton((Widget)parent, (String)name1, (ArgList)argList1, argcount);
    
#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreatePushButton: call failed rc = %d\n", rc);
#endif

     if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreatePushButtonGadget
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreatePushButtonGadget
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreatePushButtonGadget\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreatePushButtonGadget((Widget)parent, (String)name1, (ArgList)argList1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreatePushButtonGadget: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateQuestionDialog
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateQuestionDialog
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateQuestionDialog\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    rc = (jint)XmCreateQuestionDialog((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateQuestionDialog: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateScale
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateScale
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
	jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateScale\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);

    rc = (jint) XmCreateScale((Widget)parent, (String)name1, (ArgList) arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateScale: call failed rc = %d\n", rc);
#endif


    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateScrollBar
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateScrollBar
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateScrollBar\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreateScrollBar((Widget)parent, (String)name1, (ArgList)argList1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateScrollBar: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateScrolledList
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateScrolledList
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateScrolledList\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    rc = (jint)XmCreateScrolledList((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateScrolledList: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateScrolledText
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateScrolledText
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateScrolledText\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    rc = (jint)XmCreateScrolledText((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateScrolledText: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateSeparator
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateSeparator
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateSeparator\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreateSeparator((Widget)parent, (String)name1, (ArgList)argList1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateSeparator: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}


/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateSeparatorGadget
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateSeparatorGadget
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateSeparatorGadget\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreateSeparatorGadget((Widget)parent, (String)name1, (ArgList)argList1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateSeparatorGadget: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateTextField
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateTextField
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateTextField\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    rc = (jint)XmCreateTextField((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateTextField: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateToggleButton
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateToggleButton
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateToggleButton\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreateToggleButton((Widget)parent, (String)name1, (ArgList)argList1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateToggleButton: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateToggleButtonGadget
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateToggleButtonGadget
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateToggleButtonGadget\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreateToggleButtonGadget((Widget)parent, (String)name1, (ArgList)argList1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateToggleButtonGadget: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateWarningDialog
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateWarningDialog
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateWarningDialog\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    rc = (jint)XmCreateWarningDialog((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateWarningDialog: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmDragCancel
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmDragCancel
  (JNIEnv *env, jclass that, jint dragcontext)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmDragCancel\n");
#endif

    XmDragCancel((Widget)dragcontext);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmDragStart
 * Signature: (II[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmDragStart
  (JNIEnv *env, jclass that, jint widget, jobject event, jintArray arglist, jint argcount)
{
	DECL_GLOB(pGlob)
    XEvent xEvent, *lpxEvent=NULL;
    jint *arglist1;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmDragStart\n");
#endif

    if (event) {
        lpxEvent = &xEvent;
        cacheXanyeventFids(env, event, &PGLOB(XanyeventFc));
        getXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    }
    if (arglist) {
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    }
    rc = (jint) XmDragStart((Widget)widget, (XEvent *)lpxEvent, (ArgList)arglist1, (Cardinal)argcount);
    if (event) {
        setXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    }
    if (arglist) {
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmDropSiteRegister
 * Signature: (I[II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmDropSiteRegister
  (JNIEnv *env, jclass that, jint widget, jintArray arglist, jint argcount)
{
    jint *arglist1;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmDropSiteRegister\n");
#endif

    if (arglist) {
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    }
    XmDropSiteRegister((Widget)widget, (ArgList)arglist1, (Cardinal)argcount);
    if (arglist) {
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmDropSiteRetrieve
 * Signature: (I[II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmDropSiteRetrieve
  (JNIEnv *env, jclass that, jint widget, jintArray arglist, jint argcount)
{
    jint *arglist1;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmDropSiteRetrieve\n");
#endif

    if (arglist) {
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    }
    XmDropSiteRetrieve((Widget)widget, (ArgList)arglist1, (Cardinal)argcount);
    if (arglist) {
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmDropSiteUnregister
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmDropSiteUnregister
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmDropSiteUnregister\n");
#endif
    XmDropSiteUnregister((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmDropSiteUpdate
 * Signature: (I[II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmDropSiteUpdate
  (JNIEnv *env, jclass that, jint widget, jintArray arglist, jint argcount)
{
    jint *arglist1;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmDropSiteUpdate\n");
#endif

    if (arglist) {
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    }
    XmDropSiteUpdate((Widget)widget, (ArgList)arglist1, (Cardinal)argcount);
    if (arglist) {
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmDropTransferAdd
 * Signature: (I[II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmDropTransferAdd
  (JNIEnv *env, jclass that, jint drop_transfer, jintArray transfers, jint num_transfers)
{
    jint *transfers1;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmDropTransferAdd\n");
#endif

    if (transfers) {
        transfers1 = (*env)->GetIntArrayElements(env, transfers, NULL);
    }
    XmDropTransferAdd((Widget)drop_transfer, (XmDropTransferEntryRec *)transfers1, (Cardinal)num_transfers);
    if (transfers) {
        (*env)->ReleaseIntArrayElements(env, transfers, transfers1, 0);
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmDropTransferStart
 * Signature: (I[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmDropTransferStart
  (JNIEnv *env, jclass that, jint widget, jintArray arglist, jint argcount)
{
    jint *arglist1;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmDropTransferStart\n");
#endif

    if (arglist) {
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);
    }
    rc = (jint) XmDropTransferStart((Widget)widget, (ArgList)arglist1, (Cardinal)argcount);
    if (arglist) {
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmFileSelectionBoxGetChild
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmFileSelectionBoxGetChild
  (JNIEnv *env, jclass that, jint widget, jint child)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmFileSelectionBoxGetChild\n");
#endif
	return (jint) XmFileSelectionBoxGetChild((Widget)widget, child);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmFontListAppendEntry
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmFontListAppendEntry
  (JNIEnv *env, jclass that, jint oldlist,  jint entry)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmFontListAppendEntry\n");
#endif
	return (jint) XmFontListAppendEntry((XmFontList)oldlist, (XmFontListEntry)entry);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmFontListEntryFree
 * Signature: ([I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmFontListEntryFree
  (JNIEnv *env, jclass that, jintArray entry)
{
    jint *entry1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmFontListEntryFree\n");
#endif

    if (entry)
        entry1 = (*env)->GetIntArrayElements(env, entry, NULL);
    XmFontListEntryFree((XmFontListEntry *)entry1);
    if (entry)
        (*env)->ReleaseIntArrayElements(env, entry, entry1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmFontListEntryGetFont
 * Signature: (I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmFontListEntryGetFont
  (JNIEnv *env, jclass that, jint entry, jintArray type_return)
{
    jint *type_return1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmFontListEntryGetFont\n");
#endif

    if (type_return)
        type_return1 = (*env)->GetIntArrayElements(env, type_return, NULL);
    rc = (jint) XmFontListEntryGetFont((XmFontListEntry)entry, (XmFontType *)type_return1);
    if (type_return)
        (*env)->ReleaseIntArrayElements(env, type_return, type_return1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmFontListEntryCreate
 * Signature: ([BII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmFontListEntryCreate
  (JNIEnv *env, jclass that, jbyteArray tag, jint type, int font)
{
    char *tag1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmFontListEntryCreate\n");
#endif

    if (tag)
        tag1 = (char *)(*env)->GetByteArrayElements(env, tag, NULL);
    rc = (jint)XmFontListEntryCreate(tag1, (XmFontType)type, (XtPointer)font);
    if (tag)
        (*env)->ReleaseByteArrayElements(env, tag, (jbyte *)tag1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XLoadQueryFont
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XLoadQueryFont
  (JNIEnv *env, jclass that, jint display, jbyteArray fontName)
{
    char *fontName1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XLoadQueryFont\n");
#endif

    if (fontName)
        fontName1 = (char *)(*env)->GetByteArrayElements(env, fontName, NULL);
    rc = (jint)XLoadQueryFont((Display *)display, fontName1);
    if (fontName)
        (*env)->ReleaseByteArrayElements(env, fontName, (jbyte *)fontName1, 0);
    
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmFontListEntryLoad
 * Signature: (I[BI[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmFontListEntryLoad
  (JNIEnv *env, jclass that, jint display, jbyteArray fontName, jint type, jbyteArray tag)
{
    char *fontName1=NULL, *tag1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmFontListEntryLoad\n");
#endif

    if (fontName)
        fontName1 = (char *)(*env)->GetByteArrayElements(env, fontName, NULL);
    if (tag)
        tag1 = (char *)(*env)->GetByteArrayElements(env, tag, NULL);
    rc = (jint)XmFontListEntryLoad((Display *)display, fontName1, type, tag1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmFontListEntryLoad: call failed rc = %d\n", rc);
#endif

    if (fontName)
        (*env)->ReleaseByteArrayElements(env, fontName, (jbyte *)fontName1, 0);
    if (tag)
        (*env)->ReleaseByteArrayElements(env, tag, (jbyte *)tag1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmFontListFree
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmFontListFree
  (JNIEnv *env, jclass that, jint list)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmFontListFree\n");
#endif
	XmFontListFree((XmFontList)list);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmFontListFreeFontContext
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmFontListFreeFontContext
  (JNIEnv *env, jclass that, jint context)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmFontListFreeFontContext\n");
#endif
    XmFontListFreeFontContext((XmFontContext)context);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmFontListInitFontContext
 * Signature: ([II)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmFontListInitFontContext
  (JNIEnv *env, jclass that, jintArray context, jint fontlist)
{
    jint *context1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmFontListInitFontContext\n");
#endif

    if (context)
        context1 = (*env)->GetIntArrayElements(env, context, NULL);
    rc = XmFontListInitFontContext((XmFontContext *)context1, (XmFontList)fontlist);
    if (context)
        (*env)->ReleaseIntArrayElements(env, context, context1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmFontListNextEntry
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmFontListNextEntry
  (JNIEnv *env, jclass that, jint context)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmFontListNextEntry\n");
#endif
    return (jint) XmFontListNextEntry((XmFontContext) context);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmFontListCopy
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmFontListCopy
  (JNIEnv *env, jclass that, jint fontlist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmFontListCopy\n");
#endif
    return (jint) XmFontListCopy((XmFontList) fontlist);
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmGetAtomName
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmGetAtomName
  (JNIEnv *env, jclass that, jint display, jint atom)
{
    return (jint) XmGetAtomName((Display *)display, (Atom)atom);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmGetFocusWidget
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmGetFocusWidget
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmGetFocusWidget\n");
#endif
    return (jint) XmGetFocusWidget((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmGetXmDisplay
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmGetXmDisplay
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
        fprintf(stderr, "XmGetXmDisplay\n");
#endif
    return (jint) XmGetXmDisplay((Display *)display);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmGetDragContext
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmGetDragContext
  (JNIEnv *env, jclass that, jint widget, jint timestamp)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmGetDragContext\n");
#endif
    return (jint) XmGetDragContext((Widget)widget, (Time)timestamp);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmImMbLookupString
 * Signature: (ILorg/eclipse/swt/internal/motif/XKeyEvent;[BI[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmImMbLookupString
  (JNIEnv *env, jclass that, jint widget, jobject keyEvent, jbyteArray string, jint size, jintArray keysym, jintArray status)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
	jint *keysym1=NULL, *status1=NULL;
    jbyte *string1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmImMbLookupString\n");
#endif

    if (keyEvent) {
        lpxEvent = &xEvent;
        cacheXkeyeventFids(env, keyEvent, &PGLOB(XkeyeventFc));
        getXkeyeventFields(env, keyEvent, lpxEvent, &PGLOB(XkeyeventFc));
    }
    if (string)
        string1 = (*env)->GetByteArrayElements(env, string, NULL);
    if (keysym)
        keysym1 = (*env)->GetIntArrayElements(env, keysym, NULL);
    if (status)
        status1 = (*env)->GetIntArrayElements(env, status, NULL);

    rc = (jint)XmImMbLookupString((Widget)widget, (XKeyPressedEvent *)lpxEvent, (char *)string1, size, (KeySym *)keysym1, (int *)status1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmImMbLookupString: call failed rc = %d\n", rc);
#endif

    if (keyEvent) {
        setXkeyeventFields(env, keyEvent, lpxEvent, &PGLOB(XkeyeventFc));
    }
    if (string)
        (*env)->ReleaseByteArrayElements(env, string, string1, 0);
    if (keysym)
        keysym1 = (*env)->GetIntArrayElements(env, keysym, NULL);
    if (status)
        (*env)->ReleaseIntArrayElements(env, status, status1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmInternAtom
 * Signature: (I[BZ)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmInternAtom
  (JNIEnv *env, jclass that, jint display, jbyteArray name, jboolean only_if_exists)
{
    jbyte *name1;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmInternAtom\n");
#endif
    
    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    rc = (jint) XmInternAtom((Display *)display, (String)name1, only_if_exists);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmInternAtom: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListAddItemUnselected
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListAddItemUnselected
  (JNIEnv *env, jclass that, jint list, jint xmString, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListAddItemUnselected\n");
#endif
	XmListAddItemUnselected((Widget)list, (XmString)xmString, position);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListDeleteAllItems
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListDeleteAllItems
  (JNIEnv *env, jclass that, jint list)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListDeleteAllItems\n");
#endif
	XmListDeleteAllItems((Widget)list);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListDeleteItemsPos
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListDeleteItemsPos
  (JNIEnv *env, jclass that, jint list, jint item_count, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListDeleteItemsPos\n");
#endif
	XmListDeleteItemsPos((Widget)list, item_count, position);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListDeletePos
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListDeletePos
  (JNIEnv *env, jclass that, jint list, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListDeletePos\n");
#endif
	XmListDeletePos((Widget)list, position);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListDeletePositions
 * Signature: (I[II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListDeletePositions
  (JNIEnv *env, jclass that, jint list, jintArray position_list, jint position_count)
{
    jint *position_list1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListDeletePositions\n");
#endif

    if (position_list)
        position_list1 = (*env)->GetIntArrayElements(env, position_list, NULL);
    XmListDeletePositions((Widget)list, (int *)position_list1, position_count);
    if (position_list)
        (*env)->ReleaseIntArrayElements(env, position_list, position_list1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListDeselectAllItems
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListDeselectAllItems
  (JNIEnv *env, jclass that, jint list)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListDeselectAllItems\n");
#endif
	XmListDeselectAllItems((Widget)list);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListDeselectPos
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListDeselectPos
  (JNIEnv *env, jclass that, jint list, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListDeselectPos\n");
#endif
	XmListDeselectPos((Widget)list, position);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListGetKbdItemPos
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListGetKbdItemPos
  (JNIEnv *env, jclass that, jint list)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListGetKbdItemPos\n");
#endif
	return (jint)XmListGetKbdItemPos((Widget)list);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListGetSelectedPos
 * Signature: (I[I[I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListGetSelectedPos
  (JNIEnv *env, jclass that, jint list, jintArray positions, jintArray count)
{
    jint *positions1=NULL, *count1=NULL;
    jboolean rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListGetSelectedPos\n");
#endif

    if (positions)
        positions1 = (*env)->GetIntArrayElements(env, positions, NULL);
    if (count)
        count1 = (*env)->GetIntArrayElements(env, count, NULL);
    rc = (jboolean)XmListGetSelectedPos((Widget)list, (int **)positions1, (int *)count1);

#ifdef PRINT_FAILED_RCODES
    if (rc != True && rc != False)
        fprintf(stderr, "XmListGetSelectedPos: call failed rc = %d\n", rc);
#endif

    if (positions)
        (*env)->ReleaseIntArrayElements(env, positions, positions1, 0);
    if (count)
        (*env)->ReleaseIntArrayElements(env, count, count1, 0);
    return rc;	
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListItemPos
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListItemPos
  (JNIEnv *env, jclass that, jint list, jint xmString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListItemPos\n");
#endif
	return (jint)XmListItemPos((Widget)list, (XmString)xmString);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListPosSelected
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListPosSelected
  (JNIEnv *env, jclass that, jint list, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListPosSelected\n");
#endif
	return (jboolean)XmListPosSelected((Widget)list, position);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListReplaceItemsPosUnselected
 * Signature: (I[III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListReplaceItemsPosUnselected
  (JNIEnv *env, jclass that, jint list, jintArray newItems, jint item_count, jint position)
{
    jint *newItems1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListReplaceItemsPosUnselected\n");
#endif

    if (newItems)
        newItems1 = (*env)->GetIntArrayElements(env, newItems, NULL);
    XmListReplaceItemsPosUnselected((Widget)list, (XmString *)newItems1, item_count, position);
    if (newItems)
        (*env)->ReleaseIntArrayElements(env, newItems, newItems1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListSelectPos
 * Signature: (IIZ)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListSelectPos
  (JNIEnv *env, jclass that, jint list, jint position, jboolean notify)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListSelectPos\n");
#endif
	XmListSelectPos((Widget)list, position, notify);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListSetKbdItemPos
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListSetKbdItemPos
  (JNIEnv *env, jclass that, jint list, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListSetKbdItemPos\n");
#endif
	return (jboolean)XmListSetKbdItemPos((Widget)list, position);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListSetPos
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListSetPos
  (JNIEnv *env, jclass that, jint list, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListSetPos\n");
#endif
	XmListSetPos((Widget)list, position);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListUpdateSelectedList
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListUpdateSelectedList
  (JNIEnv *env, jclass that, jint list)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListUpdateSelectedList\n");
#endif
	XmListUpdateSelectedList((Widget)list);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmMainWindowSetAreas
 * Signature: (IIIIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmMainWindowSetAreas
  (JNIEnv *env, jclass that, jint widget, jint menu, jint command, jint hscroll, jint vscroll, jint wregion)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmMainWindowSetAreas\n");
#endif
    XmMainWindowSetAreas((Widget)widget, (Widget)menu, (Widget)command, (Widget)hscroll, (Widget)vscroll, (Widget)wregion);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmMenuShellWidgetClass
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmMenuShellWidgetClass
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmMenuShellWidgetClass\n");
#endif
	return (jint)xmMenuShellWidgetClass;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmMessageBoxGetChild
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmMessageBoxGetChild
  (JNIEnv *env, jclass that, jint widget, jint child)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmMessageBoxGetChild\n");
#endif
	return (jint)XmMessageBoxGetChild((Widget)widget, child);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmProcessTraversal
 * Signature: (II)
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmProcessTraversal
  (JNIEnv *env, jclass that, jint widget, jint dir)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmProcessTraversal\n");
#endif
    return (jboolean) XmProcessTraversal((Widget)widget, dir);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmPushButtonWidgetClass
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmPushButtonWidgetClass
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmPushButtonWidgetClass\n");
#endif
	return (jint)xmPushButtonWidgetClass;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringCompare
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringCompare
  (JNIEnv *env, jclass that, jint xmString1, jint xmString2)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringCompare\n");
#endif
    return (jboolean) XmStringCompare((XmString)xmString1, (XmString)xmString2);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringCreate
 * Signature: ([B[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringCreate
  (JNIEnv *env, jclass that, jbyteArray string, jbyteArray charset)
{
    jbyte *string1=NULL, *charset1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringCreate\n");
#endif

    if (string)
        string1 = (*env)->GetByteArrayElements(env, string, NULL);
    if (charset)
        charset1 = (*env)->GetByteArrayElements(env, charset, NULL);
    rc = (jint) XmStringCreate((char *)string1, (char *)charset1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmStringCreate: call failed rc = %d\n", rc);
#endif

    if (string)
        (*env)->ReleaseByteArrayElements(env, string, string1, 0);
    if (charset)
        (*env)->ReleaseByteArrayElements(env, charset, charset1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringCreateLocalized
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringCreateLocalized
  (JNIEnv *env, jclass that, jbyteArray string)
{
    jbyte *string1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringCreateLocalized\n");
#endif

    if (string)
        string1 = (*env)->GetByteArrayElements(env, string, NULL);
    rc = (jint)XmStringCreateLocalized((char *)string1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmStringCreateLocalized: call failed rc = %d\n", rc);
#endif

    if (string)
        (*env)->ReleaseByteArrayElements(env, string, string1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringCreateLtoR
 * Signature: ([B[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringCreateLtoR
  (JNIEnv *env, jclass that, jbyteArray string, jbyteArray charset)
{
    jbyte *string1=NULL, *charset1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringCreateLtoR\n");
#endif

    if (string)
        string1 = (*env)->GetByteArrayElements(env, string, NULL);
    if (charset)
        charset1 = (*env)->GetByteArrayElements(env, charset, NULL);    
    rc = (jint) XmStringCreateLtoR((char *)string1, (char *)charset1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmStringCreateLtoR: call failed rc = %d\n", rc);
#endif

    if (string)
        (*env)->ReleaseByteArrayElements(env, string, string1, 0);
    if (charset)
        (*env)->ReleaseByteArrayElements(env, charset, charset1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringDraw
 * Signature: (IIIIIIIIIILorg/eclipse/swt/internal/motif/XRectangle;)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringDraw
  (JNIEnv *env, jclass that, jint display, jint window, jint fontlist, jint xmString, jint gc, jint x, jint y, jint width, jint align, jint lay_dir, jobject clip)
{
	DECL_GLOB(pGlob)
	XRectangle xRect, *lpxRect=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "\n");
#endif

    if (clip) {
        lpxRect = &xRect;
        cacheXrectangleFids(env, clip, &PGLOB(XrectangleFc));
        getXrectangleFields(env, clip, lpxRect, &PGLOB(XrectangleFc));
    }
    XmStringDraw((Display *)display, (Window)window, (XmFontList)fontlist, (XmString)xmString, (GC)gc, x, y, width, align, lay_dir, lpxRect);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringDrawImage
 * Signature: (IIIIIIIIIILorg/eclipse/swt/internal/motif/XRectangle;)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringDrawImage
  (JNIEnv *env, jclass that, jint display, jint window, jint fontlist, jint xmString, jint gc, jint x, jint y, jint width, jint align, jint lay_dir, jobject clip)
{
	DECL_GLOB(pGlob)
	XRectangle xRect, *lpxRect=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringDrawImage\n");
#endif

    if (clip) {
        lpxRect = &xRect;
        cacheXrectangleFids(env, clip, &PGLOB(XrectangleFc));
        getXrectangleFields(env, clip, lpxRect, &PGLOB(XrectangleFc));
    }
    XmStringDrawImage((Display *)display, window, (XmFontList)fontlist, (XmString)xmString, (GC)gc, x, y, width, align, lay_dir, lpxRect);
    if (clip) {
        setXrectangleFields(env, clip, lpxRect, &PGLOB(XrectangleFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringEmpty
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringEmpty
  (JNIEnv *env, jclass that, jint s1)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringEmpty\n");
#endif
    return (jboolean) XmStringEmpty((XmString)s1);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringFree
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringFree
  (JNIEnv *env, jclass that, jint xmString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringFree\n");
#endif
    XmStringFree((XmString)xmString);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringGetLtoR
 * Signature: (I[B[I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringGetLtoR
  (JNIEnv *env, jclass that, jint xmString, jbyteArray charset, jintArray text)
{
    jbyte *charset1=NULL;
    jint *text1=NULL;
    jboolean rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringGetLtoR\n");
#endif

    if (charset)
        charset1 = (*env)->GetByteArrayElements(env, charset, NULL);
    if (text)
        text1 = (*env)->GetIntArrayElements(env, text, NULL);    

    rc = (jboolean) XmStringGetLtoR((XmString)xmString, (char *)charset1, (char **)text1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmStringGetLtoR: call failed rc = %d\n", rc);
#endif

    if (charset)
        (*env)->ReleaseByteArrayElements(env, charset, charset1, 0);
    if (text)
        (*env)->ReleaseIntArrayElements(env, text, text1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringHeight
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringHeight
  (JNIEnv *env, jclass that, jint fontlist, jint xmString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringHeight\n");
#endif
	return (jint) XmStringHeight((XmFontList)fontlist, (XmString)xmString);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringWidth
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringWidth
  (JNIEnv *env, jclass that, jint fontlist, jint xmString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringWidth\n");
#endif
	return (jint) XmStringWidth((XmFontList)fontlist, (XmString)xmString);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextClearSelection
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextClearSelection
  (JNIEnv *env, jclass that, jint widget, jint time)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextClearSelection\n");
#endif
	XmTextClearSelection((Widget)widget, time);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextCopy
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextCopy
  (JNIEnv *env, jclass that, jint widget, jint time)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextCopy\n");
#endif
	return (jboolean)XmTextCopy((Widget)widget, time);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextCut
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextCut
  (JNIEnv *env, jclass that, jint widget, jint time)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextCut\n");
#endif
	return (jboolean)XmTextCut((Widget)widget, time);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextDisableRedisplay
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextDisableRedisplay
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextDisableRedisplay\n");
#endif
	XmTextDisableRedisplay((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextEnableRedisplay
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextEnableRedisplay
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextEnableRedisplay\n");
#endif
	XmTextEnableRedisplay((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextGetInsertionPosition
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextGetInsertionPosition
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextGetInsertionPosition\n");
#endif
	return (jint) XmTextGetInsertionPosition((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextGetLastPosition
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextGetLastPosition
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextGetLastPosition\n");
#endif
	return (jint) XmTextGetLastPosition((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextGetMaxLength
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextGetMaxLength
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextGetMaxLength\n");
#endif
	return (jint) XmTextGetMaxLength((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextGetSelection
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextGetSelection
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextGetSelection\n");
#endif
	return (jint) XmTextGetSelection((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextGetSelectionPosition
 * Signature: (I[I[I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextGetSelectionPosition
  (JNIEnv *env, jclass that, jint widget, jintArray left, jintArray right)
{
    jint *left1=NULL,*right1=NULL;
    jboolean rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextGetSelectionPosition\n");
#endif

    if (left)
        left1 = (*env)->GetIntArrayElements(env, left, NULL);
    if (right)
        right1 = (*env)->GetIntArrayElements(env, right, NULL);
    rc = (jboolean) XmTextGetSelectionPosition((Widget)widget, (XmTextPosition *)left1, (XmTextPosition *)right1);

#ifdef PRINT_FAILED_RCODES
    if (rc != True && rc != False)
        fprintf(stderr, "XmTextGetSelectionPosition: call failed rc = %d\n", rc);
#endif

    if (left)
        (*env)->ReleaseIntArrayElements(env, left, left1, 0);
    if (right)
        (*env)->ReleaseIntArrayElements(env, right, right1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextGetString
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextGetString
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextGetString\n");
#endif
	return (jint) XmTextGetString((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextGetSubstring
 * Signature: (IIII[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextGetSubstring
  (JNIEnv *env, jclass that, jint widget, jint start, jint num_chars, jint buffer_size, jbyteArray buffer)
{
    jbyte *buffer1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextGetSubstring\n");
#endif

    if (buffer)
        buffer1 = (*env)->GetByteArrayElements(env, buffer, NULL);
    rc = (jint)XmTextGetSubstring((Widget)widget, start, num_chars, buffer_size, (char *)buffer1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmTextGetSubstring: call failed rc = %d\n", rc);
#endif

    if (buffer)
        (*env)->ReleaseByteArrayElements(env, buffer, buffer1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextGetSubstringWcs
 * Signature: (IIII[C)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextGetSubstringWcs
  (JNIEnv *env, jclass that, jint widget, jint start, jint num_chars, jint buffer_size, jcharArray buffer)
{
   jchar *buffer1=NULL;
   jint rc;
#ifdef DEBUG_CALL_PRINTS
        fprintf(stderr, "XmTextGetSubstringWcs\n");
#endif

    if (buffer)
        buffer1 = (*env)->GetCharArrayElements(env, buffer, NULL);

   rc = (jint)XmTextGetSubstringWcs((Widget) widget, (XmTextPosition) start, num_chars, buffer_size, (wchar_t *) buffer1);

    if (buffer)
        (*env)->ReleaseCharArrayElements(env, buffer, buffer1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextInsert
 * Signature: (II[B)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextInsert
  (JNIEnv *env, jclass that, jint widget, jint position, jbyteArray value)
{
    jbyte *value1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextInsert\n");
#endif

    if (value)
        value1 = (*env)->GetByteArrayElements(env, value, NULL);
    XmTextInsert((Widget)widget, position, (char *)value1);
    if (value)
        (*env)->ReleaseByteArrayElements(env, value, value1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextPaste
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextPaste
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextPaste\n");
#endif
	(jboolean) XmTextPaste((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextFieldPaste
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextFieldPaste
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextFieldPaste\n");
#endif
	(jboolean) XmTextFieldPaste((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextRemove
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextRemove
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextRemove\n");
#endif
	return (jboolean) XmTextRemove((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextPosToXY
 * Signature: (II[S[S)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextPosToXY
  (JNIEnv *env, jclass that, jint widget, jint position, jshortArray x, jshortArray y)
{
    jshort *x1=NULL, *y1=NULL;
    jboolean rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextPosToXY\n");
#endif

    if (x)
        x1 = (*env)->GetShortArrayElements(env, x, NULL);
    if (y)
        y1 = (*env)->GetShortArrayElements(env, y, NULL);
    rc = (jboolean) XmTextPosToXY((Widget)widget, (XmTextPosition)position, (Position *)x1, (Position *)y1);
    if (y)
        (*env)->ReleaseShortArrayElements(env, y, y1, 0);
    if (x)
        (*env)->ReleaseShortArrayElements(env, x, x1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextReplace
 * Signature: (III[B)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextReplace
  (JNIEnv *env, jclass that, jint widget, jint from_pos, jint to_pos, jbyteArray value)
{
    jbyte *value1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextReplace\n");
#endif

    if (value)
        value1 = (*env)->GetByteArrayElements(env, value, NULL);

    XmTextReplace((Widget)widget, from_pos, to_pos, (char *)value1);

    if (value)
        (*env)->ReleaseByteArrayElements(env, value, value1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextScroll
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextScroll
  (JNIEnv *env, jclass that, jint widget, jint lines)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextScroll\n");
#endif
	XmTextScroll((Widget)widget, lines);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextSetEditable
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextSetEditable
  (JNIEnv *env, jclass that, jint widget, jboolean editable)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextSetEditable\n");
#endif
	XmTextSetEditable((Widget)widget, editable);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextSetHighlight
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextSetHighlight
  (JNIEnv *env, jclass that, jint widget, jint left, jint right, jint mode)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextSetHighlight\n");
#endif
	XmTextSetHighlight((Widget)widget, left, right, mode);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextSetInsertionPosition
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextSetInsertionPosition
  (JNIEnv *env, jclass that, jint widget, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextSetInsertionPosition\n");
#endif
	XmTextSetInsertionPosition((Widget)widget, position);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextSetMaxLength
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextSetMaxLength
  (JNIEnv *env, jclass that, jint widget, jint max_length)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextSetMaxLength\n");
#endif
	XmTextSetMaxLength((Widget)widget, max_length);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextSetSelection
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextSetSelection
  (JNIEnv *env, jclass that, jint widget, jint first, jint last, jint time)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextSetSelection\n");
#endif
	XmTextSetSelection((Widget)widget, first, last, time);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextSetString
 * Signature: (I[B)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextSetString
  (JNIEnv *env, jclass that, jint widget, jbyteArray value)
{
    jbyte *value1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextSetString\n");
#endif

    if (value)
        value1 = (*env)->GetByteArrayElements(env, value, NULL);
    XmTextSetString((Widget)widget, (char *)value1);
    if (value)
        (*env)->ReleaseByteArrayElements(env, value, value1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextShowPosition
  (JNIEnv *env, jclass that, jint widget, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextShowPosition\n");
#endif
	XmTextShowPosition((Widget)widget, position);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmUpdateDisplay
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmUpdateDisplay
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmUpdateDisplay\n");
#endif
    XmUpdateDisplay((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmWidgetGetDisplayRect
 * Signature: (ILorg/eclipse/swt/internal/motif/XRectangle;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmWidgetGetDisplayRect
  (JNIEnv *env, jclass that, jint region, jobject rectangle)
{
	DECL_GLOB(pGlob)
	XRectangle xRect, *lpxRect=NULL;
    jboolean rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmWidgetGetDisplayRect\n");
#endif

    if (rectangle) {
        lpxRect = &xRect;
        cacheXrectangleFids(env, rectangle, &PGLOB(XrectangleFc));
        getXrectangleFields(env, rectangle, lpxRect, &PGLOB(XrectangleFc));
    }
    rc = (jboolean) XmWidgetGetDisplayRect((Widget)region, (XRectangle *)lpxRect);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmWidgetGetDisplayRect: call failed rc = %d\n", rc);
#endif

    if (rectangle) {
        setXrectangleFields(env, rectangle, lpxRect, &PGLOB(XrectangleFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmbLookupString
 * Signature: (ILorg/eclipse/swt/internal/motif/XInputEvent;[BI[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmbLookupString
  (JNIEnv *env, jclass that, jint widget, jobject event, jbyteArray string, jint size, jintArray keysym, jintArray status)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
    jint *keysym1=NULL, *status1=NULL;
    jbyte *string1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmbLookupString\n");
#endif

    if (event) {
        lpxEvent = &xEvent;
        cacheXkeyeventFids(env, event, &PGLOB(XkeyeventFc));
        getXkeyeventFields(env, event, lpxEvent, &PGLOB(XkeyeventFc));
    }
    if (string)
        string1 = (*env)->GetByteArrayElements(env, string, NULL);
    if (keysym)
        keysym1 = (*env)->GetIntArrayElements(env, keysym, NULL);
    if (status)
        status1 = (*env)->GetIntArrayElements(env, status, NULL);

    rc = (jint)XmImMbLookupString((Widget)widget, (XKeyPressedEvent *)lpxEvent, (char *)string1, size, (KeySym *)keysym1, (int *)status1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmImMbLookupString: call failed rc = %d\n", rc);
#endif

    if (event) {
        setXkeyeventFields(env, event, lpxEvent, &PGLOB(XkeyeventFc));
    }
    if (string)
        (*env)->ReleaseByteArrayElements(env, string, string1, 0);
    if (keysym)
        keysym1 = (*env)->GetIntArrayElements(env, keysym, NULL);
    if (status)
        (*env)->ReleaseIntArrayElements(env, status, status1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtAddCallback
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtAddCallback
  (JNIEnv *env, jclass that, jint widget, jint callback_name, jint callback, jint client_data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtAddCallback\n");
#endif
    XtAddCallback((Widget)widget, (String)callback_name, (XtCallbackProc)callback, (XtPointer)client_data);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtAddEventHandler
 * Signature: (IIZII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtAddEventHandler
  (JNIEnv *env, jclass that, jint widget, jint event_mask, jboolean nonmaskable, jint proc, jint client_data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtAddEventHandler\n");
#endif
    XtAddEventHandler((Widget)widget, event_mask, nonmaskable, (XtEventHandler)proc, (XtPointer)client_data);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtAddExposureToRegion
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtAddExposureToRegion
  (JNIEnv *env, jclass that, jint event, jint region)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtAddExposureToRegion\n");
#endif
	XtAddExposureToRegion((XEvent *)event, (Region)region);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtAppAddTimeOut
 * Signature: (IIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtAppAddTimeOut
  (JNIEnv *env, jclass that, jint applicationContext, jint interval, jint procedure, jint clientData)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtAppAddTimeOut\n");
#endif
    return (jint) XtAppAddTimeOut((XtAppContext)applicationContext, interval, (XtTimerCallbackProc)procedure, (XtPointer)clientData);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtAppCreateShell
 * Signature: ([B[BII[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtAppCreateShell
  (JNIEnv *env, jclass that, jbyteArray appName, jbyteArray appClass, jint widgetClass,
                jint display, jintArray argList, jint argCount)
{
    jbyte *appName1 = NULL;
    jbyte *appClass1 = NULL;
    jint *argList1 = NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtAppCreateShell\n");
#endif

    if (appName)
        appName1 = (*env)->GetByteArrayElements(env, appName, NULL);
    if (appClass)
        appClass1 = (*env)->GetByteArrayElements(env, appClass, NULL);
    if (argList)    
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);

/*    fprintf(stderr, "XtAppCreateShell: appName1=%d appClass1=%d widgetClass=%d display=%d arglist1=%d argCount=%d\n",
                                appName1, appClass1, widgetClass, display, argList1, argCount);
*/    
    rc = (jint) XtAppCreateShell((String)appName1, (String)appClass1, (WidgetClass)widgetClass, (Display *)display, (ArgList)argList1, argCount);

/* fprintf(stderr, "After XtAppCreateShell: rc=%d\n", rc); */
#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XtAppCreateShell: call failed rc = %d\n", rc);
#endif

    if (appName)
        (*env)->ReleaseByteArrayElements(env, appName, appName1, 0);
    if (appClass)
        (*env)->ReleaseByteArrayElements(env, appClass, appClass1, 0);
    if (argList)    
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtAppNextEvent
 * Signature: (ILorg/eclipse/swt/internal/motif/XAnyEvent;)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtAppNextEvent
  (JNIEnv *env, jclass that, jint appContext, jobject event)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtAppNextEvent\n");
#endif

    if (event) {
        lpxEvent = &xEvent;
        cacheXanyeventFids(env, event, &PGLOB(XanyeventFc));
        getXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    }
    XtAppNextEvent((XtAppContext)appContext, lpxEvent);

#ifdef EVENT_TRACE
    fprintf(stderr, "type = %d, serial = %d, send_event = %d, display = %x, window = %x \n",
     xEvent.type, xEvent.xany.serial, xEvent.xany.send_event, xEvent.xany.display, xEvent.xany.window);
#endif

    if (event) {
        setXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    }
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtAppPeekEvent
 * Signature: (ILorg/eclipse/swt/internal/motif/XAnyEvent)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XtAppPeekEvent
  (JNIEnv *env, jclass that, jint appContext, jobject event)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
    jboolean rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtAppPeekEvent\n");
#endif

    rc = (jboolean) XtAppPeekEvent((XtAppContext)appContext, &xEvent);
    if (event) {
        lpxEvent = &xEvent;
        cacheXanyeventFids(env, event, &PGLOB(XanyeventFc));
        setXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtAppPending
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtAppPending
  (JNIEnv *env, jclass that, jint appContext)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtAppPending\n");
#endif
    return (jint) XtAppPending((XtAppContext)appContext);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtAppProcessEvent
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtAppProcessEvent
  (JNIEnv *env, jclass that, jint appContext, jint inputMask)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtAppProcessEvent\n");
#endif
    XtAppProcessEvent((XtAppContext)appContext, inputMask);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtAppSetErrorHandler
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtAppSetErrorHandler
  (JNIEnv *env, jclass that, jint app_context, jint handler)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtAppSetErrorHandler\n");
#endif
    return (jint) XtAppSetErrorHandler((XtAppContext)app_context, (XtErrorHandler)handler);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtAppSetWarningHandler
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtAppSetWarningHandler
  (JNIEnv *env, jclass that, jint app_context, jint handler)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtAppSetWarningHandler\n");
#endif
    return (jint) XtAppSetWarningHandler((XtAppContext)app_context, (XtErrorHandler)handler);
}


/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtBuildEventMask
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtBuildEventMask
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtBuildEventMask\n");
#endif
    return (jint) XtBuildEventMask((Widget) widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtCallActionProc
 * Signature: (I[BLorg/eclipse/swt/internal/motif/XAnyEvent;[II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtCallActionProc
  (JNIEnv *env, jclass that, jint widget, jbyteArray action, jobject event, jintArray params, jint num_params)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
    jbyte *action1=NULL;
    jint *params1=NULL;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtCallActionProc\n");
#endif
    if (action)
        action1 = (*env)->GetByteArrayElements(env, action, NULL);
    if (params)
        params1 = (*env)->GetIntArrayElements(env, params, NULL);    

    XtCallActionProc((Widget)widget, (String)action1, lpxEvent, (String *)params1, num_params);
    if (event) {
        lpxEvent = &xEvent;
        cacheXanyeventFids(env, event, &PGLOB(XanyeventFc));
        setXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    }

    if (action)
        (*env)->ReleaseByteArrayElements(env, action, action1, 0);
    if (params)
        (*env)->ReleaseIntArrayElements(env, params, params1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtClass
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtClass
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtClass\n");
#endif
    return (jint) XtClass((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtConfigureWidget
 * Signature: (IIIIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtConfigureWidget
  (JNIEnv *env, jclass that, jint widget, jint x, jint y, jint width, jint height, jint borderWidth)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtConfigureWidget\n");
#endif
    XtConfigureWidget((Widget)widget, x, y, width, height, borderWidth);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtCreateApplicationContext
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtCreateApplicationContext
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtCreateApplicationContext\n");
#endif
    return (jint) XtCreateApplicationContext();
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtCreatePopupShell
 * Signature: ([BII[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtCreatePopupShell
  (JNIEnv *env, jclass that, jbyteArray name, jint widgetClass, jint parent, jintArray argList, jint argCount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtCreatePopupShell\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    

    rc = (jint) XtCreatePopupShell((String)name1, (WidgetClass)widgetClass, (Widget)parent, (ArgList)argList1, argCount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XtCreatePopupShell: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtDestroyApplicationContext
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtDestroyApplicationContext
  (JNIEnv *env, jclass that, jint appContext)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtDestroyApplicationContext\n");
#endif
    XtDestroyApplicationContext((XtAppContext)appContext);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtDestroyWidget
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtDestroyWidget
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtDestroyWidget\n");
#endif
    XtDestroyWidget((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtDispatchEvent
 * Signature: (Lorg/eclipse/swt/internal/motif/XAnyEvent;)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XtDispatchEvent
  (JNIEnv *env, jclass that, jobject event)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtDispatchEvent\n");
#endif

    if (event) {
        lpxEvent = &xEvent;
        cacheXanyeventFids(env, event, &PGLOB(XanyeventFc));
        getXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    #ifdef EVENT_TRACE
    	    fprintf(stderr, "type = %d, serial = %d, send_event = %d, display = %x, window = %x \n",
         xEvent.type, xEvent.xany.serial, xEvent.xany.send_event, xEvent.xany.display, xEvent.xany.window);
    #endif
    }
    return (jboolean) XtDispatchEvent(lpxEvent);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtDisplay
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtDisplay
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtDisplay\n");
#endif
    return (jint) XtDisplay((Widget)widget);
}


/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtDisplayToApplicationContext
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtDisplayToApplicationContext
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtDisplayToApplicationContext\n");
#endif
    return (jint) XtDisplayToApplicationContext((Display *)display);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtFree
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtFree
  (JNIEnv *env, jclass that, jint ptr)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtFree\n");
#endif
    XtFree((char *)ptr);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtGetMultiClickTime
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtGetMultiClickTime
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtGetMultiClickTime\n");
#endif
    return (jint) XtGetMultiClickTime((Display *)display);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtGetValues
 * Signature: (I[II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtGetValues
  (JNIEnv *env, jclass that, jint widget, jintArray argList, jint numArgs)
{
    jint *argList1=NULL;

#ifdef LINUX
    int values[numArgs];
    int zeros[numArgs];
    int i;
#endif


#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtGetValues\n");
#endif

    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
 
#ifdef LINUX
    for (i = 0; i < numArgs; i++) {   
        zeros[i] = 0;
	values[i] = 0;
        if (argList1[i * 2 + 1] == 0) {
            zeros[i] = 1;
            argList1[i * 2 + 1] = (int)&values[i];
        }
    }
#endif

    XtGetValues((Widget)widget, (ArgList)argList1, numArgs);

#ifdef LINUX
    for (i = 0; i < numArgs; i++) {   
        if (zeros[i]) {
           argList1[i * 2 + 1] = values[i];
        }
    }
#endif

    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtIsManaged
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XtIsManaged
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtIsManaged\n");
#endif
    return (jboolean) XtIsManaged((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtIsRealized
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XtIsRealized
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtIsRealized\n");
#endif
    return (jboolean) XtIsRealized((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtIsSubclass
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XtIsSubclass
  (JNIEnv *env, jclass that, jint widget, jint widgetClass)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtIsSubclass\n");
#endif
    return (jboolean) XtIsSubclass((Widget)widget, (WidgetClass)widgetClass);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtIsTopLevelShell
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XtIsTopLevelShell
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtIsTopLevelShell\n");
#endif
    return (jboolean) XtIsTopLevelShell((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtIsWidget
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XtIsWidget
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtIsWidget\n");
#endif
    return (jboolean)XtIsWidget((Widget)widget);
    
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtLastTimestampProcessed
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtLastTimestampProcessed
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtLastTimestampProcessed\n");
#endif
	return (jint)XtLastTimestampProcessed((Display *)display);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtMalloc
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtMalloc
  (JNIEnv *env, jclass that, jint size)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtMalloc\n");
#endif
    return (jint) XtMalloc(size);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtManageChild
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtManageChild
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtManageChild\n");
#endif
    XtManageChild((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtMapWidget
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtMapWidget
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtMapWidget\n");
#endif
    XtMapWidget((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtMoveWidget
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtMoveWidget
  (JNIEnv *env, jclass that, jint widget, jint x, jint y)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtMoveWidget\n");
#endif
    XtMoveWidget((Widget)widget, x, y);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtOpenDisplay
 * Signature: (I[B[B[BII[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtOpenDisplay
  (JNIEnv *env, jclass that, jint xtAppContext, jbyteArray displayName, jbyteArray applicationName,
     jbyteArray applicationClass, jint options, jint numOptions, jintArray argc, jint argv)
{
    jbyte *displayName1 = NULL;
    jbyte *applicationName1 = NULL;
    jbyte *applicationClass1 = NULL;
    jint *argc1=NULL;
    jint rc;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtOpenDisplay\n");
#endif
	if (displayName)
        displayName1 = (*env)->GetByteArrayElements(env, displayName, NULL);
    if (applicationName)
        applicationName1 = (*env)->GetByteArrayElements(env, applicationName, NULL);
    if (applicationClass)    
        applicationClass1 = (*env)->GetByteArrayElements(env, applicationClass, NULL);
    if (argc)    
        argc1 = (*env)->GetIntArrayElements(env, argc, NULL);
    

    rc = (jint) XtOpenDisplay((XtAppContext)xtAppContext, (String)displayName1, (String)applicationName1, (String)applicationClass1,
                    (XrmOptionDescRec *)options, numOptions, (int *)argc1, (char **)argv);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XtOpenDisplay: call failed rc = %d\n", rc);
#endif

    if (displayName)
        (*env)->ReleaseByteArrayElements(env, displayName, displayName1, 0);
    if (applicationName)
        (*env)->ReleaseByteArrayElements(env, applicationName, applicationName1, 0);
    if (applicationClass)
        (*env)->ReleaseByteArrayElements(env, applicationClass, applicationClass1, 0);
    if (argc)
        (*env)->ReleaseIntArrayElements(env, argc, argc1, 0);
        
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtParent
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtParent
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtParent\n");
#endif
    return (jint) XtParent((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtPopdown
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtPopdown
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtPopdown\n");
#endif
    XtPopdown((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtPopup
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtPopup
  (JNIEnv *env, jclass that, jint widget, jint flags)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtPopup\n");
#endif
    XtPopup((Widget)widget, flags);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtQueryGeometry
 * Signature: (ILorg/eclipse/swt/internal/motif/XtWidgetGeometryLorg/eclipse/swt/internal/motif/XtWidgetGeometry;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtQueryGeometry
  (JNIEnv *env, jclass that, jint widget, jobject intended, jobject preferred_return)
{
	DECL_GLOB(pGlob)

	XtWidgetGeometry intended1, preferred_return1, *lpIntended=NULL, *lpPreferred_return=NULL;
    jint rc;
   
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtQueryGeometry\n");
#endif
	lpPreferred_return = &preferred_return1;

    /* The objects passed in here are both XtWidgetGeometry structures
    ** If either or both are passed in, we only want to cache the fids once.
    */
    if (intended)
        cacheXtwidgetgeometryFids(env, intended, &PGLOB(XtwidgetgeometryFc));
    else if (preferred_return)
        cacheXtwidgetgeometryFids(env, preferred_return, &PGLOB(XtwidgetgeometryFc));
    
    if (intended) {
    	lpIntended = &intended1;
        /* intended and preferred_return are the same class XtWidgetGeometry */
        getXtwidgetgeometryFields(env, intended, lpIntended, &PGLOB(XtwidgetgeometryFc));
    }
    rc = (jint)XtQueryGeometry((Widget)widget, lpIntended, &preferred_return1);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XtQueryGeometry: call failed rc = %d\n", rc);
#endif
    
    if (preferred_return) {
        setXtwidgetgeometryFields(env, preferred_return, &preferred_return1, &PGLOB(XtwidgetgeometryFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtRealizeWidget
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtRealizeWidget
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtRealizeWidget\n");
#endif
    XtRealizeWidget((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtRemoveTimeOut
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtRemoveTimeOut
  (JNIEnv *env, jclass that, jint id)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtRemoveTimeOut\n");
#endif
    XtRemoveTimeOut(id);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtResizeWidget
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtResizeWidget
  (JNIEnv *env, jclass that, jint widget, jint width, jint height, jint borderWidth)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtResizeWidget\n");
#endif
    XtResizeWidget((Widget)widget, width, height, borderWidth);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtResizeWindow
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtResizeWindow
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtResizeWindow\n");
#endif
	XtResizeWindow((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtSetLanguageProc
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtSetLanguageProc
  (JNIEnv *env, jclass that, jint appContext, jint languageProc, jint pointer)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtSetLanguageProc\n");
#endif
    return (jint) XtSetLanguageProc((XtAppContext)appContext, (XtLanguageProc)languageProc, (XtPointer)pointer);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtSetMappedWhenManaged
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtSetMappedWhenManaged
  (JNIEnv *env, jclass that, jint widget, jboolean flag)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtSetMappedWhenManaged\n");
#endif
    XtSetMappedWhenManaged((Widget)widget, flag);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtSetValues
 * Signature: (I[II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtSetValues
  (JNIEnv *env, jclass that, jint widget, jintArray argList, jint numArgs)
{
    jint *argList1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtSetValues\n");
#endif

    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    XtSetValues((Widget)widget, (ArgList)argList1, numArgs);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtToolkitInitialize
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtToolkitInitialize
  (JNIEnv *env, jclass that)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtToolkitInitialize\n");
#endif
    XtToolkitInitialize();
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtTranslateCoords
 * Signature: (ISS[S[S)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtTranslateCoords
  (JNIEnv *env, jclass that, jint widget, jshort x, jshort y, jshortArray root_x, jshortArray root_y)
{
    jshort *root_x1=NULL,*root_y1=NULL;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtTranslateCoords\n");
#endif
    if (root_x)
        root_x1 = (*env)->GetShortArrayElements(env, root_x, NULL);
    if (root_y)
        root_y1 = (*env)->GetShortArrayElements(env, root_y, NULL);
    XtTranslateCoords((Widget)widget, x, y, root_x1, root_y1);
    if (root_x)
        (*env)->ReleaseShortArrayElements(env, root_x, root_x1, 0);
    if (root_y)
        (*env)->ReleaseShortArrayElements(env, root_y, root_y1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtUnmanageChild
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtUnmanageChild
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtUnmanageChild\n");
#endif
    XtUnmanageChild((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtUnmapWidget
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtUnmapWidget
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtUnmapWidget\n");
#endif
    XtUnmapWidget((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtWindow
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtWindow
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtWindow\n");
#endif
    return (jint) XtWindow((Widget)widget);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtWindowToWidget
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtWindowToWidget
  (JNIEnv *env, jclass that, jint display, jint window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtWindowToWidget\n");
#endif
    return (jint) XtWindowToWidget((Display *)display, window);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    strlen
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_strlen
  (JNIEnv *env, jclass that, jint string)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "strlen\n");
#endif
    return (jint) strlen((char *)string);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmClipboardCopy
 * Signature: (III[B[BII[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmClipboardCopy
  (JNIEnv *env, jclass that, jint display, jint window, jint item_id, jbyteArray format_name, jbyteArray buffer, jint length, jint private_id, jintArray data_id)
{
    jbyte *format_name1 = NULL;
    jbyte *buffer1 = NULL;
    jint  *data_id1 = NULL;
    jint   rc;
    
    if (format_name)
       format_name1 = (*env)->GetByteArrayElements(env, format_name, NULL);
       
    if (buffer)
       buffer1 = (*env)->GetByteArrayElements(env, buffer, NULL);
    	
    if (data_id)
    	data_id1 = (*env)->GetIntArrayElements(env, data_id, NULL);
        	
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmClipboardCopy\n");
#endif

    rc = (jint) XmClipboardCopy((Display *)display, (Window)window, (long)item_id, (char *)format_name1, (char *)buffer1, (unsigned long)length, private_id, (void *)data_id1);

    if (format_name)
        (*env)->ReleaseByteArrayElements(env, format_name, format_name1, 0);
        
    if (buffer)
        (*env)->ReleaseByteArrayElements(env, buffer, buffer1, 0); 
    
    if (data_id)
        (*env)->ReleaseIntArrayElements(env, data_id, data_id1, 0);
        
    return rc;
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmClipboardEndCopy
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmClipboardEndCopy
  (JNIEnv *env, jclass that, jint display, jint window, jint item_id)
{
        	
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmClipboardEndCopy\n");
#endif

    return (jint) XmClipboardEndCopy((Display *)display, (Window)window, (long)item_id );
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmClipboardStartCopy
 * Signature: (IIIIII[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmClipboardStartCopy
  (JNIEnv *env, jclass that, jint display, jint window, jint clip_label, jint timestamp, jint widget, jint callback, jintArray item_id)
{
    jint  *item_id1 = NULL;
    jint   rc;
       
    if (item_id)
       item_id1 = (*env)->GetIntArrayElements(env, item_id, NULL);
        	
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmClipboardStartCopy\n");
#endif

    rc = (jint) XmClipboardStartCopy((Display *)display, (Window)window, (XmString)clip_label, timestamp, (Widget)widget, (XmCutPasteProc)callback, (long *)item_id1);
   
    if (item_id)
        (*env)->ReleaseIntArrayElements(env, item_id, item_id1, 0);
        
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmClipboardInquireLength
 * Signature: (II[B[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmClipboardInquireLength
  (JNIEnv *env, jclass that, jint display, jint window, jbyteArray format_name, jintArray length)
{
    jbyte *format_name1 = NULL;
    jint  *length1 = NULL;
    jint   rc;
    
    if (format_name)
       format_name1 = (*env)->GetByteArrayElements(env, format_name, NULL);
         
    if (length)
    	length1 = (*env)->GetIntArrayElements(env, length, NULL);
    	
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmClipboardInquireLength\n");
#endif
    rc = (jint) XmClipboardInquireLength((Display *)display, (Window)window, (char *)format_name1, (unsigned long *)length1);

    if (format_name)
        (*env)->ReleaseByteArrayElements(env, format_name, format_name1, 0);
        
    if (length)
        (*env)->ReleaseIntArrayElements(env, length, length1, 0); 
        
    return rc;
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmClipboardInquireFormat
 * Signature: (III[BI[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmClipboardInquireFormat
  (JNIEnv *env, jclass that, jint display, jint window, int index, jbyteArray format_name_buf, int buffer_len, jintArray copied_len)
{
    jbyte *format_name_buf1 = NULL;
    jint  *copied_len1 = NULL;
    jint   rc;
    
    if (format_name_buf)
       format_name_buf1 = (*env)->GetByteArrayElements(env, format_name_buf, NULL);
         
    if (copied_len)
    	copied_len1 = (*env)->GetIntArrayElements(env, copied_len, NULL);
    	
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmClipboardInquireFormat\n");
#endif
    rc = (jint) XmClipboardInquireFormat((Display *)display, (Window)window, index, (char *)format_name_buf1, buffer_len, (unsigned long *)copied_len1);

    if (format_name_buf)
        (*env)->ReleaseByteArrayElements(env, format_name_buf, format_name_buf1, 0);
        
    if (copied_len)
        (*env)->ReleaseIntArrayElements(env, copied_len, copied_len1, 0); 
        
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmClipboardInquireCount
 * Signature: (II[I[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmClipboardInquireCount
  (JNIEnv *env, jclass that, jint display, jint window, jintArray count, jintArray max_format_name_length)
{
    jint  *count1 = NULL;
    jint  *max_format_name_length1 = NULL;
    jint   rc;
    
    if (count)
    	count1 = (*env)->GetIntArrayElements(env, count, NULL);
         
    if (max_format_name_length)
    	max_format_name_length1 = (*env)->GetIntArrayElements(env, max_format_name_length, NULL);
    	
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmClipboardInquireCount\n");
#endif
    rc = (jint) XmClipboardInquireCount((Display *)display, (Window)window, (int *)count1, (unsigned long *)max_format_name_length1);

    if (count)
        (*env)->ReleaseIntArrayElements(env, count, count1, 0);
        
    if (max_format_name_length)
        (*env)->ReleaseIntArrayElements(env, max_format_name_length, max_format_name_length1, 0); 
        
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmClipboardStartRetrieve
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmClipboardStartRetrieve
  (JNIEnv *env, jclass that, jint display, jint window, jint timestamp)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmClipboardStartRetrieve\n");
#endif
    return (jint) XmClipboardStartRetrieve((Display *)display, (Window)window, timestamp);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmClipboardRetrieve
 * Signature: (III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmClipboardRetrieve
  (JNIEnv *env, jclass that, jint display, jint window, jbyteArray format_name, jbyteArray buffer, jint length, jintArray num_bytes, jintArray private_id)
{
    jbyte *format_name1 = NULL;
    jbyte *buffer1 = NULL;
    jint  *num_bytes1 = NULL;
    jint  *private_id1 = NULL;
    jint   rc;
    
    if (format_name)
       format_name1 = (*env)->GetByteArrayElements(env, format_name, NULL);
       
    if (buffer)
       buffer1 = (*env)->GetByteArrayElements(env, buffer, NULL);
       
    if (num_bytes)
    	num_bytes1 = (*env)->GetIntArrayElements(env, num_bytes, NULL);
    	
    if (private_id)
    	private_id1 = (*env)->GetIntArrayElements(env, private_id, NULL);
        	
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmClipboardRetrieve\n");
#endif

    rc = (jint) XmClipboardRetrieve((Display *)display, (Window)window, (char *)format_name1, (char *)buffer1, (unsigned long)length, (unsigned long *)num_bytes1, (void *)private_id1);
   
    if (format_name)
        (*env)->ReleaseByteArrayElements(env, format_name, format_name1, 0);
        
    if (buffer)
        (*env)->ReleaseByteArrayElements(env, buffer, buffer1, 0);
        
    if (num_bytes)
        (*env)->ReleaseIntArrayElements(env, num_bytes, num_bytes1, 0); 
    
    if (private_id)
        (*env)->ReleaseIntArrayElements(env, private_id, private_id1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmClipboardEndRetrieve
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmClipboardEndRetrieve
  (JNIEnv *env, jclass that, jint display, jint window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmClipboardEndRetrieve\n");
#endif
    return (jint) XmClipboardEndRetrieve((Display *)display, (Window)window);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpmReadFileToPixmap
 * Signature: (II[B[I[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XpmReadFileToPixmap
  (JNIEnv *env, jclass that, jint display, jint drawable, jbyteArray fileName, jintArray pixmap_return, jintArray shapemask_return, jint attributes)
{
#ifdef XPM
	jint rc;
	jbyte *fileName1 = NULL;
	jint *pixmap_return1 = NULL;
	jint *shapemask_return1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpmReadFileToPixmap\n");
#endif
	if (fileName) fileName1 = (*env)->GetByteArrayElements(env, fileName, NULL);
	if (pixmap_return) pixmap_return1 = (*env)->GetIntArrayElements(env, pixmap_return, NULL);
	if (shapemask_return) shapemask_return1 = (*env)->GetIntArrayElements(env, shapemask_return, NULL);
	
	rc = (jint) XpmReadFileToPixmap((Display *)display, 
		drawable, 
		fileName1, 
		(Pixmap*) pixmap_return1, 
		(Pixmap*) shapemask_return1, 
		attributes);
	
  	if (fileName) (*env)->ReleaseByteArrayElements(env, fileName, fileName1, 0);
	if (pixmap_return) (*env)->ReleaseIntArrayElements(env, pixmap_return, pixmap_return1, 0);
	if (shapemask_return) (*env)->ReleaseIntArrayElements(env, shapemask_return, shapemask_return1, 0);
  	return rc;
#endif
#ifndef XPM
	return -1;
#endif
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateDrawnButton
 * Signature: (I[B[II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateDrawnButton
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
    jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateDrawnButton\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);

    rc = (jint)XmCreateDrawnButton((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateDrawnButton: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringGenerate
 * Signature: ([B[BI[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringGenerate
  (JNIEnv *env, jclass that, jbyteArray text, jbyteArray tag, jint type, jbyteArray rendition)
{
    jbyte *text1 = NULL;
    jbyte *tag1 = NULL;
    jbyte *rendition1 = NULL;
    jint rc = 0;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringGenerate\n");
#endif
    if (text) text1 = (*env)->GetByteArrayElements(env, text, NULL);
    if (tag) tag1 = (*env)->GetByteArrayElements(env, tag, NULL);    
    if (rendition) rendition1 = (*env)->GetByteArrayElements(env, rendition, NULL);
    	
    rc = (jint) XmStringGenerate((XtPointer) text1, (XmStringTag) tag1, type, (XmStringTag) rendition1);

    if (text) (*env)->ReleaseByteArrayElements(env, text, text1, 0);
    if (tag) (*env)->ReleaseByteArrayElements(env, tag, tag1, 0);
    if (rendition) (*env)->ReleaseByteArrayElements(env, rendition, rendition1, 0);
    return rc;
}


/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmRenderTableAddRenditions
 * Signature: (I[III)I
 */
JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_motif_OS_XmRenderTableAddRenditions
  (JNIEnv *env, jclass that, jint oldTable, jintArray renditions, jint renditionCount, jint mergeMode)
{
	jint *renditionArray = NULL;
	jint renderTable = 0;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmRenderTableAddRenditions\n");
#endif
	if (renditions) renditionArray = (*env)->GetIntArrayElements(env, renditions, NULL);    
	renderTable = (int) XmRenderTableAddRenditions((XmRenderTable) oldTable, (XmRendition*) renditionArray, renditionCount, mergeMode);
	if (renditions) (*env)->ReleaseIntArrayElements(env, renditions, renditionArray, 0);
	return renderTable;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmRenderTableFree
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmRenderTableFree
  (JNIEnv *env, jclass that, jint renderTable)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmRenderTableFree\n");
#endif
	XmRenderTableFree((XmRenderTable) renderTable);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmRenditionCreate
 * Signature: (I[B[II)I
 */
JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_motif_OS_XmRenditionCreate
  (JNIEnv *env, jclass that, jint widget, jbyteArray renditionTag, jintArray argList, jint argCount)
{
	jbyte *renditionTag1 = NULL;
	jint *argList1 = NULL;
	jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmRenditionCreate\n");
#endif
	if (renditionTag) renditionTag1 = (*env)->GetByteArrayElements(env, renditionTag, NULL);
	if (argList) argList1 = (*env)->GetIntArrayElements(env, argList, NULL); 
	rc = (jint) XmRenditionCreate((Widget) widget, (XmStringTag) renditionTag1, (ArgList) argList1, argCount);
	if (renditionTag) (*env)->ReleaseByteArrayElements(env, renditionTag, renditionTag1, 0);
	if (argList) (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmRenditionFree
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmRenditionFree
  (JNIEnv *env, jclass that, jint rendition)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmRenditionFree\n");
#endif
	XmRenditionFree((XmRendition) rendition);
}
 
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTabCreate
 * Signature: (IBBB[B)I
 */
JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTabCreate
  (JNIEnv *env, jclass that, jint value, jbyte units, jbyte offsetModel, jbyte alignment, jbyteArray decimal)
{
	jbyte *decimal1 = NULL;
	jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTabCreate\n");
#endif
	if (decimal) decimal1 = (*env)->GetByteArrayElements(env, decimal, NULL);
	rc = (jint) XmTabCreate(value, units, offsetModel, alignment, (char*) decimal1);
	if (decimal) (*env)->ReleaseByteArrayElements(env, decimal, decimal1, 0);	
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTabFree
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTabFree
  (JNIEnv *env, jclass that, jint tab)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTabFree\n");
#endif
	XmTabFree((XmTab) tab);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTabListFree
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTabListFree
  (JNIEnv *env, jclass that, jint tabList)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTabListFree\n");
#endif
	XmTabListFree((XmTabList) tabList);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTabListInsertTabs
 * Signature: (I[III)I
 */
JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTabListInsertTabs
  (JNIEnv *env, jclass that, jint oldList, jintArray tabs, jint tab_count, jint position)
{
	jint *tabs1 = NULL;
	jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTabListInsertTabs\n");
#endif
	if (tabs) tabs1 = (*env)->GetIntArrayElements(env, tabs, NULL); 
	rc = (jint) XmTabListInsertTabs((XmTabList) oldList, (XmTab*) tabs1, tab_count, position);
	if (tabs) (*env)->ReleaseIntArrayElements(env, tabs, tabs1, 0);
	return rc;
}
 

/* ---- unused OS calls, kept here in case we use them later ---- */

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XBlackPixel
 * Signature: (II)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XBlackPixel
  (JNIEnv *env, jclass that, jint display, jint screenNum)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XBlackPixel\n");
#endif
    return (jint) XBlackPixel((Display *)display, screenNum);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XChangeActivePointerGrab
 * Signature: (IIII)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XChangeActivePointerGrab
  (JNIEnv *env, jclass that, jint display, jint eventMask, jint cursor, jint time)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XChangeActivePointerGrab\n");
#endif
    XChangeActivePointerGrab((Display *)display, eventMask, (Cursor)cursor, (Time)time);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDefaultGC
 * Signature: (II)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDefaultGC
  (JNIEnv *env, jclass that, jint display, jint screen_number)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDefaultGC\n");
#endif
	return (jint) XDefaultGC((Display *)display, screen_number);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XNoOp
 * Signature: (I)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XNoOp
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XNoOp\n");
#endif
    XNoOp((Display *)display);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XRootWindowOfScreen
 * Signature: (I)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XRootWindowOfScreen
  (JNIEnv *env, jclass that, jint screen)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XRootWindowOfScreen\n");
#endif
    return (jint) XRootWindowOfScreen((Screen *)screen);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSendEvent
 * Signature: (Lorg/eclipse/swt/internal/motif/XAnyEvent;)Z
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XSendEvent
  (JNIEnv *env, jclass that, jint display, jint w, jboolean propagate, jint event_mask, jobject event)
{
    XEvent xEvent, *lpxEvent=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSendEvent\n");
#endif

    if (event) {
        lpxEvent = &xEvent;
        cacheXanyeventFids(env, event, &XanyeventFc);
        getXanyeventFields(env, event, lpxEvent, &XanyeventFc);
    }
    return (jint) XSendEvent((Display *)display, w, propagate, event_mask, lpxEvent);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateCascadeButton
 * Signature: (I[B[II)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateCascadeButton
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateCascadeButton\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XmCreateCascadeButton((Widget)parent, (String)name1, (ArgList)argList1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateCascadeButton: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateDrawnButton
 * Signature: (I[B[II)I
 */
/*JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateDrawnButton
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
	jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateDrawnButton\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);

    rc = (jint)XmCreateDrawnButton((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateDrawnButton: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}*/


/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateRowColumn
 * Signature: (I[B[II)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateRowColumn
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray arglist, jint argcount)
{
	jint *arglist1=NULL;
    jbyte *name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateRowColumn\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (arglist)
        arglist1 = (*env)->GetIntArrayElements(env, arglist, NULL);

    rc = (jint)XmCreateRowColumn((Widget)parent, (String)name1, (ArgList)arglist1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateRowColumn: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (arglist)
        (*env)->ReleaseIntArrayElements(env, arglist, arglist1, 0);
	return rc;
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmCreateScrolledWindow
 * Signature: (I[B[II)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmCreateScrolledWindow
  (JNIEnv *env, jclass that, jint parent, jbyteArray name, jintArray argList, jint argcount)
{
    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmCreateScrolledWindow\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);
    rc = (jint) XmCreateScrolledWindow((Widget)parent, (String)name1, (ArgList)argList1, argcount);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmCreateScrolledWindow: call failed rc = %d\n", rc);
#endif

    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmDestroyPixmap
 * Signature: (II)Z
 */
/* JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmDestroyPixmap
  (JNIEnv *env, jclass that, jint screen, jint pixmap)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmDestroyPixmap\n");
#endif
	return (jboolean) XmDestroyPixmap((Screen *)screen, pixmap);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmGetPixmapByDepth
 * Signature: (I[BIII)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmGetPixmapByDepth
  (JNIEnv *env, jclass that, jint screen, jbyteArray image_name, jint foreground, jint background, jint depth)
{
    jbyte *image_name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmGetPixmapByDepth\n");
#endif

    if (image_name)
        image_name1 = (*env)->GetByteArrayElements(env, image_name, NULL);

    rc = (jint)XmGetPixmapByDepth((Screen *)screen, (char *)image_name1, foreground, background, depth);

#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XmGetPixmapByDepth: call failed rc = %d\n", rc);
#endif

    if (image_name)
        (*env)->ReleaseByteArrayElements(env, image_name, image_name1, 0);
	return rc;
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListAddItemsUnselected
 * Signature: (IIII)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListAddItemsUnselected
  (JNIEnv *env, jclass that, jint list, jint xmStringTable, jint item_count, jint position)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListAddItemsUnselected\n");
#endif
    XmListAddItemsUnselected((Widget)list, (XmString *)xmStringTable, item_count, position);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListDeleteItem
 * Signature: (II)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListDeleteItem
  (JNIEnv *env, jclass that, jint list, jint item)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListDeleteItem\n");
#endif
    XmListDeleteItem((Widget)list, (XmString)item);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListDeselectItem
 * Signature: (II)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListDeselectItem
  (JNIEnv *env, jclass that, jint list, jint xmString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListDeselectItem\n");
#endif
	XmListDeselectItem((Widget)list, (XmString)xmString);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmListSelectItem
 * Signature: (IIZ)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmListSelectItem
  (JNIEnv *env, jclass that, jint list, jint xmString, jboolean notify)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmListSelectItem\n");
#endif
	XmListSelectItem((Widget)list, (XmString)xmString, notify);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringDrawUnderline
 * Signature: (IIIIIIIIIILorg/eclipse/swt/internal/motif/XRectangle;I)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringDrawUnderline
  (JNIEnv *env, jclass that, jint display, jint window, jint fontlist, jint xmString, jint gc, jint x, jint y, jint width, jint align, jint lay_dir, jobject clip, jint xmStringUnderline)
{
	XRectangle xRect, *lpxRect=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringDrawUnderline\n");
#endif

    if (clip) {
        lpxRect = &xRect;
        cacheXrectangleFids(env, clip, &XrectangleFc);
        getXrectangleFields(env, clip, lpxRect, &XrectangleFc);
    }
    XmStringDrawUnderline((Display *)display, window, (XmFontList)fontlist, (XmString)xmString, (GC)gc, x, y, width, align, lay_dir, lpxRect, (XmString)xmStringUnderline);
    if (clip) {
        setXrectangleFields(env, clip, lpxRect, &XrectangleFc);
    }
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringLineCount
 * Signature: (I)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringLineCount
  (JNIEnv *env, jclass that, jint xmString)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringLineCount\n");
#endif
    return (jint) XmStringLineCount((XmString)xmString);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmTextRemove
 * Signature: (I)Z
 */
/* JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XmTextRemove
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmTextRemove\n");
#endif
	return (jboolean) XmTextRemove((Widget)widget);
}
*/


/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtCloseDisplay
 * Signature: (I)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtCloseDisplay
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtCloseDisplay\n");
#endif
    XtCloseDisplay((Display *)display);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtCallActionProc
 * Signature: (I[BLorg/eclipse/swt/internal/motif/XAnyEvent;[BI)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtCallActionProc
  (JNIEnv *env, jclass that, jint widget, jbyteArray action, jobject event, jbyteArray params, jint num_params)
{
	XEvent xEvent, *lpxEvent=NULL;
    jbyte *action1, *params1;

#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtCallActionProc\n");
#endif
    if (action)
        action1 = (*env)->GetByteArrayElements(env, action, NULL);
    if (params)
        params1 = (*env)->GetByteArrayElements(env, params, NULL);    

    XtCallActionProc((Widget)widget, (String)action1, lpxEvent, (String *)params1, num_params);
    if (event) {
        lpxEvent = &xEvent;
        cacheXanyeventFids(env, event, &XanyeventFc);
        setXanyeventFields(env, event, lpxEvent, &XanyeventFc);
    }

    if (action)
        (*env)->ReleaseByteArrayElements(env, action, action1, 0);
    if (params)
        (*env)->ReleaseByteArrayElements(env, params, params1, 0);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtCloseDisplay
 * Signature: (I)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtCloseDisplay
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtCloseDisplay\n");
#endif
    XtCloseDisplay((Display *)display);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtCreateWidget
 * Signature: ([BII[II)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtCreateWidget
  (JNIEnv *env, jclass that, jbyteArray name, jint widgetClass, jint parent, jintArray argList, jint argCount)
{

    jbyte *name1=NULL;
    jint *argList1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtCreateWidget\n");
#endif

    if (name)
        name1 = (*env)->GetByteArrayElements(env, name, NULL);
    if (argList)
        argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
    rc = (jint) XtCreateWidget((String)name1, (WidgetClass)widgetClass, (Widget)parent, (ArgList)argList1, argCount);
#ifdef PRINT_FAILED_RCODES
    if (rc == 0)
        fprintf(stderr, "XtCreateWidget: call failed rc = %d\n", rc);
#endif
    if (name)
        (*env)->ReleaseByteArrayElements(env, name, name1, 0);
    if (argList)
        (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
    return rc;
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtRemoveCallback
 * Signature: (IIII)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtRemoveCallback
  (JNIEnv *env, jclass that, jint widget, jint callback_name, jint callback, jint client_data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtRemoveCallback\n");
#endif
    XtRemoveCallback((Widget)widget, (char *)callback_name, (XtCallbackProc)callback, (XtPointer)client_data);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtRemoveEventHandler
 * Signature: (IIZII)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtRemoveEventHandler
  (JNIEnv *env, jclass that, jint widget, jint event_mask, jboolean nonmaskable, jint proc, jint client_data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtRemoveEventHandler\n");
#endif
    XtRemoveEventHandler((Widget)widget, event_mask, nonmaskable, (XtEventHandler)proc, (XtPointer)client_data);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtScreen
 * Signature: (I)I
 */
/* JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtScreen
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtScreen\n");
#endif
    return (jint) XtScreen((Widget)widget);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSelectInput
 * Signature: (III)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XSelectInput
  (JNIEnv *env, jclass that, jint display, jint window, jint mask)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSelectInput\n");
#endif
    XSelectInput((Display *)display, (Window)window, mask);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtIsSensitive
 * Signature: (I)Z
 */
/* JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XtIsSensitive
  (JNIEnv *env, jclass that, jint widget)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtIsSensitive\n");
#endif
    return (jboolean) XtIsSensitive((Widget)widget);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtSetSensitive
 * Signature: (IZ)V
 */
/* JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtSetSensitive
  (JNIEnv *env, jclass that, jint widget, jboolean sensitive)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtSetSensitive\n");
#endif
    XtSetSensitive((Widget)widget, sensitive);
}
*/

/* ---- unused OS calls end here ---- */

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmParseMappingCreate
 * Signature: ([II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmParseMappingCreate
  (JNIEnv *env, jclass that, jintArray argList, jint numArgs)
{
	jint *argList1 = NULL;
	jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmParseMappingCreate\n");
#endif
	if (argList) argList1 = (*env)->GetIntArrayElements(env, argList, NULL);    
	rc = (jint) XmParseMappingCreate((ArgList)argList1, numArgs);
	if (argList) (*env)->ReleaseIntArrayElements(env, argList, argList1, 0);
	return rc;
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmParseMappingFree
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmParseMappingFree
  (JNIEnv *env, jclass that, jint parseMapping)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmParseMappingFree\n");
#endif
	XmParseMappingFree((XmParseMapping) parseMapping);
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmParseTableFree
 * Signature: ([II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XmParseTableFree
  (JNIEnv *env, jclass that, jintArray parseTable, jint mappingCount)
{
	jint *parseTable1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmParseTableFree\n");
#endif
	if (parseTable) {
		parseTable1 = (*env)->GetIntArrayElements(env, parseTable, NULL);    
		XmParseTableFree((XmParseTable) parseTable1, mappingCount);
		(*env)->ReleaseIntArrayElements(env, parseTable, parseTable1, 0);
	}
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringComponentCreate
 * Signature: (II[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringComponentCreate
  (JNIEnv *env, jclass that, jint type, jint length, jbyteArray value)
{
	jbyte* value1 = NULL;
	jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringComponentCreate\n");
#endif
	if (value) value1 = (*env)->GetByteArrayElements(env, value, NULL);    
	rc = (jint) XmStringComponentCreate(type, length, (XtPointer) value1);
	if (value) (*env)->ReleaseByteArrayElements(env, value, value1, 0);
	
	return rc;
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringParseText
 * Signature: ([BI[BI[III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringParseText
  (JNIEnv *env, jclass that, jbyteArray text, jint textEnd, jbyteArray tag, jint tagType, jintArray parseTable, jint parseCount, jint callData)
{
	jbyte* text1 = NULL;
	jbyte* tag1 = NULL;
	jint* parseTable1 = NULL;
	jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringParseText\n");
#endif
	if (text) text1 = (*env)->GetByteArrayElements(env, text, NULL);    	
	if (tag) tag1 = (*env)->GetByteArrayElements(env, tag, NULL);    
	if (parseTable) parseTable1 = (*env)->GetIntArrayElements(env, parseTable, NULL);
	rc = (jint) XmStringParseText(
		(XtPointer) text1, 
		(XtPointer*) textEnd, 
		(XmStringTag) tag1, 
		tagType, 
		(XmParseTable) parseTable1, 
		parseCount, 
		(XtPointer) callData);
	if (text) (*env)->ReleaseByteArrayElements(env, text, text1, 0);	
	if (tag) (*env)->ReleaseByteArrayElements(env, tag, tag1, 0);
	if (parseTable) (*env)->ReleaseIntArrayElements(env, parseTable, parseTable1, 0);
	return rc;
}
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XmStringUnparse
 * Signature: (I[BII[III)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XmStringUnparse
  (JNIEnv *env, jclass that, jint xmString, jbyteArray tag, jint tagType, jint outputType, jintArray parseTable, jint parseCount, jint parseModel)
{
	jbyte* tag1 = NULL;
	jint* parseTable1 = NULL;
	jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XmStringUnparse\n");
#endif
	if (tag) tag1 = (*env)->GetByteArrayElements(env, tag, NULL);    
	if (parseTable) parseTable1 = (*env)->GetIntArrayElements(env, parseTable, NULL);
	rc = (jint) XmStringUnparse((XmString) xmString, 
		(XmStringTag) tag1, 
		tagType, 
		outputType, 
		(XmParseTable) parseTable1, 
		parseCount, 
		parseModel);
	if (tag) (*env)->ReleaseByteArrayElements(env, tag, tag1, 0);
	if (parseTable) (*env)->ReleaseIntArrayElements(env, parseTable, parseTable1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtParseTranslationTable
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtParseTranslationTable
  (JNIEnv *env, jclass that, jbyteArray string)
{
	jint rc;
	jbyte *string1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtParseTranslationTable\n");
#endif
	if (string) string1 = (*env)->GetByteArrayElements(env, string, NULL);    
	rc = (jint) XtParseTranslationTable((String) string1);
	if (string) (*env)->ReleaseByteArrayElements(env, string, string1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtOverrideTranslations
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtOverrideTranslations
  (JNIEnv *env, jclass that, jint w, jint translations)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtOverrideTranslations\n");
#endif
	XtOverrideTranslations((Widget) w, (XtTranslations) translations);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCheckIfEvent
 * Signature: (ILorg/eclipse/swt/internal/motif/XAnyEvent;II)Z
 */
typedef Bool (*PredicateFunc)();

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XCheckIfEvent
  (JNIEnv *env, jclass that, jint display, jobject event, jint predicate, jint arg)
{
	DECL_GLOB(pGlob)
	XEvent xEvent, *lpxEvent=NULL;
	jboolean rc;
    
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCheckIfEvent\n");
#endif

    if (event) {
        lpxEvent = &xEvent;
        cacheXanyeventFids(env, event, &PGLOB(XanyeventFc));
        getXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    }
    rc = (jboolean)XCheckIfEvent((Display *)display, lpxEvent, (PredicateFunc) predicate, (void *) arg);

#ifdef PRINT_FAILED_RCODES
    if (rc != True && rc != False)
        fprintf(stderr, "XCheckIfEvent: call failed rc = %d\n", rc);
#endif

    if (event) {
        setXanyeventFields(env, event, lpxEvent, &PGLOB(XanyeventFc));
    }
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtToolkitThreadInitialize
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_motif_OS_XtToolkitThreadInitialize
  (JNIEnv *env, jclass that)
{
   DECL_GLOB(pGlob)
   /*
   * WARNING: When running under VA/Java, XtToolkitThreadInitialize
   * is not called because this hangs VA/Java and SWT.
   */
   if (pGlob->vajava) return;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtToolkitThreadInitialize\n");
#endif
   return (jboolean) XtToolkitThreadInitialize ();
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    Call
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_Call
  (JNIEnv *env, jclass that, jint proc, jint arg0, jint arg1)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "Call\n");
#endif
   return (*((jint (*)(jint, jint))proc)) (arg0, arg1);
}

/*
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XGetErrorText
  (JNIEnv *env, jclass that, jint display, jint code, jbyteArray buffer_return, jint length)
{
	jint rc;
	jbyte *buffer_return1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtParseTranslationTable\n");
#endif
	if (buffer_return) buffer_return1 = (*env)->GetByteArrayElements(env, buffer_return, NULL);    
	XGetErrorText ((Display *)display, code, buffer_return1, length);
	if (buffer_return) (*env)->ReleaseByteArrayElements(env, buffer_return, buffer_return1, 0);
}
*/

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XSetIOErrorHandler
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XSetIOErrorHandler
  (JNIEnv *env, jclass that, jint handler)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XSetIOErrorHandler\n");
#endif
    return (jint) XSetIOErrorHandler((XIOErrorHandler)handler);
}

/*
 * ======== Start printing functions ========
 */
 
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpCreateContext
 * Signature: (I[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XpCreateContext
  (JNIEnv *env, jclass that, jint display, jbyteArray printer_name)
{
    jbyte *printer_name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpCreateContext\n");
#endif
    if (printer_name)
        printer_name1 = (*env)->GetByteArrayElements(env, printer_name, NULL);    
    rc = (jint) XpCreateContext((Display *)display, (char *)printer_name1);
    if (printer_name)
        (*env)->ReleaseByteArrayElements(env, printer_name, printer_name1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpGetPrinterList
 * Signature: (I[B[I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XpGetPrinterList
  (JNIEnv *env, jclass that, jint display, jbyteArray printer_name, jintArray list_count)
{
    jbyte *printer_name1=NULL;
    jint *list_count1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpGetPrinterList\n");
#endif
    if (printer_name)
        printer_name1 = (*env)->GetByteArrayElements(env, printer_name, NULL);    
    if (list_count)
        list_count1 = (*env)->GetIntArrayElements(env, list_count, NULL);
    rc = (jint) XpGetPrinterList((Display *)display, (char *)printer_name1, (int *)list_count1);
    if (printer_name)
        (*env)->ReleaseByteArrayElements(env, printer_name, printer_name1, 0);
    if (list_count)
        (*env)->ReleaseIntArrayElements(env, list_count, list_count1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpFreePrinterList
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XpFreePrinterList
  (JNIEnv *env, jclass that, jint printer_list)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpFreePrinterList\n");
#endif
    XpFreePrinterList((XPPrinterList)printer_list);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpGetAttributes
 * Signature: (IIB)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XpGetAttributes
  (JNIEnv *env, jclass that, jint display, jint print_context, jbyte type)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpGetAttributes\n");
#endif
    return (jint) XpGetAttributes((Display *)display, (XPContext)print_context, (XPAttributes)type);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpGetOneAttribute
 * Signature: (IIB[B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XpGetOneAttribute
  (JNIEnv *env, jclass that, jint display, jint print_context, jbyte type, jbyteArray attribute_name)
{
    jbyte *attribute_name1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpGetOneAttribute\n");
#endif
    if (attribute_name)
        attribute_name1 = (*env)->GetByteArrayElements(env, attribute_name, NULL);    
    rc = (jint) XpGetOneAttribute((Display *)display, (XPContext)print_context, (XPAttributes)type, (char *)attribute_name1);
    if (attribute_name)
        (*env)->ReleaseByteArrayElements(env, attribute_name, attribute_name1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpSetAttributes
 * Signature: (IIB[BB)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XpSetAttributes
  (JNIEnv *env, jclass that, jint display, jint print_context, jbyte type, jbyteArray pool, jbyte replacement_rule)
{
    jbyte *pool1=NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpSetAttributes\n");
#endif
    if (pool)
        pool1 = (*env)->GetByteArrayElements(env, pool, NULL);    
    XpSetAttributes((Display *)display, (XPContext)print_context, (XPAttributes)type, (char *)pool1, (XPAttrReplacement)replacement_rule);
    if (pool)
        (*env)->ReleaseByteArrayElements(env, pool, pool1, 0);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpSetContext
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XpSetContext
  (JNIEnv *env, jclass that, jint display, jint print_context)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpSetContext\n");
#endif
    XpSetContext((Display *)display, (XPContext)print_context);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpGetScreenOfContext
 * Signature: (II)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XpGetScreenOfContext
  (JNIEnv *env, jclass that, jint display, jint print_context)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpGetScreenOfContext\n");
#endif
    return (jint) XpGetScreenOfContext((Display *)display, (XPContext)print_context);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpDestroyContext
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XpDestroyContext
  (JNIEnv *env, jclass that, jint display, jint print_context)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpDestroyContext\n");
#endif
    XpDestroyContext((Display *)display, (XPContext)print_context);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpGetPageDimensions
 * Signature: (II[S[SLorg/eclipse/swt/internal/motif/XRectangle;)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XpGetPageDimensions
  (JNIEnv *env, jclass that, jint display, jint print_context, jshortArray width, jshortArray height, jobject rectangle)
{
	DECL_GLOB(pGlob)
    XRectangle xRect, *lpxRect=NULL;
    jshort *width1=NULL, *height1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpGetPageDimensions\n");
#endif

    if (rectangle) {
        lpxRect = &xRect;
        cacheXrectangleFids(env, rectangle, &PGLOB(XrectangleFc));
        getXrectangleFields(env, rectangle, lpxRect, &PGLOB(XrectangleFc));
    }
    if (width)
        width1 = (*env)->GetShortArrayElements(env, width, NULL);    
    if (height)
        height1 = (*env)->GetShortArrayElements(env, height, NULL);    
    rc = (jint) XpGetPageDimensions((Display *)display, (XPContext)print_context, 
            (unsigned short *)width1, (unsigned short *)height1, (XRectangle *)lpxRect);
    if (rectangle) {
        setXrectangleFields(env, rectangle, lpxRect, &PGLOB(XrectangleFc));
    }
    if (width)
        (*env)->ReleaseShortArrayElements(env, width, width1, 0);
    if (height)
        (*env)->ReleaseShortArrayElements(env, height, height1, 0);
    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpStartJob
 * Signature: (IB)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XpStartJob
  (JNIEnv *env, jclass that, jint display, jbyte save_data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpStartJob\n");
#endif
    XpStartJob((Display *)display, (XPSaveData)save_data);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpStartPage
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XpStartPage
  (JNIEnv *env, jclass that, jint display, jint window)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpStartPage\n");
#endif
    XpStartPage((Display *)display, (Window)window);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpEndPage
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XpEndPage
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpEndPage\n");
#endif
    XpEndPage((Display *)display);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpEndJob
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XpEndJob
  (JNIEnv *env, jclass that, jint display)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpEndJob\n");
#endif
    XpEndJob((Display *)display);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpCancelJob
 * Signature: (IZ)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XpCancelJob
  (JNIEnv *env, jclass that, jint display, jboolean discard)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpCancelJob\n");
#endif
    XpCancelJob((Display *)display, discard);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDefaultGCOfScreen
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDefaultGCOfScreen
  (JNIEnv *env, jclass that, jint screen)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDefaultGCOfScreen\n");
#endif
    return (jint) XDefaultGCOfScreen((Screen *)screen);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDefaultColormapOfScreen
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XDefaultColormapOfScreen
  (JNIEnv *env, jclass that, jint screen)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDefaultColormapOfScreen\n");
#endif
    return (jint) XDefaultColormapOfScreen((Screen *)screen);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XRootWindowOfScreen
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XRootWindowOfScreen
  (JNIEnv *env, jclass that, jint screen)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XRootWindowOfScreen\n");
#endif
    return (jint) XRootWindowOfScreen((Screen *)screen);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XScreenNumberOfScreen
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XScreenNumberOfScreen
  (JNIEnv *env, jclass that, jint screen)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XScreenNumberOfScreen\n");
#endif
    return (jint) XScreenNumberOfScreen((Screen *)screen);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XCreateWindow
 * Signature: (IIIIIIIIIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XCreateWindow
  (JNIEnv *env, jclass that, jint display, jint parent, jint x, jint y, jint width, jint height,
	jint border_width, jint depth, jint class, jint visual, jint value_mask, jint attributes)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XCreateWindow\n");
#endif
    return (jint) XCreateWindow((Display *)display, (Window)parent, x, y, width, height,
        border_width, depth, class, (Visual *)visual, (long)value_mask, (XSetWindowAttributes *)attributes);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XDestroyWindow
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XDestroyWindow
  (JNIEnv *env, jclass that, jint display, jint w)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XDestroyWindow\n");
#endif
    XDestroyWindow((Display *)display, (Window)w);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XpQueryVersion
 * Signature: (I[S[S)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XpQueryVersion
  (JNIEnv *env, jclass that, jint display, jshortArray major_version, jshortArray minor_version)
{
    jshort *major_version1=NULL, *minor_version1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XpQueryVersion\n");
#endif

    if (major_version)
    	major_version1 = (*env)->GetShortArrayElements(env, major_version, NULL);
    if (minor_version)
    	minor_version1 = (*env)->GetShortArrayElements(env, minor_version, NULL);    
    rc = (jint) XpQueryVersion((Display *)display, (short *)major_version1, (short *)minor_version1);
    if (major_version)
    	(*env)->ReleaseShortArrayElements(env, major_version, major_version1, 0);
    if (minor_version)
    	(*env)->ReleaseShortArrayElements(env, minor_version, minor_version1, 0);

    return rc;
}

/*
 * ======== End printing functions ========
 */
 
/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    pipe
 * Signature: ([I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_pipe
  (JNIEnv *env, jclass that, jintArray filedes)
{
    jint *filedes1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "pipe\n");
#endif

    if (filedes) 
        filedes1 = (*env)->GetIntArrayElements(env, filedes, NULL);
    rc = (jint) pipe((int *)filedes1);
    if (filedes)
    	(*env)->ReleaseIntArrayElements(env, filedes, filedes1, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    read
 * Signature: (I[BI)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_read
  (JNIEnv *env, jclass that, int filedes, jbyteArray buf, int nbyte)
{
    jbyte *buf1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "read\n");
#endif

    if (buf) 
        buf1 = (*env)->GetByteArrayElements(env, buf, NULL);
    rc = (jint) read(filedes, (char *)buf1, nbyte);
    if (buf)
    	(*env)->ReleaseByteArrayElements(env, buf, buf1, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    write
 * Signature: (I[BI)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_write
  (JNIEnv *env, jclass that, int filedes, jbyteArray buf, int nbyte)
{
    jbyte *buf1=NULL;
    jint rc;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "write\n");
#endif

    if (buf) 
        buf1 = (*env)->GetByteArrayElements(env, buf, NULL);
    rc = (jint) write(filedes, (char *)buf1, nbyte);
    if (buf)
    	(*env)->ReleaseByteArrayElements(env, buf, buf1, 0);

    return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    close
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_close
  (JNIEnv *env, jclass that, int filedes)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "close\n");
#endif

    return (jint) close(filedes);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtAppAddInput
 * Signature: (IIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_OS_XtAppAddInput
  (JNIEnv *env, jclass that, jint app_context, jint source, jint condition, jint proc, jint client_data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtAppAddInput\n");
#endif

    return (jint) XtAppAddInput((XtAppContext)app_context, source, (XtPointer)condition, (XtInputCallbackProc)proc, (XtPointer)client_data);
}

/*
 * Class:     org_eclipse_swt_internal_motif_OS
 * Method:    XtRemoveInput
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_OS_XtRemoveInput
  (JNIEnv *env, jclass that, jint id)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "XtRemoveInput\n");
#endif

    XtRemoveInput((XtInputId)id);
}
