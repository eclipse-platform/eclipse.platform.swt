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
import org.eclipse.swt.accessibility.*;

public class ControlsWithAccessibleNamesExample {
	static Display display;
	static Shell shell;
	static Button button, overrideButton, imageButton, overrideImageButton;
	static Label label, overrideLabel, imageLabel, overrideImageLabel;
	static Combo combo, overrideCombo;
	static Spinner spinner, overrideSpinner;
	static Text text, overrideText;
	static Text multiLineText, overrideMultiLineText;
	static List list, overrideList;
	static Table table, overrideTable;
	static Tree tree, overrideTree;
	static Tree treeTable, overrideTreeTable;
	static ToolBar toolBar, overrideToolBar;
	static ToolBar imageToolBar, overrideImageToolBar;
	static CoolBar coolBar, overrideCoolBar;
	static Canvas canvas, overrideCanvas;
	static Composite composite, overrideComposite;
	static Group group, overrideGroup;
	static TabFolder tabFolder, overrideTabFolder;
	static CLabel cLabel, overrideCLabel;
	static CCombo cCombo, overrideCCombo;
	static CTabFolder cTabFolder, overrideCTabFolder;
	static StyledText styledText, overrideStyledText;
	static StyledText multiLineStyledText, overrideMultiLineStyledText;
	static ProgressBar progressBar, overrideProgressBar;
	static Sash sash, overrideSash;
	static Scale scale, overrideScale;
	static Slider slider, overrideSlider;
	static Link link, overrideLink;
	static Image smallImage, largeImage, transparentImage;
		
	public static void main(String[] args) {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new GridLayout(4, true));
		shell.setText("Override Accessibility Test");
		
		largeImage = new Image(display, ControlsWithAccessibleNamesExample.class.getResourceAsStream("run_wiz.gif"));
		smallImage = new Image(display, ControlsWithAccessibleNamesExample.class.getResourceAsStream("run.gif"));
		ImageData source = smallImage.getImageData();
		ImageData mask = source.getTransparencyMask();
		transparentImage = new Image(display, source, mask);
		
		new Label(shell, SWT.NONE).setText("Use Platform Name");
		new Label(shell, SWT.NONE).setText("Override Platform Name");
		new Label(shell, SWT.NONE).setText("Use Platform Name");
		new Label(shell, SWT.NONE).setText("Override Platform Name");
		
		AccessibleAdapter overrideAccessibleAdapter = new AccessibleAdapter() {
			public void getName(AccessibleEvent e) {
				Control control = ((Accessible) e.getSource()).getControl();
				if (e.childID == ACC.CHILDID_SELF) {
					e.result = "Overriding Platform Name For " + control.getData("name") + " (was " + e.result + ")";
				} else {
					e.result = "Overriding Platform Name For " + control.getData("child") + ": " + e.childID + " (was " + e.result + ")";
				}
			}
			public void getHelp(AccessibleEvent e) {
				Control control = ((Accessible) e.getSource()).getControl();
				if (e.childID == ACC.CHILDID_SELF) {
					e.result = "Overriding Platform Help For " + control.getData("name") + " (was " + e.result + ")";
				} else {
					e.result = "Overriding Platform Help For " + control.getData("child") + ": " + e.childID + " (was " + e.result + ")";
				}
			}
		};
		
//		Shell shell;
		shell.setData("name", "Shell");
		shell.getAccessible().addAccessibleListener(overrideAccessibleAdapter);

//		Label label, overrideLabel;
		label = new Label(shell, SWT.BORDER);
		label.setText("Label");
		label.setToolTipText("Label ToolTip");

		overrideLabel = new Label(shell, SWT.BORDER);
		overrideLabel.setText("Label");
		overrideLabel.setToolTipText("Label ToolTip");
		overrideLabel.setData("name", "Label");
		overrideLabel.getAccessible().addAccessibleListener(overrideAccessibleAdapter);

//		Label imageLabel, overrideImageLabel;
		imageLabel = new Label(shell, SWT.BORDER);
		imageLabel.setImage(largeImage);
		imageLabel.setToolTipText("Image Label ToolTip");

