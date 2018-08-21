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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class Bug340752_ControlGetSize {
	static ExpandBar bar;
	static Image image;
	static MenuItem item;

	public static void main(String[] args) {
		Shell shell = new Shell();
		Display display = shell.getDisplay();
		shell.setLayout(new FillLayout());
		shell.setText("ExpandBar Example");

		Menu menubar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menubar);
		MenuItem fileItem = new MenuItem(menubar, SWT.CASCADE);
		fileItem.setText("&File");
		Menu submenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setMenu(submenu);
		item = new MenuItem(submenu, SWT.PUSH);
		item.setText("New ExpandItem");

		bar = new ExpandBar(shell, SWT.V_SCROLL);
		image = display.getSystemImage(SWT.ICON_QUESTION);

		// First item
		Composite composite = new Composite(bar, SWT.NONE);
		Menu popupmenu = new Menu(shell, SWT.POP_UP);
		MenuItem popupItem = new MenuItem(popupmenu, SWT.PUSH);
		popupItem.setText("Popup");
		composite.setMenu(popupmenu);

		GridLayout layout = new GridLayout(2, false);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Label label = new Label(composite, SWT.NONE);
		label.setImage(display.getSystemImage(SWT.ICON_ERROR));
		label = new Label(composite, SWT.NONE);
		label.setText("SWT.ICON_ERROR");
		label = new Label(composite, SWT.NONE);
		label.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
		label = new Label(composite, SWT.NONE);
		label.setText("SWT.ICON_INFORMATION");
		label = new Label(composite, SWT.NONE);
		label.setImage(display.getSystemImage(SWT.ICON_WARNING));
		label = new Label(composite, SWT.NONE);
		label.setText("SWT.ICON_WARNING");
		label = new Label(composite, SWT.NONE);
		label.setImage(display.getSystemImage(SWT.ICON_QUESTION));
		label = new Label(composite, SWT.NONE);
		label.setText("SWT.ICON_QUESTION");
		ExpandItem item1 = new ExpandItem(bar, SWT.NONE);
		item1.setText("What is your favorite icon");
		item1.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item1.setControl(composite);
		item1.setImage(image);

		// Second item
		composite = new Composite(bar, SWT.NONE);
		layout = new GridLayout(2, true);
		layout.marginLeft = layout.marginTop = layout.marginRight = layout.marginBottom = 10;
		layout.verticalSpacing = 10;
		composite.setLayout(layout);
		Button button1 = new Button(composite, SWT.PUSH);
		button1.setText("Button 1");
		Button button2 = new Button(composite, SWT.PUSH);
		button2.setText("Button 2");

		ExpandItem item0 = new ExpandItem(bar, SWT.NONE);
		item0.setText("What is your favorite button");
		item0.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item0.setControl(composite);
		item0.setImage(image);
		item0.setExpanded(false);

		item.addListener(SWT.Selection, e -> {
			ExpandItem item2 = new ExpandItem(bar, SWT.NONE);
			Composite composite1 = new Composite(bar, SWT.NONE);
			GridLayout layout1 = new GridLayout(2, false);
			composite1.setLayout(layout1);
			Label label1 = new Label(composite1, SWT.NONE);
			label1.setText("What is your name?");
			Composite pcomposite = new Composite(composite1, SWT.NONE);
			Text text = new Text(pcomposite, SWT.NONE);
			item2.setText("New Question");
			text.pack();
			composite1.pack();
			Point size = composite1.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			item2.setHeight(size.y);
			item2.setControl(composite1);
			item2.setImage(image);
			item2.setExpanded(true);
		});

		bar.setSpacing(8);
		shell.setSize(400, 550);
		shell.open();
		System.out.println("---------------BUTTON 1---------------");
		System.out.println("getSize: " + button1.getSize());
		System.out.println("getBounds: " + button1.getBounds());
		System.out.println("computeSize: " + button1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		System.out.println("getLocation: " + button1.getLocation());

		System.out.println("---------------LABEL---------------");
		System.out.println("getSize: " + label.getSize());
		System.out.println("getBounds: " + label.getBounds());
		System.out.println("computeSize: " + label.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		System.out.println("getLocation: " + label.getLocation());


		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		image.dispose();
		display.dispose();
	}

}