
package org.eclipse.swt.examples.layoutexample;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import java.util.Vector;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;



/**
 * <code>Tab</code> is the abstract superclass of every page
 * in the example's tab folder.  Each page in the tab folder
 * displays a layout, and allows the user to manipulate the
 * layout.
 *
 * A typical page in a Tab contains a two column composite.
 * The left column contains the layout group, which contains
 * the "layout composite" (the one that has the example layout).
 * The right column contains the "control" group. The "control"
 * group allows the user to interact with the example. Typical
 * operations are modifying layout parameters, adding children
 * to the "layout composite", and modifying child layout data.
 * The "Code" button in the "control" group opens a new window
 * containing code that will regenerate the layout. This code
 * (or parts of it) can be selected and copied to the clipboard.
 */
abstract class Tab {	
	/* Common groups and composites */
	Composite tabFolderPage;
	Group layoutGroup, controlGroup, childGroup;
	/* The composite that contains the example layout */
	Composite layoutComposite;
	/* Common controls for modifying the example layout */
	String [] names;
	Control [] children;
	Button size, add, delete, clear, code;
	/* Common values for working with TableEditors */
	Table table;
	int index;
	TableItem newItem;
	Vector data = new Vector ();
	/* Controlling instance */
	final LayoutExample instance;

	/**
	 * Creates the Tab within a given instance of LayoutExample.
	 */
	Tab(LayoutExample instance) {
		this.instance = instance;
	}
	
