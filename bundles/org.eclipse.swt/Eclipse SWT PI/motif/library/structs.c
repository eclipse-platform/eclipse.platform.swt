/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

/**
 * JNI SWT object field getters and setters declarations for Motif structs.
 */

#include "swt.h"
#include "structs.h"

/* Globals */
VISUAL_FID_CACHE VisualFc;
XANYEVENT_FID_CACHE XanyeventFc;
XBUTTONEVENT_FID_CACHE XbuttoneventFc;
XCHARSTRUCT_FID_CACHE XcharstructFc;
XCOLOR_FID_CACHE XcolorFc;
XCONFIGUREEVENT_FID_CACHE XconfigureeventFc;
XCROSSINGEVENT_FID_CACHE XcrossingeventFc;
XEXPOSEEVENT_FID_CACHE XexposeeventFc;
XFOCUSCHANGEEVENT_FID_CACHE XfocuschangeeventFc;
XFONTSTRUCT_FID_CACHE XfontstructFc;
XGCVALUES_FID_CACHE XgcvaluesFc;
XIMAGE_FID_CACHE XimageFc;
XKEYEVENT_FID_CACHE XkeyeventFc;
XMANYCALLBACKSTRUCT_FID_CACHE XmanycallbackstructFc;
XMDRAGPROCCALLBACK_FID_CACHE XmdragproccallbackFc;
XMDROPFINISHCALLBACK_FID_CACHE XmdropfinishcallbackFc;
XMDROPPROCCALLBACK_FID_CACHE XmdropproccallbackFc;
XMOTIONEVENT_FID_CACHE XmotioneventFc;
XMTEXTBLOCKREC_FID_CACHE XmtextblockrecFc;
XMTEXTVERIFYCALLBACKSTRUCT_FID_CACHE XmtextverifycallbackstructFc;
XRECTANGLE_FID_CACHE XrectangleFc;
XSETWINDOWATTRIBUTES_FID_CACHE XsetwindowattributesFc;
XTEXTPROPERTY_FID_CACHE XtextpropertyFc;
XTWIDGETGEOMETRY_FID_CACHE XtwidgetgeometryFc;
XWINDOWATTRIBUTES_FID_CACHE XwindowattributesFc;
XWINDOWCHANGES_FID_CACHE XwindowchangesFc;

/* ----------- fid and class caches  ----------- */
/*
 * Used for Java objects passed into JNI that are
 * declared like:
 *
 * 	nativeFunction (Rectangle p1, Rectangle p2, Rectangle p3)
 *
 * and not like this
 *
 * 	nativeFunction (Object p1, Object p2, Object p3)
 *
 *
 */

void cacheXimageFids(JNIEnv *env, jobject lpXimage, PXIMAGE_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->ximageClass = (*env)->GetObjectClass(env,lpXimage);

    lpCache->width = (*env)->GetFieldID(env,lpCache->ximageClass,"width","I");
    lpCache->height = (*env)->GetFieldID(env,lpCache->ximageClass,"height","I");
    lpCache->xoffset = (*env)->GetFieldID(env,lpCache->ximageClass,"xoffset","I");
    lpCache->format = (*env)->GetFieldID(env,lpCache->ximageClass,"format","I");
    lpCache->data = (*env)->GetFieldID(env,lpCache->ximageClass,"data","I");
    lpCache->byte_order = (*env)->GetFieldID(env,lpCache->ximageClass,"byte_order","I");
    lpCache->bitmap_unit = (*env)->GetFieldID(env,lpCache->ximageClass,"bitmap_unit","I");
    lpCache->bitmap_bit_order = (*env)->GetFieldID(env,lpCache->ximageClass,"bitmap_bit_order","I");
    lpCache->bitmap_pad = (*env)->GetFieldID(env,lpCache->ximageClass,"bitmap_pad","I");
    lpCache->depth = (*env)->GetFieldID(env,lpCache->ximageClass,"depth","I");
    lpCache->bytes_per_line = (*env)->GetFieldID(env,lpCache->ximageClass,"bytes_per_line","I");
    lpCache->bits_per_pixel = (*env)->GetFieldID(env,lpCache->ximageClass,"bits_per_pixel","I");
    lpCache->red_mask = (*env)->GetFieldID(env,lpCache->ximageClass,"red_mask","I");
    lpCache->green_mask = (*env)->GetFieldID(env,lpCache->ximageClass,"green_mask","I");
    lpCache->blue_mask = (*env)->GetFieldID(env,lpCache->ximageClass,"blue_mask","I");
    lpCache->obdata = (*env)->GetFieldID(env,lpCache->ximageClass,"obdata","I");
    lpCache->create_image = (*env)->GetFieldID(env,lpCache->ximageClass,"create_image","I");
    lpCache->destroy_image = (*env)->GetFieldID(env,lpCache->ximageClass,"destroy_image","I");
    lpCache->get_pixel = (*env)->GetFieldID(env,lpCache->ximageClass,"get_pixel","I");
    lpCache->put_pixel = (*env)->GetFieldID(env,lpCache->ximageClass,"put_pixel","I");
    lpCache->sub_image = (*env)->GetFieldID(env,lpCache->ximageClass,"sub_image","I");
    lpCache->add_pixel = (*env)->GetFieldID(env,lpCache->ximageClass,"add_pixel","I");
    lpCache->cached = 1;
}

void cacheVisualFids(JNIEnv *env, jobject lpVisual, PVISUAL_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->visualClass = (*env)->GetObjectClass(env,lpVisual);

    lpCache->ext_data = (*env)->GetFieldID(env,lpCache->visualClass,"ext_data","I");
    lpCache->visualid = (*env)->GetFieldID(env,lpCache->visualClass,"visualid","I");
    lpCache->c_class = (*env)->GetFieldID(env,lpCache->visualClass,"c_class","I");
    lpCache->red_mask = (*env)->GetFieldID(env,lpCache->visualClass,"red_mask","I");
    lpCache->green_mask = (*env)->GetFieldID(env,lpCache->visualClass,"green_mask","I");
    lpCache->blue_mask = (*env)->GetFieldID(env,lpCache->visualClass,"blue_mask","I");
    lpCache->bits_per_rgb = (*env)->GetFieldID(env,lpCache->visualClass,"bits_per_rgb","I");
    lpCache->map_entries = (*env)->GetFieldID(env,lpCache->visualClass,"map_entries","I");
    lpCache->cached = 1;
}

void cacheXanyeventFids(JNIEnv *env, jobject lpXevent, PXANYEVENT_FID_CACHE lpCache)
{
    int i;

    if (lpCache->cached) return;

    lpCache->xeventClass = (*env)->GetObjectClass(env,lpXevent);

    lpCache->type = (*env)->GetFieldID(env,lpCache->xeventClass,"type","I");
    lpCache->serial = (*env)->GetFieldID(env,lpCache->xeventClass,"serial","I");
    lpCache->send_event = (*env)->GetFieldID(env,lpCache->xeventClass,"send_event","I");
    lpCache->display = (*env)->GetFieldID(env,lpCache->xeventClass,"display","I");
    lpCache->window = (*env)->GetFieldID(env,lpCache->xeventClass,"window","I");
    for (i=0; i<19; i++) {
        char buffer [8];
        sprintf(buffer, "pad%d", i);
    	lpCache->pad[i] = (*env)->GetFieldID(env,lpCache->xeventClass,buffer,"I");
    }
    lpCache->cached = 1;
}

void cacheXbuttoneventFids(JNIEnv *env, jobject lpXevent, PXBUTTONEVENT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xeventClass = (*env)->GetObjectClass(env,lpXevent);

    lpCache->type = (*env)->GetFieldID(env,lpCache->xeventClass,"type","I");
    lpCache->serial = (*env)->GetFieldID(env,lpCache->xeventClass,"serial","I");
    lpCache->send_event = (*env)->GetFieldID(env,lpCache->xeventClass,"send_event","I");
    lpCache->display = (*env)->GetFieldID(env,lpCache->xeventClass,"display","I");
    lpCache->window = (*env)->GetFieldID(env,lpCache->xeventClass,"window","I");
    lpCache->root = (*env)->GetFieldID(env,lpCache->xeventClass,"root","I");
    lpCache->subwindow = (*env)->GetFieldID(env,lpCache->xeventClass,"subwindow","I");
    lpCache->time = (*env)->GetFieldID(env,lpCache->xeventClass,"time","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->xeventClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->xeventClass,"y","I");
    lpCache->x_root = (*env)->GetFieldID(env,lpCache->xeventClass,"x_root","I");
    lpCache->y_root = (*env)->GetFieldID(env,lpCache->xeventClass,"y_root","I");
    lpCache->state = (*env)->GetFieldID(env,lpCache->xeventClass,"state","I");
    lpCache->button = (*env)->GetFieldID(env,lpCache->xeventClass,"button","I");
    lpCache->same_screen = (*env)->GetFieldID(env,lpCache->xeventClass,"same_screen","I");
    lpCache->cached = 1;
}

void cacheXcharstructFids(JNIEnv *env, jobject lpXcharstruct, PXCHARSTRUCT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xcharstructClass = (*env)->GetObjectClass(env,lpXcharstruct);

    lpCache->lbearing = (*env)->GetFieldID(env,lpCache->xcharstructClass,"lbearing","S");
    lpCache->rbearing = (*env)->GetFieldID(env,lpCache->xcharstructClass,"rbearing","S");
    lpCache->width = (*env)->GetFieldID(env,lpCache->xcharstructClass,"width","S");
    lpCache->ascent = (*env)->GetFieldID(env,lpCache->xcharstructClass,"ascent","S");
    lpCache->descent = (*env)->GetFieldID(env,lpCache->xcharstructClass,"descent","S");
    lpCache->attributes = (*env)->GetFieldID(env,lpCache->xcharstructClass,"attributes","S");
    lpCache->cached = 1;
}

void cacheXconfigureeventFids(JNIEnv *env, jobject lpXevent, PXCONFIGUREEVENT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xeventClass = (*env)->GetObjectClass(env,lpXevent);

    lpCache->type = (*env)->GetFieldID(env,lpCache->xeventClass,"type","I");
    lpCache->serial = (*env)->GetFieldID(env,lpCache->xeventClass,"serial","I");
    lpCache->send_event = (*env)->GetFieldID(env,lpCache->xeventClass,"send_event","I");
    lpCache->display = (*env)->GetFieldID(env,lpCache->xeventClass,"display","I");
    lpCache->window = (*env)->GetFieldID(env,lpCache->xeventClass,"window","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->xeventClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->xeventClass,"y","I");
    lpCache->width = (*env)->GetFieldID(env,lpCache->xeventClass,"width","I");
    lpCache->height = (*env)->GetFieldID(env,lpCache->xeventClass,"height","I");
    lpCache->border_width = (*env)->GetFieldID(env,lpCache->xeventClass,"border_width","I");
    lpCache->above = (*env)->GetFieldID(env,lpCache->xeventClass,"above","I");
    lpCache->override_redirect = (*env)->GetFieldID(env,lpCache->xeventClass,"override_redirect","I");
    lpCache->cached = 1;
}

void cacheXcrossingeventFids(JNIEnv *env, jobject lpXcrossingevent, PXCROSSINGEVENT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xcrossingeventClass = (*env)->GetObjectClass(env,lpXcrossingevent);

    lpCache->type = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"type","I");
    lpCache->serial = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"serial","I");
    lpCache->send_event = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"send_event","I");
    lpCache->display = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"display","I");
    lpCache->window = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"window","I");
    lpCache->root = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"root","I");
    lpCache->subwindow = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"subwindow","I");
    lpCache->time = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"time","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"y","I");
    lpCache->x_root = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"x_root","I");
    lpCache->y_root = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"y_root","I");
    lpCache->mode = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"mode","I");
    lpCache->detail = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"detail","I");
    lpCache->same_screen = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"same_screen","I");
    lpCache->focus = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"focus","I");
    lpCache->state = (*env)->GetFieldID(env,lpCache->xcrossingeventClass,"state","I");
    lpCache->cached = 1;
}

