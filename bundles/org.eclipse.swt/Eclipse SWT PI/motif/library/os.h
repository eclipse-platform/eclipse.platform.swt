/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
#include <X11/extensions/Print.h>

#ifdef NO_XINERAMA_EXTENSIONS
#define NO_XineramaScreenInfo
#define NO_XineramaIsActive
#define NO_XineramaQueryScreens
#define NO_memmove__Lorg_eclipse_swt_internal_motif_XineramaScreenInfo_2II
#else
#include <X11/extensions/Xinerama.h>
#endif /* NO_XINERAMA_EXTENSIONS */

#include <Xm/XmAll.h>
#include <Mrm/MrmPublic.h>

#include <stdio.h>
#include <assert.h>
#include <langinfo.h>
#include <locale.h>
#include <iconv.h>
#include <stdlib.h>
#ifdef	_HPUX
#include <sys/time.h>
#else
#include <sys/select.h>
#endif

#ifdef NO_XPRINTING_EXTENSIONS
#define NO_XpCancelJob
#define NO_XpCreateContext
#define NO_XpDestroyContext
#define NO_XpEndJob
#define NO_XpEndPage
#define NO_XpFreePrinterList
#define NO_XpGetOneAttribute
#define NO_XpGetPageDimensions
#define NO_XpGetPrinterList
#define NO_XpGetScreenOfContext
#define NO_XpSetAttributes
#define NO_XpSetContext
#define NO_XpStartJob
#define NO_XpStartPage
#endif

#include "os_stats.h"
#include "os_custom.h"

#endif /* INC_os_H */
