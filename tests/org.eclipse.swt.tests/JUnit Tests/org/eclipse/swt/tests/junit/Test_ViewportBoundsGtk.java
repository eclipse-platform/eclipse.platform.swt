// Copyright 2026 Hitesh Soliwal and Contributors to the Synexia Project.
// SPDX-License-Identifier: Apache-2.0
package org.eclipse.swt.tests.junit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Receiving-platform regression; requires a live GTK3 display and original SWT natives. */
public class Test_ViewportBoundsGtk {
	private static final String LOGICAL_MODEL = "org.eclipse.swt.internal.gtk.virtualTreeLogicalNativeModel";
	private Display display;
	private Shell shell;
	private Composite parent;
	private String previousLogicalModel;

	@BeforeEach
	public void openDisplay() {
		previousLogicalModel = System.getProperty(LOGICAL_MODEL);
		assumeTrue("gtk".equals(SWT.getPlatform()) && !"1".equals(System.getenv("SWT_GTK4")),
				"GTK3 native viewport producer qualification");
		display = new Display();
		shell = new Shell(display);
		shell.setSize(640, 480);
		parent = new Composite(shell, SWT.NONE);
		parent.setBounds(0, 0, 600, 400);
		shell.open();
		settle();
	}

	@AfterEach
	public void disposeDisplay() {
		if (shell != null && !shell.isDisposed()) shell.dispose();
		if (display != null && !display.isDisposed()) display.dispose();
		if (previousLogicalModel == null) System.clearProperty(LOGICAL_MODEL);
		else System.setProperty(LOGICAL_MODEL, previousLogicalModel);
	}

	@Test
	public void tableZeroWidthThenReopenPreservesItemsAndSelection() throws Exception {
		Table table = table();
		TableItem item = table.getItem(4);
		item.setText("stable");
		table.setSelection(4);
		assertTableHasPaintRows(table);
		table.setSize(0, 300);
		settle();
		assertEquals(0, table.getClientArea().width);
		assertTableEmpty(table);
		assertSame(item, table.getItem(4));
		assertArrayEquals(new int[] {4}, table.getSelectionIndices());
		table.setSize(500, 300);
		settle();
		assertTableHasPaintRows(table);
		assertSame(item, table.getItem(4));
		assertEquals("stable", item.getText());
	}

	@Test
	public void tableHiddenAncestorSuppressesRowsButExplicitDataAccessRemains() throws Exception {
		Table table = table();
		int[] callbacks = {0};
		table.addListener(SWT.SetData, event -> {
			callbacks[0]++;
			((TableItem)event.item).setText("on-demand");
		});
		parent.setVisible(false);
		settle();
		assertFalse(table.isVisible());
		assertTableEmpty(table);
		int before = callbacks[0];
		assertEquals("on-demand", table.getItem(900).getText());
		assertEquals(before + 1, callbacks[0]);
		assertTableEmpty(table);
		parent.setVisible(true);
		settle();
		assertTableHasPaintRows(table);
	}

	@Test
	public void treeZeroWidthThenReopenPreservesIdentityInBothModels() throws Exception {
		for (boolean logical : new boolean[] {false, true}) {
			Tree tree = tree(logical);
			TreeItem item = tree.getItem(4);
			item.setText("tree-stable");
			tree.setSelection(item);
			assertTreeHasPaintRows(tree);
			tree.setSize(0, 300);
			settle();
			assertEquals(0, tree.getClientArea().width);
			assertTreeEmpty(tree);
			assertSame(item, tree.getItem(4));
			assertArrayEquals(new TreeItem[] {item}, tree.getSelection());
			tree.setSize(500, 300);
			settle();
			assertTreeHasPaintRows(tree);
			assertSame(item, tree.getItem(4));
			assertEquals("tree-stable", item.getText());
			tree.dispose();
		}
	}

