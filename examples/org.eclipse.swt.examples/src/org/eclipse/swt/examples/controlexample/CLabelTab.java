/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.controlexample;


import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class CLabelTab extends AlignableTab {
	/* Example widgets and groups that contain them */
	CLabel label1, label2, label3;
	Group textLabelGroup;

	/* Style widgets added to the "Style" group */
	Button shadowInButton, shadowOutButton, shadowNoneButton;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	CLabelTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the text labels */
		textLabelGroup = new Group(exampleGroup, SWT.NONE);
		GridLayout gridLayout = new GridLayout ();
		textLabelGroup.setLayout (gridLayout);
		gridLayout.numColumns = 3;
		textLabelGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		textLabelGroup.setText (ControlExample.getResourceString("Custom_Labels"));
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (shadowInButton.getSelection ()) style |= SWT.SHADOW_IN;
		if (shadowNoneButton.getSelection ()) style |= SWT.SHADOW_NONE;
		if (shadowOutButton.getSelection ()) style |= SWT.SHADOW_OUT;
		if (leftButton.getSelection ()) style |= SWT.LEFT;
		if (centerButton.getSelection ()) style |= SWT.CENTER;
		if (rightButton.getSelection ()) style |= SWT.RIGHT;
	
		/* Create the example widgets */
		label1 = new CLabel (textLabelGroup, style);
		label1.setText(ControlExample.getResourceString("One"));
		label1.setImage (instance.images[ControlExample.ciClosedFolder]);
		label2 = new CLabel (textLabelGroup, style);
		label2.setImage (instance.images[ControlExample.ciTarget]);
		label3 = new CLabel (textLabelGroup, style);
		label3.setText(ControlExample.getResourceString("Example_string") + "\n" + ControlExample.getResourceString("One_Two_Three"));
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup ();
		
		/* Create the extra widgets */
		shadowNoneButton = new Button (styleGroup, SWT.RADIO);
		shadowNoneButton.setText ("SWT.SHADOW_NONE");
		shadowInButton = new Button (styleGroup, SWT.RADIO);
		shadowInButton.setText ("SWT.SHADOW_IN");
		shadowOutButton = new Button (styleGroup, SWT.RADIO);
		shadowOutButton.setText ("SWT.SHADOW_OUT");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {label1, label2, label3};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"BottomMargin", "LeftMargin", "RightMargin", "Text", "ToolTipText", "TopMargin"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "CLabel";
	}
	
	/**
	 * Sets the alignment of the "Example" widgets.
	 */
	void setExampleWidgetAlignment () {
		int alignment = 0;
		if (leftButton.getSelection ()) alignment = SWT.LEFT;
		if (centerButton.getSelection ()) alignment = SWT.CENTER;
		if (rightButton.getSelection ()) alignment = SWT.RIGHT;
		label1.setAlignment (alignment);
		label2.setAlignment (alignment);
		label3.setAlignment (alignment);
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		leftButton.setSelection ((label1.getStyle () & SWT.LEFT) != 0);
		centerButton.setSelection ((label1.getStyle () & SWT.CENTER) != 0);
		rightButton.setSelection ((label1.getStyle () & SWT.RIGHT) != 0);
		shadowInButton.setSelection ((label1.getStyle () & SWT.SHADOW_IN) != 0);
		shadowOutButton.setSelection ((label1.getStyle () & SWT.SHADOW_OUT) != 0);
		shadowNoneButton.setSelection ((label1.getStyle () & (SWT.SHADOW_IN | SWT.SHADOW_OUT)) == 0);
	}
}
