/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
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
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/*
 * Title: Bug 530969: [GTK3] Control.print() doesn't seem to work
 * How to run: launch snippet and observe the TreeItems
 * Bug description: all TreeItems have a white box as their icon
 * Expected results: all TreeItems should have a checkbox image (in some state) set as the icon
 * GTK Version(s): GTK3
 */
public class Bug530969_ControlPrint {

	private static void createButton(Composite parent, Color bgColor,
			boolean checked, boolean enabled) {
		Button button = new Button(parent, SWT.CHECK);
		button.setSelection(checked);
		button.setEnabled(enabled);
		button.setBackground(bgColor);
		Point bSize = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		button.setSize(bSize);
	}

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display, SWT.NO_TRIM);
		Color gray = new Color (222, 223, 224);
		Composite composite = new Composite(shell, SWT.NONE);
		RowLayout layout = new RowLayout();
		layout.marginTop = 0;
		layout.marginLeft = 0;
		layout.marginBottom = 0;
		layout.marginRight = 0;
		layout.spacing = 0;
		composite.setLayout(layout);
		createButton(composite, gray, true, true);
		createButton(composite, gray, false, true);
		createButton(composite, gray, true, false);
		createButton(composite, gray, false, false);

		Point cSize = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		composite.setSize(cSize);
		shell.setBackground(gray);
		shell.setLocation(0, 0);
		shell.setSize(cSize);

		shell.open();
		Image canvas = new Image(display, cSize.x, cSize.y);
		GC gc = new GC(canvas);
		composite.print(gc);

		int buttonX = cSize.x / 4;
		Image[] images = new Image[4];

		for (int i = 0; i < 4; i++) {
			Image image = new Image(display, buttonX, cSize.y);
			gc.copyArea(image, buttonX * i, 0);
			images[i] = image;
		}
		shell.close();
		Shell properShell = new Shell (display);
		properShell.setLayout(new FillLayout());
		Image firstImage = images[0];
		final Tree tree = new Tree(properShell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		TreeItem lastItem = new TreeItem(tree, SWT.NONE);
		lastItem.setImage(firstImage);
		lastItem.setText("root item with first image");

		for (int i = 0; i < 3; i++) {
			TreeItem newItem = new TreeItem(lastItem, SWT.NONE);
			newItem.setText("descendant item with image " + i);
			newItem.setImage(images[i+1]);
		}
		lastItem.setExpanded(true);

		properShell.setSize(400, 400);
		properShell.open();
		while (!properShell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
		canvas.dispose();
		gc.dispose();
		firstImage.dispose();
	}

}