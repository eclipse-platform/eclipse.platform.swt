/*
 * (c) Copyright IBM Corp., 2000, 2001
 * All Rights Reserved.
 */

/**
 * SWT GNOME natives implementation.
 */ 

/* #define PRINT_FAILED_RCODES */
#define NDEBUG 

#include "swt.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>
#include <gnome.h>

/*
 * Class:     org_eclipse_swt_internal_motif_GNOME
 * Method:    g_1get_1home_1dir
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_g_1get_1home_1dir
  (JNIEnv *env, jclass that)
{
	jint rc;
#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "g_1get_1home_1dir\n");
#endif
	rc = (jint) g_get_home_dir();
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_GNOME
 * Method:    gnome_1datadir_1file
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1datadir_1file
  (JNIEnv * env, jclass that, jbyteArray fileName)
{
    	jbyte *fileName1 = NULL;
	jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "gnome_1datadir_1file\n");
#endif

	if (fileName) fileName1 = (*env)->GetByteArrayElements(env, fileName, NULL); 
	rc = (jint) gnome_datadir_file(fileName1);
  	if (fileName) (*env)->ReleaseByteArrayElements(env, fileName, fileName1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_GNOME
 * Method:    gnome_1desktop_1entry_1load
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1desktop_1entry_1load
  (JNIEnv * env, jclass that, jbyteArray fileName)
{
    	jbyte *fileName1 = NULL;
	jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "gnome_1desktop_1entry_1load\n");
#endif

	if (fileName) fileName1 = (*env)->GetByteArrayElements(env, fileName, NULL); 
	rc = (jint) gnome_desktop_entry_load(fileName1);
  	if (fileName) (*env)->ReleaseByteArrayElements(env, fileName, fileName1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_GNOME
 * Method:    gnome_1desktop_1entry_1free
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1desktop_1entry_1free
  (JNIEnv * env, jclass that, jint entry)
{

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "gnome_1desktop_1entry_1free\n");
#endif

	gnome_desktop_entry_free((GnomeDesktopEntry*)entry);
}

/*
 * Class:     org_eclipse_swt_internal_motif_GNOME
 * Method:    gnome_1mime_1get_1value
 * Signature: ([B[B)I
 */
JNIEXPORT int JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1mime_1get_1value
  (JNIEnv *env, jclass that, jbyteArray mimeType, jbyteArray key)
{
    	jbyte *mimeType1 = NULL;
    	jbyte *key1 = NULL;
	jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "gnome_1mime_1get_1value\n");
#endif

	if (mimeType) mimeType1 = (*env)->GetByteArrayElements(env, mimeType, NULL); 
	if (key) key1 = (*env)->GetByteArrayElements(env, key, NULL); 
	rc = (jint) gnome_mime_get_value(mimeType1, key1);
	if (mimeType) (*env)->ReleaseByteArrayElements(env, mimeType, mimeType1, 0);
	if (key) (*env)->ReleaseByteArrayElements(env, key, key1, 0);
	return rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_GNOME
 * Method:    gnome_1mime_1type
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1mime_1type
  (JNIEnv *env, jclass that, jbyteArray fileName)
{
    	jbyte *fileName1 = NULL;
	const char* rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "gnome_1mime_1type\n");
#endif

	if (fileName) fileName1 = (*env)->GetByteArrayElements(env, fileName, NULL); 
	rc = gnome_mime_type(fileName1);
  	if (fileName) (*env)->ReleaseByteArrayElements(env, fileName, fileName1, 0);
	return (jint) rc;
}

/*
 * Class:     org_eclipse_swt_internal_motif_GNOME
 * Method:    gnome_1pixmap_1file
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_motif_GNOME_gnome_1pixmap_1file
  (JNIEnv * env, jclass that, jbyteArray fileName)
{
    	jbyte *fileName1 = NULL;
	jint rc;

#ifdef DEBUG_CALL_PRINTS
    fprintf(stderr, "gnome_1pixmap_1file\n");
#endif

	if (fileName) fileName1 = (*env)->GetByteArrayElements(env, fileName, NULL); 
	rc = (jint) gnome_pixmap_file(fileName1);
  	if (fileName) (*env)->ReleaseByteArrayElements(env, fileName, fileName1, 0);
	return rc;
}
