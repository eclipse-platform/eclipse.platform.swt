/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others. All rights reserved.
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

int ATK_nativeFunctionCount = 73;
int ATK_nativeFunctionCallCount[73];
char * ATK_nativeFunctionNames[] = {
	"ATK_1IS_1NO_1OP_1OBJECT_1FACTORY",
	"ATK_1TYPE_1ACTION",
	"ATK_1TYPE_1COMPONENT",
	"ATK_1TYPE_1HYPERTEXT",
	"ATK_1TYPE_1OBJECT_1FACTORY",
	"ATK_1TYPE_1SELECTION",
	"ATK_1TYPE_1TABLE",
	"ATK_1TYPE_1TEXT",
	"ATK_1TYPE_1VALUE",
	"AtkAttribute_1sizeof",
	"AtkObjectFactoryClass_1sizeof",
	"AtkObjectFactory_1sizeof",
	"AtkTextRange_1sizeof",
	"AtkTextRectangle_1sizeof",
	"GTK_1TYPE_1ACCESSIBLE",
	"_1ATK_1ACTION_1GET_1IFACE",
	"_1ATK_1COMPONENT_1GET_1IFACE",
	"_1ATK_1HYPERTEXT_1GET_1IFACE",
	"_1ATK_1OBJECT_1FACTORY_1CLASS",
	"_1ATK_1SELECTION_1GET_1IFACE",
	"_1ATK_1TABLE_1GET_1IFACE",
	"_1ATK_1TEXT_1GET_1IFACE",
	"_1ATK_1VALUE_1GET_1IFACE",
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
	"_1atk_1role_1register",
	"_1atk_1state_1set_1add_1state",
	"_1atk_1state_1set_1new",
	"_1atk_1text_1attribute_1get_1name",
	"_1atk_1text_1attribute_1get_1value",
	"_1atk_1text_1attribute_1register",
#ifndef JNI64
	"_1call__II",
#else
	"_1call__JJ",
#endif
#ifndef JNI64
	"_1call__III",
#else
	"_1call__JJJ",
#endif
#ifndef JNI64
	"_1call__IIII",
#else
	"_1call__JJJJ",
#endif
#ifndef JNI64
	"_1call__IIIII",
#else
	"_1call__JJJJJ",
#endif
#ifndef JNI64
	"_1call__IIIIII",
#else
	"_1call__JJJJJJ",
#endif
#ifndef JNI64
	"_1call__IIIIIII",
#else
	"_1call__JJJJJJJ",
#endif
	"g_1strdup",
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2",
#else
	"memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2I",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2",
#else
	"memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2",
#else
	"memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2",
#else
	"memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2",
#else
	"memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2",
#else
	"memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2",
#else
	"memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2",
#else
	"memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2I",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2I",
#else
	"memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2I",
#endif
#ifndef JNI64
	"memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2",
#else
	"memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2J",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2JI",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2J",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2I",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2J",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2J",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2J",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2J",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2I",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2J",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2J",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2JI",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2II",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2JI",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2I",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2J",
#endif
#ifndef JNI64
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I",
#else
	"memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2J",
#endif
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
