/*******************************************************************************
* Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
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

#include "swt.h"
#include "gtk_structs.h"

#define GTK_NATIVE(func) Java_org_eclipse_swt_internal_gtk_GTK_##func

#ifndef NO_GTK_1WIDGET_1HEIGHT
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1HEIGHT)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1WIDGET_1HEIGHT\n")
	rc = (jint)((GtkWidget *)arg0)->allocation.height;
	NATIVE_EXIT(env, that, "GTK_1WIDGET_1HEIGHT\n")
	return rc;
}
#endif

#ifndef NO_GTK_1WIDGET_1WIDTH
JNIEXPORT jint JNICALL OS_NATIVE(GTK_1WIDGET_1WIDTH)
	(JNIEnv *env, jclass that, jint arg0)
{
	jint rc;
	NATIVE_ENTER(env, that, "GTK_1WIDGET_1WIDTH\n")
	rc = (jint)((GtkWidget *)arg0)->allocation.width;
	NATIVE_EXIT(env, that, "GTK_1WIDGET_1WIDTH\n")
	return rc;
}
#endif