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
 * JNI SWT object field getters and setters declarations for Motif structs
 */

#ifndef INC_structs_H
#define INC_structs_H

#include <X11/X.h>
#include <X11/Xlib.h>
#include <X11/IntrinsicP.h>
#include <X11/Intrinsic.h>
#include <X11/Shell.h>
#include <X11/keysym.h>
#include <X11/extensions/Print.h>

#include <Xm/XmAll.h>
#include <Mrm/MrmPublic.h>

/* ----------- fid and class caches  ----------- */
/**
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

/* VISUAL struct */
typedef struct VISUAL_FID_CACHE {
    
    int cached;
    jclass visualClass;
    jfieldID ext_data, visualid, c_class, red_mask, green_mask, blue_mask, bits_per_rgb, map_entries;

} VISUAL_FID_CACHE;

typedef VISUAL_FID_CACHE *PVISUAL_FID_CACHE;

/* XANYEVENT struct */
typedef struct XANYEVENT_FID_CACHE {
    
    int cached;
    jclass xeventClass;
    jfieldID type, serial, send_event, display, window, pad[19];
/*
    pad0, pad1, pad2, pad3,
             pad4, pad5, pad6, pad7, pad8, pad9, pad10, pad11, pad12, pad13
             pad14, pad15, pad16, pad17, pad18;
*/

} XANYEVENT_FID_CACHE;

typedef XANYEVENT_FID_CACHE *PXANYEVENT_FID_CACHE;

/* XBUTTONEVENT struct */
typedef struct XBUTTONEVENT_FID_CACHE {
    
    int cached;
    jclass xeventClass;
    jfieldID type, serial, send_event, display, window, root, subwindow, time,
             x, y, x_root, y_root, state, button, same_screen;

} XBUTTONEVENT_FID_CACHE;

typedef XBUTTONEVENT_FID_CACHE *PXBUTTONEVENT_FID_CACHE;

/* XCHARSTRUCT struct */
typedef struct XCHARSTRUCT_FID_CACHE {
    
    int cached;
    jclass xcharstructClass;
    jfieldID lbearing, rbearing, width, ascent, descent, attributes; 

} XCHARSTRUCT_FID_CACHE;

typedef XCHARSTRUCT_FID_CACHE *PXCHARSTRUCT_FID_CACHE;

/* XCONFIGUREEVENT struct */
typedef struct XCONFIGUREEVENT_FID_CACHE {
    
    int cached;
    jclass xeventClass;
    jfieldID type, serial, send_event, display, window, root, subwindow, time,
             x, y, width, height, border_width, above, override_redirect;

} XCONFIGUREEVENT_FID_CACHE;

typedef XCONFIGUREEVENT_FID_CACHE *PXCONFIGUREEVENT_FID_CACHE;

/* XCROSSINGEVENT struct */
typedef struct XCROSSINGEVENT_FID_CACHE {

    int cached;
    jclass xcrossingeventClass;
    jfieldID type, serial, send_event, display, window, root, subwindow, time,
             x, y, x_root, y_root, mode, detail, same_screen, focus, 
             state, pad[8];
/*
    pad0, pad1, pad2, pad3, pad4, pad5, pad6, pad7;
*/

} XCROSSINGEVENT_FID_CACHE;

typedef XCROSSINGEVENT_FID_CACHE *PXCROSSINGEVENT_FID_CACHE;

/* XEXPOSEEVENT struct */
typedef struct XEXPOSEEVENT_FID_CACHE {
    
    int cached;
    jclass xeventClass;
    jfieldID type, serial, send_event, display, window, root, subwindow, time,
             x, y, width, height, count;

} XEXPOSEEVENT_FID_CACHE;

typedef XEXPOSEEVENT_FID_CACHE *PXEXPOSEEVENT_FID_CACHE;

/* XFOCUSCHANGEEVENT struct */
typedef struct XFOCUSCHANGEEVENT_FID_CACHE {
    
    int cached;
    jclass xeventClass;
    jfieldID type, serial, send_event, display, window, mode, detail, pad[17];

} XFOCUSCHANGEEVENT_FID_CACHE;