void cacheXexposeeventFids(JNIEnv *env, jobject lpXevent, PXEXPOSEEVENT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xeventClass = (*env)->GetObjectClass(env,lpXevent);

    lpCache->type = (*env)->GetFieldID(env,lpCache->xeventClass,"type","I");
    lpCache->serial = (*env)->GetFieldID(env,lpCache->xeventClass,"serial","I");
    lpCache->send_event = (*env)->GetFieldID(env,lpCache->xeventClass,"send_event","I");
    lpCache->display = (*env)->GetFieldID(env,lpCache->xeventClass,"display","I");
    lpCache->window = (*env)->GetFieldID(env,lpCache->xeventClass,"window","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->xeventClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->xeventClass,"y","I");
    lpCache->width = (*env)->GetFieldID(env,lpCache->xeventClass,"width","I");
    lpCache->height = (*env)->GetFieldID(env,lpCache->xeventClass,"height","I");
    lpCache->count = (*env)->GetFieldID(env,lpCache->xeventClass,"count","I");
    lpCache->cached = 1;
}

void cacheXfocuschangeeventFids(JNIEnv *env, jobject lpXevent, PXFOCUSCHANGEEVENT_FID_CACHE lpCache)
{
    int i;
    if (lpCache->cached) return;

    lpCache->xeventClass = (*env)->GetObjectClass(env,lpXevent);

    lpCache->type = (*env)->GetFieldID(env,lpCache->xeventClass,"type","I");
    lpCache->serial = (*env)->GetFieldID(env,lpCache->xeventClass,"serial","I");
    lpCache->send_event = (*env)->GetFieldID(env,lpCache->xeventClass,"send_event","I");
    lpCache->display = (*env)->GetFieldID(env,lpCache->xeventClass,"display","I");
    lpCache->window = (*env)->GetFieldID(env,lpCache->xeventClass,"window","I");
    lpCache->mode = (*env)->GetFieldID(env,lpCache->xeventClass,"mode","I");
    lpCache->detail = (*env)->GetFieldID(env,lpCache->xeventClass,"detail","I");
    for (i=0; i<17; i++) {
        char buffer [8];
        sprintf(buffer, "pad%d", i);
    	lpCache->pad[i] = (*env)->GetFieldID(env,lpCache->xeventClass,buffer,"I");
    }
    lpCache->cached = 1;
}

void cacheXfontstructFids(JNIEnv *env, jobject lpXfontstruct, PXFONTSTRUCT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xfontstructClass = (*env)->GetObjectClass(env,lpXfontstruct);

    lpCache->ext_data = (*env)->GetFieldID(env,lpCache->xfontstructClass,"ext_data","I");
    lpCache->fid = (*env)->GetFieldID(env,lpCache->xfontstructClass,"fid","I");
    lpCache->direction = (*env)->GetFieldID(env,lpCache->xfontstructClass,"direction","I");
    lpCache->min_char_or_byte2 = (*env)->GetFieldID(env,lpCache->xfontstructClass,"min_char_or_byte2","I");
    lpCache->max_char_or_byte2 = (*env)->GetFieldID(env,lpCache->xfontstructClass,"max_char_or_byte2","I");
    lpCache->min_byte1 = (*env)->GetFieldID(env,lpCache->xfontstructClass,"min_byte1","I");
    lpCache->max_byte1 = (*env)->GetFieldID(env,lpCache->xfontstructClass,"max_byte1","I");
    lpCache->all_chars_exist = (*env)->GetFieldID(env,lpCache->xfontstructClass,"all_chars_exist","I");
    lpCache->default_char = (*env)->GetFieldID(env,lpCache->xfontstructClass,"default_char","I");
    lpCache->n_properties = (*env)->GetFieldID(env,lpCache->xfontstructClass,"n_properties","I");
    lpCache->properties = (*env)->GetFieldID(env,lpCache->xfontstructClass,"properties","I");
    lpCache->min_bounds_lbearing = (*env)->GetFieldID(env,lpCache->xfontstructClass,"min_bounds_lbearing","S");
    lpCache->min_bounds_rbearing = (*env)->GetFieldID(env,lpCache->xfontstructClass,"min_bounds_rbearing","S");
    lpCache->min_bounds_width = (*env)->GetFieldID(env,lpCache->xfontstructClass,"min_bounds_width","S");
    lpCache->min_bounds_ascent = (*env)->GetFieldID(env,lpCache->xfontstructClass,"min_bounds_ascent","S");
    lpCache->min_bounds_descent = (*env)->GetFieldID(env,lpCache->xfontstructClass,"min_bounds_descent","S");
    lpCache->min_bounds_attributes = (*env)->GetFieldID(env,lpCache->xfontstructClass,"min_bounds_attributes","S");
    lpCache->max_bounds_lbearing = (*env)->GetFieldID(env,lpCache->xfontstructClass,"max_bounds_lbearing","S");
    lpCache->max_bounds_rbearing = (*env)->GetFieldID(env,lpCache->xfontstructClass,"max_bounds_rbearing","S");
    lpCache->max_bounds_width = (*env)->GetFieldID(env,lpCache->xfontstructClass,"max_bounds_width","S");
    lpCache->max_bounds_ascent = (*env)->GetFieldID(env,lpCache->xfontstructClass,"max_bounds_ascent","S");
    lpCache->max_bounds_descent = (*env)->GetFieldID(env,lpCache->xfontstructClass,"max_bounds_descent","S");
    lpCache->max_bounds_attributes = (*env)->GetFieldID(env,lpCache->xfontstructClass,"max_bounds_attributes","S");
    lpCache->per_char = (*env)->GetFieldID(env,lpCache->xfontstructClass,"per_char","I");
    lpCache->ascent = (*env)->GetFieldID(env,lpCache->xfontstructClass,"ascent","I");
    lpCache->descent = (*env)->GetFieldID(env,lpCache->xfontstructClass,"descent","I");
    lpCache->cached = 1;
}

void cacheXkeyeventFids(JNIEnv *env, jobject lpXevent, PXKEYEVENT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xeventClass = (*env)->GetObjectClass(env,lpXevent);

    lpCache->type = (*env)->GetFieldID(env,lpCache->xeventClass,"type","I");
    lpCache->serial = (*env)->GetFieldID(env,lpCache->xeventClass,"serial","I");
    lpCache->send_event = (*env)->GetFieldID(env,lpCache->xeventClass,"send_event","I");
    lpCache->display = (*env)->GetFieldID(env,lpCache->xeventClass,"display","I");
    lpCache->window = (*env)->GetFieldID(env,lpCache->xeventClass,"window","I");
    lpCache->root = (*env)->GetFieldID(env,lpCache->xeventClass,"root","I");
    lpCache->subwindow = (*env)->GetFieldID(env,lpCache->xeventClass,"subwindow","I");
    lpCache->time = (*env)->GetFieldID(env,lpCache->xeventClass,"time","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->xeventClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->xeventClass,"y","I");
    lpCache->x_root = (*env)->GetFieldID(env,lpCache->xeventClass,"x_root","I");
    lpCache->y_root = (*env)->GetFieldID(env,lpCache->xeventClass,"y_root","I");
    lpCache->state = (*env)->GetFieldID(env,lpCache->xeventClass,"state","I");
    lpCache->keycode = (*env)->GetFieldID(env,lpCache->xeventClass,"keycode","I");
    lpCache->same_screen = (*env)->GetFieldID(env,lpCache->xeventClass,"same_screen","I");
    lpCache->cached = 1;
}

void cacheXmotioneventFids(JNIEnv *env, jobject lpXevent, PXMOTIONEVENT_FID_CACHE lpCache)
{
    int i;
    if (lpCache->cached) return;

    lpCache->xeventClass = (*env)->GetObjectClass(env,lpXevent);

    lpCache->type = (*env)->GetFieldID(env,lpCache->xeventClass,"type","I");
    lpCache->serial = (*env)->GetFieldID(env,lpCache->xeventClass,"serial","I");
    lpCache->send_event = (*env)->GetFieldID(env,lpCache->xeventClass,"send_event","I");
    lpCache->display = (*env)->GetFieldID(env,lpCache->xeventClass,"display","I");
    lpCache->window = (*env)->GetFieldID(env,lpCache->xeventClass,"window","I");
    lpCache->root = (*env)->GetFieldID(env,lpCache->xeventClass,"root","I");
    lpCache->subwindow = (*env)->GetFieldID(env,lpCache->xeventClass,"subwindow","I");
    lpCache->time = (*env)->GetFieldID(env,lpCache->xeventClass,"time","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->xeventClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->xeventClass,"y","I");
    lpCache->x_root = (*env)->GetFieldID(env,lpCache->xeventClass,"x_root","I");
    lpCache->y_root = (*env)->GetFieldID(env,lpCache->xeventClass,"y_root","I");
    lpCache->state = (*env)->GetFieldID(env,lpCache->xeventClass,"state","I");
    lpCache->is_hint = (*env)->GetFieldID(env,lpCache->xeventClass,"is_hint","I");
    lpCache->same_screen = (*env)->GetFieldID(env,lpCache->xeventClass,"same_screen","I");
    for (i=0; i<10; i++) {
        char buffer [8];
        sprintf(buffer, "pad%d", i);
    	lpCache->pad[i] = (*env)->GetFieldID(env,lpCache->xeventClass,buffer,"I");
    }
    lpCache->cached = 1;
}

void cacheXcolorFids(JNIEnv *env, jobject lpXcolor, PXCOLOR_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xcolorClass = (*env)->GetObjectClass(env,lpXcolor);

    lpCache->pixel = (*env)->GetFieldID(env,lpCache->xcolorClass,"pixel","I");
    lpCache->red = (*env)->GetFieldID(env,lpCache->xcolorClass,"red","S");
    lpCache->green = (*env)->GetFieldID(env,lpCache->xcolorClass,"green","S");
    lpCache->blue = (*env)->GetFieldID(env,lpCache->xcolorClass,"blue","S");
    lpCache->flags = (*env)->GetFieldID(env,lpCache->xcolorClass,"flags","B");
    lpCache->pad = (*env)->GetFieldID(env,lpCache->xcolorClass,"pad","B");
    lpCache->cached = 1;
}

void cacheXgcvaluesFids(JNIEnv *env, jobject lpXgcvalues, PXGCVALUES_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xgcvaluesClass = (*env)->GetObjectClass(env,lpXgcvalues);

    lpCache->function = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"function","I");
    lpCache->plane_mask = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"plane_mask","I");
    lpCache->foreground = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"foreground","I");
    lpCache->background = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"background","I");
    lpCache->line_width = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"line_width","I");
    lpCache->line_style = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"line_style","I");
    lpCache->cap_style = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"cap_style","I");
    lpCache->join_style = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"join_style","I");
    lpCache->fill_style = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"fill_style","I");
    lpCache->fill_rule = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"fill_rule","I");
    lpCache->arc_mode = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"arc_mode","I");
    lpCache->tile = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"tile","I");
    lpCache->stipple = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"stipple","I");
    lpCache->ts_x_origin = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"ts_x_origin","I");
    lpCache->ts_y_origin = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"ts_y_origin","I");
    lpCache->font = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"font","I");
    lpCache->subwindow_mode = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"subwindow_mode","I");
    lpCache->graphics_exposures = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"graphics_exposures","I");
    lpCache->clip_x_origin = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"clip_x_origin","I");
    lpCache->clip_y_origin = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"clip_y_origin","I");
    lpCache->clip_mask = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"clip_mask","I");
    lpCache->dash_offset = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"dash_offset","I");

    lpCache->dashes = (*env)->GetFieldID(env,lpCache->xgcvaluesClass,"dashes","B");
    lpCache->cached = 1;
}

void cacheXmanycallbackstructFids(JNIEnv *env, jobject lpXmanycallback, PXMANYCALLBACKSTRUCT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xmanycallbackstructClass = (*env)->GetObjectClass(env,lpXmanycallback);

    lpCache->reason = (*env)->GetFieldID(env,lpCache->xmanycallbackstructClass,"reason","I");
    lpCache->event = (*env)->GetFieldID(env,lpCache->xmanycallbackstructClass,"event","I");
    lpCache->cached = 1;
}

