/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

class StackLayoutTab extends Tab {
	/* Controls for setting layout parameters */
	Button backButton, advanceButton;
	Label topControl;
	Spinner marginWidth, marginHeight;
	/* The example layout instance */
	StackLayout stackLayout;
	int currentLayer = -1;
	/* TableEditors and related controls*/
	TableEditor comboEditor, nameEditor;
	CCombo combo;
	Text nameText;
	final int NAME_COL = 0;
	final int TOTAL_COLS = 2;
	
	/**
	 * Creates the Tab within a given instance of LayoutExample.
	 */
	StackLayoutTab(LayoutExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the widgets in the "child" group.
	 */
	void createChildWidgets() {
		/* Add common controls */
		super.createChildWidgets();
		
		/* Add TableEditors */
		comboEditor = new TableEditor(table);
		nameEditor = new TableEditor(table);
		table.addMouseListener (new MouseAdapter() {
			public void mouseDown(MouseEvent e) { 	
				resetEditors();
				index = table.getSelectionIndex();
				if (index == -1) return;
				//set top layer of stack to the selected item
				setTopControl (index);
				
				TableItem oldItem = comboEditor.getItem();
				newItem = table.getItem(index);
				if (newItem == oldItem || newItem != lastSelected) {
					lastSelected = newItem;
					return;
				}
				table.showSelection();				
				combo = new CCombo(table, SWT.READ_ONLY);				
				createComboEditor(combo, comboEditor);
				
				nameText = new Text(table, SWT.SINGLE);
				nameText.setText(((String[])data.elementAt(index))[NAME_COL]);
				createTextEditor(nameText, nameEditor, NAME_COL);
			}
		});		
	}
	
	/**
	 * Creates the control widgets.
	 */
	void createControlWidgets() {
        /* Controls the topControl in the StackLayout */
		Group columnGroup = new Group (controlGroup, SWT.NONE);
		columnGroup.setText ("topControl");//(LayoutExample.getResourceString ("Top_Control"));
		columnGroup.setLayout(new GridLayout(3, false));
		columnGroup.setLayoutData(new GridData (SWT.FILL, SWT.FILL, false, false));
		backButton = new Button(columnGroup, SWT.PUSH);
	    backButton.setText("<<");
	    backButton.setEnabled(false);
		backButton.setLayoutData(new GridData (SWT.END, SWT.CENTER, false, false));
		backButton.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
		    	setTopControl (currentLayer - 1);
	        }
		});
		topControl = new Label (columnGroup, SWT.BORDER);
		topControl.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		advanceButton = new Button(columnGroup, SWT.PUSH);
		advanceButton.setText(">>");
		advanceButton.setEnabled(false);
		advanceButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
		    	setTopControl (currentLayer + 1);
	        }
		});		

		/* Controls the margins of the StackLayout */
		Group marginGroup = new Group(controlGroup, SWT.NONE);
		marginGroup.setText (LayoutExample.getResourceString("Margins"));
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
		
		/* Add common controls */
		super.createControlWidgets();
	}
	
	/**
	 * Creates the example layout.
	 */
	void createLayout() {
		stackLayout = new StackLayout();
		layoutComposite.setLayout(stackLayout);
	}
	
	void createLayoutComposite() {
		layoutComposite = new Composite(layoutGroup, SWT.BORDER);
		layoutComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		createLayout();
	}
	
	/** 
	 * Disposes the editors without placing their contents
	 * into the table.
	 */
	void disposeEditors() {
		comboEditor.setEditor(null, null, -1);
		combo.dispose();
		nameText.dispose();
	}
	
	/**
	 * Generates code for the example layout.
	 */
	StringBuffer generateLayoutCode() {
		StringBuffer code = new StringBuffer();
		code.append("\t\tStackLayout stackLayout = new StackLayout ();\n");
		if (stackLayout.marginWidth != 0) {
			code.append("\t\tstackLayout.marginWidth = " + stackLayout.marginWidth + ";\n");
		}
		if (stackLayout.marginHeight != 0) {
			code.append("\t\tstackLayout.marginHeight = " + stackLayout.marginHeight + ";\n");
		}
		code.append("\t\tshell.setLayout (stackLayout);\n");
		for(int i = 0; i < children.length; i++) {
			Control control = children[i];
			code.append (getChildCode(control, i));
		}
		if (children.length > 0 && currentLayer != -1) {
			code.append("\n\t\tstackLayout.topControl = " + names[currentLayer] + ";\n");
		}
		return code;
	}
	
	boolean needsCustom() {
		return true;
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
	String[] getLayoutDataFieldNames() {
		return new String[] {"Control Name", "Control Type"};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText() {
		return "StackLayout";
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
		setTopControl (currentLayer);
		layoutGroup.layout (true);
	}	

	void setTopControl (int index) {
		if (index == -1 || children.length == 0) {
			currentLayer = -1;
			topControl.setText ("");
		} else {
			currentLayer = index;
			stackLayout.topControl = children [currentLayer];
			layoutComposite.layout ();
			TableItem item = table.getItem(currentLayer);
			topControl.setText (item.getText(0));
		}
		backButton.setEnabled(children.length > 1 && currentLayer > 0);
		advanceButton.setEnabled(children.length > 1 && currentLayer < children.length - 1);
	}

	/**
	 * Sets the state of the layout.
	 */
	void setLayoutState() {
		/* Set the margins and spacing */
		stackLayout.marginWidth = marginWidth.getSelection();		
		stackLayout.marginHeight = marginHeight.getSelection();		
	}	
	
}

