/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

#ifdef NATIVE_STATS
extern int GTK_nativeFunctionCount;
extern int GTK_nativeFunctionCallCount[];
extern char* GTK_nativeFunctionNames[];
#define GTK_NATIVE_ENTER(env, that, func) GTK_nativeFunctionCallCount[func]++;
#define GTK_NATIVE_EXIT(env, that, func) 
#else
#ifndef GTK_NATIVE_ENTER
#define GTK_NATIVE_ENTER(env, that, func) 
#endif
#ifndef GTK_NATIVE_EXIT
#define GTK_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	_1GTK_1WIDGET_1HEIGHT_FUNC,
	_1GTK_1WIDGET_1WIDTH_FUNC,
	_1g_1signal_1connect_FUNC,
	_1gtk_1events_1pending_FUNC,
	_1gtk_1init_1check_FUNC,
	_1gtk_1main_FUNC,
	_1gtk_1main_1iteration_FUNC,
	_1gtk_1plug_1new_FUNC,
	_1gtk_1widget_1destroy_FUNC,
	_1gtk_1widget_1show_FUNC,
	_1gtk_1widget_1show_1now_FUNC,
	_1gtk_1window_1new_FUNC,
} GTK_FUNCS;