		overrideImageLabel = new Label(shell, SWT.BORDER);
		overrideImageLabel.setImage(largeImage);
		overrideImageLabel.setToolTipText("Image Label ToolTip");
		overrideImageLabel.setData("name", "Image Label");
		overrideImageLabel.getAccessible().addAccessibleListener(overrideAccessibleAdapter);

//		Button button, overrideButton;
		button = new Button(shell, SWT.PUSH);
		button.setText("Button");
		button.setToolTipText("Button ToolTip");

		overrideButton = new Button(shell, SWT.PUSH);
		overrideButton.setText("Button");
		overrideButton.setToolTipText("Button ToolTip");
		overrideButton.setData("name", "Button");
		overrideButton.getAccessible().addAccessibleListener(overrideAccessibleAdapter);

//		Button imageButton, overrideImageButton;
		imageButton = new Button(shell, SWT.PUSH);
		imageButton.setImage(smallImage);
		imageButton.setToolTipText("Image Button ToolTip");
		
		overrideImageButton = new Button(shell, SWT.PUSH);
		overrideImageButton.setImage(smallImage);
		overrideImageButton.setToolTipText("Image Button ToolTip");
		overrideImageButton.setData("name", "Image Button");
		overrideImageButton.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
		
//		Combo combo, overrideCombo;
		combo = new Combo(shell, SWT.BORDER);
		for (int i = 0; i < 5; i++) {
			combo.add("item" + i);
		}
		combo.setText("Combo");
		combo.setToolTipText("Combo ToolTip");

		overrideCombo = new Combo(shell, SWT.BORDER);
		for (int i = 0; i < 5; i++) {
			overrideCombo.add("item" + i);
		}
		overrideCombo.setText("Combo");
		overrideCombo.setToolTipText("Combo ToolTip");
		overrideCombo.setData("name", "Combo");
		overrideCombo.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
				
//		Spinner spinner, overrideSpinner;
		spinner = new Spinner(shell, SWT.BORDER);
		spinner.setSelection(5);
		spinner.setToolTipText("Spinner ToolTip");

		overrideSpinner = new Spinner(shell, SWT.BORDER);
		overrideSpinner.setSelection(5);
		overrideSpinner.setToolTipText("Spinner ToolTip");
		overrideSpinner.setData("name", "Spinner");
		overrideSpinner.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
				
//		Text text, overrideText;
		text = new Text(shell, SWT.SINGLE | SWT.BORDER);
		text.setText("Contents of single-line Text");
		
		overrideText = new Text(shell, SWT.SINGLE | SWT.BORDER);
		overrideText.setText("Contents of single-line Text");
		overrideText.setData("name", "Text");
		overrideText.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
		
//		Text multiLineText, overrideMultiLineText;
		multiLineText = new Text(shell, SWT.MULTI | SWT.BORDER);
		multiLineText.setText("Contents of multi-line Text\nLine 2\nLine 3\nLine 4");
		
		overrideMultiLineText = new Text(shell, SWT.MULTI | SWT.BORDER);
		overrideMultiLineText.setText("Contents of multi-line Text\nLine 2\nLine 3\nLine 4");
		overrideMultiLineText.setData("name", "MultiLineText");
		overrideMultiLineText.getAccessible().addAccessibleListener(overrideAccessibleAdapter);

//		List list, overrideList;
		list = new List(shell, SWT.SINGLE | SWT.BORDER);
		list.setItems(new String[] {"Item0", "Item1", "Item2"});
		
