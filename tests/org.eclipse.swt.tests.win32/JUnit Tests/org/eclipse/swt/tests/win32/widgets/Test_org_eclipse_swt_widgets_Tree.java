/*******************************************************************************
 * Copyright (c) 2026 vogella GmbH and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     vogella GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32.widgets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.tests.win32.SwtWin32TestUtil;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Pins which columns of a multi-column Tree report the selected state in EraseItem. */
public class Test_org_eclipse_swt_widgets_Tree {

	private static final int COLUMNS = 3;
	private static final int PAINT_TIMEOUT_MS = 2000;

	private record Erase(TreeItem item, int column, int detail) {
	}

	private final List<Erase> erases = new ArrayList<>();
	private Display display;
	private Shell shell;
	private Button focusTarget;

	@BeforeEach
	public void setUp() {
		display = Display.getDefault();
		shell = new Shell(display);
		// The selected state of a focused item also depends on the DC colors, so keep the focus elsewhere.
		focusTarget = new Button(shell, SWT.PUSH);
		focusTarget.setBounds(0, 0, 50, 20);
	}

	@AfterEach
	public void tearDown() {
		shell.dispose();
	}

	@Test
	public void test_fullSelection_reportsSelectedInEveryColumn() throws InterruptedException {
		Tree tree = createOpenTree(SWT.FULL_SELECTION);
		TreeItem selected = tree.getItem(1);
		tree.setSelection(selected);

		paint(tree);

		assertEquals(allColumns(), columnsWith(selected, SWT.SELECTED));
		assertEquals(Set.of(), columnsWith(tree.getItem(0), SWT.SELECTED));
	}

	@Test
	public void test_noFullSelection_reportsSelectedOnlyInFirstColumn() throws InterruptedException {
		Tree tree = createOpenTree(SWT.NONE);
		TreeItem selected = tree.getItem(1);
		tree.setSelection(selected);

		paint(tree);

		assertEquals(Set.of(0), columnsWith(selected, SWT.SELECTED));
	}

	private Tree createOpenTree(int style) throws InterruptedException {
		Tree tree = new Tree(shell, style);
		tree.setBounds(0, 30, 330, 150);
		tree.setHeaderVisible(true);
		for (int i = 0; i < COLUMNS; i++) {
			TreeColumn column = new TreeColumn(tree, SWT.LEFT);
			column.setText("Column " + i);
			column.setWidth(100);
		}
		for (int i = 0; i < 3; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText(new String[] { "a" + i, "b" + i, "c" + i });
		}
		tree.addListener(SWT.EraseItem, e -> erases.add(new Erase((TreeItem) e.item, e.index, e.detail)));
		shell.setSize(400, 300);
		shell.open();
		focusTarget.setFocus();
		SwtWin32TestUtil.processEvents(display, PAINT_TIMEOUT_MS, () -> !erases.isEmpty());
		return tree;
	}

	private void paint(Tree tree) throws InterruptedException {
		erases.clear();
		tree.redraw();
		tree.update();
		SwtWin32TestUtil.processEvents(display, PAINT_TIMEOUT_MS, () -> !erases.isEmpty());
		assertFalse(erases.isEmpty(), "Tree was not painted");
	}

	private Set<Integer> columnsWith(TreeItem item, int flag) {
		Set<Integer> columns = new TreeSet<>();
		for (Erase erase : erases) {
			if (erase.item() == item && (erase.detail() & flag) != 0) {
				columns.add(erase.column());
			}
		}
		return columns;
	}

	private static Set<Integer> allColumns() {
		Set<Integer> columns = new TreeSet<>();
		for (int i = 0; i < COLUMNS; i++) {
			columns.add(i);
		}
		return columns;
	}
}
