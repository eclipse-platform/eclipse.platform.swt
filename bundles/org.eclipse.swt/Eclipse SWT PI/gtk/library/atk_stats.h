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

#ifdef NATIVE_STATS
extern int ATK_nativeFunctionCount;
extern int ATK_nativeFunctionCallCount[];
extern char* ATK_nativeFunctionNames[];
#define ATK_NATIVE_ENTER(env, that, func) ATK_nativeFunctionCallCount[func]++;
#define ATK_NATIVE_EXIT(env, that, func) 
#else
#define ATK_NATIVE_ENTER(env, that, func) 
#define ATK_NATIVE_EXIT(env, that, func) 
#endif

typedef enum {
	AtkObjectFactoryClass_1sizeof_FUNC,
	AtkObjectFactory_1sizeof_FUNC,
	_1ATK_1ACTION_1GET_1IFACE_FUNC,
	_1ATK_1COMPONENT_1GET_1IFACE_FUNC,
	_1ATK_1OBJECT_1FACTORY_1CLASS_FUNC,
	_1ATK_1SELECTION_1GET_1IFACE_FUNC,
	_1ATK_1TEXT_1GET_1IFACE_FUNC,
	_1GTK_1ACCESSIBLE_FUNC,
	_1atk_1focus_1tracker_1notify_FUNC,
	_1atk_1get_1default_1registry_FUNC,
	_1atk_1object_1factory_1create_1accessible_FUNC,
	_1atk_1object_1factory_1get_1accessible_1type_FUNC,
	_1atk_1object_1initialize_FUNC,
	_1atk_1object_1ref_1relation_1set_FUNC,
	_1atk_1registry_1get_1factory_FUNC,
	_1atk_1registry_1set_1factory_1type_FUNC,
	_1atk_1relation_1set_1get_1n_1relations_FUNC,
	_1atk_1relation_1set_1get_1relation_FUNC,
	_1atk_1relation_1set_1remove_FUNC,
	_1atk_1state_1set_1add_1state_FUNC,
	_1atk_1state_1set_1new_FUNC,
	_1call__II_FUNC,
	_1call__III_FUNC,
	_1call__IIII_FUNC,
	_1call__IIIII_FUNC,
	_1call__IIIIII_FUNC,
	_1call__IIIIIII_FUNC,
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2_FUNC,
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2_FUNC,
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2_FUNC,
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2_FUNC,
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2_FUNC,
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2_FUNC,
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2_FUNC,
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I_FUNC,
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I_FUNC,
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2I_FUNC,
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I_FUNC,
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I_FUNC,
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I_FUNC,
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I_FUNC,
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I_FUNC,
} ATK_FUNCS;