	/**
	 * Creates the "child" group. This is the group that allows
	 * you to add children to the layout. It exists within the
	 * controlGroup.
	 */
	void createChildGroup () {
		childGroup = new Group (controlGroup, SWT.NONE);
		childGroup.setText (LayoutExample.getResourceString("Children"));
		GridLayout layout = new GridLayout ();
		childGroup.setLayout (layout);
		layout.numColumns = 3;
		GridData data = new GridData (GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		childGroup.setLayoutData (data); 
		createChildWidgets ();
	}
	
	/**
	 * Creates the controls for modifying the "children" 
	 * table, and the table itself.
	 * Subclasses override this method to augment the
	 * standard table.
	 */
	void createChildWidgets () {
		/* Controls for adding and removing children */
		add = new Button (childGroup, SWT.PUSH);
		add.setText (LayoutExample.getResourceString ("Add"));
		delete = new Button (childGroup, SWT.PUSH);
		delete.setText (LayoutExample.getResourceString ("Delete"));
		delete.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				resetEditors ();
				int [] selected = table.getSelectionIndices ();
				table.remove (selected);
				refreshLayoutComposite ();
				layoutComposite.layout (true);
				layoutGroup.layout (true);
			}
		});
		clear = new Button (childGroup, SWT.PUSH);
		clear.setText (LayoutExample.getResourceString ("Clear"));
		clear.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				resetEditors ();
				children = layoutComposite.getChildren ();
				for (int i = 0; i < children.length; i++) {
					children [i].dispose ();
				}
				table.removeAll ();
				data.clear ();
				children = new Control [0];
				layoutGroup.layout (true);
			}
		});
		/* Create the "children" table */
		table = new Table (childGroup, SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData gridData = new GridData ();
		gridData.horizontalSpan = 3;
		gridData.heightHint = 150;
		table.setLayoutData (gridData);
		table.addTraverseListener(new TraverseListener() {
        	public void keyTraversed(TraverseEvent e) {
            	if (e.detail == SWT.TRAVERSE_RETURN) {
                	e.doit = false;
                    resetEditors ();
                }	
            }
        });

		
		/* Add columns to the table */
		String [] columnHeaders = getLayoutDataFieldNames ();
		int [] columnWidths = getLayoutDataColumnWidths ();
		for (int i = 0; i < columnHeaders.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText (columnHeaders [i]);
			column.setWidth (columnWidths [i]);
		}
	}

	/**
	 * Creates the TableEditor with a CCombo in the first column
	 * of the table. This CCombo lists all the controls that
	 * the user can select to place on their layout.
	 */
	void createComboEditor (CCombo combo, TableEditor comboEditor) {
		combo.setItems (new String [] {
			LayoutExample.getResourceString ("Canvas"),
			LayoutExample.getResourceString ("CheckBox"),
			LayoutExample.getResourceString ("Combo"),
			LayoutExample.getResourceString ("Composite"),
			LayoutExample.getResourceString ("CoolBar"),
			LayoutExample.getResourceString ("Group"),
			LayoutExample.getResourceString ("Label"),
			LayoutExample.getResourceString ("List"),
			LayoutExample.getResourceString ("MultiLineText"),
			LayoutExample.getResourceString ("ProgressBar"),
			LayoutExample.getResourceString ("PushButton"),
			LayoutExample.getResourceString ("RadioButton"),
			LayoutExample.getResourceString ("Scale"),
			LayoutExample.getResourceString ("Slider"),
			LayoutExample.getResourceString ("StyledText"),
			LayoutExample.getResourceString ("Table"),
			LayoutExample.getResourceString ("Text"),
			LayoutExample.getResourceString ("ToolBar"),
			LayoutExample.getResourceString ("Tree"),
			LayoutExample.getResourceString ("WrappedLabel")});
		combo.setText (newItem.getText (0));
		
		/* Set up editor */
		comboEditor.horizontalAlignment = SWT.LEFT;
		comboEditor.grabHorizontal = true;
		comboEditor.minimumWidth = 50;
		comboEditor.setEditor (combo, newItem, 0);
		combo.setFocus ();
		
		/* Add listener */
		combo.addTraverseListener(new TraverseListener() {
        	public void keyTraversed(TraverseEvent e) {
            	if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_RETURN) {
                    resetEditors ();
                }
                if (e.detail == SWT.TRAVERSE_ESCAPE) {
                	disposeEditors ();
                }
            }
        });
	}
	
	/**
	 * Creates the "control" group. This is the group on the
	 * right half of each example tab. It contains controls
	 * for adding new children to the layoutComposite, and
	 * for modifying the children's layout data.
	 */	
	void createControlGroup () {
		controlGroup = new Group (tabFolderPage, SWT.NONE);
		controlGroup.setText (LayoutExample.getResourceString("Parameters"));
		GridLayout layout = new GridLayout ();
		layout.numColumns = 2;
		controlGroup.setLayout (layout);
		controlGroup.setLayoutData (new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		size = new Button (controlGroup, SWT.CHECK);
		size.setText (LayoutExample.getResourceString ("Preferred_Size"));
		size.setSelection (false);
		size.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				if (size.getSelection ()) {
					layoutGroup.setLayout (new GridLayout ());
					layoutGroup.layout (true);
				} else {
					layoutGroup.setLayout (new FillLayout ());
					layoutGroup.layout (true);
				}
			}
		});
		GridData data = new GridData ();
		data.horizontalSpan = 2;
		size.setLayoutData (data);
		createControlWidgets ();
	}
		
	/**
	 * Creates the "control" widget children.
	 * Subclasses override this method to augment
	 * the standard controls created.
	 */
	void createControlWidgets () {
		createChildGroup ();
		code = new Button (controlGroup, SWT.PUSH);
		code.setText (LayoutExample.getResourceString ("Code"));
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_CENTER);
		gridData.horizontalSpan = 2;
		code.setLayoutData (gridData);
		code.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				final Shell shell = new Shell ();	
				shell.setLayout (new FillLayout ());
				final StyledText text = new StyledText (shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
				String layoutCode = generateCode ().toString ();
				if (layoutCode.length () == 0) return;
				text.setText (layoutCode);
				
				Menu bar = new Menu (shell, SWT.BAR);
				shell.setMenuBar (bar);
				MenuItem editItem = new MenuItem (bar, SWT.CASCADE);
				editItem.setText (LayoutExample.getResourceString ("Edit"));
				Menu menu = new Menu (bar);
				MenuItem select = new MenuItem (menu, SWT.PUSH);
				select.setText (LayoutExample.getResourceString ("Select_All"));
				select.setAccelerator (SWT.CTRL + 'A');
				select.addSelectionListener (new SelectionAdapter () {
					public void widgetSelected (SelectionEvent e) {
						text.selectAll ();
					}
				});
				MenuItem copy = new MenuItem (menu, SWT.PUSH);
				copy.setText (LayoutExample.getResourceString ("Copy"));
				copy.setAccelerator (SWT.CTRL + 'C');
				copy.addSelectionListener (new SelectionAdapter () {
					public void widgetSelected (SelectionEvent e) {
						text.copy ();
					}
				});
				MenuItem exit = new MenuItem (menu, SWT.PUSH);
				exit.setText (LayoutExample.getResourceString ("Exit"));
				exit.addSelectionListener (new SelectionAdapter () {
					public void widgetSelected (SelectionEvent e) {
						shell.close ();
					}
				});
				editItem.setMenu (menu);
				
				shell.pack ();
				shell.setSize (400, 500);
				shell.open ();
				Display display = shell.getDisplay ();
				while (!shell.isDisposed ())
					if (!display.readAndDispatch ()) display.sleep ();
			}
		});
	}
	
	/**
	 * Creates the example layout.
	 * Subclasses override this method.
	 */
	void createLayout () {
	}
	
	/**
	 * Creates the composite that contains the example layout.
	 */
	void createLayoutComposite () {
		layoutComposite = new Composite (layoutGroup, SWT.BORDER);
		createLayout ();
	}
	
	/**
	 * Creates the layout group. This is the group on the
	 * left half of each example tab.
	 */
	void createLayoutGroup (Shell tabFolder) {
		layoutGroup = new Group (tabFolderPage, SWT.NONE);
		layoutGroup.setText (LayoutExample.getResourceString("Layout"));
		layoutGroup.setLayout (new FillLayout ());
		layoutGroup.setLayoutData (new GridData (GridData.FILL_BOTH));
		createLayoutComposite ();
	}
	
	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
	Composite createTabFolderPage (TabFolder tabFolder) {
		/* Create a two column page */
		tabFolderPage = new Composite (tabFolder, SWT.NULL);
		GridLayout gridLayout = new GridLayout ();
		gridLayout.numColumns = 2;
		tabFolderPage.setLayout (gridLayout);
	
		/* Create the "layout" and "control" columns */
		createLayoutGroup (tabFolder.getShell());
		createControlGroup ();
		
		return tabFolderPage;
	}
	
	/**
	 * Creates the TableEditor with a Text in the given column
	 * of the table.
	 */
	void createTextEditor (Text text, TableEditor textEditor, int i) {
		text.setFont (table.getFont ());
		text.selectAll ();
		textEditor.horizontalAlignment = SWT.LEFT;
		textEditor.grabHorizontal = true;
		textEditor.minimumWidth = 50;
		textEditor.setEditor (text, newItem, i);
		
		text.addTraverseListener(new TraverseListener() {
        	public void keyTraversed(TraverseEvent e) {
            	if (e.detail == SWT.TRAVERSE_TAB_NEXT) {
                    resetEditors (true);
                }
                if (e.detail == SWT.TRAVERSE_ESCAPE) {
                	disposeEditors ();
                }
            }
        });
	}
	
	/** 
	 * Disposes the editors without placing their contents
	 * into the table.
	 * Subclasses override this method.
	 */
	void disposeEditors () {
	}
	
	/**
	 * Generates the code needed to produce the example layout.
	 */	
	StringBuffer generateCode () {
		/* Make sure all information being entered is stored in the table */
		resetEditors ();
		
		/* Get names for controls in the layout */
		if (children.length > 0) {
			Shell shell = tabFolderPage.getShell ();
			NameDialog dialog = new NameDialog (shell);
			dialog.setChildren (children);
			names = dialog.open ();
			if (!dialog.openText) return new StringBuffer ("");
		}
	
		/* Create StringBuffer containing the code */
		StringBuffer code = new StringBuffer ();
		code.append ("import org.eclipse.swt.*;\n");
		code.append ("import org.eclipse.swt.custom.*;\n");
		code.append ("import org.eclipse.swt.graphics.*;\n");
		code.append ("import org.eclipse.swt.layout.*;\n");
		code.append ("import org.eclipse.swt.widgets.*;\n\n");
		code.append ("public class MyLayout {\n");
		code.append ("\tpublic static void main (String [] args) {\n");
		code.append ("\t\tDisplay display = new Display ();\n");
		code.append ("\t\tShell shell = new Shell (display);\n");
		
		/* Get layout specific code */
		code.append (generateLayoutCode ());
		
		code.append ("\n\t\tshell.pack ();\n\t\tshell.open ();\n\n");
		code.append ("\t\twhile (!shell.isDisposed ()) {\n");
		code.append ("\t\t\tif (!display.readAndDispatch ())\n");
		code.append ("\t\t\t\tdisplay.sleep ();\n\t\t}\n\t\tdisplay.dispose ();\n\t}\n}");
		
		return code;
	}
	
	/**
	 * Generates layout specific code for the example layout.
	 * Subclasses override this method.
	 */
	StringBuffer generateLayoutCode () {
		return new StringBuffer ();
	}
	
	/**
	 * Returns the StringBuffer for the code which will 
	 * create a child control.
	 */
	StringBuffer getChildCode (Control control, int i) {
		StringBuffer code = new StringBuffer ();
		/* Find the type of control */
		String controlClass = control.getClass().toString ();
		String controlType = controlClass.substring (controlClass.lastIndexOf ('.') + 1);
		/* Find the style of the control */
		String styleString;
		if (controlType.equals (LayoutExample.getResourceString ("Button"))) {
			int style = control.getStyle ();
			if ((style & SWT.PUSH) != 0) styleString = "SWT.PUSH";
			else if ((style & SWT.CHECK) != 0) styleString = "SWT.CHECK";
			else if ((style & SWT.RADIO) != 0) styleString = "SWT.RADIO";
			else styleString = "SWT.NONE";
		} else if (controlType.equals (LayoutExample.getResourceString ("Label"))) {
			int style = control.getStyle ();
			if ((style & SWT.WRAP) != 0) styleString = "SWT.WRAP";
			else styleString = "SWT.NONE";
		} else if (controlType.equals (LayoutExample.getResourceString ("Text")) ||
					controlType.equals (LayoutExample.getResourceString ("StyledText"))) {
			int style = control.getStyle ();
			if ((style & SWT.MULTI) != 0) {
				styleString = "SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL";
			} else {
				styleString = "SWT.BORDER";
			}
		} else if (controlType.equals (LayoutExample.getResourceString ("Canvas")) ||
					controlType.equals (LayoutExample.getResourceString ("Composite")) || 
					controlType.equals (LayoutExample.getResourceString ("Table")) ||
					controlType.equals (LayoutExample.getResourceString ("StyledText")) ||
					controlType.equals (LayoutExample.getResourceString ("ToolBar")) || 
					controlType.equals (LayoutExample.getResourceString ("Tree")) ||
					controlType.equals (LayoutExample.getResourceString ("List"))) {
			styleString = "SWT.BORDER";
		} else styleString = "SWT.NONE";
		/* Write out the control being declared */
		code.append ("\n\t\t" + controlType + " " + names [i] + 
					 " = new " + controlType + " (shell, " + styleString + ");\n");
		/* Set text for those controls that support it */			 
		if (controlType.equals (LayoutExample.getResourceString ("Button")) ||
			controlType.equals (LayoutExample.getResourceString ("Combo")) ||
			controlType.equals (LayoutExample.getResourceString ("Group")) ||
			controlType.equals (LayoutExample.getResourceString ("Label")) ||
			controlType.equals (LayoutExample.getResourceString ("MultiLineText")) ||
			controlType.equals (LayoutExample.getResourceString ("StyledText")) ||
			controlType.equals (LayoutExample.getResourceString ("Text")) ||
			controlType.equals (LayoutExample.getResourceString ("WrappedLabel"))) {
			code.append ("\t\t" + names [i] + ".setText (\"" + names [i] + "\");\n");
		}
		/* Add items to those controls that need items */
		if (controlType.equals (LayoutExample.getResourceString ("Combo")) ||
			controlType.equals (LayoutExample.getResourceString ("List"))) {
			for (int j = 1; j < 4; j++) {
				code.append ("\t\t" + names [i] + ".add (\"Item " + j + "\");\n");
			}
		} else if (controlType.equals (LayoutExample.getResourceString ("Table"))) {
			code.append ("\t\t" + names [i] + ".setLinesVisible (true);\n");
			for (int j = 1; j < 3; j++) {
				code.append ("\t\tTableItem tableItem" + j + " = new TableItem (" + names [i] + ", SWT.NONE);\n");
				code.append ("\t\ttableItem" + j + ".setText (\"Item" + j + "\");\n");
			}
		} else if (controlType.equals (LayoutExample.getResourceString ("Tree"))) {
			for (int j = 1; j < 3; j++) {
				code.append ("\t\tTreeItem treeItem" + j + " = new TreeItem (" + names [i] + ", SWT.NONE);\n");
				code.append ("\t\ttreeItem" + j + ".setText (\"Item" + j + "\");\n");
			}
		} else if (controlType.equals (LayoutExample.getResourceString ("ToolBar"))) {
			for (int j = 1; j < 3; j++) {
				code.append ("\t\tToolItem toolItem" + j + " = new ToolItem (" + names [i] + ", SWT.NONE);\n");
				code.append ("\t\ttoolItem" + j + ".setText (\"Item" + j + "\");\n");
			}
		} else if (controlType.equals (LayoutExample.getResourceString ("CoolBar"))) {
			code.append ("\t\tToolBar coolToolBar = new ToolBar (" + names [i] + ", SWT.BORDER);\n");
			for (int j = 1; j < 3; j++) {
				code.append ("\t\tToolItem coolToolItem" + j + " = new ToolItem (coolToolBar, SWT.NONE);\n");
				code.append ("\t\tcoolToolItem" + j + ".setText (\"Item" + j + "\");\n");
			}
			code.append ("\t\tCoolItem coolItem1 = new CoolItem (" + names [i] + ", SWT.NONE);\n");
			code.append ("\t\tcoolItem1.setControl (coolToolBar);\n");
			code.append ("\t\tButton coolButton = new Button (" + names [i] + ", SWT.PUSH);\n");
			code.append ("\t\tcoolButton.setText (\"Button\");\n");
			code.append ("\t\tCoolItem coolItem2 = new CoolItem (" + names [i] + ", SWT.NONE);\n");
			code.append ("\t\tcoolItem2.setControl (coolButton);\n");
			code.append ("\t\tPoint size = coolToolBar.computeSize (SWT.DEFAULT, SWT.DEFAULT);\n");
			code.append ("\t\tcoolItem1.setSize (coolItem1.computeSize (size.x, size.y));\n");
			code.append ("\t\tcoolItem2.setSize (coolItem2.computeSize (size.x, size.y));\n");
			code.append ("\t\t" + names [i] + ".setSize (" + names [i] + ".computeSize (SWT.DEFAULT, SWT.DEFAULT));\n");
		} else if (controlType.equals (LayoutExample.getResourceString ("ProgressBar"))) {
			code.append ("\t\t" + names [i] + ".setSelection (50);\n");
		} 
		return code;
	}
		
	/**
	 * Returns the layout data column widths.
	 * Subclasses override this method.
	 */
	int [] getLayoutDataColumnWidths () {
		return new int [] {};
	}
	
	/**
	 * Returns the layout data field names.
	 * Subclasses override this method.
	 */
	String [] getLayoutDataFieldNames () {
		return new String [] {};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 * Subclasses override this method.
	 */
	String getTabText () {
		return "";
	}
	
	/**
	 * Refreshes the composite and draws all controls
	 * in the layout example.
	 */
	void refreshLayoutComposite () {
		/* Remove children that are already layed out */
		children = layoutComposite.getChildren ();
		for (int i = 0; i < children.length; i++) {
			children [i].dispose ();
		}
		/* Add all children listed in the table */
		TableItem [] items = table.getItems ();
		children = new Control [items.length];
		String [] itemValues = new String [] {
			LayoutExample.getResourceString ("Item",new String [] {"1"}),
			LayoutExample.getResourceString ("Item",new String [] {"2"}),
			LayoutExample.getResourceString ("Item",new String [] {"3"})};
		for (int i = 0; i < items.length; i++) {
			String control = items [i].getText (0);
			String name = items [i].getText (1);
			if (control.equals (LayoutExample.getResourceString ("Canvas"))) {
				Canvas canvas = new Canvas (layoutComposite, SWT.BORDER);
				children [i] = canvas;
			} else if (control.equals (LayoutExample.getResourceString ("CheckBox"))) {
				Button check = new Button (layoutComposite, SWT.CHECK);
				check.setText (LayoutExample.getResourceString ("Check_Index", new String [] {new Integer (i).toString ()}));
				children [i] = check;
			} else if (control.equals (LayoutExample.getResourceString ("Combo"))) {
				Combo combo = new Combo (layoutComposite, SWT.NONE);
				combo.setText (LayoutExample.getResourceString ("Combo_Index", new String [] {new Integer (i).toString ()}));
				combo.setItems (itemValues);
				children [i] = combo;
			} else if (control.equals (LayoutExample.getResourceString ("Composite"))) { 
				Composite composite = new Composite (layoutComposite, SWT.BORDER);
				children [i] = composite;
		    } else if (control.equals (LayoutExample.getResourceString ("CoolBar"))) { 
		    	CoolBar coolBar = new CoolBar (layoutComposite, SWT.NONE);
		    	ToolBar toolBar = new ToolBar (coolBar, SWT.BORDER);
				ToolItem item1 = new ToolItem (toolBar, 0);
				item1.setText (LayoutExample.getResourceString ("Item",new String [] {"1"}));
				ToolItem item2 = new ToolItem (toolBar, 0);
				item2.setText (LayoutExample.getResourceString ("Item",new String [] {"2"}));
		    	CoolItem coolItem1 = new CoolItem (coolBar, 0);
		    	coolItem1.setControl (toolBar);
		    	CoolItem coolItem2 = new CoolItem (coolBar, 0);
		    	Button coolButton = new Button (coolBar, SWT.PUSH);
		    	coolButton.setText (LayoutExample.getResourceString ("Button"));
		    	coolItem2.setControl (coolButton);
		    	Point size = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        		coolItem1.setSize(coolItem1.computeSize (size.x, size.y));
        		coolItem2.setSize(coolItem2.computeSize (size.x, size.y));
       			coolBar.setSize(coolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				children [i] = coolBar;
		    } else if (control.equals (LayoutExample.getResourceString ("Group"))) {
				Group group = new Group (layoutComposite, SWT.NONE);
				group.setText (LayoutExample.getResourceString ("Group_Index", new String [] {new Integer (i).toString ()}));
				children [i] = group;
			} else if (control.equals (LayoutExample.getResourceString ("Label"))) {
				Label label = new Label (layoutComposite, SWT.NONE);
				label.setText (LayoutExample.getResourceString ("Label_Index", new String [] {new Integer (i).toString ()}));
				children [i] = label;
			} else if (control.equals (LayoutExample.getResourceString ("List"))) {
				List list = new List (layoutComposite, SWT.BORDER);
				list.setItems (itemValues);
				children [i] = list;
			} else if (control.equals (LayoutExample.getResourceString ("MultiLineText"))) {
				Text multi = new Text (layoutComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
				multi.setText (LayoutExample.getResourceString ("Multi_Index", new String [] {new Integer (i).toString ()}));
				children [i] = multi;
			} else if (control.equals (LayoutExample.getResourceString ("ProgressBar"))) {
				ProgressBar progress = new ProgressBar (layoutComposite, SWT.NONE);
				progress.setSelection (50);
				children [i] = progress;
			} else if (control.equals (LayoutExample.getResourceString ("PushButton"))) {
				Button push = new Button (layoutComposite, SWT.PUSH);
				push.setText (LayoutExample.getResourceString ("Push_Index", new String [] {new Integer (i).toString ()}));
				children [i] = push;
			} else if (control.equals (LayoutExample.getResourceString ("RadioButton"))) {
				Button radio = new Button (layoutComposite, SWT.RADIO);
				radio.setText (LayoutExample.getResourceString ("Radio_Index", new String [] {new Integer (i).toString ()}));
				children [i] = radio;
			} else if (control.equals (LayoutExample.getResourceString ("Scale"))) {
				Scale scale = new Scale (layoutComposite, SWT.NONE);
				children [i] = scale;
			} else if (control.equals (LayoutExample.getResourceString ("Slider"))) {
				Slider slider = new Slider (layoutComposite, SWT.NONE);
				children [i] = slider;
			} else if (control.equals (LayoutExample.getResourceString ("StyledText"))) {
				StyledText styledText = new StyledText (layoutComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
				styledText.setText (LayoutExample.getResourceString ("StyledText_Index", new String [] {new Integer (i).toString ()}));
				children [i] = styledText;			
			} else if (control.equals (LayoutExample.getResourceString ("Table"))) {
				Table table = new Table (layoutComposite, SWT.BORDER);
				table.setLinesVisible (true);
				TableItem item1 = new TableItem (table, 0);
				item1.setText (LayoutExample.getResourceString ("Item",new String [] {"1"}));
				TableItem item2 = new TableItem (table, 0);
				item2.setText (LayoutExample.getResourceString ("Item",new String [] {"2"}));
				children [i] = table;
			} else if (control.equals (LayoutExample.getResourceString ("Text"))) {
				Text text = new Text (layoutComposite, SWT.BORDER);
				text.setText (LayoutExample.getResourceString ("Text_Index", new String [] {new Integer (i).toString ()}));
				children [i] = text;
			} else if (control.equals (LayoutExample.getResourceString ("ToolBar"))) {
				ToolBar toolBar = new ToolBar (layoutComposite, SWT.BORDER);
				ToolItem item1 = new ToolItem (toolBar, 0);
				item1.setText (LayoutExample.getResourceString ("Item",new String [] {"1"}));
				ToolItem item2 = new ToolItem (toolBar, 0);
				item2.setText (LayoutExample.getResourceString ("Item",new String [] {"2"}));
				children [i] = toolBar;
			} else if (control.equals (LayoutExample.getResourceString ("Tree"))){
				Tree tree = new Tree (layoutComposite, SWT.BORDER);
				TreeItem item1 = new TreeItem (tree, 0);
				item1.setText (LayoutExample.getResourceString ("Item",new String [] {"1"}));
				TreeItem item2 = new TreeItem (tree, 0);
				item2.setText (LayoutExample.getResourceString ("Item",new String [] {"2"}));
				children [i] = tree;
			} else {
				Label wrapped = new Label (layoutComposite, SWT.WRAP);
				wrapped.setText (LayoutExample.getResourceString ("Wrapped_Index", new String [] {new Integer (i).toString ()}));
				children [i] = wrapped;
			}
		}
	}	
	
	/**
	 * Takes information from TableEditors and stores it.
	 * Subclasses override this method.
	 */
	void resetEditors () {
		resetEditors (false);
	}
	
	void resetEditors (boolean tab) {
	}
	
	/**
	 * Sets the layout data for the children of the layout. 
	 * Subclasses override this method.
	 */
	void setLayoutData () {
	}
	
	/**
	 * Sets the state of the layout.
	 * Subclasses override this method.
	 */
	void setLayoutState () {
	}
}
