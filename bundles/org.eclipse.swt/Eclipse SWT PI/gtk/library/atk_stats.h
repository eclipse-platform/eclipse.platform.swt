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
int ATK_nativeFunctionCallCount[];
char* ATK_nativeFunctionNames[];
#define ATK_NATIVE_ENTER(env, that, func) ATK_nativeFunctionCallCount[func]++;
#define ATK_NATIVE_EXIT(env, that, func) 
#else
#define ATK_NATIVE_ENTER(env, that, func) 
#define ATK_NATIVE_EXIT(env, that, func) 
#endif

#define ATK_1ACTION_1GET_1IFACE_FUNC 0
#define ATK_1COMPONENT_1GET_1IFACE_FUNC 1
#define ATK_1OBJECT_1FACTORY_1CLASS_FUNC 2
#define ATK_1SELECTION_1GET_1IFACE_FUNC 3
#define ATK_1TEXT_1GET_1IFACE_FUNC 4
#define AtkObjectFactoryClass_1sizeof_FUNC 5
#define AtkObjectFactory_1sizeof_FUNC 6
#define GInterfaceInfo_1sizeof_FUNC 7
#define GTK_1ACCESSIBLE_FUNC 8
#define GTypeInfo_1sizeof_FUNC 9
#define GTypeQuery_1sizeof_FUNC 10
#define G_1OBJECT_1CLASS_FUNC 11
#define G_1OBJECT_1GET_1CLASS_FUNC 12
#define G_1TYPE_1FROM_1INSTANCE_FUNC 13
#define atk_1focus_1tracker_1notify_FUNC 14
#define atk_1get_1default_1registry_FUNC 15
#define atk_1object_1factory_1get_1accessible_1type_FUNC 16
#define atk_1object_1initialize_FUNC 17
#define atk_1registry_1get_1factory_FUNC 18
#define atk_1registry_1set_1factory_1type_FUNC 19
#define atk_1state_1set_1add_1state_FUNC 20
#define atk_1state_1set_1new_FUNC 21
#define call__II_FUNC 22
#define call__III_FUNC 23
#define call__IIII_FUNC 24
#define call__IIIII_FUNC 25
#define call__IIIIII_FUNC 26
#define call__IIIIIII_FUNC 27
#define g_1object_1new_FUNC 28
#define g_1type_1add_1interface_1static_FUNC 29
#define g_1type_1class_1peek_FUNC 30
#define g_1type_1class_1peek_1parent_FUNC 31
#define g_1type_1from_1name_FUNC 32
#define g_1type_1interface_1peek_1parent_FUNC 33
#define g_1type_1is_1a_FUNC 34
#define g_1type_1name_FUNC 35
#define g_1type_1parent_FUNC 36
#define g_1type_1query_FUNC 37
#define g_1type_1register_1static_FUNC 38
#define gtk_1widget_1get_1toplevel_FUNC 39
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2_FUNC 40
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2_FUNC 41
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2_FUNC 42
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2_FUNC 43
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2_FUNC 44
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2_FUNC 45
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2_FUNC 46
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GInterfaceInfo_2I_FUNC 47
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2_FUNC 48
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_GTypeInfo_2I_FUNC 49
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I_FUNC 50
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I_FUNC 51
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2I_FUNC 52
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I_FUNC 53
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I_FUNC 54
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I_FUNC 55
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I_FUNC 56
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GObjectClass_2I_FUNC 57
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GTypeQuery_2II_FUNC 58
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I_FUNC 59
