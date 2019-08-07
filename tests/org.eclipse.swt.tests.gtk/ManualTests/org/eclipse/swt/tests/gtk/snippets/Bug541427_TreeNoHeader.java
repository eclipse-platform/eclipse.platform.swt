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
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

public class Bug541427_TreeNoHeader {
	public static void main(String[] args) {
		// useless setup
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setSize(new Point(300, 200));

		final Tree tree = new Tree(shell, SWT.NO_SCROLL);
		tree.setHeaderVisible(true);
		tree.setBounds(0, 0, 300, tree.getHeaderHeight());
		TreeColumn col1 = new TreeColumn(tree, SWT.NONE);
		col1.setText("Hi Mom!");
		col1.setWidth(200);
		TreeColumn col2 = new TreeColumn(tree, SWT.NONE);
		col2.setText("Hi Dad!");
		col2.setWidth(200);
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
