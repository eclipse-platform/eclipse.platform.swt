/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;
 
/*
 * Accessibility example snippet: tell a screen reader about updates
 * to a non-focused descriptive area
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.6
 */
import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet340 {
	
public static void main(String[] args) {
	Display display = new Display();
	Shell shell = new Shell(display);
	shell.setLayout(new GridLayout());
	shell.setText("Description Relation Example");
	
	final Label liveLabel = new Label(shell, SWT.BORDER);
	liveLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	liveLabel.setText("Live region messages go here");
	
	new Label(shell, SWT.NONE).setText("Type an integer from 1 to 4:");
	
	final Text textField = new Text(shell, SWT.SINGLE | SWT.BORDER);
	textField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	textField.addModifyListener(new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			String textValue = textField.getText();
			String message = textValue + " is not valid input.";
			try {
				int value = Integer.parseInt(textValue);
				switch (value) {
					case 1: message = "One for the money,"; break;
					case 2: message = "Two for the show,"; break;
					case 3: message = "Three to get ready,"; break;
					case 4: message = "And four to go!"; break;
				}
			} catch (NumberFormatException ex) {}
			liveLabel.setText(message);
			textField.getAccessible().sendEvent(ACC.EVENT_DESCRIPTION_CHANGED, null);
			textField.setSelection(0, textField.getCharCount());
		}
	});
	textField.getAccessible().addRelation(ACC.RELATION_DESCRIBED_BY, liveLabel.getAccessible());

	shell.pack();
	shell.open();
	while (!shell.isDisposed()) {
		if (!display.readAndDispatch()) display.sleep();
	}
	display.dispose();
}
}
