/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.addressbook;


/* Imports */
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;
import java.util.ResourceBundle;

/**
 * SearchDialog is a simple class that uses <code>org.eclipse.swt</code> 
 * libraries to implement a basic search dialog.
 */
public class SearchDialog {

	private static ResourceBundle resAddressBook = ResourceBundle.getBundle("examples_addressbook");

	Shell shell;
	Text searchText;
	Combo searchArea;
	Label searchAreaLabel;
	Button matchCase;
	Button matchWord;
	Button findButton;
	Button down;
	FindListener findHandler;

/**
 * Class constructor that sets the parent shell and the table widget that
 * the dialog will search.
 *
 * @param parent	Shell 
 *			The shell that is the parent of the dialog.
 */
public SearchDialog(Shell parent) {
	shell = new Shell(parent, SWT.CLOSE | SWT.BORDER | SWT.TITLE);
	GridLayout layout = new GridLayout();
	layout.numColumns = 2;
	shell.setLayout(layout);
	shell.setText(resAddressBook.getString("Search_dialog_title"));
	shell.addShellListener(new ShellAdapter(){
		public void shellClosed(ShellEvent e) {
			// don't dispose of the shell, just hide it for later use
			e.doit = false;
			shell.setVisible(false);
		}
	});
		
	Label label = new Label(shell, SWT.LEFT);
	label.setText(resAddressBook.getString("Dialog_find_what"));	
	searchText = new Text(shell, SWT.BORDER);
	GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
	gridData.widthHint = 200;
	searchText.setLayoutData(gridData);
	searchText.addModifyListener(new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			boolean enableFind = (searchText.getCharCount() != 0);
			findButton.setEnabled(enableFind);
		}
	});

	searchAreaLabel = new Label(shell, SWT.LEFT);
	searchArea = new Combo(shell, SWT.DROP_DOWN | SWT.READ_ONLY);
	gridData = new GridData(GridData.FILL_HORIZONTAL);
	gridData.widthHint = 200;
	searchArea.setLayoutData(gridData);
	
	matchCase = new Button(shell, SWT.CHECK);
	matchCase.setText(resAddressBook.getString("Dialog_match_case"));
	gridData = new GridData();
	gridData.horizontalSpan = 2;
	matchCase.setLayoutData(gridData);
	
	matchWord = new Button(shell, SWT.CHECK);
	matchWord.setText(resAddressBook.getString("Dialog_match_word"));
	gridData = new GridData();
	gridData.horizontalSpan = 2;
	matchWord.setLayoutData(gridData);
	
	Group direction = new Group(shell, SWT.NONE);
	gridData = new GridData();
	gridData.horizontalSpan = 2;
	direction.setLayoutData(gridData);
	direction.setLayout (new FillLayout ());	
	direction.setText(resAddressBook.getString("Dialog_direction"));
	
	Button up = new Button(direction, SWT.RADIO);
	up.setText(resAddressBook.getString("Dialog_dir_up"));
	up.setSelection(false);
		
	down = new Button(direction, SWT.RADIO);
	down.setText(resAddressBook.getString("Dialog_dir_down"));
	down.setSelection(true);

	Composite composite = new Composite(shell, SWT.NONE);
	gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
	gridData.horizontalSpan = 2;
	composite.setLayoutData(gridData);
	layout = new GridLayout();
	layout.numColumns = 2;
	layout.makeColumnsEqualWidth = true;
	composite.setLayout(layout);
	
	findButton = new Button(composite, SWT.PUSH);
	findButton.setText(resAddressBook.getString("Dialog_find"));
	findButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
	findButton.setEnabled(false);
	findButton.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (!findHandler.find()){
				MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK | SWT.PRIMARY_MODAL);
				box.setText(shell.getText());
				box.setMessage(resAddressBook.getString("Cannot_find") + "\"" + searchText.getText() + "\"");
				box.open();	
			}		
		}
	});
			
	Button cancelButton = new Button(composite, SWT.PUSH);
	cancelButton.setText(resAddressBook.getString("Cancel"));
	cancelButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
	cancelButton.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			shell.setVisible(false);
		}
	});
	
	shell.pack();
}
public String getSearchAreaLabel(String label) {
	return searchAreaLabel.getText();
}

public String[] getsearchAreaNames() {
	return searchArea.getItems();
}
public boolean getMatchCase() {
	return matchCase.getSelection();
}
public boolean getMatchWord() {
	return matchWord.getSelection();
}
public String getSearchString() {
	return searchText.getText();
}
public boolean getSearchDown(){
	return down.getSelection();
}
public int getSelectedSearchArea() {
	return searchArea.getSelectionIndex();
}
public void open() {
	if (shell.isVisible()) {
		shell.setFocus();
	} else {
		shell.open();
	}
	searchText.setFocus();
}
public void setSearchAreaNames(String[] names) {
	for (int i = 0; i < names.length; i++) {
		searchArea.add(names[i]);
	}
	searchArea.select(0);
}
public void setSearchAreaLabel(String label) {
	searchAreaLabel.setText(label);
}
public void setMatchCase(boolean match) {
	matchCase.setSelection(match);
}
public void setMatchWord(boolean match) {
	matchWord.setSelection(match);
}
public void setSearchDown(boolean searchDown){
	down.setSelection(searchDown);
}
public void setSearchString(String searchString) {
	searchText.setText(searchString);
}

public void setSelectedSearchArea(int index) {
	searchArea.select(index);
}
public void addFindListener(FindListener listener) {
	this.findHandler = listener;	
}
public void removeFindListener(FindListener listener) {
	this.findHandler = null;
}
}
