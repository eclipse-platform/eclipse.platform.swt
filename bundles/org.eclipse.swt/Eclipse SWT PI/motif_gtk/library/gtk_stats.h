/*******************************************************************************
* Copyright (c) 2000, 2004 IBM Corporation and others. All rights reserved.
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
int GTK_nativeFunctionCallCount[];
char* GTK_nativeFunctionNames[];
#define GTK_NATIVE_ENTER(env, that, func) GTK_nativeFunctionCallCount[func]++;
#define GTK_NATIVE_EXIT(env, that, func) 
#else
#define GTK_NATIVE_ENTER(env, that, func) 
#define GTK_NATIVE_EXIT(env, that, func) 
#endif

#define GTK_1WIDGET_1HEIGHT_FUNC 0
#define GTK_1WIDGET_1WIDTH_FUNC 1
#define g_1signal_1connect_FUNC 2
#define gtk_1events_1pending_FUNC 3
#define gtk_1init_1check_FUNC 4
#define gtk_1main_FUNC 5
#define gtk_1main_1iteration_FUNC 6
#define gtk_1plug_1new_FUNC 7
#define gtk_1widget_1destroy_FUNC 8
#define gtk_1widget_1show_FUNC 9
#define gtk_1widget_1show_1now_FUNC 10
#define gtk_1window_1new_FUNC 11
