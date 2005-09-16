/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.opengl.gtk;

import org.eclipse.swt.opengl.Library;

public class GLX {
	
static {
	Library.loadLibrary("gl");
}

/*
** Visual Config Attributes (glXGetConfig, glXGetFBConfigAttrib)
*/
public static final int GLX_USE_GL				= 1;	/* support GLX rendering */
public static final int GLX_BUFFER_SIZE			= 2;	/* depth of the color buffer */
public static final int GLX_LEVEL				= 3;	/* level in plane stacking */
public static final int GLX_RGBA				= 4;	/* true if RGBA mode */
public static final int GLX_DOUBLEBUFFER		= 5;	/* double buffering supported */
public static final int GLX_STEREO				= 6;	/* stereo buffering supported */
public static final int GLX_AUX_BUFFERS			= 7;	/* number of aux buffers */
public static final int GLX_RED_SIZE			= 8;	/* number of red component bits */
public static final int GLX_GREEN_SIZE			= 9;	/* number of green component bits */
public static final int GLX_BLUE_SIZE			= 10;	/* number of blue component bits */
public static final int GLX_ALPHA_SIZE			= 11;	/* number of alpha component bits */
public static final int GLX_DEPTH_SIZE			= 12;	/* number of depth bits */
public static final int GLX_STENCIL_SIZE		= 13;	/* number of stencil bits */
public static final int GLX_ACCUM_RED_SIZE		= 14;	/* number of red accum bits */
public static final int GLX_ACCUM_GREEN_SIZE	= 15;	/* number of green accum bits */
public static final int GLX_ACCUM_BLUE_SIZE		= 16;	/* number of blue accum bits */
public static final int GLX_ACCUM_ALPHA_SIZE	= 17;	/* number of alpha accum bits */
/*
** FBConfig-specific attributes
*/
public static final int GLX_X_VISUAL_TYPE		= 0x22;
public static final int GLX_CONFIG_CAVEAT		= 0x20;	/* Like visual_info VISUAL_CAVEAT_EXT */
public static final int GLX_TRANSPARENT_TYPE	= 0x23;
public static final int GLX_TRANSPARENT_INDEX_VALUE	= 0x24;
public static final int GLX_TRANSPARENT_RED_VALUE	= 0x25;
public static final int GLX_TRANSPARENT_GREEN_VALUE	= 0x26;
public static final int GLX_TRANSPARENT_BLUE_VALUE	= 0x27;
public static final int GLX_TRANSPARENT_ALPHA_VALUE	= 0x28;
public static final int GLX_DRAWABLE_TYPE		= 0x8010;
public static final int GLX_RENDER_TYPE			= 0x8011;
public static final int GLX_X_RENDERABLE		= 0x8012;
public static final int GLX_FBCONFIG_ID			= 0x8013;
public static final int GLX_MAX_PBUFFER_WIDTH	= 0x8016;
public static final int GLX_MAX_PBUFFER_HEIGHT	= 0x8017;
public static final int GLX_MAX_PBUFFER_PIXELS	= 0x8018;
public static final int GLX_VISUAL_ID			= 0x800B;

/*
** Error return values from glXGetConfig.  Success is indicated by
** a value of 0.
*/
public static final int GLX_BAD_SCREEN		= 1;	/* screen # is bad */
public static final int GLX_BAD_ATTRIBUTE	= 2;	/* attribute to get is bad */
public static final int GLX_NO_EXTENSION	= 3;	/* no glx extension on server */
public static final int GLX_BAD_VISUAL		= 4;	/* visual # not known by GLX */
public static final int GLX_BAD_CONTEXT		= 5;	/* returned only by import_context EXT? */
public static final int GLX_BAD_VALUE		= 6;	/* returned only by glXSwapIntervalSGI? */
public static final int GLX_BAD_ENUM		= 7;	/* unused? */

/* FBConfig attribute values */

/*
** Generic "don't care" value for glX ChooseFBConfig attributes (except
** GLX_LEVEL)
*/
public static final int GLX_DONT_CARE			= 0xFFFFFFFF;

/* GLX_RENDER_TYPE bits */
public static final int GLX_RGBA_BIT			= 0x00000001;
public static final int GLX_COLOR_INDEX_BIT		= 0x00000002;

/* GLX_DRAWABLE_TYPE bits */
public static final int GLX_WINDOW_BIT			= 0x00000001;
public static final int GLX_PIXMAP_BIT			= 0x00000002;
public static final int GLX_PBUFFER_BIT			= 0x00000004;

/* GLX_CONFIG_CAVEAT attribute values */
public static final int GLX_NONE				= 0x8000;
public static final int GLX_SLOW_CONFIG			= 0x8001;
public static final int GLX_NON_CONFORMANT_CONFIG	= 0x800D;

/* GLX_X_VISUAL_TYPE attribute values */
public static final int GLX_TRUE_COLOR			= 0x8002;
public static final int GLX_DIRECT_COLOR		= 0x8003;
public static final int GLX_PSEUDO_COLOR		= 0x8004;
public static final int GLX_STATIC_COLOR		= 0x8005;
public static final int GLX_GRAY_SCALE			= 0x8006;
public static final int GLX_STATIC_GRAY			= 0x8007;

/* GLX_TRANSPARENT_TYPE attribute values */
/* public static final int GLX_NONE			   0x8000 */
public static final int GLX_TRANSPARENT_RGB		= 0x8008;
public static final int GLX_TRANSPARENT_INDEX	= 0x8009;

/* glXCreateGLXPbuffer attributes */
public static final int GLX_PRESERVED_CONTENTS	= 0x801B;
public static final int GLX_LARGEST_PBUFFER		= 0x801C;
public static final int GLX_PBUFFER_HEIGHT		= 0x8040;	/* New for GLX 1.3 */
public static final int GLX_PBUFFER_WIDTH		= 0x8041;	/* New for GLX 1.3 */

/* glXQueryGLXPBuffer attributes */
public static final int GLX_WIDTH		= 0x801D;
public static final int GLX_HEIGHT		= 0x801E;
public static final int GLX_EVENT_MASK	= 0x801F;

/* glXCreateNewContext render_type attribute values */
public static final int GLX_RGBA_TYPE			= 0x8014;
public static final int GLX_COLOR_INDEX_TYPE	= 0x8015;

/* glXQueryContext attributes */
/* public static final int GLX_FBCONFIG_ID		  0x8013 */
/* public static final int GLX_RENDER_TYPE		  0x8011 */
public static final int GLX_SCREEN			= 0x800C;

/* glXSelectEvent event mask bits */
public static final int GLX_PBUFFER_CLOBBER_MASK	= 0x08000000;

/* GLXPbufferClobberEvent event_type values */
public static final int GLX_DAMAGED			= 0x8020;
public static final int GLX_SAVED			= 0x8021;

/* GLXPbufferClobberEvent draw_type values */
public static final int GLX_WINDOW			= 0x8022;
public static final int GLX_PBUFFER			= 0x8023;

/* GLXPbufferClobberEvent buffer_mask bits */
public static final int GLX_FRONT_LEFT_BUFFER_BIT	= 0x00000001;
public static final int GLX_FRONT_RIGHT_BUFFER_BIT	= 0x00000002;
public static final int GLX_BACK_LEFT_BUFFER_BIT	= 0x00000004;
public static final int GLX_BACK_RIGHT_BUFFER_BIT	= 0x00000008;
public static final int GLX_AUX_BUFFERS_BIT		= 0x00000010;
public static final int GLX_DEPTH_BUFFER_BIT		= 0x00000020;
public static final int GLX_STENCIL_BUFFER_BIT		= 0x00000040;
public static final int GLX_ACCUM_BUFFER_BIT		= 0x00000080;

/*
** Extension return values from glXGetConfig.  These are also
** accepted as parameter values for glXChooseVisual.
*/

public static final int GLX_X_VISUAL_TYPE_EXT = 0x22;	/* visual_info extension type */
public static final int GLX_TRANSPARENT_TYPE_EXT = 0x23;	/* visual_info extension */
public static final int GLX_TRANSPARENT_INDEX_VALUE_EXT = 0x24;	/* visual_info extension */
public static final int GLX_TRANSPARENT_RED_VALUE_EXT	= 0x25;	/* visual_info extension */
public static final int GLX_TRANSPARENT_GREEN_VALUE_EXT = 0x26;	/* visual_info extension */
public static final int GLX_TRANSPARENT_BLUE_VALUE_EXT	= 0x27;	/* visual_info extension */
public static final int GLX_TRANSPARENT_ALPHA_VALUE_EXT = 0x28;	/* visual_info extension */

/* Property values for visual_type */
public static final int GLX_TRUE_COLOR_EXT	= 0x8002;
public static final int GLX_DIRECT_COLOR_EXT	= 0x8003;
public static final int GLX_PSEUDO_COLOR_EXT	= 0x8004;
public static final int GLX_STATIC_COLOR_EXT	= 0x8005;
public static final int GLX_GRAY_SCALE_EXT	= 0x8006;
public static final int GLX_STATIC_GRAY_EXT	= 0x8007;

/* Property values for transparent pixel */
public static final int GLX_NONE_EXT		= 0x8000;
public static final int GLX_TRANSPARENT_RGB_EXT		= 0x8008;
public static final int GLX_TRANSPARENT_INDEX_EXT	= 0x8009;

/* Property values for visual_rating */
public static final int GLX_VISUAL_CAVEAT_EXT		= 0x20;  /* visual_rating extension type */
public static final int GLX_SLOW_VISUAL_EXT		= 0x8001;
public static final int GLX_NON_CONFORMANT_VISUAL_EXT	= 0x800D;

/*
** Names for attributes to glXGetClientString.
*/
public static final int GLX_VENDOR		= 0x1;
public static final int GLX_VERSION		= 0x2;
public static final int GLX_EXTENSIONS	= 0x3;

/*
** Names for attributes to glXQueryContextInfoEXT.
*/
public static final int GLX_SHARE_CONTEXT_EXT = 0x800A;	/* id of share context */
public static final int GLX_VISUAL_ID_EXT = 0x800B;	/* id of context's visual */
public static final int GLX_SCREEN_EXT = 0x800C;	/* screen number */

/*
* GLX 1.4 
*/
public static final int GLX_SAMPLE_BUFFERS = 100000;
public static final int GLX_SAMPLES = 100001;

public static final native int glXChooseVisual(int dpy, int screen, int[] attribList);
public static final native void glXCopyContext(int dpy, int src, int dst, int mask);
public static final native int glXCreateContext(int dpy, XVisualInfo vis, int shareList, boolean direct);
public static final native int glXCreateGLXPixmap(int dpy, XVisualInfo vis, int pixmap);
public static final native void glXDestroyContext(int dpy, int ctx);
public static final native void glXDestroyGLXPixmap(int dpy, int pix);
public static final native int glXGetConfig(int dpy, XVisualInfo vis, int attrib, int[] value);
public static final native int glXGetCurrentContext();
public static final native int glXGetCurrentDrawable();
public static final native boolean glXIsDirect(int dpy, int ctx);
public static final native boolean glXMakeCurrent(int dpy, int drawable, int ctx);
public static final native boolean glXQueryExtension(int dpy, int[] errorBase, int[] eventBase);
public static final native boolean glXQueryVersion(int dpy, int[] major, int[] minor);
public static final native void glXSwapBuffers(int dpy, int drawable);
public static final native void glXUseXFont(int font, int first, int count, int listBase);
public static final native void glXWaitGL();
public static final native void glXWaitX();
public static final native int glXGetClientString(int dpy, int name);
public static final native int glXQueryServerString(int dpy, int screen, int name);
public static final native int glXQueryExtensionsString(int dpy, int screen);
public static final native void memmove(XVisualInfo dest, int src, int size);
}
