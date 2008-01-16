/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
	Spinner numColumns;
	Button makeColumnsEqualWidth;
	Spinner marginWidth, marginHeight, marginLeft, marginRight, marginTop, marginBottom, horizontalSpacing, verticalSpacing;
	/* The example layout instance */
	GridLayout gridLayout;
	/* TableEditors and related controls*/
	TableEditor nameEditor, comboEditor, widthEditor, heightEditor;
	TableEditor vAlignEditor, hAlignEditor, hGrabEditor, vGrabEditor, hSpanEditor, vSpanEditor;
	TableEditor hIndentEditor, vIndentEditor, minWidthEditor, minHeightEditor, excludeEditor;
	CCombo combo, vAlign, hAlign, hGrab, vGrab, exclude;
	Text nameText, widthText, heightText, hSpan, vSpan, hIndent, vIndent, minWidthText, minHeightText;
	/* Constants */
	static final int NAME_COL = 0;
	static final int COMBO_COL = 1;
	static final int WIDTH_COL = 2;
	static final int HEIGHT_COL = 3;
	static final int HALIGN_COL = 4;
	static final int VALIGN_COL = 5;
	static final int HGRAB_COL = 6;
	static final int VGRAB_COL = 7;
	static final int HSPAN_COL = 8;
	static final int VSPAN_COL = 9;
	static final int HINDENT_COL = 10;
	static final int VINDENT_COL = 11;
	static final int MINWIDTH_COL = 12;
	static final int MINHEIGHT_COL = 13;
	static final int EXCLUDE_COL = 14;
	
	static final int TOTAL_COLS = 15;
		
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
			
		/* Add hovers to the column headers whose field names have been shortened to save space */
		table.getColumn (HALIGN_COL).setToolTipText ("horizontalAlignment");
		table.getColumn (VALIGN_COL).setToolTipText ("verticalAlignment");
		table.getColumn (HGRAB_COL).setToolTipText ("grabExcessHorizontalSpace");
		table.getColumn (VGRAB_COL).setToolTipText ("grabExcessVerticalSpace");
		table.getColumn (HSPAN_COL).setToolTipText ("horizontalSpan");
		table.getColumn (VSPAN_COL).setToolTipText ("verticalSpan");
		table.getColumn (HINDENT_COL).setToolTipText ("horizontalIndent");
		table.getColumn (VINDENT_COL).setToolTipText ("verticalIndent");
		table.getColumn (MINWIDTH_COL).setToolTipText ("minimumWidth");
		table.getColumn (MINHEIGHT_COL).setToolTipText ("minimumHeight");
		
		/* Add TableEditors */		
		nameEditor = new TableEditor (table);
		comboEditor = new TableEditor (table);
		widthEditor = new TableEditor (table);
		heightEditor = new TableEditor (table);
		vAlignEditor = new TableEditor (table);
		hAlignEditor = new TableEditor (table);
		hGrabEditor = new TableEditor (table);
		vGrabEditor = new TableEditor (table);
		hSpanEditor = new TableEditor (table);
		vSpanEditor = new TableEditor (table);
		hIndentEditor = new TableEditor (table);
		vIndentEditor = new TableEditor (table);
		minWidthEditor = new TableEditor (table);
		minHeightEditor = new TableEditor (table);
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
				
				nameText = new Text (table, SWT.SINGLE);
				nameText.setText (((String [])data.elementAt (index)) [NAME_COL]);
				createTextEditor (nameText, nameEditor, NAME_COL);
				
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
				
				String [] boolValues = new String [] {"false", "true"};
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
                
				hSpan = new Text (table, SWT.SINGLE);
				hSpan.setText (((String [])data.elementAt (index)) [HSPAN_COL]);
				createTextEditor (hSpan, hSpanEditor, HSPAN_COL);
				
				vSpan = new Text (table, SWT.SINGLE);
				vSpan.setText (((String [])data.elementAt (index)) [VSPAN_COL]);
				createTextEditor (vSpan, vSpanEditor, VSPAN_COL);
				
				hIndent = new Text (table, SWT.SINGLE);
				hIndent.setText (((String [])data.elementAt (index)) [HINDENT_COL]);
				createTextEditor (hIndent, hIndentEditor, HINDENT_COL);
				
				vIndent = new Text (table, SWT.SINGLE);
				vIndent.setText (((String [])data.elementAt (index)) [VINDENT_COL]);
				createTextEditor (vIndent, vIndentEditor, VINDENT_COL);
				
				minWidthText = new Text (table, SWT.SINGLE);
				minWidthText.setText (((String [])data.elementAt (index)) [MINWIDTH_COL]);
				createTextEditor (minWidthText, minWidthEditor, MINWIDTH_COL);
				
				minHeightText = new Text (table, SWT.SINGLE);
				minHeightText.setText (((String [])data.elementAt (index)) [MINHEIGHT_COL]);
				createTextEditor (minHeightText, minHeightEditor, MINHEIGHT_COL);

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
                    		case NAME_COL:
                    			nameText.setFocus ();
                    			break;
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
							case HGRAB_COL :
								hGrab.setFocus ();
								break;
							case VGRAB_COL :
								vGrab.setFocus ();
								break;
							case HSPAN_COL :
								hSpan.setFocus ();
								break;
							case VSPAN_COL :
								vSpan.setFocus ();
								break;
							case HINDENT_COL :
								hIndent.setFocus ();
								break;
							case VINDENT_COL :
								vIndent.setFocus ();
								break;
							case MINWIDTH_COL :	
								minWidthText.setFocus ();
								break;
							case MINHEIGHT_COL :
								minHeightText.setFocus ();
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
        /* Controls the columns in the GridLayout */
		Group columnGroup = new Group (controlGroup, SWT.NONE);
		columnGroup.setText (LayoutExample.getResourceString ("Columns"));
		columnGroup.setLayout(new GridLayout(2, false));
		columnGroup.setLayoutData(new GridData (SWT.FILL, SWT.FILL, false, false));
		new Label(columnGroup, SWT.NONE).setText ("numColumns");
		numColumns = new Spinner (columnGroup, SWT.BORDER);
		numColumns.setMinimum (1);
		numColumns.addSelectionListener (selectionListener);
		makeColumnsEqualWidth = new Button (columnGroup, SWT.CHECK);
		makeColumnsEqualWidth.setText ("makeColumnsEqualWidth");
		makeColumnsEqualWidth.addSelectionListener (selectionListener);
		makeColumnsEqualWidth.setEnabled (false);
		makeColumnsEqualWidth.setLayoutData (new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		/* Controls the margins and spacing of the GridLayout */
		Group marginGroup = new Group(controlGroup, SWT.NONE);
		marginGroup.setText (LayoutExample.getResourceString("Margins_Spacing"));
		marginGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		marginGroup.setLayout(new GridLayout(2, false));
		new Label (marginGroup, SWT.NONE).setText("marginWidth");
		marginWidth = new Spinner (marginGroup, SWT.BORDER);
		marginWidth.setSelection(5);
		marginWidth.addSelectionListener(selectionListener);
		new Label(marginGroup, SWT.NONE).setText("marginHeight");
		marginHeight = new Spinner(marginGroup, SWT.BORDER);
		marginHeight.setSelection(5);
		marginHeight.addSelectionListener(selectionListener);
		new Label(marginGroup, SWT.NONE).setText("marginLeft");
		marginLeft = new Spinner(marginGroup, SWT.BORDER);
		marginLeft.setSelection(0);
		marginLeft.addSelectionListener(selectionListener);
		new Label(marginGroup, SWT.NONE).setText("marginRight");
		marginRight = new Spinner(marginGroup, SWT.BORDER);
		marginRight.setSelection(0);
		marginRight.addSelectionListener(selectionListener);
		new Label(marginGroup, SWT.NONE).setText("marginTop");
		marginTop = new Spinner(marginGroup, SWT.BORDER);
		marginTop.setSelection(0);
		marginTop.addSelectionListener(selectionListener);
		new Label(marginGroup, SWT.NONE).setText("marginBottom");
		marginBottom = new Spinner(marginGroup, SWT.BORDER);
		marginBottom.setSelection(0);
		marginBottom.addSelectionListener(selectionListener);
		new Label(marginGroup, SWT.NONE).setText("horizontalSpacing");
		horizontalSpacing = new Spinner(marginGroup, SWT.BORDER);
		horizontalSpacing.setSelection(5);
		horizontalSpacing.addSelectionListener(selectionListener);
		new Label(marginGroup, SWT.NONE).setText("verticalSpacing");
		verticalSpacing = new Spinner(marginGroup, SWT.BORDER);
		verticalSpacing.setSelection(5);
		verticalSpacing.addSelectionListener(selectionListener);
        
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
		nameText.dispose ();
		widthText.dispose ();
		heightText.dispose ();
		hAlign.dispose ();
		vAlign.dispose ();
		hGrab.dispose ();
		vGrab.dispose ();
		hSpan.dispose ();
		vSpan.dispose ();
		hIndent.dispose ();
		vIndent.dispose ();
		minWidthText.dispose ();
		minHeightText.dispose ();
		exclude.dispose ();
	}
	
	/**
	 * Generates code for the example layout.
	 */	
	StringBuffer generateLayoutCode () {
		StringBuffer code = new StringBuffer ();
		code.append ("\t\tGridLayout gridLayout = new GridLayout (");
		if (gridLayout.numColumns != 1 || gridLayout.makeColumnsEqualWidth) {
			code.append (gridLayout.numColumns + ", " + gridLayout.makeColumnsEqualWidth);
		}
		code.append(");\n");
		if (gridLayout.marginWidth != 5) {
			code.append("\t\tgridLayout.marginWidth = " + gridLayout.marginWidth + ";\n");
		}
		if (gridLayout.marginHeight != 5) {
			code.append ("\t\tgridLayout.marginHeight = " + gridLayout.marginHeight + ";\n");
		}
		if (gridLayout.marginLeft != 0) {
			code.append ("\t\tgridLayout.marginLeft = " + gridLayout.marginLeft + ";\n");
		}
		if (gridLayout.marginRight != 0) {
			code.append ("\t\tgridLayout.marginRight = " + gridLayout.marginRight + ";\n");
		}
		if (gridLayout.marginTop != 0) {
			code.append ("\t\tgridLayout.marginTop = " + gridLayout.marginTop + ";\n");
		}
		if (gridLayout.marginBottom != 0) {
			code.append ("\t\tgridLayout.marginBottom = " + gridLayout.marginBottom + ";\n");
		}
		if (gridLayout.horizontalSpacing != 5) {
			code.append ("\t\tgridLayout.horizontalSpacing = " + gridLayout.horizontalSpacing + ";\n");
		}
		if (gridLayout.verticalSpacing != 5) {
			code.append ("\t\tgridLayout.verticalSpacing = " + gridLayout.verticalSpacing + ";\n");
		}
		code.append ("\t\tshell.setLayout (gridLayout);\n");
		
		boolean first = true;
		boolean bounds, align, grab, span;
		for (int i = 0; i < children.length; i++) {
			Control control = children [i];
			code.append (getChildCode (control, i));
			GridData data = (GridData) control.getLayoutData ();
			if (data != null) {
				/* Use the most efficient constructor */
				bounds = data.widthHint != SWT.DEFAULT || data.heightHint != SWT.DEFAULT;
				align = data.horizontalAlignment != SWT.BEGINNING || data.verticalAlignment != SWT.CENTER;
				grab = data.grabExcessHorizontalSpace || data.grabExcessVerticalSpace;
				span = data.horizontalSpan != 1 || data.verticalSpan != 1;

				code.append ("\t\t");
				if (first) {
					code.append ("GridData ");
					first = false;
				}
				if (align || grab || span) {
					code.append ("data = new GridData (");
					code.append (alignmentString(data.horizontalAlignment) + ", ");
					code.append (alignmentString(data.verticalAlignment) + ", ");
					code.append (data.grabExcessHorizontalSpace + ", ");
					code.append (data.grabExcessVerticalSpace);
					if (span) {
						code.append (", " + data.horizontalSpan);
						code.append (", " + data.verticalSpan);
					}
					code.append(");\n");
					if (data.widthHint != SWT.DEFAULT) {
						code.append ("\t\tdata.widthHint = " + data.widthHint + ";\n");
					}
					if (data.heightHint != SWT.DEFAULT) {
						code.append ("\t\tdata.heightHint = " + data.heightHint + ";\n");
					}
				} else {
					if (bounds) {
						code.append ("data = new GridData (");
						code.append (data.widthHint == SWT.DEFAULT ? "SWT.DEFAULT" : String.valueOf(data.widthHint) + ", ");
						code.append (data.heightHint == SWT.DEFAULT ? "SWT.DEFAULT" : String.valueOf(data.heightHint));
						code.append(");\n");
					} else {
						code.append ("data = new GridData ();\n");
					}
				}
				if (data.horizontalIndent != 0) {
					code.append ("\t\tdata.horizontalIndent = " + data.horizontalIndent + ";\n");
				}
				if (data.verticalIndent != 0) {
					code.append ("\t\tdata.verticalIndent = " + data.verticalIndent + ";\n");
				}
				if (data.minimumWidth != 0) {
					code.append ("\t\tdata.minimumWidth = " + data.minimumWidth + ";\n");
				}
				if (data.minimumHeight != 0) {
					code.append ("\t\tdata.minimumHeight = " + data.minimumHeight + ";\n");
				}
				if (data.exclude) {
					code.append ("\t\tdata.exclude = true;\n");
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
	
	String alignmentString(int alignment) {
		if (alignment == SWT.BEGINNING) return "SWT.BEGINNING";
		if (alignment == SWT.CENTER) return "SWT.CENTER";
		if (alignment == SWT.END) return "SWT.END";
		return "SWT.FILL";
	}

	/**
	 * Returns the string to insert when a new child control is added to the table.
	 */
	String[] getInsertString (String name, String controlType) {
		return new String [] {name, controlType,
				"-1","-1","BEGINNING","CENTER",
				"false","false","1","1","0","0",
				"0","0","false"};
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
			"hAlignment", //"horizontalAlignment", 
			"vAlignment", //"verticalAlignment", 
			"grabH", //"grabExcessHorizontalSpace", 
			"grabV", //"grabExcessVerticalSpace", 
			"hSpan", //"horizontalSpan",
			"vSpan", //"verticalSpan", 
			"hIndent", //"horizontalIndent", 
			"vIndent", //"verticalIndent",
			"minWidth", //"minimumWidth",
			"minHeight", //"minimumHeight", 
			"exclude"
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
	void resetEditors (boolean tab) {
		TableItem oldItem = comboEditor.getItem ();
		if (oldItem != null) {
			int row = table.indexOf (oldItem);
			/** Make sure user enters a valid data*/
			try {				
				new String (nameText.getText ());
			} catch (NumberFormatException e) {
				nameText.setText (oldItem.getText (NAME_COL));
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
			try {
				new Integer (hIndent.getText ()).intValue ();
			} catch (NumberFormatException e) {
				hIndent.setText (oldItem.getText (HINDENT_COL));
			}
			try {
				new Integer (vIndent.getText ()).intValue ();
			} catch (NumberFormatException e) {
				vIndent.setText (oldItem.getText (VINDENT_COL));
			}
			try {
				new Integer (minWidthText.getText ()).intValue ();
			} catch (NumberFormatException e) {
				minWidthText.setText (oldItem.getText (MINWIDTH_COL));
			}
			try {
				new Integer (minHeightText.getText ()).intValue ();
			} catch (NumberFormatException e) {
				minHeightText.setText (oldItem.getText (MINHEIGHT_COL));
			}
			String [] insert = new String [] {
				nameText.getText (), combo.getText (), widthText.getText (), heightText.getText (),
				hAlign.getText (), vAlign.getText (), hGrab.getText (), vGrab.getText (), 
				hSpan.getText (), vSpan.getText (), hIndent.getText (), vIndent.getText (), 
				minWidthText.getText (), minHeightText.getText (), exclude.getText ()
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
	 * Return the initial weight of the layout and control groups within the SashForm.
	 * @return the desired sash weights for the tab page
	 */
	int[] sashWeights () {
		return new int[] {35, 65};		
	}

	/**
	 * Sets the layout data for the children of the layout.
	 */
	void setLayoutData () {
		Control [] children = layoutComposite.getChildren ();
		TableItem [] items = table.getItems ();
		GridData data;
		int hSpan, vSpan, hIndent, vIndent;
		String vAlign, hAlign, vGrab, hGrab, exclude;
		for (int i = 0; i < children.length; i++) {
			data = new GridData ();
			/* Set widthHint and heightHint */
			data.widthHint = new Integer (items [i].getText (WIDTH_COL)).intValue ();
			data.heightHint = new Integer (items [i].getText (HEIGHT_COL)).intValue ();
			/* Set vertical alignment and horizontal alignment */
			hAlign = items [i].getText (HALIGN_COL);
			if (hAlign.equals ("CENTER")) {
				data.horizontalAlignment = SWT.CENTER;
			} else if (hAlign.equals ("END")) {
				data.horizontalAlignment = SWT.END;
			} else if (hAlign.equals ("FILL")) {
				data.horizontalAlignment = SWT.FILL;
			} else {
				data.horizontalAlignment = SWT.BEGINNING;
			}
			vAlign = items [i].getText (VALIGN_COL);
			if (vAlign.equals ("BEGINNING")) {
				data.verticalAlignment = SWT.BEGINNING;
			} else if (vAlign.equals ("END")) {
				data.verticalAlignment = SWT.END;
			} else if (vAlign.equals ("FILL")) {
				data.verticalAlignment = SWT.FILL;
			} else {
				data.verticalAlignment = SWT.CENTER;
			}
			/* Set spans and indents */
			hSpan = new Integer (items [i].getText (HSPAN_COL)).intValue ();
			data.horizontalSpan = hSpan;
			vSpan = new Integer (items [i].getText (VSPAN_COL)).intValue ();
			data.verticalSpan = vSpan;
			hIndent = new Integer (items [i].getText (HINDENT_COL)).intValue ();
			data.horizontalIndent = hIndent;
			vIndent = new Integer (items [i].getText (VINDENT_COL)).intValue ();
			data.verticalIndent = vIndent;
			/* Set grabs */
			hGrab = items [i].getText (HGRAB_COL);
			data.grabExcessHorizontalSpace = hGrab.equals ("true");
			vGrab = items [i].getText (VGRAB_COL);
			data.grabExcessVerticalSpace = vGrab.equals ("true");
			/* Set minimum width and height */
			data.minimumWidth = new Integer (items [i].getText (MINWIDTH_COL)).intValue ();
			data.minimumHeight = new Integer (items [i].getText (MINHEIGHT_COL)).intValue ();
			/* Set exclude boolean */
			exclude = items [i].getText (EXCLUDE_COL);
			data.exclude = exclude.equals ("true");
			
			children [i].setLayoutData (data);
		}
	}
	
	/**
	 * Sets the state of the layout.
	 */
	void setLayoutState () {
		/* Set the columns for the layout */
		gridLayout.numColumns = numColumns.getSelection ();
		gridLayout.makeColumnsEqualWidth = makeColumnsEqualWidth.getSelection ();
		makeColumnsEqualWidth.setEnabled (numColumns.getSelection () > 1);
		
		/* Set the margins and spacing */
		gridLayout.marginWidth = marginWidth.getSelection ();
		gridLayout.marginHeight = marginHeight.getSelection ();
		gridLayout.marginLeft = marginLeft.getSelection ();
		gridLayout.marginRight = marginRight.getSelection ();
		gridLayout.marginTop = marginTop.getSelection ();
		gridLayout.marginBottom = marginBottom.getSelection ();
		gridLayout.horizontalSpacing = horizontalSpacing.getSelection ();
		gridLayout.verticalSpacing = verticalSpacing.getSelection ();
	}
}
