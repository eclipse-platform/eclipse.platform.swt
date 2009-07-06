/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifndef INC_os_H
#define INC_os_H

#define NDEBUG

#include <X11/X.h>
#include <X11/Xlib.h>
#include <X11/IntrinsicP.h>
#include <X11/Intrinsic.h>
#include <X11/Shell.h>
#include <X11/keysym.h>
#include <X11/extensions/shape.h>
#include <X11/extensions/Print.h>
#if !(defined _HPUX || defined AIX)
#include <X11/extensions/Xrender.h>
#else
#define NO_XRenderPictureAttributes
#define NO__1XRenderCreatePicture
#endif
#if !(defined _HPUX || defined SOLARIS)
#include <X11/extensions/XTest.h>
#endif

#ifdef NO_XINERAMA_EXTENSIONS
#define NO_XineramaScreenInfo
#define NO__1XineramaIsActive
#define NO__1XineramaQueryScreens
#define NO_memmove__Lorg_eclipse_swt_internal_motif_XineramaScreenInfo_2II
#else
#include <X11/extensions/Xinerama.h>
#endif /* NO_XINERAMA_EXTENSIONS */

this is bad
#include <Xm/XmAll.h>
#include <Mrm/MrmPublic.h>

#ifndef _XmSetMenuTraversal
void _XmSetMenuTraversal(Widget wid, int traversalOn);
#endif

#ifndef _XtDefaultAppContext
XtAppContext _XtDefaultAppContext();
#endif

#include <dlfcn.h>
#include <stdio.h>
#include <assert.h>
#include <langinfo.h>
#include <locale.h>
#include <iconv.h>
#include <stdlib.h>
#include <unistd.h>
#ifdef	_HPUX
#include <sys/time.h>
#else
#include <sys/select.h>
#endif

#ifdef NO_XPRINTING_EXTENSIONS
#define NO__1XpCancelJob
#define NO__1XpCreateContext
#define NO__1XpDestroyContext
#define NO__1XpEndJob
#define NO__1XpEndPage
#define NO__1XpFreePrinterList
#define NO__1XpGetOneAttribute
#define NO__1XpGetPageDimensions
#define NO__1XpGetPrinterList
#define NO__1XpGetScreenOfContext
#define NO__1XpSetAttributes
#define NO__1XpSetContext
#define NO__1XpStartJob
#define NO__1XpStartPage
#endif

#include "os_custom.h"

#endif /* INC_os_H */
