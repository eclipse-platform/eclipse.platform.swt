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
 * SWT natives for the GTK Drawing Toolkit.
 */ 

#include "swt.h"
#include "structs.h"

#include <stdio.h>
#include <assert.h>

/*  ***** General *****  */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1screen_1width
  (JNIEnv *env, jclass that)
{
	return (jint)gdk_screen_width();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1screen_1height
  (JNIEnv *env, jclass that)
{
	return (jint)gdk_screen_height();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1screen_1width_1mm
  (JNIEnv *env, jclass that)
{
	return (jint)gdk_screen_width_mm();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1flush
  (JNIEnv *env, jclass that)
{
	gdk_flush();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1beep
  (JNIEnv *env, jclass that)
{
	gdk_beep();
}


/*  ***** Points, Rectangles and Regions *****  */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1new
  (JNIEnv *env, jclass that)
{
	return (jint)gdk_region_new();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1destroy
  (JNIEnv *env, jclass that, jint region)
{
	gdk_region_destroy((GdkRegion*)region);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1get_1clipbox
  (JNIEnv *env, jclass that, jint region, jobject rectangle)
{
	DECL_GLOB(pGlob)
	GdkRectangle rectangle_struct, *rectangle1 = NULL;
	if (rectangle) {
		rectangle1 = &rectangle_struct;
		cacheGdkRectangleFids(env, rectangle, &PGLOB(GdkRectangleFc));
		getGdkRectangleFields(env, rectangle, rectangle1, &PGLOB(GdkRectangleFc));
	}
	gdk_region_get_clipbox((GdkRegion*)region, (GdkRectangle*)rectangle1);
	if (rectangle) {
		setGdkRectangleFields(env, rectangle, rectangle1, &PGLOB(GdkRectangleFc));
	}
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1empty
  (JNIEnv *env, jclass that, jint region)
{
	return (jboolean)gdk_region_empty((GdkRegion*)region);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1equal
  (JNIEnv *env, jclass that, jint region1, jint region2)
{
	return (jboolean)gdk_region_equal((GdkRegion*)region1, (GdkRegion*)region2);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1point_1in
  (JNIEnv *env, jclass that, jint region, jint x, jint y)
{
	return (jboolean)gdk_region_point_in((GdkRegion*)region, (int)x, (int)y);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1rect_1in
  (JNIEnv *env, jclass that, jint region, jobject rect)
{
	DECL_GLOB(pGlob)
	jint rc;
	GdkRectangle rect_struct, *rect1 = NULL;
	if (rect) {
		rect1 = &rect_struct;
		cacheGdkRectangleFids(env, rect, &PGLOB(GdkRectangleFc));
		getGdkRectangleFields(env, rect, rect1, &PGLOB(GdkRectangleFc));
	}
	rc = (jint)gdk_region_rect_in((GdkRegion*)region, (GdkRectangle*)rect1);
	if (rect) {
		setGdkRectangleFields(env, rect, rect1, &PGLOB(GdkRectangleFc));
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1region_1union_1with_1rect
  (JNIEnv *env, jclass that, jint region, jobject rect)
{
	DECL_GLOB(pGlob)
	jint rc;
	GdkRectangle rect_struct, *rect1 = NULL;
	if (rect) {
		rect1 = &rect_struct;
		cacheGdkRectangleFids(env, rect, &PGLOB(GdkRectangleFc));
		getGdkRectangleFields(env, rect, rect1, &PGLOB(GdkRectangleFc));
	}
	gdk_region_union_with_rect((GdkRegion*)region, (GdkRectangle*)rect1);
	if (rect) {
		setGdkRectangleFields(env, rect, rect1, &PGLOB(GdkRectangleFc));
	}
	return 0;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1regions_1union
  (JNIEnv *env, jclass that, jint source1, jint source2)
{
	return (jint)gdk_regions_union((GdkRegion*)source1, (GdkRegion*)source2);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1regions_1subtract
  (JNIEnv *env, jclass that, jint source1, jint source2)
{
	return (jint)gdk_regions_subtract((GdkRegion*)source1, (GdkRegion*)source2);
}


/*  ***** Graphics Contexts *****  */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1new
  (JNIEnv *env, jclass that, jint window)
{
	return (jint)gdk_gc_new((GdkWindow*)window);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1unref
  (JNIEnv *env, jclass that, jint gc)
{
	gdk_gc_unref((GdkGC*)gc);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1destroy
  (JNIEnv *env, jclass that, jint gc)
{
	gdk_gc_destroy((GdkGC*)gc);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1get_1values
  (JNIEnv *env, jclass that, jint gc, jobject values)
{
	DECL_GLOB(pGlob)
	GdkGCValues values_struct, *values1 = NULL;
	if (values) {
		values1 = &values_struct;
		cacheGdkGCValuesFids(env, values, &PGLOB(GdkGCValuesFc));
		getGdkGCValuesFields(env, values, values1, &PGLOB(GdkGCValuesFc));
	}
	gdk_gc_get_values((GdkGC*)gc, (GdkGCValues*)values1);
	if (values) {
		setGdkGCValuesFields(env, values, values1, &PGLOB(GdkGCValuesFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1foreground
  (JNIEnv *env, jclass that, jint gc, jobject color)
{
	DECL_GLOB(pGlob)
	GdkColor color_struct, *color1 = NULL;
	if (color) {
		color1 = &color_struct;
		cacheGdkColorFids(env, color, &PGLOB(GdkColorFc));
		getGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	gdk_gc_set_foreground((GdkGC*)gc, (GdkColor*)color1);
	if (color) {
		setGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1background
  (JNIEnv *env, jclass that, jint gc, jobject color)
{
	DECL_GLOB(pGlob)
	GdkColor color_struct, *color1 = NULL;
	if (color) {
		color1 = &color_struct;
		cacheGdkColorFids(env, color, &PGLOB(GdkColorFc));
		getGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	gdk_gc_set_background((GdkGC*)gc, (GdkColor*)color1);
	if (color) {
		setGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1font
  (JNIEnv *env, jclass that, jint gc, jint font)
{
	gdk_gc_set_font((GdkGC*)gc, (GdkFont*)font);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1function
  (JNIEnv *env, jclass that, jint gc, jint function)
{
	gdk_gc_set_function((GdkGC*)gc, (GdkFunction)function);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1fill
  (JNIEnv *env, jclass that, jint gc, jint fill)
{
	gdk_gc_set_fill((GdkGC*)gc, (GdkFill)fill);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1stipple
  (JNIEnv *env, jclass that, jint gc, jint stipple)
{
	gdk_gc_set_stipple((GdkGC*)gc, (GdkPixmap*)stipple);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1clip_1mask
  (JNIEnv *env, jclass that, jint gc, jint mask)
{
	gdk_gc_set_clip_mask((GdkGC*)gc, (GdkBitmap*)mask);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1clip_1rectangle
  (JNIEnv *env, jclass that, jint gc, jobject rectangle)
{
	DECL_GLOB(pGlob)
	GdkRectangle rectangle_struct, *rectangle1 = NULL;
	if (rectangle) {
		rectangle1 = &rectangle_struct;
		cacheGdkRectangleFids(env, rectangle, &PGLOB(GdkRectangleFc));
		getGdkRectangleFields(env, rectangle, rectangle1, &PGLOB(GdkRectangleFc));
	}
	gdk_gc_set_clip_rectangle((GdkGC*)gc, (GdkRectangle*)rectangle1);
	if (rectangle) {
		setGdkRectangleFields(env, rectangle, rectangle1, &PGLOB(GdkRectangleFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1clip_1region
  (JNIEnv *env, jclass that, jint gc, jint region)
{
	gdk_gc_set_clip_region((GdkGC*)gc, (GdkRegion*)region);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1subwindow
  (JNIEnv *env, jclass that, jint gc, jint mode)
{
	gdk_gc_set_subwindow((GdkGC*)gc, (GdkSubwindowMode)mode);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1exposures
  (JNIEnv *env, jclass that, jint gc, jboolean exposures)
{
	gdk_gc_set_exposures((GdkGC*)gc, (gboolean)exposures);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1line_1attributes
  (JNIEnv *env, jclass that, jint gc, jint line_width, jint line_style, jint cap_style, jint join_style)
{
	gdk_gc_set_line_attributes((GdkGC*)gc, (gint)line_width, (GdkLineStyle)line_style, (GdkCapStyle)cap_style, (GdkJoinStyle)join_style);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1gc_1set_1dashes
  (JNIEnv *env, jclass that, jint gc, jint dash_offset, jbyteArray dash_list, jint n)
{
	jbyte *dash_list1 = NULL;
	if (dash_list) {
		dash_list1 = (*env)->GetByteArrayElements(env, dash_list, NULL);
	}
	gdk_gc_set_dashes((GdkGC*)gc, (gint)dash_offset, (gint8*)dash_list1, (gint)n);
	if (dash_list) {
		(*env)->ReleaseByteArrayElements(env, dash_list, dash_list1, 0);
	}
}


/*  ***** Drawing Primitives *****  */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1line
  (JNIEnv *env, jclass that, jint drawable, jint gc, jint x1, jint y1, jint x2, jint y2)
{
	gdk_draw_line((GdkDrawable*)drawable, (GdkGC*)gc, (gint)x1, (gint)y1, (gint)x2, (gint)y2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1rectangle
  (JNIEnv *env, jclass that, jint drawable, jint gc, jint filled, jint x, jint y, jint width, jint height)
{
	gdk_draw_rectangle((GdkDrawable*)drawable, (GdkGC*)gc, (gint)filled, (gint)x, (gint)y, (gint)width, (gint)height);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1arc
  (JNIEnv *env, jclass that, jint drawable, jint gc, jint filled, jint x, jint y, jint width, jint height, jint angle1, jint angle2)
{
	gdk_draw_arc((GdkDrawable*)drawable, (GdkGC*)gc, (gint)filled, (gint)x, (gint)y, (gint)width, (gint)height, (gint)angle1, (gint)angle2);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1polygon
  (JNIEnv *env, jclass that, jint drawable, jint gc, jint filled, jshortArray points, jint npoints)
{
	jshort *points1 = NULL;
	if (points) {
		points1 = (*env)->GetShortArrayElements(env, points, NULL);
	}
	gdk_draw_polygon((GdkDrawable*)drawable, (GdkGC*)gc, (gint)filled, (GdkPoint*)points1, (gint)npoints);
	if (points) {
		(*env)->ReleaseShortArrayElements(env, points, points1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1string
  (JNIEnv *env, jclass that, jint drawable, jint font, jint gc, jint x, jint y, jbyteArray string)
{
	jbyte *string1 = NULL;
	if (string) {
		string1 = (*env)->GetByteArrayElements(env, string, NULL);
	}
	gdk_draw_string((GdkDrawable*)drawable, (GdkFont*)font, (GdkGC*)gc, (gint)x, (gint)y, (gchar*)string1);
	if (string) {
		(*env)->ReleaseByteArrayElements(env, string, string1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1pixmap
  (JNIEnv *env, jclass that, jint drawable, jint gc, jint src, jint xsrc, jint ysrc, jint xdest, jint ydest, jint width, jint height)
{
	gdk_draw_pixmap((GdkDrawable*)drawable, (GdkGC*)gc, (GdkDrawable*)src, (gint)xsrc, (gint)ysrc, (gint)xdest, (gint)ydest, (gint)width, (gint)height);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1draw_1lines
  (JNIEnv *env, jclass that, jint drawable, jint gc, jshortArray points, jint npoints)
{
	jshort *points1 = NULL;
	if (points) {
		points1 = (*env)->GetShortArrayElements(env, points, NULL);
	}
	gdk_draw_lines((GdkDrawable*)drawable, (GdkGC*)gc, (GdkPoint*)points1, (gint)npoints);
	if (points) {
		(*env)->ReleaseShortArrayElements(env, points, points1, 0);
	}
}


/*  ***** Bitmaps and Pixmaps *****  */


JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixmap_1new
  (JNIEnv *env, jclass that, jint window, jint width, jint height, jint depth)
{
	return (jint)gdk_pixmap_new((GdkWindow*)window, (gint)width, (gint)height, (gint)depth);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1bitmap_1create_1from_1data
  (JNIEnv *env, jclass that, jint window, jbyteArray data, jint width, jint height)
{
	jint rc;
	jbyte *data1 = NULL;
	if (data) {
		data1 = (*env)->GetByteArrayElements(env, data, NULL);
	}
	rc = (jint)gdk_bitmap_create_from_data((GdkWindow*)window, (gchar*)data1, (gint)width, (gint)height);
	if (data) {
		(*env)->ReleaseByteArrayElements(env, data, data1, 0);
	}
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1pixmap_1unref
  (JNIEnv *env, jclass that, jint pixmap)
{
	gdk_pixmap_unref((GdkPixmap*)pixmap);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1bitmap_1unref
  (JNIEnv *env, jclass that, jint pixmap)
{
	gdk_bitmap_unref((GdkBitmap*)pixmap);
}


/*  ***** GdkRGB *****  */

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1rgb_1init
  (JNIEnv *env, jclass cl)
{
    gdk_rgb_init();
}


/*  ***** Images *****  */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1image_1get
  (JNIEnv *env, jclass that, jint window, jint x, jint y, jint width, jint height)
{
	return (jint)gdk_image_get((GdkWindow*)window, (gint)x, (gint)y, (gint)width, (gint)height);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1image_1get_1pixel
  (JNIEnv *env, jclass that, jint image, jint x, jint y)
{
	return (jint)gdk_image_get_pixel((GdkImage*)image, (gint)x, (gint)y);
}


/*  ***** Pixbufs *****  */


/*  ***** Colormaps and Colors *****  */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1colormap_1get_1system
  (JNIEnv *env, jclass that)
{
	return (jint)gdk_colormap_get_system();
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1color_1free
  (JNIEnv *env, jclass that, jobject color)
{
	DECL_GLOB(pGlob)
	GdkColor color_struct, *color1 = NULL;
	if (color) {
		color1 = &color_struct;
		cacheGdkColorFids(env, color, &PGLOB(GdkColorFc));
		getGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	gdk_color_free((GdkColor*)color1);
	if (color) {
		setGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1colors_1free
  (JNIEnv *env, jclass that, jint colormap, jintArray pixels, jint npixels, jint planes)
{
	jint *pixels1 = NULL;
	if (pixels) {
		pixels1 = (*env)->GetIntArrayElements(env, pixels, NULL);
	}
	gdk_colors_free((GdkColormap*)colormap, (gulong*)pixels1, (gint)npixels, (gulong)planes);
	if (pixels) {
		(*env)->ReleaseIntArrayElements(env, pixels, pixels1, 0);
	}
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1color_1white
  (JNIEnv *env, jclass that, jint colormap, jobject color)
{
	DECL_GLOB(pGlob)
	jboolean rc;
	GdkColor color_struct, *color1 = NULL;
	if (color) {
		color1 = &color_struct;
		cacheGdkColorFids(env, color, &PGLOB(GdkColorFc));
		getGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	rc = (jboolean)gdk_color_white((GdkColormap*)colormap, (GdkColor*)color1);
	if (color) {
		setGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	return rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1color_1alloc
  (JNIEnv *env, jclass that, jint colormap, jobject color)
{
	DECL_GLOB(pGlob)
	jboolean rc;
	GdkColor color_struct, *color1 = NULL;
	if (color) {
		color1 = &color_struct;
		cacheGdkColorFids(env, color, &PGLOB(GdkColorFc));
		getGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	rc = (jboolean)gdk_color_alloc((GdkColormap*)colormap, (GdkColor*)color1);
	if (color) {
		setGdkColorFields(env, color, color1, &PGLOB(GdkColorFc));
	}
	return rc;
}


/*  ***** Visuals *****  */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1visual_1get_1system
  (JNIEnv *env, jclass that)
{
	return (jint)gdk_visual_get_system();
}


/*  ***** Fonts *****  */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1font_1from_1description
  (JNIEnv *env, jclass that, jint desc)
{
  return (jint) gdk_font_from_description((PangoFontDescription*) desc);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GDK_1FONT_1ASCENT
  (JNIEnv *env, jclass that, jint font)
{
  return (jint) (((GdkFont*) font) -> ascent);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GDK_1FONT_1DESCENT
  (JNIEnv *env, jclass that, jint font)
{
  return (jint) (((GdkFont*) font) -> descent);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1font_1load
  (JNIEnv *env, jclass that, jbyteArray font_name)
{
	jint rc;
	jbyte *font_name1 = NULL;
	if (font_name) {
		font_name1 = (*env)->GetByteArrayElements(env, font_name, NULL);
	}
	rc = (jint)gdk_font_load((gchar*)font_name1);
	if (font_name) {
		(*env)->ReleaseByteArrayElements(env, font_name, font_name1, 0);
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1font_1ref
  (JNIEnv *env, jclass that, jint font)
{
	return (jint)gdk_font_ref((GdkFont*)font);
}

JNIEXPORT jstring JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1font_1full_1name_1get
  (JNIEnv *env, jclass that, jint font)
{
  gchar *name = gdk_font_full_name_get ((GdkFont*)font);
  return NewStringUTF (env, name);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1font_1unref
  (JNIEnv *env, jclass that, jint font)
{
	gdk_font_unref((GdkFont*)font);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1font_1equal
  (JNIEnv *env, jclass that, jint fonta, jint fontb)
{
	return (jboolean)gdk_font_equal((GdkFont*)fonta, (GdkFont*)fontb);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1string_1width
  (JNIEnv *env, jclass that, jint font, jbyteArray string)
{
	jint rc;
	jbyte *string1 = NULL;
	if (string) {
		string1 = (*env)->GetByteArrayElements(env, string, NULL);
	}
	rc = (jint)gdk_string_width((GdkFont*)font, (gchar*)string1);
	if (string) {
		(*env)->ReleaseByteArrayElements(env, string, string1, 0);
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1char_1width
  (JNIEnv *env, jclass that, jint font, jbyte character)
{
	return (jint)gdk_char_width((GdkFont*)font, (gchar)character);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1string_1height
  (JNIEnv *env, jclass that, jint font, jbyteArray string)
{
	jint rc;
	jbyte *string1 = NULL;
	if (string) {
		string1 = (*env)->GetByteArrayElements(env, string, NULL);
	}
	rc = (jint)gdk_string_height((GdkFont*)font, (gchar*)string1);
	if (string) {
		(*env)->ReleaseByteArrayElements(env, string, string1, 0);
	}
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1string_1extents
  (JNIEnv *env, jclass that, jint font, jbyteArray string, jintArray lbearing, jintArray rbearing, jintArray width, jintArray ascent, jintArray descent)
{
	jbyte *string1 = NULL;
	jint *lbearing1 = NULL;
	jint *rbearing1 = NULL;
	jint *width1 = NULL;
	jint *ascent1 = NULL;
	jint *descent1 = NULL;
	if (string) {
		string1 = (*env)->GetByteArrayElements(env, string, NULL);
	}
	if (lbearing) {
		lbearing1 = (*env)->GetIntArrayElements(env, lbearing, NULL);
	}
	if (rbearing) {
		rbearing1 = (*env)->GetIntArrayElements(env, rbearing, NULL);
	}
	if (width) {
		width1 = (*env)->GetIntArrayElements(env, width, NULL);
	}
	if (ascent) {
		ascent1 = (*env)->GetIntArrayElements(env, ascent, NULL);
	}
	if (descent) {
		descent1 = (*env)->GetIntArrayElements(env, descent, NULL);
	}
	gdk_string_extents((GdkFont*)font, (gchar*)string1, (gint*)lbearing1, (gint*)rbearing1, (gint*)width1, (gint*)ascent1, (gint*)descent1);
	if (string) {
		(*env)->ReleaseByteArrayElements(env, string, string1, 0);
	}
	if (lbearing) {
		(*env)->ReleaseIntArrayElements(env, lbearing, lbearing1, 0);
	}
	if (rbearing) {
		(*env)->ReleaseIntArrayElements(env, rbearing, rbearing1, 0);
	}
	if (width) {
		(*env)->ReleaseIntArrayElements(env, width, width1, 0);
	}
	if (ascent) {
		(*env)->ReleaseIntArrayElements(env, ascent, ascent1, 0);
	}
	if (descent) {
		(*env)->ReleaseIntArrayElements(env, descent, descent1, 0);
	}
}


/*  ***** Cursors *****  */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1cursor_1new
  (JNIEnv *env, jclass that, jint cursor_type)
{
	return (jint)gdk_cursor_new((GdkCursorType)cursor_type);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1cursor_1new_1from_1pixmap
  (JNIEnv *env, jclass that, jint source, jint mask, jobject fg, jobject bg, jint x, jint y)
{
	DECL_GLOB(pGlob)
	jint rc;
	GdkColor fg_struct, *fg1 = NULL;
	GdkColor bg_struct, *bg1 = NULL;
	if (fg) {
		fg1 = &fg_struct;
		cacheGdkColorFids(env, fg, &PGLOB(GdkColorFc));
		getGdkColorFields(env, fg, fg1, &PGLOB(GdkColorFc));
	}
	if (bg) {
		bg1 = &bg_struct;
		cacheGdkColorFids(env, bg, &PGLOB(GdkColorFc));
		getGdkColorFields(env, bg, bg1, &PGLOB(GdkColorFc));
	}
	rc = (jint)gdk_cursor_new_from_pixmap((GdkPixmap*)source, (GdkPixmap*)mask, (GdkColor*)fg1, (GdkColor*)bg1, (gint)x, (gint)y);
	if (fg) {
		setGdkColorFields(env, fg, fg1, &PGLOB(GdkColorFc));
	}
	if (bg) {
		setGdkColorFields(env, bg, bg1, &PGLOB(GdkColorFc));
	}
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1cursor_1destroy
  (JNIEnv *env, jclass that, jint cursor)
{
	gdk_cursor_destroy((GdkCursor*)cursor);
}


/*  ***** Windows *****  */


JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1at_1pointer
  (JNIEnv *env, jclass that, jintArray win_x, jintArray win_y)
{
	jint rc;
	jint *win_x1 = NULL;
	jint *win_y1 = NULL;
	if (win_x) {
		win_x1 = (*env)->GetIntArrayElements(env, win_x, NULL);
	}
	if (win_y) {
		win_y1 = (*env)->GetIntArrayElements(env, win_y, NULL);
	}
	rc = (jint)gdk_window_at_pointer((gint*)win_x1, (gint*)win_y1);
	if (win_x) {
		(*env)->ReleaseIntArrayElements(env, win_x, win_x1, 0);
	}
	if (win_y) {
		(*env)->ReleaseIntArrayElements(env, win_y, win_y1, 0);
	}
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1clear_1area
  (JNIEnv *env, jclass that, jint window, jint x, jint y, jint width, jint height)
{
	gdk_window_clear_area((GdkWindow*)window, (gint)x, (gint)y, (gint)width, (gint)height);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1resize
  (JNIEnv *env, jclass that, jint window, jint width, jint height)
{
	gdk_window_resize((GdkWindow*)window, (gint)width, (gint)height);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1move
  (JNIEnv *env, jclass that, jint window, jint x, jint y)
{
	gdk_window_move((GdkWindow*)window, (gint)x, (gint)y);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1clear_1area_1e
  (JNIEnv *env, jclass that, jint window, jint x, jint y, jint width, jint height)
{
	gdk_window_clear_area_e((GdkWindow*)window, (gint)x, (gint)y, (gint)width, (gint)height);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1copy_1area
  (JNIEnv *env, jclass that, jint window, jint gc, jint x, jint y, jint source_window, jint source_x, jint source_y, jint width, jint height)
{
	gdk_window_copy_area((GdkWindow*)window, (GdkGC*)gc, (gint)x, (gint)y, (GdkWindow*)source_window, (gint)source_x, (gint)source_y, (gint)width, (gint)height);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1raise
  (JNIEnv *env, jclass that, jint window)
{
	gdk_window_raise((GdkWindow*)window);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1lower
  (JNIEnv *env, jclass that, jint window)
{
	gdk_window_lower((GdkWindow*)window);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1user_1data
  (JNIEnv *env, jclass that, jint window, jint user_data)
{
	gdk_window_set_user_data((GdkWindow*)window, (gpointer)user_data);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1cursor
  (JNIEnv *env, jclass that, jint window, jint cursor)
{
	gdk_window_set_cursor((GdkWindow*)window, (GdkCursor*)cursor);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1get_1user_1data
  (JNIEnv *env, jclass that, jint window, jintArray data)
{
	jint *data1 = NULL;
	if (data) {
		data1 = (*env)->GetIntArrayElements(env, data, NULL);
	}
	gdk_window_get_user_data((GdkWindow*)window, (gpointer*)data1);
	if (data) {
		(*env)->ReleaseIntArrayElements(env, data, data1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1get_1geometry
  (JNIEnv *env, jclass that, jint window, jintArray x, jintArray y, jintArray width, jintArray height, jintArray depth)
{
	jint *x1 = NULL;
	jint *y1 = NULL;
	jint *width1 = NULL;
	jint *height1 = NULL;
	jint *depth1 = NULL;
	if (x) {
		x1 = (*env)->GetIntArrayElements(env, x, NULL);
	}
	if (y) {
		y1 = (*env)->GetIntArrayElements(env, y, NULL);
	}
	if (width) {
		width1 = (*env)->GetIntArrayElements(env, width, NULL);
	}
	if (height) {
		height1 = (*env)->GetIntArrayElements(env, height, NULL);
	}
	if (depth) {
		depth1 = (*env)->GetIntArrayElements(env, depth, NULL);
	}
	gdk_window_get_geometry((GdkWindow*)window, (gint*)x1, (gint*)y1, (gint*)width1, (gint*)height1, (gint*)depth1);
	if (x) {
		(*env)->ReleaseIntArrayElements(env, x, x1, 0);
	}
	if (y) {
		(*env)->ReleaseIntArrayElements(env, y, y1, 0);
	}
	if (width) {
		(*env)->ReleaseIntArrayElements(env, width, width1, 0);
	}
	if (height) {
		(*env)->ReleaseIntArrayElements(env, height, height1, 0);
	}
	if (depth) {
		(*env)->ReleaseIntArrayElements(env, depth, depth1, 0);
	}
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1drawable_1get_1size
  (JNIEnv *env, jclass that, jint drawable, jintArray width, jintArray height)
{
	jint *width1 = NULL;
	jint *height1 = NULL;
	if (width) {
		width1 = (*env)->GetIntArrayElements(env, width, NULL);
	}
	if (height) {
		height1 = (*env)->GetIntArrayElements(env, height, NULL);
	}
	gdk_drawable_get_size((GdkDrawable*)drawable, (gint*)width1, (gint*)height1);
	if (width) {
		(*env)->ReleaseIntArrayElements(env, width, width1, 0);
	}
	if (height) {
		(*env)->ReleaseIntArrayElements(env, height, height1, 0);
	}
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1drawable_1get_1depth
  (JNIEnv *env, jclass that, jint drawable)
{
  return (jint)gdk_drawable_get_depth((GdkDrawable*)drawable);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1get_1origin
  (JNIEnv *env, jclass that, jint window, jintArray x, jintArray y)
{
	jint rc;
	jint *x1 = NULL;
	jint *y1 = NULL;
	if (x) {
		x1 = (*env)->GetIntArrayElements(env, x, NULL);
	}
	if (y) {
		y1 = (*env)->GetIntArrayElements(env, y, NULL);
	}
	rc = (jint)gdk_window_get_origin((GdkWindow*)window, (gint*)x1, (gint*)y1);
	if (x) {
		(*env)->ReleaseIntArrayElements(env, x, x1, 0);
	}
	if (y) {
		(*env)->ReleaseIntArrayElements(env, y, y1, 0);
	}
	return rc;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1get_1pointer
  (JNIEnv *env, jclass that, jint window, jintArray x, jintArray y, jint mask)
{
	jint rc;
	jint *x1 = NULL;
	jint *y1 = NULL;
	if (x) {
		x1 = (*env)->GetIntArrayElements(env, x, NULL);
	}
	if (y) {
		y1 = (*env)->GetIntArrayElements(env, y, NULL);
	}
	rc = (jint)gdk_window_get_pointer((GdkWindow*)window, (gint*)x1, (gint*)y1, (GdkModifierType*)mask);
	if (x) {
		(*env)->ReleaseIntArrayElements(env, x, x1, 0);
	}
	if (y) {
		(*env)->ReleaseIntArrayElements(env, y, y1, 0);
	}
	return rc;
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1icon
  (JNIEnv *env, jclass that, jint window, jint icon_window, jint pixmap, jint mask)
{
	gdk_window_set_icon((GdkWindow*)window, (GdkWindow*)icon_window, (GdkPixmap*)pixmap, (GdkBitmap*)mask);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1set_1decorations
  (JNIEnv *env, jclass that, jint window, jint decorations)
{
	gdk_window_set_decorations((GdkWindow*)window, (GdkWMDecoration)decorations);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1window_1show
  (JNIEnv *env, jclass that, jint window)
{
	gdk_window_show((GdkWindow*)window);
}


/*  ***** Events *****  */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1get
  (JNIEnv *env, jclass that)
{
	return (jint)gdk_event_get();
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1button_1get_1button
  (JNIEnv *env, jclass that, jint event)
{
  return (jint) (((GdkEventButton*)event) -> button);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1key_1get_1keyval
  (JNIEnv *env, jclass that, jint event)
{
  return (jint) (((GdkEventKey*)event) -> keyval);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1key_1get_1length
  (JNIEnv *env, jclass that, jint event)
{
  return (jint) (((GdkEventKey*)event) -> length);
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GDK_1EVENT_1TYPE
  (JNIEnv *env, jclass that, jint event)
{
  GdkEventType type = ((GdkEventAny*)event) -> type;
  return (jint) type;
}

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1get_1time
  (JNIEnv *env, jclass that, jint event)
{
  return (jint) gdk_event_get_time((GdkEvent*) event);
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1get_1state
  (JNIEnv *env, jclass that, jint event, jintArray state)
{
  gboolean result;
  jint *state1 = NULL;
  if (state) {
    state1 = (*env)->GetIntArrayElements(env, state, NULL);
  }
  result = gdk_event_get_state((GdkEvent*)event, (GdkModifierType*)state1);
  if (state) {
    (*env)->ReleaseIntArrayElements(env, state, state1, 0);
  }
  return (jboolean) result;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1get_1coords
  (JNIEnv *env, jclass that, jint event, jdoubleArray win_x, jdoubleArray win_y)
{
  gboolean rc;
  jdouble *win_x1 = NULL;
  jdouble *win_y1 = NULL;
  if (win_x) {
	win_x1 = (*env)->GetDoubleArrayElements(env, win_x, NULL);
  }
  if (win_y) {
	win_y1 = (*env)->GetDoubleArrayElements(env, win_y, NULL);
  }
  rc = gdk_event_get_coords((GdkEvent*)event, (gdouble*)win_x1, (gdouble*)win_y1);
  if (win_x) {
  	(*env)->ReleaseDoubleArrayElements(env, win_x, win_x1, 0);
  }
  if (win_y) {
	(*env)->ReleaseDoubleArrayElements(env, win_y, win_y1, 0);
  }
  return (jboolean)rc;
}

JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1get_1root_1coords
  (JNIEnv *env, jclass that, jint event, jdoubleArray win_x, jdoubleArray win_y)
{
  gboolean rc;
  jdouble *win_x1 = NULL;
  jdouble *win_y1 = NULL;
  if (win_x) {
	win_x1 = (*env)->GetDoubleArrayElements(env, win_x, NULL);
  }
  if (win_y) {
	win_y1 = (*env)->GetDoubleArrayElements(env, win_y, NULL);
  }
  rc = gdk_event_get_root_coords((GdkEvent*)event, (gdouble*)win_x1, (gdouble*)win_y1);
  if (win_x) {
  	(*env)->ReleaseDoubleArrayElements(env, win_x, win_x1, 0);
  }
  if (win_y) {
	(*env)->ReleaseDoubleArrayElements(env, win_y, win_y1, 0);
  }
  return (jboolean)rc;
}


JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1get_1graphics_1expose
  (JNIEnv *env, jclass that, jint window)
{
	return (jint)gdk_event_get_graphics_expose((GdkWindow*)window);
}

JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1event_1free
  (JNIEnv *env, jclass that, jint event)
{
	gdk_event_free((GdkEvent*)event);
}


/*  ***** Properties and Atoms *****  */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_gdk_1atom_1intern
  (JNIEnv *env, jclass that, jbyteArray atom_name, jint only_if_exists)
{
	jint rc;
	jbyte *atom_name1 = NULL;
	if (atom_name) {
		atom_name1 = (*env)->GetByteArrayElements(env, atom_name, NULL);
	}
	rc = (jint)gdk_atom_intern((gchar*)atom_name1, (gint)only_if_exists);
	if (atom_name) {
		(*env)->ReleaseByteArrayElements(env, atom_name, atom_name1, 0);
	}
	return rc;
}


/*  ***** X Window System Interaction *****  */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GDK_1ROOT_1PARENT
  (JNIEnv *env, jclass that)
{
	return (jint) GDK_ROOT_PARENT();
}


/* Misc */

JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_OS_GDK_1CURRENT_1TIME
  (JNIEnv *env, jclass that)
{
  return (jint) GDK_CURRENT_TIME;
}
