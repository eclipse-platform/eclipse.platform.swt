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
 * -  Copyright (C) 2005, 2011 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */

#ifndef INC_cairo_H
#define INC_cairo_H

#include <cairo.h>
#ifdef CAIRO_HAS_XLIB_SURFACE
#include <cairo-xlib.h>
#else
#define NO__1cairo_1xlib_1surface_1create
#define NO__1cairo_1xlib_1surface_1get_1height
#define NO__1cairo_1xlib_1surface_1get_1width 
#endif
#include <string.h>

#ifdef _WIN32
#include <windows.h>
#else
#include <dlfcn.h>
#endif

#include "cairo_custom.h"

#define Cairo_LOAD_FUNCTION LOAD_FUNCTION

#endif /* INC_cairo_H */