typedef XFOCUSCHANGEEVENT_FID_CACHE *PXFOCUSCHANGEEVENT_FID_CACHE;

/* XFONTSTRUCT struct */
typedef struct XFONTSTRUCT_FID_CACHE {
    
    int cached;
    jclass xfontstructClass;
    jfieldID ext_data, fid, direction, min_char_or_byte2, max_char_or_byte2, \
             min_byte1, max_byte1, all_chars_exist, default_char, n_properties, \
             properties, min_bounds_lbearing, min_bounds_rbearing, min_bounds_width, \
             min_bounds_ascent, min_bounds_descent, min_bounds_attributes, \
             max_bounds_lbearing, max_bounds_rbearing, max_bounds_width, \
             max_bounds_ascent, max_bounds_descent, max_bounds_attributes, per_char, \
             ascent, descent;

} XFONTSTRUCT_FID_CACHE;

typedef XFONTSTRUCT_FID_CACHE *PXFONTSTRUCT_FID_CACHE;

/* XIMAGE struct */
typedef struct XIMAGE_FID_CACHE {

    int cached;
    jclass ximageClass;
    jfieldID width, height, xoffset, format, data, byte_order, bitmap_unit,
             bitmap_bit_order, bitmap_pad, depth, bytes_per_line, bits_per_pixel,
             red_mask, green_mask, blue_mask, obdata, create_image, destroy_image,
             get_pixel, put_pixel, sub_image, add_pixel;

} XIMAGE_FID_CACHE;

typedef XIMAGE_FID_CACHE *PXIMAGE_FID_CACHE;

/* XKEYEVENT struct */
typedef struct XKEYEVENT_FID_CACHE {
    
    int cached;
    jclass xeventClass;
    jfieldID type, serial, send_event, display, window, root, subwindow, time,
             x, y, x_root, y_root, state, keycode, same_screen;

} XKEYEVENT_FID_CACHE;

typedef XKEYEVENT_FID_CACHE *PXKEYEVENT_FID_CACHE;

/* XMOTIONEVENT struct */
typedef struct XMOTIONEVENT_FID_CACHE {
    
    int cached;
    jclass xeventClass;
    jfieldID type, serial, send_event, display, window, root, subwindow, time,
             x, y, x_root, y_root, state, is_hint, same_screen, pad[10];

} XMOTIONEVENT_FID_CACHE;

typedef XMOTIONEVENT_FID_CACHE *PXMOTIONEVENT_FID_CACHE;

/* XCOLOR struct */
typedef struct XCOLOR_FID_CACHE {
    
    int cached;
    jclass xcolorClass;
    jfieldID pixel, red, green, blue, flags, pad;

} XCOLOR_FID_CACHE;

typedef XCOLOR_FID_CACHE *PXCOLOR_FID_CACHE;

/* XGCVALUES struct */
typedef struct XGCVALUES_FID_CACHE {
    
    int cached;
    jclass xgcvaluesClass;
    jfieldID function, plane_mask, foreground, background, line_width, line_style, cap_style,
             join_style, fill_style, fill_rule, arc_mode, tile, stipple, ts_x_origin, ts_y_origin,
             font, subwindow_mode, graphics_exposures, clip_x_origin, clip_y_origin, clip_mask,
             dash_offset, dashes;

} XGCVALUES_FID_CACHE;

typedef XGCVALUES_FID_CACHE *PXGCVALUES_FID_CACHE;

/* XMANYCALLBACKSTRUCT struct */
typedef struct XMANYCALLBACKSTRUCT_FID_CACHE {

    int cached;
    jclass xmanycallbackstructClass;
    jfieldID reason, event;

} XMANYCALLBACKSTRUCT_FID_CACHE;

typedef XMANYCALLBACKSTRUCT_FID_CACHE *PXMANYCALLBACKSTRUCT_FID_CACHE;

/* XMDRAGPROCCALLBACK struct */
typedef struct XMDRAGPROCCALLBACK_FID_CACHE {
    
    int cached;
    jclass xmdragproccallbackClass;
    jfieldID reason, event, timeStamp, dragContext, x, y, 
             dropSiteStatus, operation, operations, animate;

} XMDRAGPROCCALLBACK_FID_CACHE;

