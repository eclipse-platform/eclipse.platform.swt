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
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class FillLayoutTab extends Tab {
	/* Controls for setting layout parameters */
	Button horizontal, vertical;
	Spinner marginWidth, marginHeight, spacing;
	/* The example layout instance */
	FillLayout fillLayout;
	/* TableEditors and related controls*/
	TableEditor comboEditor, nameEditor;
	CCombo combo;
	Text nameText;
	final int NAME_COL = 0;
	final int TOTAL_COLS = 2;
	
	/**
	 * Creates the Tab within a given instance of LayoutExample.
	 */
	FillLayoutTab(LayoutExample instance) {
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
		nameEditor = new TableEditor (table);
		table.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent e) {
				resetEditors ();
				index = table.getSelectionIndex ();
				if (index == -1) return;
				TableItem oldItem = comboEditor.getItem ();
				newItem = table.getItem (index);
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
			}
		});
	}
	
	/**
	 * Creates the control widgets.
	 */
	void createControlWidgets () {
		/* Controls the type of FillLayout */
		Group typeGroup = new Group (controlGroup, SWT.NONE);
		typeGroup.setText (LayoutExample.getResourceString ("Type"));
		typeGroup.setLayout (new GridLayout ());
		typeGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, false));
		horizontal = new Button (typeGroup, SWT.RADIO);
		horizontal.setText ("SWT.HORIZONTAL");
		horizontal.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, true, false));
		horizontal.setSelection(true);
		horizontal.addSelectionListener (selectionListener);
		vertical = new Button (typeGroup, SWT.RADIO);
		vertical.setText ("SWT.VERTICAL");
		vertical.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, true, false));
		vertical.addSelectionListener (selectionListener); 
		
		/* Controls the margins and spacing of the FillLayout */
		Group marginGroup = new Group(controlGroup, SWT.NONE);
		marginGroup.setText (LayoutExample.getResourceString("Margins_Spacing"));
		marginGroup.setLayout(new GridLayout(2, false));
		marginGroup.setLayoutData (new GridData(SWT.FILL, SWT.CENTER, false, false));
		new Label(marginGroup, SWT.NONE).setText("marginWidth");
		marginWidth = new Spinner(marginGroup, SWT.BORDER);
		marginWidth.setSelection(0);
		marginWidth.addSelectionListener(selectionListener);
		new Label(marginGroup, SWT.NONE).setText("marginHeight");
		marginHeight = new Spinner(marginGroup, SWT.BORDER);
		marginHeight.setSelection(0);
		marginHeight.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		marginHeight.addSelectionListener(selectionListener);
		new Label(marginGroup, SWT.NONE).setText ("spacing");
		spacing = new Spinner(marginGroup, SWT.BORDER);
		spacing.setSelection(0);
		spacing.addSelectionListener(selectionListener);
		
		/* Add common controls */
		super.createControlWidgets ();
	}
	
	/**
	 * Creates the example layout.
	 */
	void createLayout () {
		fillLayout = new FillLayout ();
		layoutComposite.setLayout (fillLayout);
	}
	
	/** 
	 * Disposes the editors without placing their contents
	 * into the table.
	 */
	void disposeEditors () {
		comboEditor.setEditor (null, null, -1);
		combo.dispose ();
		nameText.dispose();
	}

	
	/**
	 * Generates code for the example layout.
	 */
	StringBuffer generateLayoutCode () {
		StringBuffer code = new StringBuffer ();
		code.append ("\t\tFillLayout fillLayout = new FillLayout ();\n");
		if (fillLayout.type == SWT.VERTICAL) {
			code.append ("\t\tfillLayout.type = SWT.VERTICAL;\n");
		}
		if (fillLayout.marginWidth != 0) {
			code.append("\t\tfillLayout.marginWidth = " + fillLayout.marginWidth + ";\n");
		}
		if (fillLayout.marginHeight != 0) {
			code.append("\t\tfillLayout.marginHeight = " + fillLayout.marginHeight + ";\n");
		}
		if (fillLayout.spacing != 0) {
			code.append("\t\tfillLayout.spacing = " + fillLayout.spacing + ";\n");
		}
		code.append("\t\tshell.setLayout (fillLayout);\n");
		for(int i = 0; i < children.length; i++) {
			Control control = children[i];
			code.append(getChildCode(control, i));
		}
		return code;
	}
	
	/**
	 * Returns the string to insert when a new child control is added to the table.
	 */
	String[] getInsertString (String name, String controlType) {
		return new String [] {name, controlType};
	}

	/**
	 * Returns the layout data field names.
	 */
	String [] getLayoutDataFieldNames() {
		return new String [] { "Control Name", "Control Type" };
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "FillLayout";
	}
	
	/**
	 * Takes information from TableEditors and stores it.
	 */
	void resetEditors (boolean tab) {
		TableItem oldItem = comboEditor.getItem ();
		comboEditor.setEditor (null, null, -1);
		if (oldItem != null) {
			int row = table.indexOf (oldItem);
			try {				
				new String (nameText.getText ());
			} catch (NumberFormatException e) {
				nameText.setText (oldItem.getText (NAME_COL));
			}
			String [] insert = new String [] {nameText.getText (), combo.getText ()};
			data.setElementAt (insert, row);
			for (int i = 0 ; i < TOTAL_COLS; i++) {
				oldItem.setText (i, ((String [])data.elementAt (row)) [i]);
			}
			if (!tab) disposeEditors ();
		}
		setLayoutState ();
		refreshLayoutComposite ();
		layoutComposite.layout (true);
		layoutGroup.layout (true);
	}
	
	/**
	 * Sets the state of the layout.
	 */
	void setLayoutState () {
		if (vertical.getSelection()) {
			fillLayout.type = SWT.VERTICAL;
		} else {
			fillLayout.type = SWT.HORIZONTAL;
		}
		
		/* Set the margins and spacing */
		fillLayout.marginWidth = marginWidth.getSelection();		
		fillLayout.marginHeight = marginHeight.getSelection();		
		fillLayout.spacing = spacing.getSelection();
	}
}
