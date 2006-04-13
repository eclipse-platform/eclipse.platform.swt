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

class FormLayoutTab extends Tab {
	/* Controls for setting layout parameters */
	Combo marginHeight, marginWidth;
	/* The example layout instance */
	FormLayout formLayout;
	/* TableEditors and related controls*/
	TableEditor comboEditor, widthEditor, heightEditor;
	TableEditor leftEditor, rightEditor, topEditor, bottomEditor;
	CCombo combo;
	Text widthText, heightText;
	Button leftAttach, rightAttach, topAttach, bottomAttach;
	
	/* Constants */
	final int COMBO_COL = 1;
	final int WIDTH_COL = 2;
	final int HEIGHT_COL = 3;
	final int LEFT_COL = 4;
	final int RIGHT_COL = 5;
	final int TOP_COL = 6;
	final int BOTTOM_COL = 7;
	
	final int MODIFY_COLS = 4;	// The number of columns with combo or text editors
	final int TOTAL_COLS = 8;

	/**
	 * Creates the Tab within a given instance of LayoutExample.
	 */
	FormLayoutTab(LayoutExample instance) {
		super(instance);
	}
	
	/**
	 * Returns the constant for the alignment for an
	 * attachment given a string.
	 */
	int alignmentConstant (String align) {
		if (align.equals("LEFT")) return SWT.LEFT;
		if (align.equals("RIGHT")) return SWT.RIGHT;
		if (align.equals("TOP")) return SWT.TOP;
		if (align.equals("BOTTOM")) return SWT.BOTTOM;
		if (align.equals("CENTER")) return SWT.CENTER;
		return SWT.DEFAULT;
	}
	
	/**
	 * Returns a string representing the alignment for an
	 * attachment given a constant.
	 */
	String alignmentString (int align) {
		switch (align) {
			case SWT.LEFT: return "LEFT";
			case SWT.RIGHT: return "RIGHT";
			case SWT.TOP: return "TOP";
			case SWT.BOTTOM: return "BOTTOM";
			case SWT.CENTER: return "CENTER";
		}
		return "DEFAULT";
	}
	
	/**
	 * Update the attachment field in case the type of control
	 * has changed. 
	 */
	String checkAttachment (String oldAttach, FormAttachment newAttach) {
		String controlClass = newAttach.control.getClass().toString ();
		String controlType = controlClass.substring (controlClass.lastIndexOf ('.') + 1);
		int i = 0;
		while (i < oldAttach.length () && !Character.isDigit(oldAttach.charAt (i))) {
			i++;
		}
		String index = oldAttach.substring (i, oldAttach.indexOf (','));
		return controlType + index + "," + newAttach.offset + ":" + alignmentString (newAttach.alignment);
	}
	
