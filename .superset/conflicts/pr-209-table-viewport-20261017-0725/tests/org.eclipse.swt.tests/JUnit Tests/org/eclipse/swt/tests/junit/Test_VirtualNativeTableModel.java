/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Native coordinate ownership at the logical-model / resident-model boundary. */
public class Test_VirtualNativeTableModel {

private Class<?> gtk, os, memory;
private final byte[] itemCountProperty = "swt-item-count\0".getBytes(java.nio.charset.StandardCharsets.US_ASCII);

@BeforeEach
public void requireGtk3 () throws Exception {
	assumeTrue ("gtk".equals (SWT.getPlatform ()));
	gtk = Class.forName ("org.eclipse.swt.internal.gtk.GTK");
	assumeFalse (gtk.getField ("GTK4").getBoolean (null));
	os = Class.forName ("org.eclipse.swt.internal.gtk.OS");
	memory = Class.forName ("org.eclipse.swt.internal.C");
}

private Object call (Class<?> owner, String name, Class<?>[] parameters, Object... arguments) throws Exception {
	Method method = owner.getMethod (name, parameters);
	return method.invoke (null, arguments);
}

private long newModel (int count) throws Exception {
	long type = (Long)call (os, "content_providers_create_gtype", new Class<?>[] {String.class}, "SwtVirtualTableModel");
	assertNotEquals (0, type);
	long model = (Long)call (os, "g_object_new", new Class<?>[] {long.class, long.class}, type, 0L);
	assertNotEquals (0, model);
	setCount (model, count);
	return model;
}

private void setCount (long model, int count) throws Exception {
	call (os, "g_object_set", new Class<?>[] {long.class, byte[].class, int.class, long.class},
			model, itemCountProperty, count, 0L);
}

private long newIter () throws Exception {
	int size = (Integer)call (gtk, "GtkTreeIter_sizeof", new Class<?>[0]);
	long iter = (Long)call (os, "g_malloc", new Class<?>[] {long.class}, (long)size);
	assertNotEquals (0, iter);
	return iter;
}

private boolean seek (long model, long iter, int row) throws Exception {
	return (Boolean)call (gtk, "gtk_tree_model_iter_nth_child",
			new Class<?>[] {long.class, long.class, long.class, int.class}, model, iter, 0L, row);
}

private int rowCount (long model) throws Exception {
	return (Integer)call (gtk, "gtk_tree_model_iter_n_children",
			new Class<?>[] {long.class, long.class}, model, 0L);
}

private long path (long model, long iter) throws Exception {
	return (Long)call (gtk, "gtk_tree_model_get_path", new Class<?>[] {long.class, long.class}, model, iter);
}

private void freePath (long path) throws Exception {
	if (path != 0) call (gtk, "gtk_tree_path_free", new Class<?>[] {long.class}, path);
}

private void free (long address) throws Exception {
	if (address != 0) call (os, "g_free", new Class<?>[] {long.class}, address);
}

private void unref (long object) throws Exception {
	if (object != 0) call (os, "g_object_unref", new Class<?>[] {long.class}, object);
}

/** The geometry helper must link through real JNI and leave no stale output on refusal.
 * This exercises the ABI/refusal boundary only; positive layout needs a display. */
@Test
public void logicalCellAllocationRejectsForeignModelAndClearsOutput () throws Exception {
	Class<?> rectangle = Class.forName ("org.eclipse.swt.internal.gtk.GdkRectangle");
	Object allocation = rectangle.getConstructor ().newInstance ();
	Class<?>[] signature = {long.class, long.class, long.class, int.class, int.class, rectangle};
	long foreign = newModel (48);
	try {
		for (int width : new int[] {-1, 0, 1, Integer.MAX_VALUE}) {
			for (String field : new String[] {"x", "y", "width", "height"}) rectangle.getField (field).setInt (allocation, 73);
			assertFalse ((Boolean)call (os, "swt_tree_view_column_get_logical_cell_allocation", signature,
					foreign, foreign, foreign, width, 23, allocation));
			for (String field : new String[] {"x", "y", "width", "height"}) assertEquals (0, rectangle.getField (field).getInt (allocation), field);
		}
		assertFalse ((Boolean)call (os, "swt_tree_view_column_get_logical_cell_allocation", signature,
				0L, 0L, 0L, 100, 23, null));
		assertEquals (48, rowCount (foreign), "refusal must not mutate the live foreign model");
	} finally {
		unref (foreign);
	}
}

@Test
public void sameGenerationDoesNotMakeIterBelongToAnotherModel () throws Exception {
	long logical = 0, resident = 0, logicalIter = 0, residentIter = 0;
	try {
		logical = newModel (1_000_000);
		resident = newModel (48);
		logicalIter = newIter ();
		residentIter = newIter ();
		assertTrue (seek (logical, logicalIter, 7));
		assertTrue (seek (resident, residentIter, 7));
		// GtkTreeIter starts with a gint stamp. Force an equal stamp without
		// changing the opaque owner/payload fields, making this deterministic.
		int[] stamp = new int[1];
		call (memory, "memmove", new Class<?>[] {int[].class, long.class, long.class}, stamp, residentIter, 4L);
		call (memory, "memmove", new Class<?>[] {long.class, int[].class, long.class}, logicalIter, stamp, 4L);
		long foreign = path (resident, logicalIter);
		try {
			assertEquals (0, foreign, "a logical iter must not be accepted by the physical model even when generations match");
		} finally {
			freePath (foreign);
		}
		assertEquals (1_000_000, rowCount (logical));
		assertEquals (48, rowCount (resident));
	} finally {
		free (residentIter);
		free (logicalIter);
		unref (resident);
		unref (logical);
	}
}

@Test
public void maximumLogicalCountUsesConstantNativeModelStateAndResetInvalidatesIter () throws Exception {
	long model = 0, iter = 0;
	try {
		model = newModel (Integer.MAX_VALUE);
		iter = newIter ();
		assertEquals (Integer.MAX_VALUE, rowCount (model));
		assertTrue (seek (model, iter, Integer.MAX_VALUE - 1));
		long last = path (model, iter);
		try {
			assertNotEquals (0, last);
			long indices = (Long)call (gtk, "gtk_tree_path_get_indices", new Class<?>[] {long.class}, last);
			int[] row = new int[1];
			call (memory, "memmove", new Class<?>[] {int[].class, long.class, long.class}, row, indices, 4L);
			assertEquals (Integer.MAX_VALUE - 1, row[0]);
		} finally {
			freePath (last);
		}
		setCount (model, Integer.MAX_VALUE);
		long stale = path (model, iter);
		try {
			assertEquals (0, stale, "count publication must invalidate previously issued iters");
		} finally {
			freePath (stale);
		}
		assertTrue (seek (model, iter, Integer.MAX_VALUE - 1));
		assertFalse ((Boolean)call (gtk, "gtk_tree_model_iter_next", new Class<?>[] {long.class, long.class}, model, iter));
		setCount (model, 0);
		assertEquals (0, rowCount (model));
		assertFalse (seek (model, iter, 0));
	} finally {
		free (iter);
		unref (model);
	}
}
}
