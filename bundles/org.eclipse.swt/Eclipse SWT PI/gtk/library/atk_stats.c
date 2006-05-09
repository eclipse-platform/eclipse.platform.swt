/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others. All rights reserved.
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
#include "atk_stats.h"

#ifdef NATIVE_STATS

int ATK_nativeFunctionCount = 42;
int ATK_nativeFunctionCallCount[42];
char * ATK_nativeFunctionNames[] = {
	"AtkObjectFactoryClass_1sizeof",
	"AtkObjectFactory_1sizeof",
	"_1ATK_1ACTION_1GET_1IFACE",
	"_1ATK_1COMPONENT_1GET_1IFACE",
	"_1ATK_1OBJECT_1FACTORY_1CLASS",
	"_1ATK_1SELECTION_1GET_1IFACE",
	"_1ATK_1TEXT_1GET_1IFACE",
	"_1GTK_1ACCESSIBLE",
	"_1atk_1focus_1tracker_1notify",
	"_1atk_1get_1default_1registry",
	"_1atk_1object_1factory_1create_1accessible",
	"_1atk_1object_1factory_1get_1accessible_1type",
	"_1atk_1object_1initialize",
	"_1atk_1object_1ref_1relation_1set",
	"_1atk_1registry_1get_1factory",
	"_1atk_1registry_1set_1factory_1type",
	"_1atk_1relation_1set_1get_1n_1relations",
	"_1atk_1relation_1set_1get_1relation",
	"_1atk_1relation_1set_1remove",
	"_1atk_1state_1set_1add_1state",
	"_1atk_1state_1set_1new",
	"_1call__II",
	"_1call__III",
	"_1call__IIII",
	"_1call__IIIII",
	"_1call__IIIIII",
	"_1call__IIIIIII",
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2",
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2",
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2",
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2",
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2",
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2",
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2",
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I",
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I",
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2I",
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I",
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I",
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I",
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I",
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(ATK_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return ATK_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(ATK_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, ATK_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(ATK_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return ATK_nativeFunctionCallCount[index];
}

#endif
