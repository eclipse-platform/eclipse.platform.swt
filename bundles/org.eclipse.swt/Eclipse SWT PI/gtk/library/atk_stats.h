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

#ifdef NATIVE_STATS
extern int ATK_nativeFunctionCount;
extern int ATK_nativeFunctionCallCount[];
extern char* ATK_nativeFunctionNames[];
#define ATK_NATIVE_ENTER(env, that, func) ATK_nativeFunctionCallCount[func]++;
#define ATK_NATIVE_EXIT(env, that, func) 
#else
#ifndef ATK_NATIVE_ENTER
#define ATK_NATIVE_ENTER(env, that, func) 
#endif
#ifndef ATK_NATIVE_EXIT
#define ATK_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	ATK_1IS_1NO_1OP_1OBJECT_1FACTORY_FUNC,
	ATK_1TYPE_1ACTION_FUNC,
	ATK_1TYPE_1COMPONENT_FUNC,
	ATK_1TYPE_1EDITABLE_1TEXT_FUNC,
	ATK_1TYPE_1HYPERTEXT_FUNC,
	ATK_1TYPE_1OBJECT_1FACTORY_FUNC,
	ATK_1TYPE_1SELECTION_FUNC,
	ATK_1TYPE_1TABLE_FUNC,
	ATK_1TYPE_1TEXT_FUNC,
	ATK_1TYPE_1VALUE_FUNC,
	AtkAttribute_1sizeof_FUNC,
	AtkObjectFactoryClass_1sizeof_FUNC,
	AtkObjectFactory_1sizeof_FUNC,
	AtkTextRange_1sizeof_FUNC,
	AtkTextRectangle_1sizeof_FUNC,
	GTK_1TYPE_1ACCESSIBLE_FUNC,
	_1ATK_1ACTION_1GET_1IFACE_FUNC,
	_1ATK_1COMPONENT_1GET_1IFACE_FUNC,
	_1ATK_1EDITABLE_1TEXT_1GET_1IFACE_FUNC,
	_1ATK_1HYPERTEXT_1GET_1IFACE_FUNC,
	_1ATK_1OBJECT_1FACTORY_1CLASS_FUNC,
	_1ATK_1SELECTION_1GET_1IFACE_FUNC,
	_1ATK_1TABLE_1GET_1IFACE_FUNC,
	_1ATK_1TEXT_1GET_1IFACE_FUNC,
	_1ATK_1VALUE_1GET_1IFACE_FUNC,
	_1GTK_1ACCESSIBLE_FUNC,
	_1atk_1focus_1tracker_1notify_FUNC,
	_1atk_1get_1default_1registry_FUNC,
	_1atk_1object_1factory_1create_1accessible_FUNC,
	_1atk_1object_1factory_1get_1accessible_1type_FUNC,
	_1atk_1object_1initialize_FUNC,
	_1atk_1object_1notify_1state_1change_FUNC,
	_1atk_1object_1ref_1relation_1set_FUNC,
	_1atk_1registry_1get_1factory_FUNC,
	_1atk_1registry_1set_1factory_1type_FUNC,
	_1atk_1relation_1set_1get_1n_1relations_FUNC,
	_1atk_1relation_1set_1get_1relation_FUNC,
	_1atk_1relation_1set_1remove_FUNC,
	_1atk_1role_1register_FUNC,
	_1atk_1state_1set_1add_1state_FUNC,
	_1atk_1state_1set_1new_FUNC,
	_1atk_1text_1attribute_1get_1name_FUNC,
	_1atk_1text_1attribute_1get_1value_FUNC,
	_1atk_1text_1attribute_1register_FUNC,
#ifndef JNI64
	_1call__II_FUNC,
#else
	_1call__JJ_FUNC,
#endif
#ifndef JNI64
	_1call__III_FUNC,
#else
	_1call__JJJ_FUNC,
#endif
#ifndef JNI64
	_1call__IIII_FUNC,
#else
	_1call__JJJJ_FUNC,
#endif
#ifndef JNI64
	_1call__IIIII_FUNC,
#else
	_1call__JJJJJ_FUNC,
#endif
#ifndef JNI64
	_1call__IIIIII_FUNC,
#else
	_1call__JJJJJJ_FUNC,
#endif
#ifndef JNI64
	_1call__IIIIIII_FUNC,
#else
	_1call__JJJJJJJ_FUNC,
#endif
	g_1strdup_FUNC,
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2I_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2I_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2I_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2I_FUNC,
#endif
#ifndef JNI64
	memmove__ILorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2_FUNC,
#else
	memmove__JLorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2I_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkActionIface_2J_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkAttribute_2JI_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2I_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkComponentIface_2J_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2I_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkEditableTextIface_2J_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2I_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkHypertextIface_2J_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2I_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectClass_2J_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2I_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkObjectFactoryClass_2J_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2I_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkSelectionIface_2J_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2I_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTableIface_2J_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2I_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextIface_2J_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRange_2JI_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2II_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkTextRectangle_2JI_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2I_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_AtkValueIface_2J_FUNC,
#endif
#ifndef JNI64
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2I_FUNC,
#else
	memmove__Lorg_eclipse_swt_internal_accessibility_gtk_GtkAccessible_2J_FUNC,
#endif
} ATK_FUNCS;
