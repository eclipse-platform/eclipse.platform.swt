/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
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
 * Accessibility example snippet: use accessible relations to provide
 * additional information to an AT
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 * 
 * @since 3.6
 */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.accessibility.*;

public class Snippet350 {
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new GridLayout(2, false));
		shell.setText("Accessible Relations");

		Label nameLabel = new Label(shell, SWT.NONE);
		nameLabel.setText("Name:");
		Text nameText = new Text(shell, SWT.BORDER);
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Accessible accNameLabel = nameLabel.getAccessible();
		Accessible accNameText = nameText.getAccessible();
		accNameLabel.addRelation(ACC.RELATION_LABEL_FOR, accNameText);
		accNameText.addRelation(ACC.RELATION_LABELLED_BY, accNameLabel);

		Group addressGroup = new Group(shell, SWT.NONE);
		addressGroup.setText("Address");
		addressGroup.setLayout(new GridLayout(2, false));
		addressGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		
		Label streetLabel = new Label(addressGroup, SWT.NONE);
		streetLabel.setText("Street:");
		Text streetText = new Text(addressGroup, SWT.BORDER);
		streetText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Accessible accStreetLabel = streetLabel.getAccessible();
		Accessible accStreetText = streetText.getAccessible();
		accStreetLabel.addRelation(ACC.RELATION_LABEL_FOR, accStreetText);
		accStreetText.addRelation(ACC.RELATION_LABELLED_BY, accStreetLabel);
		
		Label cityLabel = new Label(addressGroup, SWT.NONE);
		cityLabel.setText("City:");
		Text cityText = new Text(addressGroup, SWT.BORDER);
		cityText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Accessible accCityLabel = cityLabel.getAccessible();
		Accessible accCityText = cityText.getAccessible();
		accCityLabel.addRelation(ACC.RELATION_LABEL_FOR, accCityText);
		accCityText.addRelation(ACC.RELATION_LABELLED_BY, accCityLabel);
		
		Accessible accAddressGroup = addressGroup.getAccessible();
		accStreetText.addRelation(ACC.RELATION_MEMBER_OF, accAddressGroup);
		accStreetText.addRelation(ACC.RELATION_LABELLED_BY, accAddressGroup);
		accCityText.addRelation(ACC.RELATION_MEMBER_OF, accAddressGroup);
		accCityText.addRelation(ACC.RELATION_LABELLED_BY, accAddressGroup);
		
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}