typedef XMDRAGPROCCALLBACK_FID_CACHE *PXMDRAGPROCCALLBACK_FID_CACHE;

/* XMDROPPROCCALLBACK struct */
typedef struct XMDROPPROCCALLBACK_FID_CACHE {
    
    int cached;
    jclass xmdropproccallbackClass;
    jfieldID reason, event, timeStamp, dragContext, x, y,
             dropSiteStatus, operation, operations, dropAction;

} XMDROPPROCCALLBACK_FID_CACHE;

typedef XMDROPPROCCALLBACK_FID_CACHE *PXMDROPPROCCALLBACK_FID_CACHE;

/* XMDROPFINISHCALLBACK struct */
typedef struct XMDROPFINISHCALLBACK_FID_CACHE {
    
    int cached;
    jclass xmdropfinishcallbackClass;
    jfieldID reason, event, timeStamp, 
             operation, operations, dropSiteStatus, dropAction, completionStatus;

} XMDROPFINISHCALLBACK_FID_CACHE;

typedef XMDROPFINISHCALLBACK_FID_CACHE *PXMDROPFINISHCALLBACK_FID_CACHE;

/* XMTEXTBLOCKREC struct */
typedef struct XMTEXTBLOCKREC_FID_CACHE {
    
    int cached;
    jclass xmtextblockrecClass;
    jfieldID ptr, length, format;

} XMTEXTBLOCKREC_FID_CACHE;

typedef XMTEXTBLOCKREC_FID_CACHE *PXMTEXTBLOCKREC_FID_CACHE;

/* XMTEXTVERIFYCALLBACKSTRUCT struct */
typedef struct XMTEXTVERIFYCALLBACKSTRUCT_FID_CACHE {
    
    int cached;
    jclass xmtextverifycallbackstructClass;
    jfieldID reason, event, doit, currInsert, newInsert, startPos, endPos, text;

} XMTEXTVERIFYCALLBACKSTRUCT_FID_CACHE;

typedef XMTEXTVERIFYCALLBACKSTRUCT_FID_CACHE *PXMTEXTVERIFYCALLBACKSTRUCT_FID_CACHE;

/* XRECTANGLE struct */
typedef struct XRECTANGLE_FID_CACHE {

    int cached;
    jclass xrectClass;
    jfieldID x, y, width, height;

} XRECTANGLE_FID_CACHE;

typedef XRECTANGLE_FID_CACHE *PXRECTANGLE_FID_CACHE;

/* XWINDOWCHANGES struct */
typedef struct XWINDOWCHANGES_FID_CACHE {
    
    int cached;
    jclass xwindowchangesClass;
    jfieldID x, y, width, height, border_width, sibling, stack_mode;

} XWINDOWCHANGES_FID_CACHE;

typedef XWINDOWCHANGES_FID_CACHE *PXWINDOWCHANGES_FID_CACHE;

/* XSETWINDOWATTRIBUTES struct */
typedef struct XSETWINDOWATTRIBUTES_FID_CACHE {
    
    int cached;
    jclass xsetwindowattributesClass;
    jfieldID background_pixmap, background_pixel, border_pixmap, border_pixel, bit_gravity, win_gravity,
             backing_store, backing_planes, backing_pixel, save_under, event_mask, do_not_propagate_mask,
             override_redirect, colormap, cursor;

} XSETWINDOWATTRIBUTES_FID_CACHE;

typedef XSETWINDOWATTRIBUTES_FID_CACHE *PXSETWINDOWATTRIBUTES_FID_CACHE;

/* XTEXTPROPERTY struct */
typedef struct XTEXTPROPERTY_FID_CACHE {
    
    int cached;
    jclass xtextpropertyClass;
    jfieldID value, encoding, format, nitems;

} XTEXTPROPERTY_FID_CACHE;

typedef XTEXTPROPERTY_FID_CACHE *PXTEXTPROPERTY_FID_CACHE;