		overrideList = new List(shell, SWT.SINGLE | SWT.BORDER);
		overrideList.setItems(new String[] {"Item0", "Item1", "Item2"});
		overrideList.setData("name", "List");
		overrideList.setData("child", "List Item");
		overrideList.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
		
//		Table table, overrideTable;
		table = new Table(shell, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		for (int col = 0; col < 3; col++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText("Col " + col);
			column.pack();
		}
		for (int row = 0; row < 3; row++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(new String [] {"C0R" + row, "C1R" + row, "C2R" + row});
		}
		
		overrideTable = new Table(shell, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		overrideTable.setHeaderVisible(true);
		overrideTable.setLinesVisible(true);
		for (int col = 0; col < 3; col++) {
			TableColumn column = new TableColumn(overrideTable, SWT.NONE);
			column.setText("Col " + col);
			column.pack();
		}
		for (int row = 0; row < 3; row++) {
			TableItem item = new TableItem(overrideTable, SWT.NONE);
			item.setText(new String [] {"C0R" + row, "C1R" + row, "C2R" + row});
		}
		overrideTable.setData("name", "Table");
		overrideTable.setData("child", "Table Item");
		overrideTable.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
		
//		Tree tree, overrideTree;
		tree = new Tree(shell, SWT.BORDER | SWT.MULTI);
		for (int i = 0; i < 3; i++) {
			TreeItem item = new TreeItem(tree, SWT.NONE);
			item.setText("Item" + i);
			for (int j = 0; j < 4; j++) {
				new TreeItem(item, SWT.NONE).setText("Item" + i + j);
			}
		}
		
		overrideTree = new Tree(shell, SWT.BORDER | SWT.MULTI);
		for (int i = 0; i < 3; i++) {
			TreeItem item = new TreeItem(overrideTree, SWT.NONE);
			item.setText("Item" + i);
			for (int j = 0; j < 4; j++) {
				new TreeItem(item, SWT.NONE).setText("Item" + i + j);
			}
		}
		overrideTree.setData("name", "Tree");
		overrideTree.setData("child", "Tree Item");
		overrideTree.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
						
//		Tree treeTable, overrideTreeTable;
		treeTable = new Tree(shell, SWT.BORDER | SWT.MULTI);
		treeTable.setHeaderVisible(true);
		treeTable.setLinesVisible(true);
		for (int col = 0; col < 3; col++) {
			TreeColumn column = new TreeColumn(treeTable, SWT.NONE);
			column.setText("Col " + col);
			column.pack();
		}
		for (int i = 0; i < 3; i++) {
			TreeItem item = new TreeItem(treeTable, SWT.NONE);
			item.setText(new String [] {"I" + i + "C0", "I" + i + "C1", "I" + i + "C2"});
			for (int j = 0; j < 4; j++) {
				new TreeItem(item, SWT.NONE).setText(new String [] {"I" + i + j + "C0", "I" + i + j + "C1", "I" + i + j + "C2"});
			}
		}
		
		overrideTreeTable = new Tree(shell, SWT.BORDER | SWT.MULTI);
		overrideTreeTable.setHeaderVisible(true);
		overrideTreeTable.setLinesVisible(true);
		for (int col = 0; col < 3; col++) {
			TreeColumn column = new TreeColumn(overrideTreeTable, SWT.NONE);
			column.setText("Col " + col);
			column.pack();
		}
		for (int i = 0; i < 3; i++) {
			TreeItem item = new TreeItem(overrideTreeTable, SWT.NONE);
			item.setText(new String [] {"I" + i + "C0", "I" + i + "C1", "I" + i + "C2"});
			for (int j = 0; j < 4; j++) {
				new TreeItem(item, SWT.NONE).setText(new String [] {"I" + i + j + "C0", "I" + i + j + "C1", "I" + i + j + "C2"});
			}
		}
		overrideTreeTable.setData("name", "Tree Table");
		overrideTreeTable.setData("child", "Tree Table Item");
		overrideTreeTable.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
						
//		ToolBar toolBar, overrideToolBar;
		toolBar = new ToolBar(shell, SWT.FLAT);
		for (int i = 0; i < 3; i++) {
			ToolItem item = new ToolItem(toolBar, SWT.PUSH);
			item.setText("Item" + i);
			item.setToolTipText("ToolItem ToolTip" + i);
		}
		
		overrideToolBar = new ToolBar(shell, SWT.FLAT);
		for (int i = 0; i < 3; i++) {
			ToolItem item = new ToolItem(overrideToolBar, SWT.PUSH);
			item.setText("Item" + i);
			item.setToolTipText("ToolItem ToolTip" + i);
		}
		overrideToolBar.setData("name", "ToolBar");
		overrideToolBar.setData("child", "ToolBar Item");
		overrideToolBar.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
		
//		ToolBar imageToolBar, overrideImageToolBar;
		imageToolBar = new ToolBar(shell, SWT.FLAT);
		for (int i = 0; i < 3; i++) {
			ToolItem item = new ToolItem(imageToolBar, SWT.PUSH);
			item.setImage(transparentImage);
			item.setToolTipText("Image ToolItem ToolTip" + i);
		}
		
		overrideImageToolBar = new ToolBar(shell, SWT.FLAT);
		for (int i = 0; i < 3; i++) {
			ToolItem item = new ToolItem(overrideImageToolBar, SWT.PUSH);
			item.setImage(transparentImage);
			item.setToolTipText("Image ToolItem ToolTip" + i);
		}
		overrideImageToolBar.setData("name", "Image ToolBar");
		overrideImageToolBar.setData("child", "Image ToolBar Item");
		overrideImageToolBar.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
		
//		CoolBar coolBar, overrideCoolBar;
		coolBar = new CoolBar(shell, SWT.FLAT);
		for (int i = 0; i < 2; i++) {
			CoolItem coolItem = new CoolItem(coolBar, SWT.PUSH);
			ToolBar coolItemToolBar = new ToolBar(coolBar, SWT.FLAT);
			int toolItemWidth = 0;
			for (int j = 0; j < 2; j++) {
				ToolItem item = new ToolItem(coolItemToolBar, SWT.PUSH);
				item.setText("I" + i + j);
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
		
		overrideCoolBar = new CoolBar(shell, SWT.FLAT);
		for (int i = 0; i < 2; i++) {
			CoolItem coolItem = new CoolItem(overrideCoolBar, SWT.PUSH);
			ToolBar coolItemToolBar = new ToolBar(overrideCoolBar, SWT.FLAT);
			int toolItemWidth = 0;
			for (int j = 0; j < 2; j++) {
				ToolItem item = new ToolItem(coolItemToolBar, SWT.PUSH);
				item.setText("I" + i + j);
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
		overrideCoolBar.setData("name", "CoolBar");
		overrideCoolBar.setData("child", "CoolBar Item");
		overrideCoolBar.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
				
//		Canvas canvas, overrideCanvas;
		canvas = new Canvas(shell, SWT.BORDER);
		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawString("Canvas", 15, 25);
			}
		});
		/* Set a caret into the canvas so that it will take focus. */
		Caret caret = new Caret(canvas, SWT.NONE);
		caret.setBounds(15, 25, 2, 20);
		canvas.setCaret (caret);
		/* Hook key listener so canvas will take focus during traversal in. */
		canvas.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				e.doit = true;
			}
			public void keyReleased(KeyEvent e) {
				e.doit = true;
			}
		});
		/* Hook traverse listener to make canvas give up focus during traversal out. */
		canvas.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				e.doit = true;
			}
		});

		overrideCanvas = new Canvas(shell, SWT.BORDER);
		overrideCanvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawString("Canvas", 15, 25);
			}
		});
		/* Set a caret into the canvas so that it will take focus. */
		caret = new Caret(overrideCanvas, SWT.NONE);
		caret.setBounds(15, 25, 2, 20);
		overrideCanvas.setCaret (caret);
		/* Hook key listener so canvas will take focus during traversal in. */
		overrideCanvas.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				e.doit = true;
			}
			public void keyReleased(KeyEvent e) {
				e.doit = true;
			}
		});
		/* Hook traverse listener to make canvas give up focus during traversal out. */
		overrideCanvas.addTraverseListener(new TraverseListener() {
			public void keyTraversed(TraverseEvent e) {
				e.doit = true;
			}
		});
		overrideCanvas.setData("name", "Canvas");
		overrideCanvas.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
		
