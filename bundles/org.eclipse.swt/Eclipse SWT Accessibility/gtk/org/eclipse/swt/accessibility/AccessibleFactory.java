/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.accessibility;


import java.util.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

class AccessibleFactory {
	int handle, parentType, previousFactory;
	Callback atkObjectFactoryCB_create_accessible;
	Callback gTypeInfoCB_base_init;
	Hashtable accessibles = new Hashtable (9);

	static Hashtable instances = new Hashtable (9);
	static AccessibleType childType;
	static final String CHILD_TYPENAME = "SWTChild";
	static final String FACTORY_NAME = "SWTFactory";
	static final String FACTORY_PARENT_CLASS = "AtkObjectFactory";
	static final String DEFAULT_PARENT_TYPE = "AtkObject";

	private AccessibleFactory (int widgetType) {
		super ();
		int widgetTypeName = OS.g_type_name (widgetType);
		byte[] factoryName = concat (FACTORY_NAME, widgetTypeName);
		if (OS.g_type_from_name (factoryName) == 0) {
			// register the factory
			int registry = ATK.atk_get_default_registry ();
			previousFactory = ATK.atk_registry_get_factory (registry, widgetType);
			parentType = ATK.atk_object_factory_get_accessible_type (previousFactory);
			if (parentType == 0) {
				parentType = OS.g_type_from_name (Converter.wcsToMbcs (null, DEFAULT_PARENT_TYPE, true));
			}
			// the following line is intentionally commented
			// OS.g_object_ref(previousFactory);
			int swtFactory = createFactory (factoryName);
			ATK.atk_registry_set_factory_type (registry, widgetType, swtFactory);
		}
	}
	
	void addAccessible (Accessible accessible, int widgetHandle) {
		accessibles.put (new Integer (widgetHandle), accessible);
	}

	int atkObjectFactory_create_accessible (int widget) {
		Accessible accessible = (Accessible) accessibles.get (new Integer (widget));
		return AccessibleType.getInstance ().createObject (accessible, widget, parentType);
	}
	
	byte[] concat (String str1, int str2ptr) {
		int str2length = OS.strlen (str2ptr) + 1;
		byte[] buffer = new byte [str2length];
		OS.memmove (buffer, str2ptr, str2length);
		byte[] newName = new byte [str1.length () + str2length];
		System.arraycopy (str1.getBytes (), 0, newName, 0, str1.length ());
		System.arraycopy (buffer, 0, newName, str1.length (), str2length);
		return newName;
	}

	int createFactory (byte[] name) {
		int parent = OS.g_type_from_name (Converter.wcsToMbcs (null, FACTORY_PARENT_CLASS, true));
		gTypeInfoCB_base_init  = new Callback (this, "gTypeInfo_base_init", 1);
		GTypeInfo typeInfo = new GTypeInfo ();
		typeInfo.base_init = gTypeInfoCB_base_init.getAddress ();
		typeInfo.class_size = ATK.AtkObjectFactoryClass_sizeof ();
		typeInfo.instance_size = ATK.AtkObjectFactory_sizeof ();
		handle = OS.g_malloc (GTypeInfo.sizeof); 
		OS.memmove (handle, typeInfo, GTypeInfo.sizeof); 
		return OS.g_type_register_static (parent, name, handle, 0);
	}

	int gTypeInfo_base_init (int klass) {
		int atkObjectFactoryClass = ATK.ATK_OBJECT_FACTORY_CLASS (klass);
		AtkObjectFactoryClass objectFactoryClassStruct = new AtkObjectFactoryClass ();
		ATK.memmove (objectFactoryClassStruct, atkObjectFactoryClass);
		atkObjectFactoryCB_create_accessible = new Callback (this, "atkObjectFactory_create_accessible", 1);
		objectFactoryClassStruct.create_accessible = atkObjectFactoryCB_create_accessible.getAddress ();
		ATK.memmove (atkObjectFactoryClass, objectFactoryClassStruct); 
		return 0;
	}
	
	static void registerAccessible (Accessible accessible, int widgetHandle) {
		int widgetType = OS.G_TYPE_FROM_INSTANCE (widgetHandle);
		AccessibleFactory factory = (AccessibleFactory) instances.get (new Integer (widgetType));
		if (factory == null) {
			factory = new AccessibleFactory (widgetType);
			instances.put (new Integer (widgetType), factory);
		}
		factory.addAccessible (accessible, widgetHandle);
	}
}
