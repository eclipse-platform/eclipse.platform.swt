package org.eclipse.swt.examples.controlexample;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.custom.*;

class TextTab extends ScrollableTab {
	/* Example widgets and groups that contain them */
	Text text;
	StyledText styledText;
	Group textGroup, styledTextGroup;

	/* Style widgets added to the "Style" group */
	Button readOnlyButton;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	TextTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the text widget */
		textGroup = new Group (exampleGroup, SWT.NULL);
		textGroup.setLayout (new GridLayout ());
		textGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		textGroup.setText (ControlExample.getResourceString("Text"));
	
		/* Create a group for the styled text widget */
		styledTextGroup = new Group (exampleGroup, SWT.NULL);
		styledTextGroup.setLayout (new GridLayout ());
		styledTextGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		styledTextGroup.setText (ControlExample.getResourceString("StyledText"));
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = SWT.NONE;
		if (singleButton.getSelection ()) style |= SWT.SINGLE;
		if (multiButton.getSelection ()) style |= SWT.MULTI;
		if (horizontalButton.getSelection ()) style |= SWT.H_SCROLL;
		if (verticalButton.getSelection ()) style |= SWT.V_SCROLL;
		if (readOnlyButton.getSelection ()) style |= SWT.READ_ONLY;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
	
		/* Create the example widgets */
		text = new Text (textGroup, style);
		text.setText (ControlExample.getResourceString("Example_string"));
		text.append (text.DELIMITER);
		text.append (ControlExample.getResourceString("One_Two_Three"));
		styledText = new StyledText (styledTextGroup, style);
		styledText.setText (ControlExample.getResourceString("Example_string"));
		styledText.append ("\n");
		styledText.append (ControlExample.getResourceString("One_Two_Three"));
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
	
		/* Create the extra widgets */
		readOnlyButton = new Button (styleGroup, SWT.CHECK);
		readOnlyButton.setText ("SWT.READ_ONLY");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {text, styledText};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return ControlExample.getResourceString("Text");
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		readOnlyButton.setSelection ((text.getStyle () & SWT.READ_ONLY) != 0);
	}
}