//		Composite composite, overrideComposite;
		composite = new Composite(shell, SWT.BORDER);
		composite.setLayout(new GridLayout());
		new Button(composite, SWT.RADIO).setText("Child 1");
		new Button(composite, SWT.RADIO).setText("Child 2");

		overrideComposite = new Composite(shell, SWT.BORDER);
		overrideComposite.setLayout(new GridLayout());
		new Button(overrideComposite, SWT.RADIO).setText("Child 1");
		new Button(overrideComposite, SWT.RADIO).setText("Child 2");
		overrideComposite.setData("name", "Composite");
		overrideComposite.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
		
//		Group group, overrideGroup;
		group = new Group(shell, SWT.NONE);
		group.setText("Group");
		group.setLayout(new FillLayout());
		new Text(group, SWT.SINGLE).setText("Text in Group");
		
		overrideGroup = new Group(shell, SWT.NONE);
		overrideGroup.setText("Group");
		overrideGroup.setLayout(new FillLayout());
		new Text(overrideGroup, SWT.SINGLE).setText("Text in Group");
		overrideGroup.setData("name", "Group");
		overrideGroup.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
		
//		TabFolder tabFolder, overrideTabFolder;
		tabFolder = new TabFolder(shell, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			TabItem item = new TabItem(tabFolder, SWT.NONE);
			item.setText("TabItem &" + i);
			item.setToolTipText("TabItem ToolTip" + i);
			Text itemText = new Text(tabFolder, SWT.MULTI | SWT.BORDER);
			itemText.setText("\nText for TabItem " + i + "\n\n");
			item.setControl(itemText);
		}
		
		overrideTabFolder = new TabFolder(shell, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			TabItem item = new TabItem(overrideTabFolder, SWT.NONE);
			item.setText("TabItem &" + i);
			item.setToolTipText("TabItem ToolTip" + i);
			Text itemText = new Text(overrideTabFolder, SWT.MULTI | SWT.BORDER);
			itemText.setText("\nText for TabItem " + i + "\n\n");
			item.setControl(itemText);
		}
		overrideTabFolder.setData("name", "TabFolder");
		overrideTabFolder.setData("child", "TabItem");
		overrideTabFolder.getAccessible().addAccessibleListener(overrideAccessibleAdapter);