void cacheXmdragproccallbackFids(JNIEnv *env, jobject lpXmdragproccallback, PXMDRAGPROCCALLBACK_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xmdragproccallbackClass = (*env)->GetObjectClass(env,lpXmdragproccallback);

    lpCache->reason = (*env)->GetFieldID(env,lpCache->xmdragproccallbackClass,"reason","I");
    lpCache->event = (*env)->GetFieldID(env,lpCache->xmdragproccallbackClass,"event","I");
    lpCache->timeStamp = (*env)->GetFieldID(env,lpCache->xmdragproccallbackClass,"timeStamp","I");
    lpCache->dragContext = (*env)->GetFieldID(env,lpCache->xmdragproccallbackClass,"dragContext","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->xmdragproccallbackClass,"x","S");
    lpCache->y = (*env)->GetFieldID(env,lpCache->xmdragproccallbackClass,"y","S");
    lpCache->dropSiteStatus = (*env)->GetFieldID(env,lpCache->xmdragproccallbackClass,"dropSiteStatus","B");
    lpCache->operation = (*env)->GetFieldID(env,lpCache->xmdragproccallbackClass,"operation","B");
    lpCache->operations = (*env)->GetFieldID(env,lpCache->xmdragproccallbackClass,"operations","B");
    lpCache->animate = (*env)->GetFieldID(env,lpCache->xmdragproccallbackClass,"animate","B");
    lpCache->cached = 1;
}

void cacheXmdropproccallbackFids(JNIEnv *env, jobject lpXmdropproccallback, PXMDROPPROCCALLBACK_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xmdropproccallbackClass = (*env)->GetObjectClass(env,lpXmdropproccallback);

    lpCache->reason = (*env)->GetFieldID(env,lpCache->xmdropproccallbackClass,"reason","I");
    lpCache->event = (*env)->GetFieldID(env,lpCache->xmdropproccallbackClass,"event","I");
    lpCache->timeStamp = (*env)->GetFieldID(env,lpCache->xmdropproccallbackClass,"timeStamp","I");
    lpCache->dragContext = (*env)->GetFieldID(env,lpCache->xmdropproccallbackClass,"dragContext","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->xmdropproccallbackClass,"x","S");
    lpCache->y = (*env)->GetFieldID(env,lpCache->xmdropproccallbackClass,"y","S");
    lpCache->dropSiteStatus = (*env)->GetFieldID(env,lpCache->xmdropproccallbackClass,"dropSiteStatus","B");
    lpCache->operation = (*env)->GetFieldID(env,lpCache->xmdropproccallbackClass,"operation","B");
    lpCache->operations = (*env)->GetFieldID(env,lpCache->xmdropproccallbackClass,"operations","B");
    lpCache->dropAction = (*env)->GetFieldID(env,lpCache->xmdropproccallbackClass,"dropAction","B");
    lpCache->cached = 1;
}

void cacheXmdropfinishcallbackFids(JNIEnv *env, jobject lpXmdropfinishcallback, PXMDROPFINISHCALLBACK_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xmdropfinishcallbackClass = (*env)->GetObjectClass(env,lpXmdropfinishcallback);

    lpCache->reason = (*env)->GetFieldID(env,lpCache->xmdropfinishcallbackClass,"reason","I");
    lpCache->event = (*env)->GetFieldID(env,lpCache->xmdropfinishcallbackClass,"event","I");
    lpCache->timeStamp = (*env)->GetFieldID(env,lpCache->xmdropfinishcallbackClass,"timeStamp","I");
    lpCache->operation = (*env)->GetFieldID(env,lpCache->xmdropfinishcallbackClass,"operation","B");
    lpCache->operations = (*env)->GetFieldID(env,lpCache->xmdropfinishcallbackClass,"operations","B");
    lpCache->dropSiteStatus = (*env)->GetFieldID(env,lpCache->xmdropfinishcallbackClass,"dropSiteStatus","B");
    lpCache->dropAction = (*env)->GetFieldID(env,lpCache->xmdropfinishcallbackClass,"dropAction","B");
    lpCache->completionStatus = (*env)->GetFieldID(env,lpCache->xmdropfinishcallbackClass,"completionStatus","B");
    lpCache->cached = 1;
}

void cacheXmtextblockrecFids(JNIEnv *env, jobject lpXmtextblockrec, PXMTEXTBLOCKREC_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xmtextblockrecClass = (*env)->GetObjectClass(env,lpXmtextblockrec);

    lpCache->ptr = (*env)->GetFieldID(env,lpCache->xmtextblockrecClass,"ptr","I");
    lpCache->length = (*env)->GetFieldID(env,lpCache->xmtextblockrecClass,"length","I");
    lpCache->format = (*env)->GetFieldID(env,lpCache->xmtextblockrecClass,"format","I");
    lpCache->cached = 1;
}

void cacheXmtextverifycallbackstructFids(JNIEnv *env, jobject lpXmtextverifycallback, PXMTEXTVERIFYCALLBACKSTRUCT_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xmtextverifycallbackstructClass = (*env)->GetObjectClass(env,lpXmtextverifycallback);

    lpCache->reason = (*env)->GetFieldID(env,lpCache->xmtextverifycallbackstructClass,"reason","I");
    lpCache->event = (*env)->GetFieldID(env,lpCache->xmtextverifycallbackstructClass,"event","I");
    lpCache->doit = (*env)->GetFieldID(env,lpCache->xmtextverifycallbackstructClass,"doit","B");
    lpCache->currInsert = (*env)->GetFieldID(env,lpCache->xmtextverifycallbackstructClass,"currInsert","I");
    lpCache->newInsert = (*env)->GetFieldID(env,lpCache->xmtextverifycallbackstructClass,"newInsert","I");
    lpCache->startPos = (*env)->GetFieldID(env,lpCache->xmtextverifycallbackstructClass,"startPos","I");
    lpCache->endPos = (*env)->GetFieldID(env,lpCache->xmtextverifycallbackstructClass,"endPos","I");
    lpCache->text = (*env)->GetFieldID(env,lpCache->xmtextverifycallbackstructClass,"text","I");
    lpCache->cached = 1;
}

void cacheXrectangleFids(JNIEnv *env, jobject lpXrect, PXRECTANGLE_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xrectClass = (*env)->GetObjectClass(env,lpXrect);

    lpCache->x = (*env)->GetFieldID(env,lpCache->xrectClass,"x","S");
    lpCache->y = (*env)->GetFieldID(env,lpCache->xrectClass,"y","S");
    lpCache->width = (*env)->GetFieldID(env,lpCache->xrectClass,"width","S");
    lpCache->height = (*env)->GetFieldID(env,lpCache->xrectClass,"height","S");
    lpCache->cached = 1;
}

void cacheXsetwindowattributesFids(JNIEnv *env, jobject lpXsetwindowattributes, PXSETWINDOWATTRIBUTES_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xsetwindowattributesClass = (*env)->GetObjectClass(env,lpXsetwindowattributes);

    lpCache->background_pixmap = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"background_pixmap","I");
    lpCache->background_pixel = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"background_pixel","I");
    lpCache->border_pixmap = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"border_pixmap","I");
    lpCache->border_pixel = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"border_pixel","I");
    lpCache->bit_gravity = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"bit_gravity","I");
    lpCache->win_gravity = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"win_gravity","I");
    lpCache->backing_store = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"backing_store","I");
    lpCache->backing_planes = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"backing_planes","I");
    lpCache->backing_pixel = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"backing_pixel","I");
    lpCache->save_under = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"save_under","I");
    lpCache->event_mask = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"event_mask","I");
    lpCache->do_not_propagate_mask = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"do_not_propagate_mask","I");
    lpCache->override_redirect = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"override_redirect","I");
    lpCache->colormap = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"colormap","I");
    lpCache->cursor = (*env)->GetFieldID(env,lpCache->xsetwindowattributesClass,"cursor","I");
    lpCache->cached = 1;
}

void cacheXtextpropertyFids(JNIEnv *env, jobject lpXtextproperty, PXTEXTPROPERTY_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xtextpropertyClass = (*env)->GetObjectClass(env,lpXtextproperty);

    lpCache->value = (*env)->GetFieldID(env,lpCache->xtextpropertyClass,"value","I");
    lpCache->encoding = (*env)->GetFieldID(env,lpCache->xtextpropertyClass,"encoding","I");
    lpCache->format = (*env)->GetFieldID(env,lpCache->xtextpropertyClass,"format","I");
    lpCache->nitems = (*env)->GetFieldID(env,lpCache->xtextpropertyClass,"nitems","I");
    lpCache->cached = 1;
}

void cacheXwindowattributesFids(JNIEnv *env, jobject lpXwindowattributes, PXWINDOWATTRIBUTES_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xwindowattributesClass = (*env)->GetObjectClass(env,lpXwindowattributes);

    lpCache->x = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"y","I");
    lpCache->width = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"width","I");
    lpCache->height = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"height","I");
    lpCache->border_width = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"border_width","I");
    lpCache->depth = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"depth","I");
    lpCache->visual = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"visual","I");
    lpCache->root = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"root","I");
    lpCache->class = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"c_class","I");
    lpCache->bit_gravity = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"bit_gravity","I");
    lpCache->win_gravity = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"win_gravity","I");
    lpCache->backing_store = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"backing_store","I");
    lpCache->backing_planes = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"backing_planes","I");
    lpCache->backing_pixel = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"backing_pixel","I");
    lpCache->save_under = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"save_under","I");
    lpCache->colormap = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"colormap","I");
    lpCache->map_installed = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"map_installed","I");
    lpCache->map_state = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"map_state","I");
    lpCache->all_event_masks = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"all_event_masks","I");
    lpCache->your_event_mask = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"your_event_mask","I");
    lpCache->do_not_propagate_mask = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"do_not_propagate_mask","I");
    lpCache->override_redirect = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"override_redirect","I");
    lpCache->screen = (*env)->GetFieldID(env,lpCache->xwindowattributesClass,"screen","I");
    lpCache->cached = 1;
}

void cacheXwindowchangesFids(JNIEnv *env, jobject lpXwindowchanges, PXWINDOWCHANGES_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xwindowchangesClass = (*env)->GetObjectClass(env,lpXwindowchanges);

    lpCache->x = (*env)->GetFieldID(env,lpCache->xwindowchangesClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->xwindowchangesClass,"y","I");
    lpCache->width = (*env)->GetFieldID(env,lpCache->xwindowchangesClass,"width","I");
    lpCache->height = (*env)->GetFieldID(env,lpCache->xwindowchangesClass,"height","I");
    lpCache->border_width = (*env)->GetFieldID(env,lpCache->xwindowchangesClass,"border_width","I");
    lpCache->sibling = (*env)->GetFieldID(env,lpCache->xwindowchangesClass,"sibling","I");
    lpCache->stack_mode = (*env)->GetFieldID(env,lpCache->xwindowchangesClass,"stack_mode","I");
    lpCache->cached = 1;
}

void cacheXtwidgetgeometryFids(JNIEnv *env, jobject lpXtwidgetgeometry, PXTWIDGETGEOMETRY_FID_CACHE lpCache)
{
    if (lpCache->cached) return;

    lpCache->xtwidgetgeometryClass = (*env)->GetObjectClass(env,lpXtwidgetgeometry);

    lpCache->request_mode = (*env)->GetFieldID(env,lpCache->xtwidgetgeometryClass,"request_mode","I");
    lpCache->x = (*env)->GetFieldID(env,lpCache->xtwidgetgeometryClass,"x","I");
    lpCache->y = (*env)->GetFieldID(env,lpCache->xtwidgetgeometryClass,"y","I");
    lpCache->width = (*env)->GetFieldID(env,lpCache->xtwidgetgeometryClass,"width","I");
    lpCache->height = (*env)->GetFieldID(env,lpCache->xtwidgetgeometryClass,"height","I");
    lpCache->border_width = (*env)->GetFieldID(env,lpCache->xtwidgetgeometryClass,"border_width","I");
    lpCache->sibling = (*env)->GetFieldID(env,lpCache->xtwidgetgeometryClass,"sibling","I");
    lpCache->stack_mode = (*env)->GetFieldID(env,lpCache->xtwidgetgeometryClass,"stack_mode","I");
    lpCache->cached = 1;
}

/* ----------- getters and setters  ----------- */
/**
 * These functions get or set object field ids assuming that the
 * fids for these objects have already been cached.
 *
 */

void getVisualFields(JNIEnv *env, jobject lpObject, Visual *lpVisual, VISUAL_FID_CACHE *lpVisualFc)
{
    lpVisual->ext_data = (XExtData *)(*env)->GetIntField(env,lpObject,lpVisualFc->ext_data);
    lpVisual->visualid = (*env)->GetIntField(env,lpObject,lpVisualFc->visualid);
    lpVisual->class = (*env)->GetIntField(env,lpObject,lpVisualFc->c_class);
    lpVisual->red_mask = (*env)->GetIntField(env,lpObject,lpVisualFc->red_mask);
    lpVisual->green_mask = (*env)->GetIntField(env,lpObject,lpVisualFc->green_mask);
    lpVisual->blue_mask = (*env)->GetIntField(env,lpObject,lpVisualFc->blue_mask);
    lpVisual->bits_per_rgb = (*env)->GetIntField(env,lpObject,lpVisualFc->bits_per_rgb);
    lpVisual->map_entries = (*env)->GetIntField(env,lpObject,lpVisualFc->map_entries);
}

void setVisualFields(JNIEnv *env, jobject lpObject, Visual *lpVisual, VISUAL_FID_CACHE *lpVisualFc)
{
    (*env)->SetIntField(env,lpObject,lpVisualFc->ext_data, (jint)lpVisual->ext_data);
    (*env)->SetIntField(env,lpObject,lpVisualFc->visualid, lpVisual->visualid);
    (*env)->SetIntField(env,lpObject,lpVisualFc->c_class, lpVisual->class);
    (*env)->SetIntField(env,lpObject,lpVisualFc->red_mask, (int)lpVisual->red_mask);
    (*env)->SetIntField(env,lpObject,lpVisualFc->green_mask, lpVisual->green_mask);
    (*env)->SetIntField(env,lpObject,lpVisualFc->blue_mask, lpVisual->blue_mask);
    (*env)->SetIntField(env,lpObject,lpVisualFc->bits_per_rgb, (int)lpVisual->bits_per_rgb);
    (*env)->SetIntField(env,lpObject,lpVisualFc->map_entries, lpVisual->map_entries);
}

void getXanyeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XANYEVENT_FID_CACHE *lpXanyeventFc)
{
    int i;

    lpXevent->type = (*env)->GetIntField(env,lpObject,lpXanyeventFc->type);
    lpXevent->xany.serial = (*env)->GetIntField(env,lpObject,lpXanyeventFc->serial);
    lpXevent->xany.send_event = (*env)->GetIntField(env,lpObject,lpXanyeventFc->send_event);
    lpXevent->xany.display = (Display *)(*env)->GetIntField(env,lpObject,lpXanyeventFc->display);
    lpXevent->xany.window = (*env)->GetIntField(env,lpObject,lpXanyeventFc->window);
    for (i=0; i<19; i++) {
    	lpXevent->pad[i+5] = (*env)->GetIntField(env,lpObject,lpXanyeventFc->pad[i]);
    }
}

void setXanyeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XANYEVENT_FID_CACHE *lpXanyeventFc)
{
    int i;

    (*env)->SetIntField(env,lpObject,lpXanyeventFc->type, lpXevent->type);
    (*env)->SetIntField(env,lpObject,lpXanyeventFc->serial, lpXevent->xany.serial);
    (*env)->SetIntField(env,lpObject,lpXanyeventFc->send_event, lpXevent->xany.send_event);
    (*env)->SetIntField(env,lpObject,lpXanyeventFc->display, (int)lpXevent->xany.display);
    (*env)->SetIntField(env,lpObject,lpXanyeventFc->window, lpXevent->xany.window);
    for (i=0; i<19; i++) {
	    (*env)->SetIntField(env,lpObject,lpXanyeventFc->pad[i],lpXevent->pad[i+5]);
    }
}

void getXbuttoneventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XBUTTONEVENT_FID_CACHE *lpXbuttoneventFc)
{
    lpXevent->type = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->type);
    lpXevent->xbutton.serial = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->serial);
    lpXevent->xbutton.send_event = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->send_event);
    lpXevent->xbutton.display = (Display *)(*env)->GetIntField(env,lpObject,lpXbuttoneventFc->display);
    lpXevent->xbutton.window = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->window);
    lpXevent->xbutton.root = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->root);
    lpXevent->xbutton.subwindow = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->subwindow);
    lpXevent->xbutton.time = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->time);
    lpXevent->xbutton.x = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->x);
    lpXevent->xbutton.y = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->y);
    lpXevent->xbutton.x_root = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->x_root);
    lpXevent->xbutton.y_root = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->y_root);
    lpXevent->xbutton.state = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->state);
    lpXevent->xbutton.button = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->button);
    lpXevent->xbutton.same_screen = (*env)->GetIntField(env,lpObject,lpXbuttoneventFc->same_screen);
}

void setXbuttoneventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XBUTTONEVENT_FID_CACHE *lpXbuttoneventFc)
{
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->type, lpXevent->type);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->serial, lpXevent->xbutton.serial);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->send_event, lpXevent->xbutton.send_event);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->display, (int)lpXevent->xbutton.display);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->window, lpXevent->xbutton.window);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->root, lpXevent->xbutton.root);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->subwindow, lpXevent->xbutton.subwindow);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->time, lpXevent->xbutton.time);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->x, lpXevent->xbutton.x);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->y, lpXevent->xbutton.y);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->x_root, lpXevent->xbutton.x_root);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->y_root, lpXevent->xbutton.y_root);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->state, lpXevent->xbutton.state);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->button, lpXevent->xbutton.button);
    (*env)->SetIntField(env,lpObject,lpXbuttoneventFc->same_screen, lpXevent->xbutton.same_screen);
}

void getXconfigureeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XCONFIGUREEVENT_FID_CACHE *lpXconfigureeventFc)
{
    lpXevent->type = (*env)->GetIntField(env,lpObject,lpXconfigureeventFc->type);
    lpXevent->xconfigure.serial = (*env)->GetIntField(env,lpObject,lpXconfigureeventFc->serial);
    lpXevent->xconfigure.send_event = (*env)->GetIntField(env,lpObject,lpXconfigureeventFc->send_event);
    lpXevent->xconfigure.display = (Display *)(*env)->GetIntField(env,lpObject,lpXconfigureeventFc->display);
    lpXevent->xconfigure.window = (*env)->GetIntField(env,lpObject,lpXconfigureeventFc->window);
    lpXevent->xconfigure.x = (*env)->GetIntField(env,lpObject,lpXconfigureeventFc->x);
    lpXevent->xconfigure.y = (*env)->GetIntField(env,lpObject,lpXconfigureeventFc->y);
    lpXevent->xconfigure.width = (*env)->GetIntField(env,lpObject,lpXconfigureeventFc->width);
    lpXevent->xconfigure.height = (*env)->GetIntField(env,lpObject,lpXconfigureeventFc->height);
    lpXevent->xconfigure.border_width = (*env)->GetIntField(env,lpObject,lpXconfigureeventFc->border_width);
    lpXevent->xconfigure.above = (*env)->GetIntField(env,lpObject,lpXconfigureeventFc->above);
    lpXevent->xconfigure.override_redirect = (*env)->GetIntField(env,lpObject,lpXconfigureeventFc->override_redirect);
}

void setXconfigureeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XCONFIGUREEVENT_FID_CACHE *lpXconfigureeventFc)
{
    (*env)->SetIntField(env,lpObject,lpXconfigureeventFc->type, lpXevent->type);
    (*env)->SetIntField(env,lpObject,lpXconfigureeventFc->serial, lpXevent->xconfigure.serial);
    (*env)->SetIntField(env,lpObject,lpXconfigureeventFc->send_event, lpXevent->xconfigure.send_event);
    (*env)->SetIntField(env,lpObject,lpXconfigureeventFc->display, (int)lpXevent->xconfigure.display);
    (*env)->SetIntField(env,lpObject,lpXconfigureeventFc->window, lpXevent->xconfigure.window);
    (*env)->SetIntField(env,lpObject,lpXconfigureeventFc->x, lpXevent->xconfigure.x);
    (*env)->SetIntField(env,lpObject,lpXconfigureeventFc->y, lpXevent->xconfigure.y);
    (*env)->SetIntField(env,lpObject,lpXconfigureeventFc->width, lpXevent->xconfigure.width);
    (*env)->SetIntField(env,lpObject,lpXconfigureeventFc->height, lpXevent->xconfigure.height);
    (*env)->SetIntField(env,lpObject,lpXconfigureeventFc->border_width, lpXevent->xconfigure.border_width);
    (*env)->SetIntField(env,lpObject,lpXconfigureeventFc->above, lpXevent->xconfigure.above);
    (*env)->SetIntField(env,lpObject,lpXconfigureeventFc->override_redirect, lpXevent->xconfigure.override_redirect);
}

void getXcharstructFields(JNIEnv *env, jobject lpObject, XCharStruct *lpXcharstruct, XCHARSTRUCT_FID_CACHE *lpXcharstructFc)
{
    lpXcharstruct->lbearing = (*env)->GetShortField(env,lpObject,lpXcharstructFc->lbearing);
    lpXcharstruct->rbearing = (*env)->GetShortField(env,lpObject,lpXcharstructFc->rbearing);
    lpXcharstruct->width = (*env)->GetShortField(env,lpObject,lpXcharstructFc->width);
    lpXcharstruct->ascent = (*env)->GetShortField(env,lpObject,lpXcharstructFc->ascent);
    lpXcharstruct->descent = (*env)->GetShortField(env,lpObject,lpXcharstructFc->descent);
    lpXcharstruct->attributes = (*env)->GetShortField(env,lpObject,lpXcharstructFc->attributes);
}

void setXcharstructFields(JNIEnv *env, jobject lpObject, XCharStruct *lpXcharstruct, XCHARSTRUCT_FID_CACHE *lpXcharstructFc)
{
    (*env)->SetShortField(env,lpObject,lpXcharstructFc->lbearing, lpXcharstruct->lbearing);
    (*env)->SetShortField(env,lpObject,lpXcharstructFc->rbearing, lpXcharstruct->rbearing);
    (*env)->SetShortField(env,lpObject,lpXcharstructFc->width, lpXcharstruct->width);
    (*env)->SetShortField(env,lpObject,lpXcharstructFc->ascent, lpXcharstruct->ascent);
    (*env)->SetShortField(env,lpObject,lpXcharstructFc->descent, lpXcharstruct->descent);
    (*env)->SetShortField(env,lpObject,lpXcharstructFc->attributes, lpXcharstruct->attributes);

}

void getXcrossingeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXcrossingevent, XCROSSINGEVENT_FID_CACHE *lpXcrossingeventFc)
{
    int i;

    lpXcrossingevent->type = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->type);
    lpXcrossingevent->xcrossing.serial = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->serial);
    lpXcrossingevent->xcrossing.send_event = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->send_event);
    lpXcrossingevent->xcrossing.display = (Display *)(*env)->GetIntField(env,lpObject,lpXcrossingeventFc->display);
    lpXcrossingevent->xcrossing.window = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->window);
    lpXcrossingevent->xcrossing.root = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->root);
    lpXcrossingevent->xcrossing.subwindow = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->subwindow);
    lpXcrossingevent->xcrossing.time = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->time);
    lpXcrossingevent->xcrossing.x = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->x);
    lpXcrossingevent->xcrossing.y = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->y);
    lpXcrossingevent->xcrossing.x_root = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->x_root);
    lpXcrossingevent->xcrossing.y_root = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->y_root);
    lpXcrossingevent->xcrossing.mode = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->mode);
    lpXcrossingevent->xcrossing.detail = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->detail);
    lpXcrossingevent->xcrossing.same_screen = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->same_screen);
    lpXcrossingevent->xcrossing.focus = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->focus);
    lpXcrossingevent->xcrossing.state = (*env)->GetIntField(env,lpObject,lpXcrossingeventFc->state);
}

void setXcrossingeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXcrossingevent, XCROSSINGEVENT_FID_CACHE *lpXcrossingeventFc)
{
    int i;

    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->type, lpXcrossingevent->type);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->serial, lpXcrossingevent->xcrossing.serial);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->send_event, lpXcrossingevent->xcrossing.send_event);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->display, (int)lpXcrossingevent->xcrossing.display);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->window, lpXcrossingevent->xcrossing.window);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->root, lpXcrossingevent->xcrossing.root);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->subwindow, lpXcrossingevent->xcrossing.subwindow);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->time, lpXcrossingevent->xcrossing.time);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->x, lpXcrossingevent->xcrossing.x);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->y, lpXcrossingevent->xcrossing.y);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->x_root, lpXcrossingevent->xcrossing.x_root);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->y_root, lpXcrossingevent->xcrossing.y_root);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->mode, lpXcrossingevent->xcrossing.mode);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->detail, lpXcrossingevent->xcrossing.detail);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->same_screen, lpXcrossingevent->xcrossing.same_screen);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->focus, lpXcrossingevent->xcrossing.focus);
    (*env)->SetIntField(env,lpObject,lpXcrossingeventFc->state, lpXcrossingevent->xcrossing.state);
}

void getXexposeeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XEXPOSEEVENT_FID_CACHE *lpXexposeeventFc)
{
    lpXevent->type = (*env)->GetIntField(env,lpObject,lpXexposeeventFc->type);
    lpXevent->xexpose.serial = (*env)->GetIntField(env,lpObject,lpXexposeeventFc->serial);
    lpXevent->xexpose.send_event = (*env)->GetIntField(env,lpObject,lpXexposeeventFc->send_event);
    lpXevent->xexpose.display = (Display *)(*env)->GetIntField(env,lpObject,lpXexposeeventFc->display);
    lpXevent->xexpose.window = (*env)->GetIntField(env,lpObject,lpXexposeeventFc->window);
    lpXevent->xexpose.x = (*env)->GetIntField(env,lpObject,lpXexposeeventFc->x);
    lpXevent->xexpose.y = (*env)->GetIntField(env,lpObject,lpXexposeeventFc->y);
    lpXevent->xexpose.width = (*env)->GetIntField(env,lpObject,lpXexposeeventFc->width);
    lpXevent->xexpose.height = (*env)->GetIntField(env,lpObject,lpXexposeeventFc->height);
    lpXevent->xexpose.count = (*env)->GetIntField(env,lpObject,lpXexposeeventFc->count);
}

void setXexposeeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XEXPOSEEVENT_FID_CACHE *lpXexposeeventFc)
{
    (*env)->SetIntField(env,lpObject,lpXexposeeventFc->type, lpXevent->type);
    (*env)->SetIntField(env,lpObject,lpXexposeeventFc->serial, lpXevent->xexpose.serial);
    (*env)->SetIntField(env,lpObject,lpXexposeeventFc->send_event, lpXevent->xexpose.send_event);
    (*env)->SetIntField(env,lpObject,lpXexposeeventFc->display, (int)lpXevent->xexpose.display);
    (*env)->SetIntField(env,lpObject,lpXexposeeventFc->window, lpXevent->xexpose.window);
    (*env)->SetIntField(env,lpObject,lpXexposeeventFc->x, lpXevent->xexpose.x);
    (*env)->SetIntField(env,lpObject,lpXexposeeventFc->y, lpXevent->xexpose.y);
    (*env)->SetIntField(env,lpObject,lpXexposeeventFc->width, lpXevent->xexpose.width);
    (*env)->SetIntField(env,lpObject,lpXexposeeventFc->height, lpXevent->xexpose.height);
    (*env)->SetIntField(env,lpObject,lpXexposeeventFc->count, lpXevent->xexpose.count);
}

void getXfontstructFields(JNIEnv *env, jobject lpObject, XFontStruct *lpXfontstruct, XFONTSTRUCT_FID_CACHE *lpXfontstructFc)
{
    lpXfontstruct->ext_data = (XExtData *)(*env)->GetIntField(env,lpObject,lpXfontstructFc->ext_data);
    lpXfontstruct->fid = (Font)(*env)->GetIntField(env,lpObject,lpXfontstructFc->fid);
    lpXfontstruct->direction = (unsigned)(*env)->GetIntField(env,lpObject,lpXfontstructFc->direction);
    lpXfontstruct->min_char_or_byte2 = (unsigned)(*env)->GetIntField(env,lpObject,lpXfontstructFc->min_char_or_byte2);
    lpXfontstruct->max_char_or_byte2 = (unsigned)(*env)->GetIntField(env,lpObject,lpXfontstructFc->max_char_or_byte2);
    lpXfontstruct->min_byte1 = (unsigned)(*env)->GetIntField(env,lpObject,lpXfontstructFc->min_byte1);
    lpXfontstruct->max_byte1 = (unsigned)(*env)->GetIntField(env,lpObject,lpXfontstructFc->max_byte1);
    lpXfontstruct->all_chars_exist = (Bool)(*env)->GetIntField(env,lpObject,lpXfontstructFc->all_chars_exist);
    lpXfontstruct->default_char = (unsigned)(*env)->GetIntField(env,lpObject,lpXfontstructFc->default_char);
    lpXfontstruct->n_properties = (*env)->GetIntField(env,lpObject,lpXfontstructFc->n_properties);
    lpXfontstruct->properties = (XFontProp *)(*env)->GetIntField(env,lpObject,lpXfontstructFc->properties);
    lpXfontstruct->min_bounds.lbearing = (*env)->GetShortField(env,lpObject,lpXfontstructFc->min_bounds_lbearing);
    lpXfontstruct->min_bounds.rbearing = (*env)->GetShortField(env,lpObject,lpXfontstructFc->min_bounds_rbearing);
    lpXfontstruct->min_bounds.width = (*env)->GetShortField(env,lpObject,lpXfontstructFc->min_bounds_lbearing);
    lpXfontstruct->min_bounds.ascent = (*env)->GetShortField(env,lpObject,lpXfontstructFc->min_bounds_lbearing);
    lpXfontstruct->min_bounds.descent = (*env)->GetShortField(env,lpObject,lpXfontstructFc->min_bounds_lbearing);
    lpXfontstruct->min_bounds.attributes = (*env)->GetShortField(env,lpObject,lpXfontstructFc->min_bounds_lbearing);
    lpXfontstruct->max_bounds.lbearing = (*env)->GetShortField(env,lpObject,lpXfontstructFc->max_bounds_lbearing);
    lpXfontstruct->max_bounds.rbearing = (*env)->GetShortField(env,lpObject,lpXfontstructFc->max_bounds_rbearing);
    lpXfontstruct->max_bounds.width = (*env)->GetShortField(env,lpObject,lpXfontstructFc->max_bounds_width);
    lpXfontstruct->max_bounds.ascent = (*env)->GetShortField(env,lpObject,lpXfontstructFc->max_bounds_ascent);
    lpXfontstruct->max_bounds.descent = (*env)->GetShortField(env,lpObject,lpXfontstructFc->max_bounds_descent);
    lpXfontstruct->max_bounds.attributes = (*env)->GetShortField(env,lpObject,lpXfontstructFc->max_bounds_attributes);
    lpXfontstruct->per_char = (XCharStruct *)(*env)->GetIntField(env,lpObject,lpXfontstructFc->per_char);
    lpXfontstruct->ascent = (*env)->GetIntField(env,lpObject,lpXfontstructFc->ascent);
    lpXfontstruct->descent = (*env)->GetIntField(env,lpObject,lpXfontstructFc->descent);
}

void setXfontstructFields(JNIEnv *env, jobject lpObject, XFontStruct *lpXfontstruct, XFONTSTRUCT_FID_CACHE *lpXfontstructFc)
{
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->ext_data, (jint)lpXfontstruct->ext_data);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->fid, (jint)lpXfontstruct->fid);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->direction, (jint)lpXfontstruct->direction);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->min_char_or_byte2, (jint)lpXfontstruct->min_char_or_byte2);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->max_char_or_byte2, (jint)lpXfontstruct->max_char_or_byte2);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->min_byte1, (jint)lpXfontstruct->min_byte1);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->max_byte1, (jint)lpXfontstruct->max_byte1);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->all_chars_exist, (jint)lpXfontstruct->all_chars_exist);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->default_char, (jint)lpXfontstruct->default_char);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->n_properties, (jint)lpXfontstruct->n_properties);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->properties, (jint)lpXfontstruct->properties);
    (*env)->SetShortField(env,lpObject,lpXfontstructFc->min_bounds_lbearing, (jint)lpXfontstruct->min_bounds.lbearing);
    (*env)->SetShortField(env,lpObject,lpXfontstructFc->min_bounds_rbearing, (jint)lpXfontstruct->min_bounds.rbearing);
    (*env)->SetShortField(env,lpObject,lpXfontstructFc->min_bounds_width, (jint)lpXfontstruct->min_bounds.width);
    (*env)->SetShortField(env,lpObject,lpXfontstructFc->min_bounds_ascent, (jint)lpXfontstruct->min_bounds.ascent);
    (*env)->SetShortField(env,lpObject,lpXfontstructFc->min_bounds_descent, (jint)lpXfontstruct->min_bounds.descent);
    (*env)->SetShortField(env,lpObject,lpXfontstructFc->min_bounds_attributes, (jint)lpXfontstruct->min_bounds.attributes);
    (*env)->SetShortField(env,lpObject,lpXfontstructFc->max_bounds_lbearing, (jint)lpXfontstruct->max_bounds.lbearing);
    (*env)->SetShortField(env,lpObject,lpXfontstructFc->max_bounds_rbearing, (jint)lpXfontstruct->max_bounds.rbearing);
    (*env)->SetShortField(env,lpObject,lpXfontstructFc->max_bounds_width, (jint)lpXfontstruct->max_bounds.width);
    (*env)->SetShortField(env,lpObject,lpXfontstructFc->max_bounds_ascent, (jint)lpXfontstruct->max_bounds.ascent);
    (*env)->SetShortField(env,lpObject,lpXfontstructFc->max_bounds_descent, (jint)lpXfontstruct->max_bounds.descent);
    (*env)->SetShortField(env,lpObject,lpXfontstructFc->max_bounds_attributes, (jint)lpXfontstruct->max_bounds.attributes);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->per_char, (jint)lpXfontstruct->per_char);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->ascent, (jint)lpXfontstruct->ascent);
    (*env)->SetIntField(env,lpObject,lpXfontstructFc->descent, (jint)lpXfontstruct->descent);
}


void getXfocuschangeeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XFOCUSCHANGEEVENT_FID_CACHE *lpXfocuschangeeventFc)
{
    int i;

    lpXevent->type = (*env)->GetIntField(env,lpObject,lpXfocuschangeeventFc->type);
    lpXevent->xfocus.serial = (*env)->GetIntField(env,lpObject,lpXfocuschangeeventFc->serial);
    lpXevent->xfocus.send_event = (*env)->GetIntField(env,lpObject,lpXfocuschangeeventFc->send_event);
    lpXevent->xfocus.display = (Display *)(*env)->GetIntField(env,lpObject,lpXfocuschangeeventFc->display);
    lpXevent->xfocus.window = (*env)->GetIntField(env,lpObject,lpXfocuschangeeventFc->window);
    lpXevent->xfocus.mode = (*env)->GetIntField(env,lpObject,lpXfocuschangeeventFc->mode);
    lpXevent->xfocus.detail = (*env)->GetIntField(env,lpObject,lpXfocuschangeeventFc->detail);
    for (i=0; i<17; i++) {
    	lpXevent->pad[i+7] = (*env)->GetIntField(env,lpObject,lpXfocuschangeeventFc->pad[i]);
    }
}

void setXfocuschangeeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XFOCUSCHANGEEVENT_FID_CACHE *lpXfocuschangeeventFc)
{
    int i;

    (*env)->SetIntField(env,lpObject,lpXfocuschangeeventFc->type, lpXevent->type);
    (*env)->SetIntField(env,lpObject,lpXfocuschangeeventFc->serial, lpXevent->xfocus.serial);
    (*env)->SetIntField(env,lpObject,lpXfocuschangeeventFc->send_event, lpXevent->xfocus.send_event);
    (*env)->SetIntField(env,lpObject,lpXfocuschangeeventFc->display, (int)lpXevent->xfocus.display);
    (*env)->SetIntField(env,lpObject,lpXfocuschangeeventFc->window, lpXevent->xfocus.window);
    (*env)->SetIntField(env,lpObject,lpXfocuschangeeventFc->mode, lpXevent->xfocus.mode);
    (*env)->SetIntField(env,lpObject,lpXfocuschangeeventFc->detail, lpXevent->xfocus.detail);
    for (i=0; i<17; i++) {
	    (*env)->SetIntField(env,lpObject,lpXfocuschangeeventFc->pad[i],lpXevent->pad[i+7]);
    }
}

void getXimageFields(JNIEnv *env, jobject lpObject, XImage *lpXimage, XIMAGE_FID_CACHE *lpXimageFc)
{
    lpXimage->width = (*env)->GetIntField(env,lpObject,lpXimageFc->width);
    lpXimage->height = (*env)->GetIntField(env,lpObject,lpXimageFc->height);
    lpXimage->xoffset = (*env)->GetIntField(env,lpObject,lpXimageFc->xoffset);
    lpXimage->format = (*env)->GetIntField(env,lpObject,lpXimageFc->format);
    lpXimage->data = (char *)(*env)->GetIntField(env,lpObject,lpXimageFc->data);
    lpXimage->byte_order = (*env)->GetIntField(env,lpObject,lpXimageFc->byte_order);
    lpXimage->bitmap_unit = (*env)->GetIntField(env,lpObject,lpXimageFc->bitmap_unit);
    lpXimage->bitmap_bit_order = (*env)->GetIntField(env,lpObject,lpXimageFc->bitmap_bit_order);
    lpXimage-> bitmap_pad= (*env)->GetIntField(env,lpObject,lpXimageFc->bitmap_pad);
    lpXimage->depth = (*env)->GetIntField(env,lpObject,lpXimageFc->depth);
    lpXimage->bytes_per_line = (*env)->GetIntField(env,lpObject,lpXimageFc->bytes_per_line);
    lpXimage->bits_per_pixel = (*env)->GetIntField(env,lpObject,lpXimageFc->bits_per_pixel);
    lpXimage->red_mask = (*env)->GetIntField(env,lpObject,lpXimageFc->red_mask);
    lpXimage->green_mask = (*env)->GetIntField(env,lpObject,lpXimageFc->green_mask);
    lpXimage->blue_mask = (*env)->GetIntField(env,lpObject,lpXimageFc->blue_mask);
    lpXimage->obdata = (char *)(*env)->GetIntField(env,lpObject,lpXimageFc->obdata);
    lpXimage->f.create_image = (struct _XImage *(*)())(*env)->GetIntField(env,lpObject,lpXimageFc->create_image);
    lpXimage->f.destroy_image = (int (*)(struct _XImage *))(*env)->GetIntField(env,lpObject,lpXimageFc->destroy_image);
    lpXimage->f.get_pixel = (unsigned long (*)(struct _XImage *, int, int))(*env)->GetIntField(env,lpObject,lpXimageFc->get_pixel);
    lpXimage->f.put_pixel = (int (*)(struct _XImage *, int, int, unsigned long))(*env)->GetIntField(env,lpObject,lpXimageFc->put_pixel);
    lpXimage->f.sub_image = (struct _XImage *(*)(struct _XImage *, int, int, unsigned int, unsigned int))(*env)->GetIntField(env,lpObject,lpXimageFc->sub_image);
    lpXimage->f.add_pixel = (int (*)(struct _XImage *, long))(*env)->GetIntField(env,lpObject,lpXimageFc->add_pixel);
}

