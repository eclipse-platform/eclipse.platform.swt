/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Cairo and SWT
 * -  Copyright (C) 2005 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */

#include "swt.h"
#include "cairo_structs.h"
#include "cairo_stats.h"

#define Cairo_NATIVE(func) Java_org_eclipse_swt_internal_cairo_Cairo_##func

#ifndef NO_cairo_1add_1path
void moveTo(cairo_t *cairo, double x, double y) {
	cairo_move_to(cairo, x, y);
}

void lineTo(cairo_t *cairo, double x, double y) {
	cairo_line_to(cairo, x, y);
}

void curveTo(cairo_t *cairo, double x1, double y1, double x2, double y2, double x3, double y3) {
	cairo_curve_to(cairo, x1, y1, x2, y2, x3, y3);
}

void closePath(cairo_t *cairo) {
	cairo_close_path(cairo);
}

JNIEXPORT void JNICALL Cairo_NATIVE(cairo_1add_1path)
	(JNIEnv *env, jclass that, SWT_PTR arg0, SWT_PTR arg1)
{
	Cairo_NATIVE_ENTER(env, that, cairo_1add_1path_FUNC);
	cairo_new_path((cairo_t *)arg0);
	cairo_current_path((cairo_t *)arg1, (cairo_move_to_func_t *)moveTo, (cairo_line_to_func_t *)lineTo, (cairo_curve_to_func_t *)curveTo, (cairo_close_path_func_t *)closePath, (void *)arg0);
	Cairo_NATIVE_EXIT(env, that, cairo_1add_1path_FUNC);
}
#endif