//		CLabel cLabel, overrideCLabel;
		cLabel = new CLabel(shell, SWT.BORDER);
		cLabel.setText("CLabel");
		cLabel.setToolTipText("CLabel ToolTip");
		cLabel.setLayoutData(new GridData(100, SWT.DEFAULT));

		overrideCLabel = new CLabel(shell, SWT.BORDER);
		overrideCLabel.setText("CLabel");
		overrideCLabel.setToolTipText("CLabel ToolTip");
		overrideCLabel.setLayoutData(new GridData(100, SWT.DEFAULT));
		overrideCLabel.setData("name", "CLabel");
		overrideCLabel.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
		
//		CCombo cCombo, overrideCCombo;
		cCombo = new CCombo(shell, SWT.BORDER);
		for (int i = 0; i < 5; i++) {
			cCombo.add("item" + i);
		}
		cCombo.setText("CCombo");
		cCombo.setToolTipText("CCombo ToolTip");

		// Note: This doesn't work well because CCombo has Control children
		overrideCCombo = new CCombo(shell, SWT.BORDER);
		for (int i = 0; i < 5; i++) {
			overrideCCombo.add("item" + i);
		}
		overrideCCombo.setText("CCombo");
		overrideCCombo.setToolTipText("CCombo ToolTip");
		overrideCCombo.setData("name", "CCombo");
		overrideCCombo.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
				
//		CTabFolder cTabFolder, overrideCTabFolder;
		cTabFolder = new CTabFolder(shell, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			CTabItem item = new CTabItem(cTabFolder, SWT.NONE);
			item.setText("CTabItem &" + i);
			item.setToolTipText("TabItem ToolTip" + i);
			Text itemText = new Text(cTabFolder, SWT.MULTI | SWT.BORDER);
			itemText.setText("\nText for CTabItem " + i + "\n\n");
			item.setControl(itemText);
		}
		cTabFolder.setSelection(cTabFolder.getItem(0));
		
		overrideCTabFolder = new CTabFolder(shell, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			CTabItem item = new CTabItem(overrideCTabFolder, SWT.NONE);
			item.setText("CTabItem &" + i);
			item.setToolTipText("TabItem ToolTip" + i);
			Text itemText = new Text(overrideCTabFolder, SWT.MULTI | SWT.BORDER);
			itemText.setText("\nText for CTabItem " + i + "\n\n");
			item.setControl(itemText);
		}
		overrideCTabFolder.setSelection(overrideCTabFolder.getItem(0));
		overrideCTabFolder.setData("name", "CTabFolder");
		overrideCTabFolder.setData("child", "CTabItem");
		overrideCTabFolder.getAccessible().addAccessibleListener(overrideAccessibleAdapter);