void setXimageFields(JNIEnv *env, jobject lpObject, XImage *lpXimage, XIMAGE_FID_CACHE *lpXimageFc)
{
    (*env)->SetIntField(env,lpObject,lpXimageFc->width, lpXimage->width);
    (*env)->SetIntField(env,lpObject,lpXimageFc->height, lpXimage->height);
    (*env)->SetIntField(env,lpObject,lpXimageFc->xoffset, lpXimage->xoffset);
    (*env)->SetIntField(env,lpObject,lpXimageFc->format, lpXimage->format);
    (*env)->SetIntField(env,lpObject,lpXimageFc->data, (jint)lpXimage->data);
    (*env)->SetIntField(env,lpObject,lpXimageFc->byte_order, lpXimage->byte_order);
    (*env)->SetIntField(env,lpObject,lpXimageFc->bitmap_unit, lpXimage->bitmap_unit);
    (*env)->SetIntField(env,lpObject,lpXimageFc->bitmap_bit_order, lpXimage->bitmap_bit_order);
    (*env)->SetIntField(env,lpObject,lpXimageFc->bitmap_pad, lpXimage->bitmap_pad);
    (*env)->SetIntField(env,lpObject,lpXimageFc->depth, lpXimage->depth);
    (*env)->SetIntField(env,lpObject,lpXimageFc->bytes_per_line, lpXimage->bytes_per_line);
    (*env)->SetIntField(env,lpObject,lpXimageFc->bits_per_pixel, lpXimage->bits_per_pixel);
    (*env)->SetIntField(env,lpObject,lpXimageFc->red_mask, lpXimage->red_mask);
    (*env)->SetIntField(env,lpObject,lpXimageFc->green_mask, lpXimage->green_mask);
    (*env)->SetIntField(env,lpObject,lpXimageFc->blue_mask, lpXimage->blue_mask);
    (*env)->SetIntField(env,lpObject,lpXimageFc->obdata, (jint)lpXimage->obdata);
    (*env)->SetIntField(env,lpObject,lpXimageFc->create_image, (jint)lpXimage->f.create_image);
    (*env)->SetIntField(env,lpObject,lpXimageFc->destroy_image, (jint)lpXimage->f.destroy_image);
    (*env)->SetIntField(env,lpObject,lpXimageFc->get_pixel, (jint)lpXimage->f.get_pixel);
    (*env)->SetIntField(env,lpObject,lpXimageFc->put_pixel, (jint)lpXimage->f.put_pixel);
    (*env)->SetIntField(env,lpObject,lpXimageFc->sub_image, (jint)lpXimage->f.sub_image);
    (*env)->SetIntField(env,lpObject,lpXimageFc->add_pixel, (jint)lpXimage->f.add_pixel);
}

void getXkeyeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XKEYEVENT_FID_CACHE *lpXkeyeventFc)
{
    lpXevent->type = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->type);
    lpXevent->xkey.serial = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->serial);
    lpXevent->xkey.send_event = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->send_event);
    lpXevent->xkey.display = (Display *)(*env)->GetIntField(env,lpObject,lpXkeyeventFc->display);
    lpXevent->xkey.window = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->window);
    lpXevent->xkey.root = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->root);
    lpXevent->xkey.subwindow = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->subwindow);
    lpXevent->xkey.time = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->time);
    lpXevent->xkey.x = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->x);
    lpXevent->xkey.y = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->y);
    lpXevent->xkey.x_root = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->x_root);
    lpXevent->xkey.y_root = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->y_root);
    lpXevent->xkey.state = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->state);
    lpXevent->xkey.keycode = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->keycode);
    lpXevent->xkey.same_screen = (*env)->GetIntField(env,lpObject,lpXkeyeventFc->same_screen);
}

void setXkeyeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XKEYEVENT_FID_CACHE *lpXkeyeventFc)
{
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->type, lpXevent->type);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->serial, lpXevent->xkey.serial);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->send_event, lpXevent->xkey.send_event);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->display, (int)lpXevent->xkey.display);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->window, lpXevent->xkey.window);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->root, lpXevent->xkey.root);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->subwindow, lpXevent->xkey.subwindow);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->time, lpXevent->xkey.time);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->x, lpXevent->xkey.x);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->y, lpXevent->xkey.y);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->x_root, lpXevent->xkey.x_root);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->y_root, lpXevent->xkey.y_root);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->state, lpXevent->xkey.state);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->keycode, lpXevent->xkey.keycode);
    (*env)->SetIntField(env,lpObject,lpXkeyeventFc->same_screen, lpXevent->xkey.same_screen);
}

void getXmotioneventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XMOTIONEVENT_FID_CACHE *lpXmotioneventFc)
{
    int i;

    lpXevent->type = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->type);
    lpXevent->xmotion.serial = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->serial);
    lpXevent->xmotion.send_event = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->send_event);
    lpXevent->xmotion.display = (Display *)(*env)->GetIntField(env,lpObject,lpXmotioneventFc->display);
    lpXevent->xmotion.window = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->window);
    lpXevent->xmotion.root = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->root);
    lpXevent->xmotion.subwindow = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->subwindow);
    lpXevent->xmotion.time = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->time);
    lpXevent->xmotion.x = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->x);
    lpXevent->xmotion.y = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->y);
    lpXevent->xmotion.x_root = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->x_root);
    lpXevent->xmotion.y_root = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->y_root);
    lpXevent->xmotion.state = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->state);
    lpXevent->xmotion.is_hint = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->is_hint);
    lpXevent->xmotion.same_screen = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->same_screen);
    for (i=0; i<10; i++) {
    	lpXevent->pad[i+15] = (*env)->GetIntField(env,lpObject,lpXmotioneventFc->pad[i]);
    }
}

void setXmotioneventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XMOTIONEVENT_FID_CACHE *lpXmotioneventFc)
{
    int i;

    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->type, lpXevent->type);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->serial, lpXevent->xmotion.serial);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->send_event, lpXevent->xmotion.send_event);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->display, (int)lpXevent->xmotion.display);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->window, lpXevent->xmotion.window);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->root, lpXevent->xmotion.root);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->subwindow, lpXevent->xmotion.subwindow);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->time, lpXevent->xmotion.time);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->x, lpXevent->xmotion.x);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->y, lpXevent->xmotion.y);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->x_root, lpXevent->xmotion.x_root);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->y_root, lpXevent->xmotion.y_root);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->state, lpXevent->xmotion.state);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->is_hint, lpXevent->xmotion.is_hint);
    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->same_screen, lpXevent->xmotion.same_screen);
    for (i=0; i<10; i++) {
	    (*env)->SetIntField(env,lpObject,lpXmotioneventFc->pad[i],lpXevent->pad[i+15]);
    }
}

void getXcolorFields(JNIEnv *env, jobject lpObject, XColor *lpXcolor, PXCOLOR_FID_CACHE lpXcolorFc)
{
    lpXcolor->pixel = (*env)->GetIntField(env,lpObject,lpXcolorFc->pixel);
    lpXcolor->red = (*env)->GetShortField(env,lpObject,lpXcolorFc->red);
    lpXcolor->green = (*env)->GetShortField(env,lpObject,lpXcolorFc->green);
    lpXcolor->blue = (*env)->GetShortField(env,lpObject,lpXcolorFc->blue);
    lpXcolor->flags = (*env)->GetByteField(env,lpObject,lpXcolorFc->flags);
    lpXcolor->pad = (*env)->GetByteField(env,lpObject,lpXcolorFc->pad);
}

void setXcolorFields(JNIEnv *env, jobject lpObject, XColor *lpXcolor, PXCOLOR_FID_CACHE lpXcolorFc)
{
    (*env)->SetIntField(env,lpObject,lpXcolorFc->pixel, lpXcolor->pixel);
    (*env)->SetShortField(env,lpObject,lpXcolorFc->red, lpXcolor->red);
    (*env)->SetShortField(env,lpObject,lpXcolorFc->green, lpXcolor->green);
    (*env)->SetShortField(env,lpObject,lpXcolorFc->blue, lpXcolor->blue);
    (*env)->SetByteField(env,lpObject,lpXcolorFc->flags, lpXcolor->flags);
    (*env)->SetByteField(env,lpObject,lpXcolorFc->pad, lpXcolor->pad);
}

void getXgcvaluesFields(JNIEnv *env, jobject lpObject, XGCValues *lpXgcvalues, PXGCVALUES_FID_CACHE lpXgcvaluesFc)
{
    lpXgcvalues->function = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->function);
    lpXgcvalues->plane_mask = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->plane_mask);
    lpXgcvalues->foreground = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->foreground);
    lpXgcvalues->background = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->background);
    lpXgcvalues->line_width = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->line_width);
    lpXgcvalues->line_style = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->line_style);
    lpXgcvalues->cap_style = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->cap_style);
    lpXgcvalues->join_style = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->join_style);
    lpXgcvalues->fill_style = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->fill_style);
    lpXgcvalues->fill_rule = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->fill_rule);
    lpXgcvalues->arc_mode = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->arc_mode);
    lpXgcvalues->tile = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->tile);
    lpXgcvalues->stipple = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->stipple);
    lpXgcvalues->ts_x_origin = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->ts_x_origin);
    lpXgcvalues->ts_y_origin = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->ts_y_origin);
    lpXgcvalues->font = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->font);
    lpXgcvalues->subwindow_mode = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->subwindow_mode);
    lpXgcvalues->graphics_exposures = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->graphics_exposures);
    lpXgcvalues->clip_x_origin = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->clip_x_origin);
    lpXgcvalues->clip_y_origin = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->clip_y_origin);
    lpXgcvalues->clip_mask = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->clip_mask);
    lpXgcvalues->dash_offset = (*env)->GetIntField(env,lpObject,lpXgcvaluesFc->dash_offset);
    lpXgcvalues->dashes = (*env)->GetByteField(env,lpObject,lpXgcvaluesFc->dashes);
}

void setXgcvaluesFields(JNIEnv *env, jobject lpObject, XGCValues *lpXgcvalues, PXGCVALUES_FID_CACHE lpXgcvaluesFc)
{
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->function, lpXgcvalues->function);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->plane_mask, lpXgcvalues->plane_mask);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->foreground, lpXgcvalues->foreground);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->background, lpXgcvalues->background);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->line_width, lpXgcvalues->line_width);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->line_style, lpXgcvalues->line_style);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->cap_style, lpXgcvalues->cap_style);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->join_style, lpXgcvalues->join_style);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->fill_style, lpXgcvalues->fill_style);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->fill_rule, lpXgcvalues->fill_rule);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->arc_mode, lpXgcvalues->arc_mode);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->tile, lpXgcvalues->tile);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->stipple, lpXgcvalues->stipple);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->ts_x_origin, lpXgcvalues->ts_x_origin);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->ts_y_origin, lpXgcvalues->ts_y_origin);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->font, lpXgcvalues->font);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->subwindow_mode, lpXgcvalues->subwindow_mode);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->graphics_exposures, lpXgcvalues->graphics_exposures);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->clip_x_origin, lpXgcvalues->clip_x_origin);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->clip_y_origin, lpXgcvalues->clip_y_origin);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->clip_mask, lpXgcvalues->clip_mask);
    (*env)->SetIntField(env,lpObject,lpXgcvaluesFc->dash_offset, lpXgcvalues->dash_offset);

    (*env)->SetByteField(env,lpObject,lpXgcvaluesFc->dashes, lpXgcvalues->dashes);
}

void getXmanycallbackstructFields(JNIEnv *env, jobject lpObject, XmAnyCallbackStruct *lpXmanycallbackstruct, PXMANYCALLBACKSTRUCT_FID_CACHE lpXmanycallbackstructFc)
{
    lpXmanycallbackstruct->reason = (*env)->GetIntField(env,lpObject,lpXmanycallbackstructFc->reason);
    lpXmanycallbackstruct->event = (XEvent *)(*env)->GetIntField(env,lpObject,lpXmanycallbackstructFc->event);
}

void setXmanycallbackstructFields(JNIEnv *env, jobject lpObject, XmAnyCallbackStruct *lpXmanycallbackstruct, PXMANYCALLBACKSTRUCT_FID_CACHE lpXmanycallbackstructFc)
{
    (*env)->SetIntField(env,lpObject,lpXmanycallbackstructFc->reason, lpXmanycallbackstruct->reason);
    (*env)->SetIntField(env,lpObject,lpXmanycallbackstructFc->event, (jint)lpXmanycallbackstruct->event);
}

