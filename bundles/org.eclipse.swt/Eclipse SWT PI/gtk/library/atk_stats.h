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
#define GTK_1ACCESSIBLE_FUNC 7
#define atk_1focus_1tracker_1notify_FUNC 8
#define atk_1get_1default_1registry_FUNC 9
#define atk_1object_1factory_1get_1accessible_1type_FUNC 10
#define atk_1object_1initialize_FUNC 11
#define atk_1registry_1get_1factory_FUNC 12
#define atk_1registry_1set_1factory_1type_FUNC 13
#define atk_1state_1set_1add_1state_FUNC 14
#define atk_1state_1set_1new_FUNC 15
#define call__II_FUNC 16
#define call__III_FUNC 17
#define call__IIII_FUNC 18
#define call__IIIII_FUNC 19
#define call__IIIIII_FUNC 20
#define call__IIIIIII_FUNC 21
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2_FUNC 22
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2_FUNC 23
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2_FUNC 24
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2_FUNC 25
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2_FUNC 26
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2_FUNC 27
#define memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2_FUNC 28
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I_FUNC 29
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I_FUNC 30
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2I_FUNC 31
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I_FUNC 32
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I_FUNC 33
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I_FUNC 34
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I_FUNC 35
#define memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I_FUNC 36