//		StyledText styledText, overrideStyledText;
		styledText = new StyledText(shell, SWT.SINGLE | SWT.BORDER);
		styledText.setText("Contents of single-line StyledText");
		
		overrideStyledText = new StyledText(shell, SWT.SINGLE | SWT.BORDER);
		overrideStyledText.setText("Contents of single-line StyledText");
		overrideStyledText.setData("name", "StyledText");
		overrideStyledText.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
	
//		StyledText multiLineStyledText, overrideMultiLineStyledText;
		multiLineStyledText = new StyledText(shell, SWT.MULTI | SWT.BORDER);
		multiLineStyledText.setText("Contents of multi-line StyledText\nLine 2\nLine 3\nLine 4");
		
		overrideMultiLineStyledText = new StyledText(shell, SWT.MULTI | SWT.BORDER);
		overrideMultiLineStyledText.setText("Contents of multi-line StyledText\nLine 2\nLine 3\nLine 4");
		overrideMultiLineStyledText.setData("name", "MultiLineStyledText");
		overrideMultiLineStyledText.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
	
//		Scale scale, overrideScale;
		scale = new Scale(shell, SWT.NONE);
		scale.setToolTipText("Scale ToolTip");

		overrideScale = new Scale(shell, SWT.NONE);
		overrideScale.setToolTipText("Scale ToolTip");
		overrideScale.setData("name", "Scale");
		overrideScale.getAccessible().addAccessibleListener(overrideAccessibleAdapter);

//		Slider slider, overrideSlider;
		slider = new Slider(shell, SWT.NONE);
		slider.setToolTipText("Slider ToolTip");

		overrideSlider = new Slider(shell, SWT.NONE);
		overrideSlider.setToolTipText("Slider ToolTip");
		overrideSlider.setData("name", "Slider");
		overrideSlider.getAccessible().addAccessibleListener(overrideAccessibleAdapter);

//		ProgressBar progressBar, overrideProgressBar;
		if (!SWT.getPlatform().equals("cocoa")) {
			progressBar = new ProgressBar(shell, SWT.NONE);
			progressBar.setSelection(50);
			progressBar.setToolTipText("ProgressBar ToolTip");
	
			overrideProgressBar = new ProgressBar(shell, SWT.NONE);
			overrideProgressBar.setSelection(50);
			overrideProgressBar.setToolTipText("ProgressBar ToolTip");
			overrideProgressBar.setData("name", "ProgressBar");
			overrideProgressBar.getAccessible().addAccessibleListener(overrideAccessibleAdapter);
		}

//		Sash sash, overrideSash;
		sash = new Sash(shell, SWT.BORDER);
		sash.setToolTipText("Sash ToolTip");

		overrideSash = new Sash(shell, SWT.BORDER);
		overrideSash.setToolTipText("Sash ToolTip");
		overrideSash.setData("name", "Sash");
		overrideSash.getAccessible().addAccessibleListener(overrideAccessibleAdapter);

//		Link link, overrideLink;
		link = new Link(shell, SWT.NONE);
		link.setText("<a>This is a link</a>");
		link.setToolTipText("Link ToolTip");

		overrideLink = new Link(shell, SWT.NONE);
		overrideLink.setText("<a>This is a link</a>");
		overrideLink.setToolTipText("Link ToolTip");
		overrideLink.setData("name", "Link");
		overrideLink.getAccessible().addAccessibleListener(overrideAccessibleAdapter);

		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		largeImage.dispose();
		smallImage.dispose();
		transparentImage.dispose();
		display.dispose();
	}
}