/*
 * Copyright (c) IBM Corp. 2000, 2001.  All rights reserved.
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
#include <gtk/gtk.h>
#include <gdk-pixbuf/gdk-pixbuf.h>

/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_get_colorspace
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1get_1colorspace
  (JNIEnv *env, jclass cl, jint pixbuf)
{
    return (jint) gdk_pixbuf_get_colorspace((GdkPixbuf*)pixbuf);
}

/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_get_n_channels
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1get_1n_1channels
  (JNIEnv *env, jclass cl, jint pixbuf)
{
    return (jint) gdk_pixbuf_get_n_channels((GdkPixbuf*)pixbuf);
}

/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_get_has_alpha
 * Signature: (I)Z
 */
JNIEXPORT jboolean JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1get_1has_1alpha
  (JNIEnv *env, jclass cl, jint pixbuf)
{
    return (jboolean) gdk_pixbuf_get_has_alpha((GdkPixbuf*)pixbuf);
}

/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_get_bits_per_sample
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1get_1bits_1per_1sample
  (JNIEnv *env, jclass cl, jint pixbuf)
{
    return (jint) gdk_pixbuf_get_bits_per_sample((GdkPixbuf*)pixbuf);
}

/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_get_pixels
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1get_1pixels
  (JNIEnv *env, jclass cl, jint pixbuf)
{
    return (jint) gdk_pixbuf_get_pixels((GdkPixbuf*)pixbuf);
}

/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_get_width
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1get_1width
  (JNIEnv *env, jclass cl, jint pixbuf)
{
    return (jint) gdk_pixbuf_get_width((GdkPixbuf*)pixbuf);
}

/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_get_height
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1get_1height
  (JNIEnv *env, jclass cl, jint pixbuf)
{
    return (jint) gdk_pixbuf_get_height((GdkPixbuf*)pixbuf);
}

/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_get_rowstride
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1get_1rowstride
  (JNIEnv *env, jclass cl, jint pixbuf)
{
    return (jint) gdk_pixbuf_get_rowstride((GdkPixbuf*)pixbuf);
}

/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_new
 * Signature: (IZIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1new
  (JNIEnv *env, jclass cl,
   jint colorspace,
   jboolean hasAlpha,
   jint bpc,
   jint width, jint height)
{
    return (jint) gdk_pixbuf_new (
	colorspace,
	(gboolean) hasAlpha,
	bpc,
	width, height
        );
}

/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_copy
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1copy
  (JNIEnv *env, jclass cl, jint source) {
    return (jint)gdk_pixbuf_copy ((GdkPixbuf*) source);
}


/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_render_to_drawable
 * Signature: (IIIIIIIIIIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1render_1to_1drawable
  (JNIEnv *env, jclass cl,
   jint pixbuf,
   jint drawable,
   jint gc,
   jint src_x, jint src_y,
   jint dest_x, jint dest_y,
   jint width, jint height,
   jint dithering,
   jint x_dither, jint y_dither) {
    gdk_pixbuf_render_to_drawable (
	(GdkPixbuf*) pixbuf,
	(GdkDrawable*) drawable,
	(GdkGC*) gc,
	src_x, src_y,
	dest_x, dest_y,
	width, height,
	dithering,
	x_dither, y_dither
        );
}


/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_render_to_drawable_alpha
 * Signature: (IIIIIIIIIIIII)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1render_1to_1drawable_1alpha
  (JNIEnv *env, jclass cl,
   jint pixbuf,
   jint drawable,
   jint src_x, jint src_y, jint dest_x, jint dest_y,
   jint width, jint height,
   jint alphaMode,
   jint alphaThreshold,
   jint dithering,
   jint x_dither, jint y_dither)
{
    gdk_pixbuf_render_to_drawable_alpha (
	(GdkPixbuf*) pixbuf,
	(GdkDrawable*) drawable,
	src_x, src_y,
	dest_x, dest_y,
	width, height,
        alphaMode,
	alphaThreshold,
	dithering,
	x_dither, y_dither
        );
}

/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_scale
 * Signature: (IIIIIIDDDDI)V
 */
JNIEXPORT void JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1scale
  (JNIEnv *env, jclass cl,
   jint source, jint dest,
   jint dest_x, jint dest_y,
   jint dest_width, jint dest_height,
   jdouble offset_x, jdouble offset_y,
   jdouble scale_x,  jdouble scale_y,
   jint interp_type) {
    gdk_pixbuf_scale ((GdkPixbuf*)source, (GdkPixbuf*)dest,
	dest_x, dest_y,
	dest_width, dest_height,
	offset_x, offset_y,
	scale_x,  scale_y,
	interp_type
    );
}

/*
 * Class:     org_eclipse_swt_internal_gtk_GDKPIXBUF
 * Method:    gdk_pixbuf_get_from_drawable
 * Signature: (IIIIIIIII)I
 */
JNIEXPORT jint JNICALL Java_org_eclipse_swt_internal_gtk_GDKPIXBUF_gdk_1pixbuf_1get_1from_1drawable
  (JNIEnv *env, jclass cl,
   jint dest, jint src, jint cmap,
   jint src_x, jint src_y,
   jint dest_x, jint dest_y,
   jint width, jint height)
{
    gdk_pixbuf_get_from_drawable (
	(GdkPixbuf*) dest,
	(GdkDrawable*) src,
	(GdkColormap*) cmap,
	src_x, src_y,
	dest_x, dest_y,
	width, height);
}
