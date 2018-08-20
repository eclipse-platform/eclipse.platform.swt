/*******************************************************************************
 * Copyright (c) 2012, 2016 IBM Corporation and others.
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
package org.eclipse.swt.snippets;

/*
 * Accessibility example snippet: Declare a message area to be a "live region",
 * and send "changed" events to a screen reader when the message area is updated.
 * Note: This snippet will only work with Windows screen readers.
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.*;
import org.eclipse.swt.accessibility.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Snippet363 {
    static final String errorMessage = "Error: Number expected.";
    static Label icon;
    static Image errorIcon;
    static Text liveLabel;

public static void main(String [] args) {
    Display display = new Display();
    errorIcon = display.getSystemImage(SWT.ICON_ERROR);
    Shell shell = new Shell(display);
    shell.setLayout(new GridLayout(2, false));
    shell.setText("LiveRegion Test");

    icon = new Label(shell, SWT.NONE);
    icon.setLayoutData(new GridData(32, 32));

    liveLabel = new Text(shell, SWT.READ_ONLY);
    GC gc = new GC(liveLabel);
    Point pt = gc.textExtent(errorMessage);
    GridData data = new GridData(SWT.FILL, SWT.CENTER, true, false);
    data.minimumWidth = (int) (pt.x + gc.getFontMetrics().getAverageCharacterWidth() * 2);
    gc.dispose();
    liveLabel.setLayoutData(data);
    liveLabel.setText("");
    liveLabel.getAccessible().addAccessibleAttributeListener(new AccessibleAttributeAdapter() {
            @Override
			public void getAttributes(AccessibleAttributeEvent e) {
                    e.attributes = new String[] {
                                    "container-live", "polite",
                                    "live", "polite",
                                    "container-live-role", "status",
                            };
            }
    });

    final Label textFieldLabel = new Label(shell, SWT.NONE);
    textFieldLabel.setText("Type a number:");
    textFieldLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

    final Text textField = new Text(shell, SWT.SINGLE | SWT.BORDER);
    textField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

    final Button okButton = new Button(shell, SWT.PUSH);
    okButton.setText("OK");
    okButton.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false, 2, 1));
    okButton.setEnabled(false);

    textField.addModifyListener(e -> {
	        boolean isNumber = false;
	        String textValue = textField.getText();
	        try {
	                Integer.parseInt(textValue);
	                isNumber = true;
	                setMessageText(false, "Thank-you");
	        } catch (NumberFormatException ex) {
	                if (textValue.isEmpty()) {
	                        setMessageText(false, "");
	                } else {
	                        setMessageText(true, "Error: Number expected.");
	                }
	        }
	        okButton.setEnabled(isNumber);
	});

    textField.setFocus();
    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
    }
    display.dispose();
}

static void setMessageText(boolean error, String newMessage) {
    String oldMessage = liveLabel.getText();
    icon.setImage(error ? errorIcon : null);
    liveLabel.setText(newMessage);
    liveLabel.getAccessible().sendEvent(ACC.EVENT_ATTRIBUTE_CHANGED, null);
    liveLabel.getAccessible().sendEvent(ACC.EVENT_TEXT_CHANGED, new Object[] {ACC.TEXT_DELETE, 0, oldMessage.length(), oldMessage});
    liveLabel.getAccessible().sendEvent(ACC.EVENT_TEXT_CHANGED, new Object[] {ACC.TEXT_INSERT, 0, newMessage.length(), newMessage});
}

}