/* XWINDOWATTRIBUTES struct */
typedef struct XWINDOWATTRIBUTES_FID_CACHE {
    
    int cached;
    jclass xwindowattributesClass;
    jfieldID x, y, width, height, border_width, depth, visual, root,
             class, bit_gravity, win_gravity, backing_store, backing_planes,
             backing_pixel, save_under, colormap, map_installed, map_state, all_event_masks,
             your_event_mask, do_not_propagate_mask, override_redirect, screen;

} XWINDOWATTRIBUTES_FID_CACHE;

typedef XWINDOWATTRIBUTES_FID_CACHE *PXWINDOWATTRIBUTES_FID_CACHE;

/* XTWIDGETGEOMETRY struct */
typedef struct XTWIDGETGEOMETRY_FID_CACHE {

    int cached;
    jclass xtwidgetgeometryClass;
    jfieldID request_mode, x, y, width, height, border_width, sibling, stack_mode;

} XTWIDGETGEOMETRY_FID_CACHE;

typedef XTWIDGETGEOMETRY_FID_CACHE *PXTWIDGETGEOMETRY_FID_CACHE;

/* ----------- cache function prototypes  ----------- */

void cacheXimageFids(JNIEnv *env, jobject lpXimage, PXIMAGE_FID_CACHE lpCache);
void cacheVisualFids(JNIEnv *env, jobject lpVisual, PVISUAL_FID_CACHE lpCache);
void cacheXanyeventFids(JNIEnv *env, jobject lpXevent, PXANYEVENT_FID_CACHE lpCache);
void cacheXbuttoneventFids(JNIEnv *env, jobject lpXevent, PXBUTTONEVENT_FID_CACHE lpCache);
void cacheXcharstructFids(JNIEnv *env, jobject lpXcharstruct, PXCHARSTRUCT_FID_CACHE lpCache);
void cacheXconfigureeventFids(JNIEnv *env, jobject lpXevent, PXCONFIGUREEVENT_FID_CACHE lpCache);
void cacheXcrossingeventFids(JNIEnv *env, jobject lpXcrossingevent, PXCROSSINGEVENT_FID_CACHE lpCache);
void cacheXexposeeventFids(JNIEnv *env, jobject lpXevent, PXEXPOSEEVENT_FID_CACHE lpCache);
void cacheXfocuschangeeventFids(JNIEnv *env, jobject lpXevent, PXFOCUSCHANGEEVENT_FID_CACHE lpCache);
void cacheXfontstructFids(JNIEnv *env, jobject lpXfontstruct, PXFONTSTRUCT_FID_CACHE lpCache);
void cacheXkeyeventFids(JNIEnv *env, jobject lpXevent, PXKEYEVENT_FID_CACHE lpCache);
void cacheXmotioneventFids(JNIEnv *env, jobject lpXevent, PXMOTIONEVENT_FID_CACHE lpCache);
void cacheXanyeventFids(JNIEnv *env, jobject lpXevent, PXANYEVENT_FID_CACHE lpCache);
void cacheXcolorFids(JNIEnv *env, jobject lpXcolor, PXCOLOR_FID_CACHE lpCache);
void cacheXgcvaluesFids(JNIEnv *env, jobject lpXgcvalues, PXGCVALUES_FID_CACHE lpCache);
void cacheXmanycallbackstructFids(JNIEnv *env, jobject lpXmanycallbackstruct, PXMANYCALLBACKSTRUCT_FID_CACHE lpCache);
void cacheXmdragproccallbackFids(JNIEnv *env, jobject lpXmdragproccallback, PXMDRAGPROCCALLBACK_FID_CACHE lpCache);
void cacheXmdropproccallbackFids(JNIEnv *env, jobject lpXmdropproccallback, PXMDROPPROCCALLBACK_FID_CACHE lpCache);
void cacheXmdropfinishcallbackFids(JNIEnv *env, jobject lpXmdropfinishcallback, PXMDROPFINISHCALLBACK_FID_CACHE lpCache);
void cacheXmtextblockrecFids(JNIEnv *env, jobject lpXmtextblockrec, PXMTEXTBLOCKREC_FID_CACHE lpCache);
void cacheXmtextverifycallbackstructFids(JNIEnv *env, jobject lpXmtextverifycallbackstruct, PXMTEXTVERIFYCALLBACKSTRUCT_FID_CACHE lpCache);
void cacheXrectangleFids(JNIEnv *env, jobject lpRect, PXRECTANGLE_FID_CACHE lpCache);
void cacheXwindowchangesFids(JNIEnv *env, jobject lpXwindowchanges, PXWINDOWCHANGES_FID_CACHE lpCache);
void cacheXsetwindowattributesFids(JNIEnv *env, jobject lpXsetwindowattributes, PXSETWINDOWATTRIBUTES_FID_CACHE lpCache);
void cacheXtextpropertyFids(JNIEnv *env, jobject lpXtextproperty, PXTEXTPROPERTY_FID_CACHE lpCache);
void cacheXwindowattributesFids(JNIEnv *env, jobject lpXwindowattributes, PXWINDOWATTRIBUTES_FID_CACHE lpCache);
void cacheXtwidgetgeometryFids(JNIEnv *env, jobject lpXtwidgetgeometry, PXTWIDGETGEOMETRY_FID_CACHE lpCache);