	/**
	 * Creates the widgets in the "child" group.
	 */
	void createChildWidgets () {
		/* Add common controls */
		super.createChildWidgets ();
		
		/* Resize the columns */
		table.getColumn (LEFT_COL).setWidth (100);
		table.getColumn (RIGHT_COL).setWidth (100);
		table.getColumn (TOP_COL).setWidth (100);
		table.getColumn (BOTTOM_COL).setWidth (100);
				
		/* Add TableEditors */			
		comboEditor = new TableEditor (table);
		widthEditor = new TableEditor (table);
		heightEditor = new TableEditor (table);
		leftEditor = new TableEditor (table);
		rightEditor = new TableEditor (table);
		topEditor = new TableEditor (table);
		bottomEditor = new TableEditor (table);
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
				
				leftAttach = new Button (table, SWT.PUSH);
				leftAttach.setText (LayoutExample.getResourceString ("Attach_Edit"));
				leftEditor.horizontalAlignment = SWT.LEFT;
				leftEditor.grabHorizontal = true;
				leftEditor.minimumWidth = leftAttach.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
				leftEditor.setEditor (leftAttach, newItem, LEFT_COL);
				leftAttach.addSelectionListener (new SelectionAdapter () {
					public void widgetSelected (SelectionEvent e) {
						Shell shell = tabFolderPage.getShell ();
						AttachDialog dialog = new AttachDialog (shell);
						dialog.setText (LayoutExample.getResourceString ("Left_Attachment"));
						dialog.setColumn (LEFT_COL);
						String attach = dialog.open ();
						newItem.setText (LEFT_COL, attach);
						resetEditors ();
					}
				});
				
				rightAttach = new Button (table, SWT.PUSH);
				rightAttach.setText (LayoutExample.getResourceString ("Attach_Edit"));
				rightEditor.horizontalAlignment = SWT.LEFT;
				rightEditor.grabHorizontal = true;
				rightEditor.minimumWidth = rightAttach.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
				rightEditor.setEditor (rightAttach, newItem, RIGHT_COL);
				rightAttach.addSelectionListener (new SelectionAdapter () {
					public void widgetSelected (SelectionEvent e) {
						Shell shell = tabFolderPage.getShell ();
						AttachDialog dialog = new AttachDialog (shell);
						dialog.setText (LayoutExample.getResourceString ("Right_Attachment"));
						dialog.setColumn (RIGHT_COL);
						String attach = dialog.open ();
						newItem.setText (RIGHT_COL, attach);
						if (newItem.getText (LEFT_COL).endsWith (")")) newItem.setText (LEFT_COL, "");
						resetEditors ();
					}
				});
				
				topAttach = new Button (table, SWT.PUSH);
				topAttach.setText (LayoutExample.getResourceString ("Attach_Edit"));
				topEditor.horizontalAlignment = SWT.LEFT;
				topEditor.grabHorizontal = true;
				topEditor.minimumWidth = topAttach.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
				topEditor.setEditor (topAttach, newItem, TOP_COL);
				topAttach.addSelectionListener (new SelectionAdapter () {
					public void widgetSelected (SelectionEvent e) {
						Shell shell = tabFolderPage.getShell ();
						AttachDialog dialog = new AttachDialog (shell);
						dialog.setText (LayoutExample.getResourceString ("Top_Attachment"));
						dialog.setColumn (TOP_COL);
						String attach = dialog.open ();
						newItem.setText (TOP_COL, attach);
						resetEditors ();
					}
				});
				bottomAttach = new Button (table, SWT.PUSH);
				bottomAttach.setText (LayoutExample.getResourceString ("Attach_Edit"));
				bottomEditor.horizontalAlignment = SWT.LEFT;
				bottomEditor.grabHorizontal = true;
				bottomEditor.minimumWidth = bottomAttach.computeSize (SWT.DEFAULT, SWT.DEFAULT).x;
				bottomEditor.setEditor (bottomAttach, newItem, BOTTOM_COL);
				bottomAttach.addSelectionListener (new SelectionAdapter () {
					public void widgetSelected (SelectionEvent e) {
						Shell shell = tabFolderPage.getShell ();
						AttachDialog dialog = new AttachDialog (shell);
						dialog.setText (LayoutExample.getResourceString ("Bottom_Attachment"));
						dialog.setColumn (BOTTOM_COL);
						String attach = dialog.open ();
						newItem.setText (BOTTOM_COL, attach);
						if (newItem.getText (TOP_COL).endsWith (")")) newItem.setText (TOP_COL, "");
						resetEditors ();
					}
				});
                
                for (int i=0; i<table.getColumnCount (); i++) {
                	Rectangle rect = newItem.getBounds (i);
                    if (rect.contains (pt)) {
                    	switch (i) {
                    		case 0:
                    			resetEditors ();
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
							default :
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
					String.valueOf (table.indexOf (item)), "Button", "-1", "-1",
					"0,0 (" + LayoutExample.getResourceString ("Default") + ")", "", 
					"0,0 (" + LayoutExample.getResourceString ("Default") + ")", ""};
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
		/* Controls the margins and spacing of the FormLayout */
		String [] marginValues = new String [] {"0","3","5","10"};
		Group marginGroup = new Group (controlGroup, SWT.NONE);
		marginGroup.setText (LayoutExample.getResourceString ("Margins"));
		GridLayout layout = new GridLayout ();
		layout.numColumns = 2;
		marginGroup.setLayout (layout);
		marginGroup.setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
		new Label (marginGroup, SWT.NONE).setText ("marginHeight");
		marginHeight = new Combo (marginGroup, SWT.NONE);
		marginHeight.setItems (marginValues);
		marginHeight.select (0);
		marginHeight.addSelectionListener (selectionListener);
		marginHeight.addTraverseListener (traverseListener);
		GridData data = new GridData (GridData.FILL_HORIZONTAL);
		data.widthHint = 60;
		marginHeight.setLayoutData (data);
		new Label (marginGroup, SWT.NONE).setText ("marginWidth");
		marginWidth = new Combo (marginGroup, SWT.NONE);
		marginWidth.setItems (marginValues);
		marginWidth.select (0);
		marginWidth.addSelectionListener (selectionListener);
		marginWidth.addTraverseListener (traverseListener);
		data = new GridData (GridData.FILL_HORIZONTAL);
		data.widthHint = 60;
		marginWidth.setLayoutData (data);
		
		/* Add common controls */
		super.createControlWidgets ();
		
		/* Position the sash */
		sash.setWeights (new int [] {6,4});
	}
	
	/**
	 * Creates the example layout.
	 */
	void createLayout () {
		formLayout = new FormLayout ();
		layoutComposite.setLayout (formLayout);
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
		leftAttach.dispose ();
		rightAttach.dispose ();
		topAttach.dispose ();
		bottomAttach.dispose ();
	}

	/**
	 * Generates code for the example layout.
	 */	
	StringBuffer generateLayoutCode () {
		StringBuffer code = new StringBuffer ();
		code.append ("\t\tFormLayout formLayout = new FormLayout ();\n");
		if (formLayout.marginHeight != 0) {
			code.append ("\t\tformLayout.marginHeight = " + formLayout.marginHeight + ";\n");
		}
		if (formLayout.marginWidth != 0) {
			code.append ("\t\tformLayout.marginWidth = " + formLayout.marginWidth + ";\n");
		}
		code.append ("\t\tshell.setLayout (formLayout);\n");
		
		boolean first = true;
		for (int i = 0; i < children.length; i++) {
			Control control = children [i];
			code.append (getChildCode (control, i));
			FormData data = (FormData) control.getLayoutData ();
			if (data != null) {
				code.append ("\t\t");
				if (first) {
					code.append ("FormData ");
					first = false;
				}
				code.append ("data = new FormData ();\n");	
				if (data.width != SWT.DEFAULT) {
					code.append ("\t\tdata.width = " + data.width + ";\n");
				}
				if (data.height != SWT.DEFAULT) {
					code.append ("\t\tdata.height = " + data.height + ";\n");
				}
				if (data.left != null) {
					if (data.left.control != null) {
						TableItem item = table.getItem (i);
						String controlString = item.getText (LEFT_COL);
						int index = new Integer (controlString.substring (controlString.indexOf (',') - 1, controlString.indexOf (','))).intValue ();
						code.append ("\t\tdata.left = new FormAttachment (" + names [index] + ", " + data.left.offset + ", SWT." + alignmentString (data.left.alignment) + ");\n");
					} else {
						if (data.right != null || (data.left.numerator != 0 ||data.left.offset != 0)) {
							code.append ("\t\tdata.left = new FormAttachment (" + data.left.numerator + ", " + data.left.offset + ");\n");
						}
					}
				}
				if (data.right != null) {
					if (data.right.control != null) {
						TableItem item = table.getItem (i);
						String controlString = item.getText (RIGHT_COL);
						int index = new Integer (controlString.substring (controlString.indexOf (',') - 1, controlString.indexOf (','))).intValue ();
						code.append ("\t\tdata.right = new FormAttachment (" + names [index] + ", " + data.right.offset + ", SWT." + alignmentString (data.right.alignment) + ");\n");
					} else {
						code.append ("\t\tdata.right = new FormAttachment (" + data.right.numerator + ", " + data.right.offset + ");\n");
					}
				}
				if (data.top != null) {
					if (data.top.control != null) {
						TableItem item = table.getItem (i);
						String controlString = item.getText (TOP_COL);
						int index = new Integer (controlString.substring (controlString.indexOf (',') - 1, controlString.indexOf (','))).intValue ();
						code.append ("\t\tdata.top = new FormAttachment (" + names [index] + ", " + data.top.offset + ", SWT." + alignmentString (data.top.alignment) + ");\n");
					} else {
						if (data.bottom != null || (data.top.numerator != 0 ||data.top.offset != 0)) {
							code.append ("\t\tdata.top = new FormAttachment (" + data.top.numerator + ", " + data.top.offset + ");\n");
						}
					}
				}
				if (data.bottom != null) {
					if (data.bottom.control != null) {
						TableItem item = table.getItem (i);
						String controlString = item.getText (BOTTOM_COL);
						int index = new Integer (controlString.substring (controlString.indexOf (',') - 1, controlString.indexOf (','))).intValue ();
						code.append ("\t\tdata.bottom = new FormAttachment (" + names [index] + ", " + data.bottom.offset + ", SWT." + alignmentString (data.bottom.alignment) + ");\n");
					} else {
						code.append ("\t\tdata.bottom = new FormAttachment (" + data.bottom.numerator + ", " + data.bottom.offset + ");\n");
					}
				}
				code.append ("\t\t" + names [i] + ".setLayoutData (data);\n");
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
			"left", 
			"right", 
			"top", 
			"bottom"
		};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "FormLayout";
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
			String [] insert = new String [] {String.valueOf (row), combo.getText (), widthText.getText (), heightText.getText ()};
			data.setElementAt (insert, row);
			for (int i = 0 ; i < MODIFY_COLS; i++) {
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
	 * Sets an attachment to the edge of a widget using the
	 * information in the table.
	 */
	FormAttachment setAttachment (String attachment) {
		String control, align;
		int position, offset;
		int comma = attachment.indexOf (',');
		char first = attachment.charAt (0);
		if (Character.isLetter(first)) {
			/* Case where there is a control */
			control = attachment.substring (0, comma);
			int i = 0;
			while (i < control.length () && !Character.isDigit (control.charAt (i))) {
				i++;
			}
			String end = control.substring (i);
			int index = new Integer (end).intValue ();
			Control attachControl = children [index];
			int colon = attachment.indexOf (':');
			try {
				offset = new Integer (attachment.substring (comma + 1, colon)).intValue ();
			} catch (NumberFormatException e) {
				offset = 0;
			}
			align = attachment.substring (colon + 1);
			return new FormAttachment (attachControl, offset, alignmentConstant (align));
		}
		/* Case where there is a position */
		try {
			position = new Integer (attachment.substring (0,comma)).intValue ();	
		} catch (NumberFormatException e) {
			position = 0;
		}
		try {
			offset = new Integer (attachment.substring (comma + 1)).intValue ();
		} catch (NumberFormatException e) {
			offset = 0;
		}
		return new FormAttachment (position, offset);		
	}
	
	/**
	 * Sets the layout data for the children of the layout.
	 */
	void setLayoutData () {
		Control [] children = layoutComposite.getChildren ();
		TableItem [] items = table.getItems ();
		FormData data;
		int width, height;
		String left, right, top, bottom;
		for (int i = 0; i < children.length; i++) {
			width = new Integer (items [i].getText (WIDTH_COL)).intValue ();
			height = new Integer (items [i].getText (HEIGHT_COL)).intValue ();
			data = new FormData ();
			if (width > 0) data.width = width;
			if (height > 0) data.height = height;
			
			left = items [i].getText (LEFT_COL);
			if (left.length () > 0) {
				data.left = setAttachment (left);
				if (data.left.control != null) {
					String attachment = checkAttachment (left, data.left);
					items [i].setText (LEFT_COL, attachment);
				}
			}
			right = items [i].getText (RIGHT_COL);
			if (right.length () > 0) {
				data.right = setAttachment (right);
				if (data.right.control != null) {
					String attachment = checkAttachment (right, data.right);
					items [i].setText (RIGHT_COL, attachment);
				}
			}
			top = items [i].getText (TOP_COL);
			if (top.length () > 0 ) {
				data.top = setAttachment (top);
				if (data.top.control != null) {
					String attachment = checkAttachment (top, data.top);
					items [i].setText (TOP_COL, attachment);
				}
			}
			bottom = items [i].getText (BOTTOM_COL);
			if (bottom.length () > 0) {
				data.bottom = setAttachment (bottom);
				if (data.bottom.control != null) {
					String attachment = checkAttachment (bottom, data.bottom);
					items [i].setText (BOTTOM_COL, attachment);
				}
			}
			children [i].setLayoutData (data);
		}
	}
	
	/**
	 * Sets the state of the layout.
	 */
	void setLayoutState () {
		/* Set the margins and spacing */
		try {
			formLayout.marginHeight = new Integer (marginHeight.getText ()).intValue ();
		} catch (NumberFormatException e) {
			formLayout.marginHeight = 0;
			marginHeight.select (0);
		}
		try {
			formLayout.marginWidth = new Integer (marginWidth.getText ()).intValue ();
		} catch (NumberFormatException e) {
			formLayout.marginWidth = 0;
			marginWidth.select (0);
		}
	}
	
	
	/**
	 * <code>AttachDialog</code> is the class that creates a
	 * dialog specific for this example. It creates a dialog
	 * with controls to set the values in a FormAttachment.
	 */
	public class AttachDialog extends Dialog {
		String result = "";
		String controlInput, positionInput, alignmentInput, offsetInput;
		int col = 0;
		
		public AttachDialog (Shell parent, int style) {
			super (parent, style);
		}
		
		public AttachDialog (Shell parent) {
			this (parent, 0);
		}
		
		public void setColumn (int col) {
			this.col = col;
		}
	 
		public String open () {
			Shell parent = getParent ();
			final Shell shell = new Shell (parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
			shell.setText (getText ());
			GridLayout layout = new GridLayout ();
			layout.numColumns = 3;
			layout.makeColumnsEqualWidth = true;
			shell.setLayout (layout);
			
			/* Find out what was previously set as an attachment */
			TableItem newItem = leftEditor.getItem ();
			result = newItem.getText (col);
			String oldAttach = result;
			String oldPos = "0", oldControl = "", oldAlign = "DEFAULT", oldOffset = "0";
			boolean isControl = false;
			if (oldAttach.length () != 0) {
				char first = oldAttach.charAt (0);
				if (Character.isLetter(first)) {
					/* We have a control */
					isControl = true;
					oldControl = oldAttach.substring (0, oldAttach.indexOf (','));
					oldAlign = oldAttach.substring (oldAttach.indexOf (':') + 1);
					oldOffset = oldAttach.substring (oldAttach.indexOf (',') + 1, oldAttach.indexOf (':'));
				} else {
					/* We have a position */
					oldPos = oldAttach.substring (0, oldAttach.indexOf (','));
					oldOffset = oldAttach.substring (oldAttach.indexOf (',') + 1);
					if (oldOffset.endsWith (")")) { // i.e. (Default)
						oldOffset = oldOffset.substring (0, oldOffset.indexOf (' '));
					}
				}
			}
			
			/* Add position field */
			final Button posButton = new Button (shell, SWT.RADIO);
			posButton.setText (LayoutExample.getResourceString ("Position"));
			posButton.setSelection (!isControl);			
			final Combo position = new Combo (shell, SWT.NONE);
			position.setItems (new String [] {"0","25","50","75","100"});
			position.setText (oldPos);
			position.setEnabled (!isControl);			
			GridData data = new GridData (GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			position.setLayoutData (data);
			
			/* Add control field */
			final Button contButton = new Button (shell, SWT.RADIO);
			contButton.setText (LayoutExample.getResourceString ("Control"));
			contButton.setSelection (isControl);
			final Combo control = new Combo (shell, SWT.READ_ONLY);
			TableItem [] items = table.getItems ();
			TableItem currentItem = leftEditor.getItem ();
			for (int i = 0; i < table.getItemCount (); i++) {
				if (items [i].getText (0).length() > 0) {
					if (items [i] != currentItem) {
						control.add (items [i].getText (COMBO_COL) + i);
					}
				}
			}
			if (oldControl.length () != 0) control.setText (oldControl);
			else control.select (0);
			control.setEnabled (isControl);
			data = new GridData (GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			control.setLayoutData (data);
			
			/* Add alignment field */
			new Label (shell, SWT.NONE).setText (LayoutExample.getResourceString ("Alignment"));
			final Combo alignment = new Combo (shell, SWT.NONE);
			String[] alignmentValues;
			if (col == LEFT_COL || col == RIGHT_COL) {
				alignmentValues = new String [] {"SWT.LEFT", "SWT.RIGHT", "SWT.CENTER", "SWT.DEFAULT"};
			} else {
				// col == TOP_COL || col == BOTTOM_COL
				alignmentValues = new String [] {"SWT.TOP", "SWT.BOTTOM", "SWT.CENTER", "SWT.DEFAULT"};
			}
			alignment.setItems (alignmentValues);
			alignment.setText ("SWT." + oldAlign);
			alignment.setEnabled (isControl);
			data = new GridData (GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			alignment.setLayoutData (data);
			
			/* Add offset field */
			new Label (shell, SWT.NONE).setText (LayoutExample.getResourceString ("Offset"));
			final Text offset = new Text (shell, SWT.SINGLE | SWT.BORDER);
			offset.setText (oldOffset);
			data = new GridData (GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			offset.setLayoutData (data);
			
			/* Add listeners for choosing between position and control */
			posButton.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					position.setEnabled (true);
					control.setEnabled (false);
					alignment.setEnabled(false);
				}
			});
			contButton.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					position.setEnabled (false);
					control.setEnabled (true);
					alignment.setEnabled(true);
				}
			});
			
			Button clear = new Button (shell, SWT.PUSH);
			clear.setText (LayoutExample.getResourceString ("Clear"));
			clear.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_END));
			clear.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					result = "";
					shell.close ();
				}
			});
			/* OK button sets data into table */
			Button ok = new Button (shell, SWT.PUSH);
			ok.setText (LayoutExample.getResourceString ("OK"));
			ok.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_CENTER));
			ok.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					controlInput = control.getText ();
					alignmentInput = alignment.getText ().substring (4);
					positionInput = position.getText ();
					if (positionInput.length () == 0) positionInput = "0";
					try {
						new Integer (positionInput).intValue ();
					} catch (NumberFormatException except) {
						positionInput = "0";
					}
					offsetInput = offset.getText ();
					if (offsetInput.length () == 0) offsetInput = "0";
					try {
						new Integer (offsetInput).intValue ();
					} catch (NumberFormatException except) {
						offsetInput = "0";
					}
					if (posButton.getSelection() || controlInput.length () == 0) {
						result = positionInput + "," + offsetInput;
					} else {
						result = controlInput + "," + offsetInput + ":" + alignmentInput;
					}
					shell.close ();
				}
			});
			Button cancel = new Button (shell, SWT.PUSH);
			cancel.setText (LayoutExample.getResourceString ("Cancel"));
			cancel.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING));
			cancel.addSelectionListener (new SelectionAdapter () {
				public void widgetSelected (SelectionEvent e) {
					shell.close ();
				}
			});
			
			shell.setDefaultButton (ok);
			shell.pack ();
			/* Center the dialog */
			Point center = parent.getLocation ();
			center.x = center.x + (parent.getBounds ().width / 2) - (shell.getBounds ().width / 2);
			center.y = center.y + (parent.getBounds ().height / 2) - (shell.getBounds ().height / 2);
			shell.setLocation (center);
			shell.open ();
			Display display = shell.getDisplay ();
			while (!shell.isDisposed ()) {
				if (display.readAndDispatch ()) display.sleep ();
			}
			
			return result;
		}
	}
}
