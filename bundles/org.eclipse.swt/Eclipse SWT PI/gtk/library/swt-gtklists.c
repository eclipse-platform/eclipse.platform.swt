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


#include "swt.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1post_1recursive_1to_1depth
  (JNIEnv *env, jclass that, jint ctree, jint node, jint depth, jint func, jint data)
{
	gtk_ctree_post_recursive_to_depth((GtkCTree*)ctree, (GtkCTreeNode*)node, (jint)depth, (GtkCTreeFunc)func, (gpointer)data);
}


JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1get_1pixtext
  (JNIEnv *env, jclass that, jint clist, jint row, jint column, jintArray text, jintArray spacing, jintArray pixmap, jintArray mask)
{
    jint *text1 = NULL;
    jint *spacing1 = NULL;
    jint *pixmap1 = NULL;
    jint *mask1 = NULL;
    int rc;
    
    if (text) text1 = (*env)->GetIntArrayElements(env, text, NULL);
    if (spacing) spacing1 = (*env)->GetIntArrayElements(env, spacing, NULL);
    if (pixmap) pixmap1 = (*env)->GetIntArrayElements(env, pixmap, NULL);
    if (mask) mask1 = (*env)->GetIntArrayElements(env, mask, NULL);
    rc = gtk_clist_get_pixtext((GtkCList*)clist, row, column, (gchar**)text1, (guint8*)spacing1, (GdkPixmap**)pixmap1, (GdkBitmap**)mask1);
    if (text) (*env)->ReleaseIntArrayElements(env, text, text1, 0);
    if (spacing) (*env)->ReleaseIntArrayElements(env, spacing, spacing1, 0);
    if (pixmap) (*env)->ReleaseIntArrayElements(env, pixmap, pixmap1, 0);
    if (mask)   (*env)->ReleaseIntArrayElements(env, mask, mask1, 0);
    
    return (jint) rc;
}


/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_row_is_visible
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1row_1is_1visible
  (JNIEnv *env, jclass that, jint clist, jint row)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_row_is_visible");
#endif

	return gtk_clist_row_is_visible((GtkCList *)clist, (gint)row);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_post_recursive
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1post_1recursive
  (JNIEnv *env, jclass that, jint ctree, jint node, jint func, jint data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_post_recursive");
#endif

	gtk_ctree_post_recursive((GtkCTree*)ctree, (GtkCTreeNode*)node, (GtkCTreeFunc)func, (gpointer)data);
}


/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_node_nth
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1nth
  (JNIEnv *env, jclass that, jint ctree, jint row)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_node_nth");
#endif

	return (jint)gtk_ctree_node_nth((GtkCTree*)ctree, row);
}



/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_remove_node
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1remove_1node
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_remove_node");
#endif

	gtk_ctree_remove_node((GtkCTree*)ctree, (GtkCTreeNode*)node);
}


