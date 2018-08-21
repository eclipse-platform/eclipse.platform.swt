/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Red Hat - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.tests.gtk.snippets;


/*
 * CoolBar example snippet: create a cool bar
 *
 * For a widget.list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class Bug302918_ToolItemSetDisabledImage {

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		CoolBar bar = new CoolBar(shell, SWT.BORDER);
		for (int i = 0; i < 2; i++) {
			CoolItem item = new CoolItem(bar, SWT.NONE);
			Button button = new Button(bar, SWT.PUSH);
			button.setText("Button " + i);
			Point size = button.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			item.setPreferredSize(item.computeSize(size.x, size.y));
			item.setControl(button);
		}
		Image nav = new Image (display, "/home/ericwill/git/SWT-snippets/src/widget.toolbar/next_nav.gif");
		Image nav_dis = new Image(display, "/home/ericwill/git/SWT-snippets/src/widget.toolbar/next_nav_dis.gif");

		ToolBar toolbar = new ToolBar(bar, SWT.NONE);
		ToolItem item1 = new ToolItem(toolbar, SWT.PUSH);
		item1.setImage(nav);
		item1.setDisabledImage(nav_dis);
		ToolItem item2 = new ToolItem(toolbar, SWT.PUSH);
		item2.setImage(nav_dis);
		item2.setDisabledImage(nav);
		ToolItem item3 = new ToolItem(toolbar, SWT.PUSH);
		item3.setImage(nav);
		item3.setDisabledImage(nav_dis);
		item3.setEnabled(false);
		ToolItem item4 = new ToolItem(toolbar, SWT.PUSH);
		item4.setImage(nav_dis);
		item4.setDisabledImage(nav);
		item4.setEnabled(false);
		toolbar.pack();
		
		CoolItem citem = new CoolItem(bar, SWT.NONE);
		Point tsize = toolbar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		citem.setPreferredSize(tsize.x+15, tsize.y);
		citem.setControl(toolbar);
		
		bar.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}