void getXmdragproccallbackFields(JNIEnv *env, jobject lpObject, XmDragProcCallbackStruct *lpXmdragproccallback, PXMDRAGPROCCALLBACK_FID_CACHE lpXmdragproccallbackFc)
{
    lpXmdragproccallback->reason = (*env)->GetIntField(env,lpObject,lpXmdragproccallbackFc->reason);
    lpXmdragproccallback->event = (XEvent *)(*env)->GetIntField(env,lpObject,lpXmdragproccallbackFc->event);
    lpXmdragproccallback->timeStamp = (*env)->GetIntField(env,lpObject,lpXmdragproccallbackFc->timeStamp);
    lpXmdragproccallback->dragContext = (Widget)(*env)->GetIntField(env,lpObject,lpXmdragproccallbackFc->dragContext);
    lpXmdragproccallback->x = (*env)->GetShortField(env,lpObject,lpXmdragproccallbackFc->x);
    lpXmdragproccallback->y = (*env)->GetShortField(env,lpObject,lpXmdragproccallbackFc->y);
    lpXmdragproccallback->dropSiteStatus = (*env)->GetByteField(env,lpObject,lpXmdragproccallbackFc->dropSiteStatus);
    lpXmdragproccallback->operation = (*env)->GetByteField(env,lpObject,lpXmdragproccallbackFc->operation);
    lpXmdragproccallback->operations = (*env)->GetByteField(env,lpObject,lpXmdragproccallbackFc->operations);
    lpXmdragproccallback->animate = (*env)->GetByteField(env,lpObject,lpXmdragproccallbackFc->animate);
}

void setXmdragproccallbackFields(JNIEnv *env, jobject lpObject, XmDragProcCallbackStruct *lpXmdragproccallback, PXMDRAGPROCCALLBACK_FID_CACHE lpXmdragproccallbackFc)
{
    (*env)->SetIntField(env,lpObject,lpXmdragproccallbackFc->reason, lpXmdragproccallback->reason);
    (*env)->SetIntField(env,lpObject,lpXmdragproccallbackFc->event, (jint)lpXmdragproccallback->event);
    (*env)->SetIntField(env,lpObject,lpXmdragproccallbackFc->timeStamp, lpXmdragproccallback->timeStamp);
    (*env)->SetIntField(env,lpObject,lpXmdragproccallbackFc->dragContext, (jint)lpXmdragproccallback->dragContext);
    (*env)->SetShortField(env,lpObject,lpXmdragproccallbackFc->x, lpXmdragproccallback->x);
    (*env)->SetShortField(env,lpObject,lpXmdragproccallbackFc->y, lpXmdragproccallback->y);
    (*env)->SetByteField(env,lpObject,lpXmdragproccallbackFc->dropSiteStatus, lpXmdragproccallback->dropSiteStatus);
    (*env)->SetByteField(env,lpObject,lpXmdragproccallbackFc->operation, lpXmdragproccallback->operation);
    (*env)->SetByteField(env,lpObject,lpXmdragproccallbackFc->operations, lpXmdragproccallback->operations);
    (*env)->SetByteField(env,lpObject,lpXmdragproccallbackFc->animate, lpXmdragproccallback->animate);
}

void getXmdropproccallbackFields(JNIEnv *env, jobject lpObject, XmDropProcCallbackStruct *lpXmdropproccallback, PXMDROPPROCCALLBACK_FID_CACHE lpXmdropproccallbackFc)
{
    lpXmdropproccallback->reason = (*env)->GetIntField(env,lpObject,lpXmdropproccallbackFc->reason);
    lpXmdropproccallback->event = (XEvent *)(*env)->GetIntField(env,lpObject,lpXmdropproccallbackFc->event);
    lpXmdropproccallback->timeStamp = (*env)->GetIntField(env,lpObject,lpXmdropproccallbackFc->timeStamp);
    lpXmdropproccallback->dragContext = (Widget)(*env)->GetIntField(env,lpObject,lpXmdropproccallbackFc->dragContext);
    lpXmdropproccallback->x = (*env)->GetShortField(env,lpObject,lpXmdropproccallbackFc->x);
    lpXmdropproccallback->y = (*env)->GetShortField(env,lpObject,lpXmdropproccallbackFc->y);
    lpXmdropproccallback->dropSiteStatus = (*env)->GetByteField(env,lpObject,lpXmdropproccallbackFc->dropSiteStatus);
    lpXmdropproccallback->operation = (*env)->GetByteField(env,lpObject,lpXmdropproccallbackFc->operation);
    lpXmdropproccallback->operations = (*env)->GetByteField(env,lpObject,lpXmdropproccallbackFc->operations);
    lpXmdropproccallback->dropAction = (*env)->GetByteField(env,lpObject,lpXmdropproccallbackFc->dropAction);
}

void setXmdropproccallbackFields(JNIEnv *env, jobject lpObject, XmDropProcCallbackStruct *lpXmdropproccallback, PXMDROPPROCCALLBACK_FID_CACHE lpXmdropproccallbackFc)
{
    (*env)->SetIntField(env,lpObject,lpXmdropproccallbackFc->reason, lpXmdropproccallback->reason);
    (*env)->SetIntField(env,lpObject,lpXmdropproccallbackFc->event, (jint)lpXmdropproccallback->event);
    (*env)->SetIntField(env,lpObject,lpXmdropproccallbackFc->timeStamp, lpXmdropproccallback->timeStamp);
    (*env)->SetIntField(env,lpObject,lpXmdropproccallbackFc->dragContext, (jint)lpXmdropproccallback->dragContext);
    (*env)->SetShortField(env,lpObject,lpXmdropproccallbackFc->x, lpXmdropproccallback->x);
    (*env)->SetShortField(env,lpObject,lpXmdropproccallbackFc->y, lpXmdropproccallback->y);
    (*env)->SetByteField(env,lpObject,lpXmdropproccallbackFc->dropSiteStatus, lpXmdropproccallback->dropSiteStatus);
    (*env)->SetByteField(env,lpObject,lpXmdropproccallbackFc->operation, lpXmdropproccallback->operation);
    (*env)->SetByteField(env,lpObject,lpXmdropproccallbackFc->operations, lpXmdropproccallback->operations);
    (*env)->SetByteField(env,lpObject,lpXmdropproccallbackFc->dropAction, lpXmdropproccallback->dropAction);
}

void getXmdropfinishcallbackFields(JNIEnv *env, jobject lpObject, XmDropFinishCallbackStruct *lpXmdropfinishcallback, PXMDROPFINISHCALLBACK_FID_CACHE lpXmdropfinishcallbackFc)
{
    lpXmdropfinishcallback->reason = (*env)->GetIntField(env,lpObject,lpXmdropfinishcallbackFc->reason);
    lpXmdropfinishcallback->event = (XEvent *)(*env)->GetIntField(env,lpObject,lpXmdropfinishcallbackFc->event);
    lpXmdropfinishcallback->timeStamp = (*env)->GetIntField(env,lpObject,lpXmdropfinishcallbackFc->timeStamp);
    lpXmdropfinishcallback->operation = (*env)->GetByteField(env,lpObject,lpXmdropfinishcallbackFc->operation);
    lpXmdropfinishcallback->operations = (*env)->GetByteField(env,lpObject,lpXmdropfinishcallbackFc->operations);
    lpXmdropfinishcallback->dropSiteStatus = (*env)->GetByteField(env,lpObject,lpXmdropfinishcallbackFc->dropSiteStatus);
    lpXmdropfinishcallback->dropAction = (*env)->GetByteField(env,lpObject,lpXmdropfinishcallbackFc->dropAction);
    lpXmdropfinishcallback->completionStatus = (*env)->GetByteField(env,lpObject,lpXmdropfinishcallbackFc->completionStatus);
}

void setXmdropfinishcallbackFields(JNIEnv *env, jobject lpObject, XmDropFinishCallbackStruct *lpXmdropfinishcallback, PXMDROPFINISHCALLBACK_FID_CACHE lpXmdropfinishcallbackFc)
{
    (*env)->SetIntField(env,lpObject,lpXmdropfinishcallbackFc->reason, lpXmdropfinishcallback->reason);
    (*env)->SetIntField(env,lpObject,lpXmdropfinishcallbackFc->event, (jint)lpXmdropfinishcallback->event);
    (*env)->SetIntField(env,lpObject,lpXmdropfinishcallbackFc->timeStamp, lpXmdropfinishcallback->timeStamp);
    (*env)->SetByteField(env,lpObject,lpXmdropfinishcallbackFc->operation, lpXmdropfinishcallback->operation);
    (*env)->SetByteField(env,lpObject,lpXmdropfinishcallbackFc->operations, lpXmdropfinishcallback->operations);
    (*env)->SetByteField(env,lpObject,lpXmdropfinishcallbackFc->dropSiteStatus, lpXmdropfinishcallback->dropSiteStatus);
    (*env)->SetByteField(env,lpObject,lpXmdropfinishcallbackFc->dropAction, lpXmdropfinishcallback->dropAction);    
    (*env)->SetByteField(env,lpObject,lpXmdropfinishcallbackFc->completionStatus, lpXmdropfinishcallback->completionStatus);
}

void getXmtextblockrecFields(JNIEnv *env, jobject lpObject, XmTextBlockRec *lpXmtextblockrec, PXMTEXTBLOCKREC_FID_CACHE lpXmtextblockrecFc)
{
    lpXmtextblockrec->ptr = (char *)(*env)->GetIntField(env,lpObject,lpXmtextblockrecFc->ptr);
    lpXmtextblockrec->length = (*env)->GetIntField(env,lpObject,lpXmtextblockrecFc->length);
    lpXmtextblockrec->format = (XmTextFormat)(*env)->GetIntField(env,lpObject,lpXmtextblockrecFc->format);
}

void setXmtextblockrecFields(JNIEnv *env, jobject lpObject, XmTextBlockRec *lpXmtextblockrec, PXMTEXTBLOCKREC_FID_CACHE lpXmtextblockrecFc)
{
    (*env)->SetIntField(env,lpObject,lpXmtextblockrecFc->ptr, (jint)lpXmtextblockrec->ptr);
    (*env)->SetIntField(env,lpObject,lpXmtextblockrecFc->length, lpXmtextblockrec->length);
    (*env)->SetIntField(env,lpObject,lpXmtextblockrecFc->format, (jint)lpXmtextblockrec->format);
}

void getXmtextverifycallbackstructFields(JNIEnv *env, jobject lpObject, XmTextVerifyCallbackStruct *lpXmtextverifycallbackstruct, PXMTEXTVERIFYCALLBACKSTRUCT_FID_CACHE lpXmtextverifycallbackstructFc)
{
    lpXmtextverifycallbackstruct->reason = (*env)->GetIntField(env,lpObject,lpXmtextverifycallbackstructFc->reason);
    lpXmtextverifycallbackstruct->event = (XEvent *)(*env)->GetIntField(env,lpObject,lpXmtextverifycallbackstructFc->event);
    lpXmtextverifycallbackstruct->doit = (Boolean)(*env)->GetByteField(env,lpObject,lpXmtextverifycallbackstructFc->doit);
    lpXmtextverifycallbackstruct->currInsert = (*env)->GetIntField(env,lpObject,lpXmtextverifycallbackstructFc->currInsert);
    lpXmtextverifycallbackstruct->newInsert = (*env)->GetIntField(env,lpObject,lpXmtextverifycallbackstructFc->newInsert);
    lpXmtextverifycallbackstruct->startPos = (*env)->GetIntField(env,lpObject,lpXmtextverifycallbackstructFc->startPos);
    lpXmtextverifycallbackstruct->endPos = (*env)->GetIntField(env,lpObject,lpXmtextverifycallbackstructFc->endPos);
    lpXmtextverifycallbackstruct->text = (XmTextBlock)(*env)->GetIntField(env,lpObject,lpXmtextverifycallbackstructFc->text);
}

void setXmtextverifycallbackstructFields(JNIEnv *env, jobject lpObject, XmTextVerifyCallbackStruct *lpXmtextverifycallbackstruct, PXMTEXTVERIFYCALLBACKSTRUCT_FID_CACHE lpXmtextverifycallbackstructFc)
{
    (*env)->SetIntField(env,lpObject,lpXmtextverifycallbackstructFc->reason, lpXmtextverifycallbackstruct->reason);
    (*env)->SetIntField(env,lpObject,lpXmtextverifycallbackstructFc->event, (jint)lpXmtextverifycallbackstruct->event);
    (*env)->SetByteField(env,lpObject,lpXmtextverifycallbackstructFc->doit, (jint)lpXmtextverifycallbackstruct->doit);
    (*env)->SetIntField(env,lpObject,lpXmtextverifycallbackstructFc->currInsert, lpXmtextverifycallbackstruct->currInsert);
    (*env)->SetIntField(env,lpObject,lpXmtextverifycallbackstructFc->newInsert, lpXmtextverifycallbackstruct->newInsert);
    (*env)->SetIntField(env,lpObject,lpXmtextverifycallbackstructFc->startPos, lpXmtextverifycallbackstruct->startPos);
    (*env)->SetIntField(env,lpObject,lpXmtextverifycallbackstructFc->endPos, lpXmtextverifycallbackstruct->endPos);
    (*env)->SetIntField(env,lpObject,lpXmtextverifycallbackstructFc->text, (jint)lpXmtextverifycallbackstruct->text);
}