/* ----------- getters and setters  ----------- */
/**
 * These functions get or set object field ids assuming that the
 * fids for these objects have already been cached.
 *
 * The header file just contains function prototypes
 */

void getVisualFields(JNIEnv *env, jobject lpObject, Visual *lpVisual, VISUAL_FID_CACHE *lpVisualFc);
void setVisualFields(JNIEnv *env, jobject lpObject, Visual *lpVisual, VISUAL_FID_CACHE *lpVisualFc);
void getXanyeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XANYEVENT_FID_CACHE *lpXeventFc);
void setXanyeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XANYEVENT_FID_CACHE *lpXeventFc);
void getXbuttoneventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, PXBUTTONEVENT_FID_CACHE lpXbuttoneventFc);
void setXbuttoneventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, PXBUTTONEVENT_FID_CACHE lpXbuttoneventFc);
void getXcharstructFields(JNIEnv *env, jobject lpObject, XCharStruct *lpXcharstruct, XCHARSTRUCT_FID_CACHE *lpXcharstructFc);
void setXcharstructFields(JNIEnv *env, jobject lpObject, XCharStruct *lpXcharstruct, XCHARSTRUCT_FID_CACHE *lpXcharstructFc);
void getXconfigureeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, PXCONFIGUREEVENT_FID_CACHE lpXconfigureeventFc);
void setXconfigureeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, PXCONFIGUREEVENT_FID_CACHE lpXconfigureeventFc);
void getXcrossingeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXcrossingevent, PXCROSSINGEVENT_FID_CACHE lpXcrossingeventFc);
void setXcrossingeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXcrossingevent, PXCROSSINGEVENT_FID_CACHE lpXcrossingeventFc);
void getXexposeeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XEXPOSEEVENT_FID_CACHE *lpXexposeeventFc);
void setXexposeeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XEXPOSEEVENT_FID_CACHE *lpXexposeeventFc);
void getXfocuschangeeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XFOCUSCHANGEEVENT_FID_CACHE *lpXfocuschangeeventFc);
void setXfocuschangeeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XFOCUSCHANGEEVENT_FID_CACHE *lpXfocuschangeeventFc);
void getXfontstructFields(JNIEnv *env, jobject lpObject, XFontStruct *lpXfontstruct, XFONTSTRUCT_FID_CACHE *lpXfontstructFc);
void setXfontstructFields(JNIEnv *env, jobject lpObject, XFontStruct *lpXfontstruct, XFONTSTRUCT_FID_CACHE *lpXfontstructFc);
void getXimageFields(JNIEnv *env, jobject lpObject, XImage *lpXimage, XIMAGE_FID_CACHE *lpXimageFc);
void setXimageFields(JNIEnv *env, jobject lpObject, XImage *lpXimage, XIMAGE_FID_CACHE *lpXimageFc);
void getXkeyeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XKEYEVENT_FID_CACHE *lpXkeyeventFc);
void setXkeyeventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XKEYEVENT_FID_CACHE *lpXkeyeventFc);
void getXmotioneventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XMOTIONEVENT_FID_CACHE *lpXmotioneventFc);
void setXmotioneventFields(JNIEnv *env, jobject lpObject, XEvent *lpXevent, XMOTIONEVENT_FID_CACHE *lpXmotioneventFc);
void getXcolorFields(JNIEnv *env, jobject lpObject, XColor *lpXcolor, XCOLOR_FID_CACHE *lpXcolorFc);
void setXcolorFields(JNIEnv *env, jobject lpObject, XColor *lpXcolor, XCOLOR_FID_CACHE *lpXcolorFc);
void getXgcvaluesFields(JNIEnv *env, jobject lpObject, XGCValues *lpXgcvalues, PXGCVALUES_FID_CACHE lpXgcvaluesFc);
void setXgcvaluesFields(JNIEnv *env, jobject lpObject, XGCValues *lpXgcvalues, PXGCVALUES_FID_CACHE lpXgcvaluesFc);
void getXmanycallbackstructFields(JNIEnv *env, jobject lpObject, XmAnyCallbackStruct *lpXmanycallbackstruct, PXMANYCALLBACKSTRUCT_FID_CACHE lpXmanycallbackstructFc);
void setXmanycallbackstructFields(JNIEnv *env, jobject lpObject, XmAnyCallbackStruct *lpXmanycallbackstruct, PXMANYCALLBACKSTRUCT_FID_CACHE lpXmanycallbackstructFc);
void getXmdragproccallbackFields(JNIEnv *env, jobject lpObject, XmDragProcCallbackStruct *lpXmdragproccallback, PXMDRAGPROCCALLBACK_FID_CACHE lpXmdragproccallbackFc);
void setXmdragproccallbackFields(JNIEnv *env, jobject lpObject, XmDragProcCallbackStruct *lpXmdragproccallback, PXMDRAGPROCCALLBACK_FID_CACHE lpXmdragproccallbackFc);
void getXmdropproccallbackFields(JNIEnv *env, jobject lpObject, XmDropProcCallbackStruct *lpXmdropproccallback, PXMDROPPROCCALLBACK_FID_CACHE lpXmdropproccallbackFc);
void setXmdropproccallbackFields(JNIEnv *env, jobject lpObject, XmDropProcCallbackStruct *lpXmdropproccallback, PXMDROPPROCCALLBACK_FID_CACHE lpXmdropproccallbackFc);
void getXmdropfinishcallbackFields(JNIEnv *env, jobject lpObject, XmDropFinishCallbackStruct *lpXmdropfinishcallback, PXMDROPFINISHCALLBACK_FID_CACHE lpXmdropfinishcallbackFc);
void setXmdropfinishcallbackFields(JNIEnv *env, jobject lpObject, XmDropFinishCallbackStruct *lpXmdropfinishcallback, PXMDROPFINISHCALLBACK_FID_CACHE lpXmdropfinishcallbackFc);
void getXmtextblockrecFields(JNIEnv *env, jobject lpObject, XmTextBlockRec *lpXmtextblockrec, PXMTEXTBLOCKREC_FID_CACHE lpXmtextblockrecFc);
void setXmtextblockrecFields(JNIEnv *env, jobject lpObject, XmTextBlockRec *lpXmtextblockrec, PXMTEXTBLOCKREC_FID_CACHE lpXmtextblockrecFc);
void getXmtextverifycallbackstructFields(JNIEnv *env, jobject lpObject, XmTextVerifyCallbackStruct *lpXmtextverifycallbackstruct, PXMTEXTVERIFYCALLBACKSTRUCT_FID_CACHE lpXmtextverifycallbackstructFc);
void setXmtextverifycallbackstructFields(JNIEnv *env, jobject lpObject, XmTextVerifyCallbackStruct *lpXmtextverifycallbackstruct, PXMTEXTVERIFYCALLBACKSTRUCT_FID_CACHE lpXmtextverifycallbackstructFc);
void getXrectangleFields(JNIEnv *env, jobject lpObject, XRectangle *lpXrect, PXRECTANGLE_FID_CACHE lpXrectFc);
void setXrectangleFields(JNIEnv *env, jobject lpObject, XRectangle *lpXrect, PXRECTANGLE_FID_CACHE lpXrectFc);
void getXsetwindowattributesFields(JNIEnv *env, jobject lpObject, XSetWindowAttributes *lpXsetwindowattributes, PXSETWINDOWATTRIBUTES_FID_CACHE lpXsetwindowattributesFc);
void setXsetwindowattributesFields(JNIEnv *env, jobject lpObject, XSetWindowAttributes *lpXsetwindowattributes, PXSETWINDOWATTRIBUTES_FID_CACHE lpXsetwindowattributesFc);
void getXtextpropertyFields(JNIEnv *env, jobject lpObject, XTextProperty *lpXtextproperty, PXTEXTPROPERTY_FID_CACHE lpXtextpropertyFc);
void setXtextpropertyFields(JNIEnv *env, jobject lpObject, XTextProperty *lpXtextproperty, PXTEXTPROPERTY_FID_CACHE lpXtextpropertyFc);
void getXwindowattributesFields(JNIEnv *env, jobject lpObject, XWindowAttributes *lpXwindowattributes, PXWINDOWATTRIBUTES_FID_CACHE lpXwindowattributesFc);
void setXwindowattributesFields(JNIEnv *env, jobject lpObject, XWindowAttributes *lpXwindowattributes, PXWINDOWATTRIBUTES_FID_CACHE lpXwindowattributesFc);
void getXwindowchangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpXwindowchanges, PXWINDOWCHANGES_FID_CACHE lpXwindowchangesFc);
void setXwindowchangesFields(JNIEnv *env, jobject lpObject, XWindowChanges *lpXwindowchanges, PXWINDOWCHANGES_FID_CACHE lpXwindowchangesFc);
void getXtwidgetgeometryFields(JNIEnv *env, jobject lpObject, XtWidgetGeometry *lpXtwidgetgeometry, PXTWIDGETGEOMETRY_FID_CACHE lpXtwidgetgeometryFc);
void setXtwidgetgeometryFields(JNIEnv *env, jobject lpObject, XtWidgetGeometry *lpXtwidgetgeometry, PXTWIDGETGEOMETRY_FID_CACHE lpXtwidgetgeometryFc);

