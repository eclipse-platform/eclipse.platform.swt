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
	Button wrap, pack, justify;
	Combo marginRight, marginLeft, marginTop, marginBottom, spacing;
	/* The example layout instance */
	RowLayout rowLayout;
	/* TableEditors and related controls*/
	TableEditor comboEditor, widthEditor, heightEditor;
	CCombo combo;
	Text widthText, heightText;
	
	/* Constants */
	final int COMBO_COL = 1;
	final int WIDTH_COL = 2;
	final int HEIGHT_COL = 3;
	
	final int TOTAL_COLS = 4;
	
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
		comboEditor = new TableEditor (table);
		widthEditor = new TableEditor (table);
		heightEditor = new TableEditor (table);
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
				
				widthText = new Text (table, SWT.SINGLE);
				widthText.setText (((String [])data.elementAt (index)) [WIDTH_COL]);
				createTextEditor (widthText, widthEditor, WIDTH_COL);
				
				heightText = new Text (table, SWT.SINGLE);
				heightText.setText (((String [])data.elementAt (index)) [HEIGHT_COL]);
				createTextEditor (heightText, heightEditor, HEIGHT_COL);
                
                for (int i=0; i<table.getColumnCount (); i++) {
                	Rectangle rect = newItem.getBounds (i);
                    if (rect.contains (pt)) {
                    	switch (i) {
							case COMBO_COL :
								combo.setFocus ();	
								break;
							case WIDTH_COL :	
								widthText.setFocus ();
								break;
							case HEIGHT_COL :
								heightText.setFocus ();
								break;
							default :
								resetEditors ();
								break;
						}
                    }
                } 
			}
		});
		
		/* Add listener to add an element to the table */
		add.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				TableItem item = new TableItem (table, 0);
				String [] insert = new String [] { 
					String.valueOf (table.indexOf (item)),
					"Button", "-1", "-1"};
				item.setText (insert);
				data.addElement (insert);
				resetEditors ();
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
		GridData data = new GridData (GridData.FILL_HORIZONTAL);
		typeGroup.setLayoutData (data);
		horizontal = new Button (typeGroup, SWT.RADIO);
		horizontal.setText ("SWT.HORIZONTAL");
		horizontal.setLayoutData (new GridData (GridData.FILL_HORIZONTAL)); 
		horizontal.setSelection(true);
		horizontal.addSelectionListener (selectionListener);
		vertical = new Button (typeGroup, SWT.RADIO);
		vertical.setText ("SWT.VERTICAL");
		vertical.setLayoutData (new GridData (GridData.FILL_HORIZONTAL)); 
		vertical.addSelectionListener (selectionListener);
		
		/* Controls the margins and spacing of the RowLayout */
		String [] marginValues = new String [] {"0","3","5","10"};
		Group marginGroup = new Group (controlGroup, SWT.NONE);
		marginGroup.setText (LayoutExample.getResourceString ("Margins_Spacing"));
		data = new GridData (GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		data.verticalSpan = 2;
		marginGroup.setLayoutData (data);
		GridLayout layout = new GridLayout ();
		layout.numColumns = 2;
		marginGroup.setLayout (layout);
		new Label (marginGroup, SWT.NONE).setText ("marginRight");
		marginRight = new Combo (marginGroup, SWT.NONE);
		marginRight.setItems (marginValues);
		marginRight.select (1);
		marginRight.setLayoutData (new GridData(GridData.FILL_HORIZONTAL));
		marginRight.addSelectionListener (selectionListener);
		marginRight.addTraverseListener (traverseListener);
		new Label (marginGroup, SWT.NONE).setText ("marginLeft");
		marginLeft = new Combo (marginGroup, SWT.NONE);
		marginLeft.setItems (marginValues);
		marginLeft.select (1);
		marginLeft.setLayoutData (new GridData(GridData.FILL_HORIZONTAL));
		marginLeft.addSelectionListener (selectionListener);
		marginLeft.addTraverseListener(traverseListener);
		new Label (marginGroup, SWT.NONE).setText ("marginTop");
		marginTop = new Combo (marginGroup, SWT.NONE);
		marginTop.setItems (marginValues);
		marginTop.select (1);
		marginTop.setLayoutData (new GridData(GridData.FILL_HORIZONTAL));
		marginTop.addSelectionListener (selectionListener);
		marginTop.addTraverseListener(traverseListener);
		new Label (marginGroup, SWT.NONE).setText ("marginBottom");
		marginBottom = new Combo (marginGroup, SWT.NONE);
		marginBottom.setItems (marginValues);
		marginBottom.select (1);
		marginBottom.setLayoutData (new GridData(GridData.FILL_HORIZONTAL));
		marginBottom.addSelectionListener (selectionListener);
		marginBottom.addTraverseListener(traverseListener);
		new Label (marginGroup, SWT.NONE).setText ("spacing");
		spacing = new Combo (marginGroup, SWT.NONE);
		spacing.setItems (marginValues);
		spacing.select (1);
		spacing.setLayoutData (new GridData(GridData.FILL_HORIZONTAL));
		spacing.addSelectionListener (selectionListener);
		spacing.addTraverseListener(traverseListener);
		
		/* Controls other parameters of the RowLayout */
		Group specGroup = new Group (controlGroup, SWT.NONE);
		specGroup.setText (LayoutExample.getResourceString ("Properties"));
		specGroup.setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
		specGroup.setLayout (new GridLayout ());
		wrap = new Button (specGroup, SWT.CHECK);
		wrap.setText ("wrap");
		wrap.setSelection (true);
		wrap.setLayoutData (new GridData (GridData.FILL_HORIZONTAL)); 
		wrap.addSelectionListener (selectionListener);
		pack = new Button (specGroup, SWT.CHECK);
		pack.setText ("pack");
		pack.setLayoutData (new GridData (GridData.FILL_HORIZONTAL)); 
		pack.setSelection (true);
		pack.addSelectionListener (selectionListener);
		justify = new Button (specGroup, SWT.CHECK);
		justify.setText ("justify");
		justify.setLayoutData (new GridData (GridData.FILL_HORIZONTAL)); 
		justify.addSelectionListener (selectionListener);
		
		/* Add common controls */
		super.createControlWidgets ();
		
		/* Position the sash */
		sash.setWeights (new int [] {6,5});
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
		if (rowLayout.justify == true) {
			code.append ("\t\trowLayout.justify = true;\n");
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
			RowData data = (RowData) control.getLayoutData ();
			if (data != null) {
				if (data.width != -1 || data.height != -1) {
					code.append ("\t\t");
					if (first) {
						code.append ("RowData ");
						first = false;
					}
					if (data.width == -1) {
						code.append ("data = new RowData (SWT.DEFAULT, " + data.height + ");\n");
					} else if (data.height == -1) {
						code.append ("data = new RowData (" + data.width + ", SWT.DEFAULT);\n");
					} else {
						code.append ("data = new RowData (" + data.width + ", " + data.height + ");\n");				
					}
					code.append ("\t\t" + names [i] + ".setLayoutData (data);\n");
				}
			}
		}
		return code;
	}
	
	/**
	 * Returns the layout data field names.
	 */
	String [] getLayoutDataFieldNames() {
		return new String [] { 
			"",
			"Control",
			"width", 
			"height"
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
	void resetEditors () {
		resetEditors (false);
	}
	
	void resetEditors (boolean tab) {
		TableItem oldItem = comboEditor.getItem ();
		if (oldItem != null) {
			int row = table.indexOf (oldItem);
			/* Make sure user has entered valid data */
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
				String.valueOf (row), combo.getText (), widthText.getText (), heightText.getText ()};
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
		for (int i = 0; i < children.length; i++) {
			width = new Integer (items [i].getText (WIDTH_COL)).intValue ();
			height = new Integer (items [i].getText (HEIGHT_COL)).intValue ();
			data = new RowData (width, height);
			children [i].setLayoutData (data);
		}
		
	}
	
	/**
	 * Sets the state of the layout.
	 */
	void setLayoutState () {
		/* Set the type of layout */
		if (vertical.getSelection ()) {
			rowLayout.type = SWT.VERTICAL;
		} else {
			rowLayout.type = SWT.HORIZONTAL;
		}
		
		/* Set the margins and spacing */
		try {
			rowLayout.marginRight = new Integer (marginRight.getText ()).intValue ();
		} catch (NumberFormatException e) {
			rowLayout.marginRight = 3;
			marginRight.select (1);
		}
		try {
			rowLayout.marginLeft = new Integer (marginLeft.getText ()).intValue ();
		} catch (NumberFormatException e) {
			rowLayout.marginLeft = 3;
			marginLeft.select (1);
		}
		try {
			rowLayout.marginTop = new Integer (marginTop.getText ()).intValue ();
		} catch (NumberFormatException e) {
			rowLayout.marginTop = 3;
			marginTop.select (1);
		}
		try {
			rowLayout.marginBottom = new Integer (marginBottom.getText ()).intValue ();
		} catch (NumberFormatException e) {
			rowLayout.marginBottom = 3;
			marginBottom.select (1);
		}
		try {
			rowLayout.spacing = new Integer (spacing.getText ()).intValue ();
		} catch (NumberFormatException e) {
			rowLayout.spacing = 3;
			spacing.select (1);
		}
		
		/* Set the other layout properties */
		rowLayout.wrap = wrap.getSelection ();
		rowLayout.pack = pack.getSelection ();
		rowLayout.justify = justify.getSelection ();
	}
}
