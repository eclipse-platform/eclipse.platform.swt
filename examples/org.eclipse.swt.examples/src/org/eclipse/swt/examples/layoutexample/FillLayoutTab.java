/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.layoutexample;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

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
	@Override
	void createChildWidgets () {
		/* Add common controls */
		super.createChildWidgets ();

		/* Add TableEditors */
		comboEditor = new TableEditor (table);
		nameEditor = new TableEditor (table);
		table.addMouseListener(MouseListener.mouseDownAdapter(e -> {
			resetEditors();
			index = table.getSelectionIndex();
			if (index == -1)
				return;
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
			nameText.setText(data.get(index)[NAME_COL]);
			createTextEditor(nameText, nameEditor, NAME_COL);
		}));
	}

	/**
	 * Creates the control widgets.
	 */
	@Override
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
	@Override
	void createLayout () {
		fillLayout = new FillLayout ();
		layoutComposite.setLayout (fillLayout);
	}

	/**
	 * Disposes the editors without placing their contents
	 * into the table.
	 */
	@Override
	void disposeEditors () {
		comboEditor.setEditor (null, null, -1);
		combo.dispose ();
		nameText.dispose();
	}


	/**
	 * Generates code for the example layout.
	 */
	@Override
	StringBuilder generateLayoutCode () {
		StringBuilder code = new StringBuilder ();
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
	@Override
	String[] getInsertString (String name, String controlType) {
		return new String [] {name, controlType};
	}

	/**
	 * Returns the layout data field names.
	 */
	@Override
	String [] getLayoutDataFieldNames() {
		return new String [] { "Control Name", "Control Type" };
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	@Override
	String getTabText () {
		return "FillLayout";
	}

	/**
	 * Takes information from TableEditors and stores it.
	 */
	@Override
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
			data.set (row, insert);
			for (int i = 0 ; i < TOTAL_COLS; i++) {
				oldItem.setText (i, data.get (row) [i]);
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
	@Override
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
