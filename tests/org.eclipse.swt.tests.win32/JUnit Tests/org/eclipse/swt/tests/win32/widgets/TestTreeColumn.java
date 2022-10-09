/*******************************************************************************
 * Copyright (c) 2022 Joerg Kubitz
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Joerg Kubitz - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.win32.widgets;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.junit.Test;

public class TestTreeColumn {

	@Test
	public void test_ColumnOrder()
			throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		Method getColumnIndex = Tree.class.getDeclaredMethod("getColumnIndex", int.class);
		getColumnIndex.setAccessible(true);
		Method getColumnIndexFromOS = Tree.class.getDeclaredMethod("getColumnIndexFromOS", int.class);
		getColumnIndexFromOS.setAccessible(true);
		Shell shell = new Shell();
		Tree tree = new Tree(shell, 0);
		List<TreeColumn> treeColumns = new ArrayList<>();
		try {
			assertEquals(0, (int) getColumnIndexFromOS.invoke(tree, 0));
			assertEquals(0, (int) getColumnIndexFromOS.invoke(tree, 1));
			assertEquals(0, (int) getColumnIndex.invoke(tree, 0));
			assertEquals(0, (int) getColumnIndex.invoke(tree, 1));
			for (; treeColumns.size() < 12;) { // create
				{
					int[] columnOrder = tree.getColumnOrder();
					assertEquals(treeColumns.size(), columnOrder.length);
					assertEquals(treeColumns.size(), tree.getColumnCount());
					for (int i = 0; i < treeColumns.size(); i++) {
						assertEquals(i, (int) getColumnIndexFromOS.invoke(tree, i));
						assertEquals(i, (int) getColumnIndex.invoke(tree, i));
					}
				}
				TreeColumn treeColumn = new TreeColumn(tree, SWT.NULL);
				treeColumns.add(treeColumn);
			}

			{ // reverse order
				int[] reversedColumnOrder = new int[treeColumns.size()];
				for (int i = 0; i < treeColumns.size(); i++) {
					reversedColumnOrder[i] = treeColumns.size() - i - 1;
				}
				tree.setColumnOrder(reversedColumnOrder);
				reversedColumnOrder = null;
				int[] columnOrder = tree.getColumnOrder();
				assertEquals(treeColumns.size(), columnOrder.length);
				assertEquals(treeColumns.size(), tree.getColumnCount());
				for (int i = 0; i < treeColumns.size(); i++) {
					assertEquals(treeColumns.size() - i - 1, (int) getColumnIndexFromOS.invoke(tree, i));
					assertEquals(treeColumns.size() - i - 1, (int) getColumnIndex.invoke(tree, i));
				}
			}
			for (; !treeColumns.isEmpty();) { // remove
				TreeColumn treeColumn = treeColumns.get(treeColumns.size() / 2);
				treeColumn.dispose();
				treeColumns.remove(treeColumn);
				int[] columnOrder = tree.getColumnOrder();
				assertEquals(treeColumns.size(), columnOrder.length);
				assertEquals(treeColumns.size(), tree.getColumnCount());
				for (int i = 0; i < treeColumns.size(); i++) {
					// still reversed
					assertEquals(treeColumns.size() - i - 1, (int) getColumnIndexFromOS.invoke(tree, i));
					assertEquals(treeColumns.size() - i - 1, (int) getColumnIndex.invoke(tree, i));
				}
			}
		} finally {
			treeColumns.forEach(TreeColumn::dispose);
			tree.dispose();
			shell.dispose();
		}
	}

	volatile static Object blackhole;
	/** performance measurement **/
	public static void main(String[] args) throws Exception {
		Shell shell = new Shell();

		Method getColumnIndexFromOS = Tree.class.getDeclaredMethod("getColumnIndexFromOS", int.class);
		getColumnIndexFromOS.setAccessible(true);
		Method getColumnRect = Tree.class.getDeclaredMethod("getColumnRect", int.class);
		getColumnRect.setAccessible(true);
		Method getColumnIndex = Tree.class.getDeclaredMethod("getColumnIndex", int.class);
		getColumnIndex.setAccessible(true);
		Tree tree = new Tree(shell, 0);
		TreeColumn treeColumn = new TreeColumn(tree, SWT.NULL);
		int count = 10000000;
		try {
			long overhead;
			{
				long n0 = System.nanoTime();
				for (int i = 0; i < count; i++) {
					blackhole = 0;
				}
				long n1 = System.nanoTime();
				overhead = (n1 - n0);
				System.out.println((n1 - n0) / count + "ns/nothing");
				// ~ 6ns (nothing)
			}
			{
				long n0 = System.nanoTime();
				for (int i = 0; i < count; i++) {
					blackhole = getColumnIndexFromOS.invoke(tree, 0);
				}
				long n1 = System.nanoTime();
				System.out.println((n1 - n0 - overhead) / count + "ns/getColumnIndexFromOS");
				// ~ 400ns
			}
			{
				long n0 = System.nanoTime();
				for (int i = 0; i < count; i++) {
					blackhole = getColumnRect.invoke(tree, 0);
				}
				long n1 = System.nanoTime();
				System.out.println((n1 - n0 - overhead) / count + "ns/getColumnRect");
				// ~ 480ns
			}
			{
				long n0 = System.nanoTime();
				for (int i = 0; i < count; i++) {
					blackhole = getColumnIndex.invoke(tree, 0);
				}
				long n1 = System.nanoTime();
				System.out.println((n1 - n0 - overhead) / count + "ns/getColumnIndex");
				// ~ 6ns (cached)
			}

		} finally {
			treeColumn.dispose();
			tree.dispose();
			shell.dispose();
		}
	}
}
