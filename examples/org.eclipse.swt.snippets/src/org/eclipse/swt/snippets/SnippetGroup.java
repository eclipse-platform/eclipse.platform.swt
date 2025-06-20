/*******************************************************************************
 * Copyright (c) 2025 ETAS GmbH and others, all rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     ETAS GmbH - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.snippets;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SnippetGroup {
    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setText("SWT Group Widget");
        shell.setSize(500, 500);
        shell.setLayout(new GridLayout(1, false));

        Group group1 = new Group(shell, SWT.BORDER);
        group1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        group1.setText("Students");
        group1.setLayout(new GridLayout(1, true));
        Group group = new Group(group1, SWT.BORDER);
        group.setText("User Information");

       // group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        group.setLayout(new GridLayout(2, false));

        Label nameLabel = new Label(group, SWT.NONE);
        nameLabel.setText("Name of the User:");

        Text nameText = new Text(group, SWT.BORDER);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        Label emailLabel = new Label(group, SWT.NONE);
        emailLabel.setText("Email:");

        Text emailText = new Text(group, SWT.BORDER);
        emailText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

        Button subscribeCheckbox = new Button(group, SWT.CHECK);
        subscribeCheckbox.setText("Subscribe to newsletter");
        subscribeCheckbox.setSelection(true);
        GridData checkData = new GridData();
        checkData.horizontalSpan = 2;
        subscribeCheckbox.setLayoutData(checkData);

        Button submitButton = new Button(group, SWT.PUSH);
        submitButton.setText("Submit");
        GridData buttonData = new GridData(SWT.RIGHT, SWT.CENTER, false, false);
        buttonData.horizontalSpan = 2;
        submitButton.setLayoutData(buttonData);

        submitButton.addListener(SWT.Selection, event -> {
            System.out.println("Submitted: " + nameText.getText() + ", " + emailText.getText());
        });

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }
}