	@Test
	public void treeHiddenAncestorCannotResurrectAllocatedPage() throws Exception {
		Tree tree = tree(true);
		assertTreeHasPaintRows(tree);
		assertTrue((int)field(tree, "virtualBodyExtent") > 0);
		parent.setVisible(false);
		settle();
		assertFalse(tree.isVisible());
		assertTreeEmpty(tree);
		assertEquals(0, field(tree, "virtualBodyExtent"));
		parent.setVisible(true);
		settle();
		assertTreeHasPaintRows(tree);
	}

	@Test
	public void treeZeroHeightRejectsStalePageAndRestoresDemandCallbacks() throws Exception {
		Tree tree = tree(true);
		assertTreeHasPaintRows(tree);
		int[] callbacks = {0};
		tree.addListener(SWT.SetData, event -> {
			callbacks[0]++;
			((TreeItem)event.item).setText("tree-on-demand");
		});
		tree.setSize(500, 0);
		settle();
		assertEquals(0, tree.getClientArea().height);
		assertTreeEmpty(tree);
		assertEquals(0, field(tree, "virtualBodyExtent"));
		int before = callbacks[0];
		assertEquals("tree-on-demand", tree.getItem(900).getText());
		assertEquals(before + 1, callbacks[0]);
		assertTreeEmpty(tree);
		tree.setSize(500, 300);
		settle();
		assertTreeHasPaintRows(tree);
	}

	private Table table() {
		Table table = new Table(parent, SWT.VIRTUAL | SWT.V_SCROLL);
		table.setBounds(0, 0, 500, 300);
		table.setItemCount(1000);
		settle();
		return table;
	}

	private Tree tree(boolean logical) {
		System.setProperty(LOGICAL_MODEL, Boolean.toString(logical));
		Tree tree = new Tree(parent, SWT.VIRTUAL | SWT.V_SCROLL);
		tree.setBounds(0, 0, 500, 300);
		tree.setItemCount(1000);
		settle();
		return tree;
	}

	private static void assertTableEmpty(Table table) throws Exception {
		invoke(table, "updateVirtualViewport");
		Object planner = field(table, "virtualViewport");
		assertEquals(0, invoke(planner, "visibleCount"));
		assertEquals(invoke(planner, "paintStart"), invoke(planner, "paintEndExclusive"));
	}

	private static void assertTableHasPaintRows(Table table) throws Exception {
		invoke(table, "updateVirtualViewport");
		Object planner = field(table, "virtualViewport");
		assertTrue((int)invoke(planner, "visibleCount") > 0);
		int count = (int)invoke(planner, "paintEndExclusive") - (int)invoke(planner, "paintStart");
		assertTrue(count <= (int)invoke(planner, "visibleCount") + 16);
	}

	private static void assertTreeEmpty(Tree tree) throws Exception {
		invoke(tree, "updateVirtualViewportGeometry");
		Object viewport = field(tree, "virtualViewport");
		assertEquals(0, invoke(viewport, "visibleRows"));
		assertEquals(0, invoke(viewport, "paintRowCount"));
	}

	private static void assertTreeHasPaintRows(Tree tree) throws Exception {
		invoke(tree, "updateVirtualViewportGeometry");
		Object viewport = field(tree, "virtualViewport");
		assertTrue((int)invoke(viewport, "visibleRows") > 0);
		assertTrue((int)invoke(viewport, "paintRowCount") <= (int)invoke(viewport, "visibleRows") + 16);
	}

	private void settle() {
		for (int pass = 0; pass < 64; pass++) {
			if (!display.readAndDispatch()) break;
		}
		shell.update();
	}

	private static Object field(Object receiver, String name) throws Exception {
		Field field = receiver.getClass().getDeclaredField(name);
		field.setAccessible(true);
		return field.get(receiver);
	}

	private static Object invoke(Object receiver, String name) throws Exception {
		Method method = receiver.getClass().getDeclaredMethod(name);
		method.setAccessible(true);
		return method.invoke(receiver);
	}
}
