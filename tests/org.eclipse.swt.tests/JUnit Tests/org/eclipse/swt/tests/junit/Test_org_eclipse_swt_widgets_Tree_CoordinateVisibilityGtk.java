/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License 2.0 which accompanies this distribution, and is
 * available at https://www.eclipse.org/legal/epl-2.0/
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.jupiter.api.Test;

/** Real GTK Tree, logical/native model, and its registered accessibility callbacks. */
public class Test_org_eclipse_swt_widgets_Tree_CoordinateVisibilityGtk {

	private static final String LOGICAL_MODEL = "org.eclipse.swt.internal.gtk.virtualTreeLogicalNativeModel";

	@Test
	public void selectedRowsExcludeCollapsedCoordinatesAndRetainPublicItemIdentity () throws Exception {
		assumeTrue ("gtk".equals (SWT.getPlatform ()));
		assumeTrue (!Class.forName ("org.eclipse.swt.internal.gtk.GTK").getField ("GTK4").getBoolean (null));
		Display display = Display.getCurrent ();
		boolean ownDisplay = display == null;
		if (ownDisplay) display = new Display ();
		Shell shell = new Shell (display);
		try {
			shell.setLayout (new FillLayout ());
			shell.setSize (480, 320);
			Tree tree = createTree (shell);
			int [] dataEvents = {0}, selectionEvents = {0};
			tree.addListener (SWT.SetData, event -> {
				dataEvents [0]++;
				((TreeItem)event.item).setText ("row " + event.index);
			});
			tree.addListener (SWT.Selection, event -> selectionEvents [0]++);
			tree.setItemCount (32);
			TreeItem parent = tree.getItem (1);
			parent.setText ("parent");
			parent.setItemCount (4);
			TreeItem branch = parent.getItem (1);
			branch.setText ("branch");
			branch.setItemCount (3);
			TreeItem leaf = branch.getItem (2);
			leaf.setText ("selected descendant");
			TreeItem sibling = tree.getItem (10);
			sibling.setText ("selected root");
			parent.setExpanded (true);
			branch.setExpanded (true);
			tree.setSelection (new TreeItem [] {leaf, sibling});
			shell.open ();
			settle (display);
			assertTrue ((Boolean)invoke (tree, "usesBoundedVirtualView"), "exercise the real bounded GTK model");
			Accessible accessible = tree.getAccessible ();
			List<AccessibleTableListener> listeners = tableListeners (accessible);
			assertSelectedRows (accessible, listeners, 6, 17);

			branch.setExpanded (false);
			settle (display);
			assertSelectionIdentity (tree, sibling);
			tree.select (leaf);
			assertFalse (branch.getExpanded (), "select does not reveal a hidden child");
			assertSelectedRows (accessible, listeners, 14);
			assertSelectionIdentity (tree, leaf, sibling);

			branch.setExpanded (true);
			parent.setExpanded (false);
			settle (display);
			assertSelectionIdentity (tree, sibling);
			tree.select (leaf);
			assertFalse (parent.getExpanded (), "select preserves the collapsed ancestor");
			assertSelectedRows (accessible, listeners, 10);
			assertSelectionIdentity (tree, leaf, sibling);
			int measuredBeforeQueries = dataEvents [0];
			for (int iteration = 0; iteration < 100; iteration++) assertSelectedRows (accessible, listeners, 10);
			assertEquals (measuredBeforeQueries, dataEvents [0], "accessibility must not materialize hidden children");

			parent.setExpanded (true);
			settle (display);
			assertSelectedRows (accessible, listeners, 6, 17);
			assertSame (parent, tree.getItem (1));
			assertSame (branch, parent.getItem (1));
			assertSame (leaf, branch.getItem (2));
			assertSame (sibling, tree.getItem (10));
			assertEquals (0, selectionEvents [0], "programmatic collapse and accessibility queries are selection-neutral");
			System.out.println ("coordinate-visibility GTK heap-used-bytes="
					+ (Runtime.getRuntime ().totalMemory () - Runtime.getRuntime ().freeMemory ()));
		} finally {
			if (!shell.isDisposed ()) shell.dispose ();
			if (ownDisplay && !display.isDisposed ()) display.dispose ();
		}
	}

	private static Tree createTree (Shell shell) {
		String previous = System.getProperty (LOGICAL_MODEL);
		System.setProperty (LOGICAL_MODEL, "true");
		try {
			return new Tree (shell, SWT.VIRTUAL | SWT.MULTI | SWT.V_SCROLL);
		} finally {
			if (previous == null) System.clearProperty (LOGICAL_MODEL);
			else System.setProperty (LOGICAL_MODEL, previous);
		}
	}

	private static Object invoke (Tree tree, String methodName) throws Exception {
		Method method = Tree.class.getDeclaredMethod (methodName);
		method.setAccessible (true);
		return method.invoke (tree);
	}

	@SuppressWarnings ("unchecked")
	private static List<AccessibleTableListener> tableListeners (Accessible accessible) throws Exception {
		Field field = Accessible.class.getDeclaredField ("accessibleTableListeners");
		field.setAccessible (true);
		List<AccessibleTableListener> listeners = (List<AccessibleTableListener>)field.get (accessible);
		assertNotNull (listeners);
		assertFalse (listeners.isEmpty (), "the Tree must install its real table-accessibility adapter");
		return listeners;
	}

	private static void assertSelectedRows (Accessible accessible, List<AccessibleTableListener> listeners, int... expected) {
		AccessibleTableEvent rows = new AccessibleTableEvent (accessible);
		AccessibleTableEvent count = new AccessibleTableEvent (accessible);
		for (AccessibleTableListener listener : listeners) {
			listener.getSelectedRows (rows);
			listener.getSelectedRowCount (count);
		}
		assertArrayEquals (expected, rows.selected, "selected visible row coordinates");
		assertEquals (expected.length, count.count, "selected-row list and count must agree");
		for (int row : rows.selected) {
			AccessibleTableEvent selected = new AccessibleTableEvent (accessible);
			selected.row = row;
			for (AccessibleTableListener listener : listeners) listener.isRowSelected (selected);
			assertTrue (selected.isSelected, "reported selected row must refer to the selected logical item");
		}
	}

	private static void assertSelectionIdentity (Tree tree, TreeItem... expected) {
		TreeItem [] actual = tree.getSelection ();
		assertEquals (expected.length, actual.length);
		for (TreeItem item : expected) assertTrue (Arrays.asList (actual).contains (item));
	}

	private static void settle (Display display) {
		for (int pass = 0; pass < 4; pass++) {
			while (display.readAndDispatch ()) { }
			display.update ();
		}
	}
}