extern VISUAL_FID_CACHE VisualFc;
extern XANYEVENT_FID_CACHE XanyeventFc;
extern XBUTTONEVENT_FID_CACHE XbuttoneventFc;
extern XCHARSTRUCT_FID_CACHE XcharstructFc;
extern XCOLOR_FID_CACHE XcolorFc;
extern XCONFIGUREEVENT_FID_CACHE XconfigureeventFc;
extern XCROSSINGEVENT_FID_CACHE XcrossingeventFc;
extern XEXPOSEEVENT_FID_CACHE XexposeeventFc;
extern XFOCUSCHANGEEVENT_FID_CACHE XfocuschangeeventFc;
extern XFONTSTRUCT_FID_CACHE XfontstructFc;
extern XGCVALUES_FID_CACHE XgcvaluesFc;
extern XIMAGE_FID_CACHE XimageFc;
extern XKEYEVENT_FID_CACHE XkeyeventFc;
extern XMANYCALLBACKSTRUCT_FID_CACHE XmanycallbackstructFc;
extern XMDRAGPROCCALLBACK_FID_CACHE XmdragproccallbackFc;
extern XMDROPFINISHCALLBACK_FID_CACHE XmdropfinishcallbackFc;
extern XMDROPPROCCALLBACK_FID_CACHE XmdropproccallbackFc;
extern XMOTIONEVENT_FID_CACHE XmotioneventFc;
extern XMTEXTBLOCKREC_FID_CACHE XmtextblockrecFc;
extern XMTEXTVERIFYCALLBACKSTRUCT_FID_CACHE XmtextverifycallbackstructFc;
extern XRECTANGLE_FID_CACHE XrectangleFc;
extern XSETWINDOWATTRIBUTES_FID_CACHE XsetwindowattributesFc;
extern XTEXTPROPERTY_FID_CACHE XtextpropertyFc;
extern XTWIDGETGEOMETRY_FID_CACHE XtwidgetgeometryFc;
extern XWINDOWATTRIBUTES_FID_CACHE XwindowattributesFc;
extern XWINDOWCHANGES_FID_CACHE XwindowchangesFc;

#endif /* INC_structs_H */
