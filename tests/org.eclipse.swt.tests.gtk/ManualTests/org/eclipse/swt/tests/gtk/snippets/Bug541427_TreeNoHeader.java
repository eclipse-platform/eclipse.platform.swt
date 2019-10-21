/*******************************************************************************
 * Copyright (c) 2018 Matthew Khouzam and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *
 * Contributors:
 *     Matthew Khouzam - initial API and implementation
 *     Syntevo         - more tests
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class Bug541427_TreeNoHeader {
	public static void main(String[] args) {
		// boilerplate
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new RowLayout(SWT.HORIZONTAL));

		for (int iTests = 0; iTests < (1 << 2); iTests++)
		{
			final boolean USE_NO_SCROLL    = (iTests & (1 << 0)) != 0;
			final boolean PACK_BEFORE_SIZE = (iTests & (1 << 1)) != 0;

			Composite testComposite = new Composite(shell, 0);
			testComposite.setLayout(new RowLayout(SWT.VERTICAL));

			for (int iHeightDiff = -5; iHeightDiff <= 5; iHeightDiff++)
			{
				Group group = new Group(testComposite, 0);
				group.setText(String.format(
						"%+d   %c%c",
						iHeightDiff,
						USE_NO_SCROLL    ? 'N' : '_',
						PACK_BEFORE_SIZE ? 'P' : '_'
				));

				final Tree tree = new Tree(group, USE_NO_SCROLL ? SWT.NO_SCROLL : 0);
				tree.setHeaderVisible(true);

				// Remember header height before creating columns.
				// It returns 0 after a column is created, is that another bug?
				int treeH = tree.getHeaderHeight() + iHeightDiff;

				// some columns. 1 is already enough to demonstrate
				for (int iColumn = 0; iColumn < 3; iColumn++)
				{
					TreeColumn column = new TreeColumn(tree, SWT.NONE);
					column.setText("Column");
					column.setWidth(70);
				}

				if (PACK_BEFORE_SIZE)
					tree.pack();

				tree.setBounds(0, 0, 220, treeH);
			}
		}

		// boilerplate
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}