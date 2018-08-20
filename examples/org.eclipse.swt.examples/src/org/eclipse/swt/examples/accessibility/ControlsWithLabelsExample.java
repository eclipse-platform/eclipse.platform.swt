/*******************************************************************************
 * Copyright (c) 2008, 2016 IBM Corporation and others.
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
package org.eclipse.swt.examples.accessibility;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

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
		shell.setLayout(new GridLayout(4, true));
		shell.setText("All Controls Test");

		new Label(shell, SWT.NONE).setText("Label for Label");
		label = new Label(shell, SWT.NONE);
		label.setText("Label");

		new Label(shell, SWT.NONE).setText("Label for CLabel");
		cLabel = new CLabel(shell, SWT.NONE);
		cLabel.setText("CLabel");

		new Label(shell, SWT.NONE).setText("Label for Push Button");
		buttonPush = new Button(shell, SWT.PUSH);
		buttonPush.setText("Push Button");

		new Label(shell, SWT.NONE).setText("Label for Radio Button");
		buttonRadio = new Button(shell, SWT.RADIO);
		buttonRadio.setText("Radio Button");

		new Label(shell, SWT.NONE).setText("Label for Check Button");
		buttonCheck = new Button(shell, SWT.CHECK);
		buttonCheck.setText("Check Button");

		new Label(shell, SWT.NONE).setText("Label for Toggle Button");
		buttonToggle = new Button(shell, SWT.TOGGLE);
		buttonToggle.setText("Toggle Button");

		new Label(shell, SWT.NONE).setText("Label for Editable Combo");
		combo = new Combo(shell, SWT.BORDER);
		for (int i = 0; i < 4; i++) {
			combo.add("item" + i);
		}
		combo.select(0);

		new Label(shell, SWT.NONE).setText("Label for Read-Only Combo");
		combo = new Combo(shell, SWT.READ_ONLY | SWT.BORDER);
		for (int i = 0; i < 4; i++) {
			combo.add("item" + i);
		}
		combo.select(0);

		new Label(shell, SWT.NONE).setText("Label for CCombo");
		cCombo = new CCombo(shell, SWT.BORDER);
		for (int i = 0; i < 5; i++) {
			cCombo.add("item" + i);
		}
		cCombo.select(0);

		new Label(shell, SWT.NONE).setText("Label for List");
		list = new List(shell, SWT.SINGLE | SWT.BORDER);
		list.setItems("Item0", "Item1", "Item2");

		new Label(shell, SWT.NONE).setText("Label for Spinner");
		spinner = new Spinner(shell, SWT.BORDER);

		new Label(shell, SWT.NONE).setText("Label for Single-line Text");
		textSingle = new Text(shell, SWT.SINGLE | SWT.BORDER);
		textSingle.setText("Contents of Single-line Text");

		new Label(shell, SWT.NONE).setText("Label for Multi-line Text");
		textMulti = new Text(shell, SWT.MULTI | SWT.BORDER);
		textMulti.setText("\nContents of Multi-line Text\n");

		new Label(shell, SWT.NONE).setText("Label for StyledText");
		styledText = new StyledText(shell, SWT.MULTI | SWT.BORDER);
		styledText.setText("\nContents of Multi-line StyledText\n");

		new Label(shell, SWT.NONE).setText("Label for Table");
		table = new Table(shell, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
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

		new Label(shell, SWT.NONE).setText("Label for Tree");
		tree = new Tree(shell, SWT.BORDER | SWT.MULTI);
		for (int i = 0; i < 3; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText("Item" + i);
			for (int j = 0; j < 4; j++) {
				new TreeItem(item, SWT.NONE).setText("Item" + i + j);
			}
		}

		new Label(shell, SWT.NONE).setText("Label for Tree with columns");
		treeTable = new Tree(shell, SWT.BORDER | SWT.MULTI);
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

		new Label(shell, SWT.NONE).setText("Label for ToolBar");
		toolBar = new ToolBar(shell, SWT.FLAT);
		for (int i = 0; i < 3; i++) {
			ToolItem item = new ToolItem(toolBar, SWT.PUSH);
			item.setText("Item" + i);
			item.setToolTipText("ToolItem ToolTip" + i);
		}

		new Label(shell, SWT.NONE).setText("Label for CoolBar");
		coolBar = new CoolBar(shell, SWT.FLAT);
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

		new Label(shell, SWT.NONE).setText("Label for Canvas");
		canvas = new Canvas(shell, SWT.BORDER);
		canvas.setLayoutData(new GridData(64, 64));
		canvas.addPaintListener(e -> e.gc.drawString("Canvas", 15, 25));
		canvas.setCaret (new Caret(canvas, SWT.NONE));
		/* Hook key listener so canvas will take focus during traversal in. */
		canvas.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
		/* Hook traverse listener to make canvas give up focus during traversal out. */
		canvas.addTraverseListener(e -> e.doit = true);

		new Label(shell, SWT.NONE).setText("Label for Group");
		group = new Group(shell, SWT.NONE);
		group.setText("Group");
		group.setLayout(new FillLayout());
		new Text(group, SWT.SINGLE | SWT.BORDER).setText("Text in Group");

		new Label(shell, SWT.NONE).setText("Label for TabFolder");
		tabFolder = new TabFolder(shell, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			TabItem item = new TabItem(tabFolder, SWT.NONE);
			item.setText("TabItem &" + i);
			item.setToolTipText("TabItem ToolTip" + i);
			Text itemText = new Text(tabFolder, SWT.SINGLE | SWT.BORDER);
			itemText.setText("Text for TabItem " + i);
			item.setControl(itemText);
		}

		new Label(shell, SWT.NONE).setText("Label for CTabFolder");
		cTabFolder = new CTabFolder(shell, SWT.BORDER);
		for (int i = 0; i < 3; i++) {
			CTabItem item = new CTabItem(cTabFolder, SWT.NONE);
			item.setText("CTabItem &" + i);
			item.setToolTipText("CTabItem ToolTip" + i);
			Text itemText = new Text(cTabFolder, SWT.SINGLE | SWT.BORDER);
			itemText.setText("Text for CTabItem " + i);
			item.setControl(itemText);
		}
		cTabFolder.setSelection(cTabFolder.getItem(0));

		new Label(shell, SWT.NONE).setText("Label for Scale");
		scale = new Scale(shell, SWT.NONE);

		new Label(shell, SWT.NONE).setText("Label for Slider");
		slider = new Slider(shell, SWT.NONE);

		new Label(shell, SWT.NONE).setText("Label for ProgressBar");
		progressBar = new ProgressBar(shell, SWT.NONE);
		progressBar.setSelection(50);

		new Label(shell, SWT.NONE).setText("Label for Sash");
		sash = new Sash(shell, SWT.NONE);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}