void getXrectangleFields(JNIEnv *env, jobject lpObject, XRectangle *lpXrect, PXRECTANGLE_FID_CACHE lpXrectFc)
{
    lpXrect->x = (*env)->GetShortField(env,lpObject,lpXrectFc->x);
    lpXrect->y = (*env)->GetShortField(env,lpObject,lpXrectFc->y);
    lpXrect->width = (*env)->GetShortField(env,lpObject,lpXrectFc->width);
    lpXrect->height = (*env)->GetShortField(env,lpObject,lpXrectFc->height);
}

void setXrectangleFields(JNIEnv *env, jobject lpObject, XRectangle *lpXrect, PXRECTANGLE_FID_CACHE lpXrectFc)
{
    (*env)->SetShortField(env,lpObject,lpXrectFc->x, lpXrect->x);
    (*env)->SetShortField(env,lpObject,lpXrectFc->y, lpXrect->y);
    (*env)->SetShortField(env,lpObject,lpXrectFc->width, lpXrect->width);
    (*env)->SetShortField(env,lpObject,lpXrectFc->height, lpXrect->height);
}

void getXsetwindowattributesFields(JNIEnv *env, jobject lpObject, XSetWindowAttributes *lpXsetwindowattributes, PXSETWINDOWATTRIBUTES_FID_CACHE lpXsetwindowattributesFc)
{
    lpXsetwindowattributes->background_pixmap = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->background_pixmap);
    lpXsetwindowattributes->background_pixel = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->background_pixel);
    lpXsetwindowattributes->border_pixmap = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->border_pixmap);
    lpXsetwindowattributes->border_pixel = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->border_pixel);
    lpXsetwindowattributes->bit_gravity = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->bit_gravity);
    lpXsetwindowattributes->win_gravity = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->win_gravity);
    lpXsetwindowattributes->backing_store = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->backing_store);
    lpXsetwindowattributes->backing_planes = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->backing_planes);
    lpXsetwindowattributes->backing_pixel = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->backing_pixel);
    lpXsetwindowattributes->save_under = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->save_under);
    lpXsetwindowattributes->event_mask = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->event_mask);
    lpXsetwindowattributes->do_not_propagate_mask = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->do_not_propagate_mask);
    lpXsetwindowattributes->override_redirect = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->override_redirect);
    lpXsetwindowattributes->colormap = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->colormap);
    lpXsetwindowattributes->cursor = (*env)->GetIntField(env,lpObject,lpXsetwindowattributesFc->cursor);
}

void setXsetwindowattributesFields(JNIEnv *env, jobject lpObject, XSetWindowAttributes *lpXsetwindowattributes, PXSETWINDOWATTRIBUTES_FID_CACHE lpXsetwindowattributesFc)
{
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->background_pixmap, lpXsetwindowattributes->background_pixmap);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->background_pixel, lpXsetwindowattributes->background_pixel);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->border_pixmap, lpXsetwindowattributes->border_pixmap);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->border_pixel, lpXsetwindowattributes->border_pixel);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->bit_gravity, lpXsetwindowattributes->bit_gravity);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->win_gravity, lpXsetwindowattributes->win_gravity);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->backing_store, lpXsetwindowattributes->backing_store);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->backing_planes, lpXsetwindowattributes->backing_planes);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->backing_pixel, lpXsetwindowattributes->backing_pixel);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->save_under, lpXsetwindowattributes->save_under);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->event_mask, lpXsetwindowattributes->event_mask);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->do_not_propagate_mask, lpXsetwindowattributes->do_not_propagate_mask);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->override_redirect, lpXsetwindowattributes->override_redirect);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->colormap, lpXsetwindowattributes->colormap);
    (*env)->SetIntField(env,lpObject,lpXsetwindowattributesFc->cursor, lpXsetwindowattributes->cursor);
}

void getXtextpropertyFields(JNIEnv *env, jobject lpObject, XTextProperty *lpXtextproperty, PXTEXTPROPERTY_FID_CACHE lpXtextpropertyFc)
{
    lpXtextproperty->value = (unsigned char *)(*env)->GetIntField(env,lpObject,lpXtextpropertyFc->value);
    lpXtextproperty->encoding = (Atom)(*env)->GetIntField(env,lpObject,lpXtextpropertyFc->encoding);
    lpXtextproperty->format = (*env)->GetIntField(env,lpObject,lpXtextpropertyFc->format);
    lpXtextproperty->nitems = (unsigned long)(*env)->GetIntField(env,lpObject,lpXtextpropertyFc->nitems);
}

void setXtextpropertyFields(JNIEnv *env, jobject lpObject, XTextProperty *lpXtextproperty, PXTEXTPROPERTY_FID_CACHE lpXtextpropertyFc)
{
    (*env)->SetIntField(env,lpObject,lpXtextpropertyFc->value, (int)lpXtextproperty->value);
    (*env)->SetIntField(env,lpObject,lpXtextpropertyFc->encoding, (int)lpXtextproperty->encoding);
    (*env)->SetIntField(env,lpObject,lpXtextpropertyFc->format, lpXtextproperty->format);
    (*env)->SetIntField(env,lpObject,lpXtextpropertyFc->nitems, lpXtextproperty->nitems);
}

void getXwindowattributesFields(JNIEnv *env, jobject lpObject, XWindowAttributes *lpXwindowattributes, PXWINDOWATTRIBUTES_FID_CACHE lpXwindowattributesFc)
{
    lpXwindowattributes->x = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->x);
    lpXwindowattributes->y = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->y);
    lpXwindowattributes->width = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->width);
    lpXwindowattributes->height = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->height);
    lpXwindowattributes->border_width = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->border_width);
    lpXwindowattributes->depth = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->depth);
    lpXwindowattributes->visual = (Visual *)(*env)->GetIntField(env,lpObject,lpXwindowattributesFc->visual);
    lpXwindowattributes->root = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->root);
    lpXwindowattributes->class = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->class);
    lpXwindowattributes->bit_gravity = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->bit_gravity);
    lpXwindowattributes->win_gravity = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->win_gravity);
    lpXwindowattributes->backing_store = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->backing_store);
    lpXwindowattributes->backing_planes = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->backing_planes);
    lpXwindowattributes->backing_pixel = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->backing_pixel);
    lpXwindowattributes->save_under = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->save_under);
    lpXwindowattributes->colormap = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->colormap);
    lpXwindowattributes->map_installed = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->map_installed);
    lpXwindowattributes->map_state = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->map_state);
    lpXwindowattributes->all_event_masks = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->all_event_masks);
    lpXwindowattributes->your_event_mask = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->your_event_mask);
    lpXwindowattributes->do_not_propagate_mask = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->do_not_propagate_mask);
    lpXwindowattributes->override_redirect = (*env)->GetIntField(env,lpObject,lpXwindowattributesFc->override_redirect);
    lpXwindowattributes->screen = (Screen *)(*env)->GetIntField(env,lpObject,lpXwindowattributesFc->screen);
}

void setXwindowattributesFields(JNIEnv *env, jobject lpObject, XWindowAttributes *lpXwindowattributes, PXWINDOWATTRIBUTES_FID_CACHE lpXwindowattributesFc)
{
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->x, lpXwindowattributes->x);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->y, lpXwindowattributes->y);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->width, lpXwindowattributes->width);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->height, lpXwindowattributes->height);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->border_width, lpXwindowattributes->border_width);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->depth, lpXwindowattributes->depth);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->visual, (int)lpXwindowattributes->visual);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->root, lpXwindowattributes->root);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->class, lpXwindowattributes->class);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->bit_gravity, lpXwindowattributes->bit_gravity);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->win_gravity, lpXwindowattributes->win_gravity);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->backing_store, lpXwindowattributes->backing_store);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->backing_planes, lpXwindowattributes->backing_planes);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->backing_pixel, lpXwindowattributes->backing_pixel);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->save_under, lpXwindowattributes->save_under);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->colormap, lpXwindowattributes->colormap);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->map_installed, lpXwindowattributes->map_installed);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->map_state, lpXwindowattributes->map_state);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->all_event_masks, lpXwindowattributes->all_event_masks);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->your_event_mask, lpXwindowattributes->your_event_mask);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->do_not_propagate_mask, lpXwindowattributes->do_not_propagate_mask);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->override_redirect, lpXwindowattributes->override_redirect);
    (*env)->SetIntField(env,lpObject,lpXwindowattributesFc->screen, (int)lpXwindowattributes->screen);
}

void getXwindowchangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpXwindowchanges, PXWINDOWCHANGES_FID_CACHE lpXwindowchangesFc)
{
    lpXwindowchanges->x = (*env)->GetIntField(env,lpObject,lpXwindowchangesFc->x);
    lpXwindowchanges->y = (*env)->GetIntField(env,lpObject,lpXwindowchangesFc->y);
    lpXwindowchanges->width = (*env)->GetIntField(env,lpObject,lpXwindowchangesFc->width);
    lpXwindowchanges->height = (*env)->GetIntField(env,lpObject,lpXwindowchangesFc->height);
    lpXwindowchanges->border_width = (*env)->GetIntField(env,lpObject,lpXwindowchangesFc->border_width);
    lpXwindowchanges->sibling = (*env)->GetIntField(env,lpObject,lpXwindowchangesFc->sibling);
    lpXwindowchanges->stack_mode = (*env)->GetIntField(env,lpObject,lpXwindowchangesFc->stack_mode);
}

void setXwindowchangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpXwindowchanges, PXWINDOWCHANGES_FID_CACHE lpXwindowchangesFc)
{
    (*env)->SetIntField(env,lpObject,lpXwindowchangesFc->x, lpXwindowchanges->x);
    (*env)->SetIntField(env,lpObject,lpXwindowchangesFc->y, lpXwindowchanges->y);
    (*env)->SetIntField(env,lpObject,lpXwindowchangesFc->width, lpXwindowchanges->width);
    (*env)->SetIntField(env,lpObject,lpXwindowchangesFc->height, lpXwindowchanges->height);
    (*env)->SetIntField(env,lpObject,lpXwindowchangesFc->border_width, lpXwindowchanges->border_width);
    (*env)->SetIntField(env,lpObject,lpXwindowchangesFc->sibling, lpXwindowchanges->sibling);
    (*env)->SetIntField(env,lpObject,lpXwindowchangesFc->stack_mode, lpXwindowchanges->stack_mode);
}

void getXtwidgetgeometryFields(JNIEnv *env, jobject lpObject, XtWidgetGeometry *lpXtwidgetgeometry, PXTWIDGETGEOMETRY_FID_CACHE lpXtwidgetgeometryFc)
{    
    lpXtwidgetgeometry->request_mode = (*env)->GetIntField(env,lpObject,lpXtwidgetgeometryFc->request_mode);
    lpXtwidgetgeometry->x = (*env)->GetIntField(env,lpObject,lpXtwidgetgeometryFc->x);
    lpXtwidgetgeometry->y = (*env)->GetIntField(env,lpObject,lpXtwidgetgeometryFc->y);
    lpXtwidgetgeometry->width = (*env)->GetIntField(env,lpObject,lpXtwidgetgeometryFc->width);
    lpXtwidgetgeometry->height = (*env)->GetIntField(env,lpObject,lpXtwidgetgeometryFc->height);
    lpXtwidgetgeometry->border_width = (*env)->GetIntField(env,lpObject,lpXtwidgetgeometryFc->border_width);
    lpXtwidgetgeometry->sibling = (Widget)(*env)->GetIntField(env,lpObject,lpXtwidgetgeometryFc->sibling);
    lpXtwidgetgeometry->stack_mode = (*env)->GetIntField(env,lpObject,lpXtwidgetgeometryFc->stack_mode);
}

void setXtwidgetgeometryFields(JNIEnv *env, jobject lpObject, XtWidgetGeometry *lpXtwidgetgeometry, PXTWIDGETGEOMETRY_FID_CACHE lpXtwidgetgeometryFc)
{
    (*env)->SetIntField(env,lpObject,lpXtwidgetgeometryFc->request_mode, lpXtwidgetgeometry->request_mode);
    (*env)->SetIntField(env,lpObject,lpXtwidgetgeometryFc->x, lpXtwidgetgeometry->x);
    (*env)->SetIntField(env,lpObject,lpXtwidgetgeometryFc->y, lpXtwidgetgeometry->y);
    (*env)->SetIntField(env,lpObject,lpXtwidgetgeometryFc->width, lpXtwidgetgeometry->width);
    (*env)->SetIntField(env,lpObject,lpXtwidgetgeometryFc->height, lpXtwidgetgeometry->height);
    (*env)->SetIntField(env,lpObject,lpXtwidgetgeometryFc->border_width, lpXtwidgetgeometry->border_width);
    (*env)->SetIntField(env,lpObject,lpXtwidgetgeometryFc->sibling, (int)lpXtwidgetgeometry->sibling);
    (*env)->SetIntField(env,lpObject,lpXtwidgetgeometryFc->stack_mode, lpXtwidgetgeometry->stack_mode);
}
