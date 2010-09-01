/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.accessibility;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.custom.*;

public class ControlsWithLabelsExample {
	static Display display;
	static Shell shell;
	static Label label;
	static Button buttonPush;
	static Button buttonRadio;
	static Button buttonCheck;
	static Button buttonToggle;
	static Combo combo;
	static CCombo cCombo;
	static List list;
	static Spinner spinner;
	static Text textSingle;
	static Text textMulti;
	static StyledText styledText;
	static Table table;
	static Tree tree;
	static Tree treeTable;
	static ToolBar toolBar;
	static CoolBar coolBar;
	static Canvas canvas;
	static Group group;
	static TabFolder tabFolder;
	static CTabFolder cTabFolder;
	static CLabel cLabel;
	static Scale scale;
	static Slider slider;
	static ProgressBar progressBar;
	static Sash sash;

	public static void main(String[] args) {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final ScrolledComposite sc1 = new ScrolledComposite(shell, SWT.V_SCROLL | SWT.BORDER);
		Composite comp = new Composite(sc1, SWT.NONE);
		sc1.setContent(comp);
		comp.setLayout(new GridLayout(2, true));
		shell.setText("All Controls Test");

		new Label(comp, SWT.NONE).setText("Label for Label");
		label = new Label(comp, SWT.NONE);
		label.setText("Label");

		new Label(comp, SWT.NONE).setText("Label for CLabel");
		cLabel = new CLabel(comp, SWT.NONE);
		cLabel.setText("CLabel");

		new Label(comp, SWT.NONE).setText("Label for Push Button");
		buttonPush = new Button(comp, SWT.PUSH);
		buttonPush.setText("Push Button");

		new Label(comp, SWT.NONE).setText("Label for Radio Button");
		buttonRadio = new Button(comp, SWT.RADIO);
		buttonRadio.setText("Radio Button");

		new Label(comp, SWT.NONE).setText("Label for Check Button");
		buttonCheck = new Button(comp, SWT.CHECK);
		buttonCheck.setText("Check Button");

		new Label(comp, SWT.NONE).setText("Label for Toggle Button");
		buttonToggle = new Button(comp, SWT.TOGGLE);
		buttonToggle.setText("Toggle Button");

		new Label(comp, SWT.NONE).setText("Label for Editable Combo");
		combo = new Combo(comp, SWT.BORDER);
		for (int i = 0; i < 4; i++) {
			combo.add("item" + i);
		}
		combo.setText("Combo");

		new Label(comp, SWT.NONE).setText("Label for Read-Only Combo");
		combo = new Combo(comp, SWT.READ_ONLY | SWT.BORDER);
		for (int i = 0; i < 4; i++) {
			combo.add("item" + i);
		}
		combo.setText("Combo");

		new Label(comp, SWT.NONE).setText("Label for CCombo");
		cCombo = new CCombo(comp, SWT.BORDER);
		for (int i = 0; i < 5; i++) {
			cCombo.add("item" + i);
		}
		cCombo.setText("CCombo");

		new Label(comp, SWT.NONE).setText("Label for List");
		list = new List(comp, SWT.SINGLE | SWT.BORDER);
		list.setItems(new String[] {"Item0", "Item1", "Item2"});

		new Label(comp, SWT.NONE).setText("Label for Spinner");
		spinner = new Spinner(comp, SWT.BORDER);

		new Label(comp, SWT.NONE).setText("Label for Single-line Text");
		textSingle = new Text(comp, SWT.SINGLE | SWT.BORDER);
		textSingle.setText("Contents of Single-line Text");

		new Label(comp, SWT.NONE).setText("Label for Multi-line Text");
		textMulti = new Text(comp, SWT.MULTI | SWT.BORDER);
		textMulti.setText("\nContents of Multi-line Text\n");

		new Label(comp, SWT.NONE).setText("Label for StyledText");
		styledText = new StyledText(comp, SWT.MULTI | SWT.BORDER);
		styledText.setText("\nContents of Multi-line StyledText\n");

		new Label(comp, SWT.NONE).setText("Label for Table");
		table = new Table(comp, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		for (int col = 0; col < 3; col++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("Col " + col);
			column.setWidth(50);
		}
		for (int row = 0; row < 3; row++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String [] {"C0R" + row, "C1R" + row, "C2R" + row});
		}

		new Label(comp, SWT.NONE).setText("Label for Tree");
		tree = new Tree(comp, SWT.BORDER | SWT.MULTI);
		for (int i = 0; i < 3; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText("Item" + i);
			for (int j = 0; j < 4; j++) {
				new TreeItem(item, SWT.NONE).setText("Item" + i + j);
			}
		}

		new Label(comp, SWT.NONE).setText("Label for Tree with columns");
		treeTable = new Tree(comp, SWT.BORDER | SWT.MULTI);
		treeTable.setHeaderVisible(true);
		treeTable.setLinesVisible(true);
		for (int col = 0; col < 3; col++) {
			TreeColumn column = new TreeColumn(treeTable, SWT.NONE);
			column.setText("Col " + col);
			column.setWidth(50);
		}
		for (int i = 0; i < 3; i++) {
			TreeItem item = new TreeItem(treeTable, SWT.NONE);
			item.setText(new String [] {"I" + i + "C0", "I" + i + "C1", "I" + i + "C2"});
			for (int j = 0; j < 4; j++) {
				new TreeItem(item, SWT.NONE).setText(new String [] {"I" + i + j + "C0", "I" + i + j + "C1", "I" + i + j + "C2"});
			}
		}

		new Label(comp, SWT.NONE).setText("Label for ToolBar");
		toolBar = new ToolBar(comp, SWT.FLAT);
		for (int i = 0; i < 3; i++) {
			ToolItem item = new ToolItem(toolBar, SWT.PUSH);
			item.setText("Item" + i);
			item.setToolTipText("ToolItem ToolTip" + i);
		}

		new Label(comp, SWT.NONE).setText("Label for CoolBar");
		coolBar = new CoolBar(comp, SWT.FLAT);
		for (int i = 0; i < 2; i++) {
			CoolItem coolItem = new CoolItem(coolBar, SWT.PUSH);
			ToolBar coolItemToolBar = new ToolBar(coolBar, SWT.FLAT);
			int toolItemWidth = 0;
			for (int j = 0; j < 2; j++) {
				ToolItem item = new ToolItem(coolItemToolBar, SWT.PUSH);
				item.setText("Item" + i + j);
				item.setToolTipText("ToolItem ToolTip" + i + j);
				if (item.getWidth() > toolItemWidth) toolItemWidth = item.getWidth();
			}
			coolItem.setControl(coolItemToolBar);
			Point size = coolItemToolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			Point coolSize = coolItem.computeSize (size.x, size.y);
			coolItem.setMinimumSize(toolItemWidth, coolSize.y);
			coolItem.setPreferredSize(coolSize);
			coolItem.setSize(coolSize);
		}

		new Label(comp, SWT.NONE).setText("Label for Canvas");
		canvas = new Canvas(comp, SWT.BORDER);
		canvas.setLayoutData(new GridData(64, 64));
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawString("Canvas", 15, 25);
			}
		});
		canvas.setCaret (new Caret(canvas, SWT.NONE));
		/* Hook key listener so canvas will take focus during traversal in. */
		canvas.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
			}
			public void keyReleased(KeyEvent e) {
			}
		});
		/* Hook traverse listener to make canvas give up focus during traversal out. */
		canvas.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				e.doit = true;
			}
		});

		new Label(comp, SWT.NONE).setText("Label for Group");
		group = new Group(comp, SWT.NONE);
		group.setText("Group");
		group.setLayout(new FillLayout());
		new Text(group, SWT.SINGLE | SWT.BORDER).setText("Text in Group");

		new Label(comp, SWT.NONE).setText("Label for TabFolder");
		tabFolder = new TabFolder(comp, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			TabItem item = new TabItem(tabFolder, SWT.NONE);
			item.setText("TabItem &" + i);
			item.setToolTipText("TabItem ToolTip" + i);
			Text itemText = new Text(tabFolder, SWT.SINGLE | SWT.BORDER);
			itemText.setText("Text for TabItem " + i);
			item.setControl(itemText);
		}

		new Label(comp, SWT.NONE).setText("Label for CTabFolder");
		cTabFolder = new CTabFolder(comp, SWT.BORDER);
		for (int i = 0; i < 3; i++) {
			CTabItem item = new CTabItem(cTabFolder, SWT.NONE);
			item.setText("CTabItem &" + i);
			item.setToolTipText("CTabItem ToolTip" + i);
			Text itemText = new Text(cTabFolder, SWT.SINGLE | SWT.BORDER);
			itemText.setText("Text for CTabItem " + i);
			item.setControl(itemText);
		}
		cTabFolder.setSelection(cTabFolder.getItem(0));

		new Label(comp, SWT.NONE).setText("Label for Scale");
		scale = new Scale(comp, SWT.NONE);

		new Label(comp, SWT.NONE).setText("Label for Slider");
		slider = new Slider(comp, SWT.NONE);

		new Label(comp, SWT.NONE).setText("Label for ProgressBar");
		progressBar = new ProgressBar(comp, SWT.NONE);
		progressBar.setSelection(50);

		new Label(comp, SWT.NONE).setText("Label for Sash");
		sash = new Sash(comp, SWT.NONE);

		comp.pack();
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}