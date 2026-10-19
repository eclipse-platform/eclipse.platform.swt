/*******************************************************************************
 * Copyright (c) 2026 Synexia, Hitesh Soliwal (hsoliwal@gmail.com) and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0, or (at your option) the Eclipse
 * Public License 2.0, which are available at
 * https://www.apache.org/licenses/LICENSE-2.0 and
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: Apache-2.0 OR EPL-2.0
 *
 * Contributors:
 *     Hitesh Soliwal (Synexia) - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import java.lang.reflect.*;
import java.util.*;

/**
 * Reflective access to the package-private virtual Tree classes of <code>org.eclipse.swt.widgets</code>
 * (topology, projection, residency, resident plan, logical viewport) for headless tests. Boxed
 * <code>Integer</code>, <code>Long</code> and <code>Boolean</code> arguments select primitive parameters;
 * a failure of the invoked member is rethrown unwrapped.
 */
final class VirtualTreeReflection {
	static final int ROOT = -1;
	/*
	 * The one place that names the package of the virtual Tree classes. They are toolkit-neutral (no
	 * Display, no native handle), so these tests move with them when the package is renamed.
	 */
	static final String PACKAGE = "org.eclipse.swt.widgets";

	private VirtualTreeReflection () {
	}

	static Class<?> type (String simpleName) {
		try {
			return Class.forName (PACKAGE + "." + simpleName);
		} catch (ClassNotFoundException e) {
			throw new AssertionError (e);
		}
	}

	private static Class<?> [] types (Object... args) {
		return Arrays.stream (args).map (v -> v instanceof Integer ? int.class
				: v instanceof Long ? long.class : v instanceof Boolean ? boolean.class : v.getClass ())
				.toArray (Class<?> []::new);
	}

	private static RuntimeException unwrap (InvocationTargetException failure) {
		Throwable cause = failure.getCause ();
		if (cause instanceof RuntimeException runtime) return runtime;
		if (cause instanceof Error error) throw error;
		return new IllegalStateException (cause);
	}

	static Object construct (String simpleName, Object... args) {
		return constructTyped (simpleName, types (args), args);
	}

	static Object constructTyped (String simpleName, Class<?> [] types, Object... args) {
		try {
			Constructor<?> constructor = type (simpleName).getDeclaredConstructor (types);
			constructor.setAccessible (true);
			return constructor.newInstance (args);
		} catch (InvocationTargetException e) {
			throw unwrap (e);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	static Object call (Object target, String name, Object... args) {
		return callTyped (target, name, types (args), args);
	}

	static Object callTyped (Object target, String name, Class<?> [] types, Object... args) {
		return invoke (target.getClass (), target, name, types, args);
	}

	static Object callStatic (String simpleName, String name, Class<?> [] types, Object... args) {
		return invoke (type (simpleName), null, name, types, args);
	}

	private static Object invoke (Class<?> owner, Object target, String name, Class<?> [] types, Object... args) {
		try {
			Method method = owner.getDeclaredMethod (name, types);
			method.setAccessible (true);
			return method.invoke (target, args);
		} catch (InvocationTargetException e) {
			throw unwrap (e);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	static Object field (Object target, String name) {
		try {
			Field field = target.getClass ().getDeclaredField (name);
			field.setAccessible (true);
			return field.get (target);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	static void setField (Object target, String name, Object value) {
		try {
			Field field = target.getClass ().getDeclaredField (name);
			field.setAccessible (true);
			field.set (target, value);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	static Object staticField (String simpleName, String name) {
		try {
			Field field = type (simpleName).getDeclaredField (name);
			field.setAccessible (true);
			return field.get (null);
		} catch (ReflectiveOperationException e) {
			throw new AssertionError (e);
		}
	}

	/** A <code>VirtualItemState</code> bit. */
	static long bit (String name) {
		return (Long) staticField ("VirtualItemState", name);
	}

	static Object topology () {
		return construct ("VirtualTreeTopology");
	}

	static Object projection (Object topology) {
		return constructTyped ("VirtualTreeVisibleProjection", new Class<?> [] {type ("VirtualTreeTopology")}, topology);
	}

	static Object residency () {
		return construct ("VirtualTreeVisibleProjection$Residency");
	}

	/** A projection's residency window, written into a new residency buffer. */
	static Object residencyWindow (Object projection, long first, int count) {
		Object residency = residency ();
		callTyped (projection, "residencyWindow", new Class<?> [] {long.class, int.class, residency.getClass ()}, first, count, residency);
		return residency;
	}

	static int i (Object value) {
		return (Integer) value;
	}

	static long l (Object value) {
		return (Long) value;
	}

	static boolean z (Object value) {
		return (Boolean) value;
	}
}
