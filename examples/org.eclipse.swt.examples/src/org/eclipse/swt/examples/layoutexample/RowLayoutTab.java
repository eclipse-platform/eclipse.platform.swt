/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.layoutexample;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.custom.*;

class RowLayoutTab extends Tab {
	/* Controls for setting layout parameters */
	Button horizontal, vertical;
	Button wrap, pack, fill, justify, center;
	Spinner marginWidth, marginHeight, marginLeft, marginRight, marginTop, marginBottom, spacing;
	/* The example layout instance */
	RowLayout rowLayout;
	/* TableEditors and related controls*/
	TableEditor comboEditor, widthEditor, heightEditor, nameEditor, excludeEditor;
	CCombo combo, exclude;
	Text nameText, widthText, heightText;
	
	/* Constants */
	final int NAME_COL = 0;
	final int COMBO_COL = 1;
	final int WIDTH_COL = 2;
	final int HEIGHT_COL = 3;
	final int EXCLUDE_COL = 4;
	final int TOTAL_COLS = 5;
	
	/**
	 * Creates the Tab within a given instance of LayoutExample.
	 */
	RowLayoutTab(LayoutExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the widgets in the "child" group.
	 */
	void createChildWidgets () {
		/* Add common controls */
		super.createChildWidgets ();
		
		/* Add TableEditors */
		nameEditor = new TableEditor (table);
		comboEditor = new TableEditor (table);
		widthEditor = new TableEditor (table);
		heightEditor = new TableEditor (table);
		excludeEditor = new TableEditor (table);
		table.addMouseListener (new MouseAdapter () {
			public void mouseDown(MouseEvent e) {
				resetEditors();
				index = table.getSelectionIndex ();
				Point pt = new Point (e.x, e.y);
                newItem = table.getItem (pt);
                if (newItem == null) return;
                TableItem oldItem = comboEditor.getItem ();
                if (newItem == oldItem || newItem != lastSelected) {
					lastSelected = newItem;
					return;
				}
				table.showSelection ();
				
				combo = new CCombo (table, SWT.READ_ONLY);
				createComboEditor (combo, comboEditor);
				
				nameText = new Text(table, SWT.SINGLE);
				nameText.setText(((String[])data.elementAt(index))[NAME_COL]);
				createTextEditor(nameText, nameEditor, NAME_COL);
				
				widthText = new Text(table, SWT.SINGLE);
				widthText.setText(((String[])data.elementAt(index))[WIDTH_COL]);
				createTextEditor(widthText, widthEditor, WIDTH_COL);
				
				heightText = new Text(table, SWT.SINGLE);
				heightText.setText (((String [])data.elementAt (index)) [HEIGHT_COL]);
				createTextEditor (heightText, heightEditor, HEIGHT_COL);
                
				String [] boolValues = new String [] {"false", "true"};
				exclude = new CCombo (table, SWT.NONE);
				exclude.setItems (boolValues);
				exclude.setText (newItem.getText (EXCLUDE_COL));
				excludeEditor.horizontalAlignment = SWT.LEFT;
				excludeEditor.grabHorizontal = true;
				excludeEditor.minimumWidth = 50;
				excludeEditor.setEditor (exclude, newItem, EXCLUDE_COL);
				exclude.addTraverseListener (traverseListener);

				for (int i=0; i<table.getColumnCount (); i++) {
                	Rectangle rect = newItem.getBounds (i);
                    if (rect.contains (pt)) {
                    	switch (i) {
                    		case NAME_COL :
                    			nameText.setFocus ();
							case COMBO_COL :
								combo.setFocus ();	
								break;
							case WIDTH_COL :	
								widthText.setFocus ();
								break;
							case HEIGHT_COL :
								heightText.setFocus ();
								break;
							case EXCLUDE_COL :
								exclude.setFocus ();
								break;
							default :
								resetEditors ();
								break;
						}
                    }
                } 
			}
		});
	}
	
	/**
	 * Creates the control widgets.
	 */
	void createControlWidgets () {
		/* Controls the type of RowLayout */
		Group typeGroup = new Group (controlGroup, SWT.NONE);
		typeGroup.setText (LayoutExample.getResourceString ("Type"));
		typeGroup.setLayout (new GridLayout ());
		typeGroup.setLayoutData(new GridData (SWT.FILL, SWT.FILL, false, false));
		horizontal = new Button (typeGroup, SWT.RADIO);
		horizontal.setText ("SWT.HORIZONTAL");
		horizontal.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false)); 
		horizontal.setSelection(true);
		horizontal.addSelectionListener (selectionListener);
		vertical = new Button (typeGroup, SWT.RADIO);
		vertical.setText ("SWT.VERTICAL");
		vertical.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false)); 
		vertical.addSelectionListener (selectionListener);
		
		/* Controls the margins and spacing of the RowLayout */
		Group marginGroup = new Group (controlGroup, SWT.NONE);
		marginGroup.setText (LayoutExample.getResourceString ("Margins_Spacing"));
		marginGroup.setLayout(new GridLayout(2, false));
		marginGroup.setLayoutData(new GridData (SWT.FILL, SWT.FILL, false, false, 1, 2));
		new Label(marginGroup, SWT.NONE).setText("marginWidth");
		marginWidth = new Spinner(marginGroup, SWT.BORDER);
		marginWidth.setSelection(0);
		marginWidth.addSelectionListener(selectionListener);
		new Label (marginGroup, SWT.NONE).setText ("marginHeight");
		marginHeight = new Spinner(marginGroup, SWT.BORDER);
		marginHeight.setSelection(0);
		marginHeight.setLayoutData (new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		marginHeight.addSelectionListener (selectionListener);
		new Label (marginGroup, SWT.NONE).setText ("marginLeft");
		marginLeft = new Spinner(marginGroup, SWT.BORDER);
		marginLeft.setSelection(3);
		marginLeft.addSelectionListener (selectionListener);
		new Label (marginGroup, SWT.NONE).setText ("marginRight");
		marginRight = new Spinner(marginGroup, SWT.BORDER);
		marginRight.setSelection(3);
		marginRight.addSelectionListener(selectionListener);
		new Label(marginGroup, SWT.NONE).setText("marginTop");
		marginTop = new Spinner(marginGroup, SWT.BORDER);
		marginTop.setSelection(3);
		marginTop.addSelectionListener(selectionListener);
		new Label (marginGroup, SWT.NONE).setText ("marginBottom");
		marginBottom = new Spinner(marginGroup, SWT.BORDER);
		marginBottom.setSelection(3);
		marginBottom.addSelectionListener (selectionListener);
		new Label (marginGroup, SWT.NONE).setText ("spacing");
		spacing = new Spinner(marginGroup, SWT.BORDER);
		spacing.setSelection(3);
		spacing.addSelectionListener (selectionListener);
		
		/* Controls other parameters of the RowLayout */
		Group specGroup = new Group (controlGroup, SWT.NONE);
		specGroup.setText (LayoutExample.getResourceString ("Properties"));
		specGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, false, false));
		specGroup.setLayout (new GridLayout ());
		wrap = new Button (specGroup, SWT.CHECK);
		wrap.setText ("Wrap");
		wrap.setSelection (true);
		wrap.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false)); 
		wrap.addSelectionListener (selectionListener);
		pack = new Button (specGroup, SWT.CHECK);
		pack.setText ("Pack");
		pack.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false)); 
		pack.setSelection (true);
		pack.addSelectionListener(selectionListener);
		fill = new Button(specGroup, SWT.CHECK);
		fill.setText("Fill");
		fill.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false)); 
		fill.addSelectionListener(selectionListener);
		justify = new Button (specGroup, SWT.CHECK);
		justify.setText ("Justify");
		justify.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false)); 
		justify.addSelectionListener (selectionListener);
		center = new Button (specGroup, SWT.CHECK);
		center.setText ("Center");
		center.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false)); 
		center.addSelectionListener (selectionListener);
		
		/* Add common controls */
		super.createControlWidgets ();
	}
	
	/**
	 * Creates the example layout.
	 */
	void createLayout () {
		rowLayout = new RowLayout ();
		layoutComposite.setLayout (rowLayout);
	}
		
	/** 
	 * Disposes the editors without placing their contents
	 * into the table.
	 */
	void disposeEditors () {
		comboEditor.setEditor (null, null, -1);
		combo.dispose ();
		widthText.dispose ();
		heightText.dispose ();
		nameText.dispose ();
		exclude.dispose ();
	}
	
	/**
	 * Generates code for the example layout.
	 */	
	StringBuffer generateLayoutCode () {
		StringBuffer code = new StringBuffer ();
		code.append ("\t\tRowLayout rowLayout = new RowLayout ();\n");
		if (rowLayout.type == SWT.VERTICAL) {
			code.append ("\t\trowLayout.type = SWT.VERTICAL;\n");
		}
		if (rowLayout.wrap == false) {
			code.append ("\t\trowLayout.wrap = false;\n");
		} 
		if (rowLayout.pack == false) {
			code.append ("\t\trowLayout.pack = false;\n");
		}
		if (rowLayout.fill == true) {
			code.append("\t\trowLayout.fill = true;\n");
		}
		if (rowLayout.justify == true) {
			code.append ("\t\trowLayout.justify = true;\n");
		}
		if (rowLayout.center == true) {
			code.append ("\t\trowLayout.center = true;\n");
		}
		if (rowLayout.marginWidth != 0) {
			code.append("\t\trowLayout.marginWidth = " + rowLayout.marginWidth + ";\n");
		}
		if (rowLayout.marginHeight != 0) {
			code.append("\t\trowLayout.marginHeight = " + rowLayout.marginHeight + ";\n");
		}
		if (rowLayout.marginLeft != 3) {
			code.append ("\t\trowLayout.marginLeft = " + rowLayout.marginLeft + ";\n");
		}
		if (rowLayout.marginRight != 3) {
			code.append ("\t\trowLayout.marginRight = " + rowLayout.marginRight + ";\n");
		}
		if (rowLayout.marginTop != 3) {
			code.append ("\t\trowLayout.marginTop = " + rowLayout.marginTop + ";\n");
		}
		if (rowLayout.marginBottom != 3) {
			code.append ("\t\trowLayout.marginBottom = " + rowLayout.marginBottom + ";\n");
		}
		if (rowLayout.spacing != 3) {
			code.append ("\t\trowLayout.spacing = " + rowLayout.spacing + ";\n");
		}
		code.append ("\t\tshell.setLayout (rowLayout);\n");
		
		boolean first = true;
		for (int i = 0; i < children.length; i++) {
			Control control = children [i];
			code.append (getChildCode (control,i));
			RowData rowData = (RowData) control.getLayoutData ();
			if (rowData != null) {
				if (rowData.width != -1 || rowData.height != -1 || rowData.exclude) {
					code.append ("\t\t");
					if (first) {
						code.append ("RowData ");
						first = false;
					}
					if (rowData.width == -1 && rowData.height == -1) {
						code.append ("rowData = new RowData ();\n");
					} else if (rowData.width == -1) {
						code.append ("rowData = new RowData (SWT.DEFAULT, " + rowData.height + ");\n");
					} else if (rowData.height == -1) {
						code.append ("rowData = new RowData (" + rowData.width + ", SWT.DEFAULT);\n");
					} else {
						code.append ("rowData = new RowData (" + rowData.width + ", " + rowData.height + ");\n");				
					}
					if (rowData.exclude) {
						code.append ("\t\trowData.exclude = true;\n");
					}
					code.append ("\t\t" + names [i] + ".setLayoutData (rowData);\n");
				}
			}
		}
		return code;
	}
	
	/**
	 * Returns the string to insert when a new child control is added to the table.
	 */
	String[] getInsertString (String name, String controlType) {
		return new String [] {name, controlType, "-1", "-1", "false"};
	}

	/**
	 * Returns the layout data field names.
	 */
	String [] getLayoutDataFieldNames() {
		return new String [] { 
			"Control Name",
			"Control Type",
			"width", 
			"height",
			"exclude"
		};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "RowLayout";
	}
	
	/**
	 * Takes information from TableEditors and stores it.
	 */
	void resetEditors (boolean tab) {
		TableItem oldItem = comboEditor.getItem ();
		if (oldItem != null) {
			int row = table.indexOf (oldItem);
			/* Make sure user has entered valid data */
			try {				
				new String(nameText.getText());
			} catch (NumberFormatException e) {
				nameText.setText(oldItem.getText(NAME_COL));
			}
			try {
				new Integer (widthText.getText ()).intValue ();
			} catch (NumberFormatException e) {
				widthText.setText (oldItem.getText (WIDTH_COL));
			}
			try {
				new Integer (heightText.getText ()).intValue ();
			} catch (NumberFormatException e) {
				heightText.setText (oldItem.getText (HEIGHT_COL));
			}
			String [] insert = new String [] {
				nameText.getText(), combo.getText (), widthText.getText (), heightText.getText (), exclude.getText ()};
			data.setElementAt (insert, row);
			for (int i = 0 ; i < TOTAL_COLS; i++) {
				oldItem.setText (i, ((String [])data.elementAt (row)) [i]);
			}
			if (!tab) disposeEditors ();
		}
		setLayoutState ();
		refreshLayoutComposite ();
		setLayoutData ();
		layoutComposite.layout (true);
		layoutGroup.layout (true);
	}
	
	/**
	 * Sets the layout data for the children of the layout.
	 */
	void setLayoutData () {
		Control [] children = layoutComposite.getChildren ();
		TableItem [] items = table.getItems ();
		RowData data;
		int width, height;
		String exclude;
		for (int i = 0; i < children.length; i++) {
			width = new Integer (items [i].getText (WIDTH_COL)).intValue ();
			height = new Integer (items [i].getText (HEIGHT_COL)).intValue ();
			data = new RowData (width, height);
			exclude = items [i].getText (EXCLUDE_COL);
			data.exclude = exclude.equals ("true");
			children [i].setLayoutData (data);
		}
		
	}
	
	/**
	 * Sets the state of the layout.
	 */
	void setLayoutState () {
		/* Set the type of layout */
		rowLayout.type = vertical.getSelection () ? SWT.VERTICAL : SWT.HORIZONTAL;
		
		/* Set the margins and spacing */
		rowLayout.marginWidth = marginWidth.getSelection ();
		rowLayout.marginHeight = marginHeight.getSelection ();
		rowLayout.marginLeft = marginLeft.getSelection ();
		rowLayout.marginRight = marginRight.getSelection ();
		rowLayout.marginTop = marginTop.getSelection ();
		rowLayout.marginBottom = marginBottom.getSelection ();
		rowLayout.spacing = spacing.getSelection ();
		
		/* Set the other layout properties */
		rowLayout.wrap = wrap.getSelection ();
		rowLayout.pack = pack.getSelection ();
		rowLayout.fill = fill.getSelection ();
		rowLayout.justify = justify.getSelection ();
		rowLayout.center = center.getSelection ();
	}
}
