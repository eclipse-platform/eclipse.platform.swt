package org.eclipse.swt.examples.layoutexample;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.*;

/**
 * @version 	1.0
 * @author
 */
/**
 * <code>NameDialog</code> is the class that creates a 
 * dialog specific for this example. It creates a dialog
 * with a text widget for each child control on the example
 * layout. It creates default names for each child, and allows
 * the user to change these names. 
 */
public class NameDialog extends Dialog {
String [] result;
Control [] children;
boolean openText = false;

	public NameDialog (Shell parent, int style) {
		super (parent, style);
	}
	
	public NameDialog (Shell parent) {
		this (parent, 0);
	}
	
	boolean checkName (String name) {
		boolean badChar = false;
		for (int i = 0; i < name.length (); i++) {
			char check = name.charAt (i);
			if (check < '0') badChar = true;
			else if (check > '9' && check < 'A') badChar = true;
			else if (check > 'Z' && check < 'a') badChar = true;
			else if (check > 'z') badChar = true;
		}
		return badChar;
	}
	
	public void setChildren (Control [] children) {
		this.children = children;
	}
	
	public Control [] getChildren () {
		return children;
	}
 
	public String [] open () {
		Shell parent = getParent ();
		final Shell shell = new Shell (parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText (LayoutExample.getResourceString ("Name_Title"));
		GridLayout layout = new GridLayout ();
		layout.numColumns = 2;
		layout.makeColumnsEqualWidth = true;
		shell.setLayout (layout);
		
		/* Create text boxes and put default names in them */
		Label title = new Label (shell, SWT.NONE);
		title.setText (LayoutExample.getResourceString ("Name_Label"));
		GridData data = new GridData ();
		data.horizontalSpan = 2;
		title.setLayoutData (data);
		result = new String [children.length];
		for (int i = 0; i < children.length; i++) {
			Control control = children [i];
			String controlClass = control.getClass ().toString ();
			String controlType = controlClass.substring (controlClass.lastIndexOf ('.') + 1);
			Text text = new Text (shell, SWT.BORDER);
			text.setText (controlType.toLowerCase () + i);
			text.selectAll ();
			data = new GridData (GridData.FILL_HORIZONTAL);
			data.horizontalSpan = 2;
			text.setLayoutData (data);
			result [i] = text.getText ();
		}
		
		/* When 'enter' button is pressed, names are placed in the result */
		Button enter = new Button (shell, SWT.PUSH);
		enter.setText (LayoutExample.getResourceString ("Enter"));
		enter.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_END));
		enter.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				Control [] texts = shell.getChildren ();
				boolean badChars = false;
				for (int i = 1; i < texts.length - 2; i++) {
					Text name = (Text)texts [i];
					String aName = name.getText ();
					/* Make sure the name entered is valid */
					badChars = checkName (aName);
					if (badChars) {
						MessageBox message = new MessageBox (shell);
						message.setMessage (LayoutExample.getResourceString ("Invalid_Name"));
						message.open ();
						name.selectAll ();
						return;
					}
					if (name.getText ().length () != 0) {
						result [i-1] = name.getText ();
					}
				}
				openText = true;
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
		
		shell.setDefaultButton (enter);
		shell.pack ();
		/* Center the dialog */
		Point center = parent.getLocation ();
		center.x = center.x + (parent.getBounds ().width / 2) - (shell.getBounds ().width / 2);
		center.y = center.y + (parent.getBounds ().height / 2) - (shell.getBounds ().height / 2);
		shell.setLocation (center);
		shell.open ();
		Display display = parent.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		
		/* Return an array of strings representing the names of the controls */
		return result;
	}
}
