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
import org.eclipse.swt.graphics.*;

class GridLayoutTab extends Tab {
	/* Controls for setting layout parameters */
	Text numColumns;
	Button makeColumnsEqualWidth;
	Combo marginHeight, marginWidth, horizontalSpacing, verticalSpacing;
	/* The example layout instance */
	GridLayout gridLayout;
	/* TableEditors and related controls*/
	TableEditor comboEditor, widthEditor, heightEditor;
	TableEditor vAlignEditor, hAlignEditor, hIndentEditor;
	TableEditor hSpanEditor, vSpanEditor, hGrabEditor, vGrabEditor;
	CCombo combo, vAlign, hAlign, hGrab, vGrab;
	Text widthText, heightText, hIndent, hSpan, vSpan;
	
	/* Constants */
	final int COMBO_COL = 1;
	final int WIDTH_COL = 2;
	final int HEIGHT_COL = 3;
	final int HALIGN_COL = 4;
	final int VALIGN_COL = 5;
	final int HINDENT_COL = 6;
	final int HSPAN_COL = 7;
	final int VSPAN_COL = 8;
	final int HGRAB_COL = 9;
	final int VGRAB_COL = 10;
	
	final int TOTAL_COLS = 11;
		
	/**
	 * Creates the Tab within a given instance of LayoutExample.
	 */
	GridLayoutTab(LayoutExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the widgets in the "child" group.
	 */
	void createChildWidgets () {
		/* Create the TraverseListener */
		final TraverseListener traverseListener = new TraverseListener () {
			public void keyTraversed (TraverseEvent e) {
				if (e.detail == SWT.TRAVERSE_RETURN || e.detail == SWT.TRAVERSE_TAB_NEXT)
					resetEditors ();
				if (e.detail == SWT.TRAVERSE_ESCAPE)
					disposeEditors ();
			}
		};
		
		/* Add common controls */
		super.createChildWidgets ();
			
		/* Add TableEditors */		
		comboEditor = new TableEditor (table);
		widthEditor = new TableEditor (table);
		heightEditor = new TableEditor (table);
		vAlignEditor = new TableEditor (table);
		hAlignEditor = new TableEditor (table);
		hIndentEditor = new TableEditor (table);
		hSpanEditor = new TableEditor (table);
		vSpanEditor = new TableEditor (table);
		hGrabEditor = new TableEditor (table);
		vGrabEditor = new TableEditor (table);
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
				String [] alignValues = new String [] {"BEGINNING","CENTER","END","FILL"};
				hAlign = new CCombo (table, SWT.NONE);
				hAlign.setItems (alignValues);
				hAlign.setText (newItem.getText (HALIGN_COL));
				hAlignEditor.horizontalAlignment = SWT.LEFT;
				hAlignEditor.grabHorizontal = true;
				hAlignEditor.minimumWidth = 50;
				hAlignEditor.setEditor (hAlign, newItem, HALIGN_COL);
				hAlign.addTraverseListener (traverseListener);
				
				vAlign = new CCombo (table, SWT.NONE);
				vAlign.setItems (alignValues);
				vAlign.setText (newItem.getText (VALIGN_COL));
				vAlignEditor.horizontalAlignment = SWT.LEFT;
				vAlignEditor.grabHorizontal = true;
				vAlignEditor.minimumWidth = 50;
				vAlignEditor.setEditor (vAlign, newItem, VALIGN_COL);
				vAlign.addTraverseListener (traverseListener);
				
				hIndent = new Text (table, SWT.SINGLE);
				hIndent.setText (((String [])data.elementAt (index)) [HINDENT_COL]);
				createTextEditor (hIndent, hIndentEditor, HINDENT_COL);
				
				hSpan = new Text (table, SWT.SINGLE);
				hSpan.setText (((String [])data.elementAt (index)) [HSPAN_COL]);
				createTextEditor (hSpan, hSpanEditor, HSPAN_COL);
				
				vSpan = new Text (table, SWT.SINGLE);
				vSpan.setText (((String [])data.elementAt (index)) [VSPAN_COL]);
				createTextEditor (vSpan, vSpanEditor, VSPAN_COL);
				
				String [] boolValues = new String [] {"false","true"};
				hGrab = new CCombo (table, SWT.NONE);
				hGrab.setItems (boolValues);
				hGrab.setText (newItem.getText (HGRAB_COL));
				hGrabEditor.horizontalAlignment = SWT.LEFT;
				hGrabEditor.grabHorizontal = true;
				hGrabEditor.minimumWidth = 50;
				hGrabEditor.setEditor (hGrab, newItem, HGRAB_COL);
				hGrab.addTraverseListener (traverseListener);
				
				vGrab = new CCombo (table, SWT.NONE);
				vGrab.setItems (boolValues);
				vGrab.setText (newItem.getText (VGRAB_COL));
				vGrabEditor.horizontalAlignment = SWT.LEFT;
				vGrabEditor.grabHorizontal = true;
				vGrabEditor.minimumWidth = 50;
				vGrabEditor.setEditor (vGrab, newItem, VGRAB_COL);
				vGrab.addTraverseListener (traverseListener);
                
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
							case HALIGN_COL :
								hAlign.setFocus ();
								break;
							case VALIGN_COL :
								vAlign.setFocus ();
								break;
							case HINDENT_COL :
								hIndent.setFocus ();
								break;
							case HSPAN_COL :
								hSpan.setFocus ();
								break;
							case VSPAN_COL :
								vSpan.setFocus ();
								break;
							case HGRAB_COL :
								hGrab.setFocus ();
								break;
							case VGRAB_COL :
								vGrab.setFocus ();
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
					String.valueOf (table.indexOf (item)), "Button",
					"-1","-1","BEGINNING","CENTER",
					"0","1","1","false","false"};
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
		/* Rearrange the layout of the control group */
		size.setLayoutData (new GridData ());
		
		/* Controls the margins and spacing of the GridLayout */
		String [] marginValues = new String [] {"0","3","5","10"};
		Group marginGroup = new Group (controlGroup, SWT.NONE);
		marginGroup.setText (LayoutExample.getResourceString ("Margins_Spacing"));
		GridData data = new GridData (GridData.FILL_HORIZONTAL);
		data.verticalSpan = 2;
		marginGroup.setLayoutData (data);
		GridLayout layout = new GridLayout ();
		layout.numColumns = 2;
		marginGroup.setLayout (layout);
		new Label (marginGroup, SWT.NONE).setText ("marginHeight");
		marginHeight = new Combo (marginGroup, SWT.NONE);
		marginHeight.setItems (marginValues);
		marginHeight.select (2);
		data = new GridData (GridData.FILL_HORIZONTAL);
		data.widthHint = 60;
		marginHeight.setLayoutData (data);
		marginHeight.addSelectionListener (selectionListener);
		marginHeight.addTraverseListener (traverseListener);
		new Label (marginGroup, SWT.NONE).setText ("marginWidth");
		marginWidth = new Combo (marginGroup, SWT.NONE);
		marginWidth.setItems (marginValues);
		marginWidth.select (2);
		data = new GridData (GridData.FILL_HORIZONTAL);
		data.widthHint = 60;
		marginWidth.setLayoutData (data);
		marginWidth.addSelectionListener (selectionListener);
		marginWidth.addTraverseListener (traverseListener);
		new Label (marginGroup, SWT.NONE).setText ("horizontalSpacing");
		horizontalSpacing = new Combo (marginGroup, SWT.NONE);
		horizontalSpacing.setItems (marginValues);
		horizontalSpacing.select (2);
		data = new GridData (GridData.FILL_HORIZONTAL);
		data.widthHint = 60;
		horizontalSpacing.setLayoutData (data);
		horizontalSpacing.addSelectionListener (selectionListener);
		horizontalSpacing.addTraverseListener (traverseListener);
		new Label (marginGroup, SWT.NONE).setText ("verticalSpacing");
		verticalSpacing = new Combo (marginGroup, SWT.NONE);
		verticalSpacing.setItems (marginValues);
		verticalSpacing.select (2);
		data = new GridData (GridData.FILL_HORIZONTAL);
		data.widthHint = 60;
		verticalSpacing.setLayoutData (data);
		verticalSpacing.addSelectionListener (selectionListener);
		verticalSpacing.addTraverseListener (traverseListener);
        
        /* Controls the columns in the GridLayout */
		Group columnGroup = new Group (controlGroup, SWT.NONE);
		columnGroup.setText (LayoutExample.getResourceString ("Columns"));
		layout = new GridLayout ();
		layout.numColumns = 2;
		columnGroup.setLayout (layout);
		data = new GridData (GridData.VERTICAL_ALIGN_FILL);
		columnGroup.setLayoutData (data);
		numColumns = new Text (columnGroup, SWT.BORDER);
		numColumns.setText ("1");
		numColumns.addSelectionListener (selectionListener);
		numColumns.addTraverseListener (traverseListener);
       	data = new GridData (GridData.FILL_HORIZONTAL);
       	data.widthHint = 15;
       	numColumns.setLayoutData (data);
       	new Label (columnGroup, SWT.NONE).setText ("numColumns");
		makeColumnsEqualWidth = new Button (columnGroup, SWT.CHECK);
		makeColumnsEqualWidth.setText ("makeColumnsEqualWidth");
		makeColumnsEqualWidth.addSelectionListener (selectionListener);
		data = new GridData (GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		data.horizontalIndent = 14;
		makeColumnsEqualWidth.setLayoutData (data);

		/* Add common controls */
		super.createControlWidgets ();
		controlGroup.pack();
	}
	
	/**
	 * Creates the example layout.
	 */
	void createLayout () {
		gridLayout = new GridLayout ();
		layoutComposite.setLayout (gridLayout);
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
		hAlign.dispose ();
		vAlign.dispose ();
		hIndent.dispose ();
		hSpan.dispose ();
		vSpan.dispose ();
		hGrab.dispose ();
		vGrab.dispose ();
	}
	
	/**
	 * Generates code for the example layout.
	 */	
	StringBuffer generateLayoutCode () {
		StringBuffer code = new StringBuffer ();
		code.append ("\t\tGridLayout gridLayout = new GridLayout ();\n");
		if (gridLayout.numColumns != 1) {
			code.append ("\t\tgridLayout.numColumns = " + gridLayout.numColumns + ";\n");
		}
		if (gridLayout.makeColumnsEqualWidth) {
			code.append ("\t\tgridLayout.makeColumnsEqualWidth = true;\n");
		}
		if (gridLayout.marginHeight != 5) {
			code.append ("\t\tgridLayout.marginHeight = " + gridLayout.marginHeight + ";\n");
		}
		if (gridLayout.marginWidth != 5) {
			code.append ("\t\tgridLayout.marginWidth = " + gridLayout.marginWidth + ";\n");
		}
		if (gridLayout.horizontalSpacing != 5) {
			code.append ("\t\tgridLayout.horizontalSpacing = " + gridLayout.horizontalSpacing + ";\n");
		}
		if (gridLayout.verticalSpacing != 5) {
			code.append ("\t\tgridLayout.verticalSpacing = " + gridLayout.verticalSpacing + ";\n");
		}
		code.append ("\t\tshell.setLayout (gridLayout);\n");
		
		boolean first = true;
		for (int i = 0; i < children.length; i++) {
			Control control = children [i];
			code.append (getChildCode (control, i));
			GridData data = (GridData) control.getLayoutData ();
			if (data != null) {
				code.append ("\t\t");
				if (first) {
					code.append ("GridData ");
					first = false;
				}
				code.append ("data = new GridData ();\n");	
				if (data.widthHint != SWT.DEFAULT) {
					code.append ("\t\tdata.widthHint = " + data.widthHint + ";\n");
				}
				if (data.heightHint != SWT.DEFAULT) {
					code.append ("\t\tdata.heightHint = " + data.heightHint + ";\n");
				}
				if (data.horizontalAlignment != GridData.BEGINNING) {
					String alignment;
					int hAlignment = data.horizontalAlignment;
					if (hAlignment == GridData.CENTER) alignment = "GridData.CENTER";
					else if (hAlignment == GridData.END) alignment = "GridData.END";
					else alignment = "GridData.FILL";
					code.append ("\t\tdata.horizontalAlignment = " + alignment + ";\n");
				}
				if (data.verticalAlignment != GridData.CENTER) {
					String alignment;
					int vAlignment = data.verticalAlignment;
					if (vAlignment == GridData.BEGINNING) alignment = "GridData.BEGINNING";
					else if (vAlignment == GridData.END) alignment = "GridData.END";
					else alignment = "GridData.FILL";
					code.append ("\t\tdata.verticalAlignment = " + alignment + ";\n");
				}	
				if (data.horizontalIndent != 0) {
					code.append ("\t\tdata.horizontalIndent = " + data.horizontalIndent + ";\n");
				}
				if (data.horizontalSpan != 1) {
					code.append ("\t\tdata.horizontalSpan = " + data.horizontalSpan + ";\n");
				}
				if (data.verticalSpan != 1) {
					code.append ("\t\tdata.verticalSpan = " + data.verticalSpan + ";\n");
				}
				if (data.grabExcessHorizontalSpace) {
					code.append ("\t\tdata.grabExcessHorizontalSpace = true;\n");
				}
				if (data.grabExcessVerticalSpace) {
					code.append ("\t\tdata.grabExcessVerticalSpace = true;\n");
				}
				if (code.substring (code.length () - 33).equals ("GridData data = new GridData ();\n")) {
					code.delete (code.length () - 33, code.length ());
					first = true;
				} else if (code.substring (code.length () - 24).equals ("data = new GridData ();\n")) { 
					code.delete (code.length () - 24, code.length ());
				} else {	
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
			"height", 
			"horizontalAlignment", 
			"verticalAlignment", 
			"horizontalIndent", 
			"horizontalSpan",
			"verticalSpan", 
			"grabExcessHorizontalSpace", 
			"grabExcessVerticalSpace"
		};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "GridLayout";
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
			try {
				new Integer (hIndent.getText ()).intValue ();
			} catch (NumberFormatException e) {
				hIndent.setText (oldItem.getText (HINDENT_COL));
			}
			try {
				new Integer (hSpan.getText ()).intValue ();
			} catch (NumberFormatException e) {
				hSpan.setText (oldItem.getText (HSPAN_COL));
			}
			try {
				new Integer (vSpan.getText ()).intValue ();
			} catch (NumberFormatException e) {
				vSpan.setText (oldItem.getText (VSPAN_COL));
			}
			String [] insert = new String [] {
				String.valueOf (row), combo.getText (), widthText.getText (), heightText.getText (),
				hAlign.getText (), vAlign.getText (), hIndent.getText (), 
				hSpan.getText (), vSpan.getText (), hGrab.getText (), vGrab.getText ()
			};
			data.setElementAt (insert, row);
			for (int i = 0; i < TOTAL_COLS; i++) {
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
		GridData data;
		int hIndent, hSpan, vSpan;
		String vAlign, hAlign, vGrab, hGrab;
		for (int i = 0; i < children.length; i++) {
			data = new GridData ();
			/* Set widthHint and heightHint */
			data.widthHint = new Integer (items [i].getText (WIDTH_COL)).intValue ();
			data.heightHint = new Integer (items [i].getText (HEIGHT_COL)).intValue ();
			/* Set vertical alignment and horizontal alignment */
			hAlign = items [i].getText (HALIGN_COL);
			if (hAlign.equals ("CENTER")) {
				data.horizontalAlignment = GridData.CENTER;
			} else if (hAlign.equals ("END")) {
				data.horizontalAlignment = GridData.END;
			} else if (hAlign.equals ("FILL")) {
				data.horizontalAlignment = GridData.FILL;
			} else {
				data.horizontalAlignment = GridData.BEGINNING;
			}
			vAlign = items [i].getText (VALIGN_COL);
			if (vAlign.equals ("BEGINNING")) {
				data.verticalAlignment = GridData.BEGINNING;
			} else if (vAlign.equals ("END")) {
				data.verticalAlignment = GridData.END;
			} else if (vAlign.equals ("FILL")) {
				data.verticalAlignment = GridData.FILL;
			} else {
				data.verticalAlignment = GridData.CENTER;
			}
			/* Set indents and spans */
			hIndent = new Integer (items [i].getText (HINDENT_COL)).intValue ();
			data.horizontalIndent = hIndent;
			hSpan = new Integer (items [i].getText (HSPAN_COL)).intValue ();
			data.horizontalSpan = hSpan;
			vSpan = new Integer (items [i].getText (VSPAN_COL)).intValue ();
			data.verticalSpan = vSpan;
			/* Set grabbers */
			hGrab = items [i].getText (HGRAB_COL);
			if (hGrab.equals ("true")) {
				data.grabExcessHorizontalSpace = true;
			} else {
				data.grabExcessHorizontalSpace = false;
			}
			vGrab = items [i].getText (VGRAB_COL);
			if (vGrab.equals ("true")) {
				data.grabExcessVerticalSpace = true;
			} else {
				data.grabExcessVerticalSpace = false;
			}
			children [i].setLayoutData (data);
		}
	}
	
	/**
	 * Sets the state of the layout.
	 */
	void setLayoutState () {
		/* Set the columns for the layout */
		try {
			gridLayout.numColumns = new Integer (numColumns.getText ()).intValue ();
		} catch (NumberFormatException e) {
			gridLayout.numColumns = 1;
		}
		gridLayout.makeColumnsEqualWidth = makeColumnsEqualWidth.getSelection ();
		
		/* Set the margins and spacing */
		try {
			gridLayout.marginHeight = new Integer (marginHeight.getText ()).intValue ();
		} catch (NumberFormatException e) {
			gridLayout.marginHeight = 5;
			marginHeight.select (2);
		}
		try {
			gridLayout.marginWidth = new Integer (marginWidth.getText ()).intValue ();
		} catch (NumberFormatException e) {
			gridLayout.marginWidth = 5;
			marginWidth.select (2);
		}
		try {
			gridLayout.horizontalSpacing = new Integer (horizontalSpacing.getText ()).intValue ();
		} catch (NumberFormatException e) {
			gridLayout.horizontalSpacing = 5;
			horizontalSpacing.select (2);
		}
		try {
			gridLayout.verticalSpacing = new Integer (verticalSpacing.getText ()).intValue ();
		} catch (NumberFormatException e) {
			gridLayout.verticalSpacing = 5;
			verticalSpacing.select (2);
		}
	}
}
