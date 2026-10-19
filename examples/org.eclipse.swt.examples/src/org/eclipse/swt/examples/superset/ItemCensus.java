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
package org.eclipse.swt.examples.superset;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;

import org.eclipse.swt.widgets.Control;

/**
 * Counts the item objects a widget holds, by reading its private
 * {@code items} field (SWT Table/Tree, Nebula Grid, Gallery, CTree...):
 * non-null array slots or collection size. Test-harness introspection only;
 * returns -1 when the widget has no such field, so the count is reported as
 * not observable instead of guessed.
 */
final class ItemCensus {

	private final Field field;

	ItemCensus(Control control) {
		Field found = null;
		for (Class<?> c = control.getClass(); c != null && found == null; c = c.getSuperclass()) {
			try {
				Field f = c.getDeclaredField("items");
				if (f.getType().isArray() || Collection.class.isAssignableFrom(f.getType())) found = f;
			} catch (NoSuchFieldException e) {
				// keep walking up
			}
		}
		if (found != null) {
			try {
				found.setAccessible(true);
			} catch (RuntimeException e) {
				found = null;
			}
		}
		field = found;
	}

	/** @return item objects currently held, or -1 when not observable */
	long count(Control control) {
		if (field == null) return -1;
		try {
			Object v = field.get(control);
			if (v == null) return 0;
			if (v instanceof Collection<?> c) return c.size();
			if (!(v instanceof Object[] items)) return Array.getLength(v);
			long n = 0;
			for (Object item : items) {
				if (item != null) n++;
			}
			return n;
		} catch (IllegalAccessException | RuntimeException e) {
			return -1;
		}
	}
}
