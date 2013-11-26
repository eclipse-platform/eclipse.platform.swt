/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tools.internal;

public abstract class ReflectItem extends AbstractItem {

static boolean convertTo32Bit(JNIType[] paramTypes, boolean floatingPointTypes) {
	boolean changed = false;
	for (int i = 0; i < paramTypes.length; i++) {
		JNIType paramType = paramTypes[i];
		if (paramType.isType("long")) {
			paramTypes[i] = new ReflectType(Integer.TYPE);
			changed = true;
		}
		if (paramType.isType("[J")) {
			paramTypes[i] = new ReflectType(int[].class);
			changed = true;
		}
		if (floatingPointTypes) {
			if (paramType.isType("double")) {
				paramTypes[i] = new ReflectType(Float.TYPE);
				changed = true;
			}
			if (paramType.isType("[D")) {
				paramTypes[i] = new ReflectType(float[].class);
				changed = true;
			}
		}
	}
	return changed;	
}

boolean canChange64(Class<?> clazz) {
	if (!GEN64) return false;
	return clazz == Integer.TYPE ||
		clazz == Long.TYPE ||
		clazz == Float.TYPE ||
		clazz == Double.TYPE ||
		clazz == int[].class ||
		clazz == long[].class ||
		clazz == float[].class ||
		clazz == double[].class;
}
}
