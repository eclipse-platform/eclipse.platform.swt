/*******************************************************************************
 * Copyright (c) 2009, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.qt;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * Generic .toString() that collects all Java bean properties.
 */
public final class ToString {

	private static final String SEPARATOR = ", "; //$NON-NLS-1$

	private ToString() {

	}

	/**
	 * Collect all Java bean properties of the given object and return them as a
	 * printable string.
	 * 
	 * @param object
	 * @return toString()
	 */
	public static String of(Object object) {
		if (object == null) {
			return "NULL"; //$NON-NLS-1$
		}
		try {
			return of(object, Introspector.getBeanInfo(object.getClass()));
		} catch (IntrospectionException e) {
			return e.toString();
		}
	}

	/**
	 * Collect all Java bean properties of the given object but stop
	 * introspection at the given stop class and return them as a printable
	 * string.
	 * 
	 * @param object
	 * @param stopClass
	 *            if {@code null} stop class will be the super class of the
	 *            given object
	 * @return toString()
	 */
	public static String of(Object object, Class<?> stopClass) {
		if (object == null) {
			return "NULL"; //$NON-NLS-1$
		}
		if (stopClass == null) {
			stopClass = object.getClass().getSuperclass();
		}
		String result = object.toString() + " - "; //$NON-NLS-1$
		try {
			result = result + of(object, Introspector.getBeanInfo(object.getClass(), stopClass));
		} catch (IntrospectionException e) {
			result = result + e.toString();
		}
		return result;
	}

	@SuppressWarnings("nls")
	private static String of(Object object, BeanInfo beanInfo) {
		StringBuilder bob = new StringBuilder(object.getClass().getSimpleName()).append(" (");
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
			Method readMethod = propertyDescriptor.getReadMethod();
			try {
				if (readMethod != null && !propertyDescriptor.getName().equals("class")) {
					Object value = readMethod.invoke(object, (Object[]) null);
					if (value != null && value.toString().startsWith(value.getClass().getName() + "@")) {
						value = "..";
					}
					bob.append(propertyDescriptor.getName()).append('=').append(value).append(SEPARATOR);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (bob.toString().endsWith(SEPARATOR)) {
			bob.setLength(bob.length() - SEPARATOR.length());
		}
		bob.append(" )");
		return bob.toString();
	}
}
