/*******************************************************************************
 * Copyright (c) 2015, 2016 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;
import static org.eclipse.swt.events.SelectionListener.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * Transparent Background example snippet: Set transparent background.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
public class Snippet365 {
	static Image oldImage;
	static Image newImage;

	// Containers
	static Composite containerGroup;
	static Canvas canvas;
	static Composite composite;
	static Group group;
	static Sash sash;

	// Native
	static Composite nativeGroup;
	static Button buttonCheckBox;
	static ToolBar toolBar;
	static CoolBar coolBar;
	static Label label;
	static Link link;
	static Scale scale;
	static Button radio;
	static Button check;
	static Button push;

	// Custom
	static Composite customGroup;
	static CLabel cLabel;
	static StyledText styledText;
	static SashForm sashForm;
	static CTabFolder cTab;
	static CTabFolder gradientCTab;

	// Item
	static Composite itemGroup;
	static TabFolder tabFolder;
	static Table table;
	static Tree tree;
	static ExpandBar expandBar;

	// As Designed
	static Composite defaultBackgroundGroup;
	static Text text;
	static Combo combo;
	static ProgressBar progressBar;
	static DateTime dateTime;
	static Slider slider;
	static List list;
	static CCombo ccombo;

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setText("Snippet365 - Transparent Background");
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		layout.spacing = 20;
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		shell.setLayout(layout);
		// Standard color background for Shell
		// shell.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

		// Gradient background for Shell
		shell.addListener(SWT.Resize, event -> {
			Rectangle rect = shell.getClientArea();
			Image newImage = new Image(display, Math.max(1, rect.width), 1);
			GC gc = new GC(newImage);
			gc.setForeground(display.getSystemColor(SWT.COLOR_BLUE));
			gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
			gc.fillGradientRectangle(rect.x, rect.y, rect.width, 1, false);
			gc.dispose();
			shell.setBackgroundImage(newImage);
			if (oldImage != null)
				oldImage.dispose();
			oldImage = newImage;
		});

		// Transparent
		buttonCheckBox = new Button(shell, SWT.CHECK | SWT.None);
		buttonCheckBox.setText("SET TRANSPARENT");
		buttonCheckBox.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
		buttonCheckBox.setSelection(false);
		buttonCheckBox.addSelectionListener(widgetSelectedAdapter( e-> {
			boolean transparent = ((Button) e.getSource()).getSelection();
			if (transparent) {
				// ContainerGroup
				containerGroup.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				canvas.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				composite.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				tabFolder.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				for (TabItem item : tabFolder.getItems()) {
					item.getControl().setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				}

				// Native
				nativeGroup.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				toolBar.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				coolBar.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				label.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				link.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				scale.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				radio.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				check.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				group.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				sash.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				slider.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				list.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));

				// Custom
				customGroup.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				cLabel.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				cTab.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_TRANSPARENT));
				gradientCTab.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_TRANSPARENT));
				sashForm.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_TRANSPARENT));
				for (Control control : sashForm.getChildren()) {
					control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_TRANSPARENT));
				}
				// Default
				push.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				defaultBackgroundGroup.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				combo.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				progressBar.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				dateTime.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				ccombo.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				text.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				styledText.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				expandBar.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				table.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
				tree.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));

				// ItemGroup
				itemGroup.setBackground(display.getSystemColor(SWT.COLOR_TRANSPARENT));
			} else {
				// Native
				nativeGroup.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				toolBar.setBackground(null);
				coolBar.setBackground(null);
				label.setBackground(null);
				link.setBackground(null);
				scale.setBackground(null);
				RGB rgb = display.getSystemColor(SWT.COLOR_CYAN).getRGB();
				radio.setBackground(new Color(new RGBA(rgb.red, rgb.blue, rgb.green, 255)));
				check.setBackgroundImage(getBackgroundImage(display));
				group.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				sash.setBackground(display.getSystemColor(SWT.COLOR_DARK_CYAN));
				slider.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
				list.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
				text.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

				// ContainerGroup
				containerGroup.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				canvas.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				composite.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				tabFolder.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				for (TabItem item : tabFolder.getItems()) {
					item.getControl().setBackground(display.getSystemColor(SWT.COLOR_CYAN));
				}
				// Custom
				customGroup.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				cLabel.setBackground((Color) null);
				styledText.setBackground((Color) null);
				sashForm.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				for (Control control : sashForm.getChildren()) {
					control.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				}
				cTab.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

				gradientCTab.setBackground(
						new Color[] { display.getSystemColor(SWT.COLOR_RED),
								display.getSystemColor(SWT.COLOR_WHITE) }, new int[] { 90 }, true);

				// Default
				defaultBackgroundGroup.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				push.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
				combo.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
				ccombo.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
				dateTime.setBackground(null);
				progressBar.setBackground(null);
				expandBar.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
				table.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
				tree.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

				// ItemGroup
				itemGroup.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
			}

		}));

		// ContainerGroup
		containerGroup = new Composite(shell, SWT.NONE);
		containerGroup.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		containerGroup.setToolTipText("CONTAINER");
		layout = new RowLayout();
		layout.spacing = 20;
		containerGroup.setLayout(layout);

		// Native
		nativeGroup = new Composite(shell, SWT.NONE);
		nativeGroup.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		nativeGroup.setToolTipText("NATIVE");
		layout = new RowLayout();
		layout.spacing = 20;
		nativeGroup.setLayout(layout);

		// Custom
		customGroup = new Composite(shell, SWT.NONE);
		customGroup.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		customGroup.setToolTipText("CUSTOM");
		layout = new RowLayout();
		layout.spacing = 20;
		customGroup.setLayout(layout);

		// AsDesigned
		defaultBackgroundGroup = new Composite(shell, SWT.NONE);
		defaultBackgroundGroup.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		defaultBackgroundGroup.setToolTipText("Default Background");
		layout = new RowLayout();
		layout.spacing = 20;
		defaultBackgroundGroup.setLayout(layout);

		// ItemGroup
		itemGroup = new Composite(shell, SWT.NONE);
		itemGroup.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		itemGroup.setToolTipText("ITEM");
		layout = new RowLayout();
		layout.spacing = 20;
		itemGroup.setLayout(layout);

		// Label
		label = new Label(nativeGroup, SWT.NONE);
		label.setText("Label");

		// Radio button
		radio = new Button(nativeGroup, SWT.RADIO);
		radio.setText("Radio Button");
		radio.setSelection(true);
		radio.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

		// Checkbox button with image
		check = new Button(nativeGroup, SWT.CHECK);
		check.setText("CheckBox Image");
		check.setSelection(true);
		check.setBackgroundImage(getBackgroundImage(display));

		// Push Button
		push = new Button(defaultBackgroundGroup, SWT.PUSH);
		push.setText("Push Button");

		// Toolbar
		toolBar = new ToolBar(nativeGroup, SWT.FLAT);
		toolBar.pack();
		ToolItem item = new ToolItem(toolBar, SWT.PUSH);
		item.setText("ToolBar_Item");

		// Coolbar
		coolBar = new CoolBar(nativeGroup, SWT.BORDER);
		for (int i=0; i<2; i++) {
			CoolItem item2 = new CoolItem (coolBar, SWT.NONE);
			Button button = new Button (coolBar, SWT.PUSH);
			button.setText ("Button " + i);
			Point size = button.computeSize (SWT.DEFAULT, SWT.DEFAULT);
			item2.setPreferredSize (item2.computeSize (size.x, size.y));
			item2.setControl (button);
		}
		// Scale
		scale = new Scale(nativeGroup, SWT.None);
		scale.setMaximum(100);
		scale.setSelection(20);

		// Link
		link = new Link(nativeGroup, SWT.NONE);
		link.setText("<a>Sample link</a>");

		// List
		list = new List(nativeGroup, SWT.BORDER);
		list.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		list.add("List_one");
		list.add("List_two");
		list.add("List_three");
		list.add("List_four");

		// Canvas
		canvas = new Canvas (containerGroup, SWT.NONE);
		canvas.setToolTipText("Canvas");
		canvas.addPaintListener(e -> {
			GC gc = e.gc;
			gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
			gc.drawRectangle(e.x+1, e.y+1, e.width-2, e.height-2);
			gc.drawArc(2, 2, e.width-4, e.height-4, 0, 360);
		});

		// Composite
		composite = new Composite(containerGroup, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		composite.setToolTipText("Composite");

		// TabFolder
		tabFolder = new TabFolder(containerGroup, SWT.BORDER);
		tabFolder.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		for (int i=0; i < 2; i++) {
			TabItem tabItem = new TabItem (tabFolder, SWT.NONE);
			tabItem.setText ("TabItem " + i);
			Label label = new Label (tabFolder, SWT.PUSH);
			label.setText ("Page " + i);
			tabItem.setControl (label);
			tabItem.getControl().setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		}
		tabFolder.pack ();

		// Group
		group = new Group(containerGroup, SWT.NONE);
		group.setText("Group");

		// Sash
		sash = new Sash(containerGroup, SWT.HORIZONTAL | SWT.BORDER);
		sash.setBackground(display.getSystemColor(SWT.COLOR_DARK_CYAN));
		sash.setLayoutData(new RowData(100, 100));
		sash.setToolTipText("Sash");

		// SashForm
		sashForm = new SashForm(containerGroup, SWT.HORIZONTAL | SWT.BORDER);
		Label leftLabel = new Label(sashForm, SWT.NONE);
		leftLabel.setText("SashForm\nLeft\n...\n...\n...\n...\n...");
		Label rightLabel = new Label(sashForm, SWT.NONE);
		rightLabel.setText("SashForm\nRight\n...\n...\n...\n...\n...");

		// DateTime
		dateTime = new DateTime(defaultBackgroundGroup, SWT.NONE);
		dateTime.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

		// Text
		text = new Text(nativeGroup, SWT.BORDER);
		text.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		text.setText("text");

		// ProgressBar
		progressBar = new ProgressBar(defaultBackgroundGroup, SWT.NONE);
		progressBar.setMaximum(100);
		progressBar.setSelection(80);

		// Combo
		combo = new Combo(defaultBackgroundGroup, SWT.BORDER);
		combo.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		combo.add("combo");
		combo.setText("combo");

		// Slider
		slider = new Slider(nativeGroup, SWT.HORIZONTAL | SWT.BORDER);
		slider.setSelection(20);
		slider.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

		// CCombo
		ccombo = new CCombo(defaultBackgroundGroup, SWT.BORDER);
		ccombo.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		ccombo.add("ccombo");
		ccombo.setText("ccombo");

		// CLable
		cLabel = new CLabel(customGroup, SWT.NONE);
		cLabel.setText("CLabel");

		// Text
		styledText = new StyledText(customGroup, SWT.BORDER);
		styledText.setFont(new Font(display, "Tahoma", 18, SWT.BOLD | SWT.ITALIC));
		styledText.setForeground(display.getSystemColor(SWT.COLOR_DARK_BLUE));
		styledText.setText("Styled Text");
		styledText.append("\n");
		styledText.append("Example_string");
		styledText.append("\n");
		styledText.append("One_Two");
		styledText.append("\n");
		styledText.append("Two_Three");

		// CTabFolder
		cTab = new CTabFolder(containerGroup, SWT.BORDER);
		for (int i = 0; i < 2; i++) {
			CTabItem cTabItem = new CTabItem(cTab, SWT.CLOSE, i);
			cTabItem.setText("CTabItem " + (i + 1));
		}
		cTab.setSelection(0);

		// Gradient CTabFolder
		gradientCTab = new CTabFolder(customGroup, SWT.BORDER);
		gradientCTab.setBackground(
				new Color[] { display.getSystemColor(SWT.COLOR_WHITE), display.getSystemColor(SWT.COLOR_RED) },
				new int[] { 90 }, true);
		for (int i = 0; i < 2; i++) {
			CTabItem cTabItem = new CTabItem(gradientCTab, SWT.CLOSE, i);
			cTabItem.setText("CTabItem " + (i + 1));
		}
		gradientCTab.setSelection(0);

		// Table
		table = new Table(itemGroup, SWT.V_SCROLL);
		table.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		TableItem tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText("TableItem - One");
		tableItem = new TableItem(table, SWT.NONE);
		tableItem.setText("TableItem - Two");

		// Tree
		tree = new Tree(itemGroup, SWT.NONE);
		TreeItem treeItem = new TreeItem (tree, SWT.NONE);
		treeItem.setText("Parent");
		TreeItem childItem = new TreeItem (treeItem, SWT.NONE);
		childItem.setText("Child1");
		childItem = new TreeItem (treeItem, SWT.NONE);
		childItem.setText("Child2");
		treeItem.setExpanded(true);
		tree.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

		// ExpandBar
		expandBar = new ExpandBar (itemGroup, SWT.V_SCROLL);
		expandBar.setBackground(display.getSystemColor(SWT.COLOR_CYAN));
		for (int i = 1; i <= 2; i++) {
			ExpandItem item1 = new ExpandItem(expandBar, SWT.NONE, 0);
			item1.setText("Expand_Bar_Entry " + i);
			item1.setExpanded(true);
			item1.setHeight(20);
		}

		shell.open();
		shell.pack();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	private static Image getBackgroundImage(final Display display) {
		if (newImage == null) {
			Rectangle rect = new Rectangle(0, 0, 115, 5);
			newImage = new Image(display, Math.max(1, rect.width), 1);
			GC gc = new GC(newImage);
			gc.setForeground(display.getSystemColor(SWT.COLOR_WHITE));
			gc.setBackground(display.getSystemColor(SWT.COLOR_RED));
			gc.fillGradientRectangle(rect.x, rect.y, rect.width, 1, false);
			gc.dispose();
		}
		return newImage;
	}

}