/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_is_viewable
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1is_1viewable
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_is_viewable");
#endif

	return (jboolean)gtk_ctree_is_viewable((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_is_hot_spot
 * Signature:	
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1is_1hot_1spot
  (JNIEnv *env, jclass that, jint ctree, jint x, jint y)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_is_hot_spot");
#endif

	return (jboolean)gtk_ctree_is_hot_spot((GtkCTree*)ctree, (gint)x, (gint)y);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_expand
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1expand
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_expand");
#endif

	gtk_ctree_expand((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_collapse
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1collapse
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_collapse");
#endif

	gtk_ctree_collapse((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_select
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1select
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_select");
#endif

	gtk_ctree_select((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_select_recursive
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1select_1recursive
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_select_recursive");
#endif

	gtk_ctree_select_recursive((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_unselect_recursive
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1unselect_1recursive
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_unselect_recursive");
#endif

	gtk_ctree_unselect_recursive((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_set_node_info
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1set_1node_1info
  (JNIEnv *env, jclass that, jint ctree, jint node, jbyteArray text, jbyte spacing, jint pixmap_closed, jint mask_closed, jint pixmap_opened, jint mask_opened, jboolean is_leaf, jboolean expanded)
{
	jbyte *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_set_node_info");
#endif

	if (text) {
		text1 = (*env)->GetByteArrayElements(env, text, NULL);
	}
	gtk_ctree_set_node_info((GtkCTree*)ctree, (GtkCTreeNode*)node, (gchar*)text1, (guint8)spacing, (GdkPixmap*)pixmap_closed, (GdkBitmap*)mask_closed, (GdkPixmap*)pixmap_opened, (GdkBitmap*)mask_opened, (gboolean)is_leaf, (gboolean)expanded);
	if (text) {
		(*env)->ReleaseByteArrayElements(env, text, text1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_get_node_info
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1get_1node_1info
  (JNIEnv *env, jclass that, jint ctree, jint node, jintArray text, jbyteArray spacing, jintArray pixmap_closed, jintArray mask_closed, jintArray pixmap_opened, jintArray mask_opened, jbooleanArray is_leaf, jbooleanArray expanded)
{
	jint rc;
	jint *text1 = NULL;
	jbyte *spacing1 = NULL;
	jint *pixmap_closed1 = NULL;
	jint *mask_closed1 = NULL;
	jint *pixmap_opened1 = NULL;
	jint *mask_opened1 = NULL;
	jboolean *is_leaf1 = NULL;
	jboolean *expanded1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_get_node_info");
#endif

	if (text) {
		text1 = (*env)->GetIntArrayElements(env, text, NULL);
	}
	if (spacing) {
		spacing1 = (*env)->GetByteArrayElements(env, spacing, NULL);
	}
	if (pixmap_closed) {
		pixmap_closed1 = (*env)->GetIntArrayElements(env, pixmap_closed, NULL);
	}
	if (mask_closed) {
		mask_closed1 = (*env)->GetIntArrayElements(env, mask_closed, NULL);
	}
	if (pixmap_opened) {
		pixmap_opened1 = (*env)->GetIntArrayElements(env, pixmap_opened, NULL);
	}
	if (mask_opened) {
		mask_opened1 = (*env)->GetIntArrayElements(env, mask_opened, NULL);
	}
	if (is_leaf) {
		is_leaf1 = (*env)->GetBooleanArrayElements(env, is_leaf, NULL);
	}
	if (expanded) {
		expanded1 = (*env)->GetBooleanArrayElements(env, expanded, NULL);
	}
	rc = (jint)gtk_ctree_get_node_info((GtkCTree*)ctree, (GtkCTreeNode*)node, (gchar**)text1, (guint8*)spacing1, (GdkPixmap**)pixmap_closed1, (GdkBitmap**)mask_closed1, (GdkPixmap**)pixmap_opened1, (GdkBitmap**)mask_opened1, (gboolean*)is_leaf1, (gboolean*)expanded1);
	if (text) {
		(*env)->ReleaseIntArrayElements(env, text, text1, 0);
	}
	if (spacing) {
		(*env)->ReleaseByteArrayElements(env, spacing, spacing1, 0);
	}
	if (pixmap_closed) {
		(*env)->ReleaseIntArrayElements(env, pixmap_closed, pixmap_closed1, 0);
	}
	if (mask_closed) {
		(*env)->ReleaseIntArrayElements(env, mask_closed, mask_closed1, 0);
	}
	if (pixmap_opened) {
		(*env)->ReleaseIntArrayElements(env, pixmap_opened, pixmap_opened1, 0);
	}
	if (mask_opened) {
		(*env)->ReleaseIntArrayElements(env, mask_opened, mask_opened1, 0);
	}
	if (is_leaf) {
		(*env)->ReleaseBooleanArrayElements(env, is_leaf, is_leaf1, 0);
	}
	if (expanded) {
		(*env)->ReleaseBooleanArrayElements(env, expanded, expanded1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_node_get_row_style
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1get_1row_1style
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_node_get_row_style");
#endif

	return (jint)gtk_ctree_node_get_row_style((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_node_set_row_data
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1set_1row_1data
  (JNIEnv *env, jclass that, jint ctree, jint node, jint data)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_node_set_row_data");
#endif

	gtk_ctree_node_set_row_data((GtkCTree*)ctree, (GtkCTreeNode*)node, (gpointer)data);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_node_get_row_data
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1get_1row_1data
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_node_get_row_data");
#endif

	return (jint)gtk_ctree_node_get_row_data((GtkCTree*)ctree, (GtkCTreeNode*)node);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_node_moveto
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1moveto
  (JNIEnv *env, jclass that, jint ctree, jint node, jint column, jfloat row_align, jfloat col_align)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_node_moveto");
#endif

	gtk_ctree_node_moveto((GtkCTree*)ctree, (GtkCTreeNode*)node, (gint)column, row_align, col_align);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_node_is_visible
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1node_1is_1visible
  (JNIEnv *env, jclass that, jint ctree, jint node)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_node_is_visible");
#endif

	return (jint)gtk_ctree_node_is_visible((GtkCTree*)ctree, (GtkCTreeNode*)node);
}


JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1new
  (JNIEnv *env, jclass that, jint columns)
{
	return (jint)gtk_clist_new((gint)columns);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1shadow_1type
  (JNIEnv *env, jclass that, jint clist, jint type)
{
	gtk_clist_set_shadow_type((GtkCList*)clist, (GtkShadowType)type);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1selection_1mode
  (JNIEnv *env, jclass that, jint clist, jint mode)
{
	gtk_clist_set_selection_mode((GtkCList*)clist, (GtkSelectionMode)mode);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1freeze
  (JNIEnv *env, jclass that, jint clist)
{
	gtk_clist_freeze((GtkCList*)clist);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1thaw
  (JNIEnv *env, jclass that, jint clist)
{
	gtk_clist_thaw((GtkCList*)clist);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1column_1titles_1show
  (JNIEnv *env, jclass that, jint clist)
{
	gtk_clist_column_titles_show((GtkCList*)clist);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1column_1titles_1hide
  (JNIEnv *env, jclass that, jint clist)
{
	gtk_clist_column_titles_hide((GtkCList*)clist);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1column_1title_1passive
  (JNIEnv *env, jclass that, jint clist, jint column)
{
	gtk_clist_column_title_passive((GtkCList*)clist, (gint)column);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1column_1titles_1passive
  (JNIEnv *env, jclass that, jint clist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_column_titles_passive");
#endif

	gtk_clist_column_titles_passive((GtkCList*)clist);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_column_title
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1title
  (JNIEnv *env, jclass that, jint clist, jint column, jbyteArray title)
{
	jbyte *title1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_column_title");
#endif

	if (title) {
		title1 = (*env)->GetByteArrayElements(env, title, NULL);
	}
	gtk_clist_set_column_title((GtkCList*)clist, (gint)column, (gchar*)title1);
	if (title) {
		(*env)->ReleaseByteArrayElements(env, title, title1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_column_justification
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1justification
  (JNIEnv *env, jclass that, jint clist, jint column, jint justification)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_column_justification");
#endif

	gtk_clist_set_column_justification((GtkCList*)clist, (gint)column, (GtkJustification)justification);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_column_visibility
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1visibility
  (JNIEnv *env, jclass that, jint clist, jint column, jboolean visible)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_column_visibility");
#endif

	gtk_clist_set_column_visibility((GtkCList*)clist, (gint)column, (gboolean)visible);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_column_resizeable
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1resizeable
  (JNIEnv *env, jclass that, jint clist, jint column, jboolean resizeable)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_column_resizeable");
#endif

	gtk_clist_set_column_resizeable((GtkCList*)clist, (gint)column, (gboolean)resizeable);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_column_width
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1column_1width
  (JNIEnv *env, jclass that, jint clist, jint column, jint width)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_column_width");
#endif

	gtk_clist_set_column_width((GtkCList*)clist, (gint)column, (gint)width);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_moveto
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1moveto
  (JNIEnv *env, jclass that, jint clist, jint row, jint column, jfloat row_align, jfloat col_align)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_moveto");
#endif

	gtk_clist_moveto((GtkCList*)clist, (gint)row, (gint)column, row_align, col_align);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_text
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1text
  (JNIEnv *env, jclass that, jint clist, jint row, jint column, jbyteArray text)
{
	jbyte *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_text");
#endif

	if (text) {
		text1 = (*env)->GetByteArrayElements(env, text, NULL);
	}
	gtk_clist_set_text((GtkCList*)clist, (gint)row, (gint)column, (gchar*)text1);
	if (text) {
		(*env)->ReleaseByteArrayElements(env, text, text1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_get_text
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1get_1text
  (JNIEnv *env, jclass that, jint clist, jint row, jint column, jintArray text)
{
	jint rc;
	jint *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_get_text");
#endif

	if (text) {
		text1 = (*env)->GetIntArrayElements(env, text, NULL);
	}
	rc = (jint)gtk_clist_get_text((GtkCList*)clist, (gint)row, (gint)column, (gchar**)text1);
	if (text) {
		(*env)->ReleaseIntArrayElements(env, text, text1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_pixmap
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1pixmap
  (JNIEnv *env, jclass that, jint clist, jint row, jint column, jint pixmap, jint mask)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_pixmap");
#endif

	gtk_clist_set_pixmap((GtkCList*)clist, (gint)row, (gint)column, (GdkPixmap*)pixmap, (GdkBitmap*)mask);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_set_pixtext
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1set_1pixtext
  (JNIEnv *env, jclass that, jint clist, jint row, jint column, jbyteArray text, jbyte spacing, jint pixmap, jint mask)
{
	jbyte *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_set_pixtext");
#endif

	if (text) {
		text1 = (*env)->GetByteArrayElements(env, text, NULL);
	}
	gtk_clist_set_pixtext((GtkCList*)clist, (gint)row, (gint)column, (gchar*)text1, (guint8)spacing, (GdkPixmap*)pixmap, (GdkBitmap*)mask);
	if (text) {
		(*env)->ReleaseByteArrayElements(env, text, text1, 0);
	}
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_append
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1append
  (JNIEnv *env, jclass that, jint clist, jintArray text)
{
	jint rc;
	jint *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_append");
#endif

	if (text) {
		text1 = (*env)->GetIntArrayElements(env, text, NULL);
	}
	rc = (jint)gtk_clist_append((GtkCList*)clist, (gchar**)text1);
	if (text) {
		(*env)->ReleaseIntArrayElements(env, text, text1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_insert
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1insert
  (JNIEnv *env, jclass that, jint clist, jint row, jintArray text)
{
	jint rc;
	jint *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_insert");
#endif

	if (text) {
		text1 = (*env)->GetIntArrayElements(env, text, NULL);
	}
	rc = (jint)gtk_clist_insert((GtkCList*)clist, (gint)row, (gchar**)text1);
	if (text) {
		(*env)->ReleaseIntArrayElements(env, text, text1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_remove
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1remove
  (JNIEnv *env, jclass that, jint clist, jint row)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_remove");
#endif

	gtk_clist_remove((GtkCList*)clist, (gint)row);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_select_row
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1select_1row
  (JNIEnv *env, jclass that, jint clist, jint row, jint column)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_select_row");
#endif

	gtk_clist_select_row((GtkCList*)clist, (gint)row, (gint)column);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_unselect_row
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1unselect_1row
  (JNIEnv *env, jclass that, jint clist, jint row, jint column)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_unselect_row");
#endif

	gtk_clist_unselect_row((GtkCList*)clist, (gint)row, (gint)column);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_clear
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1clear
  (JNIEnv *env, jclass that, jint clist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_clear");
#endif

	gtk_clist_clear((GtkCList*)clist);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_get_selection_info
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1get_1selection_1info
  (JNIEnv *env, jclass that, jint clist, jint x, jint y, jintArray row, jintArray column)
{
	jint rc;
	jint *row1 = NULL;
	jint *column1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_get_selection_info");
#endif

	if (row) {
		row1 = (*env)->GetIntArrayElements(env, row, NULL);
	}
	if (column) {
		column1 = (*env)->GetIntArrayElements(env, column, NULL);
	}
	rc = (jint)gtk_clist_get_selection_info((GtkCList*)clist, (gint)x, (gint)y, (gint*)row1, (gint*)column1);
	if (row) {
		(*env)->ReleaseIntArrayElements(env, row, row1, 0);
	}
	if (column) {
		(*env)->ReleaseIntArrayElements(env, column, column1, 0);
	}
	return rc;
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_select_all
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1select_1all
  (JNIEnv *env, jclass that, jint clist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_select_all");
#endif

	gtk_clist_select_all((GtkCList*)clist);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_clist_unselect_all
 * Signature:	
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1clist_1unselect_1all
  (JNIEnv *env, jclass that, jint clist)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_clist_unselect_all");
#endif

	gtk_clist_unselect_all((GtkCList*)clist);
}




/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_new
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1new
  (JNIEnv *env, jclass that, jint columns, jint tree_column)
{
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_new");
#endif

	return (jint)gtk_ctree_new((gint)columns, (gint)tree_column);
}

/*
 * Class:	org_eclipse_swt_internal_gtk_OS
 * Method:	gtk_ctree_insert_node
 * Signature:	
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gtk_1ctree_1insert_1node
  (JNIEnv *env, jclass that, jint ctree, jint parent, jint sibling, jintArray text, jbyte spacing, jint pixmap_closed, jint mask_closed, jint pixmap_opened, jint mask_opened, jboolean is_leaf, jboolean expanded)
{
	jint rc;
	jint *text1 = NULL;
#ifdef DEBUG_CALL_PRINTS
	fprintf(stderr, "gtk_ctree_insert_node");
#endif

	if (text) {
		text1 = (*env)->GetIntArrayElements(env, text, NULL);
	}
	rc = (jint)gtk_ctree_insert_node((GtkCTree*)ctree, (GtkCTreeNode*)parent, (GtkCTreeNode*)sibling, (gchar**)text1, (guint8)spacing, (GdkPixmap*)pixmap_closed, (GdkBitmap*)mask_closed, (GdkPixmap*)pixmap_opened, (GdkBitmap*)mask_opened, (gboolean)is_leaf, (gboolean)expanded);
	if (text) {
		(*env)->ReleaseIntArrayElements(env, text, text1, 0);
	}
	return rc;
}







