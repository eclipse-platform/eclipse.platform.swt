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
	Shell shell;
	Display display;
	/* Common groups and composites */
	Composite tabFolderPage;
	SashForm sash;
	Group layoutGroup, controlGroup, childGroup;
	/* The composite that contains the example layout */
	Composite layoutComposite;
	/* Common controls for modifying the example layout */
	String [] names;
	Control [] children;
	ToolItem add, delete, clear, code;
	int prevSelected = 0;
	/* Common values for working with TableEditors */
	Table table;
	int index;
	boolean comboReset = false;
	final String[] OPTIONS = {"Button", "Canvas", "Combo", "Composite",	"CoolBar", 
			"Group", "Label", "Link", "List", "ProgressBar", "Scale", "Slider", "StyledText",
			"Table", "Text", "ToolBar", "Tree"};
	TableItem newItem, lastSelected;
	Vector data = new Vector ();
	/* Controlling instance */
	final LayoutExample instance;

	/* Listeners */
	SelectionListener selectionListener = new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			resetEditors ();
		}
	};
		
	TraverseListener traverseListener = new TraverseListener () {
		public void keyTraversed (TraverseEvent e) {
			if (e.detail == SWT.TRAVERSE_RETURN) {
				e.doit = false;
				resetEditors ();
			}
		}
	};

	/**
	 * Creates the Tab within a given instance of LayoutExample.
	 */
	Tab(LayoutExample instance) {
		this.instance = instance;
	}
	
	/**
	 * Creates the "children" group. This is the group that allows
	 * you to add children to the layout. It exists within the
	 * controlGroup.
	 */
	void createChildGroup () {
		childGroup = new Group (controlGroup, SWT.NONE);
		childGroup.setText (LayoutExample.getResourceString("Children"));
		childGroup.setLayout(new GridLayout ());
		childGroup.setLayoutData(new GridData (SWT.FILL, SWT.FILL, true, true, 2, 1)); 

		ToolBar toolBar = new ToolBar(childGroup, SWT.FLAT);	
		toolBar.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
		add = new ToolItem(toolBar, SWT.DROP_DOWN);
		add.setText(LayoutExample.getResourceString("Add"));	
		add.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == SWT.ARROW) {
					ToolItem item = (ToolItem)event.widget;
					ToolBar bar = item.getParent ();
					final Menu menu = new Menu (shell, SWT.POP_UP);
					for (int i = 0; i < OPTIONS.length; i++) {
						final MenuItem newItem = new MenuItem (menu, SWT.RADIO);
						newItem.setText (OPTIONS [i]);						
						newItem.addSelectionListener (new SelectionAdapter () {
							public void widgetSelected (SelectionEvent event) {
								MenuItem menuItem = (MenuItem)event.widget;
								if (menuItem.getSelection ()) {
									Menu menu  = menuItem.getParent ();
									prevSelected = menu.indexOf (menuItem);
									String controlType = menuItem.getText ();
									String name = controlType.toLowerCase () + String.valueOf (table.getItemCount ());
									String [] insert = getInsertString (name, controlType);
									if (insert != null) {
										TableItem item = new TableItem (table, SWT.NONE);
										item.setText (insert);
										data.addElement (insert);
									}
									resetEditors ();
								}
							}
						});							
						newItem.setSelection (i == prevSelected);
					}
					Point pt = display.map (bar, null, event.x, event.y);
					menu.setLocation (pt.x, pt.y);
					menu.setVisible (true);
					
					while (menu != null && !menu.isDisposed () && menu.isVisible ()) {
						if (!display.readAndDispatch ()) {
							display.sleep ();
						}
					}
					menu.dispose ();
				} else {
					String controlType = OPTIONS [prevSelected];
					String name = controlType.toLowerCase () + String.valueOf (table.getItemCount ());
					String [] insert = getInsertString (name, controlType);
					if (insert != null) {
						TableItem item = new TableItem (table, 0);
						item.setText (insert);
						data.addElement (insert);
					}
					resetEditors ();
				}
			}
		});

		new ToolItem(toolBar,SWT.SEPARATOR);

		delete = new ToolItem(toolBar, SWT.PUSH);
		delete.setText (LayoutExample.getResourceString ("Delete"));
		delete.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				resetEditors ();
				int [] selected = table.getSelectionIndices ();
				table.remove (selected);
				/* Refresh the control indices of the table */
				for (int i = 0; i < table.getItemCount(); i++) {
					TableItem item = table.getItem (i);
					item.setText (0, item.getText (0));
				}
				refreshLayoutComposite ();
				layoutComposite.layout (true);
				layoutGroup.layout (true);
			}
		});
		
		new ToolItem(toolBar,SWT.SEPARATOR);
		clear = new ToolItem(toolBar, SWT.PUSH);
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
		toolBar.pack();
		
		new ToolItem (toolBar,SWT.SEPARATOR);
		code = new ToolItem (toolBar, SWT.PUSH);
		code.setText (LayoutExample.getResourceString ("Generate_Code"));
		code.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final Shell shell = new Shell();	
				shell.setText(LayoutExample.getResourceString("Generated_Code"));
				shell.setLayout(new FillLayout());
				final Text text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
				String layoutCode = generateCode().toString ();
				if (layoutCode.length() == 0) return;
				text.setText(layoutCode);
				
				Menu bar = new Menu(shell, SWT.BAR);
				shell.setMenuBar(bar);
				MenuItem editItem = new MenuItem(bar, SWT.CASCADE);
				editItem.setText(LayoutExample.getResourceString("Edit"));
				Menu menu = new Menu(bar);
				MenuItem select = new MenuItem(menu, SWT.PUSH);
				select.setText(LayoutExample.getResourceString("Select_All"));
				select.setAccelerator(SWT.MOD1 + 'A');
				select.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						text.selectAll();
					}
				});
				MenuItem copy = new MenuItem(menu, SWT.PUSH);
				copy.setText(LayoutExample.getResourceString("Copy"));
				copy.setAccelerator(SWT.MOD1 + 'C');
				copy.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						text.copy();
					}
				});
				MenuItem exit = new MenuItem(menu, SWT.PUSH);
				exit.setText(LayoutExample.getResourceString("Exit"));
				exit.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						shell.close();
					}
				});
				editItem.setMenu(menu);
				
				shell.pack();
				shell.setSize(500, 600);
				shell.open();
				while(!shell.isDisposed())
					if (!display.readAndDispatch()) display.sleep();
			}
		});
	
		createChildWidgets();
	}
	
	/**
	 * Creates the controls for modifying the "children" 
	 * table, and the table itself.
	 * Subclasses override this method to augment the
	 * standard table.
	 */
	void createChildWidgets() {
		/* Create the "children" table */
		table = new Table (childGroup, SWT.MULTI | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		FontData def[] = display.getSystemFont().getFontData();
		table.setFont(new Font(display, def[0].getName(), 10, SWT.NONE));
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gridData.heightHint = 150;
		table.setLayoutData (gridData);
		table.addTraverseListener (traverseListener);
		
		/* Add columns to the table */
		String [] columnHeaders = getLayoutDataFieldNames ();
		for (int i = 0; i < columnHeaders.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText (columnHeaders [i]);
			if (i == 0) column.setWidth (100);
			else if (i == 1) column.setWidth (90);
			else column.pack ();
		}
	}

	/**
	 * Creates the TableEditor with a CCombo in the first column
	 * of the table. This CCombo lists all the controls that
	 * the user can select to place on their layout.
	 */
	void createComboEditor (CCombo combo, TableEditor comboEditor) {
		combo.setItems (OPTIONS);
		combo.setText (newItem.getText (1));
		
		/* Set up editor */
		comboEditor.horizontalAlignment = SWT.LEFT;
		comboEditor.grabHorizontal = true;
		comboEditor.minimumWidth = 50;
		comboEditor.setEditor (combo, newItem, 1);
		
		/* Add listener */
		combo.addTraverseListener(new TraverseListener() {
        	public void keyTraversed(TraverseEvent e) {
            	if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_RETURN) {
            		comboReset = true;
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
		controlGroup = new Group (sash, SWT.NONE);
		controlGroup.setText (LayoutExample.getResourceString("Parameters"));
		GridLayout layout = new GridLayout (2, true);
		layout.horizontalSpacing = 10;
		controlGroup.setLayout (layout);
		final Button preferredButton = new Button (controlGroup, SWT.CHECK);
		preferredButton.setText (LayoutExample.getResourceString ("Preferred_Size"));
		preferredButton.setSelection (false);
		preferredButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				resetEditors ();
				GridData data = (GridData)layoutComposite.getLayoutData();
				if (preferredButton.getSelection ()) {
					data.heightHint = data.widthHint = SWT.DEFAULT;	
					data.verticalAlignment = data.horizontalAlignment = 0;
					data.grabExcessVerticalSpace = data.grabExcessHorizontalSpace = false;
				} else {
					data.verticalAlignment = data.horizontalAlignment = SWT.FILL;
					data.grabExcessVerticalSpace = data.grabExcessHorizontalSpace = true;
				}
				layoutComposite.setLayoutData (data);
				layoutGroup.layout (true);
			}
		});
		preferredButton.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false, 2, 1));	
		createControlWidgets ();
	}
		
	/**
	 * Creates the "control" widget children.
	 * Subclasses override this method to augment
	 * the standard controls created.
	 */
	void createControlWidgets () {
		createChildGroup ();
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
		layoutComposite.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		createLayout ();
	}
	
	/**
	 * Creates the layout group. This is the group on the
	 * left half of each example tab.
	 */
	void createLayoutGroup () {
		layoutGroup = new Group (sash, SWT.NONE);
		layoutGroup.setText (LayoutExample.getResourceString("Layout"));
		layoutGroup.setLayout (new GridLayout ());
		createLayoutComposite ();
	}
	
	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
	Composite createTabFolderPage (TabFolder tabFolder) {
		/* Cache the shell and display. */
		shell = tabFolder.getShell ();
		display = shell.getDisplay ();

		/* Create a two column page with a SashForm*/
		tabFolderPage = new Composite (tabFolder, SWT.NONE);
		tabFolderPage.setLayoutData (new GridData(SWT.FILL, SWT.FILL, true, true));
		tabFolderPage.setLayout (new FillLayout ());
		sash = new SashForm (tabFolderPage, SWT.HORIZONTAL);
	
		/* Create the "layout" and "control" columns */
		createLayoutGroup ();
		createControlGroup ();
		
		sash.setWeights(sashWeights ());		
		return tabFolderPage;
	}
	
	/**
	 * Return the initial weight of the layout and control groups within the SashForm.
	 * Subclasses may override to provide tab-specific weights.
	 * @return the desired sash weights for the tab page
	 */
	int[] sashWeights () {
		return new int[] {50, 50};		
	}

	/**
	 * Creates the TableEditor with a Text in the given column
	 * of the table.
	 */
	void createTextEditor (Text text, TableEditor textEditor, int column) {
		text.setFont (table.getFont ());
		text.selectAll ();
		textEditor.horizontalAlignment = SWT.LEFT;
		textEditor.grabHorizontal = true;
		textEditor.setEditor (text, newItem, column);
		
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
		names = new String [children.length];
		for (int i = 0; i < children.length; i++) {
			TableItem myItem = table.getItem(i);
			String name = myItem.getText(0);
			if (name.matches("\\d")) {
				Control control = children [i];
				String controlClass = control.getClass ().toString ();
				String controlType = controlClass.substring (controlClass.lastIndexOf ('.') + 1);
				names [i] = controlType.toLowerCase () + i;
			} else {
				names [i] = myItem.getText(0);
			}	
		}
	
		/* Create StringBuffer containing the code */
		StringBuffer code = new StringBuffer ();
		code.append ("import org.eclipse.swt.*;\n");
		code.append ("import org.eclipse.swt.layout.*;\n");
		code.append ("import org.eclipse.swt.widgets.*;\n");
		if (needsCustom ()) code.append ("import org.eclipse.swt.custom.*;\n");
		if (needsGraphics ()) code.append ("import org.eclipse.swt.graphics.*;\n");
		code.append ("\n");
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
	
	boolean needsGraphics() {
		return false;
	}

	boolean needsCustom() {
		return false;
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
		if (controlType.equals ("Button")) {
			styleString = "SWT.PUSH";
		} else if (controlType.equals ("StyledText")) {
			styleString = "SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL";
		} else if (controlType.equals ("Canvas") || controlType.equals ("Composite") || 
					controlType.equals ("Table") || controlType.equals ("StyledText") ||
					controlType.equals ("ToolBar") || controlType.equals ("Tree") ||
					controlType.equals ("List") || controlType.equals ("Text")) {
			styleString = "SWT.BORDER";
		} else styleString = "SWT.NONE";
		/* Write out the control being declared */
		code.append ("\n\t\t" + controlType + " " + names [i] + 
					 " = new " + controlType + " (shell, " + styleString + ");\n");
		/* Add items to those controls that need items */
		if (controlType.equals ("Combo") || controlType.equals ("List")) {
			code.append ("\t\t" + names [i] + ".setItems (new String [] {\"Item 1\", \"Item 2\", \"Item 2\"});\n");
		} else if (controlType.equals ("Table")) {
			code.append ("\t\t" + names [i] + ".setLinesVisible (true);\n");
			code.append ("\t\tfor (int i = 0; i < 2; i++) {\n");
			code.append ("\t\tTableItem tableItem = new TableItem (" + names [i] + ", SWT.NONE);\n");
			code.append ("\t\t\ttableItem.setText (\"Item\" + i);\n\t\t}\n");
		} else if (controlType.equals ("Tree")) {
			code.append ("\t\tfor (int i = 0; i < 2; i++) {\n");
			code.append ("\t\tTreeItem treeItem = new TreeItem (" + names [i] + ", SWT.NONE);\n");
			code.append ("\t\t\ttreeItem.setText (\"Item\" + i);\n\t\t}\n");
		} else if (controlType.equals ("ToolBar")) {
			code.append ("\t\tfor (int i = 0; i < 2; i++) {\n");
			code.append ("\t\tToolItem toolItem = new ToolItem (" + names [i] + ", SWT.NONE);\n");
			code.append ("\t\t\ttoolItem.setText (\"Item\" + i);\n\t\t}\n");
		} else if (controlType.equals ("CoolBar")) {
			code.append ("\t\tToolBar coolToolBar = new ToolBar (" + names [i] + ", SWT.BORDER);\n");
			code.append ("\t\tToolItem coolToolItem = new ToolItem (coolToolBar, SWT.NONE);\n");
			code.append ("\t\tcoolToolItem.setText (\"Item 1\");\n");
			code.append ("\t\tcoolToolItem = new ToolItem (coolToolBar, SWT.NONE);\n");
			code.append ("\t\tcoolToolItem.setText (\"Item 2\");\n");
			code.append ("\t\tCoolItem coolItem1 = new CoolItem (" + names [i] + ", SWT.NONE);\n");
			code.append ("\t\tcoolItem1.setControl (coolToolBar);\n");
			code.append ("\t\tPoint size = coolToolBar.computeSize (SWT.DEFAULT, SWT.DEFAULT);\n");
			code.append ("\t\tcoolItem1.setSize (coolItem1.computeSize (size.x, size.y));\n");
			code.append ("\t\tcoolToolBar = new ToolBar (" + names [i] + ", SWT.BORDER);\n");
			code.append ("\t\tcoolToolItem = new ToolItem (coolToolBar, SWT.NONE);\n");
			code.append ("\t\tcoolToolItem.setText (\"Item 3\");\n");
			code.append ("\t\tcoolToolItem = new ToolItem (coolToolBar, SWT.NONE);\n");
			code.append ("\t\tcoolToolItem.setText (\"Item 4\");\n");
			code.append ("\t\tCoolItem coolItem2 = new CoolItem (" + names [i] + ", SWT.NONE);\n");
			code.append ("\t\tcoolItem2.setControl (coolToolBar);\n");
			code.append ("\t\tsize = coolToolBar.computeSize (SWT.DEFAULT, SWT.DEFAULT);\n");
			code.append ("\t\tcoolItem2.setSize (coolItem2.computeSize (size.x, size.y));\n");
			code.append ("\t\t" + names [i] + ".setSize (" + names [i] + ".computeSize (SWT.DEFAULT, SWT.DEFAULT));\n");
		} else if (controlType.equals ("ProgressBar")) {
			code.append ("\t\t" + names [i] + ".setSelection (50);\n");
		} 
		/* Set text for those controls that support it */			 
		if (controlType.equals ("Button") ||
			controlType.equals ("Combo") ||
			controlType.equals ("Group") ||
			controlType.equals ("Label") ||
			controlType.equals ("Link") ||
			controlType.equals ("StyledText") ||
			controlType.equals ("Text")) {
			code.append ("\t\t" + names [i] + ".setText (\"" + names [i] + "\");\n");
		}
		return code;
	}

	/**
	 * Returns the string to insert when a new child control is added to the table.
	 * Subclasses override this method.
	 */
	String[] getInsertString (String name, String controlType) {
		return null;
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
		/* Remove children that are already laid out */
		children = layoutComposite.getChildren ();
		for (int i = 0; i < children.length; i++) {
			children [i].dispose ();
		}
		/* Add all children listed in the table */
		TableItem [] items = table.getItems ();
		children = new Control [items.length];
		String [] itemValues = new String [] {
			LayoutExample.getResourceString ("Item", new String [] {"1"}),
			LayoutExample.getResourceString ("Item", new String [] {"2"}),
			LayoutExample.getResourceString ("Item", new String [] {"3"})};
		for (int i = 0; i < items.length; i++) {
			String control = items [i].getText (1);
			String controlName = items [i].getText (0);
			if (control.equals ("Button")) {
				Button button = new Button (layoutComposite, SWT.PUSH);
				button.setText (controlName);
				children [i] = button;
			} else if (control.equals ("Canvas")) {
				Canvas canvas = new Canvas (layoutComposite, SWT.BORDER);
				children [i] = canvas;
			} else if (control.equals ("Combo")) {
				Combo combo = new Combo (layoutComposite, SWT.NONE);
				combo.setItems (itemValues);
				combo.setText (controlName);
				children [i] = combo;
			} else if (control.equals ("Composite")) { 
				Composite composite = new Composite (layoutComposite, SWT.BORDER);
				children [i] = composite;
		    } else if (control.equals ("CoolBar")) { 
		    	CoolBar coolBar = new CoolBar (layoutComposite, SWT.NONE);
		    	ToolBar toolBar = new ToolBar (coolBar, SWT.BORDER);
				ToolItem item = new ToolItem (toolBar, 0);
				item.setText (LayoutExample.getResourceString ("Item",new String [] {"1"}));
				item = new ToolItem (toolBar, 0);
				item.setText (LayoutExample.getResourceString ("Item",new String [] {"2"}));
		    	CoolItem coolItem1 = new CoolItem (coolBar, 0);
		    	coolItem1.setControl (toolBar);
				toolBar = new ToolBar (coolBar, SWT.BORDER);
				item = new ToolItem (toolBar, 0);
				item.setText (LayoutExample.getResourceString ("Item",new String [] {"3"}));
				item = new ToolItem (toolBar, 0);
				item.setText (LayoutExample.getResourceString ("Item",new String [] {"4"}));
		    	CoolItem coolItem2 = new CoolItem (coolBar, 0);
		    	coolItem2.setControl (toolBar);
		    	Point size = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        		coolItem1.setSize(coolItem1.computeSize (size.x, size.y));
        		coolItem2.setSize(coolItem2.computeSize (size.x, size.y));
       			coolBar.setSize(coolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				children [i] = coolBar;
		    } else if (control.equals ("Group")) {
				Group group = new Group (layoutComposite, SWT.NONE);
				group.setText (controlName);
				children [i] = group;
			} else if (control.equals ("Label")) {
				Label label = new Label (layoutComposite, SWT.NONE);
				label.setText (controlName);
				children [i] = label;
			} else if (control.equals ("Link")) {
				Link link = new Link (layoutComposite, SWT.NONE);
				link.setText (controlName);
				children [i] = link;
			} else if (control.equals ("List")) {
				List list = new List (layoutComposite, SWT.BORDER);
				list.setItems (itemValues);
				children [i] = list;
			} else if (control.equals ("ProgressBar")) {
				ProgressBar progress = new ProgressBar (layoutComposite, SWT.NONE);
				progress.setSelection (50);
				children [i] = progress;
			} else if (control.equals ("Scale")) {
				Scale scale = new Scale (layoutComposite, SWT.NONE);
				children [i] = scale;
			} else if (control.equals ("Slider")) {
				Slider slider = new Slider (layoutComposite, SWT.NONE);
				children [i] = slider;
			} else if (control.equals ("StyledText")) {
				StyledText styledText = new StyledText (layoutComposite, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
				styledText.setText (controlName);
				children [i] = styledText;			
			} else if (control.equals ("Table")) {
				Table table = new Table (layoutComposite, SWT.BORDER);
				table.setLinesVisible (true);
				TableItem item1 = new TableItem (table, 0);
				item1.setText (LayoutExample.getResourceString ("Item",new String [] {"1"}));
				TableItem item2 = new TableItem (table, 0);
				item2.setText (LayoutExample.getResourceString ("Item",new String [] {"2"}));
				children [i] = table;
			} else if (control.equals ("Text")) {
				Text text = new Text (layoutComposite, SWT.BORDER);
				text.setText (controlName);
				children [i] = text;
			} else if (control.equals ("ToolBar")) {
				ToolBar toolBar = new ToolBar (layoutComposite, SWT.BORDER);
				ToolItem item1 = new ToolItem (toolBar, 0);
				item1.setText (LayoutExample.getResourceString ("Item",new String [] {"1"}));
				ToolItem item2 = new ToolItem (toolBar, 0);
				item2.setText (LayoutExample.getResourceString ("Item",new String [] {"2"}));
				children [i] = toolBar;
			} else {
				Tree tree = new Tree (layoutComposite, SWT.BORDER);
				TreeItem item1 = new TreeItem (tree, 0);
				item1.setText (LayoutExample.getResourceString ("Item",new String [] {"1"}));
				TreeItem item2 = new TreeItem (tree, 0);
				item2.setText (LayoutExample.getResourceString ("Item",new String [] {"2"}));
				children [i] = tree;
			} 
		}
	}	
	
	void resetEditors () {
		resetEditors (false);
	}
	
	/**
	 * Takes information from TableEditors and stores it.
	 * Subclasses override this method.
	 */
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
