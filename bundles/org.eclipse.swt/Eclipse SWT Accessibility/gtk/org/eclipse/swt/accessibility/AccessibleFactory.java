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
import org.eclipse.swt.internal.accessibility.gtk.*;
import org.eclipse.swt.internal.gtk.*;

class AccessibleFactory {
	int handle;
	AccessibleType accessibleType;
	Callback atkObjectFactoryCB_create_accessible;
	Callback gTypeInfoCB_base_init;
	Hashtable accessibles = new Hashtable (9);
	
	static Hashtable instances = new Hashtable (9);
	static AccessibleType childType;
	static final String CHILD_TYPE = "SWTChild";
	static final String DEFAULT_PARENTTYPE = "GtkAccessible";
	static final String FACTORY_PARENTTYPE = "AtkObjectFactory";
	static final byte[] SWT_TYPE_PREFIX = "SWT".getBytes ();
	static final byte[] FACTORY_TYPE = "SWTFactory".getBytes ();

	private AccessibleFactory (int widgetType) {
		super ();
		int widgetTypeName = ATK.g_type_name (widgetType);
		int widgetTypeNameLength = OS.strlen (widgetTypeName) + 1;
		byte[] buffer = new byte [widgetTypeNameLength];
		OS.memmove (buffer, widgetTypeName, widgetTypeNameLength);
		byte[] factoryName = new byte [FACTORY_TYPE.length + widgetTypeNameLength];
		System.arraycopy (FACTORY_TYPE, 0, factoryName, 0, FACTORY_TYPE.length);
		System.arraycopy (buffer, 0, factoryName, FACTORY_TYPE.length, widgetTypeNameLength);
		if (ATK.g_type_from_name (factoryName) == 0) {
			// register the factory
			int registry = ATK.atk_get_default_registry ();
			int previousFactory = ATK.atk_registry_get_factory (registry, widgetType);
			int parentType = ATK.atk_object_factory_get_accessible_type (previousFactory);
			int swtFactory = createFactory (factoryName);
			if (parentType == 0) {
				parentType = ATK.g_type_from_name (Converter.wcsToMbcs (null, DEFAULT_PARENTTYPE, true));
			}
			ATK.atk_registry_set_factory_type (registry, widgetType, swtFactory);
			byte[] newTypeName = new byte [SWT_TYPE_PREFIX.length + widgetTypeNameLength];
			System.arraycopy (SWT_TYPE_PREFIX, 0, newTypeName, 0, SWT_TYPE_PREFIX.length);
			System.arraycopy (buffer, 0, newTypeName, SWT_TYPE_PREFIX.length, widgetTypeNameLength);
			accessibleType = new AccessibleType (newTypeName, parentType);
		}
	}
	
	void addAccessible (Accessible accessible, int widgetHandle) {
		accessibles.put (new Integer (widgetHandle), accessible);
	}

	int atkObjectFactory_create_accessible (int widget) {
		Accessible accessible = (Accessible) accessibles.get (new Integer (widget));
		return accessibleType.createObject (accessible, widget);
	}
	
	int createFactory (byte[] name) {
		int parent = ATK.g_type_from_name (Converter.wcsToMbcs (null, FACTORY_PARENTTYPE, true));
		gTypeInfoCB_base_init  = new Callback (this, "gTypeInfo_base_init", 1);
		GTypeInfo typeInfo = new GTypeInfo ();
		typeInfo.base_init = gTypeInfoCB_base_init.getAddress ();
		typeInfo.class_size = ATK.AtkObjectFactoryClass_sizeof ();
		typeInfo.instance_size = ATK.AtkObjectFactory_sizeof ();
		handle = OS.g_malloc (GTypeInfo.sizeof); 
		ATK.memmove (handle, typeInfo, GTypeInfo.sizeof); 
		return ATK.g_type_register_static (parent, name, handle, 0);
	}

	static AccessibleType getChildType () {
		if (childType == null) {
			byte[] typeName = Converter.wcsToMbcs (null, CHILD_TYPE, true);
			int parentType = ATK.g_type_from_name (Converter.wcsToMbcs (null, DEFAULT_PARENTTYPE, true));
			childType = new AccessibleType (typeName, parentType);
		}
		return childType;
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
		int widgetType = ATK.G_TYPE_FROM_INSTANCE (widgetHandle);
		AccessibleFactory factory = (AccessibleFactory) instances.get (new Integer (widgetType));
		if (factory == null) {
			factory = new AccessibleFactory (widgetType);
			instances.put (new Integer (widgetType), factory);
		}
		factory.addAccessible (accessible, widgetHandle);
	}
